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
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterClientConstants;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
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
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

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
	private SelectItem catSelect;
	private DynamicForm accInfoForm;
	private CheckboxItem cashAccountCheck;
	private DynamicForm cashBasisForm;
	private TextAreaItem commentsArea;
	private DynamicForm commentsForm;
	private DynamicForm commonForm;
	private SelectCombo typeSelect;
	private AmountField limitText;
	private IntegerField accNoText, cardNumText;
	private HorizontalPanel topHLay;
	private HorizontalPanel leftLayout;
	// private TextBox textbox;
	protected boolean isClose;
	private List<String> typeMap;
	protected List<ClientBank> allBanks;
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
	private LinkedHashMap<String, String> accountTypesMap;
	private boolean isNewBankAccount;

	private String defaultId;
	private String selectedId;

	private DynamicForm bankForm;
	private DynamicForm creditCardForm;

	private Label lab1;

	private int totalValidations = 4;

	VerticalPanel mainVLay;

	private ArrayList<DynamicForm> listforms;
	protected Long nextAccountNumber;
	private int accountSubBaseType;
	private Integer[] nominalCodeRange;

	String accountName;
	String accountNo;

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
		subAccSelect.initCombo(subAccounts);
		subAccSelect.setHelpInformation(true);
		if (selectedSubAccount != null)
			subAccSelect.setComboItem(selectedSubAccount);

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
		lab1.addStyleName(Accounter.constants().labelTitle());
		// lab1.setHeight("35px");
		hierarchy = new String("");
		accTypeSelect = new SelectCombo(Accounter.messages().accountType(
				Global.get().Account()));
		accTypeSelect.setHelpInformation(true);
		// accTypeSelect.setWidth(100);
		accTypeSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						selectedId = (String) accTypeSelect.getSelectedValue();
						accounttype_selected();
						int accountSubType = UIUtils
								.getAccountSubBaseType(getAccountType(selectedId));

						Integer accountNoRange[] = getCompany()
								.getNominalCodeRange(accountSubType);

						accNoText.setToolTip(Accounter.messages()
								.accountNumberToolTipDesc(
										accountNoRange[0] + "",
										accountNoRange[1] + ""));
					}
				});

		accNoText = new IntegerField(this, Accounter.messages().accountNo(
				Global.get().Account()));
		accNoText.setHelpInformation(true);
		accNoText.setRequired(true);
		accNoText.setWidth(100);
		accNoText.setDisabled(isInViewMode());
		accNoText.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (accNoText.getNumber() != null)
					validateAccountNumber(accNoText.getNumber());

			}
		});
		accNoText.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				// displayAndSetAccountNo();

			}
		});

		accNameText = new TextItem(Accounter.messages().accountName(
				Global.get().Account()));
		accNameText.setToolTip(Accounter.messages()
				.giveTheNameAccordingToYourID(this.getAction().getViewName()));
		accNameText.setHelpInformation(true);
		accNameText.setRequired(true);
		accNameText.setWidth(100);
		accNameText.setDisabled(isInViewMode());
		accNameText.addBlurHandler(new BlurHandler() {

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

		statusBox = new CheckboxItem(Accounter.constants().active());
		statusBox.setWidth(100);
		statusBox.setValue(true);
		statusBox.setDisabled(isInViewMode());

		cashFlowCatSelect = new SelectItem(Accounter.messages()
				.cashFlowCategory(Global.get().Account()));
		cashFlowCatSelect.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub

			}
		});
		cashFlowof = new LinkedHashMap<String, String>();

		cashFlowof.put(ClientAccount.CASH_FLOW_CATEGORY_FINANCING + "",
				AccounterClientConstants.CASH_FLOW_CATEGORY_FINANCING);
		cashFlowof.put(ClientAccount.CASH_FLOW_CATEGORY_INVESTING + "",
				AccounterClientConstants.CASH_FLOW_CATEGORY_INVESTING);
		cashFlowof.put(ClientAccount.CASH_FLOW_CATEGORY_OPERATING + "",
				AccounterClientConstants.CASH_FLOW_CATEGORY_OPERATING);

		cashFlowCatSelect.setValueMap(cashFlowof);

		opBalText = new AmountField(Accounter.constants().openingBalance(),
				this);
		opBalText.setToolTip(Accounter.messages().giveOpeningBalanceToThis(
				this.getAction().getViewName()));
		opBalText.setHelpInformation(true);

		opBalText.setWidth(100);
		opBalText.setValue("" + UIUtils.getCurrencySymbol() + "0.00");
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
		asofDate = new DateField(Accounter.constants().asOf());
		asofDate.setHelpInformation(true);
		asofDate.setToolTip(Accounter
				.messages()
				.selectDateWhenTransactioCreated(this.getAction().getViewName()));
		// asofDate.setWidth(100);
		asofDate.setEnteredDate(new ClientFinanceDate(
				getCompany().getPreferences().getPreventPostingBeforeDate() == 0 ? new ClientFinanceDate()
						.getDate() : getCompany().getPreferences()
						.getPreventPostingBeforeDate()));

		catSelect = new SelectItem(Accounter.constants().category1099());
		catSelect.setWidth(100);
		catSelect.setDisabled(true);

		accInfoForm = UIUtils.form(Accounter.messages()
				.chartOfAccountsInformation(Global.get().Account()));
		accInfoForm.setWidth("100%");

		topHLay = new HorizontalPanel();
		topHLay.setWidth("50%");
		leftLayout = new HorizontalPanel();
		leftLayout.setWidth("90%");

		if (accountType == 0
				|| (accountType != ClientAccount.TYPE_BANK && accountType != ClientAccount.TYPE_CREDIT_CARD)) {
			subAccSelect = new OtherAccountsCombo(Accounter.constants()
					.subCategoryof());
			subAccSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
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

			hierText = new TextItem(Accounter.constants().hierarchy());
			hierText.setHelpInformation(true);
			hierText.setDisabled(true);
			hierText.setWidth(100);
			// accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
			// statusBox, subAccSelect, hierText, cashFlowCatSelect,
			// opBalText, asofDate, catSelect);

			if (getPreferences().getUseAccountNumbers() == true) {
				accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
						statusBox, opBalText, asofDate);
			} else {
				accNoText.setNumber(autoGenerateAccountnumber(1100, 1179));
				accInfoForm.setFields(accTypeSelect, accNameText, statusBox,
						opBalText, asofDate);
			}

			leftLayout.add(accInfoForm);
			topHLay.add(leftLayout);

		} else {
			// accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
			// statusBox, cashFlowCatSelect, opBalText, asofDate,
			// catSelect);
			if (getPreferences().getUseAccountNumbers() == true) {
				accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
						statusBox, opBalText, asofDate);
			} else {
				accNoText.setNumber(autoGenerateAccountnumber(1100, 1179));
				accInfoForm.setFields(accTypeSelect, accNameText, statusBox,
						opBalText, asofDate);
			}

			leftLayout.add(accInfoForm);
			if (accountType == ClientAccount.TYPE_BANK)
				addBankForm();
			else {
				addCreditCardForm();
				topHLay.setWidth("100%");

			}

		}
		accInfoForm.getCellFormatter().setWidth(0, 0, "200");
		cashAccountCheck = new CheckboxItem(Accounter.messages()
				.thisIsConsideredACashAccount(Global.get().Account()));
		cashAccountCheck.setWidth(100);
		cashAccountCheck.setDisabled(isInViewMode());

		cashBasisForm = new DynamicForm();
		cashBasisForm.setIsGroup(true);
		cashBasisForm
				.setGroupTitle(Accounter.constants().cashBasisAccounting());
		cashBasisForm.setFields(cashAccountCheck);
		cashBasisForm.setWidth("100%");
		cashBasisForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "200");

		commentsArea = new TextAreaItem();
		commentsArea.setToolTip(Accounter.messages().writeCommentsForThis(
				this.getAction().getViewName()));
		commentsArea.setHelpInformation(true);
		commentsArea.setTitle(Accounter.constants().comments());
		commentsArea.setWidth(100);
		commentsArea.setDisabled(isInViewMode());
		// commentsArea.setShowTitle(false);

		commentsForm = UIUtils.form(Accounter.constants().comments());
		commentsForm.setWidth("50%");
		commentsForm.setFields(commentsArea);
		commentsForm.getCellFormatter().getElement(0, 0).getStyle()
				.setVerticalAlign(VerticalAlign.TOP);
		commentsForm.getCellFormatter().getElement(0, 0)
				.setAttribute(Accounter.constants().width(), "200");

		if (getData() == null) {
			ClientAccount account = accountType != ClientAccount.TYPE_BANK ? new ClientAccount()
					: new ClientBankAccount();
			setData(account);
		}

		mainVLay = new VerticalPanel();

		mainVLay.setSize("100%", "300px");
		mainVLay.add(lab1);
		mainVLay.add(topHLay);
		mainVLay.add(cashBasisForm);
		mainVLay.add(commentsForm);
		// setHeightForCanvas("450");
		this.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(accInfoForm);
		listforms.add(cashBasisForm);

		listforms.add(commentsForm);

	}

	protected void displayAndSetAccountNo() {
		long financeCategoryNumber = 0;

		if (isNewBankAccount()) {
			addError(
					accNoText,
					Accounter.messages()
							.theFinanceCategoryNoShouldBeBetween1100And1179(
									Global.get().Account()));
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
			addError(
					accNoText,
					Accounter.messages().theFinanceCategoryNoShouldBeBetween(
							Global.get().Account())
							+ "  "
							+ nominalCodeRange[0]
							+ " "
							+ Accounter.constants().and()
							+ " "
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
		reset(cashBasisForm);
		reset(commentsForm);
		if (accountType == ClientAccount.TYPE_INVENTORY_ASSET)
			opBalText.setDisabled(true);
		else
			opBalText.setDisabled(false);
		if (accountType != ClientAccount.TYPE_BANK
				&& accountType != ClientAccount.TYPE_CREDIT_CARD) {
			getSubAccounts();
			selectedSubAccount = null;
			subhierarchy = null;

			if (bankForm != null)
				topHLay.remove(bankForm);
			if (creditCardForm != null)
				topHLay.remove(creditCardForm);
			accInfoForm.removeAllRows();
			// accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
			// statusBox, subAccSelect, hierText, cashFlowCatSelect,
			// opBalText, asofDate, catSelect);
			if (getPreferences().getUseAccountNumbers() == true) {
				accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
						statusBox, opBalText, asofDate);
			} else {
				accNoText.setNumber(autoGenerateAccountnumber(4000, 4999));
				accInfoForm.setFields(accTypeSelect, accNameText, statusBox,
						opBalText, asofDate);
			}
			// leftLayout.add(accInfoForm);
			reset(accInfoForm);
			if (selectedId == null)
				accTypeSelect.setComboItem(Accounter.constants().income());
			accNoText.setToolTip(Accounter.messages().accountNumberToolTipDesc(
					"4000", "4999"));
			accTypeSelect.setSelected(selectedId);
			topHLay.setWidth("50%");

		}

		else {
			selectedBank = null;
			topHLay.add(accInfoForm);
			topHLay.remove(leftLayout);
			reset(accInfoForm);
			accInfoForm.removeAllRows();
			// accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
			// statusBox, cashFlowCatSelect, opBalText, asofDate,
			// catSelect);
			if (getPreferences().getUseAccountNumbers() == true) {
				accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
						statusBox, opBalText, asofDate);
			} else {
				accNoText.setNumber(autoGenerateAccountnumber(4000, 4999));
				accInfoForm.setFields(accTypeSelect, accNameText, statusBox,
						opBalText, asofDate);
			}
			leftLayout.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
			leftLayout.add(accInfoForm);
			accTypeSelect.setSelected(selectedId);

			if (accountType == ClientAccount.TYPE_BANK) {

				addBankForm();

			} else if (accountType == ClientAccount.TYPE_CREDIT_CARD) {
				addCreditCardForm();

			}

			topHLay.setWidth("100%");
			// topHLay.reflowNow();
			// mainVLay.reflowNow();

		}
		accInfoForm.getCellFormatter().setWidth(0, 0, "200");
		if (isNewBankAccount())
			lab1.setText(" "
					+ Accounter.messages().bankAccount(Global.get().Account()));
		else
			lab1.setText(" " + Utility.getAccountTypeString(this.accountType)
					+ " "
					+ Accounter.messages().account(Global.get().account()));

	}

	private DropDownCombo getBankNameSelectItem() {

		if (bankNameSelect == null) {

			bankNameSelect = new BankNameCombo(Accounter.constants().bankName());
			// bankNameSelect.setWidth(100);
			bankNameSelect
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientBank>() {

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
			bankNameSelect = null;
		}
		if (creditCardForm == null) {

			limitText = new AmountField(Accounter.constants().creditLimit(),
					this);
			limitText.setHelpInformation(true);
			limitText.setWidth(100);
			limitText.setValue("" + UIUtils.getCurrencySymbol() + "0");
			limitText.addBlurHandler(new BlurHandler() {

				public void onBlur(BlurEvent event) {
					Double limit = 0D;
					try {
						if (limitText.getValue() == null) {
							Accounter.showError(Accounter.constants()
									.creditLimitShouldNotBeNull());
						} else {
							limit = DataUtils.getAmountStringAsDouble(limitText
									.getValue().toString());
							if (DecimalUtil
									.isLessThan(limit, -1000000000000.00)
									|| DecimalUtil.isGreaterThan(limit,
											1000000000000.00)) {
								Accounter.showError(Accounter.constants()
										.creditLimitShouldBeInTheRange());
								limit = 0D;
							}
						}
					} catch (Exception e) {
						Accounter.showError(Accounter.constants()
								.invalidCreditLimit());
					} finally {
						limitText.setValue(DataUtils.getAmountAsString(limit));
						setCreditLimit(limit);
					}
				}

			});
			cardNumText = new IntegerField(this, Accounter.constants()
					.cardOrLoadNumber());
			cardNumText.setHelpInformation(true);
			cardNumText.setWidth(100);
			creditCardForm = UIUtils.form(Accounter.messages()
					.creditCardAccountInformation((Global.get().Account())));
			creditCardForm.setFields(getBankNameSelectItem(), limitText,
					cardNumText);
			creditCardForm.setWidth("100%");
			// creditCardForm.setAutoHeight();
		} else {
			if (bankNameSelect == null)
				getBankNameSelectItem();
			reset(creditCardForm);
		}
		accInfoForm.setWidth("100%");
		topHLay.add(leftLayout);
		topHLay.add(creditCardForm);

	}

	private void addBankForm() {

		if (creditCardForm != null) {

			topHLay.remove(creditCardForm);
			bankNameSelect = null;

		}

		if (bankForm == null) {

			typeSelect = new SelectCombo(Accounter.messages().bankAccountType(
					Global.get().Account()));
			// typeSelect.setWidth(100);
			// typeSelect.setWidth("*");
			typeMap = new ArrayList<String>();

			typeMap.add(AccounterClientConstants.BANK_ACCCOUNT_TYPE_CHECKING);
			typeMap.add(AccounterClientConstants.BANK_ACCCOUNT_TYPE_MONEY_MARKET);
			typeMap.add(AccounterClientConstants.BANK_ACCCOUNT_TYPE_SAVING);
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

			bankAccNumText = new TextItem(Accounter.messages()
					.bankAccountNumber(Global.get().account()));
			bankAccNumText.setHelpInformation(true);
			bankAccNumText.setWidth(100);

			// accNameText.setWidth("*");

			bankForm = UIUtils.form(Accounter.messages()
					.bankAccountInformation(Global.get().Account()));
			bankForm.setWidth("100%");
			bankForm.setFields(getBankNameSelectItem(), typeSelect,
					bankAccNumText);

			// bankForm.setWidth("100%");
			// bankForm.setAutoHeight();

		} else {
			if (bankNameSelect == null)
				getBankNameSelectItem();
			reset(bankForm);
		}
		accInfoForm.setWidth("100%");
		topHLay.setWidth("100%");
		leftLayout.setWidth("90%");
		topHLay.add(leftLayout);
		topHLay.add(bankForm);

	}

	private void initAccountTypeSelect() {

		if (isNewBankAccount) {
			accTypeSelect.setComboItem(Utility
					.getAccountTypeString(accountType));
			accTypeSelect.setDisabled(true);
		} else {

			if (!isInViewMode()) {
				accTypeSelect.initCombo(getAccountsList());
				setAccountType(Integer.parseInt(defaultId));
				accounttype_selected();

			} else {

				accTypeSelect.setComboItem(Utility.getAccountTypeString(data
						.getType()));
				accTypeSelect.setDisabled(true);
				setAccountType(data.getType());
			}
		}
		if (isNewBankAccount())
			lab1.setText(" "
					+ Accounter.messages().bankAccount(Global.get().Account()));
		else

			lab1.setText(" " + Utility.getAccountTypeString(accountType) + " "
					+ Accounter.messages().account(Global.get().account()));

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
				if (type == ClientAccount.TYPE_BANK)
					continue;
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

	private LinkedHashMap<String, String> getAccountTypesMap() {

		accountTypesMap = new LinkedHashMap<String, String>();
		if (accountTypes != null && accountTypes.size() != 0) {
			for (int type : accountTypes) {
				accountTypesMap.put(String.valueOf(type),
						Utility.getAccountTypeString(type));
			}
			defaultId = String.valueOf(accountTypes.get(0));

		} else {
			for (int type : UIUtils.accountTypes) {
				if (getCompany().getAccountingType() != ClientCompany.ACCOUNTING_TYPE_UK)
					accountTypesMap.put(String.valueOf(type),
							Utility.getAccountTypeString(type));
				else if (type != ClientAccount.TYPE_BANK
						&& type != ClientAccount.TYPE_CASH
						&& type != ClientAccount.TYPE_OTHER_INCOME
						&& type != ClientAccount.TYPE_INVENTORY_ASSET
						&& type != ClientAccount.TYPE_CREDIT_CARD
						&& type != ClientAccount.TYPE_PAYROLL_LIABILITY) {
					accountTypesMap.put(String.valueOf(type),
							Utility.getAccountTypeString(type));
				}
			}
			if (accountType != ClientAccount.TYPE_BANK
					&& accountType != ClientAccount.TYPE_CREDIT_CARD) {
				defaultId = String.valueOf(UIUtils.accountTypes[0]);
				accountType = UIUtils.accountTypes[0];
			}

		}

		return accountTypesMap;

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
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

		updateAccountObject();
		if (exceptionMessage.contains("number"))
			data.setNumber(accountNo);
		if (exceptionMessage.contains("name"))
			data.setName(accountName);
		// if (takenAccount == null)
		// else
		// Accounter.showError(FinanceApplication.constants()
		// .accountUpdationFailed());
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result == null) {
			super.saveSuccess(result);
			return;
		}
		// if (takenAccount == null)
		// Accounter.showInformation("New account with name "
		// + result.getName() + " is Created!");
		// else
		// Accounter.showInformation(result.getName()
		// + " is updated successfully");

		// if (this.yesClicked && accountType == ClientAccount.TYPE_CREDIT_CARD)
		// {
		if (accountType == ClientAccount.TYPE_CREDIT_CARD) {
			ActionFactory.getNewVendorAction().run(null, false);
		}

		super.saveSuccess(result);

	}

	public void reload() {
		if (isNewBankAccount)
			ActionFactory.getNewBankAccountAction().run(null, true);
		else
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

		result.add(accInfoForm.validate());
		String name = accNameText.getValue().toString() != null ? accNameText
				.getValue().toString() : "";
		ClientCompany company = getCompany();
		ClientAccount account = company.getAccountByName(name);
		if (name != null && !name.isEmpty()) {
			if (isInViewMode() ? (account == null ? false : !data.getName()
					.equalsIgnoreCase(name)) : account != null) {

				result.addError(accNameText, Accounter.constants()
						.alreadyExist());
				return result;
			}
		}
		long number = accNoText.getNumber();
		account = company.getAccountByNumber(number);
		if (isInViewMode() ? (account == null ? false : !(Long.parseLong(data
				.getNumber()) == number)) : account != null) {

			result.addError(accNameText, Accounter.messages()
					.alreadyAccountExist(Global.get().Account()));
			return result;
		}

		if (!(isInViewMode() && data.getName().equalsIgnoreCase(
				Accounter.constants().openingBalances()))) {
			validateAccountNumber(accNoText.getNumber());
		}
		if (AccounterValidator.isPriorToCompanyPreventPostingDate(asofDate
				.getEnteredDate())) {
			result.addError(asofDate, Accounter.constants().priorasOfDate());
		}
		if (accountType == ClientAccount.TYPE_BANK) {
			result.add(bankForm.validate());
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

		data.setType(accountType);
		data.setNumber(accNoText.getNumber() != null ? accNoText.getNumber()
				.toString() : "0");
		data.setName(accNameText.getValue().toString() != null ? accNameText
				.getValue().toString() : "");
		data.setIsActive(statusBox.getValue() != null ? (Boolean) statusBox
				.getValue() : Boolean.FALSE);
		if (cashAccountCheck != null)
			data.setConsiderAsCashAccount((Boolean) cashAccountCheck.getValue());

		if (cashFlowCatSelect.getValue() != null)
			data.setCashFlowCategory(cashFlowCatSelect.getSelectedIndex() + 1);
		// data.setCashFlowCategory(0);
		data.setOpeningBalance(opBalText.getAmount());
		data.setAsOf(asofDate.getEnteredDate().getDate());

		switch (accountType) {
		case ClientAccount.TYPE_BANK:
			((ClientBankAccount) data).setBank(Utility.getID(selectedBank));
			if (typeSelect.getSelectedValue() != null) {
				int type = 0;
				if (typeSelect.getSelectedValue().equals(
						AccounterClientConstants.BANK_ACCCOUNT_TYPE_CHECKING))
					type = ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING;
				else if (typeSelect
						.getSelectedValue()
						.equals(AccounterClientConstants.BANK_ACCCOUNT_TYPE_MONEY_MARKET))
					type = ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET;
				else if (typeSelect.getSelectedValue().equals(
						AccounterClientConstants.BANK_ACCCOUNT_TYPE_SAVING))
					type = ClientAccount.BANK_ACCCOUNT_TYPE_SAVING;
				((ClientBankAccount) data).setBankAccountType(type);
			}
			((ClientBankAccount) data).setBankAccountNumber(bankAccNumText
					.getValue().toString());
			data.setIncrease(Boolean.FALSE);
			break;
		case ClientAccount.TYPE_CREDIT_CARD:
			// data.setBank(Utility.getID(selectedBank));
			if (limitText.getValue() != null)
				data.setCreditLimit(getCreditLimit());
			if (cardNumText.getValue() != null)
				data.setCardOrLoanNumber(cardNumText.getValue().toString());
			break;
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

		initAccountTypeSelect();
		if (accountType != ClientAccount.TYPE_BANK
				&& accountType != ClientAccount.TYPE_CREDIT_CARD)
			getSubAccounts();
		if (isInViewMode())
			initView();
		super.initData();
		// if (takenAccount == null)
		// getNextAccountNumber();

	}

	private void initView() {

		accTypeSelect
				.setComboItem(Utility.getAccountTypeString(data.getType()));
		accNoText.setValue(data.getNumber() != null ? String.valueOf(data
				.getNumber()) : "");
		accountNo = data.getNumber() != null ? data.getNumber() : "0";

		if (data.getID() == getCompany().getOpeningBalancesAccount())
			accNoText.setDisabled(true);

		accNameText.setValue(data.getName());
		accountName = data.getName();
		long id = data.getID();
		if (id == getCompany().getOpeningBalancesAccount()
				|| id == getCompany().getAccountsReceivableAccountId()
				|| id == getCompany().getAccountsPayableAccount())
			accNameText.setDisabled(true);
		// statusBox.setValue(data.getIsActive() != null ? data
		// .getIsActive() : Boolean.FALSE);
		statusBox.setValue(data.getIsActive());
		if (data.getCashFlowCategory() != 0) {
			String cashFlow = getCashFlowCategory(data.getCashFlowCategory());
			cashFlowCatSelect.setValue(cashFlow);
		}

		opBalText.setAmount(data.getTotalBalance());
		if (!data.isOpeningBalanceEditable()) {
			opBalText.setDisabled(true);
		}
		// Enable Opening Balance to All Balancesheet accounts
		enableOpeningBalaceTxtByType();

		asofDate.setValue(new ClientFinanceDate(
				data.getAsOf() == 0 ? new ClientFinanceDate().getDate() : data
						.getAsOf()));
		asofDate.setDisabled(true);
		cashAccountCheck.setValue(data.isConsiderAsCashAccount());
		commentsArea.setValue(data.getComment());
		if (accountType == ClientAccount.TYPE_BANK) {

			if (((ClientBankAccount) data).getBankAccountType() != 0) {
				String type = getBankAccountType(((ClientBankAccount) data)
						.getBankAccountType());
				typeSelect.setComboItem(type);
				bankAccNumText.setValue(((ClientBankAccount) data)
						.getBankAccountNumber());
			}

		} else if (accountType == ClientAccount.TYPE_CREDIT_CARD) {
			setCreditLimit(!DecimalUtil.isEquals(data.getCreditLimit(), 0) ? data
					.getCreditLimit() : 0D);
			limitText.setValue(DataUtils.getAmountAsString(getCreditLimit()));
			cardNumText.setValue(data.getCardOrLoanNumber() != null ? data
					.getCardOrLoanNumber() : "");
		} else {
			selectedSubAccount = getCompany().getAccount(data.getParent());

			if (selectedSubAccount != null) {
				subAccSelect.setComboItem(selectedSubAccount);
				subhierarchy = Utility.getHierarchy(selectedSubAccount);
			}
			if (data.getHierarchy() != null) {
				hierText.setValue(data.getHierarchy());
			}

		}

	}

	private void enableOpeningBalaceTxtByType() {
		boolean isBalanceSheetTye = Arrays.asList(
				ClientAccount.TYPE_OTHER_CURRENT_ASSET,
				ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
				ClientAccount.TYPE_FIXED_ASSET,
				ClientAccount.TYPE_LONG_TERM_LIABILITY,
				ClientAccount.TYPE_EQUITY).contains(data.getType());

		long number = Long.parseLong(data.getNumber());
		// Checking whether type is under others accounts category.
		isBalanceSheetTye = !isBalanceSheetTye ? number >= 9501
				&& number <= 9600 : isBalanceSheetTye;

		if (isBalanceSheetTye
				&& !DecimalUtil.isEquals(data.getTotalBalance(), 0.0)) {
			opBalText.setDisabled(false);
		}

	}

	private String getCashFlowCategory(int i) {
		switch (i) {
		case ClientAccount.CASH_FLOW_CATEGORY_FINANCING:
			return AccounterClientConstants.CASH_FLOW_CATEGORY_FINANCING;

		case ClientAccount.CASH_FLOW_CATEGORY_INVESTING:
			return AccounterClientConstants.CASH_FLOW_CATEGORY_INVESTING;

		case ClientAccount.CASH_FLOW_CATEGORY_OPERATING:
			return AccounterClientConstants.CASH_FLOW_CATEGORY_OPERATING;
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

	public boolean isNewBankAccount() {
		return isNewBankAccount;
	}

	public void setNewBankAccount(boolean isNewBankAccount) {

		this.isNewBankAccount = isNewBankAccount;
		setAccountType(ClientAccount.TYPE_BANK);

	}

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

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.accNoText.setFocus();
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
				if (number.toString().equals(account.getNumber())) {
					addError(accNoText, Accounter.messages()
							.alreadyAccountExist(Global.get().account()));
					return false;
				}
			}
		}
		if (isNewBankAccount()) {
			if (number < 1100 || number > 1179) {
				addError(
						accNoText,
						Accounter
								.messages()
								.theAccountNumberchosenisincorrectPleasechooseaNumberbetween1100and1179(
										Global.get().account()));
				return false;
			} else {
				clearError(accNoText);
			}
		} else {
			accountSubBaseType = UIUtils.getAccountSubBaseType(accountType);

			nominalCodeRange = getCompany().getNominalCodeRange(
					accountSubBaseType);

			if (nominalCodeRange == null
					&& accountSubBaseType == ClientAccount.SUBBASETYPE_OTHER_ASSET) {
				return true;
			}

			if (number < nominalCodeRange[0] || number > nominalCodeRange[1]) {
				addError(
						accNoText,
						Accounter
								.messages()
								.theAccountNumberchosenisincorrectPleaschooseaNumberbetween(
										Global.get().account())
								+ "  "
								+ nominalCodeRange[0]
								+ Accounter.constants().and()
								+ nominalCodeRange[1]);
				return false;
			} else {
				clearError(accNoText);
			}
		}

		accNoText.setValue(String.valueOf(number));

		return true;

	}

	private String getBankAccountType(int type) {
		switch (type) {
		case ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING:
			return AccounterClientConstants.BANK_ACCCOUNT_TYPE_CHECKING;

		case ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET:
			return AccounterClientConstants.BANK_ACCCOUNT_TYPE_MONEY_MARKET;
		case ClientAccount.BANK_ACCCOUNT_TYPE_SAVING:
			return AccounterClientConstants.BANK_ACCCOUNT_TYPE_SAVING;

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

	@Override
	public void processupdateView(IAccounterCore core, int command) {

		switch (command) {
		case AccounterCommand.CREATION_SUCCESS:

			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.subAccSelect.addComboItem((ClientAccount) core);

			break;
		case AccounterCommand.DELETION_SUCCESS:
			if (core.getObjectType() == AccounterCoreType.ACCOUNT)
				this.subAccSelect.removeComboItem((ClientAccount) core);

		case AccounterCommand.UPDATION_SUCCESS:
			break;
		}
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
	}

	@Override
	public void onEdit() {

		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
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
		accNoText.setDisabled(isInViewMode());
		accNameText.setDisabled(isInViewMode());
		cashAccountCheck.setDisabled(isInViewMode());
		statusBox.setDisabled(isInViewMode());
		commentsArea.setDisabled(isInViewMode());
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
		return Accounter.messages().account(Global.get().account());
	}
}
