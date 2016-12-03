package be.sandervl.neighbournet.web.rest;

import be.sandervl.neighbournet.NeighbournetApiApp;
import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.repository.DocumentRepository;
import be.sandervl.neighbournet.service.DocumentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DocumentResource REST controller.
 *
 * @see DocumentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NeighbournetApiApp.class)
public class DocumentResourceIntTest {

    private static final LocalDate DEFAULT_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    @Inject
    private DocumentRepository documentRepository;

    @Inject
    private DocumentService documentService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDocumentMockMvc;

    private Document document;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DocumentResource documentResource = new DocumentResource();
        ReflectionTestUtils.setField(documentResource, "documentService", documentService);
        this.restDocumentMockMvc = MockMvcBuilders.standaloneSetup(documentResource)
                                                  .setCustomArgumentResolvers(pageableArgumentResolver)
                                                  .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createEntity(EntityManager em) {
        Document document = new Document()
            .created(DEFAULT_CREATED)
            .url(DEFAULT_URL);
        return document;
    }

    @Before
    public void initTest() {
        document = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocument() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Document

        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(document)))
                           .andExpect(status().isCreated());

        // Validate the Document in the database
        List<Document> documents = documentRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeCreate + 1);
        Document testDocument = documents.get(documents.size() - 1);
        assertThat(testDocument.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testDocument.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setUrl(null);

        // Create the Document, which fails.

        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(document)))
                           .andExpect(status().isBadRequest());

        List<Document> documents = documentRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocuments() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documents
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc"))
                           .andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                           .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
                           .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
                           .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void getDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", document.getId()))
                           .andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                           .andExpect(jsonPath("$.id").value(document.getId().intValue()))
                           .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
                           .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", Long.MAX_VALUE))
                           .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocument() throws Exception {
        // Initialize the database
        documentService.save(document);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document
        Document updatedDocument = documentRepository.findOne(document.getId());
        updatedDocument
            .created(UPDATED_CREATED)
            .url(UPDATED_URL);

        restDocumentMockMvc.perform(put("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDocument)))
                           .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documents = documentRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documents.get(documents.size() - 1);
        assertThat(testDocument.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testDocument.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void deleteDocument() throws Exception {
        // Initialize the database
        documentService.save(document);

        int databaseSizeBeforeDelete = documentRepository.findAll().size();

        // Get the document
        restDocumentMockMvc.perform(delete("/api/documents/{id}", document.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
                           .andExpect(status().isOk());

        // Validate the database is empty
        List<Document> documents = documentRepository.findAll();
        assertThat(documents).hasSize(databaseSizeBeforeDelete - 1);
    }
}
