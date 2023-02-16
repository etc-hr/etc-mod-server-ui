package com.etc.admin.ui.uploader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.data.DataManager;
import com.etc.corvetto.UploadException;
import com.etc.utils.EndpointFactory;
import com.etc.utils.EndpointFactory.EndpointIdentifier;
import com.etc.utils.sec.KeyStore;

import javafx.concurrent.Task;

/**
 * <p>
 * LoadingTask initializes the Uploader for use prior to authentication.</p>
 * 
 * @author Pete Morgan 10/29/2018
 */
public class LoadingTask extends Task<Boolean>
{
	private static int steps = 7;

	@Override
	protected Boolean call() throws Exception
	{
		boolean successful = false;

		try
		{
			//AUTHENTICATE USER
			if(successful = loadAgent())
			{
//				updateProgress(steps, steps);
//				updateMessage("Click 'Upload' to begin a transfer...");
			}
		}catch(UploadException e)
		{
			successful = false;
			updateProgress(steps, steps);
			updateMessage(e.getMessage());
		}
		return successful;
	}

	/**
	 * TODO: UPDATE COMMENTS
	 * 
	 * <p>
	 * The loadAgent method looks for and configures the following:
	 *     1. The user.home/.upload/ folder.
	 *     2. A log file at user.home/.etc/sfta.log.
	 *     3. A properties file at user.home/.etc/.sfta.properties.
	 *     4. An upload and download template file in user.home/.etc/template.sful/.sfdl.
	 *     5. The user.home/.etc/.sfta/ working transfer file folder.
	 * If the method doesn't find any of these items, it attempts to create them.
	 * 
	 * @return true if the sfta's properties object has been loaded with data.
	 * @throws UploadException 
	 */
	private boolean loadAgent() throws UploadException
	{
		boolean configured = false;

		//RESOURCES
		File cfgfile = null;
		FileOutputStream fos = null;
		FileInputStream fis = null;
		Properties cfgprops = null;
		DataManager.i().mProperty = new Properties();
		EndpointIdentifier ei = null;

		try
		{
			//STEP 1: START MESSAGE
//			updateMessage("Configuring the Uploader for use...");
//			updateProgress(1, steps);
			Thread.sleep(DataManager.i().mWaitTime);

			//STEP 2: BEGIN HOMEFOLDER CONFIGURATION
//			updateMessage("Configuring files...");
//			updateProgress(2, steps);
			Thread.sleep(DataManager.i().mWaitTime);

			//STEP 3: CREATE HOMEFOLDER
//			UploadDataManager.setHomeFolder(new HomeFolder(UploadDataManager.HOMEFOLDER_NAME));

			//STEP 4: CREATE CONFIG FILE
			cfgfile = new File(EmsApp.getInstance().getHomeFolder().getSubFolder("config", false).getAbsolutePath().concat(File.separator).concat(DataManager.CFGFILE_NAME));
			cfgfile.setReadable(true, false);
			cfgfile.setWritable(true, false);
			if(!cfgfile.exists())
			{
//				updateMessage("Creating configuration file for first-time use...");
//				updateProgress(3, steps);
				Thread.sleep(DataManager.i().mWaitTime);

				//CONFIGURE DEFAULT PROPERTIES
				cfgprops = new Properties();

				//CREATE NEW CONFIGURATION FILE WITH DEFAULTS
				cfgprops.setProperty(DataManager.CFG_LDIR_KEY, System.getProperty("user.home"));
				cfgprops.setProperty(DataManager.CFG_LEML_KEY, "");	//NOTE: INTENTIONALLY BLANK.
				cfgprops.setProperty(KeyStore.KEY_STORE_LOCATION, EmsApp.getInstance().getHomeFolder().getSubFolder("keys", false).getAbsolutePath());
				cfgprops.setProperty(KeyStore.ENABLE_CACHING, Boolean.TRUE.toString());
//				cfgprops.setProperty(UploadDataManager.CFG_VSN_KEY, UploadDataManager.VERSION);

				//GENERATE ENDPOINTIDENTIFIER
				if((ei = EndpointFactory.createEndpointIdentifier()) != null)
					cfgprops.setProperty("com.etclite.core.upload.Uploader.uuidv2", ei.getUUID());
				else
					throw new UploadException("LoadingTask.loadAgent: Invalid Endpoint Identifier.");

				//SAVE PROPERTIES
				fos = new FileOutputStream(cfgfile);
				cfgprops.store(fos, "UPLOADER CONFIGURATION FILE");
				fos.flush();
				fos = null;
				cfgprops = null;
			}
			cfgfile = null;

//			updateMessage("Loading configuration...");
//			updateProgress(4, steps);
			Thread.sleep(DataManager.i().mWaitTime);

			//STEP 5: LOAD CONFIGURATION FILE
			cfgfile = new File(EmsApp.getInstance().getHomeFolder().getSubFolder("config", false).getAbsolutePath().concat(File.separator).concat(DataManager.CFGFILE_NAME));

			//LOAD CONFIGURATION FILE PROPERTIES
			fis = new FileInputStream(cfgfile);
			DataManager.i().mProperty.load(fis);
			fis.close();
			fis = null;
//			cfgfile = null;

			//STEP 6: SET DEFAULTS FOR MISSING PROPERTIES
			DataManager.i().mProperty.setProperty(DataManager.CFG_LDIR_KEY, DataManager.i().mProperty.getProperty(DataManager.CFG_LDIR_KEY,
													System.getProperty(DataManager.CFG_LDIR_KEY, DataManager.i().mProperty.getProperty(DataManager.CFG_LDIR_KEY, ""))));
			DataManager.i().mProperty.setProperty(DataManager.CFG_LEML_KEY, DataManager.i().mProperty.getProperty(DataManager.CFG_LEML_KEY,
													System.getProperty(DataManager.CFG_LEML_KEY, DataManager.i().mProperty.getProperty(DataManager.CFG_LEML_KEY, ""))));
			DataManager.i().mProperty.setProperty(KeyStore.KEY_STORE_LOCATION, DataManager.i().mProperty.getProperty(KeyStore.KEY_STORE_LOCATION,
													System.getProperty(KeyStore.KEY_STORE_LOCATION, DataManager.i().mProperty.getProperty(KeyStore.KEY_STORE_LOCATION,
													EmsApp.getInstance().getHomeFolder().getSubFolder("keys", false).getAbsolutePath()))));
			DataManager.i().mProperty.setProperty(KeyStore.ENABLE_CACHING, DataManager.i().mProperty.getProperty(KeyStore.ENABLE_CACHING,
													System.getProperty(KeyStore.ENABLE_CACHING, DataManager.i().mProperty.getProperty(KeyStore.ENABLE_CACHING, Boolean.TRUE.toString()))));

			//VALIDATE REQUIRED CHILD PROPERTIES
//			if(!UploadDataManager.getProperties().containsKey(UploadDataManager.CFG_VSN_KEY) || 
//					(UploadDataManager.getProperties().containsKey(UploadDataManager.CFG_VSN_KEY) ? !UploadDataManager.getProperties().get(UploadDataManager.CFG_VSN_KEY).equals(UploadDataManager.VERSION) : false))
//			{
//				cfgfile.delete();
//				throw new UploadException("Missing Version. Please Restart.");
//			}

			//LOAD UUID
			if(!DataManager.i().mProperty.containsKey(DataManager.CFG_UUID_KEY))
			{
				cfgfile.delete();
				throw new UploadException("Missing Endpoint ID. Please Restart.");
			}

			//LOAD LAST LOGIN
			if(DataManager.i().mProperty.getProperty(DataManager.CFG_LEML_KEY).length() > 0)
				DataManager.i().mEmail = DataManager.i().mProperty.getProperty(DataManager.CFG_LEML_KEY);

			//LOAD LAST UPLOAD FROM DIRECTORY
			if(DataManager.i().mProperty.getProperty(DataManager.CFG_LDIR_KEY).length() > 0)
				DataManager.i().mUploadFromDirectory = DataManager.i().mProperty.getProperty(DataManager.CFG_LDIR_KEY);

			//STEP 7: CREATE ENDPOINTIDENTIFIER
			if(ei == null)
				ei = EndpointFactory.createEndpointIdentifier(DataManager.i().mProperty.getProperty(DataManager.CFG_UUID_KEY));

//			updateMessage("Configuring logging...");
//			updateProgress(5, steps);
			Thread.sleep(DataManager.i().mWaitTime);

			//STEP 8: CREATE DEFAULT LOGGING CONFIGURATION FILE
			cfgfile = new File(EmsApp.getInstance().getHomeFolder().getSubFolder("config", false).getAbsolutePath().concat(File.separator).concat(DataManager.LOGCFGFILE_NAME));
			cfgfile.setReadable(true, false);
			cfgfile.setWritable(true, false);
			if(!cfgfile.exists())
			{
				//CONFIGURE DEFAULT PROPERTIES
				cfgprops = new Properties();

				//CREATE NEW CONFIGURATION FILE WITH DEFAULTS

				//CONSOLEHANDLER SETTINGS - OFF BY DEFAULT
				cfgprops.setProperty("java.util.logging.ConsoleHandler.level", "OFF");
				cfgprops.setProperty("java.util.logging.ConsoleHandler.formatter", "java.util.logging.SimpleFormatter");

				//SIMPLEFORMATTER SETTINGS
				cfgprops.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %2$s\\: %5$s%n%6$s%n");

				//SET COREAPPLICATION LOGGER
				cfgprops.setProperty("com.etclite.core.level", "ALL");
				cfgprops.setProperty("com.etclite.core.CoreApplication.level", "ALL");

				//GLOBAL LOGGING SETTINGS
				cfgprops.setProperty("handlers", "java.util.logging.FileHandler,java.util.logging.ConsoleHandler");
				cfgprops.setProperty(".level", "ALL");	//DEFAULT LOG LEVEL IS ALL LOG MESSAGES
				cfgprops.setProperty("org.jboss.level", "SEVERE");
				cfgprops.setProperty("org.hibernate.level", "SEVERE");
				cfgprops.setProperty("com.mchange.level", "SEVERE");
				cfgprops.setProperty("com.sun.jmx.level", "SEVERE");
				cfgprops.setProperty("javax.management.level", "SEVERE");
				cfgprops.setProperty("sun.net.www.protocol.http.HttpURLConnection.level", "SEVERE");

				//FILEHANDLER SETTINGS - ON BY DEFAULT
				cfgprops.setProperty("java.util.logging.FileHandler.level", "INFO");
				cfgprops.setProperty("java.util.logging.FileHandler.pattern", EmsApp.getInstance().getHomeFolder().getSubFolder("logs", false).getAbsolutePath().concat(File.separator).concat(DataManager.LOGFILE_NAME));
				cfgprops.setProperty("java.util.logging.FileHandler.limit", "10000000");
				cfgprops.setProperty("java.util.logging.FileHandler.count", "2");
				cfgprops.setProperty("java.util.logging.FileHandler.formatter", "java.util.logging.SimpleFormatter");

				//CONSOLEHANDLER SETTINGS - OFF BY DEFAULT
				cfgprops.setProperty("java.util.logging.ConsoleHandler.level", "CONFIG");
				cfgprops.setProperty("java.util.logging.ConsoleHandler.formatter", "java.util.logging.SimpleFormatter");

				//SIMPLEFORMATTER SETTINGS
				cfgprops.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %2$s: %5$s%n%6$s%n");

				//SAVE PROPERTIES
				fos = new FileOutputStream(cfgfile);
				cfgprops.store(fos, "CORE APPLICATION LOGGING CONFIGURATION FILE");
				fos.flush();
				fos = null;
				cfgprops = null;
			}
			cfgfile = null;

			//STEP 9: LOAD LOGMANAGER CONFIGURATION
			cfgfile = new File(EmsApp.getInstance().getHomeFolder().getSubFolder("config", false).getAbsolutePath().concat(File.separator).concat(DataManager.LOGCFGFILE_NAME));

			//LOAD LOGMANAGER CONFIGURATION
			fis = new FileInputStream(cfgfile);
			LogManager.getLogManager().readConfiguration(fis);
			fis.close();
			fis = null;
			cfgfile = null;

			//SET LOGGER
			DataManager.i().mLogger = Logger.getLogger(UploadForm.class.getCanonicalName());

//			updateMessage("Loading certificates...");
//			updateProgress(6, steps);
			Thread.sleep(DataManager.i().mWaitTime);

			//STEP 10: LOAD KEYS
			DataManager.i().mKeyStore = new KeyStore(DataManager.i().mProperty);
			try { DataManager.i().mKeyStore.loadKeys("a_id_rsa", "AGENT_KEYS"); }
			catch(CoreException e) { DataManager.i().mKeyStore.createKeys("a_id_rsa", "AGENT_KEYS"); }

//			updateMessage("Agent successfully loaded...");
//			updateProgress(7, steps);
			Thread.sleep(DataManager.i().mWaitTime);

			configured = true;

			return configured;
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new UploadException(e.getMessage());
		}finally
		{
			cfgfile = null;
			fos = null;
			fis = null;
			cfgprops = null;
			ei = null;
		}
	}
}