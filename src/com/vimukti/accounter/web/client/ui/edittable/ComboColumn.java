package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.IsWidget;

public abstract class ComboColumn<T, C> extends EditColumn<T> {

	@Override
	public void render(IsWidget widget, RenderContext<T> context) {
		@SuppressWarnings("unchecked")
		ComboBox<T, C> box = (ComboBox<T, C>) widget;
		box.setDesable(!isEnable() || context.isDesable());
		T row = context.getRow();
		C value = getValue(row);
		box.setValue(value);
	}

	protected boolean isEnable() {
		return true;
	}

	protected abstract C getValue(T row);

	@Override
	public IsWidget getWidget(RenderContext<T> context) {
		ComboBox<T, C> comboBox = new ComboBox<T, C>();
		AbstractDropDownTable<C> displayTable = getDisplayTable(context
				.getRow());
		comboBox.setDropDown(displayTable);
		comboBox.setComboChangeHandler(new ComboChangeHandler<T, C>() {

			@Override
			public void onChange(T row, C newValue) {
				setValue(row, newValue);
			}

			@Override
			public void onAddNew(String text) {
				// TODO Auto-generated method stub

			}
		});
		comboBox.setRow(context.getRow());
		return comboBox;
	}

	protected abstract void setValue(T row, C newValue);

	public abstract AbstractDropDownTable<C> getDisplayTable(T row);

}
