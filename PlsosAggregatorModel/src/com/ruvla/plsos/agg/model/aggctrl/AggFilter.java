package com.ruvla.plsos.agg.model.aggctrl;

import com.ruvla.plsos.agg.model.dataview.AggDataUnit;

/**
 * @author Vladislav
 * Class that sets filter conditions on incoming devices and units
 */
public interface AggFilter {
	
	/**
	 * @param device device to be checked for matching
	 * @return true if device matches conditions set by the function, false otherwise
	 */
	public boolean match(EdgeDevice device);

	/**
	 * @param unit unit to be checked for matching
	 * @return whether device matches conditions set by function
	 */
	public boolean match(AggDataUnit unit);
}
