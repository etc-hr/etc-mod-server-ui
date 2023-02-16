package com.etc.admin.ui.planyearoffering;

import java.text.SimpleDateFormat;
import com.etc.admin.EtcAdmin;
import com.etc.admin.utils.Utils.ScreenType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewPlanYearOfferingEditController {
	
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
	@FXML
	private Label emrcvCoreIdLabel;
	@FXML
	private Label emrcvCoreActiveLabel;
	@FXML
	private Label emrcvCoreBODateLabel;
	@FXML
	private Label emrcvCoreLastUpdatedLabel;
	
	@FXML
	private Label emrcvEECCoreIdLabel;
	@FXML
	private Label emrcvEECCoreActiveLabel;
	@FXML
	private Label emrcvEECCoreBODateLabel;
	@FXML
	private Label emrcvEECCoreLastUpdatedLabel;

	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		updateEmployerCoveragePeriodData();
	}

	private void updateEmployerCoveragePeriodData(){
	/*	
		if (DataManager.i().mEmployerCoveragePeriod != null) {
			Utils.updateControl(emrcvNameField,DataManager.i().mEmployerCoveragePeriod.getName());
			Utils.setControlDate(emrcvStartDatePicker,DataManager.i().mEmployerCoveragePeriod.getPlanStartDate());
			Utils.setControlDate(emrcvEndDatePicker,DataManager.i().mEmployerCoveragePeriod.getPlanEndDate());
			
			// core data
			Utils.updateControl(emrcvCoreIdLabel,String.valueOf(DataManager.i().mEmployerCoveragePeriod.getId()));
			Utils.updateControl(emrcvCoreActiveLabel,String.valueOf(DataManager.i().mEmployerCoveragePeriod.isActive()));
			Utils.setControlDate(emrcvCoreBODateLabel,DataManager.i().mEmployerCoveragePeriod.getBornOn());
			Utils.setControlDate(emrcvCoreLastUpdatedLabel,DataManager.i().mEmployerCoveragePeriod.getLastUpdated());
			
			if (DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass() != null) {
				Utils.updateControl(emrcvEECNameField,DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass().getName());
				Utils.updateControl(emrcvEECDescriptionField,DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass().getDescription());
				
				// core data
				Utils.updateControl(emrcvEECCoreIdLabel,String.valueOf(DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass().getId()));
				Utils.updateControl(emrcvEECCoreActiveLabel,String.valueOf(DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass().isActive()));
				Utils.setControlDate(emrcvEECCoreBODateLabel,DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass().getBornOn());
				Utils.setControlDate(emrcvEECCoreLastUpdatedLabel,DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass().getLastUpdated());				
			}else {
				Utils.updateControl(emrcvEECNameField,"");
				Utils.updateControl(emrcvEECDescriptionField,"");
				
				// core data
				Utils.updateControl(emrcvEECCoreIdLabel,"");
				Utils.updateControl(emrcvEECCoreActiveLabel,"");
				Utils.updateControl(emrcvEECCoreBODateLabel,"");
				Utils.updateControl(emrcvEECCoreLastUpdatedLabel,"");				
				
			}
		}
		*/
	}
	
	private void updateEmployerCoveragePeriod(){
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
		updateEmployerCoveragePeriod();
		
		//save it to the repository and the server
		//DataManager.i().saveEmployer(DataManager.i().mEmployer);
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.PLANYEAROFFERING, true);
	}	
}
