

package org.springframework.boot.test.mock.mockito;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Beans created using Mockito.
 *
 * @author Andy Wilkinson
 */
class MockitoBeans implements Iterable<Object> {

	private final List<Object> beans = new ArrayList<>();

	void add(Object bean) {
		this.beans.add(bean);
	}

	@Override
	public Iterator<Object> iterator() {
		return this.beans.iterator();
	}

}
