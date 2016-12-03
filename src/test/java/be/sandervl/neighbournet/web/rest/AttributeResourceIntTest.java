package be.sandervl.neighbournet.web.rest;

import be.sandervl.neighbournet.NeighbournetApiApp;

import be.sandervl.neighbournet.domain.Attribute;
import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.domain.Selector;
import be.sandervl.neighbournet.repository.AttributeRepository;
import be.sandervl.neighbournet.service.AttributeService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AttributeResource REST controller.
 *
 * @see AttributeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NeighbournetApiApp.class)
public class AttributeResourceIntTest {

    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";

    @Inject
    private AttributeRepository attributeRepository;

    @Inject
    private AttributeService attributeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAttributeMockMvc;

    private Attribute attribute;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AttributeResource attributeResource = new AttributeResource();
        ReflectionTestUtils.setField(attributeResource, "attributeService", attributeService);
        this.restAttributeMockMvc = MockMvcBuilders.standaloneSetup(attributeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attribute createEntity(EntityManager em) {
        Attribute attribute = new Attribute()
                .value(DEFAULT_VALUE);
        // Add required entity
        Document document = DocumentResourceIntTest.createEntity(em);
        em.persist(document);
        em.flush();
        attribute.setDocument(document);
        // Add required entity
        Selector selector = SelectorResourceIntTest.createEntity(em);
        em.persist(selector);
        em.flush();
        attribute.setSelector(selector);
        return attribute;
    }

    @Before
    public void initTest() {
        attribute = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttribute() throws Exception {
        int databaseSizeBeforeCreate = attributeRepository.findAll().size();

        // Create the Attribute

        restAttributeMockMvc.perform(post("/api/attributes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(attribute)))
                .andExpect(status().isCreated());

        // Validate the Attribute in the database
        List<Attribute> attributes = attributeRepository.findAll();
        assertThat(attributes).hasSize(databaseSizeBeforeCreate + 1);
        Attribute testAttribute = attributes.get(attributes.size() - 1);
        assertThat(testAttribute.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllAttributes() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get all the attributes
        restAttributeMockMvc.perform(get("/api/attributes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(attribute.getId().intValue())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getAttribute() throws Exception {
        // Initialize the database
        attributeRepository.saveAndFlush(attribute);

        // Get the attribute
        restAttributeMockMvc.perform(get("/api/attributes/{id}", attribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attribute.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAttribute() throws Exception {
        // Get the attribute
        restAttributeMockMvc.perform(get("/api/attributes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttribute() throws Exception {
        // Initialize the database
        attributeService.save(attribute);

        int databaseSizeBeforeUpdate = attributeRepository.findAll().size();

        // Update the attribute
        Attribute updatedAttribute = attributeRepository.findOne(attribute.getId());
        updatedAttribute
                .value(UPDATED_VALUE);

        restAttributeMockMvc.perform(put("/api/attributes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAttribute)))
                .andExpect(status().isOk());

        // Validate the Attribute in the database
        List<Attribute> attributes = attributeRepository.findAll();
        assertThat(attributes).hasSize(databaseSizeBeforeUpdate);
        Attribute testAttribute = attributes.get(attributes.size() - 1);
        assertThat(testAttribute.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteAttribute() throws Exception {
        // Initialize the database
        attributeService.save(attribute);

        int databaseSizeBeforeDelete = attributeRepository.findAll().size();

        // Get the attribute
        restAttributeMockMvc.perform(delete("/api/attributes/{id}", attribute.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Attribute> attributes = attributeRepository.findAll();
        assertThat(attributes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
