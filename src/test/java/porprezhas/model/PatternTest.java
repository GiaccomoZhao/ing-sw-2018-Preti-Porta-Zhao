package porprezhas.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import porprezhas.model.cards.Box;
import porprezhas.model.cards.Pattern;

import java.util.ArrayList;
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

        for (Pattern.TypePattern typePattern :
                Pattern.TypePattern.values()) {
            Pattern pattern = patternList.get(typePattern.ordinal());
            System.out.println("\nPattern name: " + pattern.getNamePattern());
            System.out.print("╭─");
            for (int i = 0; i < pattern.getWidth(); i++) {
                System.out.print("───");
            }
            System.out.print("─╮");
            for (int y = 0; y < pattern.getHeight(); y++) {
                System.out.println("| ");
                for (int x = 0; x < pattern.getWidth(); x++) {
                    Box box = pattern.getBox(x, y);
                    System.out.format("%C%d ", box.getColor().name().charAt(0), box.getNumber());
                }
                System.out.println(" |");
            }
            System.out.print("╰━");
            for (int i = 0; i < pattern.getWidth(); i++) {
                System.out.print("━━━");
            }
            System.out.print("━╯");
 //           System.out.format("%C%d\t", pattern.getBox(y,x));
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
