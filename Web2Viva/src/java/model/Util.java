package model;

public class Util {

    public static String generateCode() {
        int code = (int) (Math.random() * 1000000);
        return String.format("%06d", code);
    }

    public static boolean isEmailValid(String email) {
        return email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }

    public static boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$");
    }

    public static boolean isValidMobile(String number) {
        String regex = "^07[01245678]{1}[0-9]{7}$";
        return number.matches(regex);
    }

    public static boolean isPostalCodeValid(String code) {
        return code.matches("^\\d{4,5}$");
    }
    
    public static boolean isInteger(String value) {
        return value.matches("^\\d+$");
    }
    public static boolean isDouble(String value) {
        return value.matches("^\\d+(\\.\\d{2})?$");
    }
    
    
}
