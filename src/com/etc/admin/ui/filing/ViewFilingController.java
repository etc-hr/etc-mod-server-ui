package com.etc.admin.ui.filing;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.AirFilingEvent;
import com.etc.corvetto.entities.Irs1094Filing;
import com.etc.corvetto.entities.Irs1094b;
import com.etc.corvetto.entities.Irs1094c;
import com.etc.corvetto.entities.Irs1095Filing;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class ViewFilingController {
	
	@FXML
	private AnchorPane flngAnchorPane;
	
	// IRS1094B
	@FXML
	private GridPane flngIrs1094bGrid;
	@FXML
	private TextField flngIrs1094cScenarioIdField;
	@FXML
	private CheckBox flngIrs1094cFileCICheck;
	@FXML
	private CheckBox flngIrs1094cNewFormsCheck;
	@FXML
	private CheckBox flngIrs1094cAuthoritativeCheck;
	@FXML
	private CheckBox flngIrs1094cMecJanCheck;
	@FXML
	private CheckBox flngIrs1094cMecFebCheck;
	@FXML
	private CheckBox flngIrs1094cMecMarCheck;
	@FXML
	private CheckBox flngIrs1094cMecAprCheck;
	@FXML
	private CheckBox flngIrs1094cMecMayCheck;
	@FXML
	private CheckBox flngIrs1094cMecJunCheck;
	@FXML
	private CheckBox flngIrs1094cMecJulCheck;
	@FXML
	private CheckBox flngIrs1094cMecAugCheck;
	@FXML
	private CheckBox flngIrs1094cMecSepCheck;
	@FXML
	private CheckBox flngIrs1094cMecOctCheck;
	@FXML
	private CheckBox flngIrs1094cMecNovCheck;
	@FXML
	private CheckBox flngIrs1094cMecDecCheck;
	@FXML
	private CheckBox flngIrs1094cAgGrpJanCheck;
	@FXML
	private CheckBox flngIrs1094cAgGrpFebCheck;
	@FXML
	private CheckBox flngIrs1094cAgGrpMarCheck;
	@FXML
	private CheckBox flngIrs1094cAgGrpAprCheck;
	@FXML
	private CheckBox flngIrs1094cAgGrpMayCheck;
	@FXML
	private CheckBox flngIrs1094cAgGrpJunCheck;
	@FXML
	private CheckBox flngIrs1094cAgGrpJulCheck;
	@FXML
	private CheckBox flngIrs1094cAgGrpAugCheck;
	@FXML
	private CheckBox flngIrs1094cAgGrpSepCheck;
	@FXML
	private CheckBox flngIrs1094cAgGrpOctCheck;
	@FXML
	private CheckBox flngIrs1094cAgGrpNovCheck;
	@FXML
	private CheckBox flngIrs1094cAgGrpDecCheck;
	@FXML
	private CheckBox flngIrs1094cLckJanCheck;
	@FXML
	private CheckBox flngIrs1094cLckFebCheck;
	@FXML
	private CheckBox flngIrs1094cLckMarCheck;
	@FXML
	private CheckBox flngIrs1094cLckAprCheck;
	@FXML
	private CheckBox flngIrs1094cLckMayCheck;
	@FXML
	private CheckBox flngIrs1094cLckJunCheck;
	@FXML
	private CheckBox flngIrs1094cLckJulCheck;
	@FXML
	private CheckBox flngIrs1094cLckAugCheck;
	@FXML
	private CheckBox flngIrs1094cLckSepCheck;
	@FXML
	private CheckBox flngIrs1094cLckOctCheck;
	@FXML
	private CheckBox flngIrs1094cLckNovCheck;
	@FXML
	private CheckBox flngIrs1094cLckDecCheck;
	@FXML
	private TextField flngIrs1094FtCntJanField;
	@FXML
	private TextField flngIrs1094FtCntFebField;
	@FXML
	private TextField flngIrs1094FtCntMarField;
	@FXML
	private TextField flngIrs1094FtCntAprField;
	@FXML
	private TextField flngIrs1094FtCntMayField;
	@FXML
	private TextField flngIrs1094FtCntJunField;
	@FXML
	private TextField flngIrs1094FtCntJulField;
	@FXML
	private TextField flngIrs1094FtCntAugField;
	@FXML
	private TextField flngIrs1094FtCntSepField;
	@FXML
	private TextField flngIrs1094FtCntOctField;
	@FXML
	private TextField flngIrs1094FtCntNovField;
	@FXML
	private TextField flngIrs1094FtCntDecField;
	@FXML
	private TextField flngIrs1094TtlEmpCntJanField;
	@FXML
	private TextField flngIrs1094TtlEmpCntFebField;
	@FXML
	private TextField flngIrs1094TtlEmpCntMarField;
	@FXML
	private TextField flngIrs1094TtlEmpCntAprField;
	@FXML
	private TextField flngIrs1094TtlEmpCntMayField;
	@FXML
	private TextField flngIrs1094TtlEmpCntJunField;
	@FXML
	private TextField flngIrs1094TtlEmpCntJulField;
	@FXML
	private TextField flngIrs1094TtlEmpCntAugField;
	@FXML
	private TextField flngIrs1094TtlEmpCntSepField;
	@FXML
	private TextField flngIrs1094TtlEmpCntOctField;
	@FXML
	private TextField flngIrs1094TtlEmpCntNovField;
	@FXML
	private TextField flngIrs1094TtlEmpCntDecField;
	@FXML
	private ListView<HBox1094bCell> flng1094bListView;
	
	// IRS1094C
	@FXML
	private GridPane flngIrs1094cGrid;
	@FXML
	private TextField flngIrs1094bScenarioIdField;
	@FXML
	private CheckBox flngIrs1094bFileCICheck;
	@FXML
	private CheckBox flngIrs1094bNewFormsCheck;
	@FXML
	private ListView<HBox1094cCell> flng1094cListView;
	
	// TAXYEAR
	
	// IRS1094FILING
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
	private TextField flngInitialAcceptanceLabel;
	@FXML
	private TextField flngInitialReceiptIdLabel;
	@FXML
	private Button flngEditButton;
	@FXML
	private ListView<HBox1094FilingCell> flng1094FilingsListView;
	// 1095 filings
	@FXML
	private Label flng1095FilingsListLabel;
	@FXML
	private GridPane flng1095FilingsListGrid;
	@FXML
	private ListView<HBox1095FilingCell> flng1095FilingsListView;
	// Air Filings
	@FXML
	private Label flngAirFilingEventsListLabel;
	@FXML
	private GridPane flngAirFilingEventsListGrid;
	@FXML
	private ListView<HBoxAirFilingEventCell> flngAirFilingEventsListView;
	// 1094 Submissions
	@FXML
	private Label flng1094SubmissionsListLabel;
	@FXML
	private GridPane flng1094SubmissionsListGrid;
	@FXML
	private ListView<HBox1094FilingCell> flng1094SubmissionsListView;
	// core data
	@FXML
	private Label flngCoreIdLabel;
	@FXML
	private Label flngCoreActiveLabel;
	@FXML
	private Label flngCoreBODateLabel;
	@FXML
	private Label flngCoreLastUpdatedLabel;
	
	private Irs1094b current1094b = null;
	private Irs1094c current1094c = null;
	private Irs1094Filing current1094Filing = null;

	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		loadFilingData();
		//load1094bs();
		//load1094cs();
		//update1094FilingData();
		//load our collections
		//load1095Filings();
		//loadAirFilingEvents();
		//load1094Submissions();
	}
	
	private void initControls() {
		// clean up until a selection
		show1095b(false);
		show1095c(false);
		
		//add handlers for listbox selection notification
		flng1094bListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) {
            	if (flng1094bListView.getSelectionModel().getSelectedItem() != null) {
	            	// show the correct detail grid
	        		show1095b(true);
	        		show1095c(false);
	        		// and update
	        		current1094b = 	flng1094bListView.getSelectionModel().getSelectedItem().get1094b();
	        		update1094bData();
            	}
            }
        });	
		
		flng1094cListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) {
            	// show the correct detail grid
        		show1095b(false);
        		show1095c(true);
        		// and update
        		current1094c = 	flng1094cListView.getSelectionModel().getSelectedItem().get1094c();
        		update1094cData();
            }
        });	
		
		flng1094FilingsListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) {
            	// show the correct detail grid
        		//show1095b(false);
        		//show1095c(false);
        		// and update
        		current1094Filing = flng1094FilingsListView.getSelectionModel().getSelectedItem().get1094Filing();
        		update1094FilingData();
            }
        });	
		
		/*	flng1095FilingsListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
            	DataManager.i().mIrs1095Filing = DataManager.i().mIrs1095Filings.get(flng1095FilingsListView.getSelectionModel().getSelectedIndex() - 1); 
            }
        });	
		
		flngAirFilingEventsListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
				 DataManager.i().setAirFilingEvent(flngAirFilingEventsListView.getSelectionModel().getSelectedIndex() - 1);
            }
        });	
		
		flng1094SubmissionsListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
				 DataManager.i().setIrs1094Submission(flng1094SubmissionsListView.getSelectionModel().getSelectedIndex() - 1);
            }
        });	
		
		//set functionality according to the user security level
		flngEditButton.setDisable(!Utils.userCanEdit()); */
	}
	
	private void show1095b(boolean show) {
		if (show == true) {
			flngIrs1094bGrid.setVisible(true);
			flngIrs1094bGrid.setMinHeight(145);
			flngIrs1094bGrid.setPrefHeight(145);
			flngIrs1094bGrid.setMaxHeight(145);
		} else {
			flngIrs1094bGrid.setVisible(false);
			flngIrs1094bGrid.setMinHeight(0);
			flngIrs1094bGrid.setPrefHeight(0);
			flngIrs1094bGrid.setMaxHeight(0);
		}
	}
	
	private void show1095c(boolean show) {
		if (show == true) {
			flngIrs1094cGrid.setVisible(true);
			flngIrs1094cGrid.setMinHeight(650);
			flngIrs1094cGrid.setPrefHeight(650);
			flngIrs1094cGrid.setMaxHeight(650);
		} else {
			flngIrs1094cGrid.setVisible(false);
			flngIrs1094cGrid.setMinHeight(0);
			flngIrs1094cGrid.setPrefHeight(0);
			flngIrs1094cGrid.setMaxHeight(0);
		}
	}
	
	private void loadFilingData() 
	{
		if (DataManager.i().mEmployer == null) return;
		
		// new thread
		Task<Void> task = new Task<Void>() 
		{
            @Override
            protected Void call() throws Exception {
             //   AdminPersistenceManager.getInstance().getCurrentTaxYearFilingData();
                return null;
            }
        };
        
      	task.setOnScheduled(e ->  {
      		EtcAdmin.i().setStatusMessage("Loading Filing Data...");
      		EtcAdmin.i().setProgress(0.25);});
      			
    	task.setOnSucceeded(e ->  load1094bs());
    	task.setOnFailed(e ->  load1094bs());
        new Thread(task).start();
	}
	
	private void load1094bs() {   
		if (DataManager.i().mIrs1094bs != null && DataManager.i().mIrs1094bs.size() > 0)
		{
			flng1094bListView.getItems().clear();
			flng1094bListView.getItems().add(new HBox1094bCell(null));
			for (Irs1094b irs1094b : DataManager.i().mIrs1094bs) {
				if (irs1094b == null) continue;
				flng1094bListView.getItems().add(new HBox1094bCell(irs1094b));
			};	
		}

		// continue to the Irs1094cs
		load1094cs();
	}
	
	private void load1094cs() {   
		if (DataManager.i().mIrs1094cs != null && DataManager.i().mIrs1094cs.size() > 0)
		{
			flng1094cListView.getItems().clear();
			flng1094cListView.getItems().add(new HBox1094cCell(null));
			for (Irs1094c irs1094c : DataManager.i().mIrs1094cs) {
				if (irs1094c == null) continue;
				flng1094cListView.getItems().add(new HBox1094cCell(irs1094c));
			};	
		}

		load1094Filings();
	}

	private void load1094Filings() {   
		if (DataManager.i().mIrs1094Filings != null && DataManager.i().mIrs1094Filings.size() > 0)
		{
			flng1094FilingsListView.getItems().clear();
			flng1094FilingsListView.getItems().add(new HBox1094FilingCell(null));
			for (Irs1094Filing irs1094Filing : DataManager.i().mIrs1094Filings) {
				if (irs1094Filing == null) continue;
				flng1094FilingsListView.getItems().add(new HBox1094FilingCell(irs1094Filing));
			};	
		}

		// update our status
  		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0.0);
	}
	
	private void update1094bData(){
		if (current1094b != null) {
			//Utils.updateControl(flngIrs1094bScenarioIdField,current1094b.getScenarioId());	
			Utils.updateControl(flngIrs1094bFileCICheck,current1094b.isFileCIWithSSN());	
			Utils.updateControl(flngIrs1094bNewFormsCheck,current1094b.isWithNewForms());	
		}
	}
	
	private void update1094cData(){
		//Utils.updateControl(flngIrs1094cScenarioIdField,current1094c.getScenarioId());	
		Utils.updateControl(flngIrs1094cFileCICheck,current1094c.isFileCIWithSSN());	
		Utils.updateControl(flngIrs1094cNewFormsCheck,current1094c.isWithNewForms());	
		Utils.updateControl(flngIrs1094cMecJanCheck,current1094c.isJanMECOffer());	
		Utils.updateControl(flngIrs1094cMecFebCheck,current1094c.isFebMECOffer());	
		Utils.updateControl(flngIrs1094cMecMarCheck,current1094c.isMarMECOffer());	
		Utils.updateControl(flngIrs1094cMecAprCheck,current1094c.isAprMECOffer());	
		Utils.updateControl(flngIrs1094cMecMayCheck,current1094c.isMayMECOffer());	
		Utils.updateControl(flngIrs1094cMecJunCheck,current1094c.isJunMECOffer());	
		Utils.updateControl(flngIrs1094cMecJulCheck,current1094c.isJulMECOffer());	
		Utils.updateControl(flngIrs1094cMecAugCheck,current1094c.isAugMECOffer());	
		Utils.updateControl(flngIrs1094cMecSepCheck,current1094c.isSepMECOffer());	
		Utils.updateControl(flngIrs1094cMecOctCheck,current1094c.isOctMECOffer());	
		Utils.updateControl(flngIrs1094cMecNovCheck,current1094c.isNovMECOffer());	
		Utils.updateControl(flngIrs1094cMecDecCheck,current1094c.isDecMECOffer());	
		Utils.updateControl(flngIrs1094cAgGrpJanCheck,current1094c.isJanAggGrp());	
		Utils.updateControl(flngIrs1094cAgGrpFebCheck,current1094c.isFebAggGrp());	
		Utils.updateControl(flngIrs1094cAgGrpMarCheck,current1094c.isMarAggGrp());	
		Utils.updateControl(flngIrs1094cAgGrpAprCheck,current1094c.isAprAggGrp());	
		Utils.updateControl(flngIrs1094cAgGrpMayCheck,current1094c.isMayAggGrp());	
		Utils.updateControl(flngIrs1094cAgGrpJunCheck,current1094c.isJunAggGrp());	
		Utils.updateControl(flngIrs1094cAgGrpJulCheck,current1094c.isJulAggGrp());	
		Utils.updateControl(flngIrs1094cAgGrpAugCheck,current1094c.isAugAggGrp());	
		Utils.updateControl(flngIrs1094cAgGrpSepCheck,current1094c.isSepAggGrp());	
		Utils.updateControl(flngIrs1094cAgGrpOctCheck,current1094c.isOctAggGrp());	
		Utils.updateControl(flngIrs1094cAgGrpNovCheck,current1094c.isNovAggGrp());	
		Utils.updateControl(flngIrs1094cAgGrpDecCheck,current1094c.isDecAggGrp());	
		Utils.updateControl(flngIrs1094cLckJanCheck,current1094c.isJanLocked());	
		Utils.updateControl(flngIrs1094cLckFebCheck,current1094c.isFebLocked());	
		Utils.updateControl(flngIrs1094cLckMarCheck,current1094c.isMarLocked());	
		Utils.updateControl(flngIrs1094cLckAprCheck,current1094c.isAprLocked());	
		Utils.updateControl(flngIrs1094cLckMayCheck,current1094c.isMayLocked());	
		Utils.updateControl(flngIrs1094cLckJunCheck,current1094c.isJunLocked());	
		Utils.updateControl(flngIrs1094cLckJulCheck,current1094c.isJulLocked());	
		Utils.updateControl(flngIrs1094cLckAugCheck,current1094c.isAugLocked());	
		Utils.updateControl(flngIrs1094cLckSepCheck,current1094c.isSepLocked());	
		Utils.updateControl(flngIrs1094cLckOctCheck,current1094c.isOctLocked());	
		Utils.updateControl(flngIrs1094cLckNovCheck,current1094c.isNovLocked());	
		Utils.updateControl(flngIrs1094cLckDecCheck,current1094c.isDecLocked());	
		Utils.updateControl(flngIrs1094FtCntJanField,current1094c.getJanFTEmpCount());	
		Utils.updateControl(flngIrs1094FtCntFebField,current1094c.getFebFTEmpCount());	
		Utils.updateControl(flngIrs1094FtCntMarField,current1094c.getMarFTEmpCount());	
		Utils.updateControl(flngIrs1094FtCntAprField,current1094c.getAprFTEmpCount());	
		Utils.updateControl(flngIrs1094FtCntMayField,current1094c.getMayFTEmpCount());	
		Utils.updateControl(flngIrs1094FtCntJunField,current1094c.getJunFTEmpCount());	
		Utils.updateControl(flngIrs1094FtCntJulField,current1094c.getJulFTEmpCount());	
		Utils.updateControl(flngIrs1094FtCntAugField,current1094c.getAugFTEmpCount());	
		Utils.updateControl(flngIrs1094FtCntSepField,current1094c.getSepFTEmpCount());	
		Utils.updateControl(flngIrs1094FtCntOctField,current1094c.getOctFTEmpCount());	
		Utils.updateControl(flngIrs1094FtCntNovField,current1094c.getNovFTEmpCount());	
		Utils.updateControl(flngIrs1094FtCntDecField,current1094c.getDecFTEmpCount());	
		Utils.updateControl(flngIrs1094TtlEmpCntJanField,current1094c.getJanTotEmpCount());	
		Utils.updateControl(flngIrs1094TtlEmpCntFebField,current1094c.getFebTotEmpCount());	
		Utils.updateControl(flngIrs1094TtlEmpCntMarField,current1094c.getMarTotEmpCount());	
		Utils.updateControl(flngIrs1094TtlEmpCntAprField,current1094c.getAprTotEmpCount());	
		Utils.updateControl(flngIrs1094TtlEmpCntMayField,current1094c.getMayTotEmpCount());	
		Utils.updateControl(flngIrs1094TtlEmpCntJunField,current1094c.getJunTotEmpCount());	
		Utils.updateControl(flngIrs1094TtlEmpCntJulField,current1094c.getJulTotEmpCount());	
		Utils.updateControl(flngIrs1094TtlEmpCntAugField,current1094c.getAugTotEmpCount());	
		Utils.updateControl(flngIrs1094TtlEmpCntSepField,current1094c.getSepTotEmpCount());	
		Utils.updateControl(flngIrs1094TtlEmpCntOctField,current1094c.getOctTotEmpCount());	
		Utils.updateControl(flngIrs1094TtlEmpCntNovField,current1094c.getNovTotEmpCount());	
		Utils.updateControl(flngIrs1094TtlEmpCntDecField,current1094c.getDecTotEmpCount());	
	}
	
	
	private void update1094FilingData(){
		if ( current1094Filing != null) {
			Utils.updateControl(flngReadyToFileCheckBox, current1094Filing.isReadyToFile());
			Utils.updateControl(flngCorrectedCheckBox, current1094Filing.isCorrected());
			Utils.updateControl(flngAirformTypeLabel, current1094Filing.getFormType().toString());
			Utils.updateControl(flngAirOpoerationTypeLabel, current1094Filing.getOperationType().toString());
			Utils.updateControl(flngAirStatusTypeLabel, current1094Filing.getStatus().toString());
			Utils.updateControl(flngTotalFormsLabel, current1094Filing.getTotalForms());
			Utils.updateControl(flngFiledFormsLabel, current1094Filing.getFiledForms());
			Utils.updateControl(flngLastSubmittedLabel, current1094Filing.getLastSubmitted());
			Utils.updateControl(flngInitialAcceptanceLabel, current1094Filing.getInitialAcceptance());
			Utils.updateControl(flngInitialReceiptIdLabel, (current1094Filing.getInitialReceiptId()));
			if (current1094Filing.getTaxYear() != null)
				Utils.updateControl(flngTaxYearLabel, current1094Filing.getTaxYear().getYear());
		}
	}
	
	//update the IRs1095Filings list
	private void load1095Filings() {    
	/*	Irs1094Filing filing = DataManager.i().mIrs1094Filing;
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
	
	//update the AirFilingEvents list
	private void loadAirFilingEvents() {    
	/*	Irs1094Filing filing = DataManager.i().mIrs1094Filing;
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
		} */
	}
	
	//update the IRs1095Submissions list
	private void load1094Submissions() {    
	/*	Irs1094Filing filing = DataManager.i().mIrs1094Filing;
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
		} */
	}
	
	@FXML
	private void onClose(ActionEvent event) {
		Stage stage = (Stage) flng1094bListView.getScene().getWindow();
		stage.close();
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
	
	public class HBox1094bCell extends HBox {
         Label lblDate = new Label();
         Label lblFilewithSSN = new Label();
         Label lblWithNewForms = new Label();
         CheckBox cbFileWithSSN = new CheckBox();
         CheckBox cbWithNewForms = new CheckBox();
         Irs1094b irs1094b;
         
         public Irs1094b get1094b() {
        	 return irs1094b;
         }

         HBox1094bCell(Irs1094b irs1094b) {
              super();
              this.irs1094b = irs1094b;
              
              if (irs1094b != null) {
            	  Utils.setHBoxLabel(lblDate, 100, false, irs1094b.getLastUpdated());
            	  Utils.setHBoxCheckBox(cbFileWithSSN, 100, irs1094b.isFileCIWithSSN());
            	  Utils.setHBoxCheckBox(cbWithNewForms, 100, irs1094b.isWithNewForms());
                  this.getChildren().addAll(lblDate, cbFileWithSSN, cbWithNewForms);
              } else {
        		  Utils.setHBoxLabel(lblDate, 100, true, "Date");
        		  Utils.setHBoxLabel(lblFilewithSSN, 100, true, "File w/SSN");
        		  Utils.setHBoxLabel(lblWithNewForms, 100, true, "w/New Forms");
                  this.getChildren().addAll(lblDate, lblFilewithSSN, lblWithNewForms);
              }
        }
    }	
	
	public class HBox1094cCell extends HBox {
        Label lblDate = new Label();
        Label lblFilewithSSN = new Label();
        Label lblWithNewForms = new Label();
        CheckBox cbFileWithSSN = new CheckBox();
        CheckBox cbWithNewForms = new CheckBox();
        Irs1094c irs1094c;
        
        public Irs1094c get1094c() {
       	 return irs1094c;
        }

        HBox1094cCell(Irs1094c irs1094c) {
             super();
             this.irs1094c = irs1094c;
             
             if (irs1094c != null) {
           	  Utils.setHBoxLabel(lblDate, 100, false, irs1094c.getLastUpdated());
           	  Utils.setHBoxCheckBox(cbFileWithSSN, 100, irs1094c.isFileCIWithSSN());
           	  Utils.setHBoxCheckBox(cbWithNewForms, 100, irs1094c.isWithNewForms());
                 this.getChildren().addAll(lblDate, cbFileWithSSN, cbWithNewForms);
             } else {
       		  Utils.setHBoxLabel(lblDate, 100, true, "Date");
       		  Utils.setHBoxLabel(lblFilewithSSN, 100, true, "File w/SSN");
       		  Utils.setHBoxLabel(lblWithNewForms, 100, true, "w/New Forms");
                 this.getChildren().addAll(lblDate, lblFilewithSSN, lblWithNewForms);
             }
       }
   }	

	public class HBox1094FilingCell extends HBox {
        Label lblDate = new Label();
        Label lblLastSubmission = new Label();
        Label lblAirOperationType = new Label();
        Label lblStatusType = new Label();
        Irs1094Filing irs1094Filing;
        
        public Irs1094Filing get1094Filing() {
       	 return irs1094Filing;
        }

        HBox1094FilingCell(Irs1094Filing irs1094Filing) {
             super();
             this.irs1094Filing = irs1094Filing;
             
             if (irs1094Filing != null) {
            	Utils.setHBoxLabel(lblDate, 150, false, irs1094Filing.getLastUpdated());
           	  	Utils.setHBoxLabel(lblLastSubmission, 150, false, irs1094Filing.getLastSubmitted());
           	  	if (irs1094Filing.getStatus() != null)
           	  		Utils.setHBoxLabel(lblStatusType, 150, false, irs1094Filing.getStatus().toString());
           	  	if (irs1094Filing.getOperationType() != null)
           	  		Utils.setHBoxLabel(lblAirOperationType, 150, false, irs1094Filing.getOperationType().toString());
             } else {
       		  	Utils.setHBoxLabel(lblDate, 150, true, "Date");
       		  	Utils.setHBoxLabel(lblLastSubmission,  150, false, "Last Submission");
       		  	Utils.setHBoxLabel(lblAirOperationType,  150, false, "Air Operation Type");
       		  	Utils.setHBoxLabel(lblStatusType,  150, false, "Status Type");
             }
             
             this.getChildren().addAll(lblDate, lblLastSubmission, lblAirOperationType, lblStatusType);
       }
   }	
	
	public class HBox1095FilingCell extends HBox {
        Label lblDate = new Label();
        Label lblTaxYear = new Label();
        Irs1095Filing irs1095Filing;
        
        public Irs1095Filing get1095Filing() {
       	 return irs1095Filing;
        }

        HBox1095FilingCell(Irs1095Filing irs1095Filing) {
             super();
             this.irs1095Filing = irs1095Filing;
             
             if (irs1095Filing != null) {
           	  Utils.setHBoxLabel(lblDate, 100, false, irs1095Filing.getLastUpdated());
           	  if (irs1095Filing.getTaxYear() != null)
           		  Utils.setHBoxLabel(lblTaxYear,  100, false, irs1095Filing.getTaxYear().getYear());
           	  else
           		  Utils.setHBoxLabel(lblTaxYear,  100, false, "");
             } else {
       		  Utils.setHBoxLabel(lblDate, 100, true, "Date");
       		  Utils.setHBoxLabel(lblTaxYear,  100, false, "Tax Year");
             }
             
             this.getChildren().addAll(lblDate, lblTaxYear);
       }
   }	
	
	public class HBoxAirFilingEventCell extends HBox {
        Label lblDate = new Label();
        Label lblTaxYear = new Label();
        AirFilingEvent airFilingEvent;
        
        public AirFilingEvent getAirFilingEvent() {
       	 return airFilingEvent;
        }

        HBoxAirFilingEventCell(AirFilingEvent airFilingEvent) {
             super();
             this.airFilingEvent = airFilingEvent;
             
             if (airFilingEvent != null) {
           	  Utils.setHBoxLabel(lblDate, 100, false, airFilingEvent.getLastUpdated());
           	  Utils.setHBoxLabel(lblTaxYear,  100, false, airFilingEvent.getDescription());
             } else {
       		  Utils.setHBoxLabel(lblDate, 100, true, "Date");
       		  Utils.setHBoxLabel(lblTaxYear,  100, false, "Tax Year");
             }
             
             this.getChildren().addAll(lblDate, lblTaxYear);
       }
   }	
}
