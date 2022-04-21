package sleeplessdevelopers.schedulecreator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {
    
    @GetMapping("/")
    public String index() {
        return "Hello World!";
    }
}
