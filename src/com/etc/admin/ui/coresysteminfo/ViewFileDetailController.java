package com.etc.admin.ui.coresysteminfo;

import java.util.List;
import java.util.logging.Level;

import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.ben.BenefitFile;
import com.etc.corvetto.ems.pipeline.entities.c94.Irs1094cFile;
import com.etc.corvetto.ems.pipeline.entities.cov.CoverageFile;
import com.etc.corvetto.ems.pipeline.entities.emp.EmployeeFile;
import com.etc.corvetto.ems.pipeline.entities.pay.PayFile;
import com.etc.corvetto.ems.pipeline.rqs.ben.BenefitFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.c94.Irs1094cFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.cov.CoverageFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.EmployeeFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.PayFileRequest;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewFileDetailController 
{
	@FXML
	private TextField BatchIdField;
	@FXML
	private TextField BornOnField;
	@FXML
	private TextField CoreIdField;
	@FXML
	private TextField DocIdField;
	@FXML
	private TextField FileNameField;
	@FXML
	private TextField UserField;	
	@FXML
	private Label TopLabel;
	@FXML
	private Button DownloadButton;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	boolean updated = false;
	boolean userChanges = false;
	
	// files supported
	CoverageFile coverageFile = null;
	PayFile payFile = null;
	EmployeeFile employeeFile = null;
	BenefitFile benefitFile;
	Irs1094cFile irs1094cFile = null;
	
	Long docId = 0l;
	
	@FXML
	public void initialize() 
	{
		DownloadButton.setDisable(true);
		loadData();
	}

	private void loadData()
	{
		switch (DataManager.i().mCurrentCoreDataType) 
		{
			case BENEFIT:
		       	 try {
		      		BenefitFileRequest request = new BenefitFileRequest();
		     		request.setBatchId(DataManager.i().mCoreData.getBatchId());
		     		List<BenefitFile> benefitFiles = AdminPersistenceManager.getInstance().getAll(request); 
		     		if(benefitFiles != null && benefitFiles.size() > 0)
		     			benefitFile = benefitFiles.get(0);
		     		if(benefitFile != null)
		     			showBenefitFile();
		     		else {
		     			Utils.showAlert("No batchId found", "There is no matching file information for this plan");
		     			schedulePopupExit();
		     		}
		  		 
		       	 } catch(Exception e) { DataManager.i().log(Level.SEVERE, e);  }
		       	 break;
			case COVERAGE:
		       	 try {
		      		CoverageFileRequest request = new CoverageFileRequest();
		     		request.setBatchId(DataManager.i().mCoreData.getBatchId());
		     		List<CoverageFile> coverageFiles = AdminPersistenceManager.getInstance().getAll(request); 
		     		if(coverageFiles != null && coverageFiles.size() > 0)
		     			coverageFile = coverageFiles.get(0);
		     		if(coverageFile != null)
		     			showCoverageFile();
		     		else {
		     			Utils.showAlert("No batchId found", "There is no matching file information for this coverage");
		     			schedulePopupExit();
		     		}
		  		 
		       	 } catch(Exception e) { DataManager.i().log(Level.SEVERE, e);  }
		       	 break;
			case EMPLOYEE:
		       	 try {
		      		EmployeeFileRequest request = new EmployeeFileRequest();
		     		request.setBatchId(DataManager.i().mCoreData.getBatchId());
		     		List<EmployeeFile> employeeFiles = AdminPersistenceManager.getInstance().getAll(request); 
		     		if(employeeFiles != null && employeeFiles.size() > 0)
		     			employeeFile = employeeFiles.get(0);
		     		if(employeeFile != null)
		     			showEmployeeFile();
		     		else {
		     			Utils.showAlert("No batchId found", "There is no matching file information for this employee");
		     			schedulePopupExit();
		     		}
		  		 
		       	 } catch(Exception e) { 
		       		 DataManager.i().log(Level.SEVERE, e);  
		       	}
	    		break;
/*			case IRS1094C:
		       	 try {
		      		Irs1094cFileRequest request = new Irs1094cFileRequest();
		     		request.setBatchId(DataManager.i().mCoreData.getBatchId());
		     		List<Irs1094cFile> irs1094cFiles = AdminPersistenceManager.getInstance().getAll(request); 
		     		if(irs1094cFiles != null && irs1094cFiles.size() > 0)
		     			irs1094cFile = irs1094cFiles.get(0);
		     		if(irs1094cFile != null)
		     			showIrs1094cFile();
		     		else {
		     			Utils.showAlert("No batchId found", "There is no matching file information for this plan");
		     			schedulePopupExit();
		     		}
		  		 
		       	 } catch(Exception e) { DataManager.i().log(Level.SEVERE, e);  }
		       	 break; */
			case PAY:
		       	 try {
		      		PayFileRequest request = new PayFileRequest();
		     		request.setBatchId(DataManager.i().mCoreData.getBatchId());
		     		List<PayFile> payFiles = AdminPersistenceManager.getInstance().getAll(request); 
		     		if(payFiles != null && payFiles.size() > 0)
		     			payFile = payFiles.get(0);
		     		if(payFile != null)
		     			showPayFile();
		     		else {
		     			Utils.showAlert("No batchId found", "There is no matching file information for this Pay");
		     			schedulePopupExit();
		     		}
		       	 } catch(Exception e) { DataManager.i().log(Level.SEVERE, e);  }
				break;
		default:
			Utils.showAlert("Not Supported","File Information is not yet available for this file type.");
			return;
		}

	}
	
	private void schedulePopupExit()
	{
        Platform.runLater(new Runnable() {
            @Override public void run() {
                exitPopup();
            }
        });
	}
	
	private void showBenefitFile() 
	{
		TopLabel.setText("Plan File Detail");
		Utils.updateControl(BatchIdField, benefitFile.getBatchId());
		Utils.updateControl(BornOnField, benefitFile.getBornOn());
		Utils.updateControl(CoreIdField, benefitFile.getId());

		if(benefitFile.getPipelineInformation() != null) 
		{
			docId = benefitFile.getPipelineInformation().getDocumentId();
			if (docId != null)
				DownloadButton.setDisable(false);
			Utils.updateControl(DocIdField, benefitFile.getPipelineInformation().getDocumentId());
			if(benefitFile.getPipelineInformation().getDocument() != null ) 
			{
				Utils.updateControl(FileNameField, benefitFile.getPipelineInformation().getDocument().getFileName());
				if(benefitFile.getPipelineInformation().getDocument().getCreatedBy() != null) 
				{
					String Name = benefitFile.getPipelineInformation().getDocument().getCreatedBy().getLastName() + " " + 
							benefitFile.getPipelineInformation().getDocument().getCreatedBy().getFirstName();
					Utils.updateControl(UserField, Name);
				}
			}
		}
	}

	private void showCoverageFile() 
	{
		TopLabel.setText("Coverage File Detail");
		Utils.updateControl(BatchIdField, coverageFile.getBatchId());
		Utils.updateControl(BornOnField, coverageFile.getBornOn());
		Utils.updateControl(CoreIdField, coverageFile.getId());

		if(coverageFile.getPipelineInformation() != null) 
		{
			docId = coverageFile.getPipelineInformation().getDocumentId();
			if (docId != null)
				DownloadButton.setDisable(false);
			Utils.updateControl(DocIdField, coverageFile.getPipelineInformation().getDocumentId());
			if(coverageFile.getPipelineInformation().getDocument() != null ) 
			{
				Utils.updateControl(FileNameField, coverageFile.getPipelineInformation().getDocument().getFileName());
				if(coverageFile.getPipelineInformation().getDocument().getCreatedBy() != null) 
				{
					String Name = coverageFile.getPipelineInformation().getDocument().getCreatedBy().getLastName() + " " + 
							coverageFile.getPipelineInformation().getDocument().getCreatedBy().getFirstName();
					Utils.updateControl(UserField, Name);
				}
			}
		}
	}
	
	private void showCoverageGroupMembershipFile() 
	{
		TopLabel.setText("Coverage File Detail");
		Utils.updateControl(BatchIdField, coverageFile.getBatchId());
		Utils.updateControl(BornOnField, coverageFile.getBornOn());
		Utils.updateControl(CoreIdField, coverageFile.getId());

		if(coverageFile.getPipelineInformation() != null) 
		{
			docId = coverageFile.getPipelineInformation().getDocumentId();
			if (docId != null)
				DownloadButton.setDisable(false);
			Utils.updateControl(DocIdField, coverageFile.getPipelineInformation().getDocumentId());
			if(coverageFile.getPipelineInformation().getDocument() != null ) 
			{
				Utils.updateControl(FileNameField, coverageFile.getPipelineInformation().getDocument().getFileName());
				if(coverageFile.getPipelineInformation().getDocument().getCreatedBy() != null) 
				{
					String Name = coverageFile.getPipelineInformation().getDocument().getCreatedBy().getLastName() + " " + 
							coverageFile.getPipelineInformation().getDocument().getCreatedBy().getFirstName();
					Utils.updateControl(UserField, Name);
				}
			}
		}
	}
	
	private void showEmployeeFile() 
	{
		TopLabel.setText("Employee File Detail");
		Utils.updateControl(BatchIdField, employeeFile.getBatchId());
		Utils.updateControl(BornOnField, employeeFile.getBornOn());
		Utils.updateControl(CoreIdField, employeeFile.getId());

		if(employeeFile.getPipelineInformation() != null) 
		{
			docId = employeeFile.getPipelineInformation().getDocumentId();
			if (docId != null)
				DownloadButton.setDisable(false);
			Utils.updateControl(DocIdField, employeeFile.getPipelineInformation().getDocumentId());
			if(employeeFile.getPipelineInformation().getDocument() != null ) 
			{
				Utils.updateControl(FileNameField, employeeFile.getPipelineInformation().getDocument().getFileName());
				if(employeeFile.getPipelineInformation().getDocument().getCreatedBy() != null) 
				{
					String Name = employeeFile.getPipelineInformation().getDocument().getCreatedBy().getLastName() + " " + 
							employeeFile.getPipelineInformation().getDocument().getCreatedBy().getFirstName();
					Utils.updateControl(UserField, Name);
				}
			}
		}
	}
	
	private void showIrs1094cFile() 
	{
		TopLabel.setText("Ir1094c File Detail");
		Utils.updateControl(BatchIdField, irs1094cFile.getBatchId());
		Utils.updateControl(BornOnField, irs1094cFile.getBornOn());
		Utils.updateControl(CoreIdField, irs1094cFile.getId());

		if(irs1094cFile.getPipelineInformation() != null) 
		{
			docId = irs1094cFile.getPipelineInformation().getDocumentId();
			if (docId != null)
				DownloadButton.setDisable(false);
			Utils.updateControl(DocIdField, irs1094cFile.getPipelineInformation().getDocumentId());
			if(irs1094cFile.getPipelineInformation().getDocument() != null ) 
			{
				Utils.updateControl(FileNameField, irs1094cFile.getPipelineInformation().getDocument().getFileName());
				if(benefitFile.getPipelineInformation().getDocument().getCreatedBy() != null) 
				{
					String Name = irs1094cFile.getPipelineInformation().getDocument().getCreatedBy().getLastName() + " " + 
							irs1094cFile.getPipelineInformation().getDocument().getCreatedBy().getFirstName();
					Utils.updateControl(UserField, Name);
				}
			}
		}
	}

	private void showPayFile() 
	{
		TopLabel.setText("Pay File Detail");
		Utils.updateControl(BatchIdField, payFile.getBatchId());
		Utils.updateControl(BornOnField, payFile.getBornOn());
		Utils.updateControl(CoreIdField, payFile.getId());

		if(payFile.getPipelineInformation() != null) 
		{
			docId = payFile.getPipelineInformation().getDocumentId();
			if (docId != null)
				DownloadButton.setDisable(false);
			Utils.updateControl(DocIdField, payFile.getPipelineInformation().getDocumentId());
			if(payFile.getPipelineInformation().getDocument() != null ) 
			{
				Utils.updateControl(FileNameField, payFile.getPipelineInformation().getDocument().getFileName());
				if(payFile.getPipelineInformation().getDocument().getCreatedBy() != null) 
				{
					String Name = payFile.getPipelineInformation().getDocument().getCreatedBy().getLastName() + " " + 
								  payFile.getPipelineInformation().getDocument().getCreatedBy().getFirstName();
					Utils.updateControl(UserField, Name);
				}
			}
		}
	}
	
	private void exitPopup() 
	{
		Stage stage = (Stage) BatchIdField.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onDownloadFile(ActionEvent event) {
     	try {
	 		String path = Utils.browseDirectory((Stage) BatchIdField.getScene().getWindow(), "");
	 		if (path == "") return;
	 		
			if (Utils.downloadFile(docId, path) == false) {
				return;
			}
			Utils.showAlert("File Downloaded", "The selected file has been downloaded to " + path);
	     }catch (Exception e){
	    	 Utils.showAlert("No Selection", "Please select file entry to download.");
	     }
	}	

	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
}
