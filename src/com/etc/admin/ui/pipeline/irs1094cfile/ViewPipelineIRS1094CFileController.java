package com.etc.admin.ui.pipeline.irs1094cfile;

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
import com.etc.corvetto.ems.pipeline.entities.RawEmployee;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileSpecification;
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

public class ViewPipelineIRS1094CFileController {
	
	@FXML
	private Button irs1094cEditButton;
	@FXML
	private TableView<ColumnField> irs1094cDynFileColumnFieldTable;
	@FXML
	private TextField irs1094cDynFileDescriptionLabel;
	@FXML
	private TextField irs1094cDynFileTabIndexLabel;
	@FXML
	private TextField irs1094cDynFileHeaderRowIndexLabel;
	@FXML
	private CheckBox irs1094cDynFileCreateEmployeeCheckBox;
	@FXML 
	private VBox irs1094cPipelineSpecificationsVBox; 
	@FXML
	private VBox irs1094cDynamicFileSpecificationsVBox; 
	@FXML
	private VBox irs1094cDocumentVBox; 
	@FXML
	private Button irs1094cShowPipelineSpecsButton;
	@FXML
	private Button irs1094cShowDynamicFileSpecsButton;
	@FXML
	private Button irs1094cShowDocumentButton;
	@FXML
	private ComboBox<String> irs1094cSearchComboBox;
	@FXML
	private TextField irs1094cNameLabel;
	@FXML
	private TextField irs1094cPlansCreatedLabel;
	@FXML
	private TextField irs1094cPlansUpdatedLabel;

	@FXML
	private TextField irs1094cNoChangeCountLabel;
	@FXML
	private TextField irs1094cPipelineNameLabel;
	@FXML
	private TextField irs1094cPipelineDescriptionLabel;
	@FXML
	private TextField irs1094cChannelNameLabel;
	@FXML
	private TextField irs1094cChannelDescriptionLabel;
	@FXML
	private TextField irs1094cParserDescriptionLabel;
	@FXML
	private TextField irs1094cParserPipelineStepTypeLabel;
	@FXML
	private TextField irs1094cParserInterfaceClassLabel;
	@FXML
	private TextField irs1094cParserHandlerClassLabel;
	@FXML
	private TextField irs1094cValidatorDescriptionLabel;
	@FXML
	private TextField irs1094cValidatorPipelineStepTypeLabel;
	@FXML
	private TextField irs1094cValidatorInterfaceClassLabel;
	@FXML
	private TextField irs1094cValidatorHandlerClassLabel;
	@FXML
	private TextField irs1094cConverterDescriptionLabel;
	@FXML
	private TextField irs1094cConverterPipelineStepTypeLabel;
	@FXML
	private TextField irs1094cConverterInterfaceClassLabel;
	@FXML
	private TextField irs1094cConverterHandlerClassLabel;
	@FXML
	private TextField irs1094cFinalizerDescriptionLabel;
	@FXML
	private TextField irs1094cFinalizerPipelineStepTypeLabel;
	@FXML
	private TextField irs1094cFinalizerInterfaceClassLabel;
	@FXML
	private TextField irs1094cFinalizerHandlerClassLabel;
	@FXML
	private TextField irs1094cInitializerDescriptionLabel;
	@FXML
	private TextField irs1094cInitializerPipelineStepTypeLabel;
	@FXML
	private TextField irs1094cInitializerInterfaceClassLabel;
	@FXML
	private TextField irs1094cInitializerHandlerClassLabel;
	@FXML
	private Label irs1094cRawIRS1094CListLabel;
	@FXML
	private Label irs1094cPipelineNoticesListLabel;
	@FXML
	private Label irs1094cRecordRejectionsListLabel;
	@FXML
	private ListView<HBoxCell> irs1094csRawIRS1094CListView;
	@FXML
	private ListView<HBoxCell> irs1094csPipelineNoticesListView;
	@FXML
	private ListView<HBoxCell> irs1094csRecordRejectionsListView;
	@FXML
	private Label irs1094cCoreIdLabel;
	@FXML
	private Label irs1094cCoreActiveLabel;
	@FXML
	private Label irs1094cCoreBODateLabel;
	@FXML
	private Label irs1094cCoreLastUpdatedLabel;
	@FXML
	private Label irs1094cSpecificationCoreIdLabel;
	@FXML
	private Label irs1094cSpecificationCoreActiveLabel;
	@FXML
	private Label irs1094cSpecificationCoreBODateLabel;
	@FXML
	private Label irs1094cSpecificationCoreLastUpdatedLabel;
	@FXML
	private Label irs1094cChannelCoreIdLabel;
	@FXML
	private Label irs1094cChannelCoreActiveLabel;
	@FXML
	private Label irs1094cChannelCoreBODateLabel;
	@FXML
	private Label irs1094cChannelCoreLastUpdatedLabel;
	@FXML
	private Label irs1094cInitializerCoreIdLabel;
	@FXML
	private Label irs1094cInitializerCoreActiveLabel;
	@FXML
	private Label irs1094cInitializerCoreBODateLabel;
	@FXML
	private Label irs1094cInitializerCoreLastUpdatedLabel;
	@FXML
	private Label irs1094cParserCoreIdLabel;
	@FXML
	private Label irs1094cParserCoreActiveLabel;
	@FXML
	private Label irs1094cParserCoreBODateLabel;
	@FXML
	private Label irs1094cParserCoreLastUpdatedLabel;
	@FXML
	private Label irs1094cValidatorCoreIdLabel;
	@FXML
	private Label irs1094cValidatorCoreActiveLabel;
	@FXML
	private Label irs1094cValidatorCoreBODateLabel;
	@FXML
	private Label irs1094cValidatorCoreLastUpdatedLabel;
	@FXML
	private Label irs1094cConverterCoreIdLabel;
	@FXML
	private Label irs1094cConverterCoreActiveLabel;
	@FXML
	private Label irs1094cConverterCoreBODateLabel;
	@FXML
	private Label irs1094cConverterCoreLastUpdatedLabel;
	@FXML
	private Label irs1094cFinalizerCoreIdLabel;
	@FXML
	private Label irs1094cFinalizerCoreActiveLabel;
	@FXML
	private Label irs1094cFinalizerCoreBODateLabel;
	@FXML
	private Label irs1094cFinalizerCoreLastUpdatedLabel;
	@FXML
	private TextField irs1094cDocumentNameLabel;
	@FXML
	private TextField irs1094cDocumentSizeLabel;
	@FXML
	private TextField irs1094cDocumentDescriptionLabel;
	@FXML
	private TextField irs1094cDocumentFilenameLabel;
	@FXML
	private TextField irs1094cDocumentTypeLabel;
	@FXML
	private TextField irs1094cAccountLabel;
	@FXML
	private TextField irs1094cEmployerLabel;
	@FXML
	private Label irs1094cDocumentCoreIdLabel;
	@FXML
	private Label irs1094cDocumentCoreActiveLabel;
	@FXML
	private Label irs1094cDocumentCoreBODateLabel;
	@FXML
	private Label irs1094cDocumentCoreLastUpdatedLabel;

	
	
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
		loadPipelineNotices();
		loadRecordRejections();
		loadRawIRS1094C();
	}
	
	private void clearScreen() {
		//clear all the labels
		irs1094cDynFileDescriptionLabel.setText(" ");
		irs1094cDynFileTabIndexLabel.setText(" ");
		irs1094cDynFileHeaderRowIndexLabel.setText(" ");
		irs1094cDynFileCreateEmployeeCheckBox.setText(" ");
		irs1094cNameLabel.setText(" ");
		irs1094cPlansCreatedLabel.setText(" ");
		irs1094cPlansUpdatedLabel.setText(" ");
		irs1094cNoChangeCountLabel.setText(" ");
		irs1094cPipelineNameLabel.setText(" ");
		irs1094cPipelineDescriptionLabel.setText(" ");
		irs1094cChannelNameLabel.setText(" ");
		irs1094cChannelDescriptionLabel.setText(" ");
		irs1094cParserDescriptionLabel.setText(" ");
		irs1094cParserPipelineStepTypeLabel.setText(" ");
		irs1094cParserInterfaceClassLabel.setText(" ");
		irs1094cParserHandlerClassLabel.setText(" ");
		irs1094cValidatorDescriptionLabel.setText(" ");
		irs1094cValidatorPipelineStepTypeLabel.setText(" ");
		irs1094cValidatorInterfaceClassLabel.setText(" ");
		irs1094cValidatorHandlerClassLabel.setText(" ");
		irs1094cConverterDescriptionLabel.setText(" ");
		irs1094cConverterPipelineStepTypeLabel.setText(" ");
		irs1094cConverterInterfaceClassLabel.setText(" ");
		irs1094cConverterHandlerClassLabel.setText(" ");
		irs1094cFinalizerDescriptionLabel.setText(" ");
		irs1094cFinalizerPipelineStepTypeLabel.setText(" ");
		irs1094cFinalizerInterfaceClassLabel.setText(" ");
		irs1094cFinalizerHandlerClassLabel.setText(" ");
		irs1094cInitializerDescriptionLabel.setText(" ");
		irs1094cInitializerPipelineStepTypeLabel.setText(" ");
		irs1094cInitializerInterfaceClassLabel.setText(" ");
		irs1094cInitializerHandlerClassLabel.setText(" ");
		irs1094cCoreIdLabel.setText(" ");
		irs1094cCoreActiveLabel.setText(" ");
		irs1094cCoreBODateLabel.setText(" ");
		irs1094cCoreLastUpdatedLabel.setText(" ");
		irs1094cSpecificationCoreIdLabel.setText(" ");
		irs1094cSpecificationCoreActiveLabel.setText(" ");
		irs1094cSpecificationCoreBODateLabel.setText(" ");
		irs1094cSpecificationCoreLastUpdatedLabel.setText(" ");
		irs1094cChannelCoreIdLabel.setText(" ");
		irs1094cChannelCoreActiveLabel.setText(" ");
		irs1094cChannelCoreBODateLabel.setText(" ");
		irs1094cChannelCoreLastUpdatedLabel.setText(" ");
		irs1094cInitializerCoreIdLabel.setText(" ");
		irs1094cInitializerCoreActiveLabel.setText(" ");
		irs1094cInitializerCoreBODateLabel.setText(" ");
		irs1094cInitializerCoreLastUpdatedLabel.setText(" ");
		irs1094cParserCoreIdLabel.setText(" ");
		irs1094cParserCoreActiveLabel.setText(" ");
		irs1094cParserCoreBODateLabel.setText(" ");
		irs1094cParserCoreLastUpdatedLabel.setText(" ");
		irs1094cValidatorCoreIdLabel.setText(" ");
		irs1094cValidatorCoreActiveLabel.setText(" ");
		irs1094cValidatorCoreBODateLabel.setText(" ");
		irs1094cValidatorCoreLastUpdatedLabel.setText(" ");
		irs1094cConverterCoreIdLabel.setText(" ");
		irs1094cConverterCoreActiveLabel.setText(" ");
		irs1094cConverterCoreBODateLabel.setText(" ");
		irs1094cConverterCoreLastUpdatedLabel.setText(" ");
		irs1094cFinalizerCoreIdLabel.setText(" ");
		irs1094cFinalizerCoreActiveLabel.setText(" ");
		irs1094cFinalizerCoreBODateLabel.setText(" ");
		irs1094cFinalizerCoreLastUpdatedLabel.setText(" ");
		irs1094cDocumentNameLabel.setText(" ");
		irs1094cDocumentSizeLabel.setText(" ");
		irs1094cDocumentDescriptionLabel.setText(" ");
		irs1094cDocumentFilenameLabel.setText(" ");
		irs1094cDocumentTypeLabel.setText(" ");
		irs1094cAccountLabel.setText(" ");
		irs1094cEmployerLabel.setText(" ");
		irs1094cDocumentCoreIdLabel.setText(" ");
		irs1094cDocumentCoreActiveLabel.setText(" ");
		irs1094cDocumentCoreBODateLabel.setText(" ");
		irs1094cDocumentCoreLastUpdatedLabel.setText(" ");
		
		//no edit unless something is selected
		irs1094cEditButton.setDisable(true);
		
		loadBlankColumnFields();
	}
	
	private void loadSearchBox(){
		
	}

	private void initControls() {
		
		//mark the checkboxes read only
		irs1094cDynFileCreateEmployeeCheckBox.setDisable(true);

		
		//hide the sections
		irs1094cShowPipelineSpecsButton.setText("Show Pipeline Specifications");
		irs1094cPipelineSpecificationsVBox.setVisible(false);			
		irs1094cPipelineSpecificationsVBox.setMinHeight(1);
		irs1094cPipelineSpecificationsVBox.setPrefHeight(1);
		irs1094cPipelineSpecificationsVBox.setMaxHeight(1);

		irs1094cShowDynamicFileSpecsButton.setText("Show Dynamic File Specifications");
		irs1094cDynamicFileSpecificationsVBox.setVisible(false);			
		irs1094cDynamicFileSpecificationsVBox.setMinHeight(1);
		irs1094cDynamicFileSpecificationsVBox.setPrefHeight(1);
		irs1094cDynamicFileSpecificationsVBox.setMaxHeight(1);
		
		irs1094cShowDocumentButton.setText("Show Document");
		irs1094cDocumentVBox.setVisible(false);			
		irs1094cDocumentVBox.setMinHeight(1);
		irs1094cDocumentVBox.setPrefHeight(1);
		irs1094cDocumentVBox.setMaxHeight(1);
		
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
		
		irs1094cDynFileColumnFieldTable.getColumns().clear();
		irs1094cDynFileColumnFieldTable.getColumns().add(nameColumn); 
		irs1094cDynFileColumnFieldTable.getColumns().add(columnColumn);
		irs1094cDynFileColumnFieldTable.getColumns().add(columnIndexColumn);
		irs1094cDynFileColumnFieldTable.getColumns().add(parsePatternColumn);		
			
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
        irs1094cDynFileColumnFieldTable.setItems(myObservableList);
	}

	private void updateEmployeeFileDocumentData(){
		
		Document document = DataManager.i().mPipelineEmployeeFile.getPipelineInformation().getDocument();
		if (document != null) {
			Utils.updateControl(irs1094cDocumentNameLabel, document.getName());
			Utils.updateControl(irs1094cDocumentSizeLabel, document.getName());
			Utils.updateControl(irs1094cDocumentDescriptionLabel, document.getName());
			Utils.updateControl(irs1094cDocumentFilenameLabel, document.getName());
		
			Utils.updateControl(irs1094cDocumentTypeLabel, document.getDocumentType().toString());
			
			Utils.updateControl(irs1094cDocumentCoreIdLabel,String.valueOf(document.getId()));
			Utils.updateControl(irs1094cDocumentCoreActiveLabel,String.valueOf(document.isActive()));
			Utils.updateControl(irs1094cDocumentCoreBODateLabel,document.getBornOn());
			Utils.updateControl(irs1094cDocumentCoreLastUpdatedLabel,document.getLastUpdated());
		}

	}

	private void updateEmployeeFileDynamicFileData() {
		
		//need to load the dynamic file specification
		DynamicEmployeeFileSpecification fSpec = DataManager.i().mDynamicEmployeeFileSpecification;
		
//		if (fSpec != null) {
			Utils.updateControl(irs1094cDynFileDescriptionLabel, fSpec.getName());
			Utils.updateControl(irs1094cDynFileTabIndexLabel, fSpec.getTabIndex());
			Utils.updateControl(irs1094cDynFileHeaderRowIndexLabel, fSpec.getHeaderRowIndex());
			Utils.updateControl(irs1094cDynFileCreateEmployeeCheckBox, fSpec.isCreateEmployee());
			
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
	        irs1094cDynFileColumnFieldTable.setItems(myObservableList);
//		}
	}
	
	private void UpdateEmployeeFilePipelineSpecifications(){

		PipelineSpecification pipelineSpecification = DataManager.i().mPipelineEmployeeFile.getSpecification();
		if (pipelineSpecification != null) {
			Utils.updateControl(irs1094cPipelineNameLabel,pipelineSpecification.getName());
			Utils.updateControl(irs1094cPipelineDescriptionLabel,pipelineSpecification.getDescription());

			Utils.updateControl(irs1094cSpecificationCoreIdLabel,String.valueOf(pipelineSpecification.getId()));
			Utils.updateControl(irs1094cSpecificationCoreActiveLabel,String.valueOf(pipelineSpecification.isActive()));
			Utils.updateControl(irs1094cSpecificationCoreBODateLabel,pipelineSpecification.getBornOn());
			Utils.updateControl(irs1094cSpecificationCoreLastUpdatedLabel,pipelineSpecification.getLastUpdated());
									
			PipelineChannel pipelineChannel = pipelineSpecification.getChannel();
			if (pipelineChannel!= null){
				Utils.updateControl(irs1094cChannelNameLabel,pipelineChannel.getName());
				Utils.updateControl(irs1094cChannelDescriptionLabel,pipelineChannel.getName());
			
				Utils.updateControl(irs1094cChannelCoreIdLabel,String.valueOf(pipelineChannel.getId()));
				Utils.updateControl(irs1094cChannelCoreActiveLabel,String.valueOf(pipelineChannel.isActive()));
				Utils.updateControl(irs1094cChannelCoreBODateLabel,pipelineChannel.getBornOn());
				Utils.updateControl(irs1094cChannelCoreLastUpdatedLabel,pipelineChannel.getLastUpdated());
			}
			
			PipelineFileStepHandler pipelineParser = pipelineSpecification.getParser();
			if (pipelineParser!= null){
				Utils.updateControl(irs1094cParserDescriptionLabel,pipelineParser.getName());
				Utils.updateControl(irs1094cParserPipelineStepTypeLabel,pipelineParser.getType().toString());
				Utils.updateControl(irs1094cParserInterfaceClassLabel,pipelineParser.getInterfaceClass());
				Utils.updateControl(irs1094cParserHandlerClassLabel,pipelineParser.getHandlerClass());
				
				Utils.updateControl(irs1094cParserCoreIdLabel,String.valueOf(pipelineParser.getId()));
				Utils.updateControl(irs1094cParserCoreActiveLabel,String.valueOf(pipelineParser.isActive()));
				Utils.updateControl(irs1094cParserCoreBODateLabel,pipelineParser.getBornOn());
				Utils.updateControl(irs1094cParserCoreLastUpdatedLabel,pipelineParser.getLastUpdated());
			}
			
			PipelineFileStepHandler pipelineValidator = pipelineSpecification.getValidator();
			if (pipelineValidator!= null){
				Utils.updateControl(irs1094cValidatorDescriptionLabel,pipelineValidator.getName());
				Utils.updateControl(irs1094cValidatorPipelineStepTypeLabel,pipelineValidator.getType().toString());
				Utils.updateControl(irs1094cValidatorInterfaceClassLabel,pipelineValidator.getInterfaceClass());
				Utils.updateControl(irs1094cValidatorHandlerClassLabel,pipelineValidator.getHandlerClass());
				
				Utils.updateControl(irs1094cValidatorCoreIdLabel,String.valueOf(pipelineValidator.getId()));
				Utils.updateControl(irs1094cValidatorCoreActiveLabel,String.valueOf(pipelineValidator.isActive()));
				Utils.updateControl(irs1094cValidatorCoreBODateLabel,pipelineValidator.getBornOn());
				Utils.updateControl(irs1094cValidatorCoreLastUpdatedLabel,pipelineValidator.getLastUpdated());
			}
			
			PipelineFileStepHandler pipelineConverter = pipelineSpecification.getConverter();
			if (pipelineValidator!= null){
				Utils.updateControl(irs1094cConverterDescriptionLabel,pipelineConverter.getName());
				Utils.updateControl(irs1094cConverterPipelineStepTypeLabel,pipelineConverter.getType().toString());
				Utils.updateControl(irs1094cConverterInterfaceClassLabel,pipelineConverter.getInterfaceClass());
				Utils.updateControl(irs1094cConverterHandlerClassLabel,pipelineConverter.getHandlerClass());
				
				Utils.updateControl(irs1094cConverterCoreIdLabel,String.valueOf(pipelineConverter.getId()));
				Utils.updateControl(irs1094cConverterCoreActiveLabel,String.valueOf(pipelineConverter.isActive()));
				Utils.updateControl(irs1094cConverterCoreBODateLabel,pipelineConverter.getBornOn());
				Utils.updateControl(irs1094cConverterCoreLastUpdatedLabel,pipelineConverter.getLastUpdated());
			}
			
			PipelineFileStepHandler pipelineFinalizer = pipelineSpecification.getFinalizer();
			if (pipelineFinalizer!= null){
				Utils.updateControl(irs1094cFinalizerDescriptionLabel,pipelineFinalizer.getName());
				Utils.updateControl(irs1094cFinalizerPipelineStepTypeLabel,pipelineFinalizer.getType().toString());
				Utils.updateControl(irs1094cFinalizerInterfaceClassLabel,pipelineFinalizer.getInterfaceClass());
				Utils.updateControl(irs1094cFinalizerHandlerClassLabel,pipelineFinalizer.getHandlerClass());
				
				Utils.updateControl(irs1094cFinalizerCoreIdLabel,String.valueOf(pipelineFinalizer.getId()));
				Utils.updateControl(irs1094cFinalizerCoreActiveLabel,String.valueOf(pipelineFinalizer.isActive()));
				Utils.updateControl(irs1094cFinalizerCoreBODateLabel,pipelineFinalizer.getBornOn());
				Utils.updateControl(irs1094cFinalizerCoreLastUpdatedLabel,pipelineFinalizer.getLastUpdated());
			}
			
			PipelineFileStepHandler pipelineInitializer = pipelineSpecification.getInitializer();
			if (pipelineFinalizer!= null){
				Utils.updateControl(irs1094cInitializerDescriptionLabel,pipelineInitializer.getName());
				Utils.updateControl(irs1094cInitializerPipelineStepTypeLabel,pipelineInitializer.getType().toString());
				Utils.updateControl(irs1094cInitializerInterfaceClassLabel,pipelineInitializer.getInterfaceClass());
				Utils.updateControl(irs1094cInitializerHandlerClassLabel,pipelineInitializer.getHandlerClass());
				
				Utils.updateControl(irs1094cInitializerCoreIdLabel,String.valueOf(pipelineInitializer.getId()));
				Utils.updateControl(irs1094cInitializerCoreActiveLabel,String.valueOf(pipelineInitializer.isActive()));
				Utils.updateControl(irs1094cInitializerCoreBODateLabel,pipelineInitializer.getBornOn());
				Utils.updateControl(irs1094cInitializerCoreLastUpdatedLabel,pipelineInitializer.getLastUpdated());
			}
		}
	}
	
	private void updateEmployeeFileData(){
		
		if (DataManager.i().mPipelineEmployeeFile != null) {
			//irs1094cNameLabel.setText(DataManager.i().mPipelineEmployeeFile.);
			Utils.updateControl(irs1094cPlansCreatedLabel,DataManager.i().mPipelineEmployeeFile.getEmployeesCreated());
			irs1094cPlansUpdatedLabel.setText(String.valueOf(DataManager.i().mPipelineEmployeeFile.getEmployeesUpdated()));
			irs1094cNoChangeCountLabel.setText(String.valueOf(DataManager.i().mPipelineEmployeeFile.getNoChangeCount()));

			//coredata
			Utils.updateControl(irs1094cCoreIdLabel,String.valueOf(DataManager.i().mPipelineEmployeeFile.getId()));
			Utils.updateControl(irs1094cCoreActiveLabel,String.valueOf(DataManager.i().mPipelineEmployeeFile.isActive()));
			Utils.updateControl(irs1094cCoreBODateLabel,DataManager.i().mPipelineEmployeeFile.getBornOn());
			Utils.updateControl(irs1094cCoreLastUpdatedLabel,DataManager.i().mPipelineEmployeeFile.getLastUpdated());
			
			//other sections
			UpdateEmployeeFilePipelineSpecifications();
			updateEmployeeFileDynamicFileData();
			updateEmployeeFileDocumentData();
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
		        irs1094csPipelineNoticesListView.setItems(myObservableList);		
				
		        irs1094cPipelineNoticesListLabel.setText("Employee Files (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getNotices().size()) + ")" );
			} else {
				irs1094cPipelineNoticesListLabel.setText("Employee Files (total: 0)");
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
		        irs1094csRecordRejectionsListView.setItems(myObservableList);		
				
		        irs1094cRecordRejectionsListLabel.setText("Record Rejections (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getRecordRejections().size()) + ")" );
			} else {
				irs1094cRecordRejectionsListLabel.setText("Record Rejections (total: 0)");
			}
	}
	
	private void loadRawIRS1094C() {
		
		if (DataManager.i().mPipelineEmployeeFile == null) return;
		
		List<RawEmployee> rawEmployees = DataManager.i().mPipelineEmployeeFile.getRawEmployees();
		
		if (rawEmployees != null) {
				List<HBoxCell> list = new ArrayList<>();
				list.add(new HBoxCell("Name","Date", null, 0));
				
				int i = 0;
				for (RawEmployee rawEmployee : rawEmployees) {
					list.add(new HBoxCell(rawEmployee.getFullName(), rawEmployee.getCity(),"View",i));
					i++;
				};	
				
		        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		        irs1094csRawIRS1094CListView.setItems(myObservableList);		
				
		        irs1094cRawIRS1094CListLabel.setText("Record Rejections (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getRawEmployees().size()) + ")" );
			} else {
				irs1094cRawIRS1094CListLabel.setText("Record Rejections (total: 0)");
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
        irs1094cSearchComboBox.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> {
            final javafx.scene.control.TextField editor = irs1094cSearchComboBox.getEditor();
            final String selected = irs1094cSearchComboBox.getSelectionModel().getSelectedItem();
            
            irs1094cSearchComboBox.show();
            irs1094cSearchComboBox.setVisibleRowCount(10);

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
        irs1094cSearchComboBox.setItems(filteredItems);	
		
		//close the dropdown if it is showing
        irs1094cSearchComboBox.hide();
	}
	
	public void onSearchFiles(ActionEvent event) {
		searchFiles();
	}
	
	public void searchFiles() {
		if (DataManager.i().mPipelineEmployeeFiles == null) return;
		
		String sSelection = irs1094cSearchComboBox.getValue();
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
		EtcAdmin.i().setScreen(ScreenType.PIPELINEIRS1094CFILEEDIT, true);
	}	
	
	@FXML
	private void onShowPipelineSpecs(ActionEvent event) {
		if (irs1094cShowPipelineSpecsButton.getText().contains("Show")) {
			irs1094cShowPipelineSpecsButton.setText("Hide Pipeline Specifications");
			irs1094cPipelineSpecificationsVBox.setVisible(true);
			irs1094cPipelineSpecificationsVBox.setMinHeight(VBox.USE_COMPUTED_SIZE);
			irs1094cPipelineSpecificationsVBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
			irs1094cPipelineSpecificationsVBox.setMaxHeight(VBox.USE_COMPUTED_SIZE);
		} else {
			irs1094cShowPipelineSpecsButton.setText("Show Pipeline Specifications");
			irs1094cPipelineSpecificationsVBox.setVisible(false);			
			irs1094cPipelineSpecificationsVBox.setMinHeight(1);
			irs1094cPipelineSpecificationsVBox.setPrefHeight(1);
			irs1094cPipelineSpecificationsVBox.setMaxHeight(1);
		}
	}	

	@FXML
	private void onShowDynamicFileSpecs(ActionEvent event) {
		if (irs1094cShowDynamicFileSpecsButton.getText().contains("Show")) {
			irs1094cShowDynamicFileSpecsButton.setText("Hide Dynamic File Specifications");
			irs1094cDynamicFileSpecificationsVBox.setVisible(true);
			irs1094cDynamicFileSpecificationsVBox.setMinHeight(VBox.USE_COMPUTED_SIZE);
			irs1094cDynamicFileSpecificationsVBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
			irs1094cDynamicFileSpecificationsVBox.setMaxHeight(VBox.USE_COMPUTED_SIZE);
		} else {
			irs1094cShowDynamicFileSpecsButton.setText("Show Dynamic File Specifications");
			irs1094cDynamicFileSpecificationsVBox.setVisible(false);			
			irs1094cDynamicFileSpecificationsVBox.setMinHeight(1);
			irs1094cDynamicFileSpecificationsVBox.setPrefHeight(1);
			irs1094cDynamicFileSpecificationsVBox.setMaxHeight(1);
		}
	}	

	@FXML
	private void onShowDocument(ActionEvent event) {
		if (irs1094cShowDocumentButton.getText().contains("Show")) {
			irs1094cShowDocumentButton.setText("Hide Document");
			irs1094cDocumentVBox.setVisible(true);
			irs1094cDocumentVBox.setMinHeight(VBox.USE_COMPUTED_SIZE);
			irs1094cDocumentVBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
			irs1094cDocumentVBox.setMaxHeight(VBox.USE_COMPUTED_SIZE);
		} else {
			irs1094cShowDocumentButton.setText("Show Document");
			irs1094cDocumentVBox.setVisible(false);			
			irs1094cDocumentVBox.setMinHeight(1);
			irs1094cDocumentVBox.setPrefHeight(1);
			irs1094cDocumentVBox.setMaxHeight(1);
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


