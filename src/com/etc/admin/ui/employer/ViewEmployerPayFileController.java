package com.etc.admin.ui.employer;

import java.util.ArrayList;
import java.util.List;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.embeds.PipelineInformation;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileNotice;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileRecordRejection;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;
import com.etc.corvetto.ems.pipeline.entities.RawPay;
import com.etc.corvetto.ems.pipeline.entities.pay.PayFile;
import com.etc.corvetto.entities.Document;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

public class ViewEmployerPayFileController {
	
	@FXML
	private Button payfileNextButton;
	@FXML
	private Button payfilePreviousButton;
	@FXML 
	private VBox payfilePipelineSpecificationsVBox; 
	@FXML
	private VBox payfileDynamicFileSpecificationsVBox; 
	@FXML
	private VBox payfileDocumentVBox; 
	@FXML
	private Button payfileShowPipelineSpecsButton;
	@FXML
	private Button payfileShowDocumentButton;
	@FXML
	private TextField payfileNameLabel;
	@FXML
	private TextField payfileEmployeesCreatedLabel;
	@FXML
	private TextField payfileEmployeesUpdatedLabel;
	@FXML
	private TextField payfilePaysCreatedLabel;
	@FXML
	private TextField payfilePaysUpdatedLabel;
	@FXML
	private TextField payfileNoChangeCountLabel;
	@FXML
	private TextField payfilePipelineNameLabel;
	@FXML
	private TextField payfilePipelineDescriptionLabel;
	@FXML
	private Label payfileRawPayEmployeesListLabel;
	@FXML
	private Label payfileRawPaysListLabel;
	@FXML
	private Label payfilePipelineNoticesListLabel;
	@FXML
	private Label payfileRecordRejectionsListLabel;
	@FXML
	private Label payfilePayFilesListLabel;
	@FXML
	private Label payfileDocumentSectionLabel;
	@FXML
	private Label payfileSpecificationSectionLabel;
	@FXML
	private ListView<HBoxCell> payfilesRawPayEmployeeFilesListView;
	@FXML
	private ListView<HBoxCell> payfilesRawPayFilesListView;
	@FXML
	private ListView<HBoxCell> payfilesPipelineNoticesListView;
	@FXML
	private ListView<HBoxCell> payfilesRecordRejectionsListView;
	@FXML
	private ListView<HBoxCell> payfilePayFilesListView;
	@FXML
	private GridPane payfilesRawPayEmployeeFilesGrid;
	@FXML
	private GridPane payfilesRawPayFilesGrid;
	@FXML
	private GridPane payfilesPipelineNoticesGrid;
	@FXML
	private GridPane payfilesRecordRejectionsGrid;
	@FXML
	private GridPane payfilePayFilesListGrid;
	@FXML
	private GridPane payfilesDocumentGrid;
	@FXML
	private GridPane payfilesSpecificationGrid;
	@FXML
	private Label payfileCoreIdLabel;
	@FXML
	private Label payfileCoreActiveLabel;
	@FXML
	private Label payfileCoreBODateLabel;
	@FXML
	private Label payfileCoreLastUpdatedLabel;
	@FXML
	private Label payfileSpecificationCoreIdLabel;
	@FXML
	private Label payfileSpecificationCoreActiveLabel;
	@FXML
	private Label payfileSpecificationCoreBODateLabel;
	@FXML
	private Label payfileSpecificationCoreLastUpdatedLabel;
	@FXML
	private TextField payfileDocumentNameLabel;
	@FXML
	private TextField payfileDocumentSizeLabel;
	@FXML
	private TextField payfileDocumentDescriptionLabel;
	@FXML
	private TextField payfileDocumentFilenameLabel;
	@FXML
	private Label payfileDocumentCoreIdLabel;
	@FXML
	private Label payfileDocumentCoreActiveLabel;
	@FXML
	private Label payfileDocumentCoreBODateLabel;
	@FXML
	private Label payfileDocumentCoreLastUpdatedLabel;
	
	// PIPELINE INFORMATION MEMBERS
	@FXML
	private CheckBox payfileInitializedCheckBox = null;
	@FXML
	private TextField payfileInitializedOnLabel = null;
	@FXML
	private CheckBox payfileParsedCheckBox = null;
	@FXML
	private TextField payfileParsedOnLabel = null;
	@FXML
	private CheckBox payfileValidatedCheckBox = null;
	@FXML
	private TextField payfileLastValidationLabel = null;	
	@FXML
	private CheckBox payfileConvertedCheckBox = null;
	@FXML
	private TextField payfileLastConversionLabel = null;	
	@FXML
	private CheckBox payfileFinalizedCheckBox = null;
	@FXML
	private TextField payfileLastFinalizedLabel = null;
	@FXML
	private CheckBox payfileDiscardedCheckBox = null;
	@FXML
	private CheckBox payfileRejectedCheckBox = null;
	@FXML
	private CheckBox payfileCompletedCheckBox = null;
	@FXML
	private TextField payfileRecordsLabel = null;
	@FXML
	private TextField payfileUnitsLabel = null;
	
	// our local payfile object
	PayFile payFile = null;
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
				
		clearScreen();
		updatePayFileData();
	}
	
	private void clearScreen() {
		//clear all the labels
		payfileNameLabel.setText("Please select a PayFile to continue...");
		payfileEmployeesCreatedLabel.setText(" ");
		payfileEmployeesUpdatedLabel.setText(" ");
		payfileNoChangeCountLabel.setText(" ");
		payfilePipelineNameLabel.setText(" ");
		payfilePipelineDescriptionLabel.setText(" ");
		payfileDocumentNameLabel.setText(" ");
		payfileDocumentSizeLabel.setText(" ");
		payfileDocumentDescriptionLabel.setText(" ");
		payfileDocumentFilenameLabel.setText(" ");
		payfileDocumentCoreIdLabel.setText(" ");
		payfileDocumentCoreActiveLabel.setText(" ");
		payfileDocumentCoreBODateLabel.setText(" ");
		payfileDocumentCoreLastUpdatedLabel.setText(" ");
		
		//no edit or selections unless something is loaded
		payfileShowPipelineSpecsButton.setDisable(true);
		payfileShowDocumentButton.setDisable(true);
	}
	
	private void initControls() {
		// aesthetics
		payfilePayFilesListGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		payfilePayFilesListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		payfilePayFilesListLabel.setPadding(new Insets(0, 10, 0, 10));
		payfilesRecordRejectionsGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		payfileRecordRejectionsListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		payfileRecordRejectionsListLabel.setPadding(new Insets(0, 10, 0, 10));
		payfilesPipelineNoticesGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		payfilePipelineNoticesListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		payfilePipelineNoticesListLabel.setPadding(new Insets(0, 10, 0, 10));
		payfilesRawPayFilesGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		payfileRawPaysListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		payfileRawPaysListLabel.setPadding(new Insets(0, 10, 0, 10));
		payfilesRawPayEmployeeFilesGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		payfileRawPayEmployeesListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		payfileRawPayEmployeesListLabel.setPadding(new Insets(0, 10, 0, 10));
		payfilesDocumentGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		payfileDocumentSectionLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		payfileDocumentSectionLabel.setPadding(new Insets(0, 10, 0, 10));
		payfilesSpecificationGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		payfileSpecificationSectionLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		payfileSpecificationSectionLabel.setPadding(new Insets(0, 10, 0, 10));
	}

	private void updatePayFileDocumentData(){
		if (payFile.getPipelineInformation() == null) return;

		Document document = payFile.getPipelineInformation().getDocument();
		if (document != null) {
			Utils.updateControl(payfileNameLabel, document.getName());
			Utils.updateControl(payfileDocumentNameLabel, document.getName());
			Utils.updateControl(payfileDocumentSizeLabel, document.getSizeInBytes());
			Utils.updateControl(payfileDocumentFilenameLabel, document.getFileName());
			
			Utils.updateControl(payfileDocumentCoreIdLabel,String.valueOf(document.getId()));
			Utils.updateControl(payfileDocumentCoreActiveLabel,String.valueOf(document.isActive()));
			Utils.updateControl(payfileDocumentCoreBODateLabel,document.getBornOn());
			Utils.updateControl(payfileDocumentCoreLastUpdatedLabel,document.getLastUpdated());
			
			payfileShowDocumentButton.setDisable(false);
		}
	}

	private void UpdatePayFilePipelineSpecifications(){

		PipelineSpecification pipelineSpecification = payFile.getSpecification();
		if (pipelineSpecification != null) {
			Utils.updateControl(payfilePipelineNameLabel,pipelineSpecification.getName());
			Utils.updateControl(payfilePipelineDescriptionLabel,pipelineSpecification.getDescription());
			payfileShowPipelineSpecsButton.setDisable(false);
			}
	}
	
	private void updatePayFileData(){
		
		payFile = DataManager.i().mPipelinePayFile;
		if (payFile != null) {
			// PAYFILE DATA
			Utils.updateControl(payfileEmployeesCreatedLabel,payFile.getEmployeesCreated());
			payfileEmployeesUpdatedLabel.setText(String.valueOf(payFile.getEmployeesUpdated()));
			payfileNoChangeCountLabel.setText(String.valueOf(payFile.getNoChangeCount()));
			Utils.updateControl(payfileEmployeesCreatedLabel, payFile.getEmployeesCreated());
			Utils.updateControl(payfileEmployeesUpdatedLabel, payFile.getEmployeesUpdated());
			Utils.updateControl(payfilePaysCreatedLabel, payFile.getPaysCreated());
			Utils.updateControl(payfilePaysUpdatedLabel, payFile.getPaysUpdated());
			
			// PAYFILE INFORMATION DATA
			PipelineInformation info = payFile.getPipelineInformation();
			if (info != null) {
				Utils.updateControl(payfileInitializedCheckBox, info.isInitialized());
				Utils.updateControl(payfileInitializedOnLabel, info.getInitializedOn());
				Utils.updateControl(payfileParsedCheckBox, info.isParsed());
				Utils.updateControl(payfileParsedOnLabel, info.getParsedOn());
				Utils.updateControl(payfileValidatedCheckBox, info.isValidated());
				Utils.updateControl(payfileLastValidationLabel, info.getLastValidation());
				Utils.updateControl(payfileConvertedCheckBox, info.isConverted());
				Utils.updateControl(payfileLastConversionLabel, info.getLastConversion());
				Utils.updateControl(payfileFinalizedCheckBox, info.isFinalized());
				Utils.updateControl(payfileLastFinalizedLabel, info.getLastFinalized());
				Utils.updateControl(payfileDiscardedCheckBox, info.isDiscarded());
				//Utils.updateControl(payfileRejectedCheckBox, info.isRejected());
				Utils.updateControl(payfileCompletedCheckBox, info.isCompleted());
				Utils.updateControl(payfileRecordsLabel, info.getRecords());
				Utils.updateControl(payfileUnitsLabel, info.getUnits());
			}
			//coredata
			Utils.updateControl(payfileCoreIdLabel,String.valueOf(payFile.getId()));
			Utils.updateControl(payfileCoreActiveLabel,String.valueOf(payFile.isActive()));
			Utils.updateControl(payfileCoreBODateLabel,payFile.getBornOn());
			Utils.updateControl(payfileCoreLastUpdatedLabel,payFile.getLastUpdated());
			
			//other sections
			UpdatePayFilePipelineSpecifications();
			updatePayFileDocumentData();
			//load the collections
			loadPayFiles();
			loadPipelineNotices();
			loadRecordRejections();
			//loadRawPayEmployees();
			//loadRawPays();
			
			//set the navigation buttons accordingly
			if (DataManager.i().mEmployerPayFileIndex < 1)
				payfilePreviousButton.setDisable(true);
			
			if (DataManager.i().mEmployerPayFileIndex > DataManager.i().mEmployerPayFiles.size() - 2)
				payfileNextButton.setDisable(true);
		}
	}
	
	private void loadPayFiles() {
		
		if (payFile == null) return;
		
		List<PayFile> payFiles = payFile.getPayFiles();	
		if (payFiles != null) {
				List<HBoxCell> list = new ArrayList<>();
				list.add(new HBoxCell("Name","Description", null, 0));
				
				int i = 0;
				for (PayFile pFile : payFiles) {
					list.add(new HBoxCell(pFile.getSpecification().getName(), pFile.getSpecification().getDescription(), "View",i));
					i++;
				};	
				
		        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		        payfilePayFilesListView.setItems(myObservableList);		
				
		        payfilePayFilesListLabel.setText("Related Payfiles (total: " + String.valueOf(payFiles.size()) + ")" );
			} else {
				payfilePayFilesListLabel.setText("Related Payfiles (total: 0)");
			}
		
		//adjust the size
        int nListViewSize = (payfilePayFilesListView.getItems().size() +1) * 29;
		if (nListViewSize > 300) nListViewSize = 300;
		payfilePayFilesListView.setPrefHeight(nListViewSize);
		payfilePayFilesListView.setMinHeight(nListViewSize);
		payfilePayFilesListView.setMaxHeight(nListViewSize);	
	}
	
	private void loadPipelineNotices() {
		
		if (payFile == null) return;
		
		List<PipelineFileNotice> pipelineFileNotices = payFile.getNotices();
		
		if (pipelineFileNotices != null) {
				List<HBoxCell> list = new ArrayList<>();
				list.add(new HBoxCell("Message","Type", null, 0));
				
				int i = 0;
				for (PipelineFileNotice pipelineFileNotice : pipelineFileNotices) {
					list.add(new HBoxCell(pipelineFileNotice.getMessage(), pipelineFileNotice.getType().toString(),"View",i));
					i++;
				};	
				
		        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		        payfilesPipelineNoticesListView.setItems(myObservableList);		
				
		        payfilePipelineNoticesListLabel.setText("File Notices total: " + String.valueOf(pipelineFileNotices.size()) + ")" );
			} else {
				payfilePipelineNoticesListLabel.setText("File Notices (total: 0)");
			}

		//adjust the size
        int nListViewSize = (payfilesPipelineNoticesListView.getItems().size() +1) * 29;
		if (nListViewSize > 300) nListViewSize = 300;
		payfilesPipelineNoticesListView.setPrefHeight(nListViewSize);
		payfilesPipelineNoticesListView.setMinHeight(nListViewSize);
		payfilesPipelineNoticesListView.setMaxHeight(nListViewSize);	
	}
	
	private void loadRecordRejections() {
		
		if (payFile == null) return;
		
		List<PipelineFileRecordRejection> recordRejections = payFile.getRecordRejections();
		
		if (recordRejections != null) {
				List<HBoxCell> list = new ArrayList<>();
				list.add(new HBoxCell("Message","Date", null, 0));
				
				int i = 0;
				for (PipelineFileRecordRejection recordRejection : recordRejections) {
					list.add(new HBoxCell(recordRejection.getMessage(), recordRejection.getLastUpdated().toString(),"View",i));
					i++;
				};	
				
		        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		        payfilesRecordRejectionsListView.setItems(myObservableList);		
				
		        payfileRecordRejectionsListLabel.setText("Record Rejections (total: " + String.valueOf(recordRejections.size()) + ")" );
			} else {
				payfileRecordRejectionsListLabel.setText("Record Rejections (total: 0)");
			}

		//adjust the size
        int nListViewSize = (payfilesRecordRejectionsListView.getItems().size() +1) * 29;
		if (nListViewSize > 300) nListViewSize = 300;
		payfilesRecordRejectionsListView.setPrefHeight(nListViewSize);
		payfilesRecordRejectionsListView.setMinHeight(nListViewSize);
		payfilesRecordRejectionsListView.setMaxHeight(nListViewSize);	
	}
	
/*	private void loadRawPayEmployees() {
		
		if (payFile == null) return;
		
		List<RawPayEmployee> rawEmployees = payFile.getRawPayEmployees();
		
		if (rawEmployees != null) {
				List<HBoxCell> list = new ArrayList<>();
				list.add(new HBoxCell("Name","Date", null, 0));
				
				int i = 0;
				for (RawPayEmployee rawEmployee : rawEmployees) {
					list.add(new HBoxCell(rawEmployee.getFirstName() + " " + rawEmployee.getLastName(), 
													  rawEmployee.getCity() + ", " + rawEmployee.getState(),"View",i));
					i++;
				};	
				
		        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		        payfilesRawPayEmployeeFilesListView.setItems(myObservableList);		
				
		        payfileRawPayEmployeesListLabel.setText("Record Rejections (total: " + String.valueOf(payFile.getRawPayEmployees().size()) + ")" );
			} else {
				payfileRawPayEmployeesListLabel.setText("Record Rejections (total: 0)");
			}

		//adjust the size
        int nListViewSize = (payfilesRawPayEmployeeFilesListView.getItems().size() +1) * 29;
		if (nListViewSize > 300) nListViewSize = 300;
		payfilesRawPayEmployeeFilesListView.setPrefHeight(nListViewSize);
		payfilesRawPayEmployeeFilesListView.setMinHeight(nListViewSize);
		payfilesRawPayEmployeeFilesListView.setMaxHeight(nListViewSize);	
	}
*/	
/*	private void loadRawPays() {
		
		if (payFile == null) return;
		
		List<RawPay> rawPays = payFile.getRawPays();
		
		if (rawPays != null) {
				List<HBoxCell> list = new ArrayList<>();
				list.add(new HBoxCell("Name","Date", null, 0));
				
				int i = 0;
				for (RawPay rawPay : rawPays) {
					list.add(new HBoxCell(rawPay.getRawPayEmployee().getFirstName() + " " + rawPay.getRawPayEmployee().getLastName(), 
												 rawPay.getRawPayEmployee().getCity() + ", " + rawPay.getRawPayEmployee().getState(),
												 "View",i));
					i++;
				};	
				
		        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		        payfilesRawPayFilesListView.setItems(myObservableList);		
				
		        payfileRawPaysListLabel.setText("Raw Pays (total: " + String.valueOf(rawPays.size()) + ")" );
			} else {
				payfileRawPaysListLabel.setText("Raw Pays (total: 0)");
			}

		//adjust the size
        int nListViewSize = (payfilesRawPayFilesListView.getItems().size() +1) * 29;
		if (nListViewSize > 300) nListViewSize = 300;
		payfilesRawPayFilesListView.setPrefHeight(nListViewSize);
		payfilesRawPayFilesListView.setMinHeight(nListViewSize);
		payfilesRawPayFilesListView.setMaxHeight(nListViewSize);	
	}
*/	
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
            			 // DataManager.i().setEmployeeCoveragePeriod(Integer.parseInt(btnView.getId()));
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
	
	public void setPreviousEmployerPayFile() {
		if(DataManager.i().mEmployerPayFileIndex > 0) DataManager.i().mEmployerPayFileIndex--;
		DataManager.i().mPipelinePayFile = DataManager.i().mEmployerPayFiles.get(DataManager.i().mEmployerPayFileIndex);
		EtcAdmin.i().setScreen(ScreenType.EMPLOYERPAYFILE);
	}

	public void setNextEmployerPayFile() {
		if(DataManager.i().mEmployerPayFileIndex < DataManager.i().mEmployerPayFiles.size() -1) DataManager.i().mEmployerPayFileIndex++;
		DataManager.i().mPipelinePayFile = DataManager.i().mEmployerPayFiles.get(DataManager.i().mEmployerPayFileIndex);
		EtcAdmin.i().setScreen(ScreenType.EMPLOYERPAYFILE);
	}

	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINEPAYFILEEDITFROMEMPLOYER);
	}	
	
	@FXML
	private void onNextPayfile(ActionEvent event) {
		setNextEmployerPayFile();
	}	

	@FXML
	private void onPrevousPayfile(ActionEvent event) {
		setPreviousEmployerPayFile();
	}	

	@FXML
	private void onShowPipelineSpecs(ActionEvent event) {
	}	

	@FXML
	private void onShowDocument(ActionEvent event) {
	}	

}


