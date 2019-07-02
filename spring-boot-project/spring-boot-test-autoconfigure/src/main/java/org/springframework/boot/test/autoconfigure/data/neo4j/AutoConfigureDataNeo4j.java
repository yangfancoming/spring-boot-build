

package org.springframework.boot.test.autoconfigure.data.neo4j;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

/**
 * {@link ImportAutoConfiguration Auto-configuration imports} for typical Data Neo4j
 * tests. Most tests should consider using {@link DataNeo4jTest @DataNeo4jTest} rather
 * than using this annotation directly.
 *
 * @author Eddú Meléndez
 * @since 2.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration
public @interface AutoConfigureDataNeo4j {

}
