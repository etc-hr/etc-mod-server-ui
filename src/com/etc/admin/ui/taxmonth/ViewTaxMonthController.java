package com.etc.admin.ui.taxmonth;

import java.text.SimpleDateFormat;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewTaxMonthController {
	
	@FXML
	private Label txmnTitleLabel;
	@FXML
	private TextField txmnMonthLabel;
	@FXML
	private CheckBox txmnClosedCheckBox;
	@FXML
	private TextField txmnClosedOnLabel;
	@FXML
	private TextField txmnClosedByLabel;
	@FXML
	private CheckBox txmnApprovedCheckBox;
	@FXML
	private TextField txmnApprovedOnLabel;
	@FXML
	private TextField txmnApprovedByLabel;
	@FXML
	private Button txmnEditButton;
	@FXML
	private Label txmnCoreIdLabel;
	@FXML
	private Label txmnCoreActiveLabel;
	@FXML
	private Label txmnCoreBODateLabel;
	@FXML
	private Label txmnCoreLastUpdatedLabel;
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		// initialize the controls
		initControls();
		//update the dialog data
		updateTaxMonthData();
	}

	private void initControls() {
		//set functionality according to the user security level
		txmnEditButton.setDisable(!Utils.userCanEdit());
	}
	private void updateTaxMonthData(){
		
		if (DataManager.i().mTaxMonth != null) {
			String sName = DataManager.i().mEmployer.getName() + " Tax Year " + String.valueOf(DataManager.i().mTaxYear.getYear()) + " Tax Month";
				
			Utils.updateControl(txmnTitleLabel,sName);
			Utils.updateControl(txmnMonthLabel,String.valueOf(DataManager.i().mTaxMonth.getMonth()));
			
			//closed
			Utils.updateControl(txmnClosedCheckBox,DataManager.i().mTaxMonth.isClosed());
			txmnClosedCheckBox.setDisable(true);
			
			if (DataManager.i().mTaxMonth.getClosedBy() != null) 
				Utils.updateControl(txmnClosedByLabel,DataManager.i().mTaxMonth.getClosedBy().getFirstName() 
					+ " " + DataManager.i().mTaxMonth.getClosedBy().getLastName());
			else
				Utils.updateControl(txmnClosedByLabel,"");
			
			Utils.updateControl(txmnClosedOnLabel,DataManager.i().mTaxMonth.getClosedOn());
			
			//pay periods approved
			Utils.updateControl(txmnApprovedCheckBox,DataManager.i().mTaxMonth.isPayPeriodsApproved());
			txmnApprovedCheckBox.setDisable(true);
			
			if (DataManager.i().mTaxMonth.getPayPeriodsApprovedBy() != null) 
				Utils.updateControl(txmnApprovedByLabel,DataManager.i().mTaxMonth.getPayPeriodsApprovedBy().getFirstName() 
					+ " " + DataManager.i().mTaxMonth.getPayPeriodsApprovedBy().getLastName());
			else
				Utils.updateControl(txmnApprovedByLabel,"");
			
			Utils.updateControl(txmnApprovedOnLabel,DataManager.i().mTaxMonth.getPayPeriodsApprovedOn());
			

			// core data
			Utils.updateControl(txmnCoreIdLabel,String.valueOf(DataManager.i().mTaxMonth.getId()));
			Utils.updateControl(txmnCoreActiveLabel,String.valueOf(DataManager.i().mTaxMonth.isActive()));
			Utils.updateControl(txmnCoreBODateLabel,DataManager.i().mTaxMonth.getBornOn());
			Utils.updateControl(txmnCoreLastUpdatedLabel,DataManager.i().mTaxMonth.getLastUpdated());
		}
	}	
	
	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.TAXMONTHEDIT, true);
	}	
	
	
}
