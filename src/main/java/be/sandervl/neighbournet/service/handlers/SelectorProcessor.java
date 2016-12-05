package be.sandervl.neighbournet.service.handlers;

import be.sandervl.neighbournet.domain.Document;

/**
 * @author: sander
 * @date: 28/11/2016
 */
public interface SelectorProcessor {

    String process(String value, Document document);

}
