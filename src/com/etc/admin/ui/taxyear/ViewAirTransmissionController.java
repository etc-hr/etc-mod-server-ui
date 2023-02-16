package com.etc.admin.ui.taxyear;

import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.AirTransmission;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewAirTransmissionController {
	@FXML
	private CheckBox atCompletedCheck;
	@FXML
	private TextField atAirMethodTypeField;
	@FXML
	private TextField atAirStatusTypeField;
	@FXML
	private TextField atAirFormTypeField;
	@FXML
	private TextField atAirOperationTypeField;
	@FXML
	private TextField atDataFilenameField;
	@FXML
	private TextField atDataFilesizeField;
	@FXML
	private TextField atYearField;
	@FXML
	private TextField atManifestFilenameField;
	@FXML
	private TextField atDataFileChecksumField;
	@FXML
	private TextField atReceiptIdField;
	@FXML
	private TextField atReceiptTimeField;
	@FXML
	private TextField atTransTimeField;
	@FXML
	private TextField atTransExpireField;
	@FXML
	private TextField atOriginalReceiptIdField;
	@FXML
	private TextField atUUIDField;
	@FXML
	private TextField atUniqueTransIdField;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		updateFilingData();
	}
	
	private void updateFilingData() {
		AirTransmission transmission = DataManager.i().mAirTransmission;
		if (transmission != null) {
			Utils.updateControl(atAirMethodTypeField, String.valueOf(transmission.getMethodType()));
			Utils.updateControl(atAirStatusTypeField, String.valueOf(transmission.getStatus()));
			Utils.updateControl(atAirFormTypeField, String.valueOf(transmission.getFormType()));
			Utils.updateControl(atAirOperationTypeField, String.valueOf(transmission.getOperationType()));
			Utils.updateControl(atDataFilenameField, transmission.getDataFileFilename());
			Utils.updateControl(atDataFilesizeField, transmission.getDataFileSizeInBytes());
			Utils.updateControl(atYearField, transmission.getYear());
			Utils.updateControl(atManifestFilenameField, transmission.getManifestFilename());
			Utils.updateControl(atDataFileChecksumField, String.valueOf(transmission.getStatus()));
			Utils.updateControl(atReceiptIdField, transmission.getReceiptId());
			Utils.updateControl(atReceiptTimeField, transmission.getReceiptTimestamp());
			Utils.updateControl(atTransTimeField, transmission.getTransTimestamp());
			Utils.updateControl(atTransExpireField, transmission.getTransTimestampExpiration());
			Utils.updateControl(atOriginalReceiptIdField, transmission.getOriginalReceiptId());
			Utils.updateControl(atUUIDField, transmission.getUuid());
			Utils.updateControl(atUniqueTransIdField, transmission.getUniqTransId());
			Utils.updateControl(atCompletedCheck, transmission.isCompleted());
		}
	}
	
	@FXML
	private void onClose(ActionEvent event) {
		Stage stage = (Stage) atAirMethodTypeField.getScene().getWindow();
		stage.close();
	}	
	
}


