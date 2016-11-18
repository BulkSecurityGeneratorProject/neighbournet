package be.sandervl.neighbournet.service.crawler;

import lombok.Data;

/**
 * @author: sander
 * @date: 15/11/2016
 */
@Data
public class CrawlStats {

    private int numberProcessed = 0;
    private int total = 0, numberVisited = 0;
    private CrawlStatus status = CrawlStatus.NOT_RUNNING;

    public synchronized void incNumberProcessed() {
        this.numberProcessed++;
    }

    public synchronized void incNumberVisited() {
        this.numberVisited++;
    }
}
