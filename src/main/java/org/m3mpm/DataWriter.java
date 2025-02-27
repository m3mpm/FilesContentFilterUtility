package org.m3mpm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
 *  `DataWriter` - класс, предназначенный для записи данных различных типов (целые числа, числа с плавающей точкой и строки) в отдельные файлы.
 *  Класс обеспечивает возможность записи в режиме перезаписи или добавления к существующим файлам.
 */
public class DataWriter {
    private final String integersFile;
    private final String floatsFile;
    private final String stringsFile;
    private final boolean writeMode;

    private BufferedWriter intWriter = null;
    private BufferedWriter floatWriter = null;
    private BufferedWriter stringWriter = null;

    /**
     *  Конструктор класса `DataWriter`.
     *
     *  @param integersFile  Путь к файлу для записи целых чисел.
     *  @param floatsFile    Путь к файлу для записи чисел с плавающей точкой.
     *  @param stringsFile   Путь к файлу для записи строк.
     *  @param writeMode     Режим записи: `true` - добавление, `false` - перезапись.
     */
    public DataWriter(String integersFile, String floatsFile, String stringsFile, boolean writeMode) {
        this.integersFile = integersFile;
        this.floatsFile = floatsFile;
        this.stringsFile = stringsFile;
        this.writeMode = writeMode;
    }

    /**
     *  Записывает строку в соответствующий файл, основываясь на указанном типе данных.
     *
     *  @param line  Строка для записи в файл.
     *  @param type  Тип данных, определяющий, в какой файл будет произведена запись ("int", "float", "string").
     *  @throws DataWriterException  Если произошла ошибка при записи в файл или некорректный тип данных
     */
    public void writeToFile(String line, String type) throws DataWriterException {
        try {
            switch (type) {
                case "int" -> {
                    if (intWriter == null) {
                        intWriter = new BufferedWriter(new FileWriter(integersFile, writeMode));
                    }
                    intWriter.write(line);
                    intWriter.newLine();
                }
                case "float" -> {
                    if (floatWriter == null) {
                        floatWriter = new BufferedWriter(new FileWriter(floatsFile, writeMode));
                    }
                    floatWriter.write(line);
                    floatWriter.newLine();
                }
                case "string" -> {
                    if (stringWriter == null) {
                        stringWriter = new BufferedWriter(new FileWriter(stringsFile, writeMode));
                    }
                    stringWriter.write(line);
                    stringWriter.newLine();
                }
                default -> throw new DataWriterException("ERROR: Неизвестный тип данных: " + type);
            }
        } catch (IOException e) {
            throw new DataWriterException("ERROR: Ошибка при записи в файл: " + type);
        }
    }

    /**
     *  Закрывает все открытые BufferedWriter.
     *
     *  @throws DataWriterException  Если произошла ошибка при закрытии файлов.
     */
    public void closeWriters() throws DataWriterException {
        try {
            if (intWriter != null) intWriter.close();
            if (floatWriter != null) floatWriter.close();
            if (stringWriter != null) stringWriter.close();
        } catch (IOException e) {
            throw new DataWriterException("ERROR: Ошибка при закрытии файлов");
        }
    }
}
