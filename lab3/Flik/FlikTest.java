import static org.junit.Assert.*;

import org.junit.Test;

public class FlikTest {
    @Test
    public void testisSameNumber() {
        boolean correct=Flik.isSameNumber(129,129);
        boolean wrong=Flik.isSameNumber(2,3);
        assertTrue(correct);
        assertTrue(!wrong);
    }
}
