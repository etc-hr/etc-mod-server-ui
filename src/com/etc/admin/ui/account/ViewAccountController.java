package com.etc.admin.ui.account;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.etc.CoreException;
import com.etc.admin.AdminApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.calc.CalcQueueData;
import com.etc.admin.ui.callcodes.ViewCodeCallHistoryController;
import com.etc.admin.ui.notes.ViewNotesController;
import com.etc.admin.ui.pipeline.queue.PipelineQueue;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.Employee;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.TaxYear;
import com.etc.corvetto.rqs.AccountRequest;
import com.etc.corvetto.rqs.EmployeeRequest;
import com.etc.corvetto.rqs.EmployerRequest;
import com.etc.corvetto.rqs.PersonRequest;
import com.etc.corvetto.rqs.TaxYearRequest;
import com.etc.entities.CoreData;
import com.etc.utils.types.EmailType;
import com.etc.utils.types.PhoneType;
import com.etc.utils.types.TimezoneType;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class ViewAccountController 
{
	@FXML
	private GridPane acctNameGrid;
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
	private ChoiceBox<PhoneType> acctPhoneTypeChoice;
	@FXML
	private ChoiceBox<EmailType> acctEmailTypeChoice;
	@FXML
	private ChoiceBox<TimezoneType> acctTimezoneChoice;
	@FXML
	private TextField acctOrganizationField;
	
	// LISTS
	@FXML
	private ListView<HBoxEmployerCell> acctEmployersListView;
	@FXML
	private Button acctEditButton;
	@FXML
	private Button acctSaveButton;
	@FXML
	private Label acctSpecificationsLabel;
	@FXML
	private Label acctEmployersLabel;
	@FXML
	private Button acctAddEmployerButton;
	// CORE DATA
	@FXML
	private Label acctCoreIdLabel;
	@FXML
	private Label acctCoreActiveLabel;
	@FXML
	private Label acctCoreBODateLabel;
	@FXML
	private Label acctCoreLastUpdatedLabel;
	//aesthetics
	@FXML
	private GridPane acctContactGrid;
	@FXML
	private GridPane acctPropertiesGrid;
	@FXML
	private GridPane acctUsersGrid;
	@FXML
	private GridPane acctCoreDataGrid;
	@FXML
	private VBox acctOuterVBox;
	@FXML
	private VBox acctInnerVBox;
	// File History
	@FXML
	private Label acctFileHistoryLabel;
	@FXML
	private CheckBox acctShowInactiveEmployers;
	@FXML
	private CheckBox acctShowInactiveUsers;	
	@FXML
	private Button fileHistoryButton;
	// Calc
	@FXML 
	private Button CalcIrs10945XBCButton; 
	@FXML
	private Button empMergeButton;
	@FXML
	private ComboBox<String> actCalcTaxYearCombo;
	// employee
	@FXML
	private Button acctAddEmployeeButton;
	@FXML
	private TableView<EmployeeCell> acctEmployeeTableView;
	@FXML
	private TextField acctEmployeeSSNField;
	@FXML
	private TextField acctEmployeeCoreIdField;
	@FXML
	private TextField acctEmployeeFirstNameField;
	@FXML
	private TextField acctEmployeeLastNameField;
	@FXML
	private CheckBox acctShowInactiveEmployees;
	@FXML
	private CheckBox acctShowDeletedEmployees;
	@FXML
	private Button acctSearchEmployeeButton;
	@FXML
	private Label acctCallCodeStatus;
	 
	//table sort 
	TableColumn<HBoxFileHistoryCell, String> sortColumn = null;
	TableColumn<EmployeeCell, String> employeeSortColumn = null;
	
	//file history queue
	List<PipelineQueue> fileHistoryQueue = new ArrayList<PipelineQueue>();	
	// file history data object
	FileHistoryData fileHistoryData = new FileHistoryData();
	
	//selected fileHistory queue
	PipelineQueue selectedFileHistoryQueue = null;
	private List<Employee> searchEmployees = null;
	Logger logr;
	TaxYear defaultTaxYear = null;
	
	// file history perisistence 
    Stage fhStage = null;
    
    // strings for messages after thread execution, if needed
    String message1 = "";
	String message2 = "";
    
	 // initialize is called when the FXML is loaded
	@FXML
	public void initialize() 
	{	
		try {
			logr = Logger.getLogger(this.getClass().getName());
			initControls();
			// launch the data load threads
			updateAccount();
			updateAccountEmployers();
			loadFilehistory();
		}catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		}

	}
	
	private void initControls() 
	{
		acctCallCodeStatus.setVisible(false);
		setEmployeeTableColumns();
		
		// show the inactive employees by default
		acctShowInactiveEmployees.setSelected(true);
		
		// clean up the screen where needed
		actCalcTaxYearCombo.getItems().clear();
		clearAccountDetails();
		clearEmployeeSearch();
		
		// set calc button images
		InputStream input1 = getClass().getClassLoader().getResourceAsStream("img/ERCalc1.png");
   	 	Image image1 = new Image(input1, 65f, 65f, true, true);
   	 	ImageView imageView1 = new ImageView(image1);
   	 	CalcIrs10945XBCButton.setGraphic(imageView1);

		InputStream input3 = getClass().getClassLoader().getResourceAsStream("img/ERCalc3.png");
   	 	Image image3 = new Image(input3, 65, 65, true, true);
   	 	ImageView imageView3 = new ImageView(image3);
   	 	empMergeButton.setGraphic(imageView3);
		 
		//phone type
		ObservableList<PhoneType> phoneTypes = FXCollections.observableArrayList(PhoneType.values());
		acctPhoneTypeChoice.setItems(phoneTypes);
		
		//email type
		ObservableList<EmailType> emailTypes = FXCollections.observableArrayList(EmailType.values());
		acctEmailTypeChoice.setItems(emailTypes);

		//timezone type
		ObservableList<TimezoneType> timezoneTypes = FXCollections.observableArrayList(TimezoneType.values());
		acctTimezoneChoice.setItems(timezoneTypes);

		// set the edit mode
		setEditMode(false);

		//add handlers for listbox selection notification
		// EMPLOYERS
		acctEmployersListView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
				 DataManager.i().mEmployer = acctEmployersListView.getSelectionModel().getSelectedItem().getEmployer();
				//tell main to load the employer screen
				EtcAdmin.i().setScreen(ScreenType.EMPLOYER, true);
            }
        });
		
		// EMPLOYEES
		acctEmployeeTableView.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// get the selected person
            	if(acctEmployeeTableView.getSelectionModel().getSelectedItem() != null) {
            		DataManager.i().mEmployee = acctEmployeeTableView.getSelectionModel().getSelectedItem().getEmployee();
	           	 	// preload what we need
                	// set the Employer
                	if (DataManager.i().mEmployee.getEmployer() == null) {
    	            	try { DataManager.i().mEmployer = AdminPersistenceManager.getInstance().get(Employer.class, DataManager.i().mEmployee.getEmployerId()); }
    	            	catch(CoreException e) { logr.log(Level.SEVERE, "Exception.", e); }
    	        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
                	} else 
                		DataManager.i().mEmployer = DataManager.i().mEmployee.getEmployer();
                	
                	// set the account
                	if (DataManager.i().mEmployer.getAccount() == null) {
	                	try { DataManager.i().mAccount = AdminPersistenceManager.getInstance().get(Account.class, DataManager.i().mEmployer.getAccountId()); } 	
	                	catch(CoreException e) { logr.log(Level.SEVERE, "Exception.", e); }
	            	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	                } else
                		DataManager.i().mAccount = DataManager.i().mEmployer.getAccount();
                	
                	//set the parent objects
                	DataManager.i().mEmployer.setAccount(DataManager.i().mAccount);
                	DataManager.i().mEmployee.setEmployer(DataManager.i().mEmployer);
            		
            		// and launch it
	           	 	EtcAdmin.i().setScreen(ScreenType.EMPLOYEE, true);
            	}
            }
        });
		
		//set functionality according to the user security level
		acctEditButton.setDisable(!Utils.userCanEdit());	
		acctAddEmployerButton.setDisable(!Utils.userCanAdd());
		
	}
	
	public void clearEmployeeSearch()
	{
		acctEmployeeTableView.getItems().clear();
		acctEmployeeSSNField.setText("");
		acctEmployeeCoreIdField.setText("");
		acctEmployeeFirstNameField.setText("");
		acctEmployeeLastNameField.setText("");
	}
	
	public void clearAccountDetails()
	{
			Utils.setActiveHeader(acctHeaderLabel,true);
			Utils.updateControl(acctNameField,"");
			Utils.updateControl(acctDescriptionField,"");
			Utils.updateControl(acctPhoneField,"");
			Utils.updateControl(acctEmailField,"");
	}
	

	
	public void clearSearchEmployeeList() {
		acctEmployeeTableView.getItems().clear();
	}
	
	private void setEmployeeTableColumns() 
	{
		//clear the default values
		acctEmployeeTableView.getColumns().clear();

	    TableColumn<EmployeeCell, String> x1 = new TableColumn<EmployeeCell, String>("Id");
		x1.setCellValueFactory(new PropertyValueFactory<EmployeeCell,String>("id"));
		x1.setMinWidth(75);
		x1.setComparator((String o1, String o2) -> { return Utils.compareNumberStrings(o1, o2); });
		acctEmployeeTableView.getColumns().add(x1);
		setCellFactory(x1);
		
	    TableColumn<EmployeeCell, String> x2 = new TableColumn<EmployeeCell, String>("Name");
		x2.setCellValueFactory(new PropertyValueFactory<EmployeeCell,String>("name"));
		x2.setMinWidth(225);
		acctEmployeeTableView.getColumns().add(x2);
		setCellFactory(x2);
		employeeSortColumn = x2;
		employeeSortColumn.setSortType(SortType.ASCENDING);
		
	    TableColumn<EmployeeCell, String> x3 = new TableColumn<EmployeeCell, String>("Employer");
		x3.setCellValueFactory(new PropertyValueFactory<EmployeeCell,String>("employer"));
		x3.setMinWidth(300);
		acctEmployeeTableView.getColumns().add(x3);
		setCellFactory(x3);	

		TableColumn<EmployeeCell, String> x4 = new TableColumn<EmployeeCell, String>("Date");
		x4.setCellValueFactory(new PropertyValueFactory<EmployeeCell,String>("date"));
		x4.setMinWidth(100);
		x4.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		acctEmployeeTableView.getColumns().add(x4);
		setCellFactory(x4);

	    TableColumn<EmployeeCell, String> x5 = new TableColumn<EmployeeCell, String>("SSN");
		x5.setCellValueFactory(new PropertyValueFactory<EmployeeCell,String>("ssn"));
		x5.setMinWidth(75);
		acctEmployeeTableView.getColumns().add(x5);
		setCellFactory(x5);
	}

	private void setCellFactory(TableColumn<EmployeeCell, String>  col) 
	{
		col.setCellFactory(column -> 
		{
		    return new TableCell<EmployeeCell, String>() 
		    {
		        @Override
		        protected void updateItem(String item, boolean empty) 
		        {	
		            super.updateItem(item, empty);
		            if(item == null || empty) 
		            { 
		                setText(null);
		                setStyle("");
		            } else {
		                setText(item);
		                EmployeeCell cell = getTableView().getItems().get(getIndex());
		                if(cell.getEmployee().isActive() == false)
		                	setTextFill(Color.BLUE);
		                else
		                	setTextFill(Color.BLACK);
		            }
		        }
		    };
		});
	}

	///////////////////////////////////////////////////////////////////////////
	//  START THREADED SEQUENTIAL TDATA LOAD
	//  1st THREAD - Update Account
	///////////////////////////////////////////////////////////////////////////
	private void updateAccount()
	{
		try
		{
			// create a thread to handle the update
			Task<Void> task = new Task<Void>() {
	            @Override
	            protected Void call() throws Exception
	            {
	            	try {
	            	// update 
	            	AccountRequest request = new AccountRequest(DataManager.i().mAccount);
	            	DataManager.i().mAccount = AdminPersistenceManager.getInstance().get(request);
	            	// get tax years
	            	DataManager.i().mTaxYears = new ArrayList<TaxYear>();
	            	// gather up all possible tax years for all employers for this account
	            	for (Employer er : DataManager.i().mAccount.getEmployers()) {
	            		if (er.isActive() == false || er.isDeleted() == true) continue;
		            	TaxYearRequest tyReq = new TaxYearRequest();
	            		tyReq.setEmployerId(er.getId());
	            		List<TaxYear> taxYears = AdminPersistenceManager.getInstance().getAll(tyReq);
	            		if (taxYears != null ) {
	            			for (TaxYear ty : taxYears) {
		            			DataManager.i().mTaxYears.add(ty);        				
	            			}
	            		}
	            	}
	            	} catch (Exception e) {
	            		DataManager.i().log(Level.SEVERE, e); 
	            	}
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {
	      		EtcAdmin.i().setStatusMessage("updating Account...");
	      		EtcAdmin.i().setProgress(0.25);});
	      	
	    	task.setOnSucceeded(e ->  showAccount());
	    	task.setOnFailed(e -> { 
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		showAccount();
	    	});
	    	
	    	AdminApp.getInstance().getFxQueue().put(task);
	    	
		}catch(InterruptedException e)
		{
			logr.log(Level.SEVERE, "Exception.", e);
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	///////////////////////////////////////////////////////////////////////////
	//  2nd SEQUENTIAL THREAD - Update Account Employers
	///////////////////////////////////////////////////////////////////////////
	private void updateAccountEmployers()
	{
		try
		{
			acctEmployersListView.getItems().clear();
			acctEmployersLabel.setText("Employers (loading ...)"); 
			// create a thread to handle the update
			Task<Void> task = new Task<Void>() {
	            @Override
	            protected Void call() throws Exception
	            {
	            	// Employers are updated when the app starts.
	            	// so we just need to load them from the local cache
	            	EmployerRequest rqs = new EmployerRequest();
	            	rqs.setAccountId(DataManager.i().mAccount.getId());
	            	DataManager.i().mAccount.setEmployers(AdminPersistenceManager.getInstance().getAll(rqs));
	            	return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {
	      		EtcAdmin.i().setStatusMessage("Loading Employer Data...");
	      		EtcAdmin.i().setProgress(0.25);});
	      			
	    	task.setOnSucceeded(e ->  showEmployers());
	    	task.setOnFailed(e ->  {
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		showEmployers();
	    	});
	        
	    	AdminApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e)
		{
			logr.log(Level.SEVERE, "Exception.", e);
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}

	///////////////////////////////////////////////////////////////////////////
	//  START SCREEN DISPLAY
	//  1 showAccount
	///////////////////////////////////////////////////////////////////////////
	public void showAccount()
	{
		if(DataManager.i().mAccount != null) 
		{
			Account acct = DataManager.i().mAccount;
			Utils.setActiveHeader(acctHeaderLabel,acct.isActive());
			Utils.updateControl(acctNameField,acct.getName());
			Utils.updateControl(acctDescriptionField,acct.getWebsite());
			Utils.updateControl(acctPhoneField,acct.getPhone());
			if(acct.getPhoneType() != null) 
				acctPhoneTypeChoice.getSelectionModel().select(acct.getPhoneType());
			Utils.updateControl(acctEmailField,acct.getEmail());
			if(acct.getEmailType() != null) 
				acctEmailTypeChoice.getSelectionModel().select(acct.getEmailType());
			if(acct.getTimezone() != null) 
				acctTimezoneChoice.getSelectionModel().select(acct.getTimezone()); 
			
			// add the calc tax years
			actCalcTaxYearCombo.getItems().clear();
			if (DataManager.i().mTaxYears != null) {
				for (TaxYear ty : DataManager.i().mTaxYears) {
					if (actCalcTaxYearCombo.getItems().contains(String.valueOf(ty.getYear())) == false)
						actCalcTaxYearCombo.getItems().add(String.valueOf(ty.getYear()));
				}
			}
			
			// sort
			Collections.sort(actCalcTaxYearCombo.getItems());
			// select default or last
			boolean selected = false;
			if (actCalcTaxYearCombo.getItems() != null && actCalcTaxYearCombo.getItems().size() > 0 ) {
				for (String year : actCalcTaxYearCombo.getItems())
					if (year.equals("2022") == true) {
						actCalcTaxYearCombo.getSelectionModel().select(year);
						selected = true;
					}
				if (selected == false)
					actCalcTaxYearCombo.getSelectionModel().selectLast(); 
			}
			
			//core data read only
			Utils.updateControl(acctCoreIdLabel,String.valueOf(acct.getId()));
			Utils.updateControl(acctCoreActiveLabel,String.valueOf(acct.isActive()));
			Utils.updateControl(acctCoreBODateLabel,acct.getBornOn());
			Utils.updateControl(acctCoreLastUpdatedLabel,acct.getLastUpdated());
		} 
	}
	
	///////////////////////////////////////////////////////////////////////////
	//  SCREEN DISPLAY
	//  2 showEmployers
	///////////////////////////////////////////////////////////////////////////
	private void showEmployers() 
	{
		// clear anything already in the list
		acctEmployersListView.getItems().clear();
		
		//if we have no employers, bail
		if(DataManager.i().mAccount.getEmployers() == null) return;
		
		//String sAddress;
		List<HBoxEmployerCell> list = new ArrayList<>();
		
		if(DataManager.i().mAccount.getEmployers().size() > 0)
			list.add(new HBoxEmployerCell(null));
			
		for (Employer employer : DataManager.i().mAccount.getEmployers()) 
		{
	    	if(employer.isActive() == false && acctShowInactiveEmployers.isSelected() == false) continue;
	    	if(employer.isDeleted() == true && acctShowInactiveEmployers.isSelected() == false) continue;

			list.add(new HBoxEmployerCell(employer));
		};	
		
        ObservableList<HBoxEmployerCell> myObservableList = FXCollections.observableList(list);
        acctEmployersListView.setItems(myObservableList);		
        //update our employer screen label
        acctEmployersLabel.setText("Employers (total: " + String.valueOf(DataManager.i().mAccount.getEmployers().size()) + ")" );
  		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0);
        
		//redraw the control
		acctEmployersListView.refresh();
	}
	
	private void saveAccount()
	{
		//account should never be null at this point
		if(DataManager.i().mAccount != null)
		{
			//create the updateRequest object
			AccountRequest request = new AccountRequest();
			
			// General data from the interface
			DataManager.i().mAccount.setName(acctNameField.getText());
			DataManager.i().mAccount.setWebsite(acctDescriptionField.getText());
			DataManager.i().mAccount.setPhone(acctPhoneField.getText());
			DataManager.i().mAccount.setPhoneType(acctPhoneTypeChoice.getValue());
			DataManager.i().mAccount.setEmail(acctEmailField.getText());			
			DataManager.i().mAccount.setEmailType(acctEmailTypeChoice.getValue());
			DataManager.i().mAccount.setTimezone(acctTimezoneChoice.getValue()); 
			
			// set the request entity
			request.setEntity(DataManager.i().mAccount);
			request.setId(DataManager.i().mAccount.getId());

			// update the server
			try {
				DataManager.i().mAccount = AdminPersistenceManager.getInstance().addOrUpdate(request);
			} catch (CoreException e) {
	        	DataManager.i().log(Level.SEVERE, e); 
			}
		    catch (Exception e) {  DataManager.i().logGenericException(e); }
		}
	}
	
	private void doEmployeeSearch()
	{
		// clear the current
		acctEmployeeTableView.getItems().clear();
		acctSearchEmployeeButton.setStyle("-fx-background-color:lightgreen;");
		message1 = "";
		message2 = "";
		// new thread
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            	// clear any existing search
            	if(searchEmployees != null)
            		searchEmployees.clear();
            	else
					searchEmployees =  new ArrayList<Employee>();

				// if we have an SSN, do the SSN search
				if(acctEmployeeSSNField.getText() != null && acctEmployeeSSNField.getText().isEmpty() == false) 
				{
					if(Utils.isNumber(acctEmployeeSSNField))
					{
						EmployeeRequest eRequest = new EmployeeRequest();
						eRequest.setUsrln(acctEmployeeSSNField.getText());
						eRequest.setAccountId(DataManager.i().mAccount.getId());
						eRequest.setFetchInactive(true);
						searchEmployees = AdminPersistenceManager.getInstance().getAll(eRequest);
						if (searchEmployees == null || searchEmployees.size() == 0) {
							message1 = "No Search Results";
							message2 = "There were no Account search results for the SSN " + acctEmployeeSSNField.getText();
						}
					}
					return null;
				}
				
				// if we have a coreId, do an id search
				if(acctEmployeeCoreIdField.getText() != null && acctEmployeeCoreIdField.getText().isEmpty() == false) 
				{
					if(Utils.isNumber(acctEmployeeCoreIdField)) {
						EmployeeRequest request = new EmployeeRequest();
						request.setId(Long.parseLong(acctEmployeeCoreIdField.getText()));
						request.setAccountId(DataManager.i().mAccount.getId());
						request.setFetchInactive(true);
						//request.setEmployerId(DataManager.i().mEmployer.getId());
						Employee employee = null;
						try {
							employee = AdminPersistenceManager.getInstance().get(request);
						} catch (Exception e) {
							DataManager.i().log(Level.SEVERE, e); 
						}
						if (employee != null) {
							// verify the account id
							if (employee.getEmployer() != null && employee.getEmployer().getAccountId().equals(DataManager.i().mAccount.getId())) {
								// get the person
								PersonRequest pr = new PersonRequest();
								pr.setId(employee.getPersonId());
								pr.setFetchInactive(true);
								employee.setPerson(AdminPersistenceManager.getInstance().get(pr));
								searchEmployees.add(employee);
							} else {
								message1 = "Wrong Account on CoreID Search Result";
								if (employee.getEmployer() != null && employee.getEmployer().getAccount() != null)
									message2 = "The search returned an employee for the Account\r" + employee.getEmployer().getAccount().getName() +   "\rinstead of the current selected one. Please correct and retry.";
								else
									message2 = "The search returned an employee for a different account instead of the current selected one. Please correct and retry.";
							}
						} else {
							message1 = "No Search Results";
							message2 = "There were no search results for the coreID " + acctEmployeeCoreIdField.getText();
							//DataManager.i().insertLogEntry("Empty Employee search on core id " + acctEmployeeCoreIdField.getText(), LogType.INFO);
						}
					}
					return null;
				}
				
				// must be a name search
				EmployeeRequest request = new EmployeeRequest();
				request.setAccountId(DataManager.i().mAccount.getId());
				request.setFetchInactive(true);
				// first name
				if(acctEmployeeFirstNameField.getText() != null && acctEmployeeFirstNameField.getText().isEmpty() == false)
					request.setPersonFirstName(acctEmployeeFirstNameField.getText() + "%");
				// last name
				if(acctEmployeeLastNameField.getText() != null && acctEmployeeLastNameField.getText().isEmpty() == false)
					request.setPersonLastName(acctEmployeeLastNameField.getText() + "%");
				
				searchEmployees = AdminPersistenceManager.getInstance().getAll(request);
				if (searchEmployees == null || searchEmployees.size() == 0) {
					message1 = "No Search Results";
					message2 = "There were no Account search results for the given name";
				}

				return null;
	        }
	    };
    
	  	task.setOnScheduled(e ->  {
	  		EtcAdmin.i().setStatusMessage("Searching Employees...");
	  		EtcAdmin.i().setProgress(0.25);});
	  			
		task.setOnSucceeded(e ->  showEmployees());
		task.setOnFailed(e -> {  
	    	DataManager.i().log(Level.SEVERE,task.getException());
			showEmployees();
		});
		
	    new Thread(task).start();
	}
	
	//update the employees list
	private void showEmployees() 
	{
		acctSearchEmployeeButton.setStyle("");
		acctEmployeeTableView.getItems().clear();

		if(searchEmployees != null && searchEmployees.size() > 0) 
		{
			
			for(Employee employee : searchEmployees)
			{
		    	if(employee.isDeleted() == true && acctShowDeletedEmployees.isSelected() == false) continue;
				if(employee.isActive() == false && acctShowInactiveEmployees.isSelected() == false) continue;
				// no employees from inactive employers
				if (employee.getEmployer() != null && employee.getEmployer().isActive() == false) continue;
		    	acctEmployeeTableView.getItems().add(new EmployeeCell(employee));
			};	
		}

		// add the default sort
		acctEmployeeTableView.getSortOrder().add(employeeSortColumn);
		employeeSortColumn.setSortable(true);

        // update our status
		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0);
  		
  		// show any messages from the search thread
  		if (message1 != "") {
  			Utils.showAlert(message1, message2);
  		}
  		
  		message1 = "";
  		message2 = "";
	}

	private void setEditMode(boolean mode) 
	{
		if(mode == true) 
		{
			acctEditButton.setText("Cancel");
			acctNameGrid.setStyle("-fx-background-color: #eeffee");
		}else {
			acctEditButton.setText("Edit");
			acctNameGrid.getStyleClass().clear();
			acctNameGrid.setStyle(null);	
		}
		
		acctSaveButton.setVisible(mode);
		acctNameField.setEditable(mode);
		acctDescriptionField.setEditable(mode);
		acctPhoneField.setEditable(mode);
		acctPhoneTypeChoice.setDisable(!mode);
		acctEmailField.setEditable(mode);
		acctEmailTypeChoice.setDisable(!mode);
		acctTimezoneChoice.setDisable(!mode);
	}	

	@FXML
	private void onShowSystemInfo() {
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mAccount;
			DataManager.i().mCurrentCoreDataType = SystemDataType.ACCOUNT;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        EtcAdmin.i().showMain();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
		
	}
	
	@FXML
	private void onShowNotes() {
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/notes/ViewNotes.fxml"));
			Parent ControllerNode = loader.load();
	        ViewNotesController noteController = (ViewNotesController) loader.getController();
	        noteController.setScreenType(ScreenType.ACCOUNT);
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        EtcAdmin.i().showMain();
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	@FXML
	private void onFileHistoryButton(ActionEvent event) 
	{
		try {
	        EtcAdmin.i().positionStageCenter(fhStage);
	        fhStage.showAndWait();
	        EtcAdmin.i().showMain();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	}	
	
	private void loadFilehistory()
	{
		try {
            // load the fxml
			FXMLLoader fhLoader = new FXMLLoader(AdminManager.class.getResource("ui/account/ViewFileHistory.fxml"));
			Parent fhControllerNode = fhLoader.load();
	        fhStage = new Stage();
	        fhStage.initModality(Modality.APPLICATION_MODAL);
	        fhStage.initStyle(StageStyle.UNDECORATED);
	        fhStage.setScene(new Scene(fhControllerNode));  
	        EtcAdmin.i().positionStageCenter(fhStage);
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	@FXML
	private void onEdit(ActionEvent event) 
	{
		if(acctEditButton.getText().equals("Edit"))
			setEditMode(true);
		else
			setEditMode(false);
	}	
	
	@FXML
	private void onSearchEmployees(ActionEvent event) {
		doEmployeeSearch();
	}	

	@FXML
	private void onClearSearchEmployees(ActionEvent event) {
		clearSearchEmployeeFields();
	}	

	public void clearSearchEmployeeFields() {
		acctEmployeeSSNField.setText("");
		acctEmployeeCoreIdField.setText("");
		acctEmployeeFirstNameField.setText("");
		acctEmployeeLastNameField.setText("");
	}
	
	@FXML
	private void onShowInactiveEmployees() {
		showEmployees();
	}
		
	@FXML
	private void onAddEmployee(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.EMPLOYEEADD, true);
	}	
	
	private boolean validateData()
	{
		boolean bReturn = true;

		//check for required data
		if( !Utils.validate(acctNameField)) bReturn = false;
		//phone
		if( !Utils.validatePhoneTextField(acctPhoneField)) bReturn = false;		
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
		
		// reset the edit mode
		setEditMode(false);
	}	

	@FXML
	private void onAddEmployer(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.EMPLOYERADD, true);
	}	
	
	@FXML
	private void onAddAccountContact(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.ACCOUNTCONTACTADD, true);
	}	
	
	@FXML
	private void onShowPlans(ActionEvent event) {
		try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/account/ViewPlans.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        EtcAdmin.i().showMain();
		} catch (IOException e) {
	    	DataManager.i().log(Level.SEVERE, e); 
		}        
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	
	
	@FXML
	private void onShowFilingTransmissions() {
		try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/account/ViewAccountFiling.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        EtcAdmin.i().showMain();
		} catch (IOException e) {
	    	DataManager.i().log(Level.SEVERE, e); 
		}        
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	
	
	@FXML
	private void onShowUsers(ActionEvent event) {
		try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/account/ViewUsers.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        EtcAdmin.i().showMain();
		} catch (IOException e) {
	    	DataManager.i().log(Level.SEVERE, e); 
		}        
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	
	
	@FXML
	private void onAddProperty(ActionEvent event) {
		//EtcAdmin.i().setScreen(ScreenType);
	}	
	
	@FXML
	private void onShowCodeCallHistory() {
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/callcodes/ViewCodeCallHistory.fxml"));
			Parent ControllerNode = loader.load();
			ViewCodeCallHistoryController controller = (ViewCodeCallHistoryController) loader.getController();
			controller.setIsAccountHistory(true);
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        EtcAdmin.i().showMain();
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
	}
	

	
	@FXML
	private void onEmpMerge(ActionEvent event) {
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mAccount;
			DataManager.i().mCurrentCoreDataType = SystemDataType.ACCOUNT;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employee/ViewEmployeeMerge.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        EtcAdmin.i().showMain();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	}	
	
	@FXML
	private void onShowInactiveEmployers() {
		showEmployers();
	}

	@FXML 
	private void onCalcIrs10945BC(ActionEvent event) 
	{ 
		if (actCalcTaxYearCombo.getSelectionModel().getSelectedItem() == null) {
		    Utils.showAlert("No Tax year Selected", "Please select a tax year first.");
			return;
		}
		
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Call Codes");
	    String confirmText = "Are You Sure You Want To Call Codes for all employers of account ";
	    confirmText += DataManager.i().mAccount.getName();
	    confirmText +=" for tax year ";
	    confirmText += actCalcTaxYearCombo.getSelectionModel().getSelectedItem();
	    confirmText += "?";
	    confirmAlert.setContentText(confirmText);
	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();
	    if ((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	EtcAdmin.i().showAppStatus("", "", 0, false);
	    	return;
	    }

	    if(DataManager.i().mAccount.getEmployers() == null) return;

	    // create a task to free up the gui thread
	    Task<Void> task = new Task<Void>() {
		    @Override protected Void call() throws Exception {
				try {
				    boolean completed = true;
				    boolean badTaxYear = false;
				    String name = "";
					CalcQueueData calcData = new CalcQueueData(); 
					//iterate through all the account's employers
					double counter = 0;
					double step = 1.0 / DataManager.i().mAccount.getEmployers().size();
					for (Employer employer : DataManager.i().mAccount.getEmployers()) {
						
						// skip over any inactive employers
						if (employer.isActive() == false) continue;
						
						TaxYear taxYear = null;
						// copy over the employer for use elsewhere
						DataManager.i().mEmployer = employer;
						// get the employer tax years
			        	TaxYearRequest tyReq = new TaxYearRequest(); 
			    		tyReq.setEmployerId(employer.getId());
							employer.setTaxYears(AdminPersistenceManager.getInstance().getAll(tyReq));
			
						// select the tax year
						if (actCalcTaxYearCombo.getSelectionModel().getSelectedItem() != null) {
							for (TaxYear ty : employer.getTaxYears()) {
								if (ty.getYear() == Integer.valueOf(actCalcTaxYearCombo.getSelectionModel().getSelectedItem())){
									taxYear = ty;
									break;
								}
							}
						}
						
						// didn't match, go on to the next one
						if (taxYear == null) continue;
						
						// condition 1 - there is a dissolved date before the current tax year
						if (employer.getDissolvedOn() != null && employer.getDissolvedOn().get(Calendar.YEAR) < taxYear.getYear()) continue;
						// condition 2 - tax LOS of 26 indicates that they are dissolved, skip them.
						if (taxYear.getServiceLevelId() == 26) continue;
						if (taxYear.getServiceLevel() != null && taxYear.getServiceLevel().isGenerateCodes() == false) continue;
						
						//check for an incomplete taxyear - if not found, skip this employer
						if (taxYear.getServiceLevelId() == null) {
							name = employer.getName();
							completed = false;
							badTaxYear = true;	
							continue;
						}
						
						// and create using the tax year
						counter++;
						EtcAdmin.i().showAppStatus("Calling codes", "Processing Employer: " + employer.getName(), counter * step, true);

						// look at the tax yuear service level spec to determine b or c
						//if (employer.getIrs1094bs().size() > 0) {  
						if (taxYear.getServiceLevel() != null && taxYear.getServiceLevel().getSpecificationId() == 2l) {
							if (calcData.createIrs1094BCalculation(DataManager.i().mLocalUser, taxYear, true) == false) {
								name = employer.getName();
								completed = false;
							}
						}
						else {
							if (calcData.createIrs1094CCalculation(DataManager.i().mLocalUser, taxYear, employer, null, true, null) == false) {
								name = employer.getName();
								completed = false;
							}
						}
					}
					
					if (completed == true){
						if (counter == 0)
							Utils.alertUser("Calling Codes Not Submitted", "There are no Employers eligible for Calling Codes for the selected tax year");
						else
							Utils.alertUser("Calling Codes Submitted", "Calling Codes for the employers have been submitted. Check the Cal Queue for details.");
					}else {
						if (badTaxYear == true)
							Utils.alertUser("Calling Codes Tax Year Error", "The selected tax year for the Employer \"" + name + "\" has no service level or other null data. Check the Calc queue, other employers may have been processed successfully.");
						else
							Utils.alertUser("Calling Codes Error", "There was a problem with calling codes for the Employer \"" + name + "\". Check the Calc Queue, other employers may have been processed successfully.");
					}
				} catch (CoreException e) {
					DataManager.i().log(Level.SEVERE, e);
					Utils.showAlert("Error", "There was a problem with calling codes. Please check the logs for more info.");
				}
			    catch (Exception e) {  
			    	DataManager.i().logGenericException(e); 
			    }

				EtcAdmin.i().showAppStatus("", "", 0, false);
		    	return null;
		    }
		};
		
		task.setOnFailed(e -> {  
	    	DataManager.i().log(Level.SEVERE,task.getException());
		});

		Thread thread = new Thread(task, "showAppStatus");
		thread.setDaemon(true);
		thread.start();		
	} 
 
	public class HBoxFileHistoryCell 
	{
		SimpleStringProperty user = new SimpleStringProperty();
		SimpleStringProperty dateTime = new SimpleStringProperty();
		SimpleStringProperty employer = new SimpleStringProperty();
		SimpleStringProperty description = new SimpleStringProperty();
		SimpleStringProperty status = new SimpleStringProperty();
		SimpleStringProperty result = new SimpleStringProperty();
		SimpleStringProperty fileType = new SimpleStringProperty();
		SimpleStringProperty units = new SimpleStringProperty();
		SimpleStringProperty records = new SimpleStringProperty();
		SimpleStringProperty specId = new SimpleStringProperty();
		SimpleStringProperty spec = new SimpleStringProperty();
		SimpleStringProperty mapperId = new SimpleStringProperty();
         int queueLocation = 0;

         TextField fieldState1 = new TextField();
         TextField fieldState2 = new TextField();
         TextField fieldState3 = new TextField();
         TextField fieldState4 = new TextField();
         TextField fieldState5 = new TextField();
         
         public String getUser() {
        	 return user.get();
         }
         
         public String getDateTime() {
        	 return dateTime.get();
         }
         
         public String getEmployer() {
        	 return employer.get();
         }
         
         public String getDescription() {
        	 return description.get();
         }
         
         public String getStatus() {
        	 return status.get();
         }
         
         public String getResult() {
        	 return result.get();
         }
         
         public String getFileType() {
        	 return fileType.get();
         }
         
         public String getUnits() {
        	 return units.get();
         }
         
         public String getRecords() {
        	 return records.get();
         }
         
         public String getSpecId() {
        	 return specId.get();
         }
         
         public String getSpec() {
        	 return spec.get();
         }
         
         public String getMapperId() {
        	 return mapperId.get();
         }
         
         public TextField getFieldState1() {
        	 return fieldState1;
         }
         
         public TextField getFieldState2() {
        	 return fieldState2;
         }
         
         public TextField getFieldState3() {
        	 return fieldState3;
         }
         
         public TextField getFieldState4() {
        	 return fieldState4;
         }
         
         public TextField getFieldState5() {
        	 return fieldState5;
         }
         
         public int getQueueLocation() {
        	 return queueLocation;
         }
         
        String getDate() {return dateTime.get();}

         HBoxFileHistoryCell(int queueLocation, String user, 
        		 								String dateTime, 
        		 								String employer, 
        		 								String description, 
        		 								String units, 
        		 								String records,
        		 								String specId,
        		 								String spec,
        		 								String mapperId,
        		 								String status, 
        		 								String fileType, 
        		 								boolean state1, 
        		 								boolean state2, 
        		 								boolean state3, 
        		 								boolean state4, 
        		 								boolean state5, 
        		 								String result) {
              super();

              //save the requestId;
              this.queueLocation = queueLocation;
              
              if(user == null) user = "";
              if(dateTime == null ) dateTime = "";
              if(employer == null ) employer = "";
              if(description == null ) description = "";
              if(status == null ) status = "";
              if(result == null) result = "";
              if(fileType == null) fileType= "";
              if(units == null) units = "";
              if(records == null) records = "";
              if(specId == null) specId = "";
              if(spec == null) spec = "";
              if(mapperId == null) mapperId = "";
              
              this.user.set(user);
              this.dateTime.set(dateTime);
              this.employer.set(employer);
              this.description.set(description);
              this.user.set(user);
              this.units.set(units);
              this.records.set(records);
              this.specId.set(specId);
              this.spec.set(spec);
              this.mapperId.set(mapperId);
              this.fileType.set("   " + fileType);
              this.status.set(status);
              this.result.set(result);

             // setFieldStateAttributes(fieldState1, state1);
             // setFieldStateAttributes(fieldState2, state2);
             // setFieldStateAttributes(fieldState3, state3);
             // setFieldStateAttributes(fieldState4, state4);
             // setFieldStateAttributes(fieldState5, state5);

             //Tooltip.install(this, new Tooltip(result)); 
         }

    }	
	
	public static class HBoxEmployerCell extends HBox 
	{
         Label lblId = new Label();
         Label lblName = new Label();
         Label lblAddress = new Label();
         Label lblBornOn = new Label();
         Employer employer;
         
         Employer getEmployer() {
        	 return employer;
         }

         HBoxEmployerCell(Employer employer) 
         {
              super();
              this.employer = employer;
              
           	  if (employer != null) {
               	  Utils.setHBoxLabel(lblId, 75, false, employer.getId());
               	  Utils.setHBoxLabel(lblName, 350, false, employer.getName());
	           	  Utils.setHBoxLabel(lblAddress, 150, false, employer.getIncorporatedOn());
	           	  Utils.setHBoxLabel(lblBornOn, 150, false, employer.getDissolvedOn());
               
	           	  if(employer.isDeleted() == true) 
               	  {
                	  lblId.setTextFill(Color.RED);
                	  lblName.setTextFill(Color.RED);
                	  lblBornOn.setTextFill(Color.RED);
                	  lblAddress.setTextFill(Color.RED);
                  } else {
                	  if(employer.isActive() == false) 
                	  {
    	            	  lblId.setTextFill(Color.BLUE);
    	            	  lblName.setTextFill(Color.BLUE);
    	            	  lblBornOn.setTextFill(Color.BLUE);
    	            	  lblAddress.setTextFill(Color.BLUE);
    	              } else {
    	            	  lblId.setTextFill(Color.BLACK);
    	            	  lblName.setTextFill(Color.BLACK);
    	            	  lblBornOn.setTextFill(Color.BLACK);
    	            	  lblAddress.setTextFill(Color.BLACK);
    	              }
                  }
          	  } else {
               	  Utils.setHBoxLabel(lblId, 75, true, "Id");
               	  Utils.setHBoxLabel(lblName, 350, true, "Name");
	           	  Utils.setHBoxLabel(lblAddress, 150, true, "Incorp Date");
	           	  Utils.setHBoxLabel(lblBornOn, 150, true, "Dissolved Date");           		  
           	  }
           	  
        	  this.getChildren().addAll(lblId, lblName, lblAddress, lblBornOn);
         }
    }	

	public static class HBoxSpecCell extends HBox 
	{
        Label lblRequest = new Label();
        Label lblSpecificationId = new Label();
        Label lblSpecification = new Label();
        Label lblMapperId = new Label();
        Label lblMapper = new Label();

        public String getSpecId() { return lblSpecificationId.getText(); } 
        
        HBoxSpecCell(String request, String specId, String spec, String mapperId, String mapper, boolean headerRow) 
        {
             super();

             if(request == null) request = "";
             if(specId == null) specId = "";
             if(spec == null) spec = "";
             if(mapperId == null) mapperId = "";
             if(mapper == null) mapper = "";
             
             lblRequest.setText(request);
             lblRequest.setMinWidth(350);
             lblRequest.setMaxWidth(350);
             lblRequest.setPrefWidth(350);
             HBox.setHgrow(lblRequest, Priority.ALWAYS);

             lblSpecificationId.setText(specId);
             lblSpecificationId.setMinWidth(100);
             lblSpecificationId.setMaxWidth(100);
             lblSpecificationId.setPrefWidth(100);
             HBox.setHgrow(lblSpecificationId, Priority.ALWAYS);

             lblSpecification.setText(spec);
             lblSpecification.setMinWidth(350);
             lblSpecification.setMaxWidth(350);
             lblSpecification.setPrefWidth(350);
             HBox.setHgrow(lblSpecification, Priority.ALWAYS);

             lblMapperId.setText(mapperId);
             lblMapperId.setMinWidth(100);
             lblMapperId.setMaxWidth(100);
             lblMapperId.setPrefWidth(100);
             HBox.setHgrow(lblMapperId, Priority.ALWAYS);

             lblMapper.setText(mapper);
             lblMapper.setMinWidth(350);
             lblMapper.setMaxWidth(350);
             lblMapper.setPrefWidth(350);
             HBox.setHgrow(lblMapper, Priority.ALWAYS);
             
          	 if(headerRow == true) 
          	 {
          		 lblRequest.setTextFill(Color.GREY);
          		 lblSpecificationId.setTextFill(Color.GREY);
          		 lblSpecification.setTextFill(Color.GREY);
          		 lblMapperId.setTextFill(Color.GREY);
          		 lblMapper.setTextFill(Color.GREY);
             } 
          	 this.getChildren().addAll(lblRequest, lblSpecificationId, lblSpecification, lblMapperId, lblMapper);
        }
   }	

	//extending the listview for our contacts
	public static class HBoxContactCell extends HBox 
	{
         Label lblName = new Label();
         Label lblPhone = new Label();
         Label lblEmail = new Label();

         HBoxContactCell(String sName, String sPhone, String sEmail, boolean headerRow) 
         {
              super();

              if(sName == null ) sName = "";
              if(sPhone == null ) sPhone = "";
              if(sEmail == null ) sEmail = "";
              
              lblName.setText(sName);
              lblName.setMinWidth(280);
              lblName.setMaxWidth(280);
              lblName.setPrefWidth(280);
              HBox.setHgrow(lblName, Priority.ALWAYS);

              lblPhone.setText(sPhone);
              lblPhone.setMinWidth(100);
              lblPhone.setMaxWidth(100);
              lblPhone.setPrefWidth(100);
              HBox.setHgrow(lblPhone, Priority.ALWAYS);

              lblEmail.setText(sEmail);
              lblEmail.setMinWidth(300);
              lblEmail.setMaxWidth(300);
              lblEmail.setPrefWidth(300);
              HBox.setHgrow(lblEmail, Priority.ALWAYS);

              if(headerRow == true) 
              {
            	  lblName.setTextFill(Color.GREY);
            	  lblPhone.setTextFill(Color.GREY);
            	  lblEmail.setTextFill(Color.GREY);
              }
        	  this.getChildren().addAll(lblName, lblPhone, lblEmail);
         }
    }	

	//extending the listview for our associated properties
	public static class HBoxAssociatedPropertyCell extends HBox 
	{
         Label lblName = new Label();
         Label lblValue = new Label();

         HBoxAssociatedPropertyCell(String sName, String sValue, boolean headerRow)
         {
              super();

              if(sName == null ) sName = "";
              if(sValue == null ) sValue = "";
              
              lblName.setText(sName);
              lblName.setMinWidth(300);
              lblName.setMaxWidth(300);
              lblName.setPrefWidth(300);
              HBox.setHgrow(lblName, Priority.ALWAYS);

              lblValue.setText(sValue);
              lblValue.setMinWidth(380);
              lblValue.setMaxWidth(380);
              lblValue.setPrefWidth(380);
              HBox.setHgrow(lblValue, Priority.ALWAYS);

              if(headerRow == true) 
              {
            	  lblName.setTextFill(Color.GREY);
            	  lblValue.setTextFill(Color.GREY);
              }
        	  this.getChildren().addAll(lblName, lblValue);
         }
    }	
	
	public class HBoxEmployeeCell extends HBox 
	{
        Label lblId = new Label();
        Label lblName = new Label();
        Label lblEmployer = new Label();
        Label lblLastUpdate = new Label();
        Label lblSSN = new Label();
        Employee employee;
        
        public String getName() {
        		return lblName.getText();
        }
        
        public Employee getEmployee() {
        	return employee;
        }
        
        HBoxEmployeeCell(Employee employee) 
        {
             super();
          
             this.employee = employee;

             if(employee != null && employee.getPerson() != null) 
             {
            	 Utils.setHBoxLabel(lblId, 100, false, employee.getId());
            	 Utils.setHBoxLabel(lblName,250, false, employee.getPerson().getLastName() + ", " + employee.getPerson().getFirstName());
            	 Utils.setHBoxLabel(lblLastUpdate, 150, false, employee.getLastUpdated());
                 if(employee.getPerson().getSsn() != null)
                	 Utils.setHBoxLabel(lblSSN, 150, false, employee.getPerson().getSsn().getUsrln());
                 if (employee.getEmployer() != null)
                	 Utils.setHBoxLabel(lblEmployer, 250, false, employee.getEmployer().getName());
                 if(employee.isDeleted() == true)
                 {
		           	  lblId.setTextFill(Color.RED);
		           	  lblName.setTextFill(Color.RED);
		           	  lblLastUpdate.setTextFill(Color.RED);
		           	  lblSSN.setTextFill(Color.RED);
		           	  lblEmployer.setTextFill(Color.RED);
                 } else {
	                 if(employee.isActive() == false)
	                 {
			           	  lblId.setTextFill(Color.BLUE);
			           	  lblName.setTextFill(Color.BLUE);
			           	  lblLastUpdate.setTextFill(Color.BLUE);
			           	  lblSSN.setTextFill(Color.BLUE);
			           	  lblEmployer.setTextFill(Color.BLUE);
	                 } else {
			           	  lblId.setTextFill(Color.BLACK);
			           	  lblName.setTextFill(Color.BLACK);
			           	  lblLastUpdate.setTextFill(Color.BLACK);
			           	  lblSSN.setTextFill(Color.BLACK);
			           	  lblEmployer.setTextFill(Color.BLACK);
	                 }
                 }
             } else {
            	 Utils.setHBoxLabel(lblId, 100, true, "Id");
            	 Utils.setHBoxLabel(lblName, 250, true, "Name");
            	 Utils.setHBoxLabel(lblEmployer, 250, true, "Employer");
            	 Utils.setHBoxLabel(lblLastUpdate, 150, true, "Last Update");
                 Utils.setHBoxLabel(lblSSN, 150, true, "SSN");
             }
             this.getChildren().addAll(lblId, lblName, lblEmployer, lblLastUpdate, lblSSN);
        }
   }	

	public class EmployeeCell
	{
		SimpleStringProperty id = new SimpleStringProperty();
		SimpleStringProperty name = new SimpleStringProperty();
		SimpleStringProperty employer = new SimpleStringProperty();
		SimpleStringProperty date = new SimpleStringProperty();
		SimpleStringProperty ssn = new SimpleStringProperty();
        Employee employee;
        
        public String getId() {
    		return id.get();
        }
    
        public String getName() {
        		return name.get();
        }
        
        public String getEmployer() {
    		return employer.get();
        }
    
        public String getDate() {
    		return date.get();
        }
    
        public String getSsn() {
    		return ssn.get();
        }
    
       public Employee getEmployee() {
        	return employee;
        }
        
        EmployeeCell(Employee employee) 
        {
             super();
          
             this.employee = employee;

             if(employee != null && employee.getPerson() != null) 
             {
            	 id.set(String.valueOf(employee.getId()));
            	 name.set(employee.getPerson().getLastName() + ", " + employee.getPerson().getFirstName());
        		 date.set(Utils.getDateString(employee.getLastUpdated()));
                 if(employee.getPerson().getSsn() != null)
                	 ssn.set(employee.getPerson().getSsn().getUsrln());
                 if (employee.getEmployer() != null)
                	 employer.set(employee.getEmployer().getName());
             }
        }
   }	
}
