package com.vimukti.accounterbb.ui;

import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.text.TextFilter;

import com.vimukti.accounterbb.utils.BlackBerryEditField;

public class Sample extends MainScreen{
	 ButtonField titleField;
	 ButtonField backField;
	public Sample()
	{
		BlackBerryEditField bb= new BlackBerryEditField();
		bb.setFilter(	TextFilter.get(TextFilter.REAL_NUMERIC));
	
		add(bb);
		
		
	}

}
