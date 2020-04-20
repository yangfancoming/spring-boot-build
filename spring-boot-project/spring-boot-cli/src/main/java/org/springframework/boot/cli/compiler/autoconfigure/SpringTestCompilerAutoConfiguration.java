

package org.springframework.boot.cli.compiler.autoconfigure;

import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import org.springframework.boot.cli.compiler.AstUtils;
import org.springframework.boot.cli.compiler.CompilerAutoConfiguration;
import org.springframework.boot.cli.compiler.DependencyCustomizer;
import org.springframework.boot.cli.compiler.GroovyCompilerConfiguration;

/**
 * {@link CompilerAutoConfiguration} for Spring Test.
 *
 * @author Dave Syer
 * @since 1.1.0
 */
public class SpringTestCompilerAutoConfiguration extends CompilerAutoConfiguration {

	@Override
	public boolean matches(ClassNode classNode) {
		return AstUtils.hasAtLeastOneAnnotation(classNode, "SpringBootTest");
	}

	@Override
	public void applyDependencies(DependencyCustomizer dependencies) {
		dependencies.ifAnyMissingClasses("org.springframework.http.HttpHeaders")
				.add("spring-boot-starter-web");
	}

	@Override
	public void apply(GroovyClassLoader loader, GroovyCompilerConfiguration configuration,
			GeneratorContext generatorContext, SourceUnit source, ClassNode classNode)
			throws CompilationFailedException {
		if (!AstUtils.hasAtLeastOneAnnotation(classNode, "RunWith")) {
			AnnotationNode runWith = new AnnotationNode(ClassHelper.make("RunWith"));
			runWith.addMember("value",
					new ClassExpression(ClassHelper.make("SpringRunner")));
			classNode.addAnnotation(runWith);
		}
	}

	@Override
	public void applyImports(ImportCustomizer imports) throws CompilationFailedException {
		imports.addStarImports("org.junit.runner", "org.springframework.boot.test",
				"org.springframework.boot.test.context",
				"org.springframework.boot.test.web.client", "org.springframework.http",
				"org.springframework.test.context.junit4",
				"org.springframework.test.annotation").addImports(
						"org.springframework.boot.test.context.SpringBootTest.WebEnvironment",
						"org.springframework.boot.test.web.client.TestRestTemplate");
	}

}
