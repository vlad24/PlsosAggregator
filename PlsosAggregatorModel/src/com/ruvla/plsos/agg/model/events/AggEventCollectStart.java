package com.ruvla.plsos.agg.model.events;

import com.ruvla.plsos.agg.model.PlsosConstants;

public class AggEventCollectStart extends AggEvent {

	public long edid;

	public AggEventCollectStart(long senderId, long edid2) {
		super(PlsosConstants.Event_CollectStart, senderId, " collection started from " + edid2);
		this.edid = edid2;
	}

}
