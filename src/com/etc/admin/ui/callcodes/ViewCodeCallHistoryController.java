package com.etc.admin.ui.callcodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.calc.entities.aca.Irs10945bCalculation;
import com.etc.corvetto.ems.calc.entities.aca.Irs10945cCalculation;
import com.etc.corvetto.ems.calc.rqs.aca.Irs10945bCalculationRequest;
import com.etc.corvetto.ems.calc.rqs.aca.Irs10945cCalculationRequest;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.TaxYear;
import com.etc.corvetto.rqs.EmployerRequest;
import com.etc.corvetto.rqs.TaxYearRequest;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewCodeCallHistoryController 
{

	@FXML
	private Label topLabel;
	@FXML
	private CheckBox InactiveCheck;
	@FXML
	private TableView<HBoxCodeCallHistory> tableView;
	@FXML
	private ComboBox<String> filterCombo;
	
	private boolean isAccountHistory = false;
	
	List<CodeCallHistory> historyData = null;
	List<Irs10945cCalculation> Irs10945cCalculationData = null;
	List<Irs10945bCalculation> Irs10945bCalculationData = null;
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	//table sort 
	TableColumn<HBoxCodeCallHistory, String> sortColumn = null;

	@FXML
	public void initialize() 
	{
		
	}
	
	public void setIsAccountHistory(boolean isAccountHistory)
	{
		initControls();
		this.isAccountHistory = isAccountHistory;
		if (this.isAccountHistory == true) {
			topLabel.setText("Account Code Call History");
			for (Employer emp : DataManager.i().mAccount.getEmployers()) {
				filterCombo.getItems().add(emp.getName());
			}
			updateAccountHistoryData();
		}
		else {
			topLabel.setText("Employer Code Call History");
			filterCombo.getItems().add(DataManager.i().mEmployer.getName());
			updateHistoryData();
		}
	}
	
	private void initControls() 
	{
		setTableColumns();
		
		/*dataList.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				 viewSelectedHistory();
            }
        }); */
	}
	
	private void setTableColumns() 
	{
		//clear the default values
		tableView.getColumns().clear();

	    TableColumn<HBoxCodeCallHistory, String> xUser = new TableColumn<HBoxCodeCallHistory, String>("User");
	    xUser.setCellValueFactory(new PropertyValueFactory<HBoxCodeCallHistory,String>("userName"));
	    xUser.setMinWidth(100);
		tableView.getColumns().add(xUser);

		TableColumn<HBoxCodeCallHistory, String> x2 = new TableColumn<HBoxCodeCallHistory, String>("Date");
	    x2.setCellValueFactory(new PropertyValueFactory<HBoxCodeCallHistory,String>("date"));
	    x2.setMinWidth(150);
	    x2.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		//initial sort is by start date
		sortColumn = x2;
		sortColumn.setSortType(SortType.DESCENDING);
		tableView.getColumns().add(x2);
		
	    TableColumn<HBoxCodeCallHistory, String> x3 = new TableColumn<HBoxCodeCallHistory, String>("Batch Id");
		x3.setCellValueFactory(new PropertyValueFactory<HBoxCodeCallHistory,String>("batchId"));
		x3.setMinWidth(250);
		tableView.getColumns().add(x3);
		
	    TableColumn<HBoxCodeCallHistory, String> xTaxYear = new TableColumn<HBoxCodeCallHistory, String>("TaxYear");
	    xTaxYear.setCellValueFactory(new PropertyValueFactory<HBoxCodeCallHistory,String>("taxYear"));
	    xTaxYear.setMinWidth(75);
		tableView.getColumns().add(xTaxYear);

	    TableColumn<HBoxCodeCallHistory, String> xEmployer = new TableColumn<HBoxCodeCallHistory, String>("Employer");
	    xEmployer.setCellValueFactory(new PropertyValueFactory<HBoxCodeCallHistory,String>("employerName"));
	    xEmployer.setMinWidth(200);
		tableView.getColumns().add(xEmployer);

		TableColumn<HBoxCodeCallHistory, String> x4 = new TableColumn<HBoxCodeCallHistory, String>("Type");
		x4.setCellValueFactory(new PropertyValueFactory<HBoxCodeCallHistory,String>("type"));
		x4.setMinWidth(75);
		tableView.getColumns().add(x4);
		
	    TableColumn<HBoxCodeCallHistory, String> x5 = new TableColumn<HBoxCodeCallHistory, String>("Irs1094 Created");
		x5.setCellValueFactory(new PropertyValueFactory<HBoxCodeCallHistory,String>("irs1094Created"));
		x5.setMinWidth(100);
		tableView.getColumns().add(x5);
		
	    TableColumn<HBoxCodeCallHistory, String> x6 = new TableColumn<HBoxCodeCallHistory, String>("Irs1094 updated");
		x6.setCellValueFactory(new PropertyValueFactory<HBoxCodeCallHistory,String>("irs1094updated"));
		x6.setMinWidth(100);
		tableView.getColumns().add(x6);
		
	    TableColumn<HBoxCodeCallHistory, String> x7 = new TableColumn<HBoxCodeCallHistory, String>("Irs1095 Created");
		x7.setCellValueFactory(new PropertyValueFactory<HBoxCodeCallHistory,String>("irs1095Created"));
		x7.setMinWidth(100);
		tableView.getColumns().add(x7);
		
	    TableColumn<HBoxCodeCallHistory, String> x8 = new TableColumn<HBoxCodeCallHistory, String>("Irs1095 updated");
		x8.setCellValueFactory(new PropertyValueFactory<HBoxCodeCallHistory,String>("irs1095updated"));
		x8.setMinWidth(100);
		tableView.getColumns().add(x8);
		
		TableColumn<HBoxCodeCallHistory, String> x9 = new TableColumn<HBoxCodeCallHistory, String>("Completed");
		x9.setCellValueFactory(new PropertyValueFactory<HBoxCodeCallHistory,String>("completed"));
		x9.setMinWidth(100);
		tableView.getColumns().add(x9);
		
	    TableColumn<HBoxCodeCallHistory, String> x10 = new TableColumn<HBoxCodeCallHistory, String>("Integrated");
		x10.setCellValueFactory(new PropertyValueFactory<HBoxCodeCallHistory,String>("integrated"));
		x10.setMinWidth(100);
		tableView.getColumns().add(x10);

		TableColumn<HBoxCodeCallHistory, String> x11 = new TableColumn<HBoxCodeCallHistory, String>("Rejected");
		x11.setCellValueFactory(new PropertyValueFactory<HBoxCodeCallHistory,String>("rejected"));
		x11.setMinWidth(100);
		tableView.getColumns().add(x11);
		 
	}
	
	private void setCellFactory(TableColumn<HBoxCodeCallHistory, String>  col) 
	{
		col.setCellFactory(column -> {
		    return new TableCell<HBoxCodeCallHistory, String>() 
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
		                HBoxCodeCallHistory cell = getTableView().getItems().get(getIndex());
		                //if(cell.getPayPeriod().isActive() == false)
		                //	setTextFill(Color.BLUE);
		                //else
		                //	setTextFill(Color.BLACK);
		            }
		        }
		    };
		});
	}

	
	private void updateHistoryData() {

		// create a thread to handle the update, letting the screen respond normally
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            	try {
            		if (historyData == null)
            			historyData = new ArrayList<CodeCallHistory>();
	            	// update data here
	            	Irs10945cCalculationRequest request = new Irs10945cCalculationRequest();
	            	Irs10945bCalculationRequest brequest = new Irs10945bCalculationRequest();
	            	
	            	for (TaxYear ty : DataManager.i().mEmployer.getTaxYears()) {
	            		request.setTaxYearId(ty.getId());
	            		brequest.setTaxYearId(ty.getId());
	            		
	            		// c
	            		Irs10945cCalculationData = AdminPersistenceManager.getInstance().getAll(request);
	            		if (Irs10945cCalculationData != null)
	            			for (Irs10945cCalculation cCalc : Irs10945cCalculationData)
	            				historyData.add(new CodeCallHistory(cCalc, DataManager.i().mEmployer.getName()));

	            		// b
	            		Irs10945bCalculationData = AdminPersistenceManager.getInstance().getAll(brequest);
	            		if (Irs10945bCalculationData != null)
	            			for (Irs10945bCalculation bCalc : Irs10945bCalculationData)
	            				historyData.add(new CodeCallHistory(bCalc, DataManager.i().mEmployer.getName()));
	            	}
            	}catch (Exception e) {
            		DataManager.i().log(Level.SEVERE,e);
            	}
            	
            	//limit to max time back ?
            	//Calendar queueDate = Calendar.getInstance();
            	//queueDate.add(Calendar.HOUR, -72);
            	//request.setLastUpdated(queueDate.getTimeInMillis());
            	
            	// retrieve from the server
                return null;
            }
        };
        
      	task.setOnScheduled(e ->  {
      		EtcAdmin.i().setStatusMessage("Updating History Data...");
      		EtcAdmin.i().setProgress(0.25);
      	});
      			
    	task.setOnSucceeded(e ->  showHistory());
    	
    	task.setOnFailed(e ->  {
	    	DataManager.i().log(Level.SEVERE,task.getException());
	    	showHistory();
    	});
    	
    	Thread queueRefresh = new Thread(task);
    	queueRefresh.start();
	}
	
	private void updateAccountHistoryData() {

		// create a thread to handle the update, letting the screen respond normally
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            	try {
            		if (historyData == null)
            			historyData = new ArrayList<CodeCallHistory>();
	            	// update data here
	            	Irs10945cCalculationRequest request = new Irs10945cCalculationRequest();
	            	Irs10945bCalculationRequest brequest = new Irs10945bCalculationRequest();
	            	
	            	float empCount = DataManager.i().mAccount.getEmployers().size();
	            	float step = 1;
	            	for (Employer employer : DataManager.i().mAccount.getEmployers()) {
		            	// force a refresh of the tax years -  was staying in a persistant bag at this point
		            	TaxYearRequest tReq = new TaxYearRequest();
		            	tReq.setEmployerId(employer.getId());
		            	employer.setTaxYears(AdminPersistenceManager.getInstance().getAll(tReq));
		            	
		            	for (TaxYear ty : employer.getTaxYears()) {
							EtcAdmin.i().showAppStatus("Retrieving Code Call History", "Processing " + employer.getName() + " TaxYear " + ty.getYear(), step / empCount, true);

		            		request.setTaxYearId(ty.getId());
		            		brequest.setTaxYearId(ty.getId());
		            		
		            		// c
		            		Irs10945cCalculationData = AdminPersistenceManager.getInstance().getAll(request);
		            		if (Irs10945cCalculationData != null)
		            			for (Irs10945cCalculation cCalc : Irs10945cCalculationData)
		            				historyData.add(new CodeCallHistory(cCalc, employer.getName()));
	
		            		// b
		            		Irs10945bCalculationData = AdminPersistenceManager.getInstance().getAll(brequest);
		            		if (Irs10945bCalculationData != null)
		            			for (Irs10945bCalculation bCalc : Irs10945bCalculationData)
		            				historyData.add(new CodeCallHistory(bCalc, employer.getName()));
		            	}
	            		step += 1;
	            	}
            	}catch (Exception e) {
            		DataManager.i().log(Level.SEVERE,e);
            	}
            	
            	//limit to max time back ?
            	//Calendar queueDate = Calendar.getInstance();
            	//queueDate.add(Calendar.HOUR, -72);
            	//request.setLastUpdated(queueDate.getTimeInMillis());
            	
            	EtcAdmin.i().showAppStatus("", "", 0, false);
                return null;
            }
        };
        
      	task.setOnScheduled(e ->  {
      		EtcAdmin.i().setStatusMessage("Updating History Data...");
      		EtcAdmin.i().setProgress(0.25);
      	});
      			
    	task.setOnSucceeded(e ->  showHistory());
    	
    	task.setOnFailed(e ->  {
	    	DataManager.i().log(Level.SEVERE,task.getException());
	    	showHistory();
    	});
    	
    	Thread queueRefresh = new Thread(task);
    	queueRefresh.start();
	}
	
	private void showHistory() 
	{
  		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0.0);
		Collections.sort(historyData, (o1, o2) -> (int)(o2.getDate().compareTo(o1.getDate()))); 
		tableView.getItems().clear();
		String searchString = "";
		if(historyData != null && historyData.size() > 0)
		{
			for(CodeCallHistory cch : historyData)
			{
				if(cch == null) continue;
				if (filterCombo.getValue() != null && filterCombo.getValue().isEmpty() == false) {
					searchString = cch.getEmployerName();
					searchString += " " + cch.getType();
					searchString += " " + cch.getTaxYear();
					searchString += " " + cch.getBatchId();
					searchString += " " + Utils.getDateString(cch.getDate());

					if (searchString.toUpperCase().contains(filterCombo.getValue().toUpperCase()))
						tableView.getItems().add(new HBoxCodeCallHistory(cch));
				} else
					tableView.getItems().add(new HBoxCodeCallHistory(cch));
			};	
		}
		// add the header
		//tableView.getItems().add(0,new HBoxCodeCallHistory(null));
	}

/*	private void viewSelectedHistory() 
	{
		// offset one for the header row
		DataManager.i().setDepartment(dataList.getSelectionModel().getSelectedIndex() - 1);
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/department/ViewDepartment.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	
*/	
	@FXML
	private void onShowInactivesClick(ActionEvent event) {
		showHistory();
	}	
	
	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	@FXML
	private void onClearFilter(ActionEvent event) {
		filterCombo.getSelectionModel().clearSelection();
		filterCombo.setValue("");
		showHistory();
	}	
	
    @FXML
    void onEmployerSelect(ActionEvent event) 
    {
    	showHistory();
    }

	@FXML
	private void onFilter() {
		showHistory();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) tableView.getScene().getWindow();
		stage.close();
	}
	
	public class HBoxCodeCallHistory extends HBox 
	{
		SimpleStringProperty chid = new SimpleStringProperty();
		SimpleStringProperty date = new SimpleStringProperty();
		SimpleStringProperty batchId = new SimpleStringProperty();
		SimpleStringProperty type = new SimpleStringProperty();
		SimpleStringProperty taxYear = new SimpleStringProperty();
		SimpleStringProperty irs1094Created = new SimpleStringProperty();
		SimpleStringProperty irs1094Updated = new SimpleStringProperty();
		SimpleStringProperty irs1095Created = new SimpleStringProperty();
		SimpleStringProperty irs1095Updated = new SimpleStringProperty();
		SimpleStringProperty completed = new SimpleStringProperty();
		SimpleStringProperty integrated = new SimpleStringProperty();
		SimpleStringProperty rejected = new SimpleStringProperty();
		SimpleStringProperty employerName = new SimpleStringProperty();
		SimpleStringProperty userName = new SimpleStringProperty();

		
        public String getChid() {
          	 return chid.get();
           }
           
        public String getDate() {
         	 return date.get();
          }
          
        public String getBatchId() {
         	 return batchId.get();
          }
          
        public String getType() {
         	 return type.get();
          }
          
        public String getIrs1094Created() {
         	 return irs1094Created.get();
          }
          
        public String getIrs1094Updated() {
         	 return irs1094Updated.get();
          }
          
        public String getIrs1095Created() {
         	 return irs1095Created.get();
          }
          
        public String getIrs1095Updated() {
         	 return irs1095Updated.get();
          }
          
        public String getCompleted() {
         	 return completed.get();
          }
          
        public String getIntegrated() {
         	 return integrated.get();
          }
          
        public String getRejected() {
         	 return rejected.get();
          }
          
        public String getTaxYear() {
         	 return taxYear.get();
          }
          
        public String getEmployerName() {
        	 return employerName.get();
         }
         
        public String getUserName() {
        	 return userName.get();
         }
         

		HBoxCodeCallHistory(CodeCallHistory cch)
         {
              super();
              if(cch != null) {
            	 date.set(Utils.getDateTimeString(cch.getDate()));
            	 chid.set(cch.getId().toString());
            	 batchId.set(cch.getBatchId());
            	 if (cch.getType().equals("Irs10945c"))
            		 type.set("C");
            	 if (cch.getType().equals("Irs10945b"))
            		 type.set("B");
            	 taxYear.set(String.valueOf(cch.getTaxYear()));
             	 if (cch.getCalcInfo() != null) {
             		irs1094Created.set(String.valueOf(cch.getIrs1094Created()));
             		irs1094Updated.set(String.valueOf(cch.getIrs1094Updated()));
             		irs1095Created.set(String.valueOf(cch.getIrs1095Created()));
             		irs1095Updated.set(String.valueOf(cch.getIrs1095Updated()));
             		if (cch.getCalcInfo().isCompleted() == true)
             			completed.set("Y");
             		if (cch.getCalcInfo().isIntegrated() == true)
             			integrated.set("Y");
             		if (cch.getCalcInfo().isRejected() == true)
             			rejected.set("Y");
             	 }
             	 employerName.set(cch.getEmployerName());
             	 userName.set(cch.getUserName());
              }
         }
    }	

}


