package com.etc.admin.ui.coverage;

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
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.Benefit;
import com.etc.corvetto.entities.Coverage;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.rqs.BenefitRequest;
import com.etc.corvetto.rqs.CoverageRequest;
import com.etc.entities.CoreData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewCoverageController {
	@FXML
	private DatePicker covStartDate;
	@FXML
	Label covTopLabel;
	@FXML
	private DatePicker covEndDate;
	@FXML
	private Label lblWaived;
	@FXML
	private CheckBox covWaivedCBox;
	@FXML
	private Label lblIneligible;
	@FXML
	private CheckBox covIneligibleCBox;
	@FXML
	private CheckBox covCobraCBox;
	@FXML
	private CheckBox covLaborUnionCBox;
	@FXML
	private Label lblMemberShare;
	@FXML
	private TextField covMemberShare;
	@FXML
	private TextField covBenefit;
	@FXML
	private Button covCancelButton;
	@FXML
	private Button covSaveButton;
	@FXML
	private Button covRemoveButton;
	@FXML
	private TextField covCustField1;
	@FXML
	private TextField covCustField2;
	@FXML
	private ComboBox<String> covPlan;

	public boolean addMode = false;
	public boolean changesMade = false;
	private Benefit selectedPlan;
	private Account account = null;
	private Coverage coverage = null;
	
	private boolean secondaryMode = false;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		//if (DataManager.i().mCoverage == null)
		//	setAddMode(true);
		//else {
		//	setAddMode(false);
			showCoverage();
		//}
	}
	
	public void load() {

	}
	
	public void setSecondaryMode() {
		lblWaived.setVisible(false);
		covWaivedCBox.setVisible(false);
		lblIneligible.setVisible(false);
		covIneligibleCBox.setVisible(false);
		lblMemberShare.setVisible(false);
		covMemberShare.setVisible(false);
		secondaryMode = true;
	}
	
	private void showCoverage() 
	{
		coverage = DataManager.i().mCoverage;

		// load the plan combo
		account = DataManager.i().mAccount;
		 try {
			  BenefitRequest bReq = new BenefitRequest();
			  bReq.setAccountId(account.getId());
			  account.setBenefits(AdminPersistenceManager.getInstance().getAll(bReq));
		 } catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		 }
		
		int selectionIndex = -1;
		int benefitIndex = 0;
		selectedPlan = coverage.getBenefit();
		for (Benefit benefit : account.getBenefits() ) {
			covPlan.getItems().add(benefit.getProviderReference());
			if (coverage.getBenefit().getProviderReference().equals(benefit.getProviderReference()))
					selectionIndex = benefitIndex;
		}
		
		// select the plan if found
		if (selectionIndex != -1)
			covPlan.getSelectionModel().select(selectedPlan.getProviderReference());//  clearAndSelect(selectionIndex);
		
		// change the label if it's inactive
		covTopLabel.setText("Coverage");
		if (coverage.isActive() == false)
			covTopLabel.setText("Coverage (inactive)");
		if (coverage.isDeleted() == true)
			covTopLabel.setText("Coverage (deleted)");

		Utils.updateControl(covStartDate, coverage.getStartDate());
		Utils.updateControl(covEndDate, coverage.getEndDate());
		Utils.updateControl(covWaivedCBox, coverage.isWaived());
		Utils.updateControl(covIneligibleCBox, coverage.isIneligible());
		Utils.updateControl(covCobraCBox, coverage.isCobra());
		Utils.updateControl(covLaborUnionCBox, coverage.isLaborUnion());
		Utils.updateControl(covCustField1, coverage.getCustomField1());
		Utils.updateControl(covCustField2, coverage.getCustomField2());
		if (coverage.getMemberShare() != null)
			Utils.updateControl(covMemberShare, coverage.getMemberShare());
		if (coverage.getBenefit() != null)
			Utils.updateControl(covBenefit, coverage.getBenefit().getId());
	}
	
	private void updateCoverage()
	{
		DataManager.i().mCoverage.setStartDate(Utils.getCalDate(covStartDate));
		DataManager.i().mCoverage.setEndDate(Utils.getCalDate(covEndDate));
		DataManager.i().mCoverage.setWaived(covWaivedCBox.isSelected());
		DataManager.i().mCoverage.setIneligible(covIneligibleCBox.isSelected());
		DataManager.i().mCoverage.setCobra(covCobraCBox.isSelected());
		DataManager.i().mCoverage.setCustomField1(covCustField1.getText());
		DataManager.i().mCoverage.setCustomField2(covCustField2.getText());
		DataManager.i().mCoverage.setLaborUnion(covLaborUnionCBox.isSelected());
		if (selectedPlan != null) {
			DataManager.i().mCoverage.setBenefit(selectedPlan);
			DataManager.i().mCoverage.setBenefitId(selectedPlan.getId());
		}

		if (covMemberShare.getText() != null && covMemberShare.getText().isEmpty() == false )
			DataManager.i().mCoverage.setMemberShare(Float.valueOf(covMemberShare.getText()));
		else
			DataManager.i().mCoverage.setMemberShare(null);
	}
	
	
	
/*	private void updateCoverageAddRequestData(AddCoverageRequest request) {
		request.setDecisionDate(Utils.getCalDate(covDecisionDate));
		request.setStartDate(Utils.getCalDate(covStartDate));
		request.setEndDate(Utils.getCalDate(covEndDate));
		request.setWaived(covWaivedCBox.isSelected());
		request.setIneligible(covIneligibleCBox.isSelected());
		request.setCobra(covCobraCBox.isSelected());
		request.setLaborUnion(covLaborUnionCBox.isSelected());
		request.setMemberShare(Float.valueOf(covMemberShare.getText()));
		if (DataManager.i().mCoverage != null && DataManager.i().mCoverage.getBenefit() != null)
			request.setBenefitId(DataManager.i().mCoverage.getBenefit().getId());
	}
	
	private void setAddMode(boolean mode) {
		addMode = mode;
		if (mode == true) {
			covSaveButton.setText("Add");
			covRemoveButton.setVisible(false);
		}
		else {
			covSaveButton.setText("Save");
			covRemoveButton.setVisible(true);
		}
	}
*/	
	@FXML
	private void onSave(ActionEvent event) 
	{
		//validate we have minimum data
		//if (Utils.validate(covNameField) == false)
		//	return;
		
		if (addMode == false) {
			//update the object from the screen
			updateCoverage();
			// create the request
			CoverageRequest request = new CoverageRequest();
			request.setId(DataManager.i().mCoverage.getId());
			request.setEntity(DataManager.i().mCoverage);
			if (DataManager.i().mCoverage.getEndDate() == null)
				request.setClearEndDate(true);
			if (DataManager.i().mCoverage.getMemberShare() == null)
				request.setClearMemberShare(true);
			//send it to the server
			try {
				Coverage cov = AdminPersistenceManager.getInstance().addOrUpdate(request);
				Coverage cov2 = request.getEntity();
				changesMade=true;
			} catch (CoreException e) {
	        	DataManager.i().log(Level.SEVERE, e); 
			}
		    catch (Exception e) {  DataManager.i().logGenericException(e); }
		} else {
//			// create the update request
//			AddCoverageRequest request = new AddCoverageRequest();
//			// gather the data
//			updateCoverageAddRequestData(request);
//			//send it to the server
//			DataManager.i().addCoverage(request);
		}
		
		// and exit the pop up
		changesMade = true;
		exitPopup();
	}	
	
	@FXML
	private void onPlanSelection(ActionEvent event) {
		if (account == null) return;
		selectedPlan = account.getBenefits().get(covPlan.getSelectionModel().getSelectedIndex());
		Utils.updateControl(covBenefit, selectedPlan.getId());
	}	
	
	@FXML
	private void onCancel(ActionEvent event) {
		exitPopup();
	}	
	
	private void exitPopup() {
		Stage stage = (Stage) covStartDate.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onShowSystemInfo() {
		try {
			// set the coredata
			DataManager.i().mCurrentCoreDataType = SystemDataType.COVERAGE;
			DataManager.i().mCoreData = (CoreData) DataManager.i().mCoverage;
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
	        	showCoverage();
	        }
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
	}
		
}


