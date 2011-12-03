package com.vimukti.accounter.web.client.ui.company.options;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;

public class TrackDiscountsOption extends AbstractPreferenceOption {

	private static final String CASH_DISCOUNT_ACCOUNT = "Cash Discount Given";

	@UiField
	CheckBox trackCheckbox;

	@UiField
	VerticalPanel hidePanel;

	@UiField
	Label selectDiscount;

	@UiField
	Label selectDiscountAccount;

	@UiField
	HorizontalPanel accountPanel;

	@UiField
	Label discountAccount;

	@UiField
	ListBox accountsCombo;

	private static TrackDiscountsOptionUiBinder uiBinder = GWT
			.create(TrackDiscountsOptionUiBinder.class);

	interface TrackDiscountsOptionUiBinder extends
			UiBinder<Widget, TrackDiscountsOption> {
	}

	public TrackDiscountsOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	@Override
	public String getTitle() {
		return messages.trackingDiscounts();
	}

	@Override
	public void onSave() {
		Boolean trackDiscounts = trackCheckbox.getValue();
		getCompanyPreferences().setTrackDiscounts(trackDiscounts);
		if (trackDiscounts) {
			getCompany()
					.setCashDiscountAccount(
							getAccounts().get(accountsCombo.getSelectedIndex())
									.getID());
		}
	}

	@Override
	public String getAnchor() {
		return messages.trackingDiscounts();
	}

	@Override
	public void createControls() {
		trackCheckbox.setText(messages.discounts());

		trackCheckbox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hidePanel.setVisible(trackCheckbox.getValue());

			}
		});

		discountAccount.setText(messages.discountAccount());

		for (int i = 0; i < getAccounts().size(); i++) {
			accountsCombo.addItem(getAccounts().get(i).getName());
		}
		if (getCompany().getTradingAddress() != null
				&& getCompany().getTradingAddress().getCountryOrRegion() != null) {
			accountsCombo.setSelectedIndex(getAccounts().indexOf(
					getCompany().getCashDiscountAccount()));
		} else {
			accountsCombo.setSelectedIndex(0);
		}

	}

	private List<ClientAccount> getAccounts() {
		return getCompany().getAccounts();
	}

	@Override
	public void initData() {
		trackCheckbox.setValue(getCompanyPreferences().isTrackDiscounts());
		hidePanel.setVisible(getCompanyPreferences().isTrackDiscounts());

		selectDiscount.setText(messages.selectDiscountDesc());
		selectDiscount.setStyleName("organisation_comment");

		selectDiscountAccount.setText(messages.selectDiscountAccountDesc());
		selectDiscountAccount.setStyleName("account_comment");
		accountPanel.setStyleName("account_panel");

		ClientAccount account = getCompany().getAccount(
				getCompany().getCashDiscountAccount());

		AsyncCallback<ClientAccount> asyncCallback = new AsyncCallback<ClientAccount>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ClientAccount result) {
				accountsCombo.setSelectedIndex(getAccounts().indexOf(result));

			}
		};
		if (account == null) {
			Accounter.createGETService().getObjectByName(
					AccounterCoreType.ACCOUNT, CASH_DISCOUNT_ACCOUNT,
					asyncCallback);
		} else {
			accountsCombo.setSelectedIndex(getAccounts().indexOf(account));
		}
	}

}
