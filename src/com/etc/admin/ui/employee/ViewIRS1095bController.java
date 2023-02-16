package com.etc.admin.ui.employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.Dependent;
import com.etc.corvetto.entities.Irs1095b;
import com.etc.corvetto.entities.Irs1095bCI;
import com.etc.corvetto.rqs.Irs1095bCIRequest;
import com.etc.entities.CoreData;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewIRS1095bController
{
//	@FXML
//	private ListView<HBoxCI1095bCell> dependentList;
	@FXML
	private TableView<HBoxDependentCell> dependentList;
	@FXML
	private CheckBox selfInsuredCheck;
	@FXML
	private CheckBox fileCIwithSSNCheck;
	@FXML
	private TextField originTypeField;
	@FXML
	private Label yearLabel;
	@FXML
	private Label empNamelbl;
	@FXML
	private Label emprNamelbl;
	@FXML
	private Label inactiveLabel;
	// months
	@FXML
	private CheckBox coverageJanCheck;
	@FXML
	private CheckBox coverageFebCheck;
	@FXML
	private CheckBox coverageMarCheck;
	@FXML
	private CheckBox coverageAprCheck;
	@FXML
	private CheckBox coverageMayCheck;
	@FXML
	private CheckBox coverageJunCheck;
	@FXML
	private CheckBox coverageJulCheck;
	@FXML
	private CheckBox coverageAugCheck;
	@FXML
	private CheckBox coverageSepCheck;
	@FXML
	private CheckBox coverageOctCheck;
	@FXML
	private CheckBox coverageNovCheck;
	@FXML
	private CheckBox coverageDecCheck;
	// buttons
	@FXML
	private Button DeactivateButton;
	@FXML
	private Button SaveButton;
	@FXML
	private Button prevButton;
	@FXML
	private Button nextButton;

	public boolean changesMade = false;
	Irs1095b current1095b = null;
	public Irs1095bCI irs1095bCI = null;
	int currentDataPosition = 0;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize()
	{
		initControls();
		setTableColumns();
		show1095b();
	}
	
	private void initControls() 
	{
		currentDataPosition = 0;
		// sort our data collection
		Collections.sort(DataManager.i().mIrs1095bs, (o1, o2) -> (Integer.compare(o1.getTaxYear().getYear(), o2.getTaxYear().getYear()))); 
		// set it to the most recent
		currentDataPosition = DataManager.i().mIrs1095bs.size() - 1;
		nextButton.setDisable(true);
		// if we are at 0, there is nothing to navigate through
		if (currentDataPosition == 0)
			prevButton.setDisable(true);
	}
	
	public void load() {

	}
	
	private void setTableColumns() 
	{
		//clear the default values
		dependentList.getColumns().clear();

	    TableColumn<HBoxDependentCell, String> x1 = new TableColumn<HBoxDependentCell, String>("First Name");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,String>("firstName"));
		x1.setMinWidth(124);
		dependentList.getColumns().add(x1);
		setDependentCellFactory(x1);

	    TableColumn<HBoxDependentCell, String> x2 = new TableColumn<HBoxDependentCell, String>("Last Name");
	    x2.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,String>("lastName"));
	    x2.setMinWidth(124);
		dependentList.getColumns().add(x2);
		setDependentCellFactory(x2);

	    TableColumn<HBoxDependentCell, String> x3 = new TableColumn<HBoxDependentCell, String>("SSN");
	    x3.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,String>("ssnNum"));
	    x3.setMinWidth(100);
		dependentList.getColumns().add(x3);
		setDependentCellFactory(x3);

	    TableColumn<HBoxDependentCell, String> x4 = new TableColumn<HBoxDependentCell, String>("DOB");
	    x4.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,String>("dobNum"));
	    x4.setMinWidth(100);
		dependentList.getColumns().add(x4);
		setDependentCellFactory(x4);

//		CheckBoxTableCell<HBoxDependentCell,Boolean> cell = new CheckBoxTableCell<HBoxDependentCell,Boolean>();
		TableColumn<HBoxDependentCell,Boolean> janCol = new TableColumn<HBoxDependentCell,Boolean>("Jan");
		janCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("janCheck"));
//		janCol.setCellFactory(new Callback<TableColumn<HBoxDependentCell,Boolean>,TableCell<HBoxDependentCell,Boolean>>() {
//		    @Override 
//		    public TableCell<HBoxDependentCell,Boolean> call( TableColumn<HBoxDependentCell,Boolean> p ) {
//		        CheckBoxTreeTableCell<HBoxDependentCell,Boolean> cell = new CheckBoxTreeTableCell<HBoxDependentCell,Boolean>();
//		        cell.setAlignment(Pos.CENTER);
//		        return cell;
//		    }
//		});
//        cell.setAlignment(Pos.CENTER);
//		janCol.setCellFactory(CheckBoxTableCell.forTableColumn(janCol));
//		janCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()));
		janCol.setMinWidth(40);
		dependentList.getColumns().add(janCol);
//		setDependentCheckCellFactory(janCol);

		TableColumn<HBoxDependentCell,Boolean> febCol = new TableColumn<HBoxDependentCell,Boolean>("Feb");
		febCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("febCheck"));
//		febCol.setCellFactory(CheckBoxTableCell.forTableColumn(febCol));
		febCol.setMinWidth(40);
		dependentList.getColumns().add(febCol);

		TableColumn<HBoxDependentCell,Boolean> marCol = new TableColumn<HBoxDependentCell,Boolean>("Mar");
		marCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("marCheck"));
//		marCol.setCellFactory(CheckBoxTableCell.forTableColumn(marCol));
		marCol.setMinWidth(40);
		dependentList.getColumns().add(marCol);

		TableColumn<HBoxDependentCell,Boolean> aprCol = new TableColumn<HBoxDependentCell,Boolean>("Apr");
		aprCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("aprCheck"));
//		aprCol.setCellFactory(CheckBoxTableCell.forTableColumn(aprCol));
		aprCol.setMinWidth(40);
		dependentList.getColumns().add(aprCol);

		TableColumn<HBoxDependentCell,Boolean> mayCol = new TableColumn<HBoxDependentCell,Boolean>("May");
		mayCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("mayCheck"));
//		mayCol.setCellFactory(CheckBoxTableCell.forTableColumn(mayCol));
		mayCol.setMinWidth(40);
		dependentList.getColumns().add(mayCol);

		TableColumn<HBoxDependentCell,Boolean> junCol = new TableColumn<HBoxDependentCell,Boolean>("Jun");
		junCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("junCheck"));
//		junCol.setCellFactory(CheckBoxTableCell.forTableColumn(junCol));
		junCol.setMinWidth(40);
		dependentList.getColumns().add(junCol);

		TableColumn<HBoxDependentCell,Boolean> julCol = new TableColumn<HBoxDependentCell,Boolean>("Jul");
		julCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("julCheck"));
//		julCol.setCellFactory(CheckBoxTableCell.forTableColumn(julCol));
		julCol.setMinWidth(40);
		dependentList.getColumns().add(julCol);

		TableColumn<HBoxDependentCell,Boolean> augCol = new TableColumn<HBoxDependentCell,Boolean>("Aug");
		augCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("augCheck"));
//		augCol.setCellFactory(CheckBoxTableCell.forTableColumn(augCol));
		augCol.setMinWidth(40);
		dependentList.getColumns().add(augCol);

		TableColumn<HBoxDependentCell,Boolean> sepCol = new TableColumn<HBoxDependentCell,Boolean>("Sep");
		sepCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("sepCheck"));
//		sepCol.setCellFactory(CheckBoxTableCell.forTableColumn(sepCol));
		sepCol.setMinWidth(40);
		dependentList.getColumns().add(sepCol);

		TableColumn<HBoxDependentCell,Boolean> octCol = new TableColumn<HBoxDependentCell,Boolean>("Oct");
		octCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("octCheck"));
//		octCol.setCellFactory(CheckBoxTableCell.forTableColumn(octCol));
		octCol.setMinWidth(40);
		dependentList.getColumns().add(octCol);

		TableColumn<HBoxDependentCell,Boolean> novCol = new TableColumn<HBoxDependentCell,Boolean>("Nov");
		novCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("novCheck"));
//		novCol.setCellFactory(CheckBoxTableCell.forTableColumn(novCol));
		novCol.setMinWidth(40);
		dependentList.getColumns().add(novCol);

		TableColumn<HBoxDependentCell,Boolean> decCol = new TableColumn<HBoxDependentCell,Boolean>("Dec");
		decCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("decCheck"));
//		decCol.setCellFactory(CheckBoxTableCell.forTableColumn(decCol));
		decCol.setMinWidth(40);
		dependentList.getColumns().add(decCol);

	    TableColumn<HBoxDependentCell, String> x5 = new TableColumn<HBoxDependentCell, String>("DEP ID");
	    x5.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,String>("depId"));
	    x5.setMinWidth(75);
		dependentList.getColumns().add(x5);
		setDependentCellFactory(x5);

		TableColumn<HBoxDependentCell,Boolean> activeCol = new TableColumn<HBoxDependentCell,Boolean>("Active");
		activeCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("activeCheck"));
//		active.setCellFactory(CheckBoxTableCell.forTableColumn(active));
		activeCol.setMinWidth(40);
		dependentList.getColumns().add(activeCol);
	}
	
	private void setDependentCellFactory(TableColumn<HBoxDependentCell, String>  col) 
	{
		col.setCellFactory(column -> 
		{
		    return new TableCell<HBoxDependentCell, String>() 
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
		                HBoxDependentCell cell = getTableView().getItems().get(getIndex());

		                if(cell.getIrs1095cCI().getDependent().isDeleted() == true)
		                	setTextFill(Color.RED);
		                else {
			                if(cell.getIrs1095cCI().getDependent().isActive() == false && cell.getIrs1095cCI().getDependent().isDeleted() == false)
			                	setTextFill(Color.BLUE);
			                else
			                	setTextFill(Color.BLACK);
		                }
		            }
		        }
		    };
		});
	}

	private void show1095b() 
	{
		if (DataManager.i().mIrs1095bs == null || DataManager.i().mIrs1095bs.size() == 0)
		{
			nextButton.setDisable(true);
			return;
		}
		
		current1095b = DataManager.i().mIrs1095bs.get(currentDataPosition);
		if(current1095b != null) 
		{
			// inactive label
			inactiveLabel.setVisible(false);
			if (current1095b.isActive() == false) 
			{
				inactiveLabel.setVisible(true);
				inactiveLabel.setText("INACTIVE");
				inactiveLabel.setTextFill(Color.BLUE);
			}
				
			if (current1095b.isDeleted() == true) 
			{
				inactiveLabel.setVisible(true);
				inactiveLabel.setText("DELETED");
				inactiveLabel.setTextFill(Color.RED);
			}
				
			// top label
			empNamelbl.setText(current1095b.getEmployee().getPerson().getLastName().toUpperCase() + ", " + current1095b.getEmployee().getPerson().getFirstName().toUpperCase());
			emprNamelbl.setText(current1095b.getEmployee().getEmployer().getName());
			yearLabel.setText(String.valueOf(current1095b.getTaxYear().getYear()));

			Utils.updateControl(selfInsuredCheck, current1095b.isSelfInsured());
			if (current1095b.getHealthCoverageOriginType() != null)
				Utils.updateControl(originTypeField, current1095b.getHealthCoverageOriginType().toString());
			else
				Utils.updateControl(originTypeField, "");
			Utils.updateControl(coverageJanCheck, current1095b.isJanCovered());
			Utils.updateControl(coverageFebCheck, current1095b.isFebCovered());
			Utils.updateControl(coverageMarCheck, current1095b.isMarCovered());
			Utils.updateControl(coverageAprCheck, current1095b.isAprCovered());
			Utils.updateControl(coverageMayCheck, current1095b.isMayCovered());
			Utils.updateControl(coverageJunCheck, current1095b.isJunCovered());
			Utils.updateControl(coverageJulCheck, current1095b.isJulCovered());
			Utils.updateControl(coverageAugCheck, current1095b.isAugCovered());
			Utils.updateControl(coverageSepCheck, current1095b.isSepCovered());
			Utils.updateControl(coverageOctCheck, current1095b.isOctCovered());
			Utils.updateControl(coverageNovCheck, current1095b.isNovCovered());
			Utils.updateControl(coverageDecCheck, current1095b.isDecCovered());
			showDependents();
		}
	}

	private void showDependents() 
	{
		try {
			// clear anything already in the list
			dependentList.getItems().clear();
	
			//if we have no employees, bail
			if(DataManager.i().mEmployee.getPerson() == null) return;
			if(DataManager.i().mEmployee.getPerson() == null || DataManager.i().mEmployee.getPerson().getDependents() == null) return;

			List<HBoxDependentCell> list = new ArrayList<>();
			
			for(Dependent dep : DataManager.i().mEmployee.getPerson().getDependents()) 
			{
		    	if(dep.isActive() == false) continue;
		    	if(dep.isDeleted() == true) continue;

		    	Irs1095bCIRequest irsReq = new Irs1095bCIRequest();
				irsReq.setIrs1095bId(current1095b.getId());
				irsReq.setDependentId(dep.getId());
				current1095b.setIrs1095bCIs(AdminPersistenceManager.getInstance().getAll(irsReq));

		    	if(current1095b.getIrs1095bCIs().size() > 0)
		    	{
					for(Irs1095bCI ci : current1095b.getIrs1095bCIs()) 
					{
						if(ci.getDependent().equals(dep))
						{
							list.add(new HBoxDependentCell(ci, dep));
						}
					}
		    	}
			}
	        ObservableList<HBoxDependentCell> myObservableList = FXCollections.observableList(list);
	        dependentList.setItems(myObservableList);

		} catch (CoreException e) 
		{ DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) 
		{ DataManager.i().logGenericException(e); }

		//redraw the control
        dependentList.refresh();
		EtcAdmin.i().setProgress(0); 
	}

	private void updateIrs1095b() 
	{
		DataManager.i().mIrs1095b.setSelfInsured(selfInsuredCheck.isSelected());
		DataManager.i().mIrs1095b.setJanCovered(coverageJanCheck.isSelected());
		DataManager.i().mIrs1095b.setFebCovered(coverageFebCheck.isSelected());
		DataManager.i().mIrs1095b.setMarCovered(coverageMarCheck.isSelected());
		DataManager.i().mIrs1095b.setAprCovered(coverageAprCheck.isSelected());
		DataManager.i().mIrs1095b.setMayCovered(coverageMayCheck.isSelected());
		DataManager.i().mIrs1095b.setJunCovered(coverageJunCheck.isSelected());
		DataManager.i().mIrs1095b.setJulCovered(coverageJulCheck.isSelected());
		DataManager.i().mIrs1095b.setAugCovered(coverageAugCheck.isSelected());
		DataManager.i().mIrs1095b.setSepCovered(coverageSepCheck.isSelected());
		DataManager.i().mIrs1095b.setOctCovered(coverageOctCheck.isSelected());
		DataManager.i().mIrs1095b.setNovCovered(coverageNovCheck.isSelected());
		DataManager.i().mIrs1095b.setDecCovered(coverageDecCheck.isSelected());
	}
	
	private boolean validateFields() {
		return true;
	}
	
	@FXML
	private void onShowSystemInfo() 
	{
		try {
			// set the coredata
			DataManager.i().mIrs1095b = current1095b;
			DataManager.i().mCoreData = (CoreData) current1095b;
			DataManager.i().mCurrentCoreDataType = SystemDataType.IRS1095B;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
		
	}
	
	@FXML
	private void onSave(ActionEvent event) 
	{
		if (validateFields() == false)
				return;
		
		// update
		updateIrs1095b();
		//mark changes made
		changesMade = true;
		// and exit the pop up
		exitPopup();
	}
	
	@FXML
	private void onPrevButton(ActionEvent event) 
	{
		currentDataPosition--;
		nextButton.setDisable(false);
		if (currentDataPosition < 0) currentDataPosition = 0;
		if (currentDataPosition == 0)
			prevButton.setDisable(true);
		show1095b();
	}
		
	@FXML
	private void onNextButton(ActionEvent event) 
	{
		currentDataPosition++;
		prevButton.setDisable(false);
		if (currentDataPosition + 1 == DataManager.i().mIrs1095bs.size()) {
			nextButton.setDisable(true);
		}
		show1095b();
	}	

	private void exitPopup() 
	{
		changesMade = true;
		Stage stage = (Stage) coverageJanCheck.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		exitPopup();
	}	

	public class HBoxDependentCell extends HBox 
	{
		SimpleStringProperty firstName = new SimpleStringProperty();
		SimpleStringProperty lastName = new SimpleStringProperty();
		SimpleStringProperty ssnNum = new SimpleStringProperty();
		SimpleStringProperty dobNum = new SimpleStringProperty();
		CheckBox janCheck = new CheckBox();
		CheckBox febCheck = new CheckBox();
		CheckBox marCheck = new CheckBox();
		CheckBox aprCheck = new CheckBox();
		CheckBox mayCheck = new CheckBox();
		CheckBox junCheck = new CheckBox();
		CheckBox julCheck = new CheckBox();
		CheckBox augCheck = new CheckBox();
		CheckBox sepCheck = new CheckBox();
		CheckBox octCheck = new CheckBox();
		CheckBox novCheck = new CheckBox();
		CheckBox decCheck = new CheckBox();
		SimpleStringProperty depId = new SimpleStringProperty();
		CheckBox activeCheck = new CheckBox();

		Irs1095bCI irs1095bCi;
		Dependent depdnt;

		public String getFirstName() { return firstName.get(); }
		public String getLastName() { return lastName.get(); }
		public String getSsnNum() { return ssnNum.get(); }
		public String getDobNum() { return dobNum.get(); }
		public CheckBox getJanCheck() { return janCheck; }
		public CheckBox getFebCheck() { return febCheck; }
		public CheckBox getMarCheck() { return marCheck; }
		public CheckBox getAprCheck() { return aprCheck; }
		public CheckBox getMayCheck() { return mayCheck; }
		public CheckBox getJunCheck() { return junCheck; }
		public CheckBox getJulCheck() { return julCheck; }
		public CheckBox getAugCheck() { return augCheck; }
		public CheckBox getSepCheck() { return sepCheck; }
		public CheckBox getOctCheck() { return octCheck; }
		public CheckBox getNovCheck() { return novCheck; }
		public CheckBox getDecCheck() { return decCheck; }
		public String getDepId() { return depId.get(); }
		public CheckBox getActiveCheck() { return activeCheck; }

		public Irs1095bCI getIrs1095cCI() { return irs1095bCi; }
		public Dependent getDependent() { return depdnt; }

		HBoxDependentCell(Irs1095bCI irs1095bCI, Dependent dependent) 
		{
			super();
			irs1095bCi = irs1095bCI;
			depdnt = dependent;

			firstName.set(dependent.getPerson().getFirstName());
			lastName.set(dependent.getPerson().getLastName());

			if(dependent.getPerson().getSsn() != null)
				ssnNum.set("XXX-XX-" + dependent.getPerson().getSsn().getUsrln().toString());
			else
				ssnNum.set("N/A");
			if(dependent.getPerson().getDateOfBirth() != null)
				dobNum.set(Utils.getDateString(dependent.getPerson().getDateOfBirth()));
			else
				dobNum.set("N/A");
			janCheck.setSelected(irs1095bCI.isJanCovered());
			febCheck.setSelected(irs1095bCI.isFebCovered());
			marCheck.setSelected(irs1095bCI.isMarCovered());
			aprCheck.setSelected(irs1095bCI.isAprCovered());
			mayCheck.setSelected(irs1095bCI.isMayCovered());
			junCheck.setSelected(irs1095bCI.isJunCovered());
			julCheck.setSelected(irs1095bCI.isJulCovered());
			augCheck.setSelected(irs1095bCI.isAugCovered());
			sepCheck.setSelected(irs1095bCI.isSepCovered());
			octCheck.setSelected(irs1095bCI.isOctCovered());
			novCheck.setSelected(irs1095bCI.isNovCovered());
			decCheck.setSelected(irs1095bCI.isDecCovered());
			depId.set(dependent.getId().toString());
			activeCheck.setSelected(irs1095bCI.isActive());
		}
	}
}


