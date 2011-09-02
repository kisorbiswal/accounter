package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.ui.widgets.WorkbenchPanel;

public abstract class DashBoardPortlet extends WorkbenchPanel {

	private ClientCompanyPreferences preferences = Global.get().preferences();
	private HTML title = new HTML();
	private String name;
	protected Label all;
	public VerticalPanel body;
	private int previousIndex;
	// public HTML gotoText;
	public HTML help;
	// Label gotoText;
	private int row;
	private int column;
	public String titleName;
	public ScrollPanel panel;

	public DashBoardPortlet(String title) {
		super(title);
		this.titleName = title;
		// createHandlerForTitle();
		panel = new ScrollPanel();
		body = new VerticalPanel();
		body.setStyleName("portlet-body");
		this.setSize("100%", "100%");
		// this.setHeight("124");

		addStyleName("portlet");

		createHeaderControls();
		// createBody();

		// panel.add(body);
		// panel.setHeight("100%");
		// panel.getElement().getStyle().setPadding(0, Unit.PX);
		// panel.setSize("98.9%", "100%");
		panel.add(body);
		if (!(this instanceof ExpenseClaimPortlet || this instanceof GettingStartedPortlet))
			panel.setHeight("297px");
		panel.setWidth("100%");
		super.add(panel);

	}

	// void createHandlerForTitle() {
	// this.title.addMouseOverHandler(new MouseOverHandler() {
	//
	// @Override
	// public void onMouseOver(MouseOverEvent event) {
	// title.getElement().getStyle().setCursor(Cursor.POINTER);
	// }
	// });
	// this.title.addClickHandler(new ClickHandler() {
	//
	// @Override
	// public void onClick(ClickEvent event) {
	// System.out.println();
	// }
	// });
	// }

	void createHeaderControls() {
		HorizontalPanel windowControlLayout = new HorizontalPanel();
		// HorizontalPanel hPanel = new HorizontalPanel();
		windowControlLayout.setStyleName("tool-box");

		// gotoText = new Label(getGoToText());
		// gotoText.setStyleName("portletLabel");
		// gotoText.getElement().getStyle().setTextDecoration(
		// TextDecoration.UNDERLINE);
		// gotoText.addMouseOverHandler(new MouseOverHandler() {
		//
		// @Override
		// public void onMouseOver(MouseOverEvent event) {
		// gotoText.getElement().getStyle().setCursor(Cursor.POINTER);
		//
		// }
		// });
		// gotoText.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// goToClicked();
		// }
		// });
		help = new HTML();
		help.setStyleName("link");
		help.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				helpClicked();
			}
		});
		// windowControlLayout.add(help);
		// windowControlLayout.add(gotoText);
		// hPanel.add(help);
		// hPanel.add(windowControlLayout);

		addToMain(windowControlLayout);

	}

	public abstract void helpClicked();

	public abstract void goToClicked();

	public abstract String getGoToText();

	public abstract void createBody();

	public void refreshWidget() {

	}

	@Override
	public void setTitle(String title) {
		super.setTitle(title);
		this.title.setHTML(title);
		this.title.setStyleName("portletLabel");
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void add(Widget w) {
		this.body.add(w);
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

	public ClientCompany getCompany() {
		return Accounter.getCompany();

	}

	public ClientCompanyPreferences getPreferences() {
		return preferences;
	}

	public String getDecimalCharacter() {
		return getPreferences().getDecimalCharacter();
	}

	public String amountAsString(Double amount) {
		return DataUtils.getAmountAsString(amount);
	}
}
