package com.etc.admin.ui.pipeline.queue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.AdminApp;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.embeds.PipelineInformation;
import com.etc.corvetto.ems.pipeline.entities.PipelineChannel;
import com.etc.corvetto.ems.pipeline.entities.PipelineRequest;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;
import com.etc.corvetto.ems.pipeline.entities.cov.CoverageFile;
import com.etc.corvetto.ems.pipeline.entities.emp.EmployeeFile;
import com.etc.corvetto.ems.pipeline.entities.pay.PayFile;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;
import com.etc.utils.types.RequestStatusType;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ViewPipelineQueueController {
	// UPLOAD
	@FXML
	private Button pqueUploadBrowseButton;
	// STATUS
	@FXML
	private PieChart pqueTypeChart;
	@FXML
	private BarChart<String,Number> pqueProcessChart;
	@FXML
	private LineChart<String,Number> pqueTimeChart;
	@FXML
	private Button pqueRejectButton;
	@FXML
	private Button pqueViewDetailButton;
	@FXML
	private Button pqueViewMapperButton;
	// REQUEST
	@FXML
	private ComboBox<String> pqueQueueRange;
	@FXML
	private CheckBox pqueAutoRefreshCheckbox;
	@FXML
	private TextField pqueFilesListHeader;
	@FXML
	private Label pqueFilesListLabel;
	@FXML
	private TableView<HBoxCell> pqueFilesListView;
	// AUTOUPDATE
	@FXML
	private ProgressBar pqueUpdateCounter;
	@FXML
	private TextField pqueSearchFilter;
	//@FXML
	//private ComboBox<String> pqueTimeZone;
	@FXML
	private ComboBox<String> pqueFileType;
	
	// members for access. Getting everything up front due to complexity
	QueueData queueData = new QueueData();
	PipelineRequest selPLRequest = null;
	PipelineSpecification selPLSpec = null;
	PipelineChannel selPLChannel = null;
	PipelineInformation selPLInfo = null;
	EmployeeFile employeeFile = null;
	PayFile payFile = null;
	CoverageFile coverageFile = null;

	//table sort 
	TableColumn<HBoxCell, String> sortColumn = null;
			
	//counters for the time graph
	int countT[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	
	//counter for the auto update
	double countQueueUpdate = 0.0;
	boolean timerActive = true;
	
	//keep track of our connection
	boolean bConnected = true;
	Calendar firstDisconnect;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		try {
			initControls();
			//resyncQueueData();
			updateQueueData();
		}catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		}
	}
	
	private String getCellStyle(HBoxCell cell) {
		
    	switch (cell.getStatus()) {
    	case "COMPLETED":
    		return "-fx-text-fill: #007700";
    	case "FAILED":
    		return "-fx-text-fill: #770000";
    	case "PROCESSING":
    		// check for  a stuck file
    		if (cell.getPipelineQueue() != null && cell.getPipelineQueue().getRequest() != null ) {
    			Long seconds = (Calendar.getInstance().getTimeInMillis() - cell.getPipelineQueue().getRequest().getLastUpdated().getTimeInMillis()) / 1000;
    			// over one hour - Houston, we have a problem
    			if (seconds > 3600)
    				return "-fx-text-fill: #ff0000";
    		}
    		return "-fx-text-fill: #555555";
    	case "REPROCESSED":
    		return "-fx-text-fill: #770077";
    	default:
    		return "";
    	}
    	
	}
	
	private void initControls() {
		// set the table columns for our queue
		setTableColumns();
		pqueRejectButton.setDisable(true);
		
		//add handlers for listbox selection notification
		pqueFilesListView.setOnMouseClicked(mouseClickedEvent -> {
			try {
				// get the selection
				HBoxCell cellItem = pqueFilesListView.getSelectionModel().getSelectedItem();
				if (cellItem == null) return;
				 queueData.mPipelineQueueEntry = queueData.mPipelineQueue.get(cellItem.queueLocation);
				 DataManager.i().mPipelineQueueEntry = queueData.mPipelineQueueEntry;
			
				 // set the mapper button
				pqueViewDetailButton.setDisable(false);
				if (queueData.mPipelineQueueEntry.getFileType() == PipelineFileType.EMPLOYEE || 
					queueData.mPipelineQueueEntry.getFileType() == PipelineFileType.COVERAGE ||
					queueData.mPipelineQueueEntry.getFileType() == PipelineFileType.PAY)
					pqueViewMapperButton.setDisable(false);
				else
					pqueViewMapperButton.setDisable(true);
	
				//enable the reject button if this entry is processing
				pqueRejectButton.setDisable(true);
				if (queueData.mPipelineQueueEntry.getRequest().getStatus() == RequestStatusType.PROCESSING)
					 pqueRejectButton.setDisable(false); 
	
				// launch if it is a double click
	            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
					
	            	int selColumn = pqueFilesListView.getSelectionModel().getSelectedCells().get(0).getColumn();
	            	
	            	//Utils.showAlert("Column", "Selection:" + selColumn);
	            	//if (selColumn != 99) return;
	            	switch (selColumn) {
	            	case 6: // account
	            		viewSelectedAccount();
	            		return;
	            	case 7: // employer
	            		viewSelectedEmployer();
	            		return;
	            	case 8: // spec
	            		viewSelectedSpec();
	            		return;
	            	case 9: // spec
	            		viewSelectedMapper();
	            		return;
	            	case 16: // fileType - tbd
	            		switch(queueData.mPipelineQueueEntry.getFileType()) {
	            		case PAYPERIOD: // employer
	            			break;
	            		default:
	            			break;
	            		}
	            		break;
	            	
	            	}
	            	
	            	// check to see if this was a reproces. If so, alert the user on the reprocessed record and quit
	            	for (HBoxCell cell : pqueFilesListView.getItems())
	            	{
	            		if (cell.getFileId().equals(cellItem.getFileId()) && Integer.valueOf(cell.getRequestId()) > Integer.valueOf(cellItem.getRequestId())) {
	            			Utils.alertUser("Reprocessed Request", "This is a reprocessed request with no error data. Please see Id:" + cell.getRequestId());
	            			return;
	            		}
	            	}
	
					// check for zero records  - dance around possible null Long values
	            	int records = 0;
	            	int units = 0;
	            	if (queueData.mPipelineQueueEntry != null) {
	            		if (queueData.mPipelineQueueEntry.getRecords() != null)
	            			records = queueData.mPipelineQueueEntry.getRecords().intValue();
	            		if (queueData.mPipelineQueueEntry.getUnits() != null)
	            			units = queueData.mPipelineQueueEntry.getUnits().intValue();
	            	}
	            	
	            	if (queueData.mPipelineQueueEntry == null || (records == 0 && units == 0)) {
	        			Utils.alertUser("No Records", "Sorry, there are no records/units for this queue entry to view.");
	        			return;
	            	}
	            
	            	// refresh if coming from the queue
	            	EtcAdmin.i().setScreenRefresh(true);
	            	// and load the raw field screen
	            	EtcAdmin.i().setScreen(ScreenType.PIPELINERAWFIELDGRID, true);
	            }
            } catch (Exception e) {
            	DataManager.i().log(Level.SEVERE, e); 
            }
        });	
		
		//set the ranges for the queue
		String ranges[] = { "24", "48", "72", "96", "120", "144", "168" }; 
		pqueQueueRange.setItems(FXCollections.observableArrayList(ranges));
		pqueQueueRange.getSelectionModel().select("96");

		String fileTypes[] = { "ALL ","COVERAGE ","EMPLOYEE ","IRS1094C ","IRS1095C ","IRSAIRERR ","IRSAIRRCPT ","PAY ","PAYPERIOD "}; 
		pqueFileType.setItems(FXCollections.observableArrayList(fileTypes));
		pqueFileType.getSelectionModel().select(0);
		
	    // style the update counter
	    pqueUpdateCounter.setStyle("-fx-accent: SkyBlue");
	    pqueUpdateCounter.setProgress(1.0);
	    
	    //disable details until selected
	    pqueViewDetailButton.setDisable(true);
	    pqueViewMapperButton.setDisable(true);

	    //disable upload for now
	    pqueUploadBrowseButton.setDisable(true);
	    
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
	    TableColumn<HBoxCell, String> x1 = new TableColumn<HBoxCell, String>("User");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("user"));
		x1.setMinWidth(100);
		pqueFilesListView.getColumns().add(x1);
		x1.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getUser();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex)));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
	    TableColumn<HBoxCell, String> xId = new TableColumn<HBoxCell, String>("Id");
	    xId.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("requestId"));
	    xId.setMinWidth(75);
		pqueFilesListView.getColumns().add(xId);
		xId.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getRequestId();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex)));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
	    TableColumn<HBoxCell, String> xFileId = new TableColumn<HBoxCell, String>("FileId");
	    xFileId.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("fileId"));
	    xFileId.setMinWidth(75);
	    xFileId.setComparator((String o1, String o2) -> { return Utils.compareNumberStrings(o1, o2); });
		pqueFilesListView.getColumns().add(xFileId);
		xFileId.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getFileId();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex)));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxCell, String> x2 = new TableColumn<HBoxCell, String>("Date");
		x2.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("dateTime"));
		x2.setMinWidth(140);
		//initial sort is by date
		x2.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		sortColumn = x2;
		sortColumn.setSortType(SortType.DESCENDING);
		pqueFilesListView.getColumns().add(x2);
		x2.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getDateTime();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex)));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		/*
	    TableColumn<PayCell, String> x4 = new TableColumn<PayCell, String>("End Date");
		x4.setCellValueFactory(new PropertyValueFactory<PayCell,String>("endDate"));
		x4.setMinWidth(110);
		x4.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		empPayTableView.getColumns().add(x4);
		setCellFactory(x4);
		*/

		
		TableColumn<HBoxCell, String> xStatus = new TableColumn<HBoxCell, String>("Status");
		xStatus.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("status"));
		xStatus.setMinWidth(120);
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
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex)));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxCell, String> x3 = new TableColumn<HBoxCell, String>("Description");
		x3.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("description"));
		x3.setMinWidth(400);
		pqueFilesListView.getColumns().add(x3);
		x3.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getDescription();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex)));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxCell, String> xAccount = new TableColumn<HBoxCell, String>("Account");
		xAccount.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("account"));
		xAccount.setMinWidth(250);
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
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex)));
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
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex)));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
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
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex)));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxCell, String> xDynFileSpecId = new TableColumn<HBoxCell, String>("MapId");
		xDynFileSpecId.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("dynFileSpecId"));
		xDynFileSpecId.setMinWidth(60);
		pqueFilesListView.getColumns().add(xDynFileSpecId);
		xDynFileSpecId.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getDynFileSpecId();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex)));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
//		TableColumn<HBoxCell, String> xArchive = new TableColumn<HBoxCell, String>("Archived");
//		xArchive.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("archived"));
//		xArchive.setMinWidth(60);
//		pqueFilesListView.getColumns().add(xArchive);
		
		TableColumn<HBoxCell, String> x5 = new TableColumn<HBoxCell, String>("Rcrds");
		x5.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("records"));
		x5.setMinWidth(60);
	    x5.setComparator((String o1, String o2) -> {
            return Integer.compare(Integer.valueOf(o1), Integer.valueOf(o2));
        });
		pqueFilesListView.getColumns().add(x5);
		x5.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getRecords();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex)));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxCell, TextField> S1 = new TableColumn<HBoxCell, TextField>("I");
		S1.setMaxWidth(20);
		S1.setCellValueFactory(new PropertyValueFactory<HBoxCell,TextField>("fieldState1"));
		S1.setSortable(false);
		pqueFilesListView.getColumns().add(S1);
		
		TableColumn<HBoxCell, TextField> S2 = new TableColumn<HBoxCell, TextField>("P");
		S2.setMaxWidth(20);
		S2.setCellValueFactory(new PropertyValueFactory<HBoxCell,TextField>("fieldState2"));
		S2.setSortable(false);
		pqueFilesListView.getColumns().add(S2);
		
		TableColumn<HBoxCell, TextField> S3 = new TableColumn<HBoxCell, TextField>("V");
		S3.setMaxWidth(20);
		S3.setCellValueFactory(new PropertyValueFactory<HBoxCell,TextField>("fieldState3"));
		S3.setSortable(false);
		pqueFilesListView.getColumns().add(S3);
		
		TableColumn<HBoxCell, TextField> S4 = new TableColumn<HBoxCell, TextField>("C");
		S4.setMaxWidth(20);
		S4.setCellValueFactory(new PropertyValueFactory<HBoxCell,TextField>("fieldState4"));
		S4.setSortable(false);
		pqueFilesListView.getColumns().add(S4);
		
		TableColumn<HBoxCell, TextField> S5 = new TableColumn<HBoxCell, TextField>("F");
		S5.setMaxWidth(20);
		S5.setCellValueFactory(new PropertyValueFactory<HBoxCell,TextField>("fieldState5"));
		S5.setSortable(false);
		pqueFilesListView.getColumns().add(S5);
		
		TableColumn<HBoxCell, String> x6 = new TableColumn<HBoxCell, String>("File Type");
		x6.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("fileType"));
		x6.setMinWidth(105);
		pqueFilesListView.getColumns().add(x6);
		x6.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getFileType();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex)));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxCell, String> x7 = new TableColumn<HBoxCell, String>("Result");
		x7.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("result"));
		x7.setMinWidth(800);
		pqueFilesListView.getColumns().add(x7);
		x7.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getResult();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex)));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
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
    	try {
			// pause the queue timer while retrieving data
			stopTimer();
			// keep our current sort
	       if (pqueFilesListView.getSortOrder().size()>0)
	            sortColumn = (TableColumn<HBoxCell, String>) pqueFilesListView.getSortOrder().get(0);
	
	       int dateCompletedRange = 10000;
			if (pqueQueueRange.getValue().contains("ALL") == false)
				dateCompletedRange = (Integer.valueOf(pqueQueueRange.getValue()) / 24);
			final int date1 = dateCompletedRange;
			
			// create a thread to handle the update, letting the screen respond normally
			Task<Void> task = new Task<Void>() {
	            @Override
	            protected Void call() throws Exception {
	            	setConnected(queueData.refreshPipelineRequests(date1));
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
	      		EtcAdmin.i().setStatusMessage("Updating Queue Data...");
	      		EtcAdmin.i().setProgress(0.25);});
	      			
	    	task.setOnSucceeded(e ->  loadPipelineRequests());
	    	
	    	task.setOnFailed(new EventHandler<WorkerStateEvent>() {
	    	    @Override
	    	    public void handle(WorkerStateEvent arg0) {
	    	        Exception e =new Exception(task.getException());
	            	DataManager.i().log(Level.SEVERE, e); 
	    	    }
	    	});
	    	
	    	Thread queueRefresh = new Thread(task);
	    	queueRefresh.setUncaughtExceptionHandler(h);
    		AdminApp.getInstance().getFxQueue().put(queueRefresh);
		}catch(InterruptedException e)
		{
			EtcAdmin.i().setProgress(0);
			DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
    	//queueRefresh.start();
	}
	
	@SuppressWarnings("unchecked")
	private void resyncQueueData() {
		// pause the queue timer while retrieving data
		stopTimer();
		// keep our current sort
       if (pqueFilesListView.getSortOrder().size()>0)
            sortColumn = (TableColumn<HBoxCell, String>) pqueFilesListView.getSortOrder().get(0);

		// create a thread to handle the update, letting the screen respond normally
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            	queueData.refreshPipelineRequests(168);
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
      		EtcAdmin.i().setStatusMessage("Updating Queue Data...");
      		EtcAdmin.i().setProgress(0.25);});
      			
    	task.setOnSucceeded(e ->  loadPipelineRequests());
    	
    	task.setOnFailed(new EventHandler<WorkerStateEvent>() {
    	    @Override
    	    public void handle(WorkerStateEvent arg0) {
    	        Throwable throwable = task.getException(); 
    	        StringWriter sw = new StringWriter();
    	        throwable.printStackTrace(new PrintWriter(sw));
    	        String exceptionAsString = sw.toString();  
    	        
    	        //throwable.printStackTrace();
    	        Utils.alertUser("Queue Error - StackTrace below", exceptionAsString);
    			EtcAdmin.i().setStatusMessage("Resync Error: " + DataManager.i().mDebugMessage);
    			EtcAdmin.i().setProgress(0);
    	    }
    	});
    	
    	//task.setOnFailed(e ->  QueueError());
    	Thread queueRefresh = new Thread(task);
    	queueRefresh.setUncaughtExceptionHandler(h);
    	queueRefresh.start();
	}
	
	private void setConnected (boolean connected) {
		// store the time if we are transitioning from true to false
		if (bConnected == true && connected == false)
			firstDisconnect = Calendar.getInstance();
		bConnected = connected;
		if (bConnected == false)
			//go ahead and rebuld the ident, etc.
			DataManager.i().reconnectCorvetto();
	}
	
	//update the PipelineRequests list
	private void loadPipelineRequests() {
		//warn the user if we are not connected and bail
		if (bConnected == false) {
			Calendar cal = Calendar.getInstance();
      		EtcAdmin.i().setStatusMessage("WARNING: Connection Error. Started at " + Utils.getTimeString24(firstDisconnect) + ". Last Attempt at " +  Utils.getTimeString24(cal) + ". Will Retry Next Refresh...");
      		EtcAdmin.i().setProgress(0.0);
      		return;
		}
		//reset our refresh counter
		countQueueUpdate = 0.0;
    	pqueUpdateCounter.setProgress(1.0);
		
		boolean state1 = false;
		boolean state2 = false;
		boolean state3 = false;
		boolean state4 = false;
		boolean state5 = false;
		
		//reset the graph counters
		resetCounters();
		//load the queue
		if (queueData.mPipelineQueue != null)
		{
		    List<HBoxCell> requestList = new ArrayList<>();
			for (int i = 0; i < queueData.mPipelineQueue.size(); i++) {// PipelineQueue queue : DataManager.i().mPipelineQueue) {
				PipelineQueue queue = queueData.mPipelineQueue.get(i);
				if (queue == null) continue;
				if (queue.getRequest() == null) continue;
				if (queue.getRequest().getEmployer() == null) continue;
				queue.setUnits(0l);
				queue.setRecords(0l);
				// get the file id
				String fileId = "";
				boolean rejected = false;
				switch(queue.getFileType()) {
				case COVERAGE: 
					if (queue.getCoverageFile() != null) {
						fileId = queue.getCoverageFile().getId().toString();
						if (queue.getCoverageFile().getPipelineInformation() != null)
							rejected = queue.getCoverageFile().getPipelineInformation().isRejected();
					}
					break;
				case EMPLOYEE: 
					if (queue.getEmployeeFile() != null) {
						fileId = queue.getEmployeeFile().getId().toString();
						if (queue.getEmployeeFile().getPipelineInformation() != null)
							rejected = queue.getEmployeeFile().getPipelineInformation().isRejected();
					}
					break;
				case IRS1094C: 
					if (queue.getIrs1094cFile() != null) {
						fileId = queue.getIrs1094cFile().getId().toString();
						if (queue.getIrs1094cFile().getPipelineInformation() != null)
							rejected = queue.getIrs1094cFile().getPipelineInformation().isRejected();
					}
					break;
				case IRS1095C: 
					if (queue.getIrs1095cFile() != null) {
						fileId = queue.getIrs1095cFile().getId().toString();
						if (queue.getIrs1095cFile().getPipelineInformation() != null)
							rejected = queue.getIrs1095cFile().getPipelineInformation().isRejected();
					}
					break;
				case IRSAIRERR: 
					if (queue.getAirTranErrorFile() != null) {
						fileId = queue.getAirTranErrorFile().getId().toString();
						if (queue.getAirTranErrorFile().getPipelineInformation() != null)
							rejected = queue.getAirTranErrorFile().getPipelineInformation().isRejected();
					}
					break;
				case IRSAIRRCPT: 
					if (queue.getAirTranReceiptFile() != null) {
						fileId = queue.getAirTranReceiptFile().getId().toString();
						if (queue.getAirTranReceiptFile().getPipelineInformation() != null)
							rejected = queue.getAirTranReceiptFile().getPipelineInformation().isRejected();
					}
					break;
				case PAY: 
					if (queue.getPayFile() != null) {
						fileId = queue.getPayFile().getId().toString();
						if (queue.getPayFile().getPipelineInformation() != null)
							rejected = queue.getPayFile().getPipelineInformation().isRejected();
					}
					break;
				case PAYPERIOD: 
					if (queue.getPayPeriodFile() != null && queue.getPayPeriodFile().getPipelineInformation() != null) {
						fileId = queue.getPayPeriodFile().getId().toString();
						if (queue.getPayPeriodFile().getPipelineInformation() != null)
							rejected = queue.getPayPeriodFile().getPipelineInformation().isRejected();
					}
					break;
				case DEDUCTION: 
					if (queue.getDeductionFile() != null && queue.getDeductionFile().getPipelineInformation() != null) {
						fileId = queue.getDeductionFile().getId().toString();
						if (queue.getDeductionFile().getPipelineInformation() != null)
							rejected = queue.getDeductionFile().getPipelineInformation().isRejected();
					}
					break;
				case INSURANCE: 
					if (queue.getInsuranceFile() != null && queue.getInsuranceFile().getPipelineInformation() != null) {
						fileId = queue.getInsuranceFile().getId().toString();
						if (queue.getInsuranceFile().getPipelineInformation() != null)
							rejected = queue.getInsuranceFile().getPipelineInformation().isRejected();
					}
					break;
				default:
					break;
				}
				
				String status = String.valueOf(queue.getRequest().getStatus());
				if (rejected == true) status = "REJECTED";
				//apply filter if present
				if (pqueSearchFilter.getText() != null && pqueSearchFilter.getText().isEmpty() == false) {
					String searchData = queue.getRequest().getDescription().toLowerCase();
					if (queue.getRequest().getCreatedBy() != null) {
						if (queue.getRequest().getCreatedBy().getFirstName() != null);
							searchData += queue.getRequest().getCreatedBy().getFirstName() +  " ";
					if (queue.getRequest().getCreatedBy().getLastName() != null)
						searchData += queue.getRequest().getCreatedBy().getLastName().substring(0,1) + " ";
					String mapperId = "";
					if (queue.getRequest().getSpecification() != null && queue.getRequest().getSpecification().getDynamicFileSpecificationId() != null)
						mapperId = queue.getRequest().getSpecification().getDynamicFileSpecificationId().toString();
					searchData += queue.getRequest().getEmployer().getAccount().getName() + " " +
								  queue.getRequest().getEmployer().getName() + " " +
								  queue.getRequest().getId().toString() + " " +
								  queue.getRequest().getSpecification().getId().toString() + " " + 
								  String.valueOf(queue.getFileType()) + " " +
								  mapperId + //queue.getRequest().getSpecification().getDynamicFileSpecificationId().toString() + " " +
								  status + //String.valueOf(queue.getRequest().getStatus()) + 
								  queue.getRequest().getResult() + "" + fileId;
					}
					boolean pass = true;
					String str = "";
					//tokenize on the space
					String[] tokens = pqueSearchFilter.getText().toLowerCase().split(" ");
					for (String toke : tokens) {
						str = toke;
						if (toke.startsWith("+")) {
							str = toke.substring(1);
							if (str.isEmpty() == false)
								if (searchData.toLowerCase().contains(str) == false) pass = false;
						} else if (toke.startsWith("-")) {
							str = toke.substring(1);
							if (str.isEmpty() == false)
								if (searchData.toLowerCase().contains(str) == true) pass = false;
						}else 
							if (str.isEmpty() == false)
								if (searchData.toLowerCase().contains(str) == false) pass = false;
					}
					if (pass == false) continue;
				}
				//Long fileSize = 0l;
				switch(queue.getFileType()) {
				case COVERAGE: 
					if (queue.getCoverageFile() != null) {
						if (queue.getCoverageFile().getPipelineInformation() == null) continue;
						state1 = queue.getCoverageFile().getPipelineInformation().isInitialized();
						state2 = queue.getCoverageFile().getPipelineInformation().isParsed();
						state3 = queue.getCoverageFile().getPipelineInformation().isValidated();
						state4 = queue.getCoverageFile().getPipelineInformation().isCompleted();
						state5 = queue.getCoverageFile().getPipelineInformation().isFinalized();
						queue.setUnits(queue.getCoverageFile().getPipelineInformation().getUnits());
						queue.setRecords(queue.getCoverageFile().getPipelineInformation().getRecords());
					}
					break;
				case EMPLOYEE: 
					if (queue.getEmployeeFile() != null) {
						if (queue.getEmployeeFile().getPipelineInformation() == null) continue;
						state1 = queue.getEmployeeFile().getPipelineInformation().isInitialized();
						state2 = queue.getEmployeeFile().getPipelineInformation().isParsed();
						state3 = queue.getEmployeeFile().getPipelineInformation().isValidated();
						state4 = queue.getEmployeeFile().getPipelineInformation().isCompleted();
						state5 = queue.getEmployeeFile().getPipelineInformation().isFinalized();
						queue.setUnits(queue.getEmployeeFile().getPipelineInformation().getUnits());
						queue.setRecords(queue.getEmployeeFile().getPipelineInformation().getRecords());
					}
					break;
				case IRS1094C: 
					if (queue.getIrs1094cFile() != null) {
						if (queue.getIrs1094cFile().getPipelineInformation() == null) continue;
						state1 = queue.getIrs1094cFile().getPipelineInformation().isInitialized();
						state2 = queue.getIrs1094cFile().getPipelineInformation().isParsed();
						state3 = queue.getIrs1094cFile().getPipelineInformation().isValidated();
						state4 = queue.getIrs1094cFile().getPipelineInformation().isCompleted();
						state5 = queue.getIrs1094cFile().getPipelineInformation().isFinalized();
						queue.setUnits(queue.getIrs1094cFile().getPipelineInformation().getUnits());
						queue.setRecords(queue.getIrs1094cFile().getPipelineInformation().getRecords());
					}
					break;
				case IRS1095C: 
					if (queue.getIrs1095cFile() != null) {
						if (queue.getIrs1095cFile().getPipelineInformation() == null) continue;
						state1 = queue.getIrs1095cFile().getPipelineInformation().isInitialized();
						state2 = queue.getIrs1095cFile().getPipelineInformation().isParsed();
						state3 = queue.getIrs1095cFile().getPipelineInformation().isValidated();
						state4 = queue.getIrs1095cFile().getPipelineInformation().isCompleted();
						state5 = queue.getIrs1095cFile().getPipelineInformation().isFinalized();
						queue.setUnits(queue.getIrs1095cFile().getPipelineInformation().getUnits());
						queue.setRecords(queue.getIrs1095cFile().getPipelineInformation().getRecords());
					}
					break;
				case EVENT: 
					break;
				case IRSAIRERR: 
					if (queue.getAirTranErrorFile() != null) {
						if (queue.getAirTranErrorFile().getPipelineInformation() == null) continue;
						state1 = queue.getAirTranErrorFile().getPipelineInformation().isInitialized();
						state2 = queue.getAirTranErrorFile().getPipelineInformation().isParsed();
						state3 = queue.getAirTranErrorFile().getPipelineInformation().isValidated();
						state4 = queue.getAirTranErrorFile().getPipelineInformation().isCompleted();
						state5 = queue.getAirTranErrorFile().getPipelineInformation().isFinalized();
						queue.setUnits(queue.getAirTranErrorFile().getPipelineInformation().getUnits());
						queue.setRecords(queue.getAirTranErrorFile().getPipelineInformation().getRecords());
					}
					break;
				case IRSAIRRCPT: 
					if (queue.getAirTranReceiptFile() != null) {
						if (queue.getAirTranReceiptFile().getPipelineInformation() == null) continue;
						state1 = queue.getAirTranReceiptFile().getPipelineInformation().isInitialized();
						state2 = queue.getAirTranReceiptFile().getPipelineInformation().isParsed();
						state3 = queue.getAirTranReceiptFile().getPipelineInformation().isValidated();
						state4 = queue.getAirTranReceiptFile().getPipelineInformation().isCompleted();
						state5 = queue.getAirTranReceiptFile().getPipelineInformation().isFinalized();
						queue.setUnits(queue.getAirTranReceiptFile().getPipelineInformation().getUnits());
						queue.setRecords(queue.getAirTranReceiptFile().getPipelineInformation().getRecords());
					}
					break;
				case PAY: 
					if (queue.getPayFile() != null) {
						if (queue.getPayFile().getPipelineInformation() == null) continue;
						state1 = queue.getPayFile().getPipelineInformation().isInitialized();
						state2 = queue.getPayFile().getPipelineInformation().isParsed();
						state3 = queue.getPayFile().getPipelineInformation().isValidated();
						state4 = queue.getPayFile().getPipelineInformation().isCompleted();
						state5 = queue.getPayFile().getPipelineInformation().isFinalized();
						queue.setUnits(queue.getPayFile().getPipelineInformation().getUnits());
						queue.setRecords(queue.getPayFile().getPipelineInformation().getRecords());
					}
					break;
				case PAYPERIOD: 
					if (queue.getPayPeriodFile() != null && queue.getPayPeriodFile().getPipelineInformation() != null) {
						if (queue.getPayPeriodFile().getPipelineInformation() == null) continue;
						state1 = queue.getPayPeriodFile().getPipelineInformation().isInitialized();
						state2 = queue.getPayPeriodFile().getPipelineInformation().isParsed();
						state3 = queue.getPayPeriodFile().getPipelineInformation().isValidated();
						state4 = queue.getPayPeriodFile().getPipelineInformation().isCompleted();
						state5 = queue.getPayPeriodFile().getPipelineInformation().isFinalized();
						queue.setUnits(queue.getPayPeriodFile().getPipelineInformation().getUnits());
						queue.setRecords(queue.getPayPeriodFile().getPipelineInformation().getRecords());
					}
					break;
				case DEDUCTION: 
					if (queue.getDeductionFile() != null) {
						if (queue.getDeductionFile().getPipelineInformation() == null) continue;
						state1 = queue.getDeductionFile().getPipelineInformation().isInitialized();
						state2 = queue.getDeductionFile().getPipelineInformation().isParsed();
						state3 = queue.getDeductionFile().getPipelineInformation().isValidated();
						state4 = queue.getDeductionFile().getPipelineInformation().isCompleted();
						state5 = queue.getDeductionFile().getPipelineInformation().isFinalized();
						queue.setUnits(queue.getDeductionFile().getPipelineInformation().getUnits());
						queue.setRecords(queue.getDeductionFile().getPipelineInformation().getRecords());
					}
					break;
				case INSURANCE: 
					if (queue.getInsuranceFile() != null) {
						if (queue.getInsuranceFile().getPipelineInformation() == null) continue;
						state1 = queue.getInsuranceFile().getPipelineInformation().isInitialized();
						state2 = queue.getInsuranceFile().getPipelineInformation().isParsed();
						state3 = queue.getInsuranceFile().getPipelineInformation().isValidated();
						state4 = queue.getInsuranceFile().getPipelineInformation().isCompleted();
						state5 = queue.getInsuranceFile().getPipelineInformation().isFinalized();
						queue.setUnits(queue.getInsuranceFile().getPipelineInformation().getUnits());
						queue.setRecords(queue.getInsuranceFile().getPipelineInformation().getRecords());
					}
					break;
/*				case PLAN: 
					if (queue.getPlanFile() != null) {
						fileId = queue.getPlanFile().getId().toString();
						if (queue.getPlanFile().getPipelineInformation() == null) continue;
						state1 = queue.getPlanFile().getPipelineInformation().isInitialized();
						state2 = queue.getPlanFile().getPipelineInformation().isParsed();
						state3 = queue.getPlanFile().getPipelineInformation().isValidated();
						state4 = queue.getPlanFile().getPipelineInformation().isCompleted();
						state5 = queue.getPlanFile().getPipelineInformation().isFinalized();
						queue.setUnits(queue.getPlanFile().getPipelineInformation().getUnits());
						queue.setRecords(queue.getPlanFile().getPipelineInformation().getRecords());
						//if (queue.getPlanFile().getPipelineInformation().getDocument() != null)
						//	fileSize = queue.getPlanFile().getPipelineInformation().getDocument().getSizeInBytes();
					}
					break;
*/				default:
					break;
				}

				Calendar queueDate = (Calendar)queue.getRequest().getLastUpdated().clone();

				//only list the file type selected
				if (pqueFileType.getValue() != null) {
					String filterType = pqueFileType.getValue().substring(0,pqueFileType.getValue().indexOf(" "));
					if (filterType.equals("ALL") == false)
						if (queue.getFileType().toString().equals(filterType) == false)
								continue;
				}
				
				String user = "";
				if (queue.getRequest().getCreatedBy() != null && queue.getRequest().getCreatedBy().getFirstName() != null && queue.getRequest().getCreatedBy().getLastName() != null) {
					user = queue.getRequest().getCreatedBy().getFirstName() + " " + queue.getRequest().getCreatedBy().getLastName().substring(0,1) + ".";
				} else
					user = "Unknown";
				String mapperId = "";
				if (queue.getRequest().getSpecification().getDynamicFileSpecificationId() != null)
					mapperId = queue.getRequest().getSpecification().getDynamicFileSpecificationId().toString();
				requestList.add(new HBoxCell(queue, i, queue.getRequest().getId().toString(), fileId, user,
										  Utils.getDateTimeString24(queueDate), 
										  queue.getRequest().getDescription(), 
										  queue.getRequest().getEmployer().getAccount().getName(),
										  queue.getRequest().getEmployer().getName(),
										  queue.getRequest().getSpecification().getId().toString(),
										  mapperId,
										  String.valueOf(queue.getRecords()),
										  status, //queue.getRequest().getStatus().toString(),
										  queue.getFileType().toString(),
										  state1, state2, state3, state4, state5,
										  queue.getRequest().getResult()));
				// update the counters for this entry
				updateTimeGraphCounters(queue, queueDate);
			};	
			
			ObservableList<HBoxCell> myObservableList = FXCollections.observableList(requestList);
			pqueFilesListView.setItems(myObservableList);	
			// sort
			if (sortColumn!=null) {
	    	   pqueFilesListView.getSortOrder().add(sortColumn);
	            sortColumn.setSortable(true);
	        }
		}

		// update the status for any reprocessed items 
        setReprocessedStatus();

		// update the chart
		updateTimeBarChart();
		//update our status
		EtcAdmin.i().setStatusMessage("Ready");
		EtcAdmin.i().setProgress(0);
		//reenable the queue timer
		startTimer();
	}	
	
	private void setReprocessedStatus() {
	   	for (HBoxCell checkCell : pqueFilesListView.getItems()) {
	   		if (checkCell.getStatus().contains("FAIL") == false) continue;
	   		for (HBoxCell cell : pqueFilesListView.getItems()) {
	   			if (cell.getFileId().equals(checkCell.getFileId()) && Integer.valueOf(cell.getRequestId()) > Integer.valueOf(checkCell.getRequestId())) {
	   				checkCell.status.set("REPROCESSED");
	   			}
    		}
    	}
	}
	
	class HBoxCellComparator implements Comparator<HBoxCell> {
		  @Override
		  public int compare(HBoxCell cell1, HBoxCell cell2) {
		      return cell2.getDate().compareTo(cell1.getDate());
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
		pqueTimeChart.setTitle("Processed per Hour (last 24 hrs)");
	}
	
	private void updateTimeGraphCounters(PipelineQueue queue, Calendar queueDate){
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
		SimpleStringProperty requestId = new SimpleStringProperty();
		SimpleStringProperty user = new SimpleStringProperty();
		SimpleStringProperty dateTime = new SimpleStringProperty();
		SimpleStringProperty description = new SimpleStringProperty();
		SimpleStringProperty account = new SimpleStringProperty();
		SimpleStringProperty employer = new SimpleStringProperty();
		SimpleStringProperty specId = new SimpleStringProperty();
		SimpleStringProperty dynFileSpecId = new SimpleStringProperty();
		SimpleStringProperty fileId = new SimpleStringProperty();
		SimpleStringProperty status = new SimpleStringProperty();
		SimpleStringProperty result = new SimpleStringProperty();
		SimpleStringProperty fileType = new SimpleStringProperty();
		SimpleStringProperty records = new SimpleStringProperty();
		SimpleStringProperty archived = new SimpleStringProperty();
         int queueLocation = 0;

         TextField fieldState1 = new TextField();
         TextField fieldState2 = new TextField();
         TextField fieldState3 = new TextField();
         TextField fieldState4 = new TextField();
         TextField fieldState5 = new TextField();
         
         PipelineQueue queueEntry;
         
         public PipelineQueue getPipelineQueue(){
        	 return queueEntry;
         }
         
         public String getRequestId() {
        	 return requestId.get();
         }
         
         public String getUser() {
        	 return user.get();
         }
         
         public String getDateTime() {
        	 return dateTime.get();
         }
         
         public String getDescription() {
        	 return description.get();
         }
         
         public String getAccount() {
        	 return account.get();
         }
         
         public String getEmployer() {
        	 return employer.get();
         }
         
         public String getSpecId() {
        	 return specId.get();
         }
         
         public String getFileId() {
        	 return fileId.get();
         }
         
         public String getDynFileSpecId() {
        	 return dynFileSpecId.get();
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
         
         public String getArchived() {
        	 return archived.get();
         }
         
         public String getRecords() {
        	 return records.get();
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
         
        String getDate() {return dateTime.get();}

         HBoxCell(PipelineQueue pq, int queueLocation, String requestId, String fileId, String user, String dateTime, String description, String account, String employer, String specId, String dynFileSpecId, String records, String status, String fileType, boolean state1, boolean state2, boolean state3, boolean state4, boolean state5, String result) {
              super();

              //save the requestId;
              this.queueLocation = queueLocation;
              
              if (requestId == null) requestId = "";
              if (fileId == null) fileId = "";
              if (user == null) user = "";
              if (dateTime == null ) dateTime = "";
              if (description == null ) description = "";
              if (account == null) account = "";
              if (employer == null) employer = "";
              if (specId == null) specId = "";
              if (dynFileSpecId == null) dynFileSpecId = "";
              if (status == null ) status = "";
              if (result == null) result = "";
              if (fileType == null) fileType= "";
              if (records == null) records = "";
              
              this.queueEntry = pq;
              this.requestId.set(requestId);
              this.fileId.set(fileId);
              this.user.set(user);
              this.dateTime.set(dateTime);
              this.description.set(description);
              this.account.set(account);
              this.employer.set(employer);
              this.specId.set(specId);
              this.dynFileSpecId.set(dynFileSpecId);
              this.user.set(user);
              this.records.set(records);
              this.fileType.set("   " + fileType);
              this.status.set(status);
              this.result.set(result);
              
              if (pq.getFileType() != null)
      				switch(pq.getFileType()) {
    				case COVERAGE: 
    					if (pq.getCoverageFile() != null) {
    						if (pq.getCoverageFile().getPipelineInformation() != null)
    							if (pq.getCoverageFile().getPipelineInformation().isArchived() == true)
    								archived.set("Yes");
    							else
    								archived.set("");
    					}
    					break;
    				case EMPLOYEE: 
    					if (pq.getEmployeeFile() != null) {
    						if (pq.getEmployeeFile().getPipelineInformation() != null)
    							if (pq.getEmployeeFile().getPipelineInformation().isArchived() == true)
    								archived.set("Yes");
    							else
    								archived.set("");
    					}
    					break;
    				case IRS1094C: 
    					if (pq.getIrs1094cFile() != null) {
    						if (pq.getIrs1094cFile().getPipelineInformation() != null)
    							if (pq.getIrs1094cFile().getPipelineInformation().isArchived() == true)
    								archived.set("Yes");
    							else
    								archived.set("");
    					}
    					break;
    				case IRS1095C: 
    					if (pq.getIrs1095cFile() != null) {
    						if (pq.getIrs1095cFile().getPipelineInformation() != null)
    							if (pq.getIrs1095cFile().getPipelineInformation().isArchived() == true)
    								archived.set("Yes");
    							else
    								archived.set("");
    					}
    					break;
    				case IRSAIRERR: 
    					if (pq.getAirTranErrorFile() != null) {
    						if (pq.getAirTranErrorFile().getPipelineInformation() != null)
    							if (pq.getAirTranErrorFile().getPipelineInformation().isArchived() == true)
    								archived.set("Yes");
    							else
    								archived.set("");
    					}
    					break;
    				case IRSAIRRCPT: 
    					if (pq.getAirTranReceiptFile() != null) {
    						if (pq.getAirTranReceiptFile().getPipelineInformation() != null)
    							if (pq.getAirTranReceiptFile().getPipelineInformation().isArchived() == true)
    								archived.set("Yes");
    							else
    								archived.set("");
    					}
    					break;
    				case PAY: 
    					if (pq.getPayFile() != null) {
    						if (pq.getPayFile().getPipelineInformation() != null)
    							if (pq.getPayFile().getPipelineInformation().isArchived() == true)
    								archived.set("Yes");
    							else
    								archived.set("");
    					}
    					break;
    				case PAYPERIOD: 
    					if (pq.getPayPeriodFile() != null) {
    						if (pq.getPayPeriodFile().getPipelineInformation() != null)
    							if (pq.getPayPeriodFile().getPipelineInformation().isArchived() == true)
    								archived.set("Yes");
    							else
    								archived.set("");
    					}
    					break;
    				case DEDUCTION: 
    					if (pq.getDeductionFile() != null) {
    						if (pq.getDeductionFile().getPipelineInformation() != null)
    							if (pq.getDeductionFile().getPipelineInformation().isArchived() == true)
    								archived.set("Yes");
    							else
    								archived.set("");
    					}
    					break;
    				case INSURANCE: 
    					if (pq.getInsuranceFile() != null) {
    						if (pq.getInsuranceFile().getPipelineInformation() != null)
    							if (pq.getInsuranceFile().getPipelineInformation().isArchived() == true)
    								archived.set("Yes");
    							else
    								archived.set("");
    					}
    					break;
    				default:
    					break;
    				}

              setFieldStateAttributes(fieldState1, state1);
              setFieldStateAttributes(fieldState2, state2);
              setFieldStateAttributes(fieldState3, state3);
              setFieldStateAttributes(fieldState4, state4);
              setFieldStateAttributes(fieldState5, state5);
         }
    }	
	
	public class HBoxRequestCell extends HBox {
		PipelineRequest request;
        Label lblCol1 = new Label();
        Label lblCol2 = new Label();
        Label lblCol3 = new Label();
        Label lblCol4 = new Label();

        HBoxRequestCell(PipelineRequest req, boolean isHeader) {
             super();

             request = req;
             
             lblCol1.setMinWidth(100);
             HBox.setHgrow(lblCol1, Priority.ALWAYS);

             lblCol2.setMinWidth(100);
             HBox.setHgrow(lblCol2, Priority.ALWAYS);

             lblCol3.setMinWidth(400);
             HBox.setHgrow(lblCol3, Priority.ALWAYS);

             lblCol4.setMinWidth(350);
             HBox.setHgrow(lblCol4, Priority.ALWAYS);


             if (isHeader == true) {
           	  lblCol1.setFont(Font.font(null, FontWeight.BOLD, 13));
           	  lblCol2.setFont(Font.font(null, FontWeight.BOLD, 13));
           	  lblCol3.setFont(Font.font(null, FontWeight.BOLD, 13));
           	  lblCol4.setFont(Font.font(null, FontWeight.BOLD, 13));
           	  
           	  lblCol1.setText("Id");
           	  lblCol2.setText("Last Update");
           	  lblCol3.setText("Description");
           	  lblCol4.setText("Result");
             }else {
            	 if (request != null && request.getId() != null)
            		 lblCol1.setText(request.getId().toString());
            	 else
            		 lblCol1.setText("");
           	  lblCol2.setText(Utils.getDateString(request.getLastUpdated()));
           	  lblCol3.setText(request.getDescription());
           	  lblCol4.setText(request.getResult());
             }
             
             //only using two columns for now
             this.getChildren().addAll(lblCol1, lblCol2, lblCol3, lblCol4);  
       }
        
        PipelineRequest getRequest() { 
       	 return request;
        }
   }	

	
	private void setFieldStateAttributes(TextField field, boolean green) {
        field.setMinWidth(15);
        field.setMaxWidth(15);
        field.setMinHeight(10);
        field.setPrefHeight(10);
        field.setMaxHeight(10);
        
        if (green == true)
        	field.setStyle("-fx-background-color: green");
        else
        	field.setStyle("-fx-background-color: red");
	}
	
	private void viewSelectedMapper(){
		// set the request and refresh the screen
		 HBoxCell cellItem = pqueFilesListView.getSelectionModel().getSelectedItem();
		 DataManager.i().setPipelineQueueEntry(cellItem.queueLocation);
		 // set the data needed for the mapper
		 DataManager.i().mPipelineSpecification = DataManager.i().mPipelineQueueEntry.getRequest().getSpecification();
		 DataManager.i().mPipelineChannel = DataManager.i().mPipelineQueueEntry.getRequest().getSpecification().getChannel();
		 //load the mapper screen
		 switch(DataManager.i().mPipelineQueueEntry.getFileType()) {
		 	case COVERAGE:
				DataManager.i().setScreenType(ScreenType.MAPPERCOVERAGEFROMQUEUE);
				EtcAdmin.i().setScreen(ScreenType.MAPPERCOVERAGEFROMQUEUE, true);
			 break;
		 	case EMPLOYEE:
				DataManager.i().setScreenType(ScreenType.MAPPEREMPLOYEEFROMQUEUE);
				EtcAdmin.i().setScreen(ScreenType.MAPPEREMPLOYEEFROMQUEUE, true);
			 break;
		 	case PAY:
				DataManager.i().setScreenType(ScreenType.MAPPERPAYFROMQUEUE);
				EtcAdmin.i().setScreen(ScreenType.MAPPERPAYFROMQUEUE, true);
			 break;
		 	case DEDUCTION:
				DataManager.i().setScreenType(ScreenType.MAPPERPAYFROMQUEUE);
				EtcAdmin.i().setScreen(ScreenType.MAPPERPAYFROMQUEUE, true);
			 break;
		 	case INSURANCE:
				DataManager.i().setScreenType(ScreenType.MAPPERPAYFROMQUEUE);
				EtcAdmin.i().setScreen(ScreenType.MAPPERPAYFROMQUEUE, true);
			 break;
			default:
				DataManager.i().setScreenType(ScreenType.PIPELINEDYNAMICFILESPECIFICATIONFROMQUEUE);
				EtcAdmin.i().setScreen(ScreenType.PIPELINEDYNAMICFILESPECIFICATIONFROMQUEUE, true);
			break;
		 }
	}
	
	private void viewSelectedSpec(){
		 HBoxCell cellItem = pqueFilesListView.getSelectionModel().getSelectedItem();
		 DataManager.i().setPipelineQueueEntry(cellItem.queueLocation);
		 DataManager.i().mPipelineSpecification = DataManager.i().mPipelineQueueEntry.getRequest().getSpecification();
		 DataManager.i().mPipelineChannel = DataManager.i().mPipelineSpecification.getChannel();
		 DataManager.i().setScreenType(ScreenType.PIPELINESPECIFICATIONFROMPIPELINEQUEUE);
		 EtcAdmin.i().setScreen(ScreenType.PIPELINESPECIFICATIONFROMPIPELINEQUEUE, true);
	}
	
	private void viewSelectedEmployer(){
		// set the request and refresh the screen
		 HBoxCell cellItem = pqueFilesListView.getSelectionModel().getSelectedItem();
		 DataManager.i().setPipelineQueueEntry(cellItem.queueLocation);
		 DataManager.i().mEmployer = DataManager.i().mPipelineQueueEntry.getRequest().getEmployer();
		 DataManager.i().setScreenType(ScreenType.EMPLOYERFROMPIPELINEQUEUE);
		 EtcAdmin.i().setScreen(ScreenType.EMPLOYERFROMPIPELINEQUEUE, true);
	}
	
	private void viewSelectedAccount(){
		// set the request and refresh the screen
		 HBoxCell cellItem = pqueFilesListView.getSelectionModel().getSelectedItem();
		 DataManager.i().setPipelineQueueEntry(cellItem.queueLocation);
		 DataManager.i().mAccount = DataManager.i().mPipelineQueueEntry.getRequest().getEmployer().getAccount();
		 DataManager.i().setScreenType(ScreenType.ACCOUNTFROMPIPELINEQUEUE);
		 EtcAdmin.i().setScreen(ScreenType.ACCOUNTFROMPIPELINEQUEUE, true);
	}
	
	@FXML
	private void onFilterKey() {
		loadPipelineRequests();
	}
	

	@FXML
	private void applyFilter(ActionEvent event) {
		loadPipelineRequests();
	}
	
	@FXML
	private void clearFilter(ActionEvent event) {
		// clear the filter
		pqueSearchFilter.setText("");
		// and refresh
		loadPipelineRequests();
	}
	
	@FXML
	private void onTimeZone(ActionEvent event){
		loadPipelineRequests();
	}
	
	@FXML
	private void onRefresh(ActionEvent event){
		updateQueueData();
	}
	
	@FXML
	private void onResync(ActionEvent event){
		resyncQueueData();
	}
	
	@FXML
	private void onCopyFilename(ActionEvent event) {
	      try {
	  		HBoxCell cellItem = pqueFilesListView.getSelectionModel().getSelectedItem();
			if (cellItem == null) {
		    	  Utils.showAlert("No Selection", "Please select queue entry to download");
				return;
			}
			queueData.mPipelineQueueEntry = queueData.mPipelineQueue.get(cellItem.queueLocation);
			
			
			Long docId = null;
			switch(queueData.mPipelineQueueEntry.getFileType()) {
			case COVERAGE: 
				if (queueData.mPipelineQueueEntry.getCoverageFile() != null && queueData.mPipelineQueueEntry.getCoverageFile().getPipelineInformation() != null) {
					docId = queueData.mPipelineQueueEntry.getCoverageFile().getPipelineInformation().getDocumentId();
				}
				break;
			case EMPLOYEE: 
				if (queueData.mPipelineQueueEntry.getEmployeeFile() != null && queueData.mPipelineQueueEntry.getEmployeeFile().getPipelineInformation() != null) {
					docId = queueData.mPipelineQueueEntry.getEmployeeFile().getPipelineInformation().getDocumentId();
				}
				break;
			case IRS1094C: 
				if (queueData.mPipelineQueueEntry.getIrs1094cFile() != null && queueData.mPipelineQueueEntry.getIrs1094cFile().getPipelineInformation() != null) {
					docId = queueData.mPipelineQueueEntry.getIrs1094cFile().getPipelineInformation().getDocumentId();
				}
				break;
			case IRS1095C: 
				if (queueData.mPipelineQueueEntry.getIrs1095cFile() != null && queueData.mPipelineQueueEntry.getIrs1095cFile().getPipelineInformation() != null) {
					docId = queueData.mPipelineQueueEntry.getIrs1095cFile().getPipelineInformation().getDocumentId();
				}
				break;
			case IRSAIRERR: 
				if (queueData.mPipelineQueueEntry.getAirTranErrorFile() != null && queueData.mPipelineQueueEntry.getAirTranErrorFile().getPipelineInformation() != null) {
					docId = queueData.mPipelineQueueEntry.getAirTranErrorFile().getPipelineInformation().getDocumentId();
				}
				break;
			case IRSAIRRCPT: 
				if (queueData.mPipelineQueueEntry.getAirTranReceiptFile() != null && queueData.mPipelineQueueEntry.getAirTranReceiptFile().getPipelineInformation() != null) {
					docId = queueData.mPipelineQueueEntry.getAirTranReceiptFile().getPipelineInformation().getDocumentId();
				}
				break;
			case PAY: 
				if (queueData.mPipelineQueueEntry.getPayFile() != null && queueData.mPipelineQueueEntry.getPayFile().getPipelineInformation() != null) {
					docId = queueData.mPipelineQueueEntry.getPayFile().getPipelineInformation().getDocumentId();
				}
				break;
			case PAYPERIOD: 
				if (queueData.mPipelineQueueEntry.getPayPeriodFile() != null && queueData.mPipelineQueueEntry.getPayPeriodFile().getPipelineInformation() != null) {
					docId = queueData.mPipelineQueueEntry.getPayPeriodFile().getPipelineInformation().getDocumentId();
				}
				break;
			case DEDUCTION: 
				if (queueData.mPipelineQueueEntry.getDeductionFile() != null && queueData.mPipelineQueueEntry.getDeductionFile().getPipelineInformation() != null) {
					docId = queueData.mPipelineQueueEntry.getDeductionFile().getPipelineInformation().getDocumentId();
				}
				break;
			case INSURANCE: 
				if (queueData.mPipelineQueueEntry.getInsuranceFile() != null && queueData.mPipelineQueueEntry.getInsuranceFile().getPipelineInformation() != null) {
					docId = queueData.mPipelineQueueEntry.getInsuranceFile().getPipelineInformation().getDocumentId();
				}
				break;
			default:
				Utils.showAlert("Unsupported File Type", "The selected file type is not supported.");
				return;
			}
			
			if (docId == null) {
				Utils.showAlert("File Document Type Issue", "The selected file is not supported or does not have document information");
				return;
			}
			
			try {
		 		String path = Utils.browseDirectory((Stage) pqueSearchFilter.getScene().getWindow(), "");
		 		if (path == "") return;
		 		
				if (Utils.downloadFile(docId, path) == false) {
					return;
				}
				if (queueData.mPipelineQueueEntry.getRequest() != null && queueData.mPipelineQueueEntry.getRequest().getDescription() != null)
					Utils.showAlert("File Downloaded", "The file \n" + queueData.mPipelineQueueEntry.getRequest().getDescription() + "\nhas been downloaded to \n" + path);
				else
					Utils.showAlert("File Downloaded", "The selected file has been downloaded to " + path);
			}catch (Exception e){
		    	 Utils.showAlert("No Selection", "Please select file entry to download.");
		     }	
			
	        //final Clipboard clipboard = Clipboard.getSystemClipboard();
	        //final ClipboardContent content = new ClipboardContent();
	        //content.putString(queueData.mPipelineQueueEntry.getRequest().getDescription());
	        //clipboard.setContent(content);
	        //Utils.showAlert("Copied", "Selected filename copied to System Clipboard.");
	      }catch (Exception e){
	    	  Utils.showAlert("No Selection", "Please select queue entry first.");
	      }
		
	}
	
	@FXML
	private void onViewDetail(ActionEvent event){
		// set the request and refresh the screen
		 HBoxCell cellItem = pqueFilesListView.getSelectionModel().getSelectedItem();
		 DataManager.i().setPipelineQueueEntry(cellItem.queueLocation);
		 EtcAdmin.i().setScreen(ScreenType.PIPELINEQUEUEDETAIL, true);
	}
	
	@FXML
	private void onViewMapper(ActionEvent event){
		viewSelectedMapper();
	}
	
	@FXML
	private void onRejectFile(ActionEvent event){
		 HBoxCell cellItem = pqueFilesListView.getSelectionModel().getSelectedItem();
		 DataManager.i().setPipelineQueueEntry(cellItem.queueLocation);
		 PipelineQueue queEntry = DataManager.i().mPipelineQueueEntry;
		 PipelineRequest request = queueData.rejectPipelineFile(queEntry);
		 
		 // make sure that it is rejected
		 if (request == null) {
			Utils.alertUser("Reject File Error", "The file reject request did not successfully complete.");
			return;
		 }
		 
		 //remove the file from the local store - this should not be needed since we gather the updated requests
		 //queEntry.removePipelineFile(queEntry.getRequest().getPipelineFileId(),
		 //		   					 queEntry.getFileType());
		 // refresh the queue
		 Utils.showAlert("Reject File", "The selected file with Pipeline Request id:" +  request.getId().toString() +  " has been rejected");
		 updateQueueData();
	}
	
	@FXML
	private void onQueueRange(ActionEvent event){
		updateQueueData();
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
