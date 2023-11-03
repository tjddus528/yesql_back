package com.appletantam.yesql_back;

import com.appletantam.yesql_back.config.antlr.AntlrService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

@RestController
@RequestMapping("/manage")
public class TestController {

    @Autowired
    AntlrService antlrService;
    @GetMapping("/antlr")
    public ArrayList antlr() throws IOException {
        ArrayList arrayList = antlrService.getData("http://localhost:9000/antlr/run?sql=SELECT loan_number FROM borrower WHERE customer_name = (SELECT customer_name FROM depositor WHERE account_number = \"A-215\");");
        return arrayList;
    }

}
