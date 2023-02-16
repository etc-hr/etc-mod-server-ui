package com.etc.admin.ui.pipeline.filespecification;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.corvetto.ems.pipeline.entities.c95.DynamicIrs1095cFileGenderReference;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileGenderReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileGenderReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileGenderReference;
import com.etc.corvetto.ems.pipeline.rqs.c95.DynamicIrs1095cFileGenderReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.cov.DynamicCoverageFileGenderReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileGenderReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileGenderReferenceRequest;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;
import com.etc.corvetto.entities.Employer;
import com.etc.utils.types.GenderType;

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

public class ViewDynamicFileSpecificationGenderTypeRefController 
{
	@FXML
	private Button dsrefAddButton;
	@FXML
	private Button dsrefRemoveButton;
	@FXML
	private Button dsrefSaveButton;
	@FXML
	private TableView<HBoxGenderCell> dsrefSelectedTableView;
	@FXML
	private ListView<GenderType> dsrefChoicesListView;
	@FXML
	private TextField dsrefReferenceField;
	@FXML
	private TextField dsrefEmployerFilterField;
	@FXML
	private ComboBox<String> dsRefEmployerCombo;
	
	private PipelineFileType fileType;
	
	//local employer selection
	Employer employer = null;	
	List<Long> currentEmployerListIds = new ArrayList<Long>();

	public Boolean changesMade = false;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() 
	{
		// disable the selection-dependent buttons
		dsrefRemoveButton.setDisable(true);
		dsrefReferenceField.setText("");
		dsrefReferenceField.setPromptText("Source Value (required)");

		ObservableList<GenderType> genderTypes = FXCollections.observableArrayList(GenderType.values());
		dsrefChoicesListView.setItems(genderTypes);

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

	    TableColumn<HBoxGenderCell, String> x1 = new TableColumn<HBoxGenderCell, String>("Source");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxGenderCell,String>("source"));
		x1.setMinWidth(210);
		dsrefSelectedTableView.getColumns().add(x1);
		
		TableColumn<HBoxGenderCell, String> x3 = new TableColumn<HBoxGenderCell, String>("Reference");
		x3.setCellValueFactory(new PropertyValueFactory<HBoxGenderCell,String>("reference"));
		x3.setMinWidth(190);
		dsrefSelectedTableView.getColumns().add(x3);
	}
	
	public void setFileType(PipelineFileType fileType) {
		this.fileType = fileType;
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
			case COVERAGE:
				if(DataManager.i().mDynamicCoverageFileSpecification.getGenderReferences() != null)
				{
					List<HBoxGenderCell> list = new ArrayList<>();
					
					for(DynamicCoverageFileGenderReference ref : DataManager.i().mDynamicCoverageFileSpecification.getGenderReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							list.add(new HBoxGenderCell(ref));
						}
					}
			        ObservableList<HBoxGenderCell> myObservableList = FXCollections.observableList(list);
			        Comparator<HBoxGenderCell> comparator = Comparator.comparing(HBoxGenderCell::getReference);
			        myObservableList.sort(comparator);        
					dsrefSelectedTableView.setItems(myObservableList);
				}
				break;
			case EMPLOYEE:
				if(DataManager.i().mDynamicEmployeeFileSpecification.getGenderReferences() != null)
				{
					List<HBoxGenderCell> list = new ArrayList<>();

					for(DynamicEmployeeFileGenderReference ref : DataManager.i().mDynamicEmployeeFileSpecification.getGenderReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							list.add(new HBoxGenderCell(ref));
						}
					}
			        ObservableList<HBoxGenderCell> myObservableList = FXCollections.observableList(list);
			        Comparator<HBoxGenderCell> comparator = Comparator.comparing(HBoxGenderCell::getReference);
			        myObservableList.sort(comparator);        
					dsrefSelectedTableView.setItems(myObservableList);
				}
				break;
			case IRS1095C:
				if(DataManager.i().mDynamicIrs1095cFileSpecification.getGenderReferences() != null)
				{
					List<HBoxGenderCell> list = new ArrayList<>();

					for(DynamicIrs1095cFileGenderReference ref : DataManager.i().mDynamicIrs1095cFileSpecification.getGenderReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							list.add(new HBoxGenderCell(ref));
						}
					}
			        ObservableList<HBoxGenderCell> myObservableList = FXCollections.observableList(list);
			        Comparator<HBoxGenderCell> comparator = Comparator.comparing(HBoxGenderCell::getReference);
			        myObservableList.sort(comparator);        
					dsrefSelectedTableView.setItems(myObservableList);
				}
				break;
			case PAY:
				if(DataManager.i().mDynamicPayFileSpecification.getGenderReferences() != null)
				{
					List<HBoxGenderCell> list = new ArrayList<>();

					for(DynamicPayFileGenderReference ref : DataManager.i().mDynamicPayFileSpecification.getGenderReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							list.add(new HBoxGenderCell(ref));
						}
					}
			        ObservableList<HBoxGenderCell> myObservableList = FXCollections.observableList(list);
			        Comparator<HBoxGenderCell> comparator = Comparator.comparing(HBoxGenderCell::getReference);
			        myObservableList.sort(comparator);        
					dsrefSelectedTableView.setItems(myObservableList);
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
		GenderType genderType = dsrefChoicesListView.getSelectionModel().getSelectedItem();

		switch(fileType)
		{
			case COVERAGE:
				try {
					DynamicCoverageFileGenderReferenceRequest covRequest = new DynamicCoverageFileGenderReferenceRequest(DataManager.i().mDynamicCoverageFileGenderReference);
					DynamicCoverageFileGenderReference covRef = new DynamicCoverageFileGenderReference();
					covRef.setGenderType(genderType);
					covRef.setReference(dsrefReferenceField.getText());
					covRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					covRequest.setEntity(covRef);
					covRequest.setId(covRef.getId());
					covRequest.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));
					
					// save it to the server
					covRef = AdminPersistenceManager.getInstance().addOrUpdate(covRequest);
					dsrefSelectedTableView.getItems().add(new HBoxGenderCell(covRef));
				} catch (CoreException e) { 
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) { 
        	    	DataManager.i().logGenericException(e); 
        	    }
				break;
				
			case EMPLOYEE:
				try {
					// create the request
					DynamicEmployeeFileGenderReferenceRequest empRequest = new DynamicEmployeeFileGenderReferenceRequest(DataManager.i().mDynamicEmployeeFileGenderReference);
					DynamicEmployeeFileGenderReference empRef = new DynamicEmployeeFileGenderReference();
					empRef.setGenderType(genderType);
					empRef.setReference(dsrefReferenceField.getText());
					empRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					empRequest.setEntity(empRef);
					empRequest.setId(empRef.getId());
					empRequest.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));
					
					// save it to the server
					empRef = AdminPersistenceManager.getInstance().addOrUpdate(empRequest);
					dsrefSelectedTableView.getItems().add(new HBoxGenderCell(empRef));
				} catch (CoreException e) {  
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  
        	    	DataManager.i().logGenericException(e);
        	    }
				break;
				
			case IRS1095C:
				try {
					// create the request
					DynamicIrs1095cFileGenderReferenceRequest irs1095cRequest = new DynamicIrs1095cFileGenderReferenceRequest(DataManager.i().mDynamicIrs1095cFileGenderReference);
					DynamicIrs1095cFileGenderReference irs1095cRef = new DynamicIrs1095cFileGenderReference();
					irs1095cRef.setGenderType(genderType);
					irs1095cRef.setReference(dsrefReferenceField.getText());
					irs1095cRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					irs1095cRequest.setEntity(irs1095cRef);
					irs1095cRequest.setId(irs1095cRef.getId());
					irs1095cRequest.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));
					
					// save it to the server
					irs1095cRef = AdminPersistenceManager.getInstance().addOrUpdate(irs1095cRequest);
					dsrefSelectedTableView.getItems().add(new HBoxGenderCell(irs1095cRef));
				} catch (CoreException e) {  
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  
        	    	DataManager.i().logGenericException(e); 
        	    }
				break;
				
			case PAY:
				try {
					// create the request
					DynamicPayFileGenderReferenceRequest payRequest = new DynamicPayFileGenderReferenceRequest(DataManager.i().mDynamicPayFileGenderReference);
					DynamicPayFileGenderReference payRef = new DynamicPayFileGenderReference();
					payRef.setGenderType(genderType);
					payRef.setReference(dsrefReferenceField.getText());
					payRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					payRequest.setEntity(payRef);
					payRequest.setId(payRef.getId());
					payRequest.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));
					
					// save it to the server
					payRef = AdminPersistenceManager.getInstance().addOrUpdate(payRequest);
					dsrefSelectedTableView.getItems().add(new HBoxGenderCell(payRef));
				} catch (CoreException e) {  
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) { 
        	    	DataManager.i().logGenericException(e);
        	    }
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
			case COVERAGE:
				try {
					DynamicCoverageFileGenderReferenceRequest covRequest = new DynamicCoverageFileGenderReferenceRequest();
					DynamicCoverageFileGenderReference covRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getCovRef();
					covRequest.setId(covRef.getId());
					covRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(covRequest);
				} catch (CoreException e) {  
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) { 
        	    	DataManager.i().logGenericException(e);
        	    }
				break;
				
			case EMPLOYEE:
				try {
					DynamicEmployeeFileGenderReferenceRequest empRequest = new DynamicEmployeeFileGenderReferenceRequest();
					DynamicEmployeeFileGenderReference empRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getEmployeeRef();
					empRequest.setId(empRef.getId());
					empRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(empRequest);
				} catch (CoreException e) {  
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) { 
        	    	DataManager.i().logGenericException(e);
        	    }
				break;
				
			case IRS1095C:
				try {
					DynamicIrs1095cFileGenderReferenceRequest irs1095cRequest = new DynamicIrs1095cFileGenderReferenceRequest();
					DynamicIrs1095cFileGenderReference irs1095cRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getIrs1095cRef();
					irs1095cRequest.setId(irs1095cRef.getId());
					irs1095cRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(irs1095cRequest);
				} catch (CoreException e) {  
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  
        	    	DataManager.i().logGenericException(e);
        	    }
				break;
				
			case PAY:
				try {
					DynamicPayFileGenderReferenceRequest payRequest = new DynamicPayFileGenderReferenceRequest();
					DynamicPayFileGenderReference payRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getPayRef();
					payRequest.setId(payRef.getId());
					payRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(payRequest);
				} catch (CoreException e) {  
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) { 
        	    	DataManager.i().logGenericException(e);
        	    }
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
	
	public class HBoxGenderCell extends HBox 
	{
		SimpleStringProperty source = new SimpleStringProperty();
		SimpleStringProperty reference = new SimpleStringProperty();
		DynamicEmployeeFileGenderReference empRef = null;
		DynamicCoverageFileGenderReference covRef = null;
		DynamicPayFileGenderReference payRef = null;
		DynamicIrs1095cFileGenderReference irs1095cRef = null;

		public DynamicEmployeeFileGenderReference getEmployeeRef() { return empRef; }
		public DynamicCoverageFileGenderReference getCovRef() { return covRef; }
		public DynamicPayFileGenderReference getPayRef() { return payRef; }
		public DynamicIrs1095cFileGenderReference getIrs1095cRef() { return irs1095cRef; }
		public String getSource() { return source.get(); }
		public String getReference() { return reference.get(); }

		HBoxGenderCell(DynamicEmployeeFileGenderReference ref)
		{
			super();
			this.source.set(ref.getReference());
			this.reference.set(ref.getGenderType().toString());
			this.empRef = ref;
		}

		HBoxGenderCell(DynamicCoverageFileGenderReference ref)
		{
			super();
			this.source.set(ref.getReference());
			this.reference.set(ref.getGenderType().toString());
			this.covRef = ref;
		}

		HBoxGenderCell(DynamicPayFileGenderReference ref) 
		{
			super();
			this.source.set(ref.getReference());
			this.reference.set(ref.getGenderType().toString());
			this.payRef = ref;
		}
         
		HBoxGenderCell(DynamicIrs1095cFileGenderReference ref) 
		{
			super();
			this.source.set(ref.getReference());
			this.reference.set(ref.getGenderType().toString());
			this.irs1095cRef = ref;
		} 
    }	
}
