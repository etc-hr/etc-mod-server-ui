package com.etc.admin.ui.pipeline.raw.detail;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.RawInvalidation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ViewPipelineRawInvalidationController {
	@FXML
	private TextArea rwinvMessageTextArea;
	@FXML
	private Label rwinvCoreIdLabel;
	@FXML
	private Label rwinvCoreActiveLabel;
	@FXML
	private Label rwinvCoreBODateLabel;
	@FXML
	private Label rwinvCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		updateInvalidationData();
	}
	
	private void updateInvalidationData(){		
		RawInvalidation invalidation = DataManager.i().mRawInvalidation;
		if (invalidation == null) return;
		
		Utils.updateControl(rwinvMessageTextArea,invalidation.getMessage());
		// CORE DATA
		Utils.updateControl(rwinvCoreIdLabel,invalidation.getId());
		Utils.updateControl(rwinvCoreActiveLabel,invalidation.isActive());
		Utils.updateControl(rwinvCoreBODateLabel,invalidation.getBornOn());
		Utils.updateControl(rwinvCoreLastUpdatedLabel,invalidation.getLastUpdated());
		
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
