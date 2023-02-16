package com.etc.admin.ui.employer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.coverage.ViewCoverageController;
import com.etc.admin.ui.employer.ViewEmployerController.HBox2ColCell;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.Benefit;
import com.etc.corvetto.entities.Coverage;
import com.etc.corvetto.rqs.BenefitRequest;
import com.etc.corvetto.rqs.CoverageRequest;
import com.etc.entities.DataProperty;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewEmployerPropertiesController 
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
		showProperties();
	}
	
	private void initControls() 
	{
		dataList.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				viewSelectedProperty();
            }
        });
	}
	
	private void showProperties() 
	{
		if(DataManager.i().mEmployer.getProperties()!= null)
		{
		    List<HBox2ColCell> propertyList = new ArrayList<>();
			
		    if(DataManager.i().mEmployer.getProperties().size() > 0)
		    	propertyList.add(new HBox2ColCell("Name", "Value", true));
			
		    for(DataProperty property : DataManager.i().mEmployer.getProperties()) 
		    {
				if(property == null) continue;
				if(property.isActive() == false && InactiveCheck.isSelected() == false) continue;
				if(property.isDeleted() == true && deletedCheck.isSelected() == false) continue;
			
				propertyList.add(new HBox2ColCell(property.getName(), property.getValue(), false));
		    };	
			
			ObservableList<HBox2ColCell> myObservablePlanList = FXCollections.observableList(propertyList);
			dataList.setItems(myObservablePlanList);		
			
			//update our screen label
			topLabel.setText("Properties (total: " + String.valueOf(DataManager.i().mEmployer.getProperties().size()) + ")" );
		} else {
			topLabel.setText("Properties (total: 0)");			
		}
	}
	
	private void viewSelectedProperty() 
	{
		// offset one for the header row
		DataManager.i().setEmployerAssociatedProperty(dataList.getSelectionModel().getSelectedIndex() - 1);
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/associatedproperty/ViewAssociatedProperty.fxml"));
			Parent ControllerNode = loader.load();
			//ViewUploadTypeTemplateController templateController = (ViewUploadTypeTemplateController) loader.getController();

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
		showProperties();
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


