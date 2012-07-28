package at.furti.springrest.client.repository.method;

import org.apache.commons.lang.NotImplementedException;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;

public class NotImplementedMethodAdvice implements MethodAdvice {

	public void advise(MethodInvocation invocation) {
		invocation.setCheckedException(new NotImplementedException(
				"Method not implemented"));
		invocation.rethrow();
	}
}
