package com.etc.admin.ui.benefit;

import java.io.IOException;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.coresysteminfo.ViewCoreSystemInfoController;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.Benefit;
import com.etc.corvetto.rqs.BenefitRequest;
import com.etc.corvetto.utils.types.CoverageTierType;
import com.etc.corvetto.utils.types.CoverageType;
import com.etc.corvetto.utils.types.InsuranceType;
import com.etc.corvetto.utils.types.OfferCode;
import com.etc.entities.CoreData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewBenefitController 
{
	@FXML
	private Label topLabel;
	@FXML
	private TextField name;
	@FXML
	private TextField description;
	@FXML
	private TextField reference;
	@FXML
	private ComboBox<InsuranceType> insTypeCombo;
	@FXML
	private ComboBox<CoverageType> covTypeCombo;
	@FXML
	private ComboBox<CoverageTierType> tierTypeCombo;
	@FXML
	private ComboBox<OfferCode> offerCodeCombo;
	@FXML
	private CheckBox waived;
	@FXML
	private CheckBox ineligible;
	@FXML
	private CheckBox cobra;
	@FXML
	private CheckBox laborUnion;
	@FXML
	private CheckBox maCreditable;
	@FXML
	private CheckBox iCHRA;
	@FXML
	private Button benefitSaveButton;
	@FXML
	private Button benefitCancelButton;
	@FXML
	private ComboBox<String> cvclsPayCodeTypeCombo;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
//	private boolean AddMode = false;
	public boolean changesMade = false;
	
	@FXML
	public void initialize()
	{
		initControls();
		showBenefit();
	}
	
//	public void setAddMode()
//	{
//		AddMode = true;
//		benefitSaveButton.setText("Add");
//	}
	
	private void initControls()
	{
		//Insurance type
		ObservableList<InsuranceType> insuranceTypes = FXCollections.observableArrayList(InsuranceType.values());
		insTypeCombo.setItems(insuranceTypes);
		
		//Coverage type
		ObservableList<CoverageType> coverageTypes = FXCollections.observableArrayList(CoverageType.values());
		covTypeCombo.setItems(coverageTypes);
		//Coverage Tier type
		ObservableList<CoverageTierType> tierTypes = FXCollections.observableArrayList(CoverageTierType.values());
		tierTypeCombo.setItems(tierTypes);
		
		//Offer Code
		ObservableList<OfferCode> offerCodes = FXCollections.observableArrayList(OfferCode.values());
		offerCodeCombo.setItems(offerCodes);
	}
	
	public void showBenefit() 
	{
		Benefit benefit = DataManager.i().mBenefit;
		
		if(benefit != null)
		{
			topLabel.setText("Plan Detail");
			if (benefit.isActive() == false)
				topLabel.setText("Plan Detail (inactive)");
			if (benefit.isDeleted() == true)
				topLabel.setText("Plan Detail (deleted)");

			Utils.updateControl(name, benefit.getName());
			Utils.updateControl(description, benefit.getDescription());
			Utils.updateControl(reference, benefit.getProviderReference());
			Utils.updateControl(waived, benefit.isWaived());
			Utils.updateControl(ineligible, benefit.isIneligible());
			Utils.updateControl(cobra, benefit.isCobra());
			Utils.updateControl(laborUnion, benefit.isLaborUnion());
			Utils.updateControl(maCreditable, benefit.isMaCreditable());
			Utils.updateControl(iCHRA, benefit.isIchra());
			if(benefit.getInsuranceType() != null) 
				insTypeCombo.getSelectionModel().select(benefit.getInsuranceType());
			if(benefit.getCoverageType() != null) 
				covTypeCombo.getSelectionModel().select(benefit.getCoverageType());
			if(benefit.getTierType() != null)
				tierTypeCombo.getSelectionModel().select(benefit.getTierType());
			if(benefit.getOfferCode() != null)
				offerCodeCombo.getSelectionModel().select(benefit.getOfferCode());
		}
	}

	public void addBenefit()
	{
		// create the request
		BenefitRequest request = new BenefitRequest();
		Benefit benefit = new Benefit();

		benefit.setName(name.getText());
		benefit.setInsuranceType(insTypeCombo.getValue());
		benefit.setCoverageType(covTypeCombo.getValue());
		benefit.setWaived(waived.isSelected());
		benefit.setIneligible(ineligible.isSelected());
		benefit.setCobra(cobra.isSelected());
		benefit.setLaborUnion(laborUnion.isSelected());
		benefit.setMaCreditable(maCreditable.isSelected());

		if(description.getText() != null) { benefit.setDescription(description.getText()); }
		if(reference.getText() != null) { benefit.setProviderReference(reference.getText()); }
		if(tierTypeCombo.getValue() != null) { benefit.setTierType(tierTypeCombo.getValue()); }
		if(offerCodeCombo.getValue() != null) { benefit.setOfferCode(offerCodeCombo.getValue()); }

		benefit.setAccountId(DataManager.i().mAccount.getId());

		request.setEntity(benefit);
		request.setId(benefit.getId());

		// update the server
		try {
			benefit = AdminPersistenceManager.getInstance().addOrUpdate(request);
		} catch (CoreException e) { e.printStackTrace(); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}

	public void updateBenefit()
	{
		// create the request
		BenefitRequest request = new BenefitRequest();
		Benefit benefit = DataManager.i().mBenefit;

		benefit.setName(name.getText());
		benefit.setInsuranceType(insTypeCombo.getValue());
		benefit.setCoverageType(covTypeCombo.getValue());
		benefit.setWaived(waived.isSelected());
		benefit.setIneligible(ineligible.isSelected());
		benefit.setCobra(cobra.isSelected());
		benefit.setLaborUnion(laborUnion.isSelected());
		benefit.setMaCreditable(maCreditable.isSelected());
		benefit.setIchra(iCHRA.isSelected());

		if(description.getText() != null) { benefit.setDescription(description.getText()); }
		if(reference.getText() != null) { benefit.setProviderReference(reference.getText()); }
		if(tierTypeCombo.getValue() != null) { benefit.setTierType(tierTypeCombo.getValue()); }
		if(offerCodeCombo.getValue() != null) { benefit.setOfferCode(offerCodeCombo.getValue()); }

		benefit.setAccountId(DataManager.i().mAccount.getId());

		request.setEntity(benefit);
		request.setId(benefit.getId());

		// update the server
		try {
			benefit = AdminPersistenceManager.getInstance().addOrUpdate(request);
		} catch (CoreException e) { e.printStackTrace(); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}

	private void clearFields()
	{
		name.setText("");
		description.setText("");
		reference.setText("");
		insTypeCombo.setValue(null);
		covTypeCombo.setValue(null);
		tierTypeCombo.setValue(null);
		offerCodeCombo.setValue(null);
		waived.setSelected(false);
		ineligible.setSelected(false);
		cobra.setSelected(false);
		laborUnion.setSelected(false);
		maCreditable.setSelected(false);
	}
	
	private void exitPopup() {
		Stage stage = (Stage) name.getScene().getWindow();
		stage.close();		
	}

	@FXML
	private void onSave(ActionEvent event) 
	{
		if(DataManager.i().mBenefit != null)
		{
			// update existing benefit
			updateBenefit();
		} else {
//			// add new benefit
			addBenefit();
		}

		changesMade = true;
		exitPopup();
	}	

	@FXML
	private void onTierTypeSelect(ActionEvent event) {
//		exitPopup();
	}	
	@FXML
	private void onOfferCodeSelect(ActionEvent event) {
//		exitPopup();
	}	
	@FXML
	private void onInsTypeSelect(ActionEvent event) {
//		exitPopup();
	}	
	@FXML
	private void onCovTypeSelect(ActionEvent event) {
//		exitPopup();
	}	

	@FXML
	private void onShowSystemInfo() {
		try {
			// set the coredata
			DataManager.i().mCurrentCoreDataType = SystemDataType.BENEFIT;
			DataManager.i().mCoreData = (CoreData) DataManager.i().mBenefit;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
			Parent ControllerNode = loader.load();
	        ViewCoreSystemInfoController sysInfoController = (ViewCoreSystemInfoController) loader.getController();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if (sysInfoController.changesMade == true) {
    			changesMade = true;
	        	showBenefit();
	        }
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
	}
	@FXML
	private void onCancel(ActionEvent event) {
		clearFields();
		exitPopup();
	}	
}

