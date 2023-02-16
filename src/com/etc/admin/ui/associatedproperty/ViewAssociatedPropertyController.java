package com.etc.admin.ui.associatedproperty;

import java.text.SimpleDateFormat;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils.ScreenType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ViewAssociatedPropertyController {
	
	@FXML
	private Label apropTitleLabel;
	@FXML
	private Label apropNameLabel;
	@FXML
	private Label apropValueLabel;
	@FXML
	private Label apropVersionLabel;
	@FXML
	private Label apropDefIdLabel;
	@FXML
	private Label apropCoreIdLabel;
	@FXML
	private Label apropCoreActiveLabel;
	@FXML
	private Label apropCoreBODateLabel;
	@FXML
	private Label apropCoreLastUpdatedLabel;
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		updateAssociatedPropertyData();
	}

	private void updateAssociatedPropertyData(){
		/*
		if (DataManager.i().mAssociatedProperty != null) {
			String sName;
			if (DataManager.i().mAssociatedPropertyType == 0)
				sName = DataManager.i().mAccount.getName() + " Associated Property";
			else
				sName = DataManager.i().mEmployer.getName() + " Associated Property";
				
			Utils.updateControl(apropTitleLabel,sName);
			Utils.updateControl(apropNameLabel,DataManager.i().mAssociatedProperty.getName());
			Utils.updateControl(apropValueLabel,DataManager.i().mAssociatedProperty.getValue());
			Utils.updateControl(apropVersionLabel,"");

			// need to clarify the relationship between the property and the property definition
			AssociatedPropertyDefinition propDef = DataManager.i().mAssociatedProperty.getAssociatedPropertyDefinition();
			if (propDef != null)
				Utils.updateControl(apropDefIdLabel,propDef.getName());
			else
				Utils.updateControl(apropDefIdLabel,"");
				
			// core data
			Utils.updateControl(apropCoreIdLabel,String.valueOf(DataManager.i().mAssociatedProperty.getId()));
			Utils.updateControl(apropCoreActiveLabel,String.valueOf(DataManager.i().mAssociatedProperty.isActive()));
			Utils.setControlDate(apropCoreBODateLabel,DataManager.i().mAssociatedProperty.getBornOn());
			Utils.setControlDate(apropCoreLastUpdatedLabel,DataManager.i().mAssociatedProperty.getLastUpdated());
			
		}
		*/
	}
	
	@FXML
	private void onEdit(ActionEvent event) {
		if (DataManager.i().mAssociatedPropertyType == 0)
			EtcAdmin.i().setScreen(ScreenType.ACCOUNTASSOCIATEDPROPERTYEDIT);
		else
			EtcAdmin.i().setScreen(ScreenType.EMPLOYERASSOCIATEDPROPERTYEDIT);
	}	
	
}
