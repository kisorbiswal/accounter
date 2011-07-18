package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;

import com.bizantra.server.internal.core.BizantraCompany;
import com.bizantra.server.main.Server;
import com.bizantra.server.storage.HibernateUtil;

/**
 * @author Sai Karthik K
 * 
 */
public class BizantraCompaniesStatisticsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		getStatisticsOfCompanies(req, resp);
	}

	private void getStatisticsOfCompanies(HttpServletRequest req,
			HttpServletResponse resp) {

		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);

		List<BizantraCompany> companies = (ArrayList<BizantraCompany>) session
				.getNamedQuery("get.all.companies").list();
		session.close();

		HashMap<String, HashMap<String, List<String>>> map = new HashMap<String, HashMap<String, List<String>>>();

		HashMap<String, List<String>> attrbs;

		List<String> usedStatistics;
		List<String> totalStatistics;

		for (BizantraCompany company : companies) {

			usedStatistics = new ArrayList<String>();
			totalStatistics = new ArrayList<String>();
			attrbs = new HashMap<String, List<String>>();
			String companyDomainName = company.getCompanyDomainName();

			session = HibernateUtil.openSession(companyDomainName);

			int usedfullUsers = getActivatedFullUsersSize(session);
			int usedliteUsers = getActivatedLiteUsersSize(session);
			long usedSize = (Long) session.getNamedQuery(
					"get.used.dataSize.from.company").setString("name",
					companyDomainName).uniqueResult();

			int totalNoOfFiles = getTotalNoOfFiles(session);
			int totalNoOfDiscussions = getTotalNoOfDiscussions(session);
			int totalNoOfWorkspaces = getTotalNoOfWorkspaces(session);
			int totalNoOfCommands = getTotalNoOfExecutedCommands(session);
			int loginCount = getTotalLoginCount(session);
			Timestamp lastLoginDate = getLastLoginDate(session);

			String lastLoginInstance = null;
			if (lastLoginDate != null) {

				// SimpleDateFormat formatter = new SimpleDateFormat(
				// "dd-MMM-YYYY hh-mm-ss");
				// lastLoginInstance = formatter.format(lastLoginDate);
				lastLoginInstance = String.valueOf(lastLoginDate.getTime());

			} else {
				lastLoginInstance = String.valueOf(0);
			}

			usedStatistics.add(String.valueOf(usedfullUsers));
			usedStatistics.add(String.valueOf(usedliteUsers));
			usedStatistics.add(String.valueOf(usedSize));

			attrbs.put("usedStatistics", usedStatistics);

			int totalNoOfUsers = company.getTotalNoOfUsers();
			int totalNoOfLiteUsers = company.getTotalNoOfLiteUsers();
			long totalStorage = company.getTotalSize();

			totalStatistics.add(String.valueOf(totalNoOfUsers));
			totalStatistics.add(String.valueOf(totalNoOfLiteUsers));
			totalStatistics.add(String.valueOf(totalStorage));
			totalStatistics.add(String.valueOf(totalNoOfFiles));
			totalStatistics.add(String.valueOf(totalNoOfDiscussions));
			totalStatistics.add(String.valueOf(totalNoOfWorkspaces));
			totalStatistics.add(String.valueOf(totalNoOfCommands));
			totalStatistics.add(String.valueOf(loginCount));
			totalStatistics.add(String.valueOf(lastLoginInstance));

			attrbs.put("totalStatistics", totalStatistics);

			map.put(companyDomainName, attrbs);

		}
		dispatchResponse(map, req, resp);

	}

	private void dispatchResponse(
			HashMap<String, HashMap<String, List<String>>> map,
			HttpServletRequest req, HttpServletResponse resp) {

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(map.toString());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		String jsonString = "{jsonObject:" + jsonObject.toString() + "}";
		try {
			resp.getWriter().write(jsonString);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private int getActivatedFullUsersSize(Session session) {
		session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("active_userscount");
		return ((BigInteger) query.uniqueResult()).intValue();
	}

	private int getActivatedLiteUsersSize(Session session) {
		session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("lite_userscount");
		return ((BigInteger) query.uniqueResult()).intValue();
	}

	private int getTotalNoOfFiles(Session session) {
		session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("get.total.files.count");
		return ((Integer) query.uniqueResult());
	}

	private int getTotalNoOfDiscussions(Session session) {
		session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("get.total.discussions.count");
		return ((Integer) query.uniqueResult());
	}

	private int getTotalNoOfWorkspaces(Session session) {
		session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("get.total.workspaces.count");
		return ((Integer) query.uniqueResult());
	}

	private int getTotalNoOfExecutedCommands(Session session) {
		session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("get.total.commands.count");
		return ((Integer) query.uniqueResult());
	}

	private int getTotalLoginCount(Session session) {
		session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("get.total.login.count");
		return ((Integer) query.list().get(0));
	}

	private Timestamp getLastLoginDate(Session session) {
		session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("get.last.login.date");
		return ((Timestamp) query.list().get(0));
	}

}
