package com.vimukti.accounter.encryption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.h2.tools.Server;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.*;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;

public class Encrypter extends Thread {
	private Logger log = Logger.getLogger(Encrypter.class);

	private long companyId;
	private byte[] csk;
	private byte[] d2;
	private byte[] s2;
	private String sessionId;
	private String emailId;
	private static List<Class<?>> classes;

	public static void init() {
		classes = new ArrayList<Class<?>>();
		Logger logger = Logger.getLogger(Encrypter.class);

		// File core = getDirectoryFile();
		// File[] listFiles = core.listFiles();
		Class<?>[] allClasses = getClasses();
		logger.info("Total classes: " + allClasses.length);
		try {
			Class<?> create = CreatableObject.class;

			for (Class<?> cls : allClasses) {
				try {
					if (create.isAssignableFrom(cls)) {
						classes.add(cls);
					} else {
						// System.out.println(string);
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private static Class<?>[] getClasses() {
		Class<?>[] classes = new Class<?>[] { Account.class,
				AccountComparator.class, AccounterClass.class,
				AccounterServerConstants.class, AccounterThreadLocal.class,
				AccountTransaction.class, AccountTransactionByAccount.class,
				AccountTransactionByTaxCode.class,
				AccountTransactionItem.class, Activation.class, Activity.class,
				ActivityType.class, Address.class, AdjustmentReason.class,
				Advertisement.class, Attachment.class, AuditWriter.class,
				AuditWriterImpl.class, Bank.class, BankAccount.class,
				BankRule.class, Box.class, BrandingTheme.class, Budget.class,
				BudgetItem.class, BudgetReportTemplate.class,
				BuildAssembly.class, CashPurchase.class,
				CashSalePdfGeneration.class, CashSales.class,
				ChequeLayout.class, ChequePdfGenerator.class, Client.class,
				ClientConvertUtil.class, ClientPaypalDetails.class,
				ClientSubscription.class, CloneUtil.class, CloneUtil2.class,
				CommodityCode.class, Company.class, CompanyPreferences.class,
				Contact.class, CreatableObject.class, CreditCardCharge.class,
				CreditNotePdfGeneration.class, CreditNotePDFTemplete.class,
				CreditNoteTemplete.class, CreditRating.class,
				CreditsAndPayments.class, CSVReportTemplate.class,
				Currency.class, CurrencyRate.class, Customer.class,
				CustomerCreditMemo.class, CustomerGroup.class,
				CustomerPrePayment.class, CustomerRefund.class,
				CustomerStatementTemplate.class, CustomField.class,
				CustomFieldValue.class, DeleteReason.class, Depreciation.class,
				Developer.class, EmailAccount.class, Employee.class,
				EmployeeCategory.class, EmployeeGroup.class,
				EmployeePayHeadComponent.class, EnterBill.class,
				Estimate.class, ETDSAnnexuresGenerator.class, EU.class,
				Exempted.class, Expense.class, Fax.class, FinanceDate.class,
				FinanceLogger.class, FiscalYear.class, FixedAsset.class,
				FixedAssetHistory.class, FixedAssetNote.class,
				Form16ApdfTemplate.class, Form26QAnnexureGenerator.class,
				Form27EQAnnexureGenerator.class,
				Form27QAnnexureGenerator.class, IAccounterServerCore.class,
				IMActivation.class, IMUser.class, INamedObject.class,
				InventoryAssembly.class, InventoryAssemblyItem.class,
				InventoryPurchase.class, Invoice.class,
				InvoiceExcelTemplete.class, InvoicePdfGeneration.class,
				InvoicePDFTemplete.class, IRASCompanyInfo.class,
				IRASGeneralLedgerLineInfo.class, IRASInformation.class,
				IRASPurchaseLineInfo.class, IRASSupplyLineInfo.class,
				Item.class, ItemGroup.class, ITemplate.class,
				ItemReceipt.class, ItemStatus.class, Job.class,
				JournalEntry.class, Location.class, LongUseType.class,
				MaintananceInfoUser.class, MakeDeposit.class,
				Measurement.class, MessageOrTask.class,
				Misc1099PDFTemplate.class, Misc1099SamplePDFTemplate.class,
				MISCInformationTemplate.class, MobileCookie.class, News.class,
				NominalCodeRange.class, NumberUtils.class,
				ObjectConvertUtil.class, PayBill.class, Payee.class,
				PayEmployee.class, PayExpense.class, PayHead.class,
				PayHeadField.class, PaymentTerms.class, PayrollUnit.class,
				PayRun.class, PayStructure.class, PayStructureItem.class,
				PayTAX.class, PayTAXEntries.class, PayTDS.class, Phone.class,
				PortletConfiguration.class, PortletPageConfiguration.class,
				PriceLevel.class, PrintTemplete.class, PurchaseOrder.class,
				PurchaseOrderPdfGeneration.class, Quantity.class,
				QuotePdfGeneration.class, QuotePdfTemplate.class,
				ReceivePayment.class, ReceivePaymentPdfGeneration.class,
				ReceiveVAT.class, ReceiveVATEntries.class,
				Reconciliation.class, ReconciliationItem.class,
				RecurringTransaction.class, ReffereredObject.class,
				RememberMeKey.class, Reminder.class, ReportsGenerator.class,
				ReportTemplate.class, ResetPasswordToken.class,
				SalesOrder.class, SalesOrderPdfGeneration.class,
				SalesPerson.class, Server.class, ServerConvertUtil.class,
				ServerMaintanance.class, ShippingMethod.class,
				ShippingTerms.class, Statement.class, StatementRecord.class,
				StockAdjustment.class, StockTransfer.class,
				StockTransferItem.class, Subscription.class,
				SupportedUser.class, TAXAdjustment.class, TAXAgency.class,
				TAXCode.class, TAXGroup.class, TAXItem.class,
				TAXItemGroup.class, TAXRateCalculation.class, TaxRates.class,
				TAXReturn.class, TAXReturnEntry.class, TDSChalanDetail.class,
				TDSCoveringLetterTemplate.class, TDSDeductorMasters.class,
				TDSInfo.class, TDSResponsiblePerson.class,
				TDSTransactionItem.class, TemplateBuilder.class,
				Transaction.class, TransactionCreditsAndPayments.class,
				TransactionDepositItem.class, TransactionExpense.class,
				TransactionItem.class, TransactionLog.class,
				TransactionMakeDepositEntries.class, TransactionPayBill.class,
				TransactionPayExpense.class, TransactionPayTAX.class,
				TransactionReceivePayment.class, TransactionReceiveVAT.class,
				TransferFund.class, Unit.class, UnitOfMeasure.class,
				User.class, UserPermissions.class, UserPreferences.class,
				UserUtils.class, Util.class, Utility_R.class, Utility.class,
				VATReturnBox.class, Vendor.class, VendorCreditMemo.class,
				VendorGroup.class, Warehouse.class, WareHouseAllocation.class,
				WriteCheck.class };
		return classes;
	}

	public Encrypter(long companyId, String password, byte[] d2,
			String emailId, String sessionId) throws Exception {
		this.companyId = companyId;
		csk = EU.generatePBS(password);
		this.d2 = d2;
		this.emailId = emailId;
		this.sessionId = sessionId;
		s2 = EU.getKey(sessionId);
	}

	@Override
	public void run() {
		try {
			List<List> lists = loadCoreObjects();
			updateAll(lists);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateAll(List<List> lists) {
		Session session = HibernateUtil.openSession();
		AccounterThreadLocal.set(getUser());
		Transaction beginTransaction = session.beginTransaction();
		try {
			for (List l : lists) {
				for (Object o : l) {
					session.replicate(o, ReplicationMode.OVERWRITE);
				}
			}
			Company company = getCompany();
			initChipher();
			company.setLocked(false);
			session.saveOrUpdate(company);
			beginTransaction.commit();
		} catch (Exception e) {
			beginTransaction.rollback();
			e.printStackTrace();
		} finally {
			beginTransaction.begin();
			Company company = getCompany();
			company.setLocked(false);
			session.saveOrUpdate(company);
			beginTransaction.commit();
			session.close();
		}
	}

	private void initChipher() throws Exception {
		Session session = HibernateUtil.getCurrentSession();
		Company company = getCompany();
		byte[] s3 = EU.generateSymetric();
		byte[] companySecret = EU.encrypt(s3, csk);

		String string = SecureUtils.createID(16);
		byte[] prk = EU.generatePBS(string);

		byte[] encryptedPass = EU.encrypt(csk, prk);
		company.setEncryptedPassword(encryptedPass);
		company.setSecretKey(companySecret);
		session.saveOrUpdate(company);

		byte[] userSecret = EU.encrypt(s3, EU.decrypt(d2, s2));
		EU.storeKey(s2, sessionId);
		EU.createCipher(userSecret, d2, sessionId);
		User user = getUser();
		user.setSecretKey(userSecret);
		session.saveOrUpdate(user);

		log.info("Company password recovery key---" + string);
		sendCompanyPasswordRecoveryKey(emailId, string);
	}

	private User getUser() {
		Session session = HibernateUtil.getCurrentSession();
		Query setParameter = session
				.getNamedQuery("getUser.by.mailId.and.companyId")
				.setParameter("emailId", emailId)
				.setParameter("companyId", companyId);
		return (User) setParameter.uniqueResult();
	}

	private Company getCompany() {
		Session session = HibernateUtil.getCurrentSession();
		Object object = session.get(Company.class, companyId);
		return (Company) object;
	}

	private List<List> loadCoreObjects() throws Exception {

		Session session = HibernateUtil.openSession();
		try {
			Company company = getCompany();
			log.info("Encrypting is started (" + company.getTradingName() + ")");
			List<List> lists = new ArrayList<List>();
			// classes.clear();
			// classes.add("FixedAsset");
			for (Class<?> cls : classes) {
				Criteria criteria = session.createCriteria(cls);
				criteria.add(Restrictions.eq("company", company));
				try {
					List list = criteria.list();
					List list2 = new ArrayList();
					for (Object o : list) {
						Object initializeAndUnproxy = HibernateUtil
								.initializeAndUnproxy(o);
						list2.add(initializeAndUnproxy);
					}
					lists.add(list2);
				} catch (Exception e) {
					System.out.println(cls);
				}
			}
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public static void sendCompanyPasswordRecoveryKey(String emailId, String key) {
		try {
			UsersMailSendar.sendMailToUserWithPasswordRecoverKey(emailId, key);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
