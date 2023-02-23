package com.etc.admin.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.etc.CoreException;
import com.etc.admin.AdminManager;
import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.api.Jsonable.ToJsonType;
import com.etc.api.TransferRequest;
import com.etc.embeds.SSN;
import com.etc.utils.crypto.Cryptographer;
import com.etc.utils.crypto.CryptographyException;
import com.etc.utils.crypto.EncryptedSSN;
import com.etc.utils.types.EmailType;
import com.etc.utils.types.PhoneType;
import com.etc.utils.types.SecurityLevel;
import com.etc.utils.types.TimezoneType;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Utils {
	
	/////////////////////////////////////////////
	// ui asthetics
	/////////////////////////////////////////////
	public static String uiColorListGridBackground = "#ddeeff";
	public static String uiColorListGridLabel = "#000000";
	public static String uiColorFieldLabels = "#777777";
	public static String uiColorVBox = "#ffffff";
	
	public static Long lastTimeStamp = 0l;
	
	//sdf for calendar use
	static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	static SimpleDateFormat sdfTime = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	static SimpleDateFormat sdfTime24 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	static SimpleDateFormat sdfTimeOnly24 = new SimpleDateFormat("HH:mm");
	
	//user access levels
	public static boolean userCanEdit() {
		return DataManager.i().mLocalUser.getSecurityLevel().ordinal() >= SecurityLevel.LEVEL2.ordinal();		
	}

	public static boolean userCanAdd() {
		return DataManager.i().mLocalUser.getSecurityLevel().ordinal() >= SecurityLevel.LEVEL3.ordinal();		
	}

	public static boolean userCanAddUsers() {
		return DataManager.i().mLocalUser.getSecurityLevel().ordinal() >= SecurityLevel.ADMIN0.ordinal();		
	}

	public static boolean userCanAddOrgs() {
		return DataManager.i().mLocalUser.getSecurityLevel().ordinal() >= SecurityLevel.SUPER0.ordinal();		
	}

	public static void showAlert(String title, String message) {
      	Alert alert = new Alert(AlertType.NONE, message, ButtonType.OK);
      	alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
      	alert.setTitle(title);
	    EtcAdmin.i().positionAlertCenter(alert);
      	alert.showAndWait();
	}
	
	public static Calendar getCalDate(LocalDate localDate) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(getDate(localDate));
		// adjust the time if needed. 0 at midnight is trashed by timezones
		if (calDate.get(Calendar.HOUR_OF_DAY) == 0 && calDate.get(Calendar.MINUTE) == 0 ) {
			calDate.set(Calendar.HOUR_OF_DAY, 12);
		}
		return calDate;
	}
	
	public static Calendar getCalDate(DatePicker picker) {
		if (picker == null || picker.getValue() == null) return null;
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(getDate(picker.getValue()));
		calDate.set(Calendar.HOUR_OF_DAY, 12);
		return calDate;
	}
	
	public static Calendar getCalDate(Long timeInMilliseconds) {
		Calendar calDate = Calendar.getInstance();
		calDate.setTimeInMillis(timeInMilliseconds);
		return calDate;
	}
	
	public static Calendar getCalDate(String dateString) {
		Calendar cal = Calendar.getInstance();
		try {
			if (dateString.isEmpty()) return null;
			SimpleDateFormat sdf;
			if (dateString.length() > 17) {
				sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.ENGLISH);
				cal.setTime(sdf.parse(dateString));
			}
			else if (dateString.length() > 11) {
				sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.ENGLISH);
				cal.setTime(sdf.parse(dateString));
			}
			else {
				sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
				cal.setTime(sdf.parse(dateString));
				cal.set(Calendar.HOUR_OF_DAY, 12);
			}
		
		} catch (ParseException e) {
        	DataManager.i().log(Level.INFO, e); 
		}
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		return cal;
	}
	
	public static Calendar checkDate(Calendar date) {
		if (date == null) return null;
		if (date.get(Calendar.HOUR_OF_DAY) == 0 && date.get(Calendar.MINUTE) == 0 ) {
			date.set(Calendar.HOUR_OF_DAY, 12);
		}
		return date;
	}
	
	public static long daysBetween(Calendar startDate, Calendar endDate) {
	    long end = endDate.getTimeInMillis();
	    long start = startDate.getTimeInMillis();
	    return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
	}	
	
	public static int compareDateStrings(String d1, String d2) {
		if (d1 == null || d2 == null || d1.isEmpty() || d2.isEmpty()) 
			return 0;
		return Utils.getCalDate(d1).compareTo(Utils.getCalDate(d2));
	}
	
	public static boolean compareDates(Calendar date1, Calendar date2, int totalMilliseconds) {
		if (date1 == null || date2 == null)
			return false;
		
		if (Math.abs(date1.getTimeInMillis() - date2.getTimeInMillis()) > totalMilliseconds)
			return false;
		
		return true;
	}
	
	public static int compareNumberStrings(String d1, String d2) {
		if (d1 == null || d2 == null || d1.isEmpty() || d2.isEmpty()) 
			return 0;
		Long n1 = Long.valueOf(d1);
		Long n2 = Long.valueOf(d2);
		return n1.compareTo(n2);
	}
	
	public static boolean areStringsDifferent(String d1, String d2) {
		// check null combinations
		if (d1 == null && d2 == null) return false; 
		if (d1 == null && d2 != null) return true; 
		if (d1 != null && d2 == null) return true; 
		
		if  (d1.equals(d2) == false) return true;

		return false;
	}
	
	public static boolean areDatesDifferent(Calendar d1, Calendar d2) {
		// check null combinations
		if (d1 == null && d2 == null) return false; 
		if (d1 == null && d2 != null) return true; 
		if (d1 != null && d2 == null) return true; 
		
		if  (d1.getTimeInMillis() != d2.getTimeInMillis()) return true;

		return false;
	}
	
	public static boolean areLongsDifferent(Long l1, Long l2) {
		// check null combinations
		if (l1 == null && l2 == null) return false; 
		if (l1 == null && l2 != null) return true; 
		if (l1 != null && l2 == null) return true; 
		
		if  (l1.equals(l2) == false) return true;

		return false;
	}
	
	public static String getDateString(Calendar calDate) {
		if (calDate == null || calDate.getTimeInMillis() == 0) return "";
		return sdf.format(calDate.getTime());
	}
	
	public static String getDateTimeString(Calendar calDate) {
		if (calDate == null) return "";
		return sdfTime.format(calDate.getTime());
	}
	
	public static String getDateTimeString24(Calendar calDate) {
		if (calDate == null) return "";
		return sdfTime24.format(calDate.getTime());
	}
	
	public static String getTimeString24(Calendar calDate) {
		if (calDate == null) return "";
		return sdfTimeOnly24.format(calDate.getTime());
	}
	
	public static String getMoneyString(Float amount) {
		if (amount == null) return "";
		return String.format(java.util.Locale.US,"$%.2f", amount);
	}
	
	public static void printTimeStamp(String message, boolean showElapsed) {
		Long tick = Calendar.getInstance().getTimeInMillis();
		if (showElapsed)
			System.out.println(message + ": " + tick.toString() + " Elapsed: " + String.valueOf(tick - lastTimeStamp));
		else
			System.out.println(message + ": " + tick.toString());
		lastTimeStamp = tick;
	}
	
	public static int getElapsedMinutes(Calendar time) {
		if (time == null) return 0;
		double tick = Calendar.getInstance().getTimeInMillis();
		double ticker = time.getTimeInMillis();
		Double elapsed = (tick - ticker)/60000.0;
		if (elapsed < 0.0) elapsed = 0.0;
		return elapsed.intValue(); 
	}
	
	//helper function to handle nulls in account data
	public static void updateControl (Label destination, String lblData) {
		
		if (destination  != null) {
			if (lblData != null) {
				if (lblData.equals("null")) {
					destination.setText("");
					return;
				}
				destination.setText(lblData);
			} else {
				destination.setText("");			
			}
		}
	}	
	
	//helper function to handle nulls in account data
	public static void updateControl (Label destination, int nData) {
		if (destination  != null) {
				destination.setText(String.valueOf(nData));
		}
	}	
	
	//helper function to handle nulls in account data
	public static void updateControl (Label destination, long data) {
		if (destination  != null) {
				destination.setText(String.valueOf(data));
		}
	}	
	
	//helper function to handle nulls in account data
	public static void updateControl (Label destination, boolean data) {
		if (destination  != null) {
				destination.setText(String.valueOf(data));
		}
	}	

	public static void updateControl(Label destination, Calendar date) {
		if (date != null) {
			if (date.getTimeInMillis() != 0) {
				updateControl(destination,sdf.format(date.getTime()));
				return;
			}
		}
		destination.setText("");
	}
	
	public static void updateControl (TextField destination, int nData) {
		if (destination  != null) {
				destination.setText(String.valueOf(nData));
		}
	}	

	public static void updateControl (TextField destination, Long lData) {
		if (destination  != null && lData != null) {
				destination.setText(String.valueOf(lData));
		}
	}	

	public static void markAndUpdateControl (TextField destination, long lData) {
		if (destination  != null) {
			// mark it changed here
			if (destination.getText().equals(String.valueOf(lData)) == false)
				destination.setStyle("-fx-background-color: lightcyan");
			else
				destination.setStyle("");

			destination.setText(String.valueOf(lData));
		}
	}	

	public static void updateControl (TextField destination, float fData) {
		if (destination  != null) {
				destination.setText(String.valueOf(fData));
		}
	}	

	public static void updateControl (TextField destination, String lblData) {
		if (destination  != null) {
			if (lblData != null) {
				if (lblData.equals("null")) {
					destination.setText("");
					return;
				}
				destination.setText(lblData);
			} else {
				destination.setText("");			
			}
		}
	}	

	// markAdnbUpdateControl marks the control if the new data is different
	public static void markAndUpdateControl (TextField destination, String lblData) {
		if (lblData == null) {
			if (destination.getText() != null && destination.getText().isEmpty() == false)
				destination.setStyle("-fx-background-color: lightcyan");
			else
				destination.setStyle("");
			destination.setText("");
			return;
		}
		
		if (destination  != null && lblData != null) {
			// mark it changed here
			if (destination.getText().equals(lblData) == false)
				destination.setStyle("-fx-background-color: lightcyan");
			else
				destination.setStyle("");

			if (lblData.equals("null")) {
				destination.setText("");
				return;
			}
			destination.setText(lblData);
		}
	}	

	public static void updateControl (TextArea destination, String lblData) {
		if (destination  != null) {
			if (lblData != null) {
				if (lblData.equals("null")) {
					destination.setText("");
					return;
				}
				destination.setText(lblData);
			} else {
				destination.setText("");			
			}
		}
	}	

	public static void updateControl(TextField destination, Calendar date) {
		if (date != null) {
			if (date.getTimeInMillis() != 0) {
				updateControl(destination,sdf.format(date.getTime()));
				return;
			}
		}
		destination.setText("");
	}

	public static void updateControl (CheckBox destination, boolean bData) {
		if (destination  != null) {
			destination.setSelected(bData);
	    	//keep it from fading out
			destination.setStyle("-fx-opacity: 1");
		}
	}	
	
	public static void updateControl (CheckBox destination, boolean bData, boolean bKeepSolid) {
		if (destination  != null) {
			destination.setSelected(bData);
	    	//keep it from fading out
			if (bKeepSolid == true)
				destination.setStyle("-fx-opacity: 1");
		}
	}	
	
	public static void updateControl(TextField destination,String colorTrue, String colorFalse, boolean condition) {
		if (destination  != null) {
			if (condition == true)
				destination.setStyle("-fx-background-color:" +  colorTrue);
			else
				destination.setStyle("-fx-background-color:" +  colorFalse);
		}
	}

	//helper function to handle blank dates
	public static void updateControl(DatePicker destination, Calendar date) {

		if (date != null) {
			if (date.getTimeInMillis() != 0) {
				destination.setValue(getLocalDate(date.getTime()));
				return;
			}
		} else
			destination.setValue(null);
	}
	
	public static void markAndUpdateControl(DatePicker destination, Calendar date) {
		if (destination == null) return;
		
		if (date != null) {
			LocalDate dpDate = destination.getValue();
			// Note: the month is zero based, the others are not. 
			if (dpDate == null || dpDate.getDayOfMonth() != date.get(Calendar.DAY_OF_MONTH) ||
								  dpDate.getMonthValue() != date.get(Calendar.MONTH) + 1 ||
								  dpDate.getYear() != date.get(Calendar.YEAR))
				destination.getEditor().setStyle("-fx-background-color: lightcyan");
			else
				destination.getEditor().setStyle("");
					
			if (date.getTimeInMillis() != 0) {
				destination.setValue(getLocalDate(date.getTime()));
				return;
			}
		} else {
			// incoming date is null, so check to see if is't different from the current value 
			if (destination.getValue() != null)
				destination.getEditor().setStyle("-fx-background-color: lightcyan");
			else
				destination.getEditor().setStyle("");
			destination.setValue(null);
		}
	}
	
	//helper function for combo boxes
	public static void updateControl(ComboBox<String> destination, String data) {
		destination.getEditor().setText(data);
	}
	
	public static void setActiveHeader (Label label, boolean active) {
		if (active == false) {
			label.setText("          INACTIVE          ");
			label.setTextFill(Color.WHITE);
			label.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		}else {
			label.setText("Hand Icon denotes clickable list items");
			label.setTextFill(Color.GRAY);				
			label.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		}
	}
	
	// utils to help with HBox
    public static void setHBoxLabel(Label lbl, int size, boolean header, String text) {
    	lbl.setMinWidth(size);
    	lbl.setMaxWidth(size);
    	lbl.setPrefWidth(size);
    	if (text == "null")
    		lbl.setText("");
    	else
    		lbl.setText(text);
    	if (header == true)
    		lbl.setTextFill(Color.GREY);
    }

    public static void setHBoxLabel(Label lbl, int size, boolean header, Calendar cal) {
    	lbl.setMinWidth(size);
    	lbl.setMaxWidth(size);
    	lbl.setPrefWidth(size);
    	lbl.setText(getDateString(cal));
    	if (header == true)
    		lbl.setTextFill(Color.GREY);
    }

    public static void setHBoxLabel(Label lbl, int size, boolean header, Long val) {
    	lbl.setMinWidth(size);
    	lbl.setMaxWidth(size);
    	lbl.setPrefWidth(size);
    	lbl.setText(String.valueOf(val));
    	if (header == true)
    		lbl.setTextFill(Color.GREY);
    }

    public static void setHBoxLabel(Label lbl, int size, boolean header, float val) {
    	lbl.setMinWidth(size);
    	lbl.setMaxWidth(size);
    	lbl.setPrefWidth(size);
    	lbl.setText(String.valueOf(val));
    	if (header == true)
    		lbl.setTextFill(Color.GREY);
    }

    public static void setHBoxLabel(Label lbl, int size, boolean header, int val) {
    	lbl.setMinWidth(size);
    	lbl.setMaxWidth(size);
    	lbl.setPrefWidth(size);
    	lbl.setText(String.valueOf(val));
    	if (header == true)
    		lbl.setTextFill(Color.GREY);
    }

    public static void setHBoxCheckBox(CheckBox checkBox, int size, boolean value) {
    	checkBox.setMinWidth(size);
    	checkBox.setMaxWidth(size);
    	checkBox.setPrefWidth(size);
    	checkBox.setSelected(value);
    	checkBox.setText("");
    	checkBox.setDisable(true);
    }

	public static void setControlHeight(VBox vBox, int nHeight) {
		vBox.setMinHeight(nHeight);
		vBox.setMaxHeight(nHeight);
		vBox.setPrefHeight(nHeight);
	}

	public static void setControlHeight(Separator seperator, int nHeight) {
		seperator.setMinHeight(nHeight);
		seperator.setMaxHeight(nHeight);
		seperator.setPrefHeight(nHeight);
	}

	public static LocalDate getLocalDate(Date date){
		   return LocalDate.from(Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()));
	}	
	
	public static Date getDate(LocalDate localDate) {
		if (localDate == null) return null;
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return date;
	}
	
	
	//validation
	public static boolean isInt(String sText) {
		if (sText == null) return true;
		
		if (sText.matches("[-+]?[0-9]*[0-9]+"))
			return true;
	
		return false;
	}
	
	public static boolean isFloat(String sText) {
		if (sText == null) return true;

		if (sText.matches("[-+]?[0-9]*\\.?[0-9]+"))
			return true;
	
		return false;
	}
	
	public static int getInteger(String sText) {
		if (sText == null) return 0;
		
		if (sText.matches("[-+]?[0-9]*[0-9]+") == false)
			return 0;
	
		return Integer.valueOf(sText);
	}
	
	public static Long getLong(String sText) {
		if (sText == null) return 0L;
		
		if (sText.matches("[-+]?[0-9]*[0-9]+") == false)
			return 0L;
	
		return Long.valueOf(sText);
	}
	
	public static float getFloat(String sText) {
		if (sText == null) return 0f;

		if (sText.matches("[-+]?[0-9]*\\.?[0-9]+") == false)
			return 0f;
	
		return Float.valueOf(sText);
	}
	
	public static boolean getBoolean(String sText) {
		if (sText == null) return false;
	
		return Boolean.valueOf(sText);
	}
	
	public static boolean isSSN(String sText) {
		if (sText == null) return false;
	
		if (sText.matches("^\\d{3}-?\\d{2}-?\\d{4}$") == false)
			return false;
		
		return true;
	}
	
	public static Long getLong(TextField field) {
		if (field == null || field.getText() == null) return 0L;
		String sText = field.getText();
		
		if (sText.matches("[-+]?[0-9]*[0-9]+") == false)
			return 0L;
	
		return Long.valueOf(sText);
	}
	
	public static Integer getInteger(TextField field) {
		if (field == null || field.getText() == null) return 0;
		String sText = field.getText();
		
		if (sText.matches("[-+]?[0-9]*[0-9]+") == false)
			return 0;
	
		return Integer.valueOf(sText);
	}
	
	public static Float getFloat(TextField field) {
		if (field == null || field.getText() == null) return 0f;
		String sText = field.getText();
		
		if (sText.matches("[-+]?[0-9]*\\.?[0-9]+") == false)
			return 0f;
	
		return Float.valueOf(sText);
	}
	
	// validateTextFile ensures that a value is present
	public static boolean validate(TextField sField) {
		if (sField == null) return true;
		
		if (sField.getText().equals("") ) {
			//System.out.println("Validation Fails in TextField. Empty Value.");
			//mark it as an error
			sField.setStyle("-fx-background-color: red");
			return false;
		}

		//reset the background
		sField.setStyle(null);
		return true;
	}

	// validateLength ensures that a text value in a text field is a minimum length
	public static boolean validateLength(TextField sField, int minLength) {
		boolean error = false;
		
		if (sField == null) 
			error = true;
		else 
			if (sField.getText().length() < minLength) error = true;
		
		if (error == true) {
			//mark it as an error and return
			sField.setStyle("-fx-background-color: red");
			return false;
		}

		//reset the background
		sField.setStyle(null);
		return true;
	}
	
	// validateExactLength ensures that a text value in a text field is a given dlength
	public static boolean validateTextFieldExactLength(TextField sField, int length) {
		boolean error = false;

		if (sField != null && sField.getText().isEmpty() == false)
			if (sField.getText().length() != length) 
				error = true;
		
		if (error == true) {
			//mark it as an error and return
			sField.setStyle("-fx-background-color: red");
			return false;
		}

		//reset the background
		sField.setStyle(null);
		return true;
	}
	
	// validate ensures that a value is present
	public static boolean validate(DatePicker picker) {
		if (picker == null) return true;
		
		if (picker.getValue() == null) {
			//System.out.println("Validation Fails in DatePicker. Empty Value.");
			//mark it as an error
			picker.setStyle("-fx-background-color: red");
			return false;
		}

		return true;
	}

	// validate ensures that a value is present
	public static boolean validate(TextArea txtArea) {
		if (txtArea == null) return true;
		
		if (txtArea.getText() == "" || txtArea.getText().isEmpty()) {
			//System.out.println("Validation Fails in TextArea. Empty Value.");
			//mark it as an error
			txtArea.setStyle("-fx-background-color: red");
			return false;
		}

		//reset the background
		txtArea.setStyle(null);
		return true;
	}
	
	// validate ensures that a value is present
	public static boolean validatePhoneType(ComboBox<PhoneType> comboBox) {
		if (comboBox == null) return true;
		
		if (comboBox.getValue() == null) {
			//System.out.println("Validation Fails in DatePicker. Empty Value.");
			//mark it as an error
			comboBox.setStyle("-fx-background-color: red");
			return false;
		}

		return true;
	}
	
	// validate ensures that a value is present
	public static boolean validateEmailType(ComboBox<EmailType> comboBox) {
		if (comboBox == null) return true;
		
		if (comboBox.getValue() == null) {
			//System.out.println("Validation Fails in DatePicker. Empty Value.");
			//mark it as an error
			comboBox.setStyle("-fx-background-color: red");
			return false;
		}

		return true;
	}
	
	// validate ensures that a value is present
	public static boolean validateTimezoneType(ComboBox<TimezoneType> comboBox) {
		if (comboBox == null) return true;
		
		if (comboBox.getValue() == null) {
			//System.out.println("Validation Fails in DatePicker. Empty Value.");
			//mark it as an error
			comboBox.setStyle("-fx-background-color: red");
			return false;
		}

		return true;
	}

	public static boolean validateIntTextField(TextField sField) {
		if (sField == null) return true;
		
		if (sField.getText().equals("") ) {
			sField.setStyle(null);
			return true;
		}

		if (sField.getText().matches("[-+]?[0-9]*[0-9]+")) {
			sField.setStyle(null);
			return true;
			}
	
		//System.out.println("Validation Fails in Int TextField. Value(" + sField.getText() + ")");

		//mark it as an error
		sField.setStyle("-fx-background-color: red");

		return false;
	}
	
	public static boolean isNumber(TextField sField) {
		if (sField.getText().matches("[0-9]+")) {
			return true;
			}
		return false;
	}
	
	public static boolean validatePasswordTextField(TextField sField) {
		if (sField != null) {		
			if (sField.getText().equals("") == false ) {
				if (sField.getText().length() > 5) {
					sField.setStyle(null);
					return true;
				}
			}
		}

		//System.out.println("Validation Fails in Password TextField. Value(" + sField.getText() + ")");

		//mark it as an error
		sField.setStyle("-fx-background-color: red");

		return false;
	}

	public static boolean validateIntRangeTextField(TextField sField, int nMin, int nMax) {
		if (sField == null) return true;
		
		if (sField.getText().equals("") ) return true;

		//make sure it is an int
		if (sField.getText().matches("[-+]?[0-9]*[0-9]+")) {
			int nVal = Integer.valueOf(sField.getText());
			if (nVal >= nMin && nVal <= nMax) {
				sField.setStyle(null);
				return true;
			}
		}
		
		//mark it as an error
		sField.setStyle("-fx-background-color: red");
		return false;
	}
	
	public static boolean validateFloatTextField(TextField sField) {
		if (sField == null) return true;
		
		if (sField.getText().equals("") ) {
			sField.setStyle(null);
			return true;
		}

		if (sField.getText().matches("[-+]?[0-9]*\\.?[0-9]+")) {
			sField.setStyle(null);
			return true;
		}
	
		//mark it as an error
		sField.setStyle("-fx-background-color: red");

		return false;
	}
	
	public static boolean validatePhoneTextField(TextField sField) {
		if (sField == null) {
			return true;
		}
		
		if (sField.getText().equals("") ) {
			sField.setStyle(null);
			return true;
		}

		if (sField.getText().matches("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$")) {
			sField.setStyle(null);
			return true;
		}
	
		//System.out.println("Validation Fails in Phone TextField. Value(" + sField.getText() + ")");

		//mark it as an error
		sField.setStyle("-fx-background-color: red");

		return false;
	}
	
	public static boolean validateEmailTextField(TextField sField) {
		if (sField == null) return true;
		
		if (sField.getText().equals("") ) {
			sField.setStyle(null);
			return true;
		}

		if (sField.getText().contains("@")) {
			sField.setStyle(null);
			return true;
		}
		//mark it as an error
		sField.setStyle("-fx-background-color: red");

		return false;
	}
	
	public static boolean validateSSNTextField(TextField sField) {
		if (sField == null) return true;
		
		if (sField.getText().equals("") ) {
			sField.setStyle(null);
			return true;
		}

		if (isSSN(sField.getText()) == true) 
			return true;
		
		//mark it as an error
		sField.setStyle("-fx-background-color: red");

		return false;
	}
	
	// validateComboBox ensures that a value is present
	public static boolean validate(ComboBox<String> combo) {
		if (combo == null) return true;
		if (combo.getValue() == null)  return true;
		
		if (combo.getValue().equals("") ) {
			//System.out.println("Validation Fails in TextField. Empty Value.");
			//mark it as an error
			combo.setStyle("-fx-background-color: red");
			return false;
		}

		//reset the background
		combo.setStyle(null);
		return true;
	}

	//String handler for database statement, throws SQLException
	public static void updateStatement(PreparedStatement statement, Integer index, String sVal) throws SQLException {
		if (sVal == null) {
			statement.setNull(index, Types.VARCHAR);
			return;
		}
		
		statement.setString(index, sVal);
	}
	
	//Integer handler for database statement, throws SQLException
	public static void updateStatement(PreparedStatement statement, Integer index, Integer iVal) throws SQLException {
		if (iVal == null) {
			statement.setNull(index, Types.INTEGER);
			return;
		}
		
		statement.setInt(index, iVal);
	}
	
	//Integer handler for database statement, throws SQLException
	public static void updateStatement(PreparedStatement statement, Integer index, Boolean bVal) throws SQLException {
		if (bVal == null) {
			statement.setNull(index, Types.BOOLEAN);
			return;
		}
		
		statement.setBoolean(index, bVal);
	}
	
	//Long handler for database statement, throws SQLException
	public static void updateStatement(PreparedStatement statement, Integer index, Long lVal) throws SQLException {
		if (lVal == null) {
			statement.setNull(index, Types.BIGINT);
			return;
		}
		
		statement.setLong(index, lVal);
	}
	
	//Date handler for database statement, throws SQLException
	public static void updateStatement(PreparedStatement statement, Integer index, Calendar calDate) throws SQLException {
		if (calDate == null) {
			statement.setNull(index, Types.BIGINT);
			return;
		}
		
		statement.setLong(index, calDate.getTimeInMillis());
	}
	
	//ToJsonType handler for database statement, throws SQLException
	public static void updateStatement(PreparedStatement statement, Integer index, ToJsonType toJsonType) throws SQLException {
		if (toJsonType == null) {
			statement.setNull(index, Types.VARCHAR);
			return;
		}
		
		statement.setString(index, toJsonType.toString());
	}
	
	//creates an alert to the user and waits until user responds
	public static void alertUser(String header, String message) {
        // Run on the GUI thread
        Platform.runLater(() -> {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Core Admin App");
    		alert.setHeaderText(header);
    		alert.setContentText(message);
    	    EtcAdmin.i().positionAlertCenter(alert);
    	    alert.showAndWait();
        });
  	}
	
	public static void setTextFieldInputDecimal(TextField tf)
	{
		DecimalFormat decFormat = new DecimalFormat( "#.0" );
		tf.setTextFormatter( new TextFormatter<>(c ->
		{
		    if ( c.getControlNewText().isEmpty() )
		    {
		        return c;
		    }

		    ParsePosition parsePosition = new ParsePosition( 0 );
		    Object object = decFormat.parse( c.getControlNewText(), parsePosition );

		    if ( (object == null || parsePosition.getIndex() < c.getControlNewText().length()) && c.getControlNewText().contains("\n") == false)
		    {
		        return null;
		    }
		    else
		    {
		        return c;
		    }
		}));
	}
	
/*	public static String encryptString(String input) {
		EncryptDecrypt td;
		String output = null;

		try {
			td = new EncryptDecrypt();
			output = td.encrypt(input);
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
		
		return output;
	}
*/	
	
	public static String localEncryptString(String input) {
		EncryptDecrypt td;
		String output = null;

		try {
			td = new EncryptDecrypt();
			output = td.encrypt(input);
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
		
		return output;
	}
	
	public static String localDecryptString(String input) {
		EncryptDecrypt td;
		String output = null;

		try {
			td = new EncryptDecrypt();
			output = td.decrypt(input);
		} catch (Exception e) {
        	DataManager.i().log(Level.SEVERE, e); 
		}
		
		return output;
	}	
	
	public static void addTextLimiter(final TextField tf, final int maxLength) {
	    tf.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (tf.getText().length() > maxLength) {
	                String s = tf.getText().substring(0, maxLength);
	                tf.setText(s);
	            }
	        }
	    });
	}	
	
	public static void savePDFScreenshot(Stage stage, String pdfPath, boolean show) {
		try {
			// if the path is null, ask the user
			if (pdfPath == null) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save PDF");
			    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
			    File file = fileChooser.showSaveDialog(stage);
				pdfPath = file.getPath();
			}
			
			// Create a new empty document
			PDDocument document = new PDDocument();
			// Create a new blank page and add it to the document
			PDPage page = new PDPage();
			document.addPage( page );
			// create a content strem to write ot our pdf
	        final PDPageContentStream contentStream = new PDPageContentStream(document, page);
	        // create the image from the scene (if not null) or the stage
	        WritableImage wImage;
	        	wImage = stage.getScene().snapshot(null);
			// write it to a memory file
			ByteArrayOutputStream output = new ByteArrayOutputStream();
	        ImageIO.write(SwingFXUtils.fromFXImage(wImage, null), "png", output);
	        output.close();
			// create the PDImage
	        PDImageXObject pdimage = PDImageXObject.createFromByteArray(document, output.toByteArray(), "png"); 
	        // fit image to media box of page
            PDRectangle box = page.getMediaBox();
            double factor = Math.min(box.getWidth() / wImage.getWidth(), box.getHeight() / wImage.getHeight());
            float height = (float) (wImage.getHeight() * factor);      
            // draw the pdimage
            contentStream.drawImage(pdimage, 0, box.getHeight() - height, (float) (wImage.getWidth() * factor), height);
			// close everything
			contentStream.close();
			// Save the newly created document
			document.save(pdfPath);
			// close it all 
			document.close();
			// show it in the local system
			if (show == true)
				EtcAdmin.i().getHostServices().showDocument(pdfPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/////////////////////////////////////////////
	// Encryption Support
	/////////////////////////////////////////////
	
	// encrypts an SSN using the server's public key 
	public static EncryptedSSN encryptSSN(String ssn) { 
		try {  
				PublicKey pubk = AdminManager.getCoreKey(); 
				EncryptedSSN eSSN = Cryptographer.encryptSSN(pubk, ssn); 
				return eSSN; 
			}catch(CoreException e){ 
	        	DataManager.i().log(Level.SEVERE, e); 
			} 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
		 
		return null; 
	} 

	public static String decryptSSN(SSN ssn) { 
		try {  
			KeyPair kp = AdminManager.getKeyPair(); 
			String sSSN = Cryptographer.decryptSSN(kp.getPrivate(), ssn.toEncryptedSSN()); 
			return sSSN; 
		}catch(CryptographyException e){ 
	        DataManager.i().log(Level.SEVERE, e); 
		} 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }

		return null; 
	} 
	 
	// encrypts a String using the server's public key 
	public static String encryptString(String unencryptedString) { 
		try { 
				PublicKey pubk = DataManager.i().getCorvettoManager().getServerPublicKey(); 
				String encryptedString = Cryptographer.encryptByteArrayToHexString(pubk, unencryptedString.getBytes("UTF-8")); 
				return encryptedString; 
			}catch(CoreException | UnsupportedEncodingException e){ 
	        	DataManager.i().log(Level.SEVERE, e); 
			} 
	    catch (Exception e) {  DataManager.i().logGenericException(e); }
	 
		return null; 
	} 
	
	public static String generatePassword(int length)
	{
		// must be at least 8 characters
		if (length < 8) length = 8;
		
		String newPassword = "";
		SecureRandom r = null;
		try {
			char[] password = new char[length];
			// using feed strings in case we want to specify later
			String specialChars = "@#$%^&*!()";
			String numbers = "0123456789";
			String uppercaseletters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			String lowercaseletters = "abcdefghijklmnopqrstuvwxyz";
			r = SecureRandom.getInstance("SHA1PRNG");
	
			// step 1 - generate the base string from lower case letters
			for(int i = 0;i < length;i++)
				password[i] = lowercaseletters.charAt(r.nextInt(lowercaseletters.length()));
	
			// step 2 - generate 4 upper case letters at random locations
			for(int i = 0;i < 4;i++)
				password[r.nextInt(length)] = uppercaseletters.charAt(r.nextInt(uppercaseletters.length()));
			
			// step 2 - generate 3 random numbers at random locations
			for(int i = 0;i < 3;i++)
				password[r.nextInt(length)] = numbers.charAt(r.nextInt(numbers.length()));
			
			// step 3 - generate 3 random special character at a random location
			for(int i = 0;i < 3;i++)
				password[r.nextInt(length)] = specialChars.charAt(r.nextInt(specialChars.length()));
			
			// copy to string
			newPassword = String.valueOf(password);
		
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}finally
		{
			r = null;
		}

		return newPassword;
	}
	
	public static boolean verifyPassword(String password) {
		// verify password
		// must have upper and lower case,
		// at least 1 number, at least 1 special character,
		// length of at least 8,
		// and no white space
		String regex = "^(?=.*[0-9])"
	               + "(?=.*[a-z])(?=.*[A-Z])"
	               + "(?=.*[!@#$%^&+=])"
	               + "(?=\\S+$).{8,20}$";
		Pattern p = Pattern.compile(regex);
		
		return p.matcher(password).matches();
	}
	
	public static String browseDirectory(Stage stage, String prompt)
	{
		DirectoryChooser chooser = new DirectoryChooser();
		File directory = chooser.showDialog(stage);
		
		if (directory != null)
			return directory.getAbsoluteFile().getPath();
		
		return "";
	}

	public static boolean downloadFile(Long docId, String destination)
	{
		try
		{
			showAppStatus("Downloading File", "Downloading file to " + destination, .5);

			// downlad the file using the API
			File file = null;
			TransferRequest tr = DataManager.i().getCorvettoManager().downloadFile(docId);
			file = tr.getFile();
			
			//copy it to our destination
			String path = destination + "\\" + file.getName();
			Files.copy(file.toPath(),
				        (new File(path)).toPath(),
				        StandardCopyOption.REPLACE_EXISTING);		
			EtcAdmin.i().showAppStatus("", "", 0, false);
		}catch(Exception e)
		{
			EtcAdmin.i().showAppStatus("", "", 0, false);
			DataManager.i().log(Level.SEVERE, e); 
			DataManager.i().insertLogEntry("Download failed for docID " + docId, LogType.CRITICAL);
			Utils.alertUser("Download Problem", "The selected file (DocId " + docId + ") could not be downloded. Check the logs for more information.");
			return false;
		}
	
		return true;
	}
	
	public static void showAppStatus(String header, String message, double status)
	{
	    Task<Void> task = new Task<Void>() {
		    @Override protected Void call() throws Exception {
				try {
					EtcAdmin.i().showAppStatus(header,message, status, true);
				}
			    catch (Exception e) {
			    	EtcAdmin.i().showAppStatus("","", 0, false);
			    	DataManager.i().logGenericException(e); 
			    	}
				return null;
    		}
	    };
		Thread thread = new Thread(task, "showAppStatus");
		thread.setDaemon(true);
		thread.start();		
		
	}
	
	public enum LogType {
		INFO,
		DEBUG,
		ERROR,
		WARNING,
		CRITICAL
	}

	// Data Property Types
	public static enum DataPropertyTypes{
		ACCOUNT,
		EMPLOYEE,
		EMPLOYER,
		PAY,
		COVERAGE
	}
	
	// Status Message Type
	public static enum LastUpdatedType{
		ACCOUNT,
		ACCOUNTCONTACT,
		ACCOUNTBENEFIT,
		AIRFILINGEVENT,
		AIRTRANERRFILE,
		AIRTRANRCPTFILE,
		AIREVENT,
		CALCULATIONNOTICE,
		CALCULATIONREQUEST,
		CALCULATIONSPEC,
		COVERAGE,
		COVERAGEGROUP,
		COVERAGEGROUPMEMBERSHIP,
		COVERAGEFILE,
		DEDUCTIONFILE,
		DOCUMENT,
		DEPARTMENT,
		EMPLOYEEFILE,
		EMPLOYEECVGCLASS,
		EMPLOYER,
		EMPLOYERCONTACT,
		EMPLOYERREFERENCE,
		EMPLOYEE,
		EMPLOYEEDEPENDENT,
		EMPLOYMENTPERIOD,
		FILENOTICES,
		FILERECORDREJECTIONS,
		FILESTEPHANDLERS,
		INSURANCEFILE,
		IRS1094B,
		IRS1094C,
		IRS1094CFILE,
		IRS1094FILING,
		IRS1094SUBMISSION,
		IRS10945BCALC,
		IRS10945CCALC,
		IRS1095B,
		IRS1095C,
		IRS1095CFILE,
		IRS1095FILING,
		IRS1095SUBMISSION,
		LOCATION,
		NOTEACCOUNT,
		NOTEEMPLOYEE,
		PARSEPATTERN,
		PARSEDATEFORMAT,
		PAY,
		PAYCLASS,
		PAYCONVERSIONCALC,
		PAYFILE,
		PAYPERIOD,
		PAYPERIODFILE,
		PIPELINECHANNEL,
		PIPELINEREQUEST,
		PIPELINEFILEHIST,
		PIPELINESPECS,
		PLANYEAR,
		TAXYEARSERVICELEVEL,
		TAXYEAR,
		TAXMONTH,
		UPLOADTYPE,
		USER
	}
	
	// Status Message Type
	public static enum StatusMessageType{
		INFO,
		ATTENTION,
		CAUTION,
		ERROR
	}
	
	// Logging Type
	public static enum loggingType{
		INFO,
		DEBUG,
		ERROR
	}
	
	// The current RawDetail source type
	public static enum RawDetailType{
		EMPLOYEEFILE,
		EMPLOYEESECONDARY,
		COVERAGEFILE,
		COVERAGESECONDARY,
		PAYFILE
	}
	
	public static enum RawRecordType{
		EMPLOYEE,
		COVERAGE,
		IRS1094C,
		PAY,
		DEPENDENT,
		OTHER
	}
	
	
	
	// The current screen state
	public static enum ScreenType
	{
		AIRERROR,
		AIRERROREDIT,
		AIRERRORADD,
		AIRFILINGEVENT,
		AIRFILINGEVENTERRORREPORT,
		AIRFILINGEVENTEDIT,
		AIRFILINGEVENTADD,
		AIRSTATUSREQUEST,
		AIRSTATUSREQUESTEDIT,
		AIRSTATUSREQUESTADD,
		AIRTRANSMISSION,
		AIRTRANSMISSIONEDIT,
		AIRTRANSMISSIONADD,
		BENEFIT,
		DWIQUEUE,
		DOCUMENT,
		CALCULATIONQUEUE,
		HOME,
		HRACCOUNT,
		HREMPLOYER,
		HREMPLOYEE,
		ACCOUNT,
		ACCOUNTFROMPIPELINEQUEUE,
		EMPLOYER,
		EMPLOYERFROMPIPELINEQUEUE,
		EMPLOYEE,
		EMPLOYEEFROMTAXYEAR,
		EMPLOYEEMERGE,
		EXPORTQUEUE,
		SECONDARY,
		ACCOUNTCONTACT,
		EMPLOYERCONTACT,
		ACCOUNTASSOCIATEDPROPERTY,
		EMPLOYERASSOCIATEDPROPERTY,
		EMPLOYMENTPERIOD,
		EMPLOYEECOVERAGEPERIOD,
		EMPLOYERCOVERAGEPERIOD,
		EMPLOYERELIGIBILITYPERIOD,
		DEPARTMENT,
		TAXYEAR,
		TAXMONTH,
		LOCALUSER,
		ACCOUNTUSER,
		USER,
		GUISE,
		PLAN,
		PLANPROVIDER,
		PLANSPONSOR,
		PLANCARRIER,
		PLANYEAROFFERING,
		PLANYEAROFFERINGPLAN,
		PLANCOVERAGECLASS,
		OPERATION,
		PIPELINEEMPLOYEEFILE,
		PIPELINECOVERAGEFILE,
		PIPELINEIRS1094CFILE,
		PIPELINEIRS1095CFILE,
		PIPELINEPLANFILE,
		PIPELINEPAYFILE,
		PIPELINEPAYFILEADD,
		PIPELINEPAYFILEEDIT,
		PIPELINEPAYFILEEDITFROMEMPLOYER,
		PIPELINEPAYPERIODFILE,
		PIPELINEPARSEPATTERN,
		PIPELINEPARSEDATEFORMAT,
		PIPELINECHANNEL,
		PIPELINESPECIFICATION,
		PIPELINESPECIFICATIONFROMPIPELINEQUEUE,
		PIPELINESPECIFICATIONFROMRAW,
		PIPELINESPECIFICATIONFROMACCOUNT,
		PIPELINESPECIFICATIONFROMEMPLOYER,
		PIPELINEFILESPECIFICATION,
		PIPELINESPECIFICATIONFROMPAYFILE,
		PIPELINESPECIFICATIONFROMEMPLOYEEFILE,
		PIPELINESPECIFICATIONFROMCOVERAGEFILE,
		PIPELINESPECIFICATIONFROMPAYFILEEDIT,
		PIPELINEDYNAMICFILESPECIFICATION,
		PIPELINEDYNAMICFILESPECIFICATIONFROMRAW,
		PIPELINEDYNAMICFILESPECIFICATIONFROMQUEUE,
		PIPELINEDYNAMICFILESPECIFICATIONFROMACCOUNT,
		PIPELINEDYNAMICFILESPECIFICATIONFROMEMPLOYER,
		PIPELINEDYNAMICFILESPECIFICATIONFROMPAYFILE,
		PIPELINEDYNAMICFILESPECIFICATIONFROMEMPLOYEEFILE,
		PIPELINEDYNAMICFILESPECIFICATIONFROMCOVERAGEFILE,
		MAPPERCOVERAGE,
		MAPPERCOVERAGEFROMQUEUE,
		MAPPERCOVERAGEFROMRAW,
		MAPPERCOVERAGEFROMUPLOAD,
		MAPPERCOVERAGEFROMACCOUNT,
		MAPPERCOVERAGEFROMEMPLOYER,
		MAPPERCOVERAGEFROMPAYFILE,
		MAPPERCOVERAGEFROMEMPLOYEEFILE,
		MAPPERCOVERAGEFROMCOVERAGEFILE,
		MAPPERDEDUCTION,
		MAPPERDEDUCTIONFROMUPLOAD,
		MAPPEREMPLOYEE,
		MAPPEREMPLOYEEFROMRAW,
		MAPPEREMPLOYEEFROMQUEUE,
		MAPPEREMPLOYEEFROMACCOUNT,
		MAPPEREMPLOYEEFROMEMPLOYER,
		MAPPEREMPLOYEEFROMPAYFILE,
		MAPPEREMPLOYEEFROMEMPLOYEEFILE,
		MAPPEREMPLOYEEFROMCOVERAGEFILE,
		MAPPEREMPLOYEEFROMUPLOAD,
		MAPPERINSURANCE,
		MAPPERINSURANCEFROMUPLOAD,
		MAPPERIRS1094C,
		MAPPERIRS1095C,
		MAPPERPAY,
		MAPPERPAYFROMRAW,
		MAPPERPAYFROMQUEUE,
		MAPPERPAYFROMUPLOAD,
		PIPELINEFILESTEPHANDLER,
		PIPELINEROWIGNORE,
		RAWCONVERSIONFAILURE,
		RAWCOVERAGESECONDARY,
		RAWEMPLOYEESECONDARY,
		RAWINVALIDATION,
		RAWNOTICE,
		UPLOADER,
		EMPLOYERPAYFILE,
		ACCOUNTEDIT,
		ACCOUNTADD,
		EMPLOYEREDIT,
		EMPLOYERADD,
		EMPLOYEEEDIT,
		EMPLOYEEADD,
		SECONDARYEDIT,
		SECONDARYADD,
		ACCOUNTCONTACTEDIT,
		ACCOUNTCONTACTADD,
		EMPLOYERCONTACTEDIT,
		EMPLOYERCONTACTADD,
		ACCOUNTASSOCIATEDPROPERTYEDIT,
		EMPLOYERASSOCIATEDPROPERTYEDIT,
		EMPLOYMENTPERIODEDIT,
		EMPLOYMENTPERIODADD,
		EMPLOYEECOVERAGEPERIODEDIT,
		EMPLOYEECOVERAGEPERIODADD,
		EMPLOYERCOVERAGEPERIODEDIT,
		DEPARTMENTEDIT,
		DEPARTMENTADD,
		IRS1094B,
		IRS1094BEDIT,
		IRS1094BADD,
		IRS1094C,
		IRS1094CEDIT,
		IRS1094CADD,
		IRS1094FILING,
		IRS1094FILINGEDIT,
		IRS1094FILINGADD,
		IRS1094SUBMISSION,
		IRS1094SUBMISSIONEDIT,
		IRS1094SUBMISSIONADD,
		IRS1095B,
		IRS1095BEDIT,
		IRS1095BADD,
		IRS1095C,
		IRS1095CEDIT,
		IRS1095CADD,
		IRS1095BCOVEREDSECONDARY,
		IRS1095BCOVEREDSECONDARYEDIT,
		IRS1095BCOVEREDSECONDARYADD,
		IRS1095CCOVEREDSECONDARY,
		IRS1095CCOVEREDSECONDARYEDIT,
		IRS1095CCOVEREDSECONDARYADD,
		IRS1095FILING,
		IRS1095FILINGEDIT,
		IRS1095FILINGADD,
		IRS1095SUBMISSION,
		IRS1095SUBMISSIONEDIT,
		IRS1095SUBMISSIONADD,
		TAXYEAREDIT,
		TAXYEARADD,
		TAXMONTHEDIT,
		TAXMONTHADD,
		LOCALUSEREDIT,
		ACCOUNTUSEREDIT,
		USEREDIT,
		USERADD,
		GUISEEDIT,
		PAYPERIOD,
		PLANEDIT,
		PLANADD,
		PLANPROVIDEREDIT,
		PLANPROVIDERADD,
		PLANSPONSOREDIT,
		PLANSPONSORADD,
		PLANCARRIEREDIT,
		PLANCARRIERADD,
		PLANYEAROFFERINGEDIT,
		PLANYEAROFFERINGADD,
		PLANYEAROFFERINGPLANEDIT,
		PLANYEAROFFERINGPLANADD,
		PLANCOVERAGECLASSEDIT,
		PLANCOVERAGECLASSADD,
		OPERATIONEDIT,
		PIPELINEEMPLOYEEFILEEDIT,
		PIPELINEEMPLOYEEFILEADD,
		PIPELINECOVERAGEFILEEDIT,
		PIPELINECOVERAGEFILEADD,
		PIPELINEIRS1094CFILEEDIT,
		PIPELINEIRS1095CFILEEDIT,
		PIPELINEPLANFILEEDIT,
		PIPELINEPLANFILEADD,
		PIPELINEPAYPERIODFILEEDIT,
		PIPELINEPAYPERIODFILEADD,
		PIPELINEPARSEPATTERNEDIT,
		PIPELINEPARSEPATTERNADD,
		PIPELINEPARSEDATEFORMATEDIT,
		PIPELINEPARSEDATEFORMATADD,
		PIPELINEQUEUE,
		PIPELINEQUEUEDETAIL,
		PIPELINEQUEUEDETAILEDIT,
		PIPELINERAWFIELDGRID,
		PIPELINERAWFIELDGRIDFROMACCOUNT,
		PIPELINERAWFIELDGRIDFROMEMPLOYER,
	    PIPELINERAWFIELDGRIDFROMUPLOADER,
		PIPELINERAWPAY,
		PIPELINERAWEMPLOYEE,
		PIPELINERAWCOVERAGE,
		PIPELINECHANNELEDIT,
		PIPELINECHANNELADD,
		PIPELINESPECIFICATIONEDIT,
		PIPELINESPECIFICATIONADD,
		PIPELINEDYNAMICFILESPECIFICATIONEDIT,
		PIPELINEDYNAMICFILESPECIFICATIONADD,
		PIPELINEFILESTEPHANDLEREDIT,
		PIPELINEFILESTEPHANDLERADD
	}
	
	// US states and territories
	public static List<String> StateCodes = Arrays.asList(
		"AL-ALABAMBA",
		"AK-ALASKA",
		"AS-AMERICAN SOMOA",
		"AZ-ARIZONA",
		"AR-ARKANSAS",
		"CA-CALIFORNIA",
		"CO-COLORADO",
		"CT-CONNECTICUT",
		"DE-DELAWARE",
		"DC-DISTRICT OF COLUMBIA",
		"FM-FEDERATED STATES OF MICRONESIA",
		"FL-FLORIDA",
		"GA-GEORGIA",
		"GU-GUAM",
		"HI-HAWAII",
		"ID-IDAHO",
		"IL-IllINOIS",
		"IN-INDIANA",
		"IA-IOWA",
		"KS-KANSAS",
		"KY-KENTUCKY",
		"LA-LOUISIANA",
		"ME-MAINE",
		"MH-MARSHALL ISLANDS",
		"MD-MARYLAND",
		"MA-MASSACHUSETTS",
		"MI-MICHIGAN",
		"MN-MINNESOTA",
		"MS-MISSISSIPPI",
		"MO-MISSOURI",
		"MT-MONTANA",
		"NE-NEBRASKA",
		"NV-NEVADA",
		"NH-NEW HAMPSHIRE",
		"NJ-NEW JERSEY",
		"NM-NEW MEXICO",
		"NY-NEW YORK",
		"NC-NORTH CAROLINA",
		"ND-NORTH DAKOTA",
		"MP-NORTHERN MARIANA ISLANDS",
		"OH-OHIO",
		"OK-OKLAHOMA",
		"OR-OREGON",
		"PW-PALAU",
		"PA-PENNSYLVANIA",
		"PR-PUERTO RICO",
		"RI-RHODE ISLAND",
		"SC-SOUTH CAROLINA",
		"SD-SOUTH DAKOTA",
		"TN-TENNESSEE",
		"TX-TEXAS",
		"UT-UTAH",
		"VT-VERMONT",
		"VI-VIRGIN ISLANDS",
		"VA-VIRGINIA",
		"WA-WASHINGTON",
		"WV-WEST VIRGINNNIA",
		"WI-WISCONSIN",
		"WY-WYOMING",
		"AA-ARMED FORCES AMERICAS",
		"AE-ARMED FORCES OTHER",
		"AP-ARMED FORCES PACIFIC"
	);

	// CA Provinces
	public static List<String> ProvinceCodes = Arrays.asList(
		"AB-ALBERTA",
		"BC-BRITISH COLUMBIA",
		"MB-MANITOBA",
		"NB-NEW BRUNSWICK",
		"NF-NEW FOUNDLAND",
		"NT-NORTHWEST TERRITORIES",
		"NS-NOVA SCOTIA",
		"NU-NUNAVUT",
		"ON-ONTARIO",
		"PE-PRINCE EDWARD ISLAND",
		"QC-QUEBEC",
		"SK-SASKATCHEWAN",
		"YT-YUKON"
	);
	
	// Current Object type in System Data Popup
	public enum SystemDataType
	{
		NONE,
		ACCOUNT,
		BENEFIT,
		CALCULATIONREQUEST,
		CHANNEL,
		CONTACT,
		COVERAGE,
		COVERAGECLASS,
		COVERAGEFILE,
		COVERAGEGROUPMEMBERSHIP,
		DATAPROPERTY,
		DEDUCTIONFILE,
		DEPARTMENT,
		DEPENDENT,
		ELIGIBILITYPERIOD,
		EMPLOYEE,
		EMPLOYEEFILE,
		EMPLOYER,
		EMPLOYMENTPERIOD,
		FILESTEPHANDLER,
		INSURANCEFILE,
		IRS1094B,
		IRS1094C,
		IRS1095B,
		IRS1095C,
		IRS1094CFILE,
		IRS1095FILING,
		IRS1095CFILE,
		IRSAIRRCPTFILE,
		IRSAIRERRFILE,
		LOCATION,
		NOTE,
		PAY,
		PAYFILE,
		PAYPERIOD,
		PAYPERIODFILE,
		PLANYEAR,
		SPECIFICATION,
		TAXYEAR,
		UPLOADTYPE,
		USER
	}
	
	public enum ReferenceType {
		NONREFERENCE,
		EECOVERAGEGROUP,
		EEDEPARTMENT,
		EEGENDERTYPE,
		EELOCATION,
		EEPAYCODE,
		EEUNIONTYPE,
		PYPAYCODE,
		PYPAYFREQUENCY,
		PYGENDERTYPE,
		PYUNIONTYPE
	}

}
