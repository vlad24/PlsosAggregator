package com.ruvla.plsos.agg.model.configuration.configpacks;

import java.io.Serializable;

import com.ruvla.plsos.agg.model.configuration.ConfigurationProvider;
import com.ruvla.plsos.agg.model.datapersist.DBConfig;

@SuppressWarnings("serial")
public class SaverConfig implements Serializable {
	private int unitThreshold;
	private int refreshPeriod;
	private DBConfig dbConfig;
	private int portionSize;

	@Override
	public String toString() {
		return "[" + unitThreshold + "," + refreshPeriod + "," + dbConfig + "]";
	}

	/**
	 * Configurator constructor
	 * @param provider configuration provider that will supply all the constants for the saver
	 */
	public SaverConfig(ConfigurationProvider provider) {
		this.unitThreshold = provider.getDefaultUnitThreshold();
		this.refreshPeriod = provider.getDefaultSaverRefresh();
		this.dbConfig = new DBConfig(provider);
		this.portionSize = provider.getDefaultPortionSize();
	}

	/**
	 * Full constructor
	 * @param unitThreshold amount of units after getting which saver will fetch this amount of units from aggregator
	 * @param refresh amount of time units(by now: seconds) after which saver will try to fetch 'portion size' of units from aggregator 
	 * @param dbConfig configuration for database adapter which saver will use to store data in a database
	 * @param portionSize amount of data that saver will take after a single request to an aggregator
	 */
	public SaverConfig(int unitThreshold, int refresh, DBConfig dbConfig, int portionSize) {
		this.unitThreshold = unitThreshold;
		this.refreshPeriod = refresh;
		this.dbConfig = dbConfig;
		this.portionSize = portionSize;
	}

	/**
	 * Check whether the future saver will have some refresh period
	 * @return whether the future saver will have some refresh period
	 */
	public boolean isWithRefreshPeriod() {
		return refreshPeriod > 0;
	}

	/**
	 * Get period after which saver will try to take 'portion size' units from an aggregator
	 * @return refresh period after which saver will try to take 'portion size' units from an aggregator 
	 */
	public int getRefreshPeriod() {
		return refreshPeriod;
	}
	
	/**
	 * Set period after which saver will try to take 'portion size' units from an aggregator
	 * @param refresh period after which saver will try to take 'portion size' units from an aggregator 
	 */
	public void setRefresh(int refresh) {
		this.refreshPeriod = refresh;
	}

	/**
	 * Get amount of units after getting which saver will fetch this amount of units from aggregator
	 * Returned value may be equal to PlsosConstants.NO_THRESHOLD meaning that there will be no thresholding behavior.
	 * @return amount of units after getting which saver will fetch this amount of units from aggregator
	 */
	public int getUnitThreshold() {
		return unitThreshold;
	}

	/**
	 * Set amount of units after getting which saver will fetch this amount of units from aggregator.
	 * Use PlsosConstants.NO_THRESHOLD to explicitly set that there will be no thresholding behavior.
	 * @param unitThreshold amount of units after getting which saver will fetch this amount of units from aggregator
	 */
	public void setUnitThreshold(int unitThreshold) {
		this.unitThreshold = unitThreshold;
	}

	/**
	 * Get configuration for database adapter which saver will use to store data in a database
	 * @return configuration for database adapter which saver will use to store data in a database
	 */
	public DBConfig getDbConfig() {
		return dbConfig;
	}

	/**
	 * Set configuration for database adapter which saver will use to store data in a database
	 * @param dbConfig configuration for database adapter which saver will use to store data in a database
	 */
	public void setDbConfig(DBConfig dbConfig) {
		this.dbConfig = dbConfig;
	}

	/**
	 * Check whether the future saver will have some unit threshold.
	 * @return whether the future saver will have some unit threshold
	 */
	public boolean isWithUnitThreshold() {
		return this.getUnitThreshold() > 0;
	}

	
	/**
	 * Get amount of data that saver will take after a single request to an aggregator
	 * @return amount of data that saver will take after a single request to an aggregator
	 */
	public int getPortionSize() {
		return portionSize;
	}

	/**
	 * Set amount of data that saver will take after a single request to an aggregator
	 * Use PlsosConstants.UNLIMITED as a maximum possible limit,
	 * @param portionSize amount of data that saver will take after a single request to an aggregator
	 */
	public void setPortionSize(int portionSize) {
		this.portionSize = portionSize;
	}

}
