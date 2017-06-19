package com.ruvla.plsos.agg.model;

/**
 * @author Vladislav
 * Class containing internal constants for functioning of Plsos system.
 * Do not modify this file.
 */
public final class PlsosConstants {
	//Codes
	public final static int Event_AggStart     = 900;
	public final static int Event_AggStop      = 901;
	public static final int Event_Collect      = 902;
	public final static int Event_CollectStart = 903;
	public final static int Event_CollectStop  = 904;
	public final static int Event_Detect       = 905;
	public static final int Event_Error        = 906;
	public static final int UNIT_TYPE_FINISH   = 230;
	public static final int UNIT_TYPE_Common   = 240;
	// Logging
	public static final String MESSAGE_CATEGORY_Info           = "INF";
	public static final String MESSAGE_CATEGORY_Error          = "ERR";
	public static final String MESSAGE_CATEGORY_OTHER          = "OTH";
	public static final String MESSAGE_SUBJECT_AggStart        = "AGG_START";
	public static final String MESSAGE_SUBJECT_AggStop         = "AGG_STOP ";
	public static final String MESSAGE_SUBJECT_CollectionStart = "COL_START";
	public static final String MESSAGE_SUBJECT_CollectionStop  = "COL_STOP ";
	public static final String MESSAGE_SUBJECT_Detection       = "DETECTION";
	public static final String MESSAGE_SUBJECT_Error           = "!!ERROR!!";
	public static final String MESSAGE_SUBJECT_Saver           = "SAVING";
	public static final String MESSAGE_SUBJECT_Fetching        = "FETCHING";
	public static final String MESSAGE_SUBJECT_OTHER           = "OTHER";
	public static final long GENERIC_ID = 0;
	public static final String FORMAT_JSON = "json";
	public static final String FORMAT_RAW = "raw";
	//Plsos semantic constants
	public static final String CONFIRMATION = "CONF";
	public static final String EMPTY        = "";
	public static final int NOTHING         = -1;
	public static final int NO_THRESHOLD    = Integer.MAX_VALUE;
	public static final int UNLIMITED       = Integer.MAX_VALUE;
	//Plsos defaults
	public static final int PLSOS_DEFAULT_THREAD_COUNT            = 1;
	public static final int PLSOS_DEFAULT_DETECTOR_REFRESH_PERIOD = 1;
	public static final int PLSOS_DEFAULT_SAVER_REFRESH_PERIOD    = 1;
	public static final int PLSOS_DEFAULT_PORTION                 = UNLIMITED;
	public static final int PLSOS_DEFAULT_AGG_CAPACITY            = 10;
	public static final int PLSOS_DEFAULT_UNIT_THRESHOLD          = NO_THRESHOLD;
	public static final String PLSOS_DEFAULT_DBNAME               = "plsos_agg_db";
	public static final String PLSOS_DEFAULT_HOST                 = "127.0.0.1";
	public static final String PLSOS_DEFAULT_PORT                 = "5443";
	public static final String PLSOS_DEFAULT_USER                 = "plsos";
	public static final String PLSOS_DEFAULT_PASSWORD             = "plsos";
	public static final String PLSOS_DEFAULT_AUTH_VALUE           = "";
	public static final String PLSOS_DEFAULT_CHARSET              = "UTF_8";
	public static final String PLSOS_DEFAULT_UPLOAD_URL           = "http://127.0.0.1:80";
	public static final String PLSOS_DEFAULT_FORMAT               = FORMAT_RAW;
	
}
