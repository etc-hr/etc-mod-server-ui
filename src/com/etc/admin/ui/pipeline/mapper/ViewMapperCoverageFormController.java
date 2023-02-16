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
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationGenderTypeRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicPayFilePayPeriodRuleController;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileCoverageGroupReference;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileGenderReference;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileSpecification;
import com.etc.corvetto.ems.pipeline.rqs.PipelineParseDateFormatRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineParsePatternRequest;
import com.etc.corvetto.ems.pipeline.rqs.cov.DynamicCoverageFileCoverageGroupReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.cov.DynamicCoverageFileEmployerReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.cov.DynamicCoverageFileGenderReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.cov.DynamicCoverageFileIgnoreRowSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.cov.DynamicCoverageFileSpecificationRequest;
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
 
public class ViewMapperCoverageFormController 
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
	private StackPane empFstNmSkPn; 
 
	@FXML 
	private StackPane empDOBSkPn; 
 
	@FXML 
	private StackPane emplJobSkPn; 
 
	@FXML 
	private StackPane emplStNmSkPn; 
 
	@FXML 
	private StackPane emplCitySkPn; 
	 
	@FXML 
	private StackPane emplIdSkPn; 
	 
	@FXML 
	private StackPane emplFSkPn; 
	 
	@FXML 
	private StackPane emplMNmSkPn; 
	 
	@FXML 
	private StackPane emplHrDtSkPn; 
	 
	@FXML 
	private StackPane emplPhnSkPn; 
	 
	@FXML 
	private StackPane emplAdLn1SkPn; 
 
	@FXML 
	private StackPane emplStateSkPn; 
	 
	@FXML 
	private StackPane emplSsnSkPn; 
	 
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
	private StackPane depIdSkPn; 
	 
	@FXML 
	private StackPane depFrstNmSkPn; 
	 
	@FXML 
	private StackPane depAdLn1SkPn; 
	 
	@FXML 
	private StackPane depZipSkPn; 
	 
	@FXML 
	private StackPane depCF4SkPn; 
	 
	@FXML 
	private StackPane depSSNSkPn; 
	 
	@FXML 
	private StackPane depMdNmSkPn; 
	 
	@FXML 
	private StackPane depAdLn2SkPn; 
	 
	@FXML 
	private StackPane depCF1SkPn; 
	 
	@FXML 
	private StackPane depCF5SkPn; 
	 
	@FXML 
	private StackPane depDOBSkPn; 
	 
	@FXML 
	private StackPane depLstNmSkPn; 
	 
	@FXML 
	private StackPane depCitySkPn; 
	 
	@FXML 
	private StackPane depCF2SkPn; 
	 
	@FXML 
	private StackPane depEmprIdSkPn; 
	 
	@FXML 
	private StackPane depStrtNmSkPn; 
	 
	@FXML 
	private StackPane depStateSkPn; 
	 
	@FXML 
	private StackPane depCF3SkPn; 
	 
	@FXML 
	private StackPane covMbrNmSkPn; 
	 
	@FXML 
	private StackPane covWaivSkPn; 
	 
	@FXML 
	private StackPane covStrtDtSkPn; 
	 
	@FXML 
	private StackPane covAnnCovSkPn; 
	 
	@FXML 
	private StackPane covJanSkPn; 
	 
	@FXML 
	private StackPane covMaySkPn; 
	 
	@FXML 
	private StackPane covSeptSkPn; 
	 
	@FXML 
	private StackPane covDecDtSkPn; 
	 
	@FXML 
	private StackPane covIneligSkPn; 
	 
	@FXML 
	private StackPane covEndDtSkPn; 
	 
	@FXML 
	private StackPane covTxYrSkPn; 
	 
	@FXML 
	private StackPane covFebSkPn; 
	 
	@FXML 
	private StackPane covJuneSkPn; 
	 
	@FXML 
	private StackPane covOctSkPn; 
	 
	@FXML 
	private StackPane covPlanSkPn; 
	 
	@FXML 
	private StackPane covDeducSkPn; 
	 
	@FXML 
	private StackPane covCF1SkPn; 
	 
	@FXML 
	private StackPane covMarchSkPn; 
	 
	@FXML 
	private StackPane covJulySkPn; 
	 
	@FXML 
	private StackPane covNovSkPn; 
	 
	@FXML 
	private StackPane covSubNmSkPn; 
	 
	@FXML 
	private StackPane covMbrShrAmtSkPn; 
	 
	@FXML 
	private StackPane covCF2SkPn; 
	 
	@FXML 
	private StackPane covAprilSkPn; 
	 
	@FXML 
	private StackPane covAugustSkPn; 
	 
	@FXML 
	private StackPane covDecSkPn; 
	 
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
	private CheckBox dfcovCreateSecondaryCheck; 
	 
	@FXML 
	private CheckBox dfcovCreatePlanCheck; 
	 
	@FXML 
	private CheckBox dfcovSkipLastRowCheck; 
	 
	@FXML 
	private CheckBox emprId; 
	 
	@FXML 
	private CheckBox empCovCls; 
	 
	@FXML 
	private CheckBox empFstNm; 
	 
	@FXML 
	private CheckBox empDOB; 
	 
	@FXML 
	private CheckBox emplJob; 
	 
	@FXML 
	private CheckBox emplStNm; 
	 
	@FXML 
	private CheckBox emplCity; 
	 
	@FXML 
	private CheckBox emplId; 
	 
	@FXML 
	private CheckBox emplF; 
	 
	@FXML 
	private CheckBox emplMNm; 
	 
	@FXML 
	private CheckBox emplHrDt; 
	 
	@FXML 
	private CheckBox emplPhn; 
	 
	@FXML 
	private CheckBox emplAdLn1; 
	 
	@FXML 
	private CheckBox emplState; 
	 
	@FXML 
	private CheckBox emplSsn; 
 
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
	private CheckBox depId; 
	 
	@FXML 
	private CheckBox depFrstNm; 
	 
	@FXML 
	private CheckBox depAdLn1; 
	 
	@FXML 
	private CheckBox depZip; 
	 
	@FXML 
	private CheckBox depCF4; 
	 
	@FXML 
	private CheckBox depSSN; 
	 
	@FXML 
	private CheckBox depMdNm; 
	 
	@FXML 
	private CheckBox depAdLn2; 
	 
	@FXML 
	private CheckBox depCF1; 
	 
	@FXML 
	private CheckBox depCF5; 
 
	@FXML 
	private CheckBox depDOB; 
	 
	@FXML 
	private CheckBox depLstNm; 
	 
	@FXML 
	private CheckBox depCity; 
	 
	@FXML 
	private CheckBox depCF2; 
	 
	@FXML 
	private CheckBox depEmprId; 
	 
	@FXML 
	private CheckBox depStrtNm; 
	 
	@FXML 
	private CheckBox depState; 
	 
	@FXML 
	private CheckBox depCF3; 
	 
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
	private CheckBox covInelig; 
	 
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
	private CheckBox covPlan; 
	 
	@FXML 
	private CheckBox covDeduc; 
	 
	@FXML 
	private CheckBox covCF1; 
	 
	@FXML 
	private CheckBox covMarch; 
	 
	@FXML 
	private CheckBox covJuly; 
	 
	@FXML 
	private CheckBox covNov; 
	 
	@FXML 
	private CheckBox covSubNm; 
	 
	@FXML 
	private CheckBox covMbrShrAmt; 
	 
	@FXML 
	private CheckBox covCF2; 
	 
	@FXML 
	private CheckBox covApril; 
	 
	@FXML 
	private CheckBox covAugust; 
	 
	@FXML 
	private CheckBox covDec; 
	 
	@FXML 
	private TextField emprIdIdx; 
	 
	@FXML 
	private TextField empCovClsIdx; 
	 
	@FXML 
	private TextField empFstNmIdx; 
	 
	@FXML 
	private TextField empDOBIdx; 
	 
	@FXML 
	private TextField emplJobIdx; 
	 
	@FXML 
	private TextField emplStNmIdx; 
	 
	@FXML 
	private TextField emplCityIdx; 
	 
	@FXML 
	private TextField emplIdIdx; 
	 
	@FXML 
	private TextField emplFIdx; 
	 
	@FXML 
	private TextField emplMNmIdx; 
	 
	@FXML 
	private TextField emplHrDtIdx; 
	 
	@FXML 
	private TextField emplPhnIdx; 
	 
	@FXML 
	private TextField emplAdLn1Idx; 
	 
	@FXML 
	private TextField emplStateIdx; 
	 
	@FXML 
	private TextField emplSsnIdx; 
	 
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
	private TextField depIdIdx; 
	 
	@FXML 
	private TextField depFrstNmIdx; 
	 
	@FXML 
	private TextField depAdLn1Idx; 
	 
	@FXML 
	private TextField depZipIdx; 
	 
	@FXML 
	private TextField depCF4Idx; 
	 
	@FXML 
	private TextField depSSNIdx; 
	 
	@FXML 
	private TextField depMdNmIdx; 
	 
	@FXML 
	private TextField depAdLn2Idx; 
	 
	@FXML 
	private TextField depCF1Idx; 
	 
	@FXML 
	private TextField depCF5Idx; 
	 
	@FXML 
	private TextField depDOBIdx; 
	 
	@FXML 
	private TextField depLstNmIdx; 
	 
	@FXML 
	private TextField depCityIdx; 
	 
	@FXML 
	private TextField depCF2Idx; 
	 
	@FXML 
	private TextField depEmprIdIdx; 
	 
	@FXML 
	private TextField depStrtNmIdx; 
	 
	@FXML 
	private TextField depStateIdx; 
	 
	@FXML 
	private TextField depCF3Idx; 
	 
	@FXML 
	private TextField covMbrNmIdx; 
	 
	@FXML 
	private TextField covWaivIdx; 
	 
	@FXML 
	private TextField covStrtDtIdx; 
	 
	@FXML 
	private TextField covAnnCovIdx; 
	 
	@FXML 
	private TextField covJanIdx; 
	 
	@FXML 
	private TextField covMayIdx; 
	 
	@FXML 
	private TextField covSeptIdx; 
	 
	@FXML 
	private TextField covDecDtIdx; 
	 
	@FXML 
	private TextField covIneligIdx; 
	 
	@FXML 
	private TextField covEndDtIdx; 
	 
	@FXML 
	private TextField covTxYrIdx; 
	 
	@FXML 
	private TextField covFebIdx; 
	 
	@FXML 
	private TextField covJuneIdx; 
	 
	@FXML 
	private TextField covOctIdx; 
	 
	@FXML 
	private TextField covPlanIdx; 
	 
	@FXML 
	private TextField covDeducIdx; 
	 
	@FXML 
	private TextField covCF1Idx; 
	 
	@FXML 
	private TextField covMarchIdx; 
	 
	@FXML 
	private TextField covJulyIdx; 
	 
	@FXML 
	private TextField covNovIdx; 
	 
	@FXML 
	private TextField covSubNmIdx; 
	 
	@FXML 
	private TextField covMbrShrAmtIdx; 
	 
	@FXML 
	private TextField covCF2Idx; 
	 
	@FXML 
	private TextField covAprilIdx; 
 
	@FXML 
	private TextField covAugustIdx; 
	 
	@FXML 
	private TextField covDecIdx; 
 
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
	private TextField secCF1; 
 
	@FXML 
	private TextField secCF2; 
	 
	@FXML 
	private TextField secCF3; 
 
	@FXML 
	private TextField secCF4; 
	 
	@FXML 
	private TextField secCF5; 
 
	@FXML 
	private TextField cvgCF1; 
	 
	@FXML 
	private TextField cvgCF2; 
 
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
	private Button genRefButton; 
	 
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
	private Button empDOBFormat; 
	 
	@FXML 
	private Button emplStNmParse; 
	 
	@FXML 
	private Button emplCityParse; 
	 
	@FXML 
	private Button emplFParse; 
	 
	@FXML 
	private Button emplMNmParse; 
	 
	@FXML 
	private Button emplHrDtFormat; 
	 
	@FXML 
	private Button emplAdLn1Parse; 
	 
	@FXML 
	private Button emplStateParse; 
	 
	@FXML 
	private Button emplSsnParse; 
	 
	@FXML 
	private Button emplLstNmParse; 
	 
	@FXML 
	private Button emplTrmDtFormat; 
	 
	@FXML 
	private Button emplZipParse; 
	 
	@FXML 
	private Button emplrEmpIdParse; 
	 
	@FXML 
	private Button emplCF1Parse; 
	 
	@FXML 
	private Button emplCF2Parse; 
	 
	@FXML 
	private Button depFrstNmParse; 
	 
	@FXML 
	private Button depEmprIdParse; 
	 
	@FXML 
	private Button depZipParse; 
	 
	@FXML 
	private Button depSSNParse; 
	 
	@FXML 
	private Button depMdNmParse; 
	 
	@FXML 
	private Button depStrtNmParse; 
	 
	@FXML 
	private Button depCityParse; 
	 
	@FXML 
	private Button depDOBFormat; 
	 
	@FXML 
	private Button depLstNmParse; 
	 
	@FXML 
	private Button depAdLn1Parse; 
	 
	@FXML 
	private Button depStateParse; 
	 
	@FXML 
	private Button depCF1Parse; 
	 
	@FXML 
	private Button depCF2Parse; 
	 
	@FXML 
	private Button covWaivParse; 
	 
	@FXML 
	private Button covStrtDtFormat; 
	 
	@FXML 
	private Button covAnnCovParse; 
	 
	@FXML 
	private Button covJanParse; 
	 
	@FXML 
	private Button covMayParse; 
	 
	@FXML 
	private Button covSeptParse; 
	 
	@FXML 
	private Button covDecDtFormat; 
	 
	@FXML 
	private Button covIneligParse; 
	 
	@FXML 
	private Button covEndDtFormat; 
	 
	@FXML 
	private Button covTxYrParse; 
	 
	@FXML 
	private Button covFebParse; 
	 
	@FXML 
	private Button covJuneParse; 
	 
	@FXML 
	private Button covOctParse; 
	 
	@FXML 
	private Button covPlanParse; 
	 
	@FXML 
	private Button covMarchParse; 
	 
	@FXML 
	private Button covJulyParse; 
	 
	@FXML 
	private Button covNovParse; 
	 
	@FXML 
	private Button covCF1Parse; 
	 
	@FXML 
	private Button covAprilParse; 
	 
	@FXML 
	private Button covAugustParse; 

	@FXML 
	private Button covDecParse; 

	@FXML 
	private Button allMonthParse; 
	 
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
	private Label gendRefLabel; 
 
	// read only mode for selected mapper files 
	public boolean readOnly = false;
	public boolean allParse = false;
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
			dfcovName.setText("Mapper for ".concat(DataManager.i().mPipelineSpecification.getName())
					     .concat(",  Id: ").concat(String.valueOf(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()))); 
 
		//set the file type 
		fileType = DataManager.i().mPipelineChannel.getType(); 
		 
		initControls(); 
		loadMapperData(); 
	} 
 
	private void initControls()  
	{ 
		//disable the check boxes 
		dfcovCreateEmployeeCheck.setDisable(false);		 
		dfcovCreateSecondaryCheck.setDisable(false); 
 
		//TODO: REMOVE THIS SINCE WE'RE SPLITTING FORMS 
		//enable the addspec button only for the applicable types 
		dfcovPayAdditionalHoursButton.setVisible(false); 
		switch(fileType) 
		{ 
			case COVERAGE: 
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
	        		case COVERAGE: 
	                	DataManager.i().mDynamicCoverageFileIgnoreRowSpecification = dfcovIgnoreRowSpecsListView.getSelectionModel().getSelectedItem().getCovSpec(); 
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
		empFstNmIdx.setDisable(true); 
		empDOBIdx.setDisable(true); 
		emplJobIdx.setDisable(true); 
		emplStNmIdx.setDisable(true); 
		emplCityIdx.setDisable(true); 
		emplIdIdx.setDisable(true); 
		emplFIdx.setDisable(true); 
		emplMNmIdx.setDisable(true); 
		emplHrDtIdx.setDisable(true); 
		emplPhnIdx.setDisable(true); 
		emplAdLn1Idx.setDisable(true); 
		emplStateIdx.setDisable(true); 
		emplSsnIdx.setDisable(true); 
		emplGendIdx.setDisable(true); 
		emplLstNmIdx.setDisable(true); 
		emplTrmDtIdx.setDisable(true); 
		emplEmlIdx.setDisable(true); 
		emplAdLn2Idx.setDisable(true); 
		emplZipIdx.setDisable(true); 
		emplrEmpIdIdx.setDisable(true); 
		emplCF1Idx.setDisable(true); 
		emplCF2Idx.setDisable(true); 
		emplCF3Idx.setDisable(true); 
		emplCF4Idx.setDisable(true); 
		emplCF5Idx.setDisable(true); 
		depIdIdx.setDisable(true); 
		depFrstNmIdx.setDisable(true); 
		depAdLn1Idx.setDisable(true); 
		depZipIdx.setDisable(true); 
		depCF4Idx.setDisable(true); 
		depSSNIdx.setDisable(true); 
		depMdNmIdx.setDisable(true); 
		depAdLn2Idx.setDisable(true); 
		depCF1Idx.setDisable(true); 
		depCF5Idx.setDisable(true); 
		depDOBIdx.setDisable(true); 
		depLstNmIdx.setDisable(true); 
		depCityIdx.setDisable(true); 
		depCF2Idx.setDisable(true); 
		depEmprIdIdx.setDisable(true); 
		depStrtNmIdx.setDisable(true); 
		depStateIdx.setDisable(true); 
		depCF3Idx.setDisable(true); 
		covMbrNmIdx.setDisable(true); 
		covWaivIdx.setDisable(true); 
		covStrtDtIdx.setDisable(true); 
		covAnnCovIdx.setDisable(true); 
		covJanIdx.setDisable(true); 
		covMayIdx.setDisable(true); 
		covSeptIdx.setDisable(true); 
		covDecDtIdx.setDisable(true); 
		covIneligIdx.setDisable(true); 
		covEndDtIdx.setDisable(true); 
		covTxYrIdx.setDisable(true); 
		covFebIdx.setDisable(true); 
		covJuneIdx.setDisable(true); 
		covOctIdx.setDisable(true); 
		covPlanIdx.setDisable(true); 
		covDeducIdx.setDisable(true); 
		covCF1Idx.setDisable(true); 
		covMarchIdx.setDisable(true); 
		covJulyIdx.setDisable(true); 
		covNovIdx.setDisable(true); 
		covSubNmIdx.setDisable(true); 
		covMbrShrAmtIdx.setDisable(true); 
		covCF2Idx.setDisable(true); 
		covAprilIdx.setDisable(true); 
		covAugustIdx.setDisable(true); 
		covDecIdx.setDisable(true); 
 
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
		emplFParse.setText("+ Pattern"); 
		emplFParse.setFont(Font.font("Veranda", 12.0)); 
		emplFParse.setDisable(true);		 
		emplMNmParse.setText("+ Pattern"); 
		emplMNmParse.setFont(Font.font("Veranda", 12.0)); 
		emplMNmParse.setDisable(true);		 
		emplHrDtFormat.setText("+ Format"); 
		emplHrDtFormat.setFont(Font.font("Veranda", 12.0)); 
		emplHrDtFormat.setDisable(true);		 
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
		depFrstNmParse.setText("+ Pattern"); 
		depFrstNmParse.setFont(Font.font("Veranda", 12.0)); 
		depFrstNmParse.setDisable(true);		 
		depEmprIdParse.setText("+ Pattern"); 
		depEmprIdParse.setFont(Font.font("Veranda", 12.0)); 
		depEmprIdParse.setDisable(true);		 
		depZipParse.setText("+ Pattern"); 
		depZipParse.setFont(Font.font("Veranda", 12.0)); 
		depZipParse.setDisable(true);		 
		depSSNParse.setText("+ Pattern"); 
		depSSNParse.setFont(Font.font("Veranda", 12.0)); 
		depSSNParse.setDisable(true);		 
		depMdNmParse.setText("+ Pattern"); 
		depMdNmParse.setFont(Font.font("Veranda", 12.0)); 
		depMdNmParse.setDisable(true);		 
		depStrtNmParse.setText("+ Pattern"); 
		depStrtNmParse.setFont(Font.font("Veranda", 12.0)); 
		depStrtNmParse.setDisable(true);		 
		depCityParse.setText("+ Pattern"); 
		depCityParse.setFont(Font.font("Veranda", 12.0)); 
		depCityParse.setDisable(true);		 
		depDOBFormat.setText("+ Format"); 
		depDOBFormat.setFont(Font.font("Veranda", 12.0)); 
		depDOBFormat.setDisable(true);		 
		depLstNmParse.setText("+ Pattern"); 
		depLstNmParse.setFont(Font.font("Veranda", 12.0)); 
		depLstNmParse.setDisable(true);		 
		depAdLn1Parse.setText("+ Pattern"); 
		depAdLn1Parse.setFont(Font.font("Veranda", 12.0)); 
		depAdLn1Parse.setDisable(true);		 
		depStateParse.setText("+ Pattern"); 
		depStateParse.setFont(Font.font("Veranda", 12.0)); 
		depStateParse.setDisable(true);		 
		depCF1Parse.setText("+ Pattern"); 
		depCF1Parse.setFont(Font.font("Veranda", 12.0)); 
		depCF1Parse.setDisable(true);		 
		depCF2Parse.setText("+ Pattern"); 
		depCF2Parse.setFont(Font.font("Veranda", 12.0)); 
		depCF2Parse.setDisable(true);		 
		covWaivParse.setText("+ Pattern"); 
		covWaivParse.setFont(Font.font("Veranda", 12.0)); 
		covWaivParse.setDisable(true);		 
		covStrtDtFormat.setText("+ Format"); 
		covStrtDtFormat.setFont(Font.font("Veranda", 12.0)); 
		covStrtDtFormat.setDisable(true);		 
		covAnnCovParse.setText("+ Pattern"); 
		covAnnCovParse.setFont(Font.font("Veranda", 12.0)); 
		covAnnCovParse.setDisable(true);		 
		covJanParse.setText("+ Pattern"); 
		covJanParse.setFont(Font.font("Veranda", 12.0)); 
		covJanParse.setDisable(true);		 
		covMayParse.setText("+ Pattern"); 
		covMayParse.setFont(Font.font("Veranda", 12.0)); 
		covMayParse.setDisable(true);		 
		covSeptParse.setText("+ Pattern"); 
		covSeptParse.setFont(Font.font("Veranda", 12.0)); 
		covSeptParse.setDisable(true);		 
		covDecDtFormat.setText("+ Format"); 
		covDecDtFormat.setFont(Font.font("Veranda", 12.0)); 
		covDecDtFormat.setDisable(true);		 
		covIneligParse.setText("+ Pattern"); 
		covIneligParse.setFont(Font.font("Veranda", 12.0)); 
		covIneligParse.setDisable(true);		 
		covEndDtFormat.setText("+ Format"); 
		covEndDtFormat.setFont(Font.font("Veranda", 12.0)); 
		covEndDtFormat.setDisable(true);		 
		covTxYrParse.setText("+ Pattern"); 
		covTxYrParse.setFont(Font.font("Veranda", 12.0)); 
		covTxYrParse.setDisable(true);		 
		covFebParse.setText("+ Pattern"); 
		covFebParse.setFont(Font.font("Veranda", 12.0)); 
		covFebParse.setDisable(true);		 
		covJuneParse.setText("+ Pattern"); 
		covJuneParse.setFont(Font.font("Veranda", 12.0)); 
		covJuneParse.setDisable(true);		 
		covOctParse.setText("+ Pattern"); 
		covOctParse.setFont(Font.font("Veranda", 12.0)); 
		covOctParse.setDisable(true);		 
		covPlanParse.setText("+ Pattern"); 
		covPlanParse.setFont(Font.font("Veranda", 12.0)); 
		covPlanParse.setDisable(true);		 
		covMarchParse.setText("+ Pattern"); 
		covMarchParse.setFont(Font.font("Veranda", 12.0)); 
		covMarchParse.setDisable(true);		 
		covJulyParse.setText("+ Pattern"); 
		covJulyParse.setFont(Font.font("Veranda", 12.0)); 
		covJulyParse.setDisable(true);		 
		covNovParse.setText("+ Pattern"); 
		covNovParse.setFont(Font.font("Veranda", 12.0)); 
		covNovParse.setDisable(true);		 
		covCF1Parse.setText("+ Pattern"); 
		covCF1Parse.setFont(Font.font("Veranda", 12.0)); 
		covCF1Parse.setDisable(true);		 
		covAprilParse.setText("+ Pattern"); 
		covAprilParse.setFont(Font.font("Veranda", 12.0)); 
		covAprilParse.setDisable(true);		 
		covAugustParse.setText("+ Pattern"); 
		covAugustParse.setFont(Font.font("Veranda", 12.0)); 
		covAugustParse.setDisable(true);		 
		covDecParse.setText("+ Pattern"); 
		covDecParse.setFont(Font.font("Veranda", 12.0)); 
		covDecParse.setDisable(true);		 
 
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
        mapperFields.put(empCovClsSkPn, new MapperField(empCovClsSkPn, empCovCls, empCovClsIdx, null, null, ccRefButton, "COV Class", 70)); 
        mapperFields.put(empFstNmSkPn, new MapperField(empFstNmSkPn, empFstNm, empFstNmIdx, empFstNmParse, null, null, "EE First Name", 90)); 
        mapperFields.put(empDOBSkPn, new MapperField(empDOBSkPn, empDOB, empDOBIdx, null, empDOBFormat, null, "EE DOB" , 57)); 
        mapperFields.put(emplJobSkPn, new MapperField(emplJobSkPn, emplJob, emplJobIdx, null, null, null, "EE Job Title", 76)); 
        mapperFields.put(emplStNmSkPn, new MapperField(emplStNmSkPn, emplStNm, emplStNmIdx, emplStNmParse, null, null, "EE Strt NMBR", 88)); 
        mapperFields.put(emplCitySkPn, new MapperField(emplCitySkPn, emplCity, emplCityIdx, emplCityParse, null, null, "EE City", 52)); 
        mapperFields.put(emplIdSkPn, new MapperField(emplIdSkPn, emplId, emplIdIdx, null, null, null, "ETC EE ID", 66)); 
        mapperFields.put(emplFSkPn, new MapperField(emplFSkPn, emplF, emplFIdx, emplFParse, null, null, "EE FLAG", 60)); 
        mapperFields.put(emplMNmSkPn, new MapperField(emplMNmSkPn, emplMNm, emplMNmIdx, emplMNmParse, null, null, "EE Mid Name", 90)); 
        mapperFields.put(emplHrDtSkPn, new MapperField(emplHrDtSkPn, emplHrDt, emplHrDtIdx, null, emplHrDtFormat, null, "EE Hire Date", 84)); 
        mapperFields.put(emplPhnSkPn, new MapperField(emplPhnSkPn, emplPhn, emplPhnIdx, null, null, null, "EE Phone", 66)); 
        mapperFields.put(emplAdLn1SkPn, new MapperField(emplAdLn1SkPn, emplAdLn1, emplAdLn1Idx, emplAdLn1Parse, null, null, "EE ADD LN 1", 85)); 
        mapperFields.put(emplStateSkPn, new MapperField(emplStateSkPn, emplState, emplStateIdx, emplStateParse, null, null, "EE State", 58)); 
        mapperFields.put(emplSsnSkPn, new MapperField(emplSsnSkPn, emplSsn, emplSsnIdx, emplSsnParse, null, null, "EE SSN", 54)); 
        mapperFields.put(emplGendSkPn, new MapperField(emplGendSkPn, emplGend, emplGendIdx, null, null, genRefButton, "Gender", 56)); 
        mapperFields.put(emplLstNmSkPn, new MapperField(emplLstNmSkPn, emplLstNm, emplLstNmIdx, emplLstNmParse, null, null, "EE Last Name", 89)); 
        mapperFields.put(emplTrmDtSkPn, new MapperField(emplTrmDtSkPn, emplTrmDt, emplTrmDtIdx, null, emplTrmDtFormat, null, "EE Term Date", 89)); 
        mapperFields.put(emplEmlSkPn, new MapperField(emplEmlSkPn, emplEml, emplEmlIdx, null, null, null, "EE Email", 60)); 
        mapperFields.put(emplAdLn2SkPn, new MapperField(emplAdLn2SkPn, emplAdLn2, emplAdLn2Idx, null, null, null, "EE ADD LN 2", 85)); 
        mapperFields.put(emplZipSkPn, new MapperField(emplZipSkPn, emplZip, emplZipIdx, emplZipParse, null, null, "EE Zip", 48)); 
        mapperFields.put(emplrEmpIdSkPn, new MapperField(emplrEmpIdSkPn, emplrEmpId, emplrEmpIdIdx, emplrEmpIdParse, null, null, "ER EE ID", 59)); 
        mapperFields.put(emplCF1SkPn, new MapperField(emplCF1SkPn, emplCF1, emplCF1Idx, emplCF1Parse, null, null, "EE CF 1", 55)); 
        mapperFields.put(emplCF2SkPn, new MapperField(emplCF2SkPn, emplCF2, emplCF2Idx, emplCF2Parse, null, null, "EE CF 2", 55)); 
        mapperFields.put(emplCF3SkPn, new MapperField(emplCF3SkPn, emplCF3, emplCF3Idx, null, null, null, "EE CF 3", 55)); 
        mapperFields.put(emplCF4SkPn, new MapperField(emplCF4SkPn, emplCF4, emplCF4Idx, null, null, null, "EE CF 4", 55)); 
        mapperFields.put(emplCF5SkPn, new MapperField(emplCF5SkPn, emplCF5, emplCF5Idx, null, null, null, "EE CF 5", 55)); 
 
        //DEPENDENT SECTION 
        mapperFields.put(depIdSkPn, new MapperField(depIdSkPn, depId, depIdIdx, null, null, null, "ETC DEP ID", 76)); 
        mapperFields.put(depFrstNmSkPn, new MapperField(depFrstNmSkPn, depFrstNm, depFrstNmIdx, depFrstNmParse, null, null, "DEP First Name", 100)); 
        mapperFields.put(depEmprIdSkPn, new MapperField(depEmprIdSkPn, depEmprId, depEmprIdIdx, depEmprIdParse, null, null, "DEP EMPR ID", 87)); 
        mapperFields.put(depAdLn2SkPn, new MapperField(depAdLn2SkPn, depAdLn2, depAdLn2Idx, null, null, null, "DEP ADD LN 2", 95)); 
        mapperFields.put(depZipSkPn, new MapperField(depZipSkPn, depZip, depZipIdx, depZipParse, null, null, "DEP Zip", 58)); 
        mapperFields.put(depSSNSkPn, new MapperField(depSSNSkPn, depSSN, depSSNIdx, depSSNParse, null, null, "DEP SSN", 62)); 
        mapperFields.put(depMdNmSkPn, new MapperField(depMdNmSkPn, depMdNm, depMdNmIdx, depMdNmParse, null, null, "DEP Mid Name", 99)); 
        mapperFields.put(depStrtNmSkPn, new MapperField(depStrtNmSkPn, depStrtNm, depStrtNmIdx, depStrtNmParse, null, null, "DEP Strt NMBR", 97)); 
        mapperFields.put(depCitySkPn, new MapperField(depCitySkPn, depCity, depCityIdx, depCityParse, null, null, "DEP City", 62)); 
        mapperFields.put(depDOBSkPn, new MapperField(depDOBSkPn, depDOB, depDOBIdx, null, depDOBFormat, null, "DEP DOB", 65)); 
        mapperFields.put(depLstNmSkPn, new MapperField(depLstNmSkPn, depLstNm, depLstNmIdx, depLstNmParse, null, null, "DEP Last Name", 99)); 
        mapperFields.put(depAdLn1SkPn, new MapperField(depAdLn1SkPn, depAdLn1, depAdLn1Idx, depAdLn1Parse, null, null, "DEP ADD LN 1", 95)); 
        mapperFields.put(depStateSkPn, new MapperField(depStateSkPn, depState, depStateIdx, depStateParse, null, null, "DEP State", 68)); 
        mapperFields.put(depCF1SkPn, new MapperField(depCF1SkPn, depCF1, depCF1Idx, depCF1Parse, null, null, "DEP CF 1", 63)); 
        mapperFields.put(depCF2SkPn, new MapperField(depCF2SkPn, depCF2, depCF2Idx, depCF2Parse, null, null, "DEP CF 2", 63)); 
        mapperFields.put(depCF3SkPn, new MapperField(depCF3SkPn, depCF3, depCF3Idx, null, null, null, "DEP CF 3", 63)); 
        mapperFields.put(depCF4SkPn, new MapperField(depCF4SkPn, depCF4, depCF4Idx, null, null, null, "DEP CF 4", 63)); 
        mapperFields.put(depCF5SkPn, new MapperField(depCF5SkPn, depCF5, depCF5Idx, null, null, null, "DEP CF 5", 63)); 
 
        //COVERAGE SECTION 
        mapperFields.put(covMbrNmSkPn, new MapperField(covMbrNmSkPn, covMbrNm, covMbrNmIdx, null, null, null, "MBR NUM", 72)); 
        mapperFields.put(covWaivSkPn, new MapperField(covWaivSkPn, covWaiv, covWaivIdx, covWaivParse, null, null, "Waived", 55)); 
        mapperFields.put(covStrtDtSkPn, new MapperField(covStrtDtSkPn, covStrtDt, covStrtDtIdx, null, covStrtDtFormat, null, "COV Start Date", 100)); 
        mapperFields.put(covAnnCovSkPn, new MapperField(covAnnCovSkPn, covAnnCov, covAnnCovIdx, covAnnCovParse, null, null, "COV ANN COV", 99)); 
        mapperFields.put(covJanSkPn, new MapperField(covJanSkPn, covJan, covJanIdx, covJanParse, null, allParseJan, "January", 56)); 
        mapperFields.put(covMaySkPn, new MapperField(covMaySkPn, covMay, covMayIdx, covMayParse, null, allParseMay, "May", 40)); 
        mapperFields.put(covSeptSkPn, new MapperField(covSeptSkPn, covSept, covSeptIdx, covSeptParse, null, allParseSep, "September", 75)); 
        mapperFields.put(covDecDtSkPn, new MapperField(covDecDtSkPn, covDecDt, covDecDtIdx, null, covDecDtFormat, null, "Decision Date", 91)); 
        mapperFields.put(covIneligSkPn, new MapperField(covIneligSkPn, covInelig, covIneligIdx, covIneligParse, null, null, "Ineligible", 65)); 
        mapperFields.put(covEndDtSkPn, new MapperField(covEndDtSkPn, covEndDt, covEndDtIdx, null, covEndDtFormat, null, "COV End Date", 94)); 
        mapperFields.put(covTxYrSkPn, new MapperField(covTxYrSkPn, covTxYr, covTxYrIdx, covTxYrParse, null, null, "Tax Year", 61)); 
        mapperFields.put(covFebSkPn, new MapperField(covFebSkPn, covFeb, covFebIdx, covFebParse, null, allParseFeb, "February", 62)); 
        mapperFields.put(covJuneSkPn, new MapperField(covJuneSkPn, covJune, covJuneIdx, covJuneParse, null, allParseJun, "June", 40)); 
        mapperFields.put(covOctSkPn, new MapperField(covOctSkPn, covOct, covOctIdx, covOctParse, null, allParseOct, "October", 59)); 
        mapperFields.put(covPlanSkPn, new MapperField(covPlanSkPn, covPlan, covPlanIdx, covPlanParse, null, null, "Plan ID", 54)); 
        mapperFields.put(covDeducSkPn, new MapperField(covDeducSkPn, covDeduc, covDeducIdx, null, null, null, "Deduction", 72)); 
        mapperFields.put(covSubNmSkPn, new MapperField(covSubNmSkPn, covSubNm, covSubNmIdx, null, null, null, "SUBSCBR NUM", 99)); 
        mapperFields.put(covMarchSkPn, new MapperField(covMarchSkPn, covMarch, covMarchIdx, covMarchParse, null, allParseMar, "March", 49)); 
        mapperFields.put(covJulySkPn, new MapperField(covJulySkPn, covJuly, covJulyIdx, covJulyParse, null, allParseJul, "July", 40)); 
        mapperFields.put(covNovSkPn, new MapperField(covNovSkPn, covNov, covNovIdx, covNovParse, null, allParseNov, "November", 73)); 
        mapperFields.put(covMbrShrAmtSkPn, new MapperField(covMbrShrAmtSkPn, covMbrShrAmt, covMbrShrAmtIdx, null, null, null, "MBR SHR AMT", 96)); 
        mapperFields.put(covCF1SkPn, new MapperField(covCF1SkPn, covCF1, covCF1Idx, covCF1Parse, null, null, "COV CF 1", 68)); 
        mapperFields.put(covCF2SkPn, new MapperField(covCF2SkPn, covCF2, covCF2Idx, null, null, null, "COV CF 2", 68)); 
        mapperFields.put(covAprilSkPn, new MapperField(covAprilSkPn, covApril, covAprilIdx, covAprilParse, null, allParseApr, "April", 40)); 
        mapperFields.put(covAugustSkPn, new MapperField(covAugustSkPn, covAugust, covAugustIdx, covAugustParse, null, allParseAug, "August", 53)); 
        mapperFields.put(covDecSkPn, new MapperField(covDecSkPn, covDec, covDecIdx, covDecParse, null, allParseDec, "December", 71)); 
 
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
        		DynamicCoverageFileSpecificationRequest dynCovReq = new DynamicCoverageFileSpecificationRequest(DataManager.i().mDynamicCoverageFileSpecification); 
        		dynCovReq.setId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
 
        		DataManager.i().mDynamicCoverageFileSpecification = AdminPersistenceManager.getInstance().get(dynCovReq); 
 
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
		updateCoverageFileData(); 
		loadIgnoreRows(DataManager.i().mDynamicCoverageFileSpecification); 
		checkReadOnly(); 
        EtcAdmin.i().setStatusMessage("Ready"); 
        EtcAdmin.i().setProgress(0); 
	} 
 
	private void loadIgnoreRows(DynamicCoverageFileSpecification fSpec) 
	{ 
		try {
			DynamicCoverageFileIgnoreRowSpecificationRequest request = new DynamicCoverageFileIgnoreRowSpecificationRequest(DataManager.i().mDynamicCoverageFileIgnoreRowSpecification); 
			request.setSpecificationId(fSpec.getId()); 
			DataManager.i().mDynamicCoverageFileIgnoreRowSpecifications = AdminPersistenceManager.getInstance().getAll(request);
		} catch (CoreException e) { DataManager.i().log(Level.SEVERE, e); } 
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		 
	    List<HBoxIgnoreRowCell> ignoreRowList = new ArrayList<>(); 
	     
		for(DynamicCoverageFileIgnoreRowSpecification spec : DataManager.i().mDynamicCoverageFileIgnoreRowSpecifications) 
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
	private void updateCoverageFileData() 
	{ 
		DynamicCoverageFileSpecification fSpec = DataManager.i().mDynamicCoverageFileSpecification; 
		
		if(fSpec != null)  
		{ 
			readOnly = fSpec.isLocked(); 
			Utils.updateControl(dfcovNameLabel,fSpec.getName()); 
			Utils.updateControl(dfcovTabIndexLabel,String.valueOf(fSpec.getTabIndex()));
			Utils.updateControl(dfcovHeaderRowIndexLabel,String.valueOf(fSpec.getHeaderRowIndex())); 
			Utils.updateControl(dfcovCreateEmployeeCheck,fSpec.isCreateEmployee()); 
			dfcovCreateSecondaryCheck.setVisible(true); 
			dfcovCreateSecondaryCheck.setText("Create Secondary"); 
			Utils.updateControl(dfcovCreateSecondaryCheck,fSpec.isCreateDependent()); 
			dfcovMapEEtoERCheck.setVisible(true); 
			Utils.updateControl(dfcovMapEEtoERCheck,fSpec.isMapEEtoER()); 
			dfcovCreateEmployeeCheck.setVisible(true); 
			Utils.updateControl(dfcovCreateEmployeeCheck,fSpec.isCreateEmployee()); 
			dfcovCreatePlanCheck.setVisible(true); 
			Utils.updateControl(dfcovCreatePlanCheck,fSpec.isCreateBenefit()); 
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
			Utils.updateControl(secCF1, fSpec.getDepCfld1Name()); 
			Utils.updateControl(secCF2, fSpec.getDepCfld2Name()); 
			Utils.updateControl(secCF3, fSpec.getDepCfld3Name()); 
			Utils.updateControl(secCF4, fSpec.getDepCfld4Name()); 
			Utils.updateControl(secCF5, fSpec.getDepCfld5Name()); 
			Utils.updateControl(cvgCF1, fSpec.getCovCfld1Name()); 
			Utils.updateControl(cvgCF2, fSpec.getCovCfld2Name()); 
 
			//reference counts 
			setEmpRefCount(getCoverageFileEmployerRefCount()); 
			setCvgRefCount(getCoverageFileCvgClassRefCount()); 
			setGendRefCount(getCoverageFileGenderTypeRefCount()); 
 
			//update the column field table 
			loadFieldSet1(fSpec);	  
			loadFieldSet2(fSpec); 
			loadFieldSet3(fSpec); 
		} 
	} 
 
	//Generate the coverage spec type 
	private void loadFieldSet1(DynamicCoverageFileSpecification fSpec) 
	{ 
		//EMPLOYEE SECTION 
		enableFieldSet(fSpec.isErCol(), fSpec.getErColIndex(), emprIdSkPn, null, null); 
		enableFieldSet(fSpec.isCvgGroupCol(), fSpec.getCvgGroupColIndex(), empCovClsSkPn, null, null); 
		enableFieldSet(fSpec.isfNameCol(), fSpec.getfNameColIndex(), empFstNmSkPn, fSpec.getfNameParsePatternId(), null); 
		enableFieldSet(fSpec.isDobCol(), fSpec.getDobColIndex(), empDOBSkPn, null, fSpec.getDobFormatId()); 
		enableFieldSet(fSpec.isJobTtlCol(), fSpec.getJobTtlColIndex(), emplJobSkPn, null, null); 
		enableFieldSet(fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), emplStNmSkPn, fSpec.getStreetParsePatternId(), null); 
		enableFieldSet(fSpec.isCityCol(), fSpec.getCityColIndex(), emplCitySkPn, fSpec.getCityParsePatternId(), null); 
		enableFieldSet(fSpec.isEtcRefCol(), fSpec.getEtcRefColIndex(), emplIdSkPn, null, null); 
		enableFieldSet(fSpec.isEeFlagCol(), fSpec.getEeFlagColIndex(), emplFSkPn, fSpec.getEeFlagTrueParsePatternId(), null); 
		enableFieldSet(fSpec.ismNameCol(), fSpec.getmNameColIndex(), emplMNmSkPn, fSpec.getmNameParsePatternId(), null); 
		enableFieldSet(fSpec.isHireDtCol(), fSpec.getHireDtColIndex(), emplHrDtSkPn, null, fSpec.getHireDtFormatId()); 
		enableFieldSet(fSpec.isPhoneCol(), fSpec.getPhoneColIndex(), emplPhnSkPn, null, null); 
		enableFieldSet(fSpec.isStreetCol(), fSpec.getStreetColIndex(), emplAdLn1SkPn, fSpec.getStreetParsePatternId(), null); 
		enableFieldSet(fSpec.isStateCol(), fSpec.getStateColIndex(), emplStateSkPn, fSpec.getStateParsePatternId(), null); 
		enableFieldSet(fSpec.isSsnCol(), fSpec.getSsnColIndex(), emplSsnSkPn, fSpec.getSsnParsePatternId(), null); 
		enableFieldSet(fSpec.isGenderCol(), fSpec.getGenderColIndex(), emplGendSkPn, null, null); 
		enableFieldSet(fSpec.islNameCol(), fSpec.getlNameColIndex(), emplLstNmSkPn, fSpec.getlNameParsePatternId(), null); 
		enableFieldSet(fSpec.isTermDtCol(), fSpec.getTermDtColIndex(), emplTrmDtSkPn, null, fSpec.getTermDtFormatId()); 
		enableFieldSet(fSpec.isEmlCol(), fSpec.getEmlColIndex(), emplEmlSkPn, null, null); 
		enableFieldSet(fSpec.isLin2Col(), fSpec.getLin2ColIndex(), emplAdLn2SkPn, null, null); 
		enableFieldSet(fSpec.isZipCol(), fSpec.getZipColIndex(), emplZipSkPn, fSpec.getZipParsePatternId(), null); 
		enableFieldSet(fSpec.isErRefCol(), fSpec.getErRefColIndex(), emplrEmpIdSkPn, fSpec.getErRefParsePatternId(), null); 
		enableFieldSet(fSpec.isCfld1Col(), fSpec.getCfld1ColIndex(), emplCF1SkPn, fSpec.getCfld1ParsePatternId(), null); 
		enableFieldSet(fSpec.isCfld2Col(), fSpec.getCfld2ColIndex(), emplCF2SkPn, fSpec.getCfld2ParsePatternId(), null); 
		enableFieldSet(fSpec.isCfld3Col(), fSpec.getCfld3ColIndex(), emplCF3SkPn, null, null); 
		enableFieldSet(fSpec.isCfld4Col(), fSpec.getCfld4ColIndex(), emplCF4SkPn, null, null); 
		enableFieldSet(fSpec.isCfld5Col(), fSpec.getCfld5ColIndex(), emplCF5SkPn, null, null); 
	} 
 
	private void loadFieldSet2(DynamicCoverageFileSpecification fSpec)  
	{ 
		//COVERAGE SECTION 
		enableFieldSet(fSpec.isMbrCol(), fSpec.getMbrColIndex(), covMbrNmSkPn, null, null); 
		enableFieldSet(fSpec.isWavdCol(), fSpec.getWavdColIndex(), covWaivSkPn, fSpec.getWavdTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isCovStartDtCol(), fSpec.getCovStartDtColIndex(), covStrtDtSkPn, null, fSpec.getCovStartDtFormatId()); 
		enableFieldSet(fSpec.isTySelCol(), fSpec.getTySelColIndex(), covAnnCovSkPn, fSpec.getTySelTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isJanSelCol(), fSpec.getJanSelColIndex(), covJanSkPn, fSpec.getJanSelTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isMaySelCol(), fSpec.getMaySelColIndex(), covMaySkPn, fSpec.getMaySelTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isSepSelCol(), fSpec.getSepSelColIndex(), covSeptSkPn, fSpec.getSepSelTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isDecisionDtCol(), fSpec.getDecisionDtColIndex(), covDecDtSkPn, null, fSpec.getDecisionDtFormatId()); 
		enableFieldSet(fSpec.isInelCol(), fSpec.getInelColIndex(), covIneligSkPn, fSpec.getInelTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isCovEndDtCol(), fSpec.getCovEndDtColIndex(), covEndDtSkPn, null, fSpec.getCovEndDtFormatId()); 
		enableFieldSet(fSpec.isTyCol(), fSpec.getTyColIndex(), covTxYrSkPn, fSpec.getTyParsePatternId(), null); 
		enableFieldSet(fSpec.isFebSelCol(), fSpec.getFebSelColIndex(), covFebSkPn, fSpec.getFebSelTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isJunSelCol(), fSpec.getJunSelColIndex(), covJuneSkPn, fSpec.getJunSelTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isOctSelCol(), fSpec.getOctSelColIndex(), covOctSkPn, fSpec.getOctSelTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isPlanRefCol(), fSpec.getPlanRefColIndex(), covPlanSkPn, fSpec.getPlanRefParsePatternId(), null); 
		enableFieldSet(fSpec.isDedCol(), fSpec.getDedColIndex(), covDeducSkPn, null, null); 
		enableFieldSet(fSpec.isSubcrbrCol(), fSpec.getSubcrbrColIndex(), covSubNmSkPn, null, null); 
		enableFieldSet(fSpec.isMarSelCol(), fSpec.getMarSelColIndex(), covMarchSkPn, fSpec.getMarSelTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isJulSelCol(), fSpec.getJulSelColIndex(), covJulySkPn, fSpec.getJulSelTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isNovSelCol(), fSpec.getNovSelColIndex(), covNovSkPn, fSpec.getNovSelTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isMbrShareCol(), fSpec.getMbrShareColIndex(), covMbrShrAmtSkPn, null, null); 
		enableFieldSet(fSpec.isCovCfld1Col(), fSpec.getCovCfld1ColIndex(), covCF1SkPn, fSpec.getCovCfld1ParsePatternId(), null); 
		enableFieldSet(fSpec.isCovCfld2Col(), fSpec.getCovCfld2ColIndex(), covCF2SkPn, null, null); 
		enableFieldSet(fSpec.isAprSelCol(), fSpec.getAprSelColIndex(), covAprilSkPn, fSpec.getAprSelTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isAugSelCol(), fSpec.getAugSelColIndex(), covAugustSkPn, fSpec.getAugSelTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isDecSelCol(), fSpec.getDecSelColIndex(), covDecSkPn, fSpec.getDecSelTrueParsePatternId(), null);
	}
 
	private void loadFieldSet3(DynamicCoverageFileSpecification fSpec)  
	{ 
		//DEPENDENT SECTION 
		enableFieldSet(fSpec.isDepEtcRefCol(), fSpec.getDepEtcRefColIndex(), depIdSkPn, null, null); 
		enableFieldSet(fSpec.isDepFNameCol(), fSpec.getDepFNameColIndex(), depFrstNmSkPn, fSpec.getDepFNameParsePatternId(), null); 
		enableFieldSet(fSpec.isDepErRefCol(), fSpec.getDepErRefColIndex(), depEmprIdSkPn, fSpec.getDepErRefParsePatternId(), null); 
		enableFieldSet(fSpec.isDepLin2Col(), fSpec.getDepLin2ColIndex(), depAdLn2SkPn, null, null); 
		enableFieldSet(fSpec.isDepZipCol(), fSpec.getDepZipColIndex(), depZipSkPn, fSpec.getDepZipParsePatternId(), null); 
		enableFieldSet(fSpec.isDepSSNCol(), fSpec.getDepSSNColIndex(), depSSNSkPn, fSpec.getDepSSNParsePatternId(), null); 
		enableFieldSet(fSpec.isDepMNameCol(), fSpec.getDepMNameColIndex(), depMdNmSkPn, fSpec.getDepMNameParsePatternId(), null); 
		enableFieldSet(fSpec.isDepStreetNumCol(), fSpec.getDepStreetNumColIndex(), depStrtNmSkPn, fSpec.getDepStreetParsePatternId(), null); 
		enableFieldSet(fSpec.isDepCityCol(), fSpec.getDepCityColIndex(), depCitySkPn, fSpec.getDepCityParsePatternId(), null); 
		enableFieldSet(fSpec.isDepDOBCol(), fSpec.getDepDOBColIndex(), depDOBSkPn, null, fSpec.getDepDOBFormatId()); 
		enableFieldSet(fSpec.isDepLNameCol(), fSpec.getDepLNameColIndex(), depLstNmSkPn, fSpec.getDepLNameParsePatternId(), null); 
		enableFieldSet(fSpec.isDepStreetCol(), fSpec.getDepStreetColIndex(), depAdLn1SkPn, fSpec.getDepStreetParsePatternId(), null); 
		enableFieldSet(fSpec.isDepStateCol(), fSpec.getDepStateColIndex(), depStateSkPn, fSpec.getDepStateParsePatternId(), null); 
		enableFieldSet(fSpec.isDepCfld1Col(), fSpec.getDepCfld1ColIndex(), depCF1SkPn, fSpec.getDepCfld1ParsePatternId(), null); 
		enableFieldSet(fSpec.isDepCfld2Col(), fSpec.getDepCfld2ColIndex(), depCF2SkPn, fSpec.getDepCfld2ParsePatternId(), null); 
		enableFieldSet(fSpec.isDepCfld3Col(), fSpec.getDepCfld3ColIndex(), depCF3SkPn, null, null); 
		enableFieldSet(fSpec.isDepCfld4Col(), fSpec.getDepCfld4ColIndex(), depCF4SkPn, null, null); 
		enableFieldSet(fSpec.isDepCfld5Col(), fSpec.getDepCfld5ColIndex(), depCF5SkPn, null, null); 
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
		DynamicCoverageFileSpecification fSpec = DataManager.i().mDynamicCoverageFileSpecification; 

		if(fld.getParsePatternId() != null) { fld.setTitle(fld.getTitle().replace(" (P:" + fld.getParsePatternId().toString() + ")", "")); fld.setParsePatternId(null); } 
		if(fld.getDateFormatId() != null) { fld.setTitle(fld.getTitle().replace(" (F:" + fld.getDateFormatId().toString() + ")", "")); fld.setDateFormatId(null); } 

		if(emprId.isSelected() == false){ fSpec.setErCol(false); fSpec.setErColIndex(0); } 
		if(empCovCls.isSelected() == false){ fSpec.setCvgGroupCol(false); fSpec.setCvgGroupColIndex(0); } 
		if(empFstNm.isSelected() == false){ fSpec.setfNameParsePattern(null); fSpec.setfNameCol(false); fSpec.setfNameColIndex(0);  
		   empFstNmParse.setFont(Font.font("Veranda", 12.0)); empFstNmParse.setText("+ Pattern"); } 
		if(empDOB.isSelected() == false){ fSpec.setDobCol(false); fSpec.setDobColIndex(0); fSpec.setDobFormat(null); 
		   empDOBFormat.setFont(Font.font("Veranda", 12.0)); empDOBFormat.setText("+ Format");} 
		if(emplJob.isSelected() == false){ fSpec.setJobTtlCol(false); fSpec.setJobTtlColIndex(0); } 
		if(emplStNm.isSelected() == false){ fSpec.setStreetNumCol(false); fSpec.setStreetNumColIndex(0); fSpec.setStreetParsePattern(null); 
		   emplStNmParse.setFont(Font.font("Veranda", 12.0)); emplStNmParse.setText("+ Pattern"); } 
		if(emplCity.isSelected() == false){ fSpec.setCityCol(false); fSpec.setCityColIndex(0); fSpec.setCityParsePattern(null); 
		   emplCityParse.setFont(Font.font("Veranda", 12.0)); emplCityParse.setText("+ Pattern"); } 
		if(emplId.isSelected() == false){ fSpec.setEtcRefCol(false); fSpec.setEtcRefColIndex(0); } 
		if(emplF.isSelected() == false){ fSpec.setEeFlagCol(false); fSpec.setEeFlagColIndex(0); fSpec.setEeFlagTrueParsePattern(null); 
		   emplFParse.setFont(Font.font("Veranda", 12.0)); emplFParse.setText("+ Pattern"); } 
		if(emplMNm.isSelected() == false){ fSpec.setmNameCol(false); fSpec.setmNameColIndex(0); fSpec.setmNameParsePattern(null); 
		   emplMNmParse.setFont(Font.font("Veranda", 12.0)); emplMNmParse.setText("+ Pattern"); } 
		if(emplHrDt.isSelected() == false){ fSpec.setHireDtCol(false); fSpec.setHireDtColIndex(0); fSpec.setHireDtFormat(null); 
		   emplHrDtFormat.setFont(Font.font("Veranda", 12.0)); emplHrDtFormat.setText("+ Format"); } 
		if(emplPhn.isSelected() == false){ fSpec.setPhoneCol(false); fSpec.setPhoneColIndex(0); } 
		if(emplAdLn1.isSelected() == false){ fSpec.setStreetCol(false); fSpec.setStreetColIndex(0); fSpec.setStreetParsePattern(null); 
		   emplAdLn1Parse.setFont(Font.font("Veranda", 12.0)); emplAdLn1Parse.setText("+ Pattern"); } 
		if(emplState.isSelected() == false){ fSpec.setStateCol(false); fSpec.setStateColIndex(0); fSpec.setStateParsePattern(null); 
		   emplStateParse.setFont(Font.font("Veranda", 12.0)); emplStateParse.setText("+ Pattern"); } 
		if(emplSsn.isSelected() == false){ fSpec.setSsnCol(false); fSpec.setSsnColIndex(0); fSpec.setSsnParsePattern(null); 
		   emplSsnParse.setFont(Font.font("Veranda", 12.0)); emplSsnParse.setText("+ Pattern"); } 
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
		if(emplCF1.isSelected() == false){ fSpec.setCfld1Col(false); fSpec.setCfld1ColIndex(0); fSpec.setCfld1ParsePattern(null);
		   emplCF1Parse.setFont(Font.font("Veranda", 12.0)); emplCF1Parse.setText("+ Pattern"); eeCF1.setText(""); } 
		if(emplCF2.isSelected() == false){ fSpec.setCfld2Col(false); fSpec.setCfld2ColIndex(0); fSpec.setCfld2ParsePattern(null);
		   emplCF2Parse.setFont(Font.font("Veranda", 12.0)); emplCF2Parse.setText("+ Pattern"); eeCF2.setText(""); } 
		if(emplCF3.isSelected() == false){ fSpec.setCfld3Col(false); fSpec.setCfld3ColIndex(0); eeCF3.setText(""); } 
		if(emplCF4.isSelected() == false){ fSpec.setCfld4Col(false); fSpec.setCfld4ColIndex(0); eeCF4.setText(""); } 
		if(emplCF5.isSelected() == false){ fSpec.setCfld5Col(false); fSpec.setCfld5ColIndex(0); eeCF5.setText(""); } 
		if(covMbrNm.isSelected() == false){ fSpec.setMbrCol(false); fSpec.setMbrColIndex(0); } 
		if(covWaiv.isSelected() == false){ fSpec.setWavdCol(false); fSpec.setWavdColIndex(0); fSpec.setWavdTrueParsePattern(null); 
		   covWaivParse.setFont(Font.font("Veranda", 12.0)); covWaivParse.setText("+ Pattern"); } 
		if(covStrtDt.isSelected() == false){ fSpec.setCovStartDtCol(false); fSpec.setCovStartDtColIndex(0); fSpec.setCovStartDtFormat(null); 
		   covStrtDtFormat.setFont(Font.font("Veranda", 12.0)); covStrtDtFormat.setText("+ Format"); } 
		if(covAnnCov.isSelected() == false){ fSpec.setTySelCol(false); fSpec.setTySelColIndex(0); fSpec.setTySelTrueParsePattern(null); 
		   covAnnCovParse.setFont(Font.font("Veranda", 12.0)); covAnnCovParse.setText("+ Pattern"); } 
		if(covJan.isSelected() == false){ fSpec.setJanSelCol(false); fSpec.setJanSelColIndex(0); fSpec.setJanSelTrueParsePattern(null); 
		   covJanParse.setFont(Font.font("Veranda", 12.0)); covJanParse.setText("+ Pattern"); } 
		if(covMay.isSelected() == false){ fSpec.setMaySelCol(false); fSpec.setMaySelColIndex(0); fSpec.setMaySelTrueParsePattern(null); 
		   covMayParse.setFont(Font.font("Veranda", 12.0)); covMayParse.setText("+ Pattern"); } 
		if(covSept.isSelected() == false){ fSpec.setSepSelCol(false); fSpec.setSepSelColIndex(0); fSpec.setSepSelTrueParsePattern(null); 
		   covSeptParse.setFont(Font.font("Veranda", 12.0)); covSeptParse.setText("+ Pattern"); } 
		if(covDecDt.isSelected() == false){ fSpec.setDecisionDtCol(false); fSpec.setDecisionDtColIndex(0); fSpec.setDecisionDtFormat(null); 
		   covDecDtFormat.setFont(Font.font("Veranda", 12.0)); covDecDtFormat.setText("+ Format"); } 
		if(covInelig.isSelected() == false){ fSpec.setInelCol(false); fSpec.setInelColIndex(0); fSpec.setInelTrueParsePattern(null); 
		   covIneligParse.setFont(Font.font("Veranda", 12.0)); covIneligParse.setText("+ Pattern"); } 
		if(covEndDt.isSelected() == false){ fSpec.setCovEndDtCol(false); fSpec.setCovEndDtColIndex(0); fSpec.setCovEndDtFormat(null); 
		   covEndDtFormat.setFont(Font.font("Veranda", 12.0)); covEndDtFormat.setText("+ Format"); } 
		if(covTxYr.isSelected() == false){ fSpec.setTyCol(false); fSpec.setTyColIndex(0); fSpec.setTyParsePattern(null); 
		   covTxYrParse.setFont(Font.font("Veranda", 12.0)); covTxYrParse.setText("+ Pattern"); } 
		if(covFeb.isSelected() == false){ fSpec.setFebSelCol(false); fSpec.setFebSelColIndex(0); fSpec.setFebSelTrueParsePattern(null); 
		   covFebParse.setFont(Font.font("Veranda", 12.0)); covFebParse.setText("+ Pattern"); } 
		if(covJune.isSelected() == false){ fSpec.setJunSelCol(false); fSpec.setJunSelColIndex(0); fSpec.setJunSelTrueParsePattern(null); 
		   covJuneParse.setFont(Font.font("Veranda", 12.0)); covJuneParse.setText("+ Pattern"); } 
		if(covOct.isSelected() == false){ fSpec.setOctSelCol(false); fSpec.setOctSelColIndex(0); fSpec.setOctSelTrueParsePattern(null); 
		   covOctParse.setFont(Font.font("Veranda", 12.0)); covOctParse.setText("+ Pattern"); } 
		if(covPlan.isSelected() == false){ fSpec.setPlanRefCol(false); fSpec.setPlanRefColIndex(0); fSpec.setPlanRefParsePattern(null); 
		   covPlanParse.setFont(Font.font("Veranda", 12.0)); covPlanParse.setText("+ Pattern"); } 
		if(covDeduc.isSelected() == false){ fSpec.setDedCol(false); fSpec.setDedColIndex(0); } 
		if(covCF1.isSelected() == false){ fSpec.setCovCfld1Col(false); fSpec.setCovCfld1ColIndex(0); fSpec.setCovCfld1ParsePattern(null);
		   covCF1Parse.setFont(Font.font("Veranda", 12.0)); covCF1Parse.setText("+ Pattern"); cvgCF1.setText(""); } 
		if(covMarch.isSelected() == false){ fSpec.setMarSelCol(false); fSpec.setMarSelColIndex(0); fSpec.setMarSelTrueParsePattern(null); 
		   covMarchParse.setFont(Font.font("Veranda", 12.0)); covMarchParse.setText("+ Pattern"); } 
		if(covJuly.isSelected() == false){ fSpec.setJulSelCol(false); fSpec.setJulSelColIndex(0); fSpec.setJulSelTrueParsePattern(null); 
		   covJulyParse.setFont(Font.font("Veranda", 12.0)); covJulyParse.setText("+ Pattern"); } 
		if(covNov.isSelected() == false){ fSpec.setNovSelCol(false); fSpec.setNovSelColIndex(0); fSpec.setNovSelTrueParsePattern(null); 
		   covNovParse.setFont(Font.font("Veranda", 12.0)); covNovParse.setText("+ Pattern"); }
		if(covSubNm.isSelected() == false){ fSpec.setSubcrbrCol(false); fSpec.setSubcrbrColIndex(0); } 
		if(covMbrShrAmt.isSelected() == false){ fSpec.setMbrShareCol(false); fSpec.setMbrShareColIndex(0); } 
		if(covCF2.isSelected() == false){ fSpec.setCovCfld2Col(false); fSpec.setCovCfld2ColIndex(0); cvgCF2.setText(""); } 
		if(covApril.isSelected() == false){ fSpec.setAprSelCol(false); fSpec.setAprSelColIndex(0); fSpec.setAprSelTrueParsePattern(null); 
		   covAprilParse.setFont(Font.font("Veranda", 12.0)); covAprilParse.setText("+ Pattern"); } 
		if(covAugust.isSelected() == false){ fSpec.setAugSelCol(false); fSpec.setAugSelColIndex(0); fSpec.setAugSelTrueParsePattern(null); 
		   covAugustParse.setFont(Font.font("Veranda", 12.0)); covAugustParse.setText("+ Pattern"); } 
		if(covDec.isSelected() == false){ fSpec.setDecSelCol(false); fSpec.setDecSelColIndex(0); fSpec.setDecSelTrueParsePattern(null); 
		   covDecParse.setFont(Font.font("Veranda", 12.0)); covDecParse.setText("+ Pattern"); } 
		if(depId.isSelected() == false){ fSpec.setDepEtcRefCol(false); fSpec.setDepEtcRefColIndex(0); } 
		if(depFrstNm.isSelected() == false){ fSpec.setDepFNameCol(false); fSpec.setDepFNameColIndex(0); fSpec.setDepFNameParsePattern(null); 
		   depFrstNmParse.setFont(Font.font("Veranda", 12.0)); depFrstNmParse.setText("+ Pattern"); } 
		if(depAdLn1.isSelected() == false){ fSpec.setDepStreetCol(false); fSpec.setDepStreetColIndex(0); fSpec.setDepStreetParsePattern(null); 
		   depAdLn1Parse.setFont(Font.font("Veranda", 12.0)); depAdLn1Parse.setText("+ Pattern"); } 
		if(depZip.isSelected() == false){ fSpec.setDepZipCol(false); fSpec.setDepZipColIndex(0); fSpec.setDepZipParsePattern(null); 
		   depZipParse.setFont(Font.font("Veranda", 12.0)); depZipParse.setText("+ Pattern"); } 
		if(depCF4.isSelected() == false){ fSpec.setDepCfld4Col(false); fSpec.setDepCfld4ColIndex(0); secCF4.setText(""); } 
		if(depSSN.isSelected() == false){ fSpec.setDepSSNCol(false); fSpec.setDepSSNColIndex(0); fSpec.setDepSSNParsePattern(null); 
		   depSSNParse.setFont(Font.font("Veranda", 12.0)); depSSNParse.setText("+ Pattern"); } 
		if(depMdNm.isSelected() == false){ fSpec.setDepMNameCol(false); fSpec.setDepMNameColIndex(0); fSpec.setDepMNameParsePattern(null); 
		   depMdNmParse.setFont(Font.font("Veranda", 12.0)); depMdNmParse.setText("+ Pattern"); } 
		if(depAdLn2.isSelected() == false){ fSpec.setDepLin2Col(false); fSpec.setDepLin2ColIndex(0); } 
		if(depCF1.isSelected() == false){ fSpec.setDepCfld1Col(false); fSpec.setDepCfld1ColIndex(0); fSpec.setDepCfld1ParsePattern(null);
		   depCF1Parse.setFont(Font.font("Veranda", 12.0)); depCF1Parse.setText("+ Pattern"); secCF1.setText(""); } 
		if(depCF5.isSelected() == false){ fSpec.setDepCfld5Col(false); fSpec.setDepCfld5ColIndex(0); secCF5.setText(""); } 
		if(depDOB.isSelected() == false){ fSpec.setDepDOBCol(false); fSpec.setDepDOBColIndex(0); fSpec.setDepDOBFormat(null); 
		   depDOBFormat.setFont(Font.font("Veranda", 12.0)); depDOBFormat.setText("+ Format"); } 
		if(depLstNm.isSelected() == false){ fSpec.setDepLNameCol(false); fSpec.setDepLNameColIndex(0); fSpec.setDepLNameParsePattern(null); 
		   depLstNmParse.setFont(Font.font("Veranda", 12.0)); depLstNmParse.setText("+ Pattern"); } 
		if(depCity.isSelected() == false){ fSpec.setDepCityCol(false); fSpec.setDepCityColIndex(0); fSpec.setDepCityParsePattern(null); 
		   depCityParse.setFont(Font.font("Veranda", 12.0)); depCityParse.setText("+ Pattern"); } 
		if(depCF2.isSelected() == false){ fSpec.setDepCfld2Col(false); fSpec.setDepCfld2ColIndex(0); fSpec.setDepCfld2ParsePattern(null); 
		   depCF2Parse.setFont(Font.font("Veranda", 12.0)); depCF2Parse.setText("+ Pattern"); secCF2.setText(""); } 
		if(depEmprId.isSelected() == false){ fSpec.setDepErRefCol(false); fSpec.setDepErRefColIndex(0); fSpec.setDepErRefParsePattern(null); 
		   depEmprIdParse.setFont(Font.font("Veranda", 12.0)); depEmprIdParse.setText("+ Pattern"); } 
		if(depStrtNm.isSelected() == false){ fSpec.setDepStreetNumCol(false); fSpec.setDepStreetNumColIndex(0); fSpec.setDepStreetParsePattern(null); 
		   depStrtNmParse.setFont(Font.font("Veranda", 12.0)); depStrtNmParse.setText("+ Pattern"); } 
		if(depState.isSelected() == false){ fSpec.setDepStateCol(false); fSpec.setDepStateColIndex(0); fSpec.setDepStateParsePattern(null); 
		   depStateParse.setFont(Font.font("Veranda", 12.0)); depStateParse.setText("+ Pattern"); } 
		if(depCF3.isSelected() == false){ fSpec.setDepCfld3Col(false); fSpec.setDepCfld3ColIndex(0); secCF3.setText(""); } 
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
		DynamicCoverageFileSpecification fSpec = DataManager.i().mDynamicCoverageFileSpecification; 
 
		if(eeCF1 != null){ fSpec.setCfld1Name(eeCF1.getText().toString()); } 
		if(eeCF2 != null){ fSpec.setCfld2Name(eeCF2.getText().toString()); } 
		if(eeCF3 != null){ fSpec.setCfld3Name(eeCF3.getText().toString()); } 
		if(eeCF4 != null){ fSpec.setCfld4Name(eeCF4.getText().toString()); } 
		if(eeCF5 != null){ fSpec.setCfld5Name(eeCF5.getText().toString()); } 
		if(secCF1 != null){ fSpec.setDepCfld1Name(secCF1.getText().toString()); } 
		if(secCF2 != null){ fSpec.setDepCfld2Name(secCF2.getText().toString()); } 
		if(secCF3 != null){ fSpec.setDepCfld3Name(secCF3.getText().toString()); } 
		if(secCF4 != null){ fSpec.setDepCfld4Name(secCF4.getText().toString()); } 
		if(secCF5 != null){ fSpec.setDepCfld5Name(secCF5.getText().toString()); } 
		if(cvgCF1 != null){ fSpec.setCovCfld1Name(cvgCF1.getText().toString()); } 
		if(cvgCF2 != null){ fSpec.setCovCfld2Name(cvgCF2.getText().toString()); } 
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
		dfcovCreateSecondaryCheck.setDisable(readOnly); 
		dfcovMapEEtoERCheck.setDisable(readOnly); 
		dfcovCreatePlanCheck.setDisable(readOnly); 
		dfcovClearFormButton.setDisable(readOnly); 
		dfcovSaveChangesButton.setDisable(readOnly); 
	} 

	private int getCoverageFileGenderTypeRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicCoverageFileGenderReferenceRequest request = new DynamicCoverageFileGenderReferenceRequest(DataManager.i().mDynamicCoverageFileGenderReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicCoverageFileSpecification.setGenderReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		
		for(DynamicCoverageFileGenderReference gendRef: DataManager.i().mDynamicCoverageFileSpecification.getGenderReferences()) 
		{ 
			if(gendRef.isActive() == true && gendRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getCoverageFileEmployerRefCount() 
	{ 
		int locRefSize = 0; 

		try {
			DynamicCoverageFileEmployerReferenceRequest request = new DynamicCoverageFileEmployerReferenceRequest(DataManager.i().mDynamicCoverageFileEmployerReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicCoverageFileSpecification.setErReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		
		for(DynamicCoverageFileEmployerReference erRef : DataManager.i().mDynamicCoverageFileSpecification.getErReferences()) 
		{ 
			if(erRef.isActive() == true && erRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 

	private int getCoverageFileCvgClassRefCount() 
	{
		int locRefSize = 0; 

		try {
			DynamicCoverageFileCoverageGroupReferenceRequest request = new DynamicCoverageFileCoverageGroupReferenceRequest(DataManager.i().mDynamicCoverageFileCoverageGroupReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicCoverageFileSpecification.setCvgGroupReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { DataManager.i().logGenericException(e); }
		
		for(DynamicCoverageFileCoverageGroupReference covRef : DataManager.i().mDynamicCoverageFileSpecification.getCvgGroupReferences()) 
		{ 
			if(covRef.isActive() == true && covRef.isDeleted() == false) 
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
		updateCoverageFile(); 
		EtcAdmin.i().setProgress(0);
	} 

	private void updateCoverageFile() 
	{ 
		if(DataManager.i().mDynamicCoverageFileSpecification != null) 
		{ 
			DynamicCoverageFileSpecification fSpec = DataManager.i().mDynamicCoverageFileSpecification; 
			DynamicCoverageFileSpecificationRequest request = new DynamicCoverageFileSpecificationRequest(); 

			//GATHER THE FIELDS 
			fSpec.setName(dfcovNameLabel.getText()); 
			fSpec.setTabIndex(Integer.valueOf(dfcovTabIndexLabel.getText())); 
			fSpec.setHeaderRowIndex(Integer.valueOf(dfcovHeaderRowIndexLabel.getText())); 
			fSpec.setMapEEtoER(dfcovMapEEtoERCheck.isSelected()); 
			fSpec.setCreateEmployee(dfcovCreateEmployeeCheck.isSelected()); 
			fSpec.setSkipLastRow(dfcovSkipLastRowCheck.isSelected()); 
			fSpec.setCreateDependent(dfcovCreateSecondaryCheck.isSelected()); 
			fSpec.setCreateBenefit(dfcovCreatePlanCheck.isSelected()); 

			// EMPLOYEE 
	     	// EMPLOYER IDENTIFIER 
			fSpec.setErCol(Boolean.valueOf(emprId.isSelected()));
			fSpec.setErColIndex(Integer.valueOf(emprIdIdx.getText() != null && !emprIdIdx.getText().isEmpty() ? emprIdIdx.getText() : "0")); 

			// COVERAGE CLASS 
			fSpec.setCvgGroupCol(Boolean.valueOf(empCovCls.isSelected())); 
			fSpec.setCvgGroupColIndex(Integer.valueOf(empCovClsIdx.getText() != null && !empCovClsIdx.getText().isEmpty() ? empCovClsIdx.getText() : "0")); 

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

			// JOB TITLE 
			fSpec.setJobTtlCol(Boolean.valueOf(emplJob.isSelected())); 
			fSpec.setJobTtlColIndex(Integer.valueOf(emplJobIdx.getText() != null && !emplJobIdx.getText().isEmpty() ? emplJobIdx.getText() : "0")); 

			// STREET NUMBER 
			fSpec.setStreetNumCol(Boolean.valueOf(emplStNm.isSelected())); 
			fSpec.setStreetNumColIndex(Integer.valueOf(emplStNmIdx.getText() != null && !emplStNmIdx.getText().isEmpty() ? emplStNmIdx.getText() : "0")); 
			if(emplStNmParse.getId() != null && Utils.isInt(emplStNmParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplStNmParse.getId())); 
				request.setStreetParsePatternRequest(req); 
			} else { request.setClearStreetParsePatternId(true); } 

			// CITY 
			fSpec.setCityCol(Boolean.valueOf(emplCity.isSelected())); 
			fSpec.setCityColIndex(Integer.valueOf(emplCityIdx.getText() != null && !emplCityIdx.getText().isEmpty() ? emplCityIdx.getText() : "0")); 
			if(emplCityParse.getId() != null && Utils.isInt(emplCityParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplCityParse.getId())); 
				request.setCityParsePatternRequest(req); 
			} else request.setClearCityParsePatternId(true); 

	     	// ETC LITE ID 
			fSpec.setEtcRefCol(Boolean.valueOf(emplId.isSelected())); 
			fSpec.setEtcRefColIndex(Integer.valueOf(emplIdIdx.getText() != null && !emplIdIdx.getText().isEmpty() ? emplIdIdx.getText() : "0")); 

	     	// EMPLOYEE FLAG 
			fSpec.setEeFlagCol(Boolean.valueOf(emplF.isSelected())); 
			fSpec.setEeFlagColIndex(Integer.valueOf(emplFIdx.getText() != null && !emplFIdx.getText().isEmpty() ? emplFIdx.getText() : "0")); 
			if(emplFParse.getId() != null && Utils.isInt(emplFParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplFParse.getId())); 
				request.setEeFlagTrueParsePatternRequest(req); 
			} else request.setClearEeFlagTrueParsePatternId(true); 

			// MIDDLE NAME 
			fSpec.setmNameCol(Boolean.valueOf(emplMNm.isSelected())); 
			fSpec.setmNameColIndex(Integer.valueOf(emplMNmIdx.getText() != null && !emplMNmIdx.getText().isEmpty() ? emplMNmIdx.getText() : "0")); 
			if(emplMNmParse.getId() != null && Utils.isInt(emplMNmParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplMNmParse.getId())); 
				request.setmNameParsePatternRequest(req); 
			} else request.setClearMNameParsePatternId(true); 

	     	// HIRE DATE 
			fSpec.setHireDtCol(Boolean.valueOf(emplHrDt.isSelected())); 
			fSpec.setHireDtColIndex(Integer.valueOf(emplHrDtIdx.getText() != null && !emplHrDtIdx.getText().isEmpty() ? emplHrDtIdx.getText() : "0")); 
			if(emplHrDtFormat.getId() != null && Utils.isInt(emplHrDtFormat.getId()))  
			{ 
				PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
				req.setId(Long.valueOf(emplHrDtFormat.getId())); 
				request.setHireDtFormatRequest(req); 
			} else request.setClearHireDtFormatId(true); 

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

			// EMPLOYEE SSN 
			fSpec.setSsnCol(Boolean.valueOf(emplSsn.isSelected())); 
			fSpec.setSsnColIndex(Integer.valueOf(emplSsnIdx.getText() != null && !emplSsnIdx.getText().isEmpty() ? emplSsnIdx.getText() : "0")); 
			if(emplSsnParse.getId() != null && Utils.isInt(emplSsnParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(emplSsnParse.getId())); 
				request.setSsnParsePatternRequest(req); 
			} else request.setClearSsnParsePatternId(true); 

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
				PipelineParseDateFormatRequest tdReq = new PipelineParseDateFormatRequest(); 
				tdReq.setId(Long.valueOf(emplTrmDtFormat.getId())); 
				request.setTermDtFormatRequest(tdReq); 
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

			// DEPENDENT 
			// ETC DEP ID 
			fSpec.setDepEtcRefCol(Boolean.valueOf(depId.isSelected())); 
			fSpec.setDepEtcRefColIndex(Integer.valueOf(depIdIdx.getText() != null && !depIdIdx.getText().isEmpty() ? depIdIdx.getText() : "0")); 

			// DEP FIRST NAME 
			fSpec.setDepFNameCol(Boolean.valueOf(depFrstNm.isSelected())); 
			fSpec.setDepFNameColIndex(Integer.valueOf(depFrstNmIdx.getText() != null && !depFrstNmIdx.getText().isEmpty() ? depFrstNmIdx.getText() : "0")); 
			if(depFrstNmParse.getId() != null && Utils.isInt(depFrstNmParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(depFrstNmParse.getId())); 
				request.setDepFNameParsePatternRequest(req); 
			} else request.setClearDepFNameParsePatternId(true); 

			// DEP EMPR ID 
			fSpec.setDepErRefCol(Boolean.valueOf(depEmprId.isSelected())); 
			fSpec.setDepErRefColIndex(Integer.valueOf(depEmprIdIdx.getText() != null && !depEmprIdIdx.getText().isEmpty() ? depEmprIdIdx.getText() : "0")); 
			if(depEmprIdParse.getId() != null && Utils.isInt(depEmprIdParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(depEmprIdParse.getId())); 
				request.setDepErRefParsePatternRequest(req); 
			} else request.setClearDepErRefParsePatternId(true); 

			// DEP ADDRESS LINE 2 
			fSpec.setDepLin2Col(Boolean.valueOf(depAdLn2.isSelected())); 
			fSpec.setDepLin2ColIndex(Integer.valueOf(depAdLn2Idx.getText() != null && !depAdLn2Idx.getText().isEmpty() ? depAdLn2Idx.getText() : "0")); 

			// DEP ZIP 
			fSpec.setDepZipCol(Boolean.valueOf(depZip.isSelected())); 
			fSpec.setDepZipColIndex(Integer.valueOf(depZipIdx.getText() != null && !depZipIdx.getText().isEmpty() ? depZipIdx.getText() : "0")); 
			if(depZipParse.getId() != null && Utils.isInt(depZipParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(depZipParse.getId())); 
				request.setDepZipParsePatternRequest(req); 
			} else request.setClearDepZipParsePatternId(true); 

			// DEP SSN 
			fSpec.setDepSSNCol(Boolean.valueOf(depSSN.isSelected())); 
			fSpec.setDepSSNColIndex(Integer.valueOf(depSSNIdx.getText() != null && !depSSNIdx.getText().isEmpty() ? depSSNIdx.getText() : "0")); 
			if(depSSNParse.getId() != null && Utils.isInt(depSSNParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(depSSNParse.getId())); 
				request.setDepSSNParsePatternRequest(req); 
			} else request.setClearDepSSNParsePatternId(true); 

	     	// DEP MIDDLE NAME 
			fSpec.setDepMNameCol(Boolean.valueOf(depMdNm.isSelected())); 
			fSpec.setDepMNameColIndex(Integer.valueOf(depMdNmIdx.getText() != null && !depMdNmIdx.getText().isEmpty() ? depMdNmIdx.getText() : "0")); 
			if(depMdNmParse.getId() != null && Utils.isInt(depMdNmParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(depMdNmParse.getId())); 
				request.setDepMNameParsePatternRequest(req); 
			} else request.setClearDepMNameParsePatternId(true); 

			// DEP STREET NUMBER 
			fSpec.setDepStreetNumCol(Boolean.valueOf(depStrtNm.isSelected())); 
			fSpec.setDepStreetNumColIndex(Integer.valueOf(depStrtNmIdx.getText() != null && !depStrtNmIdx.getText().isEmpty() ? depStrtNmIdx.getText() : "0")); 
			if(depStrtNmParse.getId() != null && Utils.isInt(depStrtNmParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(depStrtNmParse.getId())); 
				request.setDepStreetParsePatternRequest(req); 
			} else request.setClearDepStreetParsePatternId(true); 

			// DEP CITY 
			fSpec.setDepCityCol(Boolean.valueOf(depCity.isSelected())); 
			fSpec.setDepCityColIndex(Integer.valueOf(depCityIdx.getText() != null && !depCityIdx.getText().isEmpty() ? depCityIdx.getText() : "0")); 
			if(depCityParse.getId() != null && Utils.isInt(depCityParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(depCityParse.getId())); 
				request.setDepCityParsePatternRequest(req); 
			} else request.setClearDepCityParsePatternId(true); 

	     	// DEP DATE OF BIRTH 
			fSpec.setDepDOBCol(Boolean.valueOf(depDOB.isSelected())); 
			fSpec.setDepDOBColIndex(Integer.valueOf(depDOBIdx.getText() != null && !depDOBIdx.getText().isEmpty() ? depDOBIdx.getText() : "0")); 
			if(depDOBFormat.getId() != null && Utils.isInt(depDOBFormat.getId()))  
			{ 
				PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
				req.setId(Long.valueOf(depDOBFormat.getId())); 
				request.setDepDOBFormatRequest(req); 
			} else request.setClearDepDOBFormatId(true); 

			// DEP LAST NAME 
			fSpec.setDepLNameCol(Boolean.valueOf(depLstNm.isSelected())); 
			fSpec.setDepLNameColIndex(Integer.valueOf(depLstNmIdx.getText() != null && !depLstNmIdx.getText().isEmpty() ? depLstNmIdx.getText() : "0")); 
			if(depLstNmParse.getId() != null && Utils.isInt(depLstNmParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(depLstNmParse.getId())); 
				request.setDepLNameParsePatternRequest(req); 
			} else request.setClearDepLNameParsePatternId(true); 

			// DEP ADDRESS LINE 1 
			fSpec.setDepStreetCol(Boolean.valueOf(depAdLn1.isSelected())); 
			fSpec.setDepStreetColIndex(Integer.valueOf(depAdLn1Idx.getText() != null && !depAdLn1Idx.getText().isEmpty() ? depAdLn1Idx.getText() : "0")); 
			if(depAdLn1Parse.getId() != null && Utils.isInt(depAdLn1Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(depAdLn1Parse.getId())); 
				request.setDepStreetParsePatternRequest(req); 
			} else request.setClearDepStreetParsePatternId(true); 

			// DEP STATE 
			fSpec.setDepStateCol(Boolean.valueOf(depState.isSelected())); 
			fSpec.setDepStateColIndex(Integer.valueOf(depStateIdx.getText() != null && !depStateIdx.getText().isEmpty() ? depStateIdx.getText() : "0")); 
			if(depStateParse.getId() != null && Utils.isInt(depStateParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(depStateParse.getId())); 
				request.setDepStateParsePatternRequest(req); 
			} else request.setClearDepStateParsePatternId(true); 

	     	// DEP CUSTOM FIELD 1 
			fSpec.setDepCfld1Name(secCF1.getText() != null && !secCF1.getText().isEmpty() ? secCF1.getText() : ""); 
			fSpec.setDepCfld1Col(Boolean.valueOf(depCF1.isSelected())); 
			fSpec.setDepCfld1ColIndex(Integer.valueOf(depCF1Idx.getText() != null && !depCF1Idx.getText().isEmpty() ? depCF1Idx.getText() : "0")); 
			if(depCF1Parse.getId() != null && Utils.isInt(depCF1Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(depCF1Parse.getId())); 
				request.setDepCfld1ParsePatternRequest(req); 
			} else request.setClearDepCfld1ParsePatternId(true); 

	     	// DEP CUSTOM FIELD 2 
			fSpec.setDepCfld2Name(secCF2.getText() != null && !secCF2.getText().isEmpty() ? secCF2.getText() : ""); 
			fSpec.setDepCfld2Col(Boolean.valueOf(depCF2.isSelected())); 
			fSpec.setDepCfld2ColIndex(Integer.valueOf(depCF2Idx.getText() != null && !depCF2Idx.getText().isEmpty() ? depCF2Idx.getText() : "0")); 
			if(depCF2Parse.getId() != null && Utils.isInt(depCF2Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(depCF2Parse.getId())); 
				request.setDepCfld2ParsePatternRequest(req); 
			} else request.setClearDepCfld2ParsePatternId(true); 

	     	// DEP CUSTOM FIELD 3 
			fSpec.setDepCfld3Name(secCF3.getText() != null && !secCF3.getText().isEmpty() ? secCF3.getText() : ""); 
			fSpec.setDepCfld3Col(Boolean.valueOf(depCF3.isSelected())); 
			fSpec.setDepCfld3ColIndex(Integer.valueOf(depCF3Idx.getText() != null && !depCF3Idx.getText().isEmpty() ? depCF3Idx.getText() : "0")); 

	     	// DEP CUSTOM FIELD 4 
			fSpec.setDepCfld4Name(secCF4.getText() != null && !secCF4.getText().isEmpty() ? secCF4.getText() : ""); 
			fSpec.setDepCfld4Col(Boolean.valueOf(depCF4.isSelected())); 
			fSpec.setDepCfld4ColIndex(Integer.valueOf(depCF4Idx.getText() != null && !depCF4Idx.getText().isEmpty() ? depCF4Idx.getText() : "0")); 

	     	// DEP CUSTOM FIELD 5 
			fSpec.setDepCfld5Name(secCF5.getText() != null && !secCF5.getText().isEmpty() ? secCF5.getText() : ""); 
			fSpec.setDepCfld5Col(Boolean.valueOf(depCF5.isSelected())); 
			fSpec.setDepCfld5ColIndex(Integer.valueOf(depCF5Idx.getText() != null && !depCF5Idx.getText().isEmpty() ? depCF5Idx.getText() : "0")); 

			// MEMBER NUMBER 
			fSpec.setMbrCol(Boolean.valueOf(covMbrNm.isSelected())); 
			fSpec.setMbrColIndex(Integer.valueOf(covMbrNmIdx.getText() != null && !covMbrNmIdx.getText().isEmpty() ? covMbrNmIdx.getText() : "0")); 

			// COVERAGE WAIVED 
			fSpec.setWavdCol(Boolean.valueOf(covWaiv.isSelected())); 
			fSpec.setWavdColIndex(Integer.valueOf(covWaivIdx.getText() != null && !covWaivIdx.getText().isEmpty() ? covWaivIdx.getText() : "0")); 
			if(covWaivParse.getId() != null && Utils.isInt(covWaivParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covWaivParse.getId())); 
				request.setWavdTrueParsePatternRequest(req); 
			} else request.setClearWavdTrueParsePatternId(true); 

	     	// COVERAGE START DATE 
			fSpec.setCovStartDtCol(Boolean.valueOf(covStrtDt.isSelected())); 
			fSpec.setCovStartDtColIndex(Integer.valueOf(covStrtDtIdx.getText() != null && !covStrtDtIdx.getText().isEmpty() ? covStrtDtIdx.getText() : "0")); 
			if(covStrtDtFormat.getId() != null && Utils.isInt(covStrtDtFormat.getId()))  
			{ 
				PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
				req.setId(Long.valueOf(covStrtDtFormat.getId())); 
				request.setCovStartDtFormatRequest(req); 
			} else request.setClearCovStartDtFormatId(true); 

			// COVERAGE TAX YEAR SELECTED AKA ANNUAL COVERAGE 
			fSpec.setTySelCol(Boolean.valueOf(covAnnCov.isSelected())); 
			fSpec.setTySelColIndex(Integer.valueOf(covAnnCovIdx.getText() != null && !covAnnCovIdx.getText().isEmpty() ? covAnnCovIdx.getText() : "0")); 
			if(covAnnCovParse.getId() != null && Utils.isInt(covAnnCovParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covAnnCovParse.getId())); 
				request.setTySelTrueParsePatternRequest(req); 
			} else request.setClearTySelTrueParsePatternId(true); 

			// COVERAGE JANUARY SELECTED 
			fSpec.setJanSelCol(Boolean.valueOf(covJan.isSelected())); 
			fSpec.setJanSelColIndex(Integer.valueOf(covJanIdx.getText() != null && !covJanIdx.getText().isEmpty() ? covJanIdx.getText() : "0")); 
			if(covJanParse.getId() != null && Utils.isInt(covJanParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covJanParse.getId())); 
				request.setJanSelTrueParsePatternRequest(req); 
			} else request.setClearJanSelTrueParsePatternId(true); 

			// COVERAGE MAY SELECTED 
			fSpec.setMaySelCol(Boolean.valueOf(covMay.isSelected())); 
			fSpec.setMaySelColIndex(Integer.valueOf(covMayIdx.getText() != null && !covMayIdx.getText().isEmpty() ? covMayIdx.getText() : "0")); 
			if(covMayParse.getId() != null && Utils.isInt(covMayParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covMayParse.getId())); 
				request.setMaySelTrueParsePatternRequest(req); 
			} else request.setClearMaySelTrueParsePatternId(true); 

			// COVERAGE SEPTEMBER SELECTED 
			fSpec.setSepSelCol(Boolean.valueOf(covSept.isSelected())); 
			fSpec.setSepSelColIndex(Integer.valueOf(covSeptIdx.getText() != null && !covSeptIdx.getText().isEmpty() ? covSeptIdx.getText() : "0")); 
			if(covSeptParse.getId() != null && Utils.isInt(covSeptParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covSeptParse.getId())); 
				request.setSepSelTrueParsePatternRequest(req); 
			} else request.setClearSepSelTrueParsePatternId(true); 

			// DECISION DATE 
			fSpec.setDecisionDtCol(Boolean.valueOf(covDecDt.isSelected())); 
			fSpec.setDecisionDtColIndex(Integer.valueOf(covDecDtIdx.getText() != null && !covDecDtIdx.getText().isEmpty() ? covDecDtIdx.getText() : "0")); 
			if(covDecDtFormat.getId() != null && Utils.isInt(covDecDtFormat.getId()))  
			{ 
				PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
				req.setId(Long.valueOf(covDecDtFormat.getId())); 
				request.setDecisionDtFormatRequest(req); 
			} else request.setClearDecisionDtFormatId(true); 

			// COVERAGE INELIGIBLE 
			fSpec.setInelCol(Boolean.valueOf(covInelig.isSelected())); 
			fSpec.setInelColIndex(Integer.valueOf(covIneligIdx.getText() != null && !covIneligIdx.getText().isEmpty() ? covIneligIdx.getText() : "0")); 
			if(covIneligParse.getId() != null && Utils.isInt(covIneligParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covIneligParse.getId())); 
				request.setInelTrueParsePatternRequest(req); 
			} else request.setClearInelTrueParsePatternId(true); 

	     	// COVERAGE END DATE 
			fSpec.setCovEndDtCol(Boolean.valueOf(covEndDt.isSelected())); 
			fSpec.setCovEndDtColIndex(Integer.valueOf(covEndDtIdx.getText() != null && !covEndDtIdx.getText().isEmpty() ? covEndDtIdx.getText() : "0")); 
			if(covEndDtFormat.getId() != null && Utils.isInt(covEndDtFormat.getId()))  
			{ 
				PipelineParseDateFormatRequest req = new PipelineParseDateFormatRequest(); 
				req.setId(Long.valueOf(covEndDtFormat.getId())); 
				request.setCovEndDtFormatRequest(req); 
			} else request.setClearCovEndDtFormatId(true); 

			// COVERAGE TAX YEAR 
			fSpec.setTyCol(Boolean.valueOf(covTxYr.isSelected())); 
			fSpec.setTyColIndex(Integer.valueOf(covTxYrIdx.getText() != null && !covTxYrIdx.getText().isEmpty() ? covTxYrIdx.getText() : "0")); 
			if(covTxYrParse.getId() != null && Utils.isInt(covTxYrParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covTxYrParse.getId())); 
				request.setTyParsePatternRequest(req); 
			} else request.setClearTyParsePatternId(true); 

			// COVERAGE FEBRUARY SELECTED 
			fSpec.setFebSelCol(Boolean.valueOf(covFeb.isSelected())); 
			fSpec.setFebSelColIndex(Integer.valueOf(covFebIdx.getText() != null && !covFebIdx.getText().isEmpty() ? covFebIdx.getText() : "0")); 
			if(covFebParse.getId() != null && Utils.isInt(covFebParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covFebParse.getId())); 
				request.setFebSelTrueParsePatternRequest(req); 
			} else request.setClearFebSelTrueParsePatternId(true); 

			// COVERAGE JUNE SELECTED 
			fSpec.setJunSelCol(Boolean.valueOf(covJune.isSelected())); 
			fSpec.setJunSelColIndex(Integer.valueOf(covJuneIdx.getText() != null && !covJuneIdx.getText().isEmpty() ? covJuneIdx.getText() : "0")); 
			if(covJuneParse.getId() != null && Utils.isInt(covJuneParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covJuneParse.getId())); 
				request.setJunSelTrueParsePatternRequest(req); 
			} else request.setClearJunSelTrueParsePatternId(true); 

			// COVERAGE OCTOBER SELECTED 
			fSpec.setOctSelCol(Boolean.valueOf(covOct.isSelected())); 
			fSpec.setOctSelColIndex(Integer.valueOf(covOctIdx.getText() != null && !covOctIdx.getText().isEmpty() ? covOctIdx.getText() : "0")); 
			if(covOctParse.getId() != null && Utils.isInt(covOctParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covOctParse.getId())); 
				request.setOctSelTrueParsePatternRequest(req); 
			} else request.setClearOctSelTrueParsePatternId(true); 

			// PLAN IDENTIFIER 
			fSpec.setPlanRefCol(Boolean.valueOf(covPlan.isSelected())); 
			fSpec.setPlanRefColIndex(Integer.valueOf(covPlanIdx.getText() != null && !covPlanIdx.getText().isEmpty() ? covPlanIdx.getText() : "0")); 
			if(covPlanParse.getId() != null && Utils.isInt(covPlanParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covPlanParse.getId())); 
				request.setPlanRefParsePatternRequest(req); 
			} else request.setClearPlanRefParsePatternId(true); 

			// DEDUCTION 
			fSpec.setDedCol(Boolean.valueOf(covDeduc.isSelected())); 
			fSpec.setDedColIndex(Integer.valueOf(covDeducIdx.getText() != null && !covDeducIdx.getText().isEmpty() ? covDeducIdx.getText() : "0")); 

	     	// SUBSCRIBER NUMBER 
			fSpec.setSubcrbrCol(Boolean.valueOf(covSubNm.isSelected())); 
			fSpec.setSubcrbrColIndex(Integer.valueOf(covSubNmIdx.getText() != null && !covSubNmIdx.getText().isEmpty() ? covSubNmIdx.getText() : "0")); 

			// COVERAGE MARCH SELECTED 
			fSpec.setMarSelCol(Boolean.valueOf(covMarch.isSelected())); 
			fSpec.setMarSelColIndex(Integer.valueOf(covMarchIdx.getText() != null && !covMarchIdx.getText().isEmpty() ? covMarchIdx.getText() : "0")); 
			if(covMarchParse.getId() != null && Utils.isInt(covMarchParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covMarchParse.getId())); 
				request.setMarSelTrueParsePatternRequest(req); 
			} else request.setClearMarSelTrueParsePatternId(true); 

			// COVERAGE JULY SELECTED 
			fSpec.setJulSelCol(Boolean.valueOf(covJuly.isSelected())); 
			fSpec.setJulSelColIndex(Integer.valueOf(covJulyIdx.getText() != null && !covJulyIdx.getText().isEmpty() ? covJulyIdx.getText() : "0")); 
			if(covJulyParse.getId() != null && Utils.isInt(covJulyParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covJulyParse.getId())); 
				request.setJulSelTrueParsePatternRequest(req); 
			} else request.setClearJulSelTrueParsePatternId(true); 

			// COVERAGE NOVEMBER SELECTED 
			fSpec.setNovSelCol(Boolean.valueOf(covNov.isSelected())); 
			fSpec.setNovSelColIndex(Integer.valueOf(covNovIdx.getText() != null && !covNovIdx.getText().isEmpty() ? covNovIdx.getText() : "0")); 
			if(covNovParse.getId() != null && Utils.isInt(covNovParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covNovParse.getId())); 
				request.setNovSelTrueParsePatternRequest(req); 
			} else request.setClearNovSelTrueParsePatternId(true); 

			// MEMBER SHARE 
			fSpec.setMbrShareCol(Boolean.valueOf(covMbrShrAmt.isSelected())); 
			fSpec.setMbrShareColIndex(Integer.valueOf(covMbrShrAmtIdx.getText() != null && !covMbrShrAmtIdx.getText().isEmpty() ? covMbrShrAmtIdx.getText() : "0")); 

	     	// COVERAGE CUSTOM FIELD 1 
			fSpec.setCovCfld1Name(cvgCF1.getText() != null && !cvgCF1.getText().isEmpty() ? cvgCF1.getText() : ""); 
			fSpec.setCovCfld1Col(Boolean.valueOf(covCF1.isSelected())); 
			fSpec.setCovCfld1ColIndex(Integer.valueOf(covCF1Idx.getText() != null && !covCF1Idx.getText().isEmpty() ? covCF1Idx.getText() : "0")); 
			if(covCF1Parse.getId() != null && Utils.isInt(covCF1Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covCF1Parse.getId())); 
				request.setCovCfld1ParsePatternRequest(req); 
			} else request.setClearCovCfld1ParsePatternId(true); 

	     	// COVERAGE CUSTOM FIELD 2 
			fSpec.setCovCfld2Name(cvgCF2.getText() != null && !cvgCF2.getText().isEmpty() ? cvgCF2.getText() : ""); 
			fSpec.setCovCfld2Col(Boolean.valueOf(covCF2.isSelected())); 
			fSpec.setCovCfld2ColIndex(Integer.valueOf(covCF2Idx.getText() != null && !covCF2Idx.getText().isEmpty() ? covCF2Idx.getText() : "0")); 

			// COVERAGE APRIL SELECTED 
			fSpec.setAprSelCol(Boolean.valueOf(covApril.isSelected())); 
			fSpec.setAprSelColIndex(Integer.valueOf(covAprilIdx.getText() != null && !covAprilIdx.getText().isEmpty() ? covAprilIdx.getText() : "0")); 
			if(covAprilParse.getId() != null && Utils.isInt(covAprilParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covAprilParse.getId())); 
				request.setAprSelTrueParsePatternRequest(req); 
			} else request.setClearAprSelTrueParsePatternId(true); 

			// COVERAGE AUGUST SELECTED
			fSpec.setAugSelCol(Boolean.valueOf(covAugust.isSelected())); 
			fSpec.setAugSelColIndex(Integer.valueOf(covAugustIdx.getText() != null && !covAugustIdx.getText().isEmpty() ? covAugustIdx.getText() : "0")); 
			if(covAugustParse.getId() != null && Utils.isInt(covAugustParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covAugustParse.getId())); 
				request.setAugSelTrueParsePatternRequest(req); 
			} else request.setClearAugSelTrueParsePatternId(true); 

			// COVERAGE DECEMBER SELECTED 
			fSpec.setDecSelCol(Boolean.valueOf(covDec.isSelected())); 
			fSpec.setDecSelColIndex(Integer.valueOf(covDecIdx.getText() != null && !covDecIdx.getText().isEmpty() ? covDecIdx.getText() : "0")); 
			if(covDecParse.getId() != null && Utils.isInt(covDecParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covDecParse.getId())); 
				request.setDecSelTrueParsePatternRequest(req); 
			} else request.setClearDecSelTrueParsePatternId(true); 

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
			case COVERAGE: 
	        	DataManager.i().mDynamicCoverageFileIgnoreRowSpecification = null; 
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
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/mapper/ViewMapperCoverageRowIgnore.fxml")); 
			Parent ControllerNode = loader.load(); 
	        ViewMapperCoverageRowIgnoreController ignoreController = (ViewMapperCoverageRowIgnoreController) loader.getController(); 
 
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
	        stage.onCloseRequestProperty().setValue(e -> setEmpRefCount(getCoverageFileEmployerRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setEmpRefCount(getCoverageFileEmployerRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true) 
				setEmpRefCount(getCoverageFileEmployerRefCount()); 
		} catch(IOException e) { 
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
	        stage.onCloseRequestProperty().setValue(e -> setCvgRefCount(getCoverageFileCvgClassRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setCvgRefCount(getCoverageFileCvgClassRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
				setCvgRefCount(getCoverageFileCvgClassRefCount()); 
		} catch(IOException e) { 
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
	        stage.onCloseRequestProperty().setValue(e -> setGendRefCount(getCoverageFileGenderTypeRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setGendRefCount(getCoverageFileGenderTypeRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
				setGendRefCount(getCoverageFileGenderTypeRefCount()); 
		} catch(IOException e) { 
			 DataManager.i().log(Level.SEVERE, e); 
			Utils.alertUser("Reference Popup Load Error", e.getMessage());
		}        		 
	    catch (Exception e) { DataManager.i().logGenericException(e); }
	} 
	 
	public void loadPatternPopupAll(MapperField fld)  
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
        		DataManager.i().mDynamicCoverageFileSpecification.setJanSelTrueParsePatternId(DataManager.i().mPipelineParsePattern.getId());
        		DataManager.i().mDynamicCoverageFileSpecification.setFebSelTrueParsePatternId(DataManager.i().mPipelineParsePattern.getId());
        		DataManager.i().mDynamicCoverageFileSpecification.setMarSelTrueParsePatternId(DataManager.i().mPipelineParsePattern.getId());
        		DataManager.i().mDynamicCoverageFileSpecification.setAprSelTrueParsePatternId(DataManager.i().mPipelineParsePattern.getId());
        		DataManager.i().mDynamicCoverageFileSpecification.setMaySelTrueParsePatternId(DataManager.i().mPipelineParsePattern.getId());
        		DataManager.i().mDynamicCoverageFileSpecification.setJunSelTrueParsePatternId(DataManager.i().mPipelineParsePattern.getId());
        		DataManager.i().mDynamicCoverageFileSpecification.setJulSelTrueParsePatternId(DataManager.i().mPipelineParsePattern.getId());
        		DataManager.i().mDynamicCoverageFileSpecification.setAugSelTrueParsePatternId(DataManager.i().mPipelineParsePattern.getId());
        		DataManager.i().mDynamicCoverageFileSpecification.setSepSelTrueParsePatternId(DataManager.i().mPipelineParsePattern.getId());
        		DataManager.i().mDynamicCoverageFileSpecification.setOctSelTrueParsePatternId(DataManager.i().mPipelineParsePattern.getId());
        		DataManager.i().mDynamicCoverageFileSpecification.setNovSelTrueParsePatternId(DataManager.i().mPipelineParsePattern.getId());
        		DataManager.i().mDynamicCoverageFileSpecification.setDecSelTrueParsePatternId(DataManager.i().mPipelineParsePattern.getId());
  	        } else { fld.setParsePatternId(null); }

        } catch(IOException | CoreException e) { 
        	 DataManager.i().log(Level.SEVERE, e);
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

        } catch(IOException | CoreException e) { 
        	 DataManager.i().log(Level.SEVERE, e);
  		}        		 
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
 
  		} catch(IOException | CoreException e) { 
  			 DataManager.i().log(Level.SEVERE, e);
  		}        		 
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
	 
	/* 
	 * setParsePattern sets the Parse ID on the button, visual 
	 * to the user. 
	 */ 
	public void onAllMonthParse(ActionEvent event) 
	{ 
		Button btn1 = null; 

		if(event.getSource() instanceof Button) 
		{ 
			btn1 = Button.class.cast(event.getSource()); 
	
			for(MapperField f : mapperFields.values()) 
			{ 
				if(btn1.equals(f.getBtnReferences()))
				{
					if(f.getParsePatternId() != null) 
					{ 
						f.setTitle(f.getTitle().replace(" (P:" + f.getParsePatternId().toString() + ")", "")); 
					}
					loadPatternPopupAll(f); 
					if(f.getParsePatternId() != null) 
					{
						f.getBtnParsePtrn().setText("Pattern: " + f.getParsePatternId().toString());
						btn1.setFont(Font.font("Veranda", 12.0)); 
						btn1.setId(f.getParsePatternId().toString()); 
		
						covJanParse.setFont(Font.font("Veranda", 12.0)); 
						covJanParse.setId(f.getParsePatternId().toString()); 
						covJanParse.setText("Pattern: " + f.getParsePatternId().toString()); 
						covFebParse.setFont(Font.font("Veranda", 12.0)); 
						covFebParse.setId(f.getParsePatternId().toString()); 
						covFebParse.setText("Pattern: " + f.getParsePatternId().toString()); 
						covMarchParse.setFont(Font.font("Veranda", 12.0)); 
						covMarchParse.setId(f.getParsePatternId().toString()); 
						covMarchParse.setText("Pattern: " + f.getParsePatternId().toString()); 
						covAprilParse.setFont(Font.font("Veranda", 12.0)); 
						covAprilParse.setId(f.getParsePatternId().toString()); 
						covAprilParse.setText("Pattern: " + f.getParsePatternId().toString()); 
						covMayParse.setFont(Font.font("Veranda", 12.0)); 
						covMayParse.setId(f.getParsePatternId().toString()); 
						covMayParse.setText("Pattern: " + f.getParsePatternId().toString()); 
						covJuneParse.setFont(Font.font("Veranda", 12.0)); 
						covJuneParse.setId(f.getParsePatternId().toString()); 
						covJuneParse.setText("Pattern: " + f.getParsePatternId().toString()); 
						covJulyParse.setFont(Font.font("Veranda", 12.0)); 
						covJulyParse.setId(f.getParsePatternId().toString()); 
						covJulyParse.setText("Pattern: " + f.getParsePatternId().toString()); 
						covAugustParse.setFont(Font.font("Veranda", 12.0)); 
						covAugustParse.setId(f.getParsePatternId().toString()); 
						covAugustParse.setText("Pattern: " + f.getParsePatternId().toString()); 
						covSeptParse.setFont(Font.font("Veranda", 12.0)); 
						covSeptParse.setId(f.getParsePatternId().toString()); 
						covSeptParse.setText("Pattern: " + f.getParsePatternId().toString()); 
						covOctParse.setFont(Font.font("Veranda", 12.0)); 
						covOctParse.setId(f.getParsePatternId().toString()); 
						covOctParse.setText("Pattern: " + f.getParsePatternId().toString()); 
						covNovParse.setFont(Font.font("Veranda", 12.0)); 
						covNovParse.setId(f.getParsePatternId().toString()); 
						covNovParse.setText("Pattern: " + f.getParsePatternId().toString()); 
						covDecParse.setFont(Font.font("Veranda", 12.0)); 
						covDecParse.setId(f.getParsePatternId().toString()); 
						covDecParse.setText("Pattern: " + f.getParsePatternId().toString()); 
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
		if(emplFParse.getText().contains("+ Pattern")){ emplFParse.setId("emplFParse"); } 
		if(emplMNmParse.getText().contains("+ Pattern")){ emplMNmParse.setId("emplMNmParse"); } 
		if(emplHrDtFormat.getText().contains("+ Format")){ emplHrDtFormat.setId("emplHrDtFormat"); } 
		if(emplAdLn1Parse.getText().contains("+ Pattern")){ emplAdLn1Parse.setId("emplAdLn1Parse"); } 
		if(emplStateParse.getText().contains("+ Pattern")){ emplStateParse.setId("emplStateParse"); } 
		if(emplSsnParse.getText().contains("+ Pattern")){ emplSsnParse.setId("emplSsnParse"); } 
		if(emplLstNmParse.getText().contains("+ Pattern")){ emplLstNmParse.setId("emplLstNmParse"); } 
		if(emplTrmDtFormat.getText().contains("+ Format")){ emplTrmDtFormat.setId("emplTrmDtFormat"); } 
		if(emplZipParse.getText().contains("+ Pattern")){ emplZipParse.setId("emplZipParse"); } 
		if(emplrEmpIdParse.getText().contains("+ Pattern")){ emplrEmpIdParse.setId("emplrEmpIdParse"); } 
		if(emplCF1Parse.getText().contains("+ Pattern")){ emplCF1Parse.setId("emplCF1Parse"); } 
		if(emplCF2Parse.getText().contains("+ Pattern")){ emplCF2Parse.setId("emplCF2Parse"); } 
		if(depFrstNmParse.getText().contains("+ Pattern")){ depFrstNmParse.setId("depFrstNmParse"); } 
		if(depEmprIdParse.getText().contains("+ Pattern")){ depEmprIdParse.setId("depEmprIdParse"); } 
		if(depZipParse.getText().contains("+ Pattern")){ depZipParse.setId("depZipParse"); } 
		if(depSSNParse.getText().contains("+ Pattern")){ depSSNParse.setId("depSSNParse"); } 
		if(depMdNmParse.getText().contains("+ Pattern")){ depMdNmParse.setId("depMdNmParse"); } 
		if(depStrtNmParse.getText().contains("+ Pattern")){ depStrtNmParse.setId("depStrtNmParse"); } 
		if(depCityParse.getText().contains("+ Pattern")){ depCityParse.setId("depCityParse"); } 
		if(depDOBFormat.getText().contains("+ Format")){ depDOBFormat.setId("depDOBFormat"); } 
		if(depLstNmParse.getText().contains("+ Pattern")){ depLstNmParse.setId("depLstNmParse"); } 
		if(depAdLn1Parse.getText().contains("+ Pattern")){ depAdLn1Parse.setId("depAdLn1Parse"); } 
		if(depStateParse.getText().contains("+ Pattern")){ depStateParse.setId("depStateParse"); } 
		if(depCF1Parse.getText().contains("+ Pattern")){ depCF1Parse.setId("depCF1Parse"); } 
		if(depCF2Parse.getText().contains("+ Pattern")){ depCF2Parse.setId("depCF2Parse"); } 
		if(covWaivParse.getText().contains("+ Pattern")){ covWaivParse.setId("covWaivParse"); } 
		if(covStrtDtFormat.getText().contains("+ Format")){ covStrtDtFormat.setId("covStrtDtFormat"); } 
		if(covAnnCovParse.getText().contains("+ Pattern")){ covAnnCovParse.setId("covAnnCovParse"); } 
		if(covJanParse.getText().contains("+ Pattern")){ covJanParse.setId("covJanParse"); } 
		if(covMayParse.getText().contains("+ Pattern")){ covMayParse.setId("covMayParse"); } 
		if(covSeptParse.getText().contains("+ Pattern")){ covSeptParse.setId("covSeptParse"); } 
		if(covDecDtFormat.getText().contains("+ Format")){ covDecDtFormat.setId("covDecDtFormat"); } 
		if(covIneligParse.getText().contains("+ Pattern")){ covIneligParse.setId("covIneligParse"); } 
		if(covEndDtFormat.getText().contains("+ Format")){ covEndDtFormat.setId("covEndDtFormat"); } 
		if(covTxYrParse.getText().contains("+ Pattern")){ covTxYrParse.setId("covTxYrParse"); } 
		if(covFebParse.getText().contains("+ Pattern")){ covFebParse.setId("covFebParse"); } 
		if(covJuneParse.getText().contains("+ Pattern")){ covJuneParse.setId("covJuneParse"); } 
		if(covOctParse.getText().contains("+ Pattern")){ covOctParse.setId("covOctParse"); } 
		if(covPlanParse.getText().contains("+ Pattern")){ covPlanParse.setId("covPlanParse"); } 
		if(covMarchParse.getText().contains("+ Pattern")){ covMarchParse.setId("covMarchParse"); } 
		if(covJulyParse.getText().contains("+ Pattern")){ covJulyParse.setId("covJulyParse"); } 
		if(covNovParse.getText().contains("+ Pattern")){ covNovParse.setId("covNovParse"); } 
		if(covCF1Parse.getText().contains("+ Pattern")){ covCF1Parse.setId("covCF1Parse"); } 
		if(covAprilParse.getText().contains("+ Pattern")){ covAprilParse.setId("covAprilParse"); } 
		if(covAugustParse.getText().contains("+ Pattern")){ covAugustParse.setId("covAugustParse"); } 
		if(covDecParse.getText().contains("+ Pattern")){ covDecParse.setId("covDecParse"); } 
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
 
    public void setGendRefCount(int count) 
    { 
    	gendRefLabel.setFont(Font.font("Veranda", 12.0)); 
        gendRefLabel.setText("References: " + String.valueOf(count)); 
    } 
 
    public void setCvgRefCount(int count) 
    { 
    	covRefLabel.setFont(Font.font("Veranda", 12.0)); 
        covRefLabel.setText("References: " + String.valueOf(count)); 
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
				empFstNm.setSelected(false);  
				empDOB.setSelected(false); 
				emplJob.setSelected(false); 
				emplStNm.setSelected(false); 
				emplCity.setSelected(false); 
				emplId.setSelected(false); 
				emplMNm.setSelected(false); 
				emplF.setSelected(false); 
				emplHrDt.setSelected(false); 
				emplPhn.setSelected(false); 
				emplAdLn1.setSelected(false); 
				emplState.setSelected(false); 
				emplSsn.setSelected(false); 
				emplGend.setSelected(false); 
				emplLstNm.setSelected(false); 
				emplTrmDt.setSelected(false); 
				emplEml.setSelected(false); 
				emplAdLn2.setSelected(false); 
				emplZip.setSelected(false); 
				emplrEmpId.setSelected(false); 
				emplCF1.setSelected(false); 
				emplCF2.setSelected(false); 
				emplCF3.setSelected(false); 
				emplCF4.setSelected(false); 
				emplCF5.setSelected(false); 
				depId.setSelected(false); 
				depFrstNm.setSelected(false); 
				depAdLn1.setSelected(false); 
				depZip.setSelected(false); 
				depCF4.setSelected(false); 
				depSSN.setSelected(false); 
				depMdNm.setSelected(false); 
				depAdLn2.setSelected(false); 
				depCF1.setSelected(false); 
				depCF5.setSelected(false); 
				depDOB.setSelected(false); 
				depLstNm.setSelected(false); 
				depCity.setSelected(false); 
				depCF2.setSelected(false); 
				depEmprId.setSelected(false); 
				depStrtNm.setSelected(false); 
				depState.setSelected(false); 
				depCF3.setSelected(false); 
				covMbrNm.setSelected(false); 
				covWaiv.setSelected(false); 
				covStrtDt.setSelected(false); 
				covAnnCov.setSelected(false); 
				covJan.setSelected(false); 
				covMay.setSelected(false); 
				covSept.setSelected(false); 
				covDecDt.setSelected(false); 
				covInelig.setSelected(false); 
				covEndDt.setSelected(false); 
				covTxYr.setSelected(false); 
				covFeb.setSelected(false); 
				covJune.setSelected(false); 
				covOct.setSelected(false); 
				covPlan.setSelected(false); 
				covDeduc.setSelected(false); 
				covCF1.setSelected(false); 
				covMarch.setSelected(false); 
				covJuly.setSelected(false); 
				covNov.setSelected(false); 
				covSubNm.setSelected(false); 
				covMbrShrAmt.setSelected(false); 
				covCF2.setSelected(false); 
				covApril.setSelected(false); 
				covAugust.setSelected(false); 
				covDec.setSelected(false); 
				dfcovNameLabel.setText(""); 
				dfcovTabIndexLabel.setText("0"); 
				dfcovHeaderRowIndexLabel.setText("0"); 
				dfcovMapEEtoERCheck.setSelected(false); 
				dfcovCreateEmployeeCheck.setSelected(false); 
				dfcovSkipLastRowCheck.setSelected(false); 
				dfcovCreateSecondaryCheck.setSelected(false); 
				dfcovCreatePlanCheck.setSelected(false); 
 
				mf.getTxtFldIndex().setText(""); 
				eeCF1.setText(""); 
				eeCF2.setText(""); 
				eeCF3.setText(""); 
				eeCF4.setText(""); 
				eeCF5.setText(""); 
				secCF1.setText(""); 
				secCF2.setText(""); 
				secCF3.setText(""); 
				secCF4.setText(""); 
				secCF5.setText(""); 
				cvgCF1.setText(""); 
				cvgCF2.setText(""); 
				emprRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
				ccRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
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
		DynamicCoverageFileSpecification fSpec = DataManager.i().mDynamicCoverageFileSpecification; 
		 
		if(dfcovMapEEtoERCheck.isSelected() == true) { fSpec.setMapEEtoER(true); } 
		else { fSpec.setMapEEtoER(false); } 
 
		if(dfcovCreateEmployeeCheck.isSelected() == true) { fSpec.setCreateEmployee(true); } 
		else { fSpec.setCreateEmployee(false); } 
 
		if(dfcovSkipLastRowCheck.isSelected() == true) { fSpec.setSkipLastRow(true); } 
		else { fSpec.setSkipLastRow(false); } 
 
		if(dfcovCreateSecondaryCheck.isSelected() == true) { fSpec.setCreateDependent(true); } 
		else { fSpec.setCreateDependent(false); } 
 
		if(dfcovCreatePlanCheck.isSelected() == true) { fSpec.setCreateBenefit(true); } 
		else { fSpec.setCreateBenefit(false); } 
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
	private void onGenRefClick(ActionEvent event)  
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
	private void onEmpDOBFormat(ActionEvent event) 	 
	{  
		setFormatButtonValue(event); 
	} 
 
	@FXML 
	private void onEmplStNmParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event); 
	} 
 
	@FXML 
	private void onEmplCityParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event); 
	} 
 
	@FXML 
	private void onEmplFParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplMNmParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplHrDtFormat(ActionEvent event) 	 
	{  
		setFormatButtonValue(event);  
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
	private void onEmplSsnParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplLstNmParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplTrmDtFormat(ActionEvent event) 	 
	{  
		setFormatButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplZipParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onEmplrEmpIdParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
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
	private void onDepFrstNmParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onDepZipParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onDepSSNParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onDepMdNmParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onDepCF1Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onDepDOBFormat(ActionEvent event) 	 
	{  
		setFormatButtonValue(event);  
	} 
 
	@FXML 
	private void onDepLstNmParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onDepAdLn1Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onDepCityParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onDepCF2Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onDepEmprIdParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onDepStrtNmParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onDepStateParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovWaivParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovStrtDtFormat(ActionEvent event) 	 
	{  
		setFormatButtonValue(event);  
	} 
 
	@FXML 
	private void onCovAnnCovParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovJanParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovMayParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovSeptParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovDecDtFormat(ActionEvent event) 	 
	{  
		setFormatButtonValue(event);  
	} 
 
	@FXML 
	private void onCovIneligParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovEndDtFormat(ActionEvent event) 	 
	{  
		setFormatButtonValue(event);  
	} 
 
	@FXML 
	private void onCovTxYrParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovFebParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovJuneParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovOctParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovPlanParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovCF1Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovMarchParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovJulyParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovNovParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovAprilParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovAugustParse(ActionEvent event)
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onCovDecParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	public class HBoxIgnoreRowCell extends HBox  
	{ 
        Label lblCol1 = new Label(); 
        Label lblCol2 = new Label(); 
        DynamicCoverageFileIgnoreRowSpecification covSpec; 
 
        HBoxIgnoreRowCell(DynamicCoverageFileIgnoreRowSpecification covSpec) 
        { 
             super(); 
 
             if(covSpec != null)  
             { 
            	 lblCol1.setText(Utils.getDateString(covSpec.getLastUpdated())); 
            	 lblCol2.setText(covSpec.getName()); 
            	 this.covSpec = covSpec; 
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
 
        public DynamicCoverageFileIgnoreRowSpecification getCovSpec()  
        { 
        	return covSpec;  
        } 
    } 
}