package com.ruvla.plsos.agg.prototypes.android.plsosagg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.ruvla.plsos.agg.model.PlsosConstants;
import com.ruvla.plsos.agg.model.datapersist.DBAdapter;
import com.ruvla.plsos.agg.model.datapersist.DBConfig;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;
import com.ruvla.plsos.agg.prototypes.android.plsosagg.dataview.AndroidAggDataUnit;
import com.ruvla.plsos.agg.prototypes.android.plsosagg.dataview.AndroidAggDataUnitModel;

public class AndroidDBAdapter implements DBAdapter {
	@Override
	public void init(DBConfig config) {
		// Log.d(AndroidConstants.LOG_TAG, "%%%Android has inited db in
		// MainActivity - nothing to do here");
	}

	@Override
	public void insertAggUnitIntoDB(AggDataUnit unit) {
		// Log.d(AndroidConstants.LOG_TAG, "%%%Saving
		// unit_"+unit.getEdid()+"("+unit.toString()+")");
		// Log.d(AndroidConstants.LOG_TAG, "%%%Casting
		// AndroidAggDataUnit->AndroidAggDataUnitModel");
		AndroidAggDataUnitModel modelUnit = new AndroidAggDataUnitModel(new AndroidAggDataUnit(unit));
		// Log.d(AndroidConstants.LOG_TAG, "%%%Casted:"+modelUnit.toString());
		modelUnit.getPayload().save();
		modelUnit.save();
		// Log.d(AndroidConstants.LOG_TAG, "%%%Saved");
	}

	@Override
	public void beginTransaction() {
		ActiveAndroid.beginTransaction();
	}

	@Override
	public void commitTransaction() {
		ActiveAndroid.setTransactionSuccessful();
	}

	@Override
	public void rollbackTransaction() {
		// Log.d(AndroidConstants.LOG_TAG, "%%%Rollbacking transaction!!!!!!");
	}

	@Override
	public void endTransaction() {
		ActiveAndroid.endTransaction();
	}

	@Override
	public Queue<? extends AggDataUnit> selectAllAggDataUnits(long aggid, long requestTime, HashMap<String,String> extras) {
		if (aggid != PlsosConstants.GENERIC_ID){
			List<AndroidAggDataUnitModel> androidModelUnits = new Select().from(AndroidAggDataUnitModel.class)
					.where("AGGID = ? and SAVE_TIME <= ?", aggid, requestTime).execute();
			Queue<AndroidAggDataUnit> resultUnits = new LinkedList<AndroidAggDataUnit>();
			for (AndroidAggDataUnitModel androidModelUnit : androidModelUnits) {
				resultUnits.add(androidModelUnit.toCommon());
			}
			return resultUnits;
		}else{
			List<AndroidAggDataUnitModel> androidModelUnits = new Select().from(AndroidAggDataUnitModel.class)
					.where("SAVE_TIME <= ?", requestTime).execute();
			Queue<AndroidAggDataUnit> resultUnits = new LinkedList<AndroidAggDataUnit>();
			for (AndroidAggDataUnitModel androidModelUnit : androidModelUnits) {
				resultUnits.add(androidModelUnit.toCommon());
			}
			return resultUnits;
		}
	}

	@Override
	public void deleteAllDataUnits(long aggid, long requestTime, HashMap<String, String> extras) {
		if (aggid != PlsosConstants.GENERIC_ID){
		new Delete().from(AndroidAggDataUnitModel.class)
				.where("AGGID = ? and SAVE_TIME <= ?", aggid, requestTime).execute();
		}else{
			new Delete().from(AndroidAggDataUnitModel.class).where("SAVE_TIME <= ?", requestTime).execute();
		}
		
	}

	@Override
	public void markSent(long aggid, long timestampUpperBound, HashMap<String, String> extras) {
		if (aggid != PlsosConstants.GENERIC_ID){
			String query = "update AGG_DATA_UNITS set SENT = 1 " + "where SAVE_TIME <= " + timestampUpperBound + " "
					+ "and AGGID = " + aggid;
			ActiveAndroid.execSQL(query);
		}else{
			String query = "update AGG_DATA_UNITS set SENT = 1 " + "where SAVE_TIME <= " + timestampUpperBound;
			ActiveAndroid.execSQL(query);
		}
	}



}
