package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public interface IButtonBar {

	public void add(Button widget);

	public void remove(Button widget);

	public void add(Button widget, HorizontalAlignmentConstant alignment);

	/**
	 * Adds this ButtonBar to given Widget
	 * 
	 * @param w
	 */
	public void addTo(HasWidgets w);

	public Widget asWidget();

	public void setDisabled(boolean disable);

	public void removeButton(HasWidgets parent, Button child);

	public void addButton(HasWidgets parent, Button child);
}
