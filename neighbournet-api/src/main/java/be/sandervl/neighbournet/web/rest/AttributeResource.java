package be.sandervl.neighbournet.web.rest;

import be.sandervl.neighbournet.domain.Attribute;
import be.sandervl.neighbournet.service.AttributeService;
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
 * REST controller for managing Attribute.
 */
@RestController
@RequestMapping("/api")
public class AttributeResource {

    private final Logger log = LoggerFactory.getLogger(AttributeResource.class);

    @Inject
    private AttributeService attributeService;

    /**
     * POST  /attributes : Create a new attribute.
     *
     * @param attribute the attribute to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attribute, or with status 400 (Bad Request) if the attribute has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/attributes")
    @Timed
    public ResponseEntity<Attribute> createAttribute(@Valid @RequestBody Attribute attribute) throws URISyntaxException {
        log.debug("REST request to save Attribute : {}", attribute);
        if (attribute.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("attribute", "idexists", "A new attribute cannot already have an ID")).body(null);
        }
        Attribute result = attributeService.save(attribute);
        return ResponseEntity.created(new URI("/api/attributes/" + result.getId()))
                             .headers(HeaderUtil.createEntityCreationAlert("attribute", result.getId().toString()))
                             .body(result);
    }

    /**
     * PUT  /attributes : Updates an existing attribute.
     *
     * @param attribute the attribute to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attribute,
     * or with status 400 (Bad Request) if the attribute is not valid,
     * or with status 500 (Internal Server Error) if the attribute couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/attributes")
    @Timed
    public ResponseEntity<Attribute> updateAttribute(@Valid @RequestBody Attribute attribute) throws URISyntaxException {
        log.debug("REST request to update Attribute : {}", attribute);
        if (attribute.getId() == null) {
            return createAttribute(attribute);
        }
        Attribute result = attributeService.save(attribute);
        return ResponseEntity.ok()
                             .headers(HeaderUtil.createEntityUpdateAlert("attribute", attribute.getId().toString()))
                             .body(result);
    }

    /**
     * GET  /attributes : get all the attributes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of attributes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/attributes")
    @Timed
    public ResponseEntity<List<Attribute>> getAllAttributes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Attributes");
        Page<Attribute> page = attributeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/attributes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /attributes/:id : get the "id" attribute.
     *
     * @param id the id of the attribute to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attribute, or with status 404 (Not Found)
     */
    @GetMapping("/attributes/{id}")
    @Timed
    public ResponseEntity<Attribute> getAttribute(@PathVariable Long id) {
        log.debug("REST request to get Attribute : {}", id);
        Attribute attribute = attributeService.findOne(id);
        return Optional.ofNullable(attribute)
                       .map(result -> new ResponseEntity<>(
                           result,
                           HttpStatus.OK))
                       .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /attributes/:id : delete the "id" attribute.
     *
     * @param id the id of the attribute to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attributes/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttribute(@PathVariable Long id) {
        log.debug("REST request to delete Attribute : {}", id);
        attributeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("attribute", id.toString())).build();
    }

}
