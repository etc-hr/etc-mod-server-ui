package com.etc.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.io.FileUtils;

import com.etc.CoreException;
import com.etc.corvetto.CoreVersionException;
import com.etc.corvetto.CorvettoConnection;
import com.etc.utils.crypto.Cryptographer;
import com.etc.utils.ws.CoreConnection;
import com.etc.utils.xarriot.Xarriot;

import javafx.application.Application;
import src.com.etc.admin.utils.Utils;

public class EmsApp extends Xarriot implements RejectedExecutionHandler {

	private static final long serialVersionUID = -4292301964138169951L;
	
	/************************************************************************************************
	 * 									CONFIGURATION SETTINGS										*
	 ************************************************************************************************/
	
	/**
	 * <p>
	 * CFG_LEML						["CONFIG LAST EMAIL USED"]<br>
	 * This is the property key for the last email if it was saved/used.</p>
	 */
	public static final String CFG_LEML = "com.etc.admin.EmsApp.lastEmail";
	/**
	 * <p>
	 * JPA_HBM2DDL					["PERSISTENCE DDL SETTING FOR HIBERNATE"]<br>
	 * Set this property to modify the provided persistence unit's ddl setting.<br>
	 * By default the persistence unit will have a setting of 'update'.</p>
	 */
	public static final String JPA_HBM2DDL = "hibernate.hbm2ddl.auto";
	
	/**
	 * <p>
	 * JPA_HBCONN_URL					[STRING]<br>
	 * Set this property to override the default value in the persistence file<br>
	 * for the database url. This may be necessary if using an engine such as h2.</p>
	 */
	public static final String JPA_HBCONN_URL = "hibernate.connection.url";
	
	/**
	 * <p>
	 * JPA_HBCONN_USR					[STRING]<br>
	 * Set this property to override the default value in the persistence file.<p>
	 */
	public static final String JPA_HBCONN_USR = "hibernate.connection.username";
	
	/**
	 * <p>
	 * JPA_HBCONN_PW					[STRING]<br>
	 * Set this property to override the default value in the persistence file.<p>
	 */
	public static final String JPA_HBCONN_PW = "hibernate.connection.password";
	
	/**
	 * <p>
	 * MAX_QUEUE_SIZE					[INTEGER]<br>
	 *  This property sets the size of the request queue.</p>
	 */
	public static final String MAX_QUEUE_SIZE = "com.etc.admin.data.queues";

	/**
	 * <p>
	 * DEFAULT_QUEUES is set to 6 queued requests.</p>
	 */
	public static final String DEFAULT_QUEUES = "100";
	
	/**
	 * <p>
	 * MAX_THREAD_COUNT					[INTEGER]<br>
	 * Set this property to tell the system how many threads to use for<br>
	 * data fetching and updating.</p>
	 */
	public static final String MAX_THREAD_COUNT = "com.etc.admin.data.threads";
	
	/**
	 * <p>
	 * DEFAULT_THREAD_COUNT is set to 4 threads.</p>
	 */
	public static final String DEFAULT_THREAD_COUNT = "8";
	
	
	private static final String DEFAULT_CFG_LEML = "";
	private static final String PERSISTENCE_UNIT = "corvetto";
	private static final String LOCAL_PERSISTENCE_UNIT = "adminapp_corvetto";
	private static final String APP_CFG_FILENAME = "app.properties";
	private static final String APP_LOG_CFG_FILENAME	= "logging.properties";		//THE APP'S DEFAULT LOGGER CONFIGURATION FILE
	
	/************************************************************************************************
	 * 											MEMBERS												*
	 ************************************************************************************************/
	
	private static final String HOMEFOLDER_NAME = "adminapp";
	private static final String SUBFOLDER_NAMES = "config;keys;tmp;logs;db";
	private static final String SOFTWARE_ID = "a3980a6f-3c50-11ea-ac3a-0a6f9cb53658";
	private static final String VERSION = "0.0.58";
	public static final String DB_VERSION = "0.1.28";
	
	
	private Logger logr;
	private PublicKey coreKey;
//	private EntityManagerFactory localEmFactory;
	private static EmsApp instance;
	private Properties appProperties;
	private static String[] systemArgs;
	private LinkedBlockingQueue<Runnable> mainQueue;
	private ThreadPoolExecutor mainExecutor;
	private LinkedBlockingQueue<Runnable> fxQueue;
	private ThreadPoolExecutor fxExecutor;
	private Timer queueCheckTimer; 

	public static void main(String[] args) 
	{
		try
		{
			systemArgs = args;
			(new EmsApp(Xarriot.argsToProperties(args))).start();
		}catch(Exception e)
		{
			if(e instanceof CoreVersionException)
			{
				String[] messageArg = new String[] {"Please update your EmsApp. Your version: " + VERSION + "\n" + e.getMessage()};
				Application.launch(UpdateApp.class, messageArg);
			}
			e.printStackTrace();
		}
	}

	public EmsApp() { super(); }
	
	public EmsApp(Properties props) throws CoreException { super(props); }
	
	@Override
	public void initApplication() throws CoreException 
	{
		//initialize instance variable
		instance = this;
		//init logr
		logr = Logger.getLogger(this.getClass().getCanonicalName());
		
		Logger.getLogger(this.getClass().getCanonicalName()).info("Initializing EmsApp...");
	}

	@Override
	public void startApplication() throws CoreException 
	{
		Logger.getLogger(this.getClass().getCanonicalName()).info("Starting EmsApp...");
		Logger.getLogger(this.getClass().getCanonicalName()).warning("App Version: " + VERSION + ", DB Version: " + DB_VERSION);
		//RELEASE PROPERTIES FROM INIT
		appProperties = null;
		
		//VALIDATE XARRIOT STATUS
		if(isInitialized())
		{

			try {
				System.setOut(new PrintStream(new File(EmsApp.getInstance().getHomeFolder().getSubFolder("logs", false).getAbsolutePath().concat(File.separator).concat("server.out"))));
				//System.setErr(new PrintStream(new File(EmsApp.getInstance().getHomeFolder().getSubFolder("logs", false).getAbsolutePath().concat(File.separator).concat("server.err"))));
			} catch (FileNotFoundException | CoreException e) {
				logr.log(Level.SEVERE, "Exception.", e);
			}
			
			File pubk;
			try
			{
				FileUtils.copyToFile(this.getClass().getClassLoader().getResourceAsStream("META-INF/com_rsa.pub"),
						 pubk = new File(getHomeFolder().getSubFolder("keys", true).getAbsolutePath().concat(File.separator).concat("com_rsa.pub")));
				if((coreKey = Cryptographer.loadPublicKey(pubk)) == null)
					throw new CoreException("Unable to load PublicKey. SSN Encryption will be disabled.");
				logr.info("");
			}catch(IOException e) { logr.log(Level.SEVERE, "Exception. ", e); }
			finally
			{ pubk = null; }	
		}
		//START THREADPOOL
		startTheadPoolQueue();
		//START FX THREADPOOL
		startFxThreadPoolQueue();
		//LAUNCH ON THE JAVAFX THREAD
		Application.launch(EtcAdmin.class, systemArgs);
	}
	
	@Override
	public void stopApplication() throws CoreException 
	{
		logr.warning("Stopping EmsApp...");
	}

	@Override
	public void destroyApplication() 
	{
		logr.warning("Destroying EmsApp...");
		instance = null;
		if (queueCheckTimer != null)
			queueCheckTimer.cancel();
		if (mainExecutor != null)
			mainExecutor.shutdown();
		if (fxExecutor != null)
			fxExecutor.shutdown();
		System.exit(1);
	}

	@Override
	public String getApplicationHomeFolderName() { return HOMEFOLDER_NAME.concat((Boolean.valueOf(System.getProperty(PROD_ENV, Boolean.TRUE.toString())) ? "" : "_dev")); }

	@Override
	public String getApplicationSubFolderNames() { return SUBFOLDER_NAMES; }

	@Override
	public Properties getApplicationProperties() 
	{
		if(appProperties == null)
		{
			appProperties = new Properties();
			
			//ENABLE HIBERNATE/JPA USAGE AND PERSISTENCE LAYER
			appProperties.setProperty(Xarriot.ENABLE_JPA, Boolean.FALSE.toString());
			appProperties.setProperty(Xarriot.JPA_PERSISTENCE_UNIT, LOCAL_PERSISTENCE_UNIT);
			//ENABLE CORVETTO API
			appProperties.setProperty(Xarriot.ENABLE_API, Boolean.FALSE.toString());
			//ENABLE SDX API
			appProperties.setProperty(Xarriot.ENABLE_SDX, Boolean.FALSE.toString());
			//SET THREAD POOL SIZE
			appProperties.setProperty(MAX_THREAD_COUNT, DEFAULT_THREAD_COUNT);
			//SET QUEUE SIZE
			appProperties.setProperty(MAX_QUEUE_SIZE, DEFAULT_QUEUES);
			//SET VERSION
			appProperties.setProperty(CorvettoConnection.APP_VSN, VERSION);
			try
			{
				if(getHomeFolder() != null)
					if(getHomeFolder().getSubFolder("db", false) != null)
						//TODO: IF THE VERSION PREFIX MAJOR VERSION DOESN'T MATCH, RESET DB??
						if(!(new File(getHomeFolder().getSubFolder("db", false).getAbsolutePath().concat(File.separator).concat("adminappdb").concat("_").concat(DB_VERSION).concat(".mv.db"))).isFile())
						{
							//delete db folder and reset
							FileUtils.deleteDirectory(getHomeFolder().getSubFolder("db", false));
							
							appProperties.setProperty(JPA_HBM2DDL, "create");
							appProperties.setProperty(JPA_HBCONN_URL, "jdbc:h2:file:".concat(getHomeFolder().getSubFolder("db", true)
									.getAbsolutePath().concat(File.separator).concat("adminappdb").concat("_").concat(DB_VERSION)).concat(";").concat("DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL;DATABASE_TO_LOWER=TRUE;AUTO_SERVER=TRUE;CIPHER=AES;"));
						}else
						{
							appProperties.setProperty(JPA_HBM2DDL, "update");
						}
			}
			catch(CoreException | IOException e) { logr.log(Level.SEVERE, "Local Db Properties Configuration Exception. ", e); }
		}
		
		return appProperties;
	}

	@Override
	public Properties getApplicationLoggingProperties() 
	{
		Properties props = new Properties();
		
		//DEFAULT LOGGER LEVEL FOR ADMIN APP
		props.setProperty("com.etc.admin.level", Level.CONFIG.toString());
		props.setProperty("com.etc.utils.level", Level.WARNING.toString());
		props.setProperty("com.etc.corvetto.level", Level.SEVERE.toString());
		props.setProperty("java.util.logging.FileHandler.level", Level.CONFIG.toString());
		props.setProperty("java.util.logging.ConsoleHandler.level", Level.CONFIG.toString());
		
		return props;
	}

	@Override
	public String getApplicationSoftwareId() { return SOFTWARE_ID; }


	/************************************************************************************************
	 * 									 ADMINAPP METHODS											*
	 ************************************************************************************************/
	
	public static EmsApp getInstance() { return instance; }
	
	public boolean isServerInstance() { return false; }
	
	public LinkedBlockingQueue<Runnable> getQueue() { return this.mainQueue; }
	
	public ThreadPoolExecutor getExecutor() { return this.mainExecutor; }
	
	public LinkedBlockingQueue<Runnable> getFxQueue() { return this.fxQueue; }
	
	public ThreadPoolExecutor getFxExecutor() { return this.fxExecutor; }
	
	/**
	 * <p>fetches the public CoreKey, used for SSN encyption, <br>
	 * if it has been loaded.</p>
	 * 
	 * @return PublicKey coreKey
	 */
	public PublicKey getCoreKey() { return coreKey; }

	
	/*
	 * <p>Returns a string containing the full path the current local db file in use. </p>
	 * 
	 */
	public String getDbPath() {
		String Path = "";
		try {
			Path = getHomeFolder().getSubFolder("db", false).getAbsolutePath().concat(File.separator).concat("adminappdb").concat("_").concat(DB_VERSION).concat(".mv.db");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return Path;
	}
	

	/**
	 * <p>
	 * createLocalPersistenceContext uses a Map set of properties<br>
	 * to create a local database connection. The username and pass<br>
	 * are provided by the user during login.</p>
	 * 
	 * @return boolean successful creation of local db context
	 * @throws CoreException
	 */
	public boolean createLocalPersistenceContext() throws CoreException
	{ 
		Map<String,String> localDbConfig = new HashMap<String,String>();
		//read in props and user info
		try
		{
			if(getProperties() != null)
			{
				if(getProperties().getProperty(CorvettoConnection.USER_PWD) == null || getProperties().getProperty(CorvettoConnection.USER_EMAIL) == null)
					throw new CoreException("Invalid Configuration Property. Failed to start local db context.");
				localDbConfig.put(JPA_HBCONN_USR, "adminapp_dbusr");
				localDbConfig.put(JPA_HBCONN_PW, Utils.localDecryptString("pQg7Tq/EtQFbdsQI6NSJPw=="));
				logr.warning("hbm2ddl  ::" + getProperties().getProperty(JPA_HBM2DDL) + "::");
				//database DLL defaults to create
				localDbConfig.put(JPA_HBM2DDL, getProperties().getProperty(JPA_HBM2DDL, "create"));
				localDbConfig.put(JPA_HBCONN_URL, getProperties().getProperty(JPA_HBCONN_URL, "jdbc:h2:file:".concat(getHomeFolder().getSubFolder("db", false).getAbsolutePath()).concat(File.separator)
						.concat("adminappdb").concat("_").concat(DB_VERSION).concat(";").concat("DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL;DATABASE_TO_LOWER=TRUE;AUTO_SERVER=TRUE;CIPHER=AES;")));
				
				return Xarriot.getInstance().enableJpa(true, localDbConfig);
			}
		}finally
		{
			localDbConfig = null;
		}
		return false;
	}

	public boolean updateAppConfigProperties(final Properties props) throws CoreException
	{
		File cfgFile = null;
		Properties cfgProps = new Properties();
		
		try
		{
			if(props != null)
			{
				if((cfgFile = new File(getHomeFolder().getSubFolder("config", false).getAbsolutePath().concat(File.separator).concat(APP_CFG_FILENAME))).exists())
				{
					try (FileInputStream fis = new FileInputStream(cfgFile))
					{
						cfgProps.load(fis);
						for(String key : props.stringPropertyNames())
							cfgProps.put(key, props.getProperty(key));
						
					}
					try (FileOutputStream fos = new FileOutputStream(cfgFile))
					{
						cfgProps.store(fos, "APPLICATION CONFIGURATION FILE");
					}
					return true;
				}else
					throw new CoreException("Invalid Config File.");
			}else
				throw new CoreException("Invalid Properties supplied.");
		}catch(CoreException | IOException e)
		{
			logr.log(Level.SEVERE, "Exception.", e);
			return false;
		}
	}
	
	public boolean updateAppLoggingProperties(final Properties props) throws CoreException
	{
		File cfgFile = null;
		Properties cfgProps = new Properties();
		
		try
		{
			if(props != null)
			{
				if((cfgFile = new File(getHomeFolder().getSubFolder("config", false).getAbsolutePath().concat(File.separator).concat(APP_LOG_CFG_FILENAME))).exists())
				{
					try (FileInputStream fis = new FileInputStream(cfgFile))
					{
						cfgProps.load(fis);
						for(String key : props.stringPropertyNames())
							cfgProps.put(key, props.getProperty(key));
						
					}
					try (FileOutputStream fos = new FileOutputStream(cfgFile))
					{
						cfgProps.store(fos, "APPLICATION LOGGING CONFIGURATION FILE");
					}
					
					cfgFile = new File(getHomeFolder().getSubFolder("config", false).getAbsolutePath().concat(File.separator).concat(APP_LOG_CFG_FILENAME));
					try (FileInputStream fis = new FileInputStream(cfgFile))
					{
						LogManager.getLogManager().readConfiguration(fis);
					}
					return true;
				}else
					throw new CoreException("Invalid Logging Config File.");
			}else
				throw new CoreException("Invalid Properties supplied.");
		}catch(CoreException | IOException e)
		{
			logr.log(Level.SEVERE, "Exception.", e);
			return false;
		}
	}
	
	/**
	 * <p>
	 * getLocalEntityManager returns an entityManager instance for each call<br>
	 * referencing the local database. </p>
	 * 
	 * @return EntityManager
	 * @throws CoreException
	 */
//	public EntityManager getLocalEntityManager() throws CoreException
//	{
//		if(localEmFactory != null && localEmFactory.isOpen())
//			return localEmFactory.createEntityManager();
//		else
//			throw new CoreException("EntityManagerFactory is not initialized.");
//	}
	
	
	/**
	 * <p>
	 * enableCorvettoApi uses the provided username and password<br>
	 * to create a new CorvettoConnection and establish a connection.</p>
	 * 
	 * @param email
	 * @param pass
	 * @return boolean enabled
	 * @throws CoreException
	 */
	public boolean enableCorvettoApi(final String email, final String pass) throws CoreException
	{
		boolean enabled = false;
		if((email != null && !email.isEmpty()) && (pass != null && !pass.isEmpty()))
		{
			getProperties().setProperty(CorvettoConnection.USER_EMAIL, email);
			getProperties().setProperty(CorvettoConnection.USER_PWD, pass);
			getProperties().setProperty(CoreConnection.CNX_WAIT_TIME, String.valueOf(5000));
			
			logr.warning("Email prop: " + getProperties().getProperty(CorvettoConnection.USER_EMAIL));
			logr.warning("Pass prop: " + getProperties().getProperty(CorvettoConnection.USER_PWD));
			try
			{
				enabled = Xarriot.getInstance().enableApi(true);
			}catch(CoreException e) { logr.log(Level.SEVERE, "Unable to create Corvetto API Connection. Exception: ", e); }
		}else
			throw new CoreException("Invalid Login Parameters.");
		return enabled;
	}
	
	/**
	 * <p>
	 * startThreadPoolQueue is called to spin up the thread(s) and queue(s) used to update the<br>
	 * local database.</p>
	 * 
	 * @throws CoreException
	 */
	public void startTheadPoolQueue() throws CoreException
	{
		int queueSize;
		int maxThreads;
		try
		{
			if((queueSize = Integer.parseInt(getProperties().getProperty(MAX_QUEUE_SIZE))) > 0)
			{
				mainQueue = new LinkedBlockingQueue<Runnable>();
				mainExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, mainQueue, Executors.privilegedThreadFactory(), this);
				
				logr.warning("Started Main Thread Pool. Threads: " + mainExecutor.prestartAllCoreThreads());
				
				queueCheckTimer = new Timer("EmsApp.QueueTimer");
				queueCheckTimer.scheduleAtFixedRate((new TimerTask()
				{
					@Override public void run()
					{
						logr.info("EmsApp Queue slots available=[" + mainQueue.remainingCapacity() + "].");
					};
				}), 30000L, 30000L);
			}else
				throw new CoreException("Invalid Thread Pool and Queue properties. Cannot start thread pool.");
		}catch(IllegalArgumentException e)
		{
			throw new CoreException("Exception." , e);
		}
	}
	
	public void startFxThreadPoolQueue() throws CoreException
	{
		int maxThreads;
		
		try
		{
			if((maxThreads = Integer.parseInt(getProperties().getProperty(MAX_THREAD_COUNT))) > 0)
			{
				fxQueue = new LinkedBlockingQueue<Runnable>();
				fxExecutor = new ThreadPoolExecutor(maxThreads, maxThreads, 0L, TimeUnit.SECONDS, fxQueue, Executors.privilegedThreadFactory(), this);
				
				logr.warning("Started FX Thread Pool. Threads: " + fxExecutor.prestartAllCoreThreads());
			}
		}catch(IllegalArgumentException e)
		{
			throw new CoreException("Exception.", e);
		}
	}

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean registerEndpoint() throws CoreException 
	{
		if(!getProperties().contains(Xarriot.SYSTEM_INSTANCE_ID))
		{
			getProperties().setProperty(CorvettoConnection.USER_EMAIL, "sysadmin@etclite.com");
			getProperties().setProperty(CorvettoConnection.USER_PWD, "#A1zTL$6F");
			
			try 
			{
				//turn on api using admin credentials to create CoreSystem records
				if(Xarriot.getInstance().enableApi(true))
					Xarriot.getInstance().enableApi(false);
				else
				{
					Xarriot.getInstance().enableApi(false);
					throw new CoreException("Unable to properly register endpoint.");
				}

			}catch (Exception e) {
				//Utils.showAlert("Version Issue", e.getMessage());
				throw e;
			}finally
			{
				getProperties().remove(CorvettoConnection.USER_EMAIL);
				getProperties().remove(CorvettoConnection.USER_PWD);
			}
		}
		
		return false;
	}

	@Override
	public boolean checkIn() throws CoreException {
		// TODO Auto-generated method stub
		return false;
	}
	
}
