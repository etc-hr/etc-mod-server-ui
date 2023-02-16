package com.etc.admin.ui.pipeline.filespecification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileLocationReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileLocationReference;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileLocationReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileLocationReferenceRequest;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.Location;
import com.etc.corvetto.rqs.LocationRequest;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewDynamicFileSpecificationLocationRefController 
{
	@FXML
	private Label dsrefEmployerLabel;
	@FXML
	private Button dsrefAddButton;
	@FXML
	private Button dsrefRemoveButton;
	@FXML
	private Button dsrefSaveButton;
	@FXML
	private Button dsrefAddEmployerLocationRefButton;
	@FXML
	private Button dsrefRemoveEmployerLocationRefButton;
	@FXML
	private TableView<HBoxLocationCell> dsrefSelectedTableView;
	@FXML
	private ListView<ChoiceCell> dsrefChoicesListView;
	@FXML
	private TextField dsrefReferenceField;
	@FXML
	private TextField dsrefAddLocationField;
	@FXML
	private Label dfcovEmployerFilterLabel;
	@FXML
	private TextField dsrefEmployerFilterField;
	@FXML
	private ComboBox<String> dsRefEmployerCombo;
	
	private PipelineFileType fileType;

	/**
	 * initialize is called when the FXML is loaded
	 */
	
	//local employer selection
	Employer employer = null;
	Location location = null;
	List<Long> currentEmployerListIds = new ArrayList<Long>();
	
	public Boolean changesMade = false;
	
	@FXML
	public void initialize() 
	{
		// disable the selection-dependent buttons
		dsrefAddEmployerLocationRefButton.setDisable(true);
		dsrefRemoveButton.setDisable(true);
		dsrefRemoveEmployerLocationRefButton.setDisable(true);
		dsrefReferenceField.setText("");
		dsrefReferenceField.setPromptText("Reference Value (required)");
		dsrefAddLocationField.setDisable(true);
		dsrefAddLocationField.setText("");
		dsrefAddLocationField.setPromptText("");
		dsrefEmployerLabel.setText("Select Employer");
		
		// set a handler for the selections click
		dsrefSelectedTableView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) 
            {
            	dsrefRemoveButton.setDisable(false);
            }
        });

		dsrefChoicesListView.setOnMouseClicked(mouseClickedEvent ->
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) 
            {
            	location = dsrefChoicesListView.getSelectionModel().getSelectedItem().getLocation();
            }
        });
		setTableColumns();
	}
	
	public void setFileType(PipelineFileType fileType) {
		this.fileType = fileType;
	}
	
	private void setTableColumns() 
	{
		//clear the default values
		dsrefSelectedTableView.getColumns().clear();

	    TableColumn<HBoxLocationCell, String> x1 = new TableColumn<HBoxLocationCell, String>("Source");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxLocationCell,String>("source"));
		x1.setMinWidth(175);
		dsrefSelectedTableView.getColumns().add(x1);
		
		TableColumn<HBoxLocationCell, String> x3 = new TableColumn<HBoxLocationCell, String>("Reference");
		x3.setCellValueFactory(new PropertyValueFactory<HBoxLocationCell,String>("reference"));
		x3.setMinWidth(225);
		dsrefSelectedTableView.getColumns().add(x3);
	}
	
	public void load()
	{
		// reset the filter
		dsrefEmployerFilterField.setText("");
		refreshEmployerCombo();
		dsrefChoicesListView.getItems().clear();
		refreshSelectionDisplay();
	}

	private void refreshEmployerCombo()
	{
		dsRefEmployerCombo.getItems().clear();
		currentEmployerListIds.clear();

		for(Employer employer : DataManager.i().mEmployersList) 
		{
			if(employer.isActive() == false) continue;
			if(employer.isDeleted() == true) continue;

			if(dsrefEmployerFilterField.getText().equals("") == false) 
			{
				if(employer.getName().toUpperCase().contains(dsrefEmployerFilterField.getText().toUpperCase()) == true)
				{
					dsRefEmployerCombo.getItems().add(employer.getName());
					currentEmployerListIds.add(employer.getId());
				} 
				continue;
			} else
				dsRefEmployerCombo.getItems().add(employer.getName());
				currentEmployerListIds.add(employer.getId());
		}
	}

	private void loadLocationChoices() 
	{
		dsrefChoicesListView.getItems().clear();
		if(dsRefEmployerCombo.getSelectionModel().getSelectedItem() == null)
		{
			dsrefAddEmployerLocationRefButton.setDisable(true);
			dsrefAddLocationField.setDisable(true);
			dsrefAddLocationField.setText("");
			dsrefAddLocationField.setPromptText("");
			dsrefEmployerLabel.setText("Select Employer");
			return;
		}

		// enable the 
		dsrefAddEmployerLocationRefButton.setDisable(false);
		dsrefAddLocationField.setDisable(false);
		dsrefAddLocationField.setText("");
		dsrefAddLocationField.setPromptText("Location Name");

		try {
			// get the locations for the selected employer
			LocationRequest request = new LocationRequest();
			request.setId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));
			request.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));
			DataManager.i().mLocations = AdminPersistenceManager.getInstance().getAll(request);
			
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		dsrefEmployerLabel.setText(dsRefEmployerCombo.getSelectionModel().getSelectedItem().toString());
		if(DataManager.i().mLocations == null) return;
		
		// and load them to the list
		List<ChoiceCell> list = new ArrayList<>();	
		for(Location loc : DataManager.i().mLocations) 
		{
			if(loc.isActive() == false) continue;
			list.add(new ChoiceCell(loc));
		}
        ObservableList<ChoiceCell> newObservableList = FXCollections.observableList(list);
        dsrefChoicesListView.setItems(newObservableList);
		dsrefRemoveEmployerLocationRefButton.setDisable(false);

	}	
	
	private void refreshSelectionDisplay() 
	{
		LocationRequest request = new LocationRequest();
		try {
			DataManager.i().mLocations = AdminPersistenceManager.getInstance().getAll(request);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		dsrefSelectedTableView.getItems().clear();
		switch(fileType) 
		{
			case EMPLOYEE:
				if(DataManager.i().mDynamicEmployeeFileSpecification.getLocReferences() != null)
				{					
					for(DynamicEmployeeFileLocationReference ref : DataManager.i().mDynamicEmployeeFileSpecification.getLocReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							for(Location loc : DataManager.i().mLocations)
							{
								if(loc.isActive() == false) continue;
								if(loc.isDeleted() == true) continue;

								if(loc.getId().equals(ref.getLocationId()))
								{
									ref.setLocation(loc);
									dsrefSelectedTableView.getItems().add(new HBoxLocationCell(ref, loc));
								}
							}
						}
					}
				}		
				break;
			case PAY:
				if(DataManager.i().mDynamicPayFileSpecification.getLocReferences() != null)
				{					
					for(DynamicPayFileLocationReference ref : DataManager.i().mDynamicPayFileSpecification.getLocReferences())
					{
						if(ref.isActive() == true && ref.isDeleted() == false)
						{
							for(Location loc : DataManager.i().mLocations)
							{
								if(loc.isActive() == false) continue;
								if(loc.isDeleted() == true) continue;

								if(loc.getId().equals(ref.getLocationId()))
								{
									ref.setLocation(loc);
									dsrefSelectedTableView.getItems().add(new HBoxLocationCell(ref, loc));
								}
							}
						}
					}
				}		
				break;
			default:
				break;
		}
	}

	@FXML
	private void onAdd(ActionEvent event)
	{
		if(dsrefChoicesListView.getSelectionModel().getSelectedItem() == null) return;
		if(dsrefReferenceField.getText() == "" || dsrefReferenceField.getText().isEmpty()) return;
		changesMade = true;
		Location location = dsrefChoicesListView.getSelectionModel().getSelectedItem().getLocation();

		switch(fileType) 
		{
			case EMPLOYEE:
				try {
					// create the request
					DynamicEmployeeFileLocationReferenceRequest empRequest = new DynamicEmployeeFileLocationReferenceRequest();
					DynamicEmployeeFileLocationReference locRef = new DynamicEmployeeFileLocationReference();
					locRef.setLocationId(location.getId());
					locRef.setReference(dsrefReferenceField.getText());
					locRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					empRequest.setEntity(locRef);
					empRequest.setId(locRef.getId());
	
					// save it to the server
					AdminPersistenceManager.getInstance().addOrUpdate(empRequest);
					dsrefSelectedTableView.getItems().add(new HBoxLocationCell(locRef, location));
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			case PAY:
				try {
					DynamicPayFileLocationReferenceRequest payRequest = new DynamicPayFileLocationReferenceRequest();
					DynamicPayFileLocationReference payRef = new DynamicPayFileLocationReference();
					payRef.setLocationId(location.getId());
					payRef.setReference(dsrefReferenceField.getText());
					payRef.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
					payRequest.setEntity(payRef);
					payRequest.setId(payRef.getId());
					
					// save it to the server
					AdminPersistenceManager.getInstance().addOrUpdate(payRequest);
					dsrefSelectedTableView.getItems().add(new HBoxLocationCell(payRef, location));
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			default:
				break;
		}

		//reset the field
		dsrefReferenceField.setText("");
		dsrefReferenceField.setPromptText("Reference Value (required)");
		//and reload
//		refreshSelectionDisplay();
	}		
	
	@FXML
	private void onRemove(ActionEvent event) 
	{
		if(dsrefSelectedTableView.getSelectionModel().getSelectedItem() == null) return;
		changesMade = true;
		
		switch(DataManager.i().mPipelineChannel.getType())
		{
			case EMPLOYEE:
				try {
					DynamicEmployeeFileLocationReferenceRequest empRequest = new DynamicEmployeeFileLocationReferenceRequest();
					DynamicEmployeeFileLocationReference empRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getEmployeeRef();
					empRequest.setId(empRef.getId());
					empRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(empRequest);
				} catch (CoreException e) {	 DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			case PAY:
				try {
					DynamicPayFileLocationReferenceRequest payRequest = new DynamicPayFileLocationReferenceRequest();
					DynamicPayFileLocationReference payRef = dsrefSelectedTableView.getSelectionModel().getSelectedItem().getPayRef();
					payRequest.setId(payRef.getId());
					payRef.setActive(false);
					AdminPersistenceManager.getInstance().remove(payRequest);
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
				break;
				
			default:
				break;
		}
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
	private void onSelectEmployer(ActionEvent event) {
		loadLocationChoices();
	}
	
	@FXML
	private void onSave(ActionEvent event) {
		Stage stage = (Stage) dsrefSaveButton.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	private void onAddEmployerLocation(ActionEvent event) 
	{
		if(dsrefAddLocationField.getText().isEmpty()) return;
		
		try {
			LocationRequest request = new LocationRequest();
			Location loc = new Location();

			loc.setName(dsrefAddLocationField.getText());
			loc.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));
			request.setId(loc.getId());
			request.setEntity(loc);
			request.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));

			location = AdminPersistenceManager.getInstance().addOrUpdate(request);
		} catch (CoreException e) {	 DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		loadLocationChoices();
	}
	
	@FXML
	private void onRemoveEmployerLocation(ActionEvent event) 
	{	
		try {
			LocationRequest request = new LocationRequest();
			Location loc = location;

			request.setId(loc.getId());
			request.setEntity(loc);
			request.setEmployerId(currentEmployerListIds.get(dsRefEmployerCombo.getSelectionModel().getSelectedIndex()));

			AdminPersistenceManager.getInstance().remove(request);
		} catch (CoreException e) {	 DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		loadLocationChoices();
	}
	
	public class ChoiceCell extends HBox 
	{
		Label id = new Label();
		Label name = new Label();
		Location location;

         public Location getLocation() {
        	 return location;
         }
         
         ChoiceCell(Location location)
         {
              super();
              
              this.location = location;
              name.setText(location.getName());
              id.setText(String.valueOf(location.getId()));
              
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
	
	public class HBoxLocationCell extends HBox 
	{
		SimpleStringProperty name = new SimpleStringProperty();
		SimpleStringProperty source = new SimpleStringProperty();
		SimpleStringProperty reference = new SimpleStringProperty();
		DynamicEmployeeFileLocationReference empRef = null;
		DynamicPayFileLocationReference payRef = null;

		public DynamicEmployeeFileLocationReference getEmployeeRef() {
			return empRef;
		}
		
		public DynamicPayFileLocationReference getPayRef() {
			return payRef;
		}
		
        public String getName() {
       	 return name.get();
        }
		
         public String getSource() {
        	 return source.get();
         }
         
         public String getReference() {
        	 return reference.get();
         }
         
         HBoxLocationCell(DynamicEmployeeFileLocationReference ref, Location loc) 
         {
              super();
              if(loc != null) { this.name.set(loc.getName().toString()); }
              this.source.set(ref.getReference());
              this.reference.set(loc.getId().toString() + "	          " + loc.getName());
              this.empRef = ref;
         }
         
         HBoxLocationCell(DynamicPayFileLocationReference ref, Location loc) 
         {
             super();
             if(loc != null) { this.name.set(loc.getName().toString()); }
             this.source.set(ref.getReference());
             this.reference.set(loc.getId().toString() + "	          " + loc.getName());
             this.payRef = ref;
        }
    }	
}
