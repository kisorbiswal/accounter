package com.vimukti.accounter.web.client.ui.grids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientBudgetItem;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.company.AddBudgetAmountDialogue;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.forms.ClickableSafeHtmlCell;

public class BudgetCellTable extends CellTable<ClientBudgetItem> {

	ListDataProvider<ClientBudgetItem> listDataProvider = new ListDataProvider<ClientBudgetItem>();

	private final AccounterMessages messages = Accounter.messages();

	int rowSelected;

	public BudgetCellTable() {
		createControls();
		this.addStyleName("budget-table");
	}

	private void createControls() {

		setPageSize(50);
		listDataProvider.addDataDisplay(this);
		List<ClientBudgetItem> list = listDataProvider.getList();

		List<ClientAccount> listOfExpenseAccounts = Accounter.getCompany()
				.getAccounts();

		List<ClientAccount> listOfAccounts = new ArrayList<ClientAccount>(
				listOfExpenseAccounts);

		for (ClientAccount account : listOfAccounts) {

			ClientBudgetItem obj = new ClientBudgetItem();
			obj.setAccountsName(account.getName());
			obj.setAccount(account);

			list.add(obj);

		}

		this.setWidth("100%", true);
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		initTableColumns();
	}

	private void initTableColumns() {

		Column<ClientBudgetItem, SafeHtml> accountLinkColumn = new Column<ClientBudgetItem, SafeHtml>(
				new ClickableSafeHtmlCell()) {

			@Override
			public SafeHtml getValue(ClientBudgetItem object) {
				SafeHtmlBuilder shb = new SafeHtmlBuilder();

				shb.appendHtmlConstant("<u>");
				shb.appendEscaped(object.getAccountsName());
				shb.appendHtmlConstant("</u>");
				return shb.toSafeHtml();
			}
		};
		accountLinkColumn
				.setFieldUpdater(new FieldUpdater<ClientBudgetItem, SafeHtml>() {

					@Override
					public void update(int index,
							final ClientBudgetItem object, SafeHtml safeHtml) {
						rowSelected = index;

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
					}
				});

		Column<ClientBudgetItem, String> janMonthColumn = new Column<ClientBudgetItem, String>(
				new TextCell()) {

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
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getFebruaryAmount());

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
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getMarchAmount());

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
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getAprilAmount());

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
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getMayAmount());

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
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJuneAmount());

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
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getJulyAmount());

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
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getAugustAmount());

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
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getSpetemberAmount());

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
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getOctoberAmount());

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
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getNovemberAmount());

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
				new TextCell()) {

			@Override
			public String getValue(ClientBudgetItem object) {
				return Double.toString(object.getDecemberAmount());

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

		this.setColumnWidth(accountLinkColumn, "200px");
		this.setColumnWidth(janMonthColumn, "53px");
		this.setColumnWidth(febMonthColumn, "53px");
		this.setColumnWidth(marMonthColumn, "53px");
		this.setColumnWidth(aprMonthColumn, "53px");
		this.setColumnWidth(mayMonthColumn, "53px");
		this.setColumnWidth(juneMonthColumn, "53px");
		this.setColumnWidth(julMonthColumn, "53px");
		this.setColumnWidth(augMonthCoulmn, "53px");
		this.setColumnWidth(septMonthColumn, "53px");
		this.setColumnWidth(octMonthColumn, "53px");
		this.setColumnWidth(novMonthColumn, "53px");
		this.setColumnWidth(decMonthColumn, "53px");

		this.addColumn(accountLinkColumn, messages.Account());
		this.addColumn(janMonthColumn, messages.jan());
		this.addColumn(febMonthColumn, messages.feb());
		this.addColumn(marMonthColumn, messages.mar());
		this.addColumn(aprMonthColumn, messages.apr());
		this.addColumn(mayMonthColumn, messages.may());
		this.addColumn(juneMonthColumn, messages.jun());
		this.addColumn(julMonthColumn, messages.jul());
		this.addColumn(augMonthCoulmn, messages.aug());
		this.addColumn(septMonthColumn, messages.sept());
		this.addColumn(octMonthColumn, messages.oct());
		this.addColumn(novMonthColumn, messages.nov());
		this.addColumn(decMonthColumn, messages.dec());

	}

	public void sortRowData(int columnIndex, boolean isAscending) {
		redraw();
	}

	/*
	 * private void openLinkAction(final ClientBudgetItem object) {
	 * HashMap<String, String> map = new HashMap<String, String>(); String
	 * budgetTitle = "Add Budget for " + object.getAccountsName();
	 * AddBudgetAmountDialogue assignAccountsTo1099Dialog = new
	 * AddBudgetAmountDialogue( budgetTitle, "", map, object);
	 * assignAccountsTo1099Dialog .setCallback(new
	 * ActionCallback<HashMap<String, String>>() {
	 * 
	 * @Override public void actionResult(HashMap<String, String> result) {
	 * refreshView(result, object);
	 * 
	 * } }); assignAccountsTo1099Dialog.show(); }
	 */

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

		refreshAllRecords(obj);

	}

	private void refreshAllRecords(ClientBudgetItem obj) {

		List<ClientBudgetItem> list = listDataProvider.getList();
		list.remove(rowSelected);
		list.add(rowSelected, obj);
	}

	public List<ClientBudgetItem> getDataList() {
		List<ClientBudgetItem> list = listDataProvider.getList();
		return list;
	}

	public void setDataProvided(ClientBudget data) {

		List<ClientBudgetItem> list = listDataProvider.getList();

		for (ClientBudgetItem budgetItem : data.getBudgetItem()) {
			int i = 0;
			budgetItem.setAccountsName(budgetItem.getAccount().getName());
			for (ClientBudgetItem item : list) {
				if (item.getAccount().getID() == budgetItem.getAccount()
						.getID()) {
					list.remove(i);
					break;
				}
				i++;
			}
			list.add(budgetItem);
		}

	}
}
