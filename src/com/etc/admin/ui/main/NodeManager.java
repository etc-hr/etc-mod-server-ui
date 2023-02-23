package com.etc.admin.ui.main;

import com.etc.admin.ui.home.HomeController;
import com.etc.admin.ui.localuser.ViewLocalUserController;
import com.etc.admin.ui.user.ViewUserAddController;
import com.etc.admin.ui.user.ViewUserController;
import com.etc.admin.ui.user.ViewUserEditController;

import javafx.scene.Parent;

public class NodeManager {

	// controllers
	public HomeController homeController = null;
	public ViewLocalUserController viewLocalUserController = null;
	public ViewUserController viewUserController = null;
	public ViewUserEditController viewUserEditController = null;
	public ViewUserAddController viewUserAddController = null;
	
	// nodes
	public Parent homeControllerNode = null;
	public Parent viewHRAccountControllerNode = null;
	public Parent viewHREmployerControllerNode = null;
	public Parent viewHREmployeeControllerNode = null;
	public Parent viewEtcFilingFailureRptControllerNode = null;
	public Parent viewRowIgnoreControllerNode = null;
	public Parent viewCalcQueueControllerNode = null;
	public Parent viewDWIQueueControllerNode = null;
	public Parent viewDocumentControllerNode = null;
	public Parent viewExportQueueControllerNode = null;
	public Parent viewPipelineQueueControllerNode = null;
	public Parent viewPipelineQueueDetailControllerNode = null;
	public Parent viewPipelineChannelControllerNode = null;
	public Parent viewPipelineSpecificationControllerNode = null;
	public Parent viewDynamicFileSpecificationControllerNode = null;
	public Parent viewMapperCoverageFormControllerNode = null;
	public Parent viewMapperDeductionFormControllerNode = null;
	public Parent viewMapperEmployeeFormControllerNode = null;
	public Parent viewMapperInsuranceFormControllerNode = null;
	public Parent viewMapper1095cFormControllerNode = null;
	public Parent viewMapperPayFileFormControllerNode = null;
	public Parent viewDynamicFileSpecificationAddControllerNode = null;
	public Parent viewDynamicFileSpecificationEditControllerNode = null;
	public Parent viewParsePatternControllerNode = null;
	public Parent viewParsePatternAddControllerNode = null;
	public Parent viewParsePatternEditControllerNode = null;
	public Parent viewParseDateFormatControllerNode = null;
	public Parent viewParseDateFormatEditControllerNode = null;
	public Parent viewParseDateFormatAddControllerNode = null;
	public Parent viewAccountControllerNode = null;
	public Parent viewAccountEditControllerNode = null;
	public Parent viewAccountAddControllerNode = null;
	public Parent viewAirErrorControllerNode = null;
	public Parent viewAirErrorEditControllerNode = null;
	public Parent viewAirFilingEventControllerNode = null;
	public Parent viewAirFilingEventEditControllerNode = null;
	public Parent viewAirStatusRequestControllerNode = null;
	public Parent viewAirStatusRequestEditControllerNode = null;
	public Parent viewAirTransmissionControllerNode = null;
	public Parent viewAirTransmissionEditControllerNode = null;
	public Parent viewEmployeeMergeControllerNode = null;
	public Parent viewEmployerControllerNode = null;
	public Parent viewEmployerEditControllerNode = null;
	public Parent viewEmployerAddControllerNode = null;
	public Parent viewEmployeeControllerNode = null;
	public Parent viewEmployeeEditControllerNode = null;
	public Parent viewEmployeeAddControllerNode = null;
	public Parent viewSecondaryControllerNode = null;
	public Parent viewSecondaryEditControllerNode = null;
	public Parent viewSecondaryAddControllerNode = null;
	public Parent viewLocalUserControllerNode = null;
	public Parent viewLocalUserEditControllerNode = null;
	public Parent viewUserControllerNode = null;
	public Parent viewUserEditControllerNode = null;
	public Parent viewUserAddControllerNode = null;
	public Parent viewAccountUserControllerNode = null;
	public Parent viewAccountUserEditControllerNode = null;
	public Parent viewPayPeriodControllerNode = null;
	public Parent viewTaxYearControllerNode = null;
	public Parent viewTaxYearEditControllerNode = null;
	public Parent viewTaxYearAddControllerNode = null;
	public Parent viewTaxMonthControllerNode = null;
	public Parent viewTaxMonthEditControllerNode = null;
	public Parent viewTaxMonthAddControllerNode = null;
	public Parent viewDepartmentControllerNode = null;
	public Parent viewDepartmentEditControllerNode = null;
	public Parent viewDepartmentAddControllerNode = null;
	public Parent viewEmployeeCoveragePeriodControllerNode = null;
	public Parent viewEmployeeCoveragePeriodEditControllerNode = null;
	public Parent viewEmployeeCoveragePeriodAddControllerNode = null;
	public Parent viewEmployerEligibilityPeriodControllerNode = null;
	public Parent viewEmployerPayFileControllerNode = null;
	public Parent viewEmploymentPeriodControllerNode = null;
	public Parent viewEmploymentPeriodEditControllerNode = null;
	public Parent viewEmploymentPeriodAddControllerNode = null;
	public Parent viewAssociatedPropertyControllerNode = null;
	public Parent viewAssociatedPropertyEditControllerNode = null;
	public Parent viewContactControllerNode = null;
	public Parent viewContactEditControllerNode = null;
	public Parent viewContactAddControllerNode = null;
	public Parent viewPlanControllerNode = null;
	public Parent viewPlanEditControllerNode = null;
	public Parent viewPlanAddControllerNode = null;
	public Parent viewPlanProviderControllerNode = null;
	public Parent viewPlanProviderEditControllerNode = null;
	public Parent viewPlanProviderAddControllerNode = null;
	public Parent viewPlanSponsorControllerNode = null;
	public Parent viewPlanSponsorEditControllerNode = null;
	public Parent viewPlanSponsorAddControllerNode = null;
	public Parent viewPlanCarrierControllerNode = null;
	public Parent viewPlanCarrierEditControllerNode = null;
	public Parent viewPlanCarrierAddControllerNode = null;
	public Parent viewPlanYearOfferingControllerNode = null;
	public Parent viewPlanYearOfferingEditControllerNode = null;
	public Parent viewPlanYearOfferingAddControllerNode = null;
	public Parent viewPlanYearOfferingPlanControllerNode = null;
	public Parent viewPlanYearOfferingPlanEditControllerNode = null;
	public Parent viewPlanYearOfferingPlanAddControllerNode = null;
	public Parent viewPlanCoverageClassControllerNode = null;
	public Parent viewPlanCoverageClassEditControllerNode = null;
	public Parent viewPlanCoverageClassAddControllerNode = null;
	public Parent viewPipelineChannelEditControllerNode = null;
	public Parent viewPipelineChannelAddControllerNode = null;
	public Parent viewPipelineSpecificationEditControllerNode = null;
	public Parent viewPipelineSpecificationAddControllerNode = null;
	public Parent viewPipelinePayFileControllerNode = null;
	public Parent viewPipelinePayFileAddControllerNode = null;
	public Parent viewPipelinePayFileEditControllerNode = null;
	public Parent viewPipelineEmployeeFileControllerNode = null;
	public Parent viewPipelineEmployeeFileEditControllerNode = null;
	public Parent viewPipelineEmployeeFileAddControllerNode = null;
	public Parent viewPipelineCoverageFileControllerNode = null;
	public Parent viewPipelineCoverageFileEditControllerNode = null;
	public Parent viewPipelineCoverageFileAddControllerNode = null;
	public Parent viewPipelineIRS1094CFileControllerNode = null;
	public Parent viewPipelineIRS1095CFileControllerNode = null;
	public Parent viewPipelineRawFieldGridControllerNode = null;
	public Parent viewPipelineRawPayControllerNode = null;
	public Parent viewFileStepHandlerControllerNode = null;
	public Parent viewFileStepHandlerEditControllerNode = null;
	public Parent viewFileStepHandlerAddControllerNode = null;
	public Parent viewPipelineIRS1094CFileEditControllerNode = null;
	public Parent viewPipelineIRS1095CFileEditControllerNode = null;
	public Parent uploadFormNode = null;

}
