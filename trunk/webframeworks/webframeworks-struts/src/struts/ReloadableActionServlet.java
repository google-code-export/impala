package struts;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;

/**
 * Extension of Struts <code>ActionServlet</code> which allows it to be reloadable.
 * The <code>destroy</code> method simply removes any <code>RequestProcessor</code> instances
 * held as <code>ServletContext</code> keys
 * @author Phil Zoio
 */
public class ReloadableActionServlet extends ActionServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public void destroy() {

		List <String>values = new ArrayList<String>();
		Enumeration<String> names = getServletContext().getAttributeNames();
		while (names.hasMoreElements()) {
			values.add(names.nextElement());
		}

		Iterator<String> keys = values.iterator();
		while (keys.hasNext()) {
			String name = keys.next();
			Object value = getServletContext().getAttribute(name);

			if (!(value instanceof ModuleConfig)) {
				continue;
			}

			ModuleConfig config = (ModuleConfig) value;
			getServletContext().removeAttribute(
					Globals.REQUEST_PROCESSOR_KEY + config.getPrefix());
		}

		super.destroy();
	}

}
