package com.vimukti.accounter.web.client.ui.edittable;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class DateColumn<T> extends EditColumn<T> {

	/**
	 * To hold the date format of DatePicker
	 */
	private final DateTimeFormat format;

	public DateColumn() {
		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US)
			format = DateTimeFormat.getFormat(Accounter.constants()
					.dateFormatWithSlashStartsWithMonth());
		else
			format = DateTimeFormat.getFormat(Accounter.constants()
					.dateFormatWithSlash());
	}

	public DateColumn(String dateFormat) {
		format = DateTimeFormat.getFormat(dateFormat);
	}

	@Override
	public int getWidth() {
		return -1;
	}

	@Override
	public void render(IsWidget widget, RenderContext<T> context) {
		DateBox box = (DateBox) widget;
		ClientFinanceDate value = getValue(context.getRow());
		box.setEnabled(isEnable() && !context.isDesable());
		if (value == null || value.getDate() == 0) {
			box.setValue(new ClientFinanceDate().getDateAsObject());
		} else {
			box.setValue(value.getDateAsObject());
		}
	}

	protected boolean isEnable() {
		return true;
	}

	@Override
	public IsWidget getWidget(final RenderContext<T> context) {
		final DateBox dateBox = new DateBox();
		configure(dateBox);
		dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				ClientFinanceDate prevValue = getValue(context.getRow());
				if (prevValue == null) {
					prevValue = new ClientFinanceDate();
				}
				ClientFinanceDate newValue = new ClientFinanceDate(dateBox
						.getValue());
				if (!prevValue.equals(newValue)) {
					setValue(context.getRow(), newValue);
				}
			}
		});
		dateBox.setFormat(new DateBox.DefaultFormat(format));
		dateBox.setValue(new ClientFinanceDate().getDateAsObject());
		return dateBox;
	}

	protected void configure(DateBox dateBox) {
		dateBox.addStyleName("textEdit");
	}

	protected abstract ClientFinanceDate getValue(T row);

	protected abstract void setValue(T row, ClientFinanceDate value);
}
