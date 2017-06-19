package com.ruvla.plsos.agg.model.configuration.defaults;

import java.util.LinkedList;

import com.ruvla.plsos.agg.model.aggctrl.Aggregator;
import com.ruvla.plsos.agg.model.aggctrl.Collector;
import com.ruvla.plsos.agg.model.aggctrl.EdgeDevice;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;
import com.ruvla.plsos.agg.model.dataview.PayloadBundle;


public class DefaultDebugCollector extends Collector {

	public DefaultDebugCollector(Aggregator owner, long edid, EdgeDevice targetDevice) {
		super(owner, edid, targetDevice);
	}

	@Override
	protected void sendConfirmation(EdgeDevice device, LinkedList<AggDataUnit> tranBuffer) {
	}

	@Override
	protected PayloadBundle receivePayloadBundle(EdgeDevice device) {
		return null;
	}

	@Override
	protected String performHandshake(EdgeDevice device) {
		return device.toString();
	}

	@Override
	protected boolean establishConnection(EdgeDevice device) {
		return true;
	}

	@Override
	protected void finish(EdgeDevice device) {
	}
}
