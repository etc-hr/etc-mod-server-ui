package com.etc.admin.ui.plancarrier;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils.ScreenType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ViewPlanCarrierAddController {
	
	@FXML
	private TextField carrNameLabel;
	@FXML
	private TextField carrDescriptionLabel;
	@FXML
	private Button carrCancelButton;
	@FXML
	private Button carrSaveButton;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
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
