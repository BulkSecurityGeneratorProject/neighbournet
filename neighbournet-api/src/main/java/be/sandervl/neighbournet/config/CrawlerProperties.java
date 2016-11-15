package be.sandervl.neighbournet.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

import javax.inject.Inject;

@ConfigurationProperties(prefix = "crawler")
public class CrawlerProperties {

    @Inject
    private Environment env;

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
