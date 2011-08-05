package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public class BaseHomeView extends ParentCanvas<Object> {
	private VerticalPanel widgetLayout;

	// private FinanceMenuImages images = GWT.create(FinanceMenuImages.class);

	// private boolean wait;
	// private TextItem custNameText;
	// private TextItem emailText;
	// private TextItem contactNoText;

	public BaseHomeView() {
		createView();
	}

	private void createView() {

		HorizontalPanel mainLayout = new HorizontalPanel();
		mainLayout.setSize("100%", "100%");
		// mainLayout.setSize("100%", "100%");
		// mainLayout.setMembersMargin(5);

		// HorizontalPanel firstImagePanel = getFirstImagePanel();
		// HorizontalPanel secondImagePanel = getSecondImagePanel();
		// firstImagePanel.setStyleName(FinanceApplication.constants()
		// .imageAction());
		// secondImagePanel.setStyleName(FinanceApplication
		// .constants().imageAction());
		VerticalPanel imagePanel = new VerticalPanel();
		imagePanel.setStyleName(Accounter.constants().imageActionContainer());
		imagePanel.setSpacing(5);
		// imagePanel.setWidth("100%");
		// imagePanel.add(firstImagePanel);
		// imagePanel.add(secondImagePanel);
		widgetLayout = new VerticalPanel();
		widgetLayout.setStyleName("finance-portlet");
		widgetLayout.setWidth("100%");
		// widgetLayout.add(imagePanel);
		mainLayout.add(widgetLayout);

		// VerticalPanel rightLayout = new VerticalPanel();
		// // rightLayout.setMargin(15);
		// // rightLayout.setWidth("30%");
		// // rightLayout.setHeight("100px");
		//
		// DecoratedTabPanel tabSet = new DecoratedTabPanel();
		// // tabSet.setEdgeMarginSize(10);
		// // tabSet.setTabBarPosition(Side.TOP);
		// // tabSet.setTop(15);
		// // tabSet.setWidth("100%");
		// // tabSet.setHeight("100%");
		//
		// tabSet.add(getAddTab(), FinanceApplication.constants()
		// .add());
		// tabSet.add(getFindTab(), FinanceApplication.constants()
		// .find());
		// tabSet.selectTab(0);
		// rightLayout.add(tabSet);
		//
		// mainLayout.add(rightLayout);

		add(mainLayout);
	}

	public VerticalPanel getLeftLayout() {
		return widgetLayout;
	}

	//
	// @Override
	// public void init() {
	//
	// }
	//
	// @Override
	// public void initData() {
	//
	// }
	//
	// private ClientCustomer getCustomerObject() {
	//
	// customer = new ClientCustomer();
	//
	// customer.setName((String) custNameText.getValue());
	// customer.setFileAs((String) custNameText.getValue());
	//
	// Set<ClientEmail> emailSet = new HashSet<ClientEmail>();
	// ClientEmail email = new ClientEmail();
	// email.setEmail((String) emailText.getValue());
	// emailSet.add(email);
	//
	// customer.setEmails(emailSet);
	//
	// Set<ClientPhone> phoneNumbers = new HashSet<ClientPhone>();
	// ClientPhone clientPhone = new ClientPhone();
	// clientPhone.setNumber((String) contactNoText.getValue());
	// phoneNumbers.add(clientPhone);
	// customer.setPhoneNumbers(phoneNumbers);
	//
	// for (ClientTaxGroup clientTaxGroup : FinanceApplication.getCompany()
	// .getTaxGroups()) {
	// if (clientTaxGroup.getName().equals("None")) {
	// customer.setTaxGroup(clientTaxGroup.getID());
	// }
	// }
	//
	// return customer;
	//
	// }
	//
	// @Override
	// public void saveAndUpdateView() throws Exception {
	//
	// if (!wait) {
	// try {
	// ClientCustomer customer = getCustomerObject();
	// if (Utility.isObjectExist(FinanceApplication.getCompany()
	// .getCustomers(), customer.getName())) {
	// throw new InvalidEntryException(
	// AccounterErrorType.ALREADYEXIST);
	// }
	// createObject(customer);
	// } catch (Exception e) {
	// e.printStackTrace();
	// throw e;
	// }
	// }
	//
	// }

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
	}

	@Override
	public void fitToSize(int height, int width) {
		this.setHeight(height + "px");

	}


	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}


	@Override
	public void init(ViewManager manager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}
}
