package com.etc.admin.ui.pipeline.mapper;

import java.io.Serializable;

import com.etc.corvetto.ems.pipeline.entities.PipelineParseDateFormat;
import com.etc.corvetto.ems.pipeline.entities.PipelineParsePattern;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * <p>
 * MapperField is used to hold all fx controls used together in a colorable set.</p>
 * 
 * @author Pete Morgan 12/1/2020
 */
public class MapperField implements Serializable
{
	private static final long serialVersionUID = -1858545029289817924L;

	private String title = null;					//THE DESCRIPTION FOR THE SPREADSHEET DISPLAY
	private int cellWidth = 0;
	private StackPane stkPane = null;			//THE ENCLOSING STACKPANE
	private CheckBox ckEnabled = null;			//THE ENABLE-FIELD CHECKBOX
	private TextField txtFldIndex = null;		//THE TEXT FIELD THAT HOLDS THE COLUMN INDEX
	private Button btnParsePtrn = null;			//THE BUTTON FOR CONFIGURING PARSE PATTERNS
	private Button btnDtFormat = null;			//THE BUTTON FOR CONFIGURING DATE FORMATS
	private Button btnReferences = null;			//THE BUTTON FOR CONFIGURING FIELD REFERENCES
	private PipelineParsePattern parsePattern = null;
	private PipelineParseDateFormat dateFormat = null;
	private Long parsePatternId = null;
	private Long dateFormatId = null;

	ViewMapperCoverageFormController covForm = null;

	/**
	 * <p>
	 * Convenience constructor for inlining MapperField elements.</p>
	 * 
	 * @param sp StackPane
	 * @param cb CheckBox
	 * @param tf TextField
	 * @param bPP Button for Parse Patterns
	 * @param bDF Button for Date Formats
	 * @param bRfs Button for References
	 * @param ttl String title for display
	 */
	public MapperField(final StackPane sp, final CheckBox cb, final TextField tf, final Button bPP, final Button bDF, final Button bRfs, final String ttl, final int cellWidth)
	{
		super();

		this.stkPane = sp;
		this.ckEnabled = cb;
		this.txtFldIndex = tf;
		this.btnParsePtrn = bPP;
		this.btnDtFormat = bDF;
		this.btnReferences = bRfs;
		this.title = ttl;
		this.cellWidth = cellWidth;
		this.parsePattern = parsePattern;
		this.dateFormat = dateFormat;
		this.parsePatternId = parsePatternId;
		this.dateFormatId = dateFormatId;
	}

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	public int getCellWidth() { return cellWidth; }
	public void setCellWidth(int cellWidth) { this.cellWidth = cellWidth; }

	public StackPane getStkPane() { return stkPane; }

	public CheckBox getCkEnabled() { return ckEnabled; }

	public TextField getTxtFldIndex() { return txtFldIndex; }

	public Button getBtnParsePtrn() { return btnParsePtrn; }

	public Button getBtnDtFormat() { return btnDtFormat; }

	public Button getBtnReferences() { return btnReferences; }
	
	public Long getParsePatternId() { return parsePatternId; }
	public void setParsePatternId(Long patternId) 
	{
		parsePatternId = patternId; 
    	btnParsePtrn.setFont(Font.font("Veranda", 12.0));

   	 	if(parsePatternId == null && btnParsePtrn != null)
   	 		btnParsePtrn.setText("+ Pattern");
   	 	else
   	 		btnParsePtrn.setText("Pattern: " + String.valueOf(parsePatternId.longValue()));
		
	}
	
	public Long getDateFormatId() { return dateFormatId; }
	public void setDateFormatId(Long formatId) 
	{
		dateFormatId = formatId; 
		btnDtFormat.setFont(Font.font("Veranda", 12.0));

   	 	if(dateFormatId == null && btnDtFormat != null)
   	 		btnDtFormat.setText("+ Format");
   	 	else
   	 		btnDtFormat.setText("Format: " + String.valueOf(dateFormatId.longValue()));

	}
	
    public PipelineParsePattern getParsePattern() { return parsePattern; }

    public void setParsePattern(PipelineParsePattern pattern) 
    {
    	parsePattern = pattern;
    	btnParsePtrn.setFont(Font.font("Veranda", 12.0));

   	 	if(parsePattern == null && btnParsePtrn != null)
   	 		btnParsePtrn.setText("+ Pattern");
   	 	else
   	 		btnParsePtrn.setText("Pattern: " + String.valueOf(parsePattern.getId()));
    }
    
    public PipelineParseDateFormat getDateFormat() { return dateFormat; }
  
    public void setDateFormat(PipelineParseDateFormat format) 
    {
    	dateFormat = format;
    	btnDtFormat.setFont(Font.font("Veranda", 12.0));

   	 	if(dateFormat == null)
   	 		btnDtFormat.setText("+ Format");
   	 	else
   	 		btnDtFormat.setText("Format: " + String.valueOf(dateFormat.getId()));
    }
}