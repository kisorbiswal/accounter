package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.IsWidget;

public abstract class ComboColumn<T, C> extends EditColumn<T> {

	@Override
	public void render(IsWidget widget, RenderContext<T> context) {
		@SuppressWarnings("unchecked")
		ComboBox<C> box = (ComboBox<C>) widget;
		box.setDesable(!isEnable() || context.isDesable());
		box.setValue(getValue(context.getRow()));
	}

	protected boolean isEnable() {
		return true;
	}

	protected abstract C getValue(T row);

	@Override
	public IsWidget getWidget(final RenderContext<T> context) {
		ComboBox<C> comboBox = new ComboBox<C>();
		AbstractDropDownTable<C> displayTable = getDisplayTable(context.getRow());
		comboBox.setDropDown(displayTable);
		comboBox.setComboChangeHandler(new ComboChangeHandler<C>() {

			@Override
			public void onChange(C newValue) {
				setValue(context.getRow(), newValue);
			}

			@Override
			public void onAddNew(String text) {
				// TODO Auto-generated method stub

			}
		});
		return comboBox;
	}

	protected abstract void setValue(T row, C newValue);

	public abstract AbstractDropDownTable<C> getDisplayTable(T row);

}
