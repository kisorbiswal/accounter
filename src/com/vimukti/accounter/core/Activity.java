package com.vimukti.accounter.core;

import java.sql.Timestamp;


public class Activity {

	private long id;
	
	private User user;
	
	private ActivityType type;
	
	private Timestamp time;
	
	private int objectType;
	
	private long objectID;
	
	private String name;
	
	private FinanceDate date;
	
	private Double amount;
	
}
