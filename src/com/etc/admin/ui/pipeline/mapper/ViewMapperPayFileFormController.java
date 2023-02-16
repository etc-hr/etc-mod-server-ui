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
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationCoverageClassRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationDepartmentRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationGenderTypeRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationLocationRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationPayClassRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationPayCodeTypeRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationPayFrequencyTypeRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationUnionTypeRefController;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileAdditionalHoursSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileCoverageGroupReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileDepartmentReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileGenderReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileLocationReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayClassReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayCodeReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayFrequencyReference;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFilePayPeriodRule;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.pay.DynamicPayFileUnionTypeReference;
import com.etc.corvetto.ems.pipeline.rqs.PipelineParseDateFormatRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineParsePatternRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileAdditionalHoursSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileCoverageGroupReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileDepartmentReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileEmployerReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileGenderReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileIgnoreRowSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileLocationReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFilePayClassReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFilePayCodeReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFilePayFrequencyReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFilePayPeriodRuleRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileUnionTypeReferenceRequest;
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
 
public class ViewMapperPayFileFormController 
{ 
	@FXML 
	private ListView<HBoxIgnoreRowCell> dfcovIgnoreRowSpecsListView; 
 
	@FXML 
	private ListView<HBoxRuleCell> dfcovPayPeriodRulesListView; 
	 
	@FXML 
	private ListView<HBoxAdditionalHourCell> dfcovAdditionalHoursListView; 
 
	@FXML 
	private Label emprIdLbl; 
 
	@FXML 
	private StackPane emprIdSkPn; 
 
	@FXML 
	private StackPane empCovClsSkPn; 
 
	@FXML 
	private StackPane deptSkPn; 
 
	@FXML 
	private StackPane locSkPn; 
 
	@FXML 
	private StackPane unTpSkPn; 
 
	@FXML 
	private StackPane payFrqSkPn; 
 
	@FXML 
	private StackPane payCodeSkPn; 
 
	@FXML 
	private StackPane payClsSkPn; 
 
	@FXML 
	private StackPane hoursSkPn; 
 
	@FXML 
	private StackPane empFstNmSkPn; 
 
	@FXML 
	private StackPane emplStNmSkPn; 
 
	@FXML 
	private StackPane emplCitySkPn; 
	 
	@FXML 
	private StackPane emplrEmpIdSkPn; 
	 
	@FXML 
	private StackPane emplSsnSkPn; 
	 
	@FXML 
	private StackPane otHrsSkPn; 
	 
	@FXML 
	private StackPane ot3HrsSkPn; 
	 
	@FXML 
	private StackPane payPrdSkPn; 
	 
	@FXML 
	private StackPane rateSkPn; 
	 
	@FXML 
	private StackPane amtSkPn; 
	 
	@FXML 
	private StackPane genderSkPn; 
	 
	@FXML 
	private StackPane emplHrDtSkPn; 
	 
	@FXML 
	private StackPane emplRHrDtSkPn; 
	 
	@FXML 
	private StackPane pdTmOffHrsSkPn; 
	 
	@FXML 
	private StackPane emplMNmSkPn; 
	 
	@FXML 
	private StackPane emplAdLn1SkPn; 
 
	@FXML 
	private StackPane emplStateSkPn; 
 
	@FXML 
	private StackPane emplJobSkPn; 
 
	@FXML 
	private StackPane emailSkPn; 
	 
	@FXML 
	private StackPane ot2HrsSkPn; 
	 
	@FXML 
	private StackPane pPStrtSkPn; 
	 
	@FXML 
	private StackPane pPEndSkPn; 
	 
	@FXML 
	private StackPane payDtSkPn; 
	 
	@FXML 
	private StackPane workDtSkPn; 
	 
	@FXML 
	private StackPane emplTrmDtSkPn; 
	 
	@FXML 
	private StackPane skPyHrsSkPn; 
	 
	@FXML 
	private StackPane holPaySkPn; 
	 
	@FXML 
	private StackPane emplLstNmSkPn; 
	 
	@FXML 
	private StackPane emplAdLn2SkPn; 
	 
	@FXML 
	private StackPane emplZipSkPn; 
 
	@FXML 
	private StackPane empDOBSkPn; 
 
	@FXML 
	private StackPane phoneSkPn; 
	 
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
	private StackPane payCF1SkPn; 
	 
	@FXML 
	private StackPane payCF2SkPn; 
	 
	@FXML 
	private StackPane payCF3SkPn; 
	 
	@FXML 
	private StackPane payCF4SkPn; 
	 
	@FXML 
	private StackPane payCF5SkPn; 
	 
	@FXML 
	private StackPane payCF6SkPn; 
	 
	@FXML 
	private StackPane payCF7SkPn; 
 
	@FXML 
	private TextField dfcovNameLabel; 
	 
	@FXML 
	private TextField dfcovDescriptionLabel; 
	 
	@FXML 
	private TextField dfcovTabIndexLabel; 
	 
	@FXML 
	private TextField dfcovHeaderRowIndexLabel; 
	 
	@FXML 
	private CheckBox dfcovMapEEtoERCheck; 
	 
	@FXML 
	private CheckBox dfcovCreateEmployeeCheck; 
	 
	@FXML 
	private CheckBox dfcovCreatePayPeriod; 
	 
	@FXML 
	private CheckBox dfcovLocked; 
	 
	@FXML 
	private CheckBox dfcovSkipLastRowCheck; 
	 
	@FXML 
	private CheckBox emprId; 
	 
	@FXML 
	private CheckBox empCovCls; 
 
	@FXML 
	private CheckBox dept; 
 
	@FXML 
	private CheckBox loc; 
 
	@FXML 
	private CheckBox unTp; 
 
	@FXML 
	private CheckBox payFrq; 
 
	@FXML 
	private CheckBox payCode; 
 
	@FXML 
	private CheckBox payCls; 
 
	@FXML 
	private CheckBox hours; 
	 
	@FXML 
	private CheckBox empFstNm; 
	 
	@FXML 
	private CheckBox emplStNm; 
	 
	@FXML 
	private CheckBox emplCity; 
	 
	@FXML 
	private CheckBox emplrEmpId; 
	 
	@FXML 
	private CheckBox emplSsn; 
	 
	@FXML 
	private CheckBox otHrs; 
	 
	@FXML 
	private CheckBox ot3Hrs; 
	 
	@FXML 
	private CheckBox payPrd; 
	 
	@FXML 
	private CheckBox rate; 
	 
	@FXML 
	private CheckBox amt; 
	 
	@FXML 
	private CheckBox gender; 
	 
	@FXML 
	private CheckBox emplHrDt; 
	 
	@FXML 
	private CheckBox emplRHrDt; 
	 
	@FXML 
	private CheckBox pdTmOffHrs; 
	 
	@FXML 
	private CheckBox emplMNm; 
	 
	@FXML 
	private CheckBox emplAdLn1; 
 
	@FXML 
	private CheckBox emplState; 
 
	@FXML 
	private CheckBox emplJob; 
 
	@FXML 
	private CheckBox email; 
	 
	@FXML 
	private CheckBox ot2Hrs; 
	 
	@FXML 
	private CheckBox pPStrt; 
	 
	@FXML 
	private CheckBox pPEnd; 
	 
	@FXML 
	private CheckBox payDt; 
	 
	@FXML 
	private CheckBox workDt; 
	 
	@FXML 
	private CheckBox emplTrmDt; 
	 
	@FXML 
	private CheckBox skPyHrs; 
	 
	@FXML 
	private CheckBox holPay; 
	 
	@FXML 
	private CheckBox emplLstNm; 
	 
	@FXML 
	private CheckBox emplAdLn2; 
	 
	@FXML 
	private CheckBox emplZip; 
	 
	@FXML 
	private CheckBox empDOB; 
	 
	@FXML 
	private CheckBox phone; 
 
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
	private CheckBox payCF1; 
	 
	@FXML 
	private CheckBox payCF2; 
	 
	@FXML 
	private CheckBox payCF3; 
	 
	@FXML 
	private CheckBox payCF4; 
	 
	@FXML 
	private CheckBox payCF5; 
	 
	@FXML 
	private CheckBox payCF6; 
	 
	@FXML 
	private CheckBox payCF7; 
	 
	@FXML 
	private TextField emprIdIdx; 
	 
	@FXML 
	private TextField empCovClsIdx; 
	 
	@FXML 
	private TextField deptIdx; 
	 
	@FXML 
	private TextField locIdx; 
 
	@FXML 
	private TextField unTpIdx; 
 
	@FXML 
	private TextField payFrqIdx; 
 
	@FXML 
	private TextField payCodeIdx; 
 
	@FXML 
	private TextField payClsIdx; 
 
	@FXML 
	private TextField hoursIdx; 
	 
	@FXML 
	private TextField empFstNmIdx; 
	 
	@FXML 
	private TextField emplStNmIdx; 
	 
	@FXML 
	private TextField emplCityIdx; 
	 
	@FXML 
	private TextField emplrEmpIdIdx; 
	 
	@FXML 
	private TextField emplSsnIdx; 
	 
	@FXML 
	private TextField otHrsIdx; 
	 
	@FXML 
	private TextField ot3HrsIdx; 
	 
	@FXML 
	private TextField payPrdIdx; 
	 
	@FXML 
	private TextField rateIdx; 
	 
	@FXML 
	private TextField amtIdx; 
	 
	@FXML 
	private TextField genderIdx; 
	 
	@FXML 
	private TextField emplHrDtIdx; 
	 
	@FXML 
	private TextField emplRHrDtIdx; 
	 
	@FXML 
	private TextField pdTmOffHrsIdx; 
	 
	@FXML 
	private TextField emplMNmIdx; 
	 
	@FXML 
	private TextField emplAdLn1Idx; 
 
	@FXML 
	private TextField emplStateIdx; 
 
	@FXML 
	private TextField emplJobIdx; 
 
	@FXML 
	private TextField emailIdx; 
	 
	@FXML 
	private TextField ot2HrsIdx; 
	 
	@FXML 
	private TextField pPStrtIdx; 
	 
	@FXML 
	private TextField pPEndIdx; 
	 
	@FXML 
	private TextField payDtIdx; 
	 
	@FXML 
	private TextField workDtIdx; 
	 
	@FXML 
	private TextField emplTrmDtIdx; 
	 
	@FXML 
	private TextField skPyHrsIdx; 
	 
	@FXML 
	private TextField holPayIdx; 
	 
	@FXML 
	private TextField emplLstNmIdx; 
	 
	@FXML 
	private TextField emplAdLn2Idx; 
	 
	@FXML 
	private TextField emplZipIdx; 
	 
	@FXML 
	private TextField empDOBIdx; 
	 
	@FXML 
	private TextField phoneIdx; 
	 
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
	private TextField payCF1Idx; 
 
	@FXML 
	private TextField payCF2Idx; 
	 
	@FXML 
	private TextField payCF3Idx; 
 
	@FXML 
	private TextField payCF4Idx; 
	 
	@FXML 
	private TextField payCF5Idx; 
 
	@FXML 
	private TextField payCF6Idx; 
	 
	@FXML 
	private TextField payCF7Idx; 
	 
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
	private TextField pyCF1; 
 
	@FXML 
	private TextField pyCF2; 
	 
	@FXML 
	private TextField pyCF3; 
 
	@FXML 
	private TextField pyCF4; 
	 
	@FXML 
	private TextField pyCF5; 
 
	@FXML 
	private TextField pyCF6; 
	 
	@FXML 
	private TextField pyCF7; 
 
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
	private Button deptRefButton; 
	 
	@FXML 
	private Button locRefButton; 
	 
	@FXML 
	private Button unTpRefButton; 
 
	@FXML 
	private Button payFrqRefButton; 
 
	@FXML 
	private Button payCodeRefButton; 
 
	@FXML 
	private Button payClsRefButton; 
	 
	@FXML 
	private Button gendRefButton; 
	 
	@FXML 
	private Button allParseJan; 
	 
	@FXML 
	private Button allParseFeb; 
	 
	@FXML 
	private Button allParseMar; 
	 
	@FXML 
	private Button allParseApr; 
	 
	@FXML 
	private Button allParseMay; 

	@FXML 
	private Button allParseJun; 
	 
	@FXML 
	private Button allParseJul; 
	 
	@FXML 
	private Button allParseAug; 
	 
	@FXML 
	private Button allParseSep; 
	 
	@FXML 
	private Button allParseOct; 
	 
	@FXML 
	private Button allParseNov; 
	 
	@FXML 
	private Button allParseDec; 
	 
	@FXML 
	private Button empFstNmParse; 
	 
	@FXML 
	private Button emplCityParse; 
	 
	@FXML 
	private Button emplrEmpIdParse; 
	 
	@FXML 
	private Button emplSsnParse; 
	 
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
	private Button pPStrtParse; 
	 
	@FXML 
	private Button pPStrtFormat; 
	 
	@FXML 
	private Button pPEndParse; 
	 
	@FXML 
	private Button pPEndFormat; 
	 
	@FXML 
	private Button payDtParse; 
	 
	@FXML 
	private Button payDtFormat; 
	 
	@FXML 
	private Button workDtParse; 
	 
	@FXML 
	private Button workDtFormat; 
	 
	@FXML 
	private Button emplTrmDtFormat; 
	 
	@FXML 
	private Button emplLstNmParse; 
	 
	@FXML 
	private Button emplZipParse; 
	 
	@FXML 
	private Button empDOBFormat; 
	 
	@FXML 
	private Button emplCF1Parse; 
	 
	@FXML 
	private Button emplCF2Parse; 
	 
	@FXML 
	private Button payCF1Parse; 
	 
	@FXML 
	private Button payCF2Parse; 
	 
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
	private Label unTpRefLabel; 
 
	@FXML 
	private Label payFrqRefLabel; 
 
	@FXML 
	private Label payCodeRefLabel; 
 
	@FXML 
	private Label payClsRefLabel; 
 
	@FXML 
	private Label genderRefLabel; 
 
	// read only mode for selected mapper files 
	public boolean readOnly = false; 
	public PipelineFileType fileType; 
 
    List<HBoxRuleCell> ruleList = new ArrayList<>(); 
 
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
			case PAY: 
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
            	DataManager.i().mDynamicPayFileIgnoreRowSpecification = dfcovIgnoreRowSpecsListView.getSelectionModel().getSelectedItem().getPaySpec(); 
            	viewIgnoreRow(); 
            } 
        }); 
 
		//PAYADDITIONALHOURS 
		dfcovAdditionalHoursListView.setOnMouseClicked(mouseClickedEvent ->  
		{ 
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
            { 
        		switch(fileType) 
        		{ 
	        		case PAY: 
	                	DataManager.i().mDynamicPayFileAdditionalHoursSpecification = dfcovAdditionalHoursListView.getSelectionModel().getSelectedItem().getSpec(); 
	        			break; 
	        		default: 
	        			return; 
        		} 
        		viewAdditionalHours(); 
            } 
        }); 
 
		//PAYPERIODRULES 
		dfcovPayPeriodRulesListView.setOnMouseClicked(mouseClickedEvent ->  
		{ 
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
            { 
        		switch(fileType) 
        		{ 
	        		case PAY: 
	                	DataManager.i().mDynamicPayFilePayPeriodRule = dfcovPayPeriodRulesListView.getSelectionModel().getSelectedItem().getRule(); 
	        			break; 
	        		default: 
	        			return; 
        		} 
        		viewPayPeriodRules(); 
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
		deptRefButton.setOnMouseClicked(mouseClickedEvent ->  
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
 
		// UNION TYPE REFERENCE 
		unTpRefButton.setOnMouseClicked(mouseClickedEvent ->  
		{ 
			if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))            
			{ 
            	loadUnionTypeRefForm(); 
            } 
        }); 
 
		// PAY PERIOD FREQUENCY REFERENCE 
		payFrqRefButton.setOnMouseClicked(mouseClickedEvent ->  
		{ 
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
            { 
            	loadPayPeriodFrqRefForm(); 
            } 
        }); 
 
		// PAY CODE REFERENCE 
		payCodeRefButton.setOnMouseClicked(mouseClickedEvent ->  
		{ 
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
            { 
            	loadPayCodeRefForm(); 
            } 
        }); 
 
		// PAY CLASS REFERENCE 
		payClsRefButton.setOnMouseClicked(mouseClickedEvent ->  
		{ 
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
            { 
            	loadPayClassRefForm(); 
            } 
        }); 
 
		// GENDER REFERENCE 
		gendRefButton.setOnMouseClicked(mouseClickedEvent ->  
		{ 
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
            { 
            	loadGenderRefForm(); 
            } 
        }); 
 
		// SET IDX TO DISABLED 
		emprIdIdx.setDisable(true); 
		empCovClsIdx.setDisable(true); 
		deptIdx.setDisable(true); 
		locIdx.setDisable(true); 
		unTpIdx.setDisable(true); 
		payFrqIdx.setDisable(true); 
		payCodeIdx.setDisable(true); 
		payClsIdx.setDisable(true); 
		genderIdx.setDisable(true); 
		hoursIdx.setDisable(true); 
		empFstNmIdx.setDisable(true); 
		emplStNmIdx.setDisable(true); 
		emplCityIdx.setDisable(true); 
		emplrEmpIdIdx.setDisable(true); 
		emplSsnIdx.setDisable(true); 
		otHrsIdx.setDisable(true); 
		ot3HrsIdx.setDisable(true); 
		payPrdIdx.setDisable(true); 
		rateIdx.setDisable(true); 
		amtIdx.setDisable(true); 
		emplHrDtIdx.setDisable(true); 
		emplRHrDtIdx.setDisable(true); 
		pdTmOffHrsIdx.setDisable(true); 
		emplMNmIdx.setDisable(true); 
		emplAdLn1Idx.setDisable(true); 
		emplStateIdx.setDisable(true); 
		emplJobIdx.setDisable(true); 
		emailIdx.setDisable(true); 
		ot2HrsIdx.setDisable(true); 
		pPStrtIdx.setDisable(true); 
		pPEndIdx.setDisable(true); 
		payDtIdx.setDisable(true); 
		workDtIdx.setDisable(true); 
		emplTrmDtIdx.setDisable(true); 
		skPyHrsIdx.setDisable(true); 
		holPayIdx.setDisable(true); 
		emplLstNmIdx.setDisable(true); 
		emplAdLn2Idx.setDisable(true); 
		emplZipIdx.setDisable(true); 
		empDOBIdx.setDisable(true); 
		phoneIdx.setDisable(true); 
		emplCF1Idx.setDisable(true); 
		emplCF2Idx.setDisable(true); 
		emplCF3Idx.setDisable(true); 
		emplCF4Idx.setDisable(true); 
		emplCF5Idx.setDisable(true); 
		payCF1Idx.setDisable(true); 
		payCF2Idx.setDisable(true); 
		payCF3Idx.setDisable(true); 
		payCF4Idx.setDisable(true); 
		payCF5Idx.setDisable(true); 
		payCF6Idx.setDisable(true); 
		payCF7Idx.setDisable(true); 
 
		// SET BUTTON TEXT 
		empFstNmParse.setText("+ Pattern"); 
		empFstNmParse.setFont(Font.font("Veranda", 12.0)); 
		empFstNmParse.setDisable(true); 
		emplCityParse.setText("+ Pattern"); 
		emplCityParse.setFont(Font.font("Veranda", 12.0)); 
		emplCityParse.setDisable(true); 
		emplrEmpIdParse.setText("+ Pattern"); 
		emplrEmpIdParse.setFont(Font.font("Veranda", 12.0)); 
		emplrEmpIdParse.setDisable(true); 
		emplSsnParse.setText("+ Pattern"); 
		emplSsnParse.setFont(Font.font("Veranda", 12.0)); 
		emplSsnParse.setDisable(true); 
		emplHrDtFormat.setText("+ Format"); 
		emplHrDtFormat.setFont(Font.font("Veranda", 12.0)); 
		emplHrDtFormat.setDisable(true); 
		emplRHrDtFormat.setText("+ Format"); 
		emplRHrDtFormat.setFont(Font.font("Veranda", 12.0)); 
		emplRHrDtFormat.setDisable(true); 
		emplMNmParse.setText("+ Pattern"); 
		emplMNmParse.setFont(Font.font("Veranda", 12.0)); 
		emplMNmParse.setDisable(true); 
		emplAdLn1Parse.setText("+ Pattern"); 
		emplAdLn1Parse.setFont(Font.font("Veranda", 12.0)); 
		emplAdLn1Parse.setDisable(true); 
		emplStateParse.setText("+ Pattern"); 
		emplStateParse.setFont(Font.font("Veranda", 12.0)); 
		emplStateParse.setDisable(true); 
		pPStrtParse.setText("+ Pattern"); 
		pPStrtParse.setFont(Font.font("Veranda", 12.0)); 
		pPStrtParse.setDisable(true); 
		pPStrtFormat.setText("+ Format"); 
		pPStrtFormat.setFont(Font.font("Veranda", 12.0)); 
		pPStrtFormat.setDisable(true); 
		pPEndParse.setText("+ Pattern"); 
		pPEndParse.setFont(Font.font("Veranda", 12.0)); 
		pPEndParse.setDisable(true); 
		pPEndFormat.setText("+ Format"); 
		pPEndFormat.setFont(Font.font("Veranda", 12.0)); 
		pPEndFormat.setDisable(true); 
		payDtParse.setText("+ Pattern"); 
		payDtParse.setFont(Font.font("Veranda", 12.0)); 
		payDtParse.setDisable(true); 
		payDtFormat.setText("+ Format"); 
		payDtFormat.setFont(Font.font("Veranda", 12.0)); 
		payDtFormat.setDisable(true); 
		workDtParse.setText("+ Pattern"); 
		workDtParse.setFont(Font.font("Veranda", 12.0)); 
		workDtParse.setDisable(true); 
		workDtFormat.setText("+ Format"); 
		workDtFormat.setFont(Font.font("Veranda", 12.0)); 
		workDtFormat.setDisable(true); 
		emplTrmDtFormat.setText("+ Format"); 
		emplTrmDtFormat.setFont(Font.font("Veranda", 12.0)); 
		emplTrmDtFormat.setDisable(true); 
		emplLstNmParse.setText("+ Pattern"); 
		emplLstNmParse.setFont(Font.font("Veranda", 12.0)); 
		emplLstNmParse.setDisable(true); 
		emplZipParse.setText("+ Pattern"); 
		emplZipParse.setFont(Font.font("Veranda", 12.0)); 
		emplZipParse.setDisable(true); 
		empDOBFormat.setText("+ Format"); 
		empDOBFormat.setFont(Font.font("Veranda", 12.0)); 
		empDOBFormat.setDisable(true); 
		emplCF1Parse.setText("+ Pattern"); 
		emplCF1Parse.setFont(Font.font("Veranda", 12.0)); 
		emplCF1Parse.setDisable(true); 
		emplCF2Parse.setText("+ Pattern"); 
		emplCF2Parse.setFont(Font.font("Veranda", 12.0)); 
		emplCF2Parse.setDisable(true); 
		payCF1Parse.setText("+ Pattern"); 
		payCF1Parse.setFont(Font.font("Veranda", 12.0)); 
		payCF1Parse.setDisable(true); 
		payCF2Parse.setText("+ Pattern"); 
		payCF2Parse.setFont(Font.font("Veranda", 12.0)); 
		payCF2Parse.setDisable(true); 
 
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
		 
        //DATA SECTION 
        mapperFields.put(emprIdSkPn, new MapperField(emprIdSkPn, emprId, emprIdIdx, null, null, emprRefButton, "Employer Id", 80)); 
        mapperFields.put(empCovClsSkPn, new MapperField(empCovClsSkPn, empCovCls, empCovClsIdx, null, null, ccRefButton, "COV Class", 72)); 
        mapperFields.put(deptSkPn, new MapperField(deptSkPn, dept, deptIdx, null, null, deptRefButton, "Department", 80)); 
        mapperFields.put(locSkPn, new MapperField(locSkPn, loc, locIdx, null, null, locRefButton, "Location", 63)); 
        mapperFields.put(unTpSkPn, new MapperField(unTpSkPn, unTp, unTpIdx, null, null, unTpRefButton, "Union TP", 68)); 
        mapperFields.put(payFrqSkPn, new MapperField(payFrqSkPn, payFrq, payFrqIdx, null, null, payFrqRefButton, "Pay Period Freq", 100)); 
        mapperFields.put(payCodeSkPn, new MapperField(payCodeSkPn, payCode, payCodeIdx, null, null, payCodeRefButton, "Comp Type", 77)); 
        mapperFields.put(payClsSkPn, new MapperField(payClsSkPn, payCls, payClsIdx, null, null, payClsRefButton, "Pay Class", 70)); 
        mapperFields.put(genderSkPn, new MapperField(genderSkPn, gender, genderIdx, null, null, gendRefButton, "Gender", 58)); 
        mapperFields.put(hoursSkPn, new MapperField(hoursSkPn, hours, hoursIdx, null, null, null, "Hours", 48)); 
        mapperFields.put(empFstNmSkPn, new MapperField(empFstNmSkPn, empFstNm, empFstNmIdx, empFstNmParse, null, null, "First Name", 78)); 
        mapperFields.put(emplStNmSkPn, new MapperField(emplStNmSkPn, emplStNm, emplStNmIdx, null, null, null, "Strt NMBR", 74)); 
        mapperFields.put(emplCitySkPn, new MapperField(emplCitySkPn, emplCity, emplCityIdx, emplCityParse, null, null, "City", 40)); 
        mapperFields.put(emplrEmpIdSkPn, new MapperField(emplrEmpIdSkPn, emplrEmpId, emplrEmpIdIdx, emplrEmpIdParse, null, null, "ER EE ID", 64)); 
        mapperFields.put(emplSsnSkPn, new MapperField(emplSsnSkPn, emplSsn, emplSsnIdx, emplSsnParse, null, null, "SSN", 42)); 
        mapperFields.put(otHrsSkPn, new MapperField(otHrsSkPn, otHrs, otHrsIdx, null, null, null, "Overtime Hours", 102)); 
        mapperFields.put(ot3HrsSkPn, new MapperField(ot3HrsSkPn, ot3Hrs, ot3HrsIdx, null, null, null, "Overtime 3 Hours", 110)); 
        mapperFields.put(payPrdSkPn, new MapperField(payPrdSkPn, payPrd, payPrdIdx, null, null, null, "Pay Period ID", 87)); 
        mapperFields.put(rateSkPn, new MapperField(rateSkPn, rate, rateIdx, null, null, null, "Rate", 40)); 
        mapperFields.put(amtSkPn, new MapperField(amtSkPn, amt, amtIdx, null, null, null, "Amount", 60)); 
        mapperFields.put(emplHrDtSkPn, new MapperField(emplHrDtSkPn, emplHrDt, emplHrDtIdx, null, emplHrDtFormat, null, "Hire Date", 71)); 
        mapperFields.put(emplRHrDtSkPn, new MapperField(emplRHrDtSkPn, emplRHrDt, emplRHrDtIdx, null, emplRHrDtFormat, null, "Rehire Date", 82)); 
        mapperFields.put(pdTmOffHrsSkPn, new MapperField(pdTmOffHrsSkPn, pdTmOffHrs, pdTmOffHrsIdx, null, null, null, "Paid Time Off Hours", 122)); 
        mapperFields.put(emplMNmSkPn, new MapperField(emplMNmSkPn, emplMNm, emplMNmIdx, emplMNmParse, null, null, "Mid Name", 75)); 
        mapperFields.put(emplAdLn1SkPn, new MapperField(emplAdLn1SkPn, emplAdLn1, emplAdLn1Idx, emplAdLn1Parse, null, null, "ADD LN 1", 71)); 
        mapperFields.put(emplStateSkPn, new MapperField(emplStateSkPn, emplState, emplStateIdx, emplStateParse, null, null, "State", 46)); 
        mapperFields.put(emplJobSkPn, new MapperField(emplJobSkPn, emplJob, emplJobIdx, null, null, null, "Job Title", 63)); 
        mapperFields.put(emailSkPn, new MapperField(emailSkPn, email, emailIdx, null, null, null, "Email", 55)); 
        mapperFields.put(ot2HrsSkPn, new MapperField(ot2HrsSkPn, ot2Hrs, ot2HrsIdx, null, null, null, "Overtime 2 Hours", 110)); 
        mapperFields.put(pPStrtSkPn, new MapperField(pPStrtSkPn, pPStrt, pPStrtIdx, pPStrtParse, pPStrtFormat, null, "Pay Period Start", 100)); 
        mapperFields.put(pPEndSkPn, new MapperField(pPEndSkPn, pPEnd, pPEndIdx, pPEndParse, pPEndFormat, null, "Pay Period End", 97)); 
        mapperFields.put(payDtSkPn, new MapperField(payDtSkPn, payDt, payDtIdx, payDtParse, payDtFormat, null, "Pay Date", 65)); 
        mapperFields.put(workDtSkPn, new MapperField(workDtSkPn, workDt, workDtIdx, workDtParse, workDtFormat, null, "Work Date", 80)); 
        mapperFields.put(emplTrmDtSkPn, new MapperField(emplTrmDtSkPn, emplTrmDt, emplTrmDtIdx, null, emplTrmDtFormat, null, "Term Date", 75)); 
        mapperFields.put(skPyHrsSkPn, new MapperField(skPyHrsSkPn, skPyHrs, skPyHrsIdx, null, null, null, "Sick Pay Hours", 95)); 
        mapperFields.put(holPaySkPn, new MapperField(holPaySkPn, holPay, holPayIdx, null, null, null, "Holiday Pay Hours", 115)); 
        mapperFields.put(emplLstNmSkPn, new MapperField(emplLstNmSkPn, emplLstNm, emplLstNmIdx, emplLstNmParse, null, null, "Last Name", 76)); 
        mapperFields.put(emplAdLn2SkPn, new MapperField(emplAdLn2SkPn, emplAdLn2, emplAdLn2Idx, null, null, null, "ADD LN 2", 71)); 
        mapperFields.put(emplZipSkPn, new MapperField(emplZipSkPn, emplZip, emplZipIdx, emplZipParse, null, null, "Zip", 40)); 
        mapperFields.put(empDOBSkPn, new MapperField(empDOBSkPn, empDOB, empDOBIdx, null, empDOBFormat, null, "DOB", 42)); 
        mapperFields.put(phoneSkPn, new MapperField(phoneSkPn, phone, phoneIdx, null, null, null, "Phone", 55)); 
        mapperFields.put(emplCF1SkPn, new MapperField(emplCF1SkPn, emplCF1, emplCF1Idx, emplCF1Parse, null, null, "CF 1", 42)); 
        mapperFields.put(emplCF2SkPn, new MapperField(emplCF2SkPn, emplCF2, emplCF2Idx, emplCF2Parse, null, null, "CF 2", 42)); 
        mapperFields.put(emplCF3SkPn, new MapperField(emplCF3SkPn, emplCF3, emplCF3Idx, null, null, null, "CF 3", 42)); 
        mapperFields.put(emplCF4SkPn, new MapperField(emplCF4SkPn, emplCF4, emplCF4Idx, null, null, null, "CF 4", 42)); 
        mapperFields.put(emplCF5SkPn, new MapperField(emplCF5SkPn, emplCF5, emplCF5Idx, null, null, null, "CF 5", 42)); 
        mapperFields.put(payCF1SkPn, new MapperField(payCF1SkPn, payCF1, payCF1Idx, payCF1Parse, null, null, "CPF 1", 48)); 
        mapperFields.put(payCF2SkPn, new MapperField(payCF2SkPn, payCF2, payCF2Idx, payCF2Parse, null, null, "CPF 2", 48)); 
        mapperFields.put(payCF3SkPn, new MapperField(payCF3SkPn, payCF3, payCF3Idx, null, null, null, "CPF 3", 48)); 
        mapperFields.put(payCF4SkPn, new MapperField(payCF4SkPn, payCF4, payCF4Idx, null, null, null, "CPF 4", 48)); 
        mapperFields.put(payCF5SkPn, new MapperField(payCF5SkPn, payCF5, payCF5Idx, null, null, null, "CPF 5", 48)); 
        mapperFields.put(payCF6SkPn, new MapperField(payCF6SkPn, payCF6, payCF6Idx, null, null, null, "CPF 6", 48)); 
        mapperFields.put(payCF7SkPn, new MapperField(payCF7SkPn, payCF7, payCF7Idx, null, null, null, "CPF 7", 48)); 
 
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
        		DynamicPayFileSpecificationRequest payReq = new DynamicPayFileSpecificationRequest(DataManager.i().mDynamicPayFileSpecification); 
        		payReq.setId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
        		
        		DataManager.i().mDynamicPayFileSpecification = AdminPersistenceManager.getInstance().get(payReq);
        		
        		EtcAdmin.i().setProgress(.75); 
                return null; 
            } 
        }; 
        task.setOnSucceeded(e -> updateFileData()); 
    	task.setOnFailed(e -> updateFileData()); 
        new Thread(task).start(); 
	} 
 
	private void loadAdditionalHours()  
	{ 
		dfcovAdditionalHoursListView.getItems().clear(); 
		
		try {
			DynamicPayFileAdditionalHoursSpecificationRequest request = new DynamicPayFileAdditionalHoursSpecificationRequest(); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId());
			DataManager.i().mDynamicPayFileAdditionalHoursSpecifications = AdminPersistenceManager.getInstance().getAll(request);
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }

		if(DataManager.i().mDynamicPayFileAdditionalHoursSpecifications != null && DataManager.i().mDynamicPayFileAdditionalHoursSpecifications.size() > 0) 
		{ 
		    List<HBoxAdditionalHourCell> hoursList = new ArrayList<HBoxAdditionalHourCell>(); 
			for(DynamicPayFileAdditionalHoursSpecification spec : DataManager.i().mDynamicPayFileAdditionalHoursSpecifications) 
			{
				if(spec == null) continue; 
				if(spec.isActive() == true && spec.isDeleted() == false)
				{
					hoursList.add(new HBoxAdditionalHourCell(spec)); 
				}
		    };	 
			ObservableList<HBoxAdditionalHourCell> myObservableHoursList = FXCollections.observableList(hoursList); 
			dfcovAdditionalHoursListView.setItems(myObservableHoursList);		 
		}  
	} 

	private void loadRuleData()  
	{ 
		EtcAdmin.i().setStatusMessage("loading Rule Data...");	 
		EtcAdmin.i().setProgress(.01); 
 
		dfcovPayPeriodRulesListView.getItems().clear(); 
		ruleList.clear(); 
 
		Task<Void> task = new Task<Void>()  
		{
            @Override 
            protected Void call() throws Exception  
            { 
        		DynamicPayFileSpecification fSpec = DataManager.i().mDynamicPayFileSpecification; 

    			//Load PayPeriod Rules
    			DynamicPayFilePayPeriodRuleRequest request = new DynamicPayFilePayPeriodRuleRequest(); 
    			request.setId(fSpec.getId()); 
    			request.setSpecificationId(fSpec.getId()); 
    			DataManager.i().mDynamicPayFilePayPeriodRules = AdminPersistenceManager.getInstance().getAll(request); 

        		if(fSpec != null && DataManager.i().mDynamicPayFilePayPeriodRules != null && DataManager.i().mDynamicPayFilePayPeriodRules.size() > 0) 
        		{ 
        		    double counter = 0f; 
        		    double count = DataManager.i().mDynamicPayFilePayPeriodRules.size(); 
        		    
        			for(DynamicPayFilePayPeriodRule rule : DataManager.i().mDynamicPayFilePayPeriodRules)  
        			{
        				if(rule == null) continue; 
        				 
	        				// and add it 
	        				if(rule.isActive() == true && rule.isDeleted() == false) 
	        				{
	        					ruleList.add(new HBoxRuleCell(rule)); 
	        				} 
        				// update the progress indicator 
        				counter++; 
 
        				EtcAdmin.i().setProgress(counter/count); 
        			};	 
        		} 
                return null; 
            } 
        }; 
    	task.setOnSucceeded(e -> loadRules()); 
    	task.setOnFailed(e -> loadRules()); 
        new Thread(task).start(); 
	} 
 
	private void loadRules() 
	{ 
		ObservableList<HBoxRuleCell> myObservableDepartmentList = FXCollections.observableList(ruleList); 
		dfcovPayPeriodRulesListView.setItems(myObservableDepartmentList); 
 
		EtcAdmin.i().setStatusMessage("Ready"); 
		EtcAdmin.i().setProgress(0); 
	} 
 
	private void loadIgnoreRows(DynamicPayFileSpecification fSpec) 
	{ 
		try {
			DynamicPayFileIgnoreRowSpecificationRequest request = new DynamicPayFileIgnoreRowSpecificationRequest(DataManager.i().mDynamicPayFileIgnoreRowSpecification); 
			request.setSpecificationId(fSpec.getId()); 
			DataManager.i().mDynamicPayFileIgnoreRowSpecifications = AdminPersistenceManager.getInstance().getAll(request);
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); } 
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		 
	    List<HBoxIgnoreRowCell> ignoreRowList = new ArrayList<>(); 
	     
		for(DynamicPayFileIgnoreRowSpecification spec : DataManager.i().mDynamicPayFileIgnoreRowSpecifications) 
		{ 
		    if(spec.isActive() == true && spec.isDeleted() == false) 
		    { 
				ignoreRowList.add(new HBoxIgnoreRowCell(spec)); 
		    } 
		} 
		ObservableList<HBoxIgnoreRowCell> myObservableDepartmentList = FXCollections.observableList(ignoreRowList); 
		dfcovIgnoreRowSpecsListView.setItems(myObservableDepartmentList); 
	} 
 
	//Handles the data source selection 
	private void updateFileData() 
	{ 
		updatePayFileData(); 
		loadIgnoreRows(DataManager.i().mDynamicPayFileSpecification); 
		loadAdditionalHours(); 
		loadRuleData(); 
		checkReadOnly(); 
        EtcAdmin.i().setStatusMessage("Ready"); 
        EtcAdmin.i().setProgress(0); 
	} 
 
	/* 
	 * DynamicCoverageFileSpecification Support 
	 */ 
	private void updatePayFileData() 
	{ 
		DynamicPayFileSpecification fSpec = DataManager.i().mDynamicPayFileSpecification; 

		if(fSpec != null)  
		{ 
			readOnly = fSpec.isLocked();
			Utils.updateControl(dfcovNameLabel,fSpec.getName()); 
			Utils.updateControl(dfcovTabIndexLabel,String.valueOf(fSpec.getTabIndex())); 
			Utils.updateControl(dfcovHeaderRowIndexLabel,String.valueOf(fSpec.getHeaderRowIndex())); 
			Utils.updateControl(dfcovCreateEmployeeCheck,fSpec.isCreateEmployee()); 
			dfcovMapEEtoERCheck.setVisible(true); 
			Utils.updateControl(dfcovMapEEtoERCheck,fSpec.isMapEEtoER()); 
			dfcovCreateEmployeeCheck.setVisible(true); 
			Utils.updateControl(dfcovCreateEmployeeCheck,fSpec.isCreateEmployee()); 
			Utils.updateControl(dfcovSkipLastRowCheck, fSpec.isSkipLastRow()); 
			Utils.updateControl(dfcovCreatePayPeriod, fSpec.isCreatePayPeriod()); 
			Utils.updateControl(dfcovLocked, fSpec.isLocked()); 
 
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
			Utils.updateControl(pyCF1, fSpec.getPayCfld1Name()); 
			Utils.updateControl(pyCF2, fSpec.getPayCfld2Name()); 
			Utils.updateControl(pyCF3, fSpec.getPayCfld3Name()); 
			Utils.updateControl(pyCF4, fSpec.getPayCfld4Name()); 
			Utils.updateControl(pyCF5, fSpec.getPayCfld5Name()); 
			Utils.updateControl(pyCF6, fSpec.getPayCfld6Name()); 
			Utils.updateControl(pyCF7, fSpec.getPayCfld7Name()); 
 
			//reference counts 
			setEmpRefCount(getPayFileEmployerRefCount()); 
			setCvgRefCount(getPayFileCvgClassRefCount()); 
			setDeptRefCount(getPayFileDepartmentRefCount()); 
			setLocRefCount(getPayFileLocationRefCount()); 
			setUnTpRefCount(getPayFileUnionTypeRefCount()); 
			setPpFreqRefCount(getPayFilePayPeriodFrequencyRefCount()); 
			setPayCodeRefCount(getPayFilePayCodeTypeRefCount()); 
			setPayClassRefCount(getPayFilePayClassRefCount()); 
			setGenderRefCount(getPayFileGenderRefCount()); 
 
			//update the column field table 
			loadFieldSet1(fSpec); 
		} 
	} 
 
	//Generate the coverage spec type 
	private void loadFieldSet1(DynamicPayFileSpecification fSpec) 
	{ 
		//EMPLOYEE SECTION 
		enableFieldSet(fSpec.isErCol(), fSpec.getErColIndex(), emprIdSkPn, null, null); 
		enableFieldSet(fSpec.isCvgGroupCol(), fSpec.getCvgGroupColIndex(), empCovClsSkPn, null, null); 
		enableFieldSet(fSpec.isDeptCol(), fSpec.getDeptColIndex(), deptSkPn, null, null); 
		enableFieldSet(fSpec.isLocCol(), fSpec.getLocColIndex(), locSkPn, null, null); 
		enableFieldSet(fSpec.isUnionTypeCol(), fSpec.getUnionTypeColIndex(), unTpSkPn, null, null); 
		enableFieldSet(fSpec.isPpdFreqCol(), fSpec.getPpdFreqColIndex(), payFrqSkPn, null, null); 
		enableFieldSet(fSpec.isPayCodeCol(), fSpec.getPayCodeColIndex(), payCodeSkPn, null, null); 
		enableFieldSet(fSpec.isPayClassCol(), fSpec.getPayClassColIndex(), payClsSkPn, null, null); 
		enableFieldSet(fSpec.isGenderCol(), fSpec.getGenderColIndex(), genderSkPn, null, null); 
		enableFieldSet(fSpec.isHrsCol(), fSpec.getHrsColIndex(), hoursSkPn, null, null); 
		enableFieldSet(fSpec.isfNameCol(), fSpec.getfNameColIndex(), empFstNmSkPn, fSpec.getfNameParsePatternId(), null); 
		enableFieldSet(fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), emplStNmSkPn, null, null); 
		enableFieldSet(fSpec.isCityCol(), fSpec.getCityColIndex(), emplCitySkPn, fSpec.getCityParsePatternId(), null); 
		enableFieldSet(fSpec.isErRefCol(), fSpec.getErRefColIndex(), emplrEmpIdSkPn, fSpec.getErRefParsePatternId(), null); 
		enableFieldSet(fSpec.isSsnCol(), fSpec.getSsnColIndex(), emplSsnSkPn, fSpec.getSsnParsePatternId(), null); 
		enableFieldSet(fSpec.isOtHrsCol(), fSpec.getOtHrsColIndex(), otHrsSkPn, null, null); 
		enableFieldSet(fSpec.isOtHrs3Col(), fSpec.getOtHrs3ColIndex(), ot3HrsSkPn, null, null); 
		enableFieldSet(fSpec.isPpdErRefCol(), fSpec.getPpdErRefColIndex(), payPrdSkPn, null, null); 
		enableFieldSet(fSpec.isRateCol(), fSpec.getRateColIndex(), rateSkPn, null, null); 
		enableFieldSet(fSpec.isAmtCol(), fSpec.getAmtColIndex(), amtSkPn, null, null); 
		enableFieldSet(fSpec.isHireDtCol(), fSpec.getHireDtColIndex(), emplHrDtSkPn, null, fSpec.getHireDtFormatId()); 
		enableFieldSet(fSpec.isRhireDtCol(), fSpec.getRhireDtColIndex(), emplRHrDtSkPn, null, fSpec.getRhireDtFormatId()); 
		enableFieldSet(fSpec.isPtoHrsCol(), fSpec.getPtoHrsColIndex(), pdTmOffHrsSkPn, null, null); 
		enableFieldSet(fSpec.ismNameCol(), fSpec.getmNameColIndex(), emplMNmSkPn, fSpec.getmNameParsePatternId(), null); 
		enableFieldSet(fSpec.isStreetCol(), fSpec.getStreetColIndex(), emplAdLn1SkPn, fSpec.getStreetParsePatternId(), null); 
		enableFieldSet(fSpec.isStateCol(), fSpec.getStateColIndex(), emplStateSkPn, fSpec.getStateParsePatternId(), null); 
		enableFieldSet(fSpec.isJobTtlCol(), fSpec.getJobTtlColIndex(), emplJobSkPn, null, null); 
		enableFieldSet(fSpec.isEmlCol(), fSpec.getEmlColIndex(), emailSkPn, null, null); 
		enableFieldSet(fSpec.isOtHrs2Col(), fSpec.getOtHrs2ColIndex(), ot2HrsSkPn, null, null); 
		enableFieldSet(fSpec.isPpdStartDtCol(), fSpec.getPpdStartDtColIndex(), pPStrtSkPn, fSpec.getPpdStartDtParsePatternId(), fSpec.getPpdStartDtFormatId()); 
		enableFieldSet(fSpec.isPpdEndDtCol(), fSpec.getPpdEndDtColIndex(), pPEndSkPn, fSpec.getPpdEndDtParsePatternId(), fSpec.getPpdEndDtFormatId()); 
		enableFieldSet(fSpec.isPayDtCol(), fSpec.getPayDtColIndex(), payDtSkPn, fSpec.getPayDtParsePatternId(), fSpec.getPayDtFormatId()); 
		enableFieldSet(fSpec.isWorkDtCol(), fSpec.getWorkDtColIndex(), workDtSkPn, fSpec.getWorkDtParsePatternId(), fSpec.getWorkDtFormatId()); 
		enableFieldSet(fSpec.isTermDtCol(), fSpec.getTermDtColIndex(), emplTrmDtSkPn, null, fSpec.getTermDtFormatId()); 
		enableFieldSet(fSpec.isSickHrsCol(), fSpec.getSickHrsColIndex(), skPyHrsSkPn, null, null); 
		enableFieldSet(fSpec.isHolidayHrsCol(), fSpec.getHolidayHrsColIndex(), holPaySkPn, null, null); 
		enableFieldSet(fSpec.islNameCol(), fSpec.getlNameColIndex(), emplLstNmSkPn, fSpec.getlNameParsePatternId(), null); 
		enableFieldSet(fSpec.isLin2Col(), fSpec.getLin2ColIndex(), emplAdLn2SkPn, null, null); 
		enableFieldSet(fSpec.isZipCol(), fSpec.getZipColIndex(), emplZipSkPn, fSpec.getZipParsePatternId(), null); 
		enableFieldSet(fSpec.isDobCol(), fSpec.getDobColIndex(), empDOBSkPn, null, fSpec.getDobFormatId()); 
		enableFieldSet(fSpec.isPhoneCol(), fSpec.getPhoneColIndex(), phoneSkPn, null, null); 
		enableFieldSet(fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), emplCF1SkPn, fSpec.getCfld1ParsePatternId(), null); 
		enableFieldSet(fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), emplCF2SkPn, fSpec.getCfld2ParsePatternId(), null); 
		enableFieldSet(fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), emplCF3SkPn, null, null); 
		enableFieldSet(fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), emplCF4SkPn, null, null); 
		enableFieldSet(fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), emplCF5SkPn, null, null); 
		enableFieldSet(fSpec.isPayCfld1Col(), fSpec.getPayCfld1ColIndex(), payCF1SkPn, fSpec.getPayCfld1ParsePatternId(), null); 
		enableFieldSet(fSpec.isPayCfld2Col(), fSpec.getPayCfld2ColIndex(), payCF2SkPn, fSpec.getPayCfld2ParsePatternId(), null); 
		enableFieldSet(fSpec.isPayCfld3Col(), fSpec.getPayCfld3ColIndex(), payCF3SkPn, null, null); 
		enableFieldSet(fSpec.isPayCfld4Col(), fSpec.getPayCfld4ColIndex(), payCF4SkPn, null, null); 
		enableFieldSet(fSpec.isPayCfld5Col(), fSpec.getPayCfld5ColIndex(), payCF5SkPn, null, null); 
		enableFieldSet(fSpec.isPayCfld6Col(), fSpec.getPayCfld6ColIndex(), payCF6SkPn, null, null); 
		enableFieldSet(fSpec.isPayCfld7Col(), fSpec.getPayCfld7ColIndex(), payCF7SkPn, null, null); 
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
		DynamicPayFileSpecification fSpec = DataManager.i().mDynamicPayFileSpecification; 
 
		if(fld.getParsePatternId() != null) { fld.setTitle(fld.getTitle().replace(" (P:" + fld.getParsePatternId().toString() + ")", "")); fld.setParsePatternId(null); } 
		if(fld.getDateFormatId() != null) { fld.setTitle(fld.getTitle().replace(" (F:" + fld.getDateFormatId().toString() + ")", "")); fld.setDateFormatId(null); } 
 
		if(emprId.isSelected() == false){ fSpec.setErCol(false); fSpec.setErColIndex(0); } 
		if(empCovCls.isSelected() == false){ fSpec.setCvgGroupCol(false); fSpec.setCvgGroupColIndex(0); } 
		if(dept.isSelected() == false){ fSpec.setDeptCol(false); fSpec.setDeptColIndex(0); } 
		if(loc.isSelected() == false){ fSpec.setLocCol(false); fSpec.setLocColIndex(0); } 
		if(unTp.isSelected() == false){ fSpec.setUnionTypeCol(false); fSpec.setUnionTypeColIndex(0); } 
		if(payFrq.isSelected() == false){ fSpec.setPpdFreqCol(false); fSpec.setPpdFreqColIndex(0); } 
		if(payCode.isSelected() == false){ fSpec.setPayCodeCol(false); fSpec.setPayCodeColIndex(0); } 
		if(payCls.isSelected() == false){ fSpec.setPayClassCol(false); fSpec.setPayClassColIndex(0); } 
		if(gender.isSelected() == false){ fSpec.setGenderCol(false); fSpec.setGenderColIndex(0); } 
		if(hours.isSelected() == false){ fSpec.setHrsCol(false); fSpec.setHrsColIndex(0); } 
		if(empFstNm.isSelected() == false){ fSpec.setfNameParsePattern(null); fSpec.setfNameCol(false); fSpec.setfNameColIndex(0);  
		   empFstNmParse.setFont(Font.font("Veranda", 12.0)); empFstNmParse.setText("+ Pattern"); } 
		if(emplStNm.isSelected() == false){ fSpec.setStreetNumCol(false); fSpec.setStreetNumColIndex(0); } 
		if(emplCity.isSelected() == false){ fSpec.setCityCol(false); fSpec.setCityColIndex(0); fSpec.setCityParsePattern(null); 
		   emplCityParse.setFont(Font.font("Veranda", 12.0)); emplCityParse.setText("+ Pattern"); } 
		if(emplrEmpId.isSelected() == false){ fSpec.setErRefCol(false); fSpec.setErRefColIndex(0); fSpec.setErRefParsePattern(null); 
		   emplrEmpIdParse.setFont(Font.font("Veranda", 12.0)); emplrEmpIdParse.setText("+ Pattern"); } 
		if(emplSsn.isSelected() == false){ fSpec.setSsnCol(false); fSpec.setSsnColIndex(0); fSpec.setSsnParsePattern(null); 
		   emplSsnParse.setFont(Font.font("Veranda", 12.0)); emplSsnParse.setText("+ Pattern"); } 
		if(otHrs.isSelected() == false){ fSpec.setOtHrsCol(false); fSpec.setOtHrsColIndex(0); } 
		if(ot3Hrs.isSelected() == false){ fSpec.setOtHrs3Col(false); fSpec.setOtHrs3ColIndex(0); } 
		if(payPrd.isSelected() == false){ fSpec.setPpdErRefCol(false); fSpec.setPpdErRefColIndex(0); } 
		if(rate.isSelected() == false){ fSpec.setRateCol(false); fSpec.setRateColIndex(0); } 
		if(amt.isSelected() == false){ fSpec.setAmtCol(false); fSpec.setAmtColIndex(0); } 
		if(emplHrDt.isSelected() == false){ fSpec.setHireDtCol(false); fSpec.setHireDtColIndex(0); fSpec.setHireDtFormat(null); 
		   emplHrDtFormat.setFont(Font.font("Veranda", 12.0)); emplHrDtFormat.setText("+ Format"); } 
		if(emplRHrDt.isSelected() == false){ fSpec.setRhireDtCol(false); fSpec.setRhireDtColIndex(0); fSpec.setRhireDtFormat(null); 
		   emplRHrDtFormat.setFont(Font.font("Veranda", 12.0)); emplRHrDtFormat.setText("+ Format"); } 
		if(pdTmOffHrs.isSelected() == false){ fSpec.setPtoHrsCol(false); fSpec.setPtoHrsColIndex(0); } 
		if(emplMNm.isSelected() == false){ fSpec.setmNameCol(false); fSpec.setmNameColIndex(0); fSpec.setmNameParsePattern(null); 
		   emplMNmParse.setFont(Font.font("Veranda", 12.0)); emplMNmParse.setText("+ Pattern"); } 
		if(emplAdLn1.isSelected() == false){ fSpec.setStreetCol(false); fSpec.setStreetColIndex(0); fSpec.setStreetParsePattern(null); 
		   emplAdLn1Parse.setFont(Font.font("Veranda", 12.0)); emplAdLn1Parse.setText("+ Pattern"); } 
		if(emplState.isSelected() == false){ fSpec.setStateCol(false); fSpec.setStateColIndex(0); fSpec.setStateParsePattern(null); 
		   emplStateParse.setFont(Font.font("Veranda", 12.0)); emplStateParse.setText("+ Pattern"); } 
		if(emplJob.isSelected() == false){ fSpec.setJobTtlCol(false); fSpec.setJobTtlColIndex(0); } 
		if(email.isSelected() == false){ fSpec.setEmlCol(false); fSpec.setEmlColIndex(0); } 
		if(ot2Hrs.isSelected() == false){ fSpec.setOtHrs2Col(false); fSpec.setOtHrs2ColIndex(0); } 
		if(pPStrt.isSelected() == false){ fSpec.setPpdStartDtCol(false); fSpec.setPpdStartDtColIndex(0); fSpec.setPpdStartDtParsePattern(null); 
		   pPStrtParse.setFont(Font.font("Veranda", 12.0)); pPStrtParse.setText("+ Pattern"); fSpec.setPpdStartDtFormat(null); 
		   pPStrtFormat.setFont(Font.font("Veranda", 12.0)); pPStrtFormat.setText("+ Format");} 
		if(pPEnd.isSelected() == false){ fSpec.setPpdEndDtCol(false); fSpec.setPpdEndDtColIndex(0); fSpec.setPpdEndDtParsePattern(null); 
		   pPEndParse.setFont(Font.font("Veranda", 12.0)); pPEndParse.setText("+ Pattern"); fSpec.setPpdEndDtFormat(null); 
		   pPEndFormat.setFont(Font.font("Veranda", 12.0)); pPEndFormat.setText("+ Format");} 
		if(payDt.isSelected() == false){ fSpec.setPayDtCol(false); fSpec.setPayDtColIndex(0); fSpec.setPayDtParsePattern(null); 
		   payDtParse.setFont(Font.font("Veranda", 12.0)); payDtParse.setText("+ Pattern"); fSpec.setPayDtFormat(null); 
		   payDtFormat.setFont(Font.font("Veranda", 12.0)); payDtFormat.setText("+ Format");} 
		if(workDt.isSelected() == false){ fSpec.setWorkDtCol(false); fSpec.setWorkDtColIndex(0); fSpec.setWorkDtParsePattern(null); 
		   workDtParse.setFont(Font.font("Veranda", 12.0)); workDtParse.setText("+ Pattern"); fSpec.setWorkDtFormat(null); 
		   workDtFormat.setFont(Font.font("Veranda", 12.0)); workDtFormat.setText("+ Format");} 
		if(emplTrmDt.isSelected() == false){ fSpec.setTermDtCol(false); fSpec.setTermDtColIndex(0); fSpec.setTermDtFormat(null); 
		   emplTrmDtFormat.setFont(Font.font("Veranda", 12.0)); emplTrmDtFormat.setText("+ Format"); } 
		if(skPyHrs.isSelected() == false){ fSpec.setSickHrsCol(false); fSpec.setSickHrsColIndex(0); } 
		if(holPay.isSelected() == false){ fSpec.setHolidayHrsCol(false); fSpec.setHolidayHrsColIndex(0); } 
		if(emplLstNm.isSelected() == false){ fSpec.setlNameCol(false); fSpec.setlNameColIndex(0); fSpec.setlNameParsePattern(null); 
		   emplLstNmParse.setFont(Font.font("Veranda", 12.0)); emplLstNmParse.setText("+ Pattern"); } 
		if(emplAdLn2.isSelected() == false){ fSpec.setLin2Col(false); fSpec.setLin2ColIndex(0); } 
		if(emplZip.isSelected() == false){ fSpec.setZipCol(false); fSpec.setZipColIndex(0); fSpec.setZipParsePattern(null); 
		   emplZipParse.setFont(Font.font("Veranda", 12.0)); emplZipParse.setText("+ Pattern"); } 
		if(empDOB.isSelected() == false){ fSpec.setDobCol(false); fSpec.setDobColIndex(0); fSpec.setDobFormat(null); 
		   empDOBFormat.setFont(Font.font("Veranda", 12.0)); empDOBFormat.setText("+ Format");} 
		if(phone.isSelected() == false){ fSpec.setPhoneCol(false); fSpec.setPhoneColIndex(0); } 
		if(emplCF1.isSelected() == false){ fSpec.setCfld1Col(false); fSpec.setCfld1ColIndex(0); fSpec.setCfld1ParsePattern(null); 
		   emplCF1Parse.setFont(Font.font("Veranda", 12.0)); emplCF1Parse.setText("+ Pattern"); eeCF1.setText(""); } 
		if(emplCF2.isSelected() == false){ fSpec.setCfld2Col(false); fSpec.setCfld2ColIndex(0); fSpec.setCfld2ParsePattern(null); 
		   emplCF2Parse.setFont(Font.font("Veranda", 12.0)); emplCF2Parse.setText("+ Pattern"); eeCF2.setText(""); } 
		if(emplCF3.isSelected() == false){ fSpec.setCfld3Col(false); fSpec.setCfld3ColIndex(0); eeCF3.setText(""); } 
		if(emplCF4.isSelected() == false){ fSpec.setCfld4Col(false); fSpec.setCfld4ColIndex(0); eeCF4.setText(""); } 
		if(emplCF5.isSelected() == false){ fSpec.setCfld5Col(false); fSpec.setCfld5ColIndex(0); eeCF5.setText(""); }
		if(payCF1.isSelected() == false){ fSpec.setPayCfld1Col(false); fSpec.setPayCfld1ColIndex(0); fSpec.setPayCfld1ParsePattern(null); 
		   payCF1Parse.setFont(Font.font("Veranda", 12.0)); payCF1Parse.setText("+ Pattern"); payCF1.setText(""); } 
		if(payCF2.isSelected() == false){ fSpec.setPayCfld2Col(false); fSpec.setPayCfld2ColIndex(0); fSpec.setPayCfld2ParsePattern(null); 
		   payCF2Parse.setFont(Font.font("Veranda", 12.0)); payCF2Parse.setText("+ Pattern"); payCF2.setText(""); } 
		if(payCF3.isSelected() == false){ fSpec.setPayCfld3Col(false); fSpec.setPayCfld3ColIndex(0); payCF3.setText(""); } 
		if(payCF4.isSelected() == false){ fSpec.setPayCfld4Col(false); fSpec.setPayCfld4ColIndex(0); payCF4.setText(""); } 
		if(payCF5.isSelected() == false){ fSpec.setPayCfld5Col(false); fSpec.setPayCfld5ColIndex(0); payCF5.setText(""); } 
		if(payCF6.isSelected() == false){ fSpec.setPayCfld6Col(false); fSpec.setPayCfld6ColIndex(0); payCF6.setText(""); }
		if(payCF7.isSelected() == false){ fSpec.setPayCfld7Col(false); fSpec.setPayCfld7ColIndex(0); payCF7.setText(""); } 
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
		DynamicPayFileSpecification fSpec = DataManager.i().mDynamicPayFileSpecification; 
 
		if(eeCF1 != null){ fSpec.setCfld1Name(eeCF1.getText().toString()); } 
		if(eeCF2 != null){ fSpec.setCfld2Name(eeCF2.getText().toString()); } 
		if(eeCF3 != null){ fSpec.setCfld3Name(eeCF3.getText().toString()); } 
		if(eeCF4 != null){ fSpec.setCfld4Name(eeCF4.getText().toString()); } 
		if(eeCF5 != null){ fSpec.setCfld5Name(eeCF5.getText().toString()); } 
		if(payCF1 != null){ fSpec.setPayCfld1Name(pyCF1.getText().toString()); } 
		if(payCF2 != null){ fSpec.setPayCfld2Name(pyCF2.getText().toString()); } 
		if(payCF3 != null){ fSpec.setPayCfld3Name(pyCF3.getText().toString()); } 
		if(payCF4 != null){ fSpec.setPayCfld4Name(pyCF4.getText().toString()); } 
		if(payCF5 != null){ fSpec.setPayCfld5Name(pyCF5.getText().toString()); } 
		if(payCF6 != null){ fSpec.setPayCfld6Name(pyCF6.getText().toString()); } 
		if(payCF7 != null){ fSpec.setPayCfld7Name(pyCF7.getText().toString()); } 
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
		dfcovMapEEtoERCheck.setDisable(readOnly); 
		dfcovClearFormButton.setDisable(readOnly); 
		dfcovSaveChangesButton.setDisable(readOnly); 
	} 
 
	private int getPayFileEmployerRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicPayFileEmployerReferenceRequest request = new DynamicPayFileEmployerReferenceRequest(DataManager.i().mDynamicPayFileEmployerReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicPayFileSpecification.setErReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		
		for(DynamicPayFileEmployerReference erRef : DataManager.i().mDynamicPayFileSpecification.getErReferences()) 
		{ 
			if(erRef.isActive() == true && erRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getPayFileCvgClassRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicPayFileCoverageGroupReferenceRequest request = new DynamicPayFileCoverageGroupReferenceRequest(DataManager.i().mDynamicPayFileCoverageGroupReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicPayFileSpecification.setCvgGroupReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		
		for(DynamicPayFileCoverageGroupReference covRef : DataManager.i().mDynamicPayFileSpecification.getCvgGroupReferences()) 
		{ 
			if(covRef.isActive() == true && covRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getPayFileDepartmentRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicPayFileDepartmentReferenceRequest request = new DynamicPayFileDepartmentReferenceRequest(DataManager.i().mDynamicPayFileDepartmentReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicPayFileSpecification.setDeptReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		
		for(DynamicPayFileDepartmentReference deptRef : DataManager.i().mDynamicPayFileSpecification.getDeptReferences()) 
		{ 
			if(deptRef.isActive() == true && deptRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getPayFileLocationRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicPayFileLocationReferenceRequest request = new DynamicPayFileLocationReferenceRequest(DataManager.i().mDynamicPayFileLocationReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicPayFileSpecification.setLocReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		
		for(DynamicPayFileLocationReference locRef : DataManager.i().mDynamicPayFileSpecification.getLocReferences()) 
		{ 
			if(locRef.isActive() == true && locRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getPayFileUnionTypeRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicPayFileUnionTypeReferenceRequest request = new DynamicPayFileUnionTypeReferenceRequest(DataManager.i().mDynamicPayFileUnionTypeReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicPayFileSpecification.setUnionTypeReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		
		for(DynamicPayFileUnionTypeReference unTpRef : DataManager.i().mDynamicPayFileSpecification.getUnionTypeReferences()) 
		{ 
			if(unTpRef.isActive() == true && unTpRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getPayFilePayPeriodFrequencyRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicPayFilePayFrequencyReferenceRequest request = new DynamicPayFilePayFrequencyReferenceRequest(DataManager.i().mDynamicPayFilePayFrequencyReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicPayFileSpecification.setPpdFreqReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		
		for(DynamicPayFilePayFrequencyReference ppFrqRef : DataManager.i().mDynamicPayFileSpecification.getPpdFreqReferences()) 
		{ 
			if(ppFrqRef.isActive() == true && ppFrqRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getPayFilePayCodeTypeRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicPayFilePayCodeReferenceRequest request = new DynamicPayFilePayCodeReferenceRequest(DataManager.i().mDynamicPayFilePayCodeReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicPayFileSpecification.setPayCodeReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		
		for(DynamicPayFilePayCodeReference payCdRef : DataManager.i().mDynamicPayFileSpecification.getPayCodeReferences()) 
		{ 
			if(payCdRef.isActive() == true && payCdRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getPayFilePayClassRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicPayFilePayClassReferenceRequest request = new DynamicPayFilePayClassReferenceRequest(DataManager.i().mDynamicPayFilePayClassReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicPayFileSpecification.setPayClassReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		
		for(DynamicPayFilePayClassReference pClsRef : DataManager.i().mDynamicPayFileSpecification.getPayClassReferences()) 
		{ 
			if(pClsRef.isActive() == true && pClsRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getPayFileGenderRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicPayFileGenderReferenceRequest request = new DynamicPayFileGenderReferenceRequest(DataManager.i().mDynamicPayFileGenderReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicPayFileSpecification.setGenderReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		
		for(DynamicPayFileGenderReference gendRef: DataManager.i().mDynamicPayFileSpecification.getGenderReferences()) 
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
		updatePayFile(); 
		EtcAdmin.i().setProgress(0);
	} 
 
	private void updatePayFile() 
	{ 
		//reset the updateRequest object if it has been used 
		if(DataManager.i().mDynamicPayFileSpecification != null) 
		{ 
			DynamicPayFileSpecification fSpec = DataManager.i().mDynamicPayFileSpecification; 
			DynamicPayFileSpecificationRequest request = new DynamicPayFileSpecificationRequest(); 
 
			//GATHER THE FIELDS 
			fSpec.setName(dfcovNameLabel.getText()); 
			fSpec.setTabIndex(Integer.valueOf(dfcovTabIndexLabel.getText())); 
			fSpec.setHeaderRowIndex(Integer.valueOf(dfcovHeaderRowIndexLabel.getText()));

			fSpec.setMapEEtoER(dfcovMapEEtoERCheck.isSelected()); 
			fSpec.setCreateEmployee(dfcovCreateEmployeeCheck.isSelected()); 
			fSpec.setSkipLastRow(dfcovSkipLastRowCheck.isSelected()); 
			fSpec.setCreatePayPeriod(dfcovCreatePayPeriod.isSelected());
			fSpec.setLocked(dfcovLocked.isSelected()); 
	 
	     	// EMPLOYER IDENTIFIER 
			fSpec.setErCol(Boolean.valueOf(emprId.isSelected())); 
			fSpec.setErColIndex(Integer.valueOf(emprIdIdx.getText() != null && !emprIdIdx.getText().isEmpty() ? emprIdIdx.getText() : "0")); 
	 
	     	// COVERAGE CLASS 
			fSpec.setCvgGroupCol(Boolean.valueOf(empCovCls.isSelected())); 
			fSpec.setCvgGroupColIndex(Integer.valueOf(empCovClsIdx.getText() != null && !empCovClsIdx.getText().isEmpty() ? empCovClsIdx.getText() : "0")); 
	 
	     	// DEPARTMENT 
			fSpec.setDeptCol(Boolean.valueOf(dept.isSelected())); 
			fSpec.setDeptColIndex(Integer.valueOf(deptIdx.getText() != null && !deptIdx.getText().isEmpty() ? deptIdx.getText() : "0")); 
	 
	     	// LOCATION 
			fSpec.setLocCol(Boolean.valueOf(loc.isSelected())); 
			fSpec.setLocColIndex(Integer.valueOf(locIdx.getText() != null && !locIdx.getText().isEmpty() ? locIdx.getText() : "0")); 
	 
	     	// UNION TYPE 
			fSpec.setUnionTypeCol(Boolean.valueOf(unTp.isSelected())); 
			fSpec.setUnionTypeColIndex(Integer.valueOf(unTpIdx.getText() != null && !unTpIdx.getText().isEmpty() ? unTpIdx.getText() : "0")); 
	 
	     	// PAY PERIOD FREQUENCY 
			fSpec.setPpdFreqCol(Boolean.valueOf(payFrq.isSelected())); 
			fSpec.setPpdFreqColIndex(Integer.valueOf(payFrqIdx.getText() != null && !payFrqIdx.getText().isEmpty() ? payFrqIdx.getText() : "0")); 
	 
	     	// PAY CODE 
			fSpec.setPayCodeCol(Boolean.valueOf(payCode.isSelected())); 
			fSpec.setPayCodeColIndex(Integer.valueOf(payCodeIdx.getText() != null && !payCodeIdx.getText().isEmpty() ? payCodeIdx.getText() : "0")); 
	 
	     	// PAY CLASS 
			fSpec.setPayClassCol(Boolean.valueOf(payCls.isSelected())); 
			fSpec.setPayClassColIndex(Integer.valueOf(payClsIdx.getText() != null && !payClsIdx.getText().isEmpty() ? payClsIdx.getText() : "0")); 
	 
			// GENDER 
			fSpec.setGenderCol(Boolean.valueOf(gender.isSelected())); 
			fSpec.setGenderColIndex(Integer.valueOf(genderIdx.getText() != null && !genderIdx.getText().isEmpty() ? genderIdx.getText() : "0")); 
	 
	     	// HOURS 
			fSpec.setHrsCol(Boolean.valueOf(hours.isSelected())); 
			fSpec.setHrsColIndex(Integer.valueOf(hoursIdx.getText() != null && !hoursIdx.getText().isEmpty() ? hoursIdx.getText() : "0")); 
	 
			// FIRST NAME 
			fSpec.setfNameCol(Boolean.valueOf(empFstNm.isSelected())); 
			fSpec.setfNameColIndex(Integer.valueOf(empFstNmIdx.getText() != null && !empFstNmIdx.getText().isEmpty() ? empFstNmIdx.getText() : "0")); 
			if(empFstNmParse.getId() != null && Utils.isInt(empFstNmParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(empFstNmParse.getId())); 
				request.setfNameParsePatternRequest(req); 
			} else request.setClearFNameParsePatternId(true); 
			 
			// STREET NUMBER 
			fSpec.setStreetNumCol(Boolean.valueOf(emplStNm.isSelected())); 
			fSpec.setStreetNumColIndex(Integer.valueOf(emplStNmIdx.getText() != null && !emplStNmIdx.getText().isEmpty() ? emplStNmIdx.getText() : "0")); 
	 
			// CITY 
			fSpec.setCityCol(Boolean.valueOf(emplCity.isSelected())); 
			fSpec.setCityColIndex(Integer.valueOf(emplCityIdx.getText() != null && !emplCityIdx.getText().isEmpty() ? emplCityIdx.getText() : "0")); 
			if(emplCityParse.getId() != null && Utils.isInt(emplCityParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplCityParse.getId())); 
				request.setCityParsePatternRequest(req); 
			} else request.setClearCityParsePatternId(true); 
	 
	     	// EMPLOYEE IDENTIFIER 
			fSpec.setErRefCol(Boolean.valueOf(emplrEmpId.isSelected())); 
			fSpec.setErRefColIndex(Integer.valueOf(emplrEmpIdIdx.getText() != null && !emplrEmpIdIdx.getText().isEmpty() ? emplrEmpIdIdx.getText() : "0")); 
			if(emplrEmpIdParse.getId() != null && Utils.isInt(emplrEmpIdParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplrEmpIdParse.getId())); 
				request.setErRefParsePatternRequest(req); 
			} else request.setClearErRefParsePatternId(true); 
	 
			// SSN 
			fSpec.setSsnCol(Boolean.valueOf(emplSsn.isSelected())); 
			fSpec.setSsnColIndex(Integer.valueOf(emplSsnIdx.getText() != null && !emplSsnIdx.getText().isEmpty() ? emplSsnIdx.getText() : "0")); 
			if(emplSsnParse.getId() != null && Utils.isInt(emplSsnParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplSsnParse.getId())); 
				request.setSsnParsePatternRequest(req); 
			} else request.setClearSsnParsePatternId(true); 
	 
			// OVERTIME HOURS 
			fSpec.setOtHrsCol(Boolean.valueOf(otHrs.isSelected())); 
			fSpec.setOtHrsColIndex(Integer.valueOf(otHrsIdx.getText() != null && !otHrsIdx.getText().isEmpty() ? otHrsIdx.getText() : "0")); 
	 
			// OVERTIME 3 HOURS 
			fSpec.setOtHrs3Col(Boolean.valueOf(ot3Hrs.isSelected())); 
			fSpec.setOtHrs3ColIndex(Integer.valueOf(ot3HrsIdx.getText() != null && !ot3HrsIdx.getText().isEmpty() ? ot3HrsIdx.getText() : "0")); 
	 
			// PAY PERIOD ID 
			fSpec.setPpdErRefCol(Boolean.valueOf(payPrd.isSelected())); 
			fSpec.setPpdErRefColIndex(Integer.valueOf(payPrdIdx.getText() != null && !payPrdIdx.getText().isEmpty() ? payPrdIdx.getText() : "0")); 
	 
			// RATE 
			fSpec.setRateCol(Boolean.valueOf(rate.isSelected())); 
			fSpec.setRateColIndex(Integer.valueOf(rateIdx.getText() != null && !rateIdx.getText().isEmpty() ? rateIdx.getText() : "0")); 
	 
			// AMOUNT 
			fSpec.setAmtCol(Boolean.valueOf(amt.isSelected())); 
			fSpec.setAmtColIndex(Integer.valueOf(amtIdx.getText() != null && !amtIdx.getText().isEmpty() ? amtIdx.getText() : "0")); 
	 
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
	 
			// PAID TIME OFF HOURS 
			fSpec.setPtoHrsCol(Boolean.valueOf(pdTmOffHrs.isSelected())); 
			fSpec.setPtoHrsColIndex(Integer.valueOf(pdTmOffHrsIdx.getText() != null && !pdTmOffHrsIdx.getText().isEmpty() ? pdTmOffHrsIdx.getText() : "0")); 
	 
			// MIDDLE NAME 
			fSpec.setmNameCol(Boolean.valueOf(emplMNm.isSelected())); 
			fSpec.setmNameColIndex(Integer.valueOf(emplMNmIdx.getText() != null && !emplMNmIdx.getText().isEmpty() ? emplMNmIdx.getText() : "0")); 
			if(emplMNmParse.getId() != null && Utils.isInt(emplMNmParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplMNmParse.getId())); 
				request.setmNameParsePatternRequest(req); 
			} else request.setClearMNameParsePatternId(true); 
	 
			// ADDRESS LINE 1 
			fSpec.setStreetCol(Boolean.valueOf(emplAdLn1.isSelected())); 
			fSpec.setStreetColIndex(Integer.valueOf(emplAdLn1Idx.getText() != null && !emplAdLn1Idx.getText().isEmpty() ? emplAdLn1Idx.getText() : "0")); 
			if(emplAdLn1Parse.getId() != null && Utils.isInt(emplAdLn1Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplAdLn1Parse.getId())); 
				request.setStreetParsePatternRequest(req); 
			} else request.setClearStreetParsePatternId(true); 
			 
			// STATE 
			fSpec.setStateCol(Boolean.valueOf(emplState.isSelected())); 
			fSpec.setStateColIndex(Integer.valueOf(emplStateIdx.getText() != null && !emplStateIdx.getText().isEmpty() ? emplStateIdx.getText() : "0")); 
			if(emplStateParse.getId() != null && Utils.isInt(emplStateParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplStateParse.getId())); 
				request.setStateParsePatternRequest(req); 
			} else request.setClearStateParsePatternId(true); 
	 
			// JOB TITLE 
			fSpec.setJobTtlCol(Boolean.valueOf(emplJob.isSelected())); 
			fSpec.setJobTtlColIndex(Integer.valueOf(emplJobIdx.getText() != null && !emplJobIdx.getText().isEmpty() ? emplJobIdx.getText() : "0")); 
	 
			// EMAIL 
			fSpec.setEmlCol(Boolean.valueOf(email.isSelected())); 
			fSpec.setEmlColIndex(Integer.valueOf(emailIdx.getText() != null && !emailIdx.getText().isEmpty() ? emailIdx.getText() : "0")); 
	 
			// OVERTIME 2 HOURS 
			fSpec.setOtHrs2Col(Boolean.valueOf(ot2Hrs.isSelected())); 
			fSpec.setOtHrs2ColIndex(Integer.valueOf(ot2HrsIdx.getText() != null && !ot2HrsIdx.getText().isEmpty() ? ot2HrsIdx.getText() : "0")); 
	 
			// PAY PERIOD START 
			fSpec.setPpdStartDtCol(Boolean.valueOf(pPStrt.isSelected())); 
			fSpec.setPpdStartDtColIndex(Integer.valueOf(pPStrtIdx.getText() != null && !pPStrtIdx.getText().isEmpty() ? pPStrtIdx.getText() : "0")); 
			if(pPStrtParse.getId() != null && Utils.isInt(pPStrtParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(pPStrtParse.getId())); 
				request.setPpdStartDtParsePatternRequest(req); 
			} else request.setClearPpdStartDtParseId(true); 
			if(pPStrtFormat.getId() != null && Utils.isInt(pPStrtFormat.getId())) 
			{ 
				PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
				req.setId(Long.valueOf(pPStrtFormat.getId())); 
				request.setPpdStartDtFormatRequest(req); 
			} else request.setClearPpdStartDtFormatId(true); 
	 
			// PAY PERIOD END 
			fSpec.setPpdEndDtCol(Boolean.valueOf(pPEnd.isSelected())); 
			fSpec.setPpdEndDtColIndex(Integer.valueOf(pPEndIdx.getText() != null && !pPEndIdx.getText().isEmpty() ? pPEndIdx.getText() : "0")); 
			if(pPEndParse.getId() != null && Utils.isInt(pPEndParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(pPEndParse.getId())); 
				request.setPpdEndDtParsePatternRequest(req); 
			} else request.setClearPpdEndDtParseId(true); 
			if(pPEndFormat.getId() != null && Utils.isInt(pPEndFormat.getId())) 
			{ 
				PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
				req.setId(Long.valueOf(pPEndFormat.getId())); 
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
			} else request.setClearPayDtParseId(true); 
			if(payDtFormat.getId() != null && Utils.isInt(payDtFormat.getId())) 
			{ 
				PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
				req.setId(Long.valueOf(payDtFormat.getId())); 
				request.setPayDtFormatRequest(req); 
			} else request.setClearPayDtFormatId(true); 
	 
			// WORK DATE 
			fSpec.setWorkDtCol(Boolean.valueOf(workDt.isSelected())); 
			fSpec.setWorkDtColIndex(Integer.valueOf(workDtIdx.getText() != null && !workDtIdx.getText().isEmpty() ? workDtIdx.getText() : "0")); 
			if(workDtParse.getId() != null && Utils.isInt(workDtParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(workDtParse.getId())); 
				request.setWorkDtParsePatternRequest(req); 
			} else request.setClearWorkDtParseId(true); 
			if(workDtFormat.getId() != null && Utils.isInt(workDtFormat.getId())) 
			{ 
				PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
				req.setId(Long.valueOf(workDtFormat.getId())); 
				request.setWorkDtFormatRequest(req); 
			} else request.setClearWorkDtFormatId(true); 
	 
	     	// TERMINATION DATE 
			fSpec.setTermDtCol(Boolean.valueOf(emplTrmDt.isSelected())); 
			fSpec.setTermDtColIndex(Integer.valueOf(emplTrmDtIdx.getText() != null && !emplTrmDtIdx.getText().isEmpty() ? emplTrmDtIdx.getText() : "0")); 
			if(emplTrmDtFormat.getId() != null && Utils.isInt(emplTrmDtFormat.getId())) 
			{ 
				PipelineParseDateFormatRequest tdReq = new PipelineParseDateFormatRequest(); 
				tdReq.setId(Long.valueOf(emplTrmDtFormat.getId())); 
				request.setTermDtFormatRequest(tdReq); 
			} else request.setClearTermDtFormatId(true); 
	     	// SICK PAY HOURS 
			fSpec.setSickHrsCol(Boolean.valueOf(skPyHrs.isSelected())); 
			fSpec.setSickHrsColIndex(Integer.valueOf(skPyHrsIdx.getText() != null && !skPyHrsIdx.getText().isEmpty() ? skPyHrsIdx.getText() : "0")); 
	 
	     	// HOLIDAY PAY HOURS 
			fSpec.setHolidayHrsCol(Boolean.valueOf(holPay.isSelected())); 
			fSpec.setHolidayHrsColIndex(Integer.valueOf(holPayIdx.getText() != null && !holPayIdx.getText().isEmpty() ? holPayIdx.getText() : "0")); 
	 
			// LAST NAME 
			fSpec.setlNameCol(Boolean.valueOf(emplLstNm.isSelected())); 
			fSpec.setlNameColIndex(Integer.valueOf(emplLstNmIdx.getText() != null && !emplLstNmIdx.getText().isEmpty() ? emplLstNmIdx.getText() : "0")); 
			if(emplLstNmParse.getId() != null && Utils.isInt(emplLstNmParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplLstNmParse.getId())); 
				request.setlNameParsePatternRequest(req); 
			} else request.setClearLNameParsePatternId(true); 
	 
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
			 
			// DATE OF BIRTH 
			fSpec.setDobCol(Boolean.valueOf(empDOB.isSelected())); 
			fSpec.setDobColIndex(Integer.valueOf(empDOBIdx.getText() != null && !empDOBIdx.getText().isEmpty() ? empDOBIdx.getText() : "0")); 
			if(empDOBFormat.getId() != null && Utils.isInt(empDOBFormat.getId())) 
			{ 
				PipelineParseDateFormatRequest tdReq = new PipelineParseDateFormatRequest(); 
				tdReq.setId(Long.valueOf(empDOBFormat.getId())); 
				request.setDobFormatRequest(tdReq); 
			} else request.setClearDobFormatId(true); 
	 
			// PHONE 
			fSpec.setPhoneCol(Boolean.valueOf(phone.isSelected())); 
			fSpec.setPhoneColIndex(Integer.valueOf(phoneIdx.getText() != null && !phoneIdx.getText().isEmpty() ? phoneIdx.getText() : "0")); 
	 
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
	 
	     	// CUSTOM PAY FIELD 1 
			fSpec.setPayCfld1Name(pyCF1.getText() != null && !pyCF1.getText().isEmpty() ? pyCF1.getText() : ""); 
			fSpec.setPayCfld1Col(Boolean.valueOf(payCF1.isSelected())); 
			fSpec.setPayCfld1ColIndex(Integer.valueOf(payCF1Idx.getText() != null && !payCF1Idx.getText().isEmpty() ? payCF1Idx.getText() : "0")); 
			if(payCF1Parse.getId() != null && Utils.isInt(payCF1Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(payCF1Parse.getId())); 
				request.setPayCfld1ParsePatternRequest(req); 
			} else request.setClearPayCfld1ParsePatternId(true); 
	 
	     	// CUSTOM PAY FIELD 2 
			fSpec.setPayCfld2Name(pyCF2.getText() != null && !pyCF2.getText().isEmpty() ? pyCF2.getText() : ""); 
			fSpec.setPayCfld2Col(Boolean.valueOf(payCF2.isSelected())); 
			fSpec.setPayCfld2ColIndex(Integer.valueOf(payCF2Idx.getText() != null && !payCF2Idx.getText().isEmpty() ? payCF2Idx.getText() : "0")); 
			if(payCF2Parse.getId() != null && Utils.isInt(payCF2Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(payCF2Parse.getId())); 
				request.setPayCfld2ParsePatternRequest(req); 
			} else request.setClearPayCfld2ParsePatternId(true); 
	 
	     	// CUSTOM PAY FIELD 3 
			fSpec.setPayCfld3Name(pyCF3.getText() != null && !pyCF3.getText().isEmpty() ? pyCF3.getText() : ""); 
			fSpec.setPayCfld3Col(Boolean.valueOf(payCF3.isSelected())); 
			fSpec.setPayCfld3ColIndex(Integer.valueOf(payCF3Idx.getText() != null && !payCF3Idx.getText().isEmpty() ? payCF3Idx.getText() : "0")); 
	 
	     	// CUSTOM PAY FIELD 4 
			fSpec.setPayCfld4Name(pyCF4.getText() != null && !pyCF4.getText().isEmpty() ? pyCF4.getText() : ""); 
			fSpec.setPayCfld4Col(Boolean.valueOf(payCF4.isSelected())); 
			fSpec.setPayCfld4ColIndex(Integer.valueOf(payCF4Idx.getText() != null && !payCF4Idx.getText().isEmpty() ? payCF4Idx.getText() : "0")); 
	 
	     	// CUSTOM PAY FIELD 5 
			fSpec.setPayCfld5Name(pyCF5.getText() != null && !pyCF5.getText().isEmpty() ? pyCF5.getText() : ""); 
			fSpec.setPayCfld5Col(Boolean.valueOf(payCF5.isSelected())); 
			fSpec.setPayCfld5ColIndex(Integer.valueOf(payCF5Idx.getText() != null && !payCF5Idx.getText().isEmpty() ? payCF5Idx.getText() : "0")); 
	 
	     	// CUSTOM PAY FIELD 6 
			fSpec.setPayCfld6Name(pyCF6.getText() != null && !pyCF6.getText().isEmpty() ? pyCF6.getText() : ""); 
			fSpec.setPayCfld6Col(Boolean.valueOf(payCF6.isSelected())); 
			fSpec.setPayCfld6ColIndex(Integer.valueOf(payCF6Idx.getText() != null && !payCF6Idx.getText().isEmpty() ? payCF6Idx.getText() : "0")); 
	 
	     	// CUSTOM PAY FIELD 7 
			fSpec.setPayCfld7Name(pyCF7.getText() != null && !pyCF7.getText().isEmpty() ? pyCF7.getText() : ""); 
			fSpec.setPayCfld7Col(Boolean.valueOf(payCF7.isSelected())); 
			fSpec.setPayCfld7ColIndex(Integer.valueOf(payCF7Idx.getText() != null && !payCF7Idx.getText().isEmpty() ? payCF7Idx.getText() : "0")); 
			 
			// set the request entity 
			request.setEntity(fSpec); 
			request.setId(fSpec.getId()); 
 
			// update the server 
			try {
				fSpec = AdminPersistenceManager.getInstance().addOrUpdate(request);
			} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); } 
		    catch (Exception e) { DataManager.i().logGenericException(e); }
		} 		  
		EtcAdmin.i().setStatusMessage("Ready"); 
        EtcAdmin.i().setProgress(0); 
	} 
 
	@FXML 
	private void onAddIgnoreRow(ActionEvent event) 
	{ 
    	DataManager.i().mDynamicPayFileIgnoreRowSpecification = null; 
		viewIgnoreRow(); 
	}	 
 
	private void viewIgnoreRow()  
	{ 
        try { 
            // load the fxml 
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/mapper/ViewMapperPayFileRowIgnore.fxml")); 
			Parent ControllerNode = loader.load(); 
	        ViewMapperPayFileRowIgnoreController ignoreController = (ViewMapperPayFileRowIgnoreController) loader.getController(); 
 
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
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/mapper/ViewMapperPayFilePayPeriodRule.fxml")); 
			Parent ControllerNode = loader.load(); 
			ViewMapperPayFilePayPeriodRuleController ruleController = (ViewMapperPayFilePayPeriodRuleController) loader.getController(); 
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
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/mapper/ViewMapperPayFileAdditionalHours.fxml")); 
			Parent ControllerNode = loader.load(); 
			ViewMapperPayFileAdditionalHoursController refController = (ViewMapperPayFileAdditionalHoursController) loader.getController(); 
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
	        stage.onCloseRequestProperty().setValue(e -> setEmpRefCount(getPayFileEmployerRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setEmpRefCount(getPayFileEmployerRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true) 
				setEmpRefCount(getPayFileEmployerRefCount()); 
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
	        stage.onCloseRequestProperty().setValue(e -> setCvgRefCount(getPayFileCvgClassRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setCvgRefCount(getPayFileCvgClassRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
				setCvgRefCount(getPayFileCvgClassRefCount()); 
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
	        stage.onCloseRequestProperty().setValue(e -> setDeptRefCount(getPayFileDepartmentRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setDeptRefCount(getPayFileDepartmentRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
				setDeptRefCount(getPayFileDepartmentRefCount()); 
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
	        stage.onCloseRequestProperty().setValue(e -> setLocRefCount(getPayFileLocationRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setLocRefCount(getPayFileLocationRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
				setLocRefCount(getPayFileLocationRefCount()); 
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
	        stage.onCloseRequestProperty().setValue(e -> setUnTpRefCount(getPayFileUnionTypeRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setUnTpRefCount(getPayFileUnionTypeRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
				setUnTpRefCount(getPayFileUnionTypeRefCount()); 
		} catch (IOException e) { 
			 DataManager.i().log(Level.SEVERE, e); 
			Utils.alertUser("Reference Popup Load Error", e.getMessage());
		}        		 
	    catch (Exception e) { DataManager.i().logGenericException(e); }
	} 
	 
	private void loadPayPeriodFrqRefForm() 
	{ 
        try { 
        	// load the fxml 
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationPayFrequencyTypeRef.fxml")); 
			Parent ControllerNode = loader.load(); 
	        ViewDynamicFileSpecificationPayFrequencyTypeRefController refController = (ViewDynamicFileSpecificationPayFrequencyTypeRefController) loader.getController(); 
	        refController.load(); 
 
	        Stage stage = new Stage(); 
	        stage.initModality(Modality.APPLICATION_MODAL); 
	        stage.initStyle(StageStyle.UNDECORATED); 
	        stage.setScene(new Scene(ControllerNode));   
	        stage.onCloseRequestProperty().setValue(e -> setPpFreqRefCount(getPayFilePayPeriodFrequencyRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setPpFreqRefCount(getPayFilePayPeriodFrequencyRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
	        	setPpFreqRefCount(getPayFilePayPeriodFrequencyRefCount()); 
		} catch (IOException e) { 
			 DataManager.i().log(Level.SEVERE, e); 
			Utils.alertUser("Reference Popup Load Error", e.getMessage());
		}        		 
	    catch (Exception e) { DataManager.i().logGenericException(e); }
	} 
	 
	private void loadPayCodeRefForm() 
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
	        stage.onCloseRequestProperty().setValue(e -> setPayCodeRefCount(getPayFilePayCodeTypeRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setPayCodeRefCount(getPayFilePayCodeTypeRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
	        	setPayCodeRefCount(getPayFilePayCodeTypeRefCount()); 
		} catch (IOException e) { 
			 DataManager.i().log(Level.SEVERE, e); 
			Utils.alertUser("Reference Popup Load Error", e.getMessage());
		}        		 
	    catch (Exception e) { DataManager.i().logGenericException(e); }
	} 
	 
	private void loadPayClassRefForm() 
	{ 
        try { 
        	// load the fxml 
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/filespecification/ViewDynamicFileSpecificationPayClassRef.fxml")); 
			Parent ControllerNode = loader.load(); 
	        ViewDynamicFileSpecificationPayClassRefController refController = (ViewDynamicFileSpecificationPayClassRefController) loader.getController(); 
	        refController.load(); 
 
	        Stage stage = new Stage(); 
	        stage.initModality(Modality.APPLICATION_MODAL); 
	        stage.initStyle(StageStyle.UNDECORATED); 
	        stage.setScene(new Scene(ControllerNode));   
	        stage.onCloseRequestProperty().setValue(e -> setPayClassRefCount(getPayFilePayClassRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setPayClassRefCount(getPayFilePayClassRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
	        	setPayClassRefCount(getPayFilePayClassRefCount()); 
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
	        stage.onCloseRequestProperty().setValue(e -> setGenderRefCount(getPayFileGenderRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setGenderRefCount(getPayFileGenderRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
	        	setGenderRefCount(getPayFileGenderRefCount()); 
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
  	        stage.onCloseRequestProperty().setValue(e -> updateFileData()); 
  	        stage.onHiddenProperty().setValue(e -> updateFileData()); 
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
		if(emplCityParse.getText().contains("+ Pattern")){ emplCityParse.setId("emplCityParse"); } 
		if(emplrEmpIdParse.getText().contains("+ Pattern")){ emplrEmpIdParse.setId("emplrEmpIdParse"); } 
		if(emplSsnParse.getText().contains("+ Pattern")){ emplSsnParse.setId("emplSsnParse"); } 
		if(emplHrDtFormat.getText().contains("+ Format")){ emplHrDtFormat.setId("emplHrDtFormat"); } 
		if(emplRHrDtFormat.getText().contains("+ Format")){ emplRHrDtFormat.setId("emplRHrDtFormat"); } 
		if(emplMNmParse.getText().contains("+ Pattern")){ emplMNmParse.setId("emplMNmParse"); } 
		if(emplAdLn1Parse.getText().contains("+ Pattern")){ emplAdLn1Parse.setId("emplAdLn1Parse"); } 
		if(emplStateParse.getText().contains("+ Pattern")){ emplStateParse.setId("emplStateParse"); } 
		if(pPStrtParse.getText().contains("+ Pattern")){ pPStrtParse.setId("pPStrtParse"); } 
		if(pPStrtFormat.getText().contains("+ Format")){ pPStrtFormat.setId("pPStrtFormat"); } 
		if(pPEndParse.getText().contains("+ Pattern")){ pPEndParse.setId("pPEndParse"); } 
		if(pPEndFormat.getText().contains("+ Format")){ pPEndFormat.setId("pPEndFormat"); } 
		if(payDtParse.getText().contains("+ Pattern")){ payDtParse.setId("payDtParse"); } 
		if(payDtFormat.getText().contains("+ Format")){ payDtFormat.setId("payDtFormat"); } 
		if(workDtParse.getText().contains("+ Pattern")){ workDtParse.setId("workDtParse"); } 
		if(workDtFormat.getText().contains("+ Format")){ workDtFormat.setId("workDtFormat"); } 
		if(emplTrmDtFormat.getText().contains("+ Format")){ emplTrmDtFormat.setId("emplTrmDtFormat"); } 
		if(emplLstNmParse.getText().contains("+ Pattern")){ emplLstNmParse.setId("emplLstNmParse"); } 
		if(emplZipParse.getText().contains("+ Pattern")){ emplZipParse.setId("emplZipParse"); } 
		if(empDOBFormat.getText().contains("+ Format")){ empDOBFormat.setId("empDOBFormat"); } 
		if(emplCF1Parse.getText().contains("+ Pattern")){ emplCF1Parse.setId("emplCF1Parse"); } 
		if(emplCF2Parse.getText().contains("+ Pattern")){ emplCF2Parse.setId("emplCF2Parse"); } 
		if(payCF1Parse.getText().contains("+ Pattern")){ payCF1Parse.setId("payCF1Parse"); } 
		if(payCF2Parse.getText().contains("+ Pattern")){ payCF2Parse.setId("payCF2Parse"); } 
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
 
    public void setCvgRefCount(int count) 
    { 
    	covRefLabel.setFont(Font.font("Veranda", 12.0)); 
        covRefLabel.setText("References: " + String.valueOf(count)); 
    } 
 
    public void setDeptRefCount(int count) 
    { 
    	deptRefLabel.setFont(Font.font("Veranda", 12.0)); 
        deptRefLabel.setText("References: " + String.valueOf(count)); 
    } 
 
    public void setLocRefCount(int count) 
    { 
    	locRefLabel.setFont(Font.font("Veranda", 12.0)); 
        locRefLabel.setText("References: " + String.valueOf(count)); 
    } 
 
    public void setUnTpRefCount(int count) 
    { 
    	unTpRefLabel.setFont(Font.font("Veranda", 12.0)); 
    	unTpRefLabel.setText("References: " + String.valueOf(count)); 
    } 
 
    public void setPpFreqRefCount(int count) 
    { 
    	payFrqRefLabel.setFont(Font.font("Veranda", 12.0)); 
    	payFrqRefLabel.setText("References: " + String.valueOf(count)); 
    } 
 
    public void setPayCodeRefCount(int count) 
    { 
    	payCodeRefLabel.setFont(Font.font("Veranda", 12.0)); 
    	payCodeRefLabel.setText("References: " + String.valueOf(count)); 
    } 
 
    public void setPayClassRefCount(int count) 
    { 
    	payClsRefLabel.setFont(Font.font("Veranda", 12.0)); 
    	payClsRefLabel.setText("References: " + String.valueOf(count)); 
    } 
 
    public void setGenderRefCount(int count) 
    { 
    	genderRefLabel.setFont(Font.font("Veranda", 12.0)); 
    	genderRefLabel.setText("References: " + String.valueOf(count)); 
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
				dept.setSelected(false); 
				loc.setSelected(false); 
				unTp.setSelected(false); 
				payFrq.setSelected(false); 
				payCode.setSelected(false); 
				payCls.setSelected(false); 
				hours.setSelected(false); 
				empFstNm.setSelected(false); 
				emplStNm.setSelected(false); 
				emplCity.setSelected(false); 
				emplrEmpId.setSelected(false); 
				emplSsn.setSelected(false); 
				otHrs.setSelected(false); 
				ot3Hrs.setSelected(false); 
				payPrd.setSelected(false); 
				rate.setSelected(false); 
				amt.setSelected(false); 
				emplHrDt.setSelected(false); 
				emplRHrDt.setSelected(false); 
				pdTmOffHrs.setSelected(false); 
				emplMNm.setSelected(false); 
				emplAdLn1.setSelected(false); 
				emplState.setSelected(false); 
				emplJob.setSelected(false); 
				email.setSelected(false); 
				ot2Hrs.setSelected(false); 
				pPStrt.setSelected(false); 
				pPEnd.setSelected(false); 
				payDt.setSelected(false); 
				workDt.setSelected(false); 
				emplTrmDt.setSelected(false); 
				skPyHrs.setSelected(false); 
				holPay.setSelected(false); 
				emplLstNm.setSelected(false); 
				emplAdLn2.setSelected(false); 
				emplZip.setSelected(false); 
				empDOB.setSelected(false); 
				phone.setSelected(false); 
				emplCF1.setSelected(false); 
				emplCF2.setSelected(false); 
				emplCF3.setSelected(false); 
				emplCF4.setSelected(false); 
				emplCF5.setSelected(false); 
				payCF1.setSelected(false); 
				payCF2.setSelected(false); 
				payCF3.setSelected(false); 
				payCF4.setSelected(false); 
				payCF5.setSelected(false); 
				payCF6.setSelected(false); 
				payCF7.setSelected(false); 
				dfcovNameLabel.setText(""); 
				dfcovTabIndexLabel.setText("0"); 
				dfcovHeaderRowIndexLabel.setText("0"); 
				dfcovMapEEtoERCheck.setSelected(false); 
				dfcovCreateEmployeeCheck.setSelected(false); 
				dfcovSkipLastRowCheck.setSelected(false); 
				dfcovCreatePayPeriod.setSelected(false); 
				dfcovLocked.setSelected(false); 
 
				mf.getTxtFldIndex().setText(""); 
				eeCF1.setText(""); 
				eeCF2.setText(""); 
				eeCF3.setText(""); 
				eeCF4.setText(""); 
				eeCF5.setText(""); 
				pyCF1.setText(""); 
				pyCF2.setText(""); 
				pyCF3.setText(""); 
				pyCF4.setText(""); 
				pyCF5.setText(""); 
				pyCF6.setText(""); 
				pyCF7.setText(""); 
				emprRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
				ccRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
				deptRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
				locRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
				unTpRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
				payFrqRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
				payCodeRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
				payClsRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
				mf.getStkPane().setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
 
				for(MapperCell mc : mapperCells.values()) 
				{ 
					if(mc != null) 
					{ 
						mc.getNode().setStyle(Color.WHITE.toString()); 
						spreadsheet.getColumnConstraints().get(mc.getIdx()).setMinWidth(50); 
						refreshPatterns(mf, mc.getIdx(), mf.getParsePatternId(), mf.getDateFormatId()); 
						deregisterCell(mc.getIdx(), mf); 
						checkBoxChanged(mf); 
					} 
				} 
			} 
		} 
	} 
 
	@FXML 
	private void headerCheckBoxSelect(ActionEvent event) 
	{  
		DynamicPayFileSpecification fSpec = DataManager.i().mDynamicPayFileSpecification; 
 
		if(dfcovMapEEtoERCheck.isSelected() == true) { fSpec.setMapEEtoER(true); } 
		else { fSpec.setMapEEtoER(false); } 
 
		if(dfcovCreateEmployeeCheck.isSelected() == true) { fSpec.setCreateEmployee(true); } 
		else { fSpec.setCreateEmployee(false); } 
 
		if(dfcovSkipLastRowCheck.isSelected() == true) { fSpec.setSkipLastRow(true); } 
		else { fSpec.setSkipLastRow(false); } 
 
		if(dfcovCreatePayPeriod.isSelected() == true) { fSpec.setCreatePayPeriod(true); } 
		else { fSpec.setCreatePayPeriod(false); } 
 
		if(dfcovLocked.isSelected() == true) { fSpec.setLocked(true); } 
		else { fSpec.setLocked(false); } 
	} 
 
	public void clearRule(DynamicPayFilePayPeriodRule rule)  
	{ 
		if(rule.isActive() == false) { ruleList.clear(); } 
	} 
 
	@FXML 
	private void onClearForm(ActionEvent event) { clearForm(); }	 
 
	//Reference Button Action Events 
	@FXML 
	private void onEmprRefClick(ActionEvent event)  
	{ 
		loadEmployerRefForm(); 
	}	 
 
	@FXML 
	private void onCcRefClick(ActionEvent event)  
	{ 
		loadCoverageClassRefForm(); 
	}	 
 
	@FXML 
	private void onDeptRefClick(ActionEvent event)  
	{ 
		loadDepartmentRefForm(); 
	}	 
 
	@FXML 
	private void onLocRefClick(ActionEvent event)  
	{ 
		loadLocationRefForm(); 
	}	 
 
	@FXML 
	private void onUnTpRefClick(ActionEvent event)  
	{ 
		loadUnionTypeRefForm(); 
	}	 
 
	@FXML 
	private void onPayFrqRefClick(ActionEvent event)  
	{ 
		loadPayPeriodFrqRefForm(); 
	}	 
 
	@FXML 
	private void onPayCodeRefClick(ActionEvent event)  
	{ 
		loadPayCodeRefForm(); 
	}	 
 
	@FXML 
	private void onPayClsRefClick(ActionEvent event)  
	{ 
		loadPayClassRefForm(); 
	}	 
 
	@FXML 
	private void onGendRefClick(ActionEvent event)  
	{ 
		loadGenderRefForm(); 
	}	 
 
	//Parse and Format Button Action Events 
	@FXML 
	private void onEmpFstNmParse(ActionEvent event)  
	{ 
		setParseButtonValue(event); 
	}	 
 
	@FXML 
	private void onEmplCityParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event); 
	} 
 
	@FXML 
	private void onEmplrEmpIdParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplSsnParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplHrDtFormat(ActionEvent event) 	 
	{  
		setFormatButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplRHrDtFormat(ActionEvent event) 	 
	{  
		setFormatButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplMNmParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplAdLn1Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplStateParse(ActionEvent event)	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onPpStrtParse(ActionEvent event)	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onPpStrtFormat(ActionEvent event)	 
	{  
		setFormatButtonValue(event);  
	} 
 
	@FXML 
	private void onPpEndParse(ActionEvent event)	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onPpEndFormat(ActionEvent event)	 
	{  
		setFormatButtonValue(event);  
	} 
 
	@FXML 
	private void onPayDtParse(ActionEvent event)	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onPayDtFormat(ActionEvent event)	 
	{  
		setFormatButtonValue(event);  
	} 
 
	@FXML 
	private void onWorkDtParse(ActionEvent event)	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onWorkDtFormat(ActionEvent event)	 
	{  
		setFormatButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplTrmDtFormat(ActionEvent event) 	 
	{  
		setFormatButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplLstNmParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplZipParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onEmpDOBFormat(ActionEvent event) 	 
	{  
		setFormatButtonValue(event); 
	} 
 
	@FXML 
	private void onEmplCF1Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplCF2Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onPayCF1Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onPayCF2Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
	 
	public class HBoxRuleCell extends HBox 
	{ 
        Label lblCol1 = new Label(); 
        Label lblCol2 = new Label(); 
        Label lblCol3 = new Label(); 
        Label lblCol4 = new Label(); 
        DynamicPayFilePayPeriodRule rule;     
 
        HBoxRuleCell(DynamicPayFilePayPeriodRule rule) 
        { 
             super(); 
              
             if(rule != null)  
             { 
            	 if(rule.getPayCodeType() != null) 
            		 lblCol1.setText(rule.getPayCodeType().toString()); 
            	 if(rule.getPayFrequencyType() != null) 
            		 lblCol2.setText(rule.getPayFrequencyType().toString()); 
            	 if(rule.getEmployer() != null) 
            		 lblCol3.setText(rule.getEmployer().getName()); 
               	 if(rule.getMaxHours() != null) 
            		 lblCol4.setText(String.valueOf(rule.getMaxHours())); 
            	 this.rule = rule; 
             }else { 
            	 lblCol1.setText("Comp Type"); 
            	 lblCol1.setTextFill(Color.GREY); 
            	 lblCol2.setText("Pay Freq. Type"); 
            	 lblCol2.setTextFill(Color.GREY); 
            	 lblCol3.setText("Employer"); 
            	 lblCol3.setTextFill(Color.GREY); 
            	 lblCol4.setText("Max Hrs"); 
            	 lblCol4.setTextFill(Color.GREY); 
             } 
           
             lblCol1.setMinWidth(33); 
             lblCol1.setMaxWidth(33); 
             lblCol1.setPrefWidth(33); 
             HBox.setHgrow(lblCol1, Priority.ALWAYS); 
 
             lblCol2.setMinWidth(125); 
             lblCol2.setMaxWidth(125); 
             lblCol2.setPrefWidth(125); 
             HBox.setHgrow(lblCol2, Priority.ALWAYS); 
 
             lblCol3.setMinWidth(250); 
             lblCol3.setMaxWidth(250); 
             lblCol3.setPrefWidth(250); 
             HBox.setHgrow(lblCol3, Priority.ALWAYS); 
 
             lblCol4.setMinWidth(50); 
             lblCol4.setMaxWidth(50); 
             lblCol4.setPrefWidth(50); 
             HBox.setHgrow(lblCol4, Priority.ALWAYS); 
 
             this.getChildren().addAll(lblCol1, lblCol2, lblCol4, lblCol3); 
        } 
 
        public DynamicPayFilePayPeriodRule getRule() { 
        	return rule;  
        } 
   } 
 
	public class HBoxIgnoreRowCell extends HBox  
	{ 
        Label lblCol1 = new Label(); 
        Label lblCol2 = new Label(); 
        DynamicPayFileIgnoreRowSpecification paySpec; 
 
        HBoxIgnoreRowCell(DynamicPayFileIgnoreRowSpecification covSpec) 
        { 
             super(); 
 
             if(covSpec != null)  
             { 
            	 lblCol1.setText(Utils.getDateString(covSpec.getLastUpdated())); 
            	 lblCol2.setText(covSpec.getName()); 
            	 this.paySpec = covSpec; 
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
 
        public DynamicPayFileIgnoreRowSpecification getPaySpec()  
        { 
        	return paySpec;  
        } 
    } 
	 
	public class HBoxAdditionalHourCell extends HBox 
	{ 
        Label indexLabel = new Label(); 
        DynamicPayFileAdditionalHoursSpecification spec;   
         
        public DynamicPayFileAdditionalHoursSpecification getSpec() { 
        	return spec; 
        } 
         
        HBoxAdditionalHourCell(DynamicPayFileAdditionalHoursSpecification spec) 
        { 
              super(); 
              this.spec = spec; 
              indexLabel.setText(String.valueOf(spec.getColIndex())); 
               
        	  this.getChildren().addAll(indexLabel); 
        } 
    }	 
} 
