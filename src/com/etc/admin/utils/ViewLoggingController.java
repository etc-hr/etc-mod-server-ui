package com.etc.admin.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ViewLoggingController {
	@FXML
	private ListView<String> logListView;

	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		loadLogEntries();
	}
	
	public void load() {

	}
	
	private void loadLogEntries() {

	}
	@FXML
	private void onClear(ActionEvent event) {

	}	
	
	@FXML
	private void onClose(ActionEvent event) {
		Stage stage = (Stage) logListView.getScene().getWindow();
		stage.close();
	}	
	
}


