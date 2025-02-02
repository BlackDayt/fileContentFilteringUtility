package utilsTests;

import dev.utils.Context;
import dev.utils.Processor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProcessorTest {

    @InjectMocks
    private Processor processor;
    private Context context;

    @BeforeEach
    void setUp() {
        context = new Context();
        context.addInputFile("file1.txt");
        context.setOutputPath("output");
        context.setPrefix("prefix_");
        processor = new Processor(context);
    }

    @Test
    void testClassifyData() {
        processor.classifyData("123");
        processor.classifyData("123.45");
        processor.classifyData("text");

        assertEquals(1, processor.getIntegers().size());
        assertEquals(1, processor.getFloats().size());
        assertEquals(1, processor.getStrings().size());
    }

    @Test
    void testReadInputFiles() throws IOException {
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            BufferedReader mockedReader = mock(BufferedReader.class);
            when(mockedReader.readLine())
                    .thenReturn("123")
                    .thenReturn("123.45")
                    .thenReturn("text")
                    .thenReturn(null);
            mockedFiles.when(() -> Files.newBufferedReader(Paths.get("file1.txt")))
                    .thenReturn(mockedReader);

            processor.readInputFiles();

            assertEquals(1, processor.getIntegers().size());
            assertEquals(1, processor.getFloats().size());
            assertEquals(1, processor.getStrings().size());
        }
    }

    @Test
    public void testWriteOutputFiles() throws IOException {
        List<Long> integers = Arrays.asList(1L, 2L, 3L);
        List<Double> floats = Arrays.asList(1.1, 2.2, 3.3);
        List<String> strings = Arrays.asList("one", "two", "three");

        Context mockedContext = mock(Context.class);
        when(mockedContext.getInputFiles()).thenReturn(Arrays.asList("file1.txt", "file2.txt"));
        when(mockedContext.getOutputPath()).thenReturn("output");
        when(mockedContext.getPrefix()).thenReturn("prefix_");
        when(mockedContext.isAppendMode()).thenReturn(false);
        when(mockedContext.isFullStatistic()).thenReturn(true);

        Processor processor = new Processor(mockedContext);

        processor.getIntegers().addAll(integers);
        processor.getFloats().addAll(floats);
        processor.getStrings().addAll(strings);

        try (MockedConstruction<BufferedWriter> mockedBufferedWriter = mockConstruction(BufferedWriter.class)) {
            for (BufferedWriter writer : mockedBufferedWriter.constructed()) {
                doNothing().when(writer).write(anyString());
                doNothing().when(writer).newLine();
            }

            processor.writeOutputFiles();

            verify(mockedBufferedWriter.constructed().get(0), times(integers.size())).write(anyString());
            verify(mockedBufferedWriter.constructed().get(1), times(floats.size())).write(anyString());
            verify(mockedBufferedWriter.constructed().get(2), times(strings.size())).write(anyString());

            verify(mockedBufferedWriter.constructed().get(0), times(integers.size())).newLine();
            verify(mockedBufferedWriter.constructed().get(1), times(floats.size())).newLine();
            verify(mockedBufferedWriter.constructed().get(2), times(strings.size())).newLine();
        }
    }

    @Test
    void testWriteToFileIOException() {
        List<String> data = Arrays.asList("item1", "item2", "item3");

        try (MockedConstruction<BufferedWriter> mockedBufferedWriter = mockConstruction(BufferedWriter.class,
                (mock, context) -> doThrow(new IOException("Ошибка записи")).when(mock).write(anyString()))) {
            try (MockedConstruction<FileWriter> mockedFileWriter = mockConstruction(FileWriter.class)) {
                assertThrows(IOException.class, () -> processor.writeToFile("test.txt", data));
            }
        }
    }
}