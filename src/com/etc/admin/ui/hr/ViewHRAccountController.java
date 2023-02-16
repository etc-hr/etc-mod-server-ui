package com.etc.admin.ui.hr;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.ui.coresysteminfo.ViewCoreSystemInfoController;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.Employer;
import com.etc.entities.CoreData;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewHRAccountController {
	@FXML
	private TextField hractFilterText;
	@FXML
	private TextField hractEmployerFilterText;
	@FXML
	private TableView<AccountCell> hractAccountTableView;
	@FXML
	private ListView<HBoxEmployerCell> hractEmployerList;
	@FXML
	private Button hractAddAccountButton;
	@FXML
	private CheckBox hractShowInactive;
	@FXML
	private CheckBox hractShowDeleted;
	
	Account selectedAccount = null;
	TableColumn<AccountCell, String> sortColumn = null;
	ContextMenu cmSystemData = null;

	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		setTableColumns();
		loadAccounts();
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
	        	if (hractAccountTableView.getSelectionModel().getSelectedItems() != null) 
	        	{
	        		AccountCell cell = hractAccountTableView.getSelectionModel().getSelectedItem();
	        		Account account = cell.getAccount();
	        		try {
	        			// set the coredata
	        			DataManager.i().mCoreData = (CoreData) account;
	        			DataManager.i().mCurrentCoreDataType = SystemDataType.ACCOUNT;
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
	        	        	updateAccounts();
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
	
		//hide the add account if a non-staff user
		if (DataManager.i().mLocalUser != null)
			hractAddAccountButton.setVisible(DataManager.i().mLocalUser.isEtcStaff());
		
		hractAccountTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AccountCell>() {
			@Override
			public void changed(ObservableValue<? extends AccountCell> observable, AccountCell oldValue, AccountCell newValue) {
				AccountCell cell = hractAccountTableView.getSelectionModel().getSelectedItem();
            	if (cell != null) {
            		selectedAccount = cell.getAccount();
            		loadEmployers();
            	}
			}
	        });
		
		//add handler for listbox selection notification
		hractAccountTableView.setOnMouseClicked(mouseClickedEvent -> {
			cmSystemData.hide();
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
            	AccountCell cell = hractAccountTableView.getSelectionModel().getSelectedItem();
				DataManager.i().mAccount = cell.getAccount();
            	DataManager.i().hrSource = 0;
				EtcAdmin.i().setScreen(ScreenType.ACCOUNT, true);
				hidePopup();
            }
            if(mouseClickedEvent.getButton().equals(MouseButton.SECONDARY) && mouseClickedEvent.getClickCount() < 2) 
            {
            	if (hractAccountTableView.getSelectionModel().getSelectedItem() != null)
            		cmSystemData.show(hractAccountTableView, mouseClickedEvent.getScreenX(), mouseClickedEvent.getScreenY());
            }
        });
	}	
	
	private void hidePopup()
	{
		Stage stage = (Stage) hractAccountTableView.getScene().getWindow();
		// hide if they are overlapping
		if (EtcAdmin.i().stageOverlapsMain(stage) == true)
			stage.hide();
	}

	private void setTableColumns() 
	{
		//clear the default values
		hractAccountTableView.getColumns().clear();

	    TableColumn<AccountCell, String> x1 = new TableColumn<AccountCell, String>("Id");
		x1.setCellValueFactory(new PropertyValueFactory<AccountCell,String>("id"));
		x1.setMinWidth(50);
		x1.setComparator((String o1, String o2) -> { return Utils.compareNumberStrings(o1, o2); });
		hractAccountTableView.getColumns().add(x1);
		setCellFactory(x1);
		sortColumn = x1;
		sortColumn.setSortType(SortType.ASCENDING);
		
	    TableColumn<AccountCell, String> x2 = new TableColumn<AccountCell, String>("Date");
		x2.setCellValueFactory(new PropertyValueFactory<AccountCell,String>("date"));
		x2.setMinWidth(80);
		x2.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		hractAccountTableView.getColumns().add(x2);
		setCellFactory(x2);
		
	    TableColumn<AccountCell, String> x3 = new TableColumn<AccountCell, String>("Name");
		x3.setCellValueFactory(new PropertyValueFactory<AccountCell,String>("name"));
		x3.setMinWidth(350);
		hractAccountTableView.getColumns().add(x3);
		setCellFactory(x3);
		
	    TableColumn<AccountCell, String> x5 = new TableColumn<AccountCell, String>("Phone");
		x5.setCellValueFactory(new PropertyValueFactory<AccountCell,String>("phone"));
		x5.setMinWidth(125);
		hractAccountTableView.getColumns().add(x5);
		setCellFactory(x5);	
	}

	private void setCellFactory(TableColumn<AccountCell, String>  col) 
	{
		col.setCellFactory(column -> 
		{
		    return new TableCell<AccountCell, String>() 
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
		                AccountCell cell = getTableView().getItems().get(getIndex());
		                if(cell.getAccount().isActive() == false)
		                	setTextFill(Color.BLUE);
		                else
		                	setTextFill(Color.BLACK);
		            }
		        }
		    };
		});
	}

	private void updateAccounts() {
		Task<Void> task = new Task<Void>() 
		{ 
            @Override 
            protected Void call() throws Exception  
            { 
            	EtcAdmin.i().updateStatus(0.5, "Updating Accounts...");
            	DataManager.i().updateAccounts();
            	EtcAdmin.i().updateStatus(0.75, "Updating Employers...");
            	DataManager.i().updateEmployers();
            	EtcAdmin.i().updateStatus(0, "Ready");
                return null; 
            } 
        }; 
        task.setOnSucceeded(e -> loadAccounts()); 
        task.setOnFailed(e -> loadAccounts()); 
        new Thread(task).start(); 	
	}

	private void loadAccounts() {
		
		hractEmployerList.getItems().clear();
		hractAccountTableView.getItems().clear();
		
		if (DataManager.i().mAccounts != null) {
		    //accountList.add(new HBoxAccountCell(null));
		    String filterSource = "";
			for (Account account : DataManager.i().mAccounts ) {
				if(account.isActive() == false && hractShowInactive.isSelected() == false) continue;
		    	if(account.isDeleted() == true && hractShowDeleted.isSelected() == false) continue;

				if (hractFilterText.getText().length() > 0) {
					filterSource = account.getName(); 
					if (account.getPhone() != null)
						filterSource += " " + account.getPhone();
					filterSource += " " + String.valueOf(account.getId()) + " ";
									
			    	if (filterSource.toUpperCase().contains(hractFilterText.getText().toUpperCase()) == false)
			    		continue;
				}
				hractAccountTableView.getItems().add(new AccountCell(account));
			}

		}
	}
	
	private void loadEmployers() {

		if (DataManager.i().mEmployersList != null) {
		    List<HBoxEmployerCell> employerList = new ArrayList<>();
		    employerList.add(new HBoxEmployerCell(null));
			for (Employer employer : DataManager.i().mEmployersList ) {
				if (employer.getAccount() == null || employer.getAccount().getId().equals(selectedAccount.getId()) == false) continue;

				employerList.add(new HBoxEmployerCell(employer));
			}
			ObservableList<HBoxEmployerCell> myObservableList = FXCollections.observableList(employerList);
			hractEmployerList.setItems(myObservableList);			    
		}
	}

	public class AccountCell {
		SimpleStringProperty id = new SimpleStringProperty();
		SimpleStringProperty date = new SimpleStringProperty();
		SimpleStringProperty name = new SimpleStringProperty();
		SimpleStringProperty phone = new SimpleStringProperty();
		Account account;

        public String getId() {
        	return id.get();
        }
         
        public String getDate() {
        	return date.get();
        }

        public String getName() {
        	return name.get();
        }

        public Account getAccount() {
        	return account;
        }

        public String getPhone() {
        	return phone.get();
        }
         
        AccountCell(Account account) {
             super();

             this.account = account;     
             if (account != null) {
            	 id.set(String.valueOf(account.getId())); 
            	 name.set(account.getName()); 
            	 if (account.getPhone() != null)
            		 phone.set(account.getPhone());
        		 date.set(Utils.getDateString(account.getLastUpdated()));
             }
       }    
   }	
		
	public class HBoxEmployerCell extends HBox {
        Label lblCol1 = new Label();
        Label lblCol2 = new Label();
        Employer employer;

        HBoxEmployerCell(Employer employer) {
             super();

             this.employer = employer;
             if (employer != null) {
              	  Utils.setHBoxLabel(lblCol1, 300, false, employer.getName());
            	  Utils.setHBoxLabel(lblCol2, 100, false, employer.getLastUpdated());
                 if (employer.isActive() == false) {
                	 lblCol1.setTextFill(Color.BLUE);
                	 lblCol2.setTextFill(Color.BLUE);
                 }
             } else {
              	  Utils.setHBoxLabel(lblCol1, 300, false, "Name");
            	  Utils.setHBoxLabel(lblCol2, 100, false, "Last Updated");
             }
             this.getChildren().addAll(lblCol1, lblCol2);  
       }
        
        Employer getEmployer() { 
       	 return employer;
        }
    }
	
	@FXML
	private void onAddAccount(ActionEvent event) {
		// load the account add popup
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/hr/ViewHRAccountAdd.fxml"));
			Parent ControllerNode = loader.load();
	        //ViewNoteAddController noteController = (ViewNoteAddController) loader.getController();
	        //noteController.typeAccount = true;
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
	
		// update the list
	}		
	
	@FXML
	private void onRefresh(ActionEvent event) {
		updateAccounts();
	}		
	
	@FXML
	private void onApplyFilter() {
		//reload and apply filter if there is one
		loadAccounts();
	}
	
	@FXML
	private void onFilterKeyPressed() {
		//reload and apply filter if there is one
		loadAccounts();
	}
	
	@FXML
	private void onClearFilter() {
		// Clear filter and reload
		hractFilterText.setText("");
		loadAccounts();
	}
	
	@FXML
	private void onShowInactive() {
		loadAccounts();
	}
		
/*	public void setChannel(int nChannelID) {
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            	DataManager.i().loadPipelineChannel(nChannelID);
                //action
                return null;
            }
        };
        
    	task.setOnSucceeded(e ->   updatePipelineChannelData());
    	task.setOnFailed(e ->   updatePipelineChannelData());
        new Thread(task).start();
	}
	*/
}


