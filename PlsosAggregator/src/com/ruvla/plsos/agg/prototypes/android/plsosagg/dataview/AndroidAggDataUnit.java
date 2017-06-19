package com.ruvla.plsos.agg.prototypes.android.plsosagg.dataview;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.ruvla.plsos.agg.model.dataview.AggDataPayload;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;

public class AndroidAggDataUnit extends AggDataUnit {

	@JsonIgnore
	private long saveTime;
	private long timestamp;
	private long aggid;
	private long edid;
	@JsonIgnore
	private boolean sent;

	private AndroidAggDataPayload payload;

	@Override
	public String toString() {
		return "<<" + timestamp + ":::" + aggid + "<-" + edid + "[" + payload + "]>>";
	}

	public AndroidAggDataUnit() {
	}

	public AndroidAggDataUnit(long timestamp, long saveTime, long aggid, long edid,
			AndroidAggDataPayload androidAggDataPayload, boolean sent) {
		this.setTimestamp(timestamp);
		this.setSaveTime(saveTime);
		this.setAggid(aggid);
		this.setEdid(edid);
		this.setPayload(androidAggDataPayload);
		this.setSent(sent);
	}

	public AndroidAggDataUnit(long timestamp, AggDataPayload payload) {
		this.setAggid(-1);
		this.setEdid(-1);
		this.setTimestamp(timestamp);
		this.setSaveTime(-1);
		this.setPayload((AndroidAggDataPayload) payload);
		this.setSent(false);
	}

	//
	public AndroidAggDataUnit(AggDataUnit unit) {
		this.timestamp = unit.getTimestamp();
		this.saveTime = unit.getSaveTime();
		this.aggid = unit.getAggid();
		this.edid = unit.getEdid();
		this.payload = (AndroidAggDataPayload) (unit.getPayload());// widening
																	// from
																	// abstract
																	// =
																	// guessing
																	// cast
		this.sent = unit.isSent();
	}

	public AggDataUnit toCommon() {
		return new AggDataUnit(this.timestamp, this.saveTime, this.aggid, this.edid, (AndroidAggDataPayload) payload, // narrowing
																														// cast
																														// to
																														// abstract
																														// payload
				this.sent);
	}

	@Override
	public AggDataPayload getPayload() {
		return payload;
	}

	@Override
	public void setPayload(AggDataPayload payload) {
		this.payload = (AndroidAggDataPayload) payload;
	}

	@Override
	public long getAggid() {
		return aggid;
	}

	@Override
	public void setAggid(long aggid) {
		this.aggid = aggid;
	}

	@Override
	public long getEdid() {
		return edid;
	}

	@Override
	public void setEdid(long edid) {
		this.edid = edid;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public long getSaveTime() {
		return saveTime;
	}

	@Override
	public void setSaveTime(long saveTime) {
		this.saveTime = saveTime;
	}

	@Override
	public boolean isSent() {
		return sent;
	}

	@Override
	public void setSent(boolean sent) {
		this.sent = sent;
	}
}
