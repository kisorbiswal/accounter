package com.vimukti.accounter.mobile.commands;

import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.server.FinanceTool;

public class NewCashSaleCommand extends AbstractTransactionCommand {

	private static final String DELIVERY_DATE = "deliveryDate";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(CUSTOMER, false, true));
		list.add(new ObjectListRequirement(ITEMS, false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});
		list.add(new ObjectListRequirement(ACCOUNTS, false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("amount", false, true));
				list.add(new Requirement("discount", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});
		list.add(new Requirement(PAYMENT_METHOD, false, true));
		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement(NUMBER, true, false));
		list.add(new Requirement(CONTACT, true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(MEMO, true, true));
		list.add(new Requirement(DEPOSIT_OR_TRANSFER_TO, false, true));
		list.add(new Requirement(TAXCODE, false, true));
		list.add(new Requirement(SHIPPING_TERMS, true, true));
		list.add(new Requirement(SHIPPING_METHODS, true, true));
		list.add(new Requirement(DELIVERY_DATE, true, true));
	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = context.makeResult();

		setDefaultValues();

		String process = (String) context.getAttribute(PROCESS_ATTR);
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			} else if (process.equals(TRANSACTION_ITEM_PROCESS)) {
				result = transactionItemProcess(context);
				if (result != null) {
					return result;
				}
			} else if (process.equals(TRANSACTION_ACCOUNT_ITEM_PROCESS)) {
				result = transactionAccountProcess(context);
				if (result != null) {
					return result;
				}
			}
		}

		// Preparing Result
		Result makeResult = context.makeResult();
		makeResult.add(getMessages().readyToCreate(getConstants().cashSale()));
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);

		result = customerRequirement(context, list, CUSTOMER, Global.get()
				.Customer());
		if (result != null) {
			return result;
		}

		result = itemsAndAccountsRequirement(context, makeResult, actions,
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						if (account.getType() != ClientAccount.TYPE_CASH
								&& account.getType() != ClientAccount.TYPE_BANK
								&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& account.getType() != ClientAccount.TYPE_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
								&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
								&& account.getType() != ClientAccount.TYPE_EQUITY
								&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY) {
							return true;
						} else {
							return false;
						}
					}
				}, true);
		if (result != null) {
			return result;
		}
		result = paymentMethodRequirement(context, list, PAYMENT_METHOD,
				getConstants().paymentMethod());
		if (result != null) {
			return result;
		}

		result = accountRequirement(context, list, DEPOSIT_OR_TRANSFER_TO,
				getConstants().account(), new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						return Arrays.asList(ClientAccount.TYPE_BANK,
								ClientAccount.TYPE_OTHER_CURRENT_ASSET)
								.contains(account.getType());
					}
				});
		if (result != null) {
			return result;
		}
		makeResult.add(actions);
		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			result = taxCodeRequirement(context, list);
			if (result != null) {
				return result;
			}
		}

		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		return completeProcess(context);
	}

	private void setDefaultValues() {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER).setDefaultValue(getNextTransactionNumber());
		get(PHONE).setDefaultValue("");
		ClientContact contact = new ClientContact();
		contact.setName(null);
		get(CONTACT).setDefaultValue(contact);
		get(MEMO).setDefaultValue("");
		get(PAYMENT_METHOD).setDefaultValue(getConstants().cash());

	}

	private Result completeProcess(Context context) {

		ClientCashSales cashSale = new ClientCashSales();
		ClientFinanceDate date = get(DATE).getValue();
		cashSale.setDate(date.getDate());

		cashSale.setType(Transaction.TYPE_CASH_SALES);
		if (context.getCompany().getPreferences().getUseCustomerId()) {
			String number = get(NUMBER).getValue();
			cashSale.setNumber(number);
		}

		List<ClientTransactionItem> items = get(ITEMS).getValue();
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		accounts.addAll(items);
		cashSale.setTransactionItems(accounts);

		// TODO Location
		// TODO Class
		ClientShippingTerms shippingTerms = get(SHIPPING_TERMS).getValue();
		cashSale.setShippingTerm(shippingTerms != null ? shippingTerms.getID()
				: 0);

		ClientShippingMethod shippingMethod = get(SHIPPING_METHODS).getValue();
		cashSale.setShippingMethod(shippingMethod != null ? shippingMethod
				.getID() : 0);

		ClientCustomer customer = get(CUSTOMER).getValue();
		cashSale.setCustomer(customer.getID());

		ClientContact contact = get(CONTACT).getValue();
		cashSale.setContact(contact);

		cashSale.setShippingAdress(getAddress(ClientAddress.TYPE_SHIP_TO,
				customer));
		cashSale.setShippingAdress(getAddress(ClientAddress.TYPE_BILL_TO,
				customer));

		String phone = get(PHONE).getValue();
		cashSale.setPhone(phone);

		String memo = get(MEMO).getValue();
		cashSale.setMemo(memo);

		String paymentMethod = get(PAYMENT_METHOD).getValue();
		cashSale.setPaymentMethod(paymentMethod);

		ClientAccount account = get(DEPOSIT_OR_TRANSFER_TO).getValue();
		cashSale.setDepositIn(account.getID());

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		cashSale.setDeliverydate(deliveryDate.getDate());

		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		// if (context.getCompany())
		// ClientTAXCode taxCode = get(TAXCODE).getValue();
		// cashSale.setTaxTotal(getTaxTotal(accounts, taxCode));
		updateTotals(cashSale, true);
		create(cashSale, context);

		markDone();

		Result result = new Result();
		result.add(getMessages().createSuccessfully(getConstants().cashSale()));

		return result;
	}

	public ClientAddress getAddress(int type, ClientCustomer customer) {
		for (ClientAddress address : customer.getAddress()) {

			if (address.getType() == type) {
				return address;
			}

		}
		return null;
	}

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				markDone();
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");
		// booleanOptionalRequirement(context, selection, list,
		// AMOUNTS_INCLUDE_TAX,
		// getMessages().active(getConstants().customer()), getMessages()
		// .inActive(getConstants().customer()));
		Result result = dateOptionalRequirement(context, list, DATE,
				getConstants().date(),
				getMessages().pleaseEnter(getConstants().date()), selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, NUMBER,
				getConstants().number(),
				getMessages().pleaseEnter(getConstants().number()));
		if (result != null) {
			return result;
		}

		Requirement customerReq = get(CUSTOMER);
		ClientCustomer customer = customerReq.getValue();
		result = contactRequirement(context, list, selection, customer);
		if (result != null) {
			return result;
		}

		if (context.getCompany().getPreferences().getUseCustomerId()) {
			result = numberOptionalRequirement(context, list, selection, PHONE,
					getConstants().phoneNumber(),
					getMessages().pleaseEnter(getConstants().phoneNumber()));
			if (result != null) {
				return result;
			}
		}

		CompanyPreferences preferences = context.getCompany().getPreferences();

		if (preferences.isDoProductShipMents()) {
			result = shippingMethodRequirement(context, list, selection);
			if (result != null) {
				return result;
			}

			result = shippingTermsRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
		}

		result = dateOptionalRequirement(context, list, DELIVERY_DATE,
				getConstants().deliveryDate(),
				getMessages().pleaseEnter(getConstants().deliveryDate()),
				selection);
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, MEMO,
				getConstants().memo(), getConstants().addMemo());
		if (result != null) {
			return result;
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", getMessages().finishToCreate(getConstants().cashSale()));
		actions.add(finish);

		return makeResult;
	}

	private String getNextTransactionNumber() {
		String nextTransactionNumber = new FinanceTool()
				.getNextTransactionNumber(ClientTransaction.TYPE_CASH_SALES,
						getClientCompany().getID());
		return nextTransactionNumber;
	}

}
