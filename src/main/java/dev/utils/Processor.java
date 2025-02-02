package dev.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.*;
import java.util.regex.Pattern;

public class Processor {
    private final Context context;
    private final Logger logger;

    private static final Pattern INTEGER_PATTERN = Pattern.compile("^-?\\d+$");
    private static final Pattern FLOAT_PATTERN = Pattern.compile("^-?\\d+([.,]\\d+)?([eE][-+]?\\d+)?$");

    private final List<Long> integers = new ArrayList<>();
    private final List<Double> floats = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();
    private final List<List<?>> resultLists = new ArrayList<>(List.of(integers, floats, strings));

    public Processor(Context context) {
        this(context, Logger.getLogger(Processor.class.getName()));
    }

    public Processor(Context context, Logger logger) {
        this.logger = logger;
        this.context = context;
        setupLogger();
    }

    private void setupLogger() {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.SEVERE);
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.SEVERE);
    }

    public void run() throws IOException {
        logger.info("Запуск обработки файлов...");
        readInputFiles();
        writeOutputFiles();
        if (!context.getInputFiles().isEmpty() && !allResultFilesIsEmpty()) printStatistic();
    }

    public void readInputFiles() {
        for (String fileName : context.getInputFiles()) {

            try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
                logger.info("Чтение файла: " + fileName);
                String line;
                while ((line = reader.readLine()) != null)
                    classifyData(line.trim());
            } catch (IOException e) {
                if (!(new File(fileName).exists()))
                    System.err.println("Файл " + fileName + " не существует");
                logger.warning("Ошибка при чтении файла " + fileName + ": " + e.getMessage());
            }
        }
    }

    public void classifyData(String line) {
        if (line.isEmpty()) return;
        if(line.matches(INTEGER_PATTERN.pattern())) integers.add(Long.parseLong(line));
        else if (line.matches(FLOAT_PATTERN.pattern())) floats.add(Double.parseDouble(line));
        else strings.add(line);
    }

    public void writeOutputFiles() throws IOException {
        writeToFile("integers.txt", integers);
        writeToFile("floats.txt", floats);
        writeToFile("strings.txt", strings);
    }

    public <T> void writeToFile(String fileName, List<T> data) throws IOException {
        if (data.isEmpty()) return;
        fileName = context.getOutputPath() + File.separator + context.getPrefix() + fileName;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, context.isAppendMode()))) {
            for (T item : data) {
                writer.write(item.toString());
                writer.newLine();
            }
            logger.info("Данные записаны в: " + fileName);
        } catch (IOException e) {
            logger.warning("Ошибка записи в файл " + fileName + ": " + e.getMessage());
            throw e;
        }
    }

    public void printStatistic() {
        if (!context.isFullStatistic()) System.out.println("\nКраткая статистика:");
        else System.out.println(("\nПолная статистика:"));
        printNumberStats("integers.txt", integers);
        printNumberStats("floats.txt", floats);
        printStringStats();

    }

    private  <T extends Number & Comparable<T>> void printNumberStats(String fileName, List<T> numbers) {
        if (numbers.isEmpty()) return;
        int elementCount = numbers.size();
        T max = Collections.max(numbers);
        T min = Collections.min(numbers);
        double sum = numbers.stream().mapToDouble(Number::doubleValue).sum();
        double average = numbers.stream().mapToDouble(Number::doubleValue).average().orElse(0);

        if(!context.isFullStatistic()) System.out.printf("%s%s: %d%n", context.getPrefix(), fileName, elementCount);
        else {
            System.out.printf("\nФайл %s:%n", context.getPrefix() + fileName);
            System.out.printf("Количество элементов: %s%nМаксимум: %s%nМинимум: %s%nСумма: %.2f%nСреднее: %.2f%n",
                    elementCount, max, min, sum, average);
        }
    }

    private void printStringStats() {
        if (strings.isEmpty()) return;
        int elementCount = strings.size();
        int minLen = strings.stream().mapToInt(String::length).min().orElse(0);
        int maxLen = strings.stream().mapToInt(String::length).max().orElse(0);

        if (!context.isFullStatistic()) System.out.printf("%s%s: %d%n", context.getPrefix(), "strings.txt", elementCount);
        else {
            System.out.printf("\nФайл %s:%n", context.getPrefix() + "strings.txt");
            System.out.printf("Количество элементов: %s%nРазмер самой короткой строки: %d%nРазмер самой длинной строки: %d%n",
                    elementCount, minLen, maxLen);
        }
    }

    public boolean allResultFilesIsEmpty() {
        boolean res = true;
        for (List<?> list : resultLists) {
            if (!list.isEmpty()) {
                res = false;
                break;
            }
        }
        return res;
    }

    public Context getContext() {
        return context;
    }

    public List<Long> getIntegers() {
        return integers;
    }

    public List<Double> getFloats() {
        return floats;
    }

    public List<String> getStrings() {
        return strings;
    }
}
