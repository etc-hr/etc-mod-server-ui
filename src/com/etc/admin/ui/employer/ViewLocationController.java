package com.etc.admin.ui.employer;

import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewLocationController 
{
	@FXML
	private TextField nameField;
	@FXML
	private Button editButton;
	@FXML
	private Label inactiveLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	boolean updated = false;

	@FXML
	public void initialize() 
	{
		showLocation();
	}
	
	private void showLocation() 
	{
		if (DataManager.i().mLocation == null) return;
		
		Utils.updateControl(nameField, DataManager.i().mLocation.getName());
	}
	
	private void exitPopup() 
	{
		Stage stage = (Stage) nameField.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
}


