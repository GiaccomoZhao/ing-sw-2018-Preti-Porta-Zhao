package porprezha.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import porprezha.model.cards.Box;
import porprezha.model.cards.Pattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class PatternTest {
    @Mock
    List<Pattern> patternList;

    @Test
    public void test() {
        Box mock = Mockito.mock(Box.class);
        assertTrue(24==patternList.size());
//        Mockito.when(mock.getNumber()).thenReturn(0);
//        Mockito.verify(mock).getColor();    // check mock has called getColor

        for (Pattern pattern: patternList
             ) {
            pattern.print();

        }


    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);     // needed when we use @Mock annotation
        patternList = new ArrayList<>();
        for (Pattern.TypePattern typePattern :
                Pattern.TypePattern.values()) {
            patternList.add(new Pattern(typePattern));
        }
    }

    @After
    public void tearDown() throws Exception {
        patternList.clear();    // tanto per scrivere qualcosa...
        patternList = null;
    }

}
