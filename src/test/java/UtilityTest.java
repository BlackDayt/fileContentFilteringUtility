import dev.avagimov.utils.Processor;
import dev.avagimov.utils.Utility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UtilityTest {
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMainWithNoArgs() {
        String[] args = {};
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
        Utility.main(args);
        System.setErr(originalErr);
        assertTrue(errContent.toString().contains("Ошибка: Не переданы аргументы"));
    }

    @Test
    void testMainWithValidArgs() {
        String[] args = {"-o", "output", "-p", "prefix_", "-f", "-a", "file1.txt", "file2.txt"};

        try (MockedConstruction<Processor> mockedConstruction = Mockito.mockConstruction(Processor.class,
                (mock, context) -> doNothing().when(mock).run())) {
            Utility.main(args);
            assertEquals(1, mockedConstruction.constructed().size());
            verify(mockedConstruction.constructed().get(0), times(1)).run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}