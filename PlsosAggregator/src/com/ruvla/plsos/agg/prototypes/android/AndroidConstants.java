package com.ruvla.plsos.agg.prototypes.android;

public class AndroidConstants {
	public static final String PARAMKEY_Server = "pls_agg_server";
	public static final String PARAMKEY_ErrorComment = "pls_agg_error_comment";
	public static final String PARAMKEY_LogString = null;
	public static final String PARAMKEY_Status = "pls_db_inited";
	public static final String PARAMKEY_Factory = "pls_factory";
	public static final String PARAMKEY_DetectorConfig = "pls_refresh_detector";
	public static final String PARAMKEY_SaverConfig = "pls_refresh_saver";
	public static final String PARAMKEY_ThreadCount = "pls_threads";
	public static final String PARAMKEY_TranCount = "pls_tran";
	public static final String PARAMKEY_Event = "pls_agg_eventtype";
	public static final String PARAMKEY_Message = "pls_message";
	public static final String PARAMKEY_Aggid = "pls_aggid";
	public final static String PARAMKEY_Action = "pls_agg_action";
	public final static String PARAMKEY_AggParams = "pls_agg_value";
	public static final String PARAMKEY_ManipulatorConfig = "pls_agg_man_con";
	public static final int PARAM_ActionStop = 11;
	public final static int PARAM_ActionStart = 12;
	public static final int PARAM_ActionSend = 13;
	public final static int PARAM_ActionGetLog = 14;
	public static final int PARAM_ActionManipulatorInit = 15;
	public static final int PARAM_ActionManipulatorUpload = 16;
	public static final int PARAM_ActionManipulatorStop = 17;
	public static final int PARAM_ActionSetParams = 18;
	public static final long ANDROID_SINGLE_AGG_ID = 0;
	public static final int ANDROID_DETECT_REF = 12;
	public static final String LOG_DATE_FORMAT = "yy.MM.dd-kk:mm:ss";
	public static final String LOG_FILE_NAME = "plsos_log";
	public static final String PLSOS_LOG_DIR = "PlsosAggLogs";
	public static final String LOG_TAG = "pls_tag";
	public static final String PARAMKEY_Log = "pls_agg_log";
	public final static String FILTER_BROADCAST_TO_GUI = "com.ruvla.plsos.agg.broadcastToMainGUI";
}
