package com.ruvla.plsos.agg.model.aggctrl;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ruvla.plsos.agg.model.configuration.ConfigurationProvider;
import com.ruvla.plsos.agg.model.configuration.Configurator;
import com.ruvla.plsos.agg.model.configuration.configpacks.AggConfig;
import com.ruvla.plsos.agg.model.configuration.defaults.DefaultPlsosLogger;
import com.ruvla.plsos.agg.model.logging.PlsosLogger;

/**
 * @author Vladislav
 *
 */
public class AggregationManager {

	private static boolean inited = false;
	private static AggregationManager instance = null;

	/**
	 * Build a new AggregationManager instance
	 * @param provider - provider of configurations needed to construct a new manager instance(e.g. AggregationManagerCapacity)
	 * @param logger - logger to set for all aggregators held by this manager.
	 * <p>
	 * Note: null logger is not allowed. If null is given {@link DefaultPlsosLogger} is set.
	 * </p>
	 */
	public static void init(ConfigurationProvider provider, PlsosLogger logger){
		instance = new AggregationManager(provider.getAggregationManagerCapacity());
		if (logger != null) {
			instance.commonLogger = logger;
			instance.commonLogger.openLog();
		} else {
			logger = new DefaultPlsosLogger();
		}
		inited = true;
	}

	public static void init(Configurator configurator){
		instance = new AggregationManager(configurator.instantiateConfigProvider().getAggregationManagerCapacity());
		PlsosLogger logger = configurator.instantiateLogger();
		if (logger != null) {
			instance.commonLogger = logger;
			instance.commonLogger.openLog();
		} else {
			logger = new DefaultPlsosLogger();
		}
		inited = true;
	}

	/**
	 * Get a globally single instance of AggregatorManager
	 * @return a globally single instance of AggregatorManager
	 */
	public static AggregationManager getInstance() {
		if (inited) {
			return instance;
		}else{
			return null;
		}
	}

	private PlsosLogger commonLogger;
	private HashMap<Long, Aggregator> aggregators;
	private ExecutorService aggPlatform;
	private boolean turnedOn;

	protected AggregationManager(int capacity) {
		aggregators = new HashMap<Long, Aggregator>(capacity);
		aggPlatform = Executors.newFixedThreadPool(capacity);
		turnedOn = true;
	}

	/**
	 * Get particular aggregator.
	 * @param id  id of aggregator to be got
	 * @return aggregator with the specified id
	 */
	public Aggregator getAggregator(long id) {
		return aggregators.get(id);
	}

	/**
	 * Add particular aggregator.
	 * @param id  id of aggregator to be stopped
	 * @param config  configuration for new aggregator
	 */
	public void addAggregator(long id, AggConfig config) {
		aggregators.put(id, new Aggregator(id, config, commonLogger));
	}

	/**
	 * Remove particular aggregator
	 * @param id  id of aggregator to be removed
	 */
	public void removeAggregator(long id) {
		aggregators.remove(id);
	}

	/**
	 * Launch particular aggregator.
	 * @param id  id of aggregator to be launched
	 */
	public void launchAggregator(long id) {
		if (turnedOn) {
			Aggregator agg = aggregators.get(id);
			if (agg != null) {
				aggPlatform.submit(agg);
			}
		}
	}

	/**
	 * Stop particular aggregator.
	 * @param id  id of aggregator to be stopped
	 */
	public void stopAggregator(long id) {
		if (turnedOn) {
			Aggregator agg = aggregators.get(id);
			if (agg != null) {
				agg.stopAggregation();
			}
		}
	}

	/**
	 * Launch all added aggregators
	 */
	public void launchAll() {
		if (turnedOn) {
			for (long aggid : aggregators.keySet()) {
				Aggregator agg = aggregators.get(aggid);
				if (!agg.isWorking()) {
					aggPlatform.submit(agg);
				}
			}
		}
	}
	/**
	 * Stop all added aggregators
	 */
	public void stopAll() {
		if (turnedOn) {
			for (long aggid : aggregators.keySet()) {
				Aggregator agg = aggregators.get(aggid);
				if (agg.isWorking()) {
					aggregators.get(aggid).stopAggregation();
				}
			}
			aggPlatform.shutdownNow();
		}
	}




	public String getLogs() {
		return commonLogger.fetchLogData();
	}

	/**
	 * Stop all aggregators and close manager
	 */
	public void turnOff() {
		stopAll();
		turnedOn = false;
	}

	/**
	 * Turn off the manager, close log and nullify the singleton instance
	 */
	public void destroy() {
		turnOff();
		//commonLogger.closeLog();
		instance = null;
	}
}
