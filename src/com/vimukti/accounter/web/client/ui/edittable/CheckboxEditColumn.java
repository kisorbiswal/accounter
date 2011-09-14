package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;

public abstract class CheckboxEditColumn<T> extends EditColumn<T> {

	@Override
	public int getWidth() {
		return 10;
	}

	@Override
	public IsWidget getHeader() {
		CheckBox box = new CheckBox();
		box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				boolean value = event.getValue();
				onHeaderValueChanged(value);
			}
		});
		box.setEnabled(isEnable());
		return box;
	}

	protected abstract void onHeaderValueChanged(boolean value);

	@Override
	public void render(IsWidget widget, RenderContext<T> context) {
		CheckBox box = (CheckBox) widget;
		box.setEnabled(isEnable() && !context.isDesable());
	}

	protected boolean isEnable() {
		return true;
	}

	@Override
	public IsWidget getWidget(final RenderContext<T> context) {
		CheckBox box = new CheckBox();
		box.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				boolean value = event.getValue();
				onChangeValue(value, context.getRow());
				int index = getTable().getAllRows().indexOf(context.getRow()) + 1;
				if (value) {
					context.getRowFormatter()
							.addStyleName(index, "selected");
				} else {
					context.getRowFormatter().removeStyleName(index, "selected");
				}
			}
		});
		return box;
	}

	protected abstract void onChangeValue(boolean value, T row);

}
