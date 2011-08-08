//package com.vimukti.accounter.web.client.ui.company;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.rpc.AccounterAsyncCallback;
//import com.google.gwt.user.client.rpc.IsSerializable;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.VerticalPanel;
//import com.vimukti.accounter.web.client.core.ClientAccount;
//import com.vimukti.accounter.web.client.core.ClientCompany;
//import com.vimukti.accounter.web.client.ui.AbstractBaseView;
//import com.vimukti.accounter.web.client.ui.AccountDataSource;
//import com.vimukti.accounter.web.client.ui.FinanceApplication;
//import com.vimukti.accounter.web.client.ui.UIUtils;
//import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
//import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
//import com.vimukti.accounter.web.client.ui.core.AmountField;
//import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
//import com.vimukti.accounter.web.client.ui.forms.DateItem;
//import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
//import com.vimukti.accounter.web.client.ui.forms.SelectItem;
//import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
//import com.vimukti.accounter.web.client.ui.forms.TextItem;
//
//public class AccountView  extends AbstractBaseView<ClientAccount> {
//
//	private TextItem accNoText, accNameText, hierText;
//	private AmountField opBalText;
//
//	private CheckboxItem statusBox;
//
//	AccountCombo subAccSelect;
//
//	private SelectItem cashFlowCatSelect, catSelect;
//
//	TextItem accTypeSelect;
//
//	private DateItem asofDate;
//
//	private DynamicForm accInfoForm, form2, form3;
//
//	private CheckboxItem chkbx1;
//
//	private TextAreaItem commentsArea;
//
//	private Button saveCloseButt;
//	private Button saveNewButt;
//
//	/**
//	 * This variable is to represent the type of Account need to create
//	 */
//	private int accountType;
//
//	private ClientCompany company;
//
//	private List<ClientAccount> accountsList;
//
//	private LinkedHashMap<String, String> subAccountsof;
//	private LinkedHashMap<String, String> cashFlowof;
//	/**
//	 * This variable is for to find which button clicks either save&close or
//	 * save&new button.
//	 */
//	protected boolean isClose;
//
//	private String accTypeName;
//	private Map<String, ClientAccount> parentsAccounts = new HashMap<String, ClientAccount>();
//
//	private ClientAccount newAcc;
//
//	CompanyMessages companyConstants = GWT.create(CompanyMessages.class);
//
//	public AccountView(int type, String typeName) {
//
//		this.accountType = type;
//		this.accTypeName = typeName;
//		this.company = FinanceApplication.getCompany();
//		createControls();
//	}
//
//	public AccountView() {
//		newAcc = new ClientAccount();
//
//		createControls();
//		getAccounts();
//
//	}
//
//	private void getAccounts() {
//
//		new AccountDataSource(new AccounterAsyncCallback<ArrayList<ClientAccount>>() {
//
//			public void onException(AccounterException caught) {
//				// //UIUtils.log(caught.toString());
//			}
//
//			public void onSuccess(ArrayList<ClientAccount> result) {
//				// //UIUtils.log(result.toString());
//				accountsList = result;
//				subAccSelect.initCombo(result);
//				subAccSelect.setDisabled(false);
//				// createControls();
//			}
//		});
//
//	}
//
//	private void createControls() {
//		setTitle(UIUtils.title(companyConstants.createNewAccount()));
//
//		accTypeSelect = new TextItem(companyConstants.accountType());
//		accTypeSelect.setValue(accTypeName);
//		accTypeSelect.setDisabled(true);
//
//		accNoText = new TextItem(companyConstants.accountNo());
//
//		accNameText = new TextItem(companyConstants.accountName());
//		accNameText.setRequired(true);
//
//		statusBox = new CheckboxItem(companyConstants.active());
//
//		// subAccSelect = new SelectItem("Subaccount of");
//		subAccSelect = new AccountCombo(companyConstants.subAccountOf());
//		subAccSelect
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
//					@Override
//					public void selectedComboBoxItem(ClientAccount selectItem) {
//						newAcc.setParent(( selectItem).getID());
//
//					}
//				});
//		subAccSelect.setDisabled(true);
//
//		subAccountsof = new LinkedHashMap<String, String>();
//
//		hierText = new TextItem(companyConstants.hierarchy());
//
//		cashFlowCatSelect = new SelectItem(companyConstants.cashFlowCategory());
//		cashFlowof = new LinkedHashMap<String, String>();
//
//		cashFlowof.put("1", "Financing");
//		cashFlowof.put("2", "Investing");
//		cashFlowof.put("3", "Operating");
//
//		cashFlowCatSelect.setValueMap(cashFlowof);
//
//		opBalText = new AmountField(companyConstants.openingBalance());
//		asofDate = UIUtils.date("As of");
//
//		catSelect = new SelectItem(companyConstants.category());
//
//		accInfoForm = UIUtils.form("Chart of accounts information");
//		accInfoForm.setFields(accTypeSelect, accNoText, accNameText, statusBox,
//				subAccSelect, hierText, cashFlowCatSelect, opBalText, asofDate,
//				catSelect);
//
//		chkbx1 = new CheckboxItem(companyConstants
//				.thisisConsideredaCashAccount());
//		form2 = new DynamicForm();
//		form2.setIsGroup(true);
//		form2.setGroupTitle(companyConstants.cashBasisAccounting());
//		form2.setFields(chkbx1);
//
//		commentsArea = new TextAreaItem();
//		commentsArea.setShowTitle(false);
//		// commentsArea.setWidth("*");// 100%");
//
//		form3 = UIUtils.form(companyConstants.comments());
//		form3.setWidth("100%");
//		form3.setFields(commentsArea);
//		saveCloseButt = new Button(companyConstants.saveandClose());
//		// saveCloseButt.setAutoFit(true);
//		// saveCloseButt.setLayoutAlign(Alignment.LEFT);
//		saveCloseButt.addClickHandler(new ClickHandler() {
//
//			public void onClick(ClickEvent event) {
//				if (validateFieldsValues()) {
//					isClose = true;
//					createAccount();
//				}
//			}
//		});
//		saveNewButt = new Button(companyConstants.saveandNew());
//		// saveNewButt.setAutoFit(true);
//		// saveNewButt.setLayoutAlign(Alignment.RIGHT);
//		saveNewButt.addClickHandler(new ClickHandler() {
//
//			public void onClick(ClickEvent event) {
//				if (validateFieldsValues()) {
//					isClose = false;
//					createAccount();
//				}
//			}
//		});
//
//		HorizontalPanel buttHLay = new HorizontalPanel();
//		// buttHLay.setAlign(Alignment.RIGHT);
//		// buttHLay.setMembersMargin(20);
//		// buttHLay.setMargin(10);
//		// buttHLay.setAutoHeight();
//		buttHLay.add(saveCloseButt);
//		buttHLay.add(saveNewButt);
//
//		VerticalPanel mainVLay = new VerticalPanel();
//		mainVLay.add(accInfoForm);
//		mainVLay.add(form2);
//		mainVLay.add(form3);
//		mainVLay.add(buttHLay);
//
//		add(mainVLay);
//		setSize("700", "540");
//
//	}
//
//	protected void createAccount() {
//
//		AccounterAsyncCallback<IsSerializable> callback = new AccounterAsyncCallback<IsSerializable>() {
//
//			public void onException(AccounterException caught) {
//				// //UIUtils.log("New Account: Failed!" + "\n" +
//				// caught.toString());
//			}
//
//			public void onSuccess(IsSerializable result) {
//				// //UIUtils.log("New Account: created successfully.");
//				if (isClose) {
//					// destroy();
//				} else {
//					clearFields();
//				}
//			}
//		};
//
//		FinanceApplication.createCRUDService().createAccount(newAcc, callback);
//	}
//
////	private LinkedHashMap<String, String> getAccountTypes() {
////		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
////
////		map.put(ClientAccount.TYPE_ACCOUNT_PAYABLE + "", "Account Payable");
////		map.put(ClientAccount.TYPE_ACCOUNT_RECEIVABLE + "",
////				"Account Receivable");
////		map.put(ClientAccount.TYPE_BANK + "", "Bank Account");
////		map.put(ClientAccount.TYPE_CASH + "", "Cash Account");
////		map.put(ClientAccount.TYPE_COST_OF_GOODS_SOLD + "",
////				"Cost of goods sold");
////		map.put(ClientAccount.TYPE_CREDIT_CARD + "", "Credit Card");
////		map.put(ClientAccount.TYPE_EQUITY + "", "Equity");
////		map.put(ClientAccount.TYPE_EXPENSE + "", "Expense");
////		map.put(ClientAccount.TYPE_OTHER_EXPENSE + "", "Other Expense");
////		map.put(ClientAccount.TYPE_FIXED_ASSET + "", "Fixed Assets");
////		map.put(ClientAccount.TYPE_INVENTORY_ASSET + "", "Inventory Assets");
////		map.put(ClientAccount.TYPE_OTHER_ASSET + "", "Other Assets");
////		map.put(ClientAccount.TYPE_OTHER_CURRENT_ASSET + "",
////				"Other current Assets");
////		map.put(ClientAccount.TYPE_INCOME + "", "Income");
////		map.put(ClientAccount.TYPE_OTHER_INCOME + "", "Other Income");
////
////		map.put(ClientAccount.TYPE_LONG_TERM_LIABILITY + "",
////				"Long term liability");
////		map.put(ClientAccount.TYPE_PAYROLL_LIABILITY + "", "Payroll liability");
////
////		return map;
////	}
//
//	private boolean validateFieldsValues() {
//		if (accInfoForm.validate() && form2.validate() && form3.validate()) {
//			newAcc.setBankAccountType(accountType);
//			newAcc
//					.setBankAccountNumber(accNoText.getValue() != null ? accNoText
//							.getValue().toString()
//							: "");
//			newAcc.setName(accNameText.getValue() != null ? accNameText
//					.getValue().toString() : "");
//			newAcc
//					.setIsActive(statusBox.getValue() != null ? (Boolean) statusBox
//							.getValue()
//							: false);
//
//			newAcc
//					.setCashFlowCategory(cashFlowCatSelect.getValue() != null ? Integer
//							.parseInt(cashFlowCatSelect.getValue().toString())
//							: 0);
//			newAcc.setOpeningBalance(opBalText.getValue() != null ? Double
//					.parseDouble(opBalText.getValue().toString()) : 0.0);
//			newAcc.setAsOf(asofDate.getValue() != null ? ((Date) asofDate
//					.getValue()).getTime() : new Date().getTime());
//			newAcc
//					.setConsiderAsCashAccount(chkbx1.getValue() != null ? (Boolean) chkbx1
//							.getValue()
//							: false);
//			newAcc.setComment(commentsArea.getValue() != null ? commentsArea
//					.getValue().toString() : null);
//
//			newAcc.setType(accountType);
//			return true;
//		}
//		return !true;
//	}
//
//	private void clearFields() {
//		// XXX
//		// accInfoForm.resetValues();
//		// form2.resetValues();
//		// form3.resetValues();
//
//	}
//
//	@Override
//	public void init() {
//
//	}
//
//	@Override
//	public void initData() {
//
//	}
//
// }
