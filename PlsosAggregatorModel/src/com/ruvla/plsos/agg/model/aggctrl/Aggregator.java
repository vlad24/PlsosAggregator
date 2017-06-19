package com.ruvla.plsos.agg.model.aggctrl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ruvla.plsos.agg.model.PlsosConstants;
import com.ruvla.plsos.agg.model.configuration.Configurator;
import com.ruvla.plsos.agg.model.configuration.configpacks.AggConfig;
import com.ruvla.plsos.agg.model.configuration.configpacks.DetectorConfig;
import com.ruvla.plsos.agg.model.configuration.configpacks.SaverConfig;
import com.ruvla.plsos.agg.model.datapersist.Saver;
import com.ruvla.plsos.agg.model.dataview.AggDataPayload;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;
import com.ruvla.plsos.agg.model.dataview.MarkedPayloadBundle;
import com.ruvla.plsos.agg.model.events.AggEvent;
import com.ruvla.plsos.agg.model.events.AggEventAggStart;
import com.ruvla.plsos.agg.model.events.AggEventAggStop;
import com.ruvla.plsos.agg.model.events.AggEventCollectStart;
import com.ruvla.plsos.agg.model.events.AggEventCollectStop;
import com.ruvla.plsos.agg.model.events.AggEventDetect;
import com.ruvla.plsos.agg.model.events.AggListener;
import com.ruvla.plsos.agg.model.logging.PlsosLogger;

/**
 *
 *
 * @author Vladislav
 * @version 1.0
 */
public class Aggregator implements Runnable {


	/**
	 * Builds a new aggregator.
	 * @param aggid id that will be an identifier of the aggregator
	 * @param aggConfig configuration for the aggregation
	 * @param logger, needed for listeners of the aggregator
	 */
	public Aggregator(long aggid, AggConfig aggConfig, PlsosLogger logger) {
		this.working = false;
		this.aggid = aggid;
		this.aggConfig = aggConfig;
		this.threadCount = aggConfig.getThreadCount();
		this.configurator = aggConfig.getConfigurator();
		this.preFilter = configurator.instantiatePreFilter();
		this.processor = configurator.instantiatePreProcessor();
		this.listener = configurator.instantiateAggListener(this, logger);
		this.dataUnits = new ConcurrentLinkedQueue<AggDataUnit>();
		this.edgeDevices = new ConcurrentLinkedQueue<EdgeDevice>();
		this.detectorConfig = aggConfig.getDetectorConfig();
		this.saverConfig = aggConfig.getSaverConfig();
		this.deviceObserver = new DeviceObserver(this.preFilter);
		this.detector = configurator.instantiateDetector(this, detectorConfig);
		this.collectorManager = new CollectorManager(this, threadCount, configurator);
		this.saver = new Saver(logger, saverConfig.getDbConfig(), this, configurator.instantiatePostFilter(),
				configurator.instantiateDBAdapter(), saverConfig.getPortionSize());
		this.collectorManagerExecutor = Executors.newSingleThreadExecutor();
		this.detectorSheduler = detectorConfig.withRefreshPeriod() ? Executors.newSingleThreadScheduledExecutor()
				: null;
		this.saverSheduler = saverConfig.isWithRefreshPeriod() ? Executors.newSingleThreadScheduledExecutor() : null;
	}

	private long aggid;
	private CollectorManager collectorManager;
	private ExecutorService collectorManagerExecutor;
	private Queue<AggDataUnit> dataUnits;
	private Detector detector;
	private DetectorConfig detectorConfig;
	private ScheduledExecutorService detectorSheduler;
	private DeviceObserver deviceObserver;
	private Queue<EdgeDevice> edgeDevices;
	private Configurator configurator;
	private AggListener listener;
	private AggFilter preFilter;
	private AggProcessor processor;
	private Saver saver;
	private SaverConfig saverConfig;
	private ScheduledExecutorService saverSheduler;
	private int threadCount;
	private boolean withSaverUnitThreshold;
	private boolean working;
	private AggConfig aggConfig;

	@Override
	public String toString() {
		return "AGG" + aggid;
	}

	@Override
	public int hashCode() {
		return (int) aggid;
	}

	/**
	 * Get and not delete queue of data units that has been stored by the moment of invocation
	 * @return queue of data that has been stored by the moment of invocation
	 */
	public Queue<AggDataUnit> getAccumulatedData() {
		return dataUnits;
	}

	/**
	 * Get devices that yet have not been processed completely by the aggregator
	 * @return queue of devices that have not been processed completely yet by the aggregator
	 */
	public Queue<EdgeDevice> getEdgeDevices() {
		return edgeDevices;
	}


	/**
	 * Get some portion of data units that have been processed by post processor of the aggregator
	 * @param maxAllowed size of portion. Default is Integer.MAX_SIZE
	 * @return not more than maxAllowed units that have been processed by post processor
	 */
	public Queue<? extends AggDataUnit> getReadyData(int maxAllowed) {
		Queue<AggDataUnit> readyBuffer = new LinkedList<AggDataUnit>();
		int got = 0;
		while (!dataUnits.isEmpty() && got < maxAllowed) {
			readyBuffer.add(dataUnits.poll());
		}
		Queue<? extends AggDataUnit> readyUnits = processor.process(readyBuffer);
		return readyUnits;
	}

	/**
	 * Stores just detected device in devices queue. Thread safe.
	 * @param device just detected device to be stored in a queue of new devices
	 */
	public void pushEdgeDevice(EdgeDevice device) {
		// thread safe!
		if (device != null && deviceObserver.checkDevice(device)) {
			generateEvent(new AggEventDetect(this.aggid, device));
			edgeDevices.add(device);
		}
	}


	/**
	 * Stores just detected devices in devices queue. Thread safe.
	 * @param newDevices - set of just detected devices to be stored in a queue of new devices
	 */
	public void pushEdgeDevices(HashSet<EdgeDevice> newDevices) {
		if (newDevices != null) {
			for (EdgeDevice device : newDevices) {
				pushEdgeDevice(device);
			}
		}
	}

	/**
	 * Stores received unit in aggregator's memory.
	 * @param unit unit that will be stored in data queue
	 */
	public void pushDataUnit(AggDataUnit unit) {
		// thread safe!
		if (preFilter.match(unit)) {
			dataUnits.offer(unit);
		}
	}

	/**
	 * Stores received bundles in aggregator's memory.
	 * @param markedBundle bundle of payloads for which senders are known
	 */
	public void pushMarkedBundle(MarkedPayloadBundle markedBundle) {
		for (AggDataPayload payload : markedBundle.getBundle().getPayloads()) {
			pushDataUnit(new AggDataUnit(System.currentTimeMillis(), markedBundle.getAggid(), markedBundle.getEdid(),
					payload));
		}
		if (withSaverUnitThreshold && (dataUnits.size() > saverConfig.getUnitThreshold())) {
			saver.saveUnits();
		}
	}

	/**
	 * Launched an aggregation process.
	 * Detector, CollectorManager, Saver are started.
	 */
	public void launchAggregation(){
		if (!working && collectorManager != null && collectorManagerExecutor != null) {
			generateEvent(new AggEventAggStart(this.aggid));
			working = true;
			launchDetector();
			launchCollectorManager();
			launchSaver();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		launchAggregation();
	}


	/**
	 * Immediately shutdown all collectors and detectors
	 */
	public void stopAggregation() {
		if (working) {
			this.working = false;
			detector.stopDetection();
			if (detectorSheduler != null) {
				detectorSheduler.shutdownNow();
			}
			if (collectorManagerExecutor != null && this.collectorManager != null){
				collectorManager.stopCollecting();
				collectorManagerExecutor.shutdownNow();
			}
			saver.stopSaving();
			if (saverSheduler != null) {
				saverSheduler.shutdownNow();
			}
			generateEvent(new AggEventAggStop(this.aggid, this.dataUnits));
		}

	}


	/**
	 * Generates the given event and makes all listeners react to it
	 * @param event event to be generated
	 */
	public synchronized void generateEvent(AggEvent event) {
		if (listener != null) {
			switch (event.code) {
			case (PlsosConstants.Event_Detect): {
				listener.onDetect((AggEventDetect) event);
				break;
			}
			case (PlsosConstants.Event_CollectStart): {
				listener.onCollectStart((AggEventCollectStart) event);
				break;
			}
			case (PlsosConstants.Event_CollectStop): {
				listener.onCollectStop((AggEventCollectStop) event);
				break;
			}
			case (PlsosConstants.Event_AggStart): {
				listener.onAggStart((AggEventAggStart) event);
				break;
			}
			case (PlsosConstants.Event_AggStop): {
				listener.onAggStop((AggEventAggStop) event);
				break;
			}
			case (PlsosConstants.Event_Error): {
				listener.onErrorEvent(event);
				break;
			}
			}
		}
	}


	/**
	 * Launches internal collector manager, that regulates collection process.
	 */
	protected void launchCollectorManager() {
		this.collectorManagerExecutor.submit(collectorManager);
	}

	/**
	 * Launches internal detector, that regulates detection process.
	 */
	protected void launchDetector() {
		detector.startDetection();
		if (detectorConfig.withRefreshPeriod()) {
			this.detectorSheduler.scheduleAtFixedRate(detector, 1, detectorConfig.getDetectorRefresh(),	TimeUnit.SECONDS);
		}
	}

	/**
	 * Launches internal saver, that regulates persisting process.
	 */
	protected void launchSaver() {
		if (saverConfig.isWithRefreshPeriod()) {
			this.saverSheduler.scheduleAtFixedRate(saver, 1, saverConfig.getRefreshPeriod(), TimeUnit.SECONDS);
		}
		this.withSaverUnitThreshold = saverConfig.isWithUnitThreshold();
	}

	/**
	 * Get last detected edge device.
	 * @return last detected edge device
	 */
	protected EdgeDevice popEdgeDevice() {
		// thread safe!
		return edgeDevices.poll();
	}

	/**
	 * Get configuration with which the aggregator was built.
	 * @return the initial configuration of the aggregator.
	 */
	public AggConfig getInitialConfiguration() {
		return this.aggConfig;
	}

	/**
	 * @return the configurator which built the the aggregator
	 */
	public Configurator getInitialConfigurator() {
		return configurator;
	}


	/**
	 * @return the aggregator's unique id
	 */
	public long getAggid() {
		return aggid;
	}


	/**
	 * @return the deviceObserver
	 */
	public DeviceObserver getDeviceObserver() {
		return deviceObserver;
	}

	/**
	 * @param deviceObserver the deviceObserver to set
	 */
	public void setDeviceObserver(DeviceObserver deviceObserver) {
		this.deviceObserver = deviceObserver;
	}


	/**
	 * @return the listener
	 */
	public AggListener getListener() {
		return listener;
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(AggListener listener) {
		this.listener = listener;
	}

	/**
	 * @return the preFilter
	 */
	public AggFilter getPreFilter() {
		return preFilter;
	}

	/**
	 * @param preFilter the preFilter to set
	 */
	public void setPreFilter(AggFilter preFilter) {
		this.preFilter = preFilter;
	}

	/**
	 * @return the processor
	 */
	public AggProcessor getProcessor() {
		return processor;
	}

	/**
	 * @param processor the processor to set
	 */
	public void setProcessor(AggProcessor processor) {
		this.processor = processor;
	}


	/**
	 * @return whether this aggregator is in working state
	 */
	public boolean isWorking() {
		return working;
	}

}
