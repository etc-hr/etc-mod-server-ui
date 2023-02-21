package com.etc.admin;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PublicKey;

public class AdminManager implements Serializable {

	private static final long serialVersionUID = 4703265033117314687L;

	// CONFIG FILE KEYS
	public static final String CFG_UUID_KEY = "com.etc.sfta.AdminApp.uuid";
	public static final String CFG_LEML_KEY = "com.etc.sfta.AdminApp.lastEmail";
	public static final String CFG_LDIR_KEY = "com.etc.sfta.AdminApp.lastDirectory";
	//public static final String CFG_LDIR_KEY = "com.etc.sfta.SecureFileTransferAgent.lastDirectory";

	private static String homeDirectory = null;
	private static String documentDirectory = null;
	private static String tempDirectory = null;
	private static String fileSourceDirectory = null;
	private static String configDirectory = null;
	private static String coreKeyDirectory = null;

	private static String email = null;
	private static String password = null;
	private static String epUUID = null;
	private static KeyPair keyPair = null;
	private static PublicKey coreKey = null;
	
	public static String getHomeDirectory() {
		return homeDirectory;
	}

	public static void setHomeDirectory(String homeDirectory) {
		AdminManager.homeDirectory = homeDirectory;
	}

	public static String getDocumentDirectory() {
		return documentDirectory;
	}

	public static void setDocumentDirectory(String documentDirectory) {
		AdminManager.documentDirectory = documentDirectory;
	}

	public static String getCoreKeyDirectory() {
		return coreKeyDirectory;
	}

	public static void setCoreKeyDirectory(String coreKeyDirectory) {
		AdminManager.coreKeyDirectory = coreKeyDirectory;
	}
	public static String getTempDirectory() {
		return tempDirectory;
	}

	public static void setTempDirectory(String tempDirectory) {
		AdminManager.tempDirectory = tempDirectory;
	}

	public static String getFileSourceDirectory() {
		return fileSourceDirectory;
	}

	public static void setFileSourceDirectory(String fileSourceDirectory) {
		AdminManager.fileSourceDirectory = fileSourceDirectory;
	}

	public static String getConfigDirectory() {
		return configDirectory;
	}

	public static void setConfigDirectory(String configDirectory) {
		AdminManager.configDirectory = configDirectory;
	}

	public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		AdminManager.email = email;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		AdminManager.password = password;
	}

	public static String getEpUUID() {
		return epUUID;
	}

	public static void setEpUUID(String epUUID) {
		AdminManager.epUUID = epUUID;
	}

	public static KeyPair getKeyPair() {
		return keyPair;
	}

	public static void setKeyPair(KeyPair keyPair) {
		AdminManager.keyPair = keyPair;
	}

	public static PublicKey getCoreKey() {
		return coreKey;
	}

	public static void setCoreKey(PublicKey coreKey) {
		AdminManager.coreKey = coreKey;
	}

	public static String getCfgUuidKey() {
		return CFG_UUID_KEY;
	}

	public static String getCfgLemlKey() {
		return CFG_LEML_KEY;
	}

	public static String getCfgLdirKey() {
		return CFG_LDIR_KEY;
	}
}
