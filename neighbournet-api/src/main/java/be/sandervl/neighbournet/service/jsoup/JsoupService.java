package be.sandervl.neighbournet.service.jsoup;

import org.jsoup.nodes.Document;

import java.util.Optional;
import java.util.Set;

/**
 * @author: sander
 * @date: 21/11/2016
 */
public interface JsoupService {

    Optional<Document> getDocumentFromUrl(String url);

    <T> T getElementFromType(Document document, String selector, Class<T> type);

    <T> T getElementFromType(Document document, String selector, String attribute, Class<T> type);

    <T> Set<T> getAttributesFromType(Document document, String selector, String attribute, Class<T> type);

    default String getElement(Document document, String selector) {
        return getElementFromType(document, selector, String.class);
    }

    default String getElement(Document document, String selector, String attribute) {
        return getElementFromType(document, selector, attribute, String.class);
    }
}
