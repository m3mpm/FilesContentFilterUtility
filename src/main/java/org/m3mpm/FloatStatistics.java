package org.m3mpm;

import java.math.BigDecimal;

public class FloatStatistics implements StatisticsInterface{
    private long count = 0;
    private BigDecimal min = null;
    private BigDecimal max = null;
    private BigDecimal sum = BigDecimal.ZERO;
    private final boolean shortStatistics;
    private final boolean fullStatistics;

    public FloatStatistics(boolean shortStatistics, boolean fullStatistics) {
        this.shortStatistics = shortStatistics;
        this.fullStatistics = fullStatistics;
    }

    public void updateStatistics(String line) {
        BigDecimal value = new BigDecimal(line);
        count++;
        sum = sum.add(value);
        if (min == null && max == null) {
            min = max = value;
        }
        if (value.compareTo(min) < 0) {
            min = value;
        }
        if (value.compareTo(max) > 0) {
            max = value;
        }
    }

    @Override
    public void printStatistics() {
        if (count == 0) return;

        if (shortStatistics || fullStatistics) {
            System.out.println("Статистика по вещественным числам:");
        }
        if (shortStatistics) {
            System.out.println("Количество записей: " + count);
        }
        if (fullStatistics) {
            System.out.println("Количество записей: " + count);
            System.out.println("Минимальное значение: " + min);
            System.out.println("Максимальное значение: " + max);
            System.out.printf("Сумма: " + sum);
            System.out.println("Среднее значение: " + (sum.divide(BigDecimal.valueOf(count), java.math.RoundingMode.HALF_UP)));
        }

    }
}
