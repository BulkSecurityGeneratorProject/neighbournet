package be.sandervl.neighbournet.service.crawler;

import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.domain.Site;
import be.sandervl.neighbournet.repository.DocumentRepository;
import be.sandervl.neighbournet.repository.SelectorRepository;
import be.sandervl.neighbournet.service.AttributeService;
import be.sandervl.neighbournet.service.jsoup.JsoupService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author: sander
 * @date: 23/11/2016
 */
@RunWith(MockitoJUnitRunner.class)
public class SiteCrawlerTest {

    @Mock
    private JsoupService jsoupService;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private AttributeService attributeService;

    @Mock
    private SelectorRepository selectorRepository;

    @Mock
    private SiteCrawlerController siteCrawlerController;

    @InjectMocks
    private SiteCrawler siteCrawler;

    @Before
    public void setUp() throws Exception {
        siteCrawler = new SiteCrawler();
        initMocks(this);

        when(jsoupService.getDocumentFromUrl(anyString())).thenReturn(Optional.of(mock(org.jsoup.nodes.Document.class)));

        when(documentRepository.findByUrl(anyString())).thenReturn(Optional.empty());

        when(attributeService.findByDocument(any(Document.class))).thenReturn(Collections.emptySet());

        when(selectorRepository.findBySite(any(Site.class))).thenReturn(Collections.emptySet());
    }

    @Test
    public void correctUrlShouldBeSaved() throws Exception {
        String fullUrl = "http://www.google.be/my/path?abc=123#subscription";
        Page mock = createPageMock(fullUrl);

        siteCrawler.visit(mock);

        ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(documentRepository, times(1)).save(documentCaptor.capture());

        assertEquals("/my/path?abc=123#subscription", documentCaptor.getValue().getUrl());
    }

    @Test
    public void correctUrlShouldBeSaved2() throws Exception {
        String fullUrl = "http://www.google.be/";
        Page mock = createPageMock(fullUrl);

        siteCrawler.visit(mock);

        ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(documentRepository, times(1)).save(documentCaptor.capture());

        assertEquals("/", documentCaptor.getValue().getUrl());
    }

    @Test
    public void correctUrlShouldBeSaved3() throws Exception {
        String fullUrl = "http://www.google.be/my/path/is/so/cool";
        Page mock = createPageMock(fullUrl);

        siteCrawler.visit(mock);

        ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(documentRepository, times(1)).save(documentCaptor.capture());

        assertEquals("/my/path/is/so/cool", documentCaptor.getValue().getUrl());
    }


    private Page createPageMock(String fullUrl) {
        Page mock = mock(Page.class);
        WebURL webURL = new WebURL();
        webURL.setURL(fullUrl);
        when(mock.getWebURL()).thenReturn(webURL);
        return mock;
    }

}
