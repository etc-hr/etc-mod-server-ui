package com.etc.admin.ui.taxyear;

import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ViewCalculationNoticeController {
	@FXML
	private TextArea evntDescriptionArea;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		updateEventData();
	}
	
	private void updateEventData() {
		if (DataManager.i().mCalculationNotice == null) return;
		Utils.updateControl(evntDescriptionArea, DataManager.i().mCalculationNotice.getMessage());
	}
	
	@FXML
	private void onClose(ActionEvent event) {
		Stage stage = (Stage) evntDescriptionArea.getScene().getWindow();
		stage.close();
	}	
	
}


