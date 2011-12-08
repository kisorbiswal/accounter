package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Warehouse;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.WareHouseTransferTableRequirement;
import com.vimukti.accounter.mobile.requirements.WarehouseRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientStockTransfer;
import com.vimukti.accounter.web.client.core.ClientStockTransferItem;
import com.vimukti.accounter.web.server.FinanceTool;

public class NewWareHouseTransferCommand extends NewAbstractCommand {
	private final static String WAREHOUSE_FROM = "warehousefrom";
	private final static String WAREHOUSE_TO = "warehouseto";
	private final static String COMMENT = "comment";
	private final static String TRANSFERED_ITEMS = "transfereditems";

	ClientStockTransfer stockTransfer;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(context,
						"Select a Warehouse transfer to update.");
				return "WareHouse Transfer List";
			}
			ClientStockTransfer clientWarehouse = CommandUtils
					.getWareHouseTransfer(context.getCompany(), string);
			if (clientWarehouse == null) {
				addFirstMessage(context,
						"Select a Warehouse transfer to update.");
				return "WareHouse Transfer List " + string;
			}
			stockTransfer = clientWarehouse;
			setValues();
		} else {
			stockTransfer = new ClientStockTransfer();
		}
		return null;
	}

	private void setValues() {
		get(WAREHOUSE_FROM).setValue(
				CommandUtils.getServerObjectById(
						stockTransfer.getFromWarehouse(),
						AccounterCoreType.WAREHOUSE));
		get(WAREHOUSE_TO).setValue(
				CommandUtils.getServerObjectById(
						stockTransfer.getToWarehouse(),
						AccounterCoreType.WAREHOUSE));
		get(COMMENT).setValue(stockTransfer.getMemo());
		get(TRANSFERED_ITEMS).setValue(stockTransfer.getStockTransferItems());
	}

	@Override
	protected String getWelcomeMessage() {
		return stockTransfer.getID() == 0 ? "Creating ware house transfer"
				: "Updating stock transfer";
	}

	@Override
	protected String getDetailsMessage() {
		return stockTransfer.getID() == 0 ? getMessages().readyToCreate(
				getMessages().wareHouseTransfer()) : getMessages()
				.readyToUpdate(getMessages().wareHouseTransfer());
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return stockTransfer.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().wareHouseTransfer()) : getMessages()
				.updateSuccessfully(getMessages().wareHouseTransfer());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new WarehouseRequirement(WAREHOUSE_FROM, getMessages()
				.pleaseSelect("from " + getMessages().wareHouse()), "from "
				+ getMessages().wareHouse(), false, true, null) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
				NewWareHouseTransferCommand.this.get(TRANSFERED_ITEMS)
						.setValue(new ArrayList<ClientStockTransferItem>());
			}
		});
		list.add(new WarehouseRequirement(WAREHOUSE_TO, getMessages()
				.pleaseSelect("to " + getMessages().wareHouse()), "to "
				+ getMessages().wareHouse(), false, true, null) {
			@Override
			public void setValue(Object value) {
				if (NewWareHouseTransferCommand.this
						.isFromToWareHousesSame((Warehouse) value)) {
					addFirstMessage(getMessages()
							.pleaseSelectDiffWarehousesToTransfer());
					return;
				}
				addFirstMessage(getMessages().pleaseSelect(
						"to " + getMessages().wareHouse()));
				super.setValue(value);
			}

		});
		list.add(new StringRequirement(COMMENT, getMessages().pleaseEnter(
				getMessages().comment()), getMessages().comment(), true, true));
		list.add(new WareHouseTransferTableRequirement(TRANSFERED_ITEMS,
				getMessages().pleaseSelect(
						getMessages().pleaseSelectAtLeastOneRecord()),
				"Warehouse Transfer Item") {

			@Override
			protected List<ClientStockTransferItem> getList() {
				return NewWareHouseTransferCommand.this.getStockTransferItems();
			}

		});
	}

	protected boolean isFromToWareHousesSame(Warehouse value) {
		Warehouse from = get(WAREHOUSE_FROM).getValue();
		if (from.getID() == value.getID()) {
			return true;
		}
		return false;
	}

	protected List<ClientStockTransferItem> getStockTransferItems() {
		Warehouse fromWarehouse = get(WAREHOUSE_FROM).getValue();
		if (fromWarehouse == null) {
			return new ArrayList<ClientStockTransferItem>();
		}
		return new FinanceTool().getInventoryManager().getStockTransferItems(
				getCompanyId(), fromWarehouse.getID());
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		Warehouse fromWarehouse = get(WAREHOUSE_FROM).getValue();
		stockTransfer.setFromWarehouse(fromWarehouse.getID());
		Warehouse toWarehouse = get(WAREHOUSE_TO).getValue();
		stockTransfer.setToWarehouse(toWarehouse.getID());
		stockTransfer.setMemo((String) get(COMMENT).getValue());
		List<ClientStockTransferItem> stockTransferItems = get(TRANSFERED_ITEMS)
				.getValue();
		stockTransfer.setStockTransferItems(stockTransferItems);
		create(stockTransfer, context);
		return super.onCompleteProcess(context);
	}
}
