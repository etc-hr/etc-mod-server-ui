package com.etc.admin.ui.employer;

import java.util.ArrayList;
import java.util.List;

import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.Location;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ViewLocationsController 
{

	@FXML
	private Label topLabel;
	@FXML
	private ListView<HBox2ColCell> dataList;
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
		showLocations();
	}
	
	private void initControls() 
	{

	}
	
	private void showLocations()
	{
		if(DataManager.i().mEmployer.getLocations() != null && DataManager.i().mEmployer.getLocations() .size() > 0)
		{
		    List<HBox2ColCell> locationList = new ArrayList<>();
		    locationList.add(new HBox2ColCell("Date", "Name", true));

			for(Location location : DataManager.i().mEmployer.getLocations()) 
			{
				if(location == null) continue;
				if(location.isActive() == false && InactiveCheck.isSelected() == false) continue;
				if(location.isDeleted() == true && deletedCheck.isSelected() == false) continue;

				locationList.add(new HBox2ColCell(Utils.getDateString(location.getLastUpdated()), location.getName(), false));
			};	
			
			ObservableList<HBox2ColCell> myObservableDepartmentList = FXCollections.observableList(locationList);
			dataList.setItems(myObservableDepartmentList);		
			
			//update our label
			topLabel.setText("Locations (total: " + String.valueOf(DataManager.i().mEmployer.getLocations().size()) + ")" );
		} else 
			topLabel.setText("Locations (total: 0)");			
		
	}

	
	@FXML
	private void onShowInactivesClick(ActionEvent event) {
		showLocations();
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
	
	public class HBox2ColCell extends HBox 
	{
         Label lblCol1 = new Label();
         Label lblCol2 = new Label();

         HBox2ColCell(String sCol1, String sCol2, boolean headerRow)
         {
              super();
           
              if(sCol1 == null ) sCol1 = "";
              if(sCol2 == null ) sCol2 = "";
              
              if(sCol1.contains("null")) sCol1 = "";
              if(sCol2.contains("null")) sCol2 = "";

              lblCol1.setText(sCol1);
              lblCol1.setMinWidth(150);
              lblCol1.setMaxWidth(150);
              lblCol1.setPrefWidth(150);
              HBox.setHgrow(lblCol1, Priority.ALWAYS);

              lblCol2.setText(sCol2);
              lblCol2.setMinWidth(275);
              lblCol2.setMaxWidth(275);
              lblCol2.setPrefWidth(275);
              HBox.setHgrow(lblCol2, Priority.ALWAYS);

              // make the header row bold
              if(headerRow == true) {
            	  lblCol1.setTextFill(Color.GREY);
            	  lblCol2.setTextFill(Color.GREY);
              }
              
              this.getChildren().addAll(lblCol1, lblCol2);
         }
    }	

	
}


