package be.sandervl.neighbournet.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "crawler")
public class CrawlerProperties {

    @Getter
    @Setter
    private String crawlStorageFolder;

    @Getter
    @Setter
    private int numberOfCrawlers;

    @Getter
    @Setter
    private int maxPagesToFetch;

}
