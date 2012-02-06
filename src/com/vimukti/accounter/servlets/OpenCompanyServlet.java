package com.vimukti.accounter.servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.ActivityType;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.UTF8Control;
import com.vimukti.accounter.web.server.FinanceTool;

public class OpenCompanyServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Log logger = LogFactory
			.getLog(OpenCompanyServlet.class);
	private static final String REDIRECT_PAGE = "/WEB-INF/Redirect.jsp";
	private static final String USER_NAME = "userName";
	private static final String COMPANY_NAME = "companyName";
	private static final String SUBSCRIPTION = "subscription";
	private static final String USER_AGENT = "User-Agent";
	private static final String SUPPORTED_BROWSERS_URL = "/WEB-INF/supportedbrowsers.jsp";
	private static final String FILE_NAME = "config/SupportedBrowsers.txt";

	static Set<Pattern> patterns = new HashSet<Pattern>();

	static {
		File fileToRead = new File(FILE_NAME);
		if (fileToRead != null)
			if (fileToRead.exists()) {
				try {
					FileReader fr = new FileReader(fileToRead);
					BufferedReader br = new BufferedReader(fr);
					while (true) {
						String line = br.readLine();
						if (line == null)
							break;
						patterns.add(Pattern.compile(line.trim()));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String header = request.getHeader(USER_AGENT);
		if (header != null)
			if (!isSupportedBrowser(header)) {
				dispatch(request, response, SUPPORTED_BROWSERS_URL);
				return;
			}

		String url = request.getRequestURI().toString();
		String isTouch = (String) request.getSession().getAttribute(IS_TOUCH);
		request.setAttribute(IS_TOUCH, isTouch == null ? "false" : isTouch);

		if (url.equals(ACCOUNTER_OLD_URL)) {
			dispatch(request, response, REDIRECT_PAGE);
			return;
		}
		String emailID = (String) request.getSession().getAttribute(EMAIL_ID);

		if (emailID != null) {
			Session session = HibernateUtil.getCurrentSession();
			FinanceTool financeTool = new FinanceTool();
			Query namedQuery = session.getNamedQuery("getClient.by.mailId");
			namedQuery.setParameter(BaseServlet.EMAIL_ID, emailID);
			Client client = (Client) namedQuery.uniqueResult();
			String language = "";
			try {
				language = getlocale().getISO3Language();
			} catch (MissingResourceException e) {
				language = "eng";
			}
			HashMap<String, String> keyAndValues = financeTool.getKeyAndValues(
					client.getID(), language);

			for (Entry<String, String> entrySet : keyAndValues.entrySet()) {
				String value = entrySet.getValue();
				if (value.contains("'")) {
					value = value.replace("'", "\\'");
					entrySet.setValue(value);
				}
			}

			request.setAttribute("messages", keyAndValues);
			// Load locale aware date time constants, number format constants

			HashMap<String, String> accounterLocale = getLocaleConstants();
			request.setAttribute("accounterLocale", accounterLocale);

			Long serverCompanyID = (Long) request.getSession().getAttribute(
					COMPANY_ID);
			String create = (String) request.getSession().getAttribute(CREATE);
			if (serverCompanyID == null) {
				if (create != null && create.equals("true")) {
					RequestDispatcher dispatcher = getServletContext()
							.getRequestDispatcher("/WEB-INF/Accounter.jsp");
					dispatcher.forward(request, response);
					return;
				} else {
					response.sendRedirect(COMPANIES_URL);
					return;
				}
			}

			// initComet(request.getSession(), serverCompanyID, emailID);
			// we are create comet in getCompany method

			try {
				Transaction transaction = session.beginTransaction();

				HttpSession httpSession = request.getSession();
				Boolean isSupportedUser = (Boolean) httpSession
						.getAttribute(IS_SUPPORTED_USER);
				User user = null;
				if (isSupportedUser) {
					Company company = getCompany(request);
					Set<User> users = company.getUsers();
					for (User u : users) {
						if (u.isAdmin()) {
							user = u;
							httpSession.setAttribute(EMAIL_ID, user.getClient()
									.getEmailId());
							httpSession.setAttribute(USER_ID, user.getID());
							break;
						}
					}
				} else {
					user = getUser(emailID, serverCompanyID);
				}
				if (user == null) {
					response.sendRedirect(COMPANIES_URL);
					return;
				}

				user = HibernateUtil.initializeAndUnproxy(user);
				request.setAttribute(EMAIL_ID, user.getClient().getEmailId());
				request.setAttribute(USER_ID, user.getID());
				request.setAttribute(USER_NAME, user.getClient().getFullName());

				AccounterThreadLocal.set(user);
				if (getCompanySecretFromDB(serverCompanyID) != null) {
					try {
						if (user.getSecretKey() == null) {
							dispatch(request, response,
									"/WEB-INF/companypassword.jsp");
							return;
						}
						EU.createCipher(user.getSecretKey(), getD2(request),
								emailID);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				Company company = getCompany(request);
				request.setAttribute(COMPANY_NAME, company.getDisplayName()
						+ " - " + company.getID());
				if (!isSupportedUser) {
					Activity activity = new Activity(getCompany(request), user,
							ActivityType.LOGIN);
					session.save(activity);
				}
				transaction.commit();
				if (client.getSubscription() != null) {
					request.setAttribute(SUBSCRIPTION, client.getSubscription()
							.getId());
				}
				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher("/WEB-INF/Accounter.jsp");
				dispatcher.forward(request, response);
			} finally {
				EU.removeCipher();
			}
		} else {
			response.sendRedirect(LOGIN_URL);
			// Session is there, so show the main page

		}
	}

	private boolean isSupportedBrowser(String header) {

		for (Pattern p : patterns) {
			Matcher matcher = p.matcher(header);
			if (matcher.matches()) {
				return true;
			}
		}

		return false;
	}

	private HashMap<String, String> getLocaleConstants() {
		HashMap<String, String> result = new HashMap<String, String>();

		String files[] = { "DateTimeConstantsImpl", "NumberConstantsImpl" };

		for (String file : files) {
			ResourceBundle dateTimeConstants = ResourceBundle.getBundle(
					"com.vimukti.accounter.web.server.i18n.constants." + file,
					getlocale(), new UTF8Control());
			for (String key : dateTimeConstants.keySet()) {
				String value = dateTimeConstants.getString(key);
				String[] splites = value.split("(?<!\\\\), ");
				if (splites.length == 1) {
					result.put(key, getValue(value));
				} else {
					result.put(key, joinArray(splites));
				}
			}
		}

		return result;
	}

	private String joinArray(String[] value) {
		StringBuffer buffer = new StringBuffer();
		buffer.append('[');
		if (value.length > 0) {
			for (int x = 0; x < value.length - 1; x++) {
				buffer.append('\'');
				buffer.append(getValue(value[x]));
				buffer.append('\'');
				buffer.append(", ");
			}
			// add last one
			buffer.append('\'');
			buffer.append(getValue(value[value.length - 1]));
			buffer.append('\'');
		}
		buffer.append(']');
		return buffer.toString();
	}

	private String getValue(String string) {
		return string.replaceAll("\'", "\\\\\'");
	}

	private Locale getlocale() {
		return ServerLocal.get();
	}

	// /**
	// * Initialising comet stuff
	// *
	// * @param request
	// * @param identity
	// */
	// private void initComet(HttpSession httpSession, long companyID,
	// String emailID) {
	// // Stream must be created otherwise user will get data
	// // Continuously and browser will struck
	// CometSession cometSession = CometServlet.getCometSession(httpSession);
	// CometManager.initStream(httpSession.getId(), companyID, emailID,
	// cometSession);
	// }

}
