package com.etc.admin.ui.employee;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.coresysteminfo.ViewCoreSystemInfoController;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.Pay;
import com.etc.corvetto.entities.PaySnapshot;
import com.etc.corvetto.rqs.PayRequest;
import com.etc.corvetto.rqs.PaySnapshotRequest;
import com.etc.corvetto.utils.types.PayCodeType;
import com.etc.corvetto.utils.types.UnionType;
import com.etc.entities.CoreData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewPayController 
{
	@FXML
	private Label payTopLabel;
	@FXML
	private Label inactiveLabel;
	@FXML
	private CheckBox payProcessedCheck;
	@FXML
	private CheckBox payApprovedCheck;
	@FXML
	private Button paySaveButton;
	@FXML
	private TextField payHoursField;
	@FXML
	private TextField payRateField;
	@FXML
	private TextField payAmountField;
	@FXML
	private TextField payHoursPerDayField;
	@FXML
	private ChoiceBox<String> payPayCodeTypeChoice;
	@FXML
	private ChoiceBox<UnionType> payUnionTypeChoice;
	@FXML
	private TextField payPayPeriodIdField;
	@FXML
	private TextField payIdField;
	@FXML
	private TextField payBornOnField;
	@FXML
	private TextField payPayStartField;
	@FXML
	private TextField payPayEndField;
	@FXML
	private TextField payPayDateField;
	@FXML
	private TextField payCustomField1Field;
	@FXML
	private TextField payCustomField2Field;
	@FXML
	private TextField payCustomField3Field;
	@FXML
	private TextField payCustomField4Field;
	@FXML
	private TextField payCustomField5Field;
	@FXML
	private TextField payCustomField6Field;
	@FXML
	private TextField payCustomField7Field;
	@FXML
	private CheckBox payTypeChangedCheck;
	@FXML
	private CheckBox payTypeChangeVerifiedCheck;
	@FXML
	private ComboBox<String> editHistoryCombo;
	@FXML
	private Label historyWarningLabel;

	public boolean changesMade = false;
	List<PaySnapshot> paySnapshots = null;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	boolean updated = false;
	boolean userChanges = false;
	@FXML
	public void initialize() 
	{
		if (DataManager.i().mPay != null)
			initControls();
		showPay();
		updated = true;	
	}
	
	private void initControls() 
	{
		// need to add manually instead of using the enum
		payPayCodeTypeChoice.getItems().clear();
		payPayCodeTypeChoice.getItems().add("FTH");
		payPayCodeTypeChoice.getItems().add("VH");
		payApprovedCheck.setDisable(false);
		
		payTypeChangedCheck.setDisable(true);
	
		ObservableList<UnionType> unionTypes = FXCollections.observableArrayList(UnionType.values());
		payUnionTypeChoice.setItems(unionTypes);
		
		// handlers for capturing data changes
		payHoursField.textProperty().addListener((observable, oldValue, newValue) -> {
			onChange();
		});
		payRateField.textProperty().addListener((observable, oldValue, newValue) -> {
			onChange();
		});
		payAmountField.textProperty().addListener((observable, oldValue, newValue) -> {
			onChange();
		});
		payHoursPerDayField.textProperty().addListener((observable, oldValue, newValue) -> {
			onChange();
		});
		payApprovedCheck.setOnAction(this::onChange);
		payPayCodeTypeChoice.setOnAction(this::onChange); 		
		payUnionTypeChoice.setOnAction(this::onChange); 		
	}
	
	private void updateEditHistoryData() 
	{
		if(DataManager.i().mPay == null) return;
		
		try
		{
			// new thread
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception
	            {
	     			PaySnapshotRequest ssRequest = new PaySnapshotRequest();
	      			ssRequest.setPayId(DataManager.i().mPay.getId());
	      			paySnapshots = AdminPersistenceManager.getInstance().getAll(ssRequest);
	                return null;
	            }
	        };
	        
	    	task.setOnSucceeded(e ->  {
	    		showEditHistory();
	    	});
	    	task.setOnFailed(e ->  {
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		showEditHistory();
	    	});
	    	
	    	EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e)
		{
			DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void showEditHistory() 
	{
		Collections.sort(paySnapshots, (o1, o2) -> (int)(o2.getLastUpdated().compareTo(o1.getLastUpdated()))); 
		editHistoryCombo.getItems().clear();
		editHistoryCombo.getItems().add("Current");
		String val = "";
		for (PaySnapshot snap : paySnapshots) {
			val = Utils.getDateTimeString(snap.getLastUpdated());
			if (snap.getCreatedBy() != null)
				val += " - " + snap.getCreatedBy().getFirstName() + " " + snap.getCreatedBy().getLastName().substring(0, 1) + ".";
			editHistoryCombo.getItems().add(val);
		}
		editHistoryCombo.getSelectionModel().clearAndSelect(0);
	}

	public void load() {

	}
	
	private void showPay() 
	{
		Pay pay = DataManager.i().mPay;
		inactiveLabel.setVisible(false);
		if (pay.isActive() == false) {
			inactiveLabel.setVisible(true);
			inactiveLabel.setText("INACTIVE");
		}
		if (pay.isDeleted() == true) {
			inactiveLabel.setVisible(true);
			inactiveLabel.setText("DELETED");
		}
		if (pay.getPayPeriod() != null && pay.getPayPeriod().getPayDate() != null) {
			Utils.updateControl(payTopLabel, "Pay for Pay Date " + Utils.getDateString(pay.getPayPeriod().getPayDate()));
			Utils.updateControl(payPayStartField, pay.getPayPeriod().getStartDate());
			Utils.updateControl(payPayEndField, pay.getPayPeriod().getEndDate());
			Utils.updateControl(payPayDateField, pay.getPayPeriod().getPayDate());
			payPayPeriodIdField.setText(pay.getPayPeriod().getId().toString());
		}
		Utils.updateControl(payIdField, pay.getId());
		Utils.updateControl(payBornOnField, pay.getBornOn());
		Utils.updateControl(payApprovedCheck, pay.isApproved());
		Utils.updateControl(payProcessedCheck, pay.isProcessed());
		Utils.updateControl(payHoursField, pay.getHours());
		Utils.updateControl(payRateField, pay.getRate());
		Utils.updateControl(payAmountField, pay.getAmount());
		Utils.updateControl(payHoursPerDayField, pay.getHoursPerDay());
		if (pay.getType() != null)
			payPayCodeTypeChoice.getSelectionModel().select(pay.getType().toString());
		if (pay.getUnionType() != null)
			payUnionTypeChoice.getSelectionModel().select(pay.getUnionType());

		Utils.updateControl(payTypeChangedCheck, pay.isTypeChanged());
		Utils.updateControl(payTypeChangeVerifiedCheck, pay.isTypeChangeVerified());

		// custom fields
		Utils.updateControl(payCustomField1Field, pay.getCustomField1());
		Utils.updateControl(payCustomField2Field, pay.getCustomField2());
		Utils.updateControl(payCustomField3Field, pay.getCustomField3());
		Utils.updateControl(payCustomField4Field, pay.getCustomField4());
		Utils.updateControl(payCustomField5Field, pay.getCustomField5());
		Utils.updateControl(payCustomField6Field, pay.getCustomField6());
		Utils.updateControl(payCustomField7Field, pay.getCustomField7());
	}
	
	private void updatePayData() 
	{
		DataManager.i().mPay.setApproved(payApprovedCheck.isSelected());
		DataManager.i().mPay.setProcessed(payProcessedCheck.isSelected());
		DataManager.i().mPay.setHours(Float.valueOf(payHoursField.getText()));
		DataManager.i().mPay.setRate(Float.valueOf(payRateField.getText()));
		DataManager.i().mPay.setAmount(Float.valueOf(payAmountField.getText()));
		float hpd = getHoursPerDay(DataManager.i().mPay.getHours(),DataManager.i().mPay );
		if (hpd > 0)
			DataManager.i().mPay.setHoursPerDay(hpd);
		else
			DataManager.i().mPay.setHoursPerDay(Float.valueOf(payHoursPerDayField.getText()));
		DataManager.i().mPay.setType(PayCodeType.valueOf(payPayCodeTypeChoice.getValue()));
		DataManager.i().mPay.setUnionType(payUnionTypeChoice.getValue());
		DataManager.i().mPay.setTypeChangeVerified(payTypeChangeVerifiedCheck.isSelected());
		// custom fields
		DataManager.i().mPay.setCustomField1(payCustomField1Field.getText());
		DataManager.i().mPay.setCustomField2(payCustomField2Field.getText());
		DataManager.i().mPay.setCustomField3(payCustomField3Field.getText());
		DataManager.i().mPay.setCustomField4(payCustomField4Field.getText());
		DataManager.i().mPay.setCustomField5(payCustomField5Field.getText());
		DataManager.i().mPay.setCustomField6(payCustomField6Field.getText());
		DataManager.i().mPay.setCustomField7(payCustomField7Field.getText());
	}
	
	private float getHoursPerDay(float hours, Pay pay) {
		float hpd = 0;
		if (pay.getPayPeriod() != null) {
			float days = 1 + Utils.daysBetween(pay.getPayPeriod().getStartDate(), pay.getPayPeriod().getEndDate());
			hpd = hours/days; 
		}	
		return hpd;
	}
	
	
	private boolean validateFields() 
	{
		if (Utils.validateFloatTextField(payHoursField) == false) return false;
		if (Utils.validateFloatTextField(payRateField) == false) return false;
		if (Utils.validateFloatTextField(payAmountField) == false) return false;
		if (Utils.validateFloatTextField(payHoursPerDayField) == false) return false;
		
		return true;
	}
	
	@FXML
	private void onSave(ActionEvent event) 
	{
		try
		{
			if (paySaveButton.getText().equals("Reactivate") == false)
				if (validateFields() == false)
					return;
			
			updatePayData();
			// update the employee
			DataManager.i().mPay.setEmployee(DataManager.i().mEmployee);

			PayRequest request = new PayRequest();
			request.setEntity(DataManager.i().mPay);
			request.setId(DataManager.i().mPay.getId());
			DataManager.i().mPay = AdminPersistenceManager.getInstance().addOrUpdate(request);
			
			//mark changes made
			changesMade = true;
			// and exit the pop up
			exitPopup();
		}catch(CoreException e)
		{
			Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE, "Exception.", e);
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void exitPopup() 
	{
		Stage stage = (Stage) payHoursField.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		if (userChanges == true) {
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Changes Made");
		    confirmAlert.setContentText("Are You Sure You Want To Exit Without Saving Changes?");
		    EtcAdmin.i().positionAlertCenter(confirmAlert);
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if ((result.isPresent()) && (result.get() != ButtonType.OK)) {
		    	return;
		    }
		}
		
		exitPopup();
	}	
	
	private void onChange(ActionEvent event) {
		onChange();
	}
	
	@FXML
	private void onShowSystemInfo() {
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mPay;
			DataManager.i().mCurrentCoreDataType = SystemDataType.PAY;
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
	        if (sysInfoController.changesMade == true) {
	        	changesMade = true;
	        	showPay();
	        }
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	@FXML
	private void onChangePayPeriod(ActionEvent event) {
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employee/ViewPayPeriodSelect.fxml"));
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
		
		// update the screen
		showPay();
	}
	
	private void onChange() {
		if (updated == false) return;
		userChanges = true;
		payProcessedCheck.setSelected(false);
	}
	
}


