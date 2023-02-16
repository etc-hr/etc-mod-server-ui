package com.etc.admin.ui.filing.air;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.AirFilingEvent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewAirFilingEventController {
	
	@FXML
	private Label airTitleLabel;
	@FXML
	private TextField airDescriptionLabel;
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
		updateAirFilingEventData();
	}
	
	private void initControls() {
		//set functionality according to the user security level
		airEditButton.setDisable(!Utils.userCanEdit());
	}

	private void updateAirFilingEventData(){
		AirFilingEvent event =null; // DataManager.i().mAirFilingEvent;
		if (event != null) {
			Utils.updateControl(airTitleLabel,"Air Error");	
			Utils.updateControl(airDescriptionLabel, event.getDescription());
			// core data
			Utils.updateControl(airCoreIdLabel,String.valueOf(event.getId()));
			Utils.updateControl(airCoreActiveLabel,String.valueOf(event.isActive()));
			Utils.updateControl(airCoreBODateLabel,event.getBornOn());
			Utils.updateControl(airCoreLastUpdatedLabel,event.getLastUpdated());
		}
	}
	
	private void updateAirFilingEvent(){
//		AirFilingEvent event = DataManager.i().mAirFilingEvent;
//		if (event != null) {
//			event.setDescription(airDescriptionLabel.getText());
//		}
	}
	
	private boolean validateData() {
		boolean bReturn = true;
		
		// need a description
		bReturn = Utils.validateLength(airDescriptionLabel, 5);
		
		return bReturn;
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.AIRFILINGEVENT);
	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updateAirFilingEvent();
		
		//save it to the repository and the server
		// will come back to this when the backend is in place
		//DataManager.i().saveCurrentAirFilingEvent();
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.AIRFILINGEVENT, true);
	}		
}
