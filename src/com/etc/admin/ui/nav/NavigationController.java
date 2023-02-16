package com.etc.admin.ui.nav;

import java.util.logging.Level;

import com.etc.admin.AdminManager;
import com.etc.admin.data.DataManager;
import com.etc.admin.ui.UserInterfaceManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NavigationController {
	
	@FXML
	private ScrollPane navScrollPane;
	@FXML
	private VBox navVBox;
	@FXML
	private HBox navOrgHBox;
	@FXML
	private HBox navAccountHBox;
	@FXML
	private HBox navEmployerHBox;
	@FXML
	private HBox navHubHBox;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		
	}
	
	@FXML
	private void navOrgMouseEvent(MouseEvent mouseEvent) {
		try {
			//load the data to our application
			//EtcAdmin.setOrg(newOrg);

			
			FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/org/ViewOrganization.fxml"));
			Parent org = loader.load();
			UserInterfaceManager.requestCenter(org);
			
			
		} catch (Exception e) {
        	DataManager.i().log(Level.INFO, e); 
		}
	}
	
	@FXML
	private void navAccountMouseEvent(MouseEvent mouseEvent) {
		try {
			FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/account/ViewAccount.fxml"));
			Parent account = loader.load();
			UserInterfaceManager.requestCenter(account);
		} catch (Exception e) {
        	DataManager.i().log(Level.INFO, e); 
		}
	}
	
	@FXML
	private void navEmployerMouseEvent(MouseEvent mouseEvent) {
		try {
			FXMLLoader loader = new FXMLLoader(AdminManager.class.getResource("ui/employer/ViewEmployer.fxml"));
			Parent account = loader.load();
			UserInterfaceManager.requestCenter(account);
		} catch (Exception e) {
        	DataManager.i().log(Level.INFO, e); 
		}
	}

}
