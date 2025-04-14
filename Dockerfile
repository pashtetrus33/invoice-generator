# Используем официальный образ OpenJDK с Alpine Linux для минимального размера
FROM eclipse-temurin:17-jdk-alpine

# Создаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR-файл в контейнер
COPY target/*.jar app.jar

# Устанавливаем шрифты (если нужно)
RUN apk add --no-cache fontconfig ttf-dejavu && \
    mkdir -p /usr/share/fonts/truetype/custom && \
    apk add --no-cache msttcorefonts-installer && \
    update-ms-fonts && \
    fc-cache -f

# Копируем кастомные шрифты (если используются)
COPY src/main/resources/fonts/*.ttf /usr/share/fonts/truetype/custom/

# Открываем порт, который использует приложение
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]