package be.sandervl.neighbournet.service.handlers;

/**
 * @author: sander
 * @date: 28/11/2016
 */
public interface AbstractSelectorProcessor {

    String process(String value);

    AbstractSelectorProcessor getSuccessor();
}
