package com.etc.admin.ui.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.AirTransmission;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.TaxPeriod;
import com.etc.corvetto.rqs.AirTransmissionRequest;
import com.etc.corvetto.rqs.TaxPeriodRequest;
import com.etc.corvetto.utils.types.FilingType;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewAccountFilingController 
{

	@FXML
	private Label topLabel;
	@FXML
	private CheckBox InactiveCheck;
	@FXML
	private TableView<HBoxTransData> tableView;
	@FXML
	private TextField filterField;
	@FXML
	private ComboBox<String> taxYearCombo;
	
	//table sort 
	TableColumn<HBoxTransData, String> sortColumn = null;

	// collection to feed the table
	List<TransmissionData> transData = new ArrayList<TransmissionData>();
	String taxYearString = "";

	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() 
	{
		try {
			initControls();
    	}catch (Exception e) {
    		DataManager.i().log(Level.SEVERE,e);
    	}
	}
	
	private void initControls() 
	{
		setTableColumns();
		fillTaxYearCombo();
		
		/*dataList.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				 viewSelectedHistory();
            }
        }); */
	}
	
	private void setTableColumns() 
	{
		//clear the default values
		tableView.getColumns().clear();

	    TableColumn<HBoxTransData, String> x1 = new TableColumn<HBoxTransData, String>("Employer");
	    x1.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("employer"));
	    x1.setMinWidth(200);
		tableView.getColumns().add(x1);

		TableColumn<HBoxTransData, String> x2 = new TableColumn<HBoxTransData, String>("Year");
	    x2.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("year"));
	    x2.setMinWidth(75);
	    x2.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		//initial sort is by start date
		sortColumn = x2;
		sortColumn.setSortType(SortType.DESCENDING);
		tableView.getColumns().add(x2);
		
	    TableColumn<HBoxTransData, String> x3 = new TableColumn<HBoxTransData, String>("Generated on");
		x3.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("generatedon"));
		x3.setMinWidth(100);
		tableView.getColumns().add(x3);
		
	    TableColumn<HBoxTransData, String> x4 = new TableColumn<HBoxTransData, String>("ID");
	    x4.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("transid"));
	    x4.setMinWidth(75);
		tableView.getColumns().add(x4);

	    TableColumn<HBoxTransData, String> x5 = new TableColumn<HBoxTransData, String>("Active");
	    x5.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("active"));
	    x5.setMinWidth(100);
		tableView.getColumns().add(x5);

		TableColumn<HBoxTransData, String> x6 = new TableColumn<HBoxTransData, String>("File Type");
		x6.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("filetypr"));
		x6.setMinWidth(200);
		tableView.getColumns().add(x6);
		
	    TableColumn<HBoxTransData, String> x7 = new TableColumn<HBoxTransData, String>("Receipt Timestamp");
		x7.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("reciepttimestamp"));
		x7.setMinWidth(125);
		tableView.getColumns().add(x7);
		
	    TableColumn<HBoxTransData, String> x8 = new TableColumn<HBoxTransData, String>("1094/1095");
		x8.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("type10945"));
		x8.setMinWidth(100);
		tableView.getColumns().add(x8);
		
	    TableColumn<HBoxTransData, String> x9 = new TableColumn<HBoxTransData, String>("1094 count");
		x9.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("count1094"));
		x9.setMinWidth(100);
		tableView.getColumns().add(x9);
		
	    TableColumn<HBoxTransData, String> x10 = new TableColumn<HBoxTransData, String>("1095 Count");
		x10.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("count1095"));
		x10.setMinWidth(75);
		tableView.getColumns().add(x10);
		
		TableColumn<HBoxTransData, String> x11 = new TableColumn<HBoxTransData, String>("Filing Type");
		x11.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("filingtype"));
		x11.setMinWidth(100);
		tableView.getColumns().add(x11);
		
	    TableColumn<HBoxTransData, String> x12 = new TableColumn<HBoxTransData, String>("Filing State");
		x12.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("filingstate"));
		x12.setMinWidth(100);
		tableView.getColumns().add(x12);

		TableColumn<HBoxTransData, String> x13 = new TableColumn<HBoxTransData, String>("Receipt ID");
		x13.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("recieptid"));
		x13.setMinWidth(100);
		tableView.getColumns().add(x13);
		 
		TableColumn<HBoxTransData, String> x14 = new TableColumn<HBoxTransData, String>("UTID");
		x14.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("utid"));
		x14.setMinWidth(100);
		tableView.getColumns().add(x14);
		 
		TableColumn<HBoxTransData, String> x15 = new TableColumn<HBoxTransData, String>("Filing Method");
		x15.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("filingmethod"));
		x15.setMinWidth(100);
		tableView.getColumns().add(x15);
		 
		TableColumn<HBoxTransData, String> x16 = new TableColumn<HBoxTransData, String>("Filing Status");
		x16.setCellValueFactory(new PropertyValueFactory<HBoxTransData,String>("filingstatus"));
		x16.setMinWidth(100);
		tableView.getColumns().add(x16);
		 
	}
	
	private void setCellFactory(TableColumn<HBoxTransData, String>  col) 
	{
		col.setCellFactory(column -> {
		    return new TableCell<HBoxTransData, String>() 
		    {
		        @Override
		        protected void updateItem(String item, boolean empty) 
		        {
		            super.updateItem(item, empty);
		            if(item == null || empty) 
		            {
		                setText(null);
		                setStyle("");
		            } else {
		                setText(item);
		                HBoxTransData cell = getTableView().getItems().get(getIndex());
		                //if(cell.getPayPeriod().isActive() == false)
		                //	setTextFill(Color.BLUE);
		                //else
		                //	setTextFill(Color.BLACK);
		            }
		        }
		    };
		});
	}
	
	private void fillTaxYearCombo()
	{
		try {
			TaxPeriodRequest request = new TaxPeriodRequest();
			request.setOrganizationId(DataManager.i().mLocalUser.getOrganizationId());
			List<TaxPeriod> taxPeriods = AdminPersistenceManager.getInstance().getAll(request);
			Collections.sort(taxPeriods, (o1, o2) -> (Long.valueOf(o2.getYear()).compareTo(Long.valueOf(o1.getYear())))); 
			for (TaxPeriod tp : taxPeriods) {
				if (tp.getFilingType().equals(FilingType.FEDERAL) == false) continue;
				taxYearCombo.getItems().add(String.valueOf(tp.getYear()));
			}	
		} catch (Exception e) {
			DataManager.i().log(Level.SEVERE,e);
		}
	}

	
	private void updateFilingData() {
		// create a thread to handle the update, letting the screen respond normally
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            	try {
            		// cycle throuhg the account employers
	            	for (Employer employer : DataManager.i().mAccount.getEmployers()) {
	            		AirTransmissionRequest req = new AirTransmissionRequest();
	            		// req TBD
	            		//req.setEmployerId(employer.getId());
	            		//req.setTaxYearId(taxYearId);
	            		List<AirTransmission> transmissions = AdminPersistenceManager.getInstance().getAll(req);	            		
	            		//add the transmissions to our object collection
	            		for (AirTransmission transmission : transmissions) {
	            			transData.add(new TransmissionData(transmission, employer));
	            		}	            		
	            	}
            	}catch (Exception e) {
            		DataManager.i().log(Level.SEVERE,e);
            	}
                return null;
            }
        };
        
      	task.setOnScheduled(e ->  {
      		EtcAdmin.i().setStatusMessage("Updating History Data...");
      		EtcAdmin.i().setProgress(0.25);
      	});
      			
    	task.setOnSucceeded(e ->  showTransmissions());
    	
    	task.setOnFailed(e ->  {
	    	DataManager.i().log(Level.SEVERE,task.getException());
	    	showTransmissions();
    	});
    	
    	Thread queueRefresh = new Thread(task);
    	queueRefresh.start();
	}
	
	
	private void showTransmissions() 
	{
  		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0.0);
		tableView.getItems().clear();
		String searchString = "";
		if(transData != null && transData.size() > 0)
		{
			for(TransmissionData td : transData)
			{
				if(td == null) continue;
				if (filterField.getText().isEmpty() == false) {
					searchString = td.getEmployer().getName();
					//searchString += " " + airTrans.getType();
					//searchString += " " + cch.getTaxYear();
					//searchString += " " + cch.getBatchId();
					//searchString += " " + Utils.getDateString(cch.getDate());

					if (searchString.toUpperCase().contains(filterField.getText().toUpperCase()))
						tableView.getItems().add(new HBoxTransData(td));
				} else
					tableView.getItems().add(new HBoxTransData(td));
			};	
		}
		// add the header
		//tableView.getItems().add(0,new HBoxTransData(null));
	}

/*	private void viewSelectedHistory() 
	{
		// offset one for the header row
		DataManager.i().setDepartment(dataList.getSelectionModel().getSelectedIndex() - 1);
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/department/ViewDepartment.fxml"));
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
*/	
	@FXML
	private void onShowInactivesClick(ActionEvent event) {
		showTransmissions();
	}	
	
	@FXML
	private void onTaxYearSelection(ActionEvent event) {
		taxYearString = taxYearCombo.getValue();
		if (taxYearString != "")
			updateFilingData();
	}	
	
	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	@FXML
	private void onClearFilter(ActionEvent event) {
		filterField.clear();
		showTransmissions();
	}	
	
	@FXML
	private void onFilter() {
		showTransmissions();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) tableView.getScene().getWindow();
		stage.close();
	}
	
	public class HBoxTransData extends HBox 
	{
		SimpleStringProperty employer = new SimpleStringProperty();
		SimpleStringProperty year = new SimpleStringProperty();
		SimpleStringProperty generatedon = new SimpleStringProperty();
		SimpleStringProperty transid = new SimpleStringProperty();
		SimpleStringProperty active = new SimpleStringProperty();
		SimpleStringProperty filetype = new SimpleStringProperty();
		SimpleStringProperty receipttimestamp = new SimpleStringProperty();
		SimpleStringProperty type10945 = new SimpleStringProperty();
		SimpleStringProperty count1094 = new SimpleStringProperty();
		SimpleStringProperty count1095 = new SimpleStringProperty();
		SimpleStringProperty filingtype = new SimpleStringProperty();
		SimpleStringProperty filingstate = new SimpleStringProperty();
		SimpleStringProperty receiptid = new SimpleStringProperty();
		SimpleStringProperty utid = new SimpleStringProperty();
		SimpleStringProperty filingmethod = new SimpleStringProperty();
		SimpleStringProperty filingstatus = new SimpleStringProperty();
		
        public String getEmployer() {
          	 return employer.get();
           }
           
        public String getYear() {
         	 return year.get();
          }
          
        public String getGeneratedOn() {
         	 return generatedon.get();
          }
          
        public String getTransId() {
         	 return transid.get();
          }
          
        public String getActive() {
         	 return active.get();
          }
          
        public String getFileType() {
         	 return filetype.get();
          }
          
        public String getRecieptTimestamp() {
         	 return receipttimestamp.get();
          }
          
        public String getType10945() {
         	 return type10945.get();
          }
          
        public String getCount1094() {
         	 return count1094.get();
          }
          
        public String getCount1095() {
        	 return count1095.get();
         }
        
       public String getFilingType() {
         	 return filingtype.get();
          }
          
       public String getFilingState() {
       	 return filingstate.get();
        }
        
        public String getReceiptId() {
         	 return receiptid.get();
          }
          
        public String getUTID() {
         	 return utid.get();
          }
          
        public String getFilingMethod() {
        	 return filingmethod.get();
         }
         
        public String getFilingStatus() {
        	 return filingstatus.get();
         }
         

		HBoxTransData(TransmissionData td)
         {
              super();
              
              if(td != null) {
          		employer.set(td.getEmployer().getName());
        		year.set(String.valueOf(td.getYear()));
        		generatedon.set(Utils.getDateString(td.getTransTimestamp()));
        		transid.set(td.getUniqTransId().toString());
        		active.set(td.getActive());
        		filetype.set(td.getOperationType().toString());
        		receipttimestamp.set(Utils.getDateString(td.getReceiptTimestamp()));
        		//type10945.set(
        		count1094.set(String.valueOf(td.getIrs1094FormCount()));
        		count1095.set(String.valueOf(td.getIrs1094FormCount()));
        		filingtype.set(td.getFilingType().toString());
        		filingstate.set(td.getFilingState().name());
        		receiptid.set(td.getReceiptId());
        		utid.set(td.getUniqTransId());
        		filingmethod.set(td.getMethodType().toString());
        		filingstatus.set(td.getStatus().toString());
              }
         }
    }	

}


