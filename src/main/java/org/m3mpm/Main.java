package org.m3mpm;


import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
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

            // Массив входных файлов
            List<String> inputFiles = new ArrayList<>();

            // Парсинг аргументов командной строки
            for (int i = 0; i < args.length; i++) {
                if ("-o".equals(args[i]) && i + 1 < args.length) {
                    outputPath = args[++i];
                } else if ("-p".equals(args[i]) && i + 1 < args.length) {
                    prefix = args[++i];
                } else {
                    inputFiles.add(args[i]);
                }
            }

            if(!inputFiles.isEmpty()) {
                // Создание пути и имен выходных файлов
                String integersFile = Paths.get(outputPath, prefix + "integers.txt").normalize().toString();
                String floatsFile = Paths.get(outputPath, prefix + "floats.txt").normalize().toString();
                String stringsFile = Paths.get(outputPath, prefix + "strings.txt").normalize().toString();

                // Создание BufferedWriter для записи
                BufferedWriter intWriter = null;
                BufferedWriter floatWriter = null;
                BufferedWriter stringWriter = null;

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
                                    if (floatWriter == null) {
                                        try {
                                            floatWriter = new BufferedWriter(new FileWriter(floatsFile));
                                            floatWriter.write(line);
                                            floatWriter.newLine();
                                        }  catch (IOException e) {
                                            System.err.println("ERROR: Ошибка при создании выходных файлов");
                                            e.printStackTrace();
                                        }
                                    } else {
                                        floatWriter.write(line);
                                        floatWriter.newLine();
                                    }
                                } else if (line.matches("[-+]?\\d+")) { // Целое число
                                    if (intWriter == null) {
                                        try {
                                            intWriter = new BufferedWriter(new FileWriter(integersFile));
                                            intWriter.write(line);
                                            intWriter.newLine();
                                        }  catch (IOException e) {
                                            System.err.println("ERROR: Ошибка при создании выходных файлов");
                                            e.printStackTrace();
                                        }
                                    } else{
                                        intWriter.write(line);
                                        intWriter.newLine();
                                    }
                                } else { // Строка
                                    if (stringWriter == null) {
                                        try {
                                            stringWriter = new BufferedWriter(new FileWriter(stringsFile));
                                            stringWriter.write(line);
                                            stringWriter.newLine();
                                        }  catch (IOException e) {
                                            System.err.println("ERROR: Ошибка при создании выходных файлов");
                                            e.printStackTrace();
                                        }
                                    } else {
                                        stringWriter.write(line);
                                        stringWriter.newLine();
                                    }
                                }
                            }
                        } catch (IOException e) {
                            System.err.println("ERROR: Ошибка при чтении файла: " + inputFile);
//                        e.printStackTrace();
                        }
                    }
                }  finally {
                    // Закрытие буферов, если они были созданы
                    try {
                        if (intWriter != null) intWriter.close();
                        if (floatWriter != null) floatWriter.close();
                        if (stringWriter != null) stringWriter.close();
                    } catch (IOException e) {
                        System.err.println("ERROR: Ошибка при закрытии файлов");
                        e.printStackTrace();
                    }
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