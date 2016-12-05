package be.sandervl.neighbournet.repository;

import be.sandervl.neighbournet.domain.Selector;
import be.sandervl.neighbournet.domain.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Selector entity.
 */
@SuppressWarnings("unused")
public interface SelectorRepository extends JpaRepository<Selector,Long> {

    Iterable<Selector> findBySite(Site site);

    Iterable<Selector> findBySiteAndParentIsNull(Site site);

    @Query("select distinct selector from Selector selector left join fetch selector.children")
    List<Selector> findAllWithChildren();

    @Query("select distinct selector from Selector selector left join fetch selector.children where selector.id =:id")
    Selector findOneWithChildren(@Param("id") Long id);

    Optional<Selector> findBySiteAndName(Site site, String name);
}
