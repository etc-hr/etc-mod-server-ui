package com.etc.admin.ui.contact;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.AddressGrid;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.AccountContact;
import com.etc.corvetto.entities.Contact;
import com.etc.corvetto.entities.EmployerContact;
import com.etc.corvetto.entities.PostalAddress;
import com.etc.corvetto.rqs.AccountContactRequest;
import com.etc.corvetto.rqs.ContactRequest;
import com.etc.corvetto.rqs.EmployerContactRequest;
import com.etc.corvetto.rqs.PostalAddressRequest;
import com.etc.corvetto.utils.types.ContactType;
import com.etc.entities.CoreData;
import com.etc.utils.types.CountryType;
import com.etc.utils.types.EmailType;
import com.etc.utils.types.PhoneType;
import com.etc.utils.types.TimezoneType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewContactController 
{
	@FXML
	private GridPane contAddrGrid;
	@FXML
	private GridPane secInputLabelGrid;
	@FXML
	private GridPane secInputLabelGrid1;
	@FXML
	private TextField conFirstNameLabel;
	@FXML
	private TextField conMiddleNameLabel;
	@FXML
	private TextField conLastNameLabel;
	@FXML
	private TextField conPrimaryPhoneLabel;
	@FXML
	private TextField primPhnExt;
	@FXML
	private ComboBox<PhoneType> conPrimaryPhoneTypeCombo;
	@FXML
	private TextField conSecondaryPhoneLabel;
	@FXML
	private TextField secPhnExt;
	@FXML
	private ComboBox<PhoneType> conSecondaryPhoneTypeCombo;
	@FXML
	private TextField emPhn;
	@FXML
	private TextField emPhnExt;
	@FXML
	private ComboBox<PhoneType> emPhnTypeCombo;
	@FXML
	private ComboBox<TimezoneType> conTimezoneCombo;
	@FXML
	private TextField conEmailLabel;
	@FXML
	private ComboBox<EmailType> conEmailTypeCombo;
	@FXML
	private TextField jobTitle;
	@FXML
	private ComboBox<ContactType> contactTypeCombo;
	@FXML
	private Label conCoreIdLabel;
	@FXML
	private Label conCoreActiveLabel;
	@FXML
	private Label conCoreBODateLabel;
	@FXML
	private Label conCoreLastUpdatedLabel;
	@FXML
	private Label conContactCoreIdLabel;
	@FXML
	private Label conContactCoreActiveLabel;
	@FXML
	private Label conContactCoreBODateLabel;
	@FXML
	private Label conContactCoreLastUpdatedLabel;
	@FXML
	private Label contFillLabel;
	@FXML
	private Button editButton;
	@FXML
	private Button saveButton;

	// address object
	AddressGrid addressGrid = new AddressGrid();
	boolean editMode = false;
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	CountryType countryType = CountryType.US;

	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() 
	{
		initControls();
		updateContactData();
	}
	
	public void initControls()
	{
		saveButton.setVisible(false);
		setFieldEdits(false);

		//phone type
		ObservableList<PhoneType> phoneTypes = FXCollections.observableArrayList(PhoneType.values());
		conPrimaryPhoneTypeCombo.setItems(phoneTypes);
		conSecondaryPhoneTypeCombo.setItems(phoneTypes);
		emPhnTypeCombo.setItems(phoneTypes);
		
		//email type
		ObservableList<EmailType> emailTypes = FXCollections.observableArrayList(EmailType.values());
		conEmailTypeCombo.setItems(emailTypes);

		//timezone type
		ObservableList<TimezoneType> timezoneTypes = FXCollections.observableArrayList(TimezoneType.values());
		conTimezoneCombo.setItems(timezoneTypes);

		//contact type
		ObservableList<ContactType> contactTypes = FXCollections.observableArrayList(ContactType.values());
		contactTypeCombo.setItems(contactTypes);
	}

	private void updateContactData()
	{
		Contact contact = null;
		
		//determine which type of contact we are editing
		if(DataManager.i().mAccountContact != null || DataManager.i().mEmployerContact != null)
		{
			if(DataManager.i().mContactType == 0)
				contact = DataManager.i().mAccountContact.getContact();
			else
				contact = DataManager.i().mEmployerContact.getContact();
		}

		if(contact != null)
		{
			Utils.updateControl(conFirstNameLabel,contact.getFirstName());
			Utils.updateControl(conMiddleNameLabel,contact.getMiddleName());
			Utils.updateControl(conLastNameLabel,contact.getLastName());
			Utils.updateControl(conPrimaryPhoneLabel,contact.getPhone());
			Utils.updateControl(primPhnExt,contact.getPhoneExt());
			Utils.updateControl(conSecondaryPhoneLabel,contact.getSecondPhone());
			Utils.updateControl(secPhnExt,contact.getSecondPhoneExt());
			Utils.updateControl(emPhn,contact.getEmergencyPhone());
			Utils.updateControl(emPhnExt,contact.getEmergencyPhoneExt());
			Utils.updateControl(conEmailLabel,contact.getEmail());
			Utils.updateControl(jobTitle,contact.getJobTitle());
			
			// Phone Type
			if(contact.getPhoneType() != null) 
				conPrimaryPhoneTypeCombo.getSelectionModel().select(contact.getPhoneType());
			else
				conPrimaryPhoneTypeCombo.getSelectionModel().select(null);	
			
			if(contact.getSecondPhoneType() != null) 
				conSecondaryPhoneTypeCombo.getSelectionModel().select(contact.getPhoneType());
			else
				conSecondaryPhoneTypeCombo.getSelectionModel().select(null);	
			
			if(contact.getEmergencyPhoneType() != null) 
				emPhnTypeCombo.getSelectionModel().select(contact.getEmergencyPhoneType());
			else
				emPhnTypeCombo.getSelectionModel().select(null);	
			
			// Email Type
			if(contact.getEmailType() != null) 
				conEmailTypeCombo.getSelectionModel().select(contact.getEmailType());
			else
				conEmailTypeCombo.getSelectionModel().select(null);	
			
			// Timezone Type
			if(contact.getTimezone() != null) 
				conTimezoneCombo.getSelectionModel().select(contact.getTimezone());
			else
				conTimezoneCombo.getSelectionModel().select(null);	

			// MAIL ADDRESS
			addressGrid.createAddress(contAddrGrid, contact.getMailAddress(), "Address");
			
			//core data read only
			if(DataManager.i().mContactType == 0) 
			{
				Utils.updateControl(conCoreIdLabel,String.valueOf(DataManager.i().mAccountContact.getId()));
				Utils.updateControl(conCoreActiveLabel,String.valueOf(DataManager.i().mAccountContact.isActive()));
				Utils.updateControl(conCoreBODateLabel,DataManager.i().mAccountContact.getBornOn());
				Utils.updateControl(conCoreLastUpdatedLabel,DataManager.i().mAccountContact.getLastUpdated());
				
				// Contact Type
				if(DataManager.i().mAccountContact.getContactType() != null) 
					contactTypeCombo.getSelectionModel().select(DataManager.i().mAccountContact.getContactType());
				else
					contactTypeCombo.getSelectionModel().select(null);	
			} else {
				Utils.updateControl(conCoreIdLabel,String.valueOf(DataManager.i().mEmployerContact.getId()));
				Utils.updateControl(conCoreActiveLabel,String.valueOf(DataManager.i().mEmployerContact.isActive()));
				Utils.updateControl(conCoreBODateLabel,DataManager.i().mEmployerContact.getBornOn());
				Utils.updateControl(conCoreLastUpdatedLabel,DataManager.i().mEmployerContact.getLastUpdated());
				
				// Contact Type
				if(DataManager.i().mEmployerContact.getContactType() != null) 
					contactTypeCombo.getSelectionModel().select(DataManager.i().mEmployerContact.getContactType());
				else
					contactTypeCombo.getSelectionModel().select(null);	
			}
			
			//contact core data
			Utils.updateControl(conContactCoreIdLabel,String.valueOf(contact.getId()));
			Utils.updateControl(conContactCoreActiveLabel,String.valueOf(contact.isActive()));
			Utils.updateControl(conContactCoreBODateLabel,contact.getBornOn());
			Utils.updateControl(conContactCoreLastUpdatedLabel,contact.getLastUpdated());
			
		} else {
			
			// Set all fields to empty for Add
			conFirstNameLabel.setText("");
			conMiddleNameLabel.setText("");
			conLastNameLabel.setText("");
			conPrimaryPhoneLabel.setText("");
			primPhnExt.setText("");
			conPrimaryPhoneTypeCombo.getSelectionModel().select(null);
			conSecondaryPhoneLabel.setText("");
			secPhnExt.setText("");
			conSecondaryPhoneTypeCombo.getSelectionModel().select(null);
			emPhn.setText("");
			emPhnExt.setText("");
			emPhnTypeCombo.getSelectionModel().select(null);
			jobTitle.setText("");
			conTimezoneCombo.getSelectionModel().select(null);
			conEmailLabel.setText("");
			conEmailTypeCombo.getSelectionModel().select(null);
			contactTypeCombo.getSelectionModel().select(null);
			secInputLabelGrid1.setVisible(false);
			
			// MAIL ADDRESS
			addressGrid.createAddress(contAddrGrid, null, "Address");
		}
	}
	
	private void saveContact()
	{
		try {
			ContactRequest request = new ContactRequest();
			Contact cont = new Contact();
			
			cont.setFirstName(conFirstNameLabel.getText());
			cont.setMiddleName(conMiddleNameLabel.getText());
			cont.setLastName(conLastNameLabel.getText());
			cont.setPhone(conPrimaryPhoneLabel.getText());
			cont.setPhoneExt(primPhnExt.getText());
			cont.setPhoneType(conPrimaryPhoneTypeCombo.getValue());
			cont.setSecondPhone(conSecondaryPhoneLabel.getText());
			cont.setSecondPhoneExt(secPhnExt.getText());
			cont.setSecondPhoneType(conSecondaryPhoneTypeCombo.getValue());
			cont.setEmergencyPhone(emPhn.getText());
			cont.setEmergencyPhoneExt(emPhnExt.getText());
			cont.setEmergencyPhoneType(emPhnTypeCombo.getValue());
			cont.setTimezone(conTimezoneCombo.getValue());
			cont.setJobTitle(jobTitle.getText());
			cont.setEmail(conEmailLabel.getText());
			cont.setEmailType(conEmailTypeCombo.getValue());
			cont.setOrganizationId(Long.valueOf(1));
			
			// Add address to db
			PostalAddressRequest mailReq = new PostalAddressRequest();
			PostalAddress mailAddr = new PostalAddress();
			
			// Required fields
			mailAddr.setStreet(addressGrid.getUpdatedAddress().getStreet());
			mailAddr.setCity(addressGrid.getUpdatedAddress().getCity());
			mailAddr.setZip(addressGrid.getUpdatedAddress().getZip());
			mailAddr.setCountry(addressGrid.getUpdatedAddress().getCountry());
			
			// Nullable fields
			if(addressGrid.getUpdatedAddress().getUnit() != null) { mailAddr.setUnit(addressGrid.getUpdatedAddress().getUnit()); }
			if(addressGrid.getUpdatedAddress().getStprv2() != null) { mailAddr.setStprv2(addressGrid.getUpdatedAddress().getStprv2()); }
			if(addressGrid.getUpdatedAddress().getZp4() != null) { mailAddr.setZp4(addressGrid.getUpdatedAddress().getZp4()); }
			if(addressGrid.getUpdatedAddress().getQuarter() != null) { mailAddr.setQuarter(addressGrid.getUpdatedAddress().getQuarter()); }
			if(addressGrid.getUpdatedAddress().getDepartment() != null) { mailAddr.setDepartment(addressGrid.getUpdatedAddress().getDepartment()); }
			if(addressGrid.getUpdatedAddress().getProvince() != null) { mailAddr.setProvince(addressGrid.getUpdatedAddress().getProvince()); }
			
			mailReq.setEntity(mailAddr);
			cont.setMailAddress(AdminPersistenceManager.getInstance().addOrUpdate(mailReq));
			
			request.setMailAddressRequest(mailReq);
			request.setEntity(cont);
			request.setId(cont.getId());
			
			cont = AdminPersistenceManager.getInstance().addOrUpdate(request);
			DataManager.i().mContact = cont;
			
			// Create contact type
			if(DataManager.i().mContactType == 0)
			{
				AccountContactRequest acctContReq = new AccountContactRequest();
				AccountContact acctCont = new AccountContact();
				
				acctCont.setContactType(contactTypeCombo.getValue());
				acctCont.setContactId(DataManager.i().mContact.getId());
				acctCont.setAccountId(DataManager.i().mAccount.getId());
				acctContReq.setEntity(acctCont);
				acctContReq.setId(acctCont.getId());
				
				acctCont = AdminPersistenceManager.getInstance().addOrUpdate(acctContReq);
				
			} else {
				EmployerContactRequest empContReq = new EmployerContactRequest();
				EmployerContact empCont = new EmployerContact();
				
				empCont.setContactType(contactTypeCombo.getValue());
				empCont.setContactId(DataManager.i().mContact.getId());
				empCont.setEmployerId(DataManager.i().mEmployer.getId());
				empContReq.setEntity(empCont);
				empContReq.setId(empCont.getId());
				
				empCont = AdminPersistenceManager.getInstance().addOrUpdate(empContReq);
			}
		} catch (CoreException e) { DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}

	private boolean validateData()
	{
		boolean bReturn = true;

		//check for required data
		if(!Utils.validate(conFirstNameLabel)) bReturn = false;
		if(!Utils.validate(conLastNameLabel)) bReturn = false;
		if(!Utils.validateTimezoneType(conTimezoneCombo)) bReturn = false; 

		if(bReturn == false) { contFillLabel.setText("Fill out First Name, Last Name and Timezone at minimum"); }
		
		return bReturn;
	}

	@FXML
	private void onShowSystemInfo()
	{
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mContact;
			DataManager.i().mCurrentCoreDataType = SystemDataType.CONTACT;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (IOException e) { DataManager.i().log(Level.SEVERE, e); }        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}

	@FXML
	private void onClose(ActionEvent event) 
	{
		Stage stage = (Stage) conFirstNameLabel.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	private void onSave(ActionEvent event)
	{
		if(!validateData())
			return;
		
		saveContact();
		
		Stage stage = (Stage) conFirstNameLabel.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	private void onEdit(ActionEvent event) 
	{
		setFieldEdits(true);
		toggleEditMode();
//		if(DataManager.i().mContactType == 0)
//			EtcAdmin.i().setScreen(ScreenType.ACCOUNTCONTACTEDIT);
//		else
//			EtcAdmin.i().setScreen(ScreenType.EMPLOYERCONTACTEDIT);			
	}	

	@FXML
	private void onContactTypeSelect(ActionEvent event) 
	{
//		Stage stage = (Stage) conFirstNameLabel.getScene().getWindow();
//		stage.close();
	}	
	
	private void toggleEditMode() 
	{
		if(editMode == false) 
		{
			editMode = true;
			setFieldEdits(true);
			editButton.setText("Cancel");
			secInputLabelGrid.setStyle("-fx-background-color: #eeffee");
			contAddrGrid.setStyle("-fx-background-color: #eeffee");
			saveButton.setVisible(true);
		}else {
			editMode = false;
			setFieldEdits(false);
			editButton.setText("Edit");
			secInputLabelGrid.getStyleClass().clear();
			secInputLabelGrid.setStyle(null);	
			contAddrGrid.getStyleClass().clear();
			contAddrGrid.setStyle(null);	
			saveButton.setVisible(false);
		}
//		showEmployee();
	}
	
	private void setFieldEdits(boolean state) 
	{
		conFirstNameLabel.setEditable(state);
		conMiddleNameLabel.setEditable(state);
		conLastNameLabel.setEditable(state);
		jobTitle.setEditable(state);
		conPrimaryPhoneLabel.setEditable(state);
		primPhnExt.setEditable(state);
		conPrimaryPhoneTypeCombo.setDisable(!state);
		conSecondaryPhoneLabel.setEditable(state);
		secPhnExt.setEditable(state);
		conSecondaryPhoneTypeCombo.setDisable(!state);
		emPhn.setEditable(state);
		emPhnExt.setEditable(state);
		emPhnTypeCombo.setDisable(!state);
		conTimezoneCombo.setDisable(!state);
		addressGrid.setEditMode(state);
		conEmailLabel.setEditable(state);
		conEmailTypeCombo.setDisable(!state);
	}
}
