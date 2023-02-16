package com.etc.admin.ui.hr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.AddressGrid;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.EmployerContact;
import com.etc.corvetto.entities.EmployerIntegrationInformation;
import com.etc.corvetto.entities.PostalAddress;
import com.etc.corvetto.entities.TaxYear;
import com.etc.corvetto.rqs.ContactRequest;
import com.etc.corvetto.rqs.EmployerContactRequest;
import com.etc.corvetto.rqs.EmployerIntegrationInformationRequest;
import com.etc.corvetto.rqs.EmployerRequest;
import com.etc.corvetto.rqs.PostalAddressRequest;
import com.etc.corvetto.rqs.TaxYearRequest;
import com.etc.utils.types.EmailType;
import com.etc.utils.types.PhoneType;
import com.etc.utils.types.TimezoneType;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewHREmployerAddController 
{
	@FXML
	private GridPane empMailGrid;
	@FXML
	private GridPane empBillGrid;
	@FXML
	private Label empFillLabel;
	@FXML
	private TextField taxId;
	@FXML
	private TextField empNameField;
	@FXML
	private TextField empPhoneField;
	@FXML
	private TextField empEmailField;
	@FXML
	private ComboBox<String> accountCombo;
	@FXML
	private ComboBox<PhoneType> empPhoneTypeCombo;
	@FXML
	private ComboBox<EmailType> empEmailTypeCombo;
	@FXML
	private ComboBox<TimezoneType> empTimezoneCombo;
	@FXML
	private CheckBox empUseMailforBilling;
	// Employer Integration Infomration
	@FXML
	private TextField empEIIV1CoIdField;
	@FXML
	private TextField empEIIV1SubCoIdField;
	@FXML
	private TextField empEIIZohoIdField;
	@FXML
	private TextField empEIIPlanStartField;
	@FXML
	private Button empAddButton;
	// Tax Years
	@FXML
	private Button empAddTaxYearButton;
	@FXML
	private ListView<HBoxTaxYearCell> empTaxYearList;
	// Contacts
	@FXML
	private Button empAddContactButton;
	@FXML
	private ListView<HBoxContactCell> empContactList;
	
	//address grids
	AddressGrid mailAddress = new AddressGrid();
	AddressGrid billAddress = new AddressGrid();
	List<Long> currentAccountListIds = new ArrayList<Long>();

	 // initialize is called when the FXML is loaded
	@FXML
	public void initialize() 
	{
		initControls();
		loadSearchAccounts();
	}
	
	private void initControls() 
	{
		mailAddress.setGrid(empMailGrid, "Mail Address");
		mailAddress.setEditMode(true);
		billAddress.setGrid(empBillGrid, "Bill Address");
		billAddress.setEditMode(!empUseMailforBilling.isSelected());
		
		empAddContactButton.setDisable(false);
		
		empAddContactButton.setDisable(true);
		empAddTaxYearButton.setDisable(true);
		
		accountCombo.getEditor().setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) 
            {
            	accountCombo.getEditor().setText("");
            	accountCombo.getSelectionModel().clearSelection();
            	accountCombo.setVisibleRowCount(15);
            }
        });
		
		
		
		//phone type
		ObservableList<PhoneType> phoneTypes = FXCollections.observableArrayList(PhoneType.values());
		empPhoneTypeCombo.setItems(phoneTypes);
		
		//email type
		ObservableList<EmailType> emailTypes = FXCollections.observableArrayList(EmailType.values());
		empEmailTypeCombo.setItems(emailTypes);

		//timezone type
		ObservableList<TimezoneType> timezoneTypes = FXCollections.observableArrayList(TimezoneType.values());
		empTimezoneCombo.setItems(timezoneTypes);
	}
	
	public void loadSearchAccounts()
	{
		String sName;
		//DataManager.i().getLocalAccounts();
		ObservableList<String> accountNames = FXCollections.observableArrayList();
		for (Account account : DataManager.i().mAccounts) 
		{
			if(account.isActive() == false) 
				continue;
			sName = account.getName();
			if (sName != null)
				accountNames.add(sName);
		};		
		
        FXCollections.sort(accountNames);
		// use a filterlist to wrap the account names for combobox searching later
        FilteredList<String> filteredItems = new FilteredList<String>(accountNames, p -> true);

        // the listener will filter the list according to what is in the combobox edit control
        accountCombo.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> 
		{
            final javafx.scene.control.TextField editor = accountCombo.getEditor();
            final String selected = accountCombo.getSelectionModel().getSelectedItem();
            
            accountCombo.show();
            accountCombo.setVisibleRowCount(10);

            // Run on the GUI thread
            Platform.runLater(() -> {
                if(selected == null || !selected.equals(editor.getText())) 
                {
                    filteredItems.setPredicate(item -> {
                        if (item.toUpperCase().contains(newvalue.toUpperCase())) 
                        {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
			
		});
        
		//finally, set our filtered items as the combobox collection
        accountCombo.setItems(filteredItems);	
		
		//close the dropdown if it is showing
        accountCombo.hide();
	}

	public void onAccountHide() {
		searchAccount();
}

public void searchAccount() 
{
	String sSelection = accountCombo.getValue();
	String sName;
	
	for (int i = 0; i < DataManager.i().mAccounts.size();i++) 
	{
		Account account = DataManager.i().mAccounts.get(i);
		if (account.isActive() == false) continue;
		sName = account.getName();
		if (sName.equals(sSelection))
		{
			// set the current account
			DataManager.i().mAccount = DataManager.i().mAccounts.get(i);
			break;
		}
	}	
}	

	
	private void saveEmployer()
	{	
		empAddContactButton.setDisable(true);
	    Task<Void> task = new Task<Void>() {
		    @Override protected Void call() throws Exception {
				try {
					// show feedback to the user
					EtcAdmin.i().showAppStatus("Creating Employer", "Creating Mail Address", 0.25, true);
					// create the employer and address
					Employer employer = new Employer();
			
					//create the updateRequest object
					EmployerRequest request = new EmployerRequest();
					
					// General data from the interface
					employer.setName(empNameField.getText());
					employer.setPhone(empPhoneField.getText());
					employer.setPhoneType(empPhoneTypeCombo.getValue());
					employer.setTIN(taxId.getText());
					employer.setEmail(empEmailField.getText());			
					employer.setEmailType(empEmailTypeCombo.getValue());
					employer.setTimezone(empTimezoneCombo.getValue());
					Account account = DataManager.i().getAccount(accountCombo.getValue());
					employer.setAccountId(account.getId());
			
					// Add address to db
					PostalAddressRequest mailReq = new PostalAddressRequest();
					PostalAddress mailAddr = new PostalAddress();
					
					// Required fields
					mailAddr.setStreet(mailAddress.getUpdatedAddress().getStreet());
					mailAddr.setCity(mailAddress.getUpdatedAddress().getCity());
					mailAddr.setZip(mailAddress.getUpdatedAddress().getZip());
					mailAddr.setCountry(mailAddress.getUpdatedAddress().getCountry());
					
					// Nullable fields
					if(mailAddress.getUpdatedAddress().getUnit() != null) { mailAddr.setUnit(mailAddress.getUpdatedAddress().getUnit()); }
					if(mailAddress.getUpdatedAddress().getStprv2() != null) { mailAddr.setStprv2(mailAddress.getUpdatedAddress().getStprv2()); }
					if(mailAddress.getUpdatedAddress().getZp4() != null) { mailAddr.setZp4(mailAddress.getUpdatedAddress().getZp4()); }
					if(mailAddress.getUpdatedAddress().getQuarter() != null) { mailAddr.setQuarter(mailAddress.getUpdatedAddress().getQuarter()); }
					if(mailAddress.getUpdatedAddress().getDepartment() != null) { mailAddr.setDepartment(mailAddress.getUpdatedAddress().getDepartment()); }
					if(mailAddress.getUpdatedAddress().getProvince() != null) { mailAddr.setProvince(mailAddress.getUpdatedAddress().getProvince()); }
					
					mailReq.setEntity(mailAddr);
					employer.setMailAddress(AdminPersistenceManager.getInstance().addOrUpdate(mailReq));
					
					EtcAdmin.i().showAppStatus("Creating Employer", "Creating Bill Address", 0.5, true);
					PostalAddressRequest billReq = new PostalAddressRequest();
					PostalAddress billAddr = new PostalAddress();
					
					// if selected, use mail address for billing
					if (empUseMailforBilling.isSelected() == true) {
						// Required fields
						billAddr.setStreet(mailAddress.getUpdatedAddress().getStreet());
						billAddr.setCity(mailAddress.getUpdatedAddress().getCity());
						billAddr.setZip(mailAddress.getUpdatedAddress().getZip());
						billAddr.setCountry(mailAddress.getUpdatedAddress().getCountry());
						
						// Nullable fields
						if(mailAddress.getUpdatedAddress().getUnit() != null) { billAddr.setUnit(mailAddress.getUpdatedAddress().getUnit()); }
						if(mailAddress.getUpdatedAddress().getStprv2() != null) { billAddr.setStprv2(mailAddress.getUpdatedAddress().getStprv2()); }
						if(mailAddress.getUpdatedAddress().getZp4() != null) { billAddr.setZp4(mailAddress.getUpdatedAddress().getZp4()); }
						if(mailAddress.getUpdatedAddress().getQuarter() != null) { billAddr.setQuarter(mailAddress.getUpdatedAddress().getQuarter()); }
						if(mailAddress.getUpdatedAddress().getDepartment() != null) { billAddr.setDepartment(mailAddress.getUpdatedAddress().getDepartment()); }
						if(mailAddress.getUpdatedAddress().getProvince() != null) { billAddr.setProvince(mailAddress.getUpdatedAddress().getProvince()); }						
					} else {
						// Required fields
						billAddr.setStreet(billAddress.getUpdatedAddress().getStreet());
						billAddr.setCity(billAddress.getUpdatedAddress().getCity());
						billAddr.setZip(billAddress.getUpdatedAddress().getZip());
						billAddr.setCountry(billAddress.getUpdatedAddress().getCountry());
						
						// Nullable fields
						if(billAddress.getUpdatedAddress().getUnit() != null) { billAddr.setUnit(billAddress.getUpdatedAddress().getUnit()); }
						if(billAddress.getUpdatedAddress().getStprv2() != null) { billAddr.setStprv2(billAddress.getUpdatedAddress().getStprv2()); }
						if(billAddress.getUpdatedAddress().getZp4() != null) { billAddr.setZp4(billAddress.getUpdatedAddress().getZp4()); }
						if(billAddress.getUpdatedAddress().getQuarter() != null) { billAddr.setQuarter(billAddress.getUpdatedAddress().getQuarter()); }
						if(billAddress.getUpdatedAddress().getDepartment() != null) { billAddr.setDepartment(billAddress.getUpdatedAddress().getDepartment()); }
						if(billAddress.getUpdatedAddress().getProvince() != null) { billAddr.setProvince(billAddress.getUpdatedAddress().getProvince()); }
					}
					
					billReq.setEntity(billAddr);
					employer.setBillAddress(AdminPersistenceManager.getInstance().addOrUpdate(billReq));
			
					// set the request entity
					request.setMailAddressRequest(mailReq);
					request.setBillAddressRequest(billReq);
					request.setEntity(employer);
					request.setId(employer.getId());
			
					// update the server
					EtcAdmin.i().showAppStatus("Creating Employer", "Creating Employer", 0.75, true);
					employer = AdminPersistenceManager.getInstance().addOrUpdate(request);
					DataManager.i().mEmployers.add(employer);
					DataManager.i().mEmployer = employer;
					
					EtcAdmin.i().showAppStatus("Creating Employer", "Creating Employer Integration Entry", 0.95, true);
					saveEmployerIntegration(employer);
					EtcAdmin.i().showAppStatus("Creating Employer", "Created", 0, false);
					Utils.alertUser("Employer Created", "The Employer " + employer.getName()  + " with Account " + account.getName() + " has been created");
				
				} catch (Exception e) {
					DataManager.i().log(Level.SEVERE, e);
					Utils.alertUser("Employer Creation Error", "There was a problem with creating the employer.");
					empAddContactButton.setDisable(false);
				}

				EtcAdmin.i().showAppStatus("", "", 0, false);
		    	return null;
		    }
		};
		Thread thread = new Thread(task, "showAppStatus");
		thread.setDaemon(true);
		thread.start();		
	}
	
	private void saveEmployerIntegration(Employer employer)
	{
		EmployerIntegrationInformation eiInfo = new EmployerIntegrationInformation();
		EmployerIntegrationInformationRequest eiReq = new EmployerIntegrationInformationRequest();

		eiInfo.setV1CoId(Integer.valueOf(empEIIV1CoIdField.getText().toString()));
		eiInfo.setV1SubCoId(Integer.valueOf(empEIIV1SubCoIdField.getText().toString()));
		eiInfo.setZohoId(empEIIZohoIdField.getText().toString());
		eiInfo.setPlanStart(empEIIPlanStartField.getText().toString());
		eiInfo.setEmployerId(employer.getId());
		eiInfo.setEmployer(employer);

		//set request entity
		eiReq.setEntity(eiInfo);
		
		//Add to the database
		try {
			DataManager.i().mEmployerIntegrationInformation = AdminPersistenceManager.getInstance().addOrUpdate(eiReq);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void showContacts() 
	{
		if(DataManager.i().mEmployer != null)
		{
			empContactList.getItems().clear();
			EmployerContactRequest request = new EmployerContactRequest();
			request.setEmployerId(DataManager.i().mEmployer.getId());
			try {
				DataManager.i().mEmployerContacts =  AdminPersistenceManager.getInstance().getAll(request);
			} catch (CoreException e) {
	        	DataManager.i().log(Level.SEVERE, e); 
			}
		    catch (Exception e) {  DataManager.i().logGenericException(e); }
	
			if(DataManager.i().mEmployerContacts != null)
			{
				empContactList.getItems().add(new HBoxContactCell(null));
				for(EmployerContact employerContact : DataManager.i().mEmployerContacts)
				{
					if(employerContact == null) continue;
	
					ContactRequest conReq = new ContactRequest();
					conReq.setId(employerContact.getContactId());
					try {
						DataManager.i().mContact =  AdminPersistenceManager.getInstance().get(conReq);
					} catch (CoreException e) {
			        	DataManager.i().log(Level.SEVERE, e); 
					}
				    catch (Exception e) {  DataManager.i().logGenericException(e); }

					if(employerContact.getContactId().equals(DataManager.i().mContact.getId()))
					{
						empContactList.getItems().add(new HBoxContactCell(employerContact));
					}
				};	
	        }
		}
	}
	
	private void showTaxYears()
	{
		if(DataManager.i().mTaxYear != null)
		{
			empTaxYearList.getItems().clear();
			TaxYearRequest request = new TaxYearRequest();
			request.setId(DataManager.i().mEmployer.getId());
			request.setEmployerId(DataManager.i().mEmployer.getId());
			
			List<TaxYear> taxYears = null;
			try {
				taxYears = AdminPersistenceManager.getInstance().getAll(request);
			} catch (CoreException e) {
	        	DataManager.i().log(Level.SEVERE, e); 
			}
		    catch (Exception e) {  DataManager.i().logGenericException(e); }
	
			if(taxYears != null) 
			{
				empTaxYearList.getItems().add(new HBoxTaxYearCell(null));
				for(TaxYear taxYear : taxYears) 
				{
					if(taxYear == null) continue;

					empTaxYearList.getItems().add(new HBoxTaxYearCell(taxYear));
				};	
	        }
		}
	}
	
	private boolean validateData()
	{
		boolean bReturn = true;

		//check for required data
		if(!Utils.validate(accountCombo)) bReturn = false;
		if(!Utils.validate(empNameField)) bReturn = false;
		if(!Utils.validateIntTextField(taxId)) bReturn = false; 
		if(mailAddress.validateData() == false) bReturn = false;

		//required before save
		if(!Utils.validate(empEIIV1CoIdField)) bReturn = false; 
		if(!Utils.validate(empEIIV1SubCoIdField)) bReturn = false;  
		if(!Utils.validate(empEIIZohoIdField)) bReturn = false; 
		if(!Utils.validate(empEIIPlanStartField)) bReturn = false; 

		if(bReturn == false) { empFillLabel.setText("Fill out all Employer and Mail Address information before adding Contact or Taxyear"); }
		
		return bReturn;
	}
	
	@FXML
	private void onSave(ActionEvent event) 
	{
		//validate everything
		if(!validateData()) 
			return;
		// save the account
		saveEmployer();
		empAddContactButton.setDisable(false);
		empAddTaxYearButton.setDisable(false);

	}	
	
	@FXML
	private void onAddTaxYear(ActionEvent event) 
	{
		// load the employer add popup
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/taxyear/ViewTaxYearAdd.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        // update the employers
	        DataManager.i().updateEmployers();
	        // and reload
	        showTaxYears();
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	
	
	@FXML
	private void onAddContact(ActionEvent event) 
	{
		if(!validateData()) 
			return;
		// load the employer add popup
		try {
			DataManager.i().mContactType = 1;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/contact/ViewContact.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        // update the employers
	        DataManager.i().updateEmployers();
	        // and reload
	        showContacts();
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	
	
	@FXML
	private void onCancel(ActionEvent event) 
	{
		exitPopup();
	}	
	
	@FXML
	private void onUseMailForBilling(ActionEvent event) 
	{
		billAddress.setEditMode(!empUseMailforBilling.isSelected());
	}	
	
	private void exitPopup()
	{
		Stage stage = (Stage) empNameField.getScene().getWindow();
		stage.close();
	}
	
	public class HBoxContactCell extends HBox 
	{
        Label lblName = new Label();
        Label lblEmail = new Label();
        Label lblPhone = new Label();
        EmployerContact employerContact;
        
        public String getName() {
        		return lblName.getText();
        }
        
        public EmployerContact getEmployerContact() {
        	return employerContact;
        }
        
        HBoxContactCell(EmployerContact employerContact) 
        {
             super();
          
             this.employerContact = employerContact;
             if(employerContact != null && DataManager.i().mContact != null) 
             {
            	 Utils.setHBoxLabel(lblName, 150, false, DataManager.i().mContact.getLastName() + ", " + DataManager.i().mContact.getFirstName());
            	 Utils.setHBoxLabel(lblEmail, 150, false, DataManager.i().mContact.getEmail());
            	 Utils.setHBoxLabel(lblPhone, 150, false, DataManager.i().mContact.getPhone());
             } else {
            	 Utils.setHBoxLabel(lblName, 150, true, "Name");
            	 Utils.setHBoxLabel(lblEmail, 150, true, "Email");
            	 Utils.setHBoxLabel(lblPhone, 150, true, "Phone");
             }
             this.getChildren().addAll(lblName, lblEmail, lblPhone);
        }
	}
	
	public class HBoxTaxYearCell extends HBox 
	{
        Label lblYear = new Label();
        Label lblServiceLevel = new Label();
        Label lblPrintType = new Label();
        TaxYear taxYear;
        
        public int getYear() {
        	if(lblYear.getText() != null && lblYear.getText()!= "")
        		return (Integer.valueOf(lblYear.getText()));
        	else
        		return 0;
        }
        
        public TaxYear getTaxYear() {
        	return taxYear;
        }
        
        HBoxTaxYearCell(TaxYear taxYear) 
        {
             super();
          
             this.taxYear = taxYear;
             if(taxYear != null)
             {
            	 Utils.setHBoxLabel(lblYear, 150, false, taxYear.getYear());
            	 if(taxYear.getServiceLevel() != null)
            		 Utils.setHBoxLabel(lblServiceLevel, 150, false, taxYear.getServiceLevel().toString());
            	 if(taxYear.getPrintType() != null)
            		 Utils.setHBoxLabel(lblPrintType, 150, false, taxYear.getPrintType().toString());
             } else {
            	 Utils.setHBoxLabel(lblYear, 150, true, "Tax Year");
            	 Utils.setHBoxLabel(lblServiceLevel, 150, true, "Service Level");
            	 Utils.setHBoxLabel(lblPrintType, 150, true, "Print Type");
             }
             this.getChildren().addAll(lblYear, lblServiceLevel, lblPrintType);
        }
	}
}
