package com.ruvla.plsos.agg.model.configuration.configpacks;

import com.ruvla.plsos.agg.model.logging.PlsosLogger;

public class AggManagerConfig {
	private PlsosLogger logger;
	private int aggCapacity;
	public AggManagerConfig(PlsosLogger logger, int aggCapacity) {
		this.logger = logger;
		this.aggCapacity = aggCapacity;
	}
	/**
	 * @return the logger
	 */
	public PlsosLogger getLogger() {
		return logger;
	}
	/**
	 * @param logger the logger to set
	 */
	public void setLogger(PlsosLogger logger) {
		this.logger = logger;
	}
	/**
	 * @return the aggCapacity
	 */
	public int getAggCapacity() {
		return aggCapacity;
	}
	/**
	 * @param aggCapacity the aggCapacity to set
	 */
	public void setAggCapacity(int aggCapacity) {
		this.aggCapacity = aggCapacity;
	}

}
