package sleeplessdevelopers.schedulecreator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.websocket.OnMessage;

import org.hibernate.validator.constraints.Length;

public class LoginForm {

    @Email(message = "Please enter a valid email address")
    @NotNull(message = "Please enter an email address")
    private String username;

    @NotNull(message = "Please enter a password")
    @Password(message = "Incorrect username or password")
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