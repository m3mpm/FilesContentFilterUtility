.PHONY: all clear-screen clean build copy run clean-up

# Определяем переменные
GRADLE=./gradlew
JAR=util.jar
BUILD_DIR=build/libs

# Цель по умолчанию
all: clear-screen clean build copy run

# Очистка экрана
clear-screen:
	@clear

# Очистка проекта
clean:
	$(GRADLE) clean

# Сборка проекта
build:
	$(GRADLE) build

# Копирование JAR файла
copy:
	cp $(BUILD_DIR)/$(JAR) ./

# Запуск JAR файла
run:
	java -jar $(JAR) $(ARGS)

# Удаление JAR файла
clean-up: clear-screen
	rm -f $(JAR)
	rm *floats.txt *integers.txt *strings.txt
