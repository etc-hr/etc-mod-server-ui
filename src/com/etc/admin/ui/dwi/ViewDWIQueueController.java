package com.etc.admin.ui.dwi;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.entities.DocumentQueue;
import com.etc.corvetto.rqs.DocumentQueueRequest;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

public class ViewDWIQueueController {
	@FXML
	private TableView<HBoxCell> dwiTableView;
	@FXML
	private ProgressBar dwiProgress;
	@FXML
	private CheckBox dwiAutoUpdateCheck;
	@FXML
	private TextField dwiFilterField;

	//table sort 
	TableColumn<HBoxCell, String> sortColumn = null;
	boolean timerActive = true;
	double queueCounter = 0;
	List<DocumentQueue> docQueue = null;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		updateQueueData();
	}
	
	private void initControls() {
		// set the table columns for our queue
		setTableColumns();

		//add handlers for listbox selection notification
		dwiTableView.setOnMouseClicked(mouseClickedEvent -> {
			// get the selection
			HBoxCell cellItem = dwiTableView.getSelectionModel().getSelectedItem();
			if (cellItem == null) return;

			// launch if it is a double click
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
				// set the request and load the details
            	EtcAdmin.i().setScreen(ScreenType.PIPELINERAWFIELDGRID, true);
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
		                		dwiProgress.setProgress(1.0);
		                		updateQueueData();
		                	} else {
		                		dwiProgress.setProgress(1.0 - (queueCounter * .083));
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
		dwiTableView.getColumns().clear();

	    TableColumn<HBoxCell, String> xId = new TableColumn<HBoxCell, String>("Id");
	    xId.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("id"));
	    xId.setMinWidth(100);
		dwiTableView.getColumns().add(xId);
		
		TableColumn<HBoxCell, String> x1 = new TableColumn<HBoxCell, String>("Date");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("dateTime"));
		x1.setMinWidth(140);
		//initial sort is by date
		x1.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		sortColumn = x1;
		sortColumn.setSortType(SortType.DESCENDING);
		dwiTableView.getColumns().add(x1);
		
	    TableColumn<HBoxCell, String> x2 = new TableColumn<HBoxCell, String>("Name");
		x2.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("name"));
		x2.setMinWidth(500);
		dwiTableView.getColumns().add(x2);
		
	    TableColumn<HBoxCell, String> x3 = new TableColumn<HBoxCell, String>("Master");
	    x3.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("master"));
	    x3.setMinWidth(100);
	    dwiTableView.getColumns().add(x3);
		
	    TableColumn<HBoxCell, String> x4 = new TableColumn<HBoxCell, String>("Count");
	    x4.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("count"));
	    x4.setMinWidth(100);
	    dwiTableView.getColumns().add(x4);
		
	}

	public void stopTimer() {
		timerActive = false;
	}
	
	public void startTimer() {
		if (dwiAutoUpdateCheck.isSelected())
			timerActive = true;
	}
	
	@SuppressWarnings("unchecked")
	private void updateQueueData() {
		// pause the queue timer while retrieving data
		stopTimer();
		// keep our current sort
       if (dwiTableView.getSortOrder().size()>0)
            sortColumn = (TableColumn<HBoxCell, String>) dwiTableView.getSortOrder().get(0);

		// create a thread to handle the update, letting the screen respond normally
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            	// update data here
            	DocumentQueueRequest request = new DocumentQueueRequest();
            	//create a lastUpdated for the past day
            	Calendar queueDate = Calendar.getInstance();
            	//queueDate.add(Calendar.HOUR, -24);
            	queueDate.add(Calendar.YEAR, -1);
            	request.setLastUpdated(queueDate.getTimeInMillis());
            	// retrieve from the server
            	docQueue = AdminPersistenceManager.getInstance().getAll(request);
                return null;
            }
        };
        
    	Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() {
    	    public void uncaughtException(Thread th, Throwable ex) {
    	        System.out.println("Uncaught exception: " + ex);
    			Utils.alertUser("DWI Queue Exception", ex.getMessage());
    	    }
    	};
    	
      	task.setOnScheduled(e ->  {
      		EtcAdmin.i().setStatusMessage("Updating Queue Data...");
      		EtcAdmin.i().setProgress(0.25);});
      			
    	task.setOnSucceeded(e ->  showDWIQueue());
    	
    	task.setOnFailed(new EventHandler<WorkerStateEvent>() {
    	    @Override
    	    public void handle(WorkerStateEvent arg0) {
    	        Throwable throwable = task.getException(); 
    	        StringWriter sw = new StringWriter();
    	        throwable.printStackTrace(new PrintWriter(sw));
    	        String exceptionAsString = sw.toString();  
    	        
    	        Utils.alertUser("DWI Queue Error - StackTrace below", exceptionAsString);
    			EtcAdmin.i().setStatusMessage("Queue Error: " + DataManager.i().mDebugMessage);
    			EtcAdmin.i().setProgress(0);
    	    }
    	});
    	
    	//task.setOnFailed(e ->  QueueError());
    	Thread queueRefresh = new Thread(task);
    	queueRefresh.setUncaughtExceptionHandler(h);
    	queueRefresh.start();
	}
	
	private void showDWIQueue() {
		//reset our refresh counter
		queueCounter = 0.0;
    	dwiProgress.setProgress(1.0);
		
		//load the queue
    	dwiTableView.getItems().clear();
    	
    	if (docQueue != null) {
	    	for (DocumentQueue queue : docQueue) {
	    		// get the count here tbd
	    		// apply the filter if present
	    		if (dwiFilterField.getText().length() > 0) {
	    			if (queue.getName().contains(dwiFilterField.getText()))
	    				dwiTableView.getItems().add(new HBoxCell(queue, 0));
	    		} else
	    			dwiTableView.getItems().add(new HBoxCell(queue, 0));
	    	}
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
		SimpleStringProperty name = new SimpleStringProperty();
		SimpleStringProperty master = new SimpleStringProperty();
		SimpleStringProperty count = new SimpleStringProperty();
        DocumentQueue queue;

        String getDate() {return dateTime.get();}

        public String getId() {
       	 return id.get();
        }
        
        public String getDateTime() {
        	 return dateTime.get();
         }
         
         public String getName() {
        	 return name.get();
         }
         
         public String getMaster() {
        	 return master.get();
         }
         
         public String getCount() {
        	 return count.get();
         }

         HBoxCell(DocumentQueue queue, int nCount) {
              super();

              //save the requestId;
              this.queue = queue;
              
              if (queue != null) {
            	  id.set(queue.getId().toString());
            	  count.set(String.valueOf(nCount));
            	  name.set(queue.getName());
            	  dateTime.set(Utils.getDateTimeString24(queue.getLastUpdated()));
            	  master.set(String.valueOf(queue.isMaster()));
              } else {
            	  dateTime.set("Date");
            	  name.set("Name");
            	  master.set("Master");
            	  count.set("Count");
              }
         }
    }	
	
	@FXML
	private void onClearFilter(ActionEvent event) {
		// clear the filter
		dwiFilterField.setText("");
		// and refresh
		showDWIQueue();
	}
	
	@FXML
	private void onRefresh(ActionEvent event){
		updateQueueData();
	}
	
	@FXML
	private void onFilterKey(){
		showDWIQueue();
	}
	
	@FXML
	private void onAutorefresh(ActionEvent event) {
		if (dwiAutoUpdateCheck.isSelected()) {
			timerActive = true;
			dwiProgress.setProgress(1.0);
		}else {
			timerActive = false;
        	queueCounter = 0.0;
        	dwiProgress.setProgress(0.0);
		}
	}
}
