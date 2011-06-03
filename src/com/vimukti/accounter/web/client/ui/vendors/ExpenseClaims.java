package com.vimukti.accounter.web.client.ui.vendors;

import java.util.List;

import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author Uday kumar
 *
 */
public class ExpenseClaims extends BaseView<BillsList> {
	DecoratedTabPanel tabset;
	public int selectTab;

	public ExpenseClaims(int selectTab) {
		this.selectTab = selectTab;
	}

	public ExpenseClaims() {

	}

	@Override
	public void init() {
		this.clear();
		super.init();
		createControls();
	}

	public void createControls() {
		tabset = new DecoratedTabPanel();
		AwaitingAuthorisationView awaitingview = new AwaitingAuthorisationView();
		ExpenseClaimView expenseview = new ExpenseClaimView();
		PreviousClaimsView claimsView = new PreviousClaimsView();

		tabset.add(expenseview, "Present Claims");
		tabset.add(claimsView, "Previous Claims");
		if (FinanceApplication.getUser().canApproveExpences())
			tabset.add(awaitingview, "Awaiting Authorisation");
		setSize("100%", "100%");
		mainPanel.add(tabset);
		mainPanel.removeStyleName("main-class-pannel");
		tabset.selectTab(selectTab);
		buttonLayout.setVisible(false);
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

}
