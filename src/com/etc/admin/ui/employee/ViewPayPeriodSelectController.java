package com.etc.admin.ui.employee;

import java.util.Calendar;
import java.util.Collections;
import java.util.logging.Level;

import com.etc.admin.AdminApp;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.PayPeriod;
import com.etc.corvetto.rqs.PayPeriodRequest;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ViewPayPeriodSelectController 
{

	@FXML
	private Label ppdLabel;
	@FXML
	private CheckBox ppdShowInactiveCheck;
	@FXML
	private CheckBox ppdShowDeletedCheck;
	@FXML
	private ListView<PayPeriodCell> ppdPeriodList;
	
	int waitingToComplete = 0;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	@FXML
	public void initialize() 
	{
		initControls();
		updatePayPeriods();
	}
	
	private void initControls() 
	{
	}

	private void updatePayPeriods()
	{
		try
		{
			ppdPeriodList.getItems().clear();
			ppdLabel.setText("Pay Periods (loading ...)"); 
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
	        
	      	task.setOnScheduled(e ->  {waitingToComplete++;});		
	    	task.setOnSucceeded(e ->  {
	    		if(waitingToComplete-- == 1)
					EtcAdmin.i().setProgress(0.0);
	    		showPayPeriods();
	    	});
	    	task.setOnFailed(e ->  { 
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		EtcAdmin.i().setStatusMessage("Unable to Load Employer Pay Period."); 
	    		ppdLabel.setText("Pay Periods"); 
	    	});
	    	
	        AdminApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e) { 
			DataManager.i().log(Level.SEVERE, e); 
			ppdLabel.setText("Pay Periods"); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void showPayPeriods() 
	{
		ppdPeriodList.getItems().clear();
		ppdLabel.setText("Pay Periods"); 
		if(DataManager.i().mEmployer.getPayPeriods() != null && DataManager.i().mEmployer.getPayPeriods().size() > 0)
		{
		  //  List<HBoxPayCell> payList = new ArrayList<HBoxPayCell>();
		    for(PayPeriod ppd : DataManager.i().mEmployer.getPayPeriods()) {
				if(ppd.isActive() == false && ppdShowInactiveCheck.isSelected() == false) continue;
		    	if(ppd.isDeleted() == true && ppdShowDeletedCheck.isSelected() == false) continue;
		    	ppdPeriodList.getItems().add(new PayPeriodCell(ppd));
		    }
		} 
		
		// sort the pay periods
		Collections.sort(ppdPeriodList.getItems(), (o1, o2) -> (o2.getDate().compareTo(o1.getDate()))); 
		//select the current pay period
		if (DataManager.i().mPay.getPayPeriod() != null) {
			for (PayPeriodCell cell : ppdPeriodList.getItems()) {
				if (cell.getPayPeriod() != null && cell.getPayPeriod().getId().equals(DataManager.i().mPay.getPayPeriod().getId())) {
					ppdPeriodList.getSelectionModel().select(cell);
					break;
				}
			}
		}
	}
	
	@FXML
	private void onSave(ActionEvent event) {
		PayPeriod ppd = ppdPeriodList.getSelectionModel().getSelectedItem().getPayPeriod();
		if (ppd == null) {
			Utils.showAlert("Selection Needed", "Please select a pay period before saving");
			return;
		}
		DataManager.i().mPay.setPayPeriod(ppd);
		DataManager.i().mPay.setPayPeriodId(ppd.getId());
		exitPopup();
	}	

	@FXML
	private void onShowInactiveMemberships() {
		showPayPeriods();
	}
		
	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) ppdShowInactiveCheck.getScene().getWindow();
		stage.close();
	}
	
	public static class PayPeriodCell extends HBox 
	{
		Label Id = new Label();
		Label employerReference = new Label();
		Label startDate = new Label();
		Label endDate = new Label();
		Label payDate = new Label();
		Label payFrequencyType= new Label();
		PayPeriod ppd;
	
		PayPeriod getPayPeriod() { return ppd; }
		
		Calendar getDate() {
			return Utils.getCalDate(startDate.getText());
		}
        
		PayPeriodCell(PayPeriod ppd) 
        {
              super();
       	
 	         this.ppd = ppd;
 	         if(ppd != null) 
 	         {
              		Utils.setHBoxLabel(Id, 100, false, ppd.getId());
              		Utils.setHBoxLabel(startDate,100, false, ppd.getStartDate());
              		Utils.setHBoxLabel(endDate,100, false, Utils.getDateString(ppd.getEndDate()));
              		Utils.setHBoxLabel(payDate,100, false, Utils.getDateString(ppd.getPayDate()));
              		Utils.setHBoxLabel(payFrequencyType,250,false,String.valueOf(ppd.getPayFrequencyType()));
              		
  	              if (ppd.isDeleted() == true) {
  	            	  Id.setTextFill(Color.RED);
  	            	  startDate.setTextFill(Color.RED);
  	            	  endDate.setTextFill(Color.RED);
  	            	  payDate.setTextFill(Color.RED);
  	            	  payFrequencyType.setTextFill(Color.RED);
	              }
	               else {
	            	   if (ppd.isActive() == false) {
	   	            	  Id.setTextFill(Color.BLUE);
	  	            	  startDate.setTextFill(Color.BLUE);
	  	            	  endDate.setTextFill(Color.BLUE);
	  	            	  payDate.setTextFill(Color.BLUE);
	  	            	  payFrequencyType.setTextFill(Color.BLUE);
		               }
		               else {
		   	            	  Id.setTextFill(Color.BLACK);
		  	            	  startDate.setTextFill(Color.BLACK);
		  	            	  endDate.setTextFill(Color.BLACK);
		  	            	  payDate.setTextFill(Color.BLACK);
		  	            	  payFrequencyType.setTextFill(Color.BLACK);
		               }
	               }
              		
 	         } else {
 	        	 	Utils.setHBoxLabel(Id, 100, true, "Id");
           			Utils.setHBoxLabel(startDate, 100, true, "Start Date");
           			Utils.setHBoxLabel(endDate, 100, true, "End Date");
           			Utils.setHBoxLabel(payDate, 100, true, "Pay Date");
           			Utils.setHBoxLabel(payFrequencyType, 250, true, "Pay Freq Type");
              }

           	  this.getChildren().addAll(Id, startDate, endDate, payDate, payFrequencyType);                  
         }
    }	
	
}


