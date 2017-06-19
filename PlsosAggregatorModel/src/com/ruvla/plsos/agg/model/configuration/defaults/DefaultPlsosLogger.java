package com.ruvla.plsos.agg.model.configuration.defaults;

import com.ruvla.plsos.agg.model.logging.PlsosLogger;
import com.ruvla.plsos.agg.model.logging.PlsosMessage;

public class DefaultPlsosLogger implements PlsosLogger {

	
	public DefaultPlsosLogger() {
		
	}
	@Override
	public boolean openLog() {
		return true;
	}

	@Override
	public boolean closeLog() {
		return true;
	}

	@Override
	public void logMessage(PlsosMessage message) {
		System.out.println(message);
	}

	@Override
	public String fetchLogData() {
		return "See System.out";
	}

}
