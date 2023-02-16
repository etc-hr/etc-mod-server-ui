package com.etc.admin.ui.pipeline.parsedateformat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.PipelineParseDateFormat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class ViewParseDateFormatEditController {
	
	@FXML
	private TextField prseNameLabel;
	@FXML
	private TextField prseDescriptionLabel;
	@FXML
	private TextField prseExampleSourceLabel;
	@FXML
	private TextField prsePatternLabel;
	@FXML
	private TextArea prseFormatInfo;
	@FXML
	private ComboBox<String> prseFormatCombo;
	@FXML
	private TextField prseTestResultField;
	@FXML
	private TextArea prseTestErrorArea;
	@FXML
	private Label prseCoreIdLabel;
	@FXML
	private Label prseCoreActiveLabel;
	@FXML
	private Label prseCoreBODateLabel;
	@FXML
	private Label prseCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */

	@FXML
	public void initialize() {
		initControls();
		
		updateDateFormatData();
	}

	private void initControls() {
		//load regex reference for the user
		setFormatInfo();
		
 	}	

	private void updateDateFormatData(){

		PipelineParseDateFormat format = DataManager.i().mPipelineParseDateFormat;
		if (format != null) {
			Utils.updateControl(prseNameLabel,format.getName());
			Utils.updateControl(prseDescriptionLabel,format.getDescription());
			Utils.updateControl(prseExampleSourceLabel,format.getExampleSource());
			Utils.updateControl(prsePatternLabel,format.getFormat());
				
			//core data read only
			Utils.updateControl(prseCoreIdLabel,String.valueOf(format.getId()));
			Utils.updateControl(prseCoreActiveLabel,String.valueOf(format.isActive()));
			Utils.updateControl(prseCoreBODateLabel,format.getBornOn());
			Utils.updateControl(prseCoreLastUpdatedLabel,format.getLastUpdated());	
		}
	}
	
	private void updateDateFormat(){

/*		PipelineParseDateFormat format = DataManager.i().mPipelineParseDateFormat;
		if (format != null) {
			DataManager.i().mUpdatePipelineParseDateFormatRequest = null;			
			DataManager.i().mUpdatePipelineParseDateFormatRequest = new UpdatePipelineParseDateFormatRequest();
			DataManager.i().mUpdatePipelineParseDateFormatRequest.setPipelineParseDateFormat(format);

			DataManager.i().mUpdatePipelineParseDateFormatRequest.setName(prseNameLabel.getText());
			DataManager.i().mUpdatePipelineParseDateFormatRequest.setDescription(prseDescriptionLabel.getText());
			DataManager.i().mUpdatePipelineParseDateFormatRequest.setExampleSource(prseDescriptionLabel.getText());
			DataManager.i().mUpdatePipelineParseDateFormatRequest.setFormat(prsePatternLabel.getText());
		}
*/	}

	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINEPARSEDATEFORMAT);
	}	
	
	private boolean validateData() {
		boolean bReturn = true;
		
		//check for missing data
		if ( !Utils.validate(prseNameLabel)) bReturn = false;
		if ( !Utils.validate(prseDescriptionLabel)) bReturn = false;
		if ( !Utils.validate(prsePatternLabel)) bReturn = false;
		if ( !Utils.validate(prseFormatInfo)) bReturn = false;
		
		// validate the format is valid
		if (bReturn == true) bReturn = testFormat();
		
		bReturn = false;
		return bReturn;
	}
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updateDateFormat();
		
		//save it to the repository and the server
		//FIXME: DataManager.i().saveCurrentPipelineParseDateFormatRequest();
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.PIPELINEPARSEDATEFORMAT, true);
	}		
	
	@FXML
	private void onTestFormat(ActionEvent event) {
		testFormat();
	}
	
	private boolean testFormat() {
		//clear any errors form the last run
		prseTestErrorArea.setText("");
		// make sure we have something to test
		if (prsePatternLabel.getText() == "" || prsePatternLabel.getText().isEmpty()) {
			prseTestErrorArea.setText("Please enter a date format to test.");
			return false;
		}
		
		try {
			Date tday = Calendar.getInstance().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat(prsePatternLabel.getText());
			prseTestResultField.setText(sdf.format(tday));
		}	catch (IllegalArgumentException | IllegalStateException ex) {
			prseTestErrorArea.setText(ex.getMessage());
			return false;
		}			
		
		return true;
	}
	
	private void setFormatInfo() {
		
		prseFormatInfo.setText("Date Format Specifiers\n\n"
		+ "y   = year (yy or yyyy)\n"
		+ "M   = month (MM)\n"
		+ "d   = day in month (dd)\n"
		+ "h   = hour (0-12) (hh)\n"
		+ "H   = hour (0-23) (HH)\n"
		+ "m   = minute in hour (mm)\n"
		+ "s   = seconds (ss)\n"
		+ "S   = milliseconds (SSS)\n"
		+ "z   = time zone text (Pacific Standard Time...)\n"
		+ "Z   = time zone, time offset (-0800)\n"
		+ "\nEXAMPLES\n\n"
		+ "yyyy-MM-dd                 (2018-12-31)\n" 
		+ "dd-MM-YYYY                 (31-12-2018)\n"
		+ "yyyy-MM-dd HH:mm:ss        (2018-12-31 23:59:59)\n"
		+ "HH:mm:ss.SSS               (23:59.59.999)\n"
		+ "yyyy-MM-dd HH:mm:ss.SSS    (2018-12-31 23:59:59.999)\n"
		+ "yyyy-MM-dd HH:mm:ss.SSS Z  (2018-12-31 23:59:59.999 +0100)");
	}
	
}


