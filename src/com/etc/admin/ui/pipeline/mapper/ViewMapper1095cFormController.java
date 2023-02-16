package com.etc.admin.ui.pipeline.mapper; 
 
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.etc.CoreException;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationGenderTypeRefController;
import com.etc.admin.ui.pipeline.filespecification.ViewDynamicFileSpecificationRefController;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.pipeline.entities.c95.DynamicIrs1095cFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.c95.DynamicIrs1095cFileGenderReference;
import com.etc.corvetto.ems.pipeline.entities.c95.DynamicIrs1095cFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.rqs.PipelineParseDateFormatRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineParsePatternRequest;
import com.etc.corvetto.ems.pipeline.rqs.c95.DynamicIrs1095cFileEmployerReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.c95.DynamicIrs1095cFileGenderReferenceRequest;
import com.etc.corvetto.ems.pipeline.rqs.c95.DynamicIrs1095cFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.utils.types.PipelineFileType;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
 
public class ViewMapper1095cFormController 
{ 
	@FXML 
	private ListView<HBoxIgnoreRowCell> dfcovIgnoreRowSpecsListView; 
 
	@FXML 
	private Label emprIdLbl; 
 
	@FXML 
	private StackPane emprIdSkPn; 
 
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
	private StackPane annOfferCodeSkPn; 
	 
	@FXML 
	private StackPane annMemberShareSkPn; 
	 
	@FXML 
	private StackPane annSafeHarborSkPn; 

	@FXML 
	private StackPane annSelfInsuredSkPn; 
	 
	@FXML 
	private StackPane annZip5SkPn; 

	@FXML 
	private StackPane janOfferCodeSkPn; 

	@FXML 
	private StackPane janMemberShareSkPn; 

	@FXML 
	private StackPane janSafeHarborSkPn; 

	@FXML 
	private StackPane janSelfInsSkPn; 

	@FXML 
	private StackPane janZip5SkPn; 

	@FXML 
	private StackPane febOfferCodeSkPn; 

	@FXML 
	private StackPane febMemberShareSkPn; 

	@FXML 
	private StackPane febSafeHarborSkPn; 

	@FXML 
	private StackPane febSelfInsSkPn; 

	@FXML 
	private StackPane febZip5SkPn; 

	@FXML 
	private StackPane marOfferCodeSkPn; 

	@FXML 
	private StackPane marMemberShareSkPn; 

	@FXML 
	private StackPane marSafeHarborSkPn; 

	@FXML 
	private StackPane marSelfInsSkPn; 

	@FXML 
	private StackPane marZip5SkPn; 

	@FXML 
	private StackPane aprOfferCodeSkPn; 

	@FXML 
	private StackPane aprMemberShareSkPn; 

	@FXML 
	private StackPane aprSafeHarborSkPn; 

	@FXML 
	private StackPane aprSelfInsSkPn; 

	@FXML 
	private StackPane aprZip5SkPn; 

	@FXML 
	private StackPane mayOfferCodeSkPn; 

	@FXML 
	private StackPane mayMemberShareSkPn; 

	@FXML 
	private StackPane maySafeHarborSkPn; 

	@FXML 
	private StackPane maySelfInsSkPn; 

	@FXML 
	private StackPane mayZip5SkPn; 

	@FXML 
	private StackPane junOfferCodeSkPn; 

	@FXML 
	private StackPane junMemberShareSkPn; 

	@FXML 
	private StackPane junSafeHarborSkPn; 

	@FXML 
	private StackPane junSelfInsSkPn; 

	@FXML 
	private StackPane junZip5SkPn; 

	@FXML 
	private StackPane julOfferCodeSkPn; 

	@FXML 
	private StackPane julMemberShareSkPn; 

	@FXML 
	private StackPane julSafeHarborSkPn; 

	@FXML 
	private StackPane julSelfInsSkPn; 

	@FXML 
	private StackPane julZip5SkPn; 

	@FXML 
	private StackPane augOfferCodeSkPn; 

	@FXML 
	private StackPane augMemberShareSkPn; 

	@FXML 
	private StackPane augSafeHarborSkPn; 

	@FXML 
	private StackPane augSelfInsSkPn; 

	@FXML 
	private StackPane augZip5SkPn; 

	@FXML 
	private StackPane sepOfferCodeSkPn; 

	@FXML 
	private StackPane sepMemberShareSkPn; 

	@FXML 
	private StackPane sepSafeHarborSkPn; 

	@FXML 
	private StackPane sepSelfInsSkPn; 

	@FXML 
	private StackPane sepZip5SkPn; 

	@FXML 
	private StackPane octOfferCodeSkPn; 

	@FXML 
	private StackPane octMemberShareSkPn; 

	@FXML 
	private StackPane octSafeHarborSkPn; 

	@FXML 
	private StackPane octSelfInsSkPn; 

	@FXML 
	private StackPane octZip5SkPn; 

	@FXML 
	private StackPane novOfferCodeSkPn; 

	@FXML 
	private StackPane novMemberShareSkPn; 

	@FXML 
	private StackPane novSafeHarborSkPn; 

	@FXML 
	private StackPane novSelfInsSkPn; 

	@FXML 
	private StackPane novZip5SkPn; 

	@FXML 
	private StackPane decOfferCodeSkPn; 

	@FXML 
	private StackPane decMemberShareSkPn; 

	@FXML 
	private StackPane decSafeHarborSkPn; 

	@FXML 
	private StackPane decSelfInsSkPn; 

	@FXML 
	private StackPane decZip5SkPn; 
	
	@FXML 
	private StackPane covTxYrSkPn; 
	 
	@FXML 
	private StackPane c95CF1SkPn; 
	 
	@FXML 
	private StackPane c95CF2SkPn; 
	 
	@FXML 
	private StackPane c95CF3SkPn; 
	 
	@FXML 
	private StackPane c95CF4SkPn; 
	 
	@FXML 
	private StackPane c95CF5SkPn; 
	 
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
	private CheckBox dfLockedCheck; 
	 
	@FXML 
	private CheckBox dfcovSkipLastRowCheck; 
	 
	@FXML 
	private CheckBox emprId; 
	 
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
	private CheckBox annOfferCode; 
	 
	@FXML 
	private CheckBox annMemberShare; 
	 
	@FXML 
	private CheckBox annSafeHarbor; 

	@FXML 
	private CheckBox annSelfInsured; 
	 
	@FXML 
	private CheckBox annZip5; 

	@FXML 
	private CheckBox janOfferCode; 

	@FXML 
	private CheckBox janMemberShare; 

	@FXML 
	private CheckBox janSafeHarbor; 

	@FXML 
	private CheckBox janSelfIns; 

	@FXML 
	private CheckBox janZip5; 

	@FXML 
	private CheckBox febOfferCode; 

	@FXML 
	private CheckBox febMemberShare; 

	@FXML 
	private CheckBox febSafeHarbor; 

	@FXML 
	private CheckBox febSelfIns; 

	@FXML 
	private CheckBox febZip5; 

	@FXML 
	private CheckBox marOfferCode; 

	@FXML 
	private CheckBox marMemberShare; 

	@FXML 
	private CheckBox marSafeHarbor; 

	@FXML 
	private CheckBox marSelfIns; 

	@FXML 
	private CheckBox marZip5; 

	@FXML 
	private CheckBox aprOfferCode; 

	@FXML 
	private CheckBox aprMemberShare; 

	@FXML 
	private CheckBox aprSafeHarbor; 

	@FXML 
	private CheckBox aprSelfIns; 

	@FXML 
	private CheckBox aprZip5; 

	@FXML 
	private CheckBox mayOfferCode; 

	@FXML 
	private CheckBox mayMemberShare; 

	@FXML 
	private CheckBox maySafeHarbor; 

	@FXML 
	private CheckBox maySelfIns; 

	@FXML 
	private CheckBox mayZip5; 

	@FXML 
	private CheckBox junOfferCode; 

	@FXML 
	private CheckBox junMemberShare; 

	@FXML 
	private CheckBox junSafeHarbor; 

	@FXML 
	private CheckBox junSelfIns; 

	@FXML 
	private CheckBox junZip5; 

	@FXML 
	private CheckBox julOfferCode; 

	@FXML 
	private CheckBox julMemberShare; 

	@FXML 
	private CheckBox julSafeHarbor; 

	@FXML 
	private CheckBox julSelfIns; 

	@FXML 
	private CheckBox julZip5; 

	@FXML 
	private CheckBox augOfferCode; 

	@FXML 
	private CheckBox augMemberShare; 

	@FXML 
	private CheckBox augSafeHarbor; 

	@FXML 
	private CheckBox augSelfIns; 

	@FXML 
	private CheckBox augZip5; 

	@FXML 
	private CheckBox sepOfferCode; 

	@FXML 
	private CheckBox sepMemberShare; 

	@FXML 
	private CheckBox sepSafeHarbor; 

	@FXML 
	private CheckBox sepSelfIns; 

	@FXML 
	private CheckBox sepZip5; 

	@FXML 
	private CheckBox octOfferCode; 

	@FXML 
	private CheckBox octMemberShare; 

	@FXML 
	private CheckBox octSafeHarbor; 

	@FXML 
	private CheckBox octSelfIns; 

	@FXML 
	private CheckBox octZip5; 

	@FXML 
	private CheckBox novOfferCode; 

	@FXML 
	private CheckBox novMemberShare; 

	@FXML 
	private CheckBox novSafeHarbor; 

	@FXML 
	private CheckBox novSelfIns; 

	@FXML 
	private CheckBox novZip5; 

	@FXML 
	private CheckBox decOfferCode; 

	@FXML 
	private CheckBox decMemberShare; 

	@FXML 
	private CheckBox decSafeHarbor; 

	@FXML 
	private CheckBox decSelfIns; 

	@FXML 
	private CheckBox decZip5; 
	
	@FXML 
	private CheckBox covTxYr; 

	@FXML 
	private CheckBox c95CF1; 
	 
	@FXML 
	private CheckBox c95CF2; 
	 
	@FXML 
	private CheckBox c95CF3; 
	 
	@FXML 
	private CheckBox c95CF4; 
	 
	@FXML 
	private CheckBox c95CF5; 
	
	@FXML 
	private TextField emprIdIdx; 
	 
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
	private TextField annOfferCodeIdx; 
	
	@FXML 
	private TextField annMemberShareIdx; 
	 
	@FXML 
	private TextField annSafeHarborIdx; 

	@FXML 
	private TextField annSelfInsuredIdx; 
	 
	@FXML 
	private TextField annZip5Idx; 

	@FXML 
	private TextField janOfferCodeIdx; 

	@FXML 
	private TextField janMemberShareIdx; 

	@FXML 
	private TextField janSafeHarborIdx; 

	@FXML 
	private TextField janSelfInsIdx; 

	@FXML 
	private TextField janZip5Idx; 

	@FXML 
	private TextField febOfferCodeIdx; 

	@FXML 
	private TextField febMemberShareIdx; 

	@FXML 
	private TextField febSafeHarborIdx; 

	@FXML 
	private TextField febSelfInsIdx; 

	@FXML 
	private TextField febZip5Idx; 

	@FXML 
	private TextField marOfferCodeIdx; 

	@FXML 
	private TextField marMemberShareIdx; 

	@FXML 
	private TextField marSafeHarborIdx; 

	@FXML 
	private TextField marSelfInsIdx; 

	@FXML 
	private TextField marZip5Idx; 

	@FXML 
	private TextField aprOfferCodeIdx; 

	@FXML 
	private TextField aprMemberShareIdx; 

	@FXML 
	private TextField aprSafeHarborIdx; 

	@FXML 
	private TextField aprSelfInsIdx; 

	@FXML 
	private TextField aprZip5Idx; 

	@FXML 
	private TextField mayOfferCodeIdx; 

	@FXML 
	private TextField mayMemberShareIdx; 

	@FXML 
	private TextField maySafeHarborIdx; 

	@FXML 
	private TextField maySelfInsIdx; 

	@FXML 
	private TextField mayZip5Idx; 

	@FXML 
	private TextField junOfferCodeIdx; 

	@FXML 
	private TextField junMemberShareIdx; 

	@FXML 
	private TextField junSafeHarborIdx; 

	@FXML 
	private TextField junSelfInsIdx; 

	@FXML 
	private TextField junZip5Idx; 

	@FXML 
	private TextField julOfferCodeIdx; 

	@FXML 
	private TextField julMemberShareIdx; 

	@FXML 
	private TextField julSafeHarborIdx; 

	@FXML 
	private TextField julSelfInsIdx; 

	@FXML 
	private TextField julZip5Idx; 

	@FXML 
	private TextField augOfferCodeIdx; 

	@FXML 
	private TextField augMemberShareIdx; 

	@FXML 
	private TextField augSafeHarborIdx; 

	@FXML 
	private TextField augSelfInsIdx; 

	@FXML 
	private TextField augZip5Idx; 

	@FXML 
	private TextField sepOfferCodeIdx; 

	@FXML 
	private TextField sepMemberShareIdx; 

	@FXML 
	private TextField sepSafeHarborIdx; 

	@FXML 
	private TextField sepSelfInsIdx; 

	@FXML 
	private TextField sepZip5Idx; 

	@FXML 
	private TextField octOfferCodeIdx; 

	@FXML 
	private TextField octMemberShareIdx; 

	@FXML 
	private TextField octSafeHarborIdx; 

	@FXML 
	private TextField octSelfInsIdx; 

	@FXML 
	private TextField octZip5Idx; 

	@FXML 
	private TextField novOfferCodeIdx; 

	@FXML 
	private TextField novMemberShareIdx; 

	@FXML 
	private TextField novSafeHarborIdx; 

	@FXML 
	private TextField novSelfInsIdx; 

	@FXML 
	private TextField novZip5Idx; 

	@FXML 
	private TextField decOfferCodeIdx; 

	@FXML 
	private TextField decMemberShareIdx; 

	@FXML 
	private TextField decSafeHarborIdx; 

	@FXML 
	private TextField decSelfInsIdx; 

	@FXML 
	private TextField decZip5Idx; 
	
	@FXML 
	private TextField covTxYrIdx; 
	 
	@FXML 
	private TextField c95CF1Idx; 
	 
	@FXML 
	private TextField c95CF2Idx; 
	 
	@FXML 
	private TextField c95CF3Idx; 
	 
	@FXML 
	private TextField c95CF4Idx; 
	 
	@FXML 
	private TextField c95CF5Idx; 
	 
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
	private TextField c95CField1; 
 
	@FXML 
	private TextField c95CField2; 
	 
	@FXML 
	private TextField c95CField3; 
 
	@FXML 
	private TextField c95CField4; 
	 
	@FXML 
	private TextField c95CField5; 
 
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
	private Button genRefButton; 
	 
	@FXML 
	private Button emplSsnParse; 
	 
	@FXML 
	private Button empFstNmParse; 
	 
	@FXML 
	private Button empDOBFormat; 
	 
	@FXML 
	private Button emplCityParse; 
	 
	@FXML 
	private Button emplMNmParse; 
	 
	@FXML 
	private Button emplHrDtFormat; 
	 
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
	private Button annSelfInsuredParse; 

	@FXML 
	private Button annZp5Parse; 
	 
	@FXML 
	private Button covTxYrParse; 
	 
	@FXML 
	private Button janSelfInsParse; 
	 
	@FXML 
	private Button febSelfInsParse; 
	 
	@FXML 
	private Button marSelfInsParse; 
	 
	@FXML 
	private Button aprSelfInsParse; 
	 
	@FXML 
	private Button maySelfInsParse; 
	 
	@FXML 
	private Button junSelfInsParse; 
	 
	@FXML 
	private Button julSelfInsParse; 
	 
	@FXML 
	private Button augSelfInsParse; 
	 
	@FXML 
	private Button sepSelfInsParse; 
	 
	@FXML 
	private Button octSelfInsParse; 
	 
	@FXML 
	private Button novSelfInsParse; 
	 
	@FXML 
	private Button decSelfInsParse; 
	 
	@FXML 
	private Button janZip5Parse; 
	 
	@FXML 
	private Button febZip5Parse; 
	 
	@FXML 
	private Button marZip5Parse; 
	 
	@FXML 
	private Button aprZip5Parse; 
	 
	@FXML 
	private Button mayZip5Parse; 
	 
	@FXML 
	private Button junZip5Parse; 
	 
	@FXML 
	private Button julZip5Parse; 
	 
	@FXML 
	private Button augZip5Parse; 
	 
	@FXML 
	private Button sepZip5Parse; 
	 
	@FXML 
	private Button octZip5Parse; 
	 
	@FXML 
	private Button novZip5Parse; 
	 
	@FXML 
	private Button decZip5Parse; 
	 
	@FXML 
	private Button emplCF1Parse; 
	 
	@FXML 
	private Button emplCF2Parse; 
	 
	@FXML 
	private Button c95CF1Parse; 
	 
	@FXML 
	private Button c95CF2Parse; 

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
//		dfLockedCheck.setDisable(false); 
 
		//TODO: REMOVE THIS SINCE WE'RE SPLITTING FORMS 
		//enable the addspec button only for the applicable types 
		dfcovPayAdditionalHoursButton.setVisible(false); 
//		switch(fileType) 
//		{ 
//			case COVERAGE: 
//				dfcovAddIgnoreSpecButton.setDisable(false); 
//				dfcovPayAdditionalHoursButton.setVisible(true); 
//				break; 
//			default: 
//				dfcovAddIgnoreSpecButton.setDisable(true); 
//				break; 
//		} 
 
		//IGNOREROWSPECS 
//		dfcovIgnoreRowSpecsListView.setOnMouseClicked(mouseClickedEvent ->  
//		{ 
//            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
//            { 
//        		switch(fileType) 
//        		{ 
//	        		case COVERAGE: 
//	                	DataManager.i().mDynamicCoverageFileIgnoreRowSpecification = dfcovIgnoreRowSpecsListView.getSelectionModel().getSelectedItem().getCovSpec(); 
//	        			break; 
//	        		default: 
//	        			return; 
//        		} 
//        		viewIgnoreRow(); 
//            } 
//        }); 
 
		// EMPLOYER ID REFERENCE 
		emprRefButton.setOnMouseClicked(mouseClickedEvent ->  
		{ 
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY))  
            { 
            	loadEmployerRefForm(); 
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
		empFstNmIdx.setDisable(true); 
		empDOBIdx.setDisable(true); 
		emplJobIdx.setDisable(true); 
		emplStNmIdx.setDisable(true); 
		emplCityIdx.setDisable(true); 
		emplIdIdx.setDisable(true); 
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
		annOfferCodeIdx.setDisable(true); 
		annSafeHarborIdx.setDisable(true); 
		annSelfInsuredIdx.setDisable(true); 
		covTxYrIdx.setDisable(true); 
		annMemberShareIdx.setDisable(true);
		annZip5Idx.setDisable(true); 
		janOfferCodeIdx.setDisable(true); 
		febOfferCodeIdx.setDisable(true); 
		marOfferCodeIdx.setDisable(true); 
		aprOfferCodeIdx.setDisable(true); 
		mayOfferCodeIdx.setDisable(true); 
		junOfferCodeIdx.setDisable(true); 
		julOfferCodeIdx.setDisable(true); 
		augOfferCodeIdx.setDisable(true); 
		sepOfferCodeIdx.setDisable(true); 
		octOfferCodeIdx.setDisable(true); 
		novOfferCodeIdx.setDisable(true); 
		decOfferCodeIdx.setDisable(true); 
		janMemberShareIdx.setDisable(true); 
		febMemberShareIdx.setDisable(true); 
		marMemberShareIdx.setDisable(true); 
		aprMemberShareIdx.setDisable(true); 
		mayMemberShareIdx.setDisable(true); 
		junMemberShareIdx.setDisable(true); 
		julMemberShareIdx.setDisable(true); 
		augMemberShareIdx.setDisable(true); 
		sepMemberShareIdx.setDisable(true); 
		octMemberShareIdx.setDisable(true); 
		novMemberShareIdx.setDisable(true); 
		decMemberShareIdx.setDisable(true); 
		janSafeHarborIdx.setDisable(true); 
		febSafeHarborIdx.setDisable(true); 
		marSafeHarborIdx.setDisable(true); 
		aprSafeHarborIdx.setDisable(true); 
		maySafeHarborIdx.setDisable(true); 
		junSafeHarborIdx.setDisable(true); 
		julSafeHarborIdx.setDisable(true); 
		augSafeHarborIdx.setDisable(true); 
		sepSafeHarborIdx.setDisable(true); 
		octSafeHarborIdx.setDisable(true); 
		novSafeHarborIdx.setDisable(true); 
		decSafeHarborIdx.setDisable(true); 
		janSelfInsIdx.setDisable(true); 
		febSelfInsIdx.setDisable(true); 
		marSelfInsIdx.setDisable(true); 
		aprSelfInsIdx.setDisable(true); 
		maySelfInsIdx.setDisable(true); 
		junSelfInsIdx.setDisable(true); 
		julSelfInsIdx.setDisable(true); 
		augSelfInsIdx.setDisable(true); 
		sepSelfInsIdx.setDisable(true); 
		octSelfInsIdx.setDisable(true); 
		novSelfInsIdx.setDisable(true); 
		decSelfInsIdx.setDisable(true); 
		janZip5Idx.setDisable(true); 
		febZip5Idx.setDisable(true); 
		marZip5Idx.setDisable(true); 
		aprZip5Idx.setDisable(true); 
		mayZip5Idx.setDisable(true); 
		junZip5Idx.setDisable(true); 
		julZip5Idx.setDisable(true); 
		augZip5Idx.setDisable(true); 
		sepZip5Idx.setDisable(true); 
		octZip5Idx.setDisable(true); 
		novZip5Idx.setDisable(true); 
		decZip5Idx.setDisable(true); 
		c95CF1Idx.setDisable(true); 
		c95CF2Idx.setDisable(true); 
		c95CF3Idx.setDisable(true); 
		c95CF4Idx.setDisable(true); 
		c95CF5Idx.setDisable(true); 

		// SET BUTTON TEXT 
		empFstNmParse.setText("+ Pattern"); 
		empFstNmParse.setFont(Font.font("Veranda", 12.0)); 
		empFstNmParse.setDisable(true);		 
		empDOBFormat.setText("+ Format"); 
		empDOBFormat.setFont(Font.font("Veranda", 12.0)); 
		empDOBFormat.setDisable(true);		 
		emplCityParse.setText("+ Pattern"); 
		emplCityParse.setFont(Font.font("Veranda", 12.0)); 
		emplCityParse.setDisable(true);	
		emplrEmpIdParse.setText("+ Pattern"); 
		emplrEmpIdParse.setFont(Font.font("Veranda", 12.0)); 
		emplrEmpIdParse.setDisable(true);		 
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
		annSelfInsuredParse.setText("+ Pattern"); 
		annSelfInsuredParse.setFont(Font.font("Veranda", 12.0)); 
		annSelfInsuredParse.setDisable(true);		 
		annZp5Parse.setText("+ Pattern"); 
		annZp5Parse.setFont(Font.font("Veranda", 12.0)); 
		annZp5Parse.setDisable(true);		 
		covTxYrParse.setText("+ Pattern"); 
		covTxYrParse.setFont(Font.font("Veranda", 12.0)); 
		covTxYrParse.setDisable(true);		 
		janSelfInsParse.setText("+ Pattern"); 
		janSelfInsParse.setFont(Font.font("Veranda", 12.0)); 
		janSelfInsParse.setDisable(true);		 
		febSelfInsParse.setText("+ Pattern"); 
		febSelfInsParse.setFont(Font.font("Veranda", 12.0)); 
		febSelfInsParse.setDisable(true);		 
		marSelfInsParse.setText("+ Pattern"); 
		marSelfInsParse.setFont(Font.font("Veranda", 12.0)); 
		marSelfInsParse.setDisable(true);	
		aprSelfInsParse.setText("+ Pattern"); 
		aprSelfInsParse.setFont(Font.font("Veranda", 12.0)); 
		aprSelfInsParse.setDisable(true);	
		maySelfInsParse.setText("+ Pattern"); 
		maySelfInsParse.setFont(Font.font("Veranda", 12.0)); 
		maySelfInsParse.setDisable(true);	
		junSelfInsParse.setText("+ Pattern"); 
		junSelfInsParse.setFont(Font.font("Veranda", 12.0)); 
		junSelfInsParse.setDisable(true);		 
		julSelfInsParse.setText("+ Pattern"); 
		julSelfInsParse.setFont(Font.font("Veranda", 12.0)); 
		julSelfInsParse.setDisable(true);		 
		augSelfInsParse.setText("+ Pattern"); 
		augSelfInsParse.setFont(Font.font("Veranda", 12.0)); 
		augSelfInsParse.setDisable(true);		 
		sepSelfInsParse.setText("+ Pattern"); 
		sepSelfInsParse.setFont(Font.font("Veranda", 12.0)); 
		sepSelfInsParse.setDisable(true);		 
		octSelfInsParse.setText("+ Pattern"); 
		octSelfInsParse.setFont(Font.font("Veranda", 12.0)); 
		octSelfInsParse.setDisable(true);		 
		novSelfInsParse.setText("+ Pattern"); 
		novSelfInsParse.setFont(Font.font("Veranda", 12.0)); 
		novSelfInsParse.setDisable(true);		 
		decSelfInsParse.setText("+ Pattern"); 
		decSelfInsParse.setFont(Font.font("Veranda", 12.0)); 
		decSelfInsParse.setDisable(true);		 
		janZip5Parse.setText("+ Pattern"); 
		janZip5Parse.setFont(Font.font("Veranda", 12.0)); 
		janZip5Parse.setDisable(true);		 
		febZip5Parse.setText("+ Pattern"); 
		febZip5Parse.setFont(Font.font("Veranda", 12.0)); 
		febZip5Parse.setDisable(true);		 
		marZip5Parse.setText("+ Pattern"); 
		marZip5Parse.setFont(Font.font("Veranda", 12.0)); 
		marZip5Parse.setDisable(true);	
		aprZip5Parse.setText("+ Pattern"); 
		aprZip5Parse.setFont(Font.font("Veranda", 12.0)); 
		aprZip5Parse.setDisable(true);	
		mayZip5Parse.setText("+ Pattern"); 
		mayZip5Parse.setFont(Font.font("Veranda", 12.0)); 
		mayZip5Parse.setDisable(true);	
		junZip5Parse.setText("+ Pattern"); 
		junZip5Parse.setFont(Font.font("Veranda", 12.0)); 
		junZip5Parse.setDisable(true);		 
		julZip5Parse.setText("+ Pattern"); 
		julZip5Parse.setFont(Font.font("Veranda", 12.0)); 
		julZip5Parse.setDisable(true);		 
		augZip5Parse.setText("+ Pattern"); 
		augZip5Parse.setFont(Font.font("Veranda", 12.0)); 
		augZip5Parse.setDisable(true);		 
		sepZip5Parse.setText("+ Pattern"); 
		sepZip5Parse.setFont(Font.font("Veranda", 12.0)); 
		sepZip5Parse.setDisable(true);		 
		octZip5Parse.setText("+ Pattern"); 
		octZip5Parse.setFont(Font.font("Veranda", 12.0)); 
		octZip5Parse.setDisable(true);		 
		novZip5Parse.setText("+ Pattern"); 
		novZip5Parse.setFont(Font.font("Veranda", 12.0)); 
		novZip5Parse.setDisable(true);		 
		decZip5Parse.setText("+ Pattern"); 
		decZip5Parse.setFont(Font.font("Veranda", 12.0)); 
		decZip5Parse.setDisable(true);		 
		c95CF1Parse.setText("+ Pattern"); 
		c95CF1Parse.setFont(Font.font("Veranda", 12.0)); 
		c95CF1Parse.setDisable(true);		 
		c95CF2Parse.setText("+ Pattern"); 
		c95CF2Parse.setFont(Font.font("Veranda", 12.0)); 
		c95CF2Parse.setDisable(true);		 

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
        mapperFields.put(emplIdSkPn, new MapperField(emplIdSkPn, emplId, emplIdIdx, null, null, null, "ETC EE ID", 66)); 
        mapperFields.put(emplSsnSkPn, new MapperField(emplSsnSkPn, emplSsn, emplSsnIdx, emplSsnParse, null, null, "EE SSN", 54)); 
        mapperFields.put(emplGendSkPn, new MapperField(emplGendSkPn, emplGend, emplGendIdx, null, null, genRefButton, "Gender", 56)); 
        mapperFields.put(empFstNmSkPn, new MapperField(empFstNmSkPn, empFstNm, empFstNmIdx, empFstNmParse, null, null, "EE First Name", 90)); 
        mapperFields.put(emplMNmSkPn, new MapperField(emplMNmSkPn, emplMNm, emplMNmIdx, emplMNmParse, null, null, "EE Mid Name", 90)); 
        mapperFields.put(emplLstNmSkPn, new MapperField(emplLstNmSkPn, emplLstNm, emplLstNmIdx, emplLstNmParse, null, null, "EE Last Name", 89)); 
        mapperFields.put(empDOBSkPn, new MapperField(empDOBSkPn, empDOB, empDOBIdx, null, empDOBFormat, null, "EE DOB" , 57)); 
        mapperFields.put(emplTrmDtSkPn, new MapperField(emplTrmDtSkPn, emplTrmDt, emplTrmDtIdx, null, emplTrmDtFormat, null, "EE Term Date", 89)); 
        mapperFields.put(emplHrDtSkPn, new MapperField(emplHrDtSkPn, emplHrDt, emplHrDtIdx, null, emplHrDtFormat, null, "EE Hire Date", 84)); 
        mapperFields.put(emplJobSkPn, new MapperField(emplJobSkPn, emplJob, emplJobIdx, null, null, null, "EE Job Title", 76)); 
        mapperFields.put(emplPhnSkPn, new MapperField(emplPhnSkPn, emplPhn, emplPhnIdx, null, null, null, "EE Phone", 66)); 
        mapperFields.put(emplEmlSkPn, new MapperField(emplEmlSkPn, emplEml, emplEmlIdx, null, null, null, "EE Email", 60)); 
        mapperFields.put(emplStNmSkPn, new MapperField(emplStNmSkPn, emplStNm, emplStNmIdx, null, null, null, "EE Strt NMBR", 88)); 
        mapperFields.put(emplAdLn1SkPn, new MapperField(emplAdLn1SkPn, emplAdLn1, emplAdLn1Idx, emplAdLn1Parse, null, null, "EE ADD LN 1", 85)); 
        mapperFields.put(emplAdLn2SkPn, new MapperField(emplAdLn2SkPn, emplAdLn2, emplAdLn2Idx, null, null, null, "EE ADD LN 2", 85)); 
        mapperFields.put(emplCitySkPn, new MapperField(emplCitySkPn, emplCity, emplCityIdx, emplCityParse, null, null, "EE City", 52)); 
        mapperFields.put(emplStateSkPn, new MapperField(emplStateSkPn, emplState, emplStateIdx, emplStateParse, null, null, "EE State", 58)); 
        mapperFields.put(emplZipSkPn, new MapperField(emplZipSkPn, emplZip, emplZipIdx, emplZipParse, null, null, "EE Zip", 48)); 
        mapperFields.put(emplrEmpIdSkPn, new MapperField(emplrEmpIdSkPn, emplrEmpId, emplrEmpIdIdx, emplrEmpIdParse, null, null, "ER EE ID", 59)); 

        mapperFields.put(emplCF1SkPn, new MapperField(emplCF1SkPn, emplCF1, emplCF1Idx, emplCF1Parse, null, null, "EE CF 1", 55)); 
        mapperFields.put(emplCF2SkPn, new MapperField(emplCF2SkPn, emplCF2, emplCF2Idx, emplCF2Parse, null, null, "EE CF 2", 55)); 
        mapperFields.put(emplCF3SkPn, new MapperField(emplCF3SkPn, emplCF3, emplCF3Idx, null, null, null, "EE CF 3", 55)); 
        mapperFields.put(emplCF4SkPn, new MapperField(emplCF4SkPn, emplCF4, emplCF4Idx, null, null, null, "EE CF 4", 55)); 
        mapperFields.put(emplCF5SkPn, new MapperField(emplCF5SkPn, emplCF5, emplCF5Idx, null, null, null, "EE CF 5", 55)); 
        
        //DEPENDENT SECTION 
        mapperFields.put(depIdSkPn, new MapperField(depIdSkPn, depId, depIdIdx, null, null, null, "ETC DEP ID", 76)); 
        mapperFields.put(depSSNSkPn, new MapperField(depSSNSkPn, depSSN, depSSNIdx, depSSNParse, null, null, "DEP SSN", 62)); 
        mapperFields.put(depDOBSkPn, new MapperField(depDOBSkPn, depDOB, depDOBIdx, null, depDOBFormat, null, "DEP DOB", 65)); 
        mapperFields.put(depFrstNmSkPn, new MapperField(depFrstNmSkPn, depFrstNm, depFrstNmIdx, depFrstNmParse, null, null, "DEP First Name", 100)); 
        mapperFields.put(depMdNmSkPn, new MapperField(depMdNmSkPn, depMdNm, depMdNmIdx, depMdNmParse, null, null, "DEP Mid Name", 99)); 
        mapperFields.put(depLstNmSkPn, new MapperField(depLstNmSkPn, depLstNm, depLstNmIdx, depLstNmParse, null, null, "DEP Last Name", 99)); 
        mapperFields.put(depEmprIdSkPn, new MapperField(depEmprIdSkPn, depEmprId, depEmprIdIdx, depEmprIdParse, null, null, "DEP EMPR ID", 87)); 
        mapperFields.put(depStrtNmSkPn, new MapperField(depStrtNmSkPn, depStrtNm, depStrtNmIdx, null, null, null, "DEP Strt NMBR", 97)); 
        mapperFields.put(depAdLn1SkPn, new MapperField(depAdLn1SkPn, depAdLn1, depAdLn1Idx, depAdLn1Parse, null, null, "DEP ADD LN 1", 95)); 
        mapperFields.put(depAdLn2SkPn, new MapperField(depAdLn2SkPn, depAdLn2, depAdLn2Idx, null, null, null, "DEP ADD LN 2", 95)); 
        mapperFields.put(depZipSkPn, new MapperField(depZipSkPn, depZip, depZipIdx, depZipParse, null, null, "DEP Zip", 58)); 
        mapperFields.put(depCitySkPn, new MapperField(depCitySkPn, depCity, depCityIdx, depCityParse, null, null, "DEP City", 62)); 
        mapperFields.put(depStateSkPn, new MapperField(depStateSkPn, depState, depStateIdx, depStateParse, null, null, "DEP State", 68)); 

        mapperFields.put(depCF1SkPn, new MapperField(depCF1SkPn, depCF1, depCF1Idx, depCF1Parse, null, null, "DEP CF 1", 63)); 
        mapperFields.put(depCF2SkPn, new MapperField(depCF2SkPn, depCF2, depCF2Idx, depCF2Parse, null, null, "DEP CF 2", 63)); 
        mapperFields.put(depCF3SkPn, new MapperField(depCF3SkPn, depCF3, depCF3Idx, null, null, null, "DEP CF 3", 63)); 
        mapperFields.put(depCF4SkPn, new MapperField(depCF4SkPn, depCF4, depCF4Idx, null, null, null, "DEP CF 4", 63)); 
        mapperFields.put(depCF5SkPn, new MapperField(depCF5SkPn, depCF5, depCF5Idx, null, null, null, "DEP CF 5", 63)); 

        //1095C SECTION 
        mapperFields.put(annOfferCodeSkPn, new MapperField(annOfferCodeSkPn, annOfferCode, annOfferCodeIdx, null, null, null, "ANN OFF CODE", 102)); 
        mapperFields.put(annSafeHarborSkPn, new MapperField(annSafeHarborSkPn, annSafeHarbor, annSafeHarborIdx, null, null, null, "SAFE HBR CODE", 105)); 
        mapperFields.put(annSelfInsuredSkPn, new MapperField(annSelfInsuredSkPn, annSelfInsured, annSelfInsuredIdx, annSelfInsuredParse, null, null, "ANN SLF INS", 100)); 
        mapperFields.put(annMemberShareSkPn, new MapperField(annMemberShareSkPn, annMemberShare, annMemberShareIdx, null, null, null, "ANN MBR SHR", 96)); 
        mapperFields.put(annZip5SkPn, new MapperField(annZip5SkPn, annZip5, annZip5Idx, annZp5Parse, null, null, "ANN ZIP 5", 90)); 
        mapperFields.put(covTxYrSkPn, new MapperField(covTxYrSkPn, covTxYr, covTxYrIdx, covTxYrParse, null, null, "Tax Year", 61)); 
        
        mapperFields.put(janOfferCodeSkPn, new MapperField(janOfferCodeSkPn, janOfferCode, janOfferCodeIdx, null, null, null, "JAN OFF CODE", 95)); 
        mapperFields.put(febOfferCodeSkPn, new MapperField(febOfferCodeSkPn, febOfferCode, febOfferCodeIdx, null, null, null, "FEB OFF CODE", 95)); 
        mapperFields.put(marOfferCodeSkPn, new MapperField(marOfferCodeSkPn, marOfferCode, marOfferCodeIdx, null, null, null, "MAR OFF CODE", 95)); 
        mapperFields.put(aprOfferCodeSkPn, new MapperField(aprOfferCodeSkPn, aprOfferCode, aprOfferCodeIdx, null, null, null, "APR OFF CODE", 95)); 
        mapperFields.put(mayOfferCodeSkPn, new MapperField(mayOfferCodeSkPn, mayOfferCode, mayOfferCodeIdx, null, null, null, "MAY OFF CODE", 95)); 
        mapperFields.put(junOfferCodeSkPn, new MapperField(junOfferCodeSkPn, junOfferCode, junOfferCodeIdx, null, null, null, "JUN OFF CODE", 95)); 
        mapperFields.put(julOfferCodeSkPn, new MapperField(julOfferCodeSkPn, julOfferCode, julOfferCodeIdx, null, null, null, "JUL OFF CODE", 95)); 
        mapperFields.put(augOfferCodeSkPn, new MapperField(augOfferCodeSkPn, augOfferCode, augOfferCodeIdx, null, null, null, "AUG OFF CODE", 95)); 
        mapperFields.put(sepOfferCodeSkPn, new MapperField(sepOfferCodeSkPn, sepOfferCode, sepOfferCodeIdx, null, null, null, "SEP OFF CODE", 95)); 
        mapperFields.put(octOfferCodeSkPn, new MapperField(octOfferCodeSkPn, octOfferCode, octOfferCodeIdx, null, null, null, "OCT OFF CODE", 95)); 
        mapperFields.put(novOfferCodeSkPn, new MapperField(novOfferCodeSkPn, novOfferCode, novOfferCodeIdx, null, null, null, "NOV OFF CODE", 95)); 
        mapperFields.put(decOfferCodeSkPn, new MapperField(decOfferCodeSkPn, decOfferCode, decOfferCodeIdx, null, null, null, "DEC OFF CODE", 95)); 
        
        mapperFields.put(janMemberShareSkPn, new MapperField(janMemberShareSkPn, janMemberShare, janMemberShareIdx, null, null, null, "JAN MBR SHR", 95)); 
        mapperFields.put(febMemberShareSkPn, new MapperField(febMemberShareSkPn, febMemberShare, febMemberShareIdx, null, null, null, "FEB MBR SHR", 95)); 
        mapperFields.put(marMemberShareSkPn, new MapperField(marMemberShareSkPn, marMemberShare, marMemberShareIdx, null, null, null, "MAR MBR SHR", 95)); 
        mapperFields.put(aprMemberShareSkPn, new MapperField(aprMemberShareSkPn, aprMemberShare, aprMemberShareIdx, null, null, null, "APR MBR SHR", 95)); 
        mapperFields.put(mayMemberShareSkPn, new MapperField(mayMemberShareSkPn, mayMemberShare, mayMemberShareIdx, null, null, null, "MAY MBR SHR", 95)); 
        mapperFields.put(junMemberShareSkPn, new MapperField(junMemberShareSkPn, junMemberShare, junMemberShareIdx, null, null, null, "JUN MBR SHR", 95)); 
        mapperFields.put(julMemberShareSkPn, new MapperField(julMemberShareSkPn, julMemberShare, julMemberShareIdx, null, null, null, "JUL MBR SHR", 95)); 
        mapperFields.put(augMemberShareSkPn, new MapperField(augMemberShareSkPn, augMemberShare, augMemberShareIdx, null, null, null, "AUG MBR SHR", 95)); 
        mapperFields.put(sepMemberShareSkPn, new MapperField(sepMemberShareSkPn, sepMemberShare, sepMemberShareIdx, null, null, null, "SEP MBR SHR", 95)); 
        mapperFields.put(octMemberShareSkPn, new MapperField(octMemberShareSkPn, octMemberShare, octMemberShareIdx, null, null, null, "OCT MBR SHR", 95)); 
        mapperFields.put(novMemberShareSkPn, new MapperField(novMemberShareSkPn, novMemberShare, novMemberShareIdx, null, null, null, "NOV MBR SHR", 95)); 
        mapperFields.put(decMemberShareSkPn, new MapperField(decMemberShareSkPn, decMemberShare, decMemberShareIdx, null, null, null, "DEC MBR SHR", 95)); 
        
        mapperFields.put(janSafeHarborSkPn, new MapperField(janSafeHarborSkPn, janSafeHarbor, janSafeHarborIdx, null, null, null, "JAN SAFE HBR", 95)); 
        mapperFields.put(febSafeHarborSkPn, new MapperField(febSafeHarborSkPn, febSafeHarbor, febSafeHarborIdx, null, null, null, "FEB SAFE HBR", 95)); 
        mapperFields.put(marSafeHarborSkPn, new MapperField(marSafeHarborSkPn, marSafeHarbor, marSafeHarborIdx, null, null, null, "MAR SAFE HBR", 95)); 
        mapperFields.put(aprSafeHarborSkPn, new MapperField(aprSafeHarborSkPn, aprSafeHarbor, aprSafeHarborIdx, null, null, null, "APR SAFE HBR", 95)); 
        mapperFields.put(maySafeHarborSkPn, new MapperField(maySafeHarborSkPn, maySafeHarbor, maySafeHarborIdx, null, null, null, "MAY SAFE HBR", 95)); 
        mapperFields.put(junSafeHarborSkPn, new MapperField(junSafeHarborSkPn, junSafeHarbor, junSafeHarborIdx, null, null, null, "JUN SAFE HBR", 95)); 
        mapperFields.put(julSafeHarborSkPn, new MapperField(julSafeHarborSkPn, julSafeHarbor, julSafeHarborIdx, null, null, null, "JUL SAFE HBR", 95)); 
        mapperFields.put(augSafeHarborSkPn, new MapperField(augSafeHarborSkPn, augSafeHarbor, augSafeHarborIdx, null, null, null, "AUG SAFE HBR", 95)); 
        mapperFields.put(sepSafeHarborSkPn, new MapperField(sepSafeHarborSkPn, sepSafeHarbor, sepSafeHarborIdx, null, null, null, "SEP SAFE HBR", 95)); 
        mapperFields.put(octSafeHarborSkPn, new MapperField(octSafeHarborSkPn, octSafeHarbor, octSafeHarborIdx, null, null, null, "OCT SAFE HBR", 95)); 
        mapperFields.put(novSafeHarborSkPn, new MapperField(novSafeHarborSkPn, novSafeHarbor, novSafeHarborIdx, null, null, null, "NOV SAFE HBR", 95)); 
        mapperFields.put(decSafeHarborSkPn, new MapperField(decSafeHarborSkPn, decSafeHarbor, decSafeHarborIdx, null, null, null, "DEC SAFE HBR", 95)); 
        
        mapperFields.put(janSelfInsSkPn, new MapperField(janSelfInsSkPn, janSelfIns, janSelfInsIdx, janSelfInsParse, null, null, "JAN SLF INS", 95)); 
        mapperFields.put(febSelfInsSkPn, new MapperField(febSelfInsSkPn, febSelfIns, febSelfInsIdx, febSelfInsParse, null, null, "FEB SLF INS", 95)); 
        mapperFields.put(marSelfInsSkPn, new MapperField(marSelfInsSkPn, marSelfIns, marSelfInsIdx, marSelfInsParse, null, null, "MAR SLF INS", 95)); 
        mapperFields.put(aprSelfInsSkPn, new MapperField(aprSelfInsSkPn, aprSelfIns, aprSelfInsIdx, aprSelfInsParse, null, null, "APR SLF INS", 95)); 
        mapperFields.put(maySelfInsSkPn, new MapperField(maySelfInsSkPn, maySelfIns, maySelfInsIdx, maySelfInsParse, null, null, "MAY SLF INS", 95)); 
        mapperFields.put(junSelfInsSkPn, new MapperField(junSelfInsSkPn, junSelfIns, junSelfInsIdx, junSelfInsParse, null, null, "JUN SLF INS", 95)); 
        mapperFields.put(julSelfInsSkPn, new MapperField(julSelfInsSkPn, julSelfIns, julSelfInsIdx, julSelfInsParse, null, null, "JUL SLF INS", 95)); 
        mapperFields.put(augSelfInsSkPn, new MapperField(augSelfInsSkPn, augSelfIns, augSelfInsIdx, augSelfInsParse, null, null, "AUG SLF INS", 95)); 
        mapperFields.put(sepSelfInsSkPn, new MapperField(sepSelfInsSkPn, sepSelfIns, sepSelfInsIdx, sepSelfInsParse, null, null, "SEP SLF INS", 95)); 
        mapperFields.put(octSelfInsSkPn, new MapperField(octSelfInsSkPn, octSelfIns, octSelfInsIdx, octSelfInsParse, null, null, "OCT SLF INS", 95)); 
        mapperFields.put(novSelfInsSkPn, new MapperField(novSelfInsSkPn, novSelfIns, novSelfInsIdx, novSelfInsParse, null, null, "NOV SLF INS", 95)); 
        mapperFields.put(decSelfInsSkPn, new MapperField(decSelfInsSkPn, decSelfIns, decSelfInsIdx, decSelfInsParse, null, null, "DEC SLF INS", 95)); 
        
        mapperFields.put(janZip5SkPn, new MapperField(janZip5SkPn, janZip5, janZip5Idx, janZip5Parse, null, null, "JAN ZIP 5", 95)); 
        mapperFields.put(febZip5SkPn, new MapperField(febZip5SkPn, febZip5, febZip5Idx, febZip5Parse, null, null, "FEB ZIP 5", 95)); 
        mapperFields.put(marZip5SkPn, new MapperField(marZip5SkPn, marZip5, marZip5Idx, marZip5Parse, null, null, "MAR ZIP 5", 95)); 
        mapperFields.put(aprZip5SkPn, new MapperField(aprZip5SkPn, aprZip5, aprZip5Idx, aprZip5Parse, null, null, "APR ZIP 5", 95)); 
        mapperFields.put(mayZip5SkPn, new MapperField(mayZip5SkPn, mayZip5, mayZip5Idx, mayZip5Parse, null, null, "MAY ZIP 5", 95)); 
        mapperFields.put(junZip5SkPn, new MapperField(junZip5SkPn, junZip5, junZip5Idx, junZip5Parse, null, null, "JUN ZIP 5", 95)); 
        mapperFields.put(julZip5SkPn, new MapperField(julZip5SkPn, julZip5, julZip5Idx, julZip5Parse, null, null, "JUL ZIP 5", 95)); 
        mapperFields.put(augZip5SkPn, new MapperField(augZip5SkPn, augZip5, augZip5Idx, augZip5Parse, null, null, "AUG ZIP 5", 95)); 
        mapperFields.put(sepZip5SkPn, new MapperField(sepZip5SkPn, sepZip5, sepZip5Idx, sepZip5Parse, null, null, "SEP ZIP 5", 95)); 
        mapperFields.put(octZip5SkPn, new MapperField(octZip5SkPn, octZip5, octZip5Idx, octZip5Parse, null, null, "OCT ZIP 5", 95)); 
        mapperFields.put(novZip5SkPn, new MapperField(novZip5SkPn, novZip5, novZip5Idx, novZip5Parse, null, null, "NOV ZIP 5", 95)); 
        mapperFields.put(decZip5SkPn, new MapperField(decZip5SkPn, decZip5, decZip5Idx, decZip5Parse, null, null, "DEC ZIP 5", 95)); 

        mapperFields.put(c95CF1SkPn, new MapperField(c95CF1SkPn, c95CF1, c95CF1Idx, c95CF1Parse, null, null, "C95 CF 1", 68)); 
        mapperFields.put(c95CF2SkPn, new MapperField(c95CF2SkPn, c95CF2, c95CF2Idx, c95CF2Parse, null, null, "C95 CF 2", 68)); 
        mapperFields.put(c95CF3SkPn, new MapperField(c95CF3SkPn, c95CF3, c95CF3Idx, null, null, null, "C95 CF 3", 68)); 
        mapperFields.put(c95CF4SkPn, new MapperField(c95CF4SkPn, c95CF4, c95CF4Idx, null, null, null, "C95 CF 4", 68)); 
        mapperFields.put(c95CF5SkPn, new MapperField(c95CF5SkPn, c95CF5, c95CF5Idx, null, null, null, "C95 CF 5", 68)); 
 
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
        		DynamicIrs1095cFileSpecificationRequest dynCovReq = new DynamicIrs1095cFileSpecificationRequest(DataManager.i().mDynamicIrs1095cFileSpecification); 
        		dynCovReq.setId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
 
        		DataManager.i().mDynamicIrs1095cFileSpecification = AdminPersistenceManager.getInstance().get(dynCovReq); 
 
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
		updateIrs1095cFileData(); 
		checkReadOnly(); 
        EtcAdmin.i().setStatusMessage("Ready"); 
        EtcAdmin.i().setProgress(0); 
	} 
 
	/* 
	 * DynamicIrs1095cFileSpecification Support 
	 */ 
	private void updateIrs1095cFileData() 
	{ 
		DynamicIrs1095cFileSpecification fSpec = DataManager.i().mDynamicIrs1095cFileSpecification; 
		
		if(fSpec != null)  
		{ 
			readOnly = fSpec.isLocked(); 
			Utils.updateControl(dfcovNameLabel,fSpec.getName()); 
			Utils.updateControl(dfcovTabIndexLabel,String.valueOf(fSpec.getTabIndex()));
			Utils.updateControl(dfcovHeaderRowIndexLabel,String.valueOf(fSpec.getHeaderRowIndex())); 
			Utils.updateControl(dfcovCreateEmployeeCheck,fSpec.isCreateEmployee()); 
			Utils.updateControl(dfLockedCheck,fSpec.isLocked()); 
			dfcovMapEEtoERCheck.setVisible(true); 
			Utils.updateControl(dfcovMapEEtoERCheck,fSpec.isMapEEtoER()); 
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
 
			Utils.updateControl(secCF1, fSpec.getDepCfld1Name()); 
			Utils.updateControl(secCF2, fSpec.getDepCfld2Name()); 
			Utils.updateControl(secCF3, fSpec.getDepCfld3Name()); 
			Utils.updateControl(secCF4, fSpec.getDepCfld4Name()); 
			Utils.updateControl(secCF5, fSpec.getDepCfld5Name()); 

			Utils.updateControl(c95CField1, fSpec.getC95Cfld1Name()); 
			Utils.updateControl(c95CField2, fSpec.getC95Cfld2Name()); 
			Utils.updateControl(c95CField3, fSpec.getC95Cfld3Name()); 
			Utils.updateControl(c95CField4, fSpec.getC95Cfld4Name()); 
			Utils.updateControl(c95CField5, fSpec.getC95Cfld5Name()); 

			//reference counts 
			setEmpRefCount(getIrs1095cFileEmployerRefCount()); 
			setGendRefCount(getIrs1095cFileGenderTypeRefCount()); 
 
			//update the column field table 
			loadFieldSet1(fSpec);	  
			loadFieldSet2(fSpec); 
			loadFieldSet3(fSpec); 
		} 
	} 
 
	//Generate the coverage spec type 
	private void loadFieldSet1(DynamicIrs1095cFileSpecification fSpec) 
	{ 
		//EMPLOYEE SECTION 
		enableFieldSet(fSpec.isErCol(), fSpec.getErColIndex(), emprIdSkPn, null, null); 
		enableFieldSet(fSpec.isfNameCol(), fSpec.getfNameColIndex(), empFstNmSkPn, fSpec.getfNameParsePatternId(), null); 
		enableFieldSet(fSpec.isDobCol(), fSpec.getDobColIndex(), empDOBSkPn, null, fSpec.getDobFormatId()); 
		enableFieldSet(fSpec.isJobTtlCol(), fSpec.getJobTtlColIndex(), emplJobSkPn, null, null); 
		enableFieldSet(fSpec.isStreetNumCol(), fSpec.getStreetNumColIndex(), emplStNmSkPn, null, null); 
		enableFieldSet(fSpec.isCityCol(), fSpec.getCityColIndex(), emplCitySkPn, fSpec.getCityParsePatternId(), null); 
		enableFieldSet(fSpec.isEtcRefCol(), fSpec.getEtcRefColIndex(), emplIdSkPn, null, null); 
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
	 
	private void loadFieldSet2(DynamicIrs1095cFileSpecification fSpec)  
	{ 
		//DEPENDENT SECTION 
		enableFieldSet(fSpec.isDepEtcRefCol(), fSpec.getDepEtcRefColIndex(), depIdSkPn, null, null); 
		enableFieldSet(fSpec.isDepFNameCol(), fSpec.getDepFNameColIndex(), depFrstNmSkPn, fSpec.getDepFNameParsePatternId(), null); 
		enableFieldSet(fSpec.isDepErRefCol(), fSpec.getDepErRefColIndex(), depEmprIdSkPn, fSpec.getDepErRefParsePatternId(), null); 
		enableFieldSet(fSpec.isDepLin2Col(), fSpec.getDepLin2ColIndex(), depAdLn2SkPn, null, null); 
		enableFieldSet(fSpec.isDepZipCol(), fSpec.getDepZipColIndex(), depZipSkPn, fSpec.getDepZipParsePatternId(), null); 
		enableFieldSet(fSpec.isDepSSNCol(), fSpec.getDepSSNColIndex(), depSSNSkPn, fSpec.getDepSSNParsePatternId(), null); 
		enableFieldSet(fSpec.isDepMNameCol(), fSpec.getDepMNameColIndex(), depMdNmSkPn, fSpec.getDepMNameParsePatternId(), null); 
		enableFieldSet(fSpec.isDepStreetNumCol(), fSpec.getDepStreetNumColIndex(), depStrtNmSkPn, null, null); 
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
 
	private void loadFieldSet3(DynamicIrs1095cFileSpecification fSpec)  
	{ 
		//1095C SECTION
		enableFieldSet(fSpec.isAnnOCdCol(), fSpec.getAnnOCdColIndex(), annOfferCodeSkPn, null, null); 
		enableFieldSet(fSpec.isAnnSHCdCol(), fSpec.getAnnSHCdColIndex(), annSafeHarborSkPn, null, null); 
		enableFieldSet(fSpec.isAnnSelfCol(), fSpec.getAnnSelfColIndex(), annSelfInsuredSkPn, fSpec.getAnnSelfTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isAnnMbrShareCol(), fSpec.getAnnMbrShareColIndex(), annMemberShareSkPn, null, null); 
		enableFieldSet(fSpec.isAnnZp5Col(), fSpec.getAnnZp5ColIndex(), annZip5SkPn, fSpec.getAnnZp5ParsePatternId(), null); 
		enableFieldSet(fSpec.isTyCol(), fSpec.getTyColIndex(), covTxYrSkPn, fSpec.getTyParsePatternId(), null); 

		enableFieldSet(fSpec.isJanOCdCol(), fSpec.getJanOCdColIndex(), janOfferCodeSkPn, null, null); 
		enableFieldSet(fSpec.isFebOCdCol(), fSpec.getFebOCdColIndex(), febOfferCodeSkPn, null, null); 
		enableFieldSet(fSpec.isMarOCdCol(), fSpec.getMarOCdColIndex(), marOfferCodeSkPn, null, null); 
		enableFieldSet(fSpec.isAprOCdCol(), fSpec.getAprOCdColIndex(), aprOfferCodeSkPn, null, null); 
		enableFieldSet(fSpec.isMayOCdCol(), fSpec.getMayOCdColIndex(), mayOfferCodeSkPn, null, null); 
		enableFieldSet(fSpec.isJunOCdCol(), fSpec.getJunOCdColIndex(), junOfferCodeSkPn, null, null); 
		enableFieldSet(fSpec.isJulOCdCol(), fSpec.getJulOCdColIndex(), julOfferCodeSkPn, null, null); 
		enableFieldSet(fSpec.isAugOCdCol(), fSpec.getAugOCdColIndex(), augOfferCodeSkPn, null, null); 
		enableFieldSet(fSpec.isSepOCdCol(), fSpec.getSepOCdColIndex(), sepOfferCodeSkPn, null, null); 
		enableFieldSet(fSpec.isOctOCdCol(), fSpec.getOctOCdColIndex(), octOfferCodeSkPn, null, null); 
		enableFieldSet(fSpec.isNovOCdCol(), fSpec.getNovOCdColIndex(), novOfferCodeSkPn, null, null); 
		enableFieldSet(fSpec.isDecOCdCol(), fSpec.getDecOCdColIndex(), decOfferCodeSkPn, null, null); 
	
		enableFieldSet(fSpec.isJanMbrShareCol(), fSpec.getJanMbrShareColIndex(), janMemberShareSkPn, null, null); 
		enableFieldSet(fSpec.isFebMbrShareCol(), fSpec.getFebMbrShareColIndex(), febMemberShareSkPn, null, null); 
		enableFieldSet(fSpec.isMarMbrShareCol(), fSpec.getMarMbrShareColIndex(), marMemberShareSkPn, null, null); 
		enableFieldSet(fSpec.isAprMbrShareCol(), fSpec.getAprMbrShareColIndex(), aprMemberShareSkPn, null, null); 
		enableFieldSet(fSpec.isMayMbrShareCol(), fSpec.getMayMbrShareColIndex(), mayMemberShareSkPn, null, null); 
		enableFieldSet(fSpec.isJunMbrShareCol(), fSpec.getJunMbrShareColIndex(), junMemberShareSkPn, null, null); 
		enableFieldSet(fSpec.isJulMbrShareCol(), fSpec.getJulMbrShareColIndex(), julMemberShareSkPn, null, null); 
		enableFieldSet(fSpec.isAugMbrShareCol(), fSpec.getAugMbrShareColIndex(), augMemberShareSkPn, null, null); 
		enableFieldSet(fSpec.isSepMbrShareCol(), fSpec.getSepMbrShareColIndex(), sepMemberShareSkPn, null, null); 
		enableFieldSet(fSpec.isOctMbrShareCol(), fSpec.getOctMbrShareColIndex(), octMemberShareSkPn, null, null); 
		enableFieldSet(fSpec.isNovMbrShareCol(), fSpec.getNovMbrShareColIndex(), novMemberShareSkPn, null, null); 
		enableFieldSet(fSpec.isDecMbrShareCol(), fSpec.getDecMbrShareColIndex(), decMemberShareSkPn, null, null); 

		enableFieldSet(fSpec.isJanSHCdCol(), fSpec.getJanSHCdColIndex(), janSafeHarborSkPn, null, null); 
		enableFieldSet(fSpec.isFebSHCdCol(), fSpec.getFebSHCdColIndex(), febSafeHarborSkPn, null, null); 
		enableFieldSet(fSpec.isMarSHCdCol(), fSpec.getMarSHCdColIndex(), marSafeHarborSkPn, null, null); 
		enableFieldSet(fSpec.isAprSHCdCol(), fSpec.getAprSHCdColIndex(), aprSafeHarborSkPn, null, null); 
		enableFieldSet(fSpec.isMaySHCdCol(), fSpec.getMaySHCdColIndex(), maySafeHarborSkPn, null, null); 
		enableFieldSet(fSpec.isJunSHCdCol(), fSpec.getJunSHCdColIndex(), junSafeHarborSkPn, null, null); 
		enableFieldSet(fSpec.isJulSHCdCol(), fSpec.getJulSHCdColIndex(), julSafeHarborSkPn, null, null); 
		enableFieldSet(fSpec.isAugSHCdCol(), fSpec.getAugSHCdColIndex(), augSafeHarborSkPn, null, null); 
		enableFieldSet(fSpec.isSepSHCdCol(), fSpec.getSepSHCdColIndex(), sepSafeHarborSkPn, null, null); 
		enableFieldSet(fSpec.isOctSHCdCol(), fSpec.getOctSHCdColIndex(), octSafeHarborSkPn, null, null); 
		enableFieldSet(fSpec.isNovSHCdCol(), fSpec.getNovSHCdColIndex(), novSafeHarborSkPn, null, null); 
		enableFieldSet(fSpec.isDecSHCdCol(), fSpec.getDecSHCdColIndex(), decSafeHarborSkPn, null, null); 

		enableFieldSet(fSpec.isJanSelfCol(), fSpec.getJanSelfColIndex(), janSelfInsSkPn, fSpec.getJanSelfTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isFebSelfCol(), fSpec.getFebSelfColIndex(), febSelfInsSkPn, fSpec.getFebSelfTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isMarSelfCol(), fSpec.getMarSelfColIndex(), marSelfInsSkPn, fSpec.getMarSelfTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isAprSelfCol(), fSpec.getAprSelfColIndex(), aprSelfInsSkPn, fSpec.getAprSelfTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isMaySelfCol(), fSpec.getMaySelfColIndex(), maySelfInsSkPn, fSpec.getMaySelfTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isJunSelfCol(), fSpec.getJunSelfColIndex(), junSelfInsSkPn, fSpec.getJunSelfTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isJulSelfCol(), fSpec.getJulSelfColIndex(), julSelfInsSkPn, fSpec.getJulSelfTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isAugSelfCol(), fSpec.getAugSelfColIndex(), augSelfInsSkPn, fSpec.getAugSelfTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isSepSelfCol(), fSpec.getSepSelfColIndex(), sepSelfInsSkPn, fSpec.getSepSelfTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isOctSelfCol(), fSpec.getOctSelfColIndex(), octSelfInsSkPn, fSpec.getOctSelfTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isNovSelfCol(), fSpec.getNovSelfColIndex(), novSelfInsSkPn, fSpec.getNovSelfTrueParsePatternId(), null); 
		enableFieldSet(fSpec.isDecSelfCol(), fSpec.getDecSelfColIndex(), decSelfInsSkPn, fSpec.getDecSelfTrueParsePatternId(), null); 

		enableFieldSet(fSpec.isJanZp5Col(), fSpec.getJanZp5ColIndex(), janZip5SkPn, fSpec.getJanZp5ParsePatternId(), null); 
		enableFieldSet(fSpec.isFebZp5Col(), fSpec.getFebZp5ColIndex(), febZip5SkPn, fSpec.getFebZp5ParsePatternId(), null); 
		enableFieldSet(fSpec.isMarZp5Col(), fSpec.getMarZp5ColIndex(), marZip5SkPn, fSpec.getMarZp5ParsePatternId(), null); 
		enableFieldSet(fSpec.isAprZp5Col(), fSpec.getAprZp5ColIndex(), aprZip5SkPn, fSpec.getAprZp5ParsePatternId(), null); 
		enableFieldSet(fSpec.isMayZp5Col(), fSpec.getMayZp5ColIndex(), mayZip5SkPn, fSpec.getMayZp5ParsePatternId(), null); 
		enableFieldSet(fSpec.isJunZp5Col(), fSpec.getJunZp5ColIndex(), junZip5SkPn, fSpec.getJunZp5ParsePatternId(), null); 
		enableFieldSet(fSpec.isJulZp5Col(), fSpec.getJulZp5ColIndex(), julZip5SkPn, fSpec.getJulZp5ParsePatternId(), null); 
		enableFieldSet(fSpec.isAugZp5Col(), fSpec.getAugZp5ColIndex(), augZip5SkPn, fSpec.getAugZp5ParsePatternId(), null); 
		enableFieldSet(fSpec.isSepZp5Col(), fSpec.getSepZp5ColIndex(), sepZip5SkPn, fSpec.getSepZp5ParsePatternId(), null); 
		enableFieldSet(fSpec.isOctZp5Col(), fSpec.getOctZp5ColIndex(), octZip5SkPn, fSpec.getOctZp5ParsePatternId(), null); 
		enableFieldSet(fSpec.isNovZp5Col(), fSpec.getNovZp5ColIndex(), novZip5SkPn, fSpec.getNovZp5ParsePatternId(), null); 
		enableFieldSet(fSpec.isDecZp5Col(), fSpec.getDecZp5ColIndex(), decZip5SkPn, fSpec.getDecZp5ParsePatternId(), null); 
	
		enableFieldSet(fSpec.isC95Cfld1Col(), fSpec.getC95Cfld1ColIndex(), c95CF1SkPn, fSpec.getC95Cfld1ParsePatternId(), null); 
		enableFieldSet(fSpec.isC95Cfld2Col(), fSpec.getC95Cfld2ColIndex(), c95CF2SkPn, fSpec.getC95Cfld2ParsePatternId(), null); 
		enableFieldSet(fSpec.isC95Cfld3Col(), fSpec.getC95Cfld3ColIndex(), c95CF3SkPn, null, null); 
		enableFieldSet(fSpec.isC95Cfld4Col(), fSpec.getC95Cfld4ColIndex(), c95CF4SkPn, null, null); 
		enableFieldSet(fSpec.isC95Cfld5Col(), fSpec.getC95Cfld5ColIndex(), c95CF5SkPn, null, null); 
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
		DynamicIrs1095cFileSpecification fSpec = DataManager.i().mDynamicIrs1095cFileSpecification; 

		if(fld.getParsePatternId() != null) { fld.setTitle(fld.getTitle().replace(" (P:" + fld.getParsePatternId().toString() + ")", "")); fld.setParsePatternId(null); } 
		if(fld.getDateFormatId() != null) { fld.setTitle(fld.getTitle().replace(" (F:" + fld.getDateFormatId().toString() + ")", "")); fld.setDateFormatId(null); } 

		if(emprId.isSelected() == false){ fSpec.setErCol(false); fSpec.setErColIndex(0); }
		if(empFstNm.isSelected() == false){ fSpec.setfNameParsePattern(null); fSpec.setfNameCol(false); fSpec.setfNameColIndex(0);  
		   empFstNmParse.setFont(Font.font("Veranda", 12.0)); empFstNmParse.setText("+ Pattern"); } 
		if(empDOB.isSelected() == false){ fSpec.setDobCol(false); fSpec.setDobColIndex(0); fSpec.setDobFormat(null); 
		   empDOBFormat.setFont(Font.font("Veranda", 12.0)); empDOBFormat.setText("+ Format");} 
		if(emplJob.isSelected() == false){ fSpec.setJobTtlCol(false); fSpec.setJobTtlColIndex(0); } 
		if(emplStNm.isSelected() == false){ fSpec.setStreetNumCol(false); fSpec.setStreetNumColIndex(0); } 
		if(emplCity.isSelected() == false){ fSpec.setCityCol(false); fSpec.setCityColIndex(0); fSpec.setCityParsePattern(null); 
		   emplCityParse.setFont(Font.font("Veranda", 12.0)); emplCityParse.setText("+ Pattern"); } 
		if(emplId.isSelected() == false){ fSpec.setEtcRefCol(false); fSpec.setEtcRefColIndex(0); } 
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
		if(depStrtNm.isSelected() == false){ fSpec.setDepStreetNumCol(false); fSpec.setDepStreetNumColIndex(0); } 
		if(depState.isSelected() == false){ fSpec.setDepStateCol(false); fSpec.setDepStateColIndex(0); fSpec.setDepStateParsePattern(null); 
		   depStateParse.setFont(Font.font("Veranda", 12.0)); depStateParse.setText("+ Pattern"); } 
		if(depCF3.isSelected() == false){ fSpec.setDepCfld3Col(false); fSpec.setDepCfld3ColIndex(0); secCF3.setText(""); } 

		if(annOfferCode.isSelected() == false){ fSpec.setAnnOCdCol(false); fSpec.setAnnOCdColIndex(0); }
		if(annSafeHarbor.isSelected() == false){ fSpec.setAnnSHCdCol(false); fSpec.setAnnSHCdColIndex(0); }
		if(annSelfInsured.isSelected() == false){ fSpec.setAnnSelfCol(false); fSpec.setAnnSelfColIndex(0); fSpec.setAnnSelfTrueParsePattern(null); 
		   annSelfInsuredParse.setFont(Font.font("Veranda", 12.0)); annSelfInsuredParse.setText("+ Pattern"); } 
		if(annMemberShare.isSelected() == false){ fSpec.setAnnMbrShareCol(false); fSpec.setAnnMbrShareColIndex(0); }
		if(annOfferCode.isSelected() == false){ fSpec.setAnnOCdCol(false); fSpec.setAnnOCdColIndex(0); }
		if(covTxYr.isSelected() == false){ fSpec.setTyCol(false); fSpec.setTyColIndex(0); fSpec.setTyParsePattern(null); 
		   covTxYrParse.setFont(Font.font("Veranda", 12.0)); covTxYrParse.setText("+ Pattern"); }
		
		if(janOfferCode.isSelected() == false){ fSpec.setJanOCdCol(false); fSpec.setJanOCdColIndex(0); }
		if(febOfferCode.isSelected() == false){ fSpec.setFebOCdCol(false); fSpec.setFebOCdColIndex(0); }
		if(marOfferCode.isSelected() == false){ fSpec.setMarOCdCol(false); fSpec.setMarOCdColIndex(0); }
		if(aprOfferCode.isSelected() == false){ fSpec.setAprOCdCol(false); fSpec.setAprOCdColIndex(0); }
		if(mayOfferCode.isSelected() == false){ fSpec.setMayOCdCol(false); fSpec.setMayOCdColIndex(0); }
		if(junOfferCode.isSelected() == false){ fSpec.setJunOCdCol(false); fSpec.setJunOCdColIndex(0); }
		if(julOfferCode.isSelected() == false){ fSpec.setJulOCdCol(false); fSpec.setJulOCdColIndex(0); }
		if(augOfferCode.isSelected() == false){ fSpec.setAugOCdCol(false); fSpec.setAugOCdColIndex(0); }
		if(sepOfferCode.isSelected() == false){ fSpec.setSepOCdCol(false); fSpec.setSepOCdColIndex(0); }
		if(octOfferCode.isSelected() == false){ fSpec.setOctOCdCol(false); fSpec.setOctOCdColIndex(0); }
		if(novOfferCode.isSelected() == false){ fSpec.setNovOCdCol(false); fSpec.setNovOCdColIndex(0); }
		if(decOfferCode.isSelected() == false){ fSpec.setDecOCdCol(false); fSpec.setDecOCdColIndex(0); }

		if(janMemberShare.isSelected() == false){ fSpec.setJanMbrShareCol(false); fSpec.setJanMbrShareColIndex(0); }
		if(febMemberShare.isSelected() == false){ fSpec.setFebMbrShareCol(false); fSpec.setFebMbrShareColIndex(0); }
		if(marMemberShare.isSelected() == false){ fSpec.setMarMbrShareCol(false); fSpec.setMarMbrShareColIndex(0); }
		if(aprMemberShare.isSelected() == false){ fSpec.setAprMbrShareCol(false); fSpec.setAprMbrShareColIndex(0); }
		if(mayMemberShare.isSelected() == false){ fSpec.setMayMbrShareCol(false); fSpec.setMayMbrShareColIndex(0); }
		if(junMemberShare.isSelected() == false){ fSpec.setJunMbrShareCol(false); fSpec.setJunMbrShareColIndex(0); }
		if(julMemberShare.isSelected() == false){ fSpec.setJulMbrShareCol(false); fSpec.setJulMbrShareColIndex(0); }
		if(augMemberShare.isSelected() == false){ fSpec.setAugMbrShareCol(false); fSpec.setAugMbrShareColIndex(0); }
		if(sepMemberShare.isSelected() == false){ fSpec.setSepMbrShareCol(false); fSpec.setSepMbrShareColIndex(0); }
		if(octMemberShare.isSelected() == false){ fSpec.setOctMbrShareCol(false); fSpec.setOctMbrShareColIndex(0); }
		if(novMemberShare.isSelected() == false){ fSpec.setNovMbrShareCol(false); fSpec.setNovMbrShareColIndex(0); }
		if(decMemberShare.isSelected() == false){ fSpec.setDecMbrShareCol(false); fSpec.setDecMbrShareColIndex(0); }
		
		if(janSafeHarbor.isSelected() == false){ fSpec.setJanSHCdCol(false); fSpec.setJanSHCdColIndex(0); }
		if(febSafeHarbor.isSelected() == false){ fSpec.setFebSHCdCol(false); fSpec.setFebSHCdColIndex(0); }
		if(marSafeHarbor.isSelected() == false){ fSpec.setMarSHCdCol(false); fSpec.setMarSHCdColIndex(0); }
		if(aprSafeHarbor.isSelected() == false){ fSpec.setAprSHCdCol(false); fSpec.setAprSHCdColIndex(0); }
		if(maySafeHarbor.isSelected() == false){ fSpec.setMaySHCdCol(false); fSpec.setMaySHCdColIndex(0); }
		if(junSafeHarbor.isSelected() == false){ fSpec.setJunSHCdCol(false); fSpec.setJunSHCdColIndex(0); }
		if(julSafeHarbor.isSelected() == false){ fSpec.setJulSHCdCol(false); fSpec.setJulSHCdColIndex(0); }
		if(augSafeHarbor.isSelected() == false){ fSpec.setAugSHCdCol(false); fSpec.setAugSHCdColIndex(0); }
		if(sepSafeHarbor.isSelected() == false){ fSpec.setSepSHCdCol(false); fSpec.setSepSHCdColIndex(0); }
		if(octSafeHarbor.isSelected() == false){ fSpec.setOctSHCdCol(false); fSpec.setOctSHCdColIndex(0); }
		if(novSafeHarbor.isSelected() == false){ fSpec.setNovSHCdCol(false); fSpec.setNovSHCdColIndex(0); }
		if(decSafeHarbor.isSelected() == false){ fSpec.setDecSHCdCol(false); fSpec.setDecSHCdColIndex(0); }

		if(janSelfIns.isSelected() == false){ fSpec.setJanSelfCol(false); fSpec.setJanSelfColIndex(0); fSpec.setJanSelfTrueParsePattern(null); 
		   janSelfInsParse.setFont(Font.font("Veranda", 12.0)); janSelfInsParse.setText("+ Pattern"); } 
		if(febSelfIns.isSelected() == false){ fSpec.setFebSelfCol(false); fSpec.setFebSelfColIndex(0); fSpec.setFebSelfTrueParsePattern(null); 
		   febSelfInsParse.setFont(Font.font("Veranda", 12.0)); febSelfInsParse.setText("+ Pattern"); } 
		if(marSelfIns.isSelected() == false){ fSpec.setMarSelfCol(false); fSpec.setMarSelfColIndex(0); fSpec.setMarSelfTrueParsePattern(null); 
		   marSelfInsParse.setFont(Font.font("Veranda", 12.0)); marSelfInsParse.setText("+ Pattern"); } 
		if(aprSelfIns.isSelected() == false){ fSpec.setAprSelfCol(false); fSpec.setAprSelfColIndex(0); fSpec.setAprSelfTrueParsePattern(null); 
		   aprSelfInsParse.setFont(Font.font("Veranda", 12.0)); aprSelfInsParse.setText("+ Pattern"); } 
		if(maySelfIns.isSelected() == false){ fSpec.setMaySelfCol(false); fSpec.setMaySelfColIndex(0); fSpec.setMaySelfTrueParsePattern(null); 
		   maySelfInsParse.setFont(Font.font("Veranda", 12.0)); maySelfInsParse.setText("+ Pattern"); } 
		if(junSelfIns.isSelected() == false){ fSpec.setJunSelfCol(false); fSpec.setJunSelfColIndex(0); fSpec.setJunSelfTrueParsePattern(null); 
		   junSelfInsParse.setFont(Font.font("Veranda", 12.0)); junSelfInsParse.setText("+ Pattern"); } 
		if(julSelfIns.isSelected() == false){ fSpec.setJulSelfCol(false); fSpec.setJulSelfColIndex(0); fSpec.setJulSelfTrueParsePattern(null); 
		   julSelfInsParse.setFont(Font.font("Veranda", 12.0)); julSelfInsParse.setText("+ Pattern"); } 
		if(augSelfIns.isSelected() == false){ fSpec.setAugSelfCol(false); fSpec.setAugSelfColIndex(0); fSpec.setAugSelfTrueParsePattern(null); 
		   augSelfInsParse.setFont(Font.font("Veranda", 12.0)); augSelfInsParse.setText("+ Pattern"); } 
		if(sepSelfIns.isSelected() == false){ fSpec.setSepSelfCol(false); fSpec.setSepSelfColIndex(0); fSpec.setSepSelfTrueParsePattern(null); 
		   sepSelfInsParse.setFont(Font.font("Veranda", 12.0)); sepSelfInsParse.setText("+ Pattern"); } 
		if(octSelfIns.isSelected() == false){ fSpec.setOctSelfCol(false); fSpec.setOctSelfColIndex(0); fSpec.setOctSelfTrueParsePattern(null); 
		   octSelfInsParse.setFont(Font.font("Veranda", 12.0)); octSelfInsParse.setText("+ Pattern"); } 
		if(novSelfIns.isSelected() == false){ fSpec.setNovSelfCol(false); fSpec.setNovSelfColIndex(0); fSpec.setNovSelfTrueParsePattern(null); 
		   novSelfInsParse.setFont(Font.font("Veranda", 12.0)); novSelfInsParse.setText("+ Pattern"); } 
		if(decSelfIns.isSelected() == false){ fSpec.setDecSelfCol(false); fSpec.setDecSelfColIndex(0); fSpec.setDecSelfTrueParsePattern(null); 
		   decSelfInsParse.setFont(Font.font("Veranda", 12.0)); decSelfInsParse.setText("+ Pattern"); } 

		if(janZip5.isSelected() == false){ fSpec.setJanZp5Col(false); fSpec.setJanZp5ColIndex(0); fSpec.setJanZp5ParsePattern(null); 
		   janZip5Parse.setFont(Font.font("Veranda", 12.0)); janZip5Parse.setText("+ Pattern"); } 
		if(febZip5.isSelected() == false){ fSpec.setFebZp5Col(false); fSpec.setFebZp5ColIndex(0); fSpec.setFebZp5ParsePattern(null); 
		   febZip5Parse.setFont(Font.font("Veranda", 12.0)); febZip5Parse.setText("+ Pattern"); } 
		if(marZip5.isSelected() == false){ fSpec.setMarZp5Col(false); fSpec.setMarZp5ColIndex(0); fSpec.setMarZp5ParsePattern(null); 
		   marZip5Parse.setFont(Font.font("Veranda", 12.0)); marZip5Parse.setText("+ Pattern"); } 
		if(aprZip5.isSelected() == false){ fSpec.setAprZp5Col(false); fSpec.setAprZp5ColIndex(0); fSpec.setAprZp5ParsePattern(null); 
		   aprZip5Parse.setFont(Font.font("Veranda", 12.0)); aprZip5Parse.setText("+ Pattern"); } 
		if(mayZip5.isSelected() == false){ fSpec.setMayZp5Col(false); fSpec.setMayZp5ColIndex(0); fSpec.setMayZp5ParsePattern(null); 
		   mayZip5Parse.setFont(Font.font("Veranda", 12.0)); mayZip5Parse.setText("+ Pattern"); } 
		if(junZip5.isSelected() == false){ fSpec.setJunZp5Col(false); fSpec.setJunZp5ColIndex(0); fSpec.setJunZp5ParsePattern(null); 
		   junZip5Parse.setFont(Font.font("Veranda", 12.0)); junZip5Parse.setText("+ Pattern"); } 
		if(julZip5.isSelected() == false){ fSpec.setJulZp5Col(false); fSpec.setJulZp5ColIndex(0); fSpec.setJulZp5ParsePattern(null); 
		   julZip5Parse.setFont(Font.font("Veranda", 12.0)); julZip5Parse.setText("+ Pattern"); } 
		if(augZip5.isSelected() == false){ fSpec.setAugZp5Col(false); fSpec.setAugZp5ColIndex(0); fSpec.setAugZp5ParsePattern(null); 
		   augZip5Parse.setFont(Font.font("Veranda", 12.0)); augZip5Parse.setText("+ Pattern"); } 
		if(sepZip5.isSelected() == false){ fSpec.setSepZp5Col(false); fSpec.setSepZp5ColIndex(0); fSpec.setSepZp5ParsePattern(null); 
		   sepZip5Parse.setFont(Font.font("Veranda", 12.0)); sepZip5Parse.setText("+ Pattern"); } 
		if(octZip5.isSelected() == false){ fSpec.setOctZp5Col(false); fSpec.setOctZp5ColIndex(0); fSpec.setOctZp5ParsePattern(null); 
		   octZip5Parse.setFont(Font.font("Veranda", 12.0)); octZip5Parse.setText("+ Pattern"); } 
		if(novZip5.isSelected() == false){ fSpec.setNovZp5Col(false); fSpec.setNovZp5ColIndex(0); fSpec.setNovZp5ParsePattern(null); 
		   novZip5Parse.setFont(Font.font("Veranda", 12.0)); novZip5Parse.setText("+ Pattern"); } 
		if(decZip5.isSelected() == false){ fSpec.setDecZp5Col(false); fSpec.setDecZp5ColIndex(0); fSpec.setDecZp5ParsePattern(null); 
		   decZip5Parse.setFont(Font.font("Veranda", 12.0)); decZip5Parse.setText("+ Pattern"); } 
		
		if(c95CF1.isSelected() == false){ fSpec.setC95Cfld1Col(false); fSpec.setC95Cfld1ColIndex(0); fSpec.setC95Cfld1ParsePattern(null);
		   c95CF1Parse.setFont(Font.font("Veranda", 12.0)); c95CF1Parse.setText("+ Pattern"); c95CF1.setText(""); } 
		if(c95CF2.isSelected() == false){ fSpec.setC95Cfld2Col(false); fSpec.setC95Cfld2ColIndex(0); fSpec.setC95Cfld2ParsePattern(null);
		   c95CF2Parse.setFont(Font.font("Veranda", 12.0)); c95CF2Parse.setText("+ Pattern"); c95CF2.setText(""); } 
		if(c95CF3.isSelected() == false){ fSpec.setC95Cfld3Col(false); fSpec.setC95Cfld3ColIndex(0); c95CF3.setText(""); } 
		if(c95CF4.isSelected() == false){ fSpec.setC95Cfld4Col(false); fSpec.setC95Cfld4ColIndex(0); c95CF4.setText(""); } 
		if(c95CF5.isSelected() == false){ fSpec.setC95Cfld5Col(false); fSpec.setC95Cfld5ColIndex(0); c95CF5.setText(""); } 
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
		DynamicIrs1095cFileSpecification fSpec = DataManager.i().mDynamicIrs1095cFileSpecification;
 
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
		if(c95CF1 != null){ fSpec.setC95Cfld1Name(c95CF1.getText().toString()); } 
		if(c95CF2 != null){ fSpec.setC95Cfld2Name(c95CF2.getText().toString()); } 
		if(c95CF3 != null){ fSpec.setC95Cfld3Name(c95CF3.getText().toString()); } 
		if(c95CF4 != null){ fSpec.setC95Cfld4Name(c95CF4.getText().toString()); } 
		if(c95CF5 != null){ fSpec.setC95Cfld5Name(c95CF5.getText().toString()); } 
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
		dfLockedCheck.setDisable(readOnly); 
		dfcovMapEEtoERCheck.setDisable(readOnly); 
		dfcovClearFormButton.setDisable(readOnly); 
		dfcovSaveChangesButton.setDisable(readOnly); 
	} 

	private int getIrs1095cFileGenderTypeRefCount() 
	{ 
		int locRefSize = 0; 
 
		try {
			DynamicIrs1095cFileGenderReferenceRequest request = new DynamicIrs1095cFileGenderReferenceRequest(DataManager.i().mDynamicIrs1095cFileGenderReference); 
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicIrs1095cFileSpecification.setGenderReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  
			DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { 
	    	DataManager.i().logGenericException(e); }
		
		for(DynamicIrs1095cFileGenderReference gendRef: DataManager.i().mDynamicIrs1095cFileSpecification.getGenderReferences()) 
		{ 
			if(gendRef.isActive() == true && gendRef.isDeleted() == false) 
				locRefSize++; 
		} 
		return locRefSize; 
	} 
 
	private int getIrs1095cFileEmployerRefCount() 
	{ 
		int locRefSize = 0; 

		try {
			DynamicIrs1095cFileEmployerReferenceRequest request = new DynamicIrs1095cFileEmployerReferenceRequest(DataManager.i().mDynamicIrs1095cFileEmployerReference);
			request.setSpecificationId(DataManager.i().mPipelineSpecification.getDynamicFileSpecificationId()); 
			DataManager.i().mDynamicIrs1095cFileSpecification.setErReferences(AdminPersistenceManager.getInstance().getAll(request));
		} catch (CoreException e) {  
			DataManager.i().log(Level.SEVERE, e); }
	    catch (Exception e) { 
	    	DataManager.i().logGenericException(e); }
		
		for(DynamicIrs1095cFileEmployerReference erRef : DataManager.i().mDynamicIrs1095cFileSpecification.getErReferences()) 
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
		updateIrs1095cFile(); 
		EtcAdmin.i().setProgress(0);
	} 

	private void updateIrs1095cFile() 
	{ 
		if(DataManager.i().mDynamicIrs1095cFileSpecification != null) 
		{ 
			DynamicIrs1095cFileSpecification fSpec = DataManager.i().mDynamicIrs1095cFileSpecification; 
			DynamicIrs1095cFileSpecificationRequest request = new DynamicIrs1095cFileSpecificationRequest(); 

			//GATHER THE FIELDS 
			fSpec.setName(dfcovNameLabel.getText()); 
			fSpec.setTabIndex(Integer.valueOf(dfcovTabIndexLabel.getText())); 
			fSpec.setHeaderRowIndex(Integer.valueOf(dfcovHeaderRowIndexLabel.getText())); 
			fSpec.setMapEEtoER(dfcovMapEEtoERCheck.isSelected()); 
			fSpec.setCreateEmployee(dfcovCreateEmployeeCheck.isSelected()); 
			fSpec.setSkipLastRow(dfcovSkipLastRowCheck.isSelected()); 
			fSpec.setLocked(dfLockedCheck.isSelected()); 

			// EMPLOYEE 
	     	// EMPLOYER IDENTIFIER 
			fSpec.setErCol(Boolean.valueOf(emprId.isSelected()));
			fSpec.setErColIndex(Integer.valueOf(emprIdIdx.getText() != null && !emprIdIdx.getText().isEmpty() ? emprIdIdx.getText() : "0")); 

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

			// ANNUAL OFFER CODE 
			fSpec.setAnnOCdCol(Boolean.valueOf(annOfferCode.isSelected())); 
			fSpec.setAnnOCdColIndex(Integer.valueOf(annOfferCodeIdx.getText() != null && !annOfferCodeIdx.getText().isEmpty() ? annOfferCodeIdx.getText() : "0")); 

			// ANNUAL SAFE HARBOR CODE 
			fSpec.setAnnSHCdCol(Boolean.valueOf(annSafeHarbor.isSelected())); 
			fSpec.setAnnSHCdColIndex(Integer.valueOf(annSafeHarborIdx.getText() != null && !annSafeHarborIdx.getText().isEmpty() ? annSafeHarborIdx.getText() : "0")); 

			// ANNUAL SELF INSURED 
			fSpec.setAnnSelfCol(Boolean.valueOf(annSelfInsured.isSelected())); 
			fSpec.setAnnSelfColIndex(Integer.valueOf(annSelfInsuredIdx.getText() != null && !annSelfInsuredIdx.getText().isEmpty() ? annSelfInsuredIdx.getText() : "0")); 
			if(annSelfInsuredParse.getId() != null && Utils.isInt(annSelfInsuredParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(annSelfInsuredParse.getId())); 
				request.setAnnSelfTrueParsePatternRequest(req); 
			} else request.setClearAnnSelfTrueParsePatternId(true); 

			// ANNUAL MEMBER SHARE
			fSpec.setAnnMbrShareCol(Boolean.valueOf(annMemberShare.isSelected())); 
			fSpec.setAnnMbrShareColIndex(Integer.valueOf(annMemberShareIdx.getText() != null && !annMemberShareIdx.getText().isEmpty() ? annMemberShareIdx.getText() : "0")); 

			// ANNUAL ZIP 5
			fSpec.setAnnZp5Col(Boolean.valueOf(annZip5.isSelected())); 
			fSpec.setAnnZp5ColIndex(Integer.valueOf(annZip5Idx.getText() != null && !annZip5.getText().isEmpty() ? annZip5.getText() : "0")); 
			if(annZp5Parse.getId() != null && Utils.isInt(annZp5Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(annZp5Parse.getId())); 
				request.setAnnZp5ParsePatternRequest(req); 
			} else request.setClearAnnZp5ParsePatternId(true); 

			// TAX YEAR 
			fSpec.setTyCol(Boolean.valueOf(covTxYr.isSelected())); 
			fSpec.setTyColIndex(Integer.valueOf(covTxYrIdx.getText() != null && !covTxYrIdx.getText().isEmpty() ? covTxYrIdx.getText() : "0")); 
			if(covTxYrParse.getId() != null && Utils.isInt(covTxYrParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(covTxYrParse.getId())); 
				request.setTyParsePatternRequest(req); 
			} else request.setClearTyParsePatternId(true); 

			// OFFER CODE
			// JANUARY OFFER CODE
			fSpec.setJanOCdCol(Boolean.valueOf(janOfferCode.isSelected())); 
			fSpec.setJanOCdColIndex(Integer.valueOf(janOfferCodeIdx.getText() != null && !janOfferCodeIdx.getText().isEmpty() ? janOfferCodeIdx.getText() : "0")); 

			// FEBRUARY OFFER CODE
			fSpec.setFebOCdCol(Boolean.valueOf(febOfferCode.isSelected())); 
			fSpec.setFebOCdColIndex(Integer.valueOf(febOfferCodeIdx.getText() != null && !febOfferCodeIdx.getText().isEmpty() ? febOfferCodeIdx.getText() : "0")); 

			// MARCH OFFER CODE
			fSpec.setMarOCdCol(Boolean.valueOf(marOfferCode.isSelected())); 
			fSpec.setMarOCdColIndex(Integer.valueOf(marOfferCodeIdx.getText() != null && !marOfferCodeIdx.getText().isEmpty() ? marOfferCodeIdx.getText() : "0")); 

			// APRIL OFFER CODE
			fSpec.setAprOCdCol(Boolean.valueOf(aprOfferCode.isSelected())); 
			fSpec.setAprOCdColIndex(Integer.valueOf(aprOfferCodeIdx.getText() != null && !aprOfferCodeIdx.getText().isEmpty() ? aprOfferCodeIdx.getText() : "0")); 

			// MAY OFFER CODE
			fSpec.setMayOCdCol(Boolean.valueOf(mayOfferCode.isSelected())); 
			fSpec.setMayOCdColIndex(Integer.valueOf(mayOfferCodeIdx.getText() != null && !mayOfferCodeIdx.getText().isEmpty() ? mayOfferCodeIdx.getText() : "0")); 

			// JUNE OFFER CODE
			fSpec.setJunOCdCol(Boolean.valueOf(junOfferCode.isSelected())); 
			fSpec.setJunOCdColIndex(Integer.valueOf(junOfferCodeIdx.getText() != null && !junOfferCodeIdx.getText().isEmpty() ? junOfferCodeIdx.getText() : "0")); 

			// JULY OFFER CODE
			fSpec.setJulOCdCol(Boolean.valueOf(julOfferCode.isSelected())); 
			fSpec.setJulOCdColIndex(Integer.valueOf(julOfferCodeIdx.getText() != null && !julOfferCodeIdx.getText().isEmpty() ? julOfferCodeIdx.getText() : "0")); 

			// AUGUST OFFER CODE
			fSpec.setAugOCdCol(Boolean.valueOf(augOfferCode.isSelected())); 
			fSpec.setAugOCdColIndex(Integer.valueOf(augOfferCodeIdx.getText() != null && !augOfferCodeIdx.getText().isEmpty() ? augOfferCodeIdx.getText() : "0")); 

			// SEPTEMBER OFFER CODE
			fSpec.setSepOCdCol(Boolean.valueOf(sepOfferCode.isSelected())); 
			fSpec.setSepOCdColIndex(Integer.valueOf(sepOfferCodeIdx.getText() != null && !sepOfferCodeIdx.getText().isEmpty() ? sepOfferCodeIdx.getText() : "0")); 

			// OCTOBER OFFER CODE
			fSpec.setOctOCdCol(Boolean.valueOf(octOfferCode.isSelected())); 
			fSpec.setOctOCdColIndex(Integer.valueOf(octOfferCodeIdx.getText() != null && !octOfferCodeIdx.getText().isEmpty() ? octOfferCodeIdx.getText() : "0")); 

			// NOVEMBER OFFER CODE
			fSpec.setNovOCdCol(Boolean.valueOf(novOfferCode.isSelected())); 
			fSpec.setNovOCdColIndex(Integer.valueOf(novOfferCodeIdx.getText() != null && !novOfferCodeIdx.getText().isEmpty() ? novOfferCodeIdx.getText() : "0")); 

			// DECEMBER OFFER CODE
			fSpec.setDecOCdCol(Boolean.valueOf(decOfferCode.isSelected())); 
			fSpec.setDecOCdColIndex(Integer.valueOf(decOfferCodeIdx.getText() != null && !decOfferCodeIdx.getText().isEmpty() ? decOfferCodeIdx.getText() : "0")); 

			// JANUARY MEMBER SHARE
			fSpec.setJanSHCdCol(Boolean.valueOf(janMemberShare.isSelected())); 
			fSpec.setJanSHCdColIndex(Integer.valueOf(janMemberShareIdx.getText() != null && !janMemberShareIdx.getText().isEmpty() ? janMemberShareIdx.getText() : "0")); 

			// FEBRUARY MEMBER SHARE
			fSpec.setFebSHCdCol(Boolean.valueOf(febMemberShare.isSelected())); 
			fSpec.setFebSHCdColIndex(Integer.valueOf(febMemberShareIdx.getText() != null && !febMemberShareIdx.getText().isEmpty() ? febMemberShareIdx.getText() : "0")); 

			// MARCH MEMBER SHARE
			fSpec.setMarSHCdCol(Boolean.valueOf(marMemberShare.isSelected())); 
			fSpec.setMarSHCdColIndex(Integer.valueOf(marMemberShareIdx.getText() != null && !marMemberShareIdx.getText().isEmpty() ? marMemberShareIdx.getText() : "0")); 

			// APRIL MEMBER SHARE
			fSpec.setAprSHCdCol(Boolean.valueOf(aprMemberShare.isSelected())); 
			fSpec.setAprSHCdColIndex(Integer.valueOf(aprMemberShareIdx.getText() != null && !aprMemberShareIdx.getText().isEmpty() ? aprMemberShareIdx.getText() : "0")); 

			// MAY MEMBER SHARE
			fSpec.setMaySHCdCol(Boolean.valueOf(mayMemberShare.isSelected())); 
			fSpec.setMaySHCdColIndex(Integer.valueOf(mayMemberShareIdx.getText() != null && !mayMemberShareIdx.getText().isEmpty() ? mayMemberShareIdx.getText() : "0")); 

			// JUNE MEMBER SHARE
			fSpec.setJunSHCdCol(Boolean.valueOf(junMemberShare.isSelected())); 
			fSpec.setJunSHCdColIndex(Integer.valueOf(junMemberShareIdx.getText() != null && !junMemberShareIdx.getText().isEmpty() ? junMemberShareIdx.getText() : "0")); 

			// JULY MEMBER SHARE
			fSpec.setJulSHCdCol(Boolean.valueOf(julMemberShare.isSelected())); 
			fSpec.setJulSHCdColIndex(Integer.valueOf(julMemberShareIdx.getText() != null && !julMemberShareIdx.getText().isEmpty() ? julMemberShareIdx.getText() : "0")); 

			// AUGUST MEMBER SHARE
			fSpec.setAugSHCdCol(Boolean.valueOf(augMemberShare.isSelected())); 
			fSpec.setAugSHCdColIndex(Integer.valueOf(augMemberShareIdx.getText() != null && !augMemberShareIdx.getText().isEmpty() ? augMemberShareIdx.getText() : "0")); 

			// SEPTEMBER MEMBER SHARE
			fSpec.setSepSHCdCol(Boolean.valueOf(sepMemberShare.isSelected())); 
			fSpec.setSepSHCdColIndex(Integer.valueOf(sepMemberShareIdx.getText() != null && !sepMemberShareIdx.getText().isEmpty() ? sepMemberShareIdx.getText() : "0")); 

			// OCTOBER MEMBER SHARE
			fSpec.setOctSHCdCol(Boolean.valueOf(octMemberShare.isSelected())); 
			fSpec.setOctSHCdColIndex(Integer.valueOf(octMemberShareIdx.getText() != null && !octMemberShareIdx.getText().isEmpty() ? octMemberShareIdx.getText() : "0")); 

			// NOVEMBER MEMBER SHARE
			fSpec.setNovSHCdCol(Boolean.valueOf(novMemberShare.isSelected())); 
			fSpec.setNovSHCdColIndex(Integer.valueOf(novMemberShareIdx.getText() != null && !novMemberShareIdx.getText().isEmpty() ? novMemberShareIdx.getText() : "0")); 

			// DECEMBER MEMBER SHARE
			fSpec.setDecSHCdCol(Boolean.valueOf(decMemberShare.isSelected())); 
			fSpec.setDecSHCdColIndex(Integer.valueOf(decMemberShareIdx.getText() != null && !decMemberShareIdx.getText().isEmpty() ? decMemberShareIdx.getText() : "0")); 

			// SAFE HARBOR CODE
			// JANUARY SAFE HARBOR CODE
			fSpec.setJanMbrShareCol(Boolean.valueOf(janSafeHarbor.isSelected())); 
			fSpec.setJanMbrShareColIndex(Integer.valueOf(janSafeHarborIdx.getText() != null && !janSafeHarborIdx.getText().isEmpty() ? janSafeHarborIdx.getText() : "0")); 

			// FEBRUARY SAFE HARBOR CODE
			fSpec.setFebMbrShareCol(Boolean.valueOf(febSafeHarbor.isSelected())); 
			fSpec.setFebMbrShareColIndex(Integer.valueOf(febSafeHarborIdx.getText() != null && !febSafeHarborIdx.getText().isEmpty() ? febSafeHarborIdx.getText() : "0")); 

			// MARCH SAFE HARBOR CODE
			fSpec.setMarMbrShareCol(Boolean.valueOf(marSafeHarbor.isSelected())); 
			fSpec.setMarMbrShareColIndex(Integer.valueOf(marSafeHarborIdx.getText() != null && !marSafeHarborIdx.getText().isEmpty() ? marSafeHarborIdx.getText() : "0")); 

			// APRIL SAFE HARBOR CODE
			fSpec.setAprMbrShareCol(Boolean.valueOf(aprSafeHarbor.isSelected())); 
			fSpec.setAprMbrShareColIndex(Integer.valueOf(aprSafeHarborIdx.getText() != null && !aprSafeHarborIdx.getText().isEmpty() ? aprSafeHarborIdx.getText() : "0")); 

			// MAY SAFE HARBOR CODE
			fSpec.setMayMbrShareCol(Boolean.valueOf(maySafeHarbor.isSelected())); 
			fSpec.setMayMbrShareColIndex(Integer.valueOf(maySafeHarborIdx.getText() != null && !maySafeHarborIdx.getText().isEmpty() ? maySafeHarborIdx.getText() : "0")); 

			// JUNE SAFE HARBOR CODE
			fSpec.setJunMbrShareCol(Boolean.valueOf(junSafeHarbor.isSelected())); 
			fSpec.setJunMbrShareColIndex(Integer.valueOf(junSafeHarborIdx.getText() != null && !junSafeHarborIdx.getText().isEmpty() ? junSafeHarborIdx.getText() : "0")); 

			// JULY SAFE HARBOR CODE
			fSpec.setJulMbrShareCol(Boolean.valueOf(julSafeHarbor.isSelected())); 
			fSpec.setJulMbrShareColIndex(Integer.valueOf(julSafeHarborIdx.getText() != null && !julSafeHarborIdx.getText().isEmpty() ? julSafeHarborIdx.getText() : "0")); 

			// AUGUST SAFE HARBOR CODE
			fSpec.setAugMbrShareCol(Boolean.valueOf(augSafeHarbor.isSelected())); 
			fSpec.setAugMbrShareColIndex(Integer.valueOf(augSafeHarborIdx.getText() != null && !augSafeHarborIdx.getText().isEmpty() ? augSafeHarborIdx.getText() : "0")); 

			// SEPTEMBER SAFE HARBOR CODE
			fSpec.setSepMbrShareCol(Boolean.valueOf(sepSafeHarbor.isSelected())); 
			fSpec.setSepMbrShareColIndex(Integer.valueOf(sepSafeHarborIdx.getText() != null && !sepSafeHarborIdx.getText().isEmpty() ? sepSafeHarborIdx.getText() : "0")); 

			// OCTOBER SAFE HARBOR CODE
			fSpec.setOctMbrShareCol(Boolean.valueOf(octSafeHarbor.isSelected())); 
			fSpec.setOctMbrShareColIndex(Integer.valueOf(octSafeHarborIdx.getText() != null && !octSafeHarborIdx.getText().isEmpty() ? octSafeHarborIdx.getText() : "0")); 

			// NOVEMBER SAFE HARBOR CODE
			fSpec.setNovMbrShareCol(Boolean.valueOf(novSafeHarbor.isSelected())); 
			fSpec.setNovMbrShareColIndex(Integer.valueOf(novSafeHarborIdx.getText() != null && !novSafeHarborIdx.getText().isEmpty() ? novSafeHarborIdx.getText() : "0")); 

			// DECEMBER SAFE HARBOR CODE
			fSpec.setDecMbrShareCol(Boolean.valueOf(decSafeHarbor.isSelected())); 
			fSpec.setDecMbrShareColIndex(Integer.valueOf(decSafeHarborIdx.getText() != null && !decSafeHarborIdx.getText().isEmpty() ? decSafeHarborIdx.getText() : "0")); 

			// SELF INSURED
			// JANUARY SELF INSURED 
			fSpec.setJanSelfCol(Boolean.valueOf(janSelfIns.isSelected())); 
			fSpec.setJanSelfColIndex(Integer.valueOf(janSelfInsIdx.getText() != null && !janSelfInsIdx.getText().isEmpty() ? janSelfInsIdx.getText() : "0")); 
			if(janSelfInsParse.getId() != null && Utils.isInt(janSelfInsParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(janSelfInsParse.getId())); 
				request.setJanSelfTrueParsePatternRequest(req); 
			} else request.setClearJanSelfTrueParsePatternId(true); 

			// FEBRUARY SELF INSURED 
			fSpec.setFebSelfCol(Boolean.valueOf(febSelfIns.isSelected())); 
			fSpec.setFebSelfColIndex(Integer.valueOf(febSelfInsIdx.getText() != null && !febSelfInsIdx.getText().isEmpty() ? febSelfInsIdx.getText() : "0")); 
			if(febSelfInsIdx.getId() != null && Utils.isInt(febSelfInsParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(janSelfInsParse.getId())); 
				request.setFebSelfTrueParsePatternRequest(req); 
			} else request.setClearFebSelfTrueParsePatternId(true); 

			// MARCH SELF INSURED 
			fSpec.setMarSelfCol(Boolean.valueOf(marSelfIns.isSelected())); 
			fSpec.setMarSelfColIndex(Integer.valueOf(marSelfInsIdx.getText() != null && !marSelfInsIdx.getText().isEmpty() ? marSelfInsIdx.getText() : "0")); 
			if(marSelfInsParse.getId() != null && Utils.isInt(marSelfInsParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(marSelfInsParse.getId())); 
				request.setMarSelfTrueParsePatternRequest(req); 
			} else request.setClearMarSelfTrueParsePatternId(true); 

			// APRIL SELF INSURED 
			fSpec.setAprSelfCol(Boolean.valueOf(aprSelfIns.isSelected())); 
			fSpec.setAprSelfColIndex(Integer.valueOf(aprSelfInsIdx.getText() != null && !aprSelfInsIdx.getText().isEmpty() ? aprSelfInsIdx.getText() : "0")); 
			if(aprSelfInsParse.getId() != null && Utils.isInt(aprSelfInsParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(aprSelfInsParse.getId())); 
				request.setAprSelfTrueParsePatternRequest(req); 
			} else request.setClearAprSelfTrueParsePatternId(true); 

			// MAY SELF INSURED 
			fSpec.setMaySelfCol(Boolean.valueOf(maySelfIns.isSelected())); 
			fSpec.setMaySelfColIndex(Integer.valueOf(maySelfInsIdx.getText() != null && !maySelfInsIdx.getText().isEmpty() ? maySelfInsIdx.getText() : "0")); 
			if(maySelfInsParse.getId() != null && Utils.isInt(maySelfInsParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(maySelfInsParse.getId())); 
				request.setMaySelfTrueParsePatternRequest(req); 
			} else request.setClearMaySelfTrueParsePatternId(true); 

			// JUNE SELF INSURED 
			fSpec.setJunSelfCol(Boolean.valueOf(junSelfIns.isSelected())); 
			fSpec.setJunSelfColIndex(Integer.valueOf(junSelfInsIdx.getText() != null && !junSelfInsIdx.getText().isEmpty() ? junSelfInsIdx.getText() : "0")); 
			if(junSelfInsParse.getId() != null && Utils.isInt(junSelfInsParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(junSelfInsParse.getId())); 
				request.setJunSelfTrueParsePatternRequest(req); 
			} else request.setClearJunSelfTrueParsePatternId(true); 

			// JULY SELF INSURED 
			fSpec.setJulSelfCol(Boolean.valueOf(julSelfIns.isSelected())); 
			fSpec.setJulSelfColIndex(Integer.valueOf(julSelfInsIdx.getText() != null && !julSelfInsIdx.getText().isEmpty() ? julSelfInsIdx.getText() : "0")); 
			if(julSelfInsParse.getId() != null && Utils.isInt(julSelfInsParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(julSelfInsParse.getId())); 
				request.setJulSelfTrueParsePatternRequest(req); 
			} else request.setClearJulSelfTrueParsePatternId(true); 

			// AUGUST SELF INSURED 
			fSpec.setAugSelfCol(Boolean.valueOf(augSelfIns.isSelected())); 
			fSpec.setAugSelfColIndex(Integer.valueOf(augSelfInsIdx.getText() != null && !augSelfInsIdx.getText().isEmpty() ? augSelfInsIdx.getText() : "0")); 
			if(augSelfInsParse.getId() != null && Utils.isInt(augSelfInsParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(augSelfInsParse.getId())); 
				request.setAugSelfTrueParsePatternRequest(req); 
			} else request.setClearAugSelfTrueParsePatternId(true); 

			// SEPTEMBER SELF INSURED 
			fSpec.setSepSelfCol(Boolean.valueOf(sepSelfIns.isSelected())); 
			fSpec.setSepSelfColIndex(Integer.valueOf(sepSelfInsIdx.getText() != null && !sepSelfInsIdx.getText().isEmpty() ? sepSelfInsIdx.getText() : "0")); 
			if(sepSelfInsParse.getId() != null && Utils.isInt(sepSelfInsParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(sepSelfInsParse.getId())); 
				request.setSepSelfTrueParsePatternRequest(req); 
			} else request.setClearSepSelfTrueParsePatternId(true); 

			// OCTOBER SELF INSURED 
			fSpec.setOctSelfCol(Boolean.valueOf(octSelfIns.isSelected())); 
			fSpec.setOctSelfColIndex(Integer.valueOf(octSelfInsIdx.getText() != null && !octSelfInsIdx.getText().isEmpty() ? octSelfInsIdx.getText() : "0")); 
			if(octSelfInsParse.getId() != null && Utils.isInt(octSelfInsParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(octSelfInsParse.getId())); 
				request.setOctSelfTrueParsePatternRequest(req); 
			} else request.setClearOctSelfTrueParsePatternId(true); 

			// NOVEMBER SELF INSURED 
			fSpec.setNovSelfCol(Boolean.valueOf(novSelfIns.isSelected())); 
			fSpec.setNovSelfColIndex(Integer.valueOf(novSelfInsIdx.getText() != null && !novSelfInsIdx.getText().isEmpty() ? novSelfInsIdx.getText() : "0")); 
			if(novSelfInsParse.getId() != null && Utils.isInt(novSelfInsParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(novSelfInsParse.getId())); 
				request.setNovSelfTrueParsePatternRequest(req); 
			} else request.setClearNovSelfTrueParsePatternId(true); 

			// DECEMBER SELF INSURED 
			fSpec.setDecSelfCol(Boolean.valueOf(decSelfIns.isSelected())); 
			fSpec.setDecSelfColIndex(Integer.valueOf(decSelfInsIdx.getText() != null && !decSelfInsIdx.getText().isEmpty() ? decSelfInsIdx.getText() : "0")); 
			if(decSelfInsParse.getId() != null && Utils.isInt(decSelfInsParse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(decSelfInsParse.getId())); 
				request.setDecSelfTrueParsePatternRequest(req); 
			} else request.setClearDecSelfTrueParsePatternId(true); 

			// ZIP 5
			// JANUARY ZIP 5 
			fSpec.setJanZp5Col(Boolean.valueOf(janZip5.isSelected())); 
			fSpec.setJanZp5ColIndex(Integer.valueOf(janZip5Idx.getText() != null && !janZip5Idx.getText().isEmpty() ? janZip5Idx.getText() : "0")); 
			if(janZip5Parse.getId() != null && Utils.isInt(janZip5Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(janZip5Parse.getId())); 
				request.setJanZp5ParsePatternRequest(req); 
			} else request.setClearJanZp5ParsePatternId(true); 

			// FEBRUARY ZIP 5 
			fSpec.setFebZp5Col(Boolean.valueOf(febZip5.isSelected())); 
			fSpec.setFebZp5ColIndex(Integer.valueOf(febZip5Idx.getText() != null && !febZip5Idx.getText().isEmpty() ? febZip5Idx.getText() : "0")); 
			if(febZip5Idx.getId() != null && Utils.isInt(febZip5Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(janZip5Parse.getId())); 
				request.setFebZp5ParsePatternRequest(req); 
			} else request.setClearFebZp5ParsePatternId(true); 

			// MARCH ZIP 5 
			fSpec.setMarZp5Col(Boolean.valueOf(marZip5.isSelected())); 
			fSpec.setMarZp5ColIndex(Integer.valueOf(marZip5Idx.getText() != null && !marZip5Idx.getText().isEmpty() ? marZip5Idx.getText() : "0")); 
			if(marZip5Parse.getId() != null && Utils.isInt(marZip5Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(marZip5Parse.getId())); 
				request.setMarZp5ParsePatternRequest(req); 
			} else request.setClearMarZp5ParsePatternId(true); 

			// APRIL ZIP 5 
			fSpec.setAprZp5Col(Boolean.valueOf(aprZip5.isSelected())); 
			fSpec.setAprZp5ColIndex(Integer.valueOf(aprZip5Idx.getText() != null && !aprZip5Idx.getText().isEmpty() ? aprZip5Idx.getText() : "0")); 
			if(aprZip5Parse.getId() != null && Utils.isInt(aprZip5Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(aprZip5Parse.getId())); 
				request.setAprZp5ParsePatternRequest(req); 
			} else request.setClearAprZp5ParsePatternId(true); 

			// MAY ZIP 5 
			fSpec.setMayZp5Col(Boolean.valueOf(mayZip5.isSelected())); 
			fSpec.setMayZp5ColIndex(Integer.valueOf(mayZip5Idx.getText() != null && !mayZip5Idx.getText().isEmpty() ? mayZip5Idx.getText() : "0")); 
			if(mayZip5Parse.getId() != null && Utils.isInt(mayZip5Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(mayZip5Parse.getId())); 
				request.setMayZp5ParsePatternRequest(req); 
			} else request.setClearMayZp5ParsePatternId(true); 

			// JUNE ZIP 5 
			fSpec.setJunZp5Col(Boolean.valueOf(junZip5.isSelected())); 
			fSpec.setJunZp5ColIndex(Integer.valueOf(junZip5Idx.getText() != null && !junZip5Idx.getText().isEmpty() ? junZip5Idx.getText() : "0")); 
			if(junZip5Parse.getId() != null && Utils.isInt(junZip5Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(junZip5Parse.getId())); 
				request.setJunZp5ParsePatternRequest(req); 
			} else request.setClearJunZp5ParsePatternId(true); 

			// JULY ZIP 5 
			fSpec.setJulZp5Col(Boolean.valueOf(julZip5.isSelected())); 
			fSpec.setJulZp5ColIndex(Integer.valueOf(julZip5Idx.getText() != null && !julZip5Idx.getText().isEmpty() ? julZip5Idx.getText() : "0")); 
			if(julZip5Parse.getId() != null && Utils.isInt(julZip5Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(julZip5Parse.getId())); 
				request.setJulZp5ParsePatternRequest(req); 
			} else request.setClearJulZp5ParsePatternId(true); 

			// AUGUST ZIP 5 
			fSpec.setAugZp5Col(Boolean.valueOf(augZip5.isSelected())); 
			fSpec.setAugZp5ColIndex(Integer.valueOf(augZip5Idx.getText() != null && !augZip5Idx.getText().isEmpty() ? augZip5Idx.getText() : "0")); 
			if(augZip5Parse.getId() != null && Utils.isInt(augZip5Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(augZip5Parse.getId())); 
				request.setAugZp5ParsePatternRequest(req); 
			} else request.setClearAugZp5ParsePatternId(true); 

			// SEPTEMBER ZIP 5 
			fSpec.setSepZp5Col(Boolean.valueOf(sepZip5.isSelected())); 
			fSpec.setSepZp5ColIndex(Integer.valueOf(sepZip5Idx.getText() != null && !sepZip5Idx.getText().isEmpty() ? sepZip5Idx.getText() : "0")); 
			if(sepZip5Parse.getId() != null && Utils.isInt(sepZip5Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(sepZip5Parse.getId())); 
				request.setSepZp5ParsePatternRequest(req); 
			} else request.setClearSepZp5ParsePatternId(true); 

			// OCTOBER ZIP 5 
			fSpec.setOctZp5Col(Boolean.valueOf(octZip5.isSelected())); 
			fSpec.setOctZp5ColIndex(Integer.valueOf(octZip5Idx.getText() != null && !octZip5Idx.getText().isEmpty() ? octZip5Idx.getText() : "0")); 
			if(octZip5Parse.getId() != null && Utils.isInt(octZip5Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(octZip5Parse.getId())); 
				request.setOctZp5ParsePatternRequest(req); 
			} else request.setClearOctZp5ParsePatternId(true); 

			// NOVEMBER ZIP 5 
			fSpec.setNovZp5Col(Boolean.valueOf(novZip5.isSelected())); 
			fSpec.setNovZp5ColIndex(Integer.valueOf(novZip5Idx.getText() != null && !novZip5Idx.getText().isEmpty() ? novZip5Idx.getText() : "0")); 
			if(novZip5Parse.getId() != null && Utils.isInt(novZip5Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(novZip5Parse.getId())); 
				request.setNovZp5ParsePatternRequest(req); 
			} else request.setClearNovZp5ParsePatternId(true); 

			// DECEMBER ZIP 5 
			fSpec.setDecZp5Col(Boolean.valueOf(decZip5.isSelected())); 
			fSpec.setDecZp5ColIndex(Integer.valueOf(decZip5Idx.getText() != null && !decZip5Idx.getText().isEmpty() ? decZip5Idx.getText() : "0")); 
			if(decZip5Parse.getId() != null && Utils.isInt(decZip5Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(decZip5Parse.getId())); 
				request.setDecZp5ParsePatternRequest(req); 
			} else request.setClearDecZp5ParsePatternId(true); 

			// C95 CUSTOM FIELDS
	     	// C95 CUSTOM FIELD 1 
			fSpec.setC95Cfld1Name(c95CField1.getText() != null && !c95CField1.getText().isEmpty() ? c95CField1.getText() : ""); 
			fSpec.setC95Cfld1Col(Boolean.valueOf(c95CF1.isSelected())); 
			fSpec.setC95Cfld1ColIndex(Integer.valueOf(c95CF1Idx.getText() != null && !c95CF1Idx.getText().isEmpty() ? c95CF1Idx.getText() : "0")); 
			if(c95CF1Parse.getId() != null && Utils.isInt(c95CF1Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(c95CF1Parse.getId())); 
				request.setC95Cfld1ParsePatternRequest(req); 
			} else request.setClearC95Cfld1ParsePatternId(true); 

	     	// C95 CUSTOM FIELD 2 
			fSpec.setC95Cfld2Name(c95CField2.getText() != null && !c95CField2.getText().isEmpty() ? c95CField2.getText() : ""); 
			fSpec.setC95Cfld2Col(Boolean.valueOf(c95CF2.isSelected())); 
			fSpec.setC95Cfld2ColIndex(Integer.valueOf(c95CF2Idx.getText() != null && !c95CF2Idx.getText().isEmpty() ? c95CF2Idx.getText() : "0")); 
			if(c95CF2Parse.getId() != null && Utils.isInt(c95CF2Parse.getId()))  
			{ 
				PipelineParsePatternRequest req = new PipelineParsePatternRequest(); 
				req.setId(Long.valueOf(c95CF2Parse.getId())); 
				request.setC95Cfld2ParsePatternRequest(req); 
			} else request.setClearC95Cfld2ParsePatternId(true); 

	     	// C95 CUSTOM FIELD 3 
			fSpec.setC95Cfld3Name(c95CField3.getText() != null && !c95CField3.getText().isEmpty() ? c95CField3.getText() : ""); 
			fSpec.setC95Cfld3Col(Boolean.valueOf(c95CF3.isSelected())); 
			fSpec.setC95Cfld3ColIndex(Integer.valueOf(c95CF3Idx.getText() != null && !c95CF3Idx.getText().isEmpty() ? c95CF3Idx.getText() : "0")); 

	     	// C95 CUSTOM FIELD 4 
			fSpec.setC95Cfld4Name(c95CField4.getText() != null && !c95CField4.getText().isEmpty() ? c95CField4.getText() : ""); 
			fSpec.setC95Cfld4Col(Boolean.valueOf(c95CF4.isSelected())); 
			fSpec.setC95Cfld4ColIndex(Integer.valueOf(c95CF4Idx.getText() != null && !c95CF4Idx.getText().isEmpty() ? c95CF4Idx.getText() : "0")); 

	     	// C95 CUSTOM FIELD 5 
			fSpec.setC95Cfld5Name(c95CField5.getText() != null && !c95CField5.getText().isEmpty() ? c95CField5.getText() : ""); 
			fSpec.setC95Cfld5Col(Boolean.valueOf(c95CF5.isSelected())); 
			fSpec.setC95Cfld5ColIndex(Integer.valueOf(c95CF5Idx.getText() != null && !c95CF5Idx.getText().isEmpty() ? c95CF5Idx.getText() : "0")); 

			// set the request entity
			request.setEntity(fSpec);
			request.setId(fSpec.getId()); 

			// update the server
			try {
				fSpec = AdminPersistenceManager.getInstance().addOrUpdate(request);
			} catch (CoreException e) {  
				DataManager.i().log(Level.SEVERE, e); }
		    catch (Exception e) { 
		    	DataManager.i().logGenericException(e); }

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
//		viewIgnoreRow(); 
	}	 
 
	@FXML 
	private void onRemoveIgnoreSpec(ActionEvent event)  
	{ 
		//verify that they want to remove the spec 
//		Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION); 
//	    confirmAlert.setTitle("Remove Ignore Row Specification"); 
//	    confirmAlert.setContentText("Are You Sure You Want to remove the selected Ignore row specification?"); 
//	    Optional<ButtonType> result = confirmAlert.showAndWait(); 
//	    if((result.isPresent()) &&(result.get() != ButtonType.OK)) { return; } 
	}	 
 
	@FXML 
	private void onAddPayPeriodRules(ActionEvent event) 
	{ 
		//reset the current rule, if any 
//		DataManager.i().mDynamicPayFilePayPeriodRule = null; 
//		viewPayPeriodRules(); 
	}	 
 
	@FXML 
	private void onAddAdditionalHours(ActionEvent event)  
	{ 
//		DataManager.i().mDynamicPayFileAdditionalHoursSpecification = null; 
//		viewAdditionalHours(); 
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
	        stage.onCloseRequestProperty().setValue(e -> setEmpRefCount(getIrs1095cFileEmployerRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setEmpRefCount(getIrs1095cFileEmployerRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true) 
				setEmpRefCount(getIrs1095cFileEmployerRefCount()); 
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
	        stage.onCloseRequestProperty().setValue(e -> setGendRefCount(getIrs1095cFileGenderTypeRefCount())); 
	        stage.onHiddenProperty().setValue(e -> setGendRefCount(getIrs1095cFileGenderTypeRefCount())); 
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait(); 
 
	        if(refController.changesMade == true)  
				setGendRefCount(getIrs1095cFileGenderTypeRefCount()); 
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
	 
	public void setPatternValue(MapperField fld) 
	{		 
		if(empFstNmParse.getText().contains("+ Pattern")){ empFstNmParse.setId("empFstNmParse"); } 
		if(empDOBFormat.getText().contains("+ Format")){ empDOBFormat.setId("empDOBFormat"); } 
		if(emplCityParse.getText().contains("+ Pattern")){ emplCityParse.setId("emplCityParse"); } 
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
		if(depCityParse.getText().contains("+ Pattern")){ depCityParse.setId("depCityParse"); } 
		if(depDOBFormat.getText().contains("+ Format")){ depDOBFormat.setId("depDOBFormat"); } 
		if(depLstNmParse.getText().contains("+ Pattern")){ depLstNmParse.setId("depLstNmParse"); } 
		if(depAdLn1Parse.getText().contains("+ Pattern")){ depAdLn1Parse.setId("depAdLn1Parse"); } 
		if(depStateParse.getText().contains("+ Pattern")){ depStateParse.setId("depStateParse"); } 
		if(depCF1Parse.getText().contains("+ Pattern")){ depCF1Parse.setId("depCF1Parse"); } 
		if(depCF2Parse.getText().contains("+ Pattern")){ depCF2Parse.setId("depCF2Parse"); } 
		if(annSelfInsuredParse.getText().contains("+ Pattern")){ annSelfInsuredParse.setId("annSelfInsuredParse"); } 
		if(annZp5Parse.getText().contains("+ Pattern")){ annZp5Parse.setId("annZp5Parse"); } 
		if(covTxYrParse.getText().contains("+ Pattern")){ covTxYrParse.setId("covTxYrParse"); } 
		if(janSelfInsParse.getText().contains("+ Pattern")){ janSelfInsParse.setId("janSelfInsParse"); } 
		if(febSelfInsParse.getText().contains("+ Pattern")){ febSelfInsParse.setId("febSelfInsParse"); } 
		if(marSelfInsParse.getText().contains("+ Pattern")){ marSelfInsParse.setId("marSelfInsParse"); } 
		if(aprSelfInsParse.getText().contains("+ Pattern")){ aprSelfInsParse.setId("aprSelfInsParse"); } 
		if(maySelfInsParse.getText().contains("+ Pattern")){ maySelfInsParse.setId("maySelfInsParse"); } 
		if(junSelfInsParse.getText().contains("+ Pattern")){ junSelfInsParse.setId("junSelfInsParse"); } 
		if(julSelfInsParse.getText().contains("+ Pattern")){ julSelfInsParse.setId("julSelfInsParse"); } 
		if(augSelfInsParse.getText().contains("+ Pattern")){ augSelfInsParse.setId("augSelfInsParse"); } 
		if(sepSelfInsParse.getText().contains("+ Pattern")){ sepSelfInsParse.setId("sepSelfInsParse"); } 
		if(octSelfInsParse.getText().contains("+ Pattern")){ octSelfInsParse.setId("octSelfInsParse"); } 
		if(novSelfInsParse.getText().contains("+ Pattern")){ novSelfInsParse.setId("novSelfInsParse"); } 
		if(decSelfInsParse.getText().contains("+ Pattern")){ decSelfInsParse.setId("decSelfInsParse"); } 
		if(janZip5Parse.getText().contains("+ Pattern")){ janZip5Parse.setId("janZip5Parse"); } 
		if(febZip5Parse.getText().contains("+ Pattern")){ febZip5Parse.setId("febZip5Parse"); } 
		if(marZip5Parse.getText().contains("+ Pattern")){ marZip5Parse.setId("marZip5Parse"); } 
		if(aprZip5Parse.getText().contains("+ Pattern")){ aprZip5Parse.setId("aprZip5Parse"); } 
		if(mayZip5Parse.getText().contains("+ Pattern")){ mayZip5Parse.setId("mayZip5Parse"); } 
		if(junZip5Parse.getText().contains("+ Pattern")){ junZip5Parse.setId("junZip5Parse"); } 
		if(julZip5Parse.getText().contains("+ Pattern")){ julZip5Parse.setId("julZip5Parse"); } 
		if(augZip5Parse.getText().contains("+ Pattern")){ augZip5Parse.setId("augZip5Parse"); } 
		if(sepZip5Parse.getText().contains("+ Pattern")){ sepZip5Parse.setId("sepZip5Parse"); } 
		if(octZip5Parse.getText().contains("+ Pattern")){ octZip5Parse.setId("octZip5Parse"); } 
		if(novZip5Parse.getText().contains("+ Pattern")){ novZip5Parse.setId("novZip5Parse"); } 
		if(decZip5Parse.getText().contains("+ Pattern")){ decZip5Parse.setId("decZip5Parse"); } 
		if(c95CF1Parse.getText().contains("+ Pattern")){ c95CF1Parse.setId("c95CF1Parse"); } 
		if(c95CF2Parse.getText().contains("+ Pattern")){ c95CF2Parse.setId("c95CF2Parse"); } 
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
				empFstNm.setSelected(false);  
				empDOB.setSelected(false); 
				emplJob.setSelected(false); 
				emplStNm.setSelected(false); 
				emplCity.setSelected(false); 
				emplId.setSelected(false); 
				emplMNm.setSelected(false); 
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
				annOfferCode.setSelected(false); 
				annSafeHarbor.setSelected(false); 
				annSelfInsured.setSelected(false); 
				annMemberShare.setSelected(false); 
				annZip5.setSelected(false); 
				covTxYr.setSelected(false); 
				janOfferCode.setSelected(false); 
				febOfferCode.setSelected(false); 
				marOfferCode.setSelected(false); 
				aprOfferCode.setSelected(false); 
				mayOfferCode.setSelected(false); 
				junOfferCode.setSelected(false); 
				julOfferCode.setSelected(false); 
				augOfferCode.setSelected(false); 
				sepOfferCode.setSelected(false); 
				octOfferCode.setSelected(false); 
				novOfferCode.setSelected(false); 
				decOfferCode.setSelected(false); 
				janMemberShare.setSelected(false); 
				febMemberShare.setSelected(false); 
				marMemberShare.setSelected(false); 
				aprMemberShare.setSelected(false); 
				mayMemberShare.setSelected(false); 
				junMemberShare.setSelected(false); 
				julMemberShare.setSelected(false); 
				augMemberShare.setSelected(false); 
				sepMemberShare.setSelected(false); 
				octMemberShare.setSelected(false); 
				novMemberShare.setSelected(false); 
				decMemberShare.setSelected(false); 
				janSafeHarbor.setSelected(false); 
				febSafeHarbor.setSelected(false); 
				marSafeHarbor.setSelected(false); 
				aprSafeHarbor.setSelected(false); 
				maySafeHarbor.setSelected(false); 
				junSafeHarbor.setSelected(false); 
				julSafeHarbor.setSelected(false); 
				augSafeHarbor.setSelected(false); 
				sepSafeHarbor.setSelected(false); 
				octSafeHarbor.setSelected(false); 
				novSafeHarbor.setSelected(false); 
				decSafeHarbor.setSelected(false); 
				janSafeHarbor.setSelected(false); 
				febSelfIns.setSelected(false); 
				marSelfIns.setSelected(false); 
				aprSelfIns.setSelected(false); 
				maySelfIns.setSelected(false); 
				junSelfIns.setSelected(false); 
				julSelfIns.setSelected(false); 
				augSelfIns.setSelected(false); 
				sepSelfIns.setSelected(false); 
				octSelfIns.setSelected(false); 
				novSelfIns.setSelected(false); 
				decSelfIns.setSelected(false); 
				janZip5.setSelected(false); 
				febZip5.setSelected(false); 
				marZip5.setSelected(false); 
				aprZip5.setSelected(false); 
				mayZip5.setSelected(false); 
				junZip5.setSelected(false); 
				julZip5.setSelected(false); 
				augZip5.setSelected(false); 
				sepZip5.setSelected(false); 
				octZip5.setSelected(false); 
				novZip5.setSelected(false); 
				decZip5.setSelected(false); 
				
				dfcovNameLabel.setText(""); 
				dfcovTabIndexLabel.setText("0"); 
				dfcovHeaderRowIndexLabel.setText("0"); 
				dfcovMapEEtoERCheck.setSelected(false); 
				dfcovCreateEmployeeCheck.setSelected(false); 
				dfcovSkipLastRowCheck.setSelected(false); 
				dfLockedCheck.setSelected(false); 
 
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
				c95CField1.setText(""); 
				c95CField2.setText(""); 
				c95CField3.setText(""); 
				c95CField4.setText(""); 
				c95CField5.setText(""); 
				emprRefButton.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY))); 
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
		DynamicIrs1095cFileSpecification fSpec = DataManager.i().mDynamicIrs1095cFileSpecification; 
		 
		if(dfcovMapEEtoERCheck.isSelected() == true) { fSpec.setMapEEtoER(true); } 
		else { fSpec.setMapEEtoER(false); } 
 
		if(dfcovCreateEmployeeCheck.isSelected() == true) { fSpec.setCreateEmployee(true); } 
		else { fSpec.setCreateEmployee(false); } 
 
		if(dfcovSkipLastRowCheck.isSelected() == true) { fSpec.setSkipLastRow(true); } 
		else { fSpec.setSkipLastRow(false); } 
 
		if(dfLockedCheck.isSelected() == true) { fSpec.setLocked(true); } 
		else { fSpec.setLocked(false); } 
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
	private void onDepEmprIdParse(ActionEvent event) 	 
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
	private void onDepCF2Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onDepStateParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 

	@FXML 
	private void onAnnSelfInsuredParse(ActionEvent event) 	 
	{
		setParseButtonValue(event);  
	} 

	@FXML 
	private void onAnnZp5Parse(ActionEvent event) 	 
	{
		setParseButtonValue(event);  
	} 

	@FXML 
	private void onCovTxYrParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onJanSelfInsParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onFebSelfInsParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onMarSelfInsParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onAprSelfInsParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onMaySelfInsParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onJunSelfInsParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onJulSelfInsParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onAugSelfInsParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onSepSelfInsParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onOctSelfInsParse(ActionEvent event)
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onNovSelfInsParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
	 
	@FXML 
	private void onDecSelfInsParse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onJanZip5Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onFebZip5Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onMarZip5Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onAprZip5Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onMayZip5Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onJunZip5Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onJulZip5Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onAugZip5Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onSepZip5Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onOctZip5Parse(ActionEvent event)
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onNovZip5Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
	 
	@FXML 
	private void onDecZip5Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
	 
	@FXML 
	private void onC95CF1Parse(ActionEvent event) 	 
	{  
		setParseButtonValue(event);  
	} 
 
	@FXML 
	private void onC95CF2Parse(ActionEvent event) 	 
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