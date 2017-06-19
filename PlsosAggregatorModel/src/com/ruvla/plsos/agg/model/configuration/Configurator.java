package com.ruvla.plsos.agg.model.configuration;

import java.io.Serializable;

import com.ruvla.plsos.agg.model.aggctrl.AggFilter;
import com.ruvla.plsos.agg.model.aggctrl.AggProcessor;
import com.ruvla.plsos.agg.model.aggctrl.Aggregator;
import com.ruvla.plsos.agg.model.aggctrl.Collector;
import com.ruvla.plsos.agg.model.aggctrl.Detector;
import com.ruvla.plsos.agg.model.aggctrl.EdgeDevice;
import com.ruvla.plsos.agg.model.configuration.configpacks.DataManipulatorConfig;
import com.ruvla.plsos.agg.model.configuration.configpacks.DetectorConfig;
import com.ruvla.plsos.agg.model.configuration.configpacks.UploadConfig;
import com.ruvla.plsos.agg.model.configuration.defaults.DefaultAggDataFilter;
import com.ruvla.plsos.agg.model.configuration.defaults.DefaultJsonOutputFormatter;
import com.ruvla.plsos.agg.model.configuration.defaults.DefaultLoggingAggListener;
import com.ruvla.plsos.agg.model.configuration.defaults.DefaultPlsosLogger;
import com.ruvla.plsos.agg.model.configuration.defaults.DefaultProcessor;
import com.ruvla.plsos.agg.model.datapersist.DBAdapter;
import com.ruvla.plsos.agg.model.datapersist.Saver;
import com.ruvla.plsos.agg.model.datasend.DataManipulator;
import com.ruvla.plsos.agg.model.datasend.OutputFormatter;
import com.ruvla.plsos.agg.model.datasend.Sender;
import com.ruvla.plsos.agg.model.events.AggListener;
import com.ruvla.plsos.agg.model.logging.PlsosLogger;

/**
 * @author Vladislav
 * 
 * Class used to control instantiation and control interchanging of different components of Plsos system. 
 */
@SuppressWarnings("serial")
public interface Configurator extends Serializable {
	
	/**
	 * Instantiate DBAdapter.
	 * DBAdapter - a component used to connect to some data storage.
	 * @return adapter that will be used by other components(e.g. saver) to store collected data.
	 */
	public abstract DBAdapter instantiateDBAdapter();

	/**
	 * Instantiate Sender.
	 * Sender is a component that is responsible for transferring collected data over network to the remote server.
	 * @param owner data manipulator that holds this sender
	 * @param config configuration object used to parameterize future Sender
	 * @return a new Sender
	 */
	public abstract Sender instantiateSender(DataManipulator owner, UploadConfig config);


	/**
	 * Instantiate a new Detector.
	 * Detector - component that is responsible for detecting new edge devices around aggregator device.
	 * Detector can parameterized to work in polling mode(with some refresh period after which is scans for discoverable devices) or asynchronous mode(meaning that event model is used here).
	 * @param owner aggregator that holds the instantiated detector
	 * @param config parameters for this detector.
	 * @return new Detector
	 */
	public abstract Detector instantiateDetector(Aggregator owner, DetectorConfig config);

	/**
	 * Instantiate a new Collector.
	 * A Collector can be thought as a thread which performs serial communication operations with edge device.
	 * It established connection with an edge device, performs handshake, collects the data and finished the communication.
	 * @param owner aggregator that holds this collector
	 * @param colid id of new collector
	 * @param targetDevice device the new collector will communicate with
	 * @return new Collector
	 */
	public abstract Collector instantiateCollector(Aggregator owner, long colid, EdgeDevice targetDevice);

	/**
	 * Instantiate a new Logger.
	 * Logger is a component that is used to log all the messages that the Plsos system generates.
	 * Common logger produced by this method is used by an AggregationManager and set to all aggregators that is held by it.
	 * It works with I/O, creates logs, write to logs, read from logs, close them and handle multiple access to logs.
	 * A {@link DefaultPlsosLogger} is returned by default.
	 * @return a new Logger
	 */
	public abstract PlsosLogger instantiateLogger() ;

	/**
	 * Instantiate a new AggListener.
	 * AggListener reacts to different events and logs own actions.
	 * If the method is not overridden a {@link DefaultLoggingAggListener} is returned.
	 * @param aggregator aggregator which the listener is listening to. Can be null if listener behavior does not depend on observed aggregator.
	 * @param logger logger through which AggListener can log own actions and reactions.
	 * @return new AggListener
	 */
	public abstract AggListener instantiateAggListener(Aggregator owner, PlsosLogger logger) ;

	/**
	 * Instantiate an AggFilter to filter just collected data.
	 * AggFilter is a component which can drop some input data units if they does not meet some conditions.
	 * Pre filter is used to filter the data that an Aggregator gets right from a device with the intention not to let the data unit be processed by units that may accidentally save them.
	 * If the method is not overridden a {@link DefaultAggDataFilter} is returned.    
	 * @return a new Pre filter
	 */
	public abstract AggFilter instantiatePreFilter() ;


	/**
	 * Instantiate an AggFilter to filter data to be saved.
	 * AggFilter is a component which can drop some input data units if they does not meet some conditions.
	 * Post filter is used to filter the data that {@link Saver} is intending to save. 
	 * If the method is not overridden a {@link DefaultAggDataFilter} is returned.    
	 * @return a new Post filter
	 */
	public abstract AggFilter instantiatePostFilter() ;

	/**
	 * Instantiate an {@link OutputFormatter}
	 * OutputFormatter is a component that is responsible for serializing data units that is about to be sent to remote server.
	 * If the method is not overridden a {@link DefaultJsonOutputFormatter} is returned.
	 * @return a new OutputFormatter
	 */
	public abstract OutputFormatter instantiateOutputFormatter() ;
	
	
	/**
	 * Instantiate a new {@link DataManipulator}
	 * DataManipulator is a component that lets you control collected data extractions from database, uploadings of data to remote servers and similar.
	 * @param config configuration of DataManipulator. See {@link DataManipulatorConfig} for more information
	 * @return a new Manipulator
	 */
	public abstract DataManipulator instantiateManipulator(PlsosLogger logger, DataManipulatorConfig config) ;


	/**
	 * Instantiate an {@link AggProcessor} to transform data to be saved.
	 * AggProcessor is a component which can perform some transformations on the input units and return new ones.
	 * Such processors can have complex logic of processing units in the opposite of {@link AggFilter}.
	 * Pre processor is used to process data when it is transmitted from {@link Aggregator} to {@link Saver}.  
	 * If the method is not overridden a {@link DefaultProcessor} is returned.    
	 * @return a new AggProcessor
	 */
	public abstract AggProcessor instantiatePreProcessor() ;
	
	/**
	 * Instantiate an {@link AggProcessor} to transform data to be sent.
	 * AggProcessor is a component which can perform some transformations on the input units and return new ones.
	 * Such processors can have complex logic of processing units in the opposite of {@link AggFilter}.
	 * Post processor is used to process data when it is transmitted from {@link Saver} to remote server.  
	 * If the method is not overridden a {@link DefaultProcessor} is returned.    
	 * @return a new AggProcessor
	 */
	public abstract AggProcessor instantiatePostProcessor() ;

	/**
	 * Instantiate a {@link ConfigurationProvider}.
	 * ConfigurationProvider is a component which provide other components with global settings.
	 * These providers are highly used by constructors of classes in {@link com.ruvla.plsos.agg.model.aggctrl.configurations}.
	 * Providers allow you to change settings dynamically during runtime and create new components which are differently configured.
	 * If the method is not overridden a {@link DefaultProcessor} is returned.    
	 * @return a new AggProcessor
	 */
	public abstract ConfigurationProvider instantiateConfigProvider();

}
