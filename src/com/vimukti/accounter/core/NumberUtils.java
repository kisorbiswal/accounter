package com.vimukti.accounter.core;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;

public class NumberUtils {

	public static String getNextTransactionNumber(int transactionType,
			Company company) {

		String prevNumber = getPreviousTransactionNumber(transactionType,
				company);

		return getStringwithIncreamentedDigit(prevNumber);
	}

	public static String getPreviousTransactionNumber(int transactionType,
			Company company) {

		Query query = HibernateUtil.getCurrentSession()
				.getNamedQuery("getTransactionNumber.by.type")
				.setParameter("transactionType", transactionType)
				.setEntity("company", company);

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

	public static String getNextVoucherNumber(Company company) {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("get.Entry").setEntity("company",
				company);
		List list1 = query.list();

		if (list1.size() <= 0) {

			return getNextTransactionNumber(Transaction.TYPE_JOURNAL_ENTRY,
					company);
		}
		query = session.getNamedQuery("getEntry.byId.andMax").setEntity(
				"company", company);
		List list = query.list();

		if (list != null) {
			return getStringwithIncreamentedDigit(((String) list.get(0)));
		} else
			return "1";

	}

	public static String getNextFixedAssetNumber(Company company) {
		String prevNumber = getPreviousFixedAssetNumber(company);
		return getStringwithIncreamentedDigit(prevNumber);

	}

	private static String getPreviousFixedAssetNumber(Company company) {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getassestNumber.from.FixedAsset")
				.setEntity("company", company);
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

	public static String getNextAutoCustomerNumber(Company company) {

		Query query = HibernateUtil.getCurrentSession()
				.getNamedQuery("getCustomerNumber.orderBy.customerNumber")
				.setEntity("company", company);

		List list = query.list();
		String arr[] = (String[]) list.toArray(new String[list.size()]);
		Long longArr[] = new Long[arr.length];
		for (int iii = 0; iii < arr.length; iii++) {
			longArr[iii] = Long.parseLong(arr[iii].trim());
		}
		Arrays.sort(longArr);

		if ((list.size() == 0)) {
			return "1";
		}
		Long number = longArr[0];
		for (int i = 0; i < list.size(); i++) {
			Long num = longArr[i];
			while (number == num) {
				number++;
			}
		}

		return number.toString();

	}

	public static String getNextAutoVendorNumber(Company company) {

		Query query = HibernateUtil.getCurrentSession()
				.getNamedQuery("getVendorNumber.byId.andOrder")
				.setEntity("company", company);

		List list = query.list();
		String arr[] = (String[]) list.toArray(new String[list.size()]);
		Long longArr[] = new Long[arr.length];
		for (int iii = 0; iii < arr.length; iii++) {
			longArr[iii] = Long.parseLong(arr[iii].trim());
		}
		Arrays.sort(longArr);

		if ((list.size() == 0)) {
			return "1";
		}
		Long number = longArr[0];
		for (int i = 0; i < list.size(); i++) {
			Long num = longArr[i];
			while (number == num) {
				number++;
			}
		}

		return number.toString();

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
			prevNumber = prevNumber.replace(incredNumber,
					"" + (Long.parseLong(incredNumber) + 1));
		}
		return prevNumber;

	}

	public static String getStringwithIncreamentedDigitForCustomer(
			String prevNumber) {

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

			prevNumber = prevNumber
					.replace(
							incredNumber,
							""
									+ (new BigInteger(incredNumber)
											.add(BigInteger.valueOf(1))));

		}
		return prevNumber;

	}

}
