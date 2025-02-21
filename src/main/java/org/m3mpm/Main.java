package org.m3mpm;


public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            FileProcessor fileProcessor = new FileProcessor(args);
            try{
                fileProcessor.run();
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("ERROR: Отсутствуют аргументы командной строки!");
        }
    }
}