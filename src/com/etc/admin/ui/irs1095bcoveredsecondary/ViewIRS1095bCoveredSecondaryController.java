package com.etc.admin.ui.irs1095bcoveredsecondary;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.Irs1095b;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class ViewIRS1095bCoveredSecondaryController {
	
	@FXML
	private Label irsbTitleLabel;
	@FXML
	private CheckBox irsbAnnCoveredCheckBox;
	@FXML
	private CheckBox irsbJanCoveredCheckBox;
	@FXML
	private CheckBox irsbFebCoveredCheckBox;
	@FXML
	private CheckBox irsbMarCoveredCheckBox;
	@FXML
	private CheckBox irsbAprCoveredCheckBox;
	@FXML
	private CheckBox irsbMayCoveredCheckBox;
	@FXML
	private CheckBox irsbJunCoveredCheckBox;
	@FXML
	private CheckBox irsbJulCoveredCheckBox;
	@FXML
	private CheckBox irsbAugCoveredCheckBox;
	@FXML
	private CheckBox irsbSepCoveredCheckBox;
	@FXML
	private CheckBox irsbOctCoveredCheckBox;
	@FXML
	private CheckBox irsbNovCoveredCheckBox;
	@FXML
	private CheckBox irsbDecCoveredCheckBox;
	@FXML
	private Button irsbEditButton;
	@FXML
	private Label irsbCoreIdLabel;
	@FXML
	private Label irsbCoreActiveLabel;
	@FXML
	private Label irsbCoreBODateLabel;
	@FXML
	private Label irsbCoreLastUpdatedLabel;
	
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
		irsbEditButton.setDisable(!Utils.userCanEdit());
	}

	private void update1095bCoveredSecondaryData(){
		
		Irs1095b irsb = DataManager.i().mIrs1095b;
		if (irsb != null) {
			String sName = "";
			if (irsb.getEmployee()!= null) {
				sName = irsb.getEmployee().getPerson().getFirstName();
				sName.concat(" ");
				sName.concat(irsb.getEmployee().getPerson().getLastName());
				sName.concat(" 1095b");
			}

			Utils.updateControl(irsbTitleLabel,sName);	
			Utils.updateControl(irsbJanCoveredCheckBox,irsb.isJanCovered());	
			Utils.updateControl(irsbFebCoveredCheckBox,irsb.isFebCovered());	
			Utils.updateControl(irsbMarCoveredCheckBox,irsb.isMarCovered());	
			Utils.updateControl(irsbAprCoveredCheckBox,irsb.isAprCovered());	
			Utils.updateControl(irsbMayCoveredCheckBox,irsb.isMayCovered());	
			Utils.updateControl(irsbJunCoveredCheckBox,irsb.isJunCovered());	
			Utils.updateControl(irsbJulCoveredCheckBox,irsb.isJulCovered());	
			Utils.updateControl(irsbAugCoveredCheckBox,irsb.isAugCovered());	
			Utils.updateControl(irsbSepCoveredCheckBox,irsb.isSepCovered());	
			Utils.updateControl(irsbOctCoveredCheckBox,irsb.isOctCovered());	
			Utils.updateControl(irsbNovCoveredCheckBox,irsb.isNovCovered());	
			Utils.updateControl(irsbDecCoveredCheckBox,irsb.isDecCovered());	

			// core data
			Utils.updateControl(irsbCoreIdLabel,String.valueOf(DataManager.i().mIrs1094b.getId()));
			Utils.updateControl(irsbCoreActiveLabel,String.valueOf(DataManager.i().mIrs1094b.isActive()));
			Utils.updateControl(irsbCoreBODateLabel,DataManager.i().mIrs1094b.getBornOn());
			Utils.updateControl(irsbCoreLastUpdatedLabel,DataManager.i().mIrs1094b.getLastUpdated());
		}
	}
	
	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.IRS1095BCOVEREDSECONDARYEDIT, true);
	}	
	
	//this may not be used
	@FXML
	private void onAdd1095b(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.IRS1095BCOVEREDSECONDARYADD, true);
	}		
}
