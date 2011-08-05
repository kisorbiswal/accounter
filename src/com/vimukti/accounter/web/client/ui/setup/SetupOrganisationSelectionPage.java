package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.Selectable;
import com.vimukti.accounter.web.client.ui.CustomLabel;

public class SetupOrganisationSelectionPage extends AbstractSetupPage {
	private static final String ORG_TYPE = "OrganizationType";
	private RadioButton soleProprietorshipRadio;
	private RadioButton partnershipRadio;
	private RadioButton llcRadio;
	private RadioButton corporationRadio;
	private RadioButton sCorporationRadio;
	private RadioButton nonProfitRadio;
	private RadioButton otherNoneRadio;
	private ListBox llcFormList;

	@Override
	public String getHeader() {
		return accounterConstants.howIsYourCompanyOrganized();
	}

	@Override
	public VerticalPanel getPageBody() {
		VerticalPanel viewContainer = new VerticalPanel();
		CustomLabel headerDesc = new CustomLabel(
				accounterConstants.howIsYourCompanyOrganizedDesc());

		viewContainer.add(headerDesc);
		soleProprietorshipRadio = new RadioButton(ORG_TYPE,
				accounterConstants.soleProprietorship());
		viewContainer.add(soleProprietorshipRadio);
		viewContainer.add(new CustomLabel(accounterConstants
				.soleProprietorshipDesc()));

		partnershipRadio = new RadioButton(ORG_TYPE,
				accounterConstants.partnershipOrLLP());
		viewContainer.add(partnershipRadio);
		viewContainer.add(new CustomLabel(accounterConstants
				.partnershipOrLLPDesc()));

		llcRadio = new RadioButton(ORG_TYPE, accounterConstants.LLC());
		viewContainer.add(llcRadio);
		viewContainer.add(new CustomLabel(accounterConstants.LLCDesc()));

		llcFormList = new ListBox();
		llcFormList.addItem(accounterConstants.llcSingleMemberForm());
		llcFormList.addItem(accounterConstants.llcMultiMemberForm());
		
		viewContainer.add(llcFormList);
		
		corporationRadio = new RadioButton(ORG_TYPE,
				accounterConstants.corporation());
		viewContainer.add(corporationRadio);
		viewContainer
				.add(new CustomLabel(accounterConstants.corporationDesc()));

		sCorporationRadio = new RadioButton(ORG_TYPE,
				accounterConstants.sCorporation());
		viewContainer.add(sCorporationRadio);
		viewContainer
				.add(new CustomLabel(accounterConstants.sCorporationDesc()));

		nonProfitRadio = new RadioButton(ORG_TYPE,
				accounterConstants.nonProfit());
		viewContainer.add(nonProfitRadio);
		viewContainer.add(new CustomLabel(accounterConstants.nonProfitDesc()));

		otherNoneRadio = new RadioButton(ORG_TYPE,
				accounterConstants.otherNone());
		viewContainer.add(otherNoneRadio);

		return viewContainer;
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

}
