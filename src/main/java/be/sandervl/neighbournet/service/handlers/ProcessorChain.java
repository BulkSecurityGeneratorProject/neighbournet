package be.sandervl.neighbournet.service.handlers;

import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.domain.Selector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class ProcessorChain {

    private static List<SelectorProcessor> processors = Collections.emptyList();

    @Autowired
    private GoogleAddressProcessor googleAddressProcessor;

    @PostConstruct
    public void setup() {
        processors = Arrays.asList(
            googleAddressProcessor
        );
    }

    public String process(String value, Selector selector, Document document) {
        String[] valueToProcess = {value};
        processors.stream()
                  .filter(proc -> selector.getProcessors().stream().anyMatch(selectorProcessor -> selectorProcessor.getProcessorClass().isAssignableFrom(proc.getClass())))
                  .forEach(proc -> valueToProcess[0] = proc.process(valueToProcess[0], document));
        return valueToProcess[0];
    }

}
