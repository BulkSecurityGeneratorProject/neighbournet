package be.sandervl.neighbournet.service.impl;

import be.sandervl.neighbournet.domain.FeedItem;
import be.sandervl.neighbournet.repository.FeedItemRepository;
import be.sandervl.neighbournet.service.FeedItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service Implementation for managing FeedItem.
 */
@Service
@Transactional
public class FeedItemServiceImpl implements FeedItemService {

    private final Logger log = LoggerFactory.getLogger(FeedItemServiceImpl.class);

    @Inject
    private FeedItemRepository feedItemRepository;

    /**
     * Save a feedItem.
     *
     * @param feedItem the entity to save
     * @return the persisted entity
     */
    public FeedItem save(FeedItem feedItem) {
        log.debug("Request to save FeedItem : {}", feedItem);
        FeedItem result = feedItemRepository.save(feedItem);
        return result;
    }

    /**
     * Get all the feedItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FeedItem> findAll(Pageable pageable) {
        log.debug("Request to get all FeedItems");
        Page<FeedItem> result = feedItemRepository.findAll(pageable);
        return result;
    }

    /**
     * Get one feedItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FeedItem findOne(Long id) {
        log.debug("Request to get FeedItem : {}", id);
        FeedItem feedItem = feedItemRepository.findOne(id);
        return feedItem;
    }

    /**
     * Delete the  feedItem by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FeedItem : {}", id);
        feedItemRepository.delete(id);
    }
}
