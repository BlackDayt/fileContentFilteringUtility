# Утилита фильтрации содержимого файлов

## Описание проекта

Данная утилита предназначена для фильтрации содержимого текстовых файлов, содержащих целые числа, вещественные числа и строки. При запуске утилиты данные из входных файлов распределяются по типам и записываются в отдельные выходные файлы. Дополнительно утилита собирает статистику по каждому типу данных и выводит её на консоль.

## Возможности

- **Фильтрация данных:**  
  - Целые числа записываются в файл `integers.txt`
  - Вещественные числа записываются в файл `floats.txt`
  - Строки записываются в файл `strings.txt`
  
  Файлы создаются только, если соответствующий тип данных присутствует во входных файлах.

- **Настройки вывода:**  
  - Опция `-o <путь>`: задаёт папку для выходных файлов. По умолчанию используется текущая директория.
  - Опция `-p <префикс>`: задаёт префикс для имён выходных файлов.
  - Опция `-a`: режим добавления в существующие файлы (без данной опции файлы перезаписываются).

- **Статистика:**  
  - Опция `-s`: вывод краткой статистики (по умолчанию) – только количество элементов для каждого типа.
  - Опция `-f`: вывод полной статистики – для чисел: количество, минимальное и максимальное значение, сумма и среднее; для строк: количество, длина самой короткой и самой длинной строки.

- **Справка:**  
  - Опция `-h`: вывод справочной информации по использованию утилиты.

## Требования

- **Java:** версия 21
- **Система сборки:** Maven (рекомендуется Maven 3.8.6 или выше)

## Зависимости

Проект использует следующие зависимости. Ниже приведены XML-блоки, которые включены в файл `pom.xml`:

- **JUnit 5** (версия 5.11.3)  
  [Официальный сайт](https://junit.org/junit5/)
  ```xml
  <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <version>5.11.3</version>
      <scope>test</scope>
  </dependency>
  <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <version>5.11.3</version>
      <scope>test</scope>
  </dependency>

- **Mockito** (версия 5.14.2)  
  [Официальный сайт](https://site.mockito.org/)
  ```xml
  <dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.14.2</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <version>5.14.2</version>
    <scope>test</scope>
  </dependency>

- **Byte Buddy Agent** (версия 1.15.10)  
  [Официальный сайт](https://bytebuddy.net/)
  ```xml
  <dependency>
    <groupId>net.bytebuddy</groupId>
    <artifactId>byte-buddy-agent</artifactId>
    <version>1.15.10</version>
    <scope>test</scope>
  </dependency>

- **System Lambda** (версия 1.2.1)  
  [репозиторий на GitHub](https://github.com/stefanbirkner/system-lambda)
  ```xml
  <dependency>
    <groupId>com.github.stefanbirkner</groupId>
    <artifactId>system-lambda</artifactId>
    <version>1.2.1</version>
    <scope>test</scope>
  </dependency>

- **Maven Shade Plugin (для создания исполняемого JAR)** 
  ```xml
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.6.0</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <transformers>
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>dev.utils.Utility</mainClass>
                    </transformer>
                </transformers>
            </configuration>
        </execution>
    </executions>
  </plugin>

- **Maven Surefire Plugin (для запуска тестов)** 
  ```xml
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0-M5</version>
    <configuration>
        <argLine>-javaagent:${settings.localRepository}/net/bytebuddy/byte-buddy-agent/1.15.10/byte-buddy-agent-1.15.10.jar</argLine>
    </configuration>
  </plugin>

## Сборка проекта

1. Клонируйте репозиторий или скачайте исходный код проекта.
2. Сборка с помощью Maven:

   Откройте терминал в корневой директории проекта и выполните команду:
   ```bash
    mvn clean package
   ```
   После успешной сборки в папке target появится исполняемый JAR-файл (например, util.jar).

## Запуск утилиты

Для запуска утилиты выполните одну из следующих команд:
- **Вывод справки по использованию:**
  ```bash
    java -jar target/util.jar -h
   ```
- **Простой запуск (с краткой статистикой по умолчанию):**
  ```bash
    java -jar target/util.jar in1.txt in2.txt
   ```
- **Запуск с указанием каталога для выходных файлов, префикса для имён файлов, полной статистикой и режимом добавления:**
  ```bash
    java -jar target/util.jar -o /some/output/path -p result_ -f -a in1.txt in2.txt
   ```
  В этом случае будут созданы или дополнены файлы:
    - `/some/output/path/result_integers.txt`
    - `/some/output/path/result_floats.txt`
    - `/some/output/path/result_strings.txt`

## Особенности реализации

- **Фильтрация данных:**
  Данные разделяются по типам с использованием регулярных выражений для определения целых и вещественных чисел (поддерживаются разделители `.` и `,`).
  
- **Статистика:**
  Поддерживаются два режима вывода статистики: краткий (только количество элементов) и полный (с дополнительными сведениями: минимум, максимум, сумма и среднее для чисел; длина самой короткой и самой длинной строки для строк).
  
- **Обработка ошибок:**
  Все возможные ошибки (например, отсутствие входного файла, ошибки при записи) обрабатываются и выводятся пользователю с подробными сообщениями. Программа продолжает работу при частичных ошибках.
  
- **Логирование:**
  Используется `java.util.logging.Logger` для логирования критических ошибок с уровнем SEVERE.

## Пример использования утилиты

**Входной файл `in1.txt`:**
```txt
  Lorem ipsum dolor sit amet
  45
  Пример
  3.1415
  consectetur adipiscing
  -0.001
  тестовое задание
  100500
```

**Входного файл `in2.txt`:**
```txt
  Нормальная форма числа с плавающей запятой
  1.528535047E-25
  Long
  1234567890123456789
```

**Команда:**
```bash
  java -jar target/util.jar in1.txt in2.txt
```

**Выходной файл `integers.txt`:**
```txt
  45
  100500
  1234567890123456789
```

**Выходной файл `floats.txt`:**
```txt
  3.1415
  -0.001
  1.528535047E-25
```

**Выходной файл `strings.txt`:**
```txt
  Lorem ipsum dolor sit amet
  Пример
  consectetur adipiscing
  тестовое задание
  Нормальная форма числа с плавающей запятой
  Long
```

## Контакты

