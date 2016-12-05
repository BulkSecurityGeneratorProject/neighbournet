package be.sandervl.neighbournet.service.handlers;

import be.sandervl.neighbournet.domain.Document;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ProcessorChain {

    private static final List<SelectorProcessor> processors = Arrays.asList();

    public String process(String value, Document document) {
        for (SelectorProcessor proc : processors) {
            value = proc.process(value, document);
        }
        return value;
    }

}
