package org.m3mpm;

/**
 * Главный класс приложения, точка входа.
 */
public class Main {
    /**
     * Главный метод приложения.
     *
     * @param args Аргументы командной строки, передаваемые при запуске приложения.  Ожидается, что аргументы
     *             будут содержать параметры для обработки файлов (например, имена файлов, флаги статистики).
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            FileProcessor fileProcessor = new FileProcessor(args);
            try{
                fileProcessor.run();
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
        } else {
            System.err.println("ERROR: Отсутствуют аргументы командной строки!");
        }
    }
}