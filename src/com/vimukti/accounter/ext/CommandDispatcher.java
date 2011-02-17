/**
 * 
 */
package com.vimukti.accounter.ext;

/**
 * @author Fernandez
 * 
 */
public class CommandDispatcher {

	public static final int COMMAND_CREATE_USER = 100;
	public static final int COMMAND_ALTER_USER = 101;
	public static final int COMMAND_DELETE_USER = 102;

	public static final int COMMAND_CREATE_COMPANY = 110;
	public static final int COMMAND_ALTER_COMPANY = 111;
	public static final int COMMAND_DELETE_COMPANY = 112;

	public static final int COMMAND_CREATE_ACCOUNT = 120;
	public static final int COMMAND_ALTER_ACCOUNT = 121;
	public static final int COMMAND_DELETE_ACCOUNT = 122;

	public static final int COMMAND_CREATE_CUSTOMER = 130;
	public static final int COMMAND_ALTER_CUSTOMER = 131;
	public static final int COMMAND_DELETE_CUSTOMER = 132;

	public static final int COMMAND_CREATE_VENDOR = 140;
	public static final int COMMAND_ALTER_VENDOR = 141;
	public static final int COMMAND_DELETE_VENDOR = 142;

	public static final int COMMAND_CREATE_CURRENCY = 150;
	public static final int COMMAND_ALTER_CURRENCY = 151;
	public static final int COMMAND_DELETE_CURRENCY = 152;

	public static final int COMMAND_CREATE_CREDITRATING = 160;
	public static final int COMMAND_ALTER_CREDITRATING = 161;
	public static final int COMMAND_DELETE_CREDITRATING = 162;

	public static final int COMMAND_CREATE_CUSTOMERGROUP = 170;
	public static final int COMMAND_ALTER_CUSTOMERGROUP = 171;
	public static final int COMMAND_DELETE_CUSTOMERGROUP = 172;

	public static final int COMMAND_CREATE_ITEM = 180;
	public static final int COMMAND_ALTER_ITEM = 181;
	public static final int COMMAND_DELETE_ITEM = 182;

	public static final int COMMAND_CREATE_ITEMTAX = 190;
	public static final int COMMAND_ALTER_ITEMTAX = 191;
	public static final int COMMAND_DELETE_ITEMTAX = 192;

	public static final int COMMAND_CREATE_PAYMENTMETHOD = 200;
	public static final int COMMAND_ALTER_PAYMENTMETHOD = 201;
	public static final int COMMAND_DELETE_PAYMENTMETHOD = 202;

	public static final int COMMAND_CREATE_PAYMENTTERMS = 210;
	public static final int COMMAND_ALTER_PAYMENTTERMS = 211;
	public static final int COMMAND_DELETE_PAYMENTTERMS = 212;

	public static final int COMMAND_CREATE_PRICELEVEL = 220;
	public static final int COMMAND_ALTER_PRICELEVEL = 221;
	public static final int COMMAND_DELETE_PRICELEVEL = 222;

	public static final int COMMAND_CREATE_SALESPERSON = 230;
	public static final int COMMAND_ALTER_SALESPERSON = 231;
	public static final int COMMAND_DELETE_SALESPERSON = 232;

	public static final int COMMAND_CREATE_SHIPPINGMETHOD = 240;
	public static final int COMMAND_ALTER_SHIPPINGMETHOD = 241;
	public static final int COMMAND_DELETE_SHIPPINGMETHOD = 242;

	public static final int COMMAND_CREATE_TAXAGENCY = 250;
	public static final int COMMAND_ALTER_TAXAGENCY = 251;
	public static final int COMMAND_DELETE_TAXAGENCY = 252;

	public static final int COMMAND_CREATE_TAXCODE = 260;
	public static final int COMMAND_ALTER_TAXCODE = 261;
	public static final int COMMAND_DELETE_TAXCODE = 262;

	public static final int COMMAND_CREATE_TAXGROUP = 270;
	public static final int COMMAND_ALTER_TAXGROUP = 271;
	public static final int COMMAND_DELETE_TAXGROUP = 272;

	public static final int COMMAND_CREATE_TAXRATE = 280;
	public static final int COMMAND_ALTER_TAXRATE = 281;
	public static final int COMMAND_DELETE_TAXRATE = 282;

	public static final int COMMAND_CREATE_UNIT_OF_MEASURE = 290;
	public static final int COMMAND_ALTER_UNIT_OF_MEASURE = 291;
	public static final int COMMAND_DELETE_UNIT_OF_MEASURE = 292;

	public static final int COMMAND_CREATE_VENDORGROUP = 300;
	public static final int COMMAND_ALTER_VENDORGROUP = 301;
	public static final int COMMAND_DELETE_VENDORGROUP = 302;

	public static final int COMMAND_CREATE_CASH_PURCHASE = 310;
	public static final int COMMAND_ALTER_CASH_PURCHASE = 311;
	public static final int COMMAND_DELETE_CASH_PURCHASE = 312;

	public static final int COMMAND_CREATE_CASH_SALES = 320;
	public static final int COMMAND_ALTER_CASH_SALES = 321;
	public static final int COMMAND_DELETE_CASH_SALES = 322;

	public static final int COMMAND_CREATE_CREDITCARD_CHARGE = 330;
	public static final int COMMAND_ALTER_CREDITCARD_CHARGE = 331;
	public static final int COMMAND_DELETE_CREDITCARD_CHARGE = 332;

	public static final int COMMAND_CREATE_CUSTOMER_CREDITMEMO = 340;
	public static final int COMMAND_ALTER_CUSTOMER_CREDITMEMO = 341;
	public static final int COMMAND_DELETE_CUSTOMER_CREDITMEMO = 342;

	public static final int COMMAND_CREATE_CUSTOMER_REFUNDS = 350;
	public static final int COMMAND_ALTER_CUSTOMER_REFUNDS = 351;
	public static final int COMMAND_DELETE_CUSTOMER_REFUNDS = 352;

	public static final int COMMAND_CREATE_ENTERBILL = 360;
	public static final int COMMAND_ALTER_ENTERBILL = 361;
	public static final int COMMAND_DELETE_ENTERBILL = 362;

	public static final int COMMAND_CREATE_ESTIMATE = 370;
	public static final int COMMAND_ALTER_ESTIMATE = 371;
	public static final int COMMAND_DELETE_ESTIMATE = 372;

	public static final int COMMAND_CREATE_INVOICE = 380;
	public static final int COMMAND_ALTER_INVOICE = 381;
	public static final int COMMAND_DELETE_INVOICE = 382;

	public static final int COMMAND_CREATE_ISSUEPAYMENT = 390;
	public static final int COMMAND_ALTER_ISSUEPAYMENT = 391;
	public static final int COMMAND_DELETE_ISSUEPAYMENT = 392;

	public static final int COMMAND_CREATE_MAKEDEPOSIT = 400;
	public static final int COMMAND_ALTER_MAKEDEPOSIT = 401;
	public static final int COMMAND_DELETE_MAKEDEPOSIT = 402;

	public static final int COMMAND_CREATE_PAYBILL = 410;
	public static final int COMMAND_ALTER_PAYBILL = 411;
	public static final int COMMAND_DELETE_PAYBILL = 412;

	public static final int COMMAND_CREATE_PURCHASEORDER = 420;
	public static final int COMMAND_ALTER_PURCHASEORDER = 421;
	public static final int COMMAND_DELETE_PURCHASEORDER = 422;

	public static final int COMMAND_CREATE_RECIEVEPAYMENT = 430;
	public static final int COMMAND_ALTER_RECIEVEPAYMENT = 431;
	public static final int COMMAND_DELETE_RECIEVEPAYMENT = 432;

	public static final int COMMAND_CREATE_SALESORDER = 440;
	public static final int COMMAND_ALTER_SALESORDER = 441;
	public static final int COMMAND_DELETE_SALESORDER = 442;

	public static final int COMMAND_CREATE_TRANSFERFUND = 450;
	public static final int COMMAND_ALTER_TRANSFERFUND = 451;
	public static final int COMMAND_DELETE_TRANSFERFUND = 452;

	public static final int COMMAND_CREATE_VENDORCREDIT_MEMO = 460;
	public static final int COMMAND_ALTER_VENDORCREDIT_MEMO = 461;
	public static final int COMMAND_DELETE_VENDORCREDIT_MEMO = 462;

	public static final int COMMAND_CREATE_WRITECHECK = 470;
	public static final int COMMAND_ALTER_WRITECHECK = 471;
	public static final int COMMAND_DELETE_WRITECHECK = 472;

	public static final int COMMAND_CREATE_ITEMGROUP = 480;
	public static final int COMMAND_ALTER_ITEMGROUP = 481;
	public static final int COMMAND_DELETE_ITEMGROUP = 482;

	public static final int COMMAND_CREATE_SHIPPINGTERMS = 490;
	public static final int COMMAND_ALTER_SHIPPINGTERMS = 491;
	public static final int COMMAND_DELETE_SHIPPINGTERMS = 492;

	public static final int COMMAND_CREATE_JOURNAL = 500;
	public static final int COMMAND_ALTER_JOURNAL = 501;
	public static final int COMMAND_DELETE_JOURNAL = 502;

	public static final int COMMAND_CREATE_FISCAL_YEAR = 510;
	public static final int COMMAND_ALTER_FISCAL_YEAR = 511;
	public static final int COMMAND_DELETE_FISCAL_YEAR = 512;

	public static final int COMMAND_CREATE_BANK = 520;
	public static final int COMMAND_ALTER_BANK = 521;
	public static final int COMMAND_DELETE_BANK = 522;

	public static final int COMMAND_CREATE_PAY_SALES_TAX = 530;
	public static final int COMMAND_ALTER_PAY_SALES_TAX = 531;
	public static final int COMMAND_DELETE_PAY_SALES_TAX = 532;

	public void sendCommand(Command command) throws CommandException {
		// TODO YET TO DO SEND COMMAND

	}

}
