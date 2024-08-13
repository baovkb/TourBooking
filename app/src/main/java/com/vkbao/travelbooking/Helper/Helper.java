package com.vkbao.travelbooking.Helper;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Helper {
    public static String getCurrentTimeString() {
        return new SimpleDateFormat("HH:mm:ss dd/MM/yyy").format(new Date());
    }

    public static String removeAccent(String raw) {
        String normalizedString = Normalizer.normalize(raw, Normalizer.Form.NFD);

        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(normalizedString).replaceAll("");

        result = result.replace("Đ", "d");
        result = result.replace("đ", "d");

        return result;
    }
}
