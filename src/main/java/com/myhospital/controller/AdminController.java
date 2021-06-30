package com.myhospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping(value = "/panel")
    public String showPanel() {
        return "admin_panel";
    }
}
