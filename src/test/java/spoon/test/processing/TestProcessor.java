package spoon.test.processing;

import java.util.Date;

import spoon.processing.AbstractProcessor;
import spoon.processing.Severity;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtTypedElement;
import spoon.template.Substitution;
import spoon.test.template.TemplateWithConstructor;

public class TestProcessor extends AbstractProcessor<CtElement> {

	@Override
	public void init() {
		super.init();
		System.out.println("MAIN METHODS: "
				+ getFactory().Method().getMainMethods());
	}

	public void process(CtElement element) {
		if ((!(element instanceof CtPackage)) && !element.isParentInitialized()) {
			getEnvironment().report(this, Severity.ERROR, element,
					"Element's parent is null (" + element + ")");
			throw new RuntimeException("uninitialized parent detected");
		}
		if (element instanceof CtTypedElement) {
			if (((CtTypedElement<?>) element).getType() == null) {
				getEnvironment().report(this, Severity.WARNING, element,
						"Element's type is null (" + element + ")");
			}
		}
		if (element instanceof CtClass) {
			CtClass<?> c = (CtClass<?>) element;
			if (c.getSimpleName().equals("Secondary")) {
				@SuppressWarnings("unused")
				CompilationUnit cu = c.getPosition().getCompilationUnit();
			}
			if (c.getSimpleName().equals("C1")) {
				Substitution.insertAll(c, new TemplateWithConstructor(
						getFactory().Type().createReference(Date.class)));
			}
		}
	}

}
