package com.etc.admin.ui.calc;

import java.io.Serializable;

import com.etc.corvetto.ems.calc.entities.CalculationRequest;
import com.etc.corvetto.ems.calc.entities.aca.Irs10945bCalculation;
import com.etc.corvetto.ems.calc.entities.aca.Irs10945cCalculation;
import com.etc.corvetto.ems.calc.entities.aca.air.Irs10945FilingCalculation;
import com.etc.corvetto.ems.calc.entities.utils.emp.EmployeeMergerCalculation;

/**
 * <p>
 * CalcQueue contains the objects required to create the CalculationQueue</p>
 * 
 * @author Greg Chaffins 10/29/2021
 * 
 */
public class CalcQueue implements Serializable
{
	private static final long serialVersionUID = 3395133415854326437L;

	private CalculationRequest request = null;
	private Irs10945bCalculation  irs10945b = null;
	private Irs10945cCalculation irs10945c = null;
	private Irs10945FilingCalculation irs10945Filing = null;
	private EmployeeMergerCalculation employeeMerger = null;
	
	public CalcQueue() {
		
	}
	
	public CalcQueue(CalculationRequest request) {
		this.request = request;
	}
	
	public CalculationRequest getRequest() { return request;}
	public void setRequest(CalculationRequest request) { this.request = request;}
	
	public Irs10945bCalculation getIrs10945b() { return irs10945b;}
	public void setIrs10945b(Irs10945bCalculation irs10945b) { this.irs10945b = irs10945b;}
	
	public Irs10945cCalculation getIrs10945c() { return irs10945c;}
	public void setIrs10945c(Irs10945cCalculation irs10945c) { this.irs10945c = irs10945c;}
	
	public Irs10945FilingCalculation getIrs10945Filing() { return irs10945Filing;}
	public void setIrs10945Filing(Irs10945FilingCalculation irs10945Filing) { this.irs10945Filing = irs10945Filing;}
	
	public EmployeeMergerCalculation getEmployeeMerger() { return employeeMerger;}
	public void setEmployeeMerger(EmployeeMergerCalculation employeeMerger) { this.employeeMerger = employeeMerger;}
	
}
