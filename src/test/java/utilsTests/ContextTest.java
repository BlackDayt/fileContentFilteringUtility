package utilsTests;

import dev.utils.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ContextTest {

    private Context context;

    @BeforeEach
    void setUp() {
        context = new Context();
    }

    @Test
    void testAddInputFile() {
        context.addInputFile("file1.txt");
        context.addInputFile("file2.txt");
        assertEquals(2, context.getInputFiles().size());
        assertTrue(context.getInputFiles().contains("file1.txt"));
        assertTrue(context.getInputFiles().contains("file2.txt"));
    }

    @Test
    void testSetPrefix() {
        context.setPrefix("prefix_");
        assertEquals("prefix_", context.getPrefix());
    }

    @Test
    void testSetOutputPath() {
        context.setOutputPath("/new/path");
        assertEquals("/new/path", context.getOutputPath());
    }

    @Test
    void testSetFullStatistic() {
        context.setFullStatistic(true);
        assertTrue(context.isFullStatistic());
        context.setFullStatistic(false);
        assertFalse(context.isFullStatistic());
    }

    @Test
    void testSetAppendMode() {
        context.setAppendMode(true);
        assertTrue(context.isAppendMode());
        context.setAppendMode(false);
        assertFalse(context.isAppendMode());
    }
}