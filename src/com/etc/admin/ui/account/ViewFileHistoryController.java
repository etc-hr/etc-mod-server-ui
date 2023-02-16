package com.etc.admin.ui.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.account.ViewAccountController.HBoxSpecCell;
import com.etc.admin.ui.coresysteminfo.ViewCoreSystemInfoController;
import com.etc.admin.ui.pipeline.queue.PipelineQueue;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.ems.pipeline.entities.airerr.AirTranErrorFile;
import com.etc.corvetto.ems.pipeline.entities.airrct.AirTranReceiptFile;
import com.etc.corvetto.ems.pipeline.entities.c94.Irs1094cFile;
import com.etc.corvetto.ems.pipeline.entities.c95.Irs1095cFile;
import com.etc.corvetto.ems.pipeline.entities.cov.CoverageFile;
import com.etc.corvetto.ems.pipeline.entities.ded.DeductionFile;
import com.etc.corvetto.ems.pipeline.entities.emp.EmployeeFile;
import com.etc.corvetto.ems.pipeline.entities.ins.InsuranceFile;
import com.etc.corvetto.ems.pipeline.entities.pay.PayFile;
import com.etc.corvetto.ems.pipeline.entities.ppd.PayPeriodFile;
import com.etc.corvetto.ems.pipeline.rqs.airerr.AirTranErrorFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.airrct.AirTranReceiptFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.c94.Irs1094cFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.c95.Irs1095cFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.cov.CoverageFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.ded.DeductionFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.EmployeeFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.ins.InsuranceFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.PayFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.ppd.PayPeriodFileRequest;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.rqs.EmployerRequest;
import com.etc.corvetto.rqs.PayPeriodRequest;
import com.etc.entities.CoreData;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewFileHistoryController 
{
    @FXML
    private ComboBox<String> employerCombo;

    @FXML
    private TableView<HBoxFileHistoryCell> fileHistoryTableView;

    @FXML
    private Button acctFileHistoryViewSpecButton;

    @FXML
    private Button acctFileHistoryViewDynSpecButton;
    
    @FXML
    private ComboBox<String> dateRangeCombo;

    @FXML
    private Button closeButton;

    @FXML
    private Button clearButton;

    @FXML
    private Label fileHistoryLabel;

	//table sort 
	TableColumn<HBoxFileHistoryCell, String> sortColumn = null;
	
	//file history queue
	List<PipelineQueue> fileHistoryQueue = new ArrayList<PipelineQueue>();	

	// file history data object
	FileHistoryData fileHistoryData = new FileHistoryData();
	
	//selected fileHistory queue
	PipelineQueue selectedFileHistoryQueue = null;
	Logger logr;

    private List<Long> currentEmployerListIds = new ArrayList<Long>();
    public PipelineFileType fileType;
    Employer employer;
    
    int dateRangeMonths = 6;
    
    private boolean resetting = false;
    
    // initialize is called when the FXML is loaded
	@FXML
	public void initialize() 
	{
		logr = Logger.getLogger(this.getClass().getName());
		initControls();
    	loadSearchEmployers();
		// launch the data load threads
		updateFileHistoryData();
	}
	
	private void initControls() 
	{
		//disable the view spec button until something is selected
		//acctFileHistoryViewSpecButton.setDisable(true);
		//acctFileHistoryViewDynSpecButton.setDisable(true);
		fileHistoryTableView.setPlaceholder(new Label(""));
		
		// add the date range choices
		dateRangeCombo.getItems().clear();
		dateRangeCombo.getItems().add("6 Months");
		dateRangeCombo.getItems().add("1 Year");
		dateRangeCombo.getItems().add("All");
		dateRangeCombo.getSelectionModel().select(0);
		

		//add handlers for listbox selection notification
		// FILE HISTORY
		fileHistoryTableView.setOnMouseClicked(mouseClickedEvent -> 
		{
	        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) 
	        {
            	if (selectFileHistoryObjects() == false) return;
				// set the request and refresh the screen
				 HBoxFileHistoryCell cellItem = fileHistoryTableView.getSelectionModel().getSelectedItem();
				 if(cellItem != null)
				 {
					 DataManager.i().mPipelineQueueEntry = fileHistoryQueue.get(cellItem.queueLocation);
					 //if(DataManager.i().mPipelineQueueEntry.getRecords() > 0 || DataManager.i().mPipelineQueueEntry.getUnits() > 0)
					 EtcAdmin.i().setScreen(ScreenType.PIPELINERAWFIELDGRIDFROMACCOUNT, true);
					 //exit the popup so we can see it
					 exitPopup();
				 }else 
					 System.out.println("Null CellItem on pqueFilesListView in Queue");
	        }
        });
		
		// set the table columns
		setTableColumns();
	}
	
	private void setTableColumns()
	{
		//clear the default values
		fileHistoryTableView.getColumns().clear();

	    TableColumn<HBoxFileHistoryCell, String> colUser = new TableColumn<HBoxFileHistoryCell, String>("User");
	    colUser.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("user"));
	    colUser.setMinWidth(100);
		fileHistoryTableView.getColumns().add(colUser);
		setTableCellFactory(colUser);
		
		TableColumn<HBoxFileHistoryCell, String> colDate = new TableColumn<HBoxFileHistoryCell, String>("Date");
		colDate.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("dateTime"));
		colDate.setMinWidth(150);
		//initial sort is by date
		sortColumn = colDate;
		colDate.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		sortColumn.setSortType(SortType.DESCENDING);
		fileHistoryTableView.getColumns().add(colDate);
		setTableCellFactory(colDate);
		
	    TableColumn<HBoxFileHistoryCell, String> colStatus = new TableColumn<HBoxFileHistoryCell, String>("Status");
	    colStatus.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("status"));
	    colStatus.setMinWidth(100);
		fileHistoryTableView.getColumns().add(colStatus);
		setTableCellFactory(colStatus);
		
		TableColumn<HBoxFileHistoryCell, String> x6 = new TableColumn<HBoxFileHistoryCell, String>("File Type");
		x6.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("fileType"));
		x6.setMinWidth(125);
		fileHistoryTableView.getColumns().add(x6);
		setTableCellFactory(x6);
		
		TableColumn<HBoxFileHistoryCell, String> colEmployer = new TableColumn<HBoxFileHistoryCell, String>("Employer");
		colEmployer.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("employer"));
		colEmployer.setMinWidth(200);
		fileHistoryTableView.getColumns().add(colEmployer);
		setTableCellFactory(colEmployer);
		
		TableColumn<HBoxFileHistoryCell, String> colDesc = new TableColumn<HBoxFileHistoryCell, String>("Description");
		colDesc.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("description"));
		colDesc.setMinWidth(525);
		fileHistoryTableView.getColumns().add(colDesc);
		setTableCellFactory(colDesc);
		
		TableColumn<HBoxFileHistoryCell, String> colSpecId = new TableColumn<HBoxFileHistoryCell, String>("Spec Id");
		colSpecId.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("specId"));
		colSpecId.setMinWidth(75);
		colSpecId.setComparator((String o1, String o2) -> {
            return Integer.compare(Integer.valueOf(o1), Integer.valueOf(o2));
        });
	    fileHistoryTableView.getColumns().add(colSpecId);
		setTableCellFactory(colSpecId);
		
		TableColumn<HBoxFileHistoryCell, String> colSpec = new TableColumn<HBoxFileHistoryCell, String>("Spec");
		colSpec.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("spec"));
		colSpec.setMinWidth(200);
	    fileHistoryTableView.getColumns().add(colSpec);
		setTableCellFactory(colSpec);
		
		TableColumn<HBoxFileHistoryCell, String> colMapperId = new TableColumn<HBoxFileHistoryCell, String>("Mapper Id");
		colMapperId.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("mapperId"));
		colMapperId.setMinWidth(75);
		colMapperId.setSortable(true);
		colMapperId.setComparator((String o1, String o2) -> {
			if (o1 == null || o2 == null || o1 == "" || o2 == "") 
				return 0;
			else
				return Integer.compare(Integer.valueOf(o1), Integer.valueOf(o2));
        });
	    fileHistoryTableView.getColumns().add(colMapperId);
		setTableCellFactory(colMapperId);
	    
	    TableColumn<HBoxFileHistoryCell, String> x7 = new TableColumn<HBoxFileHistoryCell, String>("Result");
		x7.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("result"));
		x7.setMinWidth(800);
		fileHistoryTableView.getColumns().add(x7);
		setTableCellFactory(x7);
	}
	
	private void setTableCellFactory(TableColumn<HBoxFileHistoryCell, String>  col) 
	{
		col.setCellFactory(column -> 
		{
		    return new TableCell<HBoxFileHistoryCell, String>() 
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
		                HBoxFileHistoryCell cell = getTableView().getItems().get(getIndex());
		                if(cell.isRejected() == true)
		                	setTextFill(Color.ORANGE);
		                else
		                	setTextFill(Color.BLACK);
		            }
		        }
		    };
		});
	}



	public void loadSearchEmployers()
	{
		try {
			
			String sName;
			
			ObservableList<String> employerNames = FXCollections.observableArrayList();
			
			// verify that we have employers not stuck in a persistance bag
			List<Employer> employers = DataManager.i().mAccount.getEmployers();
			if (employers == null) {
				EmployerRequest request = new EmployerRequest();
				request.setAccountId(DataManager.i().mAccount.getId());
				employers = AdminPersistenceManager.getInstance().getAll(request);
				DataManager.i().mAccount.setEmployers(employers);
			}
			if (employers == null) return;
			
			for(Employer empr : employers) 
			{
				if(empr.isActive() == false) continue;
				sName = empr.getName();
				if(sName != null) 
				{
					employerNames.add(sName);
					currentEmployerListIds.add(empr.getId());
				}
			}

			FXCollections.sort(employerNames);
			// use a filterlist to wrap the account names for combobox searching later
	        FilteredList<String> filteredItems = new FilteredList<String>(employerNames, p -> true);

	        // add a listener for the edit control on the combobox
	        // the listener will filter the list according to what is in the search box edit control
	        employerCombo.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> 
			{
	            final javafx.scene.control.TextField editor = employerCombo.getEditor();
	            final String selected = String.valueOf(employerCombo.getSelectionModel().getSelectedItem());
	            
	            employerCombo.show();
	            employerCombo.setVisibleRowCount(10);

	            // Run on the GUI thread
	            Platform.runLater(() -> 
	            {
	                if(selected == null || !selected.equals(editor.getText())) 
	                {
	                    filteredItems.setPredicate(item -> 
	                    {
	                        if(item.toUpperCase().contains(newvalue.toUpperCase()))
	                        {
	                            return true;
	                        } else {
	                            return false;
	                        }
	                    });
	                }
	            });
			});

			//finally, set our filtered items as the combobox collection
	        employerCombo.setItems(filteredItems);	

			//close the dropdown if it is showing
	        employerCombo.hide();

//			} catch (Exception e) { logr.log(Level.SEVERE, "Exception.", e); }
		}catch (Exception e) { 
			DataManager.i().logGenericException(e); 
		}
	}
	
	private void selectedEmployer()
	{
		fileHistoryTableView.getItems().clear();
		getDateRange();
		
		String sSelection = employerCombo.getSelectionModel().getSelectedItem();
		for(Employer empr : DataManager.i().mAccount.getEmployers())
		{
			if(sSelection == null || sSelection.isEmpty() == true || sSelection.contains(empr.getName()))
			{
				employer = empr;
				
            	//reset the queue
            	fileHistoryQueue.clear();
            	
    			fileHistoryData.refreshFileHistory(employer.getId(), dateRangeMonths);

    			//add to our account queue 
    			for(PipelineQueue queue : fileHistoryData.mPipelineQueue) 
    				fileHistoryQueue.add(queue); 
				break;
			}
		}
		showFileHistory();
	}
	
	private void getDateRange()
	{
		dateRangeMonths = 6;
		if (dateRangeCombo.getValue() == "1 Year")
			dateRangeMonths = 12;
		if (dateRangeCombo.getValue() == "All")
			dateRangeMonths = -1;
		
	}

	////////////////////////////////////////////////////////////////////////////////////////
	///// FILE HISTORY
	////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	private void updateFileHistoryData() 
	{
		// clear the current info
		fileHistoryTableView.getItems().clear();
//		acctFileHistoryLabel.setText("File History (loading...)");
		// keep our current sort
       if(fileHistoryTableView.getSortOrder().size()>0)
            sortColumn = (TableColumn<HBoxFileHistoryCell, String>) fileHistoryTableView.getSortOrder().get(0);

       getDateRange();
		// create a thread to handle the update, letting the screen respond normally
		Task<Void> task = new Task<Void>() 
		{
            @Override
            protected Void call() throws Exception 
            {
            	//reset the queue
            	fileHistoryQueue.clear();

            	//iterate through the employers for this account
        		for(Employer employer : DataManager.i().mAccount.getEmployers()) 
        		{ 
        			fileHistoryData.refreshFileHistory(employer.getId(), dateRangeMonths);

        			//add to our account queue 
        			for(PipelineQueue queue : fileHistoryData.mPipelineQueue) 
        				fileHistoryQueue.add(queue); 
        		}	 
        		return null;
            }
        };
        
      	task.setOnScheduled(e ->  {
  		EtcAdmin.i().setStatusMessage("Updating File History Data...");
  		EtcAdmin.i().setProgress(0.25);});
      			
      	task.setOnSucceeded(e ->  showFileHistory());
    	task.setOnFailed(e ->  showFileHistory());
        //new Thread(task).start();
    	try {
			EmsApp.getInstance().getFxQueue().put(task);
		} catch (InterruptedException e) {
			logr.log(Level.SEVERE, "Exception.", e);
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}

	//update the file history list
	private void showFileHistory() 
	{
		// clear the lists
		fileHistoryTableView.getItems().clear();
//		acctFileHistoryLabel.setText("File History");
		//reset our refresh counter
		boolean state1 = false;
		boolean state2 = false;
		boolean state3 = false;
		boolean state4 = false;
		boolean state5 = false;

		//load the queue
		if(fileHistoryQueue != null)
		{
		    List<HBoxFileHistoryCell> requestList = new ArrayList<>();
		    List<HBoxSpecCell> specList = new ArrayList<>();
			specList.add(new HBoxSpecCell("Request",
					  					  "Spec Id",
					  					  "Specification",
					  					  "Mapper Id", 
					  					  "Mapper",
					  					  true));
			for (int i = 0; i < fileHistoryQueue.size(); i++)
			{
				PipelineQueue queue = fileHistoryQueue.get(i);
				if(queue == null) continue;
				if(queue.getRequest() == null) continue;
				if(queue.getRequest().getSpecification() == null) continue;
				queue.setUnits(0l);
				queue.setRecords(0l);
				//apply filter if present
				if(employerCombo.getEditor().getText().contentEquals("") == false) 
				{
					String searchData = queue.getRequest().getDescription().toLowerCase();
					searchData += Utils.getDateTimeString24(queue.getRequest().getAsOf()) + " ";
					if(queue.getRequest().getEmployer() != null)
						searchData += queue.getRequest().getEmployer().getName() + " ";
					if (queue.getRequest().getSpecification() != null) {
						searchData += queue.getRequest().getSpecification().getId().toString() + " ";
						searchData += queue.getRequest().getSpecification().getName() + " ";
					}
					if (queue.getRequest().getStatus() != null) {
						searchData += queue.getRequest().getStatus().toString() + " ";
					}
					if(queue.getRequest().getCreatedBy() != null) {
						if(queue.getRequest().getCreatedBy().getFirstName() != null)
							searchData += queue.getRequest().getCreatedBy().getFirstName().toLowerCase() +  " "; 
						if(queue.getRequest().getCreatedBy().getLastName() != null)
							searchData += queue.getRequest().getCreatedBy().getLastName().toLowerCase() +  " "; 
						searchData += String.valueOf(queue.getFileType()).toLowerCase() + " ";
					}
					if(queue.getRequest().getResult() != null)
						searchData += queue.getRequest().getResult().toLowerCase();
					if( queue.getRequest().getSpecification().getDynamicFileSpecificationId() != null && queue.getRequest().getSpecification().getDynamicFileSpecificationId() != null) {
						searchData +=  queue.getRequest().getSpecification().getDynamicFileSpecificationId().toString() + "";
					}
					if (queue.getFileType() != null)
						searchData += queue.getFileType().toString() + " ";
					if(searchData.toLowerCase().contains(employerCombo.getEditor().getText().toLowerCase()) == false) continue;
				}
				boolean bRejected = false;
				switch(queue.getFileType())
				{
					case COVERAGE: 
						if(queue.getCoverageFile() != null && queue.getCoverageFile().getPipelineInformation() != null) 
						{
							state1 = queue.getCoverageFile().getPipelineInformation().isInitialized();
							state2 = queue.getCoverageFile().getPipelineInformation().isParsed();
							state3 = queue.getCoverageFile().getPipelineInformation().isValidated();
							state4 = queue.getCoverageFile().getPipelineInformation().isCompleted();
							state5 = queue.getCoverageFile().getPipelineInformation().isFinalized();
							bRejected = queue.getCoverageFile().getPipelineInformation().isRejected();
							queue.setUnits(queue.getCoverageFile().getPipelineInformation().getUnits());
							queue.setRecords(queue.getCoverageFile().getPipelineInformation().getRecords());
						}
						break;
					case EMPLOYEE: 
						if(queue.getEmployeeFile() != null && queue.getEmployeeFile().getPipelineInformation() != null) 
						{
							state1 = queue.getEmployeeFile().getPipelineInformation().isInitialized();
							state2 = queue.getEmployeeFile().getPipelineInformation().isParsed();
							state3 = queue.getEmployeeFile().getPipelineInformation().isValidated();
							state4 = queue.getEmployeeFile().getPipelineInformation().isCompleted();
							state5 = queue.getEmployeeFile().getPipelineInformation().isFinalized();
							bRejected = queue.getEmployeeFile().getPipelineInformation().isRejected();
							queue.setUnits(queue.getEmployeeFile().getPipelineInformation().getUnits());
							queue.setRecords(queue.getEmployeeFile().getPipelineInformation().getRecords());
						}
						break;
					case IRS1094C: 
						if(queue.getIrs1094cFile() != null && queue.getIrs1094cFile().getPipelineInformation() != null) 
						{
							state1 = queue.getIrs1094cFile().getPipelineInformation().isInitialized();
							state2 = queue.getIrs1094cFile().getPipelineInformation().isParsed();
							state3 = queue.getIrs1094cFile().getPipelineInformation().isValidated();
							state4 = queue.getIrs1094cFile().getPipelineInformation().isCompleted();
							state5 = queue.getIrs1094cFile().getPipelineInformation().isFinalized();
							bRejected = queue.getIrs1094cFile().getPipelineInformation().isRejected();
							queue.setUnits(queue.getIrs1094cFile().getPipelineInformation().getUnits());
							queue.setRecords(queue.getIrs1094cFile().getPipelineInformation().getRecords());
						}
						break;
					case IRS1095C: 
						if(queue.getIrs1095cFile() != null && queue.getIrs1095cFile().getPipelineInformation() != null) 
						{
							state1 = queue.getIrs1095cFile().getPipelineInformation().isInitialized();
							state2 = queue.getIrs1095cFile().getPipelineInformation().isParsed();
							state3 = queue.getIrs1095cFile().getPipelineInformation().isValidated();
							state4 = queue.getIrs1095cFile().getPipelineInformation().isCompleted();
							state5 = queue.getIrs1095cFile().getPipelineInformation().isFinalized();
							bRejected = queue.getIrs1095cFile().getPipelineInformation().isRejected();
							queue.setUnits(queue.getIrs1095cFile().getPipelineInformation().getUnits());
							queue.setRecords(queue.getIrs1095cFile().getPipelineInformation().getRecords());
						}
						break;
					case EVENT: 
						break;
					case IRSAIRERR: 
						if(queue.getAirTranErrorFile() != null && queue.getAirTranErrorFile().getPipelineInformation() != null) 
						{
							state1 = queue.getAirTranErrorFile().getPipelineInformation().isInitialized();
							state2 = queue.getAirTranErrorFile().getPipelineInformation().isParsed();
							state3 = queue.getAirTranErrorFile().getPipelineInformation().isValidated();
							state4 = queue.getAirTranErrorFile().getPipelineInformation().isCompleted();
							state5 = queue.getAirTranErrorFile().getPipelineInformation().isFinalized();
							bRejected = queue.getAirTranErrorFile().getPipelineInformation().isRejected();
							queue.setUnits(queue.getAirTranErrorFile().getPipelineInformation().getUnits());
							queue.setRecords(queue.getAirTranErrorFile().getPipelineInformation().getRecords());
						}
						break;
					case IRSAIRRCPT: 
						if(queue.getAirTranReceiptFile() != null && queue.getAirTranReceiptFile().getPipelineInformation() != null) 
						{
							state1 = queue.getAirTranReceiptFile().getPipelineInformation().isInitialized();
							state2 = queue.getAirTranReceiptFile().getPipelineInformation().isParsed();
							state3 = queue.getAirTranReceiptFile().getPipelineInformation().isValidated();
							state4 = queue.getAirTranReceiptFile().getPipelineInformation().isCompleted();
							state5 = queue.getAirTranReceiptFile().getPipelineInformation().isFinalized();
							bRejected = queue.getAirTranReceiptFile().getPipelineInformation().isRejected();
							queue.setUnits(queue.getAirTranReceiptFile().getPipelineInformation().getUnits());
							queue.setRecords(queue.getAirTranReceiptFile().getPipelineInformation().getRecords());
						}
						break;
					case PAY: 
						if(queue.getPayFile() != null && queue.getPayFile().getPipelineInformation() != null) 
						{
							state1 = queue.getPayFile().getPipelineInformation().isInitialized();
							state2 = queue.getPayFile().getPipelineInformation().isParsed();
							state3 = queue.getPayFile().getPipelineInformation().isValidated();
							state4 = queue.getPayFile().getPipelineInformation().isCompleted();
							state5 = queue.getPayFile().getPipelineInformation().isFinalized();
							bRejected = queue.getPayFile().getPipelineInformation().isRejected();
							queue.setUnits(queue.getPayFile().getPipelineInformation().getUnits());
							queue.setRecords(queue.getPayFile().getPipelineInformation().getRecords());
						}
						break;
					case PAYPERIOD: 
						if(queue.getPayPeriodFile() != null && queue.getPayPeriodFile().getPipelineInformation() != null) 
						{
							state1 = queue.getPayPeriodFile().getPipelineInformation().isInitialized();
							state2 = queue.getPayPeriodFile().getPipelineInformation().isParsed();
							state3 = queue.getPayPeriodFile().getPipelineInformation().isValidated();
							state4 = queue.getPayPeriodFile().getPipelineInformation().isCompleted();
							state5 = queue.getPayPeriodFile().getPipelineInformation().isFinalized();
							bRejected = queue.getPayPeriodFile().getPipelineInformation().isRejected();
							queue.setUnits(queue.getPayPeriodFile().getPipelineInformation().getUnits());
							queue.setRecords(queue.getPayPeriodFile().getPipelineInformation().getRecords());
						}
						break;
			//		case PLAN: 
			//			if(queue.getPlanFile() != null && queue.getPlanFile().getPipelineInformation() != null) 
			//			{
			//				state1 = queue.getPlanFile().getPipelineInformation().isInitialized();
			//				state2 = queue.getPlanFile().getPipelineInformation().isParsed();
			//				state3 = queue.getPlanFile().getPipelineInformation().isValidated();
			//				state4 = queue.getPlanFile().getPipelineInformation().isCompleted();
			//				state5 = queue.getPlanFile().getPipelineInformation().isFinalized();
			//				queue.setUnits(queue.getPlanFile().getPipelineInformation().getUnits());
			//				queue.setRecords(queue.getPlanFile().getPipelineInformation().getRecords());
			//			}
			//			break;
					default:
						break;
				}

				String user = "";
				if(queue.getRequest().getCreatedBy() != null)
					user = queue.getRequest().getCreatedBy().getFirstName() + " " + queue.getRequest().getCreatedBy().getLastName().substring(0,1) + ".";
				String employer = "";
				if(queue.getRequest().getEmployer() != null)
					employer = queue.getRequest().getEmployer().getName();
				String mapperId = "";
				if( queue.getRequest().getSpecification().getDynamicFileSpecificationId() != null)
					mapperId =  queue.getRequest().getSpecification().getDynamicFileSpecificationId().toString();
				requestList.add(new HBoxFileHistoryCell(i, user, Utils.getDateTimeString24(queue.getRequest().getAsOf()), // getLastUpdated()), 
						  				  employer, 
										  queue.getRequest().getDescription(), 
										  String.valueOf(queue.getUnits()),
										  String.valueOf(queue.getRecords()),
										  queue.getRequest().getSpecification().getId().toString(),
										  queue.getRequest().getSpecification().getName(),
										  mapperId, //queue.getRequest().getSpecification().getDynamicFileSpecificationId().toString(),
										  queue.getRequest().getStatus().toString(),
										  queue.getFileType().toString(),
										  state1, state2, state3, state4, state5,
										  queue.getRequest().getResult(), bRejected));
			}	
			
			ObservableList<HBoxFileHistoryCell> myObservableList = FXCollections.observableList(requestList);
			fileHistoryTableView.setItems(myObservableList);	
			// sort
			if(sortColumn!=null) 
			{
				fileHistoryTableView.getSortOrder().add(sortColumn);
	            sortColumn.setSortable(true);
	        }
		}

		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0);
	}	

	private boolean selectFileHistoryObjects()
	{
 		try {
			// check to see if we have a bad selection
			if(fileHistoryTableView.getSelectionModel().getSelectedIndex() < 0) return false;
			selectedFileHistoryQueue = fileHistoryQueue.get(fileHistoryTableView.getSelectionModel().getSelectedItem().getQueueLocation());
	 		DataManager.i().mPipelineChannel = selectedFileHistoryQueue.getRequest().getSpecification().getChannel();
	 		DataManager.i().mPipelineSpecification = selectedFileHistoryQueue.getRequest().getSpecification();
	 		switch (selectedFileHistoryQueue.getFileType()) 
	 		{
				case COVERAGE: 
		     	//	DataManager.i().loadDynamicCoverageFileSpecification(selectedFileHistoryQueue.getRequest().getSpecification().getDynamicFileSpecificationId());
		     		CoverageFileRequest covReq = new CoverageFileRequest();
		     		covReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
					selectedFileHistoryQueue.setCoverageFile(AdminPersistenceManager.getInstance().get(covReq));
					DataManager.i().mCoverageFile = selectedFileHistoryQueue.getCoverageFile();
		    		return true;
				case EMPLOYEE: 
		     	//	DataManager.i().loadDynamicEmployeeFileSpecification(selectedFileHistoryQueue.getRequest().getSpecification().getDynamicFileSpecificationId());
		     		EmployeeFileRequest empReq = new EmployeeFileRequest();
		     		empReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
		     		selectedFileHistoryQueue.setEmployeeFile(AdminPersistenceManager.getInstance().get(empReq));
					DataManager.i().mEmployeeFile = selectedFileHistoryQueue.getEmployeeFile();
		    		return true;
				case PAY: 
		     	//	DataManager.i().loadDynamicPayFileSpecification(selectedFileHistoryQueue.getRequest().getSpecification().getDynamicFileSpecificationId());
		     		PayFileRequest payReq = new PayFileRequest();
		     		payReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
		     		selectedFileHistoryQueue.setPayFile(AdminPersistenceManager.getInstance().get(payReq));
					DataManager.i().mPayFile = selectedFileHistoryQueue.getPayFile();
		     		return true;
				case PAYPERIOD: 
		     		PayPeriodFileRequest ppdReq = new PayPeriodFileRequest();
		     		ppdReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
		     		selectedFileHistoryQueue.setPayPeriodFile(AdminPersistenceManager.getInstance().get(ppdReq));
					DataManager.i().mPayPeriodFile = selectedFileHistoryQueue.getPayPeriodFile();
		     		return true;
				case IRS1094C: 
		     		Irs1094cFileRequest irs1094cFileReq = new Irs1094cFileRequest();
		     		irs1094cFileReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
		     		selectedFileHistoryQueue.setIrs1094cFile(AdminPersistenceManager.getInstance().get(irs1094cFileReq));
					DataManager.i().mIrs1094cFile = selectedFileHistoryQueue.getIrs1094cFile();
		     		return true;
				case IRS1095C: 
		     		Irs1095cFileRequest irs1095cFileReq = new Irs1095cFileRequest();
		     		irs1095cFileReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
		     		selectedFileHistoryQueue.setIrs1095cFile(AdminPersistenceManager.getInstance().get(irs1095cFileReq));
					DataManager.i().mIrs1095cFile = selectedFileHistoryQueue.getIrs1095cFile();
		     		return true;
				case DEDUCTION: 
		     		DeductionFileRequest deductionFileReq = new DeductionFileRequest();
		     		deductionFileReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
		     		selectedFileHistoryQueue.setDeductionFile(AdminPersistenceManager.getInstance().get(deductionFileReq));
					DataManager.i().mDeductionFile = selectedFileHistoryQueue.getDeductionFile();
		     		return true;
				case INSURANCE: 
		     		InsuranceFileRequest insuranceFileReq = new InsuranceFileRequest();
		     		insuranceFileReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
		     		selectedFileHistoryQueue.setInsuranceFile(AdminPersistenceManager.getInstance().get(insuranceFileReq));
					DataManager.i().mInsuranceFile = selectedFileHistoryQueue.getInsuranceFile();
		     		return true;
				default:
		    		return false;
			}
		} catch (CoreException e) {
			logr.log(Level.SEVERE, "Exception.", e);
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
 		return false;
	}
	
    @FXML
    void onEmployerSelect(ActionEvent event) 
    {
    	if (resetting == true) {
    		resetting = false;
    		return;
    	}
    	selectedEmployer();
    }

    @FXML
    void onRangeSelect(ActionEvent event) 
    {
    	updateFileHistoryData();
    }

	@FXML
	private void onFileFilterKeyRelease() 
	{
		showFileHistory();
	}	

    @FXML
    void onClearFileFilter(ActionEvent event)
    {
    	employerCombo.getSelectionModel().clearSelection();
    	employerCombo.getEditor().clear();
    	employerCombo.hide();
    	showFileHistory();
    }

    @FXML
    void onCopyFilename(ActionEvent event)
    {
    	try {
			HBoxFileHistoryCell cellItem = fileHistoryTableView.getSelectionModel().getSelectedItem();
			if(cellItem != null)
			{
				DataManager.i().mPipelineQueueEntry = fileHistoryQueue.get(cellItem.queueLocation);
				final Clipboard clipboard = Clipboard.getSystemClipboard();
				final ClipboardContent content = new ClipboardContent();
				content.putString(DataManager.i().mPipelineQueueEntry.getRequest().getDescription());
				clipboard.setContent(content);
				//Utils.showAlert("Copied", "Selected filename copied to System Clipboard.");
			}else {
				Utils.showAlert("No Selection", "Please select file entry to copy");
				return;
			}
	     }catch (Exception e){
	    	 Utils.showAlert("No Selection", "Please select file entry to copy.");
	     }
    }

    @FXML
    void onDownloadFile(ActionEvent event)
    {
     	try {
     		Long docId = 0l;
			HBoxFileHistoryCell cellItem = fileHistoryTableView.getSelectionModel().getSelectedItem();
			if(cellItem != null)
			{
				PipelineQueue selectedFileHistoryQueue = fileHistoryQueue.get(cellItem.queueLocation);
		 		switch (selectedFileHistoryQueue.getFileType()) 
		 		{
					case COVERAGE: 
			     		CoverageFileRequest covReq = new CoverageFileRequest();
			     		covReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
						selectedFileHistoryQueue.setCoverageFile(AdminPersistenceManager.getInstance().get(covReq));
						docId = selectedFileHistoryQueue.getCoverageFile().getPipelineInformation().getDocumentId();
			    		break;
					case EMPLOYEE: 
			     		EmployeeFileRequest empReq = new EmployeeFileRequest();
			     		empReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
			     		selectedFileHistoryQueue.setEmployeeFile(AdminPersistenceManager.getInstance().get(empReq));
						docId = selectedFileHistoryQueue.getEmployeeFile().getPipelineInformation().getDocumentId();
			    		break;
					case PAY: 
			     		PayFileRequest payReq = new PayFileRequest();
			     		payReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
			     		selectedFileHistoryQueue.setPayFile(AdminPersistenceManager.getInstance().get(payReq));
						docId = selectedFileHistoryQueue.getPayFile().getPipelineInformation().getDocumentId();
			    		break;
					case IRS1094C: 
			     		Irs1094cFileRequest irs1094cReq = new Irs1094cFileRequest();
			     		irs1094cReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
			     		selectedFileHistoryQueue.setIrs1094cFile(AdminPersistenceManager.getInstance().get(irs1094cReq));
						docId = selectedFileHistoryQueue.getIrs1094cFile().getPipelineInformation().getDocumentId();
						break;
					case IRS1095C: 
						Irs1095cFileRequest irs1095cReq = new Irs1095cFileRequest();
						irs1095cReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
			     		selectedFileHistoryQueue.setIrs1095cFile(AdminPersistenceManager.getInstance().get(irs1095cReq));
						docId = selectedFileHistoryQueue.getIrs1095cFile().getPipelineInformation().getDocumentId();
						break;
					case IRSAIRERR: 
			     		AirTranErrorFileRequest airerrReq = new AirTranErrorFileRequest();
			     		airerrReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
			     		selectedFileHistoryQueue.setAirTranErrorFile(AdminPersistenceManager.getInstance().get(airerrReq));
						docId = selectedFileHistoryQueue.getAirTranErrorFile().getPipelineInformation().getDocumentId();
						break;
					case IRSAIRRCPT: 
			     		AirTranReceiptFileRequest airtranReq = new AirTranReceiptFileRequest();
			     		airtranReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
			     		selectedFileHistoryQueue.setAirTranReceiptFile(AdminPersistenceManager.getInstance().get(airtranReq));
						docId = selectedFileHistoryQueue.getAirTranReceiptFile().getPipelineInformation().getDocumentId();
						break;
					case PAYPERIOD: 
			     		PayPeriodFileRequest ppReq = new PayPeriodFileRequest();
			     		ppReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
			     		selectedFileHistoryQueue.setPayPeriodFile(AdminPersistenceManager.getInstance().get(ppReq));
						docId = selectedFileHistoryQueue.getPayPeriodFile().getPipelineInformation().getDocumentId();
						break;
					case DEDUCTION: 
			     		DeductionFileRequest ddfReq = new DeductionFileRequest();
			     		ddfReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
			     		selectedFileHistoryQueue.setDeductionFile(AdminPersistenceManager.getInstance().get(ddfReq));
						docId = selectedFileHistoryQueue.getDeductionFile().getPipelineInformation().getDocumentId();
						break;
					case INSURANCE: 
			     		InsuranceFileRequest insfReq = new InsuranceFileRequest();
			     		insfReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
			     		selectedFileHistoryQueue.setInsuranceFile(AdminPersistenceManager.getInstance().get(insfReq));
						docId = selectedFileHistoryQueue.getInsuranceFile().getPipelineInformation().getDocumentId();
						break;
					default:
						Utils.showAlert("Unsupported File Type", "The selected file type is not supported.");
						return;
				}	
		 		
		 		String path = Utils.browseDirectory((Stage)fileHistoryTableView.getScene().getWindow(), "");
		 		if (path == "") return;
		 		
				if (Utils.downloadFile(docId, path) == false) {
					return;
				}
				Utils.showAlert("File Downloaded", "The selected file has been downloaded to " + path);
			}else {
				Utils.showAlert("No Selection", "Please select file entry to Download");
				return;
			}
	     }catch (Exception e){
	    	 Utils.showAlert("No Selection", "Please select file entry to download.");
	     }
    }
    
    @FXML
    void onViewDynFileSpecification(ActionEvent event) 
    {
		if (selectFileHistoryObjects() == false) return;
		DataManager.i().setScreenType(ScreenType.PIPELINEDYNAMICFILESPECIFICATIONFROMACCOUNT);
		EtcAdmin.i().setScreen(ScreenType.PIPELINEDYNAMICFILESPECIFICATIONFROMACCOUNT, true);	
		exitPopup();
    }

    @FXML
    void onViewSpecification(ActionEvent event)
    {
		if (selectFileHistoryObjects() == false) return;
		DataManager.i().setScreenType(ScreenType.PIPELINESPECIFICATIONFROMACCOUNT);
		EtcAdmin.i().setScreen(ScreenType.PIPELINESPECIFICATIONFROMACCOUNT, true);	
		exitPopup();
    }

    @FXML
    void onClose(ActionEvent event)
    {
    	exitPopup();
    }
    
    @FXML
    void onReject(ActionEvent event)
    {
		if (selectFileHistoryObjects() == false) return;
     	try {
			HBoxFileHistoryCell cellItem = fileHistoryTableView.getSelectionModel().getSelectedItem();
			if(cellItem != null)
			{
				// confirm first
				Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
			    confirmAlert.setTitle("Reject File");
			    String confirmText = "Are You Sure You Want To Reject the file " + cellItem.getDescription() + "?";
			    confirmAlert.setContentText(confirmText);
			    EtcAdmin.i().positionAlertCenter(confirmAlert);
			    Optional<ButtonType> result = confirmAlert.showAndWait();
			    if ((result.isPresent()) && (result.get() != ButtonType.OK)) {
			    	EtcAdmin.i().showAppStatus("", "", 0, false);
			    	return;
			    }

				
			//	PipelineQueue selectedFileHistoryQueue = fileHistoryQueue.get(cellItem.queueLocation);
		 		switch (selectedFileHistoryQueue.getFileType()) 
		 		{
					case COVERAGE: 
			     		CoverageFileRequest covReq = new CoverageFileRequest();
			     		CoverageFile cf = selectedFileHistoryQueue.getCoverageFile();
			     		if (cf == null || cf.getPipelineInformation() == null) return;
			     		cf.getPipelineInformation().setRejected(true);
			     		covReq.setEntity(cf);
			     		covReq.setId(cf.getId());
			     		AdminPersistenceManager.getInstance().addOrUpdate(covReq);
			    		break;
					case EMPLOYEE: 
			     		EmployeeFileRequest empReq = new EmployeeFileRequest();
			     		EmployeeFile ef = selectedFileHistoryQueue.getEmployeeFile();
			     		if (ef == null || ef.getPipelineInformation() == null) return;
			     		ef.getPipelineInformation().setRejected(true);
			     		empReq.setEntity(ef);
			     		empReq.setId(ef.getId());
			     		AdminPersistenceManager.getInstance().addOrUpdate(empReq);
			    		break;
					case PAY: 
			     		PayFileRequest payReq = new PayFileRequest();
			     		PayFile pf = selectedFileHistoryQueue.getPayFile();
			     		if (pf == null || pf.getPipelineInformation() == null) return;
			     		pf.getPipelineInformation().setRejected(true);
			     		payReq.setEntity(pf);
			     		payReq.setId(pf.getId());
			     		AdminPersistenceManager.getInstance().addOrUpdate(payReq);
			    		break;
					case IRS1094C: 
			     		Irs1094cFileRequest irs1094cReq = new Irs1094cFileRequest();
			     		Irs1094cFile icf = selectedFileHistoryQueue.getIrs1094cFile();
			     		if (icf == null || icf.getPipelineInformation() == null) return;
			     		icf.getPipelineInformation().setRejected(true);
			     		irs1094cReq.setEntity(icf);
			     		irs1094cReq.setId(icf.getId());
			     		AdminPersistenceManager.getInstance().addOrUpdate(irs1094cReq);
						break;
					case IRS1095C: 
						Irs1095cFileRequest irs1095cReq = new Irs1095cFileRequest();
			     		Irs1095cFile icf5 = selectedFileHistoryQueue.getIrs1095cFile();
			     		if (icf5 == null || icf5.getPipelineInformation() == null) return;
			     		icf5.getPipelineInformation().setRejected(true);
			     		irs1095cReq.setEntity(icf5);
			     		irs1095cReq.setId(icf5.getId());
			     		AdminPersistenceManager.getInstance().addOrUpdate(irs1095cReq);
						break;
					case IRSAIRERR: 
			     		AirTranErrorFileRequest airerrReq = new AirTranErrorFileRequest();
			     		AirTranErrorFile atf = selectedFileHistoryQueue.getAirTranErrorFile();
			     		if (atf == null || atf.getPipelineInformation() == null) return;
			     		atf.getPipelineInformation().setRejected(true);
			     		airerrReq.setEntity(atf);
			     		airerrReq.setId(atf.getId());
			     		AdminPersistenceManager.getInstance().addOrUpdate(airerrReq);
						break;
					case IRSAIRRCPT: 
			     		AirTranReceiptFileRequest airtranReq = new AirTranReceiptFileRequest();
			     		AirTranReceiptFile arf = selectedFileHistoryQueue.getAirTranReceiptFile();
			     		if (arf == null || arf.getPipelineInformation() == null) return;
			     		arf.getPipelineInformation().setRejected(true);
			     		airtranReq.setEntity(arf);
			     		airtranReq.setId(arf.getId());
			     		AdminPersistenceManager.getInstance().addOrUpdate(airtranReq);
						break;
					case PAYPERIOD: 
			     		PayPeriodFileRequest ppReq = new PayPeriodFileRequest();
			     		PayPeriodFile ppf = selectedFileHistoryQueue.getPayPeriodFile();
			     		if (ppf == null || ppf.getPipelineInformation() == null) return;
			     		ppf.getPipelineInformation().setRejected(true);
			     		ppReq.setEntity(ppf);
			     		ppReq.setId(ppf.getId());
			     		AdminPersistenceManager.getInstance().addOrUpdate(ppReq);
						break;
					case DEDUCTION: 
			     		DeductionFileRequest ddfReq = new DeductionFileRequest();
			     		DeductionFile df = selectedFileHistoryQueue.getDeductionFile();
			     		if (df == null || df.getPipelineInformation() == null) return;
			     		df.getPipelineInformation().setRejected(true);
			     		ddfReq.setEntity(df);
			     		ddfReq.setId(df.getId());
			     		AdminPersistenceManager.getInstance().addOrUpdate(ddfReq);
						break;
					case INSURANCE: 
			     		InsuranceFileRequest insfReq = new InsuranceFileRequest();
			     		InsuranceFile inf = selectedFileHistoryQueue.getInsuranceFile();
			     		if (inf == null || inf.getPipelineInformation() == null) return;
			     		inf.getPipelineInformation().setRejected(true);
			     		insfReq.setEntity(inf);
			     		insfReq.setId(inf.getId());
			     		AdminPersistenceManager.getInstance().addOrUpdate(insfReq);
						break;
					default:
						Utils.showAlert("Unsupported File Type", "The selected file type is not supported.");
						return;
				}	
		 		
				Utils.showAlert("Rejected", "The selected file has been rejected.");
				updateFileHistoryData();
			}else {
				Utils.showAlert("No Selection", "Please select file entry to reject");
				return;
			}
	     }catch (Exception e){
	    	 DataManager.i().log(Level.SEVERE, e);
	    	 Utils.showAlert("Rejection Failed", "The file could not be rejected.");
	     }
    }
    
	@FXML
	private void onShowSystemInfo() {
		try {
			// make sure we have a selection
			if(fileHistoryTableView.getSelectionModel().getSelectedIndex() < 0) return;
			selectedFileHistoryQueue = fileHistoryQueue.get(fileHistoryTableView.getSelectionModel().getSelectedItem().getQueueLocation());
			
			// set the coredata
	 		switch (selectedFileHistoryQueue.getFileType()) 
	 		{
				case COVERAGE: 
					DataManager.i().mCurrentCoreDataType = SystemDataType.COVERAGEFILE;
					DataManager.i().mCoreData = (CoreData) selectedFileHistoryQueue.getCoverageFile();
		    		break;
				case EMPLOYEE: 
					DataManager.i().mCurrentCoreDataType = SystemDataType.COVERAGEFILE;
					DataManager.i().mCoreData = (CoreData) selectedFileHistoryQueue.getEmployeeFile();
		    		break;
				case PAY: 
					DataManager.i().mCurrentCoreDataType = SystemDataType.COVERAGEFILE;
					DataManager.i().mCoreData = (CoreData) selectedFileHistoryQueue.getPayFile();
		    		break;
				case IRS1094C: 
					DataManager.i().mCurrentCoreDataType = SystemDataType.COVERAGEFILE;
					DataManager.i().mCoreData = (CoreData) selectedFileHistoryQueue.getIrs1094cFile();
					break;
				case IRS1095C: 
					DataManager.i().mCurrentCoreDataType = SystemDataType.COVERAGEFILE;
					DataManager.i().mCoreData = (CoreData) selectedFileHistoryQueue.getIrs1095cFile();
					break;
				case IRSAIRERR: 
					DataManager.i().mCurrentCoreDataType = SystemDataType.COVERAGEFILE;
					DataManager.i().mCoreData = (CoreData) selectedFileHistoryQueue.getAirTranErrorFile();
					break;
				case IRSAIRRCPT: 
					DataManager.i().mCurrentCoreDataType = SystemDataType.COVERAGEFILE;
					DataManager.i().mCoreData = (CoreData) selectedFileHistoryQueue.getAirTranReceiptFile();
					break;
				case PAYPERIOD: 
					DataManager.i().mCurrentCoreDataType = SystemDataType.COVERAGEFILE;
					DataManager.i().mCoreData = (CoreData) selectedFileHistoryQueue.getPayPeriodFile();
					break;
				case DEDUCTION: 
					DataManager.i().mCurrentCoreDataType = SystemDataType.COVERAGEFILE;
					DataManager.i().mCoreData = (CoreData) selectedFileHistoryQueue.getDeductionFile();
					break;
				case INSURANCE: 
					DataManager.i().mCurrentCoreDataType = SystemDataType.COVERAGEFILE;
					DataManager.i().mCoreData = (CoreData) selectedFileHistoryQueue.getInsuranceFile();
					break;
				default:
					Utils.showAlert("Unsupported File Type", "The selected file type is not supported.");
					return;
			}	
			
			
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
			Parent ControllerNode = loader.load();
	        ViewCoreSystemInfoController sysInfoController = (ViewCoreSystemInfoController) loader.getController();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        if (sysInfoController.changesMade == true) {
	        	showFileHistory();
	        }
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		
	}
		

    
    private void exitPopup()
    {
		Stage stage = (Stage) fileHistoryLabel.getScene().getWindow();
		stage.close();    	
    }
    
   	public class HBoxFileHistoryCell 
   	{
   		SimpleStringProperty user = new SimpleStringProperty();
   		SimpleStringProperty dateTime = new SimpleStringProperty();
   		SimpleStringProperty employer = new SimpleStringProperty();
   		SimpleStringProperty description = new SimpleStringProperty();
   		SimpleStringProperty status = new SimpleStringProperty();
   		SimpleStringProperty result = new SimpleStringProperty();
   		SimpleStringProperty fileType = new SimpleStringProperty();
   		SimpleStringProperty units = new SimpleStringProperty();
   		SimpleStringProperty records = new SimpleStringProperty();
   		SimpleStringProperty specId = new SimpleStringProperty();
   		SimpleStringProperty spec = new SimpleStringProperty();
   		SimpleStringProperty mapperId = new SimpleStringProperty();
   		
   		boolean rejected;
        int queueLocation = 0;

            TextField fieldState1 = new TextField();
            TextField fieldState2 = new TextField();
            TextField fieldState3 = new TextField();
            TextField fieldState4 = new TextField();
            TextField fieldState5 = new TextField();
            
            public String getUser() {
           	 return user.get();
            }
            
            public String getDateTime() {
           	 return dateTime.get();
            }
            
            public String getEmployer() {
           	 return employer.get();
            }
            
            public String getDescription() {
           	 return description.get();
            }
            
            public String getStatus() {
           	 return status.get();
            }
            
            public String getResult() {
           	 return result.get();
            }
            
            public String getFileType() {
           	 return fileType.get();
            }
            
            public String getUnits() {
           	 return units.get();
            }
            
            public String getRecords() {
           	 return records.get();
            }
            
            public String getSpecId() {
           	 return specId.get();
            }
            
            public String getSpec() {
           	 return spec.get();
            }
            
            public String getMapperId() {
           	 return mapperId.get();
            }
            
            public TextField getFieldState1() {
           	 return fieldState1;
            }
            
            public TextField getFieldState2() {
           	 return fieldState2;
            }
            
            public TextField getFieldState3() {
           	 return fieldState3;
            }
            
            public TextField getFieldState4() {
           	 return fieldState4;
            }
            
            public TextField getFieldState5() {
           	 return fieldState5;
            }
            
            public int getQueueLocation() {
           	 return queueLocation;
            }
            
            public boolean isRejected() {
            	return rejected;
            }
            
           String getDate() {return dateTime.get();}

            HBoxFileHistoryCell(int queueLocation, String user, 
           		 								String dateTime, 
           		 								String employer, 
           		 								String description, 
           		 								String units, 
           		 								String records,
           		 								String specId,
           		 								String spec,
           		 								String mapperId,
           		 								String status, 
           		 								String fileType, 
           		 								boolean state1, 
           		 								boolean state2, 
           		 								boolean state3, 
           		 								boolean state4, 
           		 								boolean state5, 
           		 								String result,
           		 								boolean rejected) {
                 super();

                 //save the requestId;
                 this.queueLocation = queueLocation;
                 this.rejected = rejected;
                 
                 if(user == null) user = "";
                 if(dateTime == null ) dateTime = "";
                 if(employer == null ) employer = "";
                 if(description == null ) description = "";
                 if(status == null ) status = "";
                 if(result == null) result = "";
                 if(fileType == null) fileType= "";
                 if(units == null) units = "";
                 if(records == null) records = "";
                 if(specId == null) specId = "";
                 if(spec == null) spec = "";
                 if(mapperId == null) mapperId = "";
                 
                 this.user.set(user);
                 this.dateTime.set(dateTime);
                 this.employer.set(employer);
                 this.description.set(description);
                 this.user.set(user);
                 this.units.set(units);
                 this.records.set(records);
                 this.specId.set(specId);
                 this.spec.set(spec);
                 this.mapperId.set(mapperId);
                 this.fileType.set("   " + fileType);
                 this.status.set(status);
                 this.result.set(result);
            }
       }	
}
