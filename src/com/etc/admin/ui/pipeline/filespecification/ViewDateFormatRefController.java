package com.etc.admin.ui.pipeline.filespecification;

import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.PipelineParseDateFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewDateFormatRefController
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
	private TextField prsePatternLabel;
	@FXML
	private TextArea prseFormatInfo;
	@FXML
	private ComboBox<String> prseFormatCombo;
	@FXML
	private Button prseSaveButton;
	
	/**
	 * initialize is called when the FXML is loaded
	 */

	@FXML
	public void initialize() 
	{
		initControls();
		loadDateFormats();
		
		//if we have a current date format, load it
		if(DataManager.i().mPipelineParseDateFormat != null)
		{
			updateDateFormatData();
		}
	}

	private void initControls()
	{
		//disable the Save button until something is selected
		prseSaveButton.setDisable(true);   
		
		Utils.updateControl(prseIdLabel,"");
		Utils.updateControl(prseNameLabel,"");
		Utils.updateControl(prseDescriptionLabel,"");
		Utils.updateControl(prseExampleSourceLabel,"");
		Utils.updateControl(prsePatternLabel,"");
		
 	}	

	private void updateDateFormatData()
	{
		PipelineParseDateFormat format = DataManager.i().mPipelineParseDateFormat;
		
		if(format != null) 
		{
			prseFormatCombo.getSelectionModel().select(format.getName());
			prseFormatCombo.hide();
			Utils.updateControl(prseIdLabel,format.getId());
			Utils.updateControl(prseNameLabel,format.getName());
			Utils.updateControl(prseDescriptionLabel,format.getDescription());
			Utils.updateControl(prseExampleSourceLabel,format.getExampleSource());
			Utils.updateControl(prsePatternLabel,format.getFormat());
				
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
		DataManager.i().mPipelineParseDateFormat = null;
		Stage stage = (Stage) prseSaveButton.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	public void onSelection() 
	{
		DataManager.i().setPipelineParseDateFormat(prseFormatCombo.getSelectionModel().getSelectedIndex());
		updateDateFormatData();
	}
	
	public void loadDateFormats()
	{
		if(DataManager.i().mPipelineParseDateFormats == null) return;
		
		ObservableList<String> parseDateFormats = FXCollections.observableArrayList();
		for(int i = 0; i < DataManager.i().mPipelineParseDateFormats.size();i++)
		{
			parseDateFormats.add(DataManager.i().mPipelineParseDateFormats.get(i).getName());
		};		
		
		//finally, set our filtered items as the combobox collection
        prseFormatCombo.setItems(parseDateFormats);	
		
		//close the dropdown if it is showing
        prseFormatCombo.hide();
	}
	
	public void setParseDateFormat(int nFormatID) {
		DataManager.i().setPipelineParseDateFormat(nFormatID);
		updateDateFormatData();
	}
}
