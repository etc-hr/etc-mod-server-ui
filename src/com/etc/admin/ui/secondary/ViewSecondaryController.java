package com.etc.admin.ui.secondary;

import java.awt.Point;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.coresysteminfo.ViewCoreSystemInfoController;
import com.etc.admin.ui.coverage.ViewCoverageController;
import com.etc.admin.utils.AddressGrid;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.data.DSSN;
import com.etc.corvetto.entities.Benefit;
import com.etc.corvetto.entities.Coverage;
import com.etc.corvetto.entities.Dependent;
import com.etc.corvetto.entities.EmployeeInformation;
import com.etc.corvetto.entities.Person;
import com.etc.corvetto.rqs.BenefitRequest;
import com.etc.corvetto.rqs.CoverageGroupMembershipRequest;
import com.etc.corvetto.rqs.CoverageRequest;
import com.etc.corvetto.rqs.DSSNRequest;
import com.etc.corvetto.rqs.DependentRequest;
import com.etc.corvetto.rqs.EmployeeInformationRequest;
import com.etc.corvetto.rqs.PersonRequest;
import com.etc.corvetto.rqs.PostalAddressRequest;
import com.etc.embeds.SSN;
import com.etc.entities.CoreData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewSecondaryController {
	
	@FXML
	private GridPane secNameGrid;
	@FXML
	private TextField secFirstNameLabel;
	@FXML
	private TextField secMiddleNameLabel;
	@FXML
	private TextField secLastNameLabel;
	@FXML
	private TextField secDOBLabel;
	@FXML
	private TextField secSecIdLabel;
	@FXML
	private TextField secSSNLabel;
	// MAIL ADDRESS
	@FXML
	private Button secEditButton;
	@FXML
	private Button secSaveButton;
	@FXML
	private Button secSSNButton;
	// coverages
	@FXML
	private Label secCoverageLabel;
	@FXML
	private ListView<HBoxCoverageCell> secCoverageListView;
	// custom fields
	@FXML
	private TextField secCustomField1;
	@FXML
	private TextField secCustomField2;
	@FXML
	private TextField secCustomField3;
	@FXML
	private TextField secCustomField4;
	@FXML
	private TextField secCustomField5;

	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	boolean ssnEdited = false;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();

		getSecondaryData();
		// load the coverages
		updateCoverages();
	}
	
	private void initControls() {
		// hide the editSSN button until it is showing
		secSSNButton.setText("Show SSN");
		//reset edited flag
		ssnEdited = false;
		// set the edit mode
		setEditMode(false);
		// COVERAGES
		secCoverageListView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
            	//set the selected coverage
				DataManager.i().mCoverage = secCoverageListView.getSelectionModel().getSelectedItem().getCoverage();
				// and display the pop up
				try {
		            // load the fxml
			        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coverage/ViewCoverage.fxml"));
					Parent ControllerNode = loader.load();
			        ViewCoverageController coverageController = (ViewCoverageController) loader.getController();
			        coverageController.setSecondaryMode();
			        Stage stage = new Stage();
			        stage.initModality(Modality.APPLICATION_MODAL);
			        stage.initStyle(StageStyle.UNDECORATED);
			        stage.setScene(new Scene(ControllerNode));  
			        EtcAdmin.i().positionStageCenter(stage);
			        stage.showAndWait();
			        updateCoverages();
				} catch (IOException e) {
		        	DataManager.i().log(Level.SEVERE, e); 
				}        		
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
            }
        });
	}

	private void getSecondaryData() 
	{
		
		updateSecondaryData();
	}
	
	private void updateSecondaryData()
	{
		if (DataManager.i().mDependent != null) 
		{
			Dependent sec = DataManager.i().mDependent;
			Utils.updateControl(secFirstNameLabel,sec.getPerson().getFirstName());
			Utils.updateControl(secMiddleNameLabel,sec.getPerson().getMiddleName());
			Utils.updateControl(secLastNameLabel,sec.getPerson().getLastName());
			Utils.updateControl(secDOBLabel,sec.getPerson().getDateOfBirth());
			// SSN
			if (ssnEdited == false) {
				if (sec.getPerson().getSsn() != null)
					secSSNLabel.setText("XXX-XX-" + sec.getPerson().getSsn().getUsrln());
				else
					secSSNLabel.setText("XXX-XX-XXXX");
			}
			// cusotm fields
			Utils.updateControl(secCustomField1,sec.getCustomField1());
			Utils.updateControl(secCustomField2,sec.getCustomField2());
			Utils.updateControl(secCustomField3,sec.getCustomField3());
			Utils.updateControl(secCustomField4,sec.getCustomField4());
			Utils.updateControl(secCustomField5,sec.getCustomField5());
		}
		
		//set functionality according to the user security level
		secEditButton.setDisable(!Utils.userCanEdit());
	}
	
	private void updateDependent()
	{
		//update the dependent
		try {
			DataManager.i().mDependent.getPerson().setFirstName(secFirstNameLabel.getText());
			DataManager.i().mDependent.getPerson().setMiddleName(secMiddleNameLabel.getText());
			DataManager.i().mDependent.getPerson().setLastName(secLastNameLabel.getText());
			// custom fields
			DataManager.i().mDependent.setCustomField1(secCustomField1.getText());
			DataManager.i().mDependent.setCustomField2(secCustomField2.getText());
			DataManager.i().mDependent.setCustomField3(secCustomField3.getText());
			DataManager.i().mDependent.setCustomField4(secCustomField4.getText());
			DataManager.i().mDependent.setCustomField5(secCustomField5.getText());
			//calendar dates
			if (secDOBLabel.getText() != null && !secDOBLabel.getText().isEmpty())
				DataManager.i().mDependent.getPerson().setDateOfBirth(Utils.getCalDate(secDOBLabel.getText()));
			//create the request
			DependentRequest request = new DependentRequest();
			request.setEntity(DataManager.i().mDependent);
			request.setId(DataManager.i().mDependent.getId());
			// dependent needs a person request attached
			PersonRequest pReq = new PersonRequest();
			pReq.setEntity(DataManager.i().mDependent.getPerson());
			pReq.setId(DataManager.i().mDependent.getPerson().getId());
			// person needs an address request attached
			PostalAddressRequest aReq = new PostalAddressRequest(); 
			aReq.setEntity(DataManager.i().mDependent.getPerson().getMailAddress());
			aReq.setId(DataManager.i().mDependent.getPerson().getMailAddress().getId());
			// assign the mail request to the person request
			pReq.setMailAddressRequest(aReq);
			// assign the person request to the dependent request
			//AdminPersistenceManager.getInstance().addOrUpdate(pReq);
			request.setPersonRequest(pReq);
			// and update the server
			DataManager.i().mDependent = AdminPersistenceManager.getInstance().addOrUpdate(request);
		} catch (CoreException e) {
        	DataManager.i().log( Level.SEVERE, e);
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		//update the ssn if modified
		saveSSN();
	}
	
	private void saveSSN()
	{
		if (ssnEdited == false)
			return;
		
		// verify
		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmAlert.setTitle("Changing SSN");
	    String confirmText = "Are You Sure You Want To Modify the SSN?";
	    confirmAlert.setContentText(confirmText);
	    EtcAdmin.i().positionAlertCenter(confirmAlert);
	    Optional<ButtonType> result = confirmAlert.showAndWait();
	    if ((result.isPresent()) && (result.get() != ButtonType.OK)) {
	    	return;
	    }
		
		try { 
			// encrypt the SSN text and save 
			SSN ssn = new SSN(Utils.encryptSSN(secSSNLabel.getText()));	 
			if (ssn != null && DataManager.i().mDependent.getPerson() != null) { 
				// create the request and send it to the server 
				PersonRequest request = new PersonRequest(); 
				Person person = DataManager.i().mDependent.getPerson(); 
				person.setSsn(ssn); 
				request.setEntity(person); 
				request.setId(person.getId()); 
				AdminPersistenceManager.getInstance().addOrUpdate(request); 
			} 
		} catch (Exception e) { 
        	DataManager.i().log(Level.SEVERE, e);  
		} 
	}
	
	private void updateCoverages() 
	{
		try
		{
		
			secCoverageListView.getItems().clear();
			secCoverageLabel.setText("Coverages(loading...)"); 
			if (DataManager.i().mDependent.getPerson() == null ) {
				secCoverageLabel.setText("Coverages(total: 0)"); 
				return;
			}
			
			// new thread
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception 
	            {
	          		CoverageRequest cgRequest = new CoverageRequest();
	          		cgRequest.setFetchInactive(true);
	          		cgRequest.setPersonId(DataManager.i().mDependent.getPerson().getId());
	          		DataManager.i().mDependent.getPerson().setCoverages(AdminPersistenceManager.getInstance().getAll(cgRequest));
	                return null;
	            }
	        };
	        
	    	task.setOnSucceeded(e ->  { 
	    		showCoverages(); 
	    	});
	    	task.setOnFailed(e ->  { 
	    		showCoverages(); 
	    	});
	        
	    	EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e)
		{
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void showCoverages() 
	{
		secCoverageListView.getItems().clear();
	    if (DataManager.i().mDependent == null || DataManager.i().mDependent.getPerson() == null) return;
	    
		if (DataManager.i().mDependent.getPerson().getCoverages() != null)
		{
		    List<HBoxCoverageCell> coverageList = new ArrayList<HBoxCoverageCell>();
			
		    if (DataManager.i().mDependent.getPerson().getCoverages().size() > 0)
		    	coverageList.add(new HBoxCoverageCell(null));
			
		    for (Coverage coverage : DataManager.i().mDependent.getPerson().getCoverages())
		    	coverageList.add(new HBoxCoverageCell(coverage));
			
			ObservableList<HBoxCoverageCell> myObservableCoverageList = FXCollections.observableList(coverageList);
			secCoverageListView.setItems(myObservableCoverageList);		
			
			//update our employer screen label
			secCoverageLabel.setText("Coverages (total: " + String.valueOf(DataManager.i().mDependent.getPerson().getCoverages().size()) + ")");
		} else {
			secCoverageLabel.setText("Coverages (total: 0)");			
		}
		
		// reset the status
		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0);
	}
	
	private void setEditMode(boolean mode) 
	{
		if (mode == true) 
		{
			secEditButton.setText("Cancel");
			secNameGrid.setStyle("-fx-background-color: #eeffee");
		}else {
			secEditButton.setText("Edit");
			secNameGrid.getStyleClass().clear();
			secNameGrid.setStyle(null);	
		}
		
		secSaveButton.setVisible(mode);
		// NAME GRID
		secFirstNameLabel.setEditable(mode);
		secMiddleNameLabel.setEditable(mode);
		secLastNameLabel.setEditable(mode);
		secDOBLabel.setEditable(mode);
		//ssn
		secSSNLabel.setEditable(mode);
	}	
	
	@FXML
	private void onShowSystemInfo() {
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mDependent;
			DataManager.i().mCurrentCoreDataType = SystemDataType.DEPENDENT;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
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
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) secFirstNameLabel.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onShowHideSSN(ActionEvent event) {
		if (secSSNButton.getText().contentEquals("Show SSN")) {
			secSSNButton.setText("Hide SSN");
			DSSNRequest ssnRequest = new DSSNRequest();
			ssnRequest.setDependentId(DataManager.i().mDependent.getId());
			ssnRequest.setId(DataManager.i().mDependent.getId());
			DSSN dssn = null;
			try {
				dssn = AdminPersistenceManager.getInstance().get(ssnRequest);
			} catch (CoreException e) {
	        	DataManager.i().log(Level.SEVERE, e); 
			}
    	    catch (Exception e) {  DataManager.i().logGenericException(e); }
			if (dssn != null)
				secSSNLabel.setText(dssn.getDssn());
			else
				secSSNLabel.setText("XXX-XX-XXXX");
		} else {
			secSSNButton.setText("Show SSN");
			if (DataManager.i().mDependent.getPerson().getSsn() != null)
				secSSNLabel.setText("XXX-XX-" + DataManager.i().mDependent.getPerson().getSsn().getUsrln());
			else
				secSSNLabel.setText("XXX-XX-XXXX");
		}
		
		//reset edited flag
		ssnEdited = false;
		// reset any errors
		secSSNLabel.setStyle(null);
	}

/*	@FXML
	private void onEditSSN(ActionEvent event) {
		if (secEditSSNButton.getText().contentEquals("Edit SSN")) {
			secSSNButton.setText("Save SSN");
			secEditSSNButton.setText("Cancel");
			secSSNLabel.setEditable(true);
			secSSNLabel.setStyle("-fx-background-color: #ccffcc");
			secSSNLabel.requestFocus();

		} else {
			secSSNButton.setText("Show SSN");
			secEditSSNButton.setText("Edit SSN");
			secEditSSNButton.setVisible(false);
			if (DataManager.i().mDependent.getPerson().getSsn() != null)
				secSSNLabel.setText("XXX-XX-" + DataManager.i().mDependent.getPerson().getSsn().getUsrln());
			else
				secSSNLabel.setText("XXX-XX-XXXX");
			secSSNLabel.setEditable(false);
			//reset the label color
			secSSNLabel.setStyle(null);
		}
	}
*/	
	private boolean validateData() 
	{
		boolean bReturn = true;

		if ( !Utils.validate(secFirstNameLabel)) bReturn = false;
		if ( !Utils.validate(secLastNameLabel)) bReturn = false;
		//if (mailAddress.validateData() == false) bReturn = false;
		if (ssnEdited == true) {
			if(Utils.validateSSNTextField(secSSNLabel) == false)
				bReturn = false;
		}
		
		return bReturn;
	}	
	
	@FXML
	private void onEdit(ActionEvent event) 
	{
		if (secEditButton.getText().equals("Edit"))
			setEditMode(true);
		else
			setEditMode(false);
	}

	@FXML
	private void onSSNEdit() {
		ssnEdited = true;
	}
	
	@FXML
	private void onSave(ActionEvent event) 
	{
		// validate the informaiton
		if (!validateData())
			return;
		
		// update the dependent
		updateDependent();
		
		// and take everything out of edit mode
		setEditMode(false);
		// mark the refresh so the employee screen will reload
		EtcAdmin.i().setScreenRefresh(true);
	}

	@FXML
	private void onAddCoverage(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.SECONDARYEDIT, true);
	}

	public static class HBoxCoverageCell extends HBox 
	{
        Label lblId = new Label();
        Label lblStartDate = new Label();
        Label lblEndDate = new Label();
        Label lblBenefitName = new Label();
        Label lblTitle3 = new Label();
        CheckBox cbCobra = new CheckBox();
        Coverage coverage;
        
        public Coverage getCoverage() { return coverage; }

        HBoxCoverageCell(Coverage coverage) 
        {
             super();
             
             this.coverage = coverage;
             int columnWidth = 100;
             if (coverage != null) 
             {
            	 Utils.setHBoxLabel(lblId, columnWidth, false, String.valueOf(coverage.getId()));
            	 Utils.setHBoxLabel(lblStartDate, columnWidth, false,Utils.getDateString(coverage.getStartDate()));
            	 Utils.setHBoxLabel(lblEndDate, columnWidth, false,Utils.getDateString(coverage.getEndDate()));
            	 
 				 String bName = "";
 				 try {
 					  BenefitRequest bReq = new BenefitRequest();
 					  bReq.setId(coverage.getBenefitId());
 					  Benefit bf = AdminPersistenceManager.getInstance().get(bReq);
 					  if (bf != null)
 						  bName = bf.getProviderReference();
 				 } catch (CoreException e) {
 		        	DataManager.i().log(Level.SEVERE, e); 
 				 }
	        	 catch (Exception e) {  DataManager.i().logGenericException(e); }
				 Utils.setHBoxLabel(lblBenefitName, 250, false,bName);
            	 Utils.setHBoxCheckBox(cbCobra, columnWidth, coverage.isCobra());
              	  this.getChildren().addAll(lblId, lblStartDate, lblEndDate, lblBenefitName, cbCobra);
             }else {
            	 Utils.setHBoxLabel(lblId, columnWidth, true, "ID");
            	 Utils.setHBoxLabel(lblStartDate, columnWidth, true, "Start Date");
            	 Utils.setHBoxLabel(lblEndDate, columnWidth, true, "End Date");
            	 Utils.setHBoxLabel(lblBenefitName, 250, true, "Plan");
            	 Utils.setHBoxLabel(lblTitle3, columnWidth, true, "Cobra");
              	  this.getChildren().addAll(lblId, lblStartDate, lblEndDate, lblBenefitName, lblTitle3);                  
             }
        }
    }	
}
