package com.etc.admin.ui.operation;

import com.etc.admin.EtcAdmin;
import com.etc.admin.utils.Utils.ScreenType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ViewOperationController {
	
	@FXML
	private Label oprNameLabel;
	@FXML
	private Label oprDescriptionLabel;
	@FXML
	private CheckBox oprSystemCheckBox;
	@FXML
	private CheckBox oprRequiredCheckBox;
	@FXML
	private Label oprPriorityLabel;
	@FXML
	private Label oprOpSecSpecLabel;
	@FXML
	private Button oprEditButton;
	@FXML
	private ListView<HBoxOperationCell> oprOperationListView;	
	@FXML
	private Label oprOperationsLabel;
	@FXML
	private Label oprModuleNameLabel;
	@FXML
	private Label oprModuleDescriptionLabel;
	@FXML
	private Label oprCoreIdLabel;
	@FXML
	private Label oprCoreActiveLabel;
	@FXML
	private Label oprCoreBODateLabel;
	@FXML
	private Label oprCoreLastUpdatedLabel;
	@FXML
	private Label oprModuleCoreIdLabel;
	@FXML
	private Label oprModuleCoreActiveLabel;
	@FXML
	private Label oprModuleCoreBODateLabel;
	@FXML
	private Label oprModuleCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		clearScreen();
		loadOperations();
	}

	private void clearScreen() {
		oprNameLabel.setText(" ");
		oprDescriptionLabel.setText(" ");
		oprPriorityLabel.setText(" ");
		oprOpSecSpecLabel.setText(" ");
		oprModuleNameLabel.setText(" ");
		oprModuleDescriptionLabel.setText(" ");
		oprCoreIdLabel.setText(" ");
		oprCoreActiveLabel.setText(" ");
		oprCoreBODateLabel.setText(" ");
		oprCoreLastUpdatedLabel.setText(" ");
		oprModuleCoreIdLabel.setText(" ");
		oprModuleCoreActiveLabel.setText(" ");
		oprModuleCoreBODateLabel.setText(" ");
		oprModuleCoreLastUpdatedLabel.setText(" ");
		oprEditButton.setDisable(true);
	}
	
	private void loadOperations(){	    
	/*	if (EtcAdmin.mOperations != null)
		{
		    List<HBoxOperationCell> operationList = new ArrayList<>();
			
		    operationList.add(new HBoxOperationCell("Name", "Description", null, 0));

		    int i = 0;
			for (Operation operation : EtcAdmin.mOperations) {
				//this should never happen
				if (operation == null) continue;
				
				operationList.add(new HBoxOperationCell(operation.getName(), operation.getDescription(),"View", i)); 
				i++;
			};	
			
			ObservableList<HBoxOperationCell> myObservableOperationList = FXCollections.observableList(operationList);
			oprOperationListView.setItems(myObservableOperationList);		
			
			//update our operation screen label
			oprOperationsLabel.setText("Operations (total: " + String.valueOf(EtcAdmin.mOperations.size()) + ")" );
		} else {
			oprOperationsLabel.setText("Operations (total: 0)");			
		}
		*/
	}
	
	private void setOperationData(){
	/*	
		if (EtcAdmin.mOperation != null) {
			//enable the edit button
			oprEditButton.setDisable(false);
				
			Utils.updateControl(oprNameLabel,EtcAdmin.mOperation.getName());
			Utils.updateControl(oprDescriptionLabel,EtcAdmin.mOperation.getDescription());
			Utils.updateControl(oprPriorityLabel,String.valueOf(EtcAdmin.mOperation.getPriority()));
			
			if (EtcAdmin.mOperation.getOpSecSpec() != null)
				Utils.updateControl(oprOpSecSpecLabel,EtcAdmin.mOperation.getOpSecSpec().toString());
			
			Utils.updateControl(oprSystemCheckBox,EtcAdmin.mOperation.isSystem());
			oprSystemCheckBox.setDisable(true);
			Utils.updateControl(oprRequiredCheckBox,EtcAdmin.mOperation.isRequired());
			oprRequiredCheckBox.setDisable(true);

			// core data
			Utils.updateControl(oprCoreIdLabel,String.valueOf(EtcAdmin.mOperation.getId()));
			Utils.updateControl(oprCoreActiveLabel,String.valueOf(EtcAdmin.mOperation.isActive()));
			Utils.setControlDate(oprCoreBODateLabel,EtcAdmin.mOperation.getBornOn());
			Utils.setControlDate(oprCoreLastUpdatedLabel,EtcAdmin.mOperation.getLastUpdated());
			
			if (EtcAdmin.mOperation.getModule() != null) {
				Utils.updateControl(oprModuleNameLabel,EtcAdmin.mOperation.getModule().getName());
				Utils.updateControl(oprModuleDescriptionLabel,EtcAdmin.mOperation.getModule().getDescription());
				Utils.updateControl(oprModuleCoreIdLabel,String.valueOf(EtcAdmin.mOperation.getModule().getId()));
				Utils.updateControl(oprModuleCoreActiveLabel,String.valueOf(EtcAdmin.mOperation.getModule().isActive()));
				Utils.setControlDate(oprModuleCoreBODateLabel,EtcAdmin.mOperation.getModule().getBornOn());
				Utils.setControlDate(oprModuleCoreLastUpdatedLabel,EtcAdmin.mOperation.getModule().getLastUpdated());
			} else {
				oprModuleNameLabel.setText("");
				oprModuleDescriptionLabel.setText("");
				oprModuleCoreIdLabel.setText("");
				oprModuleCoreActiveLabel.setText("");
				oprModuleCoreBODateLabel.setText("");
				oprModuleCoreLastUpdatedLabel.setText("");				
			}
		}
		*/
	}	
	
	//extending the listview for our additional controls
	public class HBoxOperationCell extends HBox {
         Label lblName = new Label();
         Label lblDescription = new Label();
         Button btnView = new Button();

         HBoxOperationCell(String sName, String sDescription, String sButtonText, int nButtonID) {
              super();

              if (sName == null ) sName = "";
              if (sDescription == null ) sDescription = "";
              
              lblName.setText(sName);
              lblName.setMinWidth(330);
              lblName.setMaxWidth(330);
              lblName.setPrefWidth(330);
              HBox.setHgrow(lblName, Priority.ALWAYS);

              lblDescription.setText(sDescription);
              lblDescription.setMinWidth(330);
              lblDescription.setMaxWidth(330);
              lblDescription.setPrefWidth(330);
              HBox.setHgrow(lblDescription, Priority.ALWAYS);

              if (sButtonText == null) {
            	  lblName.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblDescription.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  this.getChildren().addAll(lblName, lblDescription );
              }else {
            	  btnView.setText(sButtonText);
            	  btnView.setId(String.valueOf(nButtonID));
            	  this.getChildren().addAll(lblName, lblDescription,  btnView);
            	  
            	  //add an event handler for this button
            	  btnView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            		  
            		  @Override
            		  public void handle(MouseEvent event) {
            			  // let the application load the current employer
            			  viewOperation(Integer.parseInt(btnView.getId()));
            		  }
            	  });
              }
         }
    }	
	
	private void viewOperation(int nOperationID) {
	//	EtcAdmin.i().setOperation(nOperationID);
		
		setOperationData();
	}	
	
	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.OPERATIONEDIT, true);
	}	
	
	
}
