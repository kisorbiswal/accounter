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
	static HashSet<Button> permanent = new HashSet<Button>();

	private HTMLPanel getAppBar() {
		if (appBar == null) {
			initAppBar();
			appBar = HTMLPanel.wrap(Document.get().getElementById("appBar"));
		}
		return appBar;

	}

	private native void initAppBar() /*-{
	//	this.appBar = $wnd.document.getElementById("appBar").winControl;
	}-*/;

	public native void hide() /*-{
		//this.appBar.hide();
	}-*/;

	public native void process(Element e) /*-{
		$wnd.WinJS.UI.process(e);
	}-*/;

	@Override
	public void addPermanent(Button btn) {
		btn.getElement().setAttribute("data-section", "selection");
		permanent.add(btn);
		addToAppBar(btn);
	}

	public void show() {
		for (int x = 0; x < getAppBar().getWidgetCount(); x++) {
			Widget w = getAppBar().getWidget(x);
			if (direct.contains(w) || map.keySet().contains(w)
					|| permanent.contains(w)) {
				continue;
			}
			getAppBar().remove(w);
		}
		for (Widget w : direct) {
			addToAppBar((Button) w);
		}

		for (Widget w : map.keySet()) {
			addToAppBar((Button) w);
		}
	}

	private void addToAppBar(Button btn) {
		Element element = btn.getElement();

		element.removeAttribute("class");

		String title = btn.getTitle();
		String icon = element.getAttribute("data-icon");

		for (int i = 0; i < element.getChildCount(); i++) {
			element.removeChild(element.getChild(i));
		}
		String label = btn.getHTML();
		if (label == null) {
			label = btn.getText();
		}

		element.setAttribute("data-win-control", "WinJS.UI.AppBarCommand");
		String section = element.getAttribute("data-section");
		if (section == null || section.isEmpty()) {
			section = "global";
		}

		element.setAttribute("data-win-options",
				"{id:'cmd" + label.replace(" ", "") + "',label:'" + label
						+ "',icon:'" + icon + "',section:'" + section
						+ "',tooltip:'" + title + "'}");

		element.setInnerHTML(" ");
		element.setInnerText("  ");

		HTMLPanel appBar = getAppBar();
		if (appBar.getWidgetIndex(btn) == -1) {
			appBar.add(btn);
			process(element);
		}
	}

	@Override
	public void add(Button btn) {
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

	@Override
	public void clearDirectButtons() {
		for (Widget widget : direct) {
			getAppBar().remove(widget);
		}
		direct.clear();
	}

	@Override
	public void addPermanent(HasWidgets parent, Button child) {
		addPermanent(child);
	}

}
