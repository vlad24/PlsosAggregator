package com.ruvla.plsos.agg.model.events;

import com.ruvla.plsos.agg.model.PlsosConstants;

public class AggEventCollectStop extends AggEvent {
	public long edid;

	public AggEventCollectStop(long senderId, long edid2) {
		super(PlsosConstants.Event_CollectStop, senderId, " collection started from " + edid2);
		this.edid = edid2;
	}

}
