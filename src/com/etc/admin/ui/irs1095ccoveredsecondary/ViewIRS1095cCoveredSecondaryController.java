package com.etc.admin.ui.irs1095ccoveredsecondary;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.Irs1095c;
import com.etc.corvetto.entities.Irs1095cCI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class ViewIRS1095cCoveredSecondaryController {
	
	@FXML
	private Label irscTitleLabel;
	@FXML
	private CheckBox irscAnnCoveredCheckBox;
	@FXML
	private CheckBox irscJanCoveredCheckBox;
	@FXML
	private CheckBox irscFebCoveredCheckBox;
	@FXML
	private CheckBox irscMarCoveredCheckBox;
	@FXML
	private CheckBox irscAprCoveredCheckBox;
	@FXML
	private CheckBox irscMayCoveredCheckBox;
	@FXML
	private CheckBox irscJunCoveredCheckBox;
	@FXML
	private CheckBox irscJulCoveredCheckBox;
	@FXML
	private CheckBox irscAugCoveredCheckBox;
	@FXML
	private CheckBox irscSepCoveredCheckBox;
	@FXML
	private CheckBox irscOctCoveredCheckBox;
	@FXML
	private CheckBox irscNovCoveredCheckBox;
	@FXML
	private CheckBox irscDecCoveredCheckBox;
	@FXML
	private Button irscEditButton;
	@FXML
	private Label irscCoreIdLabel;
	@FXML
	private Label irscCoreActiveLabel;
	@FXML
	private Label irscCoreBODateLabel;
	@FXML
	private Label irscCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		update1095bCoveredSecondaryData();
	}
	
	private void initControls() {
		//set functionality according to the user security level
		irscEditButton.setDisable(!Utils.userCanEdit());
	}

	private void update1095bCoveredSecondaryData(){
		
		Irs1095c irsc = DataManager.i().mIrs1095c;
		if (irsc != null) {
			String sName = "";
			if (irsc.getEmployee()!= null) {
				sName = irsc.getEmployee().getPerson().getFirstName();
				sName.concat(" ");
				sName.concat(irsc.getEmployee().getPerson().getLastName());
				sName.concat(" 1095c");
			}

			Utils.updateControl(irscTitleLabel,sName);	
			Utils.updateControl(irscJanCoveredCheckBox,irsc.isJanCovered());	
			Utils.updateControl(irscFebCoveredCheckBox,irsc.isFebCovered());	
			Utils.updateControl(irscMarCoveredCheckBox,irsc.isMarCovered());	
			Utils.updateControl(irscAprCoveredCheckBox,irsc.isAprCovered());	
			Utils.updateControl(irscMayCoveredCheckBox,irsc.isMayCovered());	
			Utils.updateControl(irscJunCoveredCheckBox,irsc.isJunCovered());	
			Utils.updateControl(irscJulCoveredCheckBox,irsc.isJulCovered());	
			Utils.updateControl(irscAugCoveredCheckBox,irsc.isAugCovered());	
			Utils.updateControl(irscSepCoveredCheckBox,irsc.isSepCovered());	
			Utils.updateControl(irscOctCoveredCheckBox,irsc.isOctCovered());	
			Utils.updateControl(irscNovCoveredCheckBox,irsc.isNovCovered());	
			Utils.updateControl(irscDecCoveredCheckBox,irsc.isDecCovered());	

			// core data
			Utils.updateControl(irscCoreIdLabel,String.valueOf(DataManager.i().mIrs1094b.getId()));
			Utils.updateControl(irscCoreActiveLabel,String.valueOf(DataManager.i().mIrs1094b.isActive()));
			Utils.updateControl(irscCoreBODateLabel,DataManager.i().mIrs1094b.getBornOn());
			Utils.updateControl(irscCoreLastUpdatedLabel,DataManager.i().mIrs1094b.getLastUpdated());
		}
	}
	
	private void updateIrs1095bCoveredSecondary(){
		
		Irs1095cCI irsc = DataManager.i().mIrs1095cCI;
		if (irsc != null) {
			irsc.setJanCovered(irscJanCoveredCheckBox.isSelected());	
			irsc.setFebCovered(irscFebCoveredCheckBox.isSelected());	
			irsc.setMarCovered(irscMarCoveredCheckBox.isSelected());	
			irsc.setAprCovered(irscAprCoveredCheckBox.isSelected());	
			irsc.setMayCovered(irscMayCoveredCheckBox.isSelected());	
			irsc.setJunCovered(irscJunCoveredCheckBox.isSelected());	
			irsc.setJulCovered(irscJulCoveredCheckBox.isSelected());	
			irsc.setAugCovered(irscAugCoveredCheckBox.isSelected());	
			irsc.setSepCovered(irscSepCoveredCheckBox.isSelected());	
			irsc.setOctCovered(irscOctCoveredCheckBox.isSelected());	
			irsc.setNovCovered(irscNovCoveredCheckBox.isSelected());	
			irsc.setDecCovered(irscDecCoveredCheckBox.isSelected());	
		}	
	}

	private boolean validateData() {
		boolean bReturn = true;

		//nothing to check for now
		
		return bReturn;
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.IRS1095CCOVEREDSECONDARY);
	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updateIrs1095bCoveredSecondary();
		
		//save it to the repository and the server
		// will come back to this when the backend is in place
		//DataManager.i().saveCurrentIrs1095bCoveredSecondary(); 
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.IRS1095CCOVEREDSECONDARY, true);
	}	
}
