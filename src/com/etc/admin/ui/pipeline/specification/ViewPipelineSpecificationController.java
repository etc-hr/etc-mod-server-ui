package com.etc.admin.ui.pipeline.specification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.coresysteminfo.ViewCoreSystemInfoController;
import com.etc.admin.ui.employee.ViewEmployeeCoverageController.CoverageCell;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileStepHandler;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;
import com.etc.corvetto.ems.pipeline.rqs.PipelineFileStepHandlerRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineSpecificationRequest;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.DocumentType;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.UploadHandler;
import com.etc.corvetto.entities.UploadType;
import com.etc.corvetto.entities.UploadTypeTemplate;
import com.etc.corvetto.rqs.AccountRequest;
import com.etc.corvetto.rqs.CorvettoRequest;
import com.etc.corvetto.rqs.EmployerRequest;
import com.etc.corvetto.rqs.UploadTypeRequest;
import com.etc.corvetto.rqs.UploadTypeTemplateRequest;
import com.etc.entities.CoreData;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewPipelineSpecificationController 
{
	@FXML
	private TextField pspcUploadTypeFilter;
	@FXML
	private Label pspcInactiveLabel;
	@FXML
	private GridPane pspcSpecGridPane;
	@FXML
	private TextField pspcNameLabel;
	@FXML
	private TextField pspcDynamicFileSpecificationLabel;
	@FXML
	private TextField pspcDynamicFileCalculatorLabel;
	@FXML
	private ComboBox<String> pspcInitializerCombo;
	@FXML
	private ComboBox<String> pspcParserCombo;
	@FXML
	private ComboBox<String> pspcValidatorCombo;
	@FXML
	private ComboBox<String> pspcConverterCombo;
	@FXML
	private ComboBox<String> pspcFinalizerCombo;
	@FXML
	private TextField pspcExportSpecification;
	@FXML
	private ComboBox<String> pspcUploadTypeTemplateCombo;
	@FXML
	private Button pspcViewDynSpecButton;
	@FXML
	private Button pspcViewCalcButton;
	@FXML
	private Button pspcEditButton;
	@FXML
	private Button pspcSaveButton;
	@FXML
	public CheckBox pspcPostValidationApprovalCheck;
	// UPLOAD TYPES TEMPLATE
	@FXML
	private Button pspcTemplatesButton;
	@FXML
	private ListView<TemplateCell> pspcTemplatesListView;
	@FXML
	private Label pspcTemplatesLabel;
	// UPLOADTYPE
	@FXML
	private CheckBox pspcUploadTypeShowInactive;
	@FXML
	private Label pspcUploadTitleLabel;
	@FXML
	private Label pspcUploadPrompt;
	@FXML
	private TableView<HBoxCell> pspcUploadTypeTableView;
	@FXML
	private Button pspcUploadAddButton;
	//UPLOADTYPE DETAIL
	@FXML
	private Label pspcUploadTypeInactiveLabel;
	@FXML
	public GridPane pspcUploadTypeGrid;
	@FXML
	public TextField pspcUploadName;
	@FXML
	public TextField pspcUploadDescription;
	@FXML
	public CheckBox pspsUploadEncryptedCheck;
	@FXML
	public CheckBox pspsUploadEnabledCheck;
	@FXML
	public Button pspcUploadEditCancelButton;
	@FXML
	public Button pspcUploadSaveButton;
	@FXML
	public Button pspcUploadAccountSetButton;
	@FXML
	public TextField pspcUploadAccountId;
	@FXML
	public TextField pspcUploadAccount;
	@FXML
	public Button pspcUploadEmployerSetButton;
	@FXML
	public TextField pspcUploadEmployerId;
	@FXML
	public TextField pspcUploadEmployer;
	@FXML
	public Button pspcUploadDocTypeSetButton;
	@FXML
	public TextField pspcUploadDocTypeId;
	@FXML
	public TextField pspcUploadDocType;
	@FXML
	public Button pspcUploadHandlerSetButton;
	@FXML
	public TextField pspcUploadHandlerId;
	@FXML
	public TextField pspcUploadHandler;
	//Delete Buttons
	@FXML
	public Button acctDelete;
	@FXML
	public Button emprDelete;
	@FXML
	public Button docTpDelete;
	@FXML
	public Button hndlrDelete;
	// our upload types
	private List<UploadType> uploadTypes = null;
	// selected uploadType
	UploadType currentUploadType = null;
	//selected uploadtype index
	Integer selectedUploadTypeIndex = 0;
	//track what mode the uploadtype controls are in
	EditMode uploadTypeMode;
	//table sort 
	TableColumn<HBoxCell, String> sortColumn = null;
	
    List<PipelineFileStepHandler> initializers = new ArrayList<>();
    List<PipelineFileStepHandler> parsers = new ArrayList<>();
    List<PipelineFileStepHandler> validators = new ArrayList<>();
    List<PipelineFileStepHandler> converters = new ArrayList<>();
    List<PipelineFileStepHandler> finalizers = new ArrayList<>();
    
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize()
	{
		try {
			if (DataManager.i().mPipelineChannel == null) {
				Utils.alertUser("Error","The Pipeline Channel for this Specification is Null");
			}
			
			initControls();
			
			//reset the currentUploadType
			currentUploadType = null;
			
			// Disable ComboBox until Edit Button Pressed
			pspcInitializerCombo.setDisable(true);
			pspcParserCombo.setDisable(true);
			pspcValidatorCombo.setDisable(true);
			pspcConverterCombo.setDisable(true);
			pspcFinalizerCombo.setDisable(true);
	
			//Set the ComboBox value to empty string for proper refresh
			pspcInitializerCombo.setValue("");
			pspcParserCombo.setValue("");
			pspcValidatorCombo.setValue("");
			pspcConverterCombo.setValue("");
			pspcFinalizerCombo.setValue("");
	
			// Setup the file step handler combos
		    initializers.clear();
		    parsers.clear();
		    validators.clear();
		    converters.clear();
		    finalizers.clear();
		    
		    //reset the text fields
			pspcNameLabel.setText("");
			pspcNameLabel.setStyle(null);
	
		    //separate into types for use on selection
		    for(PipelineFileStepHandler pipelineFileStepHandler : DataManager.i().mPipelineFileStepHandlers)
		    {
		    	switch(pipelineFileStepHandler.getType()) 
		    	{
			    	case INITIALIZER:
			    		initializers.add(pipelineFileStepHandler);
			    		break;
			    	case PARSER:
			    		parsers.add(pipelineFileStepHandler);
			    		break;
			    	case VALIDATOR:
			    		validators.add(pipelineFileStepHandler);
			    		break;
			    	case CONVERTER:
			    		converters.add(pipelineFileStepHandler);
			    		break;
			    	case FINALIZER:
			    		finalizers.add(pipelineFileStepHandler);
			    		break;
		    	}
		    }
		    
		    // initializers
		    List<String> initializerList =  new ArrayList<>();
		    for(PipelineFileStepHandler initializer : initializers) 
		    {
		    	switch(DataManager.i().mPipelineChannel.getType())
		    	{
			    	case COVERAGE:
			    		if(!initializer.getName().contains("Employee") && !initializer.getName().contains("Pay") && !initializer.getName().contains("Plan") 
			    		&& !initializer.getName().contains("Dwi") && !initializer.getName().contains("AIR"))
			    	    	initializerList.add(initializer.getName());
			    		break;
			    	case EMPLOYEE:
			    		if(!initializer.getName().contains("Coverage") && !initializer.getName().contains("Pay") && !initializer.getName().contains("Plan") 
			    		&& !initializer.getName().contains("Dwi") && !initializer.getName().contains("AIR"))
			    			initializerList.add(initializer.getName());
			    		break;
			    	case PAY:
			    		if(!initializer.getName().contains("Employee") && !initializer.getName().contains("Coverage") && !initializer.getName().contains("Plan") 
			    		&& !initializer.getName().contains("Dwi") && !initializer.getName().contains("AIR"))
			    			initializerList.add(initializer.getName());
			    		break;
			    	case INSURANCE:
			    		if(!initializer.getName().contains("Employee") && !initializer.getName().contains("Pay") && !initializer.getName().contains("Coverage") 
			    		&& !initializer.getName().contains("Dwi") && !initializer.getName().contains("AIR"))
			    			initializerList.add(initializer.getName());
			    		break;
			    	case DEDUCTION:
			    		if(!initializer.getName().contains("Employee") && !initializer.getName().contains("Pay") && !initializer.getName().contains("Plan") 
			    		&& !initializer.getName().contains("Dwi") && !initializer.getName().contains("AIR"))
			    			initializerList.add(initializer.getName());
			    		break;
			    	case IRS1095C:
			    		if(!initializer.getName().contains("Employee") && !initializer.getName().contains("Pay") && !initializer.getName().contains("Plan") 
			    		&& !initializer.getName().contains("Dwi") && !initializer.getName().contains("AIR"))
			    			initializerList.add(initializer.getName());
			    		break;
			    	case BENEFIT:
			    		if(!initializer.getName().contains("Employee") && !initializer.getName().contains("Pay") && !initializer.getName().contains("Irs1095c") 
			    		&& !initializer.getName().contains("Dwi") && !initializer.getName().contains("AIR"))
			    			initializerList.add(initializer.getName());
			    		break;
			    	case PAYPERIOD:
			    		if(!initializer.getName().contains("Employee") && !initializer.getName().contains("Plan") 
			    		&& !initializer.getName().contains("Dwi") && !initializer.getName().contains("AIR"))
			    			initializerList.add(initializer.getName());
			    		break;
					default:
						break;
		    	}
		    }
		    ObservableList<String> observableInitializerList = FXCollections.observableList(initializerList);
		    pspcInitializerCombo.setItems(observableInitializerList);
		    pspcInitializerCombo.getItems().add("");
		    
			// parsers
		    List<String> parserList =  new ArrayList<>();
		    for(PipelineFileStepHandler parser : parsers) 
		    {
		    	switch(DataManager.i().mPipelineChannel.getType())
		    	{
			    	case COVERAGE:
			    		if(parser.getName().contains("Coverage") || parser.getName().contains("Workforce") || parser.getName().contains("Premium") || parser.getName().contains("Share")
			    	    || parser.getName().contains("Exception"))
			    			parserList.add(parser.getName());
			    		break;
			    	case EMPLOYEE:
			    		if(parser.getName().contains("Employee") || parser.getName().contains("Workforce") || parser.getName().contains("Premium") || parser.getName().contains("Share")
					    || parser.getName().contains("Exception"))
			    			parserList.add(parser.getName());
			    		break;
			    	case PAY:
			    		if(parser.getName().contains("Pay") || parser.getName().contains("Workforce") || parser.getName().contains("Premium") || parser.getName().contains("Share")
					    || parser.getName().contains("Exception"))
			    			parserList.add(parser.getName());
			    		break;
			    	case INSURANCE:
			    		if(parser.getName().contains("Insurance") || parser.getName().contains("Workforce") || parser.getName().contains("Premium") || parser.getName().contains("Share")
					    || parser.getName().contains("Exception"))
			    			parserList.add(parser.getName());
			    		break;
			    	case DEDUCTION:
			    		if(parser.getName().contains("Deduction") || parser.getName().contains("Workforce") || parser.getName().contains("Premium") || parser.getName().contains("Share")
					    || parser.getName().contains("Exception"))
			    			parserList.add(parser.getName());
			    		break;
			    	case IRS1095C:
			    		if(parser.getName().contains("IRS1095C") || parser.getName().contains("Workforce") || parser.getName().contains("Premium") || parser.getName().contains("Share")
					    || parser.getName().contains("Exception"))
			    			parserList.add(parser.getName());
			    		break;
			    	case BENEFIT:
			    		if(parser.getName().contains("Plan") || parser.getName().contains("Workforce") || parser.getName().contains("Premium") || parser.getName().contains("Share")
					    || parser.getName().contains("Exception"))
			    			parserList.add(parser.getName());
			    		break;
			    	case PAYPERIOD:
			    		if(parser.getName().contains("Pay") || parser.getName().contains("Workforce") || parser.getName().contains("Premium") || parser.getName().contains("Share")
					    || parser.getName().contains("Exception"))
			    			parserList.add(parser.getName());
			    		break;
					default:
						break;
		    	}
		    }
		    ObservableList<String> observableParserList = FXCollections.observableList(parserList);
		    pspcParserCombo.setItems(observableParserList);
		    
			// validators
		    List<String> validatorList =  new ArrayList<>();
		    for(PipelineFileStepHandler validator : validators) 
		    {
		    	switch(DataManager.i().mPipelineChannel.getType())
		    	{
			    	case COVERAGE:
			    		if(validator.getName().contains("Coverage") || validator.getName().contains("Premium") || validator.getName().contains("Share"))
			    			validatorList.add(validator.getName());
			    		break;
			    	case EMPLOYEE:
			    		if(validator.getName().contains("Employee") || validator.getName().contains("Premium") || validator.getName().contains("Share"))
			    			validatorList.add(validator.getName());
			    		break;
			    	case PAY:
			    		if(validator.getName().contains("Pay") || validator.getName().contains("Premium") || validator.getName().contains("Share"))
			    			validatorList.add(validator.getName());
			    		break;
			    	case INSURANCE:
			    		if(validator.getName().contains("Insurance") || validator.getName().contains("Premium") || validator.getName().contains("Share"))
			    			validatorList.add(validator.getName());
			    		break;
			    	case DEDUCTION:
			    		if(validator.getName().contains("Deduction") || validator.getName().contains("Premium") || validator.getName().contains("Share"))
			    			validatorList.add(validator.getName());
			    		break;
			    	case IRS1095C:
			    		if(validator.getName().contains("IRS1095C") || validator.getName().contains("Premium") || validator.getName().contains("Share"))
			    			validatorList.add(validator.getName());
			    		break;
			    	case BENEFIT:
			    		if(validator.getName().contains("Plan") || validator.getName().contains("Premium") || validator.getName().contains("Share"))
			    			validatorList.add(validator.getName());
			    		break;
			    	case PAYPERIOD:
			    		if(validator.getName().contains("Pay") || validator.getName().contains("Premium") || validator.getName().contains("Share"))
			    			validatorList.add(validator.getName());
			    		break;
					default:
						break;
		    	}
		    }
		    ObservableList<String> observableValidatorList = FXCollections.observableList(validatorList);
		    pspcValidatorCombo.setItems(observableValidatorList);
		    pspcValidatorCombo.getItems().add("");
		    
			// converters
		    List<String> converterList =  new ArrayList<>();
		    for(PipelineFileStepHandler converter : converters) 
		    {
		    	switch(DataManager.i().mPipelineChannel.getType())
		    	{
			    	case COVERAGE:
			    		if(converter.getName().contains("Coverage") || converter.getName().contains("Workforce") || converter.getName().contains("Premium") || converter.getName().contains("Share")
			    		|| converter.getName().contains("MEWA") || converter.getName().contains("Exception"))
			    			converterList.add(converter.getName());
			    		break;
			    	case EMPLOYEE:
			    		if(converter.getName().contains("Employee") || converter.getName().contains("Workforce") || converter.getName().contains("Premium") || converter.getName().contains("Share")
			    		|| converter.getName().contains("MEWA") || converter.getName().contains("Exception"))
			    			converterList.add(converter.getName());
			    		break;
			    	case PAY:
			    		if(converter.getName().contains("Pay") || converter.getName().contains("Workforce") || converter.getName().contains("Premium") || converter.getName().contains("Share")
			    		|| converter.getName().contains("MEWA") || converter.getName().contains("Exception"))
			    			converterList.add(converter.getName());
			    		break;
			    	case INSURANCE:
			    		if(converter.getName().contains("Insurance") || converter.getName().contains("Workforce") || converter.getName().contains("Premium") || converter.getName().contains("Share")
			    		|| converter.getName().contains("MEWA") || converter.getName().contains("Exception"))
			    			converterList.add(converter.getName());
			    		break;
			    	case DEDUCTION:
			    		if(converter.getName().contains("Deduction") || converter.getName().contains("Workforce") || converter.getName().contains("Premium") || converter.getName().contains("Share")
			    		|| converter.getName().contains("MEWA") || converter.getName().contains("Exception"))
			    			converterList.add(converter.getName());
			    		break;
			    	case IRS1095C:
			    		if(converter.getName().contains("IRS1095C") || converter.getName().contains("Workforce") || converter.getName().contains("Premium") || converter.getName().contains("Share")
			    		|| converter.getName().contains("MEWA") || converter.getName().contains("Exception"))
			    			converterList.add(converter.getName());
			    		break;
			    	case BENEFIT:
			    		if(converter.getName().contains("Plan") || converter.getName().contains("Workforce") || converter.getName().contains("Premium") || converter.getName().contains("Share")
			    		|| converter.getName().contains("MEWA") || converter.getName().contains("Exception"))
			    			converterList.add(converter.getName());
			    		break;
			    	case PAYPERIOD:
			    		if(converter.getName().contains("Pay") || converter.getName().contains("Workforce") || converter.getName().contains("Premium") || converter.getName().contains("Share")
			    		|| converter.getName().contains("MEWA") || converter.getName().contains("Exception"))
			    			converterList.add(converter.getName());
			    		break;
					default:
						break;
		    	}
		    }
		    ObservableList<String> observableConverterList = FXCollections.observableList(converterList);
		    pspcConverterCombo.setItems(observableConverterList);
		    
			// finalizers
		    List<String> finalizerList =  new ArrayList<>();
		    for(PipelineFileStepHandler finalizer : finalizers) 
		    {
		    	switch(DataManager.i().mPipelineChannel.getType())
		    	{
			    	case COVERAGE:
			    		if(finalizer.getName().contains("Coverage"))
			    			finalizerList.add(finalizer.getName());
			    		break;
			    	case EMPLOYEE:
			    		if(finalizer.getName().contains("Employee"))
			    			finalizerList.add(finalizer.getName());
			    		break;
			    	case PAY:
			    		if(finalizer.getName().contains("Pay"))
			    			finalizerList.add(finalizer.getName());
			    		break;
			    	case INSURANCE:
			    		if(finalizer.getName().contains("Insurance"))
			    			finalizerList.add(finalizer.getName());
			    		break;
			    	case DEDUCTION:
			    		if(finalizer.getName().contains("Deduction"))
			    			finalizerList.add(finalizer.getName());
			    		break;
			    	case IRS1095C:
			    		if(finalizer.getName().contains("Irs1095c"))
			    			finalizerList.add(finalizer.getName());
			    		break;
			    	case BENEFIT:
			    		if(finalizer.getName().contains("Benefit"))
			    			finalizerList.add(finalizer.getName());
			    		break;
			    	case PAYPERIOD:
			    		if(finalizer.getName().contains("Pay"))
			    			finalizerList.add(finalizer.getName());
			    		break;
					default:
						break;
		    	}
		    }
		    ObservableList<String> observableFinalizerList = FXCollections.observableList(finalizerList);
		    pspcFinalizerCombo.setItems(observableFinalizerList);
		    pspcFinalizerCombo.getItems().add("");
			
			EtcAdmin.i().setStatusMessage("loading Specification Data...");
			EtcAdmin.i().setProgress(.5);
			
			try {
				DataManager.i().mUploadHandlers = AdminPersistenceManager.getInstance().getAll(new CorvettoRequest<UploadHandler>(UploadHandler.class));
				DataManager.i().mDocumentTypes = AdminPersistenceManager.getInstance().getAll(new CorvettoRequest<DocumentType>(DocumentType.class));
			} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
		    catch (Exception e) {  DataManager.i().logGenericException(e); }
	
			Task<Void> task = new Task<Void>()
			{
	            @Override
	            protected Void call() throws Exception
	            {
	        		DataManager.i().loadDynamicFileSpecification(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
	        		EtcAdmin.i().setProgress(.5);
	                return null;
	            }
	        };
	
	    	task.setOnSucceeded(e ->   showPipelineSpecification());
	    	task.setOnFailed(e ->   showPipelineSpecification());
	        new Thread(task).start();
		}catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		}
	}

	private void initControls() 
	{
		// hide the inactive flag as everything is loaded
		pspcInactiveLabel.setVisible(false);
		pspcUploadTypeInactiveLabel.setVisible(false);
		// hide the save button
		pspcSaveButton.setVisible(false);
		pspcSpecGridPane.getStyleClass().clear();
		pspcSpecGridPane.setStyle(null);	
		// set the upload type controls
		setUploadTypeControls(EditMode.VIEW);
		// clear the controls
		clearUploadTypeData();
		//enable the calc button
		pspcViewCalcButton.setVisible(false);
		acctDelete.setVisible(false);
		emprDelete.setVisible(false);
		docTpDelete.setVisible(false);
		hndlrDelete.setVisible(false);
		
		//set the columns for the upload type table
		setTableColumns();
		
		// set a handler for the UploadType double click
		pspcUploadTypeTableView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) 
            {
            	// offset one for the header row
       		 	currentUploadType = pspcUploadTypeTableView.getSelectionModel().getSelectedItem().getUploadType();
       		 	selectedUploadTypeIndex = pspcUploadTypeTableView.getSelectionModel().getSelectedIndex();
       		 	//load the selected UploadType to the screen
       		 	loadCurrentUploadTypeData();
            }
        });
	}
	
	private void setTableColumns() 
	{
		//clear the default values
		pspcUploadTypeTableView.getColumns().clear();

	    TableColumn<HBoxCell, String> x1 = new TableColumn<HBoxCell, String>("Id");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("utId"));
		x1.setMinWidth(35);
		//x1.setComparator((String o1, String o2) -> { return Utils.compareNumberStrings(o1, o2); });
		//sortColumn = x1;
		//sortColumn.setSortType(SortType.DESCENDING);
		pspcUploadTypeTableView.getColumns().add(x1);
		setUploadTypeCellFactory(x1);
		
	    TableColumn<HBoxCell, String> x2 = new TableColumn<HBoxCell, String>("Name");
		x2.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("name"));
		x2.setMinWidth(150);
		pspcUploadTypeTableView.getColumns().add(x2);
		setUploadTypeCellFactory(x2);
		
		TableColumn<HBoxCell, String> x3 = new TableColumn<HBoxCell, String>("Employer");
		x3.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("employer"));
		x3.setMinWidth(300);
		sortColumn = x3;
		sortColumn.setSortType(SortType.DESCENDING);
		pspcUploadTypeTableView.getColumns().add(x3);
		setUploadTypeCellFactory(x3);
		
	    TableColumn<HBoxCell, String> x4 = new TableColumn<HBoxCell, String>("Account");
	    x4.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("account"));
		x4.setMinWidth(300);
		pspcUploadTypeTableView.getColumns().add(x4);
		setUploadTypeCellFactory(x4);
		
	}
	
	private void setUploadTypeCellFactory(TableColumn<HBoxCell, String>  col) 
	{
		col.setCellFactory(column -> 
		{
		    return new TableCell<HBoxCell, String>() 
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
		                HBoxCell cell = getTableView().getItems().get(getIndex());
		                if (cell.getUploadType().isDeleted() == true)
		                	setTextFill(Color.RED);
		                else {
			                if (cell.getUploadType().isActive() == false)
			                	setTextFill(Color.BLUE);
			                else
			                	setTextFill(Color.BLACK);
		                }
		            }
		        }
		    };
		});
	}
	
	
	
	public void clearScreen()
	{
		currentUploadType = null;
		clearUploadTypeData();
		setUploadTypeControls(EditMode.VIEW);	
	}
	
	private void setSpecEditMode(boolean mode) 
	{
		if(mode == true) 
		{
			pspcEditButton.setText("Cancel");
			pspcSpecGridPane.setStyle("-fx-background-color: #eeffee");
		}else {
			pspcEditButton.setText("Edit");
			pspcSpecGridPane.getStyleClass().clear();
			pspcSpecGridPane.setStyle(null);	
		}
		pspcInitializerCombo.setDisable(!mode);
		pspcParserCombo.setDisable(!mode);
		pspcValidatorCombo.setDisable(!mode);
		pspcConverterCombo.setDisable(!mode);
		pspcFinalizerCombo.setDisable(!mode);
				
		pspcSaveButton.setVisible(mode);
		pspcNameLabel.setEditable(mode);
		pspcExportSpecification.setEditable(mode);
		pspcDynamicFileSpecificationLabel.setEditable(mode);
		pspcDynamicFileCalculatorLabel.setEditable(mode);
		pspcInitializerCombo.setEditable(mode);
		pspcParserCombo.setEditable(mode);
		pspcValidatorCombo.setEditable(mode);
		pspcConverterCombo.setEditable(mode);
		pspcFinalizerCombo.setEditable(mode);
		pspcPostValidationApprovalCheck.setDisable(!mode);
		
		//need to update because combo boxes reset their edit controls when editiable changes
		showPipelineSpecification();
	}	
	
	private void clearUploadTypeData() 
	{
		Utils.updateControl(pspcUploadName, "");
		Utils.updateControl(pspcUploadDescription, "");
		Utils.updateControl(pspsUploadEncryptedCheck, false);
		Utils.updateControl(pspsUploadEnabledCheck, false);
		Utils.updateControl(pspcUploadAccountId, "");
		Utils.updateControl(pspcUploadAccount, "");
		Utils.updateControl(pspcUploadEmployerId, "");
		Utils.updateControl(pspcUploadEmployer, "");
		Utils.updateControl(pspcUploadDocTypeId, "");
		Utils.updateControl(pspcUploadDocType, "");
		Utils.updateControl(pspcUploadHandlerId, "");
		Utils.updateControl(pspcUploadHandler, "");
	}
	
	
	private void setUploadTypeControls(EditMode mode) 
	{
		// save the state
		uploadTypeMode = mode;
		
		boolean controlState = false;
		String button1Text = "Edit";
		String button2Text = "Save";
		pspcUploadTypeGrid.getStyleClass().clear();
		pspcUploadTypeGrid.setStyle(null);	
		acctDelete.setVisible(false);
		emprDelete.setVisible(false);
		docTpDelete.setVisible(false);
		hndlrDelete.setVisible(false);

		switch(mode)
		{
			case VIEW:
				pspcTemplatesListView.setVisible(false);
				pspcTemplatesLabel.setVisible(false);
				pspcTemplatesButton.setVisible(false);
				pspcUploadAddButton.setVisible(true);
				pspcUploadName.setPromptText("<< Select UploadType from the list to view details");
				pspcUploadPrompt.setText("Selected Upload Type");
				pspcUploadTypeTableView.setDisable(false);
				break;
			case EDIT:
				pspcUploadTypeGrid.setStyle("-fx-background-color: #eeffee");
				controlState = true;
				button1Text = "Cancel";
				pspcTemplatesListView.setVisible(false);
				pspcTemplatesLabel.setVisible(false);
				pspcTemplatesButton.setVisible(false);
				pspcUploadAddButton.setVisible(false);
				pspcUploadName.setPromptText("");
				pspcUploadPrompt.setText("Edit Upload Type");
				//if(pspcUploadAccount.getText().isEmpty() == false) { pspcUploadEmployerSetButton.setDisable(true); } else pspcUploadEmployerSetButton.setDisable(false);
				//if(pspcUploadEmployer.getText().isEmpty() == false) { pspcUploadAccountSetButton.setDisable(true); } else pspcUploadAccountSetButton.setDisable(false);
				pspcUploadTypeTableView.setDisable(true);
				enableDelete();
				break;
			case ADD:
				pspcUploadTypeGrid.setStyle("-fx-background-color: #eeffff");
				controlState = true;
				button1Text = "Cancel";
				button2Text = "Add";
				pspsUploadEncryptedCheck.setSelected(true);
				pspcUploadName.setPromptText("");
				pspcUploadPrompt.setText("Add Upload Type");
				//loadTemplateList();
				//pspcTemplatesListView.setVisible(true);
				//pspcTemplatesLabel.setVisible(true);
				//pspcTemplatesButton.setVisible(true);
				pspcUploadAddButton.setVisible(false);
			break;
		}

		pspcUploadName.setEditable(controlState);
		pspcUploadDescription.setEditable(controlState);
		pspsUploadEncryptedCheck.setDisable(!controlState);
		pspsUploadEnabledCheck.setDisable(!controlState);
		pspcUploadEditCancelButton.setText(button1Text);
		pspcUploadSaveButton.setText(button2Text);
		pspcUploadSaveButton.setVisible(controlState);
		pspcUploadAccountSetButton.setVisible(controlState);
		pspcUploadEmployerSetButton.setVisible(controlState);
		pspcUploadDocTypeSetButton.setVisible(controlState);
		pspcUploadHandlerSetButton.setVisible(controlState);
		
		// these fields stay read only and are changed by the set button
		pspcUploadAccountId.setEditable(false);
		pspcUploadAccount.setEditable(false);
		pspcUploadEmployerId.setEditable(false);
		pspcUploadEmployer.setEditable(false);
		pspcUploadDocTypeId.setEditable(false);
		pspcUploadDocType.setEditable(false);
		pspcUploadHandlerId.setEditable(false);
		pspcUploadHandler.setEditable(false);
		
		// clear any formatting from a failed save
		pspcUploadName.getStyleClass().clear();
		pspcUploadDescription.getStyleClass().clear();
		pspcUploadAccountId.getStyleClass().clear();
		pspcUploadAccount.getStyleClass().clear();
		pspcUploadEmployerId.getStyleClass().clear();
		pspcUploadEmployer.getStyleClass().clear();
		pspcUploadDocTypeId.getStyleClass().clear();
		pspcUploadDocType.getStyleClass().clear();
		pspcUploadHandlerId.getStyleClass().clear();
		pspcUploadHandler.getStyleClass().clear();
		
		pspcUploadName.setStyle(null);
		pspcUploadDescription.setStyle(null);
		pspcUploadAccountId.setStyle(null);
		pspcUploadAccount.setStyle(null);
		pspcUploadEmployerId.setStyle(null);
		pspcUploadEmployer.setStyle(null);
		pspcUploadDocTypeId.setStyle(null);
		pspcUploadDocType.setStyle(null);
		pspcUploadHandlerId.setStyle(null);
		pspcUploadHandler.setStyle(null);
		
		pspcUploadName.getStyleClass().add("text-field");
		pspcUploadDescription.getStyleClass().add("text-field");
		pspcUploadAccountId.getStyleClass().add("text-field");
		pspcUploadAccount.getStyleClass().add("text-field");
		pspcUploadEmployerId.getStyleClass().add("text-field");
		pspcUploadEmployer.getStyleClass().add("text-field");
		pspcUploadDocTypeId.getStyleClass().add("text-field");
		pspcUploadDocType.getStyleClass().add("text-field");
		pspcUploadHandlerId.getStyleClass().add("text-field");
		pspcUploadHandler.getStyleClass().add("text-field");
		
		pspcUploadName.getStyleClass().add("text-input");
		pspcUploadDescription.getStyleClass().add("text-input");
		pspcUploadAccountId.getStyleClass().add("text-input");
		pspcUploadAccount.getStyleClass().add("text-input");
		pspcUploadEmployerId.getStyleClass().add("text-input");
		pspcUploadEmployer.getStyleClass().add("text-input");
		pspcUploadDocTypeId.getStyleClass().add("text-input");
		pspcUploadDocType.getStyleClass().add("text-input");
		pspcUploadHandlerId.getStyleClass().add("text-input");
		pspcUploadHandler.getStyleClass().add("text-input");
		
	}

	private void enableDelete()
	{
		if(pspcUploadAccount.getText().isEmpty() == false) { acctDelete.setVisible(true); }
		if(pspcUploadEmployer.getText().isEmpty() == false) { emprDelete.setVisible(true); }
		if(pspcUploadDocType.getText().isEmpty() == false) { docTpDelete.setVisible(true); }
		if(pspcUploadHandler.getText().isEmpty() == false) { hndlrDelete.setVisible(true); }
		//if(pspcUploadAccount.getText().isEmpty() == true && pspcUploadEmployer.getText().isEmpty() == true)
		//{
		//	pspcUploadAccountSetButton.setDisable(false);
		//	pspcUploadEmployerSetButton.setDisable(false);
		//}
	}
	
	private void showPipelineSpecification()
	{
		PipelineSpecification pSpecification = DataManager.i().mPipelineSpecification;
		
		if(pSpecification != null) 
		{
			// set inactive/deleted warning
			if (pSpecification.isDeleted()) {
				pspcInactiveLabel.setText("DELETED");
				pspcInactiveLabel.setVisible(true);
			} else {
				if (pSpecification.isActive() == false) {
					pspcInactiveLabel.setText("INACTIVE");
					pspcInactiveLabel.setVisible(true);
				} else {
					pspcInactiveLabel.setVisible(false);
				}
			}
				
			Utils.updateControl(pspcNameLabel,pSpecification.getName());
			if(pSpecification.getExportSpecification() != null)
				Utils.updateControl(pspcExportSpecification, pSpecification.getExportSpecification().getName());

			pspcDynamicFileSpecificationLabel.setText(String.valueOf(pSpecification.getDynamicFileSpecificationId()));
			if(pSpecification.getDynamicFileCalculatorId() != null && pSpecification.getDynamicFileCalculatorId() != 0)
				pspcDynamicFileCalculatorLabel.setText(String.valueOf(pSpecification.getDynamicFileCalculatorId()));
			else
				pspcDynamicFileCalculatorLabel.setText("");
			if(pSpecification.getInitializer() != null) {
				pspcInitializerCombo.getSelectionModel().select(String.valueOf(pSpecification.getInitializer().getName()));
			}
			if(pSpecification.getParser() != null) {
				pspcParserCombo.getSelectionModel().select(String.valueOf(pSpecification.getParser().getName()));
			}
			if(pSpecification.getValidator() != null) {
				pspcValidatorCombo.getSelectionModel().select(String.valueOf(pSpecification.getValidator().getName()));
			}
			if(pSpecification.getConverter() != null) {
				pspcConverterCombo.getSelectionModel().select(String.valueOf(pSpecification.getConverter().getName()));
			}
			if(pSpecification.getFinalizer() != null) {
				pspcFinalizerCombo.getSelectionModel().select(String.valueOf(pSpecification.getFinalizer().getName()));
			}
			Utils.updateControl(pspcPostValidationApprovalCheck, pSpecification.isPostValidationApproval());
		}
		
		//set the dynamic file button accordingly
		if(pSpecification.getChannel() != null) 
		{
			switch (pSpecification.getChannel().getType())
			{
				case IRS1094C:
//				case IRS1095C:
				case IRSAIRERR:
				case IRSAIRRCPT:
				case BENEFIT:
				case DWI:
					pspcViewDynSpecButton.setVisible(false);
					pspcDynamicFileSpecificationLabel.setText("N/A");
					break;
				default:
					pspcViewDynSpecButton.setVisible(true);
				break;
			}
		}
		
		if(pSpecification.getConverter() != null && pSpecification.getConverter().isCalculator() == true)
			pspcViewCalcButton.setVisible(true);
		else
			pspcViewCalcButton.setVisible(false);
		
		//load the UploadTypes
		loadUploadTypeData();
	}
	
	private void updatePipelineSpecification()
	{
		DataManager.i().mPipelineSpecification.setName(pspcNameLabel.getText());
		DataManager.i().mPipelineSpecification.setDescription(pspcNameLabel.getText());
		DataManager.i().mPipelineSpecification.setPostValidationApproval(pspcPostValidationApprovalCheck.isSelected());
		
		// initializer
		if(pspcInitializerCombo.getSelectionModel().getSelectedIndex() > -1 && pspcInitializerCombo.getSelectionModel().getSelectedItem() != "")
		{
			String initializerDescription = pspcInitializerCombo.getSelectionModel().getSelectedItem();
			for (PipelineFileStepHandler initializer: initializers) {
				if (initializer.getName().equals(initializerDescription)) {
					DataManager.i().mPipelineSpecification.setInitializerId(initializer.getId());
					break;
				}
			}
		} else { DataManager.i().mPipelineSpecification.setInitializerId(null); }
		
		// parser
		if(pspcParserCombo.getSelectionModel().getSelectedIndex() > -1) {
			String parserDescription = pspcParserCombo.getSelectionModel().getSelectedItem();
			for (PipelineFileStepHandler parser: parsers) {
				if (parser.getName().equals(parserDescription)) {
					DataManager.i().mPipelineSpecification.setParserId(parser.getId());
					break;
				}
			}
		}
		
		// validator
		if(pspcValidatorCombo.getSelectionModel().getSelectedIndex() > -1 && pspcValidatorCombo.getSelectionModel().getSelectedItem() != "")
		{
			String validatorDescription = pspcValidatorCombo.getSelectionModel().getSelectedItem();
			for (PipelineFileStepHandler validator: validators) {
				if (validator.getName().equals(validatorDescription)) {
					DataManager.i().mPipelineSpecification.setValidatorId(validator.getId());
					break;
				}
			}
		} else { DataManager.i().mPipelineSpecification.setValidatorId(null); }
		
		// converter
		if(pspcConverterCombo.getSelectionModel().getSelectedIndex() > -1) {
			String converterDescription = pspcConverterCombo.getSelectionModel().getSelectedItem();
			for (PipelineFileStepHandler converter: converters) {
				if (converter.getName().equals(converterDescription)) {
					DataManager.i().mPipelineSpecification.setConverterId(converter.getId());
					break;
				}
			}
		}
		
		// finalizer
		if(pspcFinalizerCombo.getSelectionModel().getSelectedIndex() > -1 && pspcFinalizerCombo.getSelectionModel().getSelectedItem() != "")
		{
			String finalizerDescription = pspcFinalizerCombo.getSelectionModel().getSelectedItem();
			for (PipelineFileStepHandler finalizer: finalizers) {
				if (finalizer.getName().equals(finalizerDescription)) {
					DataManager.i().mPipelineSpecification.setFinalizerId(finalizer.getId());
					break;
				}
			}
		} else { DataManager.i().mPipelineSpecification.setFinalizerId(null); }
			
		// set any defaults
		DataManager.i().mPipelineSpecification.setChannelId(DataManager.i().mPipelineChannel.getId());
		if(pspcDynamicFileCalculatorLabel.getText().isEmpty() == false)
			DataManager.i().mPipelineSpecification.setDynamicFileCalculatorId(Long.valueOf(pspcDynamicFileCalculatorLabel.getText())); //DataManager.i().mPipelineSpecification.getDynamicFileCalculatorId());
		else
			DataManager.i().mPipelineSpecification.setDynamicFileCalculatorId(0l);
		if(pspcDynamicFileSpecificationLabel.getText().isEmpty() == false)
			DataManager.i().mPipelineSpecification.setDynamicFileSpecificationId(Long.valueOf(pspcDynamicFileSpecificationLabel.getText())); // DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
		else
			DataManager.i().mPipelineSpecification.setDynamicFileSpecificationId(0l);

		PipelineSpecificationRequest request = new PipelineSpecificationRequest();
		request.setEntity(DataManager.i().mPipelineSpecification);
		request.setId(DataManager.i().mPipelineSpecification.getId()); 
		
		if(pspcDynamicFileCalculatorLabel.getText() == null || pspcDynamicFileCalculatorLabel.getText().length() == 0) {
			DataManager.i().mPipelineSpecification.setDynamicFileCalculatorId(null);
			request.setClearDynamicFileCalculatorId(true);
		} else
			DataManager.i().mPipelineSpecification.setDynamicFileCalculatorId(Long.valueOf(pspcDynamicFileCalculatorLabel.getText()));
	
		// sub requests
		if(DataManager.i().mPipelineSpecification.getInitializerId() != null) 
		{
			PipelineFileStepHandlerRequest initRequest = new PipelineFileStepHandlerRequest();
			initRequest.setId(DataManager.i().mPipelineSpecification.getInitializerId());
			request.setInitializerRequest(initRequest);
		} else { request.setClearInitializerId(true); }
		if(DataManager.i().mPipelineSpecification.getParserId() != null) 
		{
			PipelineFileStepHandlerRequest prsrRequest = new PipelineFileStepHandlerRequest();
			prsrRequest.setId(DataManager.i().mPipelineSpecification.getParserId());
			request.setParserRequest(prsrRequest);
		}
		if(DataManager.i().mPipelineSpecification.getValidatorId() != null)
		{
			PipelineFileStepHandlerRequest valdRequest = new PipelineFileStepHandlerRequest();
			valdRequest.setId(DataManager.i().mPipelineSpecification.getValidatorId());
			request.setValidatorRequest(valdRequest);
		} else { request.setClearValidatorId(true); }
		if(DataManager.i().mPipelineSpecification.getConverterId() != null)
		{
			PipelineFileStepHandlerRequest cvtrRequest = new PipelineFileStepHandlerRequest();
			cvtrRequest.setId(DataManager.i().mPipelineSpecification.getConverterId());
			request.setConverterRequest(cvtrRequest);
		}
		if(DataManager.i().mPipelineSpecification.getFinalizerId() != null)
		{
			PipelineFileStepHandlerRequest fnlRequest = new PipelineFileStepHandlerRequest();
			fnlRequest.setId(DataManager.i().mPipelineSpecification.getFinalizerId());
			request.setFinalizerRequest(fnlRequest);
		} else { request.setClearFinalizerId(true); }
		
		try {
			DataManager.i().mPipelineSpecification = AdminPersistenceManager.getInstance().addOrUpdate(request);
		} catch (CoreException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void loadUploadTypeData() 
	{
		EtcAdmin.i().setStatusMessage("loading Uploadtype Data...");
		EtcAdmin.i().setProgress(.5);
		
		Task<Void> task = new Task<Void>()
		{
            @Override
            protected Void call() throws Exception
            {
            	UploadTypeRequest request = new UploadTypeRequest();
            	request.setSpecificationId(DataManager.i().mPipelineSpecification.getId());
            	request.setFetchInactive(true);
        		uploadTypes = AdminPersistenceManager.getInstance().getAll(request);
        		EtcAdmin.i().setProgress(.75);
                return null;
            }
        };
        
    	task.setOnSucceeded(e ->   loadUploadTypes());
    	task.setOnFailed(e ->   loadUploadTypes());
        new Thread(task).start();
	}
	
	//update the employers list
	private void loadUploadTypes() 
	{
		// clear anything already in the table
		pspcUploadTypeTableView.getItems().clear();
		
		//if we have no uploadTypes, bail
		if(uploadTypes == null) 
		{
			//reset our status
			EtcAdmin.i().setStatusMessage("Ready");
			EtcAdmin.i().setProgress(0);			
			return;
		}
		
		String name;
		for(UploadType uploadType : uploadTypes)
		{
			name = "";
			if(uploadType.getEmployerId() != null)
			{
				// check for inactives
				if ((uploadType.isActive() == false || uploadType.isDeleted() == true) && pspcUploadTypeShowInactive.isSelected() == false)
					continue;
				
				
				Employer employer;
				employer = uploadType.getEmployer();
				
				if(employer != null && employer.getName() != null)
					name = employer.getName();
				
				// apply filter, if present
				if(pspcUploadTypeFilter.getText().contentEquals("") == false) 
				{
					String searchData =  uploadType.getId().toString();
					searchData += " ";
					searchData += name;
					searchData += " ";
					searchData += uploadType.getName();
					if(searchData.toLowerCase().contains(pspcUploadTypeFilter.getText().toLowerCase()) == false) continue;
				}

				HBoxCell cell = new HBoxCell(uploadType.getId().toString(), uploadType.getName(), name, "", false, uploadType);
				pspcUploadTypeTableView.getItems().add(cell);
				
			} else {
				
				if(uploadType.getAccountId() != null)
				{
					Account account = uploadType.getAccount();
					if(account != null && account.getName() != null)
						name = account.getName();
					
					HBoxCell cell = new HBoxCell(uploadType.getId().toString(), uploadType.getName(), "", name, false, uploadType);
					pspcUploadTypeTableView.getItems().add(cell);
					
				} else {
					
					HBoxCell cell = new HBoxCell(uploadType.getId().toString(), uploadType.getName(), "", "", false, uploadType);
					pspcUploadTypeTableView.getItems().add(cell);
				}
			}
			DataManager.i().mUploadType = uploadType;
		}

		// set sort
		pspcUploadTypeTableView.getSortOrder().clear();
		pspcUploadTypeTableView.getSortOrder().add(sortColumn);
		sortColumn.setSortable(true);

        //update our uploadTypes screen label
        pspcUploadTitleLabel.setText("UploadTypes (total: " + String.valueOf(uploadTypes.size()) + ")" );

		//reset our status
		EtcAdmin.i().setStatusMessage("Ready");
		EtcAdmin.i().setProgress(0);
	}

	private void loadTemplateList() 
	{
		pspcTemplatesListView.getItems().clear();

		try {
			UploadTypeTemplateRequest request = new UploadTypeTemplateRequest();
			DataManager.i().mUploadTypeTemplates = AdminPersistenceManager.getInstance().getAll(request);
		} catch (CoreException e) { e.printStackTrace(); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		if(DataManager.i().mUploadTypeTemplates == null) return;

		List<TemplateCell> list = new ArrayList<>();
		for(UploadTypeTemplate template : DataManager.i().mUploadTypeTemplates)
		{
			list.add(new TemplateCell(template));
		}

        ObservableList<TemplateCell> newObservableList = FXCollections.observableList(list);
        pspcTemplatesListView.setItems(newObservableList);
	}

	private void loadCurrentUploadTypeData()
	{
		// clear the screen
		clearUploadTypeData();

		if(currentUploadType == null)
		{
			pspcEditButton.setDisable(true);
			return;
		}

		// set inactive/deleted warning
		if (currentUploadType.isDeleted()) {
			pspcUploadTypeInactiveLabel.setText("DELETED");
			pspcUploadTypeInactiveLabel.setVisible(true);
		} else {
			if (currentUploadType.isActive() == false) {
				pspcUploadTypeInactiveLabel.setText("INACTIVE");
				pspcUploadTypeInactiveLabel.setVisible(true);
			} else {
				pspcUploadTypeInactiveLabel.setVisible(false);
			}
		}		

		Utils.updateControl(pspcUploadName, currentUploadType.getName());
		Utils.updateControl(pspcUploadDescription, currentUploadType.getDescription());
		Utils.updateControl(pspsUploadEncryptedCheck, currentUploadType.isEncrypted());
		Utils.updateControl(pspsUploadEnabledCheck, currentUploadType.isEnabled());

		if(currentUploadType.getAccount() != null)
		{
			Utils.updateControl(pspcUploadAccountId, currentUploadType.getAccountId());
//				currentUploadType.setAccount(DataManager.i().getAccount(currentUploadType.getAccountId().toString()));
			Utils.updateControl(pspcUploadAccount, currentUploadType.getAccount().getName());
		}
		if(currentUploadType.getEmployer() != null)
		{
			Utils.updateControl(pspcUploadEmployerId, currentUploadType.getEmployerId());
//				currentUploadType.setEmployer(DataManager.i().getEmployer(currentUploadType.getEmployerId()));
			Utils.updateControl(pspcUploadEmployer, currentUploadType.getEmployer().getName());
		}
		if(currentUploadType.getType() != null) 
		{
			Utils.updateControl(pspcUploadDocTypeId, currentUploadType.getTypeId());
//				DocumentTypeRequest dtRequest = new DocumentTypeRequest();
//				dtRequest.setId(currentUploadType.getTypeId());
//				currentUploadType.setType(DataManager.i().get(dtRequest));
			Utils.updateControl(pspcUploadDocType, currentUploadType.getType().getName());
		}
		if(currentUploadType.getHandler() != null)
		{
			Utils.updateControl(pspcUploadHandlerId, currentUploadType.getHandlerId());
//				UploadHandlerRequest uhRequest = new UploadHandlerRequest();
//				uhRequest.setId(currentUploadType.getHandlerId());
//				currentUploadType.setHandler(DataManager.i().get(uhRequest));
			Utils.updateControl(pspcUploadHandler, currentUploadType.getHandler().getName());
		}
	}
	
	private void updateUploadType() 
	{
		try {

			if(currentUploadType == null) return;
			
			// gather the data
			currentUploadType.setName(pspcUploadName.getText());
			currentUploadType.setDescription(pspcUploadDescription.getText());
			currentUploadType.setEncrypted(pspsUploadEncryptedCheck.isSelected());
			currentUploadType.setEnabled(pspsUploadEnabledCheck.isSelected());
	
			if(pspcUploadAccountId.getText().isEmpty() == false)
			{
				AccountRequest acctRequest = new AccountRequest();
				acctRequest.setId(Long.valueOf(pspcUploadAccountId.getText()));
				currentUploadType.setAccountId(Long.valueOf(pspcUploadAccountId.getText()));
				currentUploadType.setAccount(AdminPersistenceManager.getInstance().get(acctRequest));
				
			} else {
				currentUploadType.setAccountId(null);
			}
			if(pspcUploadEmployerId.getText().isEmpty() == false)
			{
				EmployerRequest emprRequest = new EmployerRequest();
				emprRequest.setId(Long.valueOf(pspcUploadEmployerId.getText()));
				currentUploadType.setEmployerId(Long.valueOf(pspcUploadEmployerId.getText()));
				currentUploadType.setEmployer(AdminPersistenceManager.getInstance().get(emprRequest));
			} else {
				currentUploadType.setEmployerId(null);
			}
			currentUploadType.setTypeId(Long.valueOf(pspcUploadDocTypeId.getText()));
			currentUploadType.setHandlerId(Long.valueOf(pspcUploadHandlerId.getText()));

			// set the specId that we are on
			currentUploadType.setSpecificationId(DataManager.i().mPipelineSpecification.getId());

		} catch (CoreException e) { e.printStackTrace(); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

	}

	private void loadPickList(int pickListType) 
	{
        try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/specification/ViewSpecificationPickList.fxml"));
			Parent ControllerNode = loader.load();
	        ViewSpecificationPickListController pickListController = (ViewSpecificationPickListController) loader.getController();

	        pickListController.setPickListType(pickListType);
	        pickListController.setSpecController(this);
	        pickListController.load();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        stage.onCloseRequestProperty().setValue(e -> refreshUploadTypeDisplay());
	        stage.onHiddenProperty().setValue(e -> refreshUploadTypeDisplay());
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}        
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void refreshUploadTypeDisplay() 
	{
		
	}
	
	private boolean validateData()
	{
		boolean bReturn = true;

		//check for nulls
		if(!Utils.validate(pspcUploadName)) bReturn = false;
		if(!Utils.validate(pspcUploadDocTypeId)) bReturn = false;
		if(!Utils.validate(pspcUploadDocType)) bReturn = false;
		if(!Utils.validate(pspcUploadHandlerId)) bReturn = false;
		if(!Utils.validate(pspcUploadHandler)) bReturn = false;

		return bReturn;
	}	

	@FXML
	private void onShowSystemInfo() {
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mPipelineSpecification;
			DataManager.i().mCurrentCoreDataType = SystemDataType.SPECIFICATION;
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
	        if (sysInfoController.changesMade == true)
	        	showPipelineSpecification();
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
	}
	
	@FXML
	private void onShowUploadTypeSystemInfo() {
		if(currentUploadType == null) return;
		
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) currentUploadType;
			DataManager.i().mCurrentCoreDataType = SystemDataType.UPLOADTYPE;
			DataManager.i().mUploadType = currentUploadType;
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
	        if (sysInfoController.changesMade == true) {
		        currentUploadType = DataManager.i().mUploadType;
		        loadCurrentUploadTypeData();
		        loadUploadTypeData();
	        }
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	@FXML
	private void onUploadTypeFilterKeyRelease() 
	{
		loadUploadTypes();
	}	
	
	@FXML
	private void onEdit(ActionEvent event) 
	{
		if(pspcEditButton.getText().equals("Edit"))
			setSpecEditMode(true);
		else
			setSpecEditMode(false);
	}	
	
	@FXML
	private void onSave(ActionEvent event)
	{
		// save it to the server
		updatePipelineSpecification();
		// reset the edit mode 
		setSpecEditMode(false);
		// and refresh the screen
		showPipelineSpecification();
	}	
	
	@FXML
	private void onViewSpecification(ActionEvent event) 
	{
		switch (DataManager.i().mPipelineChannel.getType())
		{
			case COVERAGE:
				EtcAdmin.i().setScreen(ScreenType.MAPPERCOVERAGE, true);
				break;
			case DEDUCTION:
				EtcAdmin.i().setScreen(ScreenType.MAPPERDEDUCTION, true);
				break;
			case EMPLOYEE:
				EtcAdmin.i().setScreen(ScreenType.MAPPEREMPLOYEE, true);
				break;
			case INSURANCE:
				EtcAdmin.i().setScreen(ScreenType.MAPPERINSURANCE, true);
				break;
			case IRS1095C:
				EtcAdmin.i().setScreen(ScreenType.MAPPERIRS1095C, true);
				break;
			case PAY:
				EtcAdmin.i().setScreen(ScreenType.MAPPERPAY, true);
				break;
			default:
				EtcAdmin.i().setScreen(ScreenType.PIPELINEDYNAMICFILESPECIFICATION, true);
				break;
		}
	}	

	@FXML
	private void onViewCalculator(ActionEvent event) 
	{
		loadCalculator();
	}	
	
	private void loadCalculator()
	{
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/specification/ViewPipelineSpecificationCalculation.fxml"));
			Parent ControllerNode = loader.load();
	        ViewPipelineSpecificationCalculationController pickListController = (ViewPipelineSpecificationCalculationController) loader.getController();

	        pickListController.setSpecController(this);
	        pickListController.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}        
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	// UPLOADTYPE FUNCTIONS
	@FXML
	private void onUploadTypeInactive(ActionEvent event)
	{
		loadUploadTypes();
	}
	
	@FXML
	private void onUploadTypeFilterClear(ActionEvent event)
	{
		pspcUploadTypeFilter.setText("");
		loadUploadTypes();
	}
	
	@FXML
	private void onUploadTypeEditCancel(ActionEvent event)
	{
		// check to see if we are doing a save or an edit
		if(uploadTypeMode == EditMode.VIEW)
		{
			if(currentUploadType == null) return;
			setUploadTypeControls(EditMode.EDIT);
		} else {
			//reload the details from the current UploadType
			loadCurrentUploadTypeData();
			//and set the controls to view
			setUploadTypeControls(EditMode.VIEW);
		}
	}	

	@FXML
	private void onUploadTypeSave(ActionEvent event) 
	{
		// no saves for view mode
		if(uploadTypeMode == EditMode.VIEW) return;
		// validate the info
		if(validateData() == false) return;
		
		//add, we want a new uploadType
		if(uploadTypeMode == EditMode.ADD)
			currentUploadType = new UploadType();

		acctDelete.setVisible(false);
		emprDelete.setVisible(false);
		docTpDelete.setVisible(false);
		hndlrDelete.setVisible(false);

		// update from screen
		updateUploadType();

		UploadTypeRequest request = new UploadTypeRequest();

		//create the request and update
		request.setEntity(currentUploadType);

		if(currentUploadType.getAccountId() == null)
		{
			request.setClearAccountId(true);
		}
		if(currentUploadType.getEmployerId() == null)
		{
			request.setClearEmployerId(true);
		}
		request.setId(currentUploadType.getId());
		
		try {
			currentUploadType = AdminPersistenceManager.getInstance().addOrUpdate(request);
		} catch (CoreException e) { DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		// reload
		loadUploadTypeData();
		// set the controls to view
		setUploadTypeControls(EditMode.VIEW);
		// and reload the details from the current UploadType
		loadCurrentUploadTypeData();

		// finally, highlight our row
		if(uploadTypeMode == EditMode.EDIT)
			pspcUploadTypeTableView.getSelectionModel().select(selectedUploadTypeIndex);
		else {
			int position = pspcUploadTypeTableView.getItems().size() - 1;
			pspcUploadTypeTableView.getSelectionModel().select(position);
		}

		pspcUploadTypeTableView.requestFocus();		
	}
	
	@FXML
	private void onUploadSelection(ActionEvent event) 
	{
		setUploadTypeControls(EditMode.ADD);
	}	
	
	@FXML
	private void onUploadTypeAdd(ActionEvent event)
	{
		currentUploadType = null;
		clearUploadTypeData();
		setUploadTypeControls(EditMode.ADD);
	}	
	
	@FXML
	private void onUploadTypeSetAccount(ActionEvent event) 
	{
		if (pspcUploadEmployer.getText().isEmpty() == false) {
			Utils.showAlert("Employer Set", "Please clear Employer before setting Account");
			return;
		}
		loadPickList(1);
		if(pspcUploadAccount.getText().isEmpty() == false) { 
			//pspcUploadEmployerSetButton.setDisable(true); 
			acctDelete.setVisible(true);
		}
	}	
	
	@FXML
	private void onUploadTypeSetEmployer(ActionEvent event) 
	{
		if (pspcUploadAccount.getText().isEmpty() == false) {
			Utils.showAlert("Account Set", "Please clear Account before setting Employer");
			return;
		}
		loadPickList(2);
		if(pspcUploadEmployer.getText().isEmpty() == false) { 
			//pspcUploadAccountSetButton.setDisable(true); 
			emprDelete.setVisible(true);
		}
	}	
	
	@FXML
	private void onUploadTypeSetDocType(ActionEvent event)
	{
		loadPickList(3);
		docTpDelete.setVisible(true);
	}	
	
	@FXML
	private void onUploadTypeSetHandler(ActionEvent event) 
	{
		loadPickList(4);
		hndlrDelete.setVisible(true);
	}	
	
	@FXML
	private void onAcctDelete(ActionEvent event) 
	{
		pspcUploadAccountId.setText("");
		pspcUploadAccount.setText("");
		acctDelete.setVisible(false);
		//pspcUploadEmployerSetButton.setDisable(false);
	}	
	
	@FXML
	private void onEmprDelete(ActionEvent event) 
	{
		pspcUploadEmployerId.setText("");
		pspcUploadEmployer.setText("");
		emprDelete.setVisible(false);
		//pspcUploadAccountSetButton.setDisable(false);
	}	
	
	@FXML
	private void onDocTpDelete(ActionEvent event) 
	{
		pspcUploadDocTypeId.setText("");
		pspcUploadDocType.setText("");
		docTpDelete.setVisible(false);
	}	
	
	@FXML
	private void onHndlrDelete(ActionEvent event) 
	{
		pspcUploadHandlerId.setText("");
		pspcUploadHandler.setText("");
		hndlrDelete.setVisible(false);
	}	

	@FXML
	private void onUploadTypeTemplates(ActionEvent event) 
	{
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/specification/ViewUploadTypeTemplate.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        loadTemplateList();
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}      
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	
	
	//extending the listview for our additional controls
	public class HBoxCell extends HBox 
	{
		SimpleStringProperty utId = new SimpleStringProperty();
		SimpleStringProperty name = new SimpleStringProperty();
		SimpleStringProperty employer = new SimpleStringProperty();
		SimpleStringProperty account = new SimpleStringProperty();
		UploadType uploadType;
		
        public String getUtId() 
        {
       	 	return utId.get();
        }

        public String getName() 
        {
            return name.get();
        }

        public String getEmployer() 
        {
          	return employer.get();
        }

        public String getAccount() 
        {
          	return account.get();
        }
       
        public UploadType getUploadType() 
        {
    	    return uploadType;
        }

         HBoxCell(String id, String name, String employer, String account, boolean headerRow, UploadType uploadType)
         {
              super();

              this.uploadType = uploadType;
              //clean up any nulls
              if(id == null) id = "";
              if(name == null) 
            	  name = "";
              else
            	  name = name.replace("null", "");            	  
              if(employer == null) employer = "";
              
              this.utId.set(id);
              this.name.set(name);
              this.employer.set(employer);
              this.account.set(account);
         }
    }
	
	public class TemplateCell extends HBox 
	{
		Label id = new Label();
		Label name = new Label();
		UploadTypeTemplate template = null;

         public UploadTypeTemplate getTemplate()
         {
        	 return template;
         }
         
         TemplateCell(UploadTypeTemplate template) 
         {
              super();
              
              if(template != null) 
              {
	              this.template = template;
	              
	              name.setText(template.getName());
	              id.setText(String.valueOf(template.getId()));
	              
	              //set the label widths
	              id.setMinWidth(50);
	              id.setMaxWidth(50);
	              id.setPrefWidth(50);
	              name.setMinWidth(150);
	              name.setMaxWidth(150);
	              name.setPrefWidth(150);
	
	              this.getChildren().addAll(id, name);
              }
         }
    }	
	
	private enum EditMode
	{
		VIEW,
		EDIT,
		ADD
	}
}


