package org.m3mpm;

public class StringStatistics implements StatisticsInterface{
    private long count = 0;
    private Long minLength = null;
    private Long maxLength = null;
    private final boolean shortStatistics;
    private final boolean fullStatistics;

    public StringStatistics(boolean shortStatistics, boolean fullStatistics) {
        this.shortStatistics = shortStatistics;
        this.fullStatistics = fullStatistics;
    }

    @Override
    public void updateStatistics(String line) {
        long len = line.length();
        count++;
        if(minLength == null && maxLength == null) {
            minLength = maxLength = len;
        }
        if (len < minLength) {
            minLength = len;
        }
        if (len > maxLength) {
            maxLength = len;
        }
    }

    @Override
    public void printStatistics() {
        if (count == 0) return;

        if (shortStatistics || fullStatistics) {
            System.out.println("Статистика по строкам:");
        }
        if (shortStatistics) {
            System.out.println("Количество записанных элементов: " + count);
        }
        if (fullStatistics) {
            System.out.println("Количество записанных элементов: " + count);
            System.out.println("Минимальная длина: " + minLength);
            System.out.println("Максимальная длина: " + maxLength);
        }
    }
}
