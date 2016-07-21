package com.radical.lms.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * An abstract Filter class that implements the Servlet 2.3 API.
 *
 * @author achievo
 */
public abstract class BaseFilter implements Filter {
    /**
     * Instance of config.
     */
    private FilterConfig m_config = null;

    /**
     * Instance of application.
     */
    private ServletContext m_application = null;

    /**
     * The default constructor. init Variable.
     *
     * @param config
     *            It's a Config file
     * @throws ServletException
     *             if config is null.
     */
    public void init(final FilterConfig config) throws ServletException {
        this.m_config = config;
        this.m_application = config.getServletContext();
    }

    /**
     * destroy the variable.
     */
    public final void destroy() {
        m_config = null;
        m_application = null;
    }

    /**
     * get function.
     *
     * @return the application
     */
    public final ServletContext getApplication() {
        return m_application;
    }

    /**
     * FOR COMPATIBILITY WITH WebLogic 6.1 J2EE 1.3 support. - which has
     * setFilterConfig/getFilterConfig and is missing init/destroy.
     *
     * @param config
     *            It's a Config file
     */

    public final void setFilterConfig(final FilterConfig config) {
        if (this.m_config == null) {
            try {
                init(config);
            } catch (ServletException ex) {
                config.getServletContext().log(
               getClass().getName() + " initialization failed.", ex);
            }
        }
    }

    /**
     * get function.
     *
     * @return the config
     */
    public final FilterConfig getFilterConfig() {
        return m_config;
    }
}
