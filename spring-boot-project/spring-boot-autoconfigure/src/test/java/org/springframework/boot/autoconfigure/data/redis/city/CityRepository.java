

package org.springframework.boot.autoconfigure.data.redis.city;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

public interface CityRepository extends Repository<City, Long> {

	Page<City> findAll(Pageable pageable);

	Page<City> findByNameLikeAndCountryLikeAllIgnoringCase(String name, String country,
			Pageable pageable);

	City findByNameAndCountryAllIgnoringCase(String name, String country);

}
