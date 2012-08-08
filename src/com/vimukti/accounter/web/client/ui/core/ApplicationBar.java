package com.vimukti.accounter.web.client.ui.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationBar implements IButtonBar {

	static HTMLPanel appBar;
	private static HashMap<Widget, HasWidgets> map = new HashMap<Widget, HasWidgets>();
	static HashSet<Widget> direct = new HashSet<Widget>();

	private HTMLPanel getAppBar() {
		if (appBar == null) {
			initAppBar();
			appBar = HTMLPanel.wrap(Document.get().getElementById("appBar"));
		}
		return appBar;

	}

	private native void initAppBar() /*-{
										//this.appBar = $wnd.document.getElementById("appBar").winControl;
										}-*/;

	public native void show() /*-{
								//this.appBar.show();
								}-*/;

	public native void hide() /*-{
								//this.appBar.hide();
								}-*/;

	@Override
	public void addPermanent(Button btn) {
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
	public void add(Button btn) {
		addPermanent(btn);
		direct.add(btn);
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
		remove(child);
		map.remove(child);
	}

	public void addButton(HasWidgets parent, Button child) {
		map.put(child, parent);
		addPermanent(child);
	}

	@Override
	public void remove(Button widget) {
		getAppBar().remove(widget);
	}

	@Override
	public void clear(HasWidgets group) {
		HashSet<Widget> widgets = new HashSet<Widget>();
		for (Entry<Widget, HasWidgets> entry : map.entrySet()) {
			if (group == entry.getValue()) {
				Widget widget = entry.getKey();
				widgets.add(widget);
			}
		}
		for (Widget widget : widgets) {
			getAppBar().remove(widget);
			map.remove(widget);
		}
	}

	@Override
	public void clear() {
		for (Widget widget : direct) {
			getAppBar().remove(widget);
		}
		direct.clear();

		for (Widget widget : map.keySet()) {
			getAppBar().remove(widget);
		}
		map.clear();

	}

}
