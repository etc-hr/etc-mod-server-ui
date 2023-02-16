package com.etc.admin.ui.pipeline.specification;

import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.DocumentType;
import com.etc.corvetto.entities.UploadHandler;
import com.etc.corvetto.entities.UploadTypeTemplate;
import com.etc.corvetto.rqs.UploadTypeTemplateRequest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ViewUploadTypeTemplateController 
{
	@FXML
	private TextField pspcNameField;
	@FXML
	private TextField pspcDescriptionField;
	@FXML
	private ChoiceBox<String> pspcDocTypeCombo;
	@FXML
	private ChoiceBox<String> pspcHandlerCombo;
	@FXML
	private CheckBox pspcEncryptedCheck;
	@FXML
	private CheckBox pspcEnabledCheck;
	@FXML
	private CheckBox pspcEmployerSpecificCheck;
	@FXML
	private Button pspsCancelButton;
	@FXML
	private Button pspsSaveButton;
	
	UploadTypeTemplate template = null;
	boolean addMode = false;
	
	// a flag to tell the specification screen to update
	public boolean changesMade = false;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
	}
	
	public void load(boolean addMode) {
		if(addMode == true)
			setAddMode(addMode);
	}

	private void initControls() 
	{
		//loadTemplateList();
		loadDocTypeChoices();
		loadHandlerChoices();
	}	
	
	private void loadDocTypeChoices()
	{
		pspcDocTypeCombo.getItems().clear();
		for(DocumentType docType : DataManager.i().mDocumentTypes) 
		{
			pspcDocTypeCombo.getItems().add(docType.getName()); //cell);
		}
	}	
	
	private void loadHandlerChoices() 
	{
		pspcHandlerCombo.getItems().clear();
		for(UploadHandler handler : DataManager.i().mUploadHandlers) 
		{
			pspcHandlerCombo.getItems().add(handler.getName());
		}
	}	

	private void setAddMode(boolean addMode) {
		// set the flag
		this.addMode = addMode;
		//adjust our screen accordingly.
		
	}
	
	private void updateTemplate(UploadTypeTemplateRequest request)
	{
		UploadTypeTemplate template = new UploadTypeTemplate();
		
		template.setName(pspcNameField.getText());
		template.setDescription(pspcDescriptionField.getText());
		template.setEncrypted(pspcEncryptedCheck.isSelected());
		template.setEnabled(pspcEnabledCheck.isSelected());
		template.setEmployerSpecific(pspcEmployerSpecificCheck.isSelected());
		template.setTypeId(Long.valueOf(pspcDocTypeCombo.getSelectionModel().getSelectedIndex()));
		template.setHandlerId(Long.valueOf(pspcHandlerCombo.getSelectionModel().getSelectedIndex()));
		request.setEntity(template);
		request.setId(template.getId());
		
		//send it to the server
		try {
			template = AdminPersistenceManager.getInstance().addOrUpdate(request);
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		Stage stage = (Stage) pspcNameField.getScene().getWindow();
		stage.close();
	}	

	private boolean validateData() 
	{
		boolean bReturn = true;
		
		//check for data
		if(!Utils.validate(pspcNameField)) bReturn = false;
		if(!Utils.validate(pspcDescriptionField)) bReturn = false;
		
		if(pspcDocTypeCombo.getSelectionModel().isEmpty() ) 
		{
			pspcDocTypeCombo.setStyle("-fx-background-color: red");
			bReturn = false;
		}
		pspcDocTypeCombo.setStyle(null);

		if(pspcHandlerCombo.getSelectionModel().isEmpty() ) 
		{
			pspcHandlerCombo.setStyle("-fx-background-color: red");
			bReturn = false;
		}
		pspcHandlerCombo.setStyle(null);

		return bReturn;
	}
	
	@FXML
	private void onSave(ActionEvent event) 
	{
		//validate everything
		if(!validateData())
			return;
	
		//create the request
		UploadTypeTemplateRequest request = new UploadTypeTemplateRequest();
		
		//Send it
		updateTemplate(request);
		
		// mark the update flag
		changesMade = true;
		// and exit
		Stage stage = (Stage) pspcNameField.getScene().getWindow();
		stage.close();
		
	}		
	
	public class TemplateCell extends HBox
	{
		Label id = new Label();
		Label name = new Label();
		UploadTypeTemplate template = null;

         public UploadTypeTemplate getTemplate() {
        	 return template;
         }
         
         TemplateCell(UploadTypeTemplate template) 
         {
              super();
              
              if(template != null) 
              {
	              this.template = template;
	              
	              name.setText(template.getName());
	              id.setText(String.valueOf(template.getId()));
	              
	              //set the label widths
	              id.setMinWidth(50);
	              id.setMaxWidth(50);
	              id.setPrefWidth(50);
	              name.setMinWidth(150);
	              name.setMaxWidth(150);
	              name.setPrefWidth(150);
	
	              this.getChildren().addAll(id, name);
              }
         }
    }	
	
	public class HBoxCell extends HBox
	{
		 Label lblId = new Label();
        Label lblName = new Label();
        Long id;
        String name;

        HBoxCell(Long id, String name, boolean headerRow) 
        {
             super();

             //clean up any nulls
             if(name == null) 
           	  name = "";
             else
           	  name = name.replace("null", "");            	  
             
             this.id = id;
             this.name = name;

             lblId.setText(String.valueOf(id));
             lblId.setMinWidth(75);
             lblId.setMaxWidth(75);
             lblId.setPrefWidth(75);
             HBox.setHgrow(lblId, Priority.ALWAYS);

             lblName.setText(name);
             lblName.setMinWidth(225);
             lblName.setMaxWidth(225);
             lblName.setPrefWidth(225);
             HBox.setHgrow(lblName, Priority.ALWAYS);

             if(headerRow == true) {
           	  lblId.setTextFill(Color.GREY);
           	  lblName.setTextFill(Color.GREY);
             }
             
             this.getChildren().addAll(lblName );
        }
        
        String getCellName() {
       	 return name;
        }
        
        String getCellId() {
       	 return String.valueOf(id);
        }
    }
}


