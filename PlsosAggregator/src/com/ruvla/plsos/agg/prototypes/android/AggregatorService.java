package com.ruvla.plsos.agg.prototypes.android;

import com.ruvla.plsos.agg.model.PlsosConstants;
import com.ruvla.plsos.agg.model.aggctrl.AggregationManager;
import com.ruvla.plsos.agg.model.aggctrl.Aggregator;
import com.ruvla.plsos.agg.model.aggctrl.EdgeDevice;
import com.ruvla.plsos.agg.model.configuration.Configurator;
import com.ruvla.plsos.agg.model.configuration.configpacks.AggConfig;
import com.ruvla.plsos.agg.prototypes.android.plsosagg.AndroidEdgeDevice;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class AggregatorService extends Service {

	private AggregationManager aggManager = null;
	private Configurator configurator = null;
	private boolean managerInited = false;
	private BroadcastReceiver deviceReceiver = null; 
	
	public void onCreate() {
		super.onCreate();
		IntentFilter bluetoothFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		deviceReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(BluetoothDevice.ACTION_FOUND)) {
					Aggregator agg = aggManager.getAggregator(AndroidConstants.ANDROID_SINGLE_AGG_ID);
					BluetoothDevice bluetoothDevice = (BluetoothDevice) intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					Log.d(AndroidConstants.LOG_TAG, "#AggService: device"+bluetoothDevice.getName()+" is in radius...");
					long edid = bluetoothDevice.getName().hashCode();
					EdgeDevice edgeDevice = new AndroidEdgeDevice(edid, bluetoothDevice);
					agg.pushEdgeDevice(edgeDevice);
				}
			}
		};
		registerReceiver(deviceReceiver, bluetoothFilter);
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		int action = intent.getIntExtra(AndroidConstants.PARAMKEY_Action, PlsosConstants.NOTHING);
		switch (action) {
		case AndroidConstants.PARAM_ActionStart: {
			AggConfig aggConfig = (AggConfig) intent.getExtras().getSerializable(AndroidConstants.PARAMKEY_AggParams);
			configurator = aggConfig.getConfigurator();
			if (!managerInited){
				Log.d(AndroidConstants.LOG_TAG, "Initing agg manager...");
				AggregationManager.init(configurator.instantiateConfigProvider(), configurator.instantiateLogger());
				managerInited = true;
				aggManager = AggregationManager.getInstance();
				Log.d(AndroidConstants.LOG_TAG, "Inited.");
			}
			long aggid = intent.getLongExtra(AndroidConstants.PARAMKEY_Aggid, 0);
			aggManager.addAggregator(aggid, aggConfig);
			aggManager.launchAggregator(aggid);
			break;
		}
		case AndroidConstants.PARAM_ActionGetLog: {
			fetchLogAndSendToMainActivity();
			break;
		}
		case AndroidConstants.PARAM_ActionStop: {
			aggManager.stopAll();
			break;
		}
		}
		return START_NOT_STICKY;
	}

	public void onDestroy() {
		aggManager.destroy();
		unregisterReceiver(deviceReceiver);
		super.onDestroy();
	}

	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void fetchLogAndSendToMainActivity() {
		String logData = "No logs.";
		if (aggManager != null){
			logData = aggManager.getLogs();
		}
		Intent logIntent = new Intent(AndroidConstants.FILTER_BROADCAST_TO_GUI)
				.putExtra(AndroidConstants.PARAMKEY_Action, AndroidConstants.PARAM_ActionGetLog)
				.putExtra(AndroidConstants.PARAMKEY_LogString, logData);
		sendBroadcast(logIntent);
	}

}