package com.etc.admin.ui.pipeline.queue;

import java.io.Serializable;

import com.etc.corvetto.ems.pipeline.entities.PipelineRequest;
import com.etc.corvetto.ems.pipeline.entities.airerr.AirTranErrorFile;
import com.etc.corvetto.ems.pipeline.entities.airrct.AirTranReceiptFile;
import com.etc.corvetto.ems.pipeline.entities.c94.Irs1094cFile;
import com.etc.corvetto.ems.pipeline.entities.c95.Irs1095cFile;
import com.etc.corvetto.ems.pipeline.entities.cov.CoverageFile;
import com.etc.corvetto.ems.pipeline.entities.ded.DeductionFile;
import com.etc.corvetto.ems.pipeline.entities.emp.EmployeeFile;
import com.etc.corvetto.ems.pipeline.entities.ins.InsuranceFile;
import com.etc.corvetto.ems.pipeline.entities.pay.PayFile;
import com.etc.corvetto.ems.pipeline.entities.ppd.PayPeriodFile;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;
import com.etc.entities.CoreSystem;
import com.etc.entities.Endpoint;

/**
 * <p>
 * PipelineQueue contains the objects required to create the PipelineQueue</p>
 * 
 * @author Greg Chaffins 6/18/2019
 * 
 */
public class PipelineQueue implements Serializable
{
	private static final long serialVersionUID = 3395133415854326437L;

	private PipelineFileType fileType = null;
	private PipelineRequest request = null;
	private CoreSystem coreSystem = null;
	private Endpoint endpoint = null;
	
	//keep some stats for convenience
	private Long units;
	private Long records;
	
	//at any given time only one file type will be populated
	private CoverageFile coverageFile = null;
	private EmployeeFile employeeFile = null;
	private PayFile payFile = null;
	private PayPeriodFile payPeriodFile = null;
	//private PlanFile planFile = null;
	private AirTranErrorFile airTranErrorFile = null;
	private AirTranReceiptFile airTranReceiptFile = null;
	private Irs1094cFile irs1094cFile = null;
	private Irs1095cFile irs1095cFile = null;
	private DeductionFile deductionFile = null;
	private InsuranceFile insuranceFile = null;
	
	public PipelineQueue() {
		
	}
	
	public PipelineQueue(PipelineRequest request) {
		this.request = request;
	}
	
	public Long getUnits() { return units;}
	public void setUnits(Long units) { this.units = units;}

	public Long getRecords() { return records;}
	public void setRecords(Long records) { this.records = records;}

	public PipelineFileType getFileType() { return fileType;}
	public void setFileType(PipelineFileType fileType) { this.fileType = fileType;}

	public PipelineRequest getRequest() { return request;}
	public void setRequest(PipelineRequest request) { this.request = request;}
	
	public CoreSystem getCoreSystem() { return coreSystem;}
	public void setCoreSystem(CoreSystem coreSystem) { this.coreSystem = coreSystem;}

	public Endpoint getEndpoint() { return endpoint;}
	public void setEndpoint(Endpoint endpoint) { this.endpoint = endpoint;}

	public CoverageFile getCoverageFile() { return coverageFile;}
	public void setCoverageFile(CoverageFile coverageFile) { this.coverageFile = coverageFile;}

	public EmployeeFile getEmployeeFile() { return employeeFile;}
	public void setEmployeeFile(EmployeeFile employeeFile) { this.employeeFile = employeeFile;}
	
	public PayFile getPayFile() { return payFile;}
	public void setPayFile(PayFile payFile) { this.payFile = payFile;}
	
	public DeductionFile getDeductionFile() { return deductionFile;}
	public void setDeductionFile(DeductionFile deductionFile) { this.deductionFile = deductionFile;}
	
	public InsuranceFile getInsuranceFile() { return insuranceFile;}
	public void setInsuranceFile(InsuranceFile insuranceFile) { this.insuranceFile = insuranceFile;}
	
	public PayPeriodFile getPayPeriodFile() { return payPeriodFile;}
	public void setPayPeriodFile(PayPeriodFile payPeriodFile) { this.payPeriodFile = payPeriodFile;}

	//public PlanFile getPlanFile() { return planFile;}
	//public void setPlanFile(PlanFile planFile) { this.planFile = planFile;}

	public AirTranErrorFile getAirTranErrorFile() { return airTranErrorFile;}
	public void setAirTranErrorFile(AirTranErrorFile airTranErrorFile) { this.airTranErrorFile = airTranErrorFile;}
	
	public AirTranReceiptFile getAirTranReceiptFile() { return airTranReceiptFile;}
	public void setAirTranReceiptFile(AirTranReceiptFile airTranReceiptFile) { this.airTranReceiptFile = airTranReceiptFile;}
	
	public Irs1094cFile getIrs1094cFile() { return irs1094cFile;}
	public void setIrs1094cFile(Irs1094cFile irs1094cFile) { this.irs1094cFile = irs1094cFile;}
	
	public Irs1095cFile getIrs1095cFile() { return irs1095cFile;}
	public void setIrs1095cFile(Irs1095cFile irs1095cFile) { this.irs1095cFile = irs1095cFile;}
}