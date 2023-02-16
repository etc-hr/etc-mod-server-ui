package com.etc.admin.ui.planyearofferingplan;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewPlanYearOfferingPlanEditController {
	
	@FXML
	private TextField pyopMemberShareLabel;
	@FXML
	private TextField pyopPlanYearOfferingLabel;
	@FXML
	private TextField pyopPlanLabel;
	@FXML
	private Label pyopCoreIdLabel;
	@FXML
	private Label pyopCoreActiveLabel;
	@FXML
	private Label pyopCoreBODateLabel;
	@FXML
	private Label pyopCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {

		updatePlanYearOfferingPlanData();
	}
	
	public void updatePlanYearOfferingPlanData() {
		
		if (DataManager.i().mPlanYearOfferingPlan != null) {

			//Utils.updateControl(pyopMemberShareLabel,DataManager.i().mPlanYearOfferingPlan.getName(pyopDescriptionLabel));
			Utils.updateControl(pyopPlanYearOfferingLabel,DataManager.i().mCarrier.getDescription());
			Utils.updateControl(pyopPlanLabel,DataManager.i().mCarrier.getDescription());
			
			//core data read only
			Utils.updateControl(pyopCoreIdLabel,String.valueOf(DataManager.i().mCarrier.getId()));
			Utils.updateControl(pyopCoreActiveLabel,String.valueOf(DataManager.i().mCarrier.isActive()));
			Utils.updateControl(pyopCoreBODateLabel,DataManager.i().mCarrier.getBornOn());
			Utils.updateControl(pyopCoreLastUpdatedLabel,DataManager.i().mCarrier.getLastUpdated());
		}
	}
	
	public void updatePlanYearOfferingPlan() {
		
		if (DataManager.i().mCarrier != null) {

			//DataManager.i().mCarrier.setName(pyopMemberShareLabel.getText());
			//DataManager.i().mCarrier.setDescription(pyopDescriptionLabel.getText());
		}
	}
	
	private boolean validateData() {
		boolean bReturn = true;

		//nothing to validate for now
		
		return bReturn;
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PLANYEAROFFERINGPLAN);
	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updatePlanYearOfferingPlan();
		
		//save it to the repository and the server
		//FIXME: DataManager.i().savePlanYearOfferingPlan(DataManager.i().mPlanYearOfferingPlan);
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.PLANYEAROFFERINGPLAN, true);
	}	
	
}
