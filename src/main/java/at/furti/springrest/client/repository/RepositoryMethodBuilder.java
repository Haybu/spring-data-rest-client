package at.furti.springrest.client.repository;

import java.io.PrintStream;

import org.apache.tapestry5.plastic.InstructionBuilder;
import org.apache.tapestry5.plastic.InstructionBuilderCallback;
import org.apache.tapestry5.plastic.PlasticField;

public class RepositoryMethodBuilder implements InstructionBuilderCallback {

	private PlasticField relField;

	public RepositoryMethodBuilder(PlasticField relField) {
		super();
		this.relField = relField;
	}

	public void doBuild(InstructionBuilder builder) {
		try {
			builder.getStaticField(System.class.getName(), "out", PrintStream.class);

			builder.loadThis().getField(relField);

			builder.invoke(PrintStream.class.getMethod("println", Object.class));

			builder.returnDefaultValue();
		} catch (Exception ex) {
			//TODO: logging
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
