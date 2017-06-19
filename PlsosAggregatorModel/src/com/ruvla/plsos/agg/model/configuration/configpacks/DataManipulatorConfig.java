package com.ruvla.plsos.agg.model.configuration.configpacks;

import java.io.Serializable;

import com.ruvla.plsos.agg.model.aggctrl.AggProcessor;
import com.ruvla.plsos.agg.model.configuration.ConfigurationProvider;
import com.ruvla.plsos.agg.model.configuration.Configurator;

@SuppressWarnings("serial")
public class DataManipulatorConfig implements Serializable {

	protected long aggid;
	protected Configurator configurator;
	protected UploadConfig uploadConfigs;
	protected String format;
	protected boolean cleanMode;
	protected AggProcessor postProcessor;
	protected boolean serializedSending;

	public DataManipulatorConfig(long aggid, Configurator configurator) {
		this.aggid = aggid;
		this.configurator = configurator;
		ConfigurationProvider provider = configurator.instantiateConfigProvider();
		this.uploadConfigs = new UploadConfig(provider);
		this.format = provider.getFormat();
		this.cleanMode = provider.getManipulatorCleanMode();
		this.serializedSending = provider.getManipulatorSerializedSending();
		this.postProcessor = configurator.instantiatePostProcessor();
	}

	/**
	 * @return whether this manipulator is sending data which is sorted by time of collecting
	 */
	public boolean isSerializedSending() {
		return serializedSending;
	}

	/**
	 * Set the mode of sending. If serialized sending is turned on manipulator can perform some sending optimizations.
	 * @param serializedSending flag which is true if manipulator sends data which is sorted by time of collecting 
	 */
	public void setSerializedSending(boolean serializedSending) {
		this.serializedSending = serializedSending;
	}

	/**
	 * Get upload configurations of the manipulator
	 * @return upload configurations for the manipulator
	 */
	public UploadConfig getUploadConfigs() {
		return uploadConfigs;
	}

	/**
	 * Get the internal configurator of the manipulator
	 * @return internal configurator of the manipulator
	 */
	public Configurator getConfigurator() {
		return configurator;
	}

	/**
	 * Get aggregator id that the manipulator works with
	 * @return aggregator id that the manipulator works with
	 */
	public long getOwnerAggid() {
		return aggid;
	}

	/**
	 * Set aggregator id that the manipulator works with
	 * @param aggid id to be set 
	 */
	public void setAggid(long aggid) {
		this.aggid = aggid;
	}

	/**
	 * Get processor that processes data to be sent to server before sending
	 * @return processor that processes data to be sent to server
	 */
	public AggProcessor getPostProcessor() {
		return postProcessor;
	}

	/**
	 * Set processor that processes data to be sent to server before sending
	 * @param postProcessor processor to be set
	 */
	public void setPostProcessor(AggProcessor postProcessor) {
		this.postProcessor = postProcessor;
	}

	/**
	 * Set internal configurator of future manipulator
	 * @param configurator configurator to be set
	 */
	public void setConfigurator(Configurator configurator) {
		this.configurator = configurator;
	}

	/**
	 * Set upload configurations of future manipulator
	 * @param uploadConfigs configurations to be set
	 */
	public void setUploadConfigs(UploadConfig uploadConfigs) {
		this.uploadConfigs = uploadConfigs;
	}

	
	/**
	 * Get output format that collected units will be serialized to before sending
	 * Possible values supported out of the box: RAW, JSON 
	 * @return output format that collected units will be serialized to before sending
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Set output format that collected units will be serialized to before sending
	 * Possible values supported out of the box: RAW, JSON
	 * @param format format that collected units will be serialized to before sending
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	
	/**
	 * Check if clean mode is turned on. 
	 * When clean mode is turned on all units are immediately erased from the database after sending to the server.
	 * @return flag indicating whether all units will be immediately erased from the database after sending to the server
	 */
	public boolean isCleanMode() {
		return cleanMode;
	}

	/**
	 * Set clean mode turned on/off. 
	 * When clean mode is turned on all units are immediately erased from the database after sending to the server.
	 * @param cleanMode flag indicating whether all units will be immediately erased from the database after sending to the server
	 */
	public void setCleanMode(boolean cleanMode) {
		this.cleanMode = cleanMode;
	}

}
