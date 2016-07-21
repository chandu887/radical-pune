package com.radical.lms.filter;

import java.util.StringTokenizer;

/**
 * Convenience class to allow easy access/structure to the allowable path list.
 * 
 * @author achievo
 */
public class Pattern {
	/**
	 * init patternElements object.
	 */
	private String[] m_patternElements;

	/**
	 * Constructor to initialize this object.
	 * 
	 * @param pattern
	 *            String
	 */
	public Pattern(final String pattern) {
		StringTokenizer st = new StringTokenizer(pattern, "*", true);

		m_patternElements = new String[st.countTokens()];
		for (int i = 0; i < m_patternElements.length; ++i) {
			m_patternElements[i] = st.nextToken();
		}
	}

	/**
	 * check url.
	 * 
	 * @param url
	 *            to be checked
	 * @return the boolean
	 */
	public final boolean match(final String url) {
		if(url.equals("/")) {
			return true;
		}
		int pos = 0;
		for (int i = 0; i < m_patternElements.length; i++) {
			String patternElement = m_patternElements[i];
			if(patternElement.equals("/"))
				return false;
			if (!"*".equals(patternElement)) {
				pos = url.indexOf(patternElement, pos);
				if (pos == -1) {
					return false;
				}
				pos += patternElement.length();
			}
		}
		return true;
	}
}
