package be.sandervl.neighbournet.service.crawler;


import lombok.Getter;
import lombok.Setter;

/**
 * @author: sander
 * @date: 15/11/2016
 */
public class CrawlStats {

    @Getter
    @Setter
    private int numberProcessed = 0, total = 0, numberVisited = 0, crawlersRunning = 0;

    private CrawlStatus status = CrawlStatus.NOT_RUNNING;

    public synchronized void incNumberProcessed() {
        this.numberProcessed++;
    }

    public synchronized void incNumberVisited() {
        this.numberVisited++;
    }

    public synchronized void incCrawlersRunning() {
        this.crawlersRunning++;
        if (this.crawlersRunning > 0) {
            this.status = CrawlStatus.RUNNING;
        }
    }

    public synchronized void decCrawlersRunning() {
        this.crawlersRunning++;
        if (this.crawlersRunning <= 0) {
            this.status = CrawlStatus.NOT_RUNNING;
        }
    }
}
