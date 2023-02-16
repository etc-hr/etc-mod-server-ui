package com.etc.admin.ui.pipeline.raw;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ReferenceType;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayCodeReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.PayConversionCalculationReference;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFilePayCodeReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.PayConversionCalculationReferenceRequest;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.utils.types.PayCodeType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewPipelineRawPayCodeReferenceController {
	@FXML
	private Label topLabel;
	@FXML
	private TextField referenceField;
	@FXML
	private TextField pccFilterField;
	@FXML
	private TextField pccRefKeyField;
	@FXML
	private TextField pccRefGroupField;
	@FXML
	private TextField pccRefValField;
	@FXML
	private DatePicker pccStartDatePicker;
	@FXML
	private ComboBox<String> possibleValuesCombo;
	@FXML
	private CheckBox CreateReferenceCheck;
	@FXML
	private ListView<HBoxReferenceCell> referencesList;
	@FXML
	private ListView<HBoxPCCReferenceCell> pccReferencesList;
	
	private ReferenceType referenceType;
	private String fileReference;
	private PayCodeType payCodeType = null;
	
	Employer employer = null;
	List<PayConversionCalculationReference> pccRefs = null;
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
	}
	
	public void setReferenceType(ReferenceType referenceType, String fileReference, Employer employer) {
		this.referenceType = referenceType;
		this.fileReference = fileReference;
		this.employer=employer;
		showReferences();
	}
	
	private void initControls() {
		
	}
	
	public PayCodeType getPayCodeType() {
		return payCodeType;
	}
	
	private void showReferences() {

		referencesList.getItems().clear();
		referenceField.setText(fileReference);
		ObservableList<HBoxReferenceCell> entries = FXCollections.observableArrayList();
		switch(referenceType) {
		case PYPAYCODE:
			DynamicPayFileSpecification mapper5 = DataManager.i().mDynamicPayFileSpecification;
			if (mapper5 == null) return;
			
			topLabel.setText("CompType Reference Selection");
			// load the combo for the possible values
			possibleValuesCombo.getItems().add("FTH"); 
			possibleValuesCombo.getItems().add("VH"); 
			
			//and load the listview
			List<DynamicPayFilePayCodeReference> payRefs = mapper5.getPayCodeReferences();
			if (payRefs == null || payRefs.size() == 0) {
				try {
					DynamicPayFilePayCodeReferenceRequest reqPYPAYCODE = new DynamicPayFilePayCodeReferenceRequest();
					reqPYPAYCODE.setSpecificationId(DataManager.i().mDynamicPayFileSpecification.getId());
					payRefs =  AdminPersistenceManager.getInstance().getAll(reqPYPAYCODE);
				} catch (Exception e) {
					DataManager.i().log( Level.SEVERE, e);				
				}
			}
			if (payRefs != null && payRefs.size() > 0 ) {
				for (DynamicPayFilePayCodeReference ref : payRefs) {
					if (ref.isActive() == false) continue;
					entries.add(new HBoxReferenceCell (ref));
				}
			} 
			
			// now for the PCC
			loadPCCData();
			break;
		default:
			break;
		}	
		
		// sort
		Collections.sort(entries, (o1, o2) -> (o1.getReference().compareTo(o2.getReference()))); 
		referencesList.setItems(entries);
	}
	
	private void loadPCCData() {
		try {
			PayConversionCalculationReferenceRequest pccReq = new PayConversionCalculationReferenceRequest();
			pccReq.setEmployerId(employer.getId());
			pccRefs =  AdminPersistenceManager.getInstance().getAll(pccReq);
			showPccRefs();
		} catch (Exception e) {
			DataManager.i().log( Level.SEVERE, e);				
		}
	}
	
	private void showPccRefs() {
		pccReferencesList.getItems().clear();
		pccReferencesList.getItems().add(new HBoxPCCReferenceCell (null));
		if (pccRefs != null && pccRefs.size() > 0 ) {
			Collections.sort(pccRefs, (o1, o2) -> (o1.getRefKey().compareTo(o1.getRefKey()))); 
			for (PayConversionCalculationReference pccref : pccRefs) {
				if (pccref.isActive() == false) continue;
				if (pccFilterField.getText().isEmpty() == false &&  
					pccref.getRefKey().toUpperCase().contains(pccFilterField.getText().toUpperCase()) == false && 
					pccref.getRefGrp().toUpperCase().contains(pccFilterField.getText().toUpperCase()) == false) continue;
				pccReferencesList.getItems().add(new HBoxPCCReferenceCell (pccref));
			}
		} 	
	}
	
	@FXML
	private void onSave(ActionEvent event) {
		switch(referenceType) {
		case PYPAYCODE:
			// path 1 - they selected a value
			if (possibleValuesCombo.getValue() != null && possibleValuesCombo.getValue() != "" ) {
				payCodeType = PayCodeType.valueOf(possibleValuesCombo.getValue());
				if (CreateReferenceCheck.isSelected() == true) {
	       			try {
						DynamicPayFilePayCodeReference newRef = new DynamicPayFilePayCodeReference();
						newRef.setCreatedById(DataManager.i().mLocalUser.getId());
						newRef.setCreatedBy(DataManager.i().mLocalUser);
						newRef.setReference(referenceField.getText());
						newRef.setPayCodeType(payCodeType);
						newRef.setSpecification(DataManager.i().mDynamicPayFileSpecification);
						newRef.setSpecificationId(DataManager.i().mDynamicPayFileSpecification.getId());
						DynamicPayFilePayCodeReferenceRequest req = new DynamicPayFilePayCodeReferenceRequest();
						req.setEntity(newRef);
						newRef = AdminPersistenceManager.getInstance().addOrUpdate(req);
					} catch (Exception e) {
						 DataManager.i().logGenericException(e);
					}
				}
			}
			else {	
				// path 2 - they selected an existing reference
				if (referencesList.getSelectionModel().getSelectedItem() != null)
					payCodeType = referencesList.getSelectionModel().getSelectedItem().getPYPayRef().getPayCodeType();
			}
			break;
		default:
			break;
		}	

		//update the spec since its referencews are updated
		DataManager.i().loadDynamicPayFileSpecification(DataManager.i().mDynamicPayFileSpecification.getId());
		// and exit
		exitPopup();	
	}	

	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	

	@FXML
	private void onPCCFilterClear(ActionEvent event) {
		pccFilterField.setText("");
		showPccRefs();
	}	

	@FXML
	private void onPCCFilterKeyUp() {
		showPccRefs();
	}	

	@FXML
	private void onAddPCC(ActionEvent event) {
		// verify
		if (pccRefKeyField.getText().isEmpty() == true || pccRefGroupField.getText().isEmpty() == true) {
			Utils.showAlert("Missing Data", "Valid Pay Codes must have at least a RefKey and a RefGroup. Please correct and retry.");
			return;			
		}
		if (pccRefValField.getText().isEmpty() == false && Utils.isFloat(pccRefValField.getText()) == false) {
			Utils.showAlert("Bad Number Data", "The PCC RefVal data must be a number. Please correct and retry.");
			return;			
		}
		// add the PCCRef
		try {
			// create the ref
			PayConversionCalculationReference pccRef = new PayConversionCalculationReference();
			pccRef.setEmployer(employer);
			pccRef.setEmployerId(employer.getId());
			if (pccRefValField.getText().isEmpty() == false)
				pccRef.setRefVal(Float.valueOf(pccRefValField.getText()));
			pccRef.setRefKey(pccRefKeyField.getText());
			pccRef.setRefGrp(pccRefGroupField.getText());
			pccRef.setStartDate(Utils.getCalDate(pccStartDatePicker));
			PayConversionCalculationReferenceRequest pccReq = new PayConversionCalculationReferenceRequest();
			pccReq.setEmployerId(employer.getId());
			pccReq.setEntity(pccRef);
			AdminPersistenceManager.getInstance().addOrUpdate(pccReq);
		} catch (Exception e) {
			DataManager.i().log( Level.SEVERE, e);				
		}
		
		// update the list
		pccReferencesList.getItems().clear();
		loadPCCData();
		Utils.showAlert("Pay Code Added", "The Pay Code has been added and the list updated.");
	}	

	@FXML
	private void onCreateReference() {
		if (CreateReferenceCheck.isSelected() == false) return;
		
		// make sure dependent objects are present or need to be created
		switch(referenceType) {
		case PYPAYCODE:
			// check for matching paycode (PayConversionCalculationReference)
			if (PayConversionCalRefExists() == false) {
				Utils.showAlert("Notice", "The reference \"" + fileReference + 
						"\" was not found in the PayConversionCalculationReference RefKeys for this employer. This is information level only, it may not apply to this file.");
			}
			break;
		default:
			break;
		} 
	}	

	private boolean PayConversionCalRefExists() {
		// check for matching paycode (PayConversionCalculationReference)
		if (fileReference != null && fileReference.isEmpty() == false && pccRefs != null && pccRefs.size() > 0 ) {
			for (PayConversionCalculationReference pccRef : pccRefs) {
				if (pccRef.getRefKey().contentEquals(fileReference)) 
					return true;
			}
		} 	

		return false;
	}
	
	private void exitPopup() {
		Stage stage = (Stage) referencesList.getScene().getWindow();
		stage.close();
	}
	
	public static class HBoxReferenceCell extends HBox {
         Label lblReference = new Label();
         Label lblValue = new Label();
         DynamicPayFilePayCodeReference pyPayCodeRef;
         
         public DynamicPayFilePayCodeReference getPYPayRef() {
        	 return pyPayCodeRef;
         }

         public String getValue() {
        	 return lblValue.getText();
         }

         public String getReference() {
        	 return lblReference.getText();
         }

         HBoxReferenceCell(DynamicPayFilePayCodeReference payRef) {
             super();

             if (payRef != null) {
           	  this.pyPayCodeRef = payRef;
           	  Utils.setHBoxLabel(lblReference, 260, false, payRef.getReference());
           	  Utils.setHBoxLabel(lblValue, 110, false, payRef.getPayCodeType().toString());
             } else {
           	  Utils.setHBoxLabel(lblReference, 260, true, "Reference");
           	  Utils.setHBoxLabel(lblValue, 110, true, "Value");
             }
       	  this.getChildren().addAll(lblReference, lblValue);
        }
	}	
	
	public static class HBoxPCCReferenceCell extends HBox {
        Label lblReference = new Label();
        Label lblValue = new Label();
        Label lblGroup = new Label();
        Label lblStartDate = new Label();
        
        PayConversionCalculationReference pccRef;
        
        public PayConversionCalculationReference getPCCRef() {
       	 return pccRef;
        }

        public String getValue() {
       	 return lblValue.getText();
        }

        public String getReference() {
       	 return lblReference.getText();
        }

        HBoxPCCReferenceCell(PayConversionCalculationReference pccRef) {
            super();

            if (pccRef != null) {
          	  this.pccRef = pccRef;
          	  Utils.setHBoxLabel(lblReference, 130, false, pccRef.getRefKey());
          	  Utils.setHBoxLabel(lblGroup, 100, false, pccRef.getRefGrp());
          	  Utils.setHBoxLabel(lblValue, 100, false, pccRef.getRefVal());
          	  Utils.setHBoxLabel(lblStartDate, 100, false, pccRef.getStartDate());
            } else {
          	  Utils.setHBoxLabel(lblReference, 130, true, "Ref Key");
          	  Utils.setHBoxLabel(lblGroup, 100, true, "Ref Group");
          	  Utils.setHBoxLabel(lblValue, 100, true, "Ref Val");
          	  Utils.setHBoxLabel(lblStartDate, 100, true, "Start Date");
            }
      	  this.getChildren().addAll(lblReference, lblGroup, lblValue, lblStartDate);
       }
	}	
	
}
