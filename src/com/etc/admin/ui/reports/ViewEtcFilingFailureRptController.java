package com.etc.admin.ui.reports;


import java.util.ArrayList;
import java.util.List;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class ViewEtcFilingFailureRptController {
	@FXML
	private ListView<ReportCell> hractReportList;
	@FXML
	private TextField hractFilterField;
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		loadReportData();
	}

	private void initControls() {
		// tbd
	}	
	
	private void loadReportData() {
		// new thread
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            	//get the report from the server
            	//FIXME: DataManager.i().getEtcFilingErrorReport();
                return null;
            }
        };
        
      	task.setOnScheduled(e ->  {
      		EtcAdmin.i().setStatusMessage("Loading Report Data...");
      		EtcAdmin.i().setProgress(0.25);});
      			
    	task.setOnSucceeded(e ->  loadReport());
    	task.setOnFailed(e ->  loadReport());
        new Thread(task).start();
	
	}
	
	private void loadReport() {
/*  		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0);
  		hractReportList.getItems().clear();

		if (DataManager.i().mAirFilingEventReports != null) {
		    List<ReportCell> reportList = new ArrayList<>();
		    String searchData = "";
		    
		    reportList.add(new ReportCell());
			for (AirFilingEventReport report : DataManager.i().mAirFilingEventReports ) {
				if (report == null) continue;
				if (hractFilterField.getText().isEmpty() == false) {
					searchData = report.getErrorName() + " " + 
								 report.getError() + " " + 
								 Utils.getDateString(report.getErrorDate()) + 
								 String.valueOf(report.getErrorId());
					if (searchData.toUpperCase().contains(hractFilterField.getText().toUpperCase()))
						reportList.add(new ReportCell(report));
				} else
					reportList.add(new ReportCell(report));
			}
			ObservableList<ReportCell> myObservableList = FXCollections.observableList(reportList);
			hractReportList.setItems(myObservableList);			    
		}
*/	}
	
	@FXML
	private void onApplyFilter(ActionEvent event) {
		loadReport();
	}	

	@FXML
	private void onClearFilter(ActionEvent event) {
		hractFilterField.setText("");
		loadReport();
	}	
	

	//extending the listview for our additional controls
	public class ReportCell extends HBox {
         Label lblId = new Label();
         Label lblErrorDate = new Label();
         Label lblErId = new Label();
         Label lblErNm = new Label();
         Label lblError = new Label();
        // AirFilingEventReport report;

         ReportCell() {
             super();
 
          	  lblId.setFont(Font.font(null, FontWeight.BOLD, 13));
              lblId.setText("Id");
              lblId.setMinWidth(50);
              HBox.setHgrow(lblId, Priority.ALWAYS);

              lblErrorDate.setFont(Font.font(null, FontWeight.BOLD, 13));
              lblErrorDate.setText("Date");
              lblErrorDate.setMinWidth(75);
              HBox.setHgrow(lblErrorDate, Priority.ALWAYS);

              lblErId.setFont(Font.font(null, FontWeight.BOLD, 13));
              lblErId.setText("Employer Id");
              lblErId.setMinWidth(100);
              HBox.setHgrow(lblErId, Priority.ALWAYS);

              lblErNm.setFont(Font.font(null, FontWeight.BOLD, 13));
              lblErNm.setText("Employer");
              lblErNm.setMinWidth(150);
              HBox.setHgrow(lblErNm, Priority.ALWAYS);

              lblError.setFont(Font.font(null, FontWeight.BOLD, 13));
              lblError.setText("Error");
              lblError.setMinWidth(400);
              HBox.setHgrow(lblError, Priority.ALWAYS);

              this.getChildren().addAll(lblId, lblErrorDate, lblErId, lblErNm, lblError);  
         }
         
 /*        ReportCell(AirFilingEventReport report) {
              super();

              if (report != null) {
	              this.report = report;
	              
	              lblId.setText(String.valueOf(report.getId()));
	              lblId.setMinWidth(50);
	              HBox.setHgrow(lblId, Priority.ALWAYS);
	
	              lblErrorDate.setText(String.valueOf(Utils.getDateString(report.getErrorDate())));
	              lblErrorDate.setMinWidth(75);
	              HBox.setHgrow(lblErrorDate, Priority.ALWAYS);
	
	              lblErId.setText(String.valueOf(report.getErrorId()));
	              lblErId.setMinWidth(100);
	              HBox.setHgrow(lblErId, Priority.ALWAYS);
	
	              lblErNm.setText(report.getErrorName());
	              lblErNm.setMinWidth(150);
	              HBox.setHgrow(lblErNm, Priority.ALWAYS);
	
	              lblError.setText(report.getError());
	              lblError.setMinWidth(400);
	              HBox.setHgrow(lblError, Priority.ALWAYS);
              }
              this.getChildren().addAll(lblId, lblErrorDate, lblErId, lblErNm, lblError);  
        }
 */   }	
		
}


