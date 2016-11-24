package be.sandervl.neighbournet.repository;

import be.sandervl.neighbournet.domain.FeedItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the FeedItem entity.
 */
@SuppressWarnings("unused")
public interface FeedItemRepository extends JpaRepository<FeedItem, Long> {

}
