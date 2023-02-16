package com.etc.admin.ui.employer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.corvetto.entities.Contact;
import com.etc.corvetto.entities.EmployerContact;
import com.etc.corvetto.rqs.ContactRequest;
import com.etc.corvetto.rqs.EmployerContactRequest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewEmployerContactsController 
{

	@FXML
	private Label topLabel;
	@FXML
	private ListView<HBoxContactCell> dataList;
	@FXML
	private CheckBox InactiveCheck;
	@FXML
	private CheckBox deletedCheck;
	
	int waitingToComplete = 0;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	@FXML
	public void initialize() 
	{
		initControls();
		loadData();
	}
	
	private void initControls() 
	{
		dataList.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
            	DataManager.i().mEmployerContact = dataList.getSelectionModel().getSelectedItem().getContact();
            	DataManager.i().mContactType = 1;
				viewSelectedContact();
            }
        });
	}
	
	private void loadData() {
		// new thread 
		Task<Void> task = new Task<Void>()  
		{ 
            @Override 
            protected Void call() throws Exception
            { 
            	topLabel.setText("Contacts (loading...)");
        		EmployerContactRequest request = new EmployerContactRequest();
        		request.setId(DataManager.i().mEmployer.getId());
        		request.setEmployerId(DataManager.i().mEmployer.getId());
        		try {
        			DataManager.i().mEmployerContacts = AdminPersistenceManager.getInstance().getAll(request);
        		} catch (CoreException e) {
                	DataManager.i().log(Level.SEVERE, e); 
        		}
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
            	return null; 
            } 
        }; 
         
      	task.setOnScheduled(e -> 
      	{ 
      		EtcAdmin.i().setStatusMessage("Updating Contact Data..."); 
      		EtcAdmin.i().setProgress(0.25);
      	}); 
      			 
    	task.setOnSucceeded(e ->  showContacts()); 
    	task.setOnFailed(e -> {
	    	DataManager.i().log(Level.SEVERE,task.getException());
    		showContacts(); 
    	}); 

    	new Thread(task).start(); 
	}
	
	private void showContacts() 
	{
		String sName;

		if(DataManager.i().mEmployerContacts != null) 
		{
	        List<HBoxContactCell> contactList = new ArrayList<>();
			
			if(DataManager.i().mEmployerContacts.size() > 0)
				contactList.add(new HBoxContactCell(null,"Name", "Phone", "Email", true));
			
			Contact contact = null;

			for(EmployerContact employerContact : DataManager.i().mEmployerContacts) 
			{
				if(employerContact == null) continue;
				if(employerContact.isActive() == false && InactiveCheck.isSelected() == false) continue;
				if(employerContact.isDeleted() == true && deletedCheck.isSelected() == false) continue;

				ContactRequest conReq = new ContactRequest();
				conReq.setId(employerContact.getContactId());
				try {
					DataManager.i().mContact = AdminPersistenceManager.getInstance().get(conReq);
				} catch (CoreException e) {
		        	DataManager.i().log(Level.SEVERE, e); 
				}
			    catch (Exception e) {  DataManager.i().logGenericException(e); }

				if(employerContact.getContactId().equals(DataManager.i().mContact.getId()))
				{
					contact = DataManager.i().mContact;

					if(contact == null) continue;
					// create the employer address string for display
					sName = contact.getFirstName() + " " + contact.getLastName();
					contactList.add(new HBoxContactCell(employerContact, sName, contact.getPhone(), contact.getEmail(), false));
				}
			};	
	        ObservableList<HBoxContactCell> myObservableContactList = FXCollections.observableList(contactList);
	        dataList.setItems(myObservableContactList);		
	
	        //update our employer screen label
	        topLabel.setText("Contacts (total: " + String.valueOf(DataManager.i().mEmployerContacts.size()) + ")" );
        } else 
            topLabel.setText("Contacts (total: 0)");
		
  		EtcAdmin.i().setStatusMessage("Ready"); 
  		EtcAdmin.i().setProgress(0);
	}
	
	
	private void viewSelectedContact() 
	{
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/contact/ViewContact.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode)); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	
	
	@FXML
	private void onShowInactivesClick(ActionEvent event) {
		showContacts();
	}	
	
	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) dataList.getScene().getWindow();
		stage.close();
	}
	
	public class HBox2ColCell extends HBox 
	{
         Label lblCol1 = new Label();
         Label lblCol2 = new Label();

         HBox2ColCell(String sCol1, String sCol2, boolean headerRow)
         {
              super();
           
              if(sCol1 == null ) sCol1 = "";
              if(sCol2 == null ) sCol2 = "";
              
              if(sCol1.contains("null")) sCol1 = "";
              if(sCol2.contains("null")) sCol2 = "";

              lblCol1.setText(sCol1);
              lblCol1.setMinWidth(150);
              lblCol1.setMaxWidth(150);
              lblCol1.setPrefWidth(150);
              HBox.setHgrow(lblCol1, Priority.ALWAYS);

              lblCol2.setText(sCol2);
              lblCol2.setMinWidth(275);
              lblCol2.setMaxWidth(275);
              lblCol2.setPrefWidth(275);
              HBox.setHgrow(lblCol2, Priority.ALWAYS);

              // make the header row bold
              if(headerRow == true) {
            	  lblCol1.setTextFill(Color.GREY);
            	  lblCol2.setTextFill(Color.GREY);
              }
              
              this.getChildren().addAll(lblCol1, lblCol2);
         }
    }	

	public static class HBoxContactCell extends HBox 
	{
         Label lblName = new Label();
         Label lblPhone = new Label();
         Label lblEmail = new Label();
         EmployerContact contact = null;
         
         EmployerContact getContact() { return contact; }
         
         HBoxContactCell(EmployerContact contact, String sName, String sPhone, String sEmail, boolean headerRow) {
              super();
              
              this.contact = contact;

              if(sName == null ) sName = "";
              if(sPhone == null ) sPhone = "";
              if(sEmail == null ) sEmail = "";
              
              if(sName.contains("null")) sName = "";
              if(sPhone.contains("null")) sPhone = "";
              if(sEmail.contains("null")) sEmail = "";

              lblName.setText(sName);
              lblName.setMinWidth(200);
              lblName.setMaxWidth(200);
              lblName.setPrefWidth(200);
              HBox.setHgrow(lblName, Priority.ALWAYS);

              lblPhone.setText(sPhone);
              lblPhone.setMinWidth(100);
              lblPhone.setMaxWidth(100);
              lblPhone.setPrefWidth(100);
              HBox.setHgrow(lblPhone, Priority.ALWAYS);

              lblEmail.setText(sEmail);
              lblEmail.setMinWidth(200);
              lblEmail.setMaxWidth(200);
              lblEmail.setPrefWidth(200);
              HBox.setHgrow(lblEmail, Priority.ALWAYS);

              // make the header row bold
              if(headerRow == true) {
            	  lblName.setTextFill(Color.GREY);
            	  lblPhone.setTextFill(Color.GREY);;
            	  lblEmail.setTextFill(Color.GREY);
              }
              
        	  this.getChildren().addAll(lblName, lblPhone, lblEmail);
         }
    }	
	
	
}


