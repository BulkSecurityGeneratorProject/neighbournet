package be.sandervl.neighbournet.repository;

import be.sandervl.neighbournet.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Document entity.
 */
@SuppressWarnings("unused")
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("select distinct document from Document document left join fetch document.matches")
    List<Document> findAllWithEagerRelationships();

    @Query("select document from Document document left join fetch document.matches where document.id =:id")
    Document findOneWithEagerRelationships(@Param("id") Long id);

}
