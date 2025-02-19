package org.m3mpm;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        if (args.length > 0) {

            // Параметры по умолчанию
            String outputPath = ".";
            String prefix = "";
            boolean writeMode = false;
            boolean shortStatistics = false;
            boolean fullStatistics = false;

            // Массив входных файлов
            List<String> inputFiles = new ArrayList<>();

            // Парсинг аргументов командной строки
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

            if(!inputFiles.isEmpty()) {
                // Создание пути и имен выходных файлов
                String integersFile = Paths.get(outputPath, prefix + "integers.txt").normalize().toString();
                String floatsFile = Paths.get(outputPath, prefix + "floats.txt").normalize().toString();
                String stringsFile = Paths.get(outputPath, prefix + "strings.txt").normalize().toString();

                // Создание объектов для статистики
                IntegerStatistics integerStatistics = new IntegerStatistics(shortStatistics, fullStatistics);
                FloatStatistics floatStatistics = new FloatStatistics(shortStatistics, fullStatistics);
                StringStatistics stringStatistics = new StringStatistics(shortStatistics, fullStatistics);

                // Создание объекта DataWriter для записи в файлы
                DataWriter  dataWriter = new DataWriter(integersFile, floatsFile, stringsFile, writeMode);

                try {
                    // Чтение каждого входного файла
                    for (String inputFile : inputFiles) {
                        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {

                            // Чтение каждой строки в файле
                            String line;
                            while ((line = reader.readLine()) != null) {

                                // Пропускаем пустые строки
                                line = line.trim();
                                if (line.isEmpty()) continue;

                                // Определение типа данных и запись в файл
                                if(line.matches("[-+]?\\d*\\.\\d+[Ee][-+]?\\d+") || line.matches("[-+]?\\d+\\.\\d+")) { // Научная нотация и Вещественное число
                                    if(!dataWriter.writeToFile(line,"float")){
                                        floatStatistics.updateStatistics(line);
                                    }
                                } else if (line.matches("[-+]?\\d+")) { // Целое число
                                    if(!dataWriter.writeToFile(line,"int")){
                                        integerStatistics.updateStatistics(line);
                                    }
                                } else { // Строка
                                    if(!dataWriter.writeToFile(line,"string")){
                                        stringStatistics.updateStatistics(line);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            System.err.println("ERROR: Ошибка при чтении файла: " + inputFile);
                        }
                    }

                    // Вывод статистики
                    integerStatistics.printStatistics();
                    floatStatistics.printStatistics();
                    stringStatistics.printStatistics();

                }  finally {
                    // Закрытие BufferedWriter
                    dataWriter.closeWriters();
                }

            } else {
                System.err.println("ERROR: Входные файлы не указаны!");
            }

        } else {
            System.err.println("ERROR: Отсутствуют аргументы командной строки!!!");
            System.out.println("INFO: java -jar util.jar [options]");
        }

    }
}