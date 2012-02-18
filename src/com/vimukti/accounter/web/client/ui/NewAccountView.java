package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.BankNameCombo;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.DropDownCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.customers.UploadStatementDialog;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyChangeListener;
import com.vimukti.accounter.web.client.ui.widgets.CurrencyComboWidget;

/**
 * @modified by Ravi Kiran.G
 * 
 */
public class NewAccountView extends BaseView<ClientAccount> {

	private static final int BANK_CAT_BEGIN_NO = 1100;
	private static final int BANK_CAT_END_NO = 1179;
	private int accountType;
	private SelectCombo accTypeSelect;
	private TextItem accNameText, bankAccNumText, hierText;
	private CheckboxItem statusBox;
	private OtherAccountsCombo subAccSelect;
	private SelectItem cashFlowCatSelect;
	private LinkedHashMap<String, String> cashFlowof;
	private AmountField opBalText;
	private DateField asofDate;
	private AmountField currentBalanceText;
	private SelectItem catSelect;
	private DynamicForm accInfoForm, balanceForm;
	private CheckboxItem cashAccountCheck;
	private DynamicForm cashBasisForm;
	private TextAreaItem commentsArea;
	private DynamicForm commentsForm;
	private SelectCombo typeSelect;
	private AmountField limitText;
	private IntegerField accNoText, cardNumText;
	private HorizontalPanel topHLay;
	private VerticalPanel leftLayout;
	// private TextBox textbox;
	protected boolean isClose;
	private List<String> typeMap;
	protected List<ClientBank> allBanks;
	private List<String> accTypeMap;
	protected ClientBank selectedBank;

	private List<ClientAccount> subAccounts;
	protected ClientAccount selectedSubAccount;
	private Double openingBalance = 0D;
	private Double creditLimit = 0D;
	private BankNameCombo bankNameSelect;
	private String hierarchy;
	private String subhierarchy;
	private int cashflowValue;

	private List<Integer> accountTypes;
	// private boolean isNewBankAccount;

	private String defaultId;
	private String selectedId;

	private DynamicForm bankForm;
	private DynamicForm creditCardForm;
	private DynamicForm paypalForm;
	// private CurrencyWidget currency;

	protected ClientCurrency selectCurrency;

	private Label lab1;

	VerticalPanel mainVLay;

	private ArrayList<DynamicForm> listforms;
	protected Long nextAccountNumber;
	private int accountSubBaseType;
	private Integer[] nominalCodeRange;

	String accountName;
	String accountNo;
	private TextItem paypalEmail;
	private double currencyFactor = 1.0;
	CurrencyComboWidget currencyCombo;
	private AccountCombo parentAccountCombo;
	private CheckboxItem isSubAccountBox;

	public NewAccountView() {
		super();
	}

	private void getSubAccounts() {
		subAccounts = getCompany().getAccounts(accountType);
		if (isInViewMode()) {
			for (ClientAccount account : subAccounts) {
				if (account.getID() == getData().getID()) {
					subAccounts.remove(account);
					break;
				}

			}

		}
		// subAccSelect.setComboItem(null);
		// subAccSelect.initCombo(subAccounts);
		// subAccSelect.setHelpInformation(true);
		// if (selectedSubAccount != null)
		// subAccSelect.setComboItem(selectedSubAccount);

	}

	private void getBanks() {
		this.allBanks = getCompany().getBanks();
		initBankNameSelect();
	}

	protected void initBankNameSelect() {

		bankNameSelect.initCombo(allBanks);

		if (isInViewMode()
				&& data instanceof ClientBankAccount
				&& (selectedBank = getCompany().getBank(
						((ClientBankAccount) data).getBank())) != null) {
			bankNameSelect.setComboItem(selectedBank);
		}

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		// setTitle(UIUtils.title(FinanceApplication.constants()
		// .createNewAccount()));
		lab1 = new Label();
		lab1.removeStyleName("gwt-Label");
		lab1.addStyleName("label-title");
		// lab1.setHeight("35px");
		hierarchy = new String("");
		accTypeSelect = new SelectCombo(messages.accountType());
		accTypeMap = new ArrayList<String>();
		accTypeMap.add(messages.creditCard());
		accTypeMap.add(messages.paypal());
		accTypeSelect.initCombo(accTypeMap);
		accTypeSelect.setHelpInformation(true);
		// accTypeSelect.setWidth(100);
		accTypeSelect.setDisabled(isInViewMode());
		accTypeSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						selectedId = accTypeSelect.getSelectedValue();
						accounttype_selected();
						int subBaseType = UIUtils
								.getAccountSubBaseType(getAccountType(selectedId));
						Integer[] ranges = getCompany().getNominalCodeRange(
								subBaseType);
						accNoText.setToolTip(messages.accountNumberToolTipDesc(
								String.valueOf(ranges[0]),
								String.valueOf(ranges[1])));
					}
				});

		accNoText = new IntegerField(this, messages.accountNumber());
		accNoText.setHelpInformation(true);
		accNoText.setRequired(true);
		accNoText.setWidth(100);
		accNoText.setDisabled(isInViewMode());
		accNoText.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				new Timer() {
					@Override
					public void run() {
						if (accNoText.getNumber() != null) {
							validateAccountNumber(accNoText.getNumber());
						}
					}
				}.schedule(1000);
			}
		});
		accNoText.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				// displayAndSetAccountNo();

			}
		});

		accNameText = new TextItem(messages.accountName());
		accNameText.setValue(accountName);
		accNameText.setToolTip(messages.giveTheNameAccordingToYourID(this
				.getAction().getViewName()));
		accNameText.setHelpInformation(true);
		accNameText.setRequired(true);
		accNameText.setWidth(100);
		accNameText.setDisabled(isInViewMode());
		accNameText.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				// Converts the first letter of Account Name to Upper case
				String name = accNameText.getValue().toString();
				if (name.isEmpty()) {
					return;
				}
				String lower = name.substring(0, 1);
				String upper = lower.toUpperCase();
				accNameText.setValue(name.replaceFirst(lower, upper));

				if (accountType != ClientAccount.TYPE_BANK
						&& accountType != ClientAccount.TYPE_CREDIT_CARD) {
					String temp = null;
					if (accNameText.getValue() != null) {
						temp = accNameText.getValue().toString();
						if (subhierarchy == null
								|| subhierarchy.length() == 0
								|| (subAccSelect != null && selectedSubAccount != null)) {
							hierarchy = Utility
									.getHierarchy(selectedSubAccount) != null ? Utility
									.getHierarchy(selectedSubAccount) : "";
							hierarchy = hierarchy + temp;

						} else
							hierarchy = subhierarchy + temp;
					} else
						hierarchy = subhierarchy;
					hierText.setValue(hierarchy);
				}

			}

		});

		statusBox = new CheckboxItem(messages.active());
		statusBox.setWidth(100);
		statusBox.setValue(true);
		statusBox.setDisabled(isInViewMode());

		isSubAccountBox = new CheckboxItem(messages.isSubAccount());
		isSubAccountBox.setWidth(100);
		isSubAccountBox.setDisabled(isInViewMode());
		isSubAccountBox.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				parentAccountCombo.setVisible(event.getValue());
			}
		});

		parentAccountCombo = new AccountCombo(messages.parentAccount()) {

			@Override
			protected List<ClientAccount> getAccounts() {
				return getCompany().getAccounts();
			}
		};
		parentAccountCombo.setDisabled(isInViewMode());

		parentAccountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						if (selectItem.getType() != getAccountType(accTypeSelect
								.getSelectedValue())) {
							Accounter.showError(messages
									.parenAccountTypeShouldBeSame());
						}

					}
				});

		cashFlowCatSelect = new SelectItem(messages.cashFlowCategory());
		cashFlowCatSelect.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub

			}
		});
		cashFlowof = new LinkedHashMap<String, String>();

		cashFlowof.put(ClientAccount.CASH_FLOW_CATEGORY_FINANCING + "",
				messages.financing());
		cashFlowof.put(ClientAccount.CASH_FLOW_CATEGORY_INVESTING + "",
				messages.investing());
		cashFlowof.put(ClientAccount.CASH_FLOW_CATEGORY_OPERATING + "",
				messages.operating());

		cashFlowCatSelect.setValueMap(cashFlowof);

		opBalText = new AmountField(messages.openingBalance(), this,
				getBaseCurrency(), true);
		opBalText.setToolTip(messages.giveOpeningBalanceToThis(this.getAction()
				.getViewName()));
		opBalText.setHelpInformation(true);
		opBalText.setDisabled(isInViewMode());
		opBalText.setWidth(100);
		opBalText.setValue("" + UIUtils.getCurrencySymbol() + "0.00");
		currentBalanceText = new AmountField(messages.currentBalance(), this,
				getBaseCurrency());
		currentBalanceText.setToolTip(messages.currentBalance());
		currentBalanceText.setHelpInformation(true);
		currentBalanceText.setDisabled(true);
		currentBalanceText.setWidth(100);
		currentBalanceText.setValue("" + UIUtils.getCurrencySymbol() + "0.00");
		// opBalText.setShowDisabled(false);
		// opBalText.addBlurHandler(new BlurHandler() {
		//
		// public void onBlur(BlurEvent event) {
		// Double balance = 0D;
		// try {
		// if (opBalText.getAmount() == null) {
		// Accounter.showError(FinanceApplication
		// .constants()
		// .openingBalanceShouldNotBeNull());
		// } else {
		// balance = opBalText.getAmount();
		// if (DecimalUtil.isLessThan(balance, -1000000000000.00)
		// || DecimalUtil.isGreaterThan(balance,
		// 1000000000000.00)) {
		// Accounter.showError(FinanceApplication
		// .constants()
		// .balanceShouldBeInTheRange());
		// balance = 0D;
		// }
		// }
		//
		// } catch (Exception e) {
		// Accounter.showError(FinanceApplication
		// .constants().invalidOpeningBalance());
		// } finally {
		// opBalText.setAmount(balance);
		// setOpeningBalance(balance);
		// }
		//
		// }
		//
		// });
		asofDate = new DateField(messages.asOf());
		asofDate.setHelpInformation(true);
		asofDate.setToolTip(messages.selectDateWhenTransactioCreated(this
				.getAction().getViewName()));
		// asofDate.setWidth(100);
		asofDate.setEnteredDate(new ClientFinanceDate(
				getCompany().getPreferences().getPreventPostingBeforeDate() == 0 ? new ClientFinanceDate()
						.getDate() : getCompany().getPreferences()
						.getPreventPostingBeforeDate()));

		catSelect = new SelectItem(messages.category1099());
		catSelect.setWidth(100);
		catSelect.setDisabled(true);

		accInfoForm = UIUtils.form(messages.chartOfAccountsInformation());
		// accInfoForm.setWidth("100%");
		balanceForm = new DynamicForm();
		topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		leftLayout = new VerticalPanel();
		// leftLayout.setWidth("90%");
		currencyCombo = createCurrencyComboWidget();
		if (accountType == 0
				|| (accountType != ClientAccount.TYPE_BANK && accountType != ClientAccount.TYPE_CREDIT_CARD)) {
			subAccSelect = new OtherAccountsCombo(messages.subCategoryof());
			subAccSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
						@Override
						public void selectedComboBoxItem(
								ClientAccount selectItem) {
							selectedSubAccount = selectItem;
							subhierarchy = selectedSubAccount != null ? Utility
									.getHierarchy(selectedSubAccount) : "";
							hierarchy = subhierarchy;
							if (accNameText.getValue() != null) {
								if (hierarchy.equals(""))
									hierarchy = accNameText.getValue()
											.toString();
								else
									hierarchy = subhierarchy
											+ accNameText.getValue();
							}
							hierText.setValue(hierarchy);
						}
					});
			subAccSelect.setWidth(100);

			hierText = new TextItem(messages.hierarchy());
			hierText.setHelpInformation(true);
			hierText.setDisabled(true);
			hierText.setWidth(100);
		}
		// accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
		// statusBox, cashFlowCatSelect, opBalText, asofDate,
		// catSelect);
		if (getPreferences().getUseAccountNumbers()) {

			accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
					statusBox, isSubAccountBox, parentAccountCombo);
			balanceForm.setFields(opBalText, asofDate, currentBalanceText);
		} else {
			accNoText.setNumber(autoGenerateAccountnumber(1100, 1179));
			accInfoForm.setFields(accTypeSelect, accNameText, statusBox,
					isSubAccountBox, parentAccountCombo);
			balanceForm.setFields(opBalText, asofDate, currentBalanceText);
		}
		parentAccountCombo.setVisible(false);

		leftLayout.add(accInfoForm);
		if (isMultiCurrencyEnabled()) {
			leftLayout.add(currencyCombo);
		}
		leftLayout.add(balanceForm);

		currencyCombo.setVisible(ClientAccount
				.isAllowCurrencyChange(accountType));

		topHLay.add(leftLayout);

		if (accountType == ClientAccount.TYPE_BANK)
			addBankForm();
		if (accountType == ClientAccount.TYPE_PAYPAL) {
			addPaypalForm();
		} else if (accountType == ClientAccount.TYPE_CREDIT_CARD) {
			addCreditCardForm();
		}

		// accInfoForm.getCellFormatter().setWidth(0, 0, "200");
		cashAccountCheck = new CheckboxItem(
				messages.thisIsConsideredACashAccount());
		cashAccountCheck.setWidth(100);
		cashAccountCheck.setDisabled(isInViewMode());

		cashBasisForm = new DynamicForm();
		cashBasisForm.setIsGroup(true);
		cashBasisForm.setGroupTitle(messages.cashBasisAccounting());
		cashBasisForm.setFields(cashAccountCheck);
		cashBasisForm.setWidth("100%");
		// cashBasisForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "200");

		commentsArea = new TextAreaItem();
		commentsArea.setToolTip(messages.writeCommentsForThis(this.getAction()
				.getViewName()));
		commentsArea.setHelpInformation(true);
		commentsArea.setTitle(messages.comments());
		commentsArea.setWidth(100);
		commentsArea.setDisabled(isInViewMode());
		// commentsArea.setShowTitle(false);

		commentsForm = UIUtils.form(messages.comments());
		// commentsForm.setWidth("50%");
		commentsForm.setFields(commentsArea);
		commentsForm.getCellFormatter().getElement(0, 0).getStyle()
				.setVerticalAlign(VerticalAlign.TOP);
		// commentsForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "200");
		// currency = createCurrencyWidget();

		mainVLay = new VerticalPanel();
		mainVLay.addStyleName("fields-panel");

		mainVLay.setSize("100%", "300px");
		mainVLay.add(lab1);
		mainVLay.add(topHLay);

		// mainVLay.add(cashBasisForm);

		mainVLay.add(commentsForm);
		// mainVLay.add(currency);

		// setHeightForCanvas("450");
		this.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(accInfoForm);
		listforms.add(cashBasisForm);

		listforms.add(commentsForm);

		settabIndexes();

	}

	private CurrencyComboWidget createCurrencyWidget() {

		ArrayList<ClientCurrency> currenciesList = getCompany().getCurrencies();
		ClientCurrency baseCurrency = getCompany().getPrimaryCurrency();

		CurrencyComboWidget widget = new CurrencyComboWidget(currenciesList,
				baseCurrency);
		return widget;
	}

	private void addPaypalForm() {
		if (bankForm != null) {
			topHLay.remove(bankForm);
		}
		if (creditCardForm != null)

			topHLay.remove(creditCardForm);

		bankNameSelect = null;

		if (paypalForm == null) {
			lab1.setText("Paypal Account");
			paypalForm = UIUtils.form(messages.paypalInformation());
			paypalForm.setWidth("100%");

			// typeSelect.setDefaultToFirstOption(Boolean.TRUE);

			bankAccNumText = new TextItem(messages.bankAccountNumber());
			bankAccNumText.setHelpInformation(true);
			bankAccNumText.setWidth(100);

			paypalEmail = new TextItem(messages.paypalEmail());
			paypalEmail.setWidth(100);
			paypalEmail.setHelpInformation(true);
			paypalEmail.setRequired(true);
			paypalEmail.setDisabled(isInViewMode());
			paypalEmail.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					if (event != null) {
						String em = paypalEmail.getValue().toString();
						if (em.equals("")) {
							return;
						}
						if (!UIUtils.isValidEmail(em)) {
							Accounter.showError(messages.invalidEmail());
						} else {
							data.setPaypalEmail(em);
						}

					}
				}
			});

			// accNameText.setWidth("*");
			paypalForm.setFields(/* getBankNameSelectItem(), */paypalEmail);

			// bankForm.setWidth("100%");
			// bankForm.setAutoHeight();

		}
		// else {
		// if (bankNameSelect == null)
		// getBankNameSelectItem();
		// reset(bankForm);
		// }
		topHLay.add(paypalForm);
		topHLay.setCellHorizontalAlignment(paypalForm, ALIGN_RIGHT);

	}

	protected void displayAndSetAccountNo() {
		long financeCategoryNumber = 0;

		if (accountType == ClientAccount.TYPE_BANK) {
			addError(accNoText,
					messages.theFinanceCategoryNoShouldBeBetween1100And1179());
			financeCategoryNumber = autoGenerateAccountnumber(
					BANK_CAT_BEGIN_NO, BANK_CAT_END_NO);

		} else {
			accountSubBaseType = UIUtils.getAccountSubBaseType(accountType);

			nominalCodeRange = getCompany().getNominalCodeRange(
					accountSubBaseType);

			if (nominalCodeRange == null
					&& accountSubBaseType == ClientAccount.SUBBASETYPE_OTHER_ASSET) {
				return;
			}
			addError(accNoText, messages.theFinanceCategoryNoShouldBeBetween()
					+ "  " + nominalCodeRange[0] + " " + messages.and() + " "
					+ nominalCodeRange[1]);
			financeCategoryNumber = autoGenerateAccountnumber(
					nominalCodeRange[0], nominalCodeRange[1]);

		}

		accNoText.setValue(String.valueOf(financeCategoryNumber));
	}

	protected void setCashFlowType() {
		switch (this.accountType) {
		case ClientAccount.TYPE_FIXED_ASSET:
		case ClientAccount.TYPE_OTHER_CURRENT_ASSET:
		case ClientAccount.TYPE_OTHER_ASSET:
		case ClientAccount.TYPE_INVENTORY_ASSET:
			cashflowValue = ClientAccount.CASH_FLOW_CATEGORY_INVESTING;
			break;
		case ClientAccount.TYPE_LONG_TERM_LIABILITY:
		case ClientAccount.TYPE_EQUITY:
			cashflowValue = ClientAccount.CASH_FLOW_CATEGORY_FINANCING;
			break;

		default:
			cashflowValue = ClientAccount.CASH_FLOW_CATEGORY_OPERATING;
			break;
		}
		cashFlowCatSelect.setValue(Utility
				.getCashFlowCategoryName(cashflowValue));
	}

	private void resetView() {
		accInfoForm.setWidth("100%");

		if (paypalForm != null)
			topHLay.remove(paypalForm);

		reset(cashBasisForm);
		reset(commentsForm);

		opBalText.setDisabled(false);
		if (accountType != ClientAccount.TYPE_BANK
				&& accountType != ClientAccount.TYPE_CREDIT_CARD
				&& accountType != ClientAccount.TYPE_PAYPAL) {
			getSubAccounts();
			selectedSubAccount = null;
			subhierarchy = null;

			if (bankForm != null)
				topHLay.remove(bankForm);
			if (creditCardForm != null)
				topHLay.remove(creditCardForm);

			accNoText.setToolTip(messages.accountNumberToolTipDesc("4000",
					"4999"));
			accTypeSelect.setComboItem(Utility
					.getAccountTypeString(accountType));
		}

		else {
			selectedBank = null;
			accTypeSelect.setSelected(selectedId);

			if (accountType == ClientAccount.TYPE_BANK) {

				addBankForm();
			} else if (accountType == ClientAccount.TYPE_PAYPAL) {
				addPaypalForm();
			}

			else if (accountType == ClientAccount.TYPE_CREDIT_CARD) {
				addCreditCardForm();

			}
		}
		// accInfoForm.getCellFormatter().setWidth(0, 0, "200");
		// if (isNewBankAccount())
		// lab1.setText(" " + messages.bankAccount());
		// else
		lab1.setText(" " + Utility.getAccountTypeString(this.accountType) + " "
				+ messages.account());

	}

	private DropDownCombo getBankNameSelectItem() {

		if (bankNameSelect == null) {

			bankNameSelect = new BankNameCombo(messages.bankName());
			bankNameSelect.setDisabled(isInViewMode());
			// bankNameSelect.setWidth(100);
			bankNameSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientBank>() {

						@Override
						public void selectedComboBoxItem(ClientBank selectItem) {
							if (selectItem != null) {
								selectedBank = selectItem;
								if (!allBanks.contains(selectedBank))
									allBanks.add(selectedBank);

							} else
								selectedBank = null;
						}

					});
			if (allBanks == null)
				getBanks();
			else
				initBankNameSelect();
		}

		return bankNameSelect;
	}

	private void addCreditCardForm() {

		if (bankForm != null) {

			topHLay.remove(bankForm);
		}
		if (paypalForm != null) {
			topHLay.remove(paypalForm);
		}

		bankNameSelect = null;
		if (creditCardForm == null) {
			lab1.setText(messages.creditCardAccount());
			typeSelect = new SelectCombo(messages.creditCardType());
			typeSelect.setDisabled(isInViewMode());
			// typeSelect.setWidth(100);
			// typeSelect.setWidth("*");
			typeMap = new ArrayList<String>();

			typeMap.add(messages.addNewType());
			typeMap.add(messages.master());
			typeMap.add(messages.visa());
			typeSelect.initCombo(typeMap);
			typeSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

						@Override
						public void selectedComboBoxItem(String selectItem) {
							if (selectItem != null)
								typeSelect.setComboItem(selectItem);

						}
					});
			typeSelect.setRequired(false);

			limitText = new AmountField(messages.creditLimit(), this,
					getBaseCurrency());
			limitText.setHelpInformation(true);
			limitText.setWidth(100);
			limitText.setValue("" + UIUtils.getCurrencySymbol() + "0");
			limitText.addBlurHandler(new BlurHandler() {

				@Override
				public void onBlur(BlurEvent event) {
					Double limit = 0D;
					try {
						if (limitText.getValue() == null) {
							Accounter.showError(messages
									.creditLimitShouldNotBeNull());
						} else {
							limit = DataUtils.getAmountStringAsDouble(limitText
									.getValue().toString());
							if (DecimalUtil
									.isLessThan(limit, -1000000000000.00)
									|| DecimalUtil.isGreaterThan(limit,
											1000000000000.00)) {
								Accounter.showError(messages
										.creditLimitShouldBeInTheRange());
								limit = 0D;
							}
						}
					} catch (Exception e) {
						Accounter.showError(messages.invalidCreditLimit());
					} finally {
						limitText.setValue(DataUtils
								.getAmountAsStringInPrimaryCurrency(limit));
						setCreditLimit(limit);
					}
				}

			});
			limitText.setDisabled(isInViewMode());
			cardNumText = new IntegerField(this, messages.cardOrLoanNumber());
			cardNumText.setHelpInformation(true);
			cardNumText.setWidth(100);
			cardNumText.setDisabled(isInViewMode());
			creditCardForm = UIUtils.form(messages
					.creditCardAccountInformation());
			creditCardForm.setDisabled(isInViewMode());
			creditCardForm.setFields(limitText, cardNumText);
			creditCardForm.setWidth("100%");
			// creditCardForm.setAutoHeight();
		} else {
			if (bankNameSelect == null)
				getBankNameSelectItem();
			reset(creditCardForm);
		}
		topHLay.add(creditCardForm);
		topHLay.setCellHorizontalAlignment(creditCardForm, ALIGN_RIGHT);

	}

	private void addBankForm() {
		AccounterMessages messages = Global.get().messages();
		if (creditCardForm != null) {

			topHLay.remove(creditCardForm);
		}
		if (paypalForm != null)
			topHLay.remove(paypalForm);
		bankNameSelect = null;

		if (bankForm == null) {

			typeSelect = new SelectCombo(messages.bankAccountType());
			typeSelect.setDisabled(isInViewMode());
			// typeSelect.setWidth(100);
			// typeSelect.setWidth("*");
			typeMap = new ArrayList<String>();
			typeMap.add(messages.cuurentAccount());
			typeMap.add(messages.checking());
			typeMap.add(messages.moneyMarket());
			typeMap.add(messages.saving());
			typeSelect.initCombo(typeMap);
			typeSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

						@Override
						public void selectedComboBoxItem(String selectItem) {
							if (selectItem != null)
								typeSelect.setComboItem(selectItem);

						}
					});
			typeSelect.setRequired(true);
			// typeSelect.setDefaultToFirstOption(Boolean.TRUE);
			bankAccNumText = new TextItem(messages.bankAccountNumber());
			bankAccNumText.setHelpInformation(true);
			bankAccNumText.setWidth(100);
			bankForm = UIUtils.form(messages.bankAccountInformation());
			// bankForm.setWidth("100%");
			if (isMultiCurrencyEnabled()) {
				bankForm.setFields(getBankNameSelectItem(), typeSelect,
						bankAccNumText);
			} else {
				bankForm.setFields(getBankNameSelectItem(), typeSelect,
						bankAccNumText);
			}

			// bankForm.setWidth("100%");
			// bankForm.setAutoHeight();

		} else {
			if (bankNameSelect == null)
				getBankNameSelectItem();
			reset(bankForm);
		}

		topHLay.add(bankForm);
		topHLay.setCellHorizontalAlignment(bankForm, ALIGN_RIGHT);

	}

	protected void updateCurrencyForItems(ClientCurrency selectItem) {
		currentBalanceText.setCurrency(selectItem);
		opBalText.setCurrency(selectItem);
		if (limitText != null) {
			limitText.setCurrency(selectItem);
		}
	}

	public Double getAmountInBaseCurrency(Double amount) {
		if (selectCurrency != null) {
			return amount * currencyFactor;
		} else {
			return amount;
		}
	}

	private void initAccountTypeSelect() {
		if (accountType != 0) {
			accTypeSelect.setComboItem(Utility
					.getAccountTypeString(accountType));
			// accTypeSelect.setDisabled(true);
			getNextAccountNo();
			accTypeSelect.initCombo(getAccountsList());
			accNoText.setToolTip(messages.accountNumberToolTipDesc("1100",
					"1179"));

		} else {

			if (!isInViewMode()) {
				accTypeSelect.initCombo(getAccountsList());
				setAccountType(Integer.parseInt(defaultId));
				accTypeSelect.setComboItem(Utility.getAccountTypeString(Integer
						.parseInt(defaultId)));
				accounttype_selected();

			} else {

				accTypeSelect.setComboItem(Utility.getAccountTypeString(data
						.getType()));
				accTypeSelect.setDisabled(true);
				setAccountType(data.getType());
			}
		}
		String accountTypeString = Utility.getAccountTypeString(accountType);
		if (accountTypeString != null) {
			accTypeSelect.setSelected(accountTypeString);
		}
		// if (isNewBankAccount())
		// lab1.setText(" " + messages.bankAccount());
		// else

		lab1.setText(" " + Utility.getAccountTypeString(accountType) + " "
				+ messages.Account());

	}

	private List<String> getAccountsList() {
		// List<String> list=new ArrayList<String>();
		// list.add("Income");
		// return list;
		List<String> list = new ArrayList<String>();
		if (accountTypes != null && accountTypes.size() != 0) {
			for (int type : accountTypes) {
				list.add(Utility.getAccountTypeString(type));
			}
			defaultId = String.valueOf(accountTypes.get(0));

		} else {
			for (int type : UIUtils.accountTypes) {
				// if (getCompany().getAccountingType() !=
				// ClientCompany.ACCOUNTING_TYPE_UK)
				// list.add(Utility.getAccountTypeString(type));
				// else if (type != ClientAccount.TYPE_CASH
				// && type != ClientAccount.TYPE_OTHER_INCOME
				// && type != ClientAccount.TYPE_INVENTORY_ASSET
				// && type != ClientAccount.TYPE_CREDIT_CARD
				// && type != ClientAccount.TYPE_PAYROLL_LIABILITY) {
				// if (type == ClientAccount.TYPE_BANK)
				// continue;
				list.add(Utility.getAccountTypeString(type));
				// }
			}
			if (accountType != ClientAccount.TYPE_BANK
					&& accountType != ClientAccount.TYPE_CREDIT_CARD) {
				defaultId = String.valueOf(UIUtils.accountTypes[0]);
				accountType = UIUtils.accountTypes[0];
			}
		}
		return list;
	}

	protected String getValidAmountString(String amount) {
		if (amount.lastIndexOf("" + UIUtils.getCurrencySymbol() + "") == 0)
			amount = amount.substring(1, amount.length());
		else if (amount.lastIndexOf("" + UIUtils.getCurrencySymbol() + "") == amount
				.length() - 1)
			amount = amount.substring(0, amount.length() - 1);
		else if (amount.lastIndexOf("" + UIUtils.getCurrencySymbol() + "") == 1
				&& amount.startsWith("-"))
			amount = "-" + amount.substring(2, amount.length());
		else if (amount.startsWith("("))
			amount = "-" + amount.substring(3, amount.length() - 1);
		return amount;
	}

	@Override
	public ClientAccount saveView() {
		ClientAccount saveView = super.saveView();
		if (saveView != null) {
			updateAccountObject();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {
		updateAccountObject();

		saveOrUpdate(getData());

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		// BaseView.errordata.setHTML(exception.getMessage());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		String exceptionMessage = exception.getMessage();
		// addError(this, exceptionMessage);
		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

		updateAccountObject();
		if (exceptionMessage != null) {
			if (exceptionMessage.contains("number"))
				data.setNumber(accountNo);
			if (exceptionMessage.contains("name"))
				data.setName(accountName);
		}
		// if (takenAccount == null)
		// else
		// Accounter.showError(FinanceApplication.constants()
		// .accountUpdationFailed());
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			ClientAccount account = (ClientAccount) result;
			if (getMode() == EditMode.CREATE) {
				account.setCurrentBalance(account.getOpeningBalance());
				account.setTotalBalanceInAccountCurrency(account
						.getOpeningBalance() / account.getCurrencyFactor());
			}
			super.saveSuccess(result);
		} else {
			saveFailed(new AccounterException());
		}

	}

	public void reload() {
		// if (isNewBankAccount)
		// ActionFactory.getNewBankAccountAction().run(null, true);
		// else
		ActionFactory.getNewAccountAction().run(null, true);
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		// accinfoform valid?
		// check whether the account is already available or not
		// valid account no?
		// is prior to company prevent posting date?
		// bankform valid?

		if (asofDate.getDate().getDate() == 0) {
			result.addError(asofDate, messages.pleaseSelect(messages.asOf()));
			return result;
		}

		result.add(accInfoForm.validate());
		String name = accNameText.getValue().toString() != null ? accNameText
				.getValue().toString() : "";
		ClientCompany company = getCompany();
		ClientAccount account = company.getAccountByName(name);
		if (name != null && !name.isEmpty()) {
			if (!isInViewMode() ? (account == null ? false
					: data.getName() == null ? true : !data.getName()
							.equalsIgnoreCase(name)) : account != null) {

				result.addError(accNameText, messages.alreadyExist());
				return result;
			}
		}
		// long number = accNoText.getNumber();
		// account = company.getAccountByNumber(number);
		// if (!isInViewMode() ? (account == null ? false :
		// !(Long.parseLong(data
		// .getNumber()) == number)) : account != null) {
		//
		// result.addError(accNameText, messages
		// .alreadyAccountExist(Global.get().Account()));
		// return result;
		// }

		if (!(isInViewMode() && data.getName().equalsIgnoreCase(
				messages.openingBalances()))) {
			validateAccountNumber(accNoText.getNumber());
		}
		if (isSubAccountBox.getValue()) {
			ClientAccount selectedValue = parentAccountCombo.getSelectedValue();
			if (selectedValue == null) {
				result.addError(accTypeSelect,
						messages.pleaseSelect(messages.parentAccount()));
				return result;
			}
			if (selectedValue.getType() != getAccountType(accTypeSelect
					.getSelectedValue())) {
				result.addError(accTypeSelect,
						messages.parenAccountTypeShouldBeSame());
			}
			if (selectedValue.getCurrency() != selectCurrency.getID()) {
				result.addError(accTypeSelect,
						messages.parenAccountCurrencyShouldBeSame());
			}
			if (selectedValue.getID() == data.getID()) {
				result.addError(accTypeSelect,
						messages.theAccCannotbeSubaccOfIt());
			}
		}
		if (AccounterValidator.isPriorToCompanyPreventPostingDate(asofDate
				.getEnteredDate())) {
			result.addError(asofDate, messages.priorasOfDate());
		}
		if (accountType == ClientAccount.TYPE_BANK) {
			result.add(bankForm.validate());
		}
		if (accountType == ClientAccount.TYPE_CREDIT_CARD) {
			result.add(creditCardForm.validate());
		}
		if (accountType == ClientAccount.TYPE_PAYPAL) {
			result.add(paypalForm.validate());
		}

		return result;

	}

	public Double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public Double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(Double creditLimit) {
		this.creditLimit = creditLimit;
	}

	// protected SimplePanel getWidget() {
	// return this;
	// }

	private void updateAccountObject() {
		AccounterMessages messages = Global.get().messages();
		data.setType(accountType);
		data.setOpeningBalanceEditable(true);
		data.setNumber(accNoText.getNumber() != null ? accNoText.getNumber()
				.toString() : "0");
		data.setName(accNameText.getValue().toString() != null ? accNameText
				.getValue().toString() : "");
		data.setIsActive(statusBox.getValue() != null ? (Boolean) statusBox
				.getValue() : Boolean.FALSE);
		if (cashAccountCheck != null)
			data.setConsiderAsCashAccount(cashAccountCheck.getValue());
		if (cashFlowCatSelect.getValue() != null)
			data.setCashFlowCategory(cashFlowCatSelect.getSelectedIndex() + 1);
		// data.setCashFlowCategory(0);
		data.setOpeningBalance(opBalText.getAmount());
		data.setAsOf(asofDate.getEnteredDate().getDate());
		data.setCurrencyFactor(currencyFactor);
		if (isSubAccountBox.getValue()) {
			ClientAccount parentAccount = parentAccountCombo.getSelectedValue();
			if (parentAccount != null) {
				data.setParent(parentAccount.getID());
			}
		}
		switch (accountType) {
		case ClientAccount.TYPE_BANK:
			((ClientBankAccount) data).setBank(Utility.getID(selectedBank));
			if (typeSelect.getSelectedValue() != null) {
				int type = 0;
				if (typeSelect.getSelectedValue().equals(
						messages.cuurentAccount()))
					type = ClientAccount.BANK_ACCCOUNT_TYPE_CURRENT_ACCOUNT;
				else if (typeSelect.getSelectedValue().equals(
						messages.checking()))
					type = ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING;
				else if (typeSelect.getSelectedValue().equals(
						messages.moneyMarket()))
					type = ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET;
				else if (typeSelect.getSelectedValue()
						.equals(messages.saving()))
					type = ClientAccount.BANK_ACCCOUNT_TYPE_SAVING;
				((ClientBankAccount) data).setBankAccountType(type);
			}
			((ClientBankAccount) data).setBankAccountNumber(bankAccNumText
					.getValue().toString());
			data.setIncrease(Boolean.FALSE);
			break;
		case ClientAccount.TYPE_CREDIT_CARD:
			if (limitText.getValue() != null)
				data.setCreditLimit(getCreditLimit());
			if (cardNumText.getValue() != null)
				data.setCardOrLoanNumber(cardNumText.getValue().toString());
			break;
		case ClientAccount.TYPE_PAYPAL:
			if (paypalEmail.getValue() != null) {
				data.setPaypalEmail(paypalEmail.getValue());
			}
		default:
			if (selectedSubAccount != null)
				data.setParent(selectedSubAccount.getID());
			if (hierText != null)
				data.setHierarchy(UIUtils.toStr(hierText.getValue()));
			break;
		}
		data.setComment(commentsArea.getValue() != null ? commentsArea
				.getValue().toString() : "");
		if (data.getType() == ClientAccount.TYPE_INCOME
				|| data.getType() == ClientAccount.TYPE_OTHER_INCOME
				|| data.getType() == ClientAccount.TYPE_CREDIT_CARD
				|| data.getType() == ClientAccount.TYPE_PAYROLL_LIABILITY
				|| data.getType() == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
				|| data.getType() == ClientAccount.TYPE_LONG_TERM_LIABILITY
				|| data.getType() == ClientAccount.TYPE_EQUITY
				|| data.getType() == ClientAccount.TYPE_ACCOUNT_PAYABLE) {
			data.setIncrease(Boolean.TRUE);
		} else {
			data.setIncrease(Boolean.FALSE);
		}
		data.updateBaseTypes();
		if (data.isAllowCurrencyChange() && selectCurrency != null) {
			data.setCurrency(selectCurrency.getID());
		}
	}

	@Override
	public void init() {
		super.init();
		createControls();
		// setSize("100%", "100%");
		// setOverflow(Overflow.AUTO);

	}

	@Override
	public void initData() {
		if (getData() == null) {
			ClientAccount account = accountType != ClientAccount.TYPE_BANK ? new ClientAccount(
					getCompany().getPrimaryCurrency().getID())
					: new ClientBankAccount(getCompany().getPrimaryCurrency()
							.getID());
			setData(account);
			initAccountTypeSelect();
		} else {
			accountType = data.getType();
			if (accountType != ClientAccount.TYPE_BANK
					&& accountType != ClientAccount.TYPE_CREDIT_CARD)
				getSubAccounts();
			if (accountType == ClientAccount.TYPE_PAYPAL) {
				getPaypalData();
			}
			initView();
			super.initData();
		}
		// if (takenAccount == null)
		// getNextAccountNumber();

	}

	private void getPaypalData() {
		if (data.getPaypalEmail() != null) {
			paypalEmail.setValue(data.getPaypalEmail());
		}
	}

	private void initView() {

		accTypeSelect
				.setComboItem(Utility.getAccountTypeString(data.getType()));
		accNoText.setValue(data.getNumber() != null ? String.valueOf(data
				.getNumber()) : "");
		accountNo = data.getNumber() != null ? data.getNumber() : "0";
		if (data.getID() == getCompany().getOpeningBalancesAccount())
			accNoText.setDisabled(isInViewMode());

		accNameText.setValue(data.getName());
		accountName = data.getName();
		long id = data.getID();
		if (id == getCompany().getOpeningBalancesAccount()
				|| id == getCompany().getAccountsReceivableAccountId()
				|| id == getCompany().getAccountsPayableAccount())
			accNameText.setDisabled(isInViewMode());
		// statusBox.setValue(data.getIsActive() != null ? data
		// .getIsActive() : Boolean.FALSE);
		statusBox.setValue(data.getIsActive());
		if (data.getCashFlowCategory() != 0) {
			String cashFlow = getCashFlowCategory(data.getCashFlowCategory());
			cashFlowCatSelect.setValue(cashFlow);
		}

		opBalText.setAmount(data.getOpeningBalance());
		currentBalanceText.setAmount(data.getTotalBalanceInAccountCurrency());
		// if (!data.isOpeningBalanceEditable()) {
		// opBalText.setDisabled(true);
		// }
		// Enable Opening Balance to All Balancesheet accounts
		enableOpeningBalaceTxtByType();

		isSubAccountBox.setValue(data.getParent() != 0);
		ClientAccount account = getCompany().getAccount(data.getParent());
		if (account != null) {
			parentAccountCombo.setVisible(true);
			parentAccountCombo.setComboItem(account);
		}

		asofDate.setValue(new ClientFinanceDate(
				data.getAsOf() == 0 ? new ClientFinanceDate().getDate() : data
						.getAsOf()));
		asofDate.setDisabled(isInViewMode());
		cashAccountCheck.setValue(data.isConsiderAsCashAccount());
		commentsArea.setValue(data.getComment());
		if (accountType == ClientAccount.TYPE_BANK) {

			if (((ClientBankAccount) data).getBankAccountType() != 0) {
				String type = getBankAccountType(((ClientBankAccount) data)
						.getBankAccountType());
				typeSelect.setComboItem(type);
				bankAccNumText.setValue(((ClientBankAccount) data)
						.getBankAccountNumber());

				bankAccNumText.setDisabled(isInViewMode());
			}

		} else if (accountType == ClientAccount.TYPE_CREDIT_CARD) {
			setCreditLimit(!DecimalUtil.isEquals(data.getCreditLimit(), 0) ? data
					.getCreditLimit() : 0D);
			limitText.setValue(DataUtils
					.getAmountAsStringInPrimaryCurrency(getCreditLimit()));
			cardNumText.setValue(data.getCardOrLoanNumber() != null ? data
					.getCardOrLoanNumber() : "");
		} else {
			selectedSubAccount = getCompany().getAccount(data.getParent());

			if (selectedSubAccount != null && subAccSelect != null) {
				subAccSelect.setComboItem(selectedSubAccount);
				subhierarchy = Utility.getHierarchy(selectedSubAccount);
			}
			if (data.getHierarchy() != null) {
				hierText.setValue(data.getHierarchy());
			}

		}
		if (data.isAllowCurrencyChange() && isMultiCurrencyEnabled()) {
			initCurrencyFactor();
		}

	}

	private void initCurrencyFactor() {
		if (data.getCurrency() > 0) {
			this.selectCurrency = getCompany().getCurrency(data.getCurrency());
		} else {
			this.selectCurrency = getCompany().getPrimaryCurrency();
		}
		this.currencyFactor = data.getCurrencyFactor();
		if (this.selectCurrency != null) {
			currencyCombo.setSelectedCurrency(this.selectCurrency);
		}
		currencyCombo.setCurrencyFactor(data.getCurrencyFactor());
		if (!selectCurrency.equals(getCompany().getPreferences()
				.getPrimaryCurrency())) {
			currencyCombo.disabledFactorField(false);
		}
		currencyCombo.setDisabled(isInViewMode());
		updateCurrencyForItems(selectCurrency);
	}

	private void enableOpeningBalaceTxtByType() {
		boolean isBalanceSheetTye = Arrays.asList(
				ClientAccount.TYPE_OTHER_CURRENT_ASSET,
				ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
				ClientAccount.TYPE_FIXED_ASSET,
				ClientAccount.TYPE_LONG_TERM_LIABILITY,
				ClientAccount.TYPE_EQUITY).contains(data.getType());
		if (data.getNumber() != null) {
			long number = Long.parseLong(data.getNumber());
			// Checking whether type is under others accounts category.
			isBalanceSheetTye = !isBalanceSheetTye ? number >= 9501
					&& number <= 9600 : isBalanceSheetTye;
		}

		// if (isBalanceSheetTye
		// && !DecimalUtil.isEquals(data.getTotalBalance(), 0.0)) {
		// opBalText.setDisabled(false);
		// }

	}

	private String getCashFlowCategory(int i) {
		AccounterMessages messages = Global.get().messages();
		switch (i) {
		case ClientAccount.CASH_FLOW_CATEGORY_FINANCING:
			return messages.financing();

		case ClientAccount.CASH_FLOW_CATEGORY_INVESTING:
			return messages.investing();

		case ClientAccount.CASH_FLOW_CATEGORY_OPERATING:
			return messages.operating();
		default:
			break;
		}
		return "";
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	@Override
	public void setData(ClientAccount data) {
		super.setData(data);
		if (data != null) {
			setAccountType(data.getType());
		}
	}

	public void setAccountTypes(List<Integer> accountTypes) {
		this.accountTypes = accountTypes;
	}

	// public boolean isNewBankAccount() {
	// return isNewBankAccount;
	// }
	//
	// public void setNewBankAccount(boolean isNewBankAccount) {
	//
	// this.isNewBankAccount = isNewBankAccount;
	// setAccountType(ClientAccount.TYPE_BANK);
	//
	// }

	private int getAccountType(String name) {
		for (int type : UIUtils.accountTypes) {
			if (name.equals(Utility.getAccountTypeString(type)))
				return type;
		}
		return 0;
	}

	public void reset(DynamicForm form) {
		for (FormItem item : form.getFormItems()) {
			if (item instanceof DateField)
				((DateField) item)
						.setEnteredDate(new ClientFinanceDate(
								getCompany().getPreferences()
										.getPreventPostingBeforeDate() == 0 ? new ClientFinanceDate()
										.getDate() : getCompany()
										.getPreferences()
										.getPreventPostingBeforeDate()));
			else if ((item instanceof AmountField))
				((AmountField) item).setAmount(0.0);
			else if ((item instanceof CheckboxItem))
				item.setValue(false);
			else if ((item instanceof CustomCombo<?>))
				item.setValue(null);
			else
				item.setValue("");
		}

	}

	@Override
	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.accTypeSelect.setFocus();
	}

	// private void getNextAccountNumber() {
	// rpcUtilService.getNextNominalCode(this.accountType,
	// new AccounterAsyncCallback<Long>() {
	//
	// @Override
	// public void onException(AccounterException caught) {
	//
	// }
	//
	// @Override
	// public void onSuccess(Long result) {
	// if (result != null) {
	// accNoText.setNumber(result);
	// accNameText.setFocus();
	// }
	//
	// }
	//
	// });
	// }

	/**
	 * This function autogenerates a account number when its disabled
	 * 
	 * @param range1
	 * @param range2
	 * @return long number
	 */
	public long autoGenerateAccountnumber(int range1, int range2) {
		// TODO::: add a filter to filter the accounts based on the account type
		List<ClientAccount> accounts = getCompany().getAccounts();
		Long number = null;
		if (number == null) {
			number = (long) range1;
			for (ClientAccount account : accounts) {
				while (number.toString().equals(account.getNumber())) {
					number++;
					if (number >= range2) {
						number = (long) range1;
					}
				}
			}
		}
		return number;
	}

	private boolean validateAccountNumber(Long number) {

		if (number == null)
			return true;

		List<ClientAccount> accounts = getCompany().getAccounts();
		if (!isInViewMode()) {
			for (ClientAccount account : accounts) {
				if (number.toString().equals(account.getNumber())
						&& account.getID() != getData().getID()) {
					addError(accNoText, messages.alreadyAccountExist());
					return false;
				}
			}
		}
		// if (isNewBankAccount()) {
		// if (number < 1100 || number > 1179) {
		// addError(
		// accNoText,
		// Accounter
		// .messages()
		// .theAccountNumberchosenisincorrectPleasechooseaNumberbetween1100and1179(
		// Global.get().account()));
		// return false;
		// } else {
		// clearError(accNoText);
		// }
		// } else {
		accountSubBaseType = UIUtils.getAccountSubBaseType(accountType);

		nominalCodeRange = getCompany().getNominalCodeRange(accountSubBaseType);

		if (nominalCodeRange == null
				&& accountSubBaseType == ClientAccount.SUBBASETYPE_OTHER_ASSET) {
			return true;
		}

		// Checking the account number range by company preferences
		if (getCompany().getPreferences().isAccountnumberRangeCheckEnable()) {
			if (number < nominalCodeRange[0] || number > nominalCodeRange[1]) {
				addError(
						accNoText,
						messages.theAccountNumberchosenisincorrectPleaschooseaNumberbetween()
								+ "  "
								+ nominalCodeRange[0]
								+ "  "
								+ messages.to() + "  " + nominalCodeRange[1]);
				return false;
			} else {
				clearError(accNoText);
			}
		}
		// }

		accNoText.setValue(String.valueOf(number));

		return true;

	}

	private String getBankAccountType(int type) {
		AccounterMessages messages = Global.get().messages();
		switch (type) {
		case ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING:
			return messages.checking();

		case ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET:
			return messages.moneyMarket();
		case ClientAccount.BANK_ACCCOUNT_TYPE_SAVING:
			return messages.saving();
		case ClientAccount.BANK_ACCCOUNT_TYPE_CURRENT_ACCOUNT:
			return messages.cuurentAccount();
		default:
			break;
		}

		return "";

	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	public void accounttype_selected() {
		if (selectedId != null && !selectedId.isEmpty())
			setAccountType(getAccountType(selectedId));
		// getNextAccountNumber();
		if (accountType == ClientAccount.TYPE_BANK) {
			ClientBankAccount account = new ClientBankAccount();
			setData(account);
		}
		setCashFlowType();
		resetView();
		statusBox.setValue(true);
		getNextAccountNo();
		currencyCombo.setVisible(ClientAccount
				.isAllowCurrencyChange(accountType));
	}

	private void getNextAccountNo() {
		long nextAccountNumber = getCompany().getNextAccountNumber(
				UIUtils.getAccountSubBaseType(accountType));
		if (nextAccountNumber != -1) {
			accNoText.setValue(String.valueOf(nextAccountNumber));
		}
	}

	@Override
	public void onEdit() {

		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				Accounter.showError(AccounterExceptions
						.getErrorString(errorCode));
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		this.rpcDoSerivce.canEdit(AccounterCoreType.ACCOUNT, data.getID(),
				editCallBack);
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		opBalText.setDisabled(isInViewMode());
		asofDate.setDisabled(isInViewMode());
		accNoText.setDisabled(isInViewMode());
		accNameText.setDisabled(isInViewMode());
		cashAccountCheck.setDisabled(isInViewMode());
		statusBox.setDisabled(isInViewMode());
		commentsArea.setDisabled(isInViewMode());
		if (bankAccNumText != null) {
			bankAccNumText.setDisabled(isInViewMode());
		}
		if (creditCardForm != null) {
			creditCardForm.setDisabled(isInViewMode());
		}
		accTypeSelect.setDisabled(isInViewMode());

		// if (currencyCombo != null && data.isAllowCurrencyChange()) {
		currencyCombo.setDisabled(!isInViewMode(), isInViewMode());
		// if (!selectCurrency.equals(getCompany().getPreferences()
		// .getPrimaryCurrency())) {
		// currencyCombo.disabledFactorField(false);
		// }
		// }
		if (limitText != null) {
			limitText.setDisabled(isInViewMode());
		}

		if (cardNumText != null) {
			cardNumText.setDisabled(isInViewMode());
		}
		if (accountType == ClientAccount.TYPE_PAYPAL) {
			if (typeSelect != null) {
				typeSelect.setDisabled(isInViewMode());
			}
			paypalEmail.setDisabled(isInViewMode());
		}
		isSubAccountBox.setDisabled(isInViewMode());
		parentAccountCombo.setDisabled(isInViewMode());
		super.onEdit();

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return messages.Account();
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		if ((accountType == ClientAccount.TYPE_BANK || accountType == ClientAccount.TYPE_CREDIT_CARD)
				&& getData() != null) {
			Button reconcileBtn = new Button(messages.Reconcile());
			reconcileBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ClientReconciliation clientReconciliation = new ClientReconciliation();
					clientReconciliation.setAccount(data);
					ReconciliationDialog dialog = new ReconciliationDialog(
							Global.get().messages().Reconciliation(),
							clientReconciliation);
					dialog.show();
				}
			});
			buttonBar.add(reconcileBtn);

			Button uploadStatementBtn = new Button(messages.uploadAttachment());
			uploadStatementBtn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					UploadStatementDialog dialog = new UploadStatementDialog(
							messages.uploadAttachment(), data);
					dialog.show();

				}
			});
			if (accountType == ClientAccount.TYPE_BANK) {
				// buttonBar.add(uploadStatementBtn);
			}
			buttonBar.setCellHorizontalAlignment(reconcileBtn,
					HasHorizontalAlignment.ALIGN_LEFT);
		}
		super.createButtons(buttonBar);
	}

	private void settabIndexes() {
		accTypeSelect.setTabIndex(1);
		accNoText.setTabIndex(2);
		accNameText.setTabIndex(3);
		statusBox.setTabIndex(4);
		opBalText.setTabIndex(5);
		asofDate.setTabIndex(6);
		cashAccountCheck.setTabIndex(7);
		commentsArea.setTabIndex(8);
		// currency.setTabIndex(9);
		if (bankNameSelect != null) {
			bankNameSelect.setTabIndex(10);
		}
		if (typeSelect != null) {
			typeSelect.setTabIndex(11);
		}
		if (bankAccNumText != null) {
			bankAccNumText.setTabIndex(12);
		}
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(13);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(14);
		cancelButton.setTabIndex(15);

	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	protected CurrencyComboWidget createCurrencyComboWidget() {
		ArrayList<ClientCurrency> currenciesList = getCompany().getCurrencies();
		ClientCurrency baseCurrency = getCompany().getPrimaryCurrency();
		selectCurrency = baseCurrency;
		CurrencyComboWidget widget = new CurrencyComboWidget(currenciesList,
				baseCurrency);
		widget.setListener(new CurrencyChangeListener() {

			@Override
			public void currencyChanged(ClientCurrency currency, double factor) {
				selectCurrency = currency;
				currencyFactor = factor;
				updateCurrencyForItems(currency);
			}
		});
		widget.setDisabled(isInViewMode());
		return widget;
	}

	@Override
	protected boolean canVoid() {
		return false;
	}
}
