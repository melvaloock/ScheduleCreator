package sleeplessdevelopers.schedulecreator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PasswordHashingTester {

    @Test
    public void passwordHashWorks() throws PasswordStorage.CannotPerformOperationException, PasswordStorage.InvalidHashException {
        String pass = "somepassword";
        String hash = PasswordStorage.createHash(pass);
        assertTrue(PasswordStorage.verifyPassword(pass, hash));
        assertFalse(PasswordStorage.verifyPassword("wrong", hash));
    }

}
