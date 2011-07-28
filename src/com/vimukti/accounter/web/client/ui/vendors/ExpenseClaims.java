package com.vimukti.accounter.web.client.ui.vendors;

import java.util.List;

import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author Uday kumar
 * 
 */
@SuppressWarnings("deprecation")
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

		tabset.add(expenseview, Accounter.constants().presentClaims());
		tabset.add(claimsView, Accounter.constants().previousClaims());
		if (Accounter.getUser().canApproveExpences())
			tabset.add(awaitingview, Accounter.constants()
					.awaitingAuthorisation());
		setSize("100%", "100%");
		mainPanel.add(tabset);
		mainPanel.removeStyleName("main-class-pannel");
		tabset.selectTab(selectTab);
		tabset.addTabListener(new TabListener() {

			@Override
			public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
				MainFinanceWindow.getViewManager().restoreErrorBox();
			}

			@Override
			public boolean onBeforeTabSelected(SourcesTabEvents sender,
					int tabIndex) {
				return true;
			}
		});
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

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public void printPreview() {
	}

	/**
	 * THIS METHOD DID N'T USED ANY WHERE IN THE PROJECT.
	 */
	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().expenseClaims();
	}

}
