package com.etc.admin.ui.pipeline.employeefile;

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

public class ViewPipelineEmployeeFileController {
	
	@FXML
	private Button empfileEditButton;
	@FXML
	private Button empfileAddButton;
	@FXML 
	private VBox empfilePipelineSpecificationsVBox; 
	@FXML
	private VBox empfileDynamicFileSpecificationsVBox; 
	@FXML
	private VBox empfileDocumentVBox; 
	@FXML
	private Button empfileShowPipelineSpecsButton;
	@FXML
	private Button empfileShowDocumentButton;
	@FXML
	private ComboBox<String> empfileSearchComboBox;
	@FXML
	private TextField empfileNameLabel;
	@FXML
	private TextField empfileEmployeesCreatedLabel;
	@FXML
	private TextField empfileEmployeesUpdatedLabel;
	@FXML
	private TextField empfileSecondariesCreatedLabel;
	@FXML
	private TextField empfileSecondariesUpdatedLabel;
	@FXML
	private TextField empfileemployeeCoveragePeriodsCreated;
	@FXML
	private TextField empfileemployeeCoveragePeriodsUpdated;
	@FXML
	private TextField empfilesecondaryCoveragePeriodsCreated;
	@FXML
	private TextField empfilesecondaryCoveragePeriodsUpdated;

	@FXML
	private TextField empfileNoChangeCountLabel;
	@FXML
	private TextField empfilePipelineNameLabel;
	@FXML
	private TextField empfilePipelineDescriptionLabel;
	@FXML
	private Label empfileRawPayEmployeesListLabel;
	@FXML
	private Label empfileRawPaysListLabel;
	@FXML
	private Label empfilePipelineNoticesListLabel;
	@FXML
	private Label empfileRecordRejectionsListLabel;
	@FXML
	private Label empfilePayFilesListLabel;
	@FXML
	private ListView<HBoxCell> empfilesRawPayEmployeeFilesListView;
	@FXML
	private ListView<HBoxCell> empfilesRawPayFilesListView;
	@FXML
	private ListView<HBoxCell> empfilesPipelineNoticesListView;
	@FXML
	private ListView<HBoxCell> empfilesRecordRejectionsListView;
	@FXML
	private ListView<HBoxCell> empfilePayFilesListView;
	@FXML
	private GridPane empfilesRawPayEmployeeFilesGrid;
	@FXML
	private GridPane empfilesRawPayFilesGrid;
	@FXML
	private GridPane empfilesPipelineNoticesGrid;
	@FXML
	private GridPane empfilesRecordRejectionsGrid;
	@FXML
	private GridPane empfilePayFilesListGrid;
	@FXML
	private Label empfileCoreIdLabel;
	@FXML
	private Label empfileCoreActiveLabel;
	@FXML
	private Label empfileCoreBODateLabel;
	@FXML
	private Label empfileCoreLastUpdatedLabel;
	@FXML
	private Label empfileSpecificationCoreIdLabel;
	@FXML
	private Label empfileSpecificationCoreActiveLabel;
	@FXML
	private Label empfileSpecificationCoreBODateLabel;
	@FXML
	private Label empfileSpecificationCoreLastUpdatedLabel;
	@FXML
	private TextField empfileDocumentNameLabel;
	@FXML
	private TextField empfileDocumentSizeLabel;
	@FXML
	private TextField empfileDocumentDescriptionLabel;
	@FXML
	private TextField empfileDocumentFilenameLabel;
	@FXML
	private TextField empfileDocumentTypeLabel;
	@FXML
	private TextField empfileAccountLabel;
	@FXML
	private TextField empfileEmployerLabel;
	@FXML
	private Label empfileDocumentCoreIdLabel;
	@FXML
	private Label empfileDocumentCoreActiveLabel;
	@FXML
	private Label empfileDocumentCoreBODateLabel;
	@FXML
	private Label empfileDocumentCoreLastUpdatedLabel;
	
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
		empfileNameLabel.setText(" ");
		empfileEmployeesCreatedLabel.setText(" ");
		empfileEmployeesUpdatedLabel.setText(" ");
		empfileNoChangeCountLabel.setText(" ");
		empfilePipelineNameLabel.setText(" ");
		empfilePipelineDescriptionLabel.setText(" ");
		empfileDocumentNameLabel.setText(" ");
		empfileDocumentSizeLabel.setText(" ");
		empfileDocumentDescriptionLabel.setText(" ");
		empfileDocumentFilenameLabel.setText(" ");
		empfileDocumentTypeLabel.setText(" ");
		empfileAccountLabel.setText(" ");
		empfileEmployerLabel.setText(" ");
		empfileDocumentCoreIdLabel.setText(" ");
		empfileDocumentCoreActiveLabel.setText(" ");
		empfileDocumentCoreBODateLabel.setText(" ");
		empfileDocumentCoreLastUpdatedLabel.setText(" ");
		
		//no edit unless something is selected
		empfileEditButton.setDisable(true);
	}
	
	private void loadSearchBox(){
		
	}

	private void initControls() {
		// aesthetics
		empfilePayFilesListGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		empfilePayFilesListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		empfilesRecordRejectionsGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		empfileRecordRejectionsListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		empfilesPipelineNoticesGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		empfilePipelineNoticesListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		empfilesRawPayFilesGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		empfileRawPaysListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		empfilesRawPayEmployeeFilesGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		empfileRawPayEmployeesListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));

		//set functionality according to the user security level
		empfileAddButton.setDisable(!Utils.userCanAdd());
	
	}
	

	private void updateEmployeeFileDocumentData(){
		
		Document document = DataManager.i().mPipelineEmployeeFile.getPipelineInformation().getDocument();
		if (document != null) {
			Utils.updateControl(empfileDocumentNameLabel, document.getName());
			Utils.updateControl(empfileDocumentSizeLabel, document.getName());
			Utils.updateControl(empfileDocumentDescriptionLabel, document.getName());
			Utils.updateControl(empfileDocumentFilenameLabel, document.getName());
		
			if (document.getDocumentType() != null)
				Utils.updateControl(empfileDocumentTypeLabel, document.getDocumentType().toString());
			else
				Utils.updateControl(empfileDocumentTypeLabel, " ");
			
			if (document.getAccount() != null)
				Utils.updateControl(empfileAccountLabel, document.getAccount().getName());
			else
				Utils.updateControl(empfileAccountLabel, " ");
				
			if (document.getEmployer() != null)
				Utils.updateControl(empfileEmployerLabel, document.getEmployer().getName());
			else
				Utils.updateControl(empfileAccountLabel, " ");
			
			Utils.updateControl(empfileDocumentCoreIdLabel,String.valueOf(document.getId()));
			Utils.updateControl(empfileDocumentCoreActiveLabel,String.valueOf(document.isActive()));
			Utils.updateControl(empfileDocumentCoreBODateLabel,document.getBornOn());
			Utils.updateControl(empfileDocumentCoreLastUpdatedLabel,document.getLastUpdated());
		}
	}

	private void UpdateEmployeeFilePipelineSpecifications(){

		PipelineSpecification pipelineSpecification = DataManager.i().mPipelineEmployeeFile.getSpecification();
		if (pipelineSpecification != null) {
			Utils.updateControl(empfilePipelineNameLabel,pipelineSpecification.getName());
			Utils.updateControl(empfilePipelineDescriptionLabel,pipelineSpecification.getDescription());
			}
	}
	
	private void updateEmployeeFileData(){
		
		if (DataManager.i().mPipelineEmployeeFile != null) {
			//empfileNameLabel.setText(DataManager.i().mPipelineEmployeeFile.);
			Utils.updateControl(empfileEmployeesCreatedLabel,DataManager.i().mPipelineEmployeeFile.getEmployeesCreated());
			empfileEmployeesUpdatedLabel.setText(String.valueOf(DataManager.i().mPipelineEmployeeFile.getEmployeesUpdated()));
			empfileNoChangeCountLabel.setText(String.valueOf(DataManager.i().mPipelineEmployeeFile.getNoChangeCount()));

			//coredata
			Utils.updateControl(empfileCoreIdLabel,String.valueOf(DataManager.i().mPipelineEmployeeFile.getId()));
			Utils.updateControl(empfileCoreActiveLabel,String.valueOf(DataManager.i().mPipelineEmployeeFile.isActive()));
			Utils.updateControl(empfileCoreBODateLabel,DataManager.i().mPipelineEmployeeFile.getBornOn());
			Utils.updateControl(empfileCoreLastUpdatedLabel,DataManager.i().mPipelineEmployeeFile.getLastUpdated());
			
			//other sections
			UpdateEmployeeFilePipelineSpecifications();
			updateEmployeeFileDocumentData();
			//load the collections
			loadPayFiles();
			loadPipelineNotices();
			loadRecordRejections();
			loadRawPayEmployees();
			loadRawPays();
			
			//enable the buttons
			empfileEditButton.setDisable(!Utils.userCanEdit());
			
		}
	}
	
	private void loadPayFiles() {
		
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
		        empfilePayFilesListView.setItems(myObservableList);		
				
		        empfilePayFilesListLabel.setText("Employee Files (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getEmployeeFiles().size()) + ")" );
			} else {
				empfilePayFilesListLabel.setText("Employee Files (total: 0)");
			}
		
		//adjust the size
        int nListViewSize = (empfilePayFilesListView.getItems().size() +1) * 29;
		if (nListViewSize > 300) nListViewSize = 300;
		empfilePayFilesListView.setPrefHeight(nListViewSize);
		empfilePayFilesListView.setMinHeight(nListViewSize);
		empfilePayFilesListView.setMaxHeight(nListViewSize);	
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
		        empfilesPipelineNoticesListView.setItems(myObservableList);		
				
		        empfilePipelineNoticesListLabel.setText("Employee Files (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getNotices().size()) + ")" );
			} else {
				empfilePipelineNoticesListLabel.setText("Employee Files (total: 0)");
			}

		//adjust the size
        int nListViewSize = (empfilesPipelineNoticesListView.getItems().size() +1) * 29;
		if (nListViewSize > 300) nListViewSize = 300;
		empfilesPipelineNoticesListView.setPrefHeight(nListViewSize);
		empfilesPipelineNoticesListView.setMinHeight(nListViewSize);
		empfilesPipelineNoticesListView.setMaxHeight(nListViewSize);	
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
		        empfilesRecordRejectionsListView.setItems(myObservableList);		
				
		        empfileRecordRejectionsListLabel.setText("Record Rejections (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getRecordRejections().size()) + ")" );
			} else {
				empfileRecordRejectionsListLabel.setText("Record Rejections (total: 0)");
			}

		//adjust the size
        int nListViewSize = (empfilesRecordRejectionsListView.getItems().size() +1) * 29;
		if (nListViewSize > 300) nListViewSize = 300;
		empfilesRecordRejectionsListView.setPrefHeight(nListViewSize);
		empfilesRecordRejectionsListView.setMinHeight(nListViewSize);
		empfilesRecordRejectionsListView.setMaxHeight(nListViewSize);	
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
		        empfilesRawPayEmployeeFilesListView.setItems(myObservableList);		
				
		        empfileRawPayEmployeesListLabel.setText("Record Rejections (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getRawEmployees().size()) + ")" );
			} else {
				empfileRawPayEmployeesListLabel.setText("Record Rejections (total: 0)");
			}

		//adjust the size
        int nListViewSize = (empfilesRawPayEmployeeFilesListView.getItems().size() +1) * 29;
		if (nListViewSize > 300) nListViewSize = 300;
		empfilesRawPayEmployeeFilesListView.setPrefHeight(nListViewSize);
		empfilesRawPayEmployeeFilesListView.setMinHeight(nListViewSize);
		empfilesRawPayEmployeeFilesListView.setMaxHeight(nListViewSize);	
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
		        empfilesRawPayFilesListView.setItems(myObservableList);		
				
		        empfileRawPaysListLabel.setText("Record Rejections (total: " + String.valueOf(DataManager.i().mPipelineEmployeeFile.getRawEmployees().size()) + ")" );
			} else {
				empfileRawPaysListLabel.setText("Record Rejections (total: 0)");
			}

		//adjust the size
        int nListViewSize = (empfilesRawPayFilesListView.getItems().size() +1) * 29;
		if (nListViewSize > 300) nListViewSize = 300;
		empfilesRawPayFilesListView.setPrefHeight(nListViewSize);
		empfilesRawPayFilesListView.setMinHeight(nListViewSize);
		empfilesRawPayFilesListView.setMaxHeight(nListViewSize);	
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
        empfileSearchComboBox.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> {
            final javafx.scene.control.TextField editor = empfileSearchComboBox.getEditor();
            final String selected = empfileSearchComboBox.getSelectionModel().getSelectedItem();
            
            empfileSearchComboBox.show();
            empfileSearchComboBox.setVisibleRowCount(10);

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
        empfileSearchComboBox.setItems(filteredItems);	
		
		//close the dropdown if it is showing
        empfileSearchComboBox.hide();
	}
	
	public void onSearchFiles(ActionEvent event) {
		searchFiles();
	}
	
	public void searchFiles() {
		if (DataManager.i().mPipelineEmployeeFiles == null) return;
		
		String sSelection = empfileSearchComboBox.getValue();
		String sName;
		
		for (int i = 0; i < DataManager.i().mPipelineEmployeeFiles.size();i++) {
			sName = DataManager.i().mPipelineEmployeeFiles.get(i).getSpecification().getName();;
			if (sName == sSelection) {
				// set the current account
				DataManager.i().setPipelineEmployeeFile(DataManager.i().mPipelineEmployeeFiles.get(i));
				
				//update the screen
				updateEmployeeFileData();
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
            			 // DataManager.i().setSecondary(Integer.parseInt(btnView.getId()));
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
		EtcAdmin.i().setScreen(ScreenType.PIPELINEEMPLOYEEFILEEDIT, true);
	}	
	
	@FXML
	private void onAdd(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINEEMPLOYEEFILEADD, true);
	}		
	
	@FXML
	private void onShowPipelineSpecs(ActionEvent event) {
	}	

	@FXML
	private void onShowDocument(ActionEvent event) {
	}	

}


