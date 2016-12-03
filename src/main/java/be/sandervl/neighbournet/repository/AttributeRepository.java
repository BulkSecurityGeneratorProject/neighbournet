package be.sandervl.neighbournet.repository;

import be.sandervl.neighbournet.domain.Attribute;

import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.domain.Selector;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Attribute entity.
 */
@SuppressWarnings("unused")
public interface AttributeRepository extends JpaRepository<Attribute, Long> {

    @Query("select distinct attribute from Attribute attribute left join fetch attribute.relatives")
    List<Attribute> findAllWithEagerRelationships();

    @Query("select attribute from Attribute attribute left join fetch attribute.relatives where attribute.id =:id")
    Attribute findOneWithEagerRelationships(@Param("id") Long id);

    Set<Attribute> findByDocument(Document document);

    Attribute findBySelectorAndDocument(Selector selector, Document document);
}
