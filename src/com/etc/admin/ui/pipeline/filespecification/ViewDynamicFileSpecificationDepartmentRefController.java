package com.etc.admin.ui.pipeline.filespecification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileDepartmentReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileDepartmentReference;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileDepartmentReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileDepartmentReferenceRequest;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;
import com.etc.corvetto.entities.Department;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.rqs.DepartmentRequest;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewDynamicFileSpecificationDepartmentRefController 
{
	@FXML
	private Label dsrefTopLabel;
	@FXML
	private Button dsrefAddButton;
	@FXML
	private Button dsrefRemoveButton;
	@FXML
	private Button dsrefSaveButton;
	@FXML
	private Button dsrefAddEmployerDepartmentRefButton;
	@FXML
	private Button dsrefRemoveEmployerDepartmentRefButton;
	@FXML
	private TableView<HBoxDepartmentCell> dsrefSelectedTableView;
	@FXML
	private ListView<ChoiceCell> dsrefChoicesListView;
	@FXML
	private TextField dsrefReferenceField;
	@FXML
	private TextField dsrefAddDepartmentField;
	@FXML
	private Label dfcovEmployerFilterLabel;
	@FXML
	private Label dsrefEmployerLabel;
	@FXML
	private TextField dsrefEmployerFilterField;
	@FXML
	private ComboBox<String> dsRefEmployerCombo;
	
	private PipelineFileType fileType;
	public Boolean changesMade = false;
	/**
	 * initialize is called when the FXML is loaded
	 */
	//local employer selection
	Employer employer = null;
	Department department = null;
	List<Long> currentEmployerListIds = new ArrayList<Long>();

	@FXML
	public void initialize() 
	{
		// disable the selection-dependent buttons
		dsrefRemoveButton.setDisable(true);
		dsrefRemoveEmployerDepartmentRefButton.setDisable(true);
		dsrefReferenceField.setText("");
		dsrefReferenceField.setPromptText("Source Value (required)");
		dsrefAddDepartmentField.setDisable(true);
		dsrefAddDepartmentField.setText("");
		dsrefAddDepartmentField.setPromptText("");
		dsrefEmployerLabel.setText("Select Employer");
		
		// set a handler for the selections click
		dsrefSelectedTableView.setOnMouseClicked(mouseClickedEvent ->
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) 
            {
            	dsrefRemoveButton.setDisable(false);
            }
        });

		dsrefChoicesListView.setOnMouseClicked(mouseClickedEvent ->
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) 
            {
            	department = dsrefChoicesListView.getSelectionModel().getSelectedItem().getDepartment();
            }
        });
		setTableColumns();
	}
	
	public void setFileType(PipelineFileType fileType) {
		this.fileType = fileType;
	}

	private void setTableColumns()
	{
		//clear the default values
		dsrefSelectedTableView.getColumns().clear();

	    TableColumn<HBoxDepartmentCell, String> x1 = new TableColumn<HBoxDepartmentCell, String>("Source");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxDepartmentCell,String>("source"));
		x1.setMinWidth(175);
		dsrefSelectedTableView.getColumns().add(x1);
		
		TableColumn<HBoxDepartmentCell, String> x3 = new TableColumn<HBoxDepartmentCell, String>("Reference");
		x3.setCellValueFactory(new PropertyValueFactory<HBoxDepartmentCell,String>("reference"));
		x3.setMinWidth(225);
		dsrefSelectedTableView.getColumns().add(x3);
	}
	
	public void load() 
	{
		// reset the filter
		dsrefEmployerFilterField.setText("");
		refreshEmployerCombo();
		dsrefChoicesListView.getItems().clear();
		refreshSelectionDisplay();
	}

	private void refreshEmployerCombo() 
	{
		dsRefEmployerCombo.getItems().clear();
		currentEmployerListIds.clear();
		
		for(Employer employer : DataManager.i().mEmployersList)
		{
			if(employer.isActive() == false) continue;
			if(employer.isDeleted() == true) continue;

			if(dsrefEmployerFilterField.getText().equals("") == false)
			{
				if(employer.getName().toUpperCase().contains(dsrefEmployerFilterField.getText().toUpperCase()) == true)
				{
					dsRefEmployerCombo.getItems().add(employer.getName());
					currentEmployerListIds.add(employer.getId());
				} continue;
			} else
				dsRefEmployerCombo.getItems().add(employer.getName());
				currentEmployerListIds.add(employer.getId());
		}
	}
	
	private void loadDepartmentChoices() 
	{
		dsrefChoicesListView.getItems().clear();
		if(dsRefEmployerCombo.getSelectionModel().getSelectedItem() == null) 
		{
			dsrefAddEmployerDepartmentRefButton.setDisable(true);
			dsrefAddDepartmentField.setDisable(true);
			dsrefAddDepartmentField.setText("");
			dsrefAddDepartmentField.setPromptText("");
			dsrefEmployerLabel.setText("Select Employer");
			return;
		}

		// enable the 
		dsrefAddEmployerDepartmentRefButton.setDisable(false);
		dsrefAddDepartmentField.setDisable(false);
		dsrefAddDepartmentField.setText("");
		dsrefAddDepartmentField.setPromptText("Department Name");

		try {
			// get the departments for the selected employer
			DepartmentRequest request = new DepartmentRequest(DataManager.i().mDepartment);
			request.setId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));
			request.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));
			DataManager.i().mDepartments = AdminPersistenceManager.getInstance().getAll(request);
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		dsrefEmployerLabel.setText(dsRefEmployerCombo.getSelectionModel().getSelectedItem().toString());
		if(DataManager.i().mDepartments == null) return;

		// and load them to the list
		List<ChoiceCell> list = new ArrayList<>();	
		for(Department dept : DataManager.i().mDepartments)
		{
			if(dept.isActive() == false) continue;
			list.add(new ChoiceCell(dept));
		}
        ObservableList<ChoiceCell> newObservableList = FXCollections.observableList(list);
        dsrefChoicesListView.setItems(newObservableList);	
		dsrefRemoveEmployerDepartmentRefButton.setDisable(false);
	}	

	private void refreshSelectionDisplay()
	{
		try {
			DepartmentRequest request = new DepartmentRequest();
			DataManager.i().mDepartments = AdminPersistenceManager.getInstance().getAll(request);
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		dsrefSelectedTableView.getItems().clear();
		switch(fileType) 
		{
			case EMPLOYEE:
				if(DataManager.i().mDynamicEmployeeFileSpecification.getDeptReferences() != null)
				{					
					for(DynamicEmployeeFileDepartmentReference ref : DataManager.i().mDynamicEmployeeFileSpecification.getDeptReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							for(Department dept : DataManager.i().mDepartments)
							{
								if(dept.isActive() == false) continue;
								if(dept.isDeleted() == true) continue;

								if(dept.getId().equals(ref.getDepartmentId()))
								{
									ref.setDepartment(dept);
									dsrefSelectedTableView.getItems().add(new HBoxDepartmentCell(ref, dept));
								}
							}
						}
					}
				}		
				break;
			case PAY:
				if(DataManager.i().mDynamicPayFileSpecification.getDeptReferences() != null)
				{					
					for(DynamicPayFileDepartmentReference ref : DataManager.i().mDynamicPayFileSpecification.getDeptReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							for(Department dept : DataManager.i().mDepartments)
							{
								if(dept.isActive() == false) continue;
								if(dept.isDeleted() == true) continue;

								if(dept.getId().equals(ref.getDepartmentId()))
								{
									ref.setDepartment(dept);
									dsrefSelectedTableView.getItems().add(new HBoxDepartmentCell(ref, dept));
								}
							}
						}
					}
				}		
				break;
			default:
				break;
		}
	}

	@FXML
	private void onAdd(ActionEvent event) 
	{
		if(dsrefChoicesListView.getSelectionModel().getSelectedItem() == null) return;
		if(dsrefReferenceField.getText() == "" || dsrefReferenceField.getText().isEmpty()) return;
		changesMade = true;
		Department dept = dsrefChoicesListView.getSelectionModel().getSelectedItem().getDepartment();

		switch(fileType) 
		{
			case EMPLOYEE:
				try {
					// create the request
					DynamicEmployeeFileDepartmentReferenceRequest empRequest = new DynamicEmployeeFileDepartmentReferenceRequest();
					DynamicEmployeeFileDepartmentReference empRef = new DynamicEmployeeFileDepartmentReference();
					empRef.setDepartmentId(dept.getId());
					empRef.setDepartment(dept);
					empRef.setReference(dsrefReferenceField.getText());
					empRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					empRef.setSpecification(DataManager.i().mDynamicEmployeeFileSpecification);
					empRequest.setEntity(empRef);
					empRequest.setId(empRef.getId());
	
					// save it to the server
					empRef = AdminPersistenceManager.getInstance().addOrUpdate(empRequest);
					dsrefSelectedTableView.getItems().add(new HBoxDepartmentCell(empRef, dept));
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;

			case PAY:
				try {
					DynamicPayFileDepartmentReference payRef = new DynamicPayFileDepartmentReference();
					DynamicPayFileDepartmentReferenceRequest payRequest = new DynamicPayFileDepartmentReferenceRequest();
					payRef.setDepartmentId(dept.getId());
					payRef.setReference(dsrefReferenceField.getText());
					payRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					payRequest.setEntity(payRef);
					payRequest.setId(payRef.getId());
	
					// save it to the server
					payRef = AdminPersistenceManager.getInstance().addOrUpdate(payRequest);
					dsrefSelectedTableView.getItems().add(new HBoxDepartmentCell(payRef, dept));
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			default:
				break;
		}

		//reset the field
		dsrefReferenceField.setText("");
		dsrefReferenceField.setPromptText("Source Value (required)");
		//and reload
//		refreshSelectionDisplay();
	}		

	@FXML
	private void onRemove(ActionEvent event)
	{
		if(dsrefSelectedTableView.getSelectionModel().getSelectedItem() == null) return;
		changesMade = true;

		switch(DataManager.i().mPipelineChannel.getType())
		{
			case EMPLOYEE:
				try {
					DynamicEmployeeFileDepartmentReferenceRequest empRequest = new DynamicEmployeeFileDepartmentReferenceRequest();
					DynamicEmployeeFileDepartmentReference empRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getEmployeeRef();
					empRequest.setId(empRef.getId());
					empRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(empRequest);
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			case PAY:
				try {
					DynamicPayFileDepartmentReferenceRequest payRequest = new DynamicPayFileDepartmentReferenceRequest();
					DynamicPayFileDepartmentReference payRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getPayRef();
					payRequest.setId(payRef.getId());
					payRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(payRequest);
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			default:
				break;
		}
		refreshSelectionDisplay();
	}	

	@FXML
	private void onEmployerFilter(ActionEvent event) {
		refreshEmployerCombo();
		dsRefEmployerCombo.show();
	}
 
	@FXML
	private void onEmplopyerFilterClear(ActionEvent event) {
		dsrefEmployerFilterField.setText("");
		refreshEmployerCombo();
		dsRefEmployerCombo.show();
	}

	@FXML
	private void onSelectEmployer(ActionEvent event) {
		loadDepartmentChoices();
	}

	@FXML
	private void onSave(ActionEvent event) {
		Stage stage = (Stage) dsrefSaveButton.getScene().getWindow();
		stage.close();
	}	

	@FXML
	private void onAddEmployerDepartment(ActionEvent event) 
	{
		if(dsrefAddDepartmentField.getText().isEmpty()) return;

		DepartmentRequest request = new DepartmentRequest();
		Department dept = new Department();

		try {
			dept.setName(dsrefAddDepartmentField.getText());
			dept.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));
			request.setId(dept.getId());
			request.setEntity(dept);
			request.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));

			department = AdminPersistenceManager.getInstance().addOrUpdate(request);
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
		loadDepartmentChoices(); 
	}

	@FXML
	private void onRemoveEmployerDepartment(ActionEvent event)
	{	
//		if(dsrefAddDepartmentField.getText().isEmpty()) return;

		DepartmentRequest request = new DepartmentRequest();
		Department dept = department;

		try {
			request.setId(dept.getId());
			request.setEntity(dept);
			request.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));

			AdminPersistenceManager.getInstance().remove(request);
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
		loadDepartmentChoices(); 
//		refreshEmployerCombo();
	}

	public class ChoiceCell extends HBox
	{
		 Label id = new Label();
		 Label name = new Label();
		 Department department;

         public Department getDepartment() {
        	 return department;
         }

         ChoiceCell(Department department)
         {
              super();
              
              this.department = department;
              name.setText(department.getName());
              id.setText(String.valueOf(department.getId()));
              
              //set the label widths
              id.setMinWidth(50);
              id.setMaxWidth(50);
              id.setPrefWidth(50);
              name.setMinWidth(150);
              name.setMaxWidth(150);
              name.setPrefWidth(150);

              this.getChildren().addAll(id, name);
         }
    }	
	
	public class HBoxDepartmentCell extends HBox 
	{
		SimpleStringProperty name = new SimpleStringProperty();
		SimpleStringProperty source = new SimpleStringProperty();
		SimpleStringProperty reference = new SimpleStringProperty();
		DynamicEmployeeFileDepartmentReference empRef = null;
		DynamicPayFileDepartmentReference payRef = null;
		Department dept = null;

		public DynamicEmployeeFileDepartmentReference getEmployeeRef() {
			return empRef;
		}
		
		public DynamicPayFileDepartmentReference getPayRef() {
			return payRef;
		}
		
		public Department getDepartment() {
			return dept;
		}
		
        public String getName() {
       	 return name.get();
        }
		
         public String getSource() {
        	 return source.get();
         }
         
         public String getReference() {
        	 return reference.get();
         }
         
         HBoxDepartmentCell(DynamicEmployeeFileDepartmentReference ref, Department dept) 
         {
             super();
             this.dept = dept;
             if(dept != null) { this.name.set(dept.getName().toString()); }
             this.source.set(ref.getReference());
             this.reference.set(dept.getId().toString() + "	          " + dept.getName());
             this.empRef = ref;
         }
         
         HBoxDepartmentCell(DynamicPayFileDepartmentReference ref, Department dept) 
         {
             super();
             this.dept = dept;
             if(dept != null) { this.name.set(dept.getName().toString()); }
             this.name.set(dept.getName().toString()); 
             this.source.set(ref.getReference());
             this.reference.set(dept.getId().toString() + "	          " + dept.getName());
             this.payRef = ref;
        }
    }	
}

