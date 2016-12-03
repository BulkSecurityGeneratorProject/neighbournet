package be.sandervl.neighbournet.service.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ProcessorChain {

    private static final List<AbstractSelectorProcessor> processors = Arrays.asList();

    public String process(String value) {
        for (AbstractSelectorProcessor proc : processors) {
            value = proc.process(value);
        }
        return value;
    }

}
