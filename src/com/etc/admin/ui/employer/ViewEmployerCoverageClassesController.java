package com.etc.admin.ui.employer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.ui.plancoverageclass.ViewPlanCoverageClassController;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.CoverageGroup;

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
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewEmployerCoverageClassesController 
{

	@FXML
	private Label topLabel;
	@FXML
	private ListView<HBoxCovClassCell> dataList;
	@FXML
	private CheckBox inactiveCheck;
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
		showCoverageGroups();
	}
	
	private void initControls() 
	{

		dataList.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				viewSelectedCoverageClass();
            }
        });

	}
	
	private void showCoverageGroups() 
	{
		if(DataManager.i().mEmployer.getCoverageGroups() != null)
		{
		    List<HBoxCovClassCell> coverageClassList = new ArrayList<>();
			
		    if(DataManager.i().mEmployer.getCoverageGroups().size() > 0)
		    	coverageClassList.add(new HBoxCovClassCell(null, true));
			
		    for(CoverageGroup coverageClass: DataManager.i().mEmployer.getCoverageGroups()) {
				if(coverageClass == null) continue;
				if(coverageClass.isActive() == false && inactiveCheck.isSelected() == false) continue;
		    	if(coverageClass.isDeleted() == true && deletedCheck.isSelected() == false) continue;
			
				coverageClassList.add(new HBoxCovClassCell(coverageClass, false));
		    };	
			
			ObservableList<HBoxCovClassCell> myObservablePlanList = FXCollections.observableList(coverageClassList);
			dataList.setItems(myObservablePlanList);		
			
			//update our screen label
			topLabel.setText("Coverage Classes (total: " + String.valueOf(DataManager.i().mEmployer.getCoverageGroups().size()) + ")" );
		} else {
			topLabel.setText("Coverage Classess (total: 0)");			
		}
	}

	private void viewSelectedCoverageClass() 
	{
		DataManager.i().mCoverageClass = dataList.getSelectionModel().getSelectedItem().getCoverageClass();
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/plancoverageclass/ViewPlanCoverageClass.fxml"));
			Parent ControllerNode = loader.load();
			ViewPlanCoverageClassController controller = (ViewPlanCoverageClassController) loader.getController();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode)); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        
	        if(controller.changesMade == true) {
	        	showCoverageGroups();
	        }
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	
	
	@FXML
	private void onShowInactivesClick(ActionEvent event) {
		showCoverageGroups();
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
	
	public class HBoxCovClassCell extends HBox 
	{
         Label lblCol1 = new Label();
         Label lblCol2 = new Label();
         Label lblICHRA = new Label();
         CheckBox cbICHRA = new CheckBox();
         Label lblAffordable = new Label();
         CheckBox cbAffordable = new CheckBox();
         Label lblUnion = new Label();
         CheckBox cbUnion = new CheckBox();
         CoverageGroup covClass = null;
         
         public CoverageGroup getCoverageClass() {
        	 return covClass;
         }

         HBoxCovClassCell(CoverageGroup covClass, boolean headerRow) 
         {
              super();
           
              this.covClass = covClass;
              if(covClass != null) 
              {
            	  Utils.setHBoxLabel(lblCol1, 150, false, covClass.getName());
            	  Utils.setHBoxLabel(lblCol2, 275, false, covClass.getDescription());
            	  Utils.setHBoxCheckBox(cbICHRA, 100, covClass.isIchra());
            	  Utils.setHBoxCheckBox(cbAffordable, 100, covClass.isAffordable());
            	  Utils.setHBoxCheckBox(cbUnion, 100, covClass.isLaborUnion());
            	  if(covClass.isDeleted() == true) 
            	  {
            		  lblCol1.setTextFill(Color.RED);
            		  lblCol2.setTextFill(Color.RED);
            	  } else {
                	  if(covClass.isActive() == false) 
                	  {
                		  lblCol1.setTextFill(Color.BLUE);
                		  lblCol2.setTextFill(Color.BLUE);
                	  } else {
                		  lblCol1.setTextFill(Color.BLACK);
                		  lblCol2.setTextFill(Color.BLACK);
                	  }
            	  }
                  this.getChildren().addAll(lblCol1, lblCol2, cbICHRA, cbAffordable, cbUnion);
              } else {
            	  Utils.setHBoxLabel(lblCol1, 150, true, "Name");
            	  Utils.setHBoxLabel(lblCol2, 275, true, "Description");
            	  Utils.setHBoxLabel(lblICHRA, 100, true, "ICHRA");
            	  Utils.setHBoxLabel(lblAffordable, 100, true, "Affordable");
            	  Utils.setHBoxLabel(lblUnion, 100, true, "Union");
                  this.getChildren().addAll(lblCol1, lblCol2, lblICHRA, lblAffordable, lblUnion);
              }
         }
    }	
	
}


