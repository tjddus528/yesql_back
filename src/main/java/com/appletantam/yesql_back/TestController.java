package com.appletantam.yesql_back;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/sig")
    public String signUp() {
        return "done.";
    }
}
