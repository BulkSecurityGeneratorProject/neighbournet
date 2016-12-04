package be.sandervl.neighbournet.service.jsoup;

import be.sandervl.neighbournet.domain.Selector;
import org.jsoup.nodes.Document;

import java.util.Optional;
import java.util.Set;

/**
 * @author: sander
 * @date: 21/11/2016
 */
public interface JsoupService {

    Optional<Document> getDocumentFromUrl(String url);

    /**
     * Select from a Jsoup document the values from a given CSS-selector and attribute.  If the given
     * attriute value is blank, it will be discared and the value of the CSS-selector will be returned.
     *
     * If innerHtml is set to true, only the inner html of the CSS-selector will be returned.
     */
    Set<String> getElementsFromType(Document jsoupDocument, Selector selector, be.sandervl.neighbournet.domain.Document document);

}
