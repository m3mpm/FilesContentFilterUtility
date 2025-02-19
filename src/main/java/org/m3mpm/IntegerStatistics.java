package org.m3mpm;

import java.math.BigInteger;

public class IntegerStatistics implements StatisticsInterface{
    private long count = 0;
    private BigInteger min = null;
    private BigInteger max = null;
    private BigInteger sum = BigInteger.ZERO;
    private final boolean shortStatistics;
    private final boolean fullStatistics;

    public IntegerStatistics(boolean shortStatistics, boolean fullStatistics) {
        this.shortStatistics = shortStatistics;
        this.fullStatistics = fullStatistics;
    }

    @Override
    public void updateStatistics(String line) {
        BigInteger value = new BigInteger(line);
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
            System.out.println("Статистика по целым числам:");
        }
        if (shortStatistics) {
            System.out.println("Количество записей : " + count);
        }
        if (fullStatistics) {
            System.out.println("Количество записей: " + count);
            System.out.println("Минимальное значение: " + min);
            System.out.println("Максимальное значение: " + max);
            System.out.println("Сумма: " + sum);
            System.out.println("Среднее значение: " + (sum.divide(BigInteger.valueOf(count)))); // Пример деления (может потребоваться округление)
        }
    }
}
