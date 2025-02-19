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

    public boolean writeToFile(String line, String type) {
        boolean hasError = false;
        try {
            switch (type) {
                case "int":
                    if (intWriter == null) {
                        intWriter = new BufferedWriter(new FileWriter(integersFile, writeMode));
                    }
                    intWriter.write(line);
                    intWriter.newLine();
                    break;
                case "float":
                    if (floatWriter == null) {
                        floatWriter = new BufferedWriter(new FileWriter(floatsFile, writeMode));
                    }
                    floatWriter.write(line);
                    floatWriter.newLine();
                    break;
                case "string":
                    if (stringWriter == null) {
                        stringWriter = new BufferedWriter(new FileWriter(stringsFile, writeMode));
                    }
                    stringWriter.write(line);
                    stringWriter.newLine();
                    break;
            }
            return hasError;
        } catch (IOException e) {
            System.err.println("ERROR: Ошибка при записи в файл: " + type);
            return hasError = true;
        }
    }

    public void closeWriters() {
        try {
            if (intWriter != null) intWriter.close();
            if (floatWriter != null) floatWriter.close();
            if (stringWriter != null) stringWriter.close();
        } catch (IOException e) {
            System.err.println("ERROR: Ошибка при закрытии файлов");
        }
    }
}
