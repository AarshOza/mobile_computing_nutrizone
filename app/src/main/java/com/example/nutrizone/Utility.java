package com.example.nutrizone;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    public static float getQuantity(String input, String searchString){
        String regex = ".*"+searchString+" ([0-9]*?)[^0-9].*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return Float.parseFloat(matcher.group(1));
        }
        return 0;
    }
}
