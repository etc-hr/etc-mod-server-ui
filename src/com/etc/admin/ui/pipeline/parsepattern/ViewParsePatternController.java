package com.etc.admin.ui.pipeline.parsepattern;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.PipelineParsePattern;

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

public class ViewParsePatternController {
	
	@FXML
	private TextField prseNameLabel;
	@FXML
	private TextField prseDescriptionLabel;
	@FXML
	private TextField prseExampleSourceLabel;
	@FXML
	private TextField prseExampleResultLabel;
	@FXML
	private TextArea prsePatternLabel;
	@FXML
	private TextArea prseRegexInfo;
	@FXML
	private ComboBox<String> prsePatternCombo;
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
		
		loadSearchPatterns();
		
		//if we have a current date format, load it
		if (DataManager.i().mPipelineParsePattern != null)
			updateParsePatternData();
				
	}

	private void initControls() {
		//load regex reference for the user
		setRegexInfo();
		
		//disable the edit button until something is selected
		prseEditButton.setDisable(true);
		//set functionality according to the user security level
		prseAddButton.setDisable(!Utils.userCanAdd());
	    
 	}	

	private void updateParsePatternData(){
		PipelineParsePattern pattern = DataManager.i().mPipelineParsePattern;
		if (pattern != null) {
			Utils.updateControl(prseNameLabel,pattern.getName());
			Utils.updateControl(prseDescriptionLabel,pattern.getDescription());
			Utils.updateControl(prseExampleSourceLabel,pattern.getExampleSource());
			Utils.updateControl(prseExampleResultLabel,pattern.getExampleResult());
			Utils.updateControl(prsePatternLabel,pattern.getPattern());
				
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
		EtcAdmin.i().setScreen(ScreenType.PIPELINEPARSEPATTERNEDIT, true);
	}	
	
	@FXML
	private void onAddPattern(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINEPARSEPATTERNADD, true);
	}	
	
	@FXML
	public void onSearchHide() {
		// if we already have an account selected, show it
		if (prsePatternCombo.getValue() != "")
			searchPatterns();
	}
	
	
	public void loadSearchPatterns() {
		//check for updated parse patterns
		DataManager.i().loadUpdatedParsePatterns();
		
		if (DataManager.i().mPipelineParsePatterns == null) return;
		
		ObservableList<String> parsePatterns = FXCollections.observableArrayList();
		String searchItem = "";
		for (PipelineParsePattern pattern : DataManager.i().mPipelineParsePatterns) {
			searchItem = pattern.getId().toString() + ": " + pattern.getDescription() + " - " + pattern.getPattern();
			parsePatterns.add(searchItem);
		};		
		
		// use a filterlist to wrap the account names for combobox searching later
        FilteredList<String> filteredItems = new FilteredList<String>(parsePatterns, p -> true);

        // add a listener for the edit control on the combobox
        // the listener will filter the list according to what is in the search box edit control
        prsePatternCombo.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> {
            final javafx.scene.control.TextField editor = prsePatternCombo.getEditor();
            final String selected = prsePatternCombo.getSelectionModel().getSelectedItem();
            
            prsePatternCombo.show();
            prsePatternCombo.setVisibleRowCount(10);

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
        prsePatternCombo.setItems(filteredItems);	
		
		//close the dropdown if it is showing
        prsePatternCombo.hide();
	}
	
	public void onSearchPatterns(ActionEvent event) {
		searchPatterns();
	}

	public void searchPatterns() {
		
		if (DataManager.i().mPipelineParsePatterns == null) return;
		
		String sSelection = prsePatternCombo.getValue();
		String searchItem = "";
		for ( int i = 0; i < DataManager.i().mPipelineParsePatterns.size();i++) {
			searchItem = DataManager.i().mPipelineParsePatterns.get(i).getId().toString() + ": " 
						 + DataManager.i().mPipelineParsePatterns.get(i).getDescription() + " - " 
						 + DataManager.i().mPipelineParsePatterns.get(i).getPattern();

			if (searchItem.equals(sSelection)) {			
				//load the pattern by ordinal
				setParsePattern(i);
				break;
			}
		}
	}	
		
	public void setParsePattern(int nPatternID) {
		DataManager.i().setPipelineParsePattern(nPatternID);
		updateParsePatternData();
	}
	
	private void setRegexInfo() {
		
		prseRegexInfo.setText("Anchors and $\n\n"
		+ "^The        matches any string that starts with The \n"
		+ "end$        matches a string that ends with end \n"
		+ "^The end$   exact string match (starts and ends with The end)\n"
		+ "test        matches any string that has the text test in it\n"
		+ "\nQuantifiers + ? and {}\n\n"
		+ "abc*        matches a string that has ab followed by zero or more c\n"
		+ "abc+        matches a string that has ab followed by one or more c\n"
		+ "abc?        matches a string that has ab followed by zero or one c\n"
		+ "abc{2}      matches a string that has ab followed by 2 c\n"
		+ "abc{2,}     matches a string that has ab followed by 2 or more c\n"
		+ "abc{2,5}    matches a string that has ab followed by 2 up to 5 c\n"
		+ "a(bc)*      matches a string that has a followed by zero or more copies of the sequence bc\n"
		+ "a(bc){2,5}  matches a string that has a followed by 2 up to 5 copies of the sequence bc\n"
		+ "\nOR operators | or []\n\n"
		+ "a(b|c)      matches a string that has a followed by b or c\n"
		+ "a[bc]       same as previous\n"
		+ "\nCharacter classes \\d \\w \\s and .\n\n"
		+ "\\d         matches a single character that is a digit\n"
		+ "\\D         matches a single non-digit character\n"
		+ "\\w         matches a word character (alphanumeric character plus underscore)\n"
		+ "\\W         matches a non-word character (alphanumeric character plus underscore)\n"
		+ "\\s         matches a whitespace character (includes tabs and line breaks)\n"
		+ "\\S         matches a non-whitespace character (includes tabs and line breaks)\n"
		+ ".          matches any character\n"	
		+ "\\$\\d       matches a string that has a $ before one digit\n"
		+ "\nBracket expressions []\n\n"
		+ "[abc]       matches a string that has either an a or a b or a c\n"
		+ "[a-c]       same as previous\n"
		+ "[a-fA-F0-9] string that represents a single hexadecimal digit, case insensitive\n"
		+ "[0-9]%      string that has a character from 0 to 9 before a % sign\n"
		+ "[^a-zA-Z]   string that has not a letter from a to z or from A to Z\n");
	}
	
}


