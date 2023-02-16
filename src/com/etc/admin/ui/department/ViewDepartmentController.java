package com.etc.admin.ui.department;

import java.text.SimpleDateFormat;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewDepartmentController {
	
	@FXML
	private Label deptTitleLabel;
	@FXML
	private TextField deptNameLabel;
	@FXML
	private TextField deptDescriptionLabel;
	@FXML
	private Label deptCoreIdLabel;
	@FXML
	private Label deptCoreActiveLabel;
	@FXML
	private Label deptCoreBODateLabel;
	@FXML
	private Label deptCoreLastUpdatedLabel;
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		updateDepartmentData();
	}

	private void updateDepartmentData(){
		
		if (DataManager.i().mDepartment != null) {
			String sName = DataManager.i().mEmployer.getName() + " Department";
				
			Utils.updateControl(deptTitleLabel,sName);
			Utils.updateControl(deptNameLabel,DataManager.i().mDepartment.getName());
			Utils.updateControl(deptDescriptionLabel,DataManager.i().mDepartment.getDescription());

			// core data
			Utils.updateControl(deptCoreIdLabel,String.valueOf(DataManager.i().mDepartment.getId()));
			Utils.updateControl(deptCoreActiveLabel,String.valueOf(DataManager.i().mDepartment.isActive()));
			Utils.updateControl(deptCoreBODateLabel,DataManager.i().mDepartment.getBornOn());
			Utils.updateControl(deptCoreLastUpdatedLabel,DataManager.i().mDepartment.getLastUpdated());
		}
	}
	
	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.DEPARTMENTEDIT);
	}	
	
	
	
}
