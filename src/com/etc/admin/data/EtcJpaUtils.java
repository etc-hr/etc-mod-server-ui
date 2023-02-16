package com.etc.admin.data;

public class EtcJpaUtils 
{

	public static String enforceJPAConstraints(String valToCk, final String defVal, final int length) 
	{
		return valToCk != null ? (valToCk.length() > length ? valToCk.substring(0, length) : valToCk) : defVal;
	}
}
