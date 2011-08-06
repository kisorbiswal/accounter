package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.CustomLabel;

public class SetupSellTypeAndSalesTaxPage extends AbstractSetupPage {
	private static final String SELL_TYPES = "Sell Type";
	private static final String SALES_TAX = "Sales Tax";
	private RadioButton serviceOnlyRadioButton, productOnlyRadioButton,
			bothserviceandprductRadioButton, yesRadioButton, noRadioButton;

	public SetupSellTypeAndSalesTaxPage() {
		super();
	}

	@Override
	public String getHeader() {
		return this.accounterConstants.whatDoYouSell();
	}

	@Override
	public VerticalPanel getPageBody() {

		VerticalPanel mainVerticalPanel = new VerticalPanel();

		serviceOnlyRadioButton = new RadioButton(SELL_TYPES,
				accounterConstants.services_labelonly());
		serviceOnlyRadioButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		mainVerticalPanel.add(serviceOnlyRadioButton);
		mainVerticalPanel
				.add(new CustomLabel(accounterConstants.servicesOnly()));
		mainVerticalPanel.add(new CustomLabel(""));

		productOnlyRadioButton = new RadioButton(SELL_TYPES,
				accounterConstants.products_labelonly());
		productOnlyRadioButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		mainVerticalPanel.add(productOnlyRadioButton);
		mainVerticalPanel
				.add(new CustomLabel(accounterConstants.productsOnly()));
		mainVerticalPanel.add(new CustomLabel(""));

		bothserviceandprductRadioButton = new RadioButton(SELL_TYPES,
				accounterConstants.bothservicesandProduct_labelonly());
		bothserviceandprductRadioButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		mainVerticalPanel.add(bothserviceandprductRadioButton);
		mainVerticalPanel.add(new CustomLabel(accounterConstants
				.bothServicesandProducts()));
		mainVerticalPanel.add(new CustomLabel(""));

		mainVerticalPanel.add(new CustomLabel(accounterConstants
				.doyouchargesalestax()));
		yesRadioButton = new RadioButton(SALES_TAX, accounterConstants.yes());
		yesRadioButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		mainVerticalPanel.add(yesRadioButton);

		noRadioButton = new RadioButton(SALES_TAX, accounterConstants.no());
		noRadioButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		mainVerticalPanel.add(noRadioButton);

		return mainVerticalPanel;
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onBack() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onNext() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub
		
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
