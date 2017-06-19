package com.ruvla.plsos.agg.model.datasend;

import java.util.LinkedList;
import java.util.Queue;

import com.ruvla.plsos.agg.model.PlsosConstants;
import com.ruvla.plsos.agg.model.aggctrl.AggFilter;
import com.ruvla.plsos.agg.model.aggctrl.AggProcessor;
import com.ruvla.plsos.agg.model.configuration.ConfigurationProvider;
import com.ruvla.plsos.agg.model.configuration.configpacks.DataManipulatorConfig;
import com.ruvla.plsos.agg.model.configuration.configpacks.UploadConfig;
import com.ruvla.plsos.agg.model.datapersist.DBAdapter;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;
import com.ruvla.plsos.agg.model.logging.PlsosLogger;
import com.ruvla.plsos.agg.model.logging.PlsosMessage;


public class DataManipulator implements Runnable {
	
	protected PlsosLogger logger;
	protected DataManipulatorConfig config;
	protected long mid;
	protected long ownerAggid;
	protected String authURL;
	protected UploadConfig uploadConfig;
	protected String format;
	protected OutputFormatter formatter;
	protected Sender sender;
	protected DBAdapter dbAdapter;
	protected boolean turnedOn;
	protected boolean authenticated = false;
	protected boolean serializedSending = false;
	protected boolean cleanMode;
	protected AggProcessor postProcessor;
	protected ConfigurationProvider provider;

	public DataManipulator(PlsosLogger logger, DataManipulatorConfig config) {
		this.logger = logger;
		this.logger.openLog();
		ownerAggid = config.getOwnerAggid();
		mid = Long.parseLong(ownerAggid + "111");
		uploadConfig = config.getUploadConfigs();
		formatter = config.getConfigurator().instantiateOutputFormatter();
		formatter.setFormat(config.getFormat());
		dbAdapter = config.getConfigurator().instantiateDBAdapter();
		provider = config.getConfigurator().instantiateConfigProvider();
		sender = config.getConfigurator().instantiateSender(this, uploadConfig);
		postProcessor = config.getConfigurator().instantiatePostProcessor();
		authenticated = sender.authenticate(uploadConfig);
		cleanMode = config.isCleanMode();
		turnedOn = true;
		serializedSending = config.isSerializedSending();
	}

	protected long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public SendReport uploadAllDataToServer() {
		return uploadDataToServer(getCurrentTime());
	}

	public SendReport uploadDataToServer(long upperTime) {
		logger.logMessage(new PlsosMessage(System.currentTimeMillis(), "M"+this.mid, PlsosConstants.MESSAGE_CATEGORY_Info, PlsosConstants.MESSAGE_SUBJECT_Fetching, "Uploading started"));
		SendReport report = new SendReport(false, -1, "Not authenticated");
		if (authenticated) {
			Queue<? extends AggDataUnit> units = null;
			units = dbAdapter.selectAllAggDataUnits(ownerAggid, upperTime, null);
			Queue<? extends AggDataUnit> readyUnits = postProcessor.process(units);
			Object dataForServer = formatter.format(readyUnits);
			report = sender.send(uploadConfig, dataForServer);
			if (report.isSuccess()) {
				logger.logMessage(new PlsosMessage(System.currentTimeMillis(), "M"+this.mid, PlsosConstants.MESSAGE_CATEGORY_Info, PlsosConstants.MESSAGE_SUBJECT_Fetching, "Successful upload. Commiting..."));
				commitUploading(upperTime);
			} else if (report.getLastSentTime() != PlsosConstants.NOTHING && serializedSending) {
				logger.logMessage(new PlsosMessage(System.currentTimeMillis(), "M"+this.mid, PlsosConstants.MESSAGE_CATEGORY_Info, PlsosConstants.MESSAGE_SUBJECT_Fetching, "Partially successful upload. Serialized dening. Commiting parts..."));
				commitUploading(report.getLastSentTime());
			}
		}
		return report;
	}

	public boolean isSerializedSending() {
		return serializedSending;
	}

	public void setSerializedSending(boolean serializedSending) {
		this.serializedSending = serializedSending;
	}

	public void stopUploadings() {
		logger.logMessage(new PlsosMessage(System.currentTimeMillis(), "M"+this.mid, PlsosConstants.MESSAGE_CATEGORY_Info, PlsosConstants.MESSAGE_SUBJECT_Fetching, "Stopping all uploadings..."));
		this.sender.stopSending();
	}

	public void deactivate() {
		stopUploadings();
		this.turnedOn = false;
	}

	public void destroy() {
		deactivate();
	}

	public void commitUploading(long requestTime) {
		dbAdapter.beginTransaction();
		try {
			dbAdapter.markSent(ownerAggid, requestTime, null);
			if (cleanMode) {
				dbAdapter.deleteAllDataUnits(ownerAggid, requestTime, null);
			}
			dbAdapter.commitTransaction();
		} catch (Exception error) {
			logger.logMessage(new PlsosMessage(System.currentTimeMillis(), "M"+this.mid, PlsosConstants.MESSAGE_CATEGORY_Error, PlsosConstants.MESSAGE_SUBJECT_Fetching, error.getMessage()));
			dbAdapter.rollbackTransaction();
		} finally {
			dbAdapter.endTransaction();
		}
	}

	@Override
	public void run() {
		if (this.turnedOn) {
			uploadAllDataToServer();
		}
	}

	public ConfigurationProvider getConfigProvider() {
		return this.provider;
	}

	public Queue<? extends AggDataUnit> fetchData(AggFilter filter) {
		logger.logMessage(new PlsosMessage(System.currentTimeMillis(), "M"+this.mid, PlsosConstants.MESSAGE_CATEGORY_Info, PlsosConstants.MESSAGE_SUBJECT_Fetching, "Selecting units using aggFilter..."));
		Queue<? extends AggDataUnit> units = new LinkedList<AggDataUnit>();
		Queue<AggDataUnit> resultUnits = new LinkedList<AggDataUnit>();
		units = dbAdapter.selectAllAggDataUnits(ownerAggid, getCurrentTime(), null);
		for (AggDataUnit unit : units){
			if (filter.match(unit)){
				resultUnits.add(unit);
			}
		}
		return resultUnits;
	}

}
