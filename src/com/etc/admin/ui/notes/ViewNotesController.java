package com.etc.admin.ui.notes;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Level;

import com.etc.admin.AdminApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.Employee;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.Note;
import com.etc.corvetto.rqs.NoteRequest;
import com.etc.entities.CoreData;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewNotesController 
{

	@FXML
	private Label topLabel;
	@FXML
	private ListView<HBoxNoteCell> listView;
	@FXML
	private TextArea textArea;
	@FXML
	private Button addButton;
	@FXML
	private TextField searchField;
	@FXML
	private Button searchButton;
	
	private ScreenType screenType;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	private int searchIndex = 0;
	
	@FXML
	public void initialize() 
	{
		initControls();
	}
	
	private void initControls() 
	{
		
		listView.setOnMouseClicked(mouseClickedEvent ->
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) {
            	showNoteText();
            }
        });
	}
	
	public void setScreenType(ScreenType screenType) {
		this.screenType = screenType;
		updateNotesData();
	}
	
	private void updateNotesData() 
	{
		listView.getItems().clear();
		// create a thread to handle the update
		Task<Void> task = new Task<Void>() 
		{
            @Override
            protected Void call() throws Exception {
            	NoteRequest request = new NoteRequest();
            	switch (screenType) {
            	case ACCOUNT:
                	request.setAccountId(DataManager.i().mAccount.getId());
                	Account account = DataManager.i().mAccount;
                	account.setNotes(AdminPersistenceManager.getInstance().getAll(request));
            		break;
            	case EMPLOYEE:
                	request.setEmployeeId(DataManager.i().mEmployee.getId());
                	Employee employee = DataManager.i().mEmployee;
                	employee.setNotes(AdminPersistenceManager.getInstance().getAll(request));
            		break;
            	case EMPLOYER:
                	request.setEmployerId(DataManager.i().mEmployer.getId());
                	Employer employer = DataManager.i().mEmployer;
                	employer.setNotes(AdminPersistenceManager.getInstance().getAll(request));
            		break;
            	default:
            		break;
            	}
            	
            	return null;
            }
        };
        
      	task.setOnScheduled(e ->  {
      		EtcAdmin.i().setStatusMessage("Updating Notes Data...");
      		EtcAdmin.i().setProgress(0.25);});
      			
    	task.setOnSucceeded(e ->  showNotes());
    	task.setOnFailed(e ->  showNotes());
    	
        //new Thread(task).start();
    	try {
			AdminApp.getInstance().getFxQueue().put(task);
		} catch (InterruptedException e) {
			DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void showNotes() 
	{
		// clear anything already in the list
		listView.getItems().clear();
		textArea.setText("");

    	switch (screenType) {
    	case ACCOUNT:
    		if(DataManager.i().mAccount.getNotes() != null && DataManager.i().mAccount.getNotes().size() > 0) 
    		{
    			for (Note note : DataManager.i().mAccount.getNotes()) 
    			{
    				if(note == null) continue;
    				listView.getItems().add(new HBoxNoteCell(note));
    			};	
    		}
    		break;
    	case EMPLOYEE:
    		if(DataManager.i().mEmployee.getNotes() != null && DataManager.i().mEmployee.getNotes().size() > 0) 
    		{
    			for (Note note : DataManager.i().mEmployee.getNotes()) 
    			{
    				if(note == null) continue;
    				listView.getItems().add(new HBoxNoteCell(note));
    			};	
    		}
    		break;
    	case EMPLOYER:
    		if(DataManager.i().mEmployer.getNotes() != null && DataManager.i().mEmployer.getNotes().size() > 0) 
    		{
    			for (Note note : DataManager.i().mEmployer.getNotes()) 
    			{
    				if(note == null) continue;
    				listView.getItems().add(new HBoxNoteCell(note));
    			};	
    		}
    		break;
    	default:
    		break;
    	}
		
		// sort descending
		listView.getItems().sort((o1,o2)->{ return Utils.compareDateStrings(o2.getDate(), o1.getDate()); });
		//add a header
		listView.getItems().add(0,new HBoxNoteCell(null));
		topLabel.setText("Notes (total: " + String.valueOf(listView.getItems().size() - 1) + ")" );

		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0);
	}
	
	private void showNoteText() {
    	if(listView.getSelectionModel().getSelectedItem()!= null && 
    			listView.getSelectionModel().getSelectedItem().getNote() != null)
    				textArea.setText(listView.getSelectionModel().getSelectedItem().getNote().getNote());
	}
	
	@FXML
	private void onAddNote(ActionEvent event) 
	{
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/notes/ViewNoteAdd.fxml"));
			Parent ControllerNode = loader.load();
	        ViewNoteAddController noteAddController = (ViewNoteAddController) loader.getController();
	        noteAddController.setScreenType(screenType);
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if(noteAddController.changesMade == true)
	        	updateNotesData();
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	

	@FXML
	private void onNoteSelection() 
	{
		showNoteText();
	}

	@FXML
	private void onFilterKey() {
		searchIndex = 0;
		searchButton.setText("Search");
	}
	
	@FXML
	private void onSearch(ActionEvent event) 
	{
		for (int pos = searchIndex; pos < listView.getItems().size(); pos++) {
			HBoxNoteCell cell = listView.getItems().get(pos);
			if (cell.getNote() == null) continue;
			if (cell.getNote().getNote().toUpperCase().contains(searchField.getText().toUpperCase())) {
				listView.getSelectionModel().select(cell);
				listView.scrollTo(pos);
				searchIndex = listView.getSelectionModel().getSelectedIndex() + 1;
				if (searchIndex > listView.getItems().size())
					searchIndex = 0;
				searchButton.setText("Next");

				showNoteText();
				return;
			}
		}
		
		//reset the search position
		searchIndex = 0;
		searchButton.setText("Search");
	}
	
	@FXML
	private void onShowSystemInfo() 
	{
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mNote;
			DataManager.i().mCurrentCoreDataType = SystemDataType.NOTE;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (IOException e) { DataManager.i().log(Level.SEVERE, e); }        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}

	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) listView.getScene().getWindow();
		stage.close();
	}
	
	public static class HBoxNoteCell extends HBox 
	{
         Label lblId = new Label();
         Label lblDate = new Label();
         Label lblType = new Label();
         Label lblTaxPeriod = new Label();
         Label lblEmployeeId = new Label();
         Label lblEmployeeName = new Label();
         Note note = null;

         public Note getNote() {
        	 return note;
         }
         
         public String getDate() {
        	 return lblDate.getText();
         }
           
         HBoxNoteCell(Note note) 
         {
              super();
              this.note = note;
              if(note != null) 
              {
               	  Utils.setHBoxLabel(lblId, 75, false, note.getId());
               	  Utils.setHBoxLabel(lblDate, 100, false, note.getLastUpdated());
               	  if(note.getType() != null)
               		  Utils.setHBoxLabel(lblType, 150, false, note.getType().getName().toString());
               	  else
               		  Utils.setHBoxLabel(lblType, 150, false, "");
               	  if (note.getTaxPeriod() != null)
               		  Utils.setHBoxLabel(lblTaxPeriod, 150, false, String.valueOf(note.getTaxPeriod().getYear()) + " - " 
               				  													+ note.getTaxPeriod().getFilingType().toString() + " " 
               				  													+ note.getTaxPeriod().getFilingState().toString());
               	  else
               		  Utils.setHBoxLabel(lblTaxPeriod, 150, false,"");
               	  if (note.getEmployeeId() != null)
               		Utils.setHBoxLabel(lblEmployeeId, 75, false, note.getEmployeeId());
               	  else
                 	Utils.setHBoxLabel(lblEmployeeId, 75, false,"");

               	  if (note.getEmployee() != null && note.getEmployee().getPerson() != null)
               		Utils.setHBoxLabel(lblEmployeeName, 150, false, note.getEmployee().getPerson().getLastName() + ", "  + note.getEmployee().getPerson().getFirstName()); 
               	  else
                 	Utils.setHBoxLabel(lblEmployeeName, 150, false, ""); 
              }else {
               	  Utils.setHBoxLabel(lblId, 75, true, "Id");
               	  Utils.setHBoxLabel(lblDate, 100, true, "Date");
               	  Utils.setHBoxLabel(lblType, 150, true, "Note Type");
               	  Utils.setHBoxLabel(lblTaxPeriod, 150, true, "Tax Period");
               	  Utils.setHBoxLabel(lblEmployeeId, 75, true, "Employee Id");
               	  Utils.setHBoxLabel(lblEmployeeName, 150, true, "Employee Name");
              }
              this.getChildren().addAll(lblId, lblDate, lblType, lblTaxPeriod, lblEmployeeId, lblEmployeeName);
         }
    }	
	
}


