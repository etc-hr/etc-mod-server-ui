package com.etc.admin.ui.pipeline.raw;

import java.util.Calendar;
import java.util.List;

import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.RawConversionFailure;
import com.etc.corvetto.ems.pipeline.entities.RawCoverage;
import com.etc.corvetto.ems.pipeline.entities.RawDependent;
import com.etc.corvetto.ems.pipeline.entities.RawDependentCoverage;
import com.etc.corvetto.ems.pipeline.entities.RawEmployee;
import com.etc.corvetto.ems.pipeline.entities.RawInvalidation;
import com.etc.corvetto.ems.pipeline.entities.RawNotice;
import com.etc.corvetto.ems.pipeline.entities.RawPay;
import com.etc.embeds.SSN;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class HBoxRawDetailCell extends HBox{
	Label[] lblDetail = new Label[80];
	RawPay rawPay = null;
	RawEmployee rawEmp = null;
	RawDependent rawDep = null;
	RawCoverage rawCov = null;
	RawDependentCoverage rawDepCov = null;
	
	RawMapperDisplay mapperDisplay = null;
	
	HBoxRawDetailCell()
	{
		Label lblBlank = new Label();
		lblBlank.setText("");
		this.getChildren().add(lblBlank);
	}
	
	// Raw Pay constructor
	HBoxRawDetailCell(RawPay rawPay, RawMapperDisplay mapperDisplay) {
		super();
        this.rawPay = rawPay;
        this.mapperDisplay = mapperDisplay;
       
		getLabel(0,0,400,10,getInvalidations(rawPay.getInvalidations(), rawPay.getConversionFailures()));
		getLabel(28,1,100,10,rawPay.getPayPeriodStart());
		getLabel(29,2,100,10,rawPay.getPayPeriodEnd());
		getLabel(30,3,100,10,rawPay.getPayDate());
		getLabel(31,4,100,10,rawPay.getWorkDate());
		getLabel(0,5,50,10,rawPay.getPpdErReference());
		getLabel(33,6,75,10,rawPay.getHours());
		getLabel(34,7,75,10,rawPay.getRate());
		getLabel(35,8,75,10,rawPay.getAmount());
		getLabel(36,9,75,10,""); //rawPay.getPayCode());
		getLabel(38,10,75,10,rawPay.getOvertimeHours1());
		getLabel(39,11,75,10,rawPay.getOvertimeHours2());
		getLabel(40,12,75,10,rawPay.getOvertimeHours3());
		getLabel(42,13,75,10,rawPay.getSickHours());
		getLabel(41,14,75,10,rawPay.getPtoHours());
		getLabel(43,15,75,10,rawPay.getHolidayHours());
		getLabel(49,16,50,10,rawPay.getCustomField1());
		getLabel(50,17,50,10,rawPay.getCustomField2());
		getLabel(51,18,50,10,rawPay.getCustomField3());
		getLabel(52,19,50,10,rawPay.getCustomField4());
		getLabel(53,20,50,10,rawPay.getCustomField5());
		getLabel(54,21,50,10,rawPay.getCustomField6());
		getLabel(55,22,50,10,rawPay.getCustomField7());
		getLabel(0,23,75,10,rawPay.getId());
		getLabel(0,24,75,10,rawPay.isActive());
		getLabel(0,25,100,10,rawPay.getBornOn());
		getLabel(0,26,100,10,rawPay.getLastUpdated());
		getLabel(0,27,75,10,""); //rawPay.getRawPayEmployeeId());
		// the error
		getLabel(0,28,100,10,getNotices(rawPay.getNotices()));
		// offset for  dependent element
		Label space = new Label();
		space.setText("Employee Pay: -----");
		this.getChildren().addAll(space);
    	for (int i = 0; i < 27; i++) 
    		this.getChildren().addAll(lblDetail[i]);
     }
	
	// RawEmployee Constructor
	HBoxRawDetailCell(RawEmployee rawEmp, RawMapperDisplay mapperDisplay) {
		super();
        this.rawEmp = rawEmp;
        this.mapperDisplay = mapperDisplay;
       
		getLabel(0,0,400,10,getInvalidations(rawEmp.getInvalidations(), rawEmp.getConversionFailures()));
		getLabel(4,1,150,10,rawEmp.getEmployerIdentifier());
		getLabel(3,2,150,10,rawEmp.getSsn());
		getLabel(0,3,150,10,rawEmp.getFullName());
		getLabel(5,4,150,10,rawEmp.getFirstName());
		getLabel(6,5,150,10,rawEmp.getMiddleName());
		getLabel(7,6,150,10,rawEmp.getLastName());
		getLabel(0,7,150,10,""); //rawEmp.getCity() getFullAddress());
		getLabel(19,8,150,10,rawEmp.getStreetNumber());
		getLabel(20,9,150,10,rawEmp.getStreet());
		getLabel(21,10,150,10,rawEmp.getStreetExtension());
		getLabel(22,11,150,10,rawEmp.getCity());
		getLabel(23,12,150,10,rawEmp.getState());
		getLabel(24,13,150,10,rawEmp.getZip());
		getLabel(0,14,150,10,""); //rawEmp.getGenderIdentifier());
		if (rawEmp.getGenderType() != null)
			getLabel(25,15,150,10,rawEmp.getGenderType().toString());
		else 
			getLabel(25,15,150,10,"");
		getLabel(26,16,150,10,rawEmp.getDateOfBirth());
		getLabel(27,17,150,10,rawEmp.getHireDate());
		getLabel(28,18,150,10,rawEmp.getRehireDate());
		getLabel(29,19,150,10,rawEmp.getTermDate());
		getLabel(10,20,150,10,"");//rawEmp.getDepartmentIdentifier());
		getLabel(12,21,150,10,rawEmp.getJobTitle());
		getLabel(14,22,150,10,rawEmp.getCustomField1());
		getLabel(15,23,150,10,rawEmp.getCustomField2());
		getLabel(16,24,150,10,rawEmp.getCustomField3());
		getLabel(17,25,150,10,rawEmp.getCustomField4());
		getLabel(18,26,150,10,rawEmp.getCustomField5());
		getLabel(0,27,150,10,rawEmp.getId());
		getLabel(0,28,150,10,rawEmp.isActive());
		getLabel(0,29,150,10,rawEmp.getBornOn());
		getLabel(0,30,150,10,rawEmp.getLastUpdated());
		// offset for  dependent element
		Label space = new Label();
		space.setText("Employee: ");
		this.getChildren().addAll(space);
    	for (int i = 0; i < 31; i++) 
    		this.getChildren().addAll(lblDetail[i]);
     }
	
	// RawDependent Constructor
	HBoxRawDetailCell(RawDependent rawEmpSec, RawMapperDisplay mapperDisplay) {
		super();
        this.rawDep = rawEmpSec;
        this.mapperDisplay = mapperDisplay;
       
		getLabel(0,0,400,10,getInvalidations(rawEmpSec.getInvalidations(), rawEmpSec.getConversionFailures()));
		getLabel(0,1,150,10,rawEmpSec.getId());
		getLabel(0,2,150,10,rawEmpSec.getSsn());
		getLabel(0,3,150,10,rawEmpSec.getFullName());
		getLabel(0,4,150,10,rawEmpSec.getFirstName());
		getLabel(0,5,150,10,rawEmpSec.getMiddleName());
		getLabel(0,6,150,10,rawEmpSec.getLastName());
		getLabel(0,7,150,10,rawEmpSec.getFullAddress());
		getLabel(0,8,150,10,rawEmpSec.getStreetNumber());
		getLabel(0,9,150,10,rawEmpSec.getStreet());
		getLabel(0,10,150,10,rawEmpSec.getStreetExtension());
		getLabel(0,11,150,10,rawEmpSec.getCity());
		getLabel(0,12,150,10,rawEmpSec.getState());
		getLabel(0,13,150,10,rawEmpSec.getZip());
		getLabel(0,14,150,10,rawEmpSec.getDateOfBirth());
		getLabel(0,15,150,10,rawEmpSec.getCustomField1());
		getLabel(0,16,150,10,rawEmpSec.getCustomField2());
		getLabel(0,17,150,10,rawEmpSec.getCustomField3());
		getLabel(0,18,150,10,rawEmpSec.getCustomField4());
		getLabel(0,19,150,10,rawEmpSec.getCustomField5());
		getLabel(0,20,150,10,rawEmpSec.getId());
		getLabel(0,21,150,10,rawEmpSec.isActive());
		getLabel(0,22,150,10,rawEmpSec.getBornOn());
		getLabel(0,23,150,10,rawEmpSec.getLastUpdated());
		// the errors
		getLabel(0,24,150,10,getNotices(rawEmpSec.getNotices()));
		
		// offset for  dependent element
		Label space = new Label();
		space.setText("Secondary: -----");
		this.getChildren().addAll(space);
   	for (int i = 0; i < 25; i++) 
    		this.getChildren().addAll(lblDetail[i]);
     }
	
	// RawEmployeeCoverage Constructor
	HBoxRawDetailCell(RawCoverage rawEmpCov, RawMapperDisplay mapperDisplay) {
		super();
        this.rawCov = rawEmpCov;
        this.mapperDisplay = mapperDisplay;
       
		getLabel(0,0,400,10,getInvalidations(rawEmpCov.getInvalidations(),rawEmpCov.getConversionFailures()));
		getLabel(5,1,150,10,rawEmpCov.getSubscriberNumber());
		getLabel(12,2,150,10,rawEmpCov.isWaived());
		getLabel(13,3,150,10,rawEmpCov.isIneligible());
		getLabel(14,4,150,10,rawEmpCov.getTaxYear());
		getLabel(15,5,150,10,rawEmpCov.getStartDate());
		getLabel(16,6,150,10,rawEmpCov.getEndDate());
		getLabel(17,7,150,10,rawEmpCov.isTaxYearSelected());
		getLabel(18,8,150,10,rawEmpCov.isJanuarySelected());
		getLabel(19,9,150,10,rawEmpCov.isFebruarySelected());
		getLabel(20,10,150,10,rawEmpCov.isMarchSelected());
		getLabel(21,11,150,10,rawEmpCov.isAprilSelected());
		getLabel(22,12,150,10,rawEmpCov.isMaySelected());
		getLabel(23,13,150,10,rawEmpCov.isJuneSelected());
		getLabel(24,14,150,10,rawEmpCov.isJulySelected());
		getLabel(25,15,150,10,rawEmpCov.isAugustSelected());
		getLabel(25,16,150,10,rawEmpCov.isSeptemberSelected());
		getLabel(27,17,150,10,rawEmpCov.isOctoberSelected());
		getLabel(28,18,150,10,rawEmpCov.isNovemberSelected());
		getLabel(29,19,150,10,rawEmpCov.isDecemberSelected());
		getLabel(30,20,150,10,rawEmpCov.getPlanReference());
		getLabel(31,21,150,10,rawEmpCov.getDeduction());
		getLabel(32,22,150,10,rawEmpCov.getMemberShare());
		getLabel(66,23,150,10,rawEmpCov.getCustomField1());
		getLabel(67,24,150,10,rawEmpCov.getCustomField2());
		getLabel(0,25,150,10,rawEmpCov.getId());
		getLabel(0,26,150,10,rawEmpCov.isActive());
		getLabel(0,27,150,10,rawEmpCov.getBornOn());
		getLabel(0,28,150,10,rawEmpCov.getLastUpdated());
		getLabel(0,29,150,10,""); //rawEmpCov.getRawCoverageEmployeeId());
		// the errors
		getLabel(0,30,150,10,getNotices(rawEmpCov.getNotices()));
		
		// offset for  dependent element
		Label space = new Label();
		space.setText("Employee Coverage: -----");
		this.getChildren().addAll(space);
       	for (int i = 0; i < 25; i++) 
        		this.getChildren().addAll(lblDetail[i]);
     }
	
	// RawDependentCoverage Constructor
	HBoxRawDetailCell(RawDependentCoverage rawSecCov, RawMapperDisplay mapperDisplay) {
		super();
        this.rawDepCov = rawSecCov;
        this.mapperDisplay = mapperDisplay;
       
		getLabel(0,0,400,10,getInvalidations(rawSecCov.getInvalidations(),rawSecCov.getConversionFailures()));
		getLabel(0,1,150,10,rawSecCov.getMemberNumber());
		getLabel(0,2,150,10,rawSecCov.isWaived());
		getLabel(0,3,150,10,rawSecCov.isIneligible());
		getLabel(0,4,150,10,rawSecCov.getTaxYear());
		getLabel(0,5,150,10,rawSecCov.getStartDate());
		getLabel(0,6,150,10,rawSecCov.getEndDate());
		getLabel(0,7,150,10,rawSecCov.isTaxYearSelected());
		getLabel(0,8,150,10,rawSecCov.isJanuarySelected());
		getLabel(0,9,150,10,rawSecCov.isFebruarySelected());
		getLabel(0,10,150,10,rawSecCov.isMarchSelected());
		getLabel(0,11,150,10,rawSecCov.isAprilSelected());
		getLabel(0,12,150,10,rawSecCov.isMaySelected());
		getLabel(0,13,150,10,rawSecCov.isJuneSelected());
		getLabel(0,14,150,10,rawSecCov.isJulySelected());
		getLabel(0,15,150,10,rawSecCov.isAugustSelected());
		getLabel(0,16,150,10,rawSecCov.isSeptemberSelected());
		getLabel(0,17,150,10,rawSecCov.isOctoberSelected());
		getLabel(0,18,150,10,rawSecCov.isNovemberSelected());
		getLabel(0,19,150,10,rawSecCov.isDecemberSelected());
		getLabel(0,20,150,10,rawSecCov.getPlanReference());
		getLabel(0,21,150,10,rawSecCov.getCustomField1());
		getLabel(0,22,150,10,rawSecCov.getCustomField2());	
		getLabel(0,23,150,10,rawSecCov.getId());
		getLabel(0,24,150,10,rawSecCov.isActive());
		getLabel(0,25,150,10,rawSecCov.getBornOn());
		getLabel(0,26,150,10,rawSecCov.getLastUpdated());
		getLabel(0,27,150,10,""); //rawSecCov.getRawCoverageSecondaryId());
		// the errors
		getLabel(0,28,150,10,getNotices(rawSecCov.getNotices()));
 		
		// offset for  dependent element
		Label space = new Label();
		space.setText("Coverage Dependent Coverage: ----------");
		this.getChildren().addAll(space);
       	for (int i = 0; i < 25; i++) 
        		this.getChildren().addAll(lblDetail[i]);
     } 
	
	// String Label
	private void getLabel(int color, int labelIdx, int maxWidth, int minWidth, String data) {
		lblDetail[labelIdx] = new Label();
		lblDetail[labelIdx].setText(data);
		if (labelIdx == 0)
			lblDetail[labelIdx].getStyleClass().add("Invalidation");
		else
			if (color > 0) lblDetail[labelIdx].getStyleClass().add("BackColor" + mapperDisplay.getColor(color));
		setLabelSize(labelIdx, minWidth, maxWidth);
	}
	
	// Long Label
	private void getLabel(int color, int labelIdx, int maxWidth, int minWidth,  Long data) {
		lblDetail[labelIdx] = new Label();
		if (data != null && data > 0l)
			lblDetail[labelIdx].setText(String.valueOf(data));
		if (color > 0) lblDetail[labelIdx].getStyleClass().add("BackColor" +  mapperDisplay.getColor(color));
		setLabelSize(labelIdx, minWidth, maxWidth);
	}
	
	// Int Label
	private void getLabel(int color, int labelIdx, int maxWidth, int minWidth, int data) {
		lblDetail[labelIdx] = new Label();
		lblDetail[labelIdx].setText(String.valueOf(data));
		if (color > 0) lblDetail[labelIdx].getStyleClass().add("BackColor" +  mapperDisplay.getColor(color));
		setLabelSize(labelIdx, minWidth, maxWidth);
	}
	
	// Float Label
	private void getLabel(int color, int labelIdx, int maxWidth, int minWidth, Float data) {
		if (data == null) data = 0f;
		lblDetail[labelIdx] = new Label();
		double dData = Math.round(data * 100.0)/100.0;
		lblDetail[labelIdx].setText(String.valueOf(dData));
		if (color > 0) lblDetail[labelIdx].getStyleClass().add("BackColor" +  mapperDisplay.getColor(color));
		setLabelSize(labelIdx, minWidth, maxWidth);
	}
	
	// Boolean Label
	private void getLabel(int color, int labelIdx, int maxWidth, int minWidth, Boolean data) {
		lblDetail[labelIdx] = new Label();
		lblDetail[labelIdx].setText(String.valueOf(data));
		if (color > 0) lblDetail[labelIdx].getStyleClass().add("BackColor" +  mapperDisplay.getColor(color));
		setLabelSize(labelIdx, minWidth, maxWidth);
	}
	
	// Calendar Label
	private void getLabel(int color, int labelIdx, int maxWidth, int minWidth, Calendar data) {
		lblDetail[labelIdx] = new Label();
		if (data != null)
			lblDetail[labelIdx].setText(Utils.getDateString(data));
		if (color > 0) lblDetail[labelIdx].getStyleClass().add("BackColor" +  mapperDisplay.getColor(color));
		setLabelSize(labelIdx, minWidth, maxWidth);
	}
	
	// SSN Label
	private void getLabel(int color, int labelIdx, int maxWidth, int minWidth, SSN data) {
		lblDetail[labelIdx] = new Label();
		if (data != null && data.getUsrln() != null)
			lblDetail[labelIdx].setText(String.valueOf(data.getUsrln()));
		if (color > 0) lblDetail[labelIdx].getStyleClass().add("BackColor" +  mapperDisplay.getColor(color));
		setLabelSize(labelIdx, minWidth, maxWidth);
	}
	
	private void setLabelSize(int labelIdx, int minWidth, int maxWidth) {
		if (labelIdx != 0 && (lblDetail[labelIdx].getText() == "" || lblDetail[labelIdx].getText() == null)) {
	//		lblDetail[labelIdx].setMinWidth(minWidth);
	//		lblDetail[labelIdx].setPrefWidth(minWidth);
	//		lblDetail[labelIdx].setMaxWidth(minWidth);
		}
		else {
			lblDetail[labelIdx].setMinWidth(maxWidth);
			lblDetail[labelIdx].setPrefWidth(maxWidth);
			lblDetail[labelIdx].setMaxWidth(maxWidth);
		}
	}
	
	private static String getInvalidations(List<RawInvalidation> invalidations, List<RawConversionFailure> failures) {
		String invalidationString = "";
		
		if (invalidations != null) {
			for (RawInvalidation inv : invalidations) {
				if (invalidationString.length() < 1)
					invalidationString = inv.getMessage();
				else
					invalidationString = invalidationString + "\n" + inv.getMessage();
			}
		}
		
		if (failures != null) {
			if (invalidationString.length() > 0 && failures.size() > 0) 
				invalidationString = invalidationString + "\n";
			for (RawConversionFailure fail: failures) {
				if (invalidationString.length() < 1)
					invalidationString = "Conv. Failure: " + fail.getMessage();
				else
					invalidationString = invalidationString + "\n" + "Conv. Failure: " + fail.getMessage();
			}
		}

		return invalidationString;
	}
	
	private static String getNotices(List<RawNotice> notices) {
		String noticeString = "";

		if (notices!= null) {
			for (RawNotice notice : notices) {
				if (noticeString.length() < 1)
					noticeString = notice.getMessage();
				else
					noticeString = noticeString + "\n" + notice.getMessage();
			}
		}

		return noticeString;
	}
}
