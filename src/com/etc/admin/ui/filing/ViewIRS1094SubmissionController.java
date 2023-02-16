package com.etc.admin.ui.filing;

import java.util.ArrayList;
import java.util.List;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.AirError;
import com.etc.corvetto.entities.Irs1094Submission;
import com.etc.corvetto.entities.Irs1095Submission;

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

public class ViewIRS1094SubmissionController {
	
	@FXML
	private Label flngTitleLabel;
	@FXML
	private CheckBox flngAuthoritativeCheckBox;
	@FXML
	private CheckBox flngAggGroupMemberCheckBox;
	@FXML
	private TextField flngFormCountLabel;
	@FXML
	private TextField flngSubmissionIdLabel;
	@FXML
	private TextField flngOriginalSubmissionIdLabel;
	@FXML
	private Button flngEditButton;
	// Air Errors
	@FXML
	private Label flngAirErrorsListLabel;
	@FXML
	private GridPane flngAirErrorsListGrid;
	@FXML
	private ListView<HBoxCell> flngAirErrorsListView;
	// 1095 Submissions
	@FXML
	private Label flng1095SubmissionsListLabel;
	@FXML
	private GridPane flng1095SubmissionsListGrid;
	@FXML
	private ListView<HBoxCell> flng1095SubmissionsListView;
	// core data
	@FXML
	private Label flngCoreIdLabel;
	@FXML
	private Label flngCoreActiveLabel;
	@FXML
	private Label flngCoreBODateLabel;
	@FXML
	private Label flngCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		update1094SubmissionData();
		//load our collections
		loadAirErrors();
		load1095Submissions();
	}
	
	private void initControls() {
		// aesthetics
		flngAirErrorsListGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		flng1095SubmissionsListGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);

		flngAirErrorsListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		flng1095SubmissionsListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		
		//add handlers for listbox selection notification
		flngAirErrorsListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
            	DataManager.i().mAirError = DataManager.i().mAirErrors.get(flngAirErrorsListView.getSelectionModel().getSelectedIndex() - 1);
				// load the screen
            }
        });	
		
		flng1095SubmissionsListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
            	DataManager.i().mIrs1095Submission = DataManager.i().mIrs1095Submissions.get(flng1095SubmissionsListView.getSelectionModel().getSelectedIndex() - 1);  
				// load the screen 
            }
        });	
		
		//set functionality according to the user security level
		flngEditButton.setDisable(!Utils.userCanEdit());
	}

	private void update1094SubmissionData(){
		Irs1094Submission submission = DataManager.i().mIrs1094Submission;
		if (submission != null) {
			String sName = submission.getSubmissionId() + " 1094 Submission";
			Utils.updateControl(flngTitleLabel,sName);	
			
			Utils.updateControl(flngAuthoritativeCheckBox, submission.isAuthoritative());
			Utils.updateControl(flngAggGroupMemberCheckBox, submission.isAggGroupMember());
			Utils.updateControl(flngFormCountLabel, String.valueOf(submission.getFormCount()));
			Utils.updateControl(flngSubmissionIdLabel, String.valueOf(submission.getSubmissionId()));
			Utils.updateControl(flngOriginalSubmissionIdLabel, String.valueOf(submission.getOriginalSubmissionId()));

			// core data
			Utils.updateControl(flngCoreIdLabel,String.valueOf(submission.getId()));
			Utils.updateControl(flngCoreActiveLabel,String.valueOf(submission.isActive()));
			Utils.updateControl(flngCoreBODateLabel,submission.getBornOn());
			Utils.updateControl(flngCoreLastUpdatedLabel,submission.getLastUpdated());
		}
	}
	
	//update the AirErrors list
	private void loadAirErrors() {    
		Irs1094Submission submission = DataManager.i().mIrs1094Submission;
		if (submission != null) {
		    List<HBoxCell> airErrorList = new ArrayList<>();
		    airErrorList.add(new HBoxCell("Description", "Date",true));

			for (AirError error : submission.getAirErrors()) {
				if (error == null) continue;
				airErrorList.add(new HBoxCell(error.getDescription(),error.getLastUpdated().toString(), false));
			};	
			
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(airErrorList);
			flngAirErrorsListView.setItems(myObservableList);		
			
			//update our list screen label
			flngAirErrorsListLabel.setText("AirErrors (total: " + String.valueOf(submission.getAirErrors().size()) + ")" );
		} else {
			flngAirErrorsListLabel.setText("AirErrors (total: 0)");			
		}
	}
	
	//update the IRs1095Submissions list
	private void load1095Submissions() {    
		Irs1094Submission submission = DataManager.i().mIrs1094Submission;
		if (submission != null) {
		    List<HBoxCell> irs1095SubmissionsList = new ArrayList<>();
		    irs1095SubmissionsList.add(new HBoxCell("ID", "Date",true));

			for (Irs1095Submission submission95 : submission.getIrs1095Submissions()) {
				if (submission95 == null) continue;
				irs1095SubmissionsList.add(new HBoxCell(String.valueOf(submission95.getId()),submission95.getLastUpdated().toString(), false));
			};	
			
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(irs1095SubmissionsList);
			flng1095SubmissionsListView.setItems(myObservableList);		
			
			//update our list screen label
			flng1095SubmissionsListLabel.setText("Irs1095Submissions (total: " + String.valueOf(submission.getIrs1095Submissions().size()) + ")" );
		} else {
			flng1095SubmissionsListLabel.setText("Irs1095Submissions (total: 0)");			
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
		EtcAdmin.i().setScreen(ScreenType.IRS1094SUBMISSIONEDIT, true);
	}	
	
	//this may not be used
	@FXML
	private void onAdd1095b(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.IRS1094SUBMISSIONADD, true);
	}	
	
}
