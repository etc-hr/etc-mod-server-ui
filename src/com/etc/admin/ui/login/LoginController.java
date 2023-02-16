package com.etc.admin.ui.login;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.task.LoadingTask;
import com.etc.admin.task.LoginTask;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.CorvettoConnection;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginController {
	@FXML
	private VBox loginBox;
	@FXML
	private TextField emailField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Button loginButton;
	@FXML
	private Button terminateButton;
	@FXML
	private CheckBox saveUsernameCheckbox;
	@FXML
	private Label messageLabel;
	@FXML
	private VBox progressIndicatorBox;
	@FXML
	private ProgressIndicator progressIndicator;
	@FXML
	private Label progressIndicatorLabel;
	@FXML 
	private ProgressBar loginProgressBar;
	@FXML
	private Label versionLabel;
	
	Logger logr;
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		
		//init logger
		logr = Logger.getLogger(this.getClass().getCanonicalName());
		
		EtcAdmin.i().setLoginController(this);
		
		// update the version
		versionLabel.setText("Version " + EmsApp.getInstance().getApplicationProperties().getProperty(CorvettoConnection.APP_VSN, "0.0.0"));
		
		String username = null;
		try { username = EmsApp.getInstance().getProperties().getProperty(EmsApp.CFG_LEML); } 
		catch (CoreException e) { logr.log(Level.SEVERE, "Exception.", e); }

		if (username != null && username.isEmpty() == false) {
			emailField.setText(Utils.localDecryptString(username));
			// mark that we are remembering the username
			saveUsernameCheckbox.setSelected(true);
			// set focus to the password - need it to run later since the loading tack takes the thread
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
			    	passwordField.requestFocus();
			    }
			});
		}
		
		//hide the progress bar
		setProgress(0.0);
		// create the loading task
		LoadingTask loadingTask = new LoadingTask();
//		loadingTask.setOnSucceeded(success -> {
//			System.out.println("Loading: Pass");
//		});

		loadingTask.setOnFailed(fail -> {
			System.out.print("Loading: Fail -");
			System.out.println(loadingTask.getException().toString());
		});

		new Thread(loadingTask).start();
	}
	
	@FXML
	private void onTerminate(ActionEvent event) {
		//optional settings
		Platform.exit();
	}	

	@FXML
	public void testOrgLoginAction(ActionEvent event) {

	}

	@FXML
	public void login(ActionEvent event) {			

		passwordField.setDisable(true);
		emailField.setDisable(true);
		LoginTask loginTask = new LoginTask();
		loginTask.setOnSucceeded(success -> {
			//save the current user if selected, otherwise clear it
			if (saveUsernameCheckbox.isSelected()) {
				DataManager.i().saveCurrentUsername(Utils.localEncryptString(emailField.getText()));
			}
			else
				DataManager.i().saveCurrentUsername(null);
			//load main view
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(AdminManager.class.getResource("ui/main/Main.fxml"));
				Parent clientRootNode = loader.load();

				EtcAdmin.i().getPrimaryStage().setScene(new Scene(clientRootNode));
				EtcAdmin.i().setPrimaryStageMaximized();				
			} catch (IOException e) {
	        	DataManager.i().log(Level.SEVERE, e); 
				Platform.exit();
			}
		});

		loginTask.setOnFailed(fail -> {
			Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE, "Login Failed. Exception. ", loginTask.getException());
			setProgress(0.0);
			Utils.showAlert("Login Failure", "Incorrect credentials or other login failure.");
			passwordField.setDisable(false);
			emailField.setDisable(false);
			passwordField.setText("");
		});
		
		AdminManager.setEmail(emailField.getText());
		AdminManager.setPassword(passwordField.getText());
		new Thread(loginTask).start();
	}
	
	public void setProgress(double dProgress) {
		if (dProgress < 0.1) {
			loginProgressBar.setVisible(false);
			loginButton.setDisable(false);
		}
		else {
			loginProgressBar.setVisible(true);
			loginProgressBar.setProgress(dProgress);
			loginButton.setDisable(true);
		}
	}
}
