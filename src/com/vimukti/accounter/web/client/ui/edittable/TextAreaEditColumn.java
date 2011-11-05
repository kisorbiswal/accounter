package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBoxBase;

public abstract class TextAreaEditColumn<T> extends TextEditColumn<T> {

	@Override
	protected TextBoxBase createWidget() {
		final TextArea textArea = new TextArea();
		textArea.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				updateHeight(textArea.getElement());
			}
		});

		return textArea;
	}

	private native void updateHeight(Element text)/*-{
		if (text.scrollHeight > text.clientHeight)
			text.style.height = text.scrollHeight + "px";
	}-*/;

}
