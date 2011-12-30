package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.ui.company.CurrencyFormatDialog;

public class CurrencyFormatOption extends AbstractPreferenceOption {

	private static CurrencyFormatOptionUiBinder uiBinder = GWT
			.create(CurrencyFormatOptionUiBinder.class);
	@UiField
	Label currencyFormatLabel;

	@UiField
	Button currencyFormatButton;

	interface CurrencyFormatOptionUiBinder extends
			UiBinder<Widget, CurrencyFormatOption> {
	}

	public CurrencyFormatOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	public String getTitle() {
		return messages.currencyFormat();
	}

	@Override
	public String getAnchor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createControls() {

		currencyFormatLabel.setText(messages.currencyFormat());
		currencyFormatButton.setText(messages.changeCurrencyFormat());
		currencyFormatButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CurrencyFormatDialog dialog = new CurrencyFormatDialog(messages
						.currencyFormat());
				dialog.show();
				dialog.center();
			}
		});
	}

	@Override
	public void onSave() {
	}

	@Override
	public void initData() {
	}

}