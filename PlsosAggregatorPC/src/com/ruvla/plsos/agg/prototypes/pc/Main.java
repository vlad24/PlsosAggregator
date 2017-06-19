package com.ruvla.plsos.agg.prototypes.pc;

import com.ruvla.plsos.agg.model.PlsosException;
import com.ruvla.plsos.agg.model.PlsosIDs;
import com.ruvla.plsos.agg.model.aggctrl.AggregationManager;
import com.ruvla.plsos.agg.model.configuration.Configurator;
import com.ruvla.plsos.agg.model.configuration.configpacks.AggConfig;
import com.ruvla.plsos.agg.model.configuration.defaults.DefaultPlsosConfigurator;

public class Main {
	public static void main(String[] args) throws PlsosException, InterruptedException {
		Configurator defaultConfigurator = new DefaultPlsosConfigurator();
		Configurator customConfigurator = new CustomConfigurator();
		AggregationManager.init(defaultConfigurator);
		AggConfig newAggConfig = new AggConfig.ParamsBuilder().setAllConfigurations(defaultConfigurator).toAggConfig(); 
		AggregationManager.getInstance().addAggregator(PlsosIDs.generateNext(), newAggConfig);
		AggregationManager.getInstance().launchAll();
		Thread.sleep(10000);
		AggregationManager.getInstance().stopAll();
	}
}

