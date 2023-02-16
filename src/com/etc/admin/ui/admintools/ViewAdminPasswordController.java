package com.etc.admin.ui.admintools;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.regex.Pattern;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.User;
import com.etc.corvetto.rqs.UserRequest;
import com.etc.utils.crypto.Cryptographer;
import com.etc.utils.crypto.CryptographyException;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ViewAdminPasswordController {
	
	@FXML
	private ListView<HBoxUserCell> lusrUserListView;
	// filter
	@FXML
	private TextField lusrFilterField;
	// User Information
	@FXML
	private TextField lusrFirstNameField;
	@FXML
	private TextField lusrMiddleNameField;
	@FXML
	private TextField lusrLastNameField;
	// security Information
	@FXML
	private TextField lusrUserNameField;
	@FXML
	private TextField lusrSecurityLevelField;
	@FXML
	private TextField lusrLastLoginField;
	@FXML
	private CheckBox lusrEtcStaffCheck;
	@FXML
	private CheckBox lusrWiseUserCheck;
	@FXML
	private CheckBox lusrV2UserCheck;
	// Contact Information
	@FXML
	private TextField lusrEmailField;
	@FXML
	private TextField lusrEmailTypeField;
	@FXML
	private TextField lusrPhoneField;
	@FXML
	private TextField lusrPhoneTypeField;
	// Change Password
	@FXML
	private Label luserPasswordLabel;
	@FXML
	private Button lusrChangePasswordButton;
	@FXML
	private Button luserSavePasswordButton;
	@FXML
	private Button luserSuggestPasswordButton;
	@FXML
	private Button luserCancelChangePasswordButton;
	@FXML
	private TextField luserPasswordField;
	// Core Data
	@FXML
	private Label lusrCoreIdField;
	@FXML
	private Label lusrCoreActiveField;
	@FXML
	private Label lusrCoreBODateField;
	@FXML
	private Label lusrCoreLastUpdatedField;
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	User selectedUser = null;
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		//update the users and load
		updateUsers();
	}
	
	private void initControls() {
		
		//hide things
		luserPasswordLabel.setVisible(false);
		luserPasswordField.setVisible(false);
		luserSavePasswordButton.setVisible(false);
		luserCancelChangePasswordButton.setVisible(false);
		luserSuggestPasswordButton.setVisible(false);

		// usr selection
		lusrUserListView.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) {
				// get the selected person
            	if(lusrUserListView.getSelectionModel().getSelectedItem() != null) {
            		selectedUser= lusrUserListView.getSelectionModel().getSelectedItem().getUser();
            		showSelectedUser();
            	}
            }
        });
	}
	
	private void updateUsers() 
	{
		try
		{
			// create a thread to handle the update
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception {
	    			try {
	    				UserRequest uReq = new UserRequest();
	    				DataManager.i().mUsers = AdminPersistenceManager.getInstance().getAll(uReq);
	    			} catch (CoreException e) {
	    	        	DataManager.i().log(Level.SEVERE, e); 
	    			}
	    		    catch (Exception e) {  DataManager.i().logGenericException(e); }
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {
	      		EtcAdmin.i().setStatusMessage("Updating Users...");
	      		EtcAdmin.i().setProgress(0.25);});
	      			
	    	task.setOnSucceeded(e ->  showUsers());
	    	task.setOnFailed(e ->  showUsers());
	    	
	    	EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e)
		{
			DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}

	private void showUsers() {
		// users blank? We have a problem
		if (DataManager.i().mUsers == null) {
			Utils.showAlert("User Load problem", "There was a problem loading the users. Please check the debug log for more information");;
			return;
		}
		lusrUserListView.getItems().clear();
		lusrUserListView.getItems().add(new HBoxUserCell(null));
		for (User user : DataManager.i().mUsers) {
			if (lusrFilterField.getText().isEmpty() == false) {
				String searchString = user.getFirstName() + " " + user.getLastName() + " " + user.getEmail();
				if (searchString.toLowerCase().contains(lusrFilterField.getText().toLowerCase()) == false)
					continue;
			}
			lusrUserListView.getItems().add(new HBoxUserCell(user));
		}
		
  		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0);
	}
	
	private void showSelectedUser(){
		if (selectedUser != null) {
			// User Information
			Utils.updateControl(lusrFirstNameField,selectedUser.getFirstName());
			Utils.updateControl(lusrMiddleNameField,selectedUser.getMiddleName());
			Utils.updateControl(lusrLastNameField,selectedUser.getLastName());
			Utils.updateControl(lusrUserNameField,selectedUser.getUsername());
			Utils.updateControl(lusrEmailField,selectedUser.getEmail());
			// Security Information
			if (selectedUser.getEmailType() != null)
				Utils.updateControl(lusrEmailTypeField,selectedUser.getEmailType().toString());
			else
				lusrEmailTypeField.setText(" ");
			if (selectedUser.getSecurityLevel() != null)
				Utils.updateControl(lusrSecurityLevelField,selectedUser.getSecurityLevel().toString());
			else
				lusrSecurityLevelField.setText("");
			Utils.updateControl(lusrLastLoginField,selectedUser.getLastLogin());
			Utils.updateControl(lusrEtcStaffCheck,selectedUser.isEtcStaff());
			Utils.updateControl(lusrWiseUserCheck,selectedUser.isWiseUser());
			Utils.updateControl(lusrV2UserCheck,selectedUser.isV2User());
			// Contact Information
			Utils.updateControl(lusrPhoneField,selectedUser.getPhone());
			if (selectedUser.getPhoneType() != null)
				Utils.updateControl(lusrPhoneTypeField,selectedUser.getPhoneType().toString());
			else
				lusrPhoneTypeField.setText("");
			Utils.updateControl(lusrEmailField,selectedUser.getEmail());
			if (selectedUser.getEmailType() != null)
				Utils.updateControl(lusrEmailTypeField,selectedUser.getEmailType().toString());
			else
				lusrEmailTypeField.setText("");
			//core data read only
			Utils.updateControl(lusrCoreIdField,String.valueOf(selectedUser.getId()));
			Utils.updateControl(lusrCoreActiveField,String.valueOf(selectedUser.isActive()));
			Utils.updateControl(lusrCoreBODateField,selectedUser.getBornOn());
			Utils.updateControl(lusrCoreLastUpdatedField,selectedUser.getLastUpdated());
		}
	}	
	
	@FXML
	private void onFilterKey() {
		showUsers();
	}
	
	@FXML
	private void onClearFilter(ActionEvent event) {
		//clear and reload
		lusrFilterField.setText("");
		showUsers();
	}
	
	@FXML
	private void onClose(ActionEvent event) {
		Stage stage = (Stage) lusrFirstNameField.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onChangePassword(ActionEvent event) {
		if (selectedUser == null) {
			Utils.showAlert("No Selected User", "Please select a user first.");
			return;
		}
		//show the controls
		luserPasswordLabel.setVisible(true);
		luserPasswordField.setVisible(true);
		luserSavePasswordButton.setVisible(true);
		luserSuggestPasswordButton.setVisible(true);
		luserCancelChangePasswordButton.setVisible(true);
	}
	
	@FXML
	private void onSuggestPassword(ActionEvent event) {
		// set a suggested password
		try {
			String pwd = Utils.generatePassword(9);
			while ( Utils.verifyPassword(pwd) == false)
				pwd = Utils.generatePassword(9);		
			luserPasswordField.setText(pwd);
		} catch (Exception e) {
			DataManager.i().log(Level.SEVERE, e); 
		}
	}
	
	@FXML
	private void onSavePassword(ActionEvent event) {
		if (luserPasswordField.getText() == null || luserPasswordField.getText().isEmpty()) {
			Utils.showAlert("Blank Password", "Please enter a valid password.");
			return;
		}
		
		String pwd = luserPasswordField.getText();	
		if (Utils.verifyPassword(pwd) == false) {
			Utils.showAlert("Password Not Acceptable", "Password must be at least 8 characters, not more than 20, contain upper and lower case, 1 number, 1 special character, and no white space.");
			return;
		}
		
		try {
			// encrypt the password
			String encpwd = Utils.encryptString(pwd);
			User usr = selectedUser;
			usr.setEncpwd(encpwd);
			// build the request
			UserRequest req = new UserRequest();
			req.setId(usr.getId());
			req.setEntity(usr);
			req.setChangeEncpwd(true);
			// and update
			usr = AdminPersistenceManager.getInstance().addOrUpdate(req);
			if (usr != null) Utils.showAlert("Password Updated", "The password has been updated.");
		} catch (CoreException e) {
			DataManager.i().log(Level.SEVERE, e);
			Utils.showAlert("Password Update Issue", "The password failed to update. Please check debug log for details.");
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	
		//hide things
		luserPasswordLabel.setVisible(false);
		luserPasswordField.setVisible(false);
		luserSavePasswordButton.setVisible(false);
		luserSuggestPasswordButton.setVisible(false);
		luserCancelChangePasswordButton.setVisible(false);
	}
	
	@FXML
	private void onCancelChangePassword(ActionEvent event) {
		//hide things
		luserPasswordLabel.setVisible(false);
		luserPasswordField.setVisible(false);
		luserSavePasswordButton.setVisible(false);
		luserSuggestPasswordButton.setVisible(false);
		luserCancelChangePasswordButton.setVisible(false);
	}
	
	public class HBoxUserCell extends HBox 
	{
        Label lblId = new Label();
        Label lblUser = new Label();
        Label lblName = new Label();
        Label lblEmail = new Label();
        User user;
        
        public User getUser() {
        	return user;
        }
        
        HBoxUserCell(User user) 
        {
             super();
          
             this.user = user;
             if(user != null) {
            	 Utils.setHBoxLabel(lblId, 75, false, user.getId());
            	 Utils.setHBoxLabel(lblName, 350, false, user.getLastName() + ", " + user.getFirstName());
            	 Utils.setHBoxLabel(lblEmail, 350, false, user.getEmail());
                 if(user.isActive() == false){
		           	  lblId.setTextFill(Color.BLUE);
		           	  lblName.setTextFill(Color.BLUE);
		           	  lblEmail.setTextFill(Color.BLUE);
                 }
             } else {
            	 Utils.setHBoxLabel(lblId, 100, true, "Id");
            	 Utils.setHBoxLabel(lblName, 300, true, "Name");
            	 Utils.setHBoxLabel(lblEmail, 150, true, "Email");
             }
             this.getChildren().addAll(lblId, lblName, lblEmail);
        }
   }	

}
