package com.vimukti.accounter.encryption;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.core.User;
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
	private static List<String> classes;

	public static void init() {
		classes = new ArrayList<String>();
		Logger logger = Logger.getLogger(Encrypter.class);

		// File core = getDirectoryFile();
		// File[] listFiles = core.listFiles();
		String[] list = getClassesNames();
		logger.info("Total classes: " + list.length);
		try {
			Class<?> create = Class
					.forName("com.vimukti.accounter.core.CreatableObject");

			List<String> skip = new ArrayList<String>();
			String[] skips = "Currency,Unit,Measurement,Payee".split(",");
			for (String s : skips) {
				skip.add(s);
			}

			for (String string : list) {
				if (string.endsWith(".java")) {
					string = string.substring(0, string.indexOf(".java"));
				} else {
					continue;
				}
				if (skip.contains(string)) {
					continue;
				}
				try {
					Class<?> clazz = Class
							.forName("com.vimukti.accounter.core." + string);
					if (create.isAssignableFrom(clazz)) {
						classes.add(string);
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

	private static String[] getClassesNames() {
		String string = "Account.java,AccountComparator.java,AccounterClass.java,AccounterServerConstants.java,AccounterThreadLocal.java,AccountTransaction.java,AccountTransactionByAccount.java,AccountTransactionByTaxCode.java,AccountTransactionItem.java,Activation.java,Activity.java,ActivityType.java,Address.java,AdjustmentReason.java,Advertisement.java,Attachment.java,AuditWriter.java,AuditWriterImpl.java,Bank.java,BankAccount.java,BankRule.java,Box.java,BrandingTheme.java,Budget.java,BudgetItem.java,BudgetReportTemplate.java,BuildAssembly.java,CashPurchase.java,CashSalePdfGeneration.java,CashSales.java,ChequeLayout.java,ChequePdfGenerator.java,Client.java,ClientConvertUtil.java,ClientPaypalDetails.java,ClientSubscription.java,CloneUtil.java,CloneUtil2.java,CommodityCode.java,Company.java,CompanyPreferences.java,Contact.java,CreatableObject.java,CreditCardCharge.java,CreditNotePdfGeneration.java,CreditNotePDFTemplete.java,CreditNoteTemplete.java,CreditRating.java,CreditsAndPayments.java,CSVReportTemplate.java,Currency.java,CurrencyRate.java,Customer.java,CustomerCreditMemo.java,CustomerGroup.java,CustomerPrePayment.java,CustomerRefund.java,CustomerStatementTemplate.java,CustomField.java,CustomFieldValue.java,DeleteReason.java,Depreciation.java,Developer.java,Email.java,EmailAccount.java,Employee.java,EmployeeCategory.java,EmployeeGroup.java,EmployeePayHeadComponent.java,EnterBill.java,Estimate.java,ETDSAnnexuresGenerator.java,EU.java,Exempted.java,Expense.java,Fax.java,FinanceDate.java,FinanceLogger.java,FiscalYear.java,FixedAsset.java,FixedAssetHistory.java,FixedAssetNote.java,Form16ApdfTemplate.java,Form26QAnnexureGenerator.java,Form27EQAnnexureGenerator.java,Form27QAnnexureGenerator.java,IAccounterServerCore.java,IMActivation.java,IMUser.java,INamedObject.java,InventoryAssembly.java,InventoryAssemblyItem.java,InventoryPurchase.java,Invoice.java,InvoiceExcelTemplete.java,InvoicePdfGeneration.java,InvoicePDFTemplete.java,IRASCompanyInfo.java,IRASGeneralLedgerLineInfo.java,IRASInformation.java,IRASPurchaseLineInfo.java,IRASSupplyLineInfo.java,IssuePayment.java,Item.java,ItemGroup.java,ITemplate.java,ItemReceipt.java,ItemStatus.java,Job.java,JournalEntry.java,Location.java,LongUseType.java,MaintananceInfoUser.java,MakeDeposit.java,Measurement.java,MessageOrTask.java,Misc1099PDFTemplate.java,Misc1099SamplePDFTemplate.java,MISCInformationTemplate.java,MobileCookie.java,News.java,NominalCodeRange.java,NumberUtils.java,ObjectConvertUtil.java,PayBill.java,Payee.java,PayEmployee.java,PayExpense.java,PayHead.java,PayHeadField.java,PaymentTerms.java,PayrollUnit.java,PayRun.java,PayStructure.java,PayStructureItem.java,PayTAX.java,PayTAXEntries.java,PayTDS.java,Phone.java,PortletConfiguration.java,PortletPageConfiguration.java,PriceLevel.java,PrintTemplete.java,PurchaseOrder.java,Quantity.java,QuotePdfGeneration.java,QuotePdfTemplate.java,ReceivePayment.java,ReceivePaymentPdfGeneration.java,ReceiveVAT.java,ReceiveVATEntries.java,Reconciliation.java,ReconciliationItem.java,RecurringTransaction.java,ReffereredObject.java,RememberMeKey.java,Reminder.java,ReportsGenerator.java,ReportTemplate.java,ResetPasswordToken.java,SalesOrder.java,SalesPerson.java,Server.java,ServerCompany.java,ServerConvertUtil.java,ServerMaintanance.java,ShippingMethod.java,ShippingTerms.java,Statement.java,StatementRecord.java,StockAdjustment.java,StockTransfer.java,StockTransferItem.java,Subscription.java,SupportedUser.java,TAXAdjustment.java,TAXAgency.java,TAXCode.java,TAXGroup.java,TAXItem.java,TAXItemGroup.java,TAXRateCalculation.java,TaxRates.java,TAXReturn.java,TAXReturnEntry.java,TDSChalanDetail.java,TDSCoveringLetterTemplate.java,TDSDeductorMasters.java,TDSInfo.java,TDSResponsiblePerson.java,TDSTransactionItem.java,TemplateBuilder.java,Transaction.java,TransactionCreditsAndPayments.java,TransactionDepositItem.java,TransactionExpense.java,TransactionIssuePayment.java,TransactionItem.java,TransactionLog.java,TransactionMakeDepositEntries.java,TransactionPayBill.java,TransactionPayExpense.java,TransactionPayTAX.java,TransactionReceivePayment.java,TransactionReceiveVAT.java,TransferFund.java,Unit.java,UnitOfMeasure.java,User.java,UserPermissions.java,UserPreferences.java,UserUtils.java,Util.java,Utility_R.java,Utility.java,VATReturnBox.java,Vendor.java,VendorCreditMemo.java,VendorGroup.java,Warehouse.java,WareHouseAllocation.java,WriteCheck.java";
		return string.split(",");
	}

	private static File getDirectoryFile() {
		Logger logger = Logger.getLogger(Encrypter.class);
		try {
			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();
			URL resource = classLoader
					.getResource("com/vimukti/accounter/core/");
			File core = new File(resource.getFile());
			logger.info("Parent file -->:" + resource.getFile());
			return core;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
		try {
			Transaction beginTransaction = session.beginTransaction();
			for (List l : lists) {
				for (Object o : l) {
					session.replicate(o, ReplicationMode.OVERWRITE);
				}
			}
			initChipher();
			Company company = getCompany();
			company.setLocked(false);
			session.saveOrUpdate(company);
			beginTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
			for (String cls : classes) {
				Criteria criteria = session.createCriteria(Class
						.forName("com.vimukti.accounter.core." + cls));
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
