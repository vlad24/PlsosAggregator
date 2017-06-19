package com.ruvla.plsos.agg.prototypes.android.plsosagg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.ruvla.plsos.agg.model.logging.PlsosLogger;
import com.ruvla.plsos.agg.model.logging.PlsosMessage;
import com.ruvla.plsos.agg.prototypes.android.AndroidConstants;

import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

public class AndroidPlsosLog implements PlsosLogger {

	private static AndroidPlsosLog log;
	public static AndroidPlsosLog getInstance(){
		if (log == null){
			log = new AndroidPlsosLog();
		}
		return log;
	}
	private boolean opened = false;
	private File file = null;
	private Lock logLock = null;
	private OutputStreamWriter writer = null;
	private String format = AndroidConstants.LOG_DATE_FORMAT;
	private StringBuilder logUpdates = null;

	public String toString() {
		return (this == null) ? "missing log" : file.getAbsolutePath();
	}

	private AndroidPlsosLog() {
		opened = false;
		logUpdates = new StringBuilder(128);
	}

	public String formatTime(long millis) {
		return DateFormat.format(format, millis).toString();
	}

	public long getTime() {
		return System.currentTimeMillis();
	}

	private void resetBuilder() {
		logUpdates.setLength(0);
		logUpdates.append("~~~~~~\n All logs available at: ").append(file.getParent()).append("\n~~~~~~");
	}

	public boolean isOpened() {
		return opened;
	}

	public boolean openLog() {
		if (!opened) {
			try {
				File newFolder = new File(Environment.getExternalStorageDirectory(), AndroidConstants.PLSOS_LOG_DIR);
				if (!newFolder.exists()) {
					Log.d(AndroidConstants.LOG_TAG, "Folder created");
					newFolder.mkdir();
				}
				file = new File(newFolder, AndroidConstants.LOG_FILE_NAME + "_" + getTime() + ".txt");
				Log.d(AndroidConstants.LOG_TAG, "File created:" + file.getAbsolutePath());
				file.createNewFile();
				writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
				Log.d(AndroidConstants.LOG_TAG, "Writer created");
				logLock = new ReentrantLock();
				logUpdates.append("Your logs are available at : ").append(file.getAbsolutePath())
						.append("\n Logging started at: ").append(getTime()).append("\n");
				opened = true;
			} catch (IOException e) {
				Log.i(AndroidConstants.LOG_TAG, "#AndroidPlsosLog: fileError: " + e);
				opened = false;
			}
		}
		return opened;
	}

	public boolean closeLog() {
		Log.d(AndroidConstants.LOG_TAG, "#AndroidPlsosLog: closing");
		if (opened) {
			try {
				writer.flush();
				writer.close();
				opened = false;
				return true;
			} catch (IOException e) {
				return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public void logMessage(PlsosMessage message) {
		Log.i(AndroidConstants.LOG_TAG, "#AndroidPlsosLog: constructing log enrty.");
		StringBuilder logLineBuilder = new StringBuilder();
		logLineBuilder.append(formatTime(message.getTime())).append(":").append(message.getCategory()).append(" | ")
				.append(message.getSubject()).append(" | ").append(message.getSender()).append(" | ")
				.append(message.getMessage()).append("\n");
		String result = logLineBuilder.toString();
		logLock.lock();
		Log.i(AndroidConstants.LOG_TAG, "#AndroidPlsosLog: file locked. writing " + result);
		try {
			writer.write(result);
			logUpdates.append(result);
		} catch (IOException e) {
			Log.e(AndroidConstants.LOG_TAG, "#AndroidPlsosLog: could not write," + e.toString());
		} finally {
			logLock.unlock();
			Log.i(AndroidConstants.LOG_TAG, "#AndroidPlsosLog: v. file unlocked.");
		}
		// now wait free flushing
		if (logLock.tryLock()) {
			Log.i(AndroidConstants.LOG_TAG, "#AndroidPlsosLog: locked.flushing...");
			try {
				writer.flush();
			} catch (IOException e) {
				Log.i(AndroidConstants.LOG_TAG, "#AndroidPlsosLog: flushing not succesful." + e.getMessage());
			} finally {
				logLock.unlock();
				Log.i(AndroidConstants.LOG_TAG, "#AndroidPlsosLog: v. unclocked.");
			}
		}
	}

	@Override
	public String fetchLogData() {
		// RETURNING updates!
		Log.d(AndroidConstants.LOG_TAG, "#AndroidPlsosLog: fetching log");
		String updates = null;
		synchronized (this) {
			updates = logUpdates.toString();
			resetBuilder();
		}
		return updates;
	}

}
