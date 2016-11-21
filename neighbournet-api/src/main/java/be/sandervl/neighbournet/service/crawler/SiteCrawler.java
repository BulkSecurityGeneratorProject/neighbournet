package be.sandervl.neighbournet.service.crawler;

import be.sandervl.neighbournet.domain.Attribute;
import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.domain.Selector;
import be.sandervl.neighbournet.domain.Site;
import be.sandervl.neighbournet.repository.DocumentRepository;
import be.sandervl.neighbournet.repository.SelectorRepository;
import be.sandervl.neighbournet.service.AttributeService;
import be.sandervl.neighbournet.service.jsoup.JsoupService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
@Scope("prototype")
public class SiteCrawler extends WebCrawler implements Crawler {

    private final static Pattern EXTENSIONS_TO_EXCLUDE = Pattern.compile(".*(\\.(css|js|gif|jpg"
        + "|png|mp3|mp3|zip|gz))$");

    private Pattern pattern;
    private Site site;

    private CrawlStats stats;
    private CrawlConfig config;

    @Inject
    private AttributeService attributeService;

    @Inject
    private SelectorRepository selectorRepository;

    @Inject
    private DocumentRepository documentRepository;

    @Inject
    private SiteCrawlerController controller;

    @Inject
    private JsoupService jsoupService;

    @Override
    public SiteCrawler setUp(Site site, CrawlConfig config, CrawlStats crawlStats) {
        this.site = site;
        this.config = config;
        this.stats = crawlStats;
        this.pattern = Pattern.compile(site.getRegex());
        return this;
    }

    @Override
    public void onStart() {
        this.stats.incCrawlersRunning();
        this.stats.setTotal(this.config.getMaxPagesToFetch());
        super.onStart();
    }

    @Override
    public void onBeforeExit() {
        this.stats.decCrawlersRunning();
        super.onBeforeExit();
    }

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        this.stats.incNumberVisited();
        String href = url.getURL().toLowerCase();
        return !EXTENSIONS_TO_EXCLUDE.matcher(href).matches()
            && pattern.matcher(href).matches();
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        logger.debug("Fetching URL: " + url);
        this.stats.incNumberProcessed();
        jsoupService.getDocumentFromUrl(url).ifPresent(jsoupDocument -> processDocument(page, jsoupDocument));
    }

    private void processDocument(Page page, org.jsoup.nodes.Document jsoupDocument) {
        Document document = documentRepository
            .findByUrl(page.getWebURL().getURL())
            .orElse(new Document());
        document.setSite(site);
        document.setCreated(LocalDate.now());
        document.setUrl(page.getWebURL().getPath());
        documentRepository.save(document);
        Set<Attribute> exitingAttributes = attributeService.findByDocument(document);
        selectorRepository.findBySite(site)
                          .forEach(selector -> {
                              Attribute attribute = exitingAttributes
                                  .stream()
                                  .filter(attributesFromSelectorName(selector))
                                  .findAny()
                                  .orElse(new Attribute());
                              String value = jsoupService.getElement(jsoupDocument, selector.getValue());
                              attribute.setValue(value);
                              attribute.setSelector(selector);
                              attribute.setDocument(document);
                              logger.trace("Found attribute {}", attribute);
                              if (StringUtils.isNotBlank(attribute.getValue())) {
                                  attributeService.save(attribute);
                              }
                          });
        controller.sendCrawlStatus(this.stats);
    }


    private Predicate<Attribute> attributesFromSelectorName(Selector selector) {
        return attr -> attr.getSelector().getName().equals(selector.getName());
    }

    @Override
    public Object getMyLocalData() {
        return stats;
    }
}
