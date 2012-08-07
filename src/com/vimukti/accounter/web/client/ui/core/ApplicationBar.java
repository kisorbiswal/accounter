package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationBar implements IButtonBar {

	HTMLPanel appBar;

	private HTMLPanel getAppBar() {
		if (appBar == null) {
			initAppBar();
			appBar = HTMLPanel.wrap(Document.get().getElementById("appBar"));
		}
		return appBar;

	}

	private native void initAppBar() /*-{
		this.appBar = $wnd.document.getElementById("appBar").winControl;
	}-*/;

	public native void show() /*-{
		this.appBar.show();
	}-*/;

	public native void hide() /*-{
		this.appBar.hide();
	}-*/;

	@Override
	public void add(Button btn) {
		Element element = btn.getElement();
		element.setAttribute("data-win-control", "WinJS.UI.AppBarCommand");
		element.setAttribute("data-win-options", "{id:'cmd'" + btn.getTitle()
				+ ",label:'" + btn.getText() + "',icon:'add',"
				+ "               section:'global',tooltip:'" + btn.getTitle()
				+ "'}");
		HTMLPanel appBar = getAppBar();
		appBar.add(btn);
		show();
	}

	@Override
	public void add(Button btn, HorizontalAlignmentConstant alignment) {
		add(btn);
	}

	@Override
	public void addTo(HasWidgets w) {
		// NOTHING TO DO
	}

	@Override
	public Widget asWidget() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public void setDisabled(boolean disable) {
		// TODO Auto-generated method stub

	}

	public void removeButton(HasWidgets parent, Button child) {
		add(child);
	}

	public void addButton(HasWidgets parent, Button child) {
		add(child);
	}

	@Override
	public void remove(Button widget) {
		getAppBar().remove(widget);
	}

}
