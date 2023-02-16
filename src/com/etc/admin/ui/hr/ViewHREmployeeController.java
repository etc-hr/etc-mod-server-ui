package com.etc.admin.ui.hr;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.coresysteminfo.ViewCoreSystemInfoController;
import com.etc.admin.ui.hr.ViewHRAccountController.AccountCell;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.LogType;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.Employee;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.Person;
import com.etc.corvetto.rqs.EmployeeRequest;
import com.etc.corvetto.rqs.PersonRequest;
import com.etc.entities.CoreData;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewHREmployeeController {
	@FXML
	private TextField hreeFirstNameText;
	@FXML
	private TextField hreeLastNameText;
	@FXML
	private TextField hreeSSNText;
	@FXML
	private TextField hreeEmailText;
	@FXML
	private TextField hreeCoreIdText;
	@FXML
	private TextField hreeEmployerReferenceText;
	@FXML
	private TableView<HBoxCell> hreeEmployeeTableView;
	@FXML
	private TableView<HBoxCell> hreeSearchHistoryTableView;
	@FXML
	private ComboBox<String> hreeAccountCombo;
	@FXML
	private Button hreeSearchButton;
	@FXML
	private Button hreeSearchingButton;
	@FXML
	private Button hreeClearSearchButton;
	@FXML
	private CheckBox hreeShowInactive;
	@FXML
	private CheckBox hreeShowDeleted;
	@FXML
	private Label orLabel;
	@FXML
	private Label coreIdLabel;

	Employee selectedEmployee = null;
	List<Employee> searchEmployees = null;
	List<Person> searchPersons = null;
	ContextMenu cmSystemData = null;
	
	Logger logr = null;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		//create logger
		logr = Logger.getLogger(this.getClass().getCanonicalName());
		initControls();
		updateAccounts();
	}
	
	private void updateAccounts() {
		Task<Void> task = new Task<Void>() 
		{ 
            @Override 
            protected Void call() throws Exception  
            { 
            	EtcAdmin.i().updateStatus(0.5, "Updating Accounts...");
            	DataManager.i().updateAccounts();
            	EtcAdmin.i().updateStatus(0, "Ready");
                return null; 
            } 
        }; 
        task.setOnSucceeded(e -> loadAccounts()); 
        task.setOnFailed(e -> loadAccounts()); 
        new Thread(task).start(); 	
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
	        	if (hreeEmployeeTableView.getSelectionModel().getSelectedItems() != null) 
	        	{
	        		HBoxCell cell = hreeEmployeeTableView.getSelectionModel().getSelectedItem();
	        		Employee employee = cell.getEmployee();
	        		try {
	        			// set the coredata
	        			DataManager.i().mCoreData = (CoreData) employee;
	        			DataManager.i().mEmployee = employee;
	        			DataManager.i().mCurrentCoreDataType = SystemDataType.EMPLOYEE;
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
	
		// enable the buttons
		hreeClearSearchButton.setDisable(false);
		hreeSearchButton.setDisable(false);
		hreeSearchingButton.setVisible(false);
		hreeShowInactive.setSelected(true);
		
		//change the table placeholders
		hreeEmployeeTableView.setPlaceholder(new Label("No Current Search Results"));
		hreeSearchHistoryTableView.setPlaceholder(new Label("No Current Search History"));
		
		//limit some fields to decimal input
		Utils.setTextFieldInputDecimal(hreeCoreIdText);
		Utils.setTextFieldInputDecimal(hreeSSNText);
		
		setTableColumns();
		setHistoryTableColumns();
		
		// clear the account search when clicked 
		hreeAccountCombo.getEditor().setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) {
            	hreeAccountCombo.getEditor().setText("");
            	hreeAccountCombo.getSelectionModel().clearSelection();
            	hreeAccountCombo.setVisibleRowCount(15);
            	hreeCoreIdText.setText("");  
            	hreeEmailText.setText("");
            //	hreeSSNText.setText("");
            }
        });

		hreeFirstNameText.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) {
            	hreeEmployerReferenceText.setText("");
            	hreeCoreIdText.setText("");
            }
        });
		
		hreeLastNameText.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) {
            	hreeEmployerReferenceText.setText("");
            	hreeCoreIdText.setText("");
            }
        });
		
		hreeCoreIdText.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) {
            	hreeEmployerReferenceText.setText("");
            	hreeFirstNameText.setText("");
            	hreeLastNameText.setText("");
                hreeSSNText.setText("");
                hreeEmailText.setText("");
            	hreeAccountCombo.setValue("");
            	hreeAccountCombo.hide();
            }
        });
		
		hreeSSNText.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) {
            	hreeEmployerReferenceText.setText("");
            	hreeCoreIdText.setText("");
                hreeEmailText.setText("");
            }
        });
		
		hreeEmployerReferenceText.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) {
            	hreeCoreIdText.setText("");
            	hreeFirstNameText.setText("");
            	hreeLastNameText.setText("");
            	hreeSSNText.setText("");
                hreeEmailText.setText("");
            }
        });
		
		hreeEmailText.setOnMouseClicked(mouseClickedEvent -> {
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) {
            	hreeCoreIdText.setText("");
            	hreeFirstNameText.setText("");
            	hreeLastNameText.setText("");
            	hreeSSNText.setText("");
            	hreeEmployerReferenceText.setText("");
            }
        });
		
		//add handler for listbox selection notification
		hreeEmployeeTableView.setOnMouseClicked(mouseClickedEvent -> {
			cmSystemData.hide();
			if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) {
            	HBoxCell cell = hreeEmployeeTableView.getSelectionModel().getSelectedItem();
            	if (cell == null) return;
            	Employee employee = cell.getEmployee();
            	// reset it if it is already displaying the SSN
            	if (cell.getCol7().startsWith("XXX") == false) {
            		cell.setCol7("XXX-XX-" + employee.getPerson().getSsn().getUsrln());
            		hreeEmployeeTableView.refresh();
            		return;
            	}

				hreeEmployeeTableView.refresh();
            }
            
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
            	HBoxCell cell = hreeEmployeeTableView.getSelectionModel().getSelectedItem();
            	Employee employee = cell.getEmployee();
            	DataManager.i().mEmployee = employee;
            	
            	// set the Employer
            	if (employee.getEmployer() == null) {
	            	try { DataManager.i().mEmployer = AdminPersistenceManager.getInstance().get(Employer.class, employee.getEmployerId()); }
	            	catch(CoreException e) { logr.log(Level.SEVERE, "Exception.", e); }
	        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
            	} else 
            		DataManager.i().mEmployer = employee.getEmployer();
            	
            	// set the account
            	if (DataManager.i().mEmployer.getAccount() == null) {
            	try { DataManager.i().mAccount = AdminPersistenceManager.getInstance().get(Account.class, DataManager.i().mEmployer.getAccountId()); } 	
            	catch(CoreException e) { logr.log(Level.SEVERE, "Exception.", e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
            	} else
            		DataManager.i().mAccount = DataManager.i().mEmployer.getAccount();
            	
            	//set the parent objects
            	DataManager.i().mEmployer.setAccount(DataManager.i().mAccount);
            	DataManager.i().mEmployee.setEmployer(DataManager.i().mEmployer);

            	DataManager.i().hrSource = 2;
            	// and load the employee screen
				EtcAdmin.i().setScreen(ScreenType.EMPLOYEE, true);
				hidePopup();
            }
            if(mouseClickedEvent.getButton().equals(MouseButton.SECONDARY) && mouseClickedEvent.getClickCount() < 2) 
            {
            	if (hreeEmployeeTableView.getSelectionModel().getSelectedItem() != null)
            		cmSystemData.show(hreeEmployeeTableView, mouseClickedEvent.getScreenX(), mouseClickedEvent.getScreenY());
            }

        });
		
		//add handler for listbox selection notification
		hreeSearchHistoryTableView.setOnMouseClicked(mouseClickedEvent -> {
            if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) {
            	HBoxCell cell = hreeSearchHistoryTableView.getSelectionModel().getSelectedItem();
            	Employee employee = cell.getEmployee();
            	DataManager.i().mEmployee = employee;
            	
            	// set the Employer
            	if (employee.getEmployer() == null) {
	            	try { DataManager.i().mEmployer = AdminPersistenceManager.getInstance().get(Employer.class, employee.getEmployerId()); }
	            	catch(CoreException e) { logr.log(Level.SEVERE, "Exception.", e); }
	        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
            	} else 
            		DataManager.i().mEmployer = employee.getEmployer();
            	
            	// set the account
            	if (DataManager.i().mEmployer.getAccount() == null) {
            	try { DataManager.i().mAccount = AdminPersistenceManager.getInstance().get(Account.class, DataManager.i().mEmployer.getAccountId()); } 	
            	catch(CoreException e) { logr.log(Level.SEVERE, "Exception.", e); }
        	    catch (Exception e) {  DataManager.i().logGenericException(e); }
            	} else
            		DataManager.i().mAccount = DataManager.i().mEmployer.getAccount();
            	
            	//set the parent objects
            	DataManager.i().mEmployer.setAccount(DataManager.i().mAccount);
            	DataManager.i().mEmployee.setEmployer(DataManager.i().mEmployer);

            	DataManager.i().hrSource = 2;
            	// and load the employee screen
				EtcAdmin.i().setScreen(ScreenType.EMPLOYEE, true);
				hidePopup();
            }
        });
		
		//disable the coreid for non-staff
		 if (DataManager.i().mLocalUser.isEtcStaff() == false) {
			 orLabel.setVisible(false);
			 coreIdLabel.setVisible(false);
			 hreeCoreIdText.setVisible(false);
		 }
		 else {
			 orLabel.setVisible(true);
			 coreIdLabel.setVisible(true);
			 hreeCoreIdText.setVisible(true);
		 }		
	}
	
	private void hidePopup()
	{
		Stage stage = (Stage) hreeEmployeeTableView.getScene().getWindow();
		// hide if they are overlapping
		if (EtcAdmin.i().stageOverlapsMain(stage) == true)
			stage.hide();
	}

	private void setTableColumns() {
		//clear the default values
		hreeEmployeeTableView.getColumns().clear();

	    TableColumn<HBoxCell, String> x1 = new TableColumn<HBoxCell, String>("Core Id");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col1"));
		x1.setMinWidth(100);
		hreeEmployeeTableView.getColumns().add(x1);
		setCellFactory(x1);
		
		TableColumn<HBoxCell, String> x2 = new TableColumn<HBoxCell, String>("First Name");
		x2.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col2"));
		x2.setMinWidth(125);
		hreeEmployeeTableView.getColumns().add(x2);
		setCellFactory(x2);

	    TableColumn<HBoxCell, String> x3 = new TableColumn<HBoxCell, String>("Last Name");
		x3.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col3"));
		x3.setMinWidth(125);
		hreeEmployeeTableView.getColumns().add(x3);
		setCellFactory(x3);
		
	    TableColumn<HBoxCell, String> x8 = new TableColumn<HBoxCell, String>("Account");
		x8.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col8"));
		x8.setMinWidth(250);
		hreeEmployeeTableView.getColumns().add(x8);
		setCellFactory(x8);
		
	    TableColumn<HBoxCell, String> x4 = new TableColumn<HBoxCell, String>("Employer");
		x4.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col4"));
		x4.setMinWidth(250);
		hreeEmployeeTableView.getColumns().add(x4);
		setCellFactory(x4);
		
	    TableColumn<HBoxCell, String> x5 = new TableColumn<HBoxCell, String>("Emp. Ref");
		x5.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col5"));
		x5.setMinWidth(100);
		hreeEmployeeTableView.getColumns().add(x5);
		setCellFactory(x5);
		
	    TableColumn<HBoxCell, String> x6 = new TableColumn<HBoxCell, String>("Emp. Id");
		x6.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col6"));
		x6.setMinWidth(75);
		hreeEmployeeTableView.getColumns().add(x6);
		setCellFactory(x6);
		
	    TableColumn<HBoxCell, String> x7 = new TableColumn<HBoxCell, String>("SSN");
		x7.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col7"));
		x7.setMinWidth(100);
		hreeEmployeeTableView.getColumns().add(x7);
		setCellFactory(x7);
		
	}
	
	private void setHistoryTableColumns() {
		//clear the default values
		hreeSearchHistoryTableView.getColumns().clear();
	    TableColumn<HBoxCell, String> x1 = new TableColumn<HBoxCell, String>("Core Id");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col1"));
		x1.setSortable(false);
		x1.setMinWidth(100);
		hreeSearchHistoryTableView.getColumns().add(x1);
		setCellFactory(x1);
		
		TableColumn<HBoxCell, String> x2 = new TableColumn<HBoxCell, String>("First Name");
		x2.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col2"));
		x2.setSortable(false);
		x2.setMinWidth(125);
		hreeSearchHistoryTableView.getColumns().add(x2);
		setCellFactory(x2);

	    TableColumn<HBoxCell, String> x3 = new TableColumn<HBoxCell, String>("Last Name");
		x3.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col3"));
		x3.setSortable(false);
		x3.setMinWidth(125);
		hreeSearchHistoryTableView.getColumns().add(x3);
		setCellFactory(x3);
		
	    TableColumn<HBoxCell, String> x8 = new TableColumn<HBoxCell, String>("Account");
		x8.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col8"));
		x8.setSortable(false);
		x8.setMinWidth(250);
		hreeSearchHistoryTableView.getColumns().add(x8);
		setCellFactory(x8);
		
	    TableColumn<HBoxCell, String> x4 = new TableColumn<HBoxCell, String>("Employer");
		x4.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col4"));
		x4.setSortable(false);
		x4.setMinWidth(250);
		hreeSearchHistoryTableView.getColumns().add(x4);
		setCellFactory(x4);
		
	    TableColumn<HBoxCell, String> x5 = new TableColumn<HBoxCell, String>("Emp. Ref");
		x5.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col5"));
		x5.setSortable(false);
		x5.setMinWidth(100);
		hreeSearchHistoryTableView.getColumns().add(x5);
		setCellFactory(x5);
		
	    TableColumn<HBoxCell, String> x6 = new TableColumn<HBoxCell, String>("Emp. Id");
		x6.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col6"));
		x6.setSortable(false);
		x6.setMinWidth(75);
		hreeSearchHistoryTableView.getColumns().add(x6);
		setCellFactory(x6);
		
	    TableColumn<HBoxCell, String> x7 = new TableColumn<HBoxCell, String>("SSN");
		x7.setCellValueFactory(new PropertyValueFactory<HBoxCell,String>("col7"));
		x7.setSortable(false);
		x7.setMinWidth(100);
		hreeSearchHistoryTableView.getColumns().add(x7);
		setCellFactory(x7);
		
	}
	
	private void setCellFactory(TableColumn<HBoxCell, String>  col) {
		col.setCellFactory(column -> {
		    return new TableCell<HBoxCell, String>() {
		        @Override
		        protected void updateItem(String item, boolean empty) {
		            super.updateItem(item, empty);
		            if (item == null || empty) { 
		                setText(null);
		                setStyle("");
		            } else {
		                setText(item);
		                HBoxCell cell = getTableView().getItems().get(getIndex());
		                if (cell.getEmployee().isDeleted() == true)
		                	setTextFill(Color.RED);
		                else {
			                if (cell.getEmployee().isActive() == false)
			                	setTextFill(Color.BLUE);
			                else
			                	setTextFill(Color.BLACK);
		                }
		            }
		        }
		    };
		});
	}
	
	private void loadEmployees() {
		hreeEmployeeTableView.getItems().clear();
		if (searchEmployees != null) {
		    boolean load = true;
			Account account = DataManager.i().getAccount(hreeAccountCombo.getValue());
			for (Employee employee : searchEmployees) {
				if (employee.isActive() == false && hreeShowInactive.isSelected() == false) continue;
				if (employee.isDeleted() == true && hreeShowDeleted.isSelected() == false) continue;
				if (employee.getEmployer() == null) {  // should already have it under Xarriot
					try { employee.setEmployer(AdminPersistenceManager.getInstance().get(Employer.class, employee.getEmployerId())); }
					catch(CoreException e) { logr.log(Level.SEVERE, "Exception getting Employer for Employee Id=[" + employee.getId() + "]."); }
				    catch (Exception e) {  DataManager.i().logGenericException(e); }
				}
				load = true;
				if (load == true && 
					hreeAccountCombo.getValue() != null && 
					hreeAccountCombo.getValue().isEmpty() == false &&
					hreeCoreIdText.getText() == null || hreeCoreIdText.getText().isEmpty() == true) {
						if (employee.getEmployer().getAccount().getId().longValue() != account.getId().longValue()) load = false;
				}
				if (load == true) {
					hreeEmployeeTableView.getItems().add(new HBoxCell(employee));
					hreeSearchHistoryTableView.getItems().add(0,new HBoxCell(employee));
					if (hreeSearchHistoryTableView.getItems().size() > 100)
						hreeSearchHistoryTableView.getItems().remove(100);				}
			}
		}
	}
	
	public void loadAccounts() {
		String sName;
		//DataManager.i().getLocalAccounts();
		ObservableList<String> accountNames = FXCollections.observableArrayList();
		for (Account account: DataManager.i().mAccounts) {
			if (account.isActive() == false) continue;
			
			sName = account.getName();
			if (sName != null)
				accountNames.add(sName);
		};		
		
		// use a filterlist to wrap the account names for combobox searching later
        FilteredList<String> filteredItems = new FilteredList<String>(accountNames, p -> true);

        // add a listener for the edit control on the combobox
        // the listener will filter the list according to what is in the search box edit control
		hreeAccountCombo.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> {
            final javafx.scene.control.TextField editor = hreeAccountCombo.getEditor();
            final String selected = hreeAccountCombo.getSelectionModel().getSelectedItem();
            
            hreeAccountCombo.show();
            hreeAccountCombo.setVisibleRowCount(10);

            // Run on the GUI thread
            Platform.runLater(() -> {
                if (selected == null || !selected.equals(editor.getText())) {
                    filteredItems.setPredicate(item -> {
                        if (item.toUpperCase().contains(newvalue.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
			
		});
        
		//finally, set our filtered items as the combobox collection
		hreeAccountCombo.setItems(filteredItems);	
		
		//close the dropdown if it is showing
		hreeAccountCombo.hide();
	}
	
	@FXML
	private void onSearch() {
		//reset validate condition
		hreeAccountCombo.getEditor().setStyle(null);
		hreeAccountCombo.setStyle(null);
		// must have an account selected
		if (hreeCoreIdText.getText() == null || hreeCoreIdText.getText().isEmpty())
			if (hreeAccountCombo.getValue() == null || hreeAccountCombo.getValue().isEmpty() == true) {
				hreeAccountCombo.setStyle("-fx-background-color: red");
				hreeAccountCombo.getEditor().setStyle("-fx-background-color: red");
				//Utils.alertUser("Account Required", "Employee searches require an account to be selected");
				return;
			}
		
		if (Utils.validateTextFieldExactLength(hreeSSNText, 4) == false) return;
		
		hreeSearchButton.setVisible(false);
		hreeSearchingButton.setVisible(true);
		// do the search
		doSearch();

	}
	
	private void doSearch() {
		hreeEmployeeTableView.getItems().clear();
		// new thread
		Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            	try {
	            	// clear any existing search employees
	            	if (searchEmployees != null)
	            		searchEmployees.clear();
	            	else
	            		searchEmployees = new ArrayList<Employee>();
	            	
	            	Employee employee = null;
	            	EmployeeRequest eRequest = null;
	        		
					// if we have a coreId, do an id search
					if (hreeCoreIdText.getText() != null && hreeCoreIdText.getText().isEmpty() == false) {
						if (Utils.isNumber(hreeCoreIdText)) {
							eRequest = new EmployeeRequest();
							eRequest.setId(Long.valueOf(hreeCoreIdText.getText()));
							eRequest.setFetchInactive(true);
							employee = AdminPersistenceManager.getInstance().get(eRequest);
							if (employee != null) {
								// get the person
								PersonRequest pr = new PersonRequest();
								pr.setId(employee.getPersonId());
								pr.setFetchInactive(true);
								employee.setPerson(AdminPersistenceManager.getInstance().get(pr));
								searchEmployees.add(employee);
							} else {
								DataManager.i().insertLogEntry("Empty Employee search on core id " + hreeCoreIdText.getText(), LogType.INFO);
							}
						}
						return null;
					}
					
	        		Account searchAccount = DataManager.i().getAccount(hreeAccountCombo.getValue());

	        		// if we have an email, do the email search
					if (hreeEmailText.getText() != null && hreeEmailText.getText().isEmpty() == false) {
						eRequest = new EmployeeRequest();
						eRequest.setEmail(hreeEmailText.getText());
						eRequest.setAccountId(searchAccount.getId());
						eRequest.setFetchInactive(true);
						searchEmployees = AdminPersistenceManager.getInstance().getAll(eRequest);
						return null;
					}
					
	        		// if we have an SSN, do the SSN search
					if (hreeSSNText.getText() != null && hreeSSNText.getText().isEmpty() == false) {
						if (Utils.isNumber(hreeSSNText)) {
							eRequest = new EmployeeRequest();
							eRequest.setUsrln(hreeSSNText.getText());
							eRequest.setAccountId(searchAccount.getId());
							eRequest.setFetchInactive(true);
							searchEmployees = AdminPersistenceManager.getInstance().getAll(eRequest);
						}
						return null;
					}
					
					// if we have the employer reference, do that search
					if (hreeEmployerReferenceText.getText() != null && hreeEmployerReferenceText.getText().isEmpty() == false) {
						eRequest = new EmployeeRequest();
						eRequest.setAccountId(searchAccount.getId());
						eRequest.setEmployerReference(hreeEmployerReferenceText.getText());
						eRequest.setFetchInactive(true);
						searchEmployees = AdminPersistenceManager.getInstance().getAll(eRequest);
						return null;
					}
					
					eRequest = new EmployeeRequest();
					eRequest.setAccountId(searchAccount.getId());
					eRequest.setFetchInactive(true);
					// first name
					if (hreeFirstNameText.getText() != null && hreeFirstNameText.getText().isEmpty() == false)
						eRequest.setPersonFirstName(hreeFirstNameText.getText() + "%");
					// last name
					if (hreeLastNameText.getText() != null && hreeLastNameText.getText().isEmpty() == false)
						eRequest.setPersonLastName(hreeLastNameText.getText() + "%");
					searchEmployees = AdminPersistenceManager.getInstance().getAll(eRequest);
		            return null;
		        }catch (Exception e) {
		        	// log coreid search failures
		        	if (hreeCoreIdText.getText() != null && hreeCoreIdText.getText().isEmpty() == false) {
		        		DataManager.i().insertLogEntry("Employee search fail on core id " + hreeCoreIdText.getText(), LogType.INFO);
		        		DataManager.i().log(Level.INFO, e); 
		        	}
		        	return null;
		        }
	        }
	    };
    
	  	task.setOnScheduled(e ->  {
	  		EtcAdmin.i().setStatusMessage("Searching Employees...");
	  		EtcAdmin.i().setProgress(0.25);});
	  			
		task.setOnSucceeded(e ->  endSearch());
		task.setOnFailed(e ->  endSearch());
		
		// let Xarriot marshal the thread
    	try {
			EmsApp.getInstance().getFxQueue().put(task);
		} catch (InterruptedException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	private void endSearch() {
		loadEmployees();
  		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0);;
		// enable the buttons
		hreeClearSearchButton.setDisable(false);
		hreeSearchButton.setDisable(false);
		//hreeSearchButton.setText("Search");
		hreeSearchButton.setVisible(true);
		hreeSearchingButton.setVisible(false);
	}
	
	@FXML
	private void onClearSearch() {
		clearSearch();
	}
	
	public void clearSearch(){
		// Clear filter and reload
		hreeFirstNameText.setText("");
		hreeLastNameText.setText("");
		hreeEmployeeTableView.getItems().clear();
    	hreeEmployerReferenceText.setText("");
    	hreeCoreIdText.setText("");
    	hreeSSNText.setText("");
    	hreeSSNText.setStyle(null);
    	hreeAccountCombo.setValue("");
    	hreeAccountCombo.hide();
		// enable the buttons
		hreeClearSearchButton.setDisable(false);
		hreeSearchButton.setDisable(false);		
	}
	
	@FXML
	private void onShowInactive() {
		loadEmployees();
	}
	
	@FXML
	private void onClearSearchHistory() {
		hreeSearchHistoryTableView.getItems().clear();
	}
	
	@FXML
	private void onAccountSelectin() {
		hreeCoreIdText.setText("");
	}
	
	@FXML
	private void onCopySelected() {
    	HBoxCell cell = hreeEmployeeTableView.getSelectionModel().getSelectedItem();
    	if (cell == null) return;
    	Employee employee = cell.getEmployee();

		String data = "";
		data += employee.getId();
		data += "\t";
		if (employee.getPerson() != null) {
			data += employee.getPerson().getFirstName();
			data += "\t";
			data += employee.getPerson().getLastName();
			data += "\t";
			if (employee.getPerson().getSsn() != null) {
				data += "XXX-XX-" + employee.getPerson().getSsn().getUsrln();
				data += "\t";
			}
		}
		
		if (employee.getEmployer() != null) {
			if (employee.getEmployer().getAccount() != null) {
				data += employee.getEmployer().getAccount().getName();
				data += "\t";
			}
			data += employee.getEmployer().getName();
			data += "\t";
		}

		if (employee.getEmployerReference() != null) {
			data += employee.getEmployerReference();
			data += "\t";
		}

		if (employee.getEmployerId() != null) {
			data += employee.getEmployerId().toString();
			data += "\t";
		}

		// now that have the data in a friendly format, copy it to the system clipboard
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(data);
		clipboard.setContent(content);
		 Utils.alertUser("Selected Employee Copied", "The Selected Employee and associated data has been copied to the System Clipboard.");   	
	}
	

	//extending the listview for our additional controls
	public class HBoxCell extends HBox {
		SimpleStringProperty col1 = new SimpleStringProperty();
		SimpleStringProperty col2 = new SimpleStringProperty();
		SimpleStringProperty col3 = new SimpleStringProperty();
		SimpleStringProperty col4 = new SimpleStringProperty();
		SimpleStringProperty col5 = new SimpleStringProperty();
		SimpleStringProperty col6 = new SimpleStringProperty();
		SimpleStringProperty col7 = new SimpleStringProperty();
		SimpleStringProperty col8 = new SimpleStringProperty();
        Employee employee = null;
        int cellId;

        public void setCol7(String val) {
        	col7.set(val);
        }
        
        public String getCol1() {
       	 return col1.get();
        }
        
        public String getCol2() {
          	 return col2.get();
        }
           
        public String getCol3() {
          	 return col3.get();
        }
           
        public String getCol4() {
          	 return col4.get();
        }
           
        public String getCol5() {
          	 return col5.get();
        }
           
        public String getCol6() {
          	 return col6.get();
        }

        public String getCol7() {
         	 return col7.get();
       }

        public String getCol8() {
        	 return col8.get();
      }

        int getCellId() { 
       	 return cellId;
        }
        
        Employee getEmployee() {
       	 return employee;
        }

	    HBoxCell(Employee employee) {
	    	 super();
	
	          this.employee = employee;
	
	          col1.set(employee.getId().toString());
	          col2.set(employee.getPerson().getFirstName());
	          col3.set(employee.getPerson().getLastName());
	          col5.set(employee.getEmployerReference());
	          if (employee.getPerson().getSsn() != null)
	        	  col7.set("XXX-XX-" + employee.getPerson().getSsn().getUsrln());
	          
	          // get the employer
	          Employer employer = employee.getEmployer();
	          if (employer == null) {
				try { employer = AdminPersistenceManager.getInstance().get(Employer.class, employee.getEmployerId()); } 
				catch (CoreException e) { logr.log(Level.SEVERE, "Error getting Employer.", e); }
			    catch (Exception e) {  DataManager.i().logGenericException(e); }
	          }
	          if (employer != null) {
	        	  col6.set(employee.getEmployer().getId().toString());
	        	  col4.set(employer.getName());
	        	  if (employer.getAccount() != null && employer.getAccount().getName() != null)
	        		  col8.set(employer.getAccount().getName());
	          }
	     }
         
    }	
		
}


