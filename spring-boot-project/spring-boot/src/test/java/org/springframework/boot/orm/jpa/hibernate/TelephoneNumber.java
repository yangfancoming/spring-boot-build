

package org.springframework.boot.orm.jpa.hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Simple entity used in {@link SpringPhysicalNamingStrategyTests}.
 *
 * @author Phillip Webb
 */
@Entity
public class TelephoneNumber {

	@Id
	@GeneratedValue
	Long id;

	String areaCode;

	String number;

}
