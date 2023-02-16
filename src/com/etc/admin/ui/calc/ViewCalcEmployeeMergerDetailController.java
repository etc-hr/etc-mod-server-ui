package com.etc.admin.ui.calc;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.calc.entities.CalculationNotice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewCalcEmployeeMergerDetailController {
	
	// request
	@FXML
	private TextField cdRequestDescription;
	@FXML
	private TextField cdRequestStatus;
	@FXML
	private TextField cdRequestProcessOn;
	@FXML
	private TextField cdRequestCompletedOn;
	@FXML
	private TextField cdRequestCalculationId;
	@FXML
	private TextField cdRequestResult;
	// specification
	@FXML
	private TextField cdSpecificationName;
	@FXML
	private TextField cdSpecificationDescription;
	// channel
	@FXML
	private TextField cdChannelName;
	@FXML
	private TextField cdChannelDescription;
	// CALCULATION INFORMATION
	@FXML
	private CheckBox cdCalcInfoCalculated;
	@FXML
	private CheckBox cdCalcInfoCompleted;
	@FXML
	private CheckBox cdCalcInfoIntegrated;
	@FXML
	private CheckBox cdCalcInfoRejected;
	@FXML
	private CheckBox cdCalcInfoDiscarded;
	@FXML
	private TextField cdCalcInfoCalculations;
	// Merger 
	@FXML
	private TextField cdMergerEmp1FirstName;
	@FXML
	private TextField cdMergerEmp1LastName;
	@FXML
	private TextField cdMergerEmp1Id;
	@FXML
	private TextField cdMergerEmp1SSN;
	@FXML
	private TextField cdMergerEmp1LastUpdate;
	@FXML
	private TextField cdMergerEmp2FirstName;
	@FXML
	private TextField cdMergerEmp2LastName;
	@FXML
	private TextField cdMergerEmp2Id;
	@FXML
	private TextField cdMergerEmp2SSN;
	@FXML
	private TextField cdMergerEmp2LastUpdate;
	// OWNER
	@FXML
	private TextField cdOwnerAccount;
	@FXML
	private TextField cdOwnerEmployer;
	// NOTICES
	@FXML
	private ListView<String> lstNotices;

	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		showData();
	}
	
	private void initControls() {
		// init here
	}
	
	private void showData(){

  		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0);
		
		// request
		Utils.updateControl(cdRequestDescription, DataManager.i().mCalcQueue.getRequest().getDescription());
		if (DataManager.i().mCalcQueue.getRequest().getStatus() != null)
			Utils.updateControl(cdRequestStatus, DataManager.i().mCalcQueue.getRequest().getStatus().toString());
		Utils.updateControl(cdRequestProcessOn, DataManager.i().mCalcQueue.getRequest().getProcessOn());
		Utils.updateControl(cdRequestCompletedOn, DataManager.i().mCalcQueue.getRequest().getCompletedOn());
		Utils.updateControl(cdRequestCalculationId, DataManager.i().mCalcQueue.getRequest().getCalculationId());
		Utils.updateControl(cdRequestResult, DataManager.i().mCalcQueue.getRequest().getResult());
		
		if (DataManager.i().mCalcQueue.getRequest().getSpecification() != null) {
			Utils.updateControl(cdSpecificationName, DataManager.i().mCalcQueue.getRequest().getSpecification().getName());
			Utils.updateControl(cdSpecificationDescription, DataManager.i().mCalcQueue.getRequest().getSpecification().getDescription());
			if (DataManager.i().mCalcQueue.getRequest().getSpecification().getChannel() != null) {
				Utils.updateControl(cdChannelName, DataManager.i().mCalcQueue.getRequest().getSpecification().getChannel().getName());
				Utils.updateControl(cdChannelDescription, DataManager.i().mCalcQueue.getRequest().getSpecification().getChannel().getDescription());
			}
		}
		
		if (DataManager.i().mCalcQueue.getEmployeeMerger() != null) {
			// calculation information
			if (DataManager.i().mCalcQueue.getEmployeeMerger().getCalculationInformation() != null) {
				Utils.updateControl(cdCalcInfoCalculated, DataManager.i().mCalcQueue.getEmployeeMerger().getCalculationInformation().isCalculated());
				Utils.updateControl(cdCalcInfoIntegrated, DataManager.i().mCalcQueue.getEmployeeMerger().getCalculationInformation().isIntegrated());
				Utils.updateControl(cdCalcInfoCompleted, DataManager.i().mCalcQueue.getEmployeeMerger().getCalculationInformation().isCompleted());
				Utils.updateControl(cdCalcInfoRejected, DataManager.i().mCalcQueue.getEmployeeMerger().getCalculationInformation().isRejected());
				Utils.updateControl(cdCalcInfoDiscarded, DataManager.i().mCalcQueue.getEmployeeMerger().getCalculationInformation().isDiscarded());
				Utils.updateControl(cdCalcInfoCalculations, DataManager.i().mCalcQueue.getEmployeeMerger().getCalculationInformation().getCalculations());
			}
			// employee 1
			if (DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee1() != null) {
				if (DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee1().getEmployer() != null) {
					Utils.updateControl(cdOwnerEmployer, DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee1().getEmployer().getName());
					if (DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee1().getEmployer().getAccount() != null)
						Utils.updateControl(cdOwnerAccount, DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee1().getEmployer().getAccount().getName());
				}
				
				Utils.updateControl(cdMergerEmp1Id, DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee1().getId());
				if (DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee1().getPerson() != null) {
					Utils.updateControl(cdMergerEmp1FirstName, DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee1().getPerson().getFirstName());
					Utils.updateControl(cdMergerEmp1LastName, DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee1().getPerson().getLastName());
					Utils.updateControl(cdMergerEmp1LastUpdate, DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee1().getLastUpdated());
					if (DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee1().getPerson().getSsn() != null)
						Utils.updateControl(cdMergerEmp1SSN, DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee1().getPerson().getSsn().getUsrln());
				}
			}
			// employee 2
			if (DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee2() != null) {
				Utils.updateControl(cdMergerEmp2Id, DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee2().getId());
				if (DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee2().getPerson() != null) {
					Utils.updateControl(cdMergerEmp2FirstName, DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee2().getPerson().getFirstName());
					Utils.updateControl(cdMergerEmp2LastName, DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee2().getPerson().getLastName());
					Utils.updateControl(cdMergerEmp2LastUpdate, DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee2().getLastUpdated());
					if (DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee2().getPerson().getSsn() != null)
						Utils.updateControl(cdMergerEmp2SSN, DataManager.i().mCalcQueue.getEmployeeMerger().getEmployee2().getPerson().getSsn().getUsrln());
				}
			}	
			
			showNotices();
		}
	}
	
	private void showNotices() {
		if (DataManager.i().mCalcQueue.getEmployeeMerger() == null || DataManager.i().mCalcQueue.getEmployeeMerger().getNotices() == null) return;
		
		for (CalculationNotice notice : DataManager.i().mCalcQueue.getEmployeeMerger().getNotices()) {
			lstNotices.getItems().add(notice.getMessage());
		}
	}

	@FXML
	private void onExit(ActionEvent event) {
		Stage stage = (Stage) cdRequestDescription.getScene().getWindow();
		stage.close();		
	}	

}

