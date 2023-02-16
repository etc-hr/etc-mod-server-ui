package com.etc.admin.ui.coresysteminfo;

import java.io.IOException;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.ems.pipeline.rqs.PipelineChannelRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineSpecificationRequest;
import com.etc.corvetto.rqs.AccountRequest;
import com.etc.corvetto.rqs.BenefitRequest;
import com.etc.corvetto.rqs.ContactRequest;
import com.etc.corvetto.rqs.CoverageGroupMembershipRequest;
import com.etc.corvetto.rqs.CoverageGroupRequest;
import com.etc.corvetto.rqs.CoverageRequest;
import com.etc.corvetto.rqs.DepartmentRequest;
import com.etc.corvetto.rqs.DependentRequest;
import com.etc.corvetto.rqs.EmployeeRequest;
import com.etc.corvetto.rqs.EmployerRequest;
import com.etc.corvetto.rqs.EmploymentPeriodRequest;
import com.etc.corvetto.rqs.Irs1095bRequest;
import com.etc.corvetto.rqs.Irs1095cRequest;
import com.etc.corvetto.rqs.LocationRequest;
import com.etc.corvetto.rqs.NoteRequest;
import com.etc.corvetto.rqs.PayPeriodRequest;
import com.etc.corvetto.rqs.PayRequest;
import com.etc.corvetto.rqs.PlanYearRequest;
import com.etc.corvetto.rqs.TaxYearRequest;
import com.etc.corvetto.rqs.UploadTypeRequest;
import com.etc.corvetto.rqs.UserRequest;
import com.etc.rqs.DataPropertyRequest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewCoreSystemInfoController 
{
	@FXML
	private Label topLabel;
	@FXML
	private Label spareLabel1;
	@FXML
	private Label spareLabel2;
	@FXML
	private TextField spareField1;
	@FXML
	private TextField spareField2;
	@FXML
	private TextField idField;
	@FXML
	private TextField versionField;
	@FXML
	private Button batchIdButton;
	@FXML
	private TextField bornOnField;
	@FXML
	private TextField asOfField;
	@FXML
	private TextField lastUpdatedField;
	@FXML
	private CheckBox activeCheck;
	@FXML
	private CheckBox deletedCheck;
	@FXML
	private Button editButton;
	@FXML
	private Rectangle editRectangle; 
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	public boolean changesMade = false;
	
	@FXML
	public void initialize() 
	{
		setTopLabel();
		if(DataManager.i().mCoreData != null)
			showCoreData();
	}
	
	private void showCoreData() 
	{
		spareLabel1.setVisible(false);
		spareField1.setVisible(false);
		spareLabel2.setVisible(false);
		spareField2.setVisible(false);
		
		Utils.updateControl(idField, DataManager.i().mCoreData.getId());
		if(DataManager.i().mCoreData.getVersion() != null)
			Utils.updateControl(versionField, DataManager.i().mCoreData.getVersion());
		batchIdButton.setText(DataManager.i().mCoreData.getBatchId());
		Utils.updateControl(bornOnField, DataManager.i().mCoreData.getBornOn());
		Utils.updateControl(asOfField, DataManager.i().mCoreData.getAsOf());
		Utils.updateControl(lastUpdatedField, DataManager.i().mCoreData.getLastUpdated());
		Utils.updateControl(activeCheck, DataManager.i().mCoreData.isActive());
		Utils.updateControl(deletedCheck, DataManager.i().mCoreData.isDeleted());
		
		// type specfic spare data
		if (DataManager.i().mCurrentCoreDataType == SystemDataType.EMPLOYEE && DataManager.i().mEmployee != null && DataManager.i().mEmployee.getPerson() != null) {
			spareLabel1.setVisible(true);
			spareField1.setVisible(true);
			spareLabel2.setVisible(true);
			spareField2.setVisible(true);
			
			spareLabel1.setText("Person Id");
			Utils.updateControl(spareField1, DataManager.i().mEmployee.getPersonId());
			spareLabel2.setText("Address Id");
			Utils.updateControl(spareField2, DataManager.i().mEmployee.getPerson().getMailAddressId());		
		}
	}
	
	private void setTopLabel() {
		switch(DataManager.i().mCurrentCoreDataType) 
		{
			case ACCOUNT:
				topLabel.setText("Account Core System Data");
				break;
			case BENEFIT:
				topLabel.setText("Plan Core System Data");
				break;
			case CHANNEL:
				topLabel.setText("Channel Core System Data");
				break;
			case CONTACT:
				topLabel.setText("Contact Core System Data");
				break;
			case COVERAGE:
				topLabel.setText("Coverage Core System Data");
				break;
			case COVERAGECLASS:
				topLabel.setText("Coverage Class Core System Data");
				break;
			case COVERAGEGROUPMEMBERSHIP:
				topLabel.setText("Coverage Class Membership Core System Data");
				break;
			case DATAPROPERTY:
				topLabel.setText("Data Property Core System Data");
				break;
			case DEPARTMENT:
				topLabel.setText("Department Core System Data");
				break;
			case DEPENDENT:
				topLabel.setText("Covered Individual Core System Data");
				break;
			case ELIGIBILITYPERIOD:
				topLabel.setText("Eligibility Period Core System Data");
				break;
			case EMPLOYEE:
				topLabel.setText("Employee Core System Data");
				break;
			case EMPLOYER:
				topLabel.setText("Employer Core System Data");
				break;
			case EMPLOYMENTPERIOD:
				topLabel.setText("Account Core System Data");
				break;
			case FILESTEPHANDLER:
				topLabel.setText("File Step Handler Core System Data");
				break;
			case IRS1094B:
				topLabel.setText("Irs1094b Core System Data");
				break;
			case IRS1094C:
				topLabel.setText("Irs1094c Core System Data");
				break;
			case IRS1095B:
				topLabel.setText("Irs1095b Core System Data");
				break;
			case IRS1095C:
				topLabel.setText("Irs1095c Core System Data");
				break;
			case IRS1095FILING:
				topLabel.setText("Irs1095 Filing Core System Data");
				break;
			case LOCATION:
				topLabel.setText("Location Core System Data");
				break;
			case NOTE:
				topLabel.setText("Note Core System Data");
				break;
			case PAY:
				topLabel.setText("Pay Core System Data");
				break;
			case PAYPERIOD:
				topLabel.setText("Pay Period Core System Data");
				break;
			case PLANYEAR:
				topLabel.setText("Plan Year Core System Data");
				break;
			case SPECIFICATION:
				topLabel.setText("Pipeline Specification Core System Data");
				break;
			case TAXYEAR:
				topLabel.setText("TaxYear Core System Data");
				break;
			case UPLOADTYPE:
				topLabel.setText("Upload Type Core System Data");
				break;
			case USER:
				topLabel.setText("User Core System Data");
				break;
			default:
				topLabel.setText("Core System Data");
				break;
		}

	}
	
	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	

	@FXML
	private void onBatchId(ActionEvent event) {
		showFileData();
	}	

	@FXML
	private void onDeleted(ActionEvent event) 
	{
		if(deletedCheck.isSelected() == true)
			activeCheck.setSelected(false);
	}	

	@FXML
	private void onActive(ActionEvent event)
	{
		if(activeCheck.isSelected() == true)
			deletedCheck.setSelected(false);
	}	

	@FXML
	private void onEdit(ActionEvent event) 
	{
		if (DataManager.i().mCoreData != null && DataManager.i().mCoreData.isDeleted() == true) {
			Utils.showAlert("WARNING - Deleted", "Edits are not allowed on deleted items.");
			return;
		}
		
		// filter the ability to edit on supported types
		switch(DataManager.i().mCurrentCoreDataType) 
		{
			case ACCOUNT:
			case BENEFIT:
			//case CHANNEL:
			case CONTACT:
			case COVERAGE:
			case COVERAGECLASS:
			case COVERAGEGROUPMEMBERSHIP:
			case DATAPROPERTY:
			case DEPARTMENT:
			case DEPENDENT:
			case ELIGIBILITYPERIOD:
			case EMPLOYEE:
			case EMPLOYER:
			case EMPLOYMENTPERIOD:
			case IRS1095B:
			case IRS1095C:
			case LOCATION:
			case NOTE:
			case PAY:
			case PAYPERIOD:
			case PLANYEAR:
			case SPECIFICATION:
			case TAXYEAR:
			case UPLOADTYPE:
			case USER:
				
			break;
		default:
			return;	
		}

		if(editButton.getText().equals("Edit")) 
		{
			editButton.setText("Save");
			activeCheck.setDisable(false);
			if (DataManager.i().mCurrentCoreDataType == SystemDataType.EMPLOYEE)
				deletedCheck.setDisable(false);
			editRectangle.setFill(Color.LIGHTGREEN);
		} else {
			// we are saving
			updateCoreObject();
			// cleanup
			editButton.setText("Edit");
			activeCheck.setDisable(true);
			deletedCheck.setDisable(true);
			editRectangle.setFill(Color.TRANSPARENT);
			changesMade = true;
		}
	}	

	private void exitPopup() 
	{
		// reset the current system data type
		DataManager.i().mCurrentCoreDataType = SystemDataType.NONE;
		// and exit
		Stage stage = (Stage) idField.getScene().getWindow();
		stage.close();
	}
	
	private void updateCoreObject()
	{
		try {

			String activeState = "Active";
			String deletedState = "Not Deleted";

			switch(DataManager.i().mCurrentCoreDataType)
			{
				case ACCOUNT:
					if (DataManager.i().mAccount.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
					AccountRequest acctRequest = new AccountRequest();
					acctRequest.setId(DataManager.i().mAccount.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(acctRequest);
	    				DataManager.i().mAccount = AdminPersistenceManager.getInstance().get(acctRequest);
	    			} else {  // setting it active
		    			DataManager.i().mAccount.setActive(activeCheck.isSelected());
		    			acctRequest.setEntity(DataManager.i().mAccount);
		    			DataManager.i().mAccount = AdminPersistenceManager.getInstance().addOrUpdate(acctRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mAccount.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mAccount.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Account System Data Updated", "The Account for id " + DataManager.i().mAccount.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
				case BENEFIT:
					if (DataManager.i().mBenefit.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			BenefitRequest benRequest = new BenefitRequest();
	    			benRequest.setId(DataManager.i().mBenefit.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(benRequest);
	    				DataManager.i().mBenefit = AdminPersistenceManager.getInstance().get(benRequest);
	    			} else {  // setting it active
		    			DataManager.i().mEmploymentPeriod.setActive(activeCheck.isSelected());
		    			benRequest.setEntity(DataManager.i().mBenefit);
		    			DataManager.i().mBenefit = AdminPersistenceManager.getInstance().addOrUpdate(benRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mBenefit.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mBenefit.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Plan System Data Updated", "The Plan for id " + DataManager.i().mBenefit.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
				case CHANNEL:
					if (DataManager.i().mPipelineChannel.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			PipelineChannelRequest pcRequest = new PipelineChannelRequest();
	    			pcRequest.setId(DataManager.i().mPipelineChannel.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(pcRequest);
	    				DataManager.i().mPipelineChannel = AdminPersistenceManager.getInstance().get(pcRequest);
	    			} else {  // setting it active
		    			DataManager.i().mPipelineChannel.setActive(activeCheck.isSelected());
		    			pcRequest.setEntity(DataManager.i().mPipelineChannel);
		    			DataManager.i().mPipelineChannel = AdminPersistenceManager.getInstance().addOrUpdate(pcRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mPipelineChannel.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mPipelineChannel.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Pipeline Channel Data Updated", "The Channel for id " + DataManager.i().mPipelineChannel.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
		    		break;
				case CONTACT:
					if (DataManager.i().mContact.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			ContactRequest conRequest = new ContactRequest();
	    			conRequest.setId(DataManager.i().mContact.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(conRequest);
	    				DataManager.i().mContact = AdminPersistenceManager.getInstance().get(conRequest);
	    			} else {  // setting it active
		    			DataManager.i().mContact.setActive(activeCheck.isSelected());
		    			conRequest.setEntity(DataManager.i().mContact);
		    			DataManager.i().mContact = AdminPersistenceManager.getInstance().addOrUpdate(conRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mContact.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mContact.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Contact System Data Updated", "The Contact for id " + DataManager.i().mContact.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
		    		break;
				case COVERAGE:
					if (DataManager.i().mCoverage.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			CoverageRequest covRequest = new CoverageRequest();
	    			covRequest.setId(DataManager.i().mCoverage.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(covRequest);
	    				DataManager.i().mCoverage = AdminPersistenceManager.getInstance().get(covRequest);
	    			} else {  // setting it active
		    			DataManager.i().mCoverage.setActive(activeCheck.isSelected());
		    			covRequest.setEntity(DataManager.i().mCoverage);
		    			DataManager.i().mCoverage = AdminPersistenceManager.getInstance().addOrUpdate(covRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mCoverage.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mCoverage.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Coverage System Data Updated", "The Coverage for id " + DataManager.i().mCoverage.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
		    		break;
				case COVERAGECLASS:
					if (DataManager.i().mCoverageClass.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			CoverageGroupRequest covClassRequest = new CoverageGroupRequest();
	    			covClassRequest.setId(DataManager.i().mCoverageClass.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(covClassRequest);
	    				DataManager.i().mCoverageClass = AdminPersistenceManager.getInstance().get(covClassRequest);
	    			} else {  // setting it active
		    			DataManager.i().mCoverageClass.setActive(activeCheck.isSelected());
		    			covClassRequest.setEntity(DataManager.i().mCoverageClass);
		    			DataManager.i().mCoverageClass = AdminPersistenceManager.getInstance().addOrUpdate(covClassRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mCoverageClass.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mCoverageClass.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Coverage Class System Data Updated", "The Coverage Class for id " + DataManager.i().mCoverageClass.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
		    		break;
				case COVERAGEGROUPMEMBERSHIP:
					if (DataManager.i().mEmployeeCoverageGroupMembership.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			CoverageGroupMembershipRequest covGroupMembershipRequest = new CoverageGroupMembershipRequest();
	    			covGroupMembershipRequest.setId(DataManager.i().mEmployeeCoverageGroupMembership.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(covGroupMembershipRequest);
	    				DataManager.i().mEmployeeCoverageGroupMembership = AdminPersistenceManager.getInstance().get(covGroupMembershipRequest);
	    			} else {  // setting it active
		    			DataManager.i().mEmployeeCoverageGroupMembership.setActive(activeCheck.isSelected());
		    			covGroupMembershipRequest.setEntity(DataManager.i().mEmployeeCoverageGroupMembership);
		    			DataManager.i().mEmployeeCoverageGroupMembership = AdminPersistenceManager.getInstance().addOrUpdate(covGroupMembershipRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mEmployeeCoverageGroupMembership.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mEmployeeCoverageGroupMembership.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Coverage Class Membership System Data Updated", "The Coverage Class for id " + DataManager.i().mEmployeeCoverageGroupMembership.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
		    		break;
				case DATAPROPERTY:
					if (DataManager.i().mDataProperty.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			DataPropertyRequest propRequest = new DataPropertyRequest();
	    			propRequest.setId(DataManager.i().mDataProperty.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(propRequest);
	    				DataManager.i().mDataProperty = AdminPersistenceManager.getInstance().get(propRequest);
	    			} else {  // setting it active
		    			DataManager.i().mDataProperty.setActive(activeCheck.isSelected());
		    			propRequest.setEntity(DataManager.i().mDataProperty);
		    			DataManager.i().mDataProperty = AdminPersistenceManager.getInstance().addOrUpdate(propRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mDataProperty.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mDataProperty.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Data Property System Data Updated", "The Data Property for id " + DataManager.i().mDataProperty.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
				case DEPARTMENT:
					if (DataManager.i().mDepartment.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			DepartmentRequest deptRequest = new DepartmentRequest();
	    			deptRequest.setId(DataManager.i().mDepartment.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(deptRequest);
	    				DataManager.i().mDepartment = AdminPersistenceManager.getInstance().get(deptRequest);
	    			} else {  // setting it active
		    			DataManager.i().mDepartment.setActive(activeCheck.isSelected());
		    			deptRequest.setEntity(DataManager.i().mDepartment);
		    			DataManager.i().mDepartment = AdminPersistenceManager.getInstance().addOrUpdate(deptRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mDepartment.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mDepartment.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Department System Data Updated", "The Department for id " + DataManager.i().mDepartment.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
				case DEPENDENT:
					if (DataManager.i().mDependent.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			DependentRequest depRequest = new DependentRequest();
	    			depRequest.setId(DataManager.i().mDependent.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(depRequest);
	    				DataManager.i().mDependent = AdminPersistenceManager.getInstance().get(depRequest);
	    			} else {  // setting it active
		    			DataManager.i().mDependent.setActive(activeCheck.isSelected());
		    			depRequest.setEntity(DataManager.i().mDependent);
		    			DataManager.i().mDependent = AdminPersistenceManager.getInstance().addOrUpdate(depRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mDependent.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mDependent.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Dependent System Data Updated", "The Dependent for id " + DataManager.i().mDependent.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
//				case ELIGIBILITYPERIOD:
//	    			DataManager.i().mEligibilityPeriod.setActive(activeCheck.isSelected());
//	    			DataManager.i().mEligibilityPeriod.setDeleted(deletedCheck.isSelected());
//	    			EligibilityPeriodRequest eligPeriodRequest = new EligibilityPeriodRequest();
//	    			eligPeriodRequest.setId(DataManager.i().mEligibilityPeriod.getId());
//	    			eligPeriodRequest.setEntity(DataManager.i().mEligibilityPeriod);
//	    			AdminPersistenceManager.getInstance().addOrUpdate(eligPeriodRequest);
//	    			if(DataManager.i().mEligibilityPeriod.isActive() == false)
//	    				activeState = "InActive";
//	    			if(DataManager.i().mEligibilityPeriod.isDeleted() == true)
//	    				deletedState = "Deleted";
//	    			Utils.showAlert("Eligibility Period System Data Updated", "The Eligibility Period for id " + DataManager.i().mEligibilityPeriod.getId() + 
//	    					" has been set to \n\t" + activeState + " and " + deletedState);
//	    			break;
				case EMPLOYEE:
					if (DataManager.i().mEmployee.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			EmployeeRequest empRequest = new EmployeeRequest();
	    			empRequest.setId(DataManager.i().mEmployee.getId());
	    			if (deletedCheck.isSelected() == true) {
	    				empRequest.setDeleted = true;
	    				DataManager.i().mEmployee.setDeleted(true);
	    				empRequest.setEntity(DataManager.i().mEmployee);
	    				AdminPersistenceManager.getInstance().remove(empRequest);
	    				DataManager.i().mEmployee = AdminPersistenceManager.getInstance().get(empRequest);	    				
	    			} else {
		    			if (activeCheck.isSelected() == false) {
		    				AdminPersistenceManager.getInstance().remove(empRequest);
		    				DataManager.i().mEmployee = AdminPersistenceManager.getInstance().get(empRequest);
		    			} else {  // setting it active
			    			DataManager.i().mEmployee.setActive(activeCheck.isSelected());
			    			empRequest.setEntity(DataManager.i().mEmployee);
			    			DataManager.i().mEmployee = AdminPersistenceManager.getInstance().addOrUpdate(empRequest);
		    			}
	    			}
	    			// respond to the user
	    			if(DataManager.i().mEmployee.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mEmployee.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Employee System Data Updated", "The Employee for id " + DataManager.i().mEmployee.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
				case EMPLOYER:
					if (DataManager.i().mEmployer.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			EmployerRequest emprRequest = new EmployerRequest();
	    			emprRequest.setId(DataManager.i().mEmployer.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(emprRequest);
	    				DataManager.i().mEmployer = AdminPersistenceManager.getInstance().get(emprRequest);
	    			} else {  // setting it active
		    			DataManager.i().mEmployer.setActive(activeCheck.isSelected());
		    			emprRequest.setEntity(DataManager.i().mEmployer);
		    			DataManager.i().mEmployer = AdminPersistenceManager.getInstance().addOrUpdate(emprRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mEmployer.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mEmployer.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Employer System Data Updated", "The Employer for id " + DataManager.i().mEmployer.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
				case EMPLOYMENTPERIOD:
					if (DataManager.i().mEmploymentPeriod.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			EmploymentPeriodRequest empPeriodRequest = new EmploymentPeriodRequest();
	    			empPeriodRequest.setId(DataManager.i().mEmploymentPeriod.getId());
	    			if (activeCheck.isSelected() == false) {
	    				// deleting example in the commented out portion - will use when we can set them back as active
	    				//empPeriodRequest.setDeleted = true;
	    				//DataManager.i().mEmploymentPeriod.setDeleted(true);
	    				//empPeriodRequest.setEntity(DataManager.i().mEmploymentPeriod);
	    				// end of delete example
	    				AdminPersistenceManager.getInstance().remove(empPeriodRequest);
	    				DataManager.i().mEmploymentPeriod = AdminPersistenceManager.getInstance().get(empPeriodRequest);
	    			} else {  // setting it active
		    			DataManager.i().mEmploymentPeriod.setActive(activeCheck.isSelected());
		    			empPeriodRequest.setEntity(DataManager.i().mEmploymentPeriod);
		    			DataManager.i().mEmploymentPeriod = AdminPersistenceManager.getInstance().addOrUpdate(empPeriodRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mEmploymentPeriod.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mEmploymentPeriod.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Employment Period System Data Updated", "The Employment Period \nfor id " + DataManager.i().mEmploymentPeriod.getId() + 
	    					"\nhas been set to \n\n\t" + activeState + " and " + deletedState);
	    			break;
				case IRS1095B:
					if (DataManager.i().mIrs1095b.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
					Irs1095bRequest irs1095bRequest = new Irs1095bRequest();
	    			irs1095bRequest.setId(DataManager.i().mIrs1095b.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(irs1095bRequest);
	    				DataManager.i().mIrs1095b = AdminPersistenceManager.getInstance().get(irs1095bRequest);
	    			} else {  // setting it active
		    			DataManager.i().mIrs1095b.setActive(activeCheck.isSelected());
		    			irs1095bRequest.setEntity(DataManager.i().mIrs1095b);
		    			DataManager.i().mIrs1095b = AdminPersistenceManager.getInstance().addOrUpdate(irs1095bRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mIrs1095b.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mIrs1095b.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Irs1095b System Data Updated", "The Irs1095b for id " + DataManager.i().mIrs1095b.getId() + 
	    					"has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
				case IRS1095C:
					if (DataManager.i().mIrs1095c.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			Irs1095cRequest irs1095cRequest = new Irs1095cRequest();
	    			irs1095cRequest.setId(DataManager.i().mIrs1095c.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(irs1095cRequest);
	    				DataManager.i().mIrs1095c = AdminPersistenceManager.getInstance().get(irs1095cRequest);
	    			} else {  // setting it active
		    			DataManager.i().mIrs1095c.setActive(activeCheck.isSelected());
		    			irs1095cRequest.setEntity(DataManager.i().mIrs1095c);
		    			DataManager.i().mIrs1095c = AdminPersistenceManager.getInstance().addOrUpdate(irs1095cRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mIrs1095c.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mIrs1095c.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Irs1095c System Data Updated", "The Irs1095c for id " + DataManager.i().mIrs1095c.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
				case LOCATION:
					if (DataManager.i().mLocation.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			LocationRequest locRequest = new LocationRequest();
	    			locRequest.setId(DataManager.i().mLocation.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(locRequest);
	    				DataManager.i().mLocation = AdminPersistenceManager.getInstance().get(locRequest);
	    			} else {  // setting it active
		    			DataManager.i().mLocation.setActive(activeCheck.isSelected());
		    			locRequest.setEntity(DataManager.i().mLocation);
		    			DataManager.i().mLocation = AdminPersistenceManager.getInstance().addOrUpdate(locRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mLocation.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mLocation.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Location System Data Updated", "The Location for id " + DataManager.i().mLocation.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
				case NOTE:
					if (DataManager.i().mNote.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			NoteRequest noteRequest = new NoteRequest();
	    			noteRequest.setId(DataManager.i().mNote.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(noteRequest);
	    				DataManager.i().mNote = AdminPersistenceManager.getInstance().get(noteRequest);
	    			} else {  // setting it active
		    			DataManager.i().mNote.setActive(activeCheck.isSelected());
		    			noteRequest.setEntity(DataManager.i().mNote);
		    			DataManager.i().mNote = AdminPersistenceManager.getInstance().addOrUpdate(noteRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mNote.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mNote.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Note System Data Updated", "The Note for id " + DataManager.i().mNote.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
				case PAY:
					if (DataManager.i().mPay.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			PayRequest payRequest = new PayRequest();
	    			payRequest.setId(DataManager.i().mPay.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(payRequest);
	    				DataManager.i().mPay = AdminPersistenceManager.getInstance().get(payRequest);
	    			} else {  // setting it active
		    			DataManager.i().mPay.setActive(activeCheck.isSelected());
		    			payRequest.setEntity(DataManager.i().mPay);
		    			DataManager.i().mPay = AdminPersistenceManager.getInstance().addOrUpdate(payRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mPay.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mPay.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Pay System Data Updated", "The Pay for id " + DataManager.i().mPay.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
				case PAYPERIOD:
					if (DataManager.i().mPayPeriod.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			PayPeriodRequest payPeriodRequest = new PayPeriodRequest();
	    			payPeriodRequest.setId(DataManager.i().mPayPeriod.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(payPeriodRequest);
	    				DataManager.i().mPayPeriod = AdminPersistenceManager.getInstance().get(payPeriodRequest);
	    			} else {  // setting it active
		    			DataManager.i().mPayPeriod.setActive(activeCheck.isSelected());
		    			payPeriodRequest.setEntity(DataManager.i().mPayPeriod);
		    			DataManager.i().mPayPeriod = AdminPersistenceManager.getInstance().addOrUpdate(payPeriodRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mPayPeriod.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mPayPeriod.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Pay Period System Data Updated", "The Pay Period for id " + DataManager.i().mPayPeriod.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
				case PLANYEAR:
					if (DataManager.i().mPlanYear.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			PlanYearRequest planYearRequest = new PlanYearRequest();
	    			planYearRequest.setId(DataManager.i().mPlanYear.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(planYearRequest);
	    				DataManager.i().mPlanYear = AdminPersistenceManager.getInstance().get(planYearRequest);
	    			} else {  // setting it active
		    			DataManager.i().mPlanYear.setActive(activeCheck.isSelected());
		    			planYearRequest.setEntity(DataManager.i().mPlanYear);
		    			DataManager.i().mPlanYear = AdminPersistenceManager.getInstance().addOrUpdate(planYearRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mPlanYear.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mPlanYear.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Plan Year System Data Updated", "The Plan Year for id " + DataManager.i().mPlanYear.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
				case SPECIFICATION:
					if (DataManager.i().mPipelineSpecification.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			PipelineSpecificationRequest psRequest = new PipelineSpecificationRequest();
	    			psRequest.setId(DataManager.i().mPipelineSpecification.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(psRequest);
	    				DataManager.i().mPipelineSpecification = AdminPersistenceManager.getInstance().get(psRequest);
	    			} else {  // setting it active
		    			DataManager.i().mPipelineSpecification.setActive(activeCheck.isSelected());
		    			psRequest.setEntity(DataManager.i().mPipelineSpecification);
		    			DataManager.i().mPipelineSpecification = AdminPersistenceManager.getInstance().addOrUpdate(psRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mPipelineSpecification.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mPipelineSpecification.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Pipeline Specification Data Updated", "The Specification for id " + DataManager.i().mPipelineSpecification.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
		    		break;
				case TAXYEAR:
					if (DataManager.i().mTaxYear.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			TaxYearRequest taxYearRequest = new TaxYearRequest();
	    			taxYearRequest.setId(DataManager.i().mTaxYear.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(taxYearRequest);
	    				DataManager.i().mTaxYear = AdminPersistenceManager.getInstance().get(taxYearRequest);
	    			} else {  // setting it active
		    			DataManager.i().mTaxYear.setActive(activeCheck.isSelected());
		    			taxYearRequest.setEntity(DataManager.i().mTaxYear);
		    			DataManager.i().mTaxYear = AdminPersistenceManager.getInstance().addOrUpdate(taxYearRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mTaxYear.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mTaxYear.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("Tax Year System Data Updated", "The Tax Year for id " + DataManager.i().mTaxYear.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
				case UPLOADTYPE:
					if (DataManager.i().mUploadType.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
	    			UploadTypeRequest utRequest = new UploadTypeRequest();
	    			utRequest.setId(DataManager.i().mUploadType.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(utRequest);
	    				DataManager.i().mUploadType = AdminPersistenceManager.getInstance().get(utRequest);
	    			} else {  // setting it active
		    			DataManager.i().mUploadType.setActive(activeCheck.isSelected());
		    			utRequest.setEntity(DataManager.i().mUploadType);
		    			DataManager.i().mUploadType = AdminPersistenceManager.getInstance().addOrUpdate(utRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mUploadType.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mUploadType.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("UploadType Data Updated", "The UploadType for id " + DataManager.i().mUploadType.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
		    		break;
				case USER:
					if (DataManager.i().mUser.getId().equals(DataManager.i().mCoreData.getId()) == false) {
						Utils.showAlert("Core Id Mismatch", "The Provided core data Id does not match the object being updated. Changes will not be saved.");
						return;
					}
					UserRequest userRequest = new UserRequest();
	    			userRequest.setId(DataManager.i().mUser.getId());
	    			if (activeCheck.isSelected() == false) {
	    				AdminPersistenceManager.getInstance().remove(userRequest);
	    				DataManager.i().mUser = AdminPersistenceManager.getInstance().get(userRequest);
	    			} else {  // setting it active
		    			DataManager.i().mUser.setActive(activeCheck.isSelected());
		    			userRequest.setEntity(DataManager.i().mUser);
		    			DataManager.i().mUser = AdminPersistenceManager.getInstance().addOrUpdate(userRequest);
	    			}
	    			// respond to the user
	    			if(DataManager.i().mUser.isActive() == false)
	    				activeState = "InActive";
	    			if(DataManager.i().mUser.isDeleted() == true)
	    				deletedState = "Deleted";
	    			Utils.showAlert("User System Data Updated", "The User for id " + DataManager.i().mUser.getId() + 
	    					" has been set to \n\t" + activeState + " and " + deletedState);
	    			break;
				default:
					return;
			}
		} catch (CoreException e) 
		{
        	DataManager.i().log(Level.SEVERE, e); 
        	Utils.showAlert("Error", "There was a problem with the System Info Data");
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

	}

	private void showFileData() 
	{
		switch (DataManager.i().mCurrentCoreDataType) 
		{
			case BENEFIT:
			case COVERAGE:
			//case IRS1094C:
			case EMPLOYEE:
			case PAY:
				try {
		            // load the fxml
			        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewFileDetail.fxml"));
					Parent ControllerNode = loader.load();
			        Stage stage = new Stage();
			        stage.initModality(Modality.APPLICATION_MODAL);
			        stage.initStyle(StageStyle.UNDECORATED);
			        stage.setScene(new Scene(ControllerNode));  
			        EtcAdmin.i().positionStageCenter(stage);
			        stage.showAndWait();
				} catch (IOException e) { DataManager.i().log(Level.SEVERE, e); }  
			    catch (Exception e) {  DataManager.i().logGenericException(e); }
			break;
 		default:
			return;
		}
	}
}
