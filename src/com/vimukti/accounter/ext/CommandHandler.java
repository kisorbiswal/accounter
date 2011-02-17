package com.vimukti.accounter.ext;

import org.apache.log4j.Logger;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreditCardCharge;
import com.vimukti.accounter.core.CreditRating;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerCreditMemo;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.FiscalYear;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ItemGroup;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PriceLevel;
import com.vimukti.accounter.core.PurchaseOrder;
import com.vimukti.accounter.core.SalesOrder;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.ShippingTerms;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXGroup;
import com.vimukti.accounter.core.TaxRates;
import com.vimukti.accounter.core.UnitOfMeasure;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.VendorCreditMemo;
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.services.IAccounterClientService;
import com.vimukti.accounter.services.Service;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientCreditRating;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientIssuePayment;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientPaySalesTax;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTaxRates;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.ClientUnitOfMeasure;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;

/**
 * 
 * @author Fernandez
 * 
 */
public class CommandHandler implements ICommandHandler {

	Logger log = Logger.getLogger(this.getClass());

	@Override
	public Object processCommand(Command command) throws CommandException {

		if (command == null) {
			throw new CommandException("Accounter Command Found Null!");

		}

		Object data = command.data;
		long Id = Long.valueOf(command.arg1.equals("") ? "0" : command.arg1);
		if (data == null) {
			if (Id == 0)
				return null;
		}

		int processCommand = command.command;

		IAccounterClientService service = Service.getInstance()
				.getAccounterClientService();

		try {

			switch (processCommand) {

			case CommandDispatcher.COMMAND_CREATE_USER:

				data = service.createUser((ClientUser) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_USER:

				data = service.alterUser(Long.valueOf(command.arg2),
						(ClientUser) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_USER:

				data = service.deleteObject(User.class, Id);
				;

				break;

			case CommandDispatcher.COMMAND_CREATE_COMPANY:

				data = service.createCompany((ClientCompany) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_COMPANY:

				data = service.alterCompany((ClientCompany) command.data);

				break;

			case CommandDispatcher.COMMAND_DELETE_COMPANY:

				data = service.deleteObject(Company.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_ACCOUNT:

				data = service.createAccount(Long.valueOf(command.arg2),
						(ClientAccount) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_ACCOUNT:

				data = service.alterAccount(Long.valueOf(command.arg2),
						(ClientAccount) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_ACCOUNT:

				data = service.deleteObject(Account.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_CASH_PURCHASE:

				data = service.createCashPurchase(Long.valueOf(command.arg2),
						(ClientCashPurchase) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_CASH_PURCHASE:

				data = service.alterCashPurchase(Long.valueOf(command.arg2),
						(ClientCashPurchase) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_CASH_PURCHASE:

				data = service.deleteObject(CashPurchase.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_CASH_SALES:

				data = service.createCashSales(Long.valueOf(command.arg2),
						(ClientCashSales) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_CASH_SALES:

				data = service.alterCashSales(Long.valueOf(command.arg2),
						(ClientCashSales) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_CASH_SALES:

				data = service.deleteObject(CashSales.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_CREDITCARD_CHARGE:

				data = service.createCreditCardCharge(Long
						.valueOf(command.arg2), (ClientCreditCardCharge) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_CREDITCARD_CHARGE:

				data = service.alterCreditCardCharge(
						Long.valueOf(command.arg2),
						(ClientCreditCardCharge) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_CREDITCARD_CHARGE:

				data = service.deleteObject(CreditCardCharge.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_CREDITRATING:

				data = service.createCreditRating(Long.valueOf(command.arg2),
						(ClientCreditRating) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_CREDITRATING:

				data = service.alterCreditRating(Long.valueOf(command.arg2),
						(ClientCreditRating) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_CREDITRATING:

				data = service.deleteObject(CreditRating.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_CURRENCY:

				data = service.createCurrency(Long.valueOf(command.arg2),
						(ClientCurrency) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_CURRENCY:

				data = service.alterCurrency(Long.valueOf(command.arg2),
						(ClientCurrency) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_CURRENCY:

				data = service.deleteObject(Currency.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_CUSTOMER:

				data = service.createCustomer(Long.valueOf(command.arg2),
						(ClientCustomer) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_CUSTOMER:

				data = service.alterCustomer(Long.valueOf(command.arg2),
						(ClientCustomer) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_CUSTOMER:

				data = service.deleteObject(Customer.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_CUSTOMERGROUP:

				data = service.createCustomerGroup(Long.valueOf(command.arg2),
						(ClientCustomerGroup) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_CUSTOMERGROUP:

				data = service.alterCustomerGroup(Long.valueOf(command.arg2),
						(ClientCustomerGroup) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_CUSTOMERGROUP:

				data = service.deleteObject(CustomerGroup.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_CUSTOMER_CREDITMEMO:

				data = service
						.createCustomerCreditMemo(Long.valueOf(command.arg2),
								(ClientCustomerCreditMemo) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_CUSTOMER_CREDITMEMO:

				data = service
						.alterCustomerCreditMemo(Long.valueOf(command.arg2),
								(ClientCustomerCreditMemo) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_CUSTOMER_CREDITMEMO:

				data = service.deleteObject(CustomerCreditMemo.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_CUSTOMER_REFUNDS:

				data = service
						.createCustomerRefunds(Long.valueOf(command.arg2),
								(ClientCustomerRefund) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_CUSTOMER_REFUNDS:

				data = service.alterCustomerRefunds(Long.valueOf(command.arg2),
						(ClientCustomerRefund) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_CUSTOMER_REFUNDS:

				data = service.deleteObject(CustomerRefund.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_ENTERBILL:

				data = service.createEnterBill(Long.valueOf(command.arg2),
						(ClientEnterBill) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_ENTERBILL:

				data = service.alterEnterBill(Long.valueOf(command.arg2),
						(ClientEnterBill) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_ENTERBILL:

				data = service.deleteObject(EnterBill.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_ESTIMATE:

				data = service.createEstimate(Long.valueOf(command.arg2),
						(ClientEstimate) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_ESTIMATE:

				data = service.alterEstimate(Long.valueOf(command.arg2),
						(ClientEstimate) data);

				break;

			case CommandDispatcher.COMMAND_CREATE_ISSUEPAYMENT:

				data = service.createIssuePayment(Long.valueOf(command.arg2),
						(ClientIssuePayment) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_ISSUEPAYMENT:

				data = service.createIssuePayment(Long.valueOf(command.arg2),
						(ClientIssuePayment) data);

				break;

			case CommandDispatcher.COMMAND_CREATE_ITEM:

				data = service.createItem(Long.valueOf(command.arg2),
						(ClientItem) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_ITEM:

				data = service.alterItem(Long.valueOf(command.arg2),
						(ClientItem) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_ITEM:

				data = service.deleteObject(Item.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_ITEMGROUP:

				data = service.createItemGroup(Long.valueOf(command.arg2),
						(ClientItemGroup) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_ITEMGROUP:

				data = service.alterItemGroup(Long.valueOf(command.arg2),
						(ClientItemGroup) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_ITEMGROUP:

				data = service.deleteObject(ItemGroup.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_ITEMTAX:

				// data = service.createItemTax(Long.valueOf(command.arg2),
				// (ClientItemTax) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_ITEMTAX:

				// data = service.alterItemTax(Long.valueOf(command.arg2),
				// (ClientItemTax) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_ITEMTAX:

				// data = service.deleteObject(ItemTax.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_MAKEDEPOSIT:

				data = service.createMakeDeposit(Long.valueOf(command.arg2),
						(ClientMakeDeposit) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_MAKEDEPOSIT:

				data = service.alterMakeDeposit(Long.valueOf(command.arg2),
						(ClientMakeDeposit) data);

				break;

			case CommandDispatcher.COMMAND_CREATE_PAYBILL:

				data = service.createPayBill(Long.valueOf(command.arg2),
						(ClientPayBill) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_PAYBILL:

				data = service.alterPayBill(Long.valueOf(command.arg2),
						(ClientPayBill) data);

				break;

			case CommandDispatcher.COMMAND_CREATE_PAYMENTMETHOD:

				// data =
				// service.createPaymentMethod(Long.valueOf(command.arg2),
				// (ClientPaymentMethod) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_PAYMENTMETHOD:

				// data = service.alterPaymentMethod(Long.valueOf(command.arg2),
				// (ClientPaymentMethod) data);

				break;

			case CommandDispatcher.COMMAND_CREATE_PAYMENTTERMS:

				data = service.createPaymentTerms(Long.valueOf(command.arg2),
						(ClientPaymentTerms) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_PAYMENTTERMS:

				data = service.alterPaymentTerms(Long.valueOf(command.arg2),
						(ClientPaymentTerms) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_PAYMENTTERMS:

				data = service.deleteObject(PaymentTerms.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_PRICELEVEL:

				data = service.createPriceLevel(Long.valueOf(command.arg2),
						(ClientPriceLevel) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_PRICELEVEL:

				data = service.alterPriceLevel(Long.valueOf(command.arg2),
						(ClientPriceLevel) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_PRICELEVEL:

				data = service.deleteObject(PriceLevel.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_PURCHASEORDER:

				data = service.createPurchaseOrder(Long.valueOf(command.arg2),
						(ClientPurchaseOrder) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_PURCHASEORDER:

				data = service.alterPurchaseOrder(Long.valueOf(command.arg2),
						(ClientPurchaseOrder) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_PURCHASEORDER:

				data = service.deleteObject(PurchaseOrder.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_RECIEVEPAYMENT:

				data = service.createReceivePayment(Long.valueOf(command.arg2),
						(ClientReceivePayment) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_RECIEVEPAYMENT:

				data = service.alterReceivePayment(Long.valueOf(command.arg2),
						(ClientReceivePayment) data);

				break;

			case CommandDispatcher.COMMAND_CREATE_SALESORDER:

				data = service.createSalesOrder(Long.valueOf(command.arg2),
						(ClientSalesOrder) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_SALESORDER:

				data = service.alterSalesOrder(Long.valueOf(command.arg2),
						(ClientSalesOrder) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_SALESORDER:

				data = service.deleteObject(SalesOrder.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_SALESPERSON:

				data = service.createSalesPerson(Long.valueOf(command.arg2),
						(ClientSalesPerson) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_SALESPERSON:

				data = service.alterSalesPerson(Long.valueOf(command.arg2),
						(ClientSalesPerson) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_SALESPERSON:

				data = service.deleteObject(SalesPerson.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_SHIPPINGMETHOD:

				data = service.createShippingMethod(Long.valueOf(command.arg2),
						(ClientShippingMethod) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_SHIPPINGMETHOD:

				data = service.alterShippingMethod(Long.valueOf(command.arg2),
						(ClientShippingMethod) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_SHIPPINGMETHOD:

				data = service.deleteObject(ShippingMethod.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_TAXAGENCY:

				data = service.createTaxAgency(Long.valueOf(command.arg2),
						(ClientTAXAgency) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_TAXAGENCY:

				data = service.alterTaxAgency(Long.valueOf(command.arg2),
						(ClientTAXAgency) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_TAXAGENCY:

				data = service.deleteObject(TAXAgency.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_TAXCODE:

				data = service.createTaxCode(Long.valueOf(command.arg2),
						(ClientTAXCode) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_TAXCODE:

				data = service.alterTaxCode(Long.valueOf(command.arg2),
						(ClientTAXCode) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_TAXCODE:

				data = service.deleteObject(TAXCode.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_TAXGROUP:

				data = service.createTaxGroup(Long.valueOf(command.arg2),
						(ClientTAXGroup) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_TAXGROUP:

				data = service.alterTaxGroup(Long.valueOf(command.arg2),
						(ClientTAXGroup) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_TAXGROUP:

				data = service.deleteObject(TAXGroup.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_TAXRATE:

				data = service.createTaxRates(Long.valueOf(command.arg2),
						(ClientTaxRates) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_TAXRATE:

				data = service.alterTaxRates(Long.valueOf(command.arg2),
						(ClientTaxRates) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_TAXRATE:

				data = service.deleteObject(TaxRates.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_TRANSFERFUND:

				data = service.createTransferFund(Long.valueOf(command.arg2),
						(ClientTransferFund) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_TRANSFERFUND:

				data = service.alterTransferFund(Long.valueOf(command.arg2),
						(ClientTransferFund) data);

				break;

			case CommandDispatcher.COMMAND_CREATE_UNIT_OF_MEASURE:

				data = service.createUnitOfMeasure(Long.valueOf(command.arg2),
						(ClientUnitOfMeasure) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_UNIT_OF_MEASURE:

				data = service.alterUnitOfMeasure(Long.valueOf(command.arg2),
						(ClientUnitOfMeasure) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_UNIT_OF_MEASURE:

				data = service.deleteObject(UnitOfMeasure.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_VENDOR:

				data = service.createVendor(Long.valueOf(command.arg2),
						(ClientVendor) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_VENDOR:

				data = service.alterVendor(Long.valueOf(command.arg2),
						(ClientVendor) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_VENDOR:

				data = service.deleteObject(Vendor.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_VENDORCREDIT_MEMO:

				data = service.createVendorCreditMemo(Long
						.valueOf(command.arg2), (ClientVendorCreditMemo) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_VENDORCREDIT_MEMO:

				data = service.alterVendorCreditMemo(
						Long.valueOf(command.arg2),
						(ClientVendorCreditMemo) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_VENDORCREDIT_MEMO:

				data = service.deleteObject(VendorCreditMemo.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_VENDORGROUP:

				data = service.createVendorGroup(Long.valueOf(command.arg2),
						(ClientVendorGroup) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_VENDORGROUP:

				data = service.alterVendorGroup(Long.valueOf(command.arg2),
						(ClientVendorGroup) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_VENDORGROUP:

				data = service.deleteObject(VendorGroup.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_WRITECHECK:

				data = service.createWriteCheck(Long.valueOf(command.arg2),
						(ClientWriteCheck) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_WRITECHECK:

				data = service.alterWriteCheck(Long.valueOf(command.arg2),
						(ClientWriteCheck) data);

				break;

			case CommandDispatcher.COMMAND_CREATE_SHIPPINGTERMS:

				data = service.createShippingTerms(Long.valueOf(command.arg2),
						(ClientShippingTerms) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_SHIPPINGTERMS:

				data = service.alterShippingTerms(Long.valueOf(command.arg2),
						(ClientShippingTerms) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_SHIPPINGTERMS:

				data = service.deleteObject(ShippingTerms.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_JOURNAL:

				data = service.createJournalEntry(Long.valueOf(command.arg2),
						(ClientJournalEntry) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_JOURNAL:

				data = service.alterJournalEntry(Long.valueOf(command.arg2),
						(ClientJournalEntry) data);

				break;

			case CommandDispatcher.COMMAND_CREATE_FISCAL_YEAR:

				data = service.createFiscalYear(Long.valueOf(command.arg2),
						(ClientFiscalYear) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_FISCAL_YEAR:

				data = service.alterFiscalYear(Long.valueOf(command.arg2),
						(ClientFiscalYear) data);

				break;

			case CommandDispatcher.COMMAND_DELETE_FISCAL_YEAR:

				data = service.deleteObject(FiscalYear.class, Id);

				break;

			case CommandDispatcher.COMMAND_CREATE_BANK:

				data = service.createBank(Long.valueOf(command.arg2),
						(ClientBank) data);
				break;

			case CommandDispatcher.COMMAND_ALTER_BANK:

				// data =
				// service.alterBank(Long.valueOf(command.arg2),(ClientBank)
				// data);
				break;

			case CommandDispatcher.COMMAND_DELETE_BANK:

				// data =
				// service.deleteBank(Long.valueOf(command.arg2),(ClientBank)
				// data);
				break;

			case CommandDispatcher.COMMAND_CREATE_PAY_SALES_TAX:

				data = service.createPaySalesTax(Long.valueOf(command.arg2),
						(ClientPaySalesTax) data);

				break;

			case CommandDispatcher.COMMAND_ALTER_PAY_SALES_TAX:

				data = service.alterPaySalesTax(Long.valueOf(command.arg2),
						(ClientPaySalesTax) data);
				break;

			case CommandDispatcher.COMMAND_DELETE_PAY_SALES_TAX:

				break;
			case CommandDispatcher.COMMAND_ALTER_INVOICE:
				break;
			case CommandDispatcher.COMMAND_CREATE_INVOICE:
				data = service.createInvoice(Long.valueOf(command.arg2),
						(ClientInvoice) data);
				break;

			default:
				break;
			}

		} catch (Throwable e) {
			if (e instanceof CommandException)
				throw (CommandException) e;
			e.printStackTrace();
			return null;
		}

		return data;

	}
}
