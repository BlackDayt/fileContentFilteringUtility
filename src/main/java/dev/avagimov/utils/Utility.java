package dev.avagimov.utils;

import java.io.IOException;
import java.util.*;

public class Utility {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Ошибка: Не переданы аргументы. Используйте -h для помощи.");
            return;
        }

        try {
            Context context = parseArguments(args);
            Processor processor = new Processor(context);
            processor.run();
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка: Неизвестный аргументы. Используйте -h для помощи.");
        } catch (IOException e) {
            System.err.println("Ошибка: Произошла ошибка при записи в файл");
        } catch (NullPointerException e) {
            System.err.println("Ошибка: Проверьте вводные данные");
        }
    }

    public static Context parseArguments(String[] args) {
        Context context = new Context();
        List<String> argList = new ArrayList<>(Arrays.asList(args));

        for (int i =0; i < argList.size(); i++){
            String arg = args[i].trim();
            try {
                switch (arg) {
                    case "-o" -> {
                        if (i + 1 < args.length) {
                            String path = args[++i];
                            if (path.matches("^[a-zA-Z]:[\\\\/].*|^/.*|^\\\\.([\\\\/].*)?|^\\\\.\\\\.[\\\\/].*")) {
                                context.setOutputPath(path);
                            } else {
                                System.err.println("Ошибка: Некорректный путь для выходных файлов.");
                            }
                        } else {
                            System.err.println("Ошибка: Не указан путь для выходных файлов.");
                        }
                    }
                    case "-p" -> {
                        if (i + 1 < args.length) {
                            String prefix = args[++i];
                            if (!prefix.startsWith("-")) {
                                context.setPrefix(prefix);
                            } else {
                                System.err.println("Ошибка: Некорректно указан префикс выходных файлов.");
                            }
                        } else {
                            System.err.println("Ошибка: Не указан префикс выходных файлов.");
                        }
                    }
                    case "-s" -> context.setFullStatistic(false);
                    case "-f" -> context.setFullStatistic(true);
                    case "-a" -> context.setAppendMode(true);
                    case "-h" -> {
                        printHelp();
                        context.setPrintInfo(true);
                    }
                    default -> {
                        if (arg.endsWith(".txt")) {
                            context.addInputFile(arg);
                        } else {
                            System.err.println("Предупреждение: Неизвестный аргумент " + arg);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Ошибка при обработке аргумента " + arg + ": " + e.getMessage());
            }
        }

        if (context.getInputFiles().isEmpty() && !context.isPrintInfo()) System.err.println("Не указаны входные файлы.");
        return context;
    }


    private static void printHelp() {
        System.out.println("""
    Использование: java Utility [опции] <файлы>
    
    Опции:
      -o <путь>      Указать папку для выходных файлов.
      -p <префикс>   Добавить префикс к именам выходных файлов (префикс не должен начинаться с "-").
      -s             Вывести краткую статистику (по умолчанию).
      -f             Вывести полную статистику (заменяет -s).
      -a             Добавить данные в существующие файлы (иначе файлы перезаписываются).
      -h             Показать эту справку.
    
    Входные файлы:
      Должны быть в формате .txt.
      
    Пример:
      java Utility -o /output -p result_ -f input1.txt input2.txt
    """);
    }
}
//"C:\Users\\proto\Desktop"