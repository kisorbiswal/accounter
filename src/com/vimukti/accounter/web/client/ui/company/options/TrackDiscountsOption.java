package com.vimukti.accounter.web.client.ui.company.options;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;

public class TrackDiscountsOption extends AbstractPreferenceOption {

	private static TrackDiscountsOptionUiBinder uiBinder = GWT
			.create(TrackDiscountsOptionUiBinder.class);

	@UiField
	CheckBox trackDiscountCheckBox;

	@UiField
	Label discountLabel;

	@UiField
	RadioButton onepeTransactionRadioButton;

	@UiField
	Label oneperTransactionLabel;

	@UiField
	RadioButton oneperdetaillineRadioButton;

	@UiField
	Label oneperdetaillineLabel;

	@UiField
	StyledPanel hidePanel;

	@UiField
	StyledPanel radioButtonPanel;

	@UiField
	Label selectDiscountAccount;

	@UiField
	StyledPanel accountPanel;

	@UiField
	Label discountAccount;

	@UiField
	ListBox accountsCombo;

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
		return messages.trackDiscount();
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setTrackDiscounts(
				trackDiscountCheckBox.getValue());
		getCompanyPreferences().setDiscountPerDetailLine(
				oneperdetaillineRadioButton.getValue());

	}

	@Override
	public String getAnchor() {
		return messages.trackDiscount();
	}

	@Override
	public void createControls() {

		trackDiscountCheckBox.setText(messages.trackDiscount());
		discountLabel.setText(messages.discountInAllTransaction());

		onepeTransactionRadioButton.setText(messages.onepertransaction());
		oneperTransactionLabel.setText(messages.onePerTransactionDescription());

		oneperdetaillineRadioButton.setText(messages.oneperdetailline());
		oneperdetaillineLabel.setText(messages.onePerDetailLineDescription());

		oneperdetaillineRadioButton.setName(messages.discount());
		onepeTransactionRadioButton.setName(messages.discount());

		oneperTransactionLabel.setStyleName("organisation_comment");
		oneperdetaillineLabel.setStyleName("organisation_comment");

		selectDiscountAccount.setText(messages.selectDiscountAccountDesc());

		discountAccount.setText(messages.discountAccount());

		accountPanel.setStyleName("account_panel");

		trackDiscountCheckBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hidePanel.setVisible(trackDiscountCheckBox.getValue());
			}
		});

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

		trackDiscountCheckBox.setValue(getCompanyPreferences()
				.isTrackDiscounts());
		hidePanel.setVisible(getCompanyPreferences().isTrackDiscounts());
		if (getCompanyPreferences().isDiscountPerDetailLine())
			oneperdetaillineRadioButton.setValue(true);
		else
			onepeTransactionRadioButton.setValue(true);

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
					AccounterCoreType.ACCOUNT, "Cash Discount Given",
					asyncCallback);
		} else {
			accountsCombo.setSelectedIndex(getAccounts().indexOf(account));
		}

	}
}
