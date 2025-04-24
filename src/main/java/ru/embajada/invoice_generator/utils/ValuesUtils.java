package ru.embajada.invoice_generator.utils;

import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import ru.embajada.invoice_generator.dto.PdfData;

import java.io.IOException;

public class ValuesUtils {

    public static String extractValue(String text, String key, String endKey) {
        // Ищем начало ключа
        int index = text.indexOf(key);
        if (index == -1) return "";

        // Начинаем сразу после ключа
        int start = index + key.length();
        if (start >= text.length()) return "";

        // Обрезаем остаток текста после ключа
        String remainder = text.substring(start).stripLeading();

        if (endKey != null && !endKey.isBlank()) {
            int endIndex = remainder.indexOf(endKey);
            if (endIndex != -1) {
                remainder = remainder.substring(0, endIndex).trim();
            }
        } else {
            // Если нет endKey, берем только первую строку
            String[] lines = remainder.split("\\r?\\n");
            return lines.length > 0 ? lines[0].trim() : "";
        }

        return remainder;
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

    public static void fillField(PDAcroForm form, String fieldName, String value) throws IOException {
        PDField field = form.getField(fieldName);
        if (field != null) {
            field.setValue(value != null ? value : "");
        } else {
            System.out.println("Поле не найдено: " + fieldName);
        }
    }
}