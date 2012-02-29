package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.vimukti.accounter.developer.api.core.ApiResult;
import com.vimukti.accounter.developer.api.process.ApiProcessor;
import com.vimukti.accounter.developer.api.process.CreateProcessor;
import com.vimukti.accounter.developer.api.process.DeleteProcessor;
import com.vimukti.accounter.developer.api.process.ReadProcessor;
import com.vimukti.accounter.developer.api.process.UpdateProcessor;
import com.vimukti.accounter.developer.api.process.lists.CustomerProcessor;
import com.vimukti.accounter.developer.api.process.lists.InvoicesProcessor;
import com.vimukti.accounter.utils.HibernateUtil;

public class ApiBaseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Map<String, ApiProcessor> processors = new HashMap<String, ApiProcessor>();

	@Override
	public void init() throws ServletException {
		// CRUD
		processors.put("create", new CreateProcessor());
		processors.put("read", new ReadProcessor());
		processors.put("update", new UpdateProcessor());
		processors.put("delete", new DeleteProcessor());

		// Lists
		processors.put("customers", new CustomerProcessor());

		processors.put("invoices", new InvoicesProcessor());

		// Reports
		processors.put("salesbycustomersummary", null);
		super.init();
	}

	protected void doProcess(HttpServletRequest req, HttpServletResponse resp,
			String type) throws IOException {
		ApiProcessor processor = processors.get(type);
		if (processor == null) {
			sendFail(req, resp, "Wrong request.");
			return;
		}
		Session session = HibernateUtil.openSession();
		try {
			processor.process(req, resp);
			processor.sendData(req, resp);

		} catch (Exception e) {
			sendFail(req, resp, e.getMessage());
			return;
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	protected void sendFail(HttpServletRequest req, HttpServletResponse resp,
			String result) {
		try {
			ApiResult apiResult = new ApiResult();
			apiResult.setStatus(ApiResult.FAIL);
			apiResult.setResult(result);
			ApiSerializationFactory factory = new ApiSerializationFactory(false);
			String string = factory.serialize(apiResult);
			ServletOutputStream outputStream;
			outputStream = resp.getOutputStream();
			outputStream.write(string.getBytes());
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
