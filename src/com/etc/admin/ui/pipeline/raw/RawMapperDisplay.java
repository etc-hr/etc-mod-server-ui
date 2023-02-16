package com.etc.admin.ui.pipeline.raw;

import java.util.Arrays;

import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

import com.etc.admin.data.DataManager;
import com.etc.admin.ui.pipeline.queue.PipelineQueue;
import com.etc.corvetto.ems.pipeline.entities.PipelineParseDateFormat;
import com.etc.corvetto.ems.pipeline.entities.PipelineParsePattern;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileSpecification;

public class RawMapperDisplay 
{

	SpreadsheetView mapperSPV = null;
	String[] columnFields = new String[100];
	public int[] columnCount = new int[100];
	public int[] columnColors = new int[100];
	public int[] columnRef = new int[100];
	boolean[] columnOn = new boolean[100];

	int mapperColIndex = 0;
	int mapperMaxEntries = 0;
	int mapperLastPosition = 0;
	int mapperColorIndex = 0;
	public int[] mapperColumns = new int[100];

	RawFieldData rawData = null;
	
	public void setSpreadsheetView (SpreadsheetView view)
	{
		mapperSPV = view;
	}
	
	public int getColor(int color) {
		return columnColors[columnRef[color]];
	}
	
	public void updateMapperDisplay(PipelineQueue queue) 
	{
		Arrays.fill(columnFields,"");
		Arrays.fill(columnCount, 0);
		Arrays.fill(columnColors, 0);
		Arrays.fill(columnRef, 0);
		Arrays.fill(columnOn, false);
	
		Arrays.fill(mapperColumns, -1);
		//reset our counters
		mapperColIndex = 0;
		mapperMaxEntries = 0;
		mapperLastPosition = 0;
		mapperColorIndex = 0;
		
		PipelineSpecification spec = null;
		
		//load the filespec
		if((spec = queue.getRequest().getSpecification()) == null) return;

		switch(queue.getFileType())
		{
			case EMPLOYEE:
				if(queue.getEmployeeFile() == null) return;
	    		DataManager.i().loadDynamicEmployeeFileSpecification(spec.getDynamicFileSpecificationId());
				loadFileSpec(DataManager.i().mDynamicEmployeeFileSpecification);
				DataManager.i().mPipelineSpecification = spec;
				DataManager.i().mPipelineChannel = spec.getChannel();
				break;
			case PAY:
				if(queue.getPayFile() == null) return;
				if((spec = queue.getPayFile().getSpecification()) == null) return;
	    		DataManager.i().loadDynamicPayFileSpecification(spec.getDynamicFileSpecificationId());
				loadFileSpec(DataManager.i().mDynamicPayFileSpecification);
				DataManager.i().mPipelineSpecification = spec;
				DataManager.i().mPipelineChannel = spec.getChannel();
				break;
			case COVERAGE:
				if(queue.getCoverageFile() == null) return;
	    		DataManager.i().loadDynamicCoverageFileSpecification(spec.getDynamicFileSpecificationId());
				loadFileSpec(DataManager.i().mDynamicCoverageFileSpecification);
				DataManager.i().mPipelineSpecification = spec;
				DataManager.i().mPipelineChannel = spec.getChannel();
				break;
			case IRS1094C:
				if(queue.getIrs1094cFile() == null) return;
				loadIrs1094cFileSpec();
				DataManager.i().mPipelineSpecification = spec;
				DataManager.i().mPipelineChannel = spec.getChannel();
				break;
			default:
			// no file spec
			return;
		}
	}
	
	private void CheckColumn(int colId, String colName, boolean colSelected, int colIndex, PipelineParsePattern parsePattern, PipelineParseDateFormat dateFormat)
	{
		if(colSelected == true)
		{
			mapperColIndex = colIndex;
			//check for garbage
			if(mapperColIndex > 99) mapperColIndex = 99;
			if(mapperLastPosition < mapperColIndex) mapperLastPosition = mapperColIndex;
			//manually count entries since the spreadsheet cell size has an update bug
			columnCount[mapperColIndex]++;
			if(columnCount[mapperColIndex] > mapperMaxEntries) mapperMaxEntries = columnCount[mapperColIndex];
			// append as required
			if(columnFields[mapperColIndex].length() > 0)
			{
				String newVal = columnFields[colIndex] + "\r" + colName;
				columnFields[mapperColIndex] = newVal;
			}
			else
				columnFields[mapperColIndex] = colName;
			mapperColumns[colId] = mapperColIndex;
			if(parsePattern != null) columnFields[mapperColIndex]  += (" (" + parsePattern.getDescription() + ")");
			if(dateFormat != null) 
			{
				// this could have both a pattern and a format
				if(parsePattern != null) columnFields[mapperColIndex]  += ", ";
				columnFields[mapperColIndex]  += (" (" + dateFormat.getName() + ")");
			}
			//bump it one to make it 1 based;
			colIndex++;
			// over 30 - rarely
			if(colIndex > 30) colIndex = colIndex - 30;
			// over 60 - can't see it happening
			if(colIndex > 30) colIndex = colIndex - 30;
			columnColors[mapperColIndex] = colIndex;
			// save our column selection
			columnOn[colId] = colSelected;
			// and a cross reference from id to position
			columnRef[colId] = mapperColIndex;
		}
	}
	
	public int getMapperColumn(int recordColumn)
	{
		//return the mapped column if available, otherwise a 0
		return mapperColumns[recordColumn];
	}
	
	public int getMappedField(int tableColumn)
	{
		//return the mapped column if available, otherwise a -1
		for (int i = 0; i<100; i++) {
			if (mapperColumns[i] == tableColumn)
				return i;
		}
		return -1;
	}
	
	public SpreadsheetCell getMapperCell(int colIndex, int row, int column, String data)
	{
		if(data == null) data = "";
		if(data.length() < 1) data = "          ";
		SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, column + 1, 1, 1, data);
		newCell.setEditable(false);
		if(data.trim().length() > 0 )
		{
			newCell.getStyleClass().add("BackColor" + columnColors[column + 1]);
		}
		return newCell;
	}

	private void loadFileSpec(DynamicEmployeeFileSpecification fSpec)
	{
		if(fSpec == null) return;
		CheckColumn(1,"EMPLOYEE IDENTIFIER", fSpec.isErRefCol(), fSpec.getErRefColIndex(), fSpec.getErRefParsePattern(), null);
		CheckColumn(2,"LITE ID",fSpec.isEtcRefCol(), fSpec.getEtcRefColIndex(), null, null);
		CheckColumn(3,"EMPLOYEE SSN", fSpec.isSsnCol(), fSpec.getSsnColIndex(), fSpec.getSsnParsePattern(), null);
		CheckColumn(4,"EMPLOYER IDENTIFIER",fSpec.isErCol(), fSpec.getErColIndex(), null, null);
		CheckColumn(5,"PHONE NUMBER",fSpec.isPhoneCol(), fSpec.getPhoneColIndex(), null, null);
		CheckColumn(6,"EMAIL COLUMN",fSpec.isEmlCol(), fSpec.getEmlColIndex(), null, null);
		CheckColumn(7,"FIRST NAME", fSpec.isfNameCol(), fSpec.getfNameColIndex(), fSpec.getfNameParsePattern(), null);
		CheckColumn(8,"MIDDLE NAME", fSpec.ismNameCol(), fSpec.getmNameColIndex(), fSpec.getmNameParsePattern(), null);
		CheckColumn(9,"LAST NAME", fSpec.islNameCol(), fSpec.getlNameColIndex(), fSpec.getlNameParsePattern(), null);
		CheckColumn(10,"REHIRE DATE", fSpec.isRhireDtCol(), fSpec.getRhireDtColIndex(), null, fSpec.getRhireDtFormat());

		CheckColumn(11,"DEPARTMENT", fSpec.isDeptCol(), fSpec.getDeptColIndex(), null, null);
		CheckColumn(12,"STREET NUMBER", fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), fSpec.getStreetParsePattern(), null);
		CheckColumn(13,"ADDRESS LINE 1", fSpec.isStreetCol(), fSpec.getStreetColIndex(), fSpec.getStreetParsePattern(), null);
		CheckColumn(14,"ADDRESS LINE 2", fSpec.isLin2Col(), fSpec.getLin2ColIndex(), null, null);
		CheckColumn(15,"CITY", fSpec.isCityCol(), fSpec.getCityColIndex(), fSpec.getCityParsePattern(), null);
		CheckColumn(16,"STATE", fSpec.isStateCol(), fSpec.getStateColIndex(), fSpec.getStateParsePattern(), null);
		CheckColumn(17,"ZIP", fSpec.isZipCol(), fSpec.getZipColIndex(), fSpec.getZipParsePattern(), null);
		CheckColumn(18,"GENDER", fSpec.isGenderCol(), fSpec.getGenderColIndex(), null, null);
		CheckColumn(19,"DATE OF BIRTH", fSpec.isDobCol(), fSpec.getDobColIndex(), null, fSpec.getDobFormat());
		CheckColumn(20,"HIRE DATE", fSpec.isHireDtCol(), fSpec.getHireDtColIndex(), null, fSpec.getHireDtFormat());

		CheckColumn(21,"TERMINATION DATE", fSpec.isTermDtCol(), fSpec.getTermDtColIndex(), null, fSpec.getTermDtFormat());
		CheckColumn(22,"LOCATION", fSpec.isLocCol(), fSpec.getLocColIndex(), null, null);
		CheckColumn(23,"JOB TITLE", fSpec.isJobTtlCol(), fSpec.getJobTtlColIndex(), null, null);
		CheckColumn(24,"PAYCODE", fSpec.isPayCodeCol(), fSpec.getPayCodeColIndex(), null, null);
		CheckColumn(25,"UNION TYPE", fSpec.isUnionTypeCol(), fSpec.getUnionTypeColIndex(), null, null);
		CheckColumn(26,"COVERAGE GROUP", fSpec.isCvgGroupCol(), fSpec.getCvgGroupColIndex(), null, null);

		CheckColumn(60,"CFLD1: " + fSpec.getCfld1Name(),fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), fSpec.getCfld1ParsePattern(), null);
		CheckColumn(61,"CFLD2: " + fSpec.getCfld2Name(),fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), fSpec.getCfld2ParsePattern(), null);
		CheckColumn(62,"CFLD3: " + fSpec.getCfld3Name(),fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), null, null);
		CheckColumn(63,"CFLD4: " + fSpec.getCfld4Name(),fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), null, null);
		CheckColumn(64,"CFLD5: " + fSpec.getCfld5Name(),fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), null, null);
 	}
		
	private void loadFileSpec(DynamicCoverageFileSpecification fSpec) 
	{
		if(fSpec == null) return;
		CheckColumn(1,"EMPLOYER IDENTIFIER",fSpec.isErCol(), fSpec.getErColIndex(), null, null);
		CheckColumn(2,"LITE ID", fSpec.isEtcRefCol(), fSpec.getEtcRefColIndex(), null, null);
		CheckColumn(3,"EMPLOYEE SSN", fSpec.isSsnCol(), fSpec.getSsnColIndex(), fSpec.getSsnParsePattern(), null);
		CheckColumn(4,"EMPLOYEE IDENTIFIER", fSpec.isErRefCol(), fSpec.getErRefColIndex(), fSpec.getErRefParsePattern(), null);
		CheckColumn(5,"SUBSCRIBER NUMBER",fSpec.isSubcrbrCol(), fSpec.getSubcrbrColIndex(), null, null);
		CheckColumn(6,"EMPLOYEE FLAG",fSpec.isEeFlagCol(), fSpec.getEeFlagColIndex(), fSpec.getEeFlagTrueParsePattern(), null);
		CheckColumn(7,"FIRST NAME", fSpec.isfNameCol(), fSpec.getfNameColIndex(), fSpec.getfNameParsePattern(), null);
		CheckColumn(8,"MIDDLE NAME", fSpec.ismNameCol(), fSpec.getmNameColIndex(), fSpec.getmNameParsePattern(), null);
		CheckColumn(9,"LAST NAME", fSpec.islNameCol(), fSpec.getlNameColIndex(), fSpec.getlNameParsePattern(), null);
		CheckColumn(10,"PHONE NUMBER",fSpec.isPhoneCol(), fSpec.getPhoneColIndex(), null, null);
		CheckColumn(11,"EMAIL COLUMN",fSpec.isEmlCol(), fSpec.getEmlColIndex(), null, null);

		CheckColumn(38,"COVERAGE WAIVED", fSpec.isWavdCol(), fSpec.getWavdColIndex(), fSpec.getWavdTrueParsePattern(), null);
		CheckColumn(39,"COVERAGE INELIGIBLE",fSpec.isInelCol(),fSpec.getInelColIndex(), fSpec.getInelTrueParsePattern(), null);
		CheckColumn(40,"COVERAGE TAX YEAR",fSpec.isTyCol(), fSpec.getTyColIndex(), fSpec.getTyParsePattern(), null);
		CheckColumn(42,"COVERAGE START DATE",fSpec.isCovStartDtCol(), fSpec.getCovStartDtColIndex(), null, fSpec.getCovStartDtFormat());
		CheckColumn(43,"COVERAGE END DATE",fSpec.isCovEndDtCol(), fSpec.getCovEndDtColIndex(), null, fSpec.getCovEndDtFormat());
		CheckColumn(44,"COVERAGE TAX YEAR SELECTED",fSpec.isTySelCol(), fSpec.getTySelColIndex(), fSpec.getTySelTrueParsePattern(), null);

		CheckColumn(45,"COVERAGE JANUARY SELECTED",fSpec.isJanSelCol(), fSpec.getJanSelColIndex(), fSpec.getJanSelTrueParsePattern(), null);
		CheckColumn(46,"COVERAGE FEBRUARY SELECTED",fSpec.isFebSelCol(), fSpec.getFebSelColIndex(), fSpec.getFebSelTrueParsePattern(), null);
		CheckColumn(47,"COVERAGE MARCH SELECTED",fSpec.isMarSelCol(), fSpec.getMarSelColIndex(), fSpec.getMarSelTrueParsePattern(), null);
		CheckColumn(48,"COVERAGE APRIL SELECTED",fSpec.isAprSelCol(), fSpec.getAprSelColIndex(), fSpec.getAprSelTrueParsePattern(), null);
		CheckColumn(49,"COVERAGE MAY SELECTED",fSpec.isMaySelCol(), fSpec.getMaySelColIndex(), fSpec.getMaySelTrueParsePattern(), null);
		CheckColumn(50,"COVERAGE JUNE SELECTED",fSpec.isJunSelCol(), fSpec.getJunSelColIndex(), fSpec.getJunSelTrueParsePattern(), null);
		CheckColumn(51,"COVERAGE JULY SELECTED",fSpec.isJulSelCol(), fSpec.getJulSelColIndex(), fSpec.getJulSelTrueParsePattern(), null);
		CheckColumn(52,"COVERAGE AUGUST SELECTED",fSpec.isAugSelCol(), fSpec.getAugSelColIndex(), fSpec.getAugSelTrueParsePattern(), null);
		CheckColumn(53,"COVERAGE SEPTEMBER SELECTED",fSpec.isSepSelCol(), fSpec.getSepSelColIndex(), fSpec.getSepSelTrueParsePattern(), null);
		CheckColumn(54,"COVERAGE OCTOBER SELECTED",fSpec.isOctSelCol(), fSpec.getOctSelColIndex(), fSpec.getOctSelTrueParsePattern(), null);
		CheckColumn(55,"COVERAGE NOVEMBER SELECTED",fSpec.isNovSelCol(), fSpec.getNovSelColIndex(), fSpec.getNovSelTrueParsePattern(), null);
		CheckColumn(56,"COVERAGE DECEMBER SELECTED",fSpec.isDecSelCol(), fSpec.getDecSelColIndex(), fSpec.getDecSelTrueParsePattern(), null);
		CheckColumn(57,"COVERAGE PLAN IDENTIFIER",fSpec.isPlanRefCol(), fSpec.getPlanRefColIndex(), fSpec.getPlanRefParsePattern(), null);
		CheckColumn(58,"COVERAGE DEDUCTION",fSpec.isDedCol(), fSpec.getDedColIndex(), null, null);
		CheckColumn(59,"COVERAGE MEMBER SHARE AMT",fSpec.isMbrShareCol(), fSpec.getMbrShareColIndex(), null, null);

		CheckColumn(12,"EMPLOYEE STREET NUMBER", fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), fSpec.getStreetParsePattern(), null);
		CheckColumn(13,"ADDRESS LINE 1", fSpec.isStreetCol(), fSpec.getStreetColIndex(), fSpec.getStreetParsePattern(), null);
		CheckColumn(14,"ADDRESS LINE 2", fSpec.isLin2Col(), fSpec.getLin2ColIndex(), null, null);
		CheckColumn(15,"CITY", fSpec.isCityCol(), fSpec.getCityColIndex(), fSpec.getCityParsePattern(), null);
		CheckColumn(16,"STATE", fSpec.isStateCol(), fSpec.getStateColIndex(), fSpec.getStateParsePattern(), null);
		CheckColumn(17,"ZIP", fSpec.isZipCol(), fSpec.getZipColIndex(), fSpec.getZipParsePattern(), null);
		CheckColumn(18,"GENDER", fSpec.isGenderCol(), fSpec.getGenderColIndex(), null, null);
		CheckColumn(19,"DATE OF BIRTH", fSpec.isDobCol(), fSpec.getDobColIndex(), null,fSpec.getDobFormat());
		CheckColumn(20,"HIRE DATE", fSpec.isHireDtCol(), fSpec.getHireDtColIndex(), null, fSpec.getHireDtFormat());

		CheckColumn(21,"TERMINATION DATE", fSpec.isTermDtCol(), fSpec.getTermDtColIndex(), null, fSpec.getTermDtFormat());
		CheckColumn(22,"COVERAGE CLASS", fSpec.isCvgGroupCol(), fSpec.getCvgGroupColIndex(), null, null);
		CheckColumn(23,"JOB TITLE", fSpec.isJobTtlCol(), fSpec.getJobTtlColIndex(), null, null);
		CheckColumn(24,"SEC LITE ID", fSpec.isDepEtcRefCol(), fSpec.getDepEtcRefColIndex(), null, null);
		CheckColumn(25,"SECONDARY SSN", fSpec.isDepSSNCol(), fSpec.getDepSSNColIndex(), fSpec.getDepSSNParsePattern(), null);
		CheckColumn(26,"SECONDARY IDENTIFIER", fSpec.isDepErRefCol(), fSpec.getDepErRefColIndex(), fSpec.getDepErRefParsePattern(), null);
		CheckColumn(27,"MEMBER NUMBER",fSpec.isMbrCol(), fSpec.getMbrColIndex(), null, null);
		CheckColumn(28,"SECONDARY FIRST NAME", fSpec.isDepFNameCol(), fSpec.getDepFNameColIndex(), fSpec.getDepFNameParsePattern(), null);
		CheckColumn(29,"SECONDARY MIDDLE NAME", fSpec.isDepMNameCol(), fSpec.getDepMNameColIndex(), fSpec.getDepMNameParsePattern(), null);
		CheckColumn(30,"SECONDARY LAST NAME", fSpec.isDepLNameCol(), fSpec.getDepLNameColIndex(), fSpec.getDepLNameParsePattern(), null);

		CheckColumn(31,"SECONDARY STREET NUMBER", fSpec.isDepStreetNumCol(), fSpec.getDepStreetNumColIndex(), fSpec.getDepStreetParsePattern(), null);
		CheckColumn(32,"SECONDARY ADDRESS LINE 1", fSpec.isDepStreetCol(), fSpec.getDepStreetColIndex(), fSpec.getDepStreetParsePattern(), null);
		CheckColumn(33,"SECONDARY ADDRESS LINE 2", fSpec.isDepLin2Col(), fSpec.getDepLin2ColIndex(), null, null);
		CheckColumn(34,"SECONDARY CITY", fSpec.isDepCityCol(), fSpec.getDepCityColIndex(), fSpec.getDepCityParsePattern(), null);
		CheckColumn(35,"SECONDARY STATE", fSpec.isDepStateCol(), fSpec.getDepStateColIndex(), fSpec.getDepStateParsePattern(), null);
		CheckColumn(36,"SECONDARY ZIP", fSpec.isDepZipCol(), fSpec.getDepZipColIndex(), fSpec.getDepZipParsePattern(), null);
		CheckColumn(37,"SECONDARY DATE OF BIRTH", fSpec.isDepDOBCol(), fSpec.getDepDOBColIndex(), null, fSpec.getDepDOBFormat());
		//CheckColumn(38,"COVERAGE WAIVED", fSpec.isWavdCol(), fSpec.getWavdColIndex(), null, null);
		//CheckColumn(39,"COVERAGE INELLIGIBLE", fSpec.isInelCol(), fSpec.getInelColIndex(), null, null);
		CheckColumn(60,"CFld1: " + fSpec.getCfld1Name(),fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), fSpec.getCfld1ParsePattern(), null);
		CheckColumn(61,"CFld2: " + fSpec.getCfld2Name(),fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), fSpec.getCfld2ParsePattern(), null);
		//CheckColumn(62,"CFld3: " + fSpec.getCfld3Name(),fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), null, null);
		//CheckColumn(63,"CFld4: " + fSpec.getCfld4Name(),fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), null, null);
		//CheckColumn(64,"CFld5: " + fSpec.getCfld5Name(),fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), null, null);

		CheckColumn(65,"DepCFld1: " + fSpec.getDepCfld1Name(),fSpec.isDepCfld1Col(), fSpec.getDepCfld1ColIndex(), fSpec.getDepCfld1ParsePattern(), null);
		CheckColumn(66,"DepCFld2: " + fSpec.getDepCfld2Name(),fSpec.isDepCfld2Col(), fSpec.getDepCfld2ColIndex(), fSpec.getDepCfld2ParsePattern(), null);
		CheckColumn(67,"DepCFld3: " + fSpec.getDepCfld3Name(),fSpec.isDepCfld3Col(), fSpec.getDepCfld3ColIndex(), null, null);
		CheckColumn(68,"DepCFld4: " + fSpec.getDepCfld4Name(),fSpec.isDepCfld4Col(), fSpec.getDepCfld4ColIndex(), null, null);
		CheckColumn(69,"DepCFld5: " + fSpec.getDepCfld5Name(),fSpec.isDepCfld5Col(), fSpec.getDepCfld5ColIndex(), null, null);
		//CheckColumn(70,"CovCFld1: " + fSpec.getCovCfld1Name(),fSpec.isCovCfld1Col(), fSpec.getCovCfld1ColIndex(), fSpec.getCovCfld1ParsePattern(), null);
		//CheckColumn(71,"CovCFld1: " + fSpec.getCovCfld2Name(),fSpec.isCovCfld2Col(), fSpec.getCovCfld2ColIndex(), null, null);
	}
	
	private void loadFileSpec(DynamicPayFileSpecification fSpec) 
	{
		if(fSpec == null) return;
		CheckColumn(1,"EMPLOYER IDENTIFIER",fSpec.isErCol(), fSpec.getErColIndex(), null, null);
		CheckColumn(2,"EMPLOYEE SSN", fSpec.isSsnCol(), fSpec.getSsnColIndex(), fSpec.getSsnParsePattern(), null);
		CheckColumn(3,"EMPLOYEE IDENTIFIER", fSpec.isErRefCol(), fSpec.getErRefColIndex(), fSpec.getErRefParsePattern(), null);
		CheckColumn(4,"FIRST NAME", fSpec.isfNameCol(), fSpec.getfNameColIndex(), fSpec.getfNameParsePattern(), null);
		CheckColumn(5,"MIDDLE NAME", fSpec.ismNameCol(), fSpec.getmNameColIndex(), fSpec.getmNameParsePattern(), null);
		CheckColumn(6,"LAST NAME", fSpec.islNameCol(), fSpec.getlNameColIndex(), fSpec.getlNameParsePattern(), null);
		CheckColumn(7,"STREET NUMBER", fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), fSpec.getStreetParsePattern(), null);
		CheckColumn(8,"ADDRESS LINE 1", fSpec.isStreetCol(), fSpec.getStreetColIndex(), fSpec.getStreetParsePattern(), null);
		CheckColumn(9,"ADDRESS LINE 2", fSpec.isLin2Col(), fSpec.getLin2ColIndex(), null, null);
		CheckColumn(10,"CITY", fSpec.isCityCol(), fSpec.getCityColIndex(), fSpec.getCityParsePattern(), null);

		CheckColumn(11,"STATE", fSpec.isStateCol(), fSpec.getStateColIndex(), fSpec.getStateParsePattern(), null);
		CheckColumn(12,"ZIP", fSpec.isZipCol(), fSpec.getZipColIndex(), fSpec.getZipParsePattern(), null);
		CheckColumn(13,"DATE OF BIRTH", fSpec.isDobCol(), fSpec.getDobColIndex(), null, fSpec.getDobFormat());
		CheckColumn(14,"HIRE DATE", fSpec.isHireDtCol(), fSpec.getHireDtColIndex(), null, fSpec.getHireDtFormat());
		CheckColumn(15,"REHIRE DATE", fSpec.isRhireDtCol(), fSpec.getRhireDtColIndex(), null, fSpec.getRhireDtFormat());
		CheckColumn(16,"TERMINATION DATE", fSpec.isTermDtCol(), fSpec.getTermDtColIndex(), null, fSpec.getTermDtFormat());
		CheckColumn(17,"DEPARTMENT", fSpec.isDeptCol(), fSpec.getDeptColIndex(), null, null);
		CheckColumn(18,"LOCATION", fSpec.isLocCol(), fSpec.getLocColIndex(), null, null);
		CheckColumn(19,"COVERAGE GROUP", fSpec.isCvgGroupCol(), fSpec.getCvgGroupColIndex(), null, null);
		CheckColumn(20,"GENDER", fSpec.isGenderCol(), fSpec.getGenderColIndex(), null, null);
		
		CheckColumn(21,"PHONE",fSpec.isPhoneCol(), fSpec.getPhoneColIndex(), null, null);
		CheckColumn(22,"EMAIL",fSpec.isEmlCol(), fSpec.getEmlColIndex(), null, null);
		CheckColumn(23,"JOB TITLE",fSpec.isJobTtlCol(), fSpec.getJobTtlColIndex(), null, null);
		CheckColumn(24,"PPD START DATE", fSpec.isPpdStartDtCol(), fSpec.getPpdStartDtColIndex(), fSpec.getPpdStartDtParsePattern(), fSpec.getPpdStartDtFormat());
		CheckColumn(25,"PPD END DATE", fSpec.isPpdEndDtCol(), fSpec.getPpdEndDtColIndex(), fSpec.getPpdEndDtParsePattern(), fSpec.getPpdEndDtFormat());
		CheckColumn(26,"PAY DATE", fSpec.isPayDtCol(), fSpec.getPayDtColIndex(), fSpec.getPayDtParsePattern(), fSpec.getPayDtFormat());
		CheckColumn(27,"PPD ID", fSpec.isPpdErRefCol(), fSpec.getPpdErRefColIndex(), null, null);
		CheckColumn(28,"PPD FREQUENCY", fSpec.isPpdFreqCol(), fSpec.getPpdFreqColIndex(), null, null);
		CheckColumn(29,"WORK DATE", fSpec.isWorkDtCol(), fSpec.getWorkDtColIndex(), fSpec.getWorkDtParsePattern(), fSpec.getWorkDtFormat());
		CheckColumn(30,"HOURS", fSpec.isHrsCol(), fSpec.getHrsColIndex(), null, null);
		
		CheckColumn(31,"RATE", fSpec.isRateCol(), fSpec.getRateColIndex(), null, null);
		CheckColumn(32,"AMOUNT", fSpec.isAmtCol(), fSpec.getAmtColIndex(), null, null);
		CheckColumn(33,"PAYCODE", fSpec.isPayCodeCol(), fSpec.getPayCodeColIndex(), null, null);
		CheckColumn(34,"UNION TYPE", fSpec.isUnionTypeCol(), fSpec.getUnionTypeColIndex(), null, null);
		CheckColumn(35,"OT HRS",fSpec.isOtHrsCol(), fSpec.getOtHrsColIndex(), null, null);
		CheckColumn(36,"OT 2 HRS",fSpec.isOtHrs2Col(), fSpec.getOtHrs2ColIndex(), null, null);
		CheckColumn(37,"OT 3 HRS",fSpec.isOtHrs3Col(), fSpec.getOtHrs3ColIndex(), null, null);
		CheckColumn(38,"PTO HRS",fSpec.isPtoHrsCol(), fSpec.getPtoHrsColIndex(), null, null);
		CheckColumn(39,"SICK PAY HRS",fSpec.isSickHrsCol(), fSpec.getSickHrsColIndex(), null, null);
		CheckColumn(40,"HLDY PAY HRS",fSpec.isHolidayHrsCol(), fSpec.getHolidayHrsColIndex(), null, null);
		
		CheckColumn(41,"PAY CLASS", fSpec.isPayClassCol(), fSpec.getPayClassColIndex(), null, null);
		CheckColumn(42,"CFLD1: " + fSpec.getCfld1Name(),fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), fSpec.getCfld1ParsePattern(), null);
		CheckColumn(43,"CFLD2: " + fSpec.getCfld2Name(),fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), fSpec.getCfld2ParsePattern(), null);
		CheckColumn(44,"CFLD3: " + fSpec.getCfld3Name(),fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), null, null);
		CheckColumn(45,"CFLD4: " + fSpec.getCfld4Name(),fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), null, null);
		CheckColumn(46,"CFLD5: " + fSpec.getCfld5Name(),fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), null, null);
		CheckColumn(47,"PayCFld1: " + fSpec.getPayCfld1Name(),fSpec.isPayCfld1Col(), fSpec.getPayCfld1ColIndex(), fSpec.getPayCfld1ParsePattern(), null);
		CheckColumn(48,"PayCFld2: " + fSpec.getPayCfld2Name(),fSpec.isPayCfld2Col(), fSpec.getPayCfld2ColIndex(), fSpec.getPayCfld2ParsePattern(), null);
		CheckColumn(49,"PayCFld3: " + fSpec.getPayCfld3Name(),fSpec.isPayCfld3Col(), fSpec.getPayCfld3ColIndex(), null, null);
		CheckColumn(50,"PayCFld4: " + fSpec.getPayCfld4Name(),fSpec.isPayCfld4Col(), fSpec.getPayCfld4ColIndex(), null, null);
		
		CheckColumn(51,"PayCFld5: " + fSpec.getPayCfld5Name(),fSpec.isPayCfld5Col(), fSpec.getPayCfld5ColIndex(), null, null);
		CheckColumn(52,"PayCFld6: " + fSpec.getPayCfld6Name(),fSpec.isPayCfld6Col(), fSpec.getPayCfld6ColIndex(), null, null);
		CheckColumn(53,"PayCFld7: " + fSpec.getPayCfld7Name(),fSpec.isPayCfld7Col(), fSpec.getPayCfld7ColIndex(), null, null);
	}
	
	private void loadIrs1094cFileSpec()
	{
		CheckColumn(1,"YEAR", true, 1, null, null);
		CheckColumn(2,"EIN", true, 2, null, null);
		CheckColumn(3,"NAME", true, 3, null, null);
		CheckColumn(4,"ANNFTEES", true, 4, null, null);
		CheckColumn(5,"ANNEES", true, 5, null, null);
		CheckColumn(6,"JANFTEES", true, 6, null, null);
		CheckColumn(7,"JANEES", true, 7, null, null);
		CheckColumn(8,"FEBFTEES", true, 8, null, null);
		CheckColumn(9,"FEBEES", true, 9, null, null);
		CheckColumn(10,"MARFTEES", true, 10, null, null);
		
		CheckColumn(11,"MAREES", true, 11, null, null);
		CheckColumn(12,"APRFTEES", true, 12, null, null);
		CheckColumn(13,"APREES", true, 13, null, null);
		CheckColumn(14,"MAYFTEES", true, 14, null, null);
		CheckColumn(15,"MAYEES", true, 15, null, null);
		CheckColumn(16,"JUNFTEES", true, 16, null, null);
		CheckColumn(17,"JUNEES", true, 17, null, null);
		CheckColumn(18,"JULFTEES", true, 18, null, null);
		CheckColumn(19,"JULEES", true, 19, null, null);
		CheckColumn(20,"AUGFTEES", true, 20, null, null);
		
		CheckColumn(21,"AUGEES", true, 21, null, null);
		CheckColumn(22,"SEPFTEES", true, 22, null, null);
		CheckColumn(23,"SEPEES", true, 23, null, null);
		CheckColumn(24,"OCTFTEES", true, 24, null, null);
		CheckColumn(25,"OCTEES", true, 25, null, null);
		CheckColumn(26,"NOVFTEES", true, 26, null, null);
		CheckColumn(27,"NOVEES", true, 27, null, null);
		CheckColumn(28,"DECFTEES", true, 28, null, null);
		CheckColumn(29,"DECEES", true, 29, null, null);
		CheckColumn(30,"CUSTOM FIELD 1", true, 30, null, null);
		
		CheckColumn(31,"CUSTOM FIELD 2", true, 31, null, null);
		CheckColumn(32,"CUSTOM FIELD 3", true, 32, null, null);
		CheckColumn(33,"CUSTOM FIELD 4", true, 33, null, null);
		CheckColumn(34,"CUSTOM FIELD 5", true, 34, null, null);
 	}
}
