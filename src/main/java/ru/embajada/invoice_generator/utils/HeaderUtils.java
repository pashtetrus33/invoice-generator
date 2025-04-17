package ru.embajada.invoice_generator.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class HeaderUtils {

    public static String createUtf8AttachmentHeader(String filename) {
        String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20"); // Пробелы
        return "attachment; filename*=UTF-8''" + encoded;
    }
}