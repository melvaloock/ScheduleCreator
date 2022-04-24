package sleeplessdevelopers.schedulecreator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, AccountCreationForm> {
    
    @Override
    public boolean isValid(final AccountCreationForm account, ConstraintValidatorContext context) {
        String password = account.getPassword();
        String passwordMatch = account.getPasswordMatch();

        if (password.equals(passwordMatch)) {
            return true;
        }

        return false;
    }
}
