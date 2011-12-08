package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientStockTransferItem;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class WareHouseTransferTableRequirement extends
		AbstractTableRequirement<ClientStockTransferItem> {
	private static final String ITEM_NAME = "item_name";
	private static final String TOTAL_QUANTITY = "totalQuantity";
	private static final String QUANTITY = "quantity";

	public WareHouseTransferTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, false, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		StringRequirement itemNameReq = new StringRequirement(ITEM_NAME,
				getMessages().pleaseEnter(getMessages().itemName()),
				getMessages().itemName(), true, true);
		itemNameReq.setEditable(false);
		list.add(itemNameReq);

		AmountRequirement totalQuantity = new AmountRequirement(TOTAL_QUANTITY,
				getMessages().pleaseEnter(getMessages().totalQuantity()),
				getMessages().totalQuantity(), true, true);
		totalQuantity.setEditable(false);
		list.add(totalQuantity);

		list.add(new AmountRequirement(QUANTITY, getMessages().pleaseEnter(
				getMessages().quantity()), getMessages().quantity(), true, true) {
			@Override
			public void setValue(Object value) {
				Double amount = (Double) value;
				if (amount != null && DecimalUtil.isEquals(amount, 0)) {
					amount = (double) 1;
				}
				super.setValue(amount);
			}
		});
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny("records");
	}

	@Override
	protected void getRequirementsValues(ClientStockTransferItem obj) {
		double value = get(QUANTITY).getValue();
		if (value == 0) {
			value = 1.0;
		}
		obj.getQuantity().setUnit(
				getCompany().getDefaultMeasurement().getDefaultUnit().getID());
		obj.getQuantity().setValue(value);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientStockTransferItem obj) {
		ClientItem item = (ClientItem) CommandUtils.getClientObjectById(
				obj.getItem(), AccounterCoreType.ITEM, getCompanyId());
		get(ITEM_NAME).setValue(item != null ? item.getName() : " ");
		get(TOTAL_QUANTITY).setValue(obj.getTotalQuantity().getValue());
		get(QUANTITY).setValue(obj.getQuantity().getValue());
	}

	@Override
	protected ClientStockTransferItem getNewObject() {
		return null;
	}

	@Override
	protected Record createFullRecord(ClientStockTransferItem t) {
		Record record = new Record(t);
		ClientItem item = (ClientItem) CommandUtils.getClientObjectById(
				t.getItem(), AccounterCoreType.ITEM, getCompanyId());
		record.add(getMessages().itemName(),
				item == null ? " " : item.getName());
		String totalQty = " ";
		if (item == null) {
			totalQty = String.valueOf(t.getTotalQuantity().getValue());
		} else {
			ClientUnit unit = null;
			StringBuffer data = new StringBuffer();
			if (t.getTotalQuantity() != null) {
				unit = (ClientUnit) CommandUtils.getClientObjectById(t
						.getTotalQuantity().getUnit(), AccounterCoreType.UNIT,
						getCompanyId());
				data.append(String.valueOf(t.getTotalQuantity().getValue()));
				data.append(" ");
			}
			if (unit != null) {
				data.append(unit.getType());
			}
			totalQty = data.toString();
		}
		record.add(getMessages().totalQuantity(), totalQty);
		String quantity = "";
		if (item == null || t.getQuantity() == null) {
			quantity = "";
		} else {
			ClientUnit unit = null;
			StringBuffer data = new StringBuffer();
			if (t.getQuantity() != null) {
				unit = (ClientUnit) CommandUtils.getClientObjectById(t
						.getQuantity().getUnit(), AccounterCoreType.UNIT,
						getCompanyId());
				data.append(String.valueOf(t.getQuantity().getValue()));
				data.append(" ");
			}
			if (unit != null) {
				data.append(unit.getType());
			}
			quantity = data.toString();
		}
		record.add(getMessages().transferQuantity(), quantity);
		return record;
	}

	@Override
	protected Record createRecord(ClientStockTransferItem t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getMessages().items());
	}

	@Override
	protected boolean contains(List<ClientStockTransferItem> oldValues,
			ClientStockTransferItem t) {
		for (ClientStockTransferItem clientStockTransferItem : oldValues) {
			if (clientStockTransferItem.getItem() == t.getItem()) {
				return true;
			}
		}
		return false;
	}

}
