package com.vkbao.travelbooking.Helper;

import com.vkbao.travelbooking.Helper.HMac.HMacUtil;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Helper {
    public static String getCurrentTimeString() {
        return new SimpleDateFormat("HH:mm:ss dd/MM/yyy").format(new Date());
    }

    public static long convertToUnix(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyy");
        return simpleDateFormat.parse(time).getTime();
    }

    public static String removeAccent(String raw) {
        String normalizedString = Normalizer.normalize(raw, Normalizer.Form.NFD);

        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(normalizedString).replaceAll("");

        result = result.replace("Đ", "d");
        result = result.replace("đ", "d");

        return result;
    }

    public static String getMac(String key, String data) throws NoSuchAlgorithmException, InvalidKeyException {
        return HMacUtil.HMacHexStringEncode(HMacUtil.HMACSHA256, key, data);
    }
}
