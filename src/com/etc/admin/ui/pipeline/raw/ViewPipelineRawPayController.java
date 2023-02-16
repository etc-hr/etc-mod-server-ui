package com.etc.admin.ui.pipeline.raw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.GridChange;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.ui.pipeline.queue.PipelineQueue;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileRecordRejection;
import com.etc.corvetto.ems.pipeline.entities.PipelineParseDateFormat;
import com.etc.corvetto.ems.pipeline.entities.PipelineParsePattern;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;
import com.etc.corvetto.ems.pipeline.entities.RawConversionFailure;
import com.etc.corvetto.ems.pipeline.entities.RawInvalidation;
import com.etc.corvetto.ems.pipeline.entities.RawNotice;
import com.etc.corvetto.ems.pipeline.entities.RawPay;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.PayFile;
import com.etc.corvetto.entities.Employer;
import com.etc.embeds.SSN;
import com.etc.utils.types.RequestStatusType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ViewPipelineRawPayController {
	@FXML
	private SpreadsheetView rwfldFieldsView;
	@FXML
	private ChoiceBox<String> rwfldRawTypeChoice;
	@FXML
	private VBox rwfldVBox;
	@FXML
	private CheckBox rwfldComplexViewCheck;
	@FXML
	private Label rwfldFileName;
	@FXML
	private Label rwfldDetails;
	@FXML
	private Label rwfldFileStatus;
	@FXML
	private Button rwfldLoadAllButton;
	@FXML
	private Button rwfldUndoButton;
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
	// MAPPER DISPLAY
	@FXML
	private VBox rwfldMapperVBox;
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
	// MAPPER INFO
	@FXML
	private CheckBox rwfldMapEEtoERCheck;
	@FXML
	private CheckBox rwfldCreateEmployeeCheck;
	@FXML
	private CheckBox rwfldCreateSecondaryCheck;
	@FXML
	private CheckBox rwfldCreatePlanCheck;
	
	int[] minColWidth = new int[100];
	boolean getErrorFields = true;
	int currentRawDataType = 0;
	int currentSubFile = 0;
	PipelineQueue queue = null;
	int currentSPV = 0;
	
	// mapper display support
	SpreadsheetView mapperSPV = null;
	String[] columnFields = new String[100];
	int[] columnCount = new int[100];
	static int[] columnColors = new int[100];
	static int[] columnRef = new int[100];
	boolean[] columnOn = new boolean[100];
	int mapperColIndex = 0;
	int mapperMaxEntries = 0;
	int mapperLastPosition = 0;
	int mapperColorIndex = 0;
	
    private GridBase grid = null;
	
	//List<RowDetail> rows = new ArrayList<RowDetail>();
	List<ColumnDetail> columns = new ArrayList<ColumnDetail>();
	
	Boolean complexViewEnabled = false;
	
	//dependent selections
	//RawPayEmployee selectedRawPayEmp = null;
	int currentSPVRowSelection = 0;
	
	Stack<Integer> undoOrder = new Stack<Integer>();
	Stack<GridChange> spvUndoStack = new Stack<GridChange>();
	Stack<Integer> toggleRowStack = new Stack<Integer>();

	List<ColumnDetail> columnDetails = new ArrayList<ColumnDetail>();
	String currentRawErrors = "";
	boolean currentRowIgnored = false;
	boolean currentRawInvalidated = false;
	boolean undoActive = false;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		updateMapperDisplay();
		rwfldFileName.setText("Request " +  queue.getRequest().getId().toString() + ": " + queue.getRequest().getDescription());
		getSubFiles();
		getAllData(true);
	}
	
	private void initControls() {
		// reset any error items
		rwfldApproveConversion.setVisible(false);

		// default to the first sub file
		currentSubFile = 0;
		// disable the undo button until something is done
		rwfldUndoButton.setDisable(true);
		
		// clear the records rejection info
		rwfldRecordRejectionsList.getItems().clear();
		rwfldRecordRejectionsHeaderLabel.setText("Pipeline File Record Rejections (total: 0)");
		
		rwfldFileNameField.setText("");
		
		// create a local queue entry for convenience
		queue = DataManager.i().mPipelineQueueEntry;
		if (queue == null) return;
		
		// set the load all button text
		Long size = queue.getRecords();
		if (size < queue.getRecords()) size = queue.getRecords();
		String speed = "fast";
		if (size > 25) speed = "nominal";
		if (size > 100) speed = "slow";
		if (size > 500) speed = "very slow";
		if (size > 1000) speed = "extremely slow";
		
		rwfldLoadAllButton.setText("Load all records (max " + String.valueOf(size) + ", " + speed + ")");
		
		// add some details
		if (queue.getRequest().getStatus() == RequestStatusType.COMPLETED) {
			rwfldDetails.setText("COMPLETED - Read Only");
			// pull all the fields for completed files
			getErrorFields = false;
			rwfldLoadAllButton.setText("Completed File");
			rwfldLoadAllButton.setDisable(true);
		}
		else {
			rwfldDetails.setText("Edit Mode");
			rwfldLoadAllButton.setDisable(false);
			getErrorFields = true;
		}
		
		rwfldFileStatus.setText("File Status: " + queue.getRequest().getResult());
			
    	
    	// clear the mapper display. This file may not have one
    	clearMapperDisplay();
    	
    	//set the mapper checkboxes as read only
    	rwfldMapEEtoERCheck.setDisable(true);
    	rwfldCreateEmployeeCheck.setDisable(true);
    	rwfldCreateSecondaryCheck.setDisable(true);
    	rwfldCreatePlanCheck.setDisable(true);
    	//don;t fade them out when disabled
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
	
/*
	private void setupSPV(Grid grid, int height, String spvDescription) {
		// add the description
		spvLabel.setText(spvDescription);
		rwfldVBox.getChildren().remove(spvLabel);
	    rwfldVBox.getChildren().add(spvLabel);
		
	    if (spv != null)
	    	rwfldVBox.getChildren().remove(spv);
		spv = new SpreadsheetView(grid);
	    rwfldVBox.getChildren().add(spv);
	    spv.setPrefHeight(height);
	    
	    // completed have limited context menus
	    if (queue.getRequest().getStatus() == RequestStatusType.COMPLETED) {
		    spv.getContextMenu().getItems().remove(2); // remove the comment option
		    spv.getContextMenu().getItems().remove(1); // remove the paste option
	    }else {
		    // add to the context menu
		    MenuItem menuIgnore = new MenuItem();
		    menuIgnore.setText("Toggle Ignore Row");
		    menuIgnore.setOnAction(new EventHandler<ActionEvent>() {
		        public void handle(ActionEvent t) {
		        	undoOrder.push(3);
	         		//enable the buttons now that we have something to undo or save
	    			rwfldUndoButton.setDisable(false);
	    			rwfldSaveChangesButton.setDisable(false);
	    			//save this to update later
	    			toggleRowStack.add(spv.getSelectionModel().getFocusedCell().getRow());
	    			// toggle the row
		        	toggleIgnoreRow(spv.getSelectionModel().getFocusedCell().getRow(), 0);
		        }
		    });	    
		    
		    MenuItem menuUndo = new MenuItem();
		    menuUndo.setText("Undo");
		    menuUndo.setOnAction(new EventHandler<ActionEvent>() {
		        public void handle(ActionEvent t) {
		        	undoSPVChange();
		        }
		    });	    	
		    
		    spv.getContextMenu().getItems().remove(2); // remove the comment option
		    spv.getContextMenu().getItems().add(menuIgnore);
		    spv.getContextMenu().getItems().add(menuUndo);
	    }

	    //add a change handler to capture changes
		 spv.getGrid().addEventHandler(GridChange.GRID_CHANGE_EVENT, new EventHandler<GridChange>() {
	         
	         public void handle(GridChange change) {
		        	 if (undoActive == true) {
		        		 undoActive = false;
		        		 return;
		        	 }
		        	 
		        	 spvUndoStack.push(change);
		        	 undoOrder.push(0);
	         		//enable the buttons now that we have something to undo or save
	    			rwfldUndoButton.setDisable(false);
	    			rwfldSaveChangesButton.setDisable(false);
	             }
	      });

		 // mouse selection handler
		 spv.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	handleSPVSelection();
            }
        });
	    
	    // control-z key handler for undos
	    spv.setOnKeyReleased(t -> {
	        if (t.isControlDown() && t.getCode() == KeyCode.Z) {
	        	undoSPVChange();
	            t.consume();
	        }
	        
	        // if we move rows with the keyboard, update our selection but don't consume it
	        if (t.getCode() == KeyCode.DOWN || 
	        	t.getCode() == KeyCode.KP_DOWN ||
	        	t.getCode() == KeyCode.UP ||
	        	t.getCode() == KeyCode.KP_UP ||
	        	t.getCode() == KeyCode.ENTER) {
	        	handleSPVSelection();
	        }
	    });   
	}
*/
	private void clearMapperDisplay() {
	    if (mapperSPV != null)
	    	rwfldMapperVBox.getChildren().remove(mapperSPV);
		rwfldMapperIdButton.setText("");;
		rwfldSpecIdButton.setText("");
		rwfldMapperSpecNameField.setText("");
	}
	
	private void getSubFiles() {
		// clear the sub combo
		rwfldRawTypeChoice.getItems().clear();
		disableSubControls(true);
		
/*  		DataManager.i().getSubPayFiles(queue.getPayFile().getId());
  		if (DataManager.i().mSubPayFiles.size() > 0) {
  			//load the sub file selection combo
  			ObservableList<String> subList = FXCollections.observableArrayList();
			for (PayFile file : DataManager.i().mSubPayFiles) {
				Employer employer = DataManager.i().getEmployer(file.getPipelineInformation().getEmployerId(), file.getPipelineInformation().getAccountId());
				subList.add(String.valueOf(file.getId() + " - " + employer.getName()));
				//subList.add(String.valueOf(file.getId() + " - " + queue.getRequest().getDescription()));
			}
			rwfldRawTypeChoice.setItems(subList);
			disableSubControls(false);
  		}
*/	}

	private void disableSubControls(boolean disable) {
		rwfldUpdateButton.setDisable(disable);
		rwfldsubLabel.setDisable(disable);
		rwfldRawTypeChoice.setDisable(disable);
	}
	
	private void getAllData(boolean loadAll) {
		/// set the complexView
/*		complexViewEnabled = rwfldComplexViewCheck.isSelected();
		rwfldSaveChangesButton.setDisable(true);
		// hide the sub controls if we are not in the complex view
		rwfldUpdateButton.setVisible(complexViewEnabled);
		rwfldRawTypeChoice.setVisible(complexViewEnabled);
		rwfldFileName.setVisible(complexViewEnabled);
		rwfldsubLabel.setVisible(complexViewEnabled);

		// reset the data collections
		// PAY
		if (DataManager.i().mRawPayEmployees == null)
			DataManager.i().mRawPayEmployees = new ArrayList<RawPayEmployee>();
		else
			DataManager.i().mRawPayEmployees.clear();
		
		if (DataManager.i().mRawPays == null)
			DataManager.i().mRawPays = new ArrayList<RawPay>();
		else
			DataManager.i().mRawPays.clear();

		// create a thread to handle the update, letting the screen respond normally
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            	// iterate through the sub files if we are not doing the complex view
				if (rwfldRawTypeChoice.getItems().size() > 0 && complexViewEnabled == false && loadAll  == true) {
					for (int i = 0; i < rwfldRawTypeChoice.getItems().size(); i++) {
						currentSubFile = i;
						getRawFieldData();
					}
				} else {
					getRawFieldData();
				}
				
                return null;
            }
        };
        
      	task.setOnScheduled(e ->  {
      		EtcAdmin.i().setStatusMessage("Loading Raw Data from Server...");
     		EtcAdmin.i().setProgress(0.25);});
      			
    	task.setOnSucceeded(e ->   updateRawFieldData());
    	task.setOnFailed(e ->   updateRawFieldData());
        new Thread(task).start();	
*/	}
	
	private void getRawFieldData() {
  		// if this is a completed file, get all fields
  		if (queue.getRequest().getStatus() == RequestStatusType.COMPLETED)
  			getErrorFields = false;

  		Long fileId = 0l;
  		
		if (queue.getPayFile() == null) {
			EtcAdmin.i().setStatusMessage("Ready - No Pay File Attached");
			EtcAdmin.i().setProgress(0);
			return;
		}
		// get the file's raw employees
  		EtcAdmin.i().setStatusMessage("Loading Raw Pay Employees from Server...");
  		EtcAdmin.i().setProgress(0.4);

  		// if we have sub files, use the sub's fileId
  		fileId = queue.getPayFile().getId();
  		if (DataManager.i().mSubPayFiles .size() > 0)
  			fileId = DataManager.i().mSubPayFiles.get(currentSubFile).getId();
  		
  		//DataManager.i().getRawPayEmployees(fileId, getErrorFields,true);
  		
		// and the rawPays
  		EtcAdmin.i().setStatusMessage("Loading Raw Pays from Server...");
  		EtcAdmin.i().setProgress(0.6);
		// since there are no errors assigned to the rawpays, we pull them all
  		//DataManager.i().getRawPays(fileId, getErrorFields, true);
		// reset to only error fields
		getErrorFields = true;
        return;
	}

	private void updateRawFieldData(){	
		 if (queue == null) return;
		 
		// update the selection
		if (rwfldRawTypeChoice.getItems().size() > 0 && rwfldRawTypeChoice.getItems().size() > currentSubFile)
			rwfldRawTypeChoice.getSelectionModel().select(currentSubFile);

		String fileName = "";
		fileName = queue.getPayFile().getId().toString() + " Pay";
		updateRawPayEmployeeData(queue.getPayFile());
		
		rwfldFileNameField.setText(fileName);
		loadFileRecordRejections();
	}
	
	private void setCellStyle(SpreadsheetCell cell, int columnId, int row, String type, boolean editable) {		
		
		// ignored
		if (currentRowIgnored == true) {
			if (row%2==0)
				cell.getStyleClass().add("RawCellEvenIgnored");
			else 
				cell.getStyleClass().add("RawCellOddIgnored");
			cell.setEditable(false);
			return;
		}
		
		// invalidation
		if (cell.getColumn() == 0) {
			cell.getStyleClass().add("Invalidation");
			return;
		}
		
		if (columnId > 0 && columnOn[columnId] == true) {
			cell.getStyleClass().add("BackColor" + columnColors[columnRef[columnId]]);
			return;
		}
	}
	
	// String cell
	private SpreadsheetCell getCell(int color, int row, int column, String data, boolean editable) {
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, data);
		//setCellStyle(newCell, color, row, "String", editable);
		((SpreadsheetCellBase) newCell).setTooltip(currentRawErrors);
		return newCell;
	}
	
	// Long cell
	private SpreadsheetCell getCell(int color, int row, int column, Long data, boolean editable) {
		if (data == null) data = 0l;
		SpreadsheetCell newCell = SpreadsheetCellType.INTEGER.createCell(row, column, 1, 1, data.intValue());
		setCellStyle(newCell, color, row, "Long", editable);
		return newCell;
	}
	
	// float cell
	private SpreadsheetCell getCell(int color, int row, int column, float data, boolean editable) {
		double dData = Math.round(data * 100.0)/100.0;
		SpreadsheetCell newCell = SpreadsheetCellType.DOUBLE.createCell(row, column, 1, 1, dData);
		setCellStyle(newCell, color, row, "Float", editable);
		return newCell;
	}
	
	// boolean cell
	private SpreadsheetCell getCell(int color, int row, int column, Boolean data, boolean editable) {
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1,data.toString());
		//SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, "");
		setCellStyle(newCell, color, row, "Bool", editable);
		return newCell;
	}
	
	// Calendar cell
	private SpreadsheetCell getCell(int color, int row, int column, Calendar data, boolean editable) {
		SpreadsheetCell newCell = null;
		if (data == null)
			newCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1,"");
		else
			newCell = SpreadsheetCellType.DATE.createCell(row, column, 1, 1,Utils.getLocalDate(data.getTime()));
		setCellStyle(newCell, color, row, "Date", editable);
		return newCell;
	}
	
	// SSN cell
	private SpreadsheetCell getCell(int color, int row, int column, SSN data, boolean editable) {
		SpreadsheetCell newCell = null;
		if (data == null || data.getUsrln() == null)
			newCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1,"");
		else
			newCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1,data.getUsrln());
		setCellStyle(newCell, color, row, "SSN", editable);
		return newCell;
	}

/*	private void handleSPVSelection() {
    	// get the selection
    	currentSPVRowSelection = spv.getSelectionModel().getFocusedCell().getRow();
    	if (currentSPVRowSelection < 0) return;
    	
    	selectedRawPayEmp = DataManager.i().mRawPayEmployees.get(currentSPVRowSelection);
    	updateRawPayData();
	}
*/	
	
/*	private void undoSPVChange() {
		if (undoOrder.size() < 1) return;
		
		// set our flag to not save the undo as another change
		undoActive = true;
		
		// get the next change to undo
		GridChange lastChange = null;
		switch(undoOrder.pop()) {
		case 0:
			lastChange = spvUndoStack.pop();
			spv.getGrid().setCellValue(lastChange.getRow(), lastChange.getColumn(), lastChange.getOldValue());
			break;
		case 1:
			lastChange = spv1UndoStack.pop();
			spv1.getGrid().setCellValue(lastChange.getRow(), lastChange.getColumn(), lastChange.getOldValue());
			break;
		case 2:
			lastChange = spv2UndoStack.pop();
			spv2.getGrid().setCellValue(lastChange.getRow(), lastChange.getColumn(), lastChange.getOldValue());
			break;
		case 3:
			int row = toggleRowStack.pop();
			toggleIgnoreRow(row,0);
			break;
		}
		// if we have nothing to undo or save, disable our buttons
		if (undoOrder.size() < 1) {
			rwfldUndoButton.setDisable(true);
			rwfldSaveChangesButton.setDisable(true);
		}
	}
	*/
	/*
	private void toggleIgnoreRow(int row, int source){
		if (queue == null) return;
		RecordInformation info = null;
		//mark the record at the specified row as ignored
		if (source == 0)
			info = DataManager.i().mRawPayEmployees.get(row).getRecordInformation() ;

		if (info != null) {
			if (info.isIgnored() == false) {
				info.setIgnored(true);
				info.setIgnoredOn(Calendar.getInstance());
			}else {
				info.setIgnored(false);
				info.setIgnoredOn(null);
			}
		}

		//and reload the spreadsheet
		updateRawFieldData();
	}
	*/
	/*
	private void adjustBlankColumns(SpreadsheetView ss) {
		
		ObservableList<ObservableList<SpreadsheetCell>> cells = ss.getGrid().getRows();
		// look for empty columns
		for (int cols = 0; cols < ss.getGrid().getColumnCount(); cols++) {
			boolean empty = true;
			for (int rows = 0; rows < ss.getGrid().getRowCount(); rows++) {
				if (cells.get(rows).get(cols).getText().length() > 0) {
					empty = false;
					break;
				}
			}
			// and set the column width accordingly
			if (empty == true) {
				ss.getColumns().get(cols).setPrefWidth(minColWidth[cols]);
			}
		}
		
		//finally adjust the row header size for large numbers
	    ss.setRowHeaderWidth(60);
	}
	*/
	private String getErrors(List<RawInvalidation> invalidations, List<RawNotice> notices) {
		String errors = "";
		String invalidationString = "";
		String noticeString = "";
		
		currentRawInvalidated = false;
		if (invalidations != null) {
			for (RawInvalidation inv : invalidations) {
				currentRawInvalidated = true;
				if (invalidationString.length() < 16)
					invalidationString = "Invalidations: " + inv.getMessage();
				else
					invalidationString = invalidationString + ", " + inv.getMessage();
			}
		}

		if (invalidationString.length() > 0)
			errors += invalidationString;
		if (noticeString.length() > 0)
			errors += (" " + noticeString);
		
		return errors;
	}
	
	private static String getInvalidations(List<RawInvalidation> invalidations, List<RawConversionFailure> failures) {
		String invalidationString = "";
		
		if (invalidations != null) {
			for (RawInvalidation inv : invalidations) {
				if (invalidationString.length() < 1)
					invalidationString = inv.getMessage();
				else
					invalidationString = invalidationString + "\n" + inv.getMessage();
			}
		}
		
		if (failures != null) {
			if (invalidationString.length() > 0 && failures.size() > 0) 
				invalidationString = invalidationString + "\n";
			for (RawConversionFailure fail: failures) {
				if (invalidationString.length() < 1)
					invalidationString = "Conv. Failure: " + fail.getMessage();
				else
					invalidationString = invalidationString + "\n" + "Conv. Failure: " + fail.getMessage();
			}
		}

		return invalidationString;
	}
	
	private static String getNotices(List<RawNotice> notices) {
		String noticeString = "";

		if (notices!= null) {
			for (RawNotice notice : notices) {
				if (noticeString.length() < 1)
					noticeString = notice.getMessage();
				else
					noticeString = noticeString + "\n" + notice.getMessage();
			}
		}

		return noticeString;
	}
	
	private void resetColContent()
	{
		for (ColumnDetail col : columns)
			col.content = "";
	}
	
	private void setContent (int index, String content) {
		for (ColumnDetail col : columns) {
			for (Integer mapperPosition : col.mapperPositions)
			if (mapperPosition == index)
				if (col.active == true) {
					if (col.content != null && col.content.isEmpty() == false)
						col.content = col.content + " " + content;
					else
						col.content = content;
				}
		}
	}
	
	private void updateRawPayEmployeeData(PayFile payFile){	
/*		//completeView.getItems().clear();
		currentSPV = 0;
		if (payFile == null) {
			EtcAdmin.i().setStatusMessage("Ready - No Pay File Attached");
			EtcAdmin.i().setProgress(0);
			return;
		}
		
		EtcAdmin.i().setStatusMessage("Loading Raw Pay Employee Fields...");
		EtcAdmin.i().setProgress(.75);
		
		List<RawPayEmployee> rawPayEmps = DataManager.i().mRawPayEmployees;
		if (rawPayEmps == null) {
			if (rwfldRawTypeChoice.getItems().size() > 1)
				EtcAdmin.i().setStatusMessage("Ready - No Raw Records. Please check the other SubFiles");
			else
				EtcAdmin.i().setStatusMessage("Ready - No Raw Pay Employee Records");
			EtcAdmin.i().setProgress(0);
			return;
		}
		
		int row = 0;

		for (RawPayEmployee payEmp :rawPayEmps) {
			if (payEmp == null) continue;

			// get the raw pay for this Raw Pay Emp
			RawPay rawPay = getRawPay(payEmp.getId());
			ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();	
			currentRawErrors = getErrors(payEmp.getInvalidations(), payEmp.getNotices());
			
			// set the content
			resetColContent();
			setContent(0, "");
			setContent(1, payEmp.getEmployerReference());
			setContent(2,"");
			if (payEmp.getSsn() != null)
				setContent(3,payEmp.getSsn().getUsrln());
			else
				setContent(3,"");
			setContent(4,payEmp.getEmployerIdentifier());
			setContent(5,payEmp.getFirstName());
			setContent(6,payEmp.getMiddleName());
			setContent(7,payEmp.getLastName());
			setContent(8,"");
			setContent(9,"");
			setContent(10,"");
			setContent(11,"");
			setContent(12,"");
			setContent(13,"");
			setContent(14,"");
			setContent(15,"");
			setContent(16,payEmp.getStreetNumber());
			setContent(17,payEmp.getStreet());
			setContent(18,payEmp.getStreetExtension());
			setContent(19,payEmp.getCity());
			setContent(20,payEmp.getState());
			setContent(21,payEmp.getZip());
			setContent(22,Utils.getDateString(payEmp.getDateOfBirth()));
			setContent(23,Utils.getDateString(payEmp.getHireDate()));
			setContent(24,Utils.getDateString(payEmp.getTermDate()));
			setContent(25,payEmp.getCvgClassReference());
			setContent(26,"");
			setContent(27,"");
			if (rawPay != null)
				setContent(28,Utils.getDateString(rawPay.getPayPeriodStart()));
			else
				setContent(28,"");
			if (rawPay != null)
				setContent(29,rawPay.getPayPeriodEnd().toString());
			else
				setContent(29,"");
			if (rawPay != null)
				setContent(30,Utils.getDateString(rawPay.getPayDate()));
			else
				setContent(30,"");
			if (rawPay != null)
				setContent(30,Utils.getDateString(rawPay.getWorkDate()));
			else
				setContent(31,"");
			setContent(32,"");
			if (rawPay != null)
				setContent(33,String.valueOf(rawPay.getHours()));
			else
				setContent(33,"");
			if (rawPay != null)
				setContent(34,String.valueOf(rawPay.getRate()));
			else
				setContent(34,"");
			if (rawPay != null)
				setContent(35,String.valueOf(rawPay.getAmount()));
			else
				setContent(35,"");
			if (rawPay != null)
				setContent(36,rawPay.getPayCode());
			else
				setContent(36,"");
			setContent(37,"");
			if (rawPay != null)
				setContent(37,String.valueOf(rawPay.getOvertimeHours1()));
			else
				setContent(38,"");
			if (rawPay != null)
				setContent(38,String.valueOf(rawPay.getOvertimeHours2()));
			else
				setContent(39,"");
			if (rawPay != null)
				setContent(39,String.valueOf(rawPay.getOvertimeHours3()));
			else
				setContent(40,"");
			if (rawPay != null)
				setContent(40,String.valueOf(rawPay.getPtoHours()));
			else
				setContent(41,"");
			if (rawPay != null)
				setContent(41,String.valueOf(rawPay.getSickHours()));
			else
				setContent(42,"");
			if (rawPay != null)
				setContent(42,String.valueOf(rawPay.getHolidayHours()));
			else
				setContent(43,"");
			setContent(44,payEmp.getCustomField1());
			setContent(45,payEmp.getCustomField2());
			setContent(46,payEmp.getCustomField3());
			setContent(47,payEmp.getCustomField4());
			setContent(48,payEmp.getCustomField5());
			
			// create the columns
			int pos = 0;
			for (ColumnDetail col : columns) {
				if (col.active == true)
					list.add(getCell(pos,row,col.columnPosition, col.content, true));
			}
			
			// add the error if present
			list.add(0, getCell(0, row,0,currentRawErrors, true));
			// make the columns a row
			grid.getRows().add(list);
			row++;
		}

	    if (mapperSPV != null)
	    	rwfldMapperVBox.getChildren().remove(mapperSPV);
	    mapperSPV = new SpreadsheetView(grid);
	    
	    rwfldMapperVBox.getChildren().add(mapperSPV);
	    mapperSPV.setPrefWidth(Control.USE_COMPUTED_SIZE);
	    mapperSPV.setMaxWidth(Control.USE_COMPUTED_SIZE);

	    
	    // create the tagged (A1, B2, etc.) headers
	    int pos = 1;
	    int index = 0;
	    addHeader(0, 0);
	    for (ColumnDetail col : columns) {
	    	if (col.active == true) {
	    		addHeader(pos, index);
	    		pos++;
	    	}
	    	index++;
	    }
	    
*/		EtcAdmin.i().setStatusMessage("Ready");
		EtcAdmin.i().setProgress(0);		
	}
	
	private RawPay getRawPay(Long Id) {
		if (DataManager.i().mRawPays == null) return null;
		
		for (RawPay rawPay: DataManager.i().mRawPays) {
			if (rawPay.getRawEmployeeId().longValue() == Id)
				return rawPay;
		}
		
		return null;
	}
	
	private void loadFileRecordRejections() {
		// clear anything already in the list
		rwfldRecordRejectionsList.getItems().clear();
		rwfldRecordRejectionsHeaderLabel.setText("Pipeline File Record Rejections (total: 0)");
		
		//String sAddress;
		List<PipelineFileRecordRejection> rejections = null;
		List<HBoxCell> list = new ArrayList<>();
		String recordIndex;

		if (queue.getPayFile().getRecordRejections() == null) return;
		if (queue.getPayFile().getRecordRejections().size() < 1) return;
		rejections = queue.getPayFile().getRecordRejections();
		if (rejections == null) return;
		
		list.add(new HBoxCell("Date", "Record Index", "Message", true));			
		for (PipelineFileRecordRejection rejection : rejections) {
			if (rejection.getRecordInformation() == null)
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
        if (rejectionCount < 0) rejectionCount = 0;
        rwfldRecordRejectionsHeaderLabel.setText("Pipeline File Record Rejections (total: " + String.valueOf(rejectionCount) + ")" );
	}
	
	private void updateMapperDisplay() {
		// clear our current layout
		if (grid != null) 
			grid.getRows().clear();
		//add in the placeholder columns
		columns.clear();
		for (int i = 0; i < 100; i++)
			columns.add(new ColumnDetail("",-1,""));
		
		Arrays.fill(columnFields,"");
		Arrays.fill(columnCount, 0);
		Arrays.fill(columnColors, 0);
		Arrays.fill(columnRef, 0);
		Arrays.fill(columnOn, false);
	
		//reset our counters
		mapperColIndex = 0;
		mapperMaxEntries = 0;
		mapperLastPosition = 0;
		mapperColorIndex = 0;
		
		PipelineSpecification spec = null;
		
		//load the filespec
		if ((spec = queue.getRequest().getSpecification()) == null) return;
		rwfldApproveConversion.setVisible(spec.isPostValidationApproval());
		if (queue.getPayFile() == null) return;
		if ((spec = queue.getPayFile().getSpecification()) == null) return;
		DataManager.i().loadDynamicPayFileSpecification(spec.getDynamicFileSpecificationId());
		loadFileSpec(DataManager.i().mDynamicPayFileSpecification);
		
		Utils.updateControl(rwfldCreateEmployeeCheck,DataManager.i().mDynamicPayFileSpecification.isCreateEmployee());
		rwfldMapEEtoERCheck.setVisible(true);
		Utils.updateControl(rwfldCreateSecondaryCheck,DataManager.i().mDynamicPayFileSpecification.isMapEEtoER());
		DataManager.i().mPipelineSpecification = spec;
		DataManager.i().mPipelineChannel = spec.getChannel();
		rwfldCreateSecondaryCheck.setVisible(true);
		rwfldCreateSecondaryCheck.setText("Create PayPeriod");
		rwfldCreatePlanCheck.setVisible(false);
		// reset the button if the file is marked for validation approval
		if (queue.getPayFile().getPipelineInformation() != null && queue.getPayFile().getPipelineInformation().isPostValidationApproved() == true)
			rwfldApproveConversion.setVisible(false);
		
		rwfldMapperIdButton.setText(spec.getDynamicFileSpecificationId().toString() + " (click to view)");;
		rwfldSpecIdButton.setText(spec.getId().toString() + " (click to view)");
		rwfldMapperSpecNameField.setText(spec.getName());

		// create a name row
		ObservableList<ObservableList<SpreadsheetCell>> lines = FXCollections.observableArrayList();
		ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();	

		// now go through the column data, creating spreadsheet cells for our row
		int pos = 0;
	    for (ColumnDetail col : columns) {
	    	if (col.active == true) {
				list.add(getMapperCell(columnColors[pos],1,pos,col.header));
				//col.columnPosition = pos;
	    		pos++;
	    	}
	    }

	    list.add(0,getMapperCell(columnColors[0],1,0,"Error"));
		//add the name row (a collection of 1)
		lines.add(list);
		
	    grid = new GridBase(1, list.size());
		grid.setRows(lines);
	}
	
	
	private void addHeader(int position, int value) {
		// check for error column
		if (position == 0) {
			mapperSPV.getGrid().getColumnHeaders().add(position, "");
			return;
		}
		
		String alphaVal = "";
		char c = 'A';
		char c1 = 'A';
		
		if (value < 26) {
			c += value;
			alphaVal = Character.toString(c);
		} else {
			c += value % 26;
			c1 += value/26;
			alphaVal = Character.toString(c1) + Character.toString(c);
		}
		
		alphaVal += String.valueOf(value);
		mapperSPV.getGrid().getColumnHeaders().add(position, alphaVal);
	}
	
	private void CheckColumn(int colId, String header, boolean colSelected, int colIndex, PipelineParsePattern parsePattern, PipelineParseDateFormat dateFormat) {
		if (colSelected == true && colIndex < 100 && header.isEmpty() == false) {
			if (columns.get(colIndex).header.isEmpty() == false)
				columns.get(colIndex).header = columns.get(colIndex).header + "\r" + header;
			else
				columns.get(colIndex).header = header;
			columns.get(colIndex).active = true;
			columns.get(colIndex).mapperPositions.add(colId);
			
			mapperColIndex = colIndex;
		}
	}
	
	private SpreadsheetCell getMapperCell(int colIndex, int row, int column, String data) {
		if (data == null) data = "";
		if (data.length() < 1) data = "          ";
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, data);
		newCell.setEditable(false);
		if (data.trim().length() > 0 )
			newCell.getStyleClass().add("BackColor" + columnColors[column]);
		
		return newCell;
	}

	private void loadFileSpec(DynamicPayFileSpecification fSpec) {
		if (fSpec == null) return;
		CheckColumn(1,"EMPLOYER IDENTIFIER",fSpec.isErCol(), fSpec.getErColIndex(), null, null);
		CheckColumn(3,"EMPLOYEE SSN", fSpec.isSsnCol(), fSpec.getSsnColIndex(), fSpec.getSsnParsePattern(), null);
		CheckColumn(4,"EMPLOYEE IDENTIFIER", fSpec.isErRefCol(), fSpec.getErRefColIndex(), fSpec.getErRefParsePattern(), null);
		CheckColumn(5,"FIRST NAME", fSpec.isfNameCol(), fSpec.getfNameColIndex(), fSpec.getfNameParsePattern(), null);
		CheckColumn(6,"MIDDLE NAME", fSpec.ismNameCol(), fSpec.getmNameColIndex(), fSpec.getmNameParsePattern(), null);
		CheckColumn(7,"LAST NAME", fSpec.islNameCol(), fSpec.getlNameColIndex(), fSpec.getlNameParsePattern(), null);
		CheckColumn(8,"DEPARTMENT", fSpec.isDeptCol(), fSpec.getDeptColIndex(), null, null);
		CheckColumn(9,"LOCATION", fSpec.isLocCol(), fSpec.getLocColIndex(), null, null);
		CheckColumn(10,"PAYCODE", fSpec.isPayCodeCol(), fSpec.getPayCodeColIndex(), null, null);
		CheckColumn(11,"CFLD1: " + fSpec.getCfld1Name(),fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), fSpec.getCfld1ParsePattern(), null);
		CheckColumn(12,"CFLD2: " + fSpec.getCfld2Name(),fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), fSpec.getCfld2ParsePattern(), null);
		CheckColumn(13,"CFLD3: " + fSpec.getCfld3Name(),fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), null, null);
		CheckColumn(14,"CFLD4: " + fSpec.getCfld4Name(),fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), null, null);
		CheckColumn(15,"CFLD5: " + fSpec.getCfld5Name(),fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), null, null);
		CheckColumn(16,"STREET NUMBER", fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), fSpec.getStreetParsePattern(), null);
		CheckColumn(17,"ADDRESS LINE 1", fSpec.isStreetCol(), fSpec.getStreetColIndex(), fSpec.getStreetParsePattern(), null);
		CheckColumn(18,"ADDRESS LINE 2", fSpec.isLin2Col(), fSpec.getLin2ColIndex(), null, null);
		CheckColumn(19,"CITY", fSpec.isCityCol(), fSpec.getCityColIndex(), fSpec.getCityParsePattern(), null);
		CheckColumn(20,"STATE", fSpec.isStateCol(), fSpec.getStateColIndex(), fSpec.getStateParsePattern(), null);
		CheckColumn(21,"ZIP", fSpec.isZipCol(), fSpec.getZipColIndex(), fSpec.getZipParsePattern(), null);
		CheckColumn(22,"DATE OF BIRTH", fSpec.isDobCol(), fSpec.getDobColIndex(), null, fSpec.getDobFormat());
		CheckColumn(23,"HIRE DATE", fSpec.isHireDtCol(), fSpec.getHireDtColIndex(), null, fSpec.getHireDtFormat());
		CheckColumn(24,"REHIRE DATE", fSpec.isRhireDtCol(), fSpec.getRhireDtColIndex(), null, fSpec.getRhireDtFormat());
		CheckColumn(25,"TERMINATION DATE", fSpec.isTermDtCol(), fSpec.getTermDtColIndex(), null, fSpec.getTermDtFormat());
		CheckColumn(26,"DEPARTMENT", fSpec.isDeptCol(), fSpec.getDeptColIndex(), null, null);
		CheckColumn(27,"LOCATION", fSpec.isLocCol(), fSpec.getLocColIndex(), null, null);
		CheckColumn(28,"PPD START DATE", fSpec.isPpdStartDtCol(), fSpec.getPpdStartDtColIndex(), fSpec.getPpdStartDtParsePattern(), fSpec.getPpdStartDtFormat());
		CheckColumn(29,"PPD END DATE", fSpec.isPpdEndDtCol(), fSpec.getPpdEndDtColIndex(), fSpec.getPpdEndDtParsePattern(), fSpec.getPpdEndDtFormat());
		CheckColumn(30,"PAY DATE", fSpec.isPayDtCol(), fSpec.getPayDtColIndex(), fSpec.getPayDtParsePattern(), fSpec.getPayDtFormat());
		CheckColumn(31,"PPD ID", fSpec.isPpdErRefCol(), fSpec.getPpdErRefColIndex(), null, null);
		CheckColumn(32,"PPD FREQUENCY", fSpec.isPpdFreqCol(), fSpec.getPpdFreqColIndex(), null, null);
		CheckColumn(33,"HOURS", fSpec.isHrsCol(), fSpec.getHrsColIndex(), null, null);
		CheckColumn(34,"RATE", fSpec.isRateCol(), fSpec.getRateColIndex(), null, null);
		CheckColumn(35,"AMOUNT", fSpec.isAmtCol(), fSpec.getAmtColIndex(), null, null);
		CheckColumn(36,"PAY CODE", fSpec.isPayCodeCol(), fSpec.getPayCodeColIndex(), null, null);
		CheckColumn(37,"UNION TYPE", fSpec.isUnionTypeCol(), fSpec.getUnionTypeColIndex(), null, null);
		CheckColumn(38,"OT HRS",fSpec.isOtHrsCol(), fSpec.getOtHrsColIndex(), null, null);
		CheckColumn(39,"OT 2 HRS",fSpec.isOtHrs2Col(), fSpec.getOtHrs2ColIndex(), null, null);
		CheckColumn(40,"OT 3 HRS",fSpec.isOtHrs3Col(), fSpec.getOtHrs3ColIndex(), null, null);
		CheckColumn(41,"PTO HRS",fSpec.isPtoHrsCol(), fSpec.getPtoHrsColIndex(), null, null);
		CheckColumn(42,"SICK PAY HRS",fSpec.isSickHrsCol(), fSpec.getSickHrsColIndex(), null, null);
		CheckColumn(43,"HLDY PAY HRS",fSpec.isHolidayHrsCol(), fSpec.getHolidayHrsColIndex(), null, null);
		CheckColumn(44,"CFld1: " + fSpec.getCfld1Name(),fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), fSpec.getCfld1ParsePattern(), null);
		CheckColumn(45,"CFld2: " + fSpec.getCfld2Name(),fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), fSpec.getCfld2ParsePattern(), null);
		CheckColumn(46,"CFld3: " + fSpec.getCfld3Name(),fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), null, null);
		CheckColumn(47,"CFld4: " + fSpec.getCfld4Name(),fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), null, null);
		CheckColumn(48,"CFld5: " + fSpec.getCfld5Name(),fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), null, null);
		CheckColumn(49,"PayCFld1: " + fSpec.getPayCfld1Name(),fSpec.isPayCfld1Col(), fSpec.getPayCfld1ColIndex(), fSpec.getPayCfld1ParsePattern(), null);
		CheckColumn(50,"PayCFld2: " + fSpec.getPayCfld2Name(),fSpec.isPayCfld2Col(), fSpec.getPayCfld2ColIndex(), fSpec.getPayCfld2ParsePattern(), null);
		CheckColumn(51,"PayCFld3: " + fSpec.getPayCfld3Name(),fSpec.isPayCfld3Col(), fSpec.getPayCfld3ColIndex(), null, null);
		CheckColumn(52,"PayCFld4: " + fSpec.getPayCfld4Name(),fSpec.isPayCfld4Col(), fSpec.getPayCfld4ColIndex(), null, null);
		CheckColumn(53,"PayCFld5: " + fSpec.getPayCfld5Name(),fSpec.isPayCfld5Col(), fSpec.getPayCfld5ColIndex(), null, null);
		CheckColumn(54,"PayCFld6: " + fSpec.getPayCfld6Name(),fSpec.isPayCfld6Col(), fSpec.getPayCfld6ColIndex(), null, null);
		CheckColumn(55,"PayCFld7: " + fSpec.getPayCfld7Name(),fSpec.isPayCfld7Col(), fSpec.getPayCfld7ColIndex(), null, null);
	}
	
	@FXML
	private void onRawDataType(ActionEvent event) {
		currentRawDataType = rwfldRawTypeChoice.getSelectionModel().getSelectedIndex();
		updateRawFieldData();
	}	

	@FXML
	private void onLoadAllFields(ActionEvent event) {
		// reset the errorfields flag
		getErrorFields = false;
		
		// clean up the undo stack and changes
		rwfldUndoButton.setDisable(true);
		rwfldSaveChangesButton.setDisable(true);
		spvUndoStack.clear();
		undoOrder.clear();
		
		// and reload the data
    	getAllData(true);
	}	

	@FXML
	private void onSpecId(ActionEvent event) {
		DataManager.i().setScreenType(ScreenType.PIPELINESPECIFICATIONFROMRAW);
		EtcAdmin.i().setScreen(ScreenType.PIPELINESPECIFICATIONFROMRAW, true);
	}	
	
	@FXML
	private void onMapperId(ActionEvent event) {
		DataManager.i().setScreenType(ScreenType.MAPPEREMPLOYEEFROMRAW);
		EtcAdmin.i().setScreen(ScreenType.MAPPEREMPLOYEEFROMRAW, true);
	}	
	
	@FXML
	private void onUndo(ActionEvent event) {
		//undoSPVChange();
	}	
	
	@FXML
	private void onComplexView(ActionEvent event) {
		getAllData(false);
	}	
	
	@FXML
	private void onUpdate(ActionEvent event) {
		currentSubFile = rwfldRawTypeChoice.getSelectionModel().getSelectedIndex();
		getAllData(false);
	}	
	
	@FXML
	private void onApproveConversion(ActionEvent event) {
		// update the file spec accordingly
/*		queue.getPayFile().getPipelineInformation().setPostValidationApproved(false);
		DataManager.i().mUpdatePayFileRequest = new UpdatePayFileRequest(queue.getPayFile());
		DataManager.i().saveCurrentPayFileRequest();
		
		rwfldApproveConversion.setVisible(false);
*/	}	
	
	@FXML
	private void onReprocess(ActionEvent event) {
		// create the request
/*		AddPipelineRequestRequest rq = new AddPipelineRequestRequest();
		//copy over the required data
		rq.setDescription(queue.getRequest().getDescription());
		rq.setPipelineFileId(queue.getRequest().getPipelineFileId());
		if (queue.getRequest().getSpecification() != null)
			rq.setSpecificationId(queue.getRequest().getSpecification().getId());
		rq.setBatchId(queue.getRequest().getBatchId());
		if (queue.getRequest().getInstance() != null)
			rq.setInstanceId(queue.getRequest().getInstance().getId());
		if (DataManager.i().mLocalUser != null)
			rq.setUserId(DataManager.i().mLocalUser.getId());
		if (queue.getRequest().getEmployer() != null)
			rq.setEmployerId(queue.getRequest().getEmployer().getId());
		rq.setStatus(RequestStatusType.UNPROCESSED);
		// send it to the server
		PipelineRequest pr = DataManager.i().addPipelineRequest(rq);
		// tell the user
		if (pr != null)
			Utils.alertUser("Reprocess Request", "Reprocess successfully requested");
		else
			Utils.alertUser("ERROR", "Reprocess Request Failed!");
*/	}	
	
	public static class HBoxCell extends HBox {
         Label lblDate = new Label();
         Label lblRecordIndex = new Label();
         Label lblMessage = new Label();

         HBoxCell(String date, String recordIndex, String message, boolean headerRow) {
              super();

              if (date == null ) date = "";
              if (message == null) message = "";
              if (recordIndex == null) recordIndex = "";
              
              Utils.setHBoxLabel(lblDate, 120, false, date);
              Utils.setHBoxLabel(lblRecordIndex, 120, false, recordIndex);
              Utils.setHBoxLabel(lblMessage, 600, false, message);

           	  if (headerRow == true) {
           		  lblDate.setTextFill(Color.GREY);
           		  lblRecordIndex.setTextFill(Color.GREY);
            	  lblMessage.setTextFill(Color.GREY);
              } 
           	  
        	  this.getChildren().addAll(lblDate, lblRecordIndex, lblMessage);
         }
    }	
	
	public class ColumnDetail {
		public String header;
		public String content;
		public int columnPosition = -1;
		public List<Integer> mapperPositions = new ArrayList<Integer>();
		public String color;
		public boolean active = false;
		
		public ColumnDetail(String header, int columnPosition, String color) {
			this.header = header;
			this.columnPosition = columnPosition;
			this.color = color;
		}
	}
}
