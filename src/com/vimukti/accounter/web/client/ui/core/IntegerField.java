package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class IntegerField extends TextItem {
	private Long number;

	public IntegerField(final String name) {
		setName(name);
		setTitle(name);
		((TextBox) getMainWidget()).setTextAlignment(TextBoxBase.ALIGN_RIGHT);
		addBlurHandler(getBlurHandler());

		// setKeyPressHandler(new KeyPressListener() {
		//
		// @Override
		// public void onKeyPress(int keyCode) {
		// if (!((keyCode >= 48 && keyCode <= 57))) {
		// setValue("0");
		// }
		// }
		// });
	}

	private BlurHandler getBlurHandler() {

		BlurHandler blurHandler = new BlurHandler() {

			Object value = null;

			public void onBlur(BlurEvent event) {
				try {

					Long enteredNumber = 0l;
					value = getValue();

					if (value == null)

						return;

					try {
						enteredNumber = Long.parseLong(value.toString());
						if (enteredNumber != null) {
							setNumber(enteredNumber);
						}
					} catch (Exception e) {
						if (value.toString().length() != 0) {
							// BaseView.errordata.setHTML("<li> "
							// + AccounterErrorType.INCORRECTINFORMATION
							// + ".");
							// BaseView.commentPanel.setVisible(true);
							addError(this,
									AccounterErrorType.INCORRECTINFORMATION);
						}
						// Accounter
						// .showError(AccounterErrorType.INCORRECTINFORMATION);

					}
				} catch (Exception e) {
					Accounter.showError(Accounter.constants().invalidInfo());
				}

			}
		};
		return blurHandler;
	}

	public void setNumber(Long enteredNumber) {
		this.number = enteredNumber;
		if (enteredNumber != null)
			setValue(enteredNumber);
		else
			setValue("");

	}

	/**
	 * @return the number
	 */
	public Long getNumber() {
		return number;
	}
}