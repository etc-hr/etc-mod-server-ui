package com.etc.admin.ui.plancoverageclass;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewPlanCoverageClassEditController {
	
	@FXML
	private TextField cvclsNameLabel;
	@FXML
	private TextField cvclsDescriptionLabel;
	@FXML
	private Label cvclsCoreIdLabel;
	@FXML
	private Label cvclsCoreActiveLabel;
	@FXML
	private Label cvclsCoreBODateLabel;
	@FXML
	private Label cvclsCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {

		updateCoverageClassData();
	}
	
	public void updateCoverageClassData() {
		
		if (DataManager.i().mCoverageClass != null) {

			Utils.updateControl(cvclsNameLabel,DataManager.i().mCoverageClass.getName());
			Utils.updateControl(cvclsDescriptionLabel,DataManager.i().mCoverageClass.getDescription());
			
			//core data read only
			Utils.updateControl(cvclsCoreIdLabel,String.valueOf(DataManager.i().mCoverageClass.getId()));
			Utils.updateControl(cvclsCoreActiveLabel,String.valueOf(DataManager.i().mCoverageClass.isActive()));
			Utils.updateControl(cvclsCoreBODateLabel,DataManager.i().mCoverageClass.getBornOn());
			Utils.updateControl(cvclsCoreLastUpdatedLabel,DataManager.i().mCoverageClass.getLastUpdated());
		}
	}
	
	public void updateCoverageClass() {
		
		if (DataManager.i().mCoverageClass != null) {

			DataManager.i().mCoverageClass.setName(cvclsNameLabel.getText());
			DataManager.i().mCoverageClass.setDescription(cvclsDescriptionLabel.getText());
		}
	}
	
	private boolean validateData() {
		boolean bReturn = true;

		//nothing to validate for now
		
		return bReturn;
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PLANCOVERAGECLASS);
	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updateCoverageClass();
		
		//save it to the repository and the server
		//FIXME: DataManager.i().saveCurrentCarrier();
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.PLANCOVERAGECLASS, true);
	}	
	
}
