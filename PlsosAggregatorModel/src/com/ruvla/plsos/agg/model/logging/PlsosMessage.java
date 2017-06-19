package com.ruvla.plsos.agg.model.logging;

import com.ruvla.plsos.agg.model.PlsosConstants;

public class PlsosMessage {
	private long time;
	private String sender;
	private String category;
	private String subject;
	private String message;

	public PlsosMessage(String sender, String message){
		this.time = System.currentTimeMillis();
		this.sender = sender;
		this.category = PlsosConstants.MESSAGE_CATEGORY_OTHER;
		this.subject = PlsosConstants.MESSAGE_SUBJECT_OTHER;
		this.message = message; 
	}
	
	public PlsosMessage(long time, String sender, String category, String subject, String message) {
		super();
		this.setTime(time);
		this.sender = sender;
		this.category = category;
		this.subject = subject;
		this.message = message;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
