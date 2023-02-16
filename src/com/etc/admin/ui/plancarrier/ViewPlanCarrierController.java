package com.etc.admin.ui.plancarrier;

import java.util.ArrayList;
import java.util.List;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ViewPlanCarrierController {
	
	@FXML
	private TextField carrNameLabel;
	@FXML
	private TextField carrDescriptionLabel;
	@FXML
	private Label carrPlansLabel;
	@FXML
	private ListView<HBoxProviderCell> carrPlansListView;
	@FXML
	private Button carrEditButton;
	@FXML
	private Label carrCoreIdLabel;
	@FXML
	private Label carrCoreActiveLabel;
	@FXML
	private Label carrCoreBODateLabel;
	@FXML
	private Label carrCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		//the user hasn't selected anything yet, so clear the screen
		clearScreen();

	}
	
	private void clearScreen() {
		
		carrNameLabel.setPromptText("Please Select a Plan Carrier Above");
		carrDescriptionLabel.setText(" ");
		carrCoreIdLabel.setText(" ");
		carrCoreActiveLabel.setText(" ");
		carrCoreBODateLabel.setText(" ");
		carrCoreLastUpdatedLabel.setText(" ");		
		
		//disable the edit button until something is selected
		//carrEditButton.setDisable(true);
	}
	
	/*
	private void loadPlans() {
		//update the mPlanProviders list
		if (DataManager.i().mPlanProviders != null) {
			
			List<HBoxProviderCell> list = new ArrayList<>();
			list.add(new HBoxProviderCell("Name", "Description", null, 0));
			
			int i = 0;
			for (PlanProvider planProvider : DataManager.i().mPlanProviders) {
				
				list.add(new HBoxProviderCell(planProvider.getName(), planProvider.getDescription(), "View", i));
				i++;
			};	
			
	        ObservableList<HBoxProviderCell> myObservableList = FXCollections.observableList(list);
	        carrPlansListView.setItems(myObservableList);		
			
	        //update our provider screen label
	        carrPlansLabel.setText("Plan Carriers (total: " + String.valueOf(DataManager.i().mPlanProviders.size()) + ")" );
		} else {
			carrPlansLabel.setText("Plan Carriers (total: 0)");			
		}
	}
	*/
	
	/*
	public void setProvider(int nProviderID) {
		
		if (DataManager.i().mPlanProviders != null){
			DataManager.i().setPlanProvider(nProviderID);
			
			if (DataManager.i().mPlanProviders != null) {
				//enable the edit button
				carrEditButton.setDisable(false);

				Utils.updateControl(carrNameLabel,DataManager.i().mPlanProvider.getName());
				Utils.updateControl(carrDescriptionLabel,DataManager.i().mPlanProvider.getDescription());
				
				//core data read only
				Utils.updateControl(carrCoreIdLabel,String.valueOf(DataManager.i().mPlanProvider.getId()));
				Utils.updateControl(carrCoreActiveLabel,String.valueOf(DataManager.i().mPlanProvider.isActive()));
				Utils.updateControl(carrCoreBODateLabel,DataManager.i().mPlanProvider.getBornOn());
				Utils.updateControl(carrCoreLastUpdatedLabel,DataManager.i().mPlanProvider.getLastUpdated());
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
            			  // let the acarrlication load the current secondary
            			//  setProvider(Integer.parseInt(btnView.getId()));
            		  }
            	  });
              }
              
         }
    }	
	
	
	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PLANYEAROFFERINGPLANEDIT, true);
	}	
	
}
