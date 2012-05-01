package com.vimukti.api.test;

import java.util.List;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTransaction;
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

		String apiKey = "***REMOVED***";
		String secretKey = "***REMOVED***";

		Accounter.login(APIConstants.SERIALIZATION_XML, apiKey, "***REMOVED***",
				secretKey, new ApiCallback<Accounter>() {

					@Override
					public void onSuccess(Accounter obj) {
						AccounterService service = obj.openCompany("Nagaraju");
						new Main(service).test();
					}

					@Override
					public void onFail(String reason) {
						System.out.println(reason);
					}
				});
	}

	protected void test() {
		// service.getList(AccounterCoreType.ACCOUNT,
		// new ApiCallback<List<ClientAccount>>() {
		//
		// @Override
		// public void onSuccess(List<ClientAccount> obj) {
		// System.out.println();
		// }
		//
		// @Override
		// public void onFail(String reason) {
		// System.out.println(reason);
		// }
		// }, new String[] { "active", "true" });
		//
		// // 2
		// ClientItem item = new ClientItem();
		// item.setName("nagaraju");
		// item.setISellThisItem(true);
		// item.setIncomeAccount(26);
		// item.setActive(true);
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
		//
		// // 2
		// ClientCustomer customer = new ClientCustomer();
		// customer.setName("nagaraju");
		// service.create(customer, new ApiCallback<Long>() {
		//
		// @Override
		// public void onSuccess(Long obj) {
		// System.out.println(obj);
		//
		// }
		//
		// @Override
		// public void onFail(String reason) {
		// System.out.println(reason);
		// }
		// });
		//
		// service.getList(AccounterCoreType.CURRENCY,
		// new ApiCallback<List<Currency>>() {
		//
		// @Override
		// public void onSuccess(List<Currency> obj) {
		// System.out.println(obj);
		// }
		//
		// @Override
		// public void onFail(String reason) {
		// System.out.println(reason);
		// }
		// });

		ClientInvoice invoice = new ClientInvoice();
		invoice.setTransactionDate(new ClientFinanceDate().getDate());
		invoice.setNumber("1");
		invoice.setCustomer(4);
		invoice.setCurrency(1);

		ClientTransactionItem item = new ClientTransactionItem();
		ClientQuantity quantity = new ClientQuantity();
		quantity.setValue(1);
		item.setQuantity(quantity);
		item.setItem(1);
		invoice.getTransactionItems().add(item);
		//invoice.setSaveStatus(ClientTransaction.STATUS_APPROVE);
		invoice.setDueDate(new ClientFinanceDate().getDate());
		invoice.setDiscountDate(new ClientFinanceDate().getDate());
		invoice.setDeliverydate(new ClientFinanceDate().getDate());
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
}
