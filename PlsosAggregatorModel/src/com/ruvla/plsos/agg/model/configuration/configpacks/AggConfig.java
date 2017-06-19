package com.ruvla.plsos.agg.model.configuration.configpacks;

import java.io.Serializable;

import com.ruvla.plsos.agg.model.PlsosException;
import com.ruvla.plsos.agg.model.configuration.ConfigurationProvider;
import com.ruvla.plsos.agg.model.configuration.Configurator;
import com.ruvla.plsos.agg.model.configuration.defaults.DefaultConfigurationProvider;
import com.ruvla.plsos.agg.model.configuration.defaults.DefaultPlsosConfigurator;

/**
 * Parameters that are mandatory for an Aggregator.
 * 
 * @author Vladislav
 */
@SuppressWarnings("serial")
public class AggConfig implements Serializable {

	private int threadCount;
	private Configurator configurator;
	private DetectorConfig detectorConfig;
	private SaverConfig saverConfig;

	/**
	 * @author Vladislav
	 * Builder for AggConfig class
	 */
	public static class ParamsBuilder implements Serializable {
		private int threadCount;
		private DetectorConfig detectorConfig;
		private SaverConfig saverConfig;
		private Configurator factory;

		/**
		 * Creates a param builder, where all 
		 */
		public ParamsBuilder() {
			ConfigurationProvider defaults = new DefaultConfigurationProvider();
			threadCount = defaults.getDefaultThreadCount();
			detectorConfig = new DetectorConfig(defaults);
			saverConfig = new SaverConfig(defaults);
			factory = new DefaultPlsosConfigurator();
		}

		public ParamsBuilder setAllConfigurations(Configurator configurator){
			return this.setConfiguration(configurator.instantiateConfigProvider()).setConfigurator(configurator);
		}
		
		public ParamsBuilder setConfiguration(ConfigurationProvider provider){
			return this.setThreadCount(provider.getDefaultThreadCount())
				.setDetectorConfig(new DetectorConfig(provider))
				.setSaverConfig(new SaverConfig(provider));
		}
		
		/**
		 * Set thread count 
		 * @param amount of future parallel collector threads
		 * @return updated builder
		 */
		public ParamsBuilder setThreadCount(int amount) {
			this.threadCount = amount;
			return this;
		}

		
		/**
		 * Set the configuration for future detector of the aggregator
		 * @param config configuration of the detector that will be a part of the aggregator
		 * @return updated builder
		 */
		public ParamsBuilder setDetectorConfig(DetectorConfig config) {
			this.detectorConfig = config;
			return this;
		}
		/**
		 * Set the configuration for future saver of the aggregator
		 * @param config configuration of the saver that will be a part of the aggregator
		 * @return updated builder
		 */
		public ParamsBuilder setSaverConfig(SaverConfig config) {
			this.saverConfig = config;
			return this;
		}

		/**
		 * Set configurator for future aggregator
		 * @param configurator configurator that will build necessary parts of future aggregator 
		 * @return updated builder
		 */
		public ParamsBuilder setConfigurator(Configurator configurator) {
			this.factory = configurator;
			return this;
		}

		private boolean validateParams() {
			return (threadCount >= 1 && factory != null && saverConfig != null && detectorConfig != null);
		}

		/**
		 * Build aggregator parameters to build it afterwards
		 * @return ready aggregator configuration that can be used in it's constructor
		 * @throws PlsosException when thread count is less than 1 and no configurator has been supplied
		 */
		public AggConfig toAggConfig() throws PlsosException {
			if (validateParams()) {
				return new AggConfig(this.threadCount, this.detectorConfig, this.saverConfig, this.factory);
			} else {
				throw new PlsosException("Invalid parameters");
			}
		}

	}

	private AggConfig(int threadCount, DetectorConfig detectorConfig, SaverConfig saverConfig,
			Configurator configurator) {
		super();
		this.threadCount = threadCount;
		this.setDetectorConfig(detectorConfig);
		this.setSaverConfig(saverConfig);
		this.configurator = configurator;
	}

	/**
	 * Get thread count of collector manager
	 * @return thread count of collector manager
	 */
	public int getThreadCount() {
		return threadCount;
	}

	/**
	 * Set thread count of collector manager
	 * @param collectionThreadsAmount amount of parallel collector threads
	 */
	public void setThreadCount(int collectionThreadsAmount) {
		this.threadCount = collectionThreadsAmount;
	}

	/**
	 * Get detector configuration of the aggregator configuration
	 * @return detector configuration to be set
	 */
	public DetectorConfig getDetectorConfig() {
		return detectorConfig;
	}

	/**
	 * Set detector configuration of the aggregator configuration
	 * @param detectorConfig configuration to be set
	 */
	public void setDetectorConfig(DetectorConfig detectorConfig) {
		this.detectorConfig = detectorConfig;
	}

	
	/**
	 * Get configuration for saver of the aggregator
	 * @return configuration for saver of the aggregator
	 */
	public SaverConfig getSaverConfig() {
		return saverConfig;
	}
	
	/**
	 * Set configuration for saver of the aggregator
	 * @param saverConfig saver configuration to be set
	 */
	public void setSaverConfig(SaverConfig saverConfig) {
		this.saverConfig = saverConfig;
	}

	/**
	 * Get internal configurator of the aggregator
	 * @return internal configurator of the aggregator
	 */
	public Configurator getConfigurator() {
		return configurator;
	}

	/**
	 * Set internal configurator of the aggregator
	 * @param configurator configurator to be set
	 */
	public void setConfigurator(Configurator configurator) {
		this.configurator = configurator;
	}


}