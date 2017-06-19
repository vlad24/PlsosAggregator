package com.ruvla.plsos.agg.model.datapersist;

import java.util.Queue;

import com.ruvla.plsos.agg.model.PlsosConstants;
import com.ruvla.plsos.agg.model.aggctrl.AggFilter;
import com.ruvla.plsos.agg.model.aggctrl.Aggregator;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;
import com.ruvla.plsos.agg.model.logging.PlsosLogger;
import com.ruvla.plsos.agg.model.logging.PlsosMessage;

public class Saver implements Runnable {
	protected long sid;
	protected boolean inserting;
	protected boolean blocked;
	protected Aggregator unitsOwner;
	protected DBAdapter dbAdapter;
	protected AggFilter postFilter;
	protected int portionSize;
	protected PlsosLogger logger;

	@Override
	public String toString(){
		return "S" + sid;
	}

	public Saver(PlsosLogger logger, DBConfig dbConfig, Aggregator unitsOwner, AggFilter postFilter, DBAdapter adapter,
			int portionSize) {
		this.sid =Long.parseLong(unitsOwner.getAggid() + "000");
		this.logger = logger;
		this.dbAdapter = adapter;
		this.dbAdapter.init(dbConfig);
		this.postFilter = postFilter;
		this.unitsOwner = unitsOwner;
		this.portionSize = portionSize;
		this.inserting = false;
		this.blocked = false;
	}

	@Override
	public void run() {
		saveUnits();
	}

	public void reset(){
		blocked = false;
	}

	public void stopSaving() {
		logger.logMessage(new PlsosMessage(this.toString(), "Finalizing saving..."));
		inserting = true;
		blocked = true;
		try {

			dbAdapter.beginTransaction();
			Queue<? extends AggDataUnit> unitQueue = unitsOwner.getReadyData(PlsosConstants.UNLIMITED);
			logger.logMessage(new PlsosMessage(System.currentTimeMillis(), "S"+this.sid, PlsosConstants.MESSAGE_CATEGORY_Info, PlsosConstants.MESSAGE_SUBJECT_Saver, "Saving " +  portionSize + "units" ));
			while (!unitQueue.isEmpty()) {
				AggDataUnit unit = unitQueue.poll();
				if (postFilter.match(unit)) {
					insertAggUnitIntoDB(unit);
				}
			}
			dbAdapter.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			dbAdapter.rollbackTransaction();
		} finally {
			dbAdapter.endTransaction();
			inserting = false;
		}
	}

	public void saveUnits() {
		if (!inserting && !blocked) {
			inserting = true;
			try {
				dbAdapter.beginTransaction();
				Queue<? extends AggDataUnit> unitQueue = unitsOwner.getReadyData(portionSize);
				String amount = (portionSize == PlsosConstants.UNLIMITED)? " all held " : portionSize+"";
				logger.logMessage(new PlsosMessage(System.currentTimeMillis(), "S"+this.sid, PlsosConstants.MESSAGE_CATEGORY_Info, PlsosConstants.MESSAGE_SUBJECT_Saver, "Saving " +  amount + "units." ));
				while (!unitQueue.isEmpty() && inserting) {
					AggDataUnit unit = unitQueue.poll();
					if (postFilter.match(unit)) {
						insertAggUnitIntoDB(unit);
					}
				}
				dbAdapter.commitTransaction();
			} catch (Exception e) {
				e.printStackTrace();
				dbAdapter.rollbackTransaction();
			} finally {
				dbAdapter.endTransaction();
				inserting = false;
			}
		} else {
			logger.logMessage(new PlsosMessage(System.currentTimeMillis(), "S"+this.sid, PlsosConstants.MESSAGE_CATEGORY_Info, PlsosConstants.MESSAGE_SUBJECT_Saver, "Wanted to save but COULD NOT!"));
		}
	}

	protected void insertAggUnitIntoDB(AggDataUnit unit) {
		unit.setSaveTime(System.currentTimeMillis());
		dbAdapter.insertAggUnitIntoDB(unit);
	}

}
