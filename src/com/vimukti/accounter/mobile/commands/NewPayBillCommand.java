package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.CreditsAndPayments;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaybillTableRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewPayBillCommand extends NewAbstractTransactionCommand {

	private ArrayList<ClientTransactionPayBill> records;

	@Override
	protected String getWelcomeMessage() {
		return getMessages().create(getConstants().payBill());
	}

	@Override
	protected String getDetailsMessage() {

		return getMessages().readyToCreate(getConstants().payBill());
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().payBill());
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new VendorRequirement(VENDOR, getMessages()
				.pleaseSelectVendor(getConstants().Vendor()), getConstants()
				.vendor(), false, true, new ChangeListner<ClientVendor>() {

			@Override
			public void onSelection(ClientVendor value) {
				records = null;
			}
		}) {

			@Override
			protected String getSetMessage() {
				return "Vender has been selected for Paybill";
			}

			@Override
			protected List<ClientVendor> getLists(Context context) {
				return context.getClientCompany().getVendors();
			}

			@Override
			protected String getEmptyString() {
				return "There are no Vendors";
			}

			@Override
			protected boolean filter(ClientVendor e, String name) {
				return e.getDisplayName().toLowerCase()
						.startsWith(name.toLowerCase());
			}
		});

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().billNo()), getConstants().billNo(), true, true));

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().transactionDate()), getConstants()
				.transactionDate(), true, true));

		// list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
		// .pleaseSelect(getConstants().paymentMethod()), getConstants()
		// .paymentMethod(), false, true, null) {
		//
		// @Override
		// protected String getSetMessage() {
		// // TODO Auto-generated method stub
		// return null;
		// }
		//
		// @Override
		// protected String getSelectString() {
		// // TODO Auto-generated method stub
		// return null;
		// }
		//
		// @Override
		// protected List<String> getLists(Context context) {
		//
		// /*
		// * Map<String, String> paymentMethods =
		// * context.getClientCompany() .getPaymentMethods(); List<String>
		// * paymentMethod = new ArrayList<String>(
		// * paymentMethods.values());
		// */
		// String payVatMethodArray[] = new String[] {
		// getConstants().cash(), getConstants().creditCard(),
		// getConstants().directDebit(),
		// getConstants().masterCard(),
		// getConstants().onlineBanking(),
		// getConstants().standingOrder(),
		// getConstants().switchMaestro() };
		// List<String> wordList = Arrays.asList(payVatMethodArray);
		// return wordList;
		// }
		//
		// @Override
		// protected String getEmptyString() {
		// return "Empty List";
		// }
		// });

		list.add(new AccountRequirement(PAY_FROM, getMessages()
				.pleaseSelectPayFromAccount(getConstants().bankAccount()),
				getConstants().bankAccount(), false, false, null) {

			@Override
			protected String getSetMessage() {
				return "";
			}

			@Override
			protected List<ClientAccount> getLists(Context context) {

				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						if (e.getType() == ClientAccount.TYPE_BANK
								|| e.getType() == ClientAccount.TYPE_OTHER_ASSET) {
							return true;
						}
						return false;
					}
				}, getClientCompany().getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return "No bank acounts available";
			}

			@Override
			protected boolean filter(ClientAccount e, String name) {
				return false;
			}
		});

		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, null) {

			@Override
			protected List<ClientCurrency> getLists(Context context) {
				return context.getClientCompany().getCurrencies();
			}
		});

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

		list.add(new PaybillTableRequirement("BillsDue") {

			@Override
			protected List<ClientTransactionPayBill> getList() {
				return getTransactionPayBills(
						(ClientVendor) NewPayBillCommand.this.get(VENDOR)
								.getValue(), getClientCompany());
			}
		});
	}

	private List<ClientTransactionPayBill> getTransactionPayBills(
			ClientVendor clinetVendor, ClientCompany clientCompany) {
		if (records != null) {
			return records;
		}
		try {
			ArrayList<PayBillTransactionList> billsList = new FinanceTool()
					.getVendorManager().getTransactionPayBills(
							clinetVendor.getID(), clientCompany.getID());

			if (billsList != null) {
				records = new ArrayList<ClientTransactionPayBill>();
				for (PayBillTransactionList curntRec : billsList) {
					ClientTransactionPayBill record = new ClientTransactionPayBill();

					record.setAmountDue(curntRec.getAmountDue());
					record.setDummyDue(curntRec.getAmountDue());

					record.setBillNumber(curntRec.getBillNumber());

					record.setCashDiscount(curntRec.getCashDiscount());

					record.setAppliedCredits(curntRec.getCredits());

					record.setDiscountDate(curntRec.getDiscountDate() != null ? curntRec
							.getDiscountDate().getDate() : 0);

					record.setDueDate(curntRec.getDueDate() != null ? curntRec
							.getDueDate().getDate() : 0);

					record.setOriginalAmount(curntRec.getOriginalAmount());

					// record.setPayment(curntRec.getPayment());
					ClientVendor vendor = clientCompany
							.getVendorByName(curntRec.getVendorName());
					if (vendor != null)
						record.setVendor(vendor.getID());
					records.add(record);
				}
				return records;
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected List<ClientCreditsAndPayments> getVendorCreditsAndPayments(
			long vendorId, long companyId) {
		List<ClientCreditsAndPayments> clientCreditsAndPaymentsList = new ArrayList<ClientCreditsAndPayments>();
		List<CreditsAndPayments> serverCreditsAndPayments = null;
		try {

			serverCreditsAndPayments = new FinanceTool().getVendorManager()
					.getVendorCreditsAndPayments(vendorId, companyId);
			for (CreditsAndPayments creditsAndPayments : serverCreditsAndPayments) {
				clientCreditsAndPaymentsList.add(new ClientConvertUtil()
						.toClientObject(creditsAndPayments,
								ClientCreditsAndPayments.class));
			}
			return clientCreditsAndPaymentsList;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientCreditsAndPayments>(
				clientCreditsAndPaymentsList);

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

}