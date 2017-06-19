package com.ruvla.plsos.agg.prototypes.android.plsosagg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;

import com.ruvla.plsos.agg.model.PlsosConstants;
import com.ruvla.plsos.agg.model.aggctrl.Aggregator;
import com.ruvla.plsos.agg.model.aggctrl.Collector;
import com.ruvla.plsos.agg.model.aggctrl.EdgeDevice;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;
import com.ruvla.plsos.agg.model.dataview.PayloadBundle;
import com.ruvla.plsos.agg.prototypes.android.AndroidConstants;
import com.ruvla.plsos.agg.prototypes.android.plsosagg.dataview.AndroidPayloadBundle;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class AndroidCollector extends Collector {

	private OutputStream outputStream;
	private InputStream inputStream;
	private BluetoothSocket socket;

	public AndroidCollector(Aggregator owner, long edid, EdgeDevice targetDevice, int tranCount) {
		super(owner, edid, targetDevice);
		inputStream = null;
		outputStream = null;
		socket = null;
	}

	@Override
	protected boolean establishConnection(EdgeDevice device) {
		AndroidEdgeDevice androidEDD = (AndroidEdgeDevice) device;
		try {
			// Log.d(AndroidConstants.LOG_TAG, "#getting uuid");
			UUID serialPortUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
			socket = androidEDD.getBlDevice().createRfcommSocketToServiceRecord(serialPortUUID);
			// Log.d(AndroidConstants.LOG_TAG, "#connectiong to socket");
			socket.connect();
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	protected String performHandshake(EdgeDevice device) {
		byte[] buffer = ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(owner.getAggid()).array();
		try {
			outputStream.write(buffer);
			return device.toString();
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	protected PayloadBundle receivePayloadBundle(EdgeDevice device) {
		ObjectMapper jsonMapper = new ObjectMapper();
		final byte delimiter = 10; // This is the ASCII code for a newline
									// character
		int readBufferPosition = 0;
		byte[] readBuffer = new byte[1024];
		try {
			int bytesAvailable = inputStream.available();
			if (bytesAvailable > 0) {
				byte[] allBytes = new byte[bytesAvailable];
				inputStream.read(allBytes);
				for (int i = 0; i < bytesAvailable; i++) {
					byte b = allBytes[i];
					if (b != delimiter) {
						readBuffer[readBufferPosition++] = b;
					} else {
						byte[] encodedBytes = new byte[readBufferPosition];
						System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
						String jsonString = new String(encodedBytes, owner.getInitialConfiguration().getConfigurator().instantiateConfigProvider().getSendingCharset());
						PayloadBundle payloadBundle = jsonMapper.readValue(jsonString, AndroidPayloadBundle.class);
						// Log.d(AndroidConstants.LOG_TAG, "#Payload bundle
						// parsed");
						return payloadBundle;
					}
				}
			} else {
				// Log.d(AndroidConstants.LOG_TAG, "#No more bytes available");
			}
		} catch (IOException ex) {
			// Log.d(AndroidConstants.LOG_TAG, "#Error in reading byte object" +
			// ex);
		}
		return new PayloadBundle(null); // constructing Finish unit
	}

	@Override
	protected void sendConfirmation(EdgeDevice device, LinkedList<AggDataUnit> tranBuffer) {
		byte[] buffer = PlsosConstants.CONFIRMATION.getBytes();
		try {
			outputStream.write(buffer);
		} catch (IOException e) {
			// Log.d(AndroidConstants.LOG_TAG, "#Could not send confirmation");
		}
	}

	@Override
	protected void finish(EdgeDevice device) {
		try {
			outputStream.close();
		} catch (IOException e) {
			Log.e(AndroidConstants.LOG_TAG, this + " :Could not clouse output stream");
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			Log.e(AndroidConstants.LOG_TAG, this + " :Could not clouse input stream");
		}
		try {
			socket.close();
		} catch (IOException e) {
			Log.e(AndroidConstants.LOG_TAG, this + " :Could not clouse socket");
		}
	}

}
