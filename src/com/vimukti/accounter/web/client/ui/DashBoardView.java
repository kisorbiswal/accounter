package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.portlet.PortletPage;

public class DashBoardView extends BaseHomeView {

	private PortletPage page;

	public DashBoardView() {

	}

	@Override
	public void init() {
		super.init();
		add(createControl());

	}

	private Widget createControl() {
		page = new PortletPage(PortletPage.DASHBOARD);
		return getPage();
	}

	@Override
	protected void onAttach() {
		getPage().refreshWidgets();
		super.onAttach();
	}

	@Override
	protected void onUnload() {
		super.onUnload();
	}

	public void refreshWidgetData() {
	}

	public void showGettingStarted() {
	}

	public void hideGettingStarted() {
	}

	public PortletPage getPage() {
		return page;
	}

	public void setPage(PortletPage page) {
		this.page = page;
	}

}
