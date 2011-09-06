package com.vimukti.accounter.web.client.ui.edittable;

public interface ComboChangeHandler<T> {
	public void onChange(T newValue);

	public void onAddNew(String text);
}
