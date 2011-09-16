package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openid4java.OpenIDException;
import org.openid4java.association.AssociationSessionType;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageException;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;
import org.openid4java.util.HttpClientFactory;
import org.openid4java.util.ProxyProperties;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.utils.HibernateUtil;

public class OpenIdServlet extends BaseServlet {

	private static final String OPTIONAL_VALUE = "0";
	private static final String REQUIRED_VALUE = "1";

	private static final String LOGIN_VIEW = "/WEB-INF/login.jsp";
	private ConsumerManager manager;

	/**
	 * {@inheritDoc}
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// --- Forward proxy setup (only if needed) ---
		ProxyProperties proxyProps = getProxyProperties(config);
		if (proxyProps != null) {
			HttpClientFactory.setProxyProperties(proxyProps);
		}

		this.manager = new ConsumerManager();
		manager.setAssociations(new InMemoryConsumerAssociationStore());
		manager.setNonceVerifier(new InMemoryNonceVerifier(5000));
		manager.setMinAssocSessEnc(AssociationSessionType.DH_SHA256);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if ("true".equals(req.getParameter("is_return"))) {
			processReturn(req, resp);
		} else {
			String identifier = req.getParameter("openid_identifier");
			if (identifier != null) {
				this.authRequest(identifier, req, resp);
			} else {
				this.getServletContext()
						.getRequestDispatcher("/WEB-INF/openid.jsp")
						.forward(req, resp);
			}
		}
	}

	// --- placing the authentication request ---
	public String authRequest(String userSuppliedString,
			HttpServletRequest httpReq, HttpServletResponse httpResp)
			throws IOException, ServletException {
		try {
			// configure the return_to URL where your application will receive
			// the authentication responses from the OpenID provider
			// String returnToUrl = "http://example.com/openid";
			String returnToUrl = httpReq.getRequestURL().toString()
					+ "?is_return=true";

			// perform discovery on the user-supplied identifier
			List discoveries = manager.discover(userSuppliedString);

			// attempt to associate with the OpenID provider
			// and retrieve one service endpoint for authentication
			DiscoveryInformation discovered = manager.associate(discoveries);

			// store the discovery information in the user's session
			httpReq.getSession().setAttribute("openid-disc", discovered);

			// obtain a AuthRequest message to be sent to the OpenID provider
			AuthRequest authReq = manager.authenticate(discovered, returnToUrl);

			// Simple registration example
			addSimpleRegistrationToAuthRequest(httpReq, authReq);

			// Attribute exchange example
			addAttributeExchangeToAuthRequest(httpReq, authReq);

			if (!discovered.isVersion2()) {
				// Option 1: GET HTTP-redirect to the OpenID Provider endpoint
				// The only method supported in OpenID 1.x
				// redirect-URL usually limited ~2048 bytes
				httpResp.sendRedirect(authReq.getDestinationUrl(true));
				return null;
			} else {
				// Option 2: HTML FORM Redirection (Allows payloads >2048 bytes)

				RequestDispatcher dispatcher = getServletContext()
						.getRequestDispatcher("/WEB-INF/formredirection.jsp");
				httpReq.setAttribute("prameterMap", httpReq.getParameterMap());
				httpReq.setAttribute("message", authReq);
				// httpReq.setAttribute("destinationUrl", httpResp
				// .getDestinationUrl(false));
				dispatcher.forward(httpReq, httpResp);
			}
		} catch (OpenIDException e) {
			// present error to the user
			throw new ServletException(e);
		}

		return null;
	}

	/**
	 * Simple Registration Extension example.
	 * 
	 * @param httpReq
	 * @param authReq
	 * @throws MessageException
	 * @see <a href="http://code.google.com/p/openid4java/wiki/SRegHowTo">Simple
	 *      Registration HowTo</a>
	 * @see <a
	 *      href="http://openid.net/specs/openid-simple-registration-extension-1_0.html">OpenID
	 *      Simple Registration Extension 1.0</a>
	 */
	private void addSimpleRegistrationToAuthRequest(HttpServletRequest httpReq,
			AuthRequest authReq) throws MessageException {
		// Attribute Exchange example: fetching the 'email' attribute
		// FetchRequest fetch = FetchRequest.createFetchRequest();
		SRegRequest sregReq = SRegRequest.createFetchRequest();

		String[] attributes = { "nickname", "email", "fullname", "dob",
				"gender", "postcode", "country", "language", "timezone" };
		for (int i = 0, l = attributes.length; i < l; i++) {
			String attribute = attributes[i];
			String value = httpReq.getParameter(attribute);
			if (OPTIONAL_VALUE.equals(value)) {
				sregReq.addAttribute(attribute, false);
			} else if (REQUIRED_VALUE.equals(value)) {
				sregReq.addAttribute(attribute, true);
			}
		}

		// attach the extension to the authentication request
		if (!sregReq.getAttributes().isEmpty()) {
			authReq.addExtension(sregReq);
		}
	}

	/**
	 * Attribute exchange example.
	 * 
	 * @param httpReq
	 * @param authReq
	 * @throws MessageException
	 * @see <a
	 *      href="http://code.google.com/p/openid4java/wiki/AttributeExchangeHowTo">Attribute
	 *      Exchange HowTo</a>
	 * @see <a
	 *      href="http://openid.net/specs/openid-attribute-exchange-1_0.html">OpenID
	 *      Attribute Exchange 1.0 - Final</a>
	 */
	private void addAttributeExchangeToAuthRequest(HttpServletRequest httpReq,
			AuthRequest authReq) throws MessageException {
		FetchRequest fetch = FetchRequest.createFetchRequest();
		fetch.addAttribute("email", "http://axschema.org/contact/email", true,
				1);
		authReq.addExtension(fetch);
	}

	private void processReturn(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Identifier identifier = this.verifyResponse(req);
		if (identifier == null) {
			this.getServletContext()
					.getRequestDispatcher("/WEB-INF/openid.jsp")
					.forward(req, resp);
		} else {
			req.setAttribute("identifier", identifier.getIdentifier());
			String email = (String) req.getAttribute("email");
			loginForUser(email, req, resp);
		}
	}

	private void loginForUser(String email, HttpServletRequest request,
			HttpServletResponse response) {
		Session session = HibernateUtil.openSession(LOCAL_DATABASE);
		Transaction transaction = session.beginTransaction();
		try {
			Client client = (Client) session
					.getNamedQuery("getClient.by.mailId")
					.setString("emailId", email).uniqueResult();
			if (client != null) {
				// if valid credentials are there we redirect to <dest> param or
				// /companies

				if (!client.isActive()) {
					client.setActive(true);
				} else {
					if (client.isRequirePasswordReset()) {
						client.setRequirePasswordReset(false);
						session.saveOrUpdate(client);
					}

					String destUrl = request.getParameter(PARAM_DESTINATION);
					HttpSession httpSession = request.getSession();
					httpSession.setAttribute(EMAIL_ID, client.getEmailId());
					if (destUrl == null || destUrl.isEmpty()) {
						client.setLoginCount(client.getLoginCount() + 1);
						client.setLastLoginTime(System.currentTimeMillis());
						session.saveOrUpdate(client);
						redirectExternal(request, response, COMPANIES_URL);
					} else {
						redirectExternal(request, response, destUrl);
					}

				}
			} else {
				request.setAttribute("message",
						"No account exists with this emailid.");
				dispatch(request, response, LOGIN_VIEW);
			}
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	// --- processing the authentication response ---
	public Identifier verifyResponse(HttpServletRequest httpReq)
			throws ServletException {
		try {
			// extract the parameters from the authentication response
			// (which comes in as a HTTP request from the OpenID provider)
			ParameterList response = new ParameterList(
					httpReq.getParameterMap());

			// retrieve the previously stored discovery information
			DiscoveryInformation discovered = (DiscoveryInformation) httpReq
					.getSession().getAttribute("openid-disc");

			// extract the receiving URL from the HTTP request
			StringBuffer receivingURL = httpReq.getRequestURL();
			String queryString = httpReq.getQueryString();
			if (queryString != null && queryString.length() > 0)
				receivingURL.append("?").append(queryString);

			// verify the response; ConsumerManager needs to be the same
			// (static) instance used to place the authentication request
			VerificationResult verification = manager.verify(
					receivingURL.toString(), response, discovered);

			// examine the verification result and extract the verified
			// identifier
			Identifier verified = verification.getVerifiedId();
			if (verified != null) {
				AuthSuccess authSuccess = (AuthSuccess) verification
						.getAuthResponse();

				receiveSimpleRegistration(httpReq, authSuccess);

				receiveAttributeExchange(httpReq, authSuccess);

				return verified; // success
			}
		} catch (OpenIDException e) {
			// present error to the user
			throw new ServletException(e);
		}

		return null;
	}

	/**
	 * @param httpReq
	 * @param authSuccess
	 * @throws MessageException
	 */
	private void receiveSimpleRegistration(HttpServletRequest httpReq,
			AuthSuccess authSuccess) throws MessageException {
		if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG)) {
			MessageExtension ext = authSuccess
					.getExtension(SRegMessage.OPENID_NS_SREG);
			if (ext instanceof SRegResponse) {
				SRegResponse sregResp = (SRegResponse) ext;
				for (Iterator iter = sregResp.getAttributeNames().iterator(); iter
						.hasNext();) {
					String name = (String) iter.next();
					String value = sregResp.getParameterValue(name);
					httpReq.setAttribute(name, value);
				}
			}
		}
	}

	/**
	 * @param httpReq
	 * @param authSuccess
	 * @throws MessageException
	 */
	private void receiveAttributeExchange(HttpServletRequest httpReq,
			AuthSuccess authSuccess) throws MessageException {
		if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
			FetchResponse fetchResp = (FetchResponse) authSuccess
					.getExtension(AxMessage.OPENID_NS_AX);

			List emails = fetchResp.getAttributeValues("email");
			String email = (String) emails.get(0);

			// List aliases = fetchResp.getAttributeAliases();
			// Map attributes = new LinkedHashMap();
			// for (Iterator iter = aliases.iterator(); iter.hasNext();) {
			// String alias = (String) iter.next();
			// List values = fetchResp.getAttributeValues(alias);
			// if (values.size() > 0) {
			// String[] arr = new String[values.size()];
			// values.toArray(arr);
			// attributes.put(alias, StringUtils.join(arr));
			// }
			// }
			httpReq.setAttribute("email", email);
		}
	}

	/**
	 * Get proxy properties from the context init params.
	 * 
	 * @return proxy properties
	 */
	private static ProxyProperties getProxyProperties(ServletConfig config) {
		ProxyProperties proxyProps;
		String host = config.getInitParameter("proxy.host");
		if (host == null) {
			proxyProps = null;
		} else {
			proxyProps = new ProxyProperties();
			String port = config.getInitParameter("proxy.port");
			String username = config.getInitParameter("proxy.username");
			String password = config.getInitParameter("proxy.password");
			String domain = config.getInitParameter("proxy.domain");
			proxyProps.setProxyHostName(host);
			proxyProps.setProxyPort(Integer.parseInt(port));
			proxyProps.setUserName(username);
			proxyProps.setPassword(password);
			proxyProps.setDomain(domain);
		}
		return proxyProps;
	}
}
