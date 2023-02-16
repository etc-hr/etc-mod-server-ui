package com.etc.admin.ui.pipeline.filespecification;

import com.etc.admin.data.DataManager;
import com.etc.admin.ui.pipeline.mapper.MapperField;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.PipelineParsePattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewParsePatternRefController
{
	@FXML
	private TextField prseIdLabel;
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
	private ComboBox<String> prsePatternCombo;
	@FXML
	private Button prseSaveButton;
	
	MapperField mf = null;

	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize()
	{
		initControls();
		loadPatterns();

		if(DataManager.i().mPipelineParsePattern != null)
		{
			//if we have a current parse pattern, load it
			updateParsePatternData();
		}
	}

	private void initControls()
	{
		//disable the save button until something is selected
		prseSaveButton.setDisable(true);

		Utils.updateControl(prseIdLabel,"");
		Utils.updateControl(prseNameLabel,"");
		Utils.updateControl(prseDescriptionLabel,"");
		Utils.updateControl(prseExampleSourceLabel,"");
		Utils.updateControl(prseExampleResultLabel,"");
		Utils.updateControl(prsePatternLabel,"");
 	}	

	private void updateParsePatternData()
	{
		PipelineParsePattern pattern = DataManager.i().mPipelineParsePattern;
		if(pattern != null)
		{
			prsePatternCombo.getSelectionModel().select(pattern.getId().toString() + ": " + pattern.getDescription() + " - " + pattern.getPattern());
			prsePatternCombo.hide();
			Utils.updateControl(prseIdLabel,pattern.getId());
			Utils.updateControl(prseNameLabel,pattern.getName());
			Utils.updateControl(prseDescriptionLabel,pattern.getDescription());
			Utils.updateControl(prseExampleSourceLabel,pattern.getExampleSource());
			Utils.updateControl(prseExampleResultLabel,pattern.getExampleResult());
			Utils.updateControl(prsePatternLabel,pattern.getPattern());
			
			//enable the save button
			prseSaveButton.setDisable(!Utils.userCanEdit());
		}
	}
	
	@FXML
	private void onSave(ActionEvent event) {
		Stage stage = (Stage) prseSaveButton.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	private void onCancel(ActionEvent event) {
		Stage stage = (Stage) prseSaveButton.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	private void onClear(ActionEvent event) {
		DataManager.i().mPipelineParsePattern = null;
		Stage stage = (Stage) prseSaveButton.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	public void onSelection() {
		DataManager.i().setPipelineParsePattern(prsePatternCombo.getSelectionModel().getSelectedIndex());
		updateParsePatternData();
	}

	public void loadPatterns() 
	{
		if(DataManager.i().mPipelineParsePatterns == null) return;

		ObservableList<String> parsePatterns = FXCollections.observableArrayList();
		String searchItem = "";
		
		for(PipelineParsePattern pattern : DataManager.i().mPipelineParsePatterns)
		{
			searchItem = pattern.getId().toString() + ": " + pattern.getDescription() + " - " + pattern.getPattern();
			parsePatterns.add(searchItem);
		};		

		//finally, set our filtered items as the combobox collection
        prsePatternCombo.setItems(parsePatterns);	

		//close the dropdown if it is showing
        prsePatternCombo.hide();
	}
}
