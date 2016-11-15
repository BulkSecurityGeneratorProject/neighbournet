package be.sandervl.neighbournet.service.crawler;

import be.sandervl.neighbournet.domain.Attribute;
import be.sandervl.neighbournet.domain.Site;
import be.sandervl.neighbournet.service.AttributeService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class SiteCrawler extends WebCrawler {

    private final static Pattern EXTENSIONS_TO_EXCLUDE = Pattern.compile(".*(\\.(css|js|gif|jpg"
        + "|png|mp3|mp3|zip|gz))$");

    private final Pattern pattern;
    private final Site site;
    private final AttributeService attributeService;
    private CrawlStats stats;

    public SiteCrawler(Site site, AttributeService attributeService) {
        this.site = site;
        this.pattern = Pattern.compile(site.getRegex());
        this.attributeService = attributeService;
        this.stats = new CrawlStats();
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
            be.sandervl.neighbournet.domain.Document document = new be.sandervl.neighbournet.domain.Document();
            document.setSite(site);
            document.setCreated(LocalDate.now());
            document.setUrl(url);
            site.getSelectors()
                .forEach(selector -> {
                    Attribute attribute = new Attribute();
                    attribute.setValue(jsoupDocument.select(selector.getValue()).html());
                    attribute.setSelector(selector);
                    attribute.setDocument(document);
                    logger.trace("Found attribute {}", attribute);
                    attributeService.save(attribute);
                });

        } catch (IOException e) {
            logger.error("Unable to get url {}", url, e);
        }
    }

    @Override
    public Object getMyLocalData() {
        return stats;
    }
}
