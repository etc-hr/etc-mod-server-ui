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
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationCoverageClassRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationDepartmentRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationGenderTypeRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationLocationRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationPayCodeTypeRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationUnionTypeRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicPayFilePayPeriodRuleController;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileCoverageGroupReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileDepartmentReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileGenderReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileLocationReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFilePayCodeReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileUnionTypeReference;
import com.etc.corvetto.ems.pipeline.rqs.PipelineParseDateFormatRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineParsePatternRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileCoverageGroupReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileDepartmentReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileEmployerReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileGenderReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileIgnoreRowSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileLocationReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFilePayCodeReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileUnionTypeReferenceRequest;
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
 
public class ViewMapperEmployeeFormController  
{ 
	@FXML 
	private ListView<HBoxIgnoreRowCell> dfcovIgnoreRowSpecsListView; 
 
	@FXML 
	private Label emprIdLbl; 
 
	@FXML 
	private StackPane emprIdSkPn; 
 
	@FXML 
	private StackPane empCovClsSkPn; 
 
	@FXML 
	private StackPane empDeptSkPn; 
 
	@FXML 
	private StackPane empLocSkPn; 
 
	@FXML 
	private StackPane empFstNmSkPn; 
 
	@FXML 
	private StackPane empDOBSkPn; 
 
	@FXML 
	private StackPane emplStNmSkPn; 
 
	@FXML 
	private StackPane emplCitySkPn; 
	 
	@FXML 
	private StackPane emplHrDtSkPn; 
	 
	@FXML 
	private StackPane emplRHrDtSkPn; 
 
	@FXML 
	private StackPane emplJobSkPn; 
	 
	@FXML 
	private StackPane emplMNmSkPn; 
	 
	@FXML 
	private StackPane emplIdSkPn; 
	 
	@FXML 
	private StackPane emplPhnSkPn; 
	 
	@FXML 
	private StackPane emplAdLn1SkPn; 
 
	@FXML 
	private StackPane emplStateSkPn; 
	 
	@FXML 
	private StackPane empPayCodeSkPn; 
	 
	@FXML 
	private StackPane empUnTypSkPn; 
	 
	@FXML 
	private StackPane emplGendSkPn; 
	 
	@FXML 
	private StackPane emplLstNmSkPn; 
	 
	@FXML 
	private StackPane emplTrmDtSkPn; 
	 
	@FXML 
	private StackPane emplEmlSkPn; 
	 
	@FXML 
	private StackPane emplAdLn2SkPn; 
	 
	@FXML 
	private StackPane emplZipSkPn; 
	 
	@FXML 
	private StackPane emplrEmpIdSkPn; 
	 
	@FXML 
	private StackPane emplSsnSkPn; 
	 
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
	private TextField dfcovNameLabel; 
	 
	@FXML 
	private TextField dfcovDescriptionLabel; 
	 
	@FXML 
	private TextField dfcovTabIndexLabel; 
	 
	@FXML 
	private TextField dfcovHeaderRowIndexLabel; 
	 
	@FXML 
	private CheckBox dfcovCreateEmployeeCheck; 
	 
	@FXML 
	private CheckBox dfcovSkipLastRowCheck; 
	 
	@FXML 
	private CheckBox emprId; 
	 
	@FXML 
	private CheckBox empCovCls;
	 
	@FXML 
	private CheckBox empDept; 
	 
	@FXML 
	private CheckBox empLoc; 
	 
	@FXML 
	private CheckBox empFstNm; 
	 
	@FXML 
	private CheckBox empDOB; 
	 
	@FXML 
	private CheckBox emplStNm; 
	 
	@FXML 
	private CheckBox emplCity; 
	 
	@FXML 
	private CheckBox emplHrDt; 
	 
	@FXML 
	private CheckBox emplRHrDt; 
	 
	@FXML 
	private CheckBox emplJob; 
	 
	@FXML 
	private CheckBox emplMNm; 
	 
	@FXML 
	private CheckBox emplId; 
	 
	@FXML 
	private CheckBox emplPhn; 
	 
	@FXML 
	private CheckBox emplAdLn1; 
	 
	@FXML 
	private CheckBox emplState; 
 
	@FXML 
	private CheckBox empPayCode; 
 
	@FXML 
	private CheckBox empUnTyp; 
 
	@FXML 
	private CheckBox emplGend; 
	 
	@FXML 
	private CheckBox emplLstNm; 
	 
	@FXML 
	private CheckBox emplTrmDt; 
	 
	@FXML 
	private CheckBox emplEml; 
	 
	@FXML 
	private CheckBox emplAdLn2; 
	 
	@FXML 
	private CheckBox emplZip; 
	 
	@FXML 
	private CheckBox emplrEmpId; 
	 
	@FXML 
	private CheckBox emplSsn; 
	 
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
	private TextField emprIdIdx; 
	 
	@FXML 
	private TextField empCovClsIdx; 
	 
	@FXML 
	private TextField empDeptIdx; 
	 
	@FXML 
	private TextField empLocIdx; 
	 
	@FXML 
	private TextField empFstNmIdx; 
	 
	@FXML 
	private TextField empDOBIdx; 
	 
	@FXML 
	private TextField emplStNmIdx; 
	 
	@FXML 
	private TextField emplCityIdx; 
	 
	@FXML 
	private TextField emplHrDtIdx; 
	 
	@FXML 
	private TextField emplRHrDtIdx; 
	 
	@FXML 
	private TextField emplJobIdx; 
	 
	@FXML 
	private TextField emplMNmIdx; 
	 
	@FXML 
	private TextField emplIdIdx; 
	 
	@FXML 
	private TextField emplPhnIdx; 
	 
	@FXML 
	private TextField emplAdLn1Idx; 
	 
	@FXML 
	private TextField emplStateIdx; 
	 
	@FXML 
	private TextField empPayCodeIdx; 
	 
	@FXML 
	private TextField empUnTypIdx; 
	 
	@FXML 
	private TextField emplGendIdx; 
	 
	@FXML 
	private TextField emplLstNmIdx; 
	 
	@FXML 
	private TextField emplTrmDtIdx; 
	 
	@FXML 
	private TextField emplEmlIdx; 
	 
	@FXML 
	private TextField emplAdLn2Idx; 
	 
	@FXML 
	private TextField emplZipIdx; 
	 
	@FXML 
	private TextField emplrEmpIdIdx; 
	 
	@FXML 
	private TextField emplSsnIdx; 
	 
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
	private Button ccRefButton; 
	 
	@FXML 
	private Button depRefButton; 
	 
	@FXML 
	private Button locRefButton; 
	 
	@FXML 
	private Button payRefButton; 
	 
	@FXML 
	private Button unionRefButton; 
	 
	@FXML 
	private Button genRefButton; 
	 
	@FXML 
	private Button empFstNmParse; 
	 
	@FXML 
	private Button empDOBFormat; 
	 
	@FXML 
	private Button emplStNmParse; 
	 
	@FXML 
	private Button emplCityParse; 
	 
	@FXML 
	private Button emplHrDtFormat; 
	 
	@FXML 
	private Button emplRHrDtFormat; 
	 
	@FXML 
	private Button emplMNmParse; 
	 
	@FXML 
	private Button emplAdLn1Parse; 
	 
	@FXML 
	private Button emplStateParse; 
	 
	@FXML 
	private Button emplLstNmParse; 
	 
	@FXML 
	private Button emplTrmDtFormat; 
	 
	@FXML 
	private Button emplZipParse; 
	 
	@FXML 
	private Button emplrEmpIdParse; 
	 
	@FXML 
	private Button emplSsnParse; 
	 
	@FXML 
	private Button emplCF1Parse; 
	 
	@FXML 
	private Button emplCF2Parse; 
	 
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
 
	@FXML 
	private Label covRefLabel; 
 
	@FXML 
	private Label deptRefLabel; 
 
	@FXML 
	private Label locRefLabel; 
 
	@FXML 
	private Label payRefLabel; 
 
	@FXML 
	private Label unionRefLabel; 
 
	@FXML 
	private Label gendRefLabel; 
 
	// read only mode for selected mapper files 
	public boolean readOnly = false; 
	public PipelineFileType fileType; 
 
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
			dfcovName.setText("Mapper for ".concat(DataManager.i().mPipelineSpecification.getName()).concat(",  Id: ").concat(String.valueOf(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()))); 
 
		//set the file type 
		fileType = DataManager.i().mPipelineChannel.getType(); 
		initControls(); 
		loadMapperData(); 
	} 
 
	private void initControls()  
	{ 
		//disable the check boxes 
		dfcovCreateEmployeeCheck.setDisable(false);		 
 
		//TODO: REMOVE THIS SINCE WE'RE SPLITTING FORMS 
		//enable the addspec button only for the applicable types 
		dfcovPayAdditionalHoursButton.setVisible(false); 
		switch(fileType) 
		{ 
			case EMPLOYEE: 
				dfcovAddIgnoreSpecButton.setDisable(false); 
				dfcovPayAdditionalHoursButton.setVisible(true); 
				break; 
			default: 
				dfcovAddIgnoreSpecButton.setDisable(true); 
				break; 
		}	 
 
		//IGNOREROWSPECS 
		dfcovIgnoreRowSpecsListView.setOnMouseClicked(mouseClickedEvent ->  
		{ 
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
            { 
        		switch(fileType) 
        		{ 
	        		case EMPLOYEE: 
	                	DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification = dfcovIgnoreRowSpecsListView.getSelectionModel().getSelectedItem().getEmpSpec(); 
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
 
		// COVERAGE CLASS REFERENCE 
		ccRefButton.setOnMouseClicked(mouseClickedEvent ->  
		{ 
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
            { 
            	loadCoverageClassRefForm(); 
            } 
        }); 
 
		// DEPARTMENT REFERENCE 
		depRefButton.setOnMouseClicked(mouseClickedEvent ->  
		{ 
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
            { 
            	loadDepartmentRefForm(); 
            } 
        }); 
 
		// LOCATION REFERENCE 
		locRefButton.setOnMouseClicked(mouseClickedEvent ->  
		{ 
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
            { 
            	loadLocationRefForm(); 
            } 
        }); 
 
		// PAY TYPE REFERENCE 
		payRefButton.setOnMouseClicked(mouseClickedEvent ->  
		{ 
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
            { 
            	loadPayCodeTypeRefForm(); 
            } 
        }); 
 
		// UNION TYPE REFERENCE 
		unionRefButton.setOnMouseClicked(mouseClickedEvent ->  
		{ 
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
            { 
            	loadUnionTypeRefForm(); 
            } 
        }); 
 
		// GENDER TYPE REFERENCE 
		genRefButton.setOnMouseClicked(mouseClickedEvent ->  
		{ 
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
            { 
            	loadGenderRefForm(); 
            } 
        }); 
		 
		// SET IDX DISABLED 
		emprIdIdx.setDisable(true); 
		empCovClsIdx.setDisable(true); 
		empDeptIdx.setDisable(true); 
		empLocIdx.setDisable(true); 
		empFstNmIdx.setDisable(true); 
		empDOBIdx.setDisable(true); 
		emplStNmIdx.setDisable(true); 
		emplCityIdx.setDisable(true); 
		emplHrDtIdx.setDisable(true); 
		emplRHrDtIdx.setDisable(true); 
		emplJobIdx.setDisable(true); 
		emplMNmIdx.setDisable(true); 
		emplIdIdx.setDisable(true); 
		emplPhnIdx.setDisable(true); 
		emplAdLn1Idx.setDisable(true); 
		emplStateIdx.setDisable(true); 
		empPayCodeIdx.setDisable(true); 
		empUnTypIdx.setDisable(true); 
		emplGendIdx.setDisable(true); 
		emplLstNmIdx.setDisable(true); 
		emplTrmDtIdx.setDisable(true); 
		emplEmlIdx.setDisable(true); 
		emplAdLn2Idx.setDisable(true); 
		emplZipIdx.setDisable(true); 
		emplrEmpIdIdx.setDisable(true); 
		emplSsnIdx.setDisable(true); 
		emplCF1Idx.setDisable(true); 
		emplCF2Idx.setDisable(true); 
		emplCF3Idx.setDisable(true); 
		emplCF4Idx.setDisable(true); 
		emplCF5Idx.setDisable(true); 
 
		// SET BUTTON TEXT 
		empFstNmParse.setText("+ Pattern"); 
		empFstNmParse.setFont(Font.font("Veranda", 12.0)); 
		empFstNmParse.setDisable(true);		 
		empDOBFormat.setText("+ Format"); 
		empDOBFormat.setFont(Font.font("Veranda", 12.0)); 
		empDOBFormat.setDisable(true);		 
		emplStNmParse.setText("+ Pattern"); 
		emplStNmParse.setFont(Font.font("Veranda", 12.0)); 
		emplStNmParse.setDisable(true);		 
		emplCityParse.setText("+ Pattern"); 
		emplCityParse.setFont(Font.font("Veranda", 12.0)); 
		emplCityParse.setDisable(true);		 
		emplMNmParse.setText("+ Pattern"); 
		emplMNmParse.setFont(Font.font("Veranda", 12.0)); 
		emplMNmParse.setDisable(true);		 
		emplHrDtFormat.setText("+ Format"); 
		emplHrDtFormat.setFont(Font.font("Veranda", 12.0)); 
		emplHrDtFormat.setDisable(true);		 
		emplRHrDtFormat.setText("+ Format"); 
		emplRHrDtFormat.setFont(Font.font("Veranda", 12.0)); 
		emplRHrDtFormat.setDisable(true);		 
		emplAdLn1Parse.setText("+ Pattern"); 
		emplAdLn1Parse.setFont(Font.font("Veranda", 12.0)); 
		emplAdLn1Parse.setDisable(true);		 
		emplStateParse.setText("+ Pattern"); 
		emplStateParse.setFont(Font.font("Veranda", 12.0)); 
		emplStateParse.setDisable(true);		 
		emplSsnParse.setText("+ Pattern"); 
		emplSsnParse.setFont(Font.font("Veranda", 12.0)); 
		emplSsnParse.setDisable(true);		 
		emplLstNmParse.setText("+ Pattern"); 
		emplLstNmParse.setFont(Font.font("Veranda", 12.0)); 
		emplLstNmParse.setDisable(true);		 
		emplTrmDtFormat.setText("+ Format"); 
		emplTrmDtFormat.setFont(Font.font("Veranda", 12.0)); 
		emplTrmDtFormat.setDisable(true);		 
		emplZipParse.setText("+ Pattern"); 
		emplZipParse.setFont(Font.font("Veranda", 12.0)); 
		emplZipParse.setDisable(true);		 
		emplrEmpIdParse.setText("+ Pattern"); 
		emplrEmpIdParse.setFont(Font.font("Veranda", 12.0)); 
		emplrEmpIdParse.setDisable(true);		 
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
 
        //EMPLOYEE SECTION 
        mapperFields.put(emprIdSkPn, new MapperField(emprIdSkPn, emprId, emprIdIdx, null, null, emprRefButton, "Employer Id", 80)); 
        mapperFields.put(empCovClsSkPn, new MapperField(empCovClsSkPn, empCovCls, empCovClsIdx, null, null, ccRefButton, "COV Class", 72)); 
        mapperFields.put(empDeptSkPn, new MapperField(empDeptSkPn, empDept, empDeptIdx, null, null, depRefButton, "Department", 79)); 
        mapperFields.put(empLocSkPn, new MapperField(empLocSkPn, empLoc, empLocIdx, null, null, locRefButton, "Location", 63)); 
        mapperFields.put(empFstNmSkPn, new MapperField(empFstNmSkPn, empFstNm, empFstNmIdx, empFstNmParse, null, null, "First Name", 78)); 
        mapperFields.put(empDOBSkPn, new MapperField(empDOBSkPn, empDOB, empDOBIdx, null, empDOBFormat, null, "DOB", 42)); 
        mapperFields.put(emplStNmSkPn, new MapperField(emplStNmSkPn, emplStNm, emplStNmIdx, emplStNmParse, null, null, "Strt NMBR", 74)); 
        mapperFields.put(emplCitySkPn, new MapperField(emplCitySkPn, emplCity, emplCityIdx, emplCityParse, null, null, "City", 40)); 
        mapperFields.put(emplHrDtSkPn, new MapperField(emplHrDtSkPn, emplHrDt, emplHrDtIdx, null, emplHrDtFormat, null, "Hire Date", 71)); 
        mapperFields.put(emplRHrDtSkPn, new MapperField(emplRHrDtSkPn, emplRHrDt, emplRHrDtIdx, null, emplRHrDtFormat, null, "Rehire Date", 82)); 
        mapperFields.put(emplJobSkPn, new MapperField(emplJobSkPn, emplJob, emplJobIdx, null, null, null, "Job Title", 65)); 
        mapperFields.put(emplMNmSkPn, new MapperField(emplMNmSkPn, emplMNm, emplMNmIdx, emplMNmParse, null, null, "Mid Name", 77)); 
        mapperFields.put(emplIdSkPn, new MapperField(emplIdSkPn, emplId, emplIdIdx, null, null, null, "ETC EE ID", 69)); 
        mapperFields.put(emplPhnSkPn, new MapperField(emplPhnSkPn, emplPhn, emplPhnIdx, null, null, null, "Phone", 54)); 
        mapperFields.put(emplAdLn1SkPn, new MapperField(emplAdLn1SkPn, emplAdLn1, emplAdLn1Idx, emplAdLn1Parse, null, null, "ADD LN 1", 71)); 
        mapperFields.put(emplStateSkPn, new MapperField(emplStateSkPn, emplState, emplStateIdx, emplStateParse, null, null, "State", 46)); 
        mapperFields.put(empPayCodeSkPn, new MapperField(empPayCodeSkPn, empPayCode, empPayCodeIdx, null, null, payRefButton, "Comp Type", 77)); 
        mapperFields.put(empUnTypSkPn, new MapperField(empUnTypSkPn, empUnTyp, empUnTypIdx, null, null, unionRefButton, "Union TP", 68)); 
        mapperFields.put(emplGendSkPn, new MapperField(emplGendSkPn, emplGend, emplGendIdx, null, null, genRefButton, "Gender", 59)); 
        mapperFields.put(emplLstNmSkPn, new MapperField(emplLstNmSkPn, emplLstNm, emplLstNmIdx, emplLstNmParse, null, null, "Last Name", 76)); 
        mapperFields.put(emplTrmDtSkPn, new MapperField(emplTrmDtSkPn, emplTrmDt, emplTrmDtIdx, null, emplTrmDtFormat, null, "Term Date", 76)); 
        mapperFields.put(emplEmlSkPn, new MapperField(emplEmlSkPn, emplEml, emplEmlIdx, null, null, null, "Email", 47)); 
        mapperFields.put(emplAdLn2SkPn, new MapperField(emplAdLn2SkPn, emplAdLn2, emplAdLn2Idx, null, null, null, "ADD LN 2", 72)); 
        mapperFields.put(emplZipSkPn, new MapperField(emplZipSkPn, emplZip, emplZipIdx, emplZipParse, null, null, "Zip", 40)); 
        mapperFields.put(emplrEmpIdSkPn, new MapperField(emplrEmpIdSkPn, emplrEmpId, emplrEmpIdIdx, emplrEmpIdParse, null, null, "ER EE ID", 64)); 
        mapperFields.put(emplSsnSkPn, new MapperField(emplSsnSkPn, emplSsn, emplSsnIdx, emplSsnParse, null, null, "SSN", 42)); 
        mapperFields.put(emplCF1SkPn, new MapperField(emplCF1SkPn, emplCF1, emplCF1Idx, emplCF1Parse, null, null, "CF 1", 42)); 
        mapperFields.put(emplCF2SkPn, new MapperField(emplCF2SkPn, emplCF2, emplCF2Idx, emplCF2Parse, null, null, "CF 2", 42)); 
        mapperFields.put(emplCF3SkPn, new MapperField(emplCF3SkPn, emplCF3, emplCF3Idx, null, null, null, "CF 3", 42)); 
        mapperFields.put(emplCF4SkPn, new MapperField(emplCF4SkPn, emplCF4, emplCF4Idx, null, null, null, "CF 4", 42)); 
        mapperFields.put(emplCF5SkPn, new MapperField(emplCF5SkPn, emplCF5, emplCF5Idx, null, null, null, "CF 5", 42)); 
 
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
        		DynamicEmployeeFileSpecificationRequest empCovReq = new DynamicEmployeeFileSpecificationRequest(DataManager.i().mDynamicEmployeeFileSpecification); 
        		empCovReq.setId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
 
        		DataManager.i().mDynamicEmployeeFileSpecification = AdminPersistenceManager.getInstance().get(empCovReq); 
        		 
        		EtcAdmin.i().setProgress(.75); 
                return null; 
            } 
        }; 
        EtcAdmin.i().setStatusMessage("Ready"); 
        EtcAdmin.i().setProgress(0); 
    	task.setOnSucceeded(e -> updateFileData()); 
    	task.setOnFailed(e -> updateFileData()); 
        new Thread(task).start(); 
	} 
 
	//Handles the data source selection 
	private void updateFileData() 
	{ 
		updateEmployeeFileData(); 
		loadIgnoreRows(DataManager.i().mDynamicEmployeeFileSpecification); 
		checkReadOnly(); 
        EtcAdmin.i().setStatusMessage("Ready"); 
        EtcAdmin.i().setProgress(0); 
	} 
 
	private void loadIgnoreRows(DynamicEmployeeFileSpecification fSpec) 
	{ 
		try {
			DynamicEmployeeFileIgnoreRowSpecificationRequest request = new DynamicEmployeeFileIgnoreRowSpecificationRequest(DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification); 
			request.setSpecificationId(fSpec.getId()); 
			DataManager.i().mDynamicEmployeeFileIgnoreRowSpecifications = AdminPersistenceManager.getInstance().getAll(request);
		} catch (CoreException e) { DataManager.i().log(Level.SEVERE, e); } 
	    catch (Exception e) { DataManager.i().logGenericException(e); }

	    List<HBoxIgnoreRowCell> ignoreRowList = new ArrayList<>(); 
	     
		for(DynamicEmployeeFileIgnoreRowSpecification spec : DataManager.i().mDynamicEmployeeFileIgnoreRowSpecifications) 
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
	private void updateEmployeeFileData() 
	{ 
		DynamicEmployeeFileSpecification fSpec = DataManager.i().mDynamicEmployeeFileSpecification; 
		if(fSpec != null)  
		{ 
			readOnly = fSpec.isLocked(); 
			Utils.updateControl(dfcovNameLabel,fSpec.getName()); 
			Utils.updateControl(dfcovTabIndexLabel,String.valueOf(fSpec.getTabIndex())); 
			Utils.updateControl(dfcovHeaderRowIndexLabel,String.valueOf(fSpec.getHeaderRowIndex())); 
			Utils.updateControl(dfcovCreateEmployeeCheck,fSpec.isCreateEmployee()); 
			dfcovCreateEmployeeCheck.setVisible(true); 
			Utils.updateControl(dfcovCreateEmployeeCheck,fSpec.isCreateEmployee()); 
			Utils.updateControl(dfcovSkipLastRowCheck, fSpec.isSkipLastRow()); 
			//core data read only 
			Utils.updateControl(dfcovCoreIdLabel,String.valueOf(DataManager.i().mPipelineSpecification.getId())); 
			Utils.updateControl(dfcovCoreActiveLabel,String.valueOf(fSpec.isActive())); 
			Utils.updateControl(dfcovCoreBODateLabel,fSpec.getBornOn()); 
			Utils.updateControl(dfcovCoreLastUpdatedLabel,fSpec.getLastUpdated()); 
 
			//custom text fields  
			Utils.updateControl(eeCF1, fSpec.getCfld1Name()); 
			Utils.updateControl(eeCF2, fSpec.getCfld2Name()); 
			Utils.updateControl(eeCF3, fSpec.getCfld3Name()); 
			Utils.updateControl(eeCF4, fSpec.getCfld4Name()); 
			Utils.updateControl(eeCF5, fSpec.getCfld5Name()); 
 
			//reference counts 
			setEmpRefCount(getEmployeeFileEmployerRefCount()); 
			setCvgRefCount(getEmployeeFileCvgClassRefCount()); 
			setGendRefCount(getEmployeeFileGenderTypeRefCount()); 
		    setDepRefCount(getEmployeeFileDepartmentRefCount()); 
		    setLocRefCount(getEmployeeFileLocationRefCount()); 
		    setPyTpRefCount(getEmployeeFilePayCodeTypeRefCount()); 
		    setUnTpRefCount(getEmployeeFileUnionTypeRefCount()); 
 
			//update the column field table 
			loadFieldSet1(fSpec);	  
		} 
	} 
 
	//Generate the coverage spec type 
	private void loadFieldSet1(DynamicEmployeeFileSpecification fSpec) 
	{ 
		//EMPLOYEE SECTION 
		enableFieldSet(fSpec.isErCol(), fSpec.getErColIndex(), emprIdSkPn, null, null); 
		enableFieldSet(fSpec.isCvgGroupCol(), fSpec.getCvgGroupColIndex(), empCovClsSkPn, null, null); 
		enableFieldSet(fSpec.isDeptCol(), fSpec.getDeptColIndex(), empDeptSkPn, null, null); 
		enableFieldSet(fSpec.isLocCol(), fSpec.getLocColIndex(), empLocSkPn, null, null); 
		enableFieldSet(fSpec.isfNameCol(), fSpec.getfNameColIndex(), empFstNmSkPn, fSpec.getfNameParsePatternId(), null); 
		enableFieldSet(fSpec.isDobCol(), fSpec.getDobColIndex(), empDOBSkPn, null, fSpec.getDobFormatId()); 
		enableFieldSet(fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), emplStNmSkPn, fSpec.getStreetParsePatternId(), null); 
		enableFieldSet(fSpec.isCityCol(), fSpec.getCityColIndex(), emplCitySkPn, fSpec.getCityParsePatternId(), null); 
		enableFieldSet(fSpec.isHireDtCol(), fSpec.getHireDtColIndex(), emplHrDtSkPn, null, fSpec.getHireDtFormatId()); 
		enableFieldSet(fSpec.isRhireDtCol(), fSpec.getRhireDtColIndex(), emplRHrDtSkPn, null, fSpec.getRhireDtFormatId()); 
		enableFieldSet(fSpec.isJobTtlCol(), fSpec.getJobTtlColIndex(), emplJobSkPn, null, null); 
		enableFieldSet(fSpec.ismNameCol(), fSpec.getmNameColIndex(), emplMNmSkPn, fSpec.getmNameParsePatternId(), null); 
		enableFieldSet(fSpec.isEtcRefCol(), fSpec.getEtcRefColIndex(), emplIdSkPn, null, null); 
		enableFieldSet(fSpec.isPhoneCol(), fSpec.getPhoneColIndex(), emplPhnSkPn, null, null); 
		enableFieldSet(fSpec.isStreetCol(), fSpec.getStreetColIndex(), emplAdLn1SkPn, fSpec.getStreetParsePatternId(), null); 
		enableFieldSet(fSpec.isStateCol(), fSpec.getStateColIndex(), emplStateSkPn, fSpec.getStateParsePatternId(), null); 
		enableFieldSet(fSpec.isPayCodeCol(), fSpec.getPayCodeColIndex(), empPayCodeSkPn, null, null); 
		enableFieldSet(fSpec.isUnionTypeCol(), fSpec.getUnionTypeColIndex(), empUnTypSkPn, null, null); 
		enableFieldSet(fSpec.isGenderCol(), fSpec.getGenderColIndex(), emplGendSkPn, null, null); 
		enableFieldSet(fSpec.islNameCol(), fSpec.getlNameColIndex(), emplLstNmSkPn, fSpec.getlNameParsePatternId(), null); 
		enableFieldSet(fSpec.isTermDtCol(), fSpec.getTermDtColIndex(), emplTrmDtSkPn, null, fSpec.getTermDtFormatId()); 
		enableFieldSet(fSpec.isEmlCol(), fSpec.getEmlColIndex(), emplEmlSkPn, null, null); 
		enableFieldSet(fSpec.isLin2Col(), fSpec.getLin2ColIndex(), emplAdLn2SkPn, null, null); 
		enableFieldSet(fSpec.isZipCol(), fSpec.getZipColIndex(), emplZipSkPn, fSpec.getZipParsePatternId(), null); 
		enableFieldSet(fSpec.isErRefCol(), fSpec.getErRefColIndex(), emplrEmpIdSkPn, fSpec.getErRefParsePatternId(), null); 
		enableFieldSet(fSpec.isSsnCol(), fSpec.getSsnColIndex(), emplSsnSkPn, fSpec.getSsnParsePatternId(), null); 
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
 
		if(skPn != null ? (fld = mapperFields.get(skPn)) != null : false) 
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
//						fld.getBtnParsePtrn().setStyle("-fx-background-color: ".concat(clr.getColor()).concat(";")); 
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
//						fld.getBtnDtFormat().setStyle("-fx-background-color: ".concat(clr.getColor()).concat(";")); 
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
	private ColorPalette registerCell(final Integer idx, final MapperField fld, Long ptn, Long fmt) 
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
//							f.getBtnParsePtrn().setDisable(false); 
//							f.getBtnParsePtrn().setStyle("-fx-background-color: ".concat(clr.getColor()).concat(";")); 
						} 
						if(f.getBtnDtFormat() != null) 
						{ 
//							f.getBtnDtFormat().setDisable(false); 
//							fld.getBtnDtFormat().setStyle("-fx-background-color: ".concat(clr.getColor()).concat(";")); 
						} 
						if(f.getBtnReferences() != null) 
						{ 
//							f.getBtnReferences().setDisable(false); 
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
//							f.getBtnParsePtrn().setStyle("-fx-background-color: #f0ffff;"); 
						} 
						if(f.getBtnDtFormat() != null) 
						{ 
							f.getBtnDtFormat().setDisable(true); 
//							f.getBtnDtFormat().setStyle("-fx-background-color: #f0ffff;"); 
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
//	 
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
		} catch (NumberFormatException e) { 
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
		DynamicEmployeeFileSpecification fSpec = DataManager.i().mDynamicEmployeeFileSpecification; 
 
		if(fld.getParsePatternId() != null) { fld.setTitle(fld.getTitle().replace(" (P:" + fld.getParsePatternId().toString() + ")", "")); fld.setParsePatternId(null); } 
		if(fld.getDateFormatId() != null) { fld.setTitle(fld.getTitle().replace(" (F:" + fld.getDateFormatId().toString() + ")", "")); fld.setDateFormatId(null); } 
 
		if(emprId.isSelected() == false){ fSpec.setErCol(false); fSpec.setErColIndex(0); } 
		if(empCovCls.isSelected() == false){ fSpec.setCvgGroupCol(false); fSpec.setCvgGroupColIndex(0); } 
		if(empDept.isSelected() == false){ fSpec.setDeptCol(false); fSpec.setDeptColIndex(0); } 
		if(empLoc.isSelected() == false){ fSpec.setLocCol(false); fSpec.setLocColIndex(0); } 
		if(empFstNm.isSelected() == false){ fSpec.setfNameParsePattern(null); fSpec.setfNameCol(false); fSpec.setfNameColIndex(0);  
		   empFstNmParse.setFont(Font.font("Veranda", 12.0)); empFstNmParse.setText("+ Pattern"); } 
		if(empDOB.isSelected() == false){ fSpec.setDobCol(false); fSpec.setDobColIndex(0); fSpec.setDobFormat(null); 
		   empDOBFormat.setFont(Font.font("Veranda", 12.0)); empDOBFormat.setText("+ Format");} 
		if(emplStNm.isSelected() == false){ fSpec.setStreetNumCol(false); fSpec.setStreetNumColIndex(0); fSpec.setStreetParsePattern(null); 
		   emplStNmParse.setFont(Font.font("Veranda", 12.0)); emplStNmParse.setText("+ Pattern"); } 
		if(emplCity.isSelected() == false){ fSpec.setCityCol(false); fSpec.setCityColIndex(0); fSpec.setCityParsePattern(null); 
		   emplCityParse.setFont(Font.font("Veranda", 12.0)); emplCityParse.setText("+ Pattern"); } 
		if(emplHrDt.isSelected() == false){ fSpec.setHireDtCol(false); fSpec.setHireDtColIndex(0); fSpec.setHireDtFormat(null); 
		   emplHrDtFormat.setFont(Font.font("Veranda", 12.0)); emplHrDtFormat.setText("+ Format"); } 
		if(emplRHrDt.isSelected() == false){ fSpec.setRhireDtCol(false); fSpec.setRhireDtColIndex(0); fSpec.setRhireDtFormat(null); 
		   emplRHrDtFormat.setFont(Font.font("Veranda", 12.0)); emplRHrDtFormat.setText("+ Format"); } 
		if(emplJob.isSelected() == false){ fSpec.setJobTtlCol(false); fSpec.setJobTtlColIndex(0); } 
		if(emplMNm.isSelected() == false){ fSpec.setmNameCol(false); fSpec.setmNameColIndex(0); fSpec.setmNameParsePattern(null); 
		   emplMNmParse.setFont(Font.font("Veranda", 12.0)); emplMNmParse.setText("+ Pattern"); } 
		if(emplId.isSelected() == false){ fSpec.setEtcRefCol(false); fSpec.setEtcRefColIndex(0); } 
		if(emplPhn.isSelected() == false){ fSpec.setPhoneCol(false); fSpec.setPhoneColIndex(0); } 
		if(emplAdLn1.isSelected() == false){ fSpec.setStreetCol(false); fSpec.setStreetColIndex(0); fSpec.setStreetParsePattern(null); 
		   emplAdLn1Parse.setFont(Font.font("Veranda", 12.0)); emplAdLn1Parse.setText("+ Pattern"); } 
		if(emplState.isSelected() == false){ fSpec.setStateCol(false); fSpec.setStateColIndex(0); fSpec.setStateParsePattern(null); 
		   emplStateParse.setFont(Font.font("Veranda", 12.0)); emplStateParse.setText("+ Pattern"); } 
		if(empPayCode.isSelected() == false){ fSpec.setPayCodeCol(false); fSpec.setPayCodeColIndex(0); } 
		if(empUnTyp.isSelected() == false){ fSpec.setUnionTypeCol(false); fSpec.setUnionTypeColIndex(0); } 
		if(emplGend.isSelected() == false){ fSpec.setGenderCol(false); fSpec.setGenderColIndex(0); } 
		if(emplGend.isSelected() == false){ fSpec.setGenderCol(false); fSpec.setGenderColIndex(0); } 
		if(emplLstNm.isSelected() == false){ fSpec.setlNameCol(false); fSpec.setlNameColIndex(0); fSpec.setlNameParsePattern(null); 
		   emplLstNmParse.setFont(Font.font("Veranda", 12.0)); emplLstNmParse.setText("+ Pattern"); } 
		if(emplTrmDt.isSelected() == false){ fSpec.setTermDtCol(false); fSpec.setTermDtColIndex(0); fSpec.setTermDtFormat(null); 
		   emplTrmDtFormat.setFont(Font.font("Veranda", 12.0)); emplTrmDtFormat.setText("+ Format"); } 
		if(emplEml.isSelected() == false){ fSpec.setEmlCol(false); fSpec.setEmlColIndex(0); } 
		if(emplAdLn2.isSelected() == false){ fSpec.setLin2Col(false); fSpec.setLin2ColIndex(0); } 
		if(emplZip.isSelected() == false){ fSpec.setZipCol(false); fSpec.setZipColIndex(0); fSpec.setZipParsePattern(null); 
		   emplZipParse.setFont(Font.font("Veranda", 12.0)); emplZipParse.setText("+ Pattern"); } 
		if(emplrEmpId.isSelected() == false){ fSpec.setErRefCol(false); fSpec.setErRefColIndex(0); fSpec.setErRefParsePattern(null); 
		   emplrEmpIdParse.setFont(Font.font("Veranda", 12.0)); emplrEmpIdParse.setText("+ Pattern"); } 
		if(emplSsn.isSelected() == false){ fSpec.setSsnCol(false); fSpec.setSsnColIndex(0); fSpec.setSsnParsePattern(null); 
		   emplSsnParse.setFont(Font.font("Veranda", 12.0)); emplSsnParse.setText("+ Pattern"); } 
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
		DynamicEmployeeFileSpecification fSpec = DataManager.i().mDynamicEmployeeFileSpecification; 
 
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
//							if(cel.getNode().getText().toString().matches(".*\\((P|F):[0-9]+\\).*$")) 
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
		dfcovClearFormButton.setDisable(readOnly); 
		dfcovSaveChangesButton.setDisable(readOnly); 
	} 
 
	private int getEmployeeFileEmployerRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicEmployeeFileEmployerReferenceRequest request = new DynamicEmployeeFileEmployerReferenceRequest(DataManager.i().mDynamicEmployeeFileEmployerReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicEmployeeFileSpecification.setErReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		
		for(DynamicEmployeeFileEmployerReference erRef : DataManager.i().mDynamicEmployeeFileSpecification.getErReferences()) 
		{ 
			if(erRef.isActive() == true && erRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getEmployeeFileCvgClassRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicEmployeeFileCoverageGroupReferenceRequest request = new DynamicEmployeeFileCoverageGroupReferenceRequest(DataManager.i().mDynamicEmployeeFileCoverageGroupReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicEmployeeFileSpecification.setCvgGroupReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); } 
	    catch (Exception e) { DataManager.i().logGenericException(e); }

		for(DynamicEmployeeFileCoverageGroupReference covRef : DataManager.i().mDynamicEmployeeFileSpecification.getCvgGroupReferences()) 
		{ 
			if(covRef.isActive() == true && covRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getEmployeeFileDepartmentRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicEmployeeFileDepartmentReferenceRequest request = new DynamicEmployeeFileDepartmentReferenceRequest(DataManager.i().mDynamicEmployeeFileDepartmentReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicEmployeeFileSpecification.setDeptReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); } 
	    catch (Exception e) { DataManager.i().logGenericException(e); }
 
		for(DynamicEmployeeFileDepartmentReference deptRef : DataManager.i().mDynamicEmployeeFileSpecification.getDeptReferences()) 
		{ 
			if(deptRef.isActive() == true && deptRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getEmployeeFileLocationRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicEmployeeFileLocationReferenceRequest request = new DynamicEmployeeFileLocationReferenceRequest(DataManager.i().mDynamicEmployeeFileLocationReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicEmployeeFileSpecification.setLocReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); } 
	    catch (Exception e) { DataManager.i().logGenericException(e); }

		for(DynamicEmployeeFileLocationReference locRef : DataManager.i().mDynamicEmployeeFileSpecification.getLocReferences()) 
		{ 
			if(locRef.isActive() == true && locRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getEmployeeFilePayCodeTypeRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicEmployeeFilePayCodeReferenceRequest request = new DynamicEmployeeFilePayCodeReferenceRequest(DataManager.i().mDynamicEmployeeFilePayCodeReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicEmployeeFileSpecification.setPayCodeReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); } 
	    catch (Exception e) { DataManager.i().logGenericException(e); }

		for(DynamicEmployeeFilePayCodeReference payRef : DataManager.i().mDynamicEmployeeFileSpecification.getPayCodeReferences()) 
		{ 
			if(payRef.isActive() == true && payRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getEmployeeFileUnionTypeRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicEmployeeFileUnionTypeReferenceRequest request = new DynamicEmployeeFileUnionTypeReferenceRequest(DataManager.i().mDynamicEmployeeFileUnionTypeReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicEmployeeFileSpecification.setUnionTypeReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); } 
	    catch (Exception e) { DataManager.i().logGenericException(e); }

		for(DynamicEmployeeFileUnionTypeReference unTpRef : DataManager.i().mDynamicEmployeeFileSpecification.getUnionTypeReferences()) 
		{ 
			if(unTpRef.isActive() == true && unTpRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getEmployeeFileGenderTypeRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicEmployeeFileGenderReferenceRequest request = new DynamicEmployeeFileGenderReferenceRequest(DataManager.i().mDynamicEmployeeFileGenderReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicEmployeeFileSpecification.setGenderReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); } 
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		
		for(DynamicEmployeeFileGenderReference gendRef: DataManager.i().mDynamicEmployeeFileSpecification.getGenderReferences()) 
		{ 
			if(gendRef.isActive() == true && gendRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private boolean validateData() 
	{ 
		boolean bReturn = true; 
 
		//check for required data 
		if(!Utils.validate(dfcovNameLabel)) bReturn = false; 
//		if(!Utils.validate(dfcovDescriptionLabel)) bReturn = false; 
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
		updateEmployeeFile(); 
  		EtcAdmin.i().setProgress(0); 
	} 

	private void updateEmployeeFile() 
	{ 
		if(DataManager.i().mDynamicEmployeeFileSpecification != null) 
		{ 
			DynamicEmployeeFileSpecification fSpec = DataManager.i().mDynamicEmployeeFileSpecification; 
			DynamicEmployeeFileSpecificationRequest request = new DynamicEmployeeFileSpecificationRequest(); 

			//GATHER THE FIELDS 
			fSpec.setName(dfcovNameLabel.getText()); 
			fSpec.setTabIndex(Integer.valueOf(dfcovTabIndexLabel.getText())); 
			fSpec.setHeaderRowIndex(Integer.valueOf(dfcovHeaderRowIndexLabel.getText())); 
			fSpec.setCreateEmployee(dfcovCreateEmployeeCheck.isSelected()); 
			fSpec.setSkipLastRow(dfcovSkipLastRowCheck.isSelected()); 

			// EMPLOYEE 
	     	// EMPLOYER IDENTIFIER 
			fSpec.setErCol(Boolean.valueOf(emprId.isSelected())); 
			fSpec.setErColIndex(Integer.valueOf(emprIdIdx.getText() != null && !emprIdIdx.getText().isEmpty() ? emprIdIdx.getText() : "0")); 
	 
	     	// COVERAGE CLASS 
			fSpec.setCvgGroupCol(Boolean.valueOf(empCovCls.isSelected())); 
			fSpec.setCvgGroupColIndex(Integer.valueOf(empCovClsIdx.getText() != null && !empCovClsIdx.getText().isEmpty() ? empCovClsIdx.getText() : "0")); 
	 
	     	// DEPARTMENT 
			fSpec.setDeptCol(Boolean.valueOf(empDept.isSelected())); 
			fSpec.setDeptColIndex(Integer.valueOf(empDeptIdx.getText() != null && !empDeptIdx.getText().isEmpty() ? empDeptIdx.getText() : "0")); 
	 
	     	// LOCATION 
			fSpec.setLocCol(Boolean.valueOf(empLoc.isSelected())); 
			fSpec.setLocColIndex(Integer.valueOf(empLocIdx.getText() != null && !empLocIdx.getText().isEmpty() ? empLocIdx.getText() : "0")); 
	 
			// FIRST NAME 
			fSpec.setfNameCol(Boolean.valueOf(empFstNm.isSelected())); 
			fSpec.setfNameColIndex(Integer.valueOf(empFstNmIdx.getText() != null && !empFstNmIdx.getText().isEmpty() ? empFstNmIdx.getText() : "0")); 
			if(empFstNmParse.getId() != null && Utils.isInt(empFstNmParse.getId()))  
			{
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(empFstNmParse.getId())); 
				request.setfNameParsePatternRequest(req); 
			} else request.setClearFNameParsePatternId(true);
			 
			// DATE OF BIRTH 
			fSpec.setDobCol(Boolean.valueOf(empDOB.isSelected())); 
			fSpec.setDobColIndex(Integer.valueOf(empDOBIdx.getText() != null && !empDOBIdx.getText().isEmpty() ? empDOBIdx.getText() : "0")); 
			if(empDOBFormat.getId() != null && Utils.isInt(empDOBFormat.getId()))  
			{ 
				PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
				req.setId(Long.valueOf(empDOBFormat.getId())); 
				request.setDobFormatRequest(req); 
			} else request.setClearDobFormatId(true); 
			 
			// STREET NUMBER 
			fSpec.setStreetNumCol(Boolean.valueOf(emplStNm.isSelected())); 
			fSpec.setStreetNumColIndex(Integer.valueOf(emplStNmIdx.getText() != null && !emplStNmIdx.getText().isEmpty() ? emplStNmIdx.getText() : "0")); 
			if(emplStNmParse.getId() != null && Utils.isInt(emplStNmParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplStNmParse.getId())); 
				request.setStreetParsePatternRequest(req); 
			} else request.setClearStreetParsePatternId(true); 
	 
			// CITY 
			fSpec.setCityCol(Boolean.valueOf(emplCity.isSelected())); 
			fSpec.setCityColIndex(Integer.valueOf(emplCityIdx.getText() != null && !emplCityIdx.getText().isEmpty() ? emplCityIdx.getText() : "0")); 
			if(emplCityParse.getId() != null && Utils.isInt(emplCityParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplCityParse.getId())); 
				request.setCityParsePatternRequest(req); 
			} else request.setClearCityParsePatternId(true); 
	 
	     	// HIRE DATE 
			fSpec.setHireDtCol(Boolean.valueOf(emplHrDt.isSelected())); 
			fSpec.setHireDtColIndex(Integer.valueOf(emplHrDtIdx.getText() != null && !emplHrDtIdx.getText().isEmpty() ? emplHrDtIdx.getText() : "0")); 
			if(emplHrDtFormat.getId() != null && Utils.isInt(emplHrDtFormat.getId()))  
			{ 
				PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
				req.setId(Long.valueOf(emplHrDtFormat.getId())); 
				request.setHireDtFormatRequest(req); 
			} else request.setClearHireDtFormatId(true); 
	 
	     	// REHIRE DATE 
			fSpec.setRhireDtCol(Boolean.valueOf(emplRHrDt.isSelected())); 
			fSpec.setRhireDtColIndex(Integer.valueOf(emplRHrDtIdx.getText() != null && !emplRHrDtIdx.getText().isEmpty() ? emplRHrDtIdx.getText() : "0")); 
			if(emplRHrDtFormat.getId() != null && Utils.isInt(emplRHrDtFormat.getId()))  
			{ 
				PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
				req.setId(Long.valueOf(emplRHrDtFormat.getId())); 
				request.setRhireDtFormatRequest(req); 
			} else request.setClearRhireDtFormatId(true); 
	 
			// JOB TITLE 
			fSpec.setJobTtlCol(Boolean.valueOf(emplJob.isSelected())); 
			fSpec.setJobTtlColIndex(Integer.valueOf(emplJobIdx.getText() != null && !emplJobIdx.getText().isEmpty() ? emplJobIdx.getText() : "0")); 
	 
	     	// MIDDLE NAME 
			fSpec.setmNameCol(Boolean.valueOf(emplMNm.isSelected())); 
			fSpec.setmNameColIndex(Integer.valueOf(emplMNmIdx.getText() != null && !emplMNmIdx.getText().isEmpty() ? emplMNmIdx.getText() : "0")); 
			if(emplMNmParse.getId() != null && Utils.isInt(emplMNmParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplMNmParse.getId())); 
				request.setmNameParsePatternRequest(req); 
			} else request.setClearMNameParsePatternId(true); 
	 
	     	// ETC LITE ID 
			fSpec.setEtcRefCol(Boolean.valueOf(emplId.isSelected())); 
			fSpec.setEtcRefColIndex(Integer.valueOf(emplIdIdx.getText() != null && !emplIdIdx.getText().isEmpty() ? emplIdIdx.getText() : "0")); 
	 
			// PHONE NUMBER 
			fSpec.setPhoneCol(Boolean.valueOf(emplPhn.isSelected())); 
			fSpec.setPhoneColIndex(Integer.valueOf(emplPhnIdx.getText() != null && !emplPhnIdx.getText().isEmpty() ? emplPhnIdx.getText() : "0")); 
	 
			// ADDRESS LINE 1 
			fSpec.setStreetCol(Boolean.valueOf(emplAdLn1.isSelected())); 
			fSpec.setStreetColIndex(Integer.valueOf(emplAdLn1Idx.getText() != null && !emplAdLn1Idx.getText().isEmpty() ? emplAdLn1Idx.getText() : "0")); 
			if(emplAdLn1Parse.getId() != null && Utils.isInt(emplAdLn1Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplAdLn1Parse.getId())); 
				request.setStreetParsePatternRequest(req); 
			} else { request.setClearStreetParsePatternId(true); } 
			 
			// STATE 
			fSpec.setStateCol(Boolean.valueOf(emplState.isSelected())); 
			fSpec.setStateColIndex(Integer.valueOf(emplStateIdx.getText() != null && !emplStateIdx.getText().isEmpty() ? emplStateIdx.getText() : "0")); 
			if(emplStateParse.getId() != null && Utils.isInt(emplStateParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplStateParse.getId())); 
				request.setStateParsePatternRequest(req); 
			} else request.setClearStateParsePatternId(true); 
	 
			// PAY CODE 
			fSpec.setPayCodeCol(Boolean.valueOf(empPayCode.isSelected())); 
			fSpec.setPayCodeColIndex(Integer.valueOf(empPayCodeIdx.getText() != null && !empPayCodeIdx.getText().isEmpty() ? empPayCodeIdx.getText() : "0")); 
	 
			// UNION TYPE 
			fSpec.setUnionTypeCol(Boolean.valueOf(empUnTyp.isSelected())); 
			fSpec.setUnionTypeColIndex(Integer.valueOf(empUnTypIdx.getText() != null && !empUnTypIdx.getText().isEmpty() ? empUnTypIdx.getText() : "0")); 
	 
			// GENDER 
			fSpec.setGenderCol(Boolean.valueOf(emplGend.isSelected())); 
			fSpec.setGenderColIndex(Integer.valueOf(emplGendIdx.getText() != null && !emplGendIdx.getText().isEmpty() ? emplGendIdx.getText() : "0")); 
	 
			// LAST NAME 
			fSpec.setlNameCol(Boolean.valueOf(emplLstNm.isSelected())); 
			fSpec.setlNameColIndex(Integer.valueOf(emplLstNmIdx.getText() != null && !emplLstNmIdx.getText().isEmpty() ? emplLstNmIdx.getText() : "0")); 
			if(emplLstNmParse.getId() != null && Utils.isInt(emplLstNmParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplLstNmParse.getId())); 
				request.setlNameParsePatternRequest(req); 
			} else request.setClearLNameParsePatternId(true); 
 
	     	// TERMINATION DATE 
			fSpec.setTermDtCol(Boolean.valueOf(emplTrmDt.isSelected())); 
			fSpec.setTermDtColIndex(Integer.valueOf(emplTrmDtIdx.getText() != null && !emplTrmDtIdx.getText().isEmpty() ? emplTrmDtIdx.getText() : "0")); 
			if(emplTrmDtFormat.getId() != null && Utils.isInt(emplTrmDtFormat.getId()))  
			{ 
				PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
				req.setId(Long.valueOf(emplTrmDtFormat.getId())); 
				request.setTermDtFormatRequest(req); 
			} else request.setClearTermDtFormatId(true); 
	 
			// EMAIL COLUMN 
			fSpec.setEmlCol(Boolean.valueOf(emplEml.isSelected())); 
			fSpec.setEmlColIndex(Integer.valueOf(emplEmlIdx.getText() != null && !emplEmlIdx.getText().isEmpty() ? emplEmlIdx.getText() : "0")); 
	 
			// ADDRESS LINE 2 
			fSpec.setLin2Col(Boolean.valueOf(emplAdLn2.isSelected())); 
			fSpec.setLin2ColIndex(Integer.valueOf(emplAdLn2Idx.getText() != null && !emplAdLn2Idx.getText().isEmpty() ? emplAdLn2Idx.getText() : "0")); 
	 
			// ZIP 
			fSpec.setZipCol(Boolean.valueOf(emplZip.isSelected())); 
			fSpec.setZipColIndex(Integer.valueOf(emplZipIdx.getText() != null && !emplZipIdx.getText().isEmpty() ? emplZipIdx.getText() : "0")); 
			if(emplZipParse.getId() != null && Utils.isInt(emplZipParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplZipParse.getId())); 
				request.setZipParsePatternRequest(req); 
			} else request.setClearZipParsePatternId(true); 
	 
	     	// EMPLOYEE IDENTIFIER 
			fSpec.setErRefCol(Boolean.valueOf(emplrEmpId.isSelected())); 
			fSpec.setErRefColIndex(Integer.valueOf(emplrEmpIdIdx.getText() != null && !emplrEmpIdIdx.getText().isEmpty() ? emplrEmpIdIdx.getText() : "0")); 
			if(emplrEmpIdParse.getId() != null && Utils.isInt(emplrEmpIdParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplrEmpIdParse.getId())); 
				request.setErRefParsePatternRequest(req); 
			} else request.setClearErRefParsePatternId(true); 
	 
	     	// EMPLOYEE SSN 
			fSpec.setSsnCol(Boolean.valueOf(emplSsn.isSelected())); 
			fSpec.setSsnColIndex(Integer.valueOf(emplSsnIdx.getText() != null && !emplSsnIdx.getText().isEmpty() ? emplSsnIdx.getText() : "0")); 
			if(emplSsnParse.getId() != null && Utils.isInt(emplSsnParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplSsnParse.getId())); 
				request.setSsnParsePatternRequest(req); 
			} else request.setClearSsnParsePatternId(true); 
	 
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
		} 
	} 
	 
	@FXML 
	private void onAddIgnoreRow(ActionEvent event) 
	{ 
		switch(fileType) 
		{ 
			case EMPLOYEE: 
	        	DataManager.i().mDynamicEmployeeFileIgnoreRowSpecification = null; 
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
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/mapper/ViewMapperEmployeeRowIgnore.fxml")); 
			Parent ControllerNode = loader.load(); 
	        ViewMapperEmployeeRowIgnoreController ignoreController = (ViewMapperEmployeeRowIgnoreController) loader.getController(); 
 
	        ignoreController.load(); 
	        Stage stage = new Stage(); 
	        stage.initModality(Modality.APPLICATION_MODAL); 
	        stage.initStyle(StageStyle.UNDECORATED); 
	        stage.setScene(new Scene(ControllerNode));   
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(ignoreController.changesMade == true) 
	        	loadMapperData(); 
		} catch (IOException e) { DataManager.i().log(Level.SEVERE, e); }        		 
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
	    if((result.isPresent()) && (result.get() != ButtonType.OK)) { return; } 
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
		} catch (IOException e) { DataManager.i().log(Level.SEVERE, e); }        		 
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
		} catch (IOException e) { DataManager.i().log(Level.SEVERE, e); }        		 
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
	        stage.onCloseRequestProperty().setValue(e -> setEmpRefCount(getEmployeeFileEmployerRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setEmpRefCount(getEmployeeFileEmployerRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true) 
				setEmpRefCount(getEmployeeFileEmployerRefCount()); 
		} catch (IOException e) { 
			 DataManager.i().log(Level.SEVERE, e); 
			Utils.alertUser("Reference Popup Load Error", e.getMessage());
		}         
	    catch (Exception e) { DataManager.i().logGenericException(e); }
	} 
 
	private void loadCoverageClassRefForm()  
	{ 
        try { 
        	// load the fxml 
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationCoverageClassRef.fxml")); 
			Parent ControllerNode = loader.load(); 
	        ViewDynamicFileSpecificationCoverageClassRefController refController = (ViewDynamicFileSpecificationCoverageClassRefController) loader.getController(); 
	        refController.setFileType(fileType); 
	        refController.load(); 
 
	        Stage stage = new Stage(); 
	        stage.initModality(Modality.APPLICATION_MODAL); 
	        stage.initStyle(StageStyle.UNDECORATED); 
	        stage.setScene(new Scene(ControllerNode));   
	        stage.onCloseRequestProperty().setValue(e -> setCvgRefCount(getEmployeeFileCvgClassRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setCvgRefCount(getEmployeeFileCvgClassRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
				setCvgRefCount(getEmployeeFileCvgClassRefCount()); 
		} catch (IOException e) { 
			 DataManager.i().log(Level.SEVERE, e); 
			Utils.alertUser("Reference Popup Load Error", e.getMessage());
		}        		 
	    catch (Exception e) { DataManager.i().logGenericException(e); }
	} 
	 
	private void loadDepartmentRefForm()  
	{ 
        try { 
        	// load the fxml 
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationDepartmentRef.fxml")); 
			Parent ControllerNode = loader.load(); 
	        ViewDynamicFileSpecificationDepartmentRefController refController = (ViewDynamicFileSpecificationDepartmentRefController) loader.getController(); 
 
	        refController.setFileType(fileType); 
	        refController.load(); 
 
	        Stage stage = new Stage(); 
	        stage.initModality(Modality.APPLICATION_MODAL); 
	        stage.initStyle(StageStyle.UNDECORATED); 
	        stage.setScene(new Scene(ControllerNode));   
	        stage.onCloseRequestProperty().setValue(e -> setDepRefCount(getEmployeeFileDepartmentRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setDepRefCount(getEmployeeFileDepartmentRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
				setDepRefCount(getEmployeeFileDepartmentRefCount()); 
		} catch (IOException e) { 
			 DataManager.i().log(Level.SEVERE, e); 
			Utils.alertUser("Reference Popup Load Error", e.getMessage());
		}        		 
	    catch (Exception e) { DataManager.i().logGenericException(e); }
	} 
	 
	private void loadLocationRefForm()  
	{ 
        try { 
        	// load the fxml 
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationLocationRef.fxml")); 
			Parent ControllerNode = loader.load(); 
	        ViewDynamicFileSpecificationLocationRefController refController = (ViewDynamicFileSpecificationLocationRefController) loader.getController(); 
 
	        refController.setFileType(fileType); 
	        refController.load(); 
 
	        Stage stage = new Stage(); 
	        stage.initModality(Modality.APPLICATION_MODAL); 
	        stage.initStyle(StageStyle.UNDECORATED); 
	        stage.setScene(new Scene(ControllerNode));   
	        stage.onCloseRequestProperty().setValue(e -> setLocRefCount(getEmployeeFileLocationRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setLocRefCount(getEmployeeFileLocationRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
				setLocRefCount(getEmployeeFileLocationRefCount()); 
		} catch (IOException e) { 
			 DataManager.i().log(Level.SEVERE, e); 
			Utils.alertUser("Reference Popup Load Error", e.getMessage());
		}        		 
	    catch (Exception e) { DataManager.i().logGenericException(e); }
	} 
	 
	private void loadPayCodeTypeRefForm() 
	{ 
        try {
        	// load the fxml 
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationPayCodeTypeRef.fxml")); 
			Parent ControllerNode = loader.load(); 
	        ViewDynamicFileSpecificationPayCodeTypeRefController refController = (ViewDynamicFileSpecificationPayCodeTypeRefController) loader.getController(); 

	        refController.setFileType(fileType); 
	        refController.load(); 
 
	        Stage stage = new Stage(); 
	        stage.initModality(Modality.APPLICATION_MODAL); 
	        stage.initStyle(StageStyle.UNDECORATED); 
	        stage.setScene(new Scene(ControllerNode));   
	        stage.onCloseRequestProperty().setValue(e -> setPyTpRefCount(getEmployeeFilePayCodeTypeRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setPyTpRefCount(getEmployeeFilePayCodeTypeRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
				setPyTpRefCount(getEmployeeFilePayCodeTypeRefCount()); 
		} catch (IOException e) { 
			 DataManager.i().log(Level.SEVERE, e); 
			Utils.alertUser("Reference Popup Load Error", e.getMessage());
		}        		 
	    catch (Exception e) { DataManager.i().logGenericException(e); }
	} 
	 
	private void loadUnionTypeRefForm() 
	{ 
        try { 
        	// load the fxml 
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationunionTypeRef.fxml")); 
			Parent ControllerNode = loader.load(); 
	        ViewDynamicFileSpecificationUnionTypeRefController refController = (ViewDynamicFileSpecificationUnionTypeRefController) loader.getController(); 
	        refController.setFileType(fileType); 
	        refController.load(); 
 
	        Stage stage = new Stage(); 
	        stage.initModality(Modality.APPLICATION_MODAL); 
	        stage.initStyle(StageStyle.UNDECORATED); 
	        stage.setScene(new Scene(ControllerNode));   
	        stage.onCloseRequestProperty().setValue(e -> setUnTpRefCount(getEmployeeFileUnionTypeRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setUnTpRefCount(getEmployeeFileUnionTypeRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
				setUnTpRefCount(getEmployeeFileUnionTypeRefCount()); 
		} catch (IOException e) { 
			 DataManager.i().log(Level.SEVERE, e); 
			Utils.alertUser("Reference Popup Load Error", e.getMessage());
		}        		 
	    catch (Exception e) { DataManager.i().logGenericException(e); }
	} 
 
	private void loadGenderRefForm()  
	{ 
        try { 
        	// load the fxml 
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationGenderTypeRef.fxml")); 
			Parent ControllerNode = loader.load(); 
	        ViewDynamicFileSpecificationGenderTypeRefController refController = (ViewDynamicFileSpecificationGenderTypeRefController) loader.getController(); 
	        refController.setFileType(fileType); 
	        refController.load(); 
 
	        Stage stage = new Stage(); 
	        stage.initModality(Modality.APPLICATION_MODAL); 
	        stage.initStyle(StageStyle.UNDECORATED); 
	        stage.setScene(new Scene(ControllerNode));   
	        stage.onCloseRequestProperty().setValue(e -> setGendRefCount(getEmployeeFileGenderTypeRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setGendRefCount(getEmployeeFileGenderTypeRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
				setGendRefCount(getEmployeeFileGenderTypeRefCount()); 
		} catch (IOException e) { 
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
 
  		} catch (IOException | CoreException e) { DataManager.i().log(Level.SEVERE, e); }        		 
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
  	        stage.setScene(new Scene(ControllerNode));   
	        EtcAdmin.i().positionStageCenter(stage);
  	        stage.showAndWait(); 
 
  	        if(DataManager.i().mPipelineParseDateFormat != null)
  	        {
  	  	        fld.setDateFormatId(DataManager.i().mPipelineParseDateFormat.getId()); 
  	        } else { fld.setDateFormatId(null); }
 
  		} catch (IOException | CoreException e) { DataManager.i().log(Level.SEVERE, e); }        		 
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
		if(empFstNmParse.getText().contains("+ Pattern")){ empFstNmParse.setId("empFstNmParse"); } 
		if(empDOBFormat.getText().contains("+ Format")){ empDOBFormat.setId("empDOBFormat"); } 
		if(emplStNmParse.getText().contains("+ Pattern")){ emplStNmParse.setId("emplStNmParse"); } 
		if(emplCityParse.getText().contains("+ Pattern")){ emplCityParse.setId("emplCityParse"); } 
		if(emplMNmParse.getText().contains("+ Pattern")){ emplMNmParse.setId("emplMNmParse"); } 
		if(emplHrDtFormat.getText().contains("+ Format")){ emplHrDtFormat.setId("emplHrDtFormat"); } 
		if(emplRHrDtFormat.getText().contains("+ Format")){ emplRHrDtFormat.setId("emplRHrDtFormat"); } 
		if(emplAdLn1Parse.getText().contains("+ Pattern")){ emplAdLn1Parse.setId("emplAdLn1Parse"); } 
		if(emplStateParse.getText().contains("+ Pattern")){ emplStateParse.setId("emplStateParse"); } 
		if(emplSsnParse.getText().contains("+ Pattern")){ emplSsnParse.setId("emplSsnParse"); } 
		if(emplLstNmParse.getText().contains("+ Pattern")){ emplLstNmParse.setId("emplLstNmParse"); } 
		if(emplTrmDtFormat.getText().contains("+ Format")){ emplTrmDtFormat.setId("emplTrmDtFormat"); } 
		if(emplZipParse.getText().contains("+ Pattern")){ emplZipParse.setId("emplZipParse"); } 
		if(emplrEmpIdParse.getText().contains("+ Pattern")){ emplrEmpIdParse.setId("emplrEmpIdParse"); } 
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
				fld.setTitle(fld.getTitle() + " (P:" + ptn + ")"); 
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
				fld.setTitle(fld.getTitle() + " (P:" + ptn + ")"); 
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
 
    public void setCvgRefCount(int count) 
    { 
    	covRefLabel.setFont(Font.font("Veranda", 12.0)); 
        covRefLabel.setText("References: " + String.valueOf(count)); 
    } 
 
    public void setDepRefCount(int count) 
    { 
    	deptRefLabel.setFont(Font.font("Veranda", 12.0)); 
        deptRefLabel.setText("References: " + String.valueOf(count)); 
    } 
 
    public void setLocRefCount(int count) 
    { 
    	locRefLabel.setFont(Font.font("Veranda", 12.0)); 
        locRefLabel.setText("References: " + String.valueOf(count)); 
    } 
 
    public void setPyTpRefCount(int count) 
    { 
    	payRefLabel.setFont(Font.font("Veranda", 12.0)); 
    	payRefLabel.setText("References: " + String.valueOf(count)); 
    } 
 
    public void setUnTpRefCount(int count) 
    { 
    	unionRefLabel.setFont(Font.font("Veranda", 12.0)); 
        unionRefLabel.setText("References: " + String.valueOf(count)); 
    } 
 
    public void setGendRefCount(int count) 
    { 
    	gendRefLabel.setFont(Font.font("Veranda", 12.0)); 
        gendRefLabel.setText("References: " + String.valueOf(count)); 
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
				empCovCls.setSelected(false); 
				empDept.setSelected(false); 
				empLoc.setSelected(false); 
				empFstNm.setSelected(false);  
				empDOB.setSelected(false); 
				emplStNm.setSelected(false); 
				emplCity.setSelected(false); 
				emplHrDt.setSelected(false); 
				emplRHrDt.setSelected(false); 
				emplJob.setSelected(false); 
				emplMNm.setSelected(false); 
				emplId.setSelected(false); 
				emplPhn.setSelected(false); 
				emplAdLn1.setSelected(false); 
				emplState.setSelected(false); 
				empPayCode.setSelected(false); 
				empUnTyp.setSelected(false); 
				emplGend.setSelected(false); 
				emplLstNm.setSelected(false); 
				emplTrmDt.setSelected(false); 
				emplEml.setSelected(false); 
				emplAdLn2.setSelected(false); 
				emplZip.setSelected(false); 
				emplrEmpId.setSelected(false); 
				emplSsn.setSelected(false); 
				emplCF1.setSelected(false); 
				emplCF2.setSelected(false); 
				emplCF3.setSelected(false); 
				emplCF4.setSelected(false); 
				emplCF5.setSelected(false); 
				dfcovNameLabel.setText(""); 
//				dfcovDescriptionLabel.setText(""); 
				dfcovTabIndexLabel.setText("0"); 
				dfcovHeaderRowIndexLabel.setText("0"); 
				dfcovCreateEmployeeCheck.setSelected(false); 
				dfcovSkipLastRowCheck.setSelected(false); 
 
				mf.getTxtFldIndex().setText(""); 
				eeCF1.setText(""); 
				eeCF2.setText(""); 
				eeCF3.setText(""); 
				eeCF4.setText(""); 
				eeCF5.setText(""); 
				emprRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
				ccRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
				depRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
				locRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
				payRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
				unionRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
				genRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
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
		DynamicEmployeeFileSpecification fSpec = DataManager.i().mDynamicEmployeeFileSpecification; 
		 
		if(dfcovCreateEmployeeCheck.isSelected() == true) { fSpec.setCreateEmployee(true); } 
		else { fSpec.setCreateEmployee(false); } 
 
		if(dfcovSkipLastRowCheck.isSelected() == true) { fSpec.setSkipLastRow(true); } 
		else { fSpec.setSkipLastRow(false); } 
	}	 
 
	@FXML 
	private void onClearForm(ActionEvent event) { clearForm(); }	 
 
	//Reference Button Action Events 
	@FXML 
	private void onEmprRefClick(ActionEvent event) { loadEmployerRefForm(); }	 
 
	@FXML 
	private void onCcRefClick(ActionEvent event) { loadCoverageClassRefForm(); }	 
 
	@FXML 
	private void onDepRefClick(ActionEvent event) { loadDepartmentRefForm(); }	 
 
	@FXML 
	private void onLocRefClick(ActionEvent event) { loadLocationRefForm(); }	 
 
	@FXML 
	private void onPayRefClick(ActionEvent event) { loadPayCodeTypeRefForm(); }	 
 
	@FXML 
	private void onUnionRefClick(ActionEvent event) { loadUnionTypeRefForm(); }	 
	 
	@FXML 
	private void onGenRefClick(ActionEvent event) { loadGenderRefForm(); }	 
 
	 
	//Parse and Format Button Action Events 
	@FXML 
	private void onEmpFstNmParse(ActionEvent event) { setParseButtonValue(event); }	 
 
	@FXML 
	private void onEmpDOBFormat(ActionEvent event) { setFormatButtonValue(event); } 
 
	@FXML 
	private void onEmplStNmParse(ActionEvent event) { setParseButtonValue(event); } 
 
	@FXML 
	private void onEmplCityParse(ActionEvent event) { setParseButtonValue(event); } 
 
	@FXML 
	private void onEmplMNmParse(ActionEvent event) { setParseButtonValue(event); } 
 
	@FXML 
	private void onEmplHrDtFormat(ActionEvent event) { setFormatButtonValue(event); } 
 
	@FXML 
	private void onEmplRHrDtFormat(ActionEvent event) { setFormatButtonValue(event); } 
 
	@FXML 
	private void onEmplAdLn1Parse(ActionEvent event) { setParseButtonValue(event); } 
 
	@FXML 
	private void onEmplStateParse(ActionEvent event) { setParseButtonValue(event); } 
 
	@FXML 
	private void onEmplSsnParse(ActionEvent event) { setParseButtonValue(event); } 
 
	@FXML 
	private void onEmplLstNmParse(ActionEvent event) { setParseButtonValue(event); } 
 
	@FXML 
	private void onEmplTrmDtFormat(ActionEvent event) { setFormatButtonValue(event); } 
 
	@FXML 
	private void onEmplZipParse(ActionEvent event) { setParseButtonValue(event); } 
 
	@FXML 
	private void onEmplrEmpIdParse(ActionEvent event) { setParseButtonValue(event); } 
 
	@FXML 
	private void onEmplCF1Parse(ActionEvent event) { setParseButtonValue(event); } 
 
	@FXML 
	private void onEmplCF2Parse(ActionEvent event) { setParseButtonValue(event); } 
 
	public class HBoxIgnoreRowCell extends HBox  
	{ 
        Label lblCol1 = new Label(); 
        Label lblCol2 = new Label(); 
        DynamicEmployeeFileIgnoreRowSpecification eeSpec; 
 
        HBoxIgnoreRowCell(DynamicEmployeeFileIgnoreRowSpecification eeSpec) 
        { 
             super(); 
 
             if(eeSpec != null)  
             { 
            	 lblCol1.setText(Utils.getDateString(eeSpec.getLastUpdated())); 
            	 lblCol2.setText(eeSpec.getName()); 
            	 this.eeSpec = eeSpec; 
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
 
        public DynamicEmployeeFileIgnoreRowSpecification getEmpSpec()  
        { 
        	return eeSpec;  
        } 
    } 
}