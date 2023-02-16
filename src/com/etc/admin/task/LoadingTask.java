package com.etc.admin.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.etc.admin.AdminApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.utils.crypto.Cryptographer;
import com.etc.utils.crypto.CryptographyException;

import javafx.concurrent.Task;

public class LoadingTask extends Task<Boolean> {
	private static int steps = 8;

	@Override
	protected Boolean call() throws Exception {
		boolean successful = false;
		updateMessage("Configuring agent for use...");
		updateProgress(0, steps);

		try {
			// AUTHENTICATE USER
			if (successful = loadAgent()) {
				updateProgress(steps, steps);
				updateMessage("Transmitter initialized.");
			}
		} catch (Exception e) {
			successful = false;
			updateProgress(steps, steps);
			updateMessage(e.getMessage());
        	DataManager.i().log(Level.SEVERE, e); 
			throw new Exception("Unable to load the client", e);
		}
		return successful;
	}

	/**
	 * The loadAgent configures the smplDocs client prior to logging in.
	 * 
	 * @return true if the sfta's properties object has been loaded with
	 *         data.
	 * @throws TransferException
	 */
	// TODO Make an Exception class.
	private boolean loadAgent() throws Exception {
		boolean configured = false;
		
		updateMessage("Configuring Client...");
		updateProgress(3, steps);
		
		AdminManager.setCoreKey(AdminApp.getInstance().getCoreKey());
		AdminManager.setKeyPair(AdminApp.getInstance().getKeyStore().getKeysByIdentifier("LOCALKEY"));
		
		updateMessage("Configuration Done...");
		updateProgress(8, steps);
		
		return true;

//		try {
//			updateMessage("Configuring Transmitter...");
//			updateProgress(1, steps);
//
//			String userDir = System.getProperty("user.home");
//			String pathSeparator = System.getProperty("file.separator");
//
//			if ((userDir != null && pathSeparator != null) ? (userDir.length() > 0 && pathSeparator.length() > 0)
//					: false) {
//				updateMessage("Configuring Local Filesystem...");
//				updateProgress(2, steps);
//
//				// VALIDATE/CREATE HOME DIRECTORY
//				
//				//KEYS DIR
//				AdminManager.setCoreKeyDirectory(userDir + pathSeparator + 
//											".xarriot" + pathSeparator + 
//											"apps"+ pathSeparator + 
//											"corvetto" + pathSeparator + 
//											"keys" + pathSeparator);
//				//HOME DIR
//				AdminManager.setHomeDirectory(userDir + pathSeparator + ".smpldocs" + pathSeparator);
//				File homeDir = new File(AdminManager.getHomeDirectory());
//				if ((homeDir.exists() && homeDir.isDirectory()) ? true : homeDir.mkdir()) {
//					// LOAD/CREATE CONFIG PROPERTIES
//					//CONFIG FILE/DIR
//					File configFile = new File(AdminManager.getHomeDirectory() + ".smpldocs.properties");
//					AdminManager.setConfigDirectory(AdminManager.getHomeDirectory() + ".smpldocs.properties");
//					Properties properties = new Properties();
//
//					if (EtcAdmin.i().resetUUID == false && configFile.isFile()) {
//						updateMessage("Loading Configuration...");
//						updateProgress(3, steps);
//
//						// LOAD PROPERTIES
//						FileInputStream fis = new FileInputStream(configFile);
//						properties.load(fis);
//						fis.close();
//					} else {
//						System.out.println("Resetting UUID");
//						updateMessage("Creating Configuration File...");
//						updateProgress(3, steps);
//
//						// CREATE PROPERTIES FILE, GENERATE ENDPOINT UUID
//						properties.setProperty(AdminManager.CFG_UUID_KEY, UUID.randomUUID().toString());
//						properties.setProperty(AdminManager.CFG_LEML_KEY, "");
//						properties.setProperty(AdminManager.CFG_LDIR_KEY, userDir);
//
//						// SAVE PROPERTIES
//						FileOutputStream fos = new FileOutputStream(configFile);
//						properties.store(fos, "CONFIG FILE FOR SMPLDOCS CLIENT");
//						fos.flush();
//						fos.close();
//					}
//
//					// FIX MISSING LAST EMAIL PROPERTY
//					if (!properties.containsKey(AdminManager.CFG_LEML_KEY))
//						properties.setProperty(AdminManager.CFG_LEML_KEY, "");
//
//					// FIX MISSING LAST DIRECTORY PROPERTY
//					if (!properties.containsKey(AdminManager.CFG_LDIR_KEY))
//						properties.setProperty(AdminManager.CFG_LDIR_KEY, userDir);
//
//					FileOutputStream fos = new FileOutputStream(configFile);
//					properties.store(fos, "CONFIG FILE FOR SMPLDOCS CLIENT");
//					fos.flush();
//					fos.close();
//					configFile = null;
//
//					// VERIFY UUID, LAST EMAIL, LAST DIRECTORY
//					if (properties.containsKey(AdminManager.CFG_UUID_KEY)
//							&& properties.containsKey(AdminManager.CFG_LEML_KEY)
//							&& properties.containsKey(AdminManager.CFG_LDIR_KEY)) {
//						// LOAD PREVIOUS VALID EMAIL
//						if (properties.getProperty(AdminManager.CFG_LEML_KEY).length() > 0)
//							AdminManager.setEmail(properties.getProperty(AdminManager.CFG_LEML_KEY));
//
//						// LOAD UPLOAD FROM DIRECTORY
//						AdminManager.setFileSourceDirectory(properties.getProperty(AdminManager.CFG_LDIR_KEY));
//
//						// LOAD ENDPOINT UUID
//						AdminManager.setEpUUID(properties.getProperty(AdminManager.CFG_UUID_KEY));
//
//						updateMessage("Loading Log File...");
//						updateProgress(5, steps);
//
//						// VALIDATE/CREATE LOG FILE
//						File logFile = new File(AdminManager.getHomeDirectory() + "smpldocs.log");
//						if (logFile.exists() ? true : (logFile.createNewFile() ? logFile.canWrite() : false)) {
//							// LOAD CLIENT CERTIFICATES, SERVER CERTIFICATE
//							// IS
//							// DOWNLOADED EACH TIME
//							updateMessage("Loading Client Certificates...");
//							updateProgress(6, steps);
//							
//							// GET THE CORE KEY PAIR
//							try {
//								// we are loading this from inside the project. So we convert it to a file from a stream using Apache utils
//								InputStream aPubCoreKeyFileStream = getClass().getClassLoader().getResourceAsStream("com_rsa.pub");
//								if (aPubCoreKeyFileStream == null)
//									aPubCoreKeyFileStream = getClass().getClassLoader().getResourceAsStream("img/com_rsa.pub");
//								File aPubCoreKeyFile = new File("Temp1");
//								FileUtils.copyInputStreamToFile(aPubCoreKeyFileStream, aPubCoreKeyFile);
//						         
//								InputStream aPrvCoreKeyFileStream = getClass().getClassLoader().getResourceAsStream("com_rsa");
//								if ( aPrvCoreKeyFileStream == null)
//									aPrvCoreKeyFileStream = getClass().getClassLoader().getResourceAsStream("img/com_rsa");
//								File aPrvCoreKeyFile = new File("Temp2");
//								FileUtils.copyInputStreamToFile(aPrvCoreKeyFileStream, aPrvCoreKeyFile);
//								if (aPubCoreKeyFile.canRead() && aPrvCoreKeyFile.canRead()){
//									KeyPair ckp;
//										ckp = Cryptographer.loadKeyPair(aPrvCoreKeyFile, aPubCoreKeyFile);
//									if (ckp != null) {
//										AdminManager.setCoreKeyPair(ckp);
//									}
//								}
//								// clean up the temp files.
//								aPrvCoreKeyFile.delete();
//								aPubCoreKeyFile.delete();
//							} catch (CryptographyException e) {
//						    	DataManager.i().log(Level.SEVERE, e); 
//							}
//
//							// VALIDATE/CREATE AGENT KEYPAIR
//							File aPubKeyFile = new File(AdminManager.getHomeDirectory() + ".a_id_rsa.pub");
//							File aPrvKeyFile = new File(AdminManager.getHomeDirectory() + ".a_id_rsa");
//							if ((aPubKeyFile.canRead() && aPrvKeyFile.canRead()) ? true
//									: Cryptographer.generateKeyPair(aPrvKeyFile, aPubKeyFile)) {
//								KeyPair kp = Cryptographer.loadKeyPair(aPrvKeyFile, aPubKeyFile);
//								if (kp != null) {
//									// STORE KEYPAIR
//									AdminManager.setKeyPair(kp);
//
//									updateMessage("Verifying Directories...");
//									updateProgress(7, steps);
//
//									// VALIDATE/CREATE DOCUMENT DIRECTORY
//									AdminManager.setDocumentDirectory(
//											AdminManager.getHomeDirectory() + "docs" + pathSeparator);
//									AdminManager.setTempDirectory(
//											AdminManager.getHomeDirectory() + "temp" + pathSeparator);
//									File docDir = new File(AdminManager.getDocumentDirectory());
//									File tmpDir = new File(AdminManager.getTempDirectory());
//									if ((docDir.isDirectory() && tmpDir.isDirectory()) ? true
//											: (docDir.mkdir() && tmpDir.mkdir())) {
//										updateMessage("Client Configured...");
//										updateProgress(8, steps);
//
//										configured = true;
//									} else
//										throw new Exception("Invalid working directory.");
//									docDir = null;
//									tmpDir = null;
//								} else
//									throw new Exception("Configuration error: invalid KeyPair.");
//								kp = null;
//							} else
//								throw new Exception("Configuration error: invalid client keys.");
//							aPubKeyFile = null;
//							aPrvKeyFile = null;
//						} else
//							throw new Exception("Configuration error: log file not found.");
//						logFile = null;
//					} else
//						throw new Exception("Configuration error: invalid properties.");
//					properties.clear();
//					properties = null;
//				} else
//					throw new Exception("Invalid root directory.");
//				homeDir = null;
//			} else
//				throw new Exception("There was a file system error.");
//		} catch (Exception e) {
//			throw new Exception("There was an Exception while configuring the SFT Agent.", e);
//		}
		
	}
}
