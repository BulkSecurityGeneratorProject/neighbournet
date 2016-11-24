package be.sandervl.neighbournet.repository;

import be.sandervl.neighbournet.domain.Attribute;
import be.sandervl.neighbournet.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Spring Data JPA repository for the Attribute entity.
 */
@SuppressWarnings("unused")
public interface AttributeRepository extends JpaRepository<Attribute, Long> {

    Set<Attribute> findByDocument(Document document);
}
