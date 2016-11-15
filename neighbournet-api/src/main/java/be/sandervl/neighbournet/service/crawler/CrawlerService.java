package be.sandervl.neighbournet.service.crawler;

import be.sandervl.neighbournet.domain.Site;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author: sander
 * @date: 15/11/2016
 */
@Service
public interface CrawlerService {

    void crawlSite(Site site) throws Exception;

    Optional<CrawlStats> getStats();
}
