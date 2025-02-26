package org.m3mpm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

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
            System.out.println("Количество записанных элементов: " + count);
        }
        if (fullStatistics) {
            System.out.println("Количество записанных элементов: " + count);
            System.out.println("Минимальное значение: " + min);
            System.out.println("Максимальное значение: " + max);
            System.out.println("Сумма: " + sum);
            BigDecimal avrgDecimal = getAvrgDecimal();
            System.out.println("Среднее значение: " + avrgDecimal); // Пример деления (может потребоваться округление)
        }
    }

    private BigDecimal getAvrgDecimal() {
        BigDecimal sumDecimal = new BigDecimal(sum);
        BigDecimal countDecimal = BigDecimal.valueOf(count);
        int countDigitsInSum = String.valueOf(sum).length();
        int countDigitsInCount = String.valueOf(count).length();
        int  numberDigitsAfterPoint = 2;
        int precision = Math.max(countDigitsInSum,countDigitsInCount) + numberDigitsAfterPoint;
        MathContext mathContext = new MathContext(precision, RoundingMode.HALF_UP);
        BigDecimal avrgDecimal = sumDecimal.divide(countDecimal, mathContext);
        avrgDecimal = avrgDecimal.setScale(numberDigitsAfterPoint, RoundingMode.HALF_UP);
        return avrgDecimal;
    }
}
