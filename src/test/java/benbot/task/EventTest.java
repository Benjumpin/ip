package benbot.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest {
    @Test
    public void testEventCreation() {
        Event e = new Event("project meeting", "2026-12-12 18:00", "2026-12-12 20:00");
        String expected = "[E][ ] project meeting (from: Dec 12 2026, 6:00 pm to: Dec 12 2026, 8:00 pm)";
        assertEquals(expected, e.toString());
    }

    @Test
    public void testMarkAsDone() {
        Event e = new Event("concert", "2026-05-20 19:00", "2026-05-20 22:00");
        e.markDone();
        assertEquals("[E][X] concert (from: May 20 2026, 7:00 pm to: May 20 2026, 10:00 pm)", e.toString());
    }
}