package com.etc.admin.ui.pipeline.parsedateformat;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.PipelineParseDateFormat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class ViewParseDateFormatController {
	
	@FXML
	private TextField prseNameLabel;
	@FXML
	private TextField prseDescriptionLabel;
	@FXML
	private TextField prseExampleSourceLabel;
	@FXML
	private TextField prsePatternLabel;
	@FXML
	private TextArea prseFormatInfo;
	@FXML
	private ComboBox<String> prseFormatCombo;
	@FXML
	private Button prseEditButton;
	@FXML
	private Button prseAddButton;
	@FXML
	private Label prseCoreIdLabel;
	@FXML
	private Label prseCoreActiveLabel;
	@FXML
	private Label prseCoreBODateLabel;
	@FXML
	private Label prseCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */

	@FXML
	public void initialize() {
		initControls();
		
		loadSearchDateFormats();
		
		//if we have a current date format, load it
		if (DataManager.i().mPipelineParseDateFormat != null)
			updateDateFormatData();
		
	}

	private void initControls() {
		//load regex reference for the user
		setFormatInfo();
		
		//disable the edit buttonmuntil something is selected
		prseEditButton.setDisable(true);
		//set functionality according to the user security level
		prseAddButton.setDisable(!Utils.userCanAdd());
	    
 	}	

	private void updateDateFormatData(){

		PipelineParseDateFormat pattern = DataManager.i().mPipelineParseDateFormat;
		if (pattern != null) {
			Utils.updateControl(prseNameLabel,pattern.getName());
			Utils.updateControl(prseDescriptionLabel,pattern.getDescription());
			Utils.updateControl(prseExampleSourceLabel,pattern.getExampleSource());
			Utils.updateControl(prsePatternLabel,pattern.getFormat());
				
			//core data read only
			Utils.updateControl(prseCoreIdLabel,String.valueOf(pattern.getId()));
			Utils.updateControl(prseCoreActiveLabel,String.valueOf(pattern.isActive()));
			Utils.updateControl(prseCoreBODateLabel,pattern.getBornOn());
			Utils.updateControl(prseCoreLastUpdatedLabel,pattern.getLastUpdated());	
			
			//enable the edit button
			prseEditButton.setDisable(!Utils.userCanEdit());

		}
	}
	
	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINEPARSEDATEFORMATEDIT, true);
	}	
	
	@FXML
	private void onAddDateFormat(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINEPARSEDATEFORMATADD, true);
	}	
	
	@FXML
	public void onSearchHide() {
		// if we already have an account selected, show it
		if (prseFormatCombo.getValue() != "")
			searchFormats();
	}
	
	public void loadSearchDateFormats() {
		if (DataManager.i().mPipelineParseDateFormats == null) return;
		
		ObservableList<String> parseDateFormats = FXCollections.observableArrayList();
		for (int i = 0; i < DataManager.i().mPipelineParseDateFormats.size();i++) {
			parseDateFormats.add(DataManager.i().mPipelineParseDateFormats.get(i).getName());
		};		
		
		// use a filterlist to wrap the account names for combobox searching later
        FilteredList<String> filteredItems = new FilteredList<String>(parseDateFormats, p -> true);

        // add a listener for the edit control on the combobox
        // the listener will filter the list according to what is in the search box edit control
        prseFormatCombo.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> {
            final javafx.scene.control.TextField editor = prseFormatCombo.getEditor();
            final String selected = prseFormatCombo.getSelectionModel().getSelectedItem();
            
            prseFormatCombo.show();
            prseFormatCombo.setVisibleRowCount(10);

            // Run on the GUI thread
            Platform.runLater(() -> {
                if (selected == null || !selected.equals(editor.getText())) {
                    filteredItems.setPredicate(item -> {
                        if (item.toUpperCase().contains(newvalue.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
		});
        
		//finally, set our filtered items as the combobox collection
        prseFormatCombo.setItems(filteredItems);	
		
		//close the dropdown if it is showing
        prseFormatCombo.hide();
	}
	
	public void onSearchFormats(ActionEvent event) {
		searchFormats();
	}

	public void searchFormats() {
		
		if (DataManager.i().mPipelineParseDateFormats == null) return;
		
		String sSelection = prseFormatCombo.getValue();
		for (int i = 0; i < DataManager.i().mPipelineParseDateFormats.size();i++) {
			if (DataManager.i().mPipelineParseDateFormats.get(i).getName().equals(sSelection)) {			
				//load the user
				setParseDateFormat(i);
				break;
			}
		}
	}	
		
	public void setParseDateFormat(int nFormatID) {
		DataManager.i().setPipelineParseDateFormat(nFormatID);
		updateDateFormatData();
	}
	
	private void setFormatInfo() {
		
		prseFormatInfo.setText("Date Format Specifiers\n\n"
		+ "y   = year (yy or yyyy)\n"
		+ "M   = month (MM)\n"
		+ "d   = day in month (dd)\n"
		+ "h   = hour (0-12) (hh)\n"
		+ "H   = hour (0-23) (HH)\n"
		+ "m   = minute in hour (mm)\n"
		+ "s   = seconds (ss)\n"
		+ "S   = milliseconds (SSS)\n"
		+ "z   = time zone text (Pacific Standard Time...)\n"
		+ "Z   = time zone, time offset (-0800)\n"
		+ "\nEXAMPLES\n\n"
		+ "yyyy-MM-dd                 (2018-12-31)\n" 
		+ "dd-MM-YYYY                 (31-12-2018)\n"
		+ "yyyy-MM-dd HH:mm:ss        (2018-12-31 23:59:59)\n"
		+ "HH:mm:ss.SSS               (23:59.59.999)\n"
		+ "yyyy-MM-dd HH:mm:ss.SSS    (2018-12-31 23:59:59.999)\n"
		+ "yyyy-MM-dd HH:mm:ss.SSS Z  (2018-12-31 23:59:59.999 +0100)");
	}
	
}


