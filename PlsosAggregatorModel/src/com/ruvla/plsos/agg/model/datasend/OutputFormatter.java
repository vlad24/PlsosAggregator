package com.ruvla.plsos.agg.model.datasend;

import java.util.Queue;

import com.ruvla.plsos.agg.model.PlsosConstants;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;

public abstract class OutputFormatter {
	protected String format;

	public OutputFormatter() {
		setFormat(PlsosConstants.FORMAT_JSON);
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public abstract Object format(Queue<? extends AggDataUnit> unitList);
}
