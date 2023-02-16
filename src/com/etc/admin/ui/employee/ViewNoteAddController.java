package com.etc.admin.ui.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.Note;
import com.etc.corvetto.rqs.AccountRequest;
import com.etc.corvetto.rqs.EmployeeRequest;
import com.etc.corvetto.rqs.NoteRequest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ViewNoteAddController {
	@FXML
	private ChoiceBox<String> noteTypeChoice;
	@FXML
	private TextArea noteTextArea;
	@FXML

	public boolean changesMade = false;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {

	}
	
	private boolean validateFields() {
		if (noteTextArea.getText().isEmpty() == true) return false;
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
		//set the note type
		note.setTypeId(31L);			
		noteRequest.setEntity(note);
		try { 
			note = AdminPersistenceManager.getInstance().addOrUpdate(noteRequest); }
		catch(CoreException e) { 
			Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE, "Exception.", e); 
		}
	    catch (Exception e) {  
	    	DataManager.i().logGenericException(e); 
	    }
		//check to make sure we were successful
		if (note == null) {
			Utils.alertUser("New Note Add Failure", "The system was unable to save the new note");
			return;
		}
		// update the request with the new note
		noteRequest.setEntity(note);
		noteRequest.setId(note.getId());
		List<NoteRequest> noteRequests = new ArrayList<NoteRequest>();
		
		updateEmployee(noteRequests);
		//mark changes made
		changesMade = true;
		// and exit the pop up
		exitPopup();
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


