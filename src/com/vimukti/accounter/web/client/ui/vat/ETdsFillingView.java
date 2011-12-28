package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientETDSFilling;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.ETdsCellTable;

public class ETdsFillingView extends BaseView<ClientETDSFilling> {

	private SelectCombo formType;
	private Label lab1;

	VerticalPanel mainVLay;

	ClientBudget budgetForEditing = new ClientBudget();

	private ETdsCellTable tdsCellTable;
	private SelectCombo slectAssecementYear;
	private SelectCombo financialYearCombo;
	private SelectCombo quaterSelectionCombo;
	private DynamicForm dynamicFormLeft;
	private DynamicForm dynamicFormRight;
	private int formNoSelected;
	private int quaterSelected;
	private int startYear;
	private int endYear;
	protected ArrayList<ClientETDSFilling> eTDSList;

	public ETdsFillingView() {

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
			ClientETDSFilling account = new ClientETDSFilling();
			setData(account);
		}

	}

	private void createControls() {

		lab1 = new Label();
		lab1.removeStyleName("gwt-Label");
		lab1.setText("e-TDS Filling");
		lab1.addStyleName("label-title");

		formType = new SelectCombo("Form No.");
		formType.setHelpInformation(true);
		formType.initCombo(getFormTypes());
		formType.setSelectedItem(0);
		formType.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

			@Override
			public void selectedComboBoxItem(String selectItem) {
				if (selectItem.equals(getFormTypes().get(1))) {
					formNoSelected = 1;
				} else if (selectItem.equals(getFormTypes().get(2))) {
					formNoSelected = 2;
				} else if (selectItem.equals(getFormTypes().get(3))) {
					formNoSelected = 3;
				} else if (selectItem.equals(getFormTypes().get(4))) {
					formNoSelected = 3;
				}
				initRPCService();
			}
		});

		slectAssecementYear = new SelectCombo("Assessment year");
		slectAssecementYear.setHelpInformation(true);
		slectAssecementYear.initCombo(getFinancialYearList());
		slectAssecementYear.setDisabled(true);

		financialYearCombo = new SelectCombo("Financial Year");
		financialYearCombo.setHelpInformation(true);
		financialYearCombo.initCombo(getFinancialYearList());
		financialYearCombo.setSelectedItem(0);
		financialYearCombo.setDisabled(isInViewMode());
		financialYearCombo.setRequired(true);
		financialYearCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						slectAssecementYear.setSelected(getFinancialYearList()
								.get(financialYearCombo.getSelectedIndex() + 1));

						String delims = "-";
						String[] tokens = selectItem.split(delims);

						startYear = Integer.parseInt(tokens[0]);
						endYear = Integer.parseInt(tokens[1]);
						initRPCService();
					}

				});

		quaterSelectionCombo = new SelectCombo("For Quarter");
		quaterSelectionCombo.setHelpInformation(true);
		quaterSelectionCombo.initCombo(getFinancialQuatersList());
		quaterSelectionCombo.setSelectedItem(0);
		quaterSelectionCombo.setDisabled(isInViewMode());
		quaterSelectionCombo.setRequired(true);
		quaterSelectionCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						if (selectItem.equals(getFinancialQuatersList().get(0))) {
							quaterSelected = 1;
						} else if (selectItem.equals(getFinancialQuatersList()
								.get(1))) {
							quaterSelected = 2;
						} else if (selectItem.equals(getFinancialQuatersList()
								.get(2))) {
							quaterSelected = 3;
						} else if (selectItem.equals(getFinancialQuatersList()
								.get(3))) {
							quaterSelected = 4;
						}
						initRPCService();
					}
				});

		dynamicFormLeft = UIUtils.form(messages.chartOfAccountsInformation());
		dynamicFormLeft.setWidth("100%");
		dynamicFormLeft.setFields(formType, financialYearCombo);

		dynamicFormRight = UIUtils.form(messages.chartOfAccountsInformation());
		dynamicFormRight.setWidth("100%");
		dynamicFormRight.setFields(quaterSelectionCombo, slectAssecementYear);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(dynamicFormLeft);
		topHLay.add(dynamicFormRight);

		tdsCellTable = new ETdsCellTable();
		tdsCellTable.setStyleName("user_activity_log");

		mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "300px");
		mainVLay.add(lab1);
		mainVLay.add(topHLay);

		ScrollPanel scroll = new ScrollPanel();
		scroll.add(tdsCellTable);
		scroll.setSize("910px", "100%");
		scroll.setTouchScrollingDisabled(false);

		mainVLay.add(scroll);

		this.add(mainVLay);

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
		return "e-TDS Filling";

	}

	@Override
	public void saveAndUpdateView() {
		saveOrUpdate(getData());

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);

		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

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

		return result;

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void setFocus() {

	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<String> getFormTypes() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("--select--");
		list.add("26Q");
		list.add("27Q");
		list.add("27EQ");

		return list;
	}

	@Override
	protected void initRPCService() {
		// TODO Auto-generated method stub
		super.initRPCService();
		Accounter.createHomeService().getEtdsDetails(formNoSelected,
				quaterSelected, startYear, endYear,
				new AccounterAsyncCallback<ArrayList<ClientETDSFilling>>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(
							ArrayList<ClientETDSFilling> result) {
						eTDSList = result;
						tdsCellTable.setDataProvidedValue(eTDSList);

					}
				});
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		// super.createButtons(buttonBar);

		ImageButton verifyButton = new ImageButton("Download .txt file",
				Accounter.getFinanceImages().saveAndClose());
		verifyButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				UIUtils.generateETDSFillingtext(formNoSelected, quaterSelected,
						startYear, endYear);

			}
		});
		buttonBar.add(verifyButton);

	}
}
