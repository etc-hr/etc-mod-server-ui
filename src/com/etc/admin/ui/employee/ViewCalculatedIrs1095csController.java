package com.etc.admin.ui.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.EmsApp;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.ems.calc.entities.aca.CalculatedIrs1095c;
import com.etc.corvetto.ems.calc.rqs.aca.CalculatedIrs1095cRequest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewCalculatedIrs1095csController 
{
	// secondaries
	@FXML
	private Label topLabel;
	@FXML
	private ListView<HBoxCalcIrs1095cCell> listView;
	@FXML
	private CheckBox inactiveCIsCheck;
	
	//int to track any pending threads, used to properly update the progress and message
	int waitingToComplete = 0;
	
	List<CalculatedIrs1095c> calc1095cs = null;
	/**
	 * initialize is called when the FXML is loaded
	 */
	
	@FXML
	public void initialize() 
	{
		initControls();
		updateCalcData();
	}
	
	private void initControls() 
	{

	}

	public void updateCalcData()
	{
		try
		{
			listView.getItems().clear();
			topLabel.setText("Calculated IRS1095cs (loading ...)"); 
			Task<Void> task = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					
	          		CalculatedIrs1095cRequest calcRequest = new CalculatedIrs1095cRequest();
	          		calcRequest.setEmployeeId(DataManager.i().mEmployee.getId());
	          		calc1095cs = AdminPersistenceManager.getInstance().getAll(calcRequest);
					return null;
				}
				
			};
			
			task.setOnScheduled(e -> {
				EtcAdmin.i().updateStatus(EtcAdmin.i().getProgress() + 0.15, "Updating CalcIrs1095c Data...");
				waitingToComplete++;
			});
			task.setOnSucceeded(e -> { 
				if(waitingToComplete-- == 1)
	    			EtcAdmin.i().setProgress(0.0);
				showCalcIrs1095cs(); 
			});
			task.setOnFailed(e -> { 
				EtcAdmin.i().setProgress(0.0);
		    	DataManager.i().log(Level.SEVERE,task.getException());
				showCalcIrs1095cs(); 
			});
			
			EmsApp.getInstance().getFxQueue().put(task);
		}catch(InterruptedException e) 
		{
			DataManager.i().log(Level.SEVERE, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	}

	private void showCalcIrs1095cs() 
	{
		if (calc1095cs != null) {
			List<HBoxCalcIrs1095cCell> list = new ArrayList<>();
			if(calc1095cs.size() > 0)
				list.add(new HBoxCalcIrs1095cCell(null));
			
			for(CalculatedIrs1095c calc1095c : calc1095cs) {
		    	if(calc1095c.isActive() == false && inactiveCIsCheck.isSelected() == false) continue;
		    	if(calc1095c.isDeleted() == true && inactiveCIsCheck.isSelected() == false) continue;
				list.add(new HBoxCalcIrs1095cCell(calc1095c));
			}
			
	        ObservableList<HBoxCalcIrs1095cCell> myObservableList = FXCollections.observableList(list);
	        listView.setItems(myObservableList);		
			
	        topLabel.setText("Calculated Irs1095c (total: " + String.valueOf(listView.getItems().size() - 1) + ")" );
		 } else {
			topLabel.setText("Calculated Irs1095c (total: 0)");
		 }
	}
	
	@FXML
	private void onShowInactiveCIs() {
		showCalcIrs1095cs();
	}		

	@FXML
	private void onClose(ActionEvent event) {
		exitPopup();
	}	
	
	private void exitPopup() 
	{
		Stage stage = (Stage) listView.getScene().getWindow();
		stage.close();
	}
	
 	public static class HBoxCalcIrs1095cCell extends HBox 
	{
 		Label id = new Label();
 		
 		Label empdMatrix = new Label();
 		Label subtMatrix = new Label();
 		Label lnapMatrix = new Label();
 		Label fteeMatrix = new Label();
 		Label dh2dMatrix = new Label();
 		Label inelMatrix = new Label();
 		Label wavdMatrix = new Label();
 		Label cbraMatrix = new Label();
 		Label unonMatrix = new Label();
 		Label slfiMatrix = new Label();
 		Label mincMatrix = new Label();
 		Label sh2bMatrix = new Label();
 		Label py2bMatrix = new Label();
 		Label pcodMatrix = new Label();
 		Label afdbMatrix = new Label();

 		Label janCovered = new Label();
 		Label febCovered = new Label();
 		Label marCovered = new Label();
 		Label aprCovered = new Label();
 		Label mayCovered = new Label();
 		Label junCovered = new Label();
 		Label julCovered = new Label();
 		Label augCovered = new Label();
 		Label sepCovered = new Label();
 		Label octCovered = new Label();
 		Label novCovered = new Label();
 		Label decCovered = new Label();

 		CheckBox janCoveredCB = new CheckBox();
 		CheckBox febCoveredCB = new CheckBox();
 		CheckBox marCoveredCB = new CheckBox();
 		CheckBox aprCoveredCB = new CheckBox();
 		CheckBox mayCoveredCB = new CheckBox();
 		CheckBox junCoveredCB = new CheckBox();
 		CheckBox julCoveredCB = new CheckBox();
 		CheckBox augCoveredCB = new CheckBox();
 		CheckBox sepCoveredCB = new CheckBox();
 		CheckBox octCoveredCB = new CheckBox();
 		CheckBox novCoveredCB = new CheckBox();
 		CheckBox decCoveredCB = new CheckBox();

 		Label janOfferCode = new Label();
 		Label febOfferCode = new Label();
 		Label marOfferCode = new Label();
 		Label aprOfferCode = new Label();
 		Label mayOfferCode = new Label();
 		Label junOfferCode = new Label();
 		Label julOfferCode = new Label();
 		Label augOfferCode = new Label();
 		Label sepOfferCode = new Label();
 		Label octOfferCode = new Label();
 		Label novOfferCode = new Label();
 		Label decOfferCode = new Label();

 		Label janEEShare = new Label();
 		Label febEEShare = new Label();
 		Label marEEShare = new Label();
 		Label aprEEShare = new Label();
 		Label mayEEShare = new Label();
 		Label junEEShare = new Label();
 		Label julEEShare = new Label();
 		Label augEEShare = new Label();
 		Label sepEEShare = new Label();
 		Label octEEShare = new Label();
 		Label novEEShare = new Label();
 		Label decEEShare = new Label();

 		Label janSafeHarborCode = new Label();
 		Label febSafeHarborCode = new Label();
 		Label marSafeHarborCode = new Label();
 		Label aprSafeHarborCode = new Label();
 		Label maySafeHarborCode = new Label();
 		Label junSafeHarborCode = new Label();
 		Label julSafeHarborCode = new Label();
 		Label augSafeHarborCode = new Label();
 		Label sepSafeHarborCode = new Label();
 		Label octSafeHarborCode = new Label();
 		Label novSafeHarborCode = new Label();
 		Label decSafeHarborCode = new Label();

 		Label selfInsured = new Label();
 		CheckBox selfInsuredCB = new CheckBox();
 		Label irs10945cCalculationId = new Label();
 		Label calculatedIrs1094cId = new Label();
 		Label irs1095cId = new Label();
 		
 		CalculatedIrs1095c calc1095c;
         
         public CalculatedIrs1095c getCalculatedIrs1095c() {
        	 return calc1095c;
         }
         
         HBoxCalcIrs1095cCell(CalculatedIrs1095c calc1095c) 
         {
        	 super();
    		 try {
             this.calc1095c = calc1095c;

             if (calc1095c != null) {
	        	 Utils.setHBoxLabel(empdMatrix, 75, false, calc1095c.getEmpdMatrix());
	        	 Utils.setHBoxLabel(subtMatrix, 75, false, calc1095c.getSubtMatrix());
	        	 Utils.setHBoxLabel(lnapMatrix, 75, false, calc1095c.getLnapMatrix());
	        	 Utils.setHBoxLabel(fteeMatrix, 75, false, calc1095c.getFteeMatrix());
	        	 Utils.setHBoxLabel(dh2dMatrix, 75, false, calc1095c.getDh2dMatrix());
	        	 Utils.setHBoxLabel(inelMatrix, 75, false, calc1095c.getInelMatrix());
	        	 Utils.setHBoxLabel(wavdMatrix, 75, false, calc1095c.getWavdMatrix());
	        	 Utils.setHBoxLabel(cbraMatrix, 75, false, calc1095c.getCbraMatrix());
	        	 Utils.setHBoxLabel(unonMatrix, 75, false, calc1095c.getUnonMatrix());
	        	 Utils.setHBoxLabel(slfiMatrix, 75, false, calc1095c.getSlfiMatrix());
	        	 Utils.setHBoxLabel(mincMatrix, 75, false, calc1095c.getMincMatrix());
	        	 Utils.setHBoxLabel(sh2bMatrix, 75, false, calc1095c.getSh2bMatrix());
	        	 Utils.setHBoxLabel(py2bMatrix, 75, false, calc1095c.getPy2bMatrix());
	        	 Utils.setHBoxLabel(pcodMatrix, 75, false, calc1095c.getPcodMatrix());
	        	 Utils.setHBoxLabel(afdbMatrix, 75, false, calc1095c.getAfdbMatrix());
	        	
	        	 Utils.setHBoxCheckBox(janCoveredCB, 75, calc1095c.isJanCovered());
	        	 Utils.setHBoxCheckBox(febCoveredCB, 75, calc1095c.isFebCovered());
	        	 Utils.setHBoxCheckBox(marCoveredCB, 75, calc1095c.isMarCovered());
	        	 Utils.setHBoxCheckBox(aprCoveredCB, 75, calc1095c.isAprCovered());
	        	 Utils.setHBoxCheckBox(mayCoveredCB, 75, calc1095c.isMayCovered());
	        	 Utils.setHBoxCheckBox(junCoveredCB, 75, calc1095c.isJunCovered());
	        	 Utils.setHBoxCheckBox(julCoveredCB, 75, calc1095c.isJulCovered());
	        	 Utils.setHBoxCheckBox(augCoveredCB, 75, calc1095c.isAugCovered());
	        	 Utils.setHBoxCheckBox(sepCoveredCB, 75, calc1095c.isSepCovered());
	        	 Utils.setHBoxCheckBox(octCoveredCB, 75, calc1095c.isOctCovered());
	        	 Utils.setHBoxCheckBox(novCoveredCB, 75, calc1095c.isNovCovered());
	        	 Utils.setHBoxCheckBox(decCoveredCB, 75, calc1095c.isDecCovered());
	        	 
	        	 if (calc1095c.getJanOfferCode() != null)
	        		 Utils.setHBoxLabel(janOfferCode, 75, false, calc1095c.getJanOfferCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(janOfferCode, 75, false, "");
	        	 if (calc1095c.getFebOfferCode() != null)
	        		 Utils.setHBoxLabel(febOfferCode, 75, false, calc1095c.getFebOfferCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(febOfferCode, 75, false, "");
	        	 if (calc1095c.getMarOfferCode() != null)
	        		 Utils.setHBoxLabel(marOfferCode, 75, false, calc1095c.getMarOfferCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(marOfferCode, 75, false, "");
	        	 if (calc1095c.getAprOfferCode() != null)
	        		 Utils.setHBoxLabel(aprOfferCode, 75, false, calc1095c.getAprOfferCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(aprOfferCode, 75, false, "");
	        	 if (calc1095c.getMayOfferCode() != null)
	        		 Utils.setHBoxLabel(mayOfferCode, 75, false, calc1095c.getMayOfferCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(mayOfferCode, 75, false, "");
	        	 if (calc1095c.getJunOfferCode() != null)
	        		 Utils.setHBoxLabel(junOfferCode, 75, false, calc1095c.getJunOfferCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(junOfferCode, 75, false, "");
	        	 if (calc1095c.getJulOfferCode() != null)
	        		 Utils.setHBoxLabel(julOfferCode, 75, false, calc1095c.getJulOfferCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(julOfferCode, 75, false, "");
	        	 if (calc1095c.getAugOfferCode() != null)
	        		 Utils.setHBoxLabel(augOfferCode, 75, false, calc1095c.getAugOfferCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(augOfferCode, 75, false, "");
	        	 if (calc1095c.getSepOfferCode() != null)
	        		 Utils.setHBoxLabel(sepOfferCode, 75, false, calc1095c.getSepOfferCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(sepOfferCode, 75, false, "");
	        	 if (calc1095c.getOctOfferCode() != null)
	        		 Utils.setHBoxLabel(octOfferCode, 75, false, calc1095c.getOctOfferCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(octOfferCode, 75, false, "");
	        	 if (calc1095c.getNovOfferCode() != null)
	        		 Utils.setHBoxLabel(novOfferCode, 75, false, calc1095c.getNovOfferCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(novOfferCode, 75, false, "");
	        	 if (calc1095c.getDecOfferCode() != null)
	        		 Utils.setHBoxLabel(decOfferCode, 75, false, calc1095c.getDecOfferCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(decOfferCode, 75, false, "");
	        		 
	    		 Utils.setHBoxLabel(janEEShare, 75, false, calc1095c.getJanEEShare());
	    		 Utils.setHBoxLabel(febEEShare, 75, false, calc1095c.getFebEEShare());
	    		 Utils.setHBoxLabel(marEEShare, 75, false, calc1095c.getMarEEShare());
	    		 Utils.setHBoxLabel(aprEEShare, 75, false, calc1095c.getAprEEShare());
	    		 Utils.setHBoxLabel(mayEEShare, 75, false, calc1095c.getMayEEShare());
	    		 Utils.setHBoxLabel(junEEShare, 75, false, calc1095c.getJunEEShare());
	    		 Utils.setHBoxLabel(julEEShare, 75, false, calc1095c.getJulEEShare());
	    		 Utils.setHBoxLabel(augEEShare, 75, false, calc1095c.getAugEEShare());
	    		 Utils.setHBoxLabel(sepEEShare, 75, false, calc1095c.getSepEEShare());
	    		 Utils.setHBoxLabel(octEEShare, 75, false, calc1095c.getOctEEShare());
	    		 Utils.setHBoxLabel(novEEShare, 75, false, calc1095c.getNovEEShare());
	    		 Utils.setHBoxLabel(decEEShare, 75, false, calc1095c.getDecEEShare());
	
	        	 if (calc1095c.getJanSafeHarborCode() != null)
	        		 Utils.setHBoxLabel(janSafeHarborCode, 75, false, calc1095c.getJanSafeHarborCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(janSafeHarborCode, 75, false, "");
	        	 if (calc1095c.getFebSafeHarborCode() != null)
	        		 Utils.setHBoxLabel(febSafeHarborCode, 75, false, calc1095c.getFebSafeHarborCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(febSafeHarborCode, 75, false, "");
	        	 if (calc1095c.getMarSafeHarborCode() != null)
	        		 Utils.setHBoxLabel(marSafeHarborCode, 75, false, calc1095c.getMarSafeHarborCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(marSafeHarborCode, 75, false, "");
	        	 if (calc1095c.getAprSafeHarborCode() != null)
	        		 Utils.setHBoxLabel(aprSafeHarborCode, 75, false, calc1095c.getAprSafeHarborCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(aprSafeHarborCode, 75, false, "");
	        	 if (calc1095c.getMaySafeHarborCode() != null)
	        		 Utils.setHBoxLabel(maySafeHarborCode, 75, false, calc1095c.getMaySafeHarborCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(maySafeHarborCode, 75, false, "");
	        	 if (calc1095c.getJunSafeHarborCode() != null)
	        		 Utils.setHBoxLabel(junSafeHarborCode, 75, false, calc1095c.getJunSafeHarborCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(junSafeHarborCode, 75, false, "");
	        	 if (calc1095c.getJulSafeHarborCode() != null)
	        		 Utils.setHBoxLabel(julSafeHarborCode, 75, false, calc1095c.getJulSafeHarborCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(julSafeHarborCode, 75, false, "");
	        	 if (calc1095c.getAugSafeHarborCode() != null)
	        		 Utils.setHBoxLabel(augSafeHarborCode, 75, false, calc1095c.getAugSafeHarborCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(augSafeHarborCode, 75, false, "");
	        	 if (calc1095c.getSepSafeHarborCode() != null)
	        		 Utils.setHBoxLabel(sepSafeHarborCode, 75, false, calc1095c.getSepSafeHarborCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(sepSafeHarborCode, 75, false, "");
	        	 if (calc1095c.getOctSafeHarborCode() != null)
	        		 Utils.setHBoxLabel(octSafeHarborCode, 75, false, calc1095c.getOctSafeHarborCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(octSafeHarborCode, 75, false, "");
	        	 if (calc1095c.getNovSafeHarborCode() != null)
	        		 Utils.setHBoxLabel(novSafeHarborCode, 75, false, calc1095c.getNovSafeHarborCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(novSafeHarborCode, 75, false, "");
	        	 if (calc1095c.getDecSafeHarborCode() != null)
	        		 Utils.setHBoxLabel(decSafeHarborCode, 75, false, calc1095c.getDecSafeHarborCode().toString());
	        	 else
	        		 Utils.setHBoxLabel(decSafeHarborCode, 75, false, "");
	        		 
	    		 Utils.setHBoxCheckBox(selfInsuredCB, 75, calc1095c.isSelfInsured());
	    		 Utils.setHBoxLabel(irs10945cCalculationId, 75, false, calc1095c.getIrs10945cCalculationId());
	    		 Utils.setHBoxLabel(calculatedIrs1094cId, 75, false, calc1095c.getCalculatedIrs1094cId());
	    		 Utils.setHBoxLabel(irs1095cId, 75, false, calc1095c.getIrs1095cId());

                 this.getChildren().addAll( empdMatrix, subtMatrix, lnapMatrix, fteeMatrix, dh2dMatrix, inelMatrix, wavdMatrix, cbraMatrix, unonMatrix, slfiMatrix, mincMatrix, sh2bMatrix, py2bMatrix, pcodMatrix, afdbMatrix,
                		 janCoveredCB, febCoveredCB, marCoveredCB, aprCoveredCB, mayCoveredCB, junCoveredCB, julCoveredCB, augCoveredCB, sepCoveredCB, octCoveredCB, novCoveredCB, decCoveredCB,
                		 janOfferCode, febOfferCode, marOfferCode, aprOfferCode, mayOfferCode, junOfferCode, julOfferCode, augOfferCode, sepOfferCode, octOfferCode, novOfferCode, decOfferCode,
                		 janEEShare, febEEShare, marEEShare, aprEEShare, mayEEShare, junEEShare, julEEShare, augEEShare, sepEEShare, octEEShare, novEEShare, decEEShare,
                		 janSafeHarborCode, febSafeHarborCode, marSafeHarborCode, aprSafeHarborCode, maySafeHarborCode, junSafeHarborCode, julSafeHarborCode, augSafeHarborCode, sepSafeHarborCode, octSafeHarborCode, novSafeHarborCode, decSafeHarborCode,
                		 selfInsuredCB, irs10945cCalculationId, calculatedIrs1094cId, irs1095cId);
             
             } else {
	        	 Utils.setHBoxLabel(empdMatrix, 75, false, "EmpdMatrix");
	        	 Utils.setHBoxLabel(subtMatrix, 75, false, "SubtMatrix");
	        	 Utils.setHBoxLabel(lnapMatrix, 75, false, "LnapMatrix");
	        	 Utils.setHBoxLabel(fteeMatrix, 75, false, "FteeMatrix");
	        	 Utils.setHBoxLabel(dh2dMatrix, 75, false, "Dh2dMatrix");
	        	 Utils.setHBoxLabel(inelMatrix, 75, false, "InelMatrix");
	        	 Utils.setHBoxLabel(wavdMatrix, 75, false, "WavdMatrix");
	        	 Utils.setHBoxLabel(cbraMatrix, 75, false, "CbraMatrix");
	        	 Utils.setHBoxLabel(unonMatrix, 75, false, "UnonMatrix");
	        	 Utils.setHBoxLabel(slfiMatrix, 75, false, "SlfiMatrix");
	        	 Utils.setHBoxLabel(mincMatrix, 75, false, "MincMatrix");
	        	 Utils.setHBoxLabel(sh2bMatrix, 75, false, "Sh2bMatrix");
	        	 Utils.setHBoxLabel(py2bMatrix, 75, false, "Py2bMatrix");
	        	 Utils.setHBoxLabel(pcodMatrix, 75, false, "PcodMatrix");
	        	 Utils.setHBoxLabel(afdbMatrix, 75, false, "AfdbMatrix");
	        	
	        	 Utils.setHBoxLabel(janCovered, 75, false, "Jan Cvrd");
	        	 Utils.setHBoxLabel(febCovered, 75, false, "Feb Cvrd");
	        	 Utils.setHBoxLabel(marCovered, 75, false, "Mar Cvrd");
	        	 Utils.setHBoxLabel(aprCovered, 75, false, "Apr Cvrd");
	        	 Utils.setHBoxLabel(mayCovered, 75, false, "May Cvrd");
	        	 Utils.setHBoxLabel(junCovered, 75, false, "Jun Cvrd");
	        	 Utils.setHBoxLabel(julCovered, 75, false, "Jul Cvrd");
	        	 Utils.setHBoxLabel(augCovered, 75, false, "Aug Cvrd");
	        	 Utils.setHBoxLabel(sepCovered, 75, false, "Sep Cvrd");
	        	 Utils.setHBoxLabel(octCovered, 75, false, "Oct Cvrd");
	        	 Utils.setHBoxLabel(novCovered, 75, false, "Nov Cvrd");
	        	 Utils.setHBoxLabel(decCovered, 75, false, "Dec Cvrd");
	        	 
            	  
                 this.getChildren().addAll( empdMatrix, subtMatrix, lnapMatrix, fteeMatrix, dh2dMatrix, inelMatrix, wavdMatrix, cbraMatrix, unonMatrix, slfiMatrix, mincMatrix, sh2bMatrix, py2bMatrix, pcodMatrix, afdbMatrix,
                		 janCovered, febCovered, marCovered, aprCovered, mayCovered, junCovered, julCovered, augCovered, sepCovered, octCovered, novCovered, decCovered,
                		 janOfferCode, febOfferCode, marOfferCode, aprOfferCode, mayOfferCode, junOfferCode, julOfferCode, augOfferCode, sepOfferCode, octOfferCode, novOfferCode, decOfferCode,
                		 janEEShare, febEEShare, marEEShare, aprEEShare, mayEEShare, junEEShare, julEEShare, augEEShare, sepEEShare, octEEShare, novEEShare, decEEShare,
                		 janSafeHarborCode, janSafeHarborCode, febSafeHarborCode, marSafeHarborCode, aprSafeHarborCode, maySafeHarborCode, junSafeHarborCode, julSafeHarborCode, augSafeHarborCode, sepSafeHarborCode, octSafeHarborCode, novSafeHarborCode, decSafeHarborCode,
                		 selfInsured, irs10945cCalculationId, calculatedIrs1094cId, irs1095cId);
              }
    		 } catch (Exception e) {
    			 DataManager.i().log(Level.SEVERE, e); 
    		 }
         }
    }	
	
	
}


