package be.sandervl.neighbournet.web.rest;

import be.sandervl.neighbournet.NeighbournetApiApp;
import be.sandervl.neighbournet.domain.FeedItem;
import be.sandervl.neighbournet.repository.FeedItemRepository;
import be.sandervl.neighbournet.service.FeedItemService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FeedItemResource REST controller.
 *
 * @see FeedItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NeighbournetApiApp.class)
public class FeedItemResourceIntTest {

    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";

    @Inject
    private FeedItemRepository feedItemRepository;

    @Inject
    private FeedItemService feedItemService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFeedItemMockMvc;

    private FeedItem feedItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FeedItemResource feedItemResource = new FeedItemResource();
        ReflectionTestUtils.setField(feedItemResource, "feedItemService", feedItemService);
        this.restFeedItemMockMvc = MockMvcBuilders.standaloneSetup(feedItemResource)
                                                  .setCustomArgumentResolvers(pageableArgumentResolver)
                                                  .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedItem createEntity(EntityManager em) {
        FeedItem feedItem = new FeedItem()
            .text(DEFAULT_TEXT);
        return feedItem;
    }

    @Before
    public void initTest() {
        feedItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createFeedItem() throws Exception {
        int databaseSizeBeforeCreate = feedItemRepository.findAll().size();

        // Create the FeedItem

        restFeedItemMockMvc.perform(post("/api/feed-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feedItem)))
                           .andExpect(status().isCreated());

        // Validate the FeedItem in the database
        List<FeedItem> feedItems = feedItemRepository.findAll();
        assertThat(feedItems).hasSize(databaseSizeBeforeCreate + 1);
        FeedItem testFeedItem = feedItems.get(feedItems.size() - 1);
        assertThat(testFeedItem.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedItemRepository.findAll().size();
        // set the field null
        feedItem.setText(null);

        // Create the FeedItem, which fails.

        restFeedItemMockMvc.perform(post("/api/feed-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feedItem)))
                           .andExpect(status().isBadRequest());

        List<FeedItem> feedItems = feedItemRepository.findAll();
        assertThat(feedItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFeedItems() throws Exception {
        // Initialize the database
        feedItemRepository.saveAndFlush(feedItem);

        // Get all the feedItems
        restFeedItemMockMvc.perform(get("/api/feed-items?sort=id,desc"))
                           .andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                           .andExpect(jsonPath("$.[*].id").value(hasItem(feedItem.getId().intValue())))
                           .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getFeedItem() throws Exception {
        // Initialize the database
        feedItemRepository.saveAndFlush(feedItem);

        // Get the feedItem
        restFeedItemMockMvc.perform(get("/api/feed-items/{id}", feedItem.getId()))
                           .andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                           .andExpect(jsonPath("$.id").value(feedItem.getId().intValue()))
                           .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFeedItem() throws Exception {
        // Get the feedItem
        restFeedItemMockMvc.perform(get("/api/feed-items/{id}", Long.MAX_VALUE))
                           .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFeedItem() throws Exception {
        // Initialize the database
        feedItemService.save(feedItem);

        int databaseSizeBeforeUpdate = feedItemRepository.findAll().size();

        // Update the feedItem
        FeedItem updatedFeedItem = feedItemRepository.findOne(feedItem.getId());
        updatedFeedItem
            .text(UPDATED_TEXT);

        restFeedItemMockMvc.perform(put("/api/feed-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFeedItem)))
                           .andExpect(status().isOk());

        // Validate the FeedItem in the database
        List<FeedItem> feedItems = feedItemRepository.findAll();
        assertThat(feedItems).hasSize(databaseSizeBeforeUpdate);
        FeedItem testFeedItem = feedItems.get(feedItems.size() - 1);
        assertThat(testFeedItem.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void deleteFeedItem() throws Exception {
        // Initialize the database
        feedItemService.save(feedItem);

        int databaseSizeBeforeDelete = feedItemRepository.findAll().size();

        // Get the feedItem
        restFeedItemMockMvc.perform(delete("/api/feed-items/{id}", feedItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
                           .andExpect(status().isOk());

        // Validate the database is empty
        List<FeedItem> feedItems = feedItemRepository.findAll();
        assertThat(feedItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
