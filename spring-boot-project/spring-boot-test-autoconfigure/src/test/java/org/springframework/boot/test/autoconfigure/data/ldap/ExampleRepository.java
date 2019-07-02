

package org.springframework.boot.test.autoconfigure.data.ldap;

import org.springframework.data.ldap.repository.LdapRepository;

/**
 * Example repository used with {@link DataLdapTest} tests.
 *
 * @author Eddú Meléndez
 */
public interface ExampleRepository extends LdapRepository<ExampleEntry> {

}
