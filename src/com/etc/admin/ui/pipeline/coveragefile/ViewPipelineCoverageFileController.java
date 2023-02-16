package com.etc.admin.ui.pipeline.coveragefile;

import java.util.ArrayList;
import java.util.List;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileNotice;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileRecordRejection;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;
import com.etc.corvetto.ems.pipeline.entities.RawEmployee;
import com.etc.corvetto.ems.pipeline.entities.emp.EmployeeFile;
import com.etc.corvetto.entities.Document;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ViewPipelineCoverageFileController {
	
	@FXML
	private Button cvgfileEditButton;
	@FXML
	private Button cvgfileAddButton;
	@FXML 
	private VBox cvgfilePipelineSpecificationsVBox; 
	@FXML
	private VBox cvgfileDynamicFileSpecificationsVBox; 
	@FXML
	private VBox cvgfileDocumentVBox; 
	@FXML
	private Button cvgfileShowPipelineSpecsButton;
	@FXML
	private Button cvgfileShowDocumentButton;
	@FXML
	private ComboBox<String> cvgfileSearchComboBox;
	@FXML
	private TextField cvgfileNameLabel;
	@FXML
	private TextField cvgfileEmployeesCreatedLabel;
	@FXML
	private TextField cvgfileEmployeesUpdatedLabel;
	@FXML
	private TextField cvgfileSecondariesCreatedLabel;
	@FXML
	private TextField cvgfileSecondariesUpdatedLabel;
	@FXML
	private TextField cvgfileemployeeCoveragePeriodsCreated;
	@FXML
	private TextField cvgfileemployeeCoveragePeriodsUpdated;
	@FXML
	private TextField cvgfilesecondaryCoveragePeriodsCreated;
	@FXML
	private TextField cvgfilesecondaryCoveragePeriodsUpdated;

	@FXML
	private TextField cvgfileNoChangeCountLabel;
	@FXML
	private TextField cvgfilePipelineNameLabel;
	@FXML
	private TextField cvgfilePipelineDescriptionLabel;
	@FXML
	private Label cvgfileRawPaySecondaryListLabel;
	@FXML
	private Label cvgfileRawPaySecondaryCoverageListLabel;
	@FXML
	private Label cvgfileRawPayEmployeesListLabel;
	@FXML
	private Label cvgfileRawPaysListLabel;
	@FXML
	private Label cvgfilePipelineNoticesListLabel;
	@FXML
	private Label cvgfileRecordRejectionsListLabel;
	@FXML
	private Label cvgfileCoverageFilesListLabel;
	@FXML
	private ListView<HBoxCell> cvgfilesRawPaySecondaryListView;
	@FXML
	private ListView<HBoxCell> cvgfilesRawPaySecondaryCoverageView;
	@FXML
	private ListView<HBoxCell> cvgfilesRawPayEmployeeFilesListView;
	@FXML
	private ListView<HBoxCell> cvgfilesRawcvgfilesListView;
	@FXML
	private ListView<HBoxCell> cvgfilesPipelineNoticesListView;
	@FXML
	private ListView<HBoxCell> cvgfilesRecordRejectionsListView;
	@FXML
	private ListView<HBoxCell> cvgfileCoverageFilesListView;
	@FXML
	private GridPane cvgfilesRawPaySecondaryGrid;
	@FXML
	private GridPane cvgfilesRawPaySecondaryCoverageGrid;
	@FXML
	private GridPane cvgfilesRawPayEmployeeFilesGrid;
	@FXML
	private GridPane cvgfilesRawcvgfilesGrid;
	@FXML
	private GridPane cvgfilesPipelineNoticesGrid;
	@FXML
	private GridPane cvgfilesRecordRejectionsGrid;
	@FXML
	private GridPane cvgfileCoverageFilesListGrid;
	@FXML
	private Label cvgfileCoreIdLabel;
	@FXML
	private Label cvgfileCoreActiveLabel;
	@FXML
	private Label cvgfileCoreBODateLabel;
	@FXML
	private Label cvgfileCoreLastUpdatedLabel;
	@FXML
	private Label cvgfileSpecificationCoreIdLabel;
	@FXML
	private Label cvgfileSpecificationCoreActiveLabel;
	@FXML
	private Label cvgfileSpecificationCoreBODateLabel;
	@FXML
	private Label cvgfileSpecificationCoreLastUpdatedLabel;
	@FXML
	private TextField cvgfileDocumentNameLabel;
	@FXML
	private TextField cvgfileDocumentSizeLabel;
	@FXML
	private TextField cvgfileDocumentDescriptionLabel;
	@FXML
	private TextField cvgfileDocumentFilenameLabel;
	@FXML
	private TextField cvgfileDocumentTypeLabel;
	@FXML
	private TextField cvgfileAccountLabel;
	@FXML
	private TextField cvgfileEmployerLabel;
	@FXML
	private Label cvgfileDocumentCoreIdLabel;
	@FXML
	private Label cvgfileDocumentCoreActiveLabel;
	@FXML
	private Label cvgfileDocumentCoreBODateLabel;
	@FXML
	private Label cvgfileDocumentCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
				
		clearScreen();
		loadSearchBox();

	}
	
	private void clearScreen() {
		//clear all the labels
		cvgfileNameLabel.setText(" ");
		cvgfileNameLabel.setPromptText("Please select a CoverageFile to continue...");
		cvgfileEmployeesCreatedLabel.setText(" ");
		cvgfileEmployeesUpdatedLabel.setText(" ");
		cvgfileNoChangeCountLabel.setText(" ");
		cvgfilePipelineNameLabel.setText(" ");
		cvgfilePipelineDescriptionLabel.setText(" ");
		cvgfileDocumentNameLabel.setText(" ");
		cvgfileDocumentSizeLabel.setText(" ");
		cvgfileDocumentDescriptionLabel.setText(" ");
		cvgfileDocumentFilenameLabel.setText(" ");
		cvgfileDocumentTypeLabel.setText(" ");
		cvgfileAccountLabel.setText(" ");
		cvgfileEmployerLabel.setText(" ");
		cvgfileDocumentCoreIdLabel.setText(" ");
		cvgfileDocumentCoreActiveLabel.setText(" ");
		cvgfileDocumentCoreBODateLabel.setText(" ");
		cvgfileDocumentCoreLastUpdatedLabel.setText(" ");
		
		//no edit unless something is selected
		cvgfileEditButton.setDisable(true);
	}
	
	private void loadSearchBox(){
		
	}

	private void initControls() {
		// aesthetics
		cvgfileCoverageFilesListGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		cvgfileCoverageFilesListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		cvgfilesRecordRejectionsGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		cvgfileRecordRejectionsListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		cvgfilesPipelineNoticesGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		cvgfilePipelineNoticesListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		cvgfilesRawcvgfilesGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		cvgfileRawPaysListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		cvgfilesRawPayEmployeeFilesGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		cvgfileRawPayEmployeesListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		cvgfilesRawPaySecondaryGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		cvgfileRawPaySecondaryListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		cvgfilesRawPaySecondaryCoverageGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		cvgfileRawPaySecondaryCoverageListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		
		//set functionality according to the user security level
		cvgfileAddButton.setDisable(!Utils.userCanAdd());
		
	}
	

	private void updateEmployeeFileDocumentData(){
		
		Document document = DataManager.i().mPipelineEmployeeFile.getPipelineInformation().getDocument();
		if (document != null) {
			Utils.updateControl(cvgfileDocumentNameLabel, document.getName());
			Utils.updateControl(cvgfileDocumentSizeLabel, document.getName());
			Utils.updateControl(cvgfileDocumentDescriptionLabel, document.getName());
			Utils.updateControl(cvgfileDocumentFilenameLabel, document.getName());
		
			if (document.getDocumentType() != null)
				Utils.updateControl(cvgfileDocumentTypeLabel, document.getDocumentType().toString());
			else
				Utils.updateControl(cvgfileDocumentTypeLabel, " ");
			
			if (document.getAccount() != null)
				Utils.updateControl(cvgfileAccountLabel, document.getAccount().getName());
			else
				Utils.updateControl(cvgfileAccountLabel, " ");
				
			if (document.getEmployer() != null)
				Utils.updateControl(cvgfileEmployerLabel, document.getEmployer().getName());
			else
				Utils.updateControl(cvgfileAccountLabel, " ");
			
			Utils.updateControl(cvgfileDocumentCoreIdLabel,String.valueOf(document.getId()));
			Utils.updateControl(cvgfileDocumentCoreActiveLabel,String.valueOf(document.isActive()));
			Utils.updateControl(cvgfileDocumentCoreBODateLabel,document.getBornOn());
			Utils.updateControl(cvgfileDocumentCoreLastUpdatedLabel,document.getLastUpdated());
		}
	}

	private void UpdateEmployeeFilePipelineSpecifications(){

		PipelineSpecification pipelineSpecification = DataManager.i().mPipelineEmployeeFile.getSpecification();
		if (pipelineSpecification != null) {
			Utils.updateControl(cvgfilePipelineNameLabel,pipelineSpecification.getName());
			Utils.updateControl(cvgfilePipelineDescriptionLabel,pipelineSpecification.getDescription());
			}
	}
	
	private void updateCoverageFileData(){
		
		if (DataManager.i().mPipelineEmployeeFile != null) {
			//cvgfileNameLabel.setText(DataManager.i().mPipelineEmployeeFile.);
			Utils.updateControl(cvgfileEmployeesCreatedLabel,DataManager.i().mPipelineEmployeeFile.getEmployeesCreated());
			cvgfileEmployeesUpdatedLabel.setText(String.valueOf(DataManager.i().mPipelineEmployeeFile.getEmployeesUpdated()));
			cvgfileNoChangeCountLabel.setText(String.valueOf(DataManager.i().mPipelineEmployeeFile.getNoChangeCount()));

			//coredata
			Utils.updateControl(cvgfileCoreIdLabel,String.valueOf(DataManager.i().mPipelineEmployeeFile.getId()));
			Utils.updateControl(cvgfileCoreActiveLabel,String.valueOf(DataManager.i().mPipelineEmployeeFile.isActive()));
			Utils.updateControl(cvgfileCoreBODateLabel,DataManager.i().mPipelineEmployeeFile.getBornOn());
			Utils.updateControl(cvgfileCoreLastUpdatedLabel,DataManager.i().mPipelineEmployeeFile.getLastUpdated());
			
			//other sections
			UpdateEmployeeFilePipelineSpecifications();
			updateEmployeeFileDocumentData();
			//load the collections
			loadCoveragefiles();
			loadPipelineNotices();
			loadRecordRejections();
			loadRawPayEmployees();
			loadRawPays();
			
			//set functionality according to the user security level
			cvgfileEditButton.setDisable(!Utils.userCanEdit());
		}
	}
	
	private void loadCoveragefiles() {
		
		if (DataManager.i().mPipelineEmployeeFile == null) return;
		
		List<EmployeeFile> employeeFiles = DataManager.i().mPipelineEmployeeFile.getEmployeeFiles();
		
		if (employeeFiles != null) {
				List<HBoxCell> list = new ArrayList<>();
				list.add(new HBoxCell("Name","Description", null, 0));
				
				int i = 0;
				for (EmployeeFile employeeFile : employeeFiles) {
					list.add(new HBoxCell(employeeFile.getSpecification().getName(), employeeFile.getSpecification().getDescription(), "View",i));
					i++;
				};	
				
		        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		        cvgfileCoverageFilesListView.setItems(myObservableList);		
				
		        cvgfileCoverageFilesListLabel.setText("Employee Files (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getEmployeeFiles().size()) + ")" );
			} else {
				cvgfileCoverageFilesListLabel.setText("Employee Files (total: 0)");
			}
		
		//adjust the size
        int nListViewSize = (cvgfileCoverageFilesListView.getItems().size() +1) * 29;
		if (nListViewSize > 300) nListViewSize = 300;
		cvgfileCoverageFilesListView.setPrefHeight(nListViewSize);
		cvgfileCoverageFilesListView.setMinHeight(nListViewSize);
		cvgfileCoverageFilesListView.setMaxHeight(nListViewSize);	
	}
	
	private void loadPipelineNotices() {
		
		if (DataManager.i().mPipelineEmployeeFile == null) return;
		
		List<PipelineFileNotice> pipelineFileNotices = DataManager.i().mPipelineEmployeeFile.getNotices();
		
		if (pipelineFileNotices != null) {
				List<HBoxCell> list = new ArrayList<>();
				list.add(new HBoxCell("Message","Type", null, 0));
				
				int i = 0;
				for (PipelineFileNotice pipelineFileNotice : pipelineFileNotices) {
					list.add(new HBoxCell(pipelineFileNotice.getMessage(), pipelineFileNotice.getType().toString(),"View",i));
					i++;
				};	
				
		        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		        cvgfilesPipelineNoticesListView.setItems(myObservableList);		
				
		        cvgfilePipelineNoticesListLabel.setText("Employee Files (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getNotices().size()) + ")" );
			} else {
				cvgfilePipelineNoticesListLabel.setText("Employee Files (total: 0)");
			}

		//adjust the size
        int nListViewSize = (cvgfilesPipelineNoticesListView.getItems().size() +1) * 29;
		if (nListViewSize > 300) nListViewSize = 300;
		cvgfilesPipelineNoticesListView.setPrefHeight(nListViewSize);
		cvgfilesPipelineNoticesListView.setMinHeight(nListViewSize);
		cvgfilesPipelineNoticesListView.setMaxHeight(nListViewSize);	
	}
	
	private void loadRecordRejections() {
		
		if (DataManager.i().mPipelineEmployeeFile == null) return;
		
		List<PipelineFileRecordRejection> recordRejections = DataManager.i().mPipelineEmployeeFile.getRecordRejections();
		
		if (recordRejections != null) {
				List<HBoxCell> list = new ArrayList<>();
				list.add(new HBoxCell("Message","Date", null, 0));
				
				int i = 0;
				for (PipelineFileRecordRejection recordRejection : recordRejections) {
					list.add(new HBoxCell(recordRejection.getMessage(), recordRejection.getLastUpdated().toString(),"View",i));
					i++;
				};	
				
		        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		        cvgfilesRecordRejectionsListView.setItems(myObservableList);		
				
		        cvgfileRecordRejectionsListLabel.setText("Record Rejections (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getRecordRejections().size()) + ")" );
			} else {
				cvgfileRecordRejectionsListLabel.setText("Record Rejections (total: 0)");
			}

		//adjust the size
        int nListViewSize = (cvgfilesRecordRejectionsListView.getItems().size() +1) * 29;
		if (nListViewSize > 300) nListViewSize = 300;
		cvgfilesRecordRejectionsListView.setPrefHeight(nListViewSize);
		cvgfilesRecordRejectionsListView.setMinHeight(nListViewSize);
		cvgfilesRecordRejectionsListView.setMaxHeight(nListViewSize);	
	}
	
	private void loadRawPayEmployees() {
		
		if (DataManager.i().mPipelineEmployeeFile == null) return;
		
		List<RawEmployee> rawEmployees = DataManager.i().mPipelineEmployeeFile.getRawEmployees();
		
		if (rawEmployees != null) {
				List<HBoxCell> list = new ArrayList<>();
				list.add(new HBoxCell("Name","Date", null, 0));
				
				int i = 0;
				for (RawEmployee rawEmployee : rawEmployees) {
					list.add(new HBoxCell(rawEmployee.getFullName(), rawEmployee.getCity(),"View",i));
					i++;
				};	
				
		        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		        cvgfilesRawPayEmployeeFilesListView.setItems(myObservableList);		
				
		        cvgfileRawPayEmployeesListLabel.setText("Record Rejections (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getRawEmployees().size()) + ")" );
			} else {
				cvgfileRawPayEmployeesListLabel.setText("Record Rejections (total: 0)");
			}

		//adjust the size
        int nListViewSize = (cvgfilesRawPayEmployeeFilesListView.getItems().size() +1) * 29;
		if (nListViewSize > 300) nListViewSize = 300;
		cvgfilesRawPayEmployeeFilesListView.setPrefHeight(nListViewSize);
		cvgfilesRawPayEmployeeFilesListView.setMinHeight(nListViewSize);
		cvgfilesRawPayEmployeeFilesListView.setMaxHeight(nListViewSize);	
	}
	
	private void loadRawPays() {
		
		if (DataManager.i().mPipelineEmployeeFile == null) return;
		
		List<RawEmployee> rawEmployees = DataManager.i().mPipelineEmployeeFile.getRawEmployees();
		
		if (rawEmployees != null) {
				List<HBoxCell> list = new ArrayList<>();
				list.add(new HBoxCell("Name","Date", null, 0));
				
				int i = 0;
				for (RawEmployee rawEmployee : rawEmployees) {
					list.add(new HBoxCell(rawEmployee.getFullName(), rawEmployee.getCity(),"View",i));
					i++;
				};	
				
		        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		        cvgfilesRawcvgfilesListView.setItems(myObservableList);		
				
		        cvgfileRawPaysListLabel.setText("Record Rejections (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getRawEmployees().size()) + ")" );
			} else {
				cvgfileRawPaysListLabel.setText("Record Rejections (total: 0)");
			}

		//adjust the size
        int nListViewSize = (cvgfilesRawcvgfilesListView.getItems().size() +1) * 29;
		if (nListViewSize > 300) nListViewSize = 300;
		cvgfilesRawcvgfilesListView.setPrefHeight(nListViewSize);
		cvgfilesRawcvgfilesListView.setMinHeight(nListViewSize);
		cvgfilesRawcvgfilesListView.setMaxHeight(nListViewSize);	
	}
	
	public void loadSearchFiles() {
		if (DataManager.i().mPipelineEmployeeFiles == null) return;

		String sName;
		ObservableList<String> searchNames = FXCollections.observableArrayList();
		for (int i = 0; i < DataManager.i().mPipelineEmployeeFiles.size();i++) {
			sName = DataManager.i().mPipelineEmployeeFiles.get(i).getSpecification().getName();
			if (sName != null)
				searchNames.add(sName);
		};		
		
		// use a filterlist to wrap the account names for combobox searching later
        FilteredList<String> filteredItems = new FilteredList<String>(searchNames, p -> true);

        // add a listener for the edit control on the combobox
        // the listener will filter the list according to what is in the search box edit control
        cvgfileSearchComboBox.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> {
            final javafx.scene.control.TextField editor = cvgfileSearchComboBox.getEditor();
            final String selected = cvgfileSearchComboBox.getSelectionModel().getSelectedItem();
            
            cvgfileSearchComboBox.show();
            cvgfileSearchComboBox.setVisibleRowCount(10);

            // Run on the GUI thread
            Platform.runLater(() -> {
                if (selected == null || !selected.equals(editor.getText())) {
                    filteredItems.setPredicate(item -> {
                        if (item.toUpperCase().contains(newvalue.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });		
		});
        
		//finally, set our filtered items as the combobox collection
        cvgfileSearchComboBox.setItems(filteredItems);	
		
		//close the dropdown if it is showing
        cvgfileSearchComboBox.hide();
	}
	
	public void onSearchFiles(ActionEvent event) {
		searchFiles();
	}
	
	public void searchFiles() {
		if (DataManager.i().mPipelineEmployeeFiles == null) return;
		
		String sSelection = cvgfileSearchComboBox.getValue();
		String sName;
		
		for (int i = 0; i < DataManager.i().mPipelineEmployeeFiles.size();i++) {
			sName = DataManager.i().mPipelineEmployeeFiles.get(i).getSpecification().getName();;
			if (sName == sSelection) {
				// set the current account
				DataManager.i().setPipelineEmployeeFile(DataManager.i().mPipelineEmployeeFiles.get(i));
				
				//update the screen
				updateCoverageFileData();
				break;
			}
		}
		
	}	
	
	
	//extending the listview for our additional controls
	public static class HBoxCell extends HBox {
         Label lblName = new Label();
         Label lblAddress = new Label();
         Button btnView = new Button();

         HBoxCell(String sName, String sAddress, String sButtonText, int nButtonID) {
              super();

              if (sName == null) sName = "";             
              if (sAddress == null) sAddress = "";
              
              if (sName.contains("null")) sName = "";
              if (sAddress.contains("null")) sAddress = "";
              
              // check to see if we have a bad address from all nulls
              if (sAddress.equals(" ,  "))
            	  sAddress = "";
              
              lblName.setText(sName);
              lblName.setMinWidth(280);
              lblName.setMaxWidth(280);
              lblName.setPrefWidth(280);
              HBox.setHgrow(lblName, Priority.ALWAYS);

              lblAddress.setText(sAddress);
              lblAddress.setMinWidth(390);
              lblAddress.setMaxWidth(390);
              lblAddress.setPrefWidth(390);
              HBox.setHgrow(lblAddress, Priority.ALWAYS);

              if (sButtonText != null) {
            	  btnView.setText(sButtonText);
            	  btnView.setId(String.valueOf(nButtonID));
              }       
              
              if (sButtonText == null) {
            	  lblName.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblAddress.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  this.getChildren().addAll(lblName, lblAddress);
              }else {
            	  this.getChildren().addAll(lblName, lblAddress, btnView);
            	  
            	  //add an event handler for this button
            	  btnView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            		  
            		  @Override
            		  public void handle(MouseEvent event) {
            			  // let the application load the current secondary
            			  //DataManager.i().setSecondary(Integer.parseInt(btnView.getId()));
            		  }
            	  });
              }
              
         }
    }	
	
	//extending the listview for our additional controls
	public static class HBoxEmploymentPeriodCell extends HBox {
         Label lblHireDate = new Label();
         Label lblTerminationDate = new Label();
         Button btnView = new Button();

         HBoxEmploymentPeriodCell(String sHireDate, String sTerminationDate, String sButtonText, int nButtonID) {
              super();

              if (sHireDate == null)  sHireDate = "";
              if (sTerminationDate == null)  sTerminationDate = "";
              
              if (sHireDate.contains("null")) sHireDate = "";
              if (sTerminationDate.contains("null")) sTerminationDate = "";
              
              lblHireDate.setText(sHireDate);
              lblHireDate.setMinWidth(280);
              lblHireDate.setMaxWidth(280);
              lblHireDate.setPrefWidth(280);
              HBox.setHgrow(lblHireDate, Priority.ALWAYS);

              lblTerminationDate.setText(sTerminationDate);
              lblTerminationDate.setMinWidth(390);
              lblTerminationDate.setMaxWidth(390);
              lblTerminationDate.setPrefWidth(390);
              HBox.setHgrow(lblTerminationDate, Priority.ALWAYS);

              if (sButtonText != null) {
            	  btnView.setText(sButtonText);
            	  btnView.setId(String.valueOf(nButtonID));
              }       
              
              if (sButtonText == null) {
            	  lblHireDate.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblTerminationDate.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  this.getChildren().addAll(lblHireDate, lblTerminationDate);
              }else {
            	  this.getChildren().addAll(lblHireDate, lblTerminationDate, btnView);
            	  
            	  //add an event handler for this button
            	  btnView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            		  
            		  @Override
            		  public void handle(MouseEvent event) {
            			  // let the application load the current employment period
            			  DataManager.i().setEmploymentPeriod(Integer.parseInt(btnView.getId()));
            		  }
            	  });
              }
              
         }
    }	

	//extending the listview for our additional controls
	public static class HBoxEmployeeCoveragePeriodCell extends HBox {
         Label lblStartDate = new Label();
         Label lblEndDate = new Label();
         Label lblTitle1 = new Label();
         Label lblTitle2 = new Label();
         CheckBox cbWaived = new CheckBox();
         CheckBox cbIneligible = new CheckBox();
         Button btnView = new Button();

         HBoxEmployeeCoveragePeriodCell(String sStartDate, String sEndDate, String sTitle1, Boolean bWaived, String sTitle2, Boolean bIneligible, String sButtonText, int nButtonID) {
              super();

              if (sStartDate == null)  sStartDate = "";              
              if (sEndDate == null)  sEndDate = "";
              if (sTitle1 == null)  sTitle1 = "";
              if (sTitle2 == null)  sTitle2 = "";
              
              if (sStartDate.contains("null")) sStartDate = "";
              if (sEndDate.contains("null")) sEndDate = "";
              if (sTitle1.contains("null")) sTitle1 = "";
              if (sTitle2.contains("null")) sTitle2 = "";
              
              lblStartDate.setText(sStartDate);
              lblStartDate.setMinWidth(150);
              lblStartDate.setMaxWidth(150);
              lblStartDate.setPrefWidth(150);
              HBox.setHgrow(lblStartDate, Priority.ALWAYS);

              lblEndDate.setText(sEndDate);
              lblEndDate.setMinWidth(150);
              lblEndDate.setMaxWidth(150);
              lblEndDate.setPrefWidth(150);
              HBox.setHgrow(lblEndDate, Priority.ALWAYS);

              //if the button text is null, then we'll call it a header row and add out titles
              if (sButtonText == null) {

            	  lblStartDate.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblEndDate.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblTitle1.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblTitle2.setFont(Font.font(null, FontWeight.BOLD, 13));

            	  lblTitle1.setText(sTitle1);
                  lblTitle1.setMinWidth(185);
                  lblTitle1.setMaxWidth(185);
                  lblTitle1.setPrefWidth(185);

                  lblTitle2.setText(sTitle2);
                  lblTitle2.setMinWidth(185);
                  lblTitle2.setMaxWidth(185);
                  lblTitle2.setPrefWidth(185);
                  
            	  this.getChildren().addAll(lblStartDate, lblEndDate, lblTitle1, lblTitle2);                  
              }
              else {

                  cbWaived.setText("");
                  cbWaived.setMinWidth(185);
                  cbWaived.setMaxWidth(185);
                  cbWaived.setPrefWidth(185);
                  cbWaived.setDisable(true);
                  cbWaived.setSelected(bWaived);

                  cbIneligible.setText("");
                  cbIneligible.setMinWidth(185);
                  cbIneligible.setMaxWidth(185);
                  cbIneligible.setPrefWidth(185);
                  cbIneligible.setDisable(true);
                  cbIneligible.setSelected(bIneligible);

                  btnView.setText(sButtonText);
            	  btnView.setId(String.valueOf(nButtonID));

            	  this.getChildren().addAll(lblStartDate, lblEndDate, cbWaived, cbIneligible, btnView);
            	  
            	  //add an event handler for this button
            	  btnView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            		  
            		  @Override
            		  public void handle(MouseEvent event) {
            			  // let the application load the current employment period
            			  //DataManager.i().setEmployeeCoveragePeriod(Integer.parseInt(btnView.getId()));
            		  }
            	  });
              }
              
         }
    }	
	
	public class ColumnField {
	    private String name;
	    private String column;
	    private String columnIndex;
	    private String parsePattern;

	    public ColumnField(String name, String column, String columnIndex, String parsePattern) {
	        this.name = name;
	        this.column = column;
	        this.columnIndex = columnIndex;
	        this.parsePattern = parsePattern;
	    }

	    public ColumnField(String name, Boolean bColumn, Integer nColumnIndex, String parsePattern) {
	        this.name = name;
	        this.column = String.valueOf(bColumn);
	        this.columnIndex = String.valueOf(nColumnIndex);
	        this.parsePattern = parsePattern;
	    }

	    public String getName() {
	        return name;
	    }

	    public String getColumn() {
	        return column;
	    }

	    public String getColumnIndex() {
	        return columnIndex;
	    }

	    public String getParsePattern() {
	        return parsePattern;
	    }
	}

	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINECOVERAGEFILEEDIT, true);
	}	
	
	@FXML
	private void onAdd(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINECOVERAGEFILEADD, true);
	}		
	
	@FXML
	private void onShowPipelineSpecs(ActionEvent event) {
	}	

	@FXML
	private void onShowDocument(ActionEvent event) {
	}	

}


