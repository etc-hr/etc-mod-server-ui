package com.etc.admin.ui.employee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.secondary.ViewSecondaryController;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.Coverage;
import com.etc.corvetto.entities.Dependent;
import com.etc.corvetto.rqs.CoverageRequest;
import com.etc.corvetto.rqs.DependentRequest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewCoveredIndividualsController 
{

	// secondaries
	@FXML
	private Label empCIsLabel;
	@FXML
	private ListView<HBoxDependentCell> empCIsListView;
	@FXML
	private CheckBox empInactiveCIsCheck;
	@FXML
	private CheckBox empDeletedCIsCheck;
	
	//int to track any pending threads, used to properly update the progress and message
	int waitingToComplete = 0;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	@FXML
	public void initialize() 
	{
		initControls();
		updateCIData();
	}
	
	private void initControls() 
	{
		// mouse handler
		empCIsListView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) 
            {
            	if (empCIsListView.getSelectionModel().getSelectedItem() == null) return;
				 DataManager.i().mDependent = empCIsListView.getSelectionModel().getSelectedItem().getDependent();
					// and display the pop up
					try {
			            // load the fxml
				        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/secondary/ViewSecondary.fxml"));
						Parent ControllerNode = loader.load();
				        Stage stage = new Stage();
				        stage.initModality(Modality.APPLICATION_MODAL);
				        stage.initStyle(StageStyle.UNDECORATED);
				        stage.setScene(new Scene(ControllerNode));  
				        EtcAdmin.i().positionStageCenter(stage);
				        stage.showAndWait();
				        updateCIData();
					} catch (Exception e) {
			        	DataManager.i().log(Level.SEVERE, e); 
					}        		
				// load the CI screen
				//EtcAdmin.i().setScreen(ScreenType.SECONDARY, true);
            }
        });	
	}

	public void updateCIData()
	{
		try
		{
			empCIsListView.getItems().clear();
			empCIsLabel.setText("Covered Individuals (loading ...)"); 
			Task<Void> task = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					
	          		DependentRequest depRequest = new DependentRequest();
	          		depRequest.setProviderId(DataManager.i().mEmployee.getPerson().getId());
	          		depRequest.setFetchInactive(true);
	          		DataManager.i().mEmployee.getPerson().setDependents(AdminPersistenceManager.getInstance().getAll(depRequest));
					return null;
				}
				
			};
			
			task.setOnScheduled(e -> {
				EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Updating Dependent Data...");
				waitingToComplete++;
			});
			task.setOnSucceeded(e -> { 
				if(waitingToComplete-- == 1)
	    			EtcAdmin.i().setProgress(0.0);
				showCIs(); 
			});
			task.setOnFailed(e -> { 
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
				showCIs(); 
			});
			
			EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e) 
		{
			DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}

	private void showCIs() 
	{
		if(DataManager.i().mEmployee != null && DataManager.i().mEmployee.getPerson() != null && DataManager.i().mEmployee.getPerson().getDependents() != null)
		{
			List<HBoxDependentCell> list = new ArrayList<>();
			if(DataManager.i().mEmployee.getPerson().getDependents().size() > 0)
				list.add(new HBoxDependentCell(null));
			
			for(Dependent dependent : DataManager.i().mEmployee.getPerson().getDependents()) {
		    	if(dependent.isActive() == false && empInactiveCIsCheck.isSelected() == false) continue;
		    	if(dependent.isDeleted() == true && empDeletedCIsCheck.isSelected() == false) continue;
				list.add(new HBoxDependentCell(dependent));
			}
			
	        ObservableList<HBoxDependentCell> myObservableList = FXCollections.observableList(list);
	        empCIsListView.setItems(myObservableList);		
			
	        empCIsLabel.setText("Covered Individuals (total: " + String.valueOf(DataManager.i().mEmployee.getPerson().getDependents().size()) + ")" );
		 } else {
			empCIsLabel.setText("Covered Individuals (total: 0)");
		 }
	}
	
	@FXML
	private void onShowInactiveCIs() {
		showCIs();
	}		

	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) empCIsListView.getScene().getWindow();
		stage.close();
	}
	
 	public static class HBoxDependentCell extends HBox 
	{
         Label lblFirst = new Label();
         Label lblLast = new Label();
         Label lblActive = new Label();
         CheckBox cbActive = new CheckBox();
         Label lblLastFour = new Label();
         Label lblPersonId = new Label();
         Label lblDOB = new Label();
         Dependent dependent;
         
         public Dependent getDependent() {
        	 return dependent;
         }
         
         HBoxDependentCell(Dependent dependent) 
         {
        	 super();
    		 try {
             this.dependent = dependent;
             //ser default
       	  Utils.setHBoxLabel(lblActive, 75, false, "");
			Utils.setHBoxCheckBox(cbActive, 75, false);
       	  	Utils.setHBoxLabel(lblPersonId, 100, false, "");
       	  	Utils.setHBoxLabel(lblLastFour, 100, false, "");
       	  	Utils.setHBoxLabel(lblFirst, 200, false, "");
       	  	Utils.setHBoxLabel(lblLast, 200, false, "");
       	  	Utils.setHBoxLabel(lblDOB, 100, false, "");
            if(dependent != null) 
            {
            	 // coverage info
            	 if(dependent.getPerson() != null) 
            	 {
	            	  Utils.setHBoxCheckBox(cbActive, 75, dependent.isActive());
	            	  Utils.setHBoxLabel(lblPersonId, 100, false, dependent.getPerson().getId());
	            	  if (dependent.getPerson().getSsn() != null)
	            		  Utils.setHBoxLabel(lblLastFour, 100, false, dependent.getPerson().getSsn().getUsrln());
	            	  Utils.setHBoxLabel(lblFirst, 200, false, dependent.getPerson().getFirstName());
	            	  Utils.setHBoxLabel(lblLast, 200, false, dependent.getPerson().getLastName());
	            	  Utils.setHBoxLabel(lblDOB, 100, false, dependent.getPerson().getDateOfBirth());
		              if (dependent.isDeleted() == true) {
		            	  cbActive.setTextFill(Color.RED);
		            	  lblPersonId.setTextFill(Color.RED);
		            	  lblLastFour.setTextFill(Color.RED);
		            	  lblFirst.setTextFill(Color.RED);
		            	  lblLast.setTextFill(Color.RED);
		            	  lblDOB.setTextFill(Color.RED);
		              }
		               else {
		            	   if (dependent.isActive() == false) {
				            	  cbActive.setTextFill(Color.BLUE);
				            	  lblPersonId.setTextFill(Color.BLUE);
				            	  lblLastFour.setTextFill(Color.BLUE);
				            	  lblFirst.setTextFill(Color.BLUE);
				            	  lblLast.setTextFill(Color.BLUE);
				            	  lblDOB.setTextFill(Color.BLUE);
			               }
			               else {
				            	  cbActive.setTextFill(Color.BLACK);
				            	  lblPersonId.setTextFill(Color.BLACK);
				            	  lblLastFour.setTextFill(Color.BLACK);
				            	  lblFirst.setTextFill(Color.BLACK);
				            	  lblLast.setTextFill(Color.BLACK);
				            	  lblDOB.setTextFill(Color.BLACK);
			               }
		               }
            	  }
                 this.getChildren().addAll(cbActive, lblPersonId, lblLastFour, lblFirst, lblLast, lblDOB);
              } else {
            	  Utils.setHBoxLabel(lblActive, 75, true, "Active");
            	  Utils.setHBoxLabel(lblPersonId, 100, true, "Person Id");
            	  Utils.setHBoxLabel(lblLastFour, 100, true, "SSN");
            	  Utils.setHBoxLabel(lblFirst, 200, true, "First");
            	  Utils.setHBoxLabel(lblLast, 200, true, "Last");
            	  Utils.setHBoxLabel(lblDOB, 100, true, "DOB");
                  this.getChildren().addAll(lblActive, lblPersonId, lblLastFour, lblFirst, lblLast, lblDOB);
              }
    		 } catch (Exception e) {
    			 DataManager.i().log(Level.SEVERE, e); 
    		 }
         }
    }	
	
	
}


