package com.etc.admin.ui.uploader;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.AutoCompletionBinding.ISuggestionRequest;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.account.FileHistoryData;
import com.etc.admin.ui.pipeline.queue.PipelineQueue;
import com.etc.admin.ui.pipeline.queue.QueueData;
import com.etc.admin.ui.pipeline.raw.RawFieldData;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.PipelineChannel;
import com.etc.corvetto.ems.pipeline.entities.PipelineRequest;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;
import com.etc.corvetto.ems.pipeline.entities.c94.Irs1094cFile;
import com.etc.corvetto.ems.pipeline.entities.c95.Irs1095cFile;
import com.etc.corvetto.ems.pipeline.entities.cov.CoverageFile;
import com.etc.corvetto.ems.pipeline.entities.ded.DeductionFile;
import com.etc.corvetto.ems.pipeline.entities.emp.EmployeeFile;
import com.etc.corvetto.ems.pipeline.entities.ins.InsuranceFile;
import com.etc.corvetto.ems.pipeline.entities.pay.PayFile;
import com.etc.corvetto.ems.pipeline.entities.ppd.PayPeriodFile;
import com.etc.corvetto.ems.pipeline.rqs.PipelineChannelRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineRequestRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.c94.Irs1094cFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.c95.Irs1095cFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.cov.CoverageFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.ded.DeductionFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.EmployeeFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.ins.InsuranceFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.PayFileRequest;
import com.etc.corvetto.ems.pipeline.rqs.ppd.PayPeriodFileRequest;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.UploadType;
import com.etc.corvetto.rqs.AccountRequest;
import com.etc.corvetto.rqs.EmployerRequest;
import com.etc.corvetto.rqs.UploadRequest;
import com.etc.corvetto.rqs.UploadTypeRequest;
import com.etc.corvetto.rqs.UserRequest;
import com.etc.entities.CoreData;
import com.etc.entities.DataProperty;
import com.etc.entities.DataPropertyType;
import com.etc.utils.crypto.Cryptographer;
import com.etc.utils.crypto.CryptographyException;
import com.etc.utils.file.FileGlobber;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class UploadForm implements Callback<AutoCompletionBinding.ISuggestionRequest, Collection<String>>
{
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
	private VBox mainVBox;
	
	@FXML
	private VBox topSectionVBox;
	
	@FXML
	private VBox bottomSectionVBox;
	
    @FXML
    private Label message;

    @FXML
    private ComboBox<String> accountCombo;

    @FXML
    private ComboBox<HBoxEmployerCell> employerCombo;

    @FXML
    private TextField fileLocation;
    
    @FXML
    private TextField dataType1Name;
    
    @FXML
    private TextField dataType1Value;
    
    @FXML
    private TextField dataType2Name;
    
    @FXML
    private TextField dataType2Value;
    
    @FXML
    private TextField dataType3Name;
    
    @FXML
    private TextField dataType3Value;
    
    @FXML
	private TextField acctFileHistoryFilterField;
    
    @FXML
    private VBox dataTypeVBox;

    @FXML
    private Button browse;

    @FXML
    private CheckBox encrypted;

    @FXML
    private Button upload;

    @FXML
    private Button cancel;

    @FXML
    private Button accountClear;

    @FXML
    private Label specs;

    @FXML
    private ProgressBar progress;

    @FXML
    private ComboBox<String> type;

//    @FXML
//	private ComboBox<String> pqueQueueRange;

    @FXML
	private TableView<HBoxFileHistoryCell> acctFileHistoryList;

//    @FXML
//	private Label dateRangeLabel;

    private FileChooser fileChooser = new FileChooser();
    private File file = null;
//    private AutoCompletionBinding<String> autoCompletionBinding = null;
    private ArrayList<String> filteredAccountList = new ArrayList<String>(0);
    private List<Long> currentAccountListIds = new ArrayList<Long>();
//    private ArrayList<String> filteredEmployerList = new ArrayList<String>(0);
    private List<Long> currentEmployerListIds = new ArrayList<Long>();
	private boolean isUploading = false;
	private boolean acctFirst = false;
	private boolean acctSec = false;
	private boolean showUpdFileHist = false;
	Date day45 = new Date();
//    private int hourRange = 24;
//    private int range = 0;

	Logger logr = DataManager.i().mLogger;
	Account account = null;
	UploadType uploadType = null;
	Employer employer = null;
	QueueData queueData = new QueueData();
	RawFieldData rawData = new RawFieldData();
//	public List<PipelineQueue> mPipelineQueue =  new ArrayList<PipelineQueue>();

	//keep track of our connection
	boolean bConnected = true;
	Calendar firstDisconnect;

	List<Employer> employers = null;
	List<UploadType> uploadTypes = null;

    //table sort date
	TableColumn<HBoxFileHistoryCell, String> sortColumn = null;
	
	//file history queue
	List<PipelineQueue> fileHistoryQueue = new ArrayList<PipelineQueue>();	

	//file history data object
	FileHistoryData fileHistoryData = new FileHistoryData();
	
	//selected fileHistory queue
	PipelineQueue selectedFileHistoryQueue = new PipelineQueue();

    @FXML
    public void initialize()
    {
    	VBox.setVgrow(dataTypeVBox, Priority.ALWAYS);
    	VBox.setVgrow(topSectionVBox, Priority.ALWAYS);
    	VBox.setVgrow(bottomSectionVBox, Priority.ALWAYS);

    	initControls();
    	setUploadColumns();
    	updateData();
    	cancel.setDisable(false);
    	DataManager.i().mAccount = null;
    	isUploading = false;
		accountCombo.setOpacity(0.5);
		employerCombo.setOpacity(0.5);
		accountCombo.setEditable(false);
		employerCombo.setEditable(false);
//		pqueQueueRange.setVisible(false);
//		dateRangeLabel.setVisible(false);
    }

    private void initControls()
    {
    	//CREATE BACKGROUND TASK
    	LoadingTask ltask = new LoadingTask();
    	new Thread(ltask).start();

//		accountCombo.getEditor().setOnMouseClicked(mouseClickedEvent -> 
//		{
//            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0)
//            {
//            	 accountCombo.getEditor().setText("");
//            	 accountCombo.getSelectionModel().clearSelection();
//            	 accountCombo.setVisibleRowCount(15);
//            }
//        });

//		accountCombo.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> 
//		{
//			if(accountCombo.getSelectionModel().getSelectedItem() != null)
//				return;
//            // Run on the GUI thread
//            Platform.runLater(() -> 
//            {
//    			loadSearchAccounts();
//    			accountCombo.show();
//            }); 
//		});

//		employerCombo.getEditor().setOnMouseClicked(mouseClickedEvent -> 
//		{
//            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 0)
//            {
//            	loadSearchEmployers();
//            	employerCombo.getEditor().setText("");
//            	employerCombo.getSelectionModel().clearSelection();
//            	employerCombo.setVisibleRowCount(15);
//            	employerCombo.hide();
//            }
//        });

		
//		employerCombo.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> 
//		{
//			if(employerCombo.getSelectionModel().getSelectedItem() != null)
//				return;
//            // Run on the GUI thread
//            Platform.runLater(() -> 
//            {
//    			loadSearchEmployers();
//    			employerCombo.show();
//            }); 
//		});

//		employerCombo.setConverter(new StringConverter<Employer>() 
//		{
//			@Override
//			public String toString(Employer u)
//			{
//				if(u.getSpecificationId() != null)
//				{
//					return u.getName() + " :: " + (u.getSpecification() != null ? u.getSpecification().getId().toString() : "--") + " :: " +
//							(u.getSpecification() != null ? u.getSpecification().getDynamicFileSpecificationId() != null ? 
//									u.getSpecification().getDynamicFileSpecificationId() : "--" : "--");
//				} else {
//					return null;
//				}
//			}
//			@Override
//			public Employer fromString(String s)
//			{
//				return type.getItems().stream().filter(_uploadType -> 
//				_uploadType.getName().equalsIgnoreCase(s.substring(0, s.indexOf(":")-1))).findFirst().orElse(null);
//			}
//		});

		//set the ranges for the queue
//		String ranges[] = { "24", "48", "72", "96", "120", "144", "168" }; 
//		pqueQueueRange.setItems(FXCollections.observableArrayList(ranges));
//		pqueQueueRange.getSelectionModel().select("96");

		// FILE HISTORY
		acctFileHistoryList.setOnMouseClicked(mouseClickedEvent -> 
		{
	        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1)
	        {
            	if(selectFileHistoryObjects() == false) return;
				// set the request and refresh the screen
				HBoxFileHistoryCell cellItem = acctFileHistoryList.getSelectionModel().getSelectedItem();
				if(cellItem != null)
				{
					 DataManager.i().mPipelineQueueEntry = fileHistoryQueue.get(cellItem.queueLocation);
					 //if(DataManager.i().mPipelineQueueEntry.getRecords() > 0 || DataManager.i().mPipelineQueueEntry.getUnits() > 0)
					 EtcAdmin.i().setScreen(ScreenType.PIPELINERAWFIELDGRIDFROMUPLOADER, true);
				}else 
					 System.out.println("Null CellItem on acctFileHistoryList in Queue");
	        }
        });
    	setUploadColumns();

    	fileLocation.setEditable(false);
    	fileChooser.setTitle("Select a file...");
    }
	
	private void setUploadColumns() 
	{
		//clear the default values
		acctFileHistoryList.getColumns().clear();

	    TableColumn<HBoxFileHistoryCell, String> result = new TableColumn<HBoxFileHistoryCell, String>("Result");
	    result.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("result"));
	    result.setMinWidth(250);
		acctFileHistoryList.getColumns().add(result);
		result.setCellFactory(new Callback<TableColumn<HBoxFileHistoryCell, String>,TableCell<HBoxFileHistoryCell, String>>()
        {
            public TableCell<HBoxFileHistoryCell, String> call(TableColumn<HBoxFileHistoryCell, String> param) 
            {
                return new TableCell<HBoxFileHistoryCell, String>() 
                {
                    @Override
                    protected void updateItem(String item, boolean empty)
                    {
                        if(!empty) 
                        {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getResult();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex).getStatus()));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });

		TableColumn<HBoxFileHistoryCell, String> colUser = new TableColumn<HBoxFileHistoryCell, String>("User");
	    colUser.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("user"));
	    colUser.setMinWidth(80);
		acctFileHistoryList.getColumns().add(colUser);
		colUser.setCellFactory(new Callback<TableColumn<HBoxFileHistoryCell, String>,TableCell<HBoxFileHistoryCell, String>>()
        {
            public TableCell<HBoxFileHistoryCell, String> call(TableColumn<HBoxFileHistoryCell, String> param)
            {
                return new TableCell<HBoxFileHistoryCell, String>() 
                {
                    @Override
                    protected void updateItem(String item, boolean empty) 
                    {
                        if(!empty) 
                        {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getUser();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex).getStatus()));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxFileHistoryCell, String> colDate = new TableColumn<HBoxFileHistoryCell, String>("Date");
		colDate.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("dateTime"));
		colDate.setMinWidth(140);

		//initial sort is by date
		sortColumn = colDate;
		colDate.setComparator((String o1, String o2) -> { return Utils.compareDateStrings(o1, o2); });
		sortColumn.setSortType(SortType.DESCENDING);
		acctFileHistoryList.getColumns().add(colDate);
		colDate.setCellFactory(new Callback<TableColumn<HBoxFileHistoryCell, String>,TableCell<HBoxFileHistoryCell, String>>()
        {
            public TableCell<HBoxFileHistoryCell, String> call(TableColumn<HBoxFileHistoryCell, String> param) 
            {
                return new TableCell<HBoxFileHistoryCell, String>() 
                {
                    @Override
                    protected void updateItem(String item, boolean empty) 
                    {
                        if(!empty) 
                        {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getDate();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex).getStatus()));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
	    TableColumn<HBoxFileHistoryCell, String> colStatus = new TableColumn<HBoxFileHistoryCell, String>("Status");
	    colStatus.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("status"));
	    colStatus.setMinWidth(125);
		acctFileHistoryList.getColumns().add(colStatus);
		colStatus.setCellFactory(new Callback<TableColumn<HBoxFileHistoryCell, String>,TableCell<HBoxFileHistoryCell, String>>()
        {
            public TableCell<HBoxFileHistoryCell, String> call(TableColumn<HBoxFileHistoryCell, String> param) 
            {
                return new TableCell<HBoxFileHistoryCell, String>() 
                {
                    @Override
                    protected void updateItem(String item, boolean empty) 
                    {
                        if(!empty) 
                        {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getStatus();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex).getStatus()));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxFileHistoryCell, String> fileType = new TableColumn<HBoxFileHistoryCell, String>("File Type");
		fileType.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("fileType"));
		fileType.setMinWidth(110);
		acctFileHistoryList.getColumns().add(fileType);
		fileType.setCellFactory(new Callback<TableColumn<HBoxFileHistoryCell, String>,TableCell<HBoxFileHistoryCell, String>>()
        {
            public TableCell<HBoxFileHistoryCell, String> call(TableColumn<HBoxFileHistoryCell, String> param) 
            {
                return new TableCell<HBoxFileHistoryCell, String>() 
                {
                    @Override
                    protected void updateItem(String item, boolean empty) 
                    {
                        if(!empty) 
                        {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getFileType();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex).getStatus()));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxFileHistoryCell, String> colEmployer = new TableColumn<HBoxFileHistoryCell, String>("Employer");
		colEmployer.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("employer"));
		colEmployer.setMinWidth(260);
		acctFileHistoryList.getColumns().add(colEmployer);
		colEmployer.setCellFactory(new Callback<TableColumn<HBoxFileHistoryCell, String>,TableCell<HBoxFileHistoryCell, String>>()
        {
            public TableCell<HBoxFileHistoryCell, String> call(TableColumn<HBoxFileHistoryCell, String> param) 
            {
                return new TableCell<HBoxFileHistoryCell, String>() 
                {
                    @Override
                    protected void updateItem(String item, boolean empty) 
                    {
                        if(!empty) 
                        {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getEmployer();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex).getStatus()));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxFileHistoryCell, String> colDesc = new TableColumn<HBoxFileHistoryCell, String>("Description");
		colDesc.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("description"));
		colDesc.setMinWidth(500);
		acctFileHistoryList.getColumns().add(colDesc);
		colDesc.setCellFactory(new Callback<TableColumn<HBoxFileHistoryCell, String>,TableCell<HBoxFileHistoryCell, String>>()
        {
            public TableCell<HBoxFileHistoryCell, String> call(TableColumn<HBoxFileHistoryCell, String> param) 
            {
                return new TableCell<HBoxFileHistoryCell, String>() 
                {
                    @Override
                    protected void updateItem(String item, boolean empty) 
                    {
                        if(!empty) 
                        {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getDescription();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex).getStatus()));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxFileHistoryCell, String> colSpecId = new TableColumn<HBoxFileHistoryCell, String>("Spec Id");
		colSpecId.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("specId"));
		colSpecId.setMinWidth(60);
		colSpecId.setComparator((String o1, String o2) -> {
            return Integer.compare(Integer.valueOf(o1), Integer.valueOf(o2));
        });
	    acctFileHistoryList.getColumns().add(colSpecId);
	    colSpecId.setCellFactory(new Callback<TableColumn<HBoxFileHistoryCell, String>,TableCell<HBoxFileHistoryCell, String>>()
        {
            public TableCell<HBoxFileHistoryCell, String> call(TableColumn<HBoxFileHistoryCell, String> param) 
            {
                return new TableCell<HBoxFileHistoryCell, String>() 
                {
                    @Override
                    protected void updateItem(String item, boolean empty) 
                    {
                        if(!empty) 
                        {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getSpecId();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex).getStatus()));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxFileHistoryCell, String> colSpec = new TableColumn<HBoxFileHistoryCell, String>("Spec");
		colSpec.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("spec"));
		colSpec.setMinWidth(215);
	    acctFileHistoryList.getColumns().add(colSpec);
	    colSpec.setCellFactory(new Callback<TableColumn<HBoxFileHistoryCell, String>,TableCell<HBoxFileHistoryCell, String>>()
        {
            public TableCell<HBoxFileHistoryCell, String> call(TableColumn<HBoxFileHistoryCell, String> param) 
            {
                return new TableCell<HBoxFileHistoryCell, String>() 
                {
                    @Override
                    protected void updateItem(String item, boolean empty) 
                    {
                        if(!empty) 
                        {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getSpec();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex).getStatus()));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
		
		TableColumn<HBoxFileHistoryCell, String> colMapperId = new TableColumn<HBoxFileHistoryCell, String>("Mapper Id");
		colMapperId.setCellValueFactory(new PropertyValueFactory<HBoxFileHistoryCell,String>("mapperId"));
		colMapperId.setMinWidth(75);
		colMapperId.setSortable(true);
		colMapperId.setComparator((String o1, String o2) ->
		{
			if(o1 == null || o2 == null || o1 == "" || o2 == "") 
				return 0;
			else
				return Integer.compare(Integer.valueOf(o1), Integer.valueOf(o2));
        });
	    acctFileHistoryList.getColumns().add(colMapperId);
	    colMapperId.setCellFactory(new Callback<TableColumn<HBoxFileHistoryCell, String>,TableCell<HBoxFileHistoryCell, String>>()
        {
            public TableCell<HBoxFileHistoryCell, String> call(TableColumn<HBoxFileHistoryCell, String> param) 
            {
                return new TableCell<HBoxFileHistoryCell, String>() 
                {
                    @Override
                    protected void updateItem(String item, boolean empty) 
                    {
                        if(!empty) 
                        {
                        	int currentIndex = indexProperty().getValue() < 0 ? 0: indexProperty().getValue();
                            String val = param.getTableView().getItems().get(currentIndex).getMapperId();
                        	setText(val);
                        	setStyle(getCellStyle(param.getTableView().getItems().get(currentIndex).getStatus()));
                        } else {
                        	setText(null);
                        	setStyle("");
                        }
                    }
                };
            }
        });
	}

//	ComboBox<HBoxEmployerCell> emprCombo = new ComboBox<>();
//	emprCombo.setItems
	
    public void addPropertyRows(List<DataPropertyType> props) 
    {
    	if(props != null ? !props.isEmpty() : false) 
    	{
    		int i = 0;
    		for(DataPropertyType pt : props) 
    		{
    			//manually limiting to 3 rows.
    			i++;
    			if(i > 3)
    				break;
    			if(dataTypeVBox != null)
    	    	{
    				final DataProperty dp = new DataProperty();
    				dp.setPropertyType(pt);
    				dp.setName(pt.getName());
    				if(pt.getDefaultValue() != null)
    					dp.setValue(pt.getDefaultValue());
    				
    				DataManager.mUploadDataProperties.putIfAbsent(uploadType.getId(), new ArrayList<DataProperty>());
    				
    	    		HBox hbox = new HBox();
    	    		VBox vbox = new VBox();
    	    		VBox _vbox = new VBox();
    	    		HBox _hbox = new HBox();
    	    		
    	    		Label label = new Label("Data Property Name:");
    	    		TextField textField = new TextField();
    	    		textField.setText(pt.getName());
    	    		
    	    		textField.setEditable(false);

    	    		Label _label = new Label("Data Property Value:");
    	    		TextField _textField = new TextField();
    	    		_textField.setPromptText(pt.getDefaultValue());
    	    		_textField.textProperty().addListener((observable, oldValue, newValue) -> {
    	    			//UPDATE VALUE
    	    			dp.setValue(newValue);
    	    		});
    	    		
    	    		CheckBox checkbox = new CheckBox("Required?");
    	    		checkbox.setPrefHeight(26);
    	    		checkbox.setSelected(pt.isPropertyRequired());
    	    		checkbox.setDisable(true);
    	    		checkbox.setStyle("-fx-opacity: 1");
    	    		
    	    		_hbox.setSpacing(10);
    	    		_vbox.setSpacing(10);
    	    		vbox.setSpacing(10);
    	    		hbox.setSpacing(10);
    	    		
    	    		_hbox.getChildren().addAll(_textField,checkbox);
    	    		_vbox.getChildren().addAll(_label,_hbox);
    	    		vbox.getChildren().addAll(label,textField);
    	    		hbox.getChildren().addAll(vbox,_vbox);
    	    		
    	    		dataTypeVBox.getChildren().add(hbox);
    	    		
    	    		dataTypeVBox.getScene().getWindow().sizeToScene();
    	    		
    	    		DataManager.mUploadDataProperties.get(uploadType.getId()).add(dp);
    	    	}
    		}
    	}
    }
	
	private void updateData()
	{
		Task<Void> task = new Task<Void>() 
		{ 
            @Override 
            protected Void call() throws Exception
            { 
            	EtcAdmin.i().updateStatus(0.5, "Updating Accounts...");
            	DataManager.i().updateAccounts();
            	EtcAdmin.i().updateStatus(0.75, "Almost Ready");
//            	DataManager.i().updateEmployers();
            	EtcAdmin.i().updateStatus(0, "Ready");
        		loadSearchAccounts();
        		loadSearchEmployers();
                return null;
            }
        };
        task.setOnSucceeded(e -> loadData()); 
        task.setOnFailed(e -> loadData()); 
        new Thread(task).start(); 	
	}

	private void loadData()
	{
//		loadSearchAccounts();
//		loadSearchEmployers();
		accountCombo.setOpacity(1);
		employerCombo.setOpacity(1);
		accountCombo.setEditable(true);
		employerCombo.setEditable(true);
	}

	private String getCellStyle(String type) 
	{
    	switch(type) 
    	{
	    	case "COMPLETED":
	    		return "-fx-text-fill: #007700";
	    	case "FAILED":
	    		return "-fx-text-fill: #770000";
	    	case "PROCESSING":
	    		return "-fx-text-fill: #555555";
	    	case "REPROCESSED":
	    		return "-fx-text-fill: #770077";
	    	default:
	    		return "";
    	}
	}

	public void loadSearchAccounts() 
	{
		if(acctSec == false)
		{
			String sName;
	
			ObservableList<String> accountNames = FXCollections.observableArrayList();

			try {
				
				for(Account acct : DataManager.i().mAccounts) 
				{
					if(acct.isActive() == false) continue;
	
					sName = acct.getName();
	
					if(sName != null) 
					{
						accountNames.add(sName);
						currentAccountListIds.add(acct.getId());
					}
				}

				FXCollections.sort(accountNames);
				// use a filterlist to wrap the account names for combobox searching later
		        FilteredList<String> filteredItems = new FilteredList<String>(accountNames, p -> true);

		        // add a listener for the edit control on the combobox
		        // the listener will filter the list according to what is in the search box edit control
		        accountCombo.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> 
				{
		            final javafx.scene.control.TextField editor = accountCombo.getEditor();
		            final String selected = accountCombo.getSelectionModel().getSelectedItem();

		            accountCombo.show();
		            accountCombo.setVisibleRowCount(10);

		            // Run on the GUI thread
		            Platform.runLater(() -> 
		            {
		                if(selected == null || !selected.equals(editor.getText())) 
		                {
		                    filteredItems.setPredicate(item -> 
		                    {
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
				accountCombo.setItems(filteredItems);	

				//close the dropdown if it is showing
				accountCombo.hide();
				
			} catch (Exception e) {
				logr.log(Level.SEVERE, "Exception.", e);
			}

		} else {
			String sName;
//			
			ObservableList<String> accountNames = FXCollections.observableArrayList();

			try {
//				account = employer.getAccount();
				accountCombo.getSelectionModel().select(employer.getAccount().getName());

//				if(account.isActive() == false) return;
//				
//				sName = account.getName();
//
//				if(sName != null) 
//				{
//					accountNames.add(sName);
//					currentAccountListIds.add(account.getId());
//				}

				acctFirst = false;

//				FXCollections.sort(accountNames);
//				// use a filterlist to wrap the account names for combobox searching later
//		        FilteredList<String> filteredItems = new FilteredList<String>(accountNames, p -> true);
//	
//		        // add a listener for the edit control on the combobox
//		        // the listener will filter the list according to what is in the search box edit control
//		        accountCombo.getEditor().textProperty().addListener((obc,oldvalue,newvalue) ->
//				{
//		            final javafx.scene.control.TextField editor = accountCombo.getEditor();
//		            final String selected = accountCombo.getSelectionModel().getSelectedItem();
//		            
//		            accountCombo.show();
//		            accountCombo.setVisibleRowCount(10);
		            acctSec = true;
	
		            // Run on the GUI thread
//		            Platform.runLater(() -> 
//		            {
//		                if(selected == null || !selected.equals(editor.getText())) 
//		                {
//		                    filteredItems.setPredicate(item -> 
//		                    {
//		                        if (item.toUpperCase().contains(newvalue.toUpperCase())) 
//		                        {
//		                            return true;
//		                        } else {
//		                            return false;
//		                        }
//		                    });
//		                }
//		            });
//				});

				//finally, set our filtered items as the combobox collection
//				accountCombo.setItems(filteredItems);
//		        accountCombo.getSelectionModel().select(account.getName());
				//close the dropdown if it is showing
				accountCombo.hide();
			} catch (Exception e) {
				logr.log(Level.SEVERE, "Exception.", e);
			}
		}
	}

    public void onSearchAccount(ActionEvent event)
    {
    	
    	searchAccount();
    	if(acctSec == false) 
    	{
    		acctFirst = true; 
    		loadSearchEmployers(); 
    	}
    	refreshUploadCombo();
    }

	public void searchAccount()
	{
		String sSelection = accountCombo.getValue();
		String sName;

		for(Account acct : DataManager.i().mAccounts)
		{
			if(acct.isActive() == false) continue;
			if(acct.isDeleted() == true) continue;

			sName = acct.getName();

			if(sName.equals(sSelection))
			{
				// set the current account
				account = acct;

//				updateQueueData();
				if(showUpdFileHist == false)
				{
					updateFileHistoryData();
					showUpdFileHist = true;
				}
				if(acctSec == true)
				{
					loadSearchEmployers();
				}
//				loadSearchEmployers();
//				updateQueueData();
				break;
			}
		}	
	}

	public void loadSearchEmployers()
	{
		if(acctFirst == false && acctSec == false)
		{
			try {

				List<HBoxEmployerCell> list = new ArrayList<>();

				for(Employer empr : DataManager.i().mEmployersList) 
				{
					if(empr.isActive() == false) continue;
					if(empr.isDeleted() == true) continue;

					list.add(new HBoxEmployerCell(empr));

					currentEmployerListIds.add(empr.getId());
				};		
		        ObservableList<HBoxEmployerCell> myObservableList = FXCollections.observableList(list);
//				ObservableList<String> employerNames = FXCollections.observableArrayList();

//				FXCollections.sort(employerNames, (o1, o2) -> (o1.substring(3).compareTo(o2.substring(3))));
				// use a filterlist to wrap the account names for combobox searching later
//		        FilteredList<String> filteredItems = new FilteredList<String>(employerNames, p -> true);
		        FilteredList<HBoxEmployerCell> filteredItems = new FilteredList<HBoxEmployerCell>(myObservableList, p -> true);

		        // add a listener for the edit control on the combobox
		        // the listener will filter the list according to what is in the search box edit control
		        employerCombo.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> 
				{
		            final javafx.scene.control.TextField editor = employerCombo.getEditor();
		            final String selected = String.valueOf(employerCombo.getSelectionModel().getSelectedItem());
		            
		            employerCombo.show();
		            employerCombo.setVisibleRowCount(10);

		            // Run on the GUI thread
		            Platform.runLater(() -> 
		            {
		                if(selected == null || !selected.equals(editor.getText())) 
		                {
		                    filteredItems.setPredicate(item -> 
		                    {
		                        if(item.getSearchString().toUpperCase().contains(newvalue.toUpperCase()))
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
				employerCombo.setItems(filteredItems);	
		        Comparator<HBoxEmployerCell> comparator = Comparator.comparing(HBoxEmployerCell::getEmployerName);
		        myObservableList.sort(comparator);        
//		        employerCombo.setItems(myObservableList);
				
				//close the dropdown if it is showing
				employerCombo.hide();
			} catch (Exception e) {
				logr.log(Level.SEVERE, "Exception.", e);
			}

		} else {

			try {
				EmployerRequest request = new EmployerRequest();
				request.setAccountId(account.getId());
				employers = AdminPersistenceManager.getInstance().getAll(request);

				List<HBoxEmployerCell> list = new ArrayList<>();
		        ObservableList<HBoxEmployerCell> myObservableList = FXCollections.observableList(list);

				for(Employer empr : employers) 
				{
					if(empr.isActive() == false) continue;
					if(empr.isDeleted() == true) continue;
					
					list.add(new HBoxEmployerCell(empr));
					currentEmployerListIds.add(empr.getId());
				};

//				FXCollections.sort(employerNames, (o1, o2) -> (o1.substring(4).compareTo(o2.substring(4))));
				// use a filterlist to wrap the account names for combobox searching later
		        FilteredList<HBoxEmployerCell> filteredItems = new FilteredList<HBoxEmployerCell>(myObservableList, p -> true);

		        // add a listener for the edit control on the combobox
		        // the listener will filter the list according to what is in the search box edit control
		        employerCombo.getEditor().textProperty().addListener((obc,oldvalue,newvalue) -> 
				{
		            final javafx.scene.control.TextField editor = employerCombo.getEditor();
		            final String selected = String.valueOf(employerCombo.getSelectionModel().getSelectedItem());

		            employerCombo.show();
		            employerCombo.setVisibleRowCount(10);
	
		            // Run on the GUI thread
		            Platform.runLater(() -> 
		            {
		                if(selected == null || !selected.equals(editor.getText())) 
		                {
		                    filteredItems.setPredicate(item -> 
		                    {
		                        if(item.getSearchString().toUpperCase().contains(newvalue.toUpperCase()))
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
//				employerCombo.setItems(filteredItems);	
		        Comparator<HBoxEmployerCell> comparator = Comparator.comparing(HBoxEmployerCell::getEmployerName);
		        myObservableList.sort(comparator);        

		        employerCombo.setItems(myObservableList);
				//close the dropdown if it is showing
				employerCombo.hide();
			} catch (Exception e) {
				logr.log(Level.SEVERE, "Exception.", e);
			}
		}
	}

	public void onSearchEmployer(ActionEvent event)
	{
//		if(account != null) { System.out.println("only triggers event on selection");loadSearchEmployers(); }
		searchEmployer();
		if(acctFirst == false) { loadSearchAccounts(); }
		refreshUploadCombo();
	}
	
	public void searchEmployer() 
	{
		String sSelection = String.valueOf(employerCombo.getSelectionModel().getSelectedItem());
//		Employer emplyr = employerCombo.getSelectionModel().getSelectedItem().getEmployer();

		if(acctFirst == true)
		{
			for(Employer empr : employers)
			{
				if(empr.isActive() == false) continue;
				if(empr.isDeleted() == true) continue;

				if(sSelection.contains(String.valueOf(empr.getId())) && sSelection.contains(empr.getName()))
				{
					// set the current account
					employer = empr;
//					acctSec = true;
//					updateQueueData();
//					updateFileHistoryData();
					break;
				}
			}

		} else {
			for(Employer empr : DataManager.i().mEmployersList)
			{
				if(empr.isActive() == false) continue;
				if(empr.isDeleted() == true) continue;

				if(sSelection.contains(String.valueOf(empr.getId())) && sSelection.contains(empr.getName()))
				{
					// set the current account
					employer = empr;
//					updateQueueData();
//					updateFileHistoryData();
					acctSec = true;
					break;
				}
			}	
		}
	}

	private void refreshUploadCombo()
	{
		type.getItems().clear();

		if(employerCombo.getSelectionModel().getSelectedItem() == null) return;

		if(account != null && employer != null)
		{
			try {
				UploadTypeRequest request = new UploadTypeRequest();
				request.setAccountId(account.getId());
				request.setEmployerId(employer.getId());
				uploadTypes = AdminPersistenceManager.getInstance().getAll(request);

				loadUploadTypes();

			} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
		}
	}

    private void loadUploadTypes()
    {
    	//EMPTY LIST
    	type.getItems().clear();
    	
		Task<Void> task = new Task<Void>() 
		{ 
            @Override 
            protected Void call() throws Exception  
            { 
    			for(UploadType upTp : uploadTypes)
    			{
    				if(upTp.isActive() == false) continue;
    				if(upTp.getSpecificationId() != null)
    				{
    					try {
    						//IF THE SPEC ID IS PRESENT ON THE UPLOADTYPE, WE NEED TO PULL THAT PARTICULAR 
    						//UPLOAD TYPE'S SPEC. THE DYNAMIC SPEC ID IS ON THE SPEC, SO OTHERWISE THE DYNAMIC SPEC IS NOT GOING 
    						//TO BE PRESENT ON THE UPLOADER
    						PipelineSpecificationRequest specRqs = new PipelineSpecificationRequest();
    						specRqs.setId(upTp.getSpecificationId());
    						upTp.setSpecification(AdminPersistenceManager.getInstance().get(specRqs));
    					} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
    		    	}
    			}
                return null; 
            } 
        };

        try {
			EmsApp.getInstance().getFxQueue().put(task);

		} catch (InterruptedException e) {  DataManager.i().log(Level.SEVERE, e); }

		//ORGANIZING THE EMPLOYER LIST A TO Z
        uploadTypes.sort(new Comparator<UploadType>()
	    {
			@Override
			public int compare(UploadType o1, UploadType o2) { return o1.getName().compareToIgnoreCase(o2.getName()); }
		});

		for(UploadType upTp : uploadTypes)
		{
			if(upTp.isActive() == false) continue;
			if(upTp.getSpecificationId() != null)
			{
				type.getItems().add(upTp.getName() + " :: " + upTp.getSpecificationId() + " :: " + 
								   (upTp.getSpecification().getDynamicFileSpecificationId() != null ? 
									upTp.getSpecification().getDynamicFileSpecificationId() : "--"));
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////
	///// FILE HISTORY
	////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	private void updateFileHistoryData() 
	{
		// clear the current info
		acctFileHistoryList.getItems().clear();
//		acctFileHistoryLabel.setText("File History (loading...)");
		// keep our current sort
       if(acctFileHistoryList.getSortOrder().size()>0)
            sortColumn = (TableColumn<HBoxFileHistoryCell, String>) acctFileHistoryList.getSortOrder().get(0);

		// create a thread to handle the update, letting the screen respond normally
		Task<Void> task = new Task<Void>() 
		{
            @Override
            protected Void call() throws Exception 
            {
            	//reset the queue
            	fileHistoryQueue.clear();
    		   
            	//iterate through the employers for this account
        		for(Employer empr : account.getEmployers()) 
        		{ 
//        			fileHistoryData.refreshFileHistory(empr.getId());
        			updateQueueData(empr);

        			//add to our account queue 
        			for(PipelineQueue queue : fileHistoryData.mPipelineQueue)
        			{
            			fileHistoryQueue.add(queue); 
        			}
        		}	 
        		return null;
            }
        };
        
      	task.setOnScheduled(e ->  {
  		EtcAdmin.i().setStatusMessage("Updating File History Data...");
  		EtcAdmin.i().setProgress(0.25);});
      			
      	task.setOnSucceeded(e ->  showFileHistory());
    	task.setOnFailed(e ->  showFileHistory());
        new Thread(task).start();
	}
	
	//update the file history list
	private void showFileHistory() 
	{
		//warn the user if we are not connected and bail
		if(bConnected == false) 
		{
			Calendar cal = Calendar.getInstance();
      		EtcAdmin.i().setStatusMessage("WARNING: Connection Error. Started at " + Utils.getTimeString24(firstDisconnect) + ". Last Attempt at " +  Utils.getTimeString24(cal) + ". Will Retry Next Refresh...");
      		EtcAdmin.i().setProgress(0.0);
      		return;
		}

		// clear the lists
		acctFileHistoryList.getItems().clear();
//		acctFileHistoryLabel.setText("File History");
		//reset our refresh counter
		boolean state1 = false;
		boolean state2 = false;
		boolean state3 = false;
		boolean state4 = false;
		boolean state5 = false;

		//load the queue
		if(fileHistoryQueue != null)
		{
		    List<HBoxFileHistoryCell> requestList = new ArrayList<>();
		    List<HBoxSpecCell> specList = new ArrayList<>();
			specList.add(new HBoxSpecCell("Request",
					  					  "Spec Id",
					  					  "Specification",
					  					  "Mapper Id", 
					  					  "Mapper",
					  					  true));
			for(int i = 0; i < fileHistoryQueue.size(); i++)
			{
				PipelineQueue queue = fileHistoryQueue.get(i);
				if(queue == null) continue;
				if(queue.getRequest() == null) continue;
				if(queue.getRequest().getSpecification() == null) continue;
				queue.setUnits(0l);
				queue.setRecords(0l);

				//apply filter if present
				if(acctFileHistoryFilterField.getText().contentEquals("") == false) 
				{
					String searchData = queue.getRequest().getDescription().toLowerCase();
					searchData += Utils.getDateTimeString24(queue.getRequest().getAsOf()) + " ";
					if(queue.getRequest().getEmployer() != null)
						searchData += queue.getRequest().getEmployer().getName() + " ";
					if(queue.getRequest().getSpecification() != null) 
					{
						searchData += queue.getRequest().getSpecification().getId().toString() + " ";
						searchData += queue.getRequest().getSpecification().getName() + " ";
					}
					if(queue.getRequest().getStatus() != null) 
					{
						searchData += queue.getRequest().getStatus().toString() + " ";
					}
					if(queue.getRequest().getCreatedBy() != null)
					{
						if(queue.getRequest().getCreatedBy().getFirstName() != null)
							searchData += queue.getRequest().getCreatedBy().getFirstName().toLowerCase() +  " "; 
						if(queue.getRequest().getCreatedBy().getLastName() != null)
							searchData += queue.getRequest().getCreatedBy().getLastName().toLowerCase() +  " "; 
						searchData += String.valueOf(queue.getFileType()).toLowerCase() + " ";
					}
					if(queue.getRequest().getResult() != null)
						searchData += queue.getRequest().getResult().toLowerCase();
					if(queue.getRequest().getSpecification().getDynamicFileSpecificationId() != null && queue.getRequest().getSpecification().getDynamicFileSpecificationId() != null) {
						searchData +=  queue.getRequest().getSpecification().getDynamicFileSpecificationId().toString() + "";
					}
					if(queue.getFileType() != null)
						searchData += queue.getFileType().toString() + " ";
					if(searchData.toLowerCase().contains(acctFileHistoryFilterField.getText().toLowerCase()) == false) continue;
				}
				switch(queue.getFileType())
				{
					case COVERAGE: 
						if(queue.getCoverageFile() != null && queue.getCoverageFile().getPipelineInformation() != null) 
						{
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
						if(queue.getEmployeeFile() != null && queue.getEmployeeFile().getPipelineInformation() != null) 
						{
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
						if(queue.getIrs1094cFile() != null && queue.getIrs1094cFile().getPipelineInformation() != null) 
						{
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
						if(queue.getIrs1095cFile() != null && queue.getIrs1095cFile().getPipelineInformation() != null) 
						{
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
						if(queue.getAirTranErrorFile() != null && queue.getAirTranErrorFile().getPipelineInformation() != null) 
						{
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
						if(queue.getAirTranReceiptFile() != null && queue.getAirTranReceiptFile().getPipelineInformation() != null) 
						{
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
						if(queue.getPayFile() != null && queue.getPayFile().getPipelineInformation() != null) 
						{
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
						if(queue.getPayPeriodFile() != null && queue.getPayPeriodFile().getPipelineInformation() != null) 
						{
							state1 = queue.getPayPeriodFile().getPipelineInformation().isInitialized();
							state2 = queue.getPayPeriodFile().getPipelineInformation().isParsed();
							state3 = queue.getPayPeriodFile().getPipelineInformation().isValidated();
							state4 = queue.getPayPeriodFile().getPipelineInformation().isCompleted();
							state5 = queue.getPayPeriodFile().getPipelineInformation().isFinalized();
							queue.setUnits(queue.getPayPeriodFile().getPipelineInformation().getUnits());
							queue.setRecords(queue.getPayPeriodFile().getPipelineInformation().getRecords());
						}
						break;
					default:
						break;
				}

				Calendar queueDate = (Calendar)queue.getRequest().getLastUpdated().clone();

				String user = "";
				if(queue.getRequest().getCreatedBy() != null)
					user = queue.getRequest().getCreatedBy().getFirstName() + " " + queue.getRequest().getCreatedBy().getLastName().substring(0,1) + ".";
				String empr = "";
				if(queue.getRequest().getEmployer() != null)
					empr = queue.getRequest().getEmployer().getName();
				String mapperId = "";
				if(queue.getRequest().getSpecification().getDynamicFileSpecificationId() != null)
					mapperId =  queue.getRequest().getSpecification().getDynamicFileSpecificationId().toString();
				requestList.add(new HBoxFileHistoryCell(i, user, Utils.getDateTimeString24(queueDate), // getLastUpdated()), 
										  empr, 
										  queue.getRequest().getDescription(), 
										  String.valueOf(queue.getUnits()),
										  String.valueOf(queue.getRecords()),
										  queue.getRequest().getSpecification().getId().toString(),
										  queue.getRequest().getSpecification().getName(),
										  mapperId, //queue.getRequest().getSpecification().getDynamicFileSpecificationId().toString(),
										  queue.getRequest().getStatus().toString(),
										  queue.getFileType().toString(),
										  state1, state2, state3, state4, state5,
										  queue.getRequest().getResult()));
			
				ObservableList<HBoxFileHistoryCell> myObservableList = FXCollections.observableList(requestList);
				acctFileHistoryList.setItems(myObservableList);	
				// sort
				if(sortColumn!=null) 
				{
					acctFileHistoryList.getSortOrder().add(sortColumn);
		            sortColumn.setSortable(true);
		        }
			}
		}
		EtcAdmin.i().setStatusMessage("Ready");
  		EtcAdmin.i().setProgress(0);
	}	

    /**
     * upload is called in response to the Upload button being clicked.
     */
    @FXML
    public void upload(ActionEvent event)
    {    		
    	isUploading = true;

    	if(file != null && employerCombo.getValue() != null && type.getValue() != null)
    	{
    		//DISABLE FORM WHILE UPLOADING
    	    accountCombo.setDisable(true);
    	    employerCombo.setDisable(true);
    	    type.setDisable(true);
    	    browse.setDisable(true);
    	    upload.setDisable(true);

	    	//CLEAR PROGRESS BAR
//	    	progress.setProgress(0.0);

	    	//RESOURCES
	    	File nfile = null;
	    	
	    	try
	    	{
	    		//CREATE NEW FILE FROM SOURCE
	    		nfile = new File(EmsApp.getInstance().getHomeFolder().getSubFolder("tmp", false).getAbsolutePath().concat(File.separator).concat(file.getName()));
	    		nfile.setReadable(true, false);
	    		nfile.setWritable(true, false);

				//DELETE PRE-EXISTING FILE
				if(nfile.isFile())
					FileUtils.forceDelete(nfile);

				String cks1,cks2 = null;
		    	//COPY FILE TO WORKING DIRECTORY
		    	FileUtils.copyFile(file, nfile);

		    	try {

		    		cks1 = Cryptographer.calculateMD5Checksum(file);
		    		cks2 = Cryptographer.calculateMD5Checksum(nfile);

		    		if(!cks1.equals(cks2))
		    		{
		    			logr.log(Level.WARNING, "CREATING TMP FILE :: INVALID CHECKSUM");
		    			logr.log(Level.INFO, "FILE :: " + file.getAbsolutePath());
		    			logr.log(Level.INFO, "FILE CKS1 :: " + cks1);
		    			logr.log(Level.INFO, "NFILE :: " + nfile.getAbsolutePath());
		    			logr.log(Level.INFO, "NFILE CKS2 :: " + cks2);
		    		}

		    	}catch(CryptographyException e) {  DataManager.i().log(Level.SEVERE, e); }
		    	finally {  
		    		cks1 = null;
		    		cks2 = null;
		    	}

		    	file = nfile;
		    	nfile = null;

	    	}catch(IOException | CoreException e)
	    	{
	    		  DataManager.i().log(Level.SEVERE, e); 	    		
	    		  return;
	    	}
 
    	    //TRANSFER UPLOAD INFO
	    	DataManager.i().setActiveUpload(new UploadRequest(file, uploadType, employer, null), file);

			//CREATE UPLOAD TASK
			UploadTask ultask = new UploadTask();
	    	progress.progressProperty().bind(ultask.progressProperty());
	    	message.textProperty().bind(ultask.messageProperty());

			EtcAdmin.i().setStatusMessage("loading Mapper..."); 
			EtcAdmin.i().setProgress(.5); 

	    	//HANDLE SUCCESSFUL COMPLETION OF TASK, DOES NOT MEAN UPLOAD WAS ACTUALLY SUCCESSFUL
	    	ultask.setOnSucceeded(new EventHandler<WorkerStateEvent>()
	    	{
				@Override
				public void handle(WorkerStateEvent event)
				{
					boolean uploaded = false;
					try
					{
						uploaded = ultask.get();
//						updateFileHistoryData();
//						updateQueueData();

					}catch(InterruptedException | ExecutionException e)
					{
						resetForm(e.getMessage());
						return;
					}finally
					{
						try { FileUtils.forceDelete(file); }catch(IOException e){ DataManager.i().log(Level.SEVERE, e); }
					}

					//IF TRUE, FILE WAS SUCCESSFULLY TRANSFERRED
					if(uploaded)
					{
//						resetForm("File successfully transferred.");
						resetForm("Click 'Upload' to begin a transfer...");
						acctSec = true;
//						updateData();
				    	isUploading = false;
				    	showUpdFileHist = false;
//				    	setUploadColumns();
//						initialize();
					}else
					{
						resetForm("Unable to transfer file.");
					}
				}
	    	});
			EtcAdmin.i().setProgress(.75); 

	    	//HANDLE FAILURE, UNCAUGHT EXCEPTION WITHIN TASK OR OTHER EXCEPTION WAS GENERATED
	    	ultask.setOnFailed(new EventHandler<WorkerStateEvent>()
	    	{
				@Override
				public void handle(WorkerStateEvent event)
				{
					resetForm(ultask.getException().getMessage());
					try { FileUtils.forceDelete(file); }catch(IOException e){  DataManager.i().log(Level.SEVERE, e); }
					ultask.getException().printStackTrace();
				}
	    	});

	    	new Thread(ultask).start();
			EtcAdmin.i().setProgress(0); 
    	}
    }

    /**
     * cancel is called in response to the Cancel button being clicked.
     */
    @FXML
    public void cancel(ActionEvent event)
    {
    	//Clear the form
    	accountCombo.setValue(null);
    	employerCombo.setValue(null);
    	fileLocation.setText("");
    	type.setValue(null);
    	encrypted.setSelected(false);
    	acctFileHistoryList.getItems().clear();
    	acctFirst = false;
    	acctSec = false;
    	updateData();
//    	accountCombo.setValue(null);
    	account = null;
    	employer = null;
    	showUpdFileHist = false;
    }
    
    /**
     * find is called in response to the Browse button being clicked.
     */
    @FXML
    public void find(ActionEvent event)
    {
		//SET DEFAULT UPLOADFROM FOLDER
    	if(DataManager.i().mUploadFromDirectory == null)
    		DataManager.i().mUploadFromDirectory = System.getProperty("user.home", null);

    	if(DataManager.i().mUploadFromDirectory != null)
    	{
	    	fileChooser.setInitialDirectory(new File(DataManager.i().mUploadFromDirectory));
	    	if((file = fileChooser.showOpenDialog(null)) != null)
	    	{
		    	String cks1 = null;
		    	String cks2 = null;
		    	logr = Logger.getLogger(UploadForm.class.getCanonicalName());

		    	try
		    	{
		    		cks1 = Cryptographer.calculateMD5Checksum(file);
		    	}catch(CryptographyException e) { DataManager.i().log(Level.SEVERE, e); }

		    	file.setReadable(true, false);
		    	file.setWritable(true, false);//TODO: PROBABLY DON'T NEED THIS CALL

		    	File file2 = null;
		    	try
		    	{
		    		file2 = new File(file.getAbsolutePath());
		    		file2.setReadable(true,false);
		    		cks2 = Cryptographer.calculateMD5Checksum(file2);
		    	}catch(CryptographyException e) { DataManager.i().log(Level.SEVERE, e); }
		    	finally { file2 = null; }

	    		fileLocation.setText(file.getName());
	    		DataManager.i().mUploadFromDirectory = file.getParent();

	    		//CALCULATE APPROX # OF GLOBS, GLOB SIZE, ETC.
	    		long gbs = file.length() / (long)FileGlobber.DEFAULT_GLOB_SIZE;
	    		gbs = gbs < 1L ? 1L : gbs;
	    		StringBuilder sb = new StringBuilder();
	    		sb.append("size: ");
	    		sb.append(file.length());
	    		sb.append(" bytes, approximately ");
	    		sb.append(gbs);
	    		sb.append(" glob(s).");

	    		if(!cks2.equals(cks1)) {
	    			
	    			logr.log(Level.WARNING, "INVALID CHECKSUM ON FILECHOOSER CHECK. FILE :: " + file.getAbsolutePath());
	    			logr.log(Level.INFO, "CHK1 :: " + cks1);
	    			logr.log(Level.INFO, "CHK2 :: " + cks2);
	    			
	    			specs.setText("INVALID CHECKSUM. Contact IT.");
	    		}else {
	    			logr.log(Level.INFO, "FILE :: " + file.getName() + " CHKSUM :: " + cks1);
	    			specs.setText(sb.toString());
	    		}
	    		fileLocation.setEditable(true);
	    	}else
	    	{
	    		fileLocation.setText(null);
	    		specs.setText("");
	    	}
    	}else
    	{
    		fileLocation.setText(null);
    		specs.setText("");
    	}
    }

    @FXML
    private void onUpTypSelect(ActionEvent event)
    {
    	type.setVisibleRowCount(20);

    	String sSelection = type.getSelectionModel().getSelectedItem();

		if(isUploading == false)
		{
			for(UploadType upTp : uploadTypes)
			{
				//CHECK FOR ACTIVE STATUS
				if(upTp.isActive() == false || upTp.getName() == null) continue;
	
				if(sSelection != null)
				{
					//MATCH SELECTED ITEM WITH NAME OF UPLOAD TYPE
					if(upTp.getSpecification() != null && upTp.getSpecification().getDynamicFileSpecificationId() != null)
					{
						if(sSelection.equals(upTp.getName() + " :: " + upTp.getSpecificationId() + " :: " + upTp.getSpecification().getDynamicFileSpecificationId()))
						{
							//SET ACTIVE UPLOAD TYPE
							uploadType = upTp;
							break;
						}
					} else {
						if(sSelection.equals(upTp.getName() + " :: " + upTp.getSpecificationId() + " :: " + "--"))
						{
							//SET ACTIVE UPLOAD TYPE
							uploadType = upTp;
							break;
						}
					}
				}
			}	
		}
    }

    private void resetForm(final String msg)
    {
    	//ENABLE FORM
	    accountCombo.setDisable(false);
	    employerCombo.setDisable(false);
	    type.setDisable(false);
	    browse.setDisable(false);
	    upload.setDisable(false);

    	//Clear the form except Account and Employer
    	fileLocation.setText("");
    	type.setValue(null);
    	encrypted.setSelected(false);
    	file = null;

    	//CLEAR PROGRESS BAR
    	progress.progressProperty().unbind();
    	message.textProperty().unbind();

    	progress.setProgress(0.0);
    	message.setText(msg);
    }
    
	@FXML
	private void onAccountClear(ActionEvent event)
	{
		cancel(event);
	}
    
//	@FXML
//	private void onQueueRange(ActionEvent event)
//	{
//		updateQueueData();
//	}
	
	@SuppressWarnings("unchecked")
	private void updateQueueData(Employer empr) 
	{
    	try {
			// keep our current sort
	       if(acctFileHistoryList.getSortOrder().size()>0)
	            sortColumn = (TableColumn<HBoxFileHistoryCell, String>) acctFileHistoryList.getSortOrder().get(0);
	
			// create a thread to handle the update, letting the screen respond normally
			Task<Void> task = new Task<Void>() 
			{
	            @Override
	            protected Void call() throws Exception 
	            {
	            	refreshPipelineRequests(empr);
	                return null;
	            }
	        };
	        
	    	Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() 
	    	{
	    	    public void uncaughtException(Thread th, Throwable ex)
	    	    {
	    	        System.out.println("Uncaught exception: " + ex);
	    			Utils.alertUser("Queue Exception", ex.getMessage());
	    	    }
	    	};
	    	
	      	task.setOnScheduled(e ->  
	      	{
	      		EtcAdmin.i().setStatusMessage("Updating Queue Data...");
	      		EtcAdmin.i().setProgress(0.25);});
	      			
	    	task.setOnSucceeded(e ->  showFileHistory());
	    	
	    	task.setOnFailed(new EventHandler<WorkerStateEvent>() 
	    	{
	    	    @Override
	    	    public void handle(WorkerStateEvent arg0) 
	    	    {
	    	        Exception e =new Exception(task.getException());
	            	DataManager.i().log(Level.SEVERE, e); 
	    	    }
	    	});
	    	
	    	Thread queueRefresh = new Thread(task);
	    	queueRefresh.setUncaughtExceptionHandler(h);
    		EmsApp.getInstance().getFxQueue().put(queueRefresh);
		}catch(InterruptedException e)
		{
			EtcAdmin.i().setProgress(0);
			DataManager.i().log(Level.SEVERE, e); 
		}
    	//queueRefresh.start();
	}
	
	public boolean refreshPipelineRequests(Employer empr)
	{
		try {
			updatePipelineRequests(empr);
		} catch (SQLException | ParseException e) 
		{
        	DataManager.i().log(Level.SEVERE, e); 
			return false;
		}		
		return true;
	}

	// get the most recent pipeline requests
	private <T extends CoreData> void updatePipelineRequests(Employer empr) throws SQLException, ParseException 
	{
		//reset everything
		fileHistoryQueue.clear();
		
		try {

			Date today = new Date();
			Calendar queueDays = new GregorianCalendar();
//			
			queueDays.setTime(today);
			queueDays.add(Calendar.DAY_OF_MONTH, -46);
			
			// create the request
			PipelineRequestRequest request = new PipelineRequestRequest();

			// set the lastUpdated
			request.setLastUpdated(queueDays.getTimeInMillis());
			request.setEmployerId(empr.getId());

			// get from the server
			DataManager.i().mPipelineRequests = AdminPersistenceManager.getInstance().getAll(request);

			// bail if we didn't have a return
			if(DataManager.i().mPipelineRequests  == null) return;
			//System.out.println("Request Count:" + DataManager.i().mPipelineRequests.size());
			
			HashMap<PipelineFileType, List<Long>> fileMap = new HashMap<PipelineFileType,List<Long>>();
			HashMap<PipelineFileType, List<? extends CoreData>> entityMap = new HashMap<PipelineFileType, List<? extends CoreData>>();
			
			// now build the pipeline queue entries
			for (PipelineRequest req : DataManager.i().mPipelineRequests) 
			{
				PipelineQueue queue = new PipelineQueue(req);

				//get the specification if needed
				if (req.getSpecification() == null)
				{
					PipelineSpecification specification = getSpecification(req);
					if (specification == null) continue;
					req.setSpecification(specification);
				}
				// get the channel if needed
				if (req.getSpecification().getChannel() == null) 
				{
					PipelineChannel channel = getChannel(req);
					if (channel == null) continue;		
					req.getSpecification().setChannel(channel);
				}
				// get the account if needed for use later
				if (req.getEmployer().getAccount() == null) 
				{
					Account account = getAccount(req.getEmployer().getAccountId());
					if (account == null) continue;
					req.getEmployer().setAccount(account);
				}
				// get the user if needed for use later
				if (req.getCreatedBy() == null && req.getCreatedById() != null && req.getCreatedById() != 0)
				{
					UserRequest userRequest = new UserRequest();
					userRequest.setId(req.getCreatedById());
					req.setCreatedBy(AdminPersistenceManager.getInstance().get(userRequest));
				}

				//set the file type
				queue.setFileType(req.getSpecification().getChannel().getType());
				
				//add record to map
				if(!fileMap.containsKey(queue.getFileType()))
					fileMap.put(queue.getFileType(), new ArrayList<Long>(Arrays.asList(queue.getRequest().getPipelineFileId())));
				else
					fileMap.get(queue.getFileType()).add(queue.getRequest().getPipelineFileId());
				// add it to our queue
				fileHistoryQueue.add(queue);
			}
			
			
			if(!fileMap.isEmpty())
				for(Entry<PipelineFileType,List<Long>> map : fileMap.entrySet())
				{
					switch(map.getKey())
					{
						case EMPLOYEE:
							EmployeeFileRequest efrqs = new EmployeeFileRequest();
							efrqs.setIdList(map.getValue());
							entityMap.put(PipelineFileType.EMPLOYEE, AdminPersistenceManager.getInstance().getAll(efrqs));
							break;
						case COVERAGE:
							CoverageFileRequest cfrqs = new CoverageFileRequest();
							cfrqs.setIdList(map.getValue());
							entityMap.put(PipelineFileType.COVERAGE, AdminPersistenceManager.getInstance().getAll(cfrqs));
							break;
						case PAY:
							PayFileRequest pfrqs = new PayFileRequest();
							pfrqs.setIdList(map.getValue());
							entityMap.put(PipelineFileType.PAY, AdminPersistenceManager.getInstance().getAll(pfrqs));
							break;
						case DEDUCTION:
							DeductionFileRequest dfrqs = new DeductionFileRequest();
							dfrqs.setIdList(map.getValue());
							entityMap.put(PipelineFileType.DEDUCTION, AdminPersistenceManager.getInstance().getAll(dfrqs));
							break;
						case INSURANCE:
							InsuranceFileRequest ifrqs = new InsuranceFileRequest();
							ifrqs.setIdList(map.getValue());
							entityMap.put(PipelineFileType.INSURANCE, AdminPersistenceManager.getInstance().getAll(ifrqs));
							break;
						case IRS1094C:
							Irs1094cFileRequest irs1094crqs = new Irs1094cFileRequest();
							irs1094crqs.setIdList(map.getValue());
							entityMap.put(PipelineFileType.IRS1094C, AdminPersistenceManager.getInstance().getAll(irs1094crqs));
							break;
						case IRS1095C:
							Irs1095cFileRequest irs1095crqs = new Irs1095cFileRequest();
							irs1095crqs.setIdList(map.getValue());
							entityMap.put(PipelineFileType.IRS1095C, AdminPersistenceManager.getInstance().getAll(irs1095crqs));
							break;
						case PAYPERIOD:
							PayPeriodFileRequest ppdcrqs = new PayPeriodFileRequest();
							ppdcrqs.setIdList(map.getValue());
							entityMap.put(PipelineFileType.PAYPERIOD, AdminPersistenceManager.getInstance().getAll(ppdcrqs));
							break;
						default:
							break;
					}
				}
			
			if(!entityMap.isEmpty())
				for(PipelineQueue q : fileHistoryQueue)
					if(entityMap.containsKey(q.getFileType()))
						for(CoreData d : entityMap.get(q.getFileType()))
							if(q.getRequest().getPipelineFileId().equals(d.getId()))
							{
								switch(q.getFileType()) 
								{
									case EMPLOYEE:
										q.setEmployeeFile((EmployeeFile)d);
										break;
									case COVERAGE:
										q.setCoverageFile((CoverageFile)d);
										break;
									case PAY:
										q.setPayFile((PayFile)d);
										break;
									case DEDUCTION:
										q.setDeductionFile((DeductionFile)d);
										break;
									case INSURANCE:
										q.setInsuranceFile((InsuranceFile)d);
										break;
									case IRS1094C:
										q.setIrs1094cFile((Irs1094cFile)d);
										break;
									case IRS1095C:
										q.setIrs1095cFile((Irs1095cFile)d);
										break;
									case PAYPERIOD:
										q.setPayPeriodFile((PayPeriodFile)d);
										break;
									default:
										break;
								}
							}
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	}
	
	private PipelineSpecification getSpecification(PipelineRequest pipelineRequest) 
	{
		PipelineSpecification spec =  null;
		try {
			PipelineSpecificationRequest specReq = new PipelineSpecificationRequest();
			specReq.setId(pipelineRequest.getSpecificationId());
			spec = AdminPersistenceManager.getInstance().get(specReq);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
		return spec;
	}
	
	private PipelineChannel getChannel(PipelineRequest pipelineRequest)
	{
		PipelineChannel chnl =  null;
		try {
			PipelineChannelRequest pcReq = new PipelineChannelRequest();
			pcReq.setId(pipelineRequest.getSpecification().getChannelId());
			chnl = AdminPersistenceManager.getInstance().get(pcReq);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
		return chnl;
	}
	
	private Account getAccount(Long  accountId) 
	{
		Account account =  null;
		try {
			AccountRequest actReq = new AccountRequest();
			actReq.setId(accountId);
			account = AdminPersistenceManager.getInstance().get(actReq);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
		return account;
	}

	@FXML
	private void onRefresh() {
//		updateQueueData();
		updateFileHistoryData();
	}	

	@FXML
	private void onFileFilterKeyRelease() {
		showFileHistory();
	}

	@Override
	public Collection<String> call(ISuggestionRequest param)
	{
		loadFilteredAccountList(param.getUserText());
		return filteredAccountList;
	}
	
	private boolean selectFileHistoryObjects()
	{
 		try {
			// check to see if we have a bad selection
			if(acctFileHistoryList.getSelectionModel().getSelectedIndex() < 0) return false;
			selectedFileHistoryQueue = fileHistoryQueue.get(acctFileHistoryList.getSelectionModel().getSelectedItem().getQueueLocation());
	 		DataManager.i().mPipelineChannel = selectedFileHistoryQueue.getRequest().getSpecification().getChannel();
	 		DataManager.i().mPipelineSpecification = selectedFileHistoryQueue.getRequest().getSpecification();
	 		
	 		switch (selectedFileHistoryQueue.getFileType()) 
	 		{
				case COVERAGE: 
		     		DataManager.i().loadDynamicCoverageFileSpecification(selectedFileHistoryQueue.getRequest().getSpecification().getDynamicFileSpecificationId());
		     		CoverageFileRequest covReq = new CoverageFileRequest();
		     		covReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
					selectedFileHistoryQueue.setCoverageFile(AdminPersistenceManager.getInstance().get(covReq));
		    		return true;
				case EMPLOYEE: 
		     		DataManager.i().loadDynamicEmployeeFileSpecification(selectedFileHistoryQueue.getRequest().getSpecification().getDynamicFileSpecificationId());
		     		EmployeeFileRequest empReq = new EmployeeFileRequest();
		     		empReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
		     		selectedFileHistoryQueue.setEmployeeFile(AdminPersistenceManager.getInstance().get(empReq));
		    		return true;
				case PAY: 
		     		DataManager.i().loadDynamicPayFileSpecification(selectedFileHistoryQueue.getRequest().getSpecification().getDynamicFileSpecificationId());
		     		PayFileRequest payReq = new PayFileRequest();
		     		payReq.setId(selectedFileHistoryQueue.getRequest().getPipelineFileId());
		     		selectedFileHistoryQueue.setPayFile(AdminPersistenceManager.getInstance().get(payReq));
		     		return true;
				case PAYPERIOD: 
		    		return false;
				default:
		    		return false;
			}
		} catch (Exception e) {
			logr.log(Level.SEVERE, "Exception.", e);
		}
		
 		return false;
	}

    private void loadFilteredAccountList(String snippet)
    {
		filteredAccountList.clear();

		if(snippet != null ? snippet.length() > 1 : false)
		{
			int results = 0;
			snippet = snippet.toLowerCase();
			
			//LOAD OBSRV LIST
			for(Account a : DataManager.i().mAccounts)
			{
				if(a.isActive() && a.getName().toLowerCase().contains(snippet) && results <= 20)
				{
					filteredAccountList.add(a.getName());
					results++;
				}

				if(results > 20)
					break;
			}
	    }
    }
	
//	@SuppressWarnings("unchecked")
//	private void updateQueueData() 
//	{
//    	try {
//			// pause the queue timer while retrieving data
//			// keep our current sort
//	       if(acctFileHistoryList.getSortOrder().size()>0)
//	            sortColumn = (TableColumn<HBoxFileHistoryCell, String>) acctFileHistoryList.getSortOrder().get(0);
//	System.out.println("did i get here");
//	       int dateCompletedRange = 10000;
//		   if(pqueQueueRange.getValue().contains("ALL") == false)
//			   dateCompletedRange = (Integer.valueOf(pqueQueueRange.getValue()) / 24);
//		   System.out.println(pqueQueueRange.getValue());
//		   final int date1 = dateCompletedRange;
//		   
//			// create a thread to handle the update, letting the screen respond normally
//			Task<Void> task = new Task<Void>()
//			{
//	            @Override
//	            protected Void call() throws Exception
//	            {
//	            	setConnected(queueData.refreshPipelineRequests(date1));
//	                return null;
//	            }
//	        };
//	        
//	    	Thread.UncaughtExceptionHandler h = new Thread.UncaughtExceptionHandler() 
//	    	{
//	    	    public void uncaughtException(Thread th, Throwable ex) 
//	    	    {
//	    	        System.out.println("Uncaught exception: " + ex);
//	    			Utils.alertUser("Queue Exception", ex.getMessage());
//	    	    }
//	    	};
//	    	
//	      	task.setOnScheduled(e ->  
//	      	{
//	      		EtcAdmin.i().setStatusMessage("Updating Queue Data...");
//	      		EtcAdmin.i().setProgress(0.25);});
//	      			
//	    	task.setOnSucceeded(e ->  showFileHistory());
//	    	task.setOnFailed(new EventHandler<WorkerStateEvent>() 
//	    	{
//	    	    @Override
//	    	    public void handle(WorkerStateEvent arg0) 
//	    	    {
//	    	        Exception e =new Exception(task.getException());
//	            	DataManager.i().log(Level.SEVERE, e); 
//	    	    }
//	    	});
//	    	
//	    	Thread queueRefresh = new Thread(task);
//	    	queueRefresh.setUncaughtExceptionHandler(h);
//    		EmsApp.getInstance().getFxQueue().put(queueRefresh);
//		}catch(InterruptedException e)
//		{
//			EtcAdmin.i().setProgress(0);
//			DataManager.i().log(Level.SEVERE, e); 
//		}
//    	//queueRefresh.start();
//	}
//	
//	private void setConnected (boolean connected) 
//	{
//		// store the time if we are transitioning from true to false
//		if(bConnected == true && connected == false)
//			firstDisconnect = Calendar.getInstance();
//		bConnected = connected;
//		if(bConnected == false)
//			//go ahead and rebuld the ident, etc.
//			DataManager.i().reconnectCorvetto();
//	}

	public static class HBoxEmployerCell extends HBox 
	{
        Label lblEmployerId = new Label();
        Label lblEmployerName = new Label();
        Employer employer;

        public long getEmployerId() { return Long.valueOf(lblEmployerId.getText()); } 
        public String getEmployerName() { return lblEmployerName.getText(); }
        public Employer getEmployer() { return employer; }
        
        public String getSearchString()
        {
        	String returnString = "";
        	if(lblEmployerId.getText() != null && lblEmployerName.getText() != null)
        	{
            	returnString = lblEmployerId.getText() + " " + lblEmployerName.getText();
        	}
        	return returnString;
        } 

        @Override
        public String toString() {
        	return getSearchString();
        }

        HBoxEmployerCell(Employer employer) 
        {
             super();
             this.employer = employer;

             lblEmployerId.setText(String.valueOf(employer.getId()));
             lblEmployerId.setMinWidth(100);
             lblEmployerId.setMaxWidth(100);
             lblEmployerId.setPrefWidth(100);
             HBox.setHgrow(lblEmployerId, Priority.ALWAYS);

             lblEmployerName.setText(employer.getName());
             lblEmployerName.setMinWidth(300);
             lblEmployerName.setMaxWidth(300);
             lblEmployerName.setPrefWidth(300);
             HBox.setHgrow(lblEmployerName, Priority.ALWAYS);

          	 this.getChildren().addAll(lblEmployerId, lblEmployerName);
        }
    }	
	
   	public class HBoxFileHistoryCell 
   	{
   		SimpleStringProperty user = new SimpleStringProperty();
   		SimpleStringProperty dateTime = new SimpleStringProperty();
   		SimpleStringProperty employer = new SimpleStringProperty();
   		SimpleStringProperty description = new SimpleStringProperty();
   		SimpleStringProperty status = new SimpleStringProperty();
   		SimpleStringProperty result = new SimpleStringProperty();
   		SimpleStringProperty fileType = new SimpleStringProperty();
   		SimpleStringProperty units = new SimpleStringProperty();
   		SimpleStringProperty records = new SimpleStringProperty();
   		SimpleStringProperty specId = new SimpleStringProperty();
   		SimpleStringProperty spec = new SimpleStringProperty();
   		SimpleStringProperty mapperId = new SimpleStringProperty();
        int queueLocation = 0;

        TextField fieldState1 = new TextField();
        TextField fieldState2 = new TextField();
        TextField fieldState3 = new TextField();
        TextField fieldState4 = new TextField();
        TextField fieldState5 = new TextField();
        
        public String getUser() {
       	 return user.get();
        }
        
        public String getDateTime() {
       	 return dateTime.get();
        }
        
        public String getEmployer() {
       	 return employer.get();
        }
        
        public String getDescription() {
       	 return description.get();
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
        
        public String getUnits() {
       	 return units.get();
        }
        
        public String getRecords() {
       	 return records.get();
        }
        
        public String getSpecId() {
       	 return specId.get();
        }
        
        public String getSpec() {
       	 return spec.get();
        }
        
        public String getMapperId() {
       	 return mapperId.get();
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
        
        public int getQueueLocation() {
       	 return queueLocation;
        }
        
       String getDate() {return dateTime.get();}

        HBoxFileHistoryCell(int queueLocation, String user, 
       		 								String dateTime, 
       		 								String employer, 
       		 								String description, 
       		 								String units, 
       		 								String records,
       		 								String specId,
       		 								String spec,
       		 								String mapperId,
       		 								String status, 
       		 								String fileType, 
       		 								boolean state1, 
       		 								boolean state2, 
       		 								boolean state3, 
       		 								boolean state4, 
       		 								boolean state5, 
       		 								String result) 
        {
             super();

             //save the requestId;
             this.queueLocation = queueLocation;
             
             if(user == null) user = "";
             if(dateTime == null ) dateTime = "";
             if(employer == null ) employer = "";
             if(description == null ) description = "";
             if(status == null ) status = "";
             if(result == null) result = "";
             if(fileType == null) fileType= "";
             if(units == null) units = "";
             if(records == null) records = "";
             if(specId == null) specId = "";
             if(spec == null) spec = "";
             if(mapperId == null) mapperId = "";
             
             this.user.set(user);
             this.dateTime.set(dateTime);
             this.employer.set(employer);
             this.description.set(description);
             this.user.set(user);
             this.units.set(units);
             this.records.set(records);
             this.specId.set(specId);
             this.spec.set(spec);
             this.mapperId.set(mapperId);
             this.fileType.set("   " + fileType);
             this.status.set(status);
             this.result.set(result);
        }
    }	

	public static class HBoxSpecCell extends HBox 
	{
        Label lblRequest = new Label();
        Label lblSpecificationId = new Label();
        Label lblSpecification = new Label();
        Label lblMapperId = new Label();
        Label lblMapper = new Label();

        public String getSpecId() { return lblSpecificationId.getText(); } 
        
        HBoxSpecCell(String request, String specId, String spec, String mapperId, String mapper, boolean headerRow) 
        {
             super();

             if(request == null) request = "";
             if(specId == null) specId = "";
             if(spec == null) spec = "";
             if(mapperId == null) mapperId = "";
             if(mapper == null) mapper = "";
             
             lblRequest.setText(request);
             lblRequest.setMinWidth(350);
             lblRequest.setMaxWidth(350);
             lblRequest.setPrefWidth(350);
             HBox.setHgrow(lblRequest, Priority.ALWAYS);

             lblSpecificationId.setText(specId);
             lblSpecificationId.setMinWidth(100);
             lblSpecificationId.setMaxWidth(100);
             lblSpecificationId.setPrefWidth(100);
             HBox.setHgrow(lblSpecificationId, Priority.ALWAYS);

             lblSpecification.setText(spec);
             lblSpecification.setMinWidth(350);
             lblSpecification.setMaxWidth(350);
             lblSpecification.setPrefWidth(350);
             HBox.setHgrow(lblSpecification, Priority.ALWAYS);

             lblMapperId.setText(mapperId);
             lblMapperId.setMinWidth(100);
             lblMapperId.setMaxWidth(100);
             lblMapperId.setPrefWidth(100);
             HBox.setHgrow(lblMapperId, Priority.ALWAYS);

             lblMapper.setText(mapper);
             lblMapper.setMinWidth(350);
             lblMapper.setMaxWidth(350);
             lblMapper.setPrefWidth(350);
             HBox.setHgrow(lblMapper, Priority.ALWAYS);
             
          	 if(headerRow == true) 
          	 {
          		 lblRequest.setTextFill(Color.GREY);
          		 lblSpecificationId.setTextFill(Color.GREY);
          		 lblSpecification.setTextFill(Color.GREY);
          		 lblMapperId.setTextFill(Color.GREY);
          		 lblMapper.setTextFill(Color.GREY);
             } 
          	 this.getChildren().addAll(lblRequest, lblSpecificationId, lblSpecification, lblMapperId, lblMapper);
        }
    }	
}