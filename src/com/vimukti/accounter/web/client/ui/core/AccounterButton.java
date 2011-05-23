package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.FinanceApplication;

public class AccounterButton extends Button {

	public static final int ADD_BUTTON = 1;
	private int type;

	public AccounterButton() {
	}

	public AccounterButton(SafeHtml html) {
		super(html);
	}

	public AccounterButton(String html) {
		super(html);
		// TODO Auto-generated constructor stub
	}

	public AccounterButton(Element element) {
		super(element);
		// TODO Auto-generated constructor stub
	}

	public AccounterButton(SafeHtml html, ClickHandler handler) {
		super(html, handler);
		// TODO Auto-generated constructor stub
	}

	public AccounterButton(String html, ClickHandler handler) {
		super(html, handler);
		// TODO Auto-generated constructor stub
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (enabled) {
			if (type == ADD_BUTTON) {
				enabledAddButton();
			} else {
				enabledButton();
			}

		} else {
			if (type == ADD_BUTTON) {
				disabledAddButton();
			} else {
				disabledButton();
			}
		}
	}

	public void enabledAddButton() {
		this.getElement().getParentElement().setClassName("add-button");

		Element addseparator = DOM.createSpan();
		addseparator.addClassName("add-separator");
		DOM.appendChild(this.getElement(), addseparator);

		Element addimage = DOM.createSpan();
		addimage.addClassName("add-image");
		DOM.appendChild(this.getElement(), addimage);

		ThemesUtil.addDivToButton(this, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "add-right-image");
	}

	public void enabledButton() {
		this.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(this, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");
	}

	public void disabledAddButton() {
		if (this.getElement().getParentElement().getClassName() != null) {
			this.getElement().getParentElement().removeClassName("add-button");
			if (this.getText() != null) {
				ThemesUtil.removeDivToButton(this);
			}

		}
	}

	public void disabledButton() {
		if (this.getElement().getParentElement().getClassName() != null) {
			this.getElement().getParentElement().removeClassName("ibutton");
			if (this.getText() != null) {
				ThemesUtil.removeDivToButton(this);
			}

		}

	}

}
