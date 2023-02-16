package com.etc.admin.ui.filing.air;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.AirError;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewAirErrorController {
	
	@FXML
	private Label airTitleLabel;
	@FXML
	private TextField airCodeLabel;
	@FXML
	private TextField airDescriptionLabel;
	@FXML
	private TextField airIrsDescriptionLabel;
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
		updateAirErrorData();
	}
	
	private void initControls() {
		//set functionality according to the user security level
		airEditButton.setDisable(!Utils.userCanEdit());
	}

	private void updateAirErrorData(){
		AirError error = DataManager.i().mAirError;
		if (error != null) {
			Utils.updateControl(airTitleLabel,"Air Error");	

			Utils.updateControl(airCodeLabel, error.getCode());
			Utils.updateControl(airDescriptionLabel, error.getDescription());
			Utils.updateControl(airIrsDescriptionLabel, error.getIrsDescription());

			// core data
			Utils.updateControl(airCoreIdLabel,String.valueOf(error.getId()));
			Utils.updateControl(airCoreActiveLabel,String.valueOf(error.isActive()));
			Utils.updateControl(airCoreBODateLabel,error.getBornOn());
			Utils.updateControl(airCoreLastUpdatedLabel,error.getLastUpdated());
		}
	}
	
	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.AIRERROREDIT, true);
	}	
	
	//this may not be used
	@FXML
	private void onAdd1095b(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.AIRERRORADD, true);
	}	
}
