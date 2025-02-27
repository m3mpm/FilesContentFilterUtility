package org.m3mpm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Класс `IntegerStatistics` предназначен для сбора и вывода статистических данных о целых числах,
 * полученных в виде строк.  Он вычисляет количество, минимальное, максимальное значение, сумму и среднее значение.
 * Поддерживает режимы краткой и полной статистики.
 * Для представления чисел используется `BigInteger`, что позволяет работать с целыми числами произвольного размера.
 */
public class IntegerStatistics implements StatisticsInterface{
    private long count = 0;
    private BigInteger min = null;
    private BigInteger max = null;
    private BigInteger sum = BigInteger.ZERO;
    private final boolean shortStatistics;
    private final boolean fullStatistics;

    /**
     * Конструктор класса `IntegerStatistics`.
     *
     * @param shortStatistics  Определяет, выводить ли краткую статистику (только количество).
     * @param fullStatistics Определяет, выводить ли полную статистику (количество, минимум, максимум, сумму, среднее).
     */
    public IntegerStatistics(boolean shortStatistics, boolean fullStatistics) {
        this.shortStatistics = shortStatistics;
        this.fullStatistics = fullStatistics;
    }

    /**
     * Обновляет статистику на основе входной строки, представляющей целое число.
     *
     * @param line Строка, содержащая целое число.
     * @throws NumberFormatException если строка не может быть преобразована в BigInteger.
     */
    @Override
    public void updateStatistics(String line) throws NumberFormatException {
        try {
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
        } catch (NumberFormatException e) {
            throw new NumberFormatException("ERROR: Некорректный тип данны: " + line);
        }
    }

    /**
     * Выводит статистику в консоль в зависимости от установленных флагов `shortStatistics` и `fullStatistics`.
     */
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
            System.out.println("Среднее значение: " + avrgDecimal);
        }
    }

    /**
     * Вычисляет среднее значение как BigDecimal, используя BigInteger для вычислений и учитывая необходимую точность.
     * Преобразует сумму и количество в BigDecimal для деления, чтобы получить дробный результат.
     * Устанавливает масштаб результата (количество знаков после запятой) и применяет округление.
     *
     * @return Среднее значение в формате BigDecimal.
     */
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
