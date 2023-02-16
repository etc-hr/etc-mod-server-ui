package com.etc.admin.ui.org;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ViewOrganizationController{
	
	@FXML
	private Label orgNameLabel;
	@FXML
	private Label orgDescriptionLabel;
	@FXML
	private Label orgParentOrganizationLabel;
	@FXML
	private Label orgMAddrStateLabel;
	@FXML
	private Label orgMAddrZipLabel;
	@FXML
	private Label orgMAddrStreetLabel;
	@FXML
	private Label orgMAddrCityLabel;
	@FXML
	private Label orgBAddrStateLabel;
	@FXML
	private Label orgBAddrZipLabel;
	@FXML
	private Label orgBAddrStreetLabel;
	@FXML
	private Label orgBAddrCityLabel;
	@FXML
	private ListView<Object> orgAccountsListView;

	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	private void initialize() {
		LoadMemberData();
	}
	
	
	public void LoadMemberData() {
		
	/*	if (DataManager.i().currentOrg != null) {
			orgDescriptionLabel.setText(DataManager.i().currentOrg.getDescription());
			orgParentOrganizationLabel.setText(DataManager.i().currentOrg.getName());
			orgMAddrStateLabel.setText(DataManager.i().currentOrg.getMailState());
			orgMAddrZipLabel.setText(DataManager.i().currentOrg.getMailZip());
			orgMAddrStreetLabel.setText(DataManager.i().currentOrg.getMailStreet());
			orgMAddrCityLabel.setText(DataManager.i().currentOrg.getMailCity());
			orgBAddrStateLabel.setText(DataManager.i().currentOrg.getBillState());
			orgBAddrZipLabel.setText(DataManager.i().currentOrg.getBillZip());
			orgBAddrStreetLabel.setText(DataManager.i().currentOrg.getBillStreet());
			orgBAddrCityLabel.setText(DataManager.i().currentOrg.getBillCity());
		} */ 
	}
}
