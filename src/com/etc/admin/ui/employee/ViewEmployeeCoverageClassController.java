package com.etc.admin.ui.employee;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.coresysteminfo.ViewCoreSystemInfoController;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.CoverageGroup;
import com.etc.corvetto.entities.CoverageGroupMembership;
import com.etc.corvetto.rqs.CoverageGroupMembershipRequest;
import com.etc.corvetto.rqs.CoverageGroupRequest;
import com.etc.entities.CoreData;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewEmployeeCoverageClassController 
{
	@FXML
	private ComboBox<HBoxCvgGroupCell> empCvgClassCombo;
	@FXML
	private TextField empCvgClassIdField;
	@FXML
	private DatePicker empCvgStartDatePicker;
	@FXML
	private DatePicker empCvgEndDatePicker;
	@FXML
	private Button empCvgSaveButton;
	@FXML
	private Label empCvgTopLabel;

	public boolean changesMade = false;
	boolean updated = false;
	boolean userChanges = false;
	
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
		empCvgClassIdField.setEditable(false);
		if(DataManager.i().mEmployeeCoverageGroupMembership == null) 
		{
			// must be add mode
			empCvgSaveButton.setText("Add");
			empCvgClassIdField.setVisible(false);
		}
		updateCoverageGroups();
	}
	
	private void updateCoverageGroups()
	{
		try
		{
			if(DataManager.i().mEmployer == null) return;
			
			Task<Void> task = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					
					EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Employer Coverage Groups...");
	          		CoverageGroupRequest cgRequest = new CoverageGroupRequest();
	          		cgRequest.setEmployerId(DataManager.i().mEmployer.getId());
	          		DataManager.i().mEmployer.setCoverageGroups(AdminPersistenceManager.getInstance().getAll(cgRequest));
	          		
					return null;
				}
				
			};
			
			task.setOnSucceeded(e -> {
				loadCoverageClassesCombo();
			});
			task.setOnFailed(e -> { 
		    	DataManager.i().log(Level.SEVERE,task.getException());
				loadCoverageClassesCombo();
			});
			
			EmsApp.getInstance().getFxQueue().put(task);
			
		}catch(Exception e) { DataManager.i().log(Level.SEVERE, e); }
	}
	

	
	private void loadCoverageClassesCombo() 
	{
		empCvgClassCombo.getItems().clear();
		if(DataManager.i().mEmployer.getCoverageGroups() != null)
			for(CoverageGroup cls : DataManager.i().mEmployer.getCoverageGroups())
			{
				if(cls != null && cls.isActive() == true)
					empCvgClassCombo.getItems().add(new HBoxCvgGroupCell(cls));
			}
		// now show the membership
		showMembership();
	}
	
	private void showMembership() 
	{
		if(DataManager.i().mEmployeeCoverageGroupMembership != null) 
		{
			empCvgTopLabel.setText("Coverage Class Membership");
			if (DataManager.i().mEmployeeCoverageGroupMembership.isActive() == false)
				empCvgTopLabel.setText("Coverage Class Membership - INACTIVE");
			if (DataManager.i().mEmployeeCoverageGroupMembership.isDeleted() == true)
				empCvgTopLabel.setText("Coverage Class Membership - DELETED");
			Utils.updateControl(empCvgClassIdField,DataManager.i().mEmployeeCoverageGroupMembership.getId());
			Utils.updateControl(empCvgStartDatePicker,DataManager.i().mEmployeeCoverageGroupMembership.getStartDate());
			Utils.updateControl(empCvgEndDatePicker,DataManager.i().mEmployeeCoverageGroupMembership.getEndDate());

			if(DataManager.i().mEmployeeCoverageGroupMembership != null) 
			{
				for(HBoxCvgGroupCell cell : empCvgClassCombo.getItems()) 
				{
					if(cell.getCoverageGroup().getId().longValue() == DataManager.i().mEmployeeCoverageGroupMembership.getCoverageGroupId().longValue()) 
					{
						empCvgClassCombo.setValue(cell);
						break;
					}
				}
			} 
		}
	}
	
	private void updateCoverageGroupMembershipData() 
	{
		DataManager.i().mEmployeeCoverageGroupMembership.setStartDate(Utils.getCalDate(empCvgStartDatePicker));
		DataManager.i().mEmployeeCoverageGroupMembership.setEndDate(Utils.getCalDate(empCvgEndDatePicker));
		int i = empCvgClassCombo.getSelectionModel().getSelectedIndex();
		CoverageGroup cov = DataManager.i().mEmployer.getCoverageGroups().get(i);
		if (cov != null) {
			DataManager.i().mEmployeeCoverageGroupMembership.setCoverageGroup(cov);
			DataManager.i().mEmployeeCoverageGroupMembership.setCoverageGroupId(cov.getId());
		}
	}


	@FXML
	private void onShowSystemInfo()
	{
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mEmployeeCoverageGroupMembership;
			DataManager.i().mCurrentCoreDataType = SystemDataType.COVERAGEGROUPMEMBERSHIP;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
			Parent ControllerNode = loader.load();
	        ViewCoreSystemInfoController sysInfoController = (ViewCoreSystemInfoController) loader.getController();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        // update the screen
	        if (sysInfoController.changesMade == true) {
	        	changesMade = true;
	        	showMembership();
	        }
		} catch (IOException e) { DataManager.i().log(Level.SEVERE, e); }        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}

	private boolean validateFields() 
	{
		if (empCvgStartDatePicker.getValue() == null) {
			Utils.showAlert("Start Date Blank", "Please select a start date before continuing.");
			return false;
		}
		if (empCvgEndDatePicker.getValue() != null && empCvgEndDatePicker.getValue().isBefore(empCvgStartDatePicker.getValue() )) {
			Utils.showAlert("End Date before Start Date", "The end date cannot be before the start date.");
			return false;
		}
		if (empCvgClassCombo.getValue() == null) {
			Utils.showAlert("No Coverage Class Selected", "Please select a coverage class before continuing.");
			return false;
		}
		
		return true;
	}
	
	@FXML
	private void onSave(ActionEvent event) 
	{
		try {
			// if it has just been changed to inactive don't save and reactivate
			if (changesMade == true && DataManager.i().mEmployeeCoverageGroupMembership.isActive() == false) {
				exitPopup();
				return;
			}
			
			if(validateFields() == false)
				return;	
			CoverageGroupMembership membership = null;
			CoverageGroupMembershipRequest request = new CoverageGroupMembershipRequest();

			if(DataManager.i().mEmployeeCoverageGroupMembership == null) {
				// create a new object
				DataManager.i().mEmployeeCoverageGroupMembership = new CoverageGroupMembership();
			}else {
				// assign the id of the existing object
				request.setId(DataManager.i().mEmployeeCoverageGroupMembership.getId());
			}

			// gather the data
			updateCoverageGroupMembershipData();
			// check for removed dates
			if (DataManager.i().mEmployeeCoverageGroupMembership.getEndDate() == null) {
				request.setClearEndDate(true);
			}
			// check for removed dates
			if (DataManager.i().mEmployeeCoverageGroupMembership.getStartDate() == null) {
				request.setClearStartDate(true);
			}
			DataManager.i().mEmployee.setEmployer(DataManager.i().mEmployer);
			DataManager.i().mEmployeeCoverageGroupMembership.setEmployee(DataManager.i().mEmployee);
			DataManager.i().mEmployeeCoverageGroupMembership.setEmployeeId(DataManager.i().mEmployee.getId());
			request.setEntity(DataManager.i().mEmployeeCoverageGroupMembership);
			//send it to the server
			membership = AdminPersistenceManager.getInstance().addOrUpdate(request);
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
		
		//mark changes made
		changesMade = true;
		// and exit the pop up
		exitPopup();
	}
	
	private void exitPopup() 
	{
		changesMade = true;
		Stage stage = (Stage) empCvgStartDatePicker.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onCancel(ActionEvent event) 
	{
		if(userChanges == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Changes Made");
		    confirmAlert.setContentText("Are You Sure You Want To Exit Without Saving Changes?");
		    EtcAdmin.i().positionAlertCenter(confirmAlert);
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK)) {
		    	return;
		    }
		}
		
		exitPopup();
	}	
	
	private void onChange(ActionEvent event) {
		onChange();
	}
	
	private void onChange() 
	{
		if(updated == false) return;
		userChanges = true;
	}
	
	
	@FXML
	private void onRemove(ActionEvent event) 
	{

	}	 
	
	public static class HBoxCvgGroupCell extends HBox 
	{
        Label lblId = new Label();
        Label lblName = new Label();
        Label lblDescription = new Label();
        CoverageGroup cvgClass;
         
        CoverageGroup getCoverageGroup() {
        	 return cvgClass;
        }
         
        @Override
        public String toString() {
             return cvgClass.getId().toString() + " - " + cvgClass.getDescription() + " - " + cvgClass.getName();
        }

        HBoxCvgGroupCell(CoverageGroup cvgClass)
        {
            super();
        	this.cvgClass = cvgClass;
          	Utils.setHBoxLabel(lblId, 50, false, cvgClass.getId());
          	Utils.setHBoxLabel(lblDescription, 300, false, cvgClass.getDescription());
          	Utils.setHBoxLabel(lblName, 150, false, cvgClass.getName());

            this.getChildren().addAll(lblId, lblDescription, lblName);
        }
    }	
}
