package com.etc.admin.ui.irs1094;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.Irs1094c;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewIRS1094cController {
	
	@FXML
	private Label irscTitleLabel;
	@FXML
	private CheckBox irscClosedCheckBox;
	@FXML
	private TextField irscClosedOnLabel;
	// ui data table
	@FXML
	private CheckBox irscAnnMECOfferCheckBox;
	@FXML
	private CheckBox irscAnnMECSelectedCheckBox;
	@FXML
	private TextField irscAnnFTEmpCountField;
	@FXML
	private TextField irscAnnTotEmpCountField;
	@FXML
	private CheckBox irscAnnAggGrpCheckBox;
	@FXML
	private CheckBox irscAnnAggGrpSelectedCheckBox;
	@FXML
	private CheckBox irscJanMECOfferCheckBox;
	@FXML
	private TextField irscJanFTEmpCountField;
	@FXML
	private TextField irscJanTotEmpCountField;
	@FXML
	private CheckBox irscJanAggGrpCheckBox;
	@FXML
	private CheckBox irscFebMECOfferCheckBox;
	@FXML
	private TextField irscFebFTEmpCountField;
	@FXML
	private TextField irscFebTotEmpCountField;
	@FXML
	private CheckBox irscFebAggGrpCheckBox;
	@FXML
	private CheckBox irscMarMECOfferCheckBox;
	@FXML
	private TextField irscMarFTEmpCountField;
	@FXML
	private TextField irscMarTotEmpCountField;
	@FXML
	private CheckBox irscMarAggGrpCheckBox;
	@FXML
	private CheckBox irscAprMECOfferCheckBox;
	@FXML
	private TextField irscAprFTEmpCountField;
	@FXML
	private TextField irscAprTotEmpCountField;
	@FXML
	private CheckBox irscAprAggGrpCheckBox;
	@FXML
	private CheckBox irscMayMECOfferCheckBox;
	@FXML
	private TextField irscMayFTEmpCountField;
	@FXML
	private TextField irscMayTotEmpCountField;
	@FXML
	private CheckBox irscMayAggGrpCheckBox;
	@FXML
	private CheckBox irscJunMECOfferCheckBox;
	@FXML
	private TextField irscJunFTEmpCountField;
	@FXML
	private TextField irscJunTotEmpCountField;
	@FXML
	private CheckBox irscJunAggGrpCheckBox;
	@FXML
	private CheckBox irscJulMECOfferCheckBox;
	@FXML
	private TextField irscJulFTEmpCountField;
	@FXML
	private TextField irscJulTotEmpCountField;
	@FXML
	private CheckBox irscJulAggGrpCheckBox;
	@FXML
	private CheckBox irscAugMECOfferCheckBox;
	@FXML
	private TextField irscAugFTEmpCountField;
	@FXML
	private TextField irscAugTotEmpCountField;
	@FXML
	private CheckBox irscAugAggGrpCheckBox;
	@FXML
	private CheckBox irscSepMECOfferCheckBox;
	@FXML
	private TextField irscSepFTEmpCountField;
	@FXML
	private TextField irscSepTotEmpCountField;
	@FXML
	private CheckBox irscSepAggGrpCheckBox;
	@FXML
	private CheckBox irscOctMECOfferCheckBox;
	@FXML
	private TextField irscOctFTEmpCountField;
	@FXML
	private TextField irscOctTotEmpCountField;
	@FXML
	private CheckBox irscOctAggGrpCheckBox;
	@FXML
	private CheckBox irscNovMECOfferCheckBox;
	@FXML
	private TextField irscNovFTEmpCountField;
	@FXML
	private TextField irscNovTotEmpCountField;
	@FXML
	private CheckBox irscNovAggGrpCheckBox;
	@FXML
	private CheckBox irscDecMECOfferCheckBox;
	@FXML
	private TextField irscDecFTEmpCountField;
	@FXML
	private TextField irscDecTotEmpCountField;
	@FXML
	private CheckBox irscDecAggGrpCheckBox;
	// Core Data
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
		update1094cData();
	}
	
	private void initControls() {
		// add control init here
	}

	private void update1094cData(){
		Irs1094c irsc = DataManager.i().mIrs1094c;
		if (irsc != null) {
			String sName = DataManager.i().mEmployer.getName() + " 1094c";
			Utils.updateControl(irscTitleLabel,sName);	

//			Utils.updateControl(irscAnnMECOfferCheckBox, irsc.isAnnMECOffer());
//			Utils.updateControl(irscAnnMECSelectedCheckBox, irsc.isAnnMECSelected());
//			Utils.updateControl(irscAnnFTEmpCountField, irsc.getAnnFTEmpCount());
//			Utils.updateControl(irscAnnTotEmpCountField, irsc.getAnnTotEmpCount());
//			Utils.updateControl(irscAnnAggGrpCheckBox, irsc.isAnnAggGrp());
//			Utils.updateControl(irscAnnAggGrpSelectedCheckBox, irsc.isAnnAggGrpSelected());

			Utils.updateControl(irscJanMECOfferCheckBox, irsc.isJanMECOffer());
			Utils.updateControl(irscJanFTEmpCountField, irsc.getJanFTEmpCount());
			Utils.updateControl(irscJanTotEmpCountField, irsc.getJanTotEmpCount());
			Utils.updateControl(irscJanAggGrpCheckBox, irsc.isJanAggGrp());
			
			Utils.updateControl(irscFebMECOfferCheckBox, irsc.isFebMECOffer());
			Utils.updateControl(irscFebFTEmpCountField, irsc.getFebFTEmpCount());
			Utils.updateControl(irscFebTotEmpCountField, irsc.getFebTotEmpCount());
			Utils.updateControl(irscFebAggGrpCheckBox, irsc.isFebAggGrp());
			
			Utils.updateControl(irscMarMECOfferCheckBox, irsc.isMarMECOffer());
			Utils.updateControl(irscMarFTEmpCountField, irsc.getMarFTEmpCount());
			Utils.updateControl(irscMarTotEmpCountField, irsc.getMarTotEmpCount());
			Utils.updateControl(irscMarAggGrpCheckBox, irsc.isMarAggGrp());
			
			Utils.updateControl(irscAprMECOfferCheckBox, irsc.isAprMECOffer());
			Utils.updateControl(irscAprFTEmpCountField, irsc.getAprFTEmpCount());
			Utils.updateControl(irscAprTotEmpCountField, irsc.getAprTotEmpCount());
			Utils.updateControl(irscAprAggGrpCheckBox, irsc.isAprAggGrp());
			
			Utils.updateControl(irscMayMECOfferCheckBox, irsc.isMayMECOffer());
			Utils.updateControl(irscMayFTEmpCountField, irsc.getMayFTEmpCount());
			Utils.updateControl(irscMayTotEmpCountField, irsc.getMayTotEmpCount());
			Utils.updateControl(irscMayAggGrpCheckBox, irsc.isMayAggGrp());
			
			Utils.updateControl(irscJunMECOfferCheckBox, irsc.isJunMECOffer());
			Utils.updateControl(irscJunFTEmpCountField, irsc.getJunFTEmpCount());
			Utils.updateControl(irscJunTotEmpCountField, irsc.getJunTotEmpCount());
			Utils.updateControl(irscJunAggGrpCheckBox, irsc.isJunAggGrp());
			
			Utils.updateControl(irscJulMECOfferCheckBox, irsc.isJulMECOffer());
			Utils.updateControl(irscJulFTEmpCountField, irsc.getJulFTEmpCount());
			Utils.updateControl(irscJulTotEmpCountField, irsc.getJulTotEmpCount());
			Utils.updateControl(irscJulAggGrpCheckBox, irsc.isJulAggGrp());
			
			Utils.updateControl(irscAugMECOfferCheckBox, irsc.isAugMECOffer());
			Utils.updateControl(irscAugFTEmpCountField, irsc.getAugFTEmpCount());
			Utils.updateControl(irscAugTotEmpCountField, irsc.getAugTotEmpCount());
			Utils.updateControl(irscAugAggGrpCheckBox, irsc.isAugAggGrp());
			
			Utils.updateControl(irscSepMECOfferCheckBox, irsc.isSepMECOffer());
			Utils.updateControl(irscSepFTEmpCountField, irsc.getSepFTEmpCount());
			Utils.updateControl(irscSepTotEmpCountField, irsc.getSepTotEmpCount());
			Utils.updateControl(irscSepAggGrpCheckBox, irsc.isSepAggGrp());
			
			Utils.updateControl(irscOctMECOfferCheckBox, irsc.isOctMECOffer());
			Utils.updateControl(irscOctFTEmpCountField, irsc.getOctFTEmpCount());
			Utils.updateControl(irscOctTotEmpCountField, irsc.getOctTotEmpCount());
			Utils.updateControl(irscOctAggGrpCheckBox, irsc.isOctAggGrp());
			
			Utils.updateControl(irscNovMECOfferCheckBox, irsc.isNovMECOffer());
			Utils.updateControl(irscNovFTEmpCountField, irsc.getNovFTEmpCount());
			Utils.updateControl(irscNovTotEmpCountField, irsc.getNovTotEmpCount());
			Utils.updateControl(irscNovAggGrpCheckBox, irsc.isNovAggGrp());
			
			Utils.updateControl(irscDecMECOfferCheckBox, irsc.isDecMECOffer());
			Utils.updateControl(irscDecFTEmpCountField, irsc.getDecFTEmpCount());
			Utils.updateControl(irscDecTotEmpCountField, irsc.getDecTotEmpCount());
			Utils.updateControl(irscDecAggGrpCheckBox, irsc.isDecAggGrp());
			
			// core data
			Utils.updateControl(irscCoreIdLabel,String.valueOf(DataManager.i().mIrs1094b.getId()));
			Utils.updateControl(irscCoreActiveLabel,String.valueOf(DataManager.i().mIrs1094b.isActive()));
			Utils.updateControl(irscCoreBODateLabel,DataManager.i().mIrs1094b.getBornOn());
			Utils.updateControl(irscCoreLastUpdatedLabel,DataManager.i().mIrs1094b.getLastUpdated());
		}
	}

	private void update1094c(){
		Irs1094c irsc = DataManager.i().mIrs1094c;
		if (irsc != null) {
//			irsc.setAnnMECOffer(irscAnnMECOfferCheckBox.isSelected());
//			irsc.setAnnMECSelected(irscAnnMECSelectedCheckBox.isSelected()); 
//			irsc.setAnnFTEmpCount(Long.valueOf(irscAnnFTEmpCountField.getText()));
//			irsc.setAnnTotEmpCount(Long.valueOf(irscAnnTotEmpCountField.getText()));
//			irsc.setAnnAggGrp(irscAnnAggGrpCheckBox.isSelected());
//			irsc.setAnnAggGrpSelected(irscAnnAggGrpSelectedCheckBox.isSelected());

			irsc.setJanMECOffer(irscJanMECOfferCheckBox.isSelected());
			irsc.setJanFTEmpCount(Long.valueOf(irscJanFTEmpCountField.getText()));
			irsc.setJanTotEmpCount(Long.valueOf(irscJanTotEmpCountField.getText()));
			irsc.setJanAggGrp(irscJanAggGrpCheckBox.isSelected());

			irsc.setFebMECOffer(irscFebMECOfferCheckBox.isSelected());
			irsc.setFebFTEmpCount(Long.valueOf(irscFebFTEmpCountField.getText()));
			irsc.setFebTotEmpCount(Long.valueOf(irscFebTotEmpCountField.getText()));
			irsc.setFebAggGrp(irscFebAggGrpCheckBox.isSelected());

			irsc.setMarMECOffer(irscMarMECOfferCheckBox.isSelected());
			irsc.setMarFTEmpCount(Long.valueOf(irscMarFTEmpCountField.getText()));
			irsc.setMarTotEmpCount(Long.valueOf(irscMarTotEmpCountField.getText()));
			irsc.setMarAggGrp(irscMarAggGrpCheckBox.isSelected());

			irsc.setAprMECOffer(irscAprMECOfferCheckBox.isSelected());
			irsc.setAprFTEmpCount(Long.valueOf(irscAprFTEmpCountField.getText()));
			irsc.setAprTotEmpCount(Long.valueOf(irscAprTotEmpCountField.getText()));
			irsc.setAprAggGrp(irscAprAggGrpCheckBox.isSelected());

			irsc.setMayMECOffer(irscMayMECOfferCheckBox.isSelected());
			irsc.setMayFTEmpCount(Long.valueOf(irscMayFTEmpCountField.getText()));
			irsc.setMayTotEmpCount(Long.valueOf(irscMayTotEmpCountField.getText()));
			irsc.setMayAggGrp(irscMayAggGrpCheckBox.isSelected());

			irsc.setJunMECOffer(irscJunMECOfferCheckBox.isSelected());
			irsc.setJunFTEmpCount(Long.valueOf(irscJunFTEmpCountField.getText()));
			irsc.setJunTotEmpCount(Long.valueOf(irscJunTotEmpCountField.getText()));
			irsc.setJunAggGrp(irscJunAggGrpCheckBox.isSelected());

			irsc.setJulMECOffer(irscJulMECOfferCheckBox.isSelected());
			irsc.setJulFTEmpCount(Long.valueOf(irscJulFTEmpCountField.getText()));
			irsc.setJulTotEmpCount(Long.valueOf(irscJulTotEmpCountField.getText()));
			irsc.setJulAggGrp(irscJulAggGrpCheckBox.isSelected());

			irsc.setAugMECOffer(irscAugMECOfferCheckBox.isSelected());
			irsc.setAugFTEmpCount(Long.valueOf(irscAugFTEmpCountField.getText()));
			irsc.setAugTotEmpCount(Long.valueOf(irscAugTotEmpCountField.getText()));
			irsc.setAugAggGrp(irscAugAggGrpCheckBox.isSelected());

			irsc.setSepMECOffer(irscSepMECOfferCheckBox.isSelected());
			irsc.setSepFTEmpCount(Long.valueOf(irscSepFTEmpCountField.getText()));
			irsc.setSepTotEmpCount(Long.valueOf(irscSepTotEmpCountField.getText()));
			irsc.setSepAggGrp(irscSepAggGrpCheckBox.isSelected());

			irsc.setOctMECOffer(irscOctMECOfferCheckBox.isSelected());
			irsc.setOctFTEmpCount(Long.valueOf(irscOctFTEmpCountField.getText()));
			irsc.setOctTotEmpCount(Long.valueOf(irscOctTotEmpCountField.getText()));
			irsc.setOctAggGrp(irscOctAggGrpCheckBox.isSelected());

			irsc.setNovMECOffer(irscNovMECOfferCheckBox.isSelected());
			irsc.setNovFTEmpCount(Long.valueOf(irscNovFTEmpCountField.getText()));
			irsc.setNovTotEmpCount(Long.valueOf(irscNovTotEmpCountField.getText()));
			irsc.setNovAggGrp(irscNovAggGrpCheckBox.isSelected());

			irsc.setDecMECOffer(irscDecMECOfferCheckBox.isSelected());
			irsc.setDecFTEmpCount(Long.valueOf(irscDecFTEmpCountField.getText()));
			irsc.setDecTotEmpCount(Long.valueOf(irscDecTotEmpCountField.getText()));
			irsc.setDecAggGrp(irscDecAggGrpCheckBox.isSelected());
		}
	}

	private boolean validateData() {
		boolean bReturn = true;

		//add checks here
							
		return bReturn;
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.IRS1094C);
	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		update1094c();
		
		//save it to the repository and the server
		// will come back to this when the backend is in place
		//DataManager.i().saveCurrentIrs1094c();
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.IRS1094C, true);
	}	
}
