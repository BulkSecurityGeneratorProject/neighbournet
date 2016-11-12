package be.sandervl.neighbournet.web.rest;

import be.sandervl.neighbournet.domain.FeedItem;
import be.sandervl.neighbournet.service.FeedItemService;
import be.sandervl.neighbournet.web.rest.util.HeaderUtil;
import be.sandervl.neighbournet.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FeedItem.
 */
@RestController
@RequestMapping("/api")
public class FeedItemResource {

    private final Logger log = LoggerFactory.getLogger(FeedItemResource.class);

    @Inject
    private FeedItemService feedItemService;

    /**
     * POST  /feed-items : Create a new feedItem.
     *
     * @param feedItem the feedItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new feedItem, or with status 400 (Bad Request) if the feedItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/feed-items")
    @Timed
    public ResponseEntity<FeedItem> createFeedItem(@Valid @RequestBody FeedItem feedItem) throws URISyntaxException {
        log.debug("REST request to save FeedItem : {}", feedItem);
        if (feedItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("feedItem", "idexists", "A new feedItem cannot already have an ID")).body(null);
        }
        FeedItem result = feedItemService.save(feedItem);
        return ResponseEntity.created(new URI("/api/feed-items/" + result.getId()))
                             .headers(HeaderUtil.createEntityCreationAlert("feedItem", result.getId().toString()))
                             .body(result);
    }

    /**
     * PUT  /feed-items : Updates an existing feedItem.
     *
     * @param feedItem the feedItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated feedItem,
     * or with status 400 (Bad Request) if the feedItem is not valid,
     * or with status 500 (Internal Server Error) if the feedItem couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/feed-items")
    @Timed
    public ResponseEntity<FeedItem> updateFeedItem(@Valid @RequestBody FeedItem feedItem) throws URISyntaxException {
        log.debug("REST request to update FeedItem : {}", feedItem);
        if (feedItem.getId() == null) {
            return createFeedItem(feedItem);
        }
        FeedItem result = feedItemService.save(feedItem);
        return ResponseEntity.ok()
                             .headers(HeaderUtil.createEntityUpdateAlert("feedItem", feedItem.getId().toString()))
                             .body(result);
    }

    /**
     * GET  /feed-items : get all the feedItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of feedItems in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/feed-items")
    @Timed
    public ResponseEntity<List<FeedItem>> getAllFeedItems(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of FeedItems");
        Page<FeedItem> page = feedItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/feed-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /feed-items/:id : get the "id" feedItem.
     *
     * @param id the id of the feedItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the feedItem, or with status 404 (Not Found)
     */
    @GetMapping("/feed-items/{id}")
    @Timed
    public ResponseEntity<FeedItem> getFeedItem(@PathVariable Long id) {
        log.debug("REST request to get FeedItem : {}", id);
        FeedItem feedItem = feedItemService.findOne(id);
        return Optional.ofNullable(feedItem)
                       .map(result -> new ResponseEntity<>(
                           result,
                           HttpStatus.OK))
                       .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /feed-items/:id : delete the "id" feedItem.
     *
     * @param id the id of the feedItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/feed-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteFeedItem(@PathVariable Long id) {
        log.debug("REST request to delete FeedItem : {}", id);
        feedItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("feedItem", id.toString())).build();
    }

}
