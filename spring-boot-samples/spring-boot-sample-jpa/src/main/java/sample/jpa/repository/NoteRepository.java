

package sample.jpa.repository;

import sample.jpa.domain.Note;

import java.util.List;

public interface NoteRepository {

	List<Note> findAll();

}
