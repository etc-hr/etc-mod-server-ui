package com.etc.admin.ui.taxyear;

import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.Irs1095Filing;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewIRS1095FilingController {
	@FXML
	private CheckBox flngReadyToFileCheck;
	@FXML
	private CheckBox flngCorrectedCheck;
	@FXML
	private CheckBox flngRemoveCoverageCheck;
	@FXML
	private CheckBox flngAbandonedCheck;
	@FXML
	private TextField flngFilingTypeField;
	@FXML
	private TextField flngFilingStateField;
	@FXML
	private TextField flngAirFormTypeField;
	@FXML
	private TextField flngAirOperationTypeField;
	@FXML
	private TextField flngAirStatusTypeField;
	@FXML
	private TextField flngLastSubmittedField;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		updateFilingData();
	}
	
	private void updateFilingData() {
		Irs1095Filing filing = DataManager.i().mIrs1095Filing;
		Utils.updateControl(flngReadyToFileCheck, filing.isReadyToFile());
		Utils.updateControl(flngCorrectedCheck, filing.isCorrected());
		Utils.updateControl(flngRemoveCoverageCheck, filing.isRemoveCoverage());
		Utils.updateControl(flngAbandonedCheck, filing.isAbandoned());
		Utils.updateControl(flngFilingTypeField, String.valueOf(filing.getFilingType()));
		Utils.updateControl(flngFilingStateField, String.valueOf(filing.getFilingState()));
		Utils.updateControl(flngAirFormTypeField, String.valueOf(filing.getFormType()));
		Utils.updateControl(flngAirOperationTypeField, String.valueOf(filing.getOperationType()));
		Utils.updateControl(flngAirStatusTypeField, String.valueOf(filing.getStatus()));
		Utils.updateControl(flngLastSubmittedField, (filing.getLastSubmitted()));
	}
	
	@FXML
	private void onClose(ActionEvent event) {
		Stage stage = (Stage) flngCorrectedCheck.getScene().getWindow();
		stage.close();
	}	
	
}


