package org.m3mpm;

/**
 * `DataWriterException` - пользовательское исключение, предназначенное для сигнализации об ошибках,
 * возникающих в процессе записи данных в файлы с использованием класса `DataWriter`.
 */
public class DataWriterException extends Exception {
    /**
     * Конструктор класса `DataWriterException` с сообщением об ошибке.
     *
     * @param message Сообщение, описывающее причину возникновения исключения.
     */
    public DataWriterException(String message) {
        super(message);
    }

    /**
     * Конструктор класса `DataWriterException` с сообщением об ошибке и причиной исключения.
     * Используется для передачи информации о нижележащем исключении, вызвавшем данное исключение.
     *
     * @param message Сообщение, описывающее причину возникновения исключения.
     * @param cause Исключение, вызвавшее данное исключение (причина ошибки).
     */
    public DataWriterException(String message, Throwable cause) {
        super(message, cause);
    }
}
