package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.ui.company.ChartOfAccountsAction;
import com.vimukti.accounter.web.client.ui.core.AbstractActionFactory;
import com.vimukti.accounter.web.client.ui.core.GeneralSettingsAction;
import com.vimukti.accounter.web.client.ui.core.InventoryItemsAction;

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

	public static InvoiceBrandingAction getInvoiceBrandingAction() {
		return new InvoiceBrandingAction("Invoice Branding");
	}

	public static NewBrandThemeAction getNewBrandThemeAction() {
		return new NewBrandThemeAction("New Brand Theme");

	}

}
