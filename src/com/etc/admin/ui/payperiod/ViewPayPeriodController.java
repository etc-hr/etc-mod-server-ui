package com.etc.admin.ui.payperiod;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;

import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.PayPeriod;
import com.etc.corvetto.rqs.PayPeriodRequest;
import com.etc.corvetto.utils.types.PayFrequencyType;
import com.etc.entities.CoreData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewPayPeriodController 
{
	@FXML
	private Label ppHeaderLabel;
	@FXML
	private GridPane ppGridPane;
	@FXML
	private CheckBox ppMarkInactiveCheck;
	@FXML
	private TextField ppEmployerRefField;
	@FXML
	private ChoiceBox<PayFrequencyType> ppPayFreqTypeCombo;
	// dates
	@FXML
	private DatePicker ppStartDatePicker;
	@FXML
	private DatePicker ppEndDatePicker;
	@FXML
	private DatePicker ppPayDatePicker;
	// butons
	@FXML
	private Button ppEditButton;
	@FXML
	private Button ppSaveButton;
	
	private boolean addMode = false;
	public boolean changesMade = false; 
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {

	}
	
	public void load() 
	{
		try {
			// initialize the controls
			initControls();
			if (addMode == false) 
			{
				//update the dialog data
				showPayPeriod();
			}
		}catch (Exception e) {
			DataManager.i().log(Level.SEVERE, e); 
		}
	}

	private void initControls() 
	{
		//set functionality according to the user security level
		ppEditButton.setDisable(!Utils.userCanEdit());
		
		ppMarkInactiveCheck.setSelected(false);
		
		ppPayFreqTypeCombo.getItems().clear();
		
		ObservableList<PayFrequencyType> payFreqTypes = FXCollections.observableArrayList(PayFrequencyType.values());
		ppPayFreqTypeCombo.getItems().setAll(payFreqTypes);
		//ObservableList<String>
		//for (PayFrequencyType pft : PayFrequencyType.values())
		//	ppPayFreqTypeCombo.getItems().add(pft.toString());
		Collections.sort(ppPayFreqTypeCombo.getItems(), (o1, o2) -> (o1.toString().compareTo(o2.toString()))); 
	
	}
	
	public void setEditState(int state)
	{
		boolean editState  = false;
		ppGridPane.getStyleClass().clear();
		ppGridPane.setStyle(null);	
		addMode = false;

		switch (state) 
		{
			case 0: // no edit, no add
				ppEditButton.setText("Edit");
				ppEditButton.setVisible(true);
				ppMarkInactiveCheck.setVisible(false);
				break;
			case 1: // edit
				editState = true;
				ppSaveButton.setText("Save");
				ppEditButton.setText("Cancel");
				ppEditButton.setVisible(true);
				ppGridPane.setStyle("-fx-background-color: #eeffee");
				ppMarkInactiveCheck.setVisible(true);
				break;
			case 2: // add
				editState = true;
				addMode = true;
				ppSaveButton.setText("Add");
				ppEditButton.setVisible(false);
				ppMarkInactiveCheck.setVisible(false);
				ppGridPane.setStyle("-fx-background-color: #eeffee");
				Utils.updateControl(ppHeaderLabel,"Add PayPeriod");
				break;
			default:
				break;
		}
		
		ppPayFreqTypeCombo.setDisable(!editState);
		ppStartDatePicker.setDisable(!editState);
		ppEndDatePicker.setDisable(!editState);
		ppPayDatePicker.setDisable(!editState);
		ppEmployerRefField.setDisable(!editState);	
		ppSaveButton.setVisible(editState);
	}
	
	private void showPayPeriod()
	{
		if (DataManager.i().mPayPeriod == null) return;
		PayPeriod payPeriod = DataManager.i().mPayPeriod;
		
		Utils.updateControl(ppHeaderLabel,"PayPeriod");
		Utils.updateControl(ppEmployerRefField,payPeriod.getEmployerReference());
		
		if (payPeriod.getPayFrequencyType() != null) 
			ppPayFreqTypeCombo.getSelectionModel().select(payPeriod.getPayFrequencyType());
		
		Utils.updateControl(ppStartDatePicker, payPeriod.getStartDate());
		Utils.updateControl(ppEndDatePicker, payPeriod.getEndDate());
		Utils.updateControl(ppPayDatePicker, payPeriod.getPayDate());
	}	
	
	private void updatePayPeriod(PayPeriod payPeriod)
	{
		if (payPeriod == null) return;
		
		payPeriod.setEmployerReference(ppEmployerRefField.getText());
		payPeriod.setPayFrequencyType(ppPayFreqTypeCombo.getSelectionModel().getSelectedItem());
		payPeriod.setStartDate(Utils.getCalDate(ppStartDatePicker));
		payPeriod.setEndDate(Utils.getCalDate(ppEndDatePicker));
		payPeriod.setPayDate(Utils.getCalDate(ppPayDatePicker));
	}	
	
	@FXML
	private void onShowSystemInfo()
	{
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mPayPeriod;
			DataManager.i().mCurrentCoreDataType = SystemDataType.PAYPERIOD;
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
	private void onEdit(ActionEvent event) 
	{
		if (ppEditButton.getText().equals("Edit"))
			setEditState(1);
		else
			setEditState(0);
	}	
	
	@FXML
	private void onSave(ActionEvent event)
	{
		try { 
			// verify the inactive if so marked
			if (ppMarkInactiveCheck.isSelected()) 
			{
				Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
			    confirmAlert.setTitle("Set PayPeriod Inactive");
			    confirmAlert.setContentText("Are You Sure You Want to set this PayPeriod as Inactive? This will deactivate "
			    		+ " it and remove it from the Admin App.");
			    EtcAdmin.i().positionAlertCenter(confirmAlert);
			    Optional<ButtonType> result = confirmAlert.showAndWait();
	
			    if ((result.isPresent()) && (result.get() != ButtonType.OK))
			    {
			    	ppMarkInactiveCheck.setSelected(false);
			    	return;
			    }
			}
			
			PayPeriodRequest req = new PayPeriodRequest(); 
			req.setEmployerId(DataManager.i().mEmployer.getId());
			if (addMode == true) {
				PayPeriod newPpd = new PayPeriod();
				updatePayPeriod(newPpd);
				newPpd.setEmployerId(DataManager.i().mEmployer.getId());
				req.setEntity(newPpd);
			}else {
				updatePayPeriod(DataManager.i().mPayPeriod);
				req.setId(DataManager.i().mPayPeriod.getId());
				req.setEntity(DataManager.i().mPayPeriod);
			}
			
			// send it to the server
			DataManager.i().mPayPeriod = AdminPersistenceManager.getInstance().addOrUpdate(req); 
		} catch (Exception e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
		
		changesMade = true;
		//update
		showPayPeriod();
		// reset our edit state. It should be real now.
		setEditState(0);
	}	
	
	@FXML
	private void onClose(ActionEvent event) 
	{
		Stage stage = (Stage) ppSaveButton.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onAddProperty(ActionEvent event) {
		// tbd
	}	

	public class HBoxCell extends HBox
	{
        Label lblCol1 = new Label();
        Label lblCol2 = new Label();

        HBoxCell(String sCol1, String sCol2, boolean headerRow)
        {
             super();
          
             if (sCol1 == null ) sCol1 = "";
             if (sCol2 == null ) sCol2 = "";
             
             if (sCol1.contains("null")) sCol1 = "";
             if (sCol2.contains("null")) sCol2 = "";

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
             if (headerRow == true) 
             {
           	    lblCol1.setTextFill(Color.GREY);
           	    lblCol2.setTextFill(Color.GREY);
             }
             
             this.getChildren().addAll(lblCol1, lblCol2);
        }
    }	
}
