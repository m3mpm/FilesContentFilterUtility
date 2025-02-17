package org.m3mpm;


public class Main {
    public static void main(String[] args) {

        if (args.length >= 0) {
            for (String arg : args) {
                System.out.println(arg);
            }
        }

        System.out.println("args.length = " + args.length);
    }
}