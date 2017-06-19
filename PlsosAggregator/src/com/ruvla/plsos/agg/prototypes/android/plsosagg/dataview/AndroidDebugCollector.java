package com.ruvla.plsos.agg.prototypes.android.plsosagg.dataview;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.ruvla.plsos.agg.model.PlsosConstants;
import com.ruvla.plsos.agg.model.aggctrl.Aggregator;
import com.ruvla.plsos.agg.model.aggctrl.Collector;
import com.ruvla.plsos.agg.model.aggctrl.EdgeDevice;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;
import com.ruvla.plsos.agg.model.dataview.PayloadBundle;

public class AndroidDebugCollector extends Collector{
	public AndroidDebugCollector(Aggregator owner, long edid, EdgeDevice targetDevice) {
		super(owner, edid, targetDevice);
	}

	@Override
	protected void sendConfirmation(EdgeDevice device, LinkedList<AggDataUnit> tranBuffer) {
		// Log.d(AndroidConstants.LOG_TAG, "~~~~~~~~sending confirmation to " +
		// device);
	}

	@Override
	protected PayloadBundle receivePayloadBundle(EdgeDevice device) {
		// Log.d(AndroidConstants.LOG_TAG, "~~~~~~~~receiving bundle from " +
		// device);
		int decider = new Random().nextInt(30);
		int type = (decider == 1) ? PlsosConstants.UNIT_TYPE_FINISH : PlsosConstants.UNIT_TYPE_Common;
		ArrayList<AndroidAggDataPayload> contents = new ArrayList<AndroidAggDataPayload>();
		if (type != PlsosConstants.UNIT_TYPE_FINISH) {
			for (int i = 0; i < 5; i++) {
				String data = "data^" + device.getEdid() + "^" + (System.currentTimeMillis() % 1000);
				contents.add(new AndroidAggDataPayload(data));
			}
		}
		// Log.d(AndroidConstants.LOG_TAG, "~~~~~~~~received!");
		return new AndroidPayloadBundle(contents);
	}

	@Override
	protected String performHandshake(EdgeDevice device) {
		// Log.d(AndroidConstants.LOG_TAG, "~~~~~~~~introducing to " + device);
		return device.toString();
	}

	@Override
	protected boolean establishConnection(EdgeDevice device) {
		// Log.d(AndroidConstants.LOG_TAG, "~~~~~~~~establishing connection to "
		// + device);
		return true;
	}

	@Override
	protected void finish(EdgeDevice device) {
		// Log.d(AndroidConstants.LOG_TAG, "~~~~~~~~finishing with " + device);
	}
}
