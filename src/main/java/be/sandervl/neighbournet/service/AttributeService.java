package be.sandervl.neighbournet.service;

import be.sandervl.neighbournet.domain.Attribute;
import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.domain.Selector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * Service Interface for managing Attribute.
 */
public interface AttributeService {

    /**
     * Save a attribute.
     *
     * @param attribute the entity to save
     * @return the persisted entity
     */
    Attribute save(Attribute attribute);

    /**
     * Get all the attributes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Attribute> findAll(Pageable pageable);

    /**
     * Get the "id" attribute.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Attribute findOne(Long id);

    /**
     * Delete the "id" attribute.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    Set<Attribute> findByDocument(Document document);

    Attribute findBySelectorAndDocument(Selector selector, Document document);
}
