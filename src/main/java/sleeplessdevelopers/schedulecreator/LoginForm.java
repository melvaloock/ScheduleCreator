package sleeplessdevelopers.schedulecreator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class LoginForm {

    @Email(message = "Please enter a valid email address")
    @NotNull
    private String username;

    @Length(min = 8, max = 256) // TODO: check max length with database limit
    @NotNull
    @Password(message = "Passwords must contain at least 1 number, 1 uppercase letter, and 1 special character")
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "LoginForm(username: " + this.username + ", Password: " + this.password + ")";
    }
}