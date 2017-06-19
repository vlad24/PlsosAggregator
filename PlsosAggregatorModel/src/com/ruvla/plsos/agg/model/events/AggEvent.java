package com.ruvla.plsos.agg.model.events;

public abstract class AggEvent {
	public long aggregatorId;
	public int code;
	public String comment;
	public long time;

	protected AggEvent(int code, long senderId, String comment) {
		super();
		this.time = System.currentTimeMillis();
		this.code = code;
		this.comment = comment;
		this.aggregatorId = senderId;
	}
}
