package com.etc.admin.ui.pipeline.rowignore;

import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.spreadsheet.SpreadsheetView;

import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileSpecification;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ViewRowIgnoreController {
	@FXML
	private ListView<HBoxDetailCell> dfcovSelectedSpecView;
	@FXML
	private TextField dfcovNameField;
	@FXML
	private Button dfcovCancelButton;
	@FXML
	private Button dfcovSaveChangesButton;
	@FXML
	private Button dfcovRemoveSpecButton;
	@FXML
	private Label dfcovName;

	SpreadsheetView spv = null;
	// using an array of strings to hold and sort our column field data
	String[] columnFields = new String[100];
	int[] columnCount = new int[100];
	int fieldCount = 0;
	
	int alphaId = 0;
	
	// read only mode for selected mapper files
	public boolean addMode = false;
	
	private int listIndex = 0;
	
	public boolean changesMade = false;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {

	}
	
	public void setSPV(SpreadsheetView spv) {
		this.spv = spv;
	}
	
	public void load() {
		initControls();
		clearData();
		loadIgnoreRowData();
	}
	
	private void initControls() {
		setAddMode(false);
   
		dfcovSaveChangesButton.setDisable(true);

 	}	
	
	private void loadIgnoreRowData() {
		dfcovSaveChangesButton.setDisable(false);
		switch(DataManager.i().mPipelineChannel.getType()){
		case COVERAGE:			
			if(DataManager.i().mDynamicCoverageFileIgnoreRowSpecification != null)
				loadFields(DataManager.i().mDynamicCoverageFileSpecification, DataManager.i().mDynamicCoverageFileIgnoreRowSpecification);
			else {
				loadAddSpec();
				return;
			}
			break;
		case EMPLOYEE:
			if(DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification != null)
				loadFields(DataManager.i().mDynamicEmployeeFileSpecification, DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification);
			else {
				loadAddSpec();
				return;
			}
			break;
		case PAY:
			if(DataManager.i().mDynamicPayFileIgnoreRowSpecification != null)
				loadFields(DataManager.i().mDynamicPayFileSpecification, DataManager.i().mDynamicPayFileIgnoreRowSpecification);
			else {
				loadAddSpec();
				return;
			}
			break;
		case PAYPERIOD:
		//case PLAN:
		case IRS1094C:
		case IRS1095C:
		case IRSAIRERR:
		case IRSAIRRCPT:
		case EVENT:
			break;
		default:
			break;
    		}	
	}
	
	private void setAddMode(boolean mode) {
		addMode = mode;
		if(mode == true) {
			dfcovSaveChangesButton.setText("Add");
			dfcovSaveChangesButton.setDisable(false);
			dfcovRemoveSpecButton.setVisible(false);
		}
		else {
			dfcovSaveChangesButton.setText("Save");
			dfcovSaveChangesButton.setDisable(true);
			dfcovRemoveSpecButton.setVisible(true);
		}
	}
	
	private void clearData() {
		dfcovSelectedSpecView.getItems().clear();
	}
	
	private void loadFields(DynamicCoverageFileSpecification fSpec, DynamicCoverageFileIgnoreRowSpecification igSpec ) {
		//create the spec in case it's null
//		if(igSpec == null) igSpec = new DynamicCoverageFileIgnoreRowSpecification(DataManager.i().createCoreData(0l));
//	    if(igSpec.getName() != null) dfcovNameField.setText(igSpec.getName());
		List<HBoxDetailCell> list = new ArrayList<HBoxDetailCell>();
		String header = "";
		// load the rows by the Mapper display SPV columns
		for (int i = 0; i < spv.getGrid().getColumnHeaders().size(); i++) {
			header = spv.getGrid().getColumnHeaders().get(i);
			if(header == "") continue;
			list.add(new HBoxDetailCell(spv.getGrid().getRows().get(0).get(i).getText(),true, header, false));
		}
		
		ObservableList<HBoxDetailCell> newObservableList = FXCollections.observableList(list);
	    dfcovSelectedSpecView.setItems(newObservableList);	
	
		if(fSpec.isErCol()) updateIgnoreRowData("EMPLOYER ID", igSpec.isErCol());
		if(fSpec.isEtcRefCol()) updateIgnoreRowData("LITE ID", igSpec.isEtcRefCol());
		if(fSpec.isSsnCol()) updateIgnoreRowData("EMPLOYEE SSN", igSpec.isSsnCol());
		if(fSpec.isErRefCol()) updateIgnoreRowData("EMPLOYEE ID", igSpec.isErRefCol());
		if(fSpec.isSubcrbrCol()) updateIgnoreRowData("SUBSCRBR NUMBER", igSpec.isSubcrbrCol());
		if(fSpec.isEeFlagCol()) updateIgnoreRowData("EMPLOYEE FLAG", igSpec.isEeFlagCol());
		if(fSpec.isfNameCol()) updateIgnoreRowData("FIRST NAME", igSpec.isfNameCol());
		if(fSpec.ismNameCol()) updateIgnoreRowData("MIDDLE NAME", igSpec.ismNameCol());
		if(fSpec.islNameCol()) updateIgnoreRowData("LAST NAME", igSpec.islNameCol());
		if(fSpec.isPhoneCol()) updateIgnoreRowData("PHONE NUMBER", igSpec.isPhoneCol());
		if(fSpec.isEmlCol()) updateIgnoreRowData("EMAIL COLUMN", igSpec.isEmlCol());

		if(fSpec.isWavdCol()) updateIgnoreRowData("COV WAIVED", igSpec.isWavdCol());
		if(fSpec.isInelCol()) updateIgnoreRowData("COV INELIGIBLE", igSpec.isInelCol());
		if(fSpec.isTyCol()) updateIgnoreRowData("COV TAX YEAR", igSpec.isTyCol());
		if(fSpec.isCovStartDtCol()) updateIgnoreRowData("COV START DATE", igSpec.isCovStartDtCol());
		if(fSpec.isCovEndDtCol()) updateIgnoreRowData("COV END DATE", igSpec.isCovEndDtCol());
		if(fSpec.isTySelCol()) updateIgnoreRowData("COV TX YR SEL", igSpec.isTySelCol());

		if(fSpec.isJanSelCol()) updateIgnoreRowData("COVERAGE JAN", igSpec.isJanSelCol());
		if(fSpec.isFebSelCol()) updateIgnoreRowData("COVERAGE FEB", igSpec.isFebSelCol());
		if(fSpec.isMarSelCol()) updateIgnoreRowData("COVERAGE MAR", igSpec.isMarSelCol());
		if(fSpec.isAprSelCol()) updateIgnoreRowData("COVERAGE APR", igSpec.isAprSelCol());
		if(fSpec.isMaySelCol()) updateIgnoreRowData("COVERAGE MAY", igSpec.isMaySelCol());
		if(fSpec.isJunSelCol()) updateIgnoreRowData("COVERAGE JUN", igSpec.isJunSelCol());
		if(fSpec.isJulSelCol()) updateIgnoreRowData("COVERAGE JUL", igSpec.isJulSelCol());
		if(fSpec.isAugSelCol()) updateIgnoreRowData("COVERAGE AUG", igSpec.isAugSelCol());
		if(fSpec.isSepSelCol()) updateIgnoreRowData("COVERAGE SEP", igSpec.isSepSelCol());
		if(fSpec.isOctSelCol()) updateIgnoreRowData("COVERAGE OCT", igSpec.isOctSelCol());
		if(fSpec.isNovSelCol()) updateIgnoreRowData("COVERAGE NOV", igSpec.isNovSelCol());
		if(fSpec.isDecSelCol()) updateIgnoreRowData("COVERAGE DEC", igSpec.isDecSelCol());
		if(fSpec.isPlanRefCol()) updateIgnoreRowData("COV PLAN ID", igSpec.isPlanRefCol());
		if(fSpec.isMbrShareCol()) updateIgnoreRowData("COV MBR SHR AMT", igSpec.isMbrShareCol());

		if(fSpec.isStreetNumCol()) updateIgnoreRowData("STRT NUMBER", igSpec.isStreetNumCol());
		if(fSpec.isStreetCol()) updateIgnoreRowData("ADD LINE 1", igSpec.isStreetCol());
		if(fSpec.isLin2Col()) updateIgnoreRowData("ADD LINE 2", igSpec.isLin2Col());
		if(fSpec.isCityCol()) updateIgnoreRowData("CITY", igSpec.isCityCol());
		if(fSpec.isStateCol()) updateIgnoreRowData("STATE", igSpec.isStateCol());
		if(fSpec.isZipCol()) updateIgnoreRowData("ZIP", igSpec.isZipCol());
		if(fSpec.isGenderCol()) updateIgnoreRowData("GENDER", igSpec.isGenderCol());
		if(fSpec.isDobCol()) updateIgnoreRowData("DOB", igSpec.isDobCol());
		if(fSpec.isHireDtCol()) updateIgnoreRowData("HIRE DATE", igSpec.isHireDtCol());
		if(fSpec.isTermDtCol()) updateIgnoreRowData("TERM DATE", igSpec.isTermDtCol());
//		if(fSpec.isDepEtcRefCol()) updateIgnoreRowData("SEC LITE ID", igSpec.isDepEtcRefCol());
//		if(fSpec.isDepSSNCol()) updateIgnoreRowData("SEC SSN", igSpec.isDepSSNCol());
	//	if(fSpec.isDepErRefCol()) updateIgnoreRowData("SEC ID", igSpec.isDepErRefCol());
	//	if(fSpec.isMbrCol()) updateIgnoreRowData("MEMBER NUMBER", igSpec.isMbrCol());

	//	if(fSpec.isDepFNameCol()) updateIgnoreRowData("SEC FIRST NAME", igSpec.isDepFNameCol());
	//	if(fSpec.isDepMNameCol()) updateIgnoreRowData("SEC MIDDLE NAME", igSpec.isDepMNameCol());
	//	if(fSpec.isDepLNameCol()) updateIgnoreRowData("SEC LAST NAME", igSpec.isDepLNameCol());
	//	if(fSpec.isDepStreetNumCol()) updateIgnoreRowData("SEC STRT NUMBER", igSpec.isDepStreetNumCol());
	//	if(fSpec.isDepStreetCol()) updateIgnoreRowData("SEC ADD LINE 1", igSpec.isDepStreetCol());
	//	if(fSpec.isDepLin2Col()) updateIgnoreRowData("SEC ADD LINE 2", igSpec.isDepLin2Col());
	//	if(fSpec.isDepCityCol()) updateIgnoreRowData("SEC CITY", igSpec.isDepCityCol());
	//	if(fSpec.isDepStateCol()) updateIgnoreRowData("SEC STATE", igSpec.isDepStateCol());
	//	if(fSpec.isDepZipCol()) updateIgnoreRowData("SEC ZIP", igSpec.isDepZipCol());
	//	if(fSpec.isDepDOBCol()) updateIgnoreRowData("SEC DOB", igSpec.isDepDOBCol());

		if(fSpec.isCfld1Col()) updateIgnoreRowData(fSpec.getCfld1Name(), igSpec.isCfld1Col());
		if(fSpec.isCfld2Col()) updateIgnoreRowData(fSpec.getCfld2Name(), igSpec.isCfld2Col());
		if(fSpec.isCfld3Col()) updateIgnoreRowData(fSpec.getCfld3Name(), igSpec.isCfld3Col());
		if(fSpec.isCfld4Col()) updateIgnoreRowData(fSpec.getCfld4Name(), igSpec.isCfld4Col());
		if(fSpec.isCfld5Col()) updateIgnoreRowData(fSpec.getCfld5Name(), igSpec.isCfld5Col());

//		if(fSpec.isDepCfld1Col()) updateIgnoreRowData(fSpec.getDepCfld1Name(), igSpec.isDepCfld1Col());
//		if(fSpec.isDepCfld2Col()) updateIgnoreRowData(fSpec.getDepCfld2Name(), igSpec.isDepCfld2Col());
//		if(fSpec.isDepCfld1Col()) updateIgnoreRowData(fSpec.getDepCfld3Name(), igSpec.isDepCfld3Col());
//		if(fSpec.isDepCfld1Col()) updateIgnoreRowData(fSpec.getDepCfld4Name(), igSpec.isDepCfld4Col());
//		if(fSpec.isDepCfld1Col()) updateIgnoreRowData(fSpec.getDepCfld5Name(), igSpec.isDepCfld5Col());

		if(fSpec.isCovCfld1Col()) updateIgnoreRowData(fSpec.getCovCfld1Name(), igSpec.isCovCfld1Col());
		if(fSpec.isCovCfld2Col()) updateIgnoreRowData(fSpec.getCovCfld2Name(), igSpec.isCovCfld2Col());
	}	
	
	/*
	private void updateEmployeeFileData(){

		DynamicEmployeeFileSpecification fSpec = DataManager.i().mDynamicEmployeeFileSpecification;
		DynamicEmployeeFileIgnoreRowSpecification igSpec = DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification;
		// an add has no igSpec, so create a temporary one for the screen load
		if(igSpec == null) igSpec = new DynamicEmployeeFileIgnoreRowSpecification(0);

		if(fSpec != null) {
			loadFields(fSpec, igSpec);	        
		}
	}
	*/
	
	private void loadFields(DynamicEmployeeFileSpecification fSpec, DynamicEmployeeFileIgnoreRowSpecification igSpec) {
		//create the spec in case it's null
//		if(igSpec == null) igSpec = new DynamicEmployeeFileIgnoreRowSpecification(DataManager.i().createCoreData(0l));
	    if(igSpec.getName() != null) dfcovNameField.setText(igSpec.getName());
		List<HBoxDetailCell> list = new ArrayList<HBoxDetailCell>();
		String header = "";
		// load the rows by the Mapper display SPV columns
		for (int i = 0; i < spv.getGrid().getColumnHeaders().size(); i++) {
			header = spv.getGrid().getColumnHeaders().get(i);
			if(header == "") continue;
			list.add(new HBoxDetailCell(spv.getGrid().getRows().get(0).get(i).getText(),true, header, false));
		}
		
		ObservableList<HBoxDetailCell> newObservableList = FXCollections.observableList(list);
	    dfcovSelectedSpecView.setItems(newObservableList);	
	
		if(fSpec.isSsnCol()) updateIgnoreRowData("EMPLOYEE SSN", igSpec.isSsnCol());
		if(fSpec.isErCol()) updateIgnoreRowData("EMPLOYER ID", igSpec.isErCol());
		if(fSpec.isEtcRefCol()) updateIgnoreRowData("LITE ID", igSpec.isEtcRefCol());
		if(fSpec.isSsnCol()) updateIgnoreRowData("EMPLOYEE SSN", igSpec.isSsnCol());
		if(fSpec.isErRefCol()) updateIgnoreRowData("EMPLOYEE ID", igSpec.isErRefCol());
		if(fSpec.isfNameCol()) updateIgnoreRowData("FIRST NAME", igSpec.isfNameCol());
		if(fSpec.ismNameCol()) updateIgnoreRowData("MIDDLE NAME", igSpec.ismNameCol());
		if(fSpec.islNameCol()) updateIgnoreRowData("LAST NAME", igSpec.islNameCol());
		if(fSpec.isPhoneCol()) updateIgnoreRowData("PHONE NUMBER", igSpec.isPhoneCol());
		if(fSpec.isEmlCol()) updateIgnoreRowData("EMAIL COLUMN", igSpec.isEmlCol());
		if(fSpec.isDeptCol()) updateIgnoreRowData("DEPARTMENT", igSpec.isDeptCol());
		if(fSpec.isLocCol()) updateIgnoreRowData("LOCATION", igSpec.isLocCol());
		if(fSpec.isJobTtlCol()) updateIgnoreRowData("JOB TITLE", igSpec.isJobTtlCol());
		if(fSpec.isPayCodeCol()) updateIgnoreRowData("PAYCODE", igSpec.isPayCodeCol());

		if(fSpec.isCfld1Col()) updateIgnoreRowData(fSpec.getCfld1Name(), igSpec.isCfld1Col());
		if(fSpec.isCfld2Col()) updateIgnoreRowData(fSpec.getCfld2Name(), igSpec.isCfld2Col());
		if(fSpec.isCfld3Col()) updateIgnoreRowData(fSpec.getCfld3Name(), igSpec.isCfld3Col());
		if(fSpec.isCfld4Col()) updateIgnoreRowData(fSpec.getCfld4Name(), igSpec.isCfld4Col());
		if(fSpec.isCfld5Col()) updateIgnoreRowData(fSpec.getCfld5Name(), igSpec.isCfld5Col());

		if(fSpec.isStreetNumCol()) updateIgnoreRowData("STRT NUMBER", igSpec.isStreetNumCol());
		if(fSpec.isStreetCol()) updateIgnoreRowData("ADD LINE 1", igSpec.isStreetCol());
		if(fSpec.isLin2Col()) updateIgnoreRowData("ADD LINE 2", igSpec.isLin2Col());
		if(fSpec.isCityCol()) updateIgnoreRowData("CITY", igSpec.isCityCol());
		if(fSpec.isStateCol()) updateIgnoreRowData("STATE", igSpec.isStateCol());
		if(fSpec.isZipCol()) updateIgnoreRowData("ZIP", igSpec.isZipCol());
		
		if(fSpec.isGenderCol()) updateIgnoreRowData("GENDER", igSpec.isGenderCol());
		if(fSpec.isDobCol()) updateIgnoreRowData("DOB", igSpec.isDobCol());
		if(fSpec.isHireDtCol()) updateIgnoreRowData("HIRE DATE", igSpec.isHireDtCol());
		if(fSpec.isRhireDtCol()) updateIgnoreRowData("REHIRE DATE", igSpec.isRhireDtCol());
		if(fSpec.isTermDtCol()) updateIgnoreRowData("TERM DATE", igSpec.isTermDtCol());
 	}

	/*
	private void updatePayFileData(){

		DynamicPayFileSpecification fSpec = DataManager.i().mDynamicPayFileSpecification;
		DynamicPayFileIgnoreRowSpecification igSpec = DataManager.i().mDynamicPayFileIgnoreRowSpecification;

		// an add has no igSpec, so create a temporary one for the screen load
		if(igSpec == null) igSpec = new DynamicPayFileIgnoreRowSpecification(0);

		if(fSpec != null) {
			loadFields(fSpec, igSpec);	        
		}
	}
	*/
	
	private void loadFields(DynamicPayFileSpecification fSpec, DynamicPayFileIgnoreRowSpecification igSpec) {
		
		//create the spec in case it's null
//		if(igSpec == null) igSpec = new DynamicPayFileIgnoreRowSpecification(DataManager.i().createCoreData(0l));
//	    if(igSpec.getName() != null) dfcovNameField.setText(igSpec.getName());
		List<HBoxDetailCell> list = new ArrayList<HBoxDetailCell>();
		String header = "";
		// load the rows by the Mapper display SPV columns
		for (int i = 0; i < spv.getGrid().getColumnHeaders().size(); i++) {
			header = spv.getGrid().getColumnHeaders().get(i);
			if(header == "") continue;
			list.add(new HBoxDetailCell(spv.getGrid().getRows().get(0).get(i).getText(),true, header, false));
		}
		
		ObservableList<HBoxDetailCell> newObservableList = FXCollections.observableList(list);
	    dfcovSelectedSpecView.setItems(newObservableList);	
	
	    // now update the list with the info from the IGNOREROWSPEC
	    if(fSpec.isErCol()) updateIgnoreRowData("EMPLOYER ID",igSpec.isErCol());
		if(fSpec.isSsnCol()) updateIgnoreRowData("EMPLOYEE SSN", igSpec.isSsnCol());
		if(fSpec.isErRefCol()) updateIgnoreRowData("EMPLOYEE IDE", igSpec.isErRefCol());
		if(fSpec.isfNameCol()) updateIgnoreRowData("FIRST NAME", igSpec.isfNameCol());
		if(fSpec.ismNameCol()) updateIgnoreRowData("MIDDLE NAME", igSpec.ismNameCol());
		if(fSpec.islNameCol()) updateIgnoreRowData("LAST NAME", igSpec.islNameCol());

		if(fSpec.isDobCol()) updateIgnoreRowData("DOB", igSpec.isDobCol());
		if(fSpec.isHireDtCol()) updateIgnoreRowData("HIRE DATE", igSpec.isHireDtCol());
		if(fSpec.isRhireDtCol()) updateIgnoreRowData("REHIRE DATE", igSpec.isRhireDtCol());
		if(fSpec.isTermDtCol()) updateIgnoreRowData("TERM DATE", igSpec.isTermDtCol());
		if(fSpec.isDeptCol()) updateIgnoreRowData("DEPARTMENT", igSpec.isDeptCol());
		if(fSpec.isLocCol()) updateIgnoreRowData("LOCATION", igSpec.isLocCol());
		if(fSpec.isPpdStartDtCol()) updateIgnoreRowData("PPD START DATE", igSpec.isPpdStartDtCol());
		if(fSpec.isPpdEndDtCol()) updateIgnoreRowData("PPD END DATE", igSpec.isPpdEndDtCol());
		if(fSpec.isPayDtCol()) updateIgnoreRowData("PAY DATE", igSpec.isPayDtCol());

		if(fSpec.isPpdErRefCol()) updateIgnoreRowData("PPD ID", igSpec.isPpdErRefCol());
		if(fSpec.isPpdFreqCol()) updateIgnoreRowData("PPD FREQUENCY", igSpec.isPpdFreqCol());
		if(fSpec.isHrsCol()) updateIgnoreRowData("HOURS", igSpec.isHrsCol());
		if(fSpec.isRateCol()) updateIgnoreRowData("RATE", igSpec.isRateCol());
		if(fSpec.isAmtCol()) updateIgnoreRowData("AMOUNT", igSpec.isAmtCol());
		if(fSpec.isPayCodeCol()) updateIgnoreRowData("PAY CODE", igSpec.isPayCodeCol());
		if(fSpec.isUnionTypeCol()) updateIgnoreRowData("UNION TYPE", igSpec.isUnionTypeCol());
		
		if(fSpec.isCfld1Col()) updateIgnoreRowData(fSpec.getCfld1Name(), igSpec.isCfld1Col());
		if(fSpec.isCfld2Col()) updateIgnoreRowData(fSpec.getCfld2Name(), igSpec.isCfld2Col());
		if(fSpec.isCfld3Col()) updateIgnoreRowData(fSpec.getCfld3Name(), igSpec.isCfld3Col());
		if(fSpec.isCfld4Col()) updateIgnoreRowData(fSpec.getCfld4Name(), igSpec.isCfld4Col());
		if(fSpec.isCfld5Col()) updateIgnoreRowData(fSpec.getCfld5Name(), igSpec.isCfld5Col());

		if(fSpec.isStreetNumCol()) updateIgnoreRowData("STRT NUMBER", igSpec.isStreetNumCol());
		if(fSpec.isStreetCol()) updateIgnoreRowData("ADD LINE 1", igSpec.isStreetCol());
		if(fSpec.isLin2Col()) updateIgnoreRowData("ADD LINE 2", igSpec.isStreetCol());
		if(fSpec.isCityCol()) updateIgnoreRowData("CITY", igSpec.isCityCol());
		if(fSpec.isStateCol()) updateIgnoreRowData("STATE", igSpec.isStateCol());
		if(fSpec.isZipCol()) updateIgnoreRowData("ZIP", igSpec.isZipCol());
		
		if(fSpec.isOtHrsCol()) updateIgnoreRowData("OT HRS", igSpec.isOtHrsCol());
		if(fSpec.isOtHrs2Col()) updateIgnoreRowData("OT 2 HRS", igSpec.isOtHrs2Col());
		if(fSpec.isOtHrs3Col()) updateIgnoreRowData("OT 3 HRS", igSpec.isOtHrs3Col());
		if(fSpec.isPtoHrsCol()) updateIgnoreRowData("PTO HRS", igSpec.isPtoHrsCol());
		if(fSpec.isSickHrsCol()) updateIgnoreRowData("SICK PAY HRS", igSpec.isSickHrsCol());
		if(fSpec.isHolidayHrsCol()) updateIgnoreRowData("HLDY PAY HRS", igSpec.isHolidayHrsCol());

		if(fSpec.isPayCfld1Col()) updateIgnoreRowData(fSpec.getPayCfld1Name(), igSpec.isPayCfld1Col());
		if(fSpec.isPayCfld2Col()) updateIgnoreRowData(fSpec.getPayCfld2Name(), igSpec.isPayCfld2Col());
		if(fSpec.isPayCfld3Col()) updateIgnoreRowData(fSpec.getPayCfld3Name(), igSpec.isPayCfld3Col());
		if(fSpec.isPayCfld4Col()) updateIgnoreRowData(fSpec.getPayCfld4Name(), igSpec.isPayCfld4Col());
		if(fSpec.isPayCfld5Col()) updateIgnoreRowData(fSpec.getPayCfld5Name(), igSpec.isPayCfld5Col());
		if(fSpec.isPayCfld6Col()) updateIgnoreRowData(fSpec.getPayCfld6Name(), igSpec.isPayCfld6Col());
		if(fSpec.isPayCfld7Col()) updateIgnoreRowData(fSpec.getPayCfld7Name(), igSpec.isPayCfld7Col());
	}
	
	private void updateIgnoreRowData(String label, boolean value) {
		for (HBoxDetailCell cell : dfcovSelectedSpecView.getItems()) {
			if(cell.setValue(label, value) == true) return;
		}
	}
	
	private void updateIgnoreRowSpecification() {
		
/*		switch(DataManager.i().mPipelineChannel.getType()){
		case COVERAGE:
			if(addMode == true) {
				//create a new DynamicCoverageFileIgnoreRowSpecification to hold the data
				DynamicCoverageFileIgnoreRowSpecification ignoreSpec = new DynamicCoverageFileIgnoreRowSpecification(DataManager.i().createCoreData(0));
				saveFields(ignoreSpec);
				AddDynamicCoverageFileIgnoreRowSpecificationRequest addCoverageIRRequest = 
						new AddDynamicCoverageFileIgnoreRowSpecificationRequest(ignoreSpec);
				DataManager.i().addDynamicCoverageFileIgnoreRowSpecification(addCoverageIRRequest);
			} else {
				DynamicCoverageFileIgnoreRowSpecification igSpec = DataManager.i().mDynamicCoverageFileIgnoreRowSpecification;
				saveFields(igSpec);
				UpdateDynamicCoverageFileIgnoreRowSpecificationRequest updateCoverageIRRequest = 
						new UpdateDynamicCoverageFileIgnoreRowSpecificationRequest(igSpec);
				updateCoverageIRRequest.setSpecification(DataManager.i().mDynamicCoverageFileSpecification);
				DataManager.i().updateDynamicCoverageFileIgnoreRowSpecification(updateCoverageIRRequest);
			}
			break;
		case EMPLOYEE:
			if(addMode == true) {
				//create a new DynamicEmployeeFileIgnoreRowSpecification to hold the data
				DynamicEmployeeFileIgnoreRowSpecification ignoreSpec = new DynamicEmployeeFileIgnoreRowSpecification(DataManager.i().createCoreData(0));
				saveFields(ignoreSpec);
				AddDynamicEmployeeFileIgnoreRowSpecificationRequest addEmployeeIRRequest = 
						new AddDynamicEmployeeFileIgnoreRowSpecificationRequest(ignoreSpec);
				DataManager.i().addDynamicEmployeeFileIgnoreRowSpecification(addEmployeeIRRequest);
			} else {
				DynamicEmployeeFileIgnoreRowSpecification igSpec = DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification;
				saveFields(igSpec);
				UpdateDynamicEmployeeFileIgnoreRowSpecificationRequest updateEmployeeIRRequest = 
						new UpdateDynamicEmployeeFileIgnoreRowSpecificationRequest(igSpec);
				updateEmployeeIRRequest.setSpecification(DataManager.i().mDynamicEmployeeFileSpecification);
				DataManager.i().updateDynamicEmployeeFileIgnoreRowSpecification(updateEmployeeIRRequest);
			}
			break;
		case PAY:
			if(addMode == true) {
				//create a new DynamicPayFileIgnoreRowSpecification to hold the data
				DynamicPayFileIgnoreRowSpecification ignoreSpec = new DynamicPayFileIgnoreRowSpecification(DataManager.i().createCoreData(0));
				saveFields(ignoreSpec);
				AddDynamicPayFileIgnoreRowSpecificationRequest addPayIRRequest = 
						new AddDynamicPayFileIgnoreRowSpecificationRequest(ignoreSpec);
				DataManager.i().addDynamicPayFileIgnoreRowSpecification(addPayIRRequest);
			} else {
				DynamicPayFileIgnoreRowSpecification igSpec = DataManager.i().mDynamicPayFileIgnoreRowSpecification;
				saveFields(igSpec);
				UpdateDynamicPayFileIgnoreRowSpecificationRequest updatePayIRRequest = 
						new UpdateDynamicPayFileIgnoreRowSpecificationRequest(igSpec);
				updatePayIRRequest.setSpecification(DataManager.i().mDynamicPayFileSpecification);
				DataManager.i().updateDynamicPayFileIgnoreRowSpecification(updatePayIRRequest);
			}
			break;
		case PAYPERIOD:
		case PLAN:
		case IRS1094C:
		case IRS1095C:
		case IRSAIRERR:
		case IRSAIRRCPT:
		case EVENT:
			break;
		default:
			break;
		}	
*/	}
	
	private void addIgnoreRowSpecification() {
/*		switch(DataManager.i().mPipelineChannel.getType()){
		case COVERAGE:
			DataManager.i().mDynamicCoverageFileIgnoreRowSpecification = new DynamicCoverageFileIgnoreRowSpecification(0);
			saveFields(DataManager.i().mDynamicCoverageFileIgnoreRowSpecification);
			AddDynamicCoverageFileIgnoreRowSpecificationRequest covIRRequest = 
					new AddDynamicCoverageFileIgnoreRowSpecificationRequest(DataManager.i().mDynamicCoverageFileIgnoreRowSpecification);
			DataManager.i().addDynamicCoverageFileIgnoreRowSpecification(covIRRequest);
			break;
		case EMPLOYEE:
			DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification = new DynamicEmployeeFileIgnoreRowSpecification(0);
			saveFields(DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification);
			AddDynamicEmployeeFileIgnoreRowSpecificationRequest empIRRequest = 
					new AddDynamicEmployeeFileIgnoreRowSpecificationRequest(DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification);
			DataManager.i().addDynamicEmployeeFileIgnoreRowSpecification(empIRRequest);
			break;
		case PAY:
			DataManager.i().mDynamicPayFileIgnoreRowSpecification = new DynamicPayFileIgnoreRowSpecification(0);
			//generate a new 
			//saveFields(DataManager.i().mDynamicPayFileIgnoreRowSpecification);
			AddDynamicPayFileIgnoreRowSpecificationRequest payIRRequest = 
					new AddDynamicPayFileIgnoreRowSpecificationRequest(DataManager.i().mDynamicPayFileIgnoreRowSpecification);
			payIRRequest.setSpecification(DataManager.i().mDynamicPayFileSpecification);
			payIRRequest.setName("Test");
			DataManager.i().addDynamicPayFileIgnoreRowSpecification(payIRRequest);
			break;
		case PAYPERIOD:
		case PLAN:
		case IRS1094C:
		case IRS1095C:
		case IRSAIRERR:
		case IRSAIRRCPT:
		case EVENT:
			break;
		default:
			break;
		}	
*/	}
	
	private void saveFields(DynamicEmployeeFileIgnoreRowSpecification request) {
		DynamicEmployeeFileSpecification fSpec = DataManager.i().mDynamicEmployeeFileSpecification;
		request.setSpecification(fSpec);
		request.setName(dfcovNameField.getText());

		boolean required = false;
		int refIndex = 0;
		for (HBoxDetailCell cell : dfcovSelectedSpecView.getItems()) {
			required = cell.getColumn();
			refIndex = cell.getColumnIndex();
			setRequestColValue(fSpec, refIndex, required, request);
		}
	}

	private void setRequestColValue(DynamicEmployeeFileSpecification fSpec, int refIndex, boolean required, DynamicEmployeeFileIgnoreRowSpecification request) {

		if(fSpec.getErColIndex() == refIndex && fSpec.isErCol() == true) request.setErCol(required);
		if(fSpec.getEtcRefColIndex() == refIndex && fSpec.isEtcRefCol() == true) request.setEtcRefCol(required);
		if(fSpec.getSsnColIndex() == refIndex && fSpec.isSsnCol() == true) request.setSsnCol(required);
		if(fSpec.getErRefColIndex() == refIndex && fSpec.isErRefCol() == true) request.setErRefCol(required);
		if(fSpec.getfNameColIndex() == refIndex && fSpec.isfNameCol() == true) request.setfNameCol(required);
		if(fSpec.getmNameColIndex() == refIndex && fSpec.ismNameCol() == true) request.setmNameCol(required);
		if(fSpec.getlNameColIndex() == refIndex && fSpec.islNameCol() == true) request.setlNameCol(required);
		if(fSpec.getPhoneColIndex() == refIndex && fSpec.isPhoneCol() == true) request.setPhoneCol(required);
		if(fSpec.getEmlColIndex() == refIndex && fSpec.isEmlCol() == true) request.setEmlCol(required);
		if(fSpec.getDeptColIndex() == refIndex && fSpec.isDeptCol() == true) request.setDeptCol(required);
		if(fSpec.getLocColIndex() == refIndex && fSpec.isLocCol() == true) request.setLocCol(required);
		if(fSpec.getJobTtlColIndex() == refIndex && fSpec.isJobTtlCol() == true) request.setJobTtlCol(required);
		if(fSpec.getPayCodeColIndex() == refIndex && fSpec.isPayCodeCol() == true) request.setPayCodeCol(required);

		if(fSpec.getCfld1ColIndex() == refIndex && fSpec.isCfld1Col() == true) request.setCfld1Col(required);
		if(fSpec.getCfld2ColIndex() == refIndex && fSpec.isCfld2Col() == true) request.setCfld2Col(required);
		if(fSpec.getCfld3ColIndex() == refIndex && fSpec.isCfld3Col() == true) request.setCfld3Col(required);
		if(fSpec.getCfld4ColIndex() == refIndex && fSpec.isCfld4Col() == true) request.setCfld4Col(required);
		if(fSpec.getCfld5ColIndex() == refIndex && fSpec.isCfld5Col() == true) request.setCfld5Col(required);

		if(fSpec.getStreetNumColIndex() == refIndex && fSpec.isStreetNumCol() == true) request.setStreetNumCol(required);
		if(fSpec.getStreetColIndex() == refIndex && fSpec.isStreetCol() == true) request.setStreetCol(required);
		if(fSpec.getLin2ColIndex() == refIndex && fSpec.isLin2Col() == true) request.setLin2Col(required);
		if(fSpec.getCityColIndex() == refIndex && fSpec.isCityCol() == true) request.setCityCol(required);
		if(fSpec.getStateColIndex() == refIndex && fSpec.isStateCol() == true) request.setStateCol(required);
		if(fSpec.getZipColIndex() == refIndex && fSpec.isZipCol() == true) request.setZipCol(required);
		
		if(fSpec.getGenderColIndex() == refIndex && fSpec.isGenderCol() == true) request.setGenderCol(required);
		if(fSpec.getDobColIndex() == refIndex && fSpec.isDobCol() == true) request.setDobCol(required);
		if(fSpec.getHireDtColIndex() == refIndex && fSpec.isHireDtCol() == true) request.setHireDtCol(required);
		if(fSpec.getRhireDtColIndex() == refIndex && fSpec.isRhireDtCol() == true) request.setRhireDtCol(required);
		if(fSpec.getTermDtColIndex() == refIndex && fSpec.isTermDtCol() == true) request.setTermDtCol(required);
	}

	private void saveFields(DynamicCoverageFileIgnoreRowSpecification request) {
		DynamicCoverageFileSpecification fSpec = DataManager.i().mDynamicCoverageFileSpecification;
		request.setSpecification(fSpec);
//		request.setName(dfcovNameField.getText());

		boolean required = false;
		int refIndex = 0;
		for (HBoxDetailCell cell : dfcovSelectedSpecView.getItems()) {
			required = cell.getColumn();
			refIndex = cell.getColumnIndex();
			setRequestColValue(fSpec, refIndex, required, request);
		}
	}

	private void setRequestColValue(DynamicCoverageFileSpecification fSpec, int refIndex, boolean required, DynamicCoverageFileIgnoreRowSpecification request) {
		
		if(fSpec.getErColIndex() == refIndex && fSpec.isErCol() == true) request.setErCol(required);
		if(fSpec.getEtcRefColIndex() == refIndex && fSpec.isEtcRefCol() == true) request.setEtcRefCol(required);
		if(fSpec.getSsnColIndex() == refIndex && fSpec.isSsnCol() == true) request.setSsnCol(required);
		if(fSpec.getErRefColIndex() == refIndex && fSpec.isErRefCol() == true) request.setErRefCol(required);
		if(fSpec.getSubcrbrColIndex() == refIndex && fSpec.isSubcrbrCol() == true) request.setSubcrbrCol(required);
		if(fSpec.getEeFlagColIndex() == refIndex && fSpec.isEeFlagCol() == true) request.setEeFlagCol(required);
		if(fSpec.isEeFlagCol()) request.setEeFlagCol(getNextListValue());
		if(fSpec.getfNameColIndex() == refIndex && fSpec.isfNameCol() == true) request.setfNameCol(required);
		if(fSpec.getmNameColIndex() == refIndex && fSpec.ismNameCol() == true) request.setmNameCol(required);
		if(fSpec.getlNameColIndex() == refIndex && fSpec.islNameCol() == true) request.setlNameCol(required);
		if(fSpec.getPhoneColIndex() == refIndex && fSpec.isPhoneCol() == true) request.setPhoneCol(required);
		if(fSpec.getEmlColIndex() == refIndex && fSpec.isEmlCol() == true) request.setEmlCol(required);
		if(fSpec.getWavdColIndex() == refIndex && fSpec.isWavdCol() == true) request.setWavdCol(required);
		if(fSpec.getInelColIndex() == refIndex && fSpec.isInelCol() == true) request.setInelCol(required);
		if(fSpec.getTyColIndex() == refIndex && fSpec.isTyCol() == true) request.setTyCol(required);
		if(fSpec.getCovStartDtColIndex() == refIndex && fSpec.isCovStartDtCol() == true) request.setCovStartDtCol(required);
		if(fSpec.getCovEndDtColIndex() == refIndex && fSpec.isCovEndDtCol() == true) request.setCovEndDtCol(required);
		if(fSpec.getTySelColIndex() == refIndex && fSpec.isTySelCol() == true) request.setTySelCol(required);

		if(fSpec.getJanSelColIndex() == refIndex && fSpec.isJanSelCol() == true) request.setJanSelCol(required);
		if(fSpec.getFebSelColIndex() == refIndex && fSpec.isFebSelCol() == true) request.setFebSelCol(required);
		if(fSpec.getMarSelColIndex() == refIndex && fSpec.isMarSelCol() == true) request.setMarSelCol(required);
		if(fSpec.getAprSelColIndex() == refIndex && fSpec.isAprSelCol() == true) request.setAprSelCol(required);
		if(fSpec.getMaySelColIndex() == refIndex && fSpec.isMaySelCol() == true) request.setMaySelCol(required);
		if(fSpec.getJunSelColIndex() == refIndex && fSpec.isJunSelCol() == true) request.setJunSelCol(required);
		if(fSpec.getJulSelColIndex() == refIndex && fSpec.isJulSelCol() == true) request.setJulSelCol(required);
		if(fSpec.getAugSelColIndex() == refIndex && fSpec.isAugSelCol() == true) request.setAugSelCol(required);
		if(fSpec.getSepSelColIndex() == refIndex && fSpec.isSepSelCol() == true) request.setSepSelCol(required);
		if(fSpec.getOctSelColIndex() == refIndex && fSpec.isOctSelCol() == true) request.setOctSelCol(required);
		if(fSpec.getNovSelColIndex() == refIndex && fSpec.isNovSelCol() == true) request.setNovSelCol(required);
		if(fSpec.getDecSelColIndex() == refIndex && fSpec.isDecSelCol() == true) request.setDecSelCol(required);

		if(fSpec.getPlanRefColIndex() == refIndex && fSpec.isPlanRefCol() == true) request.setPlanRefCol(required);
		if(fSpec.getMbrShareColIndex() == refIndex && fSpec.isMbrShareCol() == true) request.setMbrShareCol(required);
		if(fSpec.getStreetNumColIndex() == refIndex && fSpec.isStreetNumCol() == true) request.setStreetNumCol(required);
		if(fSpec.getStreetColIndex() == refIndex && fSpec.isStreetCol() == true) request.setStreetCol(required);
		if(fSpec.getLin2ColIndex() == refIndex && fSpec.isLin2Col() == true) request.setLin2Col(required);
		if(fSpec.getCityColIndex() == refIndex && fSpec.isCityCol() == true) request.setCityCol(required);
		if(fSpec.getStateColIndex() == refIndex && fSpec.isStateCol() == true) request.setStateCol(required);
		if(fSpec.getZipColIndex() == refIndex && fSpec.isZipCol() == true) request.setZipCol(required);
		if(fSpec.getGenderColIndex() == refIndex && fSpec.isGenderCol() == true) request.setGenderCol(required);
		if(fSpec.getDobColIndex() == refIndex && fSpec.isDobCol() == true) request.setDobCol(required);
		if(fSpec.getHireDtColIndex() == refIndex && fSpec.isHireDtCol() == true) request.setHireDtCol(required);
		if(fSpec.getTermDtColIndex() == refIndex && fSpec.isTermDtCol() == true) request.setTermDtCol(required);

//		if(fSpec.getSecEtcRefColIndex() == refIndex && fSpec.isSecEtcRefCol() == true) request.setSecEtcRefCol(required);
//		if(fSpec.getSecSSNColIndex() == refIndex && fSpec.isSecSSNCol() == true) request.setSecSSNCol(required);
//		if(fSpec.getSecErRefColIndex() == refIndex && fSpec.isSecErRefCol() == true) request.setSecErRefCol(required);
		if(fSpec.getMbrColIndex() == refIndex && fSpec.isMbrCol() == true) request.setMbrCol(required);

//		if(fSpec.getSecFNameColIndex() == refIndex && fSpec.isSecFNameCol() == true) request.setSecFNameCol(required);
//		if(fSpec.getSecMNameColIndex() == refIndex && fSpec.isSecMNameCol() == true) request.setSecMNameCol(required);
//		if(fSpec.getSecLNameColIndex() == refIndex && fSpec.isSecLNameCol() == true) request.setSecLNameCol(required);
//		if(fSpec.getSecStreetNumColIndex() == refIndex && fSpec.isSecStreetNumCol() == true) request.setSecStreetNumCol(required);
//		if(fSpec.getSecStreetColIndex() == refIndex && fSpec.isSecStreetCol() == true) request.setSecStreetCol(required);
//		if(fSpec.getSecLin2ColIndex() == refIndex && fSpec.isSecLin2Col() == true) request.setSecLin2Col(required);
//		if(fSpec.getSecCityColIndex() == refIndex && fSpec.isSecCityCol() == true) request.setSecCityCol(required);
//		if(fSpec.getSecStateColIndex() == refIndex && fSpec.isSecStateCol() == true) request.setSecStateCol(required);
//		if(fSpec.getSecZipColIndex() == refIndex && fSpec.isSecZipCol() == true) request.setSecZipCol(required);
//		if(fSpec.getSecDOBColIndex() == refIndex && fSpec.isSecDOBCol() == true) request.setSecDOBCol(required);

		if(fSpec.getCfld1ColIndex() == refIndex && fSpec.isCfld1Col() == true) request.setCfld1Col(required);
		if(fSpec.getCfld2ColIndex() == refIndex && fSpec.isCfld2Col() == true) request.setCfld2Col(required);
		if(fSpec.getCfld3ColIndex() == refIndex && fSpec.isCfld3Col() == true) request.setCfld3Col(required);
		if(fSpec.getCfld4ColIndex() == refIndex && fSpec.isCfld4Col() == true) request.setCfld4Col(required);
		if(fSpec.getCfld5ColIndex() == refIndex && fSpec.isCfld5Col() == true) request.setCfld5Col(required);

//		if(fSpec.getSecCfld1ColIndex() == refIndex && fSpec.isSecCfld1Col() == true) request.setSecCfld1Col(required);
//		if(fSpec.getSecCfld2ColIndex() == refIndex && fSpec.isSecCfld2Col() == true) request.setSecCfld2Col(required);
//		if(fSpec.getSecCfld3ColIndex() == refIndex && fSpec.isSecCfld3Col() == true) request.setSecCfld3Col(required);
//		if(fSpec.getSecCfld4ColIndex() == refIndex && fSpec.isSecCfld4Col() == true) request.setSecCfld4Col(required);
//		if(fSpec.getSecCfld5ColIndex() == refIndex && fSpec.isSecCfld5Col() == true) request.setSecCfld5Col(required);

		if(fSpec.getCovCfld1ColIndex() == refIndex && fSpec.isCovCfld1Col() == true) request.setCovCfld1Col(required);
		if(fSpec.getCovCfld2ColIndex() == refIndex && fSpec.isCovCfld2Col() == true) request.setCovCfld2Col(required);

	}
	
	private void saveFields(DynamicPayFileIgnoreRowSpecification request) {
/*		DynamicPayFileSpecification fSpec = DataManager.i().mDynamicPayFileSpecification;
		request.setSpecification(fSpec);
		request.setName(dfcovNameField.getText());

		boolean required = false;
		int refIndex = 0;
		for (HBoxDetailCell cell : dfcovSelectedSpecView.getItems()) {
			required = cell.getColumn();
			refIndex = cell.getColumnIndex();
			setRequestColValue(fSpec, refIndex, required, request);
		}
*/	}

	private void setRequestColValue(DynamicPayFileSpecification fSpec, int refIndex, boolean required, DynamicPayFileIgnoreRowSpecification request) {
		
		if(fSpec.getErColIndex() == refIndex && fSpec.isErCol() == true) request.setErCol(required);
		if(fSpec.getSsnColIndex() == refIndex && fSpec.isSsnCol() == true) request.setSsnCol(required);
		if(fSpec.getErRefColIndex() == refIndex && fSpec.isErRefCol() == true) request.setErRefCol(required);
		if(fSpec.getfNameColIndex() == refIndex && fSpec.isfNameCol() == true) request.setfNameCol(required);
		if(fSpec.getmNameColIndex() == refIndex && fSpec.ismNameCol() == true) request.setmNameCol(required);
		if(fSpec.getlNameColIndex() == refIndex && fSpec.islNameCol() == true) request.setlNameCol(required);

		if(fSpec.getDobColIndex() == refIndex && fSpec.isDobCol() == true) request.setDobCol(required);
		if(fSpec.getHireDtColIndex() == refIndex && fSpec.isHireDtCol() == true) request.setHireDtCol(required);
		if(fSpec.getRhireDtColIndex() == refIndex && fSpec.isRhireDtCol() == true) request.setRhireDtCol(required);
		if(fSpec.getTermDtColIndex() == refIndex && fSpec.isTermDtCol() == true) request.setTermDtCol(required);
		if(fSpec.getDeptColIndex() == refIndex && fSpec.isDeptCol() == true) request.setDeptCol(required);
		if(fSpec.getLocColIndex() == refIndex && fSpec.isLocCol() == true) request.setLocCol(required);
		if(fSpec.getPpdStartDtColIndex() == refIndex && fSpec.isPpdStartDtCol() == true) request.setPpdStartDtCol(required);
		if(fSpec.getPpdEndDtColIndex() == refIndex && fSpec.isPpdEndDtCol() == true) request.setPpdEndDtCol(required);
		if(fSpec.getPayDtColIndex() == refIndex && fSpec.isPayDtCol() == true) request.setPayDtCol(required);

		if(fSpec.getPpdErRefColIndex() == refIndex && fSpec.isPpdErRefCol() == true) request.setPpdErRefCol(required);
		if(fSpec.getPpdFreqColIndex() == refIndex && fSpec.isPpdFreqCol() == true) request.setPpdFreqCol(required);
		if(fSpec.getHrsColIndex() == refIndex && fSpec.isHrsCol() == true) request.setHrsCol(required);
		if(fSpec.getRateColIndex() == refIndex && fSpec.isRateCol() == true) request.setRateCol(required);
		if(fSpec.getAmtColIndex() == refIndex && fSpec.isAmtCol() == true) request.setAmtCol(required);
		if(fSpec.getPayCodeColIndex() == refIndex && fSpec.isPayCodeCol() == true) request.setPayCodeCol(required);
		if(fSpec.getUnionTypeColIndex() == refIndex && fSpec.isUnionTypeCol() == true) request.setUnionTypeCol(required);
		
		if(fSpec.getCfld1ColIndex() == refIndex && fSpec.isCfld1Col() == true) request.setCfld1Col(required);
		if(fSpec.getCfld2ColIndex() == refIndex && fSpec.isCfld2Col() == true) request.setCfld2Col(required);
		if(fSpec.getCfld3ColIndex() == refIndex && fSpec.isCfld3Col() == true) request.setCfld3Col(required);
		if(fSpec.getCfld4ColIndex() == refIndex && fSpec.isCfld4Col() == true) request.setCfld4Col(required);
		if(fSpec.getCfld5ColIndex() == refIndex && fSpec.isCfld5Col() == true) request.setCfld5Col(required);

		if(fSpec.getStreetNumColIndex() == refIndex && fSpec.isStreetNumCol() == true) request.setStreetNumCol(required);
		if(fSpec.getStreetColIndex() == refIndex && fSpec.isStreetCol() == true) request.setStreetCol(required);
		if(fSpec.getLin2ColIndex() == refIndex && fSpec.isLin2Col() == true) request.setLin2Col(required);
		if(fSpec.getCityColIndex() == refIndex && fSpec.isCityCol() == true) request.setCityCol(required);
		if(fSpec.getStateColIndex() == refIndex && fSpec.isStateCol() == true) request.setStateCol(required);
		if(fSpec.getZipColIndex() == refIndex && fSpec.isZipCol() == true) request.setZipCol(required);
		
		if(fSpec.getOtHrsColIndex() == refIndex && fSpec.isOtHrsCol() == true) request.setOtHrsCol(required);
		if(fSpec.getOtHrsColIndex() == refIndex && fSpec.isOtHrsCol() == true) request.setOtHrs2Col(required);
		if(fSpec.getOtHrs3ColIndex() == refIndex && fSpec.isOtHrs3Col() == true) request.setOtHrs3Col(required);
		if(fSpec.getPtoHrsColIndex() == refIndex && fSpec.isPtoHrsCol() == true) request.setPtoHrsCol(required);
		if(fSpec.getSickHrsColIndex() == refIndex && fSpec.isSickHrsCol() == true) request.setSickHrsCol(required);
		if(fSpec.getHolidayHrsColIndex() == refIndex && fSpec.isHolidayHrsCol() == true) request.setHolidayHrsCol(required);

		if(fSpec.getPayCfld1ColIndex() == refIndex && fSpec.isPayCfld1Col() == true) request.setPayCfld1Col(required);
		if(fSpec.getPayCfld2ColIndex() == refIndex && fSpec.isPayCfld2Col() == true) request.setPayCfld2Col(required);
		if(fSpec.getPayCfld3ColIndex() == refIndex && fSpec.isPayCfld3Col() == true) request.setPayCfld3Col(required);
		if(fSpec.getPayCfld4ColIndex() == refIndex && fSpec.isPayCfld4Col() == true) request.setPayCfld4Col(required);
		if(fSpec.getPayCfld5ColIndex() == refIndex && fSpec.isPayCfld5Col() == true) request.setPayCfld5Col(required);
		if(fSpec.getPayCfld6ColIndex() == refIndex && fSpec.isPayCfld6Col() == true) request.setPayCfld6Col(required);
		if(fSpec.getPayCfld7ColIndex() == refIndex && fSpec.isPayCfld7Col() == true) request.setPayCfld7Col(required);
		
	}

	private boolean getNextListValue() {
		listIndex++;
		HBoxDetailCell cell = dfcovSelectedSpecView.getItems().get(listIndex);
		return cell.getColumn();
		
	}
	
	public void setCoverageFile(int nPipelineSpecificationID) {
		
		DataManager.i().loadDynamicCoverageFileSpecification(nPipelineSpecificationID);
	}
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate we have minimum data
		if(Utils.validate(dfcovNameField) == false)
			return;
		
		changesMade = true;
		updateIgnoreRowSpecification();
		Stage stage = (Stage) dfcovNameField.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	private void onCancel(ActionEvent event) {
		changesMade = false;
		Stage stage = (Stage) dfcovNameField.getScene().getWindow();
		stage.close();
	}	
	
	private void loadAddSpec() {
		setAddMode(true);
		switch(DataManager.i().mPipelineChannel.getType()){
		case COVERAGE:			
			loadFields(DataManager.i().mDynamicCoverageFileSpecification, null);
			break;
		case EMPLOYEE:
			loadFields(DataManager.i().mDynamicEmployeeFileSpecification, null);
			break;
		case PAY:
			loadFields(DataManager.i().mDynamicPayFileSpecification, null);
			break;
		case PAYPERIOD:
		//case PLAN:
		case IRS1094C:
		case IRS1095C:
		case IRSAIRERR:
		case IRSAIRRCPT:
		case EVENT:
			break;
		default:
			break;
		}		
	}
	
	@FXML
	private void onRemoveSpec(ActionEvent event) {
/*		if(addMode == true) return;
		
		switch(DataManager.i().mPipelineChannel.getType()){
		case COVERAGE:
			DynamicCoverageFileIgnoreRowSpecification igSpec = DataManager.i().mDynamicCoverageFileIgnoreRowSpecification;
			saveFields(igSpec);
			RemoveDynamicCoverageFileIgnoreRowSpecificationRequest request = 
					new RemoveDynamicCoverageFileIgnoreRowSpecificationRequest();
			request.setDynamicCoverageFileIgnoreRowSpecification(igSpec);
			request.setDynamicCoverageFileIgnoreRowSpecificationId(igSpec.getId());
			DataManager.i().removeDynamicCoverageFileIgnoreRowSpecification(request);
			break;
		case EMPLOYEE:
			DynamicEmployeeFileIgnoreRowSpecification ieSpec = DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification;
			saveFields(ieSpec);
			RemoveDynamicEmployeeFileIgnoreRowSpecificationRequest eRequest = 
					new RemoveDynamicEmployeeFileIgnoreRowSpecificationRequest();
			eRequest.setDynamicEmployeeFileIgnoreRowSpecification(ieSpec);
			eRequest.setDynamicEmployeeFileIgnoreRowSpecificationId(ieSpec.getId());
			DataManager.i().removeDynamicEmployeeFileIgnoreRowSpecification(eRequest);
			break;
		case PAY:
			DynamicPayFileIgnoreRowSpecification ipSpec = DataManager.i().mDynamicPayFileIgnoreRowSpecification;
			saveFields(ipSpec);
			RemoveDynamicPayFileIgnoreRowSpecificationRequest pRequest = 
					new RemoveDynamicPayFileIgnoreRowSpecificationRequest();
			pRequest.setDynamicPayFileIgnoreRowSpecification(ipSpec);
			pRequest.setDynamicPayFileIgnoreRowSpecificationId(ipSpec.getId());
			DataManager.i().removeDynamicPayFileIgnoreRowSpecification(pRequest);
			break;
		case PAYPERIOD:
		case PLAN:
		case IRS1094C:
		case IRS1095C:
		case IRSAIRERR:
		case IRSAIRRCPT:
		case EVENT:
			break;
		default:
			break;
		}	
		
		// mark it as changed and return
		changesMade = true;
		Stage stage = (Stage) dfcovNameField.getScene().getWindow();
		stage.close();
*/ }
	
	@FXML
	private void onClose(ActionEvent event) {
		Stage stage = (Stage) dfcovNameField.getScene().getWindow();
		stage.close();
	}	
	
	public String getIndexString(int index) {
	    String letters;
		char c = 'A';
	    char c1 = 'A';
	    
    	if(alphaId < 26) {
    		c += alphaId;
    		letters =  String.valueOf(c) + index;
    	}
    	else {
    		int letterPos = alphaId/26;
    		c1 += letterPos;
    		//c1 += (letterPos - 1 + alphaId); // starting with A
    		letters = String.valueOf(c1) + String.valueOf(c + alphaId) + index;
    	}
	   
    	alphaId++;
    	return letters;
	}
	
	public class HBoxDetailCell extends HBox {
        boolean headerCell;
  
         // first column
        TextField nameField = new TextField();
        Label nameLabel = new Label();
               
        // second column
        Label columnIndexLabel = new Label();
         
        // third column
        Label requiredLabel = new Label();
        CheckBox requiredCheck = new CheckBox();
        
        public boolean setValue(String label, boolean value) 
        {
        	if (nameLabel.getText() != null && label != null && nameLabel.getText().contains(label)) {
        		requiredCheck.setSelected(value);
        		return true;
        	}else
        		return false;
        }
        
        //default constructor is a header row
        HBoxDetailCell() {
            super();
            if(DataManager.i().isWindows() == true) {
            	nameLabel.setMinWidth(200);
            	nameLabel.setPrefWidth(200);
            	columnIndexLabel.setMinWidth(100);
            	columnIndexLabel.setPrefWidth(100);
            	requiredLabel.setMinWidth(100);
            	requiredLabel.setPrefWidth(100);
            }else {
            	nameLabel.setMinWidth(200);
            	nameLabel.setPrefWidth(200);
            	columnIndexLabel.setMinWidth(100);
            	columnIndexLabel.setPrefWidth(100);
            	requiredLabel.setMinWidth(100);
            	requiredLabel.setPrefWidth(100);
            }

            nameLabel.setText("Name"); 
            nameLabel.setFont(Font.font(null, FontWeight.BOLD, 13));
       	    nameLabel.setAlignment(Pos.CENTER);
       	    columnIndexLabel.setText("Col"); 
       	    columnIndexLabel.setFont(Font.font(null, FontWeight.BOLD, 13));
       	    columnIndexLabel.setAlignment(Pos.CENTER);
       	    requiredLabel.setText("Required"); 
       	    requiredLabel.setFont(Font.font(null, FontWeight.BOLD, 13));
       	    requiredLabel.setAlignment(Pos.CENTER_LEFT);
       	    headerCell = true; 
 
       	    this.getChildren().addAll(nameLabel, columnIndexLabel, requiredLabel);
        }
        
        // header
        HBoxDetailCell(String header) {
            super();

            nameLabel.setText(header); 
            nameLabel.setMinWidth(695);
            nameLabel.setPrefWidth(695);
            nameLabel.setFont(Font.font(null, FontWeight.BOLD, 13));
            nameLabel.setAlignment(Pos.CENTER);
       	    headerCell = true;
       	    this.getChildren().addAll(nameLabel);
        }
        
        HBoxDetailCell(String name, boolean column, String columnIndex, boolean required) {
             super();
             
             //set up the separators
             headerCell = false;
 
             if(DataManager.i().isWindows() == true) {
                 nameField.setMinWidth(200);
                 nameField.setPrefWidth(200);
                 nameLabel.setMinWidth(200);
                 nameLabel.setPrefWidth(200);
                 columnIndexLabel.setMinWidth(100);
                 columnIndexLabel.setPrefWidth(100);
                 requiredCheck.setMinWidth(100);
                 requiredCheck.setPrefWidth(100);
             }else {
                 nameField.setMinWidth(200);
                 nameField.setPrefWidth(200);
                 nameLabel.setMinWidth(200);
                 nameLabel.setPrefWidth(200);
                 columnIndexLabel.setMinWidth(100);
                 columnIndexLabel.setPrefWidth(100);
                 requiredCheck.setMinWidth(100);
                 requiredCheck.setPrefWidth(100);
             }
                         
             if(name == "" || name == null)
            	 nameField.setPromptText("Custom Field");
             else {
            	 // strip out the patterns and formats
            	 if(name.contains(" (")) {
            		 int index = name.indexOf(" (");
            		 name = name.substring(0, index);
            	 }
            	 nameField.setText(name);
             }
             nameLabel.setText(name);

             // set the column if selected in the ignorerow object
             requiredCheck.setSelected(required);
             
        	 requiredCheck.setAlignment(Pos.CENTER);
        	 columnIndexLabel.setAlignment(Pos.CENTER);
        	 
        	 // set the controls according to the view's readonly member
             if(column == false) {
            	 requiredCheck.setDisable(true);
	             nameField.setEditable(false);
            	 nameLabel.setFont(Font.font(null, FontWeight.NORMAL, 13));
            	 columnIndexLabel.setFont(Font.font(null, FontWeight.NORMAL, 13));
      	   	    this.getChildren().addAll(nameLabel);
             } else {
            	 nameLabel.setFont(Font.font(null, FontWeight.BOLD, 13));
            	 columnIndexLabel.setText(columnIndex);
            	 columnIndexLabel.setFont(Font.font(null, FontWeight.BOLD, 13));
            	 requiredCheck.setDisable(false);
            	 if(nameLabel.getText().trim().length() == 0)
           	   	    this.getChildren().addAll(nameLabel, columnIndexLabel);
            	 else
      	   	    	this.getChildren().addAll(nameLabel, columnIndexLabel, requiredCheck);
             }
        }
        
        public boolean isHeader() {
        	return headerCell;
        }
        
        public void clearCell() {
        	requiredCheck.setSelected(false);
        }
        
        public String getName() {
        	return nameField.getText();
        }
        
        public Boolean getColumn() {
        	return requiredCheck.isSelected(); 
        }
        
        public int getColumnIndex() {
        	if(columnIndexLabel.getText().length() < 1 ) return 0;
        	String numString = columnIndexLabel.getText().replaceAll("[^\\d.]", "");
        	return Integer.valueOf(numString);
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
             
             if(covSpec != null) {
            	 lblCol1.setText(Utils.getDateString(covSpec.getLastUpdated()));
            	 //lblCol2.setText(covSpec.getName());
            	 this.covSpec = covSpec;
             }
          
             if(empSpec != null) {
            	 lblCol1.setText(Utils.getDateString(empSpec.getLastUpdated()));
            	 lblCol2.setText(empSpec.getName());
            	 this.empSpec = empSpec;
             }
          
             if(paySpec != null) {
            	 lblCol1.setText(Utils.getDateString(paySpec.getLastUpdated()));
            	 //lblCol2.setText(paySpec.getName());
            	 this.paySpec = paySpec;
             }
          
             lblCol1.setMinWidth(100);
             lblCol1.setMaxWidth(100);
             lblCol1.setPrefWidth(100);
             HBox.setHgrow(lblCol1, Priority.ALWAYS);

             lblCol2.setMinWidth(275);
             lblCol2.setMaxWidth(275);
             lblCol2.setPrefWidth(275);
             HBox.setHgrow(lblCol2, Priority.ALWAYS);

             this.getChildren().addAll(lblCol1, lblCol2);
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
}


