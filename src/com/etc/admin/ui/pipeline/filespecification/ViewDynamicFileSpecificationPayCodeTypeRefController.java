package com.etc.admin.ui.pipeline.filespecification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFilePayCodeReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayCodeReference;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFilePayCodeReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFilePayCodeReferenceRequest;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.utils.types.PayCodeType;

import javafx.beans.property.SimpleStringProperty;
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

public class ViewDynamicFileSpecificationPayCodeTypeRefController 
{
	@FXML
	private Button dsrefAddButton;
	@FXML
	private Button dsrefRemoveButton;
	@FXML
	private Button dsrefSaveButton;
	@FXML
	private TableView<HBoxPayCodeCell> dsrefSelectedTableView;
	@FXML
	private ListView<String> dsrefChoicesListView;
	@FXML
	private TextField dsrefReferenceField;
	@FXML
	private TextField dsrefEmployerFilterField;
	@FXML
	private ComboBox<String> dsRefEmployerCombo;
	
	private PipelineFileType fileType;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	//local employer selection
	Employer employer = null;
	List<Long> currentEmployerListIds = new ArrayList<Long>();
	
	public Boolean changesMade = false;
	
	@FXML
	public void initialize() 
	{
		// disable the selection-dependent buttons
		dsrefRemoveButton.setDisable(true);
		dsrefReferenceField.setText("");
		dsrefReferenceField.setPromptText("Source Value (required)");

		// manual add to avoid FTS
		dsrefChoicesListView.getItems().clear();
		dsrefChoicesListView.getItems().add("FTH");
		dsrefChoicesListView.getItems().add("VH");

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
	
	public void setFileType(PipelineFileType fileType) {
		this.fileType = fileType;
	}
	
	private void setTableColumns() 
	{
		//clear the default values
		dsrefSelectedTableView.getColumns().clear();

	    TableColumn<HBoxPayCodeCell, String> x1 = new TableColumn<HBoxPayCodeCell, String>("Source");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxPayCodeCell,String>("source"));
		x1.setMinWidth(175);
		dsrefSelectedTableView.getColumns().add(x1);
		
		TableColumn<HBoxPayCodeCell, String> x3 = new TableColumn<HBoxPayCodeCell, String>("Reference");
		x3.setCellValueFactory(new PropertyValueFactory<HBoxPayCodeCell,String>("reference"));
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
			case EMPLOYEE:
				if(DataManager.i().mDynamicEmployeeFileSpecification.getPayCodeReferences() != null)
					for(DynamicEmployeeFilePayCodeReference ref : DataManager.i().mDynamicEmployeeFileSpecification.getPayCodeReferences())
						if(ref.isActive() == true && ref.isDeleted() == false)
							dsrefSelectedTableView.getItems().add( new HBoxPayCodeCell(ref));
				break;
			case PAY:
				if(DataManager.i().mDynamicPayFileSpecification.getPayCodeReferences() != null)
					for(DynamicPayFilePayCodeReference ref : DataManager.i().mDynamicPayFileSpecification.getPayCodeReferences())
						if(ref.isActive() == true && ref.isDeleted() == false)
							dsrefSelectedTableView.getItems().add( new HBoxPayCodeCell(ref));
						
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
		PayCodeType pcType = PayCodeType.valueOf(dsrefChoicesListView.getSelectionModel().getSelectedItem());

		switch(fileType)
		{
			case EMPLOYEE:
				try {
					// create the request
					DynamicEmployeeFilePayCodeReferenceRequest empRequest = new DynamicEmployeeFilePayCodeReferenceRequest();
					DynamicEmployeeFilePayCodeReference empRef = new DynamicEmployeeFilePayCodeReference();
					empRef.setPayCodeType(pcType);
					empRef.setReference(dsrefReferenceField.getText());
					empRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					empRequest.setEntity(empRef);
					empRequest.setId(empRef.getId());
					empRequest.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));
	
					// save it to the server
					AdminPersistenceManager.getInstance().addOrUpdate(empRequest);
					dsrefSelectedTableView.getItems().add( new HBoxPayCodeCell(empRef));
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			case PAY:
				try {
					DynamicPayFilePayCodeReferenceRequest payRequest = new DynamicPayFilePayCodeReferenceRequest();
					DynamicPayFilePayCodeReference payRef = new DynamicPayFilePayCodeReference();
					payRef.setPayCodeType(pcType);
					payRef.setReference(dsrefReferenceField.getText());
					payRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					payRequest.setEntity(payRef);
					payRequest.setId(payRef.getId());
					payRequest.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));
					
					// save it to the server
					AdminPersistenceManager.getInstance().addOrUpdate(payRequest);
					dsrefSelectedTableView.getItems().add( new HBoxPayCodeCell(payRef));
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
					DynamicEmployeeFilePayCodeReferenceRequest empRequest = new DynamicEmployeeFilePayCodeReferenceRequest();
					DynamicEmployeeFilePayCodeReference empRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getEmployeeRef();
					empRequest.setId(empRef.getId());
					empRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(empRequest);
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			case PAY:
				try {
					DynamicPayFilePayCodeReferenceRequest payRequest = new DynamicPayFilePayCodeReferenceRequest();
					DynamicPayFilePayCodeReference payRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getPayRef();
					payRequest.setId(payRef.getId());
					payRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(payRequest);
				} catch (CoreException e) {	 DataManager.i().log(Level.SEVERE, e); }
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
	private void onSave(ActionEvent event) {
		Stage stage = (Stage) dsrefSaveButton.getScene().getWindow();
		stage.close();
	}	
	
	public class HBoxPayCodeCell extends HBox 
	{
		SimpleStringProperty source = new SimpleStringProperty();
		SimpleStringProperty reference = new SimpleStringProperty();
		DynamicEmployeeFilePayCodeReference empRef = null;
		DynamicPayFilePayCodeReference payRef = null;

		public DynamicEmployeeFilePayCodeReference getEmployeeRef() {
			return empRef;
		}
		
		public DynamicPayFilePayCodeReference getPayRef() {
			return payRef;
		}
		
         public String getSource() {
        	 return source.get();
         }
         
         public String getReference() {
        	 return reference.get();
         }
         
         HBoxPayCodeCell(DynamicEmployeeFilePayCodeReference ref) 
         {
              super();
              this.source.set(ref.getReference());
              this.reference.set(ref.getPayCodeType().toString());
              this.empRef = ref;
         }
         
         HBoxPayCodeCell(DynamicPayFilePayCodeReference ref) 
         {
             super();
             this.source.set(ref.getReference());
             this.reference.set(ref.getPayCodeType().toString());
             this.payRef = ref;
        }
    }	
}
