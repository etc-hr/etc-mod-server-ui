package com.etc.admin.ui.pipeline.payperiodfile;

import java.util.ArrayList;
import java.util.List;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.ppd.DynamicPayPeriodFileSpecification;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewDynamicPayPeriodFileSpecificationController {
	
	@FXML
	private TableView<ColumnField> dfppColumnFieldTable;
	@FXML
	private TextField dfppNameLabel;
	@FXML
	private TextField dfppDescriptionLabel;
	@FXML
	private TextField dfppTabIndexLabel;
	@FXML
	private TextField dfppHeaderRowIndexLabel;
	@FXML
	private ComboBox<String> dfppDynamicPayPeriodFileSpecificationCombo;
	@FXML
	private Label dfppCoreIdLabel;
	@FXML
	private Label dfppCoreActiveLabel;
	@FXML
	private Label dfppCoreBODateLabel;
	@FXML
	private Label dfppCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		loadColumnFields();
		
		//test
		//updatePayPeriodFileData();
				
	}

	private void initControls() {

	    
 		TableColumn<ColumnField,String> nameColumn = new TableColumn<>("Name");
 		nameColumn.setMinWidth(300);
		nameColumn.setCellValueFactory(new PropertyValueFactory<ColumnField,String>("name"));

		TableColumn<ColumnField,String> columnColumn = new TableColumn<ColumnField,String>("Column");
		columnColumn.setMinWidth(100);
		columnColumn.setCellValueFactory(new PropertyValueFactory<ColumnField,String>("column"));

		TableColumn<ColumnField,String> columnIndexColumn = new TableColumn<ColumnField,String>("Column Index");
		columnIndexColumn.setMinWidth(150);
		columnIndexColumn.setCellValueFactory(new PropertyValueFactory<ColumnField,String>("columnIndex"));

		TableColumn<ColumnField,String> parsePatternColumn = new TableColumn<ColumnField,String>("Parse Pattern");
		parsePatternColumn.setMinWidth(150);
		parsePatternColumn.setCellValueFactory(new PropertyValueFactory<ColumnField,String>("parsePattern"));
		
		dfppColumnFieldTable.getColumns().clear();
		dfppColumnFieldTable.getColumns().add(nameColumn); 
		dfppColumnFieldTable.getColumns().add(columnColumn);
		dfppColumnFieldTable.getColumns().add(columnIndexColumn);
		dfppColumnFieldTable.getColumns().add(parsePatternColumn);		
	}	
	
	private void loadColumnFields() {
		//add some data
		List<ColumnField> list = new ArrayList<ColumnField>();	
		
		list.add(new ColumnField("START DATE"," ", " ", " "));
		list.add(new ColumnField("END DATE"," ", " ", " "));
		list.add(new ColumnField("PAY DATE"," ", " ", " "));
		list.add(new ColumnField("PAY FREQUENCY TYPE"," ", " ", " "));
		list.add(new ColumnField("CUSTOM EE FIELD 1"," ", " ", " "));
		list.add(new ColumnField("CUSTOM EE FIELD 2"," ", " ", " "));
		list.add(new ColumnField("CUSTOM EE FIELD 3"," ", " ", " "));
		list.add(new ColumnField("CUSTOM EE FIELD 4"," ", " ", " "));
		list.add(new ColumnField("CUSTOM EE FIELD 5"," ", " ", " "));
		
        ObservableList<ColumnField> myObservableList = FXCollections.observableList(list);
        dfppColumnFieldTable.setItems(myObservableList);
	}

	private void updatePayPeriodFileData(){

		DynamicPayPeriodFileSpecification fSpec = DataManager.i().mDynamicPayPeriodFileSpecification;
		if (fSpec != null) {
			Utils.updateControl(dfppNameLabel,fSpec.getName());
			Utils.updateControl(dfppTabIndexLabel,String.valueOf(fSpec.getTabIndex()));
			Utils.updateControl(dfppHeaderRowIndexLabel,String.valueOf(fSpec.getHeaderRowIndex()));
				
			//core data read only
			Utils.updateControl(dfppCoreIdLabel,String.valueOf(fSpec.getId()));
			Utils.updateControl(dfppCoreActiveLabel,String.valueOf(fSpec.isActive()));
			Utils.updateControl(dfppCoreBODateLabel,fSpec.getBornOn());
			Utils.updateControl(dfppCoreLastUpdatedLabel,fSpec.getLastUpdated());
			
			//update the column field table
			loadFields(fSpec);	        
		}
	}
	
	private void loadFields(DynamicPayPeriodFileSpecification fSpec) {

		List<ColumnField> list = new ArrayList<ColumnField>();	
		
		list.add(new ColumnField("PAY PERIOD IDENTIFIER", fSpec.isErRefCol(), fSpec.getErRefColIndex(), ""));
		//list.add(new ColumnField("START DATE", fSpec.isStartDtCol(), fSpec.getS, ""));
		//list.add(new ColumnField("END DATE", fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), ""));
		//list.add(new ColumnField("PAY DATE", fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), ""));
		//list.add(new ColumnField("PAY FREQUENCY TYPE", fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), ""));

		list.add(new ColumnField(fSpec.getCfld1Name(),fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), fSpec.getCfld1ParsePattern().getName()));
		list.add(new ColumnField(fSpec.getCfld2Name(),fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), fSpec.getCfld2ParsePattern().getName()));
		list.add(new ColumnField(fSpec.getCfld3Name(),fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), ""));
		list.add(new ColumnField(fSpec.getCfld4Name(),fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), ""));
		list.add(new ColumnField(fSpec.getCfld5Name(),fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), ""));
		
		ObservableList<ColumnField> myObservableList = FXCollections.observableList(list);
        dfppColumnFieldTable.setItems(myObservableList);
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
	public void onSearchHide() {
		// if we already have an account selected, show it
		if (dfppDynamicPayPeriodFileSpecificationCombo.getValue() != "")
			searchDynamicPayPeriodFileSpecifications();
	}
	
	
	
	public void onSearchDynamicPayPeriodFileSpecifications(ActionEvent event) {
		searchDynamicPayPeriodFileSpecifications();
	}

	public void searchDynamicPayPeriodFileSpecifications() {
		
/*		if (DataManager.i().mDynamicPayPeriodFileSpecifications == null) return;
		
		String sSelection = dfppDynamicPayPeriodFileSpecificationCombo.getValue();
		for (int i = 0; i < DataManager.i().mDynamicPlanFileSpecifications.size();i++) {
			if (DataManager.i().mDynamicPlanFileSpecifications.get(i).getName().equals(sSelection)) {			
				//load the user
				setPayPeriodFile(i);
				break;
			}
		} */
	}	
		
	public void setPayPeriodFile(int nFileID) {
		
		if (DataManager.i().mUsers != null){
			DataManager.i().setDynamicPayPeriodFileSpecification(nFileID);
			updatePayPeriodFileData();
		}
	}
	
}


