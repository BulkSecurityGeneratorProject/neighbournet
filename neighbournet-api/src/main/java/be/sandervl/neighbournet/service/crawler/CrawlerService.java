package be.sandervl.neighbournet.service.crawler;

import be.sandervl.neighbournet.domain.Site;

/**
 * @author: sander
 * @date: 15/11/2016
 */
public interface CrawlerService {

    void crawlSite(Site site) throws Exception;
}
