package com.etc.admin.ui.pipeline.filespecification;

import java.util.Optional;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayPeriodRule;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFilePayPeriodRuleRequest;
import com.etc.corvetto.utils.types.PayCodeType;
import com.etc.corvetto.utils.types.PayFrequencyType;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ViewDynamicPayFilePayPeriodRuleController 
{
	@FXML
	private TextField dfcovMaxHoursField;
	@FXML
	private ChoiceBox<String> dfcovPayCodeTypeChoice;
	@FXML
	private ChoiceBox<PayFrequencyType> dfcovPayFrequencyypeChoice;
	@FXML
	private ComboBox<String> dfcovEmployerChoice;
	@FXML
	private Button dfcovCancelButton;
	@FXML
	private Button dfcovSaveChangesButton;
	@FXML
	private Button dfcovRemoveRuleButton;

	public boolean addMode = false;
	public boolean changesMade = false;

	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {

	}
	
	public void load() 
	{
		initControls();
		
		// load the current rules
		if(DataManager.i().mDynamicPayFilePayPeriodRule != null)
			loadRuleData();
		else
			setAddMode();
	}
	
	private void initControls() 
	{
		//choiceboxes are pre-filled
		ObservableList<PayFrequencyType> payFreqTypes = FXCollections.observableArrayList(PayFrequencyType.values());
		dfcovPayFrequencyypeChoice.setItems(payFreqTypes);

		dfcovPayCodeTypeChoice.getItems().clear();
		dfcovPayCodeTypeChoice.getItems().add("FTH");
		dfcovPayCodeTypeChoice.getItems().add("VH");

		loadEmployers();

	    dfcovMaxHoursField.textProperty().addListener(new ChangeListener<String>() 
	    {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, 
	            String newValue) 
	        {
	            if(!newValue.matches("|[-\\+]?|[-\\+]?\\d+\\.?|[-\\+]?\\d+\\.?\\d+")){
	            	dfcovMaxHoursField.setText(oldValue);
	            }
	        }
	    });
		
	}
	
	private void loadEmployers()
	{
		String sName;
		ObservableList<String> employerNames = FXCollections.observableArrayList();
		for(int i = 0; i < DataManager.i().mEmployersList.size();i++) 
		{
			sName = DataManager.i().mEmployersList.get(i).getName();
			if(sName != null)
				employerNames.add(sName);
		};		
		
		// use a filterlist to wrap the account names for combobox searching later
        FilteredList<String> filteredItems = new FilteredList<String>(employerNames, p -> true);

        // the listener will filter the list according to what is in the search box edit control
        dfcovEmployerChoice.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> 
        {
        	if(dfcovEmployerChoice.isFocused() == false) return;
        	
            final javafx.scene.control.TextField editor = dfcovEmployerChoice.getEditor();
            final String selected = dfcovEmployerChoice.getSelectionModel().getSelectedItem();
            
            dfcovEmployerChoice.show();
            dfcovEmployerChoice.setVisibleRowCount(10);

            // Run on the GUI thread
            Platform.runLater(() ->
            {
                if(selected == null || !selected.equals(editor.getText()))
                {
                    filteredItems.setPredicate(item -> {
                        if(item.toUpperCase().contains(newvalue.toUpperCase()))
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
        dfcovEmployerChoice.setItems(filteredItems);	
		
		//close the dropdown if it is showing
        dfcovEmployerChoice.hide();
	}
	
	private void setAddMode()
	{
		dfcovPayFrequencyypeChoice.getSelectionModel().clearSelection(); 
		dfcovPayCodeTypeChoice.getSelectionModel().clearSelection(); 
		dfcovEmployerChoice.getSelectionModel().clearSelection(); 
		dfcovMaxHoursField.setText("");
		addMode = true;
		dfcovSaveChangesButton.setText("Add");
		dfcovSaveChangesButton.setDisable(false);
		dfcovCancelButton.setVisible(true);
	}
	
	private void loadRuleData()
	{
		dfcovPayFrequencyypeChoice.getSelectionModel().select(DataManager.i().mDynamicPayFilePayPeriodRule.getPayFrequencyType()); 
		dfcovPayCodeTypeChoice.getSelectionModel().select(DataManager.i().mDynamicPayFilePayPeriodRule.getPayCodeType().toString()); 
		dfcovEmployerChoice.getSelectionModel().select(DataManager.i().mDynamicPayFilePayPeriodRule.getEmployer().getName());
		dfcovMaxHoursField.setText(String.valueOf(DataManager.i().mDynamicPayFilePayPeriodRule.getMaxHours()));
	}
	
	@FXML
	private void onSave(ActionEvent event) 
	{
		//validate we have minimum data
		if(Utils.validate(dfcovMaxHoursField) == false) return;
		if(Float.valueOf(dfcovMaxHoursField.getText()) > 160) 
		{
			Utils.alertUser("Invalid Value", "MaxHours cannot be more than 160.0");
			return;
		}
			try {
				DynamicPayFilePayPeriodRuleRequest request = new DynamicPayFilePayPeriodRuleRequest();
				DynamicPayFilePayPeriodRule ppRule = DataManager.i().mDynamicPayFilePayPeriodRule;
				ppRule.setPayFrequencyType(dfcovPayFrequencyypeChoice.getSelectionModel().getSelectedItem()); 
				ppRule.setPayCodeType(PayCodeType.valueOf(dfcovPayCodeTypeChoice.getSelectionModel().getSelectedItem()));
				ppRule.setEmployerId(DataManager.i().mEmployer.getId());
				ppRule.setMaxHours(Float.valueOf(dfcovMaxHoursField.getText()));
				ppRule.setSpecificationId(DataManager.i().mDynamicPayFileSpecification.getId());
				request.setId(ppRule.getId());
				DataManager.i().mDynamicPayFilePayPeriodRule = AdminPersistenceManager.getInstance().addOrUpdate(request);
			} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
    	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
		changesMade = true;
		Stage stage = (Stage) dfcovMaxHoursField.getScene().getWindow();
		stage.close();

	}	
	
	@FXML
	private void onCancel(ActionEvent event) {
		changesMade = false;
		Stage stage = (Stage) dfcovMaxHoursField.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	private void onRemoveRule(ActionEvent event)
	{
		//verify that they want to remove the rule
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Remove Period Rule");
	    confirmAlert.setContentText("Are You Sure You Want to remove the selected Pay Period Rule?");
	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();
	    if((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	return;
	    }
	}	
	
	@FXML
	private void onClose(ActionEvent event) {
		Stage stage = (Stage) dfcovMaxHoursField.getScene().getWindow();
		stage.close();
	}	
	
	public class HBoxRuleCell extends HBox 
	{
        Label lblCol1 = new Label();
        Label lblCol2 = new Label();
        Label lblCol3 = new Label();
        Label lblCol4 = new Label();
        DynamicPayFilePayPeriodRule rule;    

        HBoxRuleCell( DynamicPayFilePayPeriodRule rule)
        {
             super();
             
             if(rule != null) 
             {
            	 if(rule.getPayCodeType() != null)
            		 lblCol1.setText(rule.getPayCodeType().toString());
            	 if(rule.getPayFrequencyType() != null)
            		 lblCol2.setText(rule.getPayFrequencyType().toString());
            	 if(rule.getEmployer() != null)
            		 lblCol3.setText(rule.getEmployer().getName());
               	 if(rule.getMaxHours() != null)
            		 lblCol4.setText(String.valueOf(rule.getMaxHours()));
            	 this.rule = rule;
             }else {
            	 lblCol1.setText("Comp Type");
            	 lblCol1.setTextFill(Color.GREY);
            	 lblCol2.setText("Pay Freq. Type");
            	 lblCol2.setTextFill(Color.GREY);
            	 lblCol3.setText("Employer");
            	 lblCol3.setTextFill(Color.GREY);
            	 lblCol4.setText("Max Hrs");
            	 lblCol4.setTextFill(Color.GREY);
             }
          
             lblCol1.setMinWidth(100);
             lblCol1.setMaxWidth(100);
             lblCol1.setPrefWidth(100);
             HBox.setHgrow(lblCol1, Priority.ALWAYS);

             lblCol2.setMinWidth(100);
             lblCol2.setMaxWidth(100);
             lblCol2.setPrefWidth(100);
             HBox.setHgrow(lblCol2, Priority.ALWAYS);

             lblCol3.setMinWidth(225);
             lblCol3.setMaxWidth(225);
             lblCol3.setPrefWidth(225);
             HBox.setHgrow(lblCol3, Priority.ALWAYS);

             lblCol4.setMinWidth(50);
             lblCol4.setMaxWidth(50);
             lblCol4.setPrefWidth(50);
             HBox.setHgrow(lblCol4, Priority.ALWAYS);

             this.getChildren().addAll(lblCol1, lblCol2, lblCol4, lblCol3);
        }

        public DynamicPayFilePayPeriodRule getRule() {
        	return rule; 
        }
    }
}


