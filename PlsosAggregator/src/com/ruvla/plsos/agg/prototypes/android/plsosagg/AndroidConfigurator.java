package com.ruvla.plsos.agg.prototypes.android.plsosagg;

import com.ruvla.plsos.agg.model.aggctrl.Aggregator;
import com.ruvla.plsos.agg.model.aggctrl.Collector;
import com.ruvla.plsos.agg.model.aggctrl.Detector;
import com.ruvla.plsos.agg.model.aggctrl.EdgeDevice;
import com.ruvla.plsos.agg.model.configuration.ConfigurationProvider;
import com.ruvla.plsos.agg.model.configuration.configpacks.DetectorConfig;
import com.ruvla.plsos.agg.model.configuration.configpacks.UploadConfig;
import com.ruvla.plsos.agg.model.configuration.defaults.DefaultDebugDetector;
import com.ruvla.plsos.agg.model.configuration.defaults.DefaultDropboxSender;
import com.ruvla.plsos.agg.model.configuration.defaults.DefaultPlsosConfigurator;
import com.ruvla.plsos.agg.model.datapersist.DBAdapter;
import com.ruvla.plsos.agg.model.datasend.DataManipulator;
import com.ruvla.plsos.agg.model.datasend.Sender;
import com.ruvla.plsos.agg.model.logging.PlsosLogger;
import com.ruvla.plsos.agg.prototypes.android.plsosagg.dataview.AndroidDebugCollector;

@SuppressWarnings("serial")
public class AndroidConfigurator extends DefaultPlsosConfigurator {

	@Override
	public Detector instantiateDetector(Aggregator owner, DetectorConfig config) {
		// return new AndroidDetector(owner);
		return new DefaultDebugDetector(owner);
	}

	@Override
	public Collector instantiateCollector(Aggregator owner, long id, EdgeDevice targetDevice) {
		// to be changed!
		return new AndroidDebugCollector(owner, id, targetDevice);
	}

	@Override
	public DBAdapter instantiateDBAdapter() {
		return new AndroidDBAdapter();
	}

	@Override
	public Sender instantiateSender(DataManipulator owner, UploadConfig uploadConfig) {
		return new DefaultDropboxSender(owner, uploadConfig);
	}

	@Override
	public PlsosLogger instantiateLogger() {
		return AndroidPlsosLog.getInstance();
	}

	@Override
	public ConfigurationProvider instantiateConfigProvider() {
		return new SecretAndroidConfigurationProvider();
	}

}
