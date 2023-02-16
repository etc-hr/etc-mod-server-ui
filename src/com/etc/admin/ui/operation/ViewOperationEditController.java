package com.etc.admin.ui.operation;

import com.etc.admin.EtcAdmin;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewOperationEditController {
	
	@FXML
	private TextField oprNameField;
	@FXML
	private TextField oprDescriptionField;
	@FXML
	private CheckBox oprSystemCheckBox;
	@FXML
	private CheckBox oprRequiredCheckBox;
	@FXML
	private TextField oprPriorityField;
//	@FXML
//	private ChoiceBox<OpSecSpec> oprOpSecSpecChoice;
	@FXML
	private TextField oprModuleNameField;
	@FXML
	private TextField oprModuleDescriptionField;
	@FXML
	private Label oprCoreIdLabel;
	@FXML
	private Label oprCoreActiveLabel;
	@FXML
	private Label oprCoreBODateLabel;
	@FXML
	private Label oprCoreLastUpdatedLabel;
	@FXML
	private Label oprModuleCoreIdLabel;
	@FXML
	private Label oprModuleCoreActiveLabel;
	@FXML
	private Label oprModuleCoreBODateLabel;
	@FXML
	private Label oprModuleCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		updateOperationData();
	}
	
	private void initControls() {
/*		//phone type
		ObservableList<OpSecSpec> opSecSpecTypes = FXCollections.observableArrayList(OpSecSpec.values());
		oprOpSecSpecChoice.setItems(opSecSpecTypes);
*/		
	}
	
	private void updateOperationData(){
/*		
		if (EtcAdmin.mOperation != null) {
				
			Utils.updateControl(oprNameField,EtcAdmin.mOperation.getName());
			Utils.updateControl(oprDescriptionField,EtcAdmin.mOperation.getDescription());
			Utils.updateControl(oprPriorityField,String.valueOf(EtcAdmin.mOperation.getPriority()));
			
			if (EtcAdmin.mOperation.getOpSecSpec() != null)
				oprOpSecSpecChoice.getSelectionModel().select(EtcAdmin.mOperation.getOpSecSpec());
			
			Utils.updateControl(oprSystemCheckBox,EtcAdmin.mOperation.isSystem());
			Utils.updateControl(oprRequiredCheckBox,EtcAdmin.mOperation.isRequired());

			// core data
			Utils.updateControl(oprCoreIdLabel,String.valueOf(EtcAdmin.mOperation.getId()));
			Utils.updateControl(oprCoreActiveLabel,String.valueOf(EtcAdmin.mOperation.isActive()));
			Utils.setControlDate(oprCoreBODateLabel,EtcAdmin.mOperation.getBornOn());
			Utils.setControlDate(oprCoreLastUpdatedLabel,EtcAdmin.mOperation.getLastUpdated());
			
			if (EtcAdmin.mOperation.getModule() != null) {
				Utils.updateControl(oprModuleNameField,EtcAdmin.mOperation.getModule().getName());
				Utils.updateControl(oprModuleDescriptionField,EtcAdmin.mOperation.getModule().getDescription());
				Utils.updateControl(oprModuleCoreIdLabel,String.valueOf(EtcAdmin.mOperation.getModule().getId()));
				Utils.updateControl(oprModuleCoreActiveLabel,String.valueOf(EtcAdmin.mOperation.getModule().isActive()));
				Utils.setControlDate(oprModuleCoreBODateLabel,EtcAdmin.mOperation.getModule().getBornOn());
				Utils.setControlDate(oprModuleCoreLastUpdatedLabel,EtcAdmin.mOperation.getModule().getLastUpdated());
			} else {
				oprModuleNameField.setText("");
				oprModuleDescriptionField.setText("");
				oprModuleCoreIdLabel.setText("");
				oprModuleCoreActiveLabel.setText("");
				oprModuleCoreBODateLabel.setText("");
				oprModuleCoreLastUpdatedLabel.setText("");				
			}
		}
		*/
	}
	
	private void updateOperation(){
		/*
		if (EtcAdmin.mOperation != null) {
				
			EtcAdmin.mOperation.setName(oprNameField.getText());
			EtcAdmin.mOperation.setDescription(oprDescriptionField.getText());
			EtcAdmin.mOperation.setPriority(Integer.valueOf(oprPriorityField.getText()));
			EtcAdmin.mOperation.setOpSecSpec(oprOpSecSpecChoice.getSelectionModel().getSelectedItem());
			
			EtcAdmin.mOperation.setSystem(oprSystemCheckBox.isSelected());
			EtcAdmin.mOperation.setRequired(oprRequiredCheckBox.isSelected());
			
			if (EtcAdmin.mOperation.getModule() != null) {
				EtcAdmin.mOperation.getModule().setName(oprModuleNameField.getText());
				EtcAdmin.mOperation.getModule().setDescription(oprModuleDescriptionField.getText());
			} 
		}
		*/
	}

	private boolean validateData() {
		boolean bReturn = true;

		if ( !Utils.validateIntTextField(oprPriorityField)) bReturn = false;
		
		return bReturn;
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.OPERATION);
	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updateOperation();
		
		//save it to the repository and the server
		//EtcAdmin.i().saveOperation(EtcAdmin.i().mOperation);
		
		//and load the regular ui
		//EtcAdmin.setScreen(ScreenType.OPERATION);
	}	
	
	
}
