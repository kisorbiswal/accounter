/**
 * 
 */
package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.ui.fixedassets.DisposingRegisteredItemAction;
import com.vimukti.accounter.web.client.ui.fixedassets.HistoryListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.NewFixedAssetAction;
import com.vimukti.accounter.web.client.ui.fixedassets.PendingItemsListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.RegisteredItemsListAction;
import com.vimukti.accounter.web.client.ui.fixedassets.SellingRegisteredItemAction;
import com.vimukti.accounter.web.client.ui.fixedassets.SoldDisposedFixedAssetsListAction;

/**
 * @author Murali.A
 * 
 */
public class FixedAssetsActionFactory extends AbstractActionFactory {

	public FixedAssetsActionFactory() {
	}

	public static NewFixedAssetAction getNewFixedAssetAction() {
		return new NewFixedAssetAction(actionsConstants.newFixedAsset());
	}

	public static SellingRegisteredItemAction getSellingRegisteredItemAction() {
		return new SellingRegisteredItemAction(actionsConstants
				.sellingRegisteredItem());
	}

	public static DisposingRegisteredItemAction getDiposingRegisteredItemAction() {
		return new DisposingRegisteredItemAction(actionsConstants
				.disposingRegisteredItem());
	}

	public static PendingItemsListAction getPendingItemsListAction() {
		return new PendingItemsListAction(actionsConstants.pendingItemsList());

	}

	public static Action getRegisteredItemsListAction() {
		return new RegisteredItemsListAction(actionsConstants
				.registeredItemsList());
	}

	public static Action getSoldDisposedListAction() {
		return new SoldDisposedFixedAssetsListAction(actionsConstants
				.soldDisposedItems());
	}

	public static Action getHistoryListAction() {
		return new HistoryListAction(actionsConstants.historyList());
	}

}
