package com.etc.admin.ui.pipeline.filespecification;

import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayClassReference;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFilePayClassReferenceRequest;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.PayClass;
import com.etc.corvetto.rqs.PayClassRequest;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewDynamicFileSpecificationPayClassRefController 
{
	@FXML
	private Button dsrefAddButton;
	@FXML
	private Button dsrefRemoveButton;
	@FXML
	private TableView<HBoxCell> dsrefSelectedTableView;
	@FXML
	private ListView<String> dsrefChoicesListView;
	@FXML
	private TextField dsrefReferenceField;
	
	private ViewDynamicFileSpecificationController specController;

	public Boolean changesMade = false;

	/**
	 * initialize is called when the FXML is loaded
	 */
	//local employer selection
	Employer employer = null;
	@FXML
	public void initialize() 
	{
		// disable the selection-dependent buttons
		dsrefRemoveButton.setDisable(true);
		dsrefReferenceField.setText("");
		dsrefReferenceField.setPromptText("Source Value (required)");

		// set a handler for the selections click
		dsrefSelectedTableView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) 
            {
            	dsrefRemoveButton.setDisable(false);
            }
        });
	
		setTableColumns();
	}
	
	private void setTableColumns() 
	{
		//clear the default values
		dsrefSelectedTableView.getColumns().clear();

		TableColumn<HBoxCell, String> x1 = new TableColumn<HBoxCell, String>("Source");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("source"));
		x1.setMinWidth(175);
		dsrefSelectedTableView.getColumns().add(x1);

		TableColumn<HBoxCell, String> x3 = new TableColumn<HBoxCell, String>("Reference");
	    x3.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("reference"));
	    x3.setMinWidth(225);
		dsrefSelectedTableView.getColumns().add(x3);
	}
	
	public void setSpecController(ViewDynamicFileSpecificationController specController) {
		this.specController = specController;
	}
	
	public void load() 
	{
		dsrefChoicesListView.getItems().clear();

		try {
			PayClassRequest request = new PayClassRequest();
			DataManager.i().mPayClasses = AdminPersistenceManager.getInstance().getAll(request);
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
		if(DataManager.i().mPayClasses != null)
		{
			for(PayClass payclass : DataManager.i().mPayClasses)
				dsrefChoicesListView.getItems().add(payclass.getV2PayClass());
		}
		refreshSelectionDisplay();
	}

	private void refreshSelectionDisplay() 
	{
		dsrefSelectedTableView.getItems().clear();
		if(DataManager.i().mDynamicPayFileSpecification.getPayClassReferences() != null)
		{
			for(DynamicPayFilePayClassReference ref : DataManager.i().mDynamicPayFileSpecification.getPayClassReferences()) 
			{
				if(ref.isActive() == true && ref.isDeleted() == false)
				{
					for(PayClass payCls : DataManager.i().mPayClasses) 
					{
						if(payCls.isActive() == false) continue;
						if(payCls.isDeleted() == true) continue;

						if(payCls.getId().equals(ref.getPayClassId()))
						{
							ref.setPayClass(payCls);
							String v2Cls = payCls.getV2PayClass().toString();
							dsrefSelectedTableView.getItems().add(new HBoxCell(ref, v2Cls));
						}
					}
				}
			}
		}
	}

	@FXML
	private void onAdd(ActionEvent event) 
	{
		if(dsrefChoicesListView.getSelectionModel().getSelectedItem() == null) return;
		if(dsrefReferenceField.getText() == "" || dsrefReferenceField.getText().isEmpty()) return;
		changesMade = true;

		try {
			Long payClassId = (long) (dsrefChoicesListView.getSelectionModel().getSelectedIndex() + 1);

			String v2Class = dsrefChoicesListView.getSelectionModel().getSelectedItem();

			DynamicPayFilePayClassReferenceRequest payRequest = new DynamicPayFilePayClassReferenceRequest();
			DynamicPayFilePayClassReference payRef = new DynamicPayFilePayClassReference();
			payRef.setPayClassId(payClassId);
			payRef.setReference(dsrefReferenceField.getText());
			payRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
			payRequest.setEntity(payRef);
			payRequest.setId(payRef.getId());

			// save it to the server
			payRef = AdminPersistenceManager.getInstance().addOrUpdate(payRequest);

			dsrefSelectedTableView.getItems().add(new HBoxCell(payRef, v2Class));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		//reset the field
		dsrefReferenceField.setText("");
		dsrefReferenceField.setPromptText("Source Value (required)");
		//and reload
//		refreshSelectionDisplay();
	}		

	@FXML
	private void onRemove(ActionEvent event) 
	{
		if(dsrefSelectedTableView.getSelectionModel().getSelectedItem() == null) return;
		changesMade = true;

		try {
			DynamicPayFilePayClassReferenceRequest payRequest = new DynamicPayFilePayClassReferenceRequest();
			DynamicPayFilePayClassReference pyRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getPayRef();
			payRequest.setId(pyRef.getId());
			pyRef.setActive(false);
			AdminPersistenceManager.getInstance().remove(payRequest);
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		refreshSelectionDisplay();
	}	
	
	@FXML
	private void onClose(ActionEvent event) {
		Stage stage = (Stage) dsrefAddButton.getScene().getWindow();
		stage.close();
	}	
	
	public class ChoiceCell extends HBox
	{
		Label id = new Label();
		Label name = new Label();
		PayClass payClass;

         public PayClass getPayClass() {
        	 return payClass;
         }
         
         ChoiceCell(PayClass payClass) 
         {
              super();
              
              this.payClass = payClass;
              id.setText(String.valueOf(payClass.getId()));
              
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
	
	public class HBoxCell extends HBox
	{
		 SimpleStringProperty source = new SimpleStringProperty();
		 SimpleStringProperty reference = new SimpleStringProperty();
		 DynamicPayFilePayClassReference payRef = null;

		 public DynamicPayFilePayClassReference getPayRef() {
			return payRef;
		 }
		
         public String getSource() {
        	 return source.get();
         }
         
         public String getReference() {
        	 return reference.get();
         }
         
         HBoxCell(DynamicPayFilePayClassReference ref, String payCls)
         {
             super();
             this.source.set(ref.getReference());
             this.reference.set(payCls.toString());
             this.payRef = ref;
         }

         HBoxCell(String source, String reference) 
         {
             super();

             if(source == null) source = "";
             if(reference == null ) reference = "";
              
             this.source.set(source);
             this.reference.set(reference);
        }
    }	
}

