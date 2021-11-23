package com.cloud.traceApp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {
    @GetMapping("/message")
    public String getMessage() {
        return "hello";
    }
}
