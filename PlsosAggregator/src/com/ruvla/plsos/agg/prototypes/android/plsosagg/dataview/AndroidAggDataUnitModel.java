package com.ruvla.plsos.agg.prototypes.android.plsosagg.dataview;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

//needed for annotations
@Table(name = "AGG_DATA_UNITS")
public class AndroidAggDataUnitModel extends Model {
	@Override
	public String toString() {
		return "<<" + timestamp + "|" + aggid + "<-" + edid + "[" + payload + "]>>";
	}

	@Column(name = "SAVE_TIME")
	private long saveTime;
	@Column(name = "TIMESTAMPP")
	private long timestamp;
	@Column(name = "AGGID")
	private long aggid;
	@Column(name = "EDID")
	private long edid;
	@Column(name = "PAYLOAD")
	private AndroidAggDataPayloadModel payload;
	@Column(name = "SENT")
	private boolean sent;

	public AndroidAggDataPayloadModel getPayload() {
		return payload;
	}

	public AndroidAggDataUnitModel() {
		super();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
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

	public void setPayload(AndroidAggDataPayloadModel payload) {
		this.payload = payload;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public long getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(long saveTime) {
		this.saveTime = saveTime;
	}

	public AndroidAggDataUnitModel(AndroidAggDataUnit unit) {
		this.aggid = unit.getAggid();
		this.edid = unit.getEdid();
		this.timestamp = unit.getTimestamp();
		this.saveTime = unit.getSaveTime();
		this.payload = ((AndroidAggDataPayload) (unit.getPayload())).toModel();
		this.sent = unit.isSent();
	}

	public AndroidAggDataUnit toCommon() {
		return new AndroidAggDataUnit(this.timestamp, this.saveTime, this.aggid, this.edid, payload.toCommon(),
				this.sent);
	}
}
