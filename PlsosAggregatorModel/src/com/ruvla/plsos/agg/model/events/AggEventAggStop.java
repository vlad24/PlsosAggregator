package com.ruvla.plsos.agg.model.events;

import java.util.Queue;

import com.ruvla.plsos.agg.model.PlsosConstants;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;

public class AggEventAggStop extends AggEvent {

	private Queue<AggDataUnit> dataUnits;

	public AggEventAggStop(long senderId) {
		super(PlsosConstants.Event_AggStop, senderId, "Aggregation has stopped");
	}

	public AggEventAggStop(long senderId, Queue<AggDataUnit> dataUnits) {
		super(PlsosConstants.Event_AggStop, senderId, "Aggregation has stopped");
		this.dataUnits = dataUnits;
	}

	public Queue<AggDataUnit> getDataUnits() {
		return dataUnits;
	}

}
