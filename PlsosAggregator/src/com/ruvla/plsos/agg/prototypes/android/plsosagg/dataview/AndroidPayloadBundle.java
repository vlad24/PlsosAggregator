package com.ruvla.plsos.agg.prototypes.android.plsosagg.dataview;

import java.util.ArrayList;

import com.ruvla.plsos.agg.model.dataview.AggDataPayload;
import com.ruvla.plsos.agg.model.dataview.PayloadBundle;

public class AndroidPayloadBundle extends PayloadBundle {

	private ArrayList<AndroidAggDataPayload> payloads;

	public AndroidPayloadBundle() {
		super();
	}

	public AndroidPayloadBundle(ArrayList<AndroidAggDataPayload> contents) {
		this.payloads = contents;
	}

	@Override
	public ArrayList<AggDataPayload> getPayloads() {
		ArrayList<AggDataPayload> converted = new ArrayList<AggDataPayload>();
		for (AndroidAggDataPayload ap : this.payloads) {
			converted.add(ap);
		}
		return converted;
	}

	@Override
	public void setPayloads(ArrayList<AggDataPayload> newPayloads) {
		ArrayList<AndroidAggDataPayload> converted = new ArrayList<AndroidAggDataPayload>();
		for (AggDataPayload ap : newPayloads) {
			converted.add((AndroidAggDataPayload) ap);
		}
		this.payloads = converted;
	}

}
