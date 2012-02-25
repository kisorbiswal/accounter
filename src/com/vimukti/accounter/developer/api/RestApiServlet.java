package com.vimukti.accounter.developer.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.api.core.ApiResult;
import com.vimukti.accounter.core.Developer;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.IAccounterCRUDService;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.server.AccounterCRUDServiceImpl;

public class RestApiServlet extends ApiBaseServlet {

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
			IAccounterCore accounterCore = getSerializationFactory(req)
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
		String result = getSerializationFactory(req).serializeResult(apiResult);
		ServletOutputStream outputStream = resp.getOutputStream();
		outputStream.write(result.getBytes());
		updateDeveloper(req, isSuccess);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		boolean isSuccess = false;
		ServletInputStream inputStream = req.getInputStream();
		ApiResult apiResult = new ApiResult();
		try {
			IAccounterCore deserialize = getSerializationFactory(req)
					.deserialize(inputStream);
			long create = ((IAccounterCRUDService) getS2sSyncProxy(req,
					"/do/accounter/crud/rpc/service",
					IAccounterCRUDService.class)).update(deserialize);

			apiResult.setStatus(STATUS_SUCCESS);
			apiResult.setObjectId(create);
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
			apiResult.setStatus(STATUS_INTERNAL_ERROR);
			isSuccess = false;
		}
		String result = getSerializationFactory(req).serializeResult(apiResult);
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
			IAccounterCore deserialize = getSerializationFactory(req)
					.deserialize(inputStream);
			long create = ((IAccounterCRUDService) getS2sSyncProxy(req,
					"/do/accounter/crud/rpc/service",
					IAccounterCRUDService.class)).create(deserialize);

			apiResult.setStatus(STATUS_CREATED);
			apiResult.setObjectId(create);
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
			apiResult.setStatus(STATUS_INTERNAL_ERROR);
			isSuccess = false;
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		String result = getSerializationFactory(req).serializeResult(apiResult);
		ServletOutputStream outputStream = resp.getOutputStream();
		outputStream.write(result.getBytes());

		updateDeveloper(req, isSuccess);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Session session = HibernateUtil.openSession();
		try {
			super.service(req, resp);
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	public void updateDeveloper(HttpServletRequest req, boolean isSuccess) {
		Developer developer = null;
		long id = ((Long) req.getAttribute("id")).longValue();
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
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
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}
}