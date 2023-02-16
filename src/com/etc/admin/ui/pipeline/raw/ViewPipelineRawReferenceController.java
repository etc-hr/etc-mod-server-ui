package com.etc.admin.ui.pipeline.raw;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ReferenceType;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileCoverageGroupReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileDepartmentReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileGenderReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileLocationReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFilePayCodeReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileUnionTypeReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileGenderReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayCodeReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayFrequencyReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileUnionTypeReference;
import com.etc.corvetto.ems.pipeline.entities.pay.PayConversionCalculationReference;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileCoverageGroupReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileDepartmentReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileGenderReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileLocationReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFilePayCodeReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileUnionTypeReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileGenderReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFilePayCodeReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFilePayFrequencyReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileUnionTypeReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.PayConversionCalculationReferenceRequest;
import com.etc.corvetto.entities.CoverageGroup;
import com.etc.corvetto.entities.Department;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.Location;
import com.etc.corvetto.rqs.CoverageGroupRequest;
import com.etc.corvetto.rqs.DepartmentRequest;
import com.etc.corvetto.rqs.LocationRequest;
import com.etc.corvetto.utils.types.PayCodeType;
import com.etc.corvetto.utils.types.PayFrequencyType;
import com.etc.corvetto.utils.types.UnionType;
import com.etc.utils.types.GenderType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewPipelineRawReferenceController {
	@FXML
	private Label topLabel;
	@FXML
	private TextField referenceField;
	@FXML
	private ComboBox<String> possibleValuesCombo;
	@FXML
	private CheckBox CreateReferenceCheck;
	@FXML
	private ListView<HBoxReferenceCell> referencesList;
	
	private ReferenceType referenceType;
	private String fileReference;
	private PayCodeType payCodeType = null;
	private GenderType genderType = null;
	private UnionType unionType = null;
	private PayFrequencyType payFreqType = null;
	private CoverageGroup coverageGroup = null;
	private Department department = null;
	private Location location = null;
	
	Employer employer;
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
	
	public GenderType getGenderType() {
		return genderType;
	}
	
	public PayCodeType getPayCodeType() {
		return payCodeType;
	}
	
	public PayFrequencyType getPayFreqType() {
		return payFreqType;
	}
	
	public UnionType getUnionType() {
		return unionType;
	}
	
	public Department getDepartment() {
		return department;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public CoverageGroup getCoverageGroup() {
		return coverageGroup;
	}
	
	private void initControls() {
		
	}
	
	private void showReferences() {

		referencesList.getItems().clear();
		referenceField.setText(fileReference);
		ObservableList<HBoxReferenceCell> entries = FXCollections.observableArrayList();
		switch(referenceType) {
		case EECOVERAGEGROUP:
			CreateReferenceCheck.setVisible(false);
			DynamicEmployeeFileSpecification mapperEECOVERAGEGROUP = DataManager.i().mDynamicEmployeeFileSpecification;
			if (mapperEECOVERAGEGROUP == null) return;
			
			topLabel.setText("Coverage Class Reference Selection");
			// load the combo for the possible values
			try {
				CoverageGroupRequest req = new CoverageGroupRequest();
				req.setEmployerId(employer.getId());
				List<CoverageGroup> cgs =  AdminPersistenceManager.getInstance().getAll(req);
				
				if (cgs != null && cgs.size() > 0)
					for (CoverageGroup cg : cgs)
						possibleValuesCombo.getItems().add(cg.getName());
			} catch (Exception e) {
				DataManager.i().log( Level.SEVERE, e);				
			}
	
			//and load the listview
			List<DynamicEmployeeFileCoverageGroupReference> cgRefs = mapperEECOVERAGEGROUP.getCvgGroupReferences();
			if (cgRefs == null || cgRefs.size() == 0) {
				try {
					DynamicEmployeeFileCoverageGroupReferenceRequest reqEECOVERAGEGROUP = new DynamicEmployeeFileCoverageGroupReferenceRequest();
					reqEECOVERAGEGROUP.setSpecificationId(DataManager.i().mDynamicEmployeeFileSpecification.getId());
					cgRefs =  AdminPersistenceManager.getInstance().getAll(reqEECOVERAGEGROUP);
				} catch (Exception e) {
					DataManager.i().log( Level.SEVERE, e);				
				}
			}
			if (cgRefs != null && cgRefs.size() > 0 ) {
				for (DynamicEmployeeFileCoverageGroupReference ref : cgRefs) {
					if (ref.isActive() == false) continue;
					entries.add(new HBoxReferenceCell (ref));
				}
			} 
			break;
		case EEDEPARTMENT:
			CreateReferenceCheck.setVisible(false);
			DynamicEmployeeFileSpecification mapperEEDEPARTMENT = DataManager.i().mDynamicEmployeeFileSpecification;
			if (mapperEEDEPARTMENT == null) return;
			
			topLabel.setText("Department Reference Selection");
			// load the combo for the possible values
			try {
				DepartmentRequest req = new DepartmentRequest();
				req.setEmployerId(employer.getId());
				List<Department> deps =  AdminPersistenceManager.getInstance().getAll(req);
				
				if (deps != null && deps.size() > 0)
					for (Department dep : deps)
						possibleValuesCombo.getItems().add(dep.getName());
			} catch (Exception e) {
				DataManager.i().log( Level.SEVERE, e);				
			}
	
			//and load the listview
			List<DynamicEmployeeFileDepartmentReference> depRefs = mapperEEDEPARTMENT.getDeptReferences();
			if (depRefs == null || depRefs.size() == 0) {
				try {
					DynamicEmployeeFileDepartmentReferenceRequest reqEEDEPARTMENT = new DynamicEmployeeFileDepartmentReferenceRequest();
					reqEEDEPARTMENT.setSpecificationId(DataManager.i().mDynamicEmployeeFileSpecification.getId());
					depRefs =  AdminPersistenceManager.getInstance().getAll(reqEEDEPARTMENT);
				} catch (Exception e) {
					DataManager.i().log( Level.SEVERE, e);				
				}
			}
			if (depRefs != null && depRefs.size() > 0 ) {
				for (DynamicEmployeeFileDepartmentReference ref : depRefs) {
					if (ref.isActive() == false) continue;
					entries.add(new HBoxReferenceCell (ref));
				}
			} 
			break;
		case PYGENDERTYPE:
			DynamicPayFileSpecification mapper3 = DataManager.i().mDynamicPayFileSpecification;
			if (mapper3 == null) return;;
			topLabel.setText("Gender Type Reference Selection");
			// load the combo for the possible values
			for (GenderType gt : GenderType.values())
				possibleValuesCombo.getItems().add(gt.toString()); 
			
			//and load the listview
			List<DynamicPayFileGenderReference> genderRefs = mapper3.getGenderReferences();
			if (genderRefs == null || genderRefs.size() == 0) {
				try {
					DynamicPayFileGenderReferenceRequest reqPYGENDERTYPE = new DynamicPayFileGenderReferenceRequest();
					reqPYGENDERTYPE.setSpecificationId(DataManager.i().mDynamicPayFileSpecification.getId());
					genderRefs =  AdminPersistenceManager.getInstance().getAll(reqPYGENDERTYPE);
				} catch (Exception e) {
					DataManager.i().log( Level.SEVERE, e);				
				}
			}
			if (genderRefs != null && genderRefs.size() > 0 ) {
				for (DynamicPayFileGenderReference ref : genderRefs) {
					if (ref.isActive() == false) continue;
					entries.add(new HBoxReferenceCell (ref));
				}
			} 
			break;
		case EEGENDERTYPE:
			DynamicEmployeeFileSpecification mapperEEGENDERTYPE = DataManager.i().mDynamicEmployeeFileSpecification;
			if (mapperEEGENDERTYPE == null) return;;
			topLabel.setText("Gender Type Reference Selection");
			// load the combo for the possible values
			for (GenderType gt : GenderType.values())
				possibleValuesCombo.getItems().add(gt.toString()); 
			
			//and load the listview
			List<DynamicEmployeeFileGenderReference> eeGenderRefs = mapperEEGENDERTYPE.getGenderReferences();
			if (eeGenderRefs == null || eeGenderRefs.size() == 0) {
				try {
					DynamicEmployeeFileGenderReferenceRequest reqEEGENDERTYPE = new DynamicEmployeeFileGenderReferenceRequest();
					reqEEGENDERTYPE.setSpecificationId(DataManager.i().mDynamicPayFileSpecification.getId());
					eeGenderRefs =  AdminPersistenceManager.getInstance().getAll(reqEEGENDERTYPE);
				} catch (Exception e) {
					DataManager.i().log( Level.SEVERE, e);				
				}
			}
			if (eeGenderRefs != null && eeGenderRefs.size() > 0 ) {
				for (DynamicEmployeeFileGenderReference ref : eeGenderRefs) {
					if (ref.isActive() == false) continue;
					entries.add(new HBoxReferenceCell (ref));
				}
			} 
			break;
		case EELOCATION:
			CreateReferenceCheck.setVisible(false);
			DynamicEmployeeFileSpecification mapperEELOCATION = DataManager.i().mDynamicEmployeeFileSpecification;
			if (mapperEELOCATION == null) return;
			
			topLabel.setText("Location Reference Selection");
			// load the combo for the possible values
			try {
				LocationRequest req = new LocationRequest();
				req.setEmployerId(employer.getId());
				List<Location> locs =  AdminPersistenceManager.getInstance().getAll(req);
				
				if (locs != null && locs.size() > 0)
					for (Location loc : locs)
						possibleValuesCombo.getItems().add(loc.getName());
			} catch (Exception e) {
				DataManager.i().log( Level.SEVERE, e);				
			}
	
			//and load the listview
			List<DynamicEmployeeFileLocationReference> locRefs = mapperEELOCATION.getLocReferences();
			if (locRefs == null || locRefs.size() == 0) {
				try {
					DynamicEmployeeFileLocationReferenceRequest reqEELOCATION = new DynamicEmployeeFileLocationReferenceRequest();
					reqEELOCATION.setSpecificationId(DataManager.i().mDynamicPayFileSpecification.getId());
					locRefs =  AdminPersistenceManager.getInstance().getAll(reqEELOCATION);
				} catch (Exception e) {
					DataManager.i().log( Level.SEVERE, e);				
				}
			}
			if (locRefs != null && locRefs.size() > 0 ) {
				for (DynamicEmployeeFileLocationReference ref : locRefs) {
					if (ref.isActive() == false) continue;
					entries.add(new HBoxReferenceCell (ref));
				}
			} 
			break;
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
			break;
		case EEPAYCODE:
			DynamicEmployeeFileSpecification mapperEEPAYCODE = DataManager.i().mDynamicEmployeeFileSpecification;
			if (mapperEEPAYCODE == null) return;
			
			topLabel.setText("CompType Reference Selection");
			// load the combo for the possible values
			possibleValuesCombo.getItems().add("FTH"); 
			possibleValuesCombo.getItems().add("VH"); 
			
			//and load the listview
			List<DynamicEmployeeFilePayCodeReference> eePayRefs = mapperEEPAYCODE.getPayCodeReferences();
			if (eePayRefs == null || eePayRefs.size() == 0) {
				try {
					DynamicEmployeeFilePayCodeReferenceRequest reqEEPAYCODE = new DynamicEmployeeFilePayCodeReferenceRequest();
					reqEEPAYCODE.setSpecificationId(DataManager.i().mDynamicPayFileSpecification.getId());
					eePayRefs =  AdminPersistenceManager.getInstance().getAll(reqEEPAYCODE);
				} catch (Exception e) {
					DataManager.i().log( Level.SEVERE, e);				
				}
			}
			if (eePayRefs != null && eePayRefs.size() > 0 ) {
				for (DynamicEmployeeFilePayCodeReference ref : eePayRefs) {
					if (ref.isActive() == false) continue;
					entries.add(new HBoxReferenceCell (ref));
				}
			} 
			break;
		case PYPAYFREQUENCY:
			DynamicPayFileSpecification mapper6 = DataManager.i().mDynamicPayFileSpecification;
			if (mapper6 == null) return;;
			topLabel.setText("Pay Frequency Reference Selection");
			// load the combo for the possible values
			for (PayFrequencyType pft : PayFrequencyType.values())
				possibleValuesCombo.getItems().add(pft.toString()); 
			
			//and load the listview
			referencesList.getItems().clear();
			List<DynamicPayFilePayFrequencyReference> payFreqRefs = mapper6.getPpdFreqReferences();
			if (payFreqRefs == null || payFreqRefs.size() == 0) {
				try {
					DynamicPayFilePayFrequencyReferenceRequest reqPYPAYFREQUENCY = new DynamicPayFilePayFrequencyReferenceRequest();
					reqPYPAYFREQUENCY.setSpecificationId(DataManager.i().mDynamicPayFileSpecification.getId());
					payFreqRefs =  AdminPersistenceManager.getInstance().getAll(reqPYPAYFREQUENCY);
				} catch (Exception e) {
					DataManager.i().log( Level.SEVERE, e);				
				}
			}
			if (payFreqRefs != null && payFreqRefs.size() > 0 ) {
				for (DynamicPayFilePayFrequencyReference ref : payFreqRefs) {
					if (ref.isActive() == false) continue;
					entries.add(new HBoxReferenceCell (ref));
				}
			} 
			break;
		case PYUNIONTYPE:
			DynamicPayFileSpecification mapper7 = DataManager.i().mDynamicPayFileSpecification;
			if (mapper7 == null) return;;
			topLabel.setText("Union Type Reference Selection");
			// load the combo for the possible values
			for (UnionType ut : UnionType.values())
				possibleValuesCombo.getItems().add(ut.toString()); 
			
			//and load the listview
			List<DynamicPayFileUnionTypeReference> unionTypeRefs = mapper7.getUnionTypeReferences();
			if (unionTypeRefs != null && unionTypeRefs.size() > 0 ) {
				for (DynamicPayFileUnionTypeReference ref : unionTypeRefs) {
					if (ref.isActive() == false) continue;
					entries.add(new HBoxReferenceCell (ref));
				}
			} 
			break;
		case EEUNIONTYPE:
			DynamicEmployeeFileSpecification mapperEEUNIONTYPE = DataManager.i().mDynamicEmployeeFileSpecification;
			if (mapperEEUNIONTYPE == null) return;;
			topLabel.setText("Union Type Reference Selection");
			// load the combo for the possible values
			for (UnionType ut : UnionType.values())
				possibleValuesCombo.getItems().add(ut.toString()); 
			
			//and load the listview
			List<DynamicEmployeeFileUnionTypeReference> eeUnionTypeRefs = mapperEEUNIONTYPE.getUnionTypeReferences();
			if (eeUnionTypeRefs != null && eeUnionTypeRefs.size() > 0 ) {
				for (DynamicEmployeeFileUnionTypeReference ref : eeUnionTypeRefs) {
					if (ref.isActive() == false) continue;
					entries.add(new HBoxReferenceCell (ref));
				}
			} 
			break;
		default:
			break;
		}	
		
		// sort
		Collections.sort(entries, (o1, o2) -> (o1.getReference().compareTo(o2.getReference()))); 
		referencesList.setItems(entries);
	}
	
	@FXML
	private void onSave(ActionEvent event) {
		switch(referenceType) {
		case PYGENDERTYPE:
			// path 1 - they selected a value
			if (possibleValuesCombo.getValue() != null && possibleValuesCombo.getValue() != "" ) {
				genderType = GenderType.valueOf(possibleValuesCombo.getValue());
				if (CreateReferenceCheck.isSelected() == true) {
	       			try {
						DynamicPayFileGenderReference newRef = new DynamicPayFileGenderReference();
						newRef.setReference(referenceField.getText());
						newRef.setGenderType(genderType);
						newRef.setSpecification(DataManager.i().mDynamicPayFileSpecification);
						newRef.setSpecificationId(DataManager.i().mDynamicPayFileSpecification.getId());
						DynamicPayFileGenderReferenceRequest req = new DynamicPayFileGenderReferenceRequest();
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
					genderType = referencesList.getSelectionModel().getSelectedItem().getPYGenderRef().getGenderType();
			}
			break;
		case EEGENDERTYPE:
			// path 1 - they selected a value
			if (possibleValuesCombo.getValue() != null && possibleValuesCombo.getValue() != "" ) {
				genderType = GenderType.valueOf(possibleValuesCombo.getValue());
				if (CreateReferenceCheck.isSelected() == true) {
	       			try {
						DynamicEmployeeFileGenderReference newRef = new DynamicEmployeeFileGenderReference();
						newRef.setReference(referenceField.getText());
						newRef.setGenderType(genderType);
						newRef.setSpecification(DataManager.i().mDynamicEmployeeFileSpecification);
						newRef.setSpecificationId(DataManager.i().mDynamicEmployeeFileSpecification.getId());
						DynamicEmployeeFileGenderReferenceRequest req = new DynamicEmployeeFileGenderReferenceRequest();
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
					genderType = referencesList.getSelectionModel().getSelectedItem().getPYGenderRef().getGenderType();
			}
			break;
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
		case PYPAYFREQUENCY:
			// path 1 - they selected a value
			if (possibleValuesCombo.getValue() != null && possibleValuesCombo.getValue() != "" ) {
				payFreqType = PayFrequencyType.valueOf(possibleValuesCombo.getValue());
				if (CreateReferenceCheck.isSelected() == true) {
	       			try {
						DynamicPayFilePayFrequencyReference newRef = new DynamicPayFilePayFrequencyReference();
						newRef.setCreatedById(DataManager.i().mLocalUser.getId());
						newRef.setCreatedBy(DataManager.i().mLocalUser);
						newRef.setReference(referenceField.getText());
						newRef.setPayFrequencyType(payFreqType);
						newRef.setSpecification(DataManager.i().mDynamicPayFileSpecification);
						newRef.setSpecificationId(DataManager.i().mDynamicPayFileSpecification.getId());
						DynamicPayFilePayFrequencyReferenceRequest req = new DynamicPayFilePayFrequencyReferenceRequest();
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
					payFreqType = referencesList.getSelectionModel().getSelectedItem().getPYPayFreqRef().getPayFrequencyType();
			}
			break;
		case PYUNIONTYPE:
			// path 1 - they selected a value
			if (possibleValuesCombo.getValue() != null && possibleValuesCombo.getValue() != "" ) {
				unionType = UnionType.valueOf(possibleValuesCombo.getValue());
				if (CreateReferenceCheck.isSelected() == true) {
	       			try {
						DynamicPayFileUnionTypeReference newRef = new DynamicPayFileUnionTypeReference();
						newRef.setCreatedById(DataManager.i().mLocalUser.getId());
						newRef.setCreatedBy(DataManager.i().mLocalUser);
						newRef.setReference(referenceField.getText());
						newRef.setUnionType(unionType);
						newRef.setSpecification(DataManager.i().mDynamicPayFileSpecification);
						newRef.setSpecificationId(DataManager.i().mDynamicPayFileSpecification.getId());
						DynamicPayFileUnionTypeReferenceRequest req = new DynamicPayFileUnionTypeReferenceRequest();
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
					unionType = referencesList.getSelectionModel().getSelectedItem().getPYUnionTypeRef().getUnionType();
			}
			break;
		case EEUNIONTYPE:
			// path 1 - they selected a value
			if (possibleValuesCombo.getValue() != null && possibleValuesCombo.getValue() != "" ) {
				unionType = UnionType.valueOf(possibleValuesCombo.getValue());
				if (CreateReferenceCheck.isSelected() == true) {
	       			try {
						DynamicEmployeeFileUnionTypeReference newRef = new DynamicEmployeeFileUnionTypeReference();
						newRef.setCreatedById(DataManager.i().mLocalUser.getId());
						newRef.setCreatedBy(DataManager.i().mLocalUser);
						newRef.setReference(referenceField.getText());
						newRef.setUnionType(unionType);
						newRef.setSpecification(DataManager.i().mDynamicEmployeeFileSpecification);
						newRef.setSpecificationId(DataManager.i().mDynamicPayFileSpecification.getId());
						DynamicEmployeeFileUnionTypeReferenceRequest req = new DynamicEmployeeFileUnionTypeReferenceRequest();
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
					unionType = referencesList.getSelectionModel().getSelectedItem().getPYUnionTypeRef().getUnionType();
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
	private void onCreateReference() {
/*		if (CreateReferenceCheck.isSelected() == false) return;
		
		// make sure dependent objects are present or need to be created
		switch(referenceType) {
		case PYPAYCODE:
			// check for matching paycode (PayConversionCalculationReference)
			if (PayConversionCalRefExists() == false) {
				Utils.showAlert("Missing PayCode (PayConversionCalculationReference)", "The reference \"" + fileReference + 
						"\"could not be found in the PayConversionCalculationReference table. Please create one when creating this CompType reference.");
			}
			break;
		default:
			break;
		} */
	}	

	private boolean PayConversionCalRefExists() {
		// check for matching paycode (PayConversionCalculationReference)
		try {
			if (fileReference != null && fileReference.isEmpty() == false) {
			PayConversionCalculationReferenceRequest pccReq = new PayConversionCalculationReferenceRequest();
			pccReq.setEmployerId(employer.getId());
			List<PayConversionCalculationReference> pccRefs =  AdminPersistenceManager.getInstance().getAll(pccReq);
			for (PayConversionCalculationReference pccRef : pccRefs) {
				if (pccRef.isActive() == false) continue;
				if (pccRef.getRefKey().contentEquals(fileReference)) 
					return true;
			}
		}				
		} catch (Exception e) {
			DataManager.i().log( Level.SEVERE, e);				
		}
		
		return true;
		
	}
	
	private void exitPopup() {
		Stage stage = (Stage) referencesList.getScene().getWindow();
		stage.close();
	}
	
	public static class HBoxReferenceCell extends HBox {
         Label lblReference = new Label();
         Label lblValue = new Label();
         DynamicPayFilePayCodeReference pyPayCodeRef;
         DynamicPayFileGenderReference pyGenderRef;
         DynamicPayFilePayFrequencyReference pyPayFreqRef;
         DynamicPayFileUnionTypeReference pyUnionTypeRef;
         DynamicEmployeeFilePayCodeReference eePayCodeRef;
         DynamicEmployeeFileGenderReference eeGenderRef;
         DynamicEmployeeFileUnionTypeReference eeUnionTypeRef;
         DynamicEmployeeFileCoverageGroupReference eeCoverageGroupRef;
         DynamicEmployeeFileDepartmentReference eeDepartmentRef;
         DynamicEmployeeFileLocationReference eeLocationRef;
         
         public DynamicPayFilePayCodeReference getPYPayRef() {
        	 return pyPayCodeRef;
         }

         public DynamicPayFileGenderReference getPYGenderRef() {
        	 return pyGenderRef;
         }

         public DynamicPayFilePayFrequencyReference getPYPayFreqRef() {
        	 return pyPayFreqRef;
         }

         public DynamicPayFileUnionTypeReference getPYUnionTypeRef() {
        	 return pyUnionTypeRef;
         }
         
         public DynamicEmployeeFileUnionTypeReference getEEUnionTypeRef() {
        	 return eeUnionTypeRef;
         }
         
         public DynamicEmployeeFileCoverageGroupReference getEECoverageGroupRef() {
        	 return eeCoverageGroupRef;
         }

         public DynamicEmployeeFilePayCodeReference getEEPayRef() {
        	 return eePayCodeRef;
         }

         public DynamicEmployeeFileGenderReference getEEGenderRef() {
        	 return eeGenderRef;
         }

         public DynamicEmployeeFileDepartmentReference getEEDepartmentRef() {
        	 return eeDepartmentRef;
         }

         public DynamicEmployeeFileLocationReference getEELocationRef() {
        	 return eeLocationRef;
         }

         public String getValue() {
        	 return lblValue.getText();
         }

         public String getReference() {
        	 return lblReference.getText();
         }

         HBoxReferenceCell(DynamicPayFileGenderReference genderRef) {
              super();

              if (genderRef != null) {
            	  this.pyGenderRef = genderRef;
            	  Utils.setHBoxLabel(lblReference, 260, false, genderRef.getReference());
            	  Utils.setHBoxLabel(lblValue, 110, false, genderRef.getGenderType().toString());
              } else {
            	  Utils.setHBoxLabel(lblReference, 260, true, "Reference");
            	  Utils.setHBoxLabel(lblValue, 110, true, "Value");
              }
        	  this.getChildren().addAll(lblReference, lblValue);
         }

         HBoxReferenceCell(DynamicEmployeeFileGenderReference eeGenderRef) {
             super();

             if (eeGenderRef != null) {
           	  this.eeGenderRef = eeGenderRef;
           	  Utils.setHBoxLabel(lblReference, 260, false, eeGenderRef.getReference());
           	  Utils.setHBoxLabel(lblValue, 110, false, eeGenderRef.getGenderType().toString());
             } else {
           	  Utils.setHBoxLabel(lblReference, 260, true, "Reference");
           	  Utils.setHBoxLabel(lblValue, 110, true, "Value");
             }
       	  this.getChildren().addAll(lblReference, lblValue);
        }

         HBoxReferenceCell(DynamicPayFilePayFrequencyReference payFreqRef) {
             super();

             if (payFreqRef != null) {
            	 this.pyPayFreqRef = payFreqRef;
	           	 Utils.setHBoxLabel(lblReference, 260, false, payFreqRef.getReference());
	           	 Utils.setHBoxLabel(lblValue, 110, false, payFreqRef.getPayFrequencyType().toString());
             } else {
            	 Utils.setHBoxLabel(lblReference, 260, true, "Reference");
           	  	 Utils.setHBoxLabel(lblValue, 110, true, "Value");
             }
       	  this.getChildren().addAll(lblReference, lblValue);
        }

         HBoxReferenceCell(DynamicPayFileUnionTypeReference unionTypeRef) {
             super();

             if (unionTypeRef != null) {
           	  this.pyUnionTypeRef = unionTypeRef;
           	  Utils.setHBoxLabel(lblReference, 260, false, unionTypeRef.getReference());
           	  Utils.setHBoxLabel(lblValue, 110, false, unionTypeRef.getUnionType().toString());
             } else {
           	  Utils.setHBoxLabel(lblReference, 260, true, "Reference");
           	  Utils.setHBoxLabel(lblValue, 110, true, "Value");
             }
       	  this.getChildren().addAll(lblReference, lblValue);
        }

         HBoxReferenceCell(DynamicEmployeeFileUnionTypeReference eeUnionTypeRef) {
             super();

             if (eeUnionTypeRef != null) {
           	  this.eeUnionTypeRef = eeUnionTypeRef;
           	  Utils.setHBoxLabel(lblReference, 260, false, eeUnionTypeRef.getReference());
           	  Utils.setHBoxLabel(lblValue, 110, false, eeUnionTypeRef.getUnionType().toString());
             } else {
           	  Utils.setHBoxLabel(lblReference, 260, true, "Reference");
           	  Utils.setHBoxLabel(lblValue, 110, true, "Value");
             }
       	  this.getChildren().addAll(lblReference, lblValue);
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

         HBoxReferenceCell(DynamicEmployeeFilePayCodeReference eePayRef) {
             super();

             if (eePayRef != null) {
           	  this.eePayCodeRef = eePayRef;
           	  Utils.setHBoxLabel(lblReference, 260, false, eePayRef.getReference());
           	  Utils.setHBoxLabel(lblValue, 110, false, eePayRef.getPayCodeType().toString());
             } else {
           	  Utils.setHBoxLabel(lblReference, 260, true, "Reference");
           	  Utils.setHBoxLabel(lblValue, 110, true, "Value");
             }
       	  this.getChildren().addAll(lblReference, lblValue);
        }

         HBoxReferenceCell(DynamicEmployeeFileCoverageGroupReference coverageGroupRef) {
             super();

             if (coverageGroupRef != null) {
           	  this.eeCoverageGroupRef = coverageGroupRef;
           	  Utils.setHBoxLabel(lblReference, 260, false, coverageGroupRef.getReference());
           	  Utils.setHBoxLabel(lblValue, 110, false, coverageGroupRef.getCoverageGroup().getName());
             } else {
           	  Utils.setHBoxLabel(lblReference, 260, true, "Reference");
           	  Utils.setHBoxLabel(lblValue, 110, true, "Value");
             }
       	  this.getChildren().addAll(lblReference, lblValue);
        }

         HBoxReferenceCell(DynamicEmployeeFileDepartmentReference departmentRef) {
             super();

             if (departmentRef != null) {
           	  this.eeDepartmentRef = departmentRef;
           	  Utils.setHBoxLabel(lblReference, 260, false, departmentRef.getReference());
           	  Utils.setHBoxLabel(lblValue, 110, false, departmentRef.getDepartment().getName());
             } else {
           	  Utils.setHBoxLabel(lblReference, 260, true, "Reference");
           	  Utils.setHBoxLabel(lblValue, 110, true, "Value");
             }
       	  this.getChildren().addAll(lblReference, lblValue);
        }

         HBoxReferenceCell(DynamicEmployeeFileLocationReference locationRef) {
             super();

             if (locationRef != null) {
           	  this.eeLocationRef = locationRef;
           	  Utils.setHBoxLabel(lblReference, 260, false, locationRef.getReference());
           	  Utils.setHBoxLabel(lblValue, 110, false, locationRef.getLocation().getName());
             } else {
           	  Utils.setHBoxLabel(lblReference, 260, true, "Reference");
           	  Utils.setHBoxLabel(lblValue, 110, true, "Value");
             }
       	  this.getChildren().addAll(lblReference, lblValue);
        }

	}	
	
}
