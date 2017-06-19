package com.ruvla.plsos.agg.model.events;

import com.ruvla.plsos.agg.model.PlsosConstants;
import com.ruvla.plsos.agg.model.aggctrl.EdgeDevice;

public class AggEventDetect extends AggEvent {
	public long edid;

	public AggEventDetect(long senderId, EdgeDevice newDevice) {
		super(PlsosConstants.Event_Detect, senderId, "Device detected!");
		edid = newDevice.getEdid();
	}
}
