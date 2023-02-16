package com.etc.admin.ui.document;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import com.etc.CoreException;
import com.etc.admin.AdminApp;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.Document;
import com.etc.corvetto.entities.Folder;
import com.etc.corvetto.entities.Organization;
import com.etc.corvetto.rqs.DocumentRequest;
import com.etc.corvetto.rqs.FolderRequest;
import com.etc.corvetto.rqs.OrganizationRequest;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class ViewDocumentController
{
    @FXML
    private TreeTableView<HBoxFileCell> fileTreeView;

    @FXML
    private TableView<HBoxDocumentCell> searchTableView;

    @FXML
    private VBox treeVBox;

    @FXML
    private VBox searchVBox;

    @FXML
    private StackPane searchSkPn;

    @FXML
    private StackPane treeSkPn;

    @FXML
    private TextField folderFilter;

    @FXML
    private Button searchButton;

    @FXML
    private Button displayButton;

    @FXML
    private ComboBox<String> monthRange;

    Organization organization;
    HBoxFileCell cell;

    Account acct;
    List<Account> accounts;

    Folder folder;
    List<Folder> folders;
    List<Document> documents;
    List<Document> docs;
    List<Long> currentFolderListIds = new ArrayList<Long>();

    boolean isLoaded = false;
    int dateRangeMonths = 6;
    int count = 0;
	TableColumn<HBoxDocumentCell, String> sortColumn = null;

    TreeItem<HBoxFileCell> orgFolder = new TreeItem<>();
	TreeItem<HBoxFileCell> subFolder = new TreeItem<>();
    TreeView<HBoxFileCell> tree;
    TreeItem<HBoxFileCell> subSuFolder;
    TreeItem<HBoxFileCell> subSubFolder;
    TreeItem<HBoxFileCell> selectedCell;

    InputStream orgIcon = getClass().getClassLoader().getResourceAsStream("img/Org Icon.png");
 	Image orgIcon1 = new Image(orgIcon, 20f, 20f, true, true);

 	InputStream folderIcon = getClass().getClassLoader().getResourceAsStream("img/FolderIcon.png");
 	Image folderIcon1 = new Image(folderIcon, 15f, 15f, true, true);

 	InputStream fileIcon = getClass().getClassLoader().getResourceAsStream("img/File Icon.png");
 	Image fileIcon1 = new Image(fileIcon, 15f, 15f, true, true);

// 	InputStream downLoadIcon = getClass().getClassLoader().getResourceAsStream("img/Download.ico");
// 	Image downLoadIcon1 = new Image(downLoadIcon, 15f, 15f, true, true);

	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() 
	{
		initControls();
		loadData();
		setColumns();
		setTableColumns();
	}

	private void initControls()
	{
		fileTreeView.setStyle("-fx-font-size: 11;");
		searchVBox.setVisible(false);
		searchButton.setText("Search");
		displayButton.setVisible(false);
		folderFilter.setVisible(false);

		// add the date range choices
		monthRange.getItems().clear();
		monthRange.getItems().add("6 Months");
		monthRange.getItems().add("1 Year");
		monthRange.getItems().add("All");
		monthRange.getSelectionModel().select(0);

		// set a handler for the selections click
		fileTreeView.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1)
            {
            	if(fileTreeView.getSelectionModel().getSelectedItem() != null)
            	{
		        	Folder folderItem = fileTreeView.getSelectionModel().getSelectedItem().getValue().getFolder();
		        	
		        	if(folderItem != null)
		        	{
			        	findFolderData(folderItem);
		        	}
            	}
            }
        });
	}

	private void setColumns() 
	{
		//clear the default values
		fileTreeView.getColumns().clear();

	    TreeTableColumn<HBoxFileCell, String> x1 = new TreeTableColumn<HBoxFileCell, String>("File");
		x1.setCellValueFactory(new TreeItemPropertyValueFactory<HBoxFileCell,String>("fldr"));
		x1.setMinWidth(1350);
		fileTreeView.getColumns().add(x1);
//		setCellFactory(x1);

	    TreeTableColumn<HBoxFileCell, Button> x2 = new TreeTableColumn<HBoxFileCell, Button>("Download");
		x2.setCellValueFactory(new TreeItemPropertyValueFactory<HBoxFileCell,Button>("download"));
		x2.setMinWidth(150);
		fileTreeView.getColumns().add(x2);

		TreeTableColumn<HBoxFileCell, String> x3 = new TreeTableColumn<HBoxFileCell, String>("Born On Date");
	    x3.setCellValueFactory(new TreeItemPropertyValueFactory<HBoxFileCell,String>("bornOnDate"));
	    x3.setMinWidth(180);
		fileTreeView.getColumns().add(x3);
	}

	private void setTableColumns() 
	{
		//clear the default values
		searchTableView.getColumns().clear();

	    TableColumn<HBoxDocumentCell, String> x1 = new TableColumn<HBoxDocumentCell, String>("Document");
		x1.setCellValueFactory(new PropertyValueFactory<HBoxDocumentCell,String>("doc"));
		x1.setMinWidth(1350);
		searchTableView.getColumns().add(x1);
//		setCellFactory(x1);

	    TableColumn<HBoxDocumentCell, Button> x2 = new TableColumn<HBoxDocumentCell, Button>("Download");
		x2.setCellValueFactory(new PropertyValueFactory<HBoxDocumentCell,Button>("download"));
		x2.setMinWidth(150);
		searchTableView.getColumns().add(x2);


		TableColumn<HBoxDocumentCell, String> x3 = new TableColumn<HBoxDocumentCell, String>("Born On Date");
	    x3.setCellValueFactory(new PropertyValueFactory<HBoxDocumentCell,String>("bornOnDate"));
	    x3.setMinWidth(180);
	    searchTableView.getColumns().add(x3);
//		sortColumn = x3;
//		sortColumn.setSortType(SortType.ASCENDING);
	}

	private void setCellFactory(TreeTableColumn<HBoxFileCell, String>  col) 
	{
//		col.setCellFactory(column -> 
//		{
//		    return new TreeTableCell<HBoxFileCell, String>() 
//		    {
//		        @Override
//		        protected void updateItem(String item, boolean empty) 
//		        {
//		            super.updateItem(item, empty);
//	                cell = getTreeTableRow().getItem();
//
//	                if(item == null || empty) 
//		            { 
//		                setText(null);
//		                setStyle("");
//		            } else {
//		                setText(item);
//		                
//		                selectedCell = getTreeTableView().getTreeItem(getIndex());
//
//		                if(cell != null && cell.getFolder() != null && isLoaded == true)
//		                {
//		                	System.out.println(cell.getFolder().getName() + "during click");
//		                	//fileTreeView.getSelectionModel().select(cell.getValue());
//	                		findFolderData(cell.getFolder());
//		                }
//		            }
//		        }
//		    };
//		});
	}

	public void loadData() 
	{ 
    	try {
	
			EtcAdmin.i().setStatusMessage("loading Data..."); 
			EtcAdmin.i().setProgress(.5); 
	 
			Task<Void> task = new Task<Void>() 
			{ 
	            @Override 
	            protected Void call() throws Exception  
	            { 
					OrganizationRequest request = new OrganizationRequest();
					request.setId(DataManager.i().mLocalUser.getOrganization().getId());
					request.setEntity(DataManager.i().mLocalUser.getOrganization());

					// set the current organization
					organization = AdminPersistenceManager.getInstance().get(request);

					EtcAdmin.i().setProgress(.75);
	                return null; 
	            } 
	        };
			AdminApp.getInstance().getFxQueue().put(task);
	        task.setOnSucceeded(e -> showData());
	        task.setOnFailed(e -> showData());

		} catch (InterruptedException e1) { 
			e1.printStackTrace(); }
	    catch (Exception e) { 
	    	DataManager.i().logGenericException(e); 
	    }
	}

	private void showData() 
	{
    	try {

			if(organization != null && isLoaded == false)
			{
				if(organization.getName() != null)
				{
		    		orgFolder = new TreeItem<>(new HBoxFileCell(organization.getFolder(), null), new ImageView(orgIcon1));
			    	fileTreeView.setRoot(orgFolder);
				}
				findFolderData(organization.getFolder());
			}
		EtcAdmin.i().setProgress(0); 
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
	}

	public void findFolderData(Folder folder)
	{ 
    	try {

    		EtcAdmin.i().setStatusMessage("loading Data..."); 
			EtcAdmin.i().setProgress(.5); 

			Task<Void> task = new Task<Void>() 
			{ 
	            @Override 
	            protected Void call() throws Exception  
	            { 
	            	getDateRange();

	    			//load from the server 
	        		FolderRequest folderReq = new FolderRequest();
	        		folderReq.setFolderId(folder.getId());
	    			folders = AdminPersistenceManager.getInstance().getAll(folderReq);

	    			DocumentRequest docReq = new DocumentRequest();

	    			if(dateRangeMonths > 0) 
	    			{
	    				Calendar lastUpdate = null;
	    				lastUpdate = Calendar.getInstance();
	    				lastUpdate.add(Calendar.MONTH, -dateRangeMonths);
	    				docReq.setLastUpdated(lastUpdate.getTimeInMillis());
	    			}

	    			docReq.setFolderId(folder.getId());
	    			documents = AdminPersistenceManager.getInstance().getAll(docReq);
	                return null; 
	            } 
	        };
			AdminApp.getInstance().getFxQueue().put(task);
	        task.setOnSucceeded(e -> loadFolders());
	        task.setOnFailed(e -> loadFolders());

		} catch (InterruptedException e1) { 
			e1.printStackTrace(); }
	    catch (Exception e) { 
	    	DataManager.i().logGenericException(e); 
	    }
	}

	public void loadFolders()
	{
		if(folders != null && folders.size() > 0)
		{
			//ORGANIZING THE FOLDER LIST A TO Z
			folders.sort(new Comparator<Folder>()
		    {
				@Override
				public int compare(Folder o1, Folder o2) { return o1.getName().compareToIgnoreCase(o2.getName()); }
			});

			for(Folder fldr : folders)
			{
				if(isLoaded == true)
				{
		        	fileTreeView.getSelectionModel().getSelectedItem().getChildren().add(new TreeItem<HBoxFileCell>(new HBoxFileCell(fldr, null), new ImageView(folderIcon1)));
		        	fileTreeView.getSelectionModel().getSelectedItem().setExpanded(true);
				} else {
					subFolder = new TreeItem<>(new HBoxFileCell(fldr, null), new ImageView(folderIcon1));
					orgFolder.getChildren().add(subFolder);
					orgFolder.setExpanded(true);
					isLoaded = true;
//					subFolder = new TreeItem<>(new HBoxFileCell(fldr, null, null), new ImageView(folderIcon1));
//					selectedCell.getChildren().add(subFolder);
				}
			}

//        	Folder folderItem = fileTreeView.getSelectionModel().getSelectedItem().getValue().getFolder();
//        	Document docItem = fileTreeView.getSelectionModel().getSelectedItem().getValue().getDocument();
//        	
//        	if(folderItem != null)
//        	{
//	        	fileTreeView.getSelectionModel().getSelectedItem().getChildren().add(new TreeItem<HBoxFileCell>(new HBoxFileCell(folderItem, null, null), new ImageView(folderIcon1)));
//        	} else if(docItem != null)
//        	{
//	        	fileTreeView.getSelectionModel().getSelectedItem().getChildren().add(new TreeItem<HBoxFileCell>(new HBoxFileCell(null, docItem, null), new ImageView(fileIcon1)));
//        	}

//				if(fileTreeView.getSelectionModel().getSelectedIndex() == 0 || isLoaded == true)
//				{
//					System.out.println("should only be in here once");
//    				subFolder.getChildren().add(new ItemTreeNode(new HBoxFileCell(fldr, null, null)));
//					isLoaded = false;
//					System.out.println(flr.getName() + " folder name in second round");
//					subFolder.getChildren().add(new ItemTreeNode(new HBoxFileCell(fldr, null, null)));
//					subFolder = new TreeItem<>(new HBoxFileCell(fldr, null, null), new ImageView(folderIcon1));
//		    		fileTreeView.getSelectionModel().getSelectedItem().getChildren().add(new TreeItem<>(new HBoxFileCell(fldr, null), new ImageView(folderIcon1)));
//	    			subSubFolder = new TreeItem<>(new HBoxFileCell(flr, null, null), new ImageView(folderIcon1));
//	    			selectedCell.getChildren().add(subFolder);
//				}
//			}
		} else if(documents != null && documents.size() > 0)
		{
			//ORGANIZING THE DOCUMENT LIST A TO Z
//			documents.sort(new Comparator<Document>()
//		    {
//				@Override
//				public int compare(Document o1, Document o2) { return o1.getName().compareToIgnoreCase(o2.getName()); }
//			});

			for(Document document : documents)
			{
				fileTreeView.getSelectionModel().getSelectedItem().getChildren().add(new TreeItem<>(new HBoxFileCell(null, document), new ImageView(fileIcon1)));
				fileTreeView.getSelectionModel().getSelectedItem().setExpanded(true);
			}
		}
	}

	public void loadDocs()
	{
    	try {

    		EtcAdmin.i().setStatusMessage("loading Data..."); 
			EtcAdmin.i().setProgress(.5); 

			Task<Void> task = new Task<Void>() 
			{ 
	            @Override 
	            protected Void call() throws Exception  
	            { 
	            	getDateRange();

	    			//load from the server 
	    			DocumentRequest docReq = new DocumentRequest();
	    			docs = AdminPersistenceManager.getInstance().getAll(docReq);
	                return null; 
	            } 
	        };
			AdminApp.getInstance().getFxQueue().put(task);
	        task.setOnSucceeded(e -> loadFileTable());
	        task.setOnFailed(e -> loadFileTable());

		} catch (InterruptedException e1) { 
			e1.printStackTrace(); }
	    catch (Exception e) { 
	    	DataManager.i().logGenericException(e); 
	    }
	}

	public void loadFileTable()
	{
		//Load TableView with Documents
		if(docs != null && docs.size() > 0)
		{
			//ORGANIZING THE DOCUMENTS LIST A TO Z
			docs.sort(new Comparator<Document>()
		    {
				@Override
				public int compare(Document o1, Document o2) { return o2.getBornOn().compareTo(o1.getBornOn()); }
			});

			for(Document document : docs)
			{
				searchTableView.getItems().add(new HBoxDocumentCell(document));
				currentFolderListIds.add(document.getId());
			}
		}
	}

	public void loadFilteredFolders() 
	{
		searchTableView.getItems().clear();

		// load the documents
		if(docs != null)
		{
			//ORGANIZING THE DOCUMENTS LIST BY DATE
//			docs.sort(new Comparator<Document>()
//		    {
//				@Override
//				public int compare(Document o1, Document o2) { return o1.getBornOn().compareTo(o2.getBornOn()); }
//			});

		    String filterSource = "";

			for(Document document : docs)
			{
				if(document.isActive() == false) continue;
		    	if(document.isDeleted() == true) continue;

				if(folderFilter.getText().length() > 0) 
				{
					filterSource = document.getName() + " " + Utils.getDateString(document.getBornOn()); 

			    	if(filterSource.toUpperCase().contains(folderFilter.getText().toUpperCase()) == false) continue;
				}
				searchTableView.getItems().add(new HBoxDocumentCell(document));
			}
		}
	}

	private void getDateRange()
	{
		dateRangeMonths = 6;

		if(monthRange.getValue() == "1 Year")
			dateRangeMonths = 12;

		if(monthRange.getValue() == "All")
			dateRangeMonths = -1;
	}

	public void download()
	{
	   try {

	 		Long docId = 0l;
			HBoxFileCell cellItem = fileTreeView.getSelectionModel().getSelectedItem().getValue();
			docId = fileTreeView.getSelectionModel().getSelectedItem().getValue().getDocument().getId();

			if(cellItem != null)
			{
		 		String path = Utils.browseDirectory((Stage)fileTreeView.getScene().getWindow(), "");
		 		if(path == "") return;
		 		
				if(Utils.downloadFile(docId, path) == false) { return; }

				Utils.showAlert("File Downloaded", "The selected file has been downloaded to " + path);
			} else {
				Utils.showAlert("No Selection", "Please select file entry to Download");
				return;
			}
	     }catch (Exception e) {
	    	 Utils.showAlert("No Selection", "Please select file entry to download.");
	     }
     }

	public void downloadDoc()
	{
	   try {

	 		Long docId = 0l;
			HBoxDocumentCell cellItem = searchTableView.getSelectionModel().getSelectedItem();
			docId = searchTableView.getSelectionModel().getSelectedItem().getDocum().getId();

			if(cellItem != null)
			{
		 		String path = Utils.browseDirectory((Stage)searchTableView.getScene().getWindow(), "");
		 		if(path == "") return;
		 		
				if(Utils.downloadFile(docId, path) == false) { return; }

				Utils.showAlert("File Downloaded", "The selected file has been downloaded to " + path);
			} else {
				Utils.showAlert("No Selection", "Please select file entry to Download");
				return;
			}
	    }catch (Exception e) {
	    	Utils.showAlert("No Selection", "Please select file entry to download.");
	    }
    }

    @FXML
    void onFileSelect(ActionEvent event) 
    {

    }

    @FXML
    void onFolderFilter(KeyEvent event)
    {
    	loadFilteredFolders();
    }
    
    @FXML
    void onSearchButton(ActionEvent event) 
    {
    	treeSkPn.setVisible(false);
    	treeVBox.setVisible(false);
    	searchVBox.setVisible(true);
    	folderFilter.setVisible(true);
    	displayButton.setVisible(true);
    	displayButton.setText("Display");
    	loadDocs();
    }

    @FXML
    void onDisplayButton(ActionEvent event) 
    {
    	searchVBox.setVisible(false);
    	treeSkPn.setVisible(true);
    	treeVBox.setVisible(true);
    	searchVBox.setVisible(false);
    	folderFilter.setVisible(false);
    	displayButton.setVisible(false);
    	searchButton.setText("Search");

    	if(searchTableView.getSelectionModel().getSelectedItem() != null)
    	{
			fileTreeView.setRoot(null);
    		Document document = searchTableView.getSelectionModel().getSelectedItem().getDocum();

    		try {

	    		FolderRequest request = new FolderRequest();
	    		request.setId(document.getFolderId());
				DataManager.i().mFolders = AdminPersistenceManager.getInstance().getAll(request);

    		} catch (CoreException e) {
				e.printStackTrace();
			}

    		if(document.getFolder() != null)
    		{
        		for(Folder folder : DataManager.i().mFolders)
        		{
    				if(folder.getId().equals(document.getFolderId()))
    				{
//        		    	findFolderData(document.getFolder());
    		    		fileTreeView.setRoot(new TreeItem<>(new HBoxFileCell(folder, null), new ImageView(folderIcon1)));
    				}
        		}
    		} else {
    			fileTreeView.setRoot(new TreeItem<>(new HBoxFileCell(null, document), new ImageView(fileIcon1)));
    		}
    	}
    }

    @FXML
    void onMonthRange(ActionEvent event) 
    {
    	loadData();
    }

//	public static class ItemTreeNode extends TreeItem<HBoxFileCell> 
//	{
//        private boolean childrenLoaded = false;
//
//        public ItemTreeNode(HBoxFileCell value) {
//            super(value);
//        }
//
//        @Override
//        public boolean isLeaf()
//        {
//            if (childrenLoaded) 
//            {
//                return getChildren().isEmpty() ;
//            }
//            return false ;
////          return getChildren().isEmpty();
//        }
//
//        @Override
//        public ObservableList<TreeItem<HBoxFileCell>> getChildren() 
//        {
//            if (childrenLoaded) 
//            {
//                return super.getChildren();
//            }
//
//            childrenLoaded = true ;
//            if (getValue().getValue() < 100_000)
//            {
//                List<TreeItem<HBoxFileCell>> children = new ArrayList<>();
//                for (int i = 0 ; i < 10; i++) 
//                {
//                    children.add(new ItemTreeNode(new HBoxFileCell(null, null, getValue().getValue() * 10 + i)));
//                }
//                super.getChildren().addAll(children);
//            } else {
//                // great big hack:
//                super.getChildren().add(null);
//                super.getChildren().clear();
//            }
//            return super.getChildren() ;
//        }
//    }	

	public class HBoxFileCell
	{
		SimpleStringProperty fldr = new SimpleStringProperty();
		SimpleStringProperty bornOnDate = new SimpleStringProperty();
		Button download = new Button("Download");

		Folder foldr;
		Document docum;
		String bornOnDate1;

		public Folder getFolder() { return foldr; }
		public Document getDocument() { return docum; }
	    public String getFldr() { return fldr.get(); }
	    public Button getDownload() { return download; }
	    public String getBornOnDate() { return bornOnDate.get(); }

	    HBoxFileCell(Folder folder, Document document) 
	    {
	        super();
	        
	        this.foldr = folder;
	        this.docum = document;

	        download.setTextFill(Paint.valueOf("white"));
	        download.setStyle("-fx-background-color:royalblue;");
	        
			download.setOnMouseClicked(mouseClickedEvent -> 
			{
		        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) 
		        {
		        	download();
		        }
	        });

	        if(foldr != null && document == null) 
	        {
	        	download.setVisible(false);
	        	fldr.set(String.valueOf(foldr.getName()) + " /");
	        	bornOnDate.set(Utils.getDateString(foldr.getBornOn()));
	        } else if(document != null)
	        {
	        	fldr.set(String.valueOf(document.getName() + " /"));
	        	bornOnDate.set(Utils.getDateString(document.getBornOn()));
	        }
	    }
	}

	public class HBoxDocumentCell
	{
		SimpleStringProperty doc = new SimpleStringProperty();
		SimpleStringProperty bornOnDate = new SimpleStringProperty();
		Button download = new Button("Download");

		Document docum;
		String bornOnDate1;

		public Document getDocum() { return docum; }
	    public Button getDownload() { return download; }
	    public String getDoc() { return doc.get(); }
	    public String getBornOnDate() { return bornOnDate.get(); }

	    HBoxDocumentCell(Document document) 
	    {
	        super();

	        this.docum = document;

	        download.setTextFill(Paint.valueOf("white"));
	        download.setStyle("-fx-background-color:royalblue;");
	        
			download.setOnMouseClicked(mouseClickedEvent -> 
			{
		        if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 1) 
		        {
		        	downloadDoc();
		        }
	        });

	        if(document != null)
	        {
	        	doc.set(document.getName() + " /");
	        	bornOnDate.set(Utils.getDateString(document.getBornOn()));
	        }
	    }
	}
}
