package com.ruvla.plsos.agg.model.configuration.defaults;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;
import com.ruvla.plsos.agg.model.configuration.configpacks.UploadConfig;
import com.ruvla.plsos.agg.model.datasend.DataManipulator;
import com.ruvla.plsos.agg.model.datasend.SendReport;
import com.ruvla.plsos.agg.model.datasend.Sender;

public class DefaultDropboxSender extends Sender {

	private DbxClient client;
	private DbxRequestConfig requestConfig;

	public DefaultDropboxSender(DataManipulator owner, UploadConfig uploadConfig) {
		super(owner);
		requestConfig = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
	}


	@Override
	public boolean authenticate(UploadConfig uploadConfig) {
		uploadConfig.setToken(owner.getConfigProvider().getAuthToken());
		client = new DbxClient(requestConfig, (String) uploadConfig.getToken());
		return true;
	}

	@Override
	public SendReport send(UploadConfig uploadConfig, Object data) {
		// Log.d(AndroidConstants.LOG_TAG, "+++Uploading...");
		String stringData = (String) data;
		FileInputStream inputStream = null;
		File inputFile = null;
		try {
			inputFile = File.createTempFile("upload_" + System.currentTimeMillis(), ".tmp");
			inputStream = new FileInputStream(inputFile);
			BufferedWriter bw = new BufferedWriter(new FileWriter(inputFile));
			bw.write(stringData);
			bw.close();
			DbxEntry.File uploadedFile = client.uploadFile("/" + inputFile.getName(), DbxWriteMode.add(),
					inputFile.length(), inputStream);
			return new SendReport(true, -1, "Uploaded as " + uploadedFile.name);
		} catch (IOException e) {
			e.printStackTrace();
			return new SendReport(false, -1, e.getMessage());
		} catch (DbxException e) {
			e.printStackTrace();
			return new SendReport(false, -1, e.getMessage());
		} finally {
			try {
				inputFile.delete();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void stopSending() {
	}

}
