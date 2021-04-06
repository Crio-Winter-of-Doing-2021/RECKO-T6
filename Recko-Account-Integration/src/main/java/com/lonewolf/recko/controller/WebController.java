package com.lonewolf.recko.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@CrossOrigin
public class WebController {

    public String renderWelcomePage() {
        return "index.html";
    }
}
