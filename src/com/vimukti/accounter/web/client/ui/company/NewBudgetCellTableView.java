package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientBudgetItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.BudgetCellTable;

public class NewBudgetCellTableView extends BaseView<ClientBudget> {

	public static final String AUCTUAL_AMOUNT_LAST_FISCAL_YEAR = "Actual Amount from last fiscal year";
	public static final String AUCTUAL_AMOUNT_THIS_FISCAL_YEAR = "Actual Amount from this fiscal year";
	public static final String NO_AMOUNT = "Start from Scratch";
	public static final String COPY_FROM_EXISTING = "Copy from Existing Budget";

	public static final String SUBDIVIDE_DONT = "Dont Sub-devide";
	public static final String SUBDIVIDE_BUSINESS = "Business";
	public static final String SUBDIVIDE_CLASS = "Class";
	public static final String SUBDIVIDE_CUSTOMER = "Customer";

	public static final String FISCAL_YEAR_1 = "FY2010 (Jan2010 - Dec2010)";
	public static final String FISCAL_YEAR_2 = "FY2011 (Jan2011 - Dec2011)";
	public static final String FISCAL_YEAR_3 = "FY2012 (Jan2012 - Dec2012)";
	public static final String FISCAL_YEAR_4 = "FY2013 (Jan2013 - Dec2013)";
	public static final String FISCAL_YEAR_5 = "FY2014 (Jan2014 - Dec2014)";
	public static final String FISCAL_YEAR_6 = "FY2015 (Jan2015 - Dec2015)";
	public static final String FISCAL_YEAR_7 = "FY2016 (Jan2016 - Dec2016)";
	public static final String FISCAL_YEAR_8 = "FY2017 (Jan2017 - Dec2017)";
	public static final String FISCAL_YEAR_9 = "FY2018 (Jan2018 - Dec2018)";
	public static final String FISCAL_YEAR_10 = "FY2018 (Jan2018 - Dec2019)";
	public static final String FISCAL_YEAR_11 = "FY2018 (Jan2018 - Dec2020)";
	public static final String FISCAL_YEAR_12 = "FY2018 (Jan2018 - Dec2021)";
	public static final String FISCAL_YEAR_13 = "FY2018 (Jan2018 - Dec2022)";
	public static final String FISCAL_YEAR_14 = "FY2018 (Jan2018 - Dec2023)";
	public static final String FISCAL_YEAR_15 = "FY2018 (Jan2018 - Dec2024)";

	private SelectCombo budgetStartWithSelect, budgetSubdevideBy;
	private SelectCombo selectFinancialYear;
	private TextItem budgetNameText;
	private DynamicForm budgetInfoForm;
	private HorizontalPanel topHLay;
	private HorizontalPanel leftLayout;
	private Label lab1;

	VerticalPanel mainVLay;

	private ArrayList<DynamicForm> listforms;

	List<ClientBudget> budgetList;

	ClientBudget budgetForEditing = new ClientBudget();

	private BudgetCellTable budgetCellTable;
	private boolean isEditing;

	public NewBudgetCellTableView(List<ClientBudget> listData) {
		budgetList = listData;
	}

	public NewBudgetCellTableView() {

	}

	public NewBudgetCellTableView(boolean isEdit, Object data1) {
		isEditing = isEdit;
		data = (ClientBudget) data1;
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");

		if (data != null) {
			onEdit();
		}

	}

	public void initData() {
		super.initData();
		if (data == null) {
			ClientBudget account = new ClientBudget();
			setData(account);
		}

	}

	private void initView() {

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		lab1 = new Label();
		lab1.removeStyleName("gwt-Label");
		lab1.addStyleName(Accounter.messages().labelTitle());
		lab1.setText(Accounter.messages().newBudget());

		budgetStartWithSelect = new SelectCombo(Global.get().messages()
				.budgetStartWith());
		budgetStartWithSelect.setHelpInformation(true);
		budgetStartWithSelect.initCombo(getStartWithList());
		budgetStartWithSelect.setSelectedItem(0);
		budgetStartWithSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@SuppressWarnings("unchecked")
					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem == COPY_FROM_EXISTING) {
							String budgetTitle = "Copy values from Existing Budget";
							CopyBudgetDialogue copybudgetDialogue = new CopyBudgetDialogue(
									budgetTitle, "", budgetList);
							copybudgetDialogue
									.setCallback(new ActionCallback<String>() {

										@Override
										public void actionResult(String result) {
											refreshView(result);

										}

									});
							copybudgetDialogue.show();
						}
					}
				});

		budgetSubdevideBy = new SelectCombo(Global.get().messages()
				.budgetSubdivide());
		budgetSubdevideBy.setHelpInformation(true);
		budgetSubdevideBy.initCombo(getSubdevideList());
		budgetSubdevideBy
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		selectFinancialYear = new SelectCombo(Global.get().messages()
				.budgetFinancialYear());
		selectFinancialYear.setHelpInformation(true);
		selectFinancialYear.initCombo(getFiscalYearList());
		selectFinancialYear.setSelected(FISCAL_YEAR_3);
		selectFinancialYear.setRequired(true);
		selectFinancialYear
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		budgetNameText = new TextItem(Accounter.messages().payeeName(
				Accounter.messages().budget()));
		budgetNameText.setToolTip(Accounter.messages()
				.giveTheNameAccordingToYourID(this.getAction().getViewName()));
		budgetNameText.setHelpInformation(true);
		budgetNameText.setRequired(true);
		budgetNameText.setWidth(100);
		budgetNameText.addBlurHandler(new BlurHandler() {

			public void onBlur(BlurEvent event) {

				// Converts the first letter of Account Name to Upper case
				String name = budgetNameText.getValue().toString();
				if (name.isEmpty()) {
					return;
				}
				String lower = name.substring(0, 1);
				String upper = lower.toUpperCase();
				budgetNameText.setValue(name.replaceFirst(lower, upper));

			}
		});

		if (data != null) {
			onEditChangeControls();
		}

		budgetInfoForm = UIUtils.form(Accounter.messages()
				.chartOfAccountsInformation());
		budgetInfoForm.setWidth("100%");

		topHLay = new HorizontalPanel();
		topHLay.setWidth("50%");
		leftLayout = new HorizontalPanel();
		leftLayout.setWidth("100%");

		budgetInfoForm.setFields(budgetStartWithSelect, selectFinancialYear,
				budgetNameText);

		leftLayout.add(budgetInfoForm);
		topHLay.add(leftLayout);

		budgetCellTable = new BudgetCellTable();
		if (data != null) {
			budgetCellTable.setDataProvided(data);
		}

		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(TextLocation.CENTER,
				pagerResources, false, 0, true);
		pager.setDisplay(budgetCellTable);
		budgetCellTable.addColumnSortHandler(new Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent event) {
				Column<?, ?> column = event.getColumn();
				int columnIndex = budgetCellTable
						.getColumnIndex((Column<ClientBudgetItem, ?>) column);
				boolean isAscending = event.isSortAscending();
				budgetCellTable.sortRowData(columnIndex, isAscending);

			}
		});

		mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "300px");
		mainVLay.add(lab1);
		mainVLay.add(topHLay);
		mainVLay.add(budgetCellTable);
		budgetCellTable.setStyleName("user_activity_log");
		this.add(mainVLay);

		listforms.add(budgetInfoForm);

	}

	private void onEditChangeControls() {

		budgetStartWithSelect.setDisabled(true);
		budgetNameText.setDisabled(true);
		selectFinancialYear.setDisabled(true);
		budgetNameText.setValue(data.getBudgetName());
		selectFinancialYear.setSelected(data.getFinancialYear());
	}

	private List<String> getFiscalYearList() {
		List<String> list = new ArrayList<String>();

		list.add(FISCAL_YEAR_1);
		list.add(FISCAL_YEAR_2);
		list.add(FISCAL_YEAR_3);
		list.add(FISCAL_YEAR_4);
		list.add(FISCAL_YEAR_5);
		list.add(FISCAL_YEAR_6);
		list.add(FISCAL_YEAR_7);
		list.add(FISCAL_YEAR_8);
		list.add(FISCAL_YEAR_9);
		list.add(FISCAL_YEAR_10);
		list.add(FISCAL_YEAR_11);
		list.add(FISCAL_YEAR_12);
		list.add(FISCAL_YEAR_13);
		list.add(FISCAL_YEAR_14);
		list.add(FISCAL_YEAR_15);

		return list;
	}

	private List<String> getSubdevideList() {
		List<String> list = new ArrayList<String>();

		list.add(SUBDIVIDE_DONT);
		list.add(SUBDIVIDE_BUSINESS);
		list.add(SUBDIVIDE_CLASS);
		list.add(SUBDIVIDE_CUSTOMER);

		return list;
	}

	private List<String> getStartWithList() {
		List<String> list = new ArrayList<String>();

		list.add(NO_AMOUNT);
		list.add(AUCTUAL_AMOUNT_LAST_FISCAL_YEAR);
		list.add(AUCTUAL_AMOUNT_THIS_FISCAL_YEAR);
		list.add(COPY_FROM_EXISTING);

		return list;
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
		return Global.get().messages().budget();

	}

	public List<DynamicForm> getForms() {
		return listforms;
	}

	@Override
	public ClientBudget saveView() {
		ClientBudget saveView = super.saveView();
		if (data != null) {
			updateBudgetObject();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {
		updateBudgetObject();
		saveOrUpdate(getData());

	}

	private void updateBudgetObject() {

		String budgetname = budgetNameText.getValue() != null ? budgetNameText
				.getValue() : " ";
		data.setBudgetName(budgetname);

		data.setFinancialYear(selectFinancialYear.getSelectedValue());

		List<ClientBudgetItem> allGivenRecords = budgetCellTable.getDataList();
		List<ClientBudgetItem> newList = new ArrayList<ClientBudgetItem>();

		for (ClientBudgetItem clientBudgetItem : allGivenRecords) {

			if (clientBudgetItem.getTotalAmount() > 0) {
				newList.add(clientBudgetItem);
			}
		}

		data.setBudgetItem(newList);

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);

		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

		updateBudgetObject();

	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			super.saveSuccess(result);

		} else {
			saveFailed(new AccounterException());
		}
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		result.add(budgetInfoForm.validate());
		if (data == null) {
			String name = budgetNameText.getValue().toString() != null ? budgetNameText
					.getValue().toString() : "";

			String financialYear = selectFinancialYear.getSelectedValue();

			String[] temp;
			String delimiter = " ";
			temp = financialYear.split(delimiter);

			String budgetName = name + " - " + temp[0];

			if (name != null && !name.isEmpty()) {

				for (ClientBudget budget : budgetList) {
					if (budgetName.equals(budget.getBudgetName())) {
						result.addError(name, Accounter.messages()
								.alreadyExist());
						break;
					}
				}
			}
		}

		List<ClientBudgetItem> allGivenRecords = budgetCellTable.getDataList();
		List<ClientBudgetItem> newList = new ArrayList<ClientBudgetItem>();
		boolean errorAdded = false;

		for (ClientBudgetItem clientBudgetItem : allGivenRecords) {
			if (clientBudgetItem.getTotalAmount() > 0) {
				errorAdded = true;
				break;
			} else {
				errorAdded = false;
			}
		}
		if (errorAdded == false) {
			result.addError(allGivenRecords, Accounter.messages()
					.noAmountAddedtoAccounts());

		}

		return result;

	}

	private void refreshView(String result) {

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

		this.rpcDoSerivce.canEdit(AccounterCoreType.BUDGET, data.getID(),
				editCallBack);
	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		budgetStartWithSelect.setDisabled(false);
		budgetNameText.setDisabled(false);
		selectFinancialYear.setDisabled(false);

		super.onEdit();

	}

	@Override
	public void setFocus() {
		this.budgetStartWithSelect.setFocus();

	}

}
