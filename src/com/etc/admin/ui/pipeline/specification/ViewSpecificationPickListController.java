package com.etc.admin.ui.pipeline.specification;

import com.etc.admin.data.DataManager;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.DocumentType;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.UploadHandler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ViewSpecificationPickListController 
{
	@FXML
	private Label dsrefTopLabel;
	@FXML
	private Button dsrefSelectButton;
	@FXML
	private ListView<HBoxCell> dsrefChoicesListView;
	@FXML
	private TextField dsrefReferenceField;
	@FXML
	private TextField dsrefFilterField;
	
	private int pickListType = 0;
	
	private ViewPipelineSpecificationController specController;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize()
	{
		// handler for the selection
		// set a handler for the UploadType double click
		dsrefChoicesListView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) 
            {
        		if(dsrefChoicesListView.getSelectionModel().getSelectedItem() != null)
        		{
        			HBoxCell cell = dsrefChoicesListView.getSelectionModel().getSelectedItem();
        			dsrefReferenceField.setText(cell.getCellId() + " - " + cell.getCellName());
        		}
            }
        });
		
		//let the user hit enter on the filter to apply it
		dsrefFilterField.setOnKeyReleased(event -> 
		{
			if(event.getCode() == KeyCode.ENTER)
			{
				loadPickListData();
			}
		});
		
		dsrefReferenceField.setText("");
	}
	
	public void setSpecController(ViewPipelineSpecificationController specController) {
		this.specController = specController;
	}
	
	public void setPickListType(int pickListType) {
		this.pickListType = pickListType;
	}
	
	public void load() {
		loadPickListData();
	}
	
	private void loadPickListData() 
	{
		switch(pickListType)
		{
			case 1: // account
				dsrefTopLabel.setText("Account Selection");
				loadAccountChoices();
				break;
			case 2: // employer
				dsrefTopLabel.setText("Employer Selection");
				loadEmployerChoices();
				break;
			case 3: // doctype
				dsrefTopLabel.setText("Document Type Selection");
				loadDocTypeChoices();
				break;
			case 4: // handler
				dsrefTopLabel.setText("Handler Selection");
				loadHandlerChoices();
				break;
			default:
				break;
		}
	}
	
	private void loadAccountChoices() 
	{
		dsrefChoicesListView.getItems().clear();
		for(Account account : DataManager.i().mAccounts) 
		{
			if(account.isActive() == false) continue;
			if(account.isDeleted() == true) continue;
			HBoxCell cell = new HBoxCell(account.getId(), account.getName(), false);
			// filter if present
			if(dsrefFilterField.getText() != "") 
			{
				if(cell.getCellName().toUpperCase().contains(dsrefFilterField.getText().toUpperCase()))
					dsrefChoicesListView.getItems().add(cell);
			} else
				dsrefChoicesListView.getItems().add(cell);
		}
	}	
	
	private void loadEmployerChoices() 
	{
		dsrefChoicesListView.getItems().clear();
		for(Employer emp : DataManager.i().mEmployersList)
		{
			if(emp.isActive() == false) continue;
			if(emp.isDeleted() == true) continue;
			HBoxCell cell = new HBoxCell(emp.getId(), emp.getName(), false);
			// filter if present
			if(dsrefFilterField.getText() != "") 
			{
				if(cell.getCellName().toUpperCase().contains(dsrefFilterField.getText().toUpperCase()))
					dsrefChoicesListView.getItems().add(cell);
			}else
				dsrefChoicesListView.getItems().add(cell);
		}
	}	

	private void loadDocTypeChoices()
	{
		dsrefChoicesListView.getItems().clear();
		
		for(DocumentType docType : DataManager.i().mDocumentTypes) 
		{
			HBoxCell cell = new HBoxCell(docType.getId(), docType.getName(), false);
			// filter if present
			if(dsrefFilterField.getText() != "") 
			{
				if(cell.getCellName().toUpperCase().contains(dsrefFilterField.getText().toUpperCase()))
					dsrefChoicesListView.getItems().add(cell);
			}else
				dsrefChoicesListView.getItems().add(cell);
		}
	}	

	private void loadHandlerChoices()
	{
		dsrefChoicesListView.getItems().clear();

		for(UploadHandler handler : DataManager.i().mUploadHandlers) 
		{
			HBoxCell cell = new HBoxCell(handler.getId(), handler.getName(), false);
			// filter if present
			if(dsrefFilterField.getText() != "")
			{
				if(cell.getCellName().toUpperCase().contains(dsrefFilterField.getText().toUpperCase()))
					dsrefChoicesListView.getItems().add(cell);
			}else
				dsrefChoicesListView.getItems().add(cell);
		}
	}	

	@FXML
	private void onSelect(ActionEvent event) 
	{
		HBoxCell cell = dsrefChoicesListView.getSelectionModel().getSelectedItem();
		// set the  data on the spec controller controls
		switch(pickListType) {
		case 1: // account
			specController.pspcUploadAccount.setText(cell.getCellName());
			specController.pspcUploadAccountId.setText(cell.getCellId());
			break;
		case 2: // employer
			specController.pspcUploadEmployer.setText(cell.getCellName());
			specController.pspcUploadEmployerId.setText(cell.getCellId());
			break;
		case 3: // doctype
			specController.pspcUploadDocType.setText(cell.getCellName());
			specController.pspcUploadDocTypeId.setText(cell.getCellId());
			break;
		case 4: // handler
			specController.pspcUploadHandler.setText(cell.getCellName());
			specController.pspcUploadHandlerId.setText(cell.getCellId());
			break;
		default:
			break;
		}
		
		// close
		Stage stage = (Stage) dsrefSelectButton.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onApplyFilter(ActionEvent event) {
		loadPickListData();
	}
	
	@FXML
	private void onClearFilter(ActionEvent event) {
		dsrefFilterField.setText("");
		loadPickListData();
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		Stage stage = (Stage) dsrefSelectButton.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	private void onClearAndReturn(ActionEvent event) 
	{
		// clear the data we are currently setting
		switch(pickListType)
		{
			case 1: // account
				specController.pspcUploadAccount.setText("");
				specController.pspcUploadAccountId.setText("");
				break;
			case 2: // employer
				specController.pspcUploadEmployer.setText("");
				specController.pspcUploadEmployerId.setText("");
				break;
			case 3: // doctype
				specController.pspcUploadDocType.setText("");
				specController.pspcUploadDocTypeId.setText("");
				break;
			case 4: // handler
				specController.pspcUploadHandler.setText("");
				specController.pspcUploadHandlerId.setText("");
				break;
			default:
				break;
		}
		
		Stage stage = (Stage) dsrefSelectButton.getScene().getWindow();
		stage.close();
	}	
	
	//extending the listview for our additional controls
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
              
              this.getChildren().addAll(lblId, lblName );
         }
         
         String getCellName() {
        	 return name;
         }
         
         String getCellId() {
        	 return String.valueOf(id);
         }
    }
}

