package be.sandervl.neighbournet.service;

import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.domain.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * Service Interface for managing Document.
 */
public interface DocumentService {

    /**
     * Save a document.
     *
     * @param document the entity to save
     * @return the persisted entity
     */
    Document save(Document document);

    /**
     *  Get all the documents.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Document> findAll(Pageable pageable);

    /**
     *  Get the "id" document.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Document findOne(Long id);

    /**
     *  Delete the "id" document.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    Set<Document> findBySite(Site site);
}
