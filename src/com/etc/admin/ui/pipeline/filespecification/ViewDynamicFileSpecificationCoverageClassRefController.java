package com.etc.admin.ui.pipeline.filespecification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileCoverageGroupReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileCoverageGroupReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileCoverageGroupReference;
import com.etc.corvetto.ems.pipeline.rqs.cov.DynamicCoverageFileCoverageGroupReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileCoverageGroupReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileCoverageGroupReferenceRequest;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;
import com.etc.corvetto.entities.CoverageGroup;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.rqs.CoverageGroupRequest;

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

public class ViewDynamicFileSpecificationCoverageClassRefController
{
	@FXML
	private Button ccrefAddButton;
	@FXML
	private Button ccrefRemoveButton;
	@FXML
	private Button ccrefSaveButton;
	@FXML
	private TableView<HBoxCell> ccrefSelectedTableView;
	@FXML
	private ListView<ChoiceCell> ccrefChoicesListView;
	@FXML
	private TextField ccrefReferenceField;
	@FXML
	private TextField ccrefEmployerFilterField;
	@FXML
	private ComboBox<String> ccrefEmployerCombo;
	
	private PipelineFileType fileType;
	public boolean changesMade = false;
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	//local employer selection
	Employer employer = null;
	List<Long> currentEmployerListIds = new ArrayList<Long>();

	@FXML
	public void initialize() 
	{
		// disable the selection-dependent buttons
		ccrefRemoveButton.setDisable(true);
		ccrefReferenceField.setText("");
		ccrefReferenceField.setPromptText("Source Value (required)");
		
		// set a handler for the selections click
		ccrefSelectedTableView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) 
            {
            	ccrefRemoveButton.setDisable(false);
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
		ccrefSelectedTableView.getColumns().clear();

	    TableColumn<HBoxCell, String> x1 = new TableColumn<HBoxCell, String>("Source");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("covClass"));
		x1.setMinWidth(175);
		ccrefSelectedTableView.getColumns().add(x1);
		
		TableColumn<HBoxCell, String> x3 = new TableColumn<HBoxCell, String>("Reference");
		x3.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("reference"));
		x3.setMinWidth(225);
		ccrefSelectedTableView.getColumns().add(x3);
	}
	
	public void load() 
	{
		// reset the filter
		ccrefEmployerFilterField.setText("");
		refreshEmployerCombo();
		ccrefChoicesListView.getItems().clear();
		refreshSelectionDisplay();
	}
	
	private void refreshEmployerCombo()
	{
		ccrefEmployerCombo.getItems().clear();
		currentEmployerListIds.clear();
		
		for(Employer employer : DataManager.i().mEmployersList) 
		{
			if(employer.isActive() == false) continue;
			if(employer.isDeleted() == true) continue;
			if(ccrefEmployerFilterField.getText().equals("") == false)
			{
				if(employer.getName().toUpperCase().contains(ccrefEmployerFilterField.getText().toUpperCase()) == true)
				{
					ccrefEmployerCombo.getItems().add(employer.getName());
					currentEmployerListIds.add(employer.getId());
				}
				continue;
			} else
				ccrefEmployerCombo.getItems().add(employer.getName());
				currentEmployerListIds.add(employer.getId());
		}
 	}

	private void loadCoverageClassChoices()
	{
		ccrefChoicesListView.getItems().clear();
		if(ccrefEmployerCombo.getSelectionModel().getSelectedItem() == null)
		{
			return;
		}

		try {
			CoverageGroupRequest covRequest = new CoverageGroupRequest();
			covRequest.setEmployerId(currentEmployerListIds.get(ccrefEmployerCombo.getSelectionModel().getSelectedIndex()));
			DataManager.i().mCoverageClasses = AdminPersistenceManager.getInstance().getAll(covRequest);
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		if(DataManager.i().mCoverageClasses == null) return;

		// and load them to the list
		List<ChoiceCell> list = new ArrayList<>();
		for(CoverageGroup cvgClass : DataManager.i().mCoverageClasses) 
		{
			list.add(new ChoiceCell(cvgClass));
		}
        ObservableList<ChoiceCell> newObservableList = FXCollections.observableList(list);
        ccrefChoicesListView.setItems(newObservableList);	
	}	

	private void refreshSelectionDisplay()
	{
		ccrefSelectedTableView.getItems().clear();
		switch(fileType)
		{
			case COVERAGE:
				if(DataManager.i().mDynamicCoverageFileSpecification.getCvgGroupReferences() != null)
				{				
					for(DynamicCoverageFileCoverageGroupReference ref : DataManager.i().mDynamicCoverageFileSpecification.getCvgGroupReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							try {
								CoverageGroupRequest request = new CoverageGroupRequest();
								request.setEmployerId(ref.getEmployerId());
								DataManager.i().mCoverageClasses = AdminPersistenceManager.getInstance().getAll(request);
							} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
			        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
							
							for(CoverageGroup covGrp : DataManager.i().mCoverageClasses)
							{
								if(covGrp.isActive() == false) continue;
								if(covGrp.isDeleted() == true) continue;

								if(covGrp.getId().equals(ref.getCoverageGroupId()))
								{
									ref.setCoverageGroup(covGrp);
									ccrefSelectedTableView.getItems().add(new HBoxCell(ref, covGrp));
								}
							}
						}
					}
				}		
				break;
			case EMPLOYEE:
				if(DataManager.i().mDynamicEmployeeFileSpecification.getCvgGroupReferences() != null)
				{					
					for(DynamicEmployeeFileCoverageGroupReference ref : DataManager.i().mDynamicEmployeeFileSpecification.getCvgGroupReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							try {
								CoverageGroupRequest request = new CoverageGroupRequest();
								request.setEmployerId(ref.getEmployerId());
								DataManager.i().mCoverageClasses = AdminPersistenceManager.getInstance().getAll(request);
							} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
			        	    catch (Exception e) {  DataManager.i().logGenericException(e); }

							for(CoverageGroup covGrp : DataManager.i().mCoverageClasses)
							{
								if(covGrp.isActive() == false) continue;
								if(covGrp.isDeleted() == true) continue;

								if(covGrp.getId().equals(ref.getCoverageGroupId()))
								{
									ref.setCoverageGroup(covGrp);
									ccrefSelectedTableView.getItems().add(new HBoxCell(ref, covGrp));
								}
							}
						}
					}
				}		
				break;
			case PAY:
				if(DataManager.i().mDynamicPayFileSpecification.getCvgGroupReferences() != null)
				{					
					for(DynamicPayFileCoverageGroupReference ref : DataManager.i().mDynamicPayFileSpecification.getCvgGroupReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							try {
								CoverageGroupRequest request = new CoverageGroupRequest();
								request.setEmployerId(ref.getEmployerId());
								DataManager.i().mCoverageClasses = AdminPersistenceManager.getInstance().getAll(request);
							} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
			        	    catch (Exception e) {  DataManager.i().logGenericException(e); }

							for(CoverageGroup covGrp : DataManager.i().mCoverageClasses)
							{
								if(covGrp.isActive() == false) continue;
								if(covGrp.isDeleted() == true) continue;

								if(covGrp.getId().equals(ref.getCoverageGroupId()))
								{
									ref.setCoverageGroup(covGrp);
									ccrefSelectedTableView.getItems().add(new HBoxCell(ref, covGrp));
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
		if(ccrefChoicesListView.getSelectionModel().getSelectedItem() == null) return;
		if(ccrefReferenceField.getText() == "" || ccrefReferenceField.getText().isEmpty()) return;
		changesMade = true;
		CoverageGroup cvgClass = ccrefChoicesListView.getSelectionModel().getSelectedItem().getCoverageClass();

		switch(fileType)
		{
			case COVERAGE:
				try {
					DynamicCoverageFileCoverageGroupReferenceRequest covRequest = new DynamicCoverageFileCoverageGroupReferenceRequest(DataManager.i().mDynamicCoverageFileCoverageGroupReference);
					DynamicCoverageFileCoverageGroupReference covRef = new DynamicCoverageFileCoverageGroupReference();
					covRef.setReference(ccrefReferenceField.getText());
					covRef.setCoverageGroupId(cvgClass.getId());
					covRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					covRef.setEmployerId(currentEmployerListIds.get(ccrefEmployerCombo.getSelectionModel().getSelectedIndex()));
					covRequest.setEntity(covRef);
					covRequest.setId(covRef.getId());
	
					// save it to the server
					covRef = AdminPersistenceManager.getInstance().addOrUpdate(covRequest);
					ccrefSelectedTableView.getItems().add(new HBoxCell(covRef, cvgClass));
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			case EMPLOYEE:
				try {
					DynamicEmployeeFileCoverageGroupReference empRef = new DynamicEmployeeFileCoverageGroupReference();
					DynamicEmployeeFileCoverageGroupReferenceRequest empRequest = new DynamicEmployeeFileCoverageGroupReferenceRequest();
					empRef.setCoverageGroupId(cvgClass.getId());
					empRef.setReference(ccrefReferenceField.getText());
					empRef.setSpecificationId(DataManager.i().mDynamicEmployeeFileSpecification.getId());
					empRef.setEmployerId(currentEmployerListIds.get(ccrefEmployerCombo.getSelectionModel().getSelectedIndex()));
					empRequest.setEntity(empRef);
					empRequest.setId(empRef.getId());
	
					// save it to the server
					empRef = AdminPersistenceManager.getInstance().addOrUpdate(empRequest);
					ccrefSelectedTableView.getItems().add(new HBoxCell(empRef, cvgClass));
				} catch (CoreException e) {
					 DataManager.i().log(Level.SEVERE, e);
				}
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			case PAY:
				try {
					DynamicPayFileCoverageGroupReference payRef = new DynamicPayFileCoverageGroupReference();
					DynamicPayFileCoverageGroupReferenceRequest payRequest = new DynamicPayFileCoverageGroupReferenceRequest();
					payRef.setCoverageGroupId(cvgClass.getId());
					payRef.setReference(ccrefReferenceField.getText());
					payRef.setSpecificationId(DataManager.i().mDynamicPayFileSpecification.getId());
					payRef.setEmployerId(currentEmployerListIds.get(ccrefEmployerCombo.getSelectionModel().getSelectedIndex()));
					payRequest.setEntity(payRef);
					payRequest.setId(payRef.getId());
	
					// save it to the server
					payRef = AdminPersistenceManager.getInstance().addOrUpdate(payRequest);
					ccrefSelectedTableView.getItems().add(new HBoxCell(payRef, cvgClass));
				} catch (CoreException e) {
					 DataManager.i().log(Level.SEVERE, e);
				}
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			default:
				break;
		}
		//reset the field
		ccrefReferenceField.setText("");
		ccrefReferenceField.setPromptText("Source Value (required)");
		//and reload
//		refreshSelectionDisplay();
	}		

	@FXML
	private void onRemove(ActionEvent event)
	{
		if(ccrefSelectedTableView.getSelectionModel().getSelectedItem() == null) return;
		changesMade = true;
		
		switch(DataManager.i().mPipelineChannel.getType())
		{
			case COVERAGE:
				try {
					DynamicCoverageFileCoverageGroupReferenceRequest covRequest = new DynamicCoverageFileCoverageGroupReferenceRequest();
					DynamicCoverageFileCoverageGroupReference covRef = ccrefSelectedTableView.getSelectionModel().getSelectedItem().getCovRef();
					covRequest.setEntity(covRef);
					covRequest.setId(covRef.getId());
					covRef.setActive(false);
	
					AdminPersistenceManager.getInstance().remove(covRequest);
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
			
				break;
			case EMPLOYEE:
				try {
					DynamicEmployeeFileCoverageGroupReferenceRequest empRequest = new DynamicEmployeeFileCoverageGroupReferenceRequest();
					DynamicEmployeeFileCoverageGroupReference empRef = ccrefSelectedTableView.getSelectionModel().getSelectedItem().getEmpRef();
					empRequest.setEntity(empRef);
					empRequest.setId(empRef.getId());
					empRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(empRequest);
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
			
				break;
			case PAY:
				try {
					DynamicPayFileCoverageGroupReferenceRequest payRequest = new DynamicPayFileCoverageGroupReferenceRequest();
					DynamicPayFileCoverageGroupReference payRef = ccrefSelectedTableView.getSelectionModel().getSelectedItem().getPayRef();
					payRequest.setEntity(payRef);
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
		ccrefEmployerCombo.show();
	}
	
	@FXML
	private void onEmplopyerFilterClear(ActionEvent event) {
		ccrefEmployerFilterField.setText("");
		refreshEmployerCombo();
		ccrefEmployerCombo.show();
	}
	
	@FXML
	private void onSelectEmployer(ActionEvent event) {
		loadCoverageClassChoices();
	}
	
	@FXML
	private void onSave(ActionEvent event) {
		Stage stage = (Stage) ccrefSaveButton.getScene().getWindow();
		stage.close();
	}	
	
	public class ChoiceCell extends HBox 
	{
		Label id = new Label();
		Label name = new Label();
		CoverageGroup cvgClass;

         public CoverageGroup getCoverageClass() {
        	 return cvgClass;
         }
         
         ChoiceCell(CoverageGroup cvgClass)
         {
              super();
              
              this.cvgClass = cvgClass;
              Utils.setHBoxLabel(name, 150, false, cvgClass.getName());
              Utils.setHBoxLabel(id, 50, false, String.valueOf(cvgClass.getId()));
              this.getChildren().addAll(id, name);
         }
    }	
	
	public class HBoxCell extends HBox 
	{
		SimpleStringProperty name = new SimpleStringProperty();
		SimpleStringProperty covClass = new SimpleStringProperty();
		SimpleStringProperty reference = new SimpleStringProperty();
		DynamicCoverageFileCoverageGroupReference covRef;
		DynamicEmployeeFileCoverageGroupReference empRef;
		DynamicPayFileCoverageGroupReference payRef;

         public DynamicCoverageFileCoverageGroupReference getCovRef() {
        	 return covRef;
         }
         
         public DynamicPayFileCoverageGroupReference getPayRef() {
        	 return payRef;
         }
         
         public DynamicEmployeeFileCoverageGroupReference getEmpRef() {
        	 return empRef;
         }
         

         public String getCovClass() {
        	 return covClass.get();
         }
         
         public String getReference() {
        	 return reference.get();
         }
         
         public String getName() {
        	 return name.get();
         }
         
         HBoxCell(DynamicCoverageFileCoverageGroupReference covRef, CoverageGroup cvgClass)
         {
              super();
              this.covRef = covRef;
              if(cvgClass != null) { this.name.set(cvgClass.getName().toString()); }
              this.covClass.set(covRef.getReference().toString());
              this.reference.set(cvgClass.getId().toString() + "	          " + cvgClass.getName());
         }

         HBoxCell(DynamicEmployeeFileCoverageGroupReference empRef, CoverageGroup cvgClass)
         {
             super();
             this.empRef = empRef;
             if(cvgClass != null) { this.name.set(cvgClass.getName().toString()); }
             this.covClass.set(empRef.getReference().toString());
             this.reference.set(cvgClass.getId().toString() + "	          " + cvgClass.getName());
        }
         
         HBoxCell(DynamicPayFileCoverageGroupReference payRef, CoverageGroup cvgClass) 
         {
             super();
             this.payRef = payRef;
             if(cvgClass != null) { this.name.set(cvgClass.getName().toString()); }
             this.covClass.set(payRef.getReference().toString());
             this.reference.set(cvgClass.getId().toString() + "	          " + cvgClass.getName());
        }
	}	
}
