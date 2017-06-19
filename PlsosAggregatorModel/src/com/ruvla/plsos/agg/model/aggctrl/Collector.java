package com.ruvla.plsos.agg.model.aggctrl;

import java.util.LinkedList;

import com.ruvla.plsos.agg.model.dataview.AggDataUnit;
import com.ruvla.plsos.agg.model.dataview.MarkedPayloadBundle;
import com.ruvla.plsos.agg.model.dataview.PayloadBundle;
import com.ruvla.plsos.agg.model.events.AggEventCollectStop;

public abstract class Collector implements Runnable {
	private long id;
	private boolean turnedOn;
	protected Aggregator owner;
	private EdgeDevice targetDevice;

	/**
	 * Full constructor
	 * @param owner aggregator to which this collector belongs to
	 * @param id unique identifier of the collector
	 * @param targetDevice edge device the collector will work with
	 */
	public Collector(Aggregator owner, long id, EdgeDevice targetDevice) {
		this.owner = owner;
		this.id = id;
		this.targetDevice = targetDevice;
		this.turnedOn = true;
	}

	@Override
	public String toString() {
		return "COL" + this.id;
	}

	private boolean needToWork() {
		return turnedOn && !Thread.currentThread().isInterrupted();
	}

	/**
	 * Collect all the data from the targetDevice
	 */
	protected void collect() {
		while (needToWork()) {
			PayloadBundle payloadBundle = receivePayloadBundle(targetDevice);
			if (payloadBundle.isFinish()) {
				turnedOn = false;
			}
			MarkedPayloadBundle fullBundle = new MarkedPayloadBundle(owner.getAggid(), targetDevice.edid, payloadBundle);
			owner.pushMarkedBundle(fullBundle);
		}
		owner.generateEvent(new AggEventCollectStop(owner.getAggid(), targetDevice.edid));
	}

	@Override
	public void run() {
		if (establishConnection(targetDevice)) {
			if (performHandshake(targetDevice) != null)
				collect();
		}
		finish(targetDevice);
	}

	/**
	 * Establishes a connection between this collector and target edge device
	 * @param device device with which a connection should be established
	 * @return ehether a connection was successfult established
	 */
	protected abstract boolean establishConnection(EdgeDevice device);

	/**
	 * By 'handshake' it is meant that two sides should introduce to each other sending identifiers to each other.
	 * By default it is assumed that aggregator sends its own id first, then edge device does.
	 * @param device device to communicate with
	 * @return string of got id of target device
	 */
	protected abstract String performHandshake(EdgeDevice device);

	/**
	 * Receive a bundle of payloads from the edge device
	 * @param device device to communicate with
	 * @return bundle of payloads sent by the device
	 */
	protected abstract PayloadBundle receivePayloadBundle(EdgeDevice device);

	/**
	 * After FINISH bundle has been got it is needed to send a confirmation message to an edge device to let it free its recourses.
	 * The method is needed to organize sending such a confirmation.
	 * @param device device to communicate with
	 * @param tranBuffer buffer of units that have been received and can be used to introduce some logic to include in confirmation message
	 */
	protected abstract void sendConfirmation(EdgeDevice device, LinkedList<AggDataUnit> tranBuffer);

	/**
	 * Finish communication with the device, closing all connections and other necessary things
	 * @param device device to communicate with
	 */
	protected abstract void finish(EdgeDevice device);

}