package com.etc.admin.ui.pipeline.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils.LogType;
import com.etc.corvetto.ems.pipeline.entities.pay.PayConversionCalculation;
import com.etc.corvetto.ems.pipeline.rqs.pay.PayConversionCalculationRequest;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewPipelineSpecificationCalculationController 
{
	@FXML
	private Button dsrefAddButton;
	@FXML
	private Button dsrefRemoveButton;
	@FXML
	private Button dsrefEditButton;
	@FXML
	private Button dsrefSaveButton;
	@FXML
	private Button dsrefEditCurrentButton;
	@FXML
	private Button dsrefSaveCurrentButton;
	@FXML
	private Button dsrefAssignButton;
	@FXML
	private ListView<CalcCell> dsrefCalcsListView;
	@FXML
	private TextArea dsrefCurrentExpressionTextArea;
	@FXML
	private TextField dsrefCurrentIdField;
	@FXML
	private TextField dsrefCurrentNameField;
	@FXML
	private TextField dsrefCurrentDescriptionField;
	@FXML
	private TextArea dsrefExpressionTextArea;
	@FXML
	private TextField dsrefNameField;
	@FXML
	private TextField dsrefDescriptionField;
	@FXML
	private GridPane dsrefCurrentGridPane;
	@FXML
	private GridPane dsrefAllGridPane;
	
	private ViewPipelineSpecificationController specController;
	
	PayConversionCalculation  currentPayCalc = null;
	PayConversionCalculation  payCalc = null;

	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() 
	{
		//reset any edit conditions
		dsrefRemoveButton.setDisable(true);
		dsrefSaveButton.setVisible(false);
		dsrefSaveCurrentButton.setVisible(false);
		dsrefAllGridPane.getStyleClass().clear();
		dsrefAllGridPane.setStyle(null);
		
		dsrefAssignButton.setDisable(true);
		dsrefEditButton.setDisable(true);
		
		setCurrentEditable(false);

		dsrefExpressionTextArea.setEditable(false);
		dsrefNameField.setEditable(false);
		dsrefDescriptionField.setEditable(false);
		
		loadCurrentPayCalc();
		
		// set a handler for the selections click
		dsrefCalcsListView.setOnMouseClicked(mouseClickedEvent ->
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) 
            {
            	if(dsrefCalcsListView.getSelectionModel().getSelectedItem() != null) 
            	{
            		payCalc = dsrefCalcsListView.getSelectionModel().getSelectedItem().getCalc();
	        		dsrefAssignButton.setDisable(false);
	        		dsrefEditButton.setDisable(false);
	            	loadCalcDetails();
            	}
            }
        });
	}
	
	private void setCurrentEditable(boolean state)
	{
		dsrefCurrentExpressionTextArea.setEditable(state);
		dsrefCurrentNameField.setEditable(state);
		dsrefCurrentDescriptionField.setEditable(state);
		dsrefSaveCurrentButton.setVisible(state);
		
		if(state == false) 
		{
			dsrefCurrentGridPane.getStyleClass().clear();
			dsrefCurrentGridPane.setStyle(null);	
			dsrefEditCurrentButton.setText("Edit");
			loadCurrentPayCalc();
		} else {
			dsrefEditCurrentButton.setText("Cancel");
			dsrefCurrentGridPane.setStyle("-fx-background-color: #eeffee");
			dsrefCurrentGridPane.setStyle("-fx-background-color: #eeffee");
		}
	}
	
	private void setAllEditable(boolean state)
	{
		dsrefExpressionTextArea.setEditable(state);
		dsrefNameField.setEditable(state);
		dsrefDescriptionField.setEditable(state);
		dsrefSaveButton.setVisible(state);
		
		if(state == false) 
		{
			dsrefAllGridPane.getStyleClass().clear();
			dsrefAllGridPane.setStyle(null);	
			dsrefEditButton.setText("Edit");
		} else {
			dsrefEditButton.setText("Cancel");
			dsrefAllGridPane.setStyle("-fx-background-color: #eeffee");
			dsrefAllGridPane.setStyle("-fx-background-color: #eeffee");
		}
	}
	
	public void setSpecController(ViewPipelineSpecificationController specController) {
		this.specController = specController;
	}
	
	private void loadCurrentPayCalc() 
	{
		try {
			PayConversionCalculationRequest request = new PayConversionCalculationRequest();
			request.setId(DataManager.i().mPipelineSpecification.getDynamicFileCalculatorId());
			currentPayCalc = AdminPersistenceManager.getInstance().get(request);
		} catch (CoreException e) {
			 DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		if(currentPayCalc != null) 
		{
			dsrefCurrentIdField.setText(String.valueOf(currentPayCalc.getId()));
			dsrefCurrentExpressionTextArea.setText(currentPayCalc.getExpression());
			dsrefCurrentNameField.setText(currentPayCalc.getName());
			dsrefCurrentDescriptionField.setText(currentPayCalc.getDescription());
		}
	}

	private void loadCalcDetails() 
	{
		dsrefExpressionTextArea.setText(payCalc.getExpression());
		dsrefNameField.setText(payCalc.getName());
		dsrefDescriptionField.setText(payCalc.getDescription());
	}

	private void clearCalcDetails()
	{
		dsrefExpressionTextArea.setText("");
		dsrefNameField.setText("");
		dsrefDescriptionField.setText("");
	}

	public void load() 
	{
		clearCalcDetails();
		loadCalcData();
		refreshCalcList();
	}

	public void loadCalcData() 
	{
		try {
			
			PayConversionCalculationRequest request = new PayConversionCalculationRequest();
			DataManager.i().mPayConversionCalculations = AdminPersistenceManager.getInstance().getAll(request);
			
		} catch (CoreException e) {
			DataManager.i().log(Level.SEVERE, e);
		}	
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
	}
	
	private void refreshCalcList() 
	{
		dsrefCalcsListView.getItems().clear();
		
		List<CalcCell> list = new ArrayList<>();
		for(PayConversionCalculation  calc : DataManager.i().mPayConversionCalculations) 
		{
			list.add(new CalcCell(calc));
		}
		
        ObservableList<CalcCell> newObservableList = FXCollections.observableList(list);
        dsrefCalcsListView.setItems(newObservableList);	
	}
	
	@FXML
	private void onEditCurrent(ActionEvent event)
	{
		if(dsrefEditCurrentButton.getText().equals("Edit"))
			setCurrentEditable(true);
		else 
			setCurrentEditable(false);
	}
	
	@FXML
	private void onSaveCurrent(ActionEvent event) {
		// copy over the data
		currentPayCalc.setExpression(dsrefCurrentExpressionTextArea.getText());
		currentPayCalc.setName(dsrefCurrentNameField.getText());
		currentPayCalc.setDescription(dsrefCurrentDescriptionField.getText());
		
		// save it
		try {
			PayConversionCalculationRequest request = new PayConversionCalculationRequest();
			request.setId(DataManager.i().mPipelineSpecification.getDynamicFileCalculatorId());
			request.setEntity(currentPayCalc);
			currentPayCalc = AdminPersistenceManager.getInstance().addOrUpdate(request);
		} catch (CoreException e) {
			 DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
		// mark as not editble
		setCurrentEditable(false);
		// and update
		loadCurrentPayCalc();
	}
	
	@FXML
	private void onAssign(ActionEvent event) 
	{
		dsrefCurrentExpressionTextArea.setText(payCalc.getExpression());
		dsrefCurrentNameField.setText(payCalc.getName());
		dsrefCurrentDescriptionField.setText(payCalc.getDescription());
		setCurrentEditable(true);
	}
	
	@FXML
	private void onEdit(ActionEvent event) 
	{
		if(dsrefEditButton.getText().equals("Edit"))
			setAllEditable(true);
		else
			setAllEditable(false);
	}
	
	@FXML
	private void onAdd(ActionEvent event) {
		setAllEditable(true);
	}		
	
	@FXML
	private void onRemove(ActionEvent event) {
		
	}	
	
	@FXML
	private void onSave(ActionEvent event) {
		Stage stage = (Stage) dsrefRemoveButton.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	private void onClose(ActionEvent event) {
		Stage stage = (Stage) dsrefRemoveButton.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	private void onRemoveCalc(ActionEvent event) {	
		refreshCalcList();
	}
	
	public class CalcCell extends HBox 
	{
		Label id = new Label();
		Label name = new Label();
		PayConversionCalculation calc;

         public PayConversionCalculation getCalc() {
        	 return calc;
         }
         
         CalcCell(PayConversionCalculation calc) 
         {
              super();
              
              this.calc = calc;
              name.setText(calc.getDescription());
              id.setText(String.valueOf(calc.getId()));
              
              //set the label widths
              id.setMinWidth(50);
              id.setMaxWidth(50);
              id.setPrefWidth(50);
              name.setMinWidth(150);
              name.setMaxWidth(150);
              name.setPrefWidth(150);

              this.getChildren().addAll(id, name);
         }
    }	
	
	public class HBoxCell extends HBox
	{
		SimpleStringProperty source = new SimpleStringProperty();
		SimpleStringProperty reference = new SimpleStringProperty();

        public String getSource() {
        	return source.get();
        }
         
        public String getReference() {
        	return reference.get();
        }
         
        HBoxCell(String source, String reference) 
        {
             super();

             if(source == null) source = "";
             if(reference == null ) reference = "";
              
             this.source.set(source);
             this.reference.set(reference);
        }
    }	
}

