package com.ruvla.plsos.agg.prototypes.android.plsosagg;

import java.util.HashSet;

import com.ruvla.plsos.agg.model.aggctrl.Aggregator;
import com.ruvla.plsos.agg.model.aggctrl.Detector;
import com.ruvla.plsos.agg.model.aggctrl.EdgeDevice;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

public class AndroidDetector extends Detector {

	private BluetoothAdapter adapter;

	public AndroidDetector(Aggregator owner) {
		super(owner);
		// Log.d(AndroidConstants.LOG_TAG, "#AndroidCollector: Getting BL
		// adapter");
		adapter = BluetoothAdapter.getDefaultAdapter();
	}

	@Override
	public void startDetection() {
		// Log.d(AndroidConstants.LOG_TAG, "#AndroidCollector: Starting BL
		// detection...");
		turnedOn = true;
	}

	@Override
	protected HashSet<EdgeDevice> detect() {
		// ASYNC IMPLEMENTATION
		return null;
	}

	@Override
	public void stopDetection() {
		// Log.d(AndroidConstants.LOG_TAG, "#AndroidCollector: cancelling BL
		// discovery...");
		adapter.cancelDiscovery();
	}

	@Override
	public void run() {
		if (turnedOn && !Thread.currentThread().isInterrupted()) {
			adapter.cancelDiscovery();
			// Log.d(AndroidConstants.LOG_TAG, "#AndroidCollector:
			// discovering...");
			adapter.startDiscovery();
		} else {
			turnedOn = false; // just to be calm
		}
	}
}
