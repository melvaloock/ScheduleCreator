package sleeplessdevelopers.schedulecreator;

import java.lang.annotation.Target;

import javax.validation.Constraint;

@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
    String message() default "{sleeplessdevelopers.schedulecreator.Password.message}";
}

