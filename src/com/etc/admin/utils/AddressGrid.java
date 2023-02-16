package com.etc.admin.utils;


import java.util.logging.Level;

import com.etc.admin.data.DataManager;
import com.etc.corvetto.entities.PostalAddress;
import com.etc.corvetto.entities.PostalAddressSnapshot;
import com.etc.utils.types.CountryType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AddressGrid {

	private Label titleLabel = new Label("Mail");
	private Label countryLabel = new Label("Ctry");
	private ComboBox<CountryType> countryCombo = new ComboBox<CountryType>();
	private GridPane mainGrid;
	private PostalAddress address;
	
	private Label cityLabel = new Label("City");
	private TextField cityField = new TextField();
	private Label streetLabel = new Label("Street");
	private TextField streetField = new TextField();
	private Label unitLabel = new Label("Unit");
	private TextField unitField = new TextField();
	private Label stateLabel = new Label("State");
	private ComboBox<String> stateCombo = new ComboBox<String>();
	private Label zipLabel = new Label("Zip");
	private TextField zipField = new TextField();
	private Label zip4Label = new Label("Zip4");
	private TextField zip4Field = new TextField();
	private Label qtrLabel = new Label("Qtr");
	private TextField qtrField = new TextField();
	private Label provinceLabel = new Label("Prov");
	private TextField provinceField = new TextField();
	private Label deptLabel = new Label("Dept");
	private TextField deptField = new TextField();
	
	private boolean editMode = false;
	private String title = "Mail";
	private boolean markChanges = false;
	String defaultStyle = "";
	
	public void createAddress(GridPane grid, PostalAddress empAddress, String title) {
		init();
		
		markChanges = false;
		mainGrid = grid;
		// create an address if it is null
		address = new PostalAddress();
		if (empAddress != null)
			address.setId(empAddress.getId());
		// copy over the values
		if (empAddress != null) {
			address.setCity(empAddress.getCity());
			address.setCountry(empAddress.getCountry());
			address.setDepartment(empAddress.getDepartment());
			address.setQuarter(empAddress.getQuarter());
			address.setStprv2(empAddress.getStprv2());
			address.setStreet(empAddress.getStreet());
			address.setUnit(empAddress.getUnit());
			address.setZip(empAddress.getZip());
			address.setZp4(empAddress.getZp4());
			address.setProvince(empAddress.getProvince());
		} else {
			address.setCity("");
			address.setDepartment("");
			address.setQuarter("");
			address.setStprv2("");
			address.setStreet("");
			address.setUnit("");
			address.setZip("");
			address.setZp4("");
			address.setProvince("");
		}

		if (address == null)
			address = new PostalAddress();
		try {
			this.address = (PostalAddress)address.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		// default the address to the US if null
		if (address.getCountry() == null)
			address.setCountry(CountryType.US);
		
		// set the controls on the page
		this.title = title;
		clearControlStyles();
		setControls();
	}
	
	public void createAddress(GridPane grid, PostalAddressSnapshot addressSnap, String title, boolean markChanges) {
		try {
			init();
			
			this.markChanges = markChanges;
			mainGrid = grid;
			// create an address snap if it is null
			if (addressSnap == null)
				addressSnap = new PostalAddressSnapshot();
			
			// copy over the values
			address = new PostalAddress();
			address.setCity(addressSnap.getCity());
			address.setCountry(addressSnap.getCountry());
			address.setDepartment(addressSnap.getDepartment());
			address.setQuarter(addressSnap.getQuarter());
			address.setStprv2(addressSnap.getStprv2());
			address.setStreet(addressSnap.getStreet());
			address.setUnit(addressSnap.getUnit());
			address.setZip(addressSnap.getZip());
			address.setZp4(addressSnap.getZp4());
			
			// default the address to the US if null
			if (addressSnap.getCountry() == null)
				addressSnap.setCountry(CountryType.US);
			
			// set the controls on the page
			this.title = title;
			setControls();
		} catch (Exception e) {
			DataManager.i().log(Level.SEVERE, e);
		}
	}
	
	private void clearControlStyles()
	{
		cityField.setStyle("");
		streetField.setStyle("");
		unitField.setStyle("");
		zipField.setStyle("");
		zip4Field.setStyle("");
		qtrField.setStyle("");
		provinceField.setStyle("");
		deptField.setStyle("");
		stateCombo.getEditor().setStyle("");
	}
	
	public void setGrid(GridPane grid, String title) {
		init();
		
		mainGrid = grid;
		// create an address if it is null
		if (address == null)
			address = new PostalAddress();
		// default the address to the US
		if (address.getCountry() == null)
			address.setCountry(CountryType.US);
		
		// set the controls on the page
		this.title = title;
		setControls();
	}
	
	private void init() {
		stateCombo.setOnMouseClicked(e -> {
		     if(!editMode)
		    	 stateCombo.hide();
		});
		
		countryCombo.setOnMouseClicked(e -> {
		     if(!editMode)
		    	 countryCombo.hide();
		});
		
		// in order to allow custom entries, we are leaving the combo editor visible.
		// So we marshall our own read only, but allowing the user to tab through it
		stateCombo.getEditor().setOnKeyTyped(e -> {
		     if(!editMode)
		    	 e.consume();
		});
		
		stateCombo.getEditor().setOnKeyPressed(e -> {
		     if(!editMode && e.getCode() != KeyCode.TAB)
		    	 e.consume();
		});
		
		stateCombo.setOnHiding(e -> {
		     if(editMode == true && stateCombo.getValue() != null)
		    	 stateCombo.getEditor().setText(stateCombo.getValue().toString().substring(0,2));
		});
		
		countryCombo.setOnHiding(e -> {
			address.setCountry(countryCombo.getSelectionModel().getSelectedItem());
			setControls();
		});
	}
	
	private void setControls() {
		// create the columns
		createColumns();
		
		// add the title
		setTitle(title);
		// add the common elements
		setCountry();

		// add the common elements
		setStreet();
		setUnit();
		setCity();

		// now the country dependent elements
		switch (address.getCountry()) {
		case CA:
			setState(false);
			setZip();
			setZip4();
			break;
		case RU:
			setQuarter(false);
			setDepartment();
			setProvince();
			break;
		case MX:
			setQuarter(true);
			setDepartment();
			setProvince();
			break;
		case US:
		default:
			setState(true);
			setZip();
			setZip4();
			break;
		}
	}
	
	private void createColumns() {
		// clear any exisiting children
		mainGrid.getChildren().clear();
		// clear any existing columns
		mainGrid.getColumnConstraints().clear();
		// add back the columns we want
		addColumn(8);  // 0
		addColumn(17); // 1
		addColumn(8);  // 2
		addColumn(17); // 3
		addColumn(8);  // 4
		addColumn(17); // 5
		addColumn(8);  // 6
		addColumn(17); // 7
	}
	
	private void setTitle(String title) {
		titleLabel.setText(title);
		titleLabel.setMaxWidth(150);
		titleLabel.setAlignment(Pos.CENTER_LEFT);
		titleLabel.setFont(Font.font(null, FontWeight.NORMAL, 16));
		mainGrid.add(titleLabel, 0, 0, 6, 1);
	}
	
	private void setCountry() {
		setLabel(countryLabel,50);
		mainGrid.add(countryLabel, 6, 2, 1, 1);
		mainGrid.add(countryCombo, 7, 2, 1, 1);
		
		ObservableList<CountryType> countryTypes = FXCollections.observableArrayList(CountryType.values());
		countryCombo.setItems(countryTypes);

		if (address.getCountry() != null) 
			countryCombo.getSelectionModel().select(address.getCountry());
	}
	
	private void setStreet() {
		setLabel(streetLabel,50);
		mainGrid.add(streetLabel, 0, 1, 1, 1);
		mainGrid.add(streetField, 1, 1, 5, 1);
		if (markChanges == true)
			Utils.markAndUpdateControl(streetField,address.getStreet());
		else
			streetField.setText(address.getStreet());
	}
	
	private void setUnit() {
		setLabel(unitLabel,50);
		mainGrid.add(unitLabel, 6, 1, 1, 1);
		mainGrid.add(unitField, 7, 1, 2, 1);
		if (markChanges == true)
			Utils.markAndUpdateControl(unitField,address.getUnit());
		else
			unitField.setText(address.getUnit());
	}
	
	private void setCity() {
		setLabel(cityLabel,50);
		mainGrid.add(cityLabel, 0, 2, 1, 1);
		mainGrid.add(cityField, 1, 2, 3, 1);
		if (markChanges == true)
			Utils.markAndUpdateControl(cityField,address.getCity());
		else
			cityField.setText(address.getCity());
	}
	
	private void setState(boolean isState) {
		setLabel(stateLabel,50);
		mainGrid.add(stateLabel, 0, 3, 1, 1);
		mainGrid.add(stateCombo, 1, 3, 3, 1);
		// combo type
		if (isState == true) {
			// states
			stateLabel.setText("State");
			ObservableList<String> codes = FXCollections.observableList(Utils.StateCodes);
			stateCombo.setItems(codes);
		} else {
			// provinces
			stateLabel.setText("Prov.");
			ObservableList<String> codes = FXCollections.observableList(Utils.ProvinceCodes);
			stateCombo.setItems(codes);
		}
		//set the selection based on the 2 digit
		String st = address.getStprv2();
		if (st != null && st.isEmpty() == false)
		for (String state : stateCombo.getItems()) {
			if (state.startsWith(st)){
				stateCombo.getSelectionModel().select(state);
				break;
			}
		}
		stateCombo.setEditable(true);
	//	if (markChanges == true)
	//		Utils.markAndUpdateControl(stateCombo.getEditor(),address.getStprv2());
	//	else
			stateCombo.getEditor().setText(address.getStprv2());
	}
	
	private void setZip() {
		setLabel(zipLabel,50);
		mainGrid.add(zipLabel, 4, 3, 1, 1);
		mainGrid.add(zipField, 5, 3, 1, 1);
		if (markChanges == true)
			Utils.markAndUpdateControl(zipField,address.getZip());
		else
			zipField.setText(address.getZip());
	}
	
	private void setZip4() {
		setLabel(zip4Label,50);
		mainGrid.add(zip4Label, 6, 3, 1, 1);
		mainGrid.add(zip4Field, 7, 3, 1, 1);
		if (markChanges == true)
			Utils.markAndUpdateControl(zip4Field,address.getZp4());
		else
			zip4Field.setText(address.getZp4());
	}
	
	private void setQuarter(boolean isQtr) {
		setLabel(qtrLabel,50);
		mainGrid.add(qtrLabel, 0, 3, 1, 1);
		mainGrid.add(qtrField, 1, 3, 2, 1);
		if (isQtr)
			qtrLabel.setText("Qtr");
		else
			qtrLabel.setText("Dist.");
		if (markChanges == true)
			Utils.markAndUpdateControl(qtrField,address.getQuarter());
		else
			qtrField.setText(address.getQuarter());
	}
	
	private void setDepartment() {
		setLabel(deptLabel,50);
		mainGrid.add(deptLabel, 3, 3, 1, 1);
		mainGrid.add(deptField, 4, 3, 2, 1);
		if (markChanges == true)
			Utils.markAndUpdateControl(deptField,address.getDepartment());
		else
			deptField.setText(address.getDepartment());
	}
	
	private void setProvince() {
		setLabel(provinceLabel,50);
		mainGrid.add(provinceLabel, 6, 3, 1, 1);
		mainGrid.add(provinceField, 7, 3, 2, 1);
		if (markChanges == true)
			Utils.markAndUpdateControl(provinceField,address.getProvince());
		else
			provinceField.setText(address.getProvince());
	}
	
	private void setLabel(Label label, int size) {
		label.setMaxWidth(size);
		label.setAlignment(Pos.CENTER_RIGHT);
		label.setFont(Font.font(null, FontWeight.LIGHT, 12));
		label.setOpacity(0.5);
	}
	
	private void addColumn(double width) {
		ColumnConstraints cc = new ColumnConstraints();
		cc.setHgrow(Priority.ALWAYS);
		cc.setPercentWidth(width);
		mainGrid.getColumnConstraints().add(cc);
	}
	
	public void setEditMode(boolean isEdit)
	{
		editMode = isEdit;
		cityField.setEditable(isEdit);
		streetField.setEditable(isEdit);
		unitField.setEditable(isEdit);
		zipField.setEditable(isEdit);
		zip4Field.setEditable(isEdit);
		qtrField.setEditable(isEdit);
		provinceField.setEditable(isEdit);
		deptField.setEditable(isEdit);
	}
	
	public PostalAddress getUpdatedAddress() {
		//update the address from the screen
		updateAddress();
		// return the address
		return address;
	}
	
	public boolean validateData()
	{
		boolean bReturn = true;

		//check for required data
	/*	if (address.getCountry() == CountryType.US) {
			if ( !Utils.validate(zipField)) bReturn = false;
			if ( !Utils.validate(streetField)) bReturn = false;
		}
		if ( !Utils.validate(cityField)) bReturn = false;
	*/
		return bReturn;
	}
	
	private void updateAddress() {
		address.setCity(cityField.getText());
		address.setStreet(streetField.getText());
		address.setUnit(unitField.getText());
		address.setZip(zipField.getText());
		address.setZp4(zip4Field.getText());
		address.setQuarter(qtrField.getText());
		address.setProvince(provinceField.getText());
		address.setDepartment(deptField.getText());
		address.setStprv2(stateCombo.getEditor().getText());
	}
	
	public boolean isValid() {
		if (address.getCity() == null || address.getCity() == "" ) return false;
		if (address.getStreet() == null || address.getStreet() == "" ) return false;
		if (address.getZip() == null || address.getZip() == "" ) return false;
		if (address.getCountry() == null) return false;
	
		return true;
	}
}
