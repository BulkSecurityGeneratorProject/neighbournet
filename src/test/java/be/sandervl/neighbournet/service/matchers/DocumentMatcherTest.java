package be.sandervl.neighbournet.service.matchers;

import be.sandervl.neighbournet.domain.Attribute;
import be.sandervl.neighbournet.domain.AttributeMatcher;
import be.sandervl.neighbournet.domain.Document;
import be.sandervl.neighbournet.domain.Selector;
import be.sandervl.neighbournet.service.AttributeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author: sander
 * @date: 12/12/2016
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentMatcherTest {

    @Mock
    private AttributeService attributeService;

    @Mock
    private ApplicationContext applicationContext;

    private StringAttributeMatcher stringAttributeMatcher = new StringAttributeMatcher();

    @InjectMocks
    private DocumentMatcher documentMatcher = new DocumentMatcherImpl();

    private AttributeMatcher attributeMatcher;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        attributeMatcher = AttributeMatcher.STRING;
        when(applicationContext.getBean(any(Class.class))).thenReturn(stringAttributeMatcher);

    }

    @Test
    public void match() throws Exception {
        Selector primarySelector = Selector.builder()
                                           .isPrimary(true)
                                           .id(123L)
                                           .matcher(attributeMatcher)
                                           .build();
        Document a = Document.builder()
                             .build();
        Document b = Document.builder()
                             .build();

        when(attributeService.findByDocument(a)).thenReturn(new HashSet<>(Arrays.asList(
            Attribute.builder()
                     .selector(primarySelector)
                     .value("ABC")
                     .id(1L)
                     .build()
        )));
        when(attributeService.findByDocument(b)).thenReturn(new HashSet<>(Arrays.asList(
            Attribute.builder()
                     .selector(primarySelector)
                     .value("ABC")
                     .id(2L)
                     .build()
        )));
        assertTrue("Two documents with attributes with same primary selector should match",
            documentMatcher.match(a, b));
    }

    @Test
    public void matchLevenhstein() throws Exception {
        Selector primarySelector = Selector.builder()
                                           .isPrimary(true)
                                           .id(123L)
                                           .matcher(attributeMatcher)
                                           .build();
        Document a = Document.builder()
                             .build();
        Document b = Document.builder()
                             .build();

        when(attributeService.findByDocument(a)).thenReturn(new HashSet<>(Arrays.asList(
            Attribute.builder()
                     .selector(primarySelector)
                     .value("ABC")
                     .id(1L)
                     .build()
        )));
        when(attributeService.findByDocument(b)).thenReturn(new HashSet<>(Arrays.asList(
            Attribute.builder()
                     .selector(primarySelector)
                     .value("ABCD")
                     .id(2L)
                     .build()
        )));
        assertTrue("Two documents with neirly identical attribute values with same primary selector should match",
            documentMatcher.match(a, b));
    }

    @Test
    public void valuesDoNotmatch() throws Exception {
        Selector primarySelector = Selector.builder()
                                           .isPrimary(true)
                                           .id(123L)
                                           .matcher(attributeMatcher)
                                           .build();
        Document a = Document.builder()
                             .build();
        Document b = Document.builder()
                             .build();

        when(attributeService.findByDocument(a)).thenReturn(new HashSet<>(Arrays.asList(
            Attribute.builder()
                     .selector(primarySelector)
                     .value("ABCD")
                     .id(1L)
                     .build()
        )));
        when(attributeService.findByDocument(b)).thenReturn(new HashSet<>(Arrays.asList(
            Attribute.builder()
                     .selector(primarySelector)
                     .value("abcd")
                     .id(2L)
                     .build()
        )));
        assertFalse("Two documents with different attributes with same primary selector should not match",
            documentMatcher.match(a, b));
    }

    @Test
    public void noPrimariesShouldNotMatch() throws Exception {
        Selector primarySelector = Selector.builder()
                                           .isPrimary(false)
                                           .id(123L)
                                           .matcher(attributeMatcher)
                                           .build();
        Document a = Document.builder()
                             .build();
        Document b = Document.builder()
                             .build();

        when(attributeService.findByDocument(a)).thenReturn(new HashSet<>(Arrays.asList(
            Attribute.builder()
                     .selector(primarySelector)
                     .value("ABC")
                     .id(1L)
                     .build()
        )));
        when(attributeService.findByDocument(b)).thenReturn(new HashSet<>(Arrays.asList(
            Attribute.builder()
                     .selector(primarySelector)
                     .value("ABC")
                     .id(2L)
                     .build()
        )));
        assertFalse("Documents without primary selector should not match", documentMatcher.match(a, b));
    }

    @Test
    public void findMatches() throws Exception {

    }

}
