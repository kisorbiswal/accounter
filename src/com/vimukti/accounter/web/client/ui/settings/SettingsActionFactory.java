package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.core.client.GWT;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.company.ChartOfAccountsAction;
import com.vimukti.accounter.web.client.ui.core.AbstractActionFactory;
import com.vimukti.accounter.web.client.ui.core.InventoryItemsAction;

public class SettingsActionFactory extends AbstractActionFactory {
	private static SettingsMessages messages = GWT
			.create(SettingsMessages.class);

	public static GeneralSettingsAction getGeneralSettingsAction() {
		return new GeneralSettingsAction(messages.labelTitle());
	}

	public static InventoryItemsAction getInventoryItemsAction() {
		return new InventoryItemsAction(FinanceApplication
				.getSettingsMessages().inventoryItems());

	}

	public static ChartOfAccountsAction getChartOfAccountsAction() {
		return new ChartOfAccountsAction(FinanceApplication
				.getSettingsMessages().chartOfAccounts());

	}

	public static ConversionBalancesAction getConversionBalancesAction() {
		return new ConversionBalancesAction(FinanceApplication
				.getSettingsMessages().conversionBalance());
	}

	public static InvoiceBrandingAction getInvoiceBrandingAction() {
		return new InvoiceBrandingAction(FinanceApplication
				.getSettingsMessages().invoiceBranding());
	}

	public static NewBrandThemeAction getNewBrandThemeAction() {
		return new NewBrandThemeAction(messages.newBrandThemeLabel());

	}

	public static ConversionDateAction getConversionDateAction() {
		return new ConversionDateAction(FinanceApplication
				.getSettingsMessages().conversionDate());
	}

	public static AutomaticSequenceAction getAutomaticSequenceAction() {
		return new AutomaticSequenceAction(FinanceApplication
				.getSettingsMessages().automaticSequencing());
	}

	public static CustomThemeAction getCustomThemeAction() {
		return new CustomThemeAction(messages.newBrandThemeLabel());
	}

	public static UsersAction getUsersAction() {
		return new UsersAction(messages.users());
	}

	public static InviteUserAction getInviteUserAction() {
		return new InviteUserAction(messages.inviteUser());
	}

	public static DeleteThemeAction getDeleteThemeAction() {
		return new DeleteThemeAction(messages.users());
	}

	public static CopyThemeAction getCopyThemeAction() {
		return new CopyThemeAction(messages.copyTheme());
	}
}
