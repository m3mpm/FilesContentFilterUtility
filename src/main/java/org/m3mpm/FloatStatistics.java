package org.m3mpm;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Класс `FloatStatistics` предназначен для сбора и вывода статистических данных о вещественных числах,
 * полученных в виде строк.  Он вычисляет количество, минимальное, максимальное значение, сумму и среднее значение.
 * Поддерживает режимы краткой и полной статистики.
 */
public class FloatStatistics implements StatisticsInterface{
    private long count = 0;
    private BigDecimal min = null;
    private BigDecimal max = null;
    private BigDecimal sum = BigDecimal.ZERO;
    private final boolean shortStatistics;
    private final boolean fullStatistics;

    /**
     * Конструктор класса `FloatStatistics`.
     *
     * @param shortStatistics  Определяет, выводить ли краткую статистику (только количество).
     * @param fullStatistics Определяет, выводить ли полную статистику (количество, минимум, максимум, сумму, среднее).
     */
    public FloatStatistics(boolean shortStatistics, boolean fullStatistics) {
        this.shortStatistics = shortStatistics;
        this.fullStatistics = fullStatistics;
    }

    /**
     * Обновляет статистику на основе входной строки, представляющей вещественное число.
     *
     * @param line Строка, содержащая вещественное число.
     * @throws NumberFormatException если строка не может быть преобразована в BigDecimal.
     */
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

    /**
     * Выводит статистику в консоль в зависимости от установленных флагов `shortStatistics` и `fullStatistics`.
     */
    @Override
    public void printStatistics() {
        if (count == 0) return;

        if (shortStatistics || fullStatistics) {
            System.out.println("Статистика по вещественным числам:");
        }
        if (shortStatistics) {
            System.out.println("Количество записанных элементов: " + count);
        }
        if (fullStatistics) {
            System.out.println("Количество записанных элементов: " + count);
            System.out.println("Минимальное значение: " + min);
            System.out.println("Максимальное значение: " + max);
            System.out.println("Сумма: " + sum);
            System.out.println("Среднее значение: " + getAvrgDecimal());
        }
    }

    private BigDecimal getAvrgDecimal(){
        int countDigitsInSum = sum.precision();
        int countDigitsInCount = String.valueOf(count).length();
        int  numberDigitsAfterPoint = 2;
        int precision = Math.max(countDigitsInSum,countDigitsInCount) + numberDigitsAfterPoint;
        MathContext mathContext = new MathContext(precision, RoundingMode.HALF_UP);
        return (sum.divide(BigDecimal.valueOf(count), mathContext));
    }
}
