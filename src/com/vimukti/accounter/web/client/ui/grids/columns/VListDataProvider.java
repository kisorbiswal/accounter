package com.vimukti.accounter.web.client.ui.grids.columns;

import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.core.ListListener;
import com.vimukti.accounter.web.client.core.VList;

public class VListDataProvider<T> extends ListDataProvider<T> implements
		ListListener<T> {

	/**
	 * Creates new Instance
	 */
	public VListDataProvider() {
	}

	public VListDataProvider(VList<T> vList) {
		vList.addListener(this);
		this.getList().addAll(vList);
	}

	public void setList(VList<T> listToWrap) {
		listToWrap.addListener(this);
		super.setList(listToWrap);
	}

	@Override
	public void onAdd(T e) {
		this.getList().add(e);
	}

	@Override
	public void onRemove(T e) {
		this.getList().remove(e);
	}

}
