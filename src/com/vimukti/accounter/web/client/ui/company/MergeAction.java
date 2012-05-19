package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.AccountMergeDialog;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ClassMergeDialog;
import com.vimukti.accounter.web.client.ui.CustomerMergeDialog;
import com.vimukti.accounter.web.client.ui.CustomerMergeView;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.ItemMergeDialog;
import com.vimukti.accounter.web.client.ui.LocationMergeDialog;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.VendorMergeDialog;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

public class MergeAction extends Action {

	public static final int TYPE_CUSTOMERS = 1;
	public static final int TYPE_VENDORS = 2;
	public static final int TYPE_ITEMS = 3;
	public static final int TYPE_ACCOUNTS = 4;
	public static final int TYPE_CLASSES = 5;
	public static final int TYPE_LOCATIONS = 6;

	private int type;

	public MergeAction(int type) {
		super();
		this.type = type;
	}

	@Override
	public void run() {
		if (!Accounter.hasPermission(Features.MERGING) && !isCalledFromHistory) {
			Accounter.showSubscriptionWarning();
			return;
		}

		BaseDialog dialog = null;
		switch (type) {
		case TYPE_CUSTOMERS:
			if (!Accounter.isIpadApp()) {
				dialog = new CustomerMergeDialog(messages.mergeCustomers(Global
						.get().customers()),
						messages.payeeMergeDescription(Global.get().customer()));
			} else {

				CustomerMergeView view = new CustomerMergeView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, MergeAction.this);
			}
			break;
		case TYPE_VENDORS:
			if (!Accounter.isIpadApp()) {
				dialog = new VendorMergeDialog(messages.mergeVendors(Global
						.get().vendors()),
						messages.payeeMergeDescription(Global.get().vendor()));
			} else {
				VendorMergeView view = new VendorMergeView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, MergeAction.this);
			}
			break;
		case TYPE_ACCOUNTS:
			if (!Accounter.isIpadApp()) {
				dialog = new AccountMergeDialog(messages.mergeAccounts(),
						messages.mergeDescription());
			} else {
				AccountMergeView view = new AccountMergeView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, MergeAction.this);
			}
			break;
		case TYPE_ITEMS:
			if (!Accounter.isIpadApp()) {
				dialog = new ItemMergeDialog(messages.mergeItems(),
						messages.itemDescription());
			} else {
				ItemMergeView view = new ItemMergeView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, MergeAction.this);
			}
			break;
		case TYPE_CLASSES:
			if (!Accounter.isIpadApp()) {
				dialog = new ClassMergeDialog(messages.mergeClasses(),
						messages.mergeClassDescription());
			} else {
				ClassMergeView view = new ClassMergeView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, MergeAction.this);
			}
			break;
		case TYPE_LOCATIONS:
			if (!Accounter.isIpadApp()) {
				dialog = new LocationMergeDialog(messages.mergeLocations(),
						messages.mergeLocationDescription());
			} else {
				LocationMergeView view = new LocationMergeView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, MergeAction.this);
			}
			break;
		default:
			break;
		}
		if (dialog != null) {
			dialog.show();
		} else {

		}
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		switch (type) {
		case TYPE_CUSTOMERS:
			return "merge_customers";
		case TYPE_VENDORS:
			return "merge_vendor";
		case TYPE_ACCOUNTS:
			return "merge_account";
		case TYPE_ITEMS:
			return "merge_item";
		case TYPE_CLASSES:
			return HistoryTokens.MERGECLASS;
		case TYPE_LOCATIONS:
			return HistoryTokens.MERGELOCATION;
		default:
			break;
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		switch (type) {
		case TYPE_CUSTOMERS:
			return messages.mergeCustomers(Global.get().customers());
		case TYPE_VENDORS:
			return messages.mergeVendors(Global.get().vendors());
		case TYPE_ACCOUNTS:
			return messages.mergeAccounts();
		case TYPE_ITEMS:
			return messages.mergeItems();
		case TYPE_CLASSES:
			return messages.mergeClass();
		case TYPE_LOCATIONS:
			return messages.mergeClass();
		default:
			break;
		}
		return null;
	}

	public static MergeAction customers() {
		return new MergeAction(TYPE_CUSTOMERS);
	}

	public static MergeAction vendors() {
		return new MergeAction(TYPE_VENDORS);
	}

	public static MergeAction accounts() {
		return new MergeAction(TYPE_ACCOUNTS);
	}

	public static MergeAction items() {
		return new MergeAction(TYPE_ITEMS);
	}

	public static MergeAction classes() {
		return new MergeAction(TYPE_CLASSES);
	}

	public static MergeAction locations() {
		return new MergeAction(TYPE_LOCATIONS);
	}

}
