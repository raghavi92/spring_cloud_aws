package com.cloud.traceApp;

import com.cloud.aws.sqs.SQSSender;
import org.springframework.web.bind.annotation.*;

@RestController
public class BaseController {
    private final SQSSender sqsSender;

    public BaseController(SQSSender sqsSender) {
        this.sqsSender = sqsSender;
    }

    @GetMapping("/message")
    public String getMessage(@RequestParam String message) {
        return message;
    }

    @PostMapping("/message")
    public void postMessage(@RequestBody String message) {
        sqsSender.sendMessageWithTracingAttribute(message);
    }
}
