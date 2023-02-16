package com.etc.admin.ui.pipeline.specification;


import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewPipelineSpecificationEditController 
{
	@FXML
	private TextField pspcNameLabel;
	@FXML
	private TextField pspcDescriptionLabel;
	@FXML
	private ComboBox<String> pspcDynamicFileSpecificationCombo;
	@FXML
	private ComboBox<String> pspcDynamicFileCalculatorCombo;
	@FXML
	private ComboBox<String> pspcInitializerCombo;
	@FXML
	private ComboBox<String> pspcParserCombo;
	@FXML
	private ComboBox<String> pspcValidatorCombo;
	@FXML
	private ComboBox<String> pspcConverterCombo;
	@FXML
	private ComboBox<String> pspcFinalizerCombo;
	@FXML
	private Label pspcCoreIdLabel;
	@FXML
	private Label pspcCoreActiveLabel;
	@FXML
	private Label pspcCoreBODateLabel;
	@FXML
	private Label pspcCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() 
	{
		initControls();
		updatePipelineSpecificatinData();		
	}

	private void initControls() {
		//load the selection combos with possible choices
		loadFileSpecifications();
	}	
	
	private void loadFileSpecifications() 
	{
		ObservableList<String> fileSpecs = FXCollections.observableArrayList();
		if (DataManager.i().mDynamicCoverageFileSpecifications != null)
			for (int i=0; i < DataManager.i().mDynamicCoverageFileSpecifications.size(); i++)
				fileSpecs.add(DataManager.i().mDynamicCoverageFileSpecifications.get(i).getName());
		pspcDynamicFileSpecificationCombo.setItems(fileSpecs);
	}

	private void updatePipelineSpecificatinData()
	{
		PipelineSpecification pSpecification = DataManager.i().mPipelineSpecification;
		if (pSpecification != null) {
			Utils.updateControl(pspcNameLabel,pSpecification.getName());
			Utils.updateControl(pspcDescriptionLabel,pSpecification.getDescription());

			//core data read only
			Utils.updateControl(pspcCoreIdLabel,String.valueOf(pSpecification.getId()));
			Utils.updateControl(pspcCoreActiveLabel,String.valueOf(pSpecification.isActive()));
			Utils.updateControl(pspcCoreBODateLabel,pSpecification.getBornOn());
			Utils.updateControl(pspcCoreLastUpdatedLabel,pSpecification.getLastUpdated());
		}
	}
	
	private void updatePipelineSpecification()
	{
		PipelineSpecification pSpecification = DataManager.i().mPipelineSpecification;
		if(pSpecification != null)
		{
			pSpecification.setName(pspcNameLabel.getText());
			pSpecification.setDescription(pspcDescriptionLabel.getText());
		}
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINESPECIFICATION);
	}	
	
	private boolean validateData() {
		boolean bReturn = true;
		
		//add items to validate here
		return bReturn;
	}
	
	@FXML
	private void onSave(ActionEvent event)
	{
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updatePipelineSpecification();
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.PIPELINESPECIFICATION, true);
	}		
	
	@FXML
	private void onViewSpecification(ActionEvent event) {
		DataManager.i().loadDynamicFileSpecification(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
	}	

	@FXML
	private void onViewCalculator(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINECOVERAGEFILEEDIT);
	}	
}


