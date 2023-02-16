package com.etc.admin.ui.irs1095;

import java.util.ArrayList;
import java.util.List;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.Irs1095bCI;
import com.etc.corvetto.entities.Irs1095c;

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

public class ViewIRS1095cController {
	
	@FXML
	private Label irscTitleLabel;
	@FXML
	private CheckBox irscAnnCoveredCheckBox;
	@FXML
	private CheckBox irscJanCoveredCheckBox;
	@FXML
	private CheckBox irscFebCoveredCheckBox;
	@FXML
	private CheckBox irscMarCoveredCheckBox;
	@FXML
	private CheckBox irscAprCoveredCheckBox;
	@FXML
	private CheckBox irscMayCoveredCheckBox;
	@FXML
	private CheckBox irscJunCoveredCheckBox;
	@FXML
	private CheckBox irscJulCoveredCheckBox;
	@FXML
	private CheckBox irscAugCoveredCheckBox;
	@FXML
	private CheckBox irscSepCoveredCheckBox;
	@FXML
	private CheckBox irscOctCoveredCheckBox;
	@FXML
	private CheckBox irscNovCoveredCheckBox;
	@FXML
	private CheckBox irscDecCoveredCheckBox;
	@FXML
	private TextField irscAnnOfferCodeField;
	@FXML
	private TextField irscJanOfferCodeField;
	@FXML
	private TextField irscFebOfferCodeField;
	@FXML
	private TextField irscMarOfferCodeField;
	@FXML
	private TextField irscAprOfferCodeField;
	@FXML
	private TextField irscMayOfferCodeField;
	@FXML
	private TextField irscJunOfferCodeField;
	@FXML
	private TextField irscJulOfferCodeField;
	@FXML
	private TextField irscAugOfferCodeField;
	@FXML
	private TextField irscSepOfferCodeField;
	@FXML
	private TextField irscOctOfferCodeField;
	@FXML
	private TextField irscNovOfferCodeField;
	@FXML
	private TextField irscDecOfferCodeField;
	@FXML
	private TextField irscAnnEEShareField;
	@FXML
	private TextField irscJanEEShareField;
	@FXML
	private TextField irscFebEEShareField;
	@FXML
	private TextField irscMarEEShareField;
	@FXML
	private TextField irscAprEEShareField;
	@FXML
	private TextField irscMayEEShareField;
	@FXML
	private TextField irscJunEEShareField;
	@FXML
	private TextField irscJulEEShareField;
	@FXML
	private TextField irscAugEEShareField;
	@FXML
	private TextField irscSepEEShareField;
	@FXML
	private TextField irscOctEEShareField;
	@FXML
	private TextField irscNovEEShareField;
	@FXML
	private TextField irscDecEEShareField;
	@FXML
	private TextField irscAnnSafeHarborCodeField;
	@FXML
	private TextField irscJanSafeHarborCodeField;
	@FXML
	private TextField irscFebSafeHarborCodeField;
	@FXML
	private TextField irscMarSafeHarborCodeField;
	@FXML
	private TextField irscAprSafeHarborCodeField;
	@FXML
	private TextField irscMaySafeHarborCodeField;
	@FXML
	private TextField irscJunSafeHarborCodeField;
	@FXML
	private TextField irscJulSafeHarborCodeField;
	@FXML
	private TextField irscAugSafeHarborCodeField;
	@FXML
	private TextField irscSepSafeHarborCodeField;
	@FXML
	private TextField irscOctSafeHarborCodeField;
	@FXML
	private TextField irscNovSafeHarborCodeField;
	@FXML
	private TextField irscDecSafeHarborCodeField;
	@FXML
	private Label irsc1095cSecListLabel;
	@FXML
	private GridPane irsc1095cSecListGrid;
	@FXML
	private ListView<HBoxCell> irsc1095cSecListView;
	@FXML
	private Button irscEditButton;
	@FXML
	private Button irscAdd1095bSecButton;
	@FXML
	private Label irscCoreIdLabel;
	@FXML
	private Label irscCoreActiveLabel;
	@FXML
	private Label irscCoreBODateLabel;
	@FXML
	private Label irscCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		update1095cData();
		load1095cCoveredSecondaries();
	}
	
	private void initControls() {
		// aesthetics
		irsc1095cSecListGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		irsc1095cSecListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		
		//add handlers for listbox selection notification
		irsc1095cSecListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
				 DataManager.i().setIrs1095cCoveredSecondary(irsc1095cSecListView.getSelectionModel().getSelectedIndex() - 1);
            }
        });	
		
		//set functionality according to the user security level
		irscEditButton.setDisable(!Utils.userCanEdit());
		irscAdd1095bSecButton.setDisable(!Utils.userCanAdd());
	}

	private void update1095cData(){
		
		Irs1095c irsc = DataManager.i().mIrs1095c;
		if (irsc != null) {
			String sName = "";
			if (irsc.getEmployee()!= null) {
				sName = irsc.getEmployee().getPerson().getFirstName();
				sName.concat(" ");
				sName.concat(irsc.getEmployee().getPerson().getLastName());
				sName.concat(" 1095c");
			}

			Utils.updateControl(irscTitleLabel,sName);	

			Utils.updateControl(irscJanCoveredCheckBox,irsc.isJanCovered());	
			Utils.updateControl(irscFebCoveredCheckBox,irsc.isFebCovered());	
			Utils.updateControl(irscMarCoveredCheckBox,irsc.isMarCovered());	
			Utils.updateControl(irscAprCoveredCheckBox,irsc.isAprCovered());	
			Utils.updateControl(irscMayCoveredCheckBox,irsc.isMayCovered());	
			Utils.updateControl(irscJunCoveredCheckBox,irsc.isJunCovered());	
			Utils.updateControl(irscJulCoveredCheckBox,irsc.isJulCovered());	
			Utils.updateControl(irscAugCoveredCheckBox,irsc.isAugCovered());	
			Utils.updateControl(irscSepCoveredCheckBox,irsc.isSepCovered());	
			Utils.updateControl(irscOctCoveredCheckBox,irsc.isOctCovered());	
			Utils.updateControl(irscNovCoveredCheckBox,irsc.isNovCovered());	
			Utils.updateControl(irscDecCoveredCheckBox,irsc.isDecCovered());	

			Utils.updateControl(irscJanOfferCodeField,irsc.getJanOfferCode().toString());	
			Utils.updateControl(irscFebOfferCodeField,irsc.getFebOfferCode().toString());	
			Utils.updateControl(irscMarOfferCodeField,irsc.getMarOfferCode().toString());	
			Utils.updateControl(irscAprOfferCodeField,irsc.getAprOfferCode().toString());	
			Utils.updateControl(irscMayOfferCodeField,irsc.getMayOfferCode().toString());	
			Utils.updateControl(irscJunOfferCodeField,irsc.getJunOfferCode().toString());	
			Utils.updateControl(irscJulOfferCodeField,irsc.getJulOfferCode().toString());	
			Utils.updateControl(irscAugOfferCodeField,irsc.getAugOfferCode().toString());	
			Utils.updateControl(irscSepOfferCodeField,irsc.getSepOfferCode().toString());	
			Utils.updateControl(irscOctOfferCodeField,irsc.getOctOfferCode().toString());	
			Utils.updateControl(irscNovOfferCodeField,irsc.getNovOfferCode().toString());	
			Utils.updateControl(irscDecOfferCodeField,irsc.getDecOfferCode().toString());	

			Utils.updateControl(irscJanEEShareField,String.valueOf(irsc.getJanEEShare()));	
			Utils.updateControl(irscFebEEShareField,String.valueOf(irsc.getFebEEShare()));	
			Utils.updateControl(irscMarEEShareField,String.valueOf(irsc.getMarEEShare()));	
			Utils.updateControl(irscAprEEShareField,String.valueOf(irsc.getAprEEShare()));	
			Utils.updateControl(irscMayEEShareField,String.valueOf(irsc.getMayEEShare()));	
			Utils.updateControl(irscJunEEShareField,String.valueOf(irsc.getJunEEShare()));	
			Utils.updateControl(irscJulEEShareField,String.valueOf(irsc.getJulEEShare()));	
			Utils.updateControl(irscAugEEShareField,String.valueOf(irsc.getAugEEShare()));	
			Utils.updateControl(irscSepEEShareField,String.valueOf(irsc.getSepEEShare()));	
			Utils.updateControl(irscOctEEShareField,String.valueOf(irsc.getOctEEShare()));	
			Utils.updateControl(irscNovEEShareField,String.valueOf(irsc.getNovEEShare()));	
			Utils.updateControl(irscDecEEShareField,String.valueOf(irsc.getDecEEShare()));	

			Utils.updateControl(irscJanSafeHarborCodeField,irsc.getJanSafeHarborCode().toString());	
			Utils.updateControl(irscFebSafeHarborCodeField,irsc.getFebSafeHarborCode().toString());	
			Utils.updateControl(irscMarSafeHarborCodeField,irsc.getMarSafeHarborCode().toString());	
			Utils.updateControl(irscAprSafeHarborCodeField,irsc.getAprSafeHarborCode().toString());	
			Utils.updateControl(irscMaySafeHarborCodeField,irsc.getMaySafeHarborCode().toString());	
			Utils.updateControl(irscJunSafeHarborCodeField,irsc.getJunSafeHarborCode().toString());	
			Utils.updateControl(irscJulSafeHarborCodeField,irsc.getJulSafeHarborCode().toString());	
			Utils.updateControl(irscAugSafeHarborCodeField,irsc.getAugSafeHarborCode().toString());	
			Utils.updateControl(irscSepSafeHarborCodeField,irsc.getSepSafeHarborCode().toString());	
			Utils.updateControl(irscOctSafeHarborCodeField,irsc.getOctSafeHarborCode().toString());	
			Utils.updateControl(irscNovSafeHarborCodeField,irsc.getNovSafeHarborCode().toString());	
			Utils.updateControl(irscDecSafeHarborCodeField,irsc.getDecSafeHarborCode().toString());	

			// core data
			Utils.updateControl(irscCoreIdLabel,String.valueOf(DataManager.i().mIrs1094b.getId()));
			Utils.updateControl(irscCoreActiveLabel,String.valueOf(DataManager.i().mIrs1094b.isActive()));
			Utils.updateControl(irscCoreBODateLabel,DataManager.i().mIrs1094b.getBornOn());
			Utils.updateControl(irscCoreLastUpdatedLabel,DataManager.i().mIrs1094b.getLastUpdated());
		}
	}
	
	//update the IRs1095cSec list
	private void load1095cCoveredSecondaries() {
	    
		if (DataManager.i().mIrs1095b.getIrs1095bCIs() != null)
		{
		    List<HBoxCell> irs1095bSecList = new ArrayList<>();
		    irs1095bSecList.add(new HBoxCell("Name", "Year",true));

			for (Irs1095bCI a1095bSec : DataManager.i().mIrs1095b.getIrs1095bCIs() ) {
				if (a1095bSec == null) continue;
				
				String name = a1095bSec.getDependent().getPerson().getFirstName();
				name.concat(" ");
				name.concat(a1095bSec.getDependent().getPerson().getLastName());
				irs1095bSecList.add(new HBoxCell(name,String.valueOf(a1095bSec.isSepCovered()), false));
			};	
			
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(irs1095bSecList);
			irsc1095cSecListView.setItems(myObservableList);		
			
			//update our list screen label
			irsc1095cSecListLabel.setText("Irs1095b Secondaries (total: " + String.valueOf(DataManager.i().mIrs1094b.getIrs1095bs().size()) + ")" );
		} else {
			irsc1095cSecListLabel.setText("Irs1095b Secondaries (total: 0)");			
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
		EtcAdmin.i().setScreen(ScreenType.IRS1095CEDIT, true);
	}	
	
	//this may not be used
	@FXML
	private void onAdd1095b(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.IRS1095CADD, true);
	}	
	
}
