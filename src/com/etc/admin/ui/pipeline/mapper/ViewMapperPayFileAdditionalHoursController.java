package com.etc.admin.ui.pipeline.mapper;

import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileAdditionalHoursSpecification;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileAdditionalHoursSpecificationRequest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

	public class ViewMapperPayFileAdditionalHoursController 
	{
		@FXML
		private Button dsrefAddButton;
		@FXML
		private Button dsrefRemoveButton;
		@FXML
		private Button dsrefCloseButton;
		@FXML
		private TextField dsrefHourIndexField;
		
		public Boolean changesMade = false;
		//private Boolean addMode = false;
		/**
		 * initialize is called when the FXML is loaded
		 */
		@FXML
		public void initialize() {
			
		}
		
		public void load() 
		{
			if(DataManager.i().mDynamicPayFileAdditionalHoursSpecification == null) 
			{
				dsrefRemoveButton.setVisible(false);
			} else {
				dsrefAddButton.setVisible(false);
				dsrefHourIndexField.setText(String.valueOf(DataManager.i().mDynamicPayFileAdditionalHoursSpecification.getColIndex()));
			}
		}

		@FXML
		private void onAdd(ActionEvent event) 
		{
			if(dsrefHourIndexField.getText().isEmpty() || Utils.validateIntRangeTextField(dsrefHourIndexField, 0, 99) == false)
				return;
			
			changesMade = true;
			
			try {
				// create the additional hours spec using the screen info
				DynamicPayFileAdditionalHoursSpecificationRequest request = new DynamicPayFileAdditionalHoursSpecificationRequest(DataManager.i().mDynamicPayFileAdditionalHoursSpecification);
				DynamicPayFileAdditionalHoursSpecification spec = new DynamicPayFileAdditionalHoursSpecification();
				spec.setColIndex(Integer.valueOf(dsrefHourIndexField.getText()));
				spec.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
				spec.setSpecification(DataManager.i().mDynamicPayFileSpecification);
				request.setEntity(spec);
				request.setId(spec.getId());
				
				//save it to the server
				AdminPersistenceManager.getInstance().addOrUpdate(request);
			} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
		    catch (Exception e) { DataManager.i().logGenericException(e); }
			
			// and exit
			Stage stage = (Stage) dsrefRemoveButton.getScene().getWindow();
			stage.close();
		}		

		@FXML
		private void onRemove(ActionEvent event) 
		{
			changesMade = true;
			
			try {
				// create the request
				DynamicPayFileAdditionalHoursSpecificationRequest request = new DynamicPayFileAdditionalHoursSpecificationRequest();
				DynamicPayFileAdditionalHoursSpecification spec = DataManager.i().mDynamicPayFileAdditionalHoursSpecification;
				request.setId(spec.getId());
				AdminPersistenceManager.getInstance().remove(request);
				
				//update the collection
				AdminPersistenceManager.getInstance().get(request);
			} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
		    catch (Exception e) { DataManager.i().logGenericException(e); }
			
			// and exit
			Stage stage = (Stage) dsrefRemoveButton.getScene().getWindow();
			stage.close();
		}	
		
		@FXML
		private void onClose(ActionEvent event) 
		{
			Stage stage = (Stage) dsrefRemoveButton.getScene().getWindow();
			stage.close();
		}
	}
