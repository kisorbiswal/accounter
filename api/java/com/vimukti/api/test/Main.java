package com.vimukti.api.test;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.api.core.APIConstants;
import com.vimukti.api.core.ApiCallback;
import com.vimukti.api.crud.Accounter;
import com.vimukti.api.crud.AccounterService;
import com.vimukti.api.process.RequestPeocesser;

public class Main {
	AccounterService service;

	public Main(AccounterService service) {
		this.service = service;
	}

	public static void main(String[] args) {
		RequestPeocesser.getInstance().start();

		Accounter.login(APIConstants.SERIALIZATION_XML, "cr9trlp0",
				"5n3eioc0tjdy8f35", new ApiCallback<Accounter>() {

					@Override
					public void onSuccess(Accounter obj) {
						AccounterService service = obj.openCompany("Vimukti");
						new Main(service).createCustomer();
					}

					@Override
					public void onFail(String reason) {
						System.out.println(reason);
					}
				});
	}

	protected void createCustomer() {
		// ClientItem item = new ClientItem();
		// item.setName("Computer");
		// service.create(item, new ApiCallback<Long>() {
		//
		// @Override
		// public void onSuccess(Long obj) {
		// System.out.println(obj);
		// }
		//
		// @Override
		// public void onFail(String reason) {
		// System.out.println(reason);
		// }
		// });
		// ClientCustomer customer = new ClientCustomer();
		// customer.setName("Nagaraju");
		// service.create(customer, new ApiCallback<Long>() {
		//
		// @Override
		// public void onSuccess(Long obj) {
		// System.out.println(obj);
		// }
		//
		// @Override
		// public void onFail(String reason) {
		// System.out.println(reason);
		// }
		// });

		// computer
		service.get("item", 1, new ApiCallback<ClientItem>() {

			@Override
			public void onSuccess(ClientItem obj) {
				ClientInvoice invoice = new ClientInvoice();
				invoice.setTransactionDate(new ClientFinanceDate().getDate());
				invoice.setNumber("2");
				invoice.setCustomer(4);
				List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();
				ClientTransactionItem item = new ClientTransactionItem();
				item.setType(ClientTransactionItem.TYPE_ITEM);
				item.setAccountable(obj);
				// item.setLineTotal(100D);
				ClientQuantity quantity = new ClientQuantity();
				quantity.setValue(1);
				item.setQuantity(quantity);
				item.setUnitPrice(10D);
				transactionItems.add(item);
				invoice.setTransactionItems(transactionItems);

				service.create(invoice, new ApiCallback<Long>() {

					@Override
					public void onSuccess(Long obj) {
						System.out.println(obj);
					}

					@Override
					public void onFail(String reason) {
						System.out.println(reason);
					}
				});
			}

			@Override
			public void onFail(String reason) {

			}
		});

		// service.get("customer", 18, new ApiCallback<ClientCustomer>() {
		//
		// @Override
		// public void onSuccess(ClientCustomer obj) {
		// System.out.println(obj.getName());
		// }
		//
		// @Override
		// public void onFail(String reason) {
		//
		// }
		// });

	}
}
