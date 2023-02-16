package com.etc.admin.ui.user;

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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewUserEditController {
	
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
	private Button lusrPasswordButton;
	@FXML
	private Button lusrSavePasswordButton;
	@FXML
	private Label lusrPasswordLabel;
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
	public void initialize() 
	{

		initControls();

		//load the user data		
		updateUserData();
	}

	private void initControls() 
	{
		//phone type
		ObservableList<PhoneType> phoneTypes = FXCollections.observableArrayList(PhoneType.values());
		lusrPhoneTypeChoice.setItems(phoneTypes);	

		//security level
		ObservableList<SecurityLevel> securityLevels = FXCollections.observableArrayList(SecurityLevel.values());
		lusrSecurityLevelChoice.setItems(securityLevels);	

		//email type
		ObservableList<EmailType> emailTypes = FXCollections.observableArrayList(EmailType.values());
		lusrEmailTypeChoice.setItems(emailTypes);	
		
		//hide the password
		lusrPasswordButton.setText("Change Password");
		lusrPasswordLabel.setVisible(false);
		lusrPasswordField.setVisible(false);
		lusrSavePasswordButton.setVisible(false);
	}
	
	private void updateUserData()
	{
		if (DataManager.i().mUser != null) {
			Utils.updateControl(lusrFirstNameField,DataManager.i().mUser.getFirstName());
			Utils.updateControl(lusrMiddleNameField,DataManager.i().mUser.getMiddleName());
			Utils.updateControl(lusrLastNameField,DataManager.i().mUser.getLastName());
			Utils.updateControl(lusrUserNameField,DataManager.i().mUser.getUsername());
			Utils.updateControl(lusrEmailField,DataManager.i().mUser.getEmail());				
			Utils.updateControl(lusrPhoneField,DataManager.i().mUser.getPhone());
			
			if (DataManager.i().mUser.getPhoneType() != null)
				lusrPhoneTypeChoice.getSelectionModel().select(DataManager.i().mUser.getPhoneType());
			else
				lusrPhoneTypeChoice.getSelectionModel().select(PhoneType.NONE);
				
				
			if (DataManager.i().mUser.getSecurityLevel() != null)
				lusrSecurityLevelChoice.getSelectionModel().select(DataManager.i().mUser.getSecurityLevel());
			else
				lusrSecurityLevelChoice.getSelectionModel().select(SecurityLevel.LEVEL0);
				

			if (DataManager.i().mUser.getEmailType() != null)
				lusrEmailTypeChoice.getSelectionModel().select(DataManager.i().mUser.getEmailType());
			else
				lusrEmailTypeChoice.getSelectionModel().select(EmailType.NONE);
							
			if (lusrLastLoginPicker.getValue() != null)
				DataManager.i().mUser.setLastLogin(Utils.getCalDate(lusrLastLoginPicker.getValue()));
			
			//core data read only
			Utils.updateControl(lusrCoreIdLabel,String.valueOf(DataManager.i().mUser.getId()));
			Utils.updateControl(lusrCoreActiveLabel,String.valueOf(DataManager.i().mUser.isActive()));
			Utils.updateControl(lusrCoreBODateLabel,DataManager.i().mUser.getBornOn());
			Utils.updateControl(lusrCoreLastUpdatedLabel,DataManager.i().mUser.getLastUpdated());
			
		}
	}
	
	private void updateUserRequest()
	{
/*		//logically we should have a current user selected
		if (DataManager.i().mUser != null) 
		{
			//reset the updateRequest object if it has been used
			DataManager.i().mUpdateUserRequest = null;			
			DataManager.i().mUpdateUserRequest = new UpdateUserRequest();

			//update the request object with the account
			DataManager.i().mUpdateUserRequest.setUser(DataManager.i().mUser);
			
			DataManager.i().mUpdateUserRequest.setFirstName(lusrFirstNameField.getText());
			DataManager.i().mUpdateUserRequest.setMiddleName(lusrMiddleNameField.getText());
			DataManager.i().mUpdateUserRequest.setLastName(lusrLastNameField.getText());
			DataManager.i().mUpdateUserRequest.setUsername(lusrUserNameField.getText());
			DataManager.i().mUpdateUserRequest.setEmail(lusrEmailField.getText());				
			DataManager.i().mUpdateUserRequest.setPhone(lusrPhoneField.getText());
			
			DataManager.i().mUpdateUserRequest.setPhoneType(lusrPhoneTypeChoice.getSelectionModel().getSelectedItem());
			DataManager.i().mUpdateUserRequest.setSecurityLevel(lusrSecurityLevelChoice.getSelectionModel().getSelectedItem());
			DataManager.i().mUpdateUserRequest.setEmailType(lusrEmailTypeChoice.getSelectionModel().getSelectedItem());
			
			if (lusrLastLoginPicker.getValue() != null)
				DataManager.i().mUser.setLastLogin(Utils.getCalDate(lusrLastLoginPicker.getValue()));
		}
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
	private void onChangePassword(ActionEvent event) {
		if (lusrPasswordField.isVisible() == false) {
			lusrPasswordButton.setText("Cancel Password");
			lusrPasswordLabel.setVisible(true);
			lusrPasswordField.setVisible(true);	
			lusrSavePasswordButton.setVisible(true);
		} else {
			lusrPasswordButton.setText("Change Password");
			lusrPasswordLabel.setVisible(false);
			lusrPasswordField.setVisible(false);	
			lusrSavePasswordButton.setVisible(false);
		}
	}	

	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.USER);
	}	
	
	@FXML
	private void onSavePassword(ActionEvent event) {
		//validate the password field
/*		if ( !Utils.validateLength(lusrPasswordField, 5)) return;
		
		//logically we should have a current user selected
		if (DataManager.i().mUser != null) {

			//reset the updateRequest object if it has been used
			DataManager.i().mUpdateUserRequest = null;			
			DataManager.i().mUpdateUserRequest = new UpdateUserRequest();

			//update the request object with the account
			DataManager.i().mUpdateUserRequest.setUser(DataManager.i().mUser);
			
			//update the password
			DataManager.i().mUpdateUserRequest.setPassword(lusrPasswordField.getText());
			
			//request the update
			//DataManager.i().updateUserPassword();
						
			//return to the user screen
			EtcAdmin.i().setScreen(ScreenType.USER);
		}		
*/	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updateUserRequest();
		
		//save it to the repository and the server
		//FIXME: DataManager.i().saveUser(DataManager.i().mUser);
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.USER, true);
	}	
	
}
