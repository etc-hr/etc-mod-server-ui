package com.etc.admin.ui.account;

import java.io.IOException;
import java.util.logging.Level;

import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.benefit.ViewBenefitController;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.Benefit;
import com.etc.corvetto.rqs.BenefitRequest;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewPlansController 
{

	@FXML
	private Label topLabel;
	@FXML
	private ListView<HBoxBenefitCell> listView;
	@FXML
	private CheckBox inactiveCheck;
	@FXML
	private CheckBox deletedCheck;
	
	//int to track any pending threads, used to properly update the progress and message
	int waitingToComplete = 0;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	@FXML
	public void initialize() 
	{
		initControls();
		updatePlanData();
	}
	
	private void initControls() 
	{
		listView.setOnMouseClicked(mouseClickedEvent ->
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) 
            {
            	DataManager.i().mBenefit = listView.getSelectionModel().getSelectedItem().getBenefit();
				viewSelectedPlan();
            }
        });
	}
	
	private void updatePlanData() 
	{
		try
		{
			listView.getItems().clear();
			topLabel.setText("Plans loading...");
			// create a thread to handle the update
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception {
	            	// create the request
	            	BenefitRequest request = new BenefitRequest();
	            	// set the id
	            	request.setAccountId(DataManager.i().mAccount.getId());
	            	// get the updated benefits using the request
	            	request.setFetchInactive(true);
	            	DataManager.i().mAccount.setBenefits(AdminPersistenceManager.getInstance().getAll(request));
	            	DataManager.i().mAccount.setBenefits(AdminPersistenceManager.getInstance().getAll(request));
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {
	      		EtcAdmin.i().setStatusMessage("Loading Plan Data...");
	      		EtcAdmin.i().setProgress(0.9);});
	      			
	    	task.setOnSucceeded(e ->  showPlans());
	    	task.setOnFailed(e ->  {
		    	DataManager.i().log(Level.SEVERE,task.getException());
		    	showPlans();
	    	});
	        
	    	EmsApp.getInstance().getFxQueue().put(task);
	    	
		}catch(InterruptedException e)
		{
			DataManager.i().log(Level.SEVERE, e); 
		}	
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}


	private void showPlans() 
	{
		// reset the status
  		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0);

  		// clear anything already in the list
		listView.getItems().clear();

		//if we have no account contacts, bail
		if(DataManager.i().mAccount.getBenefits() != null)
		{
			listView.getItems().add(new HBoxBenefitCell(null));		
			for (Benefit benefit : DataManager.i().mAccount.getBenefits()) 
			{
				if(benefit.isActive() == false && inactiveCheck.isSelected() == false) continue;
		    	if(benefit.isDeleted() == true && deletedCheck.isSelected() == false) continue;
				listView.getItems().add(new HBoxBenefitCell(benefit));
			}
	        //update our label
	        topLabel.setText("Plans (total: " + String.valueOf(DataManager.i().mAccount.getBenefits().size()) + ")" );
		} else
	        topLabel.setText("Plans (total: 0)" );

	}
	
	private void viewSelectedPlan() 
	{
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/benefit/ViewBenefit.fxml"));
			Parent ControllerNode = loader.load();
	        ViewBenefitController plansController = (ViewBenefitController) loader.getController();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if (plansController.changesMade == true)
	        	updatePlanData();
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	

	
	@FXML
	private void onInactivesClick(ActionEvent event) {
		showPlans();
	}	
	
	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) listView.getScene().getWindow();
		stage.close();
	}
	
	public static class HBoxBenefitCell extends HBox 
	{
         Label lblId = new Label();
         Label lblName = new Label();
         Label lblCoverageType = new Label();
         Label lblInsuranceType = new Label();
         Label lblOfferCode = new Label();
         Label lblWaived = new Label();
         Label lblInel = new Label();
         Label lblCobra = new Label();
         Label lblICHRA = new Label();
         CheckBox cbICHRA = new CheckBox();
         CheckBox cbWaived = new CheckBox();
         CheckBox cbInel = new CheckBox();
         CheckBox cbCobra = new CheckBox();
         Benefit benefit = null;

         public Benefit getBenefit() {
        	 return benefit;
         }
         
         HBoxBenefitCell(Benefit benefit) 
         {
              super();
              this.benefit = benefit;
              if(benefit != null) 
              {
               	  Utils.setHBoxLabel(lblId, 75, false, benefit.getId());
               	  Utils.setHBoxLabel(lblName, 300, false, benefit.getName());
               	  if (benefit.getName().equals("Unk"))
               		Utils.setHBoxLabel(lblName, 300, false, "Unk, Prov Ref: " + benefit.getProviderReference());
               	  if(benefit.getCoverageType() != null)
               		  Utils.setHBoxLabel(lblCoverageType, 120, false, benefit.getCoverageType().toString());
               	  else
               		  Utils.setHBoxLabel(lblCoverageType, 120, false, "");
               	  if(benefit.getInsuranceType() != null)
               		  Utils.setHBoxLabel(lblInsuranceType, 120, false, benefit.getInsuranceType().toString());
               	  else
               		  Utils.setHBoxLabel(lblInsuranceType, 120, false, "");
               	  if(benefit.getOfferCode() != null)
               		  Utils.setHBoxLabel(lblOfferCode, 90, false, benefit.getOfferCode().toString());
               	  else
               		  Utils.setHBoxLabel(lblOfferCode, 90, false, "");
               	  
               	  Utils.setHBoxCheckBox(cbWaived, 65, benefit.isWaived());
               	  Utils.setHBoxCheckBox(cbInel, 65, benefit.isIneligible());
               	  Utils.setHBoxCheckBox(cbCobra, 65, benefit.isCobra());
               	  Utils.setHBoxCheckBox(cbICHRA, 65, benefit.isIchra());
               	  if(benefit.isDeleted() == true)
                  {
 		           	  lblId.setTextFill(Color.RED);
 		           	  lblName.setTextFill(Color.RED);
 		           	  lblCoverageType.setTextFill(Color.RED);
 		           	  lblInsuranceType.setTextFill(Color.RED);
 		           	  lblOfferCode.setTextFill(Color.RED);
                  } else {
	                  if(benefit.isActive() == false)
	                  {
	 		           	  lblId.setTextFill(Color.BLUE);
	 		           	  lblName.setTextFill(Color.BLUE);
	 		           	  lblCoverageType.setTextFill(Color.BLUE);
	 		           	  lblInsuranceType.setTextFill(Color.BLUE);
	 		           	  lblOfferCode.setTextFill(Color.BLUE);
	                  } else {
	 		           	  lblId.setTextFill(Color.BLACK);
	 		           	  lblName.setTextFill(Color.BLACK);
	 		           	  lblCoverageType.setTextFill(Color.BLACK);
	 		           	  lblInsuranceType.setTextFill(Color.BLACK);
	 		           	  lblOfferCode.setTextFill(Color.BLACK);
	                  }
                  }
                  this.getChildren().addAll(lblId, lblName, lblCoverageType, lblInsuranceType, lblOfferCode, cbICHRA, cbWaived, cbInel, cbCobra);
              }else {
               	  Utils.setHBoxLabel(lblId, 75, true, "Id");
               	  Utils.setHBoxLabel(lblName, 300, true, "Name");
               	  Utils.setHBoxLabel(lblCoverageType, 120, true, "Coverage Type");
               	  Utils.setHBoxLabel(lblInsuranceType, 120, true, "Ins. Type");
               	  Utils.setHBoxLabel(lblOfferCode, 90, true, "Offer Code");
               	  Utils.setHBoxLabel(lblWaived, 65, true, "Waived");
               	  Utils.setHBoxLabel(lblInel, 65, true, "Inel");
               	  Utils.setHBoxLabel(lblCobra, 65, true, "Cobra");
               	  Utils.setHBoxLabel(lblICHRA, 65, true, "ICHRA");
                  this.getChildren().addAll(lblId, lblName, lblCoverageType, lblInsuranceType, lblOfferCode, lblICHRA, lblWaived, lblInel, lblCobra);
              }
         }
    }	

	
}


