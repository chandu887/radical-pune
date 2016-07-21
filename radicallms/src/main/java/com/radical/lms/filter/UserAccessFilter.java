package com.radical.lms.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.radical.lms.filter.Pattern;
import com.radical.lms.entity.UsersEntity;

/**
 * This class is used to filter all urls. If they are valid then allowed
 * otherwise they are redirect to welcome.do
 *
 * @author Administrator
 *
 */
public class UserAccessFilter extends BaseFilter {

	/** used to store String value. */
	private static final String LOGIN_URL = "login-url";

	/** used to store String value. */
	private static final String MEMBERAREA = "memberarea-default";


	/** used to store String value. */
	private static final String ALLOWED_ADMIN_URLS = "allowed-adminUrls";
	
	private static final String ALLOWED_ALLURLS = "allowed-allUrls";

	/** used to store String value. */
	private static final String COMMA = ",";

	/** used to store String value. */
	private static final String SLASH = "/";

	/** used to store String value. */
	private static final String EXCEPTION_MESSAGE = ".doFilter() requires a servletRequest that"
			+ " implements HttpServletRequest";

	/** used to store String value. */
	private static final String CACHE_CONTROL = "Cache-Control";

	/** used to store String value. */
	private static final String CACHE_CONTROL_VALUE = "no-cache, no-store, must-revalidate";

	/** used to store String value. */
	private static final String PRAGMA = "Pragma";

	/** used to store String value. */
	private static final String PRAGMA_VALUE = "no-cache";

	/** used to store String value. */
	private static final String EXPIRES = "Expires";

	/** used to store String value. */
	private static final int EXPIRES_VALUE = 0;

	private static final int MIN_SIZE = 0;

	/** Used to store vendorBean. */
	public static final String VENDOR_BEAN = "vendorBean";

	/** Used to store partnerBean. */
	public static final String PARTNER_BEAN = "partnerBean";

	/**
	 * init loginURL.
	 */
	private String loginURL = null;


	private ArrayList<Pattern> allowedAdmins = new ArrayList<Pattern>();

	private ArrayList<Pattern> allowedAll = new ArrayList<Pattern>();
	

	/**
	 * Method called by the servlet container just after the filter is
	 * instantiated.
	 *
	 * @param config
	 *            - Servlet Container filter configuration object
	 * @throws ServletException
	 *             - Generic Servlet exception
	 */
	public final void init(final FilterConfig config) throws ServletException {
		super.init(config);

		loginURL = config.getInitParameter(LOGIN_URL);

		config.getInitParameter(MEMBERAREA);

		StringTokenizer adminSt = new StringTokenizer(config.getInitParameter(ALLOWED_ADMIN_URLS), COMMA);
		while (adminSt.hasMoreTokens()) {
			String pattern = adminSt.nextToken();
			allowedAdmins.add(new Pattern(pattern));
		}
		
		StringTokenizer allSt = new StringTokenizer(config.getInitParameter(ALLOWED_ALLURLS), COMMA);
		while (allSt.hasMoreTokens()) {
			String pattern = allSt.nextToken();
			allowedAll.add(new Pattern(pattern));
		}

	}

	public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
			final FilterChain chain) throws IOException, ServletException {
		if (!(servletRequest instanceof HttpServletRequest)) {
			throw new ServletException(UserAccessFilter.class.getName() + EXCEPTION_MESSAGE);
		}

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setHeader(CACHE_CONTROL, CACHE_CONTROL_VALUE); // HTTP 1.1.
		response.setHeader(PRAGMA, PRAGMA_VALUE); // HTTP 1.0.
		response.setDateHeader(EXPIRES, EXPIRES_VALUE); // Proxies.
		// Get the path that is relative to our application context
		String path = getRelativePath(request);

		HttpSession session = request.getSession();
		UsersEntity user = (UsersEntity) session.getAttribute("userInfo");

		if (allowAccess(path, session)) {
			// cleareSession(session, path);
			chain.doFilter(request, response);
		} else {
			session.invalidate();
			response.sendRedirect(request.getContextPath() + loginURL);
		}
	}

	/**
	 * Method to determine the path of the request relative to our web
	 * application context.
	 *
	 * NOTE: This is coded in this way since different Servlet implementations
	 * have different interpretations about what to return from
	 * getServletPath()/getPathInfo()
	 *
	 * @param request
	 *            - Object containing the client request information
	 * @return path - path information relative to application context
	 */
	private String getRelativePath(final HttpServletRequest request) {

		String cxtPath = request.getContextPath();
		String path = request.getRequestURI();
		if (!SLASH.equals(cxtPath) && path.startsWith(cxtPath)) {
			path = path.substring(cxtPath.length());
		}
		return path;
	}

	/**
	 * Method that will determine if a path is defined to the allowable pathlist
	 * as defined in web.xml.
	 * 
	 * @param path
	 *            - path to search for in the allowable list
	 * @param session
	 *            HttpSession object
	 * @return boolean
	 */
	private boolean allowAccess(final String path,  HttpSession session) {
		for (Pattern pattern : allowedAll) {
			if (pattern.match(path)) {
				return true;
			}
		}
		
		UsersEntity user = (UsersEntity) session.getAttribute("userInfo");
		if (user != null) {
			switch (user.getRoleId()) {
			case 1:
				for (int count = MIN_SIZE; count < allowedAdmins.size(); ++count) {
					Pattern pattern = (Pattern) allowedAdmins.get(count);
					if (pattern.match(path)) {
						return true;
					}
				}
				break;
			default:
				return false;
			}
		}
		
		return false;
	}
}
