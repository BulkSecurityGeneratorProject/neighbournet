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
    private int numberProcessed = 0;
    @Getter
    @Setter
    private int total = 0;
    @Getter
    @Setter
    private int numberVisited = 0;
    @Getter
    @Setter
    private int crawlersRunning = 0;
    @Getter
    @Setter
    private int newDocuments = 0;
    @Getter
    @Setter
    private int newAttributes = 0;

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
