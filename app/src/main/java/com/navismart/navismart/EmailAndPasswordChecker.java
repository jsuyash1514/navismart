package com.navismart.navismart;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailAndPasswordChecker {
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPasswordValid(final String password) {
        if(password.length()>=6) return true;
        else return false;
    }

}
