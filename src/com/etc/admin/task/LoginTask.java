package com.etc.admin.task;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.etc.CoreException;
import com.etc.admin.AdminManager;
import com.etc.admin.EmsApp;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.corvetto.entities.User;

import javafx.concurrent.Task;

public class LoginTask extends Task<Boolean> 
{
	@Override
	protected Boolean call() throws Exception 
	{
		boolean successful = false;

		// AUTHENTICATE USER
		try {
			//EtcAdmin.i().logger.info("Authenticating user");
			authenticateAgent();
		} catch (Exception e) {
			successful = false;
			//print the exception cause message
			Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE, "Exception. " + e);
			throw e;
		}

		//EtcAdmin.i().logger.info("User Authenticated okay");
		return successful;
	}

	/**
	 * authenticateAgent primarily pulls all of the data an agent needs to
	 * facilitate an upload, but through that process also authenticates the
	 * user credentials.
	 * 
	 * @return true if all the operations are successful and if there are at
	 *         least 1 UploadType, Account, and Employer as a result.
	 * @throws TransferException
	 */
	private boolean authenticateAgent() throws Exception 
	{
		boolean authenticated = false;

		try {
			EtcAdmin.mLoginController.setProgress(0.1);
			
			Logger.getLogger(this.getClass().getCanonicalName()).info("Creating Corvetto API Connection.");
			//LOGIN/CREATE CORVETTOCONNECTION
			if(!EmsApp.getInstance().enableCorvettoApi(AdminManager.getEmail(), AdminManager.getPassword()))
				throw new CoreException("Failed Corvetto Login.");
			
			// SET ANY PROPERTIES
			//properties.setProperty(CoreConnection.ENABLE_DEBUG, String.valueOf(EtcAdmin.mbDebug));
			// CREATE THE CONNECTION
			EtcAdmin.mLoginController.setProgress(0.2);
			//CREATE CONNECTION TO LOCAL DB
			Logger.getLogger(this.getClass().getName()).info("Creating local Persistence Context.");
			EmsApp.getInstance().createLocalPersistenceContext();
//			CorvettoConnection corvettoConnection = new CorvettoConnection(ident,properties);
//			DataManager.i().mCorvettoManager = corvettoConnection.getManager();
			EtcAdmin.mLoginController.setProgress(0.3);

			if(DataManager.i().getCorvettoManager() != null) 
			{
				EtcAdmin.mLoginController.setProgress(0.4);
				// SET APP LOCAL USER
				DataManager.i().setLocalUser(((User)DataManager.i().getCorvettoManager().getIdentity().getUser()));
				// update the app's cache
				EtcAdmin.mLoginController.setProgress(0.5);
				
//				Task<Void> updateCacheTask = new Task<Void>() {
//					@Override
//					protected Void call() throws Exception {
//						
//						DataManager.i().updateSecondTierCache();
//						return null;
//					}
//				};
				
				EtcAdmin.mLoginController.setProgress(0.7);
//				DataManager.i().updateCache();
				EtcAdmin.mLoginController.setProgress(0.8);
//				EmsApp.getInstance().getFxQueue().put(updateCacheTask);
				EtcAdmin.mLoginController.setProgress(0.9);
				
			} else {
				throw new Exception("There was a problem with the CORVETTO CONNECTION.");
			}
	
		} catch (NullPointerException e) {
        	DataManager.i().log(Level.SEVERE, e); 
			throw new Exception("There was an Exception while authenticating the Agent.", e);
		}
		return authenticated;
	}
}
