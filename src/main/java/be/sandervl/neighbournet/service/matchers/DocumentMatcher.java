package be.sandervl.neighbournet.service.matchers;

import be.sandervl.neighbournet.domain.Document;

/**
 * @author: sander
 * @date: 12/12/2016
 */
public interface DocumentMatcher {
    Boolean match(Document a, Document b);

    void findMatches(Document document);
}
