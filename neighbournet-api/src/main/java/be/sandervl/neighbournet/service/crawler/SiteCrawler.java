package be.sandervl.neighbournet.service.crawler;

import be.sandervl.neighbournet.domain.Attribute;
import be.sandervl.neighbournet.domain.Selector;
import be.sandervl.neighbournet.domain.Site;
import be.sandervl.neighbournet.repository.DocumentRepository;
import be.sandervl.neighbournet.repository.SelectorRepository;
import be.sandervl.neighbournet.service.AttributeService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
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

    private CrawlStats stats = new CrawlStats();
    private CrawlConfig config;

    @Inject
    private AttributeService attributeService;

    @Inject
    private SelectorRepository selectorRepository;

    @Inject
    private DocumentRepository documentRepository;

    @Inject
    private SiteCrawlerController controller;

    @Override
    public SiteCrawler setUp(Site site, CrawlConfig config) {
        this.site = site;
        this.config = config;
        this.pattern = Pattern.compile(site.getRegex());
        return this;
    }

    @Override
    public void onStart() {
        this.stats = new CrawlStats();
        this.stats.setStatus(CrawlStatus.RUNNING);
        this.stats.setTotal(this.config.getMaxPagesToFetch());
        super.onStart();
    }

    @Override
    public void onBeforeExit() {
        this.stats.setStatus(CrawlStatus.NOT_RUNNING);
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
        try {
            Document jsoupDocument = Jsoup.connect(url).get();
            be.sandervl.neighbournet.domain.Document document = documentRepository
                .findByUrl(url)
                .orElse(new be.sandervl.neighbournet.domain.Document());
            document.setSite(site);
            document.setCreated(LocalDate.now());
            document.setUrl(url);
            documentRepository.save(document);
            Set<Attribute> exitingAttributes = attributeService.findByDocument(document);
            selectorRepository.findBySite(site)
                              .forEach(selector -> {
                                  Attribute attribute = exitingAttributes
                                      .stream()
                                      .filter(attributesFromSelectorName(selector))
                                      .findAny()
                                      .orElse(new Attribute());
                                  String attributeValueWithHtml = jsoupDocument.select(selector.getValue()).html();
                                  attribute.setValue(new HtmlToPlainText().getPlainText(Jsoup.parse(attributeValueWithHtml)));
                                  attribute.setSelector(selector);
                                  attribute.setDocument(document);
                                  logger.trace("Found attribute {}", attribute);
                                  if (StringUtils.isNotBlank(attribute.getValue())) {
                                      attributeService.save(attribute);
                                  }
                              });
            controller.sendCrawlStatus(this.stats);
        } catch (IOException e) {
            logger.error("Unable to get url {}", url, e);
        }
    }

    private Predicate<Attribute> attributesFromSelectorName(Selector selector) {
        return attr -> attr.getSelector().getName().equals(selector.getName());
    }

    @Override
    public Object getMyLocalData() {
        return stats;
    }
}
