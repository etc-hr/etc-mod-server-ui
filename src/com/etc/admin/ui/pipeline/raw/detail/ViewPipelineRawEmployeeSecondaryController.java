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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ViewPipelineRawEmployeeSecondaryController {
	// RECORD INFORMATION
	@FXML
	private TextField rwemsecRecordInfoRecordIndexField;
	@FXML
	private TextField rwemsecRecordInfoValidatedOnField;
	@FXML
	private TextField rwemsecRecordInfoConvertedOnField;
	@FXML
	private TextField rwemsecRecordInfoIgnoredOnField;
	@FXML
	private CheckBox rwemsecRecordInfoValidatedCheckBox;
	@FXML
	private CheckBox rwemsecRecordInfoConvertedCheckBox;
	@FXML
	private CheckBox rwemsecRecordInfoIgnoredCheckBox;
	// RAW EMPLOYEE SECONDARY
	@FXML
	private TextField rwemsecSSNField;
	@FXML
	private TextField rwemsecFullNameField;
	@FXML
	private TextField rwemsecFirstNameField;
	@FXML
	private TextField rwemsecMiddleNameField;
	@FXML
	private TextField rwemsecLastNameField;
	@FXML
	private TextField rwemsecAddressField;
	@FXML
	private TextField rwemsecStreetNumField;
	@FXML
	private TextField rwemsecStreetField;
	@FXML
	private TextField rwemsecStreetExtField;
	@FXML
	private TextField rwemsecCityField;
	@FXML
	private TextField rwemsecStateField;
	@FXML
	private TextField rwemsecZipField;
	@FXML
	private DatePicker rwemsecDOBPicker;
	@FXML
	private TextField rwemsecCustField1Field;
	@FXML
	private TextField rwemsecCustField2Field;
	@FXML
	private TextField rwemsecCustField3Field;
	@FXML
	private TextField rwemsecCustField4Field;
	@FXML
	private TextField rwemsecCustField5Field;
	@FXML
	private Label rwemsecCoreIdLabel;
	@FXML
	private Label rwemsecCoreActiveLabel;
	@FXML
	private Label rwemsecCoreBODateLabel;
	@FXML
	private Label rwemsecCoreLastUpdatedLabel;
	@FXML
	private Button rwemsecSaveButton;
	@FXML
	private ListView<HBoxCell> rwemsecSecondariesListView;
	// RAW NOTICES
	@FXML
	private Label rwemsecNoticesListLabel;
	@FXML
	private ListView<HBoxCell> rwemsecNoticesListView;
	// RAW INVALIDATIONS
	@FXML
	private Label rwemsecInvalidationsListLabel;
	@FXML
	private ListView<HBoxCell> rwemsecInvalidationsListView;
	// RAW CONVERSION FAILURES
	@FXML
	private Label rwemsecFailuresListLabel;
	@FXML
	private ListView<HBoxCell> rwemsecFailuresListView;
	
	//raw employee
	//private RawEmployeeSecondary emp;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		updateRawEmployeeSecondaryData();
	}
	
	private void updateRawEmployeeSecondaryData(){		
/*		emp = DataManager.i().mRawEmployeeSecondary;
		if (emp == null) return;
		
		if (emp.getSsn() != null)
			Utils.updateControl(rwemsecSSNField,emp.getSsn().getUsrln());
		Utils.updateControl(rwemsecFullNameField,emp.getFullName());
		Utils.updateControl(rwemsecFirstNameField,emp.getFirstName());
		Utils.updateControl(rwemsecMiddleNameField,emp.getMiddleName());
		Utils.updateControl(rwemsecLastNameField,emp.getLastName());
		Utils.updateControl(rwemsecAddressField,emp.getFullAddress());
		Utils.updateControl(rwemsecStreetNumField,emp.getStreetNumber());
		Utils.updateControl(rwemsecStreetField,emp.getStreet());
		Utils.updateControl(rwemsecStreetExtField,emp.getStreetExtension());
		Utils.updateControl(rwemsecCityField,emp.getCity());
		Utils.updateControl(rwemsecStateField,emp.getState());
		Utils.updateControl(rwemsecZipField,emp.getZip());
		Utils.updateControl(rwemsecDOBPicker,emp.getDateOfBirth());
		Utils.updateControl(rwemsecCustField1Field,emp.getCustomField1());
		Utils.updateControl(rwemsecCustField2Field,emp.getCustomField2());
		Utils.updateControl(rwemsecCustField3Field,emp.getCustomField3());
		Utils.updateControl(rwemsecCustField4Field,emp.getCustomField4());
		Utils.updateControl(rwemsecCustField5Field,emp.getCustomField5());
		Utils.updateControl(rwemsecCoreIdLabel,emp.getId());
		Utils.updateControl(rwemsecCoreActiveLabel,emp.isActive());
		Utils.updateControl(rwemsecCoreBODateLabel,emp.getBornOn());
		Utils.updateControl(rwemsecCoreLastUpdatedLabel,emp.getLastUpdated());
		
		// RECORD INFORMATION
		RecordInformation info = emp.getRecordInformation();
		if (info != null) {
			Utils.updateControl(rwemsecRecordInfoRecordIndexField,info.getRecordIndex());
			Utils.updateControl(rwemsecRecordInfoValidatedOnField,info.getValidatedOn());
			Utils.updateControl(rwemsecRecordInfoConvertedOnField,info.getConvertedOn());
			Utils.updateControl(rwemsecRecordInfoIgnoredOnField,info.getIgnoredOn());
			Utils.updateControl(rwemsecRecordInfoValidatedCheckBox,info.isValidated());
			Utils.updateControl(rwemsecRecordInfoConvertedCheckBox,info.isConverted());
			Utils.updateControl(rwemsecRecordInfoIgnoredCheckBox,info.isIgnored());
		}
		
		//load the collections
		loadRawNotices();
		loadRawInvalidations();
		loadRawConversionFailures();
*/	}
	
	private void updateRawEmployeeSecondary(){		
/*		if (emp == null) return;
		
		//create a new SSN in case one doesn't exist
		SSN ssn = new SSN();
		ssn.setUsrln(rwemsecSSNField.getText());
		emp.setSsn(ssn);
		emp.setFullName(rwemsecFullNameField.getText());
		emp.setFirstName(rwemsecFirstNameField.getText());
		emp.setMiddleName(rwemsecMiddleNameField.getText());
		emp.setLastName(rwemsecLastNameField.getText());
		emp.setFullAddress(rwemsecAddressField.getText());
		emp.setStreetNumber(rwemsecStreetNumField.getText());
		emp.setStreet(rwemsecStreetField.getText());
		emp.setStreetExtension(rwemsecStreetExtField.getText());
		emp.setCity(rwemsecCityField.getText());
		emp.setState(rwemsecStateField.getText());
		emp.setZip(rwemsecZipField.getText());
		emp.setDateOfBirth(Utils.getCalDate(rwemsecDOBPicker));
		emp.setCustomField1(rwemsecCustField1Field.getText());
		emp.setCustomField2(rwemsecCustField2Field.getText());
		emp.setCustomField3(rwemsecCustField3Field.getText());
		emp.setCustomField4(rwemsecCustField4Field.getText());
		emp.setCustomField5(rwemsecCustField5Field.getText());
*/	}
	
	//Load the raw notices
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
			rwemsecNoticesListView.setItems(myObservableList);		
			
			//update our list screen label
			rwemsecNoticesListLabel.setText("Raw Notices (total: " + String.valueOf(emp.getNotices().size()) + ")" );
		} else {
			rwemsecNoticesListLabel.setText("Raw Notices (total: 0)");			
		}
*/	}	
	
	//Load the raw invalidations
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
			rwemsecInvalidationsListView.setItems(myObservableList);		
			
			//update our list screen label
			rwemsecInvalidationsListLabel.setText("Raw Invalidations (total: " + String.valueOf(emp.getInvalidations().size()) + ")" );
		} else {
			rwemsecInvalidationsListLabel.setText("Raw Invalidations (total: 0)");			
		}
*/	}	
	
	//Load the raw conversion failures
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
			rwemsecFailuresListView.setItems(myObservableList);		
			
			//update our list screen label
			rwemsecFailuresListLabel.setText("Raw Conversion Failures (total: " + String.valueOf(emp.getConversionFailures().size()) + ")" );
		} else {
			rwemsecFailuresListLabel.setText("Raw Conversion Failures (total: 0)");			
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
		updateRawEmployeeSecondary();
		
		//save it to the repository and the server
		//FIXME: DataManager.i().saveCurrentRawEmployeeSecondary();
		
		//and return
		EtcAdmin.i().setScreen(ScreenType.PIPELINEEMPLOYEEFILE, true);
	}	
}
