package be.sandervl.neighbournet.service.crawler;

import be.sandervl.neighbournet.domain.Site;

/**
 * @author: sander
 * @date: 18/11/2016
 */
public interface Crawler {
    SiteCrawler setUp(Site site, CrawlStats stats);
}
