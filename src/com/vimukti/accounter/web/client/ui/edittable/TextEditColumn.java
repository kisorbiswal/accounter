package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;

public abstract class TextEditColumn<T> extends EditColumn<T> {

	@Override
	public void render(IsWidget widget, RenderContext<T> context) {
		TextBox box = (TextBox) widget;
		String value = getValue(context.getRow());
		box.setEnabled(isEnable() && !context.isDesable());
		box.setText(value);
	}

	protected boolean isEnable() {
		return true;
	}

	protected abstract String getValue(T row);

	protected abstract void setValue(T row, String value);

	@Override
	public IsWidget getWidget(final RenderContext<T> context) {
		final TextBox textBox = new TextBox();
		configure(textBox);
		textBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				String prevValue = getValue(context.getRow());
				if (prevValue == null) {
					prevValue = "";
				}
				String newValue = textBox.getText().trim();
				if (!prevValue.equals(newValue)) {
					setValue(context.getRow(), newValue);
				}

			}
		});
		return textBox;
	}

	protected void configure(TextBox textBox) {
		textBox.addStyleName("textEdit");
	}

	@Override
	public int getWidth() {
		return -1;
	}
}
