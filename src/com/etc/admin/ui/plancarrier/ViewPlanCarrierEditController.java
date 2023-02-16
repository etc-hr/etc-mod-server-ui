package com.etc.admin.ui.plancarrier;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewPlanCarrierEditController {
	
	@FXML
	private TextField carrNameLabel;
	@FXML
	private TextField carrDescriptionLabel;
	@FXML
	private Button carrCancelButton;
	@FXML
	private Button carrSaveButton;
	@FXML
	private Label carrCoreIdLabel;
	@FXML
	private Label carrCoreActiveLabel;
	@FXML
	private Label carrCoreBODateLabel;
	@FXML
	private Label carrCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {

		updateCarrierData();
	}
	
	public void updateCarrierData() {
		
		if (DataManager.i().mCarrier != null) {

			Utils.updateControl(carrNameLabel,DataManager.i().mCarrier.getName());
			Utils.updateControl(carrDescriptionLabel,DataManager.i().mCarrier.getDescription());
			
			//core data read only
			Utils.updateControl(carrCoreIdLabel,String.valueOf(DataManager.i().mCarrier.getId()));
			Utils.updateControl(carrCoreActiveLabel,String.valueOf(DataManager.i().mCarrier.isActive()));
			Utils.updateControl(carrCoreBODateLabel,DataManager.i().mCarrier.getBornOn());
			Utils.updateControl(carrCoreLastUpdatedLabel,DataManager.i().mCarrier.getLastUpdated());
		}
	}
	
	public void updateCarrier() {
		
		if (DataManager.i().mCarrier != null) {

			DataManager.i().mCarrier.setName(carrNameLabel.getText());
			DataManager.i().mCarrier.setDescription(carrDescriptionLabel.getText());
		}
	}
	
	private boolean validateData() {
		boolean bReturn = true;

		//nothing to validate for now
		
		return bReturn;
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PLANCARRIER);
	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updateCarrier();
		
		//save it to the repository and the server
		//FIXME: DataManager.i().saveCurrentCarrier();
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.PLANCARRIER, true);
	}	
	
}
