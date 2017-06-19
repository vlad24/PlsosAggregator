package com.ruvla.plsos.agg.prototypes.android.plsosagg;

import com.ruvla.plsos.agg.model.aggctrl.EdgeDevice;

import android.bluetooth.BluetoothDevice;

public class AndroidEdgeDevice extends EdgeDevice {

	private BluetoothDevice blDevice;

	public AndroidEdgeDevice(long id, BluetoothDevice device) {
		super(id);
		setBlDevice(device);
	}

	public BluetoothDevice getBlDevice() {
		return blDevice;
	}

	public void setBlDevice(BluetoothDevice blDevice) {
		this.blDevice = blDevice;
	}

}
