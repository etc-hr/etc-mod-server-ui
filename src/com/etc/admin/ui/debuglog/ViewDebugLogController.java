package com.etc.admin.ui.debuglog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import com.etc.admin.EmsApp;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Logging;
import com.etc.admin.utils.Utils;

public class ViewDebugLogController {
	
	@FXML
	private ListView<String> dbgDebugLogListView;
	@FXML
	private CheckBox dbgSevereOnlyCheck;
	@FXML
	private CheckBox dbgDebugOnlyCheck;
	@FXML
	private CheckBox dbgIgnoreWarningCheck;
	@FXML
	private CheckBox dbgIgnoreInfoCheck;
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	@FXML
	public void initialize() {
		loadDebugLog();
	}
	
	private void loadDebugLog() {
	    
        try {
        	dbgDebugLogListView.getItems().clear();
			String userDir = System.getProperty("user.home"); 
			String pathSeparator = System.getProperty("file.separator"); 
			
            File file = new File(userDir + pathSeparator +  
					".xarriot" + pathSeparator +  
					"apps"+ pathSeparator +  
					EmsApp.getInstance().getApplicationHomeFolderName()  + pathSeparator +  
					"logs" + pathSeparator + 
            		"log0.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
			while ((line = br.readLine()) != null) {
				if (dbgSevereOnlyCheck.isSelected() == true && line.contains("SEVERE") == false) continue;
				if (dbgDebugOnlyCheck.isSelected() == true && line.contains("DEBUG") == false && line.contains("debug") == false  && line.contains("Debug") == false) continue;
				if (dbgIgnoreWarningCheck.isSelected() == true && line.contains("WARNING:") == true) continue;
				if (dbgIgnoreInfoCheck.isSelected() == true) {
					if (line.contains("INFO:") == true) continue;
					if (line.contains("admin.EmsApp") == true) continue;
					if (line.contains("xarriot.Xarriot") == true) continue;
					if (line.contains("updateCache") == true) continue;
					if (line.contains("updateSecondTier") == true) continue;
					if (line.contains("queue.QueuedDatabaseRequired") == true) continue;
					if (line.contains("task.LoginTask") == true) continue;
				}
				dbgDebugLogListView.getItems().add(line);
			}
	        br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
        
	}	
	
	@FXML
	private void onClose(ActionEvent event) {
		Stage stage = (Stage) dbgDebugLogListView.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	private void onSevereCheck(ActionEvent event) {
		loadDebugLog();
	}	
	
	@FXML
	private void onDebugOnlyCheck(ActionEvent event) {
		loadDebugLog();
	}	
	
}

