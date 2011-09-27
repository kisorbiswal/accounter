package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class EditColumn<R> {
	
	private EditTable<R> table;

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

	protected String getColumnName() {
		return "";
	}

	public abstract void render(IsWidget widget, RenderContext<R> context);

	public abstract IsWidget getWidget(RenderContext<R> context);

	public EditTable<R> getTable() {
		return table;
	}

	public void setTable(EditTable<R> table) {
		this.table = table;
	}

	public ClientCompanyPreferences getPreferences() {
		return Accounter.getCompany().getPreferences();
	}
}
