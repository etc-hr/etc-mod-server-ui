package com.etc.admin.ui.pipeline.filespecification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayFrequencyReference;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFilePayFrequencyReferenceRequest;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.utils.types.PayFrequencyType;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewDynamicFileSpecificationPayFrequencyTypeRefController 
{
	@FXML
	private Button dsrefAddButton;
	@FXML
	private Button dsrefRemoveButton;
	@FXML
	private Button dsrefSaveButton;
	@FXML
	private TableView<HBoxPayFreqCell> dsrefSelectedTableView;
	@FXML
	private ListView<PayFrequencyType> dsrefChoicesListView;
	@FXML
	private TextField dsrefReferenceField;
	@FXML
	private TextField dsrefEmployerFilterField;
	@FXML
	private ComboBox<String> dsRefEmployerCombo;
	
	Employer employer = null;
	
	private PipelineFileType fileType;
	
	public void setFileType(PipelineFileType fileType) {
		this.fileType = fileType;
	}

	public Boolean changesMade = false;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	@FXML
	public void initialize() 
	{
		// disable the selection-dependent buttons
		dsrefRemoveButton.setDisable(true);
		dsrefReferenceField.setText("");
		dsrefReferenceField.setPromptText("Reference Value (required)");
		
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

		ObservableList<PayFrequencyType> payFreqTypes = FXCollections.observableArrayList(PayFrequencyType.values());
		dsrefChoicesListView.setItems(payFreqTypes);

		TableColumn<HBoxPayFreqCell, String> x1 = new TableColumn<HBoxPayFreqCell, String>("Source");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxPayFreqCell,String>("source"));
		x1.setMinWidth(175);
		dsrefSelectedTableView.getColumns().add(x1);
		
		TableColumn<HBoxPayFreqCell, String> x3 = new TableColumn<HBoxPayFreqCell, String>("Reference");
		x3.setCellValueFactory(new PropertyValueFactory<HBoxPayFreqCell,String>("reference"));
		x3.setMinWidth(225);
		dsrefSelectedTableView.getColumns().add(x3);
	}
	
	public void load() 
	{
		// reset the filter
		dsrefEmployerFilterField.setText("");
		refreshEmployerCombo();
		refreshSelectionDisplay();
	}

	private void refreshEmployerCombo()
	{
		dsRefEmployerCombo.getItems().clear();
		
		List<String> list = new ArrayList<>();
		for(Employer employer : DataManager.i().mEmployersList) 
		{
			if(employer.isActive() == false) continue;
			if(employer.isDeleted() == true) continue;

			if(dsrefEmployerFilterField.getText().equals("") == false) 
			{
				if(employer.getName().toUpperCase().contains(dsrefEmployerFilterField.getText().toUpperCase()) == true) 
				{
					list.add(employer.getName()); //new ComboCell(employer.getName()));
				}
			} else {
				list.add(employer.getName()); //new ComboCell(employer.getName()));
			}
		}
        ObservableList<String> newObservableList = FXCollections.observableList(list);
        dsRefEmployerCombo.setItems(newObservableList);	
	}
	
	private void refreshSelectionDisplay() 
	{
		dsrefSelectedTableView.getItems().clear();

		if(DataManager.i().mDynamicPayFileSpecification.getPpdFreqReferences() != null)
			for(DynamicPayFilePayFrequencyReference ref : DataManager.i().mDynamicPayFileSpecification.getPpdFreqReferences())
				if(ref.isActive() == true && ref.isDeleted() == false) 
				{
					dsrefSelectedTableView.getItems().add( new HBoxPayFreqCell(ref));
				}
	}

	@FXML
	private void onAdd(ActionEvent event) 
	{
		if(dsrefChoicesListView.getSelectionModel().getSelectedItem() == null) return;
		if(dsrefReferenceField.getText() == "" || dsrefReferenceField.getText().isEmpty()) return;
		changesMade = true;
		PayFrequencyType payFreqType = dsrefChoicesListView.getSelectionModel().getSelectedItem();

		try {
			DynamicPayFilePayFrequencyReferenceRequest payRequest = new DynamicPayFilePayFrequencyReferenceRequest();
			DynamicPayFilePayFrequencyReference payRef = new DynamicPayFilePayFrequencyReference();
			payRef.setPayFrequencyType(payFreqType);
			payRef.setReference(dsrefReferenceField.getText());
			payRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
			payRequest.setEntity(payRef);
			payRequest.setId(payRef.getId());

			// save it to the server
			payRef = AdminPersistenceManager.getInstance().addOrUpdate(payRequest);
			dsrefSelectedTableView.getItems().add( new HBoxPayFreqCell(payRef));
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
			DynamicPayFilePayFrequencyReferenceRequest payRequest = new DynamicPayFilePayFrequencyReferenceRequest();
			DynamicPayFilePayFrequencyReference payRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getPayRef();
			payRequest.setId(payRef.getId());
			payRef.setActive(false);
			AdminPersistenceManager.getInstance().remove(payRequest);
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		refreshSelectionDisplay();
	}	
	
	@FXML
	private void onEmployerFilter(ActionEvent event) {
		refreshEmployerCombo();
		dsRefEmployerCombo.show();
	}
	
	@FXML
	private void onEmplopyerFilterClear(ActionEvent event) {
		dsrefEmployerFilterField.setText("");
		refreshEmployerCombo();
		dsRefEmployerCombo.show();
	}
	
	@FXML
	private void onSave(ActionEvent event) {
		Stage stage = (Stage) dsrefSaveButton.getScene().getWindow();
		stage.close();
	}	
	
	public class HBoxPayFreqCell extends HBox 
	{
		SimpleStringProperty source = new SimpleStringProperty();
		SimpleStringProperty reference = new SimpleStringProperty();
		DynamicPayFilePayFrequencyReference payRef = null;

		public DynamicPayFilePayFrequencyReference getPayRef() {
			return payRef;
		}
		
        public String getSource() {
        	return source.get();
        }
         
        public String getReference() {
        	return reference.get();
        }
         
        HBoxPayFreqCell(DynamicPayFilePayFrequencyReference ref) 
        {
             super();
             this.source.set(ref.getReference());
             this.reference.set(ref.getPayFrequencyType().toString());
             this.payRef = ref;
        }
    }	
}
