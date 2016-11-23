package be.sandervl.neighbournet.service.crawler;

import be.sandervl.neighbournet.config.CrawlerProperties;
import be.sandervl.neighbournet.domain.Site;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.authentication.FormAuthInfo;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author: sander
 * @date: 15/11/2016
 */
@Slf4j
@Service
public class CrawlerServiceImpl implements CrawlerService {

    @Autowired
    private CrawlerProperties crawlerProperties;

    @Autowired
    private ApplicationContext context;

    private CrawlController controller;

    @Override
    public void crawlSite(Site site) throws Exception {
        if (controller == null || controller.isFinished()) {
            CrawlConfig config = new CrawlConfig();
            config.setCrawlStorageFolder(crawlerProperties.getCrawlStorageFolder());
            config.setMaxPagesToFetch(crawlerProperties.getMaxPagesToFetch());
            config.setPolitenessDelay(crawlerProperties.getPolitenessDelay());
            config.setResumableCrawling(false);
            config.setAuthInfos(
                Arrays.asList(new FormAuthInfo("breezer007", "breezer007", "http://www.hookers.nl/login.php?do=login", "vb_login_username", "vb_login_password_hint")
                )
            );

            /*
             * Instantiate the controller for this crawl.
             */
            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            controller = new CrawlController(config, pageFetcher, robotstxtServer);
                /*
                 * For each crawl, you need to add some seed urls. These are the first
                 * URLs that are fetched and then the crawler starts following links
                 * which are found in these pages
                 */
            controller.addSeed(site.getSeed());

                /*
                 * Start the crawl. This is a blocking operation, meaning that your code
                 * will reach the line after this only when crawling is finished.
                 */

            // shard stats object for multiple crawlers
            CrawlStats stats = new CrawlStats();
            controller.startNonBlocking(() -> context.getBean(Crawler.class).setUp(site, config, stats), crawlerProperties.getNumberOfCrawlers());
        }
    }
}