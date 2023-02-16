package com.etc.admin.ui.search;

import javafx.fxml.FXML;

import javafx.scene.layout.HBox;

import com.etc.admin.EtcAdmin;

import javafx.event.ActionEvent;

import javafx.scene.control.MenuBar;

import javafx.scene.control.MenuItem;

import javafx.scene.control.Menu;

public class SearchController {
	
	@FXML
	private HBox parentBox;
	@FXML
	private MenuBar accountMenuBar;
	@FXML
	private Menu accountMenu;
	@FXML
	private MenuItem logoutMenuItem;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		EtcAdmin.i().getPrimaryStage().setResizable(true);
	}

	// Event Listener on MenuItem[#logoutMenuItem].onAction
	@FXML
	public void logoutMenuAction(ActionEvent event) {
	}
}
