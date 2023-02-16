package com.etc.admin.ui.callcodes;

import java.io.Serializable;
import java.util.Calendar;

import com.etc.corvetto.ems.calc.embeds.CalculationInformation;
import com.etc.corvetto.ems.calc.entities.aca.Irs10945bCalculation;
import com.etc.corvetto.ems.calc.entities.aca.Irs10945cCalculation;

/**
 * <p>
 * helper class to collect code call history data across different types</p>
 * 
 * @author Greg Chaffins 9/13/2022
 * 
 */
public class CodeCallHistory implements Serializable
{
	private static final long serialVersionUID = 3395133415854326437L;

	Long id;
	private Calendar date;
	private String type;
	private String batchId;
	private CalculationInformation calcInfo;
	private String employerName;
	private String userName;
	
	private int irs1094Created = 0;
	private int irs1094Updated = 0;
	private int irs1095Created = 0;
	private int irs1095Updated = 0;
	private int taxYear = 0;
	
	public CodeCallHistory() {
		
	}
	
	public CodeCallHistory(Irs10945cCalculation cCalc, String employerName) {
		this.id = cCalc.getId();
		this.date = cCalc.getLastUpdated();
		this.batchId = cCalc.getBatchId();
		this.type = "Irs10945c";
		this.calcInfo = cCalc.getCalculationInformation();
		this.irs1094Created = cCalc.getIrs1094csCreated();
		this.irs1094Updated = cCalc.getIrs1094csUpdated();
		this.irs1095Created = cCalc.getIrs1095csCreated();
		this.irs1095Updated = cCalc.getIrs1095csUpdated();
		this.employerName = employerName;
		if (cCalc.getTaxYear() != null)
			this.taxYear = cCalc.getTaxYear().getYear();
		if (cCalc.getCreatedBy() != null) {
			userName = cCalc.getCreatedBy().getFirstName() + " " +  cCalc.getCreatedBy().getLastName().substring(0, 1);
		}
	}
	
	public CodeCallHistory(Irs10945bCalculation bCalc, String employerName) {
		this.id = bCalc.getId();
		this.date = bCalc.getLastUpdated();
		this.batchId = bCalc.getBatchId();
		this.type = "Irs10945b";
		this.calcInfo = bCalc.getCalculationInformation();
		this.irs1094Created = bCalc.getIrs1094bsCreated();
		this.irs1094Updated = bCalc.getIrs1094bsUpdated();
		this.irs1095Created = bCalc.getIrs1095bsCreated();
		this.irs1095Updated = bCalc.getIrs1095bsUpdated();
		this.employerName = employerName;
		if (bCalc.getCreatedBy() != null) {
			userName = bCalc.getCreatedBy().getFirstName() + " " +  bCalc.getCreatedBy().getLastName().substring(0, 1);
		}
	}
	
	public Long getId() { return id;}
	public void setId(Long id) { this.id = id;}
	
	public Calendar getDate() { return date;}
	public void setDate(Calendar date) { this.date = date;}
	
	public String getBatchId() { return batchId;}
	public void setBatchId(String batchId) { this.batchId = batchId;}
	
	public String getType() { return type;}
	public void setType(String type) { this.type = type;}
	
	public CalculationInformation getCalcInfo() { return calcInfo;}
	public void setCalcInfo(CalculationInformation calcInfo) { this.calcInfo = calcInfo;}
	
	public int getIrs1094Created() { return irs1094Created;}
	public void setIrs1094Created(int irs1094Created) { this.irs1094Created = irs1094Created;}
	
	public int getIrs1094Updated() { return irs1094Updated;}
	public void setIrs1094Updated(int irs1094Updated) { this.irs1094Updated = irs1094Updated;}
	
	public int getIrs1095Created() { return irs1095Created;}
	public void setIrs1095Created(int irs1095Created) { this.irs1095Created = irs1095Created;}
	
	public int getIrs1095Updated() { return irs1095Updated;}
	public void setIrs1095Updated(int irs1095Updated) { this.irs1095Updated = irs1095Updated;}
	
	public int getTaxYear() { return taxYear;}
	public void setTaxYear(int taxYear) { this.taxYear = taxYear;}
	
	public String getEmployerName() { return employerName;}
	public void setEmployerName(String employerName) { this.employerName = employerName;}
	
	public String getUserName() { return userName;}
	public void setUserName(String userName) { this.userName = userName;}
	
}
