package com.etc.admin.ui.pipeline.queue;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils.LogType;
import com.etc.corvetto.ems.pipeline.entities.PipelineChannel;
import com.etc.corvetto.ems.pipeline.entities.PipelineRequest;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;
import com.etc.corvetto.ems.pipeline.entities.airrct.AirTranReceiptFile;
import com.etc.corvetto.ems.pipeline.entities.cov.CoverageFile;
import com.etc.corvetto.ems.pipeline.entities.ded.DeductionFile;
import com.etc.corvetto.ems.pipeline.entities.emp.EmployeeFile;
import com.etc.corvetto.ems.pipeline.entities.ins.InsuranceFile;
import com.etc.corvetto.ems.pipeline.entities.pay.PayFile;
import com.etc.corvetto.ems.pipeline.entities.ppd.PayPeriodFile;
import com.etc.corvetto.ems.pipeline.rqs.PipelineChannelRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineRequestRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.airrct.AirTranReceiptFileRequest;
import com.etc.corvetto.ems.pipeline.entities.c94.Irs1094cFile;
import com.etc.corvetto.ems.pipeline.entities.c95.Irs1095cFile;
import com.etc.corvetto.ems.pipeline.rqs.c94.Irs1094cFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.c95.Irs1095cFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.cov.CoverageFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.ded.DeductionFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.EmployeeFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.ins.InsuranceFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.PayFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.ppd.PayPeriodFileRequest;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.rqs.AccountRequest;
import com.etc.corvetto.rqs.UserRequest;
import com.etc.entities.CoreData;
import com.etc.utils.types.CustomActionType;

/**
 * <p>
 * QueueData is a worker class designed to gather the data requirements 
 * for the  PipelineQueue</p>
 * 
 * @author Greg Chaffins 6/8/2021
 * 
 */
public class QueueData implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -53311016976327211L;

	// default constructor
	public QueueData() {
		
	}
	
	public List<PipelineQueue> mPipelineQueue =  new ArrayList<PipelineQueue>();
	public List<PipelineRequest> mPipelineRequests = null;
	public PipelineQueue mPipelineQueueEntry = null;
	private List<Long> missingEntries = new ArrayList<Long>();
	private int hourRange = 24; // we increment this each time until it hits our range
	boolean resyncing = false;
	
	public boolean refreshPipelineRequests(int range){
		try {
			// update requests
			if (range > 96)
				hourRange = range;
			// uncomment below for deeper queue depth debugging
			// hourRange = 400;
			updatePipelineRequests();
		} catch (SQLException | ParseException e) {
        	DataManager.i().log(Level.SEVERE, e); 
			return false;
		}		
	    catch (Exception e) {  
	    	DataManager.i().logGenericException(e); 
	    	return false;	
	    }
		return true;
	}
	
	private void updatePipelineBaseObjects() throws CoreException, InterruptedException, SQLException, ParseException {
		EtcAdmin.i().updateStatus(.10, "Checking Base Objects...");
		DataManager.i().updateAccounts();
		DataManager.i().updateEmployers();
		DataManager.i().updatePipelineBaseObjects();
	}
	
	// get the most recent pipeline requests
	private <T extends CoreData> void updatePipelineRequests() throws SQLException, ParseException {
		//reset everything
		mPipelineQueue.clear();
		
		Calendar lastUpdate = null;
		lastUpdate = Calendar.getInstance();
		lastUpdate.add(Calendar.HOUR, -hourRange);
		
		// check to ee if we had a db reset. If so, reset the range and the flag
		if (EtcAdmin.i().mDbQueueReset == true) {
			EtcAdmin.i().mDbQueueReset = false;
			hourRange = 24;
		}
		
		hourRange += 24;
		if (hourRange > 168) hourRange = 168;
		
		try {
			// create the request
			PipelineRequestRequest request = new PipelineRequestRequest();
			// set the lastUpdated
			request.setLastUpdated(lastUpdate.getTimeInMillis());
			// get from the server
			DataManager.i().mPipelineRequests = AdminPersistenceManager.getInstance().getAll(request);

			// bail if we didn't have a return
			if(DataManager.i().mPipelineRequests  == null) return;
			//System.out.println("Request Count:" + DataManager.i().mPipelineRequests.size());
			
			HashMap<PipelineFileType, List<Long>> fileMap = new HashMap<PipelineFileType,List<Long>>();
			HashMap<PipelineFileType, List<? extends CoreData>> entityMap = new HashMap<PipelineFileType, List<? extends CoreData>>();
			
			// now build the pipeline queue entries
			for (PipelineRequest req : DataManager.i().mPipelineRequests ) {
				PipelineQueue queue = new PipelineQueue(req);
				//get the specification if needed
				if (req.getSpecification() == null) {
					PipelineSpecification specification = getSpecification(req);
					if (specification == null) continue;
					req.setSpecification(specification);
				}
				// get the channel if needed
				if (req.getSpecification().getChannel() == null) {
					PipelineChannel channel = getChannel(req);
					if (channel == null) continue;		
					req.getSpecification().setChannel(channel);
				}
				// get the account if needed for use later
				if (req.getEmployer().getAccount() == null) {
					Account account = getAccount(req.getEmployer().getAccountId());
					if (account == null) continue;
					req.getEmployer().setAccount(account);
				}
				// get the user if needed for use later
				if (req.getCreatedBy() == null && req.getCreatedById() != null && req.getCreatedById() != 0) {
					UserRequest userRequest = new UserRequest();
					userRequest.setId(req.getCreatedById());
					req.setCreatedBy(AdminPersistenceManager.getInstance().get(userRequest));
				}

				//set the file type
				queue.setFileType(req.getSpecification().getChannel().getType());
				
				//add record to map
				if(!fileMap.containsKey(queue.getFileType()))
					fileMap.put(queue.getFileType(), new ArrayList<Long>(Arrays.asList(queue.getRequest().getPipelineFileId())));
				else
					fileMap.get(queue.getFileType()).add(queue.getRequest().getPipelineFileId());
				// add it to our queue
				mPipelineQueue.add(queue);
			}
			
			
			if(!fileMap.isEmpty())
				for(Entry<PipelineFileType,List<Long>> map : fileMap.entrySet())
				{
					switch(map.getKey())
					{
					case EMPLOYEE:
						EmployeeFileRequest efrqs = new EmployeeFileRequest();
						efrqs.setIdList(map.getValue());
						entityMap.put(PipelineFileType.EMPLOYEE, AdminPersistenceManager.getInstance().getAll(efrqs));
						break;
					case COVERAGE:
						CoverageFileRequest cfrqs = new CoverageFileRequest();
						cfrqs.setIdList(map.getValue());
						entityMap.put(PipelineFileType.COVERAGE, AdminPersistenceManager.getInstance().getAll(cfrqs));
						break;
					case PAY:
						PayFileRequest pfrqs = new PayFileRequest();
						pfrqs.setIdList(map.getValue());
						entityMap.put(PipelineFileType.PAY, AdminPersistenceManager.getInstance().getAll(pfrqs));
						break;
					case DEDUCTION:
						DeductionFileRequest dfrqs = new DeductionFileRequest();
						dfrqs.setIdList(map.getValue());
						entityMap.put(PipelineFileType.DEDUCTION, AdminPersistenceManager.getInstance().getAll(dfrqs));
						break;
					case INSURANCE:
						InsuranceFileRequest ifrqs = new InsuranceFileRequest();
						ifrqs.setIdList(map.getValue());
						entityMap.put(PipelineFileType.INSURANCE, AdminPersistenceManager.getInstance().getAll(ifrqs));
						break;
					case IRS1094C:
						Irs1094cFileRequest irs1094crqs = new Irs1094cFileRequest();
						irs1094crqs.setIdList(map.getValue());
						entityMap.put(PipelineFileType.IRS1094C, AdminPersistenceManager.getInstance().getAll(irs1094crqs));
						break;
					case IRS1095C:
						Irs1095cFileRequest irs1095crqs = new Irs1095cFileRequest();
						irs1095crqs.setIdList(map.getValue());
						entityMap.put(PipelineFileType.IRS1095C, AdminPersistenceManager.getInstance().getAll(irs1095crqs));
						break;
					case IRSAIRRCPT:
						AirTranReceiptFileRequest artrqs = new AirTranReceiptFileRequest();
						artrqs.setIdList(map.getValue());
						entityMap.put(PipelineFileType.IRSAIRRCPT, AdminPersistenceManager.getInstance().getAll(artrqs));
						break;
					case PAYPERIOD:
						PayPeriodFileRequest ppdcrqs = new PayPeriodFileRequest();
						ppdcrqs.setIdList(map.getValue());
						entityMap.put(PipelineFileType.PAYPERIOD, AdminPersistenceManager.getInstance().getAll(ppdcrqs));
						break;
					default:
						break;
					}
				}
			
			if(!entityMap.isEmpty())
				for(PipelineQueue q : mPipelineQueue)
					if(entityMap.containsKey(q.getFileType()))
						for(CoreData d : entityMap.get(q.getFileType()))
							if(q.getRequest().getPipelineFileId().equals(d.getId()))
							{
								switch(q.getFileType()) 
								{
								case EMPLOYEE:
									q.setEmployeeFile((EmployeeFile)d);
									break;
								case COVERAGE:
									q.setCoverageFile((CoverageFile)d);
									break;
								case PAY:
									q.setPayFile((PayFile)d);
									break;
								case DEDUCTION:
									q.setDeductionFile((DeductionFile)d);
									break;
								case INSURANCE:
									q.setInsuranceFile((InsuranceFile)d);
									break;
								case IRS1094C:
									q.setIrs1094cFile((Irs1094cFile)d);
									break;
								case IRS1095C:
									q.setIrs1095cFile((Irs1095cFile)d);
									break;
								case IRSAIRRCPT:
									q.setAirTranReceiptFile((AirTranReceiptFile)d);
									break;
								case PAYPERIOD:
									q.setPayPeriodFile((PayPeriodFile)d);
									break;
								default:
									break;
								}
							}
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private PipelineSpecification getSpecification(PipelineRequest pipelineRequest) {
		PipelineSpecification spec =  null;
		try {
			PipelineSpecificationRequest specReq = new PipelineSpecificationRequest();
			specReq.setId(pipelineRequest.getSpecificationId());
			spec = AdminPersistenceManager.getInstance().get(specReq);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		return spec;
	}
	
	private PipelineChannel getChannel(PipelineRequest pipelineRequest) {
		PipelineChannel chnl =  null;
		try {
			PipelineChannelRequest pcReq = new PipelineChannelRequest();
			pcReq.setId(pipelineRequest.getSpecification().getChannelId());
			chnl = AdminPersistenceManager.getInstance().get(pcReq);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		return chnl;
	}
	
	private Account getAccount(Long  accountId) {
		Account account =  null;
		try {
			AccountRequest actReq = new AccountRequest();
			actReq.setId(accountId);
			account = AdminPersistenceManager.getInstance().get(actReq);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		return account;
	}
	
	public PipelineRequest rejectPipelineFile(PipelineQueue queueEntry) {
		PipelineRequest request = null;
		try {
			 PipelineRequestRequest preq = new PipelineRequestRequest();
			 preq.setActionType(CustomActionType.REJECTPFILE);
			 preq.setFileType(queueEntry.getFileType());
			 preq.setEntity(queueEntry.getRequest());
			 request = AdminPersistenceManager.getInstance().get(preq);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
		return request;
	}

	public void getMissingEntries()
	{
		// find the missing
		missingEntries.clear();
		for (int i =0; i < mPipelineQueue.size()-1; i++) {
			if (mPipelineQueue.get(i).getRequest().getId() + 1 != mPipelineQueue.get(i+1).getRequest().getId()) {
				for (Long missingId = mPipelineQueue.get(i).getRequest().getId() + 1; missingId < mPipelineQueue.get(i+1).getRequest().getId(); missingId++) {
					missingEntries.add(missingId);
					System.out.println("Missing: " + missingId);
				}
			}
		}
		
		// if we have missing entries, resync
		//if (missingEntries.size() > 0)
		//	updatePipelineRequests();
	}
	
}
