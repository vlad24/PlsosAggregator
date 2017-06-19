package com.ruvla.plsos.agg.model.aggctrl;

import java.util.HashSet;

public abstract class Detector implements Runnable {

	protected Aggregator owner;
	public boolean turnedOn;

	public Detector(Aggregator owner) {
		this.turnedOn = false;
		this.owner = owner;
	}

	public void startDetection() {
		turnedOn = true;
	}

	public void run() {
		if (turnedOn && !Thread.currentThread().isInterrupted()) {
			HashSet<EdgeDevice> newDevices = detect();
			owner.pushEdgeDevices(newDevices);
		} else {
			turnedOn = false;
		}

	}

	public void stopDetection() {
		turnedOn = false;
	}

	protected abstract HashSet<EdgeDevice> detect();

}
