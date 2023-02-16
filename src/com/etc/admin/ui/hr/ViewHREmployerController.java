package com.etc.admin.ui.hr;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.ui.coresysteminfo.ViewCoreSystemInfoController;
import com.etc.admin.ui.employee.ViewPayrollController.PayCell;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.Employer;
import com.etc.entities.CoreData;

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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewHREmployerController {
	@FXML
	private TextField hractFilterText;
	@FXML
	private TableView<HRCell> hractEmployerTableView;
	@FXML
	private CheckBox hractShowInactive;
	@FXML
	private CheckBox hractShowDeleted;
	
	//table sort 
	TableColumn<HRCell, String> sortColumn = null;
	ContextMenu cmSystemData = null;

	// initialize is called when the FXML is loaded
	@FXML
	public void initialize() {
		initControls();
		setTableColumns();
		loadEmployers();
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
	        	if (hractEmployerTableView.getSelectionModel().getSelectedItems() != null) 
	        	{
	        		HRCell cell = hractEmployerTableView.getSelectionModel().getSelectedItem();
	        		Employer emp = cell.getEmployer();
	        		try {
	        			// set the coredata
	        			DataManager.i().mCoreData = (CoreData) emp;
	        			DataManager.i().mCurrentCoreDataType = SystemDataType.EMPLOYER;
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
	        	        	loadEmployers();
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
		
		//add handler for listbox selection notification
		hractEmployerTableView.setOnMouseClicked(mouseClickedEvent -> {
			cmSystemData.hide();
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
            	HRCell cell = hractEmployerTableView.getSelectionModel().getSelectedItem();
				Employer employer = cell.getEmployer();
				if (employer.getAccount() == null) return;
				DataManager.i().mAccount = employer.getAccount();
				DataManager.i().mEmployer = employer;
            	DataManager.i().hrSource = 1;
				EtcAdmin.i().setScreen(ScreenType.EMPLOYER, true);
				hidePopup();
            }
            if(mouseClickedEvent.getButton().equals(MouseButton.SECONDARY) && mouseClickedEvent.getClickCount() < 2) 
            {
            	if (hractEmployerTableView.getSelectionModel().getSelectedItem() != null)
            		cmSystemData.show(hractEmployerTableView, mouseClickedEvent.getScreenX(), mouseClickedEvent.getScreenY());
            }

        });
	}	
	
	private void hidePopup()
	{
		Stage stage = (Stage) hractEmployerTableView.getScene().getWindow();
		// hide if they are overlapping
		if (EtcAdmin.i().stageOverlapsMain(stage) == true)
			stage.hide();
	}


	private void setTableColumns() 
	{
		//clear the default values
		hractEmployerTableView.getColumns().clear();

	    TableColumn<HRCell, String> x1 = new TableColumn<HRCell, String>("Id");
		x1.setCellValueFactory(new PropertyValueFactory<HRCell,String>("id"));
		x1.setMinWidth(75);
		x1.setComparator((String o1, String o2) -> { return Utils.compareNumberStrings(o1, o2); });
		hractEmployerTableView.getColumns().add(x1);
		setCellFactory(x1);
		sortColumn = x1;
		sortColumn.setSortType(SortType.ASCENDING);
		
	    TableColumn<HRCell, String> x2 = new TableColumn<HRCell, String>("Date");
		x2.setCellValueFactory(new PropertyValueFactory<HRCell,String>("date"));
		x2.setMinWidth(125);
		x2.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		hractEmployerTableView.getColumns().add(x2);
		setCellFactory(x2);
		
	    TableColumn<HRCell, String> x3 = new TableColumn<HRCell, String>("Name");
		x3.setCellValueFactory(new PropertyValueFactory<HRCell,String>("name"));
		x3.setMinWidth(315);
		hractEmployerTableView.getColumns().add(x3);
		setCellFactory(x3);
		
	    TableColumn<HRCell, String> x4 = new TableColumn<HRCell, String>("Account");
		x4.setCellValueFactory(new PropertyValueFactory<HRCell,String>("account"));
		x4.setMinWidth(315);
		hractEmployerTableView.getColumns().add(x4);
		setCellFactory(x4);
		
	    TableColumn<HRCell, String> x5 = new TableColumn<HRCell, String>("Phone");
		x5.setCellValueFactory(new PropertyValueFactory<HRCell,String>("phone"));
		x5.setMinWidth(100);
		hractEmployerTableView.getColumns().add(x5);
		setCellFactory(x5);
		
	    TableColumn<HRCell, String> x6 = new TableColumn<HRCell, String>("State");
		x6.setCellValueFactory(new PropertyValueFactory<HRCell,String>("mailState"));
		x6.setMinWidth(100);
		hractEmployerTableView.getColumns().add(x6);
		setCellFactory(x6);
		
	}

	private void setCellFactory(TableColumn<HRCell, String>  col) 
	{
		col.setCellFactory(column -> 
		{
		    return new TableCell<HRCell, String>() 
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
		                HRCell cell = getTableView().getItems().get(getIndex());
		                if(cell.getEmployer().isActive() == false)
		                	setTextFill(Color.BLUE);
		                else
		                	setTextFill(Color.BLACK);
		            }
		        }
		    };
		});
	}

	private void loadEmployers() {

		hractEmployerTableView.getItems().clear();
		if (DataManager.i().mEmployersList != null) {
		    List<HBoxCell> employerList = new ArrayList<>();
		    employerList.add(new HBoxCell(null));
		    String filterSource = "";
			for (Employer employer : DataManager.i().mEmployersList ) {
				if(employer.isActive() == false && hractShowInactive.isSelected() == false) continue;
		    	if(employer.isDeleted() == true && hractShowDeleted.isSelected() == false) continue;
				if (hractFilterText.getText().length() > 0) {
					// build the filter source
					filterSource = employer.getId().toString() + " " 
								 + employer.getName() + " " 
								 + Utils.getDateString(employer.getLastUpdated()); 
					if (employer.getAccount() != null)
						filterSource = filterSource + " " + employer.getAccount().getName();
	            	 if (employer.getMailAddress() != null && employer.getMailAddress().getStprv2() != null)
	            		 filterSource = filterSource + " " + employer.getMailAddress().getStprv2(); 
	            	 if(employer.getPhone() != null);
            		 	filterSource = filterSource + " " + employer.getPhone(); 
            		//and check	
			    	if (filterSource.toUpperCase().contains(hractFilterText.getText().toUpperCase()) == false)
			    		continue;
				}
				hractEmployerTableView.getItems().add(new HRCell(employer));
			}
			// add the default sort
			hractEmployerTableView.getSortOrder().add(sortColumn);
	        sortColumn.setSortable(true);
		}
	}
	
	public class HBoxCell extends HBox {
		 Employer employer;
         Label lblCol1 = new Label();
         Label lblCol2 = new Label();
         Label lblCol3 = new Label();
         Label lblCol4 = new Label();

         HBoxCell(Employer er) {
              super();

              employer = er;
              
              if (employer != null) {
            	  Utils.setHBoxLabel(lblCol1, 100, false, employer.getId());
            	  Utils.setHBoxLabel(lblCol2, 100, false, employer.getLastUpdated());
            	  Utils.setHBoxLabel(lblCol3, 400, false, employer.getName());
            	  if (employer.getAccount() != null)
            		  Utils.setHBoxLabel(lblCol4, 350, false, employer.getAccount().getId().toString() + " - " + employer.getAccount().getName());
            	  else
                	  Utils.setHBoxLabel(lblCol4, 350, false, "");

            	  if(employer.isDeleted() == true)
                  {
                   	 lblCol1.setTextFill(Color.RED);
                   	 lblCol2.setTextFill(Color.RED);
                   	 lblCol3.setTextFill(Color.RED);
                   	 lblCol4.setTextFill(Color.RED);
                  } else {
                	  if(employer.isActive() == false)
                	  {
                        	 lblCol1.setTextFill(Color.BLUE);
                           	 lblCol2.setTextFill(Color.BLUE);
                           	 lblCol3.setTextFill(Color.BLUE);
                           	 lblCol4.setTextFill(Color.BLUE);
                	  } else {

                        	 lblCol1.setTextFill(Color.BLACK);
                        	 lblCol2.setTextFill(Color.BLACK);
                        	 lblCol3.setTextFill(Color.BLACK);
                        	 lblCol4.setTextFill(Color.BLACK);
                	  }
                  }
               } else {
            	  Utils.setHBoxLabel(lblCol1, 100, true, "Id");
            	  Utils.setHBoxLabel(lblCol2, 100, true, "Last Update");
            	  Utils.setHBoxLabel(lblCol3, 400, true, "Name");
            	  Utils.setHBoxLabel(lblCol4, 350, true, "Account (id - name)");
              }
              
              this.getChildren().addAll(lblCol1, lblCol2, lblCol3, lblCol4);  
        }
         
         Employer getEmployer() { 
        	 return employer;
         }
    }	
		
	public class HRCell {
		SimpleStringProperty id = new SimpleStringProperty();
		SimpleStringProperty date = new SimpleStringProperty();
		SimpleStringProperty name = new SimpleStringProperty();
		SimpleStringProperty account = new SimpleStringProperty();
		SimpleStringProperty mailState = new SimpleStringProperty();
		SimpleStringProperty phone = new SimpleStringProperty();
		Employer employer;

        public String getId() {
        	return id.get();
        }
         
        public String getDate() {
        	return date.get();
        }

        public String getName() {
        	return name.get();
        }

        public String getAccount() {
        	return account.get();
        }

        Employer getEmployer() {
        	 return employer;
        }

        public String getMailState() {
        	return mailState.get();
        }
         
        public String getPhone() {
        	return phone.get();
        }
         
       HRCell(Employer er) {
             super();

             employer = er;     
             if (employer != null) {
            	 id.set(String.valueOf(employer.getId())); 
            	 name.set(employer.getName()); 
            	 if (employer.getMailAddress() != null)
            		 mailState.set(employer.getMailAddress().getStprv2()); 
            	 phone.set(employer.getPhone());
        		 date.set(Utils.getDateString(employer.getLastUpdated()));
            	 if (employer.getAccount() != null)
            		 account.set(employer.getAccount().getName()); 
             }
       }    
   }	
		
	@FXML
	private void onAddEmployer(ActionEvent event) {
		// load the employer add popup
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/hr/ViewHREmployerAdd.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
	        // update the employers
	        //DataManager.i().updateEmployers();
	        // and reload
	        loadEmployers();
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        		
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	@FXML
	private void onRefresh(ActionEvent event) {
		updateEmployers();
	}
	
	private void updateEmployers() {
		Task<Void> task = new Task<Void>() 
		{ 
            @Override 
            protected Void call() throws Exception  
            { 
            	EtcAdmin.i().updateStatus(0.75, "Updating Employers...");
            	DataManager.i().updateEmployers();
            	EtcAdmin.i().updateStatus(0, "Ready");
                return null; 
            } 
        }; 
        task.setOnSucceeded(e -> loadEmployers()); 
        task.setOnFailed(e -> loadEmployers()); 
        new Thread(task).start(); 	
	}
	
	@FXML
	private void onFilterKeyPressed() {
		//reload and apply filter if there is one
		loadEmployers();
	}
	
	@FXML
	private void onApplyFilter() {
		//reload and apply filter if there is one
		loadEmployers();
	}
	
	@FXML
	private void onClearFilter() {
		// Clear filter and reload
		hractFilterText.setText("");
		loadEmployers();
	}
	
	@FXML
	private void onShowInactive() {
		loadEmployers();
	}
		
}


