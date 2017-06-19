package com.ruvla.plsos.agg.model.dataview;

public class MarkedPayloadBundle {

	private long edid;

	public long getEdid() {
		return edid;
	}

	public void setEdid(long edid) {
		this.edid = edid;
	}

	public long getAggid() {
		return aggid;
	}

	public void setAggid(long aggid) {
		this.aggid = aggid;
	}

	public PayloadBundle getBundle() {
		return bundle;
	}

	public void setBundle(PayloadBundle bundle) {
		this.bundle = bundle;
	}

	private long aggid;
	private PayloadBundle bundle;

	public MarkedPayloadBundle(long aggid, long edid, PayloadBundle unitBundle) {
		this.aggid = aggid;
		this.edid = edid;
		this.bundle = unitBundle;
	}

}
