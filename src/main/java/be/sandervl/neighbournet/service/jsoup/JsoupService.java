package be.sandervl.neighbournet.service.jsoup;

import com.fasterxml.jackson.databind.JsonNode;
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
     * Select from a Jsoup document the String values from a given CSS-selector and attribute value.  If the given
     * attriute value is blank, it will be discared and the value of the CSS-selector will be returned
     */
    Set<String> getElementsFromType(Document document, String selector, String attribute);

}
