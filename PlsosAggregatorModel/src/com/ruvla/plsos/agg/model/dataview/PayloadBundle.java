package com.ruvla.plsos.agg.model.dataview;

import java.util.ArrayList;

public class PayloadBundle {
	private ArrayList<AggDataPayload> payloads;

	public ArrayList<AggDataPayload> getPayloads() {
		return payloads;
	}

	public void setPayloads(ArrayList<AggDataPayload> payloads) {
		this.payloads = payloads;
	}

	public PayloadBundle() {
		this.payloads = null;
	}

	public PayloadBundle(ArrayList<AggDataPayload> payloads) {
		this.payloads = payloads;
	}

	public boolean isFinish() {
		return (payloads == null) || (payloads.isEmpty());
	}
}
