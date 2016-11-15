package be.sandervl.neighbournet.service.crawler;

import lombok.Data;

/**
 * @author: sander
 * @date: 15/11/2016
 */
@Data
public class CrawlStats {

    private int numberProcessed;

    public void incNumberProcessed() {
        this.numberProcessed++;
    }

}
