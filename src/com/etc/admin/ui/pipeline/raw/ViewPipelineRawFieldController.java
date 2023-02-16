package com.etc.admin.ui.pipeline.raw;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.ui.pipeline.queue.PipelineQueue;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileRecordRejection;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;
import com.etc.corvetto.ems.pipeline.entities.RawConversionFailure;
import com.etc.corvetto.ems.pipeline.entities.RawCoverage;
import com.etc.corvetto.ems.pipeline.entities.RawDependent;
import com.etc.corvetto.ems.pipeline.entities.RawEmployee;
import com.etc.corvetto.ems.pipeline.entities.RawInvalidation;
import com.etc.corvetto.ems.pipeline.entities.RawNotice;
import com.etc.corvetto.ems.pipeline.entities.RawPay;
import com.etc.embeds.SSN;
import com.etc.utils.types.RequestStatusType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

	public class ViewPipelineRawFieldController 
	{
		@FXML
		private SpreadsheetView rwfldFieldsView;
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
		@FXML
		private Button rwfldLoading;
		
		int[] minColWidth = new int[100];
		boolean getErrorFields = true;
		int currentRawDataType = 0;
		int currentSubFile = 0;
		PipelineQueue queue = null;
		int currentSPV = 0;
		
		ListView<HBoxRawDetailCell> completeView = new ListView<HBoxRawDetailCell>();
		Label errorLabel = new Label("Error View - this is a comprehensive view of only the records marked with errors. Refer to color code above.");
		
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
		
		Boolean complexViewEnabled = false;
		
		RawFieldData rawData = new RawFieldData();
		RawMapperDisplay mapperDisplay = new RawMapperDisplay();
		
		Stack<Integer> toggleRowStack = new Stack<Integer>();

		String currentRawErrors = "";
		boolean currentRowIgnored = false;
		boolean currentRawInvalidated = false;
		
		List<SpreadsheetCell> rowCells = new ArrayList<>();
		ObservableList<SpreadsheetCell> mapperRow = FXCollections.observableList(rowCells);
		int currentMapperRow = 0;
		
		/**
		 * initialize is called when the FXML is loaded
		 */
		@FXML
		public void initialize() {
			initControls();
			updateControls();
			rwfldFileName.setText("Request " +  queue.getRequest().getId().toString() + ": " + queue.getRequest().getDescription());
			// hang up a loading sign
			rwfldLoading.setVisible(true);
			// and update the data
			rawData.updateData(DataManager.i().mPipelineQueueEntry);
			mapperDisplay.updateMapperDisplay(queue);
		}
		
		private void initControls() 
		{
			// set this controller 
//			rawData.setRawFieldMapController(this);
			// set the mapper display target
			//mapperDisplay.setVBox(rwfldMapperVBox);

			// reset any error items
			completeView.getItems().clear();
			completeView.setMaxHeight(350.0f);
			rwfldApproveConversion.setVisible(false);

			// default to the first sub file
			currentSubFile = 0;

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

		private void showRawFields(){	
			 if (queue == null) return;
			 
			 //set up the error view, cleverly called completeView...
			 rwfldVBox.getChildren().remove(completeView);
			 rwfldVBox.getChildren().remove(errorLabel);
			 rwfldVBox.getChildren().addAll(errorLabel, completeView);
			 
			// update the selection
			if (rwfldRawTypeChoice.getItems().size() > 0 && rwfldRawTypeChoice.getItems().size() > currentSubFile)
				rwfldRawTypeChoice.getSelectionModel().select(currentSubFile);

			String fileName = "";
			switch(queue.getFileType()) {
			case EMPLOYEE:
				fileName = queue.getEmployeeFile().getId().toString() + " Employee";
				showRawEmployees();
				break;
			case PAY:
				fileName = queue.getPayFile().getId().toString() + " Pay";
				showRawEmployees();
				showRawPays();
				break;
			case COVERAGE:
				fileName = queue.getCoverageFile().getId().toString() + " Coverage";
				showRawCoverages();
				break;
			case IRS1094C:
				//updateRawIrs1094cData();
				break;
			case IRS1095C:
				//updateRawIrs1095cData();
				break;
			case IRSAIRERR:
				//updateRawAirTranErrorData();
			case IRSAIRRCPT:
				//updateRawAirTranReceiptData();
			default:
				break;
			}
			
			rwfldFileNameField.setText(fileName);
			loadFileRecordRejections();
			
			//clean up in case there are no error records
			if (completeView.getItems().size() == 0) {
				rwfldVBox.getChildren().remove(errorLabel);
				rwfldVBox.getChildren().remove(completeView);
			}

		}
		
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
		
		private void showRawEmployees(){	
			EtcAdmin.i().setStatusMessage("Loading Raw Employee Fields...");
			EtcAdmin.i().setProgress(.75);

			List<RawEmployee> rawEmps = rawData.mRawEmployees;
			if (rawEmps == null) {
				if (rwfldRawTypeChoice.getItems().size() > 1)
					EtcAdmin.i().setStatusMessage("Ready - No Raw Records. Please check the other SubFiles");
				else
					EtcAdmin.i().setStatusMessage("Ready - No Raw Employee Records");
				EtcAdmin.i().setProgress(0);
				return;
			}
			
			currentMapperRow = 1;
			// iterate through the raw employees and fill the grid
			for (RawEmployee emp : rawEmps) {
				if (emp == null) continue;
				
				currentMapperRow++;
				createCellRow(emp);
				
				// set the ignored flag for color coding the row
				if (emp.getRecordInformation() != null)
					currentRowIgnored = emp.getRecordInformation().isIgnored();
				else
					currentRowIgnored = false;
				// gather up any errors
				currentRawErrors = getErrors(emp.getInvalidations(), emp.getNotices());
				// iterate through the fields
				
				// add to the view
				String errors = getInvalidations(emp.getInvalidations(), emp.getConversionFailures());
				if (errors != "")
					completeView.getItems().add(new HBoxRawDetailCell(emp, mapperDisplay));
				
				// add any dependent elements
				if (rawData.mRawDependents != null) {
					for (RawDependent empSec: rawData.mRawDependents) {
						if (empSec.getRawEmployeeId() == emp.getId().longValue()) {
							errors = getInvalidations(empSec.getInvalidations(), empSec.getConversionFailures());
							if (errors != "")
								completeView.getItems().add(new HBoxRawDetailCell(empSec, mapperDisplay));
						}
					}
				}
			}
			
			EtcAdmin.i().setStatusMessage("Ready");
			EtcAdmin.i().setProgress(0);		
			return;
		}
		
		private void showRawPays(){	
			EtcAdmin.i().setStatusMessage("Loading Raw Pay Fields...");
			EtcAdmin.i().setProgress(.75);

			List<RawPay> rawPays = rawData.mRawPays;
			if (rawPays == null || rawPays.size() == 0) {
				if (rwfldRawTypeChoice.getItems().size() > 1)
					EtcAdmin.i().setStatusMessage("Ready - No Raw Records. Please check the other SubFiles");
				else
					EtcAdmin.i().setStatusMessage("Ready - No Raw Pay Records");
				EtcAdmin.i().setProgress(0);
				return;
			}
			
			// iterate through the raw employees and fill the grid
			for (RawPay pay : rawPays) {
				if (pay == null) continue;
				
				// set the ignored flag for color coding the row
				if (pay.getRecordInformation() != null)
					currentRowIgnored = pay.getRecordInformation().isIgnored();
				else
					currentRowIgnored = false;
				// gather up any errors
				currentRawErrors = getErrors(pay.getInvalidations(), pay.getNotices());
				// iterate through the fields
				
				// add to the view
				String errors = getInvalidations(pay.getInvalidations(), pay.getConversionFailures());
				if (errors != "")
					completeView.getItems().add(new HBoxRawDetailCell(pay, mapperDisplay));
			}
			
			EtcAdmin.i().setStatusMessage("Ready");
			EtcAdmin.i().setProgress(0);		
			return;
		}

		private void showRawCoverages(){	
			EtcAdmin.i().setStatusMessage("Loading Raw Coverage Fields...");
			EtcAdmin.i().setProgress(.75);

			List<RawCoverage> rawCovs = rawData.mRawCoverages;
			if (rawCovs == null) {
				if (rwfldRawTypeChoice.getItems().size() > 1)
					EtcAdmin.i().setStatusMessage("Ready - No Raw Records. Please check the other SubFiles");
				else
					EtcAdmin.i().setStatusMessage("Ready - No Raw Pay Records");
				EtcAdmin.i().setProgress(0);
				return;
			}
			
			// iterate through the raw employees and fill the grid
			for (RawCoverage cov : rawCovs) {
				if (cov == null) continue;
				
				// set the ignored flag for color coding the row
				if (cov.getRecordInformation() != null)
					currentRowIgnored = cov.getRecordInformation().isIgnored();
				else
					currentRowIgnored = false;
				// gather up any errors
				currentRawErrors = getErrors(cov.getInvalidations(), cov.getNotices());
				// iterate through the fields
				
				// add to the view
				String errors = getInvalidations(cov.getInvalidations(), cov.getConversionFailures());
				if (errors != "")
					completeView.getItems().add(new HBoxRawDetailCell(cov, mapperDisplay));
			}
			
			EtcAdmin.i().setStatusMessage("Ready");
			EtcAdmin.i().setProgress(0);		
			return;
		}
		
		private void showRawDependents(){	
			EtcAdmin.i().setStatusMessage("Loading Raw Dependent Fields...");
			EtcAdmin.i().setProgress(.75);

			List<RawDependent> rawDeps = rawData.mRawDependents;
			if (rawDeps == null) {
				if (rwfldRawTypeChoice.getItems().size() > 1)
					EtcAdmin.i().setStatusMessage("Ready - No Raw Records. Please check the other SubFiles");
				else
					EtcAdmin.i().setStatusMessage("Ready - No Raw Pay Records");
				EtcAdmin.i().setProgress(0);
				return;
			}
			
			// iterate through the raw employees and fill the grid
			for (int row = 0; row <  21; row++) {
				RawDependent dep = rawDeps.get(row);
				if (dep == null) continue;
				
				// set the ignored flag for color coding the row
				if (dep.getRecordInformation() != null)
					currentRowIgnored = dep.getRecordInformation().isIgnored();
				else
					currentRowIgnored = false;
				// gather up any errors
				currentRawErrors = getErrors(dep.getInvalidations(), dep.getNotices());
				// iterate through the fields
				
				// add to the view
				String errors = getInvalidations(dep.getInvalidations(), dep.getConversionFailures());
				if (errors != "")
					completeView.getItems().add(new HBoxRawDetailCell(dep, mapperDisplay));
			}
			
			EtcAdmin.i().setStatusMessage("Ready");
			EtcAdmin.i().setProgress(0);		
			return;
		}
		
		private void createCellRow(RawEmployee rawEmp)
		{
			mapperRow.clear();
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(1), rawEmp.getEmployerIdentifier()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(2),rawEmp.getSsn()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(3),rawEmp.getFullName()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(4),rawEmp.getFirstName()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(5),rawEmp.getMiddleName()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(6),rawEmp.getLastName()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(7),"")); //rawEmp.getCity() getFullAddress()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(8),rawEmp.getStreetNumber()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(9),rawEmp.getStreet()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(10),rawEmp.getStreetExtension()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(11),rawEmp.getCity()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(12),rawEmp.getState()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(13),rawEmp.getZip()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(14),"")); //rawEmp.getGenderIdentifier()));
			if (rawEmp.getGenderType() != null)
				addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(15),rawEmp.getGenderType().toString()));
			else 
				addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(15),""));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(16),rawEmp.getDateOfBirth()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(17),rawEmp.getHireDate()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(18),rawEmp.getRehireDate()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(19),rawEmp.getTermDate()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(20),""));//rawEmp.getDepartmentIdentifier()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(21),rawEmp.getJobTitle()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(22),rawEmp.getCustomField1()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(23),rawEmp.getCustomField2()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(24),rawEmp.getCustomField3()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(25),rawEmp.getCustomField4()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(26),rawEmp.getCustomField5()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(27),rawEmp.getId()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(28),rawEmp.isActive()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(29),rawEmp.getBornOn()));
			addCell(getMapperCell(currentMapperRow, mapperDisplay.getMapperColumn(30),rawEmp.getLastUpdated()));
			// add it to the mapper display
			//mapperDisplay.addRow(currentMapperRow, mapperRow);
		}
		
		private void addCell(SpreadsheetCell cell)
		{
			if (cell == null) return;
			
			mapperRow.add(cell);
		}
		
		
		private SpreadsheetCell getMapperCell(int row, int column, String data)
		{
			if (column == 0) return null;
			
			if(data == null) data = "";
			SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, data);
			newCell.setEditable(false);
			if(data.trim().length() > 0 )
			{
				newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
			}
			return newCell;
		}

		private SpreadsheetCell getMapperCell(int row, int column, boolean data)
		{
			if (column == 0) return null;
			
			SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, String.valueOf(data));
			newCell.setEditable(false);
			newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
			return newCell;
		}

		private SpreadsheetCell getMapperCell(int row, int column, int data)
		{
			if (column == 0) return null;
			
			SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, String.valueOf(data));
			newCell.setEditable(false);
			newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
			return newCell;
		}

		private SpreadsheetCell getMapperCell(int row, int column, double data)
		{
			if (column == 0) return null;
			
			SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, String.valueOf(data));
			newCell.setEditable(false);
			newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
			return newCell;
		}

		private SpreadsheetCell getMapperCell(int row, int column, SSN ssn)
		{
			if (column == 0) return null;
			String data = "";
			if (ssn != null)
				data = ssn.getUsrln();
			
			SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, data);
			newCell.setEditable(false);
			newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
			return newCell;
		}

		private SpreadsheetCell getMapperCell(int row, int column, Calendar date)
		{
			if (column == 0) return null;
			String data = "";
			if (date != null)
				data = Utils.getDateString(date);
			
			SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, data);
			newCell.setEditable(false);
			newCell.getStyleClass().add("BackColor" + mapperDisplay.columnColors[column]);
			return newCell;
		}

		private void loadFileRecordRejections() {
			// clear anything already in the list
			rwfldRecordRejectionsList.getItems().clear();
			rwfldRecordRejectionsHeaderLabel.setText("Pipeline File Record Rejections (total: 0)");
			
			//String sAddress;
			List<PipelineFileRecordRejection> rejections = null;
			List<HBoxCell> list = new ArrayList<>();
			String recordIndex;
			switch(queue.getFileType()) {
			case EMPLOYEE:
				if (queue.getEmployeeFile().getRecordRejections() == null) return;
				if (queue.getEmployeeFile().getRecordRejections().size() < 1) return;
				list.add(new HBoxCell("Date", "Record Index", "Message", true));	
				rejections = queue.getEmployeeFile().getRecordRejections();
				break;
			case PAY:
				if (queue.getPayFile().getRecordRejections() == null) return;
				if (queue.getPayFile().getRecordRejections().size() < 1) return;
				rejections = queue.getPayFile().getRecordRejections();
				break;
			case COVERAGE:
				if (queue.getCoverageFile().getRecordRejections() == null) return;
				if (queue.getCoverageFile().getRecordRejections().size() < 1) return;
				rejections = queue.getCoverageFile().getRecordRejections();
				break;
			case IRS1094C:
				if (queue.getIrs1094cFile().getRecordRejections() == null) return;
				if (queue.getIrs1094cFile().getRecordRejections().size() < 1) return;
				rejections = queue.getIrs1094cFile().getRecordRejections();
				break;
			case IRS1095C:
				if (queue.getIrs1095cFile().getRecordRejections() == null) return;
				if (queue.getIrs1095cFile().getRecordRejections().size() < 1) return;
				rejections = queue.getIrs1095cFile().getRecordRejections();
				break;
			case IRSAIRERR:
				if (queue.getAirTranErrorFile().getRecordRejections() == null) return;
				if (queue.getAirTranErrorFile().getRecordRejections().size() < 1) return;
				rejections = queue.getAirTranErrorFile().getRecordRejections();
				break;
			case IRSAIRRCPT:
				if (queue.getAirTranReceiptFile().getRecordRejections() == null) return;
				if (queue.getAirTranReceiptFile().getRecordRejections().size() < 1) return;
				rejections = queue.getAirTranReceiptFile().getRecordRejections();
				break;
			default:
				break;
			}
			
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
		
		public void dataReady() {
			// hide the waiting sign
			rwfldLoading.setVisible(false);
			// disable the sub controls accordingly
			disableSubControls(!rawData.subFilesPresent);
			// show the raw fields
			showRawFields();
		}
		
		private void updateControls() {
			PipelineSpecification spec = null;
			
			//load the filespec
			if ((spec = queue.getRequest().getSpecification()) == null) return;
			rwfldApproveConversion.setVisible(spec.isPostValidationApproval());
			switch(queue.getFileType()) {
			case EMPLOYEE:
				if (queue.getEmployeeFile() == null) return;
	    		DataManager.i().loadDynamicEmployeeFileSpecification(spec.getDynamicFileSpecificationId());
				DataManager.i().mPipelineSpecification = spec;
				DataManager.i().mPipelineChannel = spec.getChannel();
				rwfldCreateEmployeeCheck.setVisible(true);
				Utils.updateControl(rwfldCreateEmployeeCheck,DataManager.i().mDynamicEmployeeFileSpecification.isCreateEmployee());
				rwfldMapEEtoERCheck.setVisible(false);
				rwfldCreateSecondaryCheck.setVisible(false);
				rwfldCreatePlanCheck.setVisible(false);
				// reset the button if the file is marked for validation approval
				if (queue.getEmployeeFile().getPipelineInformation() != null && queue.getEmployeeFile().getPipelineInformation().isPostValidationApproved() == true)
					rwfldApproveConversion.setVisible(false);
				break;
			case PAY:
				if (queue.getPayFile() == null) return;
				if ((spec = queue.getPayFile().getSpecification()) == null) return;
	    		DataManager.i().loadDynamicPayFileSpecification(spec.getDynamicFileSpecificationId());
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
				break;
			case COVERAGE:
				if (queue.getCoverageFile() == null) return;
	    		DataManager.i().loadDynamicCoverageFileSpecification(spec.getDynamicFileSpecificationId());
				DataManager.i().mPipelineSpecification = spec;
				DataManager.i().mPipelineChannel = spec.getChannel();
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
				if (queue.getCoverageFile().getPipelineInformation() != null && queue.getCoverageFile().getPipelineInformation().isPostValidationApproved() == true)
					rwfldApproveConversion.setVisible(false);
				break;
			case IRS1095C:
				if (queue.getIrs1095cFile() == null) return;
				rwfldMapperIdButton.setText(spec.getDynamicFileSpecificationId().toString() + " (click to view)");;
				rwfldSpecIdButton.setText(spec.getId().toString() + " (click to view)");
				rwfldMapperSpecNameField.setText(spec.getName());
				DataManager.i().mPipelineSpecification = spec;
				DataManager.i().mPipelineChannel = spec.getChannel();
				rwfldCreateEmployeeCheck.setVisible(false);
				rwfldMapEEtoERCheck.setVisible(false);
				rwfldCreateSecondaryCheck.setVisible(false);
				rwfldCreatePlanCheck.setVisible(false);
				// reset the button if the file is marked for validation approval
				if (queue.getIrs1095cFile().getPipelineInformation() != null && queue.getIrs1095cFile().getPipelineInformation().isPostValidationApproved() == true)
					rwfldApproveConversion.setVisible(false);
				return;
			case IRS1094C:
				if (queue.getIrs1094cFile() == null) return;
				rwfldMapperIdButton.setText(spec.getDynamicFileSpecificationId().toString() + " (click to view)");;
				rwfldSpecIdButton.setText(spec.getId().toString() + " (click to view)");
				rwfldMapperSpecNameField.setText(spec.getName());
				DataManager.i().mPipelineSpecification = spec;
				DataManager.i().mPipelineChannel = spec.getChannel();
				rwfldCreateEmployeeCheck.setVisible(false);
				rwfldMapEEtoERCheck.setVisible(false);
				rwfldCreateSecondaryCheck.setVisible(false);
				rwfldCreatePlanCheck.setVisible(false);
				// reset the button if the file is marked for validation approval
				if (queue.getIrs1094cFile().getPipelineInformation() != null && queue.getIrs1094cFile().getPipelineInformation().isPostValidationApproved() == true)
					rwfldApproveConversion.setVisible(false);
				return;
			case IRSAIRERR:
				if (queue.getAirTranErrorFile() == null) return;
				rwfldMapperIdButton.setText(spec.getDynamicFileSpecificationId().toString() + " (click to view)");;
				rwfldSpecIdButton.setText(spec.getId().toString() + " (click to view)");
				rwfldMapperSpecNameField.setText(spec.getName());
				DataManager.i().mPipelineSpecification = spec;
				DataManager.i().mPipelineChannel = spec.getChannel();
				rwfldCreateEmployeeCheck.setVisible(false);
				rwfldMapEEtoERCheck.setVisible(false);
				rwfldCreateSecondaryCheck.setVisible(false);
				rwfldCreatePlanCheck.setVisible(false);
				// reset the button if the file is marked for validation approval
				if (queue.getAirTranErrorFile().getPipelineInformation() != null && queue.getAirTranErrorFile().getPipelineInformation().isPostValidationApproved() == true)
					rwfldApproveConversion.setVisible(false);
				return;
			case IRSAIRRCPT:
				if (queue.getAirTranReceiptFile() == null) return;
				rwfldMapperIdButton.setText(spec.getDynamicFileSpecificationId().toString() + " (click to view)");;
				rwfldSpecIdButton.setText(spec.getId().toString() + " (click to view)");
				rwfldMapperSpecNameField.setText(spec.getName());
				DataManager.i().mPipelineSpecification = spec;
				DataManager.i().mPipelineChannel = spec.getChannel();
				rwfldCreateEmployeeCheck.setVisible(false);
				rwfldMapEEtoERCheck.setVisible(false);
				rwfldCreateSecondaryCheck.setVisible(false);
				rwfldCreatePlanCheck.setVisible(false);
				// reset the button if the file is marked for validation approval
				if (queue.getAirTranReceiptFile().getPipelineInformation() != null && queue.getAirTranReceiptFile().getPipelineInformation().isPostValidationApproved() == true)
					rwfldApproveConversion.setVisible(false);
				return;
			default:
				// no file spec
				return;
			}
			
			rwfldMapperIdButton.setText(spec.getDynamicFileSpecificationId().toString() + " (click to view)");;
			rwfldSpecIdButton.setText(spec.getId().toString() + " (click to view)");
			rwfldMapperSpecNameField.setText(spec.getName());

		}
		
		@FXML
		private void onRawDataType(ActionEvent event) {
			currentRawDataType = rwfldRawTypeChoice.getSelectionModel().getSelectedIndex();
			showRawFields();
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
			currentSubFile = rwfldRawTypeChoice.getSelectionModel().getSelectedIndex();
			rawData.getAllData(false);
		}	
		
		@FXML
		private void onApproveConversion(ActionEvent event) {
			rawData.approveConversion();
		}	
		
		@FXML
		private void onReprocess(ActionEvent event) {
			rawData.reprocess();
		}	
		
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
		
	}

