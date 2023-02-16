package com.etc.admin.ui.account;

import java.io.Serializable;
import java.util.Calendar;

import com.etc.corvetto.entities.AirTransmission;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.utils.types.AirMethodType;
import com.etc.corvetto.utils.types.AirOperationType;
import com.etc.corvetto.utils.types.AirStatusType;
import com.etc.corvetto.utils.types.FilingState;
import com.etc.corvetto.utils.types.FilingType;

/**
 * <p>
 * Transmission list helper class for display</p>
 * 
 * @author Greg Chaffins 2/09/2023
 * 
 */
public class TransmissionData implements Serializable
{
	private static final long serialVersionUID = 3395133415854326437L;

	private Employer employer = null;

	private String active;
	private FilingType filingType = FilingType.FEDERAL;
	private FilingState filingState = FilingState.NONE;
	private AirMethodType methodType = AirMethodType.A2A;
	private AirOperationType operationType = AirOperationType.AIR_ORIGINAL;			//REFLECTS CURRENT FORM SUBMISSION TYPE
	private AirStatusType status = AirStatusType.ETC_NEW;							//SHOULD REFLECT LAST TRANSMISSION RESULTS
	private int year = Calendar.getInstance().get(Calendar.YEAR);
	private String uniqTransId = null;
	private String originalReceiptId = null;				//ONLY APPLIES IF THE TRANSMISSION IS A REPLACEMENT
	private int irs1094FormCount = 0;						//THE TOTAL NUMBER OF 1094'S (1 PER SUBMISSION) IN THIS TRANSMISSION
	private int irs1095FormCount = 0;						//THE TOTAL NUMBER OF 1095'S IN THIS TRANSMISSION
	private Calendar transTimestamp = null;					//THE TRANSMISSION TIMESTAMP, USED ALL OVER THE PLACE
	private Calendar transTimestampExpiration = null;		//THERE IS A WINDOW WITHIN WHICH THE TRANSMISSION IS VALID, THIS IS THE END OF THAT WINDOW

	private String receiptId = null;
	private Calendar receiptTimestamp = null;
	
	// default constructor
	public TransmissionData() {
		
	}
	
	// overloaded constructor
	public TransmissionData(AirTransmission transmission, Employer employer) {
		this.employer = employer;
		filingType = transmission.getFilingType();
		filingState = transmission.getFilingState();
		methodType = transmission.getMethodType();
		operationType = transmission.getOperationType();
		status = transmission.getStatus();
		year = transmission.getYear();
		uniqTransId = transmission.getUniqTransId();
		originalReceiptId = transmission.getOriginalReceiptId();
		irs1094FormCount = transmission.getIrs1094FormCount();
		irs1095FormCount = transmission.getIrs1095FormCount();
		transTimestamp = transmission.getTransTimestamp();
		transTimestampExpiration = transmission.getTransTimestampExpiration();
		receiptId = transmission.getReceiptId();
		receiptTimestamp = transmission.getReceiptTimestamp();
	}
	
	public Employer getEmployer() { return employer;}
	public void setEmployer(Employer employer) { this.employer = employer;}
	
	public String getActive() { return active;}
	public void setActive(String active) { this.active = active;}
	
	public FilingState getFilingState() { return filingState; }
	public void setFilingState(FilingState filingState) { this.filingState = filingState; }

	public FilingType getFilingType() { return filingType; }
	public void setFilingType(FilingType filingType) { this.filingType = filingType; }

	public AirMethodType getMethodType() { return methodType; }
	public void setMethodType(AirMethodType methodType) { this.methodType = methodType; }

	public AirOperationType getOperationType() { return operationType; }
	public void setOperationType(AirOperationType operationType) { this.operationType = operationType; }

	public AirStatusType getStatus() { return status; }
	public void setStatus(AirStatusType status) { this.status = status; }

	public int getYear() { return year; }
	public void setYear(int year) { this.year = year; }

	public String getUniqTransId() { return uniqTransId; }
	public void setUniqTransId(String uniqTransId) { this.uniqTransId = uniqTransId; }

	public String getOriginalReceiptId() { return originalReceiptId; }
	public void setOriginalReceiptId(String originalReceiptId) { this.originalReceiptId = originalReceiptId; }

	public int getIrs1094FormCount() { return irs1094FormCount; }
	public void setIrs1094FormCount(int irs1094FormCount) { this.irs1094FormCount = irs1094FormCount; }

	public int getIrs1095FormCount() { return irs1095FormCount; }
	public void setIrs1095FormCount(int irs1095FormCount) { this.irs1095FormCount = irs1095FormCount; }

	public Calendar getTransTimestamp() { return transTimestamp; }
	public void setTransTimestamp(Calendar transTimestamp) { this.transTimestamp = transTimestamp; }

	public Calendar getTransTimestampExpiration() { return transTimestampExpiration; }
	public void setTransTimestampExpiration(Calendar transTimestampExpiration) { this.transTimestampExpiration = transTimestampExpiration; }

	public String getReceiptId() { return receiptId; }
	public void setReceiptId(String receiptId) { this.receiptId = receiptId; }

	public Calendar getReceiptTimestamp() { return receiptTimestamp; }
	public void setReceiptTimestamp(Calendar receiptTimestamp) { this.receiptTimestamp = receiptTimestamp; }

}