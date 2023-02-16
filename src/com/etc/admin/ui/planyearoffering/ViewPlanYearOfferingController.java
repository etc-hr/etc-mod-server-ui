package com.etc.admin.ui.planyearoffering;

import java.text.SimpleDateFormat;
import com.etc.admin.EtcAdmin;
import com.etc.admin.utils.Utils.ScreenType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ViewPlanYearOfferingController {
	
	@FXML
	private Label emrcvNameLabel;
	@FXML
	private Label emrcvStartDateLabel;
	@FXML
	private Label emrcvEndDateLabel;
	@FXML
	private Label emrcvEECNameLabel;
	@FXML
	private Label emrcvEECDescriptionLabel;
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
			Utils.updateControl(emrcvNameLabel,DataManager.i().mEmployerCoveragePeriod.getName());
			Utils.setControlDate(emrcvStartDateLabel,DataManager.i().mEmployerCoveragePeriod.getPlanStartDate());
			Utils.setControlDate(emrcvEndDateLabel,DataManager.i().mEmployerCoveragePeriod.getPlanEndDate());
			
			// core data
			Utils.updateControl(emrcvCoreIdLabel,String.valueOf(DataManager.i().mEmployerCoveragePeriod.getId()));
			Utils.updateControl(emrcvCoreActiveLabel,String.valueOf(DataManager.i().mEmployerCoveragePeriod.isActive()));
			Utils.setControlDate(emrcvCoreBODateLabel,DataManager.i().mEmployerCoveragePeriod.getBornOn());
			Utils.setControlDate(emrcvCoreLastUpdatedLabel,DataManager.i().mEmployerCoveragePeriod.getLastUpdated());
			
			if (DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass() != null) {
				Utils.updateControl(emrcvEECNameLabel,DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass().getName());
				Utils.updateControl(emrcvEECDescriptionLabel,DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass().getDescription());
				
				// core data
				Utils.updateControl(emrcvEECCoreIdLabel,String.valueOf(DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass().getId()));
				Utils.updateControl(emrcvEECCoreActiveLabel,String.valueOf(DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass().isActive()));
				Utils.setControlDate(emrcvEECCoreBODateLabel,DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass().getBornOn());
				Utils.setControlDate(emrcvEECCoreLastUpdatedLabel,DataManager.i().mEmployerCoveragePeriod.getEmployeeEligibilityClass().getLastUpdated());				
			}else {
				Utils.updateControl(emrcvEECNameLabel,"");
				Utils.updateControl(emrcvEECDescriptionLabel,"");
				
				// core data
				Utils.updateControl(emrcvEECCoreIdLabel,"");
				Utils.updateControl(emrcvEECCoreActiveLabel,"");
				Utils.updateControl(emrcvEECCoreBODateLabel,"");
				Utils.updateControl(emrcvEECCoreLastUpdatedLabel,"");				
				
			}
		}
		*/
	}
	
	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PLANYEAROFFERINGEDIT, true);
	}	
	
}
