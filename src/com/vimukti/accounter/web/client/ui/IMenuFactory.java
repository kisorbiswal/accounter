package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.IsWidget;
import com.vimukti.accounter.web.client.ui.core.Action;

public interface IMenuFactory {

	public static interface IMenu extends IsWidget{
		void addItem(String text, IMenu menu);
		
		void addItem(Action<?> action);
		
		void addSeparator();
	}

	IMenu createMenuBar();

}
