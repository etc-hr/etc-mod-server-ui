package com.etc.admin.ui.export;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.calc.ViewCalcQueueController.HBoxCell;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.exp.entities.ExportRequest;
import com.etc.corvetto.ems.exp.rqs.ExportRequestRequest;
import com.etc.utils.types.RequestStatusType;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;

public class ViewExportQueueController {
	@FXML
	private TableView<HBoxCell> expTableView;
	@FXML
	private ProgressBar expProgress;
	@FXML
	private CheckBox expAutoUpdateCheck;
	@FXML
	private TextField expFilterField;
	@FXML
	private ComboBox<String> expDateRangeCombo;

	//table sort 
	TableColumn<HBoxCell, String> sortColumn = null;
	boolean timerActive = true;
	double queueCounter = 0;
	List<ExportRequest> exportQueue = null;
	
	// track selected item
	int queueSelection = -1;

	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		updateQueueData((expDateRangeCombo.getSelectionModel().getSelectedIndex() + 1) * 30);
	}
	
	private void initControls() {
		// set the range choices
		expDateRangeCombo.getItems().clear();
		expDateRangeCombo.getItems().add("30");
		expDateRangeCombo.getItems().add("60");
		expDateRangeCombo.getItems().add("90");
		expDateRangeCombo.getSelectionModel().clearAndSelect(0);
		
		// set the table columns for our queue
		setTableColumns();

		//add handlers for listbox selection notification
		expTableView.setOnMouseClicked(mouseClickedEvent -> {
			// get the selection
			HBoxCell cellItem = expTableView.getSelectionModel().getSelectedItem();
			if (cellItem == null) return;
			
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) {
    			queueSelection = expTableView.getSelectionModel().getSelectedIndex();
    			showExportQueue();
            }
            
			
			// launch if it is a double click
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// set the request and load the details
            	//EtcAdmin.i().setScreen(ScreenType.EXPORTREQUESTDETAIL, true);
            }
        });	
		
	  //create timing thread to service the queue updates
        Thread queueTimer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                    	if (timerActive) {
                    		queueCounter++;
		                	if (queueCounter > 11) {
		                		queueCounter = 0.0;
		                		expProgress.setProgress(1.0);
		                		updateQueueData((expDateRangeCombo.getSelectionModel().getSelectedIndex() + 1) * 30);
		                	} else {
		                		expProgress.setProgress(1.0 - (queueCounter * .083));
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
	}
	
	private void setTableColumns() {
		//clear the default values
		expTableView.getColumns().clear();

	    TableColumn<HBoxCell, String> xUser = new TableColumn<HBoxCell, String>("User");
	    xUser.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("user"));
	    xUser.setMinWidth(100);
		expTableView.getColumns().add(xUser);
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
                        		if (param.getTableView().getItems().get(currentIndex) != null) {
                        			RequestStatusType statusType =  param.getTableView().getItems().get(currentIndex).getRequest().getStatus();
                        			setStyle(getColorString(statusType));
                        		}
                        	} 
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
	    TableColumn<HBoxCell, String> xId = new TableColumn<HBoxCell, String>("Id");
	    xId.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("id"));
	    xId.setMinWidth(100);
		expTableView.getColumns().add(xId);
		xId.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
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
                        		if (param.getTableView().getItems().get(currentIndex) != null) {
                        			RequestStatusType statusType =  param.getTableView().getItems().get(currentIndex).getRequest().getStatus();
                        			setStyle(getColorString(statusType));
                        		}
                        	} 
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxCell, String> x1 = new TableColumn<HBoxCell, String>("Date");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("dateTime"));
		x1.setMinWidth(140);
		//initial sort is by date
		x1.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		sortColumn = x1;
		sortColumn.setSortType(SortType.DESCENDING);
		expTableView.getColumns().add(x1);
		x1.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
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
                        		if (param.getTableView().getItems().get(currentIndex) != null) {
                        			RequestStatusType statusType =  param.getTableView().getItems().get(currentIndex).getRequest().getStatus();
                        			setStyle(getColorString(statusType));
                        		}
                        	} 
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
	    TableColumn<HBoxCell, String> x2 = new TableColumn<HBoxCell, String>("Description");
		x2.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("description"));
		x2.setMinWidth(500);
		expTableView.getColumns().add(x2);
		x2.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
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
                        		if (param.getTableView().getItems().get(currentIndex) != null) {
                        			RequestStatusType statusType =  param.getTableView().getItems().get(currentIndex).getRequest().getStatus();
                        			setStyle(getColorString(statusType));
                        		}
                        	} 
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		
	    TableColumn<HBoxCell, String> x3 = new TableColumn<HBoxCell, String>("Status");
	    x3.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("status"));
	    x3.setMinWidth(100);
	    expTableView.getColumns().add(x3);
		x3.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
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
                        		if (param.getTableView().getItems().get(currentIndex) != null) {
                        			RequestStatusType statusType =  param.getTableView().getItems().get(currentIndex).getRequest().getStatus();
                        			setStyle(getColorString(statusType));
                        		}
                        	} 
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
	    TableColumn<HBoxCell, String> x4 = new TableColumn<HBoxCell, String>("Spec Id");
	    x4.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("specificationId"));
	    x4.setMinWidth(100);
	    expTableView.getColumns().add(x4);
		x4.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getSpecificationId();
                        	setText(val);
                        	if (currentIndex == queueSelection)
                        		setStyle("-fx-background-color: #aaaaaa");
                        	else {
                        		if (param.getTableView().getItems().get(currentIndex) != null) {
                        			RequestStatusType statusType =  param.getTableView().getItems().get(currentIndex).getRequest().getStatus();
                        			setStyle(getColorString(statusType));
                        		}
                        	} 
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
	    TableColumn<HBoxCell, String> x5 = new TableColumn<HBoxCell, String>("Export Id");
	    x5.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("exportId"));
	    x5.setMinWidth(100);
	    expTableView.getColumns().add(x5);
		x5.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
        {
            public TableCell<HBoxCell, String> call(TableColumn<HBoxCell, String> param) {
                return new TableCell<HBoxCell, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        if (!empty) {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getExportId();
                        	setText(val);
                        	if (currentIndex == queueSelection)
                        		setStyle("-fx-background-color: #aaaaaa");
                        	else {
                        		if (param.getTableView().getItems().get(currentIndex) != null) {
                        			RequestStatusType statusType =  param.getTableView().getItems().get(currentIndex).getRequest().getStatus();
                        			setStyle(getColorString(statusType));
                        		}
                        	} 
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
	    TableColumn<HBoxCell, String> x6 = new TableColumn<HBoxCell, String>("Result");
	    x6.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("result"));
	    x6.setMinWidth(800);
	    expTableView.getColumns().add(x6);
		x6.setCellFactory(new Callback<TableColumn<HBoxCell, String>,TableCell<HBoxCell, String>>()
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
                        		if (param.getTableView().getItems().get(currentIndex) != null) {
                        			RequestStatusType statusType =  param.getTableView().getItems().get(currentIndex).getRequest().getStatus();
                        			setStyle(getColorString(statusType));
                        		}
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
	
	private String getColorString(RequestStatusType statusType) {
    	switch (statusType) {
		case UNPROCESSED:
			return "-fx-background-color: #fec077";
		case QUEUED:
			return "-fx-background-color: #feddee";
		case PROCESSING:
			return "-fx-background-color: #bbeeff";
		case PENDING:
			return "-fx-background-color: #ddffdd";
		case COMPLETED:
			return "-fx-background-color: #eeffee";
		case FAILED:
			return "-fx-background-color: #ffdddd";
	}

    	return "-fx-background-color: #ffffff";
	}



	public void stopTimer() {
		timerActive = false;
	}
	
	public void startTimer() {
		if (expAutoUpdateCheck.isSelected())
			timerActive = true;
	}
	
	@SuppressWarnings("unchecked")
	private void updateQueueData(int days) {
		// pause the queue timer while retrieving data
		stopTimer();
		// keep our current sort
       if (expTableView.getSortOrder().size()>0)
            sortColumn = (TableColumn<HBoxCell, String>) expTableView.getSortOrder().get(0);

		// create a thread to handle the update, letting the screen respond normally
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            	try {
	            	// update data here
	            	ExportRequestRequest request = new ExportRequestRequest();
	            	//create a lastUpdated for the past day
	            	Calendar queueDate = Calendar.getInstance();
	            	queueDate.add(Calendar.DAY_OF_MONTH, -days);
	            	request.setLastUpdated(queueDate.getTimeInMillis());
	            	// retrieve from the server
	            	exportQueue = AdminPersistenceManager.getInstance().getAll(request);
            	}catch (Exception e) {
            		DataManager.i().log(Level.SEVERE, e);
                }
                return null;
            }
        };
        
      	task.setOnScheduled(e ->  {
      		EtcAdmin.i().setStatusMessage("Updating Queue Data...");
      		EtcAdmin.i().setProgress(0.25);
      	});
      			
    	task.setOnSucceeded(e ->  showExportQueue());
    	
    	task.setOnFailed(e ->  {
	    	DataManager.i().log(Level.SEVERE,task.getException());
	    	showExportQueue();
    	});
    	
    	Thread queueRefresh = new Thread(task);
    	queueRefresh.start();
	}
	
	private void showExportQueue() {
		//reset our refresh counter
		queueCounter = 0.0;
    	expProgress.setProgress(1.0);
		
		//load the queue
    	expTableView.getItems().clear();
    	
    	if (exportQueue != null) {
	    	for (ExportRequest export : exportQueue) {
	    		// apply the filter if present
	    		if (expFilterField.getText().length() > 0) {
	    			// build the search string
	    			String searchString = " " + export.getDescription() + " " + 
	    								  export.getResult() + " " +
	    								  export.getSpecificationId() + " " +
	    								  export.getCreatedBy() + " " + 
	    								  export.getStatus().toString() + "" +
	    								  export.getId() + " " +
	    								  export.getExportId().toString() + " " +
	    								  Utils.getDateString(export.getLastUpdated());
	    			if (searchString.toLowerCase().contains(expFilterField.getText().toLowerCase()))
	    				expTableView.getItems().add(new HBoxCell(export, 0));
	    		} else
	    			expTableView.getItems().add(new HBoxCell(export, 0));
	    	}
    	}

		if(sortColumn!=null) 
		{
			expTableView.getSortOrder().add(sortColumn);
            sortColumn.setSortable(true);
        }

		// update the status
		EtcAdmin.i().setStatusMessage("Ready");
		EtcAdmin.i().setProgress(0);
		//reenable the queue timer
		startTimer();
	}	
	
	class HBoxCellComparator implements Comparator<HBoxCell> {
		  @Override
		  public int compare(HBoxCell cell1, HBoxCell cell2) {
		      return cell2.getDate().compareTo(cell1.getDate());
		  }
		}
	
	//extending the listview for our additional controls
	public class HBoxCell {
		SimpleStringProperty id = new SimpleStringProperty();
		SimpleStringProperty dateTime = new SimpleStringProperty();
		SimpleStringProperty description = new SimpleStringProperty();
		SimpleStringProperty status = new SimpleStringProperty();
		SimpleStringProperty specificationId = new SimpleStringProperty();
		SimpleStringProperty exportId = new SimpleStringProperty();
		SimpleStringProperty result = new SimpleStringProperty();
		SimpleStringProperty account = new SimpleStringProperty();
		SimpleStringProperty employer = new SimpleStringProperty();
		SimpleStringProperty user = new SimpleStringProperty();
        ExportRequest request;

        String getDate() {return dateTime.get();}

        public String getId() {
       	 return id.get();
        }
        
        public String getDateTime() {
        	 return dateTime.get();
         }
         
         public String getDescription() {
        	 return description.get();
         }
         
         public String getStatus() {
        	 return status.get();
         }
         
         public String getSpecificationId() {
        	 return specificationId.get();
         }
         
         public String getExportId() {
        	 return exportId.get();
         }
         
         public String getResult() {
        	 return result.get();
         }
         
         public String getAccount() {
           	 return account.get();
            }
            
         public String getEmployer() {
           	 return employer.get();
            }
            
         public String getUser() {
           	 return user.get();
            }
            
         public ExportRequest getRequest() {
        	 return request;
         }

         HBoxCell(ExportRequest request, int nCount) {
              super();

              //save the requestId;
              this.request = request;
              
              if (request != null) {
            	  id.set(request.getId().toString());
            	  dateTime.set(Utils.getDateTimeString24(request.getLastUpdated()));
            	  description.set(request.getDescription());
            	  if (request.getStatus() !=  null)
            		  status.set(request.getStatus().toString());
            	  if (request.getSpecificationId() != null)
            		  specificationId.set(request.getSpecificationId().toString());
            	  if (request.getExportId() != null)
            		  exportId.set(request.getExportId().toString());
            	  if (request.getCreatedBy() != null)
            		  user.set(request.getCreatedBy().getFirstName() + " " + request.getCreatedBy().getLastName().substring(0, 1));
            	  result.set(request.getResult());
              } else {
            	  id.set("Id");
            	  dateTime.set("Date");
            	  description.set("Description");
            	  status.set("Status");
            	  specificationId.set("Spec Id");
            	  exportId.set("Export Id");
            	  result.set("Result");
            	  user.set("User");
              }
         }
    }	
	
	@FXML
	private void onClearFilter(ActionEvent event) {
		// clear the filter
		expFilterField.setText("");
		// and refresh
		showExportQueue();
	}
	
	@FXML
	private void onRefresh(ActionEvent event){
		updateQueueData((expDateRangeCombo.getSelectionModel().getSelectedIndex() + 1) * 30);
	}
	
	@FXML
	private void onFilterKey(){
		showExportQueue();
	}
	
	@FXML
	private void onAutorefresh(ActionEvent event) {
		if (expAutoUpdateCheck.isSelected()) {
			timerActive = true;
			expProgress.setProgress(1.0);
		}else {
			timerActive = false;
        	queueCounter = 0.0;
        	expProgress.setProgress(0.0);
		}
	}
}
