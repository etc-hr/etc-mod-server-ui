package com.etc.admin.ui.irs1095;

import java.util.ArrayList;
import java.util.List;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.Irs1095b;
import com.etc.corvetto.entities.Irs1095bCI;

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

public class ViewIRS1095bController {
	
	@FXML
	private Label irsbTitleLabel;
	@FXML
	private TextField irsbScenarioIdLabel;
	@FXML
	private TextField irsbOriginTypeLabel;
	@FXML
	private CheckBox irsbSelfInsuredCheckBox;
	@FXML
	private CheckBox irsbAnnCoveredCheckBox;
	@FXML
	private CheckBox irsbJanCoveredCheckBox;
	@FXML
	private CheckBox irsbFebCoveredCheckBox;
	@FXML
	private CheckBox irsbMarCoveredCheckBox;
	@FXML
	private CheckBox irsbAprCoveredCheckBox;
	@FXML
	private CheckBox irsbMayCoveredCheckBox;
	@FXML
	private CheckBox irsbJunCoveredCheckBox;
	@FXML
	private CheckBox irsbJulCoveredCheckBox;
	@FXML
	private CheckBox irsbAugCoveredCheckBox;
	@FXML
	private CheckBox irsbSepCoveredCheckBox;
	@FXML
	private CheckBox irsbOctCoveredCheckBox;
	@FXML
	private CheckBox irsbNovCoveredCheckBox;
	@FXML
	private CheckBox irsbDecCoveredCheckBox;
	@FXML
	private Label irsb1095bSecListLabel;
	@FXML
	private GridPane irsb1095bSecListGrid;
	@FXML
	private ListView<HBoxCell> irsb1095bSecListView;
	@FXML
	private Button irsbEditButton;
	@FXML
	private Button irsbAdd1095bSecButton;
	@FXML
	private Label irsbCoreIdLabel;
	@FXML
	private Label irsbCoreActiveLabel;
	@FXML
	private Label irsbCoreBODateLabel;
	@FXML
	private Label irsbCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		update1095bData();
		load1095bCoveredSecondaries();
	}
	
	private void initControls() {
		// aesthetics
		irsb1095bSecListGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		irsb1095bSecListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		
		//add handlers for listbox selection notification
		irsb1095bSecListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
				 DataManager.i().setIrs1095bCoveredSecondary(irsb1095bSecListView.getSelectionModel().getSelectedIndex() - 1);
            }
        });	
		
		//set functionality according to the user security level
		irsbEditButton.setDisable(!Utils.userCanEdit());
		irsbAdd1095bSecButton.setDisable(!Utils.userCanAdd());
	}

	private void update1095bData(){
		
		Irs1095b irsb = DataManager.i().mIrs1095b;
		if (irsb != null) {
			String sName = "";
			if (irsb.getEmployee()!= null) {
				sName = irsb.getEmployee().getPerson().getFirstName();
				sName.concat(" ");
				sName.concat(irsb.getEmployee().getPerson().getLastName());
				sName.concat(" 1095b");
			}

			Utils.updateControl(irsbTitleLabel,sName);	
			Utils.updateControl(irsbOriginTypeLabel,irsb.getHealthCoverageOriginType().toString());	
			Utils.updateControl(irsbSelfInsuredCheckBox,irsb.isSelfInsured());	
			Utils.updateControl(irsbJanCoveredCheckBox,irsb.isJanCovered());	
			Utils.updateControl(irsbFebCoveredCheckBox,irsb.isFebCovered());	
			Utils.updateControl(irsbMarCoveredCheckBox,irsb.isMarCovered());	
			Utils.updateControl(irsbAprCoveredCheckBox,irsb.isAprCovered());	
			Utils.updateControl(irsbMayCoveredCheckBox,irsb.isMayCovered());	
			Utils.updateControl(irsbJunCoveredCheckBox,irsb.isJunCovered());	
			Utils.updateControl(irsbJulCoveredCheckBox,irsb.isJulCovered());	
			Utils.updateControl(irsbAugCoveredCheckBox,irsb.isAugCovered());	
			Utils.updateControl(irsbSepCoveredCheckBox,irsb.isSepCovered());	
			Utils.updateControl(irsbOctCoveredCheckBox,irsb.isOctCovered());	
			Utils.updateControl(irsbNovCoveredCheckBox,irsb.isNovCovered());	
			Utils.updateControl(irsbDecCoveredCheckBox,irsb.isDecCovered());	

			// core data
			Utils.updateControl(irsbCoreIdLabel,String.valueOf(DataManager.i().mIrs1094b.getId()));
			Utils.updateControl(irsbCoreActiveLabel,String.valueOf(DataManager.i().mIrs1094b.isActive()));
			Utils.updateControl(irsbCoreBODateLabel,DataManager.i().mIrs1094b.getBornOn());
			Utils.updateControl(irsbCoreLastUpdatedLabel,DataManager.i().mIrs1094b.getLastUpdated());
		}
	}
	
	//update the IRs1095bSec list
	private void load1095bCoveredSecondaries() {
	    
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
			irsb1095bSecListView.setItems(myObservableList);		
			
			//update our list screen label
			irsb1095bSecListLabel.setText("Irs1095b Secondaries (total: " + String.valueOf(DataManager.i().mIrs1094b.getIrs1095bs().size()) + ")" );
		} else {
			irsb1095bSecListLabel.setText("Irs1095b Secondaries (total: 0)");			
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
		EtcAdmin.i().setScreen(ScreenType.IRS1095BEDIT, true);
	}	
	
	//this may not be used
	@FXML
	private void onAdd1095b(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.IRS1095BADD, true);
	}	
	
}
