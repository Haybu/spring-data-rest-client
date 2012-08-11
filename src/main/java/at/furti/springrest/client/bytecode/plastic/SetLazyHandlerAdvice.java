package at.furti.springrest.client.bytecode.plastic;

import java.lang.reflect.Field;

import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;

import at.furti.springrest.client.repository.lazy.LazyLoadingHandler;

public class SetLazyHandlerAdvice implements MethodAdvice {

	private String propertyName;

	public SetLazyHandlerAdvice(String propertyName) {
		this.propertyName = propertyName;
	}

	public void advise(MethodInvocation invocation) {
		try {
			Field handlerField = invocation.getInstance().getClass()
					.getDeclaredField(propertyName + "_lazyHandler");

			handlerField.setAccessible(true);

			LazyLoadingHandler handler = (LazyLoadingHandler) handlerField
					.get(invocation.getInstance());

			handler.doSet(invocation.getParameter(0));
		} catch (Exception ex) {
			invocation.setCheckedException(ex);
			invocation.rethrow();
		}
	}
}
