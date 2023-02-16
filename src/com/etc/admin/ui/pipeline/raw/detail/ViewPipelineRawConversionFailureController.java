package com.etc.admin.ui.pipeline.raw.detail;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.RawConversionFailure;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ViewPipelineRawConversionFailureController {
	@FXML
	private TextArea rwconMessageTextArea;
	@FXML
	private Label rwconCoreIdLabel;
	@FXML
	private Label rwconCoreActiveLabel;
	@FXML
	private Label rwconCoreBODateLabel;
	@FXML
	private Label rwconCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		updateConversionFailureData();
	}
	
	private void updateConversionFailureData(){		
		RawConversionFailure failure = DataManager.i().mRawConversionFailure;
		if (failure == null) return;
		
		Utils.updateControl(rwconMessageTextArea,failure.getMessage());
		// CORE DATA
		Utils.updateControl(rwconCoreIdLabel,failure.getId());
		Utils.updateControl(rwconCoreActiveLabel,failure.isActive());
		Utils.updateControl(rwconCoreBODateLabel,failure.getBornOn());
		Utils.updateControl(rwconCoreLastUpdatedLabel,failure.getLastUpdated());		
	}
	
	private void setRawDetailBackScreen() {
		switch (DataManager.i().mRawDetailType) {
		case EMPLOYEEFILE:
			EtcAdmin.i().setScreen(ScreenType.PIPELINEEMPLOYEEFILE);
			break;
		case EMPLOYEESECONDARY:
			EtcAdmin.i().setScreen(ScreenType.RAWEMPLOYEESECONDARY);
			break;
		case COVERAGEFILE:
			EtcAdmin.i().setScreen(ScreenType.PIPELINECOVERAGEFILE);
			break;
		case COVERAGESECONDARY:
			EtcAdmin.i().setScreen(ScreenType.RAWCOVERAGESECONDARY);
			break;
		case PAYFILE:
			EtcAdmin.i().setScreen(ScreenType.PIPELINEPAYFILE);
			break;
		}
	}
	
	@FXML
	private void onBack(ActionEvent event) {
		setRawDetailBackScreen();
	}	
}
