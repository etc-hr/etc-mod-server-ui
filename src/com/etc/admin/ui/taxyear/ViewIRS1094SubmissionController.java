package com.etc.admin.ui.taxyear;

import java.awt.Point;
import java.io.IOException;
import java.util.logging.Level;

import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.Irs1094Submission;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewIRS1094SubmissionController {
	@FXML
	private CheckBox subAuthoritativeCheck;
	@FXML
	private CheckBox subAggGroupMemberCheck;
	@FXML
	private TextField subSubmissionIdField;
	@FXML
	private TextField subOrigSubmissionIdField;
	@FXML
	private TextField subFormCountField;
	@FXML
	private TextField subAirTransmissionIdField;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		updateSubmissionData();
	}
	
	private void updateSubmissionData() {
		Irs1094Submission submission = DataManager.i().mIrs1094Submission;
		if (submission != null) {
			Utils.updateControl(subAuthoritativeCheck, submission.isAuthoritative());
			Utils.updateControl(subAggGroupMemberCheck, submission.isAggGroupMember());
			Utils.updateControl(subSubmissionIdField, submission.getSubmissionId());
			Utils.updateControl(subOrigSubmissionIdField, submission.getOriginalSubmissionId());
			Utils.updateControl(subFormCountField, submission.getFormCount());
			if (submission.getAirTransmission() != null)
				Utils.updateControl(subAirTransmissionIdField, submission.getAirTransmission().getId());
		}
	}
	
	@FXML
	private void onClose(ActionEvent event) {
		Stage stage = (Stage) subFormCountField.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	private void onViewAirTtransmission(ActionEvent event) {
		if (DataManager.i().mIrs1094Submission.getAirTransmission() == null) return;
		
		// get the airtransmission from the server using the existing datamanager object
		//FIXME: DataManager.i().getAirTransmission(DataManager.i().mIrs1094Submission.getAirTransmission());
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/taxyear/ViewAirTransmission.fxml"));
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
	
}


