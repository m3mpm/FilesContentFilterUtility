package org.m3mpm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DataWriter {
    private final String integersFile;
    private final String floatsFile;
    private final String stringsFile;
    private final boolean writeMode;

    private BufferedWriter intWriter = null;
    private BufferedWriter floatWriter = null;
    private BufferedWriter stringWriter = null;

    public DataWriter(String integersFile, String floatsFile, String stringsFile, boolean writeMode) {
        this.integersFile = integersFile;
        this.floatsFile = floatsFile;
        this.stringsFile = stringsFile;
        this.writeMode = writeMode;
    }

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
