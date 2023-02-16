package com.etc.admin.ui.planyear;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.SystemDataType;
import com.etc.corvetto.entities.CoverageGroup;
import com.etc.corvetto.entities.PlanYearOfferingBenefit;
import com.etc.entities.CoreData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
//import com.etclite.core.data.EmployerCoveragePeriod;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewPlanYearController 
{
	// plan year
	@FXML
	private DatePicker pyStartDate;
	@FXML
	private DatePicker pyEndDate;
	@FXML
	private DatePicker pyImplementedOnDate;
	@FXML
	private CheckBox pyImplemented;
	@FXML
	private CheckBox pySh2b;
	@FXML
	private CheckBox pyDh2d;
	@FXML
	private CheckBox pyAffordable;
	@FXML
	private TextField pyImplementedBy;
	@FXML
	private TextField pyMemberShare;
	@FXML
	private TextField pyInsuranceType;
	
	
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private TextField planOfferCodeLabel;
	@FXML
	private TextField planNameLabel;
	@FXML
	private TextField planDescriptionLabel;
	@FXML
	private TextField planInsuranceTypeLabel;
	@FXML
	private TextField planPlanTypeLabel;
	@FXML
	private TextField planPlanProviderLabel;
	@FXML
	private TextField planCoverageTypeLabel;
	@FXML
	private TextField planPlanReferenceLabel;
	@FXML
	private CheckBox planWaivedCheckBox;
	@FXML
	private CheckBox planIneligibleCheckBox;
	@FXML
	private Button addPYO;
	@FXML
	private Button addCovCls1;
	@FXML
	private Button addCovCls2;
	@FXML
	private Button addCovCls3;
	@FXML
	private Button addCovCls4;
	@FXML
	private Button addPln1;
	@FXML
	private Button addPln2;
	@FXML
	private Button addPln3;
	@FXML
	private Button addPln4;
	@FXML
	private Button close;
	// PYOP 1
	@FXML
	private GridPane planPYOP1Grid;
	@FXML
	private ListView<HBoxCoverageClassCell> covListView1;
	@FXML
	private ListView<HBoxPlanYearCell> planPlansListView1;
	@FXML
	private ListView<HBoxCoverageClassCell> covListView2;
	@FXML
	private ListView<HBoxPlanYearCell> planPlansListView2;
	@FXML
	private ListView<HBoxCoverageClassCell> covListView3;
	@FXML
	private ListView<HBoxPlanYearCell> planPlansListView3;
	@FXML
	private ListView<HBoxCoverageClassCell> covListView4;
	@FXML
	private ListView<HBoxPlanYearCell> planPlansListView4;
	@FXML
	private GridPane planPlansGrid;
	@FXML
	private GridPane planPlansGrid1;
	@FXML
	private GridPane planPlansGrid11;
	@FXML
	private GridPane planPlansGrid111;
	@FXML
	private Label planPlansLabel;
	@FXML
	private CheckBox planAffordableCheckBox;


	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() 
	{
		initControls();
		showPlanYear();
		//load the list of coverage periods for this plan
		loadCoverageClasses();
		loadCoverageClassData();
//		loadPlanYearOfferingPlanData();
//		loadPlanYearOfferingPlans();
		addCovCls1.setDisable(false);
		addPln1.setDisable(false);
		planPlansGrid1.setVisible(false);
		planPlansGrid11.setVisible(false);
		planPlansGrid111.setVisible(false);
	}

	private void initControls() 
	{
		enablePlanYearControls(false);
		//add handlers for listbox selection notification
		// Plans
		addPYO.setDisable(false);
		addPYO.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1)
            {
				// offset one for the header row
//            	planPlansGrid1.setVisible(true);
            	
//				DataManager.i().setEmployee(planPlansListView.getSelectionModel().getSelectedIndex() - 1);
            }
        });
		covListView1.setOnMouseClicked(mouseClickedEvent -> 
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1)
            {
				// offset one for the header row
//            	viewSelectedCoverageClasses();
            	viewMockMapperForm();
//				DataManager.i().setCoverageClass(covListView1.getSelectionModel().getSelectedIndex() - 1);
            }
        });
		planPlansListView1.setOnMouseClicked(mouseClickedEvent ->
		{
            if(mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() > 1) 
            {
				// offset one for the header row
            	viewSelectedPlanYearOfferingPlans();
//				 DataManager.i().setPlanYear(planPlansListView1.getSelectionModel().getSelectedIndex() - 1);
            }
        });
//		DataManager.i().loadPlanYearOffering(0);
	}
	
	private void enablePlanYearControls(boolean state)
	{
		pyStartDate.setEditable(state);
		pyEndDate.setEditable(state);
		pyImplementedOnDate.setEditable(state);
		pyImplemented.setDisable(!state);
		pySh2b.setDisable(!state);
		pyDh2d.setDisable(!state);
		pyAffordable.setDisable(!state);
		pyImplementedBy.setEditable(state);
		pyMemberShare.setEditable(state);
		pyInsuranceType.setEditable(state);		
	}
	
	private void loadPlanYearOfferingPlanData() 
	{
		loadPlanYearOfferingPlans();
		// create a thread to handle the update
		Task<Void> task = new Task<Void>() 
		{
            @Override
            protected Void call() throws Exception 
            {
            	//DataManager.i().loadPlanYearOfferingPlans(null);
                return null;
            }
        };
        
      	task.setOnScheduled(e ->  
      	{
      		EtcAdmin.i().setStatusMessage("Loading Plan Year Offering Plans...");
      		EtcAdmin.i().setProgress(0.25);
      	});
      			
    	task.setOnSucceeded(e ->  loadCoverageClassData());
    	task.setOnFailed(e ->  loadCoverageClassData());
        new Thread(task).start();
	}
	
	private void loadCoverageClassData() 
	{
		loadCoverageClasses();
		// create a thread to handle the update
		Task<Void> task = new Task<Void>() 
		{
            @Override
            protected Void call() throws Exception 
            {
            	//DataManager.i().loadCoverageGroups();
                return null;
            }
        };
        
      	task.setOnScheduled(e ->  
      	{
      		EtcAdmin.i().setStatusMessage("Loading Coverage Classes...");
      		EtcAdmin.i().setProgress(0.25);
      	});
      			
        new Thread(task).start();
	}

	private void showPlanYear()
	{
		
		if(DataManager.i().mPlanYear != null) {
			Utils.updateControl(pyStartDate,DataManager.i().mPlanYear.getStartDate());
			Utils.updateControl(pyEndDate,DataManager.i().mPlanYear.getEndDate());
			Utils.updateControl(pyImplementedOnDate,DataManager.i().mPlanYear.getImplementedOn());
			Utils.updateControl(pyImplemented,DataManager.i().mPlanYear.isImplemented());
			Utils.updateControl(pySh2b,DataManager.i().mPlanYear.isSh2b());
			Utils.updateControl(pyDh2d,DataManager.i().mPlanYear.isDh2d());
			Utils.updateControl(pyAffordable,DataManager.i().mPlanYear.isAffordable());
			if (DataManager.i().mPlanYear.getImplementedBy() != null && 
				DataManager.i().mPlanYear.getImplementedBy().getFirstName() != null && 
				DataManager.i().mPlanYear.getImplementedBy().getLastName() != null)
					Utils.updateControl(pyImplementedBy,DataManager.i().mPlanYear.getImplementedBy().getFirstName() + " " + DataManager.i().mPlanYear.getImplementedBy().getLastName().substring(0, 1));
			Utils.updateControl(pyMemberShare,DataManager.i().mPlanYear.getMemberShare());
			if (DataManager.i().mPlanYear.getInsuranceType() != null)
				Utils.updateControl(pyInsuranceType,DataManager.i().mPlanYear.getInsuranceType().toString());
		} 
	}
	
	private int clicks = 0;

	@FXML
	private void onShowSystemInfo()
	{
		try {
			// set the coredata
			DataManager.i().mCoreData = (CoreData) DataManager.i().mPlanYear;
			DataManager.i().mCurrentCoreDataType = SystemDataType.PLANYEAR;
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/coresysteminfo/ViewCoreSystemInfo.fxml"));
			Parent ControllerNode = loader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch (Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		}        		
	}

	@FXML
	private void onPYOEdit(ActionEvent event) 
	{
		clicks++;
	
		if(clicks == 1) { planPlansGrid1.setVisible(true); }
		if(clicks == 2) { planPlansGrid11.setVisible(true); }
		if(clicks == 3) { planPlansGrid111.setVisible(true); }
//		EtcAdmin.i().setScreen(ScreenType.PLANEDIT, true);
	}	
	
	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PLANEDIT, true);
	}	
	
	@FXML
	private void onClose(ActionEvent event) 
	{
		Stage stage =(Stage) anchorPane.getScene().getWindow();
		stage.close();
	}	
	
	@FXML
	private void onAddPlan(ActionEvent event) 
	{
		viewSelectedPlanYearOfferingPlans();
//		EtcAdmin.i().setScreen(ScreenType.PLANEDIT, true);
	}	
	
	@FXML
	private void onAddCov(ActionEvent event)
	{
		viewSelectedCoverageClasses();
//		EtcAdmin.i().setScreen(ScreenType.PLANEDIT, true);
	}	
	
	//update the coverage period list
	private void loadCoverageClasses() 
	{
		if(DataManager.i().mEmployer.getCoverageGroups() != null)
		{
			
			List<HBoxCoverageClassCell> covClsList = new ArrayList<>();
			covClsList.add(new HBoxCoverageClassCell(null));
	
			for(CoverageGroup covCls : DataManager.i().mEmployer.getCoverageGroups()) 
			{
				if(covCls == null) continue;
				covClsList.add(new HBoxCoverageClassCell(covCls)); 
			}	

			ObservableList<HBoxCoverageClassCell> myObservableCovClsList = FXCollections.observableList(covClsList);
			covListView1.setItems(myObservableCovClsList);		
	        //Reset progress
			EtcAdmin.i().setStatusMessage("Ready");
	  		EtcAdmin.i().setProgress(0);
		}

//		if(DataManager.i().mEmployer.getCoverageClasses() != null)
//		{
//		    List<HBoxCoveragePeriodCell> periodList = new ArrayList<>();
//			
//		    periodList.add(new HBoxCoveragePeriodCell("Name", "Description", "Labor Union", "Affordable", "Pay Code Type", "Employer", null, 0));
//			CoverageClass coveragePeriod = null;
//			
//			for(int i = 0; i < DataManager.i().mPlan.getEmployerCoveragePeriods().size();i++) {
//				coveragePeriod = DataManager.i().mPlan.getEmployerCoveragePeriods() .get(i);
//				if(coveragePeriod == null) continue;
//				
//				String sStartDate = "";
//				if(coveragePeriod.getPlanStartDate() != null)
//					sStartDate = sdf.format(coveragePeriod.getPlanStartDate().getTime());
//				
//				String sEndDate = "";
//				if(coveragePeriod.getPlanEndDate() != null)
//					sEndDate = sdf.format(coveragePeriod.getPlanEndDate().getTime());
//				
//				periodList.add(new HBoxCoveragePeriodCell(coveragePeriod.getName(), sStartDate, sEndDate, "View", i));
//			};	
//			
//			ObservableList<HBoxCoveragePeriodCell> myObservablePeriodList = FXCollections.observableList(periodList);
//			planEmployerCoveragePeriodsListView.setItems(myObservablePeriodList);		
//			
//			//update our employer screen label
//			planEmployerCoveragePeriodLabel.setText("Employer Coverage Periods(total: " + String.valueOf(DataManager.i().mPlan.getEmployerCoveragePeriods().size()) + ")" );
//		} else {
//			planEmployerCoveragePeriodLabel.setText("Employer Coverage Periods(total: 0)");			
//		}
//		
	}	

	//update the coverage period list
	private void loadPlanYearOfferingPlans() 
	{
		if(DataManager.i().mPlanYearOffering.getPlanYearOfferingBenefits() != null || DataManager.i().mPlanYearOffering.getPlanYearOfferingBenefits().size() == 0)
		{
			List<HBoxPlanYearCell> pyopList = new ArrayList<>();
				
			pyopList.add(new HBoxPlanYearCell(null));
	
			for(PlanYearOfferingBenefit pyop : DataManager.i().mPlanYearOffering.getPlanYearOfferingBenefits()) 
			{
				if(pyop == null) continue;
				
				pyopList.add(new HBoxPlanYearCell(pyop)); 
			}	
			
			ObservableList<HBoxPlanYearCell> myObservablePyopList = FXCollections.observableList(pyopList);
			planPlansListView1.setItems(myObservablePyopList);		
	
	        //Reset progress
			EtcAdmin.i().setStatusMessage("Ready");
	  		EtcAdmin.i().setProgress(0);
		}
	}	

	private void viewSelectedCoverageClasses() 
	{
		// offset one for the header row
		DataManager.i().mCoverageClass = DataManager.i().mCoverageClasses.get(covListView1.getSelectionModel().getSelectedIndex() - 1);
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/plancoverageclass/ViewPlanCoverageClass.fxml"));
			Parent ControllerNode = loader.load();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch(IOException e) { DataManager.i().log(Level.SEVERE, e); }        
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	

	private void viewSelectedPlanYearOfferingPlans() 
	{
		// offset one for the header row
		DataManager.i().mPlanYearOffering = DataManager.i().mPlanYearOfferings.get(planPlansListView1.getSelectionModel().getSelectedIndex() - 1);
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/planyearofferingplan/ViewPlanYearOfferingPlan.fxml"));
			Parent ControllerNode = loader.load();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch(IOException e) { DataManager.i().log(Level.SEVERE, e); }        
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	

	private void viewMockMapperForm() 
	{
		// offset one for the header row
//		DataManager.i().loadPlanYearOffering(planPlansListView1.getSelectionModel().getSelectedIndex() - 1);
        try {
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/pipeline/mapper/ViewMapperCoverageForm.fxml"));
			Parent ControllerNode = loader.load();

	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.initStyle(StageStyle.UNDECORATED);
	        stage.setScene(new Scene(ControllerNode));  
	        EtcAdmin.i().positionStageCenter(stage);
	        stage.showAndWait();
		} catch(IOException e) { DataManager.i().log(Level.SEVERE, e); }        
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}	

	//extending the listview for our additional controls
	public class HBoxPlanYearCell extends HBox 
	{
         Label memberShare = new Label();
         Label planYearOfferingId = new Label();
         Label offerCode = new Label();
         PlanYearOfferingBenefit pyop;

         public PlanYearOfferingBenefit getPlanYearOfferingPlan() {
        	 return pyop;
         }

         HBoxPlanYearCell(PlanYearOfferingBenefit pyop) 
         {
              super();

              this.pyop = pyop;
              if(pyop != null) 
              {
               	  Utils.setHBoxLabel(memberShare, 100, false, pyop.getMemberShare());
               	  Utils.setHBoxLabel(planYearOfferingId, 140, false, pyop.getPlanYearOffering().toString());
               	 // Utils.setHBoxLabel(offerCode, 150, false, pyop.getOfferCode().toString());
              }else {
               	  Utils.setHBoxLabel(memberShare, 100, true, "Member Share");
               	  Utils.setHBoxLabel(planYearOfferingId, 140, true, "Plan Year Offering");
               	  Utils.setHBoxLabel(offerCode, 150, true, "Offer Code");
              }

         this.getChildren().addAll(memberShare, planYearOfferingId, offerCode);
         }
	}

	//extending the listview for our additional controls
	public class HBoxCoverageClassCell extends HBox 
	{
         Label name = new Label();
         Label description = new Label();
         Label laborUnion = new Label();
         Label affordable = new Label();
         Label payCodeType = new Label();
         Label employerId = new Label();
         CoverageGroup covcls;

         public CoverageGroup getCoverageClass() {
        	 return covcls;
         }

         HBoxCoverageClassCell(CoverageGroup covcls) 
         {
              super();

              this.covcls = covcls;
              if(covcls != null) 
              {
               	  Utils.setHBoxLabel(name, 100, false, covcls.getName());
               	  Utils.setHBoxLabel(description, 140, false, covcls.getDescription());
               	  Utils.setHBoxLabel(laborUnion, 150, false, Boolean.toString(covcls.isLaborUnion()));
               	  Utils.setHBoxLabel(affordable, 150, false, Boolean.toString(covcls.isAffordable()));

               	  if (covcls.getPayCode() != null)
               		  Utils.setHBoxLabel(payCodeType, 150, false, covcls.getPayCode().toString());
               	  else
               		  Utils.setHBoxLabel(payCodeType, 150, false, "");
               	  if (covcls.getEmployer() != null)  
               		  Utils.setHBoxLabel(employerId, 150, false, covcls.getEmployer().getId().toString());//set id explicitly
               	  else
               		  Utils.setHBoxLabel(employerId, 150, false, "");//set id explicitly
               		  
              }else {
               	  Utils.setHBoxLabel(name, 100, true, "Name");
               	  Utils.setHBoxLabel(description, 140, true, "Description");
               	  Utils.setHBoxLabel(laborUnion, 150, true, "Labor Union");
               	  Utils.setHBoxLabel(affordable, 150, true, "Affordable");
               	  Utils.setHBoxLabel(payCodeType, 150, true, "Pay Code Type");
               	  Utils.setHBoxLabel(employerId, 150, true, "Employer");
              }

         this.getChildren().addAll(name, description, laborUnion, affordable, payCodeType, employerId);
         }
	}

	//extending the listview for our additional controls
//	public static class HBoxCoveragePeriodCell extends HBox
//	{
//         Label name = new Label();
//         Label description = new Label();
//         Label laborUnion = new Label();
//         Button btnView = new Button();
//
//         HBoxCoveragePeriodCell(String sName, String sStartDate, String sEndDate, String sButtonText, int nButtonID) 
//         {
//              super();
//
//              if(sName == null ) sName = "";
//              if(sStartDate == null ) sStartDate = "";
//              if(sEndDate == null ) sEndDate = "";
//              
//              lblName.setText(sName);
//              lblName.setMinWidth(270);
//              lblName.setMaxWidth(270);
//              lblName.setPrefWidth(270);
//              HBox.setHgrow(lblName, Priority.ALWAYS);
//
//              lblStartDate.setText(sStartDate);
//              lblStartDate.setMinWidth(100);
//              lblStartDate.setMaxWidth(100);
//              lblStartDate.setPrefWidth(100);
//              HBox.setHgrow(lblStartDate, Priority.ALWAYS);
//
//              lblEndDate.setText(sEndDate);
//              lblEndDate.setMinWidth(300);
//              lblEndDate.setMaxWidth(300);
//              lblEndDate.setPrefWidth(300);
//              HBox.setHgrow(lblEndDate, Priority.ALWAYS);
//
//              if(sButtonText != null) 
//              {
//            	  btnView.setText(sButtonText);
//            	  btnView.setId(String.valueOf(nButtonID));
//              }
//              
//              if(sButtonText == null) {
//            	  lblName.setFont(Font.font(null, FontWeight.BOLD, 13));
//            	  lblStartDate.setFont(Font.font(null, FontWeight.BOLD, 13));
//            	  lblEndDate.setFont(Font.font(null, FontWeight.BOLD, 13));
//            	  this.getChildren().addAll(lblName, lblStartDate, lblEndDate);
//              }else {
//            	  this.getChildren().addAll(lblName, lblStartDate, lblEndDate, btnView);
//            	  
//            	  //add an event handler for this button
//            	  btnView.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            		  
//            		  @Override
//            		  public void handle(MouseEvent event) {
//            			  // let the application load the current employer
//            			  //DataManager.i().setEmployerCoveragePeriod(Integer.parseInt(btnView.getId()));
//            		  }
//            	  });
//              }
//         }
//    }	
}
