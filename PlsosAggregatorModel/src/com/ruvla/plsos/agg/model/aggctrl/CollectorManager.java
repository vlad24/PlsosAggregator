package com.ruvla.plsos.agg.model.aggctrl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import com.ruvla.plsos.agg.model.configuration.Configurator;
import com.ruvla.plsos.agg.model.events.AggEventCollectStart;

/**
 * @author Vladislav
 *
 */
public class CollectorManager implements Runnable {

	private final Aggregator owner;
	private final ExecutorService collectors;
	private final Configurator configurator;
	private final int threadCount;
	private boolean turnedOn;


	/**
	 * @param owner aggregator that holds this manager
	 * @param threadCount number of possible concurrent collector processes
	 * @param configurator configurator tat will build and configure all the necessary parts of the manager
	 */
	public CollectorManager(Aggregator owner, int threadCount, Configurator configurator) {
		this.owner = owner;
		this.threadCount = threadCount;
		this.collectors = Executors.newFixedThreadPool(this.threadCount);
		this.configurator = configurator;
		this.turnedOn = true;
	}

	private boolean needToWork() {
		return turnedOn && !Thread.currentThread().isInterrupted();
	}


	/**
	 * While it is possible try to submit a new collector for an unprocessed device.
	 * Invoked by run method.
	 */
	public void manageCollectors(){
		while (needToWork()) {
			EdgeDevice targetDevice = owner.popEdgeDevice();
			if (targetDevice != null) {
				owner.generateEvent(new AggEventCollectStart(owner.getAggid(), targetDevice.edid));
				try {
					Collector newCollector = configurator.instantiateCollector(owner, targetDevice.edid, targetDevice);
					collectors.submit(newCollector);
				} catch (RejectedExecutionException e) {
					turnedOn = false;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		manageCollectors();
	}


	/**
	 * Shut down all the thread pools and stop managing collectors.
	 */
	public void stopCollecting() {
		turnedOn = false;
		collectors.shutdownNow();// declining submitted tasks
		try {
			// waiting for executed tasks to terminate
			collectors.awaitTermination(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
