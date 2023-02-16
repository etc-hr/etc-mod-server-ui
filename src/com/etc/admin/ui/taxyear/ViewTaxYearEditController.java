package com.etc.admin.ui.taxyear;

import java.text.SimpleDateFormat;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewTaxYearEditController {
	
	@FXML
	private Label txyrTitleLabel;
	@FXML
	private TextField txyrYearField;
	@FXML
	private CheckBox txyrClosedCheckBox;
	@FXML
	private DatePicker txyrClosedOnPicker;
	@FXML
	private ChoiceBox<String> txyrClosedByChoice;
	@FXML
	private Label txyrCoreIdLabel;
	@FXML
	private Label txyrCoreActiveLabel;
	@FXML
	private Label txyrCoreBODateLabel;
	@FXML
	private Label txyrCoreLastUpdatedLabel;
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		updateTaxYearData();
	}
	
	private void initControls() {
		//users - might need to limit this to account users
		if (DataManager.i().mUsers != null) {
			ObservableList<String> users = FXCollections.observableArrayList();
			for (int i = 0; i < DataManager.i().mUsers.size(); i++) {
				users.add(DataManager.i().mUsers.get(i).getFirstName() +  
						" " + DataManager.i().mUsers.get(i).getLastName());
			}
			txyrClosedByChoice.setItems(users);
		}	
	}

	private void updateTaxYearData(){
		
		if (DataManager.i().mTaxYear != null) {
			String sName = DataManager.i().mEmployer.getName() + " Tax Year";
				
			Utils.updateControl(txyrTitleLabel,sName);
			Utils.updateControl(txyrClosedCheckBox,DataManager.i().mTaxYear.isClosed());
			
			if (DataManager.i().mTaxYear.getClosedBy() != null) 
				txyrClosedByChoice.getSelectionModel().select(DataManager.i().mTaxYear.getClosedBy().getFirstName() 
					+ " " + DataManager.i().mTaxYear.getClosedBy().getLastName());
			
			Utils.updateControl(txyrYearField,String.valueOf(DataManager.i().mTaxYear.getYear()));
			Utils.updateControl(txyrClosedOnPicker,DataManager.i().mTaxYear.getClosedOn());

			// core data
			Utils.updateControl(txyrCoreIdLabel,String.valueOf(DataManager.i().mTaxYear.getId()));
			Utils.updateControl(txyrCoreActiveLabel,String.valueOf(DataManager.i().mTaxYear.isActive()));
			Utils.updateControl(txyrCoreBODateLabel,DataManager.i().mTaxYear.getBornOn());
			Utils.updateControl(txyrCoreLastUpdatedLabel,DataManager.i().mTaxYear.getLastUpdated());
		}
	}
	
	private void updateTaxYear(){
		
		if (DataManager.i().mTaxYear != null) {
			DataManager.i().mTaxYear.setClosed(txyrClosedCheckBox.isSelected());
			DataManager.i().mTaxYear.setYear(Integer.valueOf(txyrYearField.getText()));
			if (txyrClosedOnPicker.getValue() != null)
				DataManager.i().mTaxYear.setClosedOn(Utils.getCalDate(txyrClosedOnPicker.getValue()));
			
			// wait for the new user type - account is user2, taxyear is user. 
			//if (txyrClosedByChoice.getSelectionModel().getSelectedIndex() > -1)
			//	DataManager.i().mTaxYear.setClosedBy(DataManager.i().mAccount.getUsers().get(txyrClosedByChoice.getSelectionModel().getSelectedIndex()));
			
		}
	}
	
	private boolean validateData() {
		boolean bReturn = true;
		
		//check for required data
		if ( !Utils.validate(txyrYearField)) bReturn = false;

		// int range check
		if (!Utils.validateIntRangeTextField(txyrYearField, 1980, 2050)) bReturn = false;
		
		return bReturn;
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.TAXYEAR);
	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updateTaxYear();
		
		//save it to the repository and the server
		//FIXME: DataManager.i().saveCurrentEmployerRequest();
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.TAXYEAR, true);
	}	
			
}
