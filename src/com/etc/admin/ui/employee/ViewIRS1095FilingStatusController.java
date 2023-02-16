package com.etc.admin.ui.employee;

import java.util.List;
import java.util.logging.Level;

import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.AirTransmission;
import com.etc.corvetto.entities.Irs1095Filing;
import com.etc.corvetto.rqs.AirTransmissionRequest;
import com.etc.entities.CoreData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewIRS1095FilingStatusController {
	@FXML
	private Label topLabel;
	@FXML
	private TextField yearField;
	@FXML
	private TextField transIdField;
	@FXML
	private TextField receiptIdField;
	@FXML
	private TextField receiptDateField;
	@FXML
	private TextField filingTypeField;
	@FXML
	private TextField statusField;
	@FXML
	private CheckBox abandoned;
	@FXML
	private TextField abandonedOnField;
	@FXML
	private TextField abandonedMethodField;
	@FXML
	private TextField abandonedReasonField;
	@FXML
	private TextField AbandonedNotesField;
	
	AirTransmission airTran = null;
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		loadData();
		showIrs1095Filing();
	}
	
	private void initControls() {

	}
	
	private void loadData() {
		// need the info from the transmission
		try {
			// first, the submission
			if (DataManager.i().mIrs1095Filing.getIrs1095Submission() != null) {
				if (DataManager.i().mIrs1095Filing.getIrs1095Submission().getIrs1094SubmissionId() != null) {
					AirTransmissionRequest req = new AirTransmissionRequest();
					req.setFetchInactive(true);
					req.setIrs1094SubmissionId(DataManager.i().mIrs1095Filing.getIrs1095Submission().getIrs1094SubmissionId());
					List<AirTransmission> airTrans = AdminPersistenceManager.getInstance().getAll(req);
					if (airTrans.size() > 0)
						airTran = airTrans.get(0);
				}
			}
		} catch(Exception e) {
			DataManager.i().log(Level.SEVERE, e);
		}
	}

	private void showIrs1095Filing(){
		Irs1095Filing filing = DataManager.i().mIrs1095Filing;
		if (filing != null) {
			String sName = "";
			if (filing.getEmployer() != null)
				sName= filing.getEmployer().getName() + " 1095 Filing Status";
			Utils.updateControl(topLabel,sName);	
			
			if (filing.getTaxYear() != null)
				Utils.updateControl(yearField, filing.getTaxYear().getYear());
			if (airTran != null) {
				Utils.updateControl(transIdField, airTran.getId());
				Utils.updateControl(receiptIdField, airTran.getReceiptId());
				Utils.updateControl(receiptDateField, airTran.getReceiptTimestamp());
			}
			
			if (filing.getFilingType() != null)
				Utils.updateControl(filingTypeField, filing.getFilingType().toString());
			if (filing.getStatus() != null)
				Utils.updateControl(statusField, filing.getStatus().toString()); 

			Utils.updateControl(abandoned, filing.isAbandoned()); 
			Utils.updateControl(abandonedOnField, filing.getAbandonedOn());
			if (filing.getAbandonedMethod() != null)
			Utils.updateControl(abandonedMethodField, filing.getAbandonedMethod().toString()); 
			if (filing.getAbandonedReason() != null)
			Utils.updateControl(abandonedReasonField, filing.getAbandonedReason().toString()); 
			Utils.updateControl(AbandonedNotesField, filing.getAbandonedNotes()); 
		}
	}
	
	@FXML
	private void onShowSystemInfo() {
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mIrs1095Filing;
			DataManager.i().mCurrentCoreDataType = SystemDataType.IRS1095FILING;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
		
	}
	
	@FXML
	private void onClose(ActionEvent event) {
		Stage stage = (Stage) topLabel.getScene().getWindow();
		stage.close();
	}	

}
