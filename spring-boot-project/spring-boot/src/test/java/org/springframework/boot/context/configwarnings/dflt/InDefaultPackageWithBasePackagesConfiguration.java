

package org.springframework.boot.context.configwarnings.dflt;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.springframework.boot.context.configwarnings.nested")
public class InDefaultPackageWithBasePackagesConfiguration {

}
