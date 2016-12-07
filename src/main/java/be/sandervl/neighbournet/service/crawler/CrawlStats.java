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
    private int numberProcessed = 0, total = 0, numberVisited = 0, crawlersRunning = 0, newDocuments = 0, newAttributes = 0;

    @Getter
    @Setter
    private CrawlStatus status = CrawlStatus.NOT_RUNNING;

    public void incNumberProcessed() {
        this.numberProcessed++;
    }

    public void incNumberVisited() {
        this.numberVisited++;
    }

    public void incCrawlersRunning() {
        this.crawlersRunning++;
        if (this.crawlersRunning > 0) {
            this.status = CrawlStatus.RUNNING;
        }
    }

    public void incNewDocuments() {
        this.newDocuments++;
    }

    public void incNewAttributes() {
        this.newAttributes++;
    }

    public void decCrawlersRunning() {
        this.crawlersRunning--;
        this.status = CrawlStatus.SHUTTING_DOWN;
        if (this.crawlersRunning <= 0) {
            this.status = CrawlStatus.NOT_RUNNING;
        }
    }
}
