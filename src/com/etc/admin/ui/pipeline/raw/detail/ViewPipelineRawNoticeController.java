package com.etc.admin.ui.pipeline.raw.detail;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.RawNotice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ViewPipelineRawNoticeController {
	@FXML
	private TextArea rwnotMessageTextArea;
	@FXML
	private Label rwnotCoreIdLabel;
	@FXML
	private Label rwnotCoreActiveLabel;
	@FXML
	private Label rwnotCoreBODateLabel;
	@FXML
	private Label rwnotCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		updateNoticeData();
	}
	
	private void updateNoticeData(){		
		RawNotice notice = DataManager.i().mRawNotice;
		if (notice == null) return;
		
		Utils.updateControl(rwnotMessageTextArea,notice.getMessage());
		// CORE DATA
		Utils.updateControl(rwnotCoreIdLabel,notice.getId());
		Utils.updateControl(rwnotCoreActiveLabel,notice.isActive());
		Utils.updateControl(rwnotCoreBODateLabel,notice.getBornOn());
		Utils.updateControl(rwnotCoreLastUpdatedLabel,notice.getLastUpdated());	
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
