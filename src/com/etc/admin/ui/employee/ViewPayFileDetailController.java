package com.etc.admin.ui.employee;

import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.pay.PayFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewPayFileDetailController 
{
	@FXML
	private TextField BatchIdField;
	@FXML
	private TextField BornOnField;
	@FXML
	private TextField CoreIdField;
	@FXML
	private TextField DocIdField;
	@FXML
	private TextField FileNameField;
	@FXML
	private TextField UserField;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	boolean updated = false;
	boolean userChanges = false;
	@FXML
	public void initialize() 
	{
		showPayFile();
	}
	
	private void showPayFile() 
	{
		PayFile pf = DataManager.i().mPayFile;
		if (pf == null) return;
		
		Utils.updateControl(BatchIdField, pf.getBatchId());
		Utils.updateControl(BornOnField, pf.getBornOn());
		Utils.updateControl(CoreIdField, pf.getId());
		if (pf.getPipelineInformation() != null) {
			Utils.updateControl(DocIdField, pf.getPipelineInformation().getDocumentId());
			if (pf.getPipelineInformation().getDocument() != null ) {
				Utils.updateControl(FileNameField, pf.getPipelineInformation().getDocument().getFileName());
				if (pf.getPipelineInformation().getDocument().getCreatedBy() != null) {
					String Name = pf.getPipelineInformation().getDocument().getCreatedBy().getLastName() + " " + 
						 	      pf.getPipelineInformation().getDocument().getCreatedBy().getFirstName();
					Utils.updateControl(UserField, Name);
				}
			}
		}

	}
	
	private void exitPopup() 
	{
		Stage stage = (Stage) BatchIdField.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
}


