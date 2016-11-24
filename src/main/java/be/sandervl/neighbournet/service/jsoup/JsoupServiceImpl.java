package be.sandervl.neighbournet.service.jsoup;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: sander
 * @date: 21/11/2016
 */
@Slf4j
@Service
public class JsoupServiceImpl implements JsoupService {

    @Override
    public Optional<Document> getDocumentFromUrl(String url) {
        try {
            return Optional.ofNullable(Jsoup.connect(url).get());
        } catch (IOException e) {
            log.error("Unable to get url {}", url);
            return Optional.empty();
        }
    }


    @Override
    public Set<String> getElementsFromType(Document document, String selector, String attribute) {
        return document.select(selector).stream().map(el -> elementToTextMapper(el, attribute)).collect(Collectors.toSet());
    }

    private String elementToTextMapper(Element element, String attribute) {
        if (org.apache.commons.lang3.StringUtils.isBlank(attribute)) {
            return StringUtils.removeHtmlTags(element.html());
        } else {
            return StringUtils.removeHtmlTags(element.attr(attribute));
        }
    }
}
