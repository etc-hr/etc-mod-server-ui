package com.etc.admin.ui.user;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.User;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ViewUserController {
	
	@FXML
	private TextField usrFirstNameLabel;
	@FXML
	private TextField usrMiddleNameLabel;
	@FXML
	private TextField usrLastNameLabel;
	@FXML
	private TextField usrUserNameLabel;
	@FXML
	private TextField usrEmailLabel;
	@FXML
	private TextField usrPhoneLabel;
	@FXML
	private TextField usrPhoneTypeLabel;
	@FXML
	private TextField usrLastLoginLabel;
	@FXML
	private TextField usrSecurityLevelLabel;
	@FXML
	private Label usrUsersLabel;
	@FXML
	private ComboBox<String> usrUsersComboBox;
	@FXML
	private Button usrEditButton;
	@FXML
	private Button usrAddUserButton;
	@FXML
	private Label usrCoreIdLabel;
	@FXML
	private Label usrCoreActiveLabel;
	@FXML
	private Label usrCoreBODateLabel;
	@FXML
	private Label usrCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() 
	{
		//initialize the screen controls
		initControls();
		//try to load the screen if we have a user
		updateUserData();
	}
	
	private void initControls() 
	{
		//the user hasn't selected anything yet, so clear the screen
		clearScreen();
		//load the user list		
		loadUsers();
		usrUsersComboBox.setEditable(true);
		//set functionality according to the user security level
		usrEditButton.setDisable(!Utils.userCanEdit());
		usrAddUserButton.setDisable(!Utils.userCanAdd());
	}

	private void clearScreen()
	{
		//need to update the user screen before this can be used
		usrFirstNameLabel.setPromptText("Please Select A User");
		usrLastNameLabel.setText(" ");
		usrEmailLabel.setText(" ");
		usrPhoneLabel.setText(" ");
		usrPhoneTypeLabel.setText(" ");
		usrLastLoginLabel.setText(" ");

		usrFirstNameLabel.setEditable(false);
		usrLastNameLabel.setEditable(false);
		usrEmailLabel.setEditable(false);
		usrPhoneLabel.setEditable(false);
		usrPhoneTypeLabel.setEditable(false);
		usrLastLoginLabel.setEditable(false);

		usrCoreIdLabel.setText(" ");
		usrCoreActiveLabel.setText(" ");
		usrCoreBODateLabel.setText(" ");
		usrCoreLastUpdatedLabel.setText(" ");
		
		//disable the buttons until something is selected
		usrEditButton.setDisable(true);
	}

	public void loadUsers() 
	{
		String sName;
		ObservableList<String> userNames = FXCollections.observableArrayList();
		for (User user :  DataManager.i().mUsers) 
		{
			sName = user.getFirstName() + " " + user.getLastName();
			if (sName != null)
				userNames.add(sName);
		};		
		
		// use a filterlist to wrap the account names for combobox searching later
        FilteredList<String> filteredItems = new FilteredList<String>(userNames, p -> true);

        // add a listener for the edit control on the combobox
        usrUsersComboBox.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> {
            final javafx.scene.control.TextField editor = usrUsersComboBox.getEditor();
            final String selected = usrUsersComboBox.getSelectionModel().getSelectedItem();
            
            usrUsersComboBox.show();
            usrUsersComboBox.setVisibleRowCount(10);

            // Run on the GUI thread
            Platform.runLater(() -> 
            {
                if (selected == null || !selected.equals(editor.getText())) 
                {
                    filteredItems.setPredicate(item -> 
                    {
                        if (item.toUpperCase().contains(newvalue.toUpperCase()))
                        {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
		});
        
		//finally, set our filtered items as the combobox collection
        usrUsersComboBox.setItems(filteredItems);	
		
		//close the dropdown if it is showing
        usrUsersComboBox.hide();
	}
	
	public void onSearchHide() {
		// if we already have an account selected, show it
		if (usrUsersComboBox.getValue() != "")
			searchUsers();
	}
	
	public void onSearchUsers(ActionEvent event) {
		searchUsers();
	}

	public void searchUsers() {
		
		String sSelection = usrUsersComboBox.getValue();
		String sName;
		
		for (int i = 0; i < DataManager.i().mUsers.size();i++) {
			sName = DataManager.i().mUsers.get(i).getFirstName() + " " + DataManager.i().mUsers.get(i).getLastName() ;
			if (sName.equals(sSelection)) {			
				//load the user
				setUser(i);
				break;
			}
		}
	}	
		
	public void setUser(int nUserID) {
		
		if (DataManager.i().mUsers != null){
			DataManager.i().setUser(nUserID);
			updateUserData();
		}
	}
		
	public boolean updateUserData() {
			
			if (DataManager.i().mUser != null) {
				//enable the buttons
				usrEditButton.setDisable(false);

				Utils.updateControl(usrFirstNameLabel,DataManager.i().mUser.getFirstName());
				Utils.updateControl(usrMiddleNameLabel,DataManager.i().mUser.getMiddleName());
				Utils.updateControl(usrLastNameLabel,DataManager.i().mUser.getLastName());
				Utils.updateControl(usrUserNameLabel,DataManager.i().mUser.getUsername());
				Utils.updateControl(usrEmailLabel,DataManager.i().mUser.getEmail());
				Utils.updateControl(usrPhoneLabel,DataManager.i().mUser.getPhone());
				
				if (DataManager.i().mUser.getPhoneType() != null)
					Utils.updateControl(usrPhoneTypeLabel,DataManager.i().mUser.getPhoneType().toString());
				else
					usrPhoneTypeLabel.setText("");
				
				Utils.updateControl(usrLastLoginLabel,DataManager.i().mUser.getLastLogin());

				if (DataManager.i().mUser.getSecurityLevel() != null)
					Utils.updateControl(usrSecurityLevelLabel,DataManager.i().mUser.getSecurityLevel().toString());
				else
					usrSecurityLevelLabel.setText("");
				
				//core data read only
				Utils.updateControl(usrCoreIdLabel,String.valueOf(DataManager.i().mUser.getId()));
				Utils.updateControl(usrCoreActiveLabel,String.valueOf(DataManager.i().mUser.isActive()));
				Utils.updateControl(usrCoreBODateLabel,DataManager.i().mUser.getBornOn());
				Utils.updateControl(usrCoreLastUpdatedLabel,DataManager.i().mUser.getLastUpdated());
				
				return true;
			}
			
			return false;
	}		

	//extending the listview for our additional controls
	public class HBoxUserCell extends HBox {
         Label lblName = new Label();
         Label lblTitle1 = new Label();
         CheckBox cbAdmin = new CheckBox();
         
         Button btnView = new Button();

         HBoxUserCell(String sName, String sTitle1, Boolean bAdmin, String sButtonText, int nButtonID) {
              super();

              //clean up any nulls
              if (sName == null) 
            	  sName = "";
              else
            	  sName = sName.replace("null", "");            	  
              
              
              lblName.setText(sName);
              lblName.setMinWidth(500);
              lblName.setMaxWidth(500);
              lblName.setPrefWidth(500);
              HBox.setHgrow(lblName, Priority.ALWAYS);

              // if the button text is null, this is a header row
              if (sButtonText == null) {
            	  lblName.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblTitle1.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblTitle1.setText(sTitle1);
            	  lblTitle1.setMinWidth(110);
            	  lblTitle1.setMaxWidth(110);
            	  lblTitle1.setPrefWidth(110);
                  HBox.setHgrow(lblTitle1, Priority.ALWAYS);
            	  

                  this.getChildren().addAll(lblName, lblTitle1 );
              }else {
            	  
                  cbAdmin.setText("");
                  cbAdmin.setMinWidth(110);
                  cbAdmin.setMaxWidth(110);
                  cbAdmin.setPrefWidth(110);
                  cbAdmin.setDisable(true);
                  cbAdmin.setSelected(bAdmin);
            	  
              	  btnView.setText(sButtonText);
            	  btnView.setId(String.valueOf(nButtonID));
  
            	  this.getChildren().addAll(lblName, cbAdmin, btnView);
            	  
            	  //add an event handler for this button
            	  btnView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            		  
            		  @Override
            		  public void handle(MouseEvent event) {
            			  // let the application load the current secondary
            			  setUser(Integer.parseInt(btnView.getId()));
            		  }
            	  });
              }
         }
    }		
	
	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.USEREDIT, true);
	}	
	
	@FXML
	private void onAddUser(ActionEvent event) {
		//EtcAdmin.i().setScreen(ScreenType.USERADD);
	}	
}
