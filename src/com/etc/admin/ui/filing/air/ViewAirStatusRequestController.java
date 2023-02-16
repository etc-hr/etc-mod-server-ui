package com.etc.admin.ui.filing.air;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
//import com.etc.corvetto.entities.AirSubmissionStatusRequest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewAirStatusRequestController {
	
	@FXML
	private Label airTitleLabel;
	@FXML
	private TextField airUuidLabel;
	@FXML
	private TextField airUniqueTransIdLabel;
	@FXML
	private TextField airTransTimestampLabel;
	@FXML
	private TextField airTransTimestampExpirationLabel;
	@FXML
	private CheckBox airCompletedCheckBox;
	@FXML
	private Button airEditButton;
	// core data
	@FXML
	private Label airCoreIdLabel;
	@FXML
	private Label airCoreActiveLabel;
	@FXML
	private Label airCoreBODateLabel;
	@FXML
	private Label airCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		updateAirStatusRequestData();
	}
	
	private void initControls() {
		//set functionality according to the user security level
		airEditButton.setDisable(!Utils.userCanEdit());
	}

	private void updateAirStatusRequestData(){
/*		AirSubmissionStatusRequest request = DataManager.i().mAirStatusRequest;
		if (request != null) {
			Utils.updateControl(airTitleLabel,"Air Status Request");	
			Utils.updateControl(airUuidLabel, request.getUuid());
			Utils.updateControl(airUniqueTransIdLabel, request.getUniqTransId());
			Utils.updateControl(airTransTimestampLabel, request.getTransTimestamp());
			Utils.updateControl(airTransTimestampExpirationLabel, request.getTransTimestampExpiration());
			Utils.updateControl(airCompletedCheckBox, request.isCompleted());

			// core data
			Utils.updateControl(airCoreIdLabel,String.valueOf(request.getId()));
			Utils.updateControl(airCoreActiveLabel,String.valueOf(request.isActive()));
			Utils.updateControl(airCoreBODateLabel,request.getBornOn());
			Utils.updateControl(airCoreLastUpdatedLabel,request.getLastUpdated());
		}
*/	}
	
	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.AIRSTATUSREQUESTEDIT, true);
	}	
	
	//this may not be used
	@FXML
	private void onAdd1095b(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.AIRSTATUSREQUESTADD, true);
	}	
}
