package com.etc.admin.ui.accountuser;

import java.io.IOException;
import java.util.logging.Level;

import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.entities.CoreData;
import com.etc.utils.types.EmailType;
import com.etc.utils.types.PhoneType;
import com.etc.utils.types.SecurityLevel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewAccountUserController 
{
	@FXML
	private GridPane secInputLabelGrid;
	@FXML
	private GridPane secInputLabelGrid1;
	@FXML
	private GridPane secInputLabelGrid11;
	@FXML
	private TextField lusrFirstNameLabel;
	@FXML
	private TextField lusrLastNameLabel;
	@FXML
	private TextField lusrEmailLabel;
	@FXML
	private ChoiceBox<EmailType> lusrEmailTypeLabel;
	@FXML
	private TextField lusrPhoneLabel;
	@FXML
	private ChoiceBox<PhoneType> lusrPhoneTypeLabel;
	@FXML
	private TextField lusrPasswordLabel;
	@FXML
	private DatePicker lusrLastLoginLabel;
	@FXML
	private ChoiceBox<SecurityLevel> lusrSecurityLevelLabel;
	@FXML
	private Label acctEtccoid;
	@FXML
	private Button lusrCloseButton;
	@FXML
	private Button lusrEditButton;
	@FXML
	private Button lusrSaveButton;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() 
	{
		//load the user data
		updateAccountUserData();
		initControls();
	}
	
	private void initControls() 
	{
		//set functionality according to the user security level
		lusrEditButton.setDisable(!Utils.userCanEdit());
		lusrSaveButton.setVisible(false);
		lusrEmailTypeLabel.setDisable(true);
		lusrPhoneTypeLabel.setDisable(true);
		lusrSecurityLevelLabel.setDisable(true);
		lusrLastLoginLabel.setDisable(true);
		
		//email type
		ObservableList<EmailType> emailType = FXCollections.observableArrayList(EmailType.values());
		lusrEmailTypeLabel.setItems(emailType);
		
		//phone type
		ObservableList<PhoneType> phoneTypes = FXCollections.observableArrayList(PhoneType.values());
		lusrPhoneTypeLabel.setItems(phoneTypes);

		//security level
		ObservableList<SecurityLevel> securityLevel = FXCollections.observableArrayList(SecurityLevel.values());
		lusrSecurityLevelLabel.setItems(securityLevel);
	}

	private void updateAccountUserData()
	{
		if (DataManager.i().mAccountUser!= null) 
		{
			Utils.updateControl(lusrFirstNameLabel,DataManager.i().mAccountUser.getFirstName());
			Utils.updateControl(lusrLastNameLabel,DataManager.i().mAccountUser.getLastName());
			Utils.updateControl(lusrEmailLabel,DataManager.i().mAccountUser.getEmail());
			Utils.updateControl(lusrPhoneLabel,DataManager.i().mAccountUser.getPhone());
			
			// email type
			if (DataManager.i().mAccountUser.getEmailType() != null) 
				lusrEmailTypeLabel.getSelectionModel().select(DataManager.i().mAccountUser.getEmailType());
			
			// phone type
			if (DataManager.i().mAccountUser.getPhoneType() != null) 
				lusrPhoneTypeLabel.getSelectionModel().select(DataManager.i().mAccountUser.getPhoneType());
			
			// security level
			if (DataManager.i().mAccountUser.getSecurityLevel() != null) 
				lusrSecurityLevelLabel.getSelectionModel().select(DataManager.i().mAccountUser.getSecurityLevel());
			
			//need to decrypt the password, maybe?
			Utils.updateControl(lusrPasswordLabel,DataManager.i().mAccountUser.getEncpwd());
			Utils.updateControl(lusrLastLoginLabel,DataManager.i().mAccountUser.getLastLogin());
		}
	}

/*	private void updateAccountUser(UpdateUserRequest request)
	{
		if (request != null) 
		{
			//update the request object with the tax year
			request.setUser(DataManager.i().mAccountUser);
			
			if (lusrEmailLabel.getText() != null && !lusrEmailLabel.getText().isEmpty())
				request.setEmail(lusrEmailLabel.getText());
			if (lusrLastLoginLabel.getValue() != null)
				request.setLastLogin(Utils.getCalDate(lusrLastLoginLabel.getValue()));
			request.setFirstName(lusrFirstNameLabel.getText());
			request.setLastName(lusrLastNameLabel.getText());
			request.setEmailType(EmailType.valueOf(lusrEmailTypeLabel.getValue().toString()));
			request.setPhone(lusrPhoneLabel.getText());
			request.setPhoneType(PhoneType.valueOf(lusrPhoneTypeLabel.getValue().toString()));
			request.setPassword(lusrPasswordLabel.getText());
			request.setSecurityLevel(SecurityLevel.valueOf(lusrSecurityLevelLabel.getValue().toString()));
		}
	}
*/
	
	@FXML
	private void onShowSystemInfo() 
	{
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mUser;
			DataManager.i().mCurrentCoreDataType = SystemDataType.USER;
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
	private void onSave(ActionEvent event)
	{
		// create a request
/*		UpdateUserRequest request = new UpdateUserRequest();
		// populate the request
		updateAccountUser(request);
		// send to the server
		DataManager.i().updateAccountUser(request);
*/		// and take everything out of edit mode
		setEditMode(false);
	}
	
	@FXML
	private void onEdit(ActionEvent event) 
	{
		if (lusrEditButton.getText().equals("Edit"))
			setEditMode(true);
		else
			setEditMode(false);
	}

	private void setEditMode(boolean mode)
	{
		if (mode == true) 
		{
			lusrEditButton.setText("Cancel");
			secInputLabelGrid.setStyle("-fx-background-color: #eeffee");
		}else {
			lusrEditButton.setText("Edit");
			secInputLabelGrid.getStyleClass().clear();
			secInputLabelGrid.setStyle(null);	
		}
		
		//Button
		lusrSaveButton.setVisible(mode);
		
		//Data
		lusrFirstNameLabel.setEditable(mode);
		lusrLastNameLabel.setEditable(mode);
		lusrEmailLabel.setEditable(mode);
		lusrEmailTypeLabel.setDisable(!mode);
		lusrPhoneLabel.setEditable(mode);
		lusrPhoneTypeLabel.setDisable(!mode);;
		lusrPasswordLabel.setEditable(mode);
		lusrLastLoginLabel.setDisable(!mode);
		lusrSecurityLevelLabel.setDisable(!mode);
	}	
	
	@FXML
	private void onClose(ActionEvent event) 
	{
		Stage stage = (Stage) secInputLabelGrid.getScene().getWindow();
		stage.close();
	}	
}
