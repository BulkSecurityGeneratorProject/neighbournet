package be.sandervl.neighbournet.repository;

import be.sandervl.neighbournet.domain.Selector;

import be.sandervl.neighbournet.domain.Site;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Selector entity.
 */
@SuppressWarnings("unused")
public interface SelectorRepository extends JpaRepository<Selector,Long> {

    @Query("select distinct selector from Selector selector left join fetch selector.children")
    List<Selector> findAllWithEagerRelationships();

    @Query("select selector from Selector selector left join fetch selector.children where selector.id =:id")
    Selector findOneWithEagerRelationships(@Param("id") Long id);

    Iterable<Selector> findBySite(Site site);

    Iterable<Selector> findBySiteAndParentIsNull(Site site);
}
