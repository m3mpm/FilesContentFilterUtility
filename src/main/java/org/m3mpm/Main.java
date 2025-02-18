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


            // Создание пути и имен выходных файлов
            String integersFile = Paths.get(outputPath, prefix + "integers.txt").normalize().toString();
            String floatsFile = Paths.get(outputPath, prefix + "floats.txt").normalize().toString();
            String stringsFile = Paths.get(outputPath, prefix + "strings.txt").normalize().toString();


            // Создание BufferedWriter для записи
            try(BufferedWriter intWriter = new BufferedWriter(new FileWriter(integersFile));
                BufferedWriter floatWriter = new BufferedWriter(new FileWriter(floatsFile));
                BufferedWriter stringWriter = new BufferedWriter(new FileWriter(stringsFile))) {

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
                                floatWriter.write(line);
                                floatWriter.newLine();
                            } else if (line.matches("[-+]?\\d+")) { // Целое число
                                intWriter.write(line);
                                intWriter.newLine();
                            } else { // Строка
                                stringWriter.write(line);
                                stringWriter.newLine();
                            }
                        }
                    } catch (IOException e) {
                        System.err.println("ERROR: Ошибка при чтении файла: " + inputFile);
//                        e.printStackTrace();
                    }
                }
            }  catch (IOException e) {
                System.err.println("ERROR: Ошибка при создании выходных файлов");
//                e.printStackTrace();
            }

        } else {
            System.err.println("ERROR: Отсутствуют аргументы командной строки!!!");
            System.out.println("INFO: java -jar util.jar [options]");
        }

    }
}