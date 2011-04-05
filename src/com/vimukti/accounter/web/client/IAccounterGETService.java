/**
 * 
 */
package com.vimukti.accounter.web.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.HelpLink;
import com.vimukti.accounter.web.client.core.HrEmployee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.KeyFinancialIndicators;
import com.vimukti.accounter.web.client.data.InvalidSessionException;

/**
 * @author Fernandez
 * 
 */
public interface IAccounterGETService extends RemoteService {

	/**
	 * Core Related
	 * 
	 * @throws InvalidSessionException
	 */

	// public Boolean checkLogin(String email, String password);
	//
	// public ClientAccount getAccount(String accountName);
	//
	// public ClientAccount getAccount(Long accountId);
	//
	// public List<ClientAccount> getAccounts();
	//
	// public ClientCompany getCompany(String companyName);
	//
	// public ClientCompany getCompany(Long companyId);
	//
	// public List<ClientCompany> getCompanies();
	//
	// public ClientCreditRating getCreditRating(String creditRatingName);
	//
	// public ClientCreditRating getCreditRating(Long creditRatingId);
	//
	// public List<ClientCreditRating> getCreditRatings();
	//
	// public ClientCurrency getCurrency(String currencyName);
	//
	// public ClientCurrency getCurrency(Long currencyId);
	//
	// public List<ClientCurrency> getCurrencies();
	//
	// public ClientCustomer getCustomer(String customerName);
	//
	// public ClientCustomer getCustomer(Long customerId);
	//
	// public List<ClientCustomer> getCustomers();
	//
	// public ClientCustomerGroup getCustomerGroup(String customerGroupName);
	//
	// public ClientCustomerGroup getCustomerGroup(Long customerGroupId);
	//
	// public List<ClientCustomerGroup> getCustomerGroups();
	//
	// public ClientItem getItem(String itemName);
	//
	// public ClientItem getItem(Long itemId);
	//
	// public List<ClientItem> getItems();
	//
	// public ClientItemGroup getItemGroup(String itemGroupName);
	//
	// public ClientItemGroup getItemGroup(Long itemGroupId);
	//
	// public List<ClientItemGroup> getItemGroups();
	//
	// public ClientPaymentTerms getPaymentTerms(String paymentsTermsName);
	//
	// public ClientPaymentTerms getPaymentTerms(Long paymentsTermsId);
	//
	// public List<ClientPaymentTerms> getPaymentTerms();
	//
	// public ClientPriceLevel getPriceLevel(String priceLevelName);
	//
	// public ClientPriceLevel getPriceLevel(Long priceLevelId);
	//
	// public List<ClientPriceLevel> getPriceLevels();
	//
	// public ClientSalesPerson getSalesPerson(String salesPersonName);
	//
	// public ClientSalesPerson getSalesPerson(Long salesPersonId);
	//
	// public List<ClientSalesPerson> getSalesPersons();
	//
	// public ClientShippingMethod getShippingMethod(String shippingMethodName);
	//
	// public ClientShippingMethod getShippingMethod(Long shippingMethodId);
	//
	// public List<ClientShippingMethod> getShippingMethods();
	//
	// public ClientShippingTerms getShippingTerms(String shippingTermsName);
	//
	// public ClientShippingTerms getShippingTerms(Long shippingTermsId);
	//
	// public List<ClientShippingTerms> getShippingTerms();
	//
	// public ClientTaxAgency getTaxAgency(String taxAgencyName);
	//
	// public ClientTaxAgency getTaxAgency(Long taxAgencyID);
	//
	// public List<ClientTaxAgency> getTaxAgencies();
	//
	// public ClientTaxCode getTaxCode(String taxCodeName);
	//
	// public ClientTaxCode getTaxCode(Long taxCodeID);
	//
	// public List<ClientTaxCode> getTaxCodes();
	//
	// public ClientTaxGroup getTaxGroup(String taxGroupName);
	//
	// public ClientTaxGroup getTaxGroup(Long taxGroupID);
	//
	// public List<ClientTaxGroup> getGroups();
	//
	// public ClientTaxRates getTaxRates(double rate);
	//
	// public ClientTaxRates getTaxRates(Long taxRateID);
	//
	// public List<ClientTaxRates> getRates();
	//
	// public ClientUnitOfMeasure getUnitOfMeasure(String unitOfMeausreName);
	//
	// public ClientUnitOfMeasure getUnitOfMeasure(Long unitOfMeausreId);
	//
	// public List<ClientUnitOfMeasure> getUnitOfMeasures();
	//
	// public ClientUser getUser(long userID);
	//
	// public ClientUser getUser(String email);
	//
	// public List<ClientUser> getUsers();
	//
	// public ClientVendor getVendor(String vendorName);
	//
	// public ClientVendor getVendor(Long vendorId);
	//
	// public List<ClientVendor> getVendors();
	//
	// public ClientVendorGroup getVendorGroup(String vendorGroupName);
	//
	// public ClientVendorGroup getVendorGroup(Long vendorGroupId);
	//
	// public List<ClientVendorGroup> getVendorGroups();
	//
	// /**
	// * Transaction Related
	// */
	//
	// public ClientCashPurchase getCashPurchase(Long cashPurchaseId);
	//
	// public ClientCashSales getCashSales(Long cashSalesId);
	//
	// public ClientCreditCardCharge getCreditCardCharge(Long
	// creditCardChargeId);
	//
	// public ClientCustomerCreditMemo getCustomerCreditMemo(
	// Long customerCreditMemoId);
	//
	// public ClientCustomerRefund getCustomerRefunds(Long customerRefundsId);
	//
	// public ClientEnterBill getEnterBill(Long enterBillId);
	//
	// public ClientEstimate getEstimate(Long estimateId);
	//
	// public ClientInvoice getInvoice(Long invoiceId);
	//
	// public ClientIssuePayment getIssuePayment(Long issuePaymentId);
	//
	// public ClientMakeDeposit getMakeDeposit(Long makeDepositId);
	//
	// public ClientPayBill getPayBill(Long payBillId);
	//
	// public ClientPurchaseOrder getPurchaseOrder(Long purchaseOrderId);
	//
	// public ClientReceivePayment getReceivePayment(Long receivePaymentId);
	//
	// public ClientSalesOrder getSalesOrder(Long salesOrderId);
	//
	// public ClientTransferFund getTransferFund(Long transferFundId);
	//
	// public ClientVendorCreditMemo getVendorCreditMemo(Long
	// vendorrCreditMemoId);
	//
	// public ClientWriteCheck getwriterCheck(Long writeCheckId);
	//
	// public List<ClientTransaction> getRegister(Long accountId);
	//
	// public List<ClientBank> getBanks();
	//
	// public List<ClientCreditsAndPayments> getCreditsAndPayments();
	//
	// public ClientCreditsAndPayments getCreditAndPayment(String memo);
	//
	// public ClientCreditsAndPayments getCreditAndPayment(Long id);
	//
	// public List<ClientPayee> getPayee();
	//
	// /**
	// * Can Delete Objects
	// */
	//
	// public Boolean canDeleteAccount(Long accountId);
	//
	// public Boolean canDeleteCompany();
	//
	// public Boolean canDeleteCreditRating(Long creditRatingId);
	//
	// public Boolean caDate dueDate;nDeleteCustomer(Long customerId);
	//
	// public Boolean canDeleteCustomerGroup(Long customerGroupId);
	//
	// public Boolean canDeleteItem(Long itemId);
	//
	// public Boolean canDeleteItemGroup(Long itemGroupId);
	//
	// public Boolean canDeletePaymentTerms(Long paymentTermsId);
	//
	// public Boolean canDeletePriceLevel(Long priceLevelId);
	//
	// public Boolean canDeleteSalesPerson(Long salesPersonId);
	//
	// public Boolean canDeleteShippingMethod(Long shippingMethodId);
	//
	// public Boolean canDeleteShippingTerms(Long shippingTermsId);
	//
	// public Boolean canDeleteTaxAgency(Long taxAgencyId);
	//
	// public Boolean canDeleteTaxCode(Long taxCodeId);
	//
	// public Boolean canDeleteTaxGroup(Long taxGroupId);
	//
	// public Boolean canDeleteTaxRates(Long taxRatesId);
	//
	// public Boolean canDeleteUnitOfMeasure(Long unitOfMeasureId);
	//
	// public Boolean canDeleteUser(Long userId);
	//
	// public Boolean canDeleteVendor(Long vendorId);
	//
	// public Boolean canDeleteVendorGroup(Long vendorGroupId);
	//
	// public List<ClientFiscalYear> getFiscalYears();
	//
	// public ClientFiscalYear getFiscalYear(Long yearId);
	//
	// public List<ClientPaySalesTaxEntries>
	// getTransactionPaySalesTaxEntriesList(
	// Date transactionDate);
	//
	// public ClientPaySalesTax getPaySalesTax(Long id);

	public <T extends IAccounterCore> T getObjectById(AccounterCoreType type,
			String stringID) throws InvalidSessionException;

	public <T extends IAccounterCore> T getObjectByName(AccounterCoreType type,
			String name) throws InvalidSessionException;

	public <T extends IAccounterCore> List<T> getObjects(AccounterCoreType type);

	public ClientCompany getCompany() throws InvalidSessionException;

	String getStringID();

	KeyFinancialIndicators getKeyFinancialIndicators();

	List<HrEmployee> getHREmployees() throws InvalidSessionException;

	public List<HelpLink> getHelpLinks(int type) throws InvalidSessionException;

}