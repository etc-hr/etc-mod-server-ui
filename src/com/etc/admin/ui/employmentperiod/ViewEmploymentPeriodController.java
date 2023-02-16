package com.etc.admin.ui.employmentperiod;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.coresysteminfo.ViewCoreSystemInfoController;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.EmploymentPeriod;
import com.etc.corvetto.rqs.EmploymentPeriodRequest;
import com.etc.entities.CoreData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewEmploymentPeriodController 
{
	@FXML
	private DatePicker empdHireDatePicker;
	@FXML
	private DatePicker empdTermDatePicker;
	@FXML
	private Label empdTitleLabel;
	@FXML
	private Button empdSaveButton;
	@FXML
	private Button empdSystemInfoButton;
	
	public boolean changesMade = false;
	private boolean sysChangesMade = false;

	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() 
	{
		updateEmploymentPeriodData();
	}
	
	private void updateEmploymentPeriodData()
	{
		if (DataManager.i().mEmploymentPeriod != null) 
		{
			empdHireDatePicker.setEditable(false);
			String sName = DataManager.i().mEmployer.getName() + " Employment Period";

			Utils.updateControl(empdTitleLabel,sName);

			if (DataManager.i().mEmploymentPeriod.isActive() == false)
				Utils.updateControl(empdTitleLabel,sName + " - INACTIVE");
			if (DataManager.i().mEmploymentPeriod.isDeleted() == true)
				Utils.updateControl(empdTitleLabel,sName + " - DELETED");
				
			Utils.updateControl(empdHireDatePicker,DataManager.i().mEmploymentPeriod.getHireDate());
			Utils.updateControl(empdTermDatePicker,DataManager.i().mEmploymentPeriod.getTermDate());
		} else { // null current period means an add
			empdSystemInfoButton.setVisible(false);
			empdSaveButton.setText("Add");
		}
	}

	@FXML
	private void onShowSystemInfo() 
	{
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mEmploymentPeriod;
			DataManager.i().mCurrentCoreDataType = SystemDataType.EMPLOYMENTPERIOD;
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
	        sysChangesMade = sysInfoController.changesMade;
	        updateEmploymentPeriodData();
		} catch (IOException e) { DataManager.i().log(Level.SEVERE, e); }        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}

	private void exitPopup()
	{
		changesMade = true;
		Stage stage = (Stage) empdHireDatePicker.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onRemove(ActionEvent event) 
	{

		try {

			DataManager.i().mEmploymentPeriod.setEmployee(DataManager.i().mEmployee);
			EmploymentPeriodRequest request = new EmploymentPeriodRequest();
			request.setId(DataManager.i().mEmploymentPeriod.getId());	
			request.setEntity(DataManager.i().mEmploymentPeriod);

			// send it to the server
			AdminPersistenceManager.getInstance().remove(request);
		} catch (CoreException e) { DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	
		changesMade = true;
		exitPopup();
	}		
	
	@FXML
	private void onSave(ActionEvent event) 
	{
		// don't update if it is inactive
		if (DataManager.i().mEmploymentPeriod != null && DataManager.i().mEmploymentPeriod.isActive() == false) {
			changesMade = true;
			exitPopup();
			return;
		}
		
		// valdidate data
		if (Utils.validate(empdHireDatePicker) == false) 
			return;
		
		//Send it to the server
		try {
			if (empdSaveButton.getText() == "Add") {
				EmploymentPeriod ep = new EmploymentPeriod();
				ep.setHireDate(Utils.getCalDate(empdHireDatePicker));
				ep.setTermDate(Utils.getCalDate(empdTermDatePicker));
				ep.setEmployeeId(DataManager.i().mEmployee.getId());	
				EmploymentPeriodRequest epReq = new EmploymentPeriodRequest();
				epReq.setEntity(ep);
				DataManager.i().mEmploymentPeriod = AdminPersistenceManager.getInstance().addOrUpdate(epReq);
			} else { // updating
				DataManager.i().mEmploymentPeriod.setEmployee(DataManager.i().mEmployee);
				EmploymentPeriodRequest request = new EmploymentPeriodRequest();
				DataManager.i().mEmploymentPeriod.setHireDate(Utils.getCalDate(empdHireDatePicker));
				Calendar termDate = Utils.getCalDate(empdTermDatePicker);
				DataManager.i().mEmploymentPeriod.setTermDate(termDate);
				request.setId(DataManager.i().mEmploymentPeriod.getId());	
				request.setEntity(DataManager.i().mEmploymentPeriod);
				DataManager.i().mEmploymentPeriod = AdminPersistenceManager.getInstance().addOrUpdate(request);
			}
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		}
		changesMade = true;
		exitPopup();
	}		
	
	@FXML
	private void onCancel(ActionEvent event) {
		exitPopup();		
	}		
}
