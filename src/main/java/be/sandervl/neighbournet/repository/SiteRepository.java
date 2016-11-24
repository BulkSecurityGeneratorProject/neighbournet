package be.sandervl.neighbournet.repository;

import be.sandervl.neighbournet.domain.Site;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Site entity.
 */
@SuppressWarnings("unused")
public interface SiteRepository extends JpaRepository<Site, Long> {

}
