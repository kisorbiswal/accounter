package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Warehouse;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.StockAdjustmentItemTableRequirement;
import com.vimukti.accounter.mobile.requirements.WarehouseRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientStockAdjustment;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.settings.StockAdjustmentList;
import com.vimukti.accounter.web.server.FinanceTool;

public class CreateStockAdjustmentCommand extends AbstractCommand {
	private static final String WAREHOUSE = "warehouse";
	private static final String STOCK_ADJUSTMENT = "stockadjustment";
	ClientStockAdjustment stockAdjustment;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (isUpdate) {
			String string = context.getString();
			if (string.isEmpty()) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().wareHouseTransfer()));
				return "warehouseTransferList";
			}
			stockAdjustment = getStockAdustMent(string);
			if (stockAdjustment == null) {
				addFirstMessage(
						context,
						getMessages().selectATransactionToUpdate(
								getMessages().wareHouseTransfer()));
				return "warehouseTransferList " + string;
			}
			setValues();
		} else {
			stockAdjustment = new ClientStockAdjustment();
		}
		return null;
	}

	private ClientStockAdjustment getStockAdustMent(String string) {
		try {
			ArrayList<StockAdjustmentList> stockAdjustments = new FinanceTool()
					.getInventoryManager().getStockAdjustments(getCompanyId());
			for (StockAdjustmentList stockAdjustmentList : stockAdjustments) {
				if (stockAdjustmentList.getStockAdjustment() == Long
						.valueOf(string)) {
					return (ClientStockAdjustment) CommandUtils
							.getClientObjectById(
									stockAdjustmentList.getStockAdjustment(),
									AccounterCoreType.STOCK_ADJUSTMENT,
									getCompanyId());
				}
			}
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setValues() {
		Warehouse warehouse = (Warehouse) CommandUtils.getServerObjectById(
				stockAdjustment.getWareHouse(), AccounterCoreType.WAREHOUSE);
		get(WAREHOUSE).setValue(warehouse);
		List<ClientTransactionItem> stockAdjustmentItems = stockAdjustment
				.getTransactionItems();
		get(STOCK_ADJUSTMENT).setValue(stockAdjustmentItems);
	}

	@Override
	protected String getWelcomeMessage() {
		return stockAdjustment.getID() == 0 ? "Creating stock adjustment"
				: "Updating stock adjustment";
	}

	@Override
	protected String getDetailsMessage() {
		return stockAdjustment.getID() == 0 ? getMessages().readyToCreate(
				getMessages().stockAdjustment()) : getMessages().readyToUpdate(
				getMessages().stockAdjustment());
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return stockAdjustment.getID() == 0 ? getMessages().createSuccessfully(
				getMessages().stockAdjustment()) : getMessages()
				.updateSuccessfully(getMessages().stockAdjustment());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new WarehouseRequirement(WAREHOUSE, getMessages()
				.pleaseSelect(getMessages().wareHouse()), getMessages()
				.wareHouse(), false, true, null));
		list.add(new StockAdjustmentItemTableRequirement(STOCK_ADJUSTMENT,
				getMessages().pleaseSelect(getMessages().stockAdjustments()),
				getMessages().stockAdjustments()) {

			@Override
			protected Warehouse getWareHouse() {
				return CreateStockAdjustmentCommand.this.get(WAREHOUSE)
						.getValue();
			}

		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Result onCompleteProcess(Context context) {
		stockAdjustment.setTransactionItems((List<ClientTransactionItem>) get(
				STOCK_ADJUSTMENT).getValue());
		stockAdjustment.setWareHouse(((Warehouse) get(WAREHOUSE).getValue())
				.getID());
		create(stockAdjustment, context);
		return super.onCompleteProcess(context);
	}

}
