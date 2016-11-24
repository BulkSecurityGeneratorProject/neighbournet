package be.sandervl.neighbournet.service.crawler;

import be.sandervl.neighbournet.domain.Site;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

/**
 * @author: sander
 * @date: 17/11/2016
 */
@Controller
@Slf4j
public class SiteCrawlerController {

    @Inject
    private CrawlerService crawlerService;

    @Inject
    SimpMessageSendingOperations messagingTemplate;

    @RequestMapping(method = RequestMethod.POST, value = "/crawler")
    @ResponseBody
    public CrawlStats startCrawl(@RequestParam(name = "id") Site site) {
        log.debug("Starting crawl for site {}", site);
        CrawlStats response = new CrawlStats();
        try {
            crawlerService.crawlSite(site);
        } catch (Exception e) {
            log.error("Exception occured while starting new crawl for site {}", site);
        }
        return response;
    }

    @SubscribeMapping("/topic/getstat")
    @SendTo("/topic/crawlstat")
    public CrawlStats sendCrawlStatus(CrawlStats stats) {
        log.debug("Sending crawl status {}", stats);
        messagingTemplate.convertAndSend("/topic/crawlstat", stats);
        return stats;
    }


}
