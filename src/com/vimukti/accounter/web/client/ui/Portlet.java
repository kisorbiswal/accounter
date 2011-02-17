package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.widgets.WorkbenchPanel;

/**
 * 
 * @author Gajendra Choudhary
 * @modified by Raj Vimal
 * 
 */

public abstract class Portlet extends WorkbenchPanel {
	private HTML title = new HTML();
	private String name;
	protected Label all;
	private VerticalPanel vPanel;
	private int previousIndex;
	public HTML refresh;
	private int row;
	private int column;

	public Portlet(String title) {
		super(title);
		vPanel = new VerticalPanel();
		vPanel.setSize("100%", "100%");
		this.setWidth("100%");
		this.setHeight("124");

		addStyleName("portlet");

		createHeaderControls();
		super.add(vPanel);

	}

	void createHeaderControls() {
		HorizontalPanel windowControlLayout = new HorizontalPanel();
		windowControlLayout.setStyleName("tool-box");

		refresh = new HTML();
		refresh.setStyleName("refresh");
		refresh.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Write RPC Call to get Data & set it to Grid
				refresh.setStyleName("loading");
				refreshClicked();
			}
		});
		HTML link = new HTML();
		link.setStyleName("link");
		link.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Write Action to be performed when Link has been clicked
				linkClicked();
			}
		});
		windowControlLayout.add(refresh);
		// windowControlLayout.add(link);

		addToMain(windowControlLayout);

	}

	public abstract void linkClicked();

	public abstract void refreshClicked();

	@Override
	public void setTitle(String title) {
		super.setTitle(title);
		this.title.setHTML(title);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void add(Widget w) {
		this.vPanel.add(w);
	}

	public void fitToSize(int width, int height) {
		width = Math.max(width, 300);
		height = Math.max(height, 100);
		this.setWidth(width + "px");
		this.setHeight(height + "px");
		// setGridWidth(width - 10, height - 40);
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void setPreviousIndex(int widgetIndex) {
		this.previousIndex = widgetIndex;

	}

	public int getPreviousIndex() {
		return this.previousIndex;
	}

}
