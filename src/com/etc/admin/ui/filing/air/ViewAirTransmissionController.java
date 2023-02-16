package com.etc.admin.ui.filing.air;

import java.util.ArrayList;
import java.util.List;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.AirError;
//import com.etc.corvetto.entities.AirSubmissionStatusRequest;
import com.etc.corvetto.entities.AirTransmission;
import com.etc.corvetto.entities.Irs1094Submission;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ViewAirTransmissionController {
	
	@FXML
	private Label airTitleLabel;
	@FXML
	private CheckBox airProductionCheckBox;
	@FXML
	private CheckBox airCompletedCheckBox;
	@FXML
	private TextField airAirMethodTypeLabel;
	@FXML
	private TextField airAirFormTypeLabel;
	@FXML
	private TextField airAirOperationTypeLabel;
	@FXML
	private TextField airAirStatusTypeLabel;
	@FXML
	private TextField airYearLabel;
	@FXML
	private TextField airUUIDLabel;
	@FXML
	private TextField airUniqTransIdLabel;
	@FXML
	private TextField airManifestFilenameLabel;
	@FXML
	private TextField airDataFilenameLabel;
	@FXML
	private TextField airDataFileSizeLabel;
	@FXML
	private TextField airReceiptIdLabel;
	@FXML
	private TextField airIrs1094FormCountLabel;
	@FXML
	private TextField airIrs1095FormCountLabel;
	@FXML
	private TextField airTransTimeStampLabel;
	@FXML
	private TextField airTimestampExpirationLabel;
	@FXML
	private TextField airReceiptIDLabel;
	@FXML
	private TextField airReceiptTimestampLabel;
	@FXML
	private Button airEditButton;
	// Air Status Requests
	@FXML
	private Label airAirStatusRequestsListLabel;
	@FXML
	private GridPane airAirStatusRequestsListGrid;
	@FXML
	private ListView<HBoxCell> airAirStatusRequestsListView;
	// 1094 Submissions
	@FXML
	private Label airIrs1094SubmissionsListLabel;
	@FXML
	private GridPane airIrs1094SubmissionsListGrid;
	@FXML
	private ListView<HBoxCell> airIrs1094SubmissionsListView;
	// core data
	@FXML
	private Label airCoreIdLabel;
	@FXML
	private Label airCoreActiveLabel;
	@FXML
	private Label airCoreBODateLabel;
	@FXML
	private Label airCoreLastUpdatedLabel;
	// Air Error
	@FXML
	private TextField airAirErrorCodeLabel;
	@FXML
	private TextField airAirErrorDescriptionLabel;
	@FXML
	private TextField airAirErrorIrsDescriptionLabel;
	// Air Error core data
	@FXML
	private Label airAirErrorCoreIdLabel;
	@FXML
	private Label airAirErrorCoreActiveLabel;
	@FXML
	private Label airAirErrorCoreBODateLabel;
	@FXML
	private Label airAirErrorCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		updateAirTransmissionData();
		//load our collections
		loadAirStatusRequests();
		loadIrs1094Submissions();
	}
	
	private void initControls() {
		// aesthetics
		airAirStatusRequestsListGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		airIrs1094SubmissionsListGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);

		airAirStatusRequestsListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		airIrs1094SubmissionsListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		
		//add handlers for listbox selection notification
/*		airAirStatusRequestsListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
				 DataManager.i().setAirStatusRequest(airAirStatusRequestsListView.getSelectionModel().getSelectedIndex() - 1);
				 // load the screen
					EtcAdmin.i().setScreen(ScreenType.AIRSTATUSREQUEST, true);
            }
        });	
*/		
		airIrs1094SubmissionsListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
				DataManager.i().setIrs1094Submission(airIrs1094SubmissionsListView.getSelectionModel().getSelectedIndex() - 1);
				// load the screen 
				EtcAdmin.i().setScreen(ScreenType.IRS1094SUBMISSION, true);
            }
        });	
		
		//set functionality according to the user security level
		airEditButton.setDisable(!Utils.userCanEdit());
	}

	private void updateAirTransmissionData(){
		AirTransmission transmission = DataManager.i().mAirTransmission;
		if (transmission != null) {
			Utils.updateControl(airTitleLabel,"Air Transmission");	

			Utils.updateControl(airProductionCheckBox,transmission.isProduction());
			Utils.updateControl(airCompletedCheckBox,transmission.isCompleted());
			Utils.updateControl(airAirMethodTypeLabel,transmission.getMethodType().toString());
			Utils.updateControl(airAirFormTypeLabel,transmission.getFormType().toString());
			Utils.updateControl(airAirOperationTypeLabel,transmission.getOperationType().toString());
			Utils.updateControl(airAirStatusTypeLabel,transmission.getStatus().toString());
			Utils.updateControl(airYearLabel,String.valueOf(transmission.getYear()));
			Utils.updateControl(airUUIDLabel,transmission.getUuid());
			Utils.updateControl(airUniqTransIdLabel,transmission.getUniqTransId());
			Utils.updateControl(airManifestFilenameLabel,transmission.getManifestFilename());
			Utils.updateControl(airDataFilenameLabel,transmission.getDataFileFilename());
			Utils.updateControl(airDataFileSizeLabel,String.valueOf(transmission.getDataFileSizeInBytes()));
			Utils.updateControl(airReceiptIdLabel,transmission.getOriginalReceiptId());
			Utils.updateControl(airIrs1094FormCountLabel,String.valueOf(transmission.getIrs1094FormCount()));
			Utils.updateControl(airIrs1095FormCountLabel,String.valueOf(transmission.getIrs1095FormCount()));
			Utils.updateControl(airTransTimeStampLabel,transmission.getTransTimestamp());
			Utils.updateControl(airTimestampExpirationLabel,transmission.getTransTimestampExpiration());
			Utils.updateControl(airReceiptIDLabel,transmission.getReceiptId());
			Utils.updateControl(airReceiptTimestampLabel,transmission.getReceiptTimestamp());
			// core data
			Utils.updateControl(airCoreIdLabel,String.valueOf(transmission.getId()));
			Utils.updateControl(airCoreActiveLabel,String.valueOf(transmission.isActive()));
			Utils.updateControl(airCoreBODateLabel,transmission.getBornOn());
			Utils.updateControl(airCoreLastUpdatedLabel,transmission.getLastUpdated());
			//air error
			AirError error = transmission.getAirError();
			if (error != null) {
				Utils.updateControl(airAirErrorCodeLabel,error.getCode());
				Utils.updateControl(airAirErrorDescriptionLabel,error.getDescription());
				Utils.updateControl(airAirErrorIrsDescriptionLabel,error.getIrsDescription());
				// air error core data
				Utils.updateControl(airCoreIdLabel,String.valueOf(error.getId()));
				Utils.updateControl(airCoreActiveLabel,String.valueOf(error.isActive()));
				Utils.updateControl(airCoreBODateLabel,error.getBornOn());
				Utils.updateControl(airCoreLastUpdatedLabel,error.getLastUpdated());
			}
		}
	}
	
	//update the AirStatusRequests list
	private void loadAirStatusRequests() {    
/*		AirTransmission transmission = DataManager.i().mAirTransmission;
		if (transmission != null) {
		    List<HBoxCell> requestList = new ArrayList<>();
		    requestList.add(new HBoxCell("UUID", "Date",true));

			for (AirSubmissionStatusRequest request : transmission.getAirStatusRequests()) {
				if (request == null) continue;
				requestList.add(new HBoxCell(request.getUuid(),request.getTransTimestamp().toString(), false));
			};	
			
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(requestList);
			airAirStatusRequestsListView.setItems(myObservableList);		
			
			//update our list screen label
			airAirStatusRequestsListLabel.setText("AirErrors (total: " + String.valueOf(transmission.getAirStatusRequests().size()) + ")" );
		} else {
			airAirStatusRequestsListLabel.setText("AirErrors (total: 0)");			
		}
*/	}
	
	//update the IRs1094Submissions list
	private void loadIrs1094Submissions() {    
		AirTransmission transmission = DataManager.i().mAirTransmission;
		if (transmission != null) {
		    List<HBoxCell> irs1094SubmissionsList = new ArrayList<>();
		    irs1094SubmissionsList.add(new HBoxCell("ID", "Date",true));

			for (Irs1094Submission submission : transmission.getIrs1094Submissions()) {
				if (submission == null) continue;
				irs1094SubmissionsList.add(new HBoxCell(String.valueOf(submission.getId()),submission.getLastUpdated().toString(), false));
			};	
			
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(irs1094SubmissionsList);
			airIrs1094SubmissionsListView.setItems(myObservableList);		
			
			//update our list screen label
			airIrs1094SubmissionsListLabel.setText("Irs1094Submissions (total: " + String.valueOf(transmission.getIrs1094Submissions().size()) + ")" );
		} else {
			airIrs1094SubmissionsListLabel.setText("Irs1094Submissions (total: 0)");			
		}
	}
	
	//extending the listview for our additional controls
	public class HBoxCell extends HBox {
         Label lblEmployee = new Label();
         Label lblYear = new Label();

         HBoxCell(String sEmployee, String sYear, boolean isHeader) {
              super();

              if (sEmployee == null ) sEmployee = "";
              if (sYear == null ) sYear = "";
              
              lblEmployee.setText(sEmployee);
              lblEmployee.setMinWidth(100);
              HBox.setHgrow(lblEmployee, Priority.ALWAYS);

              lblYear.setText(sYear);
              lblYear.setMinWidth(140);
              lblYear.setMaxWidth(140);
              lblYear.setPrefWidth(140);
              HBox.setHgrow(lblYear, Priority.ALWAYS);

              if (isHeader == true) {
            	  lblEmployee.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblYear.setFont(Font.font(null, FontWeight.BOLD, 13));
              }
              
              this.getChildren().addAll(lblEmployee, lblYear);
        }
    }	
	
	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.AIRTRANSMISSIONEDIT, true);
	}	
	
	//this may not be used
	@FXML
	private void onAddAirTransmission(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.AIRTRANSMISSIONADD, true);
	}	
	
}
