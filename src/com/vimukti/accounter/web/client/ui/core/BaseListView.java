package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.Resources;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientBudgetItem;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.BudgetListView;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.company.JournalEntryListView;
import com.vimukti.accounter.web.client.ui.customers.CustomerListView;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.PurchaseOrderListGrid;
import com.vimukti.accounter.web.client.ui.grids.SalesOrderListGrid;
import com.vimukti.accounter.web.client.ui.vat.ChalanDetailsListView;
import com.vimukti.accounter.web.client.ui.vendors.VendorListView;
import com.vimukti.accounter.web.client.util.CountryPreferenceFactory;

/**
 * 
 * This Class Serves as the Base Class for all List Views, i.e CustomerList,
 * VendorList, InvoiceList
 * 
 * @author Fernandez
 * 
 * @param <T>
 */

public abstract class BaseListView<T> extends AbstractBaseView<T> implements
		IAccounterList<T>, AsyncCallback<PaginationList<T>>,
		ISavableView<Map<String, Object>> {
	protected BaseListGrid grid;
	boolean budgetItemsExists = false;
	protected static AccounterMessages messages = Global.get().messages();
	protected int start;
	protected boolean isActive = true;
	public static final int DEFAULT_PAGE_SIZE = 50;

	public BaseListView() {
	}

	protected List<String> getViewSelectTypes() {
		return null;
	}

	public BaseListGrid getGrid() {
		return grid;
	}

	protected Label totalLabel;

	protected List<T> records;

	protected double total;
	protected boolean isDeleteDisable;
	protected SelectCombo viewSelect, dateRangeSelector;

	protected List<T> initialRecords = new ArrayList<T>();

	protected HorizontalPanel gridLayout;

	private final int TOP = 145;
	private final int BORDER = 20;
	private final int FOOTER = 22;
	protected boolean isViewSelectRequired = true;

	protected int cmd;
	public DateItem fromItem;
	public DateItem toItem;
	public Button updateButton;
	public Button prepare1099MiscForms;
	public Button budgetEdit;
	protected ClientPayee selectedPayee;
	private PaginationList<ClientBudget> budgetList;
	private ClientBudget budgetData;

	@Override
	public void init() {
		createControls();
	}

	@Override
	public void initData() {
		initListCallback();
		super.initData();
	}

	// public void initGrid(List<T> resultrecords) {
	// // grid.setRecords((List<IsSerializable>) (ArrayList) resultrecords);
	// if (resultrecords != null) {
	//
	// List<T> records = resultrecords;
	//
	// if (records != null) {
	// for (T t : records) {
	// addToGrid(t);
	// }
	//
	// }
	// }
	// }

	@Override
	public void deleteFromGrid(T objectToBeRemoved) {

		// this.grid.deleteRecord((IsSerializable) objectToBeRemoved);
		if (totalLabel != null) {
			updateTotal(objectToBeRemoved, false);
		}

	}

	@Override
	public final boolean isAListView() {
		return true;
	}

	protected void createControls() {
		HorizontalPanel hlay = new HorizontalPanel();
		hlay.setWidth("100%");

		viewSelect = getSelectItem();
		if (this instanceof BudgetListView) {
			if (viewSelect == null) {
				viewSelect = new SelectCombo(messages.currentBudget());
				viewSelect.setHelpInformation(true);
				viewSelect
						.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
							@Override
							public void selectedComboBoxItem(String selectItem) {
								if (viewSelect.getSelectedValue() != null) {
									budgetData = budgetList.get(viewSelect
											.getSelectedIndex());
									changeBudgetGrid(viewSelect
											.getSelectedIndex());
								}

							}
						});

			}
		} else {
			if (viewSelect == null) {
				viewSelect = new SelectCombo(messages.currentView());
				viewSelect.setHelpInformation(true);
				viewSelect.setComboItem(messages.active());
				// viewSelect.setWidth("150px");
				List<String> typeList = new ArrayList<String>();
				typeList.add(messages.active());
				typeList.add(messages.inActive());
				viewSelect.initCombo(typeList);
				viewSelect
						.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

							@Override
							public void selectedComboBoxItem(String selectItem) {
								if (viewSelect.getSelectedValue() != null) {
									if (viewSelect.getSelectedValue()
											.toString()
											.equalsIgnoreCase("Active"))
										filterList(true);
									else
										filterList(false);
								}
							}
						});
			}
		}

		dateRangeSelector = getDateRangeSelectItem();

		if (dateRangeSelector == null) {
			dateRangeSelector = new SelectCombo(messages.date());
			dateRangeSelector.setHelpInformation(true);
			// dateRangeSelector.setWidth("150px");
			List<String> typeList = new ArrayList<String>();
			typeList.add(messages.active());
			typeList.add(messages.inActive());
			dateRangeSelector.initCombo(typeList);
			dateRangeSelector.setDefaultValue(messages.active());
			dateRangeSelector.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					if (dateRangeSelector.getSelectedValue() != null) {

					}

				}
			});
		}

		fromItem = new DateItem();
		fromItem.setHelpInformation(true);
		fromItem.setTitle(messages.from());
		if (Accounter.getStartDate() != null) {
			fromItem.setDatethanFireEvent(Accounter.getStartDate());
		} else {
			fromItem.setDatethanFireEvent(new ClientFinanceDate());
		}
		toItem = new DateItem();
		toItem.setHelpInformation(true);
		toItem.setTitle(messages.to());
		toItem.setDatethanFireEvent(Accounter.getCompany()
				.getCurrentFiscalYearEndDate());
		// .getLastandOpenedFiscalYearEndDate());
		fromItem.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dateRangeSelector.addComboItem(messages.custom());
				dateRangeSelector.setComboItem(messages.custom());
				updateButton.setEnabled(true);
			}
		});
		toItem.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dateRangeSelector.addComboItem(messages.custom());
				dateRangeSelector.setComboItem(messages.custom());
				updateButton.setEnabled(true);
			}
		});
		updateButton = new Button(messages.update());
		updateButton.setEnabled(false);
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (dateRangeSelector.getSelectedValue().equals(
						messages.custom())) {
					customManage();
				}
			}
		});

		prepare1099MiscForms = new Button(messages.prepare1099MiscForms());
		prepare1099MiscForms.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getPrepare1099MISCAction().run(null, true);

			}
		});

		budgetEdit = new Button(messages.edit());
		budgetEdit.setWidth("10");

		budgetEdit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (budgetItemsExists == true) {
					ActionFactory.getNewBudgetAction().run(budgetData, false);
				}
			}
		});

		DynamicForm form = new DynamicForm();

		if (isTransactionListView()) {
			form.setNumCols(8);
			form.addStyleName("transations_list_table");
			if (this instanceof JournalEntryListView) {
				form.setItems(dateRangeSelector, fromItem, toItem);
			} else {
				form.setItems(viewSelect, dateRangeSelector, fromItem, toItem);
			}
			hlay.add(form);
			hlay.add(updateButton);
			hlay.setCellHorizontalAlignment(form, ALIGN_RIGHT);
			hlay.setCellHorizontalAlignment(updateButton,
					HasHorizontalAlignment.ALIGN_RIGHT);
		} else if (this instanceof VendorListView
				&& getCompany().getCountry().equals(
						CountryPreferenceFactory.UNITED_STATES)) {
			form.setFields(viewSelect);
			hlay.add(prepare1099MiscForms);
			hlay.setCellWidth(prepare1099MiscForms, "65%");
			hlay.add(form);
			hlay.setCellHorizontalAlignment(form, ALIGN_RIGHT);
			hlay.setCellHorizontalAlignment(prepare1099MiscForms, ALIGN_RIGHT);
			hlay.addStyleName("vendor_list_1099");
		} else if (this instanceof BudgetListView) {
			form.setFields(viewSelect);
			hlay.add(form);
			hlay.add(budgetEdit);
			hlay.setCellHorizontalAlignment(form, ALIGN_RIGHT);
		} else {

			if (!(this instanceof JournalEntryListView)
					&& !(this instanceof ChalanDetailsListView))
				if (viewSelect != null) {
					form.setFields(viewSelect);
				}
			hlay.add(form);
			hlay.setCellHorizontalAlignment(form, ALIGN_RIGHT);
		}
		// hlay.add(form);
		// hlay.setCellHorizontalAlignment(form, ALIGN_RIGHT);
		VerticalPanel vlayTop = new VerticalPanel();
		HorizontalPanel hlayTop = new HorizontalPanel();
		hlayTop.setWidth("100%");
		if (isTransactionListView()) {
			// vlayTop.add(addNewLabel);
			vlayTop.add(hlayTop);
			// addNewLabel.setWidth((getAddNewLabelString().length() * 6) +
			// "px");
		} else {
			// hlayTop.add(addNewLabel);
			// if (getAddNewLabelString().length() != 0) {
			// hlayTop.setCellWidth(addNewLabel, getAddNewLabelString()
			// .length() + "px");
			// }
		}
		if (isViewSelectRequired)
			hlayTop.add(hlay);

		Label lab1 = new Label(getListViewHeading());
		lab1.addStyleName("label-title-list");
		lab1.setWidth("100%");

		initGrid();

		HorizontalPanel totalLayout = getTotalLayout(grid);
		gridLayout = new HorizontalPanel() {
			@Override
			protected void onAttach() {
				if (grid.isShowFooter())
					grid.setHeight(this.getOffsetHeight() - 22 - 5 + "px");
				else
					grid.setHeight(this.getOffsetHeight() - 5 + "px");
				super.onAttach();
			}
		};
		gridLayout.setWidth("100%");
		gridLayout.setHeight("100%");
		grid.setView(this);
		gridLayout.add(grid);
		if (grid instanceof PurchaseOrderListGrid
				|| grid instanceof SalesOrderListGrid) {
			gridLayout.setCellWidth(grid, "70%");
			gridLayout.setCellHeight(grid, "100%");
		}

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setHeight("100%");
		mainVLay.setWidth("100%");

		if (totalLayout != null) {
			if (isTransactionListView()) {
				mainVLay.add(vlayTop);
				AccounterDOM.setParentElementHeight(vlayTop.getElement(), 10);
			} else {
				mainVLay.add(hlayTop);
				AccounterDOM.setParentElementHeight(hlayTop.getElement(), 5);
			}
			mainVLay.add(lab1);
			mainVLay.add(gridLayout);
			mainVLay.add(totalLayout);

		} else {
			if (isTransactionListView()) {
				mainVLay.add(vlayTop);
				AccounterDOM.setParentElementHeight(vlayTop.getElement(), 10);
			} else {
				mainVLay.add(hlayTop);
				AccounterDOM.setParentElementHeight(hlayTop.getElement(), 5);
			}
			mainVLay.add(lab1);
			AccounterDOM.setParentElementHeight(lab1.getElement(), 5);
			mainVLay.add(gridLayout);
		}
		int pageSize = getPageSize();
		if (pageSize != -1) {
			grid.addRangeChangeHandler2(new Handler() {

				@Override
				public void onRangeChange(RangeChangeEvent event) {
					onPageChange(event.getNewRange().getStart(), event
							.getNewRange().getLength());
				}
			});
			SimplePager pager = new SimplePager(TextLocation.CENTER,
					(Resources) GWT.create(Resources.class), false,
					pageSize * 2, true);
			pager.setDisplay(grid);
			updateRecordsCount(0, 0, 0);
			mainVLay.add(pager);
		}
		add(mainVLay);
		setSize("100%", "100%");

	}

	protected int getPageSize() {
		return -1;
	}

	public void updateRecordsCount(int start, int length, int total) {
		grid.updateRange(new Range(start, getPageSize()));
		grid.setRowCount(total, (start + length) == total);

	}

	protected void onPageChange(int start, int length) {
		// TODO Auto-generated method stub

	}

	protected boolean isTransactionListView() {
		return false;
	}

	public void customManage() {

	}

	protected SelectCombo getSelectItem() {
		return null;
	}

	protected SelectCombo getDateRangeSelectItem() {
		return null;
	}

	protected void filterList(boolean isActive) {
	}

	protected void changeBudgetGrid(int numberSelected) {
	}

	protected abstract void initGrid();

	protected HorizontalPanel getTotalLayout(BaseListGrid grid) {

		return null;
	}

	protected abstract String getListViewHeading();

	@Override
	public void addToGrid(T objectToBeAdded) {
		grid.addData(objectToBeAdded);
		if (totalLabel != null) {
			updateTotal(objectToBeAdded, true);
		}

	}

	protected void updateTotal(T t, boolean add) {

	}

	protected abstract Action getAddNewAction();

	protected abstract String getAddNewLabelString();

	public void initListCallback() {
		grid.removeAllRecords();
		grid.addLoadingImagePanel();
		if (getPageSize() != -1) {
			grid.setVisibleRange(start, getPageSize());
		}
	}

	protected AccounterAsyncCallback<Boolean> getGeneralizedDeleteCallBack(
			final T object) {

		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				deleteFailed(caught);
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result != null && result) {
					// Accounter.showInformation("Deleted Successfully");
					deleteFromGrid(object);
				} else
					deleteFailed(null);

			}
		};

		return callback;

	}

	@Override
	public void onFailure(Throwable exception) {
		if (exception instanceof AccounterException) {
			Accounter.showError(messages.failedRequest());
			return;
		}
		Accounter.showMessage(messages.sessionExpired());
	}

	@Override
	public void onSuccess(PaginationList<T> result) {
		if (result.isEmpty()) {
			grid.removeAllRecords();
			grid.addEmptyMessage(messages.noRecordsToShow());

			if (this instanceof BudgetListView) {
				budgetEdit.setEnabled(false);
			}
			return;
		}
		grid.removeLoadingImage();
		if (result != null) {
			initialRecords = result;
			this.records = result;

			if (this instanceof BudgetListView) {

				budgetList = (PaginationList<ClientBudget>) result;

				List<String> typeList = new ArrayList<String>();
				for (ClientBudget budget : (List<ClientBudget>) result) {
					typeList.add(budget.getBudgetName());
					budgetItemsExists = true;
				}
				if (typeList.size() < 1) {
					budgetItemsExists = false;
					typeList.add(messages.NoBudgetadded());
				}
				viewSelect.initCombo(typeList);
				viewSelect.setSelectedItem(0);
				if (budgetList.size() > 0)
					budgetData = budgetList.get(0);

				if (result.size() > 0) {
					ClientBudget budget = (ClientBudget) result.get(0);
					List<ClientBudgetItem> budgetItems = new ArrayList<ClientBudgetItem>();
					budgetItems = budget.getBudgetItem();
					for (ClientBudgetItem budgetItem : budgetItems) {
						budgetItem.setAccountsName(budgetItem.getAccount()
								.getName());
					}
					grid.setRecords(budgetItems);
					budgetEdit.setEnabled(true);
				} else {

					grid.addEmptyMessage(messages.noRecordsToShow());
					budgetEdit.setEnabled(false);
				}

			} else if (this instanceof CustomerListView
					|| this instanceof VendorListView
					|| this instanceof BudgetListView) {
				filterList(true);
			} else {
				grid.setRecords(result);
			}
		} else {
			Accounter.showInformation(messages.noRecordsToShow());
			grid.removeLoadingImage();
		}
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
	}

	public List<ClientFixedAsset> getAssetsByType(int type,
			List<ClientFixedAsset> records) {
		List<ClientFixedAsset> pendingItems = new ArrayList<ClientFixedAsset>();
		List<ClientFixedAsset> registeredItems = new ArrayList<ClientFixedAsset>();
		List<ClientFixedAsset> soldDispzdItems = new ArrayList<ClientFixedAsset>();
		for (ClientFixedAsset asset : records) {
			if (asset.getStatus() == ClientFixedAsset.STATUS_REGISTERED) {
				registeredItems.add(asset);
			} else if (asset.getStatus() == ClientFixedAsset.STATUS_SOLD_OR_DISPOSED) {
				soldDispzdItems.add(asset);
			} else
				pendingItems.add(asset);
		}
		switch (type) {
		case 1:
			return pendingItems;
		case 2:
			return registeredItems;

		case 3:
			return soldDispzdItems;
		default:
			return pendingItems;
		}
	}

	protected <A extends IAccounterCore> void deleteObject(final A core,
			AccounterCoreType coreType) {
		Accounter.deleteObject(this, core);
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		if (this.viewSelect != null) {
			this.viewSelect.setFocus();
		}
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		if (getCallback() != null)
			getCallback().onResultSuccess(result);
	}

	@Override
	public void fitToSize(int height, int width) {
		if (grid.isShowFooter())
			grid.setHeight(height - TOP - FOOTER + "px");
		else
			grid.setHeight(height - TOP + "px");
		// grid.setHeight("100%");

		grid.setWidth("100%");

	}

	public void disableFilter() {
		if (this.viewSelect != null) {
			this.viewSelect.setDisabled(true);
		}
	}

	public void setSelectedPayee(ClientPayee selectedPayee) {
		this.selectedPayee = selectedPayee;
	}

	public ClientPayee getSelectedPayee() {
		return selectedPayee;
	}

	@Override
	public void restoreView(Map<String, Object> viewDate) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Object> saveView() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Called by subclasses while downloading the csv file
	 * 
	 * @param filename
	 * @return
	 */
	protected AsyncCallback<String> getExportCSVCallback(final String filename) {
		return new AsyncCallback<String>() {

			@Override
			public void onSuccess(String id) {
				UIUtils.downloadFileFromTemp(filename + ".csv", id);

			}

			@Override
			public void onFailure(Throwable arg0) {
				arg0.printStackTrace();

			}
		};
	}
}
