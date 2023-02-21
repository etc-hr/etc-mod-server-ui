package com.etc.admin.data; 
 
import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.etc.CoreException;
import com.etc.admin.EmsApp;
import com.etc.admin.EtcAdmin;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.ui.calc.CalcQueue;
import com.etc.admin.ui.pipeline.queue.PipelineQueue;
import com.etc.admin.utils.CacheType;
import com.etc.admin.utils.Logging;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.LogType;
import com.etc.admin.utils.Utils.RawDetailType;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.api.WsManager;
import com.etc.corvetto.CorvettoConnection;
import com.etc.corvetto.ems.calc.entities.CalculationChannel;
import com.etc.corvetto.ems.calc.entities.CalculationNotice;
import com.etc.corvetto.ems.calc.entities.CalculationRequest;
import com.etc.corvetto.ems.calc.entities.CalculationSpecification;
import com.etc.corvetto.ems.calc.entities.CalculationStepHandler;
import com.etc.corvetto.ems.calc.entities.aca.Irs10945bCalculation;
import com.etc.corvetto.ems.calc.entities.aca.Irs10945cCalculation;
import com.etc.corvetto.ems.pipeline.embeds.PipelineInformation;
import com.etc.corvetto.ems.pipeline.entities.PipelineChannel;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileNotice;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileRecordRejection;
import com.etc.corvetto.ems.pipeline.entities.PipelineFileStepHandler;
import com.etc.corvetto.ems.pipeline.entities.PipelineParseDateFormat;
import com.etc.corvetto.ems.pipeline.entities.PipelineParsePattern;
import com.etc.corvetto.ems.pipeline.entities.PipelineRequest;
import com.etc.corvetto.ems.pipeline.entities.PipelineSpecification;
import com.etc.corvetto.ems.pipeline.entities.RawConversionFailure;
import com.etc.corvetto.ems.pipeline.entities.RawEmployee;
import com.etc.corvetto.ems.pipeline.entities.RawInvalidation;
import com.etc.corvetto.ems.pipeline.entities.RawIrs1094c;
import com.etc.corvetto.ems.pipeline.entities.RawIrs1095c;
import com.etc.corvetto.ems.pipeline.entities.RawIrs1095cCI;
import com.etc.corvetto.ems.pipeline.entities.RawNotice;
import com.etc.corvetto.ems.pipeline.entities.RawPay;
import com.etc.corvetto.ems.pipeline.entities.airerr.AirTranErrorFile;
import com.etc.corvetto.ems.pipeline.entities.airerr.RawAirTranError;
import com.etc.corvetto.ems.pipeline.entities.airrct.AirTranReceiptFile;
import com.etc.corvetto.ems.pipeline.entities.airrct.RawAirTranReceipt;
import com.etc.corvetto.ems.pipeline.entities.ben.BenefitFile;
import com.etc.corvetto.ems.pipeline.entities.ben.DynamicBenefitFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.c94.Irs1094cFile;
import com.etc.corvetto.ems.pipeline.entities.c95.DynamicIrs1095cFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.c95.DynamicIrs1095cFileGenderReference;
import com.etc.corvetto.ems.pipeline.entities.c95.DynamicIrs1095cFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.c95.Irs1095cFile;
import com.etc.corvetto.ems.pipeline.entities.cov.CoverageFile;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileCoverageGroupReference;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileGenderReference;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.cov.DynamicCoverageFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.ded.DeductionFile;
import com.etc.corvetto.ems.pipeline.entities.ded.DynamicDeductionFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.ded.DynamicDeductionFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.ded.DynamicDeductionFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileCoverageGroupReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileDepartmentReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileGenderReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileIgnoreRowSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileLocationReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFilePayCodeReference;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.emp.DynamicEmployeeFileUnionTypeReference;
import com.etc.corvetto.ems.pipeline.entities.emp.EmployeeFile;
import com.etc.corvetto.ems.pipeline.entities.ins.DynamicInsuranceFileEmployerReference;
import com.etc.corvetto.ems.pipeline.entities.ins.DynamicInsuranceFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.ins.InsuranceFile;
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
import com.etc.corvetto.ems.pipeline.entities.pay.PayConversionCalculation;
import com.etc.corvetto.ems.pipeline.entities.pay.PayConversionCalculationReference;
import com.etc.corvetto.ems.pipeline.entities.pay.PayFile;
import com.etc.corvetto.ems.pipeline.entities.ppd.DynamicPayPeriodFileSpecification;
import com.etc.corvetto.ems.pipeline.entities.ppd.PayPeriodFile;
import com.etc.corvetto.ems.pipeline.rqs.PipelineChannelRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineFileStepHandlerRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineParseDateFormatRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineParsePatternRequest;
import com.etc.corvetto.ems.pipeline.rqs.PipelineSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.c95.DynamicIrs1095cFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.cov.DynamicCoverageFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.ded.DynamicDeductionFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.emp.DynamicEmployeeFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.ins.DynamicInsuranceFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.DynamicPayFileSpecificationRequest;
import com.etc.corvetto.ems.pipeline.rqs.pay.PayConversionCalculationRequest;
import com.etc.corvetto.ems.pipeline.rqs.ppd.DynamicPayPeriodFileSpecificationRequest;
import com.etc.corvetto.entities.Account;
import com.etc.corvetto.entities.AccountContact;
import com.etc.corvetto.entities.AirError;
import com.etc.corvetto.entities.AirFilingEvent;
import com.etc.corvetto.entities.AirTransmission;
import com.etc.corvetto.entities.Benefit;
import com.etc.corvetto.entities.BenefitSponsor;
import com.etc.corvetto.entities.Carrier;
import com.etc.corvetto.entities.Contact;
import com.etc.corvetto.entities.Coverage;
import com.etc.corvetto.entities.CoverageGroup;
import com.etc.corvetto.entities.CoverageGroupMembership;
import com.etc.corvetto.entities.Department;
import com.etc.corvetto.entities.Dependent;
import com.etc.corvetto.entities.Document;
import com.etc.corvetto.entities.DocumentType;
import com.etc.corvetto.entities.EligibilityPeriod;
import com.etc.corvetto.entities.Employee;
import com.etc.corvetto.entities.Employer;
import com.etc.corvetto.entities.EmployerContact;
import com.etc.corvetto.entities.EmployerIntegrationInformation;
import com.etc.corvetto.entities.EmploymentPeriod;
import com.etc.corvetto.entities.Folder;
import com.etc.corvetto.entities.Irs1094Filing;
import com.etc.corvetto.entities.Irs1094Submission;
import com.etc.corvetto.entities.Irs1094b;
import com.etc.corvetto.entities.Irs1094c;
import com.etc.corvetto.entities.Irs1095Filing;
import com.etc.corvetto.entities.Irs1095Submission;
import com.etc.corvetto.entities.Irs1095b;
import com.etc.corvetto.entities.Irs1095bCI;
import com.etc.corvetto.entities.Irs1095c;
import com.etc.corvetto.entities.Irs1095cCI;
import com.etc.corvetto.entities.Location;
import com.etc.corvetto.entities.Note;
import com.etc.corvetto.entities.Organization;
import com.etc.corvetto.entities.Pay;
import com.etc.corvetto.entities.PayClass;
import com.etc.corvetto.entities.PayPeriod;
import com.etc.corvetto.entities.Person;
import com.etc.corvetto.entities.PlanYear;
import com.etc.corvetto.entities.PlanYearOffering;
import com.etc.corvetto.entities.PlanYearOfferingBenefit;
import com.etc.corvetto.entities.TaxMonth;
import com.etc.corvetto.entities.TaxYear;
import com.etc.corvetto.entities.TaxYearServiceLevel;
import com.etc.corvetto.entities.Upload;
import com.etc.corvetto.entities.UploadHandler;
import com.etc.corvetto.entities.UploadType;
import com.etc.corvetto.entities.UploadTypeTemplate;
import com.etc.corvetto.entities.User;
import com.etc.corvetto.rqs.AccountRequest;
import com.etc.corvetto.rqs.CorvettoRequest;
import com.etc.corvetto.rqs.EmployerContactRequest;
import com.etc.corvetto.rqs.EmployerRequest;
import com.etc.corvetto.rqs.FolderRequest;
import com.etc.corvetto.rqs.TaxYearServiceLevelRequest;
import com.etc.corvetto.rqs.UploadRequest;
import com.etc.corvetto.rqs.UserRequest;
import com.etc.corvetto.utils.types.HealthCoverageOriginType;
import com.etc.entities.CoreData;
import com.etc.entities.DataProperty;
import com.etc.utils.sec.KeyStore;

import javafx.concurrent.Task; 
//import com.etc.utils.crypto.Cryptographer; 
//import com.etc.utils.crypto.CryptographyException; 
//import com.etc.utils.crypto.EncryptedSSN; 
public class DataManager 
{ 
	///////////////////////////////////////////// 
	// Index tracking members 
	///////////////////////////////////////////// 
	public int mAccountIndex = 0; 
	public int mCoverageFileIndex = 0; 
	public int mDynamicCoverageFileSpecificationIndex = 0; 
	public int mDynamicEmployeeFileSpecificationIndex = 0; 
	public int mDynamicPayFileSpecificationIndex = 0; 
	public int mEmployerIndex = 0; 
	public int mEmployeeIndex = 0; 
	public int mEmployeeFileIndex = 0; 
	public int mSecondaryIndex = 0; 
	public int mParsePatternIndex = 0; 
	public int mParseDateFormatIndex = 0; 
	public int mPayFileIndex = 0;	 
	 
	///////////////////////////////////////////// 
	// standard data members  
	///////////////////////////////////////////// 
	public CoreData mCoreData = null;
	public Account mAccount = null; 
	public AccountContact mAccountContact = null; 
	public DataProperty mDataProperty = null;
	public Properties mProperty = null;
	public Department mDepartment = null;
	public Location mLocation = null; 
	public Employee mEmployee = null; 
	public EmployeeFile mEmployeeFile = null; 
	public Coverage mCoverage = null; 
	public CoverageFile mCoverageFile = null; 
	public Contact mContact = null; 
	public Pay mPay = null; 
	public PayFile mPayFile = null;
	public Person mPerson = null;
	public Employer mEmployer = null; 
	public EmployerContact mEmployerContact = null; 
	public EmploymentPeriod mEmploymentPeriod = null; 
	public Organization mOrg = null; 
	public Dependent mDependent = null; 
	public PayPeriod mPayPeriod = null; 
	public TaxMonth mTaxMonth = null; 
	public TaxYear mTaxYear = null; 
	public Note mNote = null;
	public User mAccountUser = null; 
	public User mLocalUser = null; 
	public User mUser = null;
	public File mFile = null;
	public EmployerIntegrationInformation mEmployerIntegrationInformation = null;
	 
	///////////////////////////////////////////// 
	// Operational Logic Flags 
	///////////////////////////////////////////// 
	public Boolean mEmployeeUpdated = false;
	
	///////////////////////////////////////////// 
	// tax filing members 
	///////////////////////////////////////////// 
	public AirError mAirError = null; 
	public CalculationNotice mCalculationNotice = null; 
	public AirTransmission mAirTransmission = null; 
	public Irs1094b mIrs1094b = null; 
	public Irs1094c mIrs1094c = null; 
	public Irs1095b mIrs1095b = null; 
	public Irs1095bCI mIrs1095bCI = null; 
	public Irs1095c mIrs1095c = null; 
	public Irs1095cCI mIrs1095cCI= null; 
	public Irs1094Filing mIrs1094Filing = null; 
	public Irs1094Submission mIrs1094Submission = null; 
	public Irs1095Filing mIrs1095Filing = null; 
	public Irs1095Submission mIrs1095Submission = null; 
	 
	public List<AirError> mAirErrors = null; 
	public List<AirFilingEvent> mAirFilingEvents = null; 
	public List<AirTransmission> mAirTransmissions = null; 
	public List<Irs1094b> mIrs1094bs = null; 
	public List<Irs1094c> mIrs1094cs = null; 
	public List<Irs1094Filing> mIrs1094Filings = null; 
	public List<Irs1094Submission> mIrs1094Submissions = null; 
	public List<Irs1095b> mIrs1095bs = null; 
	public List<Irs1095bCI> mIrs1095bCIs = null; 
	public List<Irs1095c> mIrs1095cs = null; 
	public List<Irs1095cCI> mIrs1095cCIs = null; 
	public List<Irs1095Submission> mIrs1095Submissions = null; 
	public List<Irs1095Filing> mIrs1095Filings = null; 
	public List<HealthCoverageOriginType> mHealthCoverageOriginTypes = null; 
 
	///////////////////////////////////////////// 
	// data list members 
	///////////////////////////////////////////// 
	public List<Account> mAccounts = new ArrayList<Account>(); 
	public List<AccountContact> mAccountContacts = null; 
	public List<Employee> mEmployees = null; 
	public List<Employer> mEmployers = null; 
	public List<Contact> mContacts = null; 
	public List<PayPeriod> mPayPeriods = null; 
	public List<CoverageGroup> mCoverageClassesList= null; 
	public List<Organization> mOrganizations = null; 
	public List<Department> mDepartments = null; 
	public List<Location> mLocations = null; 
	public List<Employer> mOrgEmployers = null; 
	public List<EmployerContact> mEmployerContacts = null; 
	public List<Dependent> mDependents = null; 
	public List<PlanYearOffering> mPlanYearOfferings = null; 
	public List<TaxYear> mTaxYears = null;
	public List<PlanYear> mPlanYears = null;
	public List<Person> mPersons = null;
	public List<User> mUsers = null; 
	public List<Note> mNotes = null; 
	public List<TaxYearServiceLevel> mTaxYearServiceLevels = null; 
	 
	///////////////////////////////////////////// 
	// lookup members 
	///////////////////////////////////////////// 
	public List<Account> mLUAccounts = null; 
	public List<Employee> mLUEmployees = null; 
	public List<Employee> mSearchEmployees = null; 
	public List<Employer> mEmployersList = null; 
	public List<Dependent> mLUDependents = null; 
	public List<User> mLUUsers = null; 
	 
	///////////////////////////////////////////// 
	// Logging and Debugging 
	///////////////////////////////////////////// 
	public List<Logging> mLogs = null; 
	private Logger logr;
	private int resetCounter = 0;
	 
	//TESTING 
	public List<String> mEmployeeNameList = null; 
 
	///////////////////////////////////////////// 
	// pipeline members 
	///////////////////////////////////////////// 
	public AirTranErrorFile mAirTranErrorFile = null; 
	public AirTranReceiptFile mAirTranReceiptFile = null; 
	public CoverageFile mPipelineCoverageFile = null; 
	public DeductionFile mDeductionFile = null; 
	public Document mPipelineDocument; 
	public EmployeeFile mPipelineEmployeeFile = null; 
	public InsuranceFile mInsuranceFile = null; 
	public Irs1094cFile mIrs1094cFile = null; 
	public Irs1095cFile mIrs1095cFile = null; 
	public PayFile mPipelinePayFile = null; 
	public PayPeriodFile mPayPeriodFile = null; 
	public PipelineChannel mPipelineChannel = null; 
	public PipelineFileRecordRejection mPipelineFileRecordRejection = null; 
	public PipelineFileStepHandler mPipelineFileStepHandler = null; 
	public PipelineInformation mPipelineInformation; 
	public PipelineParseDateFormat mPipelineParseDateFormat = null; 
	public PipelineParsePattern mPipelineParsePattern = null; 
	public PipelineRequest mPipelineRequest = null; 
	public PipelineSpecification mPipelineSpecification = null; 
	public PipelineSpecification mPipelineCoverageSpecification = null; 
	public PipelineSpecification mPipelineEmployeeSpecification = null; 
	public BenefitFile mPipelinePlanFile = null; 
	public PlanYear mPlanYear = null; 
	public RawEmployee mRawEmployee; 
	//public RawCoverageEmployee mRawCoverageEmployee; 
	public RawPay mRawPay; 
	public DocumentType mDocumentType;
	public UploadType mUploadType;
	public UploadTypeTemplate mUploadTypeTemplate; 
	public CoverageGroupMembership mEmployeeCoverageGroupMembership = null; 
	 
	public List<AirTranErrorFile> mAirTranErrorFiles = null; 
	public List<AirTranReceiptFile> mAirTranReceiptFiles = null; 
	public List<CoverageFile> mPipelineCoverageFiles = null; 
	public List<Document> mPipelineDocuments = null; 
	public List<DocumentType> mDocumentTypes = null;
	public List<UploadHandler> mUploadHandlers = null;
	public List<EmployeeFile> mPipelineEmployeeFiles = null; 
	public List<Irs1094cFile> mIrs1094cFiles = null; 
	public List<Irs1095cFile> mIrs1095cFiles = null; 
	public List<PayFile> mPipelinePayFiles = null; 
	public List<DeductionFile> mPipelineDeductionFiles = null; 
	public List<InsuranceFile> mPipelineInsuranceFiles = null; 
	public List<PayPeriodFile> mPayPeriodFiles = null; 
	public List<PipelineChannel> mPipelineChannels = null; 
	public List<PipelineFileNotice> mPipelineFileNotices = null; 
	public List<PipelineFileRecordRejection> mPipelineFileRecordRejections = null; 
	public List<PipelineFileStepHandler> mPipelineFileStepHandlers = null; 
	public List<PipelineFileStepHandler> mLUPipelineFileStepHandlers = null; 
	public List<PipelineParseDateFormat> mPipelineParseDateFormats = null; 
	public List<PipelineParseDateFormat> mLUPipelineParseDateFormats = null; 
	public List<PipelineParsePattern> mPipelineParsePatterns = null; 
	public List<PipelineParsePattern> mLUPipelineParsePatterns = null; 
	public List<PipelineRequest> mPipelineRequests = null; 
	public List<PipelineRequest> mLUPipelineRequests = null; 
	public List<PipelineSpecification> mPipelineSpecifications = null; 
	public List<PipelineSpecification> mLUPipelineSpecifications = null; 
	public List<PipelineSpecification> mPipelineCoverageSpecifications = null; 
	public List<PipelineSpecification> mPipelineEmployeeSpecifications = null; 
	public List<BenefitFile> mPipelinePlanFiles = null; 
	public List<RawEmployee> mRawEmployees = null; 
	public List<RawPay> mRawPays = null; 
	public List<RawIrs1094c> mRawIrs1094cs = null; 
	public List<RawIrs1095c> mRawIrs1095cs = null; 
	public List<RawAirTranError> mRawAirTranErrors = null; 
	public List<RawAirTranReceipt> mRawAirTranReceipts = null; 
	public List<RawIrs1095cCI> mRawIrs1095cCoveredSecondaries = null; 
	public List<UploadTypeTemplate> mUploadTypeTemplates = null; 
	public List<UploadType> mUploadTypes = null;
	public List<Upload> mUploads = null;
	public List<PayClass> mPayClasses = null; 
	 
	// sub files 
	public List<CoverageFile> mSubCoverageFiles = null; 
	public List<EmployeeFile> mSubEmployeeFiles = null; 
	public List<PayFile> mSubPayFiles = null; 
 
	///////////////////////////////////////////// 
	// Calculation members 
	///////////////////////////////////////////// 
	public CalculationRequest mCalculationRequest = null; 
	public CalculationChannel mCalculationChannel = null; 
	public CalculationStepHandler mCalculationStepHandler = null; 
	public CalculationSpecification mCalculationSpecification = null; 
	public PayConversionCalculation mPayConversionCalculation = null; 
	public PayConversionCalculationReference mPayConversionCalculationReference = null; 
	public Irs10945bCalculation mIrs10945bCalculation = null;
	public Irs10945cCalculation mIrs10945cCalculation = null;
	public List<CalculationRequest> mCalculationRequests = null; 
	public List<CalculationChannel> mCalculationChannels = null; 
	public List<CalculationStepHandler> mCalculationStepHandlers = null; 
	public List<CalculationSpecification> mCalculationSpecifications = null; 
	public List<PayConversionCalculation> mPayConversionCalculations = null; 
	public List<PayConversionCalculationReference> mPayConversionCalculationReferences = null; 
 
	///////////////////////////////////////////// 
	// pipeline RawDetail members 
	///////////////////////////////////////////// 
	public RawConversionFailure mRawConversionFailure; 
	public RawInvalidation mRawInvalidation; 
	public RawNotice mRawNotice; 
	public RawDetailType mRawDetailType = RawDetailType.EMPLOYEEFILE;  

	///////////////////////////////////////////// 
	// uploader members 
	///////////////////////////////////////////// 
	public static final String CFG_UUID_KEY = "com.etclite.core.upload.Uploader.uuidv2";
	public static final String CFG_LDIR_KEY = "com.etclite.core.upload.Uploader.lastDirectory";
	public static final String CFG_LEML_KEY = "com.etclite.core.upload.Uploader.lastEmail";
	public static final String CFG_DEL_FILE = "delete config file and reload";

	public static final String HOMEFOLDER_NAME = ".xarriot";
	public static final String CFGFILE_NAME = "com.etc.utils.xarriot.app.config.filename";
	public static final String LOGCFGFILE_NAME = "logging.properties";
	public static final String LOGFILE_NAME = "log%g.txt";
	public static final String VERSION = "1.2.0";
	public static final String SOFTWARE_ID = "9647adb5-3c50-11ea-ac3a-0a6f9cb53658";
	public static HashMap<Long,ArrayList<UploadType>> mEmployerUploadTypes = new HashMap<Long,ArrayList<UploadType>>(16,1);
	public static HashMap<Long,ArrayList<DataProperty>> mUploadDataProperties = new HashMap<Long,ArrayList<DataProperty>>(16,1);

	public UploadRequest mUploadRequest = null;
	public KeyStore mKeyStore = null;
	public Logger mLogger = null;
	public String mUploadFromDirectory = null;			//USED TO LOAD LAST UPLOAD-FROM FOLDER
	public String mEmail = null;
	public int mWaitTime = 100;							//USED TO PACE PROGRESS MESSAGES
	public boolean mCanUpload = false;


	///////////////////////////////////////////// 
	// report members 
	///////////////////////////////////////////// 
//	public AirFilingEventReport mAirFilingEventReport; 
//	public List<AirFilingEventReport> mAirFilingEventReports = null; 

	///////////////////////////////////////////// 
	// Document 
	///////////////////////////////////////////// 
	public List<Folder> mFolders = null;
	public List<Document> mDocuments = null;
	///////////////////////////////////////////// 
	// Employer Files 
	///////////////////////////////////////////// 
	public CoverageFile mEmployerCoverageFile = null; 
	public EmployeeFile mEmployerEmployeeFile = null; 
	public List<CoverageFile> mEmployerCoverageFiles = null; 
	public List<EmployeeFile> mEmployerEmployeeFiles = null; 
	public List<PayFile> mEmployerPayFiles = null; 
	//indexes for tracking the employer file navigations 
	public int mEmployerCoverageFileIndex; 
	public int mEmployerEmployeeFileIndex; 
	public int mEmployerPayFileIndex; 
 
	///////////////////////////////////////////// 
	// dynamic file specification members 
	///////////////////////////////////////////// 
	public DynamicCoverageFileSpecification mDynamicCoverageFileSpecification = null; 
	public DynamicEmployeeFileSpecification mDynamicEmployeeFileSpecification = null; 
	public DynamicDeductionFileSpecification mDynamicDeductionFileSpecification = null; 
	public DynamicIrs1095cFileSpecification mDynamicIrs1095cFileSpecification = null; 
	public DynamicPayFileSpecification mDynamicPayFileSpecification = null; 
	public DynamicPayPeriodFileSpecification mDynamicPayPeriodFileSpecification = null; 
	public DynamicBenefitFileSpecification mDynamicBenefitFileSpecification = null; 
 
	public DynamicCoverageFileGenderReference mDynamicCoverageFileGenderReference = null; 
	public DynamicCoverageFileEmployerReference mDynamicCoverageFileEmployerReference = null; 
	public DynamicCoverageFileCoverageGroupReference mDynamicCoverageFileCoverageGroupReference = null; 
	public DynamicCoverageFileIgnoreRowSpecification mDynamicCoverageFileIgnoreRowSpecification = null; 
	public DynamicDeductionFileIgnoreRowSpecification mDynamicDeductionFileIgnoreRowSpecification = null; 
	public DynamicDeductionFileEmployerReference mDynamicDeductionFileEmployerReference = null; 
	public DynamicEmployeeFileGenderReference mDynamicEmployeeFileGenderReference = null; 
	public DynamicEmployeeFileIgnoreRowSpecification mDynamicEmployeeFileIgnoreRowSpecification = null; 
	public DynamicEmployeeFilePayCodeReference mDynamicEmployeeFilePayCodeReference = null; 
	public DynamicEmployeeFileEmployerReference mDynamicEmployeeFileEmployerReference = null; 
	public DynamicEmployeeFileLocationReference mDynamicEmployeeFileLocationReference = null; 
	public DynamicEmployeeFileUnionTypeReference mDynamicEmployeeFileUnionTypeReference = null; 
	public DynamicEmployeeFileCoverageGroupReference mDynamicEmployeeFileCoverageGroupReference = null; 
	public DynamicEmployeeFileDepartmentReference mDynamicEmployeeFileDepartmentReference = null; 
	public DynamicInsuranceFileSpecification mDynamicInsuranceFileSpecification = null; 
	public DynamicInsuranceFileEmployerReference mDynamicInsuranceFileEmployerReference = null; 
	public DynamicIrs1095cFileGenderReference mDynamicIrs1095cFileGenderReference= null; 
	public DynamicIrs1095cFileEmployerReference mDynamicIrs1095cFileEmployerReference= null; 
	public DynamicPayFileCoverageGroupReference mDynamicPayFileCoverageGroupReference = null; 
	public DynamicPayFileDepartmentReference mDynamicPayFileDepartmentReference = null; 
	public DynamicPayFileIgnoreRowSpecification mDynamicPayFileIgnoreRowSpecification = null; 
	public DynamicPayFilePayPeriodRule mDynamicPayFilePayPeriodRule = null; 
	public DynamicPayFileGenderReference mDynamicPayFileGenderReference = null; 
	public DynamicPayFilePayCodeReference mDynamicPayFilePayCodeReference = null; 
	public DynamicPayFilePayClassReference mDynamicPayFilePayClassReference = null; 
	public DynamicPayFileLocationReference mDynamicPayFileLocationReference = null; 
	public DynamicPayFileEmployerReference mDynamicPayFileEmployerReference = null; 
	public DynamicPayFileUnionTypeReference mDynamicPayFileUnionTypeReference = null; 
	public DynamicPayFilePayFrequencyReference mDynamicPayFilePayFrequencyReference = null; 
	public DynamicPayFileAdditionalHoursSpecification mDynamicPayFileAdditionalHoursSpecification = null; 
 
	public List<DynamicPayFileAdditionalHoursSpecification> mDynamicPayFileAdditionalHoursSpecifications = null; 
	public List<DynamicDeductionFileIgnoreRowSpecification> mDynamicDeductionFileIgnoreRowSpecifications = null; 
	public List<DynamicPayFilePayPeriodRule> mDynamicPayFilePayPeriodRules = null; 
	public List<DynamicCoverageFileSpecification> mDynamicCoverageFileSpecifications = null; 
	public List<DynamicCoverageFileIgnoreRowSpecification> mDynamicCoverageFileIgnoreRowSpecifications = null; 
	public List<DynamicCoverageFileEmployerReference> mDynamicCoverageFileEmployerReferences = null; 
	public List<DynamicEmployeeFileSpecification> mDynamicEmployeeFileSpecifications = null; 
	public List<DynamicEmployeeFileIgnoreRowSpecification> mDynamicEmployeeFileIgnoreRowSpecifications = null; 
	public List<DynamicPayFileSpecification> mDynamicPayFileSpecifications = null; 
	public List<DynamicPayFileIgnoreRowSpecification> mDynamicPayFileIgnoreRowSpecifications = null; 
	public List<DynamicPayPeriodFileSpecification> mDynamicPayPeriodFileSpecifications = null; 
	public List<DynamicBenefitFileSpecification> mDynamicBenefitFileSpecifications = null;
	 
	///////////////////////////////////////////// 
	// plan members 
	///////////////////////////////////////////// 
	public Carrier mCarrier = null; 
	public CoverageGroup mCoverageClass = null; 
	public BenefitSponsor mPlanSponsor = null; 
	public Benefit mBenefit = null;
	public PlanYearOffering mPlanYearOffering = null; 
	public PlanYearOfferingBenefit mPlanYearOfferingPlan = null;
	public EligibilityPeriod mEligibilityPeriod = null;
	 
	public List<Carrier> mCarriers = null; 
	public List<CoverageGroup> mCoverageClasses = null; 
	public List<BenefitSponsor> mPlanSponsors = null; 
	public List<PlanYearOfferingBenefit> mPlanYearOfferingPlans = null; 
 
	///////////////////////////////////////////// 
	// type members 
	///////////////////////////////////////////// 
	public int mAssociatedPropertyType = 0;  // 0 = account, 1 = employer 
	public int mContactType = 0;  // 0 = account, 1 = employer 
	public ScreenType mScreenType = null; 
	 
	///////////////////////////////////////////// 
	// boolean to track adds between dialogs 
	///////////////////////////////////////////// 
	private boolean m_AddMode = false; 
	 
	///////////////////////////////////////////// 
	// keep os version because of differences in gui behaior 
	///////////////////////////////////////////// 
	private boolean windowsOS = false; 
	
	// track which type of core data object we could be updating in the System Data popup
	public SystemDataType mCurrentCoreDataType = SystemDataType.NONE;
	 
	public boolean isWindows() { 
		return windowsOS; 
	} 
	 
	///////////////////////////////////////////// 
	// control members 
	///////////////////////////////////////////// 
	public CorvettoConnection mCorvettoConnection; 
	public WsManager mCorvettoManager; 
 
	public boolean mbDebug = false; // member to control the debug output 
	public String mDebugMessage = "No Message"; 
	 
	// use this to track the HR source 
	public int hrSource = 0; 
	
	///////////////////////////////////////////// 
	// caching utility members
	///////////////////////////////////////////// 
	private HashMap<CacheType<?,?>,Calendar> cacheMap;
	 
	///////////////////////////////////////////// 
	// Make this a singleton that we can access  
	// this class everywhere 
	///////////////////////////////////////////// 
    private static DataManager mInstance; 
    static { mInstance = new DataManager(); } 

    public static DataManager i() 
    { 
        return mInstance; 
    }	 

	private DataManager()
    { 
    	init(); 
    } 
     
	// Clear all of the  data elements for the account 
	public void clearAccountMemberElements() 
	{ 
		// object members 
		mAccount = null; 
		mEmployer = null; 
		mEmployee = null; 
		mDependent = null; 
		mAccountContact = null; 
		mEmployerContact = null; 
		mDataProperty = null; 
		mDepartment = null; 
		mTaxYear = null; 
		mTaxMonth = null; 
		mEmploymentPeriod = null; 
 
		// data list members 
		mAccountContacts = null; 
		mEmployers = null; 
		mEmployees = null; 
		mDependents = null; 
		mUsers = null; 
		 
		// lookup members 
		mLUAccounts = null; 
		mLUEmployees = null; 
		mLUDependents = null; 
		mLUUsers = null; 
	} 
 
	public void setDebug(boolean bDebug) { 
		mbDebug = bDebug;	 
	} 
	 
	public boolean isDebug() { 
		return mbDebug; 
	} 
 
	public void setOrg(Organization newOrg) {	 
		mOrg = newOrg; 
	}	 
	 
	//addmode handlers 
	public void setAddMode(boolean addMode) { this.m_AddMode = addMode; } 
	public boolean isAddMode() { return m_AddMode; } 
	 
	public void init() 
	{ 
		if(System.getProperty("os.name").toString().toUpperCase().contains("WINDOWS")) 
			windowsOS = true; 
 
		cacheMap = new HashMap<CacheType<?,?>,Calendar>();
		logr = Logger.getLogger(this.getClass().getCanonicalName());
	} 
 
	public boolean ResetLocalDatabase() 
	{ 
		try
		{
			logr.severe("RESETTING LOCAL DATABASE.");
			EtcAdmin.i().showAppStatus("Resetting Local Database", "Disabling local DB", 0.1, true);

			//set create, so when we re-load the persistence context it drops the db.
			EmsApp.getInstance().getProperties().put(EmsApp.JPA_HBM2DDL, "create");
			//close the connection to the db
			EmsApp.getInstance().enableJpa(false, null);
			// delete the existing db files
			String userDir = System.getProperty("user.home");
			String pathSeparator = System.getProperty("file.separator");
			String dbPathString = userDir.concat(pathSeparator).concat(".xarriot")
															   .concat(pathSeparator)
															   .concat("apps")
															   .concat(pathSeparator)
															   .concat(EmsApp.getInstance().getApplicationHomeFolderName())
															   .concat(pathSeparator)
															   .concat("db")
															   .concat(pathSeparator);

			File dbFiles = new File(dbPathString);
			Thread.sleep(1000);
			EtcAdmin.i().showAppStatus("Resetting Local Database", "Cleaning Up Files", 0.3, true);

			try {
				FileUtils.cleanDirectory(dbFiles);
			} catch (Exception e) {
				EtcAdmin.i().showAppStatus("Resetting Local Database", "Could not delete DB File. Continuing reset process.", 0.4, true);
				Thread.sleep(1000);
			}
			//reset the connection to the db using the updated params.
			EmsApp.getInstance().createLocalPersistenceContext();
			//reload the accounts 
			EtcAdmin.i().showAppStatus("Resetting Local Database", "Updating Cache", 0.6, true);
			updateCache(); 
			EtcAdmin.i().showAppStatus("Resetting Local Database", "Updating Second Tier Cache", 0.6, true);
			updateSecondTierCache();
			EtcAdmin.i().showAppStatus("", "", 0, false);
			
		}catch(Exception e)
		{
			DataManager.i().log(Level.SEVERE, e);
			EtcAdmin.i().showAppStatus("", "", 0, false);
			return false;
		}
		logr.severe("DATABASE HAS BEEN RESET.");
		return true;
	} 
	 
	public WsManager getCorvettoManager() 
	{ 
		try {  return EmsApp.getInstance().getApiManager(); }
		catch (CoreException e) { DataManager.i().log(Level.SEVERE, e);  } 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		return null; 
	} 
	
	public void reconnectCorvetto() 
	{
		try {

			EtcAdmin.i().updateStatus(.5, "Reconnecting Corvetto Server");
			
			if(!EmsApp.getInstance().enableApi(false))
				if(EmsApp.getInstance().enableApi(true))
					EtcAdmin.i().updateStatus(0, "Reconnect Complete, Ready");
				else
					EtcAdmin.i().updateStatus(0, "Reconnect failed. Please restart.");
			
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	public  HashMap<CacheType<?,?>,Calendar> getCacheMap() { return this.cacheMap; }
	
	/**
	 * <p>
	 * checks if the existing type has been cached with no filter or <br>
	 * with the provided filter within the last 5 minutes. Returns true <br>
	 * if the Type is already cached.</p>
	 * @param <T> The Root cache type
	 * @param <X> The Filter cache type
	 * @param root the Root class
	 * @param filter the Filter class
	 * @return boolean true if cached
	 */
	public <T extends CoreData,X extends CoreData>boolean isPreviouslyCached(Class<T> root, Class<X> filter)
	{
		CacheType<T,X> ctype = new CacheType<T,X>(root,filter);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -5);
		
		try
		{
			return (getCacheMap().get(ctype) != null && getCacheMap().get(ctype).after(cal));
		}finally
		{
			ctype = null;
			cal = null;
		}
	}

	/////////////////////////////////////////////////////////////////////////// 
	// Generic Methods 
	/////////////////////////////////////////////////////////////////////////// 
/*	public <T extends CoreData, X extends CoreRequest<T,X>> List<T> getAll(final CoreRequest<T,X> request) 
	{ 
		List<T> tList = null; 
		try { 
			tList = getCorvettoManager().getAll(request); 
		} catch (CoreException | InterruptedException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
 
		return tList; 
	} 
	
	public <T extends CoreData, X extends CoreRequest<T,X>> T get(final CoreRequest<T,X> request) 
	{ 
		try { 
			return getCorvettoManager().get(request); 
		} catch (CoreException | InterruptedException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
		 
		return null; 
	} 
	 
	public <T extends CoreData, X extends CoreRequest<T,X>> T update(final CoreRequest<T,X> request) 
	{ 
		try { 
			return getCorvettoManager().update(request); 
		} catch (CoreException | InterruptedException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
		 
		return null; 
	} 
	 
	public <T extends CoreData, X extends CoreRequest<T,X>> T add(final CoreRequest<T,X> request) 
	{ 
		try { 
			return getCorvettoManager().add(request); 
		} catch (CoreException | InterruptedException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
		 
		return null; 
	} 
 
	public <T extends CoreData, X extends CoreRequest<T,X>> boolean remove(final CoreRequest<T,X> request) 
	{ 
		try { 
			return getCorvettoManager().remove(request); 
		} catch (CoreException | InterruptedException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
		 
		return false; 
	} 
	*/ 
	/////////////////////////////////////////////////////////////////////////// 
	// GET Functions 
	/////////////////////////////////////////////////////////////////////////// 
	public ScreenType getScreenType() { 
		return mScreenType; 
	} 
	 
	// gets the accounts from the database 
	public void updateCache()
	{ 
		try { 		
			logr.info("CACHING ORGS");
			AdminPersistenceManager.getInstance().getAll(new CorvettoRequest<Organization>(Organization.class, true));
			logr.info("CACHING ACCOUNTS");
			//mAccounts = AdminPersistenceManager.getInstance().getAll(new CorvettoRequest<Account>(Account.class, true));
			AccountRequest acctrqs = new AccountRequest();
			acctrqs.setFetchInactive(true);
			mAccounts = AdminPersistenceManager.getInstance().getAll(acctrqs);			

// pagination testing for now			
//			acctrqs.setPaginationEnabled(true);
//			acctrqs.getPagination().setPageSize(20);
//			acctrqs.getPagination().setPageNumber(30);
			//mAccounts = AdminPersistenceManager.getInstance().getAllAutoPaginated(acctrqs);
//			mAccounts = AdminPersistenceManager.getInstance().getAll(acctrqs);			
			//page2
//			acctrqs.getPagination().incrementPageNumber();
			//returns page 2
//			mAccounts = AdminPersistenceManager.getInstance().getAll(acctrqs);
// end of of pagination testing block				
			
			logr.info("CACHING EMPLOYERS");
			//mEmployers = AdminPersistenceManager.getInstance().getAll(new CorvettoRequest<Employer>(Employer.class, true));
			EmployerRequest empReq = new EmployerRequest();
			empReq.setFetchInactive(true);
			mEmployers = AdminPersistenceManager.getInstance().getAll(empReq);
			mEmployersList = mEmployers;
//			updateUsers();	 
//			updateTaxYearServiceLevels(); 
//			updatePipelineBaseObjects(); 
//			updatePayClasses();	 
//			updatePayConversionCalculations(); 
		} catch (CoreException e) { 
			DataManager.i().log(Level.SEVERE, e); 
		}		 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
	
	/**
	 * 
	 */
	public void updateSecondTierCache() 
	{
		try
		{
			logr.info("CACHING PIPELINE CHANNELS");
			PipelineChannelRequest pcReq = new PipelineChannelRequest();
			pcReq.setFetchInactive(true);
			mPipelineChannels = AdminPersistenceManager.getInstance().getAll(pcReq); //new CorvettoRequest<PipelineChannel>(PipelineChannel.class, true));
			logr.info("CACHING PIPELINE SPECIFICATIONS");
			PipelineSpecificationRequest psReq = new PipelineSpecificationRequest();
			psReq.setFetchInactive(true);
			mPipelineSpecifications = AdminPersistenceManager.getInstance().getAll(psReq); //new CorvettoRequest<PipelineSpecification>(PipelineSpecification.class, true));
			logr.info("CACHING PIPELINE FILE STEP HANDLERS");
			PipelineFileStepHandlerRequest pshReq = new  PipelineFileStepHandlerRequest();
			pshReq.setFetchInactive(true);
			mPipelineFileStepHandlers = AdminPersistenceManager.getInstance().getAll(pshReq); //new CorvettoRequest<PipelineFileStepHandler>(PipelineFileStepHandler.class, true));
			logr.info("CACHING PIPELINE PARSE PATTERNS");
			PipelineParsePatternRequest ppReq = new  PipelineParsePatternRequest();
			ppReq.setFetchInactive(true);
			mPipelineParsePatterns = AdminPersistenceManager.getInstance().getAll(ppReq); //new CorvettoRequest<PipelineParsePattern>(PipelineParsePattern.class, true));
			logr.info("CACHING PIPELINE PARSE DATE FORMATS");
			PipelineParseDateFormatRequest pdfReq = new PipelineParseDateFormatRequest();
			pdfReq.setFetchInactive(true);
			mPipelineParseDateFormats = AdminPersistenceManager.getInstance().getAll(pdfReq); //new CorvettoRequest<PipelineParseDateFormat>(PipelineParseDateFormat.class, true));
		}catch(CoreException e)
		{
			DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}
	
	public void updateAccountsAndEmployersByTask() 
	{
		Task<Void> task = new Task<Void>() 
		{ 
            @Override 
            protected Void call() throws Exception  
            { 
            	updateFolders();
            	updateAccounts();
            	updateEmployers();
                return null; 
            } 
        }; 
        new Thread(task).start(); 
	}
	
	public boolean updateAccounts() 
	{
		try
		{
			AccountRequest rqs = new AccountRequest();
			rqs.setFetchInactive(true);
			if((mAccounts = AdminPersistenceManager.getInstance().getAll(rqs)) != null)
				return true;
			else
				return false;
		}catch(CoreException e)
		{
        	DataManager.i().log(Level.SEVERE, e); 
			return false;
		}
	    catch (Exception e) {  
	    	DataManager.i().logGenericException(e); 
	    	return false;
	    }
	}
 
	public void updateEmployers() 
	{ 
		try { 
			EmployerRequest request = new EmployerRequest(); 
			request.setFetchInactive(true); 
			mEmployers = AdminPersistenceManager.getInstance().getAll(request); 
			mEmployersList = mEmployers;
		} catch (CoreException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		}		 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
	 
		public void updateFolders() 
		{ 
			try { 
				FolderRequest request = new FolderRequest(); 
				request.setFetchInactive(true); 
				mFolders = AdminPersistenceManager.getInstance().getAll(request); 
			} catch (CoreException e) { 
	        	DataManager.i().log(Level.SEVERE, e); 
			}		 
		    catch (Exception e) {  DataManager.i().logGenericException(e); }
		} 
	
	public void updateUsers() 
	{ 
		try { 
			UserRequest request = new UserRequest(); 
			mUsers = AdminPersistenceManager.getInstance().getAll(request); 
			//sort 
			Collections.sort(mUsers, (o1, o2) -> (o1.getLastName().compareTo(o2.getLastName()))); 
		} catch (CoreException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		}		 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
 
	public void updateTaxYearServiceLevels() 
	{ 
		try { 
			TaxYearServiceLevelRequest request = new TaxYearServiceLevelRequest(); 
			mTaxYearServiceLevels = AdminPersistenceManager.getInstance().getAll(request); 
			//sort 
			Collections.sort(mTaxYearServiceLevels, (o1, o2) -> (o1.getName().compareTo(o2.getName()))); 
		} catch (CoreException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		}		 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
 
//	public void updateUploadTypes(Long employerId) 
//	{ 
//		try { 
//			UploadTypeRequest request = new UploadTypeRequest(); 
////			request.setFetchInactive(true); 
//			request.setEmployerId(employerId);
//			mUploadTypes = AdminPersistenceManager.getInstance().getAll(request); 
//		} catch (CoreException e) { 
//        	DataManager.i().log(Level.SEVERE, e); 
//		}			System.out.println(mUploadTypes + " full list of uploadTypes");
//		 
//	} 

	public CoverageFile getCoverageFile(Long fileId) 
	{ 
		CoverageFile cFile = null; 
		// magic happens here 
		return cFile; 
	} 
	 
	public EmployeeFile getEmployeeFile(EmployeeFile employeefile)
	{ 
/*		try { 
			//retrieve from the server 
			employeefile = mCoreManager.getEmployeeFile(employeefile); 
			//save to the database 
			mLocalDatabaseManager.insertEmployeeFile(employeefile); 
		} catch (CoreException | InterruptedException | SQLException | ParseException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
			return null; 
		} 
		 
		return employeefile; 
*/ 
		return null; 
	} 
 
	// get the selected ParsePattern by name 
	public PipelineParsePattern getParsePattern(String parsePatternName) 
	{ 
		if(parsePatternName.length() < 1) return null; 
		 
		if(mPipelineParsePatterns != null) 
		{ 
			for(PipelineParsePattern pipelineParsePattern: mPipelineParsePatterns) 
			{ 
				if(pipelineParsePattern.getName().equals(parsePatternName)) 
					return pipelineParsePattern; 
			} 
		} 
		return null; 
	}	 
 
	// get the selected ParsePattern by ID 
	public PipelineParsePattern getParsePattern(long patternID) 
	{ 
		if(mPipelineParsePatterns != null) 
		{ 
			for(PipelineParsePattern pipelineParsePattern: mPipelineParsePatterns) 
			{ 
				if(pipelineParsePattern.getId().equals(patternID)) 
					return pipelineParsePattern; 
			} 
		} 
		return null; 
	}	 
	 
	// get the selected ParseDateFormat by name 
	public PipelineParseDateFormat getParseDateFormat(String parseDateFormatName) 
	{ 
		if(mPipelineParseDateFormats != null) 
		{ 
			for(PipelineParseDateFormat pipelineParseDateFormat: mPipelineParseDateFormats) 
			{ 
				if(pipelineParseDateFormat.getName().equals(parseDateFormatName)) 
					return pipelineParseDateFormat; 
			} 
		} 
		return null; 
	}	 
 
	//get the selected ParseDateFormat by ID 
	public PipelineParseDateFormat getParseDateFormat(long formatID) 
	{ 
		if(mPipelineParseDateFormats != null) 
		{ 
			for(PipelineParseDateFormat pipelineParseDateFormat: mPipelineParseDateFormats) 
			{ 
				if(pipelineParseDateFormat.getId().equals(formatID)) 
					return pipelineParseDateFormat; 
			} 
		} 
		return null; 
	}	 
	 
	public DynamicPayFilePayPeriodRule getDynamicPayFilePayPeriodRule(DynamicPayFilePayPeriodRule rule)
	{ 
/*		try { 
			if(rule == null) return rule; 
			// try first to get it from the local store. We should return something here. 
			DynamicPayFilePayPeriodRule newRule = mLocalDatabaseManager.getDynamicPayFilePayPeriodRule(rule.getId()); 
			// if we are still missing data, go get it from the server 
			if(newRule == null || newRule.getToJsonType() == ToJsonType.ID) { 
				newRule = mCoreManager.getDynamicPayFilePayPeriodRule(rule); 
				 
				// check to see if we got a response. If not, return the submitted object 
				if(newRule == null) return rule; 
				//We got something, store it locally 
				mLocalDatabaseManager.insertDynamicPayFilePayPeriodRule(newRule); 
				// and retrieve it so that we have the full employer and spec 
				newRule = mLocalDatabaseManager.getDynamicPayFilePayPeriodRule(rule.getId()); 
			} 
			 
			// and return it 
			return newRule; 
		} catch (SQLException | CoreException | InterruptedException | ParseException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
		 
		//If all else fails, return the submitted object 
		return rule;  
*/ 
		return null; 
	} 
 
	/////////////////////////////////////////////////////////////////////////// 
	// SET Functions 
	/////////////////////////////////////////////////////////////////////////// 
 
	public void setLocalUser(User user) { 
		mLocalUser = user; 
	} 
	 
	public void setScreenType(ScreenType screenType) { 
		mScreenType = screenType; 
	} 
	 
	// gets an account by the account name 
	public Account getAccount(String name) 
	{ 
		for(Account acct : mAccounts) 
		{ 
			if(acct.isActive() == false) continue;
			if(acct.getName().equals(name)) 
				return acct; 
		}		 
		return null; 
	} 
	 
	public void loadLogEntries() { 
		
	} 
	 
	public void insertLogEntry(String description, LogType logType) 
	{ 
		if(logType != null) 
		{
			switch(logType) 
			{
				case INFO:
					Logger.getLogger(this.getClass().getCanonicalName()).log(Level.INFO, description);	
					break;
				case DEBUG:
					Logger.getLogger(this.getClass().getCanonicalName()).log(Level.INFO, description);	
					break;
				case WARNING:
					Logger.getLogger(this.getClass().getCanonicalName()).log(Level.WARNING, description);	
					break;
				case CRITICAL:
					Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE, description);	
					break;
				case ERROR:
					Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE, description);	
					break;
				default:
					Logger.getLogger(this.getClass().getCanonicalName()).log(Level.INFO, description);	
					break;
			}	
		} else
			Logger.getLogger(this.getClass().getCanonicalName()).log(Level.INFO, description);
	}
	 
	public void log(Level level, Exception e) 
	{ 
		Logger.getLogger(this.getClass().getCanonicalName()).log(level, "Exception", e);
		
		// check for a closed db file and reset if present
		if(e == null || e.getMessage() == null) return;
		if(e.getMessage().toLowerCase().contains("the object is already closed") == true ||
		   e.getMessage().toLowerCase().contains("the file is locked")	||
		   e.getMessage().toLowerCase().contains("unable to acquire jdbc connection")) 
		{
			// houston, we have a problem. Reset the local DB connection
			insertLogEntry("System Reset requested due to reported DB issue.",LogType.ERROR);
			ResetLocalDatabase();
			resetCounter++;

			// alert the user if this keeps happening			
			if (resetCounter > 2) 
			{
				insertLogEntry("WARNING: Excessive System Resets requested due to reported closed DB.",LogType.ERROR);
				Utils.alertUser("Excessive DB Reset Required", "The local DB has required repeated resets. Please check you system for errors.");
				// reset the counter
				resetCounter = 0;
			}
		}
	} 
	 
	public void log(Level level, Throwable throwable) {
        Logger.getLogger(this.getClass().getCanonicalName()).log(level, "Throwable Catch", throwable);
	}
	
	public void logGenericException(Exception e) { 
		Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE, "Generic Exception Catch" , e);
	} 
	 
	public void clearDebugLog() { 

	} 
 
	// function verifies that a given account is allowable to the current signed in user 
	public boolean verifyAccountAllowed(long accountId) 
	{ 
		// use this to check against accounts loaded 
		for(Account account: mAccounts) 
		{ 
			if(account.getId() == accountId) 
				if(account.getOrganization() == mOrg) 
					return true; 
		} 
		return false; 
	} 
	 
	public void setAccountContact(int nContactID)
	{ 
		//set the current employee from our account collection 
		mAccountContact = mAccount.getContacts().get(nContactID);   
		mContactType = 0; 
 
		//tell main to load the contact screen 
		EtcAdmin.i().setScreen(ScreenType.ACCOUNTCONTACT, true); 
	} 
	 
	public void setEmployerContact(int nContactID)
	{ 
		//set the current employee from our account collection 
		EmployerContactRequest request = new EmployerContactRequest();
		request.setId(mEmployer.getContactId());

		try {
			mEmployerContacts = AdminPersistenceManager.getInstance().getAll(request);
		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		mEmployerContact = mEmployerContacts.get(nContactID);
		mContactType = 1; 
	} 
 
	public void setAccountDataProperty(int nAssociatedPropertyID)
	{ 
		//set the current employee from our account collection 
		mDataProperty = mAccount.getProperties().get(nAssociatedPropertyID);   
		mAssociatedPropertyType = 0; 

		//tell main to load the associated property screen 
		EtcAdmin.i().setScreen(ScreenType.ACCOUNTASSOCIATEDPROPERTY, true); 
	} 
 
	public void setEmployerAssociatedProperty(int nAssociatedPropertyID)
	{ 
		//set the current associated property from our employer collection 
		mDataProperty = mEmployer.getProperties().get(nAssociatedPropertyID);   
		mAssociatedPropertyType = 1; 
 
		//tell main to load the associated property screen 
		//EtcAdmin.i().setScreen(ScreenType.EMPLOYERASSOCIATEDPROPERTY, true); 
	} 
 
	public void setDepartment(int nDepartmentID)
	{ 
		//set the current department from our employer collection 
		mDepartment = mEmployer.getDepartments().get(nDepartmentID);   
 
		//tell main to load the Department screen 
		//EtcAdmin.i().setScreen(ScreenType.DEPARTMENT, true); 
	} 
 
	public void setIrs1094b(int nID)
	{ 
		//set from our collection using the ID 
		mIrs1094b = mIrs1094bs.get(nID);   
	} 
 
	public void setIrs1095b(int nID)
	{ 
		//set from our collection using the ID 
		mIrs1095b = mIrs1095bs.get(nID);   
	} 
 
	public void setIrs1095bCoveredSecondary(int nID)
	{ 
		//set from our collection using the ID 
		mIrs1095bCI = mIrs1095bCIs.get(nID);   
	} 
 
	public void setIrs1095cCoveredSecondary(int nID)
	{ 
		//set from our collection using the ID 
		mIrs1095cCI = mIrs1095cCIs.get(nID);   
	} 
 
	public void setIrs1094Submission(int nID)
	{ 
		//set from our collection using the ID 
		mIrs1094Submission = mIrs1094Submissions.get(nID);   
	} 
 
	public void setEmploymentPeriod(int nEmploymentPeriodID)
	{ 
		//set the current tax year from our employer collection 
		mEmploymentPeriod = mEmployee.getEmploymentPeriods().get(nEmploymentPeriodID);   
 
		//tell main to load the pertinent ui 
		EtcAdmin.i().setScreen(ScreenType.EMPLOYMENTPERIOD, true); 
	} 
 
	public void setUser(int nUserID) 
	{ 
		//set the current user from our employer collection 
		if(mUsers != null) 
			mUser = mUsers.get(nUserID);   
	} 
 
	public void setCoverageGroup(CoverageGroup covClass){ 
		mCoverageClass = covClass; 
	} 
 
	public void setPipelineCoverageFile(CoverageFile coverageFile) { 
		mPipelineCoverageFile = coverageFile; 
	} 
 
	public void setDynamicCoverageFileSpecification(int nFileID) 
	{ 
		if(mDynamicCoverageFileSpecifications != null) 
			mDynamicCoverageFileSpecification = mDynamicCoverageFileSpecifications.get(nFileID);	 
		mDynamicCoverageFileSpecificationIndex = nFileID; 
	} 
	 
	public void setDynamicEmployeeFileSpecification(int nFileID) 
	{ 
		if(mDynamicEmployeeFileSpecifications != null) 
			mDynamicEmployeeFileSpecification = mDynamicEmployeeFileSpecifications.get(nFileID);		 
	} 
	 
	public void setDynamicPayFileSpecification(int nFileID) 
	{ 
		if(mDynamicPayFileSpecifications != null) 
			mDynamicPayFileSpecification = mDynamicPayFileSpecifications.get(nFileID);		 
	} 
	 
	public void setDynamicPlanFileSpecification(int nFileID) 
	{ 
		if(mDynamicBenefitFileSpecifications != null) 
			mDynamicBenefitFileSpecification = mDynamicBenefitFileSpecifications.get(nFileID);		 
	} 
	 
	public void setDynamicPayPeriodFileSpecification(int nFileID)
	{ 
		if(mDynamicPayPeriodFileSpecifications != null) 
			mDynamicPayPeriodFileSpecification = mDynamicPayPeriodFileSpecifications.get(nFileID);		 
	} 
		 
	public void setPipelineParsePattern(int nPatternID) 
	{ 
		if(mPipelineParsePatterns != null && nPatternID > -1) 
			mPipelineParsePattern = mPipelineParsePatterns.get(nPatternID);		 
	} 
		 
	public void setPipelineParseDateFormat(int nFormatID) 
	{ 
		if(mPipelineParseDateFormats != null) 
			mPipelineParseDateFormat = mPipelineParseDateFormats.get(nFormatID);		 
	} 
 
	public void setPipelineEmployeeFile(EmployeeFile pipelineEmployeeFile) { 
		mPipelineEmployeeFile = pipelineEmployeeFile; 
	} 
 
	//Uploader getters and setters
	public UploadRequest getActiveUpload() { return mUploadRequest; }
	public void setActiveUpload(UploadRequest rq, File file) { mUploadRequest = rq; setActiveUploadFile(file); }
	public void setActiveUpload(UploadRequest rq) { mUploadRequest = rq; }
	
	public File getActiveUploadFile() { return mFile; }
	public void setActiveUploadFile(File file) { mFile = file; }

	/////////////////////////////////////////////////////////////////////////// 
	// LOAD Functions 
	/////////////////////////////////////////////////////////////////////////// 
	// Update the basic pipeline objects that are required for proper operation 
	public void updatePipelineBaseObjects()  throws CoreException, InterruptedException, SQLException, ParseException  
	{ 
    	EtcAdmin.i().setStatusMessage("Checking Pipeline Channels"); 
		loadUpdatedPipelineChannels(); 
    	EtcAdmin.i().setStatusMessage("Checking Pipeline Specifications"); 
		loadUpdatedPipelineSpecifications(); 
    	EtcAdmin.i().setStatusMessage("Checking File Step Handlers"); 
		loadUpdatedPipelineFileStepHandlers(); 
    	EtcAdmin.i().setStatusMessage("Checking Pipeline Parse Patterns"); 
		loadUpdatedParsePatterns(); 
    	EtcAdmin.i().setStatusMessage("Checking Pipeline Parse Date Formats"); 
		loadUpdatedParseDateFormats(); 
 	} 
	 
	public void loadUpdatedPipelineChannels() 
	{ 
		try { 
			PipelineChannelRequest request = new PipelineChannelRequest(); 
			mPipelineChannels = AdminPersistenceManager.getInstance().getAll(request); 
		} catch (CoreException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
 
	public void loadUpdatedParsePatterns() 
	{ 
		try { 
			PipelineParsePatternRequest request = new PipelineParsePatternRequest(); 
			mPipelineParsePatterns = AdminPersistenceManager.getInstance().getAll(request); 
		} catch (CoreException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
	 
	public void loadUpdatedParseDateFormats() 
	{ 
		try { 
			PipelineParseDateFormatRequest request = new PipelineParseDateFormatRequest(); 
			mPipelineParseDateFormats = AdminPersistenceManager.getInstance().getAll(request); 
		} catch (CoreException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
	 
	// Retrieve an employer from the local store using employer Id 
	public Employer getEmployer(Long employerId){ 
		return null; 
	} 
	 
	public void loadPipelineFileStepHandler(int nHandlerID)  
	{ 
		//we should have a collection by now 
		if(mPipelineFileStepHandlers == null || nHandlerID < 0) return; 
		mPipelineFileStepHandler = mPipelineFileStepHandlers.get(nHandlerID); 
	} 
 
	public void loadUpdatedPipelineSpecifications()  
	{ 
		try { 
			// get the most recent 
			PipelineSpecificationRequest request = new PipelineSpecificationRequest(); 
			mPipelineSpecifications = AdminPersistenceManager.getInstance().getAll(request); 
		} catch (CoreException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
 
	public void loadUpdatedPipelineFileStepHandlers() throws CoreException, InterruptedException, SQLException, ParseException 
	{ 
		try { 
			// get the most recent 
			PipelineFileStepHandlerRequest request = new PipelineFileStepHandlerRequest(); 
			mPipelineFileStepHandlers = AdminPersistenceManager.getInstance().getAll(request); 
		} catch (Exception e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	}  
	 
	public void loadDynamicFileSpecification(long dynamicFileSpecificationID) 
	{ 
		//we need to have a  pipeline channel at this point. This is a problem, but right now show a warning. 
		if(mPipelineChannel == null) 
		{ 
			Utils.alertUser("Program Error", "No current Pipeline Channel. Returning to previous screen."); 
			return; 
		} 
		 
		//need the parse patterns since we reference them 
		loadUpdatedParsePatterns(); 
		//need the parse date formats too 
		loadUpdatedParseDateFormats(); 
		 
		// We need to load the correct DynamicFileSpecification object based 
		// on the current pipeline selection 
		switch(mPipelineChannel.getType())
		{ 
			case COVERAGE: 
				loadDynamicCoverageFileSpecification(dynamicFileSpecificationID); 
				break; 
			case DEDUCTION: 
				loadDynamicDeductionFileSpecification(dynamicFileSpecificationID); 
				break; 
			case EMPLOYEE: 
				loadDynamicEmployeeFileSpecification(dynamicFileSpecificationID); 
				break; 
			case INSURANCE: 
				loadDynamicInsuranceFileSpecification(dynamicFileSpecificationID); 
				break; 
			case PAY: 
				loadDynamicPayFileSpecification(dynamicFileSpecificationID); 
				break; 
			case PAYPERIOD: 
				loadDynamicPayPeriodFileSpecification(dynamicFileSpecificationID); 
				break; 
			//case PLAN: 
			//	break; 
			case IRS1094C: 
				break; 
			case IRS1095C:
				loadDynamicIrs1095cFileSpecification(dynamicFileSpecificationID); 
				break; 
			case IRSAIRERR: 
				break; 
			case IRSAIRRCPT: 
				break; 
			case EVENT: 
				break; 
			default: 
				break; 
		} 
	} 
	 
	public void loadDynamicInsuranceFileSpecification(long dynamicFileSpecificationID)
	{ 
		try { 
			
			DynamicInsuranceFileSpecificationRequest request = new DynamicInsuranceFileSpecificationRequest(); 
			request.setId(dynamicFileSpecificationID); 
			mDynamicInsuranceFileSpecification = AdminPersistenceManager.getInstance().get(request); 
			 
		} catch (CoreException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		}  
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
	 
	public void loadDynamicIrs1095cFileSpecification(long dynamicFileSpecificationID)
	{ 
		try { 
			
			DynamicIrs1095cFileSpecificationRequest request = new DynamicIrs1095cFileSpecificationRequest(); 
			request.setId(dynamicFileSpecificationID); 
			mDynamicIrs1095cFileSpecification = AdminPersistenceManager.getInstance().get(request); 
			 
		} catch (CoreException e) { 
       	DataManager.i().log(Level.SEVERE, e); 
		}  
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
	 
	public void loadDynamicDeductionFileSpecification(long dynamicFileSpecificationID) 
	{ 
		try { 
			
			DynamicDeductionFileSpecificationRequest request = new DynamicDeductionFileSpecificationRequest(); 
			request.setId(dynamicFileSpecificationID); 
			mDynamicDeductionFileSpecification = AdminPersistenceManager.getInstance().get(request); 
			 
		} catch (CoreException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		}  
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
	 
	public void loadDynamicPayFileSpecification(long dynamicFileSpecificationID)
	{ 
		try { 
			
			DynamicPayFileSpecificationRequest request = new DynamicPayFileSpecificationRequest(); 
			request.setId(dynamicFileSpecificationID); 
			mDynamicPayFileSpecification = AdminPersistenceManager.getInstance().get(request); 
			 
			getSubObjects(mDynamicPayFileSpecification); 
			 
		} catch (CoreException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		}  
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
 
	public void getSubObjects(DynamicPayFileSpecification spec)
	{ 
		//expand sub objects 
		if(spec.getSsnParsePattern() != null) 
			spec.setSsnParsePattern(getParsePattern(spec.getSsnParsePattern().getId())); 
		if(spec.getErRefParsePattern() != null) 
			spec.setErRefParsePattern(getParsePattern(spec.getErRefParsePattern().getId())); 
		if(spec.getfNameParsePattern() != null) 
			spec.setfNameParsePattern(getParsePattern(spec.getfNameParsePattern().getId())); 
		if(spec.getmNameParsePattern() != null) 
			spec.setmNameParsePattern(getParsePattern(spec.getmNameParsePattern().getId())); 
		if(spec.getlNameParsePattern() != null) 
			spec.setlNameParsePattern(getParsePattern(spec.getlNameParsePattern().getId())); 
		if(spec.getStreetParsePattern() != null) 
			spec.setStreetParsePattern(getParsePattern(spec.getStreetParsePattern().getId())); 
		if(spec.getCityParsePattern() != null) 
			spec.setCityParsePattern(getParsePattern(spec.getCityParsePattern().getId())); 
		if(spec.getStateParsePattern() != null) 
			spec.setStateParsePattern(getParsePattern(spec.getStateParsePattern().getId())); 
		if(spec.getZipParsePattern() != null) 
			spec.setZipParsePattern(getParsePattern(spec.getZipParsePattern().getId())); 
		 
		if(spec.getDobFormat()!= null) 
			spec.setDobFormat(getParseDateFormat(spec.getDobFormat().getId())); 
		if(spec.getHireDtFormat()!= null) 
			spec.setHireDtFormat(getParseDateFormat(spec.getHireDtFormat().getId())); 
		if(spec.getRhireDtFormat()!= null) 
			spec.setRhireDtFormat(getParseDateFormat(spec.getRhireDtFormat().getId())); 
		if(spec.getTermDtFormat()!= null) 
			spec.setTermDtFormat(getParseDateFormat(spec.getTermDtFormat().getId())); 
		 
		if(spec.getPpdStartDtParsePattern() != null) 
			spec.setPpdStartDtParsePattern(getParsePattern(spec.getPpdStartDtParsePattern().getId())); 
		if(spec.getPpdStartDtFormat() != null) 
			spec.setPpdStartDtFormat(getParseDateFormat(spec.getPpdStartDtFormat().getId())); 
		if(spec.getPpdEndDtParsePattern() != null) 
			spec.setPpdEndDtParsePattern(getParsePattern(spec.getPpdEndDtParsePattern().getId())); 
		if(spec.getPpdEndDtFormat() != null) 
			spec.setPpdEndDtFormat(getParseDateFormat(spec.getPpdEndDtFormat().getId())); 
		if(spec.getPayDtParsePattern() != null) 
			spec.setPayDtParsePattern(getParsePattern(spec.getPayDtParsePattern().getId())); 
		if(spec.getPayDtFormat() != null) 
			spec.setPayDtFormat(getParseDateFormat(spec.getPayDtFormat().getId())); 
		if(spec.getCfld1ParsePattern() != null) 
			spec.setCfld1ParsePattern(getParsePattern(spec.getCfld1ParsePattern().getId())); 
		if(spec.getCfld2ParsePattern() != null) 
			spec.setCfld2ParsePattern(getParsePattern(spec.getCfld2ParsePattern().getId())); 
		if(spec.getPayCfld1ParsePattern() != null) 
			spec.setPayCfld1ParsePattern(getParsePattern(spec.getPayCfld1ParsePattern().getId())); 
		if(spec.getPayCfld2ParsePattern() != null) 
			spec.setPayCfld2ParsePattern(getParsePattern(spec.getPayCfld2ParsePattern().getId())); 
	} 
	 
	public void loadDynamicPayPeriodFileSpecification(long dynamicFileSpecificationID) 
	{ 
		try { 
			
			//compare with server, load if stale 
			DynamicPayPeriodFileSpecificationRequest request = new DynamicPayPeriodFileSpecificationRequest(); 
			request.setId(dynamicFileSpecificationID); 
			mDynamicPayPeriodFileSpecification = AdminPersistenceManager.getInstance().get(request); 

		} catch (CoreException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
	 
	public void loadDynamicCoverageFileSpecification(long dynamicFileSpecificationID) 
	{ 
		try { 
			
			//load from the server 
			DynamicCoverageFileSpecificationRequest request = new DynamicCoverageFileSpecificationRequest(); 
			request.setId(dynamicFileSpecificationID); 
			mDynamicCoverageFileSpecification = AdminPersistenceManager.getInstance().get(request);
			
			if (mDynamicCoverageFileSpecification == null) return; 
			getSubObjects(mDynamicCoverageFileSpecification); 
			 
		} catch (CoreException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
	 
	public void getSubObjects(DynamicCoverageFileSpecification spec) 
	{ 
		// expand sub data objects 
		if(spec.getAprSelTrueParsePattern() != null) 
			spec.setAprSelTrueParsePattern(getParsePattern(spec.getAprSelTrueParsePattern().getId())); 
		if(spec.getAugSelTrueParsePattern() != null) 
			spec.setAugSelTrueParsePattern(getParsePattern(spec.getAugSelTrueParsePattern().getId())); 
		if(spec.getCfld1ParsePattern() != null) 
			spec.setCfld1ParsePattern(getParsePattern(spec.getCfld1ParsePattern().getId())); 
		if(spec.getCfld2ParsePattern() != null) 
			spec.setCfld2ParsePattern(getParsePattern(spec.getCfld2ParsePattern().getId())); 
		if(spec.getCityParsePattern() != null) 
			spec.setCityParsePattern(getParsePattern(spec.getCityParsePattern().getId())); 
		if(spec.getCovCfld1ParsePattern() != null) 
			spec.setCovCfld1ParsePattern(getParsePattern(spec.getCovCfld1ParsePattern().getId())); 
		if(spec.getCovEndDtFormat() != null) 
			spec.setCovEndDtFormat(getParseDateFormat(spec.getCovEndDtFormat().getId())); 
		if(spec.getCovStartDtFormat() != null) 
			spec.setCovStartDtFormat(getParseDateFormat(spec.getCovStartDtFormat().getId())); 
		if(spec.getDecSelTrueParsePattern() != null) 
			spec.setDecSelTrueParsePattern(getParsePattern(spec.getDecSelTrueParsePattern().getId())); 
		if(spec.getDobFormat() != null) 
			spec.setDobFormat(getParseDateFormat(spec.getDobFormat().getId())); 
		if(spec.getfNameParsePattern() != null) 
			spec.setfNameParsePattern(getParsePattern(spec.getfNameParsePattern().getId())); 
		if(spec.getFebSelTrueParsePattern() != null) 
			spec.setFebSelTrueParsePattern(getParsePattern(spec.getFebSelTrueParsePattern().getId())); 
		if(spec.getHireDtFormat() != null) 
			spec.setHireDtFormat(getParseDateFormat(spec.getHireDtFormat().getId())); 
		 
		if(spec.getInelTrueParsePattern() != null) 
			spec.setInelTrueParsePattern(getParsePattern(spec.getInelTrueParsePattern().getId())); 
		if(spec.getJanSelTrueParsePattern() != null) 
			spec.setJanSelTrueParsePattern(getParsePattern(spec.getJanSelTrueParsePattern().getId())); 
		if(spec.getJulSelTrueParsePattern() != null) 
			spec.setJulSelTrueParsePattern(getParsePattern(spec.getJulSelTrueParsePattern().getId())); 
		if(spec.getJunSelTrueParsePattern() != null) 
			spec.setJunSelTrueParsePattern(getParsePattern(spec.getJunSelTrueParsePattern().getId())); 
		if(spec.getlNameParsePattern() != null) 
			spec.setlNameParsePattern(getParsePattern(spec.getlNameParsePattern().getId())); 
		if(spec.getmNameParsePattern() != null) 
			spec.setmNameParsePattern(getParsePattern(spec.getmNameParsePattern().getId())); 
		if(spec.getMarSelTrueParsePattern() != null) 
			spec.setMarSelTrueParsePattern(getParsePattern(spec.getMarSelTrueParsePattern().getId())); 
		if(spec.getMaySelTrueParsePattern() != null) 
			spec.setMaySelTrueParsePattern(getParsePattern(spec.getMaySelTrueParsePattern().getId())); 
		if(spec.getNovSelTrueParsePattern() != null) 
			spec.setNovSelTrueParsePattern(getParsePattern(spec.getNovSelTrueParsePattern().getId())); 
		if(spec.getOctSelTrueParsePattern() != null) 
			spec.setOctSelTrueParsePattern(getParsePattern(spec.getOctSelTrueParsePattern().getId())); 
		 
		if(spec.getDepCfld1ParsePattern() != null) 
			spec.setDepCfld1ParsePattern(getParsePattern(spec.getDepCfld1ParsePattern().getId())); 
		if(spec.getDepCfld2ParsePattern() != null) 
			spec.setDepCfld2ParsePattern(getParsePattern(spec.getDepCfld2ParsePattern().getId())); 
		if(spec.getDepCityParsePattern() != null) 
			spec.setDepCityParsePattern(getParsePattern(spec.getDepCityParsePattern().getId())); 
		if(spec.getDepDOBFormat() != null) 
			spec.setDepDOBFormat(getParseDateFormat(spec.getDepDOBFormat().getId())); 
		if(spec.getDepFNameParsePattern() != null) 
			spec.setDepFNameParsePattern(getParsePattern(spec.getDepFNameParsePattern().getId())); 
		if(spec.getDepLNameParsePattern() != null) 
			spec.setDepLNameParsePattern(getParsePattern(spec.getDepLNameParsePattern().getId())); 
		if(spec.getDepMNameParsePattern() != null) 
			spec.setDepMNameParsePattern(getParsePattern(spec.getDepMNameParsePattern().getId())); 
		if(spec.getDepSSNParsePattern() != null) 
			spec.setDepSSNParsePattern(getParsePattern(spec.getDepSSNParsePattern().getId())); 
		if(spec.getDepStateParsePattern() != null) 
			spec.setDepStateParsePattern(getParsePattern(spec.getDepStateParsePattern().getId())); 
		if(spec.getDepStreetParsePattern() != null) 
			spec.setDepStreetParsePattern(getParsePattern(spec.getDepStateParsePattern().getId())); 
		 
		if(spec.getDepZipParsePattern() != null) 
			spec.setDepZipParsePattern(getParsePattern(spec.getDepZipParsePattern().getId())); 
		if(spec.getSepSelTrueParsePattern() != null) 
			spec.setSepSelTrueParsePattern(getParsePattern(spec.getSepSelTrueParsePattern().getId())); 
		if(spec.getSsnParsePattern() != null) 
			spec.setSsnParsePattern(getParsePattern(spec.getSsnParsePattern().getId())); 
		if(spec.getStateParsePattern() != null) 
			spec.setStateParsePattern(getParsePattern(spec.getStateParsePattern().getId())); 
		if(spec.getStreetParsePattern() != null) 
			spec.setStreetParsePattern(getParsePattern(spec.getStreetParsePattern().getId())); 
		if(spec.getTermDtFormat() != null) 
			spec.setTermDtFormat(getParseDateFormat(spec.getTermDtFormat().getId())); 
		if(spec.getTyParsePattern() != null) 
			spec.setTyParsePattern(getParsePattern(spec.getTyParsePattern().getId())); 
		if(spec.getTySelTrueParsePattern() != null) 
			spec.setTySelTrueParsePattern(getParsePattern(spec.getTySelTrueParsePattern().getId())); 
		if(spec.getWavdTrueParsePattern() != null) 
			spec.setWavdTrueParsePattern(getParsePattern(spec.getWavdTrueParsePattern().getId())); 
		if(spec.getZipParsePattern() != null) 
			spec.setZipParsePattern(getParsePattern(spec.getZipParsePattern().getId())); 
		if(spec.getEeFlagTrueParsePattern() != null) 
			spec.setEeFlagTrueParsePattern(getParsePattern(spec.getEeFlagTrueParsePattern().getId())); 
		if(spec.getErRefParsePattern() != null) 
			spec.setErRefParsePattern(getParsePattern(spec.getErRefParsePattern().getId())); 
		if(spec.getPlanRefParsePattern() != null) 
			spec.setPlanRefParsePattern(getParsePattern(spec.getPlanRefParsePattern().getId())); 
		if(spec.getDepErRefParsePattern() != null) 
			spec.setDepErRefParsePattern(getParsePattern(spec.getDepErRefParsePattern().getId())); 
	} 
	 
	public void loadDynamicEmployeeFileSpecification(long dynamicFileSpecificationID) 
	{ 
		try {
			
			//load from the server 
			DynamicEmployeeFileSpecificationRequest request = new DynamicEmployeeFileSpecificationRequest(); 
			request.setId(dynamicFileSpecificationID); 
			mDynamicEmployeeFileSpecification = AdminPersistenceManager.getInstance().get(request);

			getSubObjects(mDynamicEmployeeFileSpecification); 

		} catch (CoreException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		} 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 
 
	public void getSubObjects(DynamicEmployeeFileSpecification spec) 
	{ 
		// expand data objects 
		if(spec.getErRefParsePattern() != null) 
			spec.setErRefParsePattern(getParsePattern(spec.getErRefParsePattern().getId())); 
		if(spec.getCityParsePattern() != null) 
			spec.setCityParsePattern(getParsePattern(spec.getCityParsePattern().getId())); 
		if(spec.getDobFormat() != null) 
			spec.setDobFormat(getParseDateFormat(spec.getDobFormat().getId())); 
		if(spec.getfNameParsePattern() != null) 
			spec.setfNameParsePattern(getParsePattern(spec.getfNameParsePattern().getId())); 
		if(spec.getHireDtFormat() != null) 
			spec.setHireDtFormat(getParseDateFormat(spec.getHireDtFormat().getId())); 
		if(spec.getlNameParsePattern() != null) 
			spec.setlNameParsePattern(getParsePattern(spec.getlNameParsePattern().getId())); 
		if(spec.getmNameParsePattern() != null) 
			spec.setmNameParsePattern(getParsePattern(spec.getmNameParsePattern().getId())); 
		if(spec.getRhireDtFormat() != null) 
			spec.setRhireDtFormat(getParseDateFormat(spec.getRhireDtFormat().getId())); 
		if(spec.getSsnParsePattern() != null) 
			spec.setSsnParsePattern(getParsePattern(spec.getSsnParsePattern().getId())); 
		if(spec.getStateParsePattern() != null) 
			spec.setStateParsePattern(getParsePattern(spec.getStateParsePattern().getId())); 
		if(spec.getStreetParsePattern() != null) 
			spec.setStreetParsePattern(getParsePattern(spec.getStreetParsePattern().getId())); 
		if(spec.getTermDtFormat() != null) 
			spec.setTermDtFormat(getParseDateFormat(spec.getTermDtFormat().getId())); 
		if(spec.getZipParsePattern() != null) 
			spec.setZipParsePattern(getParsePattern(spec.getZipParsePattern().getId())); 
		if(spec.getCfld1ParsePattern() != null) 
			spec.setCfld1ParsePattern(getParsePattern(spec.getCfld1ParsePattern().getId())); 
		if(spec.getCfld2ParsePattern() != null) 
			spec.setCfld2ParsePattern(getParsePattern(spec.getCfld2ParsePattern().getId())); 
	} 
	 
	 
	public void updatePayConversionCalculations()
	{ 
		try { 
			PayConversionCalculationRequest request = new PayConversionCalculationRequest(); 
			mPayConversionCalculations = AdminPersistenceManager.getInstance().getAll(request); 
		} catch (CoreException e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		}	 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	} 	 
	 
	public void loadPipelinePayFiles(Organization org) { 
		 
	} 
 
	public void saveCurrentUsername(String username) 
	{ 
		Properties props = new Properties();
		try 
		{ 
			props.setProperty(EmsApp.CFG_LEML, username != null ? username : "");
			EmsApp.getInstance().updateAppConfigProperties(props);
		} catch (Exception e) { DataManager.i().log(Level.SEVERE, e);  } 
	} 
 
	public void persistString(String key, String value) 
	{
		Properties props = new Properties();
		try 
		{ 
			props.setProperty(key, value != null ? value : "");
			EmsApp.getInstance().updateAppConfigProperties(props);
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e);  
		} 
	}
	
	public String getPersistedString(String key) 
	{
		String value = "";
		try { 
			value = EmsApp.getInstance().getProperties().getProperty(key); 
		} catch (CoreException e) { 
			logr.log(Level.SEVERE, "Exception.", e); 
		}
		
		return value; 
	}
	
	/////////////////////////////////////////////////////////////////////////// 
	// UPDATE Functions 
	/////////////////////////////////////////////////////////////////////////// 
 
	public void updateLocalUser(User user) 
	{ 
		//save the user 
		//saveUser(user); 
		 
		//update the display 
		EtcAdmin.i().updateLocalUserName(); 
	} 
	 
	public boolean ping()
	{ 
		try { 
 			// use the ping function to test our connectin with the server 
			return mCorvettoConnection.ping(); 
		} catch (CoreException  e) { 
        	DataManager.i().log(Level.SEVERE, e); 
		}  
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		return false; 
	} 
	
} 
