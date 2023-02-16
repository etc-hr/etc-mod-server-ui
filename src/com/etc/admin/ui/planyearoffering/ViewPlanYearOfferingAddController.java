package com.etc.admin.ui.planyearoffering;

import com.etc.admin.EtcAdmin;
import com.etc.admin.utils.Utils.ScreenType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ViewPlanYearOfferingAddController {
	
	@FXML
	private TextField emrcvNameField;
	@FXML
	private DatePicker emrcvStartDatePicker;
	@FXML
	private DatePicker emrcvEndDatePicker;
	@FXML
	private TextField emrcvEECNameField;
	@FXML
	private TextField emrcvEECDescriptionField;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
	}
	
	private void updatePlanYearOffering(){
/*		
		if (DataManager.i().mEmployerCoveragePeriod != null) {
			DataManager.i().mEmployerCoveragePeriod.setName(emrcvNameField.getText());
			if (emrcvStartDatePicker.getValue() != null)
				DataManager.i().mEmployerCoveragePeriod.setPlanStartDate(Utils.getCalDate(emrcvStartDatePicker.getValue()));
			if (emrcvEndDatePicker.getValue() != null)
				DataManager.i().mEmployerCoveragePeriod.setPlanEndDate(Utils.getCalDate(emrcvEndDatePicker.getValue()));

			if (DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass() != null) {
				DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass().setName(emrcvEECNameField.getText());
				DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass().setDescription(emrcvEECDescriptionField.getText());
			}
		}
		*/
	}
	
	private boolean validateData() {
		boolean bReturn = true;

		//nothing to validate for now
		
		return bReturn;
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PLANYEAROFFERING);
	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updatePlanYearOffering();
		
		//save it to the repository and the server
		//DataManager.i().saveEmployer(DataManager.i().mEmployer);
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.PLANYEAROFFERING, true);
	}	
}
