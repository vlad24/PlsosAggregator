package com.ruvla.plsos.agg.model.events;

import com.ruvla.plsos.agg.model.PlsosConstants;

public class AggEventAggStart extends AggEvent {

	public AggEventAggStart(long senderId) {
		super(PlsosConstants.Event_AggStart, senderId, "Aggregation has started");
	}

}
