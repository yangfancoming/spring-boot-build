
package sample.jpa.repository;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import sample.jpa.domain.Tag;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link JpaTagRepository}.
 *
 * @author Andy Wilkinson
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class JpaTagRepositoryIntegrationTests {

	@Autowired
	JpaTagRepository repository;

	@Test
	public void findsAllTags() {
		List<Tag> tags = this.repository.findAll();
		assertThat(tags).hasSize(3);
		for (Tag tag : tags) {
			assertThat(tag.getNotes().size()).isGreaterThan(0);
		}
	}

}
