package com.etc.admin.ui.uploader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.etc.CoreException;
import com.etc.admin.AdminApp;
import com.etc.admin.AdminManager;
import com.etc.admin.data.DataManager;
import com.etc.api.ActiveTransfer;
import com.etc.corvetto.CorvettoConnection;
import com.etc.corvetto.CorvettoIdentity;
import com.etc.corvetto.UploadException;
import com.etc.corvetto.rqs.UploadRequest;
import com.etc.utils.EndpointFactory;
import com.etc.utils.crypto.Cryptographer;

import javafx.concurrent.Task;

public class UploadTask extends Task<Boolean>
{
	private static int steps = 7;
	private static int pause = DataManager.i().mWaitTime;

	@Override
	protected Boolean call() throws Exception
	{
		boolean successful = false;

		try
		{
			if(successful = processUpload())
			{
				updateProgress(steps, steps);
				updateMessage("File successfully transferred...");
				updateMessage("Click 'Upload' to begin a transfer...");
			}
		}catch(UploadException e)
		{
			updateProgress(steps, steps);
			updateMessage(e.getMessage());
		}
		return successful;
	}

	/**
	 * processUpload processes an Upload based on the information contained in the DataManager.activeUpload.
	 * 
	 * Note: All File locations should be absolute paths.
	 * 
	 * @return true if the file was uploaded, false otherwise.
	 * @throws UploadException
	 */
	private boolean processUpload() throws UploadException
	{
		boolean uploaded = false;

		try
		{
			updateMessage("Preparing the file for transfer...");
			updateProgress(1, steps);
			Thread.sleep(pause);

//			String username = UploadDataManager.getEmail();
//			String password = UploadDataManager.getPassword();
			UploadRequest rq = DataManager.i().getActiveUpload();
			File file = null;
			Logger logr = Logger.getLogger(UploadForm.class.getCanonicalName());

			//VALIDATE UPLOAD PARAMETERS
			if((rq != null) ? ((file = new File(DataManager.i().getActiveUploadFile().getAbsolutePath())) != null && rq.getEntity().getEmployer() != null  && rq.getEntity().getUploadType() != null) : false) 
			{
				if(file.canRead())
				{
					logr.log(Level.INFO, "PRE-UPLOAD :: FILE :: " + file.getName() + " CHKSUM :: " + Cryptographer.calculateMD5Checksum(file));
	    			//SAVE LAST UPLOAD DIRECTORY TO PROPERTIES
					File configFile = new File(AdminApp.getInstance().getHomeFolder().getSubFolder("config", false).getAbsolutePath().concat(File.separator).concat(DataManager.CFGFILE_NAME));
					Properties properties = new Properties();
					if(configFile.isFile())
					{
						//LOAD PROPERTIES
						properties.load(new FileInputStream(configFile));
						properties.setProperty(DataManager.CFG_LDIR_KEY, DataManager.i().mUploadFromDirectory);
						properties.store(new FileOutputStream(configFile), "UPLOADER CONFIGURATION FILE");
					}
					configFile = null;
					
					//SET UPLOAD DATAPROPERTIES IF THEY EXIST.
					if(DataManager.mUploadDataProperties.get(rq.getEntity().getUploadType().getId()) != null ? !DataManager.mUploadDataProperties.get(rq.getEntity().getUploadType().getId()).isEmpty() : false)
					{
						rq.getEntity().setProperties(DataManager.mUploadDataProperties.get(rq.getEntity().getUploadType().getId()));
					}
					
					//UPLOAD FILE
					ActiveTransfer au = null;
					if((au = DataManager.i().getCorvettoManager().initUpload(rq)) != null)
					{
						updateMessage("Creating Upload...");
						updateProgress(3, steps);
						Thread.sleep(pause);

						while(!au.isCompleted() && au.getProgress() < 1.0f)
						{
							updateMessage("Uploading File...");
							updateProgress(5, steps);
							Thread.sleep(pause);
							DataManager.i().getCorvettoManager().uploadGlob(au);
						}

						//COMMIT UPLOAD
						DataManager.i().getCorvettoManager().endUpload(au);
						uploaded = au.isCompleted();
					}

					if(au.isCompleted())
					{
						updateMessage("Upload Complete...");
						updateProgress(7, steps);
						Thread.sleep(pause);
					}
				}
//				username = null;
//				password = null;
				rq = null;
				file = null;
			}else
			{
				throw new UploadException("Invalid data state.");
			}
		}catch(CoreException | InterruptedException | IOException e)
		{
			e.printStackTrace();
			throw new UploadException("There was an Exception processing an Upload.", e);
		}

		return uploaded;
	}
}