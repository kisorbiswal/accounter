package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientChequeLayout;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextBoxItem.KeyPressListener;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.ChequeLayoutWidget;

public class CheckPrintingSettingView extends BaseView<ClientChequeLayout> {

	private VerticalPanel allPanel;
	private ChequeLayoutWidget widget;
	private ClientChequeLayout layout;
	private AccountCombo accountCombo;
	private TextItem signatureItem;
	private AmountField chequeWidth, chequeHeight, payeeTop, payeeLeft,
			payeeWidth, amountWordsTopOne, amountWordsLeftOne,
			amountWordsWidthOne, amountWordsTopTwo, amountWordsLeftTwo,
			amountWordsWidthTwo, amountFigTop, amountFigLeft, amountFigWidth,
			chequeDateTop, chequeDateLeft, chequeDateWidth, companyTop,
			companyLeft, companyWidth, signatoryTop, signatoryLeft,
			signatoryWidth;
	private Label noteLabel, pageFormatsLabel;

	@Override
	public void init() {
		super.init();
		createControls();
	}

	@Override
	public void initData() {
		accountCombo.setComboItem(getCompany().getAccount(layout.getAccount()));
		signatureItem.setValue(layout.getAuthorisedSignature());
		chequeWidth.setAmount(layout.getChequeWidth());
		chequeHeight.setAmount(layout.getChequeHeight());
		payeeTop.setAmount(layout.getPayeeNameTop());
		payeeLeft.setAmount(layout.getPayeeNameLeft());
		payeeWidth.setAmount(layout.getPayeeNameWidth());
		amountWordsTopOne.setAmount(layout.getAmountWordsLin1Top());
		amountWordsLeftOne.setAmount(layout.getAmountWordsLin1Left());
		amountWordsWidthOne.setAmount(layout.getAmountWordsLin1Width());
		amountWordsTopTwo.setAmount(layout.getAmountWordsLin2Top());
		amountWordsLeftTwo.setAmount(layout.getAmountWordsLin2Left());
		amountWordsWidthTwo.setAmount(layout.getAmountWordsLin2Width());
		amountFigTop.setAmount(layout.getAmountFigTop());
		amountFigLeft.setAmount(layout.getAmountFigLeft());
		amountFigWidth.setAmount(layout.getAmountFigWidth());
		chequeDateTop.setAmount(layout.getChequeDateTop());
		chequeDateLeft.setAmount(layout.getChequeDateLeft());
		chequeDateWidth.setAmount(layout.getChequeDateWidth());
		companyTop.setAmount(layout.getCompanyNameTop());
		companyLeft.setAmount(layout.getCompanyNameLeft());
		companyWidth.setAmount(layout.getCompanyNameWidth());
		signatoryTop.setAmount(layout.getSignatoryTop());
		signatoryLeft.setAmount(layout.getSignatoryLeft());
		signatoryWidth.setAmount(layout.getSignatoryWidth());

	}

	private void createControls() {

		allPanel = new VerticalPanel();
		VerticalPanel panel = new VerticalPanel();

		accountCombo = new AccountCombo(messages.bankAccount()) {

			@Override
			protected List<ClientAccount> getAccounts() {
				return getCompany().getAccounts(ClientAccount.TYPE_BANK);
			}
		};
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						ClientChequeLayout checkLayout = getCompany()
								.getCheckLayout(selectItem.getID());
						layout = checkLayout;
						initData();
					}
				});
		signatureItem = new TextItem();
		signatureItem.setTitle(messages.authorisedSignatory());
		signatureItem.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				widget.setAuthoritySignatory(signatureItem.getValue());
			}
		});

		DynamicForm form = new DynamicForm();
		form.setNumCols(4);
		form.setWidth("100%");
		form.setFields(accountCombo, signatureItem);
		form.addStyleName("cheque_print_text");
		layout = getCompany().getCheckLayout(0);// Loading default cheque layout
		layout = new ClientChequeLayout();
		widget = new ChequeLayoutWidget(layout);

		HorizontalPanel pageFormatPanel = new HorizontalPanel();
		pageFormatsLabel = new Label(messages.pageFormats());
		noteLabel = new Label(messages.noteAllDimensions());
		pageFormatsLabel.addStyleName("cheque_print_text");
		noteLabel.addStyleName("cheque_print_text");
		pageFormatPanel.add(pageFormatsLabel);
		pageFormatPanel.add(noteLabel);
		pageFormatPanel.setWidth("100%");

		pageFormatPanel.setCellHorizontalAlignment(noteLabel, ALIGN_RIGHT);
		pageFormatPanel
				.setCellHorizontalAlignment(pageFormatsLabel, ALIGN_LEFT);

		DynamicForm sizesForm = new DynamicForm();
		LabelItem topLabelItem = new LabelItem();
		topLabelItem.setValue(messages.top());
		topLabelItem.addStyleName("cheque_print_text");
		LabelItem leftLabelItem = new LabelItem();
		leftLabelItem.addStyleName("cheque_print_text");
		leftLabelItem.setValue(messages.left());
		LabelItem widthLabelItem = new LabelItem();
		widthLabelItem.setValue(messages.width());
		widthLabelItem.addStyleName("cheque_print_text");
		sizesForm.setWidth("100%");

		chequeWidth = new AmountField(messages.chequeWidth(), this);
		addHandelers(chequeWidth, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setChequeWidth(value);
			}

		});
		chequeHeight = new AmountField(messages.chequeHeight(), this);
		addHandelers(chequeHeight, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setChequeHeight(value);
			}

		});

		LabelItem payeeLabel = new LabelItem();
		payeeLabel.setTitle(messages.nameOfPayee());
		payeeTop = new AmountField("", this);
		addHandelers(payeeTop, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setPayeeNameTop(value);
			}

		});
		payeeLeft = new AmountField("", this);
		addHandelers(payeeLeft, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setPayeeNameLeft(value);
			}

		});
		payeeWidth = new AmountField("", this);
		addHandelers(payeeWidth, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setPayeeNameWidth(value);
			}

		});

		LabelItem amountWordsLineOne = new LabelItem();
		amountWordsLineOne.setTitle(messages.amountInWordsLineOne());
		amountWordsTopOne = new AmountField("", this);
		addHandelers(amountWordsTopOne, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountWordsLin1Top(value);
			}
		});

		amountWordsLeftOne = new AmountField("", this);
		addHandelers(amountWordsLeftOne, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountWordsLin1Left(value);
			}

		});
		amountWordsWidthOne = new AmountField("", this);
		addHandelers(amountWordsWidthOne, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountWordsLin1Width(value);
			}

		});

		LabelItem amountWordsLineTwo = new LabelItem();
		amountWordsLineTwo.setTitle(messages.amountInWordsLineTwo());
		amountWordsTopTwo = new AmountField("", this);
		addHandelers(amountWordsTopTwo, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountWordsLin2Top(value);
			}

		});
		amountWordsLeftTwo = new AmountField("", this);
		addHandelers(amountWordsLeftTwo, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountWordsLin2Left(value);
			}

		});
		amountWordsWidthTwo = new AmountField("", this);
		addHandelers(amountWordsWidthTwo, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountWordsLin2Width(value);
			}

		});

		LabelItem amountFigLabel = new LabelItem();
		amountFigLabel.setTitle(messages.amountInFigures());
		amountFigTop = new AmountField("", this);
		addHandelers(amountFigTop, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountFigTop(value);
			}

		});
		amountFigLeft = new AmountField("", this);
		addHandelers(amountFigLeft, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountFigLeft(value);
			}

		});
		amountFigWidth = new AmountField("", this);
		addHandelers(amountFigWidth, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountFigWidth(value);
			}

		});

		LabelItem chequeDateLabel = new LabelItem();
		chequeDateLabel.setTitle(messages.chequeDate());
		chequeDateTop = new AmountField("", this);
		addHandelers(chequeDateTop, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setChequeDateTop(value);
			}

		});
		chequeDateLeft = new AmountField("", this);
		addHandelers(chequeDateLeft, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setChequeDateLeft(value);

			}

		});
		chequeDateWidth = new AmountField("", this);
		addHandelers(chequeDateWidth, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setChequeDateWidth(value);
			}

		});

		LabelItem companyLabel = new LabelItem();
		companyLabel.setTitle(messages.forCompany());
		companyTop = new AmountField("", this);
		addHandelers(companyTop, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setCompanyNameTop(value);
			}

		});
		companyLeft = new AmountField("", this);
		addHandelers(companyLeft, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setCompanyNameLeft(value);
			}

		});
		companyWidth = new AmountField("", this);
		addHandelers(companyWidth, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setCompanyNameWidth(value);
			}

		});

		LabelItem signatoryLabel = new LabelItem();
		signatoryLabel.setTitle(messages.authorisedSignatory());
		signatoryTop = new AmountField("", this);
		addHandelers(signatoryTop, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setSignatoryTop(value);
			}

		});
		signatoryLeft = new AmountField("", this);
		addHandelers(signatoryLeft, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setSignatoryLeft(value);
			}

		});
		signatoryWidth = new AmountField("", this);
		addHandelers(signatoryWidth, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setSignatoryWidth(value);

			}

		});

		LabelItem item = new LabelItem();
		item.setValue("");

		DynamicForm allFields = new DynamicForm();
		allFields.setWidth("100%");
		allFields.setNumCols(8);
		allFields.setFields(item, topLabelItem, leftLabelItem, widthLabelItem,
				payeeLabel, payeeTop, payeeLeft, payeeWidth,
				amountWordsLineOne, amountWordsTopOne, amountWordsLeftOne,
				amountWordsWidthOne, amountWordsLineTwo, amountWordsTopTwo,
				amountWordsLeftTwo, amountWordsWidthTwo, amountFigLabel,
				amountFigTop, amountFigLeft, amountFigWidth, chequeDateLabel,
				chequeDateTop, chequeDateLeft, chequeDateWidth, companyLabel,
				companyTop, companyLeft, companyWidth, signatoryLabel,
				signatoryTop, signatoryLeft, signatoryWidth);

		DynamicForm chequeForm = new DynamicForm();
		chequeForm.setNumCols(2);
		chequeForm.setFields(chequeWidth, chequeHeight);
		chequeForm.addStyleName("cheque_form");

		pageFormatPanel.addStyleName("pageformatpanel");
		panel.setWidth("100%");
		panel.addStyleName("checkprintbox");
		panel.add(pageFormatPanel);
		panel.add(allFields);
		panel.add(chequeForm);

		allPanel.add(form);
		allPanel.add(widget);
		allPanel.add(panel);

		this.add(allPanel);
		allPanel.setWidth("100%");
		this.setCellHorizontalAlignment(allPanel, ALIGN_LEFT);
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getForms() {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void addHandelers(final AmountField src,
			final ValueChangeHandler handler) {
		src.setKeyPressHandler(new KeyPressListener() {

			@Override
			public void onKeyPress(int keyCode) {
				Double amount = Double.valueOf(src.textBox.getText());
				if (keyCode == KeyCodes.KEY_UP) {
					amount = amount + 0.1;
					src.setAmount(amount);
					handler.onChange(amount);
				} else if (keyCode == KeyCodes.KEY_DOWN) {
					amount = amount - 0.1;
					if (amount <= 0) {
						amount = 0d;
					}
					src.setAmount(amount);
					handler.onChange(amount);
				}
			}
		});

		src.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				handler.onChange(src.getAmount());
			}
		});
	}

	interface ValueChangeHandler {
		void onChange(double value);
	}

}
