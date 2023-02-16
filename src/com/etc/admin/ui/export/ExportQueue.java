package com.etc.admin.ui.export;

import java.io.Serializable;

import com.etc.corvetto.ems.exp.entities.ExportRequest;
import com.etc.corvetto.ems.exp.entities.aca.IrsAirCorrectionsExport;
import com.etc.corvetto.ems.exp.entities.aca.IrsAirFilingExport;
import com.etc.corvetto.ems.exp.entities.aca.IrsAirFilingStatusExport;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.Employer;

/**
 * <p>
 * ExportQueue contains the objects required to create the ExportQueue</p>
 * 
 * @author Greg Chaffins 12/15/2022
 * 
 */
public class ExportQueue implements Serializable
{
	private static final long serialVersionUID = 3395133415854326437L;

	private ExportRequest request = null;
	private Account account;
	private Employer employer;
	
	private IrsAirCorrectionsExport irsAirCorrectionsExport;
	private IrsAirFilingExport irsAirFilingExport;
	private IrsAirFilingStatusExport irsAirFilingStatusExport;

	
	public ExportQueue() {
		
	}
	
	public ExportQueue(ExportRequest request) {
		this.request = request;
	}
	
	public ExportRequest getRequest() { return request;}
	public void setRequest(ExportRequest request) { this.request = request;}

	public Account getAccount() { return account;}
	public void setAccount(Account account) { this.account = account;}

	public Employer getEmployer() { return employer;}
	public void setemployer(Employer employer) { this.employer = employer;}
	
	public IrsAirCorrectionsExport getIrsAirCorrectionsExport() { return irsAirCorrectionsExport;}
	public void setIrsAirCorrectionsExport(IrsAirCorrectionsExport irsAirCorrectionsExport) { this.irsAirCorrectionsExport = irsAirCorrectionsExport;}
	
	public IrsAirFilingExport getIrsAirFilingExport() { return irsAirFilingExport;}
	public void setIrsAirFilingExport(IrsAirFilingExport irsAirFilingExport) { this.irsAirFilingExport = irsAirFilingExport;}
	
	public IrsAirFilingStatusExport getIrsAirFilingStatusExport() { return irsAirFilingStatusExport;}
	public void setIrsAirFilingStatusExport(IrsAirFilingStatusExport irsAirFilingStatusExport) { this.irsAirFilingStatusExport = irsAirFilingStatusExport;}
	
}
