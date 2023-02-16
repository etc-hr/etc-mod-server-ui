package com.etc.admin.ui.pipeline.irs1095cfile;

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

public class ViewPipelineIRS1095CFileController {
	
	@FXML
	private Button irs1095cEditButton;
	@FXML
	private TableView<ColumnField> irs1095cDynFileColumnFieldTable;
	@FXML
	private TextField irs1095cDynFileDescriptionLabel;
	@FXML
	private TextField irs1095cDynFileTabIndexLabel;
	@FXML
	private TextField irs1095cDynFileHeaderRowIndexLabel;
	@FXML
	private CheckBox irs1095cDynFileCreateEmployeeCheckBox;
	@FXML 
	private VBox irs1095cPipelineSpecificationsVBox; 
	@FXML
	private VBox irs1095cDynamicFileSpecificationsVBox; 
	@FXML
	private VBox irs1095cDocumentVBox; 
	@FXML
	private Button irs1095cShowPipelineSpecsButton;
	@FXML
	private Button irs1095cShowDynamicFileSpecsButton;
	@FXML
	private Button irs1095cShowDocumentButton;
	@FXML
	private ComboBox<String> irs1095cSearchComboBox;
	@FXML
	private TextField irs1095cNameLabel;
	@FXML
	private TextField irs1095cEmployeesCreatedLabel;
	@FXML
	private TextField irs1095cEmployeesUpdatedLabel;
	@FXML
	private TextField irs1095cSecondariesCreatedLabel;
	@FXML
	private TextField irs1095cSecondariesUpdatedLabel;
	@FXML
	private TextField irs1095cEmployeeIRS1095CCreatedLabel;
	@FXML
	private TextField irs1095cEmployeeIRS1095CUpdatedLabel;
	@FXML
	private TextField irs1095cSecondaryIRS1095CCreatedLabel;
	@FXML
	private TextField irs1095cSecondaryIRS1095CUpdatedLabel;
	@FXML
	private TextField irs1095cNoChangeCountLabel;
	@FXML
	private TextField irs1095cPipelineNameLabel;
	@FXML
	private TextField irs1095cPipelineDescriptionLabel;
	@FXML
	private TextField irs1095cChannelNameLabel;
	@FXML
	private TextField irs1095cChannelDescriptionLabel;
	@FXML
	private TextField irs1095cParserDescriptionLabel;
	@FXML
	private TextField irs1095cParserPipelineStepTypeLabel;
	@FXML
	private TextField irs1095cParserInterfaceClassLabel;
	@FXML
	private TextField irs1095cParserHandlerClassLabel;
	@FXML
	private TextField irs1095cValidatorDescriptionLabel;
	@FXML
	private TextField irs1095cValidatorPipelineStepTypeLabel;
	@FXML
	private TextField irs1095cValidatorInterfaceClassLabel;
	@FXML
	private TextField irs1095cValidatorHandlerClassLabel;
	@FXML
	private TextField irs1095cConverterDescriptionLabel;
	@FXML
	private TextField irs1095cConverterPipelineStepTypeLabel;
	@FXML
	private TextField irs1095cConverterInterfaceClassLabel;
	@FXML
	private TextField irs1095cConverterHandlerClassLabel;
	@FXML
	private TextField irs1095cFinalizerDescriptionLabel;
	@FXML
	private TextField irs1095cFinalizerPipelineStepTypeLabel;
	@FXML
	private TextField irs1095cFinalizerInterfaceClassLabel;
	@FXML
	private TextField irs1095cFinalizerHandlerClassLabel;
	@FXML
	private TextField irs1095cInitializerDescriptionLabel;
	@FXML
	private TextField irs1095cInitializerPipelineStepTypeLabel;
	@FXML
	private TextField irs1095cInitializerInterfaceClassLabel;
	@FXML
	private TextField irs1095cInitializerHandlerClassLabel;
	@FXML
	private Label irs1095cRawIRS1095CListLabel;
	@FXML
	private Label irs1095cRawCoveredSecondaryListLabel;
	@FXML
	private Label irs1095cPipelineNoticesListLabel;
	@FXML
	private Label irs1095cRecordRejectionsListLabel;
	@FXML
	private ListView<HBoxCell> irs1095cRawIRS1095CListView;
	@FXML
	private ListView<HBoxCell> irs1095cRawCoveredSecondaryListView;
	@FXML
	private ListView<HBoxCell> irs1095cPipelineNoticesListView;
	@FXML
	private ListView<HBoxCell> irs1095cRecordRejectionsListView;
	@FXML
	private Label irs1095cCoreIdLabel;
	@FXML
	private Label irs1095cCoreActiveLabel;
	@FXML
	private Label irs1095cCoreBODateLabel;
	@FXML
	private Label irs1095cCoreLastUpdatedLabel;
	@FXML
	private Label irs1095cSpecificationCoreIdLabel;
	@FXML
	private Label irs1095cSpecificationCoreActiveLabel;
	@FXML
	private Label irs1095cSpecificationCoreBODateLabel;
	@FXML
	private Label irs1095cSpecificationCoreLastUpdatedLabel;
	@FXML
	private Label irs1095cChannelCoreIdLabel;
	@FXML
	private Label irs1095cChannelCoreActiveLabel;
	@FXML
	private Label irs1095cChannelCoreBODateLabel;
	@FXML
	private Label irs1095cChannelCoreLastUpdatedLabel;
	@FXML
	private Label irs1095cInitializerCoreIdLabel;
	@FXML
	private Label irs1095cInitializerCoreActiveLabel;
	@FXML
	private Label irs1095cInitializerCoreBODateLabel;
	@FXML
	private Label irs1095cInitializerCoreLastUpdatedLabel;
	@FXML
	private Label irs1095cParserCoreIdLabel;
	@FXML
	private Label irs1095cParserCoreActiveLabel;
	@FXML
	private Label irs1095cParserCoreBODateLabel;
	@FXML
	private Label irs1095cParserCoreLastUpdatedLabel;
	@FXML
	private Label irs1095cValidatorCoreIdLabel;
	@FXML
	private Label irs1095cValidatorCoreActiveLabel;
	@FXML
	private Label irs1095cValidatorCoreBODateLabel;
	@FXML
	private Label irs1095cValidatorCoreLastUpdatedLabel;
	@FXML
	private Label irs1095cConverterCoreIdLabel;
	@FXML
	private Label irs1095cConverterCoreActiveLabel;
	@FXML
	private Label irs1095cConverterCoreBODateLabel;
	@FXML
	private Label irs1095cConverterCoreLastUpdatedLabel;
	@FXML
	private Label irs1095cFinalizerCoreIdLabel;
	@FXML
	private Label irs1095cFinalizerCoreActiveLabel;
	@FXML
	private Label irs1095cFinalizerCoreBODateLabel;
	@FXML
	private Label irs1095cFinalizerCoreLastUpdatedLabel;
	@FXML
	private TextField irs1095cDocumentNameLabel;
	@FXML
	private TextField irs1095cDocumentSizeLabel;
	@FXML
	private TextField irs1095cDocumentDescriptionLabel;
	@FXML
	private TextField irs1095cDocumentFilenameLabel;
	@FXML
	private TextField irs1095cDocumentTypeLabel;
	@FXML
	private TextField irs1095cAccountLabel;
	@FXML
	private TextField irs1095cEmployerLabel;
	@FXML
	private Label irs1095cDocumentCoreIdLabel;
	@FXML
	private Label irs1095cDocumentCoreActiveLabel;
	@FXML
	private Label irs1095cDocumentCoreBODateLabel;
	@FXML
	private Label irs1095cDocumentCoreLastUpdatedLabel;

	
	
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
		loadRawIRS1095C();
		loadRawCoveredSecondary();
	}
	
	private void clearScreen() {
		//clear all the labels
		irs1095cDynFileDescriptionLabel.setText(" ");
		irs1095cDynFileTabIndexLabel.setText(" ");
		irs1095cDynFileHeaderRowIndexLabel.setText(" ");
		irs1095cDynFileCreateEmployeeCheckBox.setText(" ");
		irs1095cNameLabel.setText(" ");
		irs1095cEmployeesCreatedLabel.setText(" ");
		irs1095cEmployeesUpdatedLabel.setText(" ");
		irs1095cSecondariesCreatedLabel.setText(" ");
		irs1095cSecondariesUpdatedLabel.setText(" ");
		irs1095cEmployeeIRS1095CCreatedLabel.setText(" ");
		irs1095cEmployeeIRS1095CUpdatedLabel.setText(" ");
		irs1095cSecondaryIRS1095CCreatedLabel.setText(" ");
		irs1095cSecondaryIRS1095CUpdatedLabel.setText(" ");
		irs1095cNoChangeCountLabel.setText(" ");
		irs1095cPipelineNameLabel.setText(" ");
		irs1095cPipelineDescriptionLabel.setText(" ");
		irs1095cChannelNameLabel.setText(" ");
		irs1095cChannelDescriptionLabel.setText(" ");
		irs1095cParserDescriptionLabel.setText(" ");
		irs1095cParserPipelineStepTypeLabel.setText(" ");
		irs1095cParserInterfaceClassLabel.setText(" ");
		irs1095cParserHandlerClassLabel.setText(" ");
		irs1095cValidatorDescriptionLabel.setText(" ");
		irs1095cValidatorPipelineStepTypeLabel.setText(" ");
		irs1095cValidatorInterfaceClassLabel.setText(" ");
		irs1095cValidatorHandlerClassLabel.setText(" ");
		irs1095cConverterDescriptionLabel.setText(" ");
		irs1095cConverterPipelineStepTypeLabel.setText(" ");
		irs1095cConverterInterfaceClassLabel.setText(" ");
		irs1095cConverterHandlerClassLabel.setText(" ");
		irs1095cFinalizerDescriptionLabel.setText(" ");
		irs1095cFinalizerPipelineStepTypeLabel.setText(" ");
		irs1095cFinalizerInterfaceClassLabel.setText(" ");
		irs1095cFinalizerHandlerClassLabel.setText(" ");
		irs1095cInitializerDescriptionLabel.setText(" ");
		irs1095cInitializerPipelineStepTypeLabel.setText(" ");
		irs1095cInitializerInterfaceClassLabel.setText(" ");
		irs1095cInitializerHandlerClassLabel.setText(" ");
		irs1095cCoreIdLabel.setText(" ");
		irs1095cCoreActiveLabel.setText(" ");
		irs1095cCoreBODateLabel.setText(" ");
		irs1095cCoreLastUpdatedLabel.setText(" ");
		irs1095cSpecificationCoreIdLabel.setText(" ");
		irs1095cSpecificationCoreActiveLabel.setText(" ");
		irs1095cSpecificationCoreBODateLabel.setText(" ");
		irs1095cSpecificationCoreLastUpdatedLabel.setText(" ");
		irs1095cChannelCoreIdLabel.setText(" ");
		irs1095cChannelCoreActiveLabel.setText(" ");
		irs1095cChannelCoreBODateLabel.setText(" ");
		irs1095cChannelCoreLastUpdatedLabel.setText(" ");
		irs1095cInitializerCoreIdLabel.setText(" ");
		irs1095cInitializerCoreActiveLabel.setText(" ");
		irs1095cInitializerCoreBODateLabel.setText(" ");
		irs1095cInitializerCoreLastUpdatedLabel.setText(" ");
		irs1095cParserCoreIdLabel.setText(" ");
		irs1095cParserCoreActiveLabel.setText(" ");
		irs1095cParserCoreBODateLabel.setText(" ");
		irs1095cParserCoreLastUpdatedLabel.setText(" ");
		irs1095cValidatorCoreIdLabel.setText(" ");
		irs1095cValidatorCoreActiveLabel.setText(" ");
		irs1095cValidatorCoreBODateLabel.setText(" ");
		irs1095cValidatorCoreLastUpdatedLabel.setText(" ");
		irs1095cConverterCoreIdLabel.setText(" ");
		irs1095cConverterCoreActiveLabel.setText(" ");
		irs1095cConverterCoreBODateLabel.setText(" ");
		irs1095cConverterCoreLastUpdatedLabel.setText(" ");
		irs1095cFinalizerCoreIdLabel.setText(" ");
		irs1095cFinalizerCoreActiveLabel.setText(" ");
		irs1095cFinalizerCoreBODateLabel.setText(" ");
		irs1095cFinalizerCoreLastUpdatedLabel.setText(" ");
		irs1095cDocumentNameLabel.setText(" ");
		irs1095cDocumentSizeLabel.setText(" ");
		irs1095cDocumentDescriptionLabel.setText(" ");
		irs1095cDocumentFilenameLabel.setText(" ");
		irs1095cDocumentTypeLabel.setText(" ");
		irs1095cAccountLabel.setText(" ");
		irs1095cEmployerLabel.setText(" ");
		irs1095cDocumentCoreIdLabel.setText(" ");
		irs1095cDocumentCoreActiveLabel.setText(" ");
		irs1095cDocumentCoreBODateLabel.setText(" ");
		irs1095cDocumentCoreLastUpdatedLabel.setText(" ");
		
		//no edit unless something is selected
		irs1095cEditButton.setDisable(true);
				
		loadBlankColumnFields();
	}
	
	private void loadSearchBox(){
		
	}

	private void initControls() {
		
		//mark the checkboxes read only
		irs1095cDynFileCreateEmployeeCheckBox.setDisable(true);
		
		//hide the sections
		irs1095cShowPipelineSpecsButton.setText("Show Pipeline Specifications");
		irs1095cPipelineSpecificationsVBox.setVisible(false);			
		irs1095cPipelineSpecificationsVBox.setMinHeight(1);
		irs1095cPipelineSpecificationsVBox.setPrefHeight(1);
		irs1095cPipelineSpecificationsVBox.setMaxHeight(1);

		irs1095cShowDynamicFileSpecsButton.setText("Show Dynamic File Specifications");
		irs1095cDynamicFileSpecificationsVBox.setVisible(false);			
		irs1095cDynamicFileSpecificationsVBox.setMinHeight(1);
		irs1095cDynamicFileSpecificationsVBox.setPrefHeight(1);
		irs1095cDynamicFileSpecificationsVBox.setMaxHeight(1);
		
		irs1095cShowDocumentButton.setText("Show Document");
		irs1095cDocumentVBox.setVisible(false);			
		irs1095cDocumentVBox.setMinHeight(1);
		irs1095cDocumentVBox.setPrefHeight(1);
		irs1095cDocumentVBox.setMaxHeight(1);
		
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
		
		irs1095cDynFileColumnFieldTable.getColumns().clear();
		irs1095cDynFileColumnFieldTable.getColumns().add(nameColumn); 
		irs1095cDynFileColumnFieldTable.getColumns().add(columnColumn);
		irs1095cDynFileColumnFieldTable.getColumns().add(columnIndexColumn);
		irs1095cDynFileColumnFieldTable.getColumns().add(parsePatternColumn);		
			
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
        irs1095cDynFileColumnFieldTable.setItems(myObservableList);
	}

	private void updateEmployeeFileDocumentData(){
		
		Document document = DataManager.i().mPipelineEmployeeFile.getPipelineInformation().getDocument();
		if (document != null) {
			Utils.updateControl(irs1095cDocumentNameLabel, document.getName());
			Utils.updateControl(irs1095cDocumentSizeLabel, document.getName());
			Utils.updateControl(irs1095cDocumentDescriptionLabel, document.getName());
			Utils.updateControl(irs1095cDocumentFilenameLabel, document.getName());
		
			//Utils.updateControl(irs1095cDocumentValue0Label, document.getValue0());
			//Utils.updateControl(irs1095cDocumentValue1Label, document.getValue1());
			//Utils.updateControl(irs1095cDocumentValue2Label, document.getValue2());
			//Utils.updateControl(irs1095cDocumentValue3Label, document.getValue3());
			//Utils.updateControl(irs1095cDocumentValue4Label, document.getValue4());
			//Utils.updateControl(irs1095cDocumentValue5Label, String.valueOf(document.getValue5()));
			//Utils.updateControl(irs1095cDocumentValue6Label, String.valueOf(document.getValue6()));
			//Utils.updateControl(irs1095cDocumentValue7Label, String.valueOf(document.getValue7()));
			//Utils.updateControl(irs1095cDocumentValue8Label, String.valueOf(document.getValue8()));
			//Utils.updateControl(irs1095cDocumentValue9Label, String.valueOf(document.getValue9()));
			//Utils.updateControl(irs1095cDocumentSdxIdLabel, String.valueOf(document.getSdxId()));
			Utils.updateControl(irs1095cDocumentTypeLabel, document.getDocumentType().toString());
			
			Utils.updateControl(irs1095cDocumentCoreIdLabel,String.valueOf(document.getId()));
			Utils.updateControl(irs1095cDocumentCoreActiveLabel,String.valueOf(document.isActive()));
			Utils.updateControl(irs1095cDocumentCoreBODateLabel,document.getBornOn());
			Utils.updateControl(irs1095cDocumentCoreLastUpdatedLabel,document.getLastUpdated());
		}

	}

	private void updateEmployeeFileDynamicFileData() {
		
		//need to load the dynamic file specification
		DynamicEmployeeFileSpecification fSpec = DataManager.i().mDynamicEmployeeFileSpecification;
		
//		if (fSpec != null) {
			Utils.updateControl(irs1095cDynFileDescriptionLabel, fSpec.getName());
			Utils.updateControl(irs1095cDynFileTabIndexLabel, fSpec.getTabIndex());
			Utils.updateControl(irs1095cDynFileHeaderRowIndexLabel, fSpec.getHeaderRowIndex());
			Utils.updateControl(irs1095cDynFileCreateEmployeeCheckBox, fSpec.isCreateEmployee());
			
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
	        irs1095cDynFileColumnFieldTable.setItems(myObservableList);
//		}
	}
	
	private void UpdateEmployeeFilePipelineSpecifications(){

		PipelineSpecification pipelineSpecification = DataManager.i().mPipelineEmployeeFile.getSpecification();
		if (pipelineSpecification != null) {
			Utils.updateControl(irs1095cPipelineNameLabel,pipelineSpecification.getName());
			Utils.updateControl(irs1095cPipelineDescriptionLabel,pipelineSpecification.getDescription());

			Utils.updateControl(irs1095cSpecificationCoreIdLabel,String.valueOf(pipelineSpecification.getId()));
			Utils.updateControl(irs1095cSpecificationCoreActiveLabel,String.valueOf(pipelineSpecification.isActive()));
			Utils.updateControl(irs1095cSpecificationCoreBODateLabel,pipelineSpecification.getBornOn());
			Utils.updateControl(irs1095cSpecificationCoreLastUpdatedLabel,pipelineSpecification.getLastUpdated());
									
			PipelineChannel pipelineChannel = pipelineSpecification.getChannel();
			if (pipelineChannel!= null){
				Utils.updateControl(irs1095cChannelNameLabel,pipelineChannel.getName());
				Utils.updateControl(irs1095cChannelDescriptionLabel,pipelineChannel.getName());
			
				Utils.updateControl(irs1095cChannelCoreIdLabel,String.valueOf(pipelineChannel.getId()));
				Utils.updateControl(irs1095cChannelCoreActiveLabel,String.valueOf(pipelineChannel.isActive()));
				Utils.updateControl(irs1095cChannelCoreBODateLabel,pipelineChannel.getBornOn());
				Utils.updateControl(irs1095cChannelCoreLastUpdatedLabel,pipelineChannel.getLastUpdated());
			}
			
			PipelineFileStepHandler pipelineParser = pipelineSpecification.getParser();
			if (pipelineParser!= null){
				Utils.updateControl(irs1095cParserDescriptionLabel,pipelineParser.getName());
				Utils.updateControl(irs1095cParserPipelineStepTypeLabel,pipelineParser.getType().toString());
				Utils.updateControl(irs1095cParserInterfaceClassLabel,pipelineParser.getInterfaceClass());
				Utils.updateControl(irs1095cParserHandlerClassLabel,pipelineParser.getHandlerClass());
				
				Utils.updateControl(irs1095cParserCoreIdLabel,String.valueOf(pipelineParser.getId()));
				Utils.updateControl(irs1095cParserCoreActiveLabel,String.valueOf(pipelineParser.isActive()));
				Utils.updateControl(irs1095cParserCoreBODateLabel,pipelineParser.getBornOn());
				Utils.updateControl(irs1095cParserCoreLastUpdatedLabel,pipelineParser.getLastUpdated());
			}
			
			PipelineFileStepHandler pipelineValidator = pipelineSpecification.getValidator();
			if (pipelineValidator!= null){
				Utils.updateControl(irs1095cValidatorDescriptionLabel,pipelineValidator.getName());
				Utils.updateControl(irs1095cValidatorPipelineStepTypeLabel,pipelineValidator.getType().toString());
				Utils.updateControl(irs1095cValidatorInterfaceClassLabel,pipelineValidator.getInterfaceClass());
				Utils.updateControl(irs1095cValidatorHandlerClassLabel,pipelineValidator.getHandlerClass());
				
				Utils.updateControl(irs1095cValidatorCoreIdLabel,String.valueOf(pipelineValidator.getId()));
				Utils.updateControl(irs1095cValidatorCoreActiveLabel,String.valueOf(pipelineValidator.isActive()));
				Utils.updateControl(irs1095cValidatorCoreBODateLabel,pipelineValidator.getBornOn());
				Utils.updateControl(irs1095cValidatorCoreLastUpdatedLabel,pipelineValidator.getLastUpdated());
			}
			
			PipelineFileStepHandler pipelineConverter = pipelineSpecification.getConverter();
			if (pipelineValidator!= null){
				Utils.updateControl(irs1095cConverterDescriptionLabel,pipelineConverter.getName());
				Utils.updateControl(irs1095cConverterPipelineStepTypeLabel,pipelineConverter.getType().toString());
				Utils.updateControl(irs1095cConverterInterfaceClassLabel,pipelineConverter.getInterfaceClass());
				Utils.updateControl(irs1095cConverterHandlerClassLabel,pipelineConverter.getHandlerClass());
				
				Utils.updateControl(irs1095cConverterCoreIdLabel,String.valueOf(pipelineConverter.getId()));
				Utils.updateControl(irs1095cConverterCoreActiveLabel,String.valueOf(pipelineConverter.isActive()));
				Utils.updateControl(irs1095cConverterCoreBODateLabel,pipelineConverter.getBornOn());
				Utils.updateControl(irs1095cConverterCoreLastUpdatedLabel,pipelineConverter.getLastUpdated());
			}
			
			PipelineFileStepHandler pipelineFinalizer = pipelineSpecification.getFinalizer();
			if (pipelineFinalizer!= null){
				Utils.updateControl(irs1095cFinalizerDescriptionLabel,pipelineFinalizer.getName());
				Utils.updateControl(irs1095cFinalizerPipelineStepTypeLabel,pipelineFinalizer.getType().toString());
				Utils.updateControl(irs1095cFinalizerInterfaceClassLabel,pipelineFinalizer.getInterfaceClass());
				Utils.updateControl(irs1095cFinalizerHandlerClassLabel,pipelineFinalizer.getHandlerClass());
				
				Utils.updateControl(irs1095cFinalizerCoreIdLabel,String.valueOf(pipelineFinalizer.getId()));
				Utils.updateControl(irs1095cFinalizerCoreActiveLabel,String.valueOf(pipelineFinalizer.isActive()));
				Utils.updateControl(irs1095cFinalizerCoreBODateLabel,pipelineFinalizer.getBornOn());
				Utils.updateControl(irs1095cFinalizerCoreLastUpdatedLabel,pipelineFinalizer.getLastUpdated());
			}
			
			PipelineFileStepHandler pipelineInitializer = pipelineSpecification.getInitializer();
			if (pipelineFinalizer!= null){
				Utils.updateControl(irs1095cInitializerDescriptionLabel,pipelineInitializer.getName());
				Utils.updateControl(irs1095cInitializerPipelineStepTypeLabel,pipelineInitializer.getType().toString());
				Utils.updateControl(irs1095cInitializerInterfaceClassLabel,pipelineInitializer.getInterfaceClass());
				Utils.updateControl(irs1095cInitializerHandlerClassLabel,pipelineInitializer.getHandlerClass());
				
				Utils.updateControl(irs1095cInitializerCoreIdLabel,String.valueOf(pipelineInitializer.getId()));
				Utils.updateControl(irs1095cInitializerCoreActiveLabel,String.valueOf(pipelineInitializer.isActive()));
				Utils.updateControl(irs1095cInitializerCoreBODateLabel,pipelineInitializer.getBornOn());
				Utils.updateControl(irs1095cInitializerCoreLastUpdatedLabel,pipelineInitializer.getLastUpdated());
			}
		}
	}
	
	private void updateEmployeeFileData(){
		
		if (DataManager.i().mPipelineEmployeeFile != null) {
			//irs1095cNameLabel.setText(DataManager.i().mPipelineEmployeeFile.);
		//	Utils.updateControl(irs1095cPlansCreatedLabel,DataManager.i().mPipelineEmployeeFile.getEmployeesCreated());
		//	irs1095cPlansUpdatedLabel.setText(String.valueOf(DataManager.i().mPipelineEmployeeFile.getEmployeesUpdated()));
			irs1095cNoChangeCountLabel.setText(String.valueOf(DataManager.i().mPipelineEmployeeFile.getNoChangeCount()));

			//coredata
			Utils.updateControl(irs1095cCoreIdLabel,String.valueOf(DataManager.i().mPipelineEmployeeFile.getId()));
			Utils.updateControl(irs1095cCoreActiveLabel,String.valueOf(DataManager.i().mPipelineEmployeeFile.isActive()));
			Utils.updateControl(irs1095cCoreBODateLabel,DataManager.i().mPipelineEmployeeFile.getBornOn());
			Utils.updateControl(irs1095cCoreLastUpdatedLabel,DataManager.i().mPipelineEmployeeFile.getLastUpdated());
			
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
		        irs1095cPipelineNoticesListView.setItems(myObservableList);		
				
		        irs1095cPipelineNoticesListLabel.setText("Employee Files (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getNotices()) + ")" );
			} else {
				irs1095cPipelineNoticesListLabel.setText("Employee Files (total: 0)");
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
		        irs1095cRecordRejectionsListView.setItems(myObservableList);		
				
		        irs1095cRecordRejectionsListLabel.setText("Record Rejections (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getRecordRejections()) + ")" );
			} else {
				irs1095cRecordRejectionsListLabel.setText("Record Rejections (total: 0)");
			}
	}
	
	private void loadRawIRS1095C() {
		
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
		        irs1095cRawIRS1095CListView.setItems(myObservableList);		
				
		        irs1095cRawIRS1095CListLabel.setText("Record Rejections (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getRawEmployees().size()) + ")" );
			} else {
				irs1095cRawIRS1095CListLabel.setText("Record Rejections (total: 0)");
			}
	}
	
	private void loadRawCoveredSecondary() {
		
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
		        irs1095cRawCoveredSecondaryListView.setItems(myObservableList);		
				
		        irs1095cRawCoveredSecondaryListLabel.setText("Record Rejections (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getRawEmployees().size()) + ")" );
			} else {
				irs1095cRawCoveredSecondaryListLabel.setText("Record Rejections (total: 0)");
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
        irs1095cSearchComboBox.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> {
            final javafx.scene.control.TextField editor = irs1095cSearchComboBox.getEditor();
            final String selected = irs1095cSearchComboBox.getSelectionModel().getSelectedItem();
            
            irs1095cSearchComboBox.show();
            irs1095cSearchComboBox.setVisibleRowCount(10);

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
        irs1095cSearchComboBox.setItems(filteredItems);	
		
		//close the dropdown if it is showing
        irs1095cSearchComboBox.hide();
	}
	
	public void onSearchFiles(ActionEvent event) {
		searchFiles();
	}
	
	public void searchFiles() {
		if (DataManager.i().mPipelineEmployeeFiles == null) return;
		
		String sSelection = irs1095cSearchComboBox.getValue();
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
            			 // DataManager.i().setSecondary(Integer.parseInt(btnView.getId()));
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
		EtcAdmin.i().setScreen(ScreenType.PIPELINEIRS1095CFILEEDIT, true);
	}	
	
	@FXML
	private void onShowPipelineSpecs(ActionEvent event) {
		if (irs1095cShowPipelineSpecsButton.getText().contains("Show")) {
			irs1095cShowPipelineSpecsButton.setText("Hide Pipeline Specifications");
			irs1095cPipelineSpecificationsVBox.setVisible(true);
			irs1095cPipelineSpecificationsVBox.setMinHeight(VBox.USE_COMPUTED_SIZE);
			irs1095cPipelineSpecificationsVBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
			irs1095cPipelineSpecificationsVBox.setMaxHeight(VBox.USE_COMPUTED_SIZE);
		} else {
			irs1095cShowPipelineSpecsButton.setText("Show Pipeline Specifications");
			irs1095cPipelineSpecificationsVBox.setVisible(false);			
			irs1095cPipelineSpecificationsVBox.setMinHeight(1);
			irs1095cPipelineSpecificationsVBox.setPrefHeight(1);
			irs1095cPipelineSpecificationsVBox.setMaxHeight(1);
		}
	}	

	@FXML
	private void onShowDynamicFileSpecs(ActionEvent event) {
		if (irs1095cShowDynamicFileSpecsButton.getText().contains("Show")) {
			irs1095cShowDynamicFileSpecsButton.setText("Hide Dynamic File Specifications");
			irs1095cDynamicFileSpecificationsVBox.setVisible(true);
			irs1095cDynamicFileSpecificationsVBox.setMinHeight(VBox.USE_COMPUTED_SIZE);
			irs1095cDynamicFileSpecificationsVBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
			irs1095cDynamicFileSpecificationsVBox.setMaxHeight(VBox.USE_COMPUTED_SIZE);
		} else {
			irs1095cShowDynamicFileSpecsButton.setText("Show Dynamic File Specifications");
			irs1095cDynamicFileSpecificationsVBox.setVisible(false);			
			irs1095cDynamicFileSpecificationsVBox.setMinHeight(1);
			irs1095cDynamicFileSpecificationsVBox.setPrefHeight(1);
			irs1095cDynamicFileSpecificationsVBox.setMaxHeight(1);
		}
	}	

	@FXML
	private void onShowDocument(ActionEvent event) {
		if (irs1095cShowDocumentButton.getText().contains("Show")) {
			irs1095cShowDocumentButton.setText("Hide Document");
			irs1095cDocumentVBox.setVisible(true);
			irs1095cDocumentVBox.setMinHeight(VBox.USE_COMPUTED_SIZE);
			irs1095cDocumentVBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
			irs1095cDocumentVBox.setMaxHeight(VBox.USE_COMPUTED_SIZE);
		} else {
			irs1095cShowDocumentButton.setText("Show Document");
			irs1095cDocumentVBox.setVisible(false);			
			irs1095cDocumentVBox.setMinHeight(1);
			irs1095cDocumentVBox.setPrefHeight(1);
			irs1095cDocumentVBox.setMaxHeight(1);
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


