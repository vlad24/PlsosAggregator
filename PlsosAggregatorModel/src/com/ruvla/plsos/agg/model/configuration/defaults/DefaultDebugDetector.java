package com.ruvla.plsos.agg.model.configuration.defaults;

import java.util.HashSet;
import java.util.Random;

import com.ruvla.plsos.agg.model.aggctrl.Aggregator;
import com.ruvla.plsos.agg.model.aggctrl.Detector;
import com.ruvla.plsos.agg.model.aggctrl.EdgeDevice;

//import android.util.Log;

public class DefaultDebugDetector extends Detector {

	public DefaultDebugDetector(Aggregator owner) {
		super(owner);
	}

	@Override
	protected HashSet<EdgeDevice> detect() {
		HashSet<EdgeDevice> devicesDetectedNow = new HashSet<EdgeDevice>();
		Random r = new Random();
		devicesDetectedNow.add(new EdgeDevice(1 + r.nextInt(50000)));
		return devicesDetectedNow;
	}
}
