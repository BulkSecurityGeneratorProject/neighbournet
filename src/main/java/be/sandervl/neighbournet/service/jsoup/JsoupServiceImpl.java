package be.sandervl.neighbournet.service.jsoup;

import be.sandervl.neighbournet.domain.Selector;
import be.sandervl.neighbournet.service.handlers.ProcessorChain;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ProcessorChain processorChain;

    @Override
    public Optional<Document> getDocumentFromUrl(String url) {
        try {
            return Optional.ofNullable(Jsoup.connect(url).validateTLSCertificates(false).get());
        } catch (IOException e) {
            log.error("Unable to get url {} due to {}", url,e.getMessage());
            return Optional.empty();
        }
    }


    @Override
    public Set<String> getElementsFromType(Document jsoupDocument, Selector selector, be.sandervl.neighbournet.domain.Document document) {
        return jsoupDocument
            .select(selector.getValue())
            .stream()
            .map(el -> elementToTextMapper(el, selector.getAttribute(), selector.isChild()))
            .map(val -> processorChain.process(val, selector, document))
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
