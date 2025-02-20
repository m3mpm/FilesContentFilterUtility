package org.m3mpm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {
    // Параметры по умолчанию
    private String outputPath = ".";
    private String prefix = "";
    private boolean writeMode = false;
    private boolean shortStatistics = false;
    private boolean fullStatistics = false;
    private List<String> inputFiles = new ArrayList<>();

    private String[] args;

    public FileProcessor(String[] args) {
        this.args = args;
    }

    public void processing() throws Exception {
        processArgs();
        if(!inputFiles.isEmpty()){
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

            boolean hasErrors = false;

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
                            if (line.matches("[-+]?\\d*\\.\\d+[Ee][-+]?\\d+") || line.matches("[-+]?\\d+\\.\\d+")) { // Научная нотация и Вещественное число
                                dataWriter.writeToFile(line, "float");
                                floatStatistics.updateStatistics(line);
                            } else if (line.matches("[-+]?\\d+")) { // Целое число
                                dataWriter.writeToFile(line, "int");
                                integerStatistics.updateStatistics(line);
                            } else { // Строка
                                dataWriter.writeToFile(line, "string");
                                stringStatistics.updateStatistics(line);
                            }
                        }
                    } catch (DataWriterException e){
                        System.err.println(e.getMessage());
                        hasErrors = true;
                    } catch (IOException e) {
                        System.err.println("ERROR: Ошибка при чтении файла: " + inputFile);
                        hasErrors = true;
                    }
                }

                // Вывод статистики
                if (!hasErrors) {
                    integerStatistics.printStatistics();
                    floatStatistics.printStatistics();
                    stringStatistics.printStatistics();
                }

            }  finally {
                // Закрытие BufferedWriter
                try{
                    dataWriter.closeWriters();
                }
                catch (DataWriterException e){
                    System.err.println(e.getMessage());
                }
            }

        } else {
//            System.err.println("ERROR: Входные файлы не указаны!");
            throw new FileProcessorException("ERROR: Входные файлы не указаны!");
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

}
