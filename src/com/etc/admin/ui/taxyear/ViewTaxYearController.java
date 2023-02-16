package com.etc.admin.ui.taxyear; 
 
 
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.lang3.BooleanUtils;

import com.etc.CoreException;
import com.etc.admin.AdminManager; 
import com.etc.admin.EtcAdmin; 
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.calc.CalcQueueData;
import com.etc.admin.ui.filing.ViewFilingController.HBox1094bCell; 
import com.etc.admin.ui.filing.ViewFilingController.HBox1094cCell; 
import com.etc.admin.utils.Utils; 
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.data.Irs1095cLocks;
import com.etc.corvetto.ems.calc.entities.CalculationNotice;
import com.etc.corvetto.ems.exp.embeds.ExportInformation;
import com.etc.corvetto.ems.exp.entities.ExportNotice;
import com.etc.corvetto.ems.exp.entities.ExportRequest;
import com.etc.corvetto.ems.exp.entities.aca.IrsAirCorrectionsExport;
import com.etc.corvetto.ems.exp.rqs.ExportRequestRequest;
import com.etc.corvetto.entities.Employee;
import com.etc.corvetto.entities.Irs1094Filing; 
import com.etc.corvetto.entities.Irs1094Submission; 
import com.etc.corvetto.entities.Irs1094b; 
import com.etc.corvetto.entities.Irs1094c; 
import com.etc.corvetto.entities.Irs1095Filing; 
import com.etc.corvetto.entities.Irs1095b; 
import com.etc.corvetto.entities.Irs1095c;
import com.etc.corvetto.rqs.EmployeeRequest;
import com.etc.corvetto.rqs.Irs1094bRequest; 
import com.etc.corvetto.rqs.Irs1094cRequest;
import com.etc.corvetto.rqs.Irs1095FilingRequest;
import com.etc.corvetto.rqs.Irs1095cLocksRequest;
import com.etc.corvetto.rqs.Irs1095cRequest;
import com.etc.corvetto.rqs.IrsAirCorrectionsExportRequest;
import com.etc.corvetto.rqs.TaxYearRequest; 
import com.etc.corvetto.utils.types.AcaFormType; 
import com.etc.corvetto.utils.types.AcaPrintType;
import com.etc.corvetto.utils.types.AcaProcessType;
import com.etc.corvetto.utils.types.SafeHarborCode;
import com.etc.entities.CoreData;

import javafx.collections.FXCollections; 
import javafx.collections.ObservableList; 
import javafx.concurrent.Task; 
import javafx.event.ActionEvent; 
import javafx.fxml.FXML; 
import javafx.fxml.FXMLLoader; 
import javafx.scene.Parent; 
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox; 
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker; 
import javafx.scene.control.Label; 
import javafx.scene.control.ListView; 
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton; 
import javafx.scene.layout.GridPane; 
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality; 
import javafx.stage.Stage; 
import javafx.stage.StageStyle; 
 
public class ViewTaxYearController
{ 
	// TAX YEAR 
	@FXML 
	private GridPane txyrInputLabelGrid; 
	@FXML 
	private GridPane txyrInputLabelGrid1; 
	@FXML 
	private GridPane txyrInputLabelGrid11; 
	@FXML 
	private Label txyrTitleLabel; 
	@FXML 
	private CheckBox txyrClosedCheckBox; 
	@FXML 
	private DatePicker txyrClosedOnLabel; 
	@FXML 
	private TextField txyrClosedByLabel; 
	@FXML 
	private CheckBox txyrApprovedCheckBox; 
	@FXML 
	private DatePicker txyrApprovedOnLabel; 
	@FXML 
	private TextField txyrApprovedByLabel; 
	@FXML 
	private TextField txyrLiasonLabel; 
	@FXML 
	private TextField txyrJrLiasonLabel; 
	@FXML 
	private ChoiceBox<AcaPrintType> txyrPrintOptionLabel; 
	@FXML 
	private ChoiceBox<AcaFormType> txyrAirformTypeLabel; 
	@FXML 
	private ChoiceBox<SafeHarborCode> txyrSafeHarborCodeLabel; 
	@FXML 
	private Button txyrEditButton; 
	@FXML 
	private Button txyrSaveButton; 
	@FXML 
	private TextField txyrCoreIdLabel; 
	 
	// Tax Year Service Level 
	@FXML 
	private TextField txyrSvcLvlNameLabel; 
	@FXML 
	private TextField txyrSvcLvlDescriptionLabel; 
	@FXML 
	private CheckBox txyrSvcLvlGenerateCodesCheckBox; 
	@FXML 
	private CheckBox txyrSvcLvlTrackEligibilityCheckBox; 
	 
	// IRS1094C 
	@FXML
	private ComboBox<AcaProcessType> flngIrs1094cProscessStatusCombo;
	@FXML 
	private GridPane flngIrs1094bGrid; 
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
	@FXML 
	private Button Irs1094cEditButton; 
	@FXML 
	private Button Irs1094cSaveButton; 
	@FXML 
	private CheckBox Irs1094cLockYearCheck; 
	@FXML 
	private CheckBox Irs1094cMecYearCheck; 
	@FXML 
	private CheckBox Irs1094cAggGroupYearCheck; 
	@FXML 
	private TextField flngIrs1094cCalcPercentField; 
	@FXML 
	private TextField flngIrs1094cValidFormPercentField; 
	@FXML
	private TextField flngIrs1094cIncorporatedOn;
	@FXML
	private TextField flngIrs1094cDissolvedOn;
	
	// IRS1094B 
	@FXML 
	private GridPane flngIrs1094cGrid; 
	@FXML 
	private CheckBox flngIrs1094bFileCICheck; 
	@FXML 
	private CheckBox flngIrs1094bNewFormsCheck; 
	@FXML 
	private ListView<HBox1094cCell> flng1094cListView; 
	@FXML 
	private Button Irs1094bEditButton; 
	@FXML 
	private Button Irs1094bSaveButton; 
	
	// IRS1094FILING 
	@FXML 
	private Label flngTitleLabel; 
	@FXML 
	private CheckBox flngReadyToFileCheckBox; 
	@FXML 
	private CheckBox flngCorrectedCheckBox; 
	@FXML 
	private TextField flngTypeField; 
	@FXML 
	private TextField flngStateField; 
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
	@FXML 
	private Label flngPrime1094FilingsListLabel; 
	 
	// 1095 FILINGS 
	@FXML 
	private Label flng1095FilingsListLabel; 
	@FXML 
	private GridPane flng1095FilingsListGrid; 
	@FXML 
	private ListView<HBox1095FilingCell> flng1095FilingsListView; 
	//@FXML 
	//private TextField flng1095FilterText; 
	
	// 1095c Tools
	@FXML 
	private CheckBox Irs1095cLockYearCheck; 
	@FXML 
	private GridPane flngIrs1095cGrid; 
	@FXML 
	private Label flngIrs1095cNotLockedLabel; 
	@FXML 
	private Label flngIrs1095cLockedLabel; 
	@FXML 
	private CheckBox flngIrs1095cLckJanCheck; 
	@FXML 
	private CheckBox flngIrs1095cLckFebCheck; 
	@FXML 
	private CheckBox flngIrs1095cLckMarCheck; 
	@FXML 
	private CheckBox flngIrs1095cLckAprCheck; 
	@FXML 
	private CheckBox flngIrs1095cLckMayCheck; 
	@FXML 
	private CheckBox flngIrs1095cLckJunCheck; 
	@FXML 
	private CheckBox flngIrs1095cLckJulCheck; 
	@FXML 
	private CheckBox flngIrs1095cLckAugCheck; 
	@FXML 
	private CheckBox flngIrs1095cLckSepCheck; 
	@FXML 
	private CheckBox flngIrs1095cLckOctCheck; 
	@FXML 
	private CheckBox flngIrs1095cLckNovCheck; 
	@FXML 
	private CheckBox flngIrs1095cLckDecCheck; 
	@FXML 
	private TextField flngIrs1095cLckCountJanField; 
	@FXML 
	private TextField flngIrs1095cLckCountFebField; 
	@FXML 
	private TextField flngIrs1095cLckCountMarField; 
	@FXML 
	private TextField flngIrs1095cLckCountAprField; 
	@FXML 
	private TextField flngIrs1095cLckCountMayField; 
	@FXML 
	private TextField flngIrs1095cLckCountJunField; 
	@FXML 
	private TextField flngIrs1095cLckCountJulField; 
	@FXML 
	private TextField flngIrs1095cLckCountAugField; 
	@FXML 
	private TextField flngIrs1095cLckCountSepField; 
	@FXML 
	private TextField flngIrs1095cLckCountOctField; 
	@FXML 
	private TextField flngIrs1095cLckCountNovField; 
	@FXML 
	private TextField flngIrs1095cLckCountDecField; 
	@FXML 
	private TextField flngIrs1095cTotalCountJanField; 
	@FXML 
	private TextField flngIrs1095cTotalCountFebField; 
	@FXML 
	private TextField flngIrs1095cTotalCountMarField; 
	@FXML 
	private TextField flngIrs1095cTotalCountAprField; 
	@FXML 
	private TextField flngIrs1095cTotalCountMayField; 
	@FXML 
	private TextField flngIrs1095cTotalCountJunField; 
	@FXML 
	private TextField flngIrs1095cTotalCountJulField; 
	@FXML 
	private TextField flngIrs1095cTotalCountAugField; 
	@FXML 
	private TextField flngIrs1095cTotalCountSepField; 
	@FXML 
	private TextField flngIrs1095cTotalCountOctField; 
	@FXML 
	private TextField flngIrs1095cTotalCountNovField; 
	@FXML 
	private TextField flngIrs1095cTotalCountDecField; 
	@FXML 
	private TextField flngIrs1095cTotalCountField; 
	@FXML 
	private Button flngIrs1095cEditButton; 
	@FXML 
	private Button flngIrs1095cSaveButton; 
 
	// NOTICES 
	@FXML
	private Label flngExportNoticesLabel;
	@FXML 
	private ListView<HBoxCalculationNoticeCell> flngCalcNoticesListView; 
	@FXML 
	private ListView<HBoxExportNoticeCell> flngExportNoticesListView; 
	 
	// 1094 SUBMISSIONS 
	@FXML 
	private Label flng1094SubmissionsListLabel; 
	@FXML 
	private GridPane flng1094SubmissionsListGrid; 
	@FXML 
	private ListView<HBox1094SubmissionCell> flng1094SubmissionsListView; 
	
	// Calc
	@FXML 
	private  Button CalcIrs10945XBCButton; 
	@FXML 
	private  Button CalcIrs10945FilingButton; 
	 
	// private data members 
	private Irs1094b current1094b = null; 
	private Irs1094c current1094c = null; 
	private Irs1094Filing current1094Filing = null; 
	private boolean isTypeB = false;
	private boolean is1095b = false; 
	private TaxYearFilingData filingData = new TaxYearFilingData(); 
	private Irs1095cLocks locks = null;
	private int lockStates1095c[] = new int[12];

	/** 
	 * initialize is called when the FXML is loaded 
	 */ 
	@FXML 
	public void initialize()  
	{ 
		try {
			initControls(); 
			showTaxYearData(); 
			showTaxYearServiceLevelData(); 
			loadFilingData(); 
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		} 

	} 
	 
	private void initControls()  
	{ 
		try {
			clearScreen(); 
			flngPrime1094FilingsListLabel.setText("Prime Irs1094 Filing"); 
			set1094bEditable(false); 
			set1094cEditable(false); 
			set1095cEditable(false);
			
			// default the 1095c lockstates
			Arrays.fill(lockStates1095c, -1);
			
			//tab handlers for keyboard flow in Total Emp Count
			flngIrs1094TtlEmpCntDecField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
	            if (event.getCode() == KeyCode.TAB) {
	            	///loop back to the top
	            	flngIrs1094TtlEmpCntJanField.requestFocus();
	                event.consume();
	            }
	        });
			
			// set calc button images
			InputStream input1 = getClass().getClassLoader().getResourceAsStream("img/ERCalc1.png");
	   	 	Image image1 = new Image(input1, 75f, 75f, true, true);
	   	 	ImageView imageView1 = new ImageView(image1);
	   	 	CalcIrs10945XBCButton.setGraphic(imageView1);
	
			InputStream input2 = getClass().getClassLoader().getResourceAsStream("img/ERCalc2.png");
	   	 	Image image2 = new Image(input2, 75, 75, true, true);
	   	 	ImageView imageView2 = new ImageView(image2);
	   	 	CalcIrs10945FilingButton.setGraphic(imageView2);
			 
			//set functionality according to the user security level 
			txyrEditButton.setDisable(!Utils.userCanEdit()); 
			txyrSaveButton.setVisible(false); 
			txyrPrintOptionLabel.setDisable(true); 
			txyrAirformTypeLabel.setDisable(true); 
			txyrSafeHarborCodeLabel.setDisable(true); 
			 
			//disable editing for now 
			txyrEditButton.setVisible(false); 
			 
			//AcaProcessType
			ObservableList<AcaProcessType> processTypes = FXCollections.observableArrayList(AcaProcessType.values()); 
			flngIrs1094cProscessStatusCombo.setItems(processTypes); 
			
			//print option 
			ObservableList<AcaPrintType> printOptions = FXCollections.observableArrayList(AcaPrintType.values()); 
			txyrPrintOptionLabel.setItems(printOptions); 
			 
			//air form type 
			ObservableList<AcaFormType> airFormTypes = FXCollections.observableArrayList(AcaFormType.values()); 
			txyrAirformTypeLabel.setItems(airFormTypes); 
	 
			//safe harbor code 
			ObservableList<SafeHarborCode> safeHarborCode = FXCollections.observableArrayList(SafeHarborCode.values()); 
			txyrSafeHarborCodeLabel.setItems(safeHarborCode); 
			 
			// double click handlers 
			flng1095FilingsListView.setOnMouseClicked(mouseClickedEvent -> 
			{ 
	            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) 
	            { 
	            	DataManager.i().mIrs1095Filing = flng1095FilingsListView.getSelectionModel().getSelectedItem().get1095Filing(); 
	
					try { 
			            // load the fxml 
				        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/taxyear/ViewIrs1095Filing.fxml")); 
						Parent ControllerNode = loader.load(); 
	 
				        Stage stage = new Stage(); 
				        stage.initModality(Modality.APPLICATION_MODAL); 
				        stage.initStyle(StageStyle.UNDECORATED); 
				        stage.setScene(new Scene(ControllerNode));   
				        EtcAdmin.i().positionStageCenter(stage);
				        stage.showAndWait(); 
					} catch (IOException e) { DataManager.i().log(Level.SEVERE, e); }        		 
	        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	           } 
	        }); 
			 
			flngCalcNoticesListView.setOnMouseClicked(mouseClickedEvent ->
			{ 
	            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) 
	            { 
	            	DataManager.i().mCalculationNotice = flngCalcNoticesListView.getSelectionModel().getSelectedItem().getAirFilingEvent(); 
				
	            	try { 
			            // load the fxml 
				        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/taxyear/ViewAirFilingEvent.fxml")); 
						Parent ControllerNode = loader.load(); 
	 
				        Stage stage = new Stage(); 
				        stage.initModality(Modality.APPLICATION_MODAL); 
				        stage.initStyle(StageStyle.UNDECORATED); 
				        stage.setScene(new Scene(ControllerNode));   
				        EtcAdmin.i().positionStageCenter(stage);
				        stage.showAndWait(); 
					} catch (IOException e) { DataManager.i().log(Level.SEVERE, e); }        		 
	        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	           } 
	        }); 
			 
			flng1094SubmissionsListView.setOnMouseClicked(mouseClickedEvent -> 
			{ 
				if(flng1094SubmissionsListView.getSelectionModel().getSelectedItem() == null || mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) == false) return; 
	        	DataManager.i().mIrs1094Submission = flng1094SubmissionsListView.getSelectionModel().getSelectedItem().get1094Submission(); 
	        
	        	if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) 
	        	{ 
					try { 
			            // load the fxml 
				        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/taxyear/ViewIRS1094Submission.fxml")); 
						Parent ControllerNode = loader.load(); 
	 
				        Stage stage = new Stage(); 
				        stage.initModality(Modality.APPLICATION_MODAL); 
				        stage.initStyle(StageStyle.UNDECORATED); 
				        stage.setScene(new Scene(ControllerNode));   
				        EtcAdmin.i().positionStageCenter(stage);
				        stage.showAndWait(); 
					} catch (IOException e) { DataManager.i().log(Level.SEVERE, e); }        		 
	        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	           } else { 
	            	// update any notices for this submission 
	            	filingData.updateExportNotices(DataManager.i().mIrs1094Submission); 
	            	showExportNotices(); 
	            } 
	        }); 
			 
			flng1094FilingsListView.setOnMouseClicked(mouseClickedEvent -> 
			{ 
				if(flng1094FilingsListView.getSelectionModel().getSelectedItem() == null || mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) == false) return; 
	
	            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) 
	            { 
	            	current1094Filing = flng1094FilingsListView.getSelectionModel().getSelectedItem().get1094Filing(); 
	        		flngPrime1094FilingsListLabel.setText("Selected Irs1094 Filing"); 
	        		flngCalcNoticesListView.getItems().clear(); 
	        		flngExportNoticesListView.getItems().clear(); 
	            	show1094FilingData(); 
	            	update1094Data(); 
	            } 
	        }); 
			 
			flngExportNoticesListView.setOnMouseClicked(mouseClickedEvent -> 
			{ 
	            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) 
	            { 
	            	// check to make sure that we 
	    			if(flngExportNoticesListView.getSelectionModel().getSelectedItem() == null ) return; 
	
	            	// get the selected export notice
	            	try {
		            	ExportNotice notice = flngExportNoticesListView.getSelectionModel().getSelectedItem().getExportNotice(); 
		        		// chekc to see if it contains 1095c info
		            	if(notice.getMessage().contains("Irs1095Filing=") == false) return;
		            	// parse out to get the id from the message
		            	String idString = notice.getMessage().replaceAll("[^0-9]", "");
		            	idString = idString.replace("1095", "");
		            	// conver to long id
		            	Long id = Long.valueOf(idString);
		            	//and retieve the filing
		            	Irs1095FilingRequest filingRequest = new Irs1095FilingRequest(); 
		            	filingRequest.setId(id);
		            	Irs1095Filing filing = null;
	            		filing = AdminPersistenceManager.getInstance().get(filingRequest);
	            		// get the Irs1095c
	            		if(filing != null && filing.getIrs1095cId() != null)
	            		{
	            			Irs1095cRequest cReq = new Irs1095cRequest();
	            			cReq.setId(filing.getIrs1095cId());            			
	            			Irs1095c irs1095c = AdminPersistenceManager.getInstance().get(cReq);
	            			if(irs1095c != null && irs1095c.getEmployeeId() != null) 
	            			{
	            				// get the employee
	            				EmployeeRequest eReq = new EmployeeRequest();
	            				eReq.setId(irs1095c.getEmployeeId());
	            				Employee employee = AdminPersistenceManager.getInstance().get(eReq);
	            				if(employee != null) 
	            				{
	            					// verify we got the correct emploee for this employer
	            					if(employee.getEmployerId().equals(DataManager.i().mEmployer.getId()) == false)
	            					{
	            						Utils.showAlert("Employer Mismatch", "Incorrect employer for selected employee. Ignoring Selection");
	            						return;
	            					}
	            					DataManager.i().mEmployee = employee;
	            	            	//set the parent objects
	            	            	DataManager.i().mEmployer.setAccount(DataManager.i().mAccount);
	            	            	DataManager.i().mEmployee.setEmployer(DataManager.i().mEmployer);
	
	            	            	// and load the employee screen
	            					EtcAdmin.i().setScreen(ScreenType.EMPLOYEEFROMTAXYEAR, true);
	            				}
	            			}
	            		}
	        		} catch (Exception e) { DataManager.i().log(Level.SEVERE, e); } 
	            } 
	        }); 
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		} 

	} 
 
	private void clearScreen()
	{ 
		show1094b(false); 
		show1094c(false); 
		flng1095FilingsListView.getItems().clear(); 
		flngCalcNoticesListView.getItems().clear(); 
		flngExportNoticesListView.getItems().clear(); 
		flng1094SubmissionsListView.getItems().clear(); 
		flng1094FilingsListView.getItems().clear(); 
		 
		Utils.updateControl(flngIrs1094bFileCICheck,false);	 
		Utils.updateControl(flngIrs1094bNewFormsCheck,false);	 
		Utils.updateControl(flngIrs1094cFileCICheck,false);	 
		Utils.updateControl(flngIrs1094cNewFormsCheck,false);	 
		Utils.updateControl(flngIrs1094cMecJanCheck,false);	 
		Utils.updateControl(flngIrs1094cMecFebCheck,false);	 
		Utils.updateControl(flngIrs1094cMecMarCheck,false);	 
		Utils.updateControl(flngIrs1094cMecAprCheck,false);	 
		Utils.updateControl(flngIrs1094cMecMayCheck,false);	 
		Utils.updateControl(flngIrs1094cMecJunCheck,false);	 
		Utils.updateControl(flngIrs1094cMecJulCheck,false);	 
		Utils.updateControl(flngIrs1094cMecAugCheck,false);	 
		Utils.updateControl(flngIrs1094cMecSepCheck,false);	 
		Utils.updateControl(flngIrs1094cMecOctCheck,false);	 
		Utils.updateControl(flngIrs1094cMecNovCheck,false);	 
		Utils.updateControl(flngIrs1094cMecDecCheck,false);	 
		Utils.updateControl(flngIrs1094cAgGrpJanCheck,false);	 
		Utils.updateControl(flngIrs1094cAgGrpFebCheck,false);	 
		Utils.updateControl(flngIrs1094cAgGrpMarCheck,false);	 
		Utils.updateControl(flngIrs1094cAgGrpAprCheck,false);	 
		Utils.updateControl(flngIrs1094cAgGrpMayCheck,false);	 
		Utils.updateControl(flngIrs1094cAgGrpJunCheck,false);	 
		Utils.updateControl(flngIrs1094cAgGrpJulCheck,false);	 
		Utils.updateControl(flngIrs1094cAgGrpAugCheck,false);	 
		Utils.updateControl(flngIrs1094cAgGrpSepCheck,false);	 
		Utils.updateControl(flngIrs1094cAgGrpOctCheck,false);	 
		Utils.updateControl(flngIrs1094cAgGrpNovCheck,false);	 
		Utils.updateControl(flngIrs1094cAgGrpDecCheck,false);	 
		Utils.updateControl(flngIrs1094cLckJanCheck,false);	 
		Utils.updateControl(flngIrs1094cLckFebCheck,false);	 
		Utils.updateControl(flngIrs1094cLckMarCheck,false);	 
		Utils.updateControl(flngIrs1094cLckAprCheck,false);	 
		Utils.updateControl(flngIrs1094cLckMayCheck,false);	 
		Utils.updateControl(flngIrs1094cLckJunCheck,false);	 
		Utils.updateControl(flngIrs1094cLckJulCheck,false);	 
		Utils.updateControl(flngIrs1094cLckAugCheck,false);	 
		Utils.updateControl(flngIrs1094cLckSepCheck,false);	 
		Utils.updateControl(flngIrs1094cLckOctCheck,false);	 
		Utils.updateControl(flngIrs1094cLckNovCheck,false);	 
		Utils.updateControl(flngIrs1094cLckDecCheck,false);	 
		Utils.updateControl(flngIrs1094FtCntJanField,"");	 
		Utils.updateControl(flngIrs1094FtCntFebField,"");	 
		Utils.updateControl(flngIrs1094FtCntMarField,"");	 
		Utils.updateControl(flngIrs1094FtCntAprField,"");	 
		Utils.updateControl(flngIrs1094FtCntMayField,"");	 
		Utils.updateControl(flngIrs1094FtCntJunField,"");	 
		Utils.updateControl(flngIrs1094FtCntJulField,"");	 
		Utils.updateControl(flngIrs1094FtCntAugField,"");	 
		Utils.updateControl(flngIrs1094FtCntSepField,"");	 
		Utils.updateControl(flngIrs1094FtCntOctField,"");	 
		Utils.updateControl(flngIrs1094FtCntNovField,"");	 
		Utils.updateControl(flngIrs1094FtCntDecField,"");	 
		Utils.updateControl(flngIrs1094TtlEmpCntJanField,"");	 
		Utils.updateControl(flngIrs1094TtlEmpCntFebField,"");	 
		Utils.updateControl(flngIrs1094TtlEmpCntMarField,"");	 
		Utils.updateControl(flngIrs1094TtlEmpCntAprField,"");	 
		Utils.updateControl(flngIrs1094TtlEmpCntMayField,"");	 
		Utils.updateControl(flngIrs1094TtlEmpCntJunField,"");	 
		Utils.updateControl(flngIrs1094TtlEmpCntJulField,"");	 
		Utils.updateControl(flngIrs1094TtlEmpCntAugField,"");	 
		Utils.updateControl(flngIrs1094TtlEmpCntSepField,"");	 
		Utils.updateControl(flngIrs1094TtlEmpCntOctField,"");	 
		Utils.updateControl(flngIrs1094TtlEmpCntNovField,"");	 
		Utils.updateControl(flngIrs1094TtlEmpCntDecField,"");	 
		Utils.updateControl(flngReadyToFileCheckBox, false); 
		Utils.updateControl(flngCorrectedCheckBox, false); 
		Utils.updateControl(flngAirformTypeLabel, ""); 
		Utils.updateControl(flngAirOpoerationTypeLabel, ""); 
		Utils.updateControl(flngAirStatusTypeLabel, ""); 
		Utils.updateControl(flngTotalFormsLabel, ""); 
		Utils.updateControl(flngFiledFormsLabel, ""); 
		Utils.updateControl(flngLastSubmittedLabel, ""); 
		Utils.updateControl(flngInitialAcceptanceLabel, ""); 
		Utils.updateControl(flngInitialReceiptIdLabel, ""); 
		Utils.updateControl(flngTaxYearLabel, ""); 
		
		Utils.updateControl(flngIrs1095cLckJanCheck,false);	 
		Utils.updateControl(flngIrs1095cLckFebCheck,false);	 
		Utils.updateControl(flngIrs1095cLckMarCheck,false);	 
		Utils.updateControl(flngIrs1095cLckAprCheck,false);	 
		Utils.updateControl(flngIrs1095cLckMayCheck,false);	 
		Utils.updateControl(flngIrs1095cLckJunCheck,false);	 
		Utils.updateControl(flngIrs1095cLckJulCheck,false);	 
		Utils.updateControl(flngIrs1095cLckAugCheck,false);	 
		Utils.updateControl(flngIrs1095cLckSepCheck,false);	 
		Utils.updateControl(flngIrs1095cLckOctCheck,false);	 
		Utils.updateControl(flngIrs1095cLckNovCheck,false);	 
		Utils.updateControl(flngIrs1095cLckDecCheck,false);	 
		
		Utils.updateControl(flngIrs1095cLckCountJanField,""); 
		Utils.updateControl(flngIrs1095cLckCountFebField,""); 
		Utils.updateControl(flngIrs1095cLckCountMarField,""); 
		Utils.updateControl(flngIrs1095cLckCountAprField,""); 
		Utils.updateControl(flngIrs1095cLckCountMayField,""); 
		Utils.updateControl(flngIrs1095cLckCountJunField,""); 
		Utils.updateControl(flngIrs1095cLckCountJulField,""); 
		Utils.updateControl(flngIrs1095cLckCountAugField,""); 
		Utils.updateControl(flngIrs1095cLckCountSepField,""); 
		Utils.updateControl(flngIrs1095cLckCountOctField,""); 
		Utils.updateControl(flngIrs1095cLckCountNovField,""); 
		Utils.updateControl(flngIrs1095cLckCountDecField,""); 
		
		Utils.updateControl(flngIrs1095cTotalCountJanField,""); 
		Utils.updateControl(flngIrs1095cTotalCountFebField,""); 
		Utils.updateControl(flngIrs1095cTotalCountMarField,""); 
		Utils.updateControl(flngIrs1095cTotalCountAprField,""); 
		Utils.updateControl(flngIrs1095cTotalCountMayField,""); 
		Utils.updateControl(flngIrs1095cTotalCountJunField,""); 
		Utils.updateControl(flngIrs1095cTotalCountJulField,""); 
		Utils.updateControl(flngIrs1095cTotalCountAugField,""); 
		Utils.updateControl(flngIrs1095cTotalCountSepField,""); 
		Utils.updateControl(flngIrs1095cTotalCountOctField,""); 
		Utils.updateControl(flngIrs1095cTotalCountNovField,""); 
		Utils.updateControl(flngIrs1095cTotalCountDecField,""); 

	} 
	
	private void set1095cEditable(boolean state)
	{
		Irs1095cLockYearCheck.setDisable(!state);
		flngIrs1095cLckJanCheck.setDisable(!state);
		flngIrs1095cLckFebCheck.setDisable(!state);
		flngIrs1095cLckMarCheck.setDisable(!state);
		flngIrs1095cLckAprCheck.setDisable(!state);
		flngIrs1095cLckMayCheck.setDisable(!state);
		flngIrs1095cLckJunCheck.setDisable(!state);
		flngIrs1095cLckJulCheck.setDisable(!state);
		flngIrs1095cLckAugCheck.setDisable(!state);
		flngIrs1095cLckSepCheck.setDisable(!state);
		flngIrs1095cLckOctCheck.setDisable(!state);
		flngIrs1095cLckNovCheck.setDisable(!state);
		flngIrs1095cLckDecCheck.setDisable(!state);
		flngIrs1095cSaveButton.setVisible(state);
		
		if(state == true) 
		{ 
			flngIrs1095cEditButton.setText("Cancel"); 
			flngIrs1095cSaveButton.setVisible(true); 
			flngIrs1095cGrid.getStyleClass().clear(); 
			flngIrs1095cGrid.setStyle(null);	 
			flngIrs1095cGrid.setStyle("-fx-background-color: #eeffee"); 
		} else { 
			flngIrs1095cEditButton.setText("Edit"); 
			flngIrs1095cSaveButton.setVisible(false); 
			flngIrs1095cGrid.getStyleClass().clear(); 
			flngIrs1095cGrid.setStyle(null);	 
			flngIrs1095cGrid.setStyle("-fx-background-color: #ccffff"); 
		} 
	}
	 
	private void loadFilingData()  
	{ 
		if(DataManager.i().mEmployer == null) return; 
		 
		// new thread 
		Task<Void> task = new Task<Void>()  
		{ 
            @Override 
            protected Void call() throws Exception
            { 
            	try {
            		filingData.refreshCurrentTaxYearFilingData(); 
        		} catch (Exception e) { 
        			DataManager.i().log(Level.SEVERE, e); 
        		} 
                return null; 
            } 
        }; 
         
      	task.setOnScheduled(e -> 
      	{ 
      		EtcAdmin.i().setStatusMessage("Updating Filing Data..."); 
      		EtcAdmin.i().setProgress(0.25);
      	}); 
      			 
    	task.setOnSucceeded(e ->  show1094bData()); 
    	task.setOnFailed(e ->  {
	    	DataManager.i().log(Level.SEVERE,task.getException());
    		show1094bData();
    	}); 
        new Thread(task).start(); 
	} 
	 
	private void update1094Data()  
	{ 
		if(current1094Filing == null) return; 
		 
		// new thread 
		Task<Void> task = new Task<Void>()  
		{ 
            @Override 
            protected Void call() throws Exception 
            { 
            	try {
                //AdminPersistenceManager.getInstance().getUpdatedAirFilingEvents(current1094Filing); 
                filingData.updateIrs1094Submissions(current1094Filing); 
        		} catch (Exception e) { 
        			DataManager.i().log(Level.SEVERE, e); 
        		} 
            	return null; 
            } 
        }; 
         
      	task.setOnScheduled(e ->  
      	{ 
      		EtcAdmin.i().setStatusMessage("Loading 1094 Data..."); 
      		EtcAdmin.i().setProgress(0.25);
      	}); 
    	task.setOnSucceeded(e ->  show1094Submissions()); 
    	task.setOnFailed(e ->  {
	    	DataManager.i().log(Level.SEVERE,task.getException());
	    	show1094Submissions();
    	}); 
    	
        new Thread(task).start(); 
	} 	 
	 
	private void showTaxYearData() 
	{ 
		try {
			if(DataManager.i().mTaxYear != null)  
			{ 
				String sName = String.valueOf(DataManager.i().mTaxYear.getYear())  + " Tax Year for " + DataManager.i().mEmployer.getName(); 
				Utils.updateControl(txyrTitleLabel,sName);				 
				Utils.updateControl(txyrCoreIdLabel, DataManager.i().mTaxYear.getId()); 
		
				// Process Status 
				if(DataManager.i().mTaxYear.getProcessStatus() != null)  
					flngIrs1094cProscessStatusCombo.getSelectionModel().select(DataManager.i().mTaxYear.getProcessStatus()); 
				
				// closed 
				Utils.updateControl(txyrClosedCheckBox,DataManager.i().mTaxYear.isClosed()); 
				if(DataManager.i().mTaxYear.getClosedBy() != null)  
					Utils.updateControl(txyrClosedByLabel,DataManager.i().mTaxYear.getClosedBy().getFirstName()  + " " + DataManager.i().mTaxYear.getClosedBy().getLastName()); 
				else 
					Utils.updateControl(txyrClosedByLabel,""); 
				Utils.updateControl(txyrClosedOnLabel,DataManager.i().mTaxYear.getClosedOn()); 
				 
				// filing approved 
				Utils.updateControl(txyrApprovedCheckBox,DataManager.i().mTaxYear.isFilingApproved()); 
				if(DataManager.i().mTaxYear.getFilingApprovedBy()!= null)  
					Utils.updateControl(txyrApprovedByLabel,DataManager.i().mTaxYear.getFilingApprovedBy().getFirstName()  + " " + DataManager.i().mTaxYear.getFilingApprovedBy().getLastName()); 
				else 
					Utils.updateControl(txyrApprovedByLabel,""); 
				Utils.updateControl(txyrApprovedOnLabel,DataManager.i().mTaxYear.getFilingApprovedOn()); 
				 
				// print option 
				if(DataManager.i().mTaxYear.getPrintType() != null)  
					txyrPrintOptionLabel.getSelectionModel().select(DataManager.i().mTaxYear.getPrintType()); 
				 
				// form option 
				if(DataManager.i().mTaxYear.getFormType() != null)  
					txyrAirformTypeLabel.getSelectionModel().select(DataManager.i().mTaxYear.getFormType()); 
				 
				// safe harbor code 
				if(DataManager.i().mTaxYear.getShDefault() != null)  
					txyrSafeHarborCodeLabel.getSelectionModel().select(DataManager.i().mTaxYear.getShDefault()); 
	 
				// liason 
				if(DataManager.i().mTaxYear.getLiaison() != null)  
					Utils.updateControl(txyrLiasonLabel,DataManager.i().mTaxYear.getLiaison().getFirstName()  + " " + DataManager.i().mTaxYear.getLiaison().getLastName()); 
				else 
					Utils.updateControl(txyrLiasonLabel,""); 
				 
				// jr liason 
				if(DataManager.i().mTaxYear.getJrLiaison() != null)  
					Utils.updateControl(txyrJrLiasonLabel,DataManager.i().mTaxYear.getJrLiaison().getFirstName()  + " " + DataManager.i().mTaxYear.getJrLiaison().getLastName()); 
				else 
					Utils.updateControl(txyrJrLiasonLabel,""); 
			} 
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		} 
		
	} 
	 
	private void showTaxYearServiceLevelData() 
	{ 
		if(DataManager.i().mTaxYear != null && DataManager.i().mTaxYear.getServiceLevel() != null)  
		{ 
			Utils.updateControl(txyrSvcLvlNameLabel,DataManager.i().mTaxYear.getServiceLevel().getName()); 
			Utils.updateControl(txyrSvcLvlDescriptionLabel,DataManager.i().mTaxYear.getServiceLevel().getDescription()); 
			Utils.updateControl(txyrSvcLvlGenerateCodesCheckBox,DataManager.i().mTaxYear.getServiceLevel().isGenerateCodes()); 
			Utils.updateControl(txyrSvcLvlTrackEligibilityCheckBox,DataManager.i().mTaxYear.getServiceLevel().isTrackEligibility()); 
		} 
	} 
	 
	private void saveTaxYear() 
	{ 
		try {
			//update the current tax year 
			if(txyrLiasonLabel.getText() != null && !txyrLiasonLabel.getText().isEmpty()) 
				DataManager.i().mTaxYear.setLiaisonId(Long.valueOf(txyrLiasonLabel.getText())); 
			if(txyrJrLiasonLabel.getText() != null && !txyrJrLiasonLabel.getText().isEmpty()) 
				DataManager.i().mTaxYear.setJrLiaisonId(Long.valueOf(txyrJrLiasonLabel.getText())); 
			if(txyrClosedByLabel.getText() != null && !txyrClosedByLabel.getText().isEmpty()) 
				DataManager.i().mTaxYear.setClosedById(Long.valueOf(txyrClosedByLabel.getText())); 
			if(txyrApprovedByLabel.getText() != null && !txyrApprovedByLabel.getText().isEmpty()) 
				DataManager.i().mTaxYear.setFilingApprovedById(Long.valueOf(txyrApprovedByLabel.getText())); 
			DataManager.i().mTaxYear.setShDefault(txyrSafeHarborCodeLabel.getValue()); 
			DataManager.i().mTaxYear.setClosed(Boolean.valueOf(txyrClosedCheckBox.getText())); 
			DataManager.i().mTaxYear.setFilingApproved(Boolean.valueOf(txyrApprovedCheckBox.getText())); 
			DataManager.i().mTaxYear.setClosedOn(Utils.getCalDate(txyrClosedOnLabel)); 
			DataManager.i().mTaxYear.setFilingApprovedOn(Utils.getCalDate(txyrApprovedOnLabel)); 
			DataManager.i().mTaxYear.setFormType(txyrAirformTypeLabel.getValue()); 
			DataManager.i().mTaxYear.setPrintType(txyrPrintOptionLabel.getValue()); 
	
			if(flngIrs1094cProscessStatusCombo.getSelectionModel().getSelectedItem() != null)  
				DataManager.i().mTaxYear.setProcessStatus(flngIrs1094cProscessStatusCombo.getSelectionModel().getSelectedItem()); 
					
			//create the request 
			TaxYearRequest request = new TaxYearRequest(); 
			request.setEntity(DataManager.i().mTaxYear); 
			request.setId(DataManager.i().mTaxYear.getId()); 
			// send it to the server 
			DataManager.i().mTaxYear = AdminPersistenceManager.getInstance().addOrUpdate(request);
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		} 
	} 
	
	private void updateTaxYearStatus(AcaProcessType status) {
		try {
			// if the 1094c is still locked, it gets a new
			if (filingData.isTaxYearLocked() == false)
				DataManager.i().mTaxYear.setProcessStatus(AcaProcessType.NEW); 
			else	
				DataManager.i().mTaxYear.setProcessStatus(status); 
			flngIrs1094cProscessStatusCombo.getSelectionModel().select(status);
			DataManager.i().mTaxYear.setProcessStatus(status);
			//create the request 
			TaxYearRequest request = new TaxYearRequest(); 
			request.setEntity(DataManager.i().mTaxYear); 
			request.setId(DataManager.i().mTaxYear.getId()); 
			// send it to the server 
			DataManager.i().mTaxYear = AdminPersistenceManager.getInstance().addOrUpdate(request);
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		} 
	}
	 
	private void show1094b(boolean show) 
	{ 
		if(show == true) 
		{ 
			flngIrs1094bGrid.setVisible(true); 
			flngIrs1094bGrid.setMinHeight(100); 
			flngIrs1094bGrid.setPrefHeight(100); 
			flngIrs1094bGrid.setMaxHeight(100); 
		} else { 
			flngIrs1094bGrid.setVisible(false); 
			flngIrs1094bGrid.setMinHeight(0); 
			flngIrs1094bGrid.setPrefHeight(0); 
			flngIrs1094bGrid.setMaxHeight(0); 
		} 
	} 
	 
	private void show1094c(boolean show)
	{ 
		if(show == true) 
		{ 
			flngIrs1094cGrid.setVisible(true); 
			flngIrs1094cGrid.setMinHeight(705); 
			flngIrs1094cGrid.setPrefHeight(705); 
			flngIrs1094cGrid.setMaxHeight(705); 
			flngIrs1095cGrid.setVisible(true); 
			flngIrs1095cGrid.setMinHeight(705); 
			flngIrs1095cGrid.setPrefHeight(705); 
			flngIrs1095cGrid.setMaxHeight(705); 
		} else { 
			flngIrs1094cGrid.setVisible(false); 
			flngIrs1094cGrid.setMinHeight(0); 
			flngIrs1094cGrid.setPrefHeight(0); 
			flngIrs1094cGrid.setMaxHeight(0); 
			flngIrs1095cGrid.setVisible(false); 
			flngIrs1095cGrid.setMinHeight(0); 
			flngIrs1095cGrid.setPrefHeight(0); 
			flngIrs1095cGrid.setMaxHeight(0); 
		} 
	} 
	 
	private void show1094FilingData()
	{ 
		if( current1094Filing != null) 
		{
			if(current1094Filing.getFilingType() != null)
				Utils.updateControl(flngTypeField, current1094Filing.getFilingType().toString()); 
			if(current1094Filing.getFilingState() != null)
				Utils.updateControl(flngStateField, current1094Filing.getFilingState().toString()); 
			Utils.updateControl(flngReadyToFileCheckBox, current1094Filing.isReadyToFile()); 
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
			if(current1094Filing.getTaxYear() != null) 
				Utils.updateControl(flngTaxYearLabel, current1094Filing.getTaxYear().getYear()); 
		} 
		 
		show1095Filings(); 
		showCalcNotices(); 
		update1094Data(); 
	} 
	 
	private void show1094bData()
	{ 
		try {
			//load the list for all 1094filings for this tax year 
			load1094Filings(); 
			// determine if it is type b or type c by the taxyear formtype
			isTypeB = false;
			if (DataManager.i().mTaxYear.getFormType() !=  null && DataManager.i().mTaxYear.getFormType().equals(AcaFormType.B)) {
				show1094b(true); 
				show1094c(false); 
				isTypeB = true;
			}
			
			current1094b = null; 
			if(filingData.mIrs1094bs != null) 
				for(Irs1094b irs1094b : filingData.mIrs1094bs) 
					if(irs1094b.getTaxYearId() != null) 
						if(irs1094b.getTaxYearId().equals(DataManager.i().mTaxYear.getId())) 
						{ 
							current1094b = irs1094b; 
							getPrime1094Filing(current1094b); 
							break; 
						} 
			 
			if(current1094b != null)
			{ 
				Utils.updateControl(flngIrs1094bFileCICheck,current1094b.isFileCIWithSSN());	 
				Utils.updateControl(flngIrs1094bNewFormsCheck,current1094b.isWithNewForms());	 
				//return; 
			} 
		} catch (Exception e) {
        	DataManager.i().log( Level.SEVERE, e);
		}
		 
		show1094cData(); 
		show1094FilingData(); 
		showCalcNotices(); 
	} 
	 
	private void saveIrs1094b()
	{ 
		try {
			// update 
			DataManager.i().mIrs1094b.setFileCIWithSSN(flngIrs1094bFileCICheck.isSelected());	 
			DataManager.i().mIrs1094b.setWithNewForms(flngIrs1094bNewFormsCheck.isSelected());	 
		
			//create the request 
			Irs1094bRequest request = new Irs1094bRequest(); 
			request.setEntity(DataManager.i().mIrs1094b); 
			request.setId(DataManager.i().mIrs1094b.getId()); 
			
			// update the server 
			DataManager.i().mIrs1094b = AdminPersistenceManager.getInstance().addOrUpdate(request);
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		} 
	} 
	 
	private void set1094bEditable(boolean editable) 
	{ 
		if(editable == true) 
		{ 
			Irs1094bEditButton.setText("Cancel"); 
			Irs1094bSaveButton.setVisible(true); 
			flngIrs1094bGrid.setStyle("-fx-background-color: #eeffee"); 
		} else { 
			Irs1094bEditButton.setText("Edit"); 
			Irs1094bSaveButton.setVisible(false); 
			flngIrs1094bGrid.getStyleClass().clear(); 
			flngIrs1094bGrid.setStyle(null);	 
			flngIrs1094bGrid.setStyle("-fx-background-color: #efefff"); 
		} 
		// always not editable 
		flngIrs1094bNewFormsCheck.setDisable(true); 
		// set the controls accordingly 
		flngIrs1094bFileCICheck.setDisable(!editable); 
	} 
 
	private void set1094cEditable(boolean editable) 
	{ 
		if(editable == true) 
		{ 
			Irs1094cEditButton.setText("Cancel"); 
			Irs1094cSaveButton.setVisible(true); 
			flngIrs1094cGrid.getStyleClass().clear(); 
			flngIrs1094cGrid.setStyle(null);	 
			flngIrs1094cGrid.setStyle("-fx-background-color: #eeffee"); 
		} else { 
			Irs1094cEditButton.setText("Edit"); 
			Irs1094cSaveButton.setVisible(false); 
			flngIrs1094cGrid.getStyleClass().clear(); 
			flngIrs1094cGrid.setStyle(null);	 
			flngIrs1094cGrid.setStyle("-fx-background-color: #ddeeff"); 
		} 
		// only Kendra, Alicia, and Heather can change authoritative flag 
		flngIrs1094cAuthoritativeCheck.setDisable(true);
		if (editable == true) {
			if (DataManager.i().mLocalUser.getId() == 3 ||  // Kendra 
				DataManager.i().mLocalUser.getId() == 20 || // Heather
				DataManager.i().mLocalUser.getId() == 22 )  // Alicia
				flngIrs1094cAuthoritativeCheck.setDisable(false);
		}
		// set the controls accordingly 
		Irs1094cLockYearCheck.setDisable(!editable);
		Irs1094cMecYearCheck.setDisable(!editable);
		Irs1094cAggGroupYearCheck.setDisable(!editable);
		flngIrs1094cNewFormsCheck.setDisable(!editable); 
		flngIrs1094cFileCICheck.setDisable(!editable); 
		flngIrs1094cMecJanCheck.setDisable(!editable); 
		flngIrs1094cMecFebCheck.setDisable(!editable); 
		flngIrs1094cMecMarCheck.setDisable(!editable); 
		flngIrs1094cMecAprCheck.setDisable(!editable); 
		flngIrs1094cMecMayCheck.setDisable(!editable); 
		flngIrs1094cMecJunCheck.setDisable(!editable); 
		flngIrs1094cMecJulCheck.setDisable(!editable); 
		flngIrs1094cMecAugCheck.setDisable(!editable); 
		flngIrs1094cMecSepCheck.setDisable(!editable); 
		flngIrs1094cMecOctCheck.setDisable(!editable); 
		flngIrs1094cMecNovCheck.setDisable(!editable); 
		flngIrs1094cMecDecCheck.setDisable(!editable); 
		flngIrs1094cAgGrpJanCheck.setDisable(!editable); 
		flngIrs1094cAgGrpFebCheck.setDisable(!editable); 
		flngIrs1094cAgGrpMarCheck.setDisable(!editable); 
		flngIrs1094cAgGrpAprCheck.setDisable(!editable); 
		flngIrs1094cAgGrpMayCheck.setDisable(!editable); 
		flngIrs1094cAgGrpJunCheck.setDisable(!editable); 
		flngIrs1094cAgGrpJulCheck.setDisable(!editable); 
		flngIrs1094cAgGrpAugCheck.setDisable(!editable); 
		flngIrs1094cAgGrpSepCheck.setDisable(!editable); 
		flngIrs1094cAgGrpOctCheck.setDisable(!editable); 
		flngIrs1094cAgGrpNovCheck.setDisable(!editable); 
		flngIrs1094cAgGrpDecCheck.setDisable(!editable); 
		flngIrs1094cLckJanCheck.setDisable(!editable); 
		flngIrs1094cLckFebCheck.setDisable(!editable); 
		flngIrs1094cLckMarCheck.setDisable(!editable); 
		flngIrs1094cLckAprCheck.setDisable(!editable); 
		flngIrs1094cLckMayCheck.setDisable(!editable); 
		flngIrs1094cLckJunCheck.setDisable(!editable); 
		flngIrs1094cLckJulCheck.setDisable(!editable); 
		flngIrs1094cLckAugCheck.setDisable(!editable); 
		flngIrs1094cLckSepCheck.setDisable(!editable); 
		flngIrs1094cLckOctCheck.setDisable(!editable); 
		flngIrs1094cLckNovCheck.setDisable(!editable); 
		flngIrs1094cLckDecCheck.setDisable(!editable); 
		flngIrs1094FtCntJanField.setEditable(editable); 
		flngIrs1094FtCntFebField.setEditable(editable); 
		flngIrs1094FtCntMarField.setEditable(editable); 
		flngIrs1094FtCntAprField.setEditable(editable); 
		flngIrs1094FtCntMayField.setEditable(editable); 
		flngIrs1094FtCntJunField.setEditable(editable); 
		flngIrs1094FtCntJulField.setEditable(editable); 
		flngIrs1094FtCntAugField.setEditable(editable); 
		flngIrs1094FtCntSepField.setEditable(editable); 
		flngIrs1094FtCntOctField.setEditable(editable); 
		flngIrs1094FtCntNovField.setEditable(editable); 
		flngIrs1094FtCntDecField.setEditable(editable); 
		flngIrs1094TtlEmpCntJanField.setEditable(editable); 
		flngIrs1094TtlEmpCntFebField.setEditable(editable); 
		flngIrs1094TtlEmpCntMarField.setEditable(editable); 
		flngIrs1094TtlEmpCntAprField.setEditable(editable); 
		flngIrs1094TtlEmpCntMayField.setEditable(editable); 
		flngIrs1094TtlEmpCntJunField.setEditable(editable); 
		flngIrs1094TtlEmpCntJulField.setEditable(editable); 
		flngIrs1094TtlEmpCntAugField.setEditable(editable); 
		flngIrs1094TtlEmpCntSepField.setEditable(editable); 
		flngIrs1094TtlEmpCntOctField.setEditable(editable); 
		flngIrs1094TtlEmpCntNovField.setEditable(editable); 
		flngIrs1094TtlEmpCntDecField.setEditable(editable); 
	} 

	private void getPrime1094Filing(Irs1094b irs1094b) 
	{ 
		try {
			current1094Filing = null; 
			if(irs1094b.getFiling() != null) 
			{ 
				for(Irs1094Filing filing : filingData.mIrs1094Filings) 
					if(filing.getId().equals(irs1094b.getFiling().getId())) 
					{ 
						current1094Filing = filing; 
						return; 
					} 
			}else { 
				if(filingData.mIrs1094Filings != null && filingData.mIrs1094Filings.size() > 0) 
					current1094Filing = filingData.mIrs1094Filings.get(0); 
			} 
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		} 

	} 
	 
	private void getPrime1094Filing(Irs1094c irs1094c) 
	{ 
		try {
			current1094Filing = null; 
			if(irs1094c.getFiling() != null) 
			{ 
				for(Irs1094Filing filing : filingData.mIrs1094Filings) 
					if(filing.getId().equals(irs1094c.getFiling().getId()))
					{ 
						current1094Filing = filing; 
						return; 
					} 
			}else { 
				if(filingData.mIrs1094Filings != null && filingData.mIrs1094Filings.size() > 0) 
					current1094Filing = filingData.mIrs1094Filings.get(0); 
			} 
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		} 
	} 
	 
	private void show1094cData()
	{ 
		try {
			current1094c = null; 
			if(filingData.mIrs1094cs != null) 
				for(Irs1094c irs1094c : filingData.mIrs1094cs) 
					if(irs1094c.getTaxYearId() != null) 
						if(irs1094c.getTaxYearId().equals(DataManager.i().mTaxYear.getId())) 
						{ 
							current1094c = irs1094c; 
							getPrime1094Filing(current1094c); 
							break; 
						} 
	 
			if(current1094c == null) return; 
			
			// set the locked indicator for the 1095cs
			locks = DataManager.i().getCorvettoManager().getIrs1095cMonthlyLockCount(current1094c.getId());
			if (locks.getAllLocked() != null && locks.getAllLocked() == true) {
				flngIrs1095cNotLockedLabel.setVisible(false);
				flngIrs1095cLockedLabel.setVisible(true);
			}else {
				flngIrs1095cNotLockedLabel.setVisible(true);
				flngIrs1095cLockedLabel.setVisible(false);
			}
		} catch (Exception e) {
			DataManager.i().log(Level.SEVERE, e);
		}
		
		// employer info for convenience
		Utils.updateControl(flngIrs1094cIncorporatedOn, DataManager.i().mEmployer.getIncorporatedOn());
		Utils.updateControl(flngIrs1094cDissolvedOn, DataManager.i().mEmployer.getDissolvedOn());
		// 1094c
		Utils.updateControl(flngIrs1094cAuthoritativeCheck,current1094c.isAuthoritative());	 
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
		setMECOfferYearLock();
		// agggrp
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
		setAggGroupYearLock();
		// locks
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
		set1094cYearLock();
		// ft count
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
		// total count
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
		Utils.updateControl(flngIrs1094cCalcPercentField,current1094c.getCalculatedPctg());	 
		Utils.updateControl(flngIrs1094cValidFormPercentField,current1094c.getValidFormPctg());	 
		// 1095c locks
		Utils.updateControl(flngIrs1095cLckJanCheck,locks.getTotalForms() - locks.getJanLockedCount() == 0); 
		Utils.updateControl(flngIrs1095cLckFebCheck,locks.getTotalForms() - locks.getFebLockedCount() == 0); 
		Utils.updateControl(flngIrs1095cLckMarCheck,locks.getTotalForms() - locks.getMarLockedCount() == 0); 
		Utils.updateControl(flngIrs1095cLckAprCheck,locks.getTotalForms() - locks.getAprLockedCount() == 0); 
		Utils.updateControl(flngIrs1095cLckMayCheck,locks.getTotalForms() - locks.getMayLockedCount() == 0); 
		Utils.updateControl(flngIrs1095cLckJunCheck,locks.getTotalForms() - locks.getJunLockedCount() == 0); 
		Utils.updateControl(flngIrs1095cLckJulCheck,locks.getTotalForms() - locks.getJulLockedCount() == 0); 
		Utils.updateControl(flngIrs1095cLckAugCheck,locks.getTotalForms() - locks.getAugLockedCount() == 0); 
		Utils.updateControl(flngIrs1095cLckSepCheck,locks.getTotalForms() - locks.getSepLockedCount() == 0); 
		Utils.updateControl(flngIrs1095cLckOctCheck,locks.getTotalForms() - locks.getOctLockedCount() == 0); 
		Utils.updateControl(flngIrs1095cLckNovCheck,locks.getTotalForms() - locks.getNovLockedCount() == 0); 
		Utils.updateControl(flngIrs1095cLckDecCheck,locks.getTotalForms() - locks.getDecLockedCount() == 0); 
		set1095cYearLock();
		// 1095c lock count
		Utils.updateControl(flngIrs1095cLckCountJanField,locks.getJanLockedCount()); 
		Utils.updateControl(flngIrs1095cLckCountFebField,locks.getFebLockedCount()); 
		Utils.updateControl(flngIrs1095cLckCountMarField,locks.getMarLockedCount()); 
		Utils.updateControl(flngIrs1095cLckCountAprField,locks.getAprLockedCount()); 
		Utils.updateControl(flngIrs1095cLckCountMayField,locks.getMayLockedCount()); 
		Utils.updateControl(flngIrs1095cLckCountJunField,locks.getJunLockedCount()); 
		Utils.updateControl(flngIrs1095cLckCountJulField,locks.getJulLockedCount()); 
		Utils.updateControl(flngIrs1095cLckCountAugField,locks.getAugLockedCount()); 
		Utils.updateControl(flngIrs1095cLckCountSepField,locks.getSepLockedCount()); 
		Utils.updateControl(flngIrs1095cLckCountOctField,locks.getOctLockedCount()); 
		Utils.updateControl(flngIrs1095cLckCountNovField,locks.getNovLockedCount()); 
		Utils.updateControl(flngIrs1095cLckCountDecField,locks.getDecLockedCount()); 
		// 1095c Unlokced count tbd
		Utils.updateControl(flngIrs1095cTotalCountJanField,locks.getTotalForms().intValue() - locks.getJanLockedCount().intValue()); 
		Utils.updateControl(flngIrs1095cTotalCountFebField,locks.getTotalForms().intValue() - locks.getFebLockedCount().intValue()); 
		Utils.updateControl(flngIrs1095cTotalCountMarField,locks.getTotalForms().intValue() - locks.getMarLockedCount().intValue()); 
		Utils.updateControl(flngIrs1095cTotalCountAprField,locks.getTotalForms().intValue() - locks.getAprLockedCount().intValue()); 
		Utils.updateControl(flngIrs1095cTotalCountMayField,locks.getTotalForms().intValue() - locks.getMayLockedCount().intValue()); 
		Utils.updateControl(flngIrs1095cTotalCountJunField,locks.getTotalForms().intValue() - locks.getJunLockedCount().intValue()); 
		Utils.updateControl(flngIrs1095cTotalCountJulField,locks.getTotalForms().intValue() - locks.getJulLockedCount().intValue()); 
		Utils.updateControl(flngIrs1095cTotalCountAugField,locks.getTotalForms().intValue() - locks.getAugLockedCount().intValue()); 
		Utils.updateControl(flngIrs1095cTotalCountSepField,locks.getTotalForms().intValue() - locks.getSepLockedCount().intValue()); 
		Utils.updateControl(flngIrs1095cTotalCountOctField,locks.getTotalForms().intValue() - locks.getOctLockedCount().intValue()); 
		Utils.updateControl(flngIrs1095cTotalCountNovField,locks.getTotalForms().intValue() - locks.getNovLockedCount().intValue()); 
		Utils.updateControl(flngIrs1095cTotalCountDecField,locks.getTotalForms().intValue() - locks.getDecLockedCount().intValue()); 
		// total count
		Utils.updateControl(flngIrs1095cTotalCountField,locks.getTotalForms()); 
		
		show1094b(false); 
		show1094c(true); 
	} 

	private void set1095cYearLock() {
		if (flngIrs1095cLckJanCheck.isSelected() && flngIrs1095cLckFebCheck.isSelected() && flngIrs1095cLckMarCheck.isSelected() && flngIrs1095cLckAprCheck.isSelected() &&
			flngIrs1095cLckMayCheck.isSelected() && flngIrs1095cLckJunCheck.isSelected() && flngIrs1095cLckJulCheck.isSelected() && flngIrs1095cLckAugCheck.isSelected() &&
			flngIrs1095cLckSepCheck.isSelected() && flngIrs1095cLckOctCheck.isSelected() && flngIrs1095cLckNovCheck.isSelected() && flngIrs1095cLckDecCheck.isSelected())
			Irs1095cLockYearCheck.setSelected(true);
		else
			Irs1095cLockYearCheck.setSelected(false);
	}

	@FXML
	private void set1094cYearLockByScreen() {
		if (flngIrs1094cLckJanCheck.isSelected() && flngIrs1094cLckFebCheck.isSelected() && flngIrs1094cLckMarCheck.isSelected() && flngIrs1094cLckAprCheck.isSelected() &&
			flngIrs1094cLckMayCheck.isSelected() && flngIrs1094cLckJunCheck.isSelected() && flngIrs1094cLckJulCheck.isSelected() && flngIrs1094cLckAugCheck.isSelected() &&
			flngIrs1094cLckSepCheck.isSelected() && flngIrs1094cLckOctCheck.isSelected() && flngIrs1094cLckNovCheck.isSelected() && flngIrs1094cLckDecCheck.isSelected())
			Irs1094cLockYearCheck.setSelected(true);
		else
			Irs1094cLockYearCheck.setSelected(false);
	}
	
	@FXML
	private void setAggGroupYearLockByScreen() {
		if (flngIrs1094cAgGrpJanCheck.isSelected() && flngIrs1094cAgGrpFebCheck.isSelected() && flngIrs1094cAgGrpMarCheck.isSelected() && flngIrs1094cAgGrpAprCheck.isSelected() &&
			flngIrs1094cAgGrpMayCheck.isSelected() && flngIrs1094cAgGrpJunCheck.isSelected() && flngIrs1094cAgGrpJulCheck.isSelected() && flngIrs1094cAgGrpAugCheck.isSelected() &&
			flngIrs1094cAgGrpSepCheck.isSelected() && flngIrs1094cAgGrpOctCheck.isSelected() && flngIrs1094cAgGrpNovCheck.isSelected() && flngIrs1094cAgGrpDecCheck.isSelected())
			Irs1094cAggGroupYearCheck.setSelected(true);
		else
			Irs1094cAggGroupYearCheck.setSelected(false);
	}
	
	@FXML
	private void setMECOfferYearLockByScreen() {
		if (flngIrs1094cMecJanCheck.isSelected() && flngIrs1094cMecFebCheck.isSelected() && flngIrs1094cMecMarCheck.isSelected() && flngIrs1094cMecAprCheck.isSelected() &&
			flngIrs1094cMecMayCheck.isSelected() && flngIrs1094cMecJunCheck.isSelected() && flngIrs1094cMecJulCheck.isSelected() && flngIrs1094cMecAugCheck.isSelected() &&
			flngIrs1094cMecSepCheck.isSelected() && flngIrs1094cMecOctCheck.isSelected() && flngIrs1094cMecNovCheck.isSelected() && flngIrs1094cMecDecCheck.isSelected())
			Irs1094cMecYearCheck.setSelected(true);
		else
			Irs1094cMecYearCheck.setSelected(false);
	}
	
	private void set1094cYearLock() {
		if (current1094c.isJanLocked() && current1094c.isFebLocked() && current1094c.isMarLocked() && current1094c.isAprLocked() &&
			current1094c.isMayLocked() && current1094c.isJunLocked() && current1094c.isJulLocked() && current1094c.isAugLocked() &&
			current1094c.isSepLocked() && current1094c.isOctLocked() && current1094c.isNovLocked() && current1094c.isDecLocked())
			Irs1094cLockYearCheck.setSelected(true);
		else
			Irs1094cLockYearCheck.setSelected(false);
	}
	
	private void setAggGroupYearLock() {
		if (current1094c.isJanAggGrp() && current1094c.isFebAggGrp() && current1094c.isMarAggGrp() && current1094c.isAprAggGrp() &&
			current1094c.isMayAggGrp() && current1094c.isJunAggGrp() && current1094c.isJulAggGrp() && current1094c.isAugAggGrp() &&
			current1094c.isSepAggGrp() && current1094c.isOctAggGrp() && current1094c.isNovAggGrp() && current1094c.isDecAggGrp())
			Irs1094cAggGroupYearCheck.setSelected(true);
		else
			Irs1094cAggGroupYearCheck.setSelected(false);
	}
	
	private void setMECOfferYearLock() {
		if (current1094c.isJanMECOffer() && current1094c.isFebMECOffer() && current1094c.isMarMECOffer() && current1094c.isAprMECOffer() &&
			current1094c.isMayMECOffer() && current1094c.isJunMECOffer() && current1094c.isJulMECOffer() && current1094c.isAugMECOffer() &&
			current1094c.isSepMECOffer() && current1094c.isOctMECOffer() && current1094c.isNovMECOffer() && current1094c.isDecMECOffer())
			Irs1094cMecYearCheck.setSelected(true);
		else
			Irs1094cMecYearCheck.setSelected(false);
	}
	
	private void saveIrs1094c()
	{ 
		
		current1094c.setFileCIWithSSN(flngIrs1094cFileCICheck.isSelected());	 
		current1094c.setWithNewForms(flngIrs1094cNewFormsCheck.isSelected());
		current1094c.setAuthoritative(flngIrs1094cAuthoritativeCheck.isSelected());
 
		current1094c.setJanMECOffer(flngIrs1094cMecJanCheck.isSelected());	 
		current1094c.setFebMECOffer(flngIrs1094cMecFebCheck.isSelected());	 
		current1094c.setMarMECOffer(flngIrs1094cMecMarCheck.isSelected());	 
		current1094c.setAprMECOffer(flngIrs1094cMecAprCheck.isSelected());	 
		current1094c.setMayMECOffer(flngIrs1094cMecMayCheck.isSelected());	 
		current1094c.setJunMECOffer(flngIrs1094cMecJunCheck.isSelected());	 
		current1094c.setJulMECOffer(flngIrs1094cMecJulCheck.isSelected());	 
		current1094c.setAugMECOffer(flngIrs1094cMecAugCheck.isSelected());	 
		current1094c.setSepMECOffer(flngIrs1094cMecSepCheck.isSelected());	 
		current1094c.setOctMECOffer(flngIrs1094cMecOctCheck.isSelected());	 
		current1094c.setNovMECOffer(flngIrs1094cMecNovCheck.isSelected());	 
		current1094c.setDecMECOffer(flngIrs1094cMecDecCheck.isSelected());	 
 
		current1094c.setJanAggGrp(flngIrs1094cAgGrpJanCheck.isSelected());	 
		current1094c.setFebAggGrp(flngIrs1094cAgGrpFebCheck.isSelected());	 
		current1094c.setMarAggGrp(flngIrs1094cAgGrpMarCheck.isSelected());	 
		current1094c.setAprAggGrp(flngIrs1094cAgGrpAprCheck.isSelected());	 
		current1094c.setMayAggGrp(flngIrs1094cAgGrpMayCheck.isSelected());	 
		current1094c.setJunAggGrp(flngIrs1094cAgGrpJunCheck.isSelected());	 
		current1094c.setJulAggGrp(flngIrs1094cAgGrpJulCheck.isSelected());	 
		current1094c.setAugAggGrp(flngIrs1094cAgGrpAugCheck.isSelected());	 
		current1094c.setSepAggGrp(flngIrs1094cAgGrpSepCheck.isSelected());	 
		current1094c.setOctAggGrp(flngIrs1094cAgGrpOctCheck.isSelected());	 
		current1094c.setNovAggGrp(flngIrs1094cAgGrpNovCheck.isSelected());	 
		current1094c.setDecAggGrp(flngIrs1094cAgGrpDecCheck.isSelected());	 
 
		current1094c.setJanLocked(flngIrs1094cLckJanCheck.isSelected());	 
		current1094c.setFebLocked(flngIrs1094cLckFebCheck.isSelected());	 
		current1094c.setMarLocked(flngIrs1094cLckMarCheck.isSelected());	 
		current1094c.setAprLocked(flngIrs1094cLckAprCheck.isSelected());	 
		current1094c.setMayLocked(flngIrs1094cLckMayCheck.isSelected());	 
		current1094c.setJunLocked(flngIrs1094cLckJunCheck.isSelected());	 
		current1094c.setJulLocked(flngIrs1094cLckJulCheck.isSelected());	 
		current1094c.setAugLocked(flngIrs1094cLckAugCheck.isSelected());	 
		current1094c.setSepLocked(flngIrs1094cLckSepCheck.isSelected());	 
		current1094c.setOctLocked(flngIrs1094cLckOctCheck.isSelected());	 
		current1094c.setNovLocked(flngIrs1094cLckNovCheck.isSelected());	 
		current1094c.setDecLocked(flngIrs1094cLckDecCheck.isSelected());	 
		 
		current1094c.setJanFTEmpCount(Utils.getLong(flngIrs1094FtCntJanField));	 
		current1094c.setFebFTEmpCount(Utils.getLong(flngIrs1094FtCntFebField));	 
		current1094c.setMarFTEmpCount(Utils.getLong(flngIrs1094FtCntMarField));	 
		current1094c.setAprFTEmpCount(Utils.getLong(flngIrs1094FtCntAprField));	 
		current1094c.setMayFTEmpCount(Utils.getLong(flngIrs1094FtCntMayField));	 
		current1094c.setJunFTEmpCount(Utils.getLong(flngIrs1094FtCntJunField));	 
		current1094c.setJulFTEmpCount(Utils.getLong(flngIrs1094FtCntJulField));	 
		current1094c.setAugFTEmpCount(Utils.getLong(flngIrs1094FtCntAugField));	 
		current1094c.setSepFTEmpCount(Utils.getLong(flngIrs1094FtCntSepField));	 
		current1094c.setOctFTEmpCount(Utils.getLong(flngIrs1094FtCntOctField));	 
		current1094c.setNovFTEmpCount(Utils.getLong(flngIrs1094FtCntNovField));	 
		current1094c.setDecFTEmpCount(Utils.getLong(flngIrs1094FtCntDecField));	 
		 
		current1094c.setJanTotEmpCount(Utils.getLong(flngIrs1094TtlEmpCntJanField));	 
		current1094c.setFebTotEmpCount(Utils.getLong(flngIrs1094TtlEmpCntFebField));	 
		current1094c.setMarTotEmpCount(Utils.getLong(flngIrs1094TtlEmpCntMarField));	 
		current1094c.setAprTotEmpCount(Utils.getLong(flngIrs1094TtlEmpCntAprField));	 
		current1094c.setMayTotEmpCount(Utils.getLong(flngIrs1094TtlEmpCntMayField));	 
		current1094c.setJunTotEmpCount(Utils.getLong(flngIrs1094TtlEmpCntJunField));	 
		current1094c.setJulTotEmpCount(Utils.getLong(flngIrs1094TtlEmpCntJulField));	 
		current1094c.setAugTotEmpCount(Utils.getLong(flngIrs1094TtlEmpCntAugField));	 
		current1094c.setSepTotEmpCount(Utils.getLong(flngIrs1094TtlEmpCntSepField));	 
		current1094c.setOctTotEmpCount(Utils.getLong(flngIrs1094TtlEmpCntOctField));	 
		current1094c.setNovTotEmpCount(Utils.getLong(flngIrs1094TtlEmpCntNovField));	 
		current1094c.setDecTotEmpCount(Utils.getLong(flngIrs1094TtlEmpCntDecField));	 
		 
		//create the request 
		Irs1094cRequest request = new Irs1094cRequest(); 
		request.setEntity(current1094c); 
		request.setId(current1094c.getId()); 
		request.setUpdateLocks(true);

		// sned it to the server 
		try {
			current1094c = AdminPersistenceManager.getInstance().addOrUpdate(request);
			updateTaxYearStatus(AcaProcessType.LOCKED);
		} catch (CoreException e) { DataManager.i().log(Level.SEVERE, e); } 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
	 
	private void load1094Filings() 
	{    
		try {
			if(filingData.mIrs1094Filings != null && filingData.mIrs1094Filings.size() > 0) 
			{ 
				flng1094FilingsListView.getItems().clear(); 
				flng1094FilingsListView.getItems().add(new HBox1094FilingCell(null)); 
				for(Irs1094Filing irs1094Filing : filingData.mIrs1094Filings) 
				{ 
					if(irs1094Filing == null || irs1094Filing.getTaxYearId() == null) 
					{ 
						flng1094FilingsListView.getItems().add(new HBox1094FilingCell(irs1094Filing)); 
						continue; 
					} 
					if(irs1094Filing.getTaxYearId().equals(DataManager.i().mTaxYear.getId())) 
						flng1094FilingsListView.getItems().add(new HBox1094FilingCell(irs1094Filing)); 
				};	 
				// clear it if we have no data 
				if(flng1094FilingsListView.getItems().size() < 2) 
					flng1094FilingsListView.getItems().clear(); 
			} 
		} catch (Exception e) {
        	DataManager.i().log( Level.SEVERE, e);
		}

	} 
	 
	private void show1095Filings() 
	{  
		try {
			if(filingData.mIrs1095Filings != null && filingData.mIrs1095Filings.size() > 0) 
			{ 
				flng1095FilingsListView.getItems().clear(); 
				boolean addHeader = false; 
				is1095b = false; 
		
				for(Irs1095Filing irs1095Filing : filingData.mIrs1095Filings) 
				{ 
					if(irs1095Filing == null || irs1095Filing.getTaxYearId() == null) continue; 
					if(irs1095Filing.getTaxYearId().equals(DataManager.i().mTaxYear.getId())) 
					{ 
						if(addHeader == false) 
						{ 
							// set the irs1095b flag for the checkbox column inclusion 
							if(irs1095Filing.getIrs1095b() != null)  
								is1095b = true; 
							// add the header only once 
							addHeader = true; 
							flng1095FilingsListView.getItems().add(new HBox1095FilingCell(null)); 
						} 
						// filter 
						HBox1095FilingCell newCell = new HBox1095FilingCell(irs1095Filing); 
						//if(newCell.filter(flng1095FilterText.getText()) == false) continue; 
						flng1095FilingsListView.getItems().add(newCell); 
					} 
				};	 
				// clear it if we have no data 
				if(flng1095FilingsListView.getItems().size() < 2) 
					flng1095FilingsListView.getItems().clear(); 
			} 
		} catch (Exception e) {
        	DataManager.i().log( Level.SEVERE, e);
		}
	} 
	 
	private void showCalcNotices() 
	{  
		try {
			flngCalcNoticesListView.getItems().clear(); 
			if(filingData.mCalculationNotices != null && filingData.mCalculationNotices.size() > 0) 
			{ 
				flngCalcNoticesListView.getItems().add(new HBoxCalculationNoticeCell(null));
	
				for(CalculationNotice notice : filingData.mCalculationNotices) 
				{ 
					if(notice == null) continue; 
					flngCalcNoticesListView.getItems().add(new HBoxCalculationNoticeCell(notice)); 
				};	 
			} 
		} catch (Exception e) {
        	DataManager.i().log( Level.SEVERE, e);
		}

		show1094Submissions(); 
	} 
	 
	private void showExportNotices() {  
		try {
			flngExportNoticesListView.getItems().clear(); 
			if(filingData.mExportNotices != null && filingData.mExportNotices.size() > 0) 
			{ 
				flngExportNoticesListView.getItems().add(new HBoxExportNoticeCell(null)); 
	
				for(ExportNotice notice : filingData.mExportNotices) 
				{ 
					if(notice == null) continue; 
					flngExportNoticesListView.getItems().add(new HBoxExportNoticeCell(notice)); 
				};	 
			} 	
		} catch (Exception e) {
        	DataManager.i().log( Level.SEVERE, e);
		}

	} 
	 
	private void show1094Submissions() 
	{   
		try {
			if(filingData.mIrs1094Submissions != null && filingData.mIrs1094Submissions.size() > 0) 
			{ 
				flng1094SubmissionsListView.getItems().clear(); 
				flng1094SubmissionsListView.getItems().add(new HBox1094SubmissionCell(null)); 
	
				for(Irs1094Submission submission : filingData.mIrs1094Submissions) 
				{ 
					if(submission == null) continue; 
					flng1094SubmissionsListView.getItems().add(new HBox1094SubmissionCell(submission)); 
				};	 
			} 
		} catch (Exception e) {
        	DataManager.i().log( Level.SEVERE, e);
		}

		// finished 
		EtcAdmin.i().setStatusMessage("Ready"); 
  		EtcAdmin.i().setProgress(0); 
	} 
	 
	private boolean validateData()  
	{ 
		boolean bReturn = true; 
 
		//if( !Utils.validate(txyrPrintOptionLabel)) bReturn = false; 
		 
		return bReturn; 
	}	 
 
	@FXML 
	private void onEdit(ActionEvent event) 
	{ 
		if(txyrEditButton.getText().equals("Edit")) 
			setEditMode(true); 
		else 
			setEditMode(false); 
	} 
 
	@FXML 
	private void onEdit1094b(ActionEvent event) 
	{ 
		if(Irs1094bEditButton.getText().equals("Edit")) 
			set1094bEditable(true); 
		else 
			set1094bEditable(false); 
	} 
 
	@FXML 
	private void onEdit1094c(ActionEvent event) 
	{ 
		if(Irs1094cEditButton.getText().equals("Edit")) {
			set1094cEditable(true);
			// set focus to the jan total emp count
			flngIrs1094TtlEmpCntJanField.requestFocus();
		}
		else 
			set1094cEditable(false); 
	} 
 
	@FXML 
	private void onSave1094b(ActionEvent event) 
	{ 
		// populate the request 
		saveIrs1094b(); 
		// update the screen 
		show1094bData(); 
		// exit edit mode 
		set1094bEditable(false); 
	} 
	 
	@FXML 
	private void onSave1094c(ActionEvent event) 
	{ 
		// update the current1094c 
		saveIrs1094c(); 
		// exit edit mode 
		set1094cEditable(false); 
		// update the screen 
		loadFilingData(); 
	} 
	 
	@FXML 
	private void onSave(ActionEvent event) 
	{ 
		// validate the information 
		if(!validateData()) 
			return; 
		// populate the request 
		saveTaxYear(); 
		// and take everything out of edit mode 
		setEditMode(false); 
	} 
	 
	@FXML 
	private void on1094cLockYearToggle(ActionEvent event) 
	{ 
		boolean state = Irs1094cLockYearCheck.isSelected();
		flngIrs1094cLckJanCheck.setSelected(state); 
		flngIrs1094cLckFebCheck.setSelected(state);
		flngIrs1094cLckMarCheck.setSelected(state); 
		flngIrs1094cLckAprCheck.setSelected(state); 
		flngIrs1094cLckMayCheck.setSelected(state); 
		flngIrs1094cLckJunCheck.setSelected(state); 
		flngIrs1094cLckJulCheck.setSelected(state); 
		flngIrs1094cLckAugCheck.setSelected(state); 
		flngIrs1094cLckSepCheck.setSelected(state); 
		flngIrs1094cLckOctCheck.setSelected(state); 
		flngIrs1094cLckNovCheck.setSelected(state); 
		flngIrs1094cLckDecCheck.setSelected(state); 
	} 
	 
	@FXML 
	private void on1094cMECYearToggle(ActionEvent event) 
	{ 
		boolean state = Irs1094cMecYearCheck.isSelected();
		flngIrs1094cMecJanCheck.setSelected(state); 
		flngIrs1094cMecFebCheck.setSelected(state);
		flngIrs1094cMecMarCheck.setSelected(state); 
		flngIrs1094cMecAprCheck.setSelected(state); 
		flngIrs1094cMecMayCheck.setSelected(state); 
		flngIrs1094cMecJunCheck.setSelected(state); 
		flngIrs1094cMecJulCheck.setSelected(state); 
		flngIrs1094cMecAugCheck.setSelected(state); 
		flngIrs1094cMecSepCheck.setSelected(state); 
		flngIrs1094cMecOctCheck.setSelected(state); 
		flngIrs1094cMecNovCheck.setSelected(state); 
		flngIrs1094cMecDecCheck.setSelected(state); 
	} 
	 
	@FXML 
	private void on1094cAggGroupYearToggle(ActionEvent event) 
	{ 
		boolean state = Irs1094cAggGroupYearCheck.isSelected();
		flngIrs1094cAgGrpJanCheck.setSelected(state); 
		flngIrs1094cAgGrpFebCheck.setSelected(state);
		flngIrs1094cAgGrpMarCheck.setSelected(state); 
		flngIrs1094cAgGrpAprCheck.setSelected(state); 
		flngIrs1094cAgGrpMayCheck.setSelected(state); 
		flngIrs1094cAgGrpJunCheck.setSelected(state); 
		flngIrs1094cAgGrpJulCheck.setSelected(state); 
		flngIrs1094cAgGrpAugCheck.setSelected(state); 
		flngIrs1094cAgGrpSepCheck.setSelected(state); 
		flngIrs1094cAgGrpOctCheck.setSelected(state); 
		flngIrs1094cAgGrpNovCheck.setSelected(state); 
		flngIrs1094cAgGrpDecCheck.setSelected(state); 
	} 
	 
	@FXML 
	private void on1095cLockYearToggle(ActionEvent event) 
	{ 
		boolean state = Irs1095cLockYearCheck.isSelected();
		Arrays.fill(lockStates1095c, BooleanUtils.toInteger(state));
		flngIrs1095cLckJanCheck.setSelected(state); 
		flngIrs1095cLckFebCheck.setSelected(state);
		flngIrs1095cLckMarCheck.setSelected(state); 
		flngIrs1095cLckAprCheck.setSelected(state); 
		flngIrs1095cLckMayCheck.setSelected(state); 
		flngIrs1095cLckJunCheck.setSelected(state); 
		flngIrs1095cLckJulCheck.setSelected(state); 
		flngIrs1095cLckAugCheck.setSelected(state); 
		flngIrs1095cLckSepCheck.setSelected(state); 
		flngIrs1095cLckOctCheck.setSelected(state); 
		flngIrs1095cLckNovCheck.setSelected(state); 
		flngIrs1095cLckDecCheck.setSelected(state); 
	} 
	 
	@FXML 
	private void on105FilingFilter(ActionEvent event) 
	{ 
		show1095Filings(); 
	} 
 
	@FXML 
	private void on105FilingClearFilter(ActionEvent event) 
	{ 
		//flng1095FilterText.setText(""); 
		show1095Filings(); 
	} 
 
	@FXML 
	private void onCalcIrs10945BC(ActionEvent event) 
	{ 
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Call Codes Request");
	    String confirmText = "Are You Sure You Want To Call Codes for this employer & tax year?";
	    confirmAlert.setContentText(confirmText);
	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();

	    if((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	return;
	    }


		if(isTypeB == true)
			createIrs1094BCalculation();
		else
			createIrs1094CCalculation();

		//Utils.showAlert("Completed", "Calling Codes Request Completed.");
	} 
	
	private void createIrs1094BCalculation()
	{
		try {
			CalcQueueData calcData = new CalcQueueData();
			calcData.createIrs1094BCalculation(DataManager.i().mLocalUser, DataManager.i().mTaxYear, false);
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		} 
	}
 
	private void createIrs1094CCalculation()
	{
		try {
			CalcQueueData calcData = new CalcQueueData();
			calcData.createIrs1094CCalculation(DataManager.i().mLocalUser, DataManager.i().mTaxYear, DataManager.i().mEmployer, null, false, null);
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		} 
	}
 
	private void createIrs1094FilingCalculation()
	{
			try {
				// check for an unlocked 1095c if not a type b
				if (isTypeB == false) {
					if (locks.getAllLocked() == false) {
						Utils.showAlert("Taxyear 1095c is not locked", "A 1095c for this TaxYear / 1094c is not locked. Filing cannot be called until all 1095cs are locked for a given 1094c.");
						return;
					}
	
				if (filingData.isTaxYearLocked() == false) {
					Utils.showAlert("Taxyear 1094c is not locked", "A 1094c for this TaxYear is not fully locked. Filing cannot be called until all 1094c months are locked for a given tax year.");
					return;
				}
				
				if (filingData.isTaxYearMECUnflagged() == false) return;
			}
			
			CalcQueueData calcData = new CalcQueueData();
			calcData.createIrs1094FilingCalculation(DataManager.i().mEmployer,DataManager.i().mLocalUser, DataManager.i().mTaxYear);
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		} 
	}
	

	@FXML 
	private void onCreateIrsErrorFile(ActionEvent event) 
	{ 
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("IRS Error File");
	    String confirmText = "Are You Sure You Want To Create an IRS Error File for this employer & tax year?";
	    confirmAlert.setContentText(confirmText);
	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();

	    if((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	return;
	    }
	    
		try {
		    // create the IrsAirCorrectionsExport
		    IrsAirCorrectionsExport acExport = new IrsAirCorrectionsExport();
		    ExportInformation ei = new ExportInformation();
		    //ei.setEmployer(DataManager.i().mEmployer);
		    ei.setEmployerId(DataManager.i().mEmployer.getId());
		    //ei.setAccount(DataManager.i().mAccount);
		    ei.setAccountId(DataManager.i().mAccount.getId());
		    acExport.setExportInformation(ei);
		    acExport.setTaxYear(DataManager.i().mTaxYear);
		    acExport.setTaxYearId(DataManager.i().mTaxYear.getId());
		    acExport.setSpecificationId(7L);
		    acExport.setCreatedBy(DataManager.i().mLocalUser);
		    acExport.setCreatedById(DataManager.i().mLocalUser.getId());
		    
			//create a new batch id
			String newBatchId = UUID.randomUUID().toString();
			acExport.setBatchId(newBatchId);
			IrsAirCorrectionsExportRequest acRequest = new IrsAirCorrectionsExportRequest();
			acRequest.setEntity(acExport);
			acExport = AdminPersistenceManager.getInstance().addOrUpdate(acRequest);
			
		    // Create the Export Request
		    ExportRequest er = new ExportRequest();
		    er.setBatchId(newBatchId);
		    er.setExportId(acExport.getId());
		    er.setSpecificationId(7L);
		   // String description = "IRSErrorFile for Acct: " + 
		   // 				   DataManager.i().mAccount.getName() + 
		   // 				   ", Empl: " + DataManager.i().mEmployer.getName() + 
		   // 				   ", ty: " + String.valueOf(DataManager.i().mTaxYear.getYear()) +
		   // 				   " requested by " + DataManager.i().mLocalUser.getFirstName() +
		   // 				   " " + DataManager.i().mLocalUser.getLastName().substring(0,1);
		    String description = "IRSErrorFile requested by " + DataManager.i().mLocalUser.getFirstName() +
 				   " " + DataManager.i().mLocalUser.getLastName().substring(0,1);
		    if (description.length() > 98)
		    	description = description.substring(0,98);
		    er.setDescription(description);
		    er.setCreatedBy(DataManager.i().mLocalUser);
		    er.setCreatedById(DataManager.i().mLocalUser.getId());
		    
		    ExportRequestRequest erReq = new ExportRequestRequest();
		    erReq.setEntity(er);
			er = AdminPersistenceManager.getInstance().addOrUpdate(erReq);

			Utils.showAlert("Irs Error File Request Created", "The Export Request for the Irs ErrorFile has been created.");
		} catch (Exception e) {
			DataManager.i().log(Level.SEVERE, e);
			Utils.showAlert("Irs Error File Request Failed", "The Request for the Irs ErrorFile failed.");
		}    
	} 
 
	@FXML
	private void onLockAllIrs1095cs() 
	{
		
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Lock All 1095cs");
	    String confirmText = "Are You Sure You Want To Fully Lock All Employee Irs1095cs For This Irs1094c?";
	    confirmAlert.setContentText(confirmText);
	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();

	    if((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	return;
	    }
	    
		try {
			//Irs1095cLocks locks = new Irs1095cLocks();
			locks.setJanLocked(true);
			locks.setFebLocked(true);
			locks.setMarLocked(true);
			locks.setAprLocked(true);
			locks.setMayLocked(true);
			locks.setJunLocked(true);
			locks.setJulLocked(true);
			locks.setAugLocked(true);
			locks.setSepLocked(true);
			locks.setOctLocked(true);
			locks.setNovLocked(true);
			locks.setDecLocked(true);
			locks.setIrs1094cId(current1094c.getId());
			Irs1095cLocksRequest req = new Irs1095cLocksRequest();
			req.setEntity(locks);
			DataManager.i().getCorvettoManager().lockAllIrs1095cs(req);
			// update the screen sicne the indicator may have changed
			show1094cData();
			// update the taxyear
			updateTaxYearStatus(AcaProcessType.LOCKED);
			Utils.showAlert("IRs1095cs Locked", "The Irs1095cs for this IRs1094c have been fully locked.");
		} catch (Exception e) {
			DataManager.i().log(Level.SEVERE, e);
			Utils.showAlert("IRs1059cs Locking Error", "There was a problem with locking the 1095cs. Please see the log for more details.");
		}
		
	}

	@FXML
	private void onMirrorLockIrs1095cs() 
	{
		
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Mirror Lock All 1095cs");
	    String confirmText = "Are You Sure You Want To Lock All Related Employee Irs1095cs The Same Way As This Irs1094c Is Locked?";
	    confirmAlert.setContentText(confirmText);
	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();

	    if((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	return;
	    }
	    
		try {
			//Irs1095cLocks locks = new Irs1095cLocks();
			locks.setJanLocked(flngIrs1094cLckJanCheck.isSelected());
			locks.setFebLocked(flngIrs1094cLckFebCheck.isSelected());
			locks.setMarLocked(flngIrs1094cLckMarCheck.isSelected());
			locks.setAprLocked(flngIrs1094cLckAprCheck.isSelected());
			locks.setMayLocked(flngIrs1094cLckMayCheck.isSelected());
			locks.setJunLocked(flngIrs1094cLckJunCheck.isSelected());
			locks.setJulLocked(flngIrs1094cLckJulCheck.isSelected());
			locks.setAugLocked(flngIrs1094cLckAugCheck.isSelected());
			locks.setSepLocked(flngIrs1094cLckSepCheck.isSelected());
			locks.setOctLocked(flngIrs1094cLckOctCheck.isSelected());
			locks.setNovLocked(flngIrs1094cLckNovCheck.isSelected());
			locks.setDecLocked(flngIrs1094cLckDecCheck.isSelected());
			locks.setIrs1094cId(current1094c.getId());
			Irs1095cLocksRequest req = new Irs1095cLocksRequest();
			req.setEntity(locks);
			DataManager.i().getCorvettoManager().lockAllIrs1095cs(req);
			// update the screen sicne the indicator may have changed
			show1094cData();
			
			Utils.showAlert("IRs1095cs Mirror Locked", "The related Irs1095cs now mirror the locking on the 1094c.");
		} catch (Exception e) {
			DataManager.i().log(Level.SEVERE, e);
			Utils.showAlert("IRs1059cs Locking Error", "There was a problem with locking the 1095cs. Please see the log for more details.");
		}
	}

	@FXML
	private void onUnlockAllIrs1095cs() 
	{
		
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Unlock All 1095cs");
	    String confirmText = "Are You Sure You Want To Fully UNLOCK All Employee Irs1095cs For This Irs1094c?";
	    confirmAlert.setContentText(confirmText);
	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();

	    if((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	return;
	    }
	    
		try {
			//Irs1095cLocks locks = new Irs1095cLocks();
			locks.setJanLocked(false);
			locks.setFebLocked(false);
			locks.setMarLocked(false);
			locks.setAprLocked(false);
			locks.setMayLocked(false);
			locks.setJunLocked(false);
			locks.setJulLocked(false);
			locks.setAugLocked(false);
			locks.setSepLocked(false);
			locks.setOctLocked(false);
			locks.setNovLocked(false);
			locks.setDecLocked(false);
			locks.setIrs1094cId(current1094c.getId());
			Irs1095cLocksRequest req = new Irs1095cLocksRequest();
			req.setEntity(locks);
			DataManager.i().getCorvettoManager().lockAllIrs1095cs(req);
			// update the screen sicne the indicator may have changed
			show1094cData();
			updateTaxYearStatus(AcaProcessType.NEW);
			Utils.showAlert("Irs1095cs UnLocked", "The Irs1095cs for this IRs1094c have been fully unlocked.");
		} catch (Exception e) {
			DataManager.i().log(Level.SEVERE, e);
			Utils.showAlert("Irs1065cs UnLocking Error", "There was a problem with unlocking the 1095cs. Please see the log for more details.");
		}	
	}

	@FXML
	private void onEdit1095cs() 
	{
		set1095cEditable(flngIrs1095cEditButton.getText().equals("Edit"));
	}
	
	@FXML
	private void onSave1095cs() 
	{
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Update 1095cs");
	    String confirmText = "Are You Sure You Want To Lock All Related Employee Irs1095cs as indicated?";
	    confirmAlert.setContentText(confirmText);
	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();

	    if((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	return;
	    }
	    
		try {
			//Irs1095cLocks locks = new Irs1095cLocks();
			//if (lockStates1095c[0] != -1)
				locks.setJanLocked(flngIrs1095cLckJanCheck.isSelected());
			//if (lockStates1095c[1] != -1)
				locks.setFebLocked(flngIrs1095cLckFebCheck.isSelected());
			//if (lockStates1095c[2] != -1)
				locks.setMarLocked(flngIrs1095cLckMarCheck.isSelected());
			//if (lockStates1095c[3] != -1)
				locks.setAprLocked(flngIrs1095cLckAprCheck.isSelected());
			//if (lockStates1095c[4] != -1)
				locks.setMayLocked(flngIrs1095cLckMayCheck.isSelected());
			//if (lockStates1095c[5] != -1)
				locks.setJunLocked(flngIrs1095cLckJunCheck.isSelected());
			//if (lockStates1095c[6] != -1)
				locks.setJulLocked(flngIrs1095cLckJulCheck.isSelected());
			//if (lockStates1095c[7] != -1)
				locks.setAugLocked(flngIrs1095cLckAugCheck.isSelected());
			//if (lockStates1095c[8] != -1)
				locks.setSepLocked(flngIrs1095cLckSepCheck.isSelected());
			//if (lockStates1095c[9] != -1)
				locks.setOctLocked(flngIrs1095cLckOctCheck.isSelected());
			//if (lockStates1095c[10] != -1)
				locks.setNovLocked(flngIrs1095cLckNovCheck.isSelected());
			//if (lockStates1095c[11] != -1)
				locks.setDecLocked(flngIrs1095cLckDecCheck.isSelected());
			locks.setIrs1094cId(current1094c.getId());
			Irs1095cLocksRequest req = new Irs1095cLocksRequest();
			req.setEntity(locks);
			DataManager.i().getCorvettoManager().lockAllIrs1095cs(req);
			// update the screen since the indicator may have changed
			show1094cData();
			
			Utils.showAlert("IRs1095cs Updated", "The Irs1095cs for this Irs1094c are now locked as selected.");
		} catch (Exception e) {
			DataManager.i().log(Level.SEVERE, e);
			Utils.showAlert("IRs1095cs Locking Error", "There was a problem with locking the 1095cs. Please see the log for more details.");
		}
		
	}
	
	@FXML
	private void onIrs1095cLockJan() 
	{
		lockStates1095c[0] = BooleanUtils.toInteger(flngIrs1095cLckJanCheck.isSelected());
		set1095cYearLock();
	}
	
	@FXML
	private void onIrs1095cLockFeb() 
	{
		lockStates1095c[1] = BooleanUtils.toInteger(flngIrs1095cLckFebCheck.isSelected());
		set1095cYearLock();
	}
	
	@FXML
	private void onIrs1095cLockMar() 
	{
		lockStates1095c[2] = BooleanUtils.toInteger(flngIrs1095cLckMarCheck.isSelected());
		set1095cYearLock();
	}
	
	@FXML
	private void onIrs1095cLockApr() 
	{
		lockStates1095c[3] = BooleanUtils.toInteger(flngIrs1095cLckAprCheck.isSelected());
		set1095cYearLock();
	}
	
	@FXML
	private void onIrs1095cLockMay() 
	{
		lockStates1095c[4] = BooleanUtils.toInteger(flngIrs1095cLckMayCheck.isSelected());
		set1095cYearLock();
	}
	
	@FXML
	private void onIrs1095cLockJun() 
	{
		lockStates1095c[5] = BooleanUtils.toInteger(flngIrs1095cLckJunCheck.isSelected());
		set1095cYearLock();
	}
	
	@FXML
	private void onIrs1095cLockJul() 
	{
		lockStates1095c[6] = BooleanUtils.toInteger(flngIrs1095cLckJulCheck.isSelected());
		set1095cYearLock();
	}
	
	@FXML
	private void onIrs1095cLockAug() 
	{
		lockStates1095c[7] = BooleanUtils.toInteger(flngIrs1095cLckAugCheck.isSelected());
		set1095cYearLock();
	}
	
	@FXML
	private void onIrs1095cLockSep() 
	{
		lockStates1095c[8] = BooleanUtils.toInteger(flngIrs1095cLckSepCheck.isSelected());
		set1095cYearLock();
	}
	
	@FXML
	private void onIrs1095cLockOct() 
	{
		lockStates1095c[9] = BooleanUtils.toInteger(flngIrs1095cLckOctCheck.isSelected());
		set1095cYearLock();
	}
	
	@FXML
	private void onIrs1095cLockNov() 
	{
		lockStates1095c[10] = BooleanUtils.toInteger(flngIrs1095cLckNovCheck.isSelected());
		set1095cYearLock();
	}
	
	@FXML
	private void onIrs1095cLockDec() 
	{
		lockStates1095c[11] = BooleanUtils.toInteger(flngIrs1095cLckDecCheck.isSelected());
		set1095cYearLock();
	}
	
	@FXML
	private void onShowSystemInfo() 
	{
		
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mTaxYear;
			DataManager.i().mCurrentCoreDataType = SystemDataType.TAXYEAR;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
			}        		
	}

	@FXML
	private void onShow1094cSystemInfo() 
	{
		
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) current1094c;
			DataManager.i().mCurrentCoreDataType = SystemDataType.IRS1094C;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
			}        		
	}

	@FXML
	private void onShow1094bSystemInfo() 
	{
		
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) current1094b;
			DataManager.i().mCurrentCoreDataType = SystemDataType.IRS1094B;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
			}        		
	}

	@FXML 
	private void onCalcIrs10945Filing(ActionEvent event) 
	{ 
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Filing Request");
	    String confirmText = "Are You Sure You Want To request filing for this employer?";
	    confirmAlert.setContentText(confirmText);
	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();

	    if((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	return;
	    }
	    
		createIrs1094FilingCalculation();

		//Utils.showAlert("Completed", "Filings Request Completed.");
	} 
	
	private void setEditMode(boolean mode) 
	{ 
		if(mode == true)  
		{ 
			txyrEditButton.setText("Cancel"); 
			txyrInputLabelGrid.setStyle("-fx-background-color: #eeffee"); 
			txyrInputLabelGrid11.setStyle("-fx-background-color: #eeffee"); 
		}else { 
			txyrEditButton.setText("Edit"); 
			txyrInputLabelGrid.getStyleClass().clear(); 
			txyrInputLabelGrid.setStyle(null);	 
			txyrInputLabelGrid11.getStyleClass().clear(); 
			txyrInputLabelGrid11.setStyle(null);	 
		} 
		 
		//Button 
		txyrSaveButton.setVisible(mode); 
		//Data 
		txyrPrintOptionLabel.setDisable(!mode); 
		txyrLiasonLabel.setEditable(mode); 
		txyrAirformTypeLabel.setDisable(!mode); 
		txyrJrLiasonLabel.setEditable(mode); 
		txyrSafeHarborCodeLabel.setDisable(!mode); 
		txyrClosedCheckBox.setDisable(!mode);; 
		txyrApprovedCheckBox.setDisable(!mode); 
		txyrClosedOnLabel.setEditable(mode); 
		txyrApprovedOnLabel.setEditable(mode); 
		txyrClosedByLabel.setEditable(mode); 
		txyrApprovedByLabel.setEditable(mode); 
		txyrSvcLvlNameLabel.setEditable(mode); 
		txyrSvcLvlDescriptionLabel.setEditable(mode); 
		txyrSvcLvlGenerateCodesCheckBox.setDisable(!mode); 
		txyrSvcLvlTrackEligibilityCheckBox.setDisable(!mode); 
		//combos
		flngIrs1094cProscessStatusCombo.setDisable(!mode);
	}	 
	 
	public class HBox1095FilingCell extends HBox 
	{ 
        Label lblId = new Label(); 
        Label lblDate = new Label(); 
        Label lblType = new Label(); 
        Label lblEmployerId = new Label(); 
        Label lblLastSubmissionDate = new Label(); 
        Label lblStatusType = new Label(); 
        Label lblFirstName = new Label(); 
        Label lblLastName = new Label(); 
        Label lblFilingType = new Label(); 
         
        Label lblFileCIsWithSSN = new Label(); 
        Label lblCorrected = new Label(); 
        Label lblAbandoned = new Label(); 
        Label lblReadyToFile = new Label(); 
        CheckBox cbFileCIsWithSSN = new CheckBox(); 
        CheckBox cbCorrected = new CheckBox(); 
        CheckBox cbAbandoned = new CheckBox(); 
        CheckBox cbReadyToFile = new CheckBox(); 
        Irs1095Filing irs1095Filing; 
         
        public Irs1095Filing get1095Filing() { 
        	return irs1095Filing; 
        } 
         
        public boolean filter(String searchVal)
        { 
        	if(searchVal == null || searchVal.isEmpty()) return true; 

        	String searchString = lblDate.getText(); 
        	searchString += lblId.getText() + " ";
        	searchString += lblType.getText() + " "; 
        	searchString += lblEmployerId.getText() + " "; 
        	searchString += lblLastSubmissionDate.getText() + " "; 
        	searchString += lblStatusType.getText() + " "; 
        	searchString += lblFirstName.getText() + " "; 
        	searchString += lblLastName.getText() + " "; 
        	searchString += lblFilingType.getText() + " "; 
        	 
        	return searchString.toUpperCase().contains(searchVal.toUpperCase()); 
        } 
 
        public boolean isError() { 
        	return lblStatusType.getText().toUpperCase().contains("ERROR"); 
        } 
 
        HBox1095FilingCell(Irs1095Filing irs1095Filing)
        { 
             super(); 
             this.irs1095Filing = irs1095Filing; 
              
             if(irs1095Filing != null)
             { 
            	 Utils.setHBoxLabel(lblId, 50, false, irs1095Filing.getIrs1095Submission().getId()); 
            	 Utils.setHBoxLabel(lblDate, 100, false, irs1095Filing.getLastUpdated()); 
            	 Utils.setHBoxLabel(lblType, 100, false, ""); 
            	 Utils.setHBoxLabel(lblFilingType, 100, false, ""); 
	           	 if(irs1095Filing.getOperationType() != null) 
	           		 Utils.setHBoxLabel(lblLastSubmissionDate, 100, false, irs1095Filing.getLastSubmitted()); 
	           	 if(irs1095Filing.getStatus() != null) 
	           		 Utils.setHBoxLabel(lblStatusType, 300, false, irs1095Filing.getStatus().toString()); 
	           	 if(irs1095Filing.getFilingType() != null) 
	           		 Utils.setHBoxLabel(lblFilingType, 100, false, irs1095Filing.getFilingType().toString()); 
				Utils.setHBoxLabel(lblEmployerId, 100, false, ""); 
				Utils.setHBoxLabel(lblFirstName, 150, false, ""); 
				Utils.setHBoxLabel(lblLastName, 150, false, ""); 
				Utils.setHBoxCheckBox(cbFileCIsWithSSN, 100, false); 
				Utils.setHBoxCheckBox(cbCorrected, 100, irs1095Filing.isCorrected()); 
				Utils.setHBoxCheckBox(cbAbandoned, 100, irs1095Filing.isAbandoned()); 
				Utils.setHBoxCheckBox(cbReadyToFile, 100, irs1095Filing.isReadyToFile()); 
				 
	           	 if(irs1095Filing.getIrs1095bId() != null) 
	           	 { 
	           		 for(Irs1095b irsb : filingData.mIrs1095bs) 
	           		 { 
	           			 if(irsb.getId().equals(irs1095Filing.getIrs1095bId()))
	           			 { 
		                      Utils.setHBoxLabel(lblType, 100, false, "1095b"); 
		                      if (DataManager.i().mEmployees != null) {
			           			  for(Employee employee : DataManager.i().mEmployees)
			           			  { 
			           				  if(employee.getId().equals(irsb.getEmployeeId())) 
			           				  { 
			           				      Utils.setHBoxLabel(lblEmployerId, 100, false, employee.getId()); 
			           					  if(employee.getPerson() != null)
			           					  { 
			           						  Utils.setHBoxLabel(lblFirstName, 150, false, employee.getPerson().getFirstName()); 
			           						  Utils.setHBoxLabel(lblLastName, 150, false, employee.getPerson().getLastName()); 
			           					  } 
			           				  } 
			           			  }
		                      }
	           			 } 
	           		 } 
	             } 
	           	  
	           	 if(irs1095Filing.getIrs1095c() != null) 
	           	 {  
//	           		 for(Irs1095c irsc : DataManager.i().mIrs1095cs) 
//	           		 { 
//	           			 if(irsc.getId().equals(irs1095Filing.getIrs1095c().getId())) 
//	           			 { 
	           		 		Irs1095c irsc = irs1095Filing.getIrs1095c(); 
	                    	Utils.setHBoxLabel(lblType, 100, false, "1095c"); 

//	           				for(Employee employee : DataManager.i().mEmployees)
//	           				{ 
//	           					if(employee.getId().equals(irsc.getEmployee().getId())) 
//	           					{ 
	                    			if(irsc.getEmployeeId() != null) 
	                    			{ 
	                    				Utils.setHBoxLabel(lblEmployerId, 100, false, irsc.getEmployeeId()); 
		           						if(irsc.getEmployee() != null && irsc.getEmployee().getPerson() != null)
		           						{ 
		           							Utils.setHBoxLabel(lblFirstName, 150, false, irsc.getEmployee().getPerson().getFirstName()); 
		           							Utils.setHBoxLabel(lblLastName, 150, false, irsc.getEmployee().getPerson().getLastName()); 
		           						} 
	                    			} 
	           					//} 
	           				//} 
	           			 //} 
	           		 } 
	           	 if(is1095b == true) 
	           		 this.getChildren().addAll(lblId, lblDate, lblType, lblFilingType, lblEmployerId, lblLastSubmissionDate, lblFirstName, lblLastName, cbCorrected, cbAbandoned, cbReadyToFile, cbFileCIsWithSSN, lblStatusType); 
	           	 else  
	           		 this.getChildren().addAll(lblId, lblDate, lblType, lblFilingType, lblEmployerId, lblLastSubmissionDate, lblFirstName, lblLastName,  cbCorrected, cbAbandoned, cbReadyToFile, lblStatusType); 
             } else { 
            	 Utils.setHBoxLabel(lblId, 50, true, "Od"); 
            	 Utils.setHBoxLabel(lblDate, 100, true, "Date"); 
            	 Utils.setHBoxLabel(lblType, 100, false, "Type"); 
            	 Utils.setHBoxLabel(lblFilingType, 100, false, "FilingType"); 
            	 Utils.setHBoxLabel(lblEmployerId, 100, true, "Employee Id"); 
            	 Utils.setHBoxLabel(lblFirstName, 150, true, "First"); 
            	 Utils.setHBoxLabel(lblLastName, 150, true, "Last"); 
            	 Utils.setHBoxLabel(lblLastSubmissionDate, 100, true, "Last Submit"); 
            	 Utils.setHBoxLabel(lblStatusType, 300, true, "Status"); 
            	 Utils.setHBoxLabel(lblFileCIsWithSSN, 100, true, "File CIs wSSN"); 
            	 Utils.setHBoxLabel(lblCorrected, 100, false, "Corrected"); 
            	 Utils.setHBoxLabel(lblAbandoned, 100, false, "Abandoned"); 
            	 Utils.setHBoxLabel(lblReadyToFile, 100, false, "ReadyToFile"); 
            	  
	           	 if(is1095b == true) 
	           		 this.getChildren().addAll(lblId, lblDate, lblType, lblFilingType, lblEmployerId, lblLastSubmissionDate, lblFirstName, lblLastName, lblCorrected, lblAbandoned, lblReadyToFile, lblFileCIsWithSSN, lblStatusType); 
	           	 else 
	           		 this.getChildren().addAll(lblId, lblDate, lblType, lblFilingType, lblEmployerId, lblLastSubmissionDate, lblFirstName, lblLastName,  lblCorrected, lblAbandoned, lblReadyToFile, lblStatusType); 
             } 
       } 
   }	 
	 
	public class HBoxCalculationNoticeCell extends HBox 
	{ 
        Label lblId = new Label(); 
        Label lblDate = new Label(); 
        Label lblDescription = new Label(); 
        CalculationNotice calcNotice; 
         
        public CalculationNotice getAirFilingEvent() { 
       	 return calcNotice; 
        } 
 
        HBoxCalculationNoticeCell(CalculationNotice calcNotice)
        { 
             super(); 
             this.calcNotice = calcNotice; 
              
             if(calcNotice != null) { 
              	  Utils.setHBoxLabel(lblId, 50, false, calcNotice.getId()); 
              	  Utils.setHBoxLabel(lblDate, 100, false, calcNotice.getLastUpdated()); 
              	  Utils.setHBoxLabel(lblDescription,  1000, false, calcNotice.getMessage()); 
             } else { 
          		  Utils.setHBoxLabel(lblId, 100, true, "Id"); 
          		  Utils.setHBoxLabel(lblDate, 100, true, "Date"); 
          		  Utils.setHBoxLabel(lblDescription,  1000, false, "Description"); 
             } 
              
             this.getChildren().addAll(lblId, lblDate, lblDescription); 
       } 
   }	 
 
	public class HBoxExportNoticeCell extends HBox 
	{ 
        Label lblId = new Label(); 
        Label lblDate = new Label(); 
        Label lblDescription = new Label(); 
        ExportNotice exportNotice; 
         
        public ExportNotice getExportNotice() { 
       	 return exportNotice; 
        } 
 
        HBoxExportNoticeCell(ExportNotice exportNotice) 
        { 
             super(); 
             this.exportNotice = exportNotice; 
              
             if(exportNotice != null)
             { 
                Utils.setHBoxLabel(lblId, 50, false, exportNotice.getId()); 
           	    Utils.setHBoxLabel(lblDate, 100, false, exportNotice.getLastUpdated()); 
           	    Utils.setHBoxLabel(lblDescription,  1500, false, exportNotice.getMessage()); 
                // color code if cllickable
                if(exportNotice.getMessage() != null && exportNotice.getMessage().contains("Irs1095Filing=") == true) 
                {
             	   lblId.setTextFill(Color.ORANGERED);
             	   lblDate.setTextFill(Color.ORANGERED);
             	   lblDescription.setTextFill(Color.ORANGERED);
                }
             } else { 
          	    Utils.setHBoxLabel(lblId, 50, true, "Id"); 
       		    Utils.setHBoxLabel(lblDate, 100, true, "Date"); 
       		    Utils.setHBoxLabel(lblDescription,  1500, false, "Description"); 
             } 
             this.getChildren().addAll(lblId, lblDate, lblDescription); 
       } 
   }	 
 
	public class HBox1094SubmissionCell extends HBox
	{ 
        Label lblId = new Label(); 
        Label lblDate = new Label(); 
        Label lblSubmissionId = new Label(); 
        Label lblGroupMember = new Label(); 
        CheckBox chkGroupMember = new CheckBox(); 
         
        Irs1094Submission irs1094Submission; 
         
        public Irs1094Submission get1094Submission() { 
       	 return irs1094Submission; 
        } 
 
        HBox1094SubmissionCell(Irs1094Submission irs1094Submission) 
        { 
             super(); 
             this.irs1094Submission = irs1094Submission; 
              
             if(irs1094Submission != null) 
             { 
            	 Utils.setHBoxLabel(lblId, 50, false, irs1094Submission.getId()); 
            	 Utils.setHBoxLabel(lblDate, 100, false, irs1094Submission.getLastUpdated()); 
            	 Utils.setHBoxLabel(lblSubmissionId, 125, false, irs1094Submission.getSubmissionId()); 
            	 Utils.setHBoxCheckBox(chkGroupMember, 150, irs1094Submission.isAggGroupMember()); 
            	 this.getChildren().addAll(lblId, lblDate, lblSubmissionId, chkGroupMember); 
             } else { 
            	 Utils.setHBoxLabel(lblId, 50, true, "Id"); 
            	 Utils.setHBoxLabel(lblDate, 100, true, "Date"); 
            	 Utils.setHBoxLabel(lblSubmissionId, 125, true, "Submission Id"); 
            	 Utils.setHBoxLabel(lblGroupMember, 150, true, "Agg. Group Member"); 
            	 this.getChildren().addAll(lblId, lblDate, lblSubmissionId, lblGroupMember); 
             } 
       } 
   }	 
	 
	public class HBox1094FilingCell extends HBox
	{ 
        Label lblId = new Label(); 
        Label lblDate = new Label(); 
        Label lblLastSubmission = new Label(); 
        Label lblAirOperationType = new Label(); 
        Label lblStatusType = new Label(); 
        Label lblType = new Label();
        Label lblState = new Label();
        Irs1094Filing irs1094Filing; 
         
        public Irs1094Filing get1094Filing() { 
       	 return irs1094Filing; 
        } 
 
        HBox1094FilingCell(Irs1094Filing irs1094Filing)
        { 
             super(); 
             this.irs1094Filing = irs1094Filing; 
              
             if(irs1094Filing != null) 
             { 
             	Utils.setHBoxLabel(lblId, 50, false, irs1094Filing.getId()); 
             	if (irs1094Filing.getInitialAcceptance() != null)
             		Utils.setHBoxLabel(lblDate, 120, false, irs1094Filing.getInitialAcceptance()); 
           	  	Utils.setHBoxLabel(lblLastSubmission, 100, false, irs1094Filing.getLastSubmitted()); 
           	  	if(irs1094Filing.getOperationType() != null) 
           	  		Utils.setHBoxLabel(lblAirOperationType, 150, false, irs1094Filing.getOperationType().toString()); 
           	  	if(irs1094Filing.getStatus() != null) 
           	  		Utils.setHBoxLabel(lblStatusType, 150, false, irs1094Filing.getStatus().toString()); 
    			if(irs1094Filing.getFilingType() != null)
           	  		Utils.setHBoxLabel(lblType, 100, false, irs1094Filing.getFilingType().toString()); 
    			if(irs1094Filing.getFilingState() != null)
           	  		Utils.setHBoxLabel(lblState, 100, false, irs1094Filing.getFilingState().toString()); 
             } else { 
        		Utils.setHBoxLabel(lblId, 50, true, "Id"); 
       		  	Utils.setHBoxLabel(lblDate, 100, true, "Init Accept"); 
       		  	Utils.setHBoxLabel(lblLastSubmission,  120, true, "Last Submission"); 
       		  	Utils.setHBoxLabel(lblAirOperationType,  150, true, "Air Operation Type"); 
       		  	Utils.setHBoxLabel(lblStatusType,  150, true, "Status Type"); 
        		Utils.setHBoxLabel(lblType, 100, true, "Type"); 
        		Utils.setHBoxLabel(lblState, 100, true, "State"); 
             } 
              
             this.getChildren().addAll(lblId, lblDate, lblLastSubmission, lblType, lblState, lblAirOperationType, lblStatusType); 
       } 
   }	 
} 
