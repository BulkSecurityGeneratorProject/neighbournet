package be.sandervl.neighbournet.service.impl;

import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.domain.Site;
import be.sandervl.neighbournet.repository.DocumentRepository;
import be.sandervl.neighbournet.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Set;

/**
 * Service Implementation for managing Document.
 */
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    @Inject
    private DocumentRepository documentRepository;

    /**
     * Save a document.
     *
     * @param document the entity to save
     * @return the persisted entity
     */
    public Document save(Document document) {
        log.debug("Request to save Document : {}", document);
        Document result = documentRepository.save(document);
        return result;
    }

    /**
     *  Get all the documents.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Document> findAll(Pageable pageable) {
        log.debug("Request to get all Documents");
        Page<Document> result = documentRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one document by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Document findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        Document document = documentRepository.findOneWithEagerRelationships(id);
        return document;
    }

    /**
     *  Delete the  document by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentRepository.delete(id);
    }

    @Override
    public Set<Document> findBySite(Site site) {
        log.debug("Request to find by Site : {}", site);
        return documentRepository.findBySite(site);
    }
}
