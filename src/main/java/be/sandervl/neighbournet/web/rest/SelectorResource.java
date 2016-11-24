package be.sandervl.neighbournet.web.rest;

import be.sandervl.neighbournet.domain.Selector;
import be.sandervl.neighbournet.repository.SelectorRepository;
import be.sandervl.neighbournet.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Selector.
 */
@RestController
@RequestMapping("/api")
public class SelectorResource {

    private final Logger log = LoggerFactory.getLogger(SelectorResource.class);

    @Inject
    private SelectorRepository selectorRepository;

    /**
     * POST  /selectors : Create a new selector.
     *
     * @param selector the selector to create
     * @return the ResponseEntity with status 201 (Created) and with body the new selector, or with status 400 (Bad Request) if the selector has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/selectors")
    @Timed
    public ResponseEntity<Selector> createSelector(@Valid @RequestBody Selector selector) throws URISyntaxException {
        log.debug("REST request to save Selector : {}", selector);
        if (selector.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("selector", "idexists", "A new selector cannot already have an ID")).body(null);
        }
        Selector result = selectorRepository.save(selector);
        return ResponseEntity.created(new URI("/api/selectors/" + result.getId()))
                             .headers(HeaderUtil.createEntityCreationAlert("selector", result.getId().toString()))
                             .body(result);
    }

    /**
     * PUT  /selectors : Updates an existing selector.
     *
     * @param selector the selector to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated selector,
     * or with status 400 (Bad Request) if the selector is not valid,
     * or with status 500 (Internal Server Error) if the selector couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/selectors")
    @Timed
    public ResponseEntity<Selector> updateSelector(@Valid @RequestBody Selector selector) throws URISyntaxException {
        log.debug("REST request to update Selector : {}", selector);
        if (selector.getId() == null) {
            return createSelector(selector);
        }
        Selector result = selectorRepository.save(selector);
        return ResponseEntity.ok()
                             .headers(HeaderUtil.createEntityUpdateAlert("selector", selector.getId().toString()))
                             .body(result);
    }

    /**
     * GET  /selectors : get all the selectors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of selectors in body
     */
    @GetMapping("/selectors")
    @Timed
    public List<Selector> getAllSelectors() {
        log.debug("REST request to get all Selectors");
        List<Selector> selectors = selectorRepository.findAll();
        return selectors;
    }

    /**
     * GET  /selectors/:id : get the "id" selector.
     *
     * @param id the id of the selector to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the selector, or with status 404 (Not Found)
     */
    @GetMapping("/selectors/{id}")
    @Timed
    public ResponseEntity<Selector> getSelector(@PathVariable Long id) {
        log.debug("REST request to get Selector : {}", id);
        Selector selector = selectorRepository.findOne(id);
        return Optional.ofNullable(selector)
                       .map(result -> new ResponseEntity<>(
                           result,
                           HttpStatus.OK))
                       .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /selectors/:id : delete the "id" selector.
     *
     * @param id the id of the selector to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/selectors/{id}")
    @Timed
    public ResponseEntity<Void> deleteSelector(@PathVariable Long id) {
        log.debug("REST request to delete Selector : {}", id);
        selectorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("selector", id.toString())).build();
    }

}
