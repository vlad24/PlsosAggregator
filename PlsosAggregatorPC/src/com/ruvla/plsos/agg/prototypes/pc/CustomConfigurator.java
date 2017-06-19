package com.ruvla.plsos.agg.prototypes.pc;

import com.ruvla.plsos.agg.model.aggctrl.AggFilter;
import com.ruvla.plsos.agg.model.aggctrl.Aggregator;
import com.ruvla.plsos.agg.model.aggctrl.Detector;
import com.ruvla.plsos.agg.model.aggctrl.EdgeDevice;
import com.ruvla.plsos.agg.model.configuration.configpacks.DetectorConfig;
import com.ruvla.plsos.agg.model.configuration.defaults.DefaultDebugDetector;
import com.ruvla.plsos.agg.model.configuration.defaults.DefaultPlsosConfigurator;
import com.ruvla.plsos.agg.model.configuration.defaults.DefaultRAMAdapter;
import com.ruvla.plsos.agg.model.datapersist.DBAdapter;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;

public class CustomConfigurator extends DefaultPlsosConfigurator {

	@Override
	public DBAdapter instantiateDBAdapter() {
		return new DefaultRAMAdapter();
	}

	@Override
	public Detector instantiateDetector(Aggregator owner, DetectorConfig config) {
		return new DefaultDebugDetector(owner);
	}

	@Override
	public AggFilter instantiatePreFilter() {
		return new AggFilter() {
			@Override
			public boolean match(AggDataUnit unit) {
				return unit.getPayload() != null;
			}
			@Override
			public boolean match(EdgeDevice device) {
				return device.getEdid() != 100L ;
			}
		};
	}
}
