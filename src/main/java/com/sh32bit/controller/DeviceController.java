package com.sh32bit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/device")
public class DeviceController {

    @GetMapping
    public String getDeviceInfo(@RequestHeader("User-Agent") String userAgent) {
        return userAgent;
    }
}
