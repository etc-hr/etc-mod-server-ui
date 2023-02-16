package com.etc.admin.ui.pipeline.filespecification;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.PipelineParseDateFormat;
import com.etc.corvetto.ems.pipeline.entities.PipelineParsePattern;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileCoverageGroupReference;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileGenderReference;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileCoverageGroupReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileDepartmentReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileGenderReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileLocationReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFilePayCodeReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileAdditionalHoursSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileCoverageGroupReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileDepartmentReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileLocationReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayClassReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayCodeReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayFrequencyReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayPeriodRule;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileUnionTypeReference;
import com.etc.corvetto.ems.pipeline.entities.ppd.DynamicPayPeriodFileSpecification;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationAdditionalHoursController;
import com.etc.admin.ui.pipeline.rowignore.ViewRowIgnoreController;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;

public class ViewDynamicFileSpecificationController {
	
	@FXML
	private ListView<HBoxDetailCell> dfcovDataListView;
	@FXML
	private ListView<HBoxDetailCell> dfcovDataList2View;
	@FXML
	private ListView<HBoxAdditionalHourCell> dfcovAdditionalHoursListView;
	@FXML
	private ListView<HBoxIgnoreRowCell> dfcovIgnoreRowSpecsListView;
	@FXML
	private ListView<HBoxRuleCell> dfcovPayPeriodRulesListView;
	@FXML
	private TextField dfcovNameLabel;
	@FXML
	private TextField dfcovDescriptionLabel;
	@FXML
	private TextField dfcovTabIndexLabel;
	@FXML
	private TextField dfcovHeaderRowIndexLabel;
	@FXML
	private CheckBox dfcovMapEEtoERCheck;
	@FXML
	private CheckBox dfcovCreateEmployeeCheck;
	@FXML
	private CheckBox dfcovCreateSecondaryCheck;
	@FXML
	private CheckBox dfcovCreatePlanCheck;
	@FXML
	private CheckBox dfcovSkipLastRowCheck;
	@FXML
	private Button dfcovEditButton;
	@FXML
	private Button dfcovClearFormButton;
	@FXML
	private Button dfcovSaveChangesButton;
	@FXML
	private Button dfcovSavingChangesButton;
	@FXML
	private Button dfcovAddIgnoreSpecButton;
	@FXML
	private Button dfcovPayPeriodRulesButton;
	@FXML
	private Button dfcovPayAdditionalHoursButton;
	@FXML
	private Label dfcovName;
	@FXML
	private Label dfcovCoreIdLabel;
	@FXML
	private Label dfcovCoreActiveLabel;
	@FXML
	private Label dfcovCoreBODateLabel;
	@FXML
	private Label dfcovCoreLastUpdatedLabel;
	@FXML
	private VBox dfcovVBox;

	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
    List<HBoxRuleCell> ruleList = new ArrayList<>();

	SpreadsheetView spv = null;
	// using an array of strings to hold and sort our column field data
	String[] columnFields = new String[100];
	int[] columnCount = new int[100];
	
	// read only mode for selected mapper files
	public boolean readOnly = false;
	public PipelineFileType fileType;
	
	ObservableList<String> patternList;
	ObservableList<String> formatList;
	
	//trackers for reference items
	public List<DynamicPayFilePayClassReference> selectedPayClassRefs = new ArrayList<DynamicPayFilePayClassReference>();
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		// reset the buttons
		dfcovSavingChangesButton.setVisible(false);
		dfcovSaveChangesButton.setVisible(true);

		// set the name at the top of the screen
		if (DataManager.i().mPipelineSpecification != null)
			dfcovName.setText("Mapper for " 
							  + DataManager.i().mPipelineSpecification.getName() 
							  + ",  Id: "  + String.valueOf(DataManager.i().mPipelineSpecification.getId()));
		// set the file type
		fileType = DataManager.i().mPipelineChannel.getType();
		initControls();
		loadMapperData();
	}
	
	private void loadMapperData() {
		// clear any stale data since the dialog stays alive
		clearData();
		
		EtcAdmin.i().setStatusMessage("loading Mapper...");
		EtcAdmin.i().setProgress(.5);
		
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
        		DataManager.i().loadDynamicFileSpecification(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
        		EtcAdmin.i().setProgress(.75);
                return null;
            }
        };
        
    	task.setOnSucceeded(e ->   updateFileData());
    	task.setOnFailed(e ->   updateFileData());
        new Thread(task).start();	
	}

	private void initControls() 
	{
		//disable the check boxes
		dfcovCreateEmployeeCheck.setDisable(false);		
		dfcovCreateSecondaryCheck.setDisable(false);
	
		//enable the addspec button only for the applicable types
		dfcovPayAdditionalHoursButton.setVisible(false);
		switch(fileType){
		case COVERAGE:
		case EMPLOYEE:
		case PAY:
			dfcovPayAdditionalHoursButton.setVisible(true);
			dfcovAddIgnoreSpecButton.setDisable(false);
			break;
		default:
			dfcovAddIgnoreSpecButton.setDisable(true);
			break;
		}	
		
		// IGNOREROWSPECS
		dfcovIgnoreRowSpecsListView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
        		switch(fileType){
        		case COVERAGE:
                	DataManager.i().mDynamicCoverageFileIgnoreRowSpecification = dfcovIgnoreRowSpecsListView.getSelectionModel().getSelectedItem().getCovSpec();
        		case EMPLOYEE:
                	DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification = dfcovIgnoreRowSpecsListView.getSelectionModel().getSelectedItem().getEmpSpec();
        		case PAY:
                	DataManager.i().mDynamicPayFileIgnoreRowSpecification = dfcovIgnoreRowSpecsListView.getSelectionModel().getSelectedItem().getPaySpec();
        			break;
        		default:
        			return;
        		}	
            	
        		viewIgnoreRow();
            }
        });

		// PAYPERIODRULES
		dfcovPayPeriodRulesListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
            	DataManager.i().mDynamicPayFilePayPeriodRule =  dfcovPayPeriodRulesListView.getSelectionModel().getSelectedItem().getRule();
        		viewPayPeriodRules();
            }
        });

		// ADDITIONALHOURS
		dfcovAdditionalHoursListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
              	DataManager.i().mDynamicPayFileAdditionalHoursSpecification =  dfcovAdditionalHoursListView.getSelectionModel().getSelectedItem().getSpec();
                viewAdditionalHours();
            }
        });
		
		// set up a parse pattern collection for comboboxes
        patternList = FXCollections.observableArrayList();
        formatList = FXCollections.observableArrayList();
        
        //clear everything
        dfcovDataListView.getItems().clear();
        dfcovDataList2View.getItems().clear();
 	}	
	
	private void clearData() {
		dfcovDataListView.getItems().clear();
		dfcovDataList2View.getItems().clear();
	    if (spv != null)
	    	dfcovVBox.getChildren().remove(spv);
	    dfcovPayPeriodRulesListView.getItems().clear();
	    dfcovIgnoreRowSpecsListView.getItems().clear();
	    dfcovAdditionalHoursListView.getItems().clear();
	}
	
	private void checkReadOnly() {
		dfcovNameLabel.setEditable(!readOnly);
		dfcovDescriptionLabel.setEditable(!readOnly);
		dfcovTabIndexLabel.setEditable(!readOnly);
		dfcovHeaderRowIndexLabel.setEditable(!readOnly);
		dfcovCreateEmployeeCheck.setDisable(readOnly);
		dfcovCreateSecondaryCheck.setDisable(readOnly);
		dfcovCreateSecondaryCheck.setDisable(readOnly);
		dfcovCreateSecondaryCheck.setDisable(readOnly);
		dfcovMapEEtoERCheck.setDisable(readOnly);
		dfcovCreateSecondaryCheck.setDisable(readOnly);
		dfcovCreateSecondaryCheck.setDisable(readOnly);
		dfcovCreateSecondaryCheck.setDisable(readOnly);
		dfcovCreatePlanCheck.setDisable(readOnly);
		dfcovCreateSecondaryCheck.setDisable(readOnly);
		dfcovClearFormButton.setDisable(readOnly);
		dfcovSaveChangesButton.setDisable(readOnly);
	}
	
	// update our manufactured file layout at the top of the screen
	private void updateFileLayoutDisplay() {

		ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
		final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();	
	
		Arrays.fill(columnFields,"");
		Arrays.fill(columnCount, 0);
		
		// cycle through the lists, looking for an assigned column
		int colIndex = 0;
		// list 1
		int maxEntries = 0;
		int lastPosition = 0;
		for (HBoxDetailCell cell : dfcovDataListView.getItems()) {
			if (cell.getColumn() == true) {
				colIndex = cell.getColumnIndex();
				//check for garbage
				if (colIndex > 99) colIndex = 99;
				if (lastPosition < colIndex) lastPosition = colIndex;
				//manually count entries since the spreadsheet cell size has an update bug
				columnCount[colIndex]++;
				if (columnCount[colIndex] > maxEntries) maxEntries = columnCount[colIndex];
				// append as required
				if (columnFields[colIndex].length() > 0) {
					String newVal = columnFields[colIndex] + "\r" + cell.getName();
					columnFields[colIndex] = newVal;
				}
				else
					columnFields[colIndex] = cell.getName();
				if (cell.getPatternName() != "") columnFields[colIndex]  += (" (" + cell.getPatternValue() + ")");
				if (cell.getFormatName() != "") {
					// this could have both a pattern and a format
					if (cell.getPatternName() != "") columnFields[colIndex]  += ", ";
					columnFields[colIndex]  += (" (" + cell.getFormatName() + ")");
				}
			}
		}
		
		// List 2
		for (HBoxDetailCell cell : dfcovDataList2View.getItems()) {
			if (cell.getColumn() == true) {
				colIndex = cell.getColumnIndex();
				if (lastPosition < colIndex) lastPosition = colIndex;
				//manually count entries since the spreadsheet cell size has an update bug
				columnCount[colIndex]++;
				if (columnCount[colIndex] > maxEntries) maxEntries = columnCount[colIndex];
				// append as required
				if (columnFields[colIndex].length() > 0) {
					String newVal = columnFields[colIndex] + "\r" + cell.getName();
					columnFields[colIndex] = newVal;
				}
				else
					columnFields[colIndex] = cell.getName();
				if (cell.getPatternName() != "") columnFields[colIndex]  += (" (" + cell.getPatternValue() + ")");
				if (cell.getFormatName() != "") {
					// this could have both a pattern and a format
					if (cell.getPatternName() != "") columnFields[colIndex]  += ", ";
					columnFields[colIndex]  += (" (" + cell.getFormatName() + ")");
				}
			}
		}
		
		// add in the pay additional hours
		if (fileType == PipelineFileType.PAY) {
			if (DataManager.i().mDynamicPayFileAdditionalHoursSpecifications != null && DataManager.i().mDynamicPayFileAdditionalHoursSpecifications.size() > 0) {
				for (DynamicPayFileAdditionalHoursSpecification spec : DataManager.i().mDynamicPayFileAdditionalHoursSpecifications) {
					int position = spec.getColIndex();
					columnFields[position] = "Additional Hour";
					if (position > lastPosition) lastPosition = position;
				}
			}
		}
		
		// now go through the column data, creating spreadsheet cells
		//colIndex = 0;
		for (int i = 0; i< lastPosition + 1; i++) {
			//String cellData = "";
			list.add(getCell(1,i,columnFields[i], true));
			//colIndex++;
		}
		
		// add a space cell because spreadsheet sometimes doesn't scroll properly
		list.add(getCell(1,list.size(),"                         ", false));
		//add the rows
		rows.add(list);
		GridBase grid = new GridBase(2, list.size());
		grid.setRows(rows);
		
		//add to the scene
	    if (spv != null)
	    	dfcovVBox.getChildren().remove(spv);
	    spv = new SpreadsheetView(grid);
	    dfcovVBox.getChildren().add(spv);
	    spv.setPrefWidth(500);
	    spv.setMaxWidth(500);
	    
	    // set the column headers
	    char c = 'A';
	    char c1 = 'A';
	    int letterPos = 0;
	    String letters = "";
	    for (int i = 0; i<list.size() - 1; i++) {
	    	if (i < 26)
	    		letters =  String.valueOf(c);
	    	else {
	    		letterPos = i/26;
	    		c1 += (letterPos - 1); // starting with A
	    		letters = String.valueOf(c1) + String.valueOf(c);
	    		c1 = 'A';
	    	}
	    	spv.getGrid().getColumnHeaders().add(i, letters + String.valueOf(i));
	    	c++;
	    	if (c> 'Z') c = 'A';
	    }
	    
	    //last column header
	    spv.getGrid().getColumnHeaders().add(list.size()-1, "");
	    
	    spv.getGrid().getRowHeaders().add(0, "Fields");
	    spv.setRowHeaderWidth(75);
	    //spv.setMouseTransparent(true);
	    //dfcovVBox.setFillWidth(true);

	    // use our calculated height
	    dfcovVBox.setMinHeight(70 + (maxEntries * 24));
	    dfcovVBox.setPrefHeight(70 + (maxEntries * 24));
	    dfcovVBox.setMaxHeight(70 + (maxEntries * 24));

	    grid.setRowHeightCallback(new GridBase.MapBasedRowHeightFactory(generateRowHeight(maxEntries * 24.0)));
	    
	    //color it
	    int color;
	    for (int i = 0; i < spv.getGrid().getRows().get(0).size(); i++) {
	    	if (spv.getGrid().getRows().get(0).get(i).getText().trim().length() < 1) continue;
	    	color = i + 1;
	    	if (color > 30) color -= 30;
	    	if (color > 30) color -= 30;
	    	if (color > 30) color -= 30;
	    	spv.getGrid().getRows().get(0).get(i).getStyleClass().add("BackColor" + color);
	    }
	    // need to cleanup later 
	    Platform.runLater(() -> updateSPV());
	    Platform.runLater(() -> updateSPV());
	}
	
    private Map<Integer, Double> generateRowHeight(double height) {
        Map<Integer, Double> rowHeight = new HashMap<>();
        rowHeight.put(0, height);
        return rowHeight;
    }	
    //Ignore for now 10/29/2020
	private void updateSPV() {
	    spv.setPrefWidth(Control.USE_COMPUTED_SIZE);
	    spv.setMaxWidth(Control.USE_COMPUTED_SIZE);
	}
	
	// String cell
	private SpreadsheetCell getCell(int row, int column, String data, boolean editable) {
		if (data == null) data = "";
		if (data.length() < 1) data = "          ";
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, data);
		newCell.setEditable(false);
		if (data.trim().contentEquals(""))
			newCell.getStyleClass().add("DynFileSpec" + row);
		else
			newCell.getStyleClass().add("DynFileSpecMarked" + row);			
		// "Additional hours Styling
		if (data.trim().contentEquals("Additional Hour"))
			newCell.getStyleClass().add("AdditionalHoursCell");			
		return newCell;
	}
	//Probably going away 10/29/2020
	private void setSpecListSize() {
		int nListViewSize = (dfcovDataListView.getItems().size() +1) * 40;
		int nListViewSize2 = (dfcovDataList2View.getItems().size() +1) * 40;
		int viewSize = nListViewSize;
		if (nListViewSize2 > viewSize) viewSize = nListViewSize2;
		
		dfcovDataListView.setPrefHeight(viewSize);
		dfcovDataListView.setMinHeight(viewSize);
		dfcovDataListView.setMaxHeight(viewSize);		
		
		dfcovDataList2View.setPrefHeight(viewSize);
		dfcovDataList2View.setMinHeight(viewSize);
		dfcovDataList2View.setMaxHeight(viewSize);		
	}

	private void loadAdditionalHours() {
		dfcovAdditionalHoursListView.getItems().clear();;
		if (DataManager.i().mDynamicPayFileAdditionalHoursSpecifications != null && DataManager.i().mDynamicPayFileAdditionalHoursSpecifications.size() > 0)
		{
		    List<HBoxAdditionalHourCell> hoursList = new ArrayList<HBoxAdditionalHourCell>();
			for (DynamicPayFileAdditionalHoursSpecification spec : DataManager.i().mDynamicPayFileAdditionalHoursSpecifications) {
				if (spec == null) continue;
				hoursList.add(new HBoxAdditionalHourCell(spec));
		    };	
			
			ObservableList<HBoxAdditionalHourCell> myObservableHoursList = FXCollections.observableList(hoursList);
			dfcovAdditionalHoursListView.setItems(myObservableHoursList);		
		} 
	}
	
	private void loadRuleData() 
	{
		EtcAdmin.i().setStatusMessage("loading Rule Data...");
		EtcAdmin.i().setProgress(.01);
		
		dfcovPayPeriodRulesListView.getItems().clear();
		ruleList.clear();

		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
        		DynamicPayFileSpecification fSpec = DataManager.i().mDynamicPayFileSpecification;

        		if (fSpec != null && fSpec.getPayPeriodRules() != null && fSpec.getPayPeriodRules().size() > 0)
        		{
        		    double counter = 0f;
        		    double count = fSpec.getPayPeriodRules().size();
        			for (DynamicPayFilePayPeriodRule rule: fSpec.getPayPeriodRules()) {
        				if (rule == null) continue;
        				// make sure it is current
        				rule = DataManager.i().getDynamicPayFilePayPeriodRule(rule);
        				// and add it
        				ruleList.add(new HBoxRuleCell(rule));
        				// update the progress indicator
        				counter++;
        				EtcAdmin.i().setProgress(counter/count);
        			};	
        		}
                return null;
            }
        };

    	task.setOnSucceeded(e -> loadRules());
    	task.setOnFailed(e -> loadRules());
        new Thread(task).start();
	}

	private void loadRules() {
		ObservableList<HBoxRuleCell> myObservableDepartmentList = FXCollections.observableList(ruleList);
		dfcovPayPeriodRulesListView.setItems(myObservableDepartmentList);		

		EtcAdmin.i().setStatusMessage("Ready");
		EtcAdmin.i().setProgress(0);
	}
	
	//Handles the data source selection
	private void updateFileData() {
		// lod combobox entries
		if (DataManager.i().mPipelineParsePatterns != null && patternList.size() == 0) {
		    patternList.add("");
			for (PipelineParsePattern pattern : DataManager.i().mPipelineParsePatterns)
				//patternList.add(pattern.getName());
				patternList.add(pattern.getDescription() + " - " + pattern.getId());
		  	}

		if (DataManager.i().mPipelineParseDateFormats != null && formatList.size() == 0) {
			formatList.add("");
			for (PipelineParseDateFormat format : DataManager.i().mPipelineParseDateFormats)
				formatList.add(format.getName() + " - " + format.getId());
		  	}

		switch(fileType){
		case COVERAGE:
			updateCoverageFileData();
			loadIgnoreRows(DataManager.i().mDynamicCoverageFileSpecification);
			break;
		case EMPLOYEE:
			updateEmployeeFileData();
			loadIgnoreRows(DataManager.i().mDynamicEmployeeFileSpecification);
			break;
		case PAY:
			updatePayFileData();
			loadIgnoreRows(DataManager.i().mDynamicPayFileSpecification);
			loadAdditionalHours();
			loadRuleData();
			break;
		case PAYPERIOD:
			updatePayPeriodFileData();
			break;
		case IRS1094C:
			break;
		case IRS1095C:
			break;
		case IRSAIRERR:
			break;
		case IRSAIRRCPT:
			break;
		case EVENT:
			break;
		default:
			break;
		}	
		
		// check to see if we are in readOnly mode
		checkReadOnly();
		EtcAdmin.i().setStatusMessage("Ready");
		EtcAdmin.i().setProgress(0);
	}

	/*
	 * DynamicCoverageFileSpecification Support
	 */
	private void updateCoverageFileData(){

		DynamicCoverageFileSpecification fSpec = DataManager.i().mDynamicCoverageFileSpecification;
		if (fSpec != null) {
			readOnly = fSpec.isLocked();
			Utils.updateControl(dfcovNameLabel,fSpec.getName());
			Utils.updateControl(dfcovTabIndexLabel,String.valueOf(fSpec.getTabIndex()));
			Utils.updateControl(dfcovHeaderRowIndexLabel,String.valueOf(fSpec.getHeaderRowIndex()));
			Utils.updateControl(dfcovCreateEmployeeCheck,fSpec.isCreateEmployee());
			dfcovCreateSecondaryCheck.setVisible(true);
			dfcovCreateSecondaryCheck.setText("Create Secondary");
			Utils.updateControl(dfcovCreateSecondaryCheck,fSpec.isCreateDependent());
			dfcovMapEEtoERCheck.setVisible(true);
			Utils.updateControl(dfcovMapEEtoERCheck,fSpec.isMapEEtoER());
			dfcovCreateEmployeeCheck.setVisible(true);
			Utils.updateControl(dfcovCreateEmployeeCheck,fSpec.isCreateEmployee());
			dfcovCreatePlanCheck.setVisible(true);
			Utils.updateControl(dfcovCreatePlanCheck,fSpec.isCreateBenefit());
			Utils.updateControl(dfcovSkipLastRowCheck, fSpec.isSkipLastRow());
					
			//core data read only
			Utils.updateControl(dfcovCoreIdLabel,String.valueOf(fSpec.getId()));
			Utils.updateControl(dfcovCoreActiveLabel,String.valueOf(fSpec.isActive()));
			Utils.updateControl(dfcovCoreBODateLabel,fSpec.getBornOn());
			Utils.updateControl(dfcovCoreLastUpdatedLabel,fSpec.getLastUpdated());
			
			//update the column field table
			loadFields(fSpec);	 
		}
	}
	//Generate the coverage spec type
	private void loadFields(DynamicCoverageFileSpecification fSpec) {
		List<HBoxDetailCell> list = new ArrayList<>();		
		list.add(new HBoxDetailCell());
		list.add(new HBoxDetailCell("Employee Information"));
		list.add(new HBoxDetailCell("EMPLOYER ID",fSpec.isErCol(), fSpec.getErColIndex(), null, null,1, true,1,1,getCoverageFileEmployerRefCount()));
		list.add(new HBoxDetailCell("LITE ID", fSpec.isEtcRefCol(), fSpec.getEtcRefColIndex(), null, null, 1, false,1,0,0));
		list.add(new HBoxDetailCell("EMPLOYEE SSN", fSpec.isSsnCol(), fSpec.getSsnColIndex(), fSpec.getSsnParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("EMPLOYEE ID", fSpec.isErRefCol(), fSpec.getErRefColIndex(), fSpec.getErRefParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("SUBSCRBR NUMBER",fSpec.isSubcrbrCol(), fSpec.getSubcrbrColIndex(), null, null,1, false,1,0,0));
		list.add(new HBoxDetailCell("EMPLOYEE FLAG",fSpec.isEeFlagCol(), fSpec.getEeFlagColIndex(), fSpec.getEeFlagTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("FIRST NAME", fSpec.isfNameCol(), fSpec.getfNameColIndex(), fSpec.getfNameParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("MIDDLE NAME", fSpec.ismNameCol(), fSpec.getmNameColIndex(), fSpec.getmNameParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("LAST NAME", fSpec.islNameCol(), fSpec.getlNameColIndex(), fSpec.getlNameParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("PHONE NUMBER",fSpec.isPhoneCol(), fSpec.getPhoneColIndex(), null, null,1, false,1,0,0));
		list.add(new HBoxDetailCell("EMAIL COLUMN",fSpec.isEmlCol(), fSpec.getEmlColIndex(), null, null,1, false,1,0,0));

		list.add(new HBoxDetailCell("Coverage Information"));
		list.add(new HBoxDetailCell("COV WAIVED", fSpec.isWavdCol(), fSpec.getWavdColIndex(), fSpec.getWavdTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("COV INELIGIBLE",fSpec.isInelCol(),fSpec.getInelColIndex(), fSpec.getInelTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("DECISION DT",fSpec.isDecisionDtCol(), fSpec.getDecisionDtColIndex(), null, fSpec.getDecisionDtFormat(),3, false,1,0,0));
		list.add(new HBoxDetailCell("COV START DATE",fSpec.isCovStartDtCol(), fSpec.getCovStartDtColIndex(), null, fSpec.getCovStartDtFormat(),3, false,1,0,0));
		list.add(new HBoxDetailCell("COV END DATE",fSpec.isCovEndDtCol(), fSpec.getCovEndDtColIndex(), null, fSpec.getCovEndDtFormat(),3, false,1,0,0));

		list.add(new HBoxDetailCell("Coverage Selection"));
		list.add(new HBoxDetailCell("ANNUAL COVERAGE",fSpec.isTySelCol(), fSpec.getTySelColIndex(), fSpec.getTySelTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("COVERAGE JAN",fSpec.isJanSelCol(), fSpec.getJanSelColIndex(), fSpec.getJanSelTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("COVERAGE FEB",fSpec.isFebSelCol(), fSpec.getFebSelColIndex(), fSpec.getFebSelTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("COVERAGE MAR",fSpec.isMarSelCol(), fSpec.getMarSelColIndex(), fSpec.getMarSelTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("COVERAGE APR",fSpec.isAprSelCol(), fSpec.getAprSelColIndex(), fSpec.getAprSelTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("COVERAGE MAY",fSpec.isMaySelCol(), fSpec.getMaySelColIndex(), fSpec.getMaySelTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("COVERAGE JUN",fSpec.isJunSelCol(), fSpec.getJunSelColIndex(), fSpec.getJunSelTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("COVERAGE JUL",fSpec.isJulSelCol(), fSpec.getJulSelColIndex(), fSpec.getJulSelTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("COVERAGE AUG",fSpec.isAugSelCol(), fSpec.getAugSelColIndex(), fSpec.getAugSelTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("COVERAGE SEP",fSpec.isSepSelCol(), fSpec.getSepSelColIndex(), fSpec.getSepSelTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("COVERAGE OCT",fSpec.isOctSelCol(), fSpec.getOctSelColIndex(), fSpec.getOctSelTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("COVERAGE NOV",fSpec.isNovSelCol(), fSpec.getNovSelColIndex(), fSpec.getNovSelTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("COVERAGE DEC",fSpec.isDecSelCol(), fSpec.getDecSelColIndex(), fSpec.getDecSelTrueParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("COV PLAN ID",fSpec.isPlanRefCol(), fSpec.getPlanRefColIndex(), fSpec.getPlanRefParsePattern(), null,2, false,1,0,0));
		list.add(new HBoxDetailCell("COV DEDUCTION",fSpec.isDedCol(), fSpec.getDedColIndex(), null, null,1, false,1,0,0));
		list.add(new HBoxDetailCell("COV MBR SHR AMT",fSpec.isMbrShareCol(), fSpec.getMbrShareColIndex(), null, null,1, false,1,0,0));
		list.add(new HBoxDetailCell("COV TAX YEAR",fSpec.isTyCol(), fSpec.getTyColIndex(), fSpec.getTyParsePattern(), null,2, false,1,0,0));
        ObservableList<HBoxDetailCell> newObservableList = FXCollections.observableList(list);
        dfcovDataListView.setItems(newObservableList);	

		List<HBoxDetailCell> list2 = new ArrayList<>();		
		list2.add(new HBoxDetailCell());
		list2.add(new HBoxDetailCell("Address Information"));
		list2.add(new HBoxDetailCell("STRT NUMBER", fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), fSpec.getStreetParsePattern(), null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("ADD LINE 1", fSpec.isStreetCol(), fSpec.getStreetColIndex(), fSpec.getStreetParsePattern(), null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("ADD LINE 2", fSpec.isLin2Col(), fSpec.getLin2ColIndex(), null, null,1, false,1,0,0));
		list2.add(new HBoxDetailCell("CITY", fSpec.isCityCol(), fSpec.getCityColIndex(), fSpec.getCityParsePattern(), null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("STATE", fSpec.isStateCol(), fSpec.getStateColIndex(), fSpec.getStateParsePattern(), null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("ZIP", fSpec.isZipCol(), fSpec.getZipColIndex(), fSpec.getZipParsePattern(), null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("GENDER", fSpec.isGenderCol(), fSpec.getGenderColIndex(), null, null,1, true,1,2, getCoverageFileGenderTypeRefCount()));
		list2.add(new HBoxDetailCell("DOB", fSpec.isDobCol(), fSpec.getDobColIndex(), null,fSpec.getDobFormat(),3, false,1,0,0));
		list2.add(new HBoxDetailCell("HIRE DATE", fSpec.isHireDtCol(), fSpec.getHireDtColIndex(), null, fSpec.getHireDtFormat(),3, false,1,0,0));
		list2.add(new HBoxDetailCell("TERM DATE", fSpec.isTermDtCol(), fSpec.getTermDtColIndex(), null, fSpec.getTermDtFormat(),3, false,1,0,0));
		list2.add(new HBoxDetailCell("CVG CLASS", fSpec.isCvgGroupCol(), fSpec.getCvgGroupColIndex(), null, null, 1, true,1,9, getCoverageFileCvgClassRefCount()));
		list2.add(new HBoxDetailCell("JOB TITLE", fSpec.isJobTtlCol(), fSpec.getJobTtlColIndex(), null, null, 1, false,1,0,0));
		list2.add(new HBoxDetailCell("SEC LITE ID", fSpec.isDepEtcRefCol(), fSpec.getDepEtcRefColIndex(), null, null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("SEC SSN", fSpec.isDepSSNCol(), fSpec.getDepSSNColIndex(), fSpec.getDepSSNParsePattern(), null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("SEC ID", fSpec.isDepErRefCol(), fSpec.getDepErRefColIndex(), fSpec.getDepErRefParsePattern(), null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("MEMBER NUMBER",fSpec.isMbrCol(), fSpec.getMbrColIndex(), null, null,1, false,1,0,0));

		list2.add(new HBoxDetailCell("Dependent Information"));
		list2.add(new HBoxDetailCell("SEC FIRST NAME", fSpec.isDepFNameCol(), fSpec.getDepFNameColIndex(), fSpec.getDepFNameParsePattern(), null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("SEC MIDDLE NAME", fSpec.isDepMNameCol(), fSpec.getDepMNameColIndex(), fSpec.getDepMNameParsePattern(), null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("SEC LAST NAME", fSpec.isDepLNameCol(), fSpec.getDepLNameColIndex(), fSpec.getDepLNameParsePattern(), null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("SEC STRT NUMBER", fSpec.isDepStreetNumCol(), fSpec.getDepStreetNumColIndex(), fSpec.getDepStreetParsePattern(), null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("SEC ADD LINE 1", fSpec.isDepStreetCol(), fSpec.getDepStreetColIndex(), fSpec.getDepStreetParsePattern(), null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("SEC ADD LINE 2", fSpec.isDepLin2Col(), fSpec.getDepLin2ColIndex(), null, null,1, false,1,0,0));
		list2.add(new HBoxDetailCell("SEC CITY", fSpec.isDepCityCol(), fSpec.getDepCityColIndex(), fSpec.getDepCityParsePattern(), null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("SEC STATE", fSpec.isDepStateCol(), fSpec.getDepStateColIndex(), fSpec.getDepStateParsePattern(), null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("SEC ZIP", fSpec.isDepZipCol(), fSpec.getDepZipColIndex(), fSpec.getDepZipParsePattern(), null,2, false,1,0,0));
		list2.add(new HBoxDetailCell("SEC DOB", fSpec.isDepDOBCol(), fSpec.getDepDOBColIndex(), null, fSpec.getDepDOBFormat(),3, false,1,0,0));

		list2.add(new HBoxDetailCell("Custom Fields"));
		list2.add(new HBoxDetailCell(fSpec.getCfld1Name(),fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), fSpec.getCfld1ParsePattern(), null,5, false,1,0,0));
		list2.add(new HBoxDetailCell(fSpec.getCfld2Name(),fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), fSpec.getCfld2ParsePattern(), null,5, false,1,0,0));
		list2.add(new HBoxDetailCell(fSpec.getCfld3Name(),fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), null, null,6, false,1,0,0));
		list2.add(new HBoxDetailCell(fSpec.getCfld4Name(),fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), null, null,6, false,1,0,0));
		list2.add(new HBoxDetailCell(fSpec.getCfld5Name(),fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), null, null,6, false,1,0,0));

		list2.add(new HBoxDetailCell("Depondary Custom Fields"));
		list2.add(new HBoxDetailCell(fSpec.getDepCfld1Name(),fSpec.isDepCfld1Col(), fSpec.getDepCfld1ColIndex(), fSpec.getDepCfld1ParsePattern(), null,5, false,1,0,0));
		list2.add(new HBoxDetailCell(fSpec.getDepCfld2Name(),fSpec.isDepCfld2Col(), fSpec.getDepCfld2ColIndex(), fSpec.getDepCfld2ParsePattern(), null,5, false,1,0,0));
		list2.add(new HBoxDetailCell(fSpec.getDepCfld3Name(),fSpec.isDepCfld3Col(), fSpec.getDepCfld3ColIndex(), null, null,6, false,1,0,0));
		list2.add(new HBoxDetailCell(fSpec.getDepCfld4Name(),fSpec.isDepCfld4Col(), fSpec.getDepCfld4ColIndex(), null, null,6, false,1,0,0));
		list2.add(new HBoxDetailCell(fSpec.getDepCfld5Name(),fSpec.isDepCfld5Col(), fSpec.getDepCfld5ColIndex(), null, null,6, false,1,0,0));

		list2.add(new HBoxDetailCell(fSpec.getCovCfld1Name(),fSpec.isCovCfld1Col(), fSpec.getCovCfld1ColIndex(), fSpec.getCovCfld1ParsePattern(), null,5, false,1,0,0));
		list2.add(new HBoxDetailCell(fSpec.getCovCfld2Name(),fSpec.isCovCfld2Col(), fSpec.getCovCfld2ColIndex(), null, null,6, false,1,0,0));
		
        ObservableList<HBoxDetailCell> newObservableList2 = FXCollections.observableList(list2);
        dfcovDataList2View.setItems(newObservableList2);	
        
        setSpecListSize();
        updateFileLayoutDisplay();
	}	
	
	/*
	 * DynamicEmployeeFileSpecification Support
	 */
	private void updateEmployeeFileData()
	{
		DynamicEmployeeFileSpecification fSpec = DataManager.i().mDynamicEmployeeFileSpecification;
		if (fSpec != null) {
			readOnly = fSpec.isLocked();
			Utils.updateControl(dfcovNameLabel,fSpec.getName());
			//Utils.updateControl(dfcovDescriptionLabel,fSpec.getDescription());
			Utils.updateControl(dfcovTabIndexLabel,String.valueOf(fSpec.getTabIndex()));
			Utils.updateControl(dfcovHeaderRowIndexLabel,String.valueOf(fSpec.getHeaderRowIndex()));
			Utils.updateControl(dfcovCreateEmployeeCheck,fSpec.isCreateEmployee());
			dfcovMapEEtoERCheck.setVisible(false);
			dfcovCreateSecondaryCheck.setVisible(false);
			dfcovCreatePlanCheck.setVisible(false);
				
			//core data read only
			Utils.updateControl(dfcovCoreIdLabel,String.valueOf(fSpec.getId()));
			Utils.updateControl(dfcovCoreActiveLabel,String.valueOf(fSpec.isActive()));
			Utils.updateControl(dfcovCoreBODateLabel,fSpec.getBornOn());
			Utils.updateControl(dfcovCoreLastUpdatedLabel,fSpec.getLastUpdated());
			Utils.updateControl(dfcovSkipLastRowCheck, fSpec.isSkipLastRow());
			
			//update the column field table
			loadFields(fSpec);	        
		}
	}
	
	private void loadFields(DynamicEmployeeFileSpecification fSpec) 
	{
		List<HBoxDetailCell> list = new ArrayList<>();		
		list.add(new HBoxDetailCell());
		list.add(new HBoxDetailCell("Employee Information"));
		list.add(new HBoxDetailCell("EMPLOYER ID",fSpec.isErCol(), fSpec.getErColIndex(), null, null,1, true,2,1, getEmployeeFileEmployerRefCount()));
		list.add(new HBoxDetailCell("LITE ID",fSpec.isEtcRefCol(), fSpec.getEtcRefColIndex(), null, null,1, false,2,0,0));
		list.add(new HBoxDetailCell("EMPLOYEE SSN", fSpec.isSsnCol(), fSpec.getSsnColIndex(), fSpec.getSsnParsePattern(), null,2, false,2,0,0));
		list.add(new HBoxDetailCell("EMPLOYEE ID", fSpec.isErRefCol(), fSpec.getErRefColIndex(), fSpec.getErRefParsePattern(), null,2, false,2,0,0));
		list.add(new HBoxDetailCell("FIRST NAME", fSpec.isfNameCol(), fSpec.getfNameColIndex(), fSpec.getfNameParsePattern(), null,2, false,2,0,0));
		list.add(new HBoxDetailCell("MIDDLE NAME", fSpec.ismNameCol(), fSpec.getmNameColIndex(), fSpec.getmNameParsePattern(), null,2, false,2,0,0));
		list.add(new HBoxDetailCell("LAST NAME", fSpec.islNameCol(), fSpec.getlNameColIndex(), fSpec.getlNameParsePattern(), null,2, false,2,0,0));
		list.add(new HBoxDetailCell("PHONE NUMBER",fSpec.isPhoneCol(), fSpec.getPhoneColIndex(), null, null,1, false,2,0,0));
		list.add(new HBoxDetailCell("EMAIL COLUMN",fSpec.isEmlCol(), fSpec.getEmlColIndex(), null, null,1, false,2,0,0));
		list.add(new HBoxDetailCell("DEPARTMENT", fSpec.isDeptCol(), fSpec.getDeptColIndex(), null, null,1, true,2,3, getEmployeeFileDepartmentRefCount()));
		list.add(new HBoxDetailCell("LOCATION", fSpec.isLocCol(), fSpec.getLocColIndex(), null, null,1, true,2,4, getEmployeeFileLocationRefCount()));
		list.add(new HBoxDetailCell("JOB TITLE", fSpec.isJobTtlCol(), fSpec.getJobTtlColIndex(), null, null,1, false,2,0,0));
		list.add(new HBoxDetailCell("COMP TYPE", fSpec.isPayCodeCol(), fSpec.getPayCodeColIndex(), null, null,1, true,2,5,getEmployeeFilePayCodeTypeRefCount()));

		list.add(new HBoxDetailCell("Employee File Custom Fields"));
		list.add(new HBoxDetailCell(fSpec.getCfld1Name(),fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), fSpec.getCfld1ParsePattern(), null,5, false,2,0,0));
		list.add(new HBoxDetailCell(fSpec.getCfld2Name(),fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), fSpec.getCfld2ParsePattern(), null,5, false,2,0,0));
		list.add(new HBoxDetailCell(fSpec.getCfld3Name(),fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), null, null,6, false,2,0,0));
		list.add(new HBoxDetailCell(fSpec.getCfld4Name(),fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), null, null,6, false,2,0,0));
		list.add(new HBoxDetailCell(fSpec.getCfld5Name(),fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), null, null,6, false,2,0,0));
        ObservableList<HBoxDetailCell> newObservableList = FXCollections.observableList(list);
        dfcovDataListView.setItems(newObservableList);	

		List<HBoxDetailCell> list2 = new ArrayList<>();		
		list2.add(new HBoxDetailCell());
		list2.add(new HBoxDetailCell("Employee Address"));
		list2.add(new HBoxDetailCell("STRT NUMBER", fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), fSpec.getStreetParsePattern(), null,2, false,2,0,0));
		list2.add(new HBoxDetailCell("ADD LINE 1", fSpec.isStreetCol(), fSpec.getStreetColIndex(), fSpec.getStreetParsePattern(), null,2, false,2,0,0));
		list2.add(new HBoxDetailCell("ADD LINE 2", fSpec.isLin2Col(), fSpec.getLin2ColIndex(), null, null,1, false,2,0,0));
		list2.add(new HBoxDetailCell("CITY", fSpec.isCityCol(), fSpec.getCityColIndex(), fSpec.getCityParsePattern(), null,2, false,2,0,0));
		list2.add(new HBoxDetailCell("STATE", fSpec.isStateCol(), fSpec.getStateColIndex(), fSpec.getStateParsePattern(), null,2, false,2,0,0));
		list2.add(new HBoxDetailCell("ZIP", fSpec.isZipCol(), fSpec.getZipColIndex(), fSpec.getZipParsePattern(), null,2, false,2,0,0));
		list2.add(new HBoxDetailCell("GENDER", fSpec.isGenderCol(), fSpec.getGenderColIndex(), null, null,1, true,2,2,getEmployeeFileGenderTypeRefCount()));

		list2.add(new HBoxDetailCell("Employee Date Data"));
		list2.add(new HBoxDetailCell("DOB", fSpec.isDobCol(), fSpec.getDobColIndex(), null, fSpec.getDobFormat(),3, false,2,0,0));
		list2.add(new HBoxDetailCell("HIRE DATE", fSpec.isHireDtCol(), fSpec.getHireDtColIndex(), null, fSpec.getHireDtFormat(),3, false,2,0,0));
		list2.add(new HBoxDetailCell("REHIRE DATE", fSpec.isRhireDtCol(), fSpec.getRhireDtColIndex(), null, fSpec.getRhireDtFormat(),3, false,2,0,0));
		list2.add(new HBoxDetailCell("TERM DATE", fSpec.isTermDtCol(), fSpec.getTermDtColIndex(), null, fSpec.getTermDtFormat(),3, false,2,0,0));
		list2.add(new HBoxDetailCell("CVG CLASS", fSpec.isCvgGroupCol(), fSpec.getCvgGroupColIndex(), null, null, 1, true,1,9, getEmployeeFileCvgClassRefCount()));
        ObservableList<HBoxDetailCell> newObservableList2 = FXCollections.observableList(list2);
        dfcovDataList2View.setItems(newObservableList2);	

        setSpecListSize();
        updateFileLayoutDisplay();
 	}

	/*
	 * DynamicPayFileSpecification Support
	 */
	private void updatePayFileData()
	{
		DynamicPayFileSpecification fSpec = DataManager.i().mDynamicPayFileSpecification;
		if (fSpec != null) 
		{
			readOnly = fSpec.isLocked();
			Utils.updateControl(dfcovNameLabel,fSpec.getName());
			//Utils.updateControl(dfcovDescriptionLabel,fSpec.getDescription());
			Utils.updateControl(dfcovTabIndexLabel,String.valueOf(fSpec.getTabIndex()));
			Utils.updateControl(dfcovHeaderRowIndexLabel,String.valueOf(fSpec.getHeaderRowIndex()));
			Utils.updateControl(dfcovCreateEmployeeCheck,fSpec.isCreateEmployee());
			dfcovMapEEtoERCheck.setVisible(true);
			Utils.updateControl(dfcovMapEEtoERCheck,fSpec.isMapEEtoER());
			Utils.updateControl(dfcovCreateSecondaryCheck,fSpec.isCreatePayPeriod());
			dfcovCreateSecondaryCheck.setVisible(true);
			dfcovCreateSecondaryCheck.setText("Create PayPeriod");
			dfcovCreatePlanCheck.setVisible(false);
			
			//core data read only
			Utils.updateControl(dfcovCoreIdLabel,String.valueOf(fSpec.getId()));
			Utils.updateControl(dfcovCoreActiveLabel,String.valueOf(fSpec.isActive()));
			Utils.updateControl(dfcovCoreBODateLabel,fSpec.getBornOn());
			Utils.updateControl(dfcovCoreLastUpdatedLabel,fSpec.getLastUpdated());
			Utils.updateControl(dfcovSkipLastRowCheck, fSpec.isSkipLastRow());
			
			// load any additional hours
			//DataManager.i().getDynamicPayFileAdditionalHoursSpecifications(fSpec.getId());
			//update the column field table
			loadFields(fSpec);	        
		}
	}
	
	private void loadFields(DynamicPayFileSpecification fSpec) {

		List<HBoxDetailCell> list = new ArrayList<HBoxDetailCell>();	
		list.add(new HBoxDetailCell());
		list.add(new HBoxDetailCell("Employee Information"));
		list.add(new HBoxDetailCell("EMPLOYER ID",fSpec.isErCol(), fSpec.getErColIndex(), null, null,1, true,3,1, getPayFileEmployerRefCount()));
		list.add(new HBoxDetailCell("EMPLOYEE SSN", fSpec.isSsnCol(), fSpec.getSsnColIndex(), fSpec.getSsnParsePattern(), null,2, false,3,0,0));
		list.add(new HBoxDetailCell("EMPLOYEE IDE", fSpec.isErRefCol(), fSpec.getErRefColIndex(), fSpec.getErRefParsePattern(), null,2, false,3,0,0));
		list.add(new HBoxDetailCell("FIRST NAME", fSpec.isfNameCol(), fSpec.getfNameColIndex(), fSpec.getfNameParsePattern(), null,2, false,3,0,0));
		list.add(new HBoxDetailCell("MIDDLE NAME", fSpec.ismNameCol(), fSpec.getmNameColIndex(), fSpec.getmNameParsePattern(), null,2, false,3,0,0));
		list.add(new HBoxDetailCell("LAST NAME", fSpec.islNameCol(), fSpec.getlNameColIndex(), fSpec.getlNameParsePattern(), null,2, false,3,0,0));

		list.add(new HBoxDetailCell("Employee Date Data"));
		list.add(new HBoxDetailCell("DOB", fSpec.isDobCol(), fSpec.getDobColIndex(), null, fSpec.getDobFormat(),3, false,3,0,0));
		list.add(new HBoxDetailCell("HIRE DATE", fSpec.isHireDtCol(), fSpec.getHireDtColIndex(), null, fSpec.getHireDtFormat(),3, false,3,0,0));
		list.add(new HBoxDetailCell("REHIRE DATE", fSpec.isRhireDtCol(), fSpec.getRhireDtColIndex(), null, fSpec.getRhireDtFormat(),3, false,3,0,0));
		list.add(new HBoxDetailCell("TERM DATE", fSpec.isTermDtCol(), fSpec.getTermDtColIndex(), null, fSpec.getTermDtFormat(),3, false,3,0,0));
		list.add(new HBoxDetailCell("CVG CLASS", fSpec.isCvgGroupCol(), fSpec.getCvgGroupColIndex(), null, null, 1, true,2,9, getPayFileCvgClassRefCount()));
		list.add(new HBoxDetailCell("JOB TITLE", fSpec.isJobTtlCol(), fSpec.getJobTtlColIndex(), null, null, 1, false,1,0,0));
		list.add(new HBoxDetailCell("DEPARTMENT", fSpec.isDeptCol(), fSpec.getDeptColIndex(), null, null, 1, true,3,3, getPayFileDepartmentRefCount()));
		list.add(new HBoxDetailCell("LOCATION", fSpec.isLocCol(), fSpec.getLocColIndex(), null, null, 1, true,3,4,getPayFileLocationRefCount()));			
		list.add(new HBoxDetailCell("PPD START DATE", fSpec.isPpdStartDtCol(), fSpec.getPpdStartDtColIndex(), fSpec.getPpdStartDtParsePattern(), fSpec.getPpdStartDtFormat(),4, false,3,0,0));
		list.add(new HBoxDetailCell("PPD END DATE", fSpec.isPpdEndDtCol(), fSpec.getPpdEndDtColIndex(), fSpec.getPpdEndDtParsePattern(), fSpec.getPpdEndDtFormat(),4, false,3,0,0));
		list.add(new HBoxDetailCell("PAY DATE", fSpec.isPayDtCol(), fSpec.getPayDtColIndex(), fSpec.getPayDtParsePattern(), fSpec.getPayDtFormat(),4, false,3,0,0));
		list.add(new HBoxDetailCell("WORK DATE", fSpec.isWorkDtCol(), fSpec.getWorkDtColIndex(), fSpec.getWorkDtParsePattern(), fSpec.getWorkDtFormat(),4, false,3,0,0));

		list.add(new HBoxDetailCell("Payperiod Amounts"));
		list.add(new HBoxDetailCell("PPD ID", fSpec.isPpdErRefCol(), fSpec.getPpdErRefColIndex(), null, null,1, false,3,0,0));
		list.add(new HBoxDetailCell("PPD FREQUENCY", fSpec.isPpdFreqCol(), fSpec.getPpdFreqColIndex(), null, null,1, true,3,6, getPayFilePayFrequencyRefCount()));
		list.add(new HBoxDetailCell("HOURS", fSpec.isHrsCol(), fSpec.getHrsColIndex(), null, null,1, false,3,0,0));
		list.add(new HBoxDetailCell("RATE", fSpec.isRateCol(), fSpec.getRateColIndex(), null, null,1, false,3,0,0));
		list.add(new HBoxDetailCell("AMOUNT", fSpec.isAmtCol(), fSpec.getAmtColIndex(), null, null,1, false,3,0,0));
		list.add(new HBoxDetailCell("COMP TYPE",fSpec.isPayCodeCol(), fSpec.getPayCodeColIndex(), null, null,1, true,3,5, getPayFilePayCodeTypeRefCount()));
		list.add(new HBoxDetailCell("UNION TYPE",fSpec.isUnionTypeCol(), fSpec.getUnionTypeColIndex(), null, null,1, true,3,7, getPayFileUnionTypeRefCount()));
		list.add(new HBoxDetailCell("PAY CLASS", fSpec.isPayClassCol(), fSpec.getPayClassColIndex(), null, null, 1, true,3,8, selectedPayClassRefs.size()));
		
		list.add(new HBoxDetailCell("Custom Fields"));		
		list.add(new HBoxDetailCell(fSpec.getCfld1Name(),fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), fSpec.getCfld1ParsePattern(), null,5, false,3,0,0));
		list.add(new HBoxDetailCell(fSpec.getCfld2Name(),fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), fSpec.getCfld2ParsePattern(), null,5, false,3,0,0));
		list.add(new HBoxDetailCell(fSpec.getCfld3Name(),fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), null, null,6, false,3,0,0));
		list.add(new HBoxDetailCell(fSpec.getCfld4Name(),fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), null, null,6, false,3,0,0));
		list.add(new HBoxDetailCell(fSpec.getCfld5Name(),fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), null, null,6, false,3,0,0));
		ObservableList<HBoxDetailCell> newObservableList = FXCollections.observableList(list);
        dfcovDataListView.setItems(newObservableList);	
        
		List<HBoxDetailCell> list2 = new ArrayList<HBoxDetailCell>();	
		list2.add(new HBoxDetailCell());
		list2.add(new HBoxDetailCell("Address Information"));		
		list2.add(new HBoxDetailCell("STRT NUMBER", fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), null, null,1, false,3,0,0));
		list2.add(new HBoxDetailCell("ADD LINE 1", fSpec.isStreetCol(), fSpec.getStreetColIndex(), fSpec.getStreetParsePattern(), null,2, false,3,0,0));
		list2.add(new HBoxDetailCell("ADD LINE 2", fSpec.isLin2Col(), fSpec.getLin2ColIndex(), null, null,1, false,3,0,0));
		list2.add(new HBoxDetailCell("CITY", fSpec.isCityCol(), fSpec.getCityColIndex(), fSpec.getCityParsePattern(), null,2, false,3,0,0));
		list2.add(new HBoxDetailCell("STATE", fSpec.isStateCol(), fSpec.getStateColIndex(), fSpec.getStateParsePattern(), null,2, false,3,0,0));
		list2.add(new HBoxDetailCell("ZIP", fSpec.isZipCol(), fSpec.getZipColIndex(), fSpec.getZipParsePattern(), null,2, false,3,0,0));
		
		list2.add(new HBoxDetailCell("Overtime Information"));		
		list2.add(new HBoxDetailCell("OT HRS", fSpec.isOtHrsCol(), fSpec.getOtHrsColIndex(), null, null,1, false,3,0,0));
		list2.add(new HBoxDetailCell("OT 2 HRS", fSpec.isOtHrs2Col(), fSpec.getOtHrs2ColIndex(), null, null,1, false,3,0,0));
		list2.add(new HBoxDetailCell("OT 3 HRS", fSpec.isOtHrs3Col(), fSpec.getOtHrs3ColIndex(), null, null,1, false,3,0,0));
		list2.add(new HBoxDetailCell("PTO HRS", fSpec.isPtoHrsCol(), fSpec.getPtoHrsColIndex(), null, null,1, false,3,0,0));
		list2.add(new HBoxDetailCell("SICK PAY HRS", fSpec.isSickHrsCol(), fSpec.getSickHrsColIndex(), null, null,1, false,3,0,0));
		list2.add(new HBoxDetailCell("HLDY PAY HRS", fSpec.isHolidayHrsCol(), fSpec.getHolidayHrsColIndex(), null, null,1, false,3,0,0));

		list2.add(new HBoxDetailCell("Custom Pay Fields"));		
		list2.add(new HBoxDetailCell(fSpec.getPayCfld1Name(),fSpec.isPayCfld1Col(), fSpec.getPayCfld1ColIndex(), fSpec.getPayCfld1ParsePattern(), null,5, false,3,0,0));
		list2.add(new HBoxDetailCell(fSpec.getPayCfld2Name(),fSpec.isPayCfld2Col(), fSpec.getPayCfld2ColIndex(), fSpec.getPayCfld2ParsePattern(), null,5, false,3,0,0));
		list2.add(new HBoxDetailCell(fSpec.getPayCfld3Name(),fSpec.isPayCfld3Col(), fSpec.getPayCfld3ColIndex(), null, null,6, false,3,0,0));
		list2.add(new HBoxDetailCell(fSpec.getPayCfld4Name(),fSpec.isPayCfld4Col(), fSpec.getPayCfld4ColIndex(), null, null,6, false,3,0,0));
		list2.add(new HBoxDetailCell(fSpec.getPayCfld5Name(),fSpec.isPayCfld5Col(), fSpec.getPayCfld5ColIndex(), null, null,6, false,3,0,0));
		list2.add(new HBoxDetailCell(fSpec.getPayCfld6Name(),fSpec.isPayCfld6Col(), fSpec.getPayCfld6ColIndex(), null, null,6, false,3,0,0));
		list2.add(new HBoxDetailCell(fSpec.getPayCfld7Name(),fSpec.isPayCfld7Col(), fSpec.getPayCfld7ColIndex(), null, null,6, false,3,0,0));
        ObservableList<HBoxDetailCell> newObservableList2 = FXCollections.observableList(list2);
        dfcovDataList2View.setItems(newObservableList2);	

        setSpecListSize();
        updateFileLayoutDisplay();
	}
	
	private int getPayFileLocationRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicPayFileSpecification.getLocReferences() != null)
			for (DynamicPayFileLocationReference payRef:DataManager.i().mDynamicPayFileSpecification.getLocReferences())
				if (payRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	private int getEmployeeFileLocationRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicEmployeeFileSpecification.getLocReferences() != null)
			for (DynamicEmployeeFileLocationReference empRef:DataManager.i().mDynamicEmployeeFileSpecification.getLocReferences())
				if (empRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	private int getPayFileDepartmentRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicPayFileSpecification.getDeptReferences() != null)
			for (DynamicPayFileDepartmentReference payRef: DataManager.i().mDynamicPayFileSpecification.getDeptReferences())
				if (payRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	private int getEmployeeFileDepartmentRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicEmployeeFileSpecification.getDeptReferences() != null)
			for (DynamicEmployeeFileDepartmentReference empRef: DataManager.i().mDynamicEmployeeFileSpecification.getDeptReferences())
				if (empRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	private int getPayFilePayFrequencyRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicPayFileSpecification.getPpdFreqReferences() != null)
			for (DynamicPayFilePayFrequencyReference payRef: DataManager.i().mDynamicPayFileSpecification.getPpdFreqReferences())
				if (payRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	private int getPayFileUnionTypeRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicPayFileSpecification.getUnionTypeReferences() != null)
			for (DynamicPayFileUnionTypeReference payRef: DataManager.i().mDynamicPayFileSpecification.getUnionTypeReferences())
				if (payRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	private int getPayFilePayCodeTypeRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicPayFileSpecification.getPayCodeReferences() != null)
			for (DynamicPayFilePayCodeReference payRef: DataManager.i().mDynamicPayFileSpecification.getPayCodeReferences())
				if (payRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	private int getEmployeeFilePayCodeTypeRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicEmployeeFileSpecification.getPayCodeReferences() != null)
			for (DynamicEmployeeFilePayCodeReference payRef: DataManager.i().mDynamicEmployeeFileSpecification.getPayCodeReferences())
				if (payRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	private int getEmployeeFileGenderTypeRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicEmployeeFileSpecification.getGenderReferences() != null)
			for (DynamicEmployeeFileGenderReference empRef: DataManager.i().mDynamicEmployeeFileSpecification.getGenderReferences())
				if (empRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	private int getCoverageFileGenderTypeRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicCoverageFileSpecification.getGenderReferences() != null)
			for (DynamicCoverageFileGenderReference empRef: DataManager.i().mDynamicCoverageFileSpecification.getGenderReferences())
				if (empRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	private int getCoverageFileEmployerRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicCoverageFileSpecification.getErReferences() != null)
			for (DynamicCoverageFileEmployerReference erRef: DataManager.i().mDynamicCoverageFileSpecification.getErReferences())
				if (erRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	private int getEmployeeFileEmployerRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicEmployeeFileSpecification.getErReferences() != null)
			for (DynamicEmployeeFileEmployerReference erRef: DataManager.i().mDynamicEmployeeFileSpecification.getErReferences())
				if (erRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	private int getPayFileEmployerRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicPayFileSpecification.getErReferences() != null)
			for (DynamicPayFileEmployerReference erRef: DataManager.i().mDynamicPayFileSpecification.getErReferences())
				if (erRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	private int getCoverageFileCvgClassRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicCoverageFileSpecification.getCvgGroupReferences() != null)
			for (DynamicCoverageFileCoverageGroupReference empRef: DataManager.i().mDynamicCoverageFileSpecification.getCvgGroupReferences())
				if (empRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	private int getEmployeeFileCvgClassRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicEmployeeFileSpecification.getCvgGroupReferences() != null)
			for (DynamicEmployeeFileCoverageGroupReference empRef: DataManager.i().mDynamicEmployeeFileSpecification.getCvgGroupReferences())
				if (empRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	private int getPayFileCvgClassRefCount(){
		int locRefSize = 0;
		if (DataManager.i().mDynamicPayFileSpecification.getCvgGroupReferences() != null)
			for (DynamicPayFileCoverageGroupReference empRef: DataManager.i().mDynamicPayFileSpecification.getCvgGroupReferences())
				if (empRef.isActive() == true)
					locRefSize++;
		return locRefSize;
	}

	/*
	 * DynamicPayPeriodFileSpecification Support
	 */
	private void updatePayPeriodFileData(){

		DynamicPayPeriodFileSpecification fSpec = DataManager.i().mDynamicPayPeriodFileSpecification;
		if (fSpec != null) {
			readOnly = fSpec.isLocked();
			Utils.updateControl(dfcovNameLabel,fSpec.getName());
			Utils.updateControl(dfcovTabIndexLabel,String.valueOf(fSpec.getTabIndex()));
			Utils.updateControl(dfcovHeaderRowIndexLabel,String.valueOf(fSpec.getHeaderRowIndex()));
			
			//core data read only
			Utils.updateControl(dfcovCoreIdLabel,String.valueOf(fSpec.getId()));
			Utils.updateControl(dfcovCoreActiveLabel,String.valueOf(fSpec.isActive()));
			Utils.updateControl(dfcovCoreBODateLabel,fSpec.getBornOn());
			Utils.updateControl(dfcovCoreLastUpdatedLabel,fSpec.getLastUpdated());
			
			//update the column field table
			loadFields(fSpec);	        
		}
	}
	
	private void loadFields(DynamicPayPeriodFileSpecification fSpec) {

		List<HBoxDetailCell> list = new ArrayList<HBoxDetailCell>();			
		list.add(new HBoxDetailCell());
		list.add(new HBoxDetailCell("Pay Period Information"));
		list.add(new HBoxDetailCell("PPD ID", fSpec.isErRefCol(), fSpec.getErRefColIndex(), fSpec.getErRefParsePattern(), null,2, false,4,0,0));
		list.add(new HBoxDetailCell("START DATE", fSpec.isStartDtCol(), fSpec.getStartDtColIndex(), null, fSpec.getStartDtFormat(),3, false,4,0,0));
		list.add(new HBoxDetailCell("END DATE", fSpec.isEndDtCol(), fSpec.getEndDtColIndex(), null, fSpec.getEndDtFormat(),3, false,4,0,0));
		list.add(new HBoxDetailCell("PAY DATE", fSpec.isPayDtCol(), fSpec.getPayDtColIndex(), null, fSpec.getPayDtFormat(),3, false,4,0,0));
        ObservableList<HBoxDetailCell> newObservableList = FXCollections.observableList(list);
        dfcovDataListView.setItems(newObservableList);	
        
		List<HBoxDetailCell> list2 = new ArrayList<HBoxDetailCell>();			
		list2.add(new HBoxDetailCell());
		list2.add(new HBoxDetailCell("Custom Fields"));
		list2.add(new HBoxDetailCell(fSpec.getCfld1Name(),fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), fSpec.getCfld1ParsePattern(), null,5, false,4,0,0));
		list2.add(new HBoxDetailCell(fSpec.getCfld2Name(),fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), fSpec.getCfld2ParsePattern(), null,5, false,4,0,0));
		list2.add(new HBoxDetailCell(fSpec.getCfld3Name(),fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), null, null,6, false,4,0,0));
		list2.add(new HBoxDetailCell(fSpec.getCfld4Name(),fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), null, null,6, false,4,0,0));
		list2.add(new HBoxDetailCell(fSpec.getCfld5Name(),fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), null, null,6, false,4,0,0));
        ObservableList<HBoxDetailCell> newObservableList2 = FXCollections.observableList(list2);
        dfcovDataList2View.setItems(newObservableList2);	
       
        setSpecListSize();
        updateFileLayoutDisplay();
	}

	/*
	 * DynamicPlanFileSpecification Support
	 */
/*	private void updatePlanFileData(){

		DynamicPlanFileSpecification fSpec = DataManager.i().mDynamicPlanFileSpecification;
		if (fSpec != null) {
			readOnly = fSpec.isLocked();
			Utils.updateControl(dfcovNameLabel,fSpec.getName());
			Utils.updateControl(dfcovDescriptionLabel,fSpec.getDescription());
			Utils.updateControl(dfcovTabIndexLabel,String.valueOf(fSpec.getTabIndex()));
			Utils.updateControl(dfcovHeaderRowIndexLabel,String.valueOf(fSpec.getHeaderRowIndex()));
			
			//core data read only
			Utils.updateControl(dfcovCoreIdLabel,String.valueOf(fSpec.getId()));
			Utils.updateControl(dfcovCoreActiveLabel,String.valueOf(fSpec.isActive()));
			Utils.updateControl(dfcovCoreBODateLabel,fSpec.getBornOn());
			Utils.updateControl(dfcovCoreLastUpdatedLabel,fSpec.getLastUpdated());
			
			//update the column field table
			loadFields(fSpec);	        
		}
	}
*/	
/*	private void loadFields(DynamicPlanFileSpecification fSpec) {

		List<HBoxDetailCell> list = new ArrayList<HBoxDetailCell>();			
		list.add(new HBoxDetailCell());
		list.add(new HBoxDetailCell("Plan Information"));
		list.add(new HBoxDetailCell("PLAN ID", fSpec.isPlanRefCol(), fSpec.getPlanRefColIndex(), fSpec.getPlanRefParsePattern(), null,2, false,5,0,0));
		list.add(new HBoxDetailCell("PLAN NAME", fSpec.isNmCol(), fSpec.getNmColIndex(), null, null,1, false,5,0,0));
		list.add(new HBoxDetailCell("PLAN DESC", fSpec.isDescCol(), fSpec.getDescColIndex(), null, null,1, false,5,0,0));
        ObservableList<HBoxDetailCell> newObservableList = FXCollections.observableList(list);
        dfcovDataListView.setItems(newObservableList);	

		List<HBoxDetailCell> list2 = new ArrayList<HBoxDetailCell>();			
		list2.add(new HBoxDetailCell());
		list2.add(new HBoxDetailCell("Plan Information"));
		list2.add(new HBoxDetailCell("PLAN TYPE", fSpec.isPlanTypeCol(), fSpec.getPlanTypeColIndex(), null, null,1, false,5,0,0));
		list2.add(new HBoxDetailCell("PLAN WAVED", fSpec.isWavdCol(), fSpec.getWavdColIndex(), fSpec.getWavdTrueParsePattern(), null,2, false,5,0,0));
		list2.add(new HBoxDetailCell("PLAN INEL", fSpec.isInelCol(), fSpec.getInelColIndex(), fSpec.getInelTrueParsePattern(), null,2, false,5,0,0));
        ObservableList<HBoxDetailCell> newObservableList2 = FXCollections.observableList(list2);
        dfcovDataList2View.setItems(newObservableList2);	
        
        setSpecListSize();
        updateFileLayoutDisplay();
	}
*/	
	private boolean validateData() {
		boolean bReturn = true;
		
			//check for required data
			if ( !Utils.validate(dfcovNameLabel)) bReturn = false;
			if ( !Utils.validate(dfcovDescriptionLabel)) bReturn = false;
			if ( !Utils.validate(dfcovTabIndexLabel)) bReturn = false;
			if ( !Utils.validate(dfcovHeaderRowIndexLabel)) bReturn = false;
			if ( !Utils.validateIntTextField(dfcovTabIndexLabel)) bReturn = false;
			if ( !Utils.validateIntTextField(dfcovHeaderRowIndexLabel)) bReturn = false;
		
		return bReturn;
	}

	private void setSaving(boolean saving) {
		if (saving == false)
			refreshScreen();
		dfcovSavingChangesButton.setVisible(saving);
		dfcovSaveChangesButton.setVisible(!saving);
	}
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		setSaving(true);

		// new thread
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
        		// update the current object
        		SaveFileSpecification();
                return null;
            }
        };
      //	task.setOnScheduled(e ->  {
      //		EtcAdmin.i().setStatusMessage("...");
      //		EtcAdmin.i().setProgress(0.5);});
      			
    	task.setOnSucceeded(e ->  setSaving(false));
    	task.setOnFailed(e ->  setSaving(false));
        new Thread(task).start();
	}
	
	private void refreshScreen() {
		clearData();
		updateFileData();

	}
	
	private void SaveFileSpecification() {
		switch(fileType){
		case COVERAGE:
			//updateCoverageFile();
			saveCoverageFileSpecification();
			break;
		case EMPLOYEE:
			//updateEmployeeFile();
			saveEmployeeFileSpecification();
			break;
		case PAY:
			//updatePayFile();
			savePayFileSpecification();
			break;
		case PAYPERIOD:
			updatePayPeriodFile();
			break;
		case IRS1094C:
			break;
		case IRS1095C:
			break;
		case IRSAIRERR:
			break;
		case IRSAIRRCPT:
			break;
		case EVENT:
			break;
		default:
			break;
		}	
	}
	
/*	private void updateEmployeeFile(){

		//reset the updateRequest object if it has been used
		DataManager.i().mUpdateDynamicEmployeeFileSpecificationRequest = null;			
		DataManager.i().mUpdateDynamicEmployeeFileSpecificationRequest = new UpdateDynamicEmployeeFileSpecificationRequest();
		// local for convenience
		UpdateDynamicEmployeeFileSpecificationRequest request = DataManager.i().mUpdateDynamicEmployeeFileSpecificationRequest;
		//update the request object with the coverage file
		request.setDynamicEmployeeFileSpecification((DataManager.i().mDynamicEmployeeFileSpecification));

		//gather the fields
		request.setName(dfcovNameLabel.getText());
		request.setDescription(dfcovDescriptionLabel.getText());
		request.setTabIndex(Integer.valueOf(dfcovTabIndexLabel.getText()));
		request.setHeaderRowIndex(Integer.valueOf(dfcovHeaderRowIndexLabel.getText()));
		request.setCreateEmployee(dfcovCreateEmployeeCheck.isSelected());
		request.setSkipLastRow(dfcovSkipLastRowCheck.isSelected());
		
		//update the column field table
		saveFields(request);	        
	} */
	
/*	private void saveFields(UpdateDynamicEmployeeFileSpecificationRequest request) {
		// LISTVIEW 1
		// EMPLOYER IDENTIFIER
		HBoxDetailCell cell = dfcovDataListView.getItems().get(2);
		request.setErCol(cell.getColumn());
		request.setErColIndex(cell.getColumnIndex());
		
     	// ETC LITE ID
		cell = dfcovDataListView.getItems().get(3);
		request.setEtcRefCol(cell.getColumn());
		request.setEtcRefColIndex(cell.getColumnIndex());
		
     	// EMPLOYEE SSN
		cell = dfcovDataListView.getItems().get(4);
		request.setSsnCol(cell.getColumn());
		request.setSsnColIndex(cell.getColumnIndex());
		request.setSsnParsePatternId(cell.getSelectedPatternId());
		
     	// EMPLOYEE IDENTIFIER
		cell = dfcovDataListView.getItems().get(5);
		request.setErRefCol(cell.getColumn());
		request.setErRefColIndex(cell.getColumnIndex());
		request.setErRefParsePatternId(cell.getSelectedPatternId());
		
     	// FIRST NAME
		cell = dfcovDataListView.getItems().get(6);
		request.setfNameCol(cell.getColumn());
		request.setfNameColIndex(cell.getColumnIndex());
		request.setfNameParsePatternId(cell.getSelectedPatternId());
		
     	// MIDDLE NAME
		cell = dfcovDataListView.getItems().get(7);
		request.setmNameCol(cell.getColumn());
		request.setmNameColIndex(cell.getColumnIndex());
		request.setmNameParsePatternId(cell.getSelectedPatternId());

		// LAST NAME
		cell = dfcovDataListView.getItems().get(8);
		request.setlNameCol(cell.getColumn());
		request.setlNameColIndex(cell.getColumnIndex());
		request.setlNameParsePatternId(cell.getSelectedPatternId());
		
		// Phone Number
		cell = dfcovDataListView.getItems().get(9);
		request.setPhoneCol(cell.getColumn());
		request.setPhoneColIndex(cell.getColumnIndex());

		// Email Column
		cell = dfcovDataListView.getItems().get(10);
		request.setEmlCol(cell.getColumn());
		request.setEmlColIndex(cell.getColumnIndex());

		// Department
		cell = dfcovDataListView.getItems().get(11);
		request.setDeptCol(cell.getColumn());
		request.setDeptColIndex(cell.getColumnIndex());
	
		// Location
		cell = dfcovDataListView.getItems().get(12);
		request.setLocCol(cell.getColumn());
		request.setLocColIndex(cell.getColumnIndex());
		
		// Job Title
		cell = dfcovDataListView.getItems().get(13);
		request.setJobTtlCol(cell.getColumn());
		request.setJobTtlColIndex(cell.getColumnIndex());
		
		// Pay Code
		cell = dfcovDataListView.getItems().get(14);
		request.setPayCodeCol(cell.getColumn());
		request.setPayCodeColIndex(cell.getColumnIndex());
		
     	// Custom Field 1
		cell = dfcovDataListView.getItems().get(16);
		request.setCfld1Name(cell.getName());
		request.setCfld1Col(cell.getColumn());
		request.setCfld1ColIndex(cell.getColumnIndex());
		request.setCfld1ParsePatternId(cell.getSelectedPatternId());

     	// Custom Field 2
		cell = dfcovDataListView.getItems().get(17);
		request.setCfld2Name(cell.getName());
		request.setCfld2Col(cell.getColumn());
		request.setCfld2ColIndex(cell.getColumnIndex());
		request.setCfld2ParsePatternId(cell.getSelectedPatternId());

     	// Custom Field 3
		cell = dfcovDataListView.getItems().get(18);
		request.setCfld3Name(cell.getName());
		request.setCfld3Col(cell.getColumn());
		request.setCfld3ColIndex(cell.getColumnIndex());

     	// Custom Field 4
		cell = dfcovDataListView.getItems().get(19);
		request.setCfld4Name(cell.getName());
		request.setCfld4Col(cell.getColumn());
		request.setCfld4ColIndex(cell.getColumnIndex());

     	// Custom Field 5
		cell = dfcovDataListView.getItems().get(20);
		request.setCfld5Name(cell.getName());
		request.setCfld5Col(cell.getColumn());
		request.setCfld5ColIndex(cell.getColumnIndex());
		
		// LISTVIEW 2
		// STREET NUMBER
		cell = dfcovDataList2View.getItems().get(2);
		request.setStreetNumCol(cell.getColumn());
		request.setStreetNumColIndex(cell.getColumnIndex());
		request.setStreetParsePatternId(cell.getSelectedPatternId());
		
		// ADDRESS LINE 1
		cell = dfcovDataList2View.getItems().get(3);
		request.setStreetCol(cell.getColumn());
		request.setStreetColIndex(cell.getColumnIndex());
		request.setStreetParsePatternId(cell.getSelectedPatternId());
		
		// ADDRESS LINE 2
		cell = dfcovDataList2View.getItems().get(4);
		request.setLin2Col(cell.getColumn());
		request.setLin2ColIndex(cell.getColumnIndex());
		
		// City
		cell = dfcovDataList2View.getItems().get(5);
		request.setCityCol(cell.getColumn());
		request.setCityColIndex(cell.getColumnIndex());
		request.setCityParsePatternId(cell.getSelectedPatternId());
		
		// State
		cell = dfcovDataList2View.getItems().get(6);
		request.setStateCol(cell.getColumn());
		request.setStateColIndex(cell.getColumnIndex());
		request.setStateParsePatternId(cell.getSelectedPatternId());
		
		// Zip
		cell = dfcovDataList2View.getItems().get(7);
		request.setZipCol(cell.getColumn());
		request.setZipColIndex(cell.getColumnIndex());
		request.setZipParsePatternId(cell.getSelectedPatternId());
	
		// Gender
		cell = dfcovDataList2View.getItems().get(8);
		request.setGenderCol(cell.getColumn());
		request.setGenderColIndex(cell.getColumnIndex());
	
     	// Date of Birth
		cell = dfcovDataList2View.getItems().get(10);
		request.setDobCol(cell.getColumn());
		request.setDobColIndex(cell.getColumnIndex());
		request.setDobFormatId(cell.getSelectedFormatId());
		
     	// Hire Date
		cell = dfcovDataList2View.getItems().get(11);
		request.setHireDtCol(cell.getColumn());
		request.setHireDtColIndex(cell.getColumnIndex());
		request.setHireDtFormatId(cell.getSelectedFormatId());
		
     	// Rehire Date
		cell = dfcovDataList2View.getItems().get(12);
		request.setRhireDtCol(cell.getColumn());
		request.setRhireDtColIndex(cell.getColumnIndex());
		request.setRhireDtFormatId(cell.getSelectedFormatId());
		
     	// Termination Date
		cell = dfcovDataList2View.getItems().get(13);
		request.setTermDtCol(cell.getColumn());
		request.setTermDtColIndex(cell.getColumnIndex());
		request.setTermDtFormatId(cell.getSelectedFormatId());

     	// Cvg Class
		cell = dfcovDataList2View.getItems().get(14);
		request.setCvgClassCol(cell.getColumn());
		request.setCvgClassColIndex(cell.getColumnIndex());
	} */

/*	private void updateCoverageFile(){
		//reset the updateRequest object if it has been used
		DataManager.i().mUpdateDynamicCoverageFileSpecificationRequest = null;			
		DataManager.i().mUpdateDynamicCoverageFileSpecificationRequest = new UpdateDynamicCoverageFileSpecificationRequest();
		// local for convenience
		UpdateDynamicCoverageFileSpecificationRequest updReq = DataManager.i().mUpdateDynamicCoverageFileSpecificationRequest;
		//update the request object with the coverage file
		updReq.setDynamicCoverageFileSpecification((DataManager.i().mDynamicCoverageFileSpecification));

		//gather the fields
		updReq.setName(dfcovNameLabel.getText());
		updReq.setDescription(dfcovDescriptionLabel.getText());
		updReq.setTabIndex(Integer.valueOf(dfcovTabIndexLabel.getText()));
		updReq.setHeaderRowIndex(Integer.valueOf(dfcovHeaderRowIndexLabel.getText()));
		updReq.setMapEEtoER(dfcovMapEEtoERCheck.isSelected());
		updReq.setCreateEmployee(dfcovCreateEmployeeCheck.isSelected());
		updReq.setCreateSecondary(dfcovCreateSecondaryCheck.isSelected());
		updReq.setCreatePlan(dfcovCreatePlanCheck.isSelected());
		updReq.setSkipLastRow(dfcovSkipLastRowCheck.isSelected());

		//update the column field table
		saveFields(updReq);	   
	}
*/		
/*	private void saveFields(UpdateDynamicCoverageFileSpecificationRequest updReq) {
		
		// LISTVIEW 1 FIELDS
     	// EMPLOYER IDENTIFIER
		HBoxDetailCell cell = dfcovDataListView.getItems().get(2);
		updReq.setErCol(cell.getColumn());
		updReq.setErColIndex(cell.getColumnIndex());
				
     	// EMPLOYER IDENTIFIER
		cell = dfcovDataListView.getItems().get(3);
		updReq.setEtcRefCol(cell.getColumn());
		updReq.setEtcRefColIndex(cell.getColumnIndex());
				
     	// EMPLOYEE SSN
		cell = dfcovDataListView.getItems().get(4);
		updReq.setSsnCol(cell.getColumn());
		updReq.setSsnColIndex(cell.getColumnIndex());
		updReq.setSsnParsePatternId(cell.getSelectedPatternId());
		
     	// EMPLOYEE IDENTIFIER
		cell = dfcovDataListView.getItems().get(5);
		updReq.setErRefCol(cell.getColumn());
		updReq.setErRefColIndex(cell.getColumnIndex());
		updReq.setErRefParsePatternId(cell.getSelectedPatternId());
		
     	// SUBSCRIBER NUMBER
		cell = dfcovDataListView.getItems().get(6);
		updReq.setSubcrbrCol(cell.getColumn());
		updReq.setSubcrbrColIndex(cell.getColumnIndex());

     	// EMPLOYEE FLAG
		cell = dfcovDataListView.getItems().get(7);
		updReq.setEeFlagCol(cell.getColumn());
		updReq.setEeFlagColIndex(cell.getColumnIndex());
		updReq.setEeFlagTrueParsePatternId(cell.getSelectedPatternId());

		// FIRST NAME
		cell = dfcovDataListView.getItems().get(8);
		updReq.setfNameCol(cell.getColumn());
		updReq.setfNameColIndex(cell.getColumnIndex());
		updReq.setfNameParsePatternId(cell.getSelectedPatternId());
		
     	// MIDDLE NAME
		cell = dfcovDataListView.getItems().get(9);
		updReq.setmNameCol(cell.getColumn());
		updReq.setmNameColIndex(cell.getColumnIndex());
		updReq.setmNameParsePatternId(cell.getSelectedPatternId());

		// LAST NAME
		cell = dfcovDataListView.getItems().get(10);
		updReq.setlNameCol(cell.getColumn());
		updReq.setlNameColIndex(cell.getColumnIndex());
		updReq.setlNameParsePatternId(cell.getSelectedPatternId());
		
		// Phone Number
		cell = dfcovDataListView.getItems().get(11);
		updReq.setPhoneCol(cell.getColumn());
		updReq.setPhoneColIndex(cell.getColumnIndex());

		// Email Column
		cell = dfcovDataListView.getItems().get(12);
		updReq.setEmlCol(cell.getColumn());
		updReq.setEmlColIndex(cell.getColumnIndex());

		// Coverage Waived
		cell = dfcovDataListView.getItems().get(14);
		updReq.setWavdCol(cell.getColumn());
		updReq.setWavdColIndex(cell.getColumnIndex());
		updReq.setWavdTrueParsePatternId(cell.getSelectedPatternId());
	
		// Coverage  INELIGIBLE
		cell = dfcovDataListView.getItems().get(15);
		updReq.setInelCol(cell.getColumn());
		updReq.setInelColIndex(cell.getColumnIndex());
		updReq.setInelTrueParsePatternId(cell.getSelectedPatternId());
	
		// Decision Date
		cell = dfcovDataListView.getItems().get(16);
		updReq.setDecisionDtCol(cell.getColumn());
		updReq.setDecisionDtColIndex(cell.getColumnIndex());
		updReq.setDecisionDtFormatId(cell.getSelectedFormatId());
	
     	// CoverageStart Date
		cell = dfcovDataListView.getItems().get(17);
		updReq.setCovStartDtCol(cell.getColumn());
		updReq.setCovStartDtColIndex(cell.getColumnIndex());
		updReq.setCovStartDtFormatId(cell.getSelectedFormatId());
		
     	// Coverage End Date
		cell = dfcovDataListView.getItems().get(18);
		updReq.setCovEndDtCol(cell.getColumn());
		updReq.setCovEndDtColIndex(cell.getColumnIndex());
		updReq.setCovEndDtFormatId(cell.getSelectedFormatId());
		
		// Coverage Tax Year Selected AKA Annual Coverage
		cell = dfcovDataListView.getItems().get(20);
		updReq.setTySelCol(cell.getColumn());
		updReq.setTySelColIndex(cell.getColumnIndex());
		updReq.setTySelTrueParsePatternId(cell.getSelectedPatternId());
		
		// Coverage January Selected
		cell = dfcovDataListView.getItems().get(21);
		updReq.setJanSelCol(cell.getColumn());
		updReq.setJanSelColIndex(cell.getColumnIndex());
		updReq.setJanSelTrueParsePatternId(cell.getSelectedPatternId());
	
		// Coverage February Selected
		cell = dfcovDataListView.getItems().get(22);
		updReq.setFebSelCol(cell.getColumn());
		updReq.setFebSelColIndex(cell.getColumnIndex());
		updReq.setFebSelTrueParsePatternId(cell.getSelectedPatternId());
	
		// Coverage March Selected
		cell = dfcovDataListView.getItems().get(23);
		updReq.setMarSelCol(cell.getColumn());
		updReq.setMarSelColIndex(cell.getColumnIndex());
		updReq.setMarSelTrueParsePatternId(cell.getSelectedPatternId());
	
		// Coverage April Selected
		cell = dfcovDataListView.getItems().get(24);
		updReq.setAprSelCol(cell.getColumn());
		updReq.setAprSelColIndex(cell.getColumnIndex());
		updReq.setAprSelTrueParsePatternId(cell.getSelectedPatternId());
	
		// Coverage May Selected
		cell = dfcovDataListView.getItems().get(25);
		updReq.setMaySelCol(cell.getColumn());
		updReq.setMaySelColIndex(cell.getColumnIndex());
		updReq.setMaySelTrueParsePatternId(cell.getSelectedPatternId());
	
		// Coverage June Selected
		cell = dfcovDataListView.getItems().get(26);
		updReq.setJunSelCol(cell.getColumn());
		updReq.setJunSelColIndex(cell.getColumnIndex());
		updReq.setJunSelTrueParsePatternId(cell.getSelectedPatternId());
	
		// Coverage July Selected
		cell = dfcovDataListView.getItems().get(27);
		updReq.setJulSelCol(cell.getColumn());
		updReq.setJulSelColIndex(cell.getColumnIndex());
		updReq.setJulSelTrueParsePatternId(cell.getSelectedPatternId());
	
		// Coverage August Selected
		cell = dfcovDataListView.getItems().get(28);
		updReq.setAugSelCol(cell.getColumn());
		updReq.setAugSelColIndex(cell.getColumnIndex());
		updReq.setAugSelTrueParsePatternId(cell.getSelectedPatternId());
	
		// Coverage September Selected
		cell = dfcovDataListView.getItems().get(29);
		updReq.setSepSelCol(cell.getColumn());
		updReq.setSepSelColIndex(cell.getColumnIndex());
		updReq.setSepSelTrueParsePatternId(cell.getSelectedPatternId());
	
		// Coverage October Selected
		cell = dfcovDataListView.getItems().get(30);
		updReq.setOctSelCol(cell.getColumn());
		updReq.setOctSelColIndex(cell.getColumnIndex());
		updReq.setOctSelTrueParsePatternId(cell.getSelectedPatternId());
	
		// Coverage November Selected
		cell = dfcovDataListView.getItems().get(31);
		updReq.setNovSelCol(cell.getColumn());
		updReq.setNovSelColIndex(cell.getColumnIndex());
		updReq.setNovSelTrueParsePatternId(cell.getSelectedPatternId());
	
		// Coverage December Selected
		cell = dfcovDataListView.getItems().get(32);
		updReq.setDecSelCol(cell.getColumn());
		updReq.setDecSelColIndex(cell.getColumnIndex());
		updReq.setDecSelTrueParsePatternId(cell.getSelectedPatternId());

		// PlanIdentifier
		cell = dfcovDataListView.getItems().get(33);
		updReq.setPlanRefCol(cell.getColumn());
		updReq.setPlanRefColIndex(cell.getColumnIndex());
		updReq.setPlanRefParsePatternId(cell.getSelectedPatternId());

		// Deduction
		cell = dfcovDataListView.getItems().get(34);
		updReq.setDedCol(cell.getColumn());
		updReq.setDedColIndex(cell.getColumnIndex());
		
		// MemberShare
		cell = dfcovDataListView.getItems().get(35);
		updReq.setMbrShareCol(cell.getColumn());
		updReq.setMbrShareColIndex(cell.getColumnIndex());
		
		// Coverage Tax Year
		cell = dfcovDataListView.getItems().get(36);
		updReq.setTyCol(cell.getColumn());
		updReq.setTyColIndex(cell.getColumnIndex());
		updReq.setTyParsePatternId(cell.getSelectedPatternId());
	
	// LISTVIEW 2 FIELDS
		// STREET NUMBER
		cell = dfcovDataList2View.getItems().get(2);
		updReq.setStreetNumCol(cell.getColumn());
		updReq.setStreetNumColIndex(cell.getColumnIndex());
		updReq.setStreetParsePatternId(cell.getSelectedPatternId());
		
		// ADDRESS LINE 1
		cell = dfcovDataList2View.getItems().get(3);
		updReq.setStreetCol(cell.getColumn());
		updReq.setStreetColIndex(cell.getColumnIndex());
		updReq.setStreetParsePatternId(cell.getSelectedPatternId());
		
		// ADDRESS LINE 2
		cell = dfcovDataList2View.getItems().get(4);
		updReq.setLin2Col(cell.getColumn());
		updReq.setLin2ColIndex(cell.getColumnIndex());
		
		// City
		cell = dfcovDataList2View.getItems().get(5);
		updReq.setCityCol(cell.getColumn());
		updReq.setCityColIndex(cell.getColumnIndex());
		updReq.setCityParsePatternId(cell.getSelectedPatternId());
		
		// State
		cell = dfcovDataList2View.getItems().get(6);
		updReq.setStateCol(cell.getColumn());
		updReq.setStateColIndex(cell.getColumnIndex());
		updReq.setStateParsePatternId(cell.getSelectedPatternId());
		
		// Zip
		cell = dfcovDataList2View.getItems().get(7);
		updReq.setZipCol(cell.getColumn());
		updReq.setZipColIndex(cell.getColumnIndex());
		updReq.setZipParsePatternId(cell.getSelectedPatternId());
	
		// Gender
		cell = dfcovDataList2View.getItems().get(8);
		updReq.setGenderCol(cell.getColumn());
		updReq.setGenderColIndex(cell.getColumnIndex());
	
     	// Date of Birth
		cell = dfcovDataList2View.getItems().get(9);
		updReq.setDobCol(cell.getColumn());
		updReq.setDobColIndex(cell.getColumnIndex());
		updReq.setDobFormatId(cell.getSelectedFormatId());
		
     	// Hire Date
		cell = dfcovDataList2View.getItems().get(10);
		updReq.setHireDtCol(cell.getColumn());
		updReq.setHireDtColIndex(cell.getColumnIndex());
		updReq.setHireDtFormatId(cell.getSelectedFormatId());
		
     	// Termination Date
		cell = dfcovDataList2View.getItems().get(11);
		updReq.setTermDtCol(cell.getColumn());
		updReq.setTermDtColIndex(cell.getColumnIndex());
		updReq.setTermDtFormatId(cell.getSelectedFormatId());

     	// Cvg Class
		cell = dfcovDataList2View.getItems().get(12);
		updReq.setCvgClassCol(cell.getColumn());
		updReq.setCvgClassColIndex(cell.getColumnIndex());

     	// Job Title
		cell = dfcovDataList2View.getItems().get(13);
		updReq.setJobTtlCol(cell.getColumn());
		updReq.setJobTtlColIndex(cell.getColumnIndex());

		// SEC LITE ID
		cell = dfcovDataList2View.getItems().get(14);
		updReq.setSecEtcRefCol(cell.getColumn());
		updReq.setSecEtcRefColIndex(cell.getColumnIndex());

		// SECONDARY SSN
		cell = dfcovDataList2View.getItems().get(15);
		updReq.setSecSSNCol(cell.getColumn());
		updReq.setSecSSNColIndex(cell.getColumnIndex());
		updReq.setSecSSNParsePatternId(cell.getSelectedPatternId());
		
		// SECONDARY IDENTIFIER
		cell = dfcovDataList2View.getItems().get(16);
		updReq.setSecErRefCol(cell.getColumn());
		updReq.setSecErRefColIndex(cell.getColumnIndex());
		updReq.setSecErRefParsePatternId(cell.getSelectedPatternId());
		
		// MEMBER NUMBER
		cell = dfcovDataList2View.getItems().get(17);
		updReq.setMbrCol(cell.getColumn());
		updReq.setMbrColIndex(cell.getColumnIndex());
			
		// SECONDARY FIRST NAME
		cell = dfcovDataList2View.getItems().get(19);
		updReq.setSecFNameCol(cell.getColumn());
		updReq.setSecFNameColIndex(cell.getColumnIndex());
		updReq.setSecFNameParsePatternId(cell.getSelectedPatternId());
		
     	// SECONDARY MIDDLE NAME
		cell = dfcovDataList2View.getItems().get(20);
		updReq.setSecMNameCol(cell.getColumn());
		updReq.setSecMNameColIndex(cell.getColumnIndex());
		updReq.setSecMNameParsePatternId(cell.getSelectedPatternId());

		// SECONDARY LAST NAME
		cell = dfcovDataList2View.getItems().get(21);
		updReq.setSecLNameCol(cell.getColumn());
		updReq.setSecLNameColIndex(cell.getColumnIndex());
		updReq.setSecLNameParsePatternId(cell.getSelectedPatternId());

		// SECONDARY STREET NUMBER
		cell = dfcovDataList2View.getItems().get(22);
		updReq.setSecStreetNumCol(cell.getColumn());
		updReq.setSecStreetNumColIndex(cell.getColumnIndex());
		updReq.setSecStreetParsePatternId(cell.getSelectedPatternId());
		
		// SECONDARY ADDRESS LINE 1
		cell = dfcovDataList2View.getItems().get(23);
		updReq.setSecStreetCol(cell.getColumn());
		updReq.setSecStreetColIndex(cell.getColumnIndex());
		updReq.setSecStreetParsePatternId(cell.getSelectedPatternId());
		
		// SECONDARY ADDRESS LINE 2
		cell = dfcovDataList2View.getItems().get(24);
		updReq.setSecLin2Col(cell.getColumn());
		updReq.setSecLin2ColIndex(cell.getColumnIndex());
		
		// SECONDARY City
		cell = dfcovDataList2View.getItems().get(25);
		updReq.setSecCityCol(cell.getColumn());
		updReq.setSecCityColIndex(cell.getColumnIndex());
		updReq.setSecCityParsePatternId(cell.getSelectedPatternId());
		
		// SECONDARY State
		cell = dfcovDataList2View.getItems().get(26);
		updReq.setSecStateCol(cell.getColumn());
		updReq.setSecStateColIndex(cell.getColumnIndex());
		updReq.setSecStateParsePatternId(cell.getSelectedPatternId());
		
		// SECONDARY Zip
		cell = dfcovDataList2View.getItems().get(27);
		updReq.setSecZipCol(cell.getColumn());
		updReq.setSecZipColIndex(cell.getColumnIndex());
		updReq.setSecZipParsePatternId(cell.getSelectedPatternId());
	
     	// SECONDARY Date of Birth
		cell = dfcovDataList2View.getItems().get(28);
		updReq.setSecDOBCol(cell.getColumn());
		updReq.setSecDOBColIndex(cell.getColumnIndex());
		updReq.setSecDOBFormatId(cell.getSelectedFormatId());
		
    	// Custom Field 1
		cell = dfcovDataList2View.getItems().get(30);
		updReq.setCfld1Name(cell.getName());
		updReq.setCfld1Col(cell.getColumn());
		updReq.setCfld1ColIndex(cell.getColumnIndex());
		updReq.setCfld1ParsePatternId(cell.getSelectedPatternId());

     	// Custom Field 2
		cell = dfcovDataList2View.getItems().get(31);
		updReq.setCfld2Name(cell.getName());
		updReq.setCfld2Col(cell.getColumn());
		updReq.setCfld2ColIndex(cell.getColumnIndex());
		updReq.setCfld2ParsePatternId(cell.getSelectedPatternId());

     	// Custom Field 3
		cell = dfcovDataList2View.getItems().get(32);
		updReq.setCfld3Name(cell.getName());
		updReq.setCfld3Col(cell.getColumn());
		updReq.setCfld3ColIndex(cell.getColumnIndex());

     	// Custom Field 4
		cell = dfcovDataList2View.getItems().get(33);
		updReq.setCfld4Name(cell.getName());
		updReq.setCfld4Col(cell.getColumn());
		updReq.setCfld4ColIndex(cell.getColumnIndex());

     	// Custom Field 5
		cell = dfcovDataList2View.getItems().get(34);
		updReq.setCfld5Name(cell.getName());
		updReq.setCfld5Col(cell.getColumn());
		updReq.setCfld5ColIndex(cell.getColumnIndex());

     	// Secondary Custom Field 1
		cell = dfcovDataList2View.getItems().get(36);
		updReq.setSecCfld1Name(cell.getName());
		updReq.setSecCfld1Col(cell.getColumn());
		updReq.setSecCfld1ColIndex(cell.getColumnIndex());
		updReq.setSecCfld1ParsePatternId(cell.getSelectedPatternId());

     	// Secondary Custom Field 2
		cell = dfcovDataList2View.getItems().get(37);
		updReq.setSecCfld2Name(cell.getName());
		updReq.setSecCfld2Col(cell.getColumn());
		updReq.setSecCfld2ColIndex(cell.getColumnIndex());
		updReq.setSecCfld2ParsePatternId(cell.getSelectedPatternId());

     	// Secondary Custom Field 3
		cell = dfcovDataList2View.getItems().get(38);
		updReq.setSecCfld3Name(cell.getName());
		updReq.setSecCfld3Col(cell.getColumn());
		updReq.setSecCfld3ColIndex(cell.getColumnIndex());

     	// Secondary Custom Field 4
		cell = dfcovDataList2View.getItems().get(39);
		updReq.setSecCfld4Name(cell.getName());
		updReq.setSecCfld4Col(cell.getColumn());
		updReq.setSecCfld4ColIndex(cell.getColumnIndex());

     	// Secondary Custom Field 5
		cell = dfcovDataList2View.getItems().get(40);
		updReq.setSecCfld5Name(cell.getName());
		updReq.setSecCfld5Col(cell.getColumn());
		updReq.setSecCfld5ColIndex(cell.getColumnIndex());

     	// Coverage Custom Field 1
		cell = dfcovDataList2View.getItems().get(41);
		updReq.setCovCfld1Name(cell.getName());
		updReq.setCovCfld1Col(cell.getColumn());
		updReq.setCovCfld1ColIndex(cell.getColumnIndex());
		updReq.setCovCfld1ParsePatternId(cell.getSelectedPatternId());
		
     	// Coverage Custom Field 2
		cell = dfcovDataList2View.getItems().get(42);
		updReq.setCovCfld2Name(cell.getName());
		updReq.setCovCfld2Col(cell.getColumn());
		updReq.setCovCfld2ColIndex(cell.getColumnIndex());
		
	}
*/
/*	private void updatePayFile(){
		//reset the updateRequest object if it has been used
		DataManager.i().mUpdateDynamicPayFileSpecificationRequest = null;			
		DataManager.i().mUpdateDynamicPayFileSpecificationRequest = new UpdateDynamicPayFileSpecificationRequest();
		// local for convenience
		UpdateDynamicPayFileSpecificationRequest request = DataManager.i().mUpdateDynamicPayFileSpecificationRequest;
		//update the request object with the coverage file
		request.setDynamicPayFileSpecification((DataManager.i().mDynamicPayFileSpecification));

		//gather the fields
		request.setName(dfcovNameLabel.getText());
		request.setDescription(dfcovDescriptionLabel.getText());
		request.setTabIndex(Integer.valueOf(dfcovTabIndexLabel.getText()));
		request.setHeaderRowIndex(Integer.valueOf(dfcovHeaderRowIndexLabel.getText()));
		request.setMapEEtoER(dfcovMapEEtoERCheck.isSelected());
		request.setCreateEmployee(dfcovCreateEmployeeCheck.isSelected());
		// NOTE: We are repurposing the createSecondary control here for pay period
		request.setCreatePayPeriod(dfcovCreateSecondaryCheck.isSelected());
		request.setSkipLastRow(dfcovSkipLastRowCheck.isSelected());
	
		//update the column field table
		saveFields(request);	        
	}
	
	private void saveFields(UpdateDynamicPayFileSpecificationRequest request) {
		// LISTVIEW 1 DATA
     	// EMPLOYER IDENTIFIER
		HBoxDetailCell cell = dfcovDataListView.getItems().get(2);
		request.setErCol(cell.getColumn());
		request.setErColIndex(cell.getColumnIndex());
		
     	// EMPLOYEE SSN
		cell = dfcovDataListView.getItems().get(3);
		request.setSsnCol(cell.getColumn());
		request.setSsnColIndex(cell.getColumnIndex());
		request.setSsnParsePatternId(cell.getSelectedPatternId());
		
     	// EMPLOYEE IDENTIFIER
		cell = dfcovDataListView.getItems().get(4);
		request.setErRefCol(cell.getColumn());
		request.setErRefFldIndex(cell.getColumnIndex());
		request.setErRefParsePatternId(cell.getSelectedPatternId());
		
     	// FIRST NAME
		cell = dfcovDataListView.getItems().get(5);
		request.setfNameCol(cell.getColumn());
		request.setfNameColIndex(cell.getColumnIndex());
		request.setfNameParsePatternId(cell.getSelectedPatternId());
		
     	// MIDDLE NAME
		cell = dfcovDataListView.getItems().get(6);
		request.setmNameCol(cell.getColumn());
		request.setmNameColIndex(cell.getColumnIndex());
		request.setmNameParsePatternId(cell.getSelectedPatternId());

		// LAST NAME
		cell = dfcovDataListView.getItems().get(7);
		request.setlNameCol(cell.getColumn());
		request.setlNameColIndex(cell.getColumnIndex());
		request.setlNameParsePatternId(cell.getSelectedPatternId());

		// Date of Birth
		cell = dfcovDataListView.getItems().get(9);
		request.setDobCol(cell.getColumn());
		request.setDobColIndex(cell.getColumnIndex());
		request.setDobFormatId(cell.getSelectedFormatId());
		
     	// Hire Date
		cell = dfcovDataListView.getItems().get(10);
		request.setHireDtCol(cell.getColumn());
		request.setHireDtColIndex(cell.getColumnIndex());
		request.setHireDtFormatId(cell.getSelectedFormatId());
		
     	// Rehire Date
		cell = dfcovDataListView.getItems().get(11);
		request.setRhireDtCol(cell.getColumn());
		request.setRhireDtColIndex(cell.getColumnIndex());
		request.setRhireDtFormatId(cell.getSelectedFormatId());
		
     	// Termination Date
		cell = dfcovDataListView.getItems().get(12);
		request.setTermDtCol(cell.getColumn());
		request.setTermDtColIndex(cell.getColumnIndex());
		request.setTermDtFormatId(cell.getSelectedFormatId());
		
     	// Cvg Class
		cell = dfcovDataListView.getItems().get(13);
		request.setCvgClassCol(cell.getColumn());
		request.setCvgClassColIndex(cell.getColumnIndex());

     	// Job Title
		cell = dfcovDataListView.getItems().get(14);
		request.setJobTtlCol(cell.getColumn());
		request.setJobTtlColIndex(cell.getColumnIndex());

		// Department
		cell = dfcovDataListView.getItems().get(15);
		request.setDeptCol(cell.getColumn());
		request.setDeptColIndex(cell.getColumnIndex());

		// Location
		cell = dfcovDataListView.getItems().get(16);
		request.setLocCol(cell.getColumn());
		request.setLocColIndex(cell.getColumnIndex());

		// Pay Period Start Date
		cell = dfcovDataListView.getItems().get(17);
		request.setPpdStartDtCol(cell.getColumn());
		request.setPpdStartDtColIndex(cell.getColumnIndex());
		request.setPpdStartDtParsePatternId(cell.getSelectedPatternId());
		request.setPpdStartDtFormatId(cell.getSelectedFormatId());
		
     	// Pay Period End Date
		cell = dfcovDataListView.getItems().get(18);
		request.setPpdEndDtCol(cell.getColumn());
		request.setPpdEndDtColIndex(cell.getColumnIndex());
		request.setPpdEndDtParsePatternId(cell.getSelectedPatternId());
		request.setPpdEndDtFormatId(cell.getSelectedFormatId());
		
     	// Pay Date
		cell = dfcovDataListView.getItems().get(19);
		request.setPayDtCol(cell.getColumn());
		request.setPayDtColIndex(cell.getColumnIndex());
		request.setPayDtParsePatternId(cell.getSelectedPatternId());
		request.setPayDtFormatId(cell.getSelectedFormatId());
		
     	// Work Date
		cell = dfcovDataListView.getItems().get(20);
		request.setWorkDtCol(cell.getColumn());
		request.setWorkDtColIndex(cell.getColumnIndex());
		request.setWorkDtParsePatternId(cell.getSelectedPatternId());
		request.setWorkDtFormatId(cell.getSelectedFormatId());
		
     	// Payperiod Identifier
		cell = dfcovDataListView.getItems().get(22);
		request.setPpdErRefCol(cell.getColumn());
		request.setPpdErRefColIndex(cell.getColumnIndex());
		
     	// Payperiod Frequency
		cell = dfcovDataListView.getItems().get(23);
		request.setPpdFreqCol(cell.getColumn());
		request.setPpdFreqColIndex(cell.getColumnIndex());
		
     	// Hours
		cell = dfcovDataListView.getItems().get(24);
		request.setHrsCol(cell.getColumn());
		request.setHrsColIndex(cell.getColumnIndex());

     	// Rate
		cell = dfcovDataListView.getItems().get(25);
		request.setRateCol(cell.getColumn());
		request.setRateColIndex(cell.getColumnIndex());

     	// Amount
		cell = dfcovDataListView.getItems().get(26);
		request.setAmtCol(cell.getColumn());
		request.setAmtColIndex(cell.getColumnIndex());

     	// Pay Code
		cell = dfcovDataListView.getItems().get(27);
		request.setPayCodeCol(cell.getColumn());
		request.setPayCodeColIndex(cell.getColumnIndex());
		
     	// Union Type
		cell = dfcovDataListView.getItems().get(28);
		request.setUnionTypeCol(cell.getColumn());
		request.setUnionTypeColIndex(cell.getColumnIndex());
		
     	// Pay Class
		cell = dfcovDataListView.getItems().get(29);
		request.setPayClassCol(cell.getColumn());
		request.setPayClassColIndex(cell.getColumnIndex());
		
     	// Custom Field 1
		cell = dfcovDataListView.getItems().get(31);
		request.setCfld1Name(cell.getName());
		request.setCfld1Col(cell.getColumn());
		request.setCfld1ColIndex(cell.getColumnIndex());
		request.setCfld1ParsePatternId(cell.getSelectedPatternId());

     	// Custom Field 2
		cell = dfcovDataListView.getItems().get(32);
		request.setCfld2Name(cell.getName());
		request.setCfld2Col(cell.getColumn());
		request.setCfld2ColIndex(cell.getColumnIndex());
		request.setCfld2ParsePatternId(cell.getSelectedPatternId());

     	// Custom Field 3
		cell = dfcovDataListView.getItems().get(33);
		request.setCfld3Name(cell.getName());
		request.setCfld3Col(cell.getColumn());
		request.setCfld3ColIndex(cell.getColumnIndex());

     	// Custom Field 4
		cell = dfcovDataListView.getItems().get(34);
		request.setCfld4Name(cell.getName());
		request.setCfld4Col(cell.getColumn());
		request.setCfld4ColIndex(cell.getColumnIndex());

     	// Custom Field 5
		cell = dfcovDataListView.getItems().get(35);
		request.setCfld5Name(cell.getName());
		request.setCfld5Col(cell.getColumn());
		request.setCfld5ColIndex(cell.getColumnIndex());
		
		// LISTVIEW 2 DATA
		
		// STREET NUMBER
		cell = dfcovDataList2View.getItems().get(2);
		request.setStreetNumCol(cell.getColumn());
		request.setStreetNumColIndex(cell.getColumnIndex());
		request.setStreetParsePatternId(cell.getSelectedPatternId());
		
		// ADDRESS LINE 1
		cell = dfcovDataList2View.getItems().get(3);
		request.setStreetCol(cell.getColumn());
		request.setStreetColIndex(cell.getColumnIndex());
		request.setStreetParsePatternId(cell.getSelectedPatternId());
		
		// ADDRESS LINE 2
		cell = dfcovDataList2View.getItems().get(4);
		request.setLin2Col(cell.getColumn());
		request.setLin2ColIndex(cell.getColumnIndex());
		
		// City
		cell = dfcovDataList2View.getItems().get(5);
		request.setCityCol(cell.getColumn());
		request.setCityColIndex(cell.getColumnIndex());
		request.setCityParsePatternId(cell.getSelectedPatternId());
		
		// State
		cell = dfcovDataList2View.getItems().get(6);
		request.setStateCol(cell.getColumn());
		request.setStateColIndex(cell.getColumnIndex());
		request.setStateParsePatternId(cell.getSelectedPatternId());
		
		// Zip
		cell = dfcovDataList2View.getItems().get(7);
		request.setZipCol(cell.getColumn());
		request.setZipColIndex(cell.getColumnIndex());
		request.setZipParsePatternId(cell.getSelectedPatternId());
		
     	// Overtime Hours
		cell = dfcovDataList2View.getItems().get(9);
		request.setOtHrsCol(cell.getColumn());
		request.setOtHrsColIndex(cell.getColumnIndex());

     	// Overtime 2 Hours
		cell = dfcovDataList2View.getItems().get(10);
		request.setOtHrs2Col(cell.getColumn());
		request.setOtHrs2ColIndex(cell.getColumnIndex());

     	// Overtime 3 Hours
		cell = dfcovDataList2View.getItems().get(11);
		request.setOtHrs3Col(cell.getColumn());
		request.setOtHrs3ColIndex(cell.getColumnIndex());

     	// Paid Time Off Hours
		cell = dfcovDataList2View.getItems().get(12);
		request.setPtoHrsCol(cell.getColumn());
		request.setPtoHrsColIndex(cell.getColumnIndex());

     	// Sick Pay Hours
		cell = dfcovDataList2View.getItems().get(13);
		request.setSickHrsCol(cell.getColumn());
		request.setSickHrsColIndex(cell.getColumnIndex());

     	// Holiday Hours
		cell = dfcovDataList2View.getItems().get(14);
		request.setHolidayHrsCol(cell.getColumn());
		request.setHolidayHrsColIndex(cell.getColumnIndex());

		// Custom Pay Field 1
		cell = dfcovDataList2View.getItems().get(16);
		request.setPayCfld1Name(cell.getName());
		request.setPayCfld1Col(cell.getColumn());
		request.setPayCfld1ColIndex(cell.getColumnIndex());
		request.setPayCfld1ParsePatternId(cell.getSelectedPatternId());

     	// Custom Pay Field 2
		cell = dfcovDataList2View.getItems().get(17);
		request.setPayCfld2Name(cell.getName());
		request.setPayCfld2Col(cell.getColumn());
		request.setPayCfld2ColIndex(cell.getColumnIndex());
		request.setPayCfld2ParsePatternId(cell.getSelectedPatternId());

     	// Custom Pay Field 3
		cell = dfcovDataList2View.getItems().get(18);
		request.setPayCfld3Name(cell.getName());
		request.setPayCfld3Col(cell.getColumn());
		request.setPayCfld3ColIndex(cell.getColumnIndex());

     	// Custom Pay Field 4
		cell = dfcovDataList2View.getItems().get(19);
		request.setPayCfld4Name(cell.getName());
		request.setPayCfld4Col(cell.getColumn());
		request.setPayCfld4ColIndex(cell.getColumnIndex());

     	// Custom Pay Field 5
		cell = dfcovDataList2View.getItems().get(20);
		request.setPayCfld5Name(cell.getName());
		request.setPayCfld5Col(cell.getColumn());
		request.setPayCfld5ColIndex(cell.getColumnIndex());

     	// Custom Pay Field 6
		cell = dfcovDataList2View.getItems().get(21);
		request.setPayCfld6Name(cell.getName());
		request.setPayCfld6Col(cell.getColumn());
		request.setPayCfld6ColIndex(cell.getColumnIndex());

     	// Custom Pay Field 7
		cell = dfcovDataList2View.getItems().get(22);
		request.setPayCfld7Name(cell.getName());
		request.setPayCfld7Col(cell.getColumn());
		request.setPayCfld7ColIndex(cell.getColumnIndex());	
	} */
	//Not currently used 10/29/2020
	private void updatePayPeriodFile(){

		DynamicPayPeriodFileSpecification fSpec = DataManager.i().mDynamicPayPeriodFileSpecification;
		if (fSpec != null) {
			readOnly = fSpec.isLocked();
			fSpec.setName(dfcovNameLabel.getText());
			//fSpec.setDescription(dfcovDescriptionLabel.getText());
			fSpec.setTabIndex(Integer.valueOf(dfcovTabIndexLabel.getText()));
			fSpec.setHeaderRowIndex(Integer.valueOf(dfcovHeaderRowIndexLabel.getText()));
			
			//update the column field table
			//saveFields(fSpec);	        
		}
	}
	
/*	private void saveFields(DynamicPayPeriodFileSpecification fSpec) {
		// LISTVIEW 1
     	// PAY PERIOD IDENTIFIER
		HBoxDetailCell cell = dfcovDataListView.getItems().get(2);
		fSpec.setErRefCol(cell.getColumn());
		fSpec.setErRefColIndex(cell.getColumnIndex());
		fSpec.setErRefParsePatternId(cell.getSelectedPatternId());
		
     	// START DATE
		cell = dfcovDataListView.getItems().get(3);
		fSpec.setStartDtCol(cell.getColumn());
		fSpec.setStartDtColIndex(cell.getColumnIndex());
		fSpec.setStartDtFormat(DataManager.i().getParseDateFormat(cell.getFormatName()));
		
     	// END DATE
		cell = dfcovDataListView.getItems().get(4);
		fSpec.setEndDtCol(cell.getColumn());
		fSpec.setEndDtColIndex(cell.getColumnIndex());
		fSpec.setEndDtFormat(DataManager.i().getParseDateFormat(cell.getFormatName()));
		
     	// PAY DATE
		cell = dfcovDataListView.getItems().get(5);
		fSpec.setPayDtCol(cell.getColumn());
		fSpec.setPayDtColIndex(cell.getColumnIndex());
		fSpec.setPayDtFormat(DataManager.i().getParseDateFormat(cell.getFormatName()));
		
		// LISTVIEW 2
     	// Custom Field 1
		cell = dfcovDataList2View.getItems().get(2);
		fSpec.setCfld1Name(cell.getName());
		fSpec.setCfld1Col(cell.getColumn());
		fSpec.setCfld1ColIndex(cell.getColumnIndex());
		fSpec.setCfld1ParsePatternId(cell.getSelectedPatternId());

     	// Custom Field 2
		cell = dfcovDataList2View.getItems().get(3);
		fSpec.setCfld2Name(cell.getName());
		fSpec.setCfld2Col(cell.getColumn());
		fSpec.setCfld2ColIndex(cell.getColumnIndex());
		fSpec.setCfld2ParsePatternId(cell.getSelectedPatternId());

     	// Custom Field 3
		cell = dfcovDataList2View.getItems().get(4);
		fSpec.setCfld3Name(cell.getName());
		fSpec.setCfld3Col(cell.getColumn());
		fSpec.setCfld3ColIndex(cell.getColumnIndex());

     	// Custom Field 4
		cell = dfcovDataList2View.getItems().get(5);
		fSpec.setCfld4Name(cell.getName());
		fSpec.setCfld4Col(cell.getColumn());
		fSpec.setCfld4ColIndex(cell.getColumnIndex());

     	// Custom Field 5
		cell = dfcovDataList2View.getItems().get(6);
		fSpec.setCfld5Name(cell.getName());
		fSpec.setCfld5Col(cell.getColumn());
		fSpec.setCfld5ColIndex(cell.getColumnIndex());
	} */
	//Not currently used 10/29/2020
/*	private void updatePlanFile(){

		DynamicPlanFileSpecification fSpec = DataManager.i().mDynamicPlanFileSpecification;
		if (fSpec != null) {
			readOnly = fSpec.isLocked();
			fSpec.setName(dfcovNameLabel.getText());
			fSpec.setDescription(dfcovDescriptionLabel.getText());
			fSpec.setTabIndex(Integer.valueOf(dfcovTabIndexLabel.getText()));
			fSpec.setHeaderRowIndex(Integer.valueOf(dfcovHeaderRowIndexLabel.getText()));
			
			//save the table fields
			saveFields(fSpec);	        
		}
	}
*/
/*	private void saveFields(DynamicPlanFileSpecification fSpec) {
		// LISTVIEW 1
     	// PLAN IDENTIFIER
		HBoxDetailCell cell = dfcovDataListView.getItems().get(2);
		fSpec.setPlanRefCol(cell.getColumn());
		fSpec.setPlanRefColIndex(cell.getColumnIndex());
		fSpec.setPlanRefParsePattern(DataManager.i().getParsePattern(cell.getPatternName()));
		
		// PLAN NAME
     	cell = dfcovDataListView.getItems().get(3);
		fSpec.setNmCol(cell.getColumn());
		fSpec.setNmColIndex(cell.getColumnIndex());
		
		// PLAN DESCRIPTION
     	cell = dfcovDataListView.getItems().get(4);
		fSpec.setDescCol(cell.getColumn());
		fSpec.setDescColIndex(cell.getColumnIndex());
		
		// LISTVIEW 2
		// PLAN TYPE
     	cell = dfcovDataList2View.getItems().get(2);
		fSpec.setPlanTypeCol(cell.getColumn());
		fSpec.setPlanTypeColIndex(cell.getColumnIndex());
		
     	// PLAN WAVED
		cell = dfcovDataList2View.getItems().get(3);
		fSpec.setWavdCol(cell.getColumn());
		fSpec.setWavdColIndex(cell.getColumnIndex());
		fSpec.setWavdTrueParsePattern(DataManager.i().getParsePattern(cell.getPatternName()));
		
     	// PLAN INEL
		cell = dfcovDataList2View.getItems().get(4);
		fSpec.setInelCol(cell.getColumn());
		fSpec.setInelColIndex(cell.getColumnIndex());
		fSpec.setInelTrueParsePattern(DataManager.i().getParsePattern(cell.getPatternName()));
	}
*/
	public void setCoverageFile(int nPipelineSpecificationID) {
		
		DataManager.i().loadDynamicCoverageFileSpecification(nPipelineSpecificationID);
		updateFileData();
	}
	
	private void saveCoverageFileSpecification() {
		// save the request
//		DataManager.i().saveCurrentDynamicCoverageFileSpecificationRequest();
	}
	
	private void saveEmployeeFileSpecification() {
		// save the request
		// this was a no-op in DataMaanger since the conversion
		//DataManager.i().saveCurrentDynamicEmployeeFileSpecificationRequest();
	}
	
	private void savePayFileSpecification() {
		// Pay Class
//		for (int i = 0; i < selectedPayClassRefs.size(); i++)
//			DataManager.i().AddDynamicPayFilePayClassReference(selectedPayClassRefs.get(i));

		// save the request
		//FIXME: DataManager.i().saveCurrentDynamicPayFileSpecificationRequest();
	}
	
	private void loadIgnoreRows(DynamicCoverageFileSpecification fSpec) {
		if (fSpec != null && fSpec.getIgnoreRowSpecifications() != null && fSpec.getIgnoreRowSpecifications().size() > 0)
		{
		    List<HBoxIgnoreRowCell> ignoreRowList = new ArrayList<>();

			for (DynamicCoverageFileIgnoreRowSpecification igSpec: fSpec.getIgnoreRowSpecifications()) {
				if (igSpec == null) continue;
				
				//if (igSpec.get .getName() == null)
				//	igSpec = DataManager.i().getDynamicCoverageFileIgnoreRowSpecification(igSpec);
			
				if (igSpec.isActive() == false) continue;				
				ignoreRowList.add(new HBoxIgnoreRowCell(igSpec,null,null));
			};	
			
			ObservableList<HBoxIgnoreRowCell> myObservableDepartmentList = FXCollections.observableList(ignoreRowList);
			dfcovIgnoreRowSpecsListView.setItems(myObservableDepartmentList);		
		}
	}
	
	private void loadIgnoreRows(DynamicEmployeeFileSpecification fSpec) {
		if (fSpec != null && fSpec.getIgnoreRowSpecifications() != null && fSpec.getIgnoreRowSpecifications().size() > 0)
		{
		    List<HBoxIgnoreRowCell> ignoreRowList = new ArrayList<>();
			for (DynamicEmployeeFileIgnoreRowSpecification igSpec: fSpec.getIgnoreRowSpecifications()) {
				if (igSpec == null) continue;
				
				if (igSpec.getName() == null)
				//	igSpec = DataManager.i().getDynamicEmployeeFileIgnoreRowSpecification(igSpec);		
		
				if (igSpec.isActive() == false) continue;				
				ignoreRowList.add(new HBoxIgnoreRowCell(null,igSpec,null));
			};	
			
			ObservableList<HBoxIgnoreRowCell> myObservableDepartmentList = FXCollections.observableList(ignoreRowList);
			dfcovIgnoreRowSpecsListView.setItems(myObservableDepartmentList);		
		}
	}
	
	private void loadIgnoreRows(DynamicPayFileSpecification fSpec) {
		dfcovIgnoreRowSpecsListView.getItems().clear();
		if (fSpec != null && fSpec.getIgnoreRowSpecifications() != null && fSpec.getIgnoreRowSpecifications().size() > 0)
		{
		    List<HBoxIgnoreRowCell> ignoreRowList = new ArrayList<>();
			for (DynamicPayFileIgnoreRowSpecification igSpec: fSpec.getIgnoreRowSpecifications()) {
				if (igSpec == null) continue;
				
			//	if (igSpec.getName() == null)
			//		igSpec = DataManager.i().getDynamicPayFileIgnoreRowSpecification(igSpec);
				
				if (igSpec.isActive() == false) continue;				
				ignoreRowList.add(new HBoxIgnoreRowCell(null,null,igSpec));
			};	
			
			ObservableList<HBoxIgnoreRowCell> myObservableDepartmentList = FXCollections.observableList(ignoreRowList);
			dfcovIgnoreRowSpecsListView.setItems(myObservableDepartmentList);		
		}
	}
	
	
	private void loadRefTableForm(int refType) {
		// check for independent lref forms
		switch(refType) {
		case 1:
			loadEmployerRefForm();
			return;
		case 2:
			loadGenderRefForm();
			return;		
		case 3:
			loadDepartmentRefForm();
			return;		
		case 4:
			loadLocationRefForm();
			return;		
		case 5:
			loadPayCodeTypeRefForm();
			return;
		case 6:
			loadPayFreqTypeRefForm();
			return;
		case 7:
			loadUnionTypeRefForm();
			return;
		case 8:
			loadPayClassRefForm();
			return;		
		case 9:
			loadCoverageClassRefForm();
			return;		
		}
	}
	
	private void loadEmployerRefForm() 
	{
		try {
        	// load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationRef.fxml"));
			Parent ControllerNode = loader.load();
	        ViewDynamicFileSpecificationRefController refController = (ViewDynamicFileSpecificationRefController) loader.getController();

	       // refController.setFileType(fileType);
	        refController.load();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        stage.onCloseRequestProperty().setValue(e -> refreshRefs());
	        stage.onHiddenProperty().setValue(e -> refreshRefs());
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}        
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}

	private void loadGenderRefForm() {
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationGenderTypeRef.fxml"));
			Parent ControllerNode = loader.load();
	        ViewDynamicFileSpecificationGenderTypeRefController refController = (ViewDynamicFileSpecificationGenderTypeRefController) loader.getController();
	        refController.setFileType(fileType);
	        refController.load();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        stage.onCloseRequestProperty().setValue(e -> refreshRefs());
	        stage.onHiddenProperty().setValue(e -> refreshRefs());
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if (refController.changesMade == true) 
	        	refreshRefs();
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void loadDepartmentRefForm() {
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationDepartmentRef.fxml"));
			Parent ControllerNode = loader.load();
	        ViewDynamicFileSpecificationDepartmentRefController refController = (ViewDynamicFileSpecificationDepartmentRefController) loader.getController();

	        refController.setFileType(fileType);
	        refController.load();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        stage.onCloseRequestProperty().setValue(e -> refreshRefs());
	        stage.onHiddenProperty().setValue(e -> refreshRefs());
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if (refController.changesMade == true) 
	        	refreshRefs();
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void loadPayCodeTypeRefForm() {
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationPayCodeTypeRef.fxml"));
			Parent ControllerNode = loader.load();
	        ViewDynamicFileSpecificationPayCodeTypeRefController refController = (ViewDynamicFileSpecificationPayCodeTypeRefController) loader.getController();

	       // refController.setFileType(fileType);
	        refController.load();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        stage.onCloseRequestProperty().setValue(e -> refreshRefs());
	        stage.onHiddenProperty().setValue(e -> refreshRefs());
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if (refController.changesMade == true) 
	        	refreshRefs();
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void loadPayClassRefForm() {
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationPayClassRef.fxml"));
			Parent ControllerNode = loader.load();
	        ViewDynamicFileSpecificationPayClassRefController refController = (ViewDynamicFileSpecificationPayClassRefController) loader.getController();

	        refController.setSpecController(this);
	        refController.load();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        stage.onCloseRequestProperty().setValue(e -> refreshRefs());
	        stage.onHiddenProperty().setValue(e -> refreshRefs());
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        // update the reference count on the cell
			HBoxDetailCell cell = dfcovDataListView.getItems().get(29);
			cell.setRefCount(selectedPayClassRefs.size());
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void loadLocationRefForm() {
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationLocationRef.fxml"));
			Parent ControllerNode = loader.load();
	        ViewDynamicFileSpecificationLocationRefController refController = (ViewDynamicFileSpecificationLocationRefController) loader.getController();

	        //refController.setFileType(fileType);
	        refController.load();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        stage.onCloseRequestProperty().setValue(e -> refreshRefs());
	        stage.onHiddenProperty().setValue(e -> refreshRefs());
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if (refController.changesMade == true) 
	        	refreshRefs();
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void loadPayFreqTypeRefForm() {
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationPayFrequencyTypeRef.fxml"));
			Parent ControllerNode = loader.load();
	        ViewDynamicFileSpecificationPayFrequencyTypeRefController refController = (ViewDynamicFileSpecificationPayFrequencyTypeRefController) loader.getController();
	        refController.load();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        stage.onCloseRequestProperty().setValue(e -> refreshRefs());
	        stage.onHiddenProperty().setValue(e -> refreshRefs());
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if (refController.changesMade == true) 
	        	refreshRefs();
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void loadUnionTypeRefForm() {
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationunionTypeRef.fxml"));
			Parent ControllerNode = loader.load();
	        ViewDynamicFileSpecificationUnionTypeRefController refController = (ViewDynamicFileSpecificationUnionTypeRefController) loader.getController();
	        refController.load();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        stage.onCloseRequestProperty().setValue(e -> refreshRefs());
	        stage.onHiddenProperty().setValue(e -> refreshRefs());
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if (refController.changesMade == true) 
	        	refreshRefs();
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void loadCoverageClassRefForm() 
	{
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationCoverageClassRef.fxml"));
			Parent ControllerNode = loader.load();
	        ViewDynamicFileSpecificationCoverageClassRefController refController = (ViewDynamicFileSpecificationCoverageClassRefController) loader.getController();
	        //refController.setFileType(fileType);
	        refController.load();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        stage.onCloseRequestProperty().setValue(e -> refreshRefs());
	        stage.onHiddenProperty().setValue(e -> refreshRefs());
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if (refController.changesMade == true) 
	        	refreshRefs();
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void refreshRefs() 
	{
		/// update the reference cells
		HBoxDetailCell cell = null;
        switch(fileType){
		case COVERAGE:
	    	cell = dfcovDataListView.getItems().get(2);
	     	cell.setRefCount(getCoverageFileEmployerRefCount());
	    	cell = dfcovDataList2View.getItems().get(8);
	     	cell.setRefCount(getCoverageFileGenderTypeRefCount());
	    	cell = dfcovDataList2View.getItems().get(12);
	     	cell.setRefCount(getCoverageFileCvgClassRefCount());
			break;
		case EMPLOYEE:
	    	cell = dfcovDataListView.getItems().get(2);
	     	cell.setRefCount(getEmployeeFileEmployerRefCount());
	    	cell = dfcovDataListView.getItems().get(11);
	     	cell.setRefCount(getEmployeeFileDepartmentRefCount());
	    	cell = dfcovDataListView.getItems().get(12);
	     	cell.setRefCount(getEmployeeFileLocationRefCount());
	    	cell = dfcovDataListView.getItems().get(14);
	     	cell.setRefCount(getEmployeeFilePayCodeTypeRefCount());
	    	cell = dfcovDataList2View.getItems().get(8);
	     	cell.setRefCount(getEmployeeFileGenderTypeRefCount());
	    	cell = dfcovDataList2View.getItems().get(14);
	     	cell.setRefCount(getEmployeeFileCvgClassRefCount());
			break;
		case PAY:
	    	cell = dfcovDataListView.getItems().get(2);
	     	cell.setRefCount(getPayFileEmployerRefCount());
	    	cell = dfcovDataListView.getItems().get(15);
	     	cell.setRefCount(getPayFileDepartmentRefCount());
	    	cell = dfcovDataListView.getItems().get(16);
	     	cell.setRefCount(getPayFileLocationRefCount());
	    	cell = dfcovDataListView.getItems().get(23);
	     	cell.setRefCount(getPayFilePayFrequencyRefCount());
	    	cell = dfcovDataListView.getItems().get(27);
	     	cell.setRefCount(getPayFilePayCodeTypeRefCount());
	    	cell = dfcovDataListView.getItems().get(28);
	     	cell.setRefCount(getPayFileUnionTypeRefCount());
	    	cell = dfcovDataListView.getItems().get(13);
	     	cell.setRefCount(getPayFileCvgClassRefCount());
			break;
		default:
			break;
		}
	}

	private void clearForm()
	{
		//clear the entries on the 2 controls
		for (int i = 0; i< dfcovDataListView.getItems().size(); i++) {
			HBoxDetailCell cell = dfcovDataListView.getItems().get(i);
			if (cell.isHeader() == false) cell.clearCell();
		}
		
		for (int i = 0; i< dfcovDataList2View.getItems().size(); i++) {
			HBoxDetailCell cell = dfcovDataList2View.getItems().get(i);
			if (cell.isHeader() == false) cell.clearCell();
		}
	}
	
	@FXML
	private void onClearForm(ActionEvent event) {
		clearForm();
	}	
	
	@FXML
	private void onAddIgnoreRow(ActionEvent event)
	{
		//DataManager.i().setAddMode(true);
		//EtcAdmin.i().setScreen(ScreenType.PIPELINEROWIGNORE, true);
		switch(fileType){
		case COVERAGE:
        	DataManager.i().mDynamicCoverageFileIgnoreRowSpecification = null;
		case EMPLOYEE:
        	DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification = null;
		case PAY:
        	DataManager.i().mDynamicPayFileIgnoreRowSpecification = null;
			break;
		default:
			return;
		}	
		viewIgnoreRow();
	}	
	
	private void viewIgnoreRow() 
	{
        try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/rowignore/ViewRowIgnore.fxml"));
			Parent ControllerNode = loader.load();
	        ViewRowIgnoreController ignoreController = (ViewRowIgnoreController) loader.getController();

	        ignoreController.setSPV(spv);
	        ignoreController.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if (ignoreController.changesMade == true)
	        	loadMapperData();
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	@FXML
	private void onAddPayPeriodRules(ActionEvent event) {
		//reset the current rule, if any
		DataManager.i().mDynamicPayFilePayPeriodRule = null;
		viewPayPeriodRules();
	}	
	
	private void viewPayPeriodRules() {
        try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicPayFilePayPeriodRule.fxml"));
			Parent ControllerNode = loader.load();
	        ViewDynamicPayFilePayPeriodRuleController ruleController = (ViewDynamicPayFilePayPeriodRuleController) loader.getController();

	        ruleController.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if (ruleController.changesMade == true)
	        	loadMapperData();
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	@FXML
	private void onRemoveIgnoreSpec(ActionEvent event) {
		//verify that they want to remove the spec
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Remove Ignore Row Specification");
	    confirmAlert.setContentText("Are You Sure You Want to remove the selected Ignore row specification?");
	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();
	    if ((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	return;
	    }
	}	
	
	@FXML
	private void onAddAdditionalHours(ActionEvent event) {
		DataManager.i().mDynamicPayFileAdditionalHoursSpecification = null;
		viewAdditionalHours();
	}	

	private void viewAdditionalHours() {
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationAdditionalHours.fxml"));
			Parent ControllerNode = loader.load();
			ViewDynamicFileSpecificationAdditionalHoursController refController = (ViewDynamicFileSpecificationAdditionalHoursController) loader.getController();
	        refController.load();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        stage.onCloseRequestProperty().setValue(e -> refreshRefs());
	        stage.onHiddenProperty().setValue(e -> refreshRefs());
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if (refController.changesMade == true)
	        	loadMapperData();
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	public void loadPatternPopup(HBoxDetailCell cell) {
        try {
  			DataManager.i().mPipelineParsePattern = cell.getPattern();

  			FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewParsePatternRef.fxml"));
  			Parent ControllerNode = loader.load();
  			
  	        Stage stage = new Stage();
  	        stage.initModality(Modality.APPLICATION_MODAL);
  	        stage.initStyle(StageStyle.UNDECORATED);
  	        stage.setScene(new Scene(ControllerNode));  
  	        //stage.onCloseRequestProperty().setValue(e -> updateFileLayoutDisplay());
  	        //stage.onHiddenProperty().setValue(e -> updateFileLayoutDisplay());
	        EtcAdmin.i().positionStageCenter(stage);
 	        stage.showAndWait();

  	        cell.setParsePattern(DataManager.i().mPipelineParsePattern);
  	        updateFileLayoutDisplay();
  		} catch (IOException e) {
  			 DataManager.i().log(Level.SEVERE, e);
  		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	public void loadFormatPopup(HBoxDetailCell cell) {
        try {
  			DataManager.i().mPipelineParseDateFormat = cell.getFormat();

  			FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDateFormatRef.fxml"));
  			Parent ControllerNode = loader.load();
  			
  	        Stage stage = new Stage();
  	        stage.initModality(Modality.APPLICATION_MODAL);
  	        stage.initStyle(StageStyle.UNDECORATED);
  	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
  	        stage.showAndWait();
  	        
  	        cell.setDateFormat(DataManager.i().mPipelineParseDateFormat);
  	        updateFileLayoutDisplay();
  		} catch (IOException e) {
  			 DataManager.i().log(Level.SEVERE, e);
  		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

	}
/*	
	public class HBoxIgnoreRowCell extends HBox {
        Label lblCol1 = new Label();
        Label lblCol2 = new Label();

        HBoxIgnoreRowCell(String sCol1, String sCol2, boolean headerRow) {
             super();
          
             if (sCol1 == null ) sCol1 = "";
             if (sCol2 == null ) sCol2 = "";
             
             if (sCol1.contains("null")) sCol1 = "";
             if (sCol2.contains("null")) sCol2 = "";

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
             if (headerRow == true) {
           	  lblCol1.setTextFill(Color.GREY);
           	  lblCol2.setTextFill(Color.GREY);
             }
             
             this.getChildren().addAll(lblCol1, lblCol2);
        }
   }	
*/
	public class HBoxDetailCell extends HBox {
        Label col1Label = new Label();
        Label col2Label = new Label();
        Label col3Label = new Label();
        Label col4Label = new Label();
        Label col5Label = new Label();
        Label col6Label = new Label();
        Integer columnIndex;
        boolean headerCell;
        
  
        //need to identify the type and row for the popup form later
        int fileType;
        int refType;
        
        CheckBox columnCheck = new CheckBox();
        Separator sep1 = new Separator();
        Separator sep2 = new Separator();
        Separator sep3 = new Separator();
        Separator sep4 = new Separator();
        Separator sep5 = new Separator();
        Separator sepLeading = new Separator();

        Button refButton = new Button();
        Label refCountLabel = new Label();
        // first column
        TextField columnIndexField = new TextField();
        TextField nameField = new TextField();
        Label nameLabel = new Label();
        
        PipelineParsePattern parsePattern = null;
        Button patternButton = new Button();
        PipelineParseDateFormat dateFormat = null;
        Button formatButton = new Button();
        
        //default constructor is a header row
        HBoxDetailCell() {
            super();
            if (DataManager.i().isWindows() == true) {
	            col1Label.setMinWidth(200);
	            col1Label.setPrefWidth(200);
	            col2Label.setMinWidth(30);
	            col2Label.setPrefWidth(30);
	            col3Label.setMinWidth(50);
	            col3Label.setPrefWidth(50);
	            col4Label.setMinWidth(225);
	            col4Label.setPrefWidth(225); 
	            col5Label.setMinWidth(175);
	            col5Label.setPrefWidth(175);
	            col6Label.setMinWidth(35);
	            col6Label.setPrefWidth(35);
            }else {
	            col1Label.setMinWidth(125);
	            col1Label.setPrefWidth(125);
	            col2Label.setMinWidth(25);
	            col2Label.setPrefWidth(25);
	            col3Label.setMinWidth(40);
	            col3Label.setPrefWidth(40);
	            col4Label.setMinWidth(200);
	            col4Label.setPrefWidth(200); 
	            col5Label.setMinWidth(150);
	            col5Label.setPrefWidth(150);
	            col6Label.setMinWidth(35);
	            col6Label.setPrefWidth(35);
            }

        	col1Label.setText("Name"); 
       	    col1Label.setFont(Font.font(null, FontWeight.BOLD, 13));
       	    col1Label.setAlignment(Pos.CENTER);
          	col2Label.setText("Col"); 
       	    col2Label.setFont(Font.font(null, FontWeight.BOLD, 13));
       	    col2Label.setAlignment(Pos.CENTER);
          	col3Label.setText("Index"); 
       	    col3Label.setFont(Font.font(null, FontWeight.BOLD, 13));
       	    col3Label.setAlignment(Pos.CENTER);
          	col4Label.setText("Parse Pattern"); 
       	    col4Label.setFont(Font.font(null, FontWeight.BOLD, 13));
       	    col4Label.setAlignment(Pos.CENTER);
          	col5Label.setText("Date Format"); 
       	    col5Label.setFont(Font.font(null, FontWeight.BOLD, 13));
          	col6Label.setText("Ref."); 
       	    col5Label.setAlignment(Pos.CENTER);
       	    col6Label.setFont(Font.font(null, FontWeight.BOLD, 13));
       	    col6Label.setAlignment(Pos.CENTER);
       	    headerCell = true; 
 
       	    //set up the separators
            sep1.setMinWidth(10);
            sep1.setVisible(false);
            sep2.setMinWidth(10);
            sep2.setVisible(false);
            sep3.setMinWidth(10);
            sep3.setVisible(false);
            sep4.setMinWidth(10);
            sep4.setVisible(false);
            sep5.setMinWidth(20);
            sep5.setVisible(false);
            sepLeading.setMinWidth(10);
            sepLeading.setVisible(false);
      	    
       	    this.getChildren().addAll(sepLeading, col1Label, sep1, col2Label, sep2, col3Label, sep3, col4Label, sep4, col5Label, sep5, col6Label);
        }
        
        // header
        HBoxDetailCell(String header) {
            super();

          	col1Label.setText(header); 
            col1Label.setMinWidth(695);
            col1Label.setPrefWidth(695);
       	    col1Label.setFont(Font.font(null, FontWeight.BOLD, 13));
       	    col1Label.setAlignment(Pos.CENTER);
       	    headerCell = true;
       	    this.getChildren().addAll(col1Label);
        }
        
        HBoxDetailCell(String name, boolean column, int columnIndex, PipelineParsePattern parsePattern, 
       		 PipelineParseDateFormat dateFormat, int parseType, boolean useRef, int fileType, int refType, int refCount) {
             super();
             
             this.refType = refType;
             this.fileType = fileType;
             this.parsePattern = parsePattern;
             this.dateFormat = dateFormat;
             this.columnIndex = columnIndex;
             
             
            // String refData = refType + " " + fileType;
             refButton.setUserData(refType);
             
             //set up the separators
             headerCell = false;
             sep1.setMinWidth(10);
             sep1.setVisible(false);
             sep2.setMinWidth(10);
             sep2.setVisible(false);
             sep3.setMinWidth(10);
             sep3.setVisible(false);
             sep4.setMinWidth(10);
             sep4.setVisible(false);
             sep5.setMinWidth(20);
             sep5.setVisible(false);
             sepLeading.setMinWidth(5);
             sepLeading.setVisible(false);
             
 
             if (DataManager.i().isWindows() == true) {
                 nameField.setMinWidth(200);
                 nameField.setPrefWidth(200);
                 nameLabel.setMinWidth(200);
                 nameLabel.setPrefWidth(200);
                 columnCheck.setMinWidth(30);
                 columnCheck.setPrefWidth(30);
                 columnIndexField.setMinWidth(50);
                 columnIndexField.setPrefWidth(50);
             }else {
                 nameField.setMinWidth(125);
                 nameField.setPrefWidth(125);
                 nameLabel.setMinWidth(125);
                 nameLabel.setPrefWidth(125);
                 columnCheck.setMinWidth(25);
                 columnCheck.setPrefWidth(25);
                 columnIndexField.setMinWidth(40);
                 columnIndexField.setPrefWidth(40);
             }
            
             
             if (name == "" || name == null)
            	 nameField.setPromptText("Custom Field");
             else
            	 nameField.setText(name);
             nameLabel.setText(name);
             columnCheck.setSelected(column);
             if (column == true)
            	 columnIndexField.setText(String.valueOf(columnIndex));
             nameField.setDisable(!column);
             
             nameLabel.setStyle("-fx-padding: 5 0 0 0;-fx-font-weight: bold;");
             nameField.setStyle("-fx-padding: 5 0 0 0;-fx-font-weight: bold;");
             //columnCheck.setAlignment(Pos.CENTER_LEFT);
             columnCheck.setPadding(new Insets(5,5,5,5));

             setStyle(columnIndex);

             // enable fields accordingly to the column selection
             columnIndexField.setDisable(!column);
             refButton.setDisable(!column);
             refCountLabel.setDisable(!column);
             
             // set the controls according to the view's readonly member
             if (readOnly == true) {
            	 columnCheck.setDisable(true);
	             nameField.setEditable(false);
	             columnIndexField.setEditable(false);
	             refButton.setDisable(true);
	             refCountLabel.setDisable(true);
             }
             
             //add a listener to enable/disable other controls
             columnCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            	    @Override
            	    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            	        columnIndexField.setDisable(!newValue);
            	        nameField.setDisable(!newValue);
            	        patternButton.setDisable(!newValue);
            	        formatButton.setDisable(!newValue);
            	        refButton.setDisable(!newValue);
            	        refCountLabel.setDisable(!newValue);
            	        if (columnIndexField.getText() == "" || columnIndexField.getText().isEmpty()) {
            	        	columnIndexField.setText("0");
            	        }
            	        updateFileLayoutDisplay();
            	        setStyle(columnIndex);
            	    }
            	});
             
             //only numbers for the column index
             columnIndexField.setOnKeyTyped(e -> {
            	 if (e.getCharacter() != null && e.getCharacter().isEmpty() == false) {
	                 char input = e.getCharacter().charAt(0);
	                 if (Character.isDigit(input) != true) {
	                     e.consume();
	                 }
            	 }
             });
             
             columnIndexField.textProperty().addListener((observable, oldValue, newValue) -> {
            	 if (newValue.length() > 0) {	
            		 Integer value = Integer.valueOf(newValue);
            		 if (value > 99) {
            			 value = 99;
            			 columnIndexField.setText("99");
            		 }
            		 
            		 this.columnIndex = value;
            	 }
             });
             
             nameField.focusedProperty().addListener(new ChangeListener<Boolean>()
             {
                 @Override
                 public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
                 {
                        updateFileLayoutDisplay();
                 }
             });
 
            columnIndexField.focusedProperty().addListener(focusListener);
/*            patternCombo.setItems(patternList);
            formatCombo.setItems(formatList);
             switch(parseType) {
	             case 2: 
	   			  	if (parsePattern != null)
	   			  		patternCombo.getSelectionModel().select(parsePattern.getDescription() + " - " + parsePattern.getId());
	            	formatCombo.setVisible(false); // .setDisable(true); 
	         		this.getChildren().addAll(nameLabel, sep1, columnCheck, sep2, columnIndexField, sep3, patternCombo, sep4, formatCombo);
	   			  	break;
	             case 3:
	   			  	if (dateFormat != null)
	   			  		formatCombo.getSelectionModel().select(dateFormat.getName() + " - " + dateFormat.getId());
	            	 patternCombo.setVisible(false); // .setDisable(true);
	          		this.getChildren().addAll(nameLabel, sep1, columnCheck, sep2, columnIndexField, sep3, patternCombo, sep4, formatCombo);
	            	 break;
            	case 4:
	   			  	if (parsePattern != null)
	   			  		patternCombo.getSelectionModel().select(parsePattern.getDescription() + " - " + parsePattern.getId());
	   			  	if (dateFormat != null)
	   			  		formatCombo.getSelectionModel().select(dateFormat.getName() + " - " + dateFormat.getId());
	   	     		this.getChildren().addAll(nameLabel, sep1, columnCheck, sep2, columnIndexField, sep3, patternCombo, sep4, formatCombo);
            		break;
	             case 5: 
	   			  	if (parsePattern != null)
	   			  		patternCombo.getSelectionModel().select(parsePattern.getDescription() + " - " + parsePattern.getId());
	            	formatCombo.setVisible(false); // .setDisable(true); 
	         		this.getChildren().addAll(nameField, sep1, columnCheck, sep2, columnIndexField, sep3, patternCombo, sep4, formatCombo);
	   			  	break;
	             case 6: 
		            patternCombo.setVisible(false); // .setDisable(true); 
		            formatCombo.setVisible(false); // .setDisable(true); 
	         		this.getChildren().addAll(nameField, sep1, columnCheck, sep2, columnIndexField, sep3, patternCombo, sep4, formatCombo);
	   			  	break;
            	default:
            		patternCombo.setVisible(false); // .setDisable(true);
            		formatCombo.setVisible(false); // .setDisable(true);
	   	     		this.getChildren().addAll(nameLabel, sep1, columnCheck, sep2, columnIndexField, sep3, patternCombo, sep4, formatCombo);
             }
*/
            this.getChildren().addAll(sepLeading);
            
             switch(parseType) {
	             case 2: 
	            	formatButton.setVisible(false); // .setDisable(true); 
	           		this.getChildren().addAll(nameLabel);
	   			  	break;
	             case 3:
	            	patternButton.setVisible(false);
	           		this.getChildren().addAll(nameLabel);
	            	 break;
            	case 4:
	           		this.getChildren().addAll(nameLabel);
            		break;
	             case 5: 
	            	formatButton.setVisible(false);
	           		this.getChildren().addAll(nameField);
	   			  	break;
	             case 6: 
		            patternButton.setVisible(false);
	           		this.getChildren().addAll(nameField);
		            formatButton.setVisible(false);
	   			  	break;
            	default:
            		patternButton.setVisible(false);
            		formatButton.setVisible(false);
	           		this.getChildren().addAll(nameLabel);
	           		break;
             }
            
       		this.getChildren().addAll(sep1, columnCheck, sep2, columnIndexField);
            if (patternButton.isVisible()) {
                 //refButton.setGraphic(imageView);
            	 if (parsePattern == null)
            		 patternButton.setText("+ Parse Pattern");
            	 else
            		 patternButton.setText("Pattern ID: " + String.valueOf(parsePattern.getId()));
            	 patternButton.setPrefWidth(125);
            	 patternButton.setStyle("-fx-background-color: #dddd99; ");
            	 patternButton.setDisable(!column);
            	 this.getChildren().addAll(sep3, patternButton);
      	         // set a handler
                 patternButton.setOnAction(new EventHandler<ActionEvent>() {
                     @Override public void handle(ActionEvent ev) {
                    	 // load this on the app thread
                    	 loadPatternPopup(getCell());
                     }
                 });      	         
             }
             
             if (formatButton.isVisible()) {
                 //refButton.setGraphic(imageView);
            	 if (dateFormat == null)
            		 formatButton.setText("+ Date Format");
            	 else
            		 formatButton.setText("Format ID: " + String.valueOf(dateFormat.getId()));
            	 formatButton.setPrefWidth(125);
            	 formatButton.setStyle("-fx-background-color: #99dddd; ");
            	 formatButton.setDisable(!column);
            	 this.getChildren().addAll(sep4, formatButton);
            	 // set a handler
            	 formatButton.setOnAction(new EventHandler<ActionEvent>() {
                     @Override public void handle(ActionEvent ev) {
                    	 // load this on the app thread
                    	 loadFormatPopup(getCell());
                     }
                 });      	         
             }
             
            if (useRef == true) {
            	 //do this as a stream so that we can resize it
            	 int buttonSize = 30;
            	 InputStream input = getClass().getClassLoader().getResourceAsStream("img/properties.png");
            	 Image image = new Image(input, buttonSize, buttonSize, true, true);
            	 ImageView imageView = new ImageView(image);
                 refButton.setGraphic(imageView);
            	 // set a handler
                 refButton.setOnAction(new EventHandler<ActionEvent>() {
                     @Override public void handle(ActionEvent e) {
                    	 Button button = (Button)e.getSource();
                    	 loadRefTableForm((int)button.getUserData());
                     }
                 });      	 
                 
                 refButton.setMinWidth(buttonSize);
            	 refButton.setPrefWidth(buttonSize);
            	 refButton.setMaxWidth(buttonSize);
            	 refButton.setMinHeight(buttonSize);
            	 refButton.setPrefHeight(buttonSize);
            	 refButton.setMaxHeight(buttonSize);
                 refCountLabel.setText("References: " + String.valueOf(refCount));
                 refCountLabel.setMinWidth(100);
                 refCountLabel.setPrefWidth(100);
                 refCountLabel.setStyle("-fx-padding: 5 10 0 0;");

            	 this.getChildren().addAll(sep5,refCountLabel,refButton);
             }
        }
        
        private ChangeListener<Boolean> focusListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            	// leave a zero if blank
            	if (columnIndexField.getText().length() == 0) columnIndexField.setText("0");
            	// only hit on losing focus
            	if (!newValue) {
        	        updateFileLayoutDisplay();
        	        setStyle(columnIndex);
                }
            }
        };
        
        public void setStyle(int styleId) {
        	// clear the old styles
        	this.getStyleClass().clear();	
        	if (getColumn() == false) return;

        	// we are working wiht a pallette of 30 colors
        	Integer color = styleId + 1;
	    	if (color > 30) color -= 30;
	    	if (color > 30) color -= 30;
	    	if (color > 30) color -= 30;

        	this.getStyleClass().add("BackColor" + color);
        }
        
         public boolean isHeader() {
        	return headerCell;
        }
        
        public void clearCell() {
            columnCheck.setSelected(false);
            columnIndexField.setText("");
            nameField.setText("");
        }
        
        public HBoxDetailCell getCell() {
        	return this;
        }
        
        public void setRefCount(int count) {
            refCountLabel.setText("References: " + String.valueOf(count));
        }
        
        public int getFileType() {
        	return fileType;
        }
        
        public int getRefType() {
        	return refType;
        }
        
        public String getName() {
        	return nameField.getText();
        }
        
        public Boolean getColumn() {
        	return columnCheck.isSelected(); 
        }
        
        public int getColumnIndex() {
        	return columnIndex;
        }
        
        public PipelineParsePattern getPattern() {
        	return parsePattern;
        }
      
        public void setParsePattern(PipelineParsePattern pattern) {
        	parsePattern = pattern;
       	 	if (parsePattern == null && patternButton != null)
       	 		patternButton.setText("+ Parse Pattern");
       	 	else
       	 		patternButton.setText("Pattern ID: " + String.valueOf(parsePattern.getId()));
        }
        
        
        public PipelineParseDateFormat getFormat() {
        	return dateFormat;
        }
      
        public void setDateFormat(PipelineParseDateFormat format) {
        	dateFormat = format;;
       	 	if (dateFormat == null)
       	 		formatButton.setText("+ Date Format");
       	 	else
       	 		formatButton.setText("Format ID: " + String.valueOf(dateFormat.getId()));
        }
        
        
        public String getPatternValue() {
        	if (parsePattern != null)
        		return parsePattern.getId().toString() + " - " + parsePattern.getDescription();
        	else
        		return "";
        }
        
        public String getPatternName() {
        	if (parsePattern != null)
        		return parsePattern.getName();
        	else
        		return "";
        }
        
        public String getPatternDescription() {
        	if (parsePattern != null)
        		return parsePattern.getDescription();
        	else
        		return "";
        }
        
        public Long getSelectedPatternId() {
        	if (parsePattern != null)
        		return parsePattern.getId();
        	else
        		return 0l;
        }
        
        public Long getSelectedFormatId() {
        	if (dateFormat != null)
        		return dateFormat.getId();
        	else
        		return 0l;
        }
        
        public String getFormatName() {
        	if (dateFormat != null)
        		return dateFormat.getName();
        	else
        		return "";
        }
   }
	
	public class HBoxRuleCell extends HBox {
        Label lblCol1 = new Label();
        Label lblCol2 = new Label();
        Label lblCol3 = new Label();
        Label lblCol4 = new Label();
        DynamicPayFilePayPeriodRule rule;    

        HBoxRuleCell( DynamicPayFilePayPeriodRule rule) {
             super();
             
             if (rule != null) {
            	 if (rule.getPayCodeType() != null)
            		 lblCol1.setText(rule.getPayCodeType().toString());
            	 if (rule.getPayFrequencyType() != null)
            		 lblCol2.setText(rule.getPayFrequencyType().toString());
            	 if (rule.getEmployer() != null)
            		 lblCol3.setText(rule.getEmployer().getName());
               	 if (rule.getMaxHours() != null)
            		 lblCol4.setText(String.valueOf(rule.getMaxHours()));
            	 this.rule = rule;
             }else {
            	 lblCol1.setText("Comp Type");
            	 lblCol1.setTextFill(Color.GREY);
            	 lblCol2.setText("Pay Freq. Type");
            	 lblCol2.setTextFill(Color.GREY);
            	 lblCol3.setText("Employer");
            	 lblCol3.setTextFill(Color.GREY);
            	 lblCol4.setText("Max Hrs");
            	 lblCol4.setTextFill(Color.GREY);
             }
          
             lblCol1.setMinWidth(30);
             lblCol1.setMaxWidth(30);
             lblCol1.setPrefWidth(30);
             HBox.setHgrow(lblCol1, Priority.ALWAYS);

             lblCol2.setMinWidth(100);
             lblCol2.setMaxWidth(100);
             lblCol2.setPrefWidth(100);
             HBox.setHgrow(lblCol2, Priority.ALWAYS);

             lblCol3.setMinWidth(250);
             lblCol3.setMaxWidth(250);
             lblCol3.setPrefWidth(250);
             HBox.setHgrow(lblCol3, Priority.ALWAYS);

             lblCol4.setMinWidth(50);
             lblCol4.setMaxWidth(50);
             lblCol4.setPrefWidth(50);
             HBox.setHgrow(lblCol4, Priority.ALWAYS);

             this.getChildren().addAll(lblCol1, lblCol2, lblCol4, lblCol3);
        }

        public DynamicPayFilePayPeriodRule getRule() {
        	return rule; 
        }
   }

	public class HBoxIgnoreRowCell extends HBox {
        Label lblCol1 = new Label();
        Label lblCol2 = new Label();
        DynamicCoverageFileIgnoreRowSpecification covSpec;
        DynamicEmployeeFileIgnoreRowSpecification empSpec;
        DynamicPayFileIgnoreRowSpecification paySpec;    

        HBoxIgnoreRowCell( DynamicCoverageFileIgnoreRowSpecification covSpec, 
        				   DynamicEmployeeFileIgnoreRowSpecification empSpec,
        				   DynamicPayFileIgnoreRowSpecification paySpec) {
             super();
             
             if (covSpec != null) {
            	 lblCol1.setText(Utils.getDateString(covSpec.getLastUpdated()));
            	 //lblCol2.setText(covSpec.getName());
            	 this.covSpec = covSpec;
             }
          
             if (empSpec != null) {
            	 lblCol1.setText(Utils.getDateString(empSpec.getLastUpdated()));
            	 lblCol2.setText(empSpec.getName());
            	 this.empSpec = empSpec;
             }
          
             if (paySpec != null) {
            	 lblCol1.setText(Utils.getDateString(paySpec.getLastUpdated()));
            	 //lblCol2.setText(paySpec.getName());
            	 this.paySpec = paySpec;
             }
          
             lblCol1.setMinWidth(100);
             lblCol1.setMaxWidth(100);
             lblCol1.setPrefWidth(100);
             HBox.setHgrow(lblCol1, Priority.ALWAYS);

             lblCol2.setMinWidth(140);
             lblCol2.setMaxWidth(140);
             lblCol2.setPrefWidth(140);
             HBox.setHgrow(lblCol2, Priority.ALWAYS);

             this.getChildren().addAll(lblCol2);
        }

        public DynamicCoverageFileIgnoreRowSpecification getCovSpec() {
        	return covSpec; 
        }
        
        public DynamicEmployeeFileIgnoreRowSpecification getEmpSpec() {
        	return empSpec; 
        }
        
        public DynamicPayFileIgnoreRowSpecification getPaySpec() {
        	return paySpec; 
        }
   }
	
	public class HBoxAdditionalHourCell extends HBox {
        Label indexLabel = new Label();
        DynamicPayFileAdditionalHoursSpecification spec;  
        
        public DynamicPayFileAdditionalHoursSpecification getSpec() {
        	return spec;
        }
        
        HBoxAdditionalHourCell(DynamicPayFileAdditionalHoursSpecification spec) {
              super();
              this.spec = spec;
              indexLabel.setText(String.valueOf(spec.getColIndex()));
              
        	  this.getChildren().addAll(indexLabel);
         }
    }	
}
