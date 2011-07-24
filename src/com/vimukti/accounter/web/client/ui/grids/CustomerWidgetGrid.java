package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.vimukti.accounter.web.client.core.Lists.DummyDebitor;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class CustomerWidgetGrid extends TreeGrid<DummyDebitor> {

	public CustomerWidgetGrid() {
		super(Accounter.getCustomersMessages().noDebitorstoShow());
	}

	@Override
	protected Object getColumnValue(DummyDebitor customer, int index) {
		switch (index) {
		case 0:
			return customer.getDebitorName();
		case 1:
			return DataUtils.getAmountAsString(customer.getDebitdays_in30()
					+ customer.getDebitdays_incurrent());
		case 2:

			return DataUtils.getAmountAsString(customer.getDebitdays_in60());
		case 3:
			return DataUtils.getAmountAsString(customer.getDebitdays_in90());
		case 4:
			return DataUtils.getAmountAsString(customer.getDebitdays_inolder());
		case 5:
			return DataUtils.getAmountAsString(customer.getDebitdays_in30()
					+ customer.getDebitdays_in60()
					+ customer.getDebitdays_in90()
					+ customer.getDebitdays_inolder()
					+ customer.getDebitdays_incurrent());
		}

		return null;
	}

	protected int getColumnType(int col, int row) {

		if (col != 0)

			return COLUMN_TYPE_DECIMAL_TEXT;
		else
			return COLUMN_TYPE_TEXT;

	}

	@Override
	protected void onClick(DummyDebitor obj, int row, int index) {

	}

	// @Override
	// protected int sort(ClientCustomer obj1, ClientCustomer obj2, int index) {
	// switch (index) {
	// case 0:
	// String name =BizantraClient.getIDentity().getUserEmailID(obj1.fromID);
	// String name1 =BizantraClient.getIDentity().getUserEmailID(obj2.fromID);
	// name.compareTo(name1);
	// case 1:
	// obj1.subject.compareTo(obj2.subject);
	//
	// }
	// return 0;
	// }

	@Override
	protected String[] getColumns() {
		return new String[] { "",
				Accounter.getCustomersMessages().Days3(),
				Accounter.getCustomersMessages().Days2(),
				Accounter.getCustomersMessages().Days1(),
				Accounter.getCustomersMessages().older(),
				Accounter.getCustomersMessages().totalBalance() };
	}

	@Override
	protected int getCellWidth(int index) {
		return -1;
	}

	@Override
	protected void onLoad() {

	}

	@Override
	protected int sort(DummyDebitor obj1, DummyDebitor obj2, int index) {
		switch (index) {
		case 0:
			return obj1.getDebitorName().toLowerCase().compareTo(
					obj2.getDebitorName().toLowerCase());
		case 1:
			return UIUtils.compareDouble((obj1.getDebitdays_in30() + obj1
					.getDebitdays_incurrent()),
					(obj2.getDebitdays_in30() + obj2.getDebitdays_incurrent()));
		case 2:
			return UIUtils.compareDouble(obj1.getDebitdays_in60(), obj2
					.getDebitdays_in60());
		case 3:
			return UIUtils.compareDouble(obj1.getDebitdays_in90(), obj2
					.getDebitdays_in90());
		case 4:
			return UIUtils.compareDouble(obj1.getDebitdays_inolder(), obj2
					.getDebitdays_inolder());
		case 5:
			return UIUtils.compareDouble((obj1.getDebitdays_in30()
					+ obj1.getDebitdays_in60() + obj1.getDebitdays_in90()
					+ obj1.getDebitdays_inolder() + obj1
					.getDebitdays_incurrent()), (obj2.getDebitdays_in30()
					+ obj2.getDebitdays_in60() + obj2.getDebitdays_in90()
					+ obj2.getDebitdays_inolder() + obj2
					.getDebitdays_incurrent()));
		}
		return 0;
	}

	public void addParentOrEdit(int col, int row, String string) {
		if (col == 0) {
			addParent(string, "/images/customers.png");
		} else {
			if (getColumnType(col, row) == COLUMN_TYPE_DECIMAL_TEXT) {
				this.cellFormatter.addStyleName(row, col, "gridDecimalCell");
			}
			this.setText(row, col, string);
		}
	}

	@Override
	public void addParentWithChilds(String name, List<DummyDebitor> childNodes) {
		if (childNodes != null) {
			DummyDebitor parent = new DummyDebitor();
			for (DummyDebitor childs : childNodes) {
				parent.setDebitdays_in30(parent.getDebitdays_in30()
						+ childs.getDebitdays_in30());
				parent.setDebitdays_in60(parent.getDebitdays_in60()
						+ childs.getDebitdays_in60());
				parent.setDebitdays_in90(parent.getDebitdays_in90()
						+ childs.getDebitdays_in90());
				parent.setDebitdays_inolder(parent.getDebitdays_inolder()
						+ childs.getDebitdays_inolder());
				parent.setDebitdays_incurrent(parent.getDebitdays_incurrent()
						+ childs.getDebitdays_incurrent());
			}
			addParentOrEdit(0, 0, name);
			addParentOrEdit(1, 0, DataUtils.getAmountAsString(parent
					.getDebitdays_in30()
					+ parent.getDebitdays_incurrent()));
			addParentOrEdit(2, 0, DataUtils.getAmountAsString(parent
					.getDebitdays_in60()));
			addParentOrEdit(3, 0, DataUtils.getAmountAsString(parent
					.getDebitdays_in90()));
			addParentOrEdit(4, 0, DataUtils.getAmountAsString(parent
					.getDebitdays_inolder()));
			addParentOrEdit(5, 0, DataUtils.getAmountAsString(parent
					.getDebitdays_in30()
					+ parent.getDebitdays_in60()
					+ parent.getDebitdays_in90()
					+ parent.getDebitdays_inolder()
					+ parent.getDebitdays_incurrent()));
			super.addNodes(childNodes);
		} else {
			super.addParentWithChilds(name, null);
		}

	}

}
