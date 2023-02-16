package com.etc.admin.ui.pipeline.payperiodfile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.PipelineChannel;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileNotice;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileRecordRejection;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileStepHandler;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.EmployeeFile;
import com.etc.corvetto.entities.Document;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ViewPipelinePayPeriodFileController {
	
	@FXML
	private Button paypdEditButton;
	@FXML
	private TableView<ColumnField> paypdfileDynFileColumnFieldTable;
	@FXML
	private TextField paypdfileDynFileDescriptionLabel;
	@FXML
	private TextField paypdfileDynFileTabIndexLabel;
	@FXML
	private TextField paypdfileDynFileHeaderRowIndexLabel;
	@FXML
	private CheckBox paypdfileDynFileCreateEmployeeCheckBox;
	@FXML 
	private VBox paypdfilePipelineSpecificationsVBox; 
	@FXML
	private VBox paypdfileDynamicFileSpecificationsVBox; 
	@FXML
	private VBox paypdfileDocumentVBox; 
	@FXML
	private Button paypdfileShowPipelineSpecsButton;
	@FXML
	private Button paypdfileShowDynamicFileSpecsButton;
	@FXML
	private Button paypdfileShowDocumentButton;
	@FXML
	private ComboBox<String> paypdfileSearchComboBox;
	@FXML
	private TextField paypdfileNameLabel;
	@FXML
	private TextField paypdfileEmployeesCreatedLabel;
	@FXML
	private TextField paypdfileEmployeesUpdatedLabel;
	@FXML
	private TextField paypdfileSecondariesCreatedLabel;
	@FXML
	private TextField paypdfileSecondariesUpdatedLabel;
	@FXML
	private TextField paypdfileemployeeCoveragePeriodsCreated;
	@FXML
	private TextField paypdfileemployeeCoveragePeriodsUpdated;
	@FXML
	private TextField paypdfilesecondaryCoveragePeriodsCreated;
	@FXML
	private TextField paypdfilesecondaryCoveragePeriodsUpdated;

	@FXML
	private TextField paypdfileNoChangeCountLabel;
	@FXML
	private TextField paypdfilePipelineNameLabel;
	@FXML
	private TextField paypdfilePipelineDescriptionLabel;
	@FXML
	private TextField paypdfileChannelNameLabel;
	@FXML
	private TextField paypdfileChannelDescriptionLabel;
	@FXML
	private TextField paypdfileParserDescriptionLabel;
	@FXML
	private TextField paypdfileParserPipelineStepTypeLabel;
	@FXML
	private TextField paypdfileParserInterfaceClassLabel;
	@FXML
	private TextField paypdfileParserHandlerClassLabel;
	@FXML
	private TextField paypdfileValidatorDescriptionLabel;
	@FXML
	private TextField paypdfileValidatorPipelineStepTypeLabel;
	@FXML
	private TextField paypdfileValidatorInterfaceClassLabel;
	@FXML
	private TextField paypdfileValidatorHandlerClassLabel;
	@FXML
	private TextField paypdfileConverterDescriptionLabel;
	@FXML
	private TextField paypdfileConverterPipelineStepTypeLabel;
	@FXML
	private TextField paypdfileConverterInterfaceClassLabel;
	@FXML
	private TextField paypdfileConverterHandlerClassLabel;
	@FXML
	private TextField paypdfileFinalizerDescriptionLabel;
	@FXML
	private TextField paypdfileFinalizerPipelineStepTypeLabel;
	@FXML
	private TextField paypdfileFinalizerInterfaceClassLabel;
	@FXML
	private TextField paypdfileFinalizerHandlerClassLabel;
	@FXML
	private TextField paypdfileInitializerDescriptionLabel;
	@FXML
	private TextField paypdfileInitializerPipelineStepTypeLabel;
	@FXML
	private TextField paypdfileInitializerInterfaceClassLabel;
	@FXML
	private TextField paypdfileInitializerHandlerClassLabel;
	@FXML
	private Label paypdfileRawPayPeriodListLabel;
	@FXML
	private Label paypdfilePipelineNoticesListLabel;
	@FXML
	private Label paypdfileRecordRejectionsListLabel;
	@FXML
	private ListView<HBoxCell> paypdfilesRawPayPeriodFilesListView;
	@FXML
	private ListView<HBoxCell> paypdfilesPipelineNoticesListView;
	@FXML
	private ListView<HBoxCell> paypdfilesRecordRejectionsListView;
	@FXML
	private Label paypdfileCoreIdLabel;
	@FXML
	private Label paypdfileCoreActiveLabel;
	@FXML
	private Label paypdfileCoreBODateLabel;
	@FXML
	private Label paypdfileCoreLastUpdatedLabel;
	@FXML
	private Label paypdfileSpecificationCoreIdLabel;
	@FXML
	private Label paypdfileSpecificationCoreActiveLabel;
	@FXML
	private Label paypdfileSpecificationCoreBODateLabel;
	@FXML
	private Label paypdfileSpecificationCoreLastUpdatedLabel;
	@FXML
	private Label paypdfileChannelCoreIdLabel;
	@FXML
	private Label paypdfileChannelCoreActiveLabel;
	@FXML
	private Label paypdfileChannelCoreBODateLabel;
	@FXML
	private Label paypdfileChannelCoreLastUpdatedLabel;
	@FXML
	private Label paypdfileInitializerCoreIdLabel;
	@FXML
	private Label paypdfileInitializerCoreActiveLabel;
	@FXML
	private Label paypdfileInitializerCoreBODateLabel;
	@FXML
	private Label paypdfileInitializerCoreLastUpdatedLabel;
	@FXML
	private Label paypdfileParserCoreIdLabel;
	@FXML
	private Label paypdfileParserCoreActiveLabel;
	@FXML
	private Label paypdfileParserCoreBODateLabel;
	@FXML
	private Label paypdfileParserCoreLastUpdatedLabel;
	@FXML
	private Label paypdfileValidatorCoreIdLabel;
	@FXML
	private Label paypdfileValidatorCoreActiveLabel;
	@FXML
	private Label paypdfileValidatorCoreBODateLabel;
	@FXML
	private Label paypdfileValidatorCoreLastUpdatedLabel;
	@FXML
	private Label paypdfileConverterCoreIdLabel;
	@FXML
	private Label paypdfileConverterCoreActiveLabel;
	@FXML
	private Label paypdfileConverterCoreBODateLabel;
	@FXML
	private Label paypdfileConverterCoreLastUpdatedLabel;
	@FXML
	private Label paypdfileFinalizerCoreIdLabel;
	@FXML
	private Label paypdfileFinalizerCoreActiveLabel;
	@FXML
	private Label paypdfileFinalizerCoreBODateLabel;
	@FXML
	private Label paypdfileFinalizerCoreLastUpdatedLabel;
	@FXML
	private TextField paypdfileDocumentNameLabel;
	@FXML
	private TextField paypdfileDocumentSizeLabel;
	@FXML
	private TextField paypdfileDocumentDescriptionLabel;
	@FXML
	private TextField paypdfileDocumentFilenameLabel;
	@FXML
	private TextField paypdfileDocumentTypeLabel;
	@FXML
	private TextField paypdfileAccountLabel;
	@FXML
	private TextField paypdfileEmployerLabel;
	@FXML
	private Label paypdfileDocumentCoreIdLabel;
	@FXML
	private Label paypdfileDocumentCoreActiveLabel;
	@FXML
	private Label paypdfileDocumentCoreBODateLabel;
	@FXML
	private Label paypdfileDocumentCoreLastUpdatedLabel;

	
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
				
		clearScreen();
		loadSearchBox();

		//load the collections
		loadRawPayPeriodFiles();
		loadPipelineNotices();
		loadRecordRejections();
	}
	
	private void clearScreen() {
		//clear all the labels
		paypdfileDynFileDescriptionLabel.setText(" ");
		paypdfileDynFileTabIndexLabel.setText(" ");
		paypdfileDynFileHeaderRowIndexLabel.setText(" ");
		paypdfileDynFileCreateEmployeeCheckBox.setText(" ");
		paypdfileNameLabel.setText(" ");
		paypdfileEmployeesCreatedLabel.setText(" ");
		paypdfileEmployeesUpdatedLabel.setText(" ");
		paypdfileNoChangeCountLabel.setText(" ");
		paypdfilePipelineNameLabel.setText(" ");
		paypdfilePipelineDescriptionLabel.setText(" ");
		paypdfileChannelNameLabel.setText(" ");
		paypdfileChannelDescriptionLabel.setText(" ");
		paypdfileParserDescriptionLabel.setText(" ");
		paypdfileParserPipelineStepTypeLabel.setText(" ");
		paypdfileParserInterfaceClassLabel.setText(" ");
		paypdfileParserHandlerClassLabel.setText(" ");
		paypdfileValidatorDescriptionLabel.setText(" ");
		paypdfileValidatorPipelineStepTypeLabel.setText(" ");
		paypdfileValidatorInterfaceClassLabel.setText(" ");
		paypdfileValidatorHandlerClassLabel.setText(" ");
		paypdfileConverterDescriptionLabel.setText(" ");
		paypdfileConverterPipelineStepTypeLabel.setText(" ");
		paypdfileConverterInterfaceClassLabel.setText(" ");
		paypdfileConverterHandlerClassLabel.setText(" ");
		paypdfileFinalizerDescriptionLabel.setText(" ");
		paypdfileFinalizerPipelineStepTypeLabel.setText(" ");
		paypdfileFinalizerInterfaceClassLabel.setText(" ");
		paypdfileFinalizerHandlerClassLabel.setText(" ");
		paypdfileInitializerDescriptionLabel.setText(" ");
		paypdfileInitializerPipelineStepTypeLabel.setText(" ");
		paypdfileInitializerInterfaceClassLabel.setText(" ");
		paypdfileInitializerHandlerClassLabel.setText(" ");
		paypdfileCoreIdLabel.setText(" ");
		paypdfileCoreActiveLabel.setText(" ");
		paypdfileCoreBODateLabel.setText(" ");
		paypdfileCoreLastUpdatedLabel.setText(" ");
		paypdfileSpecificationCoreIdLabel.setText(" ");
		paypdfileSpecificationCoreActiveLabel.setText(" ");
		paypdfileSpecificationCoreBODateLabel.setText(" ");
		paypdfileSpecificationCoreLastUpdatedLabel.setText(" ");
		paypdfileChannelCoreIdLabel.setText(" ");
		paypdfileChannelCoreActiveLabel.setText(" ");
		paypdfileChannelCoreBODateLabel.setText(" ");
		paypdfileChannelCoreLastUpdatedLabel.setText(" ");
		paypdfileInitializerCoreIdLabel.setText(" ");
		paypdfileInitializerCoreActiveLabel.setText(" ");
		paypdfileInitializerCoreBODateLabel.setText(" ");
		paypdfileInitializerCoreLastUpdatedLabel.setText(" ");
		paypdfileParserCoreIdLabel.setText(" ");
		paypdfileParserCoreActiveLabel.setText(" ");
		paypdfileParserCoreBODateLabel.setText(" ");
		paypdfileParserCoreLastUpdatedLabel.setText(" ");
		paypdfileValidatorCoreIdLabel.setText(" ");
		paypdfileValidatorCoreActiveLabel.setText(" ");
		paypdfileValidatorCoreBODateLabel.setText(" ");
		paypdfileValidatorCoreLastUpdatedLabel.setText(" ");
		paypdfileConverterCoreIdLabel.setText(" ");
		paypdfileConverterCoreActiveLabel.setText(" ");
		paypdfileConverterCoreBODateLabel.setText(" ");
		paypdfileConverterCoreLastUpdatedLabel.setText(" ");
		paypdfileFinalizerCoreIdLabel.setText(" ");
		paypdfileFinalizerCoreActiveLabel.setText(" ");
		paypdfileFinalizerCoreBODateLabel.setText(" ");
		paypdfileFinalizerCoreLastUpdatedLabel.setText(" ");
		paypdfileDocumentNameLabel.setText(" ");
		paypdfileDocumentSizeLabel.setText(" ");
		paypdfileDocumentDescriptionLabel.setText(" ");
		paypdfileDocumentFilenameLabel.setText(" ");
		paypdfileDocumentTypeLabel.setText(" ");
		paypdfileAccountLabel.setText(" ");
		paypdfileEmployerLabel.setText(" ");
		paypdfileDocumentCoreIdLabel.setText(" ");
		paypdfileDocumentCoreActiveLabel.setText(" ");
		paypdfileDocumentCoreBODateLabel.setText(" ");
		paypdfileDocumentCoreLastUpdatedLabel.setText(" ");
		
		//no edit unless something is selected
		paypdEditButton.setDisable(true);
		
		loadBlankColumnFields();
	}
	
	private void loadSearchBox(){
		
	}

	private void initControls() {
		
		//mark the checkboxes read only
		paypdfileDynFileCreateEmployeeCheckBox.setDisable(true);

		
		//hide the sections
		paypdfileShowPipelineSpecsButton.setText("Show Pipeline Specifications");
		paypdfilePipelineSpecificationsVBox.setVisible(false);			
		paypdfilePipelineSpecificationsVBox.setMinHeight(1);
		paypdfilePipelineSpecificationsVBox.setPrefHeight(1);
		paypdfilePipelineSpecificationsVBox.setMaxHeight(1);

		paypdfileShowDynamicFileSpecsButton.setText("Show Dynamic File Specifications");
		paypdfileDynamicFileSpecificationsVBox.setVisible(false);			
		paypdfileDynamicFileSpecificationsVBox.setMinHeight(1);
		paypdfileDynamicFileSpecificationsVBox.setPrefHeight(1);
		paypdfileDynamicFileSpecificationsVBox.setMaxHeight(1);
		
		paypdfileShowDocumentButton.setText("Show Document");
		paypdfileDocumentVBox.setVisible(false);			
		paypdfileDocumentVBox.setMinHeight(1);
		paypdfileDocumentVBox.setPrefHeight(1);
		paypdfileDocumentVBox.setMaxHeight(1);
		
		//set up the table
		TableColumn<ColumnField,String> nameColumn = new TableColumn<>("Name");
 		nameColumn.setMinWidth(150);
		nameColumn.setCellValueFactory(new PropertyValueFactory<ColumnField,String>("name"));

		TableColumn<ColumnField,String> columnColumn = new TableColumn<ColumnField,String>("Column");
		columnColumn.setMinWidth(150);
		columnColumn.setCellValueFactory(new PropertyValueFactory<ColumnField,String>("column"));

		TableColumn<ColumnField,String> columnIndexColumn = new TableColumn<ColumnField,String>("Column Index");
		columnIndexColumn.setMinWidth(150);
		columnIndexColumn.setCellValueFactory(new PropertyValueFactory<ColumnField,String>("columnIndex"));

		TableColumn<ColumnField,String> parsePatternColumn = new TableColumn<ColumnField,String>("Parse Pattern");
		parsePatternColumn.setMinWidth(150);
		parsePatternColumn.setCellValueFactory(new PropertyValueFactory<ColumnField,String>("parsePattern"));
		
		paypdfileDynFileColumnFieldTable.getColumns().clear();
		paypdfileDynFileColumnFieldTable.getColumns().add(nameColumn); 
		paypdfileDynFileColumnFieldTable.getColumns().add(columnColumn);
		paypdfileDynFileColumnFieldTable.getColumns().add(columnIndexColumn);
		paypdfileDynFileColumnFieldTable.getColumns().add(parsePatternColumn);		
			
	}
	
	private void loadBlankColumnFields() {
		//add some data
		List<ColumnField> list = new ArrayList<ColumnField>();
		list.add(new ColumnField("First Name"," ", " ", " "));
		list.add(new ColumnField("Middle Name"," ", " ", " "));
		list.add(new ColumnField("Last Name"," ", " ", " "));
		list.add(new ColumnField("Social Security Number"," ", " ", " "));
		list.add(new ColumnField("Employee Identifier"," ", " ", " "));
		list.add(new ColumnField("Street Number"," ", " ", " "));
		list.add(new ColumnField("Street or Address"," ", " ", " "));
		list.add(new ColumnField("Address Line 2"," ", " ", " "));
		list.add(new ColumnField("City"," ", " ", " "));
		list.add(new ColumnField("State"," ", " ", " "));
		list.add(new ColumnField("Zip"," ", " ", " "));
		list.add(new ColumnField("Gender"," ", " ", " "));
		list.add(new ColumnField("Date of Birth"," ", " ", " "));
		list.add(new ColumnField("Hire Date"," ", " ", " "));
		list.add(new ColumnField("Rehire Date"," ", " ", " "));
		list.add(new ColumnField("Termination Date"," ", " ", " "));
		list.add(new ColumnField("Department"," ", " ", " "));
		list.add(new ColumnField("Job Title"," ", " ", " "));
		list.add(new ColumnField("Custom Field 1"," ", " ", " "));
		list.add(new ColumnField("Custom Field 2"," ", " ", " "));
		list.add(new ColumnField("Custom Field 3"," ", " ", " "));
		list.add(new ColumnField("Custom Field 4"," ", " ", " "));
		list.add(new ColumnField("Custom Field 5"," ", " ", " "));

        ObservableList<ColumnField> myObservableList = FXCollections.observableList(list);
        paypdfileDynFileColumnFieldTable.setItems(myObservableList);
	}

	private void updateEmployeeFileDocumentData(){
		
		Document document = DataManager.i().mPipelineEmployeeFile.getPipelineInformation().getDocument();
		if (document != null) {
			Utils.updateControl(paypdfileDocumentNameLabel, document.getName());
			Utils.updateControl(paypdfileDocumentSizeLabel, document.getName());
			Utils.updateControl(paypdfileDocumentDescriptionLabel, document.getName());
			Utils.updateControl(paypdfileDocumentFilenameLabel, document.getName());
		
			//Utils.updateControl(paypdfileDocumentValue0Label, document.getValue0());
			//Utils.updateControl(paypdfileDocumentValue1Label, document.getValue1());
			//Utils.updateControl(paypdfileDocumentValue2Label, document.getValue2());
			//Utils.updateControl(paypdfileDocumentValue3Label, document.getValue3());
			//Utils.updateControl(paypdfileDocumentValue4Label, document.getValue4());
			//Utils.updateControl(paypdfileDocumentValue5Label, String.valueOf(document.getValue5()));
			//Utils.updateControl(paypdfileDocumentValue6Label, String.valueOf(document.getValue6()));
			//Utils.updateControl(paypdfileDocumentValue7Label, String.valueOf(document.getValue7()));
			//Utils.updateControl(paypdfileDocumentValue8Label, String.valueOf(document.getValue8()));
			//Utils.updateControl(paypdfileDocumentValue9Label, String.valueOf(document.getValue9()));
			//Utils.updateControl(paypdfileDocumentSdxIdLabel, String.valueOf(document.getSdxId()));
			Utils.updateControl(paypdfileDocumentTypeLabel, document.getDocumentType().toString());
			
			Utils.updateControl(paypdfileDocumentCoreIdLabel,String.valueOf(document.getId()));
			Utils.updateControl(paypdfileDocumentCoreActiveLabel,String.valueOf(document.isActive()));
			Utils.updateControl(paypdfileDocumentCoreBODateLabel,document.getBornOn());
			Utils.updateControl(paypdfileDocumentCoreLastUpdatedLabel,document.getLastUpdated());
		}

	}

	private void updateEmployeeFileDynamicFileData() {
		
		//need to load the dynamic file specification
		DynamicEmployeeFileSpecification fSpec = DataManager.i().mDynamicEmployeeFileSpecification;
		
//		if (fSpec != null) {
			Utils.updateControl(paypdfileDynFileDescriptionLabel, fSpec.getName());
			Utils.updateControl(paypdfileDynFileTabIndexLabel, fSpec.getTabIndex());
			Utils.updateControl(paypdfileDynFileHeaderRowIndexLabel, fSpec.getHeaderRowIndex());
			Utils.updateControl(paypdfileDynFileCreateEmployeeCheckBox, fSpec.isCreateEmployee());
			
			//update the column field table
			List<ColumnField> list = new ArrayList<ColumnField>();
			list.add(new ColumnField("First Name", fSpec.isfNameCol(), fSpec.getfNameColIndex(), fSpec.getfNameParsePattern().getName()));
			list.add(new ColumnField("Middle Name", fSpec.ismNameCol(), fSpec.getmNameColIndex(), fSpec.getmNameParsePattern().getName()));
			list.add(new ColumnField("Last Name", fSpec.islNameCol(), fSpec.getlNameColIndex(), fSpec.getlNameParsePattern().getName()));
			list.add(new ColumnField("Social Security Number", fSpec.isSsnCol(), fSpec.getSsnColIndex(), fSpec.getSsnParsePattern().getName()));
			list.add(new ColumnField("Employee Identifier", fSpec.isErCol(), fSpec.getErColIndex(), fSpec.getErRefParsePattern().getName()));
			list.add(new ColumnField("Street Number", fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), fSpec.getStreetParsePattern().getName()));
			list.add(new ColumnField("Street or Address", fSpec.isStreetCol(), fSpec.getStreetColIndex(), fSpec.getStreetParsePattern().getName()));
			list.add(new ColumnField("Address Line 2", fSpec.isLin2Col(), fSpec.getLin2ColIndex(), ""));
			list.add(new ColumnField("City", fSpec.isCityCol(), fSpec.getCityColIndex(), fSpec.getCityParsePattern().getName()));
			list.add(new ColumnField("State", fSpec.isStateCol(), fSpec.getStateColIndex(), fSpec.getStateParsePattern().getName()));
			list.add(new ColumnField("Zip", fSpec.isZipCol(), fSpec.getZipColIndex(), fSpec.getZipParsePattern().getName()));
			list.add(new ColumnField("Gender", fSpec.isGenderCol(), fSpec.getGenderColIndex(), ""));
			list.add(new ColumnField("Date of Birth", fSpec.isDobCol(), fSpec.getDobColIndex(), ""));
			list.add(new ColumnField("Hire Date", fSpec.isHireDtCol(), fSpec.getHireDtColIndex(), ""));
			list.add(new ColumnField("Rehire Date", fSpec.isfNameCol(), fSpec.getSsnColIndex(), fSpec.getSsnParsePattern().getName()));
			list.add(new ColumnField("Termination Date", fSpec.isTermDtCol(), fSpec.getTermDtColIndex(), ""));
			list.add(new ColumnField("Department", fSpec.isDeptCol(), fSpec.getDeptColIndex(), ""));
			list.add(new ColumnField("Job Title", fSpec.isJobTtlCol(), fSpec.getJobTtlColIndex(), ""));
			list.add(new ColumnField("Custom Field 1", fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), fSpec.getCfld1ParsePattern().getName()));
			list.add(new ColumnField("Custom Field 2", fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), fSpec.getCfld2ParsePattern().getName()));
			list.add(new ColumnField("Custom Field 3", fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), ""));
			list.add(new ColumnField("Custom Field 4", fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), ""));
			list.add(new ColumnField("Custom Field 5", fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), ""));

	        ObservableList<ColumnField> myObservableList = FXCollections.observableList(list);
	        paypdfileDynFileColumnFieldTable.setItems(myObservableList);
//		}
	}
	
	private void UpdateEmployeeFilePipelineSpecifications(){

		PipelineSpecification pipelineSpecification = DataManager.i().mPipelineEmployeeFile.getSpecification();
		if (pipelineSpecification != null) {
			Utils.updateControl(paypdfilePipelineNameLabel,pipelineSpecification.getName());

			Utils.updateControl(paypdfileSpecificationCoreIdLabel,String.valueOf(pipelineSpecification.getId()));
			Utils.updateControl(paypdfileSpecificationCoreActiveLabel,String.valueOf(pipelineSpecification.isActive()));
			Utils.updateControl(paypdfileSpecificationCoreBODateLabel,pipelineSpecification.getBornOn());
			Utils.updateControl(paypdfileSpecificationCoreLastUpdatedLabel,pipelineSpecification.getLastUpdated());
									
			PipelineChannel pipelineChannel = pipelineSpecification.getChannel();
			if (pipelineChannel!= null){
				Utils.updateControl(paypdfileChannelNameLabel,pipelineChannel.getName());
				Utils.updateControl(paypdfileChannelCoreIdLabel,String.valueOf(pipelineChannel.getId()));
				Utils.updateControl(paypdfileChannelCoreActiveLabel,String.valueOf(pipelineChannel.isActive()));
				Utils.updateControl(paypdfileChannelCoreBODateLabel,pipelineChannel.getBornOn());
				Utils.updateControl(paypdfileChannelCoreLastUpdatedLabel,pipelineChannel.getLastUpdated());
			}
			
			PipelineFileStepHandler pipelineParser = pipelineSpecification.getParser();
			if (pipelineParser!= null){
				Utils.updateControl(paypdfileParserDescriptionLabel,pipelineParser.getName());
				Utils.updateControl(paypdfileParserPipelineStepTypeLabel,pipelineParser.getType().toString());
				Utils.updateControl(paypdfileParserInterfaceClassLabel,pipelineParser.getInterfaceClass());
				Utils.updateControl(paypdfileParserHandlerClassLabel,pipelineParser.getHandlerClass());
				
				Utils.updateControl(paypdfileParserCoreIdLabel,String.valueOf(pipelineParser.getId()));
				Utils.updateControl(paypdfileParserCoreActiveLabel,String.valueOf(pipelineParser.isActive()));
				Utils.updateControl(paypdfileParserCoreBODateLabel,pipelineParser.getBornOn());
				Utils.updateControl(paypdfileParserCoreLastUpdatedLabel,pipelineParser.getLastUpdated());
			}
			
			PipelineFileStepHandler pipelineValidator = pipelineSpecification.getValidator();
			if (pipelineValidator!= null){
				Utils.updateControl(paypdfileValidatorDescriptionLabel,pipelineValidator.getName());
				Utils.updateControl(paypdfileValidatorPipelineStepTypeLabel,pipelineValidator.getType().toString());
				Utils.updateControl(paypdfileValidatorInterfaceClassLabel,pipelineValidator.getInterfaceClass());
				Utils.updateControl(paypdfileValidatorHandlerClassLabel,pipelineValidator.getHandlerClass());
				
				Utils.updateControl(paypdfileValidatorCoreIdLabel,String.valueOf(pipelineValidator.getId()));
				Utils.updateControl(paypdfileValidatorCoreActiveLabel,String.valueOf(pipelineValidator.isActive()));
				Utils.updateControl(paypdfileValidatorCoreBODateLabel,pipelineValidator.getBornOn());
				Utils.updateControl(paypdfileValidatorCoreLastUpdatedLabel,pipelineValidator.getLastUpdated());
			}
			
			PipelineFileStepHandler pipelineConverter = pipelineSpecification.getConverter();
			if (pipelineValidator!= null){
				Utils.updateControl(paypdfileConverterDescriptionLabel,pipelineConverter.getName());
				Utils.updateControl(paypdfileConverterPipelineStepTypeLabel,pipelineConverter.getType().toString());
				Utils.updateControl(paypdfileConverterInterfaceClassLabel,pipelineConverter.getInterfaceClass());
				Utils.updateControl(paypdfileConverterHandlerClassLabel,pipelineConverter.getHandlerClass());
				
				Utils.updateControl(paypdfileConverterCoreIdLabel,String.valueOf(pipelineConverter.getId()));
				Utils.updateControl(paypdfileConverterCoreActiveLabel,String.valueOf(pipelineConverter.isActive()));
				Utils.updateControl(paypdfileConverterCoreBODateLabel,pipelineConverter.getBornOn());
				Utils.updateControl(paypdfileConverterCoreLastUpdatedLabel,pipelineConverter.getLastUpdated());
			}
			
			PipelineFileStepHandler pipelineFinalizer = pipelineSpecification.getFinalizer();
			if (pipelineFinalizer!= null){
				Utils.updateControl(paypdfileFinalizerDescriptionLabel,pipelineFinalizer.getName());
				Utils.updateControl(paypdfileFinalizerPipelineStepTypeLabel,pipelineFinalizer.getType().toString());
				Utils.updateControl(paypdfileFinalizerInterfaceClassLabel,pipelineFinalizer.getInterfaceClass());
				Utils.updateControl(paypdfileFinalizerHandlerClassLabel,pipelineFinalizer.getHandlerClass());
				
				Utils.updateControl(paypdfileFinalizerCoreIdLabel,String.valueOf(pipelineFinalizer.getId()));
				Utils.updateControl(paypdfileFinalizerCoreActiveLabel,String.valueOf(pipelineFinalizer.isActive()));
				Utils.updateControl(paypdfileFinalizerCoreBODateLabel,pipelineFinalizer.getBornOn());
				Utils.updateControl(paypdfileFinalizerCoreLastUpdatedLabel,pipelineFinalizer.getLastUpdated());
			}
			
			PipelineFileStepHandler pipelineInitializer = pipelineSpecification.getInitializer();
			if (pipelineFinalizer!= null){
				Utils.updateControl(paypdfileInitializerDescriptionLabel,pipelineInitializer.getName());
				Utils.updateControl(paypdfileInitializerPipelineStepTypeLabel,pipelineInitializer.getType().toString());
				Utils.updateControl(paypdfileInitializerInterfaceClassLabel,pipelineInitializer.getInterfaceClass());
				Utils.updateControl(paypdfileInitializerHandlerClassLabel,pipelineInitializer.getHandlerClass());
				
				Utils.updateControl(paypdfileInitializerCoreIdLabel,String.valueOf(pipelineInitializer.getId()));
				Utils.updateControl(paypdfileInitializerCoreActiveLabel,String.valueOf(pipelineInitializer.isActive()));
				Utils.updateControl(paypdfileInitializerCoreBODateLabel,pipelineInitializer.getBornOn());
				Utils.updateControl(paypdfileInitializerCoreLastUpdatedLabel,pipelineInitializer.getLastUpdated());
			}
		}
	}
	
	private void updateEmployeeFileData(){
		
		if (DataManager.i().mPipelineEmployeeFile != null) {
			//paypdfileNameLabel.setText(DataManager.i().mPipelineEmployeeFile.);
			Utils.updateControl(paypdfileEmployeesCreatedLabel,DataManager.i().mPipelineEmployeeFile.getEmployeesCreated());
			paypdfileEmployeesUpdatedLabel.setText(String.valueOf(DataManager.i().mPipelineEmployeeFile.getEmployeesUpdated()));
			paypdfileNoChangeCountLabel.setText(String.valueOf(DataManager.i().mPipelineEmployeeFile.getNoChangeCount()));

			//coredata
			Utils.updateControl(paypdfileCoreIdLabel,String.valueOf(DataManager.i().mPipelineEmployeeFile.getId()));
			Utils.updateControl(paypdfileCoreActiveLabel,String.valueOf(DataManager.i().mPipelineEmployeeFile.isActive()));
			Utils.updateControl(paypdfileCoreBODateLabel,DataManager.i().mPipelineEmployeeFile.getBornOn());
			Utils.updateControl(paypdfileCoreLastUpdatedLabel,DataManager.i().mPipelineEmployeeFile.getLastUpdated());
			
			//other sections
			UpdateEmployeeFilePipelineSpecifications();
			updateEmployeeFileDynamicFileData();
			updateEmployeeFileDocumentData();
		}
	
	}
	
	private void loadRawPayPeriodFiles() {
		
		if (DataManager.i().mPipelineEmployeeFile == null) return;
		
		List<EmployeeFile> employeeFiles = DataManager.i().mPipelineEmployeeFile.getEmployeeFiles();
		
		if (employeeFiles != null) {
				List<HBoxCell> list = new ArrayList<>();
				list.add(new HBoxCell("Name","Description", null, 0));
				
				int i = 0;
				for (EmployeeFile employeeFile : employeeFiles) {
					list.add(new HBoxCell(employeeFile.getSpecification().getName(), employeeFile.getSpecification().getDescription(), "View",i));
					i++;
				};	
				
		        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		        paypdfilesRawPayPeriodFilesListView.setItems(myObservableList);		
				
		        paypdfileRawPayPeriodListLabel.setText("Employee Files (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getEmployeeFiles().size()) + ")" );
			} else {
				paypdfileRawPayPeriodListLabel.setText("Employee Files (total: 0)");
			}
	}
	
	private void loadPipelineNotices() {
		
		if (DataManager.i().mPipelineEmployeeFile == null) return;
		
		List<PipelineFileNotice> pipelineFileNotices = DataManager.i().mPipelineEmployeeFile.getNotices();
		
		if (pipelineFileNotices != null) {
				List<HBoxCell> list = new ArrayList<>();
				list.add(new HBoxCell("Message","Type", null, 0));
				
				int i = 0;
				for (PipelineFileNotice pipelineFileNotice : pipelineFileNotices) {
					list.add(new HBoxCell(pipelineFileNotice.getMessage(), pipelineFileNotice.getType().toString(),"View",i));
					i++;
				};	
				
		        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		        paypdfilesPipelineNoticesListView.setItems(myObservableList);		
				
		        paypdfilePipelineNoticesListLabel.setText("Employee Files (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getNotices().size()) + ")" );
			} else {
				paypdfilePipelineNoticesListLabel.setText("Employee Files (total: 0)");
			}
	}
	
	private void loadRecordRejections() {
		
		if (DataManager.i().mPipelineEmployeeFile == null) return;
		
		List<PipelineFileRecordRejection> recordRejections = DataManager.i().mPipelineEmployeeFile.getRecordRejections();
		
		if (recordRejections != null) {
				List<HBoxCell> list = new ArrayList<>();
				list.add(new HBoxCell("Message","Date", null, 0));
				
				int i = 0;
				for (PipelineFileRecordRejection recordRejection : recordRejections) {
					list.add(new HBoxCell(recordRejection.getMessage(), recordRejection.getLastUpdated().toString(),"View",i));
					i++;
				};	
				
		        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		        paypdfilesRecordRejectionsListView.setItems(myObservableList);		
				
		        paypdfileRecordRejectionsListLabel.setText("Record Rejections (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getRecordRejections().size()) + ")" );
			} else {
				paypdfileRecordRejectionsListLabel.setText("Record Rejections (total: 0)");
			}
	}
	
	public void loadSearchFiles() {
		if (DataManager.i().mPipelineEmployeeFiles == null) return;

		String sName;
		ObservableList<String> searchNames = FXCollections.observableArrayList();
		for (int i = 0; i < DataManager.i().mPipelineEmployeeFiles.size();i++) {
			sName = DataManager.i().mPipelineEmployeeFiles.get(i).getSpecification().getName();
			if (sName != null)
				searchNames.add(sName);
		};		
		
		// use a filterlist to wrap the account names for combobox searching later
        FilteredList<String> filteredItems = new FilteredList<String>(searchNames, p -> true);

        // add a listener for the edit control on the combobox
        // the listener will filter the list according to what is in the search box edit control
        paypdfileSearchComboBox.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> {
            final javafx.scene.control.TextField editor = paypdfileSearchComboBox.getEditor();
            final String selected = paypdfileSearchComboBox.getSelectionModel().getSelectedItem();
            
            paypdfileSearchComboBox.show();
            paypdfileSearchComboBox.setVisibleRowCount(10);

            // Run on the GUI thread
            Platform.runLater(() -> {
                if (selected == null || !selected.equals(editor.getText())) {
                    filteredItems.setPredicate(item -> {
                        if (item.toUpperCase().contains(newvalue.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });		
		});
        
		//finally, set our filtered items as the combobox collection
        paypdfileSearchComboBox.setItems(filteredItems);	
		
		//close the dropdown if it is showing
        paypdfileSearchComboBox.hide();
	}
	
	public void onSearchFiles(ActionEvent event) {
		searchFiles();
	}
	
	public void searchFiles() {
		if (DataManager.i().mPipelineEmployeeFiles == null) return;
		
		String sSelection = paypdfileSearchComboBox.getValue();
		String sName;
		
		for (int i = 0; i < DataManager.i().mPipelineEmployeeFiles.size();i++) {
			sName = DataManager.i().mPipelineEmployeeFiles.get(i).getSpecification().getName();;
			if (sName == sSelection) {
				// set the current account
				DataManager.i().setPipelineEmployeeFile(DataManager.i().mPipelineEmployeeFiles.get(i));
				
				break;
			}
		}
		
	}	
	
	//extending the listview for our additional controls
	public static class HBoxCell extends HBox {
         Label lblName = new Label();
         Label lblAddress = new Label();
         Button btnView = new Button();

         HBoxCell(String sName, String sAddress, String sButtonText, int nButtonID) {
              super();

              if (sName == null) sName = "";             
              if (sAddress == null) sAddress = "";
              
              if (sName.contains("null")) sName = "";
              if (sAddress.contains("null")) sAddress = "";
              
              // check to see if we have a bad address from all nulls
              if (sAddress.equals(" ,  "))
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

              if (sButtonText != null) {
            	  btnView.setText(sButtonText);
            	  btnView.setId(String.valueOf(nButtonID));
              }       
              
              if (sButtonText == null) {
            	  lblName.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblAddress.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  this.getChildren().addAll(lblName, lblAddress);
              }else {
            	  this.getChildren().addAll(lblName, lblAddress, btnView);
            	  
            	  //add an event handler for this button
            	  btnView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            		  
            		  @Override
            		  public void handle(MouseEvent event) {
            			  // let the application load the current secondary
            			  //DataManager.i().setSecondary(Integer.parseInt(btnView.getId()));
            		  }
            	  });
              }
              
         }
    }	
	
	//extending the listview for our additional controls
	public static class HBoxEmploymentPeriodCell extends HBox {
         Label lblHireDate = new Label();
         Label lblTerminationDate = new Label();
         Button btnView = new Button();

         HBoxEmploymentPeriodCell(String sHireDate, String sTerminationDate, String sButtonText, int nButtonID) {
              super();

              if (sHireDate == null)  sHireDate = "";
              if (sTerminationDate == null)  sTerminationDate = "";
              
              if (sHireDate.contains("null")) sHireDate = "";
              if (sTerminationDate.contains("null")) sTerminationDate = "";
              
              lblHireDate.setText(sHireDate);
              lblHireDate.setMinWidth(280);
              lblHireDate.setMaxWidth(280);
              lblHireDate.setPrefWidth(280);
              HBox.setHgrow(lblHireDate, Priority.ALWAYS);

              lblTerminationDate.setText(sTerminationDate);
              lblTerminationDate.setMinWidth(390);
              lblTerminationDate.setMaxWidth(390);
              lblTerminationDate.setPrefWidth(390);
              HBox.setHgrow(lblTerminationDate, Priority.ALWAYS);

              if (sButtonText != null) {
            	  btnView.setText(sButtonText);
            	  btnView.setId(String.valueOf(nButtonID));
              }       
              
              if (sButtonText == null) {
            	  lblHireDate.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblTerminationDate.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  this.getChildren().addAll(lblHireDate, lblTerminationDate);
              }else {
            	  this.getChildren().addAll(lblHireDate, lblTerminationDate, btnView);
            	  
            	  //add an event handler for this button
            	  btnView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            		  
            		  @Override
            		  public void handle(MouseEvent event) {
            			  // let the application load the current employment period
            			  DataManager.i().setEmploymentPeriod(Integer.parseInt(btnView.getId()));
            		  }
            	  });
              }
              
         }
    }	

	//extending the listview for our additional controls
	public static class HBoxEmployeeCoveragePeriodCell extends HBox {
         Label lblStartDate = new Label();
         Label lblEndDate = new Label();
         Label lblTitle1 = new Label();
         Label lblTitle2 = new Label();
         CheckBox cbWaived = new CheckBox();
         CheckBox cbIneligible = new CheckBox();
         Button btnView = new Button();

         HBoxEmployeeCoveragePeriodCell(String sStartDate, String sEndDate, String sTitle1, Boolean bWaived, String sTitle2, Boolean bIneligible, String sButtonText, int nButtonID) {
              super();

              if (sStartDate == null)  sStartDate = "";              
              if (sEndDate == null)  sEndDate = "";
              if (sTitle1 == null)  sTitle1 = "";
              if (sTitle2 == null)  sTitle2 = "";
              
              if (sStartDate.contains("null")) sStartDate = "";
              if (sEndDate.contains("null")) sEndDate = "";
              if (sTitle1.contains("null")) sTitle1 = "";
              if (sTitle2.contains("null")) sTitle2 = "";
              
              lblStartDate.setText(sStartDate);
              lblStartDate.setMinWidth(150);
              lblStartDate.setMaxWidth(150);
              lblStartDate.setPrefWidth(150);
              HBox.setHgrow(lblStartDate, Priority.ALWAYS);

              lblEndDate.setText(sEndDate);
              lblEndDate.setMinWidth(150);
              lblEndDate.setMaxWidth(150);
              lblEndDate.setPrefWidth(150);
              HBox.setHgrow(lblEndDate, Priority.ALWAYS);

              //if the button text is null, then we'll call it a header row and add out titles
              if (sButtonText == null) {

            	  lblStartDate.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblEndDate.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblTitle1.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblTitle2.setFont(Font.font(null, FontWeight.BOLD, 13));

            	  lblTitle1.setText(sTitle1);
                  lblTitle1.setMinWidth(185);
                  lblTitle1.setMaxWidth(185);
                  lblTitle1.setPrefWidth(185);

                  lblTitle2.setText(sTitle2);
                  lblTitle2.setMinWidth(185);
                  lblTitle2.setMaxWidth(185);
                  lblTitle2.setPrefWidth(185);
                  
            	  this.getChildren().addAll(lblStartDate, lblEndDate, lblTitle1, lblTitle2);                  
              }
              else {

                  cbWaived.setText("");
                  cbWaived.setMinWidth(185);
                  cbWaived.setMaxWidth(185);
                  cbWaived.setPrefWidth(185);
                  cbWaived.setDisable(true);
                  cbWaived.setSelected(bWaived);

                  cbIneligible.setText("");
                  cbIneligible.setMinWidth(185);
                  cbIneligible.setMaxWidth(185);
                  cbIneligible.setPrefWidth(185);
                  cbIneligible.setDisable(true);
                  cbIneligible.setSelected(bIneligible);

                  btnView.setText(sButtonText);
            	  btnView.setId(String.valueOf(nButtonID));

            	  this.getChildren().addAll(lblStartDate, lblEndDate, cbWaived, cbIneligible, btnView);
            	  
            	  //add an event handler for this button
            	  btnView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            		  
            		  @Override
            		  public void handle(MouseEvent event) {
            			  // let the application load the current employment period
            			 // DataManager.i().setEmployeeCoveragePeriod(Integer.parseInt(btnView.getId()));
            		  }
            	  });
              }
              
         }
    }	
	
	public class ColumnField {
	    private String name;
	    private String column;
	    private String columnIndex;
	    private String parsePattern;

	    public ColumnField(String name, String column, String columnIndex, String parsePattern) {
	        this.name = name;
	        this.column = column;
	        this.columnIndex = columnIndex;
	        this.parsePattern = parsePattern;
	    }

	    public ColumnField(String name, Boolean bColumn, Integer nColumnIndex, String parsePattern) {
	        this.name = name;
	        this.column = String.valueOf(bColumn);
	        this.columnIndex = String.valueOf(nColumnIndex);
	        this.parsePattern = parsePattern;
	    }

	    public String getName() {
	        return name;
	    }

	    public String getColumn() {
	        return column;
	    }

	    public String getColumnIndex() {
	        return columnIndex;
	    }

	    public String getParsePattern() {
	        return parsePattern;
	    }
	}
	

	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINEPAYPERIODFILEEDIT, true);
	}	
	
	@FXML
	private void onShowPipelineSpecs(ActionEvent event) {
		if (paypdfileShowPipelineSpecsButton.getText().contains("Show")) {
			paypdfileShowPipelineSpecsButton.setText("Hide Pipeline Specifications");
			paypdfilePipelineSpecificationsVBox.setVisible(true);
			paypdfilePipelineSpecificationsVBox.setMinHeight(VBox.USE_COMPUTED_SIZE);
			paypdfilePipelineSpecificationsVBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
			paypdfilePipelineSpecificationsVBox.setMaxHeight(VBox.USE_COMPUTED_SIZE);
		} else {
			paypdfileShowPipelineSpecsButton.setText("Show Pipeline Specifications");
			paypdfilePipelineSpecificationsVBox.setVisible(false);			
			paypdfilePipelineSpecificationsVBox.setMinHeight(1);
			paypdfilePipelineSpecificationsVBox.setPrefHeight(1);
			paypdfilePipelineSpecificationsVBox.setMaxHeight(1);
		}
	}	

	@FXML
	private void onShowDynamicFileSpecs(ActionEvent event) {
		if (paypdfileShowDynamicFileSpecsButton.getText().contains("Show")) {
			paypdfileShowDynamicFileSpecsButton.setText("Hide Dynamic File Specifications");
			paypdfileDynamicFileSpecificationsVBox.setVisible(true);
			paypdfileDynamicFileSpecificationsVBox.setMinHeight(VBox.USE_COMPUTED_SIZE);
			paypdfileDynamicFileSpecificationsVBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
			paypdfileDynamicFileSpecificationsVBox.setMaxHeight(VBox.USE_COMPUTED_SIZE);
		} else {
			paypdfileShowDynamicFileSpecsButton.setText("Show Dynamic File Specifications");
			paypdfileDynamicFileSpecificationsVBox.setVisible(false);			
			paypdfileDynamicFileSpecificationsVBox.setMinHeight(1);
			paypdfileDynamicFileSpecificationsVBox.setPrefHeight(1);
			paypdfileDynamicFileSpecificationsVBox.setMaxHeight(1);
		}
	}	

	@FXML
	private void onShowDocument(ActionEvent event) {
		if (paypdfileShowDocumentButton.getText().contains("Show")) {
			paypdfileShowDocumentButton.setText("Hide Document");
			paypdfileDocumentVBox.setVisible(true);
			paypdfileDocumentVBox.setMinHeight(VBox.USE_COMPUTED_SIZE);
			paypdfileDocumentVBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
			paypdfileDocumentVBox.setMaxHeight(VBox.USE_COMPUTED_SIZE);
		} else {
			paypdfileShowDocumentButton.setText("Show Document");
			paypdfileDocumentVBox.setVisible(false);			
			paypdfileDocumentVBox.setMinHeight(1);
			paypdfileDocumentVBox.setPrefHeight(1);
			paypdfileDocumentVBox.setMaxHeight(1);
		}
	}	

/*	public static class Employee {
		 
        private final SimpleStringProperty name;
        private final SimpleStringProperty department;
 
        private Employee(String name, String department) {
            this.name = new SimpleStringProperty(name);
            this.department = new SimpleStringProperty(department);
        }
 
        public String getName() {
            return name.get();
        }
 
        public void setName(String fName) {
            name.set(fName);
        }
 
        public String getDepartment() {
            return department.get();
        }
 
        public void setDepartment(String fName) {
            department.set(fName);
        }
    }	
	*/	
}


