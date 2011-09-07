package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientBudgetItem;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.BudgetListView;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.company.JournalEntryListView;
import com.vimukti.accounter.web.client.ui.customers.CustomerListView;
import com.vimukti.accounter.web.client.ui.customers.InvoiceListView;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.PurchaseOrderListGrid;
import com.vimukti.accounter.web.client.ui.grids.SalesOrderListGrid;
import com.vimukti.accounter.web.client.ui.vendors.VendorListView;

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
		IAccounterList<T>, AsyncCallback<ArrayList<T>> {
	protected List<String> listOfTypes;

	protected BaseListGrid grid;

	public BaseListGrid getGrid() {
		return grid;
	}

	protected Label totalLabel;

	protected List<T> records;

	protected double total;
	protected boolean isDeleteDisable;
	protected SelectCombo viewSelect, dateRangeSelector;

	protected List<T> initialRecords;

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

		Label addNewLabel = createAddNewLabel();

		HorizontalPanel hlay = new HorizontalPanel();
		hlay.setWidth("100%");

		viewSelect = getSelectItem();
		if (this instanceof BudgetListView) {
			if (viewSelect == null) {
				viewSelect = new SelectCombo(Accounter.constants()
						.currentBudget());
				viewSelect.setHelpInformation(true);
				viewSelect.setWidth("150px");

				viewSelect
						.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

							@Override
							public void selectedComboBoxItem(String selectItem) {
								if (viewSelect.getSelectedValue() != null) {
									changeBudgetGrid(viewSelect
											.getSelectedIndex());
								}

							}
						});
			}
		} else {
			if (viewSelect == null) {
				viewSelect = new SelectCombo(Accounter.constants()
						.currentView());
				viewSelect.setHelpInformation(true);
				viewSelect.setWidth("150px");
				List<String> typeList = new ArrayList<String>();
				typeList.add(Accounter.constants().active());
				typeList.add(Accounter.constants().inActive());
				viewSelect.initCombo(typeList);
				viewSelect.setComboItem(Accounter.constants().active());
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
			dateRangeSelector = new SelectCombo(Accounter.constants().date());
			dateRangeSelector.setHelpInformation(true);
			dateRangeSelector.setWidth("150px");
			List<String> typeList = new ArrayList<String>();
			typeList.add(Accounter.constants().active());
			typeList.add(Accounter.constants().inActive());
			dateRangeSelector.initCombo(typeList);
			dateRangeSelector.setDefaultValue(Accounter.constants().active());
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
		fromItem.setTitle(Accounter.constants().from());
		fromItem.setDatethanFireEvent(Accounter.getStartDate());

		toItem = new DateItem();
		toItem.setHelpInformation(true);
		toItem.setTitle(Accounter.constants().to());
		toItem.setDatethanFireEvent(Accounter.getCompany()
				.getLastandOpenedFiscalYearEndDate());

		updateButton = new Button(Accounter.constants().update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				dateRangeSelector.setDefaultValue(Accounter.constants()
						.custom());
				customManage();

			}
		});

		prepare1099MiscForms = new Button(Accounter.constants()
				.prepare1099MiscForms());
		prepare1099MiscForms.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getPrepare1099MISCAction().run(null, true);

			}
		});

		DynamicForm form = new DynamicForm();

		if (this instanceof InvoiceListView) {
			form.setNumCols(8);

			form.setItems(viewSelect, dateRangeSelector, fromItem, toItem);
			hlay.add(form);
			hlay.add(updateButton);
			hlay.setCellHorizontalAlignment(form, ALIGN_RIGHT);
			hlay.setCellHorizontalAlignment(updateButton,
					HasHorizontalAlignment.ALIGN_RIGHT);

		} else if (this instanceof VendorListView
				&& getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			form.setFields(viewSelect);
			hlay.add(prepare1099MiscForms);
			hlay.add(form);
		} else {

			if (!(this instanceof JournalEntryListView))
				form.setFields(viewSelect);
			hlay.add(form);
			hlay.setCellHorizontalAlignment(form, ALIGN_RIGHT);
		}
		// hlay.add(form);
		// hlay.setCellHorizontalAlignment(form, ALIGN_RIGHT);
		VerticalPanel vlayTop = new VerticalPanel();
		HorizontalPanel hlayTop = new HorizontalPanel();
		hlayTop.setWidth("100%");
		if (this instanceof InvoiceListView) {
			vlayTop.add(addNewLabel);
			vlayTop.add(hlayTop);
		} else {
			hlayTop.add(addNewLabel);
			if (getAddNewLabelString().length() != 0) {
				hlayTop.setCellWidth(addNewLabel, getAddNewLabelString()
						.length() + "px");
			}
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
			if (this instanceof InvoiceListView) {
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
			if (this instanceof InvoiceListView) {
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

		add(mainVLay);

		setSize("100%", "100%");

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

	public void addToGrid(T objectToBeAdded) {
		grid.addData(objectToBeAdded);
		if (totalLabel != null) {
			updateTotal(objectToBeAdded, true);
		}

	}

	protected void updateTotal(T t, boolean add) {

	}

	private Label createAddNewLabel() {

		Label addNewLabel = new Label(getAddNewLabelString());
		addNewLabel.setStyleName("handCursor");

		addNewLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Action action = getAddNewAction();
				if (action != null) {
					action.run(null, false);
				}
			}

		});

		return addNewLabel;
	}

	protected abstract Action getAddNewAction();

	protected abstract String getAddNewLabelString();

	public void initListCallback() {
		grid.removeAllRecords();
		grid.addLoadingImagePanel();
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
			Accounter.showError(Accounter.constants().failedRequest());
			return;
		}
		Accounter.showMessage(Accounter.constants().sessionExpired());
	}

	@Override
	public void onSuccess(ArrayList<T> result) {
		grid.removeLoadingImage();
		if (result != null) {
			initialRecords = result;
			this.records = result;

			if (this instanceof BudgetListView) {

				List<String> typeList = new ArrayList<String>();
				for (ClientBudget budget : (List<ClientBudget>) result) {
					typeList.add(budget.getBudgetName());
				}
				if (typeList.size() < 1) {
					typeList.add(Accounter.constants().emptyValue());
				} 
				viewSelect.initCombo(typeList);
				viewSelect.setSelectedItem(0);

				if(result.size()>1){
				ClientBudget budget = (ClientBudget) result.get(0);
				List<ClientBudgetItem> budgetItems = new ArrayList<ClientBudgetItem>();
				budgetItems = budget.getBudgetItem();
				grid.setRecords(budgetItems);
				}else{
					List<ClientBudgetItem> budgetItems = new ArrayList<ClientBudgetItem>();
					grid.setRecords(budgetItems);
				}

				
			} else {
				grid.setRecords(result);
			}

			if (this instanceof CustomerListView
					|| this instanceof VendorListView
					|| this instanceof BudgetListView) {
				filterList(true);
			}
		} else {
			Accounter.showInformation(AccounterWarningType.RECORDSEMPTY);
			grid.removeLoadingImage();
		}
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
		this.viewSelect.setFocus();
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
		this.viewSelect.setDisabled(true);
	}

}
