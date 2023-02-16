package com.etc.admin.ui.pipeline.raw;

	import java.util.Arrays;

import org.controlsfx.control.spreadsheet.GridBase;
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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.layout.VBox;

	public class ViewPipelineRawMapperDisplayController
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
		
		//PipelineQueue queue = null;
		RawFieldData rawData = null;
		
		//the vbox holding everything
		VBox mapperVBox = null;
		
		public void setVBox(VBox vBox) {
			mapperVBox = vBox;
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
				default:
				// no file spec
				return;
			}
			
			// Now that we have the column data, create the display
			ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
			final ObservableList<SpreadsheetCell> list = FXCollections.observableArrayList();	
			// now go through the column data, creating spreadsheet cells
			for(int i = 0; i< mapperLastPosition + 1; i++)
			{
				//String cellData = "";
				list.add(getMapperCell(columnColors[i],1,i,columnFields[i]));
			}
			
			//add the rows
			rows.add(list);
			GridBase grid = new GridBase(2, list.size());
			grid.setRows(rows);
			
			//add to the scene
		    if(mapperSPV != null)
		    	mapperVBox.getChildren().remove(mapperSPV);
		    mapperSPV = new SpreadsheetView(grid);
		    mapperVBox.getChildren().add(mapperSPV);
		    mapperSPV.setPrefWidth(500);
		    mapperSPV.setMaxWidth(500);
		    
		    // set the column headers
		    char c = 'A';
		    char c1 = 'A';
		    int letterPos = 0;
		    String letters = "";
		    for(int i = 0; i<list.size() - 1; i++)
		    {
		    	if(i < 26)
		    		letters =  String.valueOf(c);
		    	else {
		    		letterPos = i/26;
		    		c1 += (letterPos - 1); // starting with A
		    		letters = String.valueOf(c1) + String.valueOf(c);
		    		c1 = 'A';
		    	}
		    	mapperSPV.getGrid().getColumnHeaders().add(i, letters + String.valueOf(i));
		    	c++;
		    	if(c> 'Z') c = 'A';
		    }
		    
		    //last column header
		    mapperSPV.getGrid().getColumnHeaders().add(list.size()-1, "");
		    
		    mapperSPV.getGrid().getRowHeaders().add(0, "Fields");
		    mapperSPV.setRowHeaderWidth(75);

		    // use our calculated height
		    mapperVBox.setMinHeight(60 + (mapperMaxEntries * 24));
		    mapperVBox.setPrefHeight(60 + (mapperMaxEntries * 24));
		    mapperVBox.setMaxHeight(60 + (mapperMaxEntries * 24));
		    
		    mapperSPV.setPrefWidth(Control.USE_COMPUTED_SIZE);
		    mapperSPV.setMaxWidth(Control.USE_COMPUTED_SIZE);
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
		
		private SpreadsheetCell getMapperCell(int colIndex, int row, int column, String data)
		{
			if(data == null) data = "";
			if(data.length() < 1) data = "          ";
			SpreadsheetCell newCell = SpreadsheetCellType.STRING.createCell(row, column, 1, 1, data);
			newCell.setEditable(false);
			if(data.trim().length() > 0 )
			{
				newCell.getStyleClass().add("BackColor" + columnColors[column]);
			}
			return newCell;
		}

		private void loadFileSpec(DynamicEmployeeFileSpecification fSpec)
		{
			if(fSpec == null) return;
//			CheckColumn(1,"RECORD TYPE",fSpec.is, fSpec.getErColIndex(), null, null);
//			CheckColumn(1,"ERROR",fSpec.isErCol(), fSpec.getErColIndex(), null, null);
			CheckColumn(1,"EMPLOYER IDENTIFIER",fSpec.isErCol(), fSpec.getErColIndex(), null, null);
			CheckColumn(2,"LITE ID",fSpec.isEtcRefCol(), fSpec.getEtcRefColIndex(), null, null);
			CheckColumn(3,"EMPLOYEE SSN", fSpec.isSsnCol(), fSpec.getSsnColIndex(), fSpec.getSsnParsePattern(), null);
			CheckColumn(4,"EMPLOYEE IDENTIFIER", fSpec.isErRefCol(), fSpec.getErRefColIndex(), fSpec.getErRefParsePattern(), null);
			CheckColumn(5,"FIRST NAME", fSpec.isfNameCol(), fSpec.getfNameColIndex(), fSpec.getfNameParsePattern(), null);
			CheckColumn(6,"MIDDLE NAME", fSpec.ismNameCol(), fSpec.getmNameColIndex(), fSpec.getmNameParsePattern(), null);
			CheckColumn(7,"LAST NAME", fSpec.islNameCol(), fSpec.getlNameColIndex(), fSpec.getlNameParsePattern(), null);
			CheckColumn(8,"PHONE NUMBER",fSpec.isPhoneCol(), fSpec.getPhoneColIndex(), null, null);
			CheckColumn(9,"EMAIL COLUMN",fSpec.isEmlCol(), fSpec.getEmlColIndex(), null, null);
			CheckColumn(10,"DEPARTMENT", fSpec.isDeptCol(), fSpec.getDeptColIndex(), null, null);
			CheckColumn(11,"LOCATION", fSpec.isLocCol(), fSpec.getLocColIndex(), null, null);
			CheckColumn(12,"JOB TITLE", fSpec.isJobTtlCol(), fSpec.getJobTtlColIndex(), null, null);
			CheckColumn(13,"PAYCODE", fSpec.isPayCodeCol(), fSpec.getPayCodeColIndex(), null, null);
			CheckColumn(14,"CFLD1: " + fSpec.getCfld1Name(),fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), fSpec.getCfld1ParsePattern(), null);
			CheckColumn(15,"CFLD2: " + fSpec.getCfld2Name(),fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), fSpec.getCfld2ParsePattern(), null);
			CheckColumn(16,"CFLD3: " + fSpec.getCfld3Name(),fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), null, null);
			CheckColumn(17,"CFLD4: " + fSpec.getCfld4Name(),fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), null, null);
			CheckColumn(18,"CFLD5: " + fSpec.getCfld5Name(),fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), null, null);
			CheckColumn(19,"STREET NUMBER", fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), fSpec.getStreetParsePattern(), null);
			CheckColumn(20,"ADDRESS LINE 1", fSpec.isStreetCol(), fSpec.getStreetColIndex(), fSpec.getStreetParsePattern(), null);
			CheckColumn(21,"ADDRESS LINE 2", fSpec.isLin2Col(), fSpec.getLin2ColIndex(), null, null);
			CheckColumn(22,"CITY", fSpec.isCityCol(), fSpec.getCityColIndex(), fSpec.getCityParsePattern(), null);
			CheckColumn(23,"STATE", fSpec.isStateCol(), fSpec.getStateColIndex(), fSpec.getStateParsePattern(), null);
			CheckColumn(24,"ZIP", fSpec.isZipCol(), fSpec.getZipColIndex(), fSpec.getZipParsePattern(), null);
			CheckColumn(25,"GENDER", fSpec.isGenderCol(), fSpec.getGenderColIndex(), null, null);
			CheckColumn(26,"DATE OF BIRTH", fSpec.isDobCol(), fSpec.getDobColIndex(), null, fSpec.getDobFormat());
			CheckColumn(27,"HIRE DATE", fSpec.isHireDtCol(), fSpec.getHireDtColIndex(), null, fSpec.getHireDtFormat());
			CheckColumn(28,"REHIRE DATE", fSpec.isRhireDtCol(), fSpec.getRhireDtColIndex(), null, fSpec.getRhireDtFormat());
			CheckColumn(29,"TERMINATION DATE", fSpec.isTermDtCol(), fSpec.getTermDtColIndex(), null, fSpec.getTermDtFormat());
			CheckColumn(30,"UNION TYPE", fSpec.isUnionTypeCol(), fSpec.getUnionTypeColIndex(), null, null);
			CheckColumn(31,"COVERAGE GROUP", fSpec.isCvgGroupCol(), fSpec.getCvgGroupColIndex(), null, null);
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

			CheckColumn(12,"COVERAGE WAIVED", fSpec.isWavdCol(), fSpec.getWavdColIndex(), fSpec.getWavdTrueParsePattern(), null);
			CheckColumn(13,"COVERAGE INELIGIBLE",fSpec.isInelCol(),fSpec.getInelColIndex(), fSpec.getInelTrueParsePattern(), null);
			CheckColumn(14,"COVERAGE TAX YEAR",fSpec.isTyCol(), fSpec.getTyColIndex(), fSpec.getTyParsePattern(), null);
			CheckColumn(15,"COVERAGE START DATE",fSpec.isCovStartDtCol(), fSpec.getCovStartDtColIndex(), null, fSpec.getCovStartDtFormat());
			CheckColumn(16,"COVERAGE END DATE",fSpec.isCovEndDtCol(), fSpec.getCovEndDtColIndex(), null, fSpec.getCovEndDtFormat());
			CheckColumn(17,"COVERAGE TAX YEAR SELECTED",fSpec.isTySelCol(), fSpec.getTySelColIndex(), fSpec.getTySelTrueParsePattern(), null);

			CheckColumn(18,"COVERAGE JANUARY SELECTED",fSpec.isJanSelCol(), fSpec.getJanSelColIndex(), fSpec.getJanSelTrueParsePattern(), null);
			CheckColumn(19,"COVERAGE FEBRUARY SELECTED",fSpec.isFebSelCol(), fSpec.getFebSelColIndex(), fSpec.getFebSelTrueParsePattern(), null);
			CheckColumn(20,"COVERAGE MARCH SELECTED",fSpec.isMarSelCol(), fSpec.getMarSelColIndex(), fSpec.getMarSelTrueParsePattern(), null);
			CheckColumn(21,"COVERAGE APRIL SELECTED",fSpec.isAprSelCol(), fSpec.getAprSelColIndex(), fSpec.getAprSelTrueParsePattern(), null);
			CheckColumn(22,"COVERAGE MAY SELECTED",fSpec.isMaySelCol(), fSpec.getMaySelColIndex(), fSpec.getMaySelTrueParsePattern(), null);
			CheckColumn(23,"COVERAGE JUNE SELECTED",fSpec.isJunSelCol(), fSpec.getJunSelColIndex(), fSpec.getJunSelTrueParsePattern(), null);
			CheckColumn(24,"COVERAGE JULY SELECTED",fSpec.isJulSelCol(), fSpec.getJulSelColIndex(), fSpec.getJulSelTrueParsePattern(), null);
			CheckColumn(25,"COVERAGE AUGUST SELECTED",fSpec.isAugSelCol(), fSpec.getAugSelColIndex(), fSpec.getAugSelTrueParsePattern(), null);
			CheckColumn(26,"COVERAGE SEPTEMBER SELECTED",fSpec.isSepSelCol(), fSpec.getSepSelColIndex(), fSpec.getSepSelTrueParsePattern(), null);
			CheckColumn(27,"COVERAGE OCTOBER SELECTED",fSpec.isOctSelCol(), fSpec.getOctSelColIndex(), fSpec.getOctSelTrueParsePattern(), null);
			CheckColumn(28,"COVERAGE NOVEMBER SELECTED",fSpec.isNovSelCol(), fSpec.getNovSelColIndex(), fSpec.getNovSelTrueParsePattern(), null);
			CheckColumn(29,"COVERAGE DECEMBER SELECTED",fSpec.isDecSelCol(), fSpec.getDecSelColIndex(), fSpec.getDecSelTrueParsePattern(), null);
			CheckColumn(30,"COVERAGE PLAN IDENTIFIER",fSpec.isPlanRefCol(), fSpec.getPlanRefColIndex(), fSpec.getPlanRefParsePattern(), null);
			CheckColumn(31,"COVERAGE DEDUCTION",fSpec.isDedCol(), fSpec.getDedColIndex(), null, null);
			CheckColumn(32,"COVERAGE MEMBER SHARE AMT",fSpec.isMbrShareCol(), fSpec.getMbrShareColIndex(), null, null);

			CheckColumn(33,"EMPLOYEE STREET NUMBER", fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), fSpec.getStreetParsePattern(), null);
			CheckColumn(34,"ADDRESS LINE 1", fSpec.isStreetCol(), fSpec.getStreetColIndex(), fSpec.getStreetParsePattern(), null);
			CheckColumn(35,"ADDRESS LINE 2", fSpec.isLin2Col(), fSpec.getLin2ColIndex(), null, null);
			CheckColumn(36,"CITY", fSpec.isCityCol(), fSpec.getCityColIndex(), fSpec.getCityParsePattern(), null);
			CheckColumn(37,"STATE", fSpec.isStateCol(), fSpec.getStateColIndex(), fSpec.getStateParsePattern(), null);
			CheckColumn(38,"ZIP", fSpec.isZipCol(), fSpec.getZipColIndex(), fSpec.getZipParsePattern(), null);
			CheckColumn(39,"GENDER", fSpec.isGenderCol(), fSpec.getGenderColIndex(), null, null);
			CheckColumn(40,"DATE OF BIRTH", fSpec.isDobCol(), fSpec.getDobColIndex(), null,fSpec.getDobFormat());
			CheckColumn(41,"HIRE DATE", fSpec.isHireDtCol(), fSpec.getHireDtColIndex(), null, fSpec.getHireDtFormat());
			CheckColumn(42,"TERMINATION DATE", fSpec.isTermDtCol(), fSpec.getTermDtColIndex(), null, fSpec.getTermDtFormat());
			CheckColumn(43,"SEC LITE ID", fSpec.isDepEtcRefCol(), fSpec.getDepEtcRefColIndex(), null, null);
			CheckColumn(44,"SECONDARY SSN", fSpec.isDepSSNCol(), fSpec.getDepSSNColIndex(), fSpec.getDepSSNParsePattern(), null);
			CheckColumn(45,"SECONDARY IDENTIFIER", fSpec.isDepErRefCol(), fSpec.getDepErRefColIndex(), fSpec.getDepErRefParsePattern(), null);
			CheckColumn(46,"MEMBER NUMBER",fSpec.isMbrCol(), fSpec.getMbrColIndex(), null, null);

			CheckColumn(47,"SECONDARY FIRST NAME", fSpec.isDepFNameCol(), fSpec.getDepFNameColIndex(), fSpec.getDepFNameParsePattern(), null);
			CheckColumn(48,"SECONDARY MIDDLE NAME", fSpec.isDepMNameCol(), fSpec.getDepMNameColIndex(), fSpec.getDepMNameParsePattern(), null);
			CheckColumn(49,"SECONDARY LAST NAME", fSpec.isDepLNameCol(), fSpec.getDepLNameColIndex(), fSpec.getDepLNameParsePattern(), null);
			CheckColumn(50,"SECONDARY STREET NUMBER", fSpec.isDepStreetNumCol(), fSpec.getDepStreetNumColIndex(), fSpec.getDepStreetParsePattern(), null);
			CheckColumn(51,"SECONDARY ADDRESS LINE 1", fSpec.isDepStreetCol(), fSpec.getDepStreetColIndex(), fSpec.getDepStreetParsePattern(), null);
			CheckColumn(52,"SECONDARY ADDRESS LINE 2", fSpec.isDepLin2Col(), fSpec.getDepLin2ColIndex(), null, null);
			CheckColumn(53,"SECONDARY CITY", fSpec.isDepCityCol(), fSpec.getDepCityColIndex(), fSpec.getDepCityParsePattern(), null);
			CheckColumn(54,"SECONDARY STATE", fSpec.isDepStateCol(), fSpec.getDepStateColIndex(), fSpec.getDepStateParsePattern(), null);
			CheckColumn(55,"SECONDARY ZIP", fSpec.isDepZipCol(), fSpec.getDepZipColIndex(), fSpec.getDepZipParsePattern(), null);
			CheckColumn(56,"SECONDARY DATE OF BIRTH", fSpec.isDepDOBCol(), fSpec.getDepDOBColIndex(), null, fSpec.getDepDOBFormat());
			CheckColumn(57,"COVERAGE CLASS", fSpec.isCvgGroupCol(), fSpec.getCvgGroupColIndex(), null, null);
			CheckColumn(58,"JOB TITLE", fSpec.isJobTtlCol(), fSpec.getJobTtlColIndex(), null, null);
			CheckColumn(59,"CFld1: " + fSpec.getCfld1Name(),fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), fSpec.getCfld1ParsePattern(), null);
			CheckColumn(60,"CFld2: " + fSpec.getCfld2Name(),fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), fSpec.getCfld2ParsePattern(), null);
			CheckColumn(61,"CFld3: " + fSpec.getCfld3Name(),fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), null, null);
			CheckColumn(62,"CFld4: " + fSpec.getCfld4Name(),fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), null, null);
			CheckColumn(63,"CFld5: " + fSpec.getCfld5Name(),fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), null, null);

			CheckColumn(64,"DepCFld1: " + fSpec.getDepCfld1Name(),fSpec.isDepCfld1Col(), fSpec.getDepCfld1ColIndex(), fSpec.getDepCfld1ParsePattern(), null);
			CheckColumn(65,"DepCFld2: " + fSpec.getDepCfld2Name(),fSpec.isDepCfld2Col(), fSpec.getDepCfld2ColIndex(), fSpec.getDepCfld2ParsePattern(), null);
			CheckColumn(66,"DepCFld3: " + fSpec.getDepCfld3Name(),fSpec.isDepCfld3Col(), fSpec.getDepCfld3ColIndex(), null, null);
			CheckColumn(67,"DepCFld4: " + fSpec.getDepCfld4Name(),fSpec.isDepCfld4Col(), fSpec.getDepCfld4ColIndex(), null, null);
			CheckColumn(68,"DepCFld5: " + fSpec.getDepCfld5Name(),fSpec.isDepCfld5Col(), fSpec.getDepCfld5ColIndex(), null, null);
			CheckColumn(69,"CovCFld1: " + fSpec.getCovCfld1Name(),fSpec.isCovCfld1Col(), fSpec.getCovCfld1ColIndex(), fSpec.getCovCfld1ParsePattern(), null);
			CheckColumn(70,"CovCFld1: " + fSpec.getCovCfld2Name(),fSpec.isCovCfld2Col(), fSpec.getCovCfld2ColIndex(), null, null);
		}
		
		private void loadFileSpec(DynamicPayFileSpecification fSpec) 
		{
			if(fSpec == null) return;
			CheckColumn(1,"EMPLOYER IDENTIFIER",fSpec.isErCol(), fSpec.getErColIndex(), null, null);
			CheckColumn(3,"EMPLOYEE SSN", fSpec.isSsnCol(), fSpec.getSsnColIndex(), fSpec.getSsnParsePattern(), null);
			CheckColumn(4,"EMPLOYEE IDENTIFIER", fSpec.isErRefCol(), fSpec.getErRefColIndex(), fSpec.getErRefParsePattern(), null);
			CheckColumn(5,"FIRST NAME", fSpec.isfNameCol(), fSpec.getfNameColIndex(), fSpec.getfNameParsePattern(), null);
			CheckColumn(6,"MIDDLE NAME", fSpec.ismNameCol(), fSpec.getmNameColIndex(), fSpec.getmNameParsePattern(), null);
			CheckColumn(7,"LAST NAME", fSpec.islNameCol(), fSpec.getlNameColIndex(), fSpec.getlNameParsePattern(), null);
			CheckColumn(8,"DEPARTMENT", fSpec.isDeptCol(), fSpec.getDeptColIndex(), null, null);
			CheckColumn(9,"LOCATION", fSpec.isLocCol(), fSpec.getLocColIndex(), null, null);
			CheckColumn(10,"PAYCODE", fSpec.isPayCodeCol(), fSpec.getPayCodeColIndex(), null, null);
			CheckColumn(11,"CFLD1: " + fSpec.getCfld1Name(),fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), fSpec.getCfld1ParsePattern(), null);
			CheckColumn(12,"CFLD2: " + fSpec.getCfld2Name(),fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), fSpec.getCfld2ParsePattern(), null);
			CheckColumn(13,"CFLD3: " + fSpec.getCfld3Name(),fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), null, null);
			CheckColumn(14,"CFLD4: " + fSpec.getCfld4Name(),fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), null, null);
			CheckColumn(15,"CFLD5: " + fSpec.getCfld5Name(),fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), null, null);
			CheckColumn(16,"STREET NUMBER", fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), fSpec.getStreetParsePattern(), null);
			CheckColumn(17,"ADDRESS LINE 1", fSpec.isStreetCol(), fSpec.getStreetColIndex(), fSpec.getStreetParsePattern(), null);
			CheckColumn(18,"ADDRESS LINE 2", fSpec.isLin2Col(), fSpec.getLin2ColIndex(), null, null);
			CheckColumn(19,"CITY", fSpec.isCityCol(), fSpec.getCityColIndex(), fSpec.getCityParsePattern(), null);
			CheckColumn(20,"STATE", fSpec.isStateCol(), fSpec.getStateColIndex(), fSpec.getStateParsePattern(), null);
			CheckColumn(21,"ZIP", fSpec.isZipCol(), fSpec.getZipColIndex(), fSpec.getZipParsePattern(), null);
			CheckColumn(22,"DATE OF BIRTH", fSpec.isDobCol(), fSpec.getDobColIndex(), null, fSpec.getDobFormat());
			CheckColumn(23,"HIRE DATE", fSpec.isHireDtCol(), fSpec.getHireDtColIndex(), null, fSpec.getHireDtFormat());
			CheckColumn(24,"REHIRE DATE", fSpec.isRhireDtCol(), fSpec.getRhireDtColIndex(), null, fSpec.getRhireDtFormat());
			CheckColumn(25,"TERMINATION DATE", fSpec.isTermDtCol(), fSpec.getTermDtColIndex(), null, fSpec.getTermDtFormat());
			CheckColumn(26,"COVERAGE GROUP", fSpec.isCvgGroupCol(), fSpec.getCvgGroupColIndex(), null, null);
			CheckColumn(27,"GENDER", fSpec.isGenderCol(), fSpec.getGenderColIndex(), null, null);
			CheckColumn(28,"PPD START DATE", fSpec.isPpdStartDtCol(), fSpec.getPpdStartDtColIndex(), fSpec.getPpdStartDtParsePattern(), fSpec.getPpdStartDtFormat());
			CheckColumn(29,"PPD END DATE", fSpec.isPpdEndDtCol(), fSpec.getPpdEndDtColIndex(), fSpec.getPpdEndDtParsePattern(), fSpec.getPpdEndDtFormat());
			CheckColumn(30,"PAY DATE", fSpec.isPayDtCol(), fSpec.getPayDtColIndex(), fSpec.getPayDtParsePattern(), fSpec.getPayDtFormat());
			CheckColumn(31,"PPD ID", fSpec.isPpdErRefCol(), fSpec.getPpdErRefColIndex(), null, null);
			CheckColumn(32,"PPD FREQUENCY", fSpec.isPpdFreqCol(), fSpec.getPpdFreqColIndex(), null, null);
			CheckColumn(33,"HOURS", fSpec.isHrsCol(), fSpec.getHrsColIndex(), null, null);
			CheckColumn(34,"RATE", fSpec.isRateCol(), fSpec.getRateColIndex(), null, null);
			CheckColumn(35,"AMOUNT", fSpec.isAmtCol(), fSpec.getAmtColIndex(), null, null);
			CheckColumn(36,"WORK DATE", fSpec.isWorkDtCol(), fSpec.getWorkDtColIndex(), fSpec.getWorkDtParsePattern(), fSpec.getWorkDtFormat());
			CheckColumn(37,"UNION TYPE", fSpec.isUnionTypeCol(), fSpec.getUnionTypeColIndex(), null, null);
			CheckColumn(38,"OT HRS",fSpec.isOtHrsCol(), fSpec.getOtHrsColIndex(), null, null);
			CheckColumn(39,"OT 2 HRS",fSpec.isOtHrs2Col(), fSpec.getOtHrs2ColIndex(), null, null);
			CheckColumn(40,"OT 3 HRS",fSpec.isOtHrs3Col(), fSpec.getOtHrs3ColIndex(), null, null);
			CheckColumn(41,"PTO HRS",fSpec.isPtoHrsCol(), fSpec.getPtoHrsColIndex(), null, null);
			CheckColumn(42,"SICK PAY HRS",fSpec.isSickHrsCol(), fSpec.getSickHrsColIndex(), null, null);
			CheckColumn(43,"HLDY PAY HRS",fSpec.isHolidayHrsCol(), fSpec.getHolidayHrsColIndex(), null, null);
			CheckColumn(44,"PAY CLASS", fSpec.isPayClassCol(), fSpec.getPayClassColIndex(), null, null);
			CheckColumn(45,"HOURS",fSpec.isHrsCol(), fSpec.getHrsColIndex(), null, null);
			CheckColumn(46,"JOB TITLE",fSpec.isJobTtlCol(), fSpec.getJobTtlColIndex(), null, null);
			CheckColumn(47,"PHONE",fSpec.isPhoneCol(), fSpec.getPhoneColIndex(), null, null);
			CheckColumn(48,"EMAIL",fSpec.isEmlCol(), fSpec.getEmlColIndex(), null, null);
			CheckColumn(49,"PayCFld1: " + fSpec.getPayCfld1Name(),fSpec.isPayCfld1Col(), fSpec.getPayCfld1ColIndex(), fSpec.getPayCfld1ParsePattern(), null);
			CheckColumn(50,"PayCFld2: " + fSpec.getPayCfld2Name(),fSpec.isPayCfld2Col(), fSpec.getPayCfld2ColIndex(), fSpec.getPayCfld2ParsePattern(), null);
			CheckColumn(51,"PayCFld3: " + fSpec.getPayCfld3Name(),fSpec.isPayCfld3Col(), fSpec.getPayCfld3ColIndex(), null, null);
			CheckColumn(52,"PayCFld4: " + fSpec.getPayCfld4Name(),fSpec.isPayCfld4Col(), fSpec.getPayCfld4ColIndex(), null, null);
			CheckColumn(53,"PayCFld5: " + fSpec.getPayCfld5Name(),fSpec.isPayCfld5Col(), fSpec.getPayCfld5ColIndex(), null, null);
			CheckColumn(54,"PayCFld6: " + fSpec.getPayCfld6Name(),fSpec.isPayCfld6Col(), fSpec.getPayCfld6ColIndex(), null, null);
			CheckColumn(55,"PayCFld7: " + fSpec.getPayCfld7Name(),fSpec.isPayCfld7Col(), fSpec.getPayCfld7ColIndex(), null, null);
		}
	}

