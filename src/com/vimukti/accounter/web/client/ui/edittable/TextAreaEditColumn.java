package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBoxBase;

public abstract class TextAreaEditColumn<T> extends TextEditColumn<T> {

	@Override
	protected TextBoxBase createWidget() {
		return new TextArea();
	}

}
