package com.ruvla.plsos.agg.prototypes.android.plsosagg.dataview;

import com.ruvla.plsos.agg.model.dataview.AggDataPayload;

public class AndroidAggDataPayload extends AggDataPayload {
	public String data;

	public String toString() {
		return data;
	}

	public AndroidAggDataPayload(String data) {
		this.data = data;
	}

	public AndroidAggDataPayload(AggDataPayload payload) {
		this.data = "no data";
	}

	// forth
	public AndroidAggDataPayloadModel toModel() {
		return new AndroidAggDataPayloadModel(this);
	}

	// back
	public AggDataPayload toCommon() {
		return (AggDataPayload) this;
	}
}
