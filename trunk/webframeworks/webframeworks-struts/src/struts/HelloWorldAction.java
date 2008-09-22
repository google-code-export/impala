package struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class HelloWorldAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		HttpSession session = request.getSession();
		
		incrementSerializedValue(request, session);
		
		incrementValue(request, session);
		
		request.setAttribute("from", "Phil");
		return mapping.findForward("success");
	}

	private void incrementSerializedValue(HttpServletRequest request,
			HttpSession session) {
		
		SerializableValueHolder serializableValueHolder = (SerializableValueHolder) session.getAttribute("serializedSessionValue");
		if (serializableValueHolder == null) {
			serializableValueHolder = new SerializableValueHolder();
			session.setAttribute("serializedSessionValue",
					serializableValueHolder);
		}
		serializableValueHolder.increment();
		request.setAttribute("serializedSessionValue", serializableValueHolder.getCount());
	}

	private void incrementValue(HttpServletRequest request, HttpSession session) {
		
		ValueHolder valueHolder = (ValueHolder) session.getAttribute("sessionValue");
		if (valueHolder == null) {
			valueHolder = new ValueHolder();
			session.setAttribute("sessionValue",
					valueHolder);
		}
		valueHolder.increment();
		request.setAttribute("sessionValue", valueHolder.getCount());
	}

}
