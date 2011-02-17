package com.vimukti.accounter.web.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class AutoFillWidget extends ComplexPanel{
//	private static final String BOTTOM = "bottom";
//	private static final String TOP = "top";
//	private static final String LEFT = "left";

	public AutoFillWidget(){
		Element element=DOM.createDiv();
//		element.addClassName("autoFill");
		this.setElement(element);
	}
//
//	public void setBottom(String value) {
//		getElement().getStyle().setProperty(BOTTOM, value);
//	}
//
//	public void setTop(String value) {
//		getElement().getStyle().setProperty(TOP, value);
//	}
//
//
//
//	public void setLeft(String value) {
//		getElement().getStyle().setProperty(LEFT, value);
//	}

	@Override
	public void add(Widget child) {
	    add(child,getElement());
	}

}
