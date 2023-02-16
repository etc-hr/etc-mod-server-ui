	package com.etc.admin.ui.pipeline.mapper;

	import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
	import com.etc.corvetto.ems.pipeline.entities.ded.DynamicDeductionFileIgnoreRowSpecification;
	import com.etc.corvetto.ems.pipeline.entities.ded.DynamicDeductionFileSpecification;
	import com.etc.corvetto.ems.pipeline.rqs.ded.DynamicDeductionFileIgnoreRowSpecificationRequest;

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

	public class ViewMapperDeductionFileDeductionFormController
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
		private CheckBox empSSN;
		
		@FXML
		private CheckBox emprEmpId;
		
		@FXML
		private CheckBox empFstNm;
		
		@FXML
		private CheckBox empMdNm;
		
		@FXML
		private CheckBox empLstNm;
		
		@FXML
		private CheckBox empStNm;
		
		@FXML
		private CheckBox empAddLn1;
		
		@FXML
		private CheckBox empAddLn2;
		
		@FXML
		private CheckBox empCity;
		
		@FXML
		private CheckBox empState;
		
		@FXML
		private CheckBox empZip;
		
		@FXML
		private CheckBox empDOB;

		@FXML
		private CheckBox ppdStrtDt;
		
		@FXML
		private CheckBox ppdEndDt;
		
		@FXML
		private CheckBox payDt;
		
		@FXML
		private CheckBox ppdId;
		
		@FXML
		private CheckBox amt;
		
		@FXML
		private CheckBox plan;
		
		@FXML
		private CheckBox dedCF1;
		
		@FXML
		private CheckBox dedCF2;
		
		@FXML
		private CheckBox dedCF3;
		
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
		
		private int listIndex = 0;
		
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
		
		private void initControls() {
			setAddMode(false);
	   
			dfcovSaveChangesButton.setDisable(true);

	 	}	
		
		private void loadIgnoreRowData() 
		{
			dfcovSaveChangesButton.setDisable(false);
			switch(DataManager.i().mPipelineChannel.getType())
			{
				case DEDUCTION:
					if(DataManager.i().mDynamicDeductionFileIgnoreRowSpecification != null)
						loadFields(DataManager.i().mDynamicDeductionFileSpecification, DataManager.i().mDynamicDeductionFileIgnoreRowSpecification);
					else {
						loadAddSpec();
						return;
					}
					break;
				default:
				break;
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
		
		private void clearData() 
		{
			dfcovSelectedSpecView.getItems().clear();
		}
		
		private void loadFields(DynamicDeductionFileSpecification fSpec, DynamicDeductionFileIgnoreRowSpecification igSpec ) 
		{
			//create the spec in case it's null
			if(igSpec == null) igSpec = new DynamicDeductionFileIgnoreRowSpecification();
		    if(igSpec.getName() != null) dfcovNameField.setText(igSpec.getName());

		    // LOAD UP THE FIELD LIST
			loadCheck(igSpec);
		    
			//EMPLOYEE
		    emplId.setSelected(igSpec.isErCol());
			empSSN.setSelected(igSpec.isSsnCol());
			emprEmpId.setSelected(igSpec.isErRefCol());
		    empFstNm.setSelected(igSpec.isfNameCol());
			empMdNm.setSelected(igSpec.ismNameCol());
			empLstNm.setSelected(igSpec.islNameCol());
			empStNm.setSelected(igSpec.isStreetNumCol());
			empAddLn1.setSelected(igSpec.isStreetCol());
			empAddLn2.setSelected(igSpec.isLin2Col());
			empCity.setSelected(igSpec.isCityCol());
			empState.setSelected(igSpec.isStateCol());
			empZip.setSelected(igSpec.isZipCol());
			empDOB.setSelected(igSpec.isDobCol());
			ppdStrtDt.setSelected(igSpec.isPpdStartDtCol());
			ppdEndDt.setSelected(igSpec.isPpdEndDtCol());
			payDt.setSelected(igSpec.isPayDtCol());
			ppdId.setSelected(igSpec.isPpdErRefCol());
			amt.setSelected(igSpec.isAmtCol());
			plan.setSelected(igSpec.isPlanRefCol());
			dedCF1.setSelected(igSpec.isDeductionCfld1Col());
			dedCF2.setSelected(igSpec.isDeductionCfld2Col());
			dedCF3.setSelected(igSpec.isDeductionCfld3Col());
			empCF1.setSelected(igSpec.isCfld1Col());
			empCF2.setSelected(igSpec.isCfld2Col());
			empCF3.setSelected(igSpec.isCfld3Col());
			empCF4.setSelected(igSpec.isCfld4Col());
			empCF5.setSelected(igSpec.isCfld5Col());
		}
		
		public void loadCheck(DynamicDeductionFileIgnoreRowSpecification igSpec)
		{
			//DEDUCTION
			if(emplId.isSelected() == true) { igSpec.setErCol(true); }
			if(empSSN.isSelected() == true) { igSpec.setSsnCol(true); }
			if(emprEmpId.isSelected() == true) { igSpec.setErRefCol(true); }
			if(empFstNm.isSelected() == true) { igSpec.setfNameCol(true); }
			if(empMdNm.isSelected() == true) { igSpec.setmNameCol(true); }
			if(empLstNm.isSelected() == true) { igSpec.setlNameCol(true); }
			if(empStNm.isSelected() == true) { igSpec.setStreetNumCol(true); }
			if(empAddLn1.isSelected() == true) { igSpec.setStreetCol(true); }
			if(empAddLn2.isSelected() == true) { igSpec.setLin2Col(true); }
			if(empCity.isSelected() == true) { igSpec.setCityCol(true); }
			if(empState.isSelected() == true) { igSpec.setStateCol(true); }
			if(empZip.isSelected() == true) { igSpec.setZipCol(true); }
			if(empDOB.isSelected() == true) { igSpec.setDobCol(true); }
			if(ppdStrtDt.isSelected() == true) { igSpec.setPpdStartDtCol(true); }
			if(ppdEndDt.isSelected() == true) { igSpec.setPpdEndDtCol(true); }
			if(payDt.isSelected() == true) { igSpec.setPayDtCol(true); }
			if(ppdId.isSelected() == true) { igSpec.setPpdErRefCol(true); }
			if(amt.isSelected() == true) { igSpec.setAmtCol(true); }
			if(plan.isSelected() == true) { igSpec.setPlanRefCol(true); }
			if(dedCF1.isSelected() == true) { igSpec.setDeductionCfld1Col(true); }
			if(dedCF2.isSelected() == true) { igSpec.setDeductionCfld2Col(true); }
			if(dedCF3.isSelected() == true) { igSpec.setDeductionCfld3Col(true); }
			if(empCF1.isSelected() == true) { igSpec.setCfld1Col(true); }
			if(empCF2.isSelected() == true) { igSpec.setCfld2Col(true); }
			if(empCF3.isSelected() == true) { igSpec.setCfld3Col(true); }
			if(empCF4.isSelected() == true) { igSpec.setCfld4Col(true); }
			if(empCF5.isSelected() == true) { igSpec.setCfld5Col(true); }
		}

		private void updateIgnoreRowSpecification()
		{
			switch(DataManager.i().mPipelineChannel.getType())
			{
				case DEDUCTION:
					try {
						//create a new DynamicDeductionFileIgnoreRowSpecification to hold the data
						DynamicDeductionFileIgnoreRowSpecificationRequest addRequest = new DynamicDeductionFileIgnoreRowSpecificationRequest(DataManager.i().mDynamicDeductionFileIgnoreRowSpecification);
						DynamicDeductionFileIgnoreRowSpecification ignoreSpec = new DynamicDeductionFileIgnoreRowSpecification();
						setRequestColValue(ignoreSpec);
						ignoreSpec.setName(dfcovNameField.getText());
						ignoreSpec.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
						ignoreSpec.setSpecification(DataManager.i().mDynamicDeductionFileSpecification);
						addRequest.setEntity(ignoreSpec);
						addRequest.setId(ignoreSpec.getId());
						AdminPersistenceManager.getInstance().addOrUpdate(addRequest);
					} catch (CoreException e) { DataManager.i().log(Level.SEVERE, e); }
				    catch (Exception e) { DataManager.i().logGenericException(e); }
					break;
				default:
					break;
			}	
		}
		
		private void saveFields(DynamicDeductionFileIgnoreRowSpecification request)
		{
			DynamicDeductionFileSpecification fSpec = DataManager.i().mDynamicDeductionFileSpecification;
			request.setSpecification(fSpec);

			setRequestColValue(request);
		}

		private void setRequestColValue(DynamicDeductionFileIgnoreRowSpecification igSpec)
		{
			//EMPLOYEE
			if(emplId.isSelected() == true) { igSpec.setErCol(true); }
			if(empSSN.isSelected() == true) { igSpec.setSsnCol(true); }
			if(emprEmpId.isSelected() == true) { igSpec.setErRefCol(true); }
			if(empFstNm.isSelected() == true) { igSpec.setfNameCol(true); }
			if(empMdNm.isSelected() == true) { igSpec.setmNameCol(true); }
			if(empLstNm.isSelected() == true) { igSpec.setlNameCol(true); }
			if(empStNm.isSelected() == true) { igSpec.setStreetNumCol(true); }
			if(empAddLn1.isSelected() == true) { igSpec.setStreetCol(true); }
			if(empAddLn2.isSelected() == true) { igSpec.setLin2Col(true); }
			if(empCity.isSelected() == true) { igSpec.setCityCol(true); }
			if(empState.isSelected() == true) { igSpec.setStateCol(true); }
			if(empZip.isSelected() == true) { igSpec.setZipCol(true); }
			if(empDOB.isSelected() == true) { igSpec.setDobCol(true); }
			if(ppdStrtDt.isSelected() == true) { igSpec.setPpdStartDtCol(true); }
			if(ppdEndDt.isSelected() == true) { igSpec.setPpdEndDtCol(true); }
			if(payDt.isSelected() == true) { igSpec.setPayDtCol(true); }
			if(ppdId.isSelected() == true) { igSpec.setPpdErRefCol(true); }
			if(amt.isSelected() == true) { igSpec.setAmtCol(true); }
			if(plan.isSelected() == true) { igSpec.setPlanRefCol(true); }
			if(dedCF1.isSelected() == true) { igSpec.setDeductionCfld1Col(true); }
			if(dedCF2.isSelected() == true) { igSpec.setDeductionCfld2Col(true); }
			if(dedCF3.isSelected() == true) { igSpec.setDeductionCfld3Col(true); }
			if(empCF1.isSelected() == true) { igSpec.setCfld1Col(true); }
			if(empCF2.isSelected() == true) { igSpec.setCfld2Col(true); }
			if(empCF3.isSelected() == true) { igSpec.setCfld3Col(true); }
			if(empCF4.isSelected() == true) { igSpec.setCfld4Col(true); }
			if(empCF5.isSelected() == true) { igSpec.setCfld5Col(true); }
		}
		
		public void setDeductionFile(int nPipelineSpecificationID) 
		{
			DataManager.i().loadDynamicDeductionFileSpecification(nPipelineSpecificationID);
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
			switch(DataManager.i().mPipelineChannel.getType())
			{
				case DEDUCTION:
					loadFields(DataManager.i().mDynamicDeductionFileSpecification, null);
					break;
				default:
				break;
			}		
		}
		
		@FXML
		private void onRemoveSpec(ActionEvent event)
		{
			if(addMode == true) return;
			
			switch(DataManager.i().mPipelineChannel.getType())
			{
				case DEDUCTION:
					try {
						DynamicDeductionFileIgnoreRowSpecification ieSpec = DataManager.i().mDynamicDeductionFileIgnoreRowSpecification;
						saveFields(ieSpec);
						DynamicDeductionFileIgnoreRowSpecificationRequest eRequest = new DynamicDeductionFileIgnoreRowSpecificationRequest();
						eRequest.setId(ieSpec.getId());
						AdminPersistenceManager.getInstance().remove(eRequest);
					} catch (CoreException e) { DataManager.i().log(Level.SEVERE, e); }
				    catch (Exception e) { DataManager.i().logGenericException(e); }
					break;
				default:
					break;
			}	
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
		
		public String getIndexString(int index) 
		{
		    String letters;
			char c = 'A';
		    char c1 = 'A';
		    
	    	if(alphaId < 26) 
	    	{
	    		c += alphaId;
	    		letters =  String.valueOf(c) + index;
	    	} else 
	    	{
	    		int letterPos = alphaId/26;
	    		c1 += letterPos;
	    		//c1 +=(letterPos - 1 + alphaId); // starting with A
	    		letters = String.valueOf(c1) + String.valueOf(c + alphaId) + index;
	    	}

	    	alphaId++;
	    	return letters;
		}
		
		//DEDUCTION
		@FXML
		private void onEmplId(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmpSSN(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmprEmpId(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmpFstNm(ActionEvent event) { loadIgnoreRowData(); }

		@FXML
		private void onEmpMdNm(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmpLstNm(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmpStNm(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmpAddLn1(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmpAddLn2(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmpCity(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmpState(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmpZip(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmpDOB(ActionEvent event) { loadIgnoreRowData(); }

		@FXML
		private void onPpdStrtDt(ActionEvent event) { loadIgnoreRowData(); }

		@FXML
		private void onPpdEndDt(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onPayDt(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onPpdId(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onAmt(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onPlan(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onDedCF1(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onDedCF2(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onDedCF3(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmpCF1(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmpCF2(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmpCF3(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmpCF4(ActionEvent event) { loadIgnoreRowData(); }
		
		@FXML
		private void onEmpCF5(ActionEvent event) { loadIgnoreRowData(); }
		
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
	        DynamicDeductionFileIgnoreRowSpecification dedSpec;

	        HBoxIgnoreRowCell(DynamicDeductionFileIgnoreRowSpecification dedSpec) 
	        {
	             super();
	             
	             if(dedSpec != null) 
	             {
	            	 lblCol1.setText(Utils.getDateString(dedSpec.getLastUpdated()));
	            	 lblCol2.setText(dedSpec.getName());
	            	 this.dedSpec = dedSpec;
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

	        public DynamicDeductionFileIgnoreRowSpecification getDedSpec() {
	        	return dedSpec; 
	        }
		}
	}

