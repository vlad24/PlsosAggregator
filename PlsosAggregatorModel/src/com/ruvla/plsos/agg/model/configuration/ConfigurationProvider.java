package com.ruvla.plsos.agg.model.configuration;

public interface ConfigurationProvider {

	/**
	 * Build the provider.
	 * @param source - abstract object that can be used to build the configuration provider from that object(e.g. from XML file).
	 */
	public void build(Object source);

	/**
	 * Rebuild the provider.
	 * @param source - abstract object that can be used to rebuild the configuration provider from that object(e.g. HashMap object with updated configurations).
	 */
	public void rebuild(Object updateObject);

	/**
	 * @return url that can be used by data manipulator to authenticate on the server.
	 */
	public String getUrlAuth();

	/**
	 * @return url that can be used by data manipulator to upload all data to this address.
	 */
	public String getUrlUpload();

	/**
	 * @return login that can be used by data manipulator to authenticate on the server.
	 */
	public String getAuthLogin();

	/**
	 * @return password that can be used by data manipulator to authenticate on the server.
	 */
	public String getAuthPassword();

	/**
	 * @return key parameter that can be used by data manipulator to authenticate on the server.
	 */
	public String getAuthKey();

	/**
	 * @return secret parameter that can be used by data manipulator to authenticate on the server.
	 */
	public String getAuthSecret();

	/**
	 * @return token that was got from a server after authentication
	 */
	public String getAuthToken();

	/**
	 * @return name of database where saver will put all the collected data
	 */
	public String getDbconfigDbname();

	/**
	 * @return host of the database where saver will put all collected the data
	 */
	public String getDbconfigHost();

	/**
	 * @return port of database where saver will put all the collected data
	 */
	public String getDbconfigPort();

	/**
	 * @return user of database where saver will put all the collected data
	 */
	public String getDbconfigUser();

	/**
	 * @return password of database where saver will put all the collected data
	 */
	public String getDbconfigPassword();

	/**
	 * @return charset that will be used for serializing collected objects in text format (if chosen) for sending to a remote server 
	 */
	public String getSendingCharset();


	/**
	 * @return flag indicating whether all units will be immediately erased from the database after sending to the server
	 */
	public boolean getManipulatorCleanMode();

	/**
	 * @return format that collected units will be serialized to before sending to ramote server
	 */
	public String getFormat();

	/**
	 * @return flag indicating whether manipulator will send data which is sorted by time of collecting
	 */
	public boolean getManipulatorSerializedSending();

	/**
	 * @return amount of aggregators that an aggregator maanger will be able to hold
	 */
	public int getAggregationManagerCapacity();


	/**
	 * @return amount of future parallel collector threads
	 */
	public int getDefaultThreadCount();

	/**
	 * Get refresh period of detector in seconds.
	 * If period is zero it means that detector is likely to work in asynchronous mode
	 * @return refresh period of detector in seconds.
	 */
	public int getDefaultDetectorRefresh();
	/**
	 * @return default amount of time units(by now: seconds) after which saver will try to fetch 'portion size' of units from aggregator
	 */
	public int getDefaultSaverRefresh();

	/**
	 * @return default amount of units after getting which saver will fetch this amount of units from aggregator owner
	 */
	public int getDefaultUnitThreshold();

	/**
	 * @return default amount of data that saver will take after a single request to an aggregator owner
	 */
	public int getDefaultPortionSize();

}