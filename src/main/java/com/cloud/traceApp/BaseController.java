package com.cloud.traceApp;

import com.cloud.aws.sqs.SQSSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {
    private final SQSSender sqsSender;

    public BaseController(SQSSender sqsSender) {
        this.sqsSender = sqsSender;
    }

    @GetMapping("/message")
    public String getMessage() {
        return "hello";
    }

    @PostMapping("/message")
    public void postMessage(@RequestBody String message) {
        sqsSender.sendMessage(message);
    }
}
