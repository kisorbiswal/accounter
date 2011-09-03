package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccountBudget;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.BudgetAccountGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class NewBudgetView extends BaseView<ClientAccountBudget> {

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
	private List<ClientAccount> listOfAccounts;

	VerticalPanel mainVLay;
	BudgetAccountGrid gridView;

	private ArrayList<DynamicForm> listforms;

	@Override
	public void init() {
		super.init();
		createControls();
		// resetViews();
	}

	@Override
	public void initData() {

	}

	private void initView() {

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();

		lab1 = new Label();
		lab1.removeStyleName("gwt-Label");
		lab1.addStyleName(Accounter.constants().labelTitle());
		lab1.setText(Accounter.messages().account(
				Global.get().constants().newBudget()));

		// hierarchy = new String("");

		budgetStartWithSelect = new SelectCombo(Global.get().constants()
				.budgetStartWith());
		budgetStartWithSelect.setHelpInformation(true);
		budgetStartWithSelect.initCombo(getStartWithList());
		budgetStartWithSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		budgetSubdevideBy = new SelectCombo(Global.get().constants()
				.budgetSubdivide());
		budgetSubdevideBy.setHelpInformation(true);
		budgetSubdevideBy.initCombo(getSubdevideList());
		budgetSubdevideBy
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		selectFinancialYear = new SelectCombo(Global.get().constants()
				.budgetFinancialYear());
		selectFinancialYear.setHelpInformation(true);
		selectFinancialYear.initCombo(getFiscalYearList());
		selectFinancialYear
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		budgetNameText = new TextItem(Accounter.messages().accountName(
				Global.get().constants().budget()));
		budgetNameText.setToolTip(Accounter.messages()
				.giveTheNameAccordingToYourID(this.getAction().getViewName()));
		budgetNameText.setHelpInformation(true);
		budgetNameText.setRequired(true);
		budgetNameText.setWidth(100);
		budgetNameText.setDisabled(isInViewMode());
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

		budgetInfoForm = UIUtils.form(Accounter.messages()
				.chartOfAccountsInformation(Global.get().Account()));
		budgetInfoForm.setWidth("100%");

		topHLay = new HorizontalPanel();
		topHLay.setWidth("50%");
		leftLayout = new HorizontalPanel();
		leftLayout.setWidth("90%");

		budgetInfoForm.setFields(budgetStartWithSelect, budgetSubdevideBy,
				selectFinancialYear, budgetNameText);

		leftLayout.add(budgetInfoForm);
		topHLay.add(leftLayout);

		budgetInfoForm.getCellFormatter().setWidth(0, 0, "200");

		gridView = new BudgetAccountGrid();
		gridView.setDisabled(true);
		gridView.setCanEdit(true);
		gridView.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
		gridView.isEnable = false;
		gridView.init();

		listOfAccounts = getCompany().getAccounts();
		for (ClientAccount account : listOfAccounts) {
			ClientAccountBudget obj = new ClientAccountBudget();
			gridView.addData(obj, account);
		}

		mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "300px");
		mainVLay.add(lab1);
		mainVLay.add(topHLay);
		mainVLay.add(gridView);

		// setHeightForCanvas("450");
		this.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(budgetInfoForm);

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

		list.add(AUCTUAL_AMOUNT_LAST_FISCAL_YEAR);
		list.add(AUCTUAL_AMOUNT_THIS_FISCAL_YEAR);
		list.add(NO_AMOUNT);
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
		return Global.get().constants().budget();

	}

	public List<DynamicForm> getForms() {
		return listforms;
	}

}
