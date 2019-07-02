

package sample.data.jpa.service;

import sample.data.jpa.domain.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

interface CityRepository extends Repository<City, Long> {

	Page<City> findAll(Pageable pageable);

	Page<City> findByNameContainingAndCountryContainingAllIgnoringCase(String name,String country, Pageable pageable);

	City findByNameAndCountryAllIgnoringCase(String name, String country);

}
