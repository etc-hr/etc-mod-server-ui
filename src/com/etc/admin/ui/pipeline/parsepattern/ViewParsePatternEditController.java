package com.etc.admin.ui.pipeline.parsepattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils;
import com.etc.admin.utils.Utils.ScreenType;
import com.etc.corvetto.ems.pipeline.entities.PipelineParsePattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ViewParsePatternEditController {
	
	@FXML
	private TextField prseNameLabel;
	@FXML
	private TextField prseDescriptionLabel;
	@FXML
	private TextField prseExampleSourceLabel;
	@FXML
	private TextField prseExampleResultLabel;
	@FXML
	private TextArea prsePatternLabel;
	@FXML
	private TextArea prseRegexInfo;
	@FXML
	private Button prseCancelButton;
	@FXML
	private Button prseSaveButton;
	@FXML
	private TextField prseTestResultField;
	@FXML
	private TextArea prseTestErrorArea;
	@FXML
	private CheckBox prseTestMatchAllCheck;
	@FXML
	private CheckBox prseTestRequireGetGroupCheck;

	@FXML
	private Label prseCoreIdLabel;
	@FXML
	private Label prseCoreActiveLabel;
	@FXML
	private Label prseCoreBODateLabel;
	@FXML
	private Label prseCoreLastUpdatedLabel;
	
	/**
	 * initialize is called when the FXML is loaded
	 */

	@FXML
	public void initialize() {
		initControls();
		
		updateParsePatternData();		
	}

	private void initControls() {
		//load regex reference for the user
		setRegexInfo();
		
 	}	

	private void updateParsePatternData(){

		PipelineParsePattern pattern = DataManager.i().mPipelineParsePattern;
		if (pattern != null) {
			Utils.updateControl(prseNameLabel,pattern.getName());
			Utils.updateControl(prseDescriptionLabel,pattern.getDescription());
			Utils.updateControl(prseExampleSourceLabel,pattern.getExampleSource());
			Utils.updateControl(prseExampleResultLabel,pattern.getExampleResult());
			Utils.updateControl(prsePatternLabel,pattern.getPattern());
				
			//core data read only
			Utils.updateControl(prseCoreIdLabel,String.valueOf(pattern.getId()));
			Utils.updateControl(prseCoreActiveLabel,String.valueOf(pattern.isActive()));
			Utils.updateControl(prseCoreBODateLabel,pattern.getBornOn());
			Utils.updateControl(prseCoreLastUpdatedLabel,pattern.getLastUpdated());	
		}
	}

	private void updateParsePatternRequest(){
/*		PipelineParsePattern pattern = DataManager.i().mPipelineParsePattern;
		if (pattern != null) {
			//reset the updateRequest object if it has been used
			DataManager.i().mUpdatePipelineParsePatternRequest = null;			
			DataManager.i().mUpdatePipelineParsePatternRequest = new UpdatePipelineParsePatternRequest();
			DataManager.i().mUpdatePipelineParsePatternRequest.setPipelineParsePattern(pattern);
			
			DataManager.i().mUpdatePipelineParsePatternRequest.setName(prseNameLabel.getText());
			DataManager.i().mUpdatePipelineParsePatternRequest.setDescription(prseDescriptionLabel.getText());
			DataManager.i().mUpdatePipelineParsePatternRequest.setExampleSource(prseExampleSourceLabel.getText());
			DataManager.i().mUpdatePipelineParsePatternRequest.setExampleResult(prseExampleResultLabel.getText());
			DataManager.i().mUpdatePipelineParsePatternRequest.setPattern(prsePatternLabel.getText());
		}
*/	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.PIPELINEPARSEPATTERN);
	}	
	
	private boolean validateData() {
		boolean bReturn = true;
		
		//check for missing required data
		if ( !Utils.validate(prseNameLabel)) bReturn = false;
		if ( !Utils.validate(prseDescriptionLabel)) bReturn = false;
		if ( !Utils.validate(prsePatternLabel)) bReturn = false;

		//validate the parse pattern
		if (bReturn == true) bReturn = testPattern();
		
		return bReturn;
	}
	
	@FXML
	private void onSave(ActionEvent event) {

		//validate everything
		if (!validateData())
			return;
		
		//update our object
		updateParsePatternRequest();
		
		//save it to the repository and the server
		//FIXME: DataManager.i().saveCurrentPipelineParsePatternRequest();
		
		//and load the regular ui
		EtcAdmin.i().setScreen(ScreenType.PIPELINEPARSEPATTERN, true);
	}		
	
	@FXML
	private void onTestPattern(ActionEvent event) {
		testPattern();
	}
	
	private boolean testPattern() {
		//clear any errors form the last run
		prseTestErrorArea.setText("");
		// make sure we have something to test
		if (prseExampleSourceLabel.getText() == "" || prseExampleSourceLabel.getText().isEmpty()) {
			prseTestErrorArea.setText("Please supply an example source for regex pattern testing.");
			return false;
		}
		if (prsePatternLabel.getText() == "" || prsePatternLabel.getText().isEmpty()) {
			prseTestErrorArea.setText("Please enter a regex pattern to test.");
			return false;
		}
		
		String val = prseExampleSourceLabel.getText();
		String pptrn = prsePatternLabel.getText();
		Pattern ptrn = null;
		Matcher matchr = null;
		String rval = "";
		if(val != null ? !val.isEmpty() : false)
		{
			try {
				ptrn = Pattern.compile(pptrn);
				if(pptrn != null ? !pptrn.isEmpty() : false)
				{
					matchr = ptrn.matcher(val);
					// handle it according to if we are matching all of source
					if (prseTestMatchAllCheck.isSelected()) {
						if(matchr.matches()) {
							if(prseTestRequireGetGroupCheck.isSelected())
								rval = matchr.group("get");
							else
								rval = matchr.group();
						}
					} else {
						while (matchr.find()) {
							if(prseTestRequireGetGroupCheck.isSelected())
								rval += matchr.group("get");
							else
								rval += matchr.group();
						}
					}					
					if (rval == "" || rval.isEmpty())
						prseTestErrorArea.setText("No Match");
				}else
					rval = val;
			} catch (PatternSyntaxException ex) {
				prseTestErrorArea.setText(ex.getMessage());
				return false;
	        }			
			catch (IllegalArgumentException | IllegalStateException ex) {
				prseTestErrorArea.setText(ex.getMessage());
				return false;
 			}			
		}
		
		prseTestResultField.setText(rval);
		return true;
	}
	
	private void setRegexInfo() {
		
		prseRegexInfo.setText("Anchors and $\n\n"
		+ "^The        matches any string that starts with The \n"
		+ "end$        matches a string that ends with end \n"
		+ "^The end$   exact string match (starts and ends with The end)\n"
		+ "test        matches any string that has the text test in it\n"
		+ "\nQuantifiers + ? and {}\n\n"
		+ "abc*        matches a string that has ab followed by zero or more c\n"
		+ "abc+        matches a string that has ab followed by one or more c\n"
		+ "abc?        matches a string that has ab followed by zero or one c\n"
		+ "abc{2}      matches a string that has ab followed by 2 c\n"
		+ "abc{2,}     matches a string that has ab followed by 2 or more c\n"
		+ "abc{2,5}    matches a string that has ab followed by 2 up to 5 c\n"
		+ "a(bc)*      matches a string that has a followed by zero or more copies of the sequence bc\n"
		+ "a(bc){2,5}  matches a string that has a followed by 2 up to 5 copies of the sequence bc\n"
		+ "\nOR operators | or []\n\n"
		+ "a(b|c)      matches a string that has a followed by b or c\n"
		+ "a[bc]       same as previous\n"
		+ "\nCharacter classes \\d \\w \\s and .\n\n"
		+ "\\d         matches a single character that is a digit\n"
		+ "\\D         matches a single non-digit character\n"
		+ "\\w         matches a word character (alphanumeric character plus underscore)\n"
		+ "\\W         matches a non-word character (alphanumeric character plus underscore)\n"
		+ "\\s         matches a whitespace character (includes tabs and line breaks)\n"
		+ "\\S         matches a non-whitespace character (includes tabs and line breaks)\n"
		+ ".          matches any character\n"	
		+ "\\$\\d       matches a string that has a $ before one digit\n"
		+ "\nBracket expressions []\n\n"
		+ "[abc]       matches a string that has either an a or a b or a c\n"
		+ "[a-c]       same as previous\n"
		+ "[a-fA-F0-9] string that represents a single hexadecimal digit, case insensitive\n"
		+ "[0-9]%      string that has a character from 0 to 9 before a % sign\n"
		+ "[^a-zA-Z]   string that has not a letter from a to z or from A to Z\n");
	}
	
}


