package com.ruvla.plsos.agg.model.configuration.configpacks;

import java.io.Serializable;
import java.util.HashMap;

import com.ruvla.plsos.agg.model.configuration.ConfigurationProvider;

@SuppressWarnings("serial")
public class UploadConfig implements Serializable {

	protected String authURL;
	protected String uploadURL;
	protected String authKey;
	protected String authLogin;
	protected String authPassword;
	protected String authSecret;
	protected Object token;
	protected HashMap<String, String> others;

	/**
	 * Configurator constructor
	 * @param provider provider that will supply all the necessary configurations for building the object
	 */
	public UploadConfig(ConfigurationProvider provider) {
		this.authURL = provider.getUrlAuth();
		this.uploadURL = provider.getUrlUpload();
		this.authKey = provider.getAuthKey();
		this.authLogin = provider.getAuthLogin();
		this.authPassword = provider.getAuthPassword();
		this.authSecret = provider.getAuthSecret();
		this.token = provider.getAuthToken();
	}

	/**
	 * Full constructor
	 * @param authURL authorization URL for manipulator
	 * @param uploadURL address where a manipulator will upload all collected data
	 * @param authLogin login that may be used by manipulator for authorization
	 * @param authPassword password/password hash that may be used by manipulator for authorization
	 * @param authKey first additional value that may be used by manipulator for authorization
	 * @param authSecret second additional value that may be used during authorization
	 * @param credsFromServer value for tokens got from server
	 * @param others hash map for values that cannot be stored by the provided fields
	 */
	public UploadConfig(String authURL, String uploadURL, String authKey, String authLogin, String authPassword,
			String authSecret, Object credsFromServer, HashMap<String, String> others) {
		this.authURL = authURL;
		this.uploadURL = uploadURL;
		this.authKey = authKey;
		this.authLogin = authLogin;
		this.authPassword = authPassword;
		this.authSecret = authSecret;
		this.token = credsFromServer;
		this.others = others;
	}

	/**
	 * Get authorization URL for manipulator
	 * @return authorization URL for manipulator
	 */
	public String getAuthURL() {
		return authURL;
	}

	/**
	 * Set authorization URL for manipulator
	 * @param authURL authorization URL for manipulator
	 */
	public void setAuthURL(String authURL) {
		this.authURL = authURL;
	}

	/**
	 * Get address where a manipulator will upload all collected data
	 * @return address where a manipulator will upload all collected data
	 */
	public String getUploadURL() {
		return uploadURL;
	}

	/**
	 * Set address where a manipulator will upload all collected data
	 * @param uploadURL address where a manipulator will upload all collected data
	 */
	public void setUploadURL(String uploadURL) {
		this.uploadURL = uploadURL;
	}

	/**
	 * Get login that may be used by manipulator for authorization
	 * @return login that may be used by manipulator for authorization
	 */
	public String getAuthLogin() {
		return authLogin;
	}

	/**
	 * Set login that may be used by manipulator for authorization
	 * @param authLogin login that may be used by manipulator for authorization
	 */
	public void setAuthLogin(String authLogin) {
		this.authLogin = authLogin;
	}

	/**
	 * Get first additional value that may be used by manipulator for authorization
	 * @return first additional value that may be used by manipulator for authorization
	 */
	public String getAuthKey() {
		return authKey;
	}

	/**
	 * Set first additional value that may be used by manipulator for authorization
	 * @param authKey first additional value that may be used by manipulator for authorization
	 */
	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	/**
	 * Get second additional value that may be used during authorization
	 * @return second additional value that may be used during authorization
	 */
	public String getAuthSecret() {
		return authSecret;
	}

	/**
	 * Set second additional value that may be used during authorization
	 * @param authSecret second additional value that may be used during authorization
	 */
	public void setAuthSecret(String authSecret) {
		this.authSecret = authSecret;
	}

	/**
	 * Get hash map for values that cannot be stored by the provided fields
	 * @return hash map for values that cannot be stored by the provided fields
	 */
	public HashMap<String, String> getOthers() {
		return others;
	}

	/**
	 * Set hash map for values that cannot be stored by the provided fields
	 * @param others hash map for values that cannot be stored by the provided fields
	 */
	public void setOthers(HashMap<String, String> others) {
		this.others = others;
	}

	/**
	 * Get password/password hash that may be used by manipulator for authorization
	 * @return password/password hash that may be used by manipulator for authorization
	 */
	public String getAuthPassword() {
		return authPassword;
	}

	/**
	 * Set password/password hash that may be used by manipulator for authorization
	 * @param authPassword password/password hash that may be used by manipulator for authorization
	 */
	public void setAuthPassword(String authPassword) {
		this.authPassword = authPassword;
	}

	/**
	 * Get value for tokens got from server
	 * @return value for tokens got from server
	 */
	public Object getToken() {
		return token;
	}

	/**
	 * Set value for tokens got from server
	 * @param tokenFromServer value for tokens got from server to be set
	 */
	public void setToken(Object tokenFromServer) {
		this.token = tokenFromServer;
	}

}
