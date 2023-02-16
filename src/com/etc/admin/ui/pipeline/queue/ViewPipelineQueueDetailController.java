package com.etc.admin.ui.pipeline.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.embeds.PipelineInformation;
import com.etc.corvetto.ems.pipeline.entities.PipelineChannel;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileNotice;
import com.etc.corvetto.ems.pipeline.entities.PipelineRequest;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;
import com.etc.corvetto.ems.pipeline.entities.airerr.AirTranErrorFile;
import com.etc.corvetto.ems.pipeline.entities.airrct.AirTranReceiptFile;
import com.etc.corvetto.ems.pipeline.entities.c94.Irs1094cFile;
import com.etc.corvetto.ems.pipeline.entities.c95.Irs1095cFile;
import com.etc.corvetto.ems.pipeline.entities.cov.CoverageFile;
import com.etc.corvetto.ems.pipeline.entities.emp.EmployeeFile;
import com.etc.corvetto.ems.pipeline.entities.pay.PayFile;
import com.etc.corvetto.ems.pipeline.entities.ppd.PayPeriodFile;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.Document;
import com.etc.entities.CoreSystemInstance;
import com.etc.entities.Endpoint;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ViewPipelineQueueDetailController {
	// REQUEST
	@FXML
	private TextField pqueDescriptionField;
	@FXML
	private TextField pqueProcessOnField;
	@FXML
	private TextField pqueCompletedOnField;
	@FXML
	private TextField pqueStatusField;
	@FXML
	private TextArea pqueResultField;
	@FXML
	private Label pqueCoreIdLabel;
	@FXML
	private Label pqueCoreActiveLabel;
	@FXML
	private Label pqueCoreBODateLabel;
	@FXML
	private Label pqueCoreLastUpdatedLabel;
	// PIPELINE INFORMATION
	@FXML
	private TextField pqueInformationInitializedField;
	@FXML
	private TextField pqueInformationParsedField;
	@FXML
	private TextField pqueInformationValidatedField;
	@FXML
	private TextField pqueInformationConvertedField;
	@FXML
	private TextField pqueInformationFinalizedField;
	@FXML
	private TextField pqueInformationInitializedDateField;
	@FXML
	private TextField pqueInformationParsedDateField;
	@FXML
	private TextField pqueInformationValidatedDateField;
	@FXML
	private TextField pqueInformationConvertedDateField;
	@FXML
	private TextField pqueInformationFinalizedDateField;
	@FXML
	private TextField pqueInformationInitializedDurationField;
	@FXML
	private TextField pqueInformationParsedDurationField;
	@FXML
	private TextField pqueInformationValidatedDurationField;
	@FXML
	private TextField pqueInformationConvertedDurationField;
	@FXML
	private TextField pqueInformationFinalizedDurationField;
	@FXML
	private CheckBox pqueInformationDiscardedCheckBox;
	@FXML
	private CheckBox pqueInformationRejectedCheckBox;
	@FXML
	private CheckBox pqueInformationCompletedCheckBox;
	@FXML
	private TextField pqueInformationRecordsField;
	@FXML
	private TextField pqueInformationUnitsField;
	@FXML
	private TextField pqueInformationAccountField;
	@FXML
	private TextField pqueInformationEmployerField;
	@FXML
	private Button pqueInformationRejectButton;
	@FXML
	private Button pqueInformationConfirmRejectButton;
	@FXML
	private Button pqueInformationCancelRejectButton;
	@FXML
	private Button pqueInformationAccountButton;
	@FXML
	private Button pqueInformationEmployerButton;
	@FXML
	private Button pqueInformationSpecificationButton;
	// DOCUMENT
	@FXML
	private TextField pqueDocumentNameField;
	@FXML
	private TextField pqueDocumentSizeField;
	@FXML
	private TextField pqueDocumentDescriptionField;
	@FXML
	private TextField pqueDocumentNameFileNameField;
	@FXML
	private TextField pqueDocumentDocTypeField;
	@FXML
	private Label pqueDocumentCoreIdLabel;
	@FXML
	private Label pqueDocumentCoreActiveLabel;
	@FXML
	private Label pqueDocumentCoreBODateLabel;
	@FXML
	private Label pqueDocumentCoreLastUpdatedLabel;
	// COVERAGE FILE
	@FXML
	private VBox pqueCoverageFileVBox;
	@FXML
	private TextField pqueCoverageEmployeesCreatedField;
	@FXML
	private TextField pqueCoverageEmployeesUpdatedField;
	@FXML
	private TextField pqueCoverageSecondariesCreatedField;
	@FXML
	private TextField pqueCoverageSecondariesUpdatedField;
	@FXML
	private TextField pqueCoverageEmpCovPeriodsCreatedField;
	@FXML
	private TextField pqueCoverageEmpCovPeriodsUpdatedField;
	@FXML
	private TextField pqueCoverageSecCovPeriodsCreatedField;
	@FXML
	private TextField pqueCoverageSecCovPeriodsUpdatedField;
	@FXML
	private TextField pqueCoverageNoChangeCountField;
	@FXML
	private Button pqueCoverageViewFieldsButton;
	@FXML
	private Label CoverageNoticesListLabel;
	@FXML
	private ListView<HBoxCell> CoverageNoticesListView;
	@FXML
	private Label pqueCoverageCoreIdLabel;
	@FXML
	private Label pqueCoverageCoreActiveLabel;
	@FXML
	private Label pqueCoverageCoreBODateLabel;
	@FXML
	private Label pqueCoverageCoreLastUpdatedLabel;
	// EMPLOYEE FILE
	@FXML
	private VBox pqueEmployeeFileVBox;
	@FXML
	private TextField pqueEmployeeCreatedField;
	@FXML
	private TextField pqueEmployeeUpdatedField;
	@FXML
	private TextField pqueEmployeeNoChangeCountField;
	@FXML
	private Button pqueEmployeeViewFieldsButton;
	@FXML
	private Label EmployeeNoticesListLabel;
	@FXML
	private ListView<HBoxCell> EmployeeNoticesListView;
	@FXML
	private Label pqueEmployeeCoreIdLabel;
	@FXML
	private Label pqueEmployeeCoreActiveLabel;
	@FXML
	private Label pqueEmployeeCoreBODateLabel;
	@FXML
	private Label pqueEmployeeCoreLastUpdatedLabel;
	// PAY FILE
	@FXML
	private VBox pquePayFileVBox;
	@FXML
	private TextField pquePayEmployeesCreatedField;
	@FXML
	private TextField pquePayEmployeesUpdatedField;
	@FXML
	private TextField pquePayCreatedField;
	@FXML
	private TextField pquePayUpdatedField;
	@FXML
	private TextField pquePayNoChangeCountField;
	@FXML
	private Button pquePayViewFieldsButton;
	@FXML
	private Label PayNoticesListLabel;
	@FXML
	private ListView<HBoxCell> PayNoticesListView;
	@FXML
	private Label pquePayCoreIdLabel;
	@FXML
	private Label pquePayCoreActiveLabel;
	@FXML
	private Label pquePayCoreBODateLabel;
	@FXML
	private Label pquePayCoreLastUpdatedLabel;
	// AIRTRANFILE
	@FXML
	private VBox pqueAirTranFileVBox;
	@FXML
	private TextField pqueAirTranTransmissionsUpdatedField;
	@FXML
	private TextField pqueAirTranNoChangeCountField;
	@FXML
	private Button pqueAirTranViewFieldsButton;
	@FXML
	private Label AirTranNoticesListLabel;
	@FXML
	private ListView<HBoxCell> AirTranNoticesListView;
	@FXML
	private Label pqueAirTranCoreIdLabel;
	@FXML
	private Label pqueAirTranCoreActiveLabel;
	@FXML
	private Label pqueAirTranCoreBODateLabel;
	@FXML
	private Label pqueAirTranCoreLastUpdatedLabel;
	// IRS1094CFILE
	@FXML
	private VBox pqueIrs1094cFileVBox;
	@FXML
	private TextField pqueIrs1094cCreatedField;
	@FXML
	private TextField pqueIrs1094cUpdatedField;
	@FXML
	private TextField pqueIrs1094cNoChangeCountField;
	@FXML
	private Button pqueIrs1094cViewFieldsButton;
	@FXML
	private Label Irs1094cNoticesListLabel;
	@FXML
	private ListView<HBoxCell> Irs1094cNoticesListView;
	@FXML
	private Label pqueIrs1094cCoreIdLabel;
	@FXML
	private Label pqueIrs1094cCoreActiveLabel;
	@FXML
	private Label pqueIrs1094cCoreBODateLabel;
	@FXML
	private Label pqueIrs1094cCoreLastUpdatedLabel;
	// IRS1095CFILE
	@FXML
	private VBox pqueIrs1095cFileVBox;
	@FXML
	private TextField pqueIrs1095cEmployeesCreatedField;
	@FXML
	private TextField pqueIrs1095cEmployeesUpdatedField;
	@FXML
	private TextField pqueIrs1095cSecondariesCreatedField;
	@FXML
	private TextField pqueIrs1095cSecondariesUpdatedField;
	@FXML
	private TextField pqueIrs1095cNoChangeCountField;
	@FXML
	private Button pqueIrs1095cViewFieldsButton;
	@FXML
	private Label Irs1095cNoticesListLabel;
	@FXML
	private ListView<HBoxCell> Irs1095cNoticesListView;
	@FXML
	private Label pqueIrs1095cCoreIdLabel;
	@FXML
	private Label pqueIrs1095cCoreActiveLabel;
	@FXML
	private Label pqueIrs1095cCoreBODateLabel;
	@FXML
	private Label pqueIrs1095cCoreLastUpdatedLabel;
	// PAYPERIODFILE
	@FXML
	private VBox pquePayPeriodFileVBox;
	@FXML
	private TextField pquePayPeriodsCreatedField;
	@FXML
	private TextField pquePayPeriodsUpdatedField;
	@FXML
	private TextField pquePayPeriodsNoChangeCountField;
	@FXML
	private Button pquePayPeriodViewFieldsButton;
	@FXML
	private Label PayPeriodNoticesListLabel;
	@FXML
	private ListView<HBoxCell> PayPeriodNoticesListView;
	@FXML
	private Label pquePayPeriodCoreIdLabel;
	@FXML
	private Label pquePayPeriodCoreActiveLabel;
	@FXML
	private Label pquePayPeriodCoreBODateLabel;
	@FXML
	private Label pquePayPeriodCoreLastUpdatedLabel;
	// CORE INSTANCE
	@FXML
	private GridPane pqueCoreInstanceHeader;
	@FXML
	private GridPane pqueCoreInstanceBody;
	@FXML
	private TextField pqueInstanceIdField;
	@FXML
	private TextField pqueInstanceLastCheckInField;
	@FXML
	private Label pqueInstanceCoreIdLabel;
	@FXML
	private Label pqueInstanceCoreActiveLabel;
	@FXML
	private Label pqueInstanceCoreBODateLabel;
	@FXML
	private Label pqueInstanceCoreLastUpdatedLabel;
	// ENDPOINT
	@FXML
	private TextField pqueEndpointIdField;
	@FXML
	private TextField pqueEndpointKeyField;
	@FXML
	private TextField pqueEndpointHostField;
	@FXML
	private TextField pqueEndpointVMField;
	@FXML
	private TextField pqueEndpointOSField;
	// members for access. Getting everything up front due to complexity
	PipelineRequest selPLRequest = null;
	PipelineSpecification selPLSpec = null;
	PipelineChannel selPLChannel = null;
	PipelineInformation selPLInfo = null;
	EmployeeFile employeeFile = null;
	PayFile payFile = null;
	PayPeriodFile payPeriodFile = null;
	CoverageFile coverageFile = null;
	AirTranErrorFile airTranErrorFile = null;
	AirTranReceiptFile airTranReceiptFile = null;
	Irs1094cFile irs1094cFile = null;
	Irs1095cFile irs1095cFile = null;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		updateQueueData();
	}
	
	private void initControls() {
		//hide the coreinstance data for now. Noto sure if are worth the bandwidth, given the business use here.
		pqueCoreInstanceHeader.setVisible(false);
		pqueCoreInstanceBody.setVisible(false);
		
		//hide the file sections until the correct one is selected
		hideFileSections();
		
		//clear the controls
		clearControls();
	}
	
	private void clearControls() {
		pqueInformationInitializedField.setStyle("-fx-background-color:#ffffff");
		pqueInformationParsedField.setStyle("-fx-background-color:#ffffff");
		pqueInformationValidatedField.setStyle("-fx-background-color:#ffffff");
		pqueInformationConvertedField.setStyle("-fx-background-color:#ffffff");
		pqueInformationFinalizedField.setStyle("-fx-background-color:#ffffff");
		
		pqueInformationInitializedDateField.setText("");
		pqueInformationParsedDateField.setText("");
		pqueInformationValidatedDateField.setText("");
		pqueInformationConvertedDateField.setText("");
		pqueInformationFinalizedDateField.setText("");
		
		pqueInformationInitializedDurationField.setText("");
		pqueInformationParsedDurationField.setText("");
		pqueInformationValidatedDurationField.setText("");
		pqueInformationConvertedDurationField.setText("");
		pqueInformationFinalizedDurationField.setText("");
		
		pqueInformationDiscardedCheckBox.setSelected(false);
		pqueInformationRejectedCheckBox.setSelected(false);
		pqueInformationCompletedCheckBox.setSelected(false);
		
		pqueInformationRecordsField.setText("");
		pqueInformationUnitsField.setText("");
		pqueInformationAccountField.setText("");
		pqueInformationEmployerField.setText("");
	}

	private boolean getPipelineMembers() {
		PipelineQueue queue = DataManager.i().mPipelineQueueEntry;
		if (queue == null) return false;
		selPLRequest = queue.getRequest();
		if (selPLRequest == null) return false;
		selPLSpec = selPLRequest.getSpecification();
		if (selPLSpec == null) return false;
		selPLChannel = selPLSpec.getChannel();
		
		//get the pipeline information from the file specified by file type and id
		switch(selPLChannel.getType()) {
		case EMPLOYEE:
			employeeFile = queue.getEmployeeFile();
			if (employeeFile == null) return false;
			selPLInfo = employeeFile.getPipelineInformation();
			if (selPLInfo == null) return false;
			setEmployeeFileData();
			break;
		case COVERAGE:
			coverageFile = queue.getCoverageFile();
			if (coverageFile == null) return false;
			selPLInfo = coverageFile.getPipelineInformation();
			if (selPLInfo == null) return false;
			setCoverageFileData();
			break;
		case IRS1094C:
			irs1094cFile = queue.getIrs1094cFile();
			if (irs1094cFile == null) return false;
			selPLInfo = irs1094cFile.getPipelineInformation();
			if (selPLInfo == null) return false;
			setIrs1094cFileData();
			break;
		case IRS1095C:
			irs1095cFile = queue.getIrs1095cFile();
			if (irs1095cFile == null) return false;
			selPLInfo = irs1095cFile.getPipelineInformation();
			if (selPLInfo == null) return false;
			setIrs1095cFileData();
			break;
		case PAY:
			payFile = queue.getPayFile();
			if (payFile == null) return false;
			selPLInfo = payFile.getPipelineInformation();
			if (selPLInfo == null) return false;
			setPayFileData();
			break;
		case PAYPERIOD:
			payPeriodFile = queue.getPayPeriodFile();
			if (payPeriodFile == null) return false;
			selPLInfo = payPeriodFile.getPipelineInformation();
			if (selPLInfo == null) return false;
			setPayPeriodFileData();
			break;
		case IRSAIRERR:
			airTranErrorFile = queue.getAirTranErrorFile();
			if (airTranErrorFile == null) return false;
			selPLInfo = airTranErrorFile.getPipelineInformation();
			if (selPLInfo == null) return false;
			setAirTranErrorFileData();
			break;
		case IRSAIRRCPT:
			airTranReceiptFile = queue.getAirTranReceiptFile();
			if (airTranReceiptFile == null) return false;
			selPLInfo = airTranReceiptFile.getPipelineInformation();
			if (selPLInfo == null) return false;
			setAirTranReceiptFileData();
			break;
		default: // it's not a file type
			return false;
		}
		
		// we have our data members, so return true
		return true;
	}
	
	private void hideFileSections() {
		pqueCoverageFileVBox.setVisible(false);
		pqueCoverageFileVBox.setMaxHeight(1);
		pqueCoverageFileVBox.setMinHeight(1);
		pqueCoverageFileVBox.setPrefHeight(1);
		pqueEmployeeFileVBox.setVisible(false);
		pqueEmployeeFileVBox.setMaxHeight(1);
		pqueEmployeeFileVBox.setMinHeight(1);
		pqueEmployeeFileVBox.setPrefHeight(1);
		pquePayFileVBox.setVisible(false);
		pquePayFileVBox.setMaxHeight(1);
		pquePayFileVBox.setMinHeight(1);
		pquePayFileVBox.setPrefHeight(1);
		pqueAirTranFileVBox.setVisible(false);
		pqueAirTranFileVBox.setMaxHeight(1);
		pqueAirTranFileVBox.setMinHeight(1);
		pqueAirTranFileVBox.setPrefHeight(1);
		pquePayPeriodFileVBox.setVisible(false);
		pquePayPeriodFileVBox.setMaxHeight(1);
		pquePayPeriodFileVBox.setMinHeight(1);
		pquePayPeriodFileVBox.setPrefHeight(1);
		pqueIrs1094cFileVBox.setVisible(false);
		pqueIrs1094cFileVBox.setMaxHeight(1);
		pqueIrs1094cFileVBox.setMinHeight(1);
		pqueIrs1094cFileVBox.setPrefHeight(1);
		pqueIrs1095cFileVBox.setVisible(false);
		pqueIrs1095cFileVBox.setMaxHeight(1);
		pqueIrs1095cFileVBox.setMinHeight(1);
		pqueIrs1095cFileVBox.setPrefHeight(1);
	}
	
	private void updateQueueData(){
		//reset the file sections
		hideFileSections();
		
		//get our members, which will also give us our filetype
		if (getPipelineMembers() == false) return;
		
		//update the screen controls
		Utils.updateControl(pqueDescriptionField, selPLRequest.getDescription());
		Utils.updateControl(pqueProcessOnField, selPLRequest.getProcessOn());
		Utils.updateControl(pqueCompletedOnField, selPLRequest.getCompletedOn());
		Utils.updateControl(pqueStatusField, selPLRequest.getStatus().toString());
		Utils.updateControl(pqueResultField, selPLRequest.getResult());
		Utils.updateControl(pqueCoreIdLabel, selPLRequest.getId());
		Utils.updateControl(pqueCoreActiveLabel, selPLRequest.isActive());
		Utils.updateControl(pqueCoreBODateLabel, selPLRequest.getBornOn());
		Utils.updateControl(pqueCoreLastUpdatedLabel, selPLRequest.getLastUpdated());

		// PIPELINE INFORMATION
		Utils.updateControl(pqueInformationInitializedField, " #aaffaa", " #ffbbbb", selPLInfo.isInitialized());
		Utils.updateControl(pqueInformationParsedField, " #aaffaa", " #ffbbbb", selPLInfo.isParsed());
		Utils.updateControl(pqueInformationValidatedField, " #aaffaa", " #ffbbbb", selPLInfo.isValidated());
		Utils.updateControl(pqueInformationConvertedField, " #aaffaa", " #ffbbbb", selPLInfo.isConverted());
		Utils.updateControl(pqueInformationFinalizedField, " #aaffaa", " #ffbbbb", selPLInfo.isFinalized());
		Utils.updateControl(pqueInformationInitializedDateField, selPLInfo.getInitializedOn());
		Utils.updateControl(pqueInformationParsedDateField, selPLInfo.getParsedOn());
		Utils.updateControl(pqueInformationValidatedDateField, selPLInfo.getLastValidation());
		Utils.updateControl(pqueInformationConvertedDateField, selPLInfo.getLastConversion());
		Utils.updateControl(pqueInformationFinalizedDateField, selPLInfo.getLastFinalized());
		Utils.updateControl(pqueInformationInitializedDurationField, selPLInfo.getInitializationDuration());
		Utils.updateControl(pqueInformationParsedDurationField, selPLInfo.getParseDuration());
		Utils.updateControl(pqueInformationValidatedDurationField, selPLInfo.getValidationDuration());
		Utils.updateControl(pqueInformationConvertedDurationField, selPLInfo.getConversionDuration());
		Utils.updateControl(pqueInformationFinalizedDurationField, selPLInfo.getFinalizationDuration());
		Utils.updateControl(pqueInformationDiscardedCheckBox, selPLInfo.isDiscarded());
		Utils.updateControl(pqueInformationRejectedCheckBox, selPLInfo.isRejected());
		Utils.updateControl(pqueInformationCompletedCheckBox, selPLInfo.isCompleted());
		Utils.updateControl(pqueInformationRecordsField, selPLInfo.getRecords());
		Utils.updateControl(pqueInformationUnitsField, selPLInfo.getUnits());
		Utils.updateControl(pqueInformationAccountField, selPLInfo.getAccountId());
		Utils.updateControl(pqueInformationEmployerField, selPLInfo.getEmployerId());
		
		//leave disabled for now
		//pqueInformationRejectButton.setDisable(false);
		//if (selPLInfo.getAccountId() > 0) pqueInformationAccountButton.setDisable(false);
		//if (selPLInfo.getEmployerId() > 0) pqueInformationEmployerButton.setDisable(false);
		//pqueInformationSpecificationButton.setDisable(false);
		
		Document doc = selPLInfo.getDocument();
		if (doc != null) {
		// DOCUMENT
			Utils.updateControl(pqueDocumentNameField, doc.getName());
			Utils.updateControl(pqueDocumentSizeField, doc.getSizeInBytes());
			Utils.updateControl(pqueDocumentNameFileNameField, doc.getFileName());
			if (doc.getDocumentType() != null)
				Utils.updateControl(pqueDocumentDocTypeField, doc.getDocumentType().getName());
			
			//core data
			Utils.updateControl(pqueDocumentCoreIdLabel, doc.getId());
			Utils.updateControl(pqueDocumentCoreActiveLabel, doc.isActive());
			Utils.updateControl(pqueDocumentCoreBODateLabel, doc.getBornOn());
			Utils.updateControl(pqueDocumentCoreLastUpdatedLabel, doc.getLastUpdated());
		}
		
		// CORE INSTANCE
		CoreSystemInstance instance = selPLRequest.getInstance();
		if (instance != null) {
			Utils.updateControl(pqueInstanceIdField, instance.getInstanceId());
			Utils.updateControl(pqueInstanceLastCheckInField, instance.getLastCheckin());
			Utils.updateControl(pqueInstanceCoreIdLabel, instance.getId());
			Utils.updateControl(pqueInstanceCoreActiveLabel, instance.isActive());
			Utils.updateControl(pqueInstanceCoreBODateLabel, instance.getBornOn());
			Utils.updateControl(pqueInstanceCoreLastUpdatedLabel, instance.getLastUpdated());
			// ENDPOINT
			Endpoint endpoint = instance.getEndpoint();
			if (endpoint != null) {
				Utils.updateControl(pqueEndpointIdField, endpoint.getEndpointId());
				Utils.updateControl(pqueEndpointKeyField, endpoint.getEndpointKey());
				Utils.updateControl(pqueEndpointHostField, endpoint.getHost());
				Utils.updateControl(pqueEndpointVMField, endpoint.getVm());
				Utils.updateControl(pqueEndpointOSField, endpoint.getOs());
			}
		}
	}
	
	private void setCoverageFileData() {
		// show this section
		pqueCoverageFileVBox.setVisible(true);
		pqueCoverageFileVBox.setMaxHeight(Control.USE_COMPUTED_SIZE);
		pqueCoverageFileVBox.setMinHeight(Control.USE_COMPUTED_SIZE);
		pqueCoverageFileVBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
		//pqueCoverageViewFieldsButton.setDisable(false);
		
		if (coverageFile != null) {
			Utils.updateControl(pqueCoverageEmployeesCreatedField,coverageFile.getEmployeesCreated());
			Utils.updateControl(pqueCoverageEmployeesUpdatedField,coverageFile.getEmployeesUpdated());
			Utils.updateControl(pqueCoverageSecondariesCreatedField,coverageFile.getDependentsCreated());
			Utils.updateControl(pqueCoverageSecondariesUpdatedField,coverageFile.getDependentsUpdated());
//			Utils.updateControl(pqueCoverageEmpCovPeriodsCreatedField,coverageFile.getEmployeeCoveragePeriodsCreated());
//			Utils.updateControl(pqueCoverageEmpCovPeriodsUpdatedField,coverageFile.getEmployeeCoveragePeriodsUpdated());
//			Utils.updateControl(pqueCoverageSecCovPeriodsCreatedField,coverageFile.getSecondaryCoveragePeriodsCreated());
//			Utils.updateControl(pqueCoverageSecCovPeriodsUpdatedField,coverageFile.getSecondariesUpdated());
			Utils.updateControl(pqueCoverageNoChangeCountField,coverageFile.getNoChangeCount());
			
			//core data
			Utils.updateControl(pqueCoverageCoreIdLabel, coverageFile.getId());
			Utils.updateControl(pqueCoverageCoreActiveLabel, coverageFile.isActive());
			Utils.updateControl(pqueCoverageCoreBODateLabel, coverageFile.getBornOn());
			Utils.updateControl(pqueCoverageCoreLastUpdatedLabel, coverageFile.getLastUpdated());
			
			//load the Pipeline Notices
			if (coverageFile.getNotices() == null) return;
			if (coverageFile.getNotices().size() > 0)
			{
			    List<HBoxCell> noticeList = new ArrayList<>();
			    noticeList.add(new HBoxCell("Date", "Message",true));

				for (PipelineFileNotice notice : coverageFile.getNotices()) {
					if (notice == null) continue;
					noticeList.add(new HBoxCell(String.valueOf(notice.getLastUpdated()), notice.getMessage(), false));
				};	
				
				ObservableList<HBoxCell> myObservableList = FXCollections.observableList(noticeList);
				CoverageNoticesListView.setItems(myObservableList);		
				
				//update our list screen label
				CoverageNoticesListLabel.setText("Pipeline File Notices (total: " + String.valueOf(coverageFile.getNotices().size()) + ")" );
			} else {
				CoverageNoticesListLabel.setText("Pipeline File Notices (total: 0)");			
			}
		}
	}

	private void setEmployeeFileData() {
		// show this section
		pqueEmployeeFileVBox.setVisible(true);
		pqueEmployeeFileVBox.setMaxHeight(Control.USE_COMPUTED_SIZE);
		pqueEmployeeFileVBox.setMinHeight(Control.USE_COMPUTED_SIZE);
		pqueEmployeeFileVBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
		//pqueEmployeeViewFieldsButton.setDisable(false);
		
		if (employeeFile != null) {
			Utils.updateControl(pqueEmployeeCreatedField,employeeFile.getEmployeesCreated());
			Utils.updateControl(pqueEmployeeUpdatedField,employeeFile.getEmployeesUpdated());
			Utils.updateControl(pqueEmployeeNoChangeCountField,employeeFile.getNoChangeCount());
			
			//core data
			Utils.updateControl(pqueEmployeeCoreIdLabel, employeeFile.getId());
			Utils.updateControl(pqueEmployeeCoreActiveLabel, employeeFile.isActive());
			Utils.updateControl(pqueEmployeeCoreBODateLabel, employeeFile.getBornOn());
			Utils.updateControl(pqueEmployeeCoreLastUpdatedLabel, employeeFile.getLastUpdated());
			
			//load the Pipeline Notices
			if (employeeFile.getNotices() == null) return;
			if (employeeFile.getNotices().size() > 0)
			{
			    List<HBoxCell> noticeList = new ArrayList<>();
			    noticeList.add(new HBoxCell("Date", "Message",true));

				for (PipelineFileNotice notice : employeeFile.getNotices()) {
					if (notice == null) continue;
					noticeList.add(new HBoxCell(String.valueOf(notice.getLastUpdated()), notice.getMessage(), false));
				};	
				
				ObservableList<HBoxCell> myObservableList = FXCollections.observableList(noticeList);
				EmployeeNoticesListView.setItems(myObservableList);		
				
				//update our list screen label
				EmployeeNoticesListLabel.setText("Pipeline File Notices (total: " + String.valueOf(employeeFile.getNotices().size()) + ")" );
			} else {
				EmployeeNoticesListLabel.setText("Pipeline File Notices (total: 0)");	
			}
		}
	}
	
	private void setPayFileData() {
		// show this section
		pquePayFileVBox.setVisible(true);
		pquePayFileVBox.setMaxHeight(Control.USE_COMPUTED_SIZE);
		pquePayFileVBox.setMinHeight(Control.USE_COMPUTED_SIZE);
		pquePayFileVBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
		//pquePayViewFieldsButton.setDisable(false);
		
		if (payFile != null) {
			Utils.updateControl(pquePayEmployeesCreatedField,payFile.getEmployeesCreated());
			Utils.updateControl(pquePayEmployeesUpdatedField,payFile.getEmployeesUpdated());
			Utils.updateControl(pquePayCreatedField,payFile.getPaysCreated());
			Utils.updateControl(pquePayUpdatedField,payFile.getPaysUpdated());
			Utils.updateControl(pquePayNoChangeCountField,payFile.getNoChangeCount());
			
			//core data
			Utils.updateControl(pquePayCoreIdLabel, payFile.getId());
			Utils.updateControl(pquePayCoreActiveLabel, payFile.isActive());
			Utils.updateControl(pquePayCoreBODateLabel, payFile.getBornOn());
			Utils.updateControl(pquePayCoreLastUpdatedLabel, payFile.getLastUpdated());
			
			//load the Pipeline Notices
			if (payFile.getNotices() == null) return;
			if (payFile.getNotices().size() > 0)
			{
			    List<HBoxCell> noticeList = new ArrayList<>();
			    noticeList.add(new HBoxCell("Date", "Message",true));

				for (PipelineFileNotice notice : payFile.getNotices()) {
					if (notice == null) continue;
					noticeList.add(new HBoxCell(String.valueOf(notice.getLastUpdated()), notice.getMessage(), false));
				};	
				
				ObservableList<HBoxCell> myObservableList = FXCollections.observableList(noticeList);
				PayNoticesListView.setItems(myObservableList);		
				
				//update our list screen label
				PayNoticesListLabel.setText("Pipeline File Notices (total: " + String.valueOf(payFile.getNotices().size()) + ")" );
			} else {
				PayNoticesListLabel.setText("Pipeline File Notices (total: 0)");
			}
		}
	}

	private void setAirTranErrorFileData() {
		// show this section
		pqueAirTranFileVBox.setVisible(true);
		pqueAirTranFileVBox.setMaxHeight(Control.USE_COMPUTED_SIZE);
		pqueAirTranFileVBox.setMinHeight(Control.USE_COMPUTED_SIZE);
		pqueAirTranFileVBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
		//pqueAirTranViewFieldsButton.setDisable(false);
		
		if (airTranErrorFile != null) {
			Utils.updateControl(pqueAirTranNoChangeCountField,airTranErrorFile.getNoChangeCount());
			Utils.updateControl(pqueAirTranTransmissionsUpdatedField,airTranErrorFile.getTransmissionsUpdated());
			
			//core data
			Utils.updateControl(pqueAirTranCoreIdLabel, airTranErrorFile.getId());
			Utils.updateControl(pqueAirTranCoreActiveLabel, airTranErrorFile.isActive());
			Utils.updateControl(pqueAirTranCoreBODateLabel, airTranErrorFile.getBornOn());
			Utils.updateControl(pqueAirTranCoreLastUpdatedLabel, airTranErrorFile.getLastUpdated());
			
			//load the Pipeline Notices
			if (airTranErrorFile.getNotices() == null) return;
			if (airTranErrorFile.getNotices().size() > 0)
			{
			    List<HBoxCell> noticeList = new ArrayList<>();
			    noticeList.add(new HBoxCell("Date", "Message",true));

				for (PipelineFileNotice notice : airTranErrorFile.getNotices()) {
					if (notice == null) continue;
					noticeList.add(new HBoxCell(String.valueOf(notice.getLastUpdated()), notice.getMessage(), false));
				};	
				
				ObservableList<HBoxCell> myObservableList = FXCollections.observableList(noticeList);
				AirTranNoticesListView.setItems(myObservableList);		
				
				//update our list screen label
				AirTranNoticesListLabel.setText("Pipeline File Notices (total: " + String.valueOf(payFile.getNotices().size()) + ")" );
			} else {
				AirTranNoticesListLabel.setText("Pipeline File Notices (total: 0)");	
			}
		}
	}
	
	private void setAirTranReceiptFileData() {
		// show this section
		pqueAirTranFileVBox.setVisible(true);
		pqueAirTranFileVBox.setMaxHeight(Control.USE_COMPUTED_SIZE);
		pqueAirTranFileVBox.setMinHeight(Control.USE_COMPUTED_SIZE);
		pqueAirTranFileVBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
		//pqueAirTranViewFieldsButton.setDisable(false);
		
		if (airTranReceiptFile != null) {
			Utils.updateControl(pqueAirTranNoChangeCountField,airTranReceiptFile.getNoChangeCount());
			Utils.updateControl(pqueAirTranTransmissionsUpdatedField,airTranReceiptFile.getTransmissionsUpdated());
			
			//core data
			Utils.updateControl(pqueAirTranCoreIdLabel, airTranReceiptFile.getId());
			Utils.updateControl(pqueAirTranCoreActiveLabel, airTranReceiptFile.isActive());
			Utils.updateControl(pqueAirTranCoreBODateLabel, airTranReceiptFile.getBornOn());
			Utils.updateControl(pqueAirTranCoreLastUpdatedLabel, airTranReceiptFile.getLastUpdated());
			
			//load the Pipeline Notices
			if (airTranReceiptFile.getNotices() == null) return;
			if (airTranReceiptFile.getNotices().size() > 0)
			{
			    List<HBoxCell> noticeList = new ArrayList<>();
			    noticeList.add(new HBoxCell("Date", "Message",true));

				for (PipelineFileNotice notice : airTranReceiptFile.getNotices()) {
					if (notice == null) continue;
					noticeList.add(new HBoxCell(String.valueOf(notice.getLastUpdated()), notice.getMessage(), false));
				};	
				
				ObservableList<HBoxCell> myObservableList = FXCollections.observableList(noticeList);
				AirTranNoticesListView.setItems(myObservableList);		
				
				//update our list screen label
				AirTranNoticesListLabel.setText("Pipeline File Notices (total: " + String.valueOf(payFile.getNotices().size()) + ")" );
			} else {
				AirTranNoticesListLabel.setText("Pipeline File Notices (total: 0)");
			}
		}
	}
	
	private void setIrs1094cFileData() {
		// show this section
		pqueIrs1094cFileVBox.setVisible(true);
		pqueIrs1094cFileVBox.setMaxHeight(Control.USE_COMPUTED_SIZE);
		pqueIrs1094cFileVBox.setMinHeight(Control.USE_COMPUTED_SIZE);
		pqueIrs1094cFileVBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
		//pqueAirTranViewFieldsButton.setDisable(false);
		
		if (irs1094cFile != null) {
			Utils.updateControl(pqueIrs1094cNoChangeCountField,irs1094cFile.getNoChangeCount());
			Utils.updateControl(pqueIrs1094cCreatedField,irs1094cFile.getIrs1094csCreated());
			Utils.updateControl(pqueIrs1094cUpdatedField,irs1094cFile.getIrs1094csUpdated());
			
			//core data
			Utils.updateControl(pqueIrs1094cCoreIdLabel, irs1094cFile.getId());
			Utils.updateControl(pqueIrs1094cCoreActiveLabel, irs1094cFile.isActive());
			Utils.updateControl(pqueIrs1094cCoreBODateLabel, irs1094cFile.getBornOn());
			Utils.updateControl(pqueIrs1094cCoreLastUpdatedLabel, irs1094cFile.getLastUpdated());
			
			//load the Pipeline Notices
			if (irs1094cFile.getNotices() == null) return;
			if (irs1094cFile.getNotices().size() > 0)
			{
			    List<HBoxCell> noticeList = new ArrayList<>();
			    noticeList.add(new HBoxCell("Date", "Message",true));

				for (PipelineFileNotice notice : irs1094cFile.getNotices()) {
					if (notice == null) continue;
					noticeList.add(new HBoxCell(String.valueOf(notice.getLastUpdated()), notice.getMessage(), false));
				};	
				
				ObservableList<HBoxCell> myObservableList = FXCollections.observableList(noticeList);
				Irs1094cNoticesListView.setItems(myObservableList);		
				
				//update our list screen label
				Irs1094cNoticesListLabel.setText("Pipeline File Notices (total: " + String.valueOf(irs1094cFile.getNotices().size()) + ")" );
			} else {
				Irs1094cNoticesListLabel.setText("Pipeline File Notices (total: 0)");	
			}
		}
	}
	
	private void setIrs1095cFileData() {
		// show this section
		pqueIrs1095cFileVBox.setVisible(true);
		pqueIrs1095cFileVBox.setMaxHeight(Control.USE_COMPUTED_SIZE);
		pqueIrs1095cFileVBox.setMinHeight(Control.USE_COMPUTED_SIZE);
		pqueIrs1095cFileVBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
		//pqueAirTranViewFieldsButton.setDisable(false);
		
		if (irs1095cFile != null) {
			Utils.updateControl(pqueIrs1095cNoChangeCountField,irs1095cFile.getNoChangeCount());
			Utils.updateControl(pqueIrs1095cEmployeesCreatedField,irs1095cFile.getEmployeeIrs1095csCreated());
			Utils.updateControl(pqueIrs1095cEmployeesUpdatedField,irs1095cFile.getEmployeeIrs1095csUpdated());
			Utils.updateControl(pqueIrs1095cSecondariesCreatedField,irs1095cFile.getSecondaryIrs1095csCreated());
			Utils.updateControl(pqueIrs1095cSecondariesUpdatedField,irs1095cFile.getSecondaryIrs1095csUpdated());
			
			//core data
			Utils.updateControl(pqueIrs1095cCoreIdLabel, irs1095cFile.getId());
			Utils.updateControl(pqueIrs1095cCoreActiveLabel, irs1095cFile.isActive());
			Utils.updateControl(pqueIrs1095cCoreBODateLabel, irs1095cFile.getBornOn());
			Utils.updateControl(pqueIrs1095cCoreLastUpdatedLabel, irs1095cFile.getLastUpdated());
			
			//load the Pipeline Notices
			if (irs1095cFile.getNotices() == null) return;
			if (irs1095cFile.getNotices().size() > 0)
			{
			    List<HBoxCell> noticeList = new ArrayList<>();
			    noticeList.add(new HBoxCell("Date", "Message",true));

				for (PipelineFileNotice notice : irs1095cFile.getNotices()) {
					if (notice == null) continue;
					noticeList.add(new HBoxCell(String.valueOf(notice.getLastUpdated()), notice.getMessage(), false));
				};	
				
				ObservableList<HBoxCell> myObservableList = FXCollections.observableList(noticeList);
				Irs1095cNoticesListView.setItems(myObservableList);		
				
				//update our list screen label
				Irs1095cNoticesListLabel.setText("Pipeline File Notices (total: " + String.valueOf(irs1095cFile.getNotices().size()) + ")" );
			} else {
				Irs1095cNoticesListLabel.setText("Pipeline File Notices (total: 0)");	
			}
		}
	}
	
	private void setPayPeriodFileData() {
		// show this section
		pquePayPeriodFileVBox.setVisible(true);
		pquePayPeriodFileVBox.setMaxHeight(Control.USE_COMPUTED_SIZE);
		pquePayPeriodFileVBox.setMinHeight(Control.USE_COMPUTED_SIZE);
		pquePayPeriodFileVBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
		//pqueAirTranViewFieldsButton.setDisable(false);
		
		if (payPeriodFile != null) {
			Utils.updateControl(pquePayPeriodsCreatedField,payPeriodFile.getPayPeriodsCreated());
			Utils.updateControl(pquePayPeriodsUpdatedField,payPeriodFile.getPayPeriodsUpdated());
			Utils.updateControl(pquePayPeriodsNoChangeCountField,payPeriodFile.getNoChangeCount());
			
			//core data
			Utils.updateControl(pquePayPeriodCoreIdLabel, payPeriodFile.getId());
			Utils.updateControl(pquePayPeriodCoreActiveLabel, payPeriodFile.isActive());
			Utils.updateControl(pquePayPeriodCoreBODateLabel, payPeriodFile.getBornOn());
			Utils.updateControl(pquePayPeriodCoreLastUpdatedLabel, payPeriodFile.getLastUpdated());
			
			//load the Pipeline Notices
			if (payPeriodFile.getNotices() == null) return;
			if (payPeriodFile.getNotices().size() > 0)
			{
			    List<HBoxCell> noticeList = new ArrayList<>();
			    noticeList.add(new HBoxCell("Date", "Message",true));

				for (PipelineFileNotice notice : payPeriodFile.getNotices()) {
					if (notice == null) continue;
					noticeList.add(new HBoxCell(String.valueOf(notice.getLastUpdated()), notice.getMessage(), false));
				};	
				
				ObservableList<HBoxCell> myObservableList = FXCollections.observableList(noticeList);
				PayPeriodNoticesListView.setItems(myObservableList);		
				
				//update our list screen label
				PayPeriodNoticesListLabel.setText("Pipeline File Notices (total: " + String.valueOf(payPeriodFile.getNotices().size()) + ")" );
			} else {
				PayPeriodNoticesListLabel.setText("Pipeline File Notices (total: 0)");	
			}
		}
	}
	
	//extending the listview for our additional controls
	public class HBoxCell extends HBox {
         Label lblCol1 = new Label();
         Label lblCol2 = new Label();

         HBoxCell(String Col1, String Col2, boolean isHeader) {
              super();

              if (Col1 == null ) Col1 = "";
              if (Col2 == null ) Col2 = "";
              
              lblCol1.setText(Col1);
              lblCol1.setMinWidth(100);
              HBox.setHgrow(lblCol1, Priority.ALWAYS);

              lblCol2.setText(Col2);
              lblCol2.setMinWidth(840);
              lblCol2.setMaxWidth(840);
              lblCol2.setPrefWidth(840);
              HBox.setHgrow(lblCol2, Priority.ALWAYS);

              if (isHeader == true) {
            	  lblCol1.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblCol2.setFont(Font.font(null, FontWeight.BOLD, 13));
              }
              
              this.getChildren().addAll(lblCol1, lblCol2);
        }
    }	
	
	@FXML
	private void onViewAccount(ActionEvent event) {
    	try { DataManager.i().mAccount = AdminPersistenceManager.getInstance().get(Account.class, selPLInfo.getAccountId()); } 	
    	catch(CoreException e) { DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		EtcAdmin.i().setScreen(ScreenType.ACCOUNT, true);
	}	

	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINEQUEUEDETAILEDIT, true);
	}	
	
	@FXML
	private void onBack(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINEQUEUE);
	}	
	
}
