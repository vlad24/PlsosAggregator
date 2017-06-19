package com.ruvla.plsos.agg.prototypes.android;

import com.ruvla.plsos.agg.model.PlsosConstants;
import com.ruvla.plsos.agg.model.configuration.configpacks.DataManipulatorConfig;
import com.ruvla.plsos.agg.model.datasend.DataManipulator;
import com.ruvla.plsos.agg.model.logging.PlsosLogger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class DataService extends Service {

	DataManipulatorConfig manConfig;
	DataManipulator manipulator;
	boolean manInited;

	public void onCreate() {
		manInited = false;
		super.onCreate();
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		int action = intent.getIntExtra(AndroidConstants.PARAMKEY_Action, PlsosConstants.NOTHING);
		switch (action) {
		case AndroidConstants.PARAM_ActionManipulatorInit: {
			manConfig = (DataManipulatorConfig) intent.getExtras()
					.getSerializable(AndroidConstants.PARAMKEY_ManipulatorConfig);
			if (manConfig != null && !manInited) {
				PlsosLogger logger = manConfig.getConfigurator().instantiateLogger();
				manipulator = manConfig.getConfigurator().instantiateManipulator(logger, manConfig);
				manInited = true;
			}
			break;
		}
		case AndroidConstants.PARAM_ActionManipulatorUpload: {
			Log.d(AndroidConstants.LOG_TAG, "#DataService: uploading");
			if (manInited) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						manipulator.uploadAllDataToServer();
						DataService.this.sendBroadcast(new Intent(AndroidConstants.FILTER_BROADCAST_TO_GUI).putExtra(
								AndroidConstants.PARAMKEY_Action, AndroidConstants.PARAM_ActionManipulatorUpload));
					}
				}).start();
			}
			break;
		}
		case AndroidConstants.PARAM_ActionManipulatorStop: {
			if (manInited) {
				manipulator.stopUploadings();
				sendBroadcast(new Intent(AndroidConstants.FILTER_BROADCAST_TO_GUI)
						.putExtra(AndroidConstants.PARAMKEY_Action, AndroidConstants.PARAM_ActionManipulatorStop));
			}
			break;
		}
		}
		return START_NOT_STICKY;
	}

	public void onDestroy() {
		manipulator.destroy();
		super.onDestroy();
	}

	public IBinder onBind(Intent arg0) {
		return null;
	}

}
