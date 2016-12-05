package be.sandervl.neighbournet.service.handlers;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author: sander
 * @date: 4/12/2016
 */
public class GoogleAddressProcessorTest {

    private GoogleAddressProcessor googleAddressProcessor = new GoogleAddressProcessor();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void processTriple() throws Exception {
        assertEquals("restaurant, Amarante", googleAddressProcessor.process("Hulshout, restaurant, Amarante", null));
    }


    @Test
    public void processDouble() throws Exception {
        assertEquals("restaurant,...", googleAddressProcessor.process("Zottegem, restaurant,...", null));
    }

}
