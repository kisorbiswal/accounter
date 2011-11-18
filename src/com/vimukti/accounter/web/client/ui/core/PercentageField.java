package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.WidgetWithErrors;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class PercentageField extends TextItem {
	private Double percentage;
	private WidgetWithErrors errorsWidget;
	AccounterMessages messages = GWT
			.create(AccounterMessages.class);

	public PercentageField(WidgetWithErrors errorsWidget, final String name) {
		this.errorsWidget = errorsWidget;
		setName(name);
		setTitle(name);
		addBlurHandler(getBlurHandler());
		setPercentage(0.0);
		((TextBox) getMainWidget()).setTextAlignment(TextBoxBase.ALIGN_RIGHT);
	}

	public ChangeHandler getChangedHandler() {
		ChangeHandler handler = new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String strValue = getValue().toString();

				if (strValue.contains("%")) {
					strValue = strValue.replaceAll("%", "");
				}
				try {
					if (strValue.length() != 0) {
						Double enteredPercentageValue = Double
								.parseDouble(strValue);
						if (DecimalUtil.isLessThan(enteredPercentageValue, 0)
								|| DecimalUtil.isLessThan(
										enteredPercentageValue, 100)) {
							Accounter.showError(messages
									.invalidateEntry());
						}
						if (enteredPercentageValue != null) {
							setPercentage(enteredPercentageValue);
						}
					}
				} catch (Exception e) {
					Accounter.showError(messages.invalidEntry());
					setPercentage(0.0);
				}

			}
		};
		return handler;
	}

	private BlurHandler getBlurHandler() {

		BlurHandler blurHandler = new BlurHandler() {

			Object value = null;

			public void onBlur(BlurEvent event) {

				String strValue = getValue().toString();

				if (strValue.contains("%")) {
					strValue = strValue.replaceAll("%", "");
				}
				try {
					if (strValue.length() != 0) {
						Double enteredPercentageValue = Double
								.parseDouble(strValue);
						if (enteredPercentageValue != null) {
							if (DecimalUtil.isLessThan(enteredPercentageValue,
									0)) {
								// BaseView.errordata
								// .setHTML("<li> You cannot enter a negative Percentage.");
								// BaseView.commentPanel.setVisible(true);
								errorsWidget.addError(this, Accounter
										.messages().cantenternegnumber());
								// Accounter
								// .showError("You cannot enter a negative Percentage");
								setPercentage(0.0);
							} else if (DecimalUtil.isGreaterThan(
									enteredPercentageValue, 100)) {
								// BaseView.errordata
								// .setHTML("<li> You cannot enter a percentage more than 100.");
								// BaseView.commentPanel.setVisible(true);
								errorsWidget.addError(this, Accounter
										.messages().cantentermorethat100());
								// Accounter
								// .showError("You cannot enter a percentage more than 100");
								setPercentage(0.0);
							} else
								setPercentage(enteredPercentageValue);
						}
					} else {
						setPercentage(0.0);
					}

				} catch (Exception e) {
					setPercentage(0.0);
				}

			}
		};
		return blurHandler;
	}

	/**
	 * @param percentage
	 *            the percentage to set
	 */
	public void setPercentage(Double percentage) {
		if (percentage == null)
			return;
		this.percentage = percentage;
		setValue(percentage + "%");
		// setValue(NumberFormat.getPercentFormat().format(percentage));
	}

	/**
	 * @return the percentage
	 */
	public Double getPercentage() {
		return percentage;
	}

}
