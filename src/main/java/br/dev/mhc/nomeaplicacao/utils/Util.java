package br.dev.mhc.nomeaplicacao.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class Util {

    public static boolean isIntegerNumber(String text) {
        if (isNull(text)) {
            return false;
        }
        return text.matches("-?\\d+");
    }

    public static boolean isNumber(String text) {
        if (isNull(text)) {
            return false;
        }
        return text.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    public static boolean isValidEmail(String email) {
        if (isNull(email) || email.isEmpty()) {
            return false;
        }
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
