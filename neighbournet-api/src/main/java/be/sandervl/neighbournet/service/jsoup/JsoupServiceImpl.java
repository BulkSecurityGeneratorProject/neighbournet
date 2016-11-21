package be.sandervl.neighbournet.service.jsoup;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: sander
 * @date: 21/11/2016
 */
@Slf4j
public class JsoupServiceImpl implements JsoupService {
    @Override
    public Optional<Document> getDocumentFromUrl(String url) {
        try {
            return Optional.ofNullable(Jsoup.connect(url).get());
        } catch (IOException e) {
            log.error("Unable to get url {}", url, e);
            return Optional.empty();
        }
    }

    @Override
    public <T> T getElementFromType(Document document, String selector, Class<T> type) {
        String attributeValueWithHtml = document.select(selector).html();
        return (T) StringUtils.removeHtmlTags(attributeValueWithHtml);
    }


    @Override
    public <T> T getAttributeFromType(Document document, String selector, String attrName, Class<T> type) {
        return (T) document.select(selector).attr(attrName);
    }

    @Override
    public <T> Set<T> getAttributesFromType(Document document, String selector, String attribute, Class<T> type) {
        return document.select(selector).stream().map(element -> (T) element.attr(attribute)).collect(Collectors.toSet());
    }
}
