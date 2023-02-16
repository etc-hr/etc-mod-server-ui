package com.etc.admin.utils;

import java.util.logging.Level;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ViewAppStatusController 
{
	@FXML
	private TextField statusField;
	@FXML
	private Label topLabel;
	@FXML
	private ProgressBar progressBar;
	
	private Stage stage = null;
	private Timeline timeline = null;
	
	private boolean ran = false;

	/**
	 * initialize is called when the FXML is loaded
	 */
	
	
	@FXML
	public void initialize() 
	{
		setAction();
	}
	
	public void startUpdates() {
		timeline.play();
	}
	
	private void setAction() {
		
		if (ran == true) return;
		ran = true;
		try {
			timeline = new Timeline(new KeyFrame(Duration.seconds(1),
	            new EventHandler<ActionEvent>()
	            {
	                @Override
	                public void handle(ActionEvent event)
	                {
	                	if (EtcAdmin.i() != null && EtcAdmin.i().showAppStatus == true) {
	                		statusField.setText(EtcAdmin.i().appStatusMessage);
	                		topLabel.setText(EtcAdmin.i().appStatusLabel);
	                		
	                		if (EtcAdmin.i().appStatusProgress > 0) {
	                			progressBar.setVisible(true);
	                			progressBar.setProgress(EtcAdmin.i().appStatusProgress);
	                		}else
	                			progressBar.setVisible(false);
	                		
		            		stage = (Stage) topLabel.getScene().getWindow();
	                		stage.show();
	                	}
	                	else
	                		if (stage != null && stage.isShowing() == true) {
	                			stage.hide();
	                			timeline.pause();
	                		}
	                }
	            }));

			timeline.setCycleCount(Animation.INDEFINITE);
			//timeline.play();
		}catch(Exception e) {
			DataManager.i().log(Level.SEVERE, e);
		}
	}

	public void setLabel(String labelText)
	{
		topLabel.setText(labelText);
	}
	
	public void setStatus(String labelText, String statusText, double progress)
	{
		statusField.setText(statusText);
		if (progress < 0) progress = 0;
		
		progressBar.setProgress(progress);
		if (progress == 0)
			progressBar.setVisible(false);
		else
			progressBar.setVisible(true);
	}
	
	public void closePopup() 
	{
		Stage stage = (Stage) topLabel.getScene().getWindow();
		stage.close();
	}
	
}
