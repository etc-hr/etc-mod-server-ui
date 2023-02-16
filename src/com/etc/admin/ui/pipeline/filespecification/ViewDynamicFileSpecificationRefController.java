package com.etc.admin.ui.pipeline.filespecification;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.c95.DynamicIrs1095cFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.ded.DynamicDeductionFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.ins.DynamicInsuranceFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileEmployerReference;
import com.etc.corvetto.ems.pipeline.rqs.c95.DynamicIrs1095cFileEmployerReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.cov.DynamicCoverageFileEmployerReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.ded.DynamicDeductionFileEmployerReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileEmployerReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.ins.DynamicInsuranceFileEmployerReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileEmployerReferenceRequest;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.rqs.EmployerRequest;

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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewDynamicFileSpecificationRefController 
{
	@FXML
	private Button dsrefAddButton;
	@FXML
	private Button dsrefRemoveButton;
	@FXML
	private Button dsrefSaveButton;
	@FXML
	private TableView<HBoxEmployerCell> dsrefSelectedTableView;
	@FXML
	private ListView<EmployerChoiceCell> dsrefChoicesListView;
	@FXML
	private TextField dsrefReferenceField;
	@FXML
	private TextField dsrefAccountFilterField;
	@FXML
	private TextField dsrefEmployerFilterField;
	@FXML
	private ComboBox<String> dfcovFilterCombo;
	@FXML
	private Button dsrefAccountFilterApplyButton;
	@FXML
	private Button dsrefAccountFilterClearButton;
	@FXML
	private Button dsrefEmployerFilterApplyButton;
	@FXML
	private Button dsrefEmployerFilterClearButton;
	
	private PipelineFileType fileType;
	public Boolean changesMade = false;
	List<Long> currentAccountListIds = new ArrayList<Long>();

	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() 
	{
		loadAccountChoices();	
		dsrefReferenceField.setText("");
		dsrefReferenceField.setPromptText("Source Value (required)");
		setTableColumns();
	}
	
	public void setFileType(PipelineFileType fileType) 
	{
		this.fileType = fileType;
	}
	
	private void setTableColumns() 
	{
		//clear the default values
		dsrefSelectedTableView.getColumns().clear();

		TableColumn<HBoxEmployerCell, String> x1 = new TableColumn<HBoxEmployerCell, String>("Source");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxEmployerCell,String>("source"));
		x1.setMinWidth(285);
		dsrefSelectedTableView.getColumns().add(x1);

		TableColumn<HBoxEmployerCell, String> x3 = new TableColumn<HBoxEmployerCell, String>("Reference");
		x3.setCellValueFactory(new PropertyValueFactory<HBoxEmployerCell,String>("reference"));
		x3.setMinWidth(365);
		dsrefSelectedTableView.getColumns().add(x3);
	}

	public void load()
	{
		refreshSelectionDisplay();
	}
	
	private void loadAccountChoices() 
	{
		dfcovFilterCombo.getItems().clear();
		currentAccountListIds.clear();
		
		for(Account account : DataManager.i().mAccounts)
		{
			if(account.isActive() == false) continue;
			if(account.isDeleted() == true) continue;

			if(dsrefAccountFilterField.getText().isEmpty() == false)
			{
				if(account.getName().toUpperCase().contains(dsrefAccountFilterField.getText().toUpperCase()) == true)
				{
					dfcovFilterCombo.getItems().add(account.getName());
					currentAccountListIds.add(account.getId());
				}
				continue;
			} else
					dfcovFilterCombo.getItems().add(account.getName());
					currentAccountListIds.add(account.getId());
		}
	}

	private void loadEmployerChoices()
	{
		dsrefChoicesListView.getItems().clear();
		
  		try {
  			//if we have a filter, pull from the database. Otherwise simply iterate
  			EmployerRequest request = new EmployerRequest(DataManager.i().mEmployer);
  			request.setId(currentAccountListIds.get(dfcovFilterCombo.getSelectionModel().getSelectedIndex()));
  			request.setAccountId(currentAccountListIds.get(dfcovFilterCombo.getSelectionModel().getSelectedIndex()));
			DataManager.i().mEmployers = AdminPersistenceManager.getInstance().getAll(request);
			
		} catch (CoreException e) {  
			DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  
	    	DataManager.i().logGenericException(e); 
	    }
  		
		for(Employer employer : DataManager.i().mEmployers)
		{
			if(employer.isActive() == false) continue;
			if(employer.isDeleted() == true) continue;

			if(dsrefEmployerFilterField.getText().equals("") == false) 
			{
				if(employer.getName().toUpperCase().contains(dsrefEmployerFilterField.getText().toUpperCase()) == true) 
				{
					dsrefChoicesListView.getItems().add(new EmployerChoiceCell(employer));
				}
			} else {
					dsrefChoicesListView.getItems().add(new EmployerChoiceCell(employer));
			}
		}
	}

	private void refreshSelectionDisplay() 
	{
		dsrefSelectedTableView.getItems().clear();
		switch(fileType) 
		{
			case COVERAGE:
				if(DataManager.i().mDynamicCoverageFileSpecification.getErReferences() != null)
				{
					List<HBoxEmployerCell> list = new ArrayList<>();
					
					for(DynamicCoverageFileEmployerReference ref : DataManager.i().mDynamicCoverageFileSpecification.getErReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							for(Employer empr : DataManager.i().mEmployersList)
							{
								if(empr.isActive() == false) continue;
								if(empr.isDeleted() == true) continue;

								if(empr.getId().equals(ref.getEmployerId()))
								{
									ref.setEmployer(empr);
									list.add(new HBoxEmployerCell(ref,empr));
								}
							}
						}
				        ObservableList<HBoxEmployerCell> myObservableList = FXCollections.observableList(list);
				        Comparator<HBoxEmployerCell> comparator = Comparator.comparing(HBoxEmployerCell::getName);
				        myObservableList.sort(comparator);
						dsrefSelectedTableView.setItems(myObservableList);
					}
				}
				break;
			case EMPLOYEE:
				if(DataManager.i().mDynamicEmployeeFileSpecification.getErReferences() != null)
				{
					List<HBoxEmployerCell> list = new ArrayList<>();

					for(DynamicEmployeeFileEmployerReference ref : DataManager.i().mDynamicEmployeeFileSpecification.getErReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false) 
						{
							for(Employer empr : DataManager.i().mEmployersList) 
							{
								if(empr.isActive() == false) continue;
								if(empr.isDeleted() == true) continue;

								if(empr.getId().equals(ref.getEmployerId()))
								{
									ref.setEmployer(empr);
									list.add(new HBoxEmployerCell(ref,empr));
								}
							}
						}
				        ObservableList<HBoxEmployerCell> myObservableList = FXCollections.observableList(list);
				        Comparator<HBoxEmployerCell> comparator = Comparator.comparing(HBoxEmployerCell::getName);
				        myObservableList.sort(comparator);        
						dsrefSelectedTableView.setItems(myObservableList);
					}
				}
				break;
			case PAY:
				if(DataManager.i().mDynamicPayFileSpecification.getErReferences() != null)
				{
					List<HBoxEmployerCell> list = new ArrayList<>();

					for(DynamicPayFileEmployerReference ref : DataManager.i().mDynamicPayFileSpecification.getErReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false) 
						{
							for(Employer empr : DataManager.i().mEmployersList) 
							{
								if(empr.isActive() == false) continue;
								if(empr.isDeleted() == true) continue;

								if(empr.getId().equals(ref.getEmployerId()))
								{
									ref.setEmployer(empr);
									list.add(new HBoxEmployerCell(ref,empr));
								}
							}
						}
				        ObservableList<HBoxEmployerCell> myObservableList = FXCollections.observableList(list);
				        Comparator<HBoxEmployerCell> comparator = Comparator.comparing(HBoxEmployerCell::getName);
				        myObservableList.sort(comparator);        
						dsrefSelectedTableView.setItems(myObservableList);
					}
				}
				break;
			case INSURANCE:
				if(DataManager.i().mDynamicInsuranceFileSpecification.getErReferences() != null)
				{
					List<HBoxEmployerCell> list = new ArrayList<>();
					
					for(DynamicInsuranceFileEmployerReference ref : DataManager.i().mDynamicInsuranceFileSpecification.getErReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							for(Employer empr : DataManager.i().mEmployersList)
							{
								if(empr.isActive() == false) continue;
								if(empr.isDeleted() == true) continue;

								if(empr.getId().equals(ref.getEmployerId()))
								{
									ref.setEmployer(empr);
									list.add(new HBoxEmployerCell(ref,empr));
								}
							}
						}
				        ObservableList<HBoxEmployerCell> myObservableList = FXCollections.observableList(list);
				        Comparator<HBoxEmployerCell> comparator = Comparator.comparing(HBoxEmployerCell::getName);
				        myObservableList.sort(comparator);
						dsrefSelectedTableView.setItems(myObservableList);
					}
				}
				break;
			case DEDUCTION:
				if(DataManager.i().mDynamicDeductionFileSpecification.getErReferences() != null)
				{
					List<HBoxEmployerCell> list = new ArrayList<>();
					
					for(DynamicDeductionFileEmployerReference ref : DataManager.i().mDynamicDeductionFileSpecification.getErReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							for(Employer empr : DataManager.i().mEmployersList)
							{
								if(empr.isActive() == false) continue;
								if(empr.isDeleted() == true) continue;

								if(empr.getId().equals(ref.getEmployerId()))
								{
									ref.setEmployer(empr);
									list.add(new HBoxEmployerCell(ref,empr));
								}
							}
						}
				        ObservableList<HBoxEmployerCell> myObservableList = FXCollections.observableList(list);
				        Comparator<HBoxEmployerCell> comparator = Comparator.comparing(HBoxEmployerCell::getName);
				        myObservableList.sort(comparator);
						dsrefSelectedTableView.setItems(myObservableList);
					}
				}
				break;
			case IRS1095C:
				if(DataManager.i().mDynamicIrs1095cFileSpecification.getErReferences() != null)
				{
					List<HBoxEmployerCell> list = new ArrayList<>();
					
					for(DynamicIrs1095cFileEmployerReference ref : DataManager.i().mDynamicIrs1095cFileSpecification.getErReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							for(Employer empr : DataManager.i().mEmployersList)
							{
								if(empr.isActive() == false) continue;
								if(empr.isDeleted() == true) continue;

								if(empr.getId().equals(ref.getEmployerId()))
								{
									ref.setEmployer(empr);
									list.add(new HBoxEmployerCell(ref,empr));
								}
							}
						}
				        ObservableList<HBoxEmployerCell> myObservableList = FXCollections.observableList(list);
				        Comparator<HBoxEmployerCell> comparator = Comparator.comparing(HBoxEmployerCell::getName);
				        myObservableList.sort(comparator);
						dsrefSelectedTableView.setItems(myObservableList);
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
		Employer employer = dsrefChoicesListView.getSelectionModel().getSelectedItem().getEmployer();

		switch(fileType) 
		{
			case COVERAGE:
				try {
					// create the request
					DynamicCoverageFileEmployerReferenceRequest covRequest = new DynamicCoverageFileEmployerReferenceRequest(DataManager.i().mDynamicCoverageFileEmployerReference);
					DynamicCoverageFileEmployerReference covRef = new DynamicCoverageFileEmployerReference();
					covRef.setReference(dsrefReferenceField.getText());
					covRef.setEmployerId(employer.getId());
					covRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					covRequest.setEntity(covRef);
					covRequest.setId(covRef.getId());
					
					// save it to the server
					covRef = AdminPersistenceManager.getInstance().addOrUpdate(covRequest);
					dsrefSelectedTableView.getItems().add(new HBoxEmployerCell(covRef, employer));
				} catch (CoreException e) {  
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  
        	    	DataManager.i().logGenericException(e); 
        	    }
				break;
				
			case EMPLOYEE:
				try {
					// create the request
					DynamicEmployeeFileEmployerReferenceRequest empRequest = new DynamicEmployeeFileEmployerReferenceRequest(DataManager.i().mDynamicEmployeeFileEmployerReference);
					DynamicEmployeeFileEmployerReference empRef = new DynamicEmployeeFileEmployerReference();
					empRef.setReference(dsrefReferenceField.getText());
					empRef.setEmployerId(employer.getId());
					empRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					empRequest.setEntity(empRef);
					empRequest.setId(empRef.getId());
	
					// save it to the server
					empRef = AdminPersistenceManager.getInstance().addOrUpdate(empRequest);
					dsrefSelectedTableView.getItems().add(new HBoxEmployerCell(empRef, employer));
				} catch (CoreException e) {  
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) { 
        	    	DataManager.i().logGenericException(e);
        	    }
				break;
				
			case PAY:
				try {
					// create the request
					DynamicPayFileEmployerReferenceRequest payRequest = new DynamicPayFileEmployerReferenceRequest(DataManager.i().mDynamicPayFileEmployerReference);
					DynamicPayFileEmployerReference payRef = new DynamicPayFileEmployerReference();
					payRef.setReference(dsrefReferenceField.getText());
					payRef.setEmployerId(employer.getId());
					payRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					payRequest.setEntity(payRef);
					payRequest.setId(payRef.getId());
					
					// save it to the server
					payRef = AdminPersistenceManager.getInstance().addOrUpdate(payRequest);
					dsrefSelectedTableView.getItems().add(new HBoxEmployerCell(payRef, employer));
				} catch (CoreException e) {  
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  
        	    	DataManager.i().logGenericException(e); 
        	    }
				break;
				
			case INSURANCE:
				try {
					// create the request
					DynamicInsuranceFileEmployerReferenceRequest insRequest = new DynamicInsuranceFileEmployerReferenceRequest(DataManager.i().mDynamicInsuranceFileEmployerReference);
					DynamicInsuranceFileEmployerReference insRef = new DynamicInsuranceFileEmployerReference();
					insRef.setReference(dsrefReferenceField.getText());
					insRef.setEmployerId(employer.getId());
					insRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					insRequest.setEntity(insRef);
					insRequest.setId(insRef.getId());
					
					// save it to the server
					insRef = AdminPersistenceManager.getInstance().addOrUpdate(insRequest);
					dsrefSelectedTableView.getItems().add(new HBoxEmployerCell(insRef, employer));
				} catch (CoreException e) {  
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  
        	    	DataManager.i().logGenericException(e);
        	    }
				break;
				
			case DEDUCTION:
				try {
					// create the request
					DynamicDeductionFileEmployerReferenceRequest dedRequest = new DynamicDeductionFileEmployerReferenceRequest(DataManager.i().mDynamicDeductionFileEmployerReference);
					DynamicDeductionFileEmployerReference dedRef = new DynamicDeductionFileEmployerReference();
					dedRef.setReference(dsrefReferenceField.getText());
					dedRef.setEmployerId(employer.getId());
					dedRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					dedRequest.setEntity(dedRef);
					dedRequest.setId(dedRef.getId());
					
					// save it to the server
					dedRef = AdminPersistenceManager.getInstance().addOrUpdate(dedRequest);
					dsrefSelectedTableView.getItems().add(new HBoxEmployerCell(dedRef, employer));
				} catch (CoreException e) {  
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  
        	    	DataManager.i().logGenericException(e); 
        	    }
				break;
				
			case IRS1095C:
				try {
					// create the request
					DynamicIrs1095cFileEmployerReferenceRequest irs1095cRequest = new DynamicIrs1095cFileEmployerReferenceRequest(DataManager.i().mDynamicIrs1095cFileEmployerReference);
					DynamicIrs1095cFileEmployerReference irs1095cRef = new DynamicIrs1095cFileEmployerReference();
					irs1095cRef.setReference(dsrefReferenceField.getText());
					irs1095cRef.setEmployerId(employer.getId());
					irs1095cRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					irs1095cRequest.setEntity(irs1095cRef);
					irs1095cRequest.setId(irs1095cRef.getId());
					
					// save it to the server
					irs1095cRef = AdminPersistenceManager.getInstance().addOrUpdate(irs1095cRequest);
					dsrefSelectedTableView.getItems().add(new HBoxEmployerCell(irs1095cRef, employer));
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
			case COVERAGE:
				try {
					DynamicCoverageFileEmployerReferenceRequest covRequest = new DynamicCoverageFileEmployerReferenceRequest();
					DynamicCoverageFileEmployerReference covRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getCoverageRef();
					covRequest.setId(covRef.getId());
					AdminPersistenceManager.getInstance().remove(covRequest);
					covRef.setActive(false);
				} catch (CoreException e) { 
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) { 
        	    	DataManager.i().logGenericException(e); 
        	    }
				break;
				
			case EMPLOYEE:
				try {
					DynamicEmployeeFileEmployerReferenceRequest empRequest = new DynamicEmployeeFileEmployerReferenceRequest();
					DynamicEmployeeFileEmployerReference empRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getEmployeeRef();
					empRequest.setId(empRef.getId());
					empRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(empRequest);
				} catch (CoreException e) { 
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) { 
        	    	DataManager.i().logGenericException(e);
        	    }
				break;
			case PAY:
				try {
					DynamicPayFileEmployerReferenceRequest payRequest = new DynamicPayFileEmployerReferenceRequest();
					DynamicPayFileEmployerReference payRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getPayRef();
					payRequest.setId(payRef.getId());
					payRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(payRequest);
				} catch (CoreException e) {  
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) { 
        	    	DataManager.i().logGenericException(e); 
        	    }
				break;
				
			case INSURANCE:
				try {
					DynamicInsuranceFileEmployerReferenceRequest insRequest = new DynamicInsuranceFileEmployerReferenceRequest();
					DynamicInsuranceFileEmployerReference insRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getInsRef();
					insRequest.setId(insRef.getId());
					insRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(insRequest);
				} catch (CoreException e) {  
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  
        	    	DataManager.i().logGenericException(e); 
        	    }
				break;
				
			case DEDUCTION:
				try {
					DynamicDeductionFileEmployerReferenceRequest dedRequest = new DynamicDeductionFileEmployerReferenceRequest();
					DynamicDeductionFileEmployerReference dedRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getDedRef();
					dedRequest.setId(dedRef.getId());
					dedRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(dedRequest);
				} catch (CoreException e) { 
					DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) { 
        	    	DataManager.i().logGenericException(e);
        	    }
				break;
				
			case IRS1095C:
				try {
					DynamicIrs1095cFileEmployerReferenceRequest irs1095cRequest = new DynamicIrs1095cFileEmployerReferenceRequest();
					DynamicIrs1095cFileEmployerReference irs1095cRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getIrs1095cRef();
					irs1095cRequest.setId(irs1095cRef.getId());
					irs1095cRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(irs1095cRequest);
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
	private void onAccountFilter(ActionEvent event) 
	{
		loadAccountChoices();
		dfcovFilterCombo.show();
	}
	
	private void onAccountSelected()
	{
		loadEmployerChoices();
		dfcovFilterCombo.show();
	}
	
	@FXML
	private void onAccountFilterClear(ActionEvent event) 
	{
		dsrefAccountFilterField.setText("");
		loadAccountChoices();
		loadEmployerChoices();
	}
	
	@FXML
	private void onEmployerFilter(ActionEvent event) {
		loadEmployerChoices();
	}
	
	@FXML
	private void onEmplopyerFilterClear(ActionEvent event)
	{
		dsrefEmployerFilterField.setText("");
		loadEmployerChoices();
	}
	
	@FXML
	private void onFilter() 
	{
		loadEmployerChoices();
	}
	
	@FXML
	private void onSave(ActionEvent event) 
	{
		Stage stage = (Stage) dsrefSaveButton.getScene().getWindow();
		stage.close();
	}	
	
	public class EmployerChoiceCell extends HBox 
	{
		Label id = new Label();
		Label name = new Label();
		Employer employer;

        public Employer getEmployer() { return employer; }
         
        EmployerChoiceCell(Employer employer) 
        {
             super();
             this.employer = employer;
             Utils.setHBoxLabel(id, 50, false, employer.getId());
             Utils.setHBoxLabel(name, 450, false, employer.getName());
             this.getChildren().addAll(id, name);
        }
    }	
	
	public class HBoxEmployerCell extends HBox 
	{
		SimpleStringProperty name = new SimpleStringProperty();
		SimpleStringProperty source = new SimpleStringProperty();
		SimpleStringProperty reference = new SimpleStringProperty();
		DynamicCoverageFileEmployerReference ref = null;
		DynamicEmployeeFileEmployerReference empRef = null;
		DynamicPayFileEmployerReference payRef = null;
		DynamicInsuranceFileEmployerReference insRef = null;
		DynamicDeductionFileEmployerReference dedRef = null;
		DynamicIrs1095cFileEmployerReference irs1095cRef = null;
		
		Employer empr = null;
		
        public String getName() { return name.get(); }
        public String getSource() { return source.get(); }
        public String getReference() { return reference.get(); }
        public DynamicCoverageFileEmployerReference getCoverageRef() { return ref; }
 		public DynamicEmployeeFileEmployerReference getEmployeeRef() { return empRef; }
 		public DynamicPayFileEmployerReference getPayRef() { return payRef; }
 		public DynamicInsuranceFileEmployerReference getInsRef() { return insRef; }
 		public DynamicDeductionFileEmployerReference getDedRef() { return dedRef; }
 		public DynamicIrs1095cFileEmployerReference getIrs1095cRef() { return irs1095cRef; }
        public Employer getEmployer() { return empr; }

 		HBoxEmployerCell(DynamicCoverageFileEmployerReference ref, Employer empr) 
 		{
             super();
             this.empr = empr;
             if(empr != null) { this.name.set(empr.getName().toString()); }
			 this.source.set(ref.getReference().toString());
             this.reference.set(empr.getId().toString() + "	          " + empr.getName());
             this.ref = ref;
        }

        HBoxEmployerCell(DynamicEmployeeFileEmployerReference ref, Employer empr) 
        {
             super();
             this.empr = empr;
             if(empr != null) { this.name.set(empr.getName().toString()); }
             this.source.set(ref.getReference().toString());
             this.reference.set(empr.getId().toString() + "	          " + empr.getName());
             this.empRef = ref;
        }
        
        HBoxEmployerCell(DynamicPayFileEmployerReference ref, Employer empr) 
        {
             super();
             this.empr = empr;
             if(empr != null) { this.name.set(empr.getName().toString()); }
             this.source.set(ref.getReference().toString());
             this.reference.set(empr.getId().toString() + "	          " + empr.getName());
             this.payRef = ref;
        }             
        
        HBoxEmployerCell(DynamicInsuranceFileEmployerReference ref, Employer empr) 
        {
             super();
             this.empr = empr;
             if(empr != null) { this.name.set(empr.getName().toString()); }
             this.source.set(ref.getReference().toString());
             this.reference.set(empr.getId().toString() + "	          " + empr.getName());
             this.insRef = ref;
        }             
        
        HBoxEmployerCell(DynamicDeductionFileEmployerReference ref, Employer empr) 
        {
             super();
             this.empr = empr;
             if(empr != null) { this.name.set(empr.getName().toString()); }
             this.source.set(ref.getReference().toString());
             this.reference.set(empr.getId().toString() + "	          " + empr.getName());
             this.dedRef = ref;
        }             
        
        HBoxEmployerCell(DynamicIrs1095cFileEmployerReference ref, Employer empr) 
        {
             super();
             this.empr = empr;
             if(empr != null) { this.name.set(empr.getName().toString()); }
             this.source.set(ref.getReference().toString());
             this.reference.set(empr.getId().toString() + "	          " + empr.getName());
             this.irs1095cRef = ref;
        }             
    }	
}

