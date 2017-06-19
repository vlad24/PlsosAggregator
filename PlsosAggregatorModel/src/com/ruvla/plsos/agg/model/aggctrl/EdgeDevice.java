package com.ruvla.plsos.agg.model.aggctrl;

import com.ruvla.plsos.agg.model.PlsosConstants;

/**
 * @author Vladislav
 * Class representing an Edge Device.
 * Edge Device - device that an aggregator collects data from.
 * It is assumed an EdgeDevice has a unique identifier called edid. 
 */
public class EdgeDevice {
	protected long edid;

	@Override
	public int hashCode() {
		return (int) edid;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || (!(other instanceof EdgeDevice))) {
			return false;
		} else {
			return edid == (((EdgeDevice) other).edid);
		}
	}

	@Override
	public String toString() {
		return "ED_" + String.valueOf(edid);
	}


	/**
	 * Full constructor
	 * @param id unique identifier of the edge device
	 */
	public EdgeDevice(long id) {
		super();
		this.edid = id;
	}
	
	/**
	 * @return the edid
	 */
	public long getEdid() {
		return edid;
	}
	
	/**
	 * Empty constructor 
	 * IF this constructor is used edge device id of the constructed device will be equal to PlsosConstants.NOTHING
	 */
	public EdgeDevice() {
		super();
		this.edid = PlsosConstants.NOTHING;
	}

}
