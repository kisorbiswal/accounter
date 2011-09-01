package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public abstract class EditColumn<R> {
	public boolean onChange(R row) {
		return false;

	}

	/**
	 * return width of this cell, or -1 to take available space
	 * 
	 * @return
	 */
	public abstract int getWidth();

	public IsWidget getHeader() {
		Label columnHeader = new Label(getColumnName());
		return columnHeader;
	}

	private String getColumnName() {
		return "";
	}

	public abstract void render(IsWidget widget, RenderContext<R> context);

	public abstract IsWidget getWidget(RenderContext<R> context);

}
