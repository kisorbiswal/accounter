package com.vimukti.accounter.developer.api;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.vimukti.accounter.developer.api.process.ApiProcessor;
import com.vimukti.accounter.developer.api.process.CreateProcessor;
import com.vimukti.accounter.developer.api.process.DeleteProcessor;
import com.vimukti.accounter.developer.api.process.ObjectListProcessor;
import com.vimukti.accounter.developer.api.process.ReadProcessor;
import com.vimukti.accounter.developer.api.process.ReportsProcessor;
import com.vimukti.accounter.developer.api.process.TransactionListProcessor;
import com.vimukti.accounter.developer.api.process.UpdateProcessor;
import com.vimukti.accounter.utils.HibernateUtil;

public class ApiBaseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doProcess(HttpServletRequest req, HttpServletResponse resp,
			String type) throws IOException {
		ApiProcessor processor = getApiProcessor(type);
		if (processor == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong type");
			return;
		}
		Session session = HibernateUtil.openSession();
		try {
			processor.process(req, resp);
			processor.sendData(req,resp);
			
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	protected ApiProcessor getApiProcessor(String type) {
		if (type.equals("objects")) {
			return new ObjectListProcessor();
		} else if (type.equals("transactions")) {
			return new TransactionListProcessor();
		} else if (type.equals("reports")) {
			return new ReportsProcessor();
		} else if (type.equals("create")) {
			return new CreateProcessor();
		} else if (type.equals("update")) {
			return new UpdateProcessor();
		} else if (type.equals("read")) {
			return new ReadProcessor();
		} else if (type.equals("delete")) {
			return new DeleteProcessor();
		}
		return null;
	}
}
