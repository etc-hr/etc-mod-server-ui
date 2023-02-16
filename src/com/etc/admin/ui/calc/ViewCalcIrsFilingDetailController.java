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

public class ViewCalcIrsFilingDetailController {
	// REQUEST
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
	@FXML
	private TextField cdRequestCreatedBy;
	// SPECIFICATION
	@FXML
	private TextField cdSpecificationName;
	@FXML
	private TextField cdSpecificationDescription;
	// CHANNEL
	@FXML
	private TextField cdChannelName;
	@FXML
	private TextField cdChannelDescription;
	// OWNER
	@FXML
	private TextField cdOwnerAccount;
	@FXML
	private TextField cdOwnerEmployer;
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
	// Irs Filing
	@FXML
	private TextField cdTaxYear;
	@FXML
	private TextField cdAirMethodType;
	@FXML
	private TextField cdFilingType;
	@FXML
	private TextField cdFilingState;
	@FXML
	private TextField cdIrs1094FilingsCreated;
	@FXML
	private TextField cdIrs1094FilingsUpdated;
	@FXML
	private TextField cdIrs1095FilingsCreated;
	@FXML
	private TextField cdIrs1095FilingsUpdated;
	@FXML
	private TextField cd1094SubCreated;
	@FXML
	private TextField cd1095SubsCreated;
	@FXML
	private TextField cdAirTransmissionsCreated;
	// Errors
	@FXML
	private ListView<String> cdNotices;

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
		// specification
		if (DataManager.i().mCalcQueue.getRequest().getSpecification() != null) {
			Utils.updateControl(cdSpecificationName, DataManager.i().mCalcQueue.getRequest().getSpecification().getName());
			Utils.updateControl(cdSpecificationDescription, DataManager.i().mCalcQueue.getRequest().getSpecification().getDescription());
			if (DataManager.i().mCalcQueue.getRequest().getSpecification().getChannel() != null) {
				Utils.updateControl(cdChannelName, DataManager.i().mCalcQueue.getRequest().getSpecification().getChannel().getName());
				Utils.updateControl(cdChannelDescription, DataManager.i().mCalcQueue.getRequest().getSpecification().getChannel().getDescription());
			}
		}
		
		
		if (DataManager.i().mCalcQueue.getIrs10945Filing() != null) {
			// calculation information
			if (DataManager.i().mCalcQueue.getIrs10945Filing().getCalculationInformation() != null) {
				Utils.updateControl(cdCalcInfoCalculated, DataManager.i().mCalcQueue.getIrs10945Filing().getCalculationInformation().isCalculated());
				Utils.updateControl(cdCalcInfoIntegrated, DataManager.i().mCalcQueue.getIrs10945Filing().getCalculationInformation().isIntegrated());
				Utils.updateControl(cdCalcInfoCompleted, DataManager.i().mCalcQueue.getIrs10945Filing().getCalculationInformation().isCompleted());
				Utils.updateControl(cdCalcInfoRejected, DataManager.i().mCalcQueue.getIrs10945Filing().getCalculationInformation().isRejected());
				Utils.updateControl(cdCalcInfoDiscarded, DataManager.i().mCalcQueue.getIrs10945Filing().getCalculationInformation().isDiscarded());
				Utils.updateControl(cdCalcInfoCalculations, DataManager.i().mCalcQueue.getIrs10945Filing().getCalculationInformation().getCalculations());
			}
			
			if (DataManager.i().mCalcQueue.getIrs10945Filing().getAirMethodType() != null)
				Utils.updateControl(cdAirMethodType, DataManager.i().mCalcQueue.getIrs10945Filing().getAirMethodType().toString());
			if (DataManager.i().mCalcQueue.getIrs10945Filing().getFilingType() != null)
				Utils.updateControl(cdFilingType, DataManager.i().mCalcQueue.getIrs10945Filing().getFilingType().toString());
			if (DataManager.i().mCalcQueue.getIrs10945Filing().getFilingState() != null)
			Utils.updateControl(cdFilingState, DataManager.i().mCalcQueue.getIrs10945Filing().getFilingState().toString());
			Utils.updateControl(cdIrs1094FilingsCreated, DataManager.i().mCalcQueue.getIrs10945Filing().getIrs1094FilingsCreated());
			Utils.updateControl(cdIrs1094FilingsUpdated, DataManager.i().mCalcQueue.getIrs10945Filing().getIrs1094FilingsUpdated());
			Utils.updateControl(cdIrs1095FilingsCreated, DataManager.i().mCalcQueue.getIrs10945Filing().getIrs1095FilingsCreated());
			Utils.updateControl(cdIrs1095FilingsUpdated, DataManager.i().mCalcQueue.getIrs10945Filing().getIrs1095FilingsUpdated());
			Utils.updateControl(cd1094SubCreated, DataManager.i().mCalcQueue.getIrs10945Filing().getIrs1094SubmissionsCreated());
			Utils.updateControl(cd1095SubsCreated, DataManager.i().mCalcQueue.getIrs10945Filing().getIrs1095SubmissionsCreated());
			Utils.updateControl(cdAirTransmissionsCreated, DataManager.i().mCalcQueue.getIrs10945Filing().getAirTransmissionsCreated());
			
			// tax year and account, employer
			if (DataManager.i().mCalcQueue.getIrs10945Filing().getTaxYear() != null ) {
				Utils.updateControl(cdTaxYear, DataManager.i().mCalcQueue.getIrs10945Filing().getTaxYear().getYear());
				// owner
				if (DataManager.i().mCalcQueue.getIrs10945Filing().getCalculationInformation().getEmployer() != null ) {
					Utils.updateControl(cdOwnerEmployer, DataManager.i().mCalcQueue.getIrs10945Filing().getCalculationInformation().getEmployer().getName());
					if (DataManager.i().mCalcQueue.getIrs10945Filing().getCalculationInformation().getEmployer().getAccount() != null ) {
						Utils.updateControl(cdOwnerEmployer, DataManager.i().mCalcQueue.getIrs10945Filing().getCalculationInformation().getEmployer().getAccount().getName());
				
					}
				}
			}
			
			// notices
			if (DataManager.i().mCalcQueue.getIrs10945Filing().getNotices() != null ) {
				cdNotices.getItems().clear();
				for (CalculationNotice notice : DataManager.i().mCalcQueue.getIrs10945Filing().getNotices())
					cdNotices.getItems().add(notice.getMessage());
			}
		}
	}

	@FXML
	private void onExit(ActionEvent event) {
		Stage stage = (Stage) cdRequestDescription.getScene().getWindow();
		stage.close();		
	}	

}

