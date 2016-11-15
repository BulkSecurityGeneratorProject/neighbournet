package be.sandervl.neighbournet.repository;

import be.sandervl.neighbournet.domain.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Attribute entity.
 */
@SuppressWarnings("unused")
public interface AttributeRepository extends JpaRepository<Attribute, Long> {

}
