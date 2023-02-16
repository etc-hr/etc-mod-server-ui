package com.etc.admin.ui.associatedproperty;

import java.text.SimpleDateFormat;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils.ScreenType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewAssociatedPropertyEditController {
	
	@FXML
	private Label apropTitleLabel;
	@FXML
	private TextField apropNameField;
	@FXML
	private TextField apropValueField;
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
			Utils.updateControl(apropNameField,DataManager.i().mAssociatedProperty.getName());
			Utils.updateControl(apropValueField,DataManager.i().mAssociatedProperty.getValue());
				
			// core data
			Utils.updateControl(apropCoreIdLabel,String.valueOf(DataManager.i().mAssociatedProperty.getId()));
			Utils.updateControl(apropCoreActiveLabel,String.valueOf(DataManager.i().mAssociatedProperty.isActive()));
			Utils.setControlDate(apropCoreBODateLabel,DataManager.i().mAssociatedProperty.getBornOn());
			Utils.setControlDate(apropCoreLastUpdatedLabel,DataManager.i().mAssociatedProperty.getLastUpdated());
			
		}
		*/
	}

	private void updateAssociatedProperty(){
/*		
		if (DataManager.i().mAssociatedProperty != null) {
			DataManager.i().mAssociatedProperty.setName(apropNameField.getText());
			DataManager.i().mAssociatedProperty.setValue(apropValueField.getText());
		}
		*/
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		if (DataManager.i().mAssociatedPropertyType == 0)
			EtcAdmin.i().setScreen(ScreenType.ACCOUNTASSOCIATEDPROPERTY);
		else
			EtcAdmin.i().setScreen(ScreenType.EMPLOYERASSOCIATEDPROPERTY);
	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		//update our object
		updateAssociatedProperty();
		
		//save it to the repository and the server
		//if (DataManager.i().mAssociatedPropertyType == 0)
			//FIXME: DataManager.i().saveCurrentAccountRequest();
		//else
			//FIXME: DataManager.i().saveCurrentEmployerRequest();
			
		//and load the regular ui
		if (DataManager.i().mAssociatedPropertyType == 0)
			EtcAdmin.i().setScreen(ScreenType.ACCOUNTASSOCIATEDPROPERTY);
		else
			EtcAdmin.i().setScreen(ScreenType.EMPLOYERASSOCIATEDPROPERTY);
	}	
	
}
