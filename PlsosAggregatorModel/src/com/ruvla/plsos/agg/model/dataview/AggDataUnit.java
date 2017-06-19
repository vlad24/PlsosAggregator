package com.ruvla.plsos.agg.model.dataview;

public class AggDataUnit {

	@Override
	public String toString() {
		return "<<" + timestamp + ":" + aggid + "<-" + edid + "[" + payload + "]>>";
	}

	private long aggid;
	private long edid;
	private AggDataPayload payload;
	private long timestamp;
	private long saveTime;
	private boolean sent;

	public AggDataUnit() {
		this.setAggid(-1);
		this.setEdid(-1);
		this.setPayload(null);
		this.setTimestamp(-1);
		this.setSaveTime(-1);
		setSent(true);
	}

	public AggDataUnit(long timestamp, AggDataPayload payload) {
		this.setPayload(payload);
		this.setTimestamp(timestamp);
		this.setAggid(-1);
		this.setEdid(-1);
		this.setSaveTime(-1);
		this.setSent(false);
	}

	public AggDataUnit(long timestamp, long aggid, long edid, AggDataPayload payload) {
		this.setTimestamp(timestamp);
		this.setAggid(aggid);
		this.setEdid(edid);
		this.setPayload(payload);
		this.setSaveTime(-1);
		this.setSent(false);
	}

	public AggDataUnit(long timestamp, long saveTime, long aggid, long edid, AggDataPayload payload, boolean sent) {
		this.setAggid(aggid);
		this.setEdid(edid);
		this.setPayload(payload);
		this.setTimestamp(timestamp);
		this.setSaveTime(saveTime);
		this.setSent(sent);
	}

	public AggDataPayload getPayload() {
		return payload;
	}

	public void setPayload(AggDataPayload payload) {
		this.payload = payload;
	}

	public long getAggid() {
		return aggid;
	}

	public void setAggid(long aggid) {
		this.aggid = aggid;
	}

	public long getEdid() {
		return edid;
	}

	public void setEdid(long edid) {
		this.edid = edid;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(long saveTime) {
		this.saveTime = saveTime;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

}
