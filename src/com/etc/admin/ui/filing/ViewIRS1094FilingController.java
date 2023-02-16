package com.etc.admin.ui.filing;

import java.util.ArrayList;
import java.util.List;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.AirFilingEvent;
import com.etc.corvetto.entities.Irs1094Filing;
import com.etc.corvetto.entities.Irs1094Submission;
import com.etc.corvetto.entities.Irs1095Filing;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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

public class ViewIRS1094FilingController {
	
	@FXML
	private Label flngTitleLabel;
	@FXML
	private CheckBox flngReadyToFileCheckBox;
	@FXML
	private CheckBox flngCorrectedCheckBox;
	@FXML
	private TextField flngAirformTypeLabel;
	@FXML
	private TextField flngAirOpoerationTypeLabel;
	@FXML
	private TextField flngAirStatusTypeLabel;
	@FXML
	private TextField flngTotalFormsLabel;
	@FXML
	private TextField flngFiledFormsLabel;
	@FXML
	private TextField flngLastSubmittedLabel;
	@FXML
	private TextField flngTaxYearLabel;
	@FXML
	private Button flngEditButton;
	// 1095 filings
	@FXML
	private Label flng1095FilingsListLabel;
	@FXML
	private GridPane flng1095FilingsListGrid;
	@FXML
	private ListView<HBoxCell> flng1095FilingsListView;
	// Air Filings
	@FXML
	private Label flngAirFilingEventsListLabel;
	@FXML
	private GridPane flngAirFilingEventsListGrid;
	@FXML
	private ListView<HBoxCell> flngAirFilingEventsListView;
	// 1094 Submissions
	@FXML
	private Label flng1094SubmissionsListLabel;
	@FXML
	private GridPane flng1094SubmissionsListGrid;
	@FXML
	private ListView<HBoxCell> flng1094SubmissionsListView;
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
		//loadFilingData();
		//load our collections
		//load1095Filings();
		//loadAirFilingEvents();
		//load1094Submissions();
	}
	
	private void initControls() {
		//add handlers for listbox selection notification
		flng1095FilingsListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
            	DataManager.i().mIrs1095Filing = DataManager.i().mIrs1095Filings.get(flng1095FilingsListView.getSelectionModel().getSelectedIndex() - 1); 
            }
        });	
		
		flngAirFilingEventsListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
	//			 DataManager.i().setAirFilingEvent(flngAirFilingEventsListView.getSelectionModel().getSelectedIndex() - 1);
            }
        });	
		
		flng1094SubmissionsListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
				 DataManager.i().setIrs1094Submission(flng1094SubmissionsListView.getSelectionModel().getSelectedIndex() - 1);
            }
        });	
		
		//set functionality according to the user security level
		flngEditButton.setDisable(!Utils.userCanEdit());
	}

	//update the IRs1094bs list
	private void load1094bs() {    
/*		Irs1094b filing = DataManager.i().mIrs1094bs;
		if (filing != null) {
		    List<HBoxCell> irs1095FilingList = new ArrayList<>();
		    irs1095FilingList.add(new HBoxCell("Employee", "Year",true));

			for (Irs1095Filing filing95 : filing.getIrs1095Filings()) {
				if (filing95 == null) continue;
				
				String name = filing95.getEmployer().getName();
				irs1095FilingList.add(new HBoxCell(name,String.valueOf(filing95.getTaxYear().getYear()), false));
			};	
			
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(irs1095FilingList);
			flng1095FilingsListView.setItems(myObservableList);		
			
			//update our list screen label
			flng1095FilingsListLabel.setText("Irs1095Filings (total: " + String.valueOf(filing.getIrs1095Filings().size()) + ")" );
		} else {
			flng1095FilingsListLabel.setText("Irs1095Filings (total: 0)");			
		} */
	}
	
	private void update1094FilingData(){
		Irs1094Filing filing = DataManager.i().mIrs1094Filing;
		if (filing != null) {
			String sName = "";
			if (filing.getEmployer() != null)
				sName= filing.getEmployer().getName() + " 1094 Filing";
			Utils.updateControl(flngTitleLabel,sName);	
			
			Utils.updateControl(flngReadyToFileCheckBox, filing.isReadyToFile());
			Utils.updateControl(flngCorrectedCheckBox, filing.isCorrected());
			Utils.updateControl(flngAirformTypeLabel, filing.getFormType().toString());
			Utils.updateControl(flngAirOpoerationTypeLabel, filing.getOperationType().toString());
			Utils.updateControl(flngAirStatusTypeLabel, filing.getStatus().toString());
			Utils.updateControl(flngTotalFormsLabel, String.valueOf(filing.getTotalForms()));
			Utils.updateControl(flngFiledFormsLabel, String.valueOf(filing.getFiledForms()));
			Utils.updateControl(flngLastSubmittedLabel, String.valueOf(filing.getLastSubmitted()));
			if (filing.getTaxYear() != null)
				Utils.updateControl(flngTaxYearLabel, String.valueOf(filing.getTaxYear().getYear()));

			// core data
			Utils.updateControl(flngCoreIdLabel,String.valueOf(filing.getId()));
			Utils.updateControl(flngCoreActiveLabel,String.valueOf(filing.isActive()));
			Utils.updateControl(flngCoreBODateLabel,filing.getBornOn());
			Utils.updateControl(flngCoreLastUpdatedLabel,filing.getLastUpdated());
		}
	}
	
	//update the IRs1095Filings list
	private void load1095Filings() {    
		Irs1094Filing filing = DataManager.i().mIrs1094Filing;
		if (filing != null) {
		    List<HBoxCell> irs1095FilingList = new ArrayList<>();
		    irs1095FilingList.add(new HBoxCell("Employee", "Year",true));

			for (Irs1095Filing filing95 : filing.getIrs1095Filings()) {
				if (filing95 == null) continue;
				
				String name = filing95.getEmployer().getName();
				irs1095FilingList.add(new HBoxCell(name,String.valueOf(filing95.getTaxYear().getYear()), false));
			};	
			
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(irs1095FilingList);
			flng1095FilingsListView.setItems(myObservableList);		
			
			//update our list screen label
			flng1095FilingsListLabel.setText("Irs1095Filings (total: " + String.valueOf(filing.getIrs1095Filings().size()) + ")" );
		} else {
			flng1095FilingsListLabel.setText("Irs1095Filings (total: 0)");			
		}
	}
	
	//update the AirFilingEvents list
	private void loadAirFilingEvents() {    
		Irs1094Filing filing = DataManager.i().mIrs1094Filing;
		if (filing != null) {
		    List<HBoxCell> irsAirEventList = new ArrayList<>();
		    irsAirEventList.add(new HBoxCell("Description", "Date",true));

			for (AirFilingEvent event : filing.getAirFilingEvents()) {
				if (event == null) continue;
				irsAirEventList.add(new HBoxCell(event.getDescription(),event.getLastUpdated().toString(), false));
			};	
			
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(irsAirEventList);
			flngAirFilingEventsListView.setItems(myObservableList);		
			
			//update our list screen label
			flngAirFilingEventsListLabel.setText("AirFilingEvents (total: " + String.valueOf(filing.getAirFilingEvents().size()) + ")" );
		} else {
			flngAirFilingEventsListLabel.setText("AirFilingEventss (total: 0)");			
		}
	}
	
	//update the IRs1095Submissions list
	private void load1094Submissions() {    
		Irs1094Filing filing = DataManager.i().mIrs1094Filing;
		if (filing != null) {
		    List<HBoxCell> irs1094SubmissionsList = new ArrayList<>();
		    irs1094SubmissionsList.add(new HBoxCell("Employee", "Year",true));

			for (Irs1094Submission submission : filing.getIrs1094Submissions()) {
				if (submission == null) continue;
				irs1094SubmissionsList.add(new HBoxCell(String.valueOf(submission.getFormCount()),submission.getLastUpdated().toString(), false));
			};	
			
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(irs1094SubmissionsList);
			flng1094SubmissionsListView.setItems(myObservableList);		
			
			//update our list screen label
			flng1094SubmissionsListLabel.setText("Irs1094Submissions (total: " + String.valueOf(filing.getIrs1094Submissions().size()) + ")" );
		} else {
			flng1094SubmissionsListLabel.setText("Irs1094Submissions (total: 0)");			
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
		EtcAdmin.i().setScreen(ScreenType.IRS1094FILINGEDIT, true);
	}	
	
	//this may not be used
	@FXML
	private void onAdd1095b(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.IRS1094FILINGADD, true);
	}	
	
}
