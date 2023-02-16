package com.etc.admin.ui.employer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.account.FileHistoryData;
import com.etc.admin.ui.callcodes.ViewCodeCallHistoryController;
import com.etc.admin.ui.notes.ViewNotesController;
import com.etc.admin.ui.pipeline.queue.PipelineQueue;
import com.etc.admin.ui.plancoverageclass.ViewPlanCoverageClassController;
import com.etc.admin.utils.AddressGrid;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.CoverageGroup;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.EmployerContact;
import com.etc.corvetto.entities.Person;
import com.etc.corvetto.entities.TaxYear;
import com.etc.corvetto.rqs.CoverageGroupRequest;
import com.etc.corvetto.rqs.DepartmentRequest;
import com.etc.corvetto.rqs.EmployerContactRequest;
import com.etc.corvetto.rqs.EmployerRequest;
import com.etc.corvetto.rqs.LocationRequest;
import com.etc.corvetto.rqs.PayPeriodRequest;
import com.etc.corvetto.rqs.PlanYearRequest;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewEmployerController 
{
	// command toolbar
	@FXML
	private Button tbPayPeriodsButton;
	
	@FXML
	private GridPane emprNameGrid;
	@FXML
	private GridPane emprMailGrid;
	@FXML
	private GridPane emprBillGrid;
	@FXML
	private Label emprHeaderLabel;
	//@FXML
	//private Label emprTopLabel;
	@FXML
	private TextField emprNameLabel;
	@FXML
	private TextField emprPhoneLabel;
	@FXML
	private TextField emprEmailLabel;
	@FXML
	private ChoiceBox<PhoneType> emprPhoneTypeChoice;
	@FXML
	private ChoiceBox<EmailType> emprEmailTypeChoice;
	@FXML
	private ChoiceBox<TimezoneType> emprTimezoneChoice;
	@FXML
	private TextField emprTINLabel;
	@FXML
	private DatePicker emprIncorporatedOnPicker;
	@FXML
	private DatePicker emprDissolvedOnPicker;
	@FXML
	private Button emprEditButton;
	@FXML
	private Button emprSaveButton;
	// tax years
	@FXML
	private Label emprTaxYearsLabel;
	@FXML
	private Button emprAddTaxYearButton;
	@FXML
	private TableView<TaxYearCell> emprTaxYearsTableView;
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	//table sort 
	TableColumn<HBoxCell, String> sortColumn = null;

	//table sort tax year
	TableColumn<TaxYearCell, String> tySortColumn = null;

	//file history queue
	List<PipelineQueue> fileHistoryQueue;	
	// file history data object
	FileHistoryData fileHistoryData = new FileHistoryData();
	
	//selected fileHistory queue
	PipelineQueue selectedFileHistoryQueue = null;
	
	//address grids
	AddressGrid mailAddress = new AddressGrid();
	AddressGrid billAddress = new AddressGrid();
	
	//using this for tracking concurrent threads completing, in order to properly end loading bar.
	private int waitingToComplete = 0;
	
	private Logger logr;
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize()
	{
		try {
			logr = Logger.getLogger(this.getClass().getCanonicalName());
			initContols();
			// worker task to update the screen
			updateEmployer();
		}catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		}
	}
	
	private void initContols() 
	{
		// disable toolbar buttons until data thread finishes
		tbPayPeriodsButton.setDisable(true);

		//clear the existing lists while the new data loads
		clearScreen();
		setEditMode(false);
		setTaxYearTableColumns();

		//phone type
		ObservableList<PhoneType> phoneTypes = FXCollections.observableArrayList(PhoneType.values());
		emprPhoneTypeChoice.setItems(phoneTypes);
		
		//email type
		ObservableList<EmailType> emailTypes = FXCollections.observableArrayList(EmailType.values());
		emprEmailTypeChoice.setItems(emailTypes);

		//timezone type
		ObservableList<TimezoneType> timezoneTypes = FXCollections.observableArrayList(TimezoneType.values());
		emprTimezoneChoice.setItems(timezoneTypes);

		
		// TAX YEARS
		emprTaxYearsTableView.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				 viewSelectedTaxYear(false);
            }
        });

		//set functionality according to the user security level
		emprEditButton.setDisable(!Utils.userCanEdit());
		emprAddTaxYearButton.setDisable(!Utils.userCanAdd());
	}
	
	private void updateEmployer() 
	{
		try
		{
			if(DataManager.i().mEmployer == null) return;
			
			// new thread
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception 
	            {
	            	EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Employer Data...");
	            	//REFRESH EMPLOYER IF IT IS NOT FULLY LOADED
	            	if(DataManager.i().mEmployer.isPartialLoad())
	            		DataManager.i().mEmployer = AdminPersistenceManager.getInstance().get(Employer.class, DataManager.i().mEmployer.getId());
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {
	      		waitingToComplete++;
	      	});
	      			
	    	task.setOnSucceeded(e ->  {
	    		waitingToComplete--;
	    		//kick off all employer dependent tasks
				EtcAdmin.i().setProgress(0.0);
	    		showEmployer();
	    		updateCoverageGroups();
	    		updateEmployerPlanYears();
	    		updateContacts();
	    		updateTaxYears();
	    		updatePayPeriods();
	    		updateDepartments();
	    		updateLocations();
	    		updatePlanYears();
	    	});
	    	//SHOWS EMPLOYER
	    	task.setOnFailed(e ->  { 
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		EtcAdmin.i().setStatusMessage("Unable to Load Employer."); 
	    		});
	        
	    	EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e) { logr.log(Level.SEVERE, "Exception.",e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void updateCoverageGroups()
	{
		try
		{
			if(DataManager.i().mEmployer == null) return;
			
			Task<Void> task = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					
					EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Employer Coverage Groups...");
	          		CoverageGroupRequest cgRequest = new CoverageGroupRequest();
	          		cgRequest.setEmployerId(DataManager.i().mEmployer.getId());
	          		DataManager.i().mEmployer.setCoverageGroups(AdminPersistenceManager.getInstance().getAll(cgRequest));
	          		
					return null;
				}
				
			};
			
			task.setOnScheduled(e -> {
				waitingToComplete++;
			});
			task.setOnSucceeded(e -> {
				EtcAdmin.i().setProgress(0.0);
				//showCoverageGroups();
			});
			task.setOnFailed(e -> { 
				EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
				EtcAdmin.i().setStatusMessage("Unable to Load Employer Coverage Groups."); 
				});
			
			EmsApp.getInstance().getFxQueue().put(task);
			
		}catch(InterruptedException e) { logr.log(Level.SEVERE, "Exception.",e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void updateEmployerPlanYears()
	{
		try
		{
			Task<Void> task = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Employer Plan Years...");
	          		PlanYearRequest pyRequest = new PlanYearRequest();
	          		pyRequest.setEmployerId(DataManager.i().mEmployer.getId());
	          		DataManager.i().mEmployer.setPlanYears(AdminPersistenceManager.getInstance().getAll(pyRequest));
					
					return null;
				}
				
			};
			
			task.setOnScheduled(e -> {
				waitingToComplete++;
			});
			task.setOnSucceeded(e -> {
				EtcAdmin.i().setProgress(0.0);
			});
			task.setOnFailed(e -> { 
				EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
				EtcAdmin.i().setStatusMessage("Unable to Load Employer Plan Years."); 
			});
			
			EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e) { logr.log(Level.SEVERE, "Exception.",e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void updateContacts() 
	{
		try
		{
			// create a thread to handle the update
			Task<Void> task = new Task<Void>()
			{
	            @Override
	            protected Void call() throws Exception 
	            {
	          		EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Employer Contacts...");
	          		EmployerContactRequest contactRequest = new EmployerContactRequest();
	          		contactRequest.setEmployerId(DataManager.i().mEmployer.getId());
	          		DataManager.i().mEmployer.setContacts(AdminPersistenceManager.getInstance().getAll(contactRequest));
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {waitingToComplete++;});	
	    	task.setOnSucceeded(e ->  {
				EtcAdmin.i().setProgress(0.0);
	    	});
	    	task.setOnFailed(e ->  { 
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		EtcAdmin.i().setStatusMessage("Unable to Load Employer Contacts."); 
	    		});
	        
	    	EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e)
		{
			
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void updateTaxYears() 
	{
		try
		{
			emprTaxYearsTableView.getItems().clear();
			emprTaxYearsLabel.setText("Tax Years (loading ...)"); 
			// create a thread to handle the update
			Task<Void> task = new Task<Void>()
			{
	            @Override
	            protected Void call() throws Exception 
	            {
	            	EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Employer TaxYears...");
	            	TaxYearRequest request = new TaxYearRequest();
	            	request.setEmployerId(DataManager.i().mEmployer.getId());
	            	DataManager.i().mEmployer.setTaxYears(AdminPersistenceManager.getInstance().getAll(request));
	            	
	            	List<Long> erContactIds = new ArrayList<Long>();
	            	List<EmployerContact> contacts = null;
	            	
	            	for(TaxYear ty : DataManager.i().mEmployer.getTaxYears())
	            		if(ty.getIrsContactId() == null) continue;
	            		else 
	            			erContactIds.add(ty.getIrsContactId());
	            	if(!erContactIds.isEmpty())
	            	{
	            		EmployerContactRequest ecr = new EmployerContactRequest();
		            	ecr.setIdList(erContactIds);
		            	contacts = AdminPersistenceManager.getInstance().getAll(ecr);
		            	for(TaxYear ty : DataManager.i().mEmployer.getTaxYears())
		            		if(ty.getIrsContactId() == null) continue;
		            		else
		            			for(EmployerContact ec : contacts)
			            			if(ec.getId().equals(ty.getIrsContactId()))
			            			{
			            				ty.setIrsContact(ec);
			            				break;
			            			}
	            	}
	            			
	            		
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {waitingToComplete++;});
	    	task.setOnSucceeded(e ->  {
				EtcAdmin.i().setProgress(0.0);
	    		showTaxYears();
			});
	    	task.setOnFailed(e ->  { 
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		EtcAdmin.i().setStatusMessage("Unable to Load Employer TaxYears."); 
	    		});
	       
	    	EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e) { logr.log(Level.SEVERE, "Exception.",e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void updatePayPeriods()
	{
		try
		{
			// create a thread to handle the update
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception 
	            {
	            	EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Employer PayPeriods...");
	          		PayPeriodRequest ppRequest = new PayPeriodRequest();
	          		ppRequest.setFetchInactive(true);
	          		ppRequest.setEmployerId(DataManager.i().mEmployer.getId());
	          		DataManager.i().mEmployer.setPayPeriods(AdminPersistenceManager.getInstance().getAll(ppRequest));
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {waitingToComplete++;});		
	    	task.setOnSucceeded(e ->  {
				EtcAdmin.i().setProgress(0.0);
	    		tbPayPeriodsButton.setDisable(false);
	    	});
	    	task.setOnFailed(e ->  { 
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		EtcAdmin.i().setStatusMessage("Unable to Load Employer Pay Period."); 
	    	});
	    	
	        EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e) { 
			logr.log(Level.SEVERE, "Exception.",e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void updateEmployerEligibilityPeriods() 
	{
		// create a thread to handle the update
		Task<Void> task = new Task<Void>() 
		{
            @Override
            protected Void call() throws Exception 
            {
                return null;
            }
        };
        
    	task.setOnSucceeded(e ->  {
    		EtcAdmin.i().setProgress(0.0);
    		updateLocations();
    	});
    	task.setOnFailed(e ->  {
	    	DataManager.i().log(Level.SEVERE,task.getException());
    	});
        new Thread(task).start();
	}
	
	private void updateLocations() 
	{
		// create a thread to handle the update
		try
		{
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception
	            {
	            	EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Employer Locations...");
	          		LocationRequest locationRequest = new LocationRequest();
	          		locationRequest.setEmployerId(DataManager.i().mEmployer.getId());
	          		DataManager.i().mEmployer.setLocations(AdminPersistenceManager.getInstance().getAll(locationRequest));
	          		
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {});
	    	task.setOnSucceeded(e ->  {
	    		EtcAdmin.i().setProgress(0.0);
	    	});
	    	task.setOnFailed(e ->  { 
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		EtcAdmin.i().setStatusMessage("Unable to Load Employer Locations."); 
	    		});
	    	
	        EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e) { logr.log(Level.SEVERE, "Exception.",e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void updateDepartments() 
	{
		try
		{
			// create a thread to handle the update
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception
	            {
	            	EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Employer Departments...");
	            	// get any updated from the server
	            	DepartmentRequest request = new DepartmentRequest();
	            	request.setEmployerId(DataManager.i().mEmployer.getId());
	            	DataManager.i().mEmployer.setDepartments(AdminPersistenceManager.getInstance().getAll(request));
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {});
	    	task.setOnSucceeded(e ->  {EtcAdmin.i().setProgress(0.0);});
	    	task.setOnFailed(e ->  { 
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		EtcAdmin.i().setStatusMessage("Unable to Load Employer Departments."); 
	    		});
	    	
	        EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e) { logr.log(Level.SEVERE, "Exception.",e); }
	}
	
	private void updatePlanYears() 
	{	
		try
		{
			// create a thread to handle the update
			Task<Void> task = new Task<Void>()
			{
	            @Override
	            protected Void call() throws Exception 
	            {
	            	try {
	            	EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Employer PlanYears...");
	            	PlanYearRequest rqs = new PlanYearRequest();
	            	rqs.setEmployerId(DataManager.i().mEmployer.getId());
	            	DataManager.i().mEmployer.setPlanYears(AdminPersistenceManager.getInstance().getAll(rqs));
	                return null;
	            	} catch (Exception e) {
	            		DataManager.i().log(Level.SEVERE,e);
	            		return null;
	            	}
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {});
	    	task.setOnSucceeded(e ->  {
	    		EtcAdmin.i().setProgress(0.0); 
	    	});
	    	task.setOnFailed(e ->  { 
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		EtcAdmin.i().setStatusMessage("Unable to Load Employer PlanYears."); 
	    	});
	    	
	        EmsApp.getInstance().getFxQueue().put(task);
		}catch(Exception e) { 
			logr.log(Level.SEVERE, "Exception.",e); 
		}
	}
	
	private void clearScreen() 
	{
		//emprTopLabel.setText(" ");
		emprNameLabel.setText("");
		emprPhoneLabel.setText("");
		emprEmailLabel.setText("");
		//emprPhoneTypeChoice.getItems().set;
		//emprEmailTypeChoice.setText("");
		//emprTimezoneChoice.setText("");
		emprTINLabel.setText("");
		emprIncorporatedOnPicker.setValue(null);
		emprDissolvedOnPicker.setValue(null);
		emprTaxYearsLabel.setText("Tax Years");
		
		// ListViews
		emprTaxYearsTableView.getItems().clear();
	}

	public void setEmployerPayFile(int employerPayFileIndex) 
	{
		//verify that we have employer payfiles
		if(DataManager.i().mEmployerPayFiles == null || DataManager.i().mEmployerPayFiles.size() == 0) {
			return;
		}
		
		DataManager.i().mEmployerPayFileIndex = employerPayFileIndex;
		DataManager.i().mPipelinePayFile = DataManager.i().mEmployerPayFiles.get(DataManager.i().mEmployerPayFileIndex);
		EtcAdmin.i().setScreen(ScreenType.EMPLOYERPAYFILE);
	}

	private void showEmployer()
	{
		if(DataManager.i().mEmployer != null) 
		{
			Employer emp = DataManager.i().mEmployer;
			Utils.setActiveHeader(emprHeaderLabel,emp.isActive());

			Utils.updateControl(emprNameLabel,DataManager.i().mEmployer.getName());
			Utils.updateControl(emprPhoneLabel,DataManager.i().mEmployer.getPhone());
			Utils.updateControl(emprEmailLabel,DataManager.i().mEmployer.getEmail());
			Utils.updateControl(emprTINLabel,DataManager.i().mEmployer.getTIN());

			if(DataManager.i().mEmployer.getPhoneType() != null) 
				emprPhoneTypeChoice.getSelectionModel().select(DataManager.i().mEmployer.getPhoneType());
			
			if(DataManager.i().mEmployer.getEmailType() != null) 
				emprEmailTypeChoice.getSelectionModel().select(DataManager.i().mEmployer.getEmailType());
			
			// DATES
			Utils.updateControl(emprIncorporatedOnPicker, DataManager.i().mEmployer.getIncorporatedOn());
			Utils.updateControl(emprDissolvedOnPicker, DataManager.i().mEmployer.getDissolvedOn());

			// create the addresses
			mailAddress.createAddress(emprMailGrid, DataManager.i().mEmployer.getMailAddress(), "Mail Address");
			billAddress.createAddress(emprBillGrid, DataManager.i().mEmployer.getBillAddress(), "Billing Address");
			
			// TIME ZONE
			if(DataManager.i().mEmployer.getTimezone() != null) 
				emprTimezoneChoice.getSelectionModel().select(DataManager.i().mEmployer.getTimezone()); 
		}
	}
	
	private boolean validateData() 
	{
		boolean bReturn = true;
		//address
		if(mailAddress.validateData() == false) bReturn = false;
		if(billAddress.validateData() == false) bReturn = false;
		//int
		if( !Utils.validateIntTextField(emprTINLabel)) bReturn = false;
						
		//phone
		if( !Utils.validatePhoneTextField(emprPhoneLabel)) bReturn = false;		
		
		//email
		if(!Utils.validateEmailTextField(emprEmailLabel)) bReturn = false;

		return bReturn;
	}
	
	private void updateEmployerFromScreen() 
	{
		//employer should never be null at this point
		if(DataManager.i().mEmployer != null) {
			
			// General data from the interface
			DataManager.i().mEmployer.setName(emprNameLabel.getText());
			DataManager.i().mEmployer.setTIN(emprTINLabel.getText());
			DataManager.i().mEmployer.setPhone(emprPhoneLabel.getText());
			DataManager.i().mEmployer.setPhoneType(emprPhoneTypeChoice.getValue());
			DataManager.i().mEmployer.setEmail(emprEmailLabel.getText());			
			DataManager.i().mEmployer.setEmailType(emprEmailTypeChoice.getValue());
			DataManager.i().mEmployer.setTimezone(emprTimezoneChoice.getValue()); 
			
			// The dates from the pickers, if available
			if (emprIncorporatedOnPicker.getValue() == null)
				DataManager.i().mEmployer.setIncorporatedOn(null);
			else
				DataManager.i().mEmployer.setIncorporatedOn(Utils.getCalDate(emprIncorporatedOnPicker.getValue()));
			
			if (emprDissolvedOnPicker.getValue() == null)
				DataManager.i().mEmployer.setDissolvedOn(null);
			else
				DataManager.i().mEmployer.setDissolvedOn(Utils.getCalDate(emprDissolvedOnPicker.getValue()));
			
			// MAIL ADDRESS
			if (DataManager.i().mEmployer.getMailAddress() != null && mailAddress.getUpdatedAddress() != null ) {
				DataManager.i().mEmployer.getMailAddress().setStreet(mailAddress.getUpdatedAddress().getStreet());
				DataManager.i().mEmployer.getMailAddress().setUnit(mailAddress.getUpdatedAddress().getUnit());
				DataManager.i().mEmployer.getMailAddress().setCity(mailAddress.getUpdatedAddress().getCity());
				DataManager.i().mEmployer.getMailAddress().setStprv2(mailAddress.getUpdatedAddress().getStprv2());
				DataManager.i().mEmployer.getMailAddress().setZip(mailAddress.getUpdatedAddress().getZip());
				DataManager.i().mEmployer.getMailAddress().setZp4(mailAddress.getUpdatedAddress().getZp4());
				DataManager.i().mEmployer.getMailAddress().setDepartment(mailAddress.getUpdatedAddress().getDepartment());
				DataManager.i().mEmployer.getMailAddress().setQuarter(mailAddress.getUpdatedAddress().getQuarter());
				DataManager.i().mEmployer.getMailAddress().setProvince(mailAddress.getUpdatedAddress().getProvince());
				DataManager.i().mEmployer.getMailAddress().setCountry(mailAddress.getUpdatedAddress().getCountry());
			}
			
			// MAIL ADDRESS
			if (DataManager.i().mEmployer.getBillAddress() != null && billAddress.getUpdatedAddress() != null ) {
				DataManager.i().mEmployer.getBillAddress().setStreet(billAddress.getUpdatedAddress().getStreet());
				DataManager.i().mEmployer.getBillAddress().setUnit(billAddress.getUpdatedAddress().getUnit());
				DataManager.i().mEmployer.getBillAddress().setCity(billAddress.getUpdatedAddress().getCity());
				DataManager.i().mEmployer.getBillAddress().setStprv2(billAddress.getUpdatedAddress().getStprv2());
				DataManager.i().mEmployer.getBillAddress().setZip(billAddress.getUpdatedAddress().getZip());
				DataManager.i().mEmployer.getBillAddress().setZp4(billAddress.getUpdatedAddress().getZp4());
				DataManager.i().mEmployer.getBillAddress().setDepartment(billAddress.getUpdatedAddress().getDepartment());
				DataManager.i().mEmployer.getBillAddress().setQuarter(billAddress.getUpdatedAddress().getQuarter());
				DataManager.i().mEmployer.getBillAddress().setProvince(billAddress.getUpdatedAddress().getProvince());
				DataManager.i().mEmployer.getBillAddress().setCountry(billAddress.getUpdatedAddress().getCountry());
			}
			
		}
	}
	
	//update the tax years list
	private void showTaxYears() 
	{
		emprTaxYearsTableView.getItems().clear();
		if(DataManager.i().mEmployer.getTaxYears() != null && DataManager.i().mEmployer.getTaxYears().size() > 0)
		{
		    for(TaxYear taxYear : DataManager.i().mEmployer.getTaxYears()) 
		    {
		    	emprTaxYearsTableView.getItems().add(new TaxYearCell(taxYear));
		    }
		    
		    emprTaxYearsTableView.getSortOrder().add(tySortColumn);
		    tySortColumn.setSortable(true);
		    tySortColumn.setSortType(SortType.DESCENDING);
			//update our Pays label
	        emprTaxYearsLabel.setText("Tax Years (total: " + String.valueOf(DataManager.i().mEmployer.getTaxYears().size()) + ") - yellow indicates closed");
		} else
			emprTaxYearsLabel.setText("Tax Years (total: 0)");			
	}
	
	private void setTaxYearTableColumns() 
	{
		//clear the default values
		emprTaxYearsTableView.getColumns().clear();

	    TableColumn<TaxYearCell, String> x1 = new TableColumn<TaxYearCell, String>("Tax Year");
		x1.setCellValueFactory(new PropertyValueFactory<TaxYearCell,String>("year"));
		x1.setMinWidth(90);
		emprTaxYearsTableView.getColumns().add(x1);
		setCellTaxYearFactory(x1);
		tySortColumn = x1;

		TableColumn<TaxYearCell, String> x2 = new TableColumn<TaxYearCell, String>("Print Option");
		x2.setCellValueFactory(new PropertyValueFactory<TaxYearCell,String>("printOption"));
		x2.setMinWidth(125);
	    emprTaxYearsTableView.getColumns().add(x2);
		setCellTaxYearFactory(x2);

	    TableColumn<TaxYearCell, String> x3 = new TableColumn<TaxYearCell, String>("Form Type");
	    x3.setCellValueFactory(new PropertyValueFactory<TaxYearCell,String>("formType"));
	    x3.setMinWidth(105);
	    emprTaxYearsTableView.getColumns().add(x3);
		setCellTaxYearFactory(x3);
		
		TableColumn<TaxYearCell, String> x4 = new TableColumn<TaxYearCell, String>("IRS Contact");
	    x4.setCellValueFactory(new PropertyValueFactory<TaxYearCell,String>("irsContact"));
	    x4.setMinWidth(175);
		emprTaxYearsTableView.getColumns().add(x4);
		setCellTaxYearFactory(x4);

		TableColumn<TaxYearCell, String> x5 = new TableColumn<TaxYearCell, String>("Phone");
	    x5.setCellValueFactory(new PropertyValueFactory<TaxYearCell,String>("phone"));
	    x5.setMinWidth(125);
		emprTaxYearsTableView.getColumns().add(x5);
		setCellTaxYearFactory(x5);

		TableColumn<TaxYearCell, String> x6 = new TableColumn<TaxYearCell, String>("CLA");
	    x6.setCellValueFactory(new PropertyValueFactory<TaxYearCell,String>("cla"));
	    x6.setMinWidth(125);
		emprTaxYearsTableView.getColumns().add(x6);
		setCellTaxYearFactory(x6);

		TableColumn<TaxYearCell, String> x7 = new TableColumn<TaxYearCell, String>("Support");
	    x7.setCellValueFactory(new PropertyValueFactory<TaxYearCell,String>("support"));
	    x7.setMinWidth(175);
		emprTaxYearsTableView.getColumns().add(x7);
		setCellTaxYearFactory(x7);

		TableColumn<TaxYearCell, String> x8 = new TableColumn<TaxYearCell, String>("Svc Level");
	    x8.setCellValueFactory(new PropertyValueFactory<TaxYearCell,String>("svcLevel"));
	    x8.setMinWidth(150);
		emprTaxYearsTableView.getColumns().add(x8);
		setCellTaxYearFactory(x8);

		TableColumn<TaxYearCell, String> x9 = new TableColumn<TaxYearCell, String>("Calculator");
	    x9.setCellValueFactory(new PropertyValueFactory<TaxYearCell,String>("calculator"));
	    x9.setMinWidth(85);
		emprTaxYearsTableView.getColumns().add(x9);
		setCellTaxYearFactory(x9);

		TableColumn<TaxYearCell, String> x10 = new TableColumn<TaxYearCell, String>("Pre Proc");
	    x10.setCellValueFactory(new PropertyValueFactory<TaxYearCell,String>("preProc"));
	    x10.setMinWidth(85);
		emprTaxYearsTableView.getColumns().add(x10);
		setCellTaxYearFactory(x10);

		TableColumn<TaxYearCell, String> x11 = new TableColumn<TaxYearCell, String>("V2 Status");
	    x11.setCellValueFactory(new PropertyValueFactory<TaxYearCell,String>("v2Status"));
	    x11.setMinWidth(85);
		emprTaxYearsTableView.getColumns().add(x11);
		setCellTaxYearFactory(x11);

		TableColumn<TaxYearCell, String> x12 = new TableColumn<TaxYearCell, String>("MEWA");
	    x12.setCellValueFactory(new PropertyValueFactory<TaxYearCell,String>("mewa"));
	    x12.setMinWidth(85);
		emprTaxYearsTableView.getColumns().add(x12);
		setCellTaxYearFactory(x12);

		TableColumn<TaxYearCell, String> x13 = new TableColumn<TaxYearCell, String>("In Legal?");
	    x13.setCellValueFactory(new PropertyValueFactory<TaxYearCell,String>("inLegal"));
	    x13.setMinWidth(85);
		emprTaxYearsTableView.getColumns().add(x13);
		setCellTaxYearFactory(x13);
		
	}
	
	private void setCellTaxYearFactory(TableColumn<TaxYearCell, String>  col) 
	{
		col.setCellFactory(column -> {
		    return new TableCell<TaxYearCell, String>() 
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
		                TaxYearCell cell = getTableView().getItems().get(getIndex());
		                if(cell.getTaxYear().isActive() == false)
		                	setTextFill(Color.BLUE);
		                else {
			                if(cell.getTaxYear().isClosed() == true)
			                	setTextFill(Color.DARKGOLDENROD);
			                else
			                	setTextFill(Color.BLACK);
		                }
		            }
	                //if(cell.getTaxYear().isClosed() == true)
	                //	setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
		        }
		    };
		});
	}

	private void setEditMode(boolean mode) 
	{
		if(mode == true) 
		{
			emprEditButton.setText("Cancel");
			emprNameGrid.setStyle("-fx-background-color: #eeffee");
			emprMailGrid.setStyle("-fx-background-color: #eeffee");
			emprBillGrid.setStyle("-fx-background-color: #eeffee");
		}else {
			emprEditButton.setText("Edit");
			emprNameGrid.getStyleClass().clear();
			emprNameGrid.setStyle(null);	
			emprMailGrid.getStyleClass().clear();
			emprMailGrid.setStyle(null);	
			emprBillGrid.getStyleClass().clear();
			emprBillGrid.setStyle(null);	
		}
		
		emprSaveButton.setVisible(mode);

		emprNameLabel.setEditable(mode);
		emprTINLabel.setEditable(mode);
		emprPhoneLabel.setEditable(mode);
		emprPhoneTypeChoice.setDisable(!mode);
		emprEmailLabel.setEditable(mode);
		emprEmailTypeChoice.setDisable(!mode);
		emprTimezoneChoice.setDisable(!mode);
		emprIncorporatedOnPicker.setDisable(!mode);
		emprIncorporatedOnPicker.getEditor().setDisable(!mode);
		emprDissolvedOnPicker.setDisable(!mode);
		emprDissolvedOnPicker.getEditor().setDisable(!mode);
		mailAddress.setEditMode(mode);
		billAddress.setEditMode(mode);
	}	
	
	@FXML
	private void onEdit(ActionEvent event) 
	{
		if(emprEditButton.getText().equals("Edit"))
			setEditMode(true);
		else
			setEditMode(false);
	}	

	@FXML
	private void onViewFiling(ActionEvent event) {
		viewFiling();
	}	
	
	@FXML
	private void onSave(ActionEvent event) 
	{
		try {
			//validate everything
			if(!validateData())
				return;
			
			//update our object
			updateEmployerFromScreen();
			
			//save it to the repository and the server
			EmployerRequest eReq = new EmployerRequest();
			eReq.setEntity(DataManager.i().mEmployer);
			eReq.setId(DataManager.i().mEmployer.getId());
			
			// clear flags
			if (DataManager.i().mEmployer.getIncorporatedOn() == null)
				eReq.setClearIncorporatedOn(true);
			if (DataManager.i().mEmployer.getDissolvedOn() == null)
				eReq.setClearDissolvedOn(true);		
			if (DataManager.i().mEmployer.getPhone() == null || DataManager.i().mEmployer.getPhone().isEmpty() == true)
				eReq.setClearPhone(true);
			if (DataManager.i().mEmployer.getPhoneType() == null)
				eReq.setClearPhoneType(true);
			if (DataManager.i().mEmployer.getEmail() == null || DataManager.i().mEmployer.getEmail().isEmpty() == true)
				eReq.setClearEmail(true);
			if (DataManager.i().mEmployer.getEmailType() == null)
				eReq.setClearEmailType(true);
			if (DataManager.i().mEmployer.getWebsite() == null || DataManager.i().mEmployer.getWebsite().isEmpty() == true)
				eReq.setClearWebsite(true);
			if (DataManager.i().mEmployer.getTimezone() == null)
				eReq.setClearTimezone(true);
			if (DataManager.i().mEmployer.getBillAddress() == null)
				eReq.setClearBillAddress(true);
			
			AdminPersistenceManager.getInstance().addOrUpdate(eReq);
		} catch (CoreException e) {
			DataManager.i().log(Level.SEVERE,e);
		}
	
		// reset the edit mode
		setEditMode(false);
	}	

	@FXML
	private void onAddContact(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.EMPLOYERCONTACTADD, true);
	}	

	@FXML
	private void onAddProperty(ActionEvent event) {
		//EtcAdmin.i().setScreen(ScreenType.EMPLOYEREDIT);
	}	

	@FXML
	private void onAddEmployerEligibilityPeriod(ActionEvent event) {
		//EtcAdmin.i().setScreen(ScreenType.EMPLOYERELIGIBILITYPERIODADD);
	}	

	@FXML
	private void onAddCoverageClass(ActionEvent event) {
		viewAddCoverageClass(); 
	}	

	@FXML
	private void onAddTaxYear(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.TAXYEARADD, true);
	}	

	@FXML
	private void onAddPlanYear(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PLANYEAROFFERINGPLANADD, true);
	}	

	@FXML
	private void onAddPlan(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PLANYEAROFFERINGPLANADD, true);
	}	

	@FXML
	private void onAddDepartment(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.DEPARTMENTADD, true);
	}	

	@FXML
	private void onAddUser(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.USERADD, true);
	}	

	private void viewFiling() 
	{
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/filing/ViewFiling.fxml"));
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
	
	private void viewSelectedTaxYear(boolean add) 
	{
		// offset one for the header row
		if(add == false)
			DataManager.i().mTaxYear = emprTaxYearsTableView.getSelectionModel().getSelectedItem().getTaxYear();
		EtcAdmin.i().setScreen(ScreenType.TAXYEAR, true);
	}	
	
	private void viewAddCoverageClass() 
	{
		//DataManager.i().mCoverageClass = null;
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/plancoverageclass/ViewPlanCoverageClass.fxml"));
			Parent ControllerNode = loader.load();
			ViewPlanCoverageClassController controller = (ViewPlanCoverageClassController) loader.getController();
			controller.setAddMode();
			Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        EtcAdmin.i().showMain();
	        if(controller.changesMade == true) {
	        	//FIXME: DataManager.i().loadUpdatedCoverageClasses();
	        	//showCoverageGroups();
	        }
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        
        catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	@FXML
	private void onShowInactiveCoverageClasses() {
		//showCoverageGroups();
	}
	
	@FXML
	private void onShowSystemInfo() {
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mEmployer;
			DataManager.i().mCurrentCoreDataType = SystemDataType.EMPLOYER;
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
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
	}
	
	@FXML
	private void onShowNotes() {
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/notes/ViewNotes.fxml"));
			Parent ControllerNode = loader.load();
	        ViewNotesController noteController = (ViewNotesController) loader.getController();
	        noteController.setScreenType(ScreenType.EMPLOYER);
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
	private void onShowPayPeriods() {
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employer/ViewPayPeriods.fxml"));
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
	private void onShowContacts() {
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employer/ViewEmployerContacts.fxml"));
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
	private void onShowProperties() {
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employer/ViewEmployerProperties.fxml"));
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
	private void onShowUsers() {
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employer/ViewEmployerUsers.fxml"));
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
	private void onShowPlanYears() {
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employer/ViewPlanYears.fxml"));
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
	private void onShowCodeCallHistory() {
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/callcodes/ViewCodeCallHistory.fxml"));
			Parent ControllerNode = loader.load();
			ViewCodeCallHistoryController controller = (ViewCodeCallHistoryController) loader.getController();
			controller.setIsAccountHistory(false);
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
	private void onShowCoverageClass() {
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employer/ViewEmployerCoverageClasses.fxml"));
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
	private void onShowEligibilityPeriods() {
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employer/ViewEmployerEligibilityPeriods.fxml"));
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
	private void onShowLocations() {
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employer/ViewLocations.fxml"));
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
	private void onShowDepartments() {
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employer/ViewDepartments.fxml"));
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
	

////////////////////////////////////////////////////////////////////////////////////////
///// FILE HISTORY
////////////////////////////////////////////////////////////////////////////////////////

	public class HBoxCell 
	{
		SimpleStringProperty user = new SimpleStringProperty();
		SimpleStringProperty dateTime = new SimpleStringProperty();
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
         
        String getDate() {return dateTime.get();}

	     HBoxCell(int queueLocation, String user, String dateTime, String description, String units, String records, String specId, String spec, String mapperId, String status, String fileType, boolean state1, boolean state2, boolean state3, boolean state4, boolean state5, String result)
	     {
	         super();
	
	          //save the requestId;
	          this.queueLocation = queueLocation;
	          
	          if(user == null) user = "";
	          if(dateTime == null ) dateTime = "";
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
	
	          setFieldStateAttributes(fieldState1, state1);
	          setFieldStateAttributes(fieldState2, state2);
	          setFieldStateAttributes(fieldState3, state3);
	          setFieldStateAttributes(fieldState4, state4);
	          setFieldStateAttributes(fieldState5, state5);
	
	         //Tooltip.install(this, new Tooltip(result)); 
	     }
         
     	private void setFieldStateAttributes(TextField field, boolean green) 
     	{
            field.setMinWidth(15);
            field.setMaxWidth(15);
            field.setMinHeight(10);
            field.setPrefHeight(10);
            field.setMaxHeight(10);
            
            if(green == true)
            	field.setStyle("-fx-background-color: green");
            else
            	field.setStyle("-fx-background-color: red");
    	}
    }	
	
	//extending the listview for our contact info
	public static class HBoxContactCell extends HBox 
	{
         Label lblName = new Label();
         Label lblPhone = new Label();
         Label lblEmail = new Label();

         HBoxContactCell(String sName, String sPhone, String sEmail, boolean headerRow) {
              super();

              if(sName == null ) sName = "";
              if(sPhone == null ) sPhone = "";
              if(sEmail == null ) sEmail = "";
              
              if(sName.contains("null")) sName = "";
              if(sPhone.contains("null")) sPhone = "";
              if(sEmail.contains("null")) sEmail = "";

              lblName.setText(sName);
              lblName.setMinWidth(200);
              lblName.setMaxWidth(200);
              lblName.setPrefWidth(200);
              HBox.setHgrow(lblName, Priority.ALWAYS);

              lblPhone.setText(sPhone);
              lblPhone.setMinWidth(100);
              lblPhone.setMaxWidth(100);
              lblPhone.setPrefWidth(100);
              HBox.setHgrow(lblPhone, Priority.ALWAYS);

              lblEmail.setText(sEmail);
              lblEmail.setMinWidth(200);
              lblEmail.setMaxWidth(200);
              lblEmail.setPrefWidth(200);
              HBox.setHgrow(lblEmail, Priority.ALWAYS);

              // make the header row bold
              if(headerRow == true) {
            	  lblName.setTextFill(Color.GREY);
            	  lblPhone.setTextFill(Color.GREY);;
            	  lblEmail.setTextFill(Color.GREY);
              }
              
        	  this.getChildren().addAll(lblName, lblPhone, lblEmail);
         }
    }	
	
	public static class HBoxPlanCell extends HBox 
	{
         Label lblId = new Label();
         Label lblName = new Label();
         Label lblInsType = new Label();
         Label lblWaived = new Label();
         Label lblInel = new Label();
         CheckBox chkWaived = new CheckBox();
         CheckBox chkInel = new CheckBox();
         
         //header contructor
         HBoxPlanCell(String id, String name, String insType, String waived, String inel) {
             super();

             lblId.setText(id);
             lblId.setMinWidth(100);
             lblId.setMaxWidth(100);
             lblId.setPrefWidth(100);
             lblId.setTextFill(Color.GREY);
             HBox.setHgrow(lblId, Priority.ALWAYS);

             lblName.setText(name);
             lblName.setMinWidth(310);
             lblName.setMaxWidth(310);
             lblName.setPrefWidth(310);
             lblName.setTextFill(Color.GREY);
             HBox.setHgrow(lblName, Priority.ALWAYS);

             lblInsType.setText(insType);
             lblInsType.setMinWidth(150);
             lblInsType.setMaxWidth(150);
             lblInsType.setPrefWidth(150);
             lblInsType.setTextFill(Color.GREY);
             HBox.setHgrow(lblInsType, Priority.ALWAYS);

             lblWaived.setText(waived);
             lblWaived.setMinWidth(75);
             lblWaived.setMaxWidth(75);
             lblWaived.setPrefWidth(75);
             lblWaived.setTextFill(Color.GREY);

             lblInel.setText(inel);
             lblInel.setMinWidth(75);
             lblInel.setMaxWidth(75);
             lblInel.setPrefWidth(75);
             lblInel.setTextFill(Color.GREY);
             
       	  this.getChildren().addAll(lblId, lblName, lblInsType, lblWaived, lblInel);
        }
         
         HBoxPlanCell(String id, String name, String insType, boolean waived, boolean inel) 
         {
              super();

              if(id == null ) id = "";
              if(name == null ) name = "";
              if(insType == null ) insType = "";
              
              if(id.contains("null")) id = "";
              if(name.contains("null")) name = "";
              if(insType.contains("null")) insType = "";

              lblId.setText(id);
              lblId.setMinWidth(100);
              lblId.setMaxWidth(100);
              lblId.setPrefWidth(100);
              HBox.setHgrow(lblId, Priority.ALWAYS);

              lblName.setText(name);
              lblName.setMinWidth(310);
              lblName.setMaxWidth(310);
              lblName.setPrefWidth(310);
              HBox.setHgrow(lblName, Priority.ALWAYS);

              lblInsType.setText(insType);
              lblInsType.setMinWidth(160);
              lblInsType.setMaxWidth(160);
              lblInsType.setPrefWidth(160);
              HBox.setHgrow(lblInsType, Priority.ALWAYS);

              chkWaived.setSelected(waived);
              chkWaived.setDisable(true);
              chkWaived.setMinWidth(75);
              chkWaived.setMaxWidth(75);
              chkWaived.setPrefWidth(75);

              chkInel.setSelected(inel);
              chkInel.setDisable(true);
              chkInel.setMinWidth(75);
              chkInel.setMaxWidth(75);
              chkInel.setPrefWidth(75);

        	  this.getChildren().addAll(lblId, lblName, lblInsType, chkWaived, chkInel);
         }
    }	
	
	//extending the listview to two columns
	public class HBox2ColCell extends HBox 
	{
         Label lblCol1 = new Label();
         Label lblCol2 = new Label();

         HBox2ColCell(String sCol1, String sCol2, boolean headerRow)
         {
              super();
           
              if(sCol1 == null ) sCol1 = "";
              if(sCol2 == null ) sCol2 = "";
              
              if(sCol1.contains("null")) sCol1 = "";
              if(sCol2.contains("null")) sCol2 = "";

              lblCol1.setText(sCol1);
              lblCol1.setMinWidth(150);
              lblCol1.setMaxWidth(150);
              lblCol1.setPrefWidth(150);
              HBox.setHgrow(lblCol1, Priority.ALWAYS);

              lblCol2.setText(sCol2);
              lblCol2.setMinWidth(275);
              lblCol2.setMaxWidth(275);
              lblCol2.setPrefWidth(275);
              HBox.setHgrow(lblCol2, Priority.ALWAYS);

              // make the header row bold
              if(headerRow == true) {
            	  lblCol1.setTextFill(Color.GREY);
            	  lblCol2.setTextFill(Color.GREY);
              }
              
              this.getChildren().addAll(lblCol1, lblCol2);
         }
    }	

	//extending the listview to two columns
	public class HBoxCovClassCell extends HBox 
	{
         Label lblCol1 = new Label();
         Label lblCol2 = new Label();
         CoverageGroup covClass = null;
         
         public CoverageGroup getCoverageClass() {
        	 return covClass;
         }

         HBoxCovClassCell(CoverageGroup covClass, boolean headerRow) 
         {
              super();
           
              this.covClass = covClass;
              if(covClass != null) 
              {
            	  Utils.setHBoxLabel(lblCol1, 150, false, covClass.getName());
            	  Utils.setHBoxLabel(lblCol2, 275, false, covClass.getDescription());
            	  if(covClass.isDeleted() == true) 
            	  {
            		  lblCol1.setTextFill(Color.RED);
            		  lblCol2.setTextFill(Color.RED);
            	  } else {
                	  if(covClass.isActive() == false) 
                	  {
                		  lblCol1.setTextFill(Color.BLUE);
                		  lblCol2.setTextFill(Color.BLUE);
                	  } else {
                		  lblCol1.setTextFill(Color.BLACK);
                		  lblCol2.setTextFill(Color.BLACK);
                	  }
            	  }
              } else {
            	  Utils.setHBoxLabel(lblCol1, 150, true, "Name");
            	  Utils.setHBoxLabel(lblCol2, 275, true, "Description");
              }

              this.getChildren().addAll(lblCol1, lblCol2);
         }
    }	
	

	public static class TaxYearCell 
	{
		SimpleStringProperty year = new SimpleStringProperty();
		SimpleStringProperty printOption = new SimpleStringProperty();
		SimpleStringProperty formType = new SimpleStringProperty();
		SimpleStringProperty irsContact = new SimpleStringProperty();
		SimpleStringProperty phone = new SimpleStringProperty();
		SimpleStringProperty cla = new SimpleStringProperty();
		SimpleStringProperty support = new SimpleStringProperty();
		SimpleStringProperty svcLevel = new SimpleStringProperty();
		SimpleStringProperty calculator = new SimpleStringProperty();
		SimpleStringProperty preProc = new SimpleStringProperty();
		SimpleStringProperty v2Status = new SimpleStringProperty();
		SimpleStringProperty mewa = new SimpleStringProperty();
		SimpleStringProperty inLegal = new SimpleStringProperty();
		TaxYear txYr;
	
		TaxYear getTaxYear() { return txYr; }
	    public String getYear() { return year.get(); }
	    public String getPrintOption() { return printOption.get(); }
	    public String getFormType() { return formType.get(); }
	    public String getIrsContact() { return irsContact.get(); }
	    public String getPhone() { return phone.get(); }
	    public String getCla() { return cla.get(); }
	    public String getSupport() { return support.get(); }
	    public String getSvcLevel() { return svcLevel.get(); }
	    public String getCalculator() { return calculator.get(); }
	    public String getPreProc() { return preProc.get(); }
	    public String getV2Status() { return v2Status.get(); }
	    public String getMewa() { return mewa.get(); }
	    public String getInLegal() { return inLegal.get(); }
	       
	    TaxYearCell(TaxYear txYr) 
	    {
	         super();
	
	         this.txYr = txYr;
	         if(txYr != null) 
	         {
	        	 year.set(String.valueOf(txYr.getYear()));
	        	 if(txYr.getPrintType() != null)
	        		 printOption.set(String.valueOf(txYr.getPrintType()));
	        	 else
	        		 printOption.set("");
	        	 if(txYr.getFormType() != null)
	        		 formType.set(String.valueOf(txYr.getFormType()));
	        	 else
	        		 formType.set("");
	        	 
	        	 // irs contact info
	        	 if(txYr.getIrsContactId() != null)
	        	 {
	        		 if(txYr.getIrsContact() == null || txYr.getIrsContact().getContact() == null)
	        		 {
	        			 Logger.getLogger(this.getClass().getCanonicalName()).warning("Fetching Missing Irs Contact for TaxYear=[" + txYr.getId() + "].");
	        			 EmployerContact cnt = null;
	        			 try { cnt = AdminPersistenceManager.getInstance().get(EmployerContact.class, txYr.getIrsContactId()); }
	        			 catch(CoreException e) {}
	        			 catch (Exception e) {  DataManager.i().logGenericException(e); }
	        			 if(cnt != null)
	        				 txYr.setIrsContact(cnt);
	        		 }
	        		 irsContact.set(txYr.getIrsContact().getContact().getFirstName() + " " + txYr.getIrsContact().getContact().getLastName());
        			 phone.set(txYr.getIrsContact().getContact().getPhone());
	        	 }
	        	 
	        	 // CLA (liason)
	        	 if (txYr.getLiaisonId() != null) 
	        	 {
	        		 if (txYr.getLiaison() != null && txYr.getLiaison().getFirstName() != null || txYr.getLiaison().getLastName() != null) {
	        			 cla.set(txYr.getLiaison().getFirstName() + " " + txYr.getLiaison().getLastName());
	        		 }
	        	 }
	        	 // Support (jr liason)
	        	 if (txYr.getJrLiaisonId() != null) 
	        	 {
	        		 if (txYr.getJrLiaison() != null && txYr.getJrLiaison().getFirstName() != null || txYr.getJrLiaison().getLastName() != null) {
	        			 support.set(txYr.getJrLiaison().getFirstName() + " " + txYr.getJrLiaison().getLastName());
	        		 }
	        	 }
	        	 if (txYr.getServiceLevelId() != null)
	        	 {
	        		 if (txYr.getServiceLevel() != null) {
	        			 svcLevel.set(txYr.getServiceLevel().getName());
	        		 }
	        	 }
	        	// support.set(String.valueOf(""));
	        	// calculator.set(String.valueOf(txYr.isClosed()));
	        	// preProc.set(String.valueOf(txYr.isClosed()));
	        	// v2Status.set(String.valueOf(txYr.isClosed()));
	        	// mewa.set(String.valueOf(txYr.isClosed()));
	        	// inLegal.set(String.valueOf(txYr.isClosed()));
	         }
	    }
	}

	public class HBoxPersonCell extends HBox 
	{
        Label lblId = new Label();
        Label lblName = new Label();
        Label lblLastUpdate = new Label();
        Label lblSSN = new Label();
        Person person;
        
        public String getName() {
        		return lblName.getText();
        }
        
        public Person getPerson() {
        	return person;
        }
        
        HBoxPersonCell(Person person) 
        {
             super();
          
             this.person = person;
             if(person != null) {
            	 Utils.setHBoxLabel(lblId, 100, false, person.getId());
            	 Utils.setHBoxLabel(lblName, 300, false, person.getLastName() + ", " + person.getFirstName());
            	 Utils.setHBoxLabel(lblLastUpdate, 150, false, person.getLastUpdated());
                 if(person.getSsn() != null)
                	 Utils.setHBoxLabel(lblSSN, 150, false, person.getSsn().getUsrln());
                 if(person.isActive() == false){
		           	  lblId.setTextFill(Color.BLUE);
		           	  lblName.setTextFill(Color.BLUE);
		           	  lblLastUpdate.setTextFill(Color.BLUE);
		           	  lblSSN.setTextFill(Color.BLUE);
                 }
             } else {
            	 Utils.setHBoxLabel(lblId, 100, true, "Id");
            	 Utils.setHBoxLabel(lblName, 300, true, "Name");
            	 Utils.setHBoxLabel(lblLastUpdate, 150, true, "Last Update");
                 Utils.setHBoxLabel(lblSSN, 150, true, "SSN");
             }
             this.getChildren().addAll(lblName, lblLastUpdate, lblSSN);
        }
   }	

}
