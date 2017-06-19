package com.ruvla.plsos.agg.model.events;

/**
 * @author Vladislav
 * 
 * Listener that will be notified by an Aggregation master about detection of a
 * new device, collection start, collection stop, aggregation start, aggregation
 * stop
 */
public interface AggListener {
	/**
	 * Actions performed when a new device is detected
	 * @param event detection event
	 */
	void onDetect(AggEventDetect event);

	/**
	 * Actions performed when collection from a device is started
	 * @param event collection start event
	 */
	void onCollectStart(AggEventCollectStart event);

	/**
	 * Actions performed when collection from a device is stopped
	 * @param event collection stop event
	 */
	void onCollectStop(AggEventCollectStop event);

	/**
	 * Actions performed when aggregation is started by an aggregator in general
	 * @param event aggregation start event
	 */
	void onAggStart(AggEventAggStart event);

	/**
	 * Actions performed when aggregation is stopped by an aggregator in general
	 * @param event aggregation stop event
	 */
	void onAggStop(AggEventAggStop event);

	/**
	 * Actions performed when aggregation faced some problem
	 * @param event event containing information about occurred error 
	 */
	void onErrorEvent(AggEvent event);
}
