package com.example.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @GetMapping({"/", "/login"})
    public String loginPage() {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage() {
        return "registration";
    }

    @GetMapping("/oops")
    public String errorPage() {
        return "oops";
    }

    @GetMapping("/email-page")
    public String emailPage() {
        return "email-page";
    }

    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam String username,
                            @RequestParam String email,
                            @RequestParam String message,
                            Model model) {
        // TODO: реализовать сохранение или отправку сообщения
        model.addAttribute("message", "Email отправлен!");
        return "email-page";
    }

}
