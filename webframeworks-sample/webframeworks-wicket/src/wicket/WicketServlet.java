package wicket;

import org.apache.wicket.protocol.http.WicketFilter;

public class WicketServlet extends org.apache.wicket.protocol.http.WicketServlet {
 
    private static final long serialVersionUID = 1L;

    @Override
    protected WicketFilter newWicketFilter() {
        return new wicket.WicketFilter();
    }

}
