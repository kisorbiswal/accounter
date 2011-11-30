package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
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

public class NewBudgetView1 extends BaseView<ClientBudget> {

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

	private SelectCombo budgetStartWithSelect, budgetSubdevideBy;
	private SelectCombo selectFinancialYear;
	private TextItem budgetNameText;
	private DynamicForm budgetInfoForm;
	private HorizontalPanel topHLay;
	private HorizontalPanel leftLayout;
	private Label lab1;
	private final List<ClientAccount> listOfAccounts = getCompany()
			.getAccounts();
	// boolean isEditing;

	VerticalPanel mainVLay;

	// BudgetAccountGrid gridView;

	private ArrayList<DynamicForm> listforms;

	List<ClientBudget> budgetList;

	ClientBudget budgetForEditing = new ClientBudget();
	private CellTable<ClientBudgetItem> cellTable;
	private SingleSelectionModel<ClientBudgetItem> selectionModel;

	public NewBudgetView1(List<ClientBudget> listData) {
		budgetList = listData;
	}

	// public NewBudgetView(boolean isEdit, Object data1) {
	// isEditing = isEdit;
	// data = (ClientBudget) data1;
	// }

	public NewBudgetView1() {

	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");

	}

	public void initData() {
		super.initData();
		if (data == null) {
			ClientBudget account = new ClientBudget();
			setData(account);
		}
		// if (data != null) {
		// onEdit();
		// }
	}

	private void initView() {

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		lab1 = new Label();
		lab1.removeStyleName("gwt-Label");
		lab1.addStyleName(Accounter.messages().labelTitle());
		lab1.setText(Accounter.messages().newBudget());

		// hierarchy = new String("");

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

		// budgetInfoForm.setFields(budgetStartWithSelect, budgetSubdevideBy,
		// selectFinancialYear, budgetNameText);

		budgetInfoForm.setFields(budgetStartWithSelect, selectFinancialYear,
				budgetNameText);

		leftLayout.add(budgetInfoForm);
		topHLay.add(leftLayout);

		// budgetInfoForm.getCellFormatter().setWidth(0, 0, "200");

		/*
		 * gridView = new BudgetAccountGrid(); gridView.setCanEdit(true);
		 * gridView.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
		 * gridView.isEnable = false; if (data != null) {
		 * gridView.setDisabled(true); }
		 * 
		 * gridView.init();
		 * 
		 * if (data != null) { List<ClientBudgetItem> itemsList = new
		 * ArrayList<ClientBudgetItem>(); itemsList = data.getBudgetItem(); for
		 * (ClientBudgetItem item : itemsList) { ClientBudgetItem obj = new
		 * ClientBudgetItem(); obj.setAccountsName(item.getAccount().getName());
		 * obj = item; gridView.addData(obj); } } else {
		 * 
		 * listOfAccounts = getCompany().getAccounts(); for (ClientAccount
		 * account : listOfAccounts) { ClientBudgetItem obj = new
		 * ClientBudgetItem(); gridView.addData(obj, account); } }
		 */

		mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "300px");
		mainVLay.add(lab1);
		mainVLay.add(topHLay);
		mainVLay.add(getAccountsAmountGrid());

		this.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(budgetInfoForm);

	}

	private void onEditChangeControls() {

		budgetStartWithSelect.setDisabled(true);
		budgetNameText.setDisabled(true);
		selectFinancialYear.setDisabled(true);

		String budgetname = data.getBudgetName();

		String[] temp;
		String delimiter = " ";
		temp = budgetname.split(delimiter);

		budgetNameText.setValue(temp[0]);

		if (temp[2].equals("FY2010")) {
			selectFinancialYear.setSelected(FISCAL_YEAR_1);
		} else if (temp[2].equals("FY2011")) {
			selectFinancialYear.setSelected(FISCAL_YEAR_2);
		} else if (temp[2].equals("FY2012")) {
			selectFinancialYear.setSelected(FISCAL_YEAR_3);
		} else if (temp[2].equals("FY2013")) {
			selectFinancialYear.setSelected(FISCAL_YEAR_4);
		} else if (temp[2].equals("FY2014")) {
			selectFinancialYear.setSelected(FISCAL_YEAR_5);
		} else if (temp[2].equals("FY2015")) {
			selectFinancialYear.setSelected(FISCAL_YEAR_6);
		} else if (temp[2].equals("FY2016")) {
			selectFinancialYear.setSelected(FISCAL_YEAR_7);
		} else if (temp[2].equals("FY2017")) {
			selectFinancialYear.setSelected(FISCAL_YEAR_8);
		} else if (temp[2].equals("FY2018")) {
			selectFinancialYear.setSelected(FISCAL_YEAR_9);
		}

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
	public void saveAndUpdateView() {
		updateBudgetObject();

		saveOrUpdate(getData());

	}

	private void updateBudgetObject() {

		String budgetname = budgetNameText.getValue() != null ? budgetNameText
				.getValue() : " ";
		String financialYear = selectFinancialYear.getSelectedValue();

		String[] temp;
		String delimiter = " ";
		temp = financialYear.split(delimiter);

		data.setBudgetName(budgetname + " - " + temp[0]);

		// List<ClientBudgetItem> allGivenRecords = gridView.getRecords();
		//
		// data.setBudgetItem(allGivenRecords);

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		// BaseView.errordata.setHTML(exception.getMessage());
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		// String exceptionMessage = exception.getMessage();
		// addError(this, exceptionMessage);
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
		return result;

	}

	private void refreshView(String result) {

		/*
		 * gridView.removeAllRecords();
		 * 
		 * for (ClientBudget budget : budgetList) { List<ClientBudgetItem>
		 * budgetItemList = budget.getBudgetItem(); if
		 * (result.equals(budget.getBudgetName())) { for (ClientBudgetItem items
		 * : budgetItemList) {
		 * items.setAccountsName(items.getAccount().getName());
		 * gridView.addData(items); } break; }
		 * 
		 * }
		 */
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
		// gridView.setDisabled(false);

		super.onEdit();

	}

	@Override
	public void setFocus() {
		this.budgetStartWithSelect.setFocus();

	}

	public CellTable<ClientBudgetItem> getAccountsAmountGrid() {

		cellTable = new CellTable<ClientBudgetItem>(
				new ProvidesKey<ClientBudgetItem>() {

					@Override
					public Object getKey(ClientBudgetItem item) {
						// TODO Auto-generated method stub
						return null;
					}
				});
		cellTable.setWidth("100%");

		// Attach a column sort handler to the ListDataProvider to sort the
		// list.
		ListHandler<ClientBudgetItem> sortHandler = new ListHandler<ClientBudgetItem>(
				null);
		cellTable.addColumnSortHandler(sortHandler);

		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		SimplePager pager = new SimplePager(TextLocation.LEFT, pagerResources,
				false, 0, true);
		pager.setDisplay(cellTable);

		// Add a selection model so we can select cells.
		final SelectionModel<ClientBudgetItem> selectionModel = new MultiSelectionModel<ClientBudgetItem>(
				new ProvidesKey<ClientBudgetItem>() {

					@Override
					public Object getKey(ClientBudgetItem item) {
						// TODO Auto-generated method stub
						return null;
					}
				});

		cellTable.setSelectionModel(selectionModel,
				DefaultSelectionEventManager
						.<ClientBudgetItem> createCheckboxManager());

		// Initialize the columns.
		initTableColumns(selectionModel, sortHandler);

		ListDataProvider<ClientBudgetItem> dataProvider = new ListDataProvider<ClientBudgetItem>();
		// Connect the table to the data provider.
		dataProvider.addDataDisplay(cellTable);
		// Add the data to the data provider, which automatically pushes it to
		// the
		// widget.
		List<ClientBudgetItem> list = dataProvider.getList();

		for (ClientAccount account : listOfAccounts) {
			ClientBudgetItem obj = new ClientBudgetItem();
			obj.setAccountsName(account.getName());
			obj.setAccount(account);
			list.add(obj);
		}
		return cellTable;
	}

	private void initTableColumns(
			SelectionModel<ClientBudgetItem> selectionModel2,
			ListHandler<ClientBudgetItem> sortHandler) {
		// adding accounts column to cell table
		Column<ClientBudgetItem, String> accountInfoColumn = new Column<ClientBudgetItem, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return object.getAccountsName();

			}
		};
		accountInfoColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index,
							final ClientBudgetItem object, String value) {

						HashMap<String, String> map = new HashMap<String, String>();
						String budgetTitle = "Add Budget for "
								+ object.getAccountsName();
						AddBudgetAmountDialogue assignAccountsTo1099Dialog = new AddBudgetAmountDialogue(
								budgetTitle, "", map, object);
						assignAccountsTo1099Dialog
								.setCallback(new ActionCallback<HashMap<String, String>>() {

									@Override
									public void actionResult(
											HashMap<String, String> result) {
										refreshView(result, object);

									}
								});
						assignAccountsTo1099Dialog.show();

						// TODO Auto-generated method stub
					}
				});

		Column<ClientBudgetItem, String> janMonthColumn = new Column<ClientBudgetItem, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJanuaryAmount());

			}
		};
		janMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> febMonthColumn = new Column<ClientBudgetItem, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJanuaryAmount());

			}
		};
		febMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> marMonthColumn = new Column<ClientBudgetItem, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJanuaryAmount());

			}
		};
		marMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> aprMonthColumn = new Column<ClientBudgetItem, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJanuaryAmount());

			}
		};
		aprMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> mayMonthColumn = new Column<ClientBudgetItem, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJanuaryAmount());

			}
		};
		mayMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> juneMonthColumn = new Column<ClientBudgetItem, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJanuaryAmount());

			}
		};
		juneMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> julMonthColumn = new Column<ClientBudgetItem, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJanuaryAmount());

			}
		};
		julMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> augMonthCoulmn = new Column<ClientBudgetItem, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJanuaryAmount());

			}
		};
		augMonthCoulmn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> septMonthColumn = new Column<ClientBudgetItem, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJanuaryAmount());

			}
		};
		septMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> octMonthColumn = new Column<ClientBudgetItem, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJanuaryAmount());

			}
		};
		octMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> novMonthColumn = new Column<ClientBudgetItem, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJanuaryAmount());

			}
		};
		novMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		Column<ClientBudgetItem, String> decMonthColumn = new Column<ClientBudgetItem, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJanuaryAmount());

			}
		};
		decMonthColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, String>() {

					@Override
					public void update(int index, ClientBudgetItem object,
							String value) {
						// TODO Auto-generated method stub

					}

				});

		cellTable.addColumn(accountInfoColumn, messages.Account());
		cellTable.addColumn(janMonthColumn, messages.jan());
		cellTable.addColumn(febMonthColumn, messages.feb());
		cellTable.addColumn(marMonthColumn, messages.mar());
		cellTable.addColumn(aprMonthColumn, messages.apr());
		cellTable.addColumn(mayMonthColumn, messages.may());
		cellTable.addColumn(juneMonthColumn, messages.jun());
		cellTable.addColumn(julMonthColumn, messages.jul());
		cellTable.addColumn(augMonthCoulmn, messages.aug());
		cellTable.addColumn(septMonthColumn, messages.sept());
		cellTable.addColumn(octMonthColumn, messages.oct());
		cellTable.addColumn(novMonthColumn, messages.nov());
		cellTable.addColumn(decMonthColumn, messages.dec());

	}

	private void refreshView(HashMap<String, String> result,
			ClientBudgetItem obj) {

		obj.setJanuaryAmount(Double.parseDouble(result.get("jan")));
		obj.setFebruaryAmount(Double.parseDouble(result.get("feb")));
		obj.setMarchAmount(Double.parseDouble(result.get("mar")));
		obj.setAprilAmount(Double.parseDouble(result.get("apr")));
		obj.setMayAmount(Double.parseDouble(result.get("may")));
		obj.setJuneAmount(Double.parseDouble(result.get("jun")));
		obj.setJulyAmount(Double.parseDouble(result.get("jul")));
		obj.setAugustAmount(Double.parseDouble(result.get("aug")));
		obj.setOctoberAmount(Double.parseDouble(result.get("oct")));
		obj.setNovemberAmount(Double.parseDouble(result.get("nov")));
		obj.setSeptemberAmount(Double.parseDouble(result.get("sept")));
		obj.setDecemberAmount(Double.parseDouble(result.get("dec")));

		Double total;
		total = Double.parseDouble(result.get("jan"))
				+ Double.parseDouble(result.get("feb"))
				+ Double.parseDouble(result.get("mar"))
				+ Double.parseDouble(result.get("apr"))
				+ Double.parseDouble(result.get("may"))
				+ Double.parseDouble(result.get("jun"))
				+ Double.parseDouble(result.get("jul"))
				+ Double.parseDouble(result.get("aug"))
				+ Double.parseDouble(result.get("oct"))
				+ Double.parseDouble(result.get("nov"))
				+ Double.parseDouble(result.get("sept"))
				+ Double.parseDouble(result.get("dec"));

		obj.setTotalAmount(total);

		refreshAllRecords();

	}

	private void refreshAllRecords() {

	}

}
