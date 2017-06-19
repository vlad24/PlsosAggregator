package com.ruvla.plsos.agg.model;

import java.util.concurrent.atomic.AtomicLong;

public class PlsosIDs {
	private static AtomicLong id;
	public static long generateNext(){
		return id.getAndIncrement();
	}
}
