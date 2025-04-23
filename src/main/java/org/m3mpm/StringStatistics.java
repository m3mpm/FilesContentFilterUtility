package org.m3mpm;


/**
 * Класс `StringStatistics` предназначен для сбора и вывода статистических данных о строках.
 * Он вычисляет количество, минимальную и максимальную длину строк.
 * Поддерживает режимы краткой и полной статистики.
 */
public class StringStatistics implements StatisticsInterface {
    private long count = 0;
    private Long minLength = null;
    private Long maxLength = null;
    private final boolean shortStatistics;
    private final boolean fullStatistics;
    private long errorCount = 0;

    /**
     * Конструктор класса `StringStatistics`.
     *
     * @param shortStatistics Определяет, выводить ли краткую статистику (только количество).
     * @param fullStatistics  Определяет, выводить ли полную статистику (количество, минимум, максимум).
     */
    public StringStatistics(boolean shortStatistics, boolean fullStatistics) {
        this.shortStatistics = shortStatistics;
        this.fullStatistics = fullStatistics;
        if (!shortStatistics && !fullStatistics) {
            System.out.println("WARNING: Не выбрана ни краткая, ни полная статистика. Будет собрана только базовая статистика (количество).");
        }
    }

    /**
     * Обновляет статистику на основе входной строки, вычисляя ее длину.
     *
     * @param line Строка для обработки.
     */
    @Override
    public void updateStatistics(String line) {
        if (line == null) {
            System.err.println("ERROR: Получена пустая строка (null).");
            errorCount++;
            return;
        }

        long len = line.length();
        count++;

        if (minLength == null && maxLength == null) {
            minLength = maxLength = len;
        } else {
            if (len < minLength) {
                minLength = len;
            }
            if (len > maxLength) {
                maxLength = len;
            }
        }
    }

    /**
     * Выводит статистику в консоль в зависимости от установленных флагов `shortStatistics` и `fullStatistics`.
     */
    @Override
    public void printStatistics() {
        if (count == 0 && errorCount == 0) return;

        System.out.println("Статистика по строкам:");
        System.out.println("Всего обработано строк: " + (count + errorCount));
        System.out.println("Количество ошибок: " + errorCount);

        if (shortStatistics) {
            System.out.println("Количество записанных элементов: " + count);
        }

        if (fullStatistics) {
            if (count > 0) {
                System.out.println("Количество записанных элементов: " + count);
                System.out.println("Минимальная длина: " + minLength);
                System.out.println("Максимальная длина: " + maxLength);
            } else {
                System.out.println("Нет данных для вывода полной статистики.");
            }
        }
    }
}