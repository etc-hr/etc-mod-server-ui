package com.etc.admin.ui.calc;

import java.util.List;
import java.util.logging.Level;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.calc.entities.CalculationNotice;
import com.etc.corvetto.ems.calc.rqs.CalculationNoticeRequest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewCalcIrs10945cDetailController {
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
	// 109XC
	@FXML
	private TextField cd109XBCTaxYear;
	@FXML
	private TextField cd109XCEmployeeId;
	@FXML
	private TextField cd109XCEmployeeFirstName;
	@FXML
	private TextField cd109XCEmployeeLastName;
	@FXML
	private TextField cd109XC1094csCreated;
	@FXML
	private TextField cd109XC1094csUpdated;
	@FXML
	private TextField cd109XC1095csCreated;
	@FXML
	private TextField cd109XC1095csUpdated;
	@FXML
	private TextField cd109XC1095csCIsCreated;
	@FXML
	private TextField cd109XC1095csCIsUpdated;
	@FXML
	private TextField cd109XCCoverageClassId;
	@FXML
	private TextField cd109XCLastUpdated;
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
		lstNotices.getItems().clear();
	}
	
	private void showData(){

  		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0);

  		if (DataManager.i().mCalcQueue == null) return;

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
			// channel
			if (DataManager.i().mCalcQueue.getRequest().getSpecification().getChannel() != null) {
				Utils.updateControl(cdChannelName, DataManager.i().mCalcQueue.getRequest().getSpecification().getChannel().getName());
				Utils.updateControl(cdChannelDescription, DataManager.i().mCalcQueue.getRequest().getSpecification().getChannel().getDescription());
			}
		}

		if(DataManager.i().mCalcQueue.getIrs10945c() != null) {
			// calculation information
			if (DataManager.i().mCalcQueue.getIrs10945c().getCalculationInformation() != null) {
				Utils.updateControl(cdCalcInfoCalculated, DataManager.i().mCalcQueue.getIrs10945c().getCalculationInformation().isCalculated());
				Utils.updateControl(cdCalcInfoIntegrated, DataManager.i().mCalcQueue.getIrs10945c().getCalculationInformation().isIntegrated());
				Utils.updateControl(cdCalcInfoCompleted, DataManager.i().mCalcQueue.getIrs10945c().getCalculationInformation().isCompleted());
				Utils.updateControl(cdCalcInfoRejected, DataManager.i().mCalcQueue.getIrs10945c().getCalculationInformation().isRejected());
				Utils.updateControl(cdCalcInfoDiscarded, DataManager.i().mCalcQueue.getIrs10945c().getCalculationInformation().isDiscarded());
				Utils.updateControl(cdCalcInfoCalculations, DataManager.i().mCalcQueue.getIrs10945c().getCalculationInformation().getCalculations());
			}
			if (DataManager.i().mCalcQueue.getIrs10945c().getEmployeeId() != null) {
				Utils.updateControl(cd109XCEmployeeId, DataManager.i().mCalcQueue.getIrs10945c().getEmployeeId());
				if (DataManager.i().mCalcQueue.getIrs10945c().getEmployee() != null && DataManager.i().mCalcQueue.getIrs10945c().getEmployee().getPerson() != null) {
					Utils.updateControl(cd109XCEmployeeFirstName, DataManager.i().mCalcQueue.getIrs10945c().getEmployee().getPerson().getFirstName());
					Utils.updateControl(cd109XCEmployeeLastName, DataManager.i().mCalcQueue.getIrs10945c().getEmployee().getPerson().getLastName());
				}
			}
			// Tax Year & Owner
			if (DataManager.i().mCalcQueue.getIrs10945c().getTaxYear() != null ) {
				Utils.updateControl(cd109XBCTaxYear, DataManager.i().mCalcQueue.getIrs10945c().getTaxYear().getYear());
				// owner
				if (DataManager.i().mCalcQueue.getIrs10945c().getCalculationInformation().getEmployer() != null ) {
					Utils.updateControl(cdOwnerEmployer, DataManager.i().mCalcQueue.getIrs10945c().getCalculationInformation().getEmployer().getName());
					if (DataManager.i().mCalcQueue.getIrs10945c().getCalculationInformation().getEmployer().getAccount() != null ) {
						Utils.updateControl(cdOwnerAccount, DataManager.i().mCalcQueue.getIrs10945c().getCalculationInformation().getEmployer().getAccount().getName());
				
					}
				}
				
			}

			Utils.updateControl(cd109XC1094csCreated, DataManager.i().mCalcQueue.getIrs10945c().getIrs1094csCreated());
			Utils.updateControl(cd109XC1094csUpdated, DataManager.i().mCalcQueue.getIrs10945c().getIrs1094csUpdated());
			Utils.updateControl(cd109XC1095csCreated, DataManager.i().mCalcQueue.getIrs10945c().getIrs1095csCreated());
			Utils.updateControl(cd109XC1095csUpdated, DataManager.i().mCalcQueue.getIrs10945c().getIrs1095csUpdated());
			Utils.updateControl(cd109XC1095csCIsCreated, DataManager.i().mCalcQueue.getIrs10945c().getIrs1095csCIsCreated());
			Utils.updateControl(cd109XC1095csCIsUpdated, DataManager.i().mCalcQueue.getIrs10945c().getIrs1095csCIsUpdated());
			Utils.updateControl(cd109XCLastUpdated, DataManager.i().mCalcQueue.getIrs10945c().getLastUpdated());
			Utils.updateControl(cd109XCCoverageClassId, DataManager.i().mCalcQueue.getIrs10945c().getCoverageGroupId());
			showNotices();
		}
	}
	
	private void showNotices() {
		try {
		if (DataManager.i().mCalcQueue.getIrs10945c() == null || DataManager.i().mCalcQueue.getIrs10945c().getId() == null) return;
			// force load any notices
			CalculationNoticeRequest req = new CalculationNoticeRequest();
			req.setIrs10945cCalculationId(DataManager.i().mCalcQueue.getIrs10945c().getId());
			List<CalculationNotice> notices = AdminPersistenceManager.getInstance().getAll(req);
			// and display
			for (CalculationNotice notice : notices) {
				lstNotices.getItems().add(notice.getMessage());
			}
		} catch(Exception e) {
			DataManager.i().log(Level.SEVERE, e); 
		}
	}

	@FXML
	private void onExit(ActionEvent event) {
		Stage stage = (Stage) cdRequestDescription.getScene().getWindow();
		stage.close();		
	}	

}

