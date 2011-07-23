package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.banking.AccountRegisterAction;
import com.vimukti.accounter.web.client.ui.banking.BankingHomeAction;
import com.vimukti.accounter.web.client.ui.banking.ChartsOfAccountsAction;
import com.vimukti.accounter.web.client.ui.banking.CreditCardChargeAction;
import com.vimukti.accounter.web.client.ui.banking.EnterPaymentsAction;
import com.vimukti.accounter.web.client.ui.banking.ImportBankFilesAction;
import com.vimukti.accounter.web.client.ui.banking.MakeDepositAction;
import com.vimukti.accounter.web.client.ui.banking.MatchTrasactionsAction;
import com.vimukti.accounter.web.client.ui.banking.NewBankAccountAction;
import com.vimukti.accounter.web.client.ui.banking.PaymentsAction;
import com.vimukti.accounter.web.client.ui.banking.PrintChecksAction;
import com.vimukti.accounter.web.client.ui.banking.SyncOnlinePayeesAction;
import com.vimukti.accounter.web.client.ui.banking.TransferFundsAction;
import com.vimukti.accounter.web.client.ui.banking.WriteChecksAction;

/**
 * BankingActionFactory contains all static methods, each method returns
 * appropriate Action instance from Banking Section, use to Get All Banking
 * Actions instance.
 * 
 * @author kumar kasimala
 * 
 */
public class BankingActionFactory extends AbstractActionFactory {

	public static BankingHomeAction getBankingHomeAction() {
		return new BankingHomeAction(actionsConstants.bankingHome(),
				Accounter.getFinanceMenuImages().bankingHome()
						.getURL());
	}

	public static NewBankAccountAction getNewBankAccountAction() {
		return new NewBankAccountAction(actionsConstants.newBankCategory(),
				Accounter.getFinanceMenuImages().newBankAccount()
						.getURL());
	}

	public static AccountRegisterAction getAccountRegisterAction() {
		return new AccountRegisterAction(actionsConstants.accountRegister(),
				"/images/icons/banking/account_register.png");

	}

	public static WriteChecksAction getWriteChecksAction() {
		return new WriteChecksAction(actionsConstants.writeCheck(),
				Accounter.getFinanceMenuImages().newCheck().getURL());
	}

	public static WriteChecksAction getWriteChecksAction(
			ClientWriteCheck writeCheck, AsyncCallback<Object> callBackObject) {
		return new WriteChecksAction(actionsConstants.writeCheck(),
				"/images/icons/banking/write_checks.png", writeCheck,
				callBackObject);
	}

	public static MakeDepositAction getMakeDepositAction() {
		return new MakeDepositAction(actionsConstants.makeDeposit(),
				"/images/icons/banking/make_deposit.png");
	}

	public static MakeDepositAction getMakeDepositAction(
			ClientMakeDeposit makeDeposit, AsyncCallback<Object> callBackObject) {
		return new MakeDepositAction(actionsConstants.makeDeposit(),
				"/images/icons/banking/make_deposit.png", makeDeposit,
				callBackObject);
	}

	public static TransferFundsAction getTransferFundsAction() {
		return new TransferFundsAction(actionsConstants.transferFunds(),
				"/images/icons/banking/transfer_funds.png");
	}

	public static TransferFundsAction getTransferFundsAction(
			ClientTransferFund transferFund,
			AsyncCallback<Object> callBackObject) {
		return new TransferFundsAction(actionsConstants.transferFunds(),
				"/images/icons/banking/transfer_funds.png", transferFund,
				callBackObject);
	}

	public static EnterPaymentsAction getEnterPaymentsAction() {
		return new EnterPaymentsAction(actionsConstants.enterPayments(),
				"/images/icons/banking/payments.png");
	}

	public static SyncOnlinePayeesAction getSyncOnlinePayeesAction() {
		return new SyncOnlinePayeesAction(actionsConstants.syncOnlinePayees());
	}

	public static ImportBankFilesAction getImportBankFilesAction() {
		return new ImportBankFilesAction(actionsConstants.importBankFiles());
	}

	public static CreditCardChargeAction getCreditCardChargeAction() {
		return new CreditCardChargeAction(actionsConstants.creditCardCharge(),
				"/images/icons/banking/credit_card_charge.png");
	}

	public static PrintChecksAction getPrintChecksAction() {
		return new PrintChecksAction(actionsConstants.printChecks(),
				"/images/icons/banking/print_checks.png");
	}

	public static ChartsOfAccountsAction getChartsOfAccountsAction() {
		return new ChartsOfAccountsAction(actionsConstants.chartOfAccounts(),
				"/images/icons/banking/chart_of_accounts.png");
	}

	public static PaymentsAction getPaymentsAction() {
		return new PaymentsAction(actionsConstants.payments(),
				"/images/icons/banking/payments.png");
	}

	public static MatchTrasactionsAction getMatchTrasactionsAction() {
		return new MatchTrasactionsAction(actionsConstants.matchTrasactions());
	}
}
