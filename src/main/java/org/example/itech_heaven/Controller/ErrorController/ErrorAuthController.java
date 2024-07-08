package org.example.itech_heaven.Controller.ErrorController;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ErrorAuthController implements ErrorController {

    @GetMapping("/403")
    public String error403(){
        return "403";
    }

}
