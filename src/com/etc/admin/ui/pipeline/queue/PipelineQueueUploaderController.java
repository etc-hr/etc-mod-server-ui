package com.etc.admin.ui.pipeline.queue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.uploader.LoadingTask;
import com.etc.admin.ui.uploader.UploadForm;
import com.etc.admin.ui.uploader.UploadTask;
import com.etc.corvetto.ems.pipeline.rqs.PipelineSpecificationRequest;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.UploadType;
import com.etc.corvetto.rqs.EmployerRequest;
import com.etc.corvetto.rqs.UploadRequest;
import com.etc.corvetto.rqs.UploadTypeRequest;
import com.etc.entities.DataProperty;
import com.etc.entities.DataPropertyType;
import com.etc.utils.crypto.Cryptographer;
import com.etc.utils.crypto.CryptographyException;
import com.etc.utils.file.FileGlobber;

import javafx.application.Platform;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class PipelineQueueUploaderController 
{
	@FXML
    private AnchorPane anchorPane;

    @FXML
    private VBox mainVBox;

    @FXML
    private VBox topSectionVBox;

    @FXML
    private ComboBox<String> accountCombo;

    @FXML
    private ComboBox<HBoxEmployerCell> employerCombo;

    @FXML
    private Label specs;

    @FXML
    private TextField fileLocation;

    @FXML
    private Button browse;

    @FXML
    private ComboBox<String> type;

    @FXML
    private CheckBox encrypted;

    @FXML
    private VBox dataTypeVBox;

    @FXML
    private VBox bottomSectionVBox;

    @FXML
    private Button upload;

    @FXML
    private Button cancel;

    @FXML
    private Label message;

    @FXML
    private ProgressBar progress;

    @FXML
    private Button accountClear;
 
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
	Date day45 = new Date();
//    private int hourRange = 24;
//    private int range = 0;

	Logger logr = DataManager.i().mLogger;
	Account account = null;
	UploadType uploadType = null;
	Employer employer = null;


	List<Employer> employers = null;
	List<UploadType> uploadTypes = null;

    @FXML
    public void initialize()
    {
    	VBox.setVgrow(dataTypeVBox, Priority.ALWAYS);
    	VBox.setVgrow(topSectionVBox, Priority.ALWAYS);
    	VBox.setVgrow(bottomSectionVBox, Priority.ALWAYS);

    	initControls();
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

    	fileLocation.setEditable(false);
    	fileChooser.setTitle("Select a file...");
    }
	
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
        task.setOnSucceeded(e -> setCombos()); 
        task.setOnFailed(e -> setCombos()); 
        new Thread(task).start(); 	
	}

	private void setCombos()
	{
		accountCombo.setOpacity(1);
		employerCombo.setOpacity(1);
		accountCombo.setEditable(true);
		employerCombo.setEditable(true);
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

			try {
				accountCombo.getSelectionModel().select(employer.getAccount().getName());

				acctFirst = false;
				acctSec = true;
	
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

				if(acctSec == true)
				{
					loadSearchEmployers();
				}
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
		searchEmployer();
		if(acctFirst == false) { loadSearchAccounts(); }
		refreshUploadCombo();
	}
	
	public void searchEmployer() 
	{
		String sSelection = String.valueOf(employerCombo.getSelectionModel().getSelectedItem());

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
				    	isUploading = false;
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
    	acctFirst = false;
    	acctSec = false;
    	updateData();
//    	accountCombo.setValue(null);
    	account = null;
    	employer = null;
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
}
