package com.etc.admin.ui.planyearofferingplan;

import com.etc.admin.EtcAdmin;
import com.etc.admin.utils.Utils.ScreenType;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ViewPlanYearOfferingPlanController
{
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private TextField pyopMemberShareLabel;
	@FXML
	private TextField pyopPlanYearOfferingLabel;
	@FXML
	private TextField offerCodeLabel;
	@FXML
	private Label pyopPlanYearOfferingPlansLabel;
	@FXML
	private ListView<HBoxProviderCell> pyopPlanYearOfferingPlansListView;
	@FXML
	private Button pyopEditButton;
	@FXML
	private Button close;
	@FXML
	private Label pyopCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		//the user hasn't selected anything yet, so clear the screen
		clearScreen();

		//load the user list		
		loadPlanYearOfferingPlans();
	}
	
	private void clearScreen() {
		
		pyopMemberShareLabel.setPromptText("Please Select a Plan Sponsor");
		pyopMemberShareLabel.setText(" ");
		pyopPlanYearOfferingLabel.setText(" ");
		
		//disable the edit button until something is selected
		//pyopEditButton.setDisable(true);
	}
	
	private void loadPlanYearOfferingPlans() {
	/*	//update the mPlanProviders list
		if (DataManager.i().mPlanProviders != null) {
			
			List<HBoxProviderCell> list = new ArrayList<>();
			list.add(new HBoxProviderCell("Name", "Description", null, 0));
			
			int i = 0;
			for (PlanProvider planProvider : DataManager.i().mPlanProviders) {
				
				list.add(new HBoxProviderCell(planProvider.getName(), planProvider.getDescription(), "View", i));
				i++;
			};	
			
	        ObservableList<HBoxProviderCell> myObservableList = FXCollections.observableList(list);
	        pyopPlanYearOfferingPlansListView.setItems(myObservableList);		
			
	        //update our provider screen label
	        pyopPlanYearOfferingPlansLabel.setText("Plan Carriers (total: " + String.valueOf(DataManager.i().mPlanProviders.size()) + ")" );
		} else {
			pyopPlanYearOfferingPlansLabel.setText("Plan Carriers (total: 0)");			
		} */
	}
	
	public void setPlanYearOfferingPlan(int nProviderID) {
	/*	
		if (DataManager.i().mPlanProviders != null){
			DataManager.i().setPlanProvider(nProviderID);
			
			if (DataManager.i().mPlanProviders != null) {
				//enable the edit button
				pyopEditButton.setDisable(false);

				Utils.updateControl(pyopMemberShareLabel,DataManager.i().mPlanProvider.getName());
				Utils.updateControl(pyopPlanYearOfferingLabel,DataManager.i().mPlanProvider.getDescription());
				Utils.updateControl(pyopPlanLabel,DataManager.i().mPlanProvider.getDescription());
				
				//core data read only
				Utils.updateControl(pyopCoreIdLabel,String.valueOf(DataManager.i().mPlanProvider.getId()));
				Utils.updateControl(pyopCoreActiveLabel,String.valueOf(DataManager.i().mPlanProvider.isActive()));
				Utils.updateControl(pyopCoreBODateLabel,DataManager.i().mPlanProvider.getBornOn());
				Utils.updateControl(pyopCoreLastUpdatedLabel,DataManager.i().mPlanProvider.getLastUpdated());
			}
		}	 */	
	}
	
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
            			  setPlanYearOfferingPlan(Integer.parseInt(btnView.getId()));
            		  }
            	  });
              }
              
         }
    }	
	
	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PLANYEAROFFERINGPLANEDIT, true);
	}	
	
	@FXML
	private void onClose(ActionEvent event) {
		Stage stage = (Stage) anchorPane.getScene().getWindow();
		stage.close();
	}	
	
}
