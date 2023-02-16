package com.etc.admin.ui.taxyear;

import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.TaxYear;
import com.etc.corvetto.entities.TaxYearServiceLevel;
import com.etc.corvetto.entities.User;
import com.etc.corvetto.rqs.TaxYearRequest;
import com.etc.corvetto.utils.types.AcaFormType;
import com.etc.corvetto.utils.types.AcaPrintType;
import com.etc.corvetto.utils.types.SafeHarborCode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewTaxYearAddController {
	
	@FXML
	private TextField txyrYearField;
	@FXML
	private CheckBox txyrClosedCheckBox;
	@FXML
	private CheckBox txyrApprovedCheckBox;
	@FXML
	private CheckBox txyrMBrShareOVerrideCheckBox;
	@FXML
	private CheckBox txyrFileCACheckBox;
	@FXML
	private CheckBox txyrFileMACheckBox;
	@FXML
	private CheckBox txyrFileNJCheckBox;
	@FXML
	private CheckBox txyrFileDCCheckBox;
	@FXML
	private CheckBox txyrFileRICheckBox;
	@FXML
	private CheckBox txyrFileVTCheckBox;
	@FXML
	private DatePicker txyrApprovedOnPicker;
	@FXML
	private DatePicker txyrClosedOnPicker;
	@FXML
	private ComboBox<String> txyrServiceLevelCombo;
	@FXML
	private ComboBox<String> txyrApprovedByCombo;
	@FXML
	private ComboBox<String> txyrClosedByCombo;
	@FXML
	private ComboBox<String> txyrLiaisonCombo;
	@FXML
	private ComboBox<String> txyrJrLiaisonCombo;
	@FXML
	private ComboBox<AcaPrintType> txyrPrintTypeCombo;
	@FXML
	private ComboBox<AcaFormType> txyrFormTypeCombo;
	@FXML
	private ComboBox<SafeHarborCode> txyrSafeHarborCodeCombo;
	
	public boolean changesMade = false;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
	}
	
	private void initControls() 
	{
		// user combos
		txyrApprovedByCombo.getItems().clear();
		txyrClosedByCombo.getItems().clear();
		txyrLiaisonCombo.getItems().clear();
		txyrJrLiaisonCombo.getItems().clear();
		if(DataManager.i().mUsers != null) 
			for (User user : DataManager.i().mUsers) 
			{
				txyrApprovedByCombo.getItems().add(user.getLastName() + ", " + user.getFirstName());
				txyrClosedByCombo.getItems().add(user.getLastName() + ", " + user.getFirstName());
				txyrLiaisonCombo.getItems().add(user.getLastName() + ", " + user.getFirstName());
				txyrJrLiaisonCombo.getItems().add(user.getLastName() + ", " + user.getFirstName());
			}

		// Service Level Type
		if(DataManager.i().mTaxYearServiceLevels != null) 
			for (TaxYearServiceLevel serviceLevel : DataManager.i().mTaxYearServiceLevels) {
				txyrServiceLevelCombo.getItems().add(serviceLevel.getName());
			}
		
		// Form Type
		ObservableList<AcaFormType> formTypes = FXCollections.observableArrayList(AcaFormType.values());
		txyrFormTypeCombo.setItems(formTypes);
		
		// Print Type
		ObservableList<AcaPrintType> printTypes = FXCollections.observableArrayList(AcaPrintType.values());
		txyrPrintTypeCombo.setItems(printTypes);
		
		//Safe Harbor code
		ObservableList<SafeHarborCode> safeHarborCode = FXCollections.observableArrayList(SafeHarborCode.values());
		txyrSafeHarborCodeCombo.setItems(safeHarborCode);
	}
	
	private void addTaxYear()
	{
		TaxYear taxYear = new TaxYear();
		
		taxYear.setYear(Integer.valueOf(txyrYearField.getText()));
		// approved
		taxYear.setFilingApproved(txyrApprovedCheckBox.isSelected());
		if(txyrApprovedOnPicker.getValue() != null)
			taxYear.setFilingApprovedOn(Utils.getCalDate(txyrApprovedOnPicker.getValue()));
		if(txyrApprovedByCombo.getSelectionModel().getSelectedIndex() > -1)
			taxYear.setFilingApprovedById(DataManager.i().mUsers.get(txyrApprovedByCombo.getSelectionModel().getSelectedIndex()).getId());
		// closed
		taxYear.setClosed(txyrClosedCheckBox.isSelected());
		if(txyrClosedOnPicker.getValue() != null)
			taxYear.setClosedOn(Utils.getCalDate(txyrClosedOnPicker.getValue()));
		if(txyrClosedByCombo.getSelectionModel().getSelectedIndex() > -1)
			taxYear.setClosedById(DataManager.i().mUsers.get(txyrClosedByCombo.getSelectionModel().getSelectedIndex()).getId());
		// liaisons
		if(txyrLiaisonCombo.getSelectionModel().getSelectedIndex() > -1)
			taxYear.setLiaisonId(DataManager.i().mUsers.get(txyrLiaisonCombo.getSelectionModel().getSelectedIndex()).getId());
		if(txyrJrLiaisonCombo.getSelectionModel().getSelectedIndex() > -1)
			taxYear.setJrLiaisonId(DataManager.i().mUsers.get(txyrJrLiaisonCombo.getSelectionModel().getSelectedIndex()).getId());
		// form type
		if(txyrFormTypeCombo.getValue() != null)
			taxYear.setFormType(txyrFormTypeCombo.getValue());
		// print type
		if(txyrPrintTypeCombo.getValue() != null)
			taxYear.setPrintType(txyrPrintTypeCombo.getValue());
		// safe harbor code
		if(txyrSafeHarborCodeCombo.getValue() != null)
			taxYear.setShDefault(txyrSafeHarborCodeCombo.getValue());
		// other check boxes
		taxYear.setMemberShareOverride(txyrMBrShareOVerrideCheckBox.isSelected());
		taxYear.setFileCA(txyrFileCACheckBox.isSelected());
		taxYear.setFileMA(txyrFileMACheckBox.isSelected());
		taxYear.setFileNJ(txyrFileNJCheckBox.isSelected());
		taxYear.setFileDC(txyrFileDCCheckBox.isSelected());
		taxYear.setFileRI(txyrFileRICheckBox.isSelected());
		taxYear.setFileVT(txyrFileVTCheckBox.isSelected());
		taxYear.setEmployerId(DataManager.i().mEmployer.getId());
		
		//send to the server
		TaxYearRequest request = new TaxYearRequest();
		request.setEntity(taxYear);
		try {
			taxYear =  AdminPersistenceManager.getInstance().addOrUpdate(request);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		//Assign to global variable
		DataManager.i().mTaxYear = taxYear;
	}
	
	private boolean validateData() 
	{
		boolean bReturn = true;
		
		//check for required data
		if(!Utils.validate(txyrYearField)) bReturn = false;

		// int range check
		if(!Utils.validateIntRangeTextField(txyrYearField, 1980, 2050)) bReturn = false;
		
		return bReturn;
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		exitPopup();
	}	
	
	@FXML
	private void onAdd(ActionEvent event) {
		//validate everything
		if(!validateData())
			return;
		
		//add our object
		addTaxYear();
		
		//and exit the popup
		changesMade = true;
		exitPopup();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) txyrPrintTypeCombo.getScene().getWindow();
		stage.close();
	}

			
}
