//package com.vimukti.accounter.web.client;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.google.gwt.user.client.rpc.IsSerializable;
//import com.vimukti.accounter.core.Account;
//import com.vimukti.accounter.core.CashPurchase;
//import com.vimukti.accounter.core.CashSales;
//import com.vimukti.accounter.core.Company;
//import com.vimukti.accounter.core.CreditCardCharge;
//import com.vimukti.accounter.core.CreditRating;
//import com.vimukti.accounter.core.Currency;
//import com.vimukti.accounter.core.Customer;
//import com.vimukti.accounter.core.CustomerCreditMemo;
//import com.vimukti.accounter.core.CustomerGroup;
//import com.vimukti.accounter.core.CustomerRefund;
//import com.vimukti.accounter.core.EnterBill;
//import com.vimukti.accounter.core.Estimate;
//import com.vimukti.accounter.core.Invoice;
//import com.vimukti.accounter.core.IssuePayment;
//import com.vimukti.accounter.core.Item;
//import com.vimukti.accounter.core.ItemGroup;
//import com.vimukti.accounter.core.ItemTax;
//import com.vimukti.accounter.core.MakeDeposit;
//import com.vimukti.accounter.core.PayBill;
//import com.vimukti.accounter.core.PaymentMethod;
//import com.vimukti.accounter.core.PaymentTerms;
//import com.vimukti.accounter.core.PriceLevel;
//import com.vimukti.accounter.core.PurchaseOrder;
//import com.vimukti.accounter.core.ReceivePayment;
//import com.vimukti.accounter.core.SalesOrder;
//import com.vimukti.accounter.core.SalesPerson;
//import com.vimukti.accounter.core.ShippingMethod;
//import com.vimukti.accounter.core.ShippingTerms;
//import com.vimukti.accounter.core.TaxAgency;
//import com.vimukti.accounter.core.TaxCode;
//import com.vimukti.accounter.core.TaxGroup;
//import com.vimukti.accounter.core.TaxRates;
//import com.vimukti.accounter.core.Transaction;
//import com.vimukti.accounter.core.TransferFund;
//import com.vimukti.accounter.core.UnitOfMeasure;
//import com.vimukti.accounter.core.User;
//import com.vimukti.accounter.core.Vendor;
//import com.vimukti.accounter.core.VendorCreditMemo;
//import com.vimukti.accounter.core.VendorGroup;
//import com.vimukti.accounter.core.WriteCheck;
//import com.vimukti.accounter.web.client.core.reports.TrialBalance;
//
///**
// * This Class is Used as PROXY to Handle all ACCOUNTER-GET Requests, for Cache
// * Purposes
// * 
// * @author Fernandez
// * 
// */
//public class AccounterProxyService implements IsSerializable {
//
////	private IAccounterGETServiceAsync rpcService;
////
////	private Company company;
////
////	private List<Company> workingCompanies = new ArrayList<Company>();
////
////	private static AccounterProxyService instance;
////
////	// private Map<Long, CompanyDataCache> dataMap = new HashMap<Long,
////	// CompanyDataCache>();
////
////	private static CompanyDataCache cache;
////
////	private AccounterProxyService(IAccounterGETServiceAsync rpcService,
////			Company company) {
////
////		this.rpcService = rpcService;
////
////		cache = new CompanyDataCache(this);
////
////		this.company = company;
////
////		initService();
////
////	}
////
////	public static AccounterProxyService startService(
////			IAccounterGETServiceAsync rpcService, Company company) {
////
////		if (instance == null) {
////
////			instance = new AccounterProxyService(rpcService, company);
////
////		}
////		return instance;
////
////	}
////
////	private void initService() {
////
////		// TODO may Not be Required
////	}
////
////	final class CompanyDataCache {
////
////		public CompanyDataCache(AccounterProxyService service) {
////
////		}
////
////		List<Customer> customers = null;
////
////		List<Account> accounts = null;
////
////		List<Vendor> vendors = null;
////
////		List<PaymentTerms> paymentTerms = null;
////
////		PaymentTerms paymentTerm = null;
////
////		List<ShippingTerms> shippingTerms = null;
////
////		List<ShippingMethod> shippingMethods = null;
////
////		public List<Item> items;
////
////		public List<CreditRating> creditRatings;
////
////		public List<Currency> currencies;
////
////		public List<CustomerGroup> customerGroups;
////
////		public List<TaxGroup> taxGroups;
////
////		public List<ItemGroup> itemGroups;
////
////		public List<ItemTax> itemTaxes;
////
////		public List<PaymentMethod> paymentMethods;
////
////		public List<PriceLevel> priceLevels;
////
////		public List<TaxRates> taxRates;
////
////		public List<SalesPerson> salesPersons;
////
////		public List<TaxAgency> taxAgencies;
////
////		public List<VendorGroup> vendorGroups;
////
////	}
////
////	public void getAccount(Company company, String accountName,
////			AsyncCallback<Account> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getAccount(Company company, Long accountId,
////			AsyncCallback<Account> callback, boolean force) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getAccounts(Company company,
////			final AsyncCallback<List<Account>> callback) {
////
////		try {
////
////			if (cache.accounts == null) {
////
////				rpcService.getAccounts(company,
////						new AsyncCallback<List<Account>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								if (callback != null)
////									callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<Account> arg0) {
////								cache.accounts = arg0;
////								if (callback != null)
////									callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.accounts);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getCashPurchase(Company company, Long cashPurchaseId,
////			AsyncCallback<CashPurchase> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getCashSales(Company company, Long cashSalesId,
////			AsyncCallback<CashSales> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	/**
////	 * Not TO Call this method, Use GET Service Directly
////	 * 
////	 * @param user
////	 * @param callback
////	 */
////	public void getCompanies(User user, AsyncCallback<List<Company>> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getCompany(User user, String companyName,
////			AsyncCallback<Company> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getCompany(User user, Long companyId,
////			AsyncCallback<Company> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getCreditCardCharge(Company company, Long creditCardChargeId,
////			AsyncCallback<CreditCardCharge> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getCreditRating(Company company, String creditRatingName,
////			AsyncCallback<CreditRating> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getCreditRating(Company company, Long creditRatingId,
////			AsyncCallback<CreditRating> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getCreditRatings(Company company,
////			final AsyncCallback<List<CreditRating>> callback) {
////
////		try {
////
////			if (cache.creditRatings == null) {
////
////				rpcService.getCreditRatings(company,
////						new AsyncCallback<List<CreditRating>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<CreditRating> arg0) {
////								cache.creditRatings = arg0;
////								callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.creditRatings);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getCurrencies(Company company,
////			final AsyncCallback<List<Currency>> callback) {
////
////		try {
////
////			if (cache.currencies == null) {
////
////				rpcService.getCurrencies(company,
////						new AsyncCallback<List<Currency>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<Currency> arg0) {
////								cache.currencies = arg0;
////								callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.currencies);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getCurrency(Company company, String currencyName,
////			AsyncCallback<Currency> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getCurrency(Company company, Long currencyId,
////			AsyncCallback<Currency> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getCustomer(Company company, String customerName,
////			AsyncCallback<Customer> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getCustomer(Company company, Long customerId,
////			AsyncCallback<Customer> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getCustomerCreditMemo(Company company,
////			Long customerCreditMemoId,
////			AsyncCallback<CustomerCreditMemo> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getCustomerGroup(Company company, String customerGroupName,
////			AsyncCallback<CustomerGroup> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getCustomerGroup(Company company, Long customerGroupId,
////			AsyncCallback<CustomerGroup> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getCustomerGroups(Company company,
////			final AsyncCallback<List<CustomerGroup>> callback) {
////
////		try {
////
////			if (cache.customerGroups == null) {
////
////				rpcService.getCustomerGroups(company,
////						new AsyncCallback<List<CustomerGroup>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<CustomerGroup> arg0) {
////								cache.customerGroups = arg0;
////								callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.customerGroups);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getCustomerRefunds(Company company, Long customerRefundsId,
////			AsyncCallback<CustomerRefund> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getCustomers(Company company,
////			final AsyncCallback<List<Customer>> callback) {
////
////		try {
////
////			if (cache.customers == null) {
////
////				rpcService.getCustomers(company,
////						new AsyncCallback<List<Customer>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								if (callback != null)
////									callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<Customer> arg0) {
////								cache.customers = arg0;
////								if (callback != null)
////									callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.customers);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getEnterBill(Company company, Long enterBillId,
////			AsyncCallback<EnterBill> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getEstimate(Company company, Long estimateId,
////			AsyncCallback<Estimate> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getGroups(Company company,
////			final AsyncCallback<List<TaxGroup>> callback) {
////		try {
////
////			if (cache.taxGroups == null) {
////
////				rpcService.getGroups(company,
////						new AsyncCallback<List<TaxGroup>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								if (callback != null)
////									callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<TaxGroup> arg0) {
////								cache.taxGroups = arg0;
////								if (callback != null)
////									callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.taxGroups);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getInvoice(Company company, Long invoiceId,
////			AsyncCallback<Invoice> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getIssuePayment(Company company, Long issuePaymentId,
////			AsyncCallback<IssuePayment> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getItem(Company company, String itemName,
////			AsyncCallback<Item> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getItem(Company company, Long itemId,
////			AsyncCallback<Item> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getItemGroup(Company company, String itemGroupName,
////			AsyncCallback<ItemGroup> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getItemGroup(Company company, Long itemGroupId,
////			AsyncCallback<ItemGroup> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getItemGroups(Company company,
////			final AsyncCallback<List<ItemGroup>> callback) {
////		try {
////
////			if (cache.itemGroups == null) {
////
////				rpcService.getItemGroups(company,
////						new AsyncCallback<List<ItemGroup>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<ItemGroup> arg0) {
////								cache.itemGroups = arg0;
////								callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.itemGroups);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getItemTax(Company company, String itemTaxName,
////			AsyncCallback<ItemTax> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getItemTax(Company company, Long itemTaxId,
////			AsyncCallback<ItemTax> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getItemTaxes(Company company,
////			final AsyncCallback<List<ItemTax>> callback) {
////
////		try {
////
////			if (cache.itemTaxes == null) {
////
////				rpcService.getItemTaxes(company,
////						new AsyncCallback<List<ItemTax>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<ItemTax> arg0) {
////								cache.itemTaxes = arg0;
////								callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.itemTaxes);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getItems(Company company,
////			final AsyncCallback<List<Item>> callback) {
////
////		try {
////
////			if (cache.items == null) {
////
////				rpcService.getItems(company, new AsyncCallback<List<Item>>() {
////
////					@Override
////					public void onFailure(Throwable arg0) {
////						callback.onFailure(arg0);
////
////					}
////
////					@Override
////					public void onSuccess(List<Item> arg0) {
////						cache.items = arg0;
////						callback.onSuccess(arg0);
////					}
////
////				});
////
////			} else
////				callback.onSuccess(cache.items);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getMakeDeposit(Company company, Long makeDepositId,
////			AsyncCallback<MakeDeposit> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getPayBill(Company company, Long payBillId,
////			AsyncCallback<PayBill> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getPaymentMethod(Company company, int type,
////			AsyncCallback<PaymentMethod> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getPaymentMethod(Company company, Long paymentMethodId,
////			AsyncCallback<PaymentMethod> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getPaymentMethod(Company company, String paymentMethodName,
////			AsyncCallback<PaymentMethod> callBack) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getPaymentMethods(Company company,
////			final AsyncCallback<List<PaymentMethod>> callback) {
////
////		try {
////
////			if (cache.paymentMethods == null) {
////
////				rpcService.getPaymentMethods(company,
////						new AsyncCallback<List<PaymentMethod>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<PaymentMethod> arg0) {
////								cache.paymentMethods = arg0;
////								callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.paymentMethods);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getPaymentTerms(Company company, String paymentsTermsName,
////			final AsyncCallback<PaymentTerms> callback) {
////		// TODO
////
////	}
////
////	public void getPaymentTerms(Company company, Long paymentsTermsId,
////			AsyncCallback<PaymentTerms> callback) {
////
////	}
////
////	public void getPaymentTerms(Company company,
////			final AsyncCallback<List<PaymentTerms>> callback) {
////		try {
////
////			if (cache.paymentTerms == null) {
////
////				rpcService.getPaymentTerms(company,
////						new AsyncCallback<List<PaymentTerms>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<PaymentTerms> arg0) {
////								cache.paymentTerms = arg0;
////								callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.paymentTerms);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getPriceLevel(Company company, String priceLevelName,
////			AsyncCallback<PriceLevel> callback) {
////
////		// TODO
////
////	}
////
////	public void getPriceLevel(Company company, Long priceLevelId,
////			AsyncCallback<PriceLevel> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getPriceLevels(Company company,
////			final AsyncCallback<List<PriceLevel>> callback) {
////		try {
////
////			if (cache.priceLevels == null) {
////
////				rpcService.getPriceLevels(company,
////						new AsyncCallback<List<PriceLevel>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<PriceLevel> arg0) {
////								cache.priceLevels = arg0;
////								callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.priceLevels);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getPurchaseOrder(Company company, Long purchaseOrderId,
////			AsyncCallback<PurchaseOrder> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getRates(Company company,
////			final AsyncCallback<List<TaxRates>> callback) {
////
////		try {
////
////			if (cache.taxRates == null) {
////
////				rpcService.getRates(company,
////						new AsyncCallback<List<TaxRates>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<TaxRates> arg0) {
////								cache.taxRates = arg0;
////								callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.taxRates);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getReceivePayment(Company company, Long receivePaymentId,
////			AsyncCallback<ReceivePayment> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getRegister(Account account, AsyncCallback<Transaction> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getSalesOrder(Company company, Long salesOrderId,
////			AsyncCallback<SalesOrder> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getSalesPerson(Company company, String salesPersonName,
////			AsyncCallback<SalesPerson> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getSalesPerson(Company company, Long salesPersonId,
////			AsyncCallback<SalesPerson> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getSalesPersons(Company company,
////			final AsyncCallback<List<SalesPerson>> callback) {
////		try {
////
////			if (cache.salesPersons == null) {
////
////				rpcService.getSalesPersons(company,
////						new AsyncCallback<List<SalesPerson>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<SalesPerson> arg0) {
////								cache.salesPersons = arg0;
////								callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.salesPersons);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getShippingMethod(Company company, String shippingMethodName,
////			AsyncCallback<ShippingMethod> callback) {
////
////	}
////
////	public void getShippingMethod(Company company, Long shippingMethodId,
////			AsyncCallback<ShippingMethod> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getShippingMethods(Company company,
////			final AsyncCallback<List<ShippingMethod>> callback) {
////
////		try {
////
////			if (cache.shippingMethods == null) {
////
////				rpcService.getShippingMethods(company,
////						new AsyncCallback<List<ShippingMethod>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<ShippingMethod> arg0) {
////								cache.shippingMethods = arg0;
////								callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.shippingMethods);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getShippingTerms(Company company, String shippingTermsName,
////			AsyncCallback<ShippingTerms> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getShippingTerms(Company company, Long shippingTermsId,
////			AsyncCallback<ShippingTerms> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getShippingTerms(Company company,
////			final AsyncCallback<List<ShippingTerms>> callback) {
////
////		try {
////
////			if (cache.shippingTerms == null) {
////
////				rpcService.getShippingTerms(company,
////						new AsyncCallback<List<ShippingTerms>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<ShippingTerms> arg0) {
////								cache.shippingTerms = arg0;
////								callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.shippingTerms);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getTaxAgencies(Company company,
////			final AsyncCallback<List<TaxAgency>> callback) {
////
////		try {
////
////			if (cache.taxAgencies == null) {
////
////				rpcService.getTaxAgencies(company,
////						new AsyncCallback<List<TaxAgency>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<TaxAgency> arg0) {
////								cache.taxAgencies = arg0;
////								callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.taxAgencies);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getTaxAgency(Company company, String taxAgencyName,
////			AsyncCallback<TaxAgency> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getTaxAgency(Company company, Long taxAgencyID,
////			AsyncCallback<TaxAgency> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getTaxCode(Company company, String taxCodeName,
////			AsyncCallback<TaxCode> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getTaxCode(Company company, Long taxCodeID,
////			AsyncCallback<TaxCode> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getTaxCodes(Company company,
////			AsyncCallback<List<TaxCode>> callback) {
////
////	}
////
////	public void getTaxGroup(Company company, String taxGroupName,
////			AsyncCallback<TaxGroup> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getTaxGroup(Company company, Long taxGroupID,
////			AsyncCallback<TaxGroup> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getTaxRates(Company company, Double rate,
////			AsyncCallback<TaxRates> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getTaxRates(Company company, Long taxRateID,
////			AsyncCallback<TaxRates> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getTransferFund(Company company, Long transferFundId,
////			AsyncCallback<TransferFund> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getTrialBalance(Company company, String startDate,
////			String endDate, AsyncCallback<List<TrialBalance>> callBack) {
////
////	}
////
////	public void getUnitOfMeasure(Company company, String unitOfMeausreName,
////			AsyncCallback<UnitOfMeasure> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getUnitOfMeasure(Company company, Long unitOfMeausreId,
////			AsyncCallback<UnitOfMeasure> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getUnitOfMeasures(Company company,
////			AsyncCallback<List<UnitOfMeasure>> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getUser(long userID, AsyncCallback<User> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getUser(String email, AsyncCallback<User> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getUsers(Company company, AsyncCallback<List<User>> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getVendor(Company company, String vendorName,
////			AsyncCallback<Vendor> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getVendor(Company company, Long vendorId,
////			AsyncCallback<Vendor> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getVendorCreditMemo(Company company, Long vendorrCreditMemoId,
////			AsyncCallback<VendorCreditMemo> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getVendorGroup(Company company, String vendorGroupName,
////			AsyncCallback<VendorGroup> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getVendorGroup(Company company, Long vendorGroupId,
////			AsyncCallback<VendorGroup> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void getVendorGroups(Company company,
////			final AsyncCallback<List<VendorGroup>> callback) {
////
////		try {
////
////			if (cache.vendorGroups == null) {
////
////				rpcService.getVendorGroups(company,
////						new AsyncCallback<List<VendorGroup>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<VendorGroup> arg0) {
////								cache.vendorGroups = arg0;
////								callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.vendorGroups);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getVendors(Company company,
////			final AsyncCallback<List<Vendor>> callback) {
////
////		try {
////
////			if (cache.vendors == null) {
////
////				rpcService.getVendors(company,
////						new AsyncCallback<List<Vendor>>() {
////
////							@Override
////							public void onFailure(Throwable arg0) {
////								callback.onFailure(arg0);
////
////							}
////
////							@Override
////							public void onSuccess(List<Vendor> arg0) {
////								cache.vendors = arg0;
////								callback.onSuccess(arg0);
////							}
////
////						});
////
////			} else
////				callback.onSuccess(cache.vendors);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////			callback.onFailure(e);
////		}
////
////	}
////
////	public void getwriterCheck(Company company, Long writeCheckId,
////			AsyncCallback<WriteCheck> callback) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteAccount(Account account, AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteCompany(Company company, AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteCreditRating(CreditRating creditRating,
////			AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteCustomer(Customer customer,
////			AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteCustomerGroup(CustomerGroup customerGroup,
////			AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteItem(Item item, AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteItemGroup(ItemGroup itemGroup,
////			AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteItemTax(ItemTax itemTax, AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeletePaymentMethod(PaymentMethod paymentMethod,
////			AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeletePaymentTerms(PaymentTerms paymentTerms,
////			AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeletePriceLevel(PriceLevel priceLevel,
////			AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteSalesPerson(SalesPerson salesPerson,
////			AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteShippingMethod(ShippingMethod shippingMethod,
////			AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteShippingTerms(ShippingTerms shippingTerms,
////			AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteTaxAgency(TaxAgency taxAgency,
////			AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteTaxCode(TaxCode taxCode, AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteTaxGroup(TaxGroup taxGroup,
////			AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteTaxRates(TaxRates taxRates,
////			AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteUnitOfMeasure(UnitOfMeasure unitOfMeasure,
////			AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteUser(User user, AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteVendor(Vendor vendor, AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void canDeleteVendorGroup(VendorGroup vendorGroup,
////			AsyncCallback<Boolean> result) {
////		// TODO Auto-generated method stub
////
////	}
////
////	public void clearCache() {
////		cache = new CompanyDataCache(this);
////	}
//
//}
