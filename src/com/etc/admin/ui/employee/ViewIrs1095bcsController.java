package com.etc.admin.ui.employee;

import java.io.IOException;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.AdminApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.corvetto.entities.Irs1095b;
import com.etc.corvetto.entities.Irs1095c;
import com.etc.corvetto.entities.TaxYear;
import com.etc.corvetto.rqs.Irs1095bRequest;
import com.etc.corvetto.rqs.Irs1095cRequest;
import com.etc.corvetto.rqs.TaxYearRequest;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewIrs1095bcsController 
{

	@FXML
	private TableView<HBox1095bcCell> empIrs1095bTableView;
	@FXML
	private Label empIrs1095bLabel;
	
	//table sort 
	TableColumn<HBox1095bcCell, String> sortIrs1095bColumn = null;
	//int to track any pending threads, used to properly update the progress and message
	int waitingToComplete = 0;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	@FXML
	public void initialize() 
	{
		initControls();
		updateIrs1095bData();
	}
	
	private void initControls() 
	{
		// set the table columns
		setIrs1095bTableColumns();
		// add handler for mouse double click
		empIrs1095bTableView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) 
            {
            	//set the selection
				// and display the pop up
				try {
					if (DataManager.i().mIrs1095bs != null && DataManager.i().mIrs1095bs.size() > 0) {
						DataManager.i().mIrs1095b = empIrs1095bTableView.getSelectionModel().getSelectedItem().getIrs1095b();
			            // load the fxml
				        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employee/ViewIRS1095b.fxml"));
						Parent ControllerNode = loader.load();
				        ViewIRS1095bController irs1095bController = (ViewIRS1095bController) loader.getController();
	
				        Stage stage = new Stage();
				        stage.initModality(Modality.APPLICATION_MODAL);
				        stage.initStyle(StageStyle.UNDECORATED);
				        stage.setScene(new Scene(ControllerNode));  
				        EtcAdmin.i().positionStageCenter(stage);
				        stage.showAndWait();
				        if(irs1095bController.changesMade == true)
				        	showIrs1095bs();
					} else {
						DataManager.i().mIrs1095c = empIrs1095bTableView.getSelectionModel().getSelectedItem().getIrs1095c();
			            // load the fxml
				        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employee/ViewIRS1095c.fxml"));
						Parent ControllerNode = loader.load();
				        ViewIRS1095cController irs1095cController = (ViewIRS1095cController) loader.getController();
	
				        Stage stage = new Stage();
				        stage.initModality(Modality.APPLICATION_MODAL);
				        stage.initStyle(StageStyle.UNDECORATED);
				        stage.setScene(new Scene(ControllerNode));  
				        EtcAdmin.i().positionStageCenter(stage);
				        stage.showAndWait();
				        if(irs1095cController.changesMade == true)
				        	showIrs1095cs();
					}
						
				} catch (IOException e) {
		        	DataManager.i().log(Level.SEVERE, e); 
				}        		
            }
        });

	}

	private void setIrs1095bTableColumns() 
	{
		//clear the default values
		empIrs1095bTableView.getColumns().clear();

	    TableColumn<HBox1095bcCell, String> x1 = new TableColumn<HBox1095bcCell, String>("Tax Year");
		x1.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("taxYear"));
		x1.setMinWidth(75);
		sortIrs1095bColumn = x1;
		sortIrs1095bColumn.setSortType(SortType.DESCENDING);
		empIrs1095bTableView.getColumns().add(x1);
		setIrs1095bCellFactory(x1);
		
		TableColumn<HBox1095bcCell, String> x2 = new TableColumn<HBox1095bcCell, String>("Core Id");
		x2.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("id"));
		x2.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x2);
		setIrs1095bCellFactory(x2);

	    TableColumn<HBox1095bcCell, String> x3 = new TableColumn<HBox1095bcCell, String>("Origin");
		x3.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("originType"));
		x3.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x3);
		setIrs1095bCellFactory(x3);
		
	    TableColumn<HBox1095bcCell, String> x4 = new TableColumn<HBox1095bcCell, String>("Self Ins.");
		x4.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("selfInsured"));
		x4.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x4);
		setIrs1095bCellFactory(x4);
		
	    TableColumn<HBox1095bcCell, String> x5 = new TableColumn<HBox1095bcCell, String>("File CI");
		x5.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("fileCI"));
		x5.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x5);
		setIrs1095bCellFactory(x5);
		
	    TableColumn<HBox1095bcCell, String> x6 = new TableColumn<HBox1095bcCell, String>("Jan");
		x6.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("janCov"));
		x6.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x6);
		setIrs1095bCellFactory(x6);

	    TableColumn<HBox1095bcCell, String> x7 = new TableColumn<HBox1095bcCell, String>("Feb");
		x7.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("febCov"));
		x7.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x7);
		setIrs1095bCellFactory(x7);
		
	    TableColumn<HBox1095bcCell, String> x8 = new TableColumn<HBox1095bcCell, String>("Mar");
		x8.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("marCov"));
		x8.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x8);
		setIrs1095bCellFactory(x8);
		
	    TableColumn<HBox1095bcCell, String> x9 = new TableColumn<HBox1095bcCell, String>("Apr");
		x9.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("aprCov"));
		x9.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x9);
		setIrs1095bCellFactory(x9);
		
	    TableColumn<HBox1095bcCell, String> x10 = new TableColumn<HBox1095bcCell, String>("May");
		x10.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("mayCov"));
		x10.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x10);
		setIrs1095bCellFactory(x10);
		
	    TableColumn<HBox1095bcCell, String> x11 = new TableColumn<HBox1095bcCell, String>("Jun");
		x11.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("junCov"));
		x11.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x11);
		setIrs1095bCellFactory(x11);
		
	    TableColumn<HBox1095bcCell, String> x12 = new TableColumn<HBox1095bcCell, String>("Jul");
		x12.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("julCov"));
		x12.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x12);
		setIrs1095bCellFactory(x12);
		
	    TableColumn<HBox1095bcCell, String> x13 = new TableColumn<HBox1095bcCell, String>("Aug");
		x13.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("augCov"));
		x13.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x13);
		setIrs1095bCellFactory(x13);
		
	    TableColumn<HBox1095bcCell, String> x14 = new TableColumn<HBox1095bcCell, String>("Sep");
		x14.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("sepCov"));
		x14.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x14);
		setIrs1095bCellFactory(x14);
		
	    TableColumn<HBox1095bcCell, String> x15 = new TableColumn<HBox1095bcCell, String>("Oct");
		x15.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("octCov"));
		x15.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x15);
		setIrs1095bCellFactory(x15);
		
	    TableColumn<HBox1095bcCell, String> x16 = new TableColumn<HBox1095bcCell, String>("Nov");
		x16.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("novCov"));
		x16.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x16);
		setIrs1095bCellFactory(x16);
		
	    TableColumn<HBox1095bcCell, String> x17 = new TableColumn<HBox1095bcCell, String>("Dec");
		x17.setCellValueFactory(new PropertyValueFactory<HBox1095bcCell,String>("decCov"));
		x17.setMinWidth(75);
		empIrs1095bTableView.getColumns().add(x17);
		setIrs1095bCellFactory(x17);
	}
	
	private void setIrs1095bCellFactory(TableColumn<HBox1095bcCell, String>  col) 
	{
		col.setCellFactory(column -> 
		{
		    return new TableCell<HBox1095bcCell, String>() 
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
		                HBox1095bcCell cell = getTableView().getItems().get(getIndex());
		                if(cell.isActive() == false)
		                	setTextFill(Color.BLUE);
		                else
		                	setTextFill(Color.BLACK);
		            }
		        }
		    };
		});
	}

	private void updateIrs1095bData()
	{
		try
		{
			empIrs1095bTableView.getItems().clear();
			empIrs1095bLabel.setText("Irs1095 Data (loading...)"); 
			// new thread
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception 
	            {
	            	//update our employer
	            	Irs1095bRequest req = new Irs1095bRequest();
	            	req.setFetchInactive(true);
	            	req.setEmployeeId(DataManager.i().mEmployee.getId());
	            	
	            	DataManager.i().mIrs1095bs = AdminPersistenceManager.getInstance().getAll(req);
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {
	      		EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Irs1095b Data...");
	      		waitingToComplete++;
	      	});
	      			
	    	task.setOnSucceeded(e ->  { 
	    		if(waitingToComplete-- == 1)
	    			EtcAdmin.i().setProgress(0.0);
	    		showIrs1095bs(); 
	    	});
	    	task.setOnFailed(e ->  { 
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		showIrs1095bs(); 
	    	});
	        
	    	AdminApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e)
		{
			DataManager.i().log(Level.SEVERE, e); 
		}
	}
	
	private void showIrs1095bs() 
	{
		empIrs1095bTableView.getItems().clear();
		if(DataManager.i().mIrs1095bs != null && DataManager.i().mIrs1095bs.size() > 0)
		{
			for(Irs1095b irs1095b : DataManager.i().mIrs1095bs)
		    {
		    	empIrs1095bTableView.getItems().add(new HBox1095bcCell(irs1095b));
		    }
			//update our label
	    	empIrs1095bLabel.setText("Irs1095b (total: " + String.valueOf(DataManager.i().mIrs1095bs.size()) + ")");
//			updateEmploymentPeriodData();
		} else // if we have no data, see if it is a 1095c
			updateIrs1095cData();
		
	}

	private void updateIrs1095cData()
	{
		try
		{
			// new thread
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception 
	            {
	            	//update our employer
	            	Irs1095cRequest req = new Irs1095cRequest();
	            	req.setFetchInactive(true);
	            	req.setEmployeeId(DataManager.i().mEmployee.getId());
	            	DataManager.i().mIrs1095cs = AdminPersistenceManager.getInstance().getAll(req);
	                return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {
	      		EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Loading Irs1095c Data...");
	      		waitingToComplete++;
	      	});
	      			
	    	task.setOnSucceeded(e ->  { 
	    		if(waitingToComplete-- == 1)
	    			EtcAdmin.i().setProgress(0.0);
	    		showIrs1095cs(); 
	    	});
	    	task.setOnFailed(e ->  { 
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		showIrs1095cs(); 
	    	});
	       
	    	AdminApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e)
		{
			DataManager.i().log(Level.SEVERE, e); 
		}
	}
	
	private void showIrs1095cs() 
	{
		empIrs1095bTableView.getItems().clear();
		if(DataManager.i().mIrs1095cs != null && DataManager.i().mIrs1095cs.size() > 0)
		{
			for(Irs1095c irs1095c : DataManager.i().mIrs1095cs)
		    {
		    	empIrs1095bTableView.getItems().add(new HBox1095bcCell(irs1095c));
		    }
			//update our Pays label
	    	empIrs1095bLabel.setText("Irs1095c (total: " + String.valueOf(DataManager.i().mIrs1095cs.size()) + ")");
		} else
			empIrs1095bLabel.setText("Irs1095bc (total: 0)");			
		
//		updateEmploymentPeriodData();
	}

	
	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) empIrs1095bTableView.getScene().getWindow();
		stage.close();
	}
	
	public static class HBox1095bcCell  
	{
		SimpleStringProperty id = new SimpleStringProperty();
		SimpleStringProperty originType = new SimpleStringProperty();
		SimpleStringProperty taxYear = new SimpleStringProperty();
		SimpleStringProperty selfInsured = new SimpleStringProperty();
		SimpleStringProperty fileCI = new SimpleStringProperty();
 		SimpleStringProperty janCov = new SimpleStringProperty();
 		SimpleStringProperty febCov = new SimpleStringProperty();
 		SimpleStringProperty marCov = new SimpleStringProperty();
 		SimpleStringProperty aprCov = new SimpleStringProperty();
 		SimpleStringProperty mayCov = new SimpleStringProperty();
		SimpleStringProperty junCov = new SimpleStringProperty();
		SimpleStringProperty julCov = new SimpleStringProperty();
		SimpleStringProperty augCov = new SimpleStringProperty();
		SimpleStringProperty sepCov = new SimpleStringProperty();
		SimpleStringProperty octCov = new SimpleStringProperty();
		SimpleStringProperty novCov = new SimpleStringProperty();
		SimpleStringProperty decCov = new SimpleStringProperty();

        Irs1095b irs1095b;
        Irs1095c irs1095c;
        boolean active;
         
        public Irs1095b getIrs1095b() {
        	return irs1095b;
        }
        
        public Irs1095c getIrs1095c() {
        	return irs1095c;
        }
        
        public boolean isActive() {
        	return active;
        }
        
        public String getId() {
        	return id.get();
        }
 
        public String getOriginType() {
        	return originType.get();
        }
 
        public String getTaxYear() {
        	return taxYear.get();
        }
           
        public String getSelfInsured() {
        	return selfInsured.get();
        }
           
        public String getFileCI() {
        	return fileCI.get();
        }
           
        public String getJanCov() {
        	return janCov.get();
        }
           
        public String getFebCov() {
        	return febCov.get();
        }
           
        public String getMarCov() {
        	return marCov.get();
        }
           
        public String getAprCov() {
        	return aprCov.get();
        }
           
        public String getMayCov() {
        	return mayCov.get();
        }
           
        public String getJunCov() {
        	return junCov.get();
        }
           
        public String getJulCov() {
        	return julCov.get();
        }
           
        public String getAugCov() {
        	return augCov.get();
        }

        public String getSepCov() {
        	return sepCov.get();
        }

        public String getOctCov() {
        	return octCov.get();
        }

        public String getNovCov() {
        	return novCov.get();
        }

        public String getDecCov() {
        	return decCov.get();
        }

        // 1095b constructor
        HBox1095bcCell(Irs1095b irs1095b) 
        {
             super();

             this.irs1095b = irs1095b;
             if(irs1095b != null) 
             {
            	active = irs1095b.isActive();
      			TaxYearRequest txYrReq = new TaxYearRequest();
    			txYrReq.setEmployerId(DataManager.i().mEmployee.getEmployerId());
    			try {
					DataManager.i().mTaxYears = AdminPersistenceManager.getInstance().getAll(txYrReq);
				} catch (CoreException e) {
		        	DataManager.i().log(Level.SEVERE, e); 
				}

    			for(TaxYear txYr : DataManager.i().mTaxYears)
    			{
    				if(txYr.getId().equals(irs1095b.getTaxYearId()))
    				{
    					DataManager.i().mTaxYear = txYr;
    				}
    			}
    			if(DataManager.i().mTaxYear != null)
            	{
    			  	taxYear.set(String.valueOf(DataManager.i().mTaxYear.getYear()));
            	}
            	id.set(String.valueOf(irs1095b.getId()));
            	if(irs1095b.getHealthCoverageOriginType() != null)
            		originType.set(String.valueOf(irs1095b.getHealthCoverageOriginType()));
            	else
            		originType.set(String.valueOf(""));
            	selfInsured.set(String.valueOf(irs1095b.isSelfInsured()));
            	fileCI.set(String.valueOf(irs1095b.isFileCIWithSSN()));
            	janCov.set(String.valueOf(irs1095b.isJanCovered()));
            	febCov.set(String.valueOf(irs1095b.isFebCovered()));
            	marCov.set(String.valueOf(irs1095b.isMarCovered()));
            	aprCov.set(String.valueOf(irs1095b.isAprCovered()));
            	mayCov.set(String.valueOf(irs1095b.isMayCovered()));
            	junCov.set(String.valueOf(irs1095b.isJunCovered()));
            	julCov.set(String.valueOf(irs1095b.isJulCovered()));
            	augCov.set(String.valueOf(irs1095b.isAugCovered()));
            	sepCov.set(String.valueOf(irs1095b.isSepCovered()));
            	octCov.set(String.valueOf(irs1095b.isOctCovered()));
            	novCov.set(String.valueOf(irs1095b.isNovCovered()));
            	decCov.set(String.valueOf(irs1095b.isDecCovered()));
            }
        }
        
        // 1095c constructor
        HBox1095bcCell(Irs1095c irs1095c) 
        {
             super();

             this.irs1095c = irs1095c;
             if(irs1095c != null) 
             {
             	active = irs1095c.isActive();
      			TaxYearRequest txYrReq = new TaxYearRequest();
    			txYrReq.setEmployerId(DataManager.i().mEmployee.getEmployerId());
    			try {
					DataManager.i().mTaxYears = AdminPersistenceManager.getInstance().getAll(txYrReq);
				} catch (CoreException e) {
		        	DataManager.i().log(Level.SEVERE, e); 
				}

    			for(TaxYear txYr : DataManager.i().mTaxYears)
    			{
    				if(txYr.getId().equals(irs1095c.getTaxYearId()))
    				{
    					DataManager.i().mTaxYear = txYr;
    				}
    			}
    			if(DataManager.i().mTaxYear != null)
            	{
    			  	taxYear.set(String.valueOf(DataManager.i().mTaxYear.getYear()));
            	}
            	id.set(String.valueOf(irs1095c.getId()));
            //	if(irs1095c.getHealthCoverageOriginType() != null)
            //		originType.set(String.valueOf(irs1095b.getHealthCoverageOriginType()));
            //	else
            //		originType.set(String.valueOf(""));
            	selfInsured.set(String.valueOf(irs1095c.isSelfInsured()));
            	fileCI.set(String.valueOf(irs1095c.isFileCIWithSSN()));
            	// covered
            	janCov.set(String.valueOf(irs1095c.isJanCovered()));
            	febCov.set(String.valueOf(irs1095c.isFebCovered()));
            	marCov.set(String.valueOf(irs1095c.isMarCovered()));
            	aprCov.set(String.valueOf(irs1095c.isAprCovered()));
            	mayCov.set(String.valueOf(irs1095c.isMayCovered()));
            	junCov.set(String.valueOf(irs1095c.isJunCovered()));
            	julCov.set(String.valueOf(irs1095c.isJulCovered()));
            	augCov.set(String.valueOf(irs1095c.isAugCovered()));
            	sepCov.set(String.valueOf(irs1095c.isSepCovered()));
            	octCov.set(String.valueOf(irs1095c.isOctCovered()));
            	novCov.set(String.valueOf(irs1095c.isNovCovered()));
            	decCov.set(String.valueOf(irs1095c.isDecCovered()));
            }
        }
        
    }
	
	
}


