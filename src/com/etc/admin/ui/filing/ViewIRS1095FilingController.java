package com.etc.admin.ui.filing;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.Irs1095Filing;
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

public class ViewIRS1095FilingController {
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
	private TextField flngLastSubmittedLabel;
	@FXML
	private TextField flngTaxYearLabel;
	@FXML
	private Button flngEditButton;
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
		update1095FilingData();
		//load our collection
		load1095Submissions();
	}
	
	private void initControls() {
		flng1095SubmissionsListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
				 DataManager.i().setIrs1094Submission(flng1095SubmissionsListView.getSelectionModel().getSelectedIndex() - 1);
            }
        });	
		
		//set functionality according to the user security level
		flngEditButton.setDisable(!Utils.userCanEdit());
	}

	private void update1095FilingData(){
		Irs1095Filing filing = DataManager.i().mIrs1095Filing;
		if (filing != null) {
			String sName = "";
			if (filing.getEmployer() != null)
				sName= filing.getEmployer().getName() + " 1095 Filing";
			Utils.updateControl(flngTitleLabel,sName);	
			
			Utils.updateControl(flngReadyToFileCheckBox, filing.isReadyToFile());
			Utils.updateControl(flngCorrectedCheckBox, filing.isCorrected());
			Utils.updateControl(flngAirformTypeLabel, filing.getFormType().toString());
			Utils.updateControl(flngAirOpoerationTypeLabel, filing.getOperationType().toString());
			Utils.updateControl(flngAirStatusTypeLabel, filing.getStatus().toString());
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
	
	//update the IRs1095 Submissions list
	private void load1095Submissions() {    
		try{
			Irs1095Filing filing = DataManager.i().mIrs1095Filing;
			if (filing != null) {
			    if (filing.getIrs1095Submissions() == null )
			    	return;
			    List<HBoxCell> irs1095SubmissionsList = new ArrayList<>();
			    irs1095SubmissionsList.add(new HBoxCell("Employee", "Year",true));
	
				for (Irs1095Submission submission : filing.getIrs1095Submissions()) {
					if (submission == null) continue;
					irs1095SubmissionsList.add(new HBoxCell(String.valueOf(submission.getIrs1094Submission().getFormCount()),submission.getLastUpdated().toString(), false));
				};	
				
				ObservableList<HBoxCell> myObservableList = FXCollections.observableList(irs1095SubmissionsList);
				flng1095SubmissionsListView.setItems(myObservableList);		
				
				//update our list screen label
				flng1095SubmissionsListLabel.setText("Irs1095Submissions (total: " + String.valueOf(filing.getIrs1095Submissions().size()) + ")" );
			} else {
				flng1095SubmissionsListLabel.setText("Irs1095Submissions (total: 0)");			
			}
		} catch(Exception e){
			DataManager.i().log(Level.SEVERE, e);
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
