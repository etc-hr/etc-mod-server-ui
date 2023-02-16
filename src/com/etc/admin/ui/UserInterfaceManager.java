package com.etc.admin.ui;

import com.etc.admin.ui.main.MainController;

import javafx.scene.Parent;

public class UserInterfaceManager {
	
	private static MainController mainController = null;
	
	public static void requestCenter(Parent item) {
		if (mainController != null) {
			mainController.getBorderPane().setCenter(item);
		}
	}
	
	public static MainController getMainController() {
		return mainController;
	}

	public static void setMainController(MainController mainController) {
		UserInterfaceManager.mainController = mainController;
	}

}
