package com.etc.admin.ui.admintools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import org.apache.commons.lang3.BooleanUtils;

import com.etc.CoreException;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.Employee;
import com.etc.corvetto.entities.EmployeeBatch;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.TaxYear;
import com.etc.corvetto.entities.Process;
import com.etc.corvetto.entities.TaxPeriod;
import com.etc.corvetto.rqs.EmployeeBatchRequest;
import com.etc.corvetto.rqs.EmployeeRequest;
import com.etc.corvetto.rqs.ProcessRequest;
import com.etc.corvetto.rqs.TaxPeriodRequest;
import com.etc.corvetto.utils.types.FilingType;
import com.etc.entities.DataProperty;
import com.etc.entities.DataPropertyType;
import com.etc.rqs.DataPropertyRequest;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;

import javafx.stage.Stage;

public class ViewAdminEmployeeToolController 
{

	@FXML
	private TableView<DataCell> tableView;
	@FXML
	private Button callCodesButton;
	@FXML
	private Button togglePayButton;
	@FXML
	private ComboBox<String> taxyearCombo;
	
	//table sort 
	TableColumn<DataCell, String> sortColumn = null;
	ContextMenu contextMenu = null;
	
	List<Employee> employees = null;
	List<Long> idList = new ArrayList<Long>();
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	@FXML
	public void initialize() 
	{
		initControls();
	}
	
	private void initControls() 
	{
		// set the pay table columns
		setTableColumns();
		
		enableButtons(false);
		fillTaxYearCombo();
		
		// CONTEXT MENU
		contextMenu  = new ContextMenu();
		MenuItem mi1 = new MenuItem("Something cool here");
		mi1.setOnAction(new EventHandler<ActionEvent>() 
		{
	        @Override
	        public void handle(ActionEvent t)
	        {
	        	if (tableView.getSelectionModel().getSelectedItems() != null) 
	        	{
	        		//toggleSelectedPaysActivation();
	        	} else
	        		Utils.showAlert("No Selection", "There are no items selected. Please select targets by using shift-click, ctrl-A, or the Select All button");
	        }
	    });
		contextMenu.getItems().add(mi1);
		contextMenu.setAutoHide(true);
	}

	private void enableButtons(Boolean state)
	{
		callCodesButton.setDisable(!state);
		togglePayButton.setDisable(!state);
		taxyearCombo.setDisable(!state);
	}
	
	private void setTableColumns() 
	{
		//clear the default values
		tableView.getColumns().clear();

	    TableColumn<DataCell, String> x1 = new TableColumn<DataCell, String>("Id");
		x1.setCellValueFactory(new PropertyValueFactory<DataCell,String>("employeeId"));
		x1.setMinWidth(100);
		sortColumn = x1;
		sortColumn.setSortType(SortType.ASCENDING);
		tableView.getColumns().add(x1);
		setCellFactory(x1);
		
	    TableColumn<DataCell, String> x2 = new TableColumn<DataCell, String>("First Name");
		x2.setCellValueFactory(new PropertyValueFactory<DataCell,String>("firstName"));
		x2.setMinWidth(100);
		tableView.getColumns().add(x2);
		setCellFactory(x2);
		
	    TableColumn<DataCell, String> x3 = new TableColumn<DataCell, String>("Last Name");
		x3.setCellValueFactory(new PropertyValueFactory<DataCell,String>("lastName"));
		x3.setMinWidth(100);
		tableView.getColumns().add(x3);
		setCellFactory(x3);

	    TableColumn<DataCell, String> x4 = new TableColumn<DataCell, String>("Employer");
		x4.setCellValueFactory(new PropertyValueFactory<DataCell,String>("employer"));
		x4.setMinWidth(250);
		tableView.getColumns().add(x4);
		setCellFactory(x4);
	
	}

	private void setCellFactory(TableColumn<DataCell, String>  col) 
	{
		col.setCellFactory(column -> 
		{
		    return new TableCell<DataCell, String>() 
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
		                DataCell cell = getTableView().getItems().get(getIndex());
		               // if(cell.getPay().isActive() == false)
		               // 	setTextFill(Color.BLUE);
		               // else
		               // 	setTextFill(Color.BLACK);
		            }
		        }
		    };
		});
	}
	
	private void fillTaxYearCombo()
	{

		try {
			TaxPeriodRequest request = new TaxPeriodRequest();
			request.setOrganizationId(DataManager.i().mLocalUser.getOrganizationId());
			List<TaxPeriod> taxPeriods = AdminPersistenceManager.getInstance().getAll(request);
			Collections.sort(taxPeriods, (o1, o2) -> (Long.valueOf(o2.getYear()).compareTo(Long.valueOf(o1.getYear())))); 
			for (TaxPeriod tp : taxPeriods) {
				if (tp.getFilingType().equals(FilingType.FEDERAL) == false) continue;
				taxyearCombo.getItems().add(String.valueOf(tp.getYear()));
			}	
		} catch (Exception e) {
			DataManager.i().log(Level.SEVERE,e);
		}
	}

    @FXML
    void onPasteData(ActionEvent event)
    {
    	try {
    		tableView.getItems().clear();
    		// get the clipboard data into a local string
			final Clipboard clipboard = Clipboard.getSystemClipboard();
			String localContent = clipboard.getString();
			
			// parse through our data
			String[] tokens = localContent.split("\r\n");

			for (String t : tokens) {
				if (Utils.isInt(t) == false) {
					
					// confirm first
					Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
				    confirmAlert.setTitle("Data Error");
				    String confirmText = "The value \"" + t + "\" is not numeric and will not be used. Do you wish to continue?";
				    confirmAlert.setContentText(confirmText);
				    EtcAdmin.i().positionAlertCenter(confirmAlert);
				    Optional<ButtonType> result = confirmAlert.showAndWait();
				    if ((result.isPresent()) && (result.get() != ButtonType.OK))
				    	return;
				    else
				    	continue;
				}
				tableView.getItems().add(new DataCell(t));
			}
			
	     }catch (Exception e){
	    	 Utils.showAlert("No Clipboard Data", "Please copy employee data before pasting.");
	     }
    }
    
    @FXML
    void onClear(ActionEvent event)
    {
    	try {
    		tableView.getItems().clear();
	     }catch (Exception e){
			DataManager.i().log(Level.SEVERE,e);
	     }
    }
    
	@FXML
	private void onLookup(ActionEvent event) {
		
		try {
			EmployeeBatch batch = new EmployeeBatch();
			
			EmployeeRequest req = new EmployeeRequest();
			idList.clear();
			for (DataCell cell : tableView.getItems()) {
				idList.add(Long.valueOf(cell.getEmployeeId()));
			}
			
			req.setIdList(idList);
			employees = AdminPersistenceManager.getInstance().getAll(req);
			// clear the table
			tableView.getItems().clear();
			//add the employees and create a verified list
			idList.clear();
			for (Employee emp : employees) {
				tableView.getItems().add(new DataCell(emp));
				idList.add(emp.getId());
			}
			// enable further action buttons
			if (employees != null && employees.size() > 0) {
				enableButtons(true);
			}
			
		} catch (CoreException e) {
			DataManager.i().log(Level.SEVERE,e);
		}

	}	
	
	@FXML
	private void onTogglePay(ActionEvent event) {
	/*	try {
			
			EmployeeBatch batch = new EmployeeBatch();
			batch.setEmployees(employees);
			batch.setCreatedBy(DataManager.i().mLocalUser);
			batch.setCreatedById(DataManager.i().mLocalUser.getId());
			EmployeeBatchRequest req = new EmployeeBatchRequest();
			req.setEntity(batch);
			req.setEmployeeIdList(idList);
			AdminPersistenceManager.getInstance().addOrUpdate(req);
						
			//now the process request, passing up the taxYear
			DataProperty dp = new DataProperty();
			dp.setName("Tax Year");
			dp.setValue(taxYear.getId().toString());
			DataPropertyType dpType = new DataPropertyType();
			dpType.setId(3l);
			dp.setPropertyType(dpType);
			List<DataProperty> dpList = new ArrayList<DataProperty>();
			dpList.add(dp);
			DataPropertyRequest dpReq = new DataPropertyRequest();
			dpReq.setEntity(dp);

			ProcessRequest pr = new ProcessRequest();
			//pr.add(dp, DataManager.i().mLocalUser);
			
		} catch (CoreException e) {
			DataManager.i().log(Level.SEVERE,e);
		}
		*/
	}	
	
	@FXML
	private void onCallCodes(ActionEvent event) {
		try {
				
			if (taxyearCombo.getSelectionModel().getSelectedIndex() < 0 ) {
				Utils.showAlert("Tax Year Required", "Please select a tax year before calling codes");
				return;
			}
			
			String taxYear = taxyearCombo.getValue();
			
			// Create the batch
			EmployeeBatch batch = new EmployeeBatch();
			batch.setEmployees(employees);
			batch.setCreatedBy(DataManager.i().mLocalUser);
			batch.setCreatedById(DataManager.i().mLocalUser.getId());
			// now set the template - hardcoded for now
			batch.setTemplateId(1L);
			// set the taxyear
			batch.setCustomField1(taxYear);
			// create the request
			EmployeeBatchRequest req = new EmployeeBatchRequest();
			req.setEntity(batch);
			req.setEmployeeIdList(idList);
			// need a processType id
			req.setProcessTypeId(3L);
			// and send it
			batch = AdminPersistenceManager.getInstance().addOrUpdate(req);
			if (batch != null)	
				Utils.showAlert("Call Codes Submitted", "Call codes were submitted for " + employees.size() +  " employee(s) for the tax year " + taxYear + ".");
		} catch (CoreException e) {
			DataManager.i().log(Level.SEVERE,e);
			Utils.showAlert("Error", "There was a problem with calling codes.");
		}
		
	}	
	
	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) tableView.getScene().getWindow();
		stage.close();
	}
	
	public static class DataCell 
	{
		
		SimpleStringProperty employeeId = new SimpleStringProperty();
		SimpleStringProperty firstName = new SimpleStringProperty();
		SimpleStringProperty lastName = new SimpleStringProperty();
		SimpleStringProperty employer = new SimpleStringProperty();
		Employee employee;
	
	
	    public String getEmployeeId() { return employeeId.get(); }       
	    public String getFirstName() { return firstName.get(); }       
	    public String getLastName() { return lastName.get(); }       
	    public String getEmployer() { return employer.get(); }       

	    public Employee getEmployee() { return employee; }
	    
	    // set data
	    public void setEmployee(Employee employee) {
	    	this.employee = employee;
	    	if (employee != null && employee.getPerson() != null) {
	    		firstName.set(employee.getPerson().getFirstName());
	    		lastName.set(employee.getPerson().getLastName());
	    	}
	    }
	       
	    DataCell(Employee employee) 
	    {
	         super();
	
	         this.employee = employee;
	         if(employee != null) 
	         {
	        	 employeeId.set(String.valueOf(employee.getId()));
	 	    	if (employee != null && employee.getPerson() != null) {
		    		firstName.set(employee.getPerson().getFirstName());
		    		lastName.set(employee.getPerson().getLastName());
		    	}
	 	    	if (employee != null && employee.getEmployer() != null) {
		    		employer.set(employee.getEmployer().getName());
		    	}
	         }
	    }
	    
	    DataCell(Long id) 
	    {
	         super();
	
	         if(id != null) 
	         {
	        	 employeeId.set(id.toString());
	         }
	    }
	    
	    DataCell(String id) 
	    {
	         super();
	
	         if(id != null) 
	         {
	        	 employeeId.set(id);
	         }
	    }
	}	
}


