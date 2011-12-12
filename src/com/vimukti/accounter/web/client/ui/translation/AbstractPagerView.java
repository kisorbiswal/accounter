package com.vimukti.accounter.web.client.ui.translation;

import java.util.List;

import com.vimukti.accounter.web.client.ui.AbstractBaseView;

public abstract class AbstractPagerView<T> extends AbstractBaseView<T> {
	protected Pager pager;
	protected boolean hasMoreRecords;
	private List<T> t;

	public AbstractPagerView() {
		super();
		createControls();
	}

	protected abstract void createControls();

	public abstract void updateListData();

	protected void updateData(List<T> t) {
		this.t = t;
		pager.updateRangeData();
	}

	public void refreshPager() {
		if (pager != null) {
			pager.refreshPager();
		}
	}

	public int getDataSize() {
		if (t != null) {
			return t.size();
		} else {
			return 0;
		}
	}

}
