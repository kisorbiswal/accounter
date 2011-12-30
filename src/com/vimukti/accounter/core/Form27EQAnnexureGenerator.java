package com.vimukti.accounter.core;

import java.util.List;
import java.util.Set;

import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.ClientTDSDeductorMasters;
import com.vimukti.accounter.web.client.core.ClientTDSResponsiblePerson;
import com.vimukti.accounter.web.client.core.ClientTDSTransactionItem;

public class Form27EQAnnexureGenerator extends ETDSAnnexuresGenerator {

	String fileText = null;
	private ClientTDSChalanDetail chalanDetails;
	private ClientTDSTransactionItem transactionItems;
	private List<ClientTDSChalanDetail> chalanDetailsList;
	private Company companyGot;
	private Vendor vendorFinal;

	private int runningSerialNumber;
	private int runningChalanNumber;
	private int lineNumber;
	String[] codeListArray;
	String[] remarkListArray;

	public Form27EQAnnexureGenerator() {
	}

	public Form27EQAnnexureGenerator(
			List<ClientTDSDeductorMasters> tdsDeductorMasterDetails2,
			List<ClientTDSResponsiblePerson> responsiblePersonDetails2,
			Company company, String codeList, String remarkList) {
		super.setDetails(tdsDeductorMasterDetails2, responsiblePersonDetails2);

		codeListArray = codeList.split("");
		remarkListArray = remarkList.split("");

		companyGot = company;

	}

	public String generateFile() {
		Double total = 0.00;

		for (ClientTDSChalanDetail chalan : chalanDetailsList) {
			total = total + chalan.getIncomeTaxAmount()
					+ chalan.getSurchangePaidAmount()
					+ chalan.getEducationCessAmount()
					+ chalan.getInterestPaidAmount() + chalan.getOtherAmount();

		}

		fileText = generateFileHeaderRecord();
		fileText = fileText + generateBatchHeaderRecord(total);
		int i = 3;
		int chalNum = 1;
		for (ClientTDSChalanDetail chalan : chalanDetailsList) {
			chalanDetails = chalan;
			setLineNumber(i);
			setRunningChalanNumber(chalNum);
			chalNum++;
			fileText = fileText + generateChalanVoucherDetailsRecord();
			int serNum = 1;
			for (ClientTDSTransactionItem items : chalanDetails
					.getTransactionItems()) {
				setRunningSerialNumber(serNum);
				serNum++;
				Set<Vendor> vendors = companyGot.getVendors();
				for (Vendor vendor : vendors) {
					if (vendor.getID() == items.getVendorID()) {
						vendorFinal = vendor;
					}
				}
				i++;
				transactionItems = items;
				setLineNumber(i);
				fileText = fileText + generateDeducteeDetailsRecord();
			}
			i++;
		}

		return fileText;
	}

	public String generateDeducteeDetailsRecord() {

		String deucteeDetailsRecordString = getLineNumber() + addDelimiter();

		// Record Type Value "DD"(Deductee Detail) for Deductee-detail record
		deucteeDetailsRecordString = deucteeDetailsRecordString + "DD"
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getBatchNumber() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getRunningChalanNumber() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getRunningSerialNumber() + addDelimiter();

		// Mode Value should be O
		deucteeDetailsRecordString = deucteeDetailsRecordString + "O"
				+ addDelimiter();

		// Employee Serial No(Not applicable)
		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getDeducteeCode(getRunningSerialNumber()) + addDelimiter();

		// Last Employee / Party PAN ( Used for Verification) (Not applicable)
		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getDeducteePan() + addDelimiter();

		// Last Employee/Party PAN Ref. No. (Not applicable)
		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString + getPANRefNo()
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getDeducteeName() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getTDSIncomeTaxforPeriod() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getTDSSurchargeforPeriod() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString + getTDSECess()
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getTotalIncomeTaxDeductedatSource() + addDelimiter();

		// Last Total Income Tax Deducted at Source (Income Tax+Surcharge+Cess)
		// ( Used for Verification) (Not applicable)
		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getTotalTaxDeposited() + addDelimiter();

		// Last Total Tax Deposited ( Used for Verification) (Not applicable)
		// Total Value of Purchase (Not applicable)

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getAmountOFpaymentorCredit() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getDateonWhichAmountPaid() + addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getDateOnWhichTaxCollected() + addDelimiter();

		// Date of Deposit (Not applicable)
		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getRateAtWhichTaxDeducted() + addDelimiter();

		// Grossing up Indicator (Not applicable)
		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getBookEntry() + addDelimiter();

		// Date of furnishing Tax Deduction Certificate (Not applicable)
		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter();

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ getRemarks(getRunningSerialNumber()) + addDelimiter();

		// Remarks 2 (For future use)
		// Remarks 3 (For future use)
		// Record Hash (Not applicable)

		deucteeDetailsRecordString = deucteeDetailsRecordString
				+ addDelimiter() + addDelimiter() + addDelimiter();

		return deucteeDetailsRecordString + endLine();

	}

	/**
	 * Remarks 1 (Reason for non-deduction / lower deduction/ higher
	 * deduction/threshold)
	 * 
	 * @param i
	 * 
	 * @return
	 */
	private String getRemarks(int i) {
		String string = remarkListArray[i + 1];
		if (string.equals("-")) {
			return addDelimiter();
		} else {
			return string;
		}

	}

	/**
	 * Mention whether it is paid by Book entry or otherwise ('Y' or 'N').
	 * Mention "Y" for Book entry and "N" otherwise.
	 * 
	 * @return
	 */
	private String getBookEntry() {

		if (chalanDetails.isBookEntry()) {
			return "Y";
		} else {
			return "N";
		}

	}

	/**
	 * Rate at which Tax is deducted, with decimal precision of 4 point. E.g. if
	 * the rate is 2 then the same should be mentioned as 2.0000. If value in
	 * field 'Total Income Tax Deducted at Source' is 0.00 then rate at which
	 * tax deducted should be 0.0000
	 * 
	 * @return
	 */
	private String getRateAtWhichTaxDeducted() {
		return Double.toString(vendorFinal.getTAXItem().getTaxRate());
	}

	/**
	 * Date of tax deduction. Mandatory if 'Total Income Tax Deducted at Source'
	 * is greater than Zero (0.00) . No value needs to be specified if 'Total
	 * Income Tax Deducted at Source' is Zero (0.00) .
	 * 
	 * @return
	 */
	private String getDateOnWhichTaxCollected() {
		return Long.toString(transactionItems.getTransactionDate());
	}

	/**
	 * Date on which Amount paid/ Credited /Debited to deductee.
	 * 
	 * @return
	 */
	private String getDateonWhichAmountPaid() {
		return Long.toString(transactionItems.getTransactionDate());
	}

	/**
	 * Mention the Amount paid to deductee. Value should always be greater than
	 * 0.00
	 * 
	 * @return
	 */
	private String getAmountOFpaymentorCredit() {
		return Double.toString(transactionItems.getTaxAmount()
				+ transactionItems.getSurchargeAmount()
				+ transactionItems.getEduCess());
	}

	/**
	 * Mention the Total Tax Deposited for the Deductee. Zero (0.00 ) for a Nil
	 * Statement . Value should be equal to 0.00 if value in field 'Remarks 1
	 * (Reason for non-deduction / lower deduction)' is B and value in field
	 * 'Section' is 194, 194A, 194EE or 193. If value in field 'Remarks 1
	 * (Reason for non-deduction / lower deduction) is 'A' or 'null' then value
	 * should be greater than or equal to 0.00
	 * 
	 * @return
	 */
	private String getTotalTaxDeposited() {
		return Double.toString(transactionItems.getTaxAmount()
				+ transactionItems.getSurchargeAmount()
				+ transactionItems.getEduCess());
	}

	/**
	 * Total Income Tax Deducted at Source (TDS / TCS Income Tax+ TDS / TCS
	 * Surcharge + TDS/TCS Cess) I.e. (421+ 422 + 423 )
	 * 
	 * @return
	 */
	private String getTotalIncomeTaxDeductedatSource() {
		return Double.toString(transactionItems.getTaxAmount()
				+ transactionItems.getSurchargeAmount()
				+ transactionItems.getEduCess());
	}

	/**
	 * Decimal with precision value 2 is allowed. If value in field 'Remarks 1
	 * (Reason for non-deduction / lower deduction)' is 'B' and value in field
	 * 'Section' is 194, 194A, 194EE or 193 then value should be equal to 0.00.
	 * If value in field Remarks 1 (Reason for non-deduction / lower deduction)
	 * is "A" or "null" then value should be greater than or equal to 0.00
	 * 
	 * @return
	 */
	private String getTDSECess() {
		return Double.toString(transactionItems.getEduCess());
	}

	/**
	 * Decimal with precision value 2 is allowed. If value in field 'Remarks 1
	 * (Reason for non-deduction / lower deduction)' is 'B' and value in field
	 * 'Section' is 194, 194A, 194EE or 193 then value should be equal to 0.00.
	 * If value in field Remarks 1 (Reason for non-deduction / lower deduction)
	 * is "A" or "null" then value should be greater than or equal to 0.00
	 */
	private String getTDSSurchargeforPeriod() {
		return Double.toString(transactionItems.getSurchargeAmount());
	}

	/**
	 * Decimal with precision value 2 is allowed. If value in field 'Remarks 1
	 * (Reason for non-deduction / lower deduction)' is 'B' and value in field
	 * 'Section' is 194, 194A, 194EE or 193 then value should be equal to 0.00.
	 * If value in field Remarks 1 (Reason for non-deduction / lower deduction)
	 * is "A" or "null" then value should be greater than or equal to 0.00
	 * 
	 * @return
	 */
	private String getTDSIncomeTaxforPeriod() {
		return Double.toString(transactionItems.getTaxAmount());
	}

	/**
	 * Mention the Name of the deductee.
	 * 
	 * @return
	 */
	private String getDeducteeName() {
		String name = vendorFinal.getName();
		return name;
	}

	/**
	 * The PAN Ref No is a unique identifier to identify an deductee Assessee
	 * where PAN is not available This is quoted by the deductor. (A deductee
	 * may have multiple entries in a Statement).
	 * 
	 * @return
	 */
	private String getPANRefNo() {
		return addDelimiter();

	}

	/**
	 * PAN of the deductee. If available should be Valid PAN Format. There may
	 * be deductees who have not been issued PAN however who have applied for a
	 * PAN and have given adequate declaration to the deductor indicating the
	 * same. In such cases, deduction schedule in the statement will not reflect
	 * PAN and instead state PAN Ref. Number for the deductee. The deductor will
	 * however have to mention ‘PANAPPLIED’ in place of PAN. If the deductee is
	 * not sure of the PAN format he will have to mention 'PANINVALID'. However
	 * if the deductee has not given any declaration, deductor will have to
	 * mention ‘PANNOTAVBL’ in place of PAN.
	 * 
	 * @return
	 */
	private String getDeducteePan() {
		if (vendorFinal.getPANno() != null) {
			return vendorFinal.getPANno();
		} else {
			return "PANNOTAVBL";
		}

	}

	/**
	 * Deductee Code 1 for Companies / 2 for other than companies.
	 * 
	 * @param i
	 * 
	 * @return
	 */
	private String getDeducteeCode(int i) {
		String string = codeListArray[i + 1];
		if (string.equals("-")) {
			return "02";
		} else {
			return string;
		}
	}

	// /**
	// * Running serial no to indicate detail record no.
	// *
	// * @return
	// */
	// private String getRunningSerialNumber() {
	// return null;
	// }
	//
	// /**
	// * Running serial number for 'Challan Detail' records in a batch.
	// *
	// * @return
	// */
	// private String getRunningChalanNmber() {
	// return null;
	// }

	/**
	 * Value should be same as 'Batch Number' field in 'Batch Header' record
	 * 
	 * @return
	 */
	private String getBatchNumber() {
		return "1";
	}

	/**
	 * TDS Statement for Non Salary category (Challan / Transfer Voucher Detail
	 * Record)
	 * 
	 * @param lineNumber
	 * @return
	 */
	public String generateChalanVoucherDetailsRecord() {

		String chalanDetailsRecordString = null;
		chalanDetailsRecordString = getLineNumber() + addDelimiter();

		// Record Type
		chalanDetailsRecordString = chalanDetailsRecordString + "CD"
				+ addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getBatchNumber() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getRunningChalanNumber() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getDeducteeRecordsCount() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getNILChalanIndicator() + addDelimiter();

		// Challan Updation Indicator (Not applicable)
		// Filler 3
		// Filler 4
		// Filler 5
		// Last Bank Challan No ( Used for Verification) (Not applicable)
		chalanDetailsRecordString = chalanDetailsRecordString + addDelimiter()
				+ addDelimiter() + addDelimiter() + addDelimiter()
				+ addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getBankChalanNumber() + addDelimiter();

		// Last Transfer Voucher No ( Used for Verification) (Not applicable)
		chalanDetailsRecordString = chalanDetailsRecordString + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTransferVoucherNo() + addDelimiter();

		// Last Bank-Branch Code/ Form 24G Receipt Number ( Used for
		// Verification) (Not applicable)
		chalanDetailsRecordString = chalanDetailsRecordString + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getBankBranchCode() + addDelimiter();

		// Last Date of 'Bank Challan No / Transfer Voucher No' ( Used for
		// Verification) (Not applicable)
		chalanDetailsRecordString = chalanDetailsRecordString + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getDateofBankChalanNo() + addDelimiter();

		// Filler 6
		// Filler 7
		chalanDetailsRecordString = chalanDetailsRecordString + addDelimiter()
				+ addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString + getSection()
				+ addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getOLTASIncomeTax() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getOLTASSurcharge() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString + getOLTASECess()
				+ addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getOLTASInterestAmount() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getOLTASOtherAmount() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTotalOLTASDepositeAmount() + addDelimiter();

		// Last Total of Deposit Amount as per Challan ( Used for Verification)
		// (Not applicable)
		chalanDetailsRecordString = chalanDetailsRecordString + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTotalIncomeTaxinChalan() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTotalIncomeTaxinChalan() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTotalSurchargeinChalan() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTotalECessinChalan() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTDSInterestAMount() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getTDSOtherAmount() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString + getChequeDDNo()
				+ addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getByBookEntry() + addDelimiter();

		chalanDetailsRecordString = chalanDetailsRecordString
				+ getInternalChalanReferenceNo() + addDelimiter();

		// Record Hash (Not applicable)
		chalanDetailsRecordString = chalanDetailsRecordString + addDelimiter();

		return chalanDetailsRecordString + endLine();

	}

	/**
	 * Internal Challan Reference No. or Other comments.
	 * 
	 * @return
	 */
	private String getInternalChalanReferenceNo() {
		return addDelimiter();
	}

	/**
	 * Allowed values - Y/N. If Transfer Voucher Number is provided this is
	 * mandatory and only allowed value is 'Y'. If Bank Challan Number is
	 * provided value 'N' should be provided. However, for a Nil Statement -
	 * value can be either 'Y' / 'N' / Null.
	 * 
	 * @return
	 */
	private String getByBookEntry() {
		if (chalanDetails.isBookEntry()) {
			return "Y";
		} else {
			return "N";
		}

	}

	/**
	 * Mention the Cheque /DD No ,for which the challan is issued . Value should
	 * be "0" where tax is deposited in cash. No value to be provided if value
	 * in field "NIL Challan Indicator" is "Y". No value to be provided if tax
	 * deposited by book entry.
	 * 
	 * @return
	 */
	private String getChequeDDNo() {

		return Long.toString(chalanDetails.getCheckNumber());
	}

	/**
	 * Statement Other amount as per the respective deductee Annexure
	 * 
	 * @return
	 */
	private String getTDSOtherAmount() {
		return Double.toString(chalanDetails.getOtherAmount());
	}

	/**
	 * Statement Interest amount as per the respective deductee Annexure
	 * 
	 * @return
	 */
	private String getTDSInterestAMount() {
		return Double.toString(chalanDetails.getIncomeTaxAmount());
	}

	private String getTotalECessinChalan() {
		return Double.toString(chalanDetails.getEducationCessAmount());
	}

	private String getTotalSurchargeinChalan() {
		return Double.toString(chalanDetails.getSurchangePaidAmount());
	}

	/**
	 * Total sum of column no. 421 for the respective Challan
	 * 
	 * @return
	 */
	private String getTotalIncomeTaxinChalan() {
		return Double.toString(chalanDetails.getIncomeTaxAmount()
				+ chalanDetails.getEducationCessAmount()
				+ chalanDetails.getSurchangePaidAmount());
	}

	/**
	 * Mention the amount of 'Total tax deposited' through Challan. No
	 * fractional portion is allowed in this field (value should be integer) ,
	 * I.e. value "1000.50" will not be allowed, whereas value "1000.00" will be
	 * considered to be valid value. Value in this field should be equal to
	 * total of values in fields with column numbers 403, 404, 405, 406 & 407.
	 * Total of Deposit Amount as per Challan/Transfer Voucher Number ( 'Oltas
	 * TDS/ TCS -Income Tax ' + 'Oltas TDS/ TCS -Surcharge ' + 'Oltas TDS/ TCS -
	 * Cess' + Oltas TDS/ TCS - Interest Amount + Oltas TDS/ TCS - Others
	 * (amount) )
	 * 
	 * @return
	 */
	private String getTotalOLTASDepositeAmount() {
		Double total = chalanDetails.getIncomeTaxAmount()
				+ chalanDetails.getSurchangePaidAmount()
				+ chalanDetails.getEducationCessAmount()
				+ chalanDetails.getInterestPaidAmount()
				+ chalanDetails.getOtherAmount();
		return Double.toString(total);
	}

	/**
	 * Mention the amount of "Other Amount" out of the 'Total tax deposited'
	 * through Challan. No fractional portion is allowed in this field (value
	 * should be integer) , I.e. value "1000.50" will not be allowed, whereas
	 * value "1000.00" will be considered to be valid value.
	 * 
	 * @return
	 */
	private String getOLTASOtherAmount() {
		return Double.toString(chalanDetails.getOtherAmount());
	}

	/**
	 * Mention the amount of "Interest" out of the 'Total tax deposited' through
	 * Challan. No fractional portion is allowed in this field (value should be
	 * integer) , I.e. value "1000.50" will not be allowed, whereas value
	 * "1000.00" will be considered to be valid value.
	 * 
	 * @return
	 */
	private String getOLTASInterestAmount() {
		return Double.toString(chalanDetails.getInterestPaidAmount());
	}

	/**
	 * Mention the amount of "Education Cess" out of the 'Total tax deposited'
	 * through Challan. No fractional portion is allowed in this field (value
	 * should be integer) , I.e. value "1000.50" will not be allowed, whereas
	 * value "1000.00" will be considered to be valid value.
	 * 
	 * @return
	 */
	private String getOLTASECess() {
		return Double.toString(chalanDetails.getEducationCessAmount());
	}

	/**
	 * Mention the amount of "Surcharge" out of the 'Total tax deposited'
	 * through Challan. No fractional portion is allowed in this field (value
	 * should be integer) , I.e. value "1000.50" will not be allowed, whereas
	 * value "1000.00" will be considered to be valid value.
	 * 
	 * @return
	 */
	private String getOLTASSurcharge() {
		return Double.toString(chalanDetails.getSurchangePaidAmount());
	}

	/**
	 * Mention the amount of "Income Tax" out of the 'Total tax deposited'
	 * through Challan. No fractional portion is allowed in this field (value
	 * should be integer) , I.e. value "1000.50" will not be allowed, whereas
	 * value "1000.00" will be considered to be valid value.
	 * 
	 * @return
	 */
	private String getOLTASIncomeTax() {
		return Double.toString(chalanDetails.getIncomeTaxAmount());
	}

	/**
	 * Section Code under which Tax has been deducted. Refer Annexure 2.
	 * 
	 * @return
	 */
	private String getSection() {
		if (chalanDetails.getPaymentSection() != null) {
			return getSectionCode(chalanDetails.getPaymentSection());
		} else {
			return addDelimiter();
		}
	}

	/**
	 * Date of payment of tax to Govt. It cannot be Future Date. Value should be
	 * equal to last date of respective quarter if the value in field
	 * "NIL Challan Indicator" is "Y".
	 * 
	 * @return
	 */
	private String getDateofBankChalanNo() {
		return Long.toString(chalanDetails.getDateTaxPaid());
	}

	/**
	 * In case TDS deposited by 1) Challan:BSR Code of the receiving branch 2)
	 * Transfer voucher: Quote seven digit receipt number provided by AO.
	 * Applicable for govt. deductor/ collector where TDS is deposited by book
	 * entry. 3) No value to be quoted in case of Nil Statement (value in field
	 * "NIL Challan Indicator" field is "Y").
	 * 
	 * @return
	 */
	private String getBankBranchCode() {
		return Long.toString(chalanDetails.getBankBsrCode());
	}

	/**
	 * 1) Applicable only in case of a Government deductor/collector where
	 * TDS/TCS has been deposited by Book entry. 2) Quote the five digit DDO
	 * serial number provided by Accounts Officer (AO) 2) No value should be
	 * present in this column in case of a NIL Statement (value in field
	 * "NIL Challan Indicator" field is "Y")
	 * 
	 * @return
	 */
	private String getTransferVoucherNo() {
		return Long.toString(chalanDetails.getChalanSerialNumber());
	}

	/**
	 * Challan Number issued by Bank . Applicable to both Govt and Non Govt,
	 * Non-Nil statements. No value is required to be provided in case of a NIL
	 * return (I.e. the cases in which the value in field 'NIL Challan
	 * Indicator' is "Y"). Also, no value is required to be provided when some
	 * value in "Transfer Voucher No" field is provided.
	 * 
	 * @return
	 */
	private String getBankChalanNumber() {
		return Long.toString(chalanDetails.getBankChalanNumber());
	}

	/**
	 * Value should be "N". In cases where no tax has been deposited in bank,
	 * value should be "Y" (applicable in case of NIL return)
	 * 
	 * @return
	 */
	private String getNILChalanIndicator() {
		// TODO Auto-generated method stub
		return addDelimiter();
	}

	/**
	 * Count of total number of 'Deductee Detail Records' within e-TDS statement
	 * 
	 * @return
	 */
	private String getDeducteeRecordsCount() {
		return Integer.toString(chalanDetails.getTransactionItems().size());
	}

	public void setFormDetails(String formNo, String quater, String startYear,
			String endYear) {
		super.setFormDetails(formNo, quater, startYear, endYear);
	}

	public void setChalanDetailsList(List<ClientTDSChalanDetail> chalanList2) {
		chalanDetailsList = chalanList2;
		super.setChalanCount(chalanDetailsList.size());
	}

	public int getRunningSerialNumber() {
		return runningSerialNumber;
	}

	public void setRunningSerialNumber(int runningSerialNumber) {
		this.runningSerialNumber = runningSerialNumber;
	}

	/**
	 * @return the runningChalanNumber
	 */
	public int getRunningChalanNumber() {
		return runningChalanNumber;
	}

	/**
	 * @param runningChalanNumber
	 *            the runningChalanNumber to set
	 */
	public void setRunningChalanNumber(int runningChalanNumber) {
		this.runningChalanNumber = runningChalanNumber;
	}

	/**
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * @param lineNumber
	 *            the lineNumber to set
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

}
