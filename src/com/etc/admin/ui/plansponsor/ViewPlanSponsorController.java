package com.etc.admin.ui.plansponsor;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ViewPlanSponsorController {
	
	@FXML
	private TextField spnsNameLabel;
	@FXML
	private TextField spnsDescriptionLabel;
	@FXML
	private TextField spnsPhoneLabel;
	@FXML
	private TextField spnsPhoneTypeLabel;
	@FXML
	private TextField spnsTimezoneLabel;
	@FXML
	private TextField spnsEmailLabel;
	@FXML
	private TextField spnsEmailTypeLabel;
	@FXML
	private TextField spnsStreetLabel;
	@FXML
	private TextField spnsUnitLabel;
	@FXML
	private TextField spnsCityLabel;
	@FXML
	private TextField spnsStateLabel;
	@FXML
	private TextField spnsZipLabel;
	@FXML
	private TextField spnsZipsPlus4Label;
	@FXML
	private TextField spnsCountryLabel;
	@FXML
	private TextField spnsQuarterLabel;
	@FXML
	private TextField spnsDepartmentLabel;
	@FXML
	private TextField spnsProvinceLabel;
	@FXML
	private CheckBox spnsVerifiedCheckBox;
	@FXML
	private TextField spnsTINLabel;
	@FXML
	private Button spnsEditButton;
	@FXML
	private Label spnsCoreIdLabel;
	@FXML
	private Label spnsCoreActiveLabel;
	@FXML
	private Label spnsCoreBODateLabel;
	@FXML
	private Label spnsCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		//the user hasn't selected anything yet, so clear the screen
		clearScreen();
	}
	
	private void clearScreen() {
		
		spnsNameLabel.setPromptText("Please Select a Plan Sponsor Above");
		spnsDescriptionLabel.setText(" ");
		spnsCoreIdLabel.setText(" ");
		spnsCoreActiveLabel.setText(" ");
		spnsCoreBODateLabel.setText(" ");
		spnsCoreLastUpdatedLabel.setText(" ");		

		spnsPhoneLabel.setText(" ");
		spnsPhoneTypeLabel.setText(" ");
		spnsTimezoneLabel.setText(" ");
		spnsEmailLabel.setText(" ");
		spnsEmailTypeLabel.setText(" ");
		spnsStreetLabel.setText(" ");
		spnsUnitLabel.setText(" ");
		spnsCityLabel.setText(" ");
		spnsStateLabel.setText(" ");
		spnsZipLabel.setText(" ");
		spnsZipsPlus4Label.setText(" ");
		spnsTINLabel.setText(" ");
		spnsCountryLabel.setText(" ");
		spnsQuarterLabel.setText(" ");
		spnsDepartmentLabel.setText(" ");
		spnsProvinceLabel.setText(" ");
		spnsVerifiedCheckBox.setText(" ");
		
		//disable the edit button until something is selected
		//spnsEditButton.setDisable(true);
	}
	
/*
	public void setProvider(int nProviderID) {
		
		if (DataManager.i().mPlanProviders != null){
			DataManager.i().setPlanProvider(nProviderID);
			
			if (DataManager.i().mPlanProviders != null) {
				//enable the edit button
				spnsEditButton.setDisable(false);

				Utils.updateControl(spnsNameLabel,DataManager.i().mPlanProvider.getName());
				Utils.updateControl(spnsDescriptionLabel,DataManager.i().mPlanProvider.getDescription());
				
				//core data read only
				Utils.updateControl(spnsCoreIdLabel,String.valueOf(DataManager.i().mPlanProvider.getId()));
				Utils.updateControl(spnsCoreActiveLabel,String.valueOf(DataManager.i().mPlanProvider.isActive()));
				Utils.updateControl(spnsCoreBODateLabel,DataManager.i().mPlanProvider.getBornOn());
				Utils.updateControl(spnsCoreLastUpdatedLabel,DataManager.i().mPlanProvider.getLastUpdated());
			}
		}		
	}
*/	
	//extending the listview for our additional controls
	public class HBoxProviderCell extends HBox {
         Label lblName = new Label();
         Label lblDescription = new Label();
         CheckBox cbAdmin = new CheckBox();
         
         Button btnView = new Button();

         HBoxProviderCell(String sName, String sDescription, String sButtonText, int nButtonID) {
              super();

              //clean up any nulls
              if (sName == null) sName = "";
              if (sDescription == null) sDescription = "";
 
              if (sName.contains("null") ) sName = "";
              if (sDescription.contains("null") ) sDescription = "";
              
              lblName.setText(sName);
              lblName.setMinWidth(300);
              lblName.setMaxWidth(300);
              lblName.setPrefWidth(300);
              HBox.setHgrow(lblName, Priority.ALWAYS);

              lblDescription.setText(sDescription);
              lblDescription.setMinWidth(360);
              lblDescription.setMaxWidth(360);
              lblDescription.setPrefWidth(360);
              HBox.setHgrow(lblDescription, Priority.ALWAYS);

              // if the button text is null, this is a header row
              if (sButtonText == null) {
            	  lblName.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblDescription.setFont(Font.font(null, FontWeight.BOLD, 13));
                  this.getChildren().addAll(lblName, lblDescription );
              }else {
              	  btnView.setText(sButtonText);
            	  btnView.setId(String.valueOf(nButtonID));
            	  this.getChildren().addAll(lblName, lblDescription, btnView);
            	  
            	  //add an event handler for this button
            	  btnView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            		  
            		  @Override
            		  public void handle(MouseEvent event) {
            			  // let the application load the current secondary
            			 // setProvider(Integer.parseInt(btnView.getId()));
            		  }
            	  });
              }
              
         }
    }	
	
	
	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PLANPROVIDEREDIT, true);
	}	
	
}
