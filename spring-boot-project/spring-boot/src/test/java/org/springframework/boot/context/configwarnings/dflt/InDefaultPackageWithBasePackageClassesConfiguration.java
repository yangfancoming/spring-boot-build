

package org.springframework.boot.context.configwarnings.dflt;

import org.springframework.boot.context.configwarnings.real.nested.ExampleBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = ExampleBean.class)
public class InDefaultPackageWithBasePackageClassesConfiguration {

}
