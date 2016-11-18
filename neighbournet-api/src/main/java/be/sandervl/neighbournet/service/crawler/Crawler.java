package be.sandervl.neighbournet.service.crawler;

import be.sandervl.neighbournet.domain.Site;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;

/**
 * @author: sander
 * @date: 18/11/2016
 */
public interface Crawler {
    SiteCrawler setUp(Site site, CrawlConfig config);
}
