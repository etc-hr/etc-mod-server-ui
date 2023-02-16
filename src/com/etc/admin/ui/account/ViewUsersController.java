package com.etc.admin.ui.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.User;
import com.etc.corvetto.rqs.UserRequest;

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

public class ViewUsersController 
{

	@FXML
	private Label topLabel;
	@FXML
	private ListView<HBoxAccountUserCell> listView;
	@FXML
	private CheckBox inactiveCheck;
	@FXML
	private CheckBox deletedCheck;
	
	//int to track any pending threads, used to properly update the progress and message
	int waitingToComplete = 0;
	List<User> users = null;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	@FXML
	public void initialize() 
	{
		initControls();
		updateAccountUsers();
	}
	
	private void initControls() 
	{
		listView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
            	viewSelectedUser();
            }
        });
	}
	

	private void updateAccountUsers() 
	{
		try
		{
			listView.getItems().clear();
			topLabel.setText("Users (loading ...)"); 
			// create a thread to handle the update
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception {
	            	UserRequest request = new UserRequest();
	            	request.setFetchInactive(true);
	            	request.setAccountId(DataManager.i().mAccount.getId());
	            	users = AdminPersistenceManager.getInstance().getAll(request);
	            	return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {
	      		EtcAdmin.i().setStatusMessage("Loading User Data...");
	      		EtcAdmin.i().setProgress(0.25);
	      	});
	      			
	    	task.setOnSucceeded(e ->  showUsers());
	    	
	    	task.setOnFailed(e ->  {
		    	DataManager.i().log(Level.SEVERE,task.getException());
		    	showUsers();
	    	});
	       
	    	EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e)
		{
			DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void showUsers() 
	{
  		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0);

  		//update the employees list
		if(users != null) {
			String sName;
			
			// clear anything already in the list
			listView.getItems().clear();
			List<HBoxAccountUserCell> list = new ArrayList<>();
			if(users.size() > 0)
				list.add(new HBoxAccountUserCell("Id", true, "Name", "Level", "Email", true));
			
			for (User user : users) {	
				// check for inactive
				if (user.isActive() == false && inactiveCheck.isSelected() == false) continue;
				if (user.isDeleted() == true && deletedCheck.isSelected() == false) continue;
				//create a full name for the list
				sName = user.getFirstName() + " " + user.getLastName(); 	
				
				list.add(new HBoxAccountUserCell(user.getId().toString(), user.isActive(), sName, user.getSecurityLevel().toString(), user.getEmail(), false));
			} 
			
	        ObservableList<HBoxAccountUserCell> myObservableList = FXCollections.observableList(list);
	        listView.setItems(myObservableList);		

			topLabel.setText("Account Users (total: " + String.valueOf(DataManager.i().mAccount.getUsers().size()) + ")" );
		} else {
			topLabel.setText("Account Users (total: 0)"); 
		}
	}

	private void viewSelectedUser() 
	{
		// offset one for the header row
		DataManager.i().setUser(listView.getSelectionModel().getSelectedIndex() - 1);
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/accountuser/ViewAccountUser.fxml"));
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
	private void onAddUser(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.USERADD, true);
	}	
	
	
	@FXML
	private void onInactivesClick(ActionEvent event) {
		showUsers();
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
	
	public class HBoxAccountUserCell extends HBox 
	{
         Label lblId = new Label();
         Label lblName = new Label();
         Label lblEmail = new Label();
         Label lblLevel = new Label();

         HBoxAccountUserCell(String id, boolean active, String name, String level, String email, boolean headerRow) 
         {
              super();

              //clean up any nulls
              if(name == null) 
            	  name = "";
              else
            	  name = name.replace("null", "");            	  
              if(email == null) email = "";
              if(level == null) level = "";
              
              lblId.setText(id);
              lblId.setMinWidth(50);
              lblId.setMaxWidth(50);
              lblId.setPrefWidth(50);
              HBox.setHgrow(lblId, Priority.ALWAYS);

              lblName.setText(name);
              lblName.setMinWidth(175);
              lblName.setMaxWidth(175);
              lblName.setPrefWidth(175);
              HBox.setHgrow(lblName, Priority.ALWAYS);

              lblLevel.setText(level);
              lblLevel.setMinWidth(75);
              lblLevel.setMaxWidth(75);
              lblLevel.setPrefWidth(75);
              HBox.setHgrow(lblLevel, Priority.ALWAYS);

              lblEmail.setText(email);
              lblEmail.setMinWidth(275);
              lblEmail.setMaxWidth(275);
              lblEmail.setPrefWidth(275);
              HBox.setHgrow(lblEmail, Priority.ALWAYS);

              if (active == false)
              {
            	  lblId.setTextFill(Color.BLUE);
            	  lblName.setTextFill(Color.BLUE);
            	  lblLevel.setTextFill(Color.BLUE);
            	  lblEmail.setTextFill(Color.BLUE);
              }
              
              if(headerRow == true) 
              {
            	  lblId.setTextFill(Color.GREY);
            	  lblName.setTextFill(Color.GREY);
            	  lblLevel.setTextFill(Color.GREY);
            	  lblEmail.setTextFill(Color.GREY);
              }
              
              this.getChildren().addAll(lblId, lblName, lblLevel, lblEmail );
         }
    }	
	
	
}


