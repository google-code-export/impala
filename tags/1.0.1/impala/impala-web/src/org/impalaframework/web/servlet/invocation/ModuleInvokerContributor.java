package org.impalaframework.web.servlet.invocation;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Class which holds a module level mapping of filter and servlet names to suffixes.
 * Note that each of the filters and servlets should be present in the module. 
 * 
 * @author Phil Zoio
 */
public class ModuleInvokerContributor implements InitializingBean {

    private String suffix;

    private String servletName;

    private String[] filterNames;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(suffix, "suffix cannot be null");
    }
    
    public String[] getFilterNames() {
        return filterNames;
    }

    public String getServletName() {
        return servletName;
    }
    
    public String getSuffix() {
        return suffix;
    }
    
    public void setFilterNames(String[] filterNames) {
        this.filterNames = filterNames;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer(500);
        buffer.append(this.getClass().getName()).append(": ");
        buffer.append("suffix = ");
        buffer.append(this.suffix);
        buffer.append(", ");
        buffer.append("servletName = ");
        buffer.append(this.servletName);
        buffer.append(", ");
        buffer.append("filterNames = ");
        if (this.filterNames != null)
            buffer.append(this.filterNames.toString());
        else
            buffer.append("value is null");
        buffer.append("\n");
        return buffer.toString();
    }
}
