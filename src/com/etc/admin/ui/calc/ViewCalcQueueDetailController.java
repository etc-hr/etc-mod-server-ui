package com.etc.admin.ui.calc;

import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.calc.entities.CalculationRequest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewCalcQueueDetailController {
	
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
	private TextField cdSpecificationName;
	@FXML
	private TextField cdSpecificationDescription;
	
	@FXML
	private TextField cdChannelName;
	@FXML
	private TextField cdChannelDescription;
	@FXML
	private TextField cdChannelCalcClass;
	@FXML
	private TextField cdChannelInterfaceClass;
	@FXML
	private TextField cdChannelIntegratorClass;

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
		CalculationRequest calcRequest = DataManager.i().mCalculationRequest;
		if (calcRequest == null) return;

		// request
		Utils.updateControl(cdRequestDescription, calcRequest.getDescription());
		if (calcRequest.getStatus() != null)
			Utils.updateControl(cdRequestStatus, calcRequest.getStatus().toString());
		Utils.updateControl(cdRequestProcessOn, calcRequest.getProcessOn());
		Utils.updateControl(cdRequestCompletedOn, calcRequest.getCompletedOn());
		Utils.updateControl(cdRequestCalculationId, calcRequest.getCalculationId());
		Utils.updateControl(cdRequestResult, calcRequest.getResult());
		
		if (calcRequest.getSpecification() != null) {
			Utils.updateControl(cdSpecificationName, calcRequest.getSpecification().getName());
			Utils.updateControl(cdSpecificationDescription, calcRequest.getSpecification().getDescription());
			if (calcRequest.getSpecification().getChannel() != null) {
				Utils.updateControl(cdChannelName, calcRequest.getSpecification().getChannel().getName());
				Utils.updateControl(cdChannelDescription, calcRequest.getSpecification().getChannel().getDescription());
				Utils.updateControl(cdChannelCalcClass, calcRequest.getSpecification().getChannel().getCalculationClass());
				Utils.updateControl(cdChannelInterfaceClass, calcRequest.getSpecification().getChannel().getCalculatorInterfaceClass());
				Utils.updateControl(cdChannelIntegratorClass, calcRequest.getSpecification().getChannel().getIntegratorInterfaceClass());
			}
		}
	}

	@FXML
	private void onExit(ActionEvent event) {
		Stage stage = (Stage) cdRequestDescription.getScene().getWindow();
		stage.close();		
	}	

}

