package com.etc.admin.ui.pipeline.channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.employee.ViewPayrollController.PayCell;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.ems.pipeline.entities.PipelineChannel;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileStepHandler;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;
import com.etc.corvetto.ems.pipeline.rqs.PipelineFileStepHandlerRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineSpecificationRequest;
import com.etc.entities.CoreData;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

public class ViewPipelineChannelController
{
	@FXML
	private TextField pchnNameLabel;
	@FXML
	private TextField pchnFileClassLabel;
	@FXML
	private TextField pchnFileTypeLabel;
	@FXML
	private ComboBox<HBoxChannelCell> pchnPipelineChannelCombo;
	@FXML
	private Label pchnPipelineSpecificationsLabel;
	@FXML
	private Label pchnPipelineFileHandlersLabel;
	@FXML
	private GridPane pchnPipelineSpecificationsGrid;
	@FXML
	private GridPane pchnPipelineFileHandlersGrid;
	@FXML
	private ListView<HBoxSpecHistoryCell> pchnPipelineSpecificationHistoryList;
	@FXML
	private TableView<SpecCell> pchnSpecTableView;
	@FXML
	private TableView<StepHandlerCell> pchnStepHandlerTableView;
	@FXML
	private Button pchnEditButton;
	@FXML
	private Button pchnAddChannelButton;
	@FXML
	private TextField pchnFilterField;
	@FXML
	private Button pchnRefreshButton;
	@FXML
	private Button pchnRefreshBusyButton;
	@FXML
	private Button pchnClearFilterButton;
	@FXML
	private Button pchnAddSpecButton;
	@FXML
	private CheckBox pchInactiveCheck;
	@FXML
	private CheckBox pchDeletedCheck;
	@FXML
	private Button pchnSystemDataButton;
	
	boolean active = false;
	
	//table sort 
	TableColumn<SpecCell, String> sortColumn = null;
	TableColumn<StepHandlerCell, String> stepHandlerSortColumn = null;

	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize()
	{
		initControls();
		loadChannels();

		//check to see if we have an active channel
		if(DataManager.i().mPipelineChannel != null)
			updatePipelineChannelData();		
	}

	// resets the selected pipeline channel in case it was changed elsewhere
	public void setChannel() 
	{
		// new thread
		Task<Void> task = new Task<Void>() 
		{
            @Override
            protected Void call() throws Exception 
            {
        		// refresh the spec
        		if(pchnPipelineChannelCombo.getValue() != null)
        			DataManager.i().mPipelineChannel = pchnPipelineChannelCombo.getValue().getChannel();
        		else {
            		EtcAdmin.i().setStatusMessage("Ready");
            		EtcAdmin.i().setProgress(0);
                    return null;        			
        		}
        			PipelineSpecificationRequest request = new PipelineSpecificationRequest(DataManager.i().mPipelineSpecification);
        			request.setChannelId(DataManager.i().mPipelineChannel.getId());
        			request.setFetchInactive(true);
        			DataManager.i().mPipelineSpecifications = AdminPersistenceManager.getInstance().getAll(request);
        		
        		EtcAdmin.i().setStatusMessage("Ready");
        		EtcAdmin.i().setProgress(0);
                return null;
            }
        };
        
      	task.setOnScheduled(e ->  {
  		EtcAdmin.i().setStatusMessage("Updating Specifications...");
  		EtcAdmin.i().setProgress(0.5);});
      			
    	task.setOnSucceeded(e ->  loadFileSpecifications());
    	task.setOnFailed(e ->  loadFileSpecifications());
        new Thread(task).start();
	}
	
	private void initControls() 
	{
		// set the spec table columns
		setSpecTableColumns();
		setStepHandlerTableColumns();
		
		// aesthetics
		pchnPipelineSpecificationsGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		pchnPipelineSpecificationsLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));	
		pchnPipelineFileHandlersLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));	

		//override the string converters for the combo box edit control
		pchnPipelineChannelCombo.setConverter(new StringConverter<HBoxChannelCell>() 
		{
			@Override
		    public HBoxChannelCell fromString(String string) 
			{
				if(pchnPipelineChannelCombo.getValue() != null)
					return pchnPipelineChannelCombo.getValue();
		        return null;
		    } 

			@Override
			public String toString(HBoxChannelCell object)
			{
				if(object != null && object.getChannel() != null)
					return object.getChannel().getName();
				return null;
			}
		}); 

		//add handlers for listbox selection notification
		// SPECIFICATIONS
		pchnSpecTableView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) 
            {
            	SpecCell cell = pchnSpecTableView.getSelectionModel().getSelectedItem();
            	DataManager.i().mPipelineSpecification = cell.getSpec();

            	try {
        			PipelineSpecificationRequest request = new PipelineSpecificationRequest(DataManager.i().mPipelineSpecification);
					DataManager.i().mPipelineSpecification = AdminPersistenceManager.getInstance().get(request);
					// remove from historyif already found
					for (int i = 0; i < pchnPipelineSpecificationHistoryList.getItems().size(); i++) {	
						HBoxSpecHistoryCell hCell = pchnPipelineSpecificationHistoryList.getItems().get(i);
						if (hCell == null || hCell.getSpec() == null) continue;
						
						if (hCell.getSpec() != null && hCell.getSpec().getId() == DataManager.i().mPipelineSpecification.getId()) {
							pchnPipelineSpecificationHistoryList.getItems().remove(i);
							break;
						}
					}

					// add it to our history if not present
					pchnPipelineSpecificationHistoryList.getItems().add(0,new HBoxSpecHistoryCell(DataManager.i().mPipelineSpecification));

					// limit history to 25
					if (pchnPipelineSpecificationHistoryList.getItems().size() > 25) 
						pchnPipelineSpecificationHistoryList.getItems().remove(25);
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }

            	EtcAdmin.i().setScreen(ScreenType.PIPELINESPECIFICATION, true);
            }
        });

		pchnPipelineSpecificationHistoryList.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) 
            {
				// offset one for the header row
            	HBoxSpecHistoryCell cell = pchnPipelineSpecificationHistoryList.getSelectionModel().getSelectedItem();
            	if (cell == null) return;
            	DataManager.i().mPipelineSpecification = cell.getSpec(); // mPipelineSpecifications.get(nSpecificationID);

            	try {
        			PipelineSpecificationRequest request = new PipelineSpecificationRequest(DataManager.i().mPipelineSpecification);
					DataManager.i().mPipelineSpecification = AdminPersistenceManager.getInstance().get(request);
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }

            	EtcAdmin.i().setScreen(ScreenType.PIPELINESPECIFICATION, true);
            }
        });

		// FILE HANDLERS
		pchnStepHandlerTableView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1)
            {
				// offset one for the header row
				DataManager.i().mPipelineFileStepHandler = pchnStepHandlerTableView.getSelectionModel().getSelectedItem().getStepHandler();
            	viewSelectedStepHandlers();
            }
        });

		//disable the view selection buttons until something is selected
		disableControls();

		//set functionality according to the user security level
		//pchnAddChannelButton.setDisable(!Utils.userCanAdd());
		//disabled for now
		pchnAddChannelButton.setDisable(true);
	}	
	
	private void setSpecTableColumns() 
	{
		//clear the default values
		pchnSpecTableView.getColumns().clear();

	    TableColumn<SpecCell, String> x1 = new TableColumn<SpecCell, String>("Spec Id");
		x1.setCellValueFactory(new PropertyValueFactory<SpecCell,String>("id"));
		x1.setMinWidth(100);
		x1.setComparator((String o1, String o2) -> { return Utils.compareNumberStrings(o1, o2); });
		pchnSpecTableView.getColumns().add(x1);
		setCellFactory(x1);
		sortColumn = x1;
		sortColumn.setSortType(SortType.ASCENDING);
		
	    TableColumn<SpecCell, String> x2 = new TableColumn<SpecCell, String>("Mapper Id");
		x2.setCellValueFactory(new PropertyValueFactory<SpecCell,String>("mapperId"));
		x2.setMinWidth(100);
		pchnSpecTableView.getColumns().add(x2);
		setCellFactory(x2);
		
	    TableColumn<SpecCell, String> x3 = new TableColumn<SpecCell, String>("Date");
		x3.setCellValueFactory(new PropertyValueFactory<SpecCell,String>("date"));
		x3.setMinWidth(150);
		x3.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		pchnSpecTableView.getColumns().add(x3);
		setCellFactory(x3);
		
	    TableColumn<SpecCell, String> x4 = new TableColumn<SpecCell, String>("Description");
		x4.setCellValueFactory(new PropertyValueFactory<SpecCell,String>("name"));
		x4.setMinWidth(350);
		pchnSpecTableView.getColumns().add(x4);
		setCellFactory(x4);
		
	}

	private void setCellFactory(TableColumn<SpecCell, String>  col) 
	{
		col.setCellFactory(column -> 
		{
		    return new TableCell<SpecCell, String>() 
		    {
		        @Override
		        protected void updateItem(String item, boolean empty) 
		        {
		            super.updateItem(item, empty);
		            if(item == null || empty) 
		            { 
		                setText(null);
		                setStyle("");
		            } else {
		                setText(item);
		                SpecCell cell = getTableView().getItems().get(getIndex());
		                if(cell.getSpec().isActive() == false)
		                	setTextFill(Color.BLUE);
		                else
		                	setTextFill(Color.BLACK);
		            }
		        }
		    };
		});
	}

	private void setStepHandlerTableColumns() 
	{
		//clear the default values
		pchnStepHandlerTableView.getColumns().clear();

	    TableColumn<StepHandlerCell, String> x1 = new TableColumn<StepHandlerCell, String>("Id");
		x1.setCellValueFactory(new PropertyValueFactory<StepHandlerCell,String>("id"));
		x1.setMinWidth(100);
		x1.setComparator((String o1, String o2) -> { return Utils.compareNumberStrings(o1, o2); });
		pchnStepHandlerTableView.getColumns().add(x1);
		setStepHandlerCellFactory(x1);
		stepHandlerSortColumn = x1;
		stepHandlerSortColumn.setSortType(SortType.ASCENDING);
		
	    TableColumn<StepHandlerCell, String> x2 = new TableColumn<StepHandlerCell, String>("Type");
		x2.setCellValueFactory(new PropertyValueFactory<StepHandlerCell,String>("type"));
		x2.setMinWidth(150);
		pchnStepHandlerTableView.getColumns().add(x2);
		setStepHandlerCellFactory(x2);
		
	    TableColumn<StepHandlerCell, String> x3 = new TableColumn<StepHandlerCell, String>("Description");
		x3.setCellValueFactory(new PropertyValueFactory<StepHandlerCell,String>("name"));
		x3.setMinWidth(400);
		pchnStepHandlerTableView.getColumns().add(x3);
		setStepHandlerCellFactory(x3);
	}

	private void setStepHandlerCellFactory(TableColumn<StepHandlerCell, String>  col) 
	{
		col.setCellFactory(column -> 
		{
		    return new TableCell<StepHandlerCell, String>() 
		    {
		        @Override
		        protected void updateItem(String item, boolean empty) 
		        {
		            super.updateItem(item, empty);
		            if(item == null || empty) 
		            { 
		                setText(null);
		                setStyle("");
		            } else {
		                setText(item);
		                StepHandlerCell cell = getTableView().getItems().get(getIndex());
		                if(cell.getStepHandler().isActive() == false)
		                	setTextFill(Color.BLUE);
		                else
		                	setTextFill(Color.BLACK);
		            }
		        }
		    };
		});
	}

	private void disableControls() 
	{
		pchnNameLabel.setDisable(true);
		pchnEditButton.setDisable(true);
		pchnFileClassLabel.setDisable(true);
		pchnFileTypeLabel.setDisable(true);	
		pchnRefreshButton.setDisable(true);
		pchnRefreshBusyButton.setVisible(false);
		pchnClearFilterButton.setDisable(true);
		pchnFilterField.setDisable(true);
		pchnAddSpecButton.setDisable(true);
		pchnSystemDataButton.setDisable(true);
	}
	
	private void enableControls()
	{
		pchnNameLabel.setDisable(false);
		pchnFileClassLabel.setDisable(false);
		pchnFileTypeLabel.setDisable(false);	
		pchnSystemDataButton.setDisable(false);

		//set functionality according to the user security level
		//pchnEditButton.setDisable(!Utils.userCanEdit());	
		// disabled for now
		pchnEditButton.setDisable(true);	
	}
	
	private void updatePipelineChannelData()
	{
		// clear the filter
		pchnFilterField.setText("");
		
		PipelineChannel pChannel = DataManager.i().mPipelineChannel;

		if(pChannel != null) 
		{
			Utils.updateControl(pchnNameLabel,pChannel.getName());
			Utils.updateControl(pchnFileClassLabel,pChannel.getPipelineFileClass());
			if(pChannel.getType() != null)
				Utils.updateControl(pchnFileTypeLabel,pChannel.getType().toString());
			else
				pchnFileTypeLabel.setText("");
			
			
			//enable the controls
			enableControls();
			
			//load the lists
			active = true;
			loadFileStepHandlers();
			loadFileSpecifications();
			
			//update the breadcrumb
			EtcAdmin.i().setBreadCrumbLabel(2,pChannel.getName() );
		}
	}
	
	private void loadFileSpecifications()
	{
		// reset the refresh buttons
		pchnRefreshButton.setVisible(true);
		pchnRefreshBusyButton.setVisible(false);
		// if not active, leave
		if(active == false) return;
		
		pchnSpecTableView.getItems().clear();
		//enable the controls
		pchnRefreshButton.setDisable(false);
		pchnClearFilterButton.setDisable(false);
		pchnFilterField.setDisable(false);
		pchnAddSpecButton.setDisable(false);
		
		// load the specifications
		if(DataManager.i().mPipelineSpecifications != null) 
		{
		    String filterSource = "";
		    
			for(PipelineSpecification pipelineSpecification : DataManager.i().mPipelineSpecifications) 
			{
				if(pipelineSpecification.isActive() == false && pchInactiveCheck.isSelected() == false) continue;
		    	if(pipelineSpecification.isDeleted() == true && pchDeletedCheck.isSelected() == false) continue;
				if(pchnFilterField.getText().length() > 0) 
				{
					filterSource = pipelineSpecification.getName() + 
							" " + String.valueOf(pipelineSpecification.getId()) + 
							" " + String.valueOf(pipelineSpecification.getDynamicFileSpecificationId()) + 
							" " + Utils.getDateString(pipelineSpecification.getLastUpdated()); 
									
			    	if(filterSource.toUpperCase().contains(pchnFilterField.getText().toUpperCase()) == false)
			    		continue;
				}
				
				pchnSpecTableView.getItems().add(new SpecCell(pipelineSpecification));
			}
			// add the defaut sort
			pchnSpecTableView.getSortOrder().add(sortColumn);
	        sortColumn.setSortable(true);

	        pchnPipelineSpecificationsLabel.setText("Channel Specifications (total: " + pchnSpecTableView.getItems().size() + " of "+ String.valueOf(DataManager.i().mPipelineSpecifications.size()) + ")" );
		}else 	
	        pchnPipelineSpecificationsLabel.setText("Channel Specifications (total: 0)" );
	}

	private void loadFileStepHandlers() 
	{
		try {
			PipelineFileStepHandlerRequest request = new PipelineFileStepHandlerRequest();
			DataManager.i().mPipelineFileStepHandlers = AdminPersistenceManager.getInstance().getAll(request);
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
		pchnStepHandlerTableView.getItems().clear();
		if(DataManager.i().mPipelineFileStepHandlers != null) 
		{
		    for(PipelineFileStepHandler pipelineFileStepHandler : DataManager.i().mPipelineFileStepHandlers) 
		    {
		    	if (pipelineFileStepHandler.getChannelId().equals(DataManager.i().mPipelineChannel.getId()) == false) continue;
		    	pchnStepHandlerTableView.getItems().add(new StepHandlerCell(pipelineFileStepHandler));
			}

			// add the defaut sort
		    pchnStepHandlerTableView.getSortOrder().add(stepHandlerSortColumn);
	        sortColumn.setSortable(true);

	        pchnPipelineFileHandlersLabel.setText("Channel Step Handlers (total: " + String.valueOf(DataManager.i().mPipelineFileStepHandlers.size()) + ")" );
		}else 	
	        pchnPipelineFileHandlersLabel.setText("Channel Step Handlers (total: 0)" );
	}
	
	//extending the listview for our additional controls
	public class HBoxCell extends HBox 
	{
         Label lblCol1 = new Label();
         Label lblCol2 = new Label();
         Label lblCol3 = new Label();
         int cellLocation;

         HBoxCell(int location, String col1, String col2, String col3, boolean isHeader, int columns) 
         {
              super();

              if(col1 == null ) col1 = "";
              if(col2 == null ) col2 = "";
              if(col3 == null ) col3 = "";
              
              cellLocation = location;
              
              lblCol1.setText(col1);
              lblCol1.setMinWidth(50);
              HBox.setHgrow(lblCol1, Priority.ALWAYS);

              lblCol2.setText(col2);
              lblCol2.setMinWidth(100);
              HBox.setHgrow(lblCol2, Priority.ALWAYS);

              lblCol3.setText(col3);
              lblCol3.setMinWidth(500);
              HBox.setHgrow(lblCol3, Priority.ALWAYS);

              if(isHeader == true) {
            	  lblCol1.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblCol2.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblCol3.setFont(Font.font(null, FontWeight.BOLD, 13));
              }
              
              if(columns == 2)
            	  this.getChildren().addAll(lblCol1, lblCol3);
              else
            	  this.getChildren().addAll(lblCol1, lblCol2, lblCol3);
         }
         
         int getLocation() { 
        	 return cellLocation;
         }
    }	
	
	
	public class HBoxChannelCell extends HBox 
	{
         Label lblName = new Label();
         PipelineChannel channel; 
         
         public PipelineChannel getChannel() { 
        	 return channel;
         }

         HBoxChannelCell(PipelineChannel channel) 
         {
              super();
              this.channel = channel;
              Utils.setHBoxLabel(lblName, 300, false, channel.getName());
              this.getChildren().addAll(lblName);
         }
    }	
	
	public class SpecCell 
	{
		SimpleStringProperty id = new SimpleStringProperty();
 		SimpleStringProperty mapperId = new SimpleStringProperty();
 		SimpleStringProperty date = new SimpleStringProperty();
 		SimpleStringProperty name = new SimpleStringProperty();
 		PipelineSpecification spec;
         
        public String getId() {
        	return id.get();
        }
         
        public String getMapperId() {
        	return mapperId.get();
        }

        public String getDate() {
        	return date.get();
        }

        public String getName() {
        	return name.get();
        }

        PipelineSpecification getSpec() {
        	 return spec;
         }

         SpecCell(PipelineSpecification spec) 
         {
             super();

             this.spec = spec;
             
             if(spec != null) 
             {
            	 id.set(String.valueOf(spec.getId())); 
            	 if (spec.getDynamicFileSpecificationId() != null)
            		 mapperId.set(String.valueOf(spec.getDynamicFileSpecificationId())); 
            	 name.set(spec.getName()); 
        		 date.set(Utils.getDateString(spec.getLastUpdated()));
             }

         }
    }
	
	public class StepHandlerCell 
	{
		SimpleStringProperty id = new SimpleStringProperty();
 		SimpleStringProperty type = new SimpleStringProperty();
 		SimpleStringProperty name = new SimpleStringProperty();
 		PipelineFileStepHandler stepHandler;
         
        public String getId() {
        	return id.get();
        }
         
        public String getType() {
        	return type.get();
        }

        public String getName() {
        	return name.get();
        }

        PipelineFileStepHandler getStepHandler() {
        	 return stepHandler;
         }

        StepHandlerCell(PipelineFileStepHandler stepHandler) 
         {
             super();

             this.stepHandler = stepHandler;
             
             if(stepHandler != null) 
             {
            	 id.set(String.valueOf(stepHandler.getId())); 
            	 name.set(stepHandler.getName()); 
            	 if (stepHandler.getType() != null)
            		 type.set(String.valueOf(stepHandler.getType())); 
             }

         }
    }
	
	public class HBoxSpecHistoryCell extends HBox 
	{
         Label lblId = new Label();
         Label lblDynFileSpecId = new Label();
         Label lblCalcId = new Label();
         Label lblLastUpdated = new Label();
         Label lblName = new Label();
         Label lblType = new Label();
         int cellLocation;
         PipelineSpecification spec;
         
         PipelineSpecification getSpec() {
        	 return spec;
         }

         HBoxSpecHistoryCell(PipelineSpecification spec) 
         {
             super();

             this.spec = spec;
             
             if(spec != null) 
             {
              	  Utils.setHBoxLabel(lblId, 70, false, spec.getId());
              	  Utils.setHBoxLabel(lblDynFileSpecId, 90, false, spec.getDynamicFileSpecificationId());
              	  Utils.setHBoxLabel(lblLastUpdated, 150, false, spec.getLastUpdated());
              	  Utils.setHBoxLabel(lblName, 250, false, spec.getName());
              	  if (spec.getChannel() != null && spec.getChannel().getType() != null)
              		  Utils.setHBoxLabel(lblType, 200, false, spec.getChannel().getType().toString());
              	  else
              		  Utils.setHBoxLabel(lblType, 200, false, "");

                  if(spec.isDeleted() == true)
                  {
                  	 lblId.setTextFill(Color.RED);
                  	 lblName.setTextFill(Color.RED);
                  	 lblDynFileSpecId.setTextFill(Color.RED);
                  	 lblLastUpdated.setTextFill(Color.RED);
                  } else {
                 	 if(spec.isActive() == false)
                 	 {
                      	 lblId.setTextFill(Color.BLUE);
                      	 lblName.setTextFill(Color.BLUE);
                      	 lblDynFileSpecId.setTextFill(Color.BLUE);
                      	 lblLastUpdated.setTextFill(Color.BLUE);
                 	  } else {
                       	 lblId.setTextFill(Color.BLACK);
                       	 lblName.setTextFill(Color.BLACK);
                       	 lblDynFileSpecId.setTextFill(Color.BLACK);
                       	 lblLastUpdated.setTextFill(Color.BLACK);
                 	  }
                   }

             }else {
              	  Utils.setHBoxLabel(lblId, 70, true, "Spec Id");
              	  Utils.setHBoxLabel(lblDynFileSpecId, 90, false, "Mapper Id");
              	  Utils.setHBoxLabel(lblLastUpdated,150, false, "LastUpdate");
              	  Utils.setHBoxLabel(lblName, 250, false, "Description");
              	  Utils.setHBoxLabel(lblType, 200, false, "Type");
             }
             this.getChildren().addAll(lblId, lblDynFileSpecId, lblLastUpdated, lblName, lblType);
        }
         
         int getLocation() { 
        	 return cellLocation;
         }
    }	
		
	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINECHANNELEDIT, true);
	}	
	
	@FXML
	private void onAdd(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINECHANNELADD, true);
	}		
	
	@FXML
	private void onShowInactive(ActionEvent event) {
		loadFileSpecifications();
	}		
	
	private void showSpecPopup() 
	{
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/specification/ViewPipelineSpecificationAdd.fxml"));
			Parent ControllerNode = loader.load();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        stage.onCloseRequestProperty().setValue(e -> reloadChannels());
	        stage.onHiddenProperty().setValue(e -> reloadChannels());
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (IOException e) {
			 DataManager.i().log(Level.SEVERE, e);
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void reloadChannels() {
		setChannel(DataManager.i().mPipelineChannel);
	}

	private void viewSelectedStepHandlers() 
	{
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filestephandler/ViewFileStepHandler.fxml"));
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
	private void onAddSpecification(ActionEvent event) {
		showSpecPopup();
	}		
	
	@FXML
	private void onChannelsComboHide(ActionEvent event)
	{
		if(pchnPipelineChannelCombo.getValue() != null)
			setChannel(pchnPipelineChannelCombo.getValue().getChannel());
	}
	
	@FXML
	private void onFilterKeyPressed() {
		//reload and apply filter if there is one
		loadFileSpecifications();
	}
	
	@FXML
	private void onRefresh() 
	{
		pchnRefreshButton.setVisible(false);
		pchnRefreshBusyButton.setVisible(true);
		setChannel();
	}
	
	@FXML
	private void onClearFilter() 
	{
		// Clear filter and reload
		pchnFilterField.setText("");
		loadFileSpecifications();
	}
		
	@FXML
	private void onClearSpecificationHistory() 
	{
		pchnPipelineSpecificationHistoryList.getItems().clear();
	}
		
	@FXML
	private void onShowSystemInfo() {
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mPipelineChannel;
			DataManager.i().mCurrentCoreDataType = SystemDataType.CHANNEL;
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
	
	private void loadChannels() 
	{
		pchnPipelineChannelCombo.getItems().clear();
		for(PipelineChannel channel : DataManager.i().mPipelineChannels)
		{
			if(channel != null)
				pchnPipelineChannelCombo.getItems().add(new HBoxChannelCell(channel));
		}

		//pchnPipelineChannelCombo.hide();
	}
	
	public void setChannel(PipelineChannel channel) 
	{
		Task<Void> task = new Task<Void>()
		{
            @Override
            protected Void call() throws Exception 
            {
        		EtcAdmin.i().setStatusMessage("loading Pipeline Channel Specs...");
        		EtcAdmin.i().setProgress(.4);
        		
        		// set the current channel
            	DataManager.i().mPipelineChannel = channel;

            	// get the specs for this channel
    			PipelineSpecificationRequest request = new PipelineSpecificationRequest(DataManager.i().mPipelineSpecification);
    			request.setChannelId(channel.getId());
    			request.setFetchInactive(true);
    			DataManager.i().mPipelineSpecifications = AdminPersistenceManager.getInstance().getAll(request);
    			
    			// finished, return
        		EtcAdmin.i().setStatusMessage("Ready");
        		EtcAdmin.i().setProgress(0);
                return null;
            }
        };
        
    	task.setOnSucceeded(e ->   updatePipelineChannelData());
    	task.setOnFailed(e ->   updatePipelineChannelData());
        new Thread(task).start();
	}
}
