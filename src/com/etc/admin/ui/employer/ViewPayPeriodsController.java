package com.etc.admin.ui.employer;

import java.io.IOException;
import java.util.logging.Level;

import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.payperiod.ViewPayPeriodController;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.PayPeriod;
import com.etc.corvetto.rqs.PayPeriodRequest;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewPayPeriodsController 
{

	@FXML
	private CheckBox ppdsInactiveCheck;
	@FXML
	private CheckBox ppdsDeletedCheck;
	@FXML
	private TableView<PayPeriodCell> ppdsTableView;
	@FXML
	private Label ppdsLabel;
	
	int waitingToComplete = 0;
	
	//table sort 
	TableColumn<PayPeriodCell, String> sortColumn = null;
	ContextMenu paysCM = null;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	@FXML
	public void initialize() 
	{
		initControls();
		showPayPeriods();
	}
	
	private void initControls() 
	{
		// set the pay table columns
		setPayPeriodTableColumns();

		// PAYPERIODS
		ppdsTableView.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
            	viewSelectedPayPeriod(false);
            }
        });

		
	}
	
	private void setPayPeriodTableColumns() 
	{
		//clear the default values
		ppdsTableView.getColumns().clear();

	    TableColumn<PayPeriodCell, String> x1 = new TableColumn<PayPeriodCell, String>("Employer Id");
		x1.setCellValueFactory(new PropertyValueFactory<PayPeriodCell,String>("employerReference"));
		x1.setMinWidth(150);
		ppdsTableView.getColumns().add(x1);
		setCellFactory(x1);

	    TableColumn<PayPeriodCell, String> x2 = new TableColumn<PayPeriodCell, String>("Start Date");
	    x2.setCellValueFactory(new PropertyValueFactory<PayPeriodCell,String>("startDate"));
	    x2.setMinWidth(150);
	    x2.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		//initial sort is by start date
		sortColumn = x2;
		sortColumn.setSortType(SortType.DESCENDING);
		ppdsTableView.getColumns().add(x2);
		setCellFactory(x2);
		
	    TableColumn<PayPeriodCell, String> x3 = new TableColumn<PayPeriodCell, String>("End Date");
	    x3.setCellValueFactory(new PropertyValueFactory<PayPeriodCell,String>("endDate"));
	    x3.setMinWidth(150);
	    x3.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
	    ppdsTableView.getColumns().add(x3);
		setCellFactory(x3);

		TableColumn<PayPeriodCell, String> x4 = new TableColumn<PayPeriodCell, String>("Pay Date");
		x4.setCellValueFactory(new PropertyValueFactory<PayPeriodCell,String>("payDate"));
		x4.setMinWidth(150);
	    x4.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
	    ppdsTableView.getColumns().add(x4);
		setCellFactory(x4);

	    TableColumn<PayPeriodCell, String> x5 = new TableColumn<PayPeriodCell, String>("Pay Frequency Type");
	    x5.setCellValueFactory(new PropertyValueFactory<PayPeriodCell,String>("payFrequencyType"));
	    x5.setMinWidth(200);
	    ppdsTableView.getColumns().add(x5);
		setCellFactory(x5);
	}
	
	private void setCellFactory(TableColumn<PayPeriodCell, String>  col) 
	{
		col.setCellFactory(column -> {
		    return new TableCell<PayPeriodCell, String>() 
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
		                PayPeriodCell cell = getTableView().getItems().get(getIndex());
		                if(cell.getPayPeriod().isActive() == false)
		                	setTextFill(Color.BLUE);
		                else
		                	setTextFill(Color.BLACK);
		            }
		        }
		    };
		});
	}

	private void showPayPeriods() 
	{
		ppdsTableView.getItems().clear();
		ppdsLabel.setText("Pay Periods"); 
		if(DataManager.i().mEmployer.getPayPeriods() != null && DataManager.i().mEmployer.getPayPeriods().size() > 0)
		{
		  //  List<HBoxPayCell> payList = new ArrayList<HBoxPayCell>();
		    for(PayPeriod pay : DataManager.i().mEmployer.getPayPeriods()) {
				if(pay.isActive() == false && ppdsInactiveCheck.isSelected() == false) continue;
		    	if(pay.isDeleted() == true && ppdsDeletedCheck.isSelected() == false) continue;
		    	ppdsTableView.getItems().add(new PayPeriodCell(pay));
		    }

		    ppdsTableView.getSortOrder().add(sortColumn);
		    sortColumn.setSortable(true);
		} 
	}
	
	private void viewSelectedPayPeriod(boolean add) 
	{
		// offset one for the header row
		//DataManager.i().setEmployerContact(emprPayPeriodListView.getSelectionModel().getSelectedIndex() - 1);
		if(add == false)
			DataManager.i().mPayPeriod = ppdsTableView.getSelectionModel().getSelectedItem().getPayPeriod();
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/payperiod/ViewPayPeriod.fxml"));
			Parent ControllerNode = loader.load();
			ViewPayPeriodController controller = (ViewPayPeriodController) loader.getController();

			if(add == true)
				controller.setEditState(2);
			else
				controller.setEditState(0);
			controller.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if (controller.changesMade == true) {
	        	updatePayPeriods();
	        }
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	
	
	private void updatePayPeriods()
	{
		try
		{
			// create a thread to handle the update
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception 
	            {
	            	EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Employer PayPeriods...");
	          		PayPeriodRequest ppRequest = new PayPeriodRequest();
	          		ppRequest.setFetchInactive(true);
	          		ppRequest.setEmployerId(DataManager.i().mEmployer.getId());
	          		DataManager.i().mEmployer.setPayPeriods(AdminPersistenceManager.getInstance().getAll(ppRequest));
	                return null;
	            }
	        };
	        
	    	task.setOnSucceeded(e ->  {
	    		showPayPeriods();
	    	});
	    	task.setOnFailed(e ->  { 
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		EtcAdmin.i().setStatusMessage("Unable to Load Employer Pay Period."); 
	    		showPayPeriods();
	    	});
	    	
	        EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e) { 
			DataManager.i().log(Level.SEVERE, e);  
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	

	@FXML
	private void onAddPayPeriod(ActionEvent event) {
		viewSelectedPayPeriod(true);
	}	

	@FXML
	private void onPayInactivesClick(ActionEvent event) {
		showPayPeriods();
	}	
		
	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) ppdsTableView.getScene().getWindow();
		stage.close();
	}
	
	public static class PayPeriodCell 
	{
		SimpleStringProperty employerReference = new SimpleStringProperty();
		SimpleStringProperty startDate = new SimpleStringProperty();
		SimpleStringProperty endDate = new SimpleStringProperty();
		SimpleStringProperty payDate = new SimpleStringProperty();
		SimpleStringProperty payFrequencyType= new SimpleStringProperty();

		PayPeriod pay;
	
		PayPeriod getPayPeriod() { return pay; }
	
	    public String getEmployerReference() { return employerReference.get(); }
	       
	    public String getStartDate() { return startDate.get(); }
	       
	    public String getEndDate() { return endDate.get(); }
	       
	    public String getPayDate() { return payDate.get(); }

	    public String getPayFrequencyType() { return payFrequencyType.get(); }

	    PayPeriodCell(PayPeriod pay) 
	    {
	         super();
	
	         this.pay = pay;
	         if(pay != null) 
	         {
	        	 employerReference.set(String.valueOf(pay.getId()));
	        	 startDate.set(Utils.getDateString(pay.getStartDate()));
	        	 endDate.set(Utils.getDateString(pay.getEndDate()));
	        	 payDate.set(Utils.getDateString(pay.getPayDate()));
	        	 payFrequencyType.set(String.valueOf(pay.getPayFrequencyType()));
	         }
	    }
	}

	
	
	
}


