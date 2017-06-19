package com.ruvla.plsos.agg.model.datapersist;

import java.io.Serializable;
import java.util.HashMap;

import com.ruvla.plsos.agg.model.configuration.ConfigurationProvider;

public class DBConfig implements Serializable {
	private String dbName;
	private String host;
	private String port;
	private String user;
	private String password;
	private HashMap<String, String> others;

	public DBConfig(ConfigurationProvider provider) {
		this.dbName = provider.getDbconfigDbname();
		this.host = provider.getDbconfigHost();
		this.port = provider.getDbconfigPort();
		this.user = provider.getDbconfigUser();
		this.password = provider.getDbconfigPassword();
		this.setOthers(new HashMap<String, String>());
	}

	public DBConfig(String dbName, String host, String port, String user, String password) {
		this.dbName = dbName;
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.setOthers(new HashMap<String, String>());
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public HashMap<String, String> getOthers() {
		return others;
	}

	public void setOthers(HashMap<String, String> others) {
		this.others = others;
	}
}
