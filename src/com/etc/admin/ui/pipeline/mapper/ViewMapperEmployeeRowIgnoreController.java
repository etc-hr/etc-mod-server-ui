package com.etc.admin.ui.pipeline.mapper;

import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileIgnoreRowSpecificationRequest;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ViewMapperEmployeeRowIgnoreController 
{
	@FXML
	private ListView<HBoxDetailCell> dfcovSelectedSpecView;

	@FXML
	private GridPane ignoreList;
	
	@FXML
	private TextField dfcovNameField;
	
	@FXML
	private Button dfcovCancelButton;
	
	@FXML
	private Button dfcovSaveChangesButton;
	
	@FXML
	private Button dfcovRemoveSpecButton;
	
	@FXML
	private Label dfcovName;
	
	@FXML
	private CheckBox emplId;
	
	@FXML
	private CheckBox empFstNm;
	
	@FXML
	private CheckBox empDOB;
	
	@FXML
	private CheckBox empJbTit;
	
	@FXML
	private CheckBox empStNm;
	
	@FXML
	private CheckBox empCity;
	
	@FXML
	private CheckBox etcEmpId;
	
	@FXML
	private CheckBox empLoc;
	
	@FXML
	private CheckBox empDept;

	@FXML
	private CheckBox empMdNm;
	
	@FXML
	private CheckBox empHrDt;
	
	@FXML
	private CheckBox empRHrDt;
	
	@FXML
	private CheckBox empPhNm;
	
	@FXML
	private CheckBox empAddLn1;
	
	@FXML
	private CheckBox empState;
	
	@FXML
	private CheckBox empSSN;
	
	@FXML
	private CheckBox empPay;
	
	@FXML
	private CheckBox empUnType;
	
	@FXML
	private CheckBox empGend;
	
	@FXML
	private CheckBox empLstNm;
	
	@FXML
	private CheckBox empTrmDt;
	
	@FXML
	private CheckBox empEml;
	
	@FXML
	private CheckBox empAddLn2;
	
	@FXML
	private CheckBox empZip;
	
	@FXML
	private CheckBox emprEmpId;
	
	@FXML
	private CheckBox empCF1;
	
	@FXML
	private CheckBox empCF2;
	
	@FXML
	private CheckBox empCF3;
	
	@FXML
	private CheckBox empCF4;
	
	@FXML
	private CheckBox empCF5;
	
	// using an array of strings to hold and sort our column field data
	String[] columnFields = new String[100];
	int[] columnCount = new int[100];
	int fieldCount = 0;
	
	int alphaId = 0;
	
	// read only mode for selected mapper files
	public boolean addMode = false;
	public boolean changesMade = false;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {

	}
	
	public void load()
	{
		initControls();
		loadIgnoreRowData();
	}
	
	private void initControls() 
	{
		setAddMode(false);
		dfcovSaveChangesButton.setDisable(true);

 	}	
	
	private void loadIgnoreRowData() 
	{
		dfcovSaveChangesButton.setDisable(false);
		if(DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification != null)
			loadFields(DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification);
		else {
			loadAddSpec();
		}
	}
	
	private void setAddMode(boolean mode)
	{
		addMode = mode;
		if(mode == true)
		{
			dfcovSaveChangesButton.setText("Add");
			dfcovSaveChangesButton.setDisable(false);
			dfcovRemoveSpecButton.setVisible(false);
		}
		else 
		{
			dfcovSaveChangesButton.setText("Save");
			dfcovSaveChangesButton.setDisable(true);
			dfcovRemoveSpecButton.setVisible(true);
		}
	}
	
	private void clearData() {
		dfcovSelectedSpecView.getItems().clear();
	}
	
	private void loadFields(DynamicEmployeeFileIgnoreRowSpecification igSpec) 
	{
		//create the spec in case it's null
		if(igSpec == null) igSpec = new DynamicEmployeeFileIgnoreRowSpecification();
	    if(igSpec.getName() != null) dfcovNameField.setText(igSpec.getName());

	    // LOAD UP THE FIELD LIST
	    emplId.setSelected(igSpec.isErCol());
	    empFstNm.setSelected(igSpec.isfNameCol());
		empDept.setSelected(igSpec.isDeptCol());
		empLoc.setSelected(igSpec.isLocCol());
		empDOB.setSelected(igSpec.isDobCol());
		empJbTit.setSelected(igSpec.isJobTtlCol());
		empStNm.setSelected(igSpec.isStreetNumCol());
		empCity.setSelected(igSpec.isCityCol());
		etcEmpId.setSelected(igSpec.isEtcRefCol());
		empMdNm.setSelected(igSpec.ismNameCol());
		empHrDt.setSelected(igSpec.isHireDtCol());
		empRHrDt.setSelected(igSpec.isRhireDtCol());
		empPhNm.setSelected(igSpec.isPhoneCol());
		empAddLn1.setSelected(igSpec.isStreetCol());
		empState.setSelected(igSpec.isStateCol());
		empSSN.setSelected(igSpec.isSsnCol());
		empPay.setSelected(igSpec.isPayCodeCol());
		empUnType.setSelected(igSpec.isUnionTypeCol());
		empGend.setSelected(igSpec.isGenderCol());
		empLstNm.setSelected(igSpec.islNameCol());
		empTrmDt.setSelected(igSpec.isTermDtCol());
		empEml.setSelected(igSpec.isEmlCol());
		empAddLn2.setSelected(igSpec.isLin2Col());
		empZip.setSelected(igSpec.isZipCol());
		emprEmpId.setSelected(igSpec.isErRefCol());
		empCF1.setSelected(igSpec.isCfld1Col());
		empCF2.setSelected(igSpec.isCfld2Col());
		empCF3.setSelected(igSpec.isCfld3Col());
		empCF4.setSelected(igSpec.isCfld4Col());
		empCF5.setSelected(igSpec.isCfld5Col());
	}
	
	private void updateIgnoreRowSpecification()
	{
		try {
			
			if(addMode != true)
			{
				//create a new DynamicEmployeeFileIgnoreRowSpecification to hold the data
				DynamicEmployeeFileIgnoreRowSpecificationRequest addRequest = new DynamicEmployeeFileIgnoreRowSpecificationRequest(DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification);
				DynamicEmployeeFileIgnoreRowSpecification ignoreSpec = DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification;
				setRequestColValue(ignoreSpec);
				ignoreSpec.setName(dfcovNameField.getText());
				ignoreSpec.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
				ignoreSpec.setSpecification(DataManager.i().mDynamicEmployeeFileSpecification);
				addRequest.setEntity(ignoreSpec);
				addRequest.setId(ignoreSpec.getId());
				
				AdminPersistenceManager.getInstance().addOrUpdate(addRequest);
			} else {
				//create a new DynamicEmployeeFileIgnoreRowSpecification to hold the data
				DynamicEmployeeFileIgnoreRowSpecificationRequest addRequest = new DynamicEmployeeFileIgnoreRowSpecificationRequest(DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification);
				DynamicEmployeeFileIgnoreRowSpecification ignoreSpec = new DynamicEmployeeFileIgnoreRowSpecification();
				setRequestColValue(ignoreSpec);
				ignoreSpec.setName(dfcovNameField.getText());
				ignoreSpec.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
				ignoreSpec.setSpecification(DataManager.i().mDynamicEmployeeFileSpecification);
				addRequest.setEntity(ignoreSpec);
				addRequest.setId(ignoreSpec.getId());
				
				AdminPersistenceManager.getInstance().addOrUpdate(addRequest);
			}
		} catch (CoreException e) { DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }
	}
	
	private void saveFields(DynamicEmployeeFileIgnoreRowSpecification request)
	{
		DynamicEmployeeFileSpecification fSpec = DataManager.i().mDynamicEmployeeFileSpecification;
		request.setSpecification(fSpec);
		setRequestColValue(request);
	}

	private void setRequestColValue(DynamicEmployeeFileIgnoreRowSpecification igSpec)
	{
		//EMPLOYEE
		igSpec.setErCol(emplId.isSelected());
		igSpec.setfNameCol(empFstNm.isSelected());
		igSpec.setDeptCol(empDept.isSelected());
		igSpec.setLocCol(empLoc.isSelected());
		igSpec.setDobCol(empDOB.isSelected());
		igSpec.setJobTtlCol(empJbTit.isSelected());
		igSpec.setStreetNumCol(empStNm.isSelected());
		igSpec.setCityCol(empCity.isSelected());
		igSpec.setEtcRefCol(etcEmpId.isSelected());
		igSpec.setmNameCol(empMdNm.isSelected());
		igSpec.setHireDtCol(empHrDt.isSelected());
		igSpec.setRhireDtCol(empRHrDt.isSelected());
		igSpec.setPhoneCol(empPhNm.isSelected());
		igSpec.setStreetCol(empAddLn1.isSelected());
		igSpec.setStateCol(empState.isSelected());
		igSpec.setSsnCol(empSSN.isSelected());
		igSpec.setPayCodeCol(empPay.isSelected());
		igSpec.setUnionTypeCol(empUnType.isSelected());
		igSpec.setGenderCol(empGend.isSelected());
		igSpec.setlNameCol(empLstNm.isSelected());
		igSpec.setTermDtCol(empTrmDt.isSelected());
		igSpec.setEmlCol(empEml.isSelected());
		igSpec.setLin2Col(empAddLn2.isSelected());
		igSpec.setZipCol(empZip.isSelected());
		igSpec.setErRefCol(emprEmpId.isSelected());
		igSpec.setCfld1Col(empCF1.isSelected());
		igSpec.setCfld2Col(empCF2.isSelected());
		igSpec.setCfld3Col(empCF3.isSelected());
		igSpec.setCfld4Col(empCF4.isSelected());
		igSpec.setCfld5Col(empCF5.isSelected());
	}
	
	public void setCoverageFile(int nPipelineSpecificationID) 
	{
		DataManager.i().loadDynamicCoverageFileSpecification(nPipelineSpecificationID);
	}
	
	@FXML
	private void onSave(ActionEvent event)
	{
		//validate we have minimum data
		if(Utils.validate(dfcovNameField) == false)
			return;
		
		changesMade = true;
		updateIgnoreRowSpecification();
		Stage stage =(Stage) dfcovNameField.getScene().getWindow();
		stage.close();
	}	

	@FXML
	private void onCancel(ActionEvent event)
	{
		changesMade = false;
		Stage stage =(Stage) dfcovNameField.getScene().getWindow();
		stage.close();
	}	
	
	private void loadAddSpec() 
	{
		setAddMode(true);
		loadFields(null);
	}
	
	@FXML
	private void onRemoveSpec(ActionEvent event)
	{
		if(addMode == true) return;
		
		try {
			DynamicEmployeeFileIgnoreRowSpecification ieSpec = DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification;
			saveFields(ieSpec);
			DynamicEmployeeFileIgnoreRowSpecificationRequest eRequest = new DynamicEmployeeFileIgnoreRowSpecificationRequest();
			eRequest.setId(ieSpec.getId());
			
			AdminPersistenceManager.getInstance().remove(eRequest);
		} catch (CoreException e) { DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }

		// mark it as changed and return
		changesMade = true;
		Stage stage =(Stage) dfcovNameField.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	private void onClose(ActionEvent event)
	{
		Stage stage =(Stage) dfcovNameField.getScene().getWindow();
		stage.close();
	}	
	
//		public String getIndexString(int index) 
//		{
//		    String letters;
//			char c = 'A';
//		    char c1 = 'A';
//		    
//	    	if(alphaId < 26) 
//	    	{
//	    		c += alphaId;
//	    		letters =  String.valueOf(c) + index;
//	    	} else 
//	    	{
//	    		int letterPos = alphaId/26;
//	    		c1 += letterPos;
//	    		//c1 +=(letterPos - 1 + alphaId); // starting with A
//	    		letters = String.valueOf(c1) + String.valueOf(c + alphaId) + index;
//	    	}
//
//	    	alphaId++;
//	    	return letters;
//		}
	
	public class HBoxDetailCell extends HBox {
        boolean headerCell;
  
         // first column
        TextField nameField = new TextField();
        Label nameLabel = new Label();
               
        // second column
        Label columnIndexLabel = new Label();
         
        // third column
        Label requiredLabel = new Label();
        CheckBox requiredCheck = new CheckBox();
        
        public boolean setValue(String label, boolean value) {
        	if(nameLabel.getText().contains(label)) {
        		requiredCheck.setSelected(value);
        		return true;
        	}else
        		return false;
        }
        
        //default constructor is a header row
        HBoxDetailCell() {
            super();
            if(DataManager.i().isWindows() == true) {
            	nameLabel.setMinWidth(200);
            	nameLabel.setPrefWidth(200);
            	columnIndexLabel.setMinWidth(100);
            	columnIndexLabel.setPrefWidth(100);
            	requiredLabel.setMinWidth(100);
            	requiredLabel.setPrefWidth(100);
            }else {
            	nameLabel.setMinWidth(200);
            	nameLabel.setPrefWidth(200);
            	columnIndexLabel.setMinWidth(100);
            	columnIndexLabel.setPrefWidth(100);
            	requiredLabel.setMinWidth(100);
            	requiredLabel.setPrefWidth(100);
            }

            nameLabel.setText("Name"); 
            nameLabel.setFont(Font.font(null, FontWeight.BOLD, 13));
       	    nameLabel.setAlignment(Pos.CENTER);
       	    columnIndexLabel.setText("Col"); 
       	    columnIndexLabel.setFont(Font.font(null, FontWeight.BOLD, 13));
       	    columnIndexLabel.setAlignment(Pos.CENTER);
       	    requiredLabel.setText("Required"); 
       	    requiredLabel.setFont(Font.font(null, FontWeight.BOLD, 13));
       	    requiredLabel.setAlignment(Pos.CENTER_LEFT);
       	    headerCell = true; 
 
       	    this.getChildren().addAll(nameLabel, columnIndexLabel, requiredLabel);
        }
        
        // header
        HBoxDetailCell(String header) {
            super();

            nameLabel.setText(header); 
            nameLabel.setMinWidth(695);
            nameLabel.setPrefWidth(695);
            nameLabel.setFont(Font.font(null, FontWeight.BOLD, 13));
            nameLabel.setAlignment(Pos.CENTER);
       	    headerCell = true;
       	    this.getChildren().addAll(nameLabel);
        }
        
        HBoxDetailCell(String name, boolean column, String columnIndex, boolean required) {
             super();
             
             //set up the separators
             headerCell = false;
 
             if(DataManager.i().isWindows() == true) {
                 nameField.setMinWidth(200);
                 nameField.setPrefWidth(200);
                 nameLabel.setMinWidth(200);
                 nameLabel.setPrefWidth(200);
                 columnIndexLabel.setMinWidth(100);
                 columnIndexLabel.setPrefWidth(100);
                 requiredCheck.setMinWidth(100);
                 requiredCheck.setPrefWidth(100);
             }else {
                 nameField.setMinWidth(200);
                 nameField.setPrefWidth(200);
                 nameLabel.setMinWidth(200);
                 nameLabel.setPrefWidth(200);
                 columnIndexLabel.setMinWidth(100);
                 columnIndexLabel.setPrefWidth(100);
                 requiredCheck.setMinWidth(100);
                 requiredCheck.setPrefWidth(100);
             }
                         
             if(name == "" || name == null)
            	 nameField.setPromptText("Custom Field");
             else {
            	 // strip out the patterns and formats
            	 if(name.contains(" (")) {
            		 int index = name.indexOf(" (");
            		 name = name.substring(0, index);
            	 }
            	 nameField.setText(name);
             }
             nameLabel.setText(name);

             // set the column if selected in the ignorerow object
             requiredCheck.setSelected(required);
             
        	 requiredCheck.setAlignment(Pos.CENTER);
        	 columnIndexLabel.setAlignment(Pos.CENTER);
        	 
        	 // set the controls according to the view's readonly member
             if(column == false) {
            	 requiredCheck.setDisable(true);
	             nameField.setEditable(false);
            	 nameLabel.setFont(Font.font(null, FontWeight.NORMAL, 13));
            	 columnIndexLabel.setFont(Font.font(null, FontWeight.NORMAL, 13));
      	   	    this.getChildren().addAll(nameLabel);
             } else {
            	 nameLabel.setFont(Font.font(null, FontWeight.BOLD, 13));
            	 columnIndexLabel.setText(columnIndex);
            	 columnIndexLabel.setFont(Font.font(null, FontWeight.BOLD, 13));
            	 requiredCheck.setDisable(false);
            	 if(nameLabel.getText().trim().length() == 0)
           	   	    this.getChildren().addAll(nameLabel, columnIndexLabel);
            	 else
      	   	    	this.getChildren().addAll(nameLabel, columnIndexLabel, requiredCheck);
             }
        }
        
        public boolean isHeader() {
        	return headerCell;
        }
        
        public void clearCell() {
        	requiredCheck.setSelected(false);
        }
        
        public String getName() {
        	return nameField.getText();
        }
        
        public Boolean getColumn() {
        	return requiredCheck.isSelected(); 
        }
        
    }	
	
	public class HBoxIgnoreRowCell extends HBox 
	{
        Label lblCol1 = new Label();
        Label lblCol2 = new Label();
        DynamicCoverageFileIgnoreRowSpecification covSpec;
        DynamicEmployeeFileIgnoreRowSpecification empSpec;
        DynamicPayFileIgnoreRowSpecification paySpec;    

        HBoxIgnoreRowCell(DynamicCoverageFileIgnoreRowSpecification covSpec, DynamicEmployeeFileIgnoreRowSpecification empSpec, DynamicPayFileIgnoreRowSpecification paySpec) 
        {
             super();
             
             if(covSpec != null) 
             {
            	 lblCol1.setText(Utils.getDateString(covSpec.getLastUpdated()));
            	 lblCol2.setText(covSpec.getName());
            	 this.covSpec = covSpec;
             }
          
             if(empSpec != null)
             {
            	 lblCol1.setText(Utils.getDateString(empSpec.getLastUpdated()));
            	 lblCol2.setText(empSpec.getName());
            	 this.empSpec = empSpec;
             }
          
             if(paySpec != null) 
             {
            	 lblCol1.setText(Utils.getDateString(paySpec.getLastUpdated()));
            	 lblCol2.setText(paySpec.getName());
            	 this.paySpec = paySpec;
             }
          
             lblCol1.setMinWidth(100);
             lblCol1.setMaxWidth(100);
             lblCol1.setPrefWidth(100);
             HBox.setHgrow(lblCol1, Priority.ALWAYS);

             lblCol2.setMinWidth(275);
             lblCol2.setMaxWidth(275);
             lblCol2.setPrefWidth(275);
             HBox.setHgrow(lblCol2, Priority.ALWAYS);

             this.getChildren().addAll(lblCol1, lblCol2);
        }

        public DynamicCoverageFileIgnoreRowSpecification getCovSpec() {
        	return covSpec; 
        }
        
        public DynamicEmployeeFileIgnoreRowSpecification getEmpSpec() {
        	return empSpec; 
        }
        
        public DynamicPayFileIgnoreRowSpecification getPaySpec() {
        	return paySpec; 
        }
	}
}


