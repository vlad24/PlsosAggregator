package com.ruvla.plsos.agg.model.aggctrl;

import java.io.Serializable;
import java.util.Queue;

import com.ruvla.plsos.agg.model.dataview.AggDataUnit;

/**
 * @author Vladislav
 * Class that processes each input unit performs necessary changes and return processed units
 */
public interface AggProcessor extends Serializable {
	/**
	 * Process each input unit performs necessary changes and return processed units
	 * @param units units to be processed
	 * @return queue of jusst processed units
	 */
	public Queue<? extends AggDataUnit> process(Queue<? extends AggDataUnit> units);

}
