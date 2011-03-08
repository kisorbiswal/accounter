package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.ui.company.ChartOfAccountsAction;
import com.vimukti.accounter.web.client.ui.core.AbstractActionFactory;
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

	public static ConversionBalancesAction getConversionBalancesAction() {
		return new ConversionBalancesAction("Conversion Balances");
	}

	public static InvoiceBrandingAction getInvoiceBrandingAction() {
		return new InvoiceBrandingAction("Invoice Branding");
	}

	public static NewBrandThemeAction getNewBrandThemeAction() {
		return new NewBrandThemeAction("New Brand Theme");

	}

	public static ConversionDateAction getConversionDateAction() {
		return new ConversionDateAction("Conversion Date");
	}

	public static AutomaticSequenceAction getAutomaticSequenceAction() {
		return new AutomaticSequenceAction("Automatic Sequencing");
	}

	public static CustomThemeAction getCustomThemeAction() {
		return new CustomThemeAction("New Brand Theme");
	}

//	public static UsersAction getUsersAction() {
//		return new UsersAction("Users");
//	}
}
