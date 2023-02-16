package com.etc.admin.ui.pipeline.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileStepHandler;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;
import com.etc.corvetto.ems.pipeline.entities.c95.DynamicIrs1095cFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.ded.DynamicDeductionFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.ins.DynamicInsuranceFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.PayConversionCalculation;
import com.etc.corvetto.ems.pipeline.rqs.PipelineSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.c95.DynamicIrs1095cFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.cov.DynamicCoverageFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.ded.DynamicDeductionFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.ins.DynamicInsuranceFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.PayConversionCalculationRequest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewPipelineSpecificationAddController 
{
	@FXML
	private TextField pspcNameLabel;
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
	private Label pspcCalcTypeLabel;
	@FXML
	private ComboBox<String> pspcCalcTypeCombo;
	@FXML
	private Label pspcTopLabel;
	@FXML
	private CheckBox pspcExportDMZCheck;
	
    List<PipelineFileStepHandler> initializers = new ArrayList<>();
    List<PipelineFileStepHandler> parsers = new ArrayList<>();
    List<PipelineFileStepHandler> validators = new ArrayList<>();
    List<PipelineFileStepHandler> converters = new ArrayList<>();
    List<PipelineFileStepHandler> finalizers = new ArrayList<>();
    
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
	}

	private void initControls() 
	{
		//set the channel type
		pspcTopLabel.setText("Add New " + DataManager.i().mPipelineChannel.getType().toString() + " Pipeline Specification and Mapper");
		
		// Setup the file step handler combos
	    initializers.clear();
	    parsers.clear();
	    validators.clear();
	    converters.clear();
	    finalizers.clear();
	    
	    //reset the text fields
		pspcNameLabel.setText("");
		pspcNameLabel.setStyle(null);

	    //separate into types for use on selection
	    for(PipelineFileStepHandler pipelineFileStepHandler : DataManager.i().mPipelineFileStepHandlers ) 
	    {
	    	// fiulter out any that don't match the current channel 
	    	if(DataManager.i().mPipelineChannel.getId().equals(pipelineFileStepHandler.getChannelId()) == false) continue;
	    	switch(pipelineFileStepHandler.getType()) {
	    	case INITIALIZER:
	    		initializers.add(pipelineFileStepHandler);
	    		break;
	    	case PARSER:
	    		parsers.add(pipelineFileStepHandler);
	    		break;
	    	case VALIDATOR:
	    		validators.add(pipelineFileStepHandler);
	    		break;
	    	case CONVERTER:
	    		converters.add(pipelineFileStepHandler);
	    		break;
	    	case FINALIZER:
	    		finalizers.add(pipelineFileStepHandler);
	    		break;
	    	}
	    }
	    
	    // fill the combos
	    // initializers
	    List<String> initializerList =  new ArrayList<>();
	    for(PipelineFileStepHandler initializer : initializers) {
	    	initializerList.add(initializer.getName());
	    }
	    ObservableList<String> observableInitializerList = FXCollections.observableList(initializerList);
	    pspcInitializerCombo.setItems(observableInitializerList);
	    
		// parsers
	    List<String> parserList =  new ArrayList<>();
	    for(PipelineFileStepHandler parser : parsers) {
	    	parserList.add(parser.getName() + "-" + parser.getHandlerClass().substring(1 + parser.getHandlerClass().lastIndexOf(".")));
	    }
	    ObservableList<String> observableParserList = FXCollections.observableList(parserList);
	    pspcParserCombo.setItems(observableParserList);
	    
		// validators
	    List<String> validatorList =  new ArrayList<>();
	    for(PipelineFileStepHandler validator : validators) {
	    	validatorList.add(validator.getName() + " - " + validator.getHandlerClass().substring(1 + validator.getHandlerClass().lastIndexOf(".")));
	    }
	    ObservableList<String> observableValidatorList = FXCollections.observableList(validatorList);
	    pspcValidatorCombo.setItems(observableValidatorList);
		// converters
	    List<String> converterList =  new ArrayList<>();
	    for(PipelineFileStepHandler converter : converters) {
	    	converterList.add(converter.getName() + "-" + converter.getHandlerClass().substring(1 + converter.getHandlerClass().lastIndexOf(".")));
	    }
	    ObservableList<String> observableConverterList = FXCollections.observableList(converterList);
	    pspcConverterCombo.setItems(observableConverterList);
		// finalizers
	    List<String> finalizerList =  new ArrayList<>();
	    for(PipelineFileStepHandler finalizer : finalizers) {
	    	finalizerList.add(finalizer.getName() + "-" + finalizer.getHandlerClass().substring(1 + finalizer.getHandlerClass().lastIndexOf(".")));
	    }
	    ObservableList<String> observableFinalizerList = FXCollections.observableList(finalizerList);
	    pspcFinalizerCombo.setItems(observableFinalizerList);
	    
	    loadCalcList();
	    pspcCalcTypeCombo.setVisible(false);
	    pspcCalcTypeLabel.setVisible(false);
	    
	    Utils.addTextLimiter(pspcNameLabel, 25);
	}	
	
	private void loadCalcList() 
	{
		try {
			PayConversionCalculationRequest request = new PayConversionCalculationRequest();
			DataManager.i().mPayConversionCalculations = AdminPersistenceManager.getInstance().getAll(request);

		} catch (CoreException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}	
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
		pspcCalcTypeCombo.getItems().clear();
		
		for(PayConversionCalculation  calc : DataManager.i().mPayConversionCalculations) {
			pspcCalcTypeCombo.getItems().add(calc.getId().toString() + " - " + calc.getName());
		}
	}
	

	private void updatePipelineSpecification(PipelineSpecification spec)
	{
		spec.setName(pspcNameLabel.getText());
		spec.setDescription(pspcNameLabel.getText());
		
		// step handlers
		if(pspcInitializerCombo.getSelectionModel().getSelectedIndex() > -1)
			spec.setInitializerId(initializers.get(pspcInitializerCombo.getSelectionModel().getSelectedIndex()).getId());
		if(pspcParserCombo.getSelectionModel().getSelectedIndex() > -1)
			spec.setParserId(parsers.get(pspcParserCombo.getSelectionModel().getSelectedIndex()).getId());
		if(pspcValidatorCombo.getSelectionModel().getSelectedIndex() > -1)
			spec.setValidatorId(validators.get(pspcValidatorCombo.getSelectionModel().getSelectedIndex()).getId());
		if(pspcConverterCombo.getSelectionModel().getSelectedIndex() > -1)
			spec.setConverterId(converters.get(pspcConverterCombo.getSelectionModel().getSelectedIndex()).getId());
		if(pspcFinalizerCombo.getSelectionModel().getSelectedIndex() > -1)
			spec.setFinalizerId(finalizers.get(pspcFinalizerCombo.getSelectionModel().getSelectedIndex()).getId());

		// set any defaults
		spec.setDynamicFileCalculatorId(0l);
		spec.setChannelId(DataManager.i().mPipelineChannel.getId());

		if(spec.getInitializerId() == null) spec.setInitializerId(0l);
		if(spec.getParserId() == null) spec.setParserId(0l);
		if(spec.getValidatorId() == null) spec.setValidatorId(0l);
		if(spec.getConverterId() == null) spec.setConverterId(0l);
		if(spec.getFinalizerId() == null) spec.setFinalizerId(0l);
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		Stage stage = (Stage) pspcConverterCombo.getScene().getWindow();
		stage.close();
	}	
	
	private boolean validateData() 
	{
		boolean bReturn = true;
		
		//check for required data
		if(!Utils.validate(pspcNameLabel)) bReturn = false;
		if(!Utils.validate(pspcParserCombo)) bReturn = false;
		if(!Utils.validate(pspcConverterCombo)) bReturn = false;
		
		return bReturn;
	}
	
	@FXML
	private void onChangeConverter(ActionEvent event)
	{
		if(pspcConverterCombo.getValue().contains("Calc"))	
		{   
			pspcCalcTypeCombo.setVisible(true);
		    pspcCalcTypeLabel.setVisible(true);
		} else {
			pspcCalcTypeCombo.setVisible(false);
		    pspcCalcTypeLabel.setVisible(false);
		}
	}
	
	@FXML
	private void onSave(ActionEvent event) throws CoreException 
	{
		//validate everything
		if(!validateData())
			return;
		
		//update our object
		PipelineSpecification spec = new PipelineSpecification();
		updatePipelineSpecification(spec);
		
		// Create a dependent dynamicfilespec
		switch (DataManager.i().mPipelineChannel.getType())
		{
			case COVERAGE:
				//create and save a mapper
				DynamicCoverageFileSpecificationRequest covFileSpecRequest = new DynamicCoverageFileSpecificationRequest();
				DynamicCoverageFileSpecification covFileSpec = new DynamicCoverageFileSpecification();
				covFileSpecRequest.setEntity(covFileSpec);
				covFileSpec.setName(spec.getName());
				covFileSpec = AdminPersistenceManager.getInstance().addOrUpdate(covFileSpecRequest);
				// bail if null
				if(covFileSpec == null) return;
				
				// update the spec
				spec.setDynamicFileSpecificationId(covFileSpec.getId());
				if(pspcExportDMZCheck.isSelected() == true) spec.setExportSpecificationId(3l);
				break;
				
			case DEDUCTION:
				// create the emp file spec
				DynamicDeductionFileSpecificationRequest dedFileSpecRequest = new DynamicDeductionFileSpecificationRequest();
				DynamicDeductionFileSpecification dedFileSpec = new DynamicDeductionFileSpecification();
				dedFileSpecRequest.setEntity(dedFileSpec);
				dedFileSpec.setName(spec.getName());
				dedFileSpec.setCreateEmployee(true);
				dedFileSpec = AdminPersistenceManager.getInstance().addOrUpdate(dedFileSpecRequest);
				
				// bail if null
				if(dedFileSpec == null) return;
				
				//update the spec
				spec.setDynamicFileSpecificationId(dedFileSpec.getId());
				if(pspcExportDMZCheck.isSelected() == true) spec.setExportSpecificationId(2l);
				if(pspcCalcTypeCombo.isVisible() == true && pspcCalcTypeCombo.getValue() != null) // && pspcCalcTypeCombo.getValue().getCalc() != null) 
					spec.setDynamicFileCalculatorId(DataManager.i().mPayConversionCalculations.get(pspcCalcTypeCombo.getSelectionModel().getSelectedIndex()).getId());// getValue().getCalc().getId());
				break;
				
			case EMPLOYEE:
				// create the emp file spec
				DynamicEmployeeFileSpecificationRequest empFileSpecRequest = new DynamicEmployeeFileSpecificationRequest();
				DynamicEmployeeFileSpecification empFileSpec = new DynamicEmployeeFileSpecification();
				empFileSpecRequest.setEntity(empFileSpec);
				empFileSpec.setName(spec.getName());
				empFileSpec.setCreateEmployee(true);
				empFileSpec = AdminPersistenceManager.getInstance().addOrUpdate(empFileSpecRequest);

				// bail if null
				if(empFileSpec == null) return;
				
				//update the file spec
				spec.setDynamicFileSpecificationId(empFileSpec.getId());
				if(pspcExportDMZCheck.isSelected() == true) spec.setExportSpecificationId(2l);
				break;
				
			case INSURANCE:
				// create the emp file spec
				DynamicInsuranceFileSpecificationRequest insFileSpecRequest = new DynamicInsuranceFileSpecificationRequest();
				DynamicInsuranceFileSpecification insFileSpec = new DynamicInsuranceFileSpecification();
				insFileSpecRequest.setEntity(insFileSpec);
				insFileSpec.setName(spec.getName());
				insFileSpec.setCreateEmployee(true);
				insFileSpec = AdminPersistenceManager.getInstance().addOrUpdate(insFileSpecRequest);
				
				// bail if null
				if(insFileSpec == null) return;
				
				//update the spec
				spec.setDynamicFileSpecificationId(insFileSpec.getId());
				if(pspcExportDMZCheck.isSelected() == true) spec.setExportSpecificationId(2l);
				if(pspcCalcTypeCombo.isVisible() == true && pspcCalcTypeCombo.getValue() != null) // && pspcCalcTypeCombo.getValue().getCalc() != null) 
					spec.setDynamicFileCalculatorId(DataManager.i().mPayConversionCalculations.get(pspcCalcTypeCombo.getSelectionModel().getSelectedIndex()).getId());// getValue().getCalc().getId());
				break;
				
			case IRS1095C:
				//create and save a mapper
				DynamicIrs1095cFileSpecificationRequest irs1095cFileSpecRequest = new DynamicIrs1095cFileSpecificationRequest();
				DynamicIrs1095cFileSpecification irs1095cFileSpec = new DynamicIrs1095cFileSpecification();
				irs1095cFileSpecRequest.setEntity(irs1095cFileSpec);
				irs1095cFileSpec.setName(spec.getName());
				irs1095cFileSpec = AdminPersistenceManager.getInstance().addOrUpdate(irs1095cFileSpecRequest);
				// bail if null
				if(irs1095cFileSpec == null) return;
				
				// update the spec
				spec.setDynamicFileSpecificationId(irs1095cFileSpec.getId());
				if(pspcExportDMZCheck.isSelected() == true) spec.setExportSpecificationId(3l);
				break;
				
			case PAY:
				// create the emp file spec
				DynamicPayFileSpecificationRequest payFileSpecRequest = new DynamicPayFileSpecificationRequest();
				DynamicPayFileSpecification payFileSpec = new DynamicPayFileSpecification();
				payFileSpecRequest.setEntity(payFileSpec);
				payFileSpec.setName(spec.getName());
				payFileSpec.setCreateEmployee(true);
				payFileSpec = AdminPersistenceManager.getInstance().addOrUpdate(payFileSpecRequest);
				
				// bail if null
				if(payFileSpec == null) return;
				
				//update the spec
				spec.setDynamicFileSpecificationId(payFileSpec.getId());
				if(pspcExportDMZCheck.isSelected() == true) spec.setExportSpecificationId(2l);
				if(pspcCalcTypeCombo.isVisible() == true && pspcCalcTypeCombo.getValue() != null) // && pspcCalcTypeCombo.getValue().getCalc() != null) 
					spec.setDynamicFileCalculatorId(DataManager.i().mPayConversionCalculations.get(pspcCalcTypeCombo.getSelectionModel().getSelectedIndex()).getId());// getValue().getCalc().getId());
				break;
				
			default:
				return;
		}
		
		//create the request
		PipelineSpecificationRequest specRequest = new PipelineSpecificationRequest();
		specRequest.setEntity(spec);
		
		// create it
		DataManager.i().mPipelineSpecification = AdminPersistenceManager.getInstance().addOrUpdate(specRequest);
		
		DataManager.i().mPipelineSpecification.setChannel(DataManager.i().mPipelineChannel);
		
		//and reload
		switch (DataManager.i().mPipelineChannel.getType()) {
			case COVERAGE:
				EtcAdmin.i().setScreen(ScreenType.MAPPERCOVERAGE, true);
				break;
			case DEDUCTION:
				EtcAdmin.i().setScreen(ScreenType.MAPPERDEDUCTION, true);
				break;
			case EMPLOYEE:
				EtcAdmin.i().setScreen(ScreenType.MAPPEREMPLOYEE, true);
				break;
			case INSURANCE:
				EtcAdmin.i().setScreen(ScreenType.MAPPERINSURANCE, true);
				break;
			case IRS1095C:
				EtcAdmin.i().setScreen(ScreenType.MAPPERIRS1095C, true);
				break;
			case PAY:
				EtcAdmin.i().setScreen(ScreenType.MAPPERPAY, true);
				break;
			default:
				EtcAdmin.i().setScreen(ScreenType.PIPELINEDYNAMICFILESPECIFICATION, true);
				break;
		}
		
		Stage stage = (Stage) pspcConverterCombo.getScene().getWindow();
		stage.close();
	}		
	
	@FXML
	private void onViewSpecification(ActionEvent event) {
		DataManager.i().loadDynamicFileSpecification(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
	}	

	@FXML
	private void onViewCalculator(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINECOVERAGEFILEEDIT);
	}	

	public class CalcCell extends HBox 
	{
		Label id = new Label();
		Label name = new Label();
		PayConversionCalculation calc = null;

		public PayConversionCalculation getCalc() {
			return calc;
		}

		@Override
		public String toString() 
		{
			return calc.getId().toString() + " - " + calc.getName();
		}

		CalcCell(PayConversionCalculation calc) 
		{
			super();

			if (calc != null) 
			{
				this.calc = calc;

				name.setText(calc.getName());
				id.setText(String.valueOf(calc.getId()));

				// set the label widths
				id.setMinWidth(50);
				id.setMaxWidth(50);
				id.setPrefWidth(50);
				name.setMinWidth(150);
				name.setMaxWidth(150);
				name.setPrefWidth(150);

				this.getChildren().addAll(id, name);
			}
		}
	}
}


