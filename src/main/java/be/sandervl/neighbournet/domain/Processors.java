package be.sandervl.neighbournet.domain;

import be.sandervl.neighbournet.service.handlers.GoogleAddressProcessor;

/**
 * @author: sander
 * @date: 5/12/2016
 */
public enum Processors {
    GOOGLE_ADDRESS(GoogleAddressProcessor.class);

    Processors(Class processorClass) {
        this.processorClass = processorClass;
    }

    private Class processorClass;

    public Class getProcessorClass() {
        return processorClass;
    }
}
