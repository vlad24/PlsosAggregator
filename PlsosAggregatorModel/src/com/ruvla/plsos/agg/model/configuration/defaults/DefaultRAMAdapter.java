package com.ruvla.plsos.agg.model.configuration.defaults;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.ruvla.plsos.agg.model.datapersist.DBAdapter;
import com.ruvla.plsos.agg.model.datapersist.DBConfig;
import com.ruvla.plsos.agg.model.dataview.AggDataUnit;

public class DefaultRAMAdapter implements DBAdapter{

	private HashMap<Long, Queue<AggDataUnit>> storage;
	private boolean busy;
	@Override
	public void init(DBConfig config) {
		busy = false;
		storage = new HashMap<Long, Queue<AggDataUnit>>();
	}

	@Override
	public void insertAggUnitIntoDB(AggDataUnit unit) {
		storage.get(unit.getAggid()).add(unit);
		
	}

	@Override
	public Queue<? extends AggDataUnit> selectAllAggDataUnits(long aggid, long requestTime,	HashMap<String, String> extras) {
		Queue<AggDataUnit> aggUnits = storage.get(aggid);
		Queue<AggDataUnit> result = new LinkedList<AggDataUnit>();
		while(!aggUnits.isEmpty()){
			AggDataUnit unit = aggUnits.peek();
			if (unit.getTimestamp() < requestTime){
				result.add(aggUnits.poll());
			}else{
				break;
			}
		}
		return result;
	}

	@Override
	public void markSent(long aggid, long requestTime, HashMap<String, String> extras) {
		Queue<AggDataUnit> aggUnits = storage.get(aggid);
		for (AggDataUnit unit : aggUnits)
			if (unit.getTimestamp() < requestTime){
				unit.setSent(true);
			}else{
				break;
			}
	}

	@Override
	public void deleteAllDataUnits(long aggid, long requestTime, HashMap<String, String> extras) {
		Queue<AggDataUnit> aggUnits = storage.get(aggid);
		while(!aggUnits.isEmpty()){
			AggDataUnit unit = aggUnits.peek();
			if (unit.getTimestamp() < requestTime){
				aggUnits.poll();
			}else{
				break;
			}
		}
	}

	@Override
	public void beginTransaction() {
		busy = true;
	}

	@Override
	public void commitTransaction() {
		busy = false;
	}

	@Override
	public void rollbackTransaction() {
		busy = false;
	}

	@Override
	public void endTransaction() {
		busy = false;
	}

}
