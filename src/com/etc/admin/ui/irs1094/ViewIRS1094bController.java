package com.etc.admin.ui.irs1094;

import java.util.ArrayList;
import java.util.List;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.Irs1095b;

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

public class ViewIRS1094bController {
	
	@FXML
	private Label irsbTitleLabel;
	@FXML
	private CheckBox irsbClosedCheckBox;
	@FXML
	private TextField irsbClosedOnLabel;
	@FXML
	private TextField irsbClosedByLabel;
	@FXML
	private TextField irsbYearLabel;
	@FXML
	private Label irsb1095bListLabel;
	@FXML
	private GridPane irsb1095bListGrid;
	@FXML
	private ListView<HBoxCell> irsb1095bListView;
	@FXML
	private Button irsbEditButton;
	@FXML
	private Button irsbAdd1095bButton;
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
		update1094bData();
		load1095bs();
	}
	
	private void initControls() {
		// aesthetics
		irsb1095bListGrid.setStyle("-fx-background-color: " + Utils.uiColorListGridBackground);
		irsb1095bListLabel.setTextFill(Color.web(Utils.uiColorListGridLabel));
		
		//add handlers for listbox selection notification
		irsb1095bListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// offset one for the header row
				 DataManager.i().setIrs1094b(irsb1095bListView.getSelectionModel().getSelectedIndex() - 1);
            }
        });	
		
		//set functionality according to the user security level
		irsbEditButton.setDisable(!Utils.userCanEdit());
		irsbAdd1095bButton.setDisable(!Utils.userCanAdd());
	}

	private void update1094bData(){
		
		if (DataManager.i().mIrs1094b != null) {
			String sName = DataManager.i().mEmployer.getName() + " 1094b";
			Utils.updateControl(irsbTitleLabel,sName);	
			Utils.updateControl(irsbYearLabel,String.valueOf(DataManager.i().mIrs1094b.getTaxYear()));
			Utils.updateControl(irsbClosedCheckBox,DataManager.i().mIrs1094b.getTaxYear().isClosed());
			
			if (DataManager.i().mIrs1094b.getTaxYear().getClosedBy() != null) 
				Utils.updateControl(irsbClosedByLabel,DataManager.i().mIrs1094b.getTaxYear().getClosedBy().getFirstName() 
					+ " " + DataManager.i().mIrs1094b.getTaxYear().getClosedBy().getLastName());
			else
				Utils.updateControl(irsbClosedByLabel,"");
			
			Utils.updateControl(irsbYearLabel,String.valueOf(DataManager.i().mIrs1094b.getTaxYear().getYear()));
			Utils.updateControl(irsbClosedOnLabel,DataManager.i().mIrs1094b.getTaxYear().getClosedOn());			
			

			// core data
			Utils.updateControl(irsbCoreIdLabel,String.valueOf(DataManager.i().mIrs1094b.getId()));
			Utils.updateControl(irsbCoreActiveLabel,String.valueOf(DataManager.i().mIrs1094b.isActive()));
			Utils.updateControl(irsbCoreBODateLabel,DataManager.i().mIrs1094b.getBornOn());
			Utils.updateControl(irsbCoreLastUpdatedLabel,DataManager.i().mIrs1094b.getLastUpdated());
		}
	}
	
	//update the IRs1095b list
	private void load1095bs() {
	    
		if (DataManager.i().mIrs1094b.getIrs1095bs() != null)
		{
		    List<HBoxCell> irs1095bList = new ArrayList<>();
		    irs1095bList.add(new HBoxCell("Employee", "Year",true));

			for (Irs1095b a1095b : DataManager.i().mIrs1094b.getIrs1095bs()) {
				if (a1095b == null) continue;
				
				String name = a1095b.getEmployee().getPerson().getFirstName();
				name.concat(" ");
				name.concat(a1095b.getEmployee().getPerson().getLastName());
				irs1095bList.add(new HBoxCell(name,String.valueOf(a1095b.getTaxYear()), false));
			};	
			
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(irs1095bList);
			irsb1095bListView.setItems(myObservableList);		
			
			//update our list screen label
			irsb1095bListLabel.setText("Irs1095bs (total: " + String.valueOf(DataManager.i().mIrs1094b.getIrs1095bs().size()) + ")" );
		} else {
			irsb1095bListLabel.setText("Irs1095bs (total: 0)");			
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
		EtcAdmin.i().setScreen(ScreenType.IRS1094BEDIT, true);
	}	
	
	//this may not be used
	@FXML
	private void onAdd1095b(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.IRS1094BADD, true);
	}	
	
}
