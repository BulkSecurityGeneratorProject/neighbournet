package be.sandervl.neighbournet.service.impl;

import be.sandervl.neighbournet.domain.Attribute;
import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.domain.Selector;
import be.sandervl.neighbournet.repository.AttributeRepository;
import be.sandervl.neighbournet.service.AttributeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * Service Implementation for managing Attribute.
 */
@Service
@Transactional
public class AttributeServiceImpl implements AttributeService{

    private final Logger log = LoggerFactory.getLogger(AttributeServiceImpl.class);

    @Inject
    private AttributeRepository attributeRepository;

    /**
     * Save a attribute.
     *
     * @param attribute the entity to save
     * @return the persisted entity
     */
    public Attribute save(Attribute attribute) {
        log.debug("Request to save Attribute : {}", attribute);
        Attribute result = attributeRepository.save(attribute);
        return result;
    }

    /**
     *  Get all the attributes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Attribute> findAll(Pageable pageable, Boolean withRelatives) {
        log.debug("Request to get all Attributes");
        if (withRelatives != null && withRelatives) {
            List<Attribute> allWithRelatives = attributeRepository.findAllWithRelatives();
            int start = pageable.getOffset();
            int end = (start + pageable.getPageSize()) > allWithRelatives.size() ? allWithRelatives.size() : (start + pageable.getPageSize());
            Page<Attribute> result = new PageImpl<>(allWithRelatives.subList(start, end), pageable, allWithRelatives.size());
            return result;
        } else {
            return attributeRepository.findAll(pageable);
        }
    }

    /**
     *  Get one attribute by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Attribute findOne(Long id) {
        log.debug("Request to get Attribute : {}", id);
        Attribute attribute = attributeRepository.findOneWithEagerRelationships(id);
        return attribute;
    }

    /**
     *  Delete the  attribute by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Attribute : {}", id);
        attributeRepository.delete(id);
    }

    @Override
    public Set<Attribute> findByDocument(Document document) {
        log.debug("Request to get Attribute by document {} : ", document);
        return attributeRepository.findByDocument(document);
    }

    @Override
    public Attribute findBySelectorAndDocument(Selector selector, Document document) {
        log.debug("Request to get Attribute by document {} : ", document);
        return attributeRepository.findBySelectorAndDocument(selector, document);

    }
}
