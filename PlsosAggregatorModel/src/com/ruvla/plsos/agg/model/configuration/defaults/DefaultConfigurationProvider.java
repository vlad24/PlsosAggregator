package com.ruvla.plsos.agg.model.configuration.defaults;

import com.ruvla.plsos.agg.model.PlsosConstants;
import com.ruvla.plsos.agg.model.configuration.ConfigurationProvider;

public class DefaultConfigurationProvider implements ConfigurationProvider {

	@Override
	public void build(Object source) {

	}
	
	@Override
	public void rebuild(Object updateObject) {
		
	}

	@Override
	public String getUrlAuth() {
		return PlsosConstants.PLSOS_DEFAULT_UPLOAD_URL;
	}

	@Override
	public String getUrlUpload() {
		return PlsosConstants.PLSOS_DEFAULT_UPLOAD_URL;
	}

	@Override
	public String getAuthLogin() {
		return PlsosConstants.PLSOS_DEFAULT_USER;
	}

	@Override
	public String getAuthPassword() {
		return PlsosConstants.PLSOS_DEFAULT_PASSWORD;
	}

	@Override
	public String getAuthKey() {
		return PlsosConstants.PLSOS_DEFAULT_AUTH_VALUE;
	}

	@Override
	public String getAuthSecret() {
		return PlsosConstants.PLSOS_DEFAULT_AUTH_VALUE;
	}

	@Override
	public String getAuthToken() {
		return PlsosConstants.PLSOS_DEFAULT_AUTH_VALUE;
	}

	@Override
	public String getDbconfigDbname() {
		return PlsosConstants.PLSOS_DEFAULT_DBNAME;
	}

	@Override
	public String getDbconfigHost() {
		return PlsosConstants.PLSOS_DEFAULT_HOST;
	}

	@Override
	public String getDbconfigPort() {
		return PlsosConstants.PLSOS_DEFAULT_PORT;
	}

	@Override
	public String getDbconfigUser() {
		return PlsosConstants.PLSOS_DEFAULT_USER;
	}

	@Override
	public String getDbconfigPassword() {
		return PlsosConstants.PLSOS_DEFAULT_PASSWORD;
	}

	@Override
	public String getSendingCharset() {
		return PlsosConstants.PLSOS_DEFAULT_CHARSET;
	}

	@Override
	public int getDefaultUnitThreshold() {
		return PlsosConstants.PLSOS_DEFAULT_UNIT_THRESHOLD;
	}

	@Override
	public int getDefaultSaverRefresh() {
		return PlsosConstants.PLSOS_DEFAULT_SAVER_REFRESH_PERIOD;
	}

	@Override
	public int getDefaultPortionSize() {
		return PlsosConstants.PLSOS_DEFAULT_PORTION;
	}

	@Override
	public int getDefaultDetectorRefresh() {
		return PlsosConstants.PLSOS_DEFAULT_DETECTOR_REFRESH_PERIOD;
	}

	@Override
	public int getDefaultThreadCount() {
		return PlsosConstants.PLSOS_DEFAULT_THREAD_COUNT;
	}

	@Override
	public boolean getManipulatorCleanMode() {
		return false;
	}

	@Override
	public String getFormat() {
		return PlsosConstants.FORMAT_RAW;
	}

	@Override
	public boolean getManipulatorSerializedSending() {
		return false;
	}

	@Override
	public int getAggregationManagerCapacity() {
		return PlsosConstants.PLSOS_DEFAULT_AGG_CAPACITY;
	}


}
