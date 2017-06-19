package com.ruvla.plsos.agg.model.aggctrl;

import java.util.HashSet;

/**
 * @author Vladislav
 * Class that observes new incoming devices and filters them
 */
public class DeviceObserver {
	private HashSet<EdgeDevice> detectedDevices;
	private AggFilter deviceFilter;

	/**
	 *  @param deviceFilter filter that will be applied to future devices during filtering
	 */
	public DeviceObserver(AggFilter deviceFilter) {
		this.detectedDevices = new HashSet<EdgeDevice>();
		this.deviceFilter = deviceFilter;
	}

	
	/**
	 * Checks if the device has not been detected previously and satisfy internal filter conditions.
	 * @param device device to be checked
	 * @return whether device is appropriate for further manipulations by the owner aggregator
	 */
	public boolean checkDevice(EdgeDevice device) {
		boolean exists = detectedDevices.contains(device);
		if (!exists) {
			detectedDevices.add(device);
		}
		return !exists && deviceFilter.match(device);
	}
}
