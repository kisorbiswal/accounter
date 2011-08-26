package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.core.AbstractView;

public class BaseHomeView extends AbstractView<Object> {
	private VerticalPanel widgetLayout;

	public BaseHomeView() {
	}

	private void createView() {

		HorizontalPanel mainLayout = new HorizontalPanel();
		mainLayout.setSize("100%", "100%");
		VerticalPanel imagePanel = new VerticalPanel();
		imagePanel.setStyleName(Accounter.constants().imageActionContainer());
		imagePanel.setSpacing(5);

		widgetLayout = new VerticalPanel();
		widgetLayout.setStyleName("finance-portlet");
		widgetLayout.setWidth("100%");
		mainLayout.add(widgetLayout);

		add(mainLayout);
	}

	public VerticalPanel getLeftLayout() {
		return widgetLayout;
	}


	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
	}

	@Override
	public void fitToSize(int height, int width) {
//		this.setHeight(height + "px");

	}


	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}


	@Override
	public void init() {
		createView();
	}

}
