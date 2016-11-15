package be.sandervl.neighbournet.repository;

import be.sandervl.neighbournet.domain.Selector;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Selector entity.
 */
@SuppressWarnings("unused")
public interface SelectorRepository extends JpaRepository<Selector, Long> {

}
