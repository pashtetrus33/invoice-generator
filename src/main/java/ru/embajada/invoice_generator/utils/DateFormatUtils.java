package ru.embajada.invoice_generator.utils;

import java.util.Map;

public class DateFormatUtils {

    private static final Map<String, String> MONTH_MAP = Map.ofEntries(
            Map.entry("ene", "янв"),
            Map.entry("feb", "фев"),
            Map.entry("mar", "мар"),
            Map.entry("abr", "апр"),
            Map.entry("may", "май"),
            Map.entry("jun", "июн"),
            Map.entry("jul", "июл"),
            Map.entry("ago", "авг"),
            Map.entry("sep", "сен"),
            Map.entry("oct", "окт"),
            Map.entry("nov", "ноя"),
            Map.entry("dic", "дек")
    );

    private static final Map<String, String> RUSSIAN_MONTH_TO_NUMBER = Map.ofEntries(
            Map.entry("янв", "01"),
            Map.entry("фев", "02"),
            Map.entry("мар", "03"),
            Map.entry("апр", "04"),
            Map.entry("май", "05"),
            Map.entry("июн", "06"),
            Map.entry("июл", "07"),
            Map.entry("авг", "08"),
            Map.entry("сен", "09"),
            Map.entry("окт", "10"),
            Map.entry("ноя", "11"),
            Map.entry("дек", "12")
    );

    private DateFormatUtils() {
        // Приватный конструктор — утильный класс не создаётся
    }

    /**
     * Заменяет испанскую аббревиатуру месяца на русскую в строке формата dd-MMM-yyyy.
     * Пример: "06-feb-2025" → "06-фев-2025"
     */
    public static String localizeMonth(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;

        String[] parts = dateStr.split("[\\s\\-]+");
        if (parts.length < 2 || parts.length > 3) return dateStr;

        String day = parts[0];
        String month = parts[1].toLowerCase();
        String year = parts.length == 3 ? parts[2] : "";

        String monthRus = MONTH_MAP.getOrDefault(month, month);

        return parts.length == 3
                ? String.format("%s-%s-%s", day, monthRus, year)
                : String.format("%s %s", day, monthRus);
    }

    /**
     * Преобразует дату с русской аббревиатурой месяца в числовой формат.
     * Пример: "06-фев-2025" → "06.02.2025"
     */
    public static String convertRussianMonthToNumeric(String dateStr) {
        if (dateStr == null) return null;

        String[] parts = dateStr.split("-");
        if (parts.length != 3) return dateStr;

        String day = parts[0];
        String month = parts[1].toLowerCase();
        String year = parts[2];

        String monthNum = RUSSIAN_MONTH_TO_NUMBER.getOrDefault(month, "??");

        return String.format("%s.%s.%s", day, monthNum, year);
    }
}