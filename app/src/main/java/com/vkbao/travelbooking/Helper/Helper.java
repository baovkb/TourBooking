package com.vkbao.travelbooking.Helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    public static String getCurrentTimeString() {
        return new SimpleDateFormat("HH:mm:ss dd/MM/yyy").format(new Date());
    }
}
