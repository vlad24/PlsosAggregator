package com.ruvla.plsos.agg.model.configuration.defaults;

import com.ruvla.plsos.agg.model.aggctrl.AggFilter;
import com.ruvla.plsos.agg.model.aggctrl.EdgeDevice;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;

public class DefaultAggDataFilter implements AggFilter {

	@Override
	public boolean match(AggDataUnit unit) {
		return true;
	}

	@Override
	public boolean match(EdgeDevice device) {
		return true;
	}

}
