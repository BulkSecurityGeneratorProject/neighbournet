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

    Set<String> getElementsFromType(Document document, String selector, String attribute);

}
