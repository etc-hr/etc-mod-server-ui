package com.etc.admin.ui.calc;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.ui.coresysteminfo.ViewCoreSystemInfoController;
import com.etc.admin.ui.hr.ViewHREmployerController.HRCell;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.ems.calc.entities.CalculationChannel;
import com.etc.corvetto.ems.calc.entities.CalculationSpecification;
import com.etc.corvetto.ems.calc.entities.CalculationStepHandler;
import com.etc.corvetto.entities.Employer;
import com.etc.entities.CoreData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class ViewCalcChannelController {
	@FXML
	private Label cchnNameLabel;
	@FXML
	private TextField cchnDescriptionField;
	@FXML
	private TextField cchnCalcClassField;
	@FXML
	private TextField cchnCalcInterfaceClassField;
	@FXML
	private TextField cchnIntegratorClassField;
	@FXML
	private ComboBox<String> cchnCalcChannelCombo;
	@FXML
	private Label cchnCalcSpecificationsLabel;
	@FXML
	private Label cchnCalcStepHandlersLabel;
	@FXML
	private ListView<HBoxSpecCell> cchnCalcSpecificationsList;
	@FXML
	private ListView<HBoxCell> cchnCalcStepHandlersList;
	@FXML
	private Button cchnEditButton;
	@FXML
	private Button cchnAddChannelButton;
	@FXML
	private Label cchnCoreIdLabel;
	@FXML
	private Label cchnCoreActiveLabel;
	@FXML
	private Label cchnCoreBODateLabel;
	@FXML
	private Label cchnCoreLastUpdatedLabel;
	@FXML
	private TextField cchnFilterField;
	@FXML
	private Button cchnFilterButton;
	@FXML
	private Button cchnClearFilterButton;
	@FXML
	private Button cchnAddSpecButton;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {

		initControls();
		//loadColumnFields();
		
		loadSearchFiles();
		
		//check to see if we have an active channel
		if (DataManager.i().mCalculationChannel != null)
			updateCalcChannelData();		
	}

	private void initControls() {
		//add handlers for listbox selection notification
		// SPECIFICATIONS
		cchnCalcSpecificationsList.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
            	HBoxSpecCell cell = cchnCalcSpecificationsList.getSelectionModel().getSelectedItem();
            	DataManager.i().mPipelineSpecification = DataManager.i().mPipelineSpecifications.get(cell.getLocation());
        		EtcAdmin.i().setScreen(ScreenType.PIPELINESPECIFICATION, true);		
            }
        });
		
		// FILE HANDLERS
		cchnCalcStepHandlersList.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
            	DataManager.i().mPipelineFileStepHandler = DataManager.i().mPipelineFileStepHandlers.get(cchnCalcStepHandlersList.getSelectionModel().getSelectedIndex() - 1);
        		EtcAdmin.i().setScreen(ScreenType.PIPELINEFILESTEPHANDLER, true);		
            }
        });

		//disable the view selection buttons until something is selected
		disableControls();

		//set functionality according to the user security level
		//cchnAddChannelButton.setDisable(!Utils.userCanAdd());
		//disabled for now
		cchnAddChannelButton.setDisable(true);
	}	
	
	private void disableControls() {
	//	cchnNameLabel.setDisable(true);
		cchnDescriptionField.setDisable(true);
		cchnEditButton.setDisable(true);

		cchnCalcInterfaceClassField.setDisable(true);
		cchnCalcClassField.setDisable(true);	
		cchnIntegratorClassField.setDisable(true);	
		cchnFilterButton.setDisable(true);
		cchnClearFilterButton.setDisable(true);
		cchnFilterField.setDisable(true);
		cchnAddSpecButton.setDisable(true);
	}
	
	private void enableControls() {
	//	cchnNameLabel.setDisable(false);
		cchnDescriptionField.setDisable(false);
		cchnCalcInterfaceClassField.setDisable(false);
		cchnCalcClassField.setDisable(false);	
		cchnIntegratorClassField.setDisable(false);	

		// disabled for now
		cchnEditButton.setDisable(true);	
	}
	
	private void updateCalcChannelData(){
		// clear the filter
		cchnFilterField.setText("");
		
		CalculationChannel pChannel = DataManager.i().mCalculationChannel;
		if (pChannel != null) {
			Utils.updateControl(cchnNameLabel,pChannel.getName());
			Utils.updateControl(cchnDescriptionField,pChannel.getDescription());
			cchnCalcInterfaceClassField.setDisable(false);
			cchnCalcClassField.setDisable(false);	
			cchnIntegratorClassField.setDisable(false);	

			Utils.updateControl(cchnCalcClassField,pChannel.getCalculationClass());
			Utils.updateControl(cchnCalcInterfaceClassField,pChannel.getCalculatorInterfaceClass());
			Utils.updateControl(cchnIntegratorClassField,pChannel.getIntegratorInterfaceClass());
			
			//core data read only
			Utils.updateControl(cchnCoreIdLabel,String.valueOf(pChannel.getId()));
			Utils.updateControl(cchnCoreActiveLabel,String.valueOf(pChannel.isActive()));
			Utils.updateControl(cchnCoreBODateLabel,pChannel.getBornOn());
			Utils.updateControl(cchnCoreLastUpdatedLabel,pChannel.getLastUpdated());
			
			//enable the controls
			enableControls();
			
			//load the lists
			loadCalcSpecifications();
			loadStepHandlers();
			
			//update the breadcrumb
			EtcAdmin.i().setBreadCrumbLabel(2,pChannel.getName() );
		}
	}
	
	private void loadCalcSpecifications() {
		cchnCalcSpecificationsList.getItems().clear();
		//enable the controls
		cchnFilterButton.setDisable(false);
		cchnClearFilterButton.setDisable(false);
		cchnFilterField.setDisable(false);
		cchnAddSpecButton.setDisable(false);
		
		// load the specifications
		if (DataManager.i().mCalculationStepHandlers != null) {
		    List<HBoxSpecCell> handlerList = new ArrayList<>();
		    handlerList.add(new HBoxSpecCell(0, "ID", "IntegratorID", "CalcId", "LastUpdate", "Description", true));
		    int location = -1;
		    String filterSource = "";
			for (CalculationSpecification CalcSpecification : DataManager.i().mCalculationSpecifications ) {
				location++;
				if (cchnFilterField.getText().length() > 0) {
					filterSource = CalcSpecification.getName() + 
							" " + String.valueOf(CalcSpecification.getId()) + 
							" " + Utils.getDateString(CalcSpecification.getLastUpdated()); 
									
			    	if (filterSource.toUpperCase().contains(cchnFilterField.getText().toUpperCase()) == false)
			    		continue;
				}
				handlerList.add(new HBoxSpecCell(location,String.valueOf(CalcSpecification.getId()), 
						String.valueOf(CalcSpecification.getIntegratorId()),
						String.valueOf(CalcSpecification.getCalculatorId()),
						Utils.getDateString(CalcSpecification.getLastUpdated()),
			    		CalcSpecification.getName(), false));
			}
			ObservableList<HBoxSpecCell> myObservableList = FXCollections.observableList(handlerList);
			cchnCalcSpecificationsList.setItems(myObservableList);			    
	        cchnCalcSpecificationsLabel.setText("Channel Specifications (total: " + String.valueOf(DataManager.i().mCalculationSpecifications.size()) + ")" );
		}else 	
	        cchnCalcSpecificationsLabel.setText("Channel Specifications (total: 0)" );

	}
	
	private void loadStepHandlers() {
		cchnCalcStepHandlersList.getItems().clear();
		if (DataManager.i().mCalculationStepHandlers != null) {
		    List<HBoxCell> handlerList = new ArrayList<>();
		    handlerList.add(new HBoxCell(0,"ID", "Type", "Description", true, 3));

		    for (CalculationStepHandler CalcFileStepHandler : DataManager.i().mCalculationStepHandlers ) {
			    handlerList.add(new HBoxCell(0,String.valueOf(CalcFileStepHandler.getId()), 
			    							 CalcFileStepHandler.getType().toString(),
			    							 CalcFileStepHandler.getDescription(), false, 3));
			}
		 
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(handlerList);
			cchnCalcStepHandlersList.setItems(myObservableList);			    
	        cchnCalcStepHandlersLabel.setText("Channel Step Handlers (total: " + String.valueOf(DataManager.i().mCalculationStepHandlers.size()) + ")" );
		}else 	
	        cchnCalcStepHandlersLabel.setText("Channel Step Handlers (total: 0)" );

	}
	
	//extending the listview for our additional controls
	public class HBoxCell extends HBox {
         Label lblCol1 = new Label();
         Label lblCol2 = new Label();
         Label lblCol3 = new Label();
         int cellLocation;

         HBoxCell(int location, String col1, String col2, String col3, boolean isHeader, int columns) {
              super();

              if (col1 == null ) col1 = "";
              if (col2 == null ) col2 = "";
              if (col3 == null ) col3 = "";
              
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

              if (isHeader == true) {
            	  lblCol1.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblCol2.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblCol3.setFont(Font.font(null, FontWeight.BOLD, 13));
              }
              
              if (columns == 2)
            	  this.getChildren().addAll(lblCol1, lblCol3);
              else
            	  this.getChildren().addAll(lblCol1, lblCol2, lblCol3);
            	  
        }
         
         int getLocation() { 
        	 return cellLocation;
         }
    }	
		
	//extending the listview for our additional controls
	public class HBoxSpecCell extends HBox {
         Label lblId = new Label();
         Label lblDynFileSpecId = new Label();
         Label lblCalcId = new Label();
         Label lblLastUpdated = new Label();
         Label lblName = new Label();
         int cellLocation;

         HBoxSpecCell(int location, String id, String dynFileSpecId, String calcId, String lastUpdated, String name, boolean isHeader) {
              super();

              if (id == null ) id = "";
              if (dynFileSpecId == null ) dynFileSpecId = "";
              if (calcId == null ) calcId = "";
              if (lastUpdated == null ) lastUpdated = "";
              if (name == null ) name = "";
              
              cellLocation = location;
              
              lblId.setText(id);
              lblId.setMinWidth(75);
              lblId.setAlignment(Pos.CENTER);
              HBox.setHgrow(lblId, Priority.ALWAYS);

              lblDynFileSpecId.setText(dynFileSpecId);
              lblDynFileSpecId.setMinWidth(75);
              lblDynFileSpecId.setAlignment(Pos.CENTER);
              HBox.setHgrow(lblDynFileSpecId, Priority.ALWAYS);

              lblCalcId.setText(calcId);
              lblCalcId.setMinWidth(75);
              HBox.setHgrow(lblCalcId, Priority.ALWAYS);

              lblLastUpdated.setText(lastUpdated);
              lblLastUpdated.setMinWidth(150);
              lblLastUpdated.setAlignment(Pos.CENTER);
              HBox.setHgrow(lblLastUpdated, Priority.ALWAYS);

              lblName.setText(name);
              lblName.setMinWidth(450);
              HBox.setHgrow(lblName, Priority.ALWAYS);

              if (isHeader == true) {
            	  lblId.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblDynFileSpecId.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblCalcId.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblLastUpdated.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblName.setFont(Font.font(null, FontWeight.BOLD, 13));
              }
              
              //this.getChildren().addAll(lblId, lblDynFileSpecId, lblCalcId, lblLastUpdated, lblName);
              this.getChildren().addAll(lblId, lblDynFileSpecId, lblLastUpdated, lblName);
        }
         
         int getLocation() { 
        	 return cellLocation;
         }
    }	
		
	@FXML
	private void onEdit(ActionEvent event) {
	}	
	
	@FXML
	private void onAdd(ActionEvent event) {
	}		
	
	@FXML
	private void onAddSpecification(ActionEvent event) {
		//EtcAdmin.i().setScreen(ScreenType.CALCSPECIFICATIONADD, true);
	}		
	
	@FXML
	public void onSearchHide() {
		// if we already have an account selected, show it
	//	if (cchnCalcChannelCombo.getValue() != "")
//			searchCalcChannels();
	}
	
	@FXML
	private void onApplyFilter() {
		//reload and apply filter if there is one
		loadCalcSpecifications();
	}
	
	@FXML
	private void onClearFilter() {
		// Clear filter and reload
		cchnFilterField.setText("");
		loadCalcSpecifications();
	}
		
	public void loadSearchFiles() {
		if (DataManager.i().mCalculationChannels == null) return;
		
		String sName;
		ObservableList<String> searchFiles = FXCollections.observableArrayList();
		for (int i = 0; i < DataManager.i().mCalculationChannels.size();i++) {
			sName = DataManager.i().mCalculationChannels.get(i).getName();
			if (sName != null)
				searchFiles.add(sName);
		};		
		
		//finally, set our filtered items as the combobox collection
        cchnCalcChannelCombo.setItems(searchFiles);	
		//close the dropdown if it is showing
        cchnCalcChannelCombo.hide();
	}
	
	public void onSearchCalcChannels(ActionEvent event) {
		searchCalcChannels();
	}

	public void searchCalcChannels() {
		int selection = cchnCalcChannelCombo.getSelectionModel().getSelectedIndex();
		setChannel(selection);
	}	
		
	public void setChannel(int nChannelID) {
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
        		//only load them if we haven't this session
            	/*		EtcAdmin.i().setStatusMessage("loading Calculation Channel...");
            			EtcAdmin.i().setProgress(.1);
            			if(DataManager.i().mPipelineChannels == null) return;

            			DataManager.i().mPipelineChannel = DataManager.i().mPipelineChannels.get(nChannelID);
            	    	EtcAdmin.i().setProgress(.2);
            			//verify with the API
            			try {
            				DataManager.i().mPipelineChannel = mCoreManager.getPipelineChannel(mPipelineChannel);
            				// load any updated specifications
            		    	EtcAdmin.i().setProgress(.3);
            				loadUpdatedPipelineSpecifications();
            				// load updated step handlers
            		    	EtcAdmin.i().setProgress(.6);
            				loadUpdatedPipelineFileStepHandlers();
            				//update the status
            				EtcAdmin.i().setStatusMessage("Ready");
            				EtcAdmin.i().setProgress(0);
            			} catch (CoreException | InterruptedException | SQLException | ParseException e) {
            				EtcAdmin.i().setStatusMessage("Communication Error in LoadCalculationChannel. Please retry later.");
            				EtcAdmin.i().setProgress(0);
            				return;
            			}
            	*/
                //action
                return null;
            }
        };
        
    	task.setOnSucceeded(e ->   updateCalcChannelData());
    	task.setOnFailed(e ->   updateCalcChannelData());
        new Thread(task).start();
	}
	
}


