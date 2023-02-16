package com.etc.admin.ui.localuser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.User;
import com.etc.corvetto.rqs.UserRequest;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewLocalUserController {
	
	@FXML
	private TextField lusrFirstNameLabel;
	@FXML
	private TextField lusrMiddleNameLabel;
	@FXML
	private TextField lusrLastNameLabel;
	@FXML
	private TextField lusrUserNameLabel;
	@FXML
	private TextField lusrEmailLabel;
	@FXML
	private TextField lusrEmailTypeLabel;
	@FXML
	private TextField lusrPhoneLabel;
	@FXML
	private TextField lusrPhoneTypeLabel;
	@FXML
	private TextField lusrLastLoginLabel;
	@FXML
	private TextField lusrSecurityLevelLabel;
	@FXML
	private TextField lusrSecurityLevel;
	@FXML
	private Button lusrResetDatabaseButton;
	@FXML
	private Button lusrShowLogButton;
	@FXML
	private CheckBox lusrDebugCheckbox;
	@FXML
	private Label lusrCoreIdLabel;
	@FXML
	private Label lusrCoreActiveLabel;
	@FXML
	private Label lusrCoreBODateLabel;
	@FXML
	private Label lusrCoreLastUpdatedLabel;
	// PASSWORD
	@FXML
	private Label luserPasswordLabel;
	@FXML
	private TextField luserPasswordField;
	@FXML
	private Button luserChangePasswordButton;
	@FXML
	private Button luserSavePasswordButton;
	@FXML
	private Button luserCancelChangePasswordButton;
	@FXML
	private Button luserSuggestPasswordButton;
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		//set the debug checkbox
		lusrDebugCheckbox.setSelected(EtcAdmin.mbDebug);

		// reset the password change
		luserPasswordLabel.setVisible(false);
		luserPasswordField.setVisible(false);
		luserSavePasswordButton.setVisible(false);
		luserSuggestPasswordButton.setVisible(false);
		luserCancelChangePasswordButton.setVisible(false);

		//load the user data
		updateLocalUserData();
	}
	
	private void updateLocalUserData(){
		if (DataManager.i().mLocalUser != null) {
			Utils.updateControl(lusrFirstNameLabel,DataManager.i().mLocalUser.getFirstName());
			Utils.updateControl(lusrMiddleNameLabel,DataManager.i().mLocalUser.getMiddleName());
			Utils.updateControl(lusrLastNameLabel,DataManager.i().mLocalUser.getLastName());
			Utils.updateControl(lusrUserNameLabel,DataManager.i().mLocalUser.getUsername());
			Utils.updateControl(lusrEmailLabel,DataManager.i().mLocalUser.getEmail());
			
			if (DataManager.i().mLocalUser.getEmailType() != null)
				Utils.updateControl(lusrEmailTypeLabel,DataManager.i().mLocalUser.getEmailType().toString());
			else
				lusrEmailTypeLabel.setText(" ");
			
			Utils.updateControl(lusrPhoneLabel,DataManager.i().mLocalUser.getPhone());
			
			if (DataManager.i().mLocalUser.getPhoneType() != null)
				Utils.updateControl(lusrPhoneTypeLabel,DataManager.i().mLocalUser.getPhoneType().toString());
			else
				lusrPhoneTypeLabel.setText("");
			
			if (DataManager.i().mLocalUser.getSecurityLevel() != null)
				Utils.updateControl(lusrSecurityLevelLabel,DataManager.i().mLocalUser.getSecurityLevel().toString());
			else
				lusrSecurityLevelLabel.setText("");

			Utils.updateControl(lusrLastLoginLabel,DataManager.i().mLocalUser.getLastLogin());
			
			//core data read only
			Utils.updateControl(lusrCoreIdLabel,String.valueOf(DataManager.i().mLocalUser.getId()));
			Utils.updateControl(lusrCoreActiveLabel,String.valueOf(DataManager.i().mLocalUser.isActive()));
			Utils.updateControl(lusrCoreBODateLabel,DataManager.i().mLocalUser.getBornOn());
			Utils.updateControl(lusrCoreLastUpdatedLabel,DataManager.i().mLocalUser.getLastUpdated());
			
		}
	}	
	
	@FXML
	public void onShowLog(ActionEvent event) {
        try {
  			FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/debuglog/ViewDebugLog.fxml"));
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
	public void onReconnect(ActionEvent event) {
		DataManager.i().reconnectCorvetto();
	}
	
	@FXML
	private void onEdit(ActionEvent event) {
		//update this user to be the current selected user
		if (DataManager.i().mLocalUser != null) {
			DataManager.i().mUser = DataManager.i().mLocalUser;
			
			//and load the edit screen
			EtcAdmin.i().setScreen(ScreenType.USEREDIT, true);
		}
	}	
	
	@FXML
	private void onResetLocalDatabase(ActionEvent event) {
		//confirm they want to log out
	    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Reset Local Database");
	    confirmAlert.setContentText("Are You Sure You Want to reset the local database? Thus will recreate "
	    		+ "all the local client tables and force data reload from the server. \n\nYou can continue in the app after the reset is complete.");

	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();
	    if ((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	return;
	    }
		// toggle the pipeline queue to reset its ramp up
	    EtcAdmin.i().mDbQueueReset = true;
        // Run on the GUI thread
        Platform.runLater(() -> {
        	showResettingAlert();
        });

//		EtcAdmin.i().exitApp();
	}
	
	private void showResettingAlert() {
		//show an alert
		Alert alert = new Alert(Alert.AlertType.WARNING, "Resetting Local Database. \n\nThis may take a minute...");
		alert.setTitle("Please wait");
		alert.initStyle(StageStyle.UNDECORATED);
		alert.setResult(ButtonType.OK);
		((Button)alert.getDialogPane().lookupButton(ButtonType.OK)).setDisable(true);
		
	    EtcAdmin.i().positionAlertCenter(alert);
		alert.show();
		
		try
		{
			EmsApp.getInstance().getFxQueue().put(new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					if(DataManager.i().ResetLocalDatabase())
					{
						Platform.runLater(() -> {
							((Button)alert.getDialogPane().lookupButton(ButtonType.OK)).setDisable(false);
							alert.setContentText("The database has been reset. \n\nYou may close this window and continue.");
						});
					} else {
						Platform.runLater(() -> {
							((Button)alert.getDialogPane().lookupButton(ButtonType.OK)).setDisable(false);
							alert.setContentText("Database Reset Failed. \n\nPlease try again later.");
						});
					}
					return null;
				}		
			});
		}catch(Exception e2) { 
			Platform.runLater(() -> {
				((Button)alert.getDialogPane().lookupButton(ButtonType.OK)).setDisable(false);
				((Button)alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Close App");
				((Button)alert.getDialogPane().lookupButton(ButtonType.OK)).setOnMouseClicked(e -> {
					EtcAdmin.i().exitApp();
				});
				alert.setContentText("Failed to reset database. \n\nPlease close and restart the app.");
			});
			DataManager.i().log(Level.SEVERE, e2); 
		}
		
	}
	
	@FXML
	public void onClose(ActionEvent event) {
		Stage stage = (Stage) lusrFirstNameLabel.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onDebug(ActionEvent event) {
		EtcAdmin.i().setDebug(lusrDebugCheckbox.isSelected());
	}
	
	@FXML
	private void onChangePassword(ActionEvent event) {
		//show the controls
		luserPasswordLabel.setVisible(true);
		luserPasswordField.setVisible(true);
		luserSavePasswordButton.setVisible(true);
		luserCancelChangePasswordButton.setVisible(true);
		luserSuggestPasswordButton.setVisible(true);
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
			User usr = DataManager.i().mLocalUser;
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
	private void onCancelChangePassword(ActionEvent event) {
		//hide things
		luserPasswordLabel.setVisible(false);
		luserPasswordField.setVisible(false);
		luserSavePasswordButton.setVisible(false);
		luserSuggestPasswordButton.setVisible(false);
		luserCancelChangePasswordButton.setVisible(false);
	}
	
	@FXML
	private void onLogout(ActionEvent event) {
		//confirm they want to log out
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Log out of Admin App");
	    alert.setContentText("Are You Sure You Want to Log Out?");
	    EtcAdmin.i().positionAlertCenter(alert);

	    Optional<ButtonType> result = alert.showAndWait();
	    if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
    		//restart the app to login again
    		EtcAdmin.i().restartApp();
	    }
	}

}
