package com.etc.admin.ui.user;

import java.time.LocalDate;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.utils.types.EmailType;
import com.etc.utils.types.PhoneType;
import com.etc.utils.types.SecurityLevel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewUserAddController {
	
	@FXML
	private TextField lusrFirstNameField;
	@FXML
	private TextField lusrMiddleNameField;
	@FXML
	private TextField lusrLastNameField;
	@FXML
	private TextField lusrUserNameField;
	@FXML
	private ChoiceBox<EmailType> lusrEmailTypeChoice;
	@FXML
	private TextField lusrEmailField;
	@FXML
	private TextField lusrPhoneField;
	@FXML
	private ChoiceBox<PhoneType> lusrPhoneTypeChoice;
	@FXML
	private TextField lusrPasswordField;
	@FXML
	private DatePicker lusrLastLoginPicker;
	@FXML
	private ChoiceBox<SecurityLevel> lusrSecurityLevelChoice;
	@FXML
	private Label lusrCoreIdLabel;
	@FXML
	private Label lusrCoreActiveLabel;
	@FXML
	private Label lusrCoreBODateLabel;
	@FXML
	private Label lusrCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
	}

	private void initControls() {
		//phone type
		ObservableList<PhoneType> phoneTypes = FXCollections.observableArrayList(PhoneType.values());
		lusrPhoneTypeChoice.setItems(phoneTypes);	

		//security level
		ObservableList<SecurityLevel> securityLevels = FXCollections.observableArrayList(SecurityLevel.values());
		lusrSecurityLevelChoice.setItems(securityLevels);	

		//email type
		ObservableList<EmailType> emailTypes = FXCollections.observableArrayList(EmailType.values());
		lusrEmailTypeChoice.setItems(emailTypes);	
		
		//set a default date for last login
		lusrLastLoginPicker.setValue(LocalDate.now());
	}
	
	private void updateUser(){
/*			//reset the updateRequest object if it has been used
			DataManager.i().mAddUserRequest = null;			
			DataManager.i().mAddUserRequest = new AddUserRequest();
			AddUserRequest rq = DataManager.i().mAddUserRequest;

			//update the request object with the account
			rq.setUser(DataManager.i().mUser);
			
			rq.setFirstName(lusrFirstNameField.getText());
			rq.setMiddleName(lusrMiddleNameField.getText());
			rq.setLastName(lusrLastNameField.getText());
			rq.setUsername(lusrUserNameField.getText());
			rq.setEmail(lusrEmailField.getText());				
			rq.setPhone(lusrPhoneField.getText());
			
			rq.setPhoneType(lusrPhoneTypeChoice.getSelectionModel().getSelectedItem());
			rq.setSecurityLevel(lusrSecurityLevelChoice.getSelectionModel().getSelectedItem());
			rq.setEmailType(lusrEmailTypeChoice.getSelectionModel().getSelectedItem());
			
			//need to encrypt the password
			rq.setPassword(lusrPasswordField.getText());

			if (lusrLastLoginPicker.getValue() != null)
				rq.setLastLogin(Utils.getCalDate(lusrLastLoginPicker.getValue()));
*/	}
	
	private boolean validateData() {
		boolean bReturn = true;

		//check for required data
		if ( !Utils.validate(lusrFirstNameField)) bReturn = false;
		if ( !Utils.validate(lusrLastNameField)) bReturn = false;
		if ( !Utils.validate(lusrUserNameField)) bReturn = false;

		//phone
		if (!Utils.validatePhoneTextField(lusrPhoneField)) bReturn = false;
		
		//email
		if (!Utils.validateEmailTextField(lusrEmailField)) bReturn = false;

		//validate the password
		if (lusrPasswordField.isVisible())
			if (!Utils.validatePasswordTextField(lusrPasswordField)) bReturn = false;

		//phone type none when there is a phone type
		if (lusrPhoneTypeChoice.getSelectionModel().getSelectedItem() == PhoneType.NONE && lusrPhoneField.getText().length() > 0) {
			lusrPhoneField.setStyle("-fx-background-color: red");
			bReturn = false;
		}
		
		//email type none when there is an email type
		if (lusrEmailTypeChoice.getSelectionModel().getSelectedItem() == EmailType.NONE && lusrEmailField.getText().length() > 0) {
			lusrEmailField.setStyle("-fx-background-color: red");
			bReturn = false;
		}
		
		return bReturn;
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.USER);
	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updateUser();
		
		//send it to the server for creation
	//	DataManager.i().addUser(DataManager.i().mAddUserRequest);
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.USER, true);
	}	
	
}
