package com.etc.admin.data; 
 
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.EtcAdmin;
import com.etc.admin.utils.CacheType;
import com.etc.admin.utils.Logging;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.LogType;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.api.WsManager;
import com.etc.corvetto.CorvettoConnection;
import com.etc.corvetto.entities.UploadType;
import com.etc.corvetto.entities.User;
import com.etc.corvetto.rqs.UploadRequest;
import com.etc.entities.CoreData;
import com.etc.entities.DataProperty;
import com.etc.utils.sec.KeyStore; 
//import com.etc.utils.crypto.Cryptographer; 
//import com.etc.utils.crypto.CryptographyException; 
//import com.etc.utils.crypto.EncryptedSSN; 
public class DataManager 
{ 
	///////////////////////////////////////////// 
	// standard data members  
	///////////////////////////////////////////// 
	public CoreData mCoreData = null;
	 
	///////////////////////////////////////////// 
	// Logging and Debugging 
	///////////////////////////////////////////// 
	public List<Logging> mLogs = null; 
	private Logger logr;
	private int resetCounter = 0;
	 
	///////////////////////////////////////////// 
	// uploader members 
	///////////////////////////////////////////// 
	public static final String CFG_UUID_KEY = "com.etclite.core.upload.Uploader.uuidv2";
	public static final String CFG_LDIR_KEY = "com.etclite.core.upload.Uploader.lastDirectory";
	public static final String CFG_LEML_KEY = "com.etclite.core.upload.Uploader.lastEmail";
	public static final String CFG_DEL_FILE = "delete config file and reload";

	public static final String HOMEFOLDER_NAME = ".xarriot";
	public static final String CFGFILE_NAME = "com.etc.utils.xarriot.app.config.filename";
	public static final String LOGCFGFILE_NAME = "logging.properties";
	public static final String LOGFILE_NAME = "log%g.txt";
	public static final String VERSION = "1.2.0";
	public static final String SOFTWARE_ID = "9647adb5-3c50-11ea-ac3a-0a6f9cb53658";
	public static HashMap<Long,ArrayList<UploadType>> mEmployerUploadTypes = new HashMap<Long,ArrayList<UploadType>>(16,1);
	public static HashMap<Long,ArrayList<DataProperty>> mUploadDataProperties = new HashMap<Long,ArrayList<DataProperty>>(16,1);

	public UploadRequest mUploadRequest = null;
	public KeyStore mKeyStore = null;
	public Logger mLogger = null;
	public String mUploadFromDirectory = null;			//USED TO LOAD LAST UPLOAD-FROM FOLDER
	public String mEmail = null;
	public int mWaitTime = 100;							//USED TO PACE PROGRESS MESSAGES
	public boolean mCanUpload = false;
	public User mLocalUser = null;
	public User mUser = null;
	public List<User> mUsers = null;

	public ScreenType mScreenType = null; 
	 
	///////////////////////////////////////////// 
	// keep os version because of differences in gui behaior 
	///////////////////////////////////////////// 
	private boolean windowsOS = false; 
	
	// track which type of core data object we could be updating in the System Data popup
	public SystemDataType mCurrentCoreDataType = SystemDataType.NONE;
	 
	public boolean isWindows() { 
		return windowsOS; 
	} 
	 
	///////////////////////////////////////////// 
	// control members 
	///////////////////////////////////////////// 
	public CorvettoConnection mCorvettoConnection; 
	public WsManager mCorvettoManager; 
 
	public boolean mbDebug = false; // member to control the debug output 
	public String mDebugMessage = "No Message"; 
	 
	// use this to track the HR source 
	public int hrSource = 0; 
	
	///////////////////////////////////////////// 
	// caching utility members
	///////////////////////////////////////////// 
	private HashMap<CacheType<?,?>,Calendar> cacheMap;
	 
	///////////////////////////////////////////// 
	// Make this a singleton that we can access  
	// this class everywhere 
	///////////////////////////////////////////// 
    private static DataManager mInstance; 
    static { mInstance = new DataManager(); } 

    public static DataManager i() 
    { 
        return mInstance; 
    }	 

	private DataManager()
    { 
    	init(); 
    } 
     
	public void setDebug(boolean bDebug) { 
		mbDebug = bDebug;	 
	} 
	 
	public boolean isDebug() { 
		return mbDebug; 
	} 
 
	public void init() 
	{ 
		if(System.getProperty("os.name").toString().toUpperCase().contains("WINDOWS")) 
			windowsOS = true; 
 
		cacheMap = new HashMap<CacheType<?,?>,Calendar>();
		logr = Logger.getLogger(this.getClass().getCanonicalName());
	} 
 
	public boolean ResetLocalDatabase() 
	{ 
		try
		{
			logr.severe("RESETTING LOCAL DATABASE.");
			EtcAdmin.i().showAppStatus("Resetting Local Database", "Disabling local DB", 0.1, true);

			//set create, so when we re-load the persistence context it drops the db.
			EmsApp.getInstance().getProperties().put(EmsApp.JPA_HBM2DDL, "create");
			//close the connection to the db
			EmsApp.getInstance().enableJpa(false, null);
			// delete the existing db files
			String userDir = System.getProperty("user.home");
			String pathSeparator = System.getProperty("file.separator");
			String dbPathString = userDir.concat(pathSeparator).concat(".xarriot")
															   .concat(pathSeparator)
															   .concat("apps")
															   .concat(pathSeparator)
															   .concat(EmsApp.getInstance().getApplicationHomeFolderName())
															   .concat(pathSeparator)
															   .concat("db")
															   .concat(pathSeparator);

			File dbFiles = new File(dbPathString);
			Thread.sleep(1000);
			EtcAdmin.i().showAppStatus("Resetting Local Database", "Cleaning Up Files", 0.3, true);

			try {
				FileUtils.cleanDirectory(dbFiles);
			} catch (Exception e) {
				EtcAdmin.i().showAppStatus("Resetting Local Database", "Could not delete DB File. Continuing reset process.", 0.4, true);
				Thread.sleep(1000);
			}
			//reset the connection to the db using the updated params.
			EmsApp.getInstance().createLocalPersistenceContext();

			//reload the accounts 
			EtcAdmin.i().showAppStatus("Resetting Local Database", "Updating Cache", 0.6, true);
//			updateCache(); 
			EtcAdmin.i().showAppStatus("Resetting Local Database", "Updating Second Tier Cache", 0.6, true);
//			updateSecondTierCache();
			EtcAdmin.i().showAppStatus("", "", 0, false);
			
		}catch(Exception e)
		{
			DataManager.i().log(Level.SEVERE, e);
			EtcAdmin.i().showAppStatus("", "", 0, false);
			return false;
		}
		logr.severe("DATABASE HAS BEEN RESET.");
		return true;
	} 
	 
	public WsManager getCorvettoManager() 
	{ 
		try {  return EmsApp.getInstance().getApiManager(); }
		catch (CoreException e) { DataManager.i().log(Level.SEVERE, e);  } 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		return null; 
	} 

	public void saveCurrentUsername(String username) { 
		Properties props = new Properties();
		try 
		{ 
			props.setProperty(EmsApp.CFG_LEML, username != null ? username : "");
			EmsApp.getInstance().updateAppConfigProperties(props);
		} catch (Exception e) { DataManager.i().log(Level.SEVERE, e);  } 
	} 

	public void reconnectCorvetto() 
	{
		try {

			EtcAdmin.i().updateStatus(.5, "Reconnecting Corvetto Server");
			
			if(!EmsApp.getInstance().enableApi(false))
				if(EmsApp.getInstance().enableApi(true))
					EtcAdmin.i().updateStatus(0, "Reconnect Complete, Ready");
				else
					EtcAdmin.i().updateStatus(0, "Reconnect failed. Please restart.");
			
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	public  HashMap<CacheType<?,?>,Calendar> getCacheMap() { return this.cacheMap; }
	
	/**
	 * <p>
	 * checks if the existing type has been cached with no filter or <br>
	 * with the provided filter within the last 5 minutes. Returns true <br>
	 * if the Type is already cached.</p>
	 * @param <T> The Root cache type
	 * @param <X> The Filter cache type
	 * @param root the Root class
	 * @param filter the Filter class
	 * @return boolean true if cached
	 */
	public <T extends CoreData,X extends CoreData>boolean isPreviouslyCached(Class<T> root, Class<X> filter)
	{
		CacheType<T,X> ctype = new CacheType<T,X>(root,filter);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -5);
		
		try
		{
			return (getCacheMap().get(ctype) != null && getCacheMap().get(ctype).after(cal));
		}finally
		{
			ctype = null;
			cal = null;
		}
	}

	/////////////////////////////////////////////////////////////////////////// 
	// Generic Methods 
	/////////////////////////////////////////////////////////////////////////// 
/*	public <T extends CoreData, X extends CoreRequest<T,X>> List<T> getAll(final CoreRequest<T,X> request) 
	{ 
		List<T> tList = null; 
		try { 
			tList = getCorvettoManager().getAll(request); 
		} catch (CoreException | InterruptedException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
 
		return tList; 
	} 
	
	public <T extends CoreData, X extends CoreRequest<T,X>> T get(final CoreRequest<T,X> request) 
	{ 
		try { 
			return getCorvettoManager().get(request); 
		} catch (CoreException | InterruptedException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
		 
		return null; 
	} 
	 
	public <T extends CoreData, X extends CoreRequest<T,X>> T update(final CoreRequest<T,X> request) 
	{ 
		try { 
			return getCorvettoManager().update(request); 
		} catch (CoreException | InterruptedException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
		 
		return null; 
	} 
	 
	public <T extends CoreData, X extends CoreRequest<T,X>> T add(final CoreRequest<T,X> request) 
	{ 
		try { 
			return getCorvettoManager().add(request); 
		} catch (CoreException | InterruptedException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
		 
		return null; 
	} 
 
	public <T extends CoreData, X extends CoreRequest<T,X>> boolean remove(final CoreRequest<T,X> request) 
	{ 
		try { 
			return getCorvettoManager().remove(request); 
		} catch (CoreException | InterruptedException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
		 
		return false; 
	} 
	*/ 
	/////////////////////////////////////////////////////////////////////////// 
	// GET Functions 
	/////////////////////////////////////////////////////////////////////////// 
	public ScreenType getScreenType() { 
		return mScreenType; 
	} 
	 
	/////////////////////////////////////////////////////////////////////////// 
	// SET Functions 
	///////////////////////////////////////////////////////////////////////////
	public void setUser(int nUserID) 
	{ 
		//set the current user from our employer collection 
		if(mUsers != null) 
			mUser = mUsers.get(nUserID);   
	} 

	public void setLocalUser(User user) { 
		mLocalUser = user; 
	} 

	public void setScreenType(ScreenType screenType) { 
		mScreenType = screenType; 
	} 
	 
	public void insertLogEntry(String description, LogType logType) 
	{ 
		if(logType != null) 
		{
			switch(logType) 
			{
				case INFO:
					Logger.getLogger(this.getClass().getCanonicalName()).log(Level.INFO, description);	
					break;
				case DEBUG:
					Logger.getLogger(this.getClass().getCanonicalName()).log(Level.INFO, description);	
					break;
				case WARNING:
					Logger.getLogger(this.getClass().getCanonicalName()).log(Level.WARNING, description);	
					break;
				case CRITICAL:
					Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE, description);	
					break;
				case ERROR:
					Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE, description);	
					break;
				default:
					Logger.getLogger(this.getClass().getCanonicalName()).log(Level.INFO, description);	
					break;
			}	
		} else
			Logger.getLogger(this.getClass().getCanonicalName()).log(Level.INFO, description);
	}
	 
	public void log(Level level, Exception e) 
	{ 
		Logger.getLogger(this.getClass().getCanonicalName()).log(level, "Exception", e);
		
		// check for a closed db file and reset if present
		if(e == null || e.getMessage() == null) return;
		if(e.getMessage().toLowerCase().contains("the object is already closed") == true ||
		   e.getMessage().toLowerCase().contains("the file is locked")	||
		   e.getMessage().toLowerCase().contains("unable to acquire jdbc connection")) 
		{
			// houston, we have a problem. Reset the local DB connection
			insertLogEntry("System Reset requested due to reported DB issue.",LogType.ERROR);
			ResetLocalDatabase();
			resetCounter++;

			// alert the user if this keeps happening			
			if (resetCounter > 2) 
			{
				insertLogEntry("WARNING: Excessive System Resets requested due to reported closed DB.",LogType.ERROR);
				Utils.alertUser("Excessive DB Reset Required", "The local DB has required repeated resets. Please check you system for errors.");
				// reset the counter
				resetCounter = 0;
			}
		}
	} 
	 
	public void log(Level level, Throwable throwable) {
        Logger.getLogger(this.getClass().getCanonicalName()).log(level, "Throwable Catch", throwable);
	}
	
	public void logGenericException(Exception e) { 
		Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE, "Generic Exception Catch" , e);
	} 
	 
	public void clearDebugLog() { 

	} 
 
//	//Uploader getters and setters
//	public UploadRequest getActiveUpload() { return mUploadRequest; }
//	public void setActiveUpload(UploadRequest rq, File file) { mUploadRequest = rq; setActiveUploadFile(file); }
//	public void setActiveUpload(UploadRequest rq) { mUploadRequest = rq; }
	
	/////////////////////////////////////////////////////////////////////////// 
	// LOAD Functions 
	/////////////////////////////////////////////////////////////////////////// 
 
	public void persistString(String key, String value) 
	{
		Properties props = new Properties();
		try 
		{ 
			props.setProperty(key, value != null ? value : "");
			EmsApp.getInstance().updateAppConfigProperties(props);
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e);  
		} 
	}
	
	public String getPersistedString(String key) 
	{
		String value = "";
		try { 
			value = EmsApp.getInstance().getProperties().getProperty(key); 
		} catch (CoreException e) { 
			logr.log(Level.SEVERE, "Exception.", e); 
		}
		
		return value; 
	}
	
	/////////////////////////////////////////////////////////////////////////// 
	// UPDATE Functions 
	/////////////////////////////////////////////////////////////////////////// 
 
	public void updateLocalUser(User user) 
	{ 
		//save the user 
		//saveUser(user); 
		 
		//update the display 
		EtcAdmin.i().updateLocalUserName(); 
	} 
	 
	public boolean ping()
	{ 
		try { 
 			// use the ping function to test our connectin with the server 
			return mCorvettoConnection.ping(); 
		} catch (CoreException  e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		}  
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		return false; 
	} 
} 
