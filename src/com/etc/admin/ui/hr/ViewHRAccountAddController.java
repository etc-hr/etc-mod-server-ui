package com.etc.admin.ui.hr;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.AddressGrid;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.rqs.AccountRequest;
import com.etc.utils.types.EmailType;
import com.etc.utils.types.PhoneType;
import com.etc.utils.types.TimezoneType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ViewHRAccountAddController 
{
	@FXML
	private GridPane acctMailGrid;
	@FXML
	private GridPane acctBillGrid;
	@FXML
	private Label acctHeaderLabel;
	@FXML
	private Label acctTopLabel;
	@FXML
	private TextField acctNameField;
	@FXML
	private TextField acctPhoneField;
	@FXML
	private TextField acctEmailField;
	@FXML
	private TextField acctDescriptionField;
	@FXML
	private TextField fax;
	@FXML
	private TextField website;
	@FXML
	private ChoiceBox<PhoneType> acctPhoneTypeChoice;
	@FXML
	private ChoiceBox<EmailType> acctEmailTypeChoice;
	@FXML
	private ChoiceBox<TimezoneType> acctTimezoneChoice;
	@FXML
	private Button acctSaveButton;
	
	//address grids
	AddressGrid mailAddress = new AddressGrid();
	AddressGrid billAddress = new AddressGrid();
	
	 // initialize is called when the FXML is loaded
	@FXML
	public void initialize() 
	{
		initControls();
	}
	
	private void initControls() 
	{
		mailAddress.setGrid(acctMailGrid, "Mail Address");
		mailAddress.setEditMode(true);
		billAddress.setGrid(acctBillGrid, "Bill Address");
		billAddress.setEditMode(true);
		
		//phone type
		ObservableList<PhoneType> phoneTypes = FXCollections.observableArrayList(PhoneType.values());
		acctPhoneTypeChoice.setItems(phoneTypes);
		
		//email type
		ObservableList<EmailType> emailTypes = FXCollections.observableArrayList(EmailType.values());
		acctEmailTypeChoice.setItems(emailTypes);

		//timezone type
		ObservableList<TimezoneType> timezoneTypes = FXCollections.observableArrayList(TimezoneType.values());
		acctTimezoneChoice.setItems(timezoneTypes);
	}
	
	private void saveAccount()
	{
		// create the account
		Account account = new Account();
		//create the updateRequest object
		AccountRequest request = new AccountRequest();
		
		// General data from the interface
		account.setName(acctNameField.getText());
		account.setDescription(acctDescriptionField.getText());
		account.setPhone(acctPhoneField.getText());
		account.setPhoneType(acctPhoneTypeChoice.getValue());
		account.setFax(fax.getText());
		account.setWebsite(website.getText());
		account.setEmail(acctEmailField.getText());			
		account.setEmailType(acctEmailTypeChoice.getValue());
		account.setTimezone(acctTimezoneChoice.getValue()); 
		
		// UPDATE ADDRESS
		account.setMailAddress(mailAddress.getUpdatedAddress());
		account.setBillAddress(billAddress.getUpdatedAddress());
		
		// set the org to the current user
		account.setOrganizationId(DataManager.i().mLocalUser.getOrganizationId());
		// set the request entity
		request.setEntity(account);

		// update the server
		try {
			DataManager.i().mAccount = AdminPersistenceManager.getInstance().addOrUpdate(request);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private boolean validateData()
	{
		boolean bReturn = true;

		//check for required data
		if(!Utils.validate(acctNameField)) bReturn = false;
		if(mailAddress.validateData() == false) bReturn = false;
		if(billAddress.validateData() == false) bReturn = false;
		//phone
		if(!Utils.validatePhoneTextField(acctPhoneField)) bReturn = false;		
		//email
		if(!Utils.validateEmailTextField(acctEmailField)) bReturn = false;

		return bReturn;
	}
	
	@FXML
	private void onSave(ActionEvent event) 
	{
		//validate everything
		if(!validateData())
			return;
		
		// save the account
		saveAccount();
		// close the popup
		exitPopup();
	}	
	
	@FXML
	private void onCancel(ActionEvent event) 
	{
		exitPopup();
	}	
	
	private void exitPopup()
	{
		Stage stage = (Stage) acctNameField.getScene().getWindow();
		stage.close();
	}

}
