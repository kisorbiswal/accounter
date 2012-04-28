package com.vimukti.accounter.web.client.ui;

import java.util.HashMap;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.portlet.DragAndDropEnabler;
import com.vimukti.accounter.web.client.portlet.DragAndDropInput;
import com.vimukti.accounter.web.client.portlet.PortletColumn;
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
		new Timer() {

			@Override
			public void run() {
				enableDragAndDrop();
			}
		}.schedule(10 * 1000);

		return getPage();
	}

	private void enableDragAndDrop() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				HashMap<Widget, Widget> portlets = new HashMap<Widget, Widget>();
				for (PortletColumn column : page.getColumns()) {
					for (Portlet portlet : column.getPortlets()) {
						portlets.put((Widget) portlet, portlet.getHeader());
					}
				}
				DragAndDropInput input = new DragAndDropInput(page, page
						.getColumns(), portlets,
						new Callback<Boolean, Boolean>() {

							@Override
							public void onSuccess(Boolean result) {
								page.onDragEnd();
							}

							@Override
							public void onFailure(Boolean reason) {
							}
						});

				DragAndDropEnabler dNdEnabler = new DragAndDropEnabler(input);
				dNdEnabler.enable();

			}

			@Override
			public void onFailure(Throwable reason) {
				// TODO Auto-generated method stub

			}
		});
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
