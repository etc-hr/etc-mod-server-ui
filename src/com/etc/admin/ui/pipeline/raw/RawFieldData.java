package com.etc.admin.ui.pipeline.raw;

import java.util.List;
import java.util.logging.Level;


import com.etc.CoreException;
import com.etc.admin.AdminApp;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.pipeline.queue.PipelineQueue;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.LogType;
import com.etc.corvetto.data.FileRawFailure;
import com.etc.corvetto.ems.pipeline.embeds.PipelineInformation;
import com.etc.corvetto.ems.pipeline.entities.PipelineRequest;
import com.etc.corvetto.ems.pipeline.entities.RawBenefit;
import com.etc.corvetto.ems.pipeline.entities.RawConversionFailure;
import com.etc.corvetto.ems.pipeline.entities.RawCoverage;
import com.etc.corvetto.ems.pipeline.entities.RawDependent;
import com.etc.corvetto.ems.pipeline.entities.RawDependentCoverage;
import com.etc.corvetto.ems.pipeline.entities.RawEmployee;
import com.etc.corvetto.ems.pipeline.entities.RawInvalidation;
import com.etc.corvetto.ems.pipeline.entities.RawIrs1094c;
import com.etc.corvetto.ems.pipeline.entities.RawNotice;
import com.etc.corvetto.ems.pipeline.entities.RawPay;
import com.etc.corvetto.ems.pipeline.entities.RawPayPeriod;
import com.etc.corvetto.ems.pipeline.entities.cov.CoverageFile;
import com.etc.corvetto.ems.pipeline.entities.emp.EmployeeFile;
import com.etc.corvetto.ems.pipeline.entities.pay.PayFile;
import com.etc.corvetto.ems.pipeline.rqs.PipelineRequestRequest;
import com.etc.corvetto.ems.pipeline.rqs.cov.CoverageFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.EmployeeFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.PayFileRequest;
import com.etc.corvetto.rqs.FileRawFailureRequest;
import com.etc.embeds.SSN;
import com.etc.utils.types.RequestStatusType;

import javafx.concurrent.Task;

public class RawFieldData {
	
	public List<RawEmployee> mRawEmployees = null;
	public List<RawCoverage> mRawCoverages = null;
	public List<RawPay> mRawPays = null;
	public List<RawNotice> mRawNotices = null;
	public List<RawInvalidation> mRawInvalidations = null;
	public List<RawConversionFailure> mRawConversionFailures = null;
	public List<RawBenefit> mRawBenefits = null;
	public List<RawDependent> mRawDependents = null;
	public List<RawDependentCoverage> mRawDependentCoverages = null;
	public List<RawPayPeriod> mRawPayPeriods = null;
	public List<RawIrs1094c> mRawIrs1094cs = null;

	// sub files support
	public List<EmployeeFile> mSubEmployeeFiles;
	public List<CoverageFile> mSubCoverageFiles;
	public List<PayFile> mSubPayFiles;
	
	private ViewPipelineRawFieldController mRawFieldMapController = null;
	private ViewPipelineRawFieldGridController mRawFieldController = null;
	private PipelineQueue queue;
	private Long fileId;
	public int currentSubFile = 0;
	public boolean subFilesPresent = false;
	
	public void setRawFieldController(ViewPipelineRawFieldGridController rawFieldController) {
		mRawFieldController = rawFieldController;
	}
	
	public void updateData(PipelineQueue queue) {

		this.queue = queue;
		EtcAdmin.i().updateStatus(0,"Updating Raw Data");
		Task<Void> task = new Task<Void>() {
	        @Override
	        protected Void call() throws Exception {
	    		getAllData(true);
	            return null;
	        }
	    };
	    
		task.setOnSucceeded(e ->   notifyController());
		task.setOnFailed(e ->   notifyController());
	    new Thread(task).start();	

		EtcAdmin.i().updateStatus(0,"Ready");
	}
	
	private void notifyController()
	{
		mRawFieldController.dataReady();
		
		if(mRawFieldMapController != null)
		{
			mRawFieldMapController.dataReady();
		}
	}
	

	public void getAllData(boolean getAll)
	{
		FileRawFailureRequest rfRequest = new FileRawFailureRequest();

		switch(queue.getFileType()) {
			case EMPLOYEE:
				if (queue.getEmployeeFile() == null)  return;

				// if we have sub files, use the sub's fileId
		  		fileId = queue.getEmployeeFile().getId();
		  		if (mRawFieldController.updating == false)
		  			getSubEmployeeFiles(fileId);
		  		if (mSubEmployeeFiles.size() > 0)
		  			fileId = mSubEmployeeFiles.get(currentSubFile).getId();
				break;
			case PAY:
				if (queue.getPayFile() == null) return;

		  		// if we have sub files, use the sub's fileId
		  		fileId = queue.getPayFile().getId();
		  		if (mRawFieldController.updating == false)
		  			getSubPayFiles(fileId);
		  		if (mSubPayFiles .size() > 0)
		  			fileId = mSubPayFiles.get(currentSubFile).getId();
				break;
			case COVERAGE:
				if (queue.getCoverageFile() == null) return;
		
		  		// if we have sub files, use the sub's fileId
		  		fileId = queue.getCoverageFile().getId();
		  		if (mRawFieldController.updating == false)
		  			getSubCoverageFiles(fileId);
		  		if (mSubCoverageFiles.size() > 0)
		  			fileId = mSubCoverageFiles.get(currentSubFile).getId();
				break;
			case IRS1094C:
				if (queue.getIrs1094cFile() == null) return;
		  		fileId = queue.getIrs1094cFile().getId();
				break;
			default:
				break;
		}	
		
		try {
			rfRequest.setId(fileId);
			rfRequest.setType(queue.getFileType());
			rfRequest.setFetchInactive(true);
			FileRawFailure rawFailures;
			rawFailures = AdminPersistenceManager.getInstance().get(rfRequest);
			mRawEmployees = rawFailures.getRawEmployees();
			mRawPays = rawFailures.getRawPays();
			mRawCoverages = rawFailures.getRawCoverages();
			mRawDependents = rawFailures.getRawDependents();
			mRawIrs1094cs = rawFailures.getRawIrs1094cs();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	}

	public void getSubEmployeeFiles(Long employeeFileId) {
		try {
			EmployeeFileRequest efRequest = new EmployeeFileRequest();
			efRequest.setEmployeeFileId(employeeFileId);
			mSubEmployeeFiles = AdminPersistenceManager.getInstance().getAll(efRequest);
			if (mSubEmployeeFiles != null && mSubEmployeeFiles.size() > 0) 
				subFilesPresent = true;
			else
				subFilesPresent = false;
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	}
	
	public void getSubCoverageFiles(Long coverageFileId) {
		try {
			CoverageFileRequest cfRequest = new CoverageFileRequest();
			cfRequest.setCoverageFileId(coverageFileId);
			mSubCoverageFiles = AdminPersistenceManager.getInstance().getAll(cfRequest);
			if (mSubCoverageFiles != null && mSubCoverageFiles.size() > 0) 
				subFilesPresent = true;
			else
				subFilesPresent = false;
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	}
	
	public void getSubPayFiles(Long payFileId) {
		try {
			PayFileRequest pfRequest = new PayFileRequest();
			pfRequest.setPayFileId(payFileId);
			mSubPayFiles = AdminPersistenceManager.getInstance().getAll(pfRequest);
			if (mSubPayFiles != null && mSubPayFiles.size() > 0) 
				subFilesPresent = true;
			else
				subFilesPresent = false;
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	}
	
	public void approveConversion() {
		// update the file spec accordingly
		try {
			switch(queue.getFileType()) {
			case EMPLOYEE:
				queue.getEmployeeFile().getPipelineInformation().setPostValidationApproved(false);
				EmployeeFileRequest efRequest = new EmployeeFileRequest();
				efRequest.setEntity(queue.getEmployeeFile());
				efRequest.setId(queue.getEmployeeFile().getId());
					AdminPersistenceManager.getInstance().addOrUpdate(efRequest);
				break;
			case PAY:
				queue.getPayFile().getPipelineInformation().setPostValidationApproved(false);
				PayFileRequest pfRequest = new PayFileRequest();
				pfRequest.setEntity(queue.getPayFile());
				pfRequest.setId(queue.getPayFile().getId());
				AdminPersistenceManager.getInstance().addOrUpdate(pfRequest);
				break;
			case COVERAGE:
				queue.getCoverageFile().getPipelineInformation().setPostValidationApproved(false);
				CoverageFileRequest cfRequest = new CoverageFileRequest();
				cfRequest.setEntity(queue.getCoverageFile());
				cfRequest.setId(queue.getCoverageFile().getId());
				AdminPersistenceManager.getInstance().addOrUpdate(cfRequest);
				break;
			default:
				return;
			}
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	

	// resubmit is used after existing raw data is edited and saved 
	public boolean resubmit() {
		try {
			// create the request to edit the existing PipelineRequest record
			PipelineRequestRequest rq = new PipelineRequestRequest();
			rq.setEntity(DataManager.i().mPipelineQueueEntry.getRequest());
			rq.getEntity().setStatus(RequestStatusType.UNPROCESSED);
			if (DataManager.i().mLocalUser != null) {
				rq.getEntity().setCreatedBy(DataManager.i().mLocalUser);
				rq.getEntity().setCreatedById(DataManager.i().mLocalUser.getId());
				// log it
				DataManager.i().insertLogEntry("RESUBMIT REQUEST by user " + DataManager.i().mLocalUser.getFirstName() + " " + DataManager.i().mLocalUser.getLastName(), LogType.INFO);
			}
			rq.setId(rq.getEntity().getId());
			
			PipelineRequest pr = AdminPersistenceManager.getInstance().addOrUpdate(rq);
			// Share the status with the user
			if (pr != null) {
				Utils.alertUser("Reprocess Request", "Reprocess for Edited Data Successfully Submitted");
				return true;
			}
			else
				Utils.alertUser("ERROR", "Reprocess Request Failed!");

		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
			Utils.alertUser("ERROR", "Reprocess Request Failed!");
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
		return false;
	}	
	
	public void reprocess() {
		// create the request
		PipelineRequest pr = new PipelineRequest();
		PipelineRequestRequest rq = new PipelineRequestRequest();
		pr.setDescription(queue.getRequest().getDescription());
		if (queue.getRequest().getSpecification() != null)
			pr.setSpecificationId(queue.getRequest().getSpecification().getId());
		pr.setBatchId(queue.getRequest().getBatchId());
		if (queue.getRequest().getInstance() != null)
			pr.setInstanceId(queue.getRequest().getInstance().getId());
		if (DataManager.i().mLocalUser != null) {
			pr.setCreatedById(DataManager.i().mLocalUser.getId());
			pr.setCreatedBy(DataManager.i().mLocalUser);
			// log it
			DataManager.i().insertLogEntry("REPROCESS REQUEST by user " + DataManager.i().mLocalUser.getFirstName() + " " + DataManager.i().mLocalUser.getLastName(), LogType.INFO);
		}
		if (queue.getRequest().getEmployer() != null)
			pr.setEmployerId(queue.getRequest().getEmployer().getId());
		pr.setStatus(RequestStatusType.UNPROCESSED);

		pr.setPipelineFileId(createCompletedReprocessFileRecord());

		if (pr.getPipelineFileId() == null) {
			Utils.alertUser("ERROR", "Reprocess Request Failed!");
			return;
		}
		// send it to the server
		rq.setEntity(pr);
		try {
			pr = AdminPersistenceManager.getInstance().addOrUpdate(rq);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		// tell the user
		if (pr != null)
			Utils.alertUser("Reprocess Request", "Reprocess successfully requested");
		else
			Utils.alertUser("ERROR", "Reprocess Request Failed!");
	}	
	
	private Long createCompletedReprocessFileRecord()
	{
		Long fileId = null;
		try {
			switch(queue.getFileType()) {
			case EMPLOYEE:
				EmployeeFileRequest efFreshRequest = new EmployeeFileRequest();
				efFreshRequest.setId(queue.getEmployeeFile().getId());
				EmployeeFile freshEmployeeFile = AdminApp.getInstance().getApiManager().get(efFreshRequest);// AdminPersistenceManager.getInstance().get .get .get(cfFreshRequest);
				if (freshEmployeeFile == null) return 0l;

				EmployeeFile ef = new EmployeeFile();
				ef.setPipelineInformation(new PipelineInformation());
				ef.getPipelineInformation().setAccountId(freshEmployeeFile.getPipelineInformation().getAccountId());
				ef.getPipelineInformation().setEmployerId(freshEmployeeFile.getPipelineInformation().getEmployerId());
				ef.getPipelineInformation().setDocumentId(freshEmployeeFile.getPipelineInformation().getDocumentId());
				ef.setSpecificationId(freshEmployeeFile.getSpecificationId());
				EmployeeFileRequest efRequest = new EmployeeFileRequest();
				efRequest.setEntity(ef);
				ef = AdminPersistenceManager.getInstance().addOrUpdate(efRequest);
				if (ef != null)
					fileId = ef.getId();
				break;
			case PAY:
				PayFileRequest pfFreshRequest = new PayFileRequest();
				pfFreshRequest.setId(queue.getPayFile().getId());
				PayFile freshPayFile = AdminApp.getInstance().getApiManager().get(pfFreshRequest);// AdminPersistenceManager.getInstance().get .get .get(cfFreshRequest);
				if (freshPayFile == null) return 0l;

				PayFile pf = new PayFile();
				pf.setPipelineInformation(new PipelineInformation());
				pf.getPipelineInformation().setAccountId(freshPayFile.getPipelineInformation().getAccountId());
				pf.getPipelineInformation().setEmployerId(freshPayFile.getPipelineInformation().getEmployerId());
				pf.getPipelineInformation().setDocumentId(freshPayFile.getPipelineInformation().getDocumentId());
				pf.setSpecificationId(freshPayFile.getSpecificationId());
				PayFileRequest pfRequest = new PayFileRequest();
				pfRequest.setEntity(pf);
				pf = AdminPersistenceManager.getInstance().addOrUpdate(pfRequest);
				if (pf != null)
					fileId = pf.getId();
				break;
			case COVERAGE:
				//reload the file
				CoverageFileRequest cfFreshRequest = new CoverageFileRequest();
				cfFreshRequest.setId(queue.getCoverageFile().getId());
				CoverageFile freshCovFile = AdminApp.getInstance().getApiManager().get(cfFreshRequest);// AdminPersistenceManager.getInstance().get .get .get(cfFreshRequest);
				if (freshCovFile == null) return 0l;
				
				CoverageFile cf = new CoverageFile();
				cf.setPipelineInformation(new PipelineInformation());
				cf.getPipelineInformation().setAccountId(freshCovFile.getPipelineInformation().getAccountId());
				cf.getPipelineInformation().setEmployerId(freshCovFile.getPipelineInformation().getEmployerId());
				cf.getPipelineInformation().setDocumentId(freshCovFile.getPipelineInformation().getDocumentId());
				cf.setSpecificationId(freshCovFile.getSpecificationId());
				CoverageFileRequest cfRequest = new CoverageFileRequest();
				cfRequest.setEntity(cf);
				cf = AdminPersistenceManager.getInstance().addOrUpdate(cfRequest);
				if (cf != null)
					fileId = cf.getId();
				break;
			default:
				return null;
			}
		} catch (CoreException | InterruptedException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		return fileId;
	}
	
	public void updateRawEmployee(Long recordId, int fieldId, String value) {
		// look up the record by the id
		RawEmployee rawEmployee = null;
		for (RawEmployee rawEmp : mRawEmployees) {
			if (rawEmp.getId() == recordId) {
				rawEmployee = rawEmp;
				break;
			}
		}
		
		if (rawEmployee == null) return;
		
		// set the field by fieldId
		switch (fieldId) {
			case 3:
				SSN ssn = new SSN();
				ssn.dssn = value;
				rawEmployee.setSsn(ssn);
				break;
			case 4:
				rawEmployee.setEmployerIdentifier(value);
				break;
			case 5:
				rawEmployee.setFirstName(value);
				break;
			case 6:
				rawEmployee.setMiddleName(value);
				break;
			case 7:
				rawEmployee.setLastName(value);
				break;
			case 10:
				rawEmployee.setStreetNumber(value);
				break;
			case 11:
				rawEmployee.setStreet(value);
				break;
			case 12:
				rawEmployee.setStreetExtension(value);
				break;
			case 13:
				rawEmployee.setCity(value);
				break;
			case 14:
				rawEmployee.setState(value);
				break;
			case 15:
				rawEmployee.setZip(value);
				break;
			case 16:
				//rawEmployee.setZip(value);
				break;
			case 17:
				if (value == null)
					rawEmployee.setDateOfBirth(null);
				else
					rawEmployee.setDateOfBirth(Utils.getCalDate(value));
				break;
			case 18:
				if (value == null)
					rawEmployee.setHireDate(null);
				else
					rawEmployee.setHireDate(Utils.getCalDate(value));
				break;
			case 19:
				if (value == null)
					rawEmployee.setRehireDate(null);
				else
					rawEmployee.setRehireDate(Utils.getCalDate(value));
				break;
			case 20:
				if (value == null)
					rawEmployee.setTermDate(null);
				else
					rawEmployee.setTermDate(Utils.getCalDate(value));
				break;
			case 21:
				//rawEmployee.setZip(value);
				break;
			case 23:
				rawEmployee.setJobTitle(value);
				break;
			case 27:
				rawEmployee.setCustomField1(value);
				break;
			case 28:
				rawEmployee.setCustomField2(value);
				break;
			case 29:
				rawEmployee.setCustomField3(value);
				break;
			case 30:
				rawEmployee.setCustomField4(value);
				break;
			case 31:
				rawEmployee.setCustomField5(value);
				break;
			default:
				break;
		}
	}
	
	public void updateRawCoverage(Long recordId, int fieldId, String value) {
		// look up the record by the id
		
		// set the field by fieldId
		switch (fieldId) {
		
		}
	}
	
	public void updateRawPay(Long recordId, int fieldId, String value) {
		// look up the record by the id
		
		// set the field by fieldId
		switch (fieldId) {
		case 1:
			
			break;
		}
	}
	
}
