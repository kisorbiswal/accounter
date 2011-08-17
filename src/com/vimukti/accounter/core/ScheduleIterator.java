package com.vimukti.accounter.core;

import java.util.Date;

/**
 * This is for scheduling.
 * 
 * @author vimukti3
 * 
 */
public interface ScheduleIterator {

	/**
	 * Gives next scheduling date/time.
	 * 
	 * @return
	 */
	Date next();
}
