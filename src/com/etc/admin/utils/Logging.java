package com.etc.admin.utils;

import java.util.Calendar;

import com.etc.admin.utils.Utils.LogType;

public class Logging {
	
	private Calendar logTime;
	private String description;
	private LogType logType; 

	//////////////////////////////////////////////////////
	
	public Calendar getLogTime() { return logTime; }
	public void setLogTime(Calendar logTime) { this.logTime = logTime; }

	public LogType getLogType() { return logType; }
	public void setLogType(LogType logType) { this.logType = logType; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

}
