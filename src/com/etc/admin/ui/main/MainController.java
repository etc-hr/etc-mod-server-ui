package com.etc.admin.ui.main;

import java.util.logging.Level;

import org.controlsfx.control.BreadCrumbBar;
import org.controlsfx.control.BreadCrumbBar.BreadCrumbActionEvent;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.ui.UserInterfaceManager;
import com.etc.admin.ui.admintools.ViewAdminEmployeeToolController;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.StatusMessageType;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.Employee;
import com.etc.corvetto.entities.Employer;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MainController 
{
	//track if we are maximized
	private boolean bMaximized = false;
	@FXML
	private BreadCrumbBar<String> breadCrumbBar;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private Label statusMessage;
	//left navigation
	@FXML
	private TitledPane calculatorTitledPane;
	@FXML
	private TitledPane hrTitledPane;
	@FXML
	private TitledPane pipelineTitledPane;
	@FXML
	private TitledPane adminToolsTitledPane;
	@FXML
	private TitledPane reportsTitledPane;
	@FXML
	private TitledPane exporterTitledPane;
	@FXML
	private TitledPane documentTitledPane;
	@FXML
	private TitledPane hubTitledPane;
	// navigation links
	private Hyperlink hrAccountsLink = new Hyperlink("Accounts");
	private Hyperlink hrEmployersLink = new Hyperlink("Employers");
	private Hyperlink hrEmployeesLink = new Hyperlink("Employees");
	private Hyperlink calcQueueLink = new Hyperlink("Queue");
	private Hyperlink pipelineQueueLink = new Hyperlink("Queue");
	private Hyperlink pipelineChannelsLink = new Hyperlink("Channels");	
	private Hyperlink pipelinePatternsLink = new Hyperlink("Parse Patterns");
	private Hyperlink pipelineFormatsLink = new Hyperlink("Date Formats");
	private Hyperlink adminToolsUsersLink = new Hyperlink("Users");
	private Hyperlink adminToolsPayLink = new Hyperlink("Pay");
	private Hyperlink adminToolsEmployeesLink = new Hyperlink("Employees");
	private Hyperlink EtcFillingEventReportsLink = new Hyperlink("Etc Filing Errors");
	private Hyperlink exportQueueLink = new Hyperlink("Queue");
	private Hyperlink exportChannelsLink = new Hyperlink("Channels");
	private Hyperlink hubInstancesLink = new Hyperlink("Instances");
	private Hyperlink documentsLink = new Hyperlink("Documents");
	
	// persist keys
	String persistAccount1Key = "com.etc.admin.EmsApp.AccountId1";
	String persistAccount2Key = "com.etc.admin.EmsApp.AccountId2";
	String persistAccount3Key = "com.etc.admin.EmsApp.AccountId3";
	String persistEmployer1Key = "com.etc.admin.EmsApp.EmployerId1";
	String persistEmployer2Key = "com.etc.admin.EmsApp.EmployerId2";
	String persistEmployer3Key = "com.etc.admin.EmsApp.EmployerId3";
	String persistEmployee1Key = "com.etc.admin.EmsApp.EmployeeId1";
	String persistEmployee2Key = "com.etc.admin.EmsApp.EmployeeId2";
	String persistEmployee3Key = "com.etc.admin.EmsApp.EmployeeId3";

	// main screen
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private BorderPane borderPane;
	@FXML
	private BorderPane DisplayPane;
	@FXML
	private Label mainUserLabel;
	@FXML
	private ComboBox<String> searchBox;
	@FXML
	private Separator mnLeftSeperator;
	@FXML
	private GridPane mnGridPane;
	@FXML
	private Rectangle mnBlueLabel;
	@FXML
	private Rectangle mnStatusBackground;
	// work order tracking
	@FXML
	private Button WOTEnableButton;
	@FXML
	private Button WOTStartButton;
	@FXML
	private TextField WOTTimerField;
	@FXML
	private ComboBox<String> WOTEmployerBox;
	private Timeline timeline = null;
	private int WOTTime = 0;
	// quick links
	@FXML
	private Button dwiButton;
	@FXML
	private Button channelsButton;
	@FXML
	private Button uploaderButton;
	// history
	@FXML
	private VBox recentsVbox;
	@FXML
	private Hyperlink recentAccount1Link;
	@FXML
	private Hyperlink recentAccount2Link;
	@FXML
	private Hyperlink recentAccount3Link;
	@FXML
	private Hyperlink recentEmployer1Link;
	@FXML
	private Hyperlink recentEmployer2Link;
	@FXML
	private Hyperlink recentEmployer3Link;
	@FXML
	private Hyperlink recentEmployee1Link;
	@FXML
	private Hyperlink recentEmployee2Link;
	@FXML
	private Hyperlink recentEmployee3Link;

	// need a node manager to keep track of our nodes and controllers
	private NodeManager nodes = new NodeManager();
	
	// recents
	Account recentAccount1 = null;
	Account recentAccount2 = null;
	Account recentAccount3 = null;
	Employer recentEmployer1 = null;
	Employer recentEmployer2 = null;
	Employer recentEmployer3 = null;
	Employee recentEmployee1 = null;
	Employee recentEmployee2 = null;
	Employee recentEmployee3 = null;
	
	// Admin Employee Tool
	ViewAdminEmployeeToolController employeeToolController = null;
	Stage employeeToolStage = null;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() 
	{
		try {
			EtcAdmin.i().getPrimaryStage().setResizable(true);
			EtcAdmin.i().getPrimaryStage().setMinWidth(1400);
			EtcAdmin.i().getPrimaryStage().setMinHeight(875);
			EtcAdmin.i().setMainController(this);

			//adjust the gridpane size
			mnGridPane.setPrefWidth(EtcAdmin.i().getPrimaryStage().getWidth()-25);
			mnGridPane.setMaxWidth(EtcAdmin.i().getPrimaryStage().getWidth()-25);
			mnGridPane.setMinWidth(EtcAdmin.i().getPrimaryStage().getWidth()-25);
			mnGridPane.setPrefHeight(EtcAdmin.i().getPrimaryStage().getHeight()-50);
			mnGridPane.setMaxHeight(EtcAdmin.i().getPrimaryStage().getHeight()-50);
			mnGridPane.setMinHeight(EtcAdmin.i().getPrimaryStage().getHeight()-50);
			mnBlueLabel.setWidth(EtcAdmin.i().getPrimaryStage().getWidth());
			
			// set up the sidebar navigation controls
			setNavigationControls();
			
			//reset the state indicators
			setProgress(0);
			setStatusMessage("Ready");
	
			// set up Work Order Tracking
			WOTStartButton.setVisible(false);
			WOTTimerField.setVisible(false);
			WOTEmployerBox.setVisible(false);
			WOTEnableButton.setVisible(false);
			createWOTTimer();
	
			// a listener to adjust things when resized
			ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
			{
				mnGridPane.setPrefWidth(EtcAdmin.i().getPrimaryStage().getWidth()-25);
				mnGridPane.setMaxWidth(EtcAdmin.i().getPrimaryStage().getWidth()-25);
				mnGridPane.setMinWidth(EtcAdmin.i().getPrimaryStage().getWidth() -25);
				// set the correct maximized size to account for the Windows Task Bar
				if (bMaximized == true)
					EtcAdmin.i().setPrimaryStageMaximized();
				// now set the grid height
				mnGridPane.setPrefHeight(EtcAdmin.i().getPrimaryStage().getHeight()-45);
				mnGridPane.setMaxHeight(EtcAdmin.i().getPrimaryStage().getHeight()-45);
				mnGridPane.setMinHeight(EtcAdmin.i().getPrimaryStage().getHeight()-45);
				mnBlueLabel.setWidth(EtcAdmin.i().getPrimaryStage().getWidth());
			};
	
			//assign our listener to the stage size properties
		    EtcAdmin.i().getPrimaryStage().widthProperty().addListener(stageSizeListener);
		    EtcAdmin.i().getPrimaryStage().heightProperty().addListener(stageSizeListener); 
			
			//track if we are maximized. For some reason the stage extends below the task bar with Java on windows
			EtcAdmin.i().getPrimaryStage().maximizedProperty().addListener(new ChangeListener<Boolean>() 
			{
			    @Override
			    public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
			        bMaximized = t1.booleanValue();
			    }
			});		
			
			searchBox.getEditor().setOnMouseClicked(mouseClickedEvent -> 
			{
	            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0) 
	            {
					 searchBox.getEditor().setText("");
					 searchBox.getSelectionModel().clearSelection();
					 searchBox.setVisibleRowCount(15);
	            }
	        });
			

			//set the current user
			setLocalUserName();
			
			//load the home ui
			setScreen(ScreenType.HOME);
			
			UserInterfaceManager.setMainController(this);
			
			//remove the boxes on the links
			clearLinkBorders();
	
			//load the search string from the account collection in our application
			loadSearchAccounts();
			searchBox.setEditable(true);	
			
			// set a hook later to allow us to exit a bit more gracefully
            Platform.runLater(() -> 
            {
    			searchBox.getScene().getWindow().setOnCloseRequest(e -> onCloseRequest());
            });
			
			//add a handler for our breadCrumbBar
			breadCrumbBar.setOnCrumbAction(new EventHandler<BreadCrumbBar.BreadCrumbActionEvent<String>>() 
			{
		        @Override public void handle(BreadCrumbActionEvent<String> bae) 
		        {
		        	handleBreadCrumbSelection(bae.getSelectedCrumb().getValue());
		        }
			});	

			// update last used entries
			getPersistedEntries();
		}catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		}
		
	}
	
	// called when closing
	private void onCloseRequest() {
		// if we have independent popoups out there, close them.
		if (employeeToolStage != null)
			employeeToolStage.close();
	}
	
	private void setNavigationControls()
	{
		//VBox favContent = new VBox();
		//favContent.getChildren().add(new Hyperlink("N/A"));
		//favoritesTitledPane.setContent(favContent);		

		VBox hrContent = new VBox();
		hrContent.getChildren().add(hrAccountsLink);
		hrContent.getChildren().add(hrEmployersLink);
		hrContent.getChildren().add(hrEmployeesLink);
		hrTitledPane.setContent(hrContent);	
		hrTitledPane.setExpanded(false);
		
		VBox calcContent = new VBox();
		calcContent.getChildren().add(calcQueueLink);
		calculatorTitledPane.setContent(calcContent);		

		VBox pipelineContent = new VBox();
		pipelineContent.getChildren().add(pipelineQueueLink);
		pipelineContent.getChildren().add(pipelineChannelsLink);
		pipelineContent.getChildren().add(pipelinePatternsLink);
		pipelineContent.getChildren().add(pipelineFormatsLink);
		pipelineTitledPane.setContent(pipelineContent);		
		
		VBox adminToolsContent = new VBox();
		adminToolsContent.getChildren().add(adminToolsUsersLink);
		adminToolsContent.getChildren().add(adminToolsPayLink);
		adminToolsContent.getChildren().add(adminToolsEmployeesLink);
		adminToolsTitledPane.setContent(adminToolsContent);	
		/*if (DataManager.i().mLocalUser.getId() == 3 || // kendra
			DataManager.i().mLocalUser.getId() == 1484 || // pete
			DataManager.i().mLocalUser.getId() == 1462 || // stephen
			DataManager.i().mLocalUser.getId() == 1539 || // dave
			DataManager.i().mLocalUser.getId() == 15 || // april
			DataManager.i().mLocalUser.getId() == 3930) // greg
			adminToolsTitledPane.setDisable(false);
		else
			adminToolsTitledPane.setDisable(true);
		*/
		// only staff get the admin tools
		if (DataManager.i().mLocalUser.isEtcStaff() == true)
				adminToolsTitledPane.setDisable(false);
			else
				adminToolsTitledPane.setDisable(true);
			
		VBox reportsContent = new VBox();
		reportsContent.getChildren().add(EtcFillingEventReportsLink);
		reportsTitledPane.setContent(reportsContent);		

		VBox exportContent = new VBox();
		exportContent.getChildren().add(exportQueueLink);
		exportContent.getChildren().add(exportChannelsLink);
		exporterTitledPane.setContent(exportContent);		

		VBox hubContent = new VBox();
		hubContent.getChildren().add(hubInstancesLink);
		hubTitledPane.setContent(hubContent);		

		VBox docContent = new VBox();
		docContent.getChildren().add(documentsLink);
		documentTitledPane.setContent(docContent);		

		if (DataManager.i().mLocalUser.isEtcStaff() == false) {
			calculatorTitledPane.setVisible(false);
			pipelineTitledPane.setVisible(false);
			adminToolsTitledPane.setVisible(false);
			reportsTitledPane.setVisible(false);
			exporterTitledPane.setVisible(false);
			documentTitledPane.setVisible(false);
			hubTitledPane.setVisible(false);
			channelsButton.setVisible(false);
			dwiButton.setVisible(false);
		}

		//now for the handlers
		hrAccountsLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	hrAccountsLink.setVisited(false);
		    	onHRAccount();
		    }
		});		
		
		hrEmployersLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	hrEmployersLink.setVisited(false);
		    	onHREmployers();
		    }
		});		
		
		hrEmployeesLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	hrEmployeesLink.setVisited(false);
		    	onHREmployees();
		    }
		});		
		
		calcQueueLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	calcQueueLink.setVisited(false);
		    	onCalculationQueue();
		    }
		});		
		
		pipelineQueueLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	pipelineQueueLink.setVisited(false);
		    	onPipelineQueue();
		    }
		});		
		
		pipelineChannelsLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	pipelineChannelsLink.setVisited(false);
		    	onPipelineChannels();
		    	// tbd
		    }
		});		
		
		pipelinePatternsLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	pipelinePatternsLink.setVisited(false);
		    	onPipelineParsePatterns();
		    }
		});		
		
		pipelineFormatsLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	pipelineFormatsLink.setVisited(false);
		    	onPipelineParseDateFormats();
		    }
		});	
		
		adminToolsUsersLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	adminToolsUsersLink.setVisited(false);
		    	onAdminToolsUsers();
		    }
		});	
		
		adminToolsPayLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	adminToolsPayLink.setVisited(false);
		    	onAdminToolsPay();
		    }
		});	
		
		adminToolsEmployeesLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	adminToolsEmployeesLink.setVisited(false);
		    	onAdminToolsEmployees();
		    }
		});	
		
		EtcFillingEventReportsLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	EtcFillingEventReportsLink.setVisited(false);
		    	onEtcFilingEventErrorReport();
		    }
		});		

		exportQueueLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	exportQueueLink.setVisited(false);
		    	onExportQueue();
		    }
		});		
		
		exportChannelsLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	exportChannelsLink.setVisited(false);
		    	// tbd
		    }
		});		
		
		hubInstancesLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	hubInstancesLink.setVisited(false);
		    	// tbd
		    }
		});				
		
		documentsLink.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	documentsLink.setVisited(false);
		    	onDocument();
		    	// tbd
		    }
		});				
	}
	
	public void setProgress(double dValue) 
	{
		// make sure we are on the gui thread
		Platform.runLater(new Runnable() 
		{
		    @Override
		    public void run()
		    {    
				progressBar.setProgress(dValue);
				if (dValue == 0) 
				{
					progressBar.setVisible(false);
					statusMessage.setText("Ready");
				}
				else
					progressBar.setVisible(true);
		    }
		});			
	}
	
	public Double getProgress() 
	{
		if(progressBar != null)
			return progressBar.getProgress();
		else
			return null;
	}
	
	public void setStatusMessage(String message) {
		setStatusMessage(message, StatusMessageType.INFO);
	}

	public void setStatusMessage(String message, StatusMessageType type) {
		if (message == null) return;
		
		// make sure we are on the gui thread
		Platform.runLater(new Runnable() 
		{
		    @Override
		    public void run() 
		    {    
		 		statusMessage.setText(message);
				switch (type) 
				{
					case INFO:
						statusMessage.setStyle("-fx-background-color: transparent");
						break;
					case ATTENTION:
						statusMessage.setStyle("-fx-background-color: darkseagreen");
						break;
					case CAUTION:
						statusMessage.setStyle("-fx-background-color: beige");
						break;
					case ERROR:
						statusMessage.setStyle("-fx-background-color: red");
						break;
				}
		    }
		});			
	}
	
	// switching based on the crumb label without the extra data
	private void handleBreadCrumbSelection(String breadCrumbLabel) {
		if (breadCrumbLabel.isEmpty() || breadCrumbLabel == null) return;
		
		//get the label without the current info
		String crumbTarget = breadCrumbLabel;
		int targetEnd = breadCrumbLabel.indexOf(" (");
		if (targetEnd > 0)
			crumbTarget = breadCrumbLabel.substring(0, targetEnd);
			
		switch(crumbTarget) {
		case "Home":
			setScreen(ScreenType.HOME);
			break;
		case "HR":
			switch (DataManager.i().hrSource) {
			case 0:
				setScreen(ScreenType.HRACCOUNT);
				break;
			case 1:
				setScreen(ScreenType.HREMPLOYER);
				break;
			case 2:
				setScreen(ScreenType.HREMPLOYEE);
				break;
			default:
				setScreen(ScreenType.HRACCOUNT);
				break;
			}
			break;
		case "Account":
			setScreen(ScreenType.ACCOUNT);
			break;
		case "Employer":
			setScreen(ScreenType.EMPLOYER);
			break;
		case "Employee":
			setScreen(ScreenType.EMPLOYEE);
			break;
		case "Dependent":
			setScreen(ScreenType.SECONDARY);
			break;
		case "Contact":
			if (EtcAdmin.i().getScreenType() == ScreenType.ACCOUNTCONTACTEDIT ||
				EtcAdmin.i().getScreenType() == ScreenType.ACCOUNTCONTACTADD ||
				EtcAdmin.i().getScreenType() == ScreenType.ACCOUNTCONTACT )
				setScreen(ScreenType.ACCOUNTCONTACT);
			else
				setScreen(ScreenType.EMPLOYERCONTACT);
			break;
		case "Property":
			if (EtcAdmin.i().getScreenType() == ScreenType.ACCOUNTASSOCIATEDPROPERTYEDIT)
				setScreen(ScreenType.ACCOUNTASSOCIATEDPROPERTY);
			else
				setScreen(ScreenType.EMPLOYERASSOCIATEDPROPERTY);
			break;
		case "User":
			setScreen(ScreenType.ACCOUNTUSER);
			break;
		case "TaxYear":
			setScreen(ScreenType.TAXYEAR);
			break;
		case "TaxMonth":
			setScreen(ScreenType.TAXMONTH);
			break;
		case "Department":
			setScreen(ScreenType.DEPARTMENT);
			break;
		case "All Plans":
			setScreen(ScreenType.PLAN);
			break;
		case "All Providers":
			setScreen(ScreenType.PLANPROVIDER);
			break;
		case "All Sponsors":
			setScreen(ScreenType.PLANSPONSOR);
			break;
		case "All Carriers":
			setScreen(ScreenType.PLANCARRIER);
			break;
		case "All Plan Year Offerings":
			setScreen(ScreenType.PLANYEAROFFERING);
			break;
		case "All Plan Year Offering Plans":
			setScreen(ScreenType.PLANYEAROFFERINGPLAN);
			break;
		case "All Coverage Classes":
			setScreen(ScreenType.PLANCOVERAGECLASS);
			break;
		case "Employer Coverage Period":
			setScreen(ScreenType.EMPLOYERCOVERAGEPERIOD);
			break;
		case "Payfile":
			setScreen(ScreenType.EMPLOYERPAYFILE);
			break;
		case "Employment Period":
			setScreen(ScreenType.EMPLOYMENTPERIOD);
			break;
		case "Employee Coverage Period":
			setScreen(ScreenType.EMPLOYEECOVERAGEPERIOD);
			break;
		case "System Settings":
			setScreen(ScreenType.LOCALUSER);
			break;
		case "All Users":
			setScreen(ScreenType.USER);
			break;
		case "Pipeline Channels":
			setScreen(ScreenType.PIPELINECHANNEL);
			break;
		case "Pipeline Channel":
			setScreen(ScreenType.PIPELINECHANNEL);
			break;
		case "Pipeline Specification":
			setScreen(ScreenType.PIPELINESPECIFICATION);
			break;
		case "Mapper":
			setScreen(ScreenType.PIPELINEDYNAMICFILESPECIFICATION);
			break;
		case "All Pipeline Employee Files":
			setScreen(ScreenType.PIPELINEEMPLOYEEFILE);
			break;
		case "All Pipeline Coverage Files":
			setScreen(ScreenType.PIPELINECOVERAGEFILE);
			break;
		case "All Pipeline 1094c Files":
			setScreen(ScreenType.PIPELINEIRS1094CFILE);
			break;
		case "All Pipeline 1095c Files":
			setScreen(ScreenType.PIPELINEIRS1095CFILE);
			break;
		case "All Pipeline Plan Files":
			setScreen(ScreenType.PIPELINEPLANFILE);
			break;
		case "All Pipeline Pay Period Files":
			setScreen(ScreenType.PIPELINEPAYPERIODFILE);
			break;
		case "All Pipeline Parse Patterns":
			setScreen(ScreenType.PIPELINEPARSEPATTERN);
			break;
		case "All Pipeline Date Formats":
			setScreen(ScreenType.PIPELINEPARSEDATEFORMAT);
			break;
		case "Pipeline Queue":
			setScreen(ScreenType.PIPELINEQUEUE);
			break;
		case "Queue Detail":
			setScreen(ScreenType.PIPELINEQUEUEDETAIL);
			break;
		case "File Step Handler":
			setScreen(ScreenType.PIPELINEFILESTEPHANDLER);
			break;
		case "Raw Fields":
			setScreen(ScreenType.PIPELINERAWFIELDGRID);
			break;
		case "Uploader":
			setScreen(ScreenType.UPLOADER);
			break;
		}
	}
	
	public void loadWOTrackingEmployers() {
		// if we are already tracking something, don't update
		if (WOTStartButton.getText().equals("Stop")) return;

		String sName;
		ObservableList<String> employerNames = FXCollections.observableArrayList();		
		for (int i = 0; i < DataManager.i().mEmployers.size();i++) {
			sName = DataManager.i().mEmployers.get(i).getName();
			if (sName != null)
				employerNames.add(sName);
		};		
		
		// use a filterlist to wrap the account names for combobox searching later
        FilteredList<String> filteredItems = new FilteredList<String>(employerNames, p -> true);

        // add a listener for the edit control on the combobox
        WOTEmployerBox.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> {
            final javafx.scene.control.TextField editor = WOTEmployerBox.getEditor();
            final String selected = WOTEmployerBox.getSelectionModel().getSelectedItem();
            
            WOTEmployerBox.show();
            WOTEmployerBox.setVisibleRowCount(10);

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
		WOTEmployerBox.setItems(filteredItems);	
		
		//close the dropdown if it is showing
		WOTEmployerBox.hide();
	}
	
	private void clearLinkBorders() {

		//remove the selection boxes on the left menu
		hrAccountsLink.setBorder(Border.EMPTY);
		hrEmployersLink.setBorder(Border.EMPTY);
		hrEmployeesLink.setBorder(Border.EMPTY);
		calcQueueLink.setBorder(Border.EMPTY);
		pipelineQueueLink.setBorder(Border.EMPTY);
		pipelineChannelsLink.setBorder(Border.EMPTY);	
		pipelinePatternsLink.setBorder(Border.EMPTY);
		pipelineFormatsLink.setBorder(Border.EMPTY);
		adminToolsUsersLink.setBorder(Border.EMPTY);
		adminToolsPayLink.setBorder(Border.EMPTY);
		EtcFillingEventReportsLink.setBorder(Border.EMPTY);
		exportQueueLink.setBorder(Border.EMPTY);
		exportChannelsLink.setBorder(Border.EMPTY);
		hubInstancesLink.setBorder(Border.EMPTY);
	}
	
	public void loadSearchAccounts()
	{
		String sName;
		//DataManager.i().getLocalAccounts();
		ObservableList<String> accountNames = FXCollections.observableArrayList();
		for (Account account : DataManager.i().mAccounts) 
		{
			if(account.isActive() == false) 
				continue;
			sName = account.getName();
			if (sName != null)
				accountNames.add(sName);
		};		
		
        FXCollections.sort(accountNames);
		// use a filterlist to wrap the account names for combobox searching later
        FilteredList<String> filteredItems = new FilteredList<String>(accountNames, p -> true);

        // add a listener for the edit control on the combobox
        // the listener will filter the list according to what is in the search box edit control
		searchBox.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> 
		{
            final javafx.scene.control.TextField editor = searchBox.getEditor();
            final String selected = searchBox.getSelectionModel().getSelectedItem();
            
            searchBox.show();
            searchBox.setVisibleRowCount(10);

            // Run on the GUI thread
            Platform.runLater(() -> {
                if(selected == null || !selected.equals(editor.getText())) 
                {
                    filteredItems.setPredicate(item -> {
                        if (item.toUpperCase().contains(newvalue.toUpperCase())) 
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
		searchBox.setItems(filteredItems);	
		
		//close the dropdown if it is showing
		searchBox.hide();
	}
	
	public void setLocalUserName() {
		//set the current user
		String userName; 
		if (DataManager.i().mLocalUser != null)
			userName = DataManager.i().mLocalUser.getFirstName() + " " + DataManager.i().mLocalUser.getLastName();
		else
			userName = "Unknown";
		mainUserLabel.setText(userName);
	}

	public void onSearchHide() {
			searchAccount();
	}
	
	public void searchAccount() 
	{
		String sSelection = searchBox.getValue();
		String sName;
		
		for (int i = 0; i < DataManager.i().mAccounts.size();i++) 
		{
			Account account = DataManager.i().mAccounts.get(i);
			if (account.isActive() == false) continue;
			sName = account.getName();
			if (sName.equals(sSelection))
			{
				// set the current account
				DataManager.i().mAccount = DataManager.i().mAccounts.get(i);
				EtcAdmin.i().setScreenType(ScreenType.ACCOUNT);

				//and reload the account screen
				try {
					EtcAdmin.i().setScreen(ScreenType.ACCOUNT, true);
					//loadWOTrackingEmployers();
				//	WOTEnableButton.setVisible(true);
				} catch (Exception e) {
		        	DataManager.i().log(Level.SEVERE, e); 
				}
				setBreadCrumb();
				break;
			}
		}	
	}	

	// loads the ui according to the selected screentype
	public void setScreen(ScreenType screenType) 
	{
		String screenPath = "";
		boolean refresh = EtcAdmin.i().isScreenRefresh();
		// log it
		DataManager.i().insertLogEntry("Setting Screen Type :" + screenType.toString(), LogType.DEBUG);
		
		EtcAdmin.i().setScreenType(screenType);
		
		// set the breadcrumb
		setBreadCrumb();
		
		setStatusMessage("Ready");
		DisplayPane.requestFocus();
		
		try {
			switch (screenType) {
			case HOME:
				screenPath = "ui/home/Home.fxml";
				if (nodes.homeController == null) {
					FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource(screenPath));
					nodes.homeControllerNode = loader.load();
					nodes.homeController = (HomeController) loader.getController();
				}else {
				//	homeController.initialize();
				}
				DisplayPane.setCenter(nodes.homeControllerNode);
				return;
			case LOCALUSER:
				screenPath = "ui/localuser/ViewLocalUser.fxml";
				if (nodes.viewLocalUserController == null) {
					FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource(screenPath));
					nodes.viewLocalUserControllerNode = loader.load();
					nodes.viewLocalUserController = (ViewLocalUserController) loader.getController();
				}else {
					if (refresh == true)
						nodes.viewLocalUserController.initialize();
				}
				DisplayPane.setCenter(nodes.viewLocalUserControllerNode);
				return;
			case LOCALUSEREDIT:
				return;
			case USER:
				screenPath = "ui/user/ViewUser.fxml";
				if (nodes.viewUserController == null) {
					FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource(screenPath));
					nodes.viewUserControllerNode = loader.load();
					nodes.viewUserController = (ViewUserController) loader.getController();
				}else {
					//nodes.viewUserController.initialize();
				}
				DisplayPane.setCenter(nodes.viewUserControllerNode);
				return;
			case USEREDIT:
				screenPath = "ui/user/ViewUserEdit.fxml";
				if (nodes.viewUserEditController == null) {
					FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource(screenPath));
					nodes.viewUserEditControllerNode = loader.load();
					nodes.viewUserEditController = (ViewUserEditController) loader.getController();
				}else {
					if (refresh == true)
						nodes.viewUserEditController.initialize();
				}
				DisplayPane.setCenter(nodes.viewUserEditControllerNode);
				return;
			case USERADD:
				screenPath = "ui/user/ViewUserAdd.fxml";
				break;
			case ACCOUNTUSER:
				screenPath = "ui/accountuser/ViewAccountUser.fxml";
				if (nodes.viewAccountUserController == null) {
					FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource(screenPath));
					nodes.viewAccountUserControllerNode = loader.load();
					nodes.viewAccountUserController = (ViewAccountUserController) loader.getController();
				}else {
					if (refresh == true)
						nodes.viewAccountUserController.initialize();
				}
				DisplayPane.setCenter(nodes.viewAccountUserControllerNode);
				break;
			case ACCOUNTUSEREDIT:
/*				screenPath = "ui/accountuser/ViewAccountUserEdit.fxml";
				if (nodes.viewAccountUserEditController == null) {
					FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource(screenPath));
					nodes.viewAccountUserEditControllerNode = loader.load();
					nodes.viewAccountUserEditController = (ViewAccountUserEditController) loader.getController();
				}else {
					if (refresh == true)
						nodes.viewAccountUserEditController.initialize();
				}
				DisplayPane.setCenter(nodes.viewAccountUserEditControllerNode);
*/				break;
		// and load the screen using the screenPath
//		try {
//			//DisplayPane.getChildren().clear();
//			FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource(screenPath));
//			Parent parentNode = loader.load();
//			DisplayPane.setCenter(parentNode);
//		} catch (Exception exception) {
//	    	DataManager.i().log(Level.SEVERE, e); 
//			setStatusMessage("Error: " + exception.getMessage());
//			return;
//		}
		//update the status message
		setStatusMessage("Ready");
	}
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        
	}
	public void onExit(ActionEvent event) {
		Platform.exit();
	}
	
	public void onUsers(ActionEvent event) {
		//reset the link
		clearLinks();
		//DataManager.i().loadUser();
		EtcAdmin.i().setScreen(ScreenType.USER, true);
	}
	
	public void onPlanProviders(ActionEvent event) {
		//reset the link
		clearLinks();
		EtcAdmin.i().setScreen(ScreenType.PLANPROVIDER);
	}

	public void onUser() {	
		clearLinks();
		// load it as a popup
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/localuser/ViewLocalUser.fxml"));
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

	public void onPipelineChannels() {	
		clearLinks();
		DataManager.i().loadUpdatedPipelineChannels();
		setScreen(ScreenType.PIPELINECHANNEL);
	}

	public void onPipelineParsePatterns() {	
		clearLinks();
		DataManager.i().loadUpdatedParsePatterns();
		setScreen(ScreenType.PIPELINEPARSEPATTERN);
	}
	
	public void onPipelineParseDateFormats() {	
		clearLinks();
		DataManager.i().loadUpdatedParseDateFormats();
		setScreen(ScreenType.PIPELINEPARSEDATEFORMAT);
	}
	
	public void onAdminToolsUsers() {	
		clearLinks();
		// load it as a popup
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/admintools/ViewAdminPassword.fxml"));
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
	
	public void onAdminToolsPay() {	
		clearLinks();
		//Utils.showAlert("Not Active", "This Feature is not currently active");
		
		// load it as a popup
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/admintools/ViewAdminPayTool.fxml"));
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
	
	public void onAdminToolsEmployees() {	
		clearLinks();

		try {
			// check to see if it's already active
			if (employeeToolController != null && employeeToolStage != null) {
				employeeToolStage.show();
				employeeToolStage.toFront();
				return;
			}
			
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/admintools/ViewAdminEmployeeTool.fxml"));
			Parent ControllerNode = loader.load();
			employeeToolController = (ViewAdminEmployeeToolController) loader.getController();
			employeeToolStage = new Stage();
			employeeToolStage.initModality(Modality.NONE);
			employeeToolStage.initStyle(StageStyle.UTILITY);
			employeeToolStage.setResizable(false);
			employeeToolStage.setTitle("Admin Employee Tool");
			employeeToolStage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(employeeToolStage);
	        employeeToolStage.show();
		} catch (Exception e) {
	    	DataManager.i().log(Level.SEVERE, e); 
		}        
	}
	
	public void onEtcFilingEventErrorReport() {	
		clearLinks();
		
		// testing temp
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("pdf/ViewReportAlpha.fxml"));
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
		
		//setScreen(ScreenType.AIRFILINGEVENTERRORREPORT);
	}
	
	public void onPipelineQueue() {	
		setScreen(ScreenType.PIPELINEQUEUE);
	}
	
	public void onPipelineFileStepHandlers() {	
		clearLinks();
		try {
			DataManager.i().loadUpdatedPipelineFileStepHandlers();
		} catch (Exception e) {
        	EtcAdmin.i().setProgress(0);
        	EtcAdmin.i().setStatusMessage("Error in loading updated Step Handlers");
		}
		setScreen(ScreenType.PIPELINEFILESTEPHANDLER);
	}
	
	public void onPayFile() {	
		clearLinks();	
//		DataManager.i().loadPipelinePayFiles();
	}

	public void onCoverageFile() {	
		clearLinks();
//		DataManager.i().loadPipelineCoverageFiles();
	}

	public void onEmployeeFile() {	
		clearLinks();
	/*	try {
			//FIXME: DataManager.i().loadUpdatedEmployeeFiles();
		} catch (CoreException | InterruptedException | SQLException | ParseException e) {
	       	EtcAdmin.i().setProgress(0);
        	EtcAdmin.i().setStatusMessage("Error in loading updated Employee Files");
		}
		*/
	}

	public void onSimpleDocs(ActionEvent event) {
		//reset the link
		clearLinks();
	}

	public void onHub(ActionEvent event) {
		//reset the link
		clearLinks();
	}

	public void onPlanDetails() {	
		clearLinks();
		//FIXME: DataManager.i().loadPlans();
		EtcAdmin.i().setScreen(ScreenType.PLAN, true);
	}

	public void onPlanProviders() {	
		clearLinks();
		//FIXME: DataManager.i().loadAllPlanProviders();
		EtcAdmin.i().setScreen(ScreenType.PLANPROVIDER, true);
	}

	public void onPlanSponsors() {	
		clearLinks();
		//FIXME: DataManager.i().loadAllPlanSponsors();
		EtcAdmin.i().setScreen(ScreenType.PLANSPONSOR, true);
	}

	public void onPlanCarriers() {	
		clearLinks();
		//FIXME: DataManager.i().loadAllPlanCarriers();
		EtcAdmin.i().setScreen(ScreenType.PLANCARRIER, true);
	}

	public void onPlanYearOffering() {	
		clearLinks();
		//FIXME: DataManager.i().loadAllPlanYearOfferings();
		EtcAdmin.i().setScreen(ScreenType.PLANYEAROFFERING, true);
	}

	public void onPlanYearOfferingPlan() {	
		clearLinks();
		//FIXME: DataManager.i().loadAllPlanYearOfferingPlans();
		EtcAdmin.i().setScreen(ScreenType.PLANYEAROFFERINGPLAN, true);
	}

	public void onPlanCoverageClass() {	
		clearLinks();
		//FIXME: DataManager.i().loadAllPlanCoverageClasses();
		EtcAdmin.i().setScreen(ScreenType.PLANCOVERAGECLASS, true);
	}
	
	//clears the link from having a visited view
	public void clearLinks() {
		hrAccountsLink.setVisited(false);
		hrEmployersLink.setVisited(false);
		hrEmployeesLink.setVisited(false);
		calcQueueLink.setVisited(false);
		pipelineQueueLink.setVisited(false);
		pipelineChannelsLink.setVisited(false);	
		pipelinePatternsLink.setVisited(false);
		pipelineFormatsLink.setVisited(false);
		adminToolsUsersLink.setVisited(false);
		adminToolsPayLink.setVisited(false);
		adminToolsEmployeesLink.setVisited(false);
		EtcFillingEventReportsLink.setVisited(false);
		exportQueueLink.setVisited(false);
		exportChannelsLink.setVisited(false);
		hubInstancesLink.setVisited(false);
	}
	
	// set the BreadCrumb by the current screen type
	public void setBreadCrumb() {
		
		String crumb1 = "Home";
		String crumb2 = "";
		String crumb3 = "";
		String crumb4 = "";
		String crumb5 = "";
		String crumb6 = "";
		int crumbCount = 1;
		
		switch (EtcAdmin.i().getScreenType()) {
		case HOME:
			crumbCount = 1;
			break;
		case HRACCOUNT:
			crumb2 = "HR";
			crumb3 = "Account Search";
			crumbCount = 3;
			break;
		case HREMPLOYER:
			crumb2 = "HR";
			crumb3 = "Employer Search";
			crumbCount = 3;
			break;
		case HREMPLOYEE:
			crumb2 = "HR";
			crumb3 = "Employee Search";
			crumbCount = 3;
			break;
		case AIRFILINGEVENTEDIT:
			crumb2 = "Etc Filing Error Report";
			crumbCount = 2;
			break;
		case ACCOUNT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumbCount = 3;
			break;
		case ACCOUNTEDIT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Edit";
			crumbCount = 4;
			break;
		case ACCOUNTADD:
			crumb2 = "HR";
			crumb3 = "New Account";
			crumbCount = 3;
			break;
		case ACCOUNTFROMPIPELINEQUEUE:
			crumb2 = "Pipeline Queue";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumbCount = 3;
			break;	
		case EMPLOYER:
			crumb2 = "HR";
			if (DataManager.i().mAccount == null && DataManager.i().mEmployer != null)
				DataManager.i().mAccount = DataManager.i().mEmployer.getAccount();
			if (DataManager.i().mAccount == null)
				crumb3 = "Account ()";
			else
				crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			if (DataManager.i().mEmployer == null)
				crumb4 = "Employer ()";
			else
				crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumbCount = 4;
			break;
		case EMPLOYEREDIT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Edit";
			crumbCount = 5;
			break;
		case EMPLOYERADD:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "New Employer";
			crumbCount = 4;
			break;
		case EMPLOYERFROMPIPELINEQUEUE:
			crumb2 = "Pipeline Queue";
			crumb3 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumbCount = 3;
			break;	
		case EMPLOYEE:
			crumb2 = "HR";
			if (DataManager.i().mAccount != null)
				crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			else
				crumb3 = "Account";// (" + DataManager.i().mAccount.getName() + ")";
			//if (DataManager.i().mEmployer != null)
			//	crumb4 = "Employer (" + DataManager.i().mEmployer.getName() + ")";
			//else
			//	crumb4 = "Employer";
			crumb4 = "Employee (#" + DataManager.i().mEmployee.getId().toString() + " - " + DataManager.i().mEmployee.getPerson().getFirstName() + 
					 " " + DataManager.i().mEmployee.getPerson().getLastName() + ")";
			crumbCount = 4;
			break;
		case EMPLOYEEEDIT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Employee (" + DataManager.i().mEmployee.getPerson().getFirstName() + 
					 " " + DataManager.i().mEmployee.getPerson().getLastName() + ")";
			crumb6 = "Edit";
			crumbCount = 6;
			break;
		case EMPLOYEEADD:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "New Employee";
			crumbCount = 5;
			break;
		case EMPLOYEEFROMTAXYEAR:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "TaxYear (" + String.valueOf(DataManager.i().mTaxYear.getYear()) + ")";
			crumb6 = "Employee (" + DataManager.i().mEmployee.getPerson().getFirstName() + 
					 " " + DataManager.i().mEmployee.getPerson().getLastName() + ")";
			crumbCount = 6;
			break;
		case EMPLOYEEMERGE:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employee Merge";// + DataManager.i().mCalculationSpecification.getName() + ")";
			crumbCount = 4;
			break;
		case DOCUMENT:
			crumb2 = "Document";
			crumbCount = 2;
			break;
		case SECONDARY:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Employee (" + DataManager.i().mEmployee.getPerson().getFirstName() + 
					 " " + DataManager.i().mEmployee.getPerson().getLastName() + ")";
			crumb6 = "Dependent (#" + DataManager.i().mDependent.getId().toString() + " - " + DataManager.i().mDependent.getPerson().getFirstName() + 
					 " " + DataManager.i().mDependent.getPerson().getLastName() + ")";
			crumbCount = 6;
			break;
		case SECONDARYEDIT:
			crumb2 = "Account (" + DataManager.i().mAccount.getName() + ")";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employee (" + DataManager.i().mEmployee.getPerson().getFirstName() + 
					 " " + DataManager.i().mEmployee.getPerson().getLastName() + ")";
			crumb5 = "Dependent (#" + DataManager.i().mDependent.getId().toString() + " - " + DataManager.i().mDependent.getPerson().getFirstName() + 
					 " " + DataManager.i().mDependent.getPerson().getLastName() + ")";
			crumb6 = "Edit";
			crumbCount = 6;
			break;
		case SECONDARYADD:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Employee (" + DataManager.i().mEmployee.getPerson().getFirstName() + 
					 " " + DataManager.i().mEmployee.getPerson().getLastName() + ")";
			crumb6 = "New Dependent";
			crumbCount = 6;
			break;
		case UPLOADER:
			crumb2 = "Uploader";
			crumbCount = 2;
			break;
		case ACCOUNTCONTACT:
		case ACCOUNTCONTACTEDIT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Contact";
			crumbCount = 5;
			break;
		case ACCOUNTCONTACTADD:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "New Contact";
			crumbCount = 4;
			break;
		case EMPLOYERCONTACT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Contact";
			crumbCount = 5;
			break;
		case EMPLOYERCONTACTEDIT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Contact";
			crumb6 = "Edit";
			crumbCount = 6;
			break;
		case EMPLOYERCONTACTADD:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "New Contact";
			crumbCount = 5;
			break;
		case ACCOUNTASSOCIATEDPROPERTY:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Property";
			crumbCount = 4;
			break;
		case ACCOUNTASSOCIATEDPROPERTYEDIT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Property";
			crumb5 = "Edit";
			crumbCount = 5;
			break;
		case EMPLOYERASSOCIATEDPROPERTY:
			crumb2 = "HR";
			crumb3 = "Account (" + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Property";
			crumbCount = 5;
			break;
		case EMPLOYERASSOCIATEDPROPERTYEDIT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Property";
			crumb6 = "Edit";
			crumbCount = 6;
			break;
		case ACCOUNTUSER:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "User (" + DataManager.i().mAccountUser.getFirstName() + " "
					+ DataManager.i().mAccountUser.getLastName() + ")";;
			crumbCount = 4;
			break;
		case ACCOUNTUSEREDIT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "User (" + DataManager.i().mAccountUser.getFirstName() + " "
					+ DataManager.i().mAccountUser.getLastName() + ")";;
			crumb5 = "Edit";
			crumbCount = 5;
			break;
		case PAYPERIOD:
			crumb2 = "HR";
			crumb3 = "Account (" + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "PayPeriod";
			crumbCount = 5;
			break;
		case TAXYEAR:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "TaxYear (" + String.valueOf(DataManager.i().mTaxYear.getYear()) + ")";
			crumbCount = 5;
			break;
		case TAXYEAREDIT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "TaxYear (" + String.valueOf(DataManager.i().mTaxYear.getYear()) + ")";
			crumb6 = "Edit";
			crumbCount = 6;
			break;
		case TAXYEARADD:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "New TaxYear";
			crumbCount = 6;
			break;
		case TAXMONTH:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "TaxYear (" + String.valueOf(DataManager.i().mTaxYear.getYear()) + ")";
			crumb6 = "TaxMonth (" + String.valueOf(DataManager.i().mTaxMonth.getMonth()) + ")";
			crumbCount = 6;
			break;
		case TAXMONTHEDIT:
			crumb2 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb4 = "TaxYear (" + String.valueOf(DataManager.i().mTaxYear.getYear()) + ")";
			crumb5 = "TaxMonth (" + String.valueOf(DataManager.i().mTaxMonth.getMonth()) + ")";
			crumb6 = "Edit";
			crumbCount = 6;
			break;
		case TAXMONTHADD:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "TaxYear (" + String.valueOf(DataManager.i().mTaxYear.getYear()) + ")";
			crumb6 = "New TaxMonth";
			crumbCount = 6;
			break;
		case EMPLOYERELIGIBILITYPERIOD:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Eligibility Period";
			crumbCount = 5;
			break;		
		case DEPARTMENT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Department";
			crumbCount = 5;
			break;
		case DEPARTMENTEDIT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Department";
			crumb6 = "Edit";
			crumbCount = 6;
			break;
		case DEPARTMENTADD:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "New Department";
			crumbCount = 5;
			break;
		case PLANSPONSOR:
			crumb2 = "All Sponsors";
			crumbCount = 2;
			break;
		case PLANSPONSOREDIT:
			crumb2 = "All Sponsors";
			crumb3 = "Edit";
			crumbCount = 3;
			break;
		case PLANSPONSORADD:
			crumb2 = "New Sponsor";
			crumbCount = 2;
			break;
		case PLANCARRIER:
			crumb2 = "All Carriers";
			crumbCount = 2;
			break;
		case PLANCARRIEREDIT:
			crumb2 = "All Carriers";
			crumb3 = "Edit";
			crumbCount = 3;
			break;
		case PLANCARRIERADD:
			crumb2 = "New Carrier";
			crumbCount = 2;
			break;
		case PLANYEAROFFERING:
			crumb2 = "All Plan Year Offerings";
			crumbCount = 2;
			break;
		case PLANYEAROFFERINGEDIT:
			crumb2 = "All Plan Year Offerings";
			crumb3 = "Edit";
			crumbCount = 3;
			break;
		case PLANYEAROFFERINGADD:
			crumb2 = "New Plan Year Offering";
			crumbCount = 2;
			break;
		case PLANYEAROFFERINGPLAN:
			crumb2 = "All Plan Year Offering Plans";
			crumbCount = 2;
			break;
		case PLANYEAROFFERINGPLANEDIT:
			crumb2 = "All Plan Year Offering Plans";
			crumb3 = "Edit";
			crumbCount = 3;
			break;
		case PLANYEAROFFERINGPLANADD:
			crumb2 = "New Plan Year Offering Plan";
			crumbCount = 2;
			break;
		case PLANCOVERAGECLASS:
			crumb2 = "All Coverage Classes";
			crumbCount = 2;
			break;
		case PLANCOVERAGECLASSEDIT:
			crumb2 = "All Coverage Classes";
			crumb3 = "Edit";
			crumbCount = 3;
			break;
		case PLANCOVERAGECLASSADD:
			crumb2 = "New Coverage Class";
			crumbCount = 2;
			break;
		case EMPLOYERPAYFILE:
		case PIPELINEPAYFILEEDITFROMEMPLOYER:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Payfile (" + DataManager.i().mPipelinePayFile.getPipelineInformation().getDocument().getFileName() + ")";
			crumbCount = 5;
			break;
		case EMPLOYMENTPERIOD:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Employee (" + DataManager.i().mEmployee.getPerson().getFirstName() + 
					 " " + DataManager.i().mEmployee.getPerson().getLastName() + ")";
			crumb6 = "Employment Period";
			crumbCount = 6;
			break;
		case EMPLOYMENTPERIODEDIT:
			crumb2 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb3 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb4 = "Employee (" + DataManager.i().mEmployee.getPerson().getFirstName() + 
					 " " + DataManager.i().mEmployee.getPerson().getLastName() + ")";
			crumb5 = "Employment Period";
			crumb6 = "Edit";
			crumbCount = 6;
			break;
		case EMPLOYMENTPERIODADD:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "New Employment Period";
			crumbCount = 5;
			break;
		case EMPLOYEECOVERAGEPERIOD:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Employee (" + DataManager.i().mEmployee.getPerson().getFirstName() + 
					 " " + DataManager.i().mEmployee.getPerson().getLastName() + ")";
			crumb6 = "Employee Coverage Period";
			crumbCount = 6;
			break;
		case EMPLOYEECOVERAGEPERIODEDIT:
			crumb2 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb3 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb4 = "Employee (" + DataManager.i().mEmployee.getPerson().getFirstName() + 
					 " " + DataManager.i().mEmployee.getPerson().getLastName() + ")";
			crumb5 = "Employee Coverage Period";
			crumb6 = "Edit";
			crumbCount = 6;
			break;
		case EMPLOYEECOVERAGEPERIODADD:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "New Employee Coverage Period";
			crumbCount = 5;
			break;
		case LOCALUSER:
			crumb2 = "System Settings";
			crumbCount = 2;
			break;
		case LOCALUSEREDIT:
			crumb2 = "System Settings";
			crumb3 = "Edit";
			crumbCount = 3;
			break;
		case USER:
			crumb2 = "All Users";
			crumbCount = 2;
			break;
		case USEREDIT:
			crumb2 = "All Users";
			crumb3 = "Edit";
			crumbCount = 3;
			break;
		case USERADD:
			crumb2 = "All Users";
			crumb3 = "New User";
			crumbCount = 3;
			break;
		case PIPELINECHANNEL:
			crumb2 = "Pipeline Channels";
			crumbCount = 2;
			//if (DataManager.i().mPipelineChannel != null ) {
			//	crumb3 = DataManager.i().mPipelineChannel.getName();
			//	crumbCount = 3;
			//}
			break;	
		case PIPELINECHANNELEDIT:
			crumb2 = "Pipeline Channels";
			crumb3 = "Edit";
			crumbCount = 3;
			//if (DataManager.i().mPipelineChannel != null ) {
			//	crumb3 = DataManager.i().mPipelineChannel.getName();
			//	crumb4 = "Edit";
			//	crumbCount = 4;
			//}
			break;	
		case PIPELINECHANNELADD:
			crumb2 = "Pipeline Channels";
			crumb3 = "New Pipeline Channel";
			crumbCount = 3;
			break;	
		case PIPELINESPECIFICATION:
			crumb2 = "Pipeline Channels";
			//crumb3 = "Pipeline Channel (" + DataManager.i().mPipelineChannel.getName() +  ")";
			crumb3 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumbCount = 3;
			break;	
		case PIPELINESPECIFICATIONFROMRAW:
			crumb2 = "Pipeline Queue";
			crumb3 = "Raw Fields";
			crumb4 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumbCount = 4;
			break;	
		case PIPELINESPECIFICATIONFROMPIPELINEQUEUE:
			crumb2 = "Pipeline Queue";
			crumb3 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumbCount = 3;
			break;	
		case PIPELINESPECIFICATIONFROMACCOUNT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumbCount = 4;
			break;	
		case PIPELINESPECIFICATIONFROMEMPLOYER:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumbCount = 5;
			break;	
		case PIPELINESPECIFICATIONEDIT:
			crumb2 = "Pipeline Channels";
			//crumb3 = "Pipeline Channel (" + DataManager.i().mPipelineChannel.getName() +  ")";
			if (DataManager.i().mPipelineSpecification != null && DataManager.i().mPipelineSpecification.getName() != null)
				crumb3 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			else
				crumb3 = "Pipeline Specification";
			crumb4 = "Edit";
			crumbCount = 4;
			break;	
		case PIPELINESPECIFICATIONADD:
			crumb2 = "Pipeline Channels";
			//crumb3 = "Pipeline Channel (" + DataManager.i().mPipelineChannel.getName() +  ")";
			crumb3 = "New Pipeline Specification)";
			crumbCount = 3;
			break;	
		case PIPELINEDYNAMICFILESPECIFICATION:
			crumb2 = "Pipeline Channels";
			//crumb3 = "Pipeline Channel (" + DataManager.i().mPipelineChannel.getName() +  ")";
			crumb3 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumb4 = "Mapper";
			crumbCount = 4;
			break;	
		case PIPELINEDYNAMICFILESPECIFICATIONFROMRAW:
			crumb2 = "Pipeline Queue";
			crumb3 = "Raw Fields";
			crumb4 = "Mapper";
			crumbCount = 4;
			break;	
		case PIPELINEDYNAMICFILESPECIFICATIONFROMQUEUE:
			crumb2 = "Pipeline Queue";
			crumb3 = "Mapper";
			crumbCount = 3;
			break;	
		case MAPPERCOVERAGE:
			crumb2 = "Pipeline Channels";
			crumb3 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumb4 = "Coverage Mapper";
			crumbCount = 4;
			break;	
		case MAPPERCOVERAGEFROMQUEUE:
			crumb2 = "Pipeline Queue";
			crumb3 = "Mapper";
			crumbCount = 3;
			break;	
		case MAPPERCOVERAGEFROMRAW:
			crumb2 = "Pipeline Queue";
			crumb3 = "Raw Fields";
			crumb4 = "Mapper";
			crumbCount = 4;
			break;	
		case MAPPERCOVERAGEFROMUPLOAD:
			crumb2 = "Uploader";
			crumb3 = "Coverage Mapper";
			crumbCount = 3;
			break;
		case MAPPEREMPLOYEE:
			crumb2 = "Pipeline Channels";
			crumb3 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumb4 = "Employee Mapper";
			crumbCount = 4;
			break;	
		case MAPPEREMPLOYEEFROMUPLOAD:
			crumb2 = "Uploader";
			crumb3 = "Employee Mapper";
			crumbCount = 3;
			break;
		case MAPPERDEDUCTION:
			crumb2 = "Pipeline Channels";
			crumb3 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumb4 = "Deduction Mapper";
			crumbCount = 4;
			break;	
		case MAPPERDEDUCTIONFROMUPLOAD:
			crumb2 = "Uploader";
			crumb3 = "Deduction Mapper";
			crumbCount = 3;
			break;
		case MAPPERINSURANCE:
			crumb2 = "Pipeline Channels";
			crumb3 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumb4 = "Insurance Mapper";
			crumbCount = 4;
			break;	
		case MAPPERIRS1095C:
			crumb2 = "Pipeline Channels";
			crumb3 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumb4 = "Irs 1095c Mapper";
			crumbCount = 4;
			break;	
		case MAPPERINSURANCEFROMUPLOAD:
			crumb2 = "Uploader";
			crumb3 = "Insurance Mapper";
			crumbCount = 3;
			break;
		case MAPPEREMPLOYEEFROMQUEUE:
			crumb2 = "Pipeline Queue";
			crumb3 = "Mapper";
			crumbCount = 3;
			break;	
		case MAPPEREMPLOYEEFROMRAW:
			crumb2 = "Pipeline Queue";
			crumb3 = "Raw Fields";
			crumb4 = "Mapper";
			crumbCount = 4;
			break;	
		case MAPPERPAY:
			crumb2 = "Pipeline Channels";
			crumb3 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumb4 = "Pay File Mapper";
			crumbCount = 4;
			break;	
		case MAPPERPAYFROMQUEUE:
			crumb2 = "Pipeline Queue";
			crumb3 = "Mapper";
			crumbCount = 3;
			break;	
		case MAPPERPAYFROMRAW:
			crumb2 = "Pipeline Queue";
			crumb3 = "Raw Fields";
			crumb4 = "Mapper";
			crumbCount = 4;
			break;	
		case MAPPERPAYFROMUPLOAD:
			crumb2 = "Uploader";
			crumb3 = "Pay Mapper";
			crumbCount = 3;
			break;
		case PIPELINEDYNAMICFILESPECIFICATIONFROMACCOUNT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Mapper";
			crumbCount = 4;
			break;	
		case PIPELINEDYNAMICFILESPECIFICATIONFROMEMPLOYER:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Mapper";
			crumbCount = 5;
			break;	
		case PIPELINEROWIGNORE:
			crumb2 = "Pipeline Channels";
			crumb3 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumb4 = "Mapper";
			crumb5 = "Ignore Row Settings";
			crumbCount = 5;
			break;	
		case PIPELINEFILESPECIFICATION:
			crumb2 = "Pipeline Channels";
			//crumb3 = "Pipeline Channel (" + DataManager.i().mPipelineChannel.getName() +  ")";
			crumb3 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumb4 = "Dyn. File Specification)";
			crumbCount = 4;
			break;	
		case PIPELINEEMPLOYEEFILE:
			crumb2 = "All Pipeline Employee Files";
			crumbCount = 2;
			break;	
		case PIPELINEEMPLOYEEFILEEDIT:
			crumb2 = "All Pipeline Employee Files";
			crumb3 = "Edit";
			crumbCount = 3;
			break;	
		case PIPELINEEMPLOYEEFILEADD:
			crumb2 = "All Pipeline Employee Files";
			crumb3 = "New Employee File";
			crumbCount = 3;
			break;	
		case PIPELINECOVERAGEFILE:
			crumb2 = "All Pipeline Coverage Files";
			crumbCount = 2;
			break;	
		case PIPELINECOVERAGEFILEEDIT:
			crumb2 = "All Pipeline Coverage Files";
			crumb3 = "Edit";
			crumbCount = 3;
			break;	
		case PIPELINECOVERAGEFILEADD:
			crumb2 = "All Pipeline Coverage Files";
			crumb2 = "New Coverage File";
			crumbCount = 3;
			break;	
		case PIPELINEIRS1094CFILE:
			crumb2 = "All Pipeline 1094C Files";
			crumbCount = 2;
			break;	
		case PIPELINEIRS1094CFILEEDIT:
			crumb2 = "All Pipeline 1094C Files";
			crumb3 = "Edit";
			crumbCount = 3;
			break;	
		case PIPELINEIRS1095CFILE:
			crumb2 = "All Pipeline 1095C Files";
			crumbCount = 2;
			break;	
		case PIPELINEIRS1095CFILEEDIT:
			crumb2 = "All Pipeline 1095C Files";
			crumb3 = "Edit";
			crumbCount = 3;
			break;	
		case PIPELINEPLANFILE:
			crumb2 = "All Pipeline Plan Files";
			crumbCount = 2;
			break;	
		case PIPELINEPLANFILEEDIT:
			crumb2 = "All Pipeline Plan Files";
			crumb3 = "Edit";
			crumbCount = 3;
			break;	
		case PIPELINEPLANFILEADD:
			crumb2 = "All Pipeline Plan Files";
			crumb3 = "New Pipeline Plan File";
			crumbCount = 3;
			break;	
		case PIPELINEPAYFILE:
			crumb2 = "All Pipeline Pay Files";
			crumbCount = 2;
			PayFile payFile = DataManager.i().mPipelinePayFile;
			if (payFile != null) {
				if (payFile.getPipelineInformation() != null)
					if (payFile.getPipelineInformation().getDocument() != null) {
						crumb3 = "Pay File (" +  payFile.getPipelineInformation().getDocument().getFileName() + ")";
						crumbCount = 3;
					}
			}
			break;	
		case PIPELINEPAYFILEEDIT:
			crumb2 = "All Pipeline Pay Files";
			crumb3 = "Edit";
			crumbCount = 3;
			PayFile payFileEdit = DataManager.i().mPipelinePayFile;
			if (payFileEdit != null) {
				if (payFileEdit.getPipelineInformation() != null)
					if (payFileEdit.getPipelineInformation().getDocument() != null) {
						crumb3 = "Pay File (" +  payFileEdit.getPipelineInformation().getDocument().getFileName() + ")";
						crumb4 = "Edit";
						crumbCount = 4;
					}
			}
			break;	
		case PIPELINEPAYFILEADD:
			crumb2 = "All Pipeline Pay Files";
			crumb3 = "New Pay File";
			crumbCount = 3;
			break;	
		case CALCULATIONQUEUE:
			crumb2 = "Calculation Queue";
			crumbCount = 2;
			break;	
		case DWIQUEUE:
			crumb2 = "DWI Queue";
			crumbCount = 2;
			break;	
		case PIPELINEQUEUE:
			crumb2 = "Pipeline Queue";
			crumbCount = 2;
			break;	
		case PIPELINEQUEUEDETAIL:
			crumb2 = "Pipeline Queue";
			crumb3 = "Queue Detail";
			crumbCount = 3;
			break;	
		case PIPELINEQUEUEDETAILEDIT:
			crumb2 = "Pipeline Queue";
			crumb3 = "Queue Entry Detail";
			crumb4 = "Detail Edit";
			crumbCount = 4;
			break;	
		case PIPELINERAWFIELDGRID:
			crumb2 = "Pipeline Queue";
			crumb3 = "Raw Fields";
			crumbCount = 3;
			break;	
		case PIPELINERAWPAY:
			crumb2 = "Pipeline Queue";
			crumb3 = "Raw Pays";
			crumbCount = 3;
			break;	
		case PIPELINERAWFIELDGRIDFROMACCOUNT:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Raw Fields";
			crumbCount = 4;
			break;	
		case PIPELINERAWFIELDGRIDFROMEMPLOYER:
			crumb2 = "HR";
			crumb3 = "Account (#" + DataManager.i().mAccount.getId().toString() + " -  " + DataManager.i().mAccount.getName() + ")";
			crumb4 = "Employer (#" + DataManager.i().mEmployer.getId().toString() + " -  " + DataManager.i().mEmployer.getName() + ")";
			crumb5 = "Raw Fields";
			crumbCount = 5;
			break;	
		case PIPELINERAWFIELDGRIDFROMUPLOADER:
			crumb2 = "Uploader";
			crumb3 = "Raw Fields";
			crumbCount = 3;
			break;	
		case PIPELINESPECIFICATIONFROMPAYFILE:
			crumb2 = "All Pipeline Pay Files";
			crumb3 = "Pay File (" + DataManager.i().mPipelinePayFile.getPipelineInformation().getDocument().getFileName() + ")";
			crumb4 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumbCount = 4;
			break;	
		case PIPELINESPECIFICATIONFROMPAYFILEEDIT:
			crumb2 = "All Pipeline Pay Files";
			crumb3 = "Pay File (" + DataManager.i().mPipelinePayFile.getPipelineInformation().getDocument().getFileName() + ")";
			crumb4 = "Pipeline Specification (#" +  DataManager.i().mPipelineSpecification.getId().toString() + " - " + DataManager.i().mPipelineSpecification.getName() + ")";
			crumb5 = "Edit";
			crumbCount = 5;
			break;	
		case PIPELINEPAYPERIODFILE:
			crumb2 = "All Pipeline Pay Period Files";
			crumbCount = 2;
			break;	
		case PIPELINEPAYPERIODFILEEDIT:
			crumb2 = "All Pipeline Pay Period Files";
			crumb3 = "Edit";
			crumbCount = 3;
			break;	
		case PIPELINEPAYPERIODFILEADD:
			crumb2 = "All Pipeline Pay Period Files";
			crumb3 = "New PayPeriod File";
			crumbCount = 3;
			break;	
		case PIPELINEPARSEPATTERN:
			crumb2 = "All Pipeline Parse Patterns";
			crumbCount = 2;
			break;	
		case PIPELINEPARSEPATTERNEDIT:
			crumb2 = "All Pipeline Parse Patterns";
			crumb3 = "Edit";
			crumbCount = 3;
			break;	
		case PIPELINEPARSEPATTERNADD:
			crumb2 = "All Pipeline Parse Patterns";
			crumb3 = "New Parse Pattern";
			crumbCount = 3;
			break;	
		case PIPELINEPARSEDATEFORMAT:
			crumb2 = "All Pipeline Date Formats";
			crumbCount = 2;
			break;	
		case PIPELINEPARSEDATEFORMATEDIT:
			crumb2 = "All Pipeline Date Formats";
			crumb3 = "Edit";
			crumbCount = 3;
			break;	
		case PIPELINEPARSEDATEFORMATADD:
			crumb2 = "All Pipeline Date Formats";
			crumb3 = "New Date Format";
			crumbCount = 3;
			break;	
		case PIPELINEFILESTEPHANDLER:
			crumb2 = "Pipeline Channels";
			crumb3 = "File Step Handler";
			crumbCount = 3;
			break;	
		case PIPELINEFILESTEPHANDLEREDIT:
			crumb2 = "Pipeline Channels";
			crumb3 = "File Step Handler";
			crumb4 = "Edit";
			crumbCount = 4;
			break;	
		case PIPELINEFILESTEPHANDLERADD:
			crumb2 = "Pipeline Channels";
			crumb3 = "New File Step Handler";
			crumbCount = 3;
			break;	
		default:
			break;			
		}
		
		TreeItem<String> breadCrumbModel = null;
		switch (crumbCount) {
		case 1:
			breadCrumbModel = BreadCrumbBar.buildTreeModel(crumb1);
			break;
		case 2:
			breadCrumbModel = BreadCrumbBar.buildTreeModel(crumb1, crumb2);
			break;
		case 3:
			breadCrumbModel = BreadCrumbBar.buildTreeModel(crumb1, crumb2, crumb3);
			break;
		case 4:
			breadCrumbModel = BreadCrumbBar.buildTreeModel(crumb1, crumb2, crumb3, crumb4);
			break;
		case 5:
			breadCrumbModel = BreadCrumbBar.buildTreeModel(crumb1, crumb2, crumb3, crumb4, crumb5);
			break;
		case 6:
			breadCrumbModel = BreadCrumbBar.buildTreeModel(crumb1, crumb2, crumb3, crumb4, crumb5, crumb6);
			break;
		}
		
		breadCrumbBar.setSelectedCrumb(breadCrumbModel);
		// log it
		DataManager.i().insertLogEntry("Setting Breadcrumb: " + crumb1 + "/" + crumb2 + "/" + crumb3 + "/" + crumb4 + "/" + crumb5 + "/" + crumb6, LogType.DEBUG);
	}
	
	private void getPersistedEntries() {
		// accounts
		String accountId3 = DataManager.i().getPersistedString(persistAccount3Key);
		if (accountId3 != null && accountId3.isEmpty() == false) {
			Account account3 = getPersistedAccount(accountId3);
			if (account3 != null) addRecentAccount(account3);
		}
		String accountId2 = DataManager.i().getPersistedString(persistAccount2Key);
		if (accountId2 != null && accountId2.isEmpty() == false) {
			Account account2 = getPersistedAccount(accountId2);
			if (account2 != null) addRecentAccount(account2);
		}
		String accountId1 = DataManager.i().getPersistedString(persistAccount1Key);
		if (accountId1 != null && accountId1.isEmpty() == false) {
			Account account1 = getPersistedAccount(accountId1);
			if (account1 != null)addRecentAccount(account1);
		}
		
		// employers
		String employerId3 = DataManager.i().getPersistedString(persistEmployer3Key);
		if (employerId3 != null && employerId3.isEmpty() == false) {
			Employer employer3 = getPersistedEmployer(employerId3);
			if (employer3 != null) addRecentEmployer(employer3);
		}
		String employerId2 = DataManager.i().getPersistedString(persistEmployer2Key);
		if (employerId2 != null && employerId2.isEmpty() == false) {
			Employer employer2 = getPersistedEmployer(employerId2);
			if (employer2 != null) addRecentEmployer(employer2);
		}
		String employerId1 = DataManager.i().getPersistedString(persistEmployer1Key);
		if (employerId1 != null && employerId1.isEmpty() == false) {
			Employer employer1 = getPersistedEmployer(employerId1);
			if (employer1 != null) addRecentEmployer(employer1);
		}
		
		// employees
		String employeeId3 = DataManager.i().getPersistedString(persistEmployee3Key);
		if (employeeId3 != null && employeeId3.isEmpty() == false) {
			Employee employee3 = getPersistedEmployee(employeeId3);
			if (employee3 != null) addRecentEmployee(employee3);
		}
		String employeeId2 = DataManager.i().getPersistedString(persistEmployee2Key);
		if (employeeId2 != null && employeeId2.isEmpty() == false) {
			Employee employee2 = getPersistedEmployee(employeeId2);
			if (employee2 != null) addRecentEmployee(employee2);
		}
		String employeeId1 = DataManager.i().getPersistedString(persistEmployee1Key);
		if (employeeId1 != null && employeeId1.isEmpty() == false) {
			Employee employee1 = getPersistedEmployee(employeeId1);
			if (employee1 != null) addRecentEmployee(employee1);
		}
		
	}
	
	private Account getPersistedAccount(String Id) {
		try {
			AccountRequest rq = new AccountRequest();
			rq.setId(Long.valueOf(Id));
			Account account = AdminPersistenceManager.getInstance().addOrUpdate(rq);
			return account;
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
		
		return null;
	}
	
	private Employer getPersistedEmployer(String Id) {
		try {
			EmployerRequest rq = new EmployerRequest();
			rq.setId(Long.valueOf(Id));
			Employer employer = AdminPersistenceManager.getInstance().addOrUpdate(rq);
			return employer;
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
		
		return null;
	}
	
	private Employee getPersistedEmployee(String Id) {
		try {
			EmployeeRequest rq = new EmployeeRequest();
			rq.setId(Long.valueOf(Id));
			Employee employee = AdminPersistenceManager.getInstance().addOrUpdate(rq);
			return employee;
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
		
		return null;
	}
	
	private void addRecentAccount(Account account)
	{
		boolean handled = false;
		// check to see if this is a repeat
		if (recentAccount1Link.getText().equals(account.getName())) return;
		
		if (recentAccount2Link.getText().equals(account.getName())) {
			handled = true;
			recentAccount2 = recentAccount1;
			recentAccount1 = account;
			
			recentAccount2Link.setText(recentAccount1Link.getText());
			recentAccount1Link.setText(account.getName());
		}
		
		if (handled == false) {
			recentAccount3 = recentAccount2;
			recentAccount2 = recentAccount1;
			recentAccount1 = account;
			
			recentAccount3Link.setText(recentAccount2Link.getText());
			recentAccount2Link.setText(recentAccount1Link.getText());
			recentAccount1Link.setText(account.getName());
		}
		
		// persist them
		if (recentAccount1 != null) DataManager.i().persistString(persistAccount1Key, recentAccount1.getId().toString());
		if (recentAccount2 != null) DataManager.i().persistString(persistAccount2Key, recentAccount2.getId().toString());
		if (recentAccount3 != null) DataManager.i().persistString(persistAccount3Key, recentAccount3.getId().toString());
		
	}
	
	public void onRecentAccount1(ActionEvent event) {
		resetRecentLinks();
		if (recentAccount1Link.getText().equals("-") || recentAccount1 == null) return;
		gotoAccount(recentAccount1);
	}

	public void onRecentAccount2(ActionEvent event) {
		resetRecentLinks();
		if (recentAccount2Link.getText().equals("-") || recentAccount2 == null) return;
		gotoAccount(recentAccount2);
	}

	public void onRecentAccount3(ActionEvent event) {
		resetRecentLinks();
		if (recentAccount3Link.getText().equals("-") || recentAccount3 == null) return;
		gotoAccount(recentAccount3);
	}
	
	private void addRecentEmployer(Employer employer)
	{
		boolean handled = false;
		// check to see if this is a repeat
		if (recentEmployer1Link.getText().equals(employer.getName())) return;
		
		if (recentEmployer2Link.getText().equals(employer.getName())) {
			handled = true;
			recentEmployer2 = recentEmployer1;
			recentEmployer1 = employer;
			
			recentEmployer2Link.setText(recentEmployer1Link.getText());
			recentEmployer1Link.setText(employer.getName());
		}
		
		if (handled == false) {
			recentEmployer3 = recentEmployer2;
			recentEmployer2 = recentEmployer1;
			recentEmployer1 = employer;
			
			recentEmployer3Link.setText(recentEmployer2Link.getText());
			recentEmployer2Link.setText(recentEmployer1Link.getText());
			recentEmployer1Link.setText(employer.getName());
		}
		
		// persist them
		if (recentEmployer1 != null) DataManager.i().persistString(persistEmployer1Key, recentEmployer1.getId().toString());
		if (recentEmployer2 != null) DataManager.i().persistString(persistEmployer2Key, recentEmployer2.getId().toString());
		if (recentEmployer3 != null) DataManager.i().persistString(persistEmployer3Key, recentEmployer3.getId().toString());
		
	}
	
	public void onRecentEmployer1(ActionEvent event) {
		resetRecentLinks();
		if (recentEmployer1Link.getText().equals("-") || recentEmployer1 == null) return;
		gotoEmployer(recentEmployer1);
	}

	public void onRecentEmployer2(ActionEvent event) {
		resetRecentLinks();
		if (recentEmployer2Link.getText().equals("-") || recentEmployer2 == null) return;
		gotoEmployer(recentEmployer2);
	}

	public void onRecentEmployer3(ActionEvent event) {
		resetRecentLinks();
		if (recentEmployer3Link.getText().equals("-") || recentEmployer3 == null) return;
		gotoEmployer(recentEmployer3);
	}

	private void addRecentEmployee(Employee employee)
	{
		boolean handled = false;
		// check to see if this is a repeat
		if (recentEmployee1Link.getText().equals(employee.getPerson().getLastName() + ", " + employee.getPerson().getFirstName())) return;
		
		if (recentEmployee2Link.getText().equals(employee.getPerson().getLastName() + ", " + employee.getPerson().getFirstName())) {
			handled = true;
			recentEmployee2 = recentEmployee1;
			recentEmployee1 = employee;
			
			recentEmployee2Link.setText(recentEmployee1Link.getText());
			recentEmployee1Link.setText(employee.getPerson().getLastName() + ", " + employee.getPerson().getFirstName());
		}
		
		if (handled == false) {
			recentEmployee3 = recentEmployee2;
			recentEmployee2 = recentEmployee1;
			recentEmployee1 = employee;
			
			recentEmployee3Link.setText(recentEmployee2Link.getText());
			recentEmployee2Link.setText(recentEmployee1Link.getText());
			recentEmployee1Link.setText(employee.getPerson().getLastName() + ", " + employee.getPerson().getFirstName());
		}

		// persist them
		if (recentEmployee1 != null) DataManager.i().persistString(persistEmployee1Key, recentEmployee1.getId().toString());
		if (recentEmployee2 != null) DataManager.i().persistString(persistEmployee2Key, recentEmployee2.getId().toString());
		if (recentEmployee3 != null) DataManager.i().persistString(persistEmployee3Key, recentEmployee3.getId().toString());
	}
	
	public void onRecentEmployee1(ActionEvent event) {
		resetRecentLinks();
		if (recentEmployee1Link.getText().equals("-") || recentEmployee1 == null) return;
		gotoEmployee(recentEmployee1);
	}

	public void onRecentEmployee2(ActionEvent event) {
		resetRecentLinks();
		if (recentEmployee2Link.getText().equals("-") || recentEmployee2 == null) return;
		gotoEmployee(recentEmployee2);
	}

	public void onRecentEmployee3(ActionEvent event) {
		resetRecentLinks();
		if (recentEmployee3Link.getText().equals("-") || recentEmployee3 == null) return;
		gotoEmployee(recentEmployee3);
	}

	private void resetRecentLinks() {
		recentAccount1Link.setVisited(false);
		recentAccount2Link.setVisited(false);
		recentAccount3Link.setVisited(false);
		recentEmployer1Link.setVisited(false);
		recentEmployer2Link.setVisited(false);
		recentEmployer3Link.setVisited(false);
		recentEmployee1Link.setVisited(false);
		recentEmployee2Link.setVisited(false);
		recentEmployee3Link.setVisited(false);
	}

	private void gotoAccount(Account account) {
		try {
			DataManager.i().mAccount = account;
			EtcAdmin.i().setScreen(ScreenType.ACCOUNT, true);
			setBreadCrumb();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	}

	private void gotoEmployer(Employer employer) {
		try {
			DataManager.i().mAccount = employer.getAccount();
			DataManager.i().mEmployer = employer;
			EtcAdmin.i().setScreen(ScreenType.EMPLOYER, true);
			setBreadCrumb();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	}

	private void gotoEmployee(Employee employee) {
		try {
			DataManager.i().mAccount = employee.getEmployer().getAccount();
			DataManager.i().mEmployer = employee.getEmployer();
			DataManager.i().mEmployee = employee;
			EtcAdmin.i().setScreen(ScreenType.EMPLOYEE, true);
			setBreadCrumb();
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	}

	
	private void createWOTTimer() {
	    timeline = new Timeline(new KeyFrame(Duration.seconds(1),
	            new EventHandler<ActionEvent>()
	            {
	                @Override
	                public void handle(ActionEvent event)
	                {
	                	WOTTime++;
	                	String elapsed = String.format("%02d:%02d:%02d", 
	                		    TimeUnit.SECONDS.toHours(WOTTime),
	                		    TimeUnit.SECONDS.toMinutes(WOTTime) % 60,
	                		    WOTTime % 60);
	                    WOTTimerField.setText(elapsed);
	                }
	            }));

	    timeline.setCycleCount(Animation.INDEFINITE);
	}
	
	// Work Order Tracking
	public void onWOTracking() {	
		// if we are already tracking something, don't do anything
		if (WOTStartButton.getText().equals("Stop")) return;
		
		if (WOTStartButton.isVisible()) {
			WOTEnableButton.setText("Work Order Tracking >");
			WOTStartButton.setVisible(false);
			WOTTimerField.setVisible(false);
			WOTEmployerBox.setVisible(false);
			WOTTime = 0;
			WOTTimerField.setText("");
			WOTStartButton.setText("Start");
		} else {
			WOTEnableButton.setText("< Close Tracking");
			WOTStartButton.setVisible(true);
			WOTTimerField.setVisible(true);
			WOTEmployerBox.setVisible(true);
		}
	}

	public void onWOTrackingStart() {	
		//make sure we have an account selected
		if (WOTEmployerBox.getValue() == null) {
			Utils.showAlert("Work Order Tracking Error", "Please select an employer from the dropdown before starting");
			return;
		}
		if (WOTStartButton.getText().equals("Start") || WOTStartButton.getText().equals("Continue")) {
			WOTStartButton.setText("Stop");
			WOTEnableButton.setText("Currently Tracking...");
			WOTEnableButton.setDisable(true);
			WOTEmployerBox.setDisable(true);
			//start the timer
		    timeline.play();			
		} else {
			if (WOTTime > 0)
				WOTStartButton.setText("Continue");
			else
				WOTStartButton.setText("Start");
			//stop the timer
		    timeline.stop();			
			WOTEnableButton.setText("< Close Tracking");
			WOTEnableButton.setDisable(false);
			WOTEmployerBox.setDisable(false);
		}
	}

	public AnchorPane getAnchorPane() {
		return anchorPane;
	}

	public BorderPane getBorderPane() {
		return DisplayPane;
	}
	
	//left sidebar state
	public enum sidebarState{
		HOME,
		USERUTIL,
		PLAN,
		PIPELINE,
		FILES,
		FILESPECS
	}
}
