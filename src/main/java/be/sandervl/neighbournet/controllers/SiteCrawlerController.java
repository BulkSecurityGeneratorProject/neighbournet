package be.sandervl.neighbournet.controllers;

import be.sandervl.neighbournet.domain.Site;
import be.sandervl.neighbournet.service.crawler.CrawlStats;
import be.sandervl.neighbournet.service.crawler.CrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(method = RequestMethod.GET, value = "/crawler")
    @ResponseBody
    public CrawlStats getCrawler(@RequestParam(name = "id") Site site) {
        return crawlerService.getStats(site).orElse(new CrawlStats());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/crawler")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void stopCrawler(@RequestParam(name = "id") Site site) {
        crawlerService.stopCrawler(site);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/crawler")
    @ResponseBody
    public CrawlStats startCrawler(@RequestParam(name = "id") Site site) {
        log.debug("Starting crawl for site {}", site);
        CrawlStats response = new CrawlStats();
        try {
            crawlerService.startCrawler(site);
        } catch (Exception e) {
            log.error("Exception occured while starting new crawl for site {}", site, e);
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
