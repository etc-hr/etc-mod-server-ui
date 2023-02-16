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

public class ViewCalcIrs10945bDetailController {
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
	// 109XB
	@FXML
	private TextField cd109XBTaxYear;
	@FXML
	private TextField cd109XB1094bsCreated;
	@FXML
	private TextField cd109XB1094bsUpdated;
	@FXML
	private TextField cd109XB1095bsCreated;
	@FXML
	private TextField cd109XB1095bsUpdated;
	@FXML
	private TextField cd109XB1095bsCIsCreated;
	@FXML
	private TextField cd109XB1095bsCIsUpdated;
	@FXML
	private TextField cd109XBLastUpdated;
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
		// specification
		if (DataManager.i().mCalcQueue.getRequest().getSpecification() != null) {
			Utils.updateControl(cdSpecificationName, DataManager.i().mCalcQueue.getRequest().getSpecification().getName());
			Utils.updateControl(cdSpecificationDescription, DataManager.i().mCalcQueue.getRequest().getSpecification().getDescription());
			if (DataManager.i().mCalcQueue.getRequest().getSpecification().getChannel() != null) {
				Utils.updateControl(cdChannelName, DataManager.i().mCalcQueue.getRequest().getSpecification().getChannel().getName());
				Utils.updateControl(cdChannelDescription, DataManager.i().mCalcQueue.getRequest().getSpecification().getChannel().getDescription());
			}
		}
		
		
		if (DataManager.i().mCalcQueue.getIrs10945b() != null) {
			// calculation information
			if (DataManager.i().mCalcQueue.getIrs10945b().getCalculationInformation() != null) {
				Utils.updateControl(cdCalcInfoCalculated, DataManager.i().mCalcQueue.getIrs10945b().getCalculationInformation().isCalculated());
				Utils.updateControl(cdCalcInfoIntegrated, DataManager.i().mCalcQueue.getIrs10945b().getCalculationInformation().isIntegrated());
				Utils.updateControl(cdCalcInfoCompleted, DataManager.i().mCalcQueue.getIrs10945b().getCalculationInformation().isCompleted());
				Utils.updateControl(cdCalcInfoRejected, DataManager.i().mCalcQueue.getIrs10945b().getCalculationInformation().isRejected());
				Utils.updateControl(cdCalcInfoDiscarded, DataManager.i().mCalcQueue.getIrs10945b().getCalculationInformation().isDiscarded());
				Utils.updateControl(cdCalcInfoCalculations, DataManager.i().mCalcQueue.getIrs10945b().getCalculationInformation().getCalculations());
			}
			
			Utils.updateControl(cd109XB1094bsCreated, DataManager.i().mCalcQueue.getIrs10945b().getIrs1094bsCreated());
			Utils.updateControl(cd109XB1094bsUpdated, DataManager.i().mCalcQueue.getIrs10945b().getIrs1094bsUpdated());
			Utils.updateControl(cd109XB1095bsCreated, DataManager.i().mCalcQueue.getIrs10945b().getIrs1095bsCreated());
			Utils.updateControl(cd109XB1095bsUpdated, DataManager.i().mCalcQueue.getIrs10945b().getIrs1095bsUpdated());
			Utils.updateControl(cd109XB1095bsCIsCreated, DataManager.i().mCalcQueue.getIrs10945b().getIrs1095bsCIsCreated());
			Utils.updateControl(cd109XB1095bsCIsUpdated, DataManager.i().mCalcQueue.getIrs10945b().getIrs1095bsCIsUpdated());
			Utils.updateControl(cd109XBLastUpdated, DataManager.i().mCalcQueue.getIrs10945b().getLastUpdated());
			if (DataManager.i().mCalcQueue.getIrs10945b().getTaxYear() != null ) {
				Utils.updateControl(cd109XBTaxYear, DataManager.i().mCalcQueue.getIrs10945b().getTaxYear().getYear());
			}
			// owner
			if (DataManager.i().mCalcQueue.getIrs10945b().getCalculationInformation().getEmployer() != null ) {
				Utils.updateControl(cdOwnerEmployer, DataManager.i().mCalcQueue.getIrs10945b().getCalculationInformation().getEmployer().getName());
				if (DataManager.i().mCalcQueue.getIrs10945b().getCalculationInformation().getEmployer().getAccount() != null ) {
					Utils.updateControl(cdOwnerAccount, DataManager.i().mCalcQueue.getIrs10945b().getCalculationInformation().getEmployer().getAccount().getName());
			
				}
			}
			
			showNotices();
		}
	}
	
	private void showNotices() {
		try {
			if (DataManager.i().mCalcQueue.getIrs10945b() == null || DataManager.i().mCalcQueue.getIrs10945b().getNotices() == null) return;
				// force load any notices
				CalculationNoticeRequest req = new CalculationNoticeRequest();
				req.setIrs10945bCalculationId(DataManager.i().mCalcQueue.getIrs10945b().getId());
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

