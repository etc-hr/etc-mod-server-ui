package com.etc.admin;

import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.io.FileUtils;

import com.etc.CoreException;
import com.etc.admin.data.DataManager;
import com.etc.admin.ui.login.LoginController;
import com.etc.admin.ui.main.MainController;
import com.etc.admin.utils.ViewAppStatusController;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.admin.utils.Utils.StatusMessageType;
import com.etc.corvetto.CorvettoConnection;
import com.etc.utils.xarriot.Xarriot;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EtcAdmin extends Application implements Serializable {

	private static final long serialVersionUID = 12520837763260183L;		

	private static Stage primaryStage = null;
	private Parent loginRootNode = null;

	/////////////////////////////////////////////
	// type members
	/////////////////////////////////////////////
	public ScreenType mScreenType = null;
			
	/////////////////////////////////////////////
	// control members
	/////////////////////////////////////////////
	public static MainController mMainController;
	public static LoginController mLoginController;
	
	public static boolean mbDebug = false; //member to control the debug output

	/////////////////////////////////////////////
	// version
	/////////////////////////////////////////////
	public String version = "20220215";
	
    /////////////////////////////////////////////
	// special UUID Update
	/////////////////////////////////////////////
	public boolean resetUUID = false;
	public boolean mDbQueueReset = false;
	
	/////////////////////////////////////////////
	// screen refresh
	/////////////////////////////////////////////
	public boolean mRefreshScreen = false;
	public ScreenType mLastScreenType;
	public int activeScreen = 0;
	
	/////////////////////////////////////////////
	// access this class everywhere
	/////////////////////////////////////////////
    private static EtcAdmin mInstance;
    public static EtcAdmin i() {
        return mInstance;
    }	
		
//	public static void main(String[] args) {
//		launch(args);
//	}
		
    //public Logger logger = Logger.getLogger("MyLog");  
    //public FileHandler fh;  
    
	ViewAppStatusController appStatusController = null;
	Stage appStatusStage = null;
	public boolean showAppStatus = false;
	public String appStatusMessage = "";
	public String appStatusLabel = "";
	public double appStatusProgress = 0;
    
	@Override
	public void start(Stage stage) {

		mInstance = this;
	    try {  
	    	// turn off the debugging
	    	setDebug(false);
	    } catch (Exception e) {  
        	DataManager.i().log(Level.SEVERE, e); 
	    }		
		setPrimaryStage(stage);

		try {
			checkForRunningApp();
			setTitle();

			FXMLLoader loader = new FXMLLoader();

			// Load LoginForm
			loader.setLocation(this.getClass().getResource("ui/login/Login.fxml"));

			// Get LoginForm root node
			loginRootNode = loader.load();
			
			// Display LoginForm
			primaryStage.setScene(new Scene(loginRootNode));
			primaryStage.initStyle(StageStyle.DECORATED);
			primaryStage.getIcons().add(new Image("img/Icon.jpg"));
			primaryStage.show();
			
			createAppStatus();
			
		} catch (Exception e) {
			e.printStackTrace();
			Platform.exit();
		}
	}
	
	private boolean checkForRunningApp() {
		try {
			File dbFile = new File(EmsApp.getInstance().getDbPath());
			if(dbFile.exists() == true &&  dbFile.renameTo(dbFile) == false)
			{
		      	Alert alert = new Alert(AlertType.NONE, "The Admin App is already running or the local db file is locked. Please exit any other instances of the app before continuing. If problems persist, contact systems.", ButtonType.OK);
		      	alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		      	alert.setTitle("Admin App Already Running");
		      	alert.showAndWait();
			    exitApp();
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}	
	
	public boolean stageOverlapsMain(Stage stage) {
		// start overlaps
		if (stage.getX() > primaryStage.getX() && stage.getX() < primaryStage.getX()) return true;
		// width overlaps
		if (stage.getX() + stage.getWidth() > primaryStage.getX() && stage.getX() < primaryStage.getX() + primaryStage.getWidth()) return true;
		
		
		return false;
		
	}
	
	private void setTitle() {
		try {
			if(Boolean.valueOf(Xarriot.getInstance().getProperties().getProperty(Xarriot.PROD_ENV, Boolean.TRUE.toString())))
				if (mbDebug == true)
					primaryStage.setTitle("Admin App v." + EmsApp.getInstance().getApplicationProperties().getProperty(CorvettoConnection.APP_VSN, "0.0.0") + "          *** DEBUG MODE***"); // + Test Only Version 20220919.1");
				else
					primaryStage.setTitle("Admin App v." + EmsApp.getInstance().getApplicationProperties().getProperty(CorvettoConnection.APP_VSN, "0.0.0")); // + " Test Only Version 20220929.1");
			else
				if (mbDebug == true)
					primaryStage.setTitle("DEV Admin App v." + EmsApp.getInstance().getApplicationProperties().getProperty(CorvettoConnection.APP_VSN, "0.0.0") + "          *** DEBUG MODE***");
				else
					primaryStage.setTitle("DEV Admin App v." + EmsApp.getInstance().getApplicationProperties().getProperty(CorvettoConnection.APP_VSN, "0.0.0"));
		} catch (Exception e) {
			//DataManager.i().log(Level.SEVERE, e); 
		}
	
	}
	
	private void createAppStatus() {
				
		try {
            // load the fxml
	        FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("utils/ViewAppStatus.fxml"));
			Parent ControllerNode = loader.load();
			appStatusController = (ViewAppStatusController) loader.getController();
	        appStatusStage = new Stage();
	        appStatusStage.initModality(Modality.APPLICATION_MODAL);
	        appStatusStage.initStyle(StageStyle.UNDECORATED);
	        appStatusStage.setScene(new Scene(ControllerNode));  
		} catch (IOException e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}        			
	}
	
	public void showAppStatus(String topLabel, String message, double progress, boolean show) {
		if (show == true)
			appStatusController.startUpdates();
		appStatusMessage = message;
		appStatusLabel = topLabel;
		appStatusProgress = progress;
		showAppStatus = show;
	}
	
	public void hideAppStatus()
	{
		showAppStatus("", "", 0, false);
	}
	
	///////////////////////////////////////////////////////////////////////////
	// Stage helpers
	///////////////////////////////////////////////////////////////////////////
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	private void setPrimaryStage(Stage stage) {
		primaryStage = stage;
	}

	public void setPrimaryStageMaximized() {
		// make it resizable for when it is not maximized
		EtcAdmin.i().getPrimaryStage().setResizable(true);
		
		// using the mouse position to determine which screen we are on
		try {
            Point p = MouseInfo.getPointerInfo().getLocation();
            List<Screen> screens = Screen.getScreens();
            if (p != null && screens != null && screens.size() > 0) {
                Rectangle2D screenBounds;
                // Go through each screen to see if the mouse is currently on that screen
                for (Screen screen : screens) {
                    screenBounds = screen.getVisualBounds();
                    if (screenBounds.contains(p.x, p.y)) {
                		primaryStage.setX(screenBounds.getMinX() - 9);
                		primaryStage.setY(screenBounds.getMinY());
                		primaryStage.setWidth(screenBounds.getWidth() + 18);
                		primaryStage.setHeight(screenBounds.getHeight() + 9);
                		EtcAdmin.i().getPrimaryStage().setMaximized(true);
                		return;
                    }
                    activeScreen++;
                }
            }
        } catch (HeadlessException e) {
        	DataManager.i().log(Level.SEVERE, e); 
        }		
	}
	
	public Point getCurrentScreenCenter() {
		Rectangle2D screenBounds = null;
		Point p = new Point();
		try {
            List<Screen> screens = Screen.getScreens();
            if (screens != null) {
            	// base it on the current screen of the primary stage
                for (Screen screen : screens) {
                    screenBounds = screen.getVisualBounds();
                    double psX =   primaryStage.getX() + (primaryStage.getWidth() / 2);
                    double psY =   primaryStage.getY() + (primaryStage.getHeight() / 2);
                    if (screenBounds.contains(psX, psY)) {
                    	p.x = (int) (screenBounds.getMinX() + screenBounds.getWidth() / 2.0);
                    	p.y = (int) (screenBounds.getMinY() + screenBounds.getHeight() / 2.0);
                    	return p;
                    }
                }
            }
 
        } catch (HeadlessException e) {
        	DataManager.i().log(Level.SEVERE, e); 
        }		
		return p;
	}
	
	public void positionStageCenter(Stage stage)
	{
		
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	Point p = getCurrentScreenCenter();
		        stage.setX(p.getX() - (stage.getWidth() / 2));
		        stage.setY(p.getY() - (stage.getHeight() / 2));
		        
		    }
		}); 
	}
	
	public void positionAlertCenter(Alert alert)
	{
		
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	Point p = getCurrentScreenCenter();
		    	alert.setX(p.getX() - (alert.getWidth() / 2));
		    	alert.setY(p.getY() - (alert.getHeight() / 2));
		        
		    }
		}); 
	}
	
	public void showMain() {
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setAlwaysOnTop(false);
	}
	
	///////////////////////////////////////////////////////////////////////////
	// utilities
	///////////////////////////////////////////////////////////////////////////
    public void updateTitle(final String title) {
    	
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	            primaryStage.setTitle(title);
	        }
	   });    		
    }

	public void setDebug(boolean bDebug) {
		mbDebug = bDebug;	
		Properties props = new Properties();
		try 
		{ 
			if (mbDebug == true) {
				props.setProperty("com.etc.utils.ws.level", "FINER");
				props.setProperty("java.util.logging.FileHandler.level", "FINER");
				Utils.alertUser("WARNING", "Debug Mode can write very large log files. Please use only as needed and turn off accordingly. Debug mode will reset to off when restarting the EmsApp.");
			}else {
				props.setProperty("com.etc.utils.ws.level", "INFO");
				props.setProperty("java.util.logging.FileHandler.level", "INFO");
			}
			EmsApp.getInstance().updateAppLoggingProperties(props);
			setTitle();
		} catch (Exception e) { DataManager.i().log(Level.SEVERE, e);  } 
	}
	
	public boolean isDebug() {
		return mbDebug;
	}

	public void setMainController(MainController pMainController) {
			mMainController = pMainController;
	}
	
	public void setLoginProgress(double position) {
		if (mLoginController == null) return;
		
		mLoginController.setProgress(position);				
	}
	
	public void setLoginController(LoginController pLoginController) {
		// store the controller for access later
		mLoginController = pLoginController;
	}
	
	public void setProgress(double progress) {
		if (progress > .99) progress = .99;
		mMainController.setProgress(progress);
	}	
	
	public double getProgress()
	{
		if(mMainController.getProgress() == null)
			return 0.0;
		else
			return mMainController.getProgress();
	}

	public void updateStatus(double pos, String message) {
		setProgress(pos);
    	setStatusMessage(message);		
	}

	public void setStatusMessage(String message) {
		if (mMainController != null)
			mMainController.setStatusMessage(message, StatusMessageType.INFO);
	}	

	public void loadWOTrackingEmployers() {
		mMainController.loadWOTrackingEmployers();
	}	

	public void setStatusMessage(String message, StatusMessageType type) {
		mMainController.setStatusMessage(message, type);
	}	
	
	//public void disableMainInterface(boolean disable) {
	//	mMainController.getAnchorPane().setDisable(disable);
	//}

	public void restartApp() {
		primaryStage.close();
		Platform.runLater( () -> new EtcAdmin().start( new Stage() ) );
	}
	
	public void exitApp() {
        Platform.exit();
        EmsApp.getInstance().destroyApplication();
	}
	
	@Override
	public void stop()  { exitApp(); }
		
	///////////////////////////////////////////////////////////////////////////
	// Screen Type
	///////////////////////////////////////////////////////////////////////////
	public ScreenType getScreenType() {
		return mScreenType;
	}
	
	public void setScreenType(ScreenType screenType) {
		mScreenType = screenType;
	}
	
	public void setScreenRefresh(boolean refresh) {
		mRefreshScreen = refresh;
	}

	public boolean isScreenRefresh() {
		return mRefreshScreen;
	}
	public void setScreen(ScreenType screenType) {
		setScreen(screenType, mRefreshScreen);
	}
	
	public void setScreen(ScreenType screenType, boolean refresh) {
		mLastScreenType = mScreenType;
		mScreenType = screenType;
		mRefreshScreen = refresh;
		mMainController.setScreen(screenType);
		//reset the refresh after teh screen load
		mRefreshScreen = false;
	}
	
	public void setBreadCrumbLabel(int breadCrumbID, String message) { 
	//	mMainController.setBreadcrumbLabel(breadCrumbID, message);
	}

	///////////////////////////////////////////////////////////////////////////
	// Pass through Functions
	///////////////////////////////////////////////////////////////////////////
	public void updateSearchAccounts() {
		mMainController.loadSearchAccounts();
	}
	
	public void updateLocalUserName() {
		mMainController.setLocalUserName();
	}
}
