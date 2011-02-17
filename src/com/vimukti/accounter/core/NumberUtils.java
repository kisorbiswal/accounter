package com.vimukti.accounter.core;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.bizantra.server.storage.HibernateUtil;

public class NumberUtils {

	public static String getNextTransactionNumber(int transactionType) {

		String prevNumber = getPreviousTransactionNumber(transactionType);

		return getStringwithIncreamentedDigit(prevNumber);
	}

	public static String getPreviousTransactionNumber(int transactionType) {

		Query query = HibernateUtil
				.getCurrentSession()
				.createQuery(
						"select t.number from com.vimukti.accounter.core.Transaction t where t.type =:transactionType")
				.setParameter("transactionType", transactionType);

		List list = query.list();
		if ((list.size() == 0)) {
			return "0";
		}

		for (int i = list.size() - 1; i >= 0; i--) {
			String num = (String) list.get(i);
			if (num.replaceAll("[\\D]", "").length() > 0) {
				return num;
			}
			// else {
			// if (maxCount != 0) {
			// maxCount = maxCount - 1;
			// return getPreviousTransactionNumber(transactionType,
			// maxCount);
			// }
			// }
		}

		return "0";
	}

	@SuppressWarnings("unchecked")
	public static String getNextVoucherNumber() {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session
				.createQuery("from com.vimukti.accounter.core.Entry e ");
		List list1 = query.list();

		if (list1.size() <= 0) {

			return getNextTransactionNumber(Transaction.TYPE_JOURNAL_ENTRY);
		}

		query = session
				.createQuery("select e.voucherNumber from com.vimukti.accounter.core.Entry e where e.id = (select max(e1.id) from com.vimukti.accounter.core.Entry e1 )");
		List list = query.list();

		if (list != null) {
			return getStringwithIncreamentedDigit(((String) list.get(0)));
		} else
			return "1";

	}

	public static String getNextFixedAssetNumber() {
		String prevNumber = getPreviousFixedAssetNumber();
		return getStringwithIncreamentedDigit(prevNumber);

	}

	private static String getPreviousFixedAssetNumber() {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session
				.createQuery("select f.assetNumber from com.vimukti.accounter.core.FixedAsset f");
		List<?> list = query.list();
		if (list.size() == 0) {
			return "0";
		}
		for (int i = list.size() - 1; i >= 0; i--) {
			String number = (String) list.get(i);
			if (number.replaceAll("//D", "").length() > 0) {
				return number;
			}

		}
		return null;
	}
	
	public static String getNextCustomerNumber() {

		String prevNumber = getPreviousCustomerNumber();

		return getStringwithIncreamentedDigitForCustomer(prevNumber);
	}
	
	public static String getPreviousCustomerNumber() {

		Query query = HibernateUtil
				.getCurrentSession()
				.createQuery(
						"select c.number from com.vimukti.accounter.core.Customer c order by c.id");

		List list = query.list();
		if ((list.size() == 0)) {
			return "0";
		}

		for (int i = list.size() - 1; i >= 0; i--) {
			String num = (String) list.get(i);
			if (num.replaceAll("[\\D]", "").length() > 0) {
				return num;
			} else
				return num+"0";
		}

		return "0";
	}

	public static String getStringwithIncreamentedDigit(String prevNumber) {

		String incredNumber = "";
		if (prevNumber != null) {
			for (int i = prevNumber.length() - 1; i >= 0; i--) {
				char ch = prevNumber.charAt(i);

				if (incredNumber.length() > 0 && !Character.isDigit(ch)) {
					break;
				} else if (Character.isDigit(ch)) {
					incredNumber = ch + incredNumber;
				}
			}

		}
		if (incredNumber.length() > 0) {
			// incredNumber = new
			// StringBuffer(incredNumber).reverse().toString();
			prevNumber = prevNumber.replace(incredNumber, ""
					+ (Long.parseLong(incredNumber) + 1));
		}
		return prevNumber;

	}
	
	public static String getStringwithIncreamentedDigitForCustomer(String prevNumber) {

		String incredNumber = "";
		if (prevNumber != null) {
			for (int i = prevNumber.length() - 1; i >= 0; i--) {
				char ch = prevNumber.charAt(i);

				if (incredNumber.length() > 0 && !Character.isDigit(ch)) {
					break;
				} else if (Character.isDigit(ch)) {
					incredNumber = ch + incredNumber;
				}
			}

		}
		if (incredNumber.length() > 0) {
			
				prevNumber = prevNumber.replace(incredNumber, ""
					+ (new BigInteger(incredNumber).add(BigInteger.valueOf(1))));
				
		}
		return prevNumber;

	}
}
