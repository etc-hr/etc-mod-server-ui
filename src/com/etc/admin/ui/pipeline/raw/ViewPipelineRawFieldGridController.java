package com.etc.admin.ui.pipeline.raw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;

import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.GridChange;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import com.etc.CoreException;
import com.etc.admin.AdminApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.employee.ViewPayController;
import com.etc.admin.ui.pipeline.queue.PipelineQueue;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.LogType;
import com.etc.admin.utils.Utils.RawRecordType;
import com.etc.admin.utils.Utils.ReferenceType;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileRecordRejection;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;
import com.etc.corvetto.ems.pipeline.entities.RawConversionFailure;
import com.etc.corvetto.ems.pipeline.entities.RawCoverage;
import com.etc.corvetto.ems.pipeline.entities.RawDependent;
import com.etc.corvetto.ems.pipeline.entities.RawEmployee;
import com.etc.corvetto.ems.pipeline.entities.RawInvalidation;
import com.etc.corvetto.ems.pipeline.entities.RawIrs1094c;
import com.etc.corvetto.ems.pipeline.entities.RawNotice;
import com.etc.corvetto.ems.pipeline.entities.RawPay;
import com.etc.corvetto.ems.pipeline.entities.cov.CoverageFile;
import com.etc.corvetto.ems.pipeline.entities.emp.EmployeeFile;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileCoverageGroupReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileDepartmentReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileGenderReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileLocationReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayClassReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayCodeReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayFrequencyReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileUnionTypeReference;
import com.etc.corvetto.ems.pipeline.entities.pay.PayFile;
import com.etc.corvetto.ems.pipeline.rqs.PipelineFileRecordRejectionRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.RawCoverageRequest;
import com.etc.corvetto.ems.pipeline.rqs.RawDependentRequest;
import com.etc.corvetto.ems.pipeline.rqs.RawEmployeeRequest;
import com.etc.corvetto.ems.pipeline.rqs.RawPayRequest;
import com.etc.corvetto.ems.pipeline.rqs.c94.RawIrs1094cRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFilePayCodeReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.utils.ParserUtils;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;
import com.etc.corvetto.entities.CoverageGroup;
import com.etc.corvetto.entities.Department;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.Location;
import com.etc.corvetto.entities.PayClass;
import com.etc.corvetto.utils.types.PayCodeType;
import com.etc.corvetto.utils.types.PayFrequencyType;
import com.etc.corvetto.utils.types.UnionType;
import com.etc.embeds.SSN;
import com.etc.utils.types.GenderType;
import com.etc.utils.types.RequestStatusType;

import impl.org.controlsfx.spreadsheet.CellView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewPipelineRawFieldGridController {
	@FXML
	private ChoiceBox<String> rwfldRawTypeChoice;
	@FXML
	private VBox rwfldVBox;
	@FXML
	private Label rwfldFileName;
	@FXML
	private Label rwfldDetails;
	@FXML
	private Label rwfldFileStatus;
	@FXML
	private Button rwfldLoadAllButton;
	@FXML
	private Button rwfldUpdateButton;
	@FXML
	private Button rwfldApproveConversion;
	@FXML
	private Label rwfldsubLabel;
	@FXML
	private Label rwfldRecordRejectionsHeaderLabel;
	@FXML
	private ListView<HBoxCell> rwfldRecordRejectionsList;
	@FXML
	private Button rwfldMapperIdButton;
	@FXML
	private Button rwfldSpecIdButton;
	@FXML
	private TextField rwfldMapperSpecNameField;
	@FXML
	private TextField rwfldFileNameField;
	@FXML
	private Button rwfldSaveChangesButton;
	@FXML
	private Button rwfldUndoButton;
	@FXML
	private CheckBox rwfldMapEEtoERCheck;
	@FXML
	private CheckBox rwfldCreateEmployeeCheck;
	@FXML
	private CheckBox rwfldCreateSecondaryCheck;
	@FXML
	private CheckBox rwfldCreatePlanCheck;
	@FXML
	private Button rwfldLoading;
	@FXML
	private Button rwfldDataChangedReprocess;
	@FXML
	private TableView<String> rwfldDataTable;
	@FXML
	private Button rwfldReprocessButton;
	
	int[] minColWidth = new int[100];
	boolean getErrorFields = true;
	PipelineQueue queue = null;
	int currentSPV = 0;
	
	// mapper display support
	SpreadsheetView mapperSPV = null;
	String[] columnFields = new String[100];
	int[] columnCount = new int[100];
	int[] tableFieldPositions = new int[100];
	static int[] columnColors = new int[100];
	static int[] columnRef = new int[100];
	boolean[] columnOn = new boolean[100];
	int mapperColIndex = 0;
	int mapperMaxEntries = 0;
	int mapperLastPosition = 0;
	int mapperColorIndex = 0;
	
	// flag to track when we have active edits saved but not reprocessed
	private boolean dataEdited = false;
	
	List<MapperRowInfo> rowInfo = new ArrayList<MapperRowInfo>();
	
	//undo stack
	Stack<GridChange> spvUndoStack = new Stack<GridChange>();
	boolean undoActive = false;
	int editCount = 0;
	
	RawFieldData rawData = new RawFieldData();
	RawMapperDisplay mapperDisplay = new RawMapperDisplay();
	
	String currentRawErrors = "";
	boolean currentRowIgnored = false;
	boolean currentRawInvalidated = false;
	
	//spreadsheet mechanics
	ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
	ObservableList<SpreadsheetCell> columns = null;
	ObservableList<SpreadsheetCell> headerColumns = null;
	List<String> headerNames = null;
	// preload for speed
	String alphas = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKKLMNOPQRSTUVWXYZ";
	
	PipelineSpecification spec = null;
	
	// reference data
	List<RefData> refData = new ArrayList<RefData>();
	
	public boolean updating = false;
	
	int currentMapperRow = 0;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		// init
		initControls();
		updateControlData();
		rwfldFileName.setText("Request " +  queue.getRequest().getId().toString() + ": " + queue.getRequest().getDescription());
		// hang up a loading sign
		rwfldLoading.setVisible(true);
		// and update the data
		//rawData.updateData(DataManager.i().mPipelineQueueEntry);
		// show the layout
		//createMapperDisplay();
	}
	
	private void createMapperDisplay()
	{
		mapperDisplay.updateMapperDisplay(queue);
 
        rows.clear();
 /*       columns = FXCollections.observableArrayList();
        //add the error column
        columns.add(getMapperCell(1,0,"Errors"));
		for(int i = 1; i< mapperDisplay.mapperLastPosition + 2; i++)
		{
			//newInstance = SpreadsheetCellType.STRING.createCell(1, i, 1, 1, mapperDisplay.columnFields[i]);
			if(mapperDisplay.columnFields[i] != null && mapperDisplay.columnFields[i] != "")
				columns.add(getMapperCell(0,i,mapperDisplay.columnFields[i]));
		} */
	//	rows.add(columns);
	}
	
	private String getAlphaName(int num ) {
	    int a1 = 0;
	    int a2 = 0;
	    if(num > 26) {
	    	a2 = num / 26;
	    	a1 = num % 26;
	    	//a1 = a1 - (a2 * 26);
	    } else
	    	a1 = num;
	    
	    
	    String result = alphas.substring(a1,a1+1);
	    if(a2 > 0)
	    	result = alphas.substring(a2-1,a2) + result;
	    result += ":" + String.valueOf(num);
	   
	    return result;
	}
	
	private void initControls() {
		// hide the data changed indicator
		rwfldDataChangedReprocess.setVisible(false);
		// set this controller 
		rawData.setRawFieldController(this);
		columns = null;
		
		rwfldRawTypeChoice.getItems().clear();
		
	    if(mapperSPV != null)
	    	rwfldVBox.getChildren().remove(mapperSPV);

		//completeView.setMaxHeight(350.0f);
		rwfldApproveConversion.setVisible(false);

		// default to the first sub file
		rawData.currentSubFile = 0;
		
		// clear the records rejection info
		rwfldRecordRejectionsList.getItems().clear();
		rwfldRecordRejectionsHeaderLabel.setText("Pipeline File Record Rejections (total: 0)");
		
		rwfldFileNameField.setText("");
		
		// create a local queue entry for convenience
		queue = DataManager.i().mPipelineQueueEntry;
		if(queue == null) return;
		
		// set the load all button text
		Long size = queue.getRecords();
		if(size < queue.getRecords()) size = queue.getRecords();
		String speed = "fast";
		if(size > 25) speed = "nominal";
		if(size > 100) speed = "slow";
		if(size > 500) speed = "very slow";
		if(size > 1000) speed = "extremely slow";
		
		rwfldLoadAllButton.setText("Load all records (max " + String.valueOf(size) + ", " + speed + ")");
		
		// add some details
		if(queue.getRequest().getStatus() == RequestStatusType.COMPLETED) {
			// pull all the fields for completed files
			getErrorFields = false;
			rwfldLoadAllButton.setText("Completed File");
			rwfldLoadAllButton.setDisable(true);
		}
		else {
			rwfldLoadAllButton.setDisable(false);
			getErrorFields = true;
		}
		
		rwfldLoadAllButton.setVisible(false);
		rwfldFileStatus.setText("File Status: " + queue.getRequest().getResult());
			
    	// clear the mapper display. This file may not have one
    	clearMapperDisplay();
    	
    	//set the mapper checkboxes as read only
    	rwfldMapEEtoERCheck.setDisable(true);
    	rwfldCreateEmployeeCheck.setDisable(true);
    	rwfldCreateSecondaryCheck.setDisable(true);
    	rwfldCreatePlanCheck.setDisable(true);
    	//don't fade them out when disabled
    	rwfldMapEEtoERCheck.setStyle("-fx-opacity: 1");
    	rwfldCreateEmployeeCheck.setStyle("-fx-opacity: 1");
    	rwfldCreatePlanCheck.setStyle("-fx-opacity: 1");
    	rwfldMapEEtoERCheck.setStyle("-fx-opacity: 1");
    	// and uncheck them for now
    	rwfldMapEEtoERCheck.setSelected(false);
    	rwfldCreateEmployeeCheck.setSelected(false);
    	rwfldCreatePlanCheck.setSelected(false);
    	rwfldMapEEtoERCheck.setSelected(false);
	
	}
	
	private void setupMapperSPV() {
		rwfldUndoButton.setVisible(false);
		rwfldSaveChangesButton.setVisible(false);
		spvUndoStack.clear();
				
		mapperSPV.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	// check to see if it is one of our special types
            	Text targ = null;
            	SpreadsheetCell cell  = null;
            	try {
	            	targ = (Text) event.getTarget();
            		} catch (Exception e) {
            	}
            	if (targ != null)  return;

            	try {
	            	CellView cv = (CellView) event.getTarget();
	            	cell = cv.getItem();
            	} catch (Exception e) {
            		// not a cell, return;
            		return;
            	}
            	
            	RefData rd = getRefData(cell.getRow(),cell.getColumn());
        		if (rd == null) return;
            	switch (rd.refType) {
            	case PYGENDERTYPE:
                	GenderType gt = showGenderTypeReferencePopup(rd.getFileValue(), queue.getRequest().getEmployer());
                	if (gt != null)
                		mapperSPV.getGrid().setCellValue(cell.getRow(), cell.getColumn(), gt.toString());
            		break;
            	case EEGENDERTYPE:
                	GenderType ergt = showERGenderTypeReferencePopup(rd.getFileValue(), queue.getRequest().getEmployer());
                	if (ergt != null)
                		mapperSPV.getGrid().setCellValue(cell.getRow(), cell.getColumn(), ergt.toString());
            		break;
            	case PYPAYCODE:
                	PayCodeType pct = showPayCodeReferencePopup(rd.getFileValue(), queue.getRequest().getEmployer());
                	if (pct != null)
                		mapperSPV.getGrid().setCellValue(cell.getRow(), cell.getColumn(), pct.toString());
            		break;
            	case PYPAYFREQUENCY:
                	PayFrequencyType pft = showPayFrequencyReferencePopup(rd.getFileValue(), queue.getRequest().getEmployer());
                	if (pft != null)
                		mapperSPV.getGrid().setCellValue(cell.getRow(), cell.getColumn(), pft.toString());
            		break;
            	case PYUNIONTYPE:
                	UnionType ut = showUnionTypeReferencePopup(rd.getFileValue(), queue.getRequest().getEmployer());
                	if (ut != null)
                		mapperSPV.getGrid().setCellValue(cell.getRow(), cell.getColumn(), ut.toString());
            		break;
            	case EEUNIONTYPE:
                	UnionType erut = showUnionTypeReferencePopup(rd.getFileValue(), queue.getRequest().getEmployer());
                	if (erut != null)
                		mapperSPV.getGrid().setCellValue(cell.getRow(), cell.getColumn(), erut.toString());
            		break;
            	case EECOVERAGEGROUP:
                	CoverageGroup cg = showERCoverageGroupReferencePopup(rd.getFileValue(), queue.getRequest().getEmployer());
                	if (cg != null)
                		mapperSPV.getGrid().setCellValue(cell.getRow(), cell.getColumn(), cg.getName());
            		break;
            	case EEDEPARTMENT:
                	Department dp = showERDepartmentReferencePopup(rd.getFileValue(), queue.getRequest().getEmployer());
                	if (dp != null)
                		mapperSPV.getGrid().setCellValue(cell.getRow(), cell.getColumn(), dp.getName());
            		break;
            	case EELOCATION:
                	Location lo = showERLocationReferencePopup(rd.getFileValue(), queue.getRequest().getEmployer());
                	if (lo != null)
                		mapperSPV.getGrid().setCellValue(cell.getRow(), cell.getColumn(), lo.getName());
            		break;
            	default:
            		return;
            	}
            }
        });
		
		//add a change handler to capture changes
    	mapperSPV.getGrid().addEventHandler( GridChange.GRID_CHANGE_EVENT, new EventHandler<GridChange>() {	
    		public void handle(GridChange change) {
//	        	 change.consume();
		        	 if (undoActive == true) {
		        		 undoActive = false;
		        		 return;
		        	 }
		        	 
		        	 GridChange newChange = new GridChange(change.getRow(), change.getColumn(), change.getOldValue(), change.getNewValue());
		        	 spvUndoStack.push(newChange);
		        	 //enable the buttons
		        	 rwfldUndoButton.setVisible(true);
		        	 rwfldSaveChangesButton.setVisible(true);
	             }
	      });
	}
	
	private void clearMapperDisplay() {
		rwfldMapperIdButton.setText("");;
		rwfldSpecIdButton.setText("");
		rwfldMapperSpecNameField.setText("");
	}

	private void disableSubControls(boolean disable) {
		rwfldUpdateButton.setDisable(disable);
		rwfldsubLabel.setDisable(disable);
		rwfldRawTypeChoice.setDisable(disable);
	}

	private void showRawFields()
	{	
		try {
			 if(queue == null) return;
			 
			 // reset
			 rows = FXCollections.observableArrayList();
			 currentMapperRow = 0;
			 columns = null;
			 
			// update the selection
			if(rwfldRawTypeChoice.getItems().size() > 0 && rwfldRawTypeChoice.getItems().size() > rawData.currentSubFile)
				rwfldRawTypeChoice.getSelectionModel().select(rawData.currentSubFile);
	
			//reset the rowInfo data
			rowInfo.clear();
	
			String fileName = "";
			switch(queue.getFileType()) {
			case EMPLOYEE:
				fileName = queue.getEmployeeFile().getId().toString() + " Employee";
				showRawEmployees();
				break;
			case PAY:
				fileName = queue.getPayFile().getId().toString() + " Pay";
				showRawEmployees();
				break;
			case COVERAGE:
				fileName = queue.getCoverageFile().getId().toString() + " Coverage";
				showRawEmployees();
				break;
			case IRS1094C:
				fileName = queue.getIrs1094cFile().getId().toString() + " Irs1094c";
				showRawIrs1094cs();
				break;
			case IRS1095C:
				//updateRawIrs1095cData();
				break;
			case IRSAIRERR:
				//updateRawAirTranErrorData();
				break;
			case IRSAIRRCPT:
				//updateRawAirTranReceiptData();
			default:
				break;
			}
			
			rwfldFileNameField.setText(fileName);
			loadFileRecordRejections();
			
			// add the header columns
			rows.add(0,headerColumns);
			// add in a row placeholder for the header
			rowInfo.add( new MapperRowInfo(RawRecordType.OTHER, 0l, rows.size()));
			
			if (rows == null || rows.get(0) == null) {
			    EtcAdmin.i().setStatusMessage("Ready");
				EtcAdmin.i().setProgress(0);	
				return;
			}
			
	        // create the grid
			Grid grid = new GridBase(rows.size(),rows.get(0).size());
	        grid.setRows(rows);
	        
	        // verify column count and remove anything that doesn't match up in order to have a well formed grid
	        ObservableList<ObservableList<SpreadsheetCell>> cleanRows = FXCollections.observableArrayList(rows);
	        for (ObservableList<SpreadsheetCell> list : rows ) {
	        	if (list.size() != grid.getColumnCount()) {
	        		cleanRows.remove(list);
	        		// log it so we know we had an issue
	        		DataManager.i().insertLogEntry("Bad Raw Field Grid entry for type:" + queue.getFileType().toString() + ", request id:" + queue.getRequest().getId(), LogType.INFO);
	        	}
	        }
	        grid.setRows(cleanRows);

	        // Updates the SpreadsheetView grid
		    if(mapperSPV != null)
		    	rwfldVBox.getChildren().remove(mapperSPV);
		    mapperSPV = new SpreadsheetView(grid);
		    rwfldVBox.getChildren().add(mapperSPV);
		    setupMapperSPV();
		
		    //set the headers
		    for (int i = 0; i < rows.get(0).size(); i++) {
		    	mapperSPV.getGrid().getColumnHeaders().add(i,headerNames.get(i));
		    }	    
		    mapperSPV.getGrid().getColumnHeaders().remove(0);
		    mapperSPV.getGrid().getColumnHeaders().add(0,"Errors");
		    
	        updateColumnHeaders();
		    
		    // fix the top row and error colum
		    mapperSPV.getFixedRows().add(0);
		    mapperSPV.getFixedColumns().add(mapperSPV.getColumns().get(0));
		    
		    // adjust the size
		    mapperSPV.setMinHeight(625);
		    mapperSPV.setPrefHeight(625);
		    mapperSPV.setMaxHeight(625);  
		   //set own handler for right click with Dialog
		   mapperSPV.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
		    	@Override public void handle(ContextMenuEvent event) {
			      try {
			    	CellView cellView = (CellView) event.getTarget();
			        final Clipboard clipboard = Clipboard.getSystemClipboard();
			        final ClipboardContent content = new ClipboardContent();
			        content.putString(cellView.getText());
			        clipboard.setContent(content);
			        Utils.showAlert("Copied", "Cell Data: " + cellView.getText() + " copied to System Clipboard");
			      }catch (Exception e){
			    	  Utils.showAlert("Refine Selection", "Please select blank area of cell");
			      }
		    	}
		    });
		    
		    mapperSPV.getSelectionModel().clearSelection();
		} catch (Exception e) {
			DataManager.i().log( Level.SEVERE, e);
		}
	    
	    EtcAdmin.i().setStatusMessage("Ready");
		EtcAdmin.i().setProgress(0);		
	}
	
	private String getErrors(List<RawInvalidation> invalidations, List<RawNotice> notices) {
		String errors = "";
		String invalidationString = "";
		String noticeString = "";
		
		currentRawInvalidated = false;
		if(invalidations != null) {
			for (RawInvalidation inv : invalidations) {
				currentRawInvalidated = true;
				if(invalidationString.length() < 16)
					invalidationString = "Invalidations: " + inv.getMessage();
				else
					invalidationString = invalidationString + ", " + inv.getMessage();
			}
		}

		if(invalidationString.length() > 0)
			errors += invalidationString;
		if(noticeString.length() > 0)
			errors += (" " + noticeString);
		
		return errors;
	}
	
	private static String getInvalidations(List<RawInvalidation> invalidations, List<RawConversionFailure> failures) {
		String invalidationString = "";
		
		if(invalidations != null) {
			for (RawInvalidation inv : invalidations) {
				if(invalidationString.length() < 1)
					invalidationString = inv.getMessage();
				else
					invalidationString = invalidationString + "\n" + inv.getMessage();
			}
		}
		
		if(failures != null) {
			if(invalidationString.length() > 0 && failures.size() > 0) 
				invalidationString = invalidationString + "\n";
			for (RawConversionFailure fail: failures) {
				if(invalidationString.length() < 1)
					invalidationString = "Conv. Failure: " + fail.getMessage();
				else
					invalidationString = invalidationString + "\n" + "Conv. Failure: " + fail.getMessage();
			}
		}

		return invalidationString;
	}
	
	private void showRawEmployees(){	
		EtcAdmin.i().setStatusMessage("Loading Raw Employee Fields...");
		EtcAdmin.i().setProgress(.75);

		List<RawEmployee> rawEmps = rawData.mRawEmployees;
		if(rawEmps == null || rawEmps.size() < 1) {
			if(rwfldRawTypeChoice.getItems().size() > 1)
				EtcAdmin.i().setStatusMessage("Ready - No Raw Records. Please check the other SubFiles");
			else
				EtcAdmin.i().setStatusMessage("Ready - No Raw Employee Records");
			EtcAdmin.i().setProgress(0);
			return;
		}
		
		// iterate through the raw employees and fill the grid
		for (RawEmployee emp : rawEmps) {
			if(emp == null) continue;

			// set the ignored flag for color coding the row
			if(emp.getRecordInformation() != null)
				currentRowIgnored = emp.getRecordInformation().isIgnored();
			else
				currentRowIgnored = false;
			// gather up any errors
			currentRawErrors = getErrors(emp.getInvalidations(), emp.getNotices());
			// add to the view
			String errors = getInvalidations(emp.getInvalidations(), emp.getConversionFailures());
			// show any dependent records
			switch(queue.getFileType()) {
			case EMPLOYEE:
				createCellRow(emp, errors);
				showRawDependent(emp.getId());
				break;
			case PAY:
				createCellRowPayEmp(emp, errors);
				showRawPay(emp.getId());
				break;
			case COVERAGE:
				createCellRowCovEmp(emp, errors);
				showRawCoverage(emp.getId());
				showRawCoverageDependent(emp.getId());
				break;
			default:
				break;
			}
			
			// limit how much we stuff into the spreadsheet.
			if (currentMapperRow > 499) break;
		}
	}
	
	private void showRawPay(Long rawEmployeeId){	
		List<RawPay> rawPays = rawData.mRawPays;
		if(rawPays == null || rawPays.size() == 0) {
			return;
		}
		
		// iterate through the raw employees and fill the grid
		for (RawPay pay : rawPays) {
			if(pay == null) continue;
			if(pay.getRawEmployeeId().equals(rawEmployeeId) == false) continue;
			// set the ignored flag for color coding the row
			if(pay.getRecordInformation() != null)
				currentRowIgnored = pay.getRecordInformation().isIgnored();
			else
				currentRowIgnored = false;
			// gather up any errors
			currentRawErrors = getErrors(pay.getInvalidations(), pay.getNotices());
			
			// add to the view
			String errors = getInvalidations(pay.getInvalidations(), pay.getConversionFailures());
			createCellRow(pay, errors);
		}
	}

	private void showRawCoverage(Long rawEmployeeId){	
		List<RawCoverage> rawCovs = rawData.mRawCoverages;
		if(rawCovs == null) {
			return;
		}
		
		// iterate through the raw employees and fill the grid
		for (RawCoverage cov : rawCovs) {
			if(cov == null) continue;
			if(cov.getRawEmployeeId().equals(rawEmployeeId) == false) continue;
			
			// set the ignored flag for color coding the row
			if(cov.getRecordInformation() != null)
				currentRowIgnored = cov.getRecordInformation().isIgnored();
			else
				currentRowIgnored = false;
			// gather up any errors
			currentRawErrors = getErrors(cov.getInvalidations(), cov.getNotices());
			// iterate through the fields
			
			// add to the view
			String errors = getInvalidations(cov.getInvalidations(), cov.getConversionFailures());
			//completeView.getItems().add(new HBoxRawDetailCell(cov, mapperDisplay));
			createCellRow(cov, errors);
		}
		
		EtcAdmin.i().setStatusMessage("Ready");
		EtcAdmin.i().setProgress(0);		
		return;
	}

	private void showRawDependent(Long rawEmployeeId){	
		List<RawDependent> rawDependents = rawData.mRawDependents;
		if(rawDependents == null || rawDependents.size() == 0) {
			return;
		}
		
		// iterate through the raw dependent and fill the grid
		for (RawDependent dependent : rawDependents) {
			if(dependent == null) continue;
			if(dependent.getRawEmployeeId().equals(rawEmployeeId) == false) continue;
			// set the ignored flag for color coding the row
			if(dependent.getRecordInformation() != null)
				currentRowIgnored = dependent.getRecordInformation().isIgnored();
			else
				currentRowIgnored = false;
			// gather up any errors
			currentRawErrors = getErrors(dependent.getInvalidations(), dependent.getNotices());
			// iterate through the fields
			
			// add to the view
			String errors = getInvalidations(dependent.getInvalidations(), dependent.getConversionFailures());
			//completeView.getItems().add(new HBoxRawDetailCell(pay, mapperDisplay));
			createCellRow(dependent, errors);
		}
	}
	
	private void showRawCoverageDependent(Long rawEmployeeId){	
		List<RawDependent> rawDependents = rawData.mRawDependents;
		if(rawDependents == null || rawDependents.size() == 0) {
			return;
		}
		
		// iterate through the raw dependent and fill the grid
		for (RawDependent dependent : rawDependents) {
			if(dependent == null) continue;
			if(dependent.getRawEmployeeId().equals(rawEmployeeId) == false) continue;
			// set the ignored flag for color coding the row
			if(dependent.getRecordInformation() != null)
				currentRowIgnored = dependent.getRecordInformation().isIgnored();
			else
				currentRowIgnored = false;
			// gather up any errors
			currentRawErrors = getErrors(dependent.getInvalidations(), dependent.getNotices());
			// iterate through the fields
			
			// add to the view
			String errors = getInvalidations(dependent.getInvalidations(), dependent.getConversionFailures());
			//completeView.getItems().add(new HBoxRawDetailCell(pay, mapperDisplay));
			createCellRowCovDep(dependent, errors);
		}
	}
	
	private void showRawIrs1094cs() 
	{
	
		// load the headers
		
		// load the data
		List<RawIrs1094c> raw1094cs = rawData.mRawIrs1094cs;
		if(raw1094cs == null || raw1094cs.size() < 1) {
			EtcAdmin.i().setStatusMessage("Ready - No Raw Irs1094c Records");
			EtcAdmin.i().setProgress(0);
			return;
		}
		
		// iterate through and add the data
		for (RawIrs1094c raw1094c : raw1094cs) {
			currentRawErrors = getErrors(raw1094c.getInvalidations(), raw1094c.getNotices());
			createCellRow(raw1094c, currentRawErrors);
		}	
	}
	
	private void createCellRow(RawEmployee rawEmp, String Errors)
	{

		// add a space row
		if(columns != null) {
			int size = columns.size();
			columns = FXCollections.observableArrayList();
			for (int i = 0; i < size; i++) {
				columns.add(getFillerCell(currentMapperRow));
			}
			rows.add(columns);
			// add in a row placeholder
			rowInfo.add( new MapperRowInfo(RawRecordType.OTHER, 0l, rows.size()));
		}
		
		currentMapperRow++;
		columns = FXCollections.observableArrayList();
		headerColumns = FXCollections.observableArrayList();
		headerNames = new ArrayList<String>();
		// the error column
		columns.add(getMapperCell(rows.size()+1, 0, 0, "Raw Employee: " + Errors));

		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(1),1,rawEmp.getEmployerIdentifier()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(3),3,rawEmp.getSsn()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(5),5,rawEmp.getPhone()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(6),6,rawEmp.getEmail()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(7),7,rawEmp.getFirstName()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(8),8,rawEmp.getMiddleName()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(9),9,rawEmp.getLastName()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(10),10,rawEmp.getRehireDate()));
		addCell(getERDepartmentCell(rows.size()+1, mapperDisplay.getMapperColumn(11),11,rawEmp.getDepartment(), rawEmp.getDepartmentReference()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(12),12,rawEmp.getStreetNumber()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(13),13,rawEmp.getStreet()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(14),14,rawEmp.getStreetExtension()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(15),15,rawEmp.getCity()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(16),16,rawEmp.getState()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(17),17,rawEmp.getZip()));
		addCell(getGenderTypeCell(rows.size()+1, mapperDisplay.getMapperColumn(18),18,rawEmp.getGenderType(),rawEmp.getGenderReference()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(19),19,rawEmp.getDateOfBirth()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(20),20,rawEmp.getHireDate()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(21),21,rawEmp.getTermDate()));
		addCell(getERLocationCell(rows.size()+1, mapperDisplay.getMapperColumn(22),22,rawEmp.getLocation(), rawEmp.getLocationReference()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(23),23,rawEmp.getJobTitle()));
		addCell(getPayCodeTypeCell(rows.size()+1, mapperDisplay.getMapperColumn(24),24,rawEmp.getPayCodeType(), rawEmp.getPayCodeReference()));
		addCell(getUnionTypeCell(rows.size()+1, mapperDisplay.getMapperColumn(25),25,rawEmp.getUnionType(), rawEmp.getUnionReference()));
		addCell(getERCoverageGroupCell(rows.size()+1, mapperDisplay.getMapperColumn(26),26,rawEmp.getCoverageGroup(), rawEmp.getCvgClassReference()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(60),60,rawEmp.getCustomField1()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(61),61,rawEmp.getCustomField2()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(62),62,rawEmp.getCustomField3()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(63),63,rawEmp.getCustomField4()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(64),64,rawEmp.getCustomField5()));

		rows.add(columns);
		rowInfo.add( new MapperRowInfo(RawRecordType.EMPLOYEE, rawEmp.getId(), rows.size()));
	}
	
	private void createCellRowCovEmp(RawEmployee rawEmp, String Errors)
	{
		currentMapperRow++;

		// add a space row
		if(columns != null) {
			int rowCount = rows.get(0).size();
			int size = rows.get(0).size(); //columns.size();
			columns = FXCollections.observableArrayList();
			for (int i = 0; i < size; i++) {
				columns.add(getFillerCell(currentMapperRow));
			}
			rows.add(columns);
			// add in a row placeholder
			rowInfo.add( new MapperRowInfo(RawRecordType.OTHER, 0l, rows.size()));
		}
		
		currentMapperRow++;
		columns = FXCollections.observableArrayList();
		headerColumns = FXCollections.observableArrayList();
		headerNames = new ArrayList<String>();
		// the error column
		columns.add(getMapperCell(rows.size()+1, 0, 0, "Raw Employee: " + Errors));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(1),1,rawEmp.getEmployerIdentifier()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(2),2,rawEmp.getEtcReferenceId()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(4),4,rawEmp.getEmployerIdentifier()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(3),3,rawEmp.getSsn()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(6),6,rawEmp.getEmail()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(7),7,rawEmp.getFirstName()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(8),8,rawEmp.getMiddleName()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(9),9,rawEmp.getLastName()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(10),10,rawEmp.getPhone()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(11),11,rawEmp.getEmail()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(12),12,rawEmp.getStreetNumber()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(13),13,rawEmp.getStreet()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(14),14,rawEmp.getStreetExtension()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(15),15,rawEmp.getCity()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(16),16,rawEmp.getState()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(17),17,rawEmp.getZip()));
		if(rawEmp.getGenderType() != null)
			addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(18),18,rawEmp.getGenderType().toString()));
		else 
			addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(18),18,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(19),19,rawEmp.getDateOfBirth()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(20),20,rawEmp.getHireDate()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(21),21,rawEmp.getTermDate()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(23),23,rawEmp.getJobTitle()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(60),60,rawEmp.getCustomField1()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(61),61,rawEmp.getCustomField2()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(62),62,rawEmp.getCustomField3()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(63),63,rawEmp.getCustomField4()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(64),64,rawEmp.getCustomField5()));

		// now the coverage field, blank column placeholders
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(5),5,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(38),38,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(39),39,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(40),40,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(42),42,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(43),43,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(44),44,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(45),45,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(46),46,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(47),47,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(48),48,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(49),49,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(50),50,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(51),51,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(52),52,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(53),53,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(54),54,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(55),55,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(56),56,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(57),57,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(58),58,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(59),59,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(69),69,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(60),60,""));
	
		// and dependent data, blank 
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(25),25,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(28),28,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(29),29,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(30),30,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(31),31,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(32),32,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(33),33,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(34),34,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(35),35,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(36),36,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(37),37,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(64),64,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(65),65,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(66),66,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(67),67,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(68),68,""));

		rows.add(columns);
		rowInfo.add( new MapperRowInfo(RawRecordType.EMPLOYEE, rawEmp.getId(), rows.size()));
	}

	private void createCellRow(RawCoverage rawCov, String Errors)
	{
		currentMapperRow++;

		columns = FXCollections.observableArrayList();
		headerColumns = FXCollections.observableArrayList();
		headerNames = new ArrayList<String>();
		// the error column
		columns.add(getMapperCell(rows.size()+1, 0, 0, "     Raw Coverage: " + Errors));

		// first the employer fields, blank column placeholders
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(1),1,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(2),2,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(4),4,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(3),3,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(6),6,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(7),7,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(8),8,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(9),9,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(10),10,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(11),11,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(12),12,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(13),13,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(14),14,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(15),15,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(16),16,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(17),17,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(18),18,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(19),19,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(20),20,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(21),21,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(23),23,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(60),60,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(61),61,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(62),62,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(63),63,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(64),64,""));
		
		// now the coverage fields with data
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(5),5,rawCov.getSubscriberNumber()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(38),38,rawCov.isWaived()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(39),39,rawCov.isIneligible()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(40),40,rawCov.getTaxYear()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(42),42,rawCov.getStartDate()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(43),43,rawCov.getEndDate()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(44),44,rawCov.isTaxYearSelected()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(45),45,rawCov.isJanuarySelected()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(46),46,rawCov.isFebruarySelected()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(47),47,rawCov.isMarchSelected()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(48),48,rawCov.isAprilSelected()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(49),49,rawCov.isMaySelected()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(50),50,rawCov.isJuneSelected()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(51),51,rawCov.isJulySelected()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(52),52,rawCov.isAugustSelected()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(53),53,rawCov.isSeptemberSelected()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(54),54,rawCov.isOctoberSelected()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(55),55,rawCov.isNovemberSelected()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(56),56,rawCov.isDecemberSelected()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(57),57,rawCov.getPlanReference()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(58),58,rawCov.getDeduction()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(59),59,rawCov.getMemberShare()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(60),60,rawCov.getCustomField1()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(69),69,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(61),61,rawCov.getCustomField2()));

		// and dependent data, blank 
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(25),25,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(28),28,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(29),29,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(30),30,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(31),31,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(32),32,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(33),33,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(34),34,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(35),35,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(36),36,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(37),37,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(64),64,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(65),65,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(66),66,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(67),67,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(68),68,""));

		rows.add(columns);
		rowInfo.add( new MapperRowInfo(RawRecordType.COVERAGE, rawCov.getId(), rows.size()));
	}

	private void createCellRowCovDep(RawDependent rawDependent, String Errors)
	{
		currentMapperRow++;

		columns = FXCollections.observableArrayList();
		headerColumns = FXCollections.observableArrayList();
		headerNames = new ArrayList<String>();
		// the error column
		columns.add(getMapperCell(rows.size()+1, 0, 0, "     Raw Dependent: " + Errors));

		// first the employer fields, blank column placeholders
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(1),1,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(2),2,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(4),4,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(3),3,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(6),6,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(7),7,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(8),8,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(9),9,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(10),10,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(11),11,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(12),12,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(13),13,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(14),14,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(15),15,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(16),16,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(17),17,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(18),18,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(19),19,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(20),20,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(21),21,""));
		
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(25),25,rawDependent.getSsn()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(28),28,rawDependent.getFirstName()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(29),29,rawDependent.getMiddleName()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(30),30,rawDependent.getLastName()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(31),31,rawDependent.getStreetNumber()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(32),32,rawDependent.getStreet()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(33),33,rawDependent.getStreetExtension()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(34),34,rawDependent.getCity()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(35),35,rawDependent.getState()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(36),36,rawDependent.getZip()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(37),37,rawDependent.getDateOfBirth()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(64),64,rawDependent.getCustomField1()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(65),65,rawDependent.getCustomField2()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(66),66,rawDependent.getCustomField3()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(67),67,rawDependent.getCustomField4()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(68),68,rawDependent.getCustomField5()));

		// now the coverage field, blank column placeholders
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(5),5,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(38),38,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(39),39,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(40),40,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(42),42,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(43),43,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(44),44,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(45),45,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(46),46,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(47),47,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(48),48,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(49),49,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(50),50,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(51),51,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(52),52,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(53),53,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(54),54,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(55),55,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(56),56,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(57),57,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(58),58,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(59),59,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(69),69,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(60),60,""));
	
		rows.add(columns);
		rowInfo.add( new MapperRowInfo(RawRecordType.DEPENDENT, rawDependent.getId(), rows.size()));
	}
	
	private void createCellRowPayEmp(RawEmployee rawEmp, String Errors)
	{
		currentMapperRow++;

		// add a space row
		if(columns != null) {
			int size = columns.size();
			columns = FXCollections.observableArrayList();
			for (int i = 0; i < size; i++) {
				columns.add(getFillerCell(currentMapperRow));
			}
			rows.add(columns);
			// add in a row placeholder
			rowInfo.add( new MapperRowInfo(RawRecordType.OTHER, 0l, rows.size()));
		}
		
		currentMapperRow++;
		columns = FXCollections.observableArrayList();
		headerColumns = FXCollections.observableArrayList();
		headerNames = new ArrayList<String>();
		// the error column
		columns.add(getMapperCell(rows.size()+1, 0,0,"Raw Employee: " + Errors));

		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(1),1,rawEmp.getEmployerReference()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(2),2,rawEmp.getSsn()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(3),3,rawEmp.getEmployerIdentifier()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(4),4,rawEmp.getFirstName()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(5),5,rawEmp.getMiddleName()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(6),6,rawEmp.getLastName()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(7),7,rawEmp.getStreetNumber()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(8),8,rawEmp.getStreet()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(9),9,rawEmp.getStreetExtension()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(10),10,rawEmp.getCity()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(11),11,rawEmp.getState()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(12),12,rawEmp.getZip()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(13),13,rawEmp.getDateOfBirth()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(14),14,rawEmp.getHireDate()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(15),15,rawEmp.getRehireDate()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(16),16,rawEmp.getTermDate()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(17),17,rawEmp.getDepartmentId()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(18),18,rawEmp.getLocationId()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(19),19,rawEmp.getCoverageGroupId()));
		if(rawEmp.getGenderType() != null)
			addCell(getGenderTypeCell(rows.size()+1, mapperDisplay.getMapperColumn(20),20,rawEmp.getGenderType(),rawEmp.getGenderReference()));
		else 
			addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(20),20,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(21),21,rawEmp.getPhone()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(22),22,rawEmp.getEmail()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(23),23,rawEmp.getJobTitle()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(42),42,rawEmp.getCustomField1()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(43),43,rawEmp.getCustomField2()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(44),44,rawEmp.getCustomField3()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(45),45,rawEmp.getCustomField4()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(46),46,rawEmp.getCustomField5()));
		
		// then add in the raw pay blank column placeholders
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(24),24,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(25),25,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(26),25,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(27),27,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(28),28,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(29),29,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(30),30,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(31),31,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(32),32,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(33),33,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(34),34,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(35),35,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(36),36,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(37),37,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(38),38,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(39),39,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(40),40,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(41),41,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(47),47,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(48),48,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(49),49,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(50),50,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(51),51,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(52),52,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(53),53,""));
		
		rows.add(columns);
		rowInfo.add( new MapperRowInfo(RawRecordType.EMPLOYEE, rawEmp.getId(), rows.size()));
	}

	private void createCellRow(RawPay rawPay, String Errors)
	{
		currentMapperRow++;

		columns = FXCollections.observableArrayList();
		headerColumns = FXCollections.observableArrayList();
		headerNames = new ArrayList<String>();
		// the error column
		columns.add(getMapperCell(rows.size()+1, 0, 0,"     Raw Pay: " + Errors));
		
		// first, the rawEmployee blank column placeholders
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(1),1,""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(2),2, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(3),3, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(4),4, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(5),5, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(6),6, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(7),7, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(8),8, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(9),9, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(10),10, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(11),11, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(12),12, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(13),13, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(14),14, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(15),15, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(16),16, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(17),17, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(18),18, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(19),19, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(20),20, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(21),21, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(22),22, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(23),23, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(42),42, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(43),43, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(44),44, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(45),45, ""));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(46),46, ""));
		
		// then the rawPay fields with data
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(24),24,rawPay.getPayPeriodStart()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(25),25,rawPay.getPayPeriodEnd()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(26),26,rawPay.getPayDate()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(27),27,rawPay.getPayPeriodId()));
		addCell(getPayFrequencyTypeCell(rows.size()+1, mapperDisplay.getMapperColumn(28),28,rawPay.getPayFreqType(),rawPay.getPpdFreqReference()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(29),29,rawPay.getWorkDate()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(30),30,rawPay.getHours()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(31),31,rawPay.getRate()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(32),32,rawPay.getAmount()));
		addCell(getPayCodeTypeCell(rows.size()+1, mapperDisplay.getMapperColumn(33),33, rawPay.getPayCodeType(), rawPay.getPayCodeReference()));
		addCell(getUnionTypeCell(rows.size()+1, mapperDisplay.getMapperColumn(34),34,rawPay.getUnionType(),rawPay.getUnionReference()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(35),35,rawPay.getOvertimeHours1()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(36),36,rawPay.getOvertimeHours2()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(37),37,rawPay.getOvertimeHours3()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(38),38,rawPay.getPtoHours()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(39),39,rawPay.getSickHours()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(40),40,rawPay.getHolidayHours()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(41),41,rawPay.getPayClassId()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(47),47,rawPay.getCustomField1()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(48),48,rawPay.getCustomField2()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(49),49,rawPay.getCustomField3()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(50),50,rawPay.getCustomField4()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(51),51,rawPay.getCustomField5()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(52),52,rawPay.getCustomField6()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(53),53,rawPay.getCustomField7()));
		
		rows.add(columns);
		rowInfo.add( new MapperRowInfo(RawRecordType.PAY, rawPay.getId(), rows.size()));
	}

	private void createCellRow(RawDependent rawDependent, String Errors)
	{
		currentMapperRow++;

		columns = FXCollections.observableArrayList();
		headerColumns = FXCollections.observableArrayList();
		headerNames = new ArrayList<String>();
		// the error column
		columns.add(getMapperCell(rows.size()+1, 0,0, "     Raw Dependent: " + Errors));

		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(25),25,rawDependent.getSsn()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(28),28,rawDependent.getFirstName()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(29),29,rawDependent.getMiddleName()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(30),30,rawDependent.getLastName()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(31),31,rawDependent.getStreetNumber()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(32),32,rawDependent.getStreet()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(33),33,rawDependent.getStreetExtension()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(34),34,rawDependent.getCity()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(35),35,rawDependent.getState()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(36),36,rawDependent.getZip()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(37),37,rawDependent.getDateOfBirth()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(64),64,rawDependent.getCustomField1()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(65),65,rawDependent.getCustomField2()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(66),66,rawDependent.getCustomField3()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(67),67,rawDependent.getCustomField4()));
		addCell(getMapperCell(rows.size()+1, mapperDisplay.getMapperColumn(68),68,rawDependent.getCustomField5()));

		rows.add(columns);
		rowInfo.add( new MapperRowInfo(RawRecordType.DEPENDENT, rawDependent.getId(), rows.size()));
	}
	
	private void createCellRow(RawIrs1094c raw1094c, String Errors)
	{
		currentMapperRow++;

		columns = FXCollections.observableArrayList();
		headerColumns = FXCollections.observableArrayList();
		headerNames = new ArrayList<String>();
		// the error column
		columns.add(getMapperCell(rows.size()+1, 0, 0, Errors));
		
		addCell(getMapperCell(rows.size()+1, 1, 1, raw1094c.getYear()));
		addCell(getMapperCell(rows.size()+1, 2, 2, raw1094c.getEIN()));
		addCell(getMapperCell(rows.size()+1, 3, 3, raw1094c.getCompanyName()));
		addCell(getMapperCell(rows.size()+1, 4, 4, raw1094c.getAnnFTEEs()));
		addCell(getMapperCell(rows.size()+1, 5, 5, raw1094c.getAnnEEs()));
		addCell(getMapperCell(rows.size()+1, 6, 6, raw1094c.getJanFTEEs()));
		addCell(getMapperCell(rows.size()+1, 7, 7, raw1094c.getJanEEs()));
		addCell(getMapperCell(rows.size()+1, 8, 8, raw1094c.getFebFTEEs()));
		addCell(getMapperCell(rows.size()+1, 9, 9, raw1094c.getFebEEs()));
		addCell(getMapperCell(rows.size()+1, 10, 10, raw1094c.getMarFTEEs()));
		addCell(getMapperCell(rows.size()+1, 11, 11, raw1094c.getMarEEs()));
		addCell(getMapperCell(rows.size()+1, 12, 12, raw1094c.getAprFTEEs()));
		addCell(getMapperCell(rows.size()+1, 13, 13, raw1094c.getAprEEs()));
		addCell(getMapperCell(rows.size()+1, 14, 14, raw1094c.getMayFTEEs()));
		addCell(getMapperCell(rows.size()+1, 15, 15, raw1094c.getMayEEs()));
		addCell(getMapperCell(rows.size()+1, 16, 16, raw1094c.getJunFTEEs()));
		addCell(getMapperCell(rows.size()+1, 17, 17, raw1094c.getJunEEs()));
		addCell(getMapperCell(rows.size()+1, 18, 18, raw1094c.getJulFTEEs()));
		addCell(getMapperCell(rows.size()+1, 19, 19, raw1094c.getJulEEs()));
		addCell(getMapperCell(rows.size()+1, 20, 20, raw1094c.getAugFTEEs()));
		addCell(getMapperCell(rows.size()+1, 21, 21, raw1094c.getAugEEs()));
		addCell(getMapperCell(rows.size()+1, 22, 22, raw1094c.getSepFTEEs()));
		addCell(getMapperCell(rows.size()+1, 23, 23, raw1094c.getSepEEs()));
		addCell(getMapperCell(rows.size()+1, 24, 24, raw1094c.getOctFTEEs()));
		addCell(getMapperCell(rows.size()+1, 25, 25, raw1094c.getOctEEs()));
		addCell(getMapperCell(rows.size()+1, 26, 26, raw1094c.getNovFTEEs()));
		addCell(getMapperCell(rows.size()+1, 27, 27, raw1094c.getNovEEs()));
		addCell(getMapperCell(rows.size()+1, 28, 28, raw1094c.getDecFTEEs()));
		addCell(getMapperCell(rows.size()+1, 29, 29, raw1094c.getDecEEs()));
		addCell(getMapperCell(rows.size()+1, 30, 30, raw1094c.getCustomField1()));
		addCell(getMapperCell(rows.size()+1, 31, 31, raw1094c.getCustomField2()));
		addCell(getMapperCell(rows.size()+1, 32, 32, raw1094c.getCustomField3()));
		addCell(getMapperCell(rows.size()+1, 33, 33, raw1094c.getCustomField4()));
		addCell(getMapperCell(rows.size()+1, 34, 34, raw1094c.getCustomField5()));
		rows.add(columns);
		rowInfo.add( new MapperRowInfo(RawRecordType.IRS1094C, raw1094c.getId(), rows.size()));
	}

	private void addCell(SpreadsheetCell cell)
	{
		
		if(cell == null) return;
		//if(cell.getRow() > 49) return;

		columns.add(cell);
	}

	private SpreadsheetCell getFillerCell(int row)
	{
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, "");
		newCell.setEditable(false);
		//newCell.getStyleClass().add("-fx-background-color: #eeffee;");
		newCell.setStyle("-fx-background-color: #ddffdd;");
		return newCell;
	}

	
	private SpreadsheetCell getMapperCell(int row, int column, int fieldpos, String data)
	{
		if(column == -1) return null;		
		if(data == null) data = "";

		// check for garbage if not an error tag
		if (column > 0) {
			char[] dataChar = data.toCharArray();
			for (int i = 0; i < data.length(); i++) {
				if (dataChar[i] < 32 || dataChar[i] > 127)
					dataChar[i] = 94; // make it an '^'
			}
			data = String.valueOf(dataChar);
		}
		
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, data);
		//newCell.setEditable(true);
		if(data.trim().length() > 0 )
		{
			newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		}
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		return newCell;
	}

	private SpreadsheetCell getMapperCell(int row, int column,  int fieldpos, boolean data)
	{
		if(column == -1) return null;

		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, String.valueOf(data));
		//newCell.setEditable(true);
		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		return newCell;
	}

	private SpreadsheetCell getUnionTypeCell(int row, int column, int fieldpos, UnionType utype, String fileRef)
	{
		if(column == -1) return null;
		String data = "";
		if (utype != null)
			data = utype.toString();
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, data);
	
		//newCell.setEditable(true);
		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		// store the ref for lookup later
		refData.add(new RefData(ReferenceType.PYUNIONTYPE, fileRef, newCell.getRow(), newCell.getColumn()));

		return newCell;
	}
	
	private SpreadsheetCell getERUnionTypeCell(int row, int column, int fieldpos, UnionType utype, String fileRef)
	{
		if(column == -1) return null;
		String data = "";
		if (utype != null)
			data = utype.toString();
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, data);
	
		//newCell.setEditable(true);
		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		// store the ref for lookup later
		refData.add(new RefData(ReferenceType.EEUNIONTYPE, fileRef, newCell.getRow(), newCell.getColumn()));

		return newCell;
	}
	
	private SpreadsheetCell getGenderTypeCell(int row, int column, int fieldpos, GenderType gtype, String fileRef)
	{
		if(column == -1) return null;
		String data = "";
		if (gtype != null)
			data = gtype.toString();
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, data);

		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		// store the ref for lookup later
		refData.add(new RefData(ReferenceType.PYGENDERTYPE, fileRef, newCell.getRow(), newCell.getColumn()));

		return newCell;
	}

	private SpreadsheetCell getERGenderTypeCell(int row, int column, int fieldpos, GenderType gtype, String fileRef)
	{
		if(column == -1) return null;
		String data = "";
		if (gtype != null)
			data = gtype.toString();
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, data);

		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		// store the ref for lookup later
		refData.add(new RefData(ReferenceType.EEGENDERTYPE, fileRef, newCell.getRow(), newCell.getColumn()));

		return newCell;
	}

	private SpreadsheetCell getPayCodeTypeCell(int row, int column, int fieldpos, PayCodeType ptype, String fileRef)
	{
		if(column == -1) return null;
		String data = "";
		if (ptype != null)
			data = ptype.toString();
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, data);
	
		//newCell.setEditable(true);
		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		// store the ref for lookup later
		refData.add(new RefData(ReferenceType.PYPAYCODE, fileRef, newCell.getRow(), newCell.getColumn()));

		return newCell;
	}

	private SpreadsheetCell getERPayCodeTypeCell(int row, int column, int fieldpos, PayCodeType ptype, String fileRef)
	{
		if(column == -1) return null;
		String data = "";
		if (ptype != null)
			data = ptype.toString();
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, data);
	
		//newCell.setEditable(true);
		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		// store the ref for lookup later
		refData.add(new RefData(ReferenceType.EEPAYCODE, fileRef, newCell.getRow(), newCell.getColumn()));

		return newCell;
	}

	private SpreadsheetCell getERDepartmentCell(int row, int column, int fieldpos, Department department, String fileRef)
	{
		if(column == -1) return null;
		
		// look up the department if we have an id
		String data = "";
		if (department != null) {
			data = department.getName();
		}
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, data);
	
		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		// store the ref for lookup later
		refData.add(new RefData(ReferenceType.EEDEPARTMENT, fileRef, newCell.getRow(), newCell.getColumn()));

		return newCell;
	}

	private SpreadsheetCell getERLocationCell(int row, int column, int fieldpos, Location location, String fileRef)
	{
		if(column == -1) return null;
		
		// look up the department if we have an id
		String data = "";
		if (location != null) {
			data = location.getName();
		}
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, data);
	
		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		// store the ref for lookup later
		refData.add(new RefData(ReferenceType.EELOCATION, fileRef, newCell.getRow(), newCell.getColumn()));

		return newCell;
	}
	
	private SpreadsheetCell getERCoverageGroupCell(int row, int column, int fieldpos, CoverageGroup coverageGroup, String fileRef)
	{
		if(column == -1) return null;
		
		// look up the department if we have an id
		String data = "";
		if (coverageGroup != null) {
			data = coverageGroup.getName();
		}
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, data);
	
		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		// store the ref for lookup later
		refData.add(new RefData(ReferenceType.EECOVERAGEGROUP, fileRef, newCell.getRow(), newCell.getColumn()));

		return newCell;
	}
	
	private SpreadsheetCell getPayFrequencyTypeCell(int row, int column, int fieldpos, PayFrequencyType ptype, String fileRef)
	{
		if(column == -1) return null;

		String data = "";
		if (ptype != null)
			data = ptype.toString();
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, data);
		
		//newCell.setEditable(true);
		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		// store the ref for lookup later
		refData.add(new RefData(ReferenceType.PYPAYFREQUENCY, fileRef, newCell.getRow(), newCell.getColumn()));

		return newCell;
	}

	private SpreadsheetCell getMapperCell(int row, int column,  int fieldpos, int data)
	{
		if(column == -1) return null;
		
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, String.valueOf(data));
		//newCell.setEditable(false);
		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		return newCell;
	}

	private SpreadsheetCell getMapperCell(int row, int column,  int fieldpos, Long data)
	{
		if(column == -1) return null;
		
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, String.valueOf(data));
		//newCell.setEditable(false);
		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		return newCell;
	}

	private SpreadsheetCell getMapperCell(int row, int column,  int fieldpos, Double data)
	{
		if(column == -1) return null;
		if(data == null) data = 0.0;
		
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, String.valueOf(data));
		//newCell.setEditable(true);
		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		return newCell;
	}

	private SpreadsheetCell getMapperCell(int row, int column,  int fieldpos, Float data)
	{
		if(column == -1) return null;
		if(data == null) data = 0f;
		
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, String.valueOf(data));
		//newCell.setEditable(true);
		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		return newCell;
	}

	private SpreadsheetCell getMapperCell(int row, int column, int fieldpos, SSN ssn)
	{
		if(column == -1) return null;

		String data = "";
		if(ssn != null)
			data = ssn.getUsrln();
		
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, data);
		//newCell.setEditable(true);
		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		return newCell;
	}

	private SpreadsheetCell getMapperCell(int row, int column,  int fieldpos, Calendar date)
	{
		if(column == -1) return null;

		String data = "";
		if(date != null)
			data = Utils.getDateString(date);
		
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, columns.size(), 1, 1, data);
		//newCell.setEditable(true);
		newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		//create a header row cell
		SpreadsheetCell headerCell = SpreadsheetCellType.STRING.createCell(0, headerColumns.size(), 1, 1, mapperDisplay.columnFields[column]);
		headerCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
		tableFieldPositions[headerColumns.size()] = fieldpos;
		headerColumns.add(headerCell);
		// add the header name
		headerNames.add(getAlphaName(column));
		return newCell;
	}
	
	private int getTableFieldPos(int tableColumn)
	{
		return tableFieldPositions[tableColumn];
	}

	private void loadFileRecordRejections() 
	{
		try {
			// clear anything already in the list
			rwfldRecordRejectionsList.getItems().clear();
			rwfldRecordRejectionsHeaderLabel.setText("Pipeline File Record Rejections (total: 0)");
			
			//String sAddress;
			List<PipelineFileRecordRejection> rejections = null;
			List<HBoxCell> list = new ArrayList<>();
			String recordIndex;
			switch(queue.getFileType()) 
			{
				case EMPLOYEE:
			       	PipelineFileRecordRejectionRequest empReq = new PipelineFileRecordRejectionRequest();
			       	empReq.setEmployeeFileId(queue.getEmployeeFile().getId());
			       	empReq.setId(queue.getEmployeeFile().getId());
			       	queue.getEmployeeFile().setRecordRejections(AdminPersistenceManager.getInstance().getAll(empReq));
	
			       	if(queue.getEmployeeFile().getRecordRejections() == null) return;
					if(queue.getEmployeeFile().getRecordRejections().size() < 1) return;
					list.add(new HBoxCell("Date", "Record Index", "Message", true));	
					rejections = queue.getEmployeeFile().getRecordRejections();
					break;
				case PAY:
			       	PipelineFileRecordRejectionRequest payReq = new PipelineFileRecordRejectionRequest();
			       	payReq.setPayFileId(queue.getPayFile().getId());
			       	payReq.setId(queue.getPayFile().getId());
			       	queue.getPayFile().setRecordRejections(AdminPersistenceManager.getInstance().getAll(payReq));
	
			       	if(queue.getPayFile().getRecordRejections() == null) return;
					if(queue.getPayFile().getRecordRejections().size() < 1) return;
					rejections = queue.getPayFile().getRecordRejections();
					break;
				case COVERAGE:
			       	PipelineFileRecordRejectionRequest covReq = new PipelineFileRecordRejectionRequest();
			       	covReq.setCoverageFileId(queue.getCoverageFile().getId());
			       	covReq.setId(queue.getCoverageFile().getId());
			       	queue.getCoverageFile().setRecordRejections(AdminPersistenceManager.getInstance().getAll(covReq));
		
					if(queue.getCoverageFile().getRecordRejections() == null) return;
					if(queue.getCoverageFile().getRecordRejections().size() < 1) return;
					rejections = queue.getCoverageFile().getRecordRejections();
					break;
				case IRS1094C:
			       	PipelineFileRecordRejectionRequest irsCReq = new PipelineFileRecordRejectionRequest();
			       	irsCReq.setIrs1094cFileId(queue.getIrs1094cFile().getId());
			       	irsCReq.setId(queue.getIrs1094cFile().getId());
			       	queue.getIrs1094cFile().setRecordRejections(AdminPersistenceManager.getInstance().getAll(irsCReq));
	
			       	if(queue.getIrs1094cFile().getRecordRejections() == null) return;
					if(queue.getIrs1094cFile().getRecordRejections().size() < 1) return;
					rejections = queue.getIrs1094cFile().getRecordRejections();
					break;
				case IRS1095C:
			       	PipelineFileRecordRejectionRequest irsCreq = new PipelineFileRecordRejectionRequest();
			       	irsCreq.setIrs1095cFileId(queue.getIrs1095cFile().getId());
			       	irsCreq.setId(queue.getIrs1095cFile().getId());
			       	queue.getIrs1095cFile().setRecordRejections(AdminPersistenceManager.getInstance().getAll(irsCreq));
	
					if(queue.getIrs1095cFile().getRecordRejections() == null) return;
					if(queue.getIrs1095cFile().getRecordRejections().size() < 1) return;
					rejections = queue.getIrs1095cFile().getRecordRejections();
					break;
				case IRSAIRERR:
			       	PipelineFileRecordRejectionRequest airEReq = new PipelineFileRecordRejectionRequest();
			       	airEReq.setAirTranErrorFileId(queue.getAirTranErrorFile().getId());
			       	airEReq.setId(queue.getAirTranErrorFile().getId());
			       	queue.getAirTranErrorFile().setRecordRejections(AdminPersistenceManager.getInstance().getAll(airEReq));
	
					if(queue.getAirTranErrorFile().getRecordRejections() == null) return;
					if(queue.getAirTranErrorFile().getRecordRejections().size() < 1) return;
					rejections = queue.getAirTranErrorFile().getRecordRejections();
					break;
				case IRSAIRRCPT:
			       	PipelineFileRecordRejectionRequest airRReq = new PipelineFileRecordRejectionRequest();
			       	airRReq.setAirTranReceiptFileId(queue.getAirTranReceiptFile().getId());
			       	airRReq.setId(queue.getAirTranReceiptFile().getId());
			       	queue.getAirTranReceiptFile().setRecordRejections(AdminPersistenceManager.getInstance().getAll(airRReq));
	
					if(queue.getAirTranReceiptFile().getRecordRejections() == null) return;
					if(queue.getAirTranReceiptFile().getRecordRejections().size() < 1) return;
					rejections = queue.getAirTranReceiptFile().getRecordRejections();
					break;
				default:
					break;
			}
			
			if(rejections == null) return;
			
			list.add(new HBoxCell("Date", "Record Index", "Message", true));			
			for (PipelineFileRecordRejection rejection : rejections) 
			{
				if(rejection.getRecordInformation() == null)
					recordIndex = "null";
				else
					recordIndex = String.valueOf(rejection.getRecordInformation().getRecordIndex());
				list.add(new HBoxCell(Utils.getDateString(rejection.getLastUpdated()),
						recordIndex, rejection.getMessage(), false)); 
			}
	
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
	        rwfldRecordRejectionsList.setItems(myObservableList);		
	        //update our label
	        int rejectionCount = rwfldRecordRejectionsList.getItems().size() - 1;
	        if(rejectionCount < 0) rejectionCount = 0;
	        rwfldRecordRejectionsHeaderLabel.setText("Pipeline File Record Rejections (total: " + String.valueOf(rejectionCount) + ")" );
		} catch (Exception e) {
        	DataManager.i().log( Level.SEVERE, e);
		}
	}
	
	public void dataReady() {
		// hide the waiting sign
		rwfldLoading.setVisible(false);
		if (updating == false) {
			// disable the sub controls accordingly
			disableSubControls(!rawData.subFilesPresent);
			// show the sub files
			showSubfiles();
		}
		// show the raw fields
		showRawFields();
		updating = false;
	}
	
	
	private void showSubfiles()
	{
		if(rawData.subFilesPresent == false) return;
		rwfldRawTypeChoice.getItems().clear();
		
		try {
			String name = "";
			Employer emp = null;
			switch(queue.getFileType()) {
			case EMPLOYEE:
				for (EmployeeFile eFile : rawData.mSubEmployeeFiles) {
					if(eFile.getPipelineInformation() != null && eFile.getPipelineInformation().getEmployerId() != null){
		            	emp = AdminPersistenceManager.getInstance().get(Employer.class, eFile.getPipelineInformation().getEmployerId());	
						name = emp.getName();
					} else
						name = eFile.getId().toString();
					rwfldRawTypeChoice.getItems().add(name);
				}
				break;
			case PAY:
				for (PayFile pFile : rawData.mSubPayFiles) {
					if(pFile.getPipelineInformation() != null && pFile.getPipelineInformation().getEmployerId() != null){
		            	emp = AdminPersistenceManager.getInstance().get(Employer.class, pFile.getPipelineInformation().getEmployerId()); 	
						name = emp.getName();
					} else
						name = pFile.getId().toString();
					rwfldRawTypeChoice.getItems().add(name);
				}
				break;
			case COVERAGE:
				for (CoverageFile cFile : rawData.mSubCoverageFiles) {
					if(cFile.getPipelineInformation() != null && cFile.getPipelineInformation().getEmployerId() != null){
		            	emp = AdminPersistenceManager.getInstance().get(Employer.class, cFile.getPipelineInformation().getEmployerId()); 	
						name = emp.getName();
					} else
						name = cFile.getId().toString();
					rwfldRawTypeChoice.getItems().add(name);
				}
				break;
			default:
				break;
			}
			
			//Collections.sort(rwfldRawTypeChoice.getItems());
			rwfldRawTypeChoice.getSelectionModel().select(rawData.currentSubFile);
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e);
		}
	}
	
	private void updateControlData() {
		spec = null;

		try
		{
			// new thread
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception 
	            {

	        		//load the filespec
	        		if((spec = queue.getRequest().getSpecification()) == null) return null;
	        		switch(queue.getFileType()) {
	        		case EMPLOYEE:
	        			if(queue.getEmployeeFile() == null)  return  null;
	            		DataManager.i().loadDynamicEmployeeFileSpecification(spec.getDynamicFileSpecificationId());
	        			DataManager.i().mPipelineSpecification = spec;
	        			DataManager.i().mPipelineChannel = spec.getChannel();
	        			break;
	        		case PAY:
	        			if(queue.getPayFile() == null) return null;
	        			if((spec = queue.getPayFile().getSpecification()) == null) return null;
	            		DataManager.i().loadDynamicPayFileSpecification(spec.getDynamicFileSpecificationId());
	        			DataManager.i().mPipelineSpecification = spec;
	        			DataManager.i().mPipelineChannel = spec.getChannel();
	        			break;
	        		case COVERAGE:
	        			if(queue.getCoverageFile() == null) return null;
	            		DataManager.i().loadDynamicCoverageFileSpecification(spec.getDynamicFileSpecificationId());
	            		if(DataManager.i().mDynamicCoverageFileSpecification == null) return null;
	        			DataManager.i().mPipelineSpecification = spec;
	        			DataManager.i().mPipelineChannel = spec.getChannel();
	        			break;
	        		case IRS1095C:
	        			if(queue.getIrs1095cFile() == null) return null;
	        			DataManager.i().mPipelineSpecification = spec;
	        			DataManager.i().mPipelineChannel = spec.getChannel();
	        			break;
	        		case IRS1094C:
	        			if(queue.getIrs1094cFile() == null) return null;
	        			DataManager.i().mPipelineSpecification = spec;
	        			DataManager.i().mPipelineChannel = spec.getChannel();
	        			break;
	        		case IRSAIRERR:
	        			if(queue.getAirTranErrorFile() == null) return null;
	        			DataManager.i().mPipelineSpecification = spec;
	        			DataManager.i().mPipelineChannel = spec.getChannel();
	        			break;
	        		case IRSAIRRCPT:
	        			if(queue.getAirTranReceiptFile() == null) return null;
	        			DataManager.i().mPipelineSpecification = spec;
	        			DataManager.i().mPipelineChannel = spec.getChannel();
	        			break;
	        		default:
	        			// no file spec
	        			break;
	        		}

	        		return null;
	            }
	        };
	        
	    	task.setOnSucceeded(e ->  {
	    		updateControls();
	    	});
	    	task.setOnFailed(e ->  { 
	    		updateControls();
	    		});
	        
	    	AdminApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e) { 
			DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

	}
	
	private void updateControls() {
		// show the layout
		createMapperDisplay();

		if (queue.getRequest().getSpecification() == null) return;
		
		// update if not complete - this is a hibernate thing
		if (queue.getRequest().getSpecification().getDynamicFileSpecificationId() == null) {
			try {
				PipelineSpecificationRequest specReq = new PipelineSpecificationRequest();
				specReq.setId(queue.getRequest().getSpecificationId());
				queue.getRequest().setSpecification(AdminPersistenceManager.getInstance().get(specReq));
				if(queue.getRequest().getSpecification() == null) return;
			} catch (CoreException e) {
				DataManager.i().log(Level.SEVERE, e);
				return;
			}
		}
		
		spec = queue.getRequest().getSpecification();
		
		rwfldApproveConversion.setVisible(spec.isPostValidationApproval());
		switch(queue.getFileType()) {
		case EMPLOYEE:
			if(queue.getEmployeeFile() == null) return;
			rwfldCreateEmployeeCheck.setVisible(true);
			Utils.updateControl(rwfldCreateEmployeeCheck,DataManager.i().mDynamicEmployeeFileSpecification.isCreateEmployee());
			rwfldMapEEtoERCheck.setVisible(false);
			rwfldCreateSecondaryCheck.setVisible(false);
			rwfldCreatePlanCheck.setVisible(false);
			// reset the button if the file is marked for validation approval
			if(queue.getEmployeeFile().getPipelineInformation() != null && queue.getEmployeeFile().getPipelineInformation().isPostValidationApproved() == true)
				rwfldApproveConversion.setVisible(false);
			break;
		case PAY:
			if(queue.getPayFile() == null) return;
			//if((spec = queue.getPayFile().getSpecification()) == null) return;
			Utils.updateControl(rwfldCreateEmployeeCheck,DataManager.i().mDynamicPayFileSpecification.isCreateEmployee());
			rwfldMapEEtoERCheck.setVisible(true);
			Utils.updateControl(rwfldCreateSecondaryCheck,DataManager.i().mDynamicPayFileSpecification.isMapEEtoER());
			DataManager.i().mPipelineSpecification = spec;
			DataManager.i().mPipelineChannel = spec.getChannel();
			rwfldCreateSecondaryCheck.setVisible(true);
			rwfldCreateSecondaryCheck.setText("Create PayPeriod");
			rwfldCreatePlanCheck.setVisible(false);
			// reset the button if the file is marked for validation approval
			if(queue.getPayFile().getPipelineInformation() != null && queue.getPayFile().getPipelineInformation().isPostValidationApproved() == true)
				rwfldApproveConversion.setVisible(false);
			break;
		case COVERAGE:
			if(queue.getCoverageFile() == null) return;
    		if(DataManager.i().mDynamicCoverageFileSpecification == null) return;
			rwfldCreateSecondaryCheck.setVisible(true);
			rwfldCreateSecondaryCheck.setText("Create Secondary");
			Utils.updateControl(rwfldCreateSecondaryCheck,DataManager.i().mDynamicCoverageFileSpecification.isCreateDependent());
			rwfldMapEEtoERCheck.setVisible(true);
			Utils.updateControl(rwfldMapEEtoERCheck,DataManager.i().mDynamicCoverageFileSpecification.isMapEEtoER());
			rwfldCreateEmployeeCheck.setVisible(true);
			Utils.updateControl(rwfldCreateEmployeeCheck,DataManager.i().mDynamicCoverageFileSpecification.isCreateEmployee());
			rwfldCreatePlanCheck.setVisible(true);
			Utils.updateControl(rwfldCreatePlanCheck,DataManager.i().mDynamicCoverageFileSpecification.isCreateBenefit());
			// reset the button if the file is marked for validation approval
			if(queue.getCoverageFile().getPipelineInformation() != null && queue.getCoverageFile().getPipelineInformation().isPostValidationApproved() == true)
				rwfldApproveConversion.setVisible(false);
			break;
		case IRS1095C:
			if(queue.getIrs1095cFile() == null) return;
			rwfldMapperIdButton.setText(spec.getDynamicFileSpecificationId().toString() + " (click to view)");;
			rwfldSpecIdButton.setText(spec.getId().toString() + " (click to view)");
			rwfldMapperSpecNameField.setText(spec.getName());
			rwfldCreateEmployeeCheck.setVisible(false);
			rwfldMapEEtoERCheck.setVisible(false);
			rwfldCreateSecondaryCheck.setVisible(false);
			rwfldCreatePlanCheck.setVisible(false);
			// reset the button if the file is marked for validation approval
			if(queue.getIrs1095cFile().getPipelineInformation() != null && queue.getIrs1095cFile().getPipelineInformation().isPostValidationApproved() == true)
				rwfldApproveConversion.setVisible(false);
			return;
		case IRS1094C:
			if(queue.getIrs1094cFile() == null) return;
			if (spec.getDynamicFileSpecificationId() != null)
				rwfldMapperIdButton.setText(spec.getDynamicFileSpecificationId().toString() + " (click to view)");;
			rwfldSpecIdButton.setText(spec.getId().toString() + " (click to view)");
			rwfldMapperSpecNameField.setText(spec.getName());
			rwfldCreateEmployeeCheck.setVisible(false);
			rwfldMapEEtoERCheck.setVisible(false);
			rwfldCreateSecondaryCheck.setVisible(false);
			rwfldCreatePlanCheck.setVisible(false);
			// reset the button if the file is marked for validation approval
			if(queue.getIrs1094cFile().getPipelineInformation() != null && queue.getIrs1094cFile().getPipelineInformation().isPostValidationApproved() == true)
				rwfldApproveConversion.setVisible(false);
			return;
		case IRSAIRERR:
			if(queue.getAirTranErrorFile() == null) return;
			rwfldMapperIdButton.setText(spec.getDynamicFileSpecificationId().toString() + " (click to view)");;
			rwfldSpecIdButton.setText(spec.getId().toString() + " (click to view)");
			rwfldMapperSpecNameField.setText(spec.getName());
			rwfldCreateEmployeeCheck.setVisible(false);
			rwfldMapEEtoERCheck.setVisible(false);
			rwfldCreateSecondaryCheck.setVisible(false);
			rwfldCreatePlanCheck.setVisible(false);
			// reset the button if the file is marked for validation approval
			if(queue.getAirTranErrorFile().getPipelineInformation() != null && queue.getAirTranErrorFile().getPipelineInformation().isPostValidationApproved() == true)
				rwfldApproveConversion.setVisible(false);
			return;
		case IRSAIRRCPT:
			if(queue.getAirTranReceiptFile() == null) return;
			rwfldMapperIdButton.setText(spec.getDynamicFileSpecificationId().toString() + " (click to view)");;
			rwfldSpecIdButton.setText(spec.getId().toString() + " (click to view)");
			rwfldMapperSpecNameField.setText(spec.getName());
			rwfldCreateEmployeeCheck.setVisible(false);
			rwfldMapEEtoERCheck.setVisible(false);
			rwfldCreateSecondaryCheck.setVisible(false);
			rwfldCreatePlanCheck.setVisible(false);
			// reset the button if the file is marked for validation approval
			if(queue.getAirTranReceiptFile().getPipelineInformation() != null && queue.getAirTranReceiptFile().getPipelineInformation().isPostValidationApproved() == true)
				rwfldApproveConversion.setVisible(false);
			return;
		default:
			// no file spec
			return;
		}
		
		rwfldMapperIdButton.setText(spec.getDynamicFileSpecificationId().toString() + " (click to view)");;
		rwfldSpecIdButton.setText(spec.getId().toString() + " (click to view)");
		rwfldMapperSpecNameField.setText(spec.getName());
		
		// now we are ready for the actual data
		rawData.updateData(DataManager.i().mPipelineQueueEntry);
	}
	
	private void undoSPVChange() {
		if (spvUndoStack.size() < 1) return;
		
		// set our flag to not save the undo as another change
		undoActive = true;
		
		// get the next change to undo
		GridChange lastChange = null;
		lastChange = spvUndoStack.pop();
		mapperSPV.getGrid().setCellValue(lastChange.getRow(), lastChange.getColumn(), lastChange.getOldValue());
		// if we have nothing to undo or save, disable our buttons
		if (spvUndoStack.size() < 1) {
			rwfldUndoButton.setVisible(false);
			rwfldSaveChangesButton.setVisible(false);
		}
	}
	
	private void showDataEditedIndicator (boolean bShow) {
		// set the flag
		dataEdited = bShow;
		// show or hide the indicator
		rwfldDataChangedReprocess.setVisible(bShow);
	}
	
	private void Analyse() {
		switch(queue.getFileType()) {
		case EMPLOYEE:
			break;
		case PAY:
			// validate any present paycodes
			break;
		case COVERAGE:
			break;
		case IRS1095C:
			return;
		case IRS1094C:
			return;
		case IRSAIRERR:
			return;
		case IRSAIRRCPT:
			return;
		default:
			// no file spec
			return;
		}
		// check to see if the pay codes are found
	}
	
	private int getPayCodeField() {
		
	//	if (queue.get)
		return -1;
	}
	
	private GenderType showGenderTypeReferencePopup(String fileReference, Employer employer) {
		try {
            // load the popup
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/raw/ViewPipelineRawReference.fxml"));
			Parent ControllerNode = loader.load();
	        ViewPipelineRawReferenceController refController = (ViewPipelineRawReferenceController) loader.getController();
	        refController.setReferenceType(ReferenceType.PYGENDERTYPE, fileReference, employer);
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        return refController.getGenderType();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}   
		
		return null;
	}

	private GenderType showERGenderTypeReferencePopup(String fileReference, Employer employer) {
		try {
            // load the popup
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/raw/ViewPipelineRawReference.fxml"));
			Parent ControllerNode = loader.load();
	        ViewPipelineRawReferenceController refController = (ViewPipelineRawReferenceController) loader.getController();
	        refController.setReferenceType(ReferenceType.EEGENDERTYPE, fileReference, employer);
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        return refController.getGenderType();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}   
		
		return null;
	}

	private PayCodeType showPayCodeReferencePopup(String fileReference, Employer employer) {
		try {
            // load the popup
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/raw/ViewPipelineRawPayCodeReference.fxml"));
			Parent ControllerNode = loader.load();
	        ViewPipelineRawPayCodeReferenceController refController = (ViewPipelineRawPayCodeReferenceController) loader.getController();
	        refController.setReferenceType(ReferenceType.PYPAYCODE, fileReference, employer);
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        return refController.getPayCodeType();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}   
		
		return null;
	}

	private PayFrequencyType showPayFrequencyReferencePopup(String fileReference, Employer employer) {
		try {
            // load the popup
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/raw/ViewPipelineRawReference.fxml"));
			Parent ControllerNode = loader.load();
	        ViewPipelineRawReferenceController refController = (ViewPipelineRawReferenceController) loader.getController();
	        refController.setReferenceType(ReferenceType.PYPAYFREQUENCY, fileReference, employer);
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        return refController.getPayFreqType();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}   
		
		return null;
	}

	private UnionType showUnionTypeReferencePopup(String fileReference, Employer employer) {
		try {
            // load the popup
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/raw/ViewPipelineRawReference.fxml"));
			Parent ControllerNode = loader.load();
	        ViewPipelineRawReferenceController refController = (ViewPipelineRawReferenceController) loader.getController();
	        refController.setReferenceType(ReferenceType.PYUNIONTYPE, fileReference, employer);
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        return refController.getUnionType();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}   
		
		return null;
	}

	private UnionType showERUnionTypeReferencePopup(String fileReference, Employer employer) {
		try {
            // load the popup
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/raw/ViewPipelineRawReference.fxml"));
			Parent ControllerNode = loader.load();
	        ViewPipelineRawReferenceController refController = (ViewPipelineRawReferenceController) loader.getController();
	        refController.setReferenceType(ReferenceType.EEUNIONTYPE, fileReference, employer);
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        return refController.getUnionType();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}   
		
		return null;
	}

	private Department showERDepartmentReferencePopup(String fileReference, Employer employer) {
		try {
            // load the popup
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/raw/ViewPipelineRawReference.fxml"));
			Parent ControllerNode = loader.load();
	        ViewPipelineRawReferenceController refController = (ViewPipelineRawReferenceController) loader.getController();
	        refController.setReferenceType(ReferenceType.EEDEPARTMENT, fileReference, employer);
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        return refController.getDepartment();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}   
		
		return null;
	}

	private Location showERLocationReferencePopup(String fileReference, Employer employer) {
		try {
            // load the popup
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/raw/ViewPipelineRawReference.fxml"));
			Parent ControllerNode = loader.load();
	        ViewPipelineRawReferenceController refController = (ViewPipelineRawReferenceController) loader.getController();
	        refController.setReferenceType(ReferenceType.EELOCATION, fileReference, employer);
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        return refController.getLocation();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}   
		
		return null;
	}

	private CoverageGroup showERCoverageGroupReferencePopup(String fileReference, Employer employer) {
		try {
            // load the popup
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/raw/ViewPipelineRawReference.fxml"));
			Parent ControllerNode = loader.load();
	        ViewPipelineRawReferenceController refController = (ViewPipelineRawReferenceController) loader.getController();
	        refController.setReferenceType(ReferenceType.EECOVERAGEGROUP, fileReference, employer);
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        return refController.getCoverageGroup();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}   
		
		return null;
	}

	@FXML
	private void onRawDataType(ActionEvent event) {
		//currentRawDataType = rwfldRawTypeChoice.getSelectionModel().getSelectedIndex();
		//showRawFields();
	}	

	@FXML
	private void onLoadAllFields(ActionEvent event) {
		// reset the errorfields flag
		getErrorFields = false;
		// hang up a loading sign
		rwfldLoading.setVisible(true);		
		// and reload the data
    	rawData.getAllData(false);
	}	

	@FXML
	private void onSpecId(ActionEvent event) {
		DataManager.i().setScreenType(ScreenType.PIPELINESPECIFICATIONFROMRAW);
		EtcAdmin.i().setScreen(ScreenType.PIPELINESPECIFICATIONFROMRAW, true);
	}	
	
	@FXML
	private void onMapperId(ActionEvent event) {
		 
		switch (queue.getFileType()) {
		case COVERAGE:
			DataManager.i().setScreenType(ScreenType.MAPPERCOVERAGEFROMRAW);
			EtcAdmin.i().setScreen(ScreenType.MAPPERCOVERAGEFROMRAW, true);
			break;
		case EMPLOYEE:
			DataManager.i().setScreenType(ScreenType.MAPPEREMPLOYEEFROMRAW);
			EtcAdmin.i().setScreen(ScreenType.MAPPEREMPLOYEEFROMRAW, true);
			break;
		case PAY:
		default:
			DataManager.i().setScreenType(ScreenType.MAPPERPAYFROMRAW);
			EtcAdmin.i().setScreen(ScreenType.MAPPERPAYFROMRAW, true);
			break;
		}
	}	
	
	@FXML
	private void onUpdate(ActionEvent event) {
		// set a flag that we are updating, allows us to optimize the call in other places
		updating = true;
	    if(mapperSPV != null)
	    	rwfldVBox.getChildren().remove(mapperSPV);
	    mapperSPV = null;
	    
		rawData.currentSubFile = rwfldRawTypeChoice.getSelectionModel().getSelectedIndex();
		rwfldLoading.setVisible(true);
		rawData.updateData(DataManager.i().mPipelineQueueEntry);
	}	
	
	@FXML
	private void onApproveConversion(ActionEvent event) {
		rawData.approveConversion();
	}	
	
	@FXML
	private void onReprocess(ActionEvent event) {
		if (dataEdited == true) {
			if (rawData.resubmit() == true)
				showDataEditedIndicator(false);
		}else
			rawData.reprocess();
	}	
	
	@FXML
	private void onUndo(ActionEvent event) {
		undoSPVChange();
	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		// we will use the undo stack to gather the changes
		if (spvUndoStack.size() < 1) return;
		
		// get the next change to undo
		for (GridChange change : spvUndoStack) {
			// header changes are ignored
			if (change.getRow() == 0) continue;
			MapperRowInfo changedRow = rowInfo.get(change.getRow() - 1);
			switch(changedRow.type) {
			case EMPLOYEE:
				if (queue.getFileType() == PipelineFileType.PAY)
					updateRawEmployeePayData(changedRow, change);
				else
					updateRawEmployeeData(changedRow, change);
				break;
			case COVERAGE:
				updateRawCoverageData(changedRow, change);
				break;
			case PAY:
				updateRawPayData(changedRow, change);
				break;
			case DEPENDENT:
				updateRawDependentData(changedRow, change);
				break;
			case IRS1094C:
				updateRawIrs1094cData(changedRow, change);
				break;
			default:
				break;			
			}
		}
		
		rwfldUndoButton.setVisible(false);
		rwfldSaveChangesButton.setVisible(false);
		spvUndoStack.clear();
		showDataEditedIndicator(true);
		Utils.showAlert("Changes Saved", "Raw Data Field Changes saved. Note: Reprocess Request can be selected to process changes. \n\nIf this is sub file, please check the other sub files for errors before requesting reprocess.");
	}	
	
	private void updateRawEmployeeData(MapperRowInfo info, GridChange change ) {
		
		try {
			// get the employee, it should be cached
			RawEmployeeRequest eReq = new RawEmployeeRequest();
			eReq.setId(info.recordId);
			RawEmployee re = AdminPersistenceManager.getInstance().get(eReq);
			if (re != null) {
				// check the times on the dates to stop the tine zone bug
				Utils.checkDate(re.getDateOfBirth());
				Utils.checkDate(re.getHireDate());
				Utils.checkDate(re.getRehireDate());
				Utils.checkDate(re.getTermDate());
				//convert the column back into the correct field
				switch ( getTableFieldPos(change.getColumn())) {
					case 1:
						re.setEmployerIdentifier(change.getNewValue().toString());
						break;
					case 2:
						re.setEtcReferenceId(Long.valueOf(change.getNewValue().toString()));
						break;
					case 3:
						SSN ssn = new SSN();
						ssn.setUsrln(change.getNewValue().toString());
						re.setSsn(ssn);
						break;
					case 4:
						re.setEmployerIdentifier(change.getNewValue().toString());
						break;
					case 5:
						re.setPhone(change.getNewValue().toString());
						break;
					case 6:
						re.setEmail(change.getNewValue().toString());
						break;
					case 7:
						re.setFirstName(change.getNewValue().toString());
						break;
					case 8:
						re.setMiddleName(change.getNewValue().toString());
						break;
					case 9:
						re.setLastName(change.getNewValue().toString());
						break;
					case 10:
						re.setRehireDate( Utils.getCalDate(change.getNewValue().toString()));
						break;
					case 11:
						Department department = new Department();
						department.setDescription(change.getNewValue().toString());
						re.setDepartment(department);
						break;
					case 12:
						re.setStreetNumber(change.getNewValue().toString());
						break;
					case 13: // ADDRESS LINE 1
						re.setStreet(change.getNewValue().toString());
						break;
					case 14: // ADDRESS LINE 2
						re.setStreetExtension(change.getNewValue().toString());
						break;
					case 15:
						re.setCity(change.getNewValue().toString());
						break;
					case 16:
						re.setState(change.getNewValue().toString());
						break;
					case 17:
						re.setZip(change.getNewValue().toString());
						break;
					case 18:	
						re.setGenderReference(change.getNewValue().toString());
						break;
					case 19:
						re.setDateOfBirth( Utils.getCalDate(change.getNewValue().toString()));
						break;
					case 20:
						re.setHireDate( Utils.getCalDate(change.getNewValue().toString()));
						break;
					case 21:
						re.setTermDate( Utils.getCalDate(change.getNewValue().toString()));
						break;
					case 22:
						Location location = new Location();
						location.setDescription(change.getNewValue().toString());
						re.setLocation(location);
						break;
					case 23:
						re.setJobTitle(change.getNewValue().toString());
						break;
					case 24:
						PayCodeType pct = PayCodeType.valueOf(change.getNewValue().toString());
						re.setPayCodeType(pct);
						break;
					case 25:
						UnionType ut = UnionType.valueOf(change.getNewValue().toString());
						re.setUnionType(ut);
						break;
					case 26:
						//re.setEmployerIdentifier(change.getNewValue().toString());
						//CheckColumn(26,"COVERAGE GROUP", fSpec.isCvgGroupCol(), fSpec.getCvgGroupColIndex(), null, null);
						break;
					case 60:
						re.setCustomField1(change.getNewValue().toString());
						break;
					case 61:
						re.setCustomField2(change.getNewValue().toString());
						break;
					case 62:
						re.setCustomField3(change.getNewValue().toString());
						break;
					case 63:
						re.setCustomField4(change.getNewValue().toString());
						break;
					case 64:
						re.setCustomField5(change.getNewValue().toString());
						break;
					case -1:
						return;
					}
				
				// now save it
				eReq.setEntity(re);
				re = AdminPersistenceManager.getInstance().addOrUpdate(eReq);		
			}
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e);
		}
	}
	
	private void updateRawEmployeePayData(MapperRowInfo info, GridChange change ) {
		
		try {
			// get the employee, it should be cached
			RawEmployeeRequest eReq = new RawEmployeeRequest();
			eReq.setId(info.recordId);
			RawEmployee re = AdminPersistenceManager.getInstance().get(eReq);

			// get the mapper for the reference info
			DynamicPayFileSpecificationRequest mapperRequest = new DynamicPayFileSpecificationRequest();
			mapperRequest.setId(queue.getRequest().getSpecification().getDynamicFileSpecificationId());
			DynamicPayFileSpecification payMapper = AdminPersistenceManager.getInstance().get(mapperRequest);
			
			if (re != null) {
				// check the times on the dates to stop the tine zone bug
				Utils.checkDate(re.getDateOfBirth());
				Utils.checkDate(re.getHireDate());
				Utils.checkDate(re.getRehireDate());
				Utils.checkDate(re.getTermDate());
				//convert the column back into the correct field
				switch ( getTableFieldPos(change.getColumn())) {
					case 1:
						re.setEtcReferenceId(Long.valueOf(change.getNewValue().toString()));
						break;
					case 2:
						SSN ssn = new SSN();
						ssn.setUsrln(change.getNewValue().toString());
						re.setSsn(ssn);
						break;
					case 3:
						re.setEmployerIdentifier(change.getNewValue().toString());
						break;
					case 4:
						re.setFirstName(change.getNewValue().toString());
						break;
					case 5:
						re.setMiddleName(change.getNewValue().toString());
						break;
					case 6:
						re.setLastName(change.getNewValue().toString());
						break;
					case 7:
						re.setStreetNumber(change.getNewValue().toString());
						break;
					case 8: // ADDRESS LINE 1
						re.setStreet(change.getNewValue().toString());
						break;
					case 9: // ADDRESS LINE 2
						re.setStreetExtension(change.getNewValue().toString());
						break;
					case 10:
						re.setCity(change.getNewValue().toString());
						break;
					case 11:
						re.setState(change.getNewValue().toString());
						break;
					case 12:
						re.setZip(change.getNewValue().toString());
						break;
					case 17:
						// apply references if available
						//if (payMapper.getDeptReferences() != null) {
						//	for(DynamicPayFileDepartmentReference ref : payMapper.getDeptReferences())
						//		if(ref.isActive() && ref.getReference().equalsIgnoreCase(change.getNewValue().toString()) &&
						//		  (ref.getEmployerId() == null ? true : ref.getEmployerId().equals(queue.getRequest().getEmployerId())))
						//		{
						//			re.setDepartment(ref.getDepartment());
						//			break;
						//		}
						//} else {
							Department department = new Department();
							department.setDescription(change.getNewValue().toString());
							re.setDepartment(department);
						//}
						break;
					case 18:
						// apply references if available
						//if (payMapper.getLocReferences() != null) {
						//	for(DynamicPayFileLocationReference ref : payMapper.getLocReferences())
						//		if(ref.isActive() && ref.getReference().equalsIgnoreCase(change.getNewValue().toString()) &&
						//		  (ref.getEmployerId() == null ? true : ref.getEmployerId().equals(queue.getRequest().getEmployerId())))
						//		{
						//			re.setLocation(ref.getLocation());
						//			break;
						//		}
						//} else {
							Location location = new Location();
							location.setDescription(change.getNewValue().toString());
							re.setLocation(location);
						//}
						break;
					case 20:	
						// apply references if available
						//if (payMapper.getGenderReferences() != null) {
						//	for(DynamicPayFileGenderReference ref : payMapper.getGenderReferences())
						//		if(ref.isActive() && ref.getReference().equalsIgnoreCase(change.getNewValue().toString()) &&
						//		  (ref.getEmployerId() == null ? true : ref.getEmployerId().equals(queue.getRequest().getEmployerId())))
						//		{
						//			re.setGenderType(ref.getGenderType());
						//			break;
						//		}
						//} else
							re.setGenderType(GenderType.valueOf(change.getNewValue().toString()));
						break;
					case 13:
						re.setDateOfBirth( Utils.getCalDate(change.getNewValue().toString()));
						break;
					case 14:
						re.setHireDate( Utils.getCalDate(change.getNewValue().toString()));
						break;
					case 15:
						re.setRehireDate( Utils.getCalDate(change.getNewValue().toString()));
						break;
					case 16:
						re.setTermDate( Utils.getCalDate(change.getNewValue().toString()));
						break;
					case 21:
						re.setPhone(change.getNewValue().toString());
						break;
					case 22:
						re.setEmail(change.getNewValue().toString());
						break;
					case 23:
						re.setJobTitle(change.getNewValue().toString());
						break;
					case 24:
						// apply references if available
						//if (payMapper.getPayCodeReferences() != null) {
						//	for(DynamicPayFilePayCodeReference ref : payMapper.getPayCodeReferences())
						//		if(ref.isActive() && ref.getReference().equalsIgnoreCase(change.getNewValue().toString()) &&
						//		  (ref.getEmployerId() == null ? true : ref.getEmployerId().equals(queue.getRequest().getEmployerId())))
						//		{
						//			re.setPayCodeType(ref.getPayCodeType());
						//			break;
						//		}
						//} else {
							PayCodeType pct = PayCodeType.valueOf(change.getNewValue().toString());
							re.setPayCodeType(pct);
						//}
						break;
					case 25:
						// apply references if available
						//if (payMapper.getUnionTypeReferences() != null) {
						//	for(DynamicPayFileUnionTypeReference ref : payMapper.getUnionTypeReferences())
						//		if(ref.isActive() && ref.getReference().equalsIgnoreCase(change.getNewValue().toString()) &&
						//		  (ref.getEmployerId() == null ? true : ref.getEmployerId().equals(queue.getRequest().getEmployerId())))
						//		{
						//			re.setUnionType(ref.getUnionType());
						//			break;
						//		}
						//} else {
							UnionType ut = UnionType.valueOf(change.getNewValue().toString());
							re.setUnionType(ut);
						//}
						break;
					case 26:
						// apply references if available
						//if (payMapper.getCvgGroupReferences() != null) {
						//	for(DynamicPayFileCoverageGroupReference ref : payMapper.getCvgGroupReferences())
						//		if(ref.isActive() && ref.getReference().equalsIgnoreCase(change.getNewValue().toString()) &&
						//		  (ref.getEmployerId() == null ? true : ref.getEmployerId().equals(queue.getRequest().getEmployerId())))
						//		{
						//			re.setCoverageGroup(ref.getCoverageGroup());
						//			break;
						//		}
						//} //else
						//	re.setCoverageGroup(change.getNewValue().toString());
						break;
					case 42:
						re.setCustomField1(change.getNewValue().toString());
						break;
					case 43:
						re.setCustomField2(change.getNewValue().toString());
						break;
					case 44:
						re.setCustomField3(change.getNewValue().toString());
						break;
					case 45:
						re.setCustomField4(change.getNewValue().toString());
						break;
					case 46:
						re.setCustomField5(change.getNewValue().toString());
						break;
					case -1:
						return;
					}
				
				// now save it
				eReq.setEntity(re);
				re = AdminPersistenceManager.getInstance().addOrUpdate(eReq);		
			}
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e);
		}
	}
	
	private void updateRawCoverageData(MapperRowInfo info, GridChange change ) {
		
		try {
			// get the Coverage
			RawCoverageRequest eReq = new RawCoverageRequest();
			eReq.setId(info.recordId);
			RawCoverage rc = AdminPersistenceManager.getInstance().get(eReq);
			if (rc != null) {
				// check the times on the dates to stop the tine zone bug
				Utils.checkDate(rc.getEndDate());
				Utils.checkDate(rc.getStartDate());
				Utils.checkDate(rc.getDecisionDate());
				//convert the column back into the correct field
				switch ( getTableFieldPos(change.getColumn())) {
					case 38: // COVERAGE WAIVED
						rc.setWaived(Boolean.valueOf(change.getNewValue().toString()));
						break;
					case 39: // COVERAGE INELIGIBLE
						rc.setIneligible(Boolean.valueOf(change.getNewValue().toString()));
						break;
					case 40: // COVERAGE TAX YEAR
						rc.setTaxYear( Integer.valueOf(change.getNewValue().toString()));
						break;
					case 42: // COVERAGE START DATE
						rc.setStartDate( Utils.getCalDate(change.getNewValue().toString()));
						break;
					case 43: // COVERAGE END DATE
						rc.setEndDate( Utils.getCalDate(change.getNewValue().toString()));
						break;
					case 44: // COVERAGE TAX YEAR SELECTED
						rc.setTaxYearSelected(Boolean.valueOf(change.getNewValue().toString()));
						break;
					case 45: // COVERAGE JANUARY SELECTED
						rc.setJanuarySelected(Boolean.valueOf(change.getNewValue().toString()));
						break;
					case 46: // COVERAGE FEBRUARY SELECTED
						rc.setFebruarySelected(Boolean.valueOf(change.getNewValue().toString()));
						break;
					case 47: // COVERAGE MARCH SELECTED
						rc.setMarchSelected(Boolean.valueOf(change.getNewValue().toString()));
						break;
					case 48: // COVERAGE APRIL SELECTED
						rc.setAprilSelected(Boolean.valueOf(change.getNewValue().toString()));
						break;
					case 49: // COVERAGE MAY SELECTED
						rc.setMaySelected(Boolean.valueOf(change.getNewValue().toString()));
						break;
					case 50: // COVERAGE JUNE SELECTED
						rc.setJuneSelected(Boolean.valueOf(change.getNewValue().toString()));
						break;
					case 51: // COVERAGE JULY SELECTED
						rc.setJulySelected(Boolean.valueOf(change.getNewValue().toString()));
						break;
					case 52: // COVERAGE AUGUST SELECTED
						rc.setAugustSelected(Boolean.valueOf(change.getNewValue().toString()));
						break;
					case 53: // COVERAGE SEPTEMBER SELECTED
						rc.setSeptemberSelected(Boolean.valueOf(change.getNewValue().toString()));
						break;
					case 54: // COVERAGE OCTOBER SELECTED
						rc.setOctoberSelected(Boolean.valueOf(change.getNewValue().toString()));
						break;
					case 55: // COVERAGE NOVEMBER SELECTED
						rc.setNovemberSelected(Boolean.valueOf(change.getNewValue().toString()));
						break;
					case 56: // COVERAGE DECEMBER SELECTED
						rc.setDecemberSelected(Boolean.valueOf(change.getNewValue().toString()));
						break;
					case 57: // COVERAGE PLAN IDENTIFIER
						rc.setPlanReference((change.getNewValue().toString()));
						break;
					case 58: // COVERAGE DEDUCTION
						rc.setDeduction(Float.valueOf(change.getNewValue().toString()));
						break;
					case 59: // COVERAGE MEMBER SHARE AMT
						rc.setMemberShare(Float.valueOf(change.getNewValue().toString()));
						break;
					case 60:
						rc.setCustomField1(change.getNewValue().toString());
						break;
					case 61:
						rc.setCustomField2(change.getNewValue().toString());
						break;
					case -1:
						return;
				}
				
				// now save it
				eReq.setEntity(rc);
				rc = AdminPersistenceManager.getInstance().addOrUpdate(eReq);		
			}
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e);
		}
		
	}
	
	
	
	private void updateRawPayData(MapperRowInfo info, GridChange change ) {
		try {
			// get the Pay
			RawPayRequest eReq = new RawPayRequest();
			eReq.setId(info.recordId);
			RawPay rp = AdminPersistenceManager.getInstance().get(eReq);
			
			// get the mapper for the reference info
			DynamicPayFileSpecificationRequest mapperRequest = new DynamicPayFileSpecificationRequest();
			mapperRequest.setId(queue.getRequest().getSpecification().getDynamicFileSpecificationId());
			DynamicPayFileSpecification payMapper = AdminPersistenceManager.getInstance().get(mapperRequest);
			
			if (rp != null) {
				//convert the column back into the correct field
				switch ( getTableFieldPos(change.getColumn())) {
					case 24: // PPD START DATE
						rp.setPayPeriodStart( Utils.getCalDate(change.getNewValue().toString()));
						break;
					case 25: // PPD END DATE
						rp.setPayPeriodEnd( Utils.getCalDate(change.getNewValue().toString()));
						break;
					case 26: // PAY DATE
						rp.setPayDate( Utils.getCalDate(change.getNewValue().toString()));
						break;
					case 27: // PPD ID
						rp.setPayPeriodId( Long.valueOf(change.getNewValue().toString()));
						break;
					case 28: // PPD FREQUENCY
						// apply references if available
						//if (payMapper.getPpdFreqReferences() != null) {
						//	for(DynamicPayFilePayFrequencyReference ref : payMapper.getPpdFreqReferences())
						//		if(ref.isActive() && ref.getReference().equalsIgnoreCase(change.getNewValue().toString()) &&
						//		  (ref.getEmployerId() == null ? true : ref.getEmployerId().equals(queue.getRequest().getEmployerId())))
						//		{
						//			rp.setPayFreqType(ref.getPayFrequencyType());
						//			break;
						//		}
						//} else
							rp.setPayFreqType(PayFrequencyType.valueOf(change.getNewValue().toString()));
						break;
					case 29: // WORK DATE
						rp.setWorkDate( Utils.getCalDate(change.getNewValue().toString()));
						break;
					case 30: // HOURS
						rp.setHours( Float.valueOf(change.getNewValue().toString()));
						break;
					case 31: // RATE
						rp.setRate( Float.valueOf(change.getNewValue().toString()));
						break;
					case 32: // AMOUNT
						rp.setAmount( Float.valueOf(change.getNewValue().toString()));
						break;
					case 33: // PAYCODE
						String val = change.getNewValue().toString();
						// look for the values and set
						if (val.contains("FTS")) val = "FTS";
						else if (val.contains("FTH")) val = "FTH";
						else if (val.contains("VH")) val = "VH";
						else val = "";
						if (val.isEmpty() == false)
							rp.setPayCodeType(PayCodeType.valueOf(val));
						break;
					case 34: // UNION TYPE
						// apply references if available
						//if (payMapper.getUnionTypeReferences() != null) {
						//	for(DynamicPayFileUnionTypeReference ref : payMapper.getUnionTypeReferences())
						//		if(ref.isActive() && ref.getReference().equalsIgnoreCase(change.getNewValue().toString()) &&
						//		  (ref.getEmployerId() == null ? true : ref.getEmployerId().equals(queue.getRequest().getEmployerId())))
						//		{
						//			rp.setUnionType(ref.getUnionType());
						//			break;
						//		}
						//} else
							rp.setUnionType(UnionType.valueOf(change.getNewValue().toString()));
						break;
					case 35: // OT HRS
						rp.setOvertimeHours1( Float.valueOf(change.getNewValue().toString()));
						break;
					case 36: // OT 2 HRS
						rp.setOvertimeHours2( Float.valueOf(change.getNewValue().toString()));
						break;
					case 37: // OT 2 HRS
						rp.setOvertimeHours3( Float.valueOf(change.getNewValue().toString()));
						break;
					case 38: // PTO HRS
						rp.setPtoHours( Float.valueOf(change.getNewValue().toString()));
						break;
					case 39: // SICK PAY HRS
						rp.setSickHours( Float.valueOf(change.getNewValue().toString()));
						break;
					case 40: // HLDY PAY HRS
						rp.setHolidayHours( Float.valueOf(change.getNewValue().toString()));
						break;
					case 41: // PAY CLASS
						// apply references if available
						//if (payMapper.getPayClassReferences() != null) {
						//	for(DynamicPayFilePayClassReference ref : payMapper.getPayClassReferences())
						//		if(ref.isActive() && ref.getReference().equalsIgnoreCase(change.getNewValue().toString()) &&
						//		  (ref.getEmployerId() == null ? true : ref.getEmployerId().equals(queue.getRequest().getEmployerId())))
						//		{
						//			rp.setPayClass(ref.getPayClass());
						//			break;
						//		}
						//} //else
							//rp.setPayClass(PayClass.valueOf(change.getNewValue().toString()));
						break;
					case 47: // PayCFld1
						rp.setCustomField1(change.getNewValue().toString());
						break;
					case 48: // PayCFld2
						//rp.setCustomField1(ParserUtils.nullifyEmptyString(ppeval.parsePattern(row.get(dspec.getPayCfld1ColIndex()), dspec.getPayCfld1ParsePattern() != null ? dspec.getPayCfld1ParsePattern().getPattern() : null)));
						rp.setCustomField2(change.getNewValue().toString());
						break;
					case 49: // PayCFld3
						rp.setCustomField3(change.getNewValue().toString());
						break;
					case 50: // PayCFld4
						rp.setCustomField4(change.getNewValue().toString());
						break;
					case 51: // PayCFld5
						rp.setCustomField5(change.getNewValue().toString());
						break;
					case 52: // PayCFld6
						rp.setCustomField6(change.getNewValue().toString());
						break;
					case 53: // PayCFld7
						rp.setCustomField7(change.getNewValue().toString());
						break;
					case -1:
						return;
				}
				
				// now save it
				eReq.setEntity(rp);
				rp = AdminPersistenceManager.getInstance().addOrUpdate(eReq);		
			}
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e);
		}
	}
	
	private void updateColumnHeaders() {
		switch(queue.getFileType()) {
		case EMPLOYEE:
			break;
		case PAY:
			setRawPayHeaders();
			break;
		case COVERAGE:
			break;
		case IRS1094C:
			break;
		case IRS1095C:
			//updateRawIrs1095cData();
			break;
		case IRSAIRERR:
			//updateRawAirTranErrorData();
			break;
		case IRSAIRRCPT:
			//updateRawAirTranReceiptData();
		default:
			break;
		}
		
	}
	
	private void setRawPayHeaders()
	{
		// temp fix for now until reference picks are in place
		for (int i = 1; i < headerColumns.size(); i++) {
			String value = getRawPayHeader(i);
			// replace the paycode
			if (value == "PAYCODE")
				mapperSPV.getGrid().setCellValue(0, i, "COMPTYPE");
			if (value == "Custom Field 1") {
				String cf1 = headerColumns.get(i).getText();
				if (cf1.contains("PAYCODE") == true) {
					int pos = cf1.indexOf("\r");
					if (pos > 0) {
						String fv = cf1.substring(pos+1);
						mapperSPV.getGrid().setCellValue(0, i, fv);
					}
				}
			}
		}
	}
	
	private String getRawPayHeader(int column ) {
		//convert the column back into the correct field
		switch (  getTableFieldPos(column)) {
			case 24: // PPD START DATE
				return "Ppd Start Date";
			case 25: // PPD END DATE
				return "Ppd End Date";
			case 26: // PAY DATE
				return "Pay Date";
			case 27: // PPD ID
				return "Pay Period ID";
			case 28: // PPD FREQUENCY
				return "Ppd Frequency"; 
			case 29: // WORK DATE
				return "Work Date";
			case 30: // HOURS
				return "Hours";
			case 31: // RATE
				return "Rate";
			case 32: // AMOUNT
				return "Amount";
			case 33: // PAYCODE
				return "PAYCODE";
			case 34: // UNION TYPE
				return "Union Type";
			case 35: // OT HRS
				return "OT Hours";
			case 36: // OT 2 HRS
				return "OT2 Hours";
			case 37: // OT 3 HRS
				return"OT3 Hours";
			case 38: // PTO HRS
				return "PTO Hours";
			case 39: // SICK PAY HRS
				return "Sick Pay Hours";
			case 40: // HLDY PAY HRS
				return "Holiday Pay Hours";
			case 41: // PAY CLASS
				return "Pay Class";
			case 47: // PayCFld1
				return "Custom Field 1";
			case 48: // PayCFld2
				return "Custom Field 2";
			case 49: // PayCFld3
				return "Custom Field 3";
			case 50: // PayCFld4
				return "Custom Field 4";
			case 51: // PayCFld5
				return "Custom Field 5";
			case 52: // PayCFld6
				return "Custom Field 6";
			case 53: // PayCFld7
				return "Custom Field 7";
		}
		return "";
	}
	
	private void updateRawDependentData(MapperRowInfo info, GridChange change ) {
		try {
			// get the Dependent
			RawDependentRequest eReq = new RawDependentRequest();
			eReq.setId(info.recordId);
			RawDependent rd = AdminPersistenceManager.getInstance().get(eReq);
			if (rd != null) {
				// check the times on the dates to stop the tine zone bug
				Utils.checkDate(rd.getDateOfBirth());
				//convert the column back into the correct field
				switch ( getTableFieldPos(change.getColumn())) {
					case 25: // SECONDARY SSN
						SSN ssn = new SSN();
						ssn.setUsrln(change.getNewValue().toString());
						rd.setSsn(ssn);
						break;
					case 26: // SECONDARY IDENTIFIER
						rd.setId(Long.valueOf(change.getNewValue().toString()));
						break;
					case 27: // MEMBER NUMBER
						//rd.setMEmberNumber( Long.valueOf(change.getNewValue().toString()));
						break;
					case 28: // SECONDARY FIRST NAME
						rd.setFirstName(change.getNewValue().toString());
						break;
					case 29: // SECONDARY MIDDLE NAME
						rd.setMiddleName(change.getNewValue().toString());
						break;
					case 30: // SECONDARY LAST NAME
						rd.setLastName(change.getNewValue().toString());
						break;
					case 31: // SECONDARY STREET NUMBER
						rd.setStreetNumber(change.getNewValue().toString());
						break;
					case 32: // SECONDARY ADDRESS LINE 1
						rd.setStreet(change.getNewValue().toString());
						break;
					case 33: // SECONDARY ADDRESS LINE 2
						rd.setStreetExtension(change.getNewValue().toString());
						break;
					case 34: // SECONDARY CITY
						rd.setCity(change.getNewValue().toString());
						break;
					case 35: // SECONDARY STATE
						rd.setState(change.getNewValue().toString());
						break;
					case 36: // SECONDARY ZIP
						rd.setZip(change.getNewValue().toString());
						break;
					case 37: // SECONDARY DATE OF BIRTH
						rd.setDateOfBirth( Utils.getCalDate(change.getNewValue().toString()));
						break;
				}
				
				// now save it
				eReq.setEntity(rd);
				rd = AdminPersistenceManager.getInstance().addOrUpdate(eReq);		
			}
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e);
		}
	}
	
	private void updateRawIrs1094cData(MapperRowInfo info, GridChange change ) {
		try {
			// get the Dependent
			RawIrs1094cRequest eReq = new RawIrs1094cRequest();
			eReq.setId(info.recordId);
			RawIrs1094c rd = AdminPersistenceManager.getInstance().get(eReq);
			if (rd != null) {
				//convert the column back into the correct field
				switch ( getTableFieldPos(change.getColumn())) {
				case 1: // year
					rd.setYear(Integer.parseInt(change.getNewValue().toString()));
					break;
				case 2: // EIN
					rd.setEIN(change.getNewValue().toString());
					break;
				case 3: // Name
					rd.setCompanyName(change.getNewValue().toString());
					break;
				case 4: // AnnFTEEs
					rd.setAnnFTEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 5: // AnnEEs
					rd.setAnnEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 6: // JanFTEEs
					rd.setJanFTEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 7: // JanEEs
					rd.setJanEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 8: // FebFTEEs
					rd.setAnnFTEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 9: // FebEEs
					rd.setFebEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 10: // MarFTEEs
					rd.setMarFTEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 11: // MarEEs
					rd.setMarEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 12: // AprFTEEs
					rd.setAprFTEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 13: // AprEEs
					rd.setAprEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 14: // MayFTEEs
					rd.setMayFTEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 15: // MayEEs
					rd.setMayEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 16: // JunFTEEs
					rd.setJunFTEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 17: // JulEEs
					rd.setJulEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 18: // JulFTEEs
					rd.setJulFTEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 19: // JulEEs
					rd.setJulEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 20: // AugFTEEs
					rd.setAugFTEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 21: // AugEEs
					rd.setAugEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 22: // SepFTEEs
					rd.setSepFTEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 23: // SepEEs
					rd.setSepEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 24: // OctFTEEs
					rd.setOctFTEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 25: // OctEEs
					rd.setOctEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 26: // NovFTEEs
					rd.setNovFTEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 27: // NovEEs
					rd.setNovEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 28: // DecFTEEs
					rd.setDecFTEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 29: // DecEEs
					rd.setDecEEs(Long.parseLong(change.getNewValue().toString()));
					break;
				case 30: // Custom Field 1
					rd.setCustomField1(change.getNewValue().toString());
					break;
				case 31: // Custom Field 2
					rd.setCustomField2(change.getNewValue().toString());
					break;
				case 32: // Custom Field 3
					rd.setCustomField3(change.getNewValue().toString());
					break;
				case 33: // Custom Field 4
					rd.setCustomField4(change.getNewValue().toString());
					break;
				case 34: // Custom Field 5
					rd.setCustomField5(change.getNewValue().toString());
					break;
				}
				
				// now save it
				eReq.setEntity(rd);
				rd = AdminPersistenceManager.getInstance().addOrUpdate(eReq);		
			}
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e);
		}
	}
	
	@FXML
	private void copySpreadsheet() {
		// manually format clipbaord data for use in other apps
		String data = "";
		for (int x = 0; x < mapperSPV.getGrid().getRowCount(); x++) {
			for (int y = 0; y < mapperSPV.getGrid().getColumnCount(); y++) {
				String tempData = mapperSPV.getGrid().getRows().get(x).get(y).getText();
				// parse out any CRs inside of the cell
				tempData = tempData.replaceAll("\r", " - ");
				data += tempData;
				data += "\t";
			}
			// next line
			data += "\r";
		}
		
		// now that have the data in a friendly format, copy it to the system clipboard
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(data);
		clipboard.setContent(content);
		 Utils.alertUser("Spreadsheet Copied", "The Raw Data Error Spreadsheet has been copied to the System Clipboard.");
	}
	
	public static class HBoxCell extends HBox {
         Label lblDate = new Label();
         Label lblRecordIndex = new Label();
         Label lblMessage = new Label();

         HBoxCell(String date, String recordIndex, String message, boolean headerRow) {
              super();

              if(date == null ) date = "";
              if(message == null) message = "";
              if(recordIndex == null) recordIndex = "";
              
              Utils.setHBoxLabel(lblDate, 120, false, date);
              Utils.setHBoxLabel(lblRecordIndex, 120, false, recordIndex);
              Utils.setHBoxLabel(lblMessage, 600, false, message);

           	  if(headerRow == true) {
           		  lblDate.setTextFill(Color.GREY);
           		  lblRecordIndex.setTextFill(Color.GREY);
            	  lblMessage.setTextFill(Color.GREY);
              } 
           	  
        	  this.getChildren().addAll(lblDate, lblRecordIndex, lblMessage);
         }
    }
	
	public static class MapperRowInfo {
		public RawRecordType type;
		public Long recordId;
		public int rowPosition;

		MapperRowInfo(RawRecordType type, Long recordId, int rowPosition) {
             super();
             this.type = type;
             this.recordId = recordId;
             this.rowPosition = rowPosition;
        }
   }	
	
/*	private static class TestDataGenerator {
        private static final String[] LOREM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc tempus cursus diam ac blandit. Ut ultrices lacus et mattis laoreet. Morbi vehicula tincidunt eros lobortis varius. Nam quis tortor commodo, vehicula ante vitae, sagittis enim. Vivamus mollis placerat leo non pellentesque. Nam blandit, odio quis facilisis posuere, mauris elit tincidunt ante, ut eleifend augue neque dictum diam. Curabitur sed lacus eget dolor laoreet cursus ut cursus elit. Phasellus quis interdum lorem, eget efficitur enim. Curabitur commodo, est ut scelerisque aliquet, urna velit tincidunt massa, tristique varius mi neque et velit. In condimentum quis nisi et ultricies. Nunc posuere felis a velit dictum suscipit ac non nisl. Pellentesque eleifend, purus vel consequat facilisis, sapien lacus rutrum eros, quis finibus lacus magna eget est. Nullam eros nisl, sodales et luctus at, lobortis at sem.".split(" ");

        private int curWord = 0;

        List<String> getNext(int nWords) {
            List<String> words = new ArrayList<>();

            for (int i = 0; i < nWords; i++) {
                if(curWord == Integer.MAX_VALUE) {
                    curWord = 0;
                }

                words.add(LOREM[curWord % LOREM.length]);
                curWord++;
            }

            return words;x`
        }
    } */
	
	private RefData getRefData(int row, int column)
	{
		for (RefData rf : refData) {
			if (rf.row == row && rf.column == column)
				return rf;
		}
		
		return null;			
	}
	
	public class RefData {
		private ReferenceType refType;
		private String fileValue;
		private int row;
		private int column;
		
		public ReferenceType getRefType() { return refType; }
		public String getFileValue() { return fileValue; }
		public int getRow() { return row; }
		public int getColumn() { return column; }
	
		RefData (ReferenceType refType, String fileValue, int row, int column){
			this.refType = refType;
			this.fileValue = fileValue;
			this.row = row;
			this.column = column;
		}
		
	}
}
