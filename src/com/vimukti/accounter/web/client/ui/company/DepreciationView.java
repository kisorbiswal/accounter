package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientDepreciation;
import com.vimukti.accounter.web.client.core.ClientDepreciationDummyEntry;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.DepreciableFixedAssetsEntry;
import com.vimukti.accounter.web.client.core.Lists.DepreciableFixedAssetsList;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetLinkedAccountMap;
import com.vimukti.accounter.web.client.core.Lists.LinkAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.fixedassets.RollBackDepreciationDialog;
import com.vimukti.accounter.web.client.ui.fixedassets.StartDateDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.DepreciationTreeGrid;

/**
 * 
 * @author Raj Vimal
 * 
 */
public class DepreciationView extends BaseView<ClientDepreciation> {

	private DepreciationTreeGrid grid;
	private ClientFinanceDate depreciationStartDate;
	protected ListBox depreciatedToCombo;
	private ArrayList<DynamicForm> listforms;
	protected ClientFinanceDate depreciationEndDate;
	private AccounterButton startDateButton;
	protected ClientAccount account;
	private List<Long> assetIDList;
	private List<ClientFiscalYear> openFiscalYears;
	private Label fromLabel;
	private DateTimeFormat format;
	private Map<Long, List<DepreciableFixedAssetsEntry>> assets;

	public DepreciationView() {
		super();
		assetIDList = new ArrayList<Long>();
		account = new ClientAccount();
		getDepriciationLastDate();
		validationCount = 1;
	}

	@Override
	public void init() {
		super.init();

		createControls();

	}

	private void createControls() {

		listforms = new ArrayList<DynamicForm>();
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setSize("100%", "");

		Label titleLabel = new Label(Accounter.constants().depreciation());
		titleLabel.setStyleName(Accounter.constants().labelTitle());
		mainPanel.add(titleLabel);

		startDateButton = new AccounterButton(Accounter.constants().startDate());
		startDateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new StartDateDialog();

			}
		});

		AccounterButton rollBackDepreciation = new AccounterButton(Accounter
				.constants().rollBackDepreciation());
		rollBackDepreciation.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new RollBackDepreciationDialog();
			}
		});

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(10);
		buttonPanel.add(startDateButton);
		startDateButton.enabledButton();
		buttonPanel.add(rollBackDepreciation);
		rollBackDepreciation.enabledButton();
		mainPanel.add(buttonPanel);

		fromLabel = new Label(Accounter.constants().depricatiedFrom());

		format = DateTimeFormat.getFormat(Accounter.constants().ddMMyyyy());
		// fromLabel.setText("Depreciate from:  "
		// + format.format(depreciationStartDate));
		depreciatedToCombo = new ListBox();

		depreciatedToCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getDepreciableFixedAssets();

			}
		});

		AccounterButton updateButton = new AccounterButton(Accounter
				.constants().update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				getDepreciableFixedAssets();
			}
		});
		HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(10);
		panel.add(fromLabel);
		panel.add(new HTML(Accounter.constants().depreciateTo()));
		panel.add(depreciatedToCombo);
		// panel.add(updateButton);
		mainPanel.add(panel);

		VerticalPanel gridPanel = new VerticalPanel();

		grid = new DepreciationTreeGrid("");
		grid.isEnable = false;
		grid.init();
		grid.setHeight("300px");
		grid.initParentAndChildIcons(Accounter.getFinanceMenuImages()
				.newAccount().getURL(), Accounter.getFinanceMenuImages()
				.newFixedAsset().getURL());
		gridPanel.add(grid);

		mainPanel.add(gridPanel);

		HorizontalPanel actionButtonPanel = new HorizontalPanel();
		mainPanel.add(actionButtonPanel);

		canvas.add(mainPanel);

	}

	private void getDepriciationLastDate() {

		AsyncCallback<ClientFinanceDate> callBack = new AsyncCallback<ClientFinanceDate>() {

			public void onFailure(Throwable caught) {
				saveFailed(caught);
				return;

			}

			public void onSuccess(ClientFinanceDate date) {
				if (date == null) {
					ClientFinanceDate date2 = new ClientFinanceDate(
							getCompany().getPreferences()
									.getDepreciationStartDate());
					depreciationStartDate = new ClientFinanceDate(
							date2.getYear(), date2.getMonth(), 1);
				} else {
					depreciationStartDate = new ClientFinanceDate(
							date.getYear(), date.getMonth(), date.getDay() + 1);
				}
				fromLabel
						.setText("Depreciate from:  "
								+ format.format(depreciationStartDate
										.getDateAsObject()));
				for (String dateArray : getDatesArray()) {
					depreciatedToCombo.addItem(dateArray);
				}

				getDepreciableFixedAssets();
			}

		};
		Accounter.createHomeService().getDepreciationLastDate(callBack);
	}

	@Override
	public void initData() {
		super.initData();
		initGrid();

	}

	private void initGrid() {

	}

	private List<String> getDatesArray() {
		// Date startdate = depreciationStartDate;
		// Date tempDate = new Date(FinanceApplication.getCompany()
		// .getpreferences().getDepreciationStartDate());
		// Date endDate = new Date(tempDate.getYear() + 1, tempDate.getMonth(),
		// tempDate.getDate() - 1);
		// int startMonth = startdate.getMonth();
		// DateTimeFormat format = DateTimeFormat.getFormat(FinanceApplication
		// .constants().ddMMMyyyy());
		// String[] dateArray = new String[12];
		// for (int i = startMonth, j = 0; i < 12; i++, j++) {
		// int currentDate;
		// if (i == 1 && startdate.getYear() % 400 == 0) {
		// currentDate = 29;
		// } else if (i == 0 || i == 2 || i == 4 || i == 6 || i == 7 || i == 9
		// || i == 11) {
		// currentDate = 31;
		// } else if (i == 1) {
		// currentDate = 28;
		// } else {
		// currentDate = 30;
		// }
		// Date date2 = new Date(startdate.getYear(), i, currentDate);
		// if (date2.before(endDate) || date2.equals(endDate)) {
		// dateArray[j] = format.format(date2);
		// }
		// }

		DateTimeFormat format = DateTimeFormat.getFormat(Accounter.constants()
				.ddMMyyyy());
		List<String> dates = new ArrayList<String>();
		Calendar fromDateCal = Calendar.getInstance();
		fromDateCal.setTime(depreciationStartDate.getDateAsObject());

		Calendar startDateCal = Calendar.getInstance();
		startDateCal
				.setTime(new ClientFinanceDate(Accounter.getCompany()
						.getpreferences().getDepreciationStartDate())
						.getDateAsObject());

		Calendar toDateCal = Calendar.getInstance();
		int year = 0;
		if (fromDateCal.get(Calendar.MONTH) >= startDateCal.get(Calendar.MONTH)) {
			year = fromDateCal.get(Calendar.YEAR) + 1;
		} else {
			year = fromDateCal.get(Calendar.YEAR);
		}

		openFiscalYears = AccounterValidator.getOpenFiscalYears();

		toDateCal.set(Calendar.YEAR, year);
		int month = startDateCal.get(Calendar.MONTH) - 1;
		toDateCal.set(Calendar.DAY_OF_MONTH, 1);
		toDateCal.set(Calendar.MONTH, month);

		while (fromDateCal.getTime().compareTo(toDateCal.getTime()) < 0) {
			Calendar tempCal = Calendar.getInstance();
			tempCal.set(Calendar.YEAR, fromDateCal.get(Calendar.YEAR));
			tempCal.set(Calendar.MONTH, fromDateCal.get(Calendar.MONTH));
			tempCal.set(Calendar.DAY_OF_MONTH,
					fromDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));

			if (validateDate(new ClientFinanceDate(tempCal.date)))
				dates.add(format.format(tempCal.getTime()));
			fromDateCal
					.set(Calendar.MONTH, fromDateCal.get(Calendar.MONTH) + 1);

		}

		return dates;
	}

	private boolean validateDate(ClientFinanceDate date) {

		boolean validDate = true;
		for (ClientFiscalYear openFiscalYear : openFiscalYears) {
			ClientFinanceDate endDate = openFiscalYear.getEndDate();
			/**
			 * The below code will increment the fiscal year end date by 1, this
			 * will be helpful when the date and fiscal year end date are same.
			 **/
			endDate.setDay(endDate.getDay() + 1);
			int before = date.compareTo(openFiscalYear.getStartDate());
			int after = date.compareTo(endDate);

			validDate = (before < 0 || after > 0) ? false : true;
			if (validDate)
				break;

		}
		return validDate;
	}

	@Override
	public void saveAndUpdateView() throws Exception {
		try {
			ClientDepreciation depreciation = new ClientDepreciation();
			depreciation.setDepreciateFrom(depreciationStartDate.getDate());
			depreciation.setDepreciateTo(depreciationEndDate.getDate());
			// depreciation
			// .setDepreciationFor(ClientDepreciation.DEPRECIATION_FOR_ALL_FIXEDASSET);
			// depreciation.setFixedAsset(getAssetsList())
			// // depreciation.setFixedAssets(getAssetsList());
			// depreciation.setStatus(ClientDepreciation.APPROVE);
			//
			List<LinkAccount> linkedAccounts = new ArrayList<LinkAccount>();
			for (ClientDepreciationDummyEntry entry : grid.getNodes()) {

				for (Long key : assets.keySet()) {
					ClientAccount account = getCompany().getAccount(key);
					if (account.getName().equals(entry.getFixedAssetName())) {
						LinkAccount link = new LinkAccount();
						link.setAssetAccount(key);
						link.setLinkedAccount(entry.getAssetAccount());
						linkedAccounts.add(link);
					}
				}
			}
			FixedAssetLinkedAccountMap map = new FixedAssetLinkedAccountMap();
			map.setFixedAssetLinkedAccounts(linkedAccounts);
			// depreciation.setLinkedAccounts(map);

			createObject(depreciation);
			Accounter.createHomeService().runDepreciation(
					depreciationStartDate.getDate(),
					depreciationEndDate.getDate(), map, new AsyncCallback() {

						@Override
						public void onFailure(Throwable caught) {
							if (caught instanceof InvocationException) {
								Accounter
										.showMessage("Your session expired, Please login again to continue");
							} else {
								Accounter.showError("Depreciation failed!");
							}

						}

						@Override
						public void onSuccess(Object result) {

						}

					});
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean validate() throws Exception {
		if (!(assets.size() > 0)) {
			Accounter.showError("Please select a Fixed Asset");
			return false;
		}
		return super.validate();
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			// Accounter.showInformation("Depreciation Approved");
			super.saveSuccess(result);

		} else {
			saveFailed(new Exception());
		}
	}

	@Override
	public void saveFailed(Throwable exception) {
		super.saveFailed(exception);
		// BaseView.errordata.setHTML("Can not able to apply Deprecition");
		// BaseView.commentPanel.setVisible(true);
		// this.errorOccured = true;
		MainFinanceWindow.getViewManager().showError(
				"Can not able to apply Deprecition");
	}

	protected void getDepreciableFixedAssets() {

		int index = depreciatedToCombo.getSelectedIndex();

		if (index >= 0) {

			String dateString = depreciatedToCombo.getValue(index).toString();

			depreciationEndDate = UIUtils.stringToDate(dateString, Accounter
					.constants().ddMMyyyy());

			AsyncCallback<DepreciableFixedAssetsList> callBack = new AsyncCallback<DepreciableFixedAssetsList>() {

				public void onFailure(Throwable caught) {
					saveFailed(caught);
					return;

				}

				public void onSuccess(DepreciableFixedAssetsList result) {
					setValuesInGrid(result);

				}

			};
			if (depreciationStartDate != null && depreciationEndDate != null)
				grid.removeAllRows();
			grid.addLoadingImagePanel();
			Accounter.createHomeService().getDepreciableFixedAssets(
					depreciationStartDate.getDate(),
					depreciationEndDate.getDate(), callBack);
		}
	}

	protected void setValuesInGrid(DepreciableFixedAssetsList assestsList) {
		if (assestsList != null) {
			// if (grid.getRowCount() > 0) {
			grid.removeAllRows();
			// }
			assets = assestsList.getAccountViceFixedAssets();
			if (assets.size() > 0) {
				assetIDList.addAll(assestsList.getFixedAssetIDs());
				for (Long key : assets.keySet()) {
					ClientAccount account = getCompany().getAccount(key);
					List<ClientDepreciationDummyEntry> dummyEntriesList = new ArrayList<ClientDepreciationDummyEntry>();
					for (DepreciableFixedAssetsEntry entry : assets.get(key)) {
						ClientDepreciationDummyEntry dummyEntry = new ClientDepreciationDummyEntry();
						dummyEntry.setFixedAssetName(entry.getFixedAssetName());
						dummyEntry.setAmountToBeDepreciated(entry
								.getAmountToBeDepreciated());
						dummyEntry.setAssetAccount(account != null ? account
								.getLinkedAccumulatedDepreciationAccount()
								: null);
						dummyEntriesList.add(dummyEntry);
					}
					// grid.addParent(account != null ? account.getName() : "");
					grid.addParentWithChilds(
							account != null ? account.getName() : "",
							dummyEntriesList);
				}
			} else
				grid.addEmptyMessage("No Depreciable Fixed Assets to show");
		} else
			grid.addEmptyMessage("No Depreciable Fixed Assets to show");

	}

	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.startDateButton.setFocus(true);
	}

	private List<ClientFixedAsset> getAssetsList() {
		List<ClientFixedAsset> fixedAssetList = new ArrayList<ClientFixedAsset>();
		for (Long id : assetIDList) {
			fixedAssetList.add(getCompany().getFixedAsset(id));
		}
		return fixedAssetList;
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
		super.fitToSize(height, width);

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

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
		// NOTHING TO DO
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().depreciation();
	}

}
