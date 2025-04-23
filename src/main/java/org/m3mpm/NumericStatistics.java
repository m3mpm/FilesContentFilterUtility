package org.m3mpm;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Класс `NumericStatistics` предназначен для сбора и вывода статистических данных о целых числах и вещественных числах,
 * полученных в виде строк.  Он вычисляет количество, минимальное, максимальное значение, сумму и среднее значение.
 * Поддерживает режимы краткой и полной статистики.
 */
public class NumericStatistics implements StatisticsInterface {
    private static final int NUMBER_DIGITS_AFTER_POINT = 2;
    private static final MathContext MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_UP);

    private long count = 0;
    private BigDecimal min = null;
    private BigDecimal max = null;
    private BigDecimal sum = BigDecimal.ZERO;
    private final boolean shortStatistics;
    private final boolean fullStatistics;
    private long errorCount = 0;
    private final String typeName; // "целым числам" или "вещественным числам"

    /**
     * Конструктор класса `NumericStatistics`.
     *
     * @param shortStatistics  Определяет, выводить ли краткую статистику (только количество).
     * @param fullStatistics Определяет, выводить ли полную статистику (количество, минимум, максимум, сумму, среднее).
     * @param typeName Определяет числа
     */
    public NumericStatistics(boolean shortStatistics, boolean fullStatistics, String typeName) {
        this.shortStatistics = shortStatistics;
        this.fullStatistics = fullStatistics;
        this.typeName = typeName;
        if (!shortStatistics && !fullStatistics) {
            System.out.println("WARNING: Не выбрана ни краткая, ни полная статистика. Будет собрана только базовая статистика (количество).");
        }
    }

    /**
     * Обновляет статистику на основе входной строки, представляющей вещественное число.
     *
     * @param line Строка, содержащая вещественное число.
     * @throws NumberFormatException если строка не может быть преобразована в BigDecimal.
     */
    @Override
    public void updateStatistics(String line) {
        try {
            BigDecimal value = new BigDecimal(line);
            count++;
            sum = sum.add(value);

            if (min == null && max == null) {
                min = max = value;
            } else {
                if (value.compareTo(min) < 0) {
                    min = value;
                }
                if (value.compareTo(max) > 0) {
                    max = value;
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Некорректный формат числа: " + line);
            errorCount++;
        }
    }

    /**
     * Выводит статистику в консоль в зависимости от установленных флагов `shortStatistics` и `fullStatistics`.
     */
    @Override
    public void printStatistics() {
        if (count == 0 && errorCount == 0) return;

        System.out.println("Статистика по " + typeName + ":");
        System.out.println("Всего обработано строк: " + (count + errorCount));
        System.out.println("Количество ошибок: " + errorCount);

        if (shortStatistics) {
            System.out.println("Количество записанных элементов: " + count);
        }

        if (fullStatistics) {
            if (count > 0) {
                System.out.println("Количество записанных элементов: " + count);
                System.out.println("Минимальное значение: " + min);
                System.out.println("Максимальное значение: " + max);
                System.out.println("Сумма: " + sum);
                System.out.println("Среднее значение: " + getAvgDecimal());
            } else {
                System.out.println("Нет данных для вывода полной статистики.");
            }
        }
    }

    /**
     * Вычисляет среднее значение как BigDecimal с учетом необходимой точности.
     *
     * @return Среднее значение в формате BigDecimal.
     */
    private BigDecimal getAvgDecimal() {
        return sum.divide(BigDecimal.valueOf(count), MATH_CONTEXT).setScale(NUMBER_DIGITS_AFTER_POINT, RoundingMode.HALF_UP);
    }
}