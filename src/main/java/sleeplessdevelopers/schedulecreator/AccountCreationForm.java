package sleeplessdevelopers.schedulecreator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@PasswordMatch(message = "Passwords do not match")
public class AccountCreationForm {
    
    @Email(message = "Please enter a valid email address")
    @NotNull
    private String username;

    @Length(min = 8, max = 256) // TODO: check max length with database limit
    @NotNull
    @Password(message = "Please enter a valid password")
    private String password;

    @Length(min = 8, max = 256) // TODO: check max length with database limit
    @NotNull
    @Password(message = "Please enter a valid password")
    private String passwordMatch;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordMatch() {
        return passwordMatch;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordMatch(String passwordMatch) {
        this.passwordMatch = passwordMatch;
    }

    public String toString() {
        return "AccountCreationForm(username: " + this.username + ", Password: " + this.password + ", PasswordMatch: " + this.passwordMatch + ")";
    }
}
