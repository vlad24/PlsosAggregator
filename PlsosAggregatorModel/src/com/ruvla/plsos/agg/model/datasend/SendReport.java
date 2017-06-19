package com.ruvla.plsos.agg.model.datasend;

public class SendReport {
	private boolean success;
	private long lastSent;
	private String info;

	public SendReport(boolean success, long lastSent, String info) {
		this.success = success;
		this.lastSent = lastSent;
		this.info = info;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public long getLastSentTime() {
		return lastSent;
	}

	public void setLastSent(long lastSent) {
		this.lastSent = lastSent;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
