package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.ui.company.ChartOfAccountsAction;

public class SettingsActionFactory extends AbstractActionFactory {
	public static GeneralSettingsAction getGeneralSettingsAction() {
		return new GeneralSettingsAction("General Settings");
	}

	public static InventoryItemsAction getInventoryItemsAction() {
		return new InventoryItemsAction("Inventory Items");

	}

	public static ChartOfAccountsAction getChartOfAccountsAction() {
		return new ChartOfAccountsAction("Chart of Accounts");

	}

	public static ConversationBalancesAction getConversationBalancesAction() {
		return new ConversationBalancesAction("Conversation Balances");
	}
}
