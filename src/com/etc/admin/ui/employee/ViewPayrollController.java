package com.etc.admin.ui.employee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.Employee;
import com.etc.corvetto.entities.Pay;
import com.etc.corvetto.entities.PayGap;
import com.etc.corvetto.entities.PayPeriod;
import com.etc.corvetto.rqs.EmployeeRequest;
import com.etc.corvetto.rqs.PayGapRequest;
import com.etc.corvetto.rqs.PayRequest;
import com.etc.corvetto.utils.types.PayCodeType;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewPayrollController 
{

	@FXML
	private ComboBox<String> empPayCompTypeReplace;
	@FXML
	private TextField empPayHoursReplace;
	@FXML
	private Button empPayReset;
	@FXML
	private Button empPaySave;
	@FXML
	private Button empPaySaving;
	@FXML
	private CheckBox empInactivePaysCheck;
	@FXML
	private CheckBox empDeletedPaysCheck;
	@FXML
	private CheckBox empUnprocessedPaysCheck;
	@FXML
	private CheckBox empAllEmployersCheck;
	@FXML
	private TableView<PayCell> empPayTableView;
	@FXML
	private Label empPayLabel;
	
	int waitingToComplete = 0;
	String payCompTypeReplaceData = "";
	String payHoursReplaceData = "";
	
	//table sort 
	TableColumn<PayCell, String> sortColumn = null;
	ContextMenu paysCM = null;
	
	List<PayGap> paygaps = null;
	List<Pay> pays = null;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	@FXML
	public void initialize() 
	{
		initControls();
		updatePaysData();
	}
	
	private void initControls() 
	{
		payCompTypeReplaceData = ""; 
		// set the pay table columns
		setPayTableColumns();
		
		empPayTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	
		// replacement drop down for pays
		empPayCompTypeReplace.getItems().clear();
		empPayCompTypeReplace.getItems().add("FTH");
		empPayCompTypeReplace.getItems().add("VH");
		
		// PAYS CONTEXT MENU
		paysCM  = new ContextMenu();
		MenuItem mi1 = new MenuItem("Toggle Pay Active/Inactive");
		mi1.setOnAction(new EventHandler<ActionEvent>() 
		{
	        @Override
	        public void handle(ActionEvent t)
	        {
	        	if (empPayTableView.getSelectionModel().getSelectedItems() != null) 
	        	{
	        	    Task<Void> task = new Task<Void>() {
	        		    @Override protected Void call() throws Exception {
	        				try {
				        		int count = empPayTableView.getSelectionModel().getSelectedItems().size();
				        		int ticker = 0;
				        		for (PayCell pc : empPayTableView.getSelectionModel().getSelectedItems()) {
				        			ticker++;
					        		EtcAdmin.i().showAppStatus("Setting Pay Activation Status", "Updating Activation for Pay Id# " + pc.getPay().getId().toString(), ticker / count, true);
				        			togglePayActivation(pc.getPay());
				        		}
				        		EtcAdmin.i().showAppStatus("Setting Pay Activation Status", "Updating Pay List", 1, true);
					        	updatePaysData();
				        		EtcAdmin.i().hideAppStatus();
	        				}
	        			    catch (Exception e) {  DataManager.i().logGenericException(e); }
	        				return null;
		        		}
	        	    };
	        		Thread thread = new Thread(task, "showAppStatus");
	        		thread.setDaemon(true);
	        		thread.start();		
	        	}
	        }
	    });
		paysCM.getItems().add(mi1);
		paysCM.setAutoHide(true);

		// EMPLOYEE PAYS
		empPayTableView.setOnMouseClicked(mouseClickedEvent -> 
		{
			paysCM.hide();
			if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1)
			{
				if (empPayTableView.getSelectionModel().getSelectedItem() == null || empPayTableView.getSelectionModel().getSelectedItem().getPay() == null)
					return;
            	//set the selected coverage
				DataManager.i().mPay = empPayTableView.getSelectionModel().getSelectedItem().getPay();
				
				// and load the pop up
				try {
		            // load the fxml
			        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employee/ViewPay.fxml"));
					Parent ControllerNode = loader.load();
			        ViewPayController payController = (ViewPayController) loader.getController();

			        Stage stage = new Stage();
			        stage.initModality(Modality.APPLICATION_MODAL);
			        stage.initStyle(StageStyle.UNDECORATED);
			        stage.setScene(new Scene(ControllerNode));  
			        EtcAdmin.i().positionStageCenter(stage);
			        stage.showAndWait();
			        // reload the pays if a change was made
			        if(payController.changesMade == true)
			        	updatePaysData();
				} catch (IOException e) {
		        	DataManager.i().log(Level.SEVERE, e); 
				}        		
			    catch (Exception e) {  DataManager.i().logGenericException(e); }
            }
            if(mouseClickedEvent.getButton().equals(MouseButton.SECONDARY) && mouseClickedEvent.getClickCount() < 2) 
            {
            	if (empPayTableView.getSelectionModel().getSelectedItem() != null)
            		paysCM.show(empPayTableView, mouseClickedEvent.getScreenX(), mouseClickedEvent.getScreenY());
            }
        });

		empPayCompTypeReplace.getSelectionModel().clearSelection();
		empPayHoursReplace.clear();
		//empPayReset.setVisible(false);
		empPaySave.setVisible(false);
		empPaySaving.setVisible(false);
	}
	
	private void togglePayActivation(Pay pay) {
		try {
			pay.setActive(!pay.isActive());
			PayRequest payRequest = new PayRequest();
			payRequest.setId(pay.getId());
			payRequest.setEntity(pay);
			AdminPersistenceManager.getInstance().addOrUpdate(payRequest);
		} catch (CoreException e) {
			DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void setPayTableColumns() 
	{
		//clear the default values
		empPayTableView.getColumns().clear();

/*	    TableColumn<PayCell, String> x1 = new TableColumn<PayCell, String>("Core Id");
		x1.setCellValueFactory(new PropertyValueFactory<PayCell,String>("id"));
		x1.setMinWidth(100);
		empPayTableView.getColumns().add(x1);
		setCellFactory(x1);
*/		
		/*TableColumn<PayCell, String> x2 = new TableColumn<PayCell, String>("Date");
		x2.setCellValueFactory(new PropertyValueFactory<PayCell,String>("date"));
		x2.setMinWidth(110);
		x2.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		empPayTableView.getColumns().add(x2);
		setCellFactory(x2);
*/
	    TableColumn<PayCell, String> x16 = new TableColumn<PayCell, String>("Employer");
		x16.setCellValueFactory(new PropertyValueFactory<PayCell,String>("employer"));
		x16.setMinWidth(250);
		empPayTableView.getColumns().add(x16);
		setCellFactory(x16);
		
	    TableColumn<PayCell, String> x3 = new TableColumn<PayCell, String>("Comp Type");
		x3.setCellValueFactory(new PropertyValueFactory<PayCell,String>("compType"));
		x3.setMinWidth(100);
		empPayTableView.getColumns().add(x3);
		setCompTypeCellFactory(x3);
		
	    TableColumn<PayCell, String> x17 = new TableColumn<PayCell, String>("CustFld 1");
		x17.setCellValueFactory(new PropertyValueFactory<PayCell,String>("customField1"));
		x17.setMinWidth(100);
		empPayTableView.getColumns().add(x17);
		setCellFactory(x17);
		
	    TableColumn<PayCell, String> x18 = new TableColumn<PayCell, String>("CustFld 2");
		x18.setCellValueFactory(new PropertyValueFactory<PayCell,String>("customField2"));
		x18.setMinWidth(100);
		empPayTableView.getColumns().add(x18);
		setCellFactory(x18);
		
	    TableColumn<PayCell, String> x6 = new TableColumn<PayCell, String>("Hours");
		x6.setCellValueFactory(new PropertyValueFactory<PayCell,String>("hours"));
		x6.setMinWidth(100);
		empPayTableView.getColumns().add(x6);
		setHoursCellFactory(x6);

	    TableColumn<PayCell, String> x7 = new TableColumn<PayCell, String>("Amount");
		x7.setCellValueFactory(new PropertyValueFactory<PayCell,String>("amount"));
		x7.setMinWidth(100);
		empPayTableView.getColumns().add(x7);
		setCellFactory(x7);
		
	    TableColumn<PayCell, String> x8 = new TableColumn<PayCell, String>("Rate");
		x8.setCellValueFactory(new PropertyValueFactory<PayCell,String>("rate"));
		x8.setMinWidth(100);
		empPayTableView.getColumns().add(x8);
		setCellFactory(x8);
		
/*	    TableColumn<PayCell, String> x9 = new TableColumn<PayCell, String>("PP_ID");
		x9.setCellValueFactory(new PropertyValueFactory<PayCell,String>("payPeriodId"));
		x9.setMinWidth(100);
		empPayTableView.getColumns().add(x9);
		setCellFactory(x9);
*/		
	    TableColumn<PayCell, String> x10 = new TableColumn<PayCell, String>("Pay Start");
		x10.setCellValueFactory(new PropertyValueFactory<PayCell,String>("startDate"));
		x10.setMinWidth(110);
		x10.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		//initial sort is by start date
		sortColumn = x10;
		sortColumn.setSortType(SortType.DESCENDING);
		empPayTableView.getColumns().add(x10);
		setCellFactory(x10);
		
	    TableColumn<PayCell, String> x11 = new TableColumn<PayCell, String>("Pay End");
		x11.setCellValueFactory(new PropertyValueFactory<PayCell,String>("endDate"));
		x11.setMinWidth(110);
		x11.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		empPayTableView.getColumns().add(x11);
		setCellFactory(x11);
		
	    TableColumn<PayCell, String> x12 = new TableColumn<PayCell, String>("Pay Date");
		x12.setCellValueFactory(new PropertyValueFactory<PayCell,String>("payDate"));
		x12.setMinWidth(110);
		x12.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		empPayTableView.getColumns().add(x12);
		setCellFactory(x12);
		
	    TableColumn<PayCell, String> x13 = new TableColumn<PayCell, String>("Pay Freq");
		x13.setCellValueFactory(new PropertyValueFactory<PayCell,String>("payFreq"));
		x13.setMinWidth(125);
		empPayTableView.getColumns().add(x13);
		setCellFactory(x13);
		
	    TableColumn<PayCell, String> xUnionType = new TableColumn<PayCell, String>("Union Type");
	    xUnionType.setCellValueFactory(new PropertyValueFactory<PayCell,String>("unionType"));
	    xUnionType.setMinWidth(100);
		empPayTableView.getColumns().add(xUnionType);
		setCellFactory(xUnionType);
		
	    TableColumn<PayCell, String> x14 = new TableColumn<PayCell, String>("Pay Class");
		x14.setCellValueFactory(new PropertyValueFactory<PayCell,String>("payClass"));
		x14.setMinWidth(100);
		empPayTableView.getColumns().add(x14);
		setCellFactory(x14);
		
/*	    TableColumn<PayCell, String> x15 = new TableColumn<PayCell, String>("Processed");
		x15.setCellValueFactory(new PropertyValueFactory<PayCell,String>("processed"));
		x15.setMinWidth(100);
		empPayTableView.getColumns().add(x15);
		setCellFactory(x15);
*/		
	    TableColumn<PayCell, String> xTypeChanged = new TableColumn<PayCell, String>("Type Changed");
	    xTypeChanged.setCellValueFactory(new PropertyValueFactory<PayCell,String>("typeChanged"));
	    xTypeChanged.setMinWidth(125);
		empPayTableView.getColumns().add(xTypeChanged);
		setCellFactory(xTypeChanged);
		
	    TableColumn<PayCell, String> xTypeChangeVerified = new TableColumn<PayCell, String>("Type Change Verified");
	    xTypeChangeVerified.setCellValueFactory(new PropertyValueFactory<PayCell,String>("typeChangeVerified"));
	    xTypeChangeVerified.setMinWidth(175);
		empPayTableView.getColumns().add(xTypeChangeVerified);
		setCellFactory(xTypeChangeVerified);
		
	    TableColumn<PayCell, String> x19 = new TableColumn<PayCell, String>("CustFld 3");
		x19.setCellValueFactory(new PropertyValueFactory<PayCell,String>("customField3"));
		x19.setMinWidth(100);
		empPayTableView.getColumns().add(x19);
		setCellFactory(x19);
		
	    TableColumn<PayCell, String> x20 = new TableColumn<PayCell, String>("CustFld 4");
		x20.setCellValueFactory(new PropertyValueFactory<PayCell,String>("customField4"));
		x20.setMinWidth(100);
		empPayTableView.getColumns().add(x20);
		setCellFactory(x20);
		
	    TableColumn<PayCell, String> x21 = new TableColumn<PayCell, String>("CustFld 5");
		x21.setCellValueFactory(new PropertyValueFactory<PayCell,String>("customField5"));
		x21.setMinWidth(100);
		empPayTableView.getColumns().add(x21);
		setCellFactory(x21);
		
	    TableColumn<PayCell, String> x22 = new TableColumn<PayCell, String>("CustFld 6");
		x22.setCellValueFactory(new PropertyValueFactory<PayCell,String>("customField6"));
		x22.setMinWidth(100);
		empPayTableView.getColumns().add(x22);
		setCellFactory(x22);
		
	    TableColumn<PayCell, String> x23 = new TableColumn<PayCell, String>("CustFld 7");
		x23.setCellValueFactory(new PropertyValueFactory<PayCell,String>("customField7"));
		x23.setMinWidth(100);
		empPayTableView.getColumns().add(x23);
		setCellFactory(x23);
	}

	private void setCellFactory(TableColumn<PayCell, String>  col) 
	{
		col.setCellFactory(column -> 
		{
		    return new TableCell<PayCell, String>() 
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
		                PayCell cell = getTableView().getItems().get(getIndex());
		                if (cell == null) {
		                	setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
		                } else {
		                	if (cell.getPay() == null ) {
		                		setTextFill(Color.SALMON);
		                		//setBackground(new Background(new BackgroundFill(Color.SALMON, CornerRadii.EMPTY, Insets.EMPTY)));
		                	}else if(cell.getPay().isActive() == false)
			                	setTextFill(Color.BLUE);
			                else {
			                	if (cell.getTypeChanged() == "true")
			                		setTextFill(Color.TEAL);
			                	else
			                		setTextFill(Color.BLACK);
			                }
		                }
		            }
		        }
		    };
		});
	}

	private void setHoursCellFactory(TableColumn<PayCell, String>  col) 
	{
		col.setCellFactory(column -> 
		{
		    return new TableCell<PayCell, String>() 
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
		                PayCell cell = getTableView().getItems().get(getIndex());
		                if(cell.getPay().isActive() == false)
		                	setTextFill(Color.BLUE);
		                else
			                if(cell.getPay().isActive() == false)
			                	setTextFill(Color.BLUE);
			                else {
			                	if (cell.getTypeChanged() == "true")
			                		setTextFill(Color.TEAL);
			                	else
			                		setTextFill(Color.BLACK);
			                }
		                // change color if it's an edit
		                if(cell.getReplaceHours() == true)
		                	setTextFill(Color.RED);
		            }
		        }
		    };
		});
	}
	
	private void setCompTypeCellFactory(TableColumn<PayCell, String>  col) 
	{
		col.setCellFactory(column -> 
		{
		    return new TableCell<PayCell, String>() 
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
		                PayCell cell = getTableView().getItems().get(getIndex());
		                if(cell.getPay().isActive() == false)
		                	setTextFill(Color.BLUE);
		                else
		                	setTextFill(Color.BLACK);
		                // change color if it's an edit
		                if(cell.getReplaceCompType() == true)
		                	setTextFill(Color.RED);
		            }
		        }
		    };
		});
	}
	
	private void updatePaysData() 
	{
		try
		{
			empPayTableView.getItems().clear();
			empPayLabel.setText("Payroll - loading..."); 
		// new thread
			Task<Void> task = new Task<Void>()
			{
	            @Override
	            protected Void call() throws Exception
	            {
	        		if(DataManager.i().mEmployee != null) 
	        		{
	        			try {
	        				
	        				pays = null;
	        				pays = new ArrayList<Pay>();
	        				if (empAllEmployersCheck.isSelected()) {
		        				EmployeeRequest empReq = new EmployeeRequest();
		        				empReq.setPersonId(DataManager.i().mEmployee.getPersonId());
		        				List<Employee> employees = AdminPersistenceManager.getInstance().getAll(empReq);
		        				
			        			// update the pays for all matching employees
		        				for (Employee emp : employees) {
				        			PayRequest request = new PayRequest();
				        			request.setEmployeeId(emp.getId());
				        			request.setFetchInactive(true);
				        			pays.addAll(AdminPersistenceManager.getInstance().getAll(request));
		        				}
	        				}
	        				else {
			        			// update the pays for current employee
			        			PayRequest request = new PayRequest();
			        			request.setEmployeeId(DataManager.i().mEmployee.getId());
			        			request.setFetchInactive(true);
			        			DataManager.i().mEmployee.setPays(AdminPersistenceManager.getInstance().getAll(request));
			        			pays.addAll(AdminPersistenceManager.getInstance().getAll(request));
	        				}
	        				
		        			// update the paygaps
		        			PayGapRequest gapRequest = new PayGapRequest();
		        			gapRequest.setPersonId(DataManager.i().mEmployee.getPersonId());
		        			gapRequest.setFetchInactive(true);
		        			paygaps = AdminPersistenceManager.getInstance().getAll(gapRequest);
	        			} catch (Exception e) {
	        				DataManager.i().log(Level.SEVERE, e);
	        			}
	        		}
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {
	      		EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Employee Pay Data...");
	      		waitingToComplete++;
	      	});
	    	task.setOnSucceeded(e -> { 
	    		if(waitingToComplete-- == 1)
	    			EtcAdmin.i().setProgress(0.0);
	    		showPays(); 
	    	});
	    	task.setOnFailed(e -> { 
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		showPays(); 
	    	});
	        
	    	EmsApp.getInstance().getFxQueue().put(task);
		}catch(Exception e)
		{
			DataManager.i().log(Level.SEVERE, e);
			empPayLabel.setText("Payroll (total: 0)"); 
		}
	}
	
	private void showPays() 
	{
		empPayTableView.getItems().clear();
		if(pays != null && pays.size() > 0)
		{
		    for(Pay pay : pays)
		    {
		    	if(pay.isActive() == false && empInactivePaysCheck.isSelected() == false) continue;
		    	if(pay.isDeleted() == true && empDeletedPaysCheck.isSelected() == false) continue;
		    	if(empUnprocessedPaysCheck.isSelected() == true && pay.isProcessed() == true) continue;
				empPayTableView.getItems().add(new PayCell(pay));
		    }
		    
		   if (paygaps != null && empUnprocessedPaysCheck.isSelected() == false) {
		    for(PayGap pg : paygaps)
			    {
			    	if(pg.isActive() == false && empInactivePaysCheck.isSelected() == false) continue;
			    	if(pg.isDeleted() == true && empDeletedPaysCheck.isSelected() == false) continue;
					empPayTableView.getItems().add(new PayCell(pg));
			    }
		   }
		    
		    empPayTableView.getSortOrder().add(sortColumn);
	        sortColumn.setSortable(true);
			//update our Pays label
			empPayLabel.setText("Payroll (total: " + String.valueOf(empPayTableView.getItems().size()) + ")");
		} else {
			empPayLabel.setText("Payroll (total: 0)");			
			Utils.alertUser("No Payroll Available", "There is no payroll available to display for this user.");
		}
  		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0.0);
	}
	
	private void savePayData() 
	{
		try
		{
			// new thread
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception 
	            {
	        		// iterate through our pay collection
	        		for(PayCell cell : empPayTableView.getItems()) 
	        		{
	        			Pay pay = cell.getPay();
	        			if(pay == null) continue;
	        			//create the request and populate from the pay
	        			PayRequest payRequest = new PayRequest();
	        			//pay.setEmployee(DataManager.i().mEmployee);
	        			pay.setEmployeeId(DataManager.i().mEmployee.getId());
	        			if(payHoursReplaceData != null && payHoursReplaceData.isEmpty() == false)
	        				pay.setHours(Float.valueOf(payHoursReplaceData));
	        			if(payCompTypeReplaceData != null && payCompTypeReplaceData.isEmpty() == false)
	        				pay.setType(PayCodeType.valueOf(payCompTypeReplaceData));
	        			// calculate the hours per day
	        			float hoursPerDay = getHoursPerDay(pay.getHours(), pay);
	        			if(hoursPerDay > 0)
	        				pay.setHoursPerDay(hoursPerDay);
	        			pay.setProcessed(false);
	        			//if (pay.getAsOf() == null) pay.setAsOf(pay.getLastUpdated());
	        			//send it to the server
	        			payRequest.setId(pay.getId());
	        			payRequest.setEntity(pay);
	        			pay = AdminPersistenceManager.getInstance().addOrUpdate(payRequest);
	        		}
	        		
	        		// need to analyze the employee's pays now
	        		//DataAnalyzer.analyzeEmployeePay(EmsApp.getInstance().getEntityManager(), DataManager.i().mEmployee, DataManager.i().mEmployee.getBatchId());
	        		return null;
	 	        }
		    };
	    
		  	task.setOnScheduled(e ->  {
		  		EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Saving Pay Updates...");
		  	});	
			task.setOnSucceeded(e ->  finishPaySave());
			task.setOnFailed(e ->  finishPaySave());
		    
			EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e)
		{
			DataManager.i().log(Level.SEVERE, e);
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private float getHoursPerDay(float hours, Pay pay) 
	{
		float hpd = 0;
		if(pay.getPayPeriod() != null)
		{
			float days = 1 + Utils.daysBetween(pay.getPayPeriod().getStartDate(), pay.getPayPeriod().getEndDate());
			hpd = hours/days; 
		}	
		return hpd;
	}
	
	private void finishPaySave() 
	{
  		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0.0);
		empPayCompTypeReplace.getSelectionModel().clearSelection();
		empPayHoursReplace.clear();
		payHoursReplaceData = "";
		payCompTypeReplaceData = "";
		empPaySaving.setVisible(false);
		//update our pays
		updatePaysData();
	}
	
	@FXML
	private void onSave(ActionEvent event) 
	{
		savePayData();
	}
	
	@FXML
	private void onPayInactivesClick(ActionEvent event) {
		showPays();
	}	
		
	@FXML
	private void onShowAllEmployersClick(ActionEvent event) {
		updatePaysData();
	}	
		
	@FXML
	private void onPayUnprocessedOnlyClick(ActionEvent event) {
		showPays();
	}	
		
	@FXML
	private void onPayReplaceComp(ActionEvent event) 
	{
		if(empPayCompTypeReplace.getValue() == null) return;

		payCompTypeReplaceData = empPayCompTypeReplace.getValue();
		empPayReset.setVisible(true);
		empPaySave.setVisible(true);
		showPays();
	}	
		
	@FXML
	private void onPayReplaceHours(ActionEvent event) {
		payHoursReplaceData = empPayHoursReplace.getText();
		empPayReset.setVisible(true);
		empPaySave.setVisible(true);
		showPays();
	}	
		
	@FXML
	private void onPayReset(ActionEvent event) 
	{
		empPayCompTypeReplace.getSelectionModel().clearSelection();
		empPayHoursReplace.clear();
		payHoursReplaceData = "";
		payCompTypeReplaceData = "";
		empPaySave.setVisible(false);
		empPaySaving.setVisible(false);
		showPays();
	}	
		
	@FXML
	private void onPaySave(ActionEvent event)
	{
		empPaySave.setVisible(false);
		empPaySaving.setVisible(true);
		savePayData();
	}	
	
	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) empPayCompTypeReplace.getScene().getWindow();
		stage.close();
	}
	
	public class PayCell 
	{
		SimpleStringProperty id = new SimpleStringProperty();
		SimpleStringProperty date = new SimpleStringProperty();
		SimpleStringProperty hours = new SimpleStringProperty();
		SimpleStringProperty startDate = new SimpleStringProperty();
		SimpleStringProperty endDate = new SimpleStringProperty();
		SimpleStringProperty compType = new SimpleStringProperty();
		SimpleStringProperty unionType = new SimpleStringProperty();
		SimpleStringProperty payClass = new SimpleStringProperty();
		SimpleStringProperty rate = new SimpleStringProperty();
		SimpleStringProperty amount = new SimpleStringProperty();
		SimpleStringProperty processed = new SimpleStringProperty();
		SimpleStringProperty hoursPerDay = new SimpleStringProperty();
		SimpleStringProperty payPeriodId = new SimpleStringProperty();
		SimpleStringProperty employer = new SimpleStringProperty();
		SimpleStringProperty payDate = new SimpleStringProperty();
		SimpleStringProperty payFreq = new SimpleStringProperty();
		SimpleStringProperty customField1 = new SimpleStringProperty();
		SimpleStringProperty customField2 = new SimpleStringProperty();
		SimpleStringProperty customField3 = new SimpleStringProperty();
		SimpleStringProperty customField4 = new SimpleStringProperty();
		SimpleStringProperty customField5 = new SimpleStringProperty();
		SimpleStringProperty customField6 = new SimpleStringProperty();
		SimpleStringProperty customField7 = new SimpleStringProperty();
		SimpleStringProperty typeChanged = new SimpleStringProperty();
		SimpleStringProperty typeChangeVerified = new SimpleStringProperty();
		Pay pay;
		Boolean replaceCompType = false;
		Boolean replaceHours = false;

        Pay getPay() {
       	 return pay;
        }
        
        Boolean getReplaceCompType() {
        	return replaceCompType;
        }
 
        Boolean getReplaceHours() {
        	return replaceHours;
        }
 
        public String getId() {
        	return id.get();
        }
           
        public String getDate() {
        	return date.get();
        }
           
        public String getHours() {
        	return hours.get();
        }
           
        public String getHoursPerDay() {
        	return hoursPerDay.get();
        }
           
        public String getStartDate() {
        	return startDate.get();
        }
           
        public String getEndDate() {
        	return endDate.get();
        }
           
        public String getCompType() {
        	return compType.get();
        }
           
        public String getUnionType() {
        	return unionType.get();
        }
           
        public String getPayClass() {
        	return payClass.get();
        }
           
        public String getRate() {
        	return rate.get();
        }
           
        public String getAmount() {
        	return amount.get();
        }
           
        public String getProcessed() {
        	return processed.get();
        }

        public String getPayPeriodId() {
        	return payPeriodId.get();
        }

        public String getEmployer() {
        	return employer.get();
        }

        public String getCustomField1() {
        	return customField1.get();
        }

        public String getCustomField2() {
        	return customField2.get();
        }

        public String getCustomField3() {
        	return customField3.get();
        }

        public String getCustomField4() {
        	return customField4.get();
        }

        public String getCustomField5() {
        	return customField5.get();
        }

        public String getCustomField6() {
        	return customField6.get();
        }

        public String getCustomField7() {
        	return customField7.get();
        }

        public String getPayDate() {
        	return payDate.get();
        }

        public String getPayFreq() {
        	return payFreq.get();
        }

        public String getTypeChanged() {
        	return typeChanged.get();
        }

        public String getTypeChangeVerified() {
        	return typeChangeVerified.get();
        }

        PayCell(Pay pay) 
        {
             super();
             
             this.pay = pay;
             if(pay != null) 
             {
            	 PayPeriod ppd = null;
            	 //load payperiod
            	 try {
            		 	//the pay will have its payperiod attached here if it exists.
            		 	if((ppd = pay.getPayPeriod()) == null)
            		 		ppd = AdminPersistenceManager.getInstance().get(PayPeriod.class, pay.getPayPeriodId()); 
            		 }
				catch(CoreException e) { DataManager.i().log(Level.SEVERE, e);  }
         	    catch (Exception e) {  DataManager.i().logGenericException(e); }

            	 id.set(String.valueOf(pay.getId()));
        		 if(ppd.getId().equals(pay.getPayPeriodId()))
        		 {
        			 payFreq.set(String.valueOf(ppd.getPayFrequencyType()));
        			 if(ppd != null && ppd.getPayDate() != null) 
                	 {
                		 payDate.set(Utils.getDateString(ppd.getPayDate()));
                		 startDate.set(Utils.getDateString(ppd.getStartDate()));
                		 endDate.set(Utils.getDateString(ppd.getEndDate()));
                	 }
            	 }
            	 if(payCompTypeReplaceData != null && payCompTypeReplaceData.isEmpty() == false) {
            		 compType.set(payCompTypeReplaceData);
            		 replaceCompType = true;
            	 }
            	 else
            		 compType.set(String.valueOf(pay.getType()));

            	 date.set(Utils.getDateString(pay.getLastUpdated()));
            	 unionType.set(String.valueOf(pay.getUnionType()));
            	 payClass.set(pay.getV2PayClass());
            	 payPeriodId.set(String.valueOf(pay.getPayPeriodId()));
            	 if(pay.getEmployee() != null && pay.getEmployee().getEmployer() != null)
            		 employer.set(pay.getEmployee().getEmployer().getName());
            	 customField1.set(pay.getCustomField1());
            	 customField2.set(pay.getCustomField2());
            	 customField3.set(pay.getCustomField3());
            	 customField4.set(pay.getCustomField4());
            	 customField5.set(pay.getCustomField5());
            	 customField6.set(pay.getCustomField6());
            	 customField7.set(pay.getCustomField7());
            	 typeChanged.set(pay.isTypeChanged().toString());
            	 typeChangeVerified.set(pay.isTypeChangeVerified().toString());
            	 
            	 if(payHoursReplaceData.isEmpty() == false) 
            	 {
            		 hours.set(payHoursReplaceData);
            		 replaceHours = true;
            		 float fHours = Float.valueOf(payHoursReplaceData);
            		 float newHPD = ViewPayrollController.this.getHoursPerDay(fHours, pay);
                      hoursPerDay.set(String.valueOf(newHPD));
            	 } else {
            		 hours.set(String.valueOf(pay.getHours()));
                	 hoursPerDay.set(String.valueOf(pay.getHoursPerDay()));
            	 }
            	 rate.set(Utils.getMoneyString(pay.getRate()));
            	 amount.set(Utils.getMoneyString(pay.getAmount()));
            	 processed.set(String.valueOf(pay.isProcessed()));
             }
        }
        
        PayCell(PayGap gap) 
        {
             super();
             
             if(gap != null) 
             {
            	 id.set(String.valueOf(gap.getId()));
            	 employer.set("Pay Gap " + gap.getDays() + " days");
        		 startDate.set(Utils.getDateString(gap.getStartDate()));
        		 endDate.set(Utils.getDateString(gap.getEndDate()));
             }
        }

    }
		
}


