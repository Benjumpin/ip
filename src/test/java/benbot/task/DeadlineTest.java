package benbot.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeadlineTest {
    @Test
    public void testDeadlineCreation() {

        Deadline d = new Deadline("return book", "2026-12-12 18:00");
        assertEquals("[D][ ] return book (by: Dec 12 2026, 6:00 pm)", d.toString());
    }
}