package com.etc.admin.ui.notes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.Employee;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.Note;
import com.etc.corvetto.entities.NoteType;
import com.etc.corvetto.entities.TaxPeriod;
import com.etc.corvetto.rqs.AccountRequest;
import com.etc.corvetto.rqs.EmployeeRequest;
import com.etc.corvetto.rqs.NoteRequest;
import com.etc.corvetto.rqs.NoteTypeRequest;
import com.etc.corvetto.rqs.TaxPeriodRequest;
import com.etc.corvetto.rqs.TaxYearRequest;
import com.etc.corvetto.utils.types.FilingState;
import com.etc.corvetto.utils.types.FilingType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewNoteAddController {
	@FXML
	private ComboBox<String> noteTypeCombo;
	@FXML
	private TextArea noteTextArea;
	@FXML
	private TextField yearField;
	@FXML
	private ComboBox<String> taxPeriodCombo;

	public boolean changesMade = false;
	// this dialog is used by both account and employee.
	public boolean typeAccount = true;
	
	private List<TaxPeriod> taxPeriods = new ArrayList<TaxPeriod>();
	private List<NoteType> noteTypes = new ArrayList<NoteType>();
	private ScreenType screenType;

	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {

	}
	
	public void setScreenType(ScreenType screenType) {
		this.screenType = screenType;
		initControls();
	}
	
	private void initControls() {
		//need a selection of taxyears for this account
		try {
			
			TaxPeriodRequest request = new TaxPeriodRequest();
			request.setOrganizationId(DataManager.i().mLocalUser.getOrganizationId());
			taxPeriods = AdminPersistenceManager.getInstance().getAll(request);
			Collections.sort(taxPeriods, (o1, o2) -> (Long.valueOf(o2.getYear()).compareTo(Long.valueOf(o1.getYear())))); 
			for (TaxPeriod tp : taxPeriods) {
				if (tp.getFilingType().equals(FilingType.FEDERAL) == false) continue;
				taxPeriodCombo.getItems().add(String.valueOf(tp.getYear()));
			}	
			
			NoteTypeRequest ntRequest = new NoteTypeRequest();
			List<NoteType> nts = AdminPersistenceManager.getInstance().getAll(ntRequest);
			Collections.sort(nts, (o1, o2) -> (o1.getName().compareTo(o2.getName()))); 
			
        	switch (screenType) {
        	case ACCOUNT:
    			for (NoteType nt : nts) {
    				if (nt.getType().name().equals("ACCOUNT"))
    					noteTypes.add(nt);
    			}
        		break;
        	case EMPLOYEE:
    			for (NoteType nt : nts) {
    				if (nt.getType().name().equals("EMPLOYEE"))
    					noteTypes.add(nt);
    			}
        		break;
        	case EMPLOYER:
    			for (NoteType nt : nts) {
    				if (nt.getType().name().equals("EMPLOYER"))
    					noteTypes.add(nt);
    			}
        		break;
        	default:
        		return;
        	}
			
			for (NoteType nt : noteTypes) {
				noteTypeCombo.getItems().add(nt.getName());
			}
			
		} catch (Exception e) {
			DataManager.i().log(Level.SEVERE, e); 
		}
		
	}
	
	private boolean validateFields() {
		if (noteTextArea.getText().isEmpty() == true) return false;
		if (noteTypeCombo.getValue() == null || noteTypeCombo.getValue().isEmpty()) {
			Utils.showAlert("Note Type Selection", "Please select a note type before saving");
			return false;
		}
		//if (Utils.validateFloatTextField(noteTypeChoice) == false) return false;
		return true;
	}
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate
		if (validateFields() == false) return;
		
		// create the Note request to add it
		NoteRequest noteRequest = new NoteRequest();
		Note note = new Note();
		note.setNote(noteTextArea.getText());
		// set the tax year if one is selected

		if (taxPeriodCombo.getValue() != null && taxPeriodCombo.getValue().isEmpty() == false) {
			 int tpy = Integer.valueOf(taxPeriodCombo.getSelectionModel().getSelectedItem());
			for (TaxPeriod tp : taxPeriods) {
				// only federals in the collection
				if (tp.getFilingType().equals(FilingType.FEDERAL) == false) continue;
				if (tp.getYear() == tpy) {
					note.setTaxPeriod(tp);
					note.setTaxPeriodId(tp.getId());
					break;
				}
			}	
		}
		
		NoteType nt = noteTypes.get(noteTypeCombo.getSelectionModel().getSelectedIndex());
		note.setType(nt);
		note.setTypeId(nt.getId());
		note.setAccount(DataManager.i().mAccount);
		note.setAccountId(DataManager.i().mAccount.getId());
		noteRequest.setAccountId(DataManager.i().mAccount.getId());
		noteRequest.setNoteTypeId(nt.getId());
		noteRequest.setUserId(DataManager.i().mLocalUser.getId());
    	switch (screenType) {
    	//case ACCOUNT:
		//	note.setAccount(DataManager.i().mAccount);
		//	note.setAccountId(DataManager.i().mAccount.getId());
		//	noteRequest.setAccountId(DataManager.i().mAccount.getId());
    	//	break;
    	case EMPLOYEE:
			note.setEmployer(DataManager.i().mEmployer);
			note.setEmployerId(DataManager.i().mEmployer.getId());
			noteRequest.setEmployerId(DataManager.i().mEmployer.getId());
			note.setEmployee(DataManager.i().mEmployee);
			note.setEmployeeId(DataManager.i().mEmployee.getId());
			noteRequest.setEmployeeId(DataManager.i().mEmployee.getId());
    		break;
    	case EMPLOYER:
			note.setEmployer(DataManager.i().mEmployer);
			note.setEmployerId(DataManager.i().mEmployer.getId());
			noteRequest.setEmployerId(DataManager.i().mEmployer.getId());
    		break;
    	default:
    		break;
    	}
		
		
		noteRequest.setEntity(note);	
		try { 
			note = AdminPersistenceManager.getInstance().addOrUpdate(noteRequest); 
		} catch (Exception e) {  
			DataManager.i().log(Level.SEVERE, e);  
		}
		//check to make sure we were successful
		if (note == null) {
			Utils.alertUser("New Note Add Failure", "The system was unable to save the new note");
			return;
		}
		
		
		// update the request with the new note
/*		noteRequest.setEntity(note);
		noteRequest.setId(note.getId());
		List<NoteRequest> noteRequests = new ArrayList<NoteRequest>();
		
		//now update the realtionships
		if (typeAccount == true)
			updateAccount(noteRequests);
		else
			updateEmployee(noteRequests);
*/		//mark changes made
		changesMade = true;
		// and exit the pop up
		exitPopup();
	}
	
	private void updateAccount(List<NoteRequest> noteRequests) {
		try
		{
			AccountRequest acctRequest = new AccountRequest();
			acctRequest.setEntity(DataManager.i().mAccount);
			acctRequest.setId(DataManager.i().mAccount.getId());
			acctRequest.setNoteRequests(noteRequests);
			AdminPersistenceManager.getInstance().addOrUpdate(acctRequest);
		}catch(CoreException e)
		{
			Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE, "Exception.", e);
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void updateEmployee(List<NoteRequest> noteRequests) {
		try
		{
			EmployeeRequest empRequest = new EmployeeRequest();
			empRequest.setEntity(DataManager.i().mEmployee);
			empRequest.setId(DataManager.i().mEmployee.getId());
			empRequest.setNoteRequests(noteRequests);
			AdminPersistenceManager.getInstance().addOrUpdate(empRequest);
		}catch(CoreException e)
		{
			Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE, "Exception.", e);
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	
	private void exitPopup() {
		changesMade = true;
		Stage stage = (Stage) noteTextArea.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		exitPopup();
	}	
}


