package com.ruvla.plsos.agg.model.logging;

public interface PlsosLogger {
	public boolean openLog();

	public boolean closeLog();

	public void logMessage(PlsosMessage message);

	public String fetchLogData();

}
