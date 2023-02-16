package com.etc.admin.ui.pipeline.mapper;

import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.rqs.cov.DynamicCoverageFileIgnoreRowSpecificationRequest;

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

public class ViewMapperCoverageRowIgnoreController
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
	private CheckBox empCvgCls;
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
	private CheckBox empFlag;
	@FXML
	private CheckBox empMdNm;
	@FXML
	private CheckBox empHrDt;
	@FXML
	private CheckBox empPhNm;
	@FXML
	private CheckBox empAddLn1;
	@FXML
	private CheckBox empState;
	@FXML
	private CheckBox empSSN;
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
	@FXML
	private CheckBox depEtcId;
	@FXML
	private CheckBox depFstNm;
	@FXML
	private CheckBox depEmprId;
	@FXML
	private CheckBox depAddLn2;
	@FXML
	private CheckBox depZip;
	@FXML
	private CheckBox depSSN;
	@FXML
	private CheckBox depMdNm;
	@FXML
	private CheckBox depStrtNm;
	@FXML
	private CheckBox depCity;
	@FXML
	private CheckBox depDOB;
	@FXML
	private CheckBox depLstNm;
	@FXML
	private CheckBox depAddLn1;
	@FXML
	private CheckBox depState;
	@FXML
	private CheckBox depCF1;
	@FXML
	private CheckBox depCF2;
	@FXML
	private CheckBox depCF3;
	@FXML
	private CheckBox depCF4;
	@FXML
	private CheckBox depCF5;
	@FXML
	private CheckBox covMbrNm;
	@FXML
	private CheckBox covWaiv;
	@FXML
	private CheckBox covStrtDt;
	@FXML
	private CheckBox covAnnCov;
	@FXML
	private CheckBox covJan;
	@FXML
	private CheckBox covMay;
	@FXML
	private CheckBox covSept;
	@FXML
	private CheckBox covDecDt;
	@FXML
	private CheckBox covInelg;
	@FXML
	private CheckBox covEndDt;
	@FXML
	private CheckBox covTxYr;
	@FXML
	private CheckBox covFeb;
	@FXML
	private CheckBox covJune;
	@FXML
	private CheckBox covOct;
	@FXML
	private CheckBox covPlnId;
	@FXML
	private CheckBox covDeduc;
	@FXML
	private CheckBox covSubNm;
	@FXML
	private CheckBox covMar;
	@FXML
	private CheckBox covJuly;
	@FXML
	private CheckBox covNov;
	@FXML
	private CheckBox covMbrShrAmt;
	@FXML
	private CheckBox covCF1;
	@FXML
	private CheckBox covCF2;
	@FXML
	private CheckBox covApr;
	@FXML
	private CheckBox covAug;
	@FXML
	private CheckBox covDec;

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
	
	public void load() {
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

		if(DataManager.i().mDynamicCoverageFileIgnoreRowSpecification != null)
			loadFields(DataManager.i().mDynamicCoverageFileIgnoreRowSpecification);
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
	
	private void loadFields(DynamicCoverageFileIgnoreRowSpecification igSpec ) 
	{
		//create the spec in case it's null
		if(igSpec == null) igSpec = new DynamicCoverageFileIgnoreRowSpecification();
	    if(igSpec.getName() != null) dfcovNameField.setText(igSpec.getName());

	    // LOAD UP THE FIELD LIST
	    emplId.setSelected(igSpec.isErCol());
	    empFstNm.setSelected(igSpec.isfNameCol());
		empDOB.setSelected(igSpec.isDobCol());
		empJbTit.setSelected(igSpec.isJobTtlCol());
		empStNm.setSelected(igSpec.isStreetNumCol());
		empCity.setSelected(igSpec.isCityCol());
		etcEmpId.setSelected(igSpec.isEtcRefCol());
		empFlag.setSelected(igSpec.isEeFlagCol());
		empMdNm.setSelected(igSpec.ismNameCol());
		empHrDt.setSelected(igSpec.isHireDtCol());
		empPhNm.setSelected(igSpec.isPhoneCol());
		empAddLn1.setSelected(igSpec.isStreetCol());
		empState.setSelected(igSpec.isStateCol());
		empSSN.setSelected(igSpec.isSsnCol());
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

		//DEPENDENT
		depEtcId.setSelected(igSpec.isDepEtcRefCol());
		depFstNm.setSelected(igSpec.isDepFNameCol());
		depEmprId.setSelected(igSpec.isDepErRefCol());
		depAddLn2.setSelected(igSpec.isDepLin2Col());
		depZip.setSelected(igSpec.isDepZipCol());
		depSSN.setSelected(igSpec.isDepSSNCol());
		depMdNm.setSelected(igSpec.isDepMNameCol());
		depStrtNm.setSelected(igSpec.isDepStreetNumCol());
		depCity.setSelected(igSpec.isDepCityCol());
		depDOB.setSelected(igSpec.isDepDOBCol());
		depLstNm.setSelected(igSpec.isDepLNameCol());
		depAddLn1.setSelected(igSpec.isDepStreetCol());
		depState.setSelected(igSpec.isDepStateCol());
		depCF1.setSelected(igSpec.isDepCfld1Col());
		depCF2.setSelected(igSpec.isDepCfld2Col());
		depCF3.setSelected(igSpec.isDepCfld3Col());
		depCF4.setSelected(igSpec.isDepCfld4Col());
		depCF5.setSelected(igSpec.isDepCfld5Col());
		
		//COVERAGE
		covMbrNm.setSelected(igSpec.isMbrCol());
		covWaiv.setSelected(igSpec.isWavdCol());
		covStrtDt.setSelected(igSpec.isCovStartDtCol());
		covAnnCov.setSelected(igSpec.isTySelCol());
		covJan.setSelected(igSpec.isJanSelCol());
		covMay.setSelected(igSpec.isMaySelCol());
		covSept.setSelected(igSpec.isSepSelCol());
		covInelg.setSelected(igSpec.isInelCol());
		covEndDt.setSelected(igSpec.isCovEndDtCol());
		covTxYr.setSelected(igSpec.isTyCol());
		covFeb.setSelected(igSpec.isFebSelCol());
		covJune.setSelected(igSpec.isJunSelCol());
		covOct.setSelected(igSpec.isOctSelCol());
		covPlnId.setSelected(igSpec.isPlanRefCol());
		covSubNm.setSelected(igSpec.isSubcrbrCol());
		covMar.setSelected(igSpec.isMarSelCol());
		covJuly.setSelected(igSpec.isJulSelCol());
		covNov.setSelected(igSpec.isNovSelCol());
		covMbrShrAmt.setSelected(igSpec.isMbrShareCol());
		covCF1.setSelected(igSpec.isCovCfld1Col());
		covCF2.setSelected(igSpec.isCovCfld2Col());
		covApr.setSelected(igSpec.isAprSelCol());
		covAug.setSelected(igSpec.isAugSelCol());
		covDec.setSelected(igSpec.isDecSelCol());
	}	
	
	private void updateIgnoreRowSpecification()
	{
		try {

			if(addMode != true)
			{
				DynamicCoverageFileIgnoreRowSpecificationRequest addRequest = new DynamicCoverageFileIgnoreRowSpecificationRequest(DataManager.i().mDynamicCoverageFileIgnoreRowSpecification);
				DynamicCoverageFileIgnoreRowSpecification ignoreSpec = DataManager.i().mDynamicCoverageFileIgnoreRowSpecification;
				setRequestColValue(ignoreSpec);
				ignoreSpec.setName(dfcovNameField.getText());
				ignoreSpec.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
				ignoreSpec.setSpecification(DataManager.i().mDynamicCoverageFileSpecification);
				addRequest.setEntity(ignoreSpec);
				addRequest.setId(ignoreSpec.getId());
				
				AdminPersistenceManager.getInstance().addOrUpdate(addRequest);
			} else {
				//create a new DynamicCoverageFileIgnoreRowSpecification to hold the data
				DynamicCoverageFileIgnoreRowSpecificationRequest addRequest = new DynamicCoverageFileIgnoreRowSpecificationRequest(DataManager.i().mDynamicCoverageFileIgnoreRowSpecification);
				DynamicCoverageFileIgnoreRowSpecification ignoreSpec = new DynamicCoverageFileIgnoreRowSpecification();
				setRequestColValue(ignoreSpec);
				ignoreSpec.setName(dfcovNameField.getText());
				ignoreSpec.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
				ignoreSpec.setSpecification(DataManager.i().mDynamicCoverageFileSpecification);
				addRequest.setEntity(ignoreSpec);
				addRequest.setId(ignoreSpec.getId());
				
				AdminPersistenceManager.getInstance().addOrUpdate(addRequest);
			}
		} catch (CoreException e) { DataManager.i().log(Level.SEVERE, e); }	
	    catch (Exception e) { DataManager.i().logGenericException(e); }
	}

	private void saveFields(DynamicCoverageFileIgnoreRowSpecification request)
	{
		DynamicCoverageFileSpecification fSpec = DataManager.i().mDynamicCoverageFileSpecification;
		request.setSpecification(fSpec);
		setRequestColValue(request);
	}

	private void setRequestColValue(DynamicCoverageFileIgnoreRowSpecification igSpec)
	{
		//EMPLOYEE
		igSpec.setErCol(emplId.isSelected());
		igSpec.setfNameCol(empFstNm.isSelected());
		igSpec.setDobCol(empDOB.isSelected());
		igSpec.setJobTtlCol(empJbTit.isSelected());
		igSpec.setStreetNumCol(empStNm.isSelected());
		igSpec.setCityCol(empCity.isSelected());
		igSpec.setEtcRefCol(etcEmpId.isSelected());
		igSpec.setEeFlagCol(empFlag.isSelected());
		igSpec.setmNameCol(empMdNm.isSelected());
		igSpec.setHireDtCol(empHrDt.isSelected());
		igSpec.setPhoneCol(empPhNm.isSelected());
		igSpec.setStreetCol(empAddLn1.isSelected());
		igSpec.setStateCol(empState.isSelected());
		igSpec.setSsnCol(empSSN.isSelected());
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

		//DEPENDENT
		igSpec.setDepEtcRefCol(depEtcId.isSelected());
		igSpec.setDepFNameCol(depFstNm.isSelected());
		igSpec.setDepErRefCol(depEmprId.isSelected());
		igSpec.setDepLin2Col(depAddLn2.isSelected());
		igSpec.setDepZipCol(depZip.isSelected());
		igSpec.setDepSSNCol(depSSN.isSelected());
		igSpec.setDepMNameCol(depMdNm.isSelected());
		igSpec.setDepStreetNumCol(depStrtNm.isSelected());
		igSpec.setDepCityCol(depCity.isSelected());
		igSpec.setDepDOBCol(depDOB.isSelected());
		igSpec.setDepLNameCol(depLstNm.isSelected());
		igSpec.setDepStreetCol(depAddLn1.isSelected());
		igSpec.setDepStateCol(depState.isSelected());
		igSpec.setDepCfld1Col(depCF1.isSelected());
		igSpec.setDepCfld2Col(depCF2.isSelected());
		igSpec.setDepCfld3Col(depCF3.isSelected());
		igSpec.setDepCfld4Col(depCF4.isSelected());
		igSpec.setDepCfld5Col(depCF5.isSelected());
		
		//COVERAGE
		igSpec.setMbrCol(covMbrNm.isSelected());
		igSpec.setWavdCol(covWaiv.isSelected());
		igSpec.setCovStartDtCol(covStrtDt.isSelected());
		igSpec.setTySelCol(covAnnCov.isSelected());
		igSpec.setJanSelCol(covJan.isSelected());
		igSpec.setMaySelCol(covMay.isSelected());
		igSpec.setSepSelCol(covSept.isSelected());
		igSpec.setInelCol(covInelg.isSelected());
		igSpec.setCovEndDtCol(covEndDt.isSelected());
		igSpec.setTyCol(covTxYr.isSelected());
		igSpec.setFebSelCol(covFeb.isSelected());
		igSpec.setJunSelCol(covJune.isSelected());
		igSpec.setOctSelCol(covOct.isSelected());
		igSpec.setPlanRefCol(covPlnId.isSelected());
		igSpec.setSubcrbrCol(covSubNm.isSelected());
		igSpec.setMarSelCol(covMar.isSelected());
		igSpec.setJulSelCol(covJuly.isSelected());
		igSpec.setNovSelCol(covNov.isSelected());
		igSpec.setMbrShareCol(covMbrShrAmt.isSelected());
		igSpec.setCovCfld1Col(covCF1.isSelected());
		igSpec.setCovCfld2Col(covCF2.isSelected());
		igSpec.setAprSelCol(covApr.isSelected());
		igSpec.setAugSelCol(covAug.isSelected());
		igSpec.setDecSelCol(covDec.isSelected());
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
			
			DynamicCoverageFileIgnoreRowSpecification igSpec = DataManager.i().mDynamicCoverageFileIgnoreRowSpecification;
			saveFields(igSpec);
			DynamicCoverageFileIgnoreRowSpecificationRequest request = new DynamicCoverageFileIgnoreRowSpecificationRequest();
			request.setId(igSpec.getId());
			AdminPersistenceManager.getInstance().remove(request);
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
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
	
//	public String getIndexString(int index) 
//	{
//	    String letters;
//		char c = 'A';
//	    char c1 = 'A';
//	    
//    	if(alphaId < 26) 
//    	{
//    		c += alphaId;
//    		letters =  String.valueOf(c) + index;
//    	} else 
//    	{
//    		int letterPos = alphaId/26;
//    		c1 += letterPos;
//    		//c1 +=(letterPos - 1 + alphaId); // starting with A
//    		letters = String.valueOf(c1) + String.valueOf(c + alphaId) + index;
//    	}
//
//    	alphaId++;
//    	return letters;
//	}
	
	public class HBoxDetailCell extends HBox 
	{
        boolean headerCell;
  
         // first column
        TextField nameField = new TextField();
        Label nameLabel = new Label();
               
        // second column
        Label columnIndexLabel = new Label();
         
        // third column
        Label requiredLabel = new Label();
        CheckBox requiredCheck = new CheckBox();
        
        public boolean setValue(String label, boolean value) 
        {
        	if(nameLabel.getText().contains(label))
        	{
        		requiredCheck.setSelected(value);
        		return true;
        	}else
        		return false;
        }
        
        //default constructor is a header row
        HBoxDetailCell()
        {
            super();
            if(DataManager.i().isWindows() == true) 
            {
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
        HBoxDetailCell(String header)
        {
            super();

            nameLabel.setText(header); 
            nameLabel.setMinWidth(695);
            nameLabel.setPrefWidth(695);
            nameLabel.setFont(Font.font(null, FontWeight.BOLD, 13));
            nameLabel.setAlignment(Pos.CENTER);
       	    headerCell = true;
       	    this.getChildren().addAll(nameLabel);
        }
        
        HBoxDetailCell(String name, boolean column, String columnIndex, boolean required) 
        {
             super();
             
             //set up the separators
             headerCell = false;
 
             if(DataManager.i().isWindows() == true)
             {
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
            	 if(name.contains(" (")) 
            	 {
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
             if(column == false) 
             {
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
