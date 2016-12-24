package bett.blib.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author BeTT
 * @email betranthanh@gmail.com
 */
public class StringUtils {

    private StringUtils() {
        throw new AssertionError();
    }

	public static enum Mode {
	    ALPHA, ALPHANUMERIC, NUMERIC 
	}
	
	public static String generateRandomString(int length, Mode mode) {

		StringBuffer buffer = new StringBuffer();
		String characters = "";

		switch(mode){
		
		case ALPHA:
			characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
			break;
		
		case ALPHANUMERIC:
			characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
			break;
	
		case NUMERIC:
			characters = "1234567890";
		    break;
		}
		
		int charactersLength = characters.length();

		for (int i = 0; i < length; i++) {
			double index = Math.random() * charactersLength;
			buffer.append(characters.charAt((int) index));
		}
		return buffer.toString();
	}
	
	public static String generateRandomString(Mode mode) {
		return generateRandomString(10, mode);
	}

    /**
     * capitalize first letter
     *
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (str.isEmpty()) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
                .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

	public static boolean isEmpty(String input) {
		if (input == null)
			return true;
		if (input.trim().isEmpty())
			return true;
		return false;
	}

	public static boolean isNotEmpty(String input) {
		return !isEmpty(input);
	}

	public static String generateRandomString() {
		return generateRandomString(10, Mode.ALPHANUMERIC) + TimeUtils.getCurrentTimeInLong();
	}

	public static String ordinal(int i) {
		String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
		switch (i % 100) {
			case 11:
			case 12:
			case 13:
				return i + "th";
			default:
				return i + sufixes[i % 10];

		}
	}

	/**
	 * check the email address is valid or not.
	 *
	 * @param email pass email id in string
	 * @return true when its valid otherwise false
	 */
	public static boolean isEmailIdValid(String email) {
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches()) {
			return true;
		}

		return false;
	}
}
