package com.etc.admin.ui.employer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.corvetto.entities.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ViewEmployerUsersController 
{

	@FXML
	private Label topLabel;
	@FXML
	private ListView<HBox2ColCell> dataList;
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
		showUsers();
	}
	
	private void initControls() 
	{
		dataList.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				viewSelectedUser();
            }
        });
	}
	
	private void showUsers() 
	{
		if(DataManager.i().mEmployer.getUsers() != null)
		{
		    List<HBox2ColCell> userList = new ArrayList<>();
			
		    if(DataManager.i().mEmployer.getUsers().size() > 0)
		    	userList.add(new HBox2ColCell("First Name", "Last Name", true));
			
		    for(User user: DataManager.i().mEmployer.getUsers()) {
				if(user == null) continue;
				if(user.isActive() == false && InactiveCheck.isSelected() == false) continue;
				if(user.isDeleted() == true && deletedCheck.isSelected() == false) continue;
			
				userList.add(new HBox2ColCell(user.getFirstName(), user.getLastName(), false));
		    };	
			
			ObservableList<HBox2ColCell> myObservablePlanList = FXCollections.observableList(userList);
			dataList.setItems(myObservablePlanList);		
			
			//update our screen label
			topLabel.setText("Users (total: " + String.valueOf(DataManager.i().mEmployer.getUsers().size()) + ")" );
		} else {
			topLabel.setText("Users (total: 0)");			
		}
	}
	
	private void viewSelectedUser() 
	{
		// offset one for the header row
		DataManager.i().setUser(dataList.getSelectionModel().getSelectedIndex() - 1);
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/user/ViewUser.fxml"));
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

	
}


