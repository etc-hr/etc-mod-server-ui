package com.etc.admin.ui.employee;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.AdminApp;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.calc.embeds.CalculationInformation;
import com.etc.corvetto.ems.calc.entities.CalculationRequest;
import com.etc.corvetto.ems.calc.entities.utils.emp.EmployeeMergerCalculation;
import com.etc.corvetto.ems.calc.rqs.CalculationRequestRequest;
import com.etc.corvetto.ems.calc.rqs.EmployeeMergerRequest;
import com.etc.corvetto.entities.Employee;
import com.etc.corvetto.entities.EmployeeInformation;
import com.etc.corvetto.entities.EmploymentPeriod;
import com.etc.corvetto.entities.Pay;
import com.etc.corvetto.entities.Person;
import com.etc.corvetto.rqs.EmployeeInformationRequest;
import com.etc.corvetto.rqs.EmployeeRequest;
import com.etc.corvetto.rqs.EmploymentPeriodRequest;
import com.etc.corvetto.rqs.PayRequest;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ViewEmployeeMergeController
{
	// Employee 1 
	@FXML
	private TextField emp1Id;
	@FXML
	private TextField ee1Name;
	@FXML
	private TextField emp1DSSN;
	@FXML
	private TextField ee1LstUpd;
	@FXML
	private TextField prsn1LstUpd;
	@FXML
	private TextField prsn1DOB;
	@FXML
	private TextField empr1Name;
	@FXML
	private TextField person1Id;
	@FXML
	private TextField emp1Status;
	// Employee 2
	@FXML
	private TextField emp2Id;
	@FXML
	private TextField ee2Name;
	@FXML
	private TextField emp2DSSN;
	@FXML
	private TextField ee2LstUpd;
	@FXML
	private TextField prsn2LstUpd;
	@FXML
	private TextField prsn2DOB;
	@FXML
	private TextField empr2Name;
	@FXML
	private TextField person2Id;
	@FXML
	private TextField emp2Status;
	// Other Controls
	@FXML
	private Button updateSSN1Check;
	@FXML
	private Button updateSSN2Check;
	@FXML
	private Button mrgEEtoEmpButton;
	
	@FXML 
	private TableView<PayCell1> emp1LstPdDtTableView; 
	@FXML 
	private TableView<EmploymentPeriodCell1> emp1EmpPrdTableView; 
	@FXML 
	private TableView<PayCell2> emp2LstPdDtTableView; 
	@FXML 
	private TableView<EmploymentPeriodCell2> emp2EmpPrdTableView; 
	
	Employee employee1 = null;
	Employee employee2 = null;
	Person person1 = null;
	Person person2 = null;
	
	TableColumn<PayCell1, String> pay1SortColumn = null; 
	TableColumn<PayCell2, String> pay2SortColumn = null; 
	TableColumn<EmploymentPeriodCell1, String> ep1SortColumn = null; 
	TableColumn<EmploymentPeriodCell2, String> ep2SortColumn = null; 
	
	List<EmploymentPeriod> empPeriods1 = null; 
	List<EmploymentPeriod> empPeriods2 = null; 
	List <EmployeeInformation> employeeInfo1 = null;
	List <EmployeeInformation> employeeInfo2 = null;
	
	Pay pay1 = null; 
	Pay pay2 = null; 
	List<Pay> pays1 = null; 
	List<Pay> pays2 = null; 

	String status1 = "";
	String status2 = "";

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
        //Disable Buttons
        mrgEEtoEmpButton.setDisable(true);
        updateSSN1Check.setDisable(true);
        updateSSN2Check.setDisable(true);
        
        emp1LstPdDtTableView.setPlaceholder(new Label(""));
        emp1EmpPrdTableView.setPlaceholder(new Label(""));
        emp2LstPdDtTableView.setPlaceholder(new Label(""));
        emp2EmpPrdTableView.setPlaceholder(new Label(""));
        
        setPay1Columns();
        setPay2Columns();
        
        setEmprPrd1Columns();
        setEmprPrd2Columns();
	}
	
	private void setPay1Columns()  
	{ 
		//clear the default values 
		emp1LstPdDtTableView.getColumns().clear(); 
 
	    TableColumn<PayCell1, String> x1 = new TableColumn<PayCell1, String>("Employee Id"); 
		x1.setCellValueFactory(new PropertyValueFactory<PayCell1,String>("employeeId1")); 
		x1.setMinWidth(90); 
		emp1LstPdDtTableView.getColumns().add(x1); 
		setPay1CellFactory(x1); 
 
	    TableColumn<PayCell1, String> x2 = new TableColumn<PayCell1, String>("Pay End"); 
	    x2.setCellValueFactory(new PropertyValueFactory<PayCell1,String>("payEndDate1")); 
	    x2.setMinWidth(136); 
		pay1SortColumn = x2; 
		x2.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		pay1SortColumn.setSortType(SortType.DESCENDING); 
	    emp1LstPdDtTableView.getColumns().add(x2); 
	    setPay1CellFactory(x2); 
	} 
 
	private void setPay2Columns()  
	{ 
		//clear the default values 
		emp2LstPdDtTableView.getColumns().clear(); 
 
	    TableColumn<PayCell2, String> x3 = new TableColumn<PayCell2, String>("Employee Id"); 
	    x3.setCellValueFactory(new PropertyValueFactory<PayCell2,String>("employeeId2")); 
		x3.setMinWidth(90); 
		emp2LstPdDtTableView.getColumns().add(x3); 
		setPay2CellFactory(x3); 
 
	    TableColumn<PayCell2, String> x4 = new TableColumn<PayCell2, String>("Pay End"); 
	    x4.setCellValueFactory(new PropertyValueFactory<PayCell2,String>("payEndDate2")); 
	    x4.setMinWidth(136); 
		pay2SortColumn = x4; 
		x4.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		pay2SortColumn.setSortType(SortType.DESCENDING); 
	    emp2LstPdDtTableView.getColumns().add(x4); 
	    setPay2CellFactory(x4); 
	} 

	private void setPay1CellFactory(TableColumn<PayCell1, String>  col)  
	{ 
		col.setCellFactory(column -> { 
		    return new TableCell<PayCell1, String>()  
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
		                PayCell1 cell = getTableView().getItems().get(getIndex()); 
		                if(cell.getPay().isActive() == false && cell.getPay().isDeleted() == false) 
		                	setTextFill(Color.BLUE); 
		                else 
		                	setTextFill(Color.BLACK); 
		            } 
		        } 
		    }; 
		}); 
	} 
	 
	private void setPay2CellFactory(TableColumn<PayCell2, String>  col)  
	{ 
		col.setCellFactory(column -> { 
		    return new TableCell<PayCell2, String>()  
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
		                PayCell2 cell = getTableView().getItems().get(getIndex()); 
		                if(cell.getPay().isActive() == false && cell.getPay().isDeleted() == false) 
		                	setTextFill(Color.BLUE); 
		                else 
		                	setTextFill(Color.BLACK); 
		            } 
		        } 
		    }; 
		}); 
	} 
	 
	private void setEmprPrd1Columns()  
	{ 
		//clear the default values 
		emp1EmpPrdTableView.getColumns().clear(); 
 
	    TableColumn<EmploymentPeriodCell1, String> x1 = new TableColumn<EmploymentPeriodCell1, String>("Employee Id"); 
		x1.setCellValueFactory(new PropertyValueFactory<EmploymentPeriodCell1,String>("employeeId1")); 
		x1.setMinWidth(90); 
		emp1EmpPrdTableView.getColumns().add(x1); 
		setEmprPrd1CellFactory(x1); 
 
	    TableColumn<EmploymentPeriodCell1, String> x2 = new TableColumn<EmploymentPeriodCell1, String>("Hire Date"); 
	    x2.setCellValueFactory(new PropertyValueFactory<EmploymentPeriodCell1,String>("hireDate1")); 
	    x2.setMinWidth(125); 
	    emp1EmpPrdTableView.getColumns().add(x2); 
	    setEmprPrd1CellFactory(x2); 
		 
	    TableColumn<EmploymentPeriodCell1, String> x3 = new TableColumn<EmploymentPeriodCell1, String>("Term Date"); 
	    x3.setCellValueFactory(new PropertyValueFactory<EmploymentPeriodCell1,String>("termDate1")); 
	    x3.setMinWidth(125); 
	    emp1EmpPrdTableView.getColumns().add(x3); 
	    setEmprPrd1CellFactory(x3); 
	} 
	 
	private void setEmprPrd2Columns()  
	{ 
		//clear the default values 
		emp2EmpPrdTableView.getColumns().clear(); 
 
	    TableColumn<EmploymentPeriodCell2, String> x1 = new TableColumn<EmploymentPeriodCell2, String>("Employee Id"); 
		x1.setCellValueFactory(new PropertyValueFactory<EmploymentPeriodCell2,String>("employeeId2")); 
		x1.setMinWidth(90); 
		emp2EmpPrdTableView.getColumns().add(x1); 
		setEmprPrd2CellFactory(x1); 
 
	    TableColumn<EmploymentPeriodCell2, String> x2 = new TableColumn<EmploymentPeriodCell2, String>("Hire Date"); 
	    x2.setCellValueFactory(new PropertyValueFactory<EmploymentPeriodCell2,String>("hireDate2")); 
	    x2.setMinWidth(125); 
	    emp2EmpPrdTableView.getColumns().add(x2); 
	    setEmprPrd2CellFactory(x2); 
		 
	    TableColumn<EmploymentPeriodCell2, String> x3 = new TableColumn<EmploymentPeriodCell2, String>("Term Date"); 
	    x3.setCellValueFactory(new PropertyValueFactory<EmploymentPeriodCell2,String>("termDate2")); 
	    x3.setMinWidth(125); 
	    emp2EmpPrdTableView.getColumns().add(x3); 
	    setEmprPrd2CellFactory(x3); 
	} 
	 
	private void setEmprPrd1CellFactory(TableColumn<EmploymentPeriodCell1, String>  col)  
	{ 
		col.setCellFactory(column -> { 
		    return new TableCell<EmploymentPeriodCell1, String>()  
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
		                EmploymentPeriodCell1 cell = getTableView().getItems().get(getIndex()); 
		                if(cell.getEmploymentPeriod().isActive() == false && cell.getEmploymentPeriod().isDeleted() == false) 
		                	setTextFill(Color.BLUE); 
		                else 
		                	setTextFill(Color.BLACK); 
		            } 
		        } 
		    }; 
		}); 
	} 
	 
	private void setEmprPrd2CellFactory(TableColumn<EmploymentPeriodCell2, String>  col)  
	{ 
		col.setCellFactory(column -> { 
		    return new TableCell<EmploymentPeriodCell2, String>()  
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
		                EmploymentPeriodCell2 cell = getTableView().getItems().get(getIndex()); 
		                if(cell.getEmploymentPeriod().isActive() == false && cell.getEmploymentPeriod().isDeleted() == false) 
		                	setTextFill(Color.BLUE); 
		                else 
		                	setTextFill(Color.BLACK); 
		            } 
		        } 
		    }; 
		}); 
	} 

	private void loadEmployee1() {
		
		try {
			EmployeeRequest employeeReq = new EmployeeRequest();
			employeeReq.setId(Long.valueOf(emp1Id.getText()));
			employee1 = AdminPersistenceManager.getInstance().get(employeeReq);

			loadEmp1Data();		
			showPerson1Data();
		} catch (Exception e) {
			Utils.showAlert("Employee 1 not Found", "An employee for coreID " + emp1Id.getText() + " could not be found. Recheck the data and try again.");
			//DataManager.i().log(Level.SEVERE, e); 
		}	
	}
	
	private void loadEmployee2() {
		
		try {
			EmployeeRequest employeeReq = new EmployeeRequest();
			employeeReq.setId(Long.valueOf(emp2Id.getText()));
			employee2 = AdminPersistenceManager.getInstance().get(employeeReq);
			
			loadEmp2Data();
			showPerson2Data();
		} catch (Exception e) {
			Utils.showAlert("Employee 2 not Found", "An employee for coreID " + emp2Id.getText() + " could not be found. Recheck the data and try again.");
			//DataManager.i().log(Level.SEVERE, e); 
		}	
	}
	
	private void showPerson1Data()
	{
		String status = "";

		try {
			EmployeeInformationRequest request = new EmployeeInformationRequest();
			request.setEmployeeId(employee1.getId());
			employeeInfo1 = AdminPersistenceManager.getInstance().getAll(request);

			for(EmployeeInformation empInfo : employeeInfo1)
			{
				if(empInfo.getEmployeeId().equals(employee1.getId()))
				{
					emp1DSSN.setText(empInfo.getDssn().toString());
				}
			}
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		
		if(employee1.isActive() == true) { status = "Active"; }
		if(employee1.isActive() == false) { status = "Inactive"; }
		
		// employee 1
  		emp1Id.setText(employee1.getId().toString());
  		ee1LstUpd.setText(Utils.getDateTimeString(employee1.getLastUpdated()));
  		
  		if(employee1.getPerson().getDateOfBirth() != null) 
  		{ 
  			prsn1DOB.setText(Utils.getDateTimeString(employee1.getPerson().getDateOfBirth())); 
  		}

  		if(employee1.getPerson().getLastUpdated() != null) 
  		{ 
  			prsn1LstUpd.setText(Utils.getDateTimeString(employee1.getPerson().getLastUpdated())); 
  		}
  		
  		ee1Name.setText(employee1.getPerson().getLastName() + ", " + employee1.getPerson().getFirstName());

  		empr1Name.setText(employee1.getEmployer().getName());
  		person1Id.setText(employee1.getPersonId().toString());
  		emp1Status.setText(status);

  		//Enable buttons
        updateSSN1Check.setDisable(false);
        
        enableButtons();
	}

	private void showPerson2Data()
	{
		String status = "";

		try {
			EmployeeInformationRequest request = new EmployeeInformationRequest();
			request.setEmployeeId(employee2.getId());
			employeeInfo2 = AdminPersistenceManager.getInstance().getAll(request);

			for(EmployeeInformation empInfo : employeeInfo2)
			{
				if(empInfo.getEmployeeId().equals(employee2.getId()))
				{
					emp2DSSN.setText(empInfo.getDssn().toString());
				}
			}
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		
		if(employee2.isActive() == true) { status = "Active"; }
		if(employee2.isActive() == false) { status = "Inactive"; }
		
		// employee 2
  		emp2Id.setText(employee2.getId().toString());
  		ee2LstUpd.setText(Utils.getDateTimeString(employee2.getLastUpdated()));
  		
  		if(employee2.getPerson().getDateOfBirth() != null) 
  		{
  			prsn2DOB.setText(Utils.getDateTimeString(employee2.getPerson().getDateOfBirth())); 
  		}

  		if(employee2.getPerson().getLastUpdated() != null) 
  		{
  			prsn2LstUpd.setText(Utils.getDateTimeString(employee2.getPerson().getLastUpdated())); 
  		}

  		ee2Name.setText(employee2.getPerson().getLastName() + ", " + employee2.getPerson().getFirstName());

  		empr2Name.setText(employee2.getEmployer().getName());
  		person2Id.setText(employee2.getPersonId().toString());
  		emp2Status.setText(status);
  		
  		//Enable buttons
        updateSSN2Check.setDisable(false);

        enableButtons();
	}
	
	private void loadEmp1Data()  
	{  
		try { 
	  
			emp1LstPdDtTableView.getItems().clear(); 
	 
			Task<Void> task = new Task<Void>()  
			{  
	            @Override  
	            protected Void call() throws Exception   
	            {  
	    			//load from the server  
	        		PayRequest request = new PayRequest(); 
	        		request.setEmployeeId(employee1.getId()); 
	        		pays1 = AdminPersistenceManager.getInstance().getAll(request); 
	 
	        		EmploymentPeriodRequest employeeReq = new EmploymentPeriodRequest(); 
	        		employeeReq.setEmployeeId(employee1.getId()); 
	        		empPeriods1 = AdminPersistenceManager.getInstance().getAll(employeeReq); 
	 
	    			EtcAdmin.i().setProgress(.75);  
	                return null;  
	            }  
	        };  

	        task.setOnSucceeded(e -> showEmp1Data());  
	    	task.setOnFailed(e -> { 
		    	DataManager.i().log(Level.SEVERE,task.getException()); 
		    	showEmp1Data(); 
		    });  
			AdminApp.getInstance().getFxQueue().put(task); 
			 
		} catch (InterruptedException e1) { e1.printStackTrace(); } 
	    catch (Exception e) { DataManager.i().logGenericException(e); } 
	}  
	 
	private void loadEmp2Data()  
	{  
		try { 
 
			EtcAdmin.i().setStatusMessage("loading Data...");  
			EtcAdmin.i().setProgress(.5);  
	  
			Task<Void> task = new Task<Void>()  
			{  
	            @Override  
	            protected Void call() throws Exception   
	            {  
	    			//load from the server  
	        		PayRequest request = new PayRequest(); 
	        		request.setEmployeeId(employee2.getId()); 
	        		pays2 = AdminPersistenceManager.getInstance().getAll(request); 
	 
	        		EmploymentPeriodRequest employeeReq = new EmploymentPeriodRequest(); 
	        		employeeReq.setEmployeeId(employee2.getId()); 
	        		empPeriods2 = AdminPersistenceManager.getInstance().getAll(employeeReq); 
	 
	        		EtcAdmin.i().setProgress(.75);  
	                return null;  
	            }  
	        };  
			EtcAdmin.i().setProgress(0);  
	    	task.setOnSucceeded(e -> showEmp2Data());  
	    	task.setOnFailed(e -> { 
		    	DataManager.i().log(Level.SEVERE,task.getException()); 
	    		showEmp2Data();  
	    	});  
			AdminApp.getInstance().getFxQueue().put(task); 
			 
		} catch (InterruptedException e1) { e1.printStackTrace(); } 
	    catch (Exception e) { DataManager.i().logGenericException(e); } 
	}  	

	private void showEmp1Data()  
	{ 
		emp1LstPdDtTableView.getItems().clear(); 
		emp1EmpPrdTableView.getItems().clear(); 
 
		if(pays1 != null && pays1.size() > 0) 
		{ 
		    for(Pay pay : pays1)  
		    { 
				if(pay.isActive() == false) continue; 
				if(pay.isDeleted() == true) continue; 
 
				if(pay.getEmployeeId().equals(employee1.getId())) 
				{ 
					emp1LstPdDtTableView.getItems().add(new PayCell1(pay)); 
				} 
		    } 

		    
		    if(pay1SortColumn!=null) 
			{
				emp1LstPdDtTableView.getSortOrder().add(pay1SortColumn);
				pay1SortColumn.setSortable(true);
	        }
		} 
		 
		if(empPeriods1 != null && empPeriods1.size() > 0) 
		{ 
		    for(EmploymentPeriod empPrd : empPeriods1)  
		    { 
				if(empPrd.isActive() == false) continue; 
				if(empPrd.isDeleted() == true) continue; 
 
				if(empPrd.getId().equals(employee1.getEmploymentPeriodId())) 
				{ 
					emp1EmpPrdTableView.getItems().add(new EmploymentPeriodCell1(empPrd)); 
				} 
		    } 
		}  
		EtcAdmin.i().setProgress(0);  
	} 
 
	private void showEmp2Data()  
	{ 
		emp2LstPdDtTableView.getItems().clear(); 
		emp2EmpPrdTableView.getItems().clear(); 
 
		if(pays2 != null && pays2.size() > 0) 
		{ 
		    for(Pay pay : pays2)  
		    { 
				if(pay.isActive() == false) continue; 
				if(pay.isDeleted() == true) continue; 
 
				if(pay.getEmployeeId().equals(employee2.getId())) 
				{ 
					emp2LstPdDtTableView.getItems().add(new PayCell2(pay)); 
				} 
		    } 

		    if(pay2SortColumn!=null) 
			{
				emp2LstPdDtTableView.getSortOrder().add(pay2SortColumn);
				pay2SortColumn.setSortable(true);
	        }
		} 
 
		if(empPeriods2 != null && empPeriods2.size() > 0) 
		{ 
		    for(EmploymentPeriod empPrd : empPeriods2)  
		    { 
				if(empPrd.isActive() == false) continue; 
				if(empPrd.isDeleted() == true) continue; 
 
				if(empPrd.getId().equals(employee2.getEmploymentPeriodId())) 
				{ 
					emp2EmpPrdTableView.getItems().add(new EmploymentPeriodCell2(empPrd)); 
				} 
		    } 
		}  
		EtcAdmin.i().setProgress(0);  
	} 
	 	

	private void enableButtons()
	{
		mrgEEtoEmpButton.setDisable(false);
	}

	@FXML
	public void onMrgEEtoEmp(ActionEvent event)
	{
	    // create a task to free up the gui thread
	    Task<Void> task = new Task<Void>() {
		    @Override protected Void call() throws Exception {
				try {
						EmployeeRequest empRequest1 = new EmployeeRequest();
						EmployeeRequest empRequest2 = new EmployeeRequest();

						EtcAdmin.i().showAppStatus("Employee Merge", "Processing Employees", 0.25, true);
						empRequest1.setEntity(employee1);
						empRequest1.setId(employee1.getId());
						empRequest1.setEmployerId(employee1.getEmployerId());
						empRequest1.setPersonId(employee1.getPersonId());
						employee1.getPerson().setMailAddress(employee1.getPerson().getMailAddress());
			
						empRequest2.setEntity(employee2);
						empRequest2.setId(employee2.getId());
						empRequest2.setEmployerId(employee2.getEmployerId());
						empRequest2.setPersonId(employee2.getPersonId());
						employee2.getPerson().setMailAddress(employee2.getPerson().getMailAddress());
			
						//EtcAdmin.i().showAppStatus("Employee Merge", "Updating Employee1 lastupdated", 0.5, true);
						// employee 1 is always the winner. Update them.
						//employee1.setLastUpdated(Calendar.getInstance());
						//empRequest1.setEntity(employee1);
						//AdminPersistenceManager.getInstance().addOrUpdate(empRequest1);
					//	if(employee1.getLastUpdated().getTimeInMillis() > employee2.getLastUpdated().getTimeInMillis()) 
					//	{ 
					//		employee2.setDeleted(true);
					//		AdminPersistenceManager.getInstance().remove(empRequest2);
					//	} else
					//	if(employee2.getLastUpdated().getTimeInMillis() > employee1.getLastUpdated().getTimeInMillis())
					//	{
					//		employee1.setDeleted(true);
					//		AdminPersistenceManager.getInstance().remove(empRequest1);
					//	}
						EtcAdmin.i().showAppStatus("Employee Merge", "Creating EmployeeMergerCalculation", 0.6, true);
			
						EmployeeMergerCalculation eCalc = new EmployeeMergerCalculation();
						eCalc.setEmployee1Id(employee1.getId());
						eCalc.setEmployee2Id(employee2.getId());
						eCalc.setSpecificationId(7l);
						eCalc.setCalculationInformation(new CalculationInformation());
						
						EmployeeMergerRequest request = new EmployeeMergerRequest();
						request.setEntity(eCalc);
						eCalc = AdminPersistenceManager.getInstance().addOrUpdate(request);
			
						EtcAdmin.i().showAppStatus("Employee Merge", "Creating Calculation Request", 0.7, true);
						//Create calc request
						if(eCalc != null)
						{
							//TODO: Requires specId, calcId, and maybe status?
							CalculationRequest calcReq = new CalculationRequest();
							calcReq.setCalculationId(eCalc.getId());
							calcReq.setSpecificationId(7l);
							calcReq.setDescription("Merge Request for Employees " + employee1.getId().toString() + " and " + employee2.getId().toString());
			
							CalculationRequestRequest calcRequest = new CalculationRequestRequest();
							calcRequest.setEntity(calcReq);
							calcReq = AdminPersistenceManager.getInstance().addOrUpdate(calcRequest);
						}
			
						// update the employees
						employee1 = eCalc.getEmployee1();
						employee2 = eCalc.getEmployee2();
						
						// update the employee information table with the chosen ssn
						// first, get the unencrypted ssn for our winner
					/*	DSSN dssn = null;
						if (employee1 != null && employee1.getPerson() != null && employee1.getPerson().getSsn()!=null)
						{
							DSSNRequest ssnRequest = new DSSNRequest();
							ssnRequest.setEmployeeId(employee1.getId());
							ssnRequest.setId(employee1.getId());
							dssn = AdminPersistenceManager.getInstance().get(ssnRequest);
						}
						if (dssn != null) {
							EtcAdmin.i().showAppStatus("Employee Merge", "Updating EmployeeInformation for Employee1", 0.8, true);
							updateEmployeeInfo(employee1, dssn.getDssn());
							EtcAdmin.i().showAppStatus("Employee Merge", "Updating EmployeeInformation for Employee2", 0.8, true);
							updateEmployeeInfo(employee2, dssn.getDssn());
						}
						*/
					} catch (Exception e) { 
						EtcAdmin.i().showAppStatus("", "", 0, false);
						DataManager.i().log(Level.SEVERE, e); 
					}

					EtcAdmin.i().showAppStatus("", "", 0, false);
					return null;
		    	}
			};
				
			task.setOnFailed(e -> {  
		    	DataManager.i().log(Level.SEVERE,task.getException());
				Utils.showAlert("Merge Failed", "The Merge Request failed to create. Check the logs for more information.");
			});

			task.setOnSucceeded(e ->  {
				showPerson1Data();
				showPerson2Data();
				Utils.showAlert("Merge Complete", "The Merge Request has been created.");
	    	});
	    	
			Thread thread = new Thread(task, "showAppStatus");
			thread.setDaemon(true);
			thread.start();		
	}

	private void updateEmployeeInfo(Employee employee, String dssn) {
		try {
			EmployeeInformationRequest request = new EmployeeInformationRequest();
			request.setEmployeeId(employee.getId());
			List<EmployeeInformation> empInfos = AdminPersistenceManager.getInstance().getAll(request);

			for(EmployeeInformation empInfo : empInfos)
			{
				empInfo.setDssn(dssn);
				EmployeeInformationRequest updateRequest = new EmployeeInformationRequest();
				updateRequest.setEntity(empInfo);
				AdminPersistenceManager.getInstance().addOrUpdate(updateRequest);
			}
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e);
		}
	}
	
	
	//Takes selected Employee Id and updates Employee.lastUpdated and Person.lastUpdated to Present time
	@FXML
	public void updatePrsn1DOB(ActionEvent event)
	{
		Calendar cal = Calendar.getInstance();

		employee1.setLastUpdated(cal);
		employee1.getPerson().setLastUpdated(cal);
		showPerson1Data();
	}

	//Takes selected Employee Id and updates Employee.lastUpdated and Person.lastUpdated to Present time
	@FXML
	public void updatePrsn2DOB(ActionEvent event)
	{
		Calendar cal = Calendar.getInstance();

		employee2.setLastUpdated(cal);
		employee2.getPerson().setLastUpdated(cal);
		showPerson2Data();
	}
	
	@FXML
	public void onSearchEmployee1(ActionEvent event)
	{
		loadEmployee1();
	}
	
	@FXML
	public void onSearchEmployee2(ActionEvent event)
	{
		loadEmployee2();
	}
	
	//Takes selected Employee Id and updates Employee.verified to true - No checks made
	@FXML
	public void onUpdateSSN1Check(ActionEvent event)
	{
		employee1.setVerified(true);
		showPerson1Data();
	}
	
	//Takes selected Employee Id and updates Employee.verified to true - No checks made
	@FXML
	public void onUpdateSSN2Check(ActionEvent event)
	{
		employee2.setVerified(true);
		showPerson2Data();
	}
	
	//Takes both selected Employee Ids and updates Employee.verified to true for both - No checks made
	@FXML
	public void onUpdBothSSNs(ActionEvent event)
	{
		employee1.setVerified(true);
		showPerson1Data();
		employee2.setVerified(true);
		showPerson2Data();
	}
	
	public class HBoxEmployee1Cell extends HBox 
	{
        Label lblPersonLastName = new Label();
        Label lblPersonFirstName = new Label();
        Label lblPersonName = new Label();
        Label lblPersonId = new Label();
        Label lblStatus = new Label();
        Label lblEmployerName = new Label();
        Employee employee;

        public String getPersonLastName() { return lblPersonLastName.getText(); } 
        public String getPersonFirstName() { return lblPersonFirstName.getText(); } 
        public String getPersonName() { return lblPersonName.getText(); } 
        public String getPersonId() { return lblPersonId.getText(); } 
        public String getStatus() { return lblStatus.getText(); } 
        public String getEmployerName() { return lblEmployerName.getText(); }
        public Employee getEmployee() { return employee; }
        public void setEmployee(Employee employee) { this.employee = employee; }
        
        public String getSearchString()
        {
        	String returnString = "";
        	if(lblPersonLastName.getText() != null && lblPersonFirstName.getText() != null)
        	{
            	returnString = lblPersonLastName.getText() + ", " + lblPersonFirstName.getText() + " " + lblPersonId.getText() + " " + lblEmployerName.getText();
        	}
        	return returnString;
        } 

        @Override
        public String toString() {
        	return getSearchString();
        }

        HBoxEmployee1Cell(Employee employee) 
        {
             super();
             this.employee = employee;

             lblPersonLastName.setText(employee.getPerson().getLastName());
             lblPersonLastName.setMinWidth(300);
             lblPersonLastName.setMaxWidth(300);
             lblPersonLastName.setPrefWidth(300);
             HBox.setHgrow(lblPersonLastName, Priority.ALWAYS);

             lblPersonFirstName.setText(employee.getPerson().getFirstName());
             lblPersonFirstName.setMinWidth(300);
             lblPersonFirstName.setMaxWidth(300);
             lblPersonFirstName.setPrefWidth(300);
             HBox.setHgrow(lblPersonFirstName, Priority.ALWAYS);

             lblPersonName.setText(employee.getPerson().getLastName() + ", " + employee.getPerson().getFirstName());
             lblPersonName.setMinWidth(300);
             lblPersonName.setMaxWidth(300);
             lblPersonName.setPrefWidth(300);
             HBox.setHgrow(lblPersonLastName, Priority.ALWAYS);

             lblPersonId.setText(String.valueOf(employee.getPerson().getId()));
             lblPersonId.setMinWidth(100);
             lblPersonId.setMaxWidth(100);
             lblPersonId.setPrefWidth(100);
             HBox.setHgrow(lblPersonId, Priority.ALWAYS);

             lblStatus.setText(String.valueOf(status1));
             lblStatus.setMinWidth(100);
             lblStatus.setMaxWidth(100);
             lblStatus.setPrefWidth(100);
             HBox.setHgrow(lblPersonId, Priority.ALWAYS);

             lblEmployerName.setText(employee.getEmployer().getName());
             lblEmployerName.setMinWidth(300);
             lblEmployerName.setMaxWidth(300);
             lblEmployerName.setPrefWidth(300);
             HBox.setHgrow(lblEmployerName, Priority.ALWAYS);

          	 this.getChildren().addAll(lblPersonName, lblPersonId, lblStatus, lblEmployerName);
        }
    }	
	
	public class HBoxEmployee2Cell extends HBox 
	{
        Label lblPersonLastName = new Label();
        Label lblPersonFirstName = new Label();
        Label lblPersonName = new Label();
        Label lblPersonId = new Label();
        Label lblStatus = new Label();
        Label lblEmployerName = new Label();
        Employee employee;

        public String getPersonLastName() { return lblPersonLastName.getText(); } 
        public String getPersonFirstName() { return lblPersonFirstName.getText(); } 
        public String getPersonName() { return lblPersonName.getText(); } 
        public String getPersonId() { return lblPersonId.getText(); } 
        public String getStatus() { return lblStatus.getText(); } 
        public String getEmployerName() { return lblEmployerName.getText(); }
        public Employee getEmployee() { return employee; }
        public void setEmployee(Employee employee) { this.employee = employee; }

        public String getSearchString()
        {
        	String returnString = "";
        	if(lblPersonLastName.getText() != null && lblPersonFirstName.getText() != null)
        	{
            	returnString = lblPersonLastName.getText() + ", " + lblPersonFirstName.getText() + " " + lblPersonId.getText() + " " + lblEmployerName.getText();
        	}
        	return returnString;
        } 

        @Override
        public String toString() {
        	return getSearchString();
        }

        HBoxEmployee2Cell(Employee employee) 
        {
             super();
             this.employee = employee;

             lblPersonLastName.setText(employee.getPerson().getLastName());
             lblPersonLastName.setMinWidth(300);
             lblPersonLastName.setMaxWidth(300);
             lblPersonLastName.setPrefWidth(300);
             HBox.setHgrow(lblPersonLastName, Priority.ALWAYS);

             lblPersonFirstName.setText(employee.getPerson().getFirstName());
             lblPersonFirstName.setMinWidth(300);
             lblPersonFirstName.setMaxWidth(300);
             lblPersonFirstName.setPrefWidth(300);
             HBox.setHgrow(lblPersonFirstName, Priority.ALWAYS);

             lblPersonName.setText(employee.getPerson().getLastName() + ", " + employee.getPerson().getFirstName());
             lblPersonName.setMinWidth(300);
             lblPersonName.setMaxWidth(300);
             lblPersonName.setPrefWidth(300);
             HBox.setHgrow(lblPersonLastName, Priority.ALWAYS);

             lblPersonId.setText(String.valueOf(employee.getPerson().getId()));
             lblPersonId.setMinWidth(100);
             lblPersonId.setMaxWidth(100);
             lblPersonId.setPrefWidth(100);
             HBox.setHgrow(lblPersonId, Priority.ALWAYS);

             lblStatus.setText(status2);
             lblStatus.setMinWidth(100);
             lblStatus.setMaxWidth(100);
             lblStatus.setPrefWidth(100);
             HBox.setHgrow(lblPersonId, Priority.ALWAYS);

             lblEmployerName.setText(employee.getEmployer().getName());
             lblEmployerName.setMinWidth(300);
             lblEmployerName.setMaxWidth(300);
             lblEmployerName.setPrefWidth(300);
             HBox.setHgrow(lblEmployerName, Priority.ALWAYS);

          	 this.getChildren().addAll(lblPersonName, lblPersonId, lblStatus, lblEmployerName);
        }
    }	

	public static class PayCell1
	{
		SimpleStringProperty employeeId1 = new SimpleStringProperty();
		SimpleStringProperty payEndDate1 = new SimpleStringProperty();

		Pay pay;
		
		Pay getPay() { return pay; }

	    public String getEmployeeId1() { return employeeId1.get(); }
	       
	    public String getPayEndDate1() { return payEndDate1.get(); }
	       
	    PayCell1(Pay pay) 
	    {
	        super();
	        
	        this.pay = pay;

	        if(pay != null) 
	        {
	        	employeeId1.set(String.valueOf(pay.getEmployeeId()));
	        	payEndDate1.set(Utils.getDateString(pay.getPayPeriod().getBornOn()));
	        }
	    }
	}
	
	public static class PayCell2 
	{
		SimpleStringProperty employeeId2 = new SimpleStringProperty();
		SimpleStringProperty payEndDate2 = new SimpleStringProperty();

		Pay pay;
		
		Pay getPay() { return pay; }

	    public String getEmployeeId2() { return employeeId2.get(); }
	       
	    public String getPayEndDate2() { return payEndDate2.get(); }
	       
	    PayCell2(Pay pay) 
	    {
	        super();

	        this.pay = pay;

	        if(pay != null) 
	        {
	        	employeeId2.set(String.valueOf(pay.getEmployeeId()));
	        	payEndDate2.set(Utils.getDateString(pay.getPayPeriod().getBornOn()));
	        }
	    }
	}
	
	public static class EmploymentPeriodCell1
	{
		SimpleStringProperty employeeId1 = new SimpleStringProperty();
		SimpleStringProperty hireDate1 = new SimpleStringProperty();
		SimpleStringProperty termDate1 = new SimpleStringProperty();

		EmploymentPeriod employmentPeriod;

		EmploymentPeriod getEmploymentPeriod() { return employmentPeriod; }
	
	    public String getEmployeeId1() { return employeeId1.get(); }
	       
	    public String getHireDate1() { return hireDate1.get(); }
	       
	    public String getTermDate1() { return termDate1.get(); }

	    EmploymentPeriodCell1(EmploymentPeriod employmentPeriod) 
	    {
	        super();

	        this.employmentPeriod = employmentPeriod;

	        if(employmentPeriod != null) 
	        {
	        	employeeId1.set(String.valueOf(employmentPeriod.getId()));
	        	hireDate1.set(Utils.getDateString(employmentPeriod.getHireDate()));
	        	if(employmentPeriod.getTermDate() != null) { termDate1.set(Utils.getDateString(employmentPeriod.getTermDate())); }
	        }
	    }
	}

	public static class EmploymentPeriodCell2 
	{
		SimpleStringProperty employeeId2 = new SimpleStringProperty();
		SimpleStringProperty hireDate2 = new SimpleStringProperty();
		SimpleStringProperty termDate2 = new SimpleStringProperty();

		EmploymentPeriod employmentPeriod;

		EmploymentPeriod getEmploymentPeriod() { return employmentPeriod; }
	
	    public String getEmployeeId2() { return employeeId2.get(); }
	       
	    public String getHireDate2() { return hireDate2.get(); }
	       
	    public String getTermDate2() { return termDate2.get(); }

	    EmploymentPeriodCell2(EmploymentPeriod employmentPeriod) 
	    {
	        super();

	        this.employmentPeriod = employmentPeriod;

	        if(employmentPeriod != null) 
	        {
	        	employeeId2.set(String.valueOf(employmentPeriod.getId()));
	        	hireDate2.set(Utils.getDateString(employmentPeriod.getHireDate()));
	        	if(employmentPeriod.getTermDate() != null) { termDate2.set(Utils.getDateString(employmentPeriod.getTermDate())); }
	        }
	    }
	}

	@FXML
	private void onExit(ActionEvent event) 
	{
		Stage stage = (Stage) emp1Id.getScene().getWindow();
		stage.close();		
	}
}
