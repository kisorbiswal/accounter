package com.vimukti.accounter.web.client.externalization;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public interface AccounterMessages extends Messages {

	public SafeHtml addComparativeButton();

	public SafeHtml allHTML();

	public SafeHtml bodyFooter();

	public SafeHtml changePayees(String payee);

	public SafeHtml companyCommentHtml();

	public SafeHtml companySettingsTitle();

	public SafeHtml conversationDateSelectionHTML();

	public SafeHtml conversionBalanaceHeader();

	public SafeHtml conversionCommet();

	public SafeHtml conversionDateButton();

	public SafeHtml conversionHTML();

	public SafeHtml footerComment();

	public SafeHtml generalSettingsHeading();

	public String helpCenter();

	public SafeHtml helpContent();

	public SafeHtml helpHTML();

	public SafeHtml invoiceBrandingHTML();

	public SafeHtml invoiceComment();

	public SafeHtml logoComment1();

	public SafeHtml logoComment2();

	public SafeHtml logoComment3();

	public SafeHtml logoComment4();

	public SafeHtml logoComment5();

	public String logoutHTML();

	public SafeHtml removeHTML();

	public SafeHtml setupComplitionDesc();

	public SafeHtml sureToDelete(String string);

	public SafeHtml undoneHtml();

	public SafeHtml upload();

	public SafeHtml uploadLogo();

	public SafeHtml userHTML();

	public SafeHtml usersComment();

	public String MISC1099TransactionDetailByVendor(String payee);

	public String SelectVendorsToTrack1099(String payee);

	public String accandaccumulatedDepreciationAccShouldnotbesame(
			String accandaccumulatedDepreciationString);

	public String accountNumberToolTipDesc(String starting, String ending);

	public String accountPayable(String account);

	public String accountReceivable(String accountReceivable);

	public String accountRegister(String accRegisterString);

	public String accountToDebitForSale(String msgString);

	public String accountTransactionItems(String account);

	public String accountType(String accounttype);

	public String accounterCategoryList(String accounterCategoryList);

	public String accountsSelected(String account);

	public String accumulatedDepreciationAccount(
			String accumulatedDepreciationAccountString);

	public String actionClassNameis(String className);

	public String addANewVendorPayment(String addANewVendorPaymentString);

	public String addBankAccount(String addBankAccount);

	public String addMore(String name);

	public String addNew(String account);

	public String addaNew(String payeeName);

	public String adjustmentAccount(String accountAString);

	public String allTransactionDetails(String text);

	public String alreadyAccountExist(String alreadyAccountExistString);

	public String amountDueToVendor(String amountDueToVendor);

	public String assetAccountYouHaveSelectedNeedsLinkedAccumulatedDepreciationAccount(
			String msgString);

	public String assignAccounts(String account);

	public String bankAccount(String bankString);

	public String bankAccountInformation(String bankacInfoString);

	public String bankAccountNo(String bankAccNoString);

	public String bankAccountNumber(String bankString);

	public String bankAccountType(String bankAccountString);

	public String bankAccounts(String bankAccountsString);

	public String billstrackingdescription(String accString);

	public String bothCustomerAndVendor(String payeeString, String vendorString);

	public String budgetAddBy();

	public String cannotUsePurchaseItem(String msgString);

	public String cannotUseSalesItem(String msgString);

	public String cashBackAccount(String cashString);

	public String cashFlowCategory(String cashFlowCategoryString);

	public String chartOfAccounts(String chartOfAccountsString);

	public String chartOfAccountsInformation(
			String chartOfAccountsInformationString);

	public String clickThisObjToOpen(String objType, String resultant);

	public String clickThisTo(String text, String viewName);

	public String clickThisToOpen(String next);

	public String clickThisToOpenNew(String viewName);

	public String clickToAddContact(String viewName);

	public String clickToAddItem(String viewName);

	public String contactDetailsHtml();

	public String conversionAccount(String conversionAccounStringt);

	public String create(String name);

	public String createSuccessfully(String cashSale);

	public String updateSuccessfully(String cashSale);

	public String createanyadditionalbankaccounts(String msgString);

	public String creating(String quote);

	public String creditCardAccountInformation(String msgString);

	public String currencyTotal(String string);

	public String objAlreadyExistsWithName(String payee);

	public String objAlreadyExistsWithNameAndNo(String payee);

	public String objAlreadyExistsWithNumber(
			String customerAlreadyExistsWithNumber);

	public String payeeBalance(String payeeBalance);

	public String payeeCredit(String payeeCredit);

	public String customerCreditNote(String payee);

	public String payeeFrom(String cuString);

	public String payeeGroup(String payee);

	public String payeeGroupAlreadyExists(String payee);

	public String payeeGroupList(String payee);

	public String payeeID(String payeeID);

	public String payeeList(String payee);

	public String payeeLists(String payee);

	public String payeeMeaning(String payee);

	public String payeeName(String payee);

	public String payeeNumber(String payee);

	public String payeeNumberShouldBeNumber(String payee);

	public String payeeNumberShouldBePos(String payee);

	public String payeeOrderNo(String payee);

	public String payeeOrderNumber(String payee);

	public String payeePayment(String payee);

	public String payeePrePayment(String prepayment);

	public String customerRefund(String payeeRefund);

	public String customerRefundIssued(String payee);

	public String customerRefunds(String payeeRefunds);

	public String payeeSince(String payeeSince);

	public String payeeStatement(String payeeStatement);

	public String payeeTo(String payeeName);

	public String payeeTransactionHistory(String payeeTransactionHistory);

	public String payees(String payees);

	public String customersAndReceivable(String payeesAndReceivable);

	public String payeesHome(String payeesHome);

	public String depositAccount(String account);

	public String depreciationAccount(String depreciationAccountString);

	public String details(String name);

	public String discountAccount(String discountAccountString);

	public String endFiscalHTML();

	public String errorMsg(String erroMsg);

	public String expenseAccount(String expenseAccountString);

	public String failedToGetRecievePayments(String failedToGetRecievePayments);

	public String failedToLoadCustomerHome(String failedToLoadCustomerHome);

	public String failedToLoadVendorsHome(String msgString);

	public String failedTogetCreditsListAndPayments(String name);

	public String failedTransaction(String transName);

	public String failedtoGetListofAccounts(String msgString, String name);

	public String failedtogetAccountReceivablechartvalues(String msgString);

	public String failedtogetBankaccountchartvalues(String msgString);

	public String finishToCreate(String cashSale);

	public String fiscalStartEndCompreHTML();

	public String fiscalYearAlreadyExists();

	public String fiscalYearEndDateAlreadyExists();

	public String fiscalYearStartDateAlreadyExists();

	public String fromAccount(String fromAccountString);

	public String fullName(String firstName, String lastName);

	public String getCustomersRefundListViewHeading(
			String getCustomersRefundListViewHeading);

	public String getSalesByLocationDetails(String locationString);

	public String getindustryhead();

	public String giveNoTo(String viewName);

	public String giveOf(String fieldName, String transactionName);

	public String giveOpeningBalanceToThis(String viewName);

	public String giveTheNameAccordingToYourID(String transaction);

	public String goToAccountReceivable(String msgString);

	public String goToAccountsPayable(String msgString);

	public String hasSelected(String paymentMethod);

	public String howDoYouReferYourAccounts();

	public String howDoYouReferYourCustoemrs();

	public String howDoYouReferYourVendors();

	public String howIsYourCompanyOrganizedDesc(String msgString);

	public String incomeAccount(String incomeAccountString);

	public String incomeAndExpenseAccounts(String msgString);

	public String industrySelectNote();

	public String invoiceMailMessage(String payee, String number,
			ClientFinanceDate date);

	public String items(String name);

	public String lastActivityMessageForNote(String date, String userName);

	public String lastActivityMessages(String activityType, String userName,
			String date);

	public String location(String location);

	public String locationTracking(String location);

	public String makeDepositCashBackAccount(String accString);

	public String manageCustomerGroup(String manageCustomerGroup);

	public String manageVendorGroup(String manageVendorGroupString);

	public String managingList1();

	public String managingList2();

	public String managingList3();

	public String mergeAccounts(String accountString);

	public String mergeCustomers(String mergeCustomers);

	public String mergeDescription(String desc);

	public String mergeFinancialAccounts(String msgString);

	public String mergeVendors(String payeeString);

	public String mostProfitableCustomer(String mostProfitableCustomer);

	public String mostProfitableCustomers(String mostProfitableCustomers);

	public String msg(String msg);

	public String nameWithCurrency(String name, String formalName);

	public String newBankAccount(String msgString);

	public String newPayee(String newCustomer);

	public String noBillsAreAvailableFirstAddABill(String noBillsString);

	public String noCreditsForThisVendor(String msgString);

	public String noCreditsforthiscustomer(String noCreditsforthiscustomer);

	public String noRecordsToShow();

	public String non1099Vendors(String payee);

	public String notMove(String payee);

	public String notMoveAccount(String account, String accounts);

	public String panNumber(String panNumberString);

	public String paypalEmailHtml();

	public String phoneNumber(String catagory);

	public String pleaseChooseAnAccount(String msgString);

	public String pleaseEnter(String itemName);

	public String pleaseEnterName(String itemName);

	public String pleaseEnterNameOrNumber(String payee);

	public String pleaseEnterThe(String objectName, String name);

	public String pleaseEnterVendorNumberItShouldNotBeEmpty(String payee);

	public String pleaseSelect(String vatReturnBox);

	public String pleaseSelectPayFromAccount(String msgString);

	public String pleaseSelectThePayee(String msgString);

	public String pleaseselectvalidtransactionGrid(String string);

	public String preferredVendor(String msgString);

	public String printTAXReturnLabel1();

	public String printTAXReturnLabel2();

	public String purchaseByVendorDetail(String purchaseByVendorString);

	public String purchaseByVendorSummary(String venSummaryString);

	public String purchaseLiabilityAccount(String msgString);

	public String quarterPeriod(String quarterNo, String startMonth,
			String endMonth);

	public String readyToCreate(String cashSale);

	public String readyToUpdate(String cashSale);

	public String recommendedAccounts();

	public String recommendedAccountsComment();

	public String recommendedNote();

	public String reviewIncomeAndExpensesAccounts(String msgString);

	public String salesByCustomerDetail(String custString);

	public String salesByCustomerSummary(String salesByCustomerSummary);

	public String salesByLocationSummary(String locationString);

	public String salesLiabilityAccount(String msgString);

	public String selectAccountType(String msgString);

	public String selectAccountsToAssign(String account);

	public String selectDateOfBirth(String viewName);

	public String selectDateOfHire(String viewName);

	public String selectDateUntilDue(String transaction);

	public String selectDateWhenTransactioCreated(String transaction);

	public String selectIndustry();

	public String selectIndustryInfoHTML1();

	public String selectIndustryInfoHTML2();

	public String selectIndustryInfoHTML3();

	public String selectIndustryInfoAchor1();

	public String selectIndustryInfoHTML4();

	public String selectIndustryInfoAchor2();

	public String selectPaymentMethod(String msgString);

	public String noteColon();

	public String selectTypeOfThis(String Account);

	public String selectWhichWeHaveInOurCompany(String comboType);

	public String selectWhichWeHaveInOurCompanyOrAddNew(String comboType);

	public String selectedit(String payeePayment);

	public String setupCancelMessgae();

	public String setupVendorsAndAccounts(String payee, String account);

	public String shouldBePositive(String name);

	public String shouldNotbeZero(String amountString);

	public String showAllAccounts(String msgString);

	public String skipSetupInfo();

	public String startFiscalHTML();

	public String startSetupInfo();

	public String taxAccount(String taxString);

	public String taxAgencyFinanceAcount(String msgString);

	public String taxItemWithRate(String name, double taxRate);

	public String terminology(String payee);

	public String theAccountNumberchosenisincorrectPleaschooseaNumberbetween(
			String msgString);

	public String theFinanceCategoryNoShouldBeBetween(String accountString);

	public String theFinanceCategoryNoShouldBeBetween1100And1179(
			String accString);

	public String thereisNoRecordsTosave(String transactionType);

	public String thisIsConsideredACashAccount(String msgString);

	public String to(String string, String string2);

	public String toAccount(String toAccountString);

	public String toAddPayeeGroup(String toAddCustomerGroup);

	public String toTransferFunds(String msgString);

	public String transactionDetailByAccount(String msgString);

	public String transactionDetailsByAccount(String msgString);

	public String unableToGet(String string);

	public String unableToSaveNote(String string);

	public String unusedCredits(String primaryCurrency);

	public String unusedPayments(String primaryCurrency);

	public String useAccountNos(String msgString);

	public String usePayeeId(String useCustomerId);

	public String useTerminologyFor(String location);

	public String userGuidelinesMessage();

	public String userName(String loginUserName);

	public String valueCannotBe0orlessthan0(String amount);

	public String vendor1099(String msgString);

	public String payeeAddress(String payeeAddressString);

	public String vendorBendor(String bendorString);

	public String vendorBill(String billString);

	public String vendorBlah(String msgString);

	public String payeeInformation(String payeeInformationString);

	public String vendorProductNo(String payeeProductNoString);

	public String vendorServiceNo(String msgString);

	public String vendorTDSCode(String payeeTdsCodeString);

	public String vendorWhendor(String payeeWhendorString);

	public String vendorsAndPayables(String payeesString);

	public String vendorsBelowThreshold(String payee);

	public String vendorsSelected(String payee);

	public String venodrsThatMeetThreshold(String payee);

	public String wareHouseLoadingError(String exp);

	public String wecantDeleteThisTheme(String themeName);

	public String whyshoudIUseRecommended();

	public String writeCommentsForThis(String transaction);

	public String writeOffAccount(String writeOffAccountString);

	public String youDontHaveAny(String name);

	public String youcannotchangeaCustomertypetoVendortype(
			String youcannotchangeaCustomertypetoVendortype, String msgString);

	public String payeesList(String payee);

	public String payeePayments(String payee);

	public String payeePaymentList(String payee);

	public String didNotGetRecords(String name);

	public String selectFor(String taxCode, String itemName);

	public String youCantEdit(String recordName);

	public String thereAreNo(String contacts);

	public String all(String contact);

	public String selectToDelete(String account);

	public String setDefault(String accounts);

	public String ProblemWhileCreating(String company);

	public String total(String credit);

	public String selectedAs(String displayName, String customer);

	public String foundRecords(int size, String name);

	public String companyDefaultTaxCode(String tax);

	public String enbleTDSdescription();

	public String amountReceived(String primaryCurrency);

	public String selectedTranslated(String selectedValue);

	public String unableToVoteThisTranslation(String string);

	public String unableToSaveThisTranslation(String string);

	public String amount(String string);

	public String balance(String string);

	public String range(int start, int last);

	public String languageName(String languageTooltip, String languageName);

	public String untranslated();

	public String myTranslations();

	public String all();

	public String pleaseEnterTheWordsAsItis();

	public String unConfirmed();
}
