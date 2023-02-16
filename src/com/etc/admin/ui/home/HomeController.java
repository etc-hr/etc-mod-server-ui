package com.etc.admin.ui.home; 
 
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.EmsApp;
import com.etc.admin.data.DataManager;
import com.etc.admin.localData.AdminPersistenceManager;
import com.etc.admin.utils.Utils;
import com.etc.corvetto.CorvettoConnection;
import com.etc.entities.CoreSystemInstance; 
import com.etc.rqs.CoreSystemInstanceRequest; 
 
import javafx.event.ActionEvent; 
import javafx.fxml.FXML; 
import javafx.scene.control.Label; 
import javafx.scene.control.ListView; 
import javafx.scene.control.TextField; 
import javafx.scene.layout.HBox; 
 
public class HomeController { 
	 
	@FXML 
	private Label homeVersionLabel; 
	@FXML 
	private ListView<HBoxChangesCell> homeChangesList; 
	@FXML 
	private TextField homeCalcStatusField; 
	@FXML 
	private TextField homePipelineStatusField; 
	@FXML 
	private TextField homeExporterStatusField; 
	@FXML 
	private TextField homeModServerStatusField; 
	@FXML 
	private TextField homeAccountsField; 
	@FXML 
	private TextField homeEmployersField; 
	@FXML 
	private TextField homeChannelsField; 
	@FXML 
	private TextField homeSpecificationsField; 
	/** 
	 * initialize is called when the FXML is loaded 
	 */ 
	
	// pause before we start servicing the cache
	boolean pauseCacheUpdate = true;
	
	@FXML 
	public void initialize() { 
		try {
			homeVersionLabel.setText("Admin App Version " + EmsApp.getInstance().getApplicationProperties().getProperty(CorvettoConnection.APP_VSN, "0.0.0")); 
			initUpdates(); 
			addChanges(); 
		} catch(Exception e) { 
			DataManager.i().log(Level.SEVERE, e); 
		} 
	} 
	 
	private void initUpdates() 
	{ 
		// using a thread to control the updates 
        Thread updateTimer = new Thread(new Runnable() { 
 
            @Override 
            public void run() { 
                while (true) { 
                    try { 
                    	// update the status 
                        Thread.sleep(250); 
                    	setStatus(); 
                    	// and wait 
                        Thread.sleep(300000); 
                    } catch (Exception e) { 
                    	DataManager.i().log(Level.SEVERE, e); 
                    } 
                } 
            } 
        });	     
	     
        updateTimer.start(); 
	} 
	
	private void setStatus() { 
		try { 
			setCalcStatus(); 
			setPipelineStatus(); 
			setExporterStatus(); 
			setModServerStatus(); 
			updateCache();
		} catch(Exception e) { 
			// future stub 
			DataManager.i().log(Level.SEVERE, e); 
		} 
	}
	
	private void updateCache()
	{
		if (pauseCacheUpdate == true)
			pauseCacheUpdate = false;
		else
			DataManager.i().updateAccountsAndEmployersByTask();	
		
		// accounts
		homeAccountsField.setText("Acct: " + String.valueOf(DataManager.i().mAccounts.size()));
		homeAccountsField.setStyle("-fx-background-color:#99ff99; -fx-border-color:#000000"); 

		// employers
		homeEmployersField.setText("Empr: " + String.valueOf(DataManager.i().mEmployersList.size()));
		homeEmployersField.setStyle("-fx-background-color:#99ff99; -fx-border-color:#000000"); 
	}
	 
	private CoreSystemInstance getCoreSystemInstance(Long systemId) 
	{ 
		CoreSystemInstanceRequest request = new CoreSystemInstanceRequest(); 
		request.setCoreSystemId(systemId); 
		List<CoreSystemInstance> instances = null;
		try {
			instances = AdminPersistenceManager.getInstance().getAll(request);
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		} 
		if (instances != null) { 
			CoreSystemInstance newestInstance = instances.get(0); 
			for (CoreSystemInstance instance : instances) { 
				if (instance.getLastUpdated().getTimeInMillis() > newestInstance.getLastUpdated().getTimeInMillis()) 
					newestInstance = instance; 
			} 
			return newestInstance; 
		} 
		else 
			return null; 
	} 
	 
	private void setCalcStatus() { 
		CoreSystemInstance instance = getCoreSystemInstance(6l); 
		if (instance == null) return; 
		// get the elapsed time 
		int elapsed = Utils.getElapsedMinutes(instance.getLastUpdated()); 
		// set the status 
		if (elapsed > 1 || elapsed == 0) 
			homeCalcStatusField.setText(String.valueOf(elapsed) + " minutes"); 
		else 
			homeCalcStatusField.setText(String.valueOf(elapsed) + " minute"); 
			 
		if (elapsed < 30) { 
			homeCalcStatusField.setStyle("-fx-background-color:#99ff99; -fx-border-color:#000000"); 
		}else { 
        	homeCalcStatusField.setStyle("-fx-background-color:#ff5555; -fx-border-color:#000000"); 
		} 
	} 
	 
	private void setPipelineStatus() { 
		CoreSystemInstance instance = getCoreSystemInstance(9l); 
		if (instance == null) return; 
		// get the elapsed time 
		int elapsed = Utils.getElapsedMinutes(instance.getLastUpdated()); 
		// set the status 
		if (elapsed > 1 || elapsed == 0) 
			homePipelineStatusField.setText(String.valueOf(elapsed) + " minutes"); 
		else 
			homePipelineStatusField.setText(String.valueOf(elapsed) + " minute"); 
		if (elapsed < 30) { 
			homePipelineStatusField.setStyle("-fx-background-color:#99ff99; -fx-border-color:#000000" ); 
		}else { 
        	homePipelineStatusField.setStyle("-fx-background-color:#ff5555; -fx-border-color:#000000"); 
		} 
	} 
	 
	private void setExporterStatus() { 
		// get the system instance 
		CoreSystemInstance instance = getCoreSystemInstance(8l); 
		if (instance == null) return; 
		// get the elapsed time 
		int elapsed = Utils.getElapsedMinutes(instance.getLastUpdated()); 
		// set the status 
		if (elapsed > 1 || elapsed == 0) 
			homeExporterStatusField.setText(String.valueOf(elapsed) + " minutes"); 
		else 
			homeExporterStatusField.setText(String.valueOf(elapsed) + " minute"); 
		if (elapsed < 30) { 
			homeExporterStatusField.setStyle("-fx-background-color:#99ff99; -fx-border-color:#000000"); 
		}else { 
        	homeExporterStatusField.setStyle("-fx-background-color:#ff5555; -fx-border-color:#000000"); 
		} 
	} 
	 
	private void setModServerStatus() { 
		CoreSystemInstance instance = getCoreSystemInstance(2l); 
		if (instance == null) return; 
		// get the elapsed time 
		int elapsed = Utils.getElapsedMinutes(instance.getLastUpdated()); 
		// set the status 
		if (elapsed > 1 || elapsed == 0) 
			homeModServerStatusField.setText(String.valueOf(elapsed) + " minutes"); 
		else 
			homeModServerStatusField.setText(String.valueOf(elapsed) + " minute"); 
		if (elapsed < 30) { 
			homeModServerStatusField.setStyle("-fx-background-color:#99ff99; -fx-border-color:#000000" ); 
		}else { 
			homeModServerStatusField.setStyle("-fx-background-color:#ff5555; -fx-border-color:#000000"); 
		} 
	} 
	  
	private void addChanges() { 
		//homeChangesList.getItems().add(new HBoxChangesCell("",""));
		// 0.0.58
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.58","Document Screen updates and fixes."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.58","Document Screen Conversion to independent popup."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.58","Fix for Irs1095c controller where the Irs1095c could have null Irs1095cCIs"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.58","Fix for Calc queue where Irs10945b taxyear could be null, causing entry to not display"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.58","Calc Queue detail pages for Code Clls should now properly display any relevant notices."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.58","Fix for issue where Calc queue may not load on a failed CodeCall process."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.58","Fix for occcasional null Calc Spec Id when Calling Codes."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.58","Fixed bug with Tax Year Calling codes MEC Confirmation - button functions were swapped."));
		// 0.0.57
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.57","Tax Year 1095c section layout and operation changed to use edit/save functionality instead of individual buttons."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.57","Document screen updates and fixes."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.57","Clear button added to Admin Employee Tool."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.57","The Admin Employee Tool is now an independent Popup, allowing for easier user placement."));
		// 0.0.56
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.56","Fix for filing requests for Type B clients (ticket #544.)"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.56","TaxYear now updates its process status when changes are made to Irs1095cs."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.56","New layout on Irs1095c section in TaxYear, with new columns."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.56","New Admin Employee Tool functionality and layout (admin tools, left nav menu), featuring support for codes calling."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.56","1095c mapper bug fixes and doc updates."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.56","Employee Merge update ssn bug fix."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.56","Inactive option for employee searches on account screen now defaults to on."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.56","Default memory footprint for Windows exe version increased to 2500 - 5000."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.56","New Button on TaxYear Screen (in Irs1094b section, when visible) to view Irs1094b System Info."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.56","The Uploader is now an independent popup, allowing quick access without losing place in the app and flexible placement."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.56","New Button on TaxYear screen (in Irs1094c section, when visible) to view Irs1094c System Info."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.56","Fixed issue where Calling Codes on B Form would fail if there were no filings (ticket #530.)"));
		// 0.0.55
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.55","Added Employer Incorporated on and Dissolved on to Tax Year 1094c section."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.55","Fixed an issue that was preventing abandoned 1095 filings (tax year screen) from displaying."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.55","Quick navigation Queue button renamed to Pipeline Queue for clarity."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.55","New Pipeline Queue color coding in bright red for files stuck in processing (ticket #529.)"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.55","Correction for occasional blank or incorrect Raw Field Display."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.55","Inactive Employers removed from Account Employee search (ticket #527.)"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.55","Corrected a problem that could lead to TaxYears not being displayed for  Call Code selection (ticket #528.)"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.55","Cleaned up CI View and its related popups (ticket #525.)"));
		// 0.0.54
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.54","Direct key selections for the Irs1095c form combo."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.54","Added check for Generates Codes in Employer Tax Year Service level when calling codes on the Account Screen."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.54","Increased App Memory Footprint when launched on Windows to combat app locking."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.54","Added Additional columns to Employer PlanYear List popup."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.54","Added Affordable element to PlanYear List and PlanYear detail page."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.54","Corrected issue where the selected plan on Coverage detail Popup was not correct (ticket #501.)"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.54","Fixed a problem with filtering on the Calc Queue that could cause entries to not be listed when active (ticket #521.)"));
		// 0.0.53
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.53","Account File History Listing is now color coded orange for rejected files."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.53","Account File History System Info button added."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.53","Account File History Popup Rejection is now active."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.53","The selections for TaxYear when calling codes in the Account and Employee screens now defaults to 2022, but users can still select others when available (ticket #514.)"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.53","Users should now be able to change the plan (benefit) on employee coverages through the coverage detail popup (ticket #507.)"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.53","Irs1095c System Popup now defaults correctly to the IRs1095c object, allowing activation settings (ticket #511.)"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.53","Coverage Detail Screen now lists the plan, allows selecting a new plan from new dropdown (task #501.)"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.53","CI Coverages now list the Plan Provider Reference instead of the name, matching the Employee coverage list (ticket #509.)"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.53","Notes updated to use the TaxPeriod Federal Year with matching org when adding notes instead of all Tax Periods (tickets #500 & #484.)"));
		// 0.0.52
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.52","Fixed issue where Inactive Employer would still have codes called when processed from Account Screen."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.52","Process Status on Employer / TaxYear moved from 1094c section to TaxYear (ticket #488.)"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.52","Employee Call code Default Tax Year selection corrected (ticket #485.)"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.52","HR Account Search now works as an independent popup, allowing user to search without losing place in app and optional placement."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.52","HR Employer Search now works as an independent popup, allowing user to search without losing place in app and optional placement."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.52","HR Employee Search now works as an independent popup, allowing user to search without losing place in app and optional placement."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.52","Context aware user messages added for failed Account Employee searches."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.52","Account Screen Employee Search for CoreId now works (ticket #478)"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.52","Obsolete Employer Description field removed from screen."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.52","Employer Edits should now save (ticket #477.)"));
		// 0.0.51
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.51","TaxYear Authoritative now editable by select few users."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.51","TaxYear 1094 New Form checkbox is now editable."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.51","Fix for Account Calling Codes when one employer had no TaxYear Service Level (ticket #468.)"));
		// 0.0.50
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.50","Employee coverage Edits no longer overwrite null Member share values with 0 (ticket #467.)"));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.50","Fix for Parse Pattern save on Employee Mapper Zip Filed (ticket #466.)"));
		// 0.0.49
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.49","Employee Coverage list now supports right click inactivate toggle, multuiple row selection included (ticket #463.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.49","Irs1095b bug fixes and updates.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.49","User added to Export Queue.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.49","Export Queue is now color coded based on status (ticket #462.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.49","Employee person edits users should now be tracked Correctly (task #341.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.49","Export Queue is now an independent popup (ticket #461.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.49","Clearing employee coverage class changes Dates should now properly mark them as empty (task #349.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.49","Create IRS Error File button in TaxYear is now active.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.49","Last used Account, Employer, and Employee quick links now persist across EmsApp restarts (ticket #459.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.49","Users can now add Employee Pay Periods with new button (ticket #447.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.49","Added Union Type to Employee Pay View Popup (ticket #448.)")); 
		// 0.0.48
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.48","Added support for Irs1094c Percentage fields in TaxYear Screen (ticket #444.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.48","Program Startup now pauses 1 update cycle before launching UpdateCache thread to stop potential load failure for inital screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.48","Added DocId to message and log when Download  for better debugging.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.48","Inactive CIs should now show on the Employee CI popup when so selected (ticket 454.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.48","Corrected an issue where Calling codes could create a B record instead of a C.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.48","Added support for ICHRA flags (ticket #445.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.48","Added support for ACA Print Type E.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.48","Fixed a bug in file history that could cause it to fail loading.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.48","Added user updates to the reset database operation.")); 
		// 0.0.47
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.47","Calc queue is now an independent popup, allowing for placement on another monitor or letting users check status without losing place in the app.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.47","Account notes should now allow adding new notes.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.47","Calling codes for Employer CoverageClass should now be active.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.47","Expanded error notifications when calling codes.")); 
		// 0.0.46
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.46","Added User (where available) to the Call Codes history table.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.46","Irs1095c - Updated Dependent subclass to handle parent class variables for transitioning into Dependent Table for proper display, saving.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.46","Added option to unlock all Irs1095cs in the taxyear 1095 tools (task #440)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.46","The 1095c Custom lock checkboxes in the taxyear 1095 tools now reset on entry (task #441.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.46","Account Code Call History occasional empty list corrected (task #431.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.46","Fixed an issue with CalculationInformation when requesting filing on the TaxYear screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.46","Update of taxYears, Employer verification on employee screen (no user change).")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.46","There are now separate controls throughout the app for showing inactive and deleted objects where applicable (task #377)")); 
		// 0.0.45
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.45","Fixed an issue when calling codes for Type B Tax Years.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.45","System Info Dialog now warns the user if they try to edit a deleted object (task #433.)")); 
		// 0.0.44
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.44","New Code Call History popup, button on Account, added filter search (task #431.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.44","Added description to Calculation Request when an employee merger is requested.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.44","New Employee Merger, converted to a popup and simplified with CoreId search driven layout and user feedback.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.44","New Coverage Class Call codes section and functionality added to the Coverage Class detail screen (task #405.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.44","Added check for viable taxyear when calling codes on account, warn user accordingly.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.44","Corrected Headers on Raw Field Paycode Reference Popup (task #430.)")); 
		// 0.0.43
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.43","Notes added to Employer Screen, will show employer notes where available.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.43","Notes viewing cleanup Including TaxPeriod year columns, corrected note text wrapping, and new search functionality, ")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.43","File History Reject button disabled until API update.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.43","New popup for RawField PayCode References supporting Pay Conversion Calculations verification and adding.")); 
		// 0.0.42
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.42","Added editable Process Status Combo to the TaxYear 1094c.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.42","Taxyear 1094c MEC select / unselect button name change, function correction.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.42","New Popup for handling PayCode and other references in Raw Field Edits.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.42","Added new Taxyear Irs1095c tool section in the 1094c area, allowing the user to fully lock, mirror lock (same as the 1094c) and custom lock the related 1095cs.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.42","The app now checks all 1095cs for a given 1094c when filIng and does not allow it if unlocked months are found.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.42","Fixed a problem where raw field edits would fail if a header change was erroneously added to the change stack, affecting raw pays ( task #410.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.42","Correction for Lock edit status saves on TaxYear Irs1094c.")); 
		// 0.0.41
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.41","New recent section in left Nav menu. Most recently visited 3 Accounts, Employers, and Employees are listed for simple 1 click access (task #409.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.41","Now checking for MEC checked on the 1094c when submitting a filing. User can select to continue (task #400.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.41","Now checking for locked 1094 months when submitting a filing. Unlocked months are not allowed (task #400.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.41","TaxYear 1094c now has a lock/unlock button to handle all monthly lock buttons at once.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.41","Added rejection functionality to Account File History (task #397.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.41","Employer Call History popup changed to use sortable table, time added to date, column cleanup (task #402.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.41","TaxYear Irs1094c Authoritative checkbox was not being properly displayed. This has been corrected (task #407.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.41","Requesting a filing on a taxyear with a service level would result in the wrong process being called. This has been corrected.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.41","Tax Year added to the employer call code history popup (task #394.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.41","Double clicking the account file history now leads to the raw field screen, hiding popup in the process (task #396.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.41","Login Cursor position now locates to password if user has been saved (task #403.)")); 
		// 0.0.40
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.40","Archived column removed from Pipeline Queue.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.40","Fix for Raw Field Data PayCode and Custom Field 1 column headers.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.40","Fixed a problem where editing a start date on a type coverage raw field would not save.")); 
		// 0.0.39
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.39","AirTransReceipt and other file types are now supported for download in the pipeline queue and file history.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.39","Pipeline Queue Archive function support, new Pipeline Queue column for archived raw field records. (task #391)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.39","Paygaps are now listed in the Payroll Popup for Employees.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.39","Debug log viewer now correctly loads the correct dev version of the the logs when the dev version of the app is ran.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.39","Reprocess requests now set the current user id on the submitted pipeline request.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.39","Logging added for reprocess requests for troubleshooting user info.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.39","Coverage Class Memberships now editable in system info popups.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.39","Coverage Class Membership cleanup for functions now present in the System Info Popup.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.39","Fixed a condition in the Pipeline Queue that could cause an exception throw when a zero record entry was selected.")); 
		// 0.0.38
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.38","New Checkbox and Functionality - Show across all Employers -  added to Employee payroll listing.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.38","Teal font highlighting added for Employee payroll typeChange entries.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.38","Location information should now show in Employee Screen (task #389)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.38","Vertical size of raw field grid adjusted to show horizontal scroll without vertical scroll.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.38","The Raw Field Screen now clears before updating when entering from the pipeline queue. Old data displayed while loading may have been confusing.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.38","Employee Coverage Class Membership logic checks - Start date must be present, End date cannot be before start date, and class must be selected.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.38","Employee Coverage Class Memberships can now be added (task #385.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.38","Raw fields of Type Irs1094c are now editable and save.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.38","Raw field info should now be viewable for type Irs1094c files (task #365)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.38","Label for Employer Coverage Classes changed from Coverage Groups - sometimes it's the little things.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.38","Extra message box when calling codes in TaxYear corrected.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.38","System Info popup File Detail and downloads for Employee Coverage File should now work as designed (task #382.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.38","Coverage Class Membership System Info should now work.")); 
		// 0.0.37
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.37","User message added to Payroll popup when there is no payroll data to display for selected employee.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.37","The Admin App now checks on startup for another running admin app and informs the user if one is found, then exits.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.37","Auto Database Reset expanded for more error conditions.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.37","Reset Database now deletes db files and locks for problematic resets.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.37","Fix bug where account may not be present for Calc queue, causing issue. (task #371)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.37","Select Pipeline Queue columns (Account, Employer, Spec Id, Mapper Id) now show item when doubleclicked. Other columns behave as before.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.37","Additional Error handling added for init process of major screens.")); 
		// 0.0.36
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.36","Added person id and address id to system info popup when object is employee.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.36","Users can now add and edit Employer Pay Periods."));
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.36","File Download Capabiity  has been added to the Pipeline Queue (replaces the copy filename button.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.36","New logging added to track screen and data for better crash troubleshooting.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.36","Debug checkbox in user settings now active with message and title bar warning. Creates large files, use sparingly.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.36","Fixed a bug that could cause a crash when selecting files in pipeline queue.")); 
		// 0.0.35
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.35","Added user message about current and suggested version when app is out of date and barred from running on system.")); 
		// 0.0.34
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.34","New Export Queue, accessed through the navigation menu on left (task #351.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.34","Corrected DSSN save to Employee Information table when editing an employee (task #364.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.34","Calc Queue taxyear column removed, Coverage Class Id added to detail page (task #363.)")); 
		// 0.0.33
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.33","Fixed a problem with date comparison in calc and other queues that would not properly sort within the same day.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.33","File Downloads are now available (new button) on the Account File History Popup.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.33","File Downloads are now available in the System Data popup file info (accessed by clicking on the batch id) for select object types.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.33","New Code Call history (via button) on the employer screen (task #357.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.33","Employee Edit History listing and resulting action corrected, should now show changes properly.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.33","Raw Field edits bug fixed that would not properly save changes (task #356.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.33","Fixed a problem where dates using date picker in other time zones could have saved date reduced by one.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.33","Employee editing when using the combo boxes were not properly saving, this has been corrected.")); 
		// 0.0.32
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.32","Employee History Viewing added support for independent Person and Address Snapshots.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.32","Employee Save now saves SSN inline instead of separate save, no longer creating additional Person snapshot.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.32","Coverage Class Membership detail dropdown blank entry fixed (task #349)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.32","Added Thread Failure Throwable Logging to application log file for better thread troubleshooting.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.32","Fixed bug where Employee change would keep resetting Account Employee search.")); 
		// 0.0.31
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.31","Coverage Group Membership saving fix (task #349.)")); 	
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.31","File History clear button now clears input (task #347.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.31","Bug fixes for Employee Edit History viewing.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.31","TypeChanged and TypeChangeVerified added to pay screens.")); 
		// 0.0.30
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.30","Account File History search update.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.30","New Edit History Viewing capability on Employee screen, accessed through new edit history combo.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.30","Account Calling codes logic changed to allow dissolved date in year of selected tax year, new message if none (task #337.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.30","Corrected bug that was displaying the Spec Id for the mapper id on the pipeline channel spec list (task #336.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.30","File History and Raw Field screens were launching the wrong version of the mapper screen, corrected (task #335)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.30","New Pay Tool for bulk updates by Pay Period found in Admin tools.")); 
		// 0.0.29
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.29","Account File History now has a Date Range. Choices are  of 6 month (default), 1 year, or all.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.29","Account File History now moved to its own larger, friendlier popup.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.29","Added reminder about other sub files on Raw Field editing saves.")); 
		// 0.0.28
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.28","Employee Merge Request Bug Fixes.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.28","Account Employer Table column change to Incorporated and Dissolved dates")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.28","Employee Employment Period Inactive fix for saving after marking inactive.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.28","HR Employer screen updated, file history kept to HR Account Screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.28","Multi-SelectSelect inactivate for employee pay update, progress indicator also added.")); 
		// 0.0.27
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.27","1095c Bug fixes and GenericException support for mappers")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.27","System Data Popup Deletes are now only active for Employees.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.27","Employment Periods, other objects, updated to use new Remove API functions when setting inactive.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.27","Employee Employment Periods Corrected to show inactive and deleted items when inactive selected.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.27","Behind the scenes - Extended generic Exception catch and logging added for better error tracking.")); 
		// 0.0.26
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.26","Corrections for data editing on a failed file (raw field edits - task# 317.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.26","Account Calling Codes should now ignore employers that are dissolved or tracking only (task# 320)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.26","Correction for Employment Periods not saving, along with server API changes. (task# 313.)")); 
		// 0.0.25
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.25","Irs1095c Dep Checkboxes editable and save.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.25","Irs1095c Multiple Dep Display Fix.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.25","Fixes for Raw Data editing and submission.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.25","Calc Queue now has System data access context menu on right click.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.25","Employment Period Popup now indicates deleted or inactive in top label.")); 
		// 0.0.24
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.24","Irs1095c Form bug fixes.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.24","Upload Form bug fixes.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.24","Tab order correction in HR Employee Search.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.24","Calc Queue Layout Fixed for hidden By default table scroll bar.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.24","Orange Color adjust on Calc Queue Calling Codes type.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.24","Descriptions added to the processing records for Calling Codes and other Calc requests.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.24","Raw Field Grid Top of screen has reminder to reprocess when edits are saved.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.24","Raw Field Editing correction for saving changes - bug fix.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.24","Raw Filed Editing Reprocess submission changed as needed for reprocessing existing raw data.")); 
		// 0.0.23
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.23","Employee Irs1095c Form Updates.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.23","The Employee Screen Employee Reference ID now indicates when the existing value is null.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.23","The Employee Screen Employee Reference ID is now editable.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.23","Add Employer function now provides user feedback during and after the update process.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.23","Fixed Bug in Add Employer function.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.23","Account Selection Combo in Add Employer popup (HR Employer) is now sorted with live filtering.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.23","HR Employee Search Button now bigger and uses image.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.23","Right click menu for system data popup added to HR Employee Search results.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.23","Added Refresh Button to HR Employer Search.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.23","Added Filter Clear Button to HR Employer Search.")); 
		// 0.0.22
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.22","Uploader bug fixes.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.22","Account Screen Employee search results converted to a sortable table.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.22","New clear filter button on the HR Account Search Screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.22","Refresh button for accounts and employers added to the HR Account search screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.22","Context menu (right click) added to HR Account search screen to quickly access system data information.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.22","HR Account Search screen changed to use a sortable table.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.22","Context menu for System Data popup added to HR Employer.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.22","HR Employer List View changed to sortable table view, filterable columns added.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.22","Pipeline Channel Pipeline Specification list is now a sortable table.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.22","Pipeline Channel File Step Handlers now filter to selected channel.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.22","Pipeline Channel File Step Handlers list converted to a sortable table view.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.22","Fixed potential performance bug from startup due to AppStatus activity.")); 
		// 0.0.21
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.21","Uploader and Irs1095c Bug Fixes")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.21","Employee Payroll List now supports multiple selection and bulk activation toggling.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.21","Core Id added to object names in the app's top breadcrumb control.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.21","Account Calling codes fix for missing Employer Ids.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.21","Calling Codes Calc Specification ID corrected to use provided TaxYearServiceLevel spec id.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.21","New AppStatus popup for user feedback during operations, currently used in Account Calling Codes.")); 
		// 0.0.20
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.20","Uploader Improvements and Bug Fixes.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.20","HR Employee Search now defaults enter key on search fields to start the search.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.20","Fixed issue where top left Account Search combobox would cause multiple data loads when using arrow keys to navigate.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.20","Pipeline Channel Specification List Inactive/Deleted color coding.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.20","Fixed Pipeline Channel Specification List Sort.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.20","Added Inactive notification flag to Pipeline Specification screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.20","Pipeline Specification UploadType Inactive support w/checkbox and Inactive text flag.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.20","Added Pipeline Specification Upload Type Selection Filter.")); 
		// 0.0.19
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.19","Added User indicators (green save button, bottom status) for the Employee Edit.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.19","Dev and Production versions of the app now use different logs and local DBs, can be ran together.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.19","New Uploader Employer Sort.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.19","Employee Edits now invalidate the Account Employee search list.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.19","Fix for Raw Data Invalidations when subfiles are present (task #295.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.19","Core System Data Popup now includes Object name in title for supported types.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.19","System Data Info button added for Pipeline Channel File Step Handlers.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.19","Account Calling Codes now scans its employers for all possible tax years for selection.")); 
		//0.0.18
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.18","Employee Irs1095c Save correction.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.18","Fix for Employer Pay Period detail popup not loading in some circumstances.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.18","Employer Contact List now loads before retrieving data, showing loading status.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.18","Employer Contact Detail now loads much quicker.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.18","Employer Tax Year 1094filings listing date now shows initial acceptance instead of last updated.")); 	
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.18","Pipeline Channel screen now uses the standard system data popups.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.18","Pipeline Spec screen now uses the standard System data Popups ")); 
		// 0.0.17
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.17","Nav Menu and HR Search fix for non-staff users.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.17","Employee Irs1095c Locking Method fix.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.17","Employee Irs1095c Screen Updates.")); 
		// 0.0.16
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.16","Fix for broken queues.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.16","User level filtering support for non-staff.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.16","Navigation menu update for non-staff users.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.16","Uploader updates and fixes.")); 
		// 0.0.15
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.15","Fix for Employee Irs1095c Failure to launch.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.15","Irs1095c Filing detail now implemented.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.15","Uploader load speed improvement and clear function.")); 
		// 0.0.14
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.14","Fix for saving Pipeline Specification Step Handlers.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.14","Prev/Next navigation for Employee Irs1095b, bypassing list.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.14","Employee Irs1095b and IRs1095c Tax Year sort for navigation.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.14","Employee Irs1095b now has CI (dependent) information.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.14","Top label and employer info added to Employee Irs1095b.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.14","A little self-indulgence - the Debug log screen is now wider.")); 
		// 0.0.13
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.13","Uploader Employee full list search with account info added to combo.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.13","Uploader Raw error form off table record selection.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.13","Uploader Color coding added to match pipeline queue.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.13","Correction for SSN editing when hiding - task #147")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.13","New layout and workflow for Employee IRs1095c, including new buttons and functionality.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.13","Improved Home Screen layout incorporating targeted screen size.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.13","Fix for Pay System Data Deactivate.")); 
		// 0.0.12
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.12","Double Verify when Editing SSN, improved workflow - task #147")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.12","Fixed Covered Individual launch issue - task #264")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.12","Added Employer Name & Id to Employee Screen - task #270")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.12","Fixed Filing Errors Employee Launch, added color coding - task #269")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.12","Added State Filing Information to Tax Year - task #64")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.12","Monthly Indicator For Coverage File Mapper")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.12","Employee Verified is now editable - task #272")); 
		// 0.0.11
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.11","Employee Search now on the Account screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.11","Account screen Users are now a popup.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.11","Account notes are a popup. Subject to change.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.11","Account screen no longer has vertical scroll at target resolution.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.11","Employer Screen employee search removed. Tax years expanded.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.11","Employer screen no longer has vertical screen at target resolution.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.11","Employee Calling Codes fix, Calc information was not getting set.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.11","System data Inactive fix for Pay Period when hitting save button after change.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.11","Correction for CI listing person id, CI Popup size.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.11","System Data Popup now has Batch Id file info popup for the supported HR Object types.")); 
		// 0.0.10
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.10","Added Custom Fields to Employee Coverage (ticket #191)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.10","System Data delete and inactivate for Comversges (ticket #187)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.10","Country and State Combos now active in Employee Edit (ticket #190)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.10","Employee Calc Request Tax Year defaults to current year (ticket 192, ongoing)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.10","Employee Screen now uses default screen size mechanis, no scrolling.")); 
		// 0.0.9
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.9","Account Screen Command Toolbar Added, functionality to come.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.9","Deduction removed from Employee Coverage.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.9","Decision Date Removed From Coverage.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.9","Ongoing updates to the Employee Screen and its popups")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.9","Covered Individual List Columns update")); 
		// 0.0.8
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.8","Employer Screen Layout redesign, with control toolbar and sections offscreen moved to individual popups")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.8","Employee Payroll Custom Fields 1 and 2 relocated to left of list, union type to the right side.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.8","Corrections for Multiple Monitor Setups.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.8","Employee Coverage Class Toolbar Button fix.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.8","Added Employee Payroll Loading notice on top of popup.")); 
		// 0.0.7
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.7","Pay Detail Batch Id button now launches File Detail popup.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.7","Payroll list now monitors for change instead of updating after every pay detail view.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.7","New Pay Detail Pay Period selection popup for changing pay period for a given pay.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.7","The Employee Coverage Memberships is now a Popup launched from a color-coordinated Tool Area Button.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.7","Employee Coverages are now a popup, also with a lovely color-coordinated Tool Area Button.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.7","Not to be outdone, the CIs (a.k.a dependents) are now a popup with an equally stunning matching button.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.7","Notes, while lacking a colorful launch button, now reside above the fold on the Employee screen.")); 
		// 0.0.6
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.6","Employee 1095bcs moved to a popup accessed through the applicable tools button.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.6","Employee Payroll moved to a popup accessed through the applicable tools.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.6","Pay Detail Popup changed to include more data and pay custom fields.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.6","Employee Core System Data is now available in new SystemInfo popup.")); 
		// 0.0.5
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.5","Fix for Uploader Spec and Employer when user changes screens.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.5","Log File now writes app and db version on startup for better debugging.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.5","Calc Queue Filter fix for missing entries when filtering.")); 
		// 0.0.4
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.4","Version Format Drops beta nomenclature.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.4","Improved logging for general JVM errors through redirection of system errors and out.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.4","New Employee Screen Tools Section.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.4","Login email and password disabled while system logging in.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.4","Employee Screen Data Properties Removed.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.4","Employee Screen Edit and delete buttons relocated to new Toolbar area.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.4","Lockup prevention - Account Screen File History Data and other tasks moved to managed fxqueue.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.4","Tax Year Submission Export Notice scrolling fixed to display entire message.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.4","Tax Year Screen now lists the coreid for the objects listed for easier troubleshooting.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.4","Double clicking a Tax Year Submission now takes you to the impacted employee, if available.")); 
		// 0.0.3-beta-0.1
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","New version format to line up with other development efforts.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Account Benefits relabled to Plans.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Unused Contacts Section removed from Account Screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Unused Properties Section Removed from Account Screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Calc Filing Button, option removed from Account Screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Fixed an issue with Account users being listed.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Added Inactive support to Account Users on the account screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Fixed an issue with Calling codes for all employers on the Account screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Uploader bug fix for similarly named Pipeline Specifications.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Correction for CoreID and SSN Employee search in the Employer Screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","CoreID added to Employee list on the Employer Screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Fix for Calling codes on the Employee level.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Tax Year Submission Notice now scrolls to see entire message.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Tax Year Submission Notice double click now takes user to impacted employee screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","HR Employee Search now properly clears other fields during use.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","HR Employee search by Employer Reference fix.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Deleted flag checking and support for references and mappers.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Mapper Reference bug fixes.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Mapper ign row specs, Add Hrs fixes.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Deleted object support for more App Screens, including appropriate color coding.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Uploader bug fix for Employer selection.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Calculation Queue Detail screens now support Calculation Notices for displaying errors.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3-beta-0.1","Admin tools access for system beta users.")); 
		// 0.0.3
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.3","Uploader fix for Spec Id / Upload type.")); 
		// 0.0.2
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.2","Fix for Uploader Uploadtype Listings.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.2","Fix for Uploader upload Progress Bar.")); 
		// 0.0.1
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.1","New Major/minor versioning nomenclature, starting now with 0.0.1.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.1","Added error notices to remaining calc types (was already active for IrsFiling.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.1","Corrected an issue with saving Dependent changes.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.1","Removed Primary from the Benefits screen data.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("0.0.1","Fix for UploadType Combo Bug.")); 
		// 20220208
		homeChangesList.getItems().add(new HBoxChangesCell("20220208","Support for Tax year Print Option of NA")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220208","Uploader update to clean up lists and operation.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220208","Type Irs1094c Support in Pipeline Quuue raw data grid (errors.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220208","Spec screen now hides view mapper buttons and marks N/A when not available.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220208","Fix for paste when copying Excel cell into Employee Search CoreId.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220208","Corrected Deleted Objects support for the HR Employee Screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220208","Deleted Object Support for Applicable Employee List Objects.")); 
		// 20220201
		homeChangesList.getItems().add(new HBoxChangesCell("20220201","Uploader updated to hold Account and Employer between screen changes.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220201","Added DB Monitor in the case of DB File Issues.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220201","Pipeline Queue now featues subtle color coding based on status.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220201","Quicker Employee Screen launch for Employee Search selection.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220201","Correction for Upload Type Account, Employer Interlocks.")); 
		// 20220125
		homeChangesList.getItems().add(new HBoxChangesCell("20220125","Added Uploader clear function when Uploader is exited to prevent hanging Account.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220125","Uploader File History to TableView including double click on record to access Mapper.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220125","Fixed coreid search issue in HR employee.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220125","New Search History section added to HR Employee.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220125","New Specification History section added to Pipeline Channels.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220125","Password change now has a 'Suggest Password' function for a random value that meets requirements.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220125","Pipeline queue Filename copy no longer annoyingly prompts that it has copied.")); 
		// 20220119Z
		homeChangesList.getItems().add(new HBoxChangesCell("20220119A","Fix for reprocessing type Pay Files")); 
		// 20220119
		homeChangesList.getItems().add(new HBoxChangesCell("20220119","Pipeline Queue Reprocess File fix.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220119","Local User Password Changes are now active.")); 
		// 20220118
		homeChangesList.getItems().add(new HBoxChangesCell("20220118","Fix for uploadtype add/save.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220118","App System-local user now a popup instead of a screen for faster access to debug log.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220118","App System-local user now offers a password change for the user, pending system publish.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220118","Employee Irs1095b details should now work as intended.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220118","Employer Employee search button now changes color (green) to indicate active search.")); 
		// 20220114
		homeChangesList.getItems().add(new HBoxChangesCell("20220114","Fix for Reprocessing Coverage Files.")); 
		// 20220111
		homeChangesList.getItems().add(new HBoxChangesCell("20220111","Fixed inactive accounts from being loaded into HR employee search combo.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220111","Uploader refresh button for single page work loads.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220111","Fix for Irs109xc status indicators in Pipeline Queue.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220111","Employer File History live search and included field expansion.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220111","Employer File History now Mapper Id sortable.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220111","Employer File History Dyn File Spec view now Mapper View.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220111","Missing Info Tax Year fix for Account Calc Calling Codes.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220111","Obsolete Account screen addresses replaced with Benefits section for quicker access.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220111","Fix for Account Benefits that would occasionally leave the benefits list empty.")); 

		// 20220104
		homeChangesList.getItems().add(new HBoxChangesCell("20220104","Uploader User Updates and refinement.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220104","Enhanced Employee CoreID Search logging and refinement.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220104","Account level calc calling codes now has tax year selection.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220104","Employee Calling Codes Capability added.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220104","New Local Cache section on home controller status.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220104","Account file History View Mapper and View Spec buttons launch issue fixed.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220104","Account File History now sortable by  Mapper Id.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220104","Account File History search now incorporates the mapper id and other fields.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220104","Account File History now uses a live search, no apply button needed.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20220104","Account File History View Mapper button correctly labeled (was dyn file spec.)")); 
		// 20211228
		homeChangesList.getItems().add(new HBoxChangesCell("20211228","Employer Tax Years Now Sorted Descending.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211228","Calc Queue Filter now functions.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211228","Calc Queue Columns are now sortable by clicking the headers.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211228","Templates Option removed from Upload Type Add")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211228","Labels are now set correctly in upload type add and edit.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211228","Removed delay after double clicking some failed files would have before showing the raw field display.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211228","Reprocessing requests now creates a new file record, same as completed files.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211228","Color Legend added for calc queue entries")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211228","Employer Screen Employee List Deleted Employee color coding (in red.)")); 
		// 20211221
		homeChangesList.getItems().add(new HBoxChangesCell("20211221","More Uploader tweaks and improvements.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211221","Corrected issue where calling codes button would throw up error message about missing information.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211221","Hr Employee search now sports a copy button for copying the selected employee to the system clipboard.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211221","HR Employee search now indicates deleted employees with red.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211221","Added Employee Deleted Notification to Employee Screen. ")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211221","Fix for saving PayConversionCalculation edits.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211221","Raw field Edits now use combo list instead of free form entries for typed elements (uniontype, etc.)")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211221","Corrected issue where leaving Employee screen before data threads finish could cause an exception.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211221","Cleaned up Calculation Request area of the account screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211221","Fix for occasional Employer tax year exception when Irs object employee not present.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211221","Closed Employer Tax Years now show up as yellow in list.")); 
		// 20211214
		homeChangesList.getItems().add(new HBoxChangesCell("20211214","Raw Field Edits in failed Pipeline Files now support saving.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211214","Corrected bug in Calc submissions that prevented submission from properly registering.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211214","Further Uploader refinements and fixes.")); 
		// 20211207
		homeChangesList.getItems().add(new HBoxChangesCell("20211207","New Calc Request Buttons on Account and Employer Tax Years")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211207","Uploader fixes and improvements")); 
		// 20211130
		homeChangesList.getItems().add(new HBoxChangesCell("20211130","Uploader is now part of the Admin App, accessed through left navigation menu.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211130","Calc Queue Detail for IRS filing now has correct error support.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211130","Calc Queue now color coded for better user interaction.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211130","Wrong credentials notice when user login is incorrect.")); 
		// 20211123
		homeChangesList.getItems().add(new HBoxChangesCell("20211123","New Calc Queue Detail Popup - Irs10945 Filing.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211123","Added Copy Filename button to Account and Employer File History sections.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211123","Added Excel friendly Spreadsheet copy and button to Raw Field Error Grid.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211123","Moved HR Employee search to Xarriot thread pool for better debugging.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211123","Account and Employer should now show on the Calc Queue 109XB and 109XC detail Popups.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211123","Calc Queue filter expanded to include all columns.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211123","Calc Queue filter now uses live filtering for each keystroke.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211123","Calc Queue Expanded Date Range control for reasonable historical uses.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211123","Removed Calc Channel from navigation menu.")); 
		// 20211116
		homeChangesList.getItems().add(new HBoxChangesCell("20211116","Raw Field Error Display fix for select Coverage Type Files.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211116","Individual Calculator Queue Detail Popups for Employee Merger, others.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211116","Added Copy Filename to Pipeline Queue to copy selected entry's filename to system clipboard.")); 
		// 20211111
		homeChangesList.getItems().add(new HBoxChangesCell("20211111","Raw Field Display Correction for select Coverage Files")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211111","Account File History now launches the raw field display when double clicked.")); 
		// 20211109
		homeChangesList.getItems().add(new HBoxChangesCell("20211109","Benefit creation update and functionality")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211109","Calc queue functional again. But new type specific Detail popups are still coming.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211109","New local DB cleanup and functional opening in response to previous launch issue.")); 
		// 20211102
		homeChangesList.getItems().add(new HBoxChangesCell("20211102","New Windows EXE version of the app for proper heap management.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211102","New Benefit popup added on the Account Screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211102","Reprocessing of Completed Files capability added.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211102","Duplicate Mapper Creation bug fixed.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211102","Fix for Mapper Payclass References.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211102","Conversion to Hand icons on remaining lists and tables.")); 
		// 20211020A
		homeChangesList.getItems().add(new HBoxChangesCell("20211020A","Better error logging for pipeline queue thread.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211020A","New debug logging viewer in the user settings area, same button to launch.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211020A","Fix for employee screen load.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211020A","Fix for HR Employee search speed.")); 
		// 20211020
		homeChangesList.getItems().add(new HBoxChangesCell("20211020","Correction for current user identification.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211020","Pipeline Queue fix that was giving erroneous zero record alert.")); 
		// 20211019
		homeChangesList.getItems().add(new HBoxChangesCell("20211019","New Xarriot version of the app, new API under the hood, new Caching DB.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211019","Attempt at a multiple monitor fix, based on errors gathered during operation.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211019","Employee Pays now dynamically loaded, no refresh pays button required.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211019","Employee Employment Period updates fixed.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211019","Employee Coverage Editing now funtional.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211019","Benefit (plan) name added to Coverage tables on employee and dependent.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211019","Employee and Dependent addresses not editable for now, will change later.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211019","Added Zero record user notification in Pipeline Queue when such entry selected.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211019","Raw Dependent error info and details added to Raw Field Error Display")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211019","New system wide Logging mechanics for improved error collection and reporting.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211019","Employee Emp Id now reads ER Ref Id.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211019","DynSpecId header in Spec list on channel changed to MapperId")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211019","Resync capability in pipeline queue corrected to full 168 hours instead of 96.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211019","File History load, thanks to Xarriot, now loads dyanically, no Reload button needed.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20211019","Independent thread loading by section for major HR controllers, such as Account & Employer screens.")); 
		// 20210928
		homeChangesList.getItems().add(new HBoxChangesCell("20210928","Fix for Insurance File types in the Pipeline queue display.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210928","New Employer Add Functionality.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210928","Fix for breadcrumb in new file type handling.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210928","Local Cache support for Insurance and Deduction file Types")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210928","New Reconnect button in settings screen for resetting the local connection object.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210928","Auto connection object discard and creation after repeated queue failures.")); 
		// 20210921
		homeChangesList.getItems().add(new HBoxChangesCell("20210921","ID fix for all of the reference Popups that pull accounts and/or employers.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210921","Although saves are not active just yet, Queue Raw Data is updated with the ability to edit, feedback welcome!")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210921","The Status in the pipeline queue will now show Reprocessed queue entries for easier tracking by the user.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210921","Added in an alert to the user if they try to view pipeline queue entry that has been reprocessed.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210921","Fixed a problem with the local caching of the EmploymentPeriod, which also impacted the Employee Information update.")); 
		// 20210915
		homeChangesList.getItems().add(new HBoxChangesCell("20210915","Fixed an issue that was preventing the creation of upload types.")); 
		// 20210914
		homeChangesList.getItems().add(new HBoxChangesCell("20210914","Added FileId to Pipeline Queue to better track Reprocessing entries.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210914","Updated employee Irs1059c layout, fixed missing values.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210914","Specification Upload Types changes were not removing account and employer ids on edit.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210914","Employee SSN changes should now also change the Employee Information table record.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210914","Fix for employer listings in some mapper reference dialogs when an account filter is used.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210914","Dropped Corvetto label from the version number, no turning back now.")); 
		// 20210907
		homeChangesList.getItems().add(new HBoxChangesCell("20210907","ComboBox bug fixes for all references linked to mappers")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210907","Progress bar fix for all mappers")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210907","Fix for Clearing the Calculator Id on a pipeline specification.")); 
		// 20210904
		homeChangesList.getItems().add(new HBoxChangesCell("20210904","Pipeline Queue Watchdog refinement to filter out future times from processing.")); 
		// 20210902
		homeChangesList.getItems().add(new HBoxChangesCell("20210902","New watchdog process in Pipeline Queue to watch for missing entries and resync if found.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210902","New Resync button in Pipeline Queue to manually call queue data resync.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210902","Queue Date Range selection for completed and failures merged into single selection.")); 
		// 20210901
		homeChangesList.getItems().add(new HBoxChangesCell("20210901","Corrected bug that would prevent account list load for display servicing.")); 
		// 20210831
		homeChangesList.getItems().add(new HBoxChangesCell("20210831","New update process for accounts and employers while active. This impacted different parts of the app.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210831","Employer Active filter added for Pipelne upload types.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210831","Raw Data fix for record rejection information.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210831","1095c data and popup added for Employee screen.")); 
		// 20210826
		homeChangesList.getItems().add(new HBoxChangesCell("20210825","(re)fixed an issue that would prevent the employee pay popup from launching.")); 
		// 20210824
		homeChangesList.getItems().add(new HBoxChangesCell("20210824","Dependent Reference update function fixed.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210824","Location Reference update functionality corrected.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210824","CovGroup Reference modal behavior bug corrected.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210824","Custom Field support added to the Dependent screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210824","Location (and its lookup when needed) added to the Employee screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210824","Irs1095b listing relocated on employee screen and should now load properly.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210824","Batch Id now displays on Employee Pay Data.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210824","Irs1095b Inactives are now present and marked in blue.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210824","Removed Time Zone selection fro Pipeline Queue, letting user system set the time.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210824","Changed Pipeline Queue pull to widen pull for the pay files themselves to helkp with queue entry population.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210824","DWI Queue is now present but data may not be in place just yet.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210824","Pipeline Queue now has the request id for easier troubleshooting and tracking.")); 
		// 20210823
		homeChangesList.getItems().add(new HBoxChangesCell("20210823","Fix for string error that was preventing raw field info from displaying.")); 
		// 20210819
		homeChangesList.getItems().add(new HBoxChangesCell("20210819","Fix for Employee pay bug where primary click would launch context menu")); 
		// 20210817
		homeChangesList.getItems().add(new HBoxChangesCell("20210817","Fixed an issue where zero records in Raw Field Display would cause an exception and not clear status.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210817","Employee custom fields are now editable.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210817","Changed queue update timing mechanics to pick up missing queue entries.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210817","Corrected bug in Raw Field screen where pipelinefilerecordrejections were not displaying.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210817","Employer Tax year columns update and additions.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210817","Employee pay table columns correction.")); 
		// 20210810
		homeChangesList.getItems().add(new HBoxChangesCell("20210810","Expanded columns on the employee pay table in order to better match WISE.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210810","Added the Custom Fields to the Employee Controller.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210810","Reorganized top portion of Employee Screen for better user convenience.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210810","Employee CoverageGroupMembership Detail Popup was not properly loading.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210810","Changed Account Description to Account Website.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210810","Starting implementation of hand w/finger cursor for items that open other screens or popups.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210810","The Employee Screen now includes the verified checkbox for tracking employees with clients.")); 
		// 20210804 
		homeChangesList.getItems().add(new HBoxChangesCell("20210805","Corrected a problem with the dependent lookup on Employee")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210805","Fixed an issue where Employee Coverage Group Memberships would not load.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210805","Calculation Notices are now tied to the selected tax year and should display as expected.")); 
		// 20210803 
		homeChangesList.getItems().add(new HBoxChangesCell("20210803","Corrected issue where entry to Employee from HR Employee would create long data wait times.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210803","Shiny new Insurance Mapper now available for your mapping needs.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210803","Submission Export Notices are now available on the Employer Tax Year Screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210803","Employee Employment Periods should once again be present on the employee screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210803","Fixed bug that prevented the employee coverages table from having data.")); 
		// 20210730 
		homeChangesList.getItems().add(new HBoxChangesCell("20210730","Employee Pays now properly load and display inactive items when so selected.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210730","Employee Pay Refresh speed should be MUCH faster now.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210730","Cached or preloaded Employee Pays should now display by default.")); 
		// 20210729  
		homeChangesList.getItems().add(new HBoxChangesCell("20210729","Corrected bug where newly created Mappers were not keeping the correct file handlers.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210729","Spec list on Pipeline Channel Screen now automaticallychecks for updated specs when entering.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210729","Spec list on Pipeline Channel Screen now sports an update button and a clear filter button.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210729","Bug was preventing dependents from being listed on the employee screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210729","Encryption correction and update for SSN editing.")); 
		// 20210727 
		homeChangesList.getItems().add(new HBoxChangesCell("20210727","New Email search option on the Hr Employee Screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210727","New Raw Field screen layout, with single control, fixed info, and copy capability. Feedback welcome.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210727","Bug fix for inoperative mapper, mapper header clear function, and ID display fix.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210727","Email and Phone now present on the Employee screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210727","Raw Sub file indicator now correctly displays the currently selected sub file.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210727","Raw Display sub file entries were duplicating when hitting update button.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210727","Corrected issue where Pay Mappers were not clearing first name selection.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210727","Hr Employee SSN search should now work again.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210727","Fix for mail addresses on employee screen")); 
		// 20210720 
		homeChangesList.getItems().add(new HBoxChangesCell("20210720","Fix for the additional hours display on the payFile mapper.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210720","Correction for Pipeline Specification Handlers clearing and loading.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210720","Employer Screen has contacts listed again.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210720","Employee Payroll Remove Should now be functional.")); 
		// 20210716 
		homeChangesList.getItems().add(new HBoxChangesCell("20210716","Bug caused Pipeline Specification changes to revert in spec display until restarted.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210716","Account Inactive Benefits are now active and selectable.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210716","Employer Inactive Pay Periods are now active and selectable.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210716","Fixed problem where some Employer Coverage Groups were not loading in the group display list.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210716","Employer Departments, where available, should now properly display.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210716","Employer Locations, where available, should now properly display.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210716","Employee searches in HR and Employer are now faster.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210716","The Pipeline Queue now uses a live filter for better user convenience, just type.")); 
		// 20210714 
		homeChangesList.getItems().add(new HBoxChangesCell("20210714","Updated Raw Queue mapper display to include last column data element assignment.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210714","Pipeline Specification Edits would not save the file step handler changes.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210714","Employee search on the Employer Screen would list employees in same account, wrong employer.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210714","The inactive checkbox on the Employer's Employee search should now work correctly.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210714","Inactive Accounts, Employers and Employees are once again present in the app.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210714","HR sourced employee screen would not properly back into the employer and/or account screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210714","Spec filter on the pipeline channel screen now uses a live filter, just type instead of needing to hit filter button.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210714","HR Account now uses a live filter.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210714","HR Employer now uses a live filter.")); 
		// 20210713 
		homeChangesList.getItems().add(new HBoxChangesCell("20210713","Fixed issue where employee selection on employer screen could result in wrong employee selected")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210713","Employee search was not listing all employees where person id was shared. This is now corrected.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210713","Raw Error display now includes blank row between raw employees for better data grouping.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210713","Raw Error display uses softer color for errors.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210713","Raw Error row display expanded to use available screen space.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210713","Initial System Status display on home is delayed until after app start to avoid thread contention.")); 
		// 20210712 
		homeChangesList.getItems().add(new HBoxChangesCell("20210712","Pipeline Specification Edits Should Now Save")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210712","Raw Errors should now pull all errors for a given file.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210712","Raw Errors should be much faster now")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210712","Raw Errors for files that had subfiles should now work, please select subfile and hit update for these types.")); 
		//20210709 
		homeChangesList.getItems().add(new HBoxChangesCell("20210709","Temporary change to limit raw errors. This will allow large files to pull until problem is corrected.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210709","Raw pays and coverages should now be grouped with their raw employee for easier reading.")); 
		// 20210708A 
		homeChangesList.getItems().add(new HBoxChangesCell("20210708A","Under certain circumstances a Raw type coverage queue entry would not display the raw errors. This has been fixed.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210708A","Corrected an issue in HR Employee search when searching by CoreId")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210708A","Corrected where Pay Dates on the Employee screen pays were showing up blank.")); 
		// 20210708 
		homeChangesList.getItems().add(new HBoxChangesCell("20210708","The save for group changes to the employee pays should be functioning again.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210708","Corrected upload type list where the Employer and Account names were expecting object not id, casuing blanks.")); 
		// 20210707B 
		homeChangesList.getItems().add(new HBoxChangesCell("20210707B","The HR Account Screen now has the ability to add an account. This feature is still in testing only.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210707B","The new calcuation notices on the tax year screen should now be functional, where available.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210707B","The Colored Mapper Display on the Raw Field Screen is now updated to include added fields.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210707B","Select references on Mappers are now sorted for your referencing convenience.")); 
		// 20210707A 
		homeChangesList.getItems().add(new HBoxChangesCell("20210707A","Fixes issue where removed mapper data elements would not save.")); 
		// 20210707 
		homeChangesList.getItems().add(new HBoxChangesCell("20210707","Upload types adds and edits should now work as they did before.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210707","Parse/format button fix for affected elements on mappers.")); 
		// 20210706 
		homeChangesList.getItems().add(new HBoxChangesCell("20210706","Employee pay edits should now update properly again.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210706","Added last part of handler class to name for distinction, filtered on channel id on new Mappers.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210706","Upload type details when selected on the specification screen were not being properly fetched and displayed.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210706","Correction for Null column in doctype and upload handler in Upload adds and edits.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210706","Account and Employer local storage now uses PostalAdress objects.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210706","Show SSN on Employee and Dependent screesn should now work as intended.")); 
		//20210703 
		homeChangesList.getItems().add(new HBoxChangesCell("20210703","Raw field error display accessible again from a double click in the pipeline queue")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210703","Additonal fixes for mappers and select associated references")); 
		//20210702B 
		homeChangesList.getItems().add(new HBoxChangesCell("20210702B","Fix for Queue Date bug not pulling new, fixes for mappers and select associated references")); 
		//20210702 
		homeChangesList.getItems().add(new HBoxChangesCell("20210702","First production release running on the Corvetto API")); 
		// 20210518 
		homeChangesList.getItems().add(new HBoxChangesCell("20210518","Added Gender data element and Reference to PayFile mapper.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210518","Updated Employer Reference form.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210518","Security Enhancement: Right click test mode removed from login.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210518","Security Enhancement: Context menu removed from login.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210518","Internal Code-Based Security enhancements.")); 
		// 20210511 
		homeChangesList.getItems().add(new HBoxChangesCell("20210511","Correction for combo box issues in the Pay Frequency Reference dialog.")); 
		// 20210504 
		homeChangesList.getItems().add(new HBoxChangesCell("20210504","More robust exception handling for the tax year screen data.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210504","New logging types - CRITICAL and WARNING for better error tracking and future user notice expansion.")); 
		// 20210430 
		homeChangesList.getItems().add(new HBoxChangesCell("20210430","Fix for Payroll Mapper Hours and new layout for that screen.")); 
		// 20210427 
		homeChangesList.getItems().add(new HBoxChangesCell("20210427","A brand new Pay Mapper Screen is the big news in this release. It replaces the old pay mapper.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210427","Mapper screens were changed to allow different editing orders on the patterns.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210427","Changes to the pipeline queue request mechanism to gather other objects that may be out of sync.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210427","Corrected a condition where pipeline channel was changed elsewhere, causing wrong mapper screen to load.")); 
		// 20210420 
		homeChangesList.getItems().add(new HBoxChangesCell("20210420","New Status section on the home screen, showing green/red status of the system Calc, Pipeline, and Exporter.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210420","Change to pipeline queue query time window in order to curtail missing out of sync entries in the queue.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210420","Dependent addresses now default to Employee's when blank to allow for editing.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210420","Correction for reference button image on the employee mapper.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210420","Add Pipeline/Mapper Popup calc combo corrected to show name instead of description.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210420","Add Pipeline/Mapper Popup calc combo no longer turns white or clear during use.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210420","Employer screen now uses the new Address class used elsewhere with addresses.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210420","Address class now has a label identifying it as mail or billing.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210420","Address Class fonts changed to match the other dialogs.")); 
		// 20210413 
		homeChangesList.getItems().add(new HBoxChangesCell("20210413","New Mapper Screens - Fixed bug with header checkboxes changing status when parse/format pattern changed on form.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210413","New Mapper Screens - Fixed bug issue with parse pattern not clearing and saving properly.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210413","New Mapper Screens - Fixed issue with index sometimes not allowing element assignment.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210413","New Mapper Screens - Correction for different entry sequence (all checkboxes first, etc.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210413","New Address class generating address sections based on Country type - also corrects tab order issue.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210413","Home screen now shows version information and latest program updates history")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210413","About button info on Main Screen reduced to show program info")); 
		// 20210406 
		homeChangesList.getItems().add(new HBoxChangesCell("20210406","The Mapper screen fixed an issue with header spacing and displaying ptn/fmt IDs.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210406","The Mapper screen index now disables when the box is unchecked")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210406","DOBs before 1970 would show up as null due to the mechanics of Epoch time. This has been corrected.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210406","Verified fields are removed from all addresses.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210406","The employee screen has a new address layout controlled by the chosen country.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210406","The employer screen for a noncached (first visit) employer should now load faster.")); 
		// 20210330 
		homeChangesList.getItems().add(new HBoxChangesCell("20210330","The big news for this release is a shiny new mapper screen for type employee.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210330","The tax year screen has changed from showing all 1095cFiling to only the abandoned ones, along with a 1094c optimization. This should greatly help with the speed on a large account.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210330","Tax Years are no longer editable. This may change in a future version as business needs change.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210330","The bread crumb for employees now includes the employer name.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210330","The employee screen would not reset the DOB field when the next employee had a null or empty DOB. This has been corrected.")); 
		// 20210324 
		homeChangesList.getItems().add(new HBoxChangesCell("20210324","New Reprocess Request button (blue button, top right) on the Raw Field Screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210324","Fix for missing data entries on Employee Screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210324","Inactive Employers on Account screen should now list correctly in blue.")); 
        // 20210316
		homeChangesList.getItems().add(new HBoxChangesCell("20210316","New Coverage Mapper now includes the PArse Pattern and Date Formats Ids in the mapper layout.")); 
        homeChangesList.getItems().add(new HBoxChangesCell("20210316","Employer screen now incorporates an employee, search similar to the HR Employee.")); 
        homeChangesList.getItems().add(new HBoxChangesCell("20210316","Fix for missing dates on Employee Screen.")); 
        homeChangesList.getItems().add(new HBoxChangesCell("20210316","Correct coverage mapper now loads from the Raw Field screen.")); 
        // 20210309
        homeChangesList.getItems().add(new HBoxChangesCell("20210309","New Approve button on the raw detail screen for the postValidation Approval process.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210309","Data properties are now included on the employee screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210309","Further refinements and enhancements for the Coverage Mapper screen.")); 
		// 20210302
		homeChangesList.getItems().add(new HBoxChangesCell("20210302","This version incorporates a fix for the parse patterns in the new mapper screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210302","There are other various minor fixes in the app as well.")); 
		// 20210223
		homeChangesList.getItems().add(new HBoxChangesCell("20210223","This version incorporates a fix for the new mapper screen when a clear and an immediate save action was performed.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210223","There are also minor fixes for that screen, including corrected editing actions and updates, more uniform fonts and a saving indicator.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210223","The Tax Year filing information should now cache properly and perform much faster on repeat visits.")); 
		// 20210216
		homeChangesList.getItems().add(new HBoxChangesCell("20210216","Big News: In this version we offer the latest and greatest Mapper editor for Coverage files.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210216","The Tax Year screen has been revamped again and now includes employee info for 1095Filings when available.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210216","The 1095Filings list also includes controls for filtering and checkboxes for showing only errors and/or abandonments.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210216","Screen layout update in order to show more information in the case of large AirFilingEvent descriptions.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210216","Miscl. bug fixes and updates.")); 
		// 20210209
		homeChangesList.getItems().add(new HBoxChangesCell("20210209","Editing is now enabled for the taxYear's Irs1094b and Irs1094c objects on that screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210209","Employee employment periods now show inactve, and they can be set active and inactive as needed.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210209","Employee Coverages are now displayed in a table instead of a list, all columns inthe table are sortable.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210209","Employees now load all the data except pays on initial load.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210209","The refresh button, which now refreshes only the pays, is moved to that section.")); 
		// 20210202
		homeChangesList.getItems().add(new HBoxChangesCell("20210202","The filing section on the TaxYear screen now supports multiple Irs1094Filings for a given tax year, defaulting to the prime.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210202","The user can select other 1094filings, when available, by double clicking the entry in the list")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210202","In this version we now have access to the AirTransmission for a given Irs1094Submission.")); 
		// 20210126
		homeChangesList.getItems().add(new HBoxChangesCell("20210126","New Filing data structure and location. Now all filing data is based on the tax year from the employer screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210126","Selecting a tax year will show the filing info on the detail screen, along with additional popups.")); 
		// 20210119
		homeChangesList.getItems().add(new HBoxChangesCell("20210119","New Employee Refresh Data button. For faster employee browsing, some of the employee data has moved to refresh on demand. Cached data loads automatically.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210119","New Filing section on the Employere screen. this can be accessed by clicking the filing button on the top right of that screen.")); 
		homeChangesList.getItems().add(new HBoxChangesCell("20210119","Pipeline specifications now update on startup of app. This is to ensure that all of the pipeline specs are current.")); 
	} 
	 
	@FXML 
	private void onManualUpdate(ActionEvent event){ 
		setStatus(); 
	} 
	 
	public static class HBoxChangesCell extends HBox  
	{ 
         Label lblVersion = new Label(); 
         Label lblDescription = new Label(); 
 
         HBoxChangesCell(String version, String description)  
         { 
              super(); 
              if (version != null) { 
	           	  Utils.setHBoxLabel(lblVersion, 100, false, version); 
	           	  Utils.setHBoxLabel(lblDescription, 1000, false, description); 
              } else { 
	           	  Utils.setHBoxLabel(lblVersion, 100, true, "Version"); 
	           	  Utils.setHBoxLabel(lblDescription, 1000, true, "Description"); 
              } 
        	  this.getChildren().addAll(lblVersion, lblDescription); 
         } 
    }	 
 
	 
	 
} 
