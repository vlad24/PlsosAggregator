package com.ruvla.plsos.agg.prototypes.android;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.ruvla.plsos.agg.prototypes.android.plsosagg.dataview.AndroidAggDataPayloadModel;
import com.ruvla.plsos.agg.prototypes.android.plsosagg.dataview.AndroidAggDataUnitModel;

import android.app.Application;

public class AAInit extends Application {
	public static final String TAG = "VIVZ";

	@Override
	public void onCreate() {
		super.onCreate();
		Configuration.Builder configurationBuilder = new Configuration.Builder(this);
		configurationBuilder.addModelClasses(AndroidAggDataPayloadModel.class);
		configurationBuilder.addModelClasses(AndroidAggDataUnitModel.class);
		ActiveAndroid.initialize(configurationBuilder.create());
	}
}
