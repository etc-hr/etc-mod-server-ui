	package com.etc.admin.ui.pipeline.mapper;
	
	import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationAdditionalHoursController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicPayFilePayPeriodRuleController;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.ded.DynamicDeductionFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.ded.DynamicDeductionFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.ded.DynamicDeductionFileSpecification;
import com.etc.corvetto.ems.pipeline.rqs.PipelineParseDateFormatRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineParsePatternRequest;
import com.etc.corvetto.ems.pipeline.rqs.ded.DynamicDeductionFileEmployerReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.ded.DynamicDeductionFileIgnoreRowSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.ded.DynamicDeductionFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle; 
	 
	public class ViewMapperDeductionFormController
	{ 
		@FXML 
		private ListView<HBoxIgnoreRowCell> dfcovIgnoreRowSpecsListView; 
	 
		@FXML 
		private Label emprIdLbl; 
	 
		@FXML 
		private StackPane emprIdSkPn; 
		 
		@FXML 
		private StackPane emplSsnSkPn; 
		 
		@FXML 
		private StackPane emplrEmpIdSkPn; 
		 
		@FXML 
		private StackPane empFstNmSkPn; 
		 
		@FXML 
		private StackPane emplMNmSkPn; 
		 
		@FXML 
		private StackPane emplLstNmSkPn; 
		 
		@FXML 
		private StackPane emplStNmSkPn;
		 
		@FXML 
		private StackPane emplAdLn1SkPn; 
		 
		@FXML 
		private StackPane emplAdLn2SkPn; 
		 
		@FXML 
		private StackPane emplCitySkPn; 
	 
		@FXML 
		private StackPane emplStateSkPn; 
		 
		@FXML 
		private StackPane emplZipSkPn; 
		
		@FXML 
		private StackPane empDOBSkPn; 
		 
		@FXML 
		private StackPane payStrtDtSkPn; 
		 
		@FXML 
		private StackPane payEndDtSkPn; 
	
		@FXML 
		private StackPane payDtSkPn; 
		
		@FXML 
		private StackPane payPrdIdSkPn; 
		 
		@FXML 
		private StackPane amtSkPn;
		 
		@FXML 
		private StackPane covPlanSkPn; 
		 
		@FXML 
		private StackPane emplCF1SkPn; 
		 
		@FXML 
		private StackPane emplCF2SkPn; 
		 
		@FXML 
		private StackPane emplCF3SkPn; 
		 
		@FXML 
		private StackPane emplCF4SkPn; 
		 
		@FXML 
		private StackPane emplCF5SkPn; 
		 
		@FXML 
		private StackPane dedCF1SkPn; 
		 
		@FXML 
		private StackPane dedCF2SkPn; 
		 
		@FXML 
		private StackPane dedCF3SkPn; 

		@FXML 
		private TextField dfcovNameLabel; 
		 
		@FXML 
		private TextField dfcovDescriptionLabel; 
		 
		@FXML 
		private TextField dfcovTabIndexLabel; 

		@FXML 
		private TextField dfcovHeaderRowIndexLabel; 
		 
		@FXML 
		private CheckBox dfpayPeriodCheck; 
		 
		@FXML 
		private CheckBox dfcovCreateEmployeeCheck; 
		 
		@FXML 
		private CheckBox dfcovLockedCheck; 
		 
		@FXML 
		private CheckBox dfcovSkipLastRowCheck; 
		 
		@FXML 
		private CheckBox dfplanCheck; 
		 
		@FXML 
		private CheckBox emprId; 
		 
		@FXML 
		private CheckBox emplSsn; 
		 
		@FXML 
		private CheckBox emplrEmpId; 
		 
		@FXML 
		private CheckBox empFstNm; 
		 
		@FXML 
		private CheckBox emplMNm; 
		 
		@FXML 
		private CheckBox emplLstNm; 
		 
		@FXML 
		private CheckBox emplStNm;
		 
		@FXML 
		private CheckBox emplAdLn1; 
		 
		@FXML 
		private CheckBox emplAdLn2; 
		 
		@FXML 
		private CheckBox emplCity; 
	 
		@FXML 
		private CheckBox emplState; 
		 
		@FXML 
		private CheckBox emplZip; 
		
		@FXML 
		private CheckBox empDOB; 
		 
		@FXML 
		private CheckBox payStrtDt; 
		 
		@FXML 
		private CheckBox payEndDt; 
	
		@FXML 
		private CheckBox payDt; 
		
		@FXML 
		private CheckBox payPrdId; 
		 
		@FXML 
		private CheckBox amt;
		 
		@FXML 
		private CheckBox covPlan; 
		 
		@FXML 
		private CheckBox emplCF1; 
		 
		@FXML 
		private CheckBox emplCF2; 
		 
		@FXML 
		private CheckBox emplCF3; 
		 
		@FXML 
		private CheckBox emplCF4; 
		 
		@FXML 
		private CheckBox emplCF5; 
		 
		@FXML 
		private CheckBox dedCF1; 
		 
		@FXML 
		private CheckBox dedCF2; 
		 
		@FXML 
		private CheckBox dedCF3; 
		 
		@FXML 
		private TextField emprIdIdx; 
		 
		@FXML 
		private TextField emplSsnIdx; 
		 
		@FXML 
		private TextField emplrEmpIdIdx; 
		 
		@FXML 
		private TextField empFstNmIdx; 
		 
		@FXML 
		private TextField emplMNmIdx; 
		 
		@FXML 
		private TextField emplLstNmIdx; 
		 
		@FXML 
		private TextField emplStNmIdx;
		 
		@FXML 
		private TextField emplAdLn1Idx; 
		 
		@FXML 
		private TextField emplAdLn2Idx; 
		 
		@FXML 
		private TextField emplCityIdx; 
	 
		@FXML 
		private TextField emplStateIdx; 
		 
		@FXML 
		private TextField emplZipIdx; 
		
		@FXML 
		private TextField empDOBIdx; 
		 
		@FXML 
		private TextField payStrtDtIdx; 
		 
		@FXML 
		private TextField payEndDtIdx; 
	
		@FXML 
		private TextField payDtIdx; 
		
		@FXML 
		private TextField payPrdIdIdx; 
		 
		@FXML 
		private TextField amtIdx;
		 
		@FXML 
		private TextField covPlanIdx; 
		 
		@FXML 
		private TextField emplCF1Idx; 
		 
		@FXML 
		private TextField emplCF2Idx; 
		 
		@FXML 
		private TextField emplCF3Idx; 
		 
		@FXML 
		private TextField emplCF4Idx; 
		 
		@FXML 
		private TextField emplCF5Idx; 
		 
		@FXML 
		private TextField dedCF1Idx; 
		 
		@FXML 
		private TextField dedCF2Idx; 
		 
		@FXML 
		private TextField dedCF3Idx; 
		
		@FXML 
		private TextField deductCF1; 
	 
		@FXML 
		private TextField deductCF2; 
		 
		@FXML 
		private TextField deductCF3; 
		 
		@FXML 
		private TextField eeCF1; 
		 
		@FXML 
		private TextField eeCF2; 
	 
		@FXML 
		private TextField eeCF3; 
		 
		@FXML 
		private TextField eeCF4; 
	 
		@FXML 
		private TextField eeCF5; 
		 
		@FXML 
		private Button dfcovEditButton; 
		 
		@FXML 
		private Button dfcovClearFormButton; 
		 
		@FXML 
		private Button dfcovSaveChangesButton; 
		 
		@FXML 
		private Button dfcovSavingChangesButton; 
		 
		@FXML 
		private Button dfcovAddIgnoreSpecButton; 
		 
		@FXML 
		private Button dfcovPayPeriodRulesButton; 
		 
		@FXML 
		private Button dfcovPayAdditionalHoursButton; 
		 
		@FXML 
		private Button emprRefButton; 
		 
		@FXML 
		private Button emplSsnParse; 
		 
		@FXML 
		private Button emplrEmpIdParse; 
		 
		@FXML 
		private Button empFstNmParse; 
		 
		@FXML 
		private Button emplMNmParse; 
		 
		@FXML 
		private Button emplLstNmParse; 
		 
		@FXML 
		private Button emplAdLn1Parse; 
		 
		@FXML 
		private Button emplCityParse; 
		 
		@FXML 
		private Button emplStateParse; 
		 
		@FXML 
		private Button emplZipParse; 
		 
		@FXML 
		private Button empDOBFormat; 
		 
		@FXML 
		private Button payStrtDtParse; 
		 
		@FXML 
		private Button payStrtDtFormat; 
		
		@FXML 
		private Button payEndDtParse; 
		
		@FXML 
		private Button payEndDtFormat; 
		
		@FXML 
		private Button payDtParse; 
		
		@FXML 
		private Button payDtFormat; 
		
		@FXML 
		private Button covPlanParse; 
		 
		@FXML 
		private Button emplCF1Parse; 
		 
		@FXML 
		private Button emplCF2Parse; 
		 
		@FXML 
		private Button dedCF1Parse; 
		 
		@FXML 
		private Button dedCF2Parse; 
		 
		@FXML 
		private Label dfcovName; 
		 
		@FXML 
		private Label dfcovCoreIdLabel; 
		 
		@FXML 
		private Label dfcovCoreActiveLabel; 
		 
		@FXML 
		private Label dfcovCoreBODateLabel; 
		 
		@FXML 
		private Label dfcovCoreLastUpdatedLabel; 
		 
		@FXML 
		private GridPane spreadsheet; 
	 
		@FXML 
		private VBox dfcovVBox; 
	 
		@FXML 
		private TextArea cell_a; 
	 
		@FXML 
		private TextArea cell_b; 
	 
		@FXML 
		private TextArea cell_c; 
	 
		@FXML 
		private TextArea cell_d; 
	 
		@FXML 
		private TextArea cell_e; 
	 
		@FXML 
		private TextArea cell_f; 
	 
		@FXML 
		private TextArea cell_g; 
	 
		@FXML 
		private TextArea cell_h; 
	 
		@FXML 
		private TextArea cell_i; 
	 
		@FXML 
		private TextArea cell_j; 
	 
		@FXML 
		private TextArea cell_k; 
	 
		@FXML 
		private TextArea cell_l; 
	 
		@FXML 
		private TextArea cell_m; 
	 
		@FXML 
		private TextArea cell_n; 
	 
		@FXML 
		private TextArea cell_o; 
	 
		@FXML 
		private TextArea cell_p; 
	 
		@FXML 
		private TextArea cell_q; 
	 
		@FXML 
		private TextArea cell_r; 
	 
		@FXML 
		private TextArea cell_s; 
	 
		@FXML 
		private TextArea cell_t; 
	 
		@FXML 
		private TextArea cell_u; 
	 
		@FXML 
		private TextArea cell_v; 
	 
		@FXML 
		private TextArea cell_w; 
	 
		@FXML 
		private TextArea cell_x; 
	 
		@FXML 
		private TextArea cell_y; 
	 
		@FXML 
		private TextArea cell_z; 
	 
		@FXML 
		private TextArea cell_aa; 
	 
		@FXML 
		private TextArea cell_ab; 
	 
		@FXML 
		private TextArea cell_ac; 
	 
		@FXML 
		private TextArea cell_ad; 
	 
		@FXML 
		private TextArea cell_ae; 
	 
		@FXML 
		private TextArea cell_af; 
	 
		@FXML 
		private TextArea cell_ag; 
	 
		@FXML 
		private TextArea cell_ah; 
	 
		@FXML 
		private TextArea cell_ai; 
	 
		@FXML 
		private TextArea cell_aj; 
	 
		@FXML 
		private TextArea cell_ak; 
	 
		@FXML 
		private TextArea cell_al; 
	 
		@FXML 
		private TextArea cell_am; 
	 
		@FXML 
		private TextArea cell_an; 
	 
		@FXML 
		private TextArea cell_ao; 
	 
		@FXML 
		private TextArea cell_ap; 
	 
		@FXML 
		private TextArea cell_aq; 
	 
		@FXML 
		private TextArea cell_ar; 
	 
		@FXML 
		private TextArea cell_as; 
	 
		@FXML 
		private TextArea cell_at; 
	 
		@FXML 
		private TextArea cell_au; 
	 
		@FXML 
		private TextArea cell_av; 
	 
		@FXML 
		private TextArea cell_aw; 
	 
		@FXML 
		private TextArea cell_ax; 
	 
		@FXML 
		private TextArea cell_ay; 
	 
		@FXML 
		private TextArea cell_az; 
	 
		@FXML 
		private TextArea cell_ba; 
	 
		@FXML 
		private TextArea cell_bb; 
	 
		@FXML 
		private TextArea cell_bc; 
	 
		@FXML 
		private TextArea cell_bd; 
	 
		@FXML 
		private TextArea cell_be; 
	 
		@FXML 
		private TextArea cell_bf; 
	 
		@FXML 
		private TextArea cell_bg; 
	 
		@FXML 
		private TextArea cell_bh; 
	 
		@FXML 
		private TextArea cell_bi; 
	 
		@FXML 
		private TextArea cell_bj; 
	 
		@FXML 
		private TextArea cell_bk; 
	 
		@FXML 
		private TextArea cell_bl; 
	 
		@FXML 
		private TextArea cell_bm; 
	 
		@FXML 
		private TextArea cell_bn; 
	 
		@FXML 
		private TextArea cell_bo; 
	 
		@FXML 
		private TextArea cell_bp; 
	 
		@FXML 
		private TextArea cell_bq; 
	 
		@FXML 
		private TextArea cell_br; 
	 
		@FXML 
		private TextArea cell_bs; 
	 
		@FXML 
		private TextArea cell_bt; 
	 
		@FXML 
		private TextArea cell_bu; 
	 
		@FXML 
		private TextArea cell_bv; 
	 
		@FXML 
		private TextArea cell_bw; 
	 
		@FXML 
		private TextArea cell_bx; 
	 
		@FXML 
		private TextArea cell_by; 
	 
		@FXML 
		private TextArea cell_bz; 
	 
		@FXML 
		private TextArea cell_ca; 
	 
		@FXML 
		private TextArea cell_cb; 
	 
		@FXML 
		private TextArea cell_cc; 
	 
		@FXML 
		private TextArea cell_cd; 
	 
		@FXML 
		private TextArea cell_ce; 
	 
		@FXML 
		private TextArea cell_cf; 
	 
		@FXML 
		private TextArea cell_cg; 
	 
		@FXML 
		private TextArea cell_ch; 
	 
		@FXML 
		private TextArea cell_ci; 
	 
		@FXML 
		private TextArea cell_cj; 
	 
		@FXML 
		private TextArea cell_ck; 
	 
		@FXML 
		private TextArea cell_cl; 
	 
		@FXML 
		private TextArea cell_cm; 
	 
		@FXML 
		private TextArea cell_cn; 
	 
		@FXML 
		private TextArea cell_co; 
	 
		@FXML 
		private TextArea cell_cp; 
	 
		@FXML 
		private TextArea cell_cq; 
	 
		@FXML 
		private TextArea cell_cr; 
	 
		@FXML 
		private TextArea cell_cs; 
	 
		@FXML 
		private TextArea cell_ct; 
	 
		@FXML 
		private TextArea cell_cu; 
	 
		@FXML 
		private TextArea cell_cv; 
	 
		@FXML 
		private TextArea cell_cw; 
	 
		@FXML 
		private TextArea cell_cx; 
	 
		@FXML 
		private TextArea cell_cy; 
	 
		@FXML 
		private TextArea cell_cz; 
	 
		@FXML 
		private Label empRefLabel; 
	 
		//FIXME 
		// read only mode for selected mapper files 
		public boolean readOnly = false; 
		public PipelineFileType fileType; 
	 
		//FIXME: PETE'S CODE 
		private HashMap<Integer,MapperCell> mapperCells = new HashMap<Integer,MapperCell>(72, 1.0f); 
		private HashMap<StackPane,MapperField> mapperFields = new HashMap<StackPane,MapperField>(72, 1.0f); 
	 
		/** 
		 * initialize is called when the FXML is loaded 
		 */ 
		@FXML 
		public void initialize() 
		{ 
			// reset the buttons 
			dfcovSavingChangesButton.setVisible(false); 
			dfcovSaveChangesButton.setVisible(true); 
	 
			// set the name at the top of the screen 
			if(DataManager.i().mPipelineSpecification != null) 
				dfcovName.setText("Mapper for ".concat(DataManager.i().mPipelineSpecification.getName()).concat(",  Id: ").concat(String.valueOf(DataManager.i().mPipelineSpecification.getId()))); 
	 
			//set the file type 
			fileType = DataManager.i().mPipelineChannel.getType(); 
			 
			initControls(); 
			loadMapperData(); 
		} 
	 
		private void initControls()  
		{ 
			//disable the check boxes 
			dfcovCreateEmployeeCheck.setDisable(false);		 
			dfcovLockedCheck.setDisable(false); 
	 
			//IGNOREROWSPECS 
			dfcovIgnoreRowSpecsListView.setOnMouseClicked(mouseClickedEvent ->  
			{ 
	            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
	            { 
	        		switch(fileType) 
	        		{ 
		        		case DEDUCTION: 
		                	DataManager.i().mDynamicDeductionFileIgnoreRowSpecification = dfcovIgnoreRowSpecsListView.getSelectionModel().getSelectedItem().getDedSpec(); 
		        			break; 
		        		default: 
		        			return; 
	        		} 
	        		viewIgnoreRow(); 
	            } 
	        }); 
	 
			// EMPLOYER ID REFERENCE 
			emprRefButton.setOnMouseClicked(mouseClickedEvent ->  
			{ 
	            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
	            { 
	            	loadEmployerRefForm(); 
	            } 
	        }); 
	 
			// SET IDX DISABLED 
			covPlanIdx.setDisable(true); 
			empFstNmIdx.setDisable(true); 
			emplStNmIdx.setDisable(true); 
			emplCityIdx.setDisable(true); 
			empDOBIdx.setDisable(true); 
			emplSsnIdx.setDisable(true); 
			emplrEmpIdIdx.setDisable(true); 
			emplMNmIdx.setDisable(true); 
			emplAdLn1Idx.setDisable(true); 
			emplStateIdx.setDisable(true); 
			amtIdx.setDisable(true); 
			payPrdIdIdx.setDisable(true); 
			emprIdIdx.setDisable(true); 
			emplLstNmIdx.setDisable(true); 
			emplAdLn2Idx.setDisable(true); 
			emplZipIdx.setDisable(true); 
			payStrtDtIdx.setDisable(true); 
			payEndDtIdx.setDisable(true); 
			payDtIdx.setDisable(true); 
			dedCF1Idx.setDisable(true); 
			dedCF2Idx.setDisable(true); 
			dedCF3Idx.setDisable(true); 
			emplCF1Idx.setDisable(true); 
			emplCF2Idx.setDisable(true); 
			emplCF3Idx.setDisable(true); 
			emplCF4Idx.setDisable(true); 
			emplCF5Idx.setDisable(true); 
	 
			// SET BUTTON TEXT 
			covPlanParse.setText("+ Pattern"); 
			covPlanParse.setFont(Font.font("Veranda", 12.0)); 
			covPlanParse.setDisable(true);		 
			empFstNmParse.setText("+ Pattern"); 
			empFstNmParse.setFont(Font.font("Veranda", 12.0)); 
			empFstNmParse.setDisable(true);		 
			emplCityParse.setText("+ Pattern"); 
			emplCityParse.setFont(Font.font("Veranda", 12.0)); 
			emplCityParse.setDisable(true);		 
			empDOBFormat.setText("+ Format"); 
			empDOBFormat.setFont(Font.font("Veranda", 12.0)); 
			empDOBFormat.setDisable(true);		 
			emplSsnParse.setText("+ Pattern"); 
			emplSsnParse.setFont(Font.font("Veranda", 12.0)); 
			emplSsnParse.setDisable(true);		 
			emplrEmpIdParse.setText("+ Pattern"); 
			emplrEmpIdParse.setFont(Font.font("Veranda", 12.0)); 
			emplrEmpIdParse.setDisable(true);		 
			emplMNmParse.setText("+ Pattern"); 
			emplMNmParse.setFont(Font.font("Veranda", 12.0)); 
			emplMNmParse.setDisable(true);		 
			emplAdLn1Parse.setText("+ Pattern"); 
			emplAdLn1Parse.setFont(Font.font("Veranda", 12.0)); 
			emplAdLn1Parse.setDisable(true);		 
			emplStateParse.setText("+ Pattern"); 
			emplStateParse.setFont(Font.font("Veranda", 12.0)); 
			emplStateParse.setDisable(true);		 
			emplLstNmParse.setText("+ Pattern"); 
			emplLstNmParse.setFont(Font.font("Veranda", 12.0)); 
			emplLstNmParse.setDisable(true);		 
			emplZipParse.setText("+ Pattern"); 
			emplZipParse.setFont(Font.font("Veranda", 12.0)); 
			emplZipParse.setDisable(true);		 
			payStrtDtParse.setText("+ Pattern"); 
			payStrtDtParse.setFont(Font.font("Veranda", 12.0)); 
			payStrtDtParse.setDisable(true);		 
			payStrtDtFormat.setText("+ Format"); 
			payStrtDtFormat.setFont(Font.font("Veranda", 12.0)); 
			payStrtDtFormat.setDisable(true);		 
			payEndDtParse.setText("+ Pattern"); 
			payEndDtParse.setFont(Font.font("Veranda", 12.0)); 
			payEndDtParse.setDisable(true);		 
			payEndDtFormat.setText("+ Format"); 
			payEndDtFormat.setFont(Font.font("Veranda", 12.0)); 
			payEndDtFormat.setDisable(true);		 
			payDtParse.setText("+ Pattern"); 
			payDtParse.setFont(Font.font("Veranda", 12.0)); 
			payDtParse.setDisable(true);		 
			payDtFormat.setText("+ Format"); 
			payDtFormat.setFont(Font.font("Veranda", 12.0)); 
			payDtFormat.setDisable(true);		 
			dedCF1Parse.setText("+ Pattern"); 
			dedCF1Parse.setFont(Font.font("Veranda", 12.0)); 
			dedCF1Parse.setDisable(true);		 
			dedCF2Parse.setText("+ Pattern"); 
			dedCF2Parse.setFont(Font.font("Veranda", 12.0)); 
			dedCF2Parse.setDisable(true);		 
			emplCF1Parse.setText("+ Pattern"); 
			emplCF1Parse.setFont(Font.font("Veranda", 12.0)); 
			emplCF1Parse.setDisable(true);		 
			emplCF2Parse.setText("+ Pattern"); 
			emplCF2Parse.setFont(Font.font("Veranda", 12.0)); 
			emplCF2Parse.setDisable(true);		 
	 
			//REMOVE EDITABLILITY FROM TEXTAREA HEADER LAYOUT 
			cell_a.setEditable(false); 
			cell_b.setEditable(false); 
			cell_c.setEditable(false); 
			cell_d.setEditable(false); 
			cell_e.setEditable(false); 
			cell_f.setEditable(false); 
			cell_g.setEditable(false); 
			cell_h.setEditable(false); 
			cell_i.setEditable(false); 
			cell_j.setEditable(false); 
			cell_k.setEditable(false); 
			cell_l.setEditable(false); 
			cell_m.setEditable(false); 
			cell_n.setEditable(false); 
			cell_o.setEditable(false); 
			cell_p.setEditable(false); 
			cell_q.setEditable(false); 
			cell_r.setEditable(false); 
			cell_s.setEditable(false); 
			cell_t.setEditable(false); 
			cell_u.setEditable(false); 
			cell_v.setEditable(false); 
			cell_w.setEditable(false); 
			cell_x.setEditable(false); 
			cell_y.setEditable(false); 
			cell_z.setEditable(false); 
			cell_aa.setEditable(false); 
			cell_ab.setEditable(false); 
			cell_ac.setEditable(false); 
			cell_ad.setEditable(false); 
			cell_ae.setEditable(false); 
			cell_af.setEditable(false); 
			cell_ag.setEditable(false); 
			cell_ah.setEditable(false); 
			cell_ai.setEditable(false); 
			cell_aj.setEditable(false); 
			cell_ak.setEditable(false); 
			cell_al.setEditable(false); 
			cell_am.setEditable(false); 
			cell_an.setEditable(false); 
			cell_ao.setEditable(false); 
			cell_ap.setEditable(false); 
			cell_aq.setEditable(false); 
			cell_ar.setEditable(false); 
			cell_as.setEditable(false); 
			cell_at.setEditable(false); 
			cell_au.setEditable(false); 
			cell_av.setEditable(false); 
			cell_aw.setEditable(false); 
			cell_ax.setEditable(false); 
			cell_ay.setEditable(false); 
			cell_az.setEditable(false); 
			cell_ba.setEditable(false); 
			cell_bb.setEditable(false); 
			cell_bc.setEditable(false); 
			cell_bd.setEditable(false); 
			cell_be.setEditable(false); 
			cell_bf.setEditable(false); 
			cell_bg.setEditable(false); 
			cell_bh.setEditable(false); 
			cell_bi.setEditable(false); 
			cell_bj.setEditable(false); 
			cell_bk.setEditable(false); 
			cell_bl.setEditable(false); 
			cell_bm.setEditable(false); 
			cell_bn.setEditable(false); 
			cell_bo.setEditable(false); 
			cell_bp.setEditable(false); 
			cell_bq.setEditable(false); 
			cell_br.setEditable(false); 
			cell_bs.setEditable(false); 
			cell_bt.setEditable(false); 
			cell_bu.setEditable(false); 
			cell_bv.setEditable(false); 
			cell_bw.setEditable(false); 
			cell_bx.setEditable(false); 
			cell_by.setEditable(false); 
			cell_bz.setEditable(false); 
			cell_ca.setEditable(false); 
			cell_cb.setEditable(false); 
			cell_cc.setEditable(false); 
			cell_cd.setEditable(false); 
			cell_ce.setEditable(false); 
			cell_cf.setEditable(false); 
			cell_cg.setEditable(false); 
			cell_ch.setEditable(false); 
			cell_ci.setEditable(false); 
			cell_cj.setEditable(false); 
			cell_ck.setEditable(false); 
			cell_cl.setEditable(false); 
			cell_cm.setEditable(false); 
			cell_cn.setEditable(false); 
			cell_co.setEditable(false); 
			cell_cp.setEditable(false); 
			cell_cq.setEditable(false); 
			cell_cr.setEditable(false); 
			cell_cs.setEditable(false); 
			cell_ct.setEditable(false); 
			cell_cu.setEditable(false); 
			cell_cv.setEditable(false); 
			cell_cw.setEditable(false); 
			cell_cx.setEditable(false); 
			cell_cy.setEditable(false); 
			cell_cz.setEditable(false); 
			 
	        //INSURANCE SECTION 
	        mapperFields.put(covPlanSkPn, new MapperField(covPlanSkPn, covPlan, covPlanIdx, covPlanParse, null, null, "Plan ID", 54)); 
	        mapperFields.put(empFstNmSkPn, new MapperField(empFstNmSkPn, empFstNm, empFstNmIdx, empFstNmParse, null, null, "EE First Name", 90)); 
	        mapperFields.put(emplStNmSkPn, new MapperField(emplStNmSkPn, emplStNm, emplStNmIdx, null, null, null, "EE Strt NMBR", 88)); 
	        mapperFields.put(emplCitySkPn, new MapperField(emplCitySkPn, emplCity, emplCityIdx, emplCityParse, null, null, "EE City", 52)); 
	        mapperFields.put(empDOBSkPn, new MapperField(empDOBSkPn, empDOB, empDOBIdx, null, empDOBFormat, null, "EE DOB" , 57));
	        mapperFields.put(emplSsnSkPn, new MapperField(emplSsnSkPn, emplSsn, emplSsnIdx, emplSsnParse, null, null, "EE SSN", 54)); 
	        mapperFields.put(emplrEmpIdSkPn, new MapperField(emplrEmpIdSkPn, emplrEmpId, emplrEmpIdIdx, emplrEmpIdParse, null, null, "EMPR EE ID", 78)); 
	        mapperFields.put(emplMNmSkPn, new MapperField(emplMNmSkPn, emplMNm, emplMNmIdx, emplMNmParse, null, null, "EE Mid Name", 90)); 
	        mapperFields.put(emplAdLn1SkPn, new MapperField(emplAdLn1SkPn, emplAdLn1, emplAdLn1Idx, emplAdLn1Parse, null, null, "EE ADD LN 1", 85)); 
	        mapperFields.put(emplStateSkPn, new MapperField(emplStateSkPn, emplState, emplStateIdx, emplStateParse, null, null, "EE State", 58)); 
	        mapperFields.put(amtSkPn, new MapperField(amtSkPn, amt, amtIdx, null, null, null, "Amount", 60)); 
	        mapperFields.put(payPrdIdSkPn, new MapperField(payPrdIdSkPn, payPrdId, payPrdIdIdx, null, null, null, "Pay Period ID", 87)); 
	        mapperFields.put(emprIdSkPn, new MapperField(emprIdSkPn, emprId, emprIdIdx, null, null, emprRefButton, "Employer Id", 80)); 
	        mapperFields.put(emplLstNmSkPn, new MapperField(emplLstNmSkPn, emplLstNm, emplLstNmIdx, emplLstNmParse, null, null, "EE Last Name", 89)); 
	        mapperFields.put(emplAdLn2SkPn, new MapperField(emplAdLn2SkPn, emplAdLn2, emplAdLn2Idx, null, null, null, "EE ADD LN 2", 85)); 
	        mapperFields.put(emplZipSkPn, new MapperField(emplZipSkPn, emplZip, emplZipIdx, emplZipParse, null, null, "EE Zip", 48)); 
	        mapperFields.put(payStrtDtSkPn, new MapperField(payStrtDtSkPn, payStrtDt, payStrtDtIdx, payStrtDtParse, payStrtDtFormat, null, "PAY Start Date", 100)); 
	        mapperFields.put(payEndDtSkPn, new MapperField(payEndDtSkPn, payEndDt, payEndDtIdx, payEndDtParse, payEndDtFormat, null, "PAY End Date", 94)); 
	        mapperFields.put(payDtSkPn, new MapperField(payDtSkPn, payDt, payDtIdx, payDtParse, payDtFormat, null, "Pay Date", 65)); 
	        mapperFields.put(dedCF1SkPn, new MapperField(dedCF1SkPn, dedCF1, dedCF1Idx, dedCF1Parse, null, null, "DED CF 1", 62)); 
	        mapperFields.put(dedCF2SkPn, new MapperField(dedCF2SkPn, dedCF2, dedCF2Idx, dedCF2Parse, null, null, "DED CF 2", 62)); 
	        mapperFields.put(dedCF3SkPn, new MapperField(dedCF3SkPn, dedCF3, dedCF3Idx, null, null, null, "DED CF 3", 62)); 
	        mapperFields.put(emplCF1SkPn, new MapperField(emplCF1SkPn, emplCF1, emplCF1Idx, emplCF1Parse, null, null, "EE CF 1", 55)); 
	        mapperFields.put(emplCF2SkPn, new MapperField(emplCF2SkPn, emplCF2, emplCF2Idx, emplCF2Parse, null, null, "EE CF 2", 55)); 
	        mapperFields.put(emplCF3SkPn, new MapperField(emplCF3SkPn, emplCF3, emplCF3Idx, null, null, null, "EE CF 3", 55)); 
	        mapperFields.put(emplCF4SkPn, new MapperField(emplCF4SkPn, emplCF4, emplCF4Idx, null, null, null, "EE CF 4", 55)); 
	        mapperFields.put(emplCF5SkPn, new MapperField(emplCF5SkPn, emplCF5, emplCF5Idx, null, null, null, "EE CF 5", 55)); 
	 
	        //CREATE MAPPERCELLS 
	        mapperCells.put(0, new MapperCell(0, cell_a)); 
	        mapperCells.put(1, new MapperCell(1, cell_b)); 
	        mapperCells.put(2, new MapperCell(2, cell_c)); 
	        mapperCells.put(3, new MapperCell(3, cell_d)); 
	        mapperCells.put(4, new MapperCell(4, cell_e)); 
	        mapperCells.put(5, new MapperCell(5, cell_f)); 
	        mapperCells.put(6, new MapperCell(6, cell_g)); 
	        mapperCells.put(7, new MapperCell(7, cell_h)); 
	        mapperCells.put(8, new MapperCell(8, cell_i)); 
	        mapperCells.put(9, new MapperCell(9, cell_j)); 
	        mapperCells.put(10, new MapperCell(10, cell_k)); 
	        mapperCells.put(11, new MapperCell(11, cell_l)); 
	        mapperCells.put(12, new MapperCell(12, cell_m)); 
	        mapperCells.put(13, new MapperCell(13, cell_n)); 
	        mapperCells.put(14, new MapperCell(14, cell_o)); 
	        mapperCells.put(15, new MapperCell(15, cell_p)); 
	        mapperCells.put(16, new MapperCell(16, cell_q)); 
	        mapperCells.put(17, new MapperCell(17, cell_r)); 
	        mapperCells.put(18, new MapperCell(18, cell_s)); 
	        mapperCells.put(19, new MapperCell(19, cell_t)); 
	        mapperCells.put(20, new MapperCell(20, cell_u)); 
	        mapperCells.put(21, new MapperCell(21, cell_v)); 
	        mapperCells.put(22, new MapperCell(22, cell_w)); 
	        mapperCells.put(23, new MapperCell(23, cell_x)); 
	        mapperCells.put(24, new MapperCell(24, cell_y)); 
	        mapperCells.put(25, new MapperCell(25, cell_z)); 
	        mapperCells.put(26, new MapperCell(26, cell_aa)); 
	        mapperCells.put(27, new MapperCell(27, cell_ab)); 
	        mapperCells.put(28, new MapperCell(28, cell_ac)); 
	        mapperCells.put(29, new MapperCell(29, cell_ad)); 
	        mapperCells.put(30, new MapperCell(30, cell_ae)); 
	        mapperCells.put(31, new MapperCell(31, cell_af)); 
	        mapperCells.put(32, new MapperCell(32, cell_ag)); 
	        mapperCells.put(33, new MapperCell(33, cell_ah)); 
	        mapperCells.put(34, new MapperCell(34, cell_ai)); 
	        mapperCells.put(35, new MapperCell(35, cell_aj)); 
	        mapperCells.put(36, new MapperCell(36, cell_ak)); 
	        mapperCells.put(37, new MapperCell(37, cell_al)); 
	        mapperCells.put(38, new MapperCell(38, cell_am)); 
	        mapperCells.put(39, new MapperCell(39, cell_an)); 
	        mapperCells.put(40, new MapperCell(40, cell_ao)); 
	        mapperCells.put(41, new MapperCell(41, cell_ap)); 
	        mapperCells.put(42, new MapperCell(42, cell_aq)); 
	        mapperCells.put(43, new MapperCell(43, cell_ar)); 
	        mapperCells.put(44, new MapperCell(44, cell_as)); 
	        mapperCells.put(45, new MapperCell(45, cell_at)); 
	        mapperCells.put(46, new MapperCell(46, cell_au)); 
	        mapperCells.put(47, new MapperCell(47, cell_av)); 
	        mapperCells.put(48, new MapperCell(48, cell_aw)); 
	        mapperCells.put(49, new MapperCell(49, cell_ax)); 
	        mapperCells.put(50, new MapperCell(50, cell_ay)); 
	        mapperCells.put(51, new MapperCell(51, cell_az)); 
	        mapperCells.put(52, new MapperCell(52, cell_ba)); 
	        mapperCells.put(53, new MapperCell(53, cell_bb)); 
	        mapperCells.put(54, new MapperCell(54, cell_bc)); 
	        mapperCells.put(55, new MapperCell(55, cell_bd)); 
	        mapperCells.put(56, new MapperCell(56, cell_be)); 
	        mapperCells.put(57, new MapperCell(57, cell_bf)); 
	        mapperCells.put(58, new MapperCell(58, cell_bg)); 
	        mapperCells.put(59, new MapperCell(59, cell_bh)); 
	        mapperCells.put(60, new MapperCell(60, cell_bi)); 
	        mapperCells.put(61, new MapperCell(61, cell_bj)); 
	        mapperCells.put(62, new MapperCell(62, cell_bk)); 
	        mapperCells.put(63, new MapperCell(63, cell_bl)); 
	        mapperCells.put(64, new MapperCell(64, cell_bm)); 
	        mapperCells.put(65, new MapperCell(65, cell_bn)); 
	        mapperCells.put(66, new MapperCell(66, cell_bo)); 
	        mapperCells.put(67, new MapperCell(67, cell_bp)); 
	        mapperCells.put(68, new MapperCell(68, cell_bq)); 
	        mapperCells.put(69, new MapperCell(69, cell_br)); 
	        mapperCells.put(70, new MapperCell(70, cell_bs)); 
	        mapperCells.put(71, new MapperCell(71, cell_bt)); 
	        mapperCells.put(72, new MapperCell(72, cell_bu)); 
	        mapperCells.put(73, new MapperCell(73, cell_bv)); 
	        mapperCells.put(74, new MapperCell(74, cell_bw)); 
	        mapperCells.put(75, new MapperCell(75, cell_bx)); 
	        mapperCells.put(76, new MapperCell(76, cell_by)); 
	        mapperCells.put(77, new MapperCell(77, cell_bz)); 
	        mapperCells.put(78, new MapperCell(78, cell_ca)); 
	        mapperCells.put(79, new MapperCell(79, cell_cb)); 
	        mapperCells.put(80, new MapperCell(80, cell_cc)); 
	        mapperCells.put(81, new MapperCell(81, cell_cd)); 
	        mapperCells.put(82, new MapperCell(82, cell_ce)); 
	        mapperCells.put(83, new MapperCell(83, cell_cf)); 
	        mapperCells.put(84, new MapperCell(84, cell_cg)); 
	        mapperCells.put(85, new MapperCell(85, cell_ch)); 
	        mapperCells.put(86, new MapperCell(86, cell_ci)); 
	        mapperCells.put(87, new MapperCell(87, cell_cj)); 
	        mapperCells.put(88, new MapperCell(88, cell_ck)); 
	        mapperCells.put(89, new MapperCell(89, cell_cl)); 
	        mapperCells.put(90, new MapperCell(90, cell_cm)); 
	        mapperCells.put(91, new MapperCell(91, cell_cn)); 
	        mapperCells.put(92, new MapperCell(92, cell_co)); 
	        mapperCells.put(93, new MapperCell(93, cell_cp)); 
	        mapperCells.put(94, new MapperCell(94, cell_cq)); 
	        mapperCells.put(95, new MapperCell(95, cell_cr)); 
	        mapperCells.put(96, new MapperCell(96, cell_cs)); 
	        mapperCells.put(97, new MapperCell(97, cell_ct)); 
	        mapperCells.put(98, new MapperCell(98, cell_cu)); 
	        mapperCells.put(99, new MapperCell(99, cell_cv)); 
	        mapperCells.put(100, new MapperCell(100, cell_cw)); 
	        mapperCells.put(101, new MapperCell(101, cell_cx)); 
	        mapperCells.put(102, new MapperCell(102, cell_cy)); 
	        mapperCells.put(103, new MapperCell(103, cell_cz)); 
	 	}	 
	 
		private void loadMapperData() 
		{ 
			EtcAdmin.i().setStatusMessage("loading Mapper..."); 
			EtcAdmin.i().setProgress(.5); 
	 
			Task<Void> task = new Task<Void>() 
			{ 
	            @Override 
	            protected Void call() throws Exception  
	            { 
	    			//load from the server 
	        		DynamicDeductionFileSpecificationRequest dynDedReq = new DynamicDeductionFileSpecificationRequest(DataManager.i().mDynamicDeductionFileSpecification); 
	        		dynDedReq.setId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
	 
	        		DataManager.i().mDynamicDeductionFileSpecification = AdminPersistenceManager.getInstance().get(dynDedReq); 
	 
	    			EtcAdmin.i().setProgress(.75); 
	                return null; 
	            } 
	        }; 
	    	task.setOnSucceeded(e -> updateFileData()); 
	    	task.setOnFailed(e -> updateFileData()); 
	        new Thread(task).start();
		} 
	 
		//Handles the data source selection 
		private void updateFileData() 
		{ 
			updateDeductionFileData(); 
			loadIgnoreRows(DataManager.i().mDynamicDeductionFileSpecification); 
			checkReadOnly(); 
	        EtcAdmin.i().setStatusMessage("Ready"); 
	        EtcAdmin.i().setProgress(0); 
		} 

		private void loadIgnoreRows(DynamicDeductionFileSpecification fSpec) 
		{ 
			try {
				DynamicDeductionFileIgnoreRowSpecificationRequest request = new DynamicDeductionFileIgnoreRowSpecificationRequest(DataManager.i().mDynamicDeductionFileIgnoreRowSpecification); 
				request.setSpecificationId(fSpec.getId()); 
				DataManager.i().mDynamicDeductionFileIgnoreRowSpecifications = AdminPersistenceManager.getInstance().getAll(request);
			} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); } 
		    catch (Exception e) { DataManager.i().logGenericException(e); }
			 
		    List<HBoxIgnoreRowCell> ignoreRowList = new ArrayList<>(); 
		     
			for(DynamicDeductionFileIgnoreRowSpecification spec : DataManager.i().mDynamicDeductionFileIgnoreRowSpecifications) 
			{ 
			    if(spec.isActive() == true && spec.isDeleted() == false) 
			    { 
					ignoreRowList.add(new HBoxIgnoreRowCell(spec)); 
			    } 
			} 
			ObservableList<HBoxIgnoreRowCell> myObservableDepartmentList = FXCollections.observableList(ignoreRowList); 
			dfcovIgnoreRowSpecsListView.setItems(myObservableDepartmentList); 
		} 

		/* 
		 * DynamicCoverageFileSpecification Support 
		 */ 
		private void updateDeductionFileData() 
		{ 
			DynamicDeductionFileSpecification fSpec = DataManager.i().mDynamicDeductionFileSpecification; 
			
			if(fSpec != null)  
			{ 
				readOnly = fSpec.isLocked(); 
				Utils.updateControl(dfcovNameLabel,fSpec.getName()); 
				Utils.updateControl(dfcovTabIndexLabel,String.valueOf(fSpec.getTabIndex()));
				Utils.updateControl(dfcovHeaderRowIndexLabel,String.valueOf(fSpec.getHeaderRowIndex())); 
				dfpayPeriodCheck.setVisible(true); 
				Utils.updateControl(dfpayPeriodCheck,fSpec.isCreatePayPeriod()); 
				dfcovCreateEmployeeCheck.setVisible(true); 
				Utils.updateControl(dfcovCreateEmployeeCheck,fSpec.isCreateEmployee()); 
				dfplanCheck.setVisible(true); 
				Utils.updateControl(dfplanCheck,fSpec.isCreatePlan()); 
				dfcovLockedCheck.setVisible(true); 
				Utils.updateControl(dfcovLockedCheck,fSpec.isLocked()); 
				Utils.updateControl(dfcovSkipLastRowCheck, fSpec.isSkipLastRow()); 
	 
				//core data read only 
				Utils.updateControl(dfcovCoreIdLabel,String.valueOf(fSpec.getId())); 
				Utils.updateControl(dfcovCoreActiveLabel,String.valueOf(fSpec.isActive())); 
				Utils.updateControl(dfcovCoreBODateLabel,fSpec.getBornOn()); 
				Utils.updateControl(dfcovCoreLastUpdatedLabel,fSpec.getLastUpdated()); 
	 
				//custom text fields  
				Utils.updateControl(eeCF1, fSpec.getCfld1Name()); 
				Utils.updateControl(eeCF2, fSpec.getCfld2Name()); 
				Utils.updateControl(eeCF3, fSpec.getCfld3Name()); 
				Utils.updateControl(eeCF4, fSpec.getCfld4Name()); 
				Utils.updateControl(eeCF5, fSpec.getCfld5Name()); 
				Utils.updateControl(deductCF1, fSpec.getDeductionCfld1Name()); 
				Utils.updateControl(deductCF2, fSpec.getDeductionCfld2Name()); 
				Utils.updateControl(deductCF3, fSpec.getDeductionCfld3Name()); 
	 
				//reference counts 
				setEmpRefCount(getDeductionFileEmployerRefCount()); 
	 
				//update the column field table 
				loadFieldSet1(fSpec);	  
			} 
		} 
	 
		//Generate the deduction spec type 
		private void loadFieldSet1(DynamicDeductionFileSpecification fSpec) 
		{ 
			//EMPLOYEE SECTION 
			enableFieldSet(fSpec.isErCol(), fSpec.getErColIndex(), emprIdSkPn, null, null); 
			enableFieldSet(fSpec.isSsnCol(), fSpec.getSsnColIndex(), emplSsnSkPn, fSpec.getSsnParsePatternId(), null); 
			enableFieldSet(fSpec.isErRefCol(), fSpec.getErRefColIndex(), emplrEmpIdSkPn, fSpec.getErRefParsePatternId(), null); 
			enableFieldSet(fSpec.isfNameCol(), fSpec.getfNameColIndex(), empFstNmSkPn, fSpec.getfNameParsePatternId(), null); 
			enableFieldSet(fSpec.ismNameCol(), fSpec.getmNameColIndex(), emplMNmSkPn, fSpec.getmNameParsePatternId(), null); 
			enableFieldSet(fSpec.islNameCol(), fSpec.getlNameColIndex(), emplLstNmSkPn, fSpec.getlNameParsePatternId(), null); 
			enableFieldSet(fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), emplStNmSkPn, fSpec.getStreetParsePatternId(), null); 
			enableFieldSet(fSpec.isStreetCol(), fSpec.getStreetColIndex(), emplAdLn1SkPn, fSpec.getStreetParsePatternId(), null); 
			enableFieldSet(fSpec.isLin2Col(), fSpec.getLin2ColIndex(), emplAdLn2SkPn, null, null); 
			enableFieldSet(fSpec.isCityCol(), fSpec.getCityColIndex(), emplCitySkPn, fSpec.getCityParsePatternId(), null); 
			enableFieldSet(fSpec.isStateCol(), fSpec.getStateColIndex(), emplStateSkPn, fSpec.getStateParsePatternId(), null); 
			enableFieldSet(fSpec.isZipCol(), fSpec.getZipColIndex(), emplZipSkPn, fSpec.getZipParsePatternId(), null); 
			enableFieldSet(fSpec.isDobCol(), fSpec.getDobColIndex(), empDOBSkPn, null, fSpec.getDobFormatId()); 
			enableFieldSet(fSpec.isPpdStartDtCol(), fSpec.getPpdStartDtColIndex(), payStrtDtSkPn, fSpec.getPpdStartDtParsePatternId(), fSpec.getPpdStartDtFormatId()); 
			enableFieldSet(fSpec.isPpdEndDtCol(), fSpec.getPpdEndDtColIndex(), payEndDtSkPn, fSpec.getPpdEndDtParsePatternId(), fSpec.getPpdEndDtFormatId()); 
			enableFieldSet(fSpec.isPayDtCol(), fSpec.getPayDtColIndex(), payDtSkPn, fSpec.getPayDtParsePatternId(), fSpec.getPayDtFormatId()); 
			enableFieldSet(fSpec.isPpdErRefCol(), fSpec.getPpdErRefColIndex(), payPrdIdSkPn, null, null); 
			enableFieldSet(fSpec.isAmtCol(), fSpec.getAmtColIndex(), amtSkPn, null, null); 
			enableFieldSet(fSpec.isPlanRefCol(), fSpec.getPlanRefColIndex(), covPlanSkPn, fSpec.getPlanRefParsePatternId(), null); 
			enableFieldSet(fSpec.isDeductionCfld1Col(), fSpec.getDeductionCfld1ColIndex(), dedCF1SkPn, fSpec.getDeductionCfld1ParsePatternId(), null); 
			enableFieldSet(fSpec.isDeductionCfld2Col(), fSpec.getDeductionCfld2ColIndex(), dedCF2SkPn, fSpec.getDeductionCfld2ParsePatternId(), null); 
			enableFieldSet(fSpec.isDeductionCfld3Col(), fSpec.getDeductionCfld3ColIndex(), dedCF3SkPn, null, null); 
			enableFieldSet(fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), emplCF1SkPn, fSpec.getCfld1ParsePatternId(), null); 
			enableFieldSet(fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), emplCF2SkPn, fSpec.getCfld2ParsePatternId(), null); 
			enableFieldSet(fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), emplCF3SkPn, null, null); 
			enableFieldSet(fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), emplCF4SkPn, null, null); 
			enableFieldSet(fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), emplCF5SkPn, null, null); 
		} 
	 
		/** 
		 * <p> 
		 * enableFieldSet turns on a MapperField on the page based on its status in the mapper specification.</p> 
		 *  
		 * @param enable boolean turns it on or off 
		 * @param idx The column index from the specification 
		 * @param skPn The StackPane is used to lookup the MapperField from the list of all of them 
		 * @param ptn The configured parse pattern 
		 * @param fmt The configured date format 
		 */ 
		private void enableFieldSet(final boolean enable, final Integer idx, final StackPane skPn, final Long ptn, final Long fmt) 
		{ 
			//RESOURCES 
			MapperField fld = null; 
			ColorPalette clr = null; 
	 
			if(skPn != null ?(fld = mapperFields.get(skPn)) != null : false) 
			{ 
				if(enable) 
				{					 
					if(!fld.getCkEnabled().isSelected() && idx != null) 
					{ 
						refreshPatterns(fld, idx, ptn, fmt); 

						//SET FIELDS TO ENABLED STATES 
						fld.getCkEnabled().setSelected(true); 
						fld.getTxtFldIndex().setText(idx.toString()); 
						fld.getTxtFldIndex().setDisable(false); 

						//CREATE OR UPDATE MAPPERCELL AND GET ITS COLOR 
						clr = registerCell(idx, fld, ptn, fmt); 
	 
						fld.getStkPane().setBackground(new Background(new BackgroundFill(Color.valueOf(clr.getColor()), CornerRadii.EMPTY, Insets.EMPTY))); 
						if(fld.getBtnParsePtrn() != null) 
						{ 
							fld.getBtnParsePtrn().setDisable(false); 
							fld.getBtnParsePtrn().setText("+ Pattern"); 
							fld.getBtnParsePtrn().setFont(Font.font("Veranda", 12.0)); 
//							fld.getBtnParsePtrn().setStyle("-fx-background-color: ".concat(clr.getColor()).concat(";")); 
							if(ptn != null) 
								fld.getBtnParsePtrn().setText("Pattern: ".concat(ptn.toString())); 
							if(ptn != null) { fld.getBtnParsePtrn().setId(ptn.toString()); } 
								fld.setParsePatternId(ptn); 
						} 
						if(fld.getBtnDtFormat() != null) 
						{ 
							fld.getBtnDtFormat().setDisable(false); 
							fld.getBtnDtFormat().setText("+ Format"); 
							fld.getBtnDtFormat().setFont(Font.font("Veranda", 12.0)); 
//							fld.getBtnDtFormat().setStyle("-fx-background-color: ".concat(clr.getColor()).concat(";")); 
							if(fmt != null) 
								fld.getBtnDtFormat().setText("Format: ".concat(fmt.toString())); 
							if(fmt != null) { fld.getBtnDtFormat().setId(fmt.toString()); } 
								fld.setDateFormatId(fmt); 
						} 
						if(fld.getBtnReferences() != null) 
						{ 
							fld.getBtnReferences().setDisable(false); 
							fld.getBtnReferences().setStyle("-fx-background-color: ".concat(clr.getColor()).concat(";")); 
						} 
					}else { 
						fld.getTxtFldIndex().setDisable(true); 
					} 
				} 
			} 
		} 
	 
		/** 
		 * <p> 
		 * registerCell links a MapperField to its representation in the spreadsheet row at the top<br> 
		 *  of the form.</p> 
		 *  
		 * @param idx The column index. 
		 * @param fld The set of form fields to be linked. 
		 *  
		 * @return The assigned ColorPalette 
		 */ 
		private ColorPalette registerCell(final Integer idx, final MapperField fld, final Long ptn, final Long fmt) 
		{ 
			//RESOURCES 
			MapperCell cel = null; 
			ColorPalette plt = null; 
			boolean found = false; 
			StringBuilder sb = null; 
	 
			if(idx != null && fld != null) 
			{										 
				if((cel = mapperCells.get(idx)) != null) 
				{ 
					//ADD MAPPERFIELD TO MAPPERCELL 
					if(!cel.getMapperFields().contains(fld)) 
					{ 
						cel.getMapperFields().add(fld); 
						columnEdit(idx, fld); 
	 
						//UPDATE TEXT 
						sb = new StringBuilder(); 
						cel.getNode().setFont(Font.font("Veranda", 12.0)); 
						for(MapperField mf : cel.getMapperFields()) 
						{ 
							sb.append(sb.length() == 0 ? mf.getTitle() : "\n".concat(mf.getTitle())); 
						} 
						cel.getNode().setText(sb.toString()); 
					} 
	 
					//COLOR 
					if((plt = cel.getColor()) == null) 
					{ 
						for(ColorPalette c : ColorPalette.values()) 
							if(!c.equals(ColorPalette.AZURE)) 
							{ 
								found = false; 
								for(MapperCell m : mapperCells.values()) 
									if(m.getColor() != null ? m.getColor().equals(c) : false) 
									{ 
										found = true; 
										break; 
									} 
								if(!found) 
								{ 
									cel.setColor(plt = c); 
									break; 
								} 
							} 
	 
						//SET COLOR 
						cel.getNode().setStyle("-fx-control-inner-background: ".concat(plt.getColor()).concat(";")); 
					} 
				} 
			} 
			return plt != null ? plt : ColorPalette.AZURE; 
		} 
	 
		/** 
		 * <p> 
		 * deregisterCell removes a MapperField from a MapperCell and if that cell is empty,<br> 
		 *  it removes the MapperCell as well.</p> 
		 *  
		 * @param idxThe column index. 
		 * @param fld The set of form fields to be removed. 
		 */ 
		private void deregisterCell(final Integer idx, final MapperField fld) 
		{ 
			//RESOURCES 
			MapperCell cel = null; 
			int didx = 0; 
			Integer ridx = null; 
	 
			if((cel = mapperCells.get(idx)) != null) 
			{ 
				//REMOVE MAPPERFIELD FROM CELL 
				mapperCells.get(idx).getMapperFields().remove(fld); 
	 
				textAreaRefresh(idx, fld, cel); 
	 
				//REMOVE CELL IF EMPTY 
				if(cel.getMapperFields().isEmpty()) 
				{ 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(40); 
					cel.getNode().setStyle("-fx-control-inner-background: ".concat(Color.WHITE.toString()).concat(";")); 
					cel.setColor(null); 
					mapperCells.get(idx).getNode().clear(); 
	 
//					mapperCells.remove(idx); 
					didx = idx.intValue(); 
					while((--didx) >= 0) 
					{ 
						ridx = null; 
						for(Integer i : mapperCells.keySet()) 
							if(i.equals(didx) ? mapperCells.get(i).getMapperFields().isEmpty() : false) 
							{ 
								ridx = i; 
								break; 
							}					 
//						if(ridx != null) 
//							mapperCells.remove(ridx); 
//						else 
//							break; 
					} 
				} 
			} 
		} 
	 
		/** 
		 * <p> 
		 * fieldSelected is the general handler for all of the MapperField checkboxes.</p> 
		 *  
		 * @param event 
		 */ 
		@FXML 
		private void fieldSelected(final ActionEvent event) 
		{ 
			//RESOURCES 
			CheckBox cb = null; 
			Integer idx = null; 
			ColorPalette clr = null; 
	 
			if(event.getSource() instanceof CheckBox) 
			{ 
				//CAST AS CHECKBOX 
				cb = CheckBox.class.cast(event.getSource()); 
	 
				//FIND SPECIFIC CHECKBOX MAPPERFIELD 
				for(MapperField f : mapperFields.values()) 
				{ 
					if(cb.equals(f.getCkEnabled())) 
					{ 
						if(f.getTxtFldIndex().getText().length() > 0) 
							idx = Integer.decode(f.getTxtFldIndex().getText()); 
						else 
							idx = new Integer(0); 
	 
						//REGISTER OR DEREGISTER CELLS 
						if(cb.isSelected()) 
						{					 
							if(f.getTxtFldIndex().getText().length() == 0) 
								f.getTxtFldIndex().setText(""); 
							 
							clr = registerCell(idx, f, f.getParsePatternId(), f.getDateFormatId()); 
	 
							f.getTxtFldIndex().setDisable(false); 
							f.getStkPane().setBackground(new Background(new BackgroundFill(Color.valueOf(clr.getColor()), CornerRadii.EMPTY, Insets.EMPTY))); 
							if(f.getBtnParsePtrn() != null) 
							{ 
//								f.getBtnParsePtrn().setDisable(false); 
//								f.getBtnParsePtrn().setStyle("-fx-background-color: ".concat(clr.getColor()).concat(";")); 
							} 
							if(f.getBtnDtFormat() != null) 
							{ 
//								f.getBtnDtFormat().setDisable(false); 
//								fld.getBtnDtFormat().setStyle("-fx-background-color: ".concat(clr.getColor()).concat(";")); 
							} 
							if(f.getBtnReferences() != null) 
							{ 
//								f.getBtnReferences().setDisable(false); 
								f.getBtnReferences().setStyle("-fx-background-color: ".concat(clr.getColor()).concat(";")); 
							} 
						}else 
						{ 
							checkBoxChanged(f); 
	 
							f.getTxtFldIndex().setDisable(true); 
							f.getTxtFldIndex().setText(""); 
							f.getStkPane().setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
							if(f.getBtnParsePtrn() != null) 
							{ 
								f.getBtnParsePtrn().setDisable(true); 
//								f.getBtnParsePtrn().setStyle("-fx-background-color: #f0ffff;"); 
							} 
							if(f.getBtnDtFormat() != null) 
							{ 
								f.getBtnDtFormat().setDisable(true); 
//								f.getBtnDtFormat().setStyle("-fx-background-color: #f0ffff;"); 
							} 
							if(f.getBtnReferences() != null) 
							{ 
								f.getBtnReferences().setDisable(true); 
								f.getBtnReferences().setStyle("-fx-background-color: #f0ffff;"); 
							} 
							deregisterCell(idx, f); 
						} 
						break; 
					} 
				} 
			} 
		} 
	 
		/** 
		 * <p> 
		 * indexChanged is triggered whenever the value in a MapperField's index text field is edited.</p> 
		 *  
		 * @param event 
		 */ 
		@FXML 
		private void indexChanged(final KeyEvent event) throws NumberFormatException 
		{ 
			//RESOURCES 
			TextField txt = null; 
			Integer odx = null; 
			Integer idx = null; 
			ColorPalette clr = null; 
	 
			try { 
	 
				if(event.getSource() instanceof TextField) 
				{ 
					//CAST AS CHECKBOX 
					txt = TextField.class.cast(event.getSource()); 
		 
					//FIND SPECIFIC TEXTFIELD MAPPERFIELD 
					for(MapperField f : mapperFields.values()) 
					{ 
						if(txt.equals(f.getTxtFldIndex())) 
						{ 
							if(f.getTxtFldIndex().getText().length() > 0) 
								idx = Integer.decode(f.getTxtFldIndex().getText()); 
							else 
								idx = new Integer(0); 
	 
							//FIND CURRENTLY ASSOCIATED MAPPERCELL 
							for(MapperCell c : mapperCells.values()) 
								if(!c.getMapperFields().isEmpty()) 
									for(MapperField cf : c.getMapperFields()) 
										if(cf.equals(f)) 
										{ 
											odx = c.getIdx(); 
											break; 
										} 
							 
							if(odx != null) 
	 
								deregisterCell(odx, f); 
	 
							if(f.getTxtFldIndex().getText().length() == 0) 
								f.getTxtFldIndex().setText(""); 
	 
							clr = registerCell(idx, f, f.getParsePatternId(), f.getDateFormatId()); 
	 
							f.getTxtFldIndex().setDisable(false); 
							f.getStkPane().setBackground(new Background(new BackgroundFill(Color.valueOf(clr.getColor()), CornerRadii.EMPTY, Insets.EMPTY))); 
	 
							if(f.getBtnParsePtrn() != null) 
							{ 
								refreshPatterns(f, idx, f.getParsePatternId(), f.getDateFormatId()); 
								f.getBtnParsePtrn().setDisable(false);
		//						f.getBtnParsePtrn().setStyle("-fx-background-color: ".concat(clr.getColor()).concat(";")); 
							} 
							if(f.getBtnDtFormat() != null) 
							{ 
								refreshPatterns(f, idx, f.getParsePatternId(), f.getDateFormatId()); 
								f.getBtnDtFormat().setDisable(false); 
		//						fld.getBtnDtFormat().setStyle("-fx-background-color: ".concat(clr.getColor()).concat(";")); 
							} 
							if(f.getBtnParsePtrn() == null && f.getBtnDtFormat() == null) 
							{ 
								refreshPatterns(f, idx, f.getParsePatternId(), f.getDateFormatId()); 
							} 
							if(f.getBtnReferences() != null) 
							{ 
								f.getBtnReferences().setDisable(false); 
								f.getBtnReferences().setStyle("-fx-background-color: ".concat(clr.getColor()).concat(";")); 
							} 
							break; 
						} 
					} 
				} 
			} catch(NumberFormatException e) { 
				System.out.println("Use number format of no more than two digits"); 
			} 
		} 
		 
		/** 
		 * <p> 
		 * checkBoxChanged is triggered whenever the value in a Check Box field is changed.</p> 
		 *  
		 * @param fld 
		 */ 
		private void checkBoxChanged(MapperField fld) 
		{ 
			DynamicDeductionFileSpecification fSpec = DataManager.i().mDynamicDeductionFileSpecification; 
	 
			if(fld.getParsePatternId() != null) { fld.setTitle(fld.getTitle().replace(" (P:" + fld.getParsePatternId().toString() + ")", "")); fld.setParsePatternId(null); } 
			if(fld.getDateFormatId() != null) { fld.setTitle(fld.getTitle().replace(" (F:" + fld.getDateFormatId().toString() + ")", "")); fld.setDateFormatId(null); } 
	 
			if(emprId.isSelected() == false){ fSpec.setErCol(false); fSpec.setErColIndex(0); } 
			if(emplSsn.isSelected() == false){ fSpec.setSsnCol(false); fSpec.setSsnColIndex(0); fSpec.setSsnParsePattern(null); 
			   emplSsnParse.setFont(Font.font("Veranda", 12.0)); emplSsnParse.setText("+ Pattern"); } 
			if(emplrEmpId.isSelected() == false){ fSpec.setErRefCol(false); fSpec.setErRefColIndex(0); fSpec.setErRefParsePattern(null); 
			   emplrEmpIdParse.setFont(Font.font("Veranda", 12.0)); emplrEmpIdParse.setText("+ Pattern"); } 
			if(empFstNm.isSelected() == false){ fSpec.setfNameParsePattern(null); fSpec.setfNameCol(false); fSpec.setfNameColIndex(0);  
			   empFstNmParse.setFont(Font.font("Veranda", 12.0)); empFstNmParse.setText("+ Pattern"); } 
			if(emplMNm.isSelected() == false){ fSpec.setmNameCol(false); fSpec.setmNameColIndex(0); fSpec.setmNameParsePattern(null); 
			   emplMNmParse.setFont(Font.font("Veranda", 12.0)); emplMNmParse.setText("+ Pattern"); } 
			if(emplLstNm.isSelected() == false){ fSpec.setlNameCol(false); fSpec.setlNameColIndex(0); fSpec.setlNameParsePattern(null); 
			   emplLstNmParse.setFont(Font.font("Veranda", 12.0)); emplLstNmParse.setText("+ Pattern"); } 
			if(emplStNm.isSelected() == false){ fSpec.setStreetNumCol(false); fSpec.setStreetNumColIndex(0); fSpec.setStreetParsePattern(null); }
			if(emplAdLn1.isSelected() == false){ fSpec.setStreetCol(false); fSpec.setStreetColIndex(0); fSpec.setStreetParsePattern(null); 
			   emplAdLn1Parse.setFont(Font.font("Veranda", 12.0)); emplAdLn1Parse.setText("+ Pattern"); } 
			if(emplAdLn2.isSelected() == false){ fSpec.setLin2Col(false); fSpec.setLin2ColIndex(0); } 
			if(emplCity.isSelected() == false){ fSpec.setCityCol(false); fSpec.setCityColIndex(0); fSpec.setCityParsePattern(null); 
			   emplCityParse.setFont(Font.font("Veranda", 12.0)); emplCityParse.setText("+ Pattern"); } 
			if(emplState.isSelected() == false){ fSpec.setStateCol(false); fSpec.setStateColIndex(0); fSpec.setStateParsePattern(null); 
			   emplStateParse.setFont(Font.font("Veranda", 12.0)); emplStateParse.setText("+ Pattern"); } 
			if(emplZip.isSelected() == false){ fSpec.setZipCol(false); fSpec.setZipColIndex(0); fSpec.setZipParsePattern(null); 
			   emplZipParse.setFont(Font.font("Veranda", 12.0)); emplZipParse.setText("+ Pattern"); } 
			if(empDOB.isSelected() == false){ fSpec.setDobCol(false); fSpec.setDobColIndex(0); fSpec.setDobFormat(null); 
			   empDOBFormat.setFont(Font.font("Veranda", 12.0)); empDOBFormat.setText("+ Format"); } 
			if(payStrtDt.isSelected() == false){ fSpec.setPpdStartDtCol(false); fSpec.setPpdStartDtColIndex(0); fSpec.setPpdStartDtFormat(null); fSpec.setPpdStartDtParsePattern(null); 
			   payStrtDtParse.setFont(Font.font("Veranda", 12.0)); payStrtDtParse.setText("+ Pattern"); 
			   payStrtDtFormat.setFont(Font.font("Veranda", 12.0)); payStrtDtFormat.setText("+ Format"); } 
			if(payEndDt.isSelected() == false){ fSpec.setPpdEndDtCol(false); fSpec.setPpdEndDtColIndex(0); fSpec.setPpdEndDtFormat(null); fSpec.setPpdEndDtParsePattern(null); 
			   payEndDtParse.setFont(Font.font("Veranda", 12.0)); payEndDtParse.setText("+ Pattern");
			   payEndDtFormat.setFont(Font.font("Veranda", 12.0)); payEndDtFormat.setText("+ Format"); } 
			if(payDt.isSelected() == false){ fSpec.setPayDtCol(false); fSpec.setPayDtColIndex(0); fSpec.setPayDtFormat(null); fSpec.setPayDtParsePattern(null); 
			   payDtParse.setFont(Font.font("Veranda", 12.0)); payDtParse.setText("+ Pattern");
			   payDtFormat.setFont(Font.font("Veranda", 12.0)); payDtFormat.setText("+ Format"); } 
			if(amt.isSelected() == false){ fSpec.setAmtCol(false); fSpec.setAmtColIndex(0); } 
			if(covPlan.isSelected() == false){ fSpec.setPlanRefParsePattern(null); fSpec.setPlanRefCol(false); fSpec.setPlanRefColIndex(0); 
			   covPlanParse.setFont(Font.font("Veranda", 12.0)); covPlanParse.setText("+ Pattern"); } 
			if(dedCF1.isSelected() == false){ fSpec.setDeductionCfld1Col(false); fSpec.setDeductionCfld1ColIndex(0); fSpec.setDeductionCfld1ParsePattern(null);
			   dedCF1Parse.setFont(Font.font("Veranda", 12.0)); dedCF1Parse.setText("+ Pattern"); deductCF1.setText(""); } 
			if(dedCF2.isSelected() == false){ fSpec.setDeductionCfld2Col(false); fSpec.setDeductionCfld2ColIndex(0); fSpec.setDeductionCfld2ParsePattern(null);
			   dedCF2Parse.setFont(Font.font("Veranda", 12.0)); dedCF2Parse.setText("+ Pattern"); deductCF2.setText(""); } 
			if(dedCF3.isSelected() == false){ fSpec.setDeductionCfld3Col(false); fSpec.setDeductionCfld3ColIndex(0); }
			if(emplCF1.isSelected() == false){ fSpec.setCfld1Col(false); fSpec.setCfld1ColIndex(0); fSpec.setCfld1ParsePattern(null);
			   emplCF1Parse.setFont(Font.font("Veranda", 12.0)); emplCF1Parse.setText("+ Pattern"); eeCF1.setText(""); } 
			if(emplCF2.isSelected() == false){ fSpec.setCfld2Col(false); fSpec.setCfld2ColIndex(0); fSpec.setCfld2ParsePattern(null);
			   emplCF2Parse.setFont(Font.font("Veranda", 12.0)); emplCF2Parse.setText("+ Pattern"); eeCF2.setText(""); } 
			if(emplCF3.isSelected() == false){ fSpec.setCfld3Col(false); fSpec.setCfld3ColIndex(0); eeCF3.setText(""); } 
			if(emplCF4.isSelected() == false){ fSpec.setCfld4Col(false); fSpec.setCfld4ColIndex(0); eeCF4.setText(""); } 
			if(emplCF5.isSelected() == false){ fSpec.setCfld5Col(false); fSpec.setCfld5ColIndex(0); eeCF5.setText(""); } 
		} 
	 
		/** 
		 * <p> 
		 * customFieldChanged is triggered whenever the value in a Custom text field is edited.</p> 
		 *  
		 * @param event 
		 */ 
		@FXML 
		private void customFieldChanged(final KeyEvent event) 
		{ 
			DynamicDeductionFileSpecification fSpec = DataManager.i().mDynamicDeductionFileSpecification; 
	 
			if(deductCF1 != null){ fSpec.setDeductionCfld1Name(deductCF1.getText().toString()); } 
			if(deductCF2 != null){ fSpec.setDeductionCfld2Name(deductCF2.getText().toString()); } 
			if(deductCF3 != null){ fSpec.setDeductionCfld3Name(deductCF3.getText().toString()); } 
			if(eeCF1 != null){ fSpec.setCfld1Name(eeCF1.getText().toString()); } 
			if(eeCF2 != null){ fSpec.setCfld2Name(eeCF2.getText().toString()); } 
			if(eeCF3 != null){ fSpec.setCfld3Name(eeCF3.getText().toString()); } 
			if(eeCF4 != null){ fSpec.setCfld4Name(eeCF4.getText().toString()); } 
			if(eeCF5 != null){ fSpec.setCfld5Name(eeCF5.getText().toString()); } 
		} 
	 
		/* 
		 * textAreaRefresh is responsible for removing the string from the mapper header 
		 * and also for sizing the mapper header column down to the next largest String width passed in. 
		 */ 
		private void textAreaRefresh(final Integer idx, final MapperField fld, final MapperCell cel) throws NullPointerException 
		{ 
			for(Entry<Integer,MapperCell> mc : mapperCells.entrySet()) 
			{ 
				if(mc.getValue().getNode() != null) 
				{ 
					if(mc.getValue().getNode().getText().contains(fld.getTitle().toString())) 
					{ 
						StringBuilder sb = new StringBuilder(); 
	 
						for(MapperField mf : cel.getMapperFields())  
						{ 
							sb.append(sb.length() == 0 ? mf.getTitle() : "\n".concat(mf.getTitle())); 
							mc.getValue().getNode().setText(sb.toString()); 
	 
							if(cel.getNode().getText().contains(mf.getTitle().toString())) 
							{ 
//								if(cel.getNode().getText().toString().matches(".*\\((P|F):[0-9]+\\).*$")) 
								if(mf.getParsePattern() != null) 
								{ 
									if(cel.getNode().getText().toString().contains(" (P:" + mf.getParsePattern().getId().toString() + ")")) 
									{ 
										columnEdit(idx, fld); 
									} 							 
								}  
	 
								if(mf.getDateFormat() != null) 
								{ 
									if(cel.getNode().getText().toString().contains(" (F:" + mf.getDateFormat().getId().toString() + ")")) 
									{ 
										columnEdit(idx, fld); 
									}											 
								} 
	 
								if(mf.getParsePattern() == null && mf.getDateFormat() == null) 
								{ 
									columnEdit(idx, fld); 
								} 
	 
								if(mf.getParsePattern() != null && mf.getDateFormat() != null) 
								{ 
									columnEdit(idx, fld); 
								} 
							} 
						} 
					} 
				} 
			} 
		}	 
	 
		/* 
		 * columnEdit increases the mapper header column width to the largest width passed in 
		 * from the mapperField array. 
		 */ 
		private void columnEdit(final Integer idx, final MapperField fld) 
		{ 
			Double dub = null; 
			Integer dubWd = fld.getCellWidth(); 
	 
			dub = spreadsheet.getColumnConstraints().get(idx + 1).getMinWidth(); 
	 
			if(dubWd >= dub)  
			{ 
				if(fld.getParsePattern() != null && fld.getParsePattern().getId() >= 10) 
				{ 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(dubWd + 35); 
				} else { 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(dubWd); 
				} 
			} 
	 
			if(dubWd >= dub)  
			{ 
				if(fld.getParsePattern() != null && fld.getParsePattern().getId() <= 9) 
				{ 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(dubWd + 25); 
				} else { 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(dubWd); 
				} 
			}	 
			 
			if(dubWd >= dub)  
			{ 
				if(fld.getDateFormat() != null && fld.getDateFormat().getId() <= 10) 
				{ 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(dubWd + 25); 
				} else { 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(dubWd); 
				} 
			} 
			 
			if(dubWd >= dub) 
			{ 
				spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(dubWd); 
			} else { 
				spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(dub); 
			} 
		} 
		 
		private void checkReadOnly() 
		{ 
			dfcovNameLabel.setEditable(!readOnly); 
			dfcovDescriptionLabel.setEditable(!readOnly); 
			dfcovTabIndexLabel.setEditable(!readOnly); 
			dfcovHeaderRowIndexLabel.setEditable(!readOnly); 
			dfcovCreateEmployeeCheck.setDisable(readOnly); 
			dfplanCheck.setDisable(readOnly); 
			dfpayPeriodCheck.setDisable(readOnly);
			dfcovLockedCheck.setDisable(readOnly); 
			dfcovClearFormButton.setDisable(readOnly); 
			dfcovSaveChangesButton.setDisable(readOnly); 
		} 

		private int getDeductionFileEmployerRefCount() 
		{ 
			int locRefSize = 0; 

			try {
				DynamicDeductionFileEmployerReferenceRequest request = new DynamicDeductionFileEmployerReferenceRequest(DataManager.i().mDynamicDeductionFileEmployerReference); 
				request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
				DataManager.i().mDynamicDeductionFileSpecification.setErReferences(AdminPersistenceManager.getInstance().getAll(request));
			} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
		    catch (Exception e) { DataManager.i().logGenericException(e); }
			
			for(DynamicDeductionFileEmployerReference erRef : DataManager.i().mDynamicDeductionFileSpecification.getErReferences()) 
			{ 
				if(erRef.isActive() == true && erRef.isDeleted() == false) 
					locRefSize++; 
			} 
			return locRefSize; 
		} 

		private boolean validateData() 
		{ 
			boolean bReturn = true;

			//check for required data 
			if(!Utils.validate(dfcovNameLabel)) bReturn = false; 
			if(!Utils.validate(dfcovTabIndexLabel)) bReturn = false; 
			if(!Utils.validate(dfcovHeaderRowIndexLabel)) bReturn = false; 
			if(!Utils.validateIntTextField(dfcovTabIndexLabel)) bReturn = false; 
			if(!Utils.validateIntTextField(dfcovHeaderRowIndexLabel)) bReturn = false; 

			return bReturn; 
		} 

		private void setSaving(boolean saving) 
		{ 
			if(saving == false) 
	 
			dfcovSavingChangesButton.setVisible(saving); 
			dfcovSaveChangesButton.setVisible(!saving); 
		} 

		@FXML 
		private void onSave(ActionEvent event)  
		{ 
			//validate everything 
			if(!validateData()) 
				return; 
			dfcovSavingChangesButton.setVisible(true); 
			setSaving(true); 

			// new thread 
			Task<Void> task = new Task<Void>()  
			{ 
	            @Override 
	            protected Void call() throws Exception  
	            { 
	        		// update the current object 
	        		SaveFileSpecification(); 
	                return null; 
	            } 
	        }; 
	      	task.setOnScheduled(e ->  {
	  		EtcAdmin.i().setStatusMessage("..."); 
	  		EtcAdmin.i().setProgress(0.5);}); 

	    	task.setOnSucceeded(e ->  setSaving(false)); 
	    	task.setOnFailed(e ->  setSaving(false)); 
	        new Thread(task).start(); 
		} 

		private void SaveFileSpecification()  
		{ 
			updateDeductionFile(); 
			EtcAdmin.i().setProgress(0);
//			saveInsuranceFileSpecification(); 
		} 

		private void updateDeductionFile() 
		{ 
			if(DataManager.i().mDynamicDeductionFileSpecification != null) 
			{ 
				DynamicDeductionFileSpecification fSpec = DataManager.i().mDynamicDeductionFileSpecification; 
				DynamicDeductionFileSpecificationRequest request = new DynamicDeductionFileSpecificationRequest(); 

				//GATHER THE FIELDS 
				fSpec.setName(dfcovNameLabel.getText()); 
				fSpec.setTabIndex(Integer.valueOf(dfcovTabIndexLabel.getText())); 
				fSpec.setHeaderRowIndex(Integer.valueOf(dfcovHeaderRowIndexLabel.getText())); 
				fSpec.setCreatePlan(dfplanCheck.isSelected());
				fSpec.setCreatePayPeriod(dfpayPeriodCheck.isSelected());
				fSpec.setCreateEmployee(dfcovCreateEmployeeCheck.isSelected()); 
				fSpec.setSkipLastRow(dfcovSkipLastRowCheck.isSelected()); 
				fSpec.setLocked(dfcovLockedCheck.isSelected()); 

				// EMPLOYEE 
		     	// EMPLOYER IDENTIFIER 
				fSpec.setErCol(Boolean.valueOf(emprId.isSelected()));
				fSpec.setErColIndex(Integer.valueOf(emprIdIdx.getText() != null && !emprIdIdx.getText().isEmpty() ? emprIdIdx.getText() : "0")); 

				// EMPLOYEE SSN 
				fSpec.setSsnCol(Boolean.valueOf(emplSsn.isSelected())); 
				fSpec.setSsnColIndex(Integer.valueOf(emplSsnIdx.getText() != null && !emplSsnIdx.getText().isEmpty() ? emplSsnIdx.getText() : "0")); 
				if(emplSsnParse.getId() != null && Utils.isInt(emplSsnParse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(emplSsnParse.getId())); 
					request.setSsnParsePatternRequest(req); 
				} else request.setClearSsnParsePatternId(true); 

		     	// EMPLOYEE IDENTIFIER 
				fSpec.setErRefCol(Boolean.valueOf(emplrEmpId.isSelected())); 
				fSpec.setErRefColIndex(Integer.valueOf(emplrEmpIdIdx.getText() != null && !emplrEmpIdIdx.getText().isEmpty() ? emplrEmpIdIdx.getText() : "0")); 
				if(emplrEmpIdParse.getId() != null && Utils.isInt(emplrEmpIdParse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(emplrEmpIdParse.getId())); 
					request.setErRefParsePatternRequest(req); 
				} else request.setClearErRefParsePatternId(true); 

				// FIRST NAME 
				fSpec.setfNameCol(Boolean.valueOf(empFstNm.isSelected())); 
				fSpec.setfNameColIndex(Integer.valueOf(empFstNmIdx.getText() != null && !empFstNmIdx.getText().isEmpty() ? empFstNmIdx.getText() : "0")); 
				if(empFstNmParse.getId() != null && Utils.isInt(empFstNmParse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(empFstNmParse.getId())); 
					request.setfNameParsePatternRequest(req); 
				} else request.setClearFNameParsePatternId(true); 

				// MIDDLE NAME 
				fSpec.setmNameCol(Boolean.valueOf(emplMNm.isSelected())); 
				fSpec.setmNameColIndex(Integer.valueOf(emplMNmIdx.getText() != null && !emplMNmIdx.getText().isEmpty() ? emplMNmIdx.getText() : "0")); 
				if(emplMNmParse.getId() != null && Utils.isInt(emplMNmParse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(emplMNmParse.getId())); 
					request.setmNameParsePatternRequest(req); 
				} else request.setClearMNameParsePatternId(true); 

				// LAST NAME 
				fSpec.setlNameCol(Boolean.valueOf(emplLstNm.isSelected())); 
				fSpec.setlNameColIndex(Integer.valueOf(emplLstNmIdx.getText() != null && !emplLstNmIdx.getText().isEmpty() ? emplLstNmIdx.getText() : "0")); 
				if(emplLstNmParse.getId() != null && Utils.isInt(emplLstNmParse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(emplLstNmParse.getId())); 
					request.setlNameParsePatternRequest(req); 
				} else request.setClearLNameParsePatternId(true); 

				// STREET NUMBER 
				fSpec.setStreetNumCol(Boolean.valueOf(emplStNm.isSelected())); 
				fSpec.setStreetNumColIndex(Integer.valueOf(emplStNmIdx.getText() != null && !emplStNmIdx.getText().isEmpty() ? emplStNmIdx.getText() : "0")); 

				// ADDRESS LINE 1 
				fSpec.setStreetCol(Boolean.valueOf(emplAdLn1.isSelected())); 
				fSpec.setStreetColIndex(Integer.valueOf(emplAdLn1Idx.getText() != null && !emplAdLn1Idx.getText().isEmpty() ? emplAdLn1Idx.getText() : "0")); 
				if(emplAdLn1Parse.getId() != null && Utils.isInt(emplAdLn1Parse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(emplAdLn1Parse.getId())); 
					request.setStreetParsePatternRequest(req); 
				} else { request.setClearStreetParsePatternId(true); } 

				// ADDRESS LINE 2 
				fSpec.setLin2Col(Boolean.valueOf(emplAdLn2.isSelected())); 
				fSpec.setLin2ColIndex(Integer.valueOf(emplAdLn2Idx.getText() != null && !emplAdLn2Idx.getText().isEmpty() ? emplAdLn2Idx.getText() : "0")); 

				// CITY 
				fSpec.setCityCol(Boolean.valueOf(emplCity.isSelected())); 
				fSpec.setCityColIndex(Integer.valueOf(emplCityIdx.getText() != null && !emplCityIdx.getText().isEmpty() ? emplCityIdx.getText() : "0")); 
				if(emplCityParse.getId() != null && Utils.isInt(emplCityParse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(emplCityParse.getId())); 
					request.setCityParsePatternRequest(req); 
				} else request.setClearCityParsePatternId(true); 

				// STATE 
				fSpec.setStateCol(Boolean.valueOf(emplState.isSelected())); 
				fSpec.setStateColIndex(Integer.valueOf(emplStateIdx.getText() != null && !emplStateIdx.getText().isEmpty() ? emplStateIdx.getText() : "0")); 
				if(emplStateParse.getId() != null && Utils.isInt(emplStateParse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(emplStateParse.getId())); 
					request.setStateParsePatternRequest(req); 
				} else request.setClearStateParsePatternId(true); 

				// ZIP 
				fSpec.setZipCol(Boolean.valueOf(emplZip.isSelected())); 
				fSpec.setZipColIndex(Integer.valueOf(emplZipIdx.getText() != null && !emplZipIdx.getText().isEmpty() ? emplZipIdx.getText() : "0")); 
				if(emplZipParse.getId() != null && Utils.isInt(emplZipParse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(emplZipParse.getId())); 
					request.setZipParsePatternRequest(req); 
				} else request.setClearZipParsePatternId(true); 

				// DATE OF BIRTH 
				fSpec.setDobCol(Boolean.valueOf(empDOB.isSelected())); 
				fSpec.setDobColIndex(Integer.valueOf(empDOBIdx.getText() != null && !empDOBIdx.getText().isEmpty() ? empDOBIdx.getText() : "0")); 
				if(empDOBFormat.getId() != null && Utils.isInt(empDOBFormat.getId()))  
				{ 
					PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
					req.setId(Long.valueOf(empDOBFormat.getId())); 
					request.setDobFormatRequest(req); 
				} else request.setClearDobFormatId(true); 
				
		     	// PAY PERIOD START DATE 
				fSpec.setPpdStartDtCol(Boolean.valueOf(payStrtDt.isSelected())); 
				fSpec.setPpdStartDtColIndex(Integer.valueOf(payStrtDtIdx.getText() != null && !payStrtDtIdx.getText().isEmpty() ? payStrtDtIdx.getText() : "0")); 
				if(payStrtDtParse.getId() != null && Utils.isInt(payStrtDtParse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(payStrtDtParse.getId())); 
					request.setPpdStartDtParsePatternRequest(req); 
				} else request.setClearPpdStartDtParsePatternId(true); 
				if(payStrtDtFormat.getId() != null && Utils.isInt(payStrtDtFormat.getId()))  
				{ 
					PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
					req.setId(Long.valueOf(payStrtDtFormat.getId())); 
					request.setPpdStartDtFormatRequest(req); 
				} else request.setClearPpdStartDtFormatId(true); 

		     	// PAY PERIOD END DATE 
				fSpec.setPpdEndDtCol(Boolean.valueOf(payEndDt.isSelected())); 
				fSpec.setPpdEndDtColIndex(Integer.valueOf(payEndDtIdx.getText() != null && !payEndDtIdx.getText().isEmpty() ? payEndDtIdx.getText() : "0")); 
				if(payEndDtParse.getId() != null && Utils.isInt(payEndDtParse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(payEndDtParse.getId())); 
					request.setPpdEndDtParsePatternRequest(req); 
				} else request.setClearPpdEndDtParsePatternId(true); 
				if(payEndDtFormat.getId() != null && Utils.isInt(payEndDtFormat.getId()))  
				{ 
					PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
					req.setId(Long.valueOf(payEndDtFormat.getId())); 
					request.setPpdEndDtFormatRequest(req); 
				} else request.setClearPpdEndDtFormatId(true); 

		     	// PAY DATE
				fSpec.setPayDtCol(Boolean.valueOf(payDt.isSelected())); 
				fSpec.setPayDtColIndex(Integer.valueOf(payDtIdx.getText() != null && !payDtIdx.getText().isEmpty() ? payDtIdx.getText() : "0")); 
				if(payDtParse.getId() != null && Utils.isInt(payDtParse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(payDtParse.getId())); 
					request.setPayDtParsePatternRequest(req); 
				} else request.setClearPayDtParsePatternId(true); 
				if(payDtFormat.getId() != null && Utils.isInt(payDtFormat.getId()))  
				{ 
					PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
					req.setId(Long.valueOf(payDtFormat.getId())); 
					request.setPayDtFormatRequest(req); 
				} else request.setClearPayDtFormatId(true); 

		     	// PAY PERIOD ID
				fSpec.setPpdErRefCol(Boolean.valueOf(payPrdId.isSelected())); 
				fSpec.setPpdErRefColIndex(Integer.valueOf(payPrdIdIdx.getText() != null && !payPrdIdIdx.getText().isEmpty() ? payPrdIdIdx.getText() : "0")); 
				
				// AMOUNT
				fSpec.setAmtCol(Boolean.valueOf(amt.isSelected())); 
				fSpec.setAmtColIndex(Integer.valueOf(amtIdx.getText() != null && !amtIdx.getText().isEmpty() ? amtIdx.getText() : "0")); 

				// PLAN ID
				fSpec.setPlanRefCol(Boolean.valueOf(covPlan.isSelected())); 
				fSpec.setPlanRefColIndex(Integer.valueOf(covPlanIdx.getText() != null && !covPlanIdx.getText().isEmpty() ? covPlanIdx.getText() : "0")); 
				if(covPlanParse.getId() != null && Utils.isInt(covPlanParse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(covPlanParse.getId())); 
					request.setPlanRefParsePatternRequest(req); 
				} else request.setClearPlanRefParsePatternId(true);

		     	// DEDUCTION CUSTOM FIELD 1 
				fSpec.setDeductionCfld1Name(deductCF1.getText() != null && !deductCF1.getText().isEmpty() ? deductCF1.getText() : ""); 
				fSpec.setDeductionCfld1Col(Boolean.valueOf(dedCF1.isSelected())); 
				fSpec.setDeductionCfld1ColIndex(Integer.valueOf(dedCF1Idx.getText() != null && !dedCF1Idx.getText().isEmpty() ? dedCF1Idx.getText() : "0")); 
				if(dedCF1Parse.getId() != null && Utils.isInt(dedCF1Parse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(dedCF1Parse.getId())); 
					request.setDeductionCfld1ParsePatternRequest(req); 
				} else request.setClearDeductionCfld1ParsePatternId(true); 

		     	// DEDUCTION CUSTOM FIELD 2 
				fSpec.setDeductionCfld2Name(deductCF2.getText() != null && !deductCF2.getText().isEmpty() ? deductCF2.getText() : ""); 
				fSpec.setDeductionCfld2Col(Boolean.valueOf(dedCF2.isSelected())); 
				fSpec.setDeductionCfld2ColIndex(Integer.valueOf(dedCF2Idx.getText() != null && !dedCF2Idx.getText().isEmpty() ? dedCF2Idx.getText() : "0")); 
				if(dedCF2Parse.getId() != null && Utils.isInt(dedCF2Parse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(dedCF2Parse.getId())); 
					request.setDeductionCfld2ParsePatternRequest(req); 
				} else request.setClearDeductionCfld2ParsePatternId(true); 

		     	// DEDUCTION CUSTOM FIELD 3 
				fSpec.setDeductionCfld3Name(deductCF3.getText() != null && !deductCF3.getText().isEmpty() ? deductCF3.getText() : ""); 
				fSpec.setDeductionCfld3Col(Boolean.valueOf(dedCF3.isSelected())); 
				fSpec.setDeductionCfld3ColIndex(Integer.valueOf(dedCF3Idx.getText() != null && !dedCF3Idx.getText().isEmpty() ? dedCF3Idx.getText() : "0")); 

		     	// CUSTOM FIELD 1 
				fSpec.setCfld1Name(eeCF1.getText() != null && !eeCF1.getText().isEmpty() ? eeCF1.getText() : ""); 
				fSpec.setCfld1Col(Boolean.valueOf(emplCF1.isSelected())); 
				fSpec.setCfld1ColIndex(Integer.valueOf(emplCF1Idx.getText() != null && !emplCF1Idx.getText().isEmpty() ? emplCF1Idx.getText() : "0")); 
				if(emplCF1Parse.getId() != null && Utils.isInt(emplCF1Parse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(emplCF1Parse.getId())); 
					request.setCfld1ParsePatternRequest(req); 
				} else request.setClearCfld1ParsePatternId(true); 

		     	// CUSTOM FIELD 2 
				fSpec.setCfld2Name(eeCF2.getText() != null && !eeCF2.getText().isEmpty() ? eeCF2.getText() : ""); 
				fSpec.setCfld2Col(Boolean.valueOf(emplCF2.isSelected())); 
				fSpec.setCfld2ColIndex(Integer.valueOf(emplCF2Idx.getText() != null && !emplCF2Idx.getText().isEmpty() ? emplCF2Idx.getText() : "0")); 
				if(emplCF2Parse.getId() != null && Utils.isInt(emplCF2Parse.getId()))  
				{ 
					PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
					req.setId(Long.valueOf(emplCF2Parse.getId())); 
					request.setCfld2ParsePatternRequest(req); 
				} else request.setClearCfld2ParsePatternId(true); 

		     	// CUSTOM FIELD 3 
				fSpec.setCfld3Name(eeCF3.getText() != null && !eeCF3.getText().isEmpty() ? eeCF3.getText() : ""); 
				fSpec.setCfld3Col(Boolean.valueOf(emplCF3.isSelected())); 
				fSpec.setCfld3ColIndex(Integer.valueOf(emplCF3Idx.getText() != null && !emplCF3Idx.getText().isEmpty() ? emplCF3Idx.getText() : "0")); 

		     	// CUSTOM FIELD 4 
				fSpec.setCfld4Name(eeCF4.getText() != null && !eeCF4.getText().isEmpty() ? eeCF4.getText() : ""); 
				fSpec.setCfld4Col(Boolean.valueOf(emplCF4.isSelected())); 
				fSpec.setCfld4ColIndex(Integer.valueOf(emplCF4Idx.getText() != null && !emplCF4Idx.getText().isEmpty() ? emplCF4Idx.getText() : "0")); 

		     	// CUSTOM FIELD 5 
				fSpec.setCfld5Name(eeCF5.getText() != null && !eeCF5.getText().isEmpty() ? eeCF5.getText() : ""); 
				fSpec.setCfld5Col(Boolean.valueOf(emplCF5.isSelected())); 
				fSpec.setCfld5ColIndex(Integer.valueOf(emplCF5Idx.getText() != null && !emplCF5Idx.getText().isEmpty() ? emplCF5Idx.getText() : "0")); 

				// set the request entity
				request.setEntity(fSpec);
				request.setId(fSpec.getId()); 

				// update the server
				try {
					fSpec = AdminPersistenceManager.getInstance().addOrUpdate(request);
				} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
			    catch (Exception e) { DataManager.i().logGenericException(e); }

				EtcAdmin.i().setProgress(1);
				EtcAdmin.i().setProgress(0);
			} 
		} 

		@FXML 
		private void onAddIgnoreRow(ActionEvent event) 
		{ 
			switch(fileType) 
			{ 
				case DEDUCTION: 
		        	DataManager.i().mDynamicDeductionFileIgnoreRowSpecification = null; 
					break; 
				default: 
					return; 
			}	 
			viewIgnoreRow(); 
		}	 
	 
		private void viewIgnoreRow()  
		{ 
	        try { 
	            // load the fxml 
		        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/mapper/ViewMapperDeductionRowIgnore.fxml")); 
				Parent ControllerNode = loader.load(); 
		        ViewMapperDeductionRowIgnoreController ignoreController = (ViewMapperDeductionRowIgnoreController) loader.getController(); 
	 
		        ignoreController.load(); 
		        Stage stage = new Stage(); 
		        stage.initModality(Modality.APPLICATION_MODAL); 
		        stage.initStyle(StageStyle.UNDECORATED); 
		        stage.setScene(new Scene(ControllerNode));   
		        EtcAdmin.i().positionStageCenter(stage);
		        stage.showAndWait(); 
		         
		        if(ignoreController.changesMade == true) 
		        	loadMapperData(); 
			} catch(IOException e) { DataManager.i().log(Level.SEVERE, e); }        		 
		    catch (Exception e) { DataManager.i().logGenericException(e); }
		} 
	 
		@FXML 
		private void onRemoveIgnoreSpec(ActionEvent event)  
		{ 
			//verify that they want to remove the spec 
			Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION); 
		    confirmAlert.setTitle("Remove Ignore Row Specification"); 
		    confirmAlert.setContentText("Are You Sure You Want to remove the selected Ignore row specification?"); 
		    EtcAdmin.i().positionAlertCenter(confirmAlert);
		    Optional<ButtonType> result = confirmAlert.showAndWait(); 
		    if((result.isPresent()) &&(result.get() != ButtonType.OK)) { return; } 
		}	 
	 
		@FXML 
		private void onAddPayPeriodRules(ActionEvent event) 
		{ 
			//reset the current rule, if any 
			DataManager.i().mDynamicPayFilePayPeriodRule = null; 
			viewPayPeriodRules(); 
		}	 
	 
		private void viewPayPeriodRules()  
		{ 
	        try { 
	            // load the fxml 
		        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicPayFilePayPeriodRule.fxml")); 
				Parent ControllerNode = loader.load(); 
		        ViewDynamicPayFilePayPeriodRuleController ruleController = (ViewDynamicPayFilePayPeriodRuleController) loader.getController(); 
	 
		        ruleController.load(); 
		        Stage stage = new Stage(); 
		        stage.initModality(Modality.APPLICATION_MODAL); 
		        stage.initStyle(StageStyle.UNDECORATED); 
		        stage.setScene(new Scene(ControllerNode));   
		        EtcAdmin.i().positionStageCenter(stage);
		        stage.showAndWait(); 
	 
		        if(ruleController.changesMade == true) 
		        	loadMapperData(); 
			} catch(IOException e) { DataManager.i().log(Level.SEVERE, e); }        		 
		    catch (Exception e) { DataManager.i().logGenericException(e); }
		} 
	 
		@FXML 
		private void onAddAdditionalHours(ActionEvent event)  
		{ 
			DataManager.i().mDynamicPayFileAdditionalHoursSpecification = null; 
			viewAdditionalHours(); 
		}	 
	 
		private void viewAdditionalHours()  
		{ 
	        try { 
	        	// load the fxml 
		        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationAdditionalHours.fxml")); 
				Parent ControllerNode = loader.load(); 
				ViewDynamicFileSpecificationAdditionalHoursController refController = (ViewDynamicFileSpecificationAdditionalHoursController) loader.getController(); 
		        refController.load(); 
	 
		        Stage stage = new Stage(); 
		        stage.initModality(Modality.APPLICATION_MODAL); 
		        stage.initStyle(StageStyle.UNDECORATED); 
		        stage.setScene(new Scene(ControllerNode));   
		        EtcAdmin.i().positionStageCenter(stage);
		        stage.showAndWait(); 
	 
		        if(refController.changesMade == true) 
		        	loadMapperData(); 
			} catch(IOException e) { DataManager.i().log(Level.SEVERE, e); }        		 
		    catch (Exception e) { DataManager.i().logGenericException(e); }
		} 
	 
		private void loadEmployerRefForm()  
		{ 
			try { 
	        	// load the fxml 
		        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationRef.fxml")); 
				Parent ControllerNode = loader.load(); 
		        ViewDynamicFileSpecificationRefController refController = (ViewDynamicFileSpecificationRefController) loader.getController(); 
		        refController.setFileType(fileType); 
		        refController.load(); 
	 
		        Stage stage = new Stage(); 
		        stage.initModality(Modality.APPLICATION_MODAL); 
		        stage.initStyle(StageStyle.UNDECORATED); 
		        stage.setScene(new Scene(ControllerNode));   
		        stage.onCloseRequestProperty().setValue(e -> setEmpRefCount(getDeductionFileEmployerRefCount())); 
		        stage.onHiddenProperty().setValue(e -> setEmpRefCount(getDeductionFileEmployerRefCount())); 
		        EtcAdmin.i().positionStageCenter(stage);
		        stage.showAndWait(); 
	 
		        if(refController.changesMade == true) 
					setEmpRefCount(getDeductionFileEmployerRefCount()); 
			} catch(IOException e) { 
				 DataManager.i().log(Level.SEVERE, e); 
				Utils.alertUser("Reference Popup Load Error", e.getMessage());
			}         
		    catch (Exception e) { DataManager.i().logGenericException(e); }
		} 
	 
		public void loadPatternPopup(MapperField fld)  
		{ 
	        try {

	        	DataManager.i().mPipelineParsePattern = fld.getParsePattern();
	        	if(fld.getParsePatternId() != null)
	        	{
	    			PipelineParsePatternRequest request = new PipelineParsePatternRequest();
	    			request.setId(fld.getParsePatternId());
	    			DataManager.i().mPipelineParsePattern = AdminPersistenceManager.getInstance().get(request);
	        	}

	        	// load the fxml 
	        	FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewParsePatternRef.fxml")); 
	  			Parent ControllerNode = loader.load(); 
	 
	  	        Stage stage = new Stage(); 
	  	        stage.initModality(Modality.APPLICATION_MODAL); 
	  	        stage.initStyle(StageStyle.UNDECORATED); 
	  	        stage.setScene(new Scene(ControllerNode)); 
	  	        stage.onCloseRequestProperty().setValue(e -> updateFileData()); 
	  	        stage.onHiddenProperty().setValue(e -> updateFileData()); 
		        EtcAdmin.i().positionStageCenter(stage);
	  	        stage.showAndWait(); 

	  	        if(DataManager.i().mPipelineParsePattern != null)
	  	        {
	  	  	        fld.setParsePatternId(DataManager.i().mPipelineParsePattern.getId()); 
	  	        } else { fld.setParsePatternId(null); }

	        } catch(IOException | CoreException e) { DataManager.i().log(Level.SEVERE, e); }        		 
		    catch (Exception e) { DataManager.i().logGenericException(e); }
		} 
	 
		public void loadFormatPopup(MapperField fld)  
		{ 
	        try { 
	        	 
	        	DataManager.i().mPipelineParseDateFormat = fld.getDateFormat(); 
	           	if(fld.getDateFormatId() != null)
	        	{
	    			PipelineParseDateFormatRequest request = new PipelineParseDateFormatRequest();
	    			request.setId(fld.getDateFormatId());
	    			DataManager.i().mPipelineParseDateFormat = AdminPersistenceManager.getInstance().get(request);
	        	}
	  
	        	// load the fxml 
	 	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDateFormatRef.fxml")); 
	  			Parent ControllerNode = loader.load(); 
	 
	  	        Stage stage = new Stage(); 
	  	        stage.initModality(Modality.APPLICATION_MODAL); 
	  	        stage.initStyle(StageStyle.UNDECORATED); 
	  	        stage.onCloseRequestProperty().setValue(e -> updateFileData()); 
	  	        stage.onHiddenProperty().setValue(e -> updateFileData());
	  	        stage.setScene(new Scene(ControllerNode));   
		        EtcAdmin.i().positionStageCenter(stage);
	  	        stage.showAndWait(); 

	  	        if(DataManager.i().mPipelineParseDateFormat != null)
	  	        {
	  	  	        fld.setDateFormatId(DataManager.i().mPipelineParseDateFormat.getId()); 
	  	        } else { fld.setDateFormatId(null); }
	 
	  		} catch(IOException | CoreException e) { DataManager.i().log(Level.SEVERE, e); }        		 
		    catch (Exception e) { DataManager.i().logGenericException(e); }
		} 
		 
		/* 
		 * setParseButtonValue sets the Pattern ID on the button, visual 
		 * to the user. 
		 */ 
		public void setParseButtonValue(ActionEvent event) 
		{ 
			Button btn = null; 
			 
			if(event.getSource() instanceof Button) 
			{ 
				btn = Button.class.cast(event.getSource()); 
	 
				for(MapperField f : mapperFields.values()) 
				{ 
					if(btn.equals(f.getBtnParsePtrn())) 
					{   			 
						if(f.getParsePatternId() != null) 
						{ 
							f.setTitle(f.getTitle().replace(" (P:" + f.getParsePatternId().toString() + ")", "")); 
						} 
						loadPatternPopup(f); 
						if(f.getParsePatternId() != null) 
						{ 
							f.getBtnParsePtrn().setText("Pattern: " + f.getParsePatternId().toString()); 
							btn.setFont(Font.font("Veranda", 12.0)); 
							btn.setId(f.getParsePatternId().toString()); 
							deregisterCell(Integer.valueOf(f.getTxtFldIndex().getText().toString()), f); 
							refreshPatterns(f, Integer.valueOf(f.getTxtFldIndex().getText().toString()), f.getParsePatternId(), f.getDateFormatId()); 
							registerCell(Integer.valueOf(f.getTxtFldIndex().getText().toString()), f, f.getParsePatternId(), f.getDateFormatId()); 
						} else { 
							setPatternValue(f); 
							deregisterCell(Integer.valueOf(f.getTxtFldIndex().getText().toString()), f); 
							registerCell(Integer.valueOf(f.getTxtFldIndex().getText().toString()), f, f.getParsePatternId(), f.getDateFormatId()); 
						} 
					} 
				} 
			} 
		} 
	 
		/* 
		 * setFormatButtonValue sets the Format ID on the button, visual 
		 * to the user. 
		 */ 
		public void setFormatButtonValue(ActionEvent event) 
		{ 
			Button btn1 = null; 
	 
			if(event.getSource() instanceof Button) 
			{
				btn1 = Button.class.cast(event.getSource()); 
	 
				for(MapperField f : mapperFields.values()) 
				{ 
					if(btn1.equals(f.getBtnDtFormat())) 
					{ 
						if(f.getDateFormatId() != null) 
						{
							f.setTitle(f.getTitle().replace(" (F:" + f.getDateFormatId().toString() + ")", "")); 
						}		 
						loadFormatPopup(f); 
						if(f.getDateFormatId() != null) 
						{ 
							f.getBtnDtFormat().setText("Format: " + f.getDateFormatId().toString()); 
							btn1.setFont(Font.font("Veranda", 12.0)); 
							btn1.setId(f.getDateFormatId().toString()); 
							deregisterCell(Integer.valueOf(f.getTxtFldIndex().getText().toString()), f); 
							refreshPatterns(f, Integer.valueOf(f.getTxtFldIndex().getText().toString()), f.getParsePatternId(), f.getDateFormatId()); 
							registerCell(Integer.valueOf(f.getTxtFldIndex().getText().toString()), f, f.getParsePatternId(), f.getDateFormatId()); 
						} else { 
							setPatternValue(f); 
							deregisterCell(Integer.valueOf(f.getTxtFldIndex().getText().toString()), f); 
							registerCell(Integer.valueOf(f.getTxtFldIndex().getText().toString()), f, f.getParsePatternId(), f.getDateFormatId()); 
						} 
					} 
				} 
			} 
		} 
	 
		public void setPatternValue(MapperField fld) 
		{		 
			if(emplSsnParse.getText().contains("+ Pattern")){ emplSsnParse.setId("emplSsnParse"); } 
			if(emplrEmpIdParse.getText().contains("+ Pattern")){ emplrEmpIdParse.setId("emplrEmpIdParse"); } 
			if(empFstNmParse.getText().contains("+ Pattern")){ empFstNmParse.setId("empFstNmParse"); } 
			if(emplMNmParse.getText().contains("+ Pattern")){ emplMNmParse.setId("emplMNmParse"); } 
			if(emplLstNmParse.getText().contains("+ Pattern")){ emplLstNmParse.setId("emplLstNmParse"); } 
			if(emplAdLn1Parse.getText().contains("+ Pattern")){ emplAdLn1Parse.setId("emplAdLn1Parse"); } 
			if(emplCityParse.getText().contains("+ Pattern")){ emplCityParse.setId("emplCityParse"); } 
			if(emplStateParse.getText().contains("+ Pattern")){ emplStateParse.setId("emplStateParse"); } 
			if(emplZipParse.getText().contains("+ Pattern")){ emplZipParse.setId("emplZipParse"); } 
			if(empDOBFormat.getText().contains("+ Format")){ empDOBFormat.setId("empDOBFormat"); } 
			if(payStrtDtParse.getText().contains("+ Pattern")){ payStrtDtParse.setId("payStrtDtParse"); } 
			if(payStrtDtFormat.getText().contains("+ Format")){ payStrtDtFormat.setId("payStrtDtFormat"); } 
			if(payEndDtParse.getText().contains("+ Pattern")){ payEndDtParse.setId("payEndDtParse"); } 
			if(payEndDtFormat.getText().contains("+ Format")){ payEndDtFormat.setId("payEndDtFormat"); }
			if(payDtParse.getText().contains("+ Parse")){ payDtParse.setId("payDtParse"); }
			if(payDtFormat.getText().contains("+ Format")){ payDtFormat.setId("payDtFormat"); }
			if(covPlanParse.getText().contains("+ Pattern")){ covPlanParse.setId("covPlanParse"); } 
			if(dedCF1Parse.getText().contains("+ Pattern")){ dedCF1Parse.setId("dedCF1Parse"); } 
			if(dedCF2Parse.getText().contains("+ Pattern")){ dedCF2Parse.setId("dedCF2Parse"); } 
			if(emplCF1Parse.getText().contains("+ Pattern")){ emplCF1Parse.setId("emplCF1Parse"); } 
			if(emplCF2Parse.getText().contains("+ Pattern")){ emplCF2Parse.setId("emplCF2Parse"); } 
		} 
		 
		public void refreshPatterns(MapperField fld, Integer idx, Long ptn, Long fmt) 
		{	 
			Integer w = fld.getCellWidth(); 
			Double dW = null; 
	 
			dW = spreadsheet.getColumnConstraints().get(idx + 1).getMinWidth(); 
			 
			if(ptn != null && ptn >= 10) 
			{ 
				if(fld.getTitle().contains(ptn.toString())) 
				{ 
					fld.setTitle(fld.getTitle()); 
				} else { 
					fld.setTitle(fld.getTitle() + " (P:" + ptn.toString() + ")"); 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(w + 35); 
				} 
	 
				if(w >= dW) 
				{ 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(w + 35); 
				} else { 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(dW); 
				} 
			} 
	 
			if(ptn != null && ptn <= 9) 
			{ 
				if(fld.getTitle().contains(ptn.toString())) 
				{ 
					fld.setTitle(fld.getTitle()); 
				} else { 
					fld.setTitle(fld.getTitle() + " (P:" + ptn.toString() + ")"); 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(w + 25); 
				} 
	 
				if(w >= dW) 
				{ 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(w + 25); 
				} else { 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(dW); 
				} 
			}  
	 
			if(fmt != null) 
			{  
				if(fld.getTitle().contains(fmt.toString())) 
				{ 
					fld.setTitle(fld.getTitle()); 
				} else { 
					fld.setTitle(fld.getTitle() + " (F:" + fmt.toString() + ")"); 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(w + 25); 
				} 
	 
				if(w >= dW) 
				{ 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(w + 25); 
				} else { 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(dW); 
				} 
			} 
			 
			if(ptn != null && fmt != null) 
			{ 
				spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(w + 50); 
				 
				if(w >= dW) 
				{ 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(w + 50); 
				} else { 
					spreadsheet.getColumnConstraints().get(idx + 1).setMinWidth(dW); 
				} 
			} 
		} 
	 
	    public void setEmpRefCount(int count) 
	    { 
	    	empRefLabel.setFont(Font.font("Veranda", 12.0)); 
	        empRefLabel.setText("References: " + String.valueOf(count)); 
	    } 
	 
	    /* 
	     * clearForm clears all of the mapper header rows and data elements  
	     * to default, empty column fields, unchecked boxes, and uncolored everything. 
	     */ 
		private void clearForm() 
		{ 
			for(MapperField mf : mapperFields.values()) 
			{ 
				if(mf != null) 
				{ 
					mf.getCkEnabled().setSelected(false); 
					emprId.setSelected(false); 
					emplSsn.setSelected(false); 
					emplrEmpId.setSelected(false); 
					empFstNm.setSelected(false);  
					emplMNm.setSelected(false); 
					emplLstNm.setSelected(false); 
					emplStNm.setSelected(false); 
					emplAdLn1.setSelected(false); 
					emplAdLn2.setSelected(false); 
					emplCity.setSelected(false); 
					emplState.setSelected(false); 
					emplZip.setSelected(false); 
					empDOB.setSelected(false); 
					payStrtDt.setSelected(false); 
					payEndDt.setSelected(false); 
					payDt.setSelected(false); 
					payPrdId.setSelected(false); 
					amt.setSelected(false); 
					covPlan.setSelected(false); 
					dedCF1.setSelected(false); 
					dedCF2.setSelected(false); 
					dedCF3.setSelected(false); 
					emplCF1.setSelected(false); 
					emplCF2.setSelected(false); 
					emplCF3.setSelected(false); 
					emplCF4.setSelected(false); 
					emplCF5.setSelected(false);
					
					dfcovNameLabel.setText(""); 
					dfcovTabIndexLabel.setText("0"); 
					dfcovHeaderRowIndexLabel.setText("0"); 
					dfplanCheck.setSelected(false); 
					dfpayPeriodCheck.setSelected(false); 
					dfcovCreateEmployeeCheck.setSelected(false); 
					dfcovSkipLastRowCheck.setSelected(false); 
					dfcovLockedCheck.setSelected(false); 
	 
					mf.getTxtFldIndex().setText(""); 
					deductCF1.setText(""); 
					deductCF2.setText(""); 
					deductCF3.setText(""); 
					eeCF1.setText(""); 
					eeCF2.setText(""); 
					eeCF3.setText(""); 
					eeCF4.setText(""); 
					eeCF5.setText(""); 
					emprRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
					mf.getStkPane().setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY)));
					
					for(MapperCell mc : mapperCells.values()) 
					{ 
						if(mc != null) 
						{ 
							mc.getNode().setStyle(Color.WHITE.toString()); 
							spreadsheet.getColumnConstraints().get(mc.getIdx()).setMinWidth(50); 
							deregisterCell(mc.getIdx(), mf); 
							refreshPatterns(mf, mc.getIdx(), mf.getParsePatternId(), mf.getDateFormatId()); 
							checkBoxChanged(mf); 
						} 
					} 
				}		 
			} 
		} 
		 
		@FXML 
		private void headerCheckBoxSelect(ActionEvent event) 
		{  
			DynamicDeductionFileSpecification fSpec = DataManager.i().mDynamicDeductionFileSpecification; 
			 
			if(dfplanCheck.isSelected() == true) { fSpec.setCreatePlan(true); } 
			else { fSpec.setCreatePlan(false); } 
			 
			if(dfpayPeriodCheck.isSelected() == true) { fSpec.setCreatePayPeriod(true); } 
			else { fSpec.setCreatePayPeriod(false); } 
	 
			if(dfcovCreateEmployeeCheck.isSelected() == true) { fSpec.setCreateEmployee(true); } 
			else { fSpec.setCreateEmployee(false); } 
	 
			if(dfcovSkipLastRowCheck.isSelected() == true) { fSpec.setSkipLastRow(true); } 
			else { fSpec.setSkipLastRow(false); } 
	 
			if(dfcovLockedCheck.isSelected() == true) { fSpec.setLocked(true); } 
			else { fSpec.setLocked(false); } 
		}
		 
		@FXML 
		private void onClearForm(ActionEvent event) { clearForm(); }	 
	 
		//Reference Button Action Events 
		@FXML 
		private void onEmprRefClick(ActionEvent event) { loadEmployerRefForm(); }	 
	 
		//Parse and Format Button Action Events 
		@FXML 
		private void onCovPlanParse(ActionEvent event) { setParseButtonValue(event); } 

		@FXML 
		private void onEmpFstNmParse(ActionEvent event) { setParseButtonValue(event); }	 
		 
		@FXML 
		private void onEmplCityParse(ActionEvent event) { setParseButtonValue(event); } 
		 
		@FXML 
		private void onEmpDOBFormat(ActionEvent event) { setFormatButtonValue(event); } 
		 
		@FXML 
		private void onEmplSsnParse(ActionEvent event) { setParseButtonValue(event); } 
		 
		@FXML 
		private void onEmplrEmpIdParse(ActionEvent event) { setParseButtonValue(event); } 
		 
		@FXML 
		private void onEmplMNmParse(ActionEvent event) { setParseButtonValue(event); } 
	 
		@FXML 
		private void onEmplAdLn1Parse(ActionEvent event) { setParseButtonValue(event); } 
	 
		@FXML 
		private void onEmplStateParse(ActionEvent event) { setParseButtonValue(event); } 
		 
		@FXML 
		private void onEmplLstNmParse(ActionEvent event) { setParseButtonValue(event); } 
	 
		@FXML 
		private void onEmplZipParse(ActionEvent event) { setParseButtonValue(event); } 
		
		@FXML 
		private void onPayStrtDtParse(ActionEvent event) { setParseButtonValue(event); } 
	 
		@FXML 
		private void onPayStrtDtFormat(ActionEvent event) { setFormatButtonValue(event); } 
		
		@FXML 
		private void onPayEndDtParse(ActionEvent event) { setParseButtonValue(event); } 
	 
		@FXML 
		private void onPayEndDtFormat(ActionEvent event) { setFormatButtonValue(event); } 
		
		@FXML 
		private void onPayDtParse(ActionEvent event) { setParseButtonValue(event); } 
	 
		@FXML 
		private void onPayDtFormat(ActionEvent event) { setFormatButtonValue(event); } 
		 
		@FXML 
		private void onDedCF1Parse(ActionEvent event) { setParseButtonValue(event); } 
	 
		@FXML 
		private void onDedCF2Parse(ActionEvent event) { setParseButtonValue(event); } 
	 
		@FXML 
		private void onEmplCF1Parse(ActionEvent event) { setParseButtonValue(event); } 
	 
		@FXML 
		private void onEmplCF2Parse(ActionEvent event) { setParseButtonValue(event); }
	 
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
	 
	             lblCol2.setMinWidth(140); 
	             lblCol2.setMaxWidth(140); 
	             lblCol2.setPrefWidth(140); 
	             HBox.setHgrow(lblCol2, Priority.ALWAYS); 
	 
	             this.getChildren().addAll(lblCol2); 
	        } 
	 
	        public DynamicDeductionFileIgnoreRowSpecification getDedSpec()  
	        { 
	        	return dedSpec;  
	        } 
	    } 
	}