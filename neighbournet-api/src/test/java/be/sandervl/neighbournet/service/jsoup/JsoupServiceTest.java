package be.sandervl.neighbournet.service.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * @author: sander
 * @date: 21/11/2016
 */
public class JsoupServiceTest {

    private JsoupService jsoupService;

    @Before
    public void setUp() throws Exception {
        jsoupService = spy(new JsoupServiceImpl());
        ClassLoader classLoader = getClass().getClassLoader();
        File input = new File(classLoader.getResource("testinput.html").getFile());
        Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");

        doReturn(Optional.of(doc)).when(jsoupService).getDocumentFromUrl(anyString());
    }

    @Test
    public void getDocumentFromUrl() throws Exception {
        Optional<Document> actualDoc = jsoupService.getDocumentFromUrl("http://nl.resto.be/restaurant/antwerpen/2000-antwerpen-centrum/1345-kommilfoo/");

        assertTrue(actualDoc.isPresent());
    }

    @Test
    public void getAttributeFromType() throws Exception {
        Optional<Document> actualDoc = jsoupService.getDocumentFromUrl("http://nl.resto.be/restaurant/antwerpen/2000-antwerpen-centrum/1345-kommilfoo/");
        Set<String> actualElem = jsoupService.getElementsFromType(actualDoc.get(), ".h-mobile-hidden img[data-src]", "data-src");

        assertNotNull(actualElem);
        assertTrue(actualElem.size() > 0);
        assertTrue(actualElem.contains("https://images.resto.com/view?iid=resto.be:ff45f473-6320-4a71-b596-35c29301e4a3&context=default&width=1400&height=605"));
    }

    @Test
    public void getHtmlFromType() throws Exception {
        Optional<Document> actualDoc = jsoupService.getDocumentFromUrl("http://nl.resto.be/restaurant/antwerpen/2000-antwerpen-centrum/1345-kommilfoo/");
        Set<String> actualElem = jsoupService.getElementsFromType(actualDoc.get(), "h1.cwb-restaurant-name", "");

        assertNotNull(actualElem);
        assertTrue(actualElem.size() > 0);
        assertTrue(actualElem.contains("Kommilfoo"));
    }

}
