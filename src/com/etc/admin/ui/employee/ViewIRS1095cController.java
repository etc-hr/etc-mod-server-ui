package com.etc.admin.ui.employee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.etc.CoreException;
import com.etc.admin.AdminApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.coresysteminfo.ViewCoreSystemInfoController;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.Dependent;
import com.etc.corvetto.entities.Irs1095Filing;
import com.etc.corvetto.entities.Irs1095c;
import com.etc.corvetto.entities.Irs1095cCI;
import com.etc.corvetto.rqs.DependentRequest;
import com.etc.corvetto.rqs.Irs1095FilingRequest;
import com.etc.corvetto.rqs.Irs1095cCIRequest;
import com.etc.corvetto.rqs.Irs1095cRequest;
import com.etc.corvetto.utils.types.OfferCode;
import com.etc.corvetto.utils.types.SafeHarborCode;
import com.etc.entities.CoreData;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewIRS1095cController
{
//	@FXML
//	private CheckBox selfInsuredCheck;
	@FXML
	private TableView<HBoxDependentCell> dependentList;
	@FXML
	private CheckBox activeFlag1095;
	@FXML
	private CheckBox fileCIWithSSNCheck;
	@FXML
	private CheckBox coverageJanCheck;
	@FXML
	private CheckBox coverageFebCheck;
	@FXML
	private CheckBox coverageMarCheck;
	@FXML
	private CheckBox coverageAprCheck;
	@FXML
	private CheckBox coverageMayCheck;
	@FXML
	private CheckBox coverageJunCheck;
	@FXML
	private CheckBox coverageJulCheck;
	@FXML
	private CheckBox coverageAugCheck;
	@FXML
	private CheckBox coverageSepCheck;
	@FXML
	private CheckBox coverageOctCheck;
	@FXML
	private CheckBox coverageNovCheck;
	@FXML
	private CheckBox coverageDecCheck;
	@FXML
	private CheckBox lockedJanCheck;
	@FXML
	private CheckBox lockedFebCheck;
	@FXML
	private CheckBox lockedMarCheck;
	@FXML
	private CheckBox lockedAprCheck;
	@FXML
	private CheckBox lockedMayCheck;
	@FXML
	private CheckBox lockedJunCheck;
	@FXML
	private CheckBox lockedJulCheck;
	@FXML
	private CheckBox lockedAugCheck;
	@FXML
	private CheckBox lockedSepCheck;
	@FXML
	private CheckBox lockedOctCheck;
	@FXML
	private CheckBox lockedNovCheck;
	@FXML
	private CheckBox lockedDecCheck;
	@FXML
	private ComboBox<String> offerCodeJanField;
	@FXML
	private ComboBox<String> offerCodeFebField;
	@FXML
	private ComboBox<String> offerCodeMarField;
	@FXML
	private ComboBox<String> offerCodeAprField;
	@FXML
	private ComboBox<String> offerCodeMayField;
	@FXML
	private ComboBox<String> offerCodeJunField;
	@FXML
	private ComboBox<String> offerCodeJulField;
	@FXML
	private ComboBox<String> offerCodeAugField;
	@FXML
	private ComboBox<String> offerCodeSepField;
	@FXML
	private ComboBox<String> offerCodeOctField;
	@FXML
	private ComboBox<String> offerCodeNovField;
	@FXML
	private ComboBox<String> offerCodeDecField;
	@FXML
	private TextField eeShareJanField;
	@FXML
	private TextField eeShareFebField;
	@FXML
	private TextField eeShareMarField;
	@FXML
	private TextField eeShareAprField;
	@FXML
	private TextField eeShareMayField;
	@FXML
	private TextField eeShareJunField;
	@FXML
	private TextField eeShareJulField;
	@FXML
	private TextField eeShareAugField;
	@FXML
	private TextField eeShareSepField;
	@FXML
	private TextField eeShareOctField;
	@FXML
	private TextField eeShareNovField;
	@FXML
	private TextField eeShareDecField;
	@FXML
	private ComboBox<String> shcJanField;
	@FXML
	private ComboBox<String> shcFebField;
	@FXML
	private ComboBox<String> shcMarField;
	@FXML
	private ComboBox<String> shcAprField;
	@FXML
	private ComboBox<String> shcMayField;
	@FXML
	private ComboBox<String> shcJunField;
	@FXML
	private ComboBox<String> shcJulField;
	@FXML
	private ComboBox<String> shcAugField;
	@FXML
	private ComboBox<String> shcSepField;
	@FXML
	private ComboBox<String> shcOctField;
	@FXML
	private ComboBox<String> shcNovField;
	@FXML
	private ComboBox<String> shcDecField;
	@FXML
	private Label inactiveLabel;
	@FXML
	private Label yearLabel;
	@FXML
	private Label empNamelbl;
	@FXML
	private Label emprNamelbl;
	@FXML
	private Button saveButton;
	@FXML
	private Button prevButton;
	@FXML
	private Button nextButton;
	@FXML
	private Button ocJanButton;
	@FXML
	private Button ocFebButton;
	@FXML
	private Button ocMarButton;
	@FXML
	private Button ocAprButton;
	@FXML
	private Button ocMayButton;
	@FXML
	private Button ocJunButton;
	@FXML
	private Button ocJulButton;
	@FXML
	private Button ocAugButton;
	@FXML
	private Button ocSepButton;
	@FXML
	private Button ocOctButton;
	@FXML
	private Button ocNovButton;
	@FXML
	private Button ocDecButton;
	@FXML
	private Button eeJanButton;
	@FXML
	private Button eeFebButton;
	@FXML
	private Button eeMarButton;
	@FXML
	private Button eeAprButton;
	@FXML
	private Button eeMayButton;
	@FXML
	private Button eeJunButton;
	@FXML
	private Button eeJulButton;
	@FXML
	private Button eeAugButton;
	@FXML
	private Button eeSepButton;
	@FXML
	private Button eeOctButton;
	@FXML
	private Button eeNovButton;
	@FXML
	private Button eeDecButton;
	@FXML
	private Button shJanButton;
	@FXML
	private Button shFebButton;
	@FXML
	private Button shMarButton;
	@FXML
	private Button shAprButton;
	@FXML
	private Button shMayButton;
	@FXML
	private Button shJunButton;
	@FXML
	private Button shJulButton;
	@FXML
	private Button shAugButton;
	@FXML
	private Button shSepButton;
	@FXML
	private Button shOctButton;
	@FXML
	private Button shNovButton;
	@FXML
	private Button shDecButton;
	@FXML
	private Button covJanButton;
	@FXML
	private Button covFebButton;
	@FXML
	private Button covMarButton;
	@FXML
	private Button covAprButton;
	@FXML
	private Button covMayButton;
	@FXML
	private Button covJunButton;
	@FXML
	private Button covJulButton;
	@FXML
	private Button covAugButton;
	@FXML
	private Button covSepButton;
	@FXML
	private Button covOctButton;
	@FXML
	private Button covNovButton;
	@FXML
	private Button covDecButton;
	@FXML
	private Button lockCodesButton;
	@FXML
	private Button unlockCodesButton;
	@FXML
	private Label janOffLbl;
	@FXML
	private Label febOffLbl;
	@FXML
	private Label marOffLbl;
	@FXML
	private Label aprOffLbl;
	@FXML
	private Label mayOffLbl;
	@FXML
	private Label junOffLbl;
	@FXML
	private Label julOffLbl;
	@FXML
	private Label augOffLbl;
	@FXML
	private Label sepOffLbl;
	@FXML
	private Label octOffLbl;
	@FXML
	private Label novOffLbl;
	@FXML
	private Label decOffLbl;
	@FXML
	private Label janEmpLbl;
	@FXML
	private Label febEmpLbl;
	@FXML
	private Label marEmpLbl;
	@FXML
	private Label aprEmpLbl;
	@FXML
	private Label mayEmpLbl;
	@FXML
	private Label junEmpLbl;
	@FXML
	private Label julEmpLbl;
	@FXML
	private Label augEmpLbl;
	@FXML
	private Label sepEmpLbl;
	@FXML
	private Label octEmpLbl;
	@FXML
	private Label novEmpLbl;
	@FXML
	private Label decEmpLbl;
	@FXML
	private Label janSHbLbl;
	@FXML
	private Label febSHbLbl;
	@FXML
	private Label marSHbLbl;
	@FXML
	private Label aprSHbLbl;
	@FXML
	private Label maySHbLbl;
	@FXML
	private Label junSHbLbl;
	@FXML
	private Label julSHbLbl;
	@FXML
	private Label augSHbLbl;
	@FXML
	private Label sepSHbLbl;
	@FXML
	private Label octSHbLbl;
	@FXML
	private Label novSHbLbl;
	@FXML
	private Label decSHbLbl;

	public SimpleStringProperty firstName = new SimpleStringProperty();
	public SimpleStringProperty lastName = new SimpleStringProperty();
	public SimpleStringProperty ssnNum = new SimpleStringProperty();
	public SimpleStringProperty dobNum = new SimpleStringProperty();
	public CheckBox janCheck = new CheckBox();
	public CheckBox febCheck = new CheckBox();
	public CheckBox marCheck = new CheckBox();
	public CheckBox aprCheck = new CheckBox();
	public CheckBox mayCheck = new CheckBox();
	public CheckBox junCheck = new CheckBox();
	public CheckBox julCheck = new CheckBox();
	public CheckBox augCheck = new CheckBox();
	public CheckBox sepCheck = new CheckBox();
	public CheckBox octCheck = new CheckBox();
	public CheckBox novCheck = new CheckBox();
	public CheckBox decCheck = new CheckBox();
	public SimpleStringProperty depId = new SimpleStringProperty();
	public CheckBox activeCheck = new CheckBox();

	public boolean changesMade = false;
	public List<Irs1095cCI> irs1095cCIs = null;
	Logger logr;
	int waitingToComplete = 0;
	int currentDataPosition = 0;
	private Irs1095c current1095c;

	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize()
	{
		initControls();
		setTableColumns();
		updateData();
	}
	
	private void initControls() 
	{
		currentDataPosition = 0;

		// sort our data collection
		Collections.sort(DataManager.i().mIrs1095cs, (o1, o2) -> (Integer.compare(o1.getTaxYear().getYear(), o2.getTaxYear().getYear())));

		// set it to the most recent
		currentDataPosition = DataManager.i().mIrs1095cs.size() - 1;
		saveButton.setDisable(true);
		nextButton.setDisable(true);

		// if we are at 0, there is nothing to navigate through
		if(currentDataPosition == 0)
			prevButton.setDisable(true);
		
		eeShareJanField.setOnKeyReleased(event -> { changesMade(); });
		eeShareFebField.setOnKeyReleased(event -> { changesMade(); });
		eeShareMarField.setOnKeyReleased(event -> { changesMade(); });
		eeShareAprField.setOnKeyReleased(event -> { changesMade(); });
		eeShareMayField.setOnKeyReleased(event -> { changesMade(); });
		eeShareJunField.setOnKeyReleased(event -> { changesMade(); });
		eeShareJulField.setOnKeyReleased(event -> { changesMade(); });
		eeShareAugField.setOnKeyReleased(event -> { changesMade(); });
		eeShareSepField.setOnKeyReleased(event -> { changesMade(); });
		eeShareOctField.setOnKeyReleased(event -> { changesMade(); });
		eeShareNovField.setOnKeyReleased(event -> { changesMade(); });
		eeShareDecField.setOnKeyReleased(event -> { changesMade(); });

		//ComboBox KeyEvents
		offerCodeJanField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
				offerCodeJanField.show(); 
				offerCodeJanField.setVisibleRowCount(15);
				setOfferCodeField(e, offerCodeJanField);
//            e.consume();
			changesMade();
        });

		offerCodeFebField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
				offerCodeFebField.show(); 
				offerCodeFebField.setVisibleRowCount(15);
				setOfferCodeField(e, offerCodeFebField);
//            e.consume();
			changesMade();
        });
		
		offerCodeMarField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
				offerCodeMarField.show(); 
				offerCodeMarField.setVisibleRowCount(15);
				setOfferCodeField(e, offerCodeMarField);
//            e.consume();
			changesMade();
        });
		
		offerCodeAprField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
				offerCodeAprField.show(); 
				offerCodeAprField.setVisibleRowCount(15);
				setOfferCodeField(e, offerCodeAprField);
//            e.consume();
			changesMade();
        });
		
		offerCodeMayField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
				offerCodeMayField.show(); 
				offerCodeMayField.setVisibleRowCount(15);
				setOfferCodeField(e, offerCodeMayField);
//            e.consume();
			changesMade();
        });
		
		offerCodeJunField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
				offerCodeJunField.show(); 
				offerCodeJunField.setVisibleRowCount(15);
				setOfferCodeField(e, offerCodeJunField);
//            e.consume();
			changesMade();
        });
		
		offerCodeJulField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
				offerCodeJulField.show(); 
				offerCodeJulField.setVisibleRowCount(15);
				setOfferCodeField(e, offerCodeJulField);
//            e.consume();
			changesMade();
        });
		
		offerCodeAugField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
				offerCodeAugField.show(); 
				offerCodeAugField.setVisibleRowCount(15);
				setOfferCodeField(e, offerCodeAugField);
//            e.consume();
			changesMade();
        });
		
		offerCodeSepField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
				offerCodeSepField.show(); 
				offerCodeSepField.setVisibleRowCount(15);
				setOfferCodeField(e, offerCodeSepField);
//            e.consume();
			changesMade();
        });
		
		offerCodeOctField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
				offerCodeOctField.show(); 
				offerCodeOctField.setVisibleRowCount(15);
				setOfferCodeField(e, offerCodeOctField);
//            e.consume();
			changesMade();
        });
		
		offerCodeNovField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
				offerCodeNovField.show(); 
				offerCodeNovField.setVisibleRowCount(15);
				setOfferCodeField(e, offerCodeNovField);
//            e.consume();
			changesMade();
        });
		
		offerCodeDecField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
				offerCodeDecField.show(); 
				offerCodeDecField.setVisibleRowCount(15);
				setOfferCodeField(e, offerCodeDecField);
//            e.consume();
			changesMade();
        });
		
		shcJanField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
            	shcJanField.show(); 
            	shcJanField.setVisibleRowCount(15);
            	setSafeHarborCodeField(e, shcJanField);
//            e.consume();
			changesMade();
        });
		
		shcFebField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
            	shcFebField.show(); 
            	shcFebField.setVisibleRowCount(15);
            	setSafeHarborCodeField(e, shcFebField);
//            e.consume();
			changesMade();
        });
		
		shcMarField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
            	shcMarField.show(); 
            	shcMarField.setVisibleRowCount(15);
            	setSafeHarborCodeField(e, shcMarField);
//            e.consume();
			changesMade();
        });
		
		shcAprField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
            	shcAprField.show(); 
            	shcAprField.setVisibleRowCount(15);
            	setSafeHarborCodeField(e, shcAprField);
//            e.consume();
			changesMade();
        });
		
		shcMayField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
            	shcMayField.show(); 
            	shcMayField.setVisibleRowCount(15);
            	setSafeHarborCodeField(e, shcMayField);
//            e.consume();
			changesMade();
        });
		
		shcJunField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
            	shcJunField.show(); 
            	shcJunField.setVisibleRowCount(15);
            	setSafeHarborCodeField(e, shcJunField);
//            e.consume();
			changesMade();
        });
		
		shcJulField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
            	shcJulField.show(); 
            	shcJulField.setVisibleRowCount(15);
            	setSafeHarborCodeField(e, shcJulField);
//            e.consume();
			changesMade();
        });
		
		shcAugField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
            	shcAugField.show(); 
            	shcAugField.setVisibleRowCount(15);
            	setSafeHarborCodeField(e, shcAugField);
//            e.consume();
			changesMade();
        });
		
		shcSepField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
            	shcSepField.show(); 
            	shcSepField.setVisibleRowCount(15);
            	setSafeHarborCodeField(e, shcSepField);
//            e.consume();
			changesMade();
        });
		
		shcOctField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
            	shcOctField.show(); 
            	shcOctField.setVisibleRowCount(15);
            	setSafeHarborCodeField(e, shcOctField);
//            e.consume();
			changesMade();
        });
		
		shcNovField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
            	shcNovField.show(); 
            	shcNovField.setVisibleRowCount(15);
            	setSafeHarborCodeField(e, shcNovField);
//            e.consume();
			changesMade();
        });
		
		shcDecField.addEventFilter(KeyEvent.KEY_PRESSED,  e -> 
		{ 
            if (e.getCode() == KeyCode.ENTER) 
            	System.out.println("");
            	shcDecField.show(); 
            	shcDecField.setVisibleRowCount(15);
            	setSafeHarborCodeField(e, shcDecField);
//            e.consume();
			changesMade();
        });
		
		//Offer Codes
		ObservableList<String> janOffCds = FXCollections.observableArrayList();
		janOffCds.add("");
		janOffCds.add("A");
		janOffCds.add("B");
		janOffCds.add("C");
		janOffCds.add("D");
		janOffCds.add("E");
		janOffCds.add("F");
		janOffCds.add("G");
		janOffCds.add("H");
		janOffCds.add("X");
		offerCodeJanField.setItems(janOffCds);

		ObservableList<String> febOffCds = FXCollections.observableArrayList();
		febOffCds.add("");
		febOffCds.add("A");
		febOffCds.add("B");
		febOffCds.add("C");
		febOffCds.add("D");
		febOffCds.add("E");
		febOffCds.add("F");
		febOffCds.add("G");
		febOffCds.add("H");
		febOffCds.add("X");
		offerCodeFebField.setItems(febOffCds);

		ObservableList<String> marOffCds = FXCollections.observableArrayList();
		marOffCds.add("");
		marOffCds.add("A");
		marOffCds.add("B");
		marOffCds.add("C");
		marOffCds.add("D");
		marOffCds.add("E");
		marOffCds.add("F");
		marOffCds.add("G");
		marOffCds.add("H");
		marOffCds.add("X");
		offerCodeMarField.setItems(marOffCds);

		ObservableList<String> aprOffCds = FXCollections.observableArrayList();
		aprOffCds.add("");
		aprOffCds.add("A");
		aprOffCds.add("B");
		aprOffCds.add("C");
		aprOffCds.add("D");
		aprOffCds.add("E");
		aprOffCds.add("F");
		aprOffCds.add("G");
		aprOffCds.add("H");
		aprOffCds.add("X");
		offerCodeAprField.setItems(aprOffCds);

		ObservableList<String> mayOffCds = FXCollections.observableArrayList();
		mayOffCds.add("");
		mayOffCds.add("A");
		mayOffCds.add("B");
		mayOffCds.add("C");
		mayOffCds.add("D");
		mayOffCds.add("E");
		mayOffCds.add("F");
		mayOffCds.add("G");
		mayOffCds.add("H");
		mayOffCds.add("X");
		offerCodeMayField.setItems(mayOffCds);

		ObservableList<String> junOffCds = FXCollections.observableArrayList();
		junOffCds.add("");
		junOffCds.add("A");
		junOffCds.add("B");
		junOffCds.add("C");
		junOffCds.add("D");
		junOffCds.add("E");
		junOffCds.add("F");
		junOffCds.add("G");
		junOffCds.add("H");
		junOffCds.add("X");
		offerCodeJunField.setItems(junOffCds);

		ObservableList<String> julOffCds = FXCollections.observableArrayList();
		julOffCds.add("");
		julOffCds.add("A");
		julOffCds.add("B");
		julOffCds.add("C");
		julOffCds.add("D");
		julOffCds.add("E");
		julOffCds.add("F");
		julOffCds.add("G");
		julOffCds.add("H");
		julOffCds.add("X");
		offerCodeJulField.setItems(julOffCds);

		ObservableList<String> augOffCds = FXCollections.observableArrayList();
		augOffCds.add("");
		augOffCds.add("A");
		augOffCds.add("B");
		augOffCds.add("C");
		augOffCds.add("D");
		augOffCds.add("E");
		augOffCds.add("F");
		augOffCds.add("G");
		augOffCds.add("H");
		augOffCds.add("X");
		offerCodeAugField.setItems(augOffCds);
		
		ObservableList<String> sepOffCds = FXCollections.observableArrayList();
		sepOffCds.add("");
		sepOffCds.add("A");
		sepOffCds.add("B");
		sepOffCds.add("C");
		sepOffCds.add("D");
		sepOffCds.add("E");
		sepOffCds.add("F");
		sepOffCds.add("G");
		sepOffCds.add("H");
		sepOffCds.add("X");
		offerCodeSepField.setItems(sepOffCds);

		ObservableList<String> octOffCds = FXCollections.observableArrayList();
		octOffCds.add("");
		octOffCds.add("A");
		octOffCds.add("B");
		octOffCds.add("C");
		octOffCds.add("D");
		octOffCds.add("E");
		octOffCds.add("F");
		octOffCds.add("G");
		octOffCds.add("H");
		octOffCds.add("X");
		offerCodeOctField.setItems(octOffCds);

		ObservableList<String> novOffCds = FXCollections.observableArrayList();
		novOffCds.add("");
		novOffCds.add("A");
		novOffCds.add("B");
		novOffCds.add("C");
		novOffCds.add("D");
		novOffCds.add("E");
		novOffCds.add("F");
		novOffCds.add("G");
		novOffCds.add("H");
		novOffCds.add("X");
		offerCodeNovField.setItems(novOffCds);

		ObservableList<String> decOffCds = FXCollections.observableArrayList();
		decOffCds.add("");
		decOffCds.add("A");
		decOffCds.add("B");
		decOffCds.add("C");
		decOffCds.add("D");
		decOffCds.add("E");
		decOffCds.add("F");
		decOffCds.add("G");
		decOffCds.add("H");
		decOffCds.add("X");
		offerCodeDecField.setItems(decOffCds);
		
		//Safe Harbor Codes
		ObservableList<String> janSHCds = FXCollections.observableArrayList();
		janSHCds.add("");
		janSHCds.add("A");
		janSHCds.add("B");
		janSHCds.add("C");
		janSHCds.add("D");
		janSHCds.add("E");
		janSHCds.add("F");
		janSHCds.add("G");
		janSHCds.add("H");
		janSHCds.add("I");
		janSHCds.add("J");
		janSHCds.add("K");
		janSHCds.add("L");
		janSHCds.add("M");
		janSHCds.add("N");
		janSHCds.add("O");
		janSHCds.add("P");
		janSHCds.add("Q");
		janSHCds.add("R");
		janSHCds.add("S");
		janSHCds.add("X");
		shcJanField.setItems(janSHCds);

		ObservableList<String> febSHCds = FXCollections.observableArrayList();
		febSHCds.add("");
		febSHCds.add("A");
		febSHCds.add("B");
		febSHCds.add("C");
		febSHCds.add("D");
		febSHCds.add("E");
		febSHCds.add("F");
		febSHCds.add("G");
		febSHCds.add("H");
		febSHCds.add("I");
		febSHCds.add("J");
		febSHCds.add("K");
		febSHCds.add("L");
		febSHCds.add("M");
		febSHCds.add("N");
		febSHCds.add("O");
		febSHCds.add("P");
		febSHCds.add("Q");
		febSHCds.add("R");
		febSHCds.add("S");
		febSHCds.add("X");
		shcFebField.setItems(febSHCds);

		ObservableList<String> marSHCds = FXCollections.observableArrayList();
		marSHCds.add("");
		marSHCds.add("A");
		marSHCds.add("B");
		marSHCds.add("C");
		marSHCds.add("D");
		marSHCds.add("E");
		marSHCds.add("F");
		marSHCds.add("G");
		marSHCds.add("H");
		marSHCds.add("I");
		marSHCds.add("J");
		marSHCds.add("K");
		marSHCds.add("L");
		marSHCds.add("M");
		marSHCds.add("N");
		marSHCds.add("O");
		marSHCds.add("P");
		marSHCds.add("Q");
		marSHCds.add("R");
		marSHCds.add("S");
		marSHCds.add("X");
		shcMarField.setItems(marSHCds);

		ObservableList<String> aprSHCds = FXCollections.observableArrayList();
		aprSHCds.add("");
		aprSHCds.add("A");
		aprSHCds.add("B");
		aprSHCds.add("C");
		aprSHCds.add("D");
		aprSHCds.add("E");
		aprSHCds.add("F");
		aprSHCds.add("G");
		aprSHCds.add("H");
		aprSHCds.add("I");
		aprSHCds.add("J");
		aprSHCds.add("K");
		aprSHCds.add("L");
		aprSHCds.add("M");
		aprSHCds.add("N");
		aprSHCds.add("O");
		aprSHCds.add("P");
		aprSHCds.add("Q");
		aprSHCds.add("R");
		aprSHCds.add("S");
		aprSHCds.add("X");
		shcAprField.setItems(aprSHCds);

		ObservableList<String> maySHCds = FXCollections.observableArrayList();
		maySHCds.add("");
		maySHCds.add("A");
		maySHCds.add("B");
		maySHCds.add("C");
		maySHCds.add("D");
		maySHCds.add("E");
		maySHCds.add("F");
		maySHCds.add("G");
		maySHCds.add("H");
		maySHCds.add("I");
		maySHCds.add("J");
		maySHCds.add("K");
		maySHCds.add("L");
		maySHCds.add("M");
		maySHCds.add("N");
		maySHCds.add("O");
		maySHCds.add("P");
		maySHCds.add("Q");
		maySHCds.add("R");
		maySHCds.add("S");
		maySHCds.add("X");
		shcMayField.setItems(maySHCds);

		ObservableList<String> junSHCds = FXCollections.observableArrayList();
		junSHCds.add("");
		junSHCds.add("A");
		junSHCds.add("B");
		junSHCds.add("C");
		junSHCds.add("D");
		junSHCds.add("E");
		junSHCds.add("F");
		junSHCds.add("G");
		junSHCds.add("H");
		junSHCds.add("I");
		junSHCds.add("J");
		junSHCds.add("K");
		junSHCds.add("L");
		junSHCds.add("M");
		junSHCds.add("N");
		junSHCds.add("O");
		junSHCds.add("P");
		junSHCds.add("Q");
		junSHCds.add("R");
		junSHCds.add("S");
		junSHCds.add("X");
		shcJunField.setItems(junSHCds);

		ObservableList<String> julSHCds = FXCollections.observableArrayList();
		julSHCds.add("");
		julSHCds.add("A");
		julSHCds.add("B");
		julSHCds.add("C");
		julSHCds.add("D");
		julSHCds.add("E");
		julSHCds.add("F");
		julSHCds.add("G");
		julSHCds.add("H");
		julSHCds.add("I");
		julSHCds.add("J");
		julSHCds.add("K");
		julSHCds.add("L");
		julSHCds.add("M");
		julSHCds.add("N");
		julSHCds.add("O");
		julSHCds.add("P");
		julSHCds.add("Q");
		julSHCds.add("R");
		julSHCds.add("S");
		julSHCds.add("X");
		shcJulField.setItems(julSHCds);

		ObservableList<String> augSHCds = FXCollections.observableArrayList();
		augSHCds.add("");
		augSHCds.add("A");
		augSHCds.add("B");
		augSHCds.add("C");
		augSHCds.add("D");
		augSHCds.add("E");
		augSHCds.add("F");
		augSHCds.add("G");
		augSHCds.add("H");
		augSHCds.add("I");
		augSHCds.add("J");
		augSHCds.add("K");
		augSHCds.add("L");
		augSHCds.add("M");
		augSHCds.add("N");
		augSHCds.add("O");
		augSHCds.add("P");
		augSHCds.add("Q");
		augSHCds.add("R");
		augSHCds.add("S");
		augSHCds.add("X");
		shcAugField.setItems(augSHCds);
		
		ObservableList<String> sepSHCds = FXCollections.observableArrayList();
		sepSHCds.add("");
		sepSHCds.add("A");
		sepSHCds.add("B");
		sepSHCds.add("C");
		sepSHCds.add("D");
		sepSHCds.add("E");
		sepSHCds.add("F");
		sepSHCds.add("G");
		sepSHCds.add("H");
		sepSHCds.add("I");
		sepSHCds.add("J");
		sepSHCds.add("K");
		sepSHCds.add("L");
		sepSHCds.add("M");
		sepSHCds.add("N");
		sepSHCds.add("O");
		sepSHCds.add("P");
		sepSHCds.add("Q");
		sepSHCds.add("R");
		sepSHCds.add("S");
		sepSHCds.add("X");
		shcSepField.setItems(sepSHCds);

		ObservableList<String> octSHCds = FXCollections.observableArrayList();
		octSHCds.add("");
		octSHCds.add("A");
		octSHCds.add("B");
		octSHCds.add("C");
		octSHCds.add("D");
		octSHCds.add("E");
		octSHCds.add("F");
		octSHCds.add("G");
		octSHCds.add("H");
		octSHCds.add("I");
		octSHCds.add("J");
		octSHCds.add("K");
		octSHCds.add("L");
		octSHCds.add("M");
		octSHCds.add("N");
		octSHCds.add("O");
		octSHCds.add("P");
		octSHCds.add("Q");
		octSHCds.add("R");
		octSHCds.add("S");
		octSHCds.add("X");
		shcOctField.setItems(octSHCds);

		ObservableList<String> novSHCds = FXCollections.observableArrayList();
		novSHCds.add("");
		novSHCds.add("A");
		novSHCds.add("B");
		novSHCds.add("C");
		novSHCds.add("D");
		novSHCds.add("E");
		novSHCds.add("F");
		novSHCds.add("G");
		novSHCds.add("H");
		novSHCds.add("I");
		novSHCds.add("J");
		novSHCds.add("K");
		novSHCds.add("L");
		novSHCds.add("M");
		novSHCds.add("N");
		novSHCds.add("O");
		novSHCds.add("P");
		novSHCds.add("Q");
		novSHCds.add("R");
		novSHCds.add("S");
		novSHCds.add("X");
		shcNovField.setItems(novSHCds);

		ObservableList<String> decSHCds = FXCollections.observableArrayList();
		decSHCds.add("");
		decSHCds.add("A");
		decSHCds.add("B");
		decSHCds.add("C");
		decSHCds.add("D");
		decSHCds.add("E");
		decSHCds.add("F");
		decSHCds.add("G");
		decSHCds.add("H");
		decSHCds.add("I");
		decSHCds.add("J");
		decSHCds.add("K");
		decSHCds.add("L");
		decSHCds.add("M");
		decSHCds.add("N");
		decSHCds.add("O");
		decSHCds.add("P");
		decSHCds.add("Q");
		decSHCds.add("R");
		decSHCds.add("S");
		decSHCds.add("X");
		shcDecField.setItems(decSHCds);
	}
	
	private void setOfferCodeField(KeyEvent code, ComboBox<String> value)
	{
		switch(code.getCode())
		{
			case A:
				value.setValue("A");
				break;
			case B:
				value.setValue("B");
				break;
			case C:
				value.setValue("C");
				break;
			case D:
				value.setValue("D");
				break;
			case E:
				value.setValue("E");
				break;
			case F:
				value.setValue("F");
				break;
			case G:
				value.setValue("G");
				break;
			case H:
				value.setValue("H");
				break;
			case X:
				value.setValue("X");
				break;
			default:
				break;
		}
	}
	
	private void setSafeHarborCodeField(KeyEvent code, ComboBox<String> value)
	{
		switch(code.getCode())
		{
			case A:
				value.setValue("A");
				break;
			case B:
				value.setValue("B");
				break;
			case C:
				value.setValue("C");
				break;
			case D:
				value.setValue("D");
				break;
			case E:
				value.setValue("E");
				break;
			case F:
				value.setValue("F");
				break;
			case G:
				value.setValue("G");
				break;
			case H:
				value.setValue("H");
				break;
			case I:
				value.setValue("I");
				break;
			case J:
				value.setValue("J");
				break;
			case K:
				value.setValue("K");
				break;
			case L:
				value.setValue("L");
				break;
			case M:
				value.setValue("M");
				break;
			case N:
				value.setValue("N");
				break;
			case O:
				value.setValue("O");
				break;
			case P:
				value.setValue("P");
				break;
			case Q:
				value.setValue("Q");
				break;
			case R:
				value.setValue("R");
				break;
			case S:
				value.setValue("S");
				break;
			case X:
				value.setValue("X");
				break;
			default:
				break;
		}
	}

	private void setTableColumns() 
	{
		//clear the default values
		dependentList.getColumns().clear();

	    TableColumn<HBoxDependentCell, String> x1 = new TableColumn<HBoxDependentCell, String>("First Name");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,String>("firstName"));
		x1.setMinWidth(124);
		dependentList.getColumns().add(x1);
		setDependentCellFactory(x1);

	    TableColumn<HBoxDependentCell, String> x2 = new TableColumn<HBoxDependentCell, String>("Last Name");
	    x2.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,String>("lastName"));
	    x2.setMinWidth(124);
		dependentList.getColumns().add(x2);
		setDependentCellFactory(x2);

	    TableColumn<HBoxDependentCell, String> x3 = new TableColumn<HBoxDependentCell, String>("SSN");
	    x3.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,String>("ssnNum"));
	    x3.setMinWidth(100);
		dependentList.getColumns().add(x3);
		setDependentCellFactory(x3);

	    TableColumn<HBoxDependentCell, String> x4 = new TableColumn<HBoxDependentCell, String>("DOB");
	    x4.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,String>("dobNum"));
	    x4.setMinWidth(100);
		dependentList.getColumns().add(x4);
		setDependentCellFactory(x4);

//		CheckBoxTableCell<HBoxDependentCell,Boolean> cell = new CheckBoxTableCell<HBoxDependentCell,Boolean>();
		TableColumn<HBoxDependentCell,Boolean> janCol = new TableColumn<HBoxDependentCell,Boolean>("Jan");
		janCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("janCheck"));
//		janCol.setCellFactory(new Callback<TableColumn<HBoxDependentCell,Boolean>,TableCell<HBoxDependentCell,Boolean>>() {
//		    @Override 
//		    public TableCell<HBoxDependentCell,Boolean> call( TableColumn<HBoxDependentCell,Boolean> p ) {
//		        CheckBoxTreeTableCell<HBoxDependentCell,Boolean> cell = new CheckBoxTreeTableCell<HBoxDependentCell,Boolean>();
//		        cell.setAlignment(Pos.CENTER);
//		        return cell;
//		    }
//		});
//        cell.setAlignment(Pos.CENTER);
//		janCol.setCellFactory(CheckBoxTableCell.forTableColumn(janCol));
//		janCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()));
		janCol.setMinWidth(40);
		dependentList.getColumns().add(janCol);
//		setDependentCheckCellFactory(janCol);

		TableColumn<HBoxDependentCell,Boolean> febCol = new TableColumn<HBoxDependentCell,Boolean>("Feb");
		febCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("febCheck"));
//		febCol.setCellFactory(CheckBoxTableCell.forTableColumn(febCol));
		febCol.setMinWidth(40);
		dependentList.getColumns().add(febCol);

		TableColumn<HBoxDependentCell,Boolean> marCol = new TableColumn<HBoxDependentCell,Boolean>("Mar");
		marCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("marCheck"));
//		marCol.setCellFactory(CheckBoxTableCell.forTableColumn(marCol));
		marCol.setMinWidth(40);
		dependentList.getColumns().add(marCol);

		TableColumn<HBoxDependentCell,Boolean> aprCol = new TableColumn<HBoxDependentCell,Boolean>("Apr");
		aprCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("aprCheck"));
//		aprCol.setCellFactory(CheckBoxTableCell.forTableColumn(aprCol));
		aprCol.setMinWidth(40);
		dependentList.getColumns().add(aprCol);

		TableColumn<HBoxDependentCell,Boolean> mayCol = new TableColumn<HBoxDependentCell,Boolean>("May");
		mayCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("mayCheck"));
//		mayCol.setCellFactory(CheckBoxTableCell.forTableColumn(mayCol));
		mayCol.setMinWidth(40);
		dependentList.getColumns().add(mayCol);

		TableColumn<HBoxDependentCell,Boolean> junCol = new TableColumn<HBoxDependentCell,Boolean>("Jun");
		junCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("junCheck"));
//		junCol.setCellFactory(CheckBoxTableCell.forTableColumn(junCol));
		junCol.setMinWidth(40);
		dependentList.getColumns().add(junCol);

		TableColumn<HBoxDependentCell,Boolean> julCol = new TableColumn<HBoxDependentCell,Boolean>("Jul");
		julCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("julCheck"));
//		julCol.setCellFactory(CheckBoxTableCell.forTableColumn(julCol));
		julCol.setMinWidth(40);
		dependentList.getColumns().add(julCol);

		TableColumn<HBoxDependentCell,Boolean> augCol = new TableColumn<HBoxDependentCell,Boolean>("Aug");
		augCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("augCheck"));
//		augCol.setCellFactory(CheckBoxTableCell.forTableColumn(augCol));
		augCol.setMinWidth(40);
		dependentList.getColumns().add(augCol);

		TableColumn<HBoxDependentCell,Boolean> sepCol = new TableColumn<HBoxDependentCell,Boolean>("Sep");
		sepCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("sepCheck"));
//		sepCol.setCellFactory(CheckBoxTableCell.forTableColumn(sepCol));
		sepCol.setMinWidth(40);
		dependentList.getColumns().add(sepCol);

		TableColumn<HBoxDependentCell,Boolean> octCol = new TableColumn<HBoxDependentCell,Boolean>("Oct");
		octCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("octCheck"));
//		octCol.setCellFactory(CheckBoxTableCell.forTableColumn(octCol));
		octCol.setMinWidth(40);
		dependentList.getColumns().add(octCol);

		TableColumn<HBoxDependentCell,Boolean> novCol = new TableColumn<HBoxDependentCell,Boolean>("Nov");
		novCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("novCheck"));
//		novCol.setCellFactory(CheckBoxTableCell.forTableColumn(novCol));
		novCol.setMinWidth(40);
		dependentList.getColumns().add(novCol);

		TableColumn<HBoxDependentCell,Boolean> decCol = new TableColumn<HBoxDependentCell,Boolean>("Dec");
		decCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("decCheck"));
//		decCol.setCellFactory(CheckBoxTableCell.forTableColumn(decCol));
		decCol.setMinWidth(40);
		dependentList.getColumns().add(decCol);

	    TableColumn<HBoxDependentCell, String> x5 = new TableColumn<HBoxDependentCell, String>("DEP ID");
	    x5.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,String>("depId"));
	    x5.setMinWidth(75);
		dependentList.getColumns().add(x5);
		setDependentCellFactory(x5);

		TableColumn<HBoxDependentCell,Boolean> activeCol = new TableColumn<HBoxDependentCell,Boolean>("Active");
		activeCol.setCellValueFactory(new PropertyValueFactory<HBoxDependentCell,Boolean>("activeCheck"));
//		active.setCellFactory(CheckBoxTableCell.forTableColumn(active));
		activeCol.setMinWidth(40);
		dependentList.getColumns().add(activeCol);
	}
	
	private void setDependentCellFactory(TableColumn<HBoxDependentCell, String>  col) 
	{
		col.setCellFactory(column -> 
		{
		    return new TableCell<HBoxDependentCell, String>() 
		    {
		        @Override
		        protected void updateItem(String item, boolean empty) 
		        {
		            super.updateItem(item, empty);
		            if(item == null || empty)
		            {
		                setText(null);
		                setStyle("");
		            } else {
		                setText(item);
		                HBoxDependentCell cell = getTableView().getItems().get(getIndex());

		                if(cell.getIrs1095cCI().getDependent().isDeleted() == true)
		                	setTextFill(Color.RED);
		                else {
			                if(cell.getIrs1095cCI().getDependent().isActive() == false && cell.getIrs1095cCI().getDependent().isDeleted() == false)
			                	setTextFill(Color.BLUE);
			                else
			                	setTextFill(Color.BLACK);
		                }
		            }
		        }
		    };
		});
	}

	private void updateIrs1095c() 
	{
		Irs1095c irs1095c = current1095c;

		//Irs1095c
		irs1095c.setActive(activeFlag1095.isSelected());
		if(activeFlag1095.isSelected() == false) { inactiveLabel.setVisible(true); }
		irs1095c.setFileCIWithSSN(fileCIWithSSNCheck.isSelected());

		// coverage
		irs1095c.setJanCovered(coverageJanCheck.isSelected());
		irs1095c.setFebCovered(coverageFebCheck.isSelected());
		irs1095c.setMarCovered(coverageMarCheck.isSelected());
		irs1095c.setAprCovered(coverageAprCheck.isSelected());
		irs1095c.setMayCovered(coverageMayCheck.isSelected());
		irs1095c.setJunCovered(coverageJunCheck.isSelected());
		irs1095c.setJulCovered(coverageJulCheck.isSelected());
		irs1095c.setAugCovered(coverageAugCheck.isSelected());
		irs1095c.setSepCovered(coverageSepCheck.isSelected());
		irs1095c.setOctCovered(coverageOctCheck.isSelected());
		irs1095c.setNovCovered(coverageNovCheck.isSelected());
		irs1095c.setDecCovered(coverageDecCheck.isSelected());

		// Offer Code
		if(offerCodeJanField != null && offerCodeJanField.getValue() != null && offerCodeJanField.getValue().isEmpty() == false)
			irs1095c.setJanOfferCode(OfferCode.valueOf(offerCodeJanField.getValue()));
		else
			irs1095c.setJanOfferCode(null);

		if(offerCodeFebField != null && offerCodeFebField.getValue() != null && offerCodeFebField.getValue().isEmpty() == false)
			irs1095c.setFebOfferCode(OfferCode.valueOf(offerCodeFebField.getValue()));
		else
			irs1095c.setFebOfferCode(null);

		if(offerCodeMarField != null && offerCodeMarField.getValue() != null && offerCodeMarField.getValue().isEmpty() == false)
			irs1095c.setMarOfferCode(OfferCode.valueOf(offerCodeMarField.getValue()));
		else
			irs1095c.setMarOfferCode(null);
		
		if(offerCodeAprField != null && offerCodeAprField.getValue() != null && offerCodeAprField.getValue().isEmpty() == false)
			irs1095c.setAprOfferCode(OfferCode.valueOf(offerCodeAprField.getValue()));
		else
			irs1095c.setAprOfferCode(null);
		
		if(offerCodeMayField != null && offerCodeMayField.getValue() != null && offerCodeMayField.getValue().isEmpty() == false)
			irs1095c.setMayOfferCode(OfferCode.valueOf(offerCodeMayField.getValue()));
		else
			irs1095c.setMayOfferCode(null);
		
		if(offerCodeJunField != null && offerCodeJunField.getValue() != null && offerCodeJunField.getValue().isEmpty() == false)
			irs1095c.setJunOfferCode(OfferCode.valueOf(offerCodeJunField.getValue()));
		else
			irs1095c.setJunOfferCode(null);
		
		if(offerCodeJulField != null && offerCodeJulField.getValue() != null && offerCodeJulField.getValue().isEmpty() == false)
			irs1095c.setJulOfferCode(OfferCode.valueOf(offerCodeJulField.getValue()));
		else
			irs1095c.setJulOfferCode(null);
		
		if(offerCodeAugField != null && offerCodeAugField.getValue() != null && offerCodeAugField.getValue().isEmpty() == false)
			irs1095c.setAugOfferCode(OfferCode.valueOf(offerCodeAugField.getValue()));
		else
			irs1095c.setAugOfferCode(null);
		
		if(offerCodeSepField != null && offerCodeSepField.getValue() != null && offerCodeSepField.getValue().isEmpty() == false)
			irs1095c.setSepOfferCode(OfferCode.valueOf(offerCodeSepField.getValue()));
		else
			irs1095c.setSepOfferCode(null);
		
		if(offerCodeOctField != null && offerCodeOctField.getValue() != null && offerCodeOctField.getValue().isEmpty() == false)
			irs1095c.setOctOfferCode(OfferCode.valueOf(offerCodeOctField.getValue()));
		else
			irs1095c.setOctOfferCode(null);
		
		if(offerCodeNovField != null && offerCodeNovField.getValue() != null && offerCodeNovField.getValue().isEmpty() == false)
			irs1095c.setNovOfferCode(OfferCode.valueOf(offerCodeNovField.getValue()));
		else
			irs1095c.setNovOfferCode(null);
		
		if(offerCodeDecField != null && offerCodeDecField.getValue() != null && offerCodeDecField.getValue().isEmpty() == false)
			irs1095c.setDecOfferCode(OfferCode.valueOf(offerCodeDecField.getValue()));
		else
			irs1095c.setDecOfferCode(null);

		// ee share
		if(eeShareJanField.getText() != null && eeShareJanField.getText().length() > 0)
			irs1095c.setJanEEShare(Float.valueOf(eeShareJanField.getText()));
		else
			irs1095c.setJanEEShare(null);
		
		if(eeShareFebField.getText() != null && eeShareFebField.getText().length() > 0)
			irs1095c.setFebEEShare(Float.valueOf(eeShareFebField.getText()));
		else
			irs1095c.setFebEEShare(null);
		
		if(eeShareMarField.getText() != null && eeShareMarField.getText().length() > 0)
			irs1095c.setMarEEShare(Float.valueOf(eeShareMarField.getText()));
		else
			irs1095c.setMarEEShare(null);
		
		if(eeShareAprField.getText() != null && eeShareAprField.getText().length() > 0)
			irs1095c.setAprEEShare(Float.valueOf(eeShareAprField.getText()));
		else
			irs1095c.setAprEEShare(null);
		
		if(eeShareMayField.getText() != null && eeShareMayField.getText().length() > 0)
			irs1095c.setMayEEShare(Float.valueOf(eeShareMayField.getText()));
		else
			irs1095c.setMayEEShare(null);
		
		if(eeShareJunField.getText() != null && eeShareJunField.getText().length() > 0)
			irs1095c.setJunEEShare(Float.valueOf(eeShareJunField.getText()));
		else
			irs1095c.setJunEEShare(null);
		
		if(eeShareJulField.getText() != null && eeShareJulField.getText().length() > 0)
			irs1095c.setJulEEShare(Float.valueOf(eeShareJulField.getText()));
		else
			irs1095c.setJulEEShare(null);
		
		if(eeShareAugField.getText() != null && eeShareAugField.getText().length() > 0)
			irs1095c.setAugEEShare(Float.valueOf(eeShareAugField.getText()));
		else
			irs1095c.setAugEEShare(null);
		
		if(eeShareSepField.getText() != null && eeShareSepField.getText().length() > 0)
			irs1095c.setSepEEShare(Float.valueOf(eeShareSepField.getText()));
		else
			irs1095c.setSepEEShare(null);
		
		if(eeShareOctField.getText() != null && eeShareOctField.getText().length() > 0)
			irs1095c.setOctEEShare(Float.valueOf(eeShareOctField.getText()));
		else
			irs1095c.setOctEEShare(null);
		
		if(eeShareNovField.getText() != null && eeShareNovField.getText().length() > 0)
			irs1095c.setNovEEShare(Float.valueOf(eeShareNovField.getText()));
		else
			irs1095c.setNovEEShare(null);
		
		if(eeShareDecField.getText() != null && eeShareDecField.getText().length() > 0)
			irs1095c.setDecEEShare(Float.valueOf(eeShareDecField.getText()));
		else
			irs1095c.setDecEEShare(null);
		
		// SHC
		if(shcJanField != null && shcJanField.getValue() != null && shcJanField.getValue().isEmpty() == false)
			irs1095c.setJanSafeHarborCode(SafeHarborCode.valueOf(shcJanField.getValue()));
		else
			irs1095c.setJanSafeHarborCode(null);
		
		if(shcFebField != null && shcFebField.getValue() != null && shcFebField.getValue().isEmpty() == false)
			irs1095c.setFebSafeHarborCode(SafeHarborCode.valueOf(shcFebField.getValue()));
		else
			irs1095c.setFebSafeHarborCode(null);
		
		if(shcMarField != null && shcMarField.getValue() != null && shcMarField.getValue().isEmpty() == false)
			irs1095c.setMarSafeHarborCode(SafeHarborCode.valueOf(shcMarField.getValue()));
		else
			irs1095c.setMarSafeHarborCode(null);
		
		if(shcAprField != null && shcAprField.getValue() != null && shcAprField.getValue().isEmpty() == false)
			irs1095c.setAprSafeHarborCode(SafeHarborCode.valueOf(shcAprField.getValue()));
		else
			irs1095c.setAprSafeHarborCode(null);
		
		if(shcMayField != null && shcMayField.getValue() != null && shcMayField.getValue().isEmpty() == false)
			irs1095c.setMaySafeHarborCode(SafeHarborCode.valueOf(shcMayField.getValue()));
		else
			irs1095c.setMaySafeHarborCode(null);
		
		if(shcJunField != null && shcJunField.getValue() != null && shcJunField.getValue().isEmpty() == false)
			irs1095c.setJunSafeHarborCode(SafeHarborCode.valueOf(shcJunField.getValue()));
		else
			irs1095c.setJunSafeHarborCode(null);
		
		if(shcJulField != null && shcJulField.getValue() != null && shcJulField.getValue().isEmpty() == false)
			irs1095c.setJulSafeHarborCode(SafeHarborCode.valueOf(shcJulField.getValue()));
		else
			irs1095c.setJulSafeHarborCode(null);
		
		if(shcAugField != null && shcAugField.getValue() != null && shcAugField.getValue().isEmpty() == false)
			irs1095c.setAugSafeHarborCode(SafeHarborCode.valueOf(shcAugField.getValue()));
		else
			irs1095c.setAugSafeHarborCode(null);
		
		if(shcSepField != null && shcSepField.getValue() != null && shcSepField.getValue().isEmpty() == false)
			irs1095c.setSepSafeHarborCode(SafeHarborCode.valueOf(shcSepField.getValue()));
		else
			irs1095c.setSepSafeHarborCode(null);
		
		if(shcOctField != null && shcOctField.getValue() != null && shcOctField.getValue().isEmpty() == false)
			irs1095c.setOctSafeHarborCode(SafeHarborCode.valueOf(shcOctField.getValue()));
		else
			irs1095c.setOctSafeHarborCode(null);
		
		if(shcNovField != null && shcNovField.getValue() != null && shcNovField.getValue().isEmpty() == false)
			irs1095c.setNovSafeHarborCode(SafeHarborCode.valueOf(shcNovField.getValue()));
		else
			irs1095c.setNovSafeHarborCode(null);
		
		if(shcDecField != null && shcDecField.getValue() != null && shcDecField.getValue().isEmpty() == false)
			irs1095c.setDecSafeHarborCode(SafeHarborCode.valueOf(shcDecField.getValue()));
		else
			irs1095c.setDecSafeHarborCode(null);
		
		try {

			Irs1095cRequest request = new Irs1095cRequest();
			irs1095c.setIrs1094cId(current1095c.getIrs1094cId());
			irs1095c.setIrs1094c(current1095c.getIrs1094c());
			irs1095c.setEmployee(current1095c.getEmployee());
			irs1095c.setEmployeeId(current1095c.getEmployeeId());
			irs1095c.setTaxYearId(current1095c.getTaxYearId());
			request.setId(current1095c.getId());
			request.setEntity(current1095c);

			current1095c = AdminPersistenceManager.getInstance().addOrUpdate(request);
			DataManager.i().mIrs1095cs.set(currentDataPosition, current1095c);

			updateDependent();

		} catch (CoreException e) {	
			/*DataManager.i().log(Level.SEVERE, e);*/ e.printStackTrace(); }
	    catch (Exception e) { 
	    	DataManager.i().logGenericException(e); }
	}

	private void updateData()
	{
		try
		{
			// create a thread to handle the update
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception
	            {
	            	// update 

//        			EmployeeRequest req = new EmployeeRequest();
//        			req.setId(DataManager.i().mEmployee.getId());
//        			DataManager.i().mEmployee = AdminPersistenceManager.getInstance().addOrUpdate(req);

	          		DependentRequest depRequest = new DependentRequest();
	          		depRequest.setProviderId(DataManager.i().mEmployee.getPerson().getId());
	          		DataManager.i().mEmployee.getPerson().setDependents(AdminPersistenceManager.getInstance().getAll(depRequest));
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {
      		EtcAdmin.i().setStatusMessage("updating Data...");
      		EtcAdmin.i().setProgress(0.25);});
	      	
	    	task.setOnSucceeded(e ->  show1095c());
	    	task.setOnFailed(e ->  {
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		show1095c(); 
	    	});
	    	
	    	AdminApp.getInstance().getFxQueue().put(task);
	    	
//		}catch(CoreException e) { DataManager.i().log(Level.SEVERE, e); }
		}catch (Exception e) { DataManager.i().logGenericException(e); }
	}

	private void show1095c() 
	{
		if(DataManager.i().mIrs1095cs == null || DataManager.i().mIrs1095cs.size() == 0) 
		{
			nextButton.setDisable(true);
			return;
		}
		
		current1095c = DataManager.i().mIrs1095cs.get(currentDataPosition);

		try {

			if(current1095c != null) 
			{
				inactiveLabel.setVisible(false);
	
				if(current1095c.isActive() == false)
				{
					inactiveLabel.setVisible(true);
					inactiveLabel.setText("INACTIVE");
					inactiveLabel.setTextFill(Color.BLUE);
				}
					
				if(current1095c.isDeleted() == true) 
				{
					inactiveLabel.setVisible(true);
					inactiveLabel.setText("DELETED");
					inactiveLabel.setTextFill(Color.RED);
				}
					
				// top label
				empNamelbl.setText(current1095c.getEmployee().getPerson().getLastName().toUpperCase() + ", " + current1095c.getEmployee().getPerson().getFirstName().toUpperCase());
				emprNamelbl.setText(current1095c.getEmployee().getEmployer().getName());
				yearLabel.setText(String.valueOf(current1095c.getTaxYear().getYear()));
				
	//			Utils.updateControl(selfInsuredCheck, current1095c.isSelfInsured());
				Utils.updateControl(fileCIWithSSNCheck, current1095c.isFileCIWithSSN());
				Utils.updateControl(activeFlag1095, current1095c.isActive());
	
				// coverage
				Utils.updateControl(coverageJanCheck, current1095c.isJanCovered());
				Utils.updateControl(coverageFebCheck, current1095c.isFebCovered());
				Utils.updateControl(coverageMarCheck, current1095c.isMarCovered());
				Utils.updateControl(coverageAprCheck, current1095c.isAprCovered());
				Utils.updateControl(coverageMayCheck, current1095c.isMayCovered());
				Utils.updateControl(coverageJunCheck, current1095c.isJunCovered());
				Utils.updateControl(coverageJulCheck, current1095c.isJulCovered());
				Utils.updateControl(coverageAugCheck, current1095c.isAugCovered());
				Utils.updateControl(coverageSepCheck, current1095c.isSepCovered());
				Utils.updateControl(coverageOctCheck, current1095c.isOctCovered());
				Utils.updateControl(coverageNovCheck, current1095c.isNovCovered());
				Utils.updateControl(coverageDecCheck, current1095c.isDecCovered());
	
				// locked
				Utils.updateControl(lockedJanCheck, current1095c.isJanLocked());
				janOffLbl.setText(String.valueOf(current1095c.getJanOfferCode()));
				janEmpLbl.setText(String.valueOf(current1095c.getJanEEShare()));
				janSHbLbl.setText(String.valueOf(current1095c.getJanSafeHarborCode()));

				if(current1095c.isJanLocked())
				{
					offerCodeJanField.setDisable(true); 
					eeShareJanField.setDisable(true);
					shcJanField.setDisable(true);
					coverageJanCheck.setDisable(true);
					coverageJanCheck.setStyle("-fx-opacity: 0.3");
					ocJanButton.setVisible(true);
					eeJanButton.setVisible(true);
					shJanButton.setVisible(true);
					covJanButton.setVisible(true);
					janOffLbl.setVisible(true);
					janEmpLbl.setVisible(true);
					janSHbLbl.setVisible(true);
					janCheck.setDisable(true);
				} else { 
					offerCodeJanField.setDisable(false); 
					eeShareJanField.setDisable(false);
					shcJanField.setDisable(false);
					coverageJanCheck.setDisable(false);
					coverageJanCheck.setStyle("-fx-opacity: 1");
					janOffLbl.setVisible(false);
					janEmpLbl.setVisible(false);
					janSHbLbl.setVisible(false);
					ocJanButton.setVisible(false);
					eeJanButton.setVisible(false);
					shJanButton.setVisible(false);
					covJanButton.setVisible(false);
					janCheck.setDisable(false);
				}
	
				Utils.updateControl(lockedFebCheck, current1095c.isFebLocked());
				febOffLbl.setText(String.valueOf(current1095c.getFebOfferCode()));
				febEmpLbl.setText(String.valueOf(current1095c.getFebEEShare()));
				febSHbLbl.setText(String.valueOf(current1095c.getFebSafeHarborCode()));
	
				if(current1095c.isFebLocked()) 
				{ 
					offerCodeFebField.setDisable(true); 
					eeShareFebField.setDisable(true);
					shcFebField.setDisable(true);
					coverageFebCheck.setDisable(true);
					coverageFebCheck.setStyle("-fx-opacity: 0.3");
					ocFebButton.setVisible(true);
					eeFebButton.setVisible(true);
					shFebButton.setVisible(true);
					covFebButton.setVisible(true);
					febOffLbl.setVisible(true);
					febEmpLbl.setVisible(true);
					febSHbLbl.setVisible(true);
					febCheck.setDisable(true);
				} else { 
					offerCodeFebField.setDisable(false); 
					eeShareFebField.setDisable(false);
					shcFebField.setDisable(false);
					coverageFebCheck.setDisable(false);
					coverageFebCheck.setStyle("-fx-opacity: 1");
					febOffLbl.setVisible(false);
					febEmpLbl.setVisible(false);
					febSHbLbl.setVisible(false);
					ocFebButton.setVisible(false);
					eeFebButton.setVisible(false);
					shFebButton.setVisible(false);
					covFebButton.setVisible(false);
					febCheck.setDisable(false);
				}
				
				Utils.updateControl(lockedMarCheck, current1095c.isMarLocked());
				marOffLbl.setText(String.valueOf(current1095c.getMarOfferCode()));
				marEmpLbl.setText(String.valueOf(current1095c.getMarEEShare()));
				marSHbLbl.setText(String.valueOf(current1095c.getMarSafeHarborCode()));
	
				if(current1095c.isMarLocked()) 
				{ 
					offerCodeMarField.setDisable(true); 
					eeShareMarField.setDisable(true);
					shcMarField.setDisable(true);
					coverageMarCheck.setDisable(true);
					coverageMarCheck.setStyle("-fx-opacity: 0.3");
					ocMarButton.setVisible(true);
					eeMarButton.setVisible(true);
					shMarButton.setVisible(true);
					covMarButton.setVisible(true);
					marOffLbl.setVisible(true);
					marEmpLbl.setVisible(true);
					marSHbLbl.setVisible(true);
					marCheck.setDisable(true);
				} else { 
					offerCodeMarField.setDisable(false); 
					eeShareMarField.setDisable(false);
					shcMarField.setDisable(false);
					coverageMarCheck.setDisable(false);
					coverageMarCheck.setStyle("-fx-opacity: 1");
					ocMarButton.setVisible(false);
					eeMarButton.setVisible(false);
					shMarButton.setVisible(false);
					covMarButton.setVisible(false);
					marOffLbl.setVisible(false);
					marEmpLbl.setVisible(false);
					marSHbLbl.setVisible(false);
					marCheck.setDisable(false);
				}
	
				Utils.updateControl(lockedAprCheck, current1095c.isAprLocked());
				aprOffLbl.setText(String.valueOf(current1095c.getAprOfferCode()));
				aprEmpLbl.setText(String.valueOf(current1095c.getAprEEShare()));
				aprSHbLbl.setText(String.valueOf(current1095c.getAprSafeHarborCode()));
	
				if(current1095c.isAprLocked()) 
				{
					offerCodeAprField.setDisable(true); 
					eeShareAprField.setDisable(true);
					shcAprField.setDisable(true);
					coverageAprCheck.setDisable(true);
					coverageAprCheck.setStyle("-fx-opacity: 0.3");
					ocAprButton.setVisible(true);
					eeAprButton.setVisible(true);
					shAprButton.setVisible(true);
					covAprButton.setVisible(true);
					aprOffLbl.setVisible(true);
					aprEmpLbl.setVisible(true);
					aprSHbLbl.setVisible(true);
					aprCheck.setDisable(true);
				} else { 
					offerCodeAprField.setDisable(false); 
					eeShareAprField.setDisable(false);
					shcAprField.setDisable(false);
					coverageAprCheck.setDisable(false);
					coverageAprCheck.setStyle("-fx-opacity: 1");
					ocAprButton.setVisible(false);
					eeAprButton.setVisible(false);
					shAprButton.setVisible(false);
					covAprButton.setVisible(false);
					aprOffLbl.setVisible(false);
					aprEmpLbl.setVisible(false);
					aprSHbLbl.setVisible(false);
					aprCheck.setDisable(false);
				}
				
				Utils.updateControl(lockedMayCheck, current1095c.isMayLocked());
				mayOffLbl.setText(String.valueOf(current1095c.getMayOfferCode()));
				mayEmpLbl.setText(String.valueOf(current1095c.getMayEEShare()));
				maySHbLbl.setText(String.valueOf(current1095c.getMaySafeHarborCode()));
	
				if(current1095c.isMayLocked()) 
				{ 
					offerCodeMayField.setDisable(true); 
					eeShareMayField.setDisable(true);
					shcMayField.setDisable(true);
					coverageMayCheck.setDisable(true);
					coverageMayCheck.setStyle("-fx-opacity: 0.3");
					ocMayButton.setVisible(true);
					eeMayButton.setVisible(true);
					shMayButton.setVisible(true);
					covMayButton.setVisible(true);
					mayOffLbl.setVisible(true);
					mayEmpLbl.setVisible(true);
					maySHbLbl.setVisible(true);
					mayCheck.setDisable(true);
				} else { 
					offerCodeMayField.setDisable(false); 
					eeShareMayField.setDisable(false);
					shcMayField.setDisable(false);
					coverageMayCheck.setDisable(false);
					coverageMayCheck.setStyle("-fx-opacity: 1");
					ocMayButton.setVisible(false);
					eeMayButton.setVisible(false);
					shMayButton.setVisible(false);
					covMayButton.setVisible(false);
					mayOffLbl.setVisible(false);
					mayEmpLbl.setVisible(false);
					maySHbLbl.setVisible(false);
					mayCheck.setDisable(false);
				}
	
				Utils.updateControl(lockedJunCheck, current1095c.isJunLocked());
				junOffLbl.setText(String.valueOf(current1095c.getJunOfferCode()));
				junEmpLbl.setText(String.valueOf(current1095c.getJunEEShare()));
				junSHbLbl.setText(String.valueOf(current1095c.getJunSafeHarborCode()));
	
				if(current1095c.isJunLocked()) 
				{ 
					offerCodeJunField.setDisable(true); 
					eeShareJunField.setDisable(true);
					shcJunField.setDisable(true);
					coverageJunCheck.setDisable(true);
					coverageJunCheck.setStyle("-fx-opacity: 0.3");
					ocJunButton.setVisible(true);
					eeJunButton.setVisible(true);
					shJunButton.setVisible(true);
					covJunButton.setVisible(true);
					junOffLbl.setVisible(true);
					junEmpLbl.setVisible(true);
					junSHbLbl.setVisible(true);
					junCheck.setDisable(true);
				} else { 
					offerCodeJunField.setDisable(false); 
					eeShareJunField.setDisable(false);
					shcJunField.setDisable(false);
					coverageJunCheck.setDisable(false);
					coverageJunCheck.setStyle("-fx-opacity: 1");
					ocJunButton.setVisible(false);
					eeJunButton.setVisible(false);
					shJunButton.setVisible(false);
					covJunButton.setVisible(false);
					junOffLbl.setVisible(false);
					junEmpLbl.setVisible(false);
					junSHbLbl.setVisible(false);
					junCheck.setDisable(false);
				}
	
				Utils.updateControl(lockedJulCheck, current1095c.isJulLocked());
				julOffLbl.setText(String.valueOf(current1095c.getJulOfferCode()));
				julEmpLbl.setText(String.valueOf(current1095c.getJulEEShare()));
				julSHbLbl.setText(String.valueOf(current1095c.getJulSafeHarborCode()));
	
				if(current1095c.isJulLocked()) 
				{ 
					offerCodeJulField.setDisable(true); 
					eeShareJulField.setDisable(true);
					shcJulField.setDisable(true);
					coverageJulCheck.setDisable(true);
					coverageJulCheck.setStyle("-fx-opacity: 0.3");
					ocJulButton.setVisible(true);
					eeJulButton.setVisible(true);
					shJulButton.setVisible(true);
					covJulButton.setVisible(true);
					julOffLbl.setVisible(true);
					julEmpLbl.setVisible(true);
					julSHbLbl.setVisible(true);
					julCheck.setDisable(true);
				} else { 
					offerCodeJulField.setDisable(false); 
					eeShareJulField.setDisable(false);
					shcJulField.setDisable(false);
					coverageJulCheck.setDisable(false);
					coverageJulCheck.setStyle("-fx-opacity: 1");
					ocJulButton.setVisible(false);
					eeJulButton.setVisible(false);
					shJulButton.setVisible(false);
					covJulButton.setVisible(false);
					julOffLbl.setVisible(false);
					julEmpLbl.setVisible(false);
					julSHbLbl.setVisible(false);
					julCheck.setDisable(false);
				}
	
				Utils.updateControl(lockedAugCheck, current1095c.isAugLocked());
				augOffLbl.setText(String.valueOf(current1095c.getAugOfferCode()));
				augEmpLbl.setText(String.valueOf(current1095c.getAugEEShare()));
				augSHbLbl.setText(String.valueOf(current1095c.getAugSafeHarborCode()));
	
				if(current1095c.isAugLocked()) 
				{
					offerCodeAugField.setDisable(true); 
					eeShareAugField.setDisable(true);
					shcAugField.setDisable(true);
					coverageAugCheck.setDisable(true);
					coverageAugCheck.setStyle("-fx-opacity: 0.3");
					ocAugButton.setVisible(true);
					eeAugButton.setVisible(true);
					shAugButton.setVisible(true);
					covAugButton.setVisible(true);
					augOffLbl.setVisible(true);
					augEmpLbl.setVisible(true);
					augSHbLbl.setVisible(true);
					augCheck.setDisable(true);
				} else { 
					offerCodeAugField.setDisable(false); 
					eeShareAugField.setDisable(false);
					shcAugField.setDisable(false);
					coverageAugCheck.setDisable(false);
					coverageAugCheck.setStyle("-fx-opacity: 1");
					ocAugButton.setVisible(false);
					eeAugButton.setVisible(false);
					shAugButton.setVisible(false);
					covAugButton.setVisible(false);
					augOffLbl.setVisible(false);
					augEmpLbl.setVisible(false);
					augSHbLbl.setVisible(false);
					augCheck.setDisable(false);
				}
	
				Utils.updateControl(lockedSepCheck, current1095c.isSepLocked());
				sepOffLbl.setText(String.valueOf(current1095c.getSepOfferCode()));
				sepEmpLbl.setText(String.valueOf(current1095c.getSepEEShare()));
				sepSHbLbl.setText(String.valueOf(current1095c.getSepSafeHarborCode()));
	
				if(current1095c.isSepLocked()) 
				{
					offerCodeSepField.setDisable(true); 
					eeShareSepField.setDisable(true);
					shcSepField.setDisable(true);
					coverageSepCheck.setDisable(true);
					coverageSepCheck.setStyle("-fx-opacity: 0.3");
					ocSepButton.setVisible(true);
					eeSepButton.setVisible(true);
					shSepButton.setVisible(true);
					covSepButton.setVisible(true);
					sepOffLbl.setVisible(true);
					sepEmpLbl.setVisible(true);
					sepSHbLbl.setVisible(true);
					sepCheck.setDisable(true);
				} else { 
					offerCodeSepField.setDisable(false); 
					eeShareSepField.setDisable(false);
					shcSepField.setDisable(false);
					coverageSepCheck.setDisable(false);
					coverageSepCheck.setStyle("-fx-opacity: 1");
					ocSepButton.setVisible(false);
					eeSepButton.setVisible(false);
					shSepButton.setVisible(false);
					covSepButton.setVisible(false);
					sepOffLbl.setVisible(false);
					sepEmpLbl.setVisible(false);
					sepSHbLbl.setVisible(false);
					sepCheck.setDisable(false);
				}
	
				Utils.updateControl(lockedOctCheck, current1095c.isOctLocked());
				octOffLbl.setText(String.valueOf(current1095c.getOctOfferCode()));
				octEmpLbl.setText(String.valueOf(current1095c.getOctEEShare()));
				octSHbLbl.setText(String.valueOf(current1095c.getOctSafeHarborCode()));
	
				if(current1095c.isOctLocked()) 
				{
					offerCodeOctField.setDisable(true); 
					eeShareOctField.setDisable(true);
					shcOctField.setDisable(true);
					coverageOctCheck.setDisable(true);
					coverageOctCheck.setStyle("-fx-opacity: 0.3");
					ocOctButton.setVisible(true);
					eeOctButton.setVisible(true);
					shOctButton.setVisible(true);
					covOctButton.setVisible(true);
					octOffLbl.setVisible(true);
					octEmpLbl.setVisible(true);
					octSHbLbl.setVisible(true);
					octCheck.setDisable(true);
				} else { 
					offerCodeOctField.setDisable(false); 
					eeShareOctField.setDisable(false);
					shcOctField.setDisable(false);
					coverageOctCheck.setDisable(false);
					coverageOctCheck.setStyle("-fx-opacity: 1");
					ocOctButton.setVisible(false);
					eeOctButton.setVisible(false);
					shOctButton.setVisible(false);
					covOctButton.setVisible(false);
					octOffLbl.setVisible(false);
					octEmpLbl.setVisible(false);
					octSHbLbl.setVisible(false);
					octCheck.setDisable(false);
				}
	
				Utils.updateControl(lockedNovCheck, current1095c.isNovLocked());
				novOffLbl.setText(String.valueOf(current1095c.getNovOfferCode()));
				novEmpLbl.setText(String.valueOf(current1095c.getNovEEShare()));
				novSHbLbl.setText(String.valueOf(current1095c.getNovSafeHarborCode()));
	
				if(current1095c.isNovLocked()) 
				{
					offerCodeNovField.setDisable(true); 
					eeShareNovField.setDisable(true);
					shcNovField.setDisable(true);
					coverageNovCheck.setDisable(true);
					coverageNovCheck.setStyle("-fx-opacity: 0.3");
					ocNovButton.setVisible(true);
					eeNovButton.setVisible(true);
					shNovButton.setVisible(true);
					covNovButton.setVisible(true);
					novOffLbl.setVisible(true);
					novEmpLbl.setVisible(true);
					novSHbLbl.setVisible(true);
					novCheck.setDisable(true);
				} else { 
					offerCodeNovField.setDisable(false); 
					eeShareNovField.setDisable(false);
					shcNovField.setDisable(false);
					coverageNovCheck.setDisable(false);
					coverageNovCheck.setStyle("-fx-opacity: 1");
					ocNovButton.setVisible(false);
					eeNovButton.setVisible(false);
					shNovButton.setVisible(false);
					covNovButton.setVisible(false);
					novOffLbl.setVisible(false);
					novEmpLbl.setVisible(false);
					novSHbLbl.setVisible(false);
					novCheck.setDisable(false);
				}
	
				Utils.updateControl(lockedDecCheck, current1095c.isDecLocked());
				decOffLbl.setText(String.valueOf(current1095c.getDecOfferCode()));
				decEmpLbl.setText(String.valueOf(current1095c.getDecEEShare()));
				decSHbLbl.setText(String.valueOf(current1095c.getDecSafeHarborCode()));

				if(current1095c.isDecLocked()) 
				{
					offerCodeDecField.setDisable(true); 
					eeShareDecField.setDisable(true);
					shcDecField.setDisable(true);
					coverageDecCheck.setDisable(true);
					coverageDecCheck.setStyle("-fx-opacity: 0.3");
					ocDecButton.setVisible(true);
					eeDecButton.setVisible(true);
					shDecButton.setVisible(true);
					covDecButton.setVisible(true);
					decOffLbl.setVisible(true);
					decEmpLbl.setVisible(true);
					decSHbLbl.setVisible(true);
					decCheck.setDisable(true);
				} else { 
					offerCodeDecField.setDisable(false); 
					eeShareDecField.setDisable(false);
					shcDecField.setDisable(false);
					coverageDecCheck.setDisable(false);
					coverageDecCheck.setStyle("-fx-opacity: 1");
					ocDecButton.setVisible(false);
					eeDecButton.setVisible(false);
					shDecButton.setVisible(false);
					covDecButton.setVisible(false);
					decOffLbl.setVisible(false);
					decEmpLbl.setVisible(false);
					decSHbLbl.setVisible(false);
					decCheck.setDisable(false);
				}

				// offer code
				if(offerCodeJanField != null && current1095c.getJanOfferCode() != null) 
				{ 
					offerCodeJanField.setValue(String.valueOf(current1095c.getJanOfferCode())); 
				} else { 
					offerCodeJanField.setValue("");
					janOffLbl.setText("");
					
				}

				if(offerCodeFebField != null && current1095c.getFebOfferCode() != null) 
				{
					offerCodeFebField.setValue(String.valueOf(current1095c.getFebOfferCode().toString())); 
				} else {
					offerCodeFebField.setValue("");
					febOffLbl.setText("");
				}

				if(offerCodeMarField != null && current1095c.getMarOfferCode() != null) 
				{ 
					offerCodeMarField.setValue(String.valueOf(current1095c.getMarOfferCode().toString())); 
				} else {
					offerCodeMarField.setValue("");
					marOffLbl.setText("");
				}

				if(offerCodeAprField != null && current1095c.getAprOfferCode() != null)
				{
					offerCodeAprField.setValue(String.valueOf(current1095c.getAprOfferCode().toString())); 
				} else {
					offerCodeAprField.setValue("");
					aprOffLbl.setText("");
				}

				if(offerCodeMayField != null && current1095c.getMayOfferCode() != null) 
				{
					offerCodeMayField.setValue(String.valueOf(current1095c.getMayOfferCode().toString())); 
				} else {
					offerCodeMayField.setValue("");
					mayOffLbl.setText("");
				}

				if(offerCodeJunField != null && current1095c.getJunOfferCode() != null) 
				{
					offerCodeJunField.setValue(String.valueOf(current1095c.getJunOfferCode().toString())); 
				} else {
					offerCodeJunField.setValue("");
					junOffLbl.setText("");
				}

				if(offerCodeJulField != null && current1095c.getJulOfferCode() != null) 
				{
					offerCodeJulField.setValue(String.valueOf(current1095c.getJulOfferCode().toString())); 
				} else {
					offerCodeJulField.setValue("");
					julOffLbl.setText("");
				}

				if(offerCodeAugField != null && current1095c.getAugOfferCode() != null)
				{
					offerCodeAugField.setValue(String.valueOf(current1095c.getAugOfferCode().toString())); 
				} else {
					offerCodeAugField.setValue("");
					augOffLbl.setText("");
				}

				if(offerCodeSepField != null && current1095c.getSepOfferCode() != null)
				{
					offerCodeSepField.setValue(String.valueOf(current1095c.getSepOfferCode().toString())); 
				} else {
					offerCodeSepField.setValue("");
					sepOffLbl.setText("");
				}

				if(offerCodeOctField != null && current1095c.getOctOfferCode() != null) 
				{
					offerCodeOctField.setValue(String.valueOf(current1095c.getOctOfferCode().toString())); 
				} else {
					offerCodeOctField.setValue("");
					octOffLbl.setText("");
				}

				if(offerCodeNovField != null && current1095c.getNovOfferCode() != null) 
				{
					offerCodeNovField.setValue(String.valueOf(current1095c.getNovOfferCode().toString()));
				} else {
					offerCodeNovField.setValue("");
					novOffLbl.setText("");
				}

				if(offerCodeDecField != null && current1095c.getDecOfferCode() != null)
				{
					offerCodeDecField.setValue(String.valueOf(current1095c.getDecOfferCode().toString())); 
				} else {
					offerCodeDecField.setValue("");
					decOffLbl.setText("");
				}
	
				// ee share
				if(eeShareJanField != null && current1095c.getJanEEShare() != null) 
				{
					eeShareJanField.setText(String.valueOf(current1095c.getJanEEShare()));
				} else {
					eeShareJanField.setText("");
					janEmpLbl.setText("");
				}

				if(eeShareFebField != null && current1095c.getFebEEShare() != null) 
				{
					eeShareFebField.setText(String.valueOf(current1095c.getFebEEShare()));
				} else {
					eeShareFebField.setText("");
					febEmpLbl.setText("");
				}

				if(eeShareMarField != null && current1095c.getMarEEShare() != null) 
				{
					eeShareMarField.setText(String.valueOf(current1095c.getMarEEShare()));
				} else {
					eeShareMarField.setText("");
					marEmpLbl.setText("");
				}

				if(eeShareAprField != null && current1095c.getAprEEShare() != null) 
				{
					eeShareAprField.setText(String.valueOf(current1095c.getAprEEShare()));
				} else {
					eeShareAprField.setText("");
					aprEmpLbl.setText("");
				}

				if(eeShareMayField != null && current1095c.getMayEEShare() != null) 
				{
					eeShareMayField.setText(String.valueOf(current1095c.getMayEEShare()));
				} else {
					eeShareMayField.setText("");
					mayEmpLbl.setText("");
				}

				if(eeShareJunField != null && current1095c.getJunEEShare() != null) 
				{
					eeShareJunField.setText(String.valueOf(current1095c.getJunEEShare()));
				} else {
					eeShareJunField.setText("");
					junEmpLbl.setText("");
				}

				if(eeShareJulField != null && current1095c.getJulEEShare() != null) 
				{
					eeShareJulField.setText(String.valueOf(current1095c.getJulEEShare()));
				} else {
					eeShareJulField.setText("");
					julEmpLbl.setText("");
				}

				if(eeShareAugField != null && current1095c.getAugEEShare() != null) 
				{
					eeShareAugField.setText(String.valueOf(current1095c.getAugEEShare()));
				} else {
					eeShareAugField.setText("");
					augEmpLbl.setText("");
				}

				if(eeShareSepField != null && current1095c.getSepEEShare() != null) 
				{
					eeShareSepField.setText(String.valueOf(current1095c.getSepEEShare()));
				} else {
					eeShareSepField.setText("");
					sepEmpLbl.setText("");
				}

				if(eeShareOctField != null && current1095c.getOctEEShare() != null) 
				{
					eeShareOctField.setText(String.valueOf(current1095c.getOctEEShare()));
				} else {
					eeShareOctField.setText("");
					octEmpLbl.setText("");
				}

				if(eeShareNovField != null && current1095c.getNovEEShare() != null) 
				{
					eeShareNovField.setText(String.valueOf(current1095c.getNovEEShare()));
				} else {
					eeShareNovField.setText("");
					novEmpLbl.setText("");
				}

				if(eeShareDecField != null && current1095c.getDecEEShare() != null) 
				{
					eeShareDecField.setText(String.valueOf(current1095c.getDecEEShare()));
				} else {
					eeShareDecField.setText("");
					decEmpLbl.setText("");
				}

				// SHC
				if(shcJanField != null && current1095c.getJanSafeHarborCode() != null) 
				{
					shcJanField.setValue(String.valueOf(current1095c.getJanSafeHarborCode())); 
				} else {
					shcJanField.setValue("");
					janSHbLbl.setText("");
				}

				if(shcFebField != null && current1095c.getFebSafeHarborCode() != null) 
				{
					shcFebField.setValue(String.valueOf(current1095c.getFebSafeHarborCode())); 
				} else {
					shcFebField.setValue("");
					febSHbLbl.setText("");
				}

				if(shcMarField != null && current1095c.getMarSafeHarborCode() != null) 
				{
					shcMarField.setValue(String.valueOf(current1095c.getMarSafeHarborCode())); 
				} else {
					shcMarField.setValue("");
					marSHbLbl.setText("");
				}

				if(shcAprField != null && current1095c.getAprSafeHarborCode() != null) 
				{
					shcAprField.setValue(String.valueOf(current1095c.getAprSafeHarborCode())); 
				} else {
					shcAprField.setValue("");
					aprSHbLbl.setText("");
				}

				if(shcMayField != null && current1095c.getMaySafeHarborCode() != null) 
				{
					shcMayField.setValue(String.valueOf(current1095c.getMaySafeHarborCode())); 
				} else {
					shcMayField.setValue("");
					maySHbLbl.setText("");
				}

				if(shcJunField != null && current1095c.getJunSafeHarborCode() != null) 
				{
					shcJunField.setValue(String.valueOf(current1095c.getJunSafeHarborCode())); 
				} else {
					shcJunField.setValue("");
					junSHbLbl.setText("");
				}

				if(shcJulField != null && current1095c.getJulSafeHarborCode() != null) 
				{
					shcJulField.setValue(String.valueOf(current1095c.getJulSafeHarborCode())); 
				} else {
					shcJulField.setValue("");
					julSHbLbl.setText("");
				}

				if(shcAugField != null && current1095c.getAugSafeHarborCode() != null) 
				{
					shcAugField.setValue(String.valueOf(current1095c.getAugSafeHarborCode())); 
				} else {
					shcAugField.setValue("");
					augSHbLbl.setText("");
				}

				if(shcSepField != null && current1095c.getSepSafeHarborCode() != null) 
				{
					shcSepField.setValue(String.valueOf(current1095c.getSepSafeHarborCode())); 
				} else {
					shcSepField.setValue("");
					sepSHbLbl.setText("");
				}

				if(shcOctField != null && current1095c.getOctSafeHarborCode() != null) 
				{
					shcOctField.setValue(String.valueOf(current1095c.getOctSafeHarborCode())); 
				} else {
					shcOctField.setValue("");
					octSHbLbl.setText("");
				}

				if(shcNovField != null && current1095c.getNovSafeHarborCode() != null) 
				{
					shcNovField.setValue(String.valueOf(current1095c.getNovSafeHarborCode())); 
				} else {
					shcNovField.setValue("");
					novSHbLbl.setText("");
				}

				if(shcDecField != null && current1095c.getDecSafeHarborCode() != null)
				{
					shcDecField.setValue(String.valueOf(current1095c.getDecSafeHarborCode())); 
				} else {
					shcDecField.setValue("");
					decSHbLbl.setText("");
				}
				showDependents();
			}
//		} catch (CoreException e) {	DataManager.i().log(Level.SEVERE, e); }
		}catch (Exception e) { 
			DataManager.i().logGenericException(e); 
		}
	}

	private void showDependents() 
	{
		try {
			// clear anything already in the list
			dependentList.getItems().clear();

			//if we have no employees, bail
			if(DataManager.i().mEmployee.getPerson() == null) return;
			if(DataManager.i().mEmployee.getPerson() == null || DataManager.i().mEmployee.getPerson().getDependents() == null) return;
			List<HBoxDependentCell> list = new ArrayList<>();
			
			for(Dependent dep : DataManager.i().mEmployee.getPerson().getDependents()) 
			{
		    	if(dep.isActive() == false) continue;
		    	if(dep.isDeleted() == true) continue;

		    	Irs1095cCIRequest irsReq = new Irs1095cCIRequest();
				irsReq.setDependentId(dep.getId());
				List<Irs1095cCI> irs1095cCIs = AdminPersistenceManager.getInstance().getAll(irsReq);

				for(Irs1095cCI ci : irs1095cCIs) 
				{
					if(ci.getIrs1095c() != null && ci.getIrs1095c().getId().equals(current1095c.getId()))
					{
						list.add(new HBoxDependentCell(ci, dep));
					}
				}
			}
	        ObservableList<HBoxDependentCell> myObservableList = FXCollections.observableList(list);
	        dependentList.setItems(myObservableList);

		} catch (CoreException e) {
			DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { 
	    	DataManager.i().logGenericException(e);
	    }

		//redraw the control
        dependentList.refresh();
		EtcAdmin.i().setProgress(0); 
	}

	private boolean changesMade()
	{
		try {
				
			//Offer Code ComboBoxes
			if(offerCodeJanField != null && current1095c.getJanOfferCode() != null)
			{
				if(!offerCodeJanField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getJanOfferCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(offerCodeFebField.getValue() != null && current1095c.getFebOfferCode() != null)
			{
				if(!offerCodeFebField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getFebOfferCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(offerCodeMarField.getValue() != null && current1095c.getMarOfferCode() != null)
			{
				if(!offerCodeMarField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getMarOfferCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(offerCodeAprField.getValue() != null && current1095c.getAprOfferCode() != null)
			{
				if(!offerCodeAprField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getAprOfferCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(offerCodeMayField.getValue() != null && current1095c.getMayOfferCode() != null)
			{
				if(!offerCodeMayField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getMayOfferCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(offerCodeJunField.getValue() != null && current1095c.getJunOfferCode() != null)
			{
				if(!offerCodeJunField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getJunOfferCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(offerCodeJulField.getValue() != null && current1095c.getJulOfferCode() != null)
			{
				if(!offerCodeJulField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getJulOfferCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(offerCodeAugField.getValue() != null && current1095c.getAugOfferCode() != null)
			{
				if(!offerCodeAugField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getAugOfferCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(offerCodeSepField.getValue() != null && current1095c.getSepOfferCode() != null)
			{
				if(!offerCodeSepField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getSepOfferCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(offerCodeOctField.getValue() != null && current1095c.getOctOfferCode() != null)
			{
				if(!offerCodeOctField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getOctOfferCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(offerCodeNovField.getValue() != null && current1095c.getNovOfferCode() != null)
			{
				if(!offerCodeNovField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getNovOfferCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(offerCodeDecField.getValue() != null && current1095c.getDecOfferCode() != null)
			{
				if(!offerCodeDecField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getDecOfferCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			
			//Emp Share TextFields
			if(eeShareJanField.getText() != null)
			{
				/*if(!eeShareJanField.getText().toString().equals(current1095c.getJanEEShare().toString())) {*/ changesMade = true; saveButton.setDisable(false); }
//			}
			if(eeShareFebField.getText() != null)
			{
				changesMade = true; saveButton.setDisable(false);
			}
			if(eeShareMarField.getText() != null)
			{
				changesMade = true; saveButton.setDisable(false);
			}
			if(eeShareAprField.getText() != null)
			{
				changesMade = true; saveButton.setDisable(false);
			}
			if(eeShareMayField.getText() != null)
			{
				changesMade = true; saveButton.setDisable(false);
			}
			if(eeShareJunField.getText() != null)
			{
				changesMade = true; saveButton.setDisable(false);
			}
			if(eeShareJulField.getText() != null)
			{
				changesMade = true; saveButton.setDisable(false);
			}
			if(eeShareAugField.getText() != null)
			{
				changesMade = true; saveButton.setDisable(false);
			}
			if(eeShareSepField.getText() != null)
			{
				changesMade = true; saveButton.setDisable(false);
			}
			if(eeShareOctField.getText() != null)
			{
				changesMade = true; saveButton.setDisable(false);
			}
			if(eeShareNovField.getText() != null)
			{
				changesMade = true; saveButton.setDisable(false);
			}
			if(eeShareDecField.getText() != null)
			{
				changesMade = true; saveButton.setDisable(false);
			}
	
			//SafeHarbor ComboBoxes
			if(shcJanField.getValue() != null && current1095c.getJanSafeHarborCode() != null)
			{
				if(!shcJanField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getJanSafeHarborCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(shcFebField.getValue() != null && current1095c.getFebSafeHarborCode() != null)
			{
				if(!shcFebField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getFebSafeHarborCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(shcMarField.getValue() != null && current1095c.getMarSafeHarborCode() != null)
			{
				if(!shcMarField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getMarSafeHarborCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(shcAprField.getValue() != null && current1095c.getAprSafeHarborCode() != null)
			{
				if(!shcAprField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getAprSafeHarborCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(shcMayField.getValue() != null && current1095c.getMaySafeHarborCode() != null)
			{
				if(!shcMayField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getMaySafeHarborCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(shcJunField.getValue() != null && current1095c.getJunSafeHarborCode() != null)
			{
				if(!shcJunField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getJunSafeHarborCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(shcJulField.getValue() != null && current1095c.getJulSafeHarborCode() != null)
			{
				if(!shcJulField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getJulSafeHarborCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(shcAugField.getValue() != null && current1095c.getAugSafeHarborCode() != null)
			{
				if(!shcAugField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getAugSafeHarborCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(shcSepField.getValue() != null && current1095c.getSepSafeHarborCode() != null)
			{
				if(!shcSepField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getSepSafeHarborCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(shcOctField.getValue() != null && current1095c.getOctSafeHarborCode() != null)
			{
				if(!shcOctField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getOctSafeHarborCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(shcNovField.getValue() != null && current1095c.getNovSafeHarborCode() != null)
			{
				if(!shcNovField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getNovSafeHarborCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			if(shcDecField.getValue() != null && current1095c.getDecSafeHarborCode() != null)
			{
				if(!shcDecField.getSelectionModel().getSelectedItem().toString().equals(current1095c.getDecSafeHarborCode().toString())) { changesMade = true; saveButton.setDisable(false); }
			}
			
			//Covered Individual CheckBoxes
			if(!coverageJanCheck.isSelected() == current1095c.isJanCovered()) { changesMade = true; saveButton.setDisable(false); }
			if(!coverageFebCheck.isSelected() == current1095c.isFebCovered()) { changesMade = true; saveButton.setDisable(false); }
			if(!coverageMarCheck.isSelected() == current1095c.isMarCovered()) { changesMade = true; saveButton.setDisable(false); }
			if(!coverageAprCheck.isSelected() == current1095c.isAprCovered()) { changesMade = true; saveButton.setDisable(false); }
			if(!coverageMayCheck.isSelected() == current1095c.isMayCovered()) { changesMade = true; saveButton.setDisable(false); }
			if(!coverageJunCheck.isSelected() == current1095c.isJunCovered()) { changesMade = true; saveButton.setDisable(false); }
			if(!coverageJulCheck.isSelected() == current1095c.isJulCovered()) { changesMade = true; saveButton.setDisable(false); }
			if(!coverageAugCheck.isSelected() == current1095c.isAugCovered()) { changesMade = true; saveButton.setDisable(false); }
			if(!coverageSepCheck.isSelected() == current1095c.isSepCovered()) { changesMade = true; saveButton.setDisable(false); }
			if(!coverageOctCheck.isSelected() == current1095c.isOctCovered()) { changesMade = true; saveButton.setDisable(false); }
			if(!coverageNovCheck.isSelected() == current1095c.isNovCovered()) { changesMade = true; saveButton.setDisable(false); }
			if(!coverageDecCheck.isSelected() == current1095c.isDecCovered()) { changesMade = true; saveButton.setDisable(false); }
	
			//Locked CheckBoxes
			if(!lockedJanCheck.isSelected() == current1095c.isJanLocked()) { changesMade = true; saveButton.setDisable(false); }
			if(!lockedFebCheck.isSelected() == current1095c.isFebLocked()) { changesMade = true; saveButton.setDisable(false); }
			if(!lockedMarCheck.isSelected() == current1095c.isMarLocked()) { changesMade = true; saveButton.setDisable(false); }
			if(!lockedAprCheck.isSelected() == current1095c.isAprLocked()) { changesMade = true; saveButton.setDisable(false); }
			if(!lockedMayCheck.isSelected() == current1095c.isMayLocked()) { changesMade = true; saveButton.setDisable(false); }
			if(!lockedJunCheck.isSelected() == current1095c.isJunLocked()) { changesMade = true; saveButton.setDisable(false); }
			if(!lockedJulCheck.isSelected() == current1095c.isJulLocked()) { changesMade = true; saveButton.setDisable(false); }
			if(!lockedAugCheck.isSelected() == current1095c.isAugLocked()) { changesMade = true; saveButton.setDisable(false); }
			if(!lockedSepCheck.isSelected() == current1095c.isSepLocked()) { changesMade = true; saveButton.setDisable(false); }
			if(!lockedOctCheck.isSelected() == current1095c.isOctLocked()) { changesMade = true; saveButton.setDisable(false); }
			if(!lockedNovCheck.isSelected() == current1095c.isNovLocked()) { changesMade = true; saveButton.setDisable(false); }
			if(!lockedDecCheck.isSelected() == current1095c.isDecLocked()) { changesMade = true; saveButton.setDisable(false); }
	
			//Other CheckBoxes
			if(!fileCIWithSSNCheck.isSelected() == current1095c.isFileCIWithSSN()) { changesMade = true; saveButton.setDisable(false); }
			if(!activeFlag1095.isSelected() == current1095c.isActive()) { changesMade = true; saveButton.setDisable(false); }

			if(current1095c.getIrs1095cCIs() != null && current1095c.getIrs1095cCIs().size() > 0)
			{
				for(Irs1095cCI irs1095cCI : current1095c.getIrs1095cCIs())
				{
					if(!janCheck.isSelected() == irs1095cCI.isJanCovered()) { changesMade = true; saveButton.setDisable(false); }
					if(!febCheck.isSelected() == irs1095cCI.isFebCovered()) { changesMade = true; saveButton.setDisable(false); }
					if(!marCheck.isSelected() == irs1095cCI.isMarCovered()) { changesMade = true; saveButton.setDisable(false); }
					if(!aprCheck.isSelected() == irs1095cCI.isAprCovered()) { changesMade = true; saveButton.setDisable(false); }
					if(!mayCheck.isSelected() == irs1095cCI.isMayCovered()) { changesMade = true; saveButton.setDisable(false); }
					if(!junCheck.isSelected() == irs1095cCI.isJunCovered()) { changesMade = true; saveButton.setDisable(false); }
					if(!julCheck.isSelected() == irs1095cCI.isJulCovered()) { changesMade = true; saveButton.setDisable(false); }
					if(!augCheck.isSelected() == irs1095cCI.isAugCovered()) { changesMade = true; saveButton.setDisable(false); }
					if(!sepCheck.isSelected() == irs1095cCI.isSepCovered()) { changesMade = true; saveButton.setDisable(false); }
					if(!octCheck.isSelected() == irs1095cCI.isOctCovered()) { changesMade = true; saveButton.setDisable(false); }
					if(!novCheck.isSelected() == irs1095cCI.isNovCovered()) { changesMade = true; saveButton.setDisable(false); }
					if(!decCheck.isSelected() == irs1095cCI.isDecCovered()) { changesMade = true; saveButton.setDisable(false); }
				}
			}

//		} catch (CoreException e) {	/*DataManager.i().log(Level.SEVERE, e);*/ e.printStackTrace(); }
		}catch (Exception e) { 
			DataManager.i().logGenericException(e); 
		}
		return changesMade;
	}

	private boolean validateFields() {
		return true;
	}

	@FXML
	private void onLockCodes(ActionEvent event) 
	{
		if(saveButton.getText().equals("Reactivate") == false)
			if(validateFields() == false) return;

		changesMade();
		if(changesMade == true) 
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Changes Made");
		    confirmAlert.setContentText("Click OK to save your changes, or click Cancel to return to form.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    {
		    	return;
		    } else {
				// update
				current1095c.setJanLocked(true);
				current1095c.setFebLocked(true);
				current1095c.setMarLocked(true);
				current1095c.setAprLocked(true);
				current1095c.setMayLocked(true);
				current1095c.setJunLocked(true);
				current1095c.setJulLocked(true);
				current1095c.setAugLocked(true);
				current1095c.setSepLocked(true);
				current1095c.setOctLocked(true);
				current1095c.setNovLocked(true);
				current1095c.setDecLocked(true);

				changesMade();
				updateDependent();
				updateIrs1095c();
				show1095c();
		    }
	    } else {
			// update
			current1095c.setJanLocked(true);
			current1095c.setFebLocked(true);
			current1095c.setMarLocked(true);
			current1095c.setAprLocked(true);
			current1095c.setMayLocked(true);
			current1095c.setJunLocked(true);
			current1095c.setJulLocked(true);
			current1095c.setAugLocked(true);
			current1095c.setSepLocked(true);
			current1095c.setOctLocked(true);
			current1095c.setNovLocked(true);
			current1095c.setDecLocked(true);

			changesMade();
			updateDependent();
			updateIrs1095c();
			show1095c();
	    }
	}	

	@FXML
	private void onUnlockCodes(ActionEvent event) 
	{
		current1095c.setJanLocked(false);
		current1095c.setFebLocked(false);
		current1095c.setMarLocked(false);
		current1095c.setAprLocked(false);
		current1095c.setMayLocked(false);
		current1095c.setJunLocked(false);
		current1095c.setJulLocked(false);
		current1095c.setAugLocked(false);
		current1095c.setSepLocked(false);
		current1095c.setOctLocked(false);
		current1095c.setNovLocked(false);
		current1095c.setDecLocked(false);

		changesMade();
		lockUnlockCheck(current1095c);
//		show1095c();
	}	
	
	private void lockUnlockCheck(Irs1095c irs1095c)
	{
		try {

			updateDependent();

			Irs1095cRequest req = new Irs1095cRequest();
			irs1095c.setIrs1094cId(current1095c.getIrs1094cId());
			irs1095c.setIrs1094c(current1095c.getIrs1094c());
			irs1095c.setEmployee(current1095c.getEmployee());
			irs1095c.setEmployeeId(current1095c.getEmployeeId());
			irs1095c.setTaxYearId(current1095c.getTaxYearId());
			req.setId(irs1095c.getId());
			req.setEntity(irs1095c);

			current1095c = AdminPersistenceManager.getInstance().addOrUpdate(req);

			show1095c();

		} catch (CoreException e) {	
			DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { 
	    	DataManager.i().logGenericException(e); 
	    }
	}

	@FXML
	private void onGetDatacheck(ActionEvent event) 
	{
		// lets try a screen capture
		Stage stage = (Stage) prevButton.getScene().getWindow();
		Utils.savePDFScreenshot(stage, null, true);
	}

	@FXML
	private void onActiveSelect(ActionEvent event) 
	{
		if(activeFlag1095.isSelected())
		{
			inactiveLabel.setVisible(false);
		} else {
			inactiveLabel.setVisible(true);
		}
		changesMade();
	}

	@FXML
	private void onFileSsn(ActionEvent event) 
	{
		changesMade();
//		if(fileCIWithSSNCheck.isSelected() == true)
//		{
//			current1095c.setFileCIWithSSN(true);
//		} else {
//			current1095c.setFileCIWithSSN(false);
//		}
	}
	
	@FXML
	private void onFilingStatus(ActionEvent event)
	{
		try {
			if(current1095c == null ) return;

			// if the gfiling object is null, go get it
			if(current1095c.getFiling() == null) 
			{
				Irs1095FilingRequest fReq = new Irs1095FilingRequest();
				fReq.setIrs1095cId(current1095c.getId());
				List<Irs1095Filing> filings = AdminPersistenceManager.getInstance().getAll(fReq);

				if(filings != null && filings.size() > 0) 
				{
					// match based on tax year
					current1095c.setFiling(filings.get(0));
				}
			//	Irs1094FilingRequest f1094Req = new Irs1094FilingRequest();
			}
			
			// verify we do have domething now
			if(current1095c.getFiling() == null) return;
			
			// set the filing object
			DataManager.i().mIrs1095Filing = current1095c.getFiling();
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employee/ViewIRS1095FilingStatus.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (CoreException e) { 
			DataManager.i().log(Level.SEVERE, e); }        		
	    catch (Exception e) { 
	    	DataManager.i().logGenericException(e);
	    }
	}
	
	@FXML
	private void onViewCalc(ActionEvent event) 
	{
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employee/ViewCalculatedIrs1095cs.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
//		} catch (CoreException e) { DataManager.i().log(Level.SEVERE, e); }        		
		}catch (Exception e) { 
			DataManager.i().logGenericException(e); 
		}
	}
	
	@FXML
	private void onViewAuditTrail(ActionEvent event) {
		
	}
	
	@FXML
	private void onShowSystemInfo() 
	{
		try {
			// set the coredata
			DataManager.i().mIrs1095c = current1095c;
			DataManager.i().mCoreData = (CoreData) current1095c;
			DataManager.i().mCurrentCoreDataType = SystemDataType.IRS1095C;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
			Parent ControllerNode = loader.load();
	        ViewCoreSystemInfoController sysInfoController = (ViewCoreSystemInfoController) loader.getController();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if (sysInfoController.changesMade == true) 
	        {
	        	// update the local collection
	        	DataManager.i().mIrs1095cs.set(currentDataPosition, DataManager.i().mIrs1095c);
	        	// and show
	        	show1095c();
	        }
		} catch (IOException e) {
			DataManager.i().log(Level.SEVERE, e); }        		
	    catch (Exception e) {
	    	DataManager.i().logGenericException(e);
	    }
	}
	
	@FXML
	private void onSave(ActionEvent event) 
	{
		if(saveButton.getText().equals("Reactivate") == false)
			if(validateFields() == false) return;

		changesMade();
		if(changesMade == true) 
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Changes Made");
		    confirmAlert.setContentText("Click OK to save your changes, or click Cancel to exit without saving.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    {
		    	exitPopup();
		    	return;
		   } else {
				// update
				updateIrs1095c();
				//mark changes made
				changesMade = true;
				// and exit the pop up
				exitPopup();
		    }
	    }
	}
	
	@FXML
	private void onCancel(ActionEvent event) 
	{
//		changesMade();
		if(changesMade == true) 
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Changes Made");
		    confirmAlert.setContentText("You made changes to this form, click OK to disregard changes without saving.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		    exitPopup();
		} else {
			exitPopup();
		}
	}	
	
	private void exitPopup()
	{
		changesMade = true;
		Stage stage = (Stage) inactiveLabel.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void onPrevButton(ActionEvent event) 
	{
//		changesMade();
		if(changesMade == true) 
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Changes Made");
		    confirmAlert.setContentText("You made changes to this form, click OK to disregard changes without saving.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    {
		    	return;
		    } else {
				currentDataPosition--;
				nextButton.setDisable(false);
				if(currentDataPosition < 0) currentDataPosition = 0;
				if(currentDataPosition == 0)
					prevButton.setDisable(true);

				show1095c();
				changesMade = false;
				saveButton.setDisable(true);
		    }
	    } else {
			currentDataPosition--;
			nextButton.setDisable(false);
			if(currentDataPosition < 0) currentDataPosition = 0;
			if(currentDataPosition == 0)
				prevButton.setDisable(true);

			show1095c();
			saveButton.setDisable(true);
//			changesMade = false;
	    }
	}
		
	@FXML
	private void onNextButton(ActionEvent event) 
	{
//		changesMade();
		if(changesMade == true) 
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Changes Made");
		    confirmAlert.setContentText("You made changes to this form, click OK to disregard changes without saving.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    {
		    	return;
		    } else {
				currentDataPosition++;
				prevButton.setDisable(false);
				if(currentDataPosition + 1 == DataManager.i().mIrs1095cs.size()) 
				{
					nextButton.setDisable(true);
				}
				show1095c();
				changesMade = false;
				saveButton.setDisable(true);
		    }
	    } else {
			currentDataPosition++;
			prevButton.setDisable(false);
			if(currentDataPosition + 1 == DataManager.i().mIrs1095cs.size()) 
			{
				nextButton.setDisable(true);
			}
			show1095c();
			saveButton.setDisable(true);
//			changesMade = false;
	    }
	}	

	//Offer Code
	@FXML
	private void onOcJanButton(ActionEvent event) 
	{
		if(lockedJanCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			ocJanButton.setVisible(false);
		}
	}	

	@FXML
	private void onOcFebButton(ActionEvent event) 
	{
		if(lockedFebCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			ocFebButton.setVisible(false);
		}
	}	

	@FXML
	private void onOcMarButton(ActionEvent event) 
	{
		if(lockedMarCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			ocMarButton.setVisible(false);
		}
	}	

	@FXML
	private void onOcAprButton(ActionEvent event) 
	{
		if(lockedAprCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			ocAprButton.setVisible(false);
		}
	}	

	@FXML
	private void onOcMayButton(ActionEvent event) 
	{
		if(lockedMayCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			ocMayButton.setVisible(false);
		}
	}	

	@FXML
	private void onOcJunButton(ActionEvent event) 
	{
		if(lockedJunCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			ocJunButton.setVisible(false);
		}
	}	

	@FXML
	private void onOcJulButton(ActionEvent event) 
	{
		if(lockedJulCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			ocJulButton.setVisible(false);
		}
	}	

	@FXML
	private void onOcAugButton(ActionEvent event) 
	{
		if(lockedAugCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			ocAugButton.setVisible(false);
		}
	}	

	@FXML
	private void onOcSepButton(ActionEvent event) 
	{
		if(lockedSepCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			ocSepButton.setVisible(false);
		}
	}	

	@FXML
	private void onOcOctButton(ActionEvent event) 
	{
		if(lockedOctCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			ocOctButton.setVisible(false);
		}
	}	

	@FXML
	private void onOcNovButton(ActionEvent event) 
	{
		if(lockedNovCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			ocNovButton.setVisible(false);
		}
	}	

	@FXML
	private void onOcDecButton(ActionEvent event) 
	{
		if(lockedDecCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			ocDecButton.setVisible(false);
		}
	}	

	//Emp Share
	@FXML
	private void onEeJanButton(ActionEvent event) 
	{
		if(lockedJanCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			eeJanButton.setVisible(false);
		}
	}	

	@FXML
	private void onEeFebButton(ActionEvent event) 
	{
		if(lockedFebCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			eeFebButton.setVisible(false);
		}
	}	

	@FXML
	private void onEeMarButton(ActionEvent event) 
	{
		if(lockedMarCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			eeMarButton.setVisible(false);
		}
	}	

	@FXML
	private void onEeAprButton(ActionEvent event) 
	{
		if(lockedAprCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			eeAprButton.setVisible(false);
		}
	}	

	@FXML
	private void onEeMayButton(ActionEvent event) 
	{
		if(lockedMayCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			eeMayButton.setVisible(false);
		}
	}	

	@FXML
	private void onEeJunButton(ActionEvent event) 
	{
		if(lockedJunCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			eeJunButton.setVisible(false);
		}
	}	

	@FXML
	private void onEeJulButton(ActionEvent event) 
	{
		if(lockedJulCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			eeJulButton.setVisible(false);
		}
	}	

	@FXML
	private void onEeAugButton(ActionEvent event) 
	{
		if(lockedAugCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			eeAugButton.setVisible(false);
		}
	}	

	@FXML
	private void onEeSepButton(ActionEvent event) 
	{
		if(lockedSepCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			eeSepButton.setVisible(false);
		}
	}	

	@FXML
	private void onEeOctButton(ActionEvent event) 
	{
		if(lockedOctCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			eeOctButton.setVisible(false);
		}
	}	

	@FXML
	private void onEeNovButton(ActionEvent event) 
	{
		if(lockedNovCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			eeNovButton.setVisible(false);
		}
	}	

	@FXML
	private void onEeDecButton(ActionEvent event) 
	{
		if(lockedDecCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			eeDecButton.setVisible(false);
		}
	}	

	//Safe Harbor
	@FXML
	private void onShJanButton(ActionEvent event) 
	{
		if(lockedJanCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			shJanButton.setVisible(false);
		}
	}	

	@FXML
	private void onShFebButton(ActionEvent event) 
	{
		if(lockedFebCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			shFebButton.setVisible(false);
		}
	}	

	@FXML
	private void onShMarButton(ActionEvent event) 
	{
		if(lockedMarCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			shMarButton.setVisible(false);
		}
	}	

	@FXML
	private void onShAprButton(ActionEvent event) 
	{
		if(lockedAprCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			shAprButton.setVisible(false);
		}
	}	

	@FXML
	private void onShMayButton(ActionEvent event) 
	{
		if(lockedMayCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			shMayButton.setVisible(false);
		}
	}	

	@FXML
	private void onShJunButton(ActionEvent event) 
	{
		if(lockedJunCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			shJunButton.setVisible(false);
		}
	}	

	@FXML
	private void onShJulButton(ActionEvent event) 
	{
		if(lockedJulCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			shJulButton.setVisible(false);
		}
	}	

	@FXML
	private void onShAugButton(ActionEvent event) 
	{
		if(lockedAugCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			shAugButton.setVisible(false);
		}
	}	

	@FXML
	private void onShSepButton(ActionEvent event) 
	{
		if(lockedSepCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			shSepButton.setVisible(false);
		}
	}	

	@FXML
	private void onShOctButton(ActionEvent event) 
	{
		if(lockedOctCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			shOctButton.setVisible(false);
		}
	}	

	@FXML
	private void onShNovButton(ActionEvent event) 
	{
		if(lockedNovCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			shNovButton.setVisible(false);
		}
	}	

	@FXML
	private void onShDecButton(ActionEvent event) 
	{
		if(lockedDecCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			shDecButton.setVisible(false);
		}
	}	

	//Covered Individual
	@FXML
	private void onCovJanCheck(ActionEvent event) { changesMade(); }	
	@FXML
	private void onCovFebCheck(ActionEvent event) { changesMade(); }	
	@FXML
	private void onCovMarCheck(ActionEvent event) { changesMade(); }	
	@FXML
	private void onCovAprCheck(ActionEvent event) { changesMade(); }	
	@FXML
	private void onCovMayCheck(ActionEvent event) { changesMade(); }	
	@FXML
	private void onCovJunCheck(ActionEvent event) { changesMade(); }	
	@FXML
	private void onCovJulCheck(ActionEvent event) { changesMade(); }	
	@FXML
	private void onCovAugCheck(ActionEvent event) { changesMade(); }	
	@FXML
	private void onCovSepCheck(ActionEvent event) { changesMade(); }	
	@FXML
	private void onCovOctCheck(ActionEvent event) { changesMade(); }	
	@FXML
	private void onCovNovCheck(ActionEvent event) { changesMade(); }	
	@FXML
	private void onCovDecCheck(ActionEvent event) { changesMade(); }	

	@FXML
	private void onCovJanButton(ActionEvent event) 
	{
		if(lockedJanCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			covJanButton.setVisible(false);
		}
	}	

	@FXML
	private void onCovFebButton(ActionEvent event) 
	{
		if(lockedFebCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			covFebButton.setVisible(false);
		}
	}	

	@FXML
	private void onCovMarButton(ActionEvent event) 
	{
		if(lockedMarCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			covMarButton.setVisible(false);
		}
	}	

	@FXML
	private void onCovAprButton(ActionEvent event) 
	{
		if(lockedAprCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			covAprButton.setVisible(false);
		}
	}	

	@FXML
	private void onCovMayButton(ActionEvent event) 
	{
		if(lockedMayCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			covMayButton.setVisible(false);
		}
	}	

	@FXML
	private void onCovJunButton(ActionEvent event) 
	{
		if(lockedJunCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			covJunButton.setVisible(false);
		}
	}	

	@FXML
	private void onCovJulButton(ActionEvent event) 
	{
		if(lockedJulCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			covJulButton.setVisible(false);
		}
	}	

	@FXML
	private void onCovAugButton(ActionEvent event) 
	{
		if(lockedAugCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			covAugButton.setVisible(false);
		}
	}	

	@FXML
	private void onCovSepButton(ActionEvent event) 
	{
		if(lockedSepCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			covSepButton.setVisible(false);
		}
	}	

	@FXML
	private void onCovOctButton(ActionEvent event) 
	{
		if(lockedOctCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			covOctButton.setVisible(false);
		}
	}	

	@FXML
	private void onCovNovButton(ActionEvent event) 
	{
		if(lockedNovCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			covNovButton.setVisible(false);
		}
	}	

	@FXML
	private void onCovDecButton(ActionEvent event) 
	{
		if(lockedDecCheck.isSelected() == true)
		{
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
		    confirmAlert.setTitle("Form Locked");
		    confirmAlert.setContentText("Unlock form in order to edit it.");
		    Optional<ButtonType> result = confirmAlert.showAndWait();
		    if((result.isPresent()) && (result.get() != ButtonType.OK))
		    	return;
		} else {
			covDecButton.setVisible(false);
		}
	}	
	
	//Emp Share
	@FXML
	private void onEeShareJan(ActionEvent event) { /*changesMade();*/ }

	@FXML
	private void onEeShareFeb(ActionEvent event) { /*changesMade();*/ }

	@FXML
	private void onEeShareMar(ActionEvent event) { /*changesMade();*/ }

	@FXML
	private void onEeShareApr(ActionEvent event) { /*changesMade();*/ }

	@FXML
	private void onEeShareMay(ActionEvent event) { /*changesMade();*/ }

	@FXML
	private void onEeShareJun(ActionEvent event) { /*changesMade();*/ }

	@FXML
	private void onEeShareJul(ActionEvent event) { /*changesMade();*/ }

	@FXML
	private void onEeShareAug(ActionEvent event) { /*changesMade();*/ }

	@FXML
	private void onEeShareSep(ActionEvent event) { /*changesMade();*/ }

	@FXML
	private void onEeShareOct(ActionEvent event) { /*changesMade();*/ }

	@FXML
	private void onEeShareNov(ActionEvent event) { /*changesMade();*/ }

	@FXML
	private void onEeShareDec(ActionEvent event) { /*changesMade();*/ }

	@FXML
	private void onLockJan(ActionEvent event) 
	{
		if(lockedJanCheck.isSelected())
		{
			changesMade();
			offerCodeJanField.setDisable(true);
			eeShareJanField.setDisable(true);
			shcJanField.setDisable(true);
			coverageJanCheck.setDisable(true);
			coverageJanCheck.setStyle("-fx-opacity: 0.3");
			ocJanButton.setVisible(true);
			eeJanButton.setVisible(true);
			shJanButton.setVisible(true);
			covJanButton.setVisible(true);
			janOffLbl.setVisible(true);
			janEmpLbl.setVisible(true);
			janSHbLbl.setVisible(true);
			current1095c.setJanLocked(true);
			current1095c.setJanCovered(coverageJanCheck.isSelected());
			janCheck.setDisable(true);

			if(offerCodeJanField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setJanOfferCode(OfferCode.valueOf(offerCodeJanField.getValue()));
			} else {
				janOffLbl.setText("");
				current1095c.setJanOfferCode(null);
			}

			if(eeShareJanField.getText() != null && eeShareJanField.getText().length() > 0)
			{
				current1095c.setJanEEShare(Float.valueOf(eeShareJanField.getText()));
			} else {
				janEmpLbl.setText("");
				current1095c.setJanEEShare(null);
			}

			if(shcJanField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setJanSafeHarborCode(SafeHarborCode.valueOf(shcJanField.getValue()));
			} else {
				janSHbLbl.setText("");
				current1095c.setJanSafeHarborCode(null);
			}

			lockUnlockCheck(current1095c);
		} else {
			changesMade();
			offerCodeJanField.setDisable(false);
			eeShareJanField.setDisable(false);
			shcJanField.setDisable(false);
			coverageJanCheck.setDisable(false);
			coverageJanCheck.setStyle("-fx-opacity: 1");
			ocJanButton.setVisible(false);
			eeJanButton.setVisible(false);
			shJanButton.setVisible(false);
			covJanButton.setVisible(false);
			current1095c.setJanLocked(false);
			janOffLbl.setVisible(false);
			janEmpLbl.setVisible(false);
			janSHbLbl.setVisible(false);
			janCheck.setDisable(false);
			lockUnlockCheck(current1095c);
		}
	}

	@FXML
	private void onLockFeb(ActionEvent event) 
	{
		if(lockedFebCheck.isSelected())
		{
			changesMade();
			offerCodeFebField.setDisable(true);
			eeShareFebField.setDisable(true);
			shcFebField.setDisable(true);
			coverageFebCheck.setDisable(true);
			coverageFebCheck.setStyle("-fx-opacity: 0.3");
			ocFebButton.setVisible(true);
			eeFebButton.setVisible(true);
			shFebButton.setVisible(true);
			covFebButton.setVisible(true);
			febOffLbl.setVisible(true);
			febEmpLbl.setVisible(true);
			febSHbLbl.setVisible(true);
			current1095c.setFebLocked(true);
			current1095c.setFebCovered(coverageFebCheck.isSelected());
			febCheck.setDisable(true);

			if(offerCodeFebField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setFebOfferCode(OfferCode.valueOf(offerCodeFebField.getValue()));
			} else {
				febOffLbl.setText("");
				current1095c.setFebOfferCode(null);
			}

			if(eeShareFebField.getText() != null && eeShareFebField.getText().length() > 0)
			{
				current1095c.setFebEEShare(Float.valueOf(eeShareFebField.getText()));
			} else {
				febEmpLbl.setText("");
				current1095c.setFebEEShare(null);
			}

			if(shcFebField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setFebSafeHarborCode(SafeHarborCode.valueOf(shcFebField.getValue()));
			} else {
				febSHbLbl.setText("");
				current1095c.setFebSafeHarborCode(null);
			}

			lockUnlockCheck(current1095c);
		} else {
			changesMade();
			offerCodeFebField.setDisable(false);
			eeShareFebField.setDisable(false);
			shcFebField.setDisable(false);
			coverageFebCheck.setDisable(false);
			coverageFebCheck.setStyle("-fx-opacity: 1");
			ocFebButton.setVisible(false);
			eeFebButton.setVisible(false);
			shFebButton.setVisible(false);
			covFebButton.setVisible(false);
			current1095c.setFebLocked(false);
			febOffLbl.setVisible(false);
			febEmpLbl.setVisible(false);
			febSHbLbl.setVisible(false);
			febCheck.setDisable(false);
			lockUnlockCheck(current1095c);
		}
	}	

	@FXML
	private void onLockMar(ActionEvent event) 
	{
		if(lockedMarCheck.isSelected())
		{
			changesMade();
			offerCodeMarField.setDisable(true);
			eeShareMarField.setDisable(true);
			shcMarField.setDisable(true);
			coverageMarCheck.setDisable(true);
			coverageMarCheck.setStyle("-fx-opacity: 0.3");
			ocMarButton.setVisible(true);
			eeMarButton.setVisible(true);
			shMarButton.setVisible(true);
			covMarButton.setVisible(true);
			marOffLbl.setVisible(true);
			marEmpLbl.setVisible(true);
			marSHbLbl.setVisible(true);
			current1095c.setMarLocked(true);
			current1095c.setMarCovered(coverageMarCheck.isSelected());
			marCheck.setDisable(true);

			if(offerCodeMarField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setMarOfferCode(OfferCode.valueOf(offerCodeMarField.getValue()));
			} else {
				marOffLbl.setText("");
				current1095c.setMarOfferCode(null);
			}

			if(eeShareMarField.getText() != null && eeShareMarField.getText().length() > 0)
			{
				current1095c.setMarEEShare(Float.valueOf(eeShareMarField.getText()));
			} else {
				marEmpLbl.setText("");
				current1095c.setMarEEShare(null);
			}

			if(shcMarField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setMarSafeHarborCode(SafeHarborCode.valueOf(shcMarField.getValue()));
			} else {
				marSHbLbl.setText("");
				current1095c.setMarSafeHarborCode(null);
			}

			lockUnlockCheck(current1095c);
		} else {
			changesMade();
			offerCodeMarField.setDisable(false);
			eeShareMarField.setDisable(false);
			shcMarField.setDisable(false);
			coverageMarCheck.setDisable(false);
			coverageMarCheck.setStyle("-fx-opacity: 1");
			ocMarButton.setVisible(false);
			eeMarButton.setVisible(false);
			shMarButton.setVisible(false);
			covMarButton.setVisible(false);
			current1095c.setMarLocked(false);
			marOffLbl.setVisible(false);
			marEmpLbl.setVisible(false);
			marSHbLbl.setVisible(false);
			marCheck.setDisable(false);
			lockUnlockCheck(current1095c);
		}
	}	

	@FXML
	private void onLockApr(ActionEvent event) 
	{
		if(lockedAprCheck.isSelected())
		{
			changesMade();
			offerCodeAprField.setDisable(true);
			eeShareAprField.setDisable(true);
			shcAprField.setDisable(true);
			coverageAprCheck.setDisable(true);
			coverageAprCheck.setStyle("-fx-opacity: 0.3");
			ocAprButton.setVisible(true);
			eeAprButton.setVisible(true);
			shAprButton.setVisible(true);
			covAprButton.setVisible(true);
			aprOffLbl.setVisible(true);
			aprEmpLbl.setVisible(true);
			aprSHbLbl.setVisible(true);
			current1095c.setAprLocked(true);
			current1095c.setAprCovered(coverageAprCheck.isSelected());
			aprCheck.setDisable(true);

			if(offerCodeAprField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setAprOfferCode(OfferCode.valueOf(offerCodeAprField.getValue()));
			} else {
				aprOffLbl.setText("");
				current1095c.setAprOfferCode(null);
			}

			if(eeShareAprField.getText() != null && eeShareAprField.getText().length() > 0)
			{
				current1095c.setAprEEShare(Float.valueOf(eeShareAprField.getText()));
			} else {
				aprEmpLbl.setText("");
				current1095c.setAprEEShare(null);
			}

			if(shcAprField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setAprSafeHarborCode(SafeHarborCode.valueOf(shcAprField.getValue()));
			} else {
				aprSHbLbl.setText("");
				current1095c.setAprSafeHarborCode(null);
			}

			lockUnlockCheck(current1095c);
		} else {
			changesMade();
			offerCodeAprField.setDisable(false);
			eeShareAprField.setDisable(false);
			shcAprField.setDisable(false);
			coverageAprCheck.setDisable(false);
			coverageAprCheck.setStyle("-fx-opacity: 1");
			ocAprButton.setVisible(false);
			eeAprButton.setVisible(false);
			shAprButton.setVisible(false);
			covAprButton.setVisible(false);
			current1095c.setAprLocked(false);
			aprOffLbl.setVisible(false);
			aprEmpLbl.setVisible(false);
			aprSHbLbl.setVisible(false);
			aprCheck.setDisable(false);
			lockUnlockCheck(current1095c);
		}
	}

	@FXML
	private void onLockMay(ActionEvent event) 
	{
		if(lockedMayCheck.isSelected())
		{
			changesMade();
			offerCodeMayField.setDisable(true);
			eeShareMayField.setDisable(true);
			shcMayField.setDisable(true);
			coverageMayCheck.setDisable(true);
			coverageMayCheck.setStyle("-fx-opacity: 0.3");
			ocMayButton.setVisible(true);
			eeMayButton.setVisible(true);
			shMayButton.setVisible(true);
			covMayButton.setVisible(true);
			mayOffLbl.setVisible(true);
			mayEmpLbl.setVisible(true);
			maySHbLbl.setVisible(true);
			current1095c.setMayLocked(true);
			current1095c.setMayCovered(coverageMayCheck.isSelected());
			mayCheck.setDisable(true);

			if(offerCodeMayField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setMayOfferCode(OfferCode.valueOf(offerCodeMayField.getValue()));
			} else {
				mayOffLbl.setText("");
				current1095c.setMayOfferCode(null);
			}

			if(eeShareMayField.getText() != null && eeShareMayField.getText().length() > 0)
			{
				current1095c.setMayEEShare(Float.valueOf(eeShareMayField.getText()));
			} else {
				mayEmpLbl.setText("");
				current1095c.setMayEEShare(null);
			}

			if(shcMayField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setMaySafeHarborCode(SafeHarborCode.valueOf(shcMayField.getValue()));
			} else {
				maySHbLbl.setText("");
				current1095c.setMaySafeHarborCode(null);
			}

			lockUnlockCheck(current1095c);
		} else {
			changesMade();
			offerCodeMayField.setDisable(false);
			eeShareMayField.setDisable(false);
			shcMayField.setDisable(false);
			coverageMayCheck.setDisable(false);
			coverageMayCheck.setStyle("-fx-opacity: 1");
			ocMayButton.setVisible(false);
			eeMayButton.setVisible(false);
			shMayButton.setVisible(false);
			covMayButton.setVisible(false);
			current1095c.setMayLocked(false);
			mayOffLbl.setVisible(false);
			mayEmpLbl.setVisible(false);
			maySHbLbl.setVisible(false);
			mayCheck.setDisable(false);
			lockUnlockCheck(current1095c);
		}
	}

	@FXML
	private void onLockJun(ActionEvent event) 
	{
		if(lockedJunCheck.isSelected())
		{
			changesMade();
			offerCodeJunField.setDisable(true);
			eeShareJunField.setDisable(true);
			shcJunField.setDisable(true);
			coverageJunCheck.setDisable(true);
			coverageJunCheck.setStyle("-fx-opacity: 0.3");
			ocJunButton.setVisible(true);
			eeJunButton.setVisible(true);
			shJunButton.setVisible(true);
			covJunButton.setVisible(true);
			junOffLbl.setVisible(true);
			junEmpLbl.setVisible(true);
			junSHbLbl.setVisible(true);
			current1095c.setJunLocked(true);
			current1095c.setJunCovered(coverageJunCheck.isSelected());
			junCheck.setDisable(true);

			if(offerCodeJunField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setJunOfferCode(OfferCode.valueOf(offerCodeJunField.getValue()));
			} else {
				junOffLbl.setText("");
				current1095c.setJunOfferCode(null);
			}

			if(eeShareJunField.getText() != null && eeShareJunField.getText().length() > 0)
			{
				current1095c.setJunEEShare(Float.valueOf(eeShareJunField.getText()));
			} else {
				junEmpLbl.setText("");
				current1095c.setJunEEShare(null);
			}

			if(shcJunField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setJunSafeHarborCode(SafeHarborCode.valueOf(shcJunField.getValue()));
			} else {
				junSHbLbl.setText("");
				current1095c.setJunSafeHarborCode(null);
			}

			lockUnlockCheck(current1095c);
		} else {
			changesMade();
			offerCodeJunField.setDisable(false);
			eeShareJunField.setDisable(false);
			shcJunField.setDisable(false);
			coverageJunCheck.setDisable(false);
			coverageJunCheck.setStyle("-fx-opacity: 1");
			ocJunButton.setVisible(false);
			eeJunButton.setVisible(false);
			shJunButton.setVisible(false);
			covJunButton.setVisible(false);
			current1095c.setJunLocked(false);
			junOffLbl.setVisible(false);
			junEmpLbl.setVisible(false);
			junSHbLbl.setVisible(false);
			junCheck.setDisable(false);
			lockUnlockCheck(current1095c);
		}
	}	

	@FXML
	private void onLockJul(ActionEvent event) 
	{
		if(lockedJulCheck.isSelected())
		{
			changesMade();
			offerCodeJulField.setDisable(true);
			eeShareJulField.setDisable(true);
			shcJulField.setDisable(true);
			coverageJulCheck.setDisable(true);
			coverageJulCheck.setStyle("-fx-opacity: 0.3");
			ocJulButton.setVisible(true);
			eeJulButton.setVisible(true);
			shJulButton.setVisible(true);
			covJulButton.setVisible(true);
			julOffLbl.setVisible(true);
			julEmpLbl.setVisible(true);
			julSHbLbl.setVisible(true);
			current1095c.setJulLocked(true);
			current1095c.setJulCovered(coverageJulCheck.isSelected());
			julCheck.setDisable(true);

			if(offerCodeJulField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setJulOfferCode(OfferCode.valueOf(offerCodeJulField.getValue()));
			} else {
				julOffLbl.setText("");
				current1095c.setJulOfferCode(null);
			}

			if(eeShareJulField.getText() != null && eeShareJulField.getText().length() > 0)
			{
				current1095c.setJulEEShare(Float.valueOf(eeShareJulField.getText()));
			} else {
				julEmpLbl.setText("");
				current1095c.setJulEEShare(null);
			}

			if(shcJulField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setJulSafeHarborCode(SafeHarborCode.valueOf(shcJulField.getValue()));
			} else {
				julSHbLbl.setText("");
				current1095c.setJulSafeHarborCode(null);
			}

			lockUnlockCheck(current1095c);
		} else {
			changesMade();
			offerCodeJulField.setDisable(false);
			eeShareJulField.setDisable(false);
			shcJulField.setDisable(false);
			coverageJulCheck.setDisable(false);
			coverageJulCheck.setStyle("-fx-opacity: 1");
			ocJulButton.setVisible(false);
			eeJulButton.setVisible(false);
			shJulButton.setVisible(false);
			covJulButton.setVisible(false);
			current1095c.setJulLocked(false);
			julOffLbl.setVisible(false);
			julEmpLbl.setVisible(false);
			julSHbLbl.setVisible(false);
			julCheck.setDisable(false);
			lockUnlockCheck(current1095c);
		}
	}

	@FXML
	private void onLockAug(ActionEvent event) 
	{
		if(lockedAugCheck.isSelected())
		{
			changesMade();
			offerCodeAugField.setDisable(true);
			eeShareAugField.setDisable(true);
			shcAugField.setDisable(true);
			coverageAugCheck.setDisable(true);
			coverageAugCheck.setStyle("-fx-opacity: 0.3");
			ocAugButton.setVisible(true);
			eeAugButton.setVisible(true);
			shAugButton.setVisible(true);
			covAugButton.setVisible(true);
			augOffLbl.setVisible(true);
			augEmpLbl.setVisible(true);
			augSHbLbl.setVisible(true);
			current1095c.setAugLocked(true);
			current1095c.setAugCovered(coverageAugCheck.isSelected());
			augCheck.setDisable(true);

			if(offerCodeAugField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setAugOfferCode(OfferCode.valueOf(offerCodeAugField.getValue()));
			} else {
				augOffLbl.setText("");
				current1095c.setAugOfferCode(null);
			}

			if(eeShareAugField.getText() != null && eeShareAugField.getText().length() > 0)
			{
				current1095c.setAugEEShare(Float.valueOf(eeShareAugField.getText()));
			} else {
				augEmpLbl.setText("");
				current1095c.setAugEEShare(null);
			}

			if(shcAugField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setAugSafeHarborCode(SafeHarborCode.valueOf(shcAugField.getValue()));
			} else {
				augSHbLbl.setText("");
				current1095c.setAugSafeHarborCode(null);
			}

			lockUnlockCheck(current1095c);
		} else {
			changesMade();
			offerCodeAugField.setDisable(false);
			eeShareAugField.setDisable(false);
			shcAugField.setDisable(false);
			coverageAugCheck.setDisable(false);
			coverageAugCheck.setStyle("-fx-opacity: 1");
			ocAugButton.setVisible(false);
			eeAugButton.setVisible(false);
			shAugButton.setVisible(false);
			covAugButton.setVisible(false);
			current1095c.setAugLocked(false);
			augOffLbl.setVisible(false);
			augEmpLbl.setVisible(false);
			augSHbLbl.setVisible(false);
			augCheck.setDisable(false);
			lockUnlockCheck(current1095c);
		}
	}

	@FXML
	private void onLockSep(ActionEvent event) 
	{
		if(lockedSepCheck.isSelected())
		{
			changesMade();
			offerCodeSepField.setDisable(true);
			eeShareSepField.setDisable(true);
			shcSepField.setDisable(true);
			coverageSepCheck.setDisable(true);
			coverageSepCheck.setStyle("-fx-opacity: 0.3");
			ocSepButton.setVisible(true);
			eeSepButton.setVisible(true);
			shSepButton.setVisible(true);
			covSepButton.setVisible(true);
			sepOffLbl.setVisible(true);
			sepEmpLbl.setVisible(true);
			sepSHbLbl.setVisible(true);
			current1095c.setSepLocked(true);
			current1095c.setSepCovered(coverageSepCheck.isSelected());
			sepCheck.setDisable(true);

			if(offerCodeSepField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setSepOfferCode(OfferCode.valueOf(offerCodeSepField.getValue()));
			} else {
				sepOffLbl.setText("");
				current1095c.setSepOfferCode(null);
			}

			if(eeShareSepField.getText() != null && eeShareSepField.getText().length() > 0)
			{
				current1095c.setSepEEShare(Float.valueOf(eeShareSepField.getText()));
			} else {
				sepEmpLbl.setText("");
				current1095c.setSepEEShare(null);
			}

			if(shcSepField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setSepSafeHarborCode(SafeHarborCode.valueOf(shcSepField.getValue()));
			} else {
				sepSHbLbl.setText("");
				current1095c.setSepSafeHarborCode(null);
			}

			lockUnlockCheck(current1095c);
		} else {
			changesMade();
			offerCodeSepField.setDisable(false);
			eeShareSepField.setDisable(false);
			shcSepField.setDisable(false);
			coverageSepCheck.setDisable(false);
			coverageSepCheck.setStyle("-fx-opacity: 1");
			ocSepButton.setVisible(false);
			eeSepButton.setVisible(false);
			shSepButton.setVisible(false);
			covSepButton.setVisible(false);
			current1095c.setSepLocked(false);
			sepOffLbl.setVisible(false);
			sepEmpLbl.setVisible(false);
			sepSHbLbl.setVisible(false);
			sepCheck.setDisable(false);
			lockUnlockCheck(current1095c);
		}
	}

	@FXML
	private void onLockOct(ActionEvent event) 
	{
		if(lockedOctCheck.isSelected())
		{
			changesMade();
			offerCodeOctField.setDisable(true);
			eeShareOctField.setDisable(true);
			shcOctField.setDisable(true);
			coverageOctCheck.setDisable(true);
			coverageOctCheck.setStyle("-fx-opacity: 0.3");
			ocOctButton.setVisible(true);
			eeOctButton.setVisible(true);
			shOctButton.setVisible(true);
			covOctButton.setVisible(true);
			octOffLbl.setVisible(true);
			octEmpLbl.setVisible(true);
			octSHbLbl.setVisible(true);
			current1095c.setOctLocked(true);
			current1095c.setOctCovered(coverageOctCheck.isSelected());
			octCheck.setDisable(true);

			if(offerCodeOctField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setOctOfferCode(OfferCode.valueOf(offerCodeOctField.getValue()));
			} else {
				octOffLbl.setText("");
				current1095c.setOctOfferCode(null);
			}

			if(eeShareOctField.getText() != null && eeShareOctField.getText().length() > 0)
			{
				current1095c.setOctEEShare(Float.valueOf(eeShareOctField.getText()));
			} else {
				octEmpLbl.setText("");
				current1095c.setOctEEShare(null);
			}

			if(shcOctField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setOctSafeHarborCode(SafeHarborCode.valueOf(shcOctField.getValue()));
			} else {
				octSHbLbl.setText("");
				current1095c.setOctSafeHarborCode(null);
			}

			lockUnlockCheck(current1095c);
		} else {
			changesMade();
			offerCodeOctField.setDisable(false);
			eeShareOctField.setDisable(false);
			shcOctField.setDisable(false);
			coverageOctCheck.setDisable(false);
			coverageOctCheck.setStyle("-fx-opacity: 1");
			ocOctButton.setVisible(false);
			eeOctButton.setVisible(false);
			shOctButton.setVisible(false);
			covOctButton.setVisible(false);
			current1095c.setOctLocked(false);
			octOffLbl.setVisible(false);
			octEmpLbl.setVisible(false);
			octSHbLbl.setVisible(false);
			octCheck.setDisable(false);
			lockUnlockCheck(current1095c);
		}
	}

	@FXML
	private void onLockNov(ActionEvent event) 
	{
		if(lockedNovCheck.isSelected())
		{
			changesMade();
			offerCodeNovField.setDisable(true);
			eeShareNovField.setDisable(true);
			shcNovField.setDisable(true);
			coverageNovCheck.setDisable(true);
			coverageNovCheck.setStyle("-fx-opacity: 0.3");
			ocNovButton.setVisible(true);
			eeNovButton.setVisible(true);
			shNovButton.setVisible(true);
			covNovButton.setVisible(true);
			novOffLbl.setVisible(true);
			novEmpLbl.setVisible(true);
			novSHbLbl.setVisible(true);
			current1095c.setNovLocked(true);
			current1095c.setNovCovered(coverageNovCheck.isSelected());
			novCheck.setDisable(true);

			if(offerCodeNovField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setNovOfferCode(OfferCode.valueOf(offerCodeNovField.getValue()));
			} else {
				novOffLbl.setText("");
				current1095c.setNovOfferCode(null);
			}

			if(eeShareNovField.getText() != null && eeShareNovField.getText().length() > 0)
			{
				current1095c.setNovEEShare(Float.valueOf(eeShareNovField.getText()));
			} else {
				novEmpLbl.setText("");
				current1095c.setNovEEShare(null);
			}

			if(shcNovField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setNovSafeHarborCode(SafeHarborCode.valueOf(shcNovField.getValue()));
			} else {
				novSHbLbl.setText("");
				current1095c.setNovSafeHarborCode(null);
			}

			lockUnlockCheck(current1095c);
		} else {
			changesMade();
			offerCodeNovField.setDisable(false);
			eeShareNovField.setDisable(false);
			shcNovField.setDisable(false);
			coverageNovCheck.setDisable(false);
			coverageNovCheck.setStyle("-fx-opacity: 1");
			ocNovButton.setVisible(false);
			eeNovButton.setVisible(false);
			shNovButton.setVisible(false);
			covNovButton.setVisible(false);
			current1095c.setNovLocked(false);
			novOffLbl.setVisible(false);
			novEmpLbl.setVisible(false);
			novSHbLbl.setVisible(false);
			novCheck.setDisable(false);
			lockUnlockCheck(current1095c);
		}
	}

	@FXML
	private void onLockDec(ActionEvent event) 
	{
		if(lockedDecCheck.isSelected())
		{
			changesMade();
			offerCodeDecField.setDisable(true);
			eeShareDecField.setDisable(true);
			shcDecField.setDisable(true);
			coverageDecCheck.setDisable(true);
			coverageDecCheck.setStyle("-fx-opacity: 0.3");
			ocDecButton.setVisible(true);
			eeDecButton.setVisible(true);
			shDecButton.setVisible(true);
			covDecButton.setVisible(true);
			decOffLbl.setVisible(true);
			decEmpLbl.setVisible(true);
			decSHbLbl.setVisible(true);
			current1095c.setDecLocked(true);
			current1095c.setDecCovered(coverageDecCheck.isSelected());
			decCheck.setDisable(true);

			if(offerCodeDecField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setDecOfferCode(OfferCode.valueOf(offerCodeDecField.getValue()));
			} else {
				decOffLbl.setText("");
				current1095c.setDecOfferCode(null);
			}

			if(eeShareDecField.getText() != null && eeShareDecField.getText().length() > 0)
			{
				current1095c.setDecEEShare(Float.valueOf(eeShareDecField.getText()));
			} else {
				decEmpLbl.setText("");
				current1095c.setDecEEShare(null);
			}

			if(shcDecField.getSelectionModel().getSelectedItem().isEmpty() == false)
			{
				current1095c.setDecSafeHarborCode(SafeHarborCode.valueOf(shcDecField.getValue()));
			} else {
				decSHbLbl.setText("");
				current1095c.setDecSafeHarborCode(null);
			}

			lockUnlockCheck(current1095c);
		} else {
			changesMade();
			offerCodeDecField.setDisable(false);
			eeShareDecField.setDisable(false);
			shcDecField.setDisable(false);
			coverageDecCheck.setDisable(false);
			coverageDecCheck.setStyle("-fx-opacity: 1");
			ocDecButton.setVisible(false);
			eeDecButton.setVisible(false);
			shDecButton.setVisible(false);
			covDecButton.setVisible(false);
			current1095c.setDecLocked(false);
			decOffLbl.setVisible(false);
			decEmpLbl.setVisible(false);
			decSHbLbl.setVisible(false);
			decCheck.setDisable(false);
			lockUnlockCheck(current1095c);
		}
	}	

	public void updateDependent()
	{
		try {

			Irs1095cCIRequest irs1095cCiReq = new Irs1095cCIRequest();

			for(Dependent dependent : DataManager.i().mEmployee.getPerson().getDependents())
			{
				if (dependent.getIrs1095cCIs() != null) {
					for(Irs1095cCI irs1095cCi : dependent.getIrs1095cCIs())
					{
						irs1095cCi.setJanCovered(janCheck.isSelected());
						irs1095cCi.setFebCovered(febCheck.isSelected());
						irs1095cCi.setMarCovered(marCheck.isSelected());
						irs1095cCi.setAprCovered(aprCheck.isSelected());
						irs1095cCi.setMayCovered(mayCheck.isSelected());
						irs1095cCi.setJunCovered(junCheck.isSelected());
						irs1095cCi.setJulCovered(julCheck.isSelected());
						irs1095cCi.setAugCovered(augCheck.isSelected());
						irs1095cCi.setSepCovered(sepCheck.isSelected());
						irs1095cCi.setOctCovered(octCheck.isSelected());
						irs1095cCi.setNovCovered(novCheck.isSelected());
						irs1095cCi.setDecCovered(decCheck.isSelected());
		
						irs1095cCiReq.setEntity(irs1095cCi);
						irs1095cCiReq.setId(irs1095cCi.getId());
						irs1095cCi.setDependentId(dependent.getId());
						irs1095cCi.setDependent(dependent);
						irs1095cCi.setIrs1095cId(current1095c.getId());
						irs1095cCi.setIrs1095c(current1095c);
		
						irs1095cCi = AdminPersistenceManager.getInstance().addOrUpdate(irs1095cCiReq);
					}
				}
			}
		} catch (CoreException e) {	
			/*DataManager.i().log(Level.SEVERE, e); */ e.printStackTrace(); }
	    catch (Exception e) { 
	    	DataManager.i().logGenericException(e); 
	    }
	}

	public class HBoxDependentCell extends HBox 
	{
		SimpleStringProperty firstName = new SimpleStringProperty();
		SimpleStringProperty lastName = new SimpleStringProperty();
		SimpleStringProperty ssnNum = new SimpleStringProperty();
		SimpleStringProperty dobNum = new SimpleStringProperty();
		CheckBox janCheck = new CheckBox();
		CheckBox febCheck = new CheckBox();
		CheckBox marCheck = new CheckBox();
		CheckBox aprCheck = new CheckBox();
		CheckBox mayCheck = new CheckBox();
		CheckBox junCheck = new CheckBox();
		CheckBox julCheck = new CheckBox();
		CheckBox augCheck = new CheckBox();
		CheckBox sepCheck = new CheckBox();
		CheckBox octCheck = new CheckBox();
		CheckBox novCheck = new CheckBox();
		CheckBox decCheck = new CheckBox();
		SimpleStringProperty depId = new SimpleStringProperty();
		CheckBox activeCheck = new CheckBox();

		Irs1095cCI irs1095cCi;
		Dependent dependent;

		public String getFirstName() { return firstName.get(); }
		public String getLastName() { return lastName.get(); }
		public String getSsnNum() { return ssnNum.get(); }
		public String getDobNum() { return dobNum.get(); }
		public CheckBox getJanCheck() { return janCheck; }
		public CheckBox getFebCheck() { return febCheck; }
		public CheckBox getMarCheck() { return marCheck; }
		public CheckBox getAprCheck() { return aprCheck; }
		public CheckBox getMayCheck() { return mayCheck; }
		public CheckBox getJunCheck() { return junCheck; }
		public CheckBox getJulCheck() { return julCheck; }
		public CheckBox getAugCheck() { return augCheck; }
		public CheckBox getSepCheck() { return sepCheck; }
		public CheckBox getOctCheck() { return octCheck; }
		public CheckBox getNovCheck() { return novCheck; }
		public CheckBox getDecCheck() { return decCheck; }
		public String getDepId() { return depId.get(); }
		public CheckBox getActiveCheck() { return activeCheck; }
		public Irs1095cCI getIrs1095cCI() { return irs1095cCi; }
		public Dependent getDependent() { return dependent; }

		HBoxDependentCell(Irs1095cCI irs1095cCi, Dependent dependent) 
		{
			super();
			this.irs1095cCi = irs1095cCi;
			this.dependent = (dependent);

			//Dependent CheckBox listeners
			janCheck.setOnMouseClicked(mouseClickedEvent -> 
			{
		        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) 
		        {
	            	changesMade();
		        }
	        });

			febCheck.setOnMouseClicked(mouseClickedEvent -> 
			{
		        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) 
		        {
	            	changesMade();
		        }
	        });

			marCheck.setOnMouseClicked(mouseClickedEvent -> 
			{
		        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) 
		        {
	            	changesMade();
		        }
	        });

			aprCheck.setOnMouseClicked(mouseClickedEvent -> 
			{
		        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) 
		        {
	            	changesMade();
		        }
	        });

			mayCheck.setOnMouseClicked(mouseClickedEvent -> 
			{
		        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) 
		        {
	            	changesMade();
		        }
	        });

			junCheck.setOnMouseClicked(mouseClickedEvent -> 
			{
		        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) 
		        {
	            	changesMade();
		        }
	        });

			julCheck.setOnMouseClicked(mouseClickedEvent -> 
			{
		        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) 
		        {
	            	changesMade();
		        }
	        });

			augCheck.setOnMouseClicked(mouseClickedEvent -> 
			{
		        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) 
		        {
	            	changesMade();
		        }
	        });

			sepCheck.setOnMouseClicked(mouseClickedEvent -> 
			{
		        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) 
		        {
	            	changesMade();
		        }
	        });

			octCheck.setOnMouseClicked(mouseClickedEvent -> 
			{
		        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) 
		        {
	            	changesMade();
		        }
	        });

			novCheck.setOnMouseClicked(mouseClickedEvent -> 
			{
		        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) 
		        {
	            	changesMade();
		        }
	        });

			decCheck.setOnMouseClicked(mouseClickedEvent -> 
			{
		        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) 
		        {
	            	changesMade();
		        }
	        });

			firstName.set(dependent.getPerson().getFirstName());
			lastName.set(dependent.getPerson().getLastName());
			if(dependent.getPerson().getSsn() != null)
				ssnNum.set("XXX-XX-" + dependent.getPerson().getSsn().getUsrln());
			else
				ssnNum.set("N/A");
			if(dependent.getPerson().getDateOfBirth() != null)
				dobNum.set(Utils.getDateString(dependent.getPerson().getDateOfBirth()));
			else
				dobNum.set("N/A");
			janCheck.setSelected(irs1095cCi.isJanCovered());
			febCheck.setSelected(irs1095cCi.isFebCovered());
			marCheck.setSelected(irs1095cCi.isMarCovered());
			aprCheck.setSelected(irs1095cCi.isAprCovered());
			mayCheck.setSelected(irs1095cCi.isMayCovered());
			junCheck.setSelected(irs1095cCi.isJunCovered());
			julCheck.setSelected(irs1095cCi.isJulCovered());
			augCheck.setSelected(irs1095cCi.isAugCovered());
			sepCheck.setSelected(irs1095cCi.isSepCovered());
			octCheck.setSelected(irs1095cCi.isOctCovered());
			novCheck.setSelected(irs1095cCi.isNovCovered());
			decCheck.setSelected(irs1095cCi.isDecCovered());
			depId.set(dependent.getId().toString());
			activeCheck.setSelected(dependent.isActive());
		}
	}
}