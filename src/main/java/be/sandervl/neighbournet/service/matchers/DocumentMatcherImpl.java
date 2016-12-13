package be.sandervl.neighbournet.service.matchers;

import be.sandervl.neighbournet.domain.Attribute;
import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.repository.SiteRepository;
import be.sandervl.neighbournet.service.AttributeService;
import be.sandervl.neighbournet.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DocumentMatcherImpl implements DocumentMatcher {

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private AttributeService attributeService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Boolean match(Document a, Document b) {
        return attributeService.findByDocument(a).stream()
                               .filter(attribute -> attribute.getSelector().isIsPrimary())
                               .allMatch(attribute -> attributeService.findByDocument(b).stream()
                                                                      .filter(other -> attribute.getSelector().equals(other.getSelector()))
                                                                      .findFirst()
                                                                      .map(other -> findAttributeMatcher(attribute)
                                                                          .map(matcher -> matcher.match(attribute, other))
                                                                          .orElse(false))
                                                                      .orElse(false));
    }

    private Optional<AttributeMatcher> findAttributeMatcher(Attribute attribute) {
        Class matcherClass = attribute.getSelector().getMatcher().getMatcherClass();
        try {
            return
                Optional.ofNullable((AttributeMatcher) applicationContext.getBean(matcherClass));
        } catch (ClassCastException | BeansException ex) {
            log.error("No suitable bean could be found for {}", matcherClass, ex);
            return Optional.empty();
        }
    }

    @Override
    public void findMatches(Document document) {
        Set<Document> matches =
            siteRepository.findAll()
                          .parallelStream()
                          .flatMap(site -> documentService.findBySite(site).parallelStream()
                                                          .filter(toCompare -> !toCompare.equals(document))
                                                          .filter(toCompare -> this.match(document, toCompare))
                          ).collect(Collectors.toSet());
        if (!matches.isEmpty()) {
            log.debug("Found {} matches for document {}", matches.size(), document);
            document.setMatches(matches);
            documentService.save(document);
        }
    }
}
