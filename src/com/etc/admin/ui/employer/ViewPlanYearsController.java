package com.etc.admin.ui.employer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.CoverageGroup;
import com.etc.corvetto.entities.PlanYear;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewPlanYearsController 
{

	@FXML
	private Label topLabel;
	@FXML
	private ListView<HBoxPlanYearCell> dataList;
	@FXML
	private CheckBox InactiveCheck;
	@FXML
	private CheckBox deletedCheck;
	
	int waitingToComplete = 0;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	@FXML
	public void initialize() 
	{
		initControls();
		showPlanYears();
	}
	
	private void initControls() 
	{
		dataList.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
            	viewSelectedPlanYear();
            }
        });
	}
	
	private void showPlanYears() 
	{
		if(DataManager.i().mEmployer.getPlanYears() != null)
		{
		    List<HBoxPlanYearCell> planYearList = new ArrayList<>();
			
		    if(DataManager.i().mEmployer.getPlanYears().size() > 0)
		    	planYearList.add(new HBoxPlanYearCell(null));
			
		    for(PlanYear planYear: DataManager.i().mEmployer.getPlanYears()) {
				if(planYear == null) continue;
				if(planYear.isActive() == false && InactiveCheck.isSelected() == false) continue;
				if(planYear.isDeleted() == true && deletedCheck.isSelected() == false) continue;
			
				planYearList.add(new HBoxPlanYearCell(planYear));
		    };	
			
			ObservableList<HBoxPlanYearCell> myObservablePlanList = FXCollections.observableList(planYearList);
			dataList.setItems(myObservablePlanList);		
			
			//update our screen label
			topLabel.setText("Plan Years (total: " + String.valueOf(DataManager.i().mEmployer.getPlanYears().size()) + ")" );
		} else {
			topLabel.setText("Plan Years (total: 0)");			
		}
	}

	private void viewSelectedPlanYear() {
		// offset one for the header row
		DataManager.i().mPlanYear = DataManager.i().mEmployer.getPlanYears().get(dataList.getSelectionModel().getSelectedIndex() - 1);  
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/planyear/ViewPlanYear.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	
	
	@FXML
	private void onShowInactivesClick(ActionEvent event) {

	}	
	
	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) dataList.getScene().getWindow();
		stage.close();
	}
	
	public class HBoxPlanYearCell extends HBox 
	{
         Label lblStartDate = new Label();
         Label lblEndDate = new Label();
         Label lblInsuranceType = new Label();
         Label lblImplemented = new Label();
         Label lblMemberShare = new Label();
         Label lblAffordable = new Label();
         CheckBox cbImplemented = new CheckBox();
         CheckBox cbAffordable = new CheckBox();
         PlanYear planYear = null;
         
         public PlanYear getPlanYear() {
        	 return planYear;
         }

         HBoxPlanYearCell(PlanYear planYear) 
         {
              super();
           
              this.planYear = planYear;
              if(planYear != null) 
              {
            	  Utils.setHBoxLabel(lblStartDate, 125, false, planYear.getStartDate());
            	  Utils.setHBoxLabel(lblEndDate, 125, false, planYear.getEndDate());
            	  if (planYear.getInsuranceType() != null)
            		  Utils.setHBoxLabel(lblInsuranceType, 250, false, planYear.getInsuranceType().toString());
            	  else
        			  Utils.setHBoxLabel(lblInsuranceType, 250, false, "");

            	  Utils.setHBoxCheckBox(cbImplemented, 125, planYear.isImplemented());
            	  if (planYear.getMemberShare() != null)
            		  Utils.setHBoxLabel(lblMemberShare, 125, false, planYear.getMemberShare());
            	  else
            		  Utils.setHBoxLabel(lblMemberShare, 125, false, "");
            	  
            	  Utils.setHBoxCheckBox(cbAffordable, 125, planYear.isAffordable());
            	  if(planYear.isDeleted() == true) 
            	  {
            		  lblStartDate.setTextFill(Color.RED);
            		  lblEndDate.setTextFill(Color.RED);
            		  lblInsuranceType.setTextFill(Color.RED);
            		  lblMemberShare.setTextFill(Color.RED);
            	  } else {
                	  if(planYear.isActive() == false) 
                	  {
                		  lblStartDate.setTextFill(Color.BLUE);
                		  lblEndDate.setTextFill(Color.BLUE);
                		  lblInsuranceType.setTextFill(Color.BLUE);
                		  lblMemberShare.setTextFill(Color.BLUE);
                	  } else {
                		  lblStartDate.setTextFill(Color.BLACK);
                		  lblEndDate.setTextFill(Color.BLACK);
                		  lblInsuranceType.setTextFill(Color.BLACK);
                		  lblMemberShare.setTextFill(Color.BLACK);
                	  }
            	  }
                  this.getChildren().addAll(lblStartDate, lblEndDate, lblInsuranceType, cbImplemented, lblMemberShare, cbAffordable );
              } else {
            	  Utils.setHBoxLabel(lblStartDate, 125, true, "Start Date");
            	  Utils.setHBoxLabel(lblEndDate, 125, true, "End Date");
            	  Utils.setHBoxLabel(lblInsuranceType, 250, true, "Insurance Type");
            	  Utils.setHBoxLabel(lblImplemented, 125, true, "Implemented");
            	  Utils.setHBoxLabel(lblMemberShare, 125, true, "Member Share");
            	  Utils.setHBoxLabel(lblAffordable, 125, true, "Affordable");
                  this.getChildren().addAll(lblStartDate, lblEndDate, lblInsuranceType, lblImplemented, lblMemberShare, lblAffordable );
              }
         }
    }	
	

	
	
	
}


