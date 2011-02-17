package com.vimukti.accounter.core;

import java.util.Date;

public class ReportList {

	long transactionId;

	int transactionType;

	String itemName;

	String Name;

	String GroupName;

	String memo;

	double OpeningBalance;

	double Balance;

	double quantity;

	double amount;

	double discount;

	double writeOff;

	boolean flag;

	int itemType;

	int baseType;

	int status;

	Date date;

	long number;

	Date dueDate;

	public void setNumber(long num) {
		this.number = num;
	}

	public void setDate(Date d) {
		this.date = d;
	}

	public void setDueDate(Date d) {
		this.dueDate = d;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public int getBaseType() {
		return baseType;
	}

	public void setBaseType(int baseType) {
		this.baseType = baseType;
	}

	public void setItemType(int type) {
		this.itemType = type;
	}

	public void setFlag(boolean f) {
		this.flag = f;
	}

	public void setItemName(String name) {
		this.itemName = name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public void setGroupName(String name) {
		this.GroupName = name;
	}

	public void setAmount(double amt) {
		this.amount = amt;
	}

	public void setWriteOff(double amt) {
		this.writeOff = amt;
	}

	public void setQuantity(double q) {
		this.quantity = q;
	}

	public void setDiscount(double q) {
		this.discount = q;
	}

	public void setOpeningBalance(double q) {
		this.OpeningBalance = q;
	}

	public void setBalance(double q) {
		this.Balance = q;
	}

	public void setStatus(int st) {

		this.status = st;
	}

	public int getItemType() {
		return this.itemType;
	}

	public String getItemName() {
		return this.itemName;
	}

	public String getName() {
		return this.Name;
	}

	public String getGroupName() {
		return this.GroupName;
	}

	public double getAmount() {
		return this.amount;
	}

	public double getQuantity() {
		return this.quantity;
	}

	public double getDiscount() {
		return this.discount;
	}

	public double getWriteOff() {
		return this.writeOff;
	}

	public double getOpeningBalance() {
		return this.OpeningBalance;
	}

	public double getBalance() {
		return this.Balance;
	}

	public boolean getFlag() {
		return this.flag;
	}

	public long getNumber() {
		return this.number;
	}

	public Date getDate() {
		return this.date;
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public int getStatus() {
		return this.status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}