package com.etc.admin.ui.employee;

import java.io.IOException;
import java.util.logging.Level;

import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.CoverageGroupMembership;
import com.etc.corvetto.rqs.CoverageGroupMembershipRequest;
import com.etc.corvetto.rqs.EmployeeRequest;

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

public class ViewCoverageMembershipsController 
{

	@FXML
	private Label empEmpCvgClassLabel;
	@FXML
	private CheckBox empEmpCvgClassShowInactiveCheck;
	@FXML
	private CheckBox empEmpCvgClassShowDeletedCheck;
	@FXML
	private ListView<HBoxCvgGroupMembershipCell> empEmpCvgClassList;
	
	//int to track any pending threads, used to properly update the progress and message
	int waitingToComplete = 0;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	@FXML
	public void initialize() 
	{
		initControls();
		updateCoverageGroupMemberships();
	}
	
	private void initControls() 
	{
		// EMPLOYEE COVERAGE CLASSES
		empEmpCvgClassList.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) 
            {
            	//set the selected coverage
				DataManager.i().mEmployeeCoverageGroupMembership = empEmpCvgClassList.getSelectionModel().getSelectedItem().getCvgMembership();
				// and display the pop up
				try {
		            // load the fxml
			        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employee/ViewEmployeeCoverageClass.fxml"));
					Parent ControllerNode = loader.load();
			        ViewEmployeeCoverageClassController coverageController = (ViewEmployeeCoverageClassController) loader.getController();

			        Stage stage = new Stage();
			        stage.initModality(Modality.APPLICATION_MODAL);
			        stage.initStyle(StageStyle.UNDECORATED);
			        stage.setScene(new Scene(ControllerNode));  
			        EtcAdmin.i().positionStageCenter(stage);
			        stage.showAndWait();
			        if(coverageController.changesMade == true) {
			        	updateCoverageGroupMemberships();
			        }
				} catch (IOException e) {
		        	DataManager.i().log(Level.SEVERE, e); 
				}        		
			    catch (Exception e) {  DataManager.i().logGenericException(e); }
            }
        });

	}

	private void updateCoverageGroupMemberships() 
	{
		try
		{
			empEmpCvgClassList.getItems().clear();
			empEmpCvgClassLabel.setText("Coverage Memberships (loading...)"); 
			// new thread
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception 
	            {
	          		EtcAdmin.i().updateStatus(0.8, "Loading Coverage Group Memberships...");
	          		CoverageGroupMembershipRequest cgRequest = new CoverageGroupMembershipRequest();
	          		cgRequest.setFetchInactive(true);
	          		cgRequest.setEmployeeId(DataManager.i().mEmployee.getId());
	          		DataManager.i().mEmployee.setCoverageGroupMemberships(AdminPersistenceManager.getInstance().getAll(cgRequest));
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {
	      		EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Coverage Group Membership Data...");
	      		waitingToComplete++;
	      	});
	      			
	    	task.setOnSucceeded(e ->  { 
	    		if(waitingToComplete-- == 1)
	    			EtcAdmin.i().setProgress(0.0);
	    		showCoverageGroupMemberships(); 
	    	});
	    	task.setOnFailed(e ->  { 
	    		EtcAdmin.i().setProgress(0.0);
	    		DataManager.i().log(Level.SEVERE,task.getException());
	    		showCoverageGroupMemberships(); 
	    	});
	        
	    	EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e)
		{
			DataManager.i().log(Level.SEVERE, e); 
			empEmpCvgClassLabel.setText("Coverage Class (loading...)"); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	//update the coverages list
	private void showCoverageGroupMemberships() 
	{
		empEmpCvgClassList.getItems().clear();
	    if(DataManager.i().mEmployee == null) return;

		if(DataManager.i().mEmployee.getCoverageGroupMemberships() != null)
		{
			// add  a header row if we have items
		    if(DataManager.i().mEmployee.getCoverageGroupMemberships().size() > 0)
				empEmpCvgClassList.getItems().add(new HBoxCvgGroupMembershipCell(null));
			
		    for(CoverageGroupMembership cvg : DataManager.i().mEmployee.getCoverageGroupMemberships()) 
		    {
				if(cvg == null) continue;
				if(cvg.isActive() == false && empEmpCvgClassShowInactiveCheck.isSelected() == false) continue;
		    	if(cvg.isDeleted() == true && empEmpCvgClassShowDeletedCheck.isSelected() == false) continue;
				empEmpCvgClassList.getItems().add(new HBoxCvgGroupMembershipCell(cvg));
			}
			//update our employer screen label
			empEmpCvgClassLabel.setText("Coverage Class Memberships (total: " + String.valueOf(DataManager.i().mEmployee.getCoverageGroupMemberships().size()) + ")");
		} else {
			empEmpCvgClassLabel.setText("Coverage Class Memberships (total: 0)");			
		}
	}

	private void viewAddMembership()
	{
		try {
			// reset the current membership to default to add 
			DataManager.i().mEmployeeCoverageGroupMembership = null;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employee/ViewEmployeeCoverageClass.fxml"));
			Parent ControllerNode = loader.load();
	        ViewEmployeeCoverageClassController coverageController = (ViewEmployeeCoverageClassController) loader.getController();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if(coverageController.changesMade == true)
	        {
	        	updateCoverageGroupMemberships();
	        }
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	@FXML
	private void onAddMembership() {
		viewAddMembership();
	}
		
	@FXML
	private void onShowInactiveMemberships() {
		showCoverageGroupMemberships();
	}
		
	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) empEmpCvgClassList.getScene().getWindow();
		stage.close();
	}
	
	public static class HBoxCvgGroupMembershipCell extends HBox 
	{
         Label lblId = new Label();
         Label lblStartDate = new Label();
         Label lblEndDate = new Label();
         Label lblCoverageGroup = new Label();
         CoverageGroupMembership cvgMembership;
         
         CoverageGroupMembership getCvgMembership() {
        	 return cvgMembership;
         }

         HBoxCvgGroupMembershipCell(CoverageGroupMembership cvgMembership) 
         {
              super();
              
              this.cvgMembership = cvgMembership;
              if(cvgMembership != null) 
              {
               	  Utils.setHBoxLabel(lblId, 75, false, cvgMembership.getId());
               	  Utils.setHBoxLabel(lblStartDate, 100, false, cvgMembership.getStartDate());
               	  Utils.setHBoxLabel(lblEndDate, 100, false, cvgMembership.getEndDate());
               	  if(cvgMembership.getCoverageGroup() != null)
               		  Utils.setHBoxLabel(lblCoverageGroup, 250, false, cvgMembership.getCoverageGroup().getDescription());
               	  else
               		  Utils.setHBoxLabel(lblCoverageGroup, 250, false, "");
               	  
               	  if(cvgMembership.isActive() == false) 
               	  {
               		lblId.setTextFill(Color.BLUE);
               		lblStartDate.setTextFill(Color.BLUE);
               		lblEndDate.setTextFill(Color.BLUE);
               		lblCoverageGroup.setTextFill(Color.BLUE);
               	  }
              }else {
               	  Utils.setHBoxLabel(lblId, 75, true, "Id");
               	  Utils.setHBoxLabel(lblStartDate, 100, true, "Start Date");
               	  Utils.setHBoxLabel(lblEndDate, 100, true, "End Date");
               	  Utils.setHBoxLabel(lblCoverageGroup, 250, true, "Coverage Membership");
              }
           	  this.getChildren().addAll(lblId, lblStartDate, lblEndDate, lblCoverageGroup);                  
         }
    }	
		
	
	
}


