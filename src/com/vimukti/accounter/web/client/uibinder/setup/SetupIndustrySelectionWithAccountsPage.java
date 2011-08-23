/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccountsTemplate;
import com.vimukti.accounter.web.client.core.TemplateAccount;

/**
 * @author Prasanna Kumar G
 * 
 */
public class SetupIndustrySelectionWithAccountsPage extends AbstractSetupPage {
	private static SetupIndustrySelectionWithAccountsPageUiBinder uiBinder = GWT
			.create(SetupIndustrySelectionWithAccountsPageUiBinder.class);
	@UiField
	VerticalPanel viewPanel;
	@UiField
	HorizontalPanel industryAccountsPanel;
	@UiField
	ListBox industryList;
	@UiField
	VerticalPanel accountsPanel;
	@UiField
	HTML customizeAccounter, settingsLater, industry;
	@UiField
	HTML selectIndustry;
	@UiField
	Label headerLabel;
	@UiField
	HTML accountsLabel;

	private SetupWizard setupWizard;
	private Map<Integer, List<TemplateAccount>> industriesWithAccounts = new HashMap<Integer, List<TemplateAccount>>();
	private int selectedIndustry = -1;

	interface SetupIndustrySelectionWithAccountsPageUiBinder extends
			UiBinder<Widget, SetupIndustrySelectionWithAccountsPage> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 * 
	 * @param setupWizard
	 */
	public SetupIndustrySelectionWithAccountsPage(SetupWizard setupWizard) {
		initWidget(uiBinder.createAndBindUi(this));
		this.setupWizard = setupWizard;
		createControls();
	}

	@Override
	protected void onLoad() {
		if (selectedIndustry == -1) {
			Map<Integer, AccountsTemplate> allIndustiesWithAccounts = setupWizard
					.getAllIndustiesWithAccounts();
			for (AccountsTemplate template : allIndustiesWithAccounts.values()) {
				int industyType = template.getType();
				List<TemplateAccount> selectedAccounts = new ArrayList<TemplateAccount>();
				for (TemplateAccount account : template.getAccounts()) {
					if (account.getDefaultValue()) {
						selectedAccounts.add(account);
					}
				}
				industriesWithAccounts.put(industyType, selectedAccounts);
				industryList.addItem(template.getName());
			}
		}

	}

	@Override
	protected void onSave() {
		setupWizard.setSelectedAccountsList(industriesWithAccounts
				.get(selectIndustry));
	}

	@Override
	protected void createControls() {
		headerLabel.setText(accounterConstants.selectYourIndustry());

		customizeAccounter.setHTML(accounterMessages.selectIndustryInfoHTML());
		settingsLater.setHTML(accounterMessages.industrySelectNote());
		selectIndustry.setHTML("<b>" + accounterMessages.selectIndustry()
				+ "</b>");
		industryList.setName(accounterConstants.industry());

		industryList.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				int selectedIndex = industryList.getSelectedIndex();
				if (selectedIndex < 0) {
					return;
				}
				selectedIndustry = ++selectedIndex;
				changeAccounts(selectedIndex);
			}
		});
		industryList.setVisibleItemCount(15);

		accountsLabel.setHTML("<b"
				+ accounterMessages.accounts(Global.get().Account()) + "</b>");

		// accountsList
		// .setName(accounterMessages.accounts(Global.get().Account()));

	}

	/**
	 * @param selectedIndex
	 */
	protected void changeAccounts(int industyType) {
		List<TemplateAccount> accounts = industriesWithAccounts
				.get(industyType);
		accountsPanel.clear();
		if (accounts == null) {
			return;
		}

		Map<String, List<String>> accMap = new HashMap<String, List<String>>();
		for (TemplateAccount acc : accounts) {
			List<String> list = accMap.get(acc.getType());
			if (list == null) {
				list = new ArrayList<String>();
			}
			list.add(acc.getName());
			accMap.put(acc.getType(), list);
		}
		for (String accType : accMap.keySet()) {
			accountsPanel.add(new HTML("<b>" + accType + "</b>"));
			List<String> list = accMap.get(accType);
			for (String accName : list) {
				accountsPanel.add(new HTML(accName));
			}
		}

	}

	@Override
	public boolean canShow() {
		return true;
	}

	@Override
	protected boolean validate() {
		if (industryList.getSelectedIndex() == -1) {
			return false;
		} else {
			return true;
		}
	}

}
