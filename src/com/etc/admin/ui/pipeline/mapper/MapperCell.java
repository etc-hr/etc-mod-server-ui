package com.etc.admin.ui.pipeline.mapper;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.control.TextArea;

/**
 * <p>
 * MapperCell is used to manage a mapped cell in a spreadsheet and holds links to the<br>
 *  MappedFields that point to it.</p>
 * 
 * @author Pete Morgan 12/1/2020
 */
public class MapperCell implements Serializable
{
	private static final long serialVersionUID = -8250710362559514729L;

	private Integer idx = null;
	private ColorPalette color = null;
	private ArrayList<MapperField> mapperFields = new ArrayList<MapperField>(0);
	private TextArea node = null;

	public MapperCell(final Integer idx, final TextArea node)
	{
		super();

		this.idx = idx;
		this.node = node;
	}

	public Integer getIdx() { return idx; }

	public ArrayList<MapperField> getMapperFields() { return mapperFields; }

	public ColorPalette getColor() { return color; }
	public void setColor(ColorPalette color) { this.color = color; }

	public TextArea getNode() { return node; }
	public void setNode(TextArea node) { this.node = node; }
}