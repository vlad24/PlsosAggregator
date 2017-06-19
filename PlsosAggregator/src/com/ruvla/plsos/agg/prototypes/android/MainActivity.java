package com.ruvla.plsos.agg.prototypes.android;

import java.util.HashMap;

import com.ruvla.plsos.agg.model.PlsosConstants;
import com.ruvla.plsos.agg.model.PlsosException;
import com.ruvla.plsos.agg.model.configuration.ConfigurationProvider;
import com.ruvla.plsos.agg.model.configuration.configpacks.AggConfig;
import com.ruvla.plsos.agg.model.configuration.configpacks.DataManipulatorConfig;
import com.ruvla.plsos.agg.model.configuration.configpacks.DetectorConfig;
import com.ruvla.plsos.agg.model.configuration.configpacks.SaverConfig;
import com.ruvla.plsos.agg.model.datapersist.DBConfig;
import com.ruvla.plsos.agg.prototypes.android.plsosagg.AndroidConfigurator;
//import com.ruvla.plsos.collector.prototypes.android.plsosagg.AndroidConfigurator;
import com.ruvla.plsoscollector.R;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private AndroidConfigurator mainConfigurator;
	private AggConfig aggConfig;
	private String newLogData;
	private BroadcastReceiver guiReceiver;
	private boolean logWindowNeeded = false;
	private boolean aggregationLaunched = false;
	private boolean uploading = false;
	// Elements
	private EditText etParamThread;
	private EditText etParamPerRef;
	private TextView tvStatus;
	private TextView tvLog;
	private Dialog logWindow;
	private Button bLaunchAgg;
	private Button bStopAgg;
	private Button bShowLogs;
	private Button bSendData;
	private Button bCloseLog;
	private ProgressBar pbBusy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Log.d(AndroidConstants.LOG_TAG, "MainActivity onCreate() called");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		enableBluetooth();
		mainConfigurator = new AndroidConfigurator();
		guiReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				int action = intent.getIntExtra(AndroidConstants.PARAMKEY_Action, PlsosConstants.NOTHING);
				if (action == AndroidConstants.PARAM_ActionGetLog) {
					newLogData = intent.getStringExtra(AndroidConstants.PARAMKEY_LogString);
					tvLog.setText(newLogData);
					// Log.d(AndroidConstants.LOG_TAG, "#MainActivity.Receiver:
					// log data updated");
					if (logWindowNeeded) {
						logWindowNeeded = false;
						logWindow.show();
					}
				} else if (action == AndroidConstants.PARAM_ActionManipulatorUpload && uploading) {
					tvStatus.setText(R.string.upload_finished);
					uploading = false;
					bSendData.setEnabled(true);
					if (!aggregationLaunched) {
						pbBusy.setVisibility(View.INVISIBLE);
						bLaunchAgg.setEnabled(true);
					}
				}
			}
		};
		registerReceiver(guiReceiver, new IntentFilter(AndroidConstants.FILTER_BROADCAST_TO_GUI));
		//// Finding GUI elements
		etParamThread = (EditText) findViewById(R.id.param_threads);
		etParamPerRef = (EditText) findViewById(R.id.param_saver_refresh);
		tvStatus = (TextView) findViewById(R.id.status_view);
		logWindow = new Dialog(this);
		logWindow.setContentView(R.layout.custom_dialog);
		tvLog = (TextView) logWindow.findViewById(R.id.log_tv);
		bCloseLog = (Button) logWindow.findViewById(R.id.close_log_button);
		bLaunchAgg = (Button) findViewById(R.id.start_button);
		bStopAgg = (Button) findViewById(R.id.stop_button);
		bSendData = (Button) findViewById(R.id.send_button);
		bShowLogs = (Button) findViewById(R.id.log_button);
		pbBusy = (ProgressBar) findViewById(R.id.progressBar1);
		//// Initializing GUI elements
		pbBusy.setVisibility(View.INVISIBLE);
		logWindow.setTitle(R.string.log_updates_title);
		tvLog.setMovementMethod(new ScrollingMovementMethod());
		bLaunchAgg.setEnabled(true);
		bStopAgg.setEnabled(false);
		bSendData.setEnabled(false);
		bShowLogs.setEnabled(false);
		///// Buttons' callbacks
		bCloseLog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Log.d(AndroidConstants.LOG_TAG, "#MainActivity: Closing by
				// button");
				if (!logWindowNeeded) {
					tvStatus.post(new Runnable() {
						@Override
						public void run() {
							tvStatus.setText("Logs closed.");
						}
					});
					logWindow.dismiss();
					logWindowNeeded = false;
					newLogData = null;
				}
			}
		});
		bLaunchAgg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (MainActivity.this.buildConfigFromUserInput()) {
					MainActivity.this.startAggServiceForAggregation();
				}
			}
		});
		bStopAgg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.this.stopAggService();
				// Log.d(AndroidConstants.LOG_TAG, "#MainActivity: Stopping
				// aggregation triggered!!!");
			}
		});
		bSendData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!uploading) {
					Log.d(AndroidConstants.LOG_TAG, "#MainActivity: Uploading to server triggered!!!!");
					Intent initIntent = new Intent(MainActivity.this, DataService.class);
					initIntent.putExtra(AndroidConstants.PARAMKEY_Action, AndroidConstants.PARAM_ActionManipulatorInit);
					Log.d(AndroidConstants.LOG_TAG, "#MainActivity: building data manipulator config...");
					DataManipulatorConfig config = new DataManipulatorConfig(AndroidConstants.ANDROID_SINGLE_AGG_ID,
							mainConfigurator);
					initIntent.putExtra(AndroidConstants.PARAMKEY_ManipulatorConfig, config);
					Log.d(AndroidConstants.LOG_TAG, "#MainActivity:starting data service for initing");
					startService(initIntent);
					Intent uploadIntent = new Intent(MainActivity.this, DataService.class);
					uploadIntent.putExtra(AndroidConstants.PARAMKEY_Action,
							AndroidConstants.PARAM_ActionManipulatorUpload);
					Log.d(AndroidConstants.LOG_TAG, "#MainActivity:starting data service for uploading");
					startService(uploadIntent);
					pbBusy.setVisibility(View.VISIBLE);
					tvStatus.setText(R.string.upload_started);
					bSendData.setEnabled(false);
					uploading = true;
				}
			}
		});
		bShowLogs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Log.d(AndroidConstants.LOG_TAG, "#MainActivity: Showing logs
				// in separate winfow triggered!!!!");
				logWindowNeeded = true;
				tvStatus.post(new Runnable() {
					@Override
					public void run() {
						tvStatus.setText("Loading logs. Please wait...");
					}
				});
				if (!aggregationLaunched) {
					bShowLogs.setEnabled(false);
				}
				if (newLogData == null) {
					// Log.d(AndroidConstants.LOG_TAG, "#MainActivity: fetching
					// logs from service");
					startAggServiceForUnfetchedLogs();
				} else {
					// Log.d(AndroidConstants.LOG_TAG, "#MainActivity: unshowed
					// data presents");
					sendBroadcast(new Intent(AndroidConstants.FILTER_BROADCAST_TO_GUI)
							.putExtra(AndroidConstants.PARAMKEY_Action, AndroidConstants.PARAM_ActionGetLog)
							.putExtra(AndroidConstants.PARAMKEY_LogString, newLogData));
				}
			}
		});
		//// Restoring old state if needed
		if (savedInstanceState != null) {
			// Log.d(AndroidConstants.LOG_TAG, "#MainActivity: restoring old
			// state");
		}
	}

	@Override
	public void onDestroy() {
		// Log.d(AndroidConstants.LOG_TAG, "#MainActivity: onDestroy() called");
		super.onDestroy();
		// Log.d(AndroidConstants.LOG_TAG, "#MainActivity: unregistering
		// receiver");
		unregisterReceiver(this.guiReceiver);
		//Singleton!
		mainConfigurator.instantiateLogger().closeLog();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Log.d(AndroidConstants.LOG_TAG, "#Main activity: saving state...");
		super.onSaveInstanceState(savedInstanceState);
	}

	public boolean buildConfigFromUserInput() {
		AggConfig.ParamsBuilder builder = new AggConfig.ParamsBuilder();
		try {
			Integer threadCount = Integer.parseInt(etParamThread.getText().toString());
			Integer saverRefresh = Integer.parseInt(etParamPerRef.getText().toString());
			ConfigurationProvider configs = mainConfigurator.instantiateConfigProvider();
			HashMap<String, Integer> updates = new HashMap<String, Integer>();
			updates.put("THREAD_COUNT", threadCount); updates.put("SAVER_REFRESH", saverRefresh); updates.put("DETECTOR_REFRESH", saverRefresh  - 1);
			configs.rebuild(updates);
			aggConfig = builder.setConfiguration(configs).setConfigurator(mainConfigurator).toAggConfig();
			return true;
		} catch (NumberFormatException e) {
			MainActivity.this.tvStatus.setText(R.string.param_input_format_error);
			return false;
		} catch (PlsosException e) {
			MainActivity.this.tvStatus.setText(R.string.param_input_logical_error);
			return false;
		}
	}

	protected void startAggServiceForUnfetchedLogs() {
		Intent logFetchIntent = new Intent(MainActivity.this, AggregatorService.class);
		logFetchIntent.putExtra(AndroidConstants.PARAMKEY_Action, AndroidConstants.PARAM_ActionGetLog);
		startService(logFetchIntent);
	}

	protected void enableBluetooth() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (!adapter.isEnabled()) {
			adapter.enable();
			Toast.makeText(getApplicationContext(), "Enabling bluetooth for correct working...", Toast.LENGTH_SHORT)
					.show();
		}
	}

	protected void startAggServiceForAggregation() {
		bLaunchAgg.setEnabled(false);
		bStopAgg.setEnabled(true);
		tvStatus.setText(R.string.agg_launched);
		bSendData.setText(R.string.send_string);
		bSendData.setEnabled(true);
		bShowLogs.setEnabled(true);
		Intent aggregationIntent = new Intent(MainActivity.this, AggregatorService.class);
		aggregationIntent.putExtra(AndroidConstants.PARAMKEY_Action, AndroidConstants.PARAM_ActionStart)
				.putExtra(AndroidConstants.PARAMKEY_AggParams, aggConfig)
				.putExtra(AndroidConstants.PARAMKEY_Aggid, AndroidConstants.ANDROID_SINGLE_AGG_ID);
		// Log.d(AndroidConstants.LOG_TAG, "#MainActivity: extras have been
		// put");
		aggregationLaunched = true;
		startService(aggregationIntent);
		pbBusy.setVisibility(View.VISIBLE);
		// Log.d(AndroidConstants.LOG_TAG, "#MainActivity: Agg service has been
		// created.");
	}

	private void prepareAggServiceToStop() {
		pbBusy.setVisibility(View.VISIBLE);
		startService(new Intent(MainActivity.this, AggregatorService.class).putExtra(AndroidConstants.PARAMKEY_Action,
				AndroidConstants.PARAM_ActionStop));
	}

	protected void stopAggService() {
		// Log.d(AndroidConstants.LOG_TAG, "#MainActivity: Aggregation is being
		// prepared to stop!");
		prepareAggServiceToStop();
		// Log.d(AndroidConstants.LOG_TAG, "#MainActivity: Fetching last
		// logs!");
		startAggServiceForUnfetchedLogs();
		// Log.d(AndroidConstants.LOG_TAG, "#MainActivity: Destroying service");
		stopService(new Intent(MainActivity.this, AggregatorService.class));
		aggregationLaunched = false;
		bLaunchAgg.setEnabled(true);
		bStopAgg.setEnabled(false);
		bSendData.setEnabled(true);
		tvStatus.setText(R.string.agg_stopped);
		// Log.d(AndroidConstants.LOG_TAG, "#MainActivity: Aggregation has been
		// stopped!");
		pbBusy.setVisibility(View.INVISIBLE);
	}

}
