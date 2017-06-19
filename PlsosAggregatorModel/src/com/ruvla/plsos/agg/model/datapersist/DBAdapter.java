package com.ruvla.plsos.agg.model.datapersist;

import java.util.HashMap;
import java.util.Queue;

import com.ruvla.plsos.agg.model.aggctrl.Aggregator;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;

/**
 * 
 * Class that controls interaction with a database where 
 * all the information collected by multiple {@link Aggregator} instances will be stored using {@link Saver}
 * 
 * @author Vladislav
 *
 * 
 */
public interface DBAdapter {
	/**
	 * Initialize database using the provided configuration
	 * @param config configuration for database
	 */
	void init(DBConfig config);

	/**
	 * Insert a single collected unit into the database
	 * @param unit unit to be inserted
	 */
	void insertAggUnitIntoDB(AggDataUnit unit);

	/**
	 * Select units collected by a single aggregator.
	 * It may be not needed to select all units each time so a requestTime can be useful for sequential fetching of some portions of unsent data units.  
	 * @param aggid id of an aggregator which units should be selected. If PlsosConstants.GENERIC_ID is passed then units matching the criteria of all aggregators will be processed.
	 * @param requestTime time of request that can be used, for example, for fetching data with saveTime not more than given time 
	 * @param extras hash map for other parameters for queries that cannot be passed through above parameters
	 * @return queue of selected units of particular aggregator, matching conditions set via other parameters
	 */
	Queue<? extends AggDataUnit> selectAllAggDataUnits(long aggid, long requestTime, HashMap<String, String> extras);


	/**
	 * Mark some units of a particular aggregator as sent given a request time of marking. 
	 * @param aggid id of an aggregator which units are processed. If PlsosConstants.GENERIC_ID is passed then units matching the criteria of all aggregators will be processed.
	 * @param requestTime time that can be used for marking
	 */
	void markSent(long aggid, long requestTime, HashMap<String, String> extras);



	/**
	 * Delete all data units of a particular aggregator using requestTime and extra parameters. 
	 * @param aggid id of aggregator which units should be deleted. If PlsosConstants.GENERIC_ID is passed then units matching the criteria of all aggregators will be processed.
	 * @param requestTime
	 * @param extras
	 */
	void deleteAllDataUnits(long aggid, long requestTime, HashMap<String, String> extras);


	/**
	 * 
	 */
	void beginTransaction();

	/**
	 * 
	 */
	void commitTransaction();

	/**
	 * 
	 */
	void rollbackTransaction();

	/**
	 * 
	 */
	void endTransaction();
}
