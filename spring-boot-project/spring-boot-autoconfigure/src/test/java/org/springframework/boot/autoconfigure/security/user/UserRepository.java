

package org.springframework.boot.autoconfigure.security.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);

}
