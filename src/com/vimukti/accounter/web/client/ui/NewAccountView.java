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
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterConstants;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.combo.BankNameCombo;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.DropDownCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BankingActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.VendorsActionFactory;
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

	private int accountType;
	private ClientAccount takenAccount;
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
		subAccounts = FinanceApplication.getCompany().getAccounts(accountType);
		if (takenAccount != null) {
			for (ClientAccount account : subAccounts) {
				if (account.getStringID() == takenAccount.getStringID()) {
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
		this.allBanks = FinanceApplication.getCompany().getBanks();
		initBankNameSelect();
	}

	protected void initBankNameSelect() {

		bankNameSelect.initCombo(allBanks);

		if (takenAccount != null
				&& (selectedBank = FinanceApplication.getCompany().getBank(
						takenAccount.getBank())) != null) {
			bankNameSelect.setComboItem(selectedBank);
		}

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		// setTitle(UIUtils.title(FinanceApplication.getFinanceUIConstants()
		// .createNewAccount()));
		lab1 = new Label();
		lab1.removeStyleName("gwt-Label");
		lab1.addStyleName(FinanceApplication.getFinanceUIConstants()
				.lableTitle());
		// lab1.setHeight("35px");
		hierarchy = new String("");
		accTypeSelect = new SelectCombo(FinanceApplication
				.getFinanceUIConstants().accountType());
		accTypeSelect.setHelpInformation(true);
		// accTypeSelect.setWidth(100);
		accTypeSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						selectedId = (String) accTypeSelect.getSelectedValue();
						accounttype_selected();

					}
				});

		accNoText = new IntegerField(FinanceApplication.getFinanceUIConstants()
				.accountNo());
		accNoText.setHelpInformation(true);
		accNoText.setRequired(true);
		accNoText.setWidth(100);
		accNoText.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if (accNoText.getNumber() != null)
					validateAccountNumber(accNoText.getNumber());

			}
		});

		accNameText = new TextItem(FinanceApplication.getFinanceUIConstants()
				.accountName());
		accNameText.setHelpInformation(true);
		accNameText.setRequired(true);
		accNameText.setWidth(100);
		accNameText.addBlurHandler(new BlurHandler() {

			public void onBlur(BlurEvent event) {
				// Converts the first letter of Account Name to Upper case
				String name = accNameText.getValue().toString();
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
									.getHierarchy(selectedSubAccount)
									: "";
							hierarchy = hierarchy + temp;

						} else
							hierarchy = subhierarchy + temp;
					} else
						hierarchy = subhierarchy;
					hierText.setValue(hierarchy);
				}

			}

		});

		statusBox = new CheckboxItem(FinanceApplication.getFinanceUIConstants()
				.active());
		statusBox.setWidth(100);
		statusBox.setValue(true);

		cashFlowCatSelect = new SelectItem(FinanceApplication
				.getFinanceUIConstants().cashFlowcategory());
		cashFlowCatSelect.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub

			}
		});
		cashFlowof = new LinkedHashMap<String, String>();

		cashFlowof.put(ClientAccount.CASH_FLOW_CATEGORY_FINANCING + "",
				AccounterConstants.CASH_FLOW_CATEGORY_FINANCING);
		cashFlowof.put(ClientAccount.CASH_FLOW_CATEGORY_INVESTING + "",
				AccounterConstants.CASH_FLOW_CATEGORY_INVESTING);
		cashFlowof.put(ClientAccount.CASH_FLOW_CATEGORY_OPERATING + "",
				AccounterConstants.CASH_FLOW_CATEGORY_OPERATING);

		cashFlowCatSelect.setValueMap(cashFlowof);

		opBalText = new AmountField(FinanceApplication.getFinanceUIConstants()
				.openingBalance());
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
		// .getFinanceUIConstants()
		// .openingBalanceShouldNotBeNull());
		// } else {
		// balance = opBalText.getAmount();
		// if (DecimalUtil.isLessThan(balance, -1000000000000.00)
		// || DecimalUtil.isGreaterThan(balance,
		// 1000000000000.00)) {
		// Accounter.showError(FinanceApplication
		// .getFinanceUIConstants()
		// .balanceShouldBeInTheRange());
		// balance = 0D;
		// }
		// }
		//
		// } catch (Exception e) {
		// Accounter.showError(FinanceApplication
		// .getFinanceUIConstants().invalidOpeningBalance());
		// } finally {
		// opBalText.setAmount(balance);
		// setOpeningBalance(balance);
		// }
		//
		// }
		//
		// });
		asofDate = new DateField(FinanceApplication.getFinanceUIConstants()
				.asOf());
		asofDate.setHelpInformation(true);
		// asofDate.setWidth(100);
		asofDate
				.setEnteredDate(new ClientFinanceDate(
						FinanceApplication.getCompany().getPreferences()
								.getPreventPostingBeforeDate() == 0 ? new ClientFinanceDate()
								.getTime()
								: FinanceApplication.getCompany()
										.getPreferences()
										.getPreventPostingBeforeDate()));

		catSelect = new SelectItem(FinanceApplication.getFinanceUIConstants()
				.category1099());
		catSelect.setWidth(100);
		catSelect.setDisabled(true);

		accInfoForm = UIUtils.form(FinanceApplication.getFinanceUIConstants()
				.chartOfAccountsInformation());
		accInfoForm.setWidth("100%");

		topHLay = new HorizontalPanel();
		topHLay.setWidth("50%");
		leftLayout = new HorizontalPanel();
		leftLayout.setWidth("90%");

		if (accountType == 0 || accountType != ClientAccount.TYPE_BANK
				&& accountType != ClientAccount.TYPE_CREDIT_CARD) {
			subAccSelect = new OtherAccountsCombo(FinanceApplication
					.getAccounterComboConstants().subCategoryof());
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

			hierText = new TextItem(FinanceApplication.getFinanceUIConstants()
					.hierarchy());
			hierText.setHelpInformation(true);
			hierText.setDisabled(true);
			hierText.setWidth(100);
			// accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
			// statusBox, subAccSelect, hierText, cashFlowCatSelect,
			// opBalText, asofDate, catSelect);

			accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
					statusBox, opBalText, asofDate);
			leftLayout.add(accInfoForm);
			topHLay.add(leftLayout);

		} else {
			// accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
			// statusBox, cashFlowCatSelect, opBalText, asofDate,
			// catSelect);
			accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
					statusBox, opBalText, asofDate);
			leftLayout.add(accInfoForm);
			if (accountType == ClientAccount.TYPE_BANK)
				addBankForm();
			else {
				addCreditCardForm();
				topHLay.setWidth("100%");

			}

		}
		accInfoForm.getCellFormatter().setWidth(0, 0, "200");
		cashAccountCheck = new CheckboxItem(FinanceApplication
				.getFinanceUIConstants().thisIsConsideredACashAccount());
		cashAccountCheck.setWidth(100);

		cashBasisForm = new DynamicForm();
		cashBasisForm.setIsGroup(true);
		cashBasisForm.setGroupTitle(FinanceApplication.getFinanceUIConstants()
				.cashBasisAccounting());
		cashBasisForm.setFields(cashAccountCheck);
		cashBasisForm.setWidth("100%");
		cashBasisForm.getCellFormatter().getElement(0, 0).setAttribute(
				FinanceApplication.getVendorsMessages().width(), "200");

		commentsArea = new TextAreaItem();
		commentsArea.setHelpInformation(true);
		commentsArea.setTitle(FinanceApplication.getFinanceUIConstants()
				.comments());
		commentsArea.setWidth(100);
		// commentsArea.setShowTitle(false);

		commentsForm = UIUtils.form(FinanceApplication.getFinanceUIConstants()
				.comments());
		commentsForm.setWidth("50%");
		commentsForm.setFields(commentsArea);
		commentsForm.getCellFormatter().getElement(0, 0).getStyle()
				.setVerticalAlign(VerticalAlign.TOP);
		commentsForm.getCellFormatter().getElement(0, 0).setAttribute(
				FinanceApplication.getVendorsMessages().width(), "200");

		if (takenAccount != null) {
		}

		mainVLay = new VerticalPanel();

		mainVLay.setSize("100%", "300px");
		mainVLay.add(lab1);
		mainVLay.add(topHLay);
		mainVLay.add(cashBasisForm);
		mainVLay.add(commentsForm);
		// setHeightForCanvas("450");
		canvas.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(accInfoForm);
		listforms.add(cashBasisForm);

		listforms.add(commentsForm);

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
		if (accountType == ClientAccount.TYPE_CREDIT_CARD)
			validationCount = 5;
		else
			validationCount = 5;
		totalValidations = validationCount;
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
			accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
					statusBox, opBalText, asofDate);
			// leftLayout.add(accInfoForm);
			reset(accInfoForm);
			if (selectedId == null)
				accTypeSelect.setComboItem("Income");
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
			accInfoForm.setFields(accTypeSelect, accNoText, accNameText,
					statusBox, opBalText, asofDate);
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
					+ FinanceApplication.getAccounterComboConstants()
							.bankCategory());
		else
			lab1.setText(" "
					+ Utility.getAccountTypeString(this.accountType)
					+ " "
					+ FinanceApplication.getAccounterComboConstants()
							.financeCategory());

	}

	private DropDownCombo getBankNameSelectItem() {

		if (bankNameSelect == null) {

			bankNameSelect = new BankNameCombo(FinanceApplication
					.getFinanceUIConstants().bankName());
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

			limitText = new AmountField(FinanceApplication
					.getFinanceUIConstants().creditLimit());
			limitText.setHelpInformation(true);
			limitText.setWidth(100);
			limitText.setValue("" + UIUtils.getCurrencySymbol() + "0");
			limitText.addBlurHandler(new BlurHandler() {

				public void onBlur(BlurEvent event) {
					Double limit = 0D;
					try {
						if (limitText.getValue() == null) {
							Accounter.showError(FinanceApplication
									.getFinanceUIConstants()
									.creditLimitShouldNotBeNull());
						} else {
							limit = DataUtils.getAmountStringAsDouble(limitText
									.getValue().toString());
							if (DecimalUtil
									.isLessThan(limit, -1000000000000.00)
									|| DecimalUtil.isGreaterThan(limit,
											1000000000000.00)) {
								Accounter.showError(FinanceApplication
										.getFinanceUIConstants()
										.creditLimitShouldBeInTheRange());
								limit = 0D;
							}
						}
					} catch (Exception e) {
						Accounter.showError(FinanceApplication
								.getFinanceUIConstants().invalidCreditLimit());
					} finally {
						limitText.setValue(DataUtils.getAmountAsString(limit));
						setCreditLimit(limit);
					}
				}

			});
			cardNumText = new IntegerField(FinanceApplication
					.getFinanceUIConstants().cardOrLoadNumber());
			cardNumText.setHelpInformation(true);
			cardNumText.setWidth(100);
			creditCardForm = UIUtils.form(FinanceApplication
					.getFinanceUIConstants().creditCardAccountInformation());
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

			typeSelect = new SelectCombo(FinanceApplication
					.getFinanceUIConstants().bankAccountType());
			// typeSelect.setWidth(100);
			// typeSelect.setWidth("*");
			typeMap = new ArrayList<String>();

			typeMap.add(AccounterConstants.BANK_ACCCOUNT_TYPE_CHECKING);
			typeMap.add(AccounterConstants.BANK_ACCCOUNT_TYPE_MONEY_MARKET);
			typeMap.add(AccounterConstants.BANK_ACCCOUNT_TYPE_SAVING);
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

			bankAccNumText = new TextItem(FinanceApplication
					.getAccounterComboConstants().bankFinanceCategorynumber());
			bankAccNumText.setHelpInformation(true);
			bankAccNumText.setWidth(100);

			// accNameText.setWidth("*");

			bankForm = UIUtils.form(FinanceApplication.getFinanceUIConstants()
					.bankAccountInformation());
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

			if (takenAccount == null) {
				accTypeSelect.initCombo(getAccountsList());
				setAccountType(Integer.parseInt(defaultId));
				accounttype_selected();

			} else if (takenAccount != null) {

				accTypeSelect.setComboItem(Utility
						.getAccountTypeString(takenAccount.getType()));
				accTypeSelect.setDisabled(true);
				setAccountType(takenAccount.getType());
			}
			// FIXME
			// accTypeSelect.setDisabled(isEditable);

		}
		if (isNewBankAccount())
			lab1.setText(" "
					+ FinanceApplication.getAccounterComboConstants()
							.bankCategory());
		else

			lab1.setText(" "
					+ Utility.getAccountTypeString(accountType)
					+ " "
					+ FinanceApplication.getAccounterComboConstants()
							.financeCategory());

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
				if (FinanceApplication.getCompany().getAccountingType() != ClientCompany.ACCOUNTING_TYPE_UK)
					list.add(Utility.getAccountTypeString(type));
				else if (type != ClientAccount.TYPE_CASH
						&& type != ClientAccount.TYPE_OTHER_INCOME
						&& type != ClientAccount.TYPE_INVENTORY_ASSET
						&& type != ClientAccount.TYPE_CREDIT_CARD
						&& type != ClientAccount.TYPE_PAYROLL_LIABILITY) {
					if (!FinanceApplication.getUser().canDoBanking()
							&& type == ClientAccount.TYPE_BANK)
						continue;
					list.add(Utility.getAccountTypeString(type));
				}
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
				accountTypesMap.put(String.valueOf(type), Utility
						.getAccountTypeString(type));
			}
			defaultId = String.valueOf(accountTypes.get(0));

		} else {
			for (int type : UIUtils.accountTypes) {
				if (FinanceApplication.getCompany().getAccountingType() != ClientCompany.ACCOUNTING_TYPE_UK)
					accountTypesMap.put(String.valueOf(type), Utility
							.getAccountTypeString(type));
				else if (type != ClientAccount.TYPE_BANK
						&& type != ClientAccount.TYPE_CASH
						&& type != ClientAccount.TYPE_OTHER_INCOME
						&& type != ClientAccount.TYPE_INVENTORY_ASSET
						&& type != ClientAccount.TYPE_CREDIT_CARD
						&& type != ClientAccount.TYPE_PAYROLL_LIABILITY) {
					accountTypesMap.put(String.valueOf(type), Utility
							.getAccountTypeString(type));
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
	public void saveAndUpdateView() throws Exception {
		try {
			ClientAccount account = getAccountObject();

			if (takenAccount == null) {
				if (Utility.isNumberCorrect(account)) {
					throw new InvalidEntryException(
							AccounterErrorType.INVALIDNUMBER);
				} else if (Utility.isObjectExist(FinanceApplication
						.getCompany().getAccounts(), account.getName())) {
					throw new InvalidEntryException(
							AccounterErrorType.ALREADYEXIST);
				} else {
					createObject(account);
				}
			} else
				alterObject(account);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		// BaseView.errordata.setHTML(exception.getMessage());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		String exceptionMessage = exception.getMessage();
		MainFinanceWindow.getViewManager().showError(exceptionMessage);

		ClientAccount account = getAccountObject();
		if (exceptionMessage.contains("number"))
			account.setNumber(accountNo);
		if (exceptionMessage.contains("name"))
			account.setName(accountName);
		// if (takenAccount == null)
		// else
		// Accounter.showError(FinanceApplication.getFinanceUIConstants()
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

		if (this.yesClicked && accountType == ClientAccount.TYPE_CREDIT_CARD) {
			HistoryTokenUtils.setPresentToken(VendorsActionFactory
					.getNewVendorAction(), null);
			VendorsActionFactory.getNewVendorAction().run(null, false);
		}

		super.saveSuccess(result);

	}

	public void reload() {
		if (isNewBankAccount)
			BankingActionFactory.getNewBankAccountAction().run(null, true);
		else
			CompanyActionFactory.getNewAccountAction().run(null, true);
	}

	@Override
	public boolean validate() throws InvalidEntryException {

		// if (totalValidations == 2){
		// AccounterValidator.validateForm(accInfoForm);
		// totalValidations--;
		// }else {

		/*
		 * If the account type is CreditCard,then a special case need to be
		 * check using onCreditCardAccountSaved(),otherwise validate only
		 * account name
		 */
		if (accountType == ClientAccount.TYPE_CREDIT_CARD) {
			switch (validationCount) {
			case 5:
				String name = accNameText.getValue().toString() != null ? accNameText
						.getValue().toString()
						: "";
				if (takenAccount != null ? (takenAccount.getName()
						.equalsIgnoreCase(name) ? true : (Utility
						.isObjectExist(FinanceApplication.getCompany()
								.getAccounts(), name) ? false : true)) : true) {
					return true;
				} else
					throw new InvalidEntryException(
							AccounterErrorType.ALREADYEXIST);
			case 4:
				return AccounterValidator.validateForm(accInfoForm, false);
			case 3:
				if (takenAccount != null
						&& takenAccount.getName().equalsIgnoreCase(
								FinanceApplication.getAccounterComboConstants()
										.openingBalances()))
					return true;
				else
					return validateAccountNumber(accNoText.getNumber());
			case 2:
				return AccounterValidator.onCreditCardAccountSaved(this);
			case 1:
				ClientFinanceDate asOfDate = asofDate.getEnteredDate();
				return AccounterValidator.isPriorAsOfDate(asOfDate, this);
			default:
				break;
			}
		} else {
			switch (validationCount) {
			case 5:
				String name = accNameText.getValue().toString() != null ? accNameText
						.getValue().toString()
						: "";
				if (takenAccount != null ? (takenAccount.getName()
						.equalsIgnoreCase(name) ? true : (Utility
						.isObjectExist(FinanceApplication.getCompany()
								.getAccounts(), name) ? false : true)) : true) {
					return true;
				} else
					throw new InvalidEntryException(
							AccounterErrorType.ALREADYEXIST);
			case 4:
				return validateAccountNumber(accNoText.getNumber());
			case 3:
				return AccounterValidator.validateForm(accInfoForm, false);
			case 2:
				if (accountType == ClientAccount.TYPE_BANK)
					return AccounterValidator.validateForm(bankForm, false);
				return true;
			case 1:
				ClientFinanceDate asOfDate = asofDate.getEnteredDate();
				return AccounterValidator.isPriorAsOfDate(asOfDate, this);
			default:
				break;
			}
		}

		return validateAccountNumber(accNoText.getNumber());

		// if (!accInfoForm.validate())
		// throw new InvalidEntryException(
		// "Required Fields are shown in bold.Those Fields should be Filled");
		// // if(this.accountType!=selectedSubAccount.getType())
		// // throw new InvalidEntryException(
		// // "Parent and Child financial Account Types must Match..");
		// return true;
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

	protected boolean validateForm() {
		if (accountType != ClientAccount.TYPE_BANK
				&& accountType != ClientAccount.TYPE_CREDIT_CARD) {
			return accInfoForm.validate(false);

		} else
			return accInfoForm.validate(false) && commonForm.validate(false);
	}

	// protected SimplePanel getWidget() {
	// return this;
	// }

	private ClientAccount getAccountObject() {
		ClientAccount account;
		if (takenAccount != null)
			account = takenAccount;
		else
			account = new ClientAccount();
		account.setType(accountType);
		account.setNumber(accNoText.getNumber() != null ? accNoText.getNumber()
				.toString() : "");
		account.setName(accNameText.getValue().toString() != null ? accNameText
				.getValue().toString() : "");
		account.setIsActive(statusBox.getValue() != null ? (Boolean) statusBox
				.getValue() : Boolean.FALSE);
		if (cashAccountCheck != null)
			account.setConsiderAsCashAccount((Boolean) cashAccountCheck
					.getValue());

		if (cashFlowCatSelect.getValue() != null)
			account
					.setCashFlowCategory(cashFlowCatSelect.getSelectedIndex() + 1);
		// account.setCashFlowCategory(0);
		account.setOpeningBalance(opBalText.getAmount());
		account.setAsOf(asofDate.getEnteredDate().getTime());

		switch (accountType) {
		case ClientAccount.TYPE_BANK:
			account.setBank(Utility.getId(selectedBank));
			if (typeSelect.getSelectedValue() != null) {
				int type = 0;
				if (typeSelect.getSelectedValue().equals(
						AccounterConstants.BANK_ACCCOUNT_TYPE_CHECKING))
					type = ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING;
				else if (typeSelect.getSelectedValue().equals(
						AccounterConstants.BANK_ACCCOUNT_TYPE_MONEY_MARKET))
					type = ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET;
				else if (typeSelect.getSelectedValue().equals(
						AccounterConstants.BANK_ACCCOUNT_TYPE_SAVING))
					type = ClientAccount.BANK_ACCCOUNT_TYPE_SAVING;
				account.setBankAccountType(type);
			}
			account.setBankAccountNumber(bankAccNumText.getValue().toString());
			account.setIncrease(Boolean.FALSE);
			break;
		case ClientAccount.TYPE_CREDIT_CARD:
			account.setBank(Utility.getId(selectedBank));
			if (limitText.getValue() != null)
				account.setCreditLimit(getCreditLimit());
			if (cardNumText.getValue() != null)
				account.setCardOrLoanNumber(cardNumText.getValue().toString());
			break;
		default:
			if (selectedSubAccount != null)
				account.setParent(selectedSubAccount.getStringID());
			if (hierText != null)
				account.setHierarchy(UIUtils.toStr(hierText.getValue()));
			break;
		}
		account.setComment(commentsArea.getValue() != null ? commentsArea
				.getValue().toString() : "");
		if (account.getType() == ClientAccount.TYPE_INCOME
				|| account.getType() == ClientAccount.TYPE_OTHER_INCOME
				|| account.getType() == ClientAccount.TYPE_CREDIT_CARD
				|| account.getType() == ClientAccount.TYPE_PAYROLL_LIABILITY
				|| account.getType() == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
				|| account.getType() == ClientAccount.TYPE_LONG_TERM_LIABILITY
				|| account.getType() == ClientAccount.TYPE_EQUITY
				|| account.getType() == ClientAccount.TYPE_ACCOUNT_PAYABLE) {
			account.setIncrease(Boolean.TRUE);
		} else {
			account.setIncrease(Boolean.FALSE);
		}
		return account;
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
		if (takenAccount != null)
			initView();
		validationCount = totalValidations;
		super.initData();
		// if (takenAccount == null)
		// getNextAccountNumber();

	}

	private void initView() {

		accTypeSelect.setComboItem(Utility.getAccountTypeString(takenAccount
				.getType()));
		accNoText.setValue(takenAccount.getNumber() != null ? takenAccount
				.getNumber() : 0);
		accountNo = takenAccount.getNumber() != null ? takenAccount.getNumber()
				: "0";

		if (takenAccount.getName().equalsIgnoreCase("Opening Balances"))
			accNoText.setDisabled(true);

		accNameText.setValue(takenAccount.getName());
		accountName = takenAccount.getName();
		if (accountName.equalsIgnoreCase("Opening Balances")
				|| accountName.equalsIgnoreCase("Un Deposited Funds")
				|| accountName.equalsIgnoreCase("Accounts Receivable")
				|| accountName.equalsIgnoreCase("Accounts Payable"))
			accNameText.setDisabled(true);
		// statusBox.setValue(takenAccount.getIsActive() != null ? takenAccount
		// .getIsActive() : Boolean.FALSE);
		statusBox.setValue(takenAccount.getIsActive());
		if (takenAccount.getCashFlowCategory() != 0) {
			String cashFlow = getCashFlowCategory(takenAccount
					.getCashFlowCategory());
			cashFlowCatSelect.setValue(cashFlow);
		}

		opBalText.setAmount(takenAccount.getTotalBalance());
		if (!takenAccount.isOpeningBalanceEditable()) {
			opBalText.setDisabled(true);
		}
		// Enable Opening Balance to All Balancesheet accounts
		enableOpeningBalaceTxtByType();

		asofDate.setValue(new ClientFinanceDate(
				takenAccount.getAsOf() == 0 ? new ClientFinanceDate().getTime()
						: takenAccount.getAsOf()));
		asofDate.setDisabled(true);
		cashAccountCheck.setValue(takenAccount.isConsiderAsCashAccount());
		commentsArea.setValue(takenAccount.getComment());
		if (accountType == ClientAccount.TYPE_BANK) {

			if (takenAccount.getBankAccountType() != 0) {
				String type = getBankAccountType(takenAccount
						.getBankAccountType());
				typeSelect.setComboItem(type);
				bankAccNumText.setValue(takenAccount.getBankAccountNumber());
			}

		} else if (accountType == ClientAccount.TYPE_CREDIT_CARD) {
			setCreditLimit(!DecimalUtil.isEquals(takenAccount.getCreditLimit(),
					0) ? takenAccount.getCreditLimit() : 0D);
			limitText.setValue(DataUtils.getAmountAsString(getCreditLimit()));
			cardNumText
					.setValue(takenAccount.getCardOrLoanNumber() != null ? takenAccount
							.getCardOrLoanNumber()
							: "");
		} else {
			selectedSubAccount = FinanceApplication.getCompany().getAccount(
					takenAccount.getParent());

			if (selectedSubAccount != null) {
				subAccSelect.setComboItem(selectedSubAccount);
				subhierarchy = Utility.getHierarchy(selectedSubAccount);
			}
			if (takenAccount.getHierarchy() != null) {
				hierText.setValue(takenAccount.getHierarchy());
			}

		}

	}

	private void enableOpeningBalaceTxtByType() {
		boolean isBalanceSheetTye = Arrays.asList(
				ClientAccount.TYPE_OTHER_CURRENT_ASSET,
				ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
				ClientAccount.TYPE_FIXED_ASSET,
				ClientAccount.TYPE_LONG_TERM_LIABILITY,
				ClientAccount.TYPE_EQUITY).contains(takenAccount.getType());

		long number = Long.parseLong(takenAccount.getNumber());
		// Checking whether type is under others accounts category.
		isBalanceSheetTye = !isBalanceSheetTye ? number >= 9501
				&& number <= 9600 : isBalanceSheetTye;

		if (isBalanceSheetTye
				&& !DecimalUtil.isEquals(takenAccount.getTotalBalance(), 0.0)) {
			opBalText.setDisabled(false);
		}

	}

	private String getCashFlowCategory(int i) {
		switch (i) {
		case ClientAccount.CASH_FLOW_CATEGORY_FINANCING:
			return AccounterConstants.CASH_FLOW_CATEGORY_FINANCING;

		case ClientAccount.CASH_FLOW_CATEGORY_INVESTING:
			return AccounterConstants.CASH_FLOW_CATEGORY_INVESTING;

		case ClientAccount.CASH_FLOW_CATEGORY_OPERATING:
			return AccounterConstants.CASH_FLOW_CATEGORY_OPERATING;
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
			takenAccount = (ClientAccount) data;
			setAccountType(takenAccount.getType());
		} else
			takenAccount = null;
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
								FinanceApplication.getCompany()
										.getPreferences()
										.getPreventPostingBeforeDate() == 0 ? new ClientFinanceDate()
										.getTime()
										: FinanceApplication.getCompany()
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
	// new AsyncCallback<Long>() {
	//
	// @Override
	// public void onFailure(Throwable caught) {
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

	private boolean validateAccountNumber(Long number) {

		if (number == null)
			return true;

		List<ClientAccount> accounts = FinanceApplication.getCompany()
				.getAccounts();
		if (takenAccount == null) {
			for (ClientAccount account : accounts) {
				if (number.toString().equals(account.getNumber())) {
					// BaseView.errordata.setHTML("<li> "
					// + FinanceApplication.getCompanyMessages()
					// .alreadyAccountExist() + ".");
					// BaseView.commentPanel.setVisible(true);
					// AbstractBaseView.errorOccured = true;
					MainFinanceWindow.getViewManager().showError(
							FinanceApplication.getCompanyMessages()
									.alreadyAccountExist());
					// Accounter.showError(FinanceApplication.getCompanyMessages()
					// .alreadyAccountExist());
					return false;
				}
			}
		}
		if (isNewBankAccount()) {
			if (number < 1100 || number > 1179) {
				// BaseView.errordata
				// .setHTML("<li> The Account Number chosen is incorrect. Please choose a Number between 1100 and 1179.");
				// BaseView.commentPanel.setVisible(true);
				MainFinanceWindow
						.getViewManager()
						.showError(
								"The Account Number chosen is incorrect. Please choose a Number between 1100 and 1179");
				// Accounter
				// .showError("The Account Number chosen is incorrect. Please choose a Number between 1100 and 1179");
				// accNoText.setNumber(null);
				return false;
			} else {
				// BaseView.errordata.setHTML("");
				// BaseView.commentPanel.setVisible(false);
				MainFinanceWindow.getViewManager().restoreErrorBox();
			}
		} else {
			accountSubBaseType = UIUtils.getAccountSubBaseType(accountType);

			nominalCodeRange = FinanceApplication.getCompany()
					.getNominalCodeRange(accountSubBaseType);

			if (nominalCodeRange == null
					&& accountSubBaseType == ClientAccount.SUBBASETYPE_OTHER_ASSET) {
				return true;
			}

			if (number < nominalCodeRange[0] || number > nominalCodeRange[1]) {
				// BaseView.errordata
				// .setHTML("<li> The Account Number chosen is incorrect. Please choose a Number between"
				// + "  "
				// + nominalCodeRange[0]
				// + " and "
				// + nominalCodeRange[1] + ".");
				// BaseView.commentPanel.setVisible(true);
				MainFinanceWindow.getViewManager().showError(
						"The Account Number chosen is incorrect. Please choose a Number between"
								+ "  " + nominalCodeRange[0] + " and "
								+ nominalCodeRange[1]);
				// Accounter
				// .showError("The Account Number chosen is incorrect. Please choose a Number between"
				// + "  "
				// + nominalCodeRange[0]
				// + " and "
				// + nominalCodeRange[1]);
				// accNoText.setNumber(null);
				return false;
			} else {
				// BaseView.errordata.setHTML("");
				// BaseView.commentPanel.setVisible(false);
				MainFinanceWindow.getViewManager().restoreErrorBox();
			}
		}
		accNoText.setValue(number);

		return true;

	}

	private String getBankAccountType(int type) {
		switch (type) {
		case ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING:
			return AccounterConstants.BANK_ACCCOUNT_TYPE_CHECKING;

		case ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET:
			return AccounterConstants.BANK_ACCCOUNT_TYPE_MONEY_MARKET;
		case ClientAccount.BANK_ACCCOUNT_TYPE_SAVING:
			return AccounterConstants.BANK_ACCCOUNT_TYPE_SAVING;

		default:
			break;
		}

		return "";

	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

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
		setCashFlowType();
		resetView();
		statusBox.setValue(true);
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return FinanceApplication.getCompanyMessages().account();
	}
}
