package com.etc.admin.ui.taxmonth;

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

public class ViewTaxMonthEditController {
	
	@FXML
	private Label txmnTitleLabel;
	@FXML
	private ChoiceBox<String> txmnMonthChoice;
	@FXML
	private CheckBox txmnClosedCheckBox;
	@FXML
	private DatePicker txmnClosedOnPicker;
	@FXML
	private ChoiceBox<String> txmnClosedByChoice;
	@FXML
	private CheckBox txmnApprovedCheckBox;
	@FXML
	private DatePicker txmnApprovedOnPicker;
	@FXML
	private ChoiceBox<String> txmnApprovedByChoice;
	@FXML
	private Label txmnCoreIdLabel;
	@FXML
	private Label txmnCoreActiveLabel;
	@FXML
	private Label txmnCoreBODateLabel;
	@FXML
	private Label txmnCoreLastUpdatedLabel;
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		updateTaxMonthData();
	}

	private void initControls() {

		//users
		if (DataManager.i().mUsers != null) {
			ObservableList<String> users = FXCollections.observableArrayList();
			for (int i = 0; i < DataManager.i().mUsers.size(); i++) {
				users.add(DataManager.i().mUsers.get(i).getFirstName() +  
						" " + DataManager.i().mUsers.get(i).getLastName());
			}
			txmnClosedByChoice.setItems(users);
			txmnApprovedByChoice.setItems(users);
		}
		
		//tax months
		ObservableList<String> months = FXCollections.observableArrayList();
		for (int i = 1; i < 13; i++)
			months.add(String.valueOf(i));
		txmnMonthChoice.setItems(months);
		
	}
	
	private void updateTaxMonthData(){
		
		if (DataManager.i().mTaxMonth != null) {
			String sName = DataManager.i().mEmployer.getName() + " Tax Year " + String.valueOf(DataManager.i().mTaxYear.getYear()) + " Tax Month";
				
			Utils.updateControl(txmnTitleLabel,sName);
			txmnMonthChoice.getSelectionModel().select(String.valueOf(DataManager.i().mTaxMonth.getMonth()));
			
			//closed
			Utils.updateControl(txmnClosedCheckBox,DataManager.i().mTaxMonth.isClosed());
			
			if (DataManager.i().mTaxMonth.getClosedBy() != null) 
				txmnClosedByChoice.getSelectionModel().select(DataManager.i().mTaxMonth.getClosedBy().getFirstName() 
					+ " " + DataManager.i().mTaxMonth.getClosedBy().getLastName());
			
			Utils.updateControl(txmnClosedOnPicker,DataManager.i().mTaxMonth.getClosedOn());
			
			//pay periods approved
			Utils.updateControl(txmnApprovedCheckBox,DataManager.i().mTaxMonth.isPayPeriodsApproved());
			
			if (DataManager.i().mTaxMonth.getPayPeriodsApprovedBy() != null) 
				txmnApprovedByChoice.getSelectionModel().select(DataManager.i().mTaxMonth.getPayPeriodsApprovedBy().getFirstName() 
					+ " " + DataManager.i().mTaxMonth.getPayPeriodsApprovedBy().getLastName());
			
			Utils.updateControl(txmnApprovedOnPicker,DataManager.i().mTaxMonth.getPayPeriodsApprovedOn());
			

			// core data
			Utils.updateControl(txmnCoreIdLabel,String.valueOf(DataManager.i().mTaxMonth.getId()));
			Utils.updateControl(txmnCoreActiveLabel,String.valueOf(DataManager.i().mTaxMonth.isActive()));
			Utils.updateControl(txmnCoreBODateLabel,DataManager.i().mTaxMonth.getBornOn());
			Utils.updateControl(txmnCoreLastUpdatedLabel,DataManager.i().mTaxMonth.getLastUpdated());
		}
	}	
	
	private void updateTaxMonth(){
		
		// this should not be null
		if (DataManager.i().mTaxMonth != null) {
			DataManager.i().mTaxMonth.setMonth(Integer.valueOf(txmnMonthChoice.getSelectionModel().getSelectedItem()));
			
			//closed
			DataManager.i().mTaxMonth.setClosed(txmnClosedCheckBox.isSelected());
			
			// coming back to user
			//if (DataManager.i().mTaxMonth.getClosedBy() != null) 
			//	txmnClosedByChoice.getSelectionModel().select(DataManager.i().mTaxMonth.getClosedBy().getFirstName() 
			//		+ " " + DataManager.i().mTaxMonth.getClosedBy().getLastName());
			
			if (txmnClosedOnPicker.getValue() != null)
				DataManager.i().mTaxMonth.setClosedOn(Utils.getCalDate(txmnClosedOnPicker.getValue()));
			
			//pay periods approved
			DataManager.i().mTaxMonth.setPayPeriodsApproved(txmnApprovedCheckBox.isSelected());
			
			// coming back to user
			//if (DataManager.i().mTaxMonth.getPayPeriodsApprovedBy() != null) 
			//	txmnApprovedByChoice.getSelectionModel().select(DataManager.i().mTaxMonth.getPayPeriodsApprovedBy().getFirstName() 
			//		+ " " + DataManager.i().mTaxMonth.getPayPeriodsApprovedBy().getLastName());
			
			if (txmnApprovedOnPicker.getValue() != null)
				DataManager.i().mTaxMonth.setPayPeriodsApprovedOn(Utils.getCalDate(txmnApprovedOnPicker.getValue()));
		}
	}	
	
	private boolean validateData() {
		boolean bReturn = true;

		// add data to be validated here
		
		return bReturn;
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.TAXMONTH);
	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updateTaxMonth();
		
		//save it to the repository and the server
		//FIXME: DataManager.i().saveCurrentEmployerRequest();
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.TAXMONTH, true);
	}	
	
}
