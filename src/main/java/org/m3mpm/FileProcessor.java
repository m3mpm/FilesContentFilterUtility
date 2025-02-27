package org.m3mpm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {

    // Аргументы командой строки
    private String[] args;

    // Параметры для обработки файлов
    private String outputPath = ".";
    private String prefix = "";
    private boolean writeMode = false;
    private boolean shortStatistics = false;
    private boolean fullStatistics = false;
    private List<String> inputFiles = new ArrayList<>();

    // Объекты для сбора статистики
    IntegerStatistics integerStatistics = null;
    FloatStatistics floatStatistics = null;
    StringStatistics stringStatistics = null;
    boolean hasErrors = false;

    // Объект для записи данных в файлы
    DataWriter  dataWriter = null;

    // Конструктор
    public FileProcessor(String[] args) {
        this.args = args;
    }

    public void run() throws Exception {
        processArgs();

        if(!inputFiles.isEmpty()){
            if(Files.isDirectory(Paths.get(outputPath))) {
                // Создание пути и имен выходных файлов
                String integersFile = Paths.get(outputPath, prefix + "integers.txt").normalize().toString();
                String floatsFile = Paths.get(outputPath, prefix + "floats.txt").normalize().toString();
                String stringsFile = Paths.get(outputPath, prefix + "strings.txt").normalize().toString();

                // Создание объектов для статистики
                integerStatistics = new IntegerStatistics(shortStatistics, fullStatistics);
                floatStatistics = new FloatStatistics(shortStatistics, fullStatistics);
                stringStatistics = new StringStatistics(shortStatistics, fullStatistics);

                // Создание объекта DataWriter для записи в файлы
                dataWriter = new DataWriter(integersFile, floatsFile, stringsFile, writeMode);

                processFiles();

                // Вывод статистики
                if (!hasErrors) {
                    integerStatistics.printStatistics();
                    floatStatistics.printStatistics();
                    stringStatistics.printStatistics();
                }

                try {
                    dataWriter.closeWriters();
                } catch (DataWriterException e) {
                    System.err.println(e.getMessage());
                }
            } else {
                System.err.println("ERROR: Неверно указан путь: " + outputPath);
            }
        } else {
            System.err.println("ERROR: Входные файлы не указаны!");
        }
    }

    private void processArgs(){
        for (int i = 0; i < args.length; i++) {
            if ("-o".equals(args[i]) && i + 1 < args.length) {
                outputPath = args[++i];
            } else if ("-p".equals(args[i]) && i + 1 < args.length) {
                prefix = args[++i];
            } else if ("-a".equals(args[i])) {
                writeMode = true;
            } else if("-s".equals(args[i])) {
                shortStatistics = true;
            } else if ("-f".equals(args[i])) {
                fullStatistics = true;
            } else {
                inputFiles.add(args[i]);
            }
        }
    }

    private void processFiles() {
        // Чтение каждого входного файла
        for (String inputFile : inputFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {

                // Чтение каждой строки в файле
                String line;
                while ((line = reader.readLine()) != null) {

                    // Пропускаем пустые строки
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    processLine(line);

                }
            } catch (IOException e) {
                hasErrors = true;
                System.err.println("ERROR: Ошибка при чтении файла: " + inputFile);
            }
        }
    }

    private void processLine(String line) {
        try {
            // Определение типа данных и запись в файл

            // Экспоненциальная запись числа и Вещественное число
            if (line.matches("[-+]?\\d+(\\.\\d+)?[Ee][-+]?\\d+") || line.matches("[-+]?\\d+\\.\\d+")) {
                dataWriter.writeToFile(line, "float");
                floatStatistics.updateStatistics(line);
            } else if (line.matches("[-+]?\\d+")) { // Целое число
                dataWriter.writeToFile(line, "int");
                integerStatistics.updateStatistics(line);
            } else { // Строка
                dataWriter.writeToFile(line, "string");
                stringStatistics.updateStatistics(line);
            }
        } catch (DataWriterException e) {
            hasErrors = true;
            System.err.println(e.getMessage());
        }
    }

}
