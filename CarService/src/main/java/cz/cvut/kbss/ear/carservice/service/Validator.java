package cz.cvut.kbss.ear.carservice.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static final String USERNAME_REGEX = "^[A-Za-z]\\w{5,29}$";
    public static final String REGEX = "^[A-Za-z]\\w{2,60}$";

    public static boolean isUsernameValid(String username){
        Pattern p = Pattern.compile(USERNAME_REGEX);
        Matcher m = p.matcher(username);
        return m.matches();
    }

    public static boolean isNameValid(String name){
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(name);
        return m.matches();
    }
}
