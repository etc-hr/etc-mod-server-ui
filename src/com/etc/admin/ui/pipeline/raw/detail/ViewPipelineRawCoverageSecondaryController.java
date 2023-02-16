package com.etc.admin.ui.pipeline.raw.detail;

import java.util.ArrayList;
import java.util.List;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ViewPipelineRawCoverageSecondaryController {
	// RECORD INFORMATION
	@FXML
	private TextField rwcvsecRecordInfoRecordIndexField;
	@FXML
	private TextField rwcvsecRecordInfoValidatedOnField;
	@FXML
	private TextField rwcvsecRecordInfoConvertedOnField;
	@FXML
	private TextField rwcvsecRecordInfoIgnoredOnField;
	@FXML
	private CheckBox rwcvsecRecordInfoValidatedCheckBox;
	@FXML
	private CheckBox rwcvsecRecordInfoConvertedCheckBox;
	@FXML
	private CheckBox rwcvsecRecordInfoIgnoredCheckBox;
	// RAW COVERAGE SECONDARY
	@FXML
	private TextField rwcvsecSSNField;
	@FXML
	private TextField rwcvsecFirstNameField;
	@FXML
	private TextField rwcvsecMiddleNameField;
	@FXML
	private TextField rwcvsecLastNameField;
	@FXML
	private TextField rwcvsecStreetNumField;
	@FXML
	private TextField rwcvsecStreetField;
	@FXML
	private TextField rwcvsecStreetExtField;
	@FXML
	private TextField rwcvsecCityField;
	@FXML
	private TextField rwcvsecStateField;
	@FXML
	private TextField rwcvsecZipField;
	@FXML
	private DatePicker rwcvsecDOBPicker;
	@FXML
	private TextField rwcvsecCustField1Field;
	@FXML
	private TextField rwcvsecCustField2Field;
	@FXML
	private TextField rwcvsecCustField3Field;
	@FXML
	private TextField rwcvsecCustField4Field;
	@FXML
	private TextField rwcvsecCustField5Field;
	@FXML
	private Label rwcvsecCoreIdLabel;
	@FXML
	private Label rwcvsecCoreActiveLabel;
	@FXML
	private Label rwcvsecCoreBODateLabel;
	@FXML
	private Label rwcvsecCoreLastUpdatedLabel;
	// RAW NOTICES
	@FXML
	private Label rwcvsecNoticesListLabel;
	@FXML
	private ListView<HBoxCell> rwcvsecNoticesListView;
	// RAW INVALIDATIONS
	@FXML
	private Label rwcvsecInvalidationsListLabel;
	@FXML
	private ListView<HBoxCell> rwcvsecInvalidationsListView;
	// RAW CONVERSION FAILURES
	@FXML
	private Label rwcvsecFailuresListLabel;
	@FXML
	private ListView<HBoxCell> rwcvsecFailuresListView;
	
	//raw employee
	//private RawCoverageSecondary emp;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		updateCoverageSecondaryData();
	}
	
	private void updateCoverageSecondaryData(){		
/*		emp = DataManager.i().mRawCoverageSecondary;
		if (emp == null) return;
		
		if (emp.getSsn() != null)
			Utils.updateControl(rwcvsecSSNField,emp.getSsn().getUsrln());
		Utils.updateControl(rwcvsecFirstNameField,emp.getFirstName());
		Utils.updateControl(rwcvsecMiddleNameField,emp.getMiddleName());
		Utils.updateControl(rwcvsecLastNameField,emp.getLastName());
		Utils.updateControl(rwcvsecStreetNumField,emp.getStreetNumber());
		Utils.updateControl(rwcvsecStreetField,emp.getStreet());
		Utils.updateControl(rwcvsecStreetExtField,emp.getStreetExtension());
		Utils.updateControl(rwcvsecCityField,emp.getCity());
		Utils.updateControl(rwcvsecStateField,emp.getState());
		Utils.updateControl(rwcvsecZipField,emp.getZip());
		Utils.updateControl(rwcvsecDOBPicker,emp.getDateOfBirth());
		Utils.updateControl(rwcvsecCustField1Field,emp.getCustomField1());
		Utils.updateControl(rwcvsecCustField2Field,emp.getCustomField2());
		Utils.updateControl(rwcvsecCustField3Field,emp.getCustomField3());
		Utils.updateControl(rwcvsecCustField4Field,emp.getCustomField4());
		Utils.updateControl(rwcvsecCustField5Field,emp.getCustomField5());
		Utils.updateControl(rwcvsecCoreIdLabel,emp.getId());
		Utils.updateControl(rwcvsecCoreActiveLabel,emp.isActive());
		Utils.updateControl(rwcvsecCoreBODateLabel,emp.getBornOn());
		Utils.updateControl(rwcvsecCoreLastUpdatedLabel,emp.getLastUpdated());
		
		// RECORD INFORMATION
		RecordInformation info = emp.getRecordInformation();
		if (info != null) {
			Utils.updateControl(rwcvsecRecordInfoRecordIndexField,info.getRecordIndex());
			Utils.updateControl(rwcvsecRecordInfoValidatedOnField,info.getValidatedOn());
			Utils.updateControl(rwcvsecRecordInfoConvertedOnField,info.getConvertedOn());
			Utils.updateControl(rwcvsecRecordInfoIgnoredOnField,info.getIgnoredOn());
			Utils.updateControl(rwcvsecRecordInfoValidatedCheckBox,info.isValidated());
			Utils.updateControl(rwcvsecRecordInfoConvertedCheckBox,info.isConverted());
			Utils.updateControl(rwcvsecRecordInfoIgnoredCheckBox,info.isIgnored());
		}
		
		//load the collections for this raw employee
		loadRawNotices();
		loadRawInvalidations();
		loadRawConversionFailures();
*/	}
	
	private void updateRawEmployee(){		
/*		if (emp == null) return;
		
		//create a new SSN in case one doesn't exist
		SSN ssn = new SSN();
		ssn.setUsrln(rwcvsecSSNField.getText());
		emp.setSsn(ssn);
		emp.setFirstName(rwcvsecFirstNameField.getText());
		emp.setMiddleName(rwcvsecMiddleNameField.getText());
		emp.setLastName(rwcvsecLastNameField.getText());
		emp.setStreetNumber(rwcvsecStreetNumField.getText());
		emp.setStreet(rwcvsecStreetField.getText());
		emp.setStreetExtension(rwcvsecStreetExtField.getText());
		emp.setCity(rwcvsecCityField.getText());
		emp.setState(rwcvsecStateField.getText());
		emp.setZip(rwcvsecZipField.getText());
		emp.setDateOfBirth(Utils.getCalDate(rwcvsecDOBPicker));
		emp.setCustomField1(rwcvsecCustField1Field.getText());
		emp.setCustomField2(rwcvsecCustField2Field.getText());
		emp.setCustomField3(rwcvsecCustField3Field.getText());
		emp.setCustomField4(rwcvsecCustField4Field.getText());
		emp.setCustomField5(rwcvsecCustField5Field.getText());
*/	}
	
	//Load the raw notices for this raw employee
	private void loadRawNotices() {
/*		if (emp == null) return;
		
		if (emp.getNotices() != null)
		{
		    List<HBoxCell> noticeList = new ArrayList<>();
		    noticeList.add(new HBoxCell("Date", "Message",true));

			for (RawNotice notice : emp.getNotices()) {
				if (notice == null) continue;
				noticeList.add(new HBoxCell(String.valueOf(notice.getLastUpdated()), notice.getMessage(), false));
			};	
			
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(noticeList);
			rwcvsecNoticesListView.setItems(myObservableList);		
			
			//update our list screen label
			rwcvsecNoticesListLabel.setText("Raw Notices (total: " + String.valueOf(emp.getNotices().size()) + ")" );
		} else {
			rwcvsecNoticesListLabel.setText("Raw Notices (total: 0)");			
		}
*/	}	
	
	//Load the raw invalidations for this raw employee
	private void loadRawInvalidations() {
/*		if (emp == null) return;
		
		if (emp.getInvalidations() != null)
		{
		    List<HBoxCell> invalidationList = new ArrayList<>();
		    invalidationList.add(new HBoxCell("Date", "Message",true));

			for (RawInvalidation invalidation : emp.getInvalidations()) {
				if (invalidation == null) continue;
				invalidationList.add(new HBoxCell(String.valueOf(invalidation.getLastUpdated()), invalidation.getMessage(), false));
			};	
			
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(invalidationList);
			rwcvsecInvalidationsListView.setItems(myObservableList);		
			
			//update our list screen label
			rwcvsecInvalidationsListLabel.setText("Raw Invalidations (total: " + String.valueOf(emp.getInvalidations().size()) + ")" );
		} else {
			rwcvsecInvalidationsListLabel.setText("Raw Invalidations (total: 0)");			
		}
*/	}	
	
	//Load the raw conversion failures for this raw employee
	private void loadRawConversionFailures() {
/*		if (emp == null) return;
		
		if (emp.getConversionFailures() != null)
		{
		    List<HBoxCell> failuresList = new ArrayList<>();
		    failuresList.add(new HBoxCell("Date", "Message",true));

			for (RawConversionFailure failure : emp.getConversionFailures()) {
				if (failure == null) continue;
				failuresList.add(new HBoxCell(String.valueOf(failure.getLastUpdated()), failure.getMessage(), false));
			};	
			
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(failuresList);
			rwcvsecFailuresListView.setItems(myObservableList);		
			
			//update our list screen label
			rwcvsecFailuresListLabel.setText("Raw Conversion Failures (total: " + String.valueOf(emp.getConversionFailures().size()) + ")" );
		} else {
			rwcvsecFailuresListLabel.setText("Raw Conversion Failures (total: 0)");			
		}
*/	}	
	
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
              lblCol2.setMinWidth(140);
              lblCol2.setMaxWidth(140);
              lblCol2.setPrefWidth(140);
              HBox.setHgrow(lblCol2, Priority.ALWAYS);

              if (isHeader == true) {
            	  lblCol1.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblCol2.setFont(Font.font(null, FontWeight.BOLD, 13));
              }
              
              this.getChildren().addAll(lblCol1, lblCol2);
        }
    }	
	
	private boolean validateData() {
		boolean bReturn = true;
		// insert validation here		
		return bReturn;
	}
	
	@FXML
	private void onSave(ActionEvent event) {
		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updateRawEmployee();
		
		//save it to the repository and the server
		//FIXME: DataManager.i().saveCurrentRawCoverageSecondary();
		
		//and return
		EtcAdmin.i().setScreen(ScreenType.PIPELINECOVERAGEFILE, true);
	}	
}
