package be.sandervl.neighbournet.service.jsoup;

import be.sandervl.neighbournet.service.handlers.ProcessorChain;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
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

    @Autowired
    private ProcessorChain processorChain;

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
    public Set<String> getElementsFromType(Document document, String selector, String attribute, boolean innerHtml) {
        return document
            .select(selector)
            .stream()
            .map(el -> elementToTextMapper(el,attribute, innerHtml))
            .map(processorChain::process)
            .collect(Collectors.toSet());
    }

    public String elementToTextMapper(Element element, String attribute, boolean innerHtml) {
        if (org.apache.commons.lang3.StringUtils.isBlank(attribute)) {
            return innerHtml ? StringUtils.removeHtmlTags(element.html()) : element.toString();
        } else {
            return StringUtils.removeHtmlTags(element.attr(attribute));
        }
    }
}
