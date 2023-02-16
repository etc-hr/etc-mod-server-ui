package com.etc.admin;

import java.awt.HeadlessException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;

import com.etc.admin.data.DataManager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UpdateApp extends Application implements Serializable {

	private static final long serialVersionUID = 12520837763260183L;		

	private static Stage primaryStage = null;
    Label lblMessage = new Label();
			
	@Override
	public void start(Stage stage) {
		setPrimaryStage(stage);

		try {
			primaryStage.setTitle("Version Out Of Date");

			// Create the VBox
	        VBox root = new VBox();
	         
	        // Set the width and height of the VBox
	        root.setMinWidth(450);
	        root.setMinHeight(150);

	        Parameters params = this.getParameters();
	        List<String> paramStrings = params.getUnnamed();
	        if (paramStrings !=  null && paramStrings.size() > 0)
	        	lblMessage.setText(paramStrings.get(0).toString());
	        else
	        	lblMessage.setText("This version is out of date. Please update to the latest version of the Admin App.");
	        root.getChildren().add(lblMessage);
	         
	        // Give it a littel bling
	        root.setStyle("-fx-padding: 10;" +
	                "-fx-border-style: solid inside;" +
	                "-fx-border-width: 2;" +
	                "-fx-border-insets: 50;" +
	                "-fx-border-radius: 5;"  +
	                "-fx-border-color: transparent;");
	         
	        // Create the Scene
	        Scene scene = new Scene(root);
	        // Add the Scene to the Stage
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.DECORATED);
			primaryStage.getIcons().add(new Image("img/Icon.jpg"));
			positionStageCenter(primaryStage);
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
			Platform.exit();
		}
	}

	public void setMessage(String message) {
		lblMessage.setText(message);
	}
	///////////////////////////////////////////////////////////////////////////
	// Stage helpers
	///////////////////////////////////////////////////////////////////////////
	private void setPrimaryStage(Stage stage) {
		primaryStage = stage;
	}

	public Point getCurrentScreenCenter() {
		// using the mouse position to determine current screen
		Rectangle2D screenBounds = null;
		Point p = null;
		try {
            p = MouseInfo.getPointerInfo().getLocation();
            List<Screen> screens = Screen.getScreens();
            if (p != null && screens != null) {
                // check mouse position
                for (Screen screen : screens) {
                    screenBounds = screen.getVisualBounds();
                    if (screenBounds.contains(p.x, p.y)) {
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
	public void exitApp() {
        Platform.exit();
        EmsApp.getInstance().destroyApplication();
	}
	
	@Override
	public void stop()  { exitApp(); }
		
}
