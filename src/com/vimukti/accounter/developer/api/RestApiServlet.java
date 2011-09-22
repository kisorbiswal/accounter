package com.vimukti.accounter.developer.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.api.core.ApiResult;
import com.vimukti.accounter.core.Developer;
import com.vimukti.accounter.core.Server;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.server.AccounterCRUDServiceImpl;

public class RestApiServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int STATUS_SUCCESS = 200;
	private static int STATUS_CREATED = 201;
	private static int STATUS_INTERNAL_ERROR = 500;
	private static int STATUS_NOT_FOUND = 404;

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		boolean isSuccess = false;
		ServletInputStream inputStream = req.getInputStream();
		ApiResult apiResult = new ApiResult();
		try {
			IAccounterCore accounterCore = getApiSerializationFactory()
					.deserialize(inputStream);
			boolean delete = new AccounterCRUDServiceImpl().delete(
					accounterCore.getObjectType(), accounterCore.getID());
			if (delete) {
				apiResult.setStatus(STATUS_SUCCESS);
			} else {
				apiResult.setStatus(STATUS_NOT_FOUND);
			}
			isSuccess = delete;
		} catch (Exception e) {
			e.printStackTrace();
			apiResult.setStatus(STATUS_INTERNAL_ERROR);
			isSuccess = false;
		}
		String result = getApiSerializationFactory().serializeResult(apiResult);
		ServletOutputStream outputStream = resp.getOutputStream();
		outputStream.write(result.getBytes());
		updateDeveloper(req, isSuccess);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		boolean isSuccess = false;
		ServletInputStream inputStream = req.getInputStream();
		ApiResult apiResult = new ApiResult();
		try {
			IAccounterCore deserialize = getApiSerializationFactory()
					.deserialize(inputStream);
			long create = new AccounterCRUDServiceImpl().update(deserialize);

			apiResult.setStatus(STATUS_SUCCESS);
			apiResult.setObjectId(create);
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
			apiResult.setStatus(STATUS_INTERNAL_ERROR);
			isSuccess = false;
		}
		String result = getApiSerializationFactory().serializeResult(apiResult);
		ServletOutputStream outputStream = resp.getOutputStream();
		outputStream.write(result.getBytes());
		updateDeveloper(req, isSuccess);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		boolean isSuccess = false;
		ServletInputStream inputStream = req.getInputStream();
		ApiResult apiResult = new ApiResult();
		try {
			IAccounterCore deserialize = getApiSerializationFactory()
					.deserialize(inputStream);
			long create = new AccounterCRUDServiceImpl().create(deserialize);

			apiResult.setStatus(STATUS_CREATED);
			apiResult.setObjectId(create);
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
			apiResult.setStatus(STATUS_INTERNAL_ERROR);
			isSuccess = false;
		}
		String result = getApiSerializationFactory().serializeResult(apiResult);
		ServletOutputStream outputStream = resp.getOutputStream();
		outputStream.write(result.getBytes());

		updateDeveloper(req, isSuccess);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO get the companyID and set it to the request attributes
		Session session = HibernateUtil.openSession();
		try {
			super.service(req, resp);
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	private ApiSerializationFactory getApiSerializationFactory() {
		// TODO
		return null;
	}

	public void updateDeveloper(HttpServletRequest req, boolean isSuccess) {
		Developer developer = null;
		long id = ((Long) req.getAttribute("id")).longValue();
		Session session = HibernateUtil.openSession();
		Transaction transaction = session.beginTransaction();
		try {
			developer = (Developer) session.get(Developer.class, id);
			if (isSuccess) {
				developer.succeedRequests++;
			} else {
				developer.failureRequests++;
			}
			session.saveOrUpdate(developer);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}
}