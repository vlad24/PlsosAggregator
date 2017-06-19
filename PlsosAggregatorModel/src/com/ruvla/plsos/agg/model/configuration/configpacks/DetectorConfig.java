package com.ruvla.plsos.agg.model.configuration.configpacks;

import java.io.Serializable;

import com.ruvla.plsos.agg.model.configuration.ConfigurationProvider;

/**
 * @author Vladislav
 *
 */
@SuppressWarnings("serial")
public class DetectorConfig implements Serializable {
	
	private int detectorRefresh;
	
	public DetectorConfig(ConfigurationProvider provider){
		this.detectorRefresh = provider.getDefaultDetectorRefresh();
	}
	
	public DetectorConfig(int detectorRefresh) {
		this.detectorRefresh = detectorRefresh;
	}


	/**
	 * Get refresh period of detector in seconds.
	 * If period is zero it means that detector is likely to work in asynchronous mode
	 * @return refresh after which detector will initiate polling or zero
	 */
	public int getDetectorRefresh() {
		return detectorRefresh;
	}

	/**
	 * Set refresh period of detector in seconds.
	 * Zero period means that detector will work in asynchronous mode.
	 * @param detectorRefresh refresh after which detector will initiate polling or zero
	 */
	public void setDetectorRefresh(int detectorRefresh) {
		this.detectorRefresh = detectorRefresh;
	}

	
	/**
	 * Check whether the detector will have some refresh period or will work asynchronously
	 * @return whether the detector will have some refresh period or will work asynchronously
	 */
	public boolean withRefreshPeriod() {
		return detectorRefresh > 0;
	}

}
