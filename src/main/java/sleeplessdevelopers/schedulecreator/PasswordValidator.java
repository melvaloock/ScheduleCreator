package sleeplessdevelopers.schedulecreator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(final String password, ConstraintValidatorContext context) {
        Pattern symbolReg = Pattern.compile("[^a-zA-Z0-9]");
		Matcher symMatch = symbolReg.matcher(password);
		boolean checkTrue = symMatch.find();
		//string of symbols and else if for checking for said symbols
		//provided by
		// https://codingface.com/how-to-check-string-contains-special-characters-in-java/#What_is_a_Special_Character
		boolean hasNum = false;
		boolean hasUpper = false;
		boolean hasSym = false;
		char[] passwordArray = password.toCharArray();
		for (int i = 0; i < passwordArray.length; i++){
			if (Character.isSpaceChar(i)){
				return false;
			}
			else if (Character.isDigit(password.charAt(i))){ //numbers
				hasNum = true;
			}
			else if(Character.isUpperCase(password.charAt(i))) { //uppercase letters
				hasUpper = true;
			}
		}

		if (checkTrue){ //symbols, ascii range or list of symbols to check
			hasSym = true;
		}

		if (hasNum == true && hasSym == true && hasUpper == true){
			return true;
		}

		return false;
    }
}
    
