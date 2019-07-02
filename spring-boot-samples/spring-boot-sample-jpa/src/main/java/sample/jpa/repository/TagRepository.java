

package sample.jpa.repository;

import sample.jpa.domain.Tag;

import java.util.List;

public interface TagRepository {

	List<Tag> findAll();

}
