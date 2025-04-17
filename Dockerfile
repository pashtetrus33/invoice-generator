# Используем официальный образ OpenJDK с Alpine Linux для минимального размера
FROM eclipse-temurin:17-jdk-alpine

# Создаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR-файл в контейнер
COPY target/*.jar app.jar


# Открываем порт, который использует приложение
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]