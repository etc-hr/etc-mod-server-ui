package com.etc.admin.ui.plansponsor;

import com.etc.admin.EtcAdmin;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.utils.types.CountryType;
import com.etc.utils.types.EmailType;
import com.etc.utils.types.PhoneType;
import com.etc.utils.types.TimezoneType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewPlanSponsorEditController {
		
	@FXML
	private TextField spnsNameField;
	@FXML
	private TextField spnsDescriptionField;
	@FXML
	private TextField spnsPhoneField;
	@FXML
	private ChoiceBox<PhoneType> spnsPhoneTypeChoice;
	@FXML
	private ChoiceBox<TimezoneType> spnsTimezoneChoice;
	@FXML
	private TextField spnsEmailField;
	@FXML
	private ChoiceBox<EmailType> spnsEmailTypeChoice;
	@FXML
	private TextField spnsStreetField;
	@FXML
	private TextField spnsUnitField;
	@FXML
	private TextField spnsCityField;
	@FXML
	private TextField spnsStateField;
	@FXML
	private TextField spnsZipField;
	@FXML
	private TextField spnsZipPlus4Field;
	@FXML
	private ChoiceBox<CountryType>  spnsCountryChoice;
	@FXML
	private TextField spnsQuarterField;
	@FXML
	private TextField spnsDepartmentField;
	@FXML
	private TextField spnsProvinceField;
	@FXML
	private CheckBox spnsVerifiedCheckBox;
	
	//core data
	@FXML
	private Label spnsCoreIdLabel;
	@FXML
	private Label spnsCoreActiveLabel;
	@FXML
	private Label spnsCoreBODateLabel;
	@FXML
	private Label spnsCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		//init the controls
		initControls();
		
		//and update the screen data
		updateProviderData();
	}
	
	private void initControls() {
		//phone type
		ObservableList<PhoneType> phoneTypes = FXCollections.observableArrayList(PhoneType.values());
		spnsPhoneTypeChoice.setItems(phoneTypes);
		
		//email type
		ObservableList<EmailType> emailTypes = FXCollections.observableArrayList(EmailType.values());
		spnsEmailTypeChoice.setItems(emailTypes);

		//timezone type
		ObservableList<TimezoneType> timezoneTypes = FXCollections.observableArrayList(TimezoneType.values());
		spnsTimezoneChoice.setItems(timezoneTypes);

		//country type
		ObservableList<CountryType> countryTypes = FXCollections.observableArrayList(CountryType.values());
		spnsCountryChoice.setItems(countryTypes);
	}

	
	public void updateProviderData() {
	/*	
		if (DataManager.i().mPlanProviders != null) {

			//textfields
			Utils.updateControl(spnsNameField,DataManager.i().mPlanProvider.getName());
			Utils.updateControl(spnsDescriptionField,DataManager.i().mPlanProvider.getDescription());
			Utils.updateControl(spnsPhoneField,DataManager.i().mPlanProvider.getPhone());
			Utils.updateControl(spnsEmailField,DataManager.i().mPlanProvider.getEmail());
			Utils.updateControl(spnsStreetField,DataManager.i().mPlanProvider.getMailStreet());
			Utils.updateControl(spnsUnitField,DataManager.i().mPlanProvider.getMailUnit());
			Utils.updateControl(spnsCityField,DataManager.i().mPlanProvider.getMailCity());
			Utils.updateControl(spnsStateField,DataManager.i().mPlanProvider.getMailState());
			Utils.updateControl(spnsZipField,DataManager.i().mPlanProvider.getMailZip());
			Utils.updateControl(spnsZipPlus4Field,DataManager.i().mPlanProvider.getMailZip4());
			Utils.updateControl(spnsQuarterField,DataManager.i().mPlanProvider.getMailQuarter());
			Utils.updateControl(spnsDepartmentField,DataManager.i().mPlanProvider.getMailDepartment());
			Utils.updateControl(spnsProvinceField,DataManager.i().mPlanProvider.getMailProvince());
			
			//choiceboxes
			if (DataManager.i().mPlanProvider.getPhoneType() != null) 
				spnsPhoneTypeChoice.getSelectionModel().select(DataManager.i().mPlanProvider.getPhoneType());
			
			if (DataManager.i().mPlanProvider.getEmailType() != null) 
				spnsEmailTypeChoice.getSelectionModel().select( DataManager.i().mPlanProvider.getEmailType());

			if (DataManager.i().mPlanProvider.getTimezone() != null) 
				spnsTimezoneChoice.getSelectionModel().select( DataManager.i().mPlanProvider.getTimezone());

			if (DataManager.i().mPlanProvider.getMailCountry() != null) 
				spnsCountryChoice.getSelectionModel().select( DataManager.i().mPlanProvider.getMailCountry());
			
			//checkbox
			Utils.updateControl(spnsVerifiedCheckBox,DataManager.i().mPlanProvider.isMailVerified());
			
			//core data read only
			Utils.updateControl(spnsCoreIdLabel,String.valueOf(DataManager.i().mPlanProvider.getId()));
			Utils.updateControl(spnsCoreActiveLabel,String.valueOf(DataManager.i().mPlanProvider.isActive()));
			Utils.updateControl(spnsCoreBODateLabel,DataManager.i().mPlanProvider.getBornOn());
			Utils.updateControl(spnsCoreLastUpdatedLabel,DataManager.i().mPlanProvider.getLastUpdated());
		} */
	}
	
	public void updateProvider() {
		/*
		if (DataManager.i().mPlanProvider != null) {
			DataManager.i().mPlanProvider.setName(spnsNameField.getText());
			DataManager.i().mPlanProvider.setDescription(spnsDescriptionField.getText());

			DataManager.i().mPlanProvider.setPhone(spnsPhoneField.getText());
			DataManager.i().mPlanProvider.setEmail(spnsEmailField.getText());
			DataManager.i().mPlanProvider.setMailStreet(spnsStreetField.getText());
			DataManager.i().mPlanProvider.setMailUnit(spnsUnitField.getText());
			DataManager.i().mPlanProvider.setMailCity(spnsCityField.getText());
			DataManager.i().mPlanProvider.setMailState(spnsStateField.getText());
			DataManager.i().mPlanProvider.setMailZip(spnsZipField.getText());
			DataManager.i().mPlanProvider.setMailZip4(spnsZipPlus4Field.getText());
			DataManager.i().mPlanProvider.setMailQuarter(spnsQuarterField.getText());
			DataManager.i().mPlanProvider.setMailDepartment(spnsDepartmentField.getText());
			DataManager.i().mPlanProvider.setMailProvince(spnsProvinceField.getText());
			
			//choiceboxes
			DataManager.i().mPlanProvider.setPhoneType(spnsPhoneTypeChoice.getValue());
			DataManager.i().mPlanProvider.setEmailType(spnsEmailTypeChoice.getValue());
			DataManager.i().mPlanProvider.setTimezone(spnsTimezoneChoice.getValue());
			DataManager.i().mPlanProvider.setMailCountry(spnsCountryChoice.getValue());
			
			//checkbox
			DataManager.i().mPlanProvider.setMailVerified(spnsVerifiedCheckBox.isSelected());
		} */
	}
		
	private boolean validateData() {
		boolean bReturn = true;
	
		//nothing to validate at this time
		
		return bReturn;
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PLANPROVIDER);
	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updateProvider();
		
		//save it to the repository and the server
		//DataManager.i().savePlanProvider(DataManager.i().mPlanProvider);
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.PLANPROVIDER, true);
	}	
	
}
