package com.etc.admin.ui.employereligibilityperiod;

import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewEmployerEligibilityPeriodController {
	
	@FXML
	private TextField elpdEligibilityPeriodDaysLabel;
	@FXML
	private CheckBox elpdQualifyingOfferMethodCheckBox;
	@FXML
	private CheckBox elpdNinetyEightPercentOfferMethodCheckBox;
	@FXML
	private Label elpdCoreIdLabel;
	@FXML
	private Label elpdCoreActiveLabel;
	@FXML
	private Label elpdCoreBODateLabel;
	@FXML
	private Label elpdCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		updatePeriodData();
	}

	private void updatePeriodData(){
		
/*		if (DataManager.i().mEmployerEligibilityPeriod != null) {
				
			Utils.updateControl(elpdEligibilityPeriodDaysLabel,String.valueOf(DataManager.i().mEmployerEligibilityPeriod.getEligibilityPeriodDays()));
			Utils.updateControl(elpdQualifyingOfferMethodCheckBox,DataManager.i().mEmployerEligibilityPeriod.isQualifyingOfferMethod());
			Utils.updateControl(elpdNinetyEightPercentOfferMethodCheckBox,DataManager.i().mEmployerEligibilityPeriod.isNinetyEightPctOfferMethod());

			// core data
			Utils.updateControl(elpdCoreIdLabel,String.valueOf(DataManager.i().mEmployerEligibilityPeriod.getId()));
			Utils.updateControl(elpdCoreActiveLabel,String.valueOf(DataManager.i().mEmployerEligibilityPeriod.isActive()));
			Utils.updateControl(elpdCoreBODateLabel,DataManager.i().mEmployerEligibilityPeriod.getBornOn());
			Utils.updateControl(elpdCoreLastUpdatedLabel,DataManager.i().mEmployerEligibilityPeriod.getLastUpdated());
		}
*/	}
	
	@FXML
	private void onEdit(ActionEvent event) {
		//EtcAdmin.i().setScreen(ScreenType.DEPARTMENTEDIT);
	}	
	
}
