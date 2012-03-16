package com.vimukti.accounter.web.client.ui;

public class AccounterInitialiser {

	Accounter accounter;

//	public AccounterInitialiser(Accounter accounter) {
//		accounter.loadCompany();
//	}

	public AccounterInitialiser() {
		// TODO Auto-generated constructor stub
	}

	public void initalize() {
		Accounter.loadCompany();
	}

}
