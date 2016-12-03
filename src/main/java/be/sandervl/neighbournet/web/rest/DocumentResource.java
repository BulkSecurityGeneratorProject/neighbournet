package be.sandervl.neighbournet.web.rest;

import be.sandervl.neighbournet.domain.Attribute;
import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.service.AttributeService;
import be.sandervl.neighbournet.service.DocumentService;
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
import java.util.stream.Collectors;

/**
 * REST controller for managing Document.
 */
@RestController
@RequestMapping("/api")
public class DocumentResource {

    private final Logger log = LoggerFactory.getLogger(DocumentResource.class);

    @Inject
    private DocumentService documentService;

    @Inject
    private AttributeService attributeService;

    /**
     * POST  /documents : Create a new document.
     *
     * @param document the document to create
     * @return the ResponseEntity with status 201 (Created) and with body the new document, or with status 400 (Bad Request) if the document has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/documents")
    @Timed
    public ResponseEntity<Document> createDocument(@Valid @RequestBody Document document) throws URISyntaxException {
        log.debug("REST request to save Document : {}", document);
        if (document.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("document", "idexists", "A new document cannot already have an ID")).body(null);
        }
        Document result = documentService.save(document);
        return ResponseEntity.created(new URI("/api/documents/" + result.getId()))
                             .headers(HeaderUtil.createEntityCreationAlert("document", result.getId().toString()))
                             .body(result);
    }

    /**
     * PUT  /documents : Updates an existing document.
     *
     * @param document the document to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated document,
     * or with status 400 (Bad Request) if the document is not valid,
     * or with status 500 (Internal Server Error) if the document couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/documents")
    @Timed
    public ResponseEntity<Document> updateDocument(@Valid @RequestBody Document document) throws URISyntaxException {
        log.debug("REST request to update Document : {}", document);
        if (document.getId() == null) {
            return createDocument(document);
        }
        Document result = documentService.save(document);
        return ResponseEntity.ok()
                             .headers(HeaderUtil.createEntityUpdateAlert("document", document.getId().toString()))
                             .body(result);
    }

    /**
     * GET  /documents : get all the documents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of documents in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/documents")
    @Timed
    public ResponseEntity<List<Document>> getAllDocuments(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Documents");
        Page<Document> page = documentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/documents");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /documents/:id : get the "id" document.
     *
     * @param id the id of the document to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the document, or with status 404 (Not Found)
     */
    @GetMapping("/documents/{id}")
    @Timed
    public ResponseEntity<Document> getDocument(@PathVariable Long id) {
        log.debug("REST request to get Document : {}", id);
        Document document = documentService.findOne(id);
        List<Attribute> attributes = attributeService.findByDocument(document)
                                                     .stream()
                                                     .sorted((a, b) -> -Boolean.compare(a.getSelector().isIsPrimary(), b.getSelector().isIsPrimary()))
                                                     .collect(Collectors.toList());
        document.setAttributes(attributes);
        return Optional.ofNullable(document)
                       .map(result -> new ResponseEntity<>(
                           result,
                           HttpStatus.OK))
                       .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /documents/:id : delete the "id" document.
     *
     * @param id the id of the document to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/documents/{id}")
    @Timed
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        log.debug("REST request to delete Document : {}", id);
        documentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("document", id.toString())).build();
    }

}
