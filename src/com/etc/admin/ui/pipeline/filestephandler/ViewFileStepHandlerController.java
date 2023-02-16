package com.etc.admin.ui.pipeline.filestephandler;

import java.io.IOException;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileStepHandler;
import com.etc.corvetto.ems.pipeline.rqs.PipelineFileStepHandlerRequest;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineStepType;
import com.etc.entities.CoreData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewFileStepHandlerController
{
	@FXML
	private GridPane stpHndlrGrid;
	@FXML
	private TextField stphndlrNameLabel;
	@FXML
	private TextField stphndlrDescriptionLabel;
	@FXML
	private ComboBox<PipelineStepType> stphndlrPipelineStepTypeCombo;
	@FXML
	private TextField stphndlrInterfaceClassLabel;
	@FXML
	private TextField stphndlrHandlerClassLabel;
	@FXML
	private TextField revision;
	@FXML
	private Button stphndlrEditButton;
	@FXML
	private Button stphndlrSaveButton;
	@FXML
	private Button stphndlrCloseButton;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() 
	{
		initControls();
		updateFileStepHandlerData();
	}

	private void initControls() 
	{
		ObservableList<PipelineStepType> stepTypes = FXCollections.observableArrayList(PipelineStepType.values());
		stphndlrPipelineStepTypeCombo.setItems(stepTypes);	    

		//disable the edit buttonm until something is selected
		stphndlrEditButton.setDisable(!Utils.userCanEdit());
		stphndlrSaveButton.setVisible(false);
		stphndlrPipelineStepTypeCombo.setDisable(true);
 	}	

	private void updateFileStepHandlerData()
	{
		PipelineFileStepHandler stepHandler = DataManager.i().mPipelineFileStepHandler;
		if(stepHandler != null)
		{
			Utils.updateControl(stphndlrNameLabel,stepHandler.getName());
			Utils.updateControl(stphndlrInterfaceClassLabel,stepHandler.getInterfaceClass());
			Utils.updateControl(stphndlrHandlerClassLabel,stepHandler.getHandlerClass());
			
			// Pipeline Step Type
			if(stepHandler.getType() != null) 
				stphndlrPipelineStepTypeCombo.getSelectionModel().select(stepHandler.getType());

			//enable the edit button
			stphndlrEditButton.setDisable(false);
		}
	}

	private void updateFileStepHandler(PipelineFileStepHandlerRequest request)
	{
		if(request != null) 
		{
			PipelineFileStepHandler stepHndlr = new PipelineFileStepHandler();
			
			stepHndlr.setChannelId(DataManager.i().mPipelineChannel.getId());
			stepHndlr.setChannel(DataManager.i().mPipelineChannel);

			if(stphndlrInterfaceClassLabel.getText() != null)
				stepHndlr.setInterfaceClass(stphndlrInterfaceClassLabel.getText());
			stepHndlr.setName(stphndlrNameLabel.getText());
			stepHndlr.setRevision(Long.valueOf(revision.toString()));
			stepHndlr.setType(PipelineStepType.valueOf(stphndlrPipelineStepTypeCombo.getValue().toString()));
			stepHndlr.setHandlerClass(stphndlrHandlerClassLabel.getText());
			
			request.setEntity(stepHndlr);
			request.setId(stepHndlr.getId());
			
			// send to the server
			try {
				stepHndlr = AdminPersistenceManager.getInstance().addOrUpdate(request);
			} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
    	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		}
	}

	@FXML
	private void onShowSystemInfo() {
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mPipelineFileStepHandler;
			DataManager.i().mCurrentCoreDataType = SystemDataType.FILESTEPHANDLER;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
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
	private void onSave(ActionEvent event)
	{
		//Create a new Request to pass into update method
		PipelineFileStepHandlerRequest request = new PipelineFileStepHandlerRequest();
		updateFileStepHandler(request);
		
		// and take everything out of edit mode
		setEditMode(false);
	}
	
	@FXML
	private void onEdit(ActionEvent event) 
	{
		if(stphndlrEditButton.getText().equals("Edit"))
			setEditMode(true);
		else
			setEditMode(false);
	}

	private void setEditMode(boolean mode)
	{
		if(mode == true) 
		{
			stphndlrEditButton.setText("Cancel");
			stpHndlrGrid.setStyle("-fx-background-color: #eeffee");
		} else {
			stphndlrEditButton.setText("Edit");
			stpHndlrGrid.getStyleClass().clear();
			stpHndlrGrid.setStyle(null);	
		}
		
		//Button
		stphndlrSaveButton.setVisible(mode);
		
		//Data
		stphndlrNameLabel.setEditable(mode);
		stphndlrDescriptionLabel.setEditable(mode);
		stphndlrPipelineStepTypeCombo.setDisable(!mode);
		stphndlrInterfaceClassLabel.setEditable(mode);
		stphndlrHandlerClassLabel.setEditable(mode);
	}	
	
	@FXML
	private void onClose(ActionEvent event) 
	{
		Stage stage = (Stage) stpHndlrGrid.getScene().getWindow();
		stage.close();
	}	
}

