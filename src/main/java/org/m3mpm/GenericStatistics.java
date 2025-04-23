package org.m3mpm;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Класс для сбора и анализа статистики по значениям различных типов.
 *
 * @param <T> тип значений, которые будут обрабатываться (например, Integer, Float, String).
 */
class GenericStatistics<T> implements StatisticsInterface {
    private static final int NUMBER_DIGITS_AFTER_POINT = 2;
    private static final MathContext MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_UP);

    private long count = 0;
    private T min = null;
    private T max = null;
    private BigDecimal sum = BigDecimal.ZERO;
    private final boolean shortStatistics;
    private final boolean fullStatistics;
    private final Class<T> type;

    /**
     * Конструктор для инициализации объекта GenericStatistics.
     *
     * @param type тип значений, которые будут обрабатываться
     * @param shortStatistics флаг для вывода краткой статистики
     * @param fullStatistics флаг для вывода полной статистики
     */
    public GenericStatistics(Class<T> type, boolean shortStatistics, boolean fullStatistics) {
        this.type = type;
        this.shortStatistics = shortStatistics;
        this.fullStatistics = fullStatistics;
    }

    /**
     * Обновляет статистику на основе входной строки.
     *
     * @param line строка, содержащая значение для обработки
     * @throws NumberFormatException если строка не может быть преобразована в число
     */
    @Override
    public void updateStatistics(String line) {
        try {
            if (type == Integer.class || type == Float.class) {
                // Обработка для чисел
                BigDecimal value = new BigDecimal(line);
                count++;
                sum = sum.add(value);
                if (min == null && max == null) {
                    min = (T) value;
                    max = (T) value;
                } else {
                    if (value.compareTo((BigDecimal) min) < 0) {
                        min = (T) value;
                    }
                    if (value.compareTo((BigDecimal) max) > 0) {
                        max = (T) value;
                    }
                }
            } else if (type == String.class) {
                // Обработка для строк
                long len = line.length();
                count++;
                if (min == null && max == null) {
                    min = (T) Long.valueOf(len);
                    max = (T) Long.valueOf(len);
                } else {
                    if (len < (Long) min) {
                        min = (T) Long.valueOf(len);
                    }
                    if (len > (Long) max) {
                        max = (T) Long.valueOf(len);
                    }
                }
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("ERROR: Некорректный тип данных: " + line);
        }
    }

    /**
     * Печатает статистику на экран.
     */
    @Override
    public void printStatistics() {
        if (count == 0) return;

        if (shortStatistics || fullStatistics) {
            System.out.println("Статистика по типу: " + type.getSimpleName());
        }
        if (shortStatistics) {
            System.out.println("Количество записанных элементов: " + count);
        }
        if (fullStatistics) {
            if (type == Integer.class || type == Float.class) {
                System.out.println("Количество записанных элементов: " + count);
                System.out.println("Минимальное значение: " + min);
                System.out.println("Максимальное значение: " + max);
                System.out.println("Сумма: " + sum);
                System.out.println("Среднее значение: " + getAvgDecimal());
            } else if (type == String.class) {
                System.out.println("Количество записанных элементов: " + count);
                System.out.println("Минимальная длина: " + min);
                System.out.println("Максимальная длина: " + max);
            }
        }
    }

    /**
     * Вычисляет среднее значение на основе суммы и количества значений.
     *
     * @return среднее значение в виде BigDecimal
     */
    private BigDecimal getAvgDecimal() {
        return sum.divide(BigDecimal.valueOf(count), MATH_CONTEXT).setScale(NUMBER_DIGITS_AFTER_POINT, RoundingMode.HALF_UP);
    }

}
