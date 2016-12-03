package be.sandervl.neighbournet.service.crawler;

import be.sandervl.neighbournet.domain.Attribute;
import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.domain.Selector;
import be.sandervl.neighbournet.domain.Site;
import be.sandervl.neighbournet.repository.DocumentRepository;
import be.sandervl.neighbournet.repository.SelectorRepository;
import be.sandervl.neighbournet.service.AttributeService;
import be.sandervl.neighbournet.service.handlers.ProcessorChain;
import be.sandervl.neighbournet.service.jsoup.JsoupService;
import be.sandervl.neighbournet.service.jsoup.JsoupServiceImpl;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static be.sandervl.neighbournet.utils.TestObjectCreation.documentFromFileName;
import static be.sandervl.neighbournet.utils.TestObjectCreation.pageFromUrl;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author: sander
 * @date: 23/11/2016
 */
@RunWith(MockitoJUnitRunner.class)
public class SiteCrawlerTest {

    @Spy
    @InjectMocks
    private JsoupService jsoupService = spy(new JsoupServiceImpl());

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

    @Mock
    private ProcessorChain processorChain;

    @Before
    public void setUp() throws Exception {
        siteCrawler = new SiteCrawler();
        initMocks(this);

        doReturn(Optional.of(mock(org.jsoup.nodes.Document.class))).when(jsoupService).getDocumentFromUrl(anyString());

        when(documentRepository.findByUrl(anyString())).thenReturn(Optional.empty());

        when(attributeService.findByDocument(any(Document.class))).thenReturn(Collections.emptySet());

        when(selectorRepository.findBySiteAndParentIsNull(any(Site.class))).thenReturn(Collections.emptySet());

        when(processorChain.process(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);
    }

    @Test
    public void correctUrlShouldBeSaved() throws Exception {
        String fullUrl = "http://www.google.be/my/path?abc=123#subscription";
        Page mock = pageFromUrl(fullUrl);

        siteCrawler.visit(mock);

        ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(documentRepository, times(1)).save(documentCaptor.capture());

        assertEquals("/my/path?abc=123#subscription", documentCaptor.getValue().getUrl());
    }

    @Test
    public void correctUrlShouldBeSaved2() throws Exception {
        String fullUrl = "http://www.google.be/";
        Page mock = pageFromUrl(fullUrl);

        siteCrawler.visit(mock);

        ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(documentRepository, times(1)).save(documentCaptor.capture());

        assertEquals("/", documentCaptor.getValue().getUrl());
    }

    @Test
    public void correctUrlShouldBeSaved3() throws Exception {
        String fullUrl = "http://www.google.be/my/path/is/so/cool";
        Page mock = pageFromUrl(fullUrl);

        siteCrawler.visit(mock);

        ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(documentRepository, times(1)).save(documentCaptor.capture());

        assertEquals("/my/path/is/so/cool", documentCaptor.getValue().getUrl());
    }

    @Test
    public void fullInputSelectorTreeTest() throws Exception {
        org.jsoup.nodes.Document document = documentFromFileName("testinput2.html");

        doReturn(Optional.of(document)).when(jsoupService).getDocumentFromUrl(anyString());

        String fullUrl = "http://www.google.be/my/path/is/so/cool";
        Page mock = pageFromUrl(fullUrl);

        Selector parent = Selector.builder()
                                  .name("post")
                                  .value(".postcontainer")
                                  .build();
        Selector child1 = Selector.builder()
                                  .name("date")
                                  .value(".postdate")
                                  .parent(parent)
                                  .build();
        Selector child2 = Selector.builder()
                                  .name("username")
                                  .value(".postdetails .username")
                                  .parent(parent)
                                  .build();
        parent.setChildren(new HashSet<>(Arrays.asList(child1, child2)));

        when(selectorRepository.findBySiteAndParentIsNull(any(Site.class))).thenReturn(Arrays.asList(
            parent
        ));

        siteCrawler.visit(mock);

        ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(documentRepository, times(1)).save(documentCaptor.capture());
        ArgumentCaptor<Attribute> attributeCaptor = ArgumentCaptor.forClass(Attribute.class);

        verify(attributeService, times(6)).save(attributeCaptor.capture());
        assertEquals(6, attributeCaptor.getAllValues().size());
    }

    private void checkAttribute(ArgumentCaptor<Attribute> attributeCaptor, int index, int relativeSize, String value) {
        assertEquals(relativeSize, attributeCaptor.getAllValues().get(index).getRelatives().size());
        assertEquals(value, attributeCaptor.getAllValues().get(index).getValue());
    }


}
