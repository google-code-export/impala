package wicket;

import javax.servlet.http.HttpServletRequest;

public class WicketFilter extends org.apache.wicket.protocol.http.WicketFilter {

    @Override
    public String getRelativePath(HttpServletRequest request) {
        final String servletPath = request.getServletPath();
        int indexOfSlash = servletPath.lastIndexOf("/");
        String path = servletPath.substring(indexOfSlash+1);
        int indexOfDot = path.indexOf(".");
        if (indexOfDot > 0) {
            path = path.substring(0, indexOfDot);
        }
        
        return path;
    }
    
}
