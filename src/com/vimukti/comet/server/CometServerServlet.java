package com.vimukti.comet.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Request;
import org.mortbay.util.ajax.Continuation;
import org.mortbay.util.ajax.ContinuationSupport;

import com.bizantra.server.main.LocalServer;
import com.bizantra.server.main.Server;
import com.bizantra.server.sock.c2s.client.ClientPacketHandler;

public class CometServerServlet extends HttpServlet {

	/**
	 * 
	 */

	public static HashMap<String, Long> map = new HashMap<String, Long>();
	private static final long serialVersionUID = 1L;
	private static final int MODE_XHRINTERACTIVE = 1;
	private static final int MODE_SMARTPOLL = 2;
	private static final int MODE_LONGPOLL = 3;
	private static final int MODE_IFRAME = 4;
	private static final int MODE_SIMPLEPOLL = 5;

	Logger log = Logger.getLogger(CometServerServlet.class);

	/**
	 * This servlet will drop the connection when more than this number of bytes
	 * is written.
	 */
	private int maximumBytesWritten;

	/**
	 * This servlet will drop the connection when the connection has been open
	 * for more than this amount of time in milliseconds.
	 */
	private int connectionTimeout;

	// private boolean continued;

	final static ThreadLocal<HttpServletRequest> httpServletRequests = new ThreadLocal<HttpServletRequest>();

	final static ThreadLocal<HttpServletResponse> httpServletResponses = new ThreadLocal<HttpServletResponse>();
	private static final long SLEEP_TIME = 5000;

	public static void closeIfNecessary(final OutputStream stream) {
		if (stream != null) {
			try {
				stream.flush();
			} catch (final IOException closing) {
				// closing.printStackTrace();
			}

			try {
				stream.close();
			} catch (final IOException closing) {
				// closing.printStackTrace();
			}
		}
	}

	public static String htmlEncode(final String plainText) {

		final StringBuffer buf = new StringBuffer();
		final int length = plainText.length();
		for (int i = 0; i < length; i++) {
			final char c = plainText.charAt(i);

			if ('<' == c) {
				buf.append("&lt;");
				continue;
			}
			if ('>' == c) {
				buf.append("&gt;");
				continue;
			}
			if ('&' == c) {
				buf.append("&amp;");
				continue;
			}
			// if ('\'' == c) {
			// buf.append("&apos;");
			// continue;
			// }
			if ('"' == c) {
				buf.append("&quot;");
				continue;
			}
			buf.append(c);
		}

		return buf.toString();
	}

	public static boolean isNullOrEmpty(final String string) {
		return string == null || string.length() == 0;
	}

	void clearHttpServletRequest() {
		CometServerServlet.httpServletRequests.remove();
	}

	void clearHttpServletResponse() {
		CometServerServlet.httpServletResponses.remove();
	}

	/**
	 * The main method that takes care of initiating a comet session.
	 */
	@Override
	public void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException,
			ServletException {
		String identityID = (String) request.getSession().getAttribute(
				"identityID");
		String sessionID = request.getSession().getId();

		String sessionKey = identityID + sessionID;

		String ncTime = request.getParameter("nc");
		if (identityID == null) {
			// System.out.println("<script>eof();</script>");
			response.setCharacterEncoding("UTF-8");
			ServletOutputStream servletOutputStream = response
					.getOutputStream();
			servletOutputStream.println("<script>eof();</script>");
			servletOutputStream.flush();
			servletOutputStream.close();
			// log.info("Session Expired...sending EOF");
			return;
		}
		if (ncTime == null) {
			// log.info("NC=null");
			return;
		}
		// log.info("processing request with nc="+ncTime);
		Long interactiveTime1 = map.get(sessionKey);
		if (getHttpServletRequest().getContinuation(true).isNew()
				|| interactiveTime1 == null) {
			String companyName = (String) request.getSession().getAttribute(
					"companyName");
			CometManager.onConnectionEsatablished(identityID, companyName);

			// log.info("processing fresh connection for :"
			// + request.getRemoteHost());
			request.setAttribute("processed", Boolean.TRUE);

			map.put(sessionKey, Long.parseLong(ncTime));

			prepare(response);
		} else {
			// log.info("_____________Last Request made:" + ncTime
			// + "\t Current time in request param:"
			// + getHttpServletRequest().getParameter("nc"));
			if (Long.parseLong(ncTime) < interactiveTime1) {

				// log
				// .info("Old request is trying to process with the following continuation ID"
				// + getHttpServletRequest().getContinuation()
				// .hashCode());
				// throw new IOException("Old requst");
				return;
			}

			// log.info("processing continuation" + request.getRemoteHost());

		}
		String url = request.getRequestURI();
		// System.out.println(url);
		int mode = 0;
		if (url.endsWith("/xhrinteractive")) {
			mode = MODE_XHRINTERACTIVE;

		} else if (url.endsWith("/smartpoll")) {
			mode = MODE_SMARTPOLL;

		} else if (url.endsWith("/longpoll")) {
			mode = MODE_LONGPOLL;

		} else if (url.endsWith("/iframe")) {
			mode = MODE_IFRAME;

		} else {
			mode = MODE_SIMPLEPOLL;
		}

		try {
			request.getSession();
			this.setHttpServletRequest(request);
			this.setHttpServletResponse(response);
			this.poller(response, mode);
		}

		catch (IOException e) {
			log.info("Got IO Exception:::::" + e);
			e.printStackTrace();
			map.remove(sessionKey);
			// getHttpServletRequest().getSession().invalidate();
			throw e;
		}

		finally {
			this.clearHttpServletRequest();
			this.clearHttpServletResponse();
		}
	}

	/**
	 * Post requests are not supported by this servlet, this method responds
	 * with a METHOD NOT ALLOWED error code.
	 */
	public void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException,
			ServletException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	protected void flush(final HttpServletResponse response) throws IOException {

		response.getOutputStream().flush();
		response.flushBuffer();

	}

	protected int getConnectionTimeout() {
		return this.connectionTimeout;
	}

	/**
	 * This method is called just before the server side socket is closed.
	 * 
	 * @return
	 */
	protected String getDocumentEndHtml() {
		return Constants.DOCUMENT_END_HTML;
	}

	/**
	 * This method is called before any payloads are written.
	 * 
	 * @return
	 */
	protected String getDocumentStartHtml(int mode) {
		switch (mode) {
		case MODE_XHRINTERACTIVE:
			return Constants.XHR_START;
		case MODE_IFRAME:
			return Constants.IFRAME_START;
		case MODE_LONGPOLL:
			break;
		case MODE_SMARTPOLL:
			return "";
		default:
			return Constants.DOCUMENT_START_HTML;

		}
		return Constants.DOCUMENT_START_HTML;
	}

	public Request getHttpServletRequest() {

		return Request
				.getRequest((HttpServletRequest) CometServerServlet.httpServletRequests
						.get());

	}

	public HttpServletResponse getHttpServletResponse() {
		return (HttpServletResponse) CometServerServlet.httpServletResponses
				.get();
	}

	protected int getMaximumBytesWritten() {
		// Checker.greaterThan("field:maximumBytesWritten", 0,
		// this.maximumBytesWritten);
		return this.maximumBytesWritten;
	}

	/**
	 * Convenience method that fetches the named init parameter and then
	 * converts it into a number.
	 * 
	 * @param name
	 * @return
	 * @throws ServletException
	 */
	protected int getPositiveNumberInitParameter(final String name)
			throws ServletException {
		final String value = this.getInitParameter(name);
		if (isNullOrEmpty(value)) {
			this.throwInitParameterIsMissing(name);
		}

		int number = 0;
		try {
			number = Integer.parseInt(value);

			if (number < 0) {
				this
						.throwInitParameterDoesntContainAPositiveNumber(name,
								value);
			}

		} catch (final NumberFormatException badNumber) {
			this.throwInitParameterDoesntContainAPositiveNumber(name, value);
		}

		return number;
	}

	/**
	 * This reads and saves the maximumBytesWritten and connectionTimeout init
	 * parameters.
	 */
	@Override
	public void init() throws ServletException {
		final int maximumBytesWritten = this
				.getPositiveNumberInitParameter(Constants.MAXIMUM_BYTES_WRITTEN_INIT_PARAMETER);
		this.setMaximumBytesWritten(maximumBytesWritten);

		final int connectionTimeout = this
				.getPositiveNumberInitParameter(Constants.CONNECTION_TIME_OUT_INIT_PARAMETER);
		this.setConnectionTimeout(connectionTimeout);
		// getServletContext().setProperty("ChunkingForced",Boolean.FALSE);
	}

	/**
	 * This method is called when the server drops the connection because too
	 * many bytes have been written
	 * 
	 * @param byteWriteCount
	 *            The total number of bytes written
	 */
	protected void onByteWriteLimitExceeded(final int byteWriteCount) {
	}

	/**
	 * This method is invoked whenever the server drops the connection because
	 * it has been open for too long.
	 * 
	 * @param milliseconds
	 *            The total time the connection was open.
	 */
	protected void onConnectionOpenTooLong(final long milliseconds) {
	}

	/**
	 * Sub classes must override this method to include any object pushing as
	 * well as a delay before returning.
	 * 
	 * @throws ServletException
	 */
	protected boolean poll(CometConnection cometConnection)
			throws ServletException {
		String sessionKey = (String) getHttpServletRequest().getSession()
				.getAttribute("identityID");
		String sessionID = getHttpServletRequest().getSession().getId();
		// log.info("IdentityID:" + sessionKey + " Session:" + sessionID);
		if (sessionKey == null) {
			// log.info("Terminating Comet");
			cometConnection.terminate();
			return false;
		}
		CommandQueue<ObjectPayload> queue = CometManager.getQueue(sessionID,
				sessionKey);

		if (queue == null) {
			return false;
		}
		synchronized (queue) {
			if (queue.isEmpty()) { // log.warn("Ping:" + identityID);
				// Yes - so prepare a continuation
				// cometConnection.push(queue.poll());
				// System.out.println("Queue is Empty. Sleeping");
				Continuation continuation = ContinuationSupport
						.getContinuation(getHttpServletRequest(), queue);
				queue.setContinuation(continuation);
				// wait for an event or timeout
				// log.info("Suspending Continuation");
				continuation.suspend(SLEEP_TIME);
			} else {
				// log.info("Processing Queue");

				while (!queue.isEmpty()) {
					// log.info("Enqueying Data to the  cometConnection");
					cometConnection.push(queue.poll());
				}
			}
		}
		return true;
	}

	/**
	 * This method is responsible for calling the {@link #poll} method and also
	 * includes the logic to push objects and drop connections that have written
	 * too many bytes or been open too long.
	 * 
	 * @param mode
	 * 
	 * @param servletOutputStream
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void poller(final HttpServletResponse response, int mode)
			throws IOException, ServletException {

		int bytesWritten = 0;

		// final int byteWriteLimit = this.getMaximumBytesWritten();

		long connectedAt = System.currentTimeMillis();

		final long connectionTimeout = this.getConnectionTimeout();

		ServletOutputStream servletOutputStream = null;

		try {
			servletOutputStream = response.getOutputStream();
			if (getHttpServletRequest().getContinuation().isNew()) {

				final String before = this.getDocumentStartHtml(mode);
				servletOutputStream.println(before);
				bytesWritten = bytesWritten + before.length() * 2;
			}
			final CometConnection cometConnection = new CometConnection();
			int seq = 0;
			while (true) {

				final long openDuration = System.currentTimeMillis()
						- connectedAt;
				if (openDuration >= connectionTimeout) {
					this.onConnectionOpenTooLong(openDuration);
					// log.error("OpenDuration and STREAM RESET");
					servletOutputStream.println("<script>r();</script>");
					break;
				}
				String escaped = this.preparePayload(getSyncStatus(), mode, -1,
						"ping2");
				// System.out.println(escaped);
				// Log.info(escaped);
				servletOutputStream.println(escaped);

				if (!this.poll(cometConnection)) {
					// log.error("poll(cometConnection) and STREAM RESET");
					servletOutputStream.println("<script>r1();</script>");
					break;
				}

				getHttpServletRequest().getContinuation().reset();

				if (cometConnection.isTerminated()) {
					// log.error("Comet connection terminated and STREAM RESET");
					servletOutputStream.println("<script>r2();</script>");
					break;
				}

				// write out any new messages.
				final Iterator<Message> messages = cometConnection
						.getMessages().iterator();
				while (messages.hasNext()) {
					seq++;
					final Message message = messages.next();

					// final int command = message.getCommand();
					// String stream=cometConnection.getStream();
					final String payload = message.getData();
					if (payload != null && payload.length() > 1) {
						escaped = this.preparePayload(payload, mode, seq,
								message.getStream());
						// log.info(escaped);
						byte[] data = escaped.getBytes("UTF-8");
						// servletOutputStream.println(escaped);
						servletOutputStream.write(data);

						// guess that 2 bytes were written for every char...
						// bytesWritten = bytesWritten + escaped.length() * 2;
						bytesWritten = data.length;

						// remove the processed message.
						messages.remove();
					}
				}

				this.flush(response);

			}

		} finally {
			if (getHttpServletRequest().getContinuation().isResumed()) {

				this.flush(response);
				closeIfNecessary(servletOutputStream);
			}
		}
	}

	private String getSyncStatus() {
		if (Server.isLocal()) {
			LocalServer server = LocalServer.getInstance();
			ClientPacketHandler packetHadler = server.getHandler();
			return String.valueOf(packetHadler.getStatus());
		}
		return "";
	}

	protected void prepare(final HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("text/html; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Transfer-Encoding", "Chunked");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setBufferSize(512);
	}

	/**
	 * This step generates the script tag within embedded javascript that will
	 * written to the client and then executed. The client will need to unescape
	 * the encoded String prior to deserializing.
	 * 
	 * @param serializedForm
	 * @return
	 */
	protected String preparePayload(String serializedForm, int mode,
			int sequence, String channel) {
		// final String escaped = htmlEncode(serializedForm);
		final String escaped = serializedForm.replace("\\", "\\\\").replace(
				"'", "\\'");

		StringBuffer sb = new StringBuffer();
		switch (mode) {
		case MODE_XHRINTERACTIVE:
		case MODE_IFRAME:
			sb.append(Constants.XHR_SCRIPT_TAG_OPEN);
			sb.append(sequence);
			sb.append(",\"");
			sb.append(channel);
			sb.append("\",'");
			sb.append(escaped);
			sb.append("'");
			sb.append(Constants.XHR_SCRIPT_TAG_CLOSE);
		case MODE_LONGPOLL:
			break;
		case MODE_SMARTPOLL:
			sb.append(Constants.SMARTPOLL_OPEN);
			sb.append(sequence);
			sb.append(",\"");
			sb.append(channel);
			sb.append("\",\"");
			sb.append(escaped);
			sb.append("\"");
			sb.append(Constants.SMARTPOLL_CLOSE);
			break;
		default:
			return Constants.DOCUMENT_START_HTML;

		}
		return sb.toString();
	}

	// private String serialize(int command, Object payload) {
	// if (payload instanceof JSONSerializable) {
	//
	// return JSONSTREAM + ":"
	// + ((JSONSerializable) payload).toJson().toString();
	// } else {
	// GWTObject object = (GWTObject) payload;
	// try {
	// return GWTSTREAM
	// + ":"
	// + RPC.encodeResponseForSuccess(object.getMethodObj(),
	// object.getObject(), this
	// .createSerializationPolicy());
	// } catch (SerializationException e) {
	// e.printStackTrace();
	// }
	// }
	// return null;
	// }

	// public SerializationPolicy createSerializationPolicy() {
	// return new SerializationPolicy() {
	// public boolean shouldDeserializeFields(final Class<?> clazz) {
	// throw new UnsupportedOperationException(
	// "shouldDeserializeFields");
	// }
	//
	// public boolean shouldSerializeFields(final Class<?> clazz) {
	// return Object.class != clazz;
	// }
	//
	// public void validateDeserialize(final Class<?> clazz) {
	// throw new UnsupportedOperationException("validateDeserialize");
	// }
	//
	// public void validateSerialize(final Class<?> clazz) {
	// }
	// };
	// }

	protected void setConnectionTimeout(final int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setContinued(boolean val) {
		getHttpServletRequest().setAttribute("continued", val);

	}

	void setHttpServletRequest(final HttpServletRequest httpServletRequests) {
		CometServerServlet.httpServletRequests.set(httpServletRequests);
	}

	void setHttpServletResponse(final HttpServletResponse httpServletResponses) {
		CometServerServlet.httpServletResponses.set(httpServletResponses);
	}

	protected void setMaximumBytesWritten(final int maximumBytesWritten) {
		this.maximumBytesWritten = maximumBytesWritten;
	}

	protected void throwInitParameterDoesntContainAPositiveNumber(
			final String name, final String value) throws ServletException {
		this.throwServletException("The required init parameter \"" + name
				+ "\" with a value of \"" + value
				+ "\" doesnt contain a positive number,");
	}

	protected void throwInitParameterIsMissing(final String name)
			throws ServletException {
		this.throwServletException("The required init parameter \"" + name
				+ "\" is missing,");
	}

	protected void throwServletException(final String message)
			throws ServletException {
		throw new ServletException(message + " from the servlet \""
				+ this.getServletName() + "\".");
	}

}
