package com.ruvla.plsos.agg.model.configuration.defaults;

import com.ruvla.plsos.agg.model.PlsosConstants;
import com.ruvla.plsos.agg.model.events.AggEvent;
import com.ruvla.plsos.agg.model.events.AggEventAggStart;
import com.ruvla.plsos.agg.model.events.AggEventAggStop;
import com.ruvla.plsos.agg.model.events.AggEventCollectStart;
import com.ruvla.plsos.agg.model.events.AggEventCollectStop;
import com.ruvla.plsos.agg.model.events.AggEventDetect;
import com.ruvla.plsos.agg.model.events.AggListener;
import com.ruvla.plsos.agg.model.logging.PlsosLogger;
import com.ruvla.plsos.agg.model.logging.PlsosMessage;

public class DefaultLoggingAggListener implements AggListener {
	private PlsosLogger out;

	public DefaultLoggingAggListener(PlsosLogger out) {
		this.out = out;
	}

	public void onDetect(AggEventDetect event) {
		if (out != null) {
			PlsosMessage m = new PlsosMessage(event.time,
					"A" + event.aggregatorId,
					PlsosConstants.MESSAGE_CATEGORY_Info,
					PlsosConstants.MESSAGE_SUBJECT_Detection,
					"device_" + event.edid + " detected"
			);
			out.logMessage(m);
		}
	}

	@Override
	public void onCollectStart(final AggEventCollectStart event) {
		if (out != null) {
			PlsosMessage m = new PlsosMessage(event.time, "A"+event.aggregatorId, PlsosConstants.MESSAGE_CATEGORY_Info,
					PlsosConstants.MESSAGE_SUBJECT_CollectionStart,
					"collection from device_" + event.edid + " has started");
			out.logMessage(m);
		}
	}

	@Override
	public void onCollectStop(AggEventCollectStop event) {
		if (out != null) {
			PlsosMessage m = new PlsosMessage(event.time, "A"+event.aggregatorId, PlsosConstants.MESSAGE_CATEGORY_Info,
					PlsosConstants.MESSAGE_SUBJECT_CollectionStop, "collection from " + event.edid + " stopped");
			out.logMessage(m);
		}
	}

	@Override
	public void onAggStart(AggEventAggStart event) {
		if (out != null) {
			PlsosMessage m = new PlsosMessage(event.time, "A"+event.aggregatorId, PlsosConstants.MESSAGE_CATEGORY_Info,
					PlsosConstants.MESSAGE_SUBJECT_AggStart, "aggregtion started");
			out.logMessage(m);
		}
	}

	@Override
	public void onAggStop(AggEventAggStop event) {
		if (out != null) {
			PlsosMessage m = new PlsosMessage(event.time, "A"+event.aggregatorId, PlsosConstants.MESSAGE_CATEGORY_Info,
					PlsosConstants.MESSAGE_SUBJECT_AggStop,
					"aggregtion stopped. Units received:" + event.getDataUnits().size());
			out.logMessage(m);
		}
	}

	@Override
	public void onErrorEvent(final AggEvent event) {
		if (out != null) {
			PlsosMessage m = new PlsosMessage(event.time, "A"+event.aggregatorId, PlsosConstants.MESSAGE_CATEGORY_Error,
					PlsosConstants.MESSAGE_SUBJECT_Error, "XXX" + event.comment);
			out.logMessage(m);
		}
	}

}
