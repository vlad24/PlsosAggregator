package com.ruvla.plsos.agg.model.datasend;

import com.ruvla.plsos.agg.model.configuration.configpacks.UploadConfig;

public abstract class Sender {
	protected DataManipulator owner;

	public Sender(DataManipulator owner) {
		this.owner = owner;
	}

	public abstract boolean authenticate(UploadConfig config);

	public abstract SendReport send(UploadConfig uploadConfig, Object data);

	public abstract void stopSending();
}
