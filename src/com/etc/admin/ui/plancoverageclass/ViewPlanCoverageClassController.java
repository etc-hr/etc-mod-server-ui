package com.etc.admin.ui.plancoverageclass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.calc.CalcQueueData;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.CoverageGroup;
import com.etc.corvetto.entities.TaxYear;
import com.etc.corvetto.rqs.TaxYearRequest;
import com.etc.entities.CoreData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewPlanCoverageClassController {
	
	@FXML
	private TextField cvclsIdField;
	@FXML
	private TextField cvclsNameField;
	@FXML
	private TextField cvclsDescriptionField;
	@FXML
	private CheckBox cvclsICHRACheck;
	@FXML
	private CheckBox cvclsLaborUnionCheck;
	@FXML
	private CheckBox cvclsAffordableCheck;
	@FXML
	private Button cvclsSaveButton;
	@FXML
	private Button cvclsRemoveButton;
	@FXML
	private Button cvclsCallCodesButton;
	@FXML
	private ComboBox<String> cvclsPayCodeTypeCombo;
	@FXML
	private ComboBox<String> cvclsTaxYearCombo;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	private boolean AddMode = false;
	public boolean changesMade = false;

	@FXML
	public void initialize() 
	{
		initControls();
		
		//load	
		if (AddMode == false)
			updateCoverageClassData();
	}
	
	public void setAddMode(){
		AddMode = true;
		cvclsRemoveButton.setVisible(false);
		cvclsSaveButton.setText("Add");
	}
	
	private void initControls() {
		
		// set calc button image
		InputStream input1 = getClass().getClassLoader().getResourceAsStream("img/ERCalc1.png");
   	 	Image image1 = new Image(input1, 75f, 75f, true, true);
   	 	ImageView imageView1 = new ImageView(image1);
   	 	cvclsCallCodesButton.setGraphic(imageView1);
		
		// load the tax year selections
		// tax year combo
   	 cvclsTaxYearCombo.getItems().clear();
		if (DataManager.i().mEmployer.getTaxYears() == null) {
    		try {
            	TaxYearRequest tyReq = new TaxYearRequest(); 
        		tyReq.setEmployerId(DataManager.i().mEmployer.getId());
				DataManager.i().mEmployer.setTaxYears(AdminPersistenceManager.getInstance().getAll(tyReq));
			} catch (CoreException e) {
	        	DataManager.i().log(Level.SEVERE, e); 
			}
    	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		}
		
		String calcSelection = "";
		for (TaxYear taxYear : DataManager.i().mEmployer.getTaxYears()) {
			cvclsTaxYearCombo.getItems().add(String.valueOf(taxYear.getYear()));
			// if it matches the current year, default to it
			if (taxYear.getYear() == Calendar.getInstance().get(Calendar.YEAR))
				calcSelection = String.valueOf(taxYear.getYear());
		}
		// sort
		Collections.sort(cvclsTaxYearCombo.getItems());

		// select default
		if (calcSelection != "")
			cvclsTaxYearCombo.getSelectionModel().select(calcSelection);
		else
			if (DataManager.i().mEmployer.getTaxYear() != null)
				cvclsTaxYearCombo.getSelectionModel().select(String.valueOf(DataManager.i().mEmployer.getTaxYear().getYear()));
   	 	
   	 	
		// need to add manually instead of using the enum
		cvclsPayCodeTypeCombo.getItems().clear();
		cvclsPayCodeTypeCombo.getItems().add("FTH");
		cvclsPayCodeTypeCombo.getItems().add("VH");
	}
	
	public void updateCoverageClassData()
	{
		CoverageGroup cvgClass = DataManager.i().mCoverageClass;
		if (cvgClass != null)
		{
			if (cvgClass.isActive() == false) 
			{
				cvclsRemoveButton.setVisible(false);
				cvclsSaveButton.setText("Set Active");
			}

			Utils.updateControl(cvclsIdField,cvgClass.getId());
			Utils.updateControl(cvclsNameField,cvgClass.getName());
			Utils.updateControl(cvclsDescriptionField,cvgClass.getDescription());
			Utils.updateControl(cvclsICHRACheck,cvgClass.isIchra());
			Utils.updateControl(cvclsLaborUnionCheck,cvgClass.isLaborUnion());
			Utils.updateControl(cvclsAffordableCheck,cvgClass.isAffordable());
			if (cvgClass.getPayCode() != null)
				cvclsPayCodeTypeCombo.getSelectionModel().select(cvgClass.getPayCode().toString());
		}		
	}
	
/*	public void updateCoverageClassRequest(UpdateCoverageClassRequest request) {
		CoverageClass cvgClass = DataManager.i().mCoverageClass;
		if (cvgClass != null){
			DataManager.i().mEmployer.setAccount(DataManager.i().mAccount);
			cvgClass.setEmployer(DataManager.i().mEmployer);
			request.setCoverageClass(cvgClass);
			request.setName(cvclsNameField.getText());
			request.setDescription(cvclsDescriptionField.getText());
			request.setLaborUnion(cvclsLaborUnionCheck.isSelected());
			request.setAffordable(cvclsAffordableCheck.isSelected());
			if (cvclsPayCodeTypeCombo.getValue() != null && cvclsPayCodeTypeCombo.getValue().isEmpty() == false)
				request.setPayCodeType(PayCodeType.valueOf(cvclsPayCodeTypeCombo.getValue()));
		}		
	}
*/	
/*	public void updateCoverageClassRequest(AddCoverageClassRequest request) {
		request.setEmployerId(DataManager.i().mEmployer.getId());
		request.setName(cvclsNameField.getText());
		request.setDescription(cvclsDescriptionField.getText());
		request.setLaborUnion(cvclsLaborUnionCheck.isSelected());
		request.setAffordable(cvclsAffordableCheck.isSelected());
		if (cvclsPayCodeTypeCombo.getValue() != null && cvclsPayCodeTypeCombo.getValue().isEmpty() == false)
			request.setPayCodeType(PayCodeType.valueOf(cvclsPayCodeTypeCombo.getValue()));
	}
*/	

	@FXML
	private void onShowSystemInfo()
	{
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mCoverageClass;
			DataManager.i().mCurrentCoreDataType = SystemDataType.COVERAGECLASS;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (IOException e) { DataManager.i().log(Level.SEVERE, e); }        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}

	private void exitPopup() 
	{
		Stage stage = (Stage) cvclsNameField.getScene().getWindow();
		stage.close();		
	}
	
	@FXML 
	private void onCalcIrs10945BC(ActionEvent event) 
	{ 
		if (cvclsTaxYearCombo.getSelectionModel().getSelectedItem() == null) {
		    Utils.showAlert("No Tax year Selected", "Please select a tax year first.");
			return;
		}
		
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Call Codes");
	    String confirmText = "Are You Sure You Want To Call Codes for this Coverage Class";
	    confirmText +=" for tax year ";
	    confirmText += cvclsTaxYearCombo.getSelectionModel().getSelectedItem();
	    confirmText += "?";
	    confirmAlert.setContentText(confirmText);
	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();
	    if ((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	return;
	    }

	    if(DataManager.i().mEmployer == null) return;
		
		try {
			CalcQueueData calcData = new CalcQueueData(); 
			//iterate through all the account's employers
			TaxYear taxYear = null;
			// get the employer tax years
        	TaxYearRequest tyReq = new TaxYearRequest(); 
    		tyReq.setEmployerId(DataManager.i().mEmployer.getId());
    		DataManager.i().mEmployer.setTaxYears(AdminPersistenceManager.getInstance().getAll(tyReq));

			// select the tax year
			if (cvclsTaxYearCombo.getSelectionModel().getSelectedItem() != null) {
				for (TaxYear ty : DataManager.i().mEmployer.getTaxYears()) {
					if (ty.getYear() == Integer.valueOf(cvclsTaxYearCombo.getSelectionModel().getSelectedItem())){
						taxYear = ty;
						break;
					}
				}
			}
			
			// no tax year match, bail out
			if (taxYear == null) {
				Utils.showAlert("Tax Year Error", "Tax Year Mismatch, please double check selection.");
				return;
			}
			
			// and create using the tax year
			calcData.createIrs1094CCalculation(DataManager.i().mLocalUser, taxYear, DataManager.i().mEmployer, DataManager.i().mEmployee, false, DataManager.i().mCoverageClass);
		} catch (Exception e) {
			DataManager.i().log(Level.SEVERE, e);
			Utils.showAlert("Error", "There was a problem with the calling codes request. Please check the logs for more info.");
		}

	} 
	
	@FXML
	private void onSave(ActionEvent event) 
	{
/*		if (AddMode == true) {
			// create the request
			AddCoverageClassRequest addRequest = new AddCoverageClassRequest();
			// update the request
			updateCoverageClassRequest(addRequest);
			// update the server
			DataManager.i().mCoverageClass = DataManager.i().addCoverageClass(addRequest);
		}else {
			// create the request
			UpdateCoverageClassRequest updateRequest = new UpdateCoverageClassRequest();
			// update the request
			updateCoverageClassRequest(updateRequest);
			// update the server
			DataManager.i().mCoverageClass = DataManager.i().updateCoverageClass(updateRequest);
		}
		
		changesMade = true;
		exitPopup();
*/	}	
	
	@FXML
	private void onRemove(ActionEvent event) 
	{
/*		RemoveCoverageClassRequest request = new RemoveCoverageClassRequest();
		request.setCoverageClass(DataManager.i().mCoverageClass);
		request.setCoverageClassId(DataManager.i().mCoverageClass.getId());

		DataManager.i().removeCoverageClass(request);
		changesMade = true;
		exitPopup();
*/	}	
	
	@FXML
	private void onCancel(ActionEvent event) {
		exitPopup();
	}	
}
