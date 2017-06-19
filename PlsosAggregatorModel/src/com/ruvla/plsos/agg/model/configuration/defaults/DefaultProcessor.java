package com.ruvla.plsos.agg.model.configuration.defaults;

import java.util.Queue;

import com.ruvla.plsos.agg.model.aggctrl.AggProcessor;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;

public class DefaultProcessor implements AggProcessor {

	@Override
	public Queue<? extends AggDataUnit> process(Queue<? extends AggDataUnit> units) {
		return units;
	}

}
