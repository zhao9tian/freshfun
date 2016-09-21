package com.quxin.freshfun.spring.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
	
	private static final ThreadLocal<DynamicDataSourceGlobal> contextHolder = new ThreadLocal<>();

	@Override
	protected Object determineCurrentLookupKey() {
		return getCurrentLookupKey();
	}
	
	public static DynamicDataSourceGlobal getCurrentLookupKey(){
		return contextHolder.get();
	}
	
	public static void setCurrentLookupKey(DynamicDataSourceGlobal currentLookupKey){
		contextHolder.set(currentLookupKey);
	}
}
