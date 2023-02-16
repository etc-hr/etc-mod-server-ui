package com.etc.admin.ui.pipeline.filespecification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileUnionTypeReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileUnionTypeReference;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileUnionTypeReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileUnionTypeReferenceRequest;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.utils.types.UnionType;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewDynamicFileSpecificationUnionTypeRefController
{
	@FXML
	private Button dsrefAddButton;
	@FXML
	private Button dsrefRemoveButton;
	@FXML
	private Button dsrefSaveButton;
	@FXML
	private TableView<HBoxUnionTypeCell> dsrefSelectedTableView;
	@FXML
	private ListView<UnionType> dsrefChoicesListView;
	@FXML
	private TextField dsrefReferenceField;
	@FXML
	private TextField dsrefEmployerFilterField;
	@FXML
	private ComboBox<String> dsRefEmployerCombo;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	private PipelineFileType fileType;

	//local employer selection
	Employer employer = null;
	List<Long> currentEmployerListIds = new ArrayList<Long>();
	
	public void setFileType(PipelineFileType fileType) {
		this.fileType = fileType;
	}

	public Boolean changesMade = false;
	@FXML
	public void initialize() 
	{
		// disable the selection-dependent buttons
		dsrefRemoveButton.setDisable(true);
		dsrefReferenceField.setText("");
		dsrefReferenceField.setPromptText("Reference Value (required)");
		
		// set a handler for the selections click
		dsrefSelectedTableView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0)
            {
            	dsrefRemoveButton.setDisable(false);
            }
        });
	
		setTableColumns();
	}
	
	private void setTableColumns() 
	{
		//clear the default values
		dsrefSelectedTableView.getColumns().clear();

		ObservableList<UnionType> unionTypes = FXCollections.observableArrayList(UnionType.values());
		dsrefChoicesListView.setItems(unionTypes);

		TableColumn<HBoxUnionTypeCell, String> x1 = new TableColumn<HBoxUnionTypeCell, String>("Source");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxUnionTypeCell,String>("source"));
		x1.setMinWidth(175);
		dsrefSelectedTableView.getColumns().add(x1);
		
		TableColumn<HBoxUnionTypeCell, String> x3 = new TableColumn<HBoxUnionTypeCell, String>("Reference");
		x3.setCellValueFactory(new PropertyValueFactory<HBoxUnionTypeCell,String>("reference"));
		x3.setMinWidth(225);
		dsrefSelectedTableView.getColumns().add(x3);
	}
	
	public void load() 
	{
		// reset the filter
		dsrefEmployerFilterField.setText("");
		refreshEmployerCombo();
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
				}
				continue;
			} else 
				dsRefEmployerCombo.getItems().add(employer.getName());
				currentEmployerListIds.add(employer.getId());
		}
	}
	
	private void refreshSelectionDisplay() 
	{
		dsrefSelectedTableView.getItems().clear();
		switch(fileType) 
		{
			case PAY:
				if(DataManager.i().mDynamicPayFileSpecification.getUnionTypeReferences() != null)
					for(DynamicPayFileUnionTypeReference ref : DataManager.i().mDynamicPayFileSpecification.getUnionTypeReferences())
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							dsrefSelectedTableView.getItems().add( new HBoxUnionTypeCell(ref));
						}
				break;
			case EMPLOYEE:
				if(DataManager.i().mDynamicEmployeeFileSpecification.getUnionTypeReferences() != null)
					for(DynamicEmployeeFileUnionTypeReference ref : DataManager.i().mDynamicEmployeeFileSpecification.getUnionTypeReferences())
						if(ref.isActive() == true && ref.isDeleted() == false) 
						{
							dsrefSelectedTableView.getItems().add( new HBoxUnionTypeCell(ref));
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
		UnionType unionType = dsrefChoicesListView.getSelectionModel().getSelectedItem();
		
		switch(fileType)
		{
			case PAY:
				try {
					DynamicPayFileUnionTypeReferenceRequest unRequest = new DynamicPayFileUnionTypeReferenceRequest();
					DynamicPayFileUnionTypeReference unRef = new DynamicPayFileUnionTypeReference();
					
					unRef.setUnionType(unionType);
					unRef.setReference(dsrefReferenceField.getText());
					unRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					unRef.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));
					unRequest.setEntity(unRef);
					unRequest.setId(unRef.getId());
	
					// save it to the server
					AdminPersistenceManager.getInstance().addOrUpdate(unRequest);
					dsrefSelectedTableView.getItems().add( new HBoxUnionTypeCell(unRef));
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			case EMPLOYEE:
				try {
					// create the request
					DynamicEmployeeFileUnionTypeReferenceRequest empRequest = new DynamicEmployeeFileUnionTypeReferenceRequest();
					DynamicEmployeeFileUnionTypeReference empRef = new DynamicEmployeeFileUnionTypeReference();
					
					empRef.setUnionType(unionType);
					empRef.setReference(dsrefReferenceField.getText());
					empRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					empRef.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));
					empRequest.setEntity(empRef);
					empRequest.setId(empRef.getId());
					
					// save it to the server
					AdminPersistenceManager.getInstance().addOrUpdate(empRequest);
					dsrefSelectedTableView.getItems().add( new HBoxUnionTypeCell(empRef));
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			default:
				break;
		}
		//reset the field
		dsrefReferenceField.setText("");
		dsrefReferenceField.setPromptText("Reference Value (required)");
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
			case PAY:
				try {
					DynamicPayFileUnionTypeReferenceRequest payRequest = new DynamicPayFileUnionTypeReferenceRequest();
					DynamicPayFileUnionTypeReference payRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getPayRef();
					payRequest.setId(payRef.getId());
					payRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(payRequest);
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			case EMPLOYEE:
				try {
					DynamicEmployeeFileUnionTypeReferenceRequest empRequest = new DynamicEmployeeFileUnionTypeReferenceRequest();
					DynamicEmployeeFileUnionTypeReference empRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getEeRef();
					empRequest.setId(empRef.getId());
					empRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(empRequest);
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			default:
				break;
		}
		refreshSelectionDisplay();
	}	
	
	@FXML
	private void onEmployerFilter(ActionEvent event)
	{
		refreshEmployerCombo();
		dsRefEmployerCombo.show();
	}
	
	@FXML
	private void onEmplopyerFilterClear(ActionEvent event)
	{
		dsrefEmployerFilterField.setText("");
		refreshEmployerCombo();
		dsRefEmployerCombo.show();
	}
	
	@FXML
	private void onSave(ActionEvent event) 
	{
		Stage stage = (Stage) dsrefSaveButton.getScene().getWindow();
		stage.close();
	}	
	
	public class HBoxUnionTypeCell extends HBox 
	{
		SimpleStringProperty source = new SimpleStringProperty();
		SimpleStringProperty reference = new SimpleStringProperty();
		DynamicPayFileUnionTypeReference payRef = null;
		DynamicEmployeeFileUnionTypeReference eeRef = null;

		public DynamicPayFileUnionTypeReference getPayRef() {
			return payRef;
		}
		
		public DynamicEmployeeFileUnionTypeReference getEeRef() {
			return eeRef;
		}
		
        public String getSource() {
        	return source.get();
        }
         
        public String getReference() {
        	return reference.get();
        }
         
        HBoxUnionTypeCell(DynamicPayFileUnionTypeReference ref) 
        {
             super();
             this.source.set(ref.getReference());
             this.reference.set(ref.getUnionType().toString());
             this.payRef = ref;
        }
         
        HBoxUnionTypeCell(DynamicEmployeeFileUnionTypeReference ref)
        {
             super();
             this.source.set(ref.getReference());
             this.reference.set(ref.getUnionType().toString());
             this.eeRef = ref;
        }
    }	
}
