package be.sandervl.neighbournet.service.matchers;

import be.sandervl.neighbournet.domain.Attribute;

/**
 * @author: sander
 * @date: 12/12/2016
 */
public interface AttributeMatcher {
    Boolean match(Attribute a, Attribute b);
}
