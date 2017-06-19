package com.ruvla.plsos.agg.prototypes.android.plsosagg.dataview;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "CUSTOM_PAYLOAD")
public class AndroidAggDataPayloadModel extends Model {

	@Column(name = "RAW_DATA")
	public String data;

	public String toString() {
		return data;
	}

	public AndroidAggDataPayloadModel() {
		super();
	}

	public AndroidAggDataPayloadModel(AndroidAggDataPayload payload) {
		this.data = payload.data;
	}

	// back
	public AndroidAggDataPayload toCommon() {
		return new AndroidAggDataPayload(this.data);
	}

}
