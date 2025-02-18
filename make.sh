#!/bin/sh

echo "---Очистка проекта---"
./gradlew clean


echo "---Сборка проекта---"
./gradlew build
# Проверяем, была ли сборка успешной
if [ $? -ne 0 ]; then
    echo "---Сборка не удалась--"
    exit 1
fi


echo "---Копирование приложения в корневую папку---"
cp build/libs/util.jar ./


echo "---Запуск приложения---"
java -jar util.jar "$@"


echo "---Удаление приложения---"
rm util.jar
