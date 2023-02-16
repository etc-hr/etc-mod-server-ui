package com.etc.admin.ui.employee;

import java.io.IOException;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.coverage.ViewCoverageController;
import com.etc.admin.ui.employee.ViewPayrollController.PayCell;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.Benefit;
import com.etc.corvetto.entities.Coverage;
import com.etc.corvetto.entities.Pay;
import com.etc.corvetto.rqs.BenefitRequest;
import com.etc.corvetto.rqs.CoverageRequest;
import com.etc.corvetto.rqs.CoverageSnapshotRequest;
import com.etc.corvetto.rqs.PayRequest;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
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

public class ViewEmployeeCoverageController 
{

	@FXML
	private Label empCoverageLabel;
	@FXML
	private TableView<CoverageCell> empCoverageTableView;
	@FXML
	private CheckBox empInactiveCoveragesCheck;
	@FXML
	private CheckBox empDeletedCoveragesCheck;
	
	//table sort 
	TableColumn<CoverageCell, String> sortCoverageColumn = null;
	//int to track any pending threads, used to properly update the progress and message
	int waitingToComplete = 0;
	ContextMenu coverageCM = null;

	
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	@FXML
	public void initialize() 
	{
		initControls();
		updateCoverageData();
	}
	
	private void initControls() 
	{
		// set the table columns
		setCoverageTableColumns();
		
		// allow multiple row selection
		empCoverageTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		// Create a context menu
		coverageCM  = new ContextMenu();
		MenuItem mi1 = new MenuItem("Toggle Coverage Active/Inactive");
		mi1.setOnAction(new EventHandler<ActionEvent>() 
		{
	        @Override
	        public void handle(ActionEvent t)
	        {
	        	if (empCoverageTableView.getSelectionModel().getSelectedItems() != null) 
	        	{
	        	    Task<Void> task = new Task<Void>() {
	        		    @Override protected Void call() throws Exception {
	        				try {
				        		int count = empCoverageTableView.getSelectionModel().getSelectedItems().size();
				        		int ticker = 0;
				        		for (CoverageCell pc : empCoverageTableView.getSelectionModel().getSelectedItems()) {
				        			ticker++;
					        		EtcAdmin.i().showAppStatus("Setting Coverage Activation Status", "Updating Activation for Coverage Id# " + pc.getCoverage().getId().toString(), ticker / count, true);
					        		toggleCoverageActivation(pc.getCoverage());
				        		}
				        		EtcAdmin.i().showAppStatus("Setting Coverage Activation Status", "Updating Coverage List", 1, true);
					        	updateCoverageData();
				        		EtcAdmin.i().hideAppStatus();
	        				}
	        			    catch (Exception e) {  DataManager.i().logGenericException(e); }
	        				return null;
		        		}
	        	    };
	        		Thread thread = new Thread(task, "showAppStatus");
	        		thread.setDaemon(true);
	        		thread.start();		
	        	}
	        }
	    });
		coverageCM.getItems().add(mi1);
		coverageCM.setAutoHide(true);
	
		// click handler
		empCoverageTableView.setOnMouseClicked(mouseClickedEvent ->
		{
			coverageCM.hide();
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) 
            {
            	//set the selected coverage
				DataManager.i().mCoverage = empCoverageTableView.getSelectionModel().getSelectedItem().getCoverage();
				// and display the pop up
				try {
		            // load the fxml
			        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coverage/ViewCoverage.fxml"));
					Parent ControllerNode = loader.load();
			        ViewCoverageController coverageController = (ViewCoverageController) loader.getController();
			        Stage stage = new Stage();
			        stage.initModality(Modality.APPLICATION_MODAL);
			        stage.initStyle(StageStyle.UNDECORATED);
			        stage.setScene(new Scene(ControllerNode));  
			        EtcAdmin.i().positionStageCenter(stage);
			        stage.showAndWait();
			        if(coverageController.changesMade == true)
			        	updateCoverageData();
				} catch (IOException e) {
		        	DataManager.i().log(Level.SEVERE, e); 
				}        		
			    catch (Exception e) {  DataManager.i().logGenericException(e); }
            }
            if(mouseClickedEvent.getButton().equals(MouseButton.SECONDARY) && mouseClickedEvent.getClickCount() < 2) 
            {
            	if (empCoverageTableView.getSelectionModel().getSelectedItem() != null)
            		coverageCM.show(empCoverageTableView, mouseClickedEvent.getScreenX(), mouseClickedEvent.getScreenY());
            }
        });
	}

	private void setCoverageTableColumns() 
	{
		//clear the default values
		empCoverageTableView.getColumns().clear();

	    TableColumn<CoverageCell, String> x1 = new TableColumn<CoverageCell, String>("Core Id");
		x1.setCellValueFactory(new PropertyValueFactory<CoverageCell,String>("id"));
		x1.setMinWidth(100);
		empCoverageTableView.getColumns().add(x1);
		setCoverageCellFactory(x1);
		
		TableColumn<CoverageCell, String> x2 = new TableColumn<CoverageCell, String>("Start Date");
		x2.setCellValueFactory(new PropertyValueFactory<CoverageCell,String>("startDate"));
		x2.setMinWidth(110);
		x2.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		empCoverageTableView.getColumns().add(x2);
		setCoverageCellFactory(x2);

	    TableColumn<CoverageCell, String> x3 = new TableColumn<CoverageCell, String>("End Date");
		x3.setCellValueFactory(new PropertyValueFactory<CoverageCell,String>("endDate"));
		x3.setMinWidth(110);
		x3.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		//initial sort is by start date
		sortCoverageColumn = x3;
		sortCoverageColumn.setSortType(SortType.DESCENDING);
		empCoverageTableView.getColumns().add(x3);
		setCoverageCellFactory(x3);
		
	    TableColumn<CoverageCell, String> xBf = new TableColumn<CoverageCell, String>("Plan");
	    xBf.setCellValueFactory(new PropertyValueFactory<CoverageCell,String>("benefitName"));
	    xBf.setMinWidth(150);
		empCoverageTableView.getColumns().add(xBf);
		setCoverageCellFactory(xBf);
		
	    TableColumn<CoverageCell, String> xIT = new TableColumn<CoverageCell, String>("Ins Type");
	    xIT.setCellValueFactory(new PropertyValueFactory<CoverageCell,String>("insuranceType"));
	    xIT.setMinWidth(150);
		empCoverageTableView.getColumns().add(xIT);
		setCoverageCellFactory(xIT);
		
	    TableColumn<CoverageCell, String> x4 = new TableColumn<CoverageCell, String>("Member Share");
		x4.setCellValueFactory(new PropertyValueFactory<CoverageCell,String>("memberShare"));
		x4.setMinWidth(120);
		empCoverageTableView.getColumns().add(x4);
		setCoverageCellFactory(x4);
		
	  /*  TableColumn<CoverageCell, String> x5 = new TableColumn<CoverageCell, String>("Deduction");
		x5.setCellValueFactory(new PropertyValueFactory<CoverageCell,String>("deduction"));
		x5.setMinWidth(100);
		empCoverageTableView.getColumns().add(x5);
		setCoverageCellFactory(x5);
		*/
	    TableColumn<CoverageCell, String> x6 = new TableColumn<CoverageCell, String>("Waived");
		x6.setCellValueFactory(new PropertyValueFactory<CoverageCell,String>("waived"));
		x6.setMinWidth(100);
		empCoverageTableView.getColumns().add(x6);
		setCoverageCellFactory(x6);
		
	    TableColumn<CoverageCell, String> x7 = new TableColumn<CoverageCell, String>("Ineligible");
		x7.setCellValueFactory(new PropertyValueFactory<CoverageCell,String>("ineligible"));
		x7.setMinWidth(100);
		empCoverageTableView.getColumns().add(x7);
		setCoverageCellFactory(x7);
		
	    TableColumn<CoverageCell, String> x8 = new TableColumn<CoverageCell, String>("Cobra");
		x8.setCellValueFactory(new PropertyValueFactory<CoverageCell,String>("cobra"));
		x8.setMinWidth(100);
		empCoverageTableView.getColumns().add(x8);
		setCoverageCellFactory(x8);
		
	    TableColumn<CoverageCell, String> xc1 = new TableColumn<CoverageCell, String>("Custom Field 1");
	    xc1.setCellValueFactory(new PropertyValueFactory<CoverageCell,String>("customField1"));
	    xc1.setMinWidth(150);
		empCoverageTableView.getColumns().add(xc1);
		setCoverageCellFactory(xc1);

	    TableColumn<CoverageCell, String> xc2 = new TableColumn<CoverageCell, String>("Custom Field 2");
	    xc2.setCellValueFactory(new PropertyValueFactory<CoverageCell,String>("customField2"));
	    xc2.setMinWidth(150);
		empCoverageTableView.getColumns().add(xc2);
		setCoverageCellFactory(xc2);

	}
	
	private void setCoverageCellFactory(TableColumn<CoverageCell, String>  col) 
	{
		col.setCellFactory(column -> 
		{
		    return new TableCell<CoverageCell, String>() 
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
		                CoverageCell cell = getTableView().getItems().get(getIndex());
		                if (cell.getCoverage().isDeleted() == true)
		                	setTextFill(Color.RED);
		                else {
			                if (cell.getCoverage().isActive() == false)
			                	setTextFill(Color.BLUE);
			                else
			                	setTextFill(Color.BLACK);
		                }
		            }
		        }
		    };
		});
	}
	
	private void toggleCoverageActivation(Coverage coverage) {
		try {
			coverage.setActive(!coverage.isActive());
			CoverageRequest covRequest = new CoverageRequest();
			covRequest.setId(coverage.getId());
			covRequest.setEntity(coverage);
			AdminPersistenceManager.getInstance().addOrUpdate(covRequest);
		} catch (Exception e) {
			DataManager.i().log(Level.SEVERE, e); 
		}
	}
	
	// update the employee's coverage data
	private void updateCoverageData() 
	{
		try
		{
			empCoverageTableView.getItems().clear();
			//empCoverageLabel.setText("Coverages (loading...)"); 
			// new thread
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception
	            {
	            	//update our employer
	    			if(DataManager.i().mEmployee.getPerson() != null) 
	    			{
	                	CoverageRequest request = new CoverageRequest();
	        			request.setPersonId(DataManager.i().mEmployee.getPersonId());
	        			request.setFetchInactive(true);
	        			// retrieve any non-cached from the server
	        			DataManager.i().mEmployee.getPerson().setCoverages(AdminPersistenceManager.getInstance().getAll(request));
	    			}
	    			return null;
	            }
	        };
	        
	      	task.setOnScheduled(e ->  {
	      		EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Updating Employee Coverage Data...");
	      		waitingToComplete++;
	      	});		
	    	task.setOnSucceeded(e ->  { 
	    		if(waitingToComplete-- == 1)
	    			EtcAdmin.i().setProgress(0.0);
	    		showEmployeeCoverages(); 
	    	});
	    	task.setOnFailed(e ->  { 
	    		EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
	    		showEmployeeCoverages(); 
	    	});
	        
	    	EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e)
		{
			DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	//update the coverages table
	private void showEmployeeCoverages() 
	{
		//CoverageSnapshotRequest req = new 
		empCoverageTableView.getItems().clear();
	    if(DataManager.i().mEmployee == null || DataManager.i().mEmployee.getPerson() == null) return;

		if(DataManager.i().mEmployee.getPerson().getCoverages() != null)
		{
		    for(Coverage coverage : DataManager.i().mEmployee.getPerson().getCoverages()) 
		    {
		    	if(coverage.isActive() == false && empInactiveCoveragesCheck.isSelected() == false) continue;
		    	if(coverage.isDeleted() == true && empDeletedCoveragesCheck.isSelected() == false) continue;
		    	empCoverageTableView.getItems().add(new CoverageCell(coverage));
		    }
		    
		    empCoverageTableView.getSortOrder().add(sortCoverageColumn);
	        sortCoverageColumn.setSortable(true);

	        empCoverageLabel.setText("Employee Coverages (total: " + String.valueOf(DataManager.i().mEmployee.getPerson().getCoverages().size()) + ")");
		} else
			empCoverageLabel.setText("Employee Coverages (total: 0)");			
	}
	
	@FXML
	private void onCoverageInactivesClick(ActionEvent event) {
		showEmployeeCoverages();
	}	
	
	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) empCoverageTableView.getScene().getWindow();
		stage.close();
	}
	
	public class CoverageCell 
	{
		SimpleStringProperty id = new SimpleStringProperty();
		SimpleStringProperty startDate = new SimpleStringProperty();
		SimpleStringProperty endDate = new SimpleStringProperty();
		SimpleStringProperty benefitName = new SimpleStringProperty();
		SimpleStringProperty insuranceType = new SimpleStringProperty();
		SimpleStringProperty memberShare = new SimpleStringProperty();
		SimpleStringProperty deduction = new SimpleStringProperty();
		SimpleStringProperty waived = new SimpleStringProperty();
		SimpleStringProperty ineligible = new SimpleStringProperty();
		SimpleStringProperty cobra = new SimpleStringProperty();
		SimpleStringProperty customField1 = new SimpleStringProperty();
		SimpleStringProperty customField2 = new SimpleStringProperty();
		Coverage coverage;

		Coverage getCoverage() {
       	 return coverage;
        }
        
        public String getId() {
        	return id.get();
        }
           
        public String getStartDate() {
        	return startDate.get();
        }
        
        public String getBenefitName() {
        	return benefitName.get();
        }
           
        public String getInsuranceType() {
        	return insuranceType.get();
        }
           
        public String getMemberShare() {
        	return memberShare.get();
        }
        
        public String getDeduction() {
        	return deduction.get();
        }
        
        public String getWaived() {
        	return waived.get();
        }
        
        public String getIneligible() {
        	return ineligible.get();
        }
        
        public String getEndDate() {
        	return endDate.get();
        }
        
        public String getCobra() {
        	return cobra.get();
        }
        
        public String getCustomField1() {
        	return customField1.get();
        }
        
        public String getCustomField2() {
        	return customField2.get();
        }
        
        CoverageCell(Coverage coverage) 
        {
        	super();
		 
        	this.coverage = coverage;
        	if(coverage != null) 
        	{
        		id.set(String.valueOf(coverage.getId()));
        		if(coverage.getStartDate() != null)
        			startDate.set(Utils.getDateString(coverage.getStartDate()));
        		if(coverage.getEndDate() != null)
        			endDate.set(Utils.getDateString(coverage.getEndDate()));
        		if(coverage.getMemberShare() != null)
       		 		memberShare.set(String.valueOf(coverage.getMemberShare()));
        		else
        			memberShare.set(" ");
        		if(coverage.getDeduction() != null)
       		 		deduction.set(String.valueOf(coverage.getDeduction()));
        		else
        			deduction.set(" ");
       		 	ineligible.set(String.valueOf(coverage.isIneligible()));
       		 	cobra.set(String.valueOf(coverage.isCobra()));
       		 	waived.set(String.valueOf(coverage.isWaived()));
       		 	customField1.set(coverage.getCustomField1());
       		 	customField2.set(coverage.getCustomField2());
       		 	// benefit name
				String bName = "";
				try {
					  BenefitRequest bReq = new BenefitRequest();
					  bReq.setId(coverage.getBenefitId());
					  Benefit bf = AdminPersistenceManager.getInstance().get(bReq);
					  if (bf != null) {
						  bName = bf.getProviderReference();
						  if (bf.getInsuranceType() != null)
							  insuranceType.set(bf.getInsuranceType().toString());
					  }
				} catch (CoreException e) {
		        	DataManager.i().log(Level.SEVERE, e); 
				}
			    catch (Exception e) {  DataManager.i().logGenericException(e); }
				benefitName.set(bName);
        	}
        }
    }
	
	
	
}


