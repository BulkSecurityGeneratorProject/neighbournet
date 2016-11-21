package be.sandervl.neighbournet.service.jsoup;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author: sander
 * @date: 21/11/2016
 */
public class JsoupServiceTest {

    private JsoupService jsoupService;

    @Before
    public void setUp() throws Exception {
        jsoupService = new JsoupServiceImpl();
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
        assertTrue(actualElem.contains("KOMMILFOO"));
    }

}
