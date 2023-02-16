package com.etc.admin.ui.calc;

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
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.LogType;
import com.etc.corvetto.ems.calc.embeds.CalculationInformation;
import com.etc.corvetto.ems.calc.entities.CalculationChannel;
import com.etc.corvetto.ems.calc.entities.CalculationRequest;
import com.etc.corvetto.ems.calc.entities.CalculationSpecification;
import com.etc.corvetto.ems.calc.entities.aca.Irs10945bCalculation;
import com.etc.corvetto.ems.calc.entities.aca.Irs10945cCalculation;
import com.etc.corvetto.ems.calc.entities.aca.air.Irs10945FilingCalculation;
import com.etc.corvetto.ems.calc.entities.utils.emp.EmployeeMergerCalculation;
import com.etc.corvetto.ems.calc.rqs.CalculationChannelRequest;
import com.etc.corvetto.ems.calc.rqs.CalculationRequestRequest;
import com.etc.corvetto.ems.calc.rqs.CalculationSpecificationRequest;
import com.etc.corvetto.ems.calc.rqs.EmployeeMergerRequest;
import com.etc.corvetto.ems.calc.rqs.aca.Irs10945bCalculationRequest;
import com.etc.corvetto.ems.calc.rqs.aca.Irs10945cCalculationRequest;
import com.etc.corvetto.ems.calc.rqs.aca.air.Irs10945FilingCalculationRequest;
import com.etc.corvetto.entities.CoverageGroup;
import com.etc.corvetto.entities.Employee;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.TaxYear;
import com.etc.corvetto.entities.TaxYearServiceLevel;
import com.etc.corvetto.entities.User;
import com.etc.corvetto.rqs.EmployerRequest;
import com.etc.corvetto.rqs.TaxYearServiceLevelRequest;
import com.etc.corvetto.rqs.UserRequest;

/**
 * <p>
 * CalcQueueData is a worker class designed to gather the data requirements 
 * for the  Calculation Queue</p>
 * 
 * @author Greg Chaffins 10/29/2021
 * 
 */
public class CalcQueueData implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -53311016976327211L;

	// default constructor
	public CalcQueueData() {
		
	}
	
	public List<CalcQueue> mCalcQueue =  new ArrayList<CalcQueue>();
	public List<CalculationRequest> mCalcRequests = null;
	private List<Long> Irs1094XBCalcIds = new ArrayList<Long>();
	private List<Long> Irs1094XCCalcIds = new ArrayList<Long>();
	private List<Long> IrsFilingCalcIds = new ArrayList<Long>();
	private List<Long> EmployeeMergerIds = new ArrayList<Long>();

	private List<Irs10945bCalculation> Irs10945bCalculations = new ArrayList<Irs10945bCalculation>();
	private List<Irs10945cCalculation> Irs10945cCalculations = new ArrayList<Irs10945cCalculation>();
	private List<Irs10945FilingCalculation> IrsFilingCalculations = new ArrayList<Irs10945FilingCalculation>();
	private List<EmployeeMergerCalculation> EmployeeMergerCalculations = new ArrayList<EmployeeMergerCalculation>();
	
	// get the most recent pipeline requests
	public void updateCalcRequests(boolean useExpandedRange) throws SQLException, ParseException {
		//reset everything
		mCalcQueue.clear();
		Irs1094XBCalcIds.clear();
		Irs1094XCCalcIds.clear();
		IrsFilingCalcIds.clear();
		EmployeeMergerIds.clear();
		
		Irs10945bCalculations.clear();
		Irs10945cCalculations.clear();
		IrsFilingCalculations.clear();
		EmployeeMergerCalculations.clear();
		
		Calendar lastUpdate = null;
		lastUpdate = Calendar.getInstance();
		if (useExpandedRange == true)
			lastUpdate.add(Calendar.HOUR, -128);
		else
			lastUpdate.add(Calendar.HOUR, -72);
		
		try {
			// create the request
			CalculationRequestRequest request = new CalculationRequestRequest();
			// set the lastUpdated
			request.setLastUpdated(lastUpdate.getTimeInMillis());
			// get from the server
			mCalcRequests = AdminPersistenceManager.getInstance().getAll(request);

			if (mCalcRequests == null) return;
			
			// specification
			for (CalculationRequest req  : mCalcRequests){
				// specification
				if (req.getSpecification() == null || req.getSpecification().getName() == null || req.getSpecification().getName().isEmpty())
					req.setSpecification(getCalcSpecification(req));
				
				// channel
				if (req.getSpecification() == null || req.getSpecification().getChannelId() == null) continue;
				if (req.getSpecification().getChannel() == null || req.getSpecification().getChannel().getName() == null || req.getSpecification().getChannel().getName().isEmpty())
					req.getSpecification().setChannel(getCalcChannel(req));
				
				// user
				if (req.getCreatedBy() == null || req.getCreatedBy().getFirstName() == null || req.getCreatedBy().getFirstName().isEmpty()) {
					req.setCreatedBy(getUser(req));
				}
				
				// calculation id
				switch(req.getSpecification().getChannel().getId().intValue()) {
            	case 1:
            		Irs1094XCCalcIds.add(req.getCalculationId());
            		break;
            	case 2:
            		Irs1094XBCalcIds.add(req.getCalculationId());
            		break;
            	case 6:
            		EmployeeMergerIds.add(req.getCalculationId());
            		break;
            	case 7:
            		IrsFilingCalcIds.add(req.getCalculationId());
            		break;
            	default:
            		break;		
				}
			}
			
			// get the calculations
			if (Irs1094XBCalcIds.size() > 0) {
				Irs10945bCalculationRequest bReq = new Irs10945bCalculationRequest();
				bReq.setIdList(Irs1094XBCalcIds);
				Irs10945bCalculations = AdminPersistenceManager.getInstance().getAll(bReq);
			}

			if (Irs1094XCCalcIds.size() > 0) {
				Irs10945cCalculationRequest cReq = new Irs10945cCalculationRequest();
				cReq.setIdList(Irs1094XCCalcIds);
				Irs10945cCalculations = AdminPersistenceManager.getInstance().getAll(cReq);
			}
			
			if (IrsFilingCalcIds.size() > 0) {
				Irs10945FilingCalculationRequest fReq = new Irs10945FilingCalculationRequest();
				fReq.setIdList(IrsFilingCalcIds);
				IrsFilingCalculations = AdminPersistenceManager.getInstance().getAll(fReq);
			}

			if (EmployeeMergerIds.size() > 0) {
				EmployeeMergerRequest eReq = new EmployeeMergerRequest();
				eReq.setIdList(EmployeeMergerIds);
				EmployeeMergerCalculations = AdminPersistenceManager.getInstance().getAll(eReq);
			}
		
			// build the queue object collection
			for (CalculationRequest req  : mCalcRequests){
				CalcQueue cq = new CalcQueue();
				cq.setRequest(req);
				switch(req.getSpecification().getChannel().getId().intValue()) {
            	case 1:
            		for (Irs10945cCalculation cCalc : Irs10945cCalculations) {
            			if (cCalc.getId().equals(req.getCalculationId())) {
            				cq.setIrs10945c(cCalc);
            				break;
            			}
            		}
            		break;
            	case 2:
            		for (Irs10945bCalculation bCalc : Irs10945bCalculations) {
            			if (bCalc.getId().equals(req.getCalculationId())) {
            				cq.setIrs10945b(bCalc);
            				break;
            			}
            		}
            		break;
            	case 6:
            		for (EmployeeMergerCalculation eCalc : EmployeeMergerCalculations) {
            			if (eCalc.getId().equals(req.getCalculationId())) {
            				cq.setEmployeeMerger(eCalc);
            				break;
            			}
            		}
            		break;
            	case 7:
            		for (Irs10945FilingCalculation fCalc : IrsFilingCalculations) {
            			if (fCalc.getId().equals(req.getCalculationId())) {
            				cq.setIrs10945Filing(fCalc);
            				break;
            			}
            		}
            		break;
            	default:
            		break;		
				}
				
				mCalcQueue.add(cq);
			}
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private CalculationSpecification getCalcSpecification(CalculationRequest calcRequest) {
		if (calcRequest.getSpecificationId() == null) return null;
		
		CalculationSpecification spec =  null;
		try {
			CalculationSpecificationRequest specReq = new CalculationSpecificationRequest();
			specReq.setId(calcRequest.getSpecificationId());
			spec = AdminPersistenceManager.getInstance().get(specReq);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		return spec;
	}
	
	private CalculationChannel getCalcChannel(CalculationRequest calcRequest) {
		if (calcRequest.getSpecification().getChannelId() == null) return null;
	
		CalculationChannel chnl =  null;
		try {
			CalculationChannelRequest ccReq = new CalculationChannelRequest();
			ccReq.setId(calcRequest.getSpecification().getChannelId());
			chnl = AdminPersistenceManager.getInstance().get(ccReq);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		return chnl;
	}
	
	private User getUser(CalculationRequest calcRequest) {
		if (calcRequest.getCreatedById() == null) return null;
		User user =  null;
		try {
			UserRequest userReq = new UserRequest();
			userReq.setId(calcRequest.getCreatedById());
			user = AdminPersistenceManager.getInstance().get(userReq);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
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
	
	public boolean createIrs1094BCalculation(User user, TaxYear taxYear, boolean silent)
	{
		if (user == null) {
			Utils.alertUser("Calculation Request Failed", "The Calculation Request was missing the user information.");
			return false;
		}

		if (taxYear == null) {
			Utils.alertUser("Calculation Request Failed", "The Calculation Request was missing the tax year information.");
			return false;
		}

		try {
			// create
			Irs10945bCalculation bCalc = new Irs10945bCalculation();
			// set the calc spec id by the tax year service level
			if (taxYear.getServiceLevel() != null && taxYear.getServiceLevel().getSpecificationId() != null)
				bCalc.setSpecificationId(taxYear.getServiceLevel().getSpecificationId());
			else
				bCalc.setSpecificationId(2l);
			bCalc.setCreatedById(user.getId());
			bCalc.setCreatedBy(user);
			bCalc.setTaxYear(taxYear);
			bCalc.setTaxYearId(taxYear.getId());
			// calculation Information data
			bCalc.setCalculationInformation(new CalculationInformation());
			if (DataManager.i().mAccount != null) {
				bCalc.getCalculationInformation().setAccountId(DataManager.i().mAccount.getId());
				bCalc.getCalculationInformation().setAccount(DataManager.i().mAccount);
			} else {
				Utils.alertUser("Calculation Request Failed", "The Calculation Request was missing the Account Information.");
				return false;
			}
			if (DataManager.i().mEmployer != null) {
				bCalc.getCalculationInformation().setEmployerId(DataManager.i().mEmployer.getId());
				bCalc.getCalculationInformation().setEmployer(DataManager.i().mEmployer);
			} else {
				Utils.alertUser("Calculation Request Failed", "The Calculation Request was missing the Employer Information.");
				return false;
			}

			Irs10945bCalculationRequest bReq = new Irs10945bCalculationRequest();
			bReq.setEntity(bCalc);
			bCalc = AdminPersistenceManager.getInstance().addOrUpdate(bReq);
			// create the calc request
			if (bCalc != null) {
				CalculationRequest req = new CalculationRequest();
				req.setCreatedBy(user);
				req.setCreatedById(user.getId());
				req.setCalculationId(bCalc.getId());
				req.setDescription("Employer CallCodes requested by " + user.getFirstName() + " " + user.getLastName().substring(0, 1));
				// set the calc spec id by the tax year service level
				if (taxYear.getServiceLevel() != null && taxYear.getServiceLevel().getSpecificationId() != null)
					req.setSpecificationId(taxYear.getServiceLevel().getSpecificationId());
				else
					req.setSpecificationId(2l);
				CalculationRequestRequest request = new CalculationRequestRequest();
				request.setEntity(req);
				req = AdminPersistenceManager.getInstance().addOrUpdate(request);
				if (req != null) {
					if (silent == false)
						Utils.alertUser("Calculation Request Created", "The Calculation Request has been submitted.");
					return true;
				}
			} else {
				Utils.alertUser("Calculation Request Failed", "The Irs10945bCalculation could not be created");
				return false;
			}

		} catch (Exception e){
	      	DataManager.i().log(Level.SEVERE, e); 
		}
		
		Utils.alertUser("Calculation Request Failed", "The Calculation Request failed to create.");
		return false;
	}
 
	public boolean createIrs1094CCalculation(User user, TaxYear taxYear, Employer employer, Employee employee, boolean silent, CoverageGroup coverageGroup)
	{
		if (user == null) {
			Utils.alertUser("Calculation Request Failed", "The Calculation Request was missing the user information.");
			return false;
		}

		if (taxYear == null) {
			Utils.alertUser("Calculation Request Failed", "The Calculation Request was missing the tax year information.");
			return false;
		}

		try {
			// create the Irs1094bc
			Irs10945cCalculation cCalc = new Irs10945cCalculation();
			
			// fetch the service level
			TaxYearServiceLevelRequest tyslReq = new TaxYearServiceLevelRequest();
			tyslReq.setId(taxYear.getServiceLevelId());
			TaxYearServiceLevel serviceLevel = AdminPersistenceManager.getInstance().get(tyslReq);
			
			// set the calc spec id by the tax year service level
			if (serviceLevel != null && serviceLevel.getSpecificationId() != null) {
				cCalc.setSpecificationId(serviceLevel.getSpecificationId());
			}
			else
				cCalc.setSpecificationId(1l);
			
			cCalc.setCreatedById(user.getId());
			cCalc.setCreatedBy(user);
			cCalc.setTaxYear(taxYear);
			cCalc.setTaxYearId(taxYear.getId());
			cCalc.setCalculationInformation(new CalculationInformation());
			if (employee != null) {
				cCalc.setEmployeeId(employee.getId());
			}
			if (DataManager.i().mAccount != null) {
				cCalc.getCalculationInformation().setAccountId(DataManager.i().mAccount.getId());
				cCalc.getCalculationInformation().setAccount(DataManager.i().mAccount);
			} else {
				Utils.alertUser("Calculation Request Failed", "The Calculation Request was missing the Account Information.");
				return false;
			}
			if (employer != null) {
				cCalc.getCalculationInformation().setEmployerId(employer.getId());
				cCalc.getCalculationInformation().setEmployer(employer);
			} else {
				Utils.alertUser("Calculation Request Failed", "The Calculation Request was missing the Employer Information.");
				return false;
			}
			if (coverageGroup != null) {
				cCalc.setCoverageGroup(coverageGroup);
				cCalc.setCoverageGroupId(coverageGroup.getId());
			}
			Irs10945cCalculationRequest bReq = new Irs10945cCalculationRequest();
			bReq.setEntity(cCalc);
			cCalc = AdminPersistenceManager.getInstance().addOrUpdate(bReq);
			// create the calc request
			if (cCalc != null) {
				CalculationRequest req = new CalculationRequest();
				req.setCreatedBy(user);
				req.setCreatedById(user.getId());
				req.setCalculationId(cCalc.getId());
				if (employee != null)
					req.setDescription("Employee CallCodes requested by " + user.getFirstName() + " " + user.getLastName().substring(0, 1));
				else {
					if (coverageGroup != null)
						req.setDescription("CoverageClass CallCodes requested by " + user.getFirstName() + " " + user.getLastName().substring(0, 1));
					else
						req.setDescription("Employer CallCodes requested by " + user.getFirstName() + " " + user.getLastName().substring(0, 1));
				}
				// set the calc spec id by the tax year service level
				if (taxYear.getServiceLevel() != null && taxYear.getServiceLevel().getSpecificationId() != null)
					req.setSpecificationId(taxYear.getServiceLevel().getSpecificationId());
				else
					req.setSpecificationId(1l);
				CalculationRequestRequest request = new CalculationRequestRequest();
				request.setCreatedById(user.getId());
				request.setEntity(req);
				req = AdminPersistenceManager.getInstance().addOrUpdate(request);
				if (req != null) {
					if (silent == false)
						Utils.alertUser("Calculation Request Created", "The Calculation Request has been submitted.");
					return true;
				}
			} else {
				Utils.alertUser("Calculation Request Failed", "The Irs10945cCalculation could not be created");
				return false;
			}
		} catch (Exception e){
	      	DataManager.i().log(Level.SEVERE, e); 
		}
		
		Utils.alertUser("Calculation Request Failed", "The Calculation Request failed to create.");
		return false;
	}
 
	public void createIrs1094FilingCalculation(Employer employer, User user, TaxYear taxYear)
	{
		if (user == null) {
			Utils.alertUser("Filing Request Failed", "The Filing Request was missing the user information.");
			return;
		}

		if (taxYear == null) {
			Utils.alertUser("Filing Request Failed", "The Filing Request was missing the tax year information.");
			return;
		}

		if (employer == null) {
			Utils.alertUser("Filing Request Failed", "The Filing Request was missing the employer information.");
			return;
		}

		try {
			// create
			Irs10945FilingCalculation fCalc = new Irs10945FilingCalculation();
			if (taxYear.getServiceLevel() != null)
				fCalc.setSpecificationId(taxYear.getServiceLevel().getSpecificationId());
			else
				fCalc.setSpecificationId(8l);
			fCalc.setCreatedById(user.getId());
			fCalc.setCreatedBy(user);
			fCalc.setTaxYear(taxYear);
			fCalc.setTaxYearId(taxYear.getId());
			fCalc.setCalculationInformation(new CalculationInformation());
			if (fCalc.getCalculationInformation() != null) {
				fCalc.getCalculationInformation().setEmployer(employer);
				fCalc.getCalculationInformation().setEmployerId(employer.getId());
				fCalc.getCalculationInformation().setAccountId(employer.getAccountId());
				if (employer.getAccount() != null)
					fCalc.getCalculationInformation().setAccount(employer.getAccount());
			}
			
			Irs10945FilingCalculationRequest fReq = new Irs10945FilingCalculationRequest();
			fReq.setEntity(fCalc);
			fCalc = AdminPersistenceManager.getInstance().addOrUpdate(fReq);
			// create the calc request
			if (fCalc != null) {
				CalculationRequest req = new CalculationRequest();
				req.setCreatedBy(user);
				req.setCreatedById(user.getId());
				req.setCalculationId(fCalc.getId());
				// filing is calc spec 8
				req.setSpecificationId(8l);
				CalculationRequestRequest request = new CalculationRequestRequest();
				req.setDescription("Filing Calc requested by " + user.getFirstName() + " " + user.getLastName().substring(0, 1));
				request.setEntity(req);
				req = AdminPersistenceManager.getInstance().addOrUpdate(request);
				if (req != null) {
					Utils.alertUser("Filing Request Created", "The Filing Request has been submitted.");
					return;
				}
			} else {
				Utils.alertUser("Filing Request Failed", "The Irs10945FilingCalculation could not be created");
				return;
			}
		} catch (Exception e){
	      	DataManager.i().log(Level.SEVERE, e); 
		}
		
		Utils.alertUser("Calculation Request Failed", "The Calculation Request failed to create.");
	}
}
