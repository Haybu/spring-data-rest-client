package at.furti.springrest.client.repository.method;

import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;

import at.furti.springrest.client.repository.exception.NotExportedException;

public class NotExportedAdvice implements MethodAdvice {

	public void advise(MethodInvocation invocation) {
		invocation.setCheckedException(new NotExportedException(
				"Resource is not exported"));
		invocation.rethrow();
	}
}
