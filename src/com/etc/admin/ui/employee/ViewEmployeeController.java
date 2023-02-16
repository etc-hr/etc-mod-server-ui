package com.etc.admin.ui.employee;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.etc.CoreException;
import com.etc.admin.AdminApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.calc.CalcQueueData;
import com.etc.admin.ui.coresysteminfo.ViewCoreSystemInfoController;
import com.etc.admin.ui.employmentperiod.ViewEmploymentPeriodController;
import com.etc.admin.ui.notes.ViewNoteAddController;
import com.etc.admin.utils.AddressGrid;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.data.DSSN;
import com.etc.corvetto.entities.Dependent;
import com.etc.corvetto.entities.Employee;
import com.etc.corvetto.entities.EmployeeInformation;
import com.etc.corvetto.entities.EmployeeSnapshot;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.EmploymentPeriod;
import com.etc.corvetto.entities.Irs1095c;
import com.etc.corvetto.entities.Note;
import com.etc.corvetto.entities.Person;
import com.etc.corvetto.entities.PersonSnapshot;
import com.etc.corvetto.entities.PostalAddress;
import com.etc.corvetto.entities.PostalAddressSnapshot;
import com.etc.corvetto.entities.TaxYear;
import com.etc.corvetto.rqs.DSSNRequest;
import com.etc.corvetto.rqs.EmployeeInformationRequest;
import com.etc.corvetto.rqs.EmployeeRequest;
import com.etc.corvetto.rqs.EmployeeSnapshotRequest;
import com.etc.corvetto.rqs.EmploymentPeriodRequest;
import com.etc.corvetto.rqs.Irs1095bRequest;
import com.etc.corvetto.rqs.Irs1095cRequest;
import com.etc.corvetto.rqs.NoteRequest;
import com.etc.corvetto.rqs.PersonRequest;
import com.etc.corvetto.rqs.PersonSnapshotRequest;
import com.etc.corvetto.rqs.PostalAddressRequest;
import com.etc.corvetto.rqs.PostalAddressSnapshotRequest;
import com.etc.corvetto.rqs.TaxYearRequest;
import com.etc.corvetto.utils.types.EmploymentStatusType;
import com.etc.corvetto.utils.types.PayCodeType;
import com.etc.embeds.SSN;
import com.etc.entities.CoreData;
import com.etc.utils.types.CountryType;
import com.etc.utils.types.GenderType;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewEmployeeController 
{
	@FXML
	private Label empHeaderLabel;
	@FXML
	private TextField empFirstNameLabel;
	@FXML
	private TextField empMiddleNameLabel;
	@FXML
	private TextField empLastNameLabel;
	@FXML
	private TextField empErReferenceLabel;
	@FXML
	private TextField empSSNLabel;
	@FXML
	private TextField empEmailLabel;
	@FXML
	private TextField empPhoneLabel;
	@FXML
	private ComboBox<String> empGenderCombo;
	@FXML
	private ComboBox<String> empEmpStatusCombo;
	@FXML
	private TextField empJobTitleLabel;
	@FXML
	private TextField empDepartmentLabel;	
	@FXML
	private TextField empLocationField;
	@FXML
	private CheckBox empVerifiedCheck;
	// Employer Info
	@FXML
	private TextField empEmployerField;
	@FXML
	private TextField empEmployerIdField;
	// MAIL ADDRESS
	@FXML
	private Label empMAddrStateLabel;
	@FXML
	private Label empMAddrZipLabel;
	@FXML
	private Label empMAddrZip4Label;
	@FXML
	private Label empMAddrDeptLabel;
	@FXML
	private Label empMAddrQtrLabel;
	@FXML
	private Label empMAddrProvinceLabel;
	@FXML
	private TextField empMAddrStreetField;
	@FXML
	private TextField empMAddrUnitField;
	@FXML
	private TextField empMAddrCityField;
	@FXML
	private ComboBox<String> empMAddrStateCombo;
	@FXML
	private TextField empMAddrZipField;
	@FXML
	private TextField empMAddrZip4Field;
	@FXML
	private TextField empMAddrDeptField;
	@FXML
	private TextField empMAddrQuarterField;
	@FXML
	private TextField empMAddrProvinceField;
	@FXML
	private ComboBox<CountryType> empMAddrCountryCombo;
	// employment periods
	@FXML
	private Label empEmploymentPeriodsLabel;
	@FXML
	private CheckBox empInactiveEmploymentPeriodsCheck;
	@FXML
	private CheckBox empDeletedEmploymentPeriodsCheck;
	@FXML
	private ListView<HBoxEmploymentPeriodCell> empEmploymentPeriodsListView;
	@FXML 
	private DatePicker empDOBDate;
	@FXML
	private ComboBox<String> empCompCombo;
	@FXML
	private GridPane empData1Grid;
	@FXML
	private GridPane empData2Grid;
	@FXML
	private GridPane empSecondariesListGrid;
	@FXML
	private GridPane empEmploymentPeriodsListGrid;
	@FXML
	private GridPane empEmployeeCoveragePeriodsListGrid;
	@FXML
	private Button empEditButton;
	@FXML
	private Button empSaveButton;
	@FXML
	private Button empAddEmploymentPeriodButton;
	@FXML
	private Button empAddCoveragePeriodButton;
	@FXML
	private Button empSSNButton;
	// NOTES
	@FXML
	private Label empNotesLabel;
	@FXML
	private ListView<HBoxNoteCell> empNotesListView;
	@FXML
	private TextArea empNotesTextArea;
	@FXML
	private Button empNotesAddButton;
	// CORE DATA
	@FXML
	private Label empCoreIdLabel;
	@FXML
	private Label empCoreActiveLabel;
	@FXML
	private Label empCoreBODateLabel;
	@FXML
	private Label empCoreLastUpdatedLabel;
	// custom fields
	@FXML
	private TextField empCustomField1;
	@FXML
	private TextField empCustomField2;
	@FXML
	private TextField empCustomField3;
	@FXML
	private TextField empCustomField4;
	@FXML
	private TextField empCustomField5;
	@FXML
	private Label empDeletedLabel;
	// delete
	@FXML
	private Button empDeleteButton;
	// Calc
	@FXML 
	private Button CalcIrs10945XBCButton; 
	@FXML
	private ComboBox<String> CalcTaxYearCombo;
	// Edit History
	@FXML
	private ComboBox<String> editHistoryCombo;
	@FXML
	private Label historyWarningLabel;
	// Nulcear Deactivate
	@FXML
	private Button empNuclearDeactivateButton;
	
	// address object
	AddressGrid addressGrid = new AddressGrid();
	
	//snapshot support
	EmployeeSnapshot empSnap = null;
	PersonSnapshot personSnap = null;
	PostalAddressSnapshot addressSnap = null;
	
	//int to track any pending threads, used to properly update the progress and message
	int waitingToComplete = 0;
	
	Logger logr;
	
	boolean editMode = false;
	Employee employee = null;
	String payCompTypeReplaceData = "";
	String payHoursReplaceData = "";
	
	CountryType countryType = CountryType.US;
	
	// edit history snapshots
	List<EmployeeSnapshot> empSnapShots = null;
	List<PersonSnapshot> personSnapShots = null;
	List<PostalAddressSnapshot> addressSnapShots = null;
	List<EditHistoryData> historyData = new ArrayList<>();
	boolean ssnEdited = false;
	boolean historyMode = false;
	String defaultStyle = "";
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize()
	{
		try {
			logr = Logger.getLogger(this.getClass().getCanonicalName());
			initControls();
			updateEmployeeData();
			updateEditHistoryData();
		}catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		}
	}
	
	private void initControls() 
	{
		if (defaultStyle != "")
			defaultStyle = empJobTitleLabel.getStyle();
		clearChangedMarks();
		
		historyWarningLabel.setVisible(false);
		payCompTypeReplaceData = ""; 
		empDeletedLabel.setVisible(false);
		empDeleteButton.setDisable(false);
		// clear the screen in case something is not overwritten with the current data set
		clearScreen();
		//disable field edits
		setFieldEdits(false);
		//reset edited flag
		ssnEdited = false;
		// hide the editSSN button until it is showing
		empSSNButton.setText("Show SSN");
		// aesthetics
		empEmploymentPeriodsListGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		//empEmployeeCoveragePeriodsListGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);

		// set calc button images
		InputStream input1 = getClass().getClassLoader().getResourceAsStream("img/ERCalc1.png");
   	 	Image image1 = new Image(input1, 65f, 65f, true, true);
   	 	ImageView imageView1 = new ImageView(image1);
   	 	CalcIrs10945XBCButton.setGraphic(imageView1);

		//disable the dates from being editable when, well, not editable
		empDOBDate.setOnMouseClicked(e -> {
		     if(!empDOBDate.isEditable())
		    	 empDOBDate.hide();
		});
		// and the combos
	//	empCompCombo.setOnMouseClicked(e -> {
	//	     if(!empCompCombo.isEditable())
	//	    	 empCompCombo.hide();
	//	});
		
	//	empGenderCombo.setOnMouseClicked(e -> {
	//	     if(!empGenderCombo.isEditable())
	//	    	 empGenderCombo.hide();
	//	});
		
	//	empEmpStatusCombo.setOnMouseClicked(e -> {
	//	     if(!empEmpStatusCombo.isEditable())
	//	    	 empEmpStatusCombo.hide();
	//	});
		
		// Comp(Paycode) type
		ObservableList<String> compTypes = FXCollections.observableArrayList();
		//compTypes.add("");
		compTypes.add("FTH");
		compTypes.add("VH");
		empCompCombo.setItems(compTypes);

		// Gender type
		empGenderCombo.getItems().clear();
		//empGenderCombo.getItems().add("");
		for (GenderType gt : GenderType.values())
			empGenderCombo.getItems().add(gt.toString());

		// Country type
		ObservableList<CountryType> countryTypes = FXCollections.observableArrayList(CountryType.values());
		empMAddrCountryCombo.setItems(countryTypes);
		
		setStateCombo(true);

		//Employment Status type
		empEmpStatusCombo.getItems().clear();
		//empEmpStatusCombo.getItems().add("");
		for (EmploymentStatusType est : EmploymentStatusType.values())
			empEmpStatusCombo.getItems().add(est.toString());

		// NOTES
		empNotesListView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) 
            {
            	if(empNotesListView.getSelectionModel().getSelectedItem()!= null && 
            			empNotesListView.getSelectionModel().getSelectedItem().getNote() != null)
            				empNotesTextArea.setText(empNotesListView.getSelectionModel().getSelectedItem().getNote().getNote());
            }
        });
		
		// EMPLOYMENT PERIODS
		empEmploymentPeriodsListView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) 
            {
            	if (empEmploymentPeriodsListView.getSelectionModel().getSelectedItem() == null)
            		return;
            	
				DataManager.i().mEmploymentPeriod = empEmploymentPeriodsListView.getSelectionModel().getSelectedItem().getEmploymentPeriod();
				// and display the pop up
				try {
		            // load the fxml
			        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employmentperiod/ViewEmploymentPeriod.fxml"));
					Parent ControllerNode = loader.load();
			        ViewEmploymentPeriodController periodController = (ViewEmploymentPeriodController) loader.getController();

			        Stage stage = new Stage();
			        stage.initModality(Modality.APPLICATION_MODAL);
			        stage.initStyle(StageStyle.UNDECORATED);
			        stage.setScene(new Scene(ControllerNode));  
			        EtcAdmin.i().positionStageCenter(stage);
			        stage.showAndWait();
			        EtcAdmin.i().showMain();
			        if(periodController.changesMade == true)
			        	updateEmploymentPeriodData();
				} catch (Exception e) {
		        	DataManager.i().log(Level.SEVERE, e); 
				}        		
           }
        });
		
		//set functionality according to the user security level
		empEditButton.setDisable(!Utils.userCanEdit());
	}
	
	private void setStateCombo(boolean states)
	{
		if(states == true) 
		{
			// states
			ObservableList<String> codes = FXCollections.observableList(Utils.StateCodes);
			empMAddrStateCombo.setItems(codes);
		} else {
			// provinces
			ObservableList<String> codes = FXCollections.observableList(Utils.ProvinceCodes);
			empMAddrStateCombo.setItems(codes);
		}
	}

	private void clearScreen() 
	{
		empFirstNameLabel.setText("");
		empMiddleNameLabel.setText("");
		empLastNameLabel.setText("");
		empPhoneLabel.setText("");
		empEmailLabel.setText("");
		empSSNLabel.setText("");
		empJobTitleLabel.setText("");
		empDepartmentLabel.setText("");
		empMAddrStreetField.setText("");
		empMAddrUnitField.setText("");
		empMAddrCityField.setText("");
		empMAddrZipField.setText("");
		empMAddrZip4Field.setText("");
		empMAddrDeptField.setText("");
		empMAddrQuarterField.setText("");
		empMAddrProvinceField.setText("");
		empEmploymentPeriodsLabel.setText("Employment Periods");
		empEmploymentPeriodsListView.getItems().clear();
		empData1Grid.getStyleClass().clear();
		empData1Grid.setStyle(null);	
		empData2Grid.getStyleClass().clear();
		empData2Grid.setStyle(null);	
		empSaveButton.setVisible(false);
		empNotesListView.getItems().clear();
		empNotesTextArea.setText("");
		empDeletedLabel.setVisible(false);
		empDeleteButton.setDisable(false);

		empDOBDate.setValue(null);
		empGenderCombo.setValue("");// .getSelectionModel().select("");	
		empEmpStatusCombo.setValue("");//.getSelectionModel().select("");
		empCompCombo.setValue("");//.getSelectionModel().select("");
		empDepartmentLabel.setText("");
		empMAddrCountryCombo.getSelectionModel().select(null);
		empMAddrStateCombo.getSelectionModel().select(null);
	}

	private void setFieldEdits(boolean state) 
	{
		empFirstNameLabel.setEditable(state);
		empMiddleNameLabel.setEditable(state);
		empLastNameLabel.setEditable(state);
		empPhoneLabel.setEditable(state);
		empEmailLabel.setEditable(state);
		empSSNLabel.setEditable(state);
		empGenderCombo.setDisable(!state);
		empEmpStatusCombo.setDisable(!state);
		empCompCombo.setDisable(!state);
		empDOBDate.setEditable(state);
		empJobTitleLabel.setEditable(state);
		empDepartmentLabel.setEditable(state);
		empMAddrStreetField.setEditable(state);
		empMAddrUnitField.setEditable(state);
		empMAddrCityField.setEditable(state);
		empMAddrStateCombo.setEditable(true);
		empMAddrZipField.setEditable(state);
		empMAddrZip4Field.setEditable(state);
		empMAddrDeptField.setEditable(state);
		empMAddrQuarterField.setEditable(state);
		empMAddrProvinceField.setEditable(state);
		empMAddrCountryCombo.setEditable(state);
		empVerifiedCheck.setDisable(!state);
		addressGrid.setEditMode(state);
		empErReferenceLabel.setEditable(state);
		// cusotm fields
		empCustomField1.setEditable(state);
		empCustomField2.setEditable(state);
		empCustomField3.setEditable(state);
		empCustomField4.setEditable(state);
		empCustomField5.setEditable(state);
	}
	
	
	/*******************************************/
	/*              UPDATE THREADS             */
	/*******************************************/
	
	private void updateEmployeeData() 
	{
		if(DataManager.i().mEmployer == null) return;

		// verify that the current employere is the correct one
		if(DataManager.i().mEmployer.getId().equals(DataManager.i().mEmployee.getEmployerId()) == false){
			Employer employer = DataManager.i().mEmployer;
			Utils.showAlert("Employer - Employee Mismatch", "The selected Employer does not match this employee. Please reselect the employer from the account screen. If problem persists contact systems.");
			return;
		}
		
		try
		{
			// new thread
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception
	            {
	          		EmployeeRequest eRequest = new EmployeeRequest();
	          		eRequest.setId(DataManager.i().mEmployee.getId());
	          		DataManager.i().mEmployee = AdminPersistenceManager.getInstance().get(eRequest);

	          		//get the mail address
	          		if(DataManager.i().mEmployee.getPerson() == null || (DataManager.i().mEmployee.getPerson().getMailAddress() == null && DataManager.i().mEmployee.getPerson().getMailAddressId() != null)) 
	          		{
		          		PostalAddressRequest request = new PostalAddressRequest();
		          		request.setId(DataManager.i().mEmployee.getPerson().getMailAddressId());
		          		DataManager.i().mEmployee.getPerson().setMailAddress(AdminPersistenceManager.getInstance().get(request));
	          		}
	          		
	          		// force an update of the tax years
	        		try {
		            	TaxYearRequest tyReq = new TaxYearRequest(); 
		        		tyReq.setEmployerId(DataManager.i().mEmployer.getId());
						DataManager.i().mEmployer.setTaxYears(AdminPersistenceManager.getInstance().getAll(tyReq));
					} catch (Exception e) {
			        	DataManager.i().log(Level.SEVERE, e); 
					}
		
	                return null;
	            }
	        };
	        
	        task.setOnScheduled(e -> { 
	        	//update the employer since we need this info for the employee screen
          		EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Updating Employee Data...");
          		waitingToComplete++;
          	});
	    	task.setOnSucceeded(e ->  {
	    		EtcAdmin.i().setProgress(0.0);
	    		showEmployee();
	    		updateEmploymentPeriodData();
	    		updateNotesData();
	    	});
	    	task.setOnFailed(e ->  {
	    		EtcAdmin.i().setProgress(0.0);
	    		DataManager.i().log(Level.SEVERE,task.getException());
	    		showEmployee();
	    	});
	    	
	    	AdminApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e)
		{
			logr.log(Level.SEVERE, "Exception.", e);
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}

	private void updateEditHistoryData() 
	{
		if(DataManager.i().mEmployer == null) return;
		
		try
		{
			// new thread
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception
	            {
	            	historyData.clear();
	            	// employee
	     			EmployeeSnapshotRequest employeeSnapshotRequest = new EmployeeSnapshotRequest();
	     			employeeSnapshotRequest.setEmployeeId(DataManager.i().mEmployee.getId());
	      			List<EmployeeSnapshot> empSnapshots = AdminPersistenceManager.getInstance().getAll(employeeSnapshotRequest);
	      			for (EmployeeSnapshot es : empSnapshots)
	      				if (employeeSnapshotChanged(DataManager.i().mEmployee, es) == true)
	      					historyData.add(new EditHistoryData(es,null,null));
	      			// person	
	     			PersonSnapshotRequest personSnapshotRequest = new PersonSnapshotRequest();
	     			personSnapshotRequest.setPersonId(DataManager.i().mEmployee.getPersonId());
	      			personSnapShots = AdminPersistenceManager.getInstance().getAll(personSnapshotRequest);
	      			for (PersonSnapshot ps : personSnapShots) { // filter out those with a matching batch ids to the employee
	      				if (personSnapshotChanged(DataManager.i().mEmployee.getPerson(), ps) == true)
	      					historyData.add(new EditHistoryData(null,ps,null));
	      			}
	      			// address
	     			PostalAddressSnapshotRequest addressSnapshotRequest = new PostalAddressSnapshotRequest();
	     			addressSnapshotRequest.setPostalAddressId(DataManager.i().mEmployee.getPerson().getMailAddressId());
	      			List<PostalAddressSnapshot> addressSnapShots = AdminPersistenceManager.getInstance().getAll(addressSnapshotRequest);
	      			for (PostalAddressSnapshot as : addressSnapShots) {
	      				if (DataManager.i().mEmployee.getPerson().getMailAddress() == null) {
	      					historyData.add(new EditHistoryData(null,null,as));
	      					continue;
	      				}
	      				if (addressSnapshotChanged(DataManager.i().mEmployee.getPerson().getMailAddress(), as) == true)
	      					historyData.add(new EditHistoryData(null,null,as));
	      			}
	      			return null;
	            }
	        };
	        
	    	task.setOnSucceeded(e ->  {
	    		EtcAdmin.i().setProgress(0.0);
	    		showEditHistory();
	    	});
	    	task.setOnFailed(e ->  {
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		showEditHistory();
	    	});
	    	
	    	AdminApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e)
		{
			logr.log(Level.SEVERE, "Exception.", e);
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private boolean employeeSnapshotChanged(Employee em, EmployeeSnapshot es)
	{
		if (es.isEmployed() != em.isEmployed()) return true;
		if (es.isActive() != em.isActive()) return true;
		if (es.getEmploymentStatusType() != em.getEmploymentStatusType()) return true;
		if (es.getLocation() != em.getLocation()) return true;
		if (es.getPayCodeType() != em.getPayCodeType()) return true;
		
		if (Utils.areStringsDifferent(es.getEmployerReference(), em.getEmployerReference()) == true) return true;
		if (Utils.areStringsDifferent(es.getJobTitle(), em.getJobTitle()) == true) return true;

		if (Utils.areLongsDifferent(es.getDepartmentId(), em.getDepartmentId()) == true) return true;
		if (Utils.areLongsDifferent(es.getEmployerId(), em.getEmployerId()) == true) return true;

		return false;
	}

	private boolean personSnapshotChanged(Person pr, PersonSnapshot ps)
	{
		if (ps.getGenderType() != pr.getGenderType()) return true;

		if (Utils.areStringsDifferent(ps.getFirstName(), pr.getFirstName()) == true) return true;
		if (Utils.areStringsDifferent(ps.getMiddleName(), pr.getMiddleName()) == true) return true;
		if (Utils.areStringsDifferent(ps.getLastName(), pr.getLastName()) == true) return true;
		if (Utils.areStringsDifferent(ps.getPhone(), pr.getPhone()) == true) return true;
		if (Utils.areStringsDifferent(ps.getEmail(), pr.getEmail()) == true) return true;

		if (ps.getSsn() != null && pr.getSsn() == null) return true;
		if (ps.getSsn() == null && pr.getSsn() != null) return true;
		if (ps.getSsn() != null && pr.getSsn() != null) 
			if (ps.getSsn().getUsrln().equals(pr.getSsn().getUsrln()) == false) return true;
		
		if (Utils.areDatesDifferent(ps.getDateOfBirth(), pr.getDateOfBirth()) == true) return true;
		
		return false;
	}

	private boolean addressSnapshotChanged(PostalAddress pa, PostalAddressSnapshot ps)
	{
		if (Utils.areStringsDifferent(ps.getStreet(), pa.getStreet()) == true) return true;
		if (Utils.areStringsDifferent(ps.getStprv2(), pa.getStprv2()) == true) return true;
		if (Utils.areStringsDifferent(ps.getUnit(), pa.getUnit()) == true) return true;
		if (Utils.areStringsDifferent(ps.getCity(), pa.getCity()) == true) return true;
		if (Utils.areStringsDifferent(ps.getQuarter(), pa.getQuarter()) == true) return true;
		if (Utils.areStringsDifferent(ps.getZip(), pa.getZip()) == true) return true;
		if (Utils.areStringsDifferent(ps.getZp4(), pa.getZp4()) == true) return true;
		if (Utils.areStringsDifferent(ps.getProvince(), pa.getProvince()) == true) return true;
		if (Utils.areStringsDifferent(ps.getDepartment(), pa.getDepartment()) == true) return true;
		if (Utils.areStringsDifferent(ps.getStreet(), pa.getStreet()) == true) return true;

		if (ps.getCountry() != pa.getCountry()) return true;
		
		return false;
	}

	private void updateEmploymentPeriodData() 
	{
		try
		{
			empEmploymentPeriodsListView.getItems().clear();
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception 
	            {
	            	//update our employer
	        		if(DataManager.i().mEmployee != null) 
	        		{
	        			// create the reuqst
	        			EmploymentPeriodRequest request = new EmploymentPeriodRequest();
	        			request.setEmployeeId(DataManager.i().mEmployee.getId());
	        			request.setFetchInactive(true);
	        			// retrieve any non-cached from the server
	        			DataManager.i().mEmployee.setEmploymentPeriods(AdminPersistenceManager.getInstance().getAll(request));
	        		}
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {
	      		EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Employment Period Data...");
	      		waitingToComplete++;
	      	});
	    	task.setOnSucceeded(e ->  {
	    		EtcAdmin.i().setProgress(0.0);
	    		showEmploymentPeriods();
	    	});
	    	task.setOnFailed(e ->  {
	    		EtcAdmin.i().setProgress(0.0);
	    		showEmploymentPeriods();
	    	});
	        
	    	AdminApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e)
		{
			logr.log(Level.SEVERE, "Exception.", e);
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void refreshEmployeeData() 
	{
		//if(DataManager.i().mEmployer == null) return;
		//empRefreshData.setVisible(false);
	}
	
	private void showEmployee()
	{	
		if(DataManager.i().mEmployee != null) 
		{
			employee = DataManager.i().mEmployee;
			Utils.setActiveHeader(empHeaderLabel,employee.isActive());
			Utils.updateControl(empFirstNameLabel,employee.getPerson().getFirstName());
			Utils.updateControl(empMiddleNameLabel,employee.getPerson().getMiddleName());
			Utils.updateControl(empLastNameLabel,employee.getPerson().getLastName());
			Utils.updateControl(empPhoneLabel,employee.getPerson().getPhone());
			Utils.updateControl(empEmailLabel,employee.getPerson().getEmail());
			Utils.updateControl(empJobTitleLabel,employee.getJobTitle());
			Utils.updateControl(empDOBDate, employee.getPerson().getDateOfBirth());
			Utils.updateControl(empVerifiedCheck,employee.isVerified());
			// custom fields
			Utils.updateControl(empCustomField1,employee.getCustomField1());
			Utils.updateControl(empCustomField2,employee.getCustomField2());
			Utils.updateControl(empCustomField3,employee.getCustomField3());
			Utils.updateControl(empCustomField4,employee.getCustomField4());
			Utils.updateControl(empCustomField5,employee.getCustomField5());
			//if(employee.getEm)
			if (employee.getEmployerReference() == null) {
				empErReferenceLabel.setText("");
				empErReferenceLabel.setPromptText("NULL");
			}else {
				Utils.updateControl(empErReferenceLabel,employee.getEmployerReference());
				empErReferenceLabel.setPromptText("");
			}
			if (employee.getEmployer() != null) {
				Utils.updateControl(empEmployerField, employee.getEmployer().getName());
				Utils.updateControl(empEmployerIdField, employee.getEmployer().getId());
			}
			// SSN - update unless edited
			if (ssnEdited == false) {
				if(employee.getPerson().getSsn() != null)
					Utils.updateControl(empSSNLabel,"XXX-XX-" + employee.getPerson().getSsn().getUsrln());
				else
					empSSNLabel.setText("");
			}
			// tax year combo
			CalcTaxYearCombo.getItems().clear();
			if (DataManager.i().mEmployer.getTaxYears() == null) {
        		try {
	            	TaxYearRequest tyReq = new TaxYearRequest(); 
	        		tyReq.setEmployerId(DataManager.i().mEmployer.getId());
					DataManager.i().mEmployer.setTaxYears(AdminPersistenceManager.getInstance().getAll(tyReq));
				} catch (CoreException e) {
		        	DataManager.i().log(Level.SEVERE, e); 
				}
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
			}
			
			String calcSelection = "";
			for (TaxYear taxYear : DataManager.i().mEmployer.getTaxYears()) {
				CalcTaxYearCombo.getItems().add(String.valueOf(taxYear.getYear()));
				// if it matches the current year, default to it
				if (taxYear.getYear() == Calendar.getInstance().get(Calendar.YEAR))
					calcSelection = String.valueOf(taxYear.getYear());
			}
			
			// sort
			Collections.sort(CalcTaxYearCombo.getItems());
			// select default or last
			boolean selected = false;
			if (CalcTaxYearCombo.getItems() != null && CalcTaxYearCombo.getItems().size() > 0 ) {
				for (String year : CalcTaxYearCombo.getItems())
					if (year.equals("2022") == true) {
						CalcTaxYearCombo.getSelectionModel().select(year);
						selected = true;
					}
				if (selected == false)
					CalcTaxYearCombo.getSelectionModel().selectLast(); 
			}
			
			
			// deleted flag
			if (employee.isDeleted() == true) {
				empDeletedLabel.setVisible(true);
				empDeleteButton.setDisable(true);

			}
			else {
				empDeletedLabel.setVisible(false);
				empDeleteButton.setDisable(false);
			}
			
			// EMPLOYMENT PERIOD
			//if( employee.getEmploymentPeriod() != null) {
			//	Utils.updateControl(empHireDate, employee.getEmploymentPeriod().getHireDate());
			//	Utils.updateControl(empTermDate, employee.getEmploymentPeriod().getTermDate());
			//}
			// GENDER TYPE
			if(employee.getPerson().getGenderType() != null) 
				empGenderCombo.getSelectionModel().select(employee.getPerson().getGenderType().toString());
			else
				empGenderCombo.getSelectionModel().select("");	
			// EMPLOYMENT STATUS TYPE
			if(employee.getEmploymentStatusType() != null) 
				empEmpStatusCombo.getSelectionModel().select(employee.getEmploymentStatusType().toString());
			else
				empEmpStatusCombo.getSelectionModel().select("");
			// PAY CODE TYPE
			if(employee.getPayCodeType() != null) 
				empCompCombo.getSelectionModel().select(employee.getPayCodeType().toString());
			else
				empCompCombo.getSelectionModel().select("");
			// DEPARTMENT
			if(employee.getDepartment() != null) 
				Utils.updateControl(empDepartmentLabel,employee.getDepartment().getName());
			else
				empDepartmentLabel.setText("");
			// LOCATION
			if(employee.getLocation() != null) 
				Utils.updateControl(empLocationField,employee.getLocation().getName());
			else
				empLocationField.setText("");

			// MAIL ADDRESS
			addressGrid.createAddress(empData2Grid, employee.getPerson().getMailAddress(), "Mail Address");

			//core data read only
			Utils.updateControl(empCoreIdLabel,String.valueOf(employee.getId()));
			Utils.updateControl(empCoreActiveLabel,String.valueOf(employee.isActive()));
			Utils.updateControl(empCoreBODateLabel,employee.getBornOn());
			Utils.updateControl(empCoreLastUpdatedLabel,employee.getLastUpdated());
		}
	}
	
	//update the employment periods list
	private void showEmploymentPeriods()
	{
		if(DataManager.i().mEmployee.getEmploymentPeriods() != null)
		{
		    List<HBoxEmploymentPeriodCell> employmentPeriodList = new ArrayList<HBoxEmploymentPeriodCell>();
		    if(DataManager.i().mEmployee.getEmploymentPeriods().size() > 0)
		    	employmentPeriodList.add(new HBoxEmploymentPeriodCell(null));
		    for(EmploymentPeriod employmentPeriod : DataManager.i().mEmployee.getEmploymentPeriods()) 
		    {
				if(employmentPeriod == null) continue;
				if(employmentPeriod.isActive() == false && empInactiveEmploymentPeriodsCheck.isSelected() == false) continue;
				if(employmentPeriod.isDeleted() == true && empDeletedEmploymentPeriodsCheck.isSelected() == false) continue;
				employmentPeriodList.add(new HBoxEmploymentPeriodCell(employmentPeriod));
		    };	
			
			ObservableList<HBoxEmploymentPeriodCell> myObservableEmploymentPeriodList = FXCollections.observableList(employmentPeriodList);
			empEmploymentPeriodsListView.setItems(myObservableEmploymentPeriodList);		
			//update our employer screen label
			empEmploymentPeriodsLabel.setText("Employment Periods (total: " + String.valueOf(empEmploymentPeriodsListView.getItems().size() - 1) + ")");
		} else {
			empEmploymentPeriodsLabel.setText("Employment Periods (total: 0)");			
		}
	}
	
	private void updateNotesData()
	{
		try
		{
			empNotesListView.getItems().clear();
			empNotesTextArea.setText("");
			empNotesLabel.setText("Notes (loading...)");
			// create a thread to handle the update
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception 
	            {
	            	NoteRequest request = new NoteRequest();
	            	request.setEmployeeId(DataManager.i().mEmployee.getId());
	            	List<Note> notes = AdminPersistenceManager.getInstance().getAll(request);
	            	DataManager.i().mEmployee.setNotes(notes);
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {
	      		EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Updating Notes Data...");
	      		waitingToComplete++;
	      	});
	      			
	    	task.setOnSucceeded(e ->  { 
	    		EtcAdmin.i().setProgress(0.0);
	    		showNotes(); 
	    	});
	    	task.setOnFailed(e ->  { 
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		showNotes(); 
	    	});
	        
	    	AdminApp.getInstance().getFxQueue().put(task);
		}catch(Exception e)
		{
			logr.log(Level.SEVERE, "Exception.", e);
		}
	}
	
	private void showEditHistory() 
	{
		editHistoryCombo.getItems().clear();
		Collections.sort(historyData, (o1, o2) -> (int)(o2.getDate().compareTo(o1.getDate())));
		String val = "";
		for (EditHistoryData hd : historyData)
			editHistoryCombo.getItems().add(hd.getComboString());

		editHistoryCombo.getItems().add(0,"Current");
		editHistoryCombo.getSelectionModel().clearAndSelect(0);
	}
	
	private void showSelectedHistory()
	{
		try {
			EtcAdmin.i().showAppStatus("","",0,false);
		
			//reset things back to the current state so that our comparison is made with it
			clearChangedMarks();
			showEmployee();
	
			if (editHistoryCombo.getSelectionModel().getSelectedIndex() < 0) return;
			if (editHistoryCombo.getSelectionModel().getSelectedIndex() == 0) {
				historyMode = false;
				historyWarningLabel.setVisible(false);
				empData1Grid.getStyleClass().clear();
				empData1Grid.setStyle(null);	
				empData2Grid.getStyleClass().clear();
				empData2Grid.setStyle(null);	
				return;
			}
			
			// mark the background
			empData1Grid.setStyle("-fx-background-color: oldlace");
			empData2Grid.setStyle("-fx-background-color: oldlace");
			historyMode = true;
			historyWarningLabel.setVisible(true);
			
			EditHistoryData sh =  historyData.get(editHistoryCombo.getSelectionModel().getSelectedIndex()-1);
			if (sh == null) return;
			switch (sh.getSnapshotType()) {
				case 1: //employee
					empSnap = sh.getEmployeeSnapshot();
					// shw the employee snapshot
					showEmployeeSnapshot(empSnap);
					// check for any matching person snapshots and show
	    		/*	if (personSnapShots != null && personSnapShots.size() > 0) {
						for (PersonSnapshot pSnap : personSnapShots) {
							// check to see if it is within 10 seconds
							if (Utils.compareDates(empSnap.getLastUpdated(), pSnap.getLastUpdated(), 5000) == true) {
								showPersonSnapshot(pSnap);
								break;
							}
						}
	    			}
					// check for any matching address snapshots and show
	    			if (addressSnapShots != null && addressSnapShots.size() > 0) {
						for (PostalAddressSnapshot aSnap : addressSnapShots) {
							// check to see if it is in wihtin 15 seconds
							if (Utils.compareDates(empSnap.getLastUpdated(), aSnap.getLastUpdated(), 5000) == true) {
								showPostalAddressSnapshot(aSnap);
								break;
							}
						}
	    			}
			*/	break;
				case 2: // person
					personSnap = sh.getPersonSnapshot();
					showPersonSnapshot(personSnap);
				break;
				case 3: // address
					addressSnap = sh.getAddressSnapshot();
					showPostalAddressSnapshot(addressSnap);
					break;
			}
		} catch (Exception e) {
			DataManager.i().log(Level.SEVERE, e); 
		}
	}
	
	private void showEmployeeSnapshot(EmployeeSnapshot empSnap)
	{
		if (empSnap != null) {
			Utils.markAndUpdateControl(empJobTitleLabel,empSnap.getJobTitle());
			if (empSnap.getEmployerReference() == null) {
				// mark it if it is a history changed
				if (historyMode == true) {
					if (empErReferenceLabel.getText() != "" && empErReferenceLabel.getPromptText() != "NULL")
						empErReferenceLabel.setStyle("-fx-background-color: lightcyan");
					else
						empErReferenceLabel.setStyle("");
				}
				empErReferenceLabel.setText("");
				empErReferenceLabel.setPromptText("NULL");
			}else {
				Utils.markAndUpdateControl(empErReferenceLabel,empSnap.getEmployerReference());
				empErReferenceLabel.setPromptText("");
			}
			// custom fields are at this moment not support by the emp snapshot. So leave them
			//Utils.markAndUpdateControl(empCustomField1,"");
			//Utils.markAndUpdateControl(empCustomField2,"");
			//Utils.markAndUpdateControl(empCustomField3,"");
			//Utils.markAndUpdateControl(empCustomField4,"");
			//Utils.markAndUpdateControl(empCustomField5,"");
			if (empSnap.getEmployer() != null) {
				Utils.markAndUpdateControl(empEmployerField, empSnap.getEmployer().getName());
				Utils.markAndUpdateControl(empEmployerIdField, empSnap.getEmployer().getId());
			}
			if(empSnap.getDepartment() != null) 
				Utils.markAndUpdateControl(empDepartmentLabel,empSnap.getDepartment().getName());
			else
				empDepartmentLabel.setText("");

			// typed combos
			empEmpStatusCombo.getEditor().setStyle("");
			if (empEmpStatusCombo.getSelectionModel().getSelectedItem() != null && empEmpStatusCombo.getSelectionModel().getSelectedItem() != ""  && empSnap.getEmploymentStatusType() == null)
				empEmpStatusCombo.getEditor().setStyle("-fx-background-color: lightcyan");
			if (empEmpStatusCombo.getSelectionModel().getSelectedItem() == null && empSnap.getEmploymentStatusType() != null)
				empEmpStatusCombo.getEditor().setStyle("-fx-background-color: lightcyan");
			if (empEmpStatusCombo.getSelectionModel().getSelectedItem() != null && empSnap.getEmploymentStatusType() != null && empEmpStatusCombo.getSelectionModel().getSelectedItem().equals(empSnap.getEmploymentStatusType().toString()) == false)
				empEmpStatusCombo.getEditor().setStyle("-fx-background-color: lightcyan");
			if(empSnap.getEmploymentStatusType() != null) 
				empEmpStatusCombo.getSelectionModel().select(empSnap.getEmploymentStatusType().toString());
			else
				empEmpStatusCombo.getSelectionModel().select(null);
			
			empCompCombo.getEditor().setStyle("");
			if (empCompCombo.getSelectionModel().getSelectedItem() != null && empCompCombo.getSelectionModel().getSelectedItem() != "" && empSnap.getPayCodeType() == null)
				empCompCombo.getEditor().setStyle("-fx-background-color: lightcyan");
			if (empCompCombo.getSelectionModel().getSelectedItem() == null && empSnap.getPayCodeType() != null)
				empCompCombo.getEditor().setStyle("-fx-background-color: lightcyan");
			if (empCompCombo.getSelectionModel().getSelectedItem() != null && empSnap.getPayCodeType() != null && empCompCombo.getSelectionModel().getSelectedItem().equals(empSnap.getPayCodeType().toString()) == false)
				empCompCombo.getEditor().setStyle("-fx-background-color: lightcyan");
			if(empSnap.getPayCodeType() != null) 
				empCompCombo.getSelectionModel().select(empSnap.getPayCodeType().toString());
			else
				empCompCombo.getSelectionModel().select(null);
			
			// show deleted flag
			if (empSnap.isDeleted() == true) {
				empDeletedLabel.setVisible(true);
				empDeleteButton.setDisable(true);

			}
			else {
				empDeletedLabel.setVisible(false);
				empDeleteButton.setDisable(false);
			}		
		}	
	}
	
	private void showPersonSnapshot(PersonSnapshot personSnap)
	{
		if (personSnap != null) {
			// debug info
			Utils.markAndUpdateControl(empFirstNameLabel,personSnap.getFirstName());
			Utils.markAndUpdateControl(empMiddleNameLabel,personSnap.getMiddleName());
			Utils.markAndUpdateControl(empLastNameLabel,personSnap.getLastName());
			Utils.markAndUpdateControl(empPhoneLabel,personSnap.getPhone());
			Utils.markAndUpdateControl(empEmailLabel,personSnap.getEmail());
			Utils.markAndUpdateControl(empDOBDate, personSnap.getDateOfBirth());
			// GENDER TYPE
			empGenderCombo.getEditor().setStyle("");
			if (empGenderCombo.getSelectionModel().getSelectedItem() != null && empGenderCombo.getSelectionModel().getSelectedItem() != "" && personSnap.getGenderType() == null)
				empGenderCombo.getEditor().setStyle("-fx-background-color: lightcyan");
			if (empGenderCombo.getSelectionModel().getSelectedItem() == null && personSnap.getGenderType() != null)
				empGenderCombo.getEditor().setStyle("-fx-background-color: lightcyan");
			if (empGenderCombo.getSelectionModel().getSelectedItem() != null && personSnap.getGenderType() != null && empGenderCombo.getSelectionModel().getSelectedItem().equals(personSnap.getGenderType().toString()) == false)
				empGenderCombo.getEditor().setStyle("-fx-background-color: lightcyan");
			
			if(personSnap.getGenderType() != null) 
				empGenderCombo.getSelectionModel().select(personSnap.getGenderType().toString());
			else
				empGenderCombo.getSelectionModel().select("");	
			// SSN - update unless edited
			if(personSnap.getSsn() != null) {
				// if the SSN is visible, decrypt and show
				if(empSSNButton.getText().contentEquals("Show SSN") == false) 
					Utils.markAndUpdateControl(empSSNLabel,Utils.decryptSSN(personSnap.getSsn()));
				else // otherwise just show the masked usln
					Utils.markAndUpdateControl(empSSNLabel,"XXX-XX-" + personSnap.getSsn().getUsrln());
			}
			else
				empSSNLabel.setText("");
		}
	}

	private void showPostalAddressSnapshot(PostalAddressSnapshot addressSnap)
	{
		if (addressSnap != null)
			addressGrid.createAddress(empData2Grid, addressSnap, "Mail Address", true);
		else
			addressGrid.createAddress(empData2Grid, null, "Mail Address", true);
	}
	
	private void showHistoryWarning()
	{
		Utils.showAlert("Viewing History", "You are currently viewing history, which is read only. \n\nPlease select current in the edit history combo to make changes or view additional information.");
	}
	
	private void clearChangedMarks()
	{
		empJobTitleLabel.setStyle(defaultStyle);
		empErReferenceLabel.setStyle(defaultStyle);
		empCustomField1.setStyle(defaultStyle);
		empCustomField2.setStyle(defaultStyle);
		empCustomField3.setStyle(defaultStyle);
		empCustomField4.setStyle(defaultStyle);
		empCustomField5.setStyle(defaultStyle);
		empEmployerField.setStyle(defaultStyle);
		empEmployerIdField.setStyle(defaultStyle);
		empDepartmentLabel.setStyle(defaultStyle);
		empFirstNameLabel.setStyle(defaultStyle);
		empMiddleNameLabel.setStyle(defaultStyle);
		empLastNameLabel.setStyle(defaultStyle);
		empPhoneLabel.setStyle(defaultStyle);
		empEmailLabel.setStyle(defaultStyle);
		empSSNLabel.setStyle(defaultStyle);

		empDOBDate.getEditor().setStyle(defaultStyle);
		empGenderCombo.getEditor().setStyle(defaultStyle);
		empEmpStatusCombo.getEditor().setStyle(defaultStyle);
		empCompCombo.getEditor().setStyle(defaultStyle);
	}
	
	private void showNotes()
	{
		// clear anything already in the list
		empNotesListView.getItems().clear();
		empNotesTextArea.setText("");
		
		Employee employee = DataManager.i().mEmployee;
		List<Note> notes = employee.getNotes(); 

		if (notes != null && notes.size() > 0) 
		{
			//add a header
			empNotesListView.getItems().add(new HBoxNoteCell(null));
			// and iterate
			for(Note note : notes)
			{
				if(note == null) continue;
				empNotesListView.getItems().add(new HBoxNoteCell(note));
			};	
		}
		
        //update our label
		if (notes.size() > 0)
			empNotesLabel.setText("Notes (total: " + String.valueOf(empNotesListView.getItems().size() - 1) + ")" );
		else
			empNotesLabel.setText("Notes (total: 0)");
        
        //end of the chain, update our status
  		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0.0);		
	}
	
	//extending the listview for our additional controls
	public static class HBoxNoteCell extends HBox 
	{
        Label lblId = new Label();
        Label lblDate = new Label();
        Label lblType = new Label();
        Label lblTaxPeriod = new Label();
        Note note = null;

        public Note getNote() {
       	 return note;
        }
        
        HBoxNoteCell(Note note) 
        {
             super();
             this.note = note;
             
             if(note != null) 
             {
              	  Utils.setHBoxLabel(lblId, 75, false, note.getId());
              	  Utils.setHBoxLabel(lblDate, 100, false, note.getLastUpdated());
              	  if (note.getType() != null)
              		  Utils.setHBoxLabel(lblType, 150, false, note.getType().getName());
              	  else
              		Utils.setHBoxLabel(lblType, 150, false, "");
               	  if (note.getTaxPeriod() != null)
               		  Utils.setHBoxLabel(lblTaxPeriod, 150, false, String.valueOf(note.getTaxPeriod().getYear()) + " - " 
               				  													+ note.getTaxPeriod().getFilingType().toString() + " " 
               				  													+ note.getTaxPeriod().getFilingState().toString());
               	  else
               		  Utils.setHBoxLabel(lblTaxPeriod, 150, false,"");

             }else {
              	  Utils.setHBoxLabel(lblId, 75, true, "Id");
              	  Utils.setHBoxLabel(lblDate, 100, true, "Date");
              	  Utils.setHBoxLabel(lblType, 150, true, "Note Type");
              	  Utils.setHBoxLabel(lblTaxPeriod, 150, true, "Tax Period");
             }         
             this.getChildren().addAll(lblId, lblDate, lblType, lblTaxPeriod);
        }
   }	

	public static class HBoxCell extends HBox 
	{
         Label lblName = new Label();
         Label lblAddress = new Label();

         HBoxCell(String sName, String sAddress, boolean headerRow)
         {
              super();

              if(sName == null) sName = "";             
              if(sAddress == null) sAddress = "";
              
              if(sName.contains("null")) sName = "";
              if(sAddress.contains("null")) sAddress = "";
              
              // check to see if we have a bad address from all nulls
              if(sAddress.equals(" ,  "))
            	  sAddress = "";
              
              lblName.setText(sName);
              lblName.setMinWidth(280);
              lblName.setMaxWidth(280);
              lblName.setPrefWidth(280);
              HBox.setHgrow(lblName, Priority.ALWAYS);

              lblAddress.setText(sAddress);
              lblAddress.setMinWidth(390);
              lblAddress.setMaxWidth(390);
              lblAddress.setPrefWidth(390);
              HBox.setHgrow(lblAddress, Priority.ALWAYS);

              if(headerRow == true) {
            	  lblName.setTextFill(Color.GREY);
            	  lblAddress.setTextFill(Color.GREY);
              }              
              this.getChildren().addAll(lblName, lblAddress);
         }
    }	
	
	private void saveEmployee()
	{
		if(DataManager.i().mEmployee != null && DataManager.i().mEmployee.getPerson() != null && DataManager.i().mEmployer != null)
		{
	  		empSaveButton.setStyle("-fx-background-color:lightgreen;");
	  		EtcAdmin.i().setStatusMessage("Saving Employee");
	  		EtcAdmin.i().setProgress(0.5);	
			

			// check to see if they want to save the SSN
			if (ssnEdited == true) {
				// verify
				Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
			    confirmAlert.setTitle("Changing SSN");
			    String confirmText = "Are You Sure You Want To Modify the SSN?";
			    confirmAlert.setContentText(confirmText);
			    EtcAdmin.i().positionAlertCenter(confirmAlert);
			    Optional<ButtonType> result = confirmAlert.showAndWait();
			    if ((result.isPresent()) && (result.get() != ButtonType.OK)) {
			    	ssnEdited = false;
			    }
			}
			
			try {
				// create a task to update
				// new thread
				Task<Void> task = new Task<Void>() 
				{
		            @Override
		            protected Void call() throws Exception
		            {
		            	doSave();
		            	return null;
		            }
		        };
		        
		    	task.setOnSucceeded(e ->  {
		    		finishSave();
		    	});
		    	task.setOnFailed(e ->  {
			    	DataManager.i().log(Level.SEVERE,task.getException());
		    		finishSave();
		    	});
		    	
		    	AdminApp.getInstance().getFxQueue().put(task);
			}catch(InterruptedException e)
			{
				logr.log(Level.SEVERE, "Exception.", e);
			}
		    catch (Exception e) {  DataManager.i().logGenericException(e); }
						
		}
	}
	
	private void doSave() {
		
			//create a batch id
			String newBatchId = UUID.randomUUID().toString();
			
			EmployeeRequest request = new EmployeeRequest();
			request.setBatchId(newBatchId);
			DataManager.i().mEmployee.getPerson().setFirstName(empFirstNameLabel.getText());
			DataManager.i().mEmployee.getPerson().setMiddleName(empMiddleNameLabel.getText());
			DataManager.i().mEmployee.getPerson().setLastName(empLastNameLabel.getText());
			DataManager.i().mEmployee.getPerson().setPhone(empPhoneLabel.getText());
			DataManager.i().mEmployee.getPerson().setEmail(empEmailLabel.getText());
			
			if (empGenderCombo.getValue() != null && empGenderCombo.getValue() != "") {
				GenderType gt = GenderType.valueOf(empGenderCombo.getValue());
				DataManager.i().mEmployee.getPerson().setGenderType(gt);
			} else
				DataManager.i().mEmployee.getPerson().setGenderType(null);
			
			DataManager.i().mEmployee.setVerified(empVerifiedCheck.isSelected());
			DataManager.i().mEmployee.setEmployerReference(empErReferenceLabel.getText());
			//calendar dates
			if(empDOBDate.getValue() != null)
				DataManager.i().mEmployee.getPerson().setDateOfBirth(Utils.getCalDate(empDOBDate.getValue()));
			else {
				DataManager.i().mEmployee.getPerson().setDateOfBirth(null);
			}
			// EMPLOYMENT PERIOD - straight copy to the update request
			if(DataManager.i().mEmployee.getEmploymentPeriod() != null) 
			{
				DataManager.i().mEmployee.getEmploymentPeriod().setHireDate(employee.getEmploymentPeriod().getHireDate());
				DataManager.i().mEmployee.getEmploymentPeriod().setTermDate(employee.getEmploymentPeriod().getTermDate());
			}

			if (empEmpStatusCombo.getValue() != null && empEmpStatusCombo.getValue() != "" ) {
				EmploymentStatusType esType = EmploymentStatusType.valueOf(empEmpStatusCombo.getValue());
				DataManager.i().mEmployee.setEmploymentStatusType(esType);
			} else 
				DataManager.i().mEmployee.setEmploymentStatusType(null);
			
			DataManager.i().mEmployee.setJobTitle(empJobTitleLabel.getText());

			// custom fields
			DataManager.i().mEmployee.setCustomField1(empCustomField1.getText());
			DataManager.i().mEmployee.setCustomField2(empCustomField2.getText());
			DataManager.i().mEmployee.setCustomField3(empCustomField3.getText());
			DataManager.i().mEmployee.setCustomField4(empCustomField4.getText());
			DataManager.i().mEmployee.setCustomField5(empCustomField5.getText());
			
			// SSN Update
			if (ssnEdited == true) {
				SSN ssn = new SSN(Utils.encryptSSN(empSSNLabel.getText()));
				DataManager.i().mEmployee.getPerson().setSsn(ssn);
			}

			if(empCompCombo.getValue() == null || empCompCombo.getValue().isEmpty()) 
			{
				DataManager.i().mEmployee.setPayCodeType(null);
			} else
				DataManager.i().mEmployee.setPayCodeType(PayCodeType.valueOf(empCompCombo.getValue()));
			
			//create a personrequest to set the created by
			PersonRequest pReq = new PersonRequest();
			pReq.setId(DataManager.i().mEmployee.getPersonId());
			pReq.setEntity(DataManager.i().mEmployee.getPerson());

			// MAIL ADDRESS
			PostalAddress mAddress = addressGrid.getUpdatedAddress();
			DataManager.i().mEmployee.getPerson().setMailAddress(mAddress);
			
			// update the address
			if (mAddress != null && addressGrid.isValid() == true) {
				PostalAddressRequest aRequest = new PostalAddressRequest();
				//aRequest.setBatchId(newBatchId);
				aRequest.setEntity(mAddress);
				//aRequest.setId(mAddress.getId());
				if (mAddress.getId() != null && mAddress.getId() != 0)
					aRequest.setId(mAddress.getId());
				//try {
				//	AdminPersistenceManager.getInstance().addOrUpdate(aRequest);
				//} catch (Exception e) {
		        //	DataManager.i().log(Level.SEVERE, e); 
				//}
				pReq.setMailAddressRequest(aRequest);
			}

			// update the employee
			request.setEntity(DataManager.i().mEmployee);
			request.setId(DataManager.i().mEmployee.getId());			
			request.setEmployerId(DataManager.i().mEmployer.getId());
			
			request.setPersonRequest(pReq);
			
			// update the employee
			try {
				DataManager.i().mEmployee = AdminPersistenceManager.getInstance().addOrUpdate(request);
			} catch (CoreException e) {
	        	DataManager.i().log(Level.SEVERE, e); 
			}
		    catch (Exception e) {  DataManager.i().logGenericException(e); }
			
			//update the ssn if modified
	  		EtcAdmin.i().setStatusMessage("Saving Employee Information");
	  		EtcAdmin.i().setProgress(0.75);		
	  		
	  		saveEmployeeInformationRequest();
			
			//update the edit history
			updateEditHistoryData();
	}
	
	private void finishSave() {
  		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0.0);		
  		empSaveButton.setStyle("");
		
		//load the regular ui
		toggleEditMode();
		
		// note that an employee was updated
		DataManager.i().mEmployeeUpdated = true;
	}
	
	private boolean validateData()
	{
		boolean bReturn = true;

		if( !Utils.validate(empFirstNameLabel)) bReturn = false;
		if( !Utils.validate(empLastNameLabel)) bReturn = false;
		if (ssnEdited == true) {
			if(Utils.validateSSNTextField(empSSNLabel) == false)
				bReturn = false;
		}
		
		//if(addressGrid.validateData() == false) bReturn = false;
		return bReturn;
	}	
	
	private void toggleEditMode() 
	{
		if(editMode == false) 
		{
			editMode = true;
			setFieldEdits(true);
			empEditButton.setText("Cancel");
			empData1Grid.setStyle("-fx-background-color: #eeffee");
			empData2Grid.setStyle("-fx-background-color: #eeffee");
			empSaveButton.setVisible(true);
		}else {
			editMode = false;
			setFieldEdits(false);
			empEditButton.setText("Edit");
			empData1Grid.getStyleClass().clear();
			empData1Grid.setStyle(null);	
			empData2Grid.getStyleClass().clear();
			empData2Grid.setStyle(null);	
			empSaveButton.setVisible(false);
		}
		showEmployee();
	}
	
	@FXML
	private void onShowPayroll() {
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employee/ViewPayroll.fxml"));
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
	private void onShow1095c() {
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		
		try {
			// load the b data
        	Irs1095bRequest bReq = new Irs1095bRequest();
        	bReq.setFetchInactive(true);
        	bReq.setEmployeeId(DataManager.i().mEmployee.getId());
        	DataManager.i().mIrs1095bs = AdminPersistenceManager.getInstance().getAll(bReq);
			
            // load the needed fxml
			if (DataManager.i().mIrs1095bs != null && DataManager.i().mIrs1095bs.size() > 0 ) {
		        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employee/ViewIRS1095b.fxml"));
				Parent ControllerNode = loader.load();
		        Stage stage = new Stage();
		        stage.initModality(Modality.APPLICATION_MODAL);
		        stage.initStyle(StageStyle.UNDECORATED);
		        stage.setScene(new Scene(ControllerNode));  
		        EtcAdmin.i().positionStageCenter(stage);
		        stage.showAndWait();
		        EtcAdmin.i().showMain();
			} else {
				// must be a C - load that data if available
            	Irs1095cRequest cReq = new Irs1095cRequest();
            	cReq.setFetchInactive(true);
            	cReq.setEmployeeId(DataManager.i().mEmployee.getId());
            	DataManager.i().mIrs1095cs = AdminPersistenceManager.getInstance().getAll(cReq);
            	if (DataManager.i().mIrs1095cs == null || DataManager.i().mIrs1095cs.size() == 0) {
            		Utils.showAlert("No Data", "There is no Irs1095B/C data for the selected employee to display");
            		return;
            	}
            	
            	// debug furistic LU timestamp - was giving 3 hours in the future.May have been a flulke but will monitor.
            	//List<Irs1095c> l95s = DataManager.i().mIrs1095cs;            	
            	//Calendar dt1 = l95s.get(0).getLastUpdated();
            	//String calString = Utils.getDateTimeString(dt1);
            	
		        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employee/ViewIRS1095c.fxml"));
				Parent ControllerNode = loader.load();
		        Stage stage = new Stage();
		        stage.initModality(Modality.APPLICATION_MODAL);
		        stage.initStyle(StageStyle.UNDECORATED);
		        stage.setScene(new Scene(ControllerNode));  
		        EtcAdmin.i().positionStageCenter(stage);
		        stage.showAndWait();
		        EtcAdmin.i().showMain();
			}
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        				
	}
	
	@FXML
	private void onShowCoverageMemberships() {
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employee/ViewCoverageMemberships.fxml"));
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
	private void onShowEmployeeCoverages() {
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employee/ViewEmployeeCoverage.fxml"));
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
	private void onShowCoveredIndividuals() {
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employee/ViewCoveredIndividuals.fxml"));
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
	private void onShowSystemInfo() {
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mEmployee;
			DataManager.i().mCurrentCoreDataType = SystemDataType.EMPLOYEE;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
			Parent ControllerNode = loader.load();
	        ViewCoreSystemInfoController sysInfoController = (ViewCoreSystemInfoController) loader.getController();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        EtcAdmin.i().showMain();
	        if (sysInfoController.changesMade == true)
	        	showEmployee();
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
	}
	
	@FXML
	private void onSSNEdit() {
		ssnEdited = true;
	}
	
	@FXML
	private void onRefreshData(ActionEvent event) {
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		
		refreshEmployeeData();
	}	

	@FXML
	private void onEdit(ActionEvent event) {
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		
		toggleEditMode();
	}
	
	@FXML
	private void onHistorySelected() {
		// reset the ssn view
		empSSNButton.setText("Show SSN");
		if(DataManager.i().mEmployee != null && DataManager.i().mEmployee.getPerson() != null && DataManager.i().mEmployee.getPerson().getSsn() != null)
			empSSNLabel.setText("XXX-XX-" + DataManager.i().mEmployee.getPerson().getSsn().getUsrln());
		else
			empSSNLabel.setText("XXX-XX-XXXX");
	
		showSelectedHistory();
	}
		
	@FXML
	private void onDelete(ActionEvent event) {
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Delete Employee");
	    String confirmText = "Are You Sure You Want To Delete this employee, id:";
	    confirmText += DataManager.i().mEmployee.getId().toString();
	    if (DataManager.i().mEmployee != null && DataManager.i().mEmployee.getPerson() != null) {
	    	confirmText += ", Name: ";
	    	confirmText += DataManager.i().mEmployee.getPerson().getFirstName();
	    	confirmText += " ";
	    	confirmText += DataManager.i().mEmployee.getPerson().getLastName();
	    }
	    confirmText += "?";
	    confirmAlert.setContentText(confirmText);
	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();
	    if ((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	return;
	    }
	    
	    // alert for now
	    Utils.showAlert("Function Not Active", "This functionality is not currently active");
	}	
		
	@FXML
	private void onNuclearDeactivate(ActionEvent event) {
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Nuclear Deactivate Employee");
	    String confirmText = "Are You Sure You Want To set this employee, id: ";
	    confirmText += DataManager.i().mEmployee.getId().toString();
	    if (DataManager.i().mEmployee != null && DataManager.i().mEmployee.getPerson() != null) {
	    	confirmText += ", Name: ";
	    	confirmText += DataManager.i().mEmployee.getPerson().getFirstName();
	    	confirmText += " ";
	    	confirmText += DataManager.i().mEmployee.getPerson().getLastName();
	    }
    	confirmText += " and all of its associated data inactive?";
	    confirmAlert.setContentText(confirmText);
	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();
	    if ((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	return;
	    }
	    
	    // alert for now
	    Utils.showAlert("Function Not Active", "Nuclear Deactivate option not available yet.");
	}	
		
/*	@FXML
	private void onCvgClassEdit(ActionEvent event) {
		toggleCvgClassEditMode();
	}	
		
	@FXML
	private void onCvlClassSave(ActionEvent event) {
	}	
*/	
	
	@FXML 
	private void onCalcIrs10945BC(ActionEvent event) 
	{ 
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}

		if (CalcTaxYearCombo.getSelectionModel().getSelectedItem() == null) {
		    Utils.showAlert("No Tax year Selected", "Please select a tax year first.");
			return;
		}
		
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Call Codes");
	    String confirmText = "Are You Sure You Want To Call Codes for this employee";
	    confirmText +=" for tax year ";
	    confirmText += CalcTaxYearCombo.getSelectionModel().getSelectedItem();
	    confirmText += "?";
	    confirmAlert.setContentText(confirmText);
	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();
	    if ((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	return;
	    }

	    if(DataManager.i().mAccount.getEmployers() == null) return;
		
		try {
			CalcQueueData calcData = new CalcQueueData(); 
			//iterate through all the account's employers
			TaxYear taxYear = null;
			// get the employer tax years
        	TaxYearRequest tyReq = new TaxYearRequest(); 
    		tyReq.setEmployerId(DataManager.i().mEmployer.getId());
    		DataManager.i().mEmployer.setTaxYears(AdminPersistenceManager.getInstance().getAll(tyReq));

			// select the tax year
			if (CalcTaxYearCombo.getSelectionModel().getSelectedItem() != null) {
				for (TaxYear ty : DataManager.i().mEmployer.getTaxYears()) {
					if (ty.getYear() == Integer.valueOf(CalcTaxYearCombo.getSelectionModel().getSelectedItem())){
						taxYear = ty;
						break;
					}
				}
			}
			
			// no tax year match, bail out
			if (taxYear == null) {
				Utils.showAlert("Tax Year Error", "Tax Year Mismatch, please double check selection.");
				return;
			}
			
			// and create using the tax year
			calcData.createIrs1094CCalculation(DataManager.i().mLocalUser, taxYear, DataManager.i().mEmployer, DataManager.i().mEmployee, false, null);
		} catch (CoreException e) {
			DataManager.i().log(Level.SEVERE, e);
			Utils.showAlert("Error", "There was a problem with the calling codes request. Please check the logs for more info.");
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

	} 
 
	
	@FXML
	private void onSave(ActionEvent event) 
	{
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		
		//validate everything
		if(!validateData())
			return;
		
		//update our object
		saveEmployee();
	}	
		
	@FXML
	private void onShowInactiveEmploymentPeriods() {
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		showEmploymentPeriods();
	}
		
	@FXML
	private void onAddEmploymentPeriod(ActionEvent event) 
	{
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		
		DataManager.i().mEmploymentPeriod = null;
		// and display the pop up
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employmentperiod/ViewEmploymentPeriod.fxml"));
			Parent ControllerNode = loader.load();
	        ViewEmploymentPeriodController periodController = (ViewEmploymentPeriodController) loader.getController();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        EtcAdmin.i().showMain();
	        if(periodController.changesMade == true)
	        	updateEmploymentPeriodData();
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	
		
	@FXML
	private void onAddEmployeeCoveragePeriod(ActionEvent event) 
	{
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coverage/ViewCoverage.fxml"));
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
	private void onAddNote(ActionEvent event) 
	{
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/notes/ViewNoteAdd.fxml"));
			Parent ControllerNode = loader.load();
	        ViewNoteAddController noteAddController = (ViewNoteAddController) loader.getController();
	        noteAddController.setScreenType(ScreenType.EMPLOYEE);
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if(noteAddController.changesMade == true)
	        	updateNotesData();
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	
		
	@FXML
	private void onShowHideSSN(ActionEvent event) 
	{
		if (historyMode==true) {
			showHistoryWarning();
			return;
		}
		
		if(empSSNButton.getText().contentEquals("Show SSN")) 
		{
			empSSNButton.setText("Hide SSN");
			if (DataManager.i().mEmployee != null && DataManager.i().mEmployee.getPerson() != null && DataManager.i().mEmployee.getPerson().getSsn()!=null)
			{
				DSSNRequest ssnRequest = new DSSNRequest();
				ssnRequest.setEmployeeId(DataManager.i().mEmployee.getId());
				ssnRequest.setId(DataManager.i().mEmployee.getId());
				DSSN dssn = null;
				try {
					dssn = AdminPersistenceManager.getInstance().get(ssnRequest);
				} catch (CoreException e) {
		        	DataManager.i().log(Level.SEVERE, e); 
				}
			    catch (Exception e) {  DataManager.i().logGenericException(e); }
				if (dssn != null)
					empSSNLabel.setText(dssn.getDssn());
				else
					empSSNLabel.setText("XXX-XX-XXXX");
			}
		} else {
			empSSNButton.setText("Show SSN");
			if(DataManager.i().mEmployee.getPerson().getSsn() != null)
				empSSNLabel.setText("XXX-XX-" + DataManager.i().mEmployee.getPerson().getSsn().getUsrln());
			else
				empSSNLabel.setText("XXX-XX-XXXX");
		}
		empSSNLabel.setStyle(null);
		ssnEdited = false;
	}
	
	private void saveEmployeeInformationRequest()
	{
		
		try {
			// update the Employee Information Table
			EmployeeInformationRequest eiRequest = new EmployeeInformationRequest();
			eiRequest.setEmployeeId(DataManager.i().mEmployee.getId());
			// get the current one
			List<EmployeeInformation> eis = AdminPersistenceManager.getInstance().getAll(eiRequest);
			// and update
			if (eis != null && eis.size() > 0) {	
				// need the full ssn for the dssn here for now
				DSSNRequest ssnRequest = new DSSNRequest();
				ssnRequest.setEmployeeId(DataManager.i().mEmployee.getId());
				ssnRequest.setId(DataManager.i().mEmployee.getId());
				DSSN dssn = AdminPersistenceManager.getInstance().get(ssnRequest);
				if (dssn != null)
					eis.get(0).setDssn(dssn.getDssn());
				eiRequest.setEntity(eis.get(0));
				eiRequest.setId(eis.get(0).getId());
				AdminPersistenceManager.getInstance().addOrUpdate(eiRequest);
			}
			
		} catch (Exception e) {
			Utils.showAlert("Employee Information Save Error", "Employee Information Request Save Error. Please check the app's logs for more information.");
			logr.log(Level.SEVERE, "Exception. ", e);
		}		
	}
	
	//extending the listview for our additional controls
	public static class HBoxEmploymentPeriodCell extends HBox 
	{
        Label lblId = new Label();
        Label lblHireDate = new Label();
        Label lblTerminationDate = new Label();
        EmploymentPeriod ep;

        public EmploymentPeriod getEmploymentPeriod() {
        	return ep;
        }
        
        HBoxEmploymentPeriodCell(EmploymentPeriod ep) 
        {
              super();

              this.ep = ep;
              if(ep != null) 
              {
               	  Utils.setHBoxLabel(lblId, 100, false, ep.getId());
               	  Utils.setHBoxLabel(lblHireDate, 175, false, ep.getHireDate());
               	  Utils.setHBoxLabel(lblTerminationDate, 175, false, ep.getTermDate());
	              if (ep.isDeleted() == true) {
	            	  lblId.setTextFill(Color.RED);
	            	  lblHireDate.setTextFill(Color.RED);
	            	  lblTerminationDate.setTextFill(Color.RED);
	              }
	               else {
	            	   if (ep.isActive() == false) {
	                 		lblId.setTextFill(Color.BLUE);
	                 		lblHireDate.setTextFill(Color.BLUE);
	                 		lblTerminationDate.setTextFill(Color.BLUE);
		               }
		               else {
	                 		lblId.setTextFill(Color.BLACK);
	                 		lblHireDate.setTextFill(Color.BLACK);
	                 		lblTerminationDate.setTextFill(Color.BLACK);
		               }
	               }
              } else {
               	  Utils.setHBoxLabel(lblId, 100, true, "Id");
               	  Utils.setHBoxLabel(lblHireDate, 175, true, "Hire Date");
               	  Utils.setHBoxLabel(lblTerminationDate, 175, true, "Term Date");
              }

           	  this.getChildren().addAll(lblId, lblHireDate, lblTerminationDate);                  
         }
    }	

	public static class EditHistoryData extends HBox 
	{
		Long sId;
        String sDate;
        String sType;
        String sName;
        int type = 0;
        Calendar date;
        EmployeeSnapshot employeeSnap;
        PersonSnapshot personSnap;
        PostalAddressSnapshot addressSnap;
        
        String getComboString() {
        	String comboString = sDate + " - " + sType;
        	if (sName != null && sName != "")
        		comboString += " - " + sName;
        	return comboString;
        }
        
        public EmployeeSnapshot getEmployeeSnapshot() {
        	return employeeSnap;
        }
        
        public PersonSnapshot getPersonSnapshot() {
        	return personSnap;
        }
        
        public PostalAddressSnapshot getAddressSnapshot() {
        	return addressSnap;
        }
        
        public int getSnapshotType() {
        	return type;
        }
        
        public Calendar getDate() {
        	return date;
        }
        
        EditHistoryData(EmployeeSnapshot es, PersonSnapshot ps, PostalAddressSnapshot as) 
        {
              super();

              employeeSnap = es;
              personSnap = ps;
              addressSnap = as;
    
              if(es != null) 
              {
            	  sId = es.getId();
               	  sDate = Utils.getDateTimeString(es.getLastUpdated());
               	  sType = "Employee Data";
               	  if (es.getCreatedBy() != null ) {
               		 sName = es.getCreatedBy().getFirstName() + " " + es.getCreatedBy().getLastName().substring(0,1) + ".";
               	  }
               	  type = 1;
               	  date=es.getLastUpdated();
              }
              
              if(ps != null) 
              {
            	  sId = ps.getId();
               	  sDate = Utils.getDateTimeString(ps.getLastUpdated());
               	  sType = "Person Data";
               	  if (ps.getCreatedBy() != null ) {
               		 sName = ps.getCreatedBy().getFirstName() + " " + ps.getCreatedBy().getLastName().substring(0,1) + ".";
               	  }
               	  type = 2;
               	  date=ps.getLastUpdated();
              }
              
              if(as != null) 
              {
            	  sId = as.getId();
               	  sDate = Utils.getDateTimeString(as.getLastUpdated());
               	  sType = "Address Data";
               	  if (as.getCreatedBy() != null ) {
               		 sName = as.getCreatedBy().getFirstName() + " " + as.getCreatedBy().getLastName().substring(0,1) + ".";
               	  }
               	  type = 3;
               	  date=as.getLastUpdated();
              }
         }
    }	

	public static class HBoxDependentCell extends HBox 
	{
         Label lblFirst = new Label();
         Label lblLast = new Label();
         Dependent dependent;
         
         public Dependent getDependent() {
        	 return dependent;
         }
         
         HBoxDependentCell(Dependent dependent) 
         {
              super();

              this.dependent = dependent;
              if(dependent != null) 
              {
            	  //String address = "";
            	  //String name = "";
            	  if(dependent.getPerson() != null) 
            	  {
            		  //name = dependent.getPerson().getFirstName() + " " + dependent.getPerson().getLastName();
	            	  Utils.setHBoxLabel(lblFirst, 200, false, dependent.getPerson().getFirstName());
	            	  Utils.setHBoxLabel(lblLast, 200, false, dependent.getPerson().getLastName());
		              if (dependent.isDeleted() == true) {
		                	lblFirst.setTextFill(Color.RED);
		                	lblLast.setTextFill(Color.RED);
		              }
		               else {
		            	   if (dependent.isActive() == false) {
		            		   lblFirst.setTextFill(Color.BLUE);
		            		   lblLast.setTextFill(Color.BLUE);
			               }
			               else {
			            	   lblFirst.setTextFill(Color.BLACK);
			                	lblLast.setTextFill(Color.BLACK);
			               }
		               }
            	  }
              } else {
               	  Utils.setHBoxLabel(lblFirst, 200, false, "First");
            	  Utils.setHBoxLabel(lblLast, 200, false, "Last");
              }
              this.getChildren().addAll(lblFirst, lblLast);
         }
    }	
}
