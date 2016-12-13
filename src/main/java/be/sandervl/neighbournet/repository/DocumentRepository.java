package be.sandervl.neighbournet.repository;

import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.domain.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the Document entity.
 */
@SuppressWarnings("unused")
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("select distinct document from Document document left join fetch document.matches")
    List<Document> findAllWithEagerRelationships();

    @Query("select document from Document document left join fetch document.matches where document.id =:id")
    Document findOneWithEagerRelationships(@Param("id") Long id);

    Optional<Document> findByUrl(String url);

    Page<Document> findAllOrderByCreated(Pageable pageable);

    Set<Document> findBySite(Site site);
}
