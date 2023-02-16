package com.etc.admin.ui.export;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.corvetto.ems.exp.entities.ExportChannel;
import com.etc.corvetto.ems.exp.entities.ExportRequest;
import com.etc.corvetto.ems.exp.entities.ExportSpecification;
import com.etc.corvetto.ems.exp.entities.aca.IrsAirCorrectionsExport;
import com.etc.corvetto.ems.exp.entities.aca.IrsAirFilingExport;
import com.etc.corvetto.ems.exp.entities.aca.IrsAirFilingStatusExport;
import com.etc.corvetto.ems.exp.rqs.ExportChannelRequest;
import com.etc.corvetto.ems.exp.rqs.ExportRequestRequest;
import com.etc.corvetto.ems.exp.rqs.ExportSpecificationRequest;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.User;
import com.etc.corvetto.rqs.EmployerRequest;
import com.etc.corvetto.rqs.UserRequest;

/**
 * <p>
 * ExportQueueData is a worker class designed to gather the data requirements 
 * for the  Export Queue</p>
 * 
 * @author Greg Chaffins 12/15/2022
 * 
 */
public class ExportQueueData implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -53311016976327211L;


	//lists for optimizations
	private List<Long> irsAirCorrectionsExportIds;
	private List<Long> irsAirFilingExportIds;
	private List<Long> irsAirFilingStatusExportIds;
	
	private List<IrsAirCorrectionsExport> irsAirCorrectionsExports;
	private List<IrsAirFilingExport> irsAirFilingExports;
	private List<IrsAirFilingStatusExport> irsAirFilingStatusExports;

	
	// default constructor
	public ExportQueueData() {
		
	}
	
	public List<ExportQueue> mExportQueue =  new ArrayList<ExportQueue>();
	public List<ExportRequest> mExportRequests = null;
	// get the most recent pipeline requests
	public void updateExportRequests(boolean useExpandedRange) throws SQLException, ParseException {
		//reset everything
		mExportQueue.clear();
		
		Calendar lastUpdate = null;
		lastUpdate = Calendar.getInstance();
		if (useExpandedRange == true)
			lastUpdate.add(Calendar.HOUR, -128);
		else
			lastUpdate.add(Calendar.HOUR, -72);
		
		try {
			// create the request
			ExportRequestRequest request = new ExportRequestRequest();
			// set the lastUpdated
			request.setLastUpdated(lastUpdate.getTimeInMillis());
			// get from the server
			mExportRequests = AdminPersistenceManager.getInstance().getAll(request);

			if (mExportRequests == null) return;
			
			// specification
			for (ExportRequest req  : mExportRequests){
				// specification
				if (req.getSpecification() == null || req.getSpecification().getName() == null || req.getSpecification().getName().isEmpty())
					req.setSpecification(getExportSpecification(req));
				
				// channel
				if (req.getSpecification() == null || req.getSpecification().getChannelId() == null) continue;
				if (req.getSpecification().getChannel() == null || req.getSpecification().getChannel().getName() == null || req.getSpecification().getChannel().getName().isEmpty())
					req.getSpecification().setChannel(getExportChannel(req));
				
				// user
				if (req.getCreatedBy() == null || req.getCreatedBy().getFirstName() == null || req.getCreatedBy().getFirstName().isEmpty()) {
					req.setCreatedBy(getUser(req));
				}
				
				// export id
				switch(req.getSpecification().getChannel().getId().intValue()) {
            	case 5:
            		irsAirFilingExportIds.add(req.getExportId());
            		break;
            	case 6:
            		irsAirFilingStatusExportIds.add(req.getExportId());
            		break;
            	case 7:
            		irsAirCorrectionsExportIds.add(req.getExportId());
            		break;
            	default:
            		break;		
				}
			}
			
			// need requests 
			// get the export objects
//			if (irsAirFilingExportIds.size() > 0) {
//				IrsAirFilingExportRequest eReq = new IrsAirFilingExportRequest();
//				eReq.setIdList(Irs1094XBCalcIds);
//				irsAirCorrectionsExports = AdminPersistenceManager.getInstance().getAll(eReq);
//			}

			// build the queue object collection
			for (ExportRequest req  : mExportRequests){
				ExportQueue eq = new ExportQueue();
				eq.setRequest(req);
				switch(req.getSpecification().getChannel().getId().intValue()) {
            	case 5:
            		for (IrsAirFilingExport afExport : irsAirFilingExports) {
            			if (afExport.getId().equals(req.getExportId())) {
            				eq.setIrsAirFilingExport(afExport);
            				break;
            			}
            		}
            		break;
            	case 6:
            		for (IrsAirFilingStatusExport afsExport : irsAirFilingStatusExports) {
            			if (afsExport.getId().equals(req.getExportId())) {
            				eq.setIrsAirFilingStatusExport(afsExport);
            				break;
            			}
            		}
            		break;
            	case 7:
            		for (IrsAirCorrectionsExport acExport : irsAirCorrectionsExports) {
            			if (acExport.getId().equals(req.getExportId())) {
            				eq.setIrsAirCorrectionsExport(acExport);
            				break;
            			}
            		}
            		break;
            	default:
            		break;		
				}
				
				mExportQueue.add(eq);
			}
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	}
	
	private ExportSpecification getExportSpecification(ExportRequest exportRequest) {
		if (exportRequest.getSpecificationId() == null) return null;
		
		ExportSpecification spec =  null;
		try {
			ExportSpecificationRequest specReq = new ExportSpecificationRequest();
			specReq.setId(exportRequest.getSpecificationId());
			spec = AdminPersistenceManager.getInstance().get(specReq);
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
		return spec;
	}
	
	private ExportChannel getExportChannel(ExportRequest exportRequest) {
		if (exportRequest.getSpecification().getChannelId() == null) return null;
	
		ExportChannel chnl =  null;
		try {
			ExportChannelRequest exReq = new ExportChannelRequest();
			exReq.setId(exportRequest.getSpecification().getChannelId());
			chnl = AdminPersistenceManager.getInstance().get(exReq);
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
		return chnl;
	}
	
	private User getUser(ExportRequest exportRequest) {
		if (exportRequest.getCreatedById() == null) return null;
		User user =  null;
		try {
			UserRequest userReq = new UserRequest();
			userReq.setId(exportRequest.getCreatedById());
			user = AdminPersistenceManager.getInstance().get(userReq);
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
		return user;
	}
	
	public Employer getEmployer(Long erId) {
		
		Employer er =  null;
		try {
			EmployerRequest erReq = new EmployerRequest();
			erReq.setId(erId);
			er = AdminPersistenceManager.getInstance().get(erReq);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		return er;
	}
}
