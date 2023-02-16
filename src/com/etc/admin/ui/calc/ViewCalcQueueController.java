package com.etc.admin.ui.calc;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.logging.Level;

import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.coresysteminfo.ViewCoreSystemInfoController;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.rqs.AccountRequest;
import com.etc.entities.CoreData;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class ViewCalcQueueController {
	// STATUS
	@FXML
	private LineChart<String,Number> pqueTimeChart;
	// REQUEST
	@FXML
	private CheckBox pqueAutoRefreshCheckbox;
	@FXML
	private TableView<HBoxCell> pqueFilesListView;
	// AUTOUPDATE
	@FXML
	private ProgressBar pqueUpdateCounter;
	@FXML
	private TextField pqueSearchFilter;
	@FXML
	private CheckBox pqueExpandedDateRange;
	
	// members for access. Getting everything up front due to complexity

	//table sort 
	TableColumn<HBoxCell, String> sortColumn = null;
	ContextMenu cmSystemData = null;

			
	//counters for the time graph
	int countT[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

	//counter for the auto update
	double countQueueUpdate = 0.0;
	boolean timerActive = true;
	
	//keep track of our connection
	boolean bConnected = true;
	Calendar firstDisconnect;
	
	// track selected item
	int queueSelection = -1;
	
	// our data object
	CalcQueueData calcData = new CalcQueueData();
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		updateQueueData();
	}
	
	private void initControls() {
		// CONTEXT MENU
		cmSystemData  = new ContextMenu();
		MenuItem mi1 = new MenuItem("Show System Data");
		mi1.setOnAction(new EventHandler<ActionEvent>() 
		{
	        @Override
	        public void handle(ActionEvent t)
	        {
	        	if (pqueFilesListView.getSelectionModel().getSelectedItems() != null) 
	        	{
	        		HBoxCell cell = pqueFilesListView.getSelectionModel().getSelectedItem();
	        		CalcQueue que = cell.getCalcQueue();
	        		try {
	        			// set the coredata
	        			DataManager.i().mCoreData = (CoreData) que.getRequest();
	        			DataManager.i().mCurrentCoreDataType = SystemDataType.CALCULATIONREQUEST;
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
	        	        	DataManager.i().updateEmployers();
	        	        	updateQueueData();
	        	        }
	        		} catch (IOException e) {
	                	DataManager.i().log(Level.SEVERE, e); 
	        		}        		
	        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	        	}
	        }
	    });
		cmSystemData.getItems().add(mi1);
		cmSystemData.setAutoHide(true);
		
		// set the table columns for our queue
		setTableColumns();
		//DataManager.i().updateEmployers();
		
		//add handlers for listbox selection notification
		pqueFilesListView.setOnMouseClicked(mouseClickedEvent -> {
			// launch if it is a double click
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
    			// get the selection
    			HBoxCell cellItem = pqueFilesListView.getSelectionModel().getSelectedItem();
    			if (cellItem == null) return;
    		
            	DataManager.i().mCalcQueue = cellItem.getCalcQueue();
            	switch(DataManager.i().mCalcQueue.getRequest().getSpecification().getChannelId().intValue()) {
            	case 1:
            		showIrs10945cDetail();
            		break;
            	case 2:
            		showIrs10945bDetail();
            		break;
            	case 6:
            		showEmployeeMergerDetail();
            		break;
            	case 7:
            		showIrs10945FilingDetail();
            		break;
            	default:
            		showCalculationRequestDetail();
            		break;
            	}
            }
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) {
            	queueSelection = pqueFilesListView.getSelectionModel().getSelectedIndex();
    			showCalculationQueue();
    			pqueFilesListView.sort();
            }
            
            if(mouseClickedEvent.getButton().equals(MouseButton.SECONDARY) && mouseClickedEvent.getClickCount() < 2) 
            {
            	if (pqueFilesListView.getSelectionModel().getSelectedItem() != null)
            		cmSystemData.show(pqueFilesListView, mouseClickedEvent.getScreenX(), mouseClickedEvent.getScreenY());
            }

        });	
		
	    // style the update counter
	    pqueUpdateCounter.setStyle("-fx-accent: SkyBlue");
	    pqueUpdateCounter.setProgress(1.0);
	    
	    //default to non-updating
	    pqueAutoRefreshCheckbox.setSelected(false);
	    
	  //create timing thread to service the queue updates
        Thread queueTimer = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                    	if (timerActive) {
		                	countQueueUpdate++;
		                	if (countQueueUpdate > 11) {
		                		countQueueUpdate = 0.0;
		                		pqueUpdateCounter.setProgress(1.0);
		                		updateQueueData();
		                	} else {
		                		pqueUpdateCounter.setProgress(1.0 - (countQueueUpdate * .083));
		                	}
                    	}
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    	DataManager.i().log(Level.SEVERE, e); 
                    }
            	    catch (Exception e) {  DataManager.i().logGenericException(e); }
               }
            }
        });	    
	    
        queueTimer.start();
	    pqueTimeChart.setAnimated(false);
	}
	
	private void setTableColumns() {
		//clear the default values
		pqueFilesListView.getColumns().clear();

	    TableColumn<HBoxCell, String> xUser = new TableColumn<HBoxCell, String>("User");
		xUser.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("user"));
		xUser.setMinWidth(100);
		xUser.setSortable(true);
		pqueFilesListView.getColumns().add(xUser);
		xUser.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getUser();
                        	setText(val);
                        	if (currentIndex == queueSelection)
                        		setStyle("-fx-background-color: #aaaaaa");
                        	else {
                            	int id =  param.getTableView().getItems().get(currentIndex).getType();
                            	setStyle(getColorString(id));
                        	} 
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxCell, String> xDate = new TableColumn<HBoxCell, String>("Date");
		xDate.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("dateTime"));
		xDate.setMinWidth(175);
		xDate.setSortable(true);
		xDate.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		xDate.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getDateTime();
                        	setText(val);
                        	if (currentIndex == queueSelection)
                        		setStyle("-fx-background-color: #aaaaaa");
                        	else {
                            	int id =  param.getTableView().getItems().get(currentIndex).getType();
                            	setStyle(getColorString(id));
                        	}
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		//initial sort is by date
		sortColumn = xDate;
		sortColumn.setSortType(SortType.DESCENDING);
		pqueFilesListView.getColumns().add(xDate);
		
		TableColumn<HBoxCell, String> xAccount = new TableColumn<HBoxCell, String>("Account");
		xAccount.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("account"));
		xAccount.setMinWidth(150);
		xAccount.setSortable(true);
		pqueFilesListView.getColumns().add(xAccount);
		xAccount.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getAccount();
                        	setText(val);
                        	if (currentIndex == queueSelection)
                        		setStyle("-fx-background-color: #aaaaaa");
                        	else {
                            	int id =  param.getTableView().getItems().get(currentIndex).getType();
                            	setStyle(getColorString(id));
                        	}
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxCell, String> xEmployer = new TableColumn<HBoxCell, String>("Employer");
		xEmployer.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("employer"));
		xEmployer.setMinWidth(250);
		xEmployer.setSortable(true);
		pqueFilesListView.getColumns().add(xEmployer);
		xEmployer.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getEmployer();
                        	setText(val);
                        	if (currentIndex == queueSelection)
                        		setStyle("-fx-background-color: #aaaaaa");
                        	else {
                            	int id =  param.getTableView().getItems().get(currentIndex).getType();
                            	setStyle(getColorString(id));
                        	}
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		/*
		TableColumn<HBoxCell, String> xTaxYear = new TableColumn<HBoxCell, String>("TaxYear");
		xTaxYear.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("taxYear"));
		xTaxYear.setMinWidth(100);
		xTaxYear.setSortable(true);
		pqueFilesListView.getColumns().add(xTaxYear);
		xTaxYear.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getTaxYear();
                        	setText(val);
                        	if (currentIndex == queueSelection)
                        		setStyle("-fx-background-color: #aaaaaa");
                        	else {
                            	int id =  param.getTableView().getItems().get(currentIndex).getType();
                            	setStyle(getColorString(id));
                        	}
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		*/
		TableColumn<HBoxCell, String> xStatus = new TableColumn<HBoxCell, String>("Status");
		xStatus.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("status"));
		xStatus.setMinWidth(105);
		xStatus.setSortable(true);
		pqueFilesListView.getColumns().add(xStatus);
		xStatus.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getStatus();
                        	setText(val);
                        	if (currentIndex == queueSelection)
                        		setStyle("-fx-background-color: #aaaaaa");
                        	else {
                            	int id =  param.getTableView().getItems().get(currentIndex).getType();
                            	setStyle(getColorString(id));
                        	}
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxCell, String> xRequestId = new TableColumn<HBoxCell, String>("RequestId");
		xRequestId.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("id"));
		xRequestId.setMinWidth(105);
		xRequestId.setSortable(true);
		pqueFilesListView.getColumns().add(xRequestId);
		xRequestId.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getId();
                        	setText(val);
                        	if (currentIndex == queueSelection)
                        		setStyle("-fx-background-color: #aaaaaa");
                        	else {
                            	int id =  param.getTableView().getItems().get(currentIndex).getType();
                            	setStyle(getColorString(id));
                        	}
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxCell, String> xName = new TableColumn<HBoxCell, String>("Name");
		xName.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("name"));
		xName.setMinWidth(250);
		pqueFilesListView.getColumns().add(xName);
		xName.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getName();
                        	setText(val);
                        	if (currentIndex == queueSelection)
                        		setStyle("-fx-background-color: #aaaaaa");
                        	else {
                            	int id =  param.getTableView().getItems().get(currentIndex).getType();
                            	setStyle(getColorString(id));
                        	}
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxCell, String> xDescription = new TableColumn<HBoxCell, String>("Description");
		xDescription.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("description"));
		xDescription.setMinWidth(300);
		pqueFilesListView.getColumns().add(xDescription);
		xDescription.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getDescription();
                        	setText(val);
                        	if (currentIndex == queueSelection)
                        		setStyle("-fx-background-color: #aaaaaa");
                        	else {
                            	int id =  param.getTableView().getItems().get(currentIndex).getType();
                            	setStyle(getColorString(id));
                        	}
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		//TableColumn<HBoxCell, String> xProcessOn = new TableColumn<HBoxCell, String>("Process On");
		//xProcessOn.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("processOn"));
		//xProcessOn.setMinWidth(250);
		//pqueFilesListView.getColumns().add(xProcessOn);
		
		//TableColumn<HBoxCell, String> xEmployer = new TableColumn<HBoxCell, String>("Completed On");
		//xEmployer.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("completedOn"));
		//xEmployer.setMinWidth(150);
		//pqueFilesListView.getColumns().add(xEmployer);
		
		TableColumn<HBoxCell, String> xSpecId = new TableColumn<HBoxCell, String>("SpecId");
		xSpecId.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("specId"));
		xSpecId.setMinWidth(60);
		pqueFilesListView.getColumns().add(xSpecId);
		xSpecId.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getSpecId();
                        	setText(val);
                        	if (currentIndex == queueSelection)
                        		setStyle("-fx-background-color: #aaaaaa");
                        	else {
                            	int id =  param.getTableView().getItems().get(currentIndex).getType();
                            	setStyle(getColorString(id));
                        	}
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxCell, String> xResult = new TableColumn<HBoxCell, String>("Result");
		xResult.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("result"));
		xResult.setMinWidth(700);
		pqueFilesListView.getColumns().add(xResult);
		xResult.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getResult();
                        	setText(val);
                        	if (currentIndex == queueSelection)
                        		setStyle("-fx-background-color: #aaaaaa");
                        	else {
                            	int id =  param.getTableView().getItems().get(currentIndex).getType();
                            	setStyle(getColorString(id));
                        	}
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
	}
	
	private String getColorString(int typeId) {
    	switch (typeId) {
		case 1:  // calling codes c
			return "-fx-background-color: #fec077";
		case 2: // calling codes b
			return "-fx-background-color: #fec077";
		case 6: // employee merger
			return "-fx-background-color: #bbeeff";
		case 7: // filing
			return "-fx-background-color: #ccffcc";
		case 9: // eligibility
			return "-fx-background-color: #ffffcc";
	}

    	return "-fx-background-color: #ffffff";
	}

	public void stopTimer() {
		timerActive = false;
	}
	
	public void startTimer() {
		if (pqueAutoRefreshCheckbox.isSelected())
			timerActive = true;
	}
	
	@SuppressWarnings("unchecked")
	private void updateQueueData() {
		// pause the queue timer while retrieving data
		stopTimer();
		// keep our current sort
       if (pqueFilesListView.getSortOrder().size()>0)
            sortColumn = (TableColumn<HBoxCell, String>) pqueFilesListView.getSortOrder().get(0);
       
		// create a thread to handle the update, letting the screen respond normally
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            	calcData.updateCalcRequests(pqueExpandedDateRange.isSelected());
                return null;
            }
        };
        
    	Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
    	    public void uncaughtException(Thread th, Throwable ex) {
    	        System.out.println("Uncaught exception: " + ex);
    			Utils.alertUser("Queue Exception", ex.getMessage());
    	    }
    	};
    	
      	task.setOnScheduled(e ->  {
      		EtcAdmin.i().setStatusMessage("Updating Calc Queue Data...");
      		EtcAdmin.i().setProgress(0.25);});
      			
    	task.setOnSucceeded(e ->  showCalculationQueue());
    	
    	task.setOnFailed(new EventHandler<WorkerStateEvent>() {
    	    @Override
    	    public void handle(WorkerStateEvent arg0) {
    	        Throwable throwable = task.getException(); 
    	        StringWriter sw = new StringWriter();
    	        throwable.printStackTrace(new PrintWriter(sw));
    	        String exceptionAsString = sw.toString();  
    	        
    	        Utils.alertUser("Queue Error - StackTrace below", exceptionAsString);
    			EtcAdmin.i().setStatusMessage("Queue Error: " + DataManager.i().mDebugMessage);
    			EtcAdmin.i().setProgress(0);
    	    }
    	});
    	
    	//task.setOnFailed(e ->  QueueError());
    	Thread queueRefresh = new Thread(task);
    	queueRefresh.setUncaughtExceptionHandler(h);
    	queueRefresh.start();
	}
	
	//update the PipelineRequests list
	private void showCalculationQueue() {
		//reset our refresh counter
		countQueueUpdate = 0.0;
    	pqueUpdateCounter.setProgress(1.0);
		
        //reset the graph counters
		resetCounters();
		
		pqueFilesListView.getItems().clear();
		//load the queue
		if (calcData.mCalcQueue != null && calcData.mCalcQueue.size() > 0)
		{
			for (CalcQueue cq : calcData.mCalcQueue) {
				//apply filter if present
				if (pqueSearchFilter.getText() != null && pqueSearchFilter.getText().isEmpty() == false) {
					String searchData = "";
					searchData +=  cq.getRequest().getDescription() + " " +
							cq.getRequest().getSpecification().getChannel().getName() + " " +
						  	   Utils.getDateTimeString(cq.getRequest().getLastUpdated()) + " " +
						  	   Utils.getDateTimeString(cq.getRequest().getProcessOn()) + " " +
						  	   Utils.getDateTimeString(cq.getRequest().getCompletedOn()) + " " +
						  	 cq.getRequest().getResult();
					searchData += cq.getRequest().getId().toString() + " ";
					if (cq.getRequest().getCalculationId() != null)
						searchData += cq.getRequest().getCalculationId().toString() + " ";
					if (cq.getRequest().getSpecification() != null) {
						searchData += cq.getRequest().getSpecification().getId().toString() + " ";
						if (cq.getRequest().getSpecification().getChannel() != null)
							searchData += cq.getRequest().getSpecification().getChannel().getName() + " ";
					}
					if (cq.getRequest().getStatus() != null)
						searchData += cq.getRequest().getStatus().toString() + " ";
					if (cq.getRequest().getCreatedBy() != null)
						searchData += cq.getRequest().getCreatedBy().getFirstName() +  " " +  
								cq.getRequest().getCreatedBy().getLastName() + " ";
					// account and employer
	            	switch(cq.getRequest().getSpecification().getChannelId().intValue()) {
	            	case 1:
            			if (cq.getIrs10945c() != null && cq.getIrs10945c().getCalculationInformation().getEmployer() != null) {
            				searchData += cq.getIrs10945c().getCalculationInformation().getEmployer().getName() + " ";
                			if (cq.getIrs10945c().getCalculationInformation().getEmployer().getAccount() != null)
                				searchData += cq.getIrs10945c().getCalculationInformation().getEmployer().getAccount().getName() + " ";
            			}
	            		break;
	            	case 2:
            			if (cq.getIrs10945b() != null && cq.getIrs10945b().getCalculationInformation() != null && cq.getIrs10945b().getCalculationInformation().getEmployer() != null) {
            				searchData += cq.getIrs10945b().getCalculationInformation().getEmployer().getName() + " ";
                			if (cq.getIrs10945b().getCalculationInformation().getEmployer().getAccount() != null)
                				searchData += cq.getIrs10945b().getCalculationInformation().getEmployer().getAccount().getName() + " ";
            			}
	            		break;
	            	case 6:
            			if (cq.getEmployeeMerger() != null && cq.getEmployeeMerger().getEmployee1().getEmployer() != null) {
            				searchData += cq.getEmployeeMerger().getEmployee1().getEmployer().getName() + " ";
                			if (cq.getEmployeeMerger().getEmployee1().getEmployer().getAccount() != null)
                				searchData += cq.getEmployeeMerger().getEmployee1().getEmployer().getAccount().getName() + " ";
            			}
	            		break;
	            	case 7:
            			if (cq.getIrs10945Filing() != null && cq.getIrs10945Filing().getCalculationInformation().getEmployer() != null) {
            				searchData += cq.getIrs10945Filing().getCalculationInformation().getEmployer().getName() + " ";
                			if (cq.getIrs10945Filing().getCalculationInformation().getEmployer().getAccount() != null)
                				searchData += cq.getIrs10945Filing().getCalculationInformation().getEmployer().getAccount().getName() + " ";
            			}
	            		break;
	            	default:
	            		break;
	            	}
	            	
					if (searchData.toLowerCase().contains(pqueSearchFilter.getText().toLowerCase()) == false) continue;
					// passes, add it
					pqueFilesListView.getItems().add(new HBoxCell(cq));
				} else
					pqueFilesListView.getItems().add(new HBoxCell(cq));
				// update the counters for this entry
				Calendar queueDate = (Calendar)cq.getRequest().getLastUpdated();
				updateTimeGraphCounters(queueDate);
			}	
			
			// sort
			if (sortColumn!=null) {
				//pqueFilesListView.getSortOrder().clear();
				pqueFilesListView.getSortOrder().add(sortColumn);
	            sortColumn.setSortable(true);
	        }

		//	if (queueSelection > -1)
		//		pqueFilesListView.getSelectionModel().clearAndSelect(queueSelection);
		}

		// update the chart
		updateTimeBarChart();

		EtcAdmin.i().setStatusMessage("Ready");
		EtcAdmin.i().setProgress(0);
		
		//reenable the queue timer
		startTimer();
	}	
	
	private void  showCalculationRequestDetail() {
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/calc/ViewCalcQueueDetail.fxml"));
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
	
	private void  showEmployeeMergerDetail() {
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/calc/ViewCalcEmployeeMergerDetail.fxml"));
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
	
	private void  showIrs10945FilingDetail() {
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/calc/ViewCalcIrsFilingDetail.fxml"));
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
	
	private void  showIrs10945bDetail() {
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/calc/ViewCalcIrs10945bDetail.fxml"));
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
	
	private void  showIrs10945cDetail() {
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/calc/ViewCalcIrs10945cDetail.fxml"));
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
	private void onFilterKey() {
		queueSelection = -1;
		pqueFilesListView.getSelectionModel().clearSelection();
		showCalculationQueue();
	}
	
	class HBoxCellComparator implements Comparator<HBoxCell> {
		  @Override
		  public int compare(HBoxCell cell1, HBoxCell cell2) {
		      return cell2.getDateTime().compareTo(cell1.getDateTime());
		  }
		}
	
	private void resetCounters() {
		//hours line graph
		Arrays.fill(countT,0);
	}

	private void updateTimeBarChart() {
		// TIME CHART
        XYChart.Series<String,Number> dataTimeSeries = new XYChart.Series<>();
        dataTimeSeries.setName("Time");
        
        dataTimeSeries.getData().clear();
        dataTimeSeries.getData().add(new XYChart.Data<>("Hour 1", countT[0]));
        dataTimeSeries.getData().add(new XYChart.Data<>("2", countT[1]));
        dataTimeSeries.getData().add(new XYChart.Data<>("3", countT[2]));
        dataTimeSeries.getData().add(new XYChart.Data<>("4", countT[3]));
        dataTimeSeries.getData().add(new XYChart.Data<>("5", countT[4]));
        dataTimeSeries.getData().add(new XYChart.Data<>("6", countT[5]));
        dataTimeSeries.getData().add(new XYChart.Data<>("7", countT[6]));
        dataTimeSeries.getData().add(new XYChart.Data<>("8", countT[7]));
        dataTimeSeries.getData().add(new XYChart.Data<>("9", countT[8]));
        dataTimeSeries.getData().add(new XYChart.Data<>("10", countT[9]));
        dataTimeSeries.getData().add(new XYChart.Data<>("11", countT[10]));
        dataTimeSeries.getData().add(new XYChart.Data<>("12", countT[11]));
        dataTimeSeries.getData().add(new XYChart.Data<>("13", countT[12]));
        dataTimeSeries.getData().add(new XYChart.Data<>("14", countT[13]));
        dataTimeSeries.getData().add(new XYChart.Data<>("15", countT[14]));
        dataTimeSeries.getData().add(new XYChart.Data<>("16", countT[15]));
        dataTimeSeries.getData().add(new XYChart.Data<>("17", countT[16]));
        dataTimeSeries.getData().add(new XYChart.Data<>("18", countT[17]));
        dataTimeSeries.getData().add(new XYChart.Data<>("19", countT[18]));
        dataTimeSeries.getData().add(new XYChart.Data<>("20", countT[19]));
        dataTimeSeries.getData().add(new XYChart.Data<>("21", countT[20]));
        dataTimeSeries.getData().add(new XYChart.Data<>("22", countT[21]));
        dataTimeSeries.getData().add(new XYChart.Data<>("23", countT[22]));
        dataTimeSeries.getData().add(new XYChart.Data<>("24", countT[23]));
       

        pqueTimeChart.getData().clear();
        pqueTimeChart.getData().add(dataTimeSeries);
        Color color = Color.BLUE; // or any other color
        String rgb = String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

        
        //Node fill = dataTimeSeries.getNode().lookup(".chart-series-area-fill"); // only for AreaChart
        dataTimeSeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
		pqueTimeChart.setTitle("Processed per Hour (last 24 hrs)");
		
	}
	
	private void updateTimeGraphCounters(Calendar queueDate){
		//update the time graph counters based on request lastUpdate
		Calendar currentDate = Calendar.getInstance();
		long timeDifference = currentDate.getTimeInMillis() - queueDate.getTimeInMillis();
		int seconds = (int) (timeDifference/1000l);
		int timeSlot = seconds / (3600);
		switch (timeSlot) {
			case 0:
				countT[0]++;
				break;
			case 1:
				countT[1]++;
				break;
			case 2:
				countT[2]++;
				break;
			case 3:
				countT[3]++;
				break;
			case 4:
				countT[4]++;
				break;
			case 5:
				countT[5]++;
				break;
			case 6:
				countT[6]++;
				break;
			case 7:
				countT[7]++;
				break;
			case 8:
				countT[8]++;
				break;
			case 9:
				countT[9]++;
				break;
			case 10:
				countT[10]++;
				break;
			case 11:
				countT[11]++;
				break;
			case 12:
				countT[12]++;
				break;
			case 13:
				countT[13]++;
				break;
			case 14:
				countT[14]++;
				break;
			case 15:
				countT[15]++;
				break;
			case 16:
				countT[16]++;
				break;
			case 17:
				countT[17]++;
				break;
			case 18:
				countT[18]++;
				break;
			case 19:
				countT[19]++;
				break;
			case 20:
				countT[20]++;
				break;
			case 21:
				countT[21]++;
				break;
			case 22:
				countT[22]++;
				break;
			case 23:
				countT[23]++;
				break;
		}
	}
	
	//extending the listview for our additional controls
	public class HBoxCell {
		SimpleStringProperty user = new SimpleStringProperty();
		SimpleStringProperty id = new SimpleStringProperty();
		SimpleStringProperty dateTime = new SimpleStringProperty();
		SimpleStringProperty account = new SimpleStringProperty();
		SimpleStringProperty employer = new SimpleStringProperty();
		SimpleStringProperty taxYear = new SimpleStringProperty();
		SimpleStringProperty calculationId = new SimpleStringProperty();
		SimpleStringProperty name = new SimpleStringProperty();
		SimpleStringProperty description = new SimpleStringProperty();
		SimpleStringProperty status = new SimpleStringProperty();
		SimpleStringProperty processOn = new SimpleStringProperty();
		SimpleStringProperty completedOn = new SimpleStringProperty();
		SimpleStringProperty specId = new SimpleStringProperty();
		SimpleStringProperty result = new SimpleStringProperty();
		
		int type = 0;

		CalcQueue calcQueue = null;
		
		public void clear() {
			user.set("");
			id.set("");
			dateTime.set("");
			account.set("");
			employer.set("");
			taxYear.set("");
			calculationId.set("");
			name.set("");
			description.set("");
			status.set("");
			processOn.set("");
			completedOn.set("");
			specId.set("");
			result.set("");
		}
		
		public CalcQueue getCalcQueue() {
			return calcQueue;
		}
		
        public String getId() {
        	 return id.get();
         }
         
        public String getAccount() {
       	 return account.get();
        }
        
        public String getEmployer() {
       	 return employer.get();
        }
        
        public String getTaxYear() {
       	 return taxYear.get();
        }
        
         public String getDateTime() {
        	 return dateTime.get();
         }
         
         public String getUser() {
        	 return user.get();
         }
         
         public String getCalculationId() {
        	 return calculationId.get();
         }
         
         public String getName() {
        	 return name.get();
         }
         
         public String getDescription() {
        	 return description.get();
         }
         
         public String getStatus() {
        	 return status.get();
         }
         
         public String getProcessOn() {
        	 return processOn.get();
         }
         
         public String getCompletedOn() {
        	 return completedOn.get();
         }
         
         public String getSpecId() {
        	 return specId.get();
         }
         
         public String getResult() {
        	 return result.get();
         }
         
         public int getType() {
        	 return type;
         }
         
         HBoxCell(CalcQueue cq) {
        	 super();
        	 try {
	        	 //save the requestId;
	             calcQueue = cq;
	
	             if (cq.getRequest() != null) {
	 				if (cq.getRequest().getCreatedBy() != null)
						user.set(cq.getRequest().getCreatedBy().getFirstName() + " " + cq.getRequest().getCreatedBy().getLastName());
					id.set(cq.getRequest().getId().toString());
					dateTime.set(Utils.getDateTimeString(cq.getRequest().getLastUpdated()));
					if (cq.getRequest().getCalculationId() != null)
						calculationId.set(cq.getRequest().getCalculationId().toString());
					description.set(cq.getRequest().getDescription());
					if (cq.getRequest().getStatus() != null)
						status.set(cq.getRequest().getStatus().toString());
					processOn.set(Utils.getDateTimeString(cq.getRequest().getProcessOn()));
					completedOn.set(Utils.getDateTimeString(cq.getRequest().getCompletedOn()));
					if (cq.getRequest().getSpecification() != null)
						specId.set(cq.getRequest().getSpecification().getId().toString());
					result.set(cq.getRequest().getResult());
					// channel name
					if (cq.getRequest().getSpecification() != null && cq.getRequest().getSpecification().getChannel() != null) {
						name.set(cq.getRequest().getSpecification().getChannel().getName());
						type = cq.getRequest().getSpecification().getChannel().getId().intValue();
						// calc specific data
		            	switch(cq.getRequest().getSpecification().getChannelId().intValue()) {
		            	case 1: // Irs10945c
		            		if (cq.getIrs10945c().getTaxYear() != null)
		            			taxYear.set(String.valueOf(cq.getIrs10945c().getTaxYear().getYear()));
	            			// check to see if the employer is still lagging. If so, go get it.
		            		if (DataManager.i().mEmployersList != null && DataManager.i().mEmployersList.size() > 0 && cq.getIrs10945c().getCalculationInformation().getEmployer() == null && cq.getIrs10945c().getTaxYear().getEmployerId() != null) {
		            			for (Employer ee : DataManager.i().mEmployersList) {
	            					if (ee.getId().equals(cq.getIrs10945c().getTaxYear().getEmployerId())){
	            						cq.getIrs10945c().getCalculationInformation().setEmployer(ee);
	            						break;
	            					}
	            				}
	            			} 
		            		// now use it
	            			if (cq.getIrs10945c().getCalculationInformation().getEmployer() != null) {
		            			employer.set(cq.getIrs10945c().getCalculationInformation().getEmployer().getName());
		            			if (cq.getIrs10945c().getCalculationInformation().getEmployer().getAccount() != null)
		            				account.set(cq.getIrs10945c().getCalculationInformation().getEmployer().getAccount().getName());
		            			else {  // go retrieve the account
		            				AccountRequest aReq = new AccountRequest();
		            				aReq.setId(cq.getIrs10945c().getCalculationInformation().getEmployer().getAccountId());
		            				cq.getIrs10945c().getCalculationInformation().getEmployer().setAccount(AdminPersistenceManager.getInstance().get(aReq));
		            				if (cq.getIrs10945c().getCalculationInformation() != null &&
			            					cq.getIrs10945c().getCalculationInformation().getEmployer() != null &&
			            					cq.getIrs10945c().getCalculationInformation().getEmployer().getAccount() != null &&
			            					cq.getIrs10945c().getCalculationInformation().getEmployer().getAccount() != null &&
			            					cq.getIrs10945c().getCalculationInformation().getEmployer().getAccount().getName() != null)
			            					account.set(cq.getIrs10945c().getCalculationInformation().getEmployer().getAccount().getName());
		            			}
		            		}
		            		break;
		            	case 2: // Irs10945b
	            			// check to see if the employer is still lagging. If so, go get it.
		            		if (DataManager.i().mEmployersList != null && DataManager.i().mEmployersList.size() > 0 && cq.getIrs10945b().getCalculationInformation().getEmployer() == null && cq.getIrs10945b().getTaxYear().getEmployerId() != null) {
	            				for (Employer ee : DataManager.i().mEmployersList) {
	            					if (ee.getId().equals(cq.getIrs10945b().getTaxYear().getEmployerId())){
	            						cq.getIrs10945b().getCalculationInformation().setEmployer(ee);
	            						break;
	            					}
	            				}
	            			}
		            		if (cq.getIrs10945b().getCalculationInformation().getEmployer() != null) {
		            			employer.set(cq.getIrs10945b().getCalculationInformation().getEmployer().getName());
		            			if (cq.getIrs10945b().getCalculationInformation().getEmployer().getAccount() != null)
		            				account.set(cq.getIrs10945b().getCalculationInformation().getEmployer().getAccount().getName());
		            			else {  // go retrieve the account
		            				AccountRequest aReq = new AccountRequest();
		            				aReq.setId(cq.getIrs10945b().getCalculationInformation().getEmployer().getAccountId());
		            				cq.getIrs10945b().getCalculationInformation().getEmployer().setAccount(AdminPersistenceManager.getInstance().get(aReq));
		            				account.set(cq.getIrs10945b().getCalculationInformation().getEmployer().getAccount().getName());
		            			}
		            		}
		            		if (cq.getIrs10945b().getTaxYear() != null)
		            			taxYear.set(String.valueOf(cq.getIrs10945b().getTaxYear().getYear()));
			            	
		            		break;
		            	case 6: // EmployeeMerger
	            			// check to see if the employer is still lagging. If so, go get it.
		            		if (DataManager.i().mEmployersList != null && DataManager.i().mEmployersList.size() > 0 && cq.getEmployeeMerger().getEmployee1().getEmployer() == null && cq.getEmployeeMerger().getEmployee1().getEmployerId() != null) {
	            				for (Employer ee : DataManager.i().mEmployersList) {
	            					if (ee.getId().equals(cq.getEmployeeMerger().getEmployee1().getEmployerId())){
	            						cq.getEmployeeMerger().getEmployee1().setEmployer(ee);
	            						break;
	            					}
	            				}
	            			}
		            		if (cq.getEmployeeMerger().getEmployee1().getEmployer() != null) {
		            			employer.set(cq.getEmployeeMerger().getEmployee1().getEmployer().getName());
		            			if (cq.getEmployeeMerger().getEmployee1().getEmployer().getAccount() != null)
		            				account.set(cq.getEmployeeMerger().getEmployee1().getEmployer().getAccount().getName());
		            			else {  // go retrieve the account
		            				AccountRequest aReq = new AccountRequest();
		            				aReq.setId(cq.getEmployeeMerger().getCalculationInformation().getEmployer().getAccountId());
		            				cq.getEmployeeMerger().getCalculationInformation().getEmployer().setAccount(AdminPersistenceManager.getInstance().get(aReq));
		            				account.set(cq.getEmployeeMerger().getCalculationInformation().getEmployer().getAccount().getName());
		            			}
			         		}
		            		break;
		            	case 7: // IRs10945Filing
	            			// check to see if the employer is still lagging. If so, go get it.
		            		if (DataManager.i().mEmployersList != null && DataManager.i().mEmployersList.size() > 0 && cq.getIrs10945Filing().getCalculationInformation().getEmployer() == null && cq.getIrs10945Filing().getTaxYear().getEmployerId() != null) {
	            				for (Employer ee : DataManager.i().mEmployersList) {
	            					if (ee.getId().equals(cq.getIrs10945Filing().getTaxYear().getEmployerId())){
	            						cq.getIrs10945Filing().getCalculationInformation().setEmployer(ee);
	            						break;
	            					}
	            				}
	            			}
		            		if (cq.getIrs10945Filing().getCalculationInformation().getEmployer() != null) {
		            			employer.set(cq.getIrs10945Filing().getCalculationInformation().getEmployer().getName());
		            			if (cq.getIrs10945Filing().getCalculationInformation().getEmployer().getAccount() != null)
		            				account.set(cq.getIrs10945Filing().getCalculationInformation().getEmployer().getAccount().getName());
		            			else {  // go retrieve the account
		            				AccountRequest aReq = new AccountRequest();
		            				aReq.setId(cq.getIrs10945Filing().getCalculationInformation().getEmployer().getAccountId());
		            				cq.getIrs10945Filing().getCalculationInformation().getEmployer().setAccount(AdminPersistenceManager.getInstance().get(aReq));
		            				account.set(cq.getIrs10945Filing().getCalculationInformation().getEmployer().getAccount().getName());
		            			}
			            	}
		            		if (cq.getIrs10945Filing().getTaxYear() != null)
		            			taxYear.set(String.valueOf(cq.getIrs10945Filing().getTaxYear().getYear()));
		            		break;
		            	default:
		            		break;
		            	}
					}
	             }
     		}catch (Exception e) { 
    			DataManager.i().log(Level.SEVERE, e); 
    		}

         }
    }	
	
	@FXML
	private void applyFilter(ActionEvent event) {
		showCalculationQueue();
	}
	
	@FXML
	private void clearFilter(ActionEvent event) {
		// clear the filter
		pqueSearchFilter.setText("");
		// and refresh
		showCalculationQueue();
	}
	
	@FXML
	private void onRefresh(ActionEvent event){
		updateQueueData();
	}
	
	@FXML
	private void onExpandedQueue(ActionEvent event){
		queueSelection = -1;
		pqueFilesListView.getSelectionModel().clearSelection();
		updateQueueData();
	}
	
	@FXML
	private void onQueueKeystroke(){
		queueSelection = pqueFilesListView.getSelectionModel().getSelectedIndex();
		showCalculationQueue();
	}
	
	@FXML
	private void onAutorefresh(ActionEvent event) {
		if (pqueAutoRefreshCheckbox.isSelected()) {
			timerActive = true;
			pqueUpdateCounter.setProgress(1.0);
		}else {
			timerActive = false;
        	countQueueUpdate = 0.0;
        	pqueUpdateCounter.setProgress(0.0);
		}
	}
}
