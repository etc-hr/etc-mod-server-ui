package com.etc.admin.ui.taxyear; 
 
import java.io.Serializable; 
import java.util.ArrayList; 
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import com.etc.admin.EtcAdmin; 
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils; 
import com.etc.admin.utils.Utils.LogType; 
import com.etc.corvetto.ems.calc.entities.CalculationNotice; 
import com.etc.corvetto.ems.calc.entities.aca.Irs10945bCalculation; 
import com.etc.corvetto.ems.calc.entities.aca.Irs10945cCalculation; 
import com.etc.corvetto.ems.calc.rqs.CalculationNoticeRequest; 
import com.etc.corvetto.ems.exp.entities.ExportNotice; 
import com.etc.corvetto.ems.exp.rqs.ExportNoticeRequest; 
import com.etc.corvetto.entities.Irs1094Filing; 
import com.etc.corvetto.entities.Irs1094Submission; 
import com.etc.corvetto.entities.Irs1094b; 
import com.etc.corvetto.entities.Irs1094c; 
import com.etc.corvetto.entities.Irs1095Filing; 
import com.etc.corvetto.entities.Irs1095b; 
import com.etc.corvetto.entities.Irs1095c; 
import com.etc.corvetto.rqs.Irs1094FilingRequest; 
import com.etc.corvetto.rqs.Irs1094SubmissionRequest; 
import com.etc.corvetto.rqs.Irs1094bRequest; 
import com.etc.corvetto.rqs.Irs1094cRequest; 
import com.etc.corvetto.rqs.Irs1095FilingRequest; 
import com.etc.corvetto.rqs.Irs1095bRequest;
import com.etc.corvetto.rqs.Irs1095cRequest;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType; 
 
public class TaxYearFilingData  implements Serializable  
{ 
	/** 
	 *  
	 */ 
	private static final long serialVersionUID = -1100509310940506118L; 
 
	public TaxYearFilingData ()  
	{ 
		 
	} 
	 
	// data members 
	public Irs1094b mIrs1094 = null; 
	public Irs1095b mIrs1095b = null; 
	public Irs1095c mIrs1095c = null; 
	public Irs1095Filing mIrs1095Filing = null; 
	public Irs1094Submission mIrs1094Submission = null; 
	public List<Irs1094Filing> mIrs1094Filings = new ArrayList<Irs1094Filing>(); 
	public List<Irs1095Filing> mIrs1095Filings = new ArrayList<Irs1095Filing>(); 
	public List<Irs1094b> mIrs1094bs = new ArrayList<Irs1094b>(); 
	public List<Irs1094c> mIrs1094cs = new ArrayList<Irs1094c>(); 
	public List<Irs1095b> mIrs1095bs = new ArrayList<Irs1095b>(); 
	public List<Irs1095c> mIrs1095cs = new ArrayList<Irs1095c>(); 
	public List<Irs1094Submission> mIrs1094Submissions = new ArrayList<Irs1094Submission>(); 
	public List<CalculationNotice> mCalculationNotices = new ArrayList<CalculationNotice>(); 
	public List<ExportNotice> mExportNotices = new ArrayList<ExportNotice>(); 
	public List<Irs10945bCalculation> mIrs10945bCalculations = new ArrayList<Irs10945bCalculation>(); 
	public List<Irs10945cCalculation> mIrs10945cCalculations = new ArrayList<Irs10945cCalculation>(); 
	 
	//public List<Irs1094FilingCalculation>  
	public void refreshCurrentTaxYearFilingData() { 
		//Irs1094bs 
  		EtcAdmin.i().updateStatus(0.2, "Loading 1094b Data..."); 
		updateTaxYear1094bs(); 
		//Irs1094cs 
  		EtcAdmin.i().updateStatus(0.4, "Loading 1094c Data..."); 
		updateTaxYear1094cs(); 
		// Irs1094Filings 
  		EtcAdmin.i().updateStatus(0.5, "Loading 1094Filing Data..."); 
		updateTaxYear1094Filings(); 
		if(mIrs1094Filings == null) { 
	 		EtcAdmin.i().updateStatus(0, "No Irs1094Filing Data on Record"); 
	  		mIrs1095Filings = null; 
	  		mIrs1095bs = null; 
	  		mIrs1095cs = null; 
	  		mIrs1095Filing = null; 
	  		mIrs1095b = null; 
	  		mIrs1095c = null; 
	  		Utils.alertUser("No Irs1094Filing for Selected TaxYear", "Please Note: The selected TaxYear has no Irs1094 Filing Data on record. However, there could still be Irs1094b/c data records."); 
	  		return; 
		} 
		// Irs1095Filings 
		EtcAdmin.i().updateStatus(0.6, "Loading 1095Filing Data..."); 
  		EtcAdmin.i().setProgress(0.6); 
		updateTaxYear1095FilingsAbandoned(); 
		// Irs1095bs 
  		EtcAdmin.i().updateStatus(0.7, "Loading 1095b Data..."); 
 		updateTaxYear1095bs(); 
 		// 1094 submissions 
  	//	EtcAdmin.i().updateStatus(0.7, "Loading 1094 Submission Data..."); 
 	//	update1094Submissions(); 
  		// calc notices 
 		EtcAdmin.i().updateStatus(0.7, "Updating Calculation Notices..."); 
 		updateCalculationNotices(); 
  		EtcAdmin.i().updateStatus(0,"Ready"); 
		// Irs1095cs - not used as performance inprovement 
  		//EtcAdmin.i().updateStatus(0.8, "Loading 1095c Data..."); 
  		//EtcAdmin.i().setProgress(0.8); 
		//getUpdatedTaxYear1095cs(); 
	} 
	 
	private void updateTaxYear1094bs() { 
		try { 
			//retrieve from the server 
			Irs1094bRequest request = new Irs1094bRequest(); 
			request.setTaxYearId(DataManager.i().mTaxYear.getId()); 
			mIrs1094bs = AdminPersistenceManager.getInstance().getAll(request); 
		} catch (Exception e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	} 
	 
	private void updateTaxYear1094cs() { 
		try { 
			//retrieve from the server 
			Irs1094cRequest request = new Irs1094cRequest(); 
			request.setTaxYearId(DataManager.i().mTaxYear.getId()); 
			mIrs1094cs = AdminPersistenceManager.getInstance().getAll(request); 
			
			// testing
			//Irs1095cRequest cReq = new Irs1095cRequest();
			//cReq.setIrs1094cId(mIrs1094cs.get(0).getId());
			//cReq.setGetFormUnlocked(true);
			//mIrs1095cs = AdminPersistenceManager.getInstance().getAll(cReq); 
			
		} catch (Exception e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	} 
	 
	private void updateTaxYear1095bs() { 
		try { 
			Irs1095bRequest request = new Irs1095bRequest(); 
			request.setTaxYearId(DataManager.i().mTaxYear.getId()); 
			mIrs1095bs = AdminPersistenceManager.getInstance().getAll(request); 
		} catch (Exception e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	} 
	 
	private void updateTaxYear1094Filings() { 
		try { 
			//retrieve from the server 
			Irs1094FilingRequest request = new Irs1094FilingRequest(); 
			request.setTaxYearId(DataManager.i().mTaxYear.getId()); 
			mIrs1094Filings = AdminPersistenceManager.getInstance().getAll(request); 
		} catch (Exception e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	} 
	 
	private void updateTaxYear1095FilingsAbandoned() { 
		try { 
			Irs1095FilingRequest request = new Irs1095FilingRequest(); 
			request.setTaxYearId(DataManager.i().mTaxYear.getId()); 
			request.setAbandoned(true); 
			mIrs1095Filings = AdminPersistenceManager.getInstance().getAll(request); 
		} catch (Exception e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	} 
		 
	public void updateIrs1094Submissions(Irs1094Filing filing) { 
		try { 
			Irs1094SubmissionRequest request = new Irs1094SubmissionRequest(); 
			request.setIrs1094FilingId(filing.getId()); 
			mIrs1094Submissions = AdminPersistenceManager.getInstance().getAll(request); 
		} catch (Exception e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	}  
 
	public void updateCalculationNotices() { 
		try { 
			mCalculationNotices.clear(); 
			CalculationNoticeRequest noticeRequest = new CalculationNoticeRequest(); 
			noticeRequest.setTaxYearId(DataManager.i().mTaxYear.getId()); 
			noticeRequest.setEmployerId(DataManager.i().mEmployer.getId()); 
			mCalculationNotices = AdminPersistenceManager.getInstance().getAll(noticeRequest); 
		} catch (Exception e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	} 
 
	public void updateExportNotices(Irs1094Submission submission) { 
		try { 
			mExportNotices.clear(); 
			ExportNoticeRequest noticeRequest = new ExportNoticeRequest(); 
			noticeRequest.setIrs1094SubmissionId(submission.getId()); 
			mExportNotices = AdminPersistenceManager.getInstance().getAll(noticeRequest); 
		} catch (Exception e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	} 
	 
	public void getAllCalculationNotices() { 
		try { 
			CalculationNoticeRequest noticeRequest = new CalculationNoticeRequest(); 
			noticeRequest.setTaxYearId(DataManager.i().mTaxYear.getId()); 
			noticeRequest.setEmployerId(DataManager.i().mEmployer.getId()); 
			mCalculationNotices = AdminPersistenceManager.getInstance().getAll(noticeRequest); 
		} catch (Exception e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	} 

	public boolean isTaxYearLocked(){
		boolean locked = true;
		try { 
			// first, check to see if the 1094 is locked
			for ( Irs1094c irs1094c : mIrs1094cs) {
				if (irs1094c.isJanLocked() == false) locked = false;
				if (irs1094c.isFebLocked() == false) locked = false;
				if (irs1094c.isMarLocked() == false) locked = false;
				if (irs1094c.isAprLocked() == false) locked = false;
				if (irs1094c.isMayLocked() == false) locked = false;
				if (irs1094c.isJunLocked() == false) locked = false;
				if (irs1094c.isJulLocked() == false) locked = false;
				if (irs1094c.isAugLocked() == false) locked = false;
				if (irs1094c.isSepLocked() == false) locked = false;
				if (irs1094c.isOctLocked() == false) locked = false;
				if (irs1094c.isNovLocked() == false) locked = false;
				if (irs1094c.isDecLocked() == false) locked = false;
				// check each one
				if (locked == false) {
					return false;
				}
			}
		} catch (Exception e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
		
		return true;
	}
	
	public boolean isTaxYearMECUnflagged(){
		boolean flagged = true;
		try { 
			for ( Irs1094c irs1094c : mIrs1094cs) {
				if (irs1094c.isJanMECOffer() == false) flagged = false;
				if (irs1094c.isFebMECOffer() == false) flagged = false;
				if (irs1094c.isMarMECOffer() == false) flagged = false;
				if (irs1094c.isAprMECOffer() == false) flagged = false;
				if (irs1094c.isMayMECOffer() == false) flagged = false;
				if (irs1094c.isJunMECOffer() == false) flagged = false;
				if (irs1094c.isJulMECOffer() == false) flagged = false;
				if (irs1094c.isAugMECOffer() == false) flagged = false;
				if (irs1094c.isSepMECOffer() == false) flagged = false;
				if (irs1094c.isOctMECOffer() == false) flagged = false;
				if (irs1094c.isNovMECOffer() == false) flagged = false;
				if (irs1094c.isDecMECOffer() == false) flagged = false;
				// check each one
				if (flagged == false) {
					Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
				    confirmAlert.setTitle("MEC Unchecked");
				    String confirmText = "The 1094 for this tax year has an unchecked MEC. Do you wish to continue?";
				    confirmAlert.setContentText(confirmText);
				    EtcAdmin.i().positionAlertCenter(confirmAlert);
				    Optional<ButtonType> result = confirmAlert.showAndWait();

				    if((result.isPresent()) && (result.get() == ButtonType.OK))
				    	return true;
				    else
				    	return false;
				}
			}
		} catch (Exception e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
		
		return true;
	}
	
} 
