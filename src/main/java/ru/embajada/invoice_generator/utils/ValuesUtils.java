package ru.embajada.invoice_generator.utils;

import ru.embajada.invoice_generator.dto.PdfData;

public class ValuesUtils {

    public static String extractValue(String text, String key) {
        // Ищем начало ключа
        int index = text.indexOf(key);
        if (index == -1) return "";

        // Начало значения — сразу после ключа
        int start = index + key.length();

        // Обрезаем часть текста начиная с позиции после ключа
        String remainder = text.substring(start).stripLeading();

        // Разбиваем остаток текста на строки
        String[] lines = remainder.split("\\r?\\n");

        // Первая строка после ключа — и есть нужное значение
        return lines.length > 0 ? lines[0].trim() : "";
    }

    public static String parsePeriod(PdfData data) {
        if (data == null || data.getPeriod() == null || data.getPeriod().isBlank()) {
            return "Период не указан";
        }

        String[] parts = data.getPeriod().split("(?i)\\s*al\\s*"); // разделитель "al" с учётом регистра и пробелов

        if (parts.length != 2) {
            return data.getPeriod(); // если формат неожиданный — вернуть как есть
        }

        String from = DateFormatUtils.localizeMonth(parts[0].trim());
        String to = DateFormatUtils.localizeMonth(parts[1].trim());

        return "C " + from + " по " + to;
    }
}