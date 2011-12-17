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
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
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

	private Label noteLabel, pageFormatsLabel;

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {

		allPanel = new VerticalPanel();
		VerticalPanel panel = new VerticalPanel();
		DynamicForm allFields = new DynamicForm();

		accountCombo = new AccountCombo(messages.bankAccount()) {

			@Override
			protected List<ClientAccount> getAccounts() {
				return getCompany().getAccounts(ClientAccount.TYPE_BANK);
			}
		};
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

		AmountField chequeWidth = new AmountField(messages.chequeWidth(), this);
		chequeWidth.setAmount(5d);
		addHandelers(chequeWidth, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setChequeWidth(value);
			}

		});
		AmountField chequeHeight = new AmountField(messages.chequeHeight(),
				this);
		chequeHeight.setAmount(5d);
		addHandelers(chequeHeight, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setChequeHeight(value);
			}

		});

		LabelItem payeeLabel = new LabelItem();
		payeeLabel.setTitle(messages.nameOfPayee());
		AmountField payeeTop = new AmountField("", this);
		addHandelers(payeeTop, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setPayeeNameTop(value);
			}

		});
		AmountField payeeLeft = new AmountField("", this);
		addHandelers(payeeLeft, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setPayeeNameLeft(value);
			}

		});
		AmountField payeeWidth = new AmountField("", this);
		addHandelers(payeeWidth, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setPayeeNameWidth(value);
			}

		});

		LabelItem amountWordsLineOne = new LabelItem();
		amountWordsLineOne.setTitle(messages.amountInWordsLineOne());
		AmountField amountWordsTopOne = new AmountField("", this);
		addHandelers(amountWordsTopOne, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountWordsLin1Top(value);
			}
		});

		AmountField amountWordsLeftOne = new AmountField("", this);
		addHandelers(amountWordsLeftOne, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountWordsLin1Left(value);
			}

		});
		AmountField amountWordsWidthOne = new AmountField("", this);
		addHandelers(amountWordsWidthOne, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountWordsLin1Width(value);
			}

		});
		LabelItem amountWordsLineTwo = new LabelItem();
		amountWordsLineTwo.setTitle(messages.amountInWordsLineTwo());
		AmountField amountWordsTopTwo = new AmountField("", this);
		addHandelers(amountWordsTopTwo, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountWordsLin2Top(value);
			}

		});
		AmountField amountWordsLeftTwo = new AmountField("", this);
		addHandelers(amountWordsLeftTwo, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountWordsLin2Left(value);
			}

		});
		AmountField amountWordsWidthTwo = new AmountField("", this);
		addHandelers(amountWordsWidthTwo, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountWordsLin2Width(value);
			}

		});

		LabelItem amountInFiguresLabel = new LabelItem();
		amountInFiguresLabel.setTitle(messages.amountInFigures());
		AmountField amountInFiguresTopField = new AmountField("", this);
		addHandelers(amountInFiguresTopField, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountFigTop(value);
			}

		});
		AmountField amountInFiguresLeftField = new AmountField("", this);
		addHandelers(amountInFiguresLeftField, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountFigLeft(value);
			}

		});
		AmountField amountInFiguresWidthField = new AmountField("", this);
		addHandelers(amountInFiguresWidthField, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setAmountFigWidth(value);
			}

		});

		LabelItem chequeDateLabel = new LabelItem();
		chequeDateLabel.setTitle(messages.chequeDate());
		AmountField chequeDateTopField = new AmountField("", this);
		addHandelers(chequeDateTopField, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setChequeDateTop(value);
			}

		});
		AmountField chequeDateLeftField = new AmountField("", this);
		addHandelers(chequeDateLeftField, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setChequeDateLeft(value);

			}

		});
		AmountField chequeDateWidthField = new AmountField("", this);
		addHandelers(chequeDateWidthField, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setChequeDateWidth(value);
			}

		});

		LabelItem forCompanyLabel = new LabelItem();
		forCompanyLabel.setTitle(messages.forCompany());
		AmountField forCompanyTopField = new AmountField("", this);
		addHandelers(forCompanyTopField, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setCompanyNameTop(value);
			}

		});
		AmountField forCompanyLeftField = new AmountField("", this);
		addHandelers(forCompanyLeftField, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setCompanyNameLeft(value);
			}

		});
		AmountField forCompanyWidthField = new AmountField("", this);
		addHandelers(forCompanyWidthField, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setCompanyNameWidth(value);
			}

		});

		LabelItem authorisedSignatoryLabel = new LabelItem();
		authorisedSignatoryLabel.setTitle(messages.authorisedSignatory());
		AmountField authorisedSignatoryTopField = new AmountField("", this);
		addHandelers(authorisedSignatoryTopField, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setSignatoryTop(value);
			}

		});
		AmountField signatoryLeft = new AmountField("", this);
		addHandelers(signatoryLeft, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setSignatoryLeft(value);
			}

		});
		AmountField signatoryWidth = new AmountField("", this);
		addHandelers(signatoryWidth, new ValueChangeHandler() {

			@Override
			public void onChange(double value) {
				widget.setSignatoryWidth(value);

			}

		});
		LabelItem item = new LabelItem();
		item.setValue("");
		allFields.setWidth("100%");
		allFields.setNumCols(8);
		allFields.setFields(item, topLabelItem, leftLabelItem, widthLabelItem,
				payeeLabel, payeeTop, payeeLeft, payeeWidth,
				amountWordsLineOne, amountWordsTopOne, amountWordsLeftOne,
				amountWordsWidthOne, amountWordsLineTwo, amountWordsTopTwo,
				amountWordsLeftTwo, amountWordsWidthTwo, amountInFiguresLabel,
				amountInFiguresTopField, amountInFiguresLeftField,
				amountInFiguresWidthField, chequeDateLabel, chequeDateTopField,
				chequeDateLeftField, chequeDateWidthField, forCompanyLabel,
				forCompanyTopField, forCompanyLeftField, forCompanyWidthField,
				authorisedSignatoryLabel, authorisedSignatoryTopField,
				signatoryLeft, signatoryWidth);

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
