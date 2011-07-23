package com.vimukti.accounter.web.client.commet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;
import com.vimukti.accounter.web.client.IAccounterGETService;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.comet.client.CometClient;
import com.vimukti.comet.client.ICometListener;

public class AccounterCometClient {
	private Accounter accounter;

	public AccounterCometClient(Accounter accounter) {
		this.accounter = accounter;
	}

	public void start() {
		final SerializationStreamFactory ssf = GWT
				.create(IAccounterGETService.class);
		CometClient.register("", Accounter.getCustomersMessages().accounter(),
				new ICometListener() {

					public void onPayload(final Object obj) {
						processCommand((IAccounterCore) obj);
					}

					@Override
					public void onTerminate() {
					}

					@Override
					public void onReset() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStatusChange(int status) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSyncStatusChange(int status) {
						// TODO Auto-generated method stub
					}
				}, ssf);
	}

	protected void processCommand(IAccounterCore obj) {
		accounter.getCompany().processCommand(obj);
	}

	public void cometStop() {
		CometClient
				.unRegister("", Accounter.getCustomersMessages().accounter());
	}

}
